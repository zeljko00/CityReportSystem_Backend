package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.CityOfficialDAO;
import is.cityreportsystem.DAO.CityServiceDAO;
import is.cityreportsystem.DAO.ReportDAO;
import is.cityreportsystem.model.*;
import is.cityreportsystem.model.DTO.CitizenDTO;
import is.cityreportsystem.model.DTO.PageDTO;
import is.cityreportsystem.model.DTO.ReportDTO;
import is.cityreportsystem.model.DTO.ReportRequest;
import is.cityreportsystem.model.enums.ReportState;
import is.cityreportsystem.services.CitizenService;
import is.cityreportsystem.services.ReportImageService;
import is.cityreportsystem.services.ReportService;
import is.cityreportsystem.services.ReportTypeService;
import is.cityreportsystem.util.LoggerBean;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    @Value("${reports.images.repository}")
    private String imagesRepo;
    private final ModelMapper modelMapper;

    private final ReportDAO reportDAO;
    private final CitizenService citizenService;
    private final CityServiceDAO cityServiceDAO;
    private final CityOfficialDAO cityOfficialDAO;
    private final ReportImageService reportImageService;
    private final ReportTypeService reportTypeService;
    private HashMap<String, List<Tuple>> uploadedImages = new HashMap<String, List<Tuple>>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat dateFormatLocale = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private final LoggerBean loggerBean;

    public ReportServiceImpl(ModelMapper modelMapper, ReportDAO reportDAO, CitizenService citizenService, CityServiceDAO cityServiceDAO, CityOfficialDAO cityOfficialDAO, ReportImageService reportImageService, ReportTypeService reportTypeService, LoggerBean loggerBean) {
        this.modelMapper = modelMapper;
        this.reportDAO = reportDAO;
        this.citizenService = citizenService;
        this.cityServiceDAO = cityServiceDAO;
        this.cityOfficialDAO = cityOfficialDAO;
        this.reportImageService = reportImageService;
        this.reportTypeService = reportTypeService;
        this.loggerBean = loggerBean;
    }

    public boolean saveImage(byte[] data, String id) {
        System.out.println(id);
        try {
            String[] tokens = id.split("--");
            String random = tokens[0];
            synchronized (uploadedImages) {
                if (!uploadedImages.containsKey(random))
                    uploadedImages.put(random, new ArrayList<Tuple>());
                else
                    System.out.println("already contains");
                Tuple temp = new Tuple();
                System.out.println(tokens[1]);
                temp.setId(tokens[1]);
                temp.setData(data);
                uploadedImages.get(random).add(temp);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            loggerBean.logError(e);
            return false;
        }
    }

    public void deleteImage(String id) {
        try {
            String[] tokens = id.split("--");
            String random = tokens[0];
            synchronized (uploadedImages) {
                List<Tuple> imgs = uploadedImages.get(random);
                Tuple toDelete = null;
                for (Tuple t : imgs)
                    if (t.getId().equals(tokens[1]))
                        toDelete = t;
                System.out.println("deleted " + toDelete.getId());
                imgs.remove(toDelete);
            }
        } catch (Exception e) {
            e.printStackTrace();
            loggerBean.logError(e);
        }
    }

    public ReportDTO createReport(ReportRequest report) {
        Report reportEntity = modelMapper.map(report, Report.class);
        reportEntity.setDate(dateFormat.format(new Date()));
        reportEntity.setState(ReportState.RECEIVED);
        reportEntity.setId(-1l);
        reportEntity.setRequiredInfo(false);
        try {
            ReportType type = reportTypeService.getByName(report.getType());

            if (type != null) {
                CityService cityService = type.getResponsibleService();
                if (cityService != null)
                    reportEntity.setRecepient(cityService);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long tempId = report.getId();
        System.out.println(tempId);
        System.out.println("creator " + report.getCreator());
        if (report.getCreator() > 0) {
            CitizenDTO citizen = citizenService.findCitizenById(report.getCreator());
            reportEntity.setCreator(modelMapper.map(citizen, Citizen.class));
        }
        Report result = reportDAO.save(reportEntity);
        synchronized (uploadedImages) {
            if (uploadedImages.get(Long.toString(tempId)) != null) {
                System.out.println("Images exist!");
                List<Tuple> images = uploadedImages.get(Long.toString(tempId));
                uploadedImages.remove(Long.toString(tempId));
                int count = 1;
                for (Tuple t : images) {
                    ReportImage reportImage = new ReportImage();
                    reportImage.setReport(result);
                    reportImage.setName(result.getId() + "_" + count++ + ".jpg");
                    reportImageService.addImage(reportImage);
                    String path = imagesRepo + File.separator + reportImage.getName();
                    File file = new File(path);
                    try {
                        file.createNewFile();
                        Files.write(Paths.get(path), t.getData());
                        System.out.println("saved");
                    } catch (Exception e) {
                        e.printStackTrace();
                        loggerBean.logError(e);
                    }
                }
            } else
                System.out.println("no images");
        }

        return modelMapper.map(result, ReportDTO.class);
    }

    public List<ReportDTO> getReportsByAuthor(long id) {
        CitizenDTO citizenDTO = citizenService.findCitizenById(id);
        Citizen citizen = modelMapper.map(citizenDTO, Citizen.class);
        List<Report> reports = reportDAO.findReportsByCreator(citizen);
        List<ReportDTO> result = new ArrayList<ReportDTO>();
        if (reports != null)
            for (Report report : reports)
                result.add(modelMapper.map(report, ReportDTO.class));
        return result;
    }

    public boolean requireInfo(long id, String required) {
        try {
            Report report = reportDAO.findById(id).get();
            report.setRequiredAdditionalInfo(required);
            report.setRequiredInfo(true);
            reportDAO.save(report);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            loggerBean.logError(e);
            return false;
        }
    }

    public boolean provideInfo(long id, String required) {
        try {
            Report report = reportDAO.findById(id).get();
            report.setProvidedAdditionalInfo(required);
            report.setRequiredInfo(false);
            reportDAO.save(report);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            loggerBean.logError(e);
            return false;
        }
    }

    public PageDTO<ReportDTO> getReports(long userId, long departmentId, Pageable pageable, String search, String typeFilter, String stateFilter) {
        PageDTO<ReportDTO> result = new PageDTO<>();
        try {
            CityOfficial user = cityOfficialDAO.findById(userId).get();
            if (user == null)
                throw new Exception();
            CityService cityService = cityServiceDAO.findById(departmentId).get();
            if (cityService == null)
                throw new Exception();
            if (user.getDepartment().getId() != departmentId)
                throw new Exception();
            List<String> types = cityService.getReportTypes().stream().map(t -> t.getName()).collect(Collectors.toList());
            Page<Report> page = null;
            if ("-".equals(search)) {
                if ("all".equals(typeFilter)) {
                    if ("all".equals(stateFilter)) {
                        page = reportDAO.findReportsByTypeIn(pageable, types);
                    } else {
                        page = reportDAO.findReportsByTypeInAndState(pageable, types, ReportState.valueOf(stateFilter));
                    }
                } else {
                    if ("all".equals(stateFilter)) {
                        page = reportDAO.findReportsByType(pageable, typeFilter);
                    } else {
                        page = reportDAO.findReportsByTypeAndState(pageable, typeFilter, ReportState.valueOf(stateFilter));
                    }
                }
            } else {
                if ("all".equals(typeFilter)) {
                    if ("all".equals(stateFilter)) {
                        page = reportDAO.findReportsByTypeInAndTitleContainsIgnoreCase(pageable, types, search);
                    } else {
                        page = reportDAO.findReportsByTypeInAndStateAndTitleContainsIgnoreCase(pageable, types, ReportState.valueOf(stateFilter), search);
                    }
                } else {
                    if ("all".equals(stateFilter)) {
                        page = reportDAO.findReportsByTypeAndTitleContainsIgnoreCase(pageable, typeFilter, search);
                    } else {
                        page = reportDAO.findReportsByTypeAndStateAndTitleContainsIgnoreCase(pageable, typeFilter, ReportState.valueOf(stateFilter), search);
                    }
                }
            }
            result.setPages(page.getTotalElements());
            result.setData(page.getContent().
                    stream().
                    map(r -> {
                        ReportDTO temp = modelMapper.map(r, ReportDTO.class);
                        if (temp.getSolvedDate() != null) {
                            try {
                                temp.setSolvedDate(dateFormatLocale.format(dateFormat.parse(temp.getSolvedDate())));
                            } catch (Exception e) {
                            }
                        }
                        return temp;
                    }).
                    collect(Collectors.toList()));

        } catch (Exception e) {
            e.printStackTrace();
            loggerBean.logError(e);
            return null;
        }
        return result;
    }

    public boolean addFeedback(long id, String feedback) {
        try {
            Report report = reportDAO.findById(id).get();
            report.setFeedback(report.getFeedback().replace("null","") + "||" + feedback);
            reportDAO.saveAndFlush(report);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean changeState(long user, long id, String state) {
        try {
            System.out.println(user);
            CityOfficial cityOfficial = cityOfficialDAO.findById(user).get();
            Report report = reportDAO.findById(id).get();
            if (report.getRecepient().getId() != cityOfficial.getDepartment().getId())
                throw new Exception();
            System.out.println(state);
            ReportState newState = ReportState.valueOf(state.replace("\"",""));
            if (newState.equals(report.getState()) == false) {
                report.setState(newState);
                if (newState.equals(ReportState.CLOSED)) {
                    report.setSolvedDate(dateFormat.format(new Date()));
                    cityOfficial.setSolvedReports(cityOfficial.getSolvedReports() + 1);
                    cityOfficialDAO.saveAndFlush(cityOfficial);
                }
                reportDAO.saveAndFlush(report);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            loggerBean.logError(e);
            return false;
        }
    }
}
