package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.CityOfficialDAO;
import is.cityreportsystem.DAO.ReportDAO;
import is.cityreportsystem.model.*;
import is.cityreportsystem.model.DTO.*;
import is.cityreportsystem.model.enums.ReportState;
import is.cityreportsystem.services.*;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    @Value("${reports.images.repository}")
    private String imagesRepo;
    private final ModelMapper modelMapper;

    private final ReportDAO reportDAO;
    private final CitizenService citizenService;
    private final CityServiceService cityServiceService;
    private final CityOfficialDAO cityOfficialDAO;
    private final ReportImageService reportImageService;
    private final ReportTypeService reportTypeService;
    private HashMap<String, List<Tuple>> uploadedImages = new HashMap<String, List<Tuple>>();

    public ReportServiceImpl(ModelMapper modelMapper, ReportDAO reportDAO, CitizenService citizenService, CityServiceService cityServiceService, CityOfficialDAO cityOfficialDAO, ReportImageService reportImageService, ReportTypeService reportTypeService) {
        this.modelMapper = modelMapper;
        this.reportDAO = reportDAO;
        this.citizenService = citizenService;
        this.cityServiceService = cityServiceService;
        this.cityOfficialDAO = cityOfficialDAO;
        this.reportImageService = reportImageService;
        this.reportTypeService = reportTypeService;
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
        }
    }

    public ReportDTO createReport(ReportRequest report) {
        Report reportEntity = modelMapper.map(report, Report.class);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        reportEntity.setDate(df.format(new Date()));
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
            return false;
        }
    }
    public   PageDTO<ReportDTO> getReports(long userId,long departmentId,Pageable pageable, String search, String typeFilter, String stateFilter){
        PageDTO<ReportDTO> result = new PageDTO<>();
        try{
            CityOfficial user=cityOfficialDAO.findById(userId).get();
            if(user==null)
                throw new Exception();
            CityServiceDTO cityService=cityServiceService.getCityServiceById(departmentId);
            if(cityService==null)
                throw  new Exception();
            if(user.getDepartment().getId()!=departmentId)
                throw new Exception();
            List<String> types=cityService.getReportTypes();
            Page<Report> page=null;
            if("-".equals(search)){
                if("all".equals(typeFilter)){
                    if("all".equals(stateFilter)){
                        page=reportDAO.findReportsByTypeIn(pageable,types);
                    }else{
                        page=reportDAO.findReportsByTypeInAndState(pageable,types,ReportState.valueOf(stateFilter));
                    }
                }else{
                    if("all".equals(stateFilter)){
                        page=reportDAO.findReportsByType(pageable,typeFilter);
                    }else{
                        page=reportDAO.findReportsByTypeAndState(pageable,typeFilter,ReportState.valueOf(stateFilter));
                    }
                }
            }else{
                if("all".equals(typeFilter)){
                    if("all".equals(stateFilter)){
                        page=reportDAO.findReportsByTypeInAndTitleContainsIgnoreCase(pageable,types,search);
                    }else{
                        page=reportDAO.findReportsByTypeInAndStateAndTitleContainsIgnoreCase(pageable,types,ReportState.valueOf(stateFilter),search);
                    }
                }else{
                    if("all".equals(stateFilter)){
                        page=reportDAO.findReportsByTypeAndTitleContainsIgnoreCase(pageable,typeFilter,search);
                    }else{
                        page=reportDAO.findReportsByTypeAndStateAndTitleContainsIgnoreCase(pageable,typeFilter,ReportState.valueOf(stateFilter),search);
                    }
                }
            }
            result.setPages(page.getTotalElements());
            result.setData(page.getContent().
                    stream().
                    map( r -> modelMapper.map(r,ReportDTO.class)).
                    collect(Collectors.toList()));

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
