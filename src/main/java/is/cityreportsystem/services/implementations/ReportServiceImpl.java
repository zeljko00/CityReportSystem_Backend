package is.cityreportsystem.services.implementations;

import is.cityreportsystem.DAO.ReportDAO;
import is.cityreportsystem.model.Citizen;
import is.cityreportsystem.model.DTO.CitizenDTO;
import is.cityreportsystem.model.DTO.ReportDTO;
import is.cityreportsystem.model.DTO.ReportRequest;
import is.cityreportsystem.model.Report;
import is.cityreportsystem.model.ReportImage;
import is.cityreportsystem.model.enums.ReportState;
import is.cityreportsystem.services.CitizenService;
import is.cityreportsystem.services.CityServiceService;
import is.cityreportsystem.services.ReportImageService;
import is.cityreportsystem.services.ReportService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    @Value("${images.repository}")
    private String imagesRepo;
    private final ModelMapper modelMapper;

    private final ReportDAO reportDAO;
    private final CitizenService citizenService;
    private final CityServiceService cityServiceService;
    private final ReportImageService reportImageService;
    private HashMap<String, List<String>> uploadedImages=new HashMap<String,List<String>>();

    public ReportServiceImpl(ModelMapper modelMapper, ReportDAO reportDAO, CitizenService citizenService, CityServiceService cityServiceService, ReportImageService reportImageService) {
        this.modelMapper = modelMapper;
        this.reportDAO = reportDAO;
        this.citizenService = citizenService;
        this.cityServiceService = cityServiceService;
        this.reportImageService = reportImageService;
    }

    public boolean saveImage(byte[] data, int id){
        if(!uploadedImages.containsKey(Integer.toString(id)))
            uploadedImages.put(Integer.toString(id),new ArrayList<String>());
        String imageName=id+"_"+uploadedImages.get(Integer.toString(id)).size()+".jpg";
        uploadedImages.get(Integer.toString(id)).add(imageName);
        System.out.println("added: "+id);
        String path=imagesRepo+File.separator+imageName;
        try{
            File file=new File(path);
            file.createNewFile();
            Files.write(Paths.get(path), data);
        return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public ReportDTO createReport(ReportRequest report){
        Report reportEntity=modelMapper.map(report, Report.class);
        DateFormat df=new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        reportEntity.setDate(df.format(new Date()));
        reportEntity.setState(ReportState.RECEIVED);
        long tempId=reportEntity.getId();
        if(report.getCreator()>=0){
            CitizenDTO citizen=citizenService.findCitizenById(report.getCreator());
            reportEntity.setCreator(modelMapper.map(citizen, Citizen.class));
        }
        Report result=reportDAO.save(reportEntity);
//        System.out.println(tempId);
//        System.out.println(uploadedImages.size());
//        for(Map.Entry<String,List<String>> entry: uploadedImages.entrySet()){
//            System.out.println(entry.getKey());
//            for(String s: entry.getValue())
//                System.out.print(s+" ");
//            System.out.println("=======================================");
//        }
//       System.out.println(uploadedImages.get(tempId));
        if(uploadedImages.get(Long.toString(tempId))!=null){
            System.out.println("Iamges exist!");
            List<String> images=uploadedImages.get(Long.toString(tempId));
            uploadedImages.remove(Long.toString(tempId));
            int count=1;
            for(String s: images){
                ReportImage reportImage=new ReportImage();
                reportImage.setReport(result);
                reportImage.setName(result.getId()+"_"+count+++".jpeg");
                File temp=new File(imagesRepo+File.separator+s);
                temp.renameTo(new File(imagesRepo+File.separator+reportImage.getName()+".jpg"));
                reportImageService.addImage(reportImage);
            }
        }
        return modelMapper.map(result,ReportDTO.class);
    }

    public List<ReportDTO> getReportsByAuthor(long id){
        CitizenDTO citizenDTO=citizenService.findCitizenById(id);
        Citizen citizen=modelMapper.map(citizenDTO,Citizen.class);
        List<Report> reports=reportDAO.findReportsByCreator(citizen);
        List<ReportDTO> result=new ArrayList<ReportDTO>();
        if(reports!=null)
            for(Report report: reports)
                result.add(modelMapper.map(report,ReportDTO.class));
        return result;
    }
}
