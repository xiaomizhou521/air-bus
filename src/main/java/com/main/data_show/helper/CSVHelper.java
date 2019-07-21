package com.main.data_show.helper;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.main.data_show.consts.ApplicationConsts;
import com.main.data_show.consts.ParamConsts;
import com.main.data_show.consts.SysConsts;
import com.main.data_show.enums.EnumPointTypeDefine;
import com.main.data_show.pojo.TaInstantPointData;
import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaUsagePointData;
import com.main.data_show.service.TaPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

@Service
public class CSVHelper {
    private static Logger logger = LoggerFactory.getLogger(CSVHelper.class);
    @Autowired
    private Environment env;

    @Autowired
    private PointHelper pointHelper;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private PointDataHelper pointDataHelper;

    @Autowired
    private AllPointDataHelper allPointDataHelper;

    @Autowired
    private InstantPointDataHelper instantPointDataHelper;

    @Autowired
    private UsagePointDataHelper usagePointDataHelper;

    @Autowired
    private ToolHelper toolHelper;

    @Autowired
    private TaPointService taPointService;

    @Autowired
    private UsagePointDataDateHelper usagePointDataDateHelper;
    @Autowired
    private UsagePointDataWeekHelper usagePointDataWeekHelper;
    @Autowired
    private UsagePointDataMonHelper usagePointDataMonHelper;

    //导入点的基础数据
    //遍历文件夹读取所有文件
    public  void exportPointBaseData() throws Exception {
        long startTime = System.currentTimeMillis();
        String readBasePath = env.getProperty(ApplicationConsts.SYS_DEMO_IMPORT_DATA_BASE_PATH);
        File file=new File(readBasePath);
        if(!file.isDirectory()){
            //file不是文件夹
            System.out.println(file.getName()+"不是一个文件夹");
        }else{
            traversalFile(readBasePath,readBasePath,"basePointDate");
          //  exportPointBaseRemarkData();
        }
        long endTime = System.currentTimeMillis();
        long useTime = endTime-startTime;
        System.out.println("---数据跑入共耗时："+ useTime+"毫秒");
        System.out.println("---数据跑入共耗时："+ useTime+"毫秒");
        System.out.println("---数据跑入共耗时："+ useTime+"毫秒");
        System.out.println("---数据跑入共耗时："+ useTime+"毫秒");
        System.out.println("---数据跑入共耗时："+ useTime+"毫秒");
        System.out.println("---数据跑入共耗时："+ useTime+"毫秒");
        System.out.println("---数据跑入共耗时："+ useTime+"毫秒");
        System.out.println("---数据跑入共耗时："+ useTime+"毫秒");
        System.out.println("---数据跑入共耗时："+ useTime+"毫秒");
        System.out.println("---数据跑入共耗时："+ useTime+"毫秒");
        System.out.println("---数据跑入共耗时："+ useTime+"毫秒");
        System.out.println("---数据跑入共耗时："+ useTime+"毫秒");
    }

    //导入点的备注信息
    //遍历文件夹读取所有文件
    public  void exportPointBaseRemarkData() throws Exception {
        String readBasePath = env.getProperty(ApplicationConsts.SYS_DEMO_IMPORT_DATA_REMARK_BASE_PATH);
        File file=new File(readBasePath);
        if(!file.isDirectory()){
            //file不是文件夹
            System.out.println(file.getName()+"不是一个文件夹");
        }else{
            traversalFile(readBasePath,readBasePath,"basePointRemark");
        }
    }
//basePath第一次的根路径
    public  void traversalFile(String filePath,String basePath,String readFileType) throws Exception {
        File file=new File(filePath);
        String[] fileList =file.list();
        for(String fileName:fileList){
            String chileFilePath="";
            if(filePath.endsWith("/")){
                chileFilePath=filePath+fileName;
            }else{
                chileFilePath=filePath+"/"+fileName;
            }
            File chilefile=new File(chileFilePath);
            if(chilefile.isDirectory()){
                traversalFile(chileFilePath,basePath,readFileType);
            }else{
                if(checkCSV(fileName)){
                    if("basePointDate".equals(readFileType)){
                        System.out.println("读取CSV文件:"+fileName+"开始");
                        //点文件相对路径
                        String relativePath = toolHelper.getPointRelativePath(filePath,basePath);
                        //点文件名的前缀
                        String fileNamePrefix = toolHelper.getPointRFilenNamePrefix(fileName);
                        readCSV(chileFilePath,relativePath,fileNamePrefix);
                    }else if("basePointRemark".equals(readFileType)){
                        readPointRemarkCSV(chileFilePath);
                    }
                }

            }
        }
    }

    public  Boolean checkCSV(String fileName){
        //“.”和“|”都是转义字符,必须得加"\\"
        String[] str=fileName.split("\\.");
        String suffix=str[1];
        if("CSV".equals(suffix)){
            return true;
        }else{
            return false;
        }
    }

    public void startTimerImportCSVFile(){
        long startTime = System.currentTimeMillis();
        String readBasePath = env.getProperty(ApplicationConsts.SYS_DEMO_IMPORT_DATA_BASE_PATH);
        if(!readBasePath.endsWith(ParamConsts.SEPERRE_STR)){
            readBasePath = readBasePath+ParamConsts.SEPERRE_STR;
        }
        //遍历所有的点  取相对路径和 文件名前缀 要去重
        List<TaPoint> allPointRelativePathList = taPointService.getAllPointRelativePath();
        for(TaPoint pointVo : allPointRelativePathList){
            try {
                String relativePath = pointVo.getFileRelativePath();
                if(!relativePath.endsWith(ParamConsts.SEPERRE_STR)){
                    relativePath = relativePath + ParamConsts.SEPERRE_STR;
                }
                //计算文件名称
                //String dateStr = toolHelper.dateToStrDate(new Date(), SysConsts.DATE_FORMAT_6);
                String dateStr = "07-03-19";
                String fileNamePrefix = pointVo.getFilePrefixName()+dateStr+".CSV";
                String allPath = readBasePath + relativePath + fileNamePrefix;
                logger.info("当前读取的文件名全路径为:"+allPath);
                readCSV(allPath,relativePath,fileNamePrefix);
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        long endTime = System.currentTimeMillis();
        long userTime = endTime - startTime;
        logger.info("---------------共耗时："+userTime+"毫秒");
        logger.info("---------------共耗时："+userTime+"毫秒");
        logger.info("---------------共耗时："+userTime+"毫秒");
        logger.info("---------------共耗时："+userTime+"毫秒");
        logger.info("---------------共耗时："+userTime+"毫秒");
        logger.info("---------------共耗时："+userTime+"毫秒");
        logger.info("---------------共耗时："+userTime+"毫秒");
        logger.info("---------------共耗时："+userTime+"毫秒");
        logger.info("---------------共耗时："+userTime+"毫秒");
    }


    //具体读取某个csv文件
    public void readCSV(String filePath,String relativePath,String fileNamePrefix) throws Exception {
        try {
            File file = new File(filePath);
            if(!file.exists()){
              logger.error("文件："+filePath+" 不存在！！");
              throw new Exception("文件："+filePath+" 不存在！！");
            }
            //取当前点的类型 路径中包含 "REPORTS/POWER/KWH"的是 用量的点
            String pointType = EnumPointTypeDefine.instant.toString();
            if(filePath.indexOf(SysConsts.POINT_USAGE_DEF_FILE_PATH)>-1){
                pointType = EnumPointTypeDefine.usage.toString();
            }
            //用量的点 需要保存上一小时的用量算差值，文档第一条需要查数据库取得 上一小时的抄表数据。如果没有给0
            Map<Integer,String> lastPointUsageMap = new HashMap<Integer,String>();
            CsvReader csvReader = new CsvReader(filePath, ',', Charset.forName("GBK"));
            //保存所有点信息 保持顺序
            Map<Integer,TaPoint> resultMap = new HashMap<Integer,TaPoint>();
            int pointIndexFlg = 1;
            boolean dateIndexFlg = false;
            // 读内容
            while (csvReader.readRecord()) {
                // 读一整行
                int length = csvReader.getRawRecord().length();
                String result = csvReader.get(0);
                if(result.indexOf("****")>-1){
                    continue;
                }
                if(result.startsWith("Point_")){
                    String pointName = csvReader.get(1);
                    //保存点到数据库库 pointName不存在才创建
                    TaPoint pointVo = pointHelper.savePoint(pointName, pointType, "none", "none", SysConsts.DEF_SYS_USER_ID,relativePath,fileNamePrefix);
                    resultMap.put(pointIndexFlg,pointVo);
                    pointIndexFlg++;
                }
                if(dateIndexFlg){
                    //这里是数据部门
                    String dateStr = csvReader.get(0);
                    dateStr = toolHelper.dateStrFormatStr(dateStr);
                    String hourStr = csvReader.get(1);
                    int pointDataFlg = 2;
                    for(Map.Entry<Integer,TaPoint> map : resultMap.entrySet()){
                        int index = map.getKey();
                        TaPoint ponitVo = map.getValue();
                        int pointId = ponitVo.getPointId();
                        String ponitValue = csvReader.get(pointDataFlg);
                        //总表 不做处理立马就插进来  为了保证数据的完成 不丢失 之后在做别的处理
                        Date dateTime = toolHelper.makeDateByDateAndHour(dateStr, hourStr);
                        long dateTimeInt = toolHelper.dateToNumDate(dateTime,SysConsts.DATE_FORMAT_3);
                        //allPointDataHelper.insertAllPoint(pointId,dateStr,hourStr,ponitValue,0,dateTime,dateTimeInt);
                        //每个小时的用量
                        double pointUsage = 0;
                        //用量时 需要保存上一小时的结果 算当前小时的用量
                        if(EnumPointTypeDefine.usage.toString().equals(pointType)){
                            if(lastPointUsageMap.containsKey(pointId)){
                                //取上一次的数据
                                String lastHourData = lastPointUsageMap.get(pointId);
                                if(toolHelper.isNumeric(ponitValue)&&toolHelper.isNumeric(lastHourData)){
                                    pointUsage = toolHelper.doubleSubtract(ponitValue,lastHourData);
                                }else{
                                   logger.error("数据格式错误，ponitValue:"+ponitValue+",lastHourData:"+lastHourData);
                                }
                            }else{
                                //如果没有 数据库查上次的时间
                                TaUsagePointData lastUsagePointVo = usagePointDataHelper.getUsagePointDataByPointIdAndTime(pointId, dateTime);
                                if(lastUsagePointVo!=null&&toolHelper.isNumeric(ponitValue)&&toolHelper.isNumeric(lastUsagePointVo.getPointData())){
                                    pointUsage = toolHelper.doubleSubtract(ponitValue,lastUsagePointVo.getPointData());;
                                }else{
                                    pointUsage = 0;
                                }
                            }
                            //把这次读出来的数据 放入map 给下次计算用
                            if(toolHelper.isNumeric(ponitValue)){
                                lastPointUsageMap.put(pointId,ponitValue);
                            }
                        }
                        pointDataFlg ++;
                        insertToDateBase(ponitVo,ponitValue,dateStr,hourStr,pointUsage,dateTime,dateTimeInt);
                    }
                }
                if("<>Date".equals(result)){
                    dateIndexFlg = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("文件："+filePath+",导入时异常，原因："+e.getMessage());
            throw e;
        }
    }

    //读取csv数据后要放入到数据库中
    public void insertToDateBase(TaPoint pointVo,String pointData,String dateStr,String hourStr,double pointUsage,Date dateTime, long dateTimeInt) throws Exception {
           
           //点瞬时数据保存到 instantPointDate中
           if(EnumPointTypeDefine.instant.toString().equals(pointVo.getPointType())){
                instantPointDataHelper.insertAllPoint(pointVo.getPointId(),dateStr,hourStr,pointData,0,dateTime,dateTimeInt);
           //点用量数据保存到 usagePointDate中
           }else if(EnumPointTypeDefine.usage.toString().equals(pointVo.getPointType())){
              // usagePointDataHelper.insertAllPoint(pointVo.getPointId(),dateStr,hourStr,pointData,pointUsage,dateTime,dateTimeInt);
               usagePointDataDateHelper.makeUsagePointDate(pointVo.getPointId(),pointUsage,dateTime);
               usagePointDataWeekHelper.makeUsagePointWeek(pointVo.getPointId(),pointUsage,dateTime);
               usagePointDataMonHelper.makeUsagePointMon(pointVo.getPointId(),pointUsage,dateTime);
           }else{
               System.out.println("错误的点类型啊！！！！！！！");
           }
    }


    //具体读取点的备注
    public void readPointRemarkCSV(String filePath){
        try {
            CsvReader csvReader = new CsvReader(filePath, ',', Charset.forName("GBK"));
            // 读表头
            int pointIndexFlg = 1;
            boolean dateIndexFlg = false;
            // 读内容
            while (csvReader.readRecord()) {
                // 读一整行
                int length = csvReader.getRawRecord().length();
                String result = csvReader.get(0);
                if(result.startsWith("Point_")){
                    String pointName = csvReader.get(1);
                    String pointRemark = csvReader.get(2);
                    //保存点的备注到数据库  根据点名查找
                    TaPoint tapoint = new TaPoint();
                    tapoint.setPointType("");
                    tapoint.setPointUnit("");
                    tapoint.setBlockNo("");
                    tapoint.setState(1);
                    tapoint.setFileRelativePath("");
                    tapoint.setFilePrefixName("");
                    tapoint.setPointName(pointName);
                    tapoint.setRemarksName(pointRemark);
                    tapoint.setModUser(-1);
                    taPointService.updateTapointByName(tapoint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("文件："+filePath+",导入时异常，原因："+e.getMessage());
        }
    }


    public String writeCSV1(List<TaPoint> taPointList, Map<Long,Map<Integer, TaInstantPointData>> resultDateMap, String startTime, String endTime, HttpServletResponse response) throws Exception {
        try {
            String readBasePath = env.getProperty(ApplicationConsts.SYS_DEMO_INSTANT_EXPORT_DATA_BASE_PATH);
            if(toolHelper.isEmpty(readBasePath)){
                throw new Exception(ApplicationConsts.SYS_DEMO_INSTANT_EXPORT_DATA_BASE_PATH+",基础路径为空!");
            }
            if(!readBasePath.endsWith(ParamConsts.SEPERRE_STR)){
                readBasePath = readBasePath+ParamConsts.SEPERRE_STR;
            }
            File file1 = new File(readBasePath);
            if(!file1.exists()){
                file1.mkdir();
            }
            //生成文件名
            String fileName = "Instant("+startTime+"_"+endTime+").CSV";
            String exportFilePath = readBasePath+fileName;
            File file = new File(exportFilePath);
            if(!file.exists()){
                file.createNewFile();
            }
            CsvWriter csvWriter = new CsvWriter(file.getCanonicalPath(), ',', Charset.forName("GBK"));
            //通用部分
            writeCommon(csvWriter,taPointList);
            Map<Date,ArrayList> pointDateMap = new LinkedHashMap<>();
            for(Map.Entry<Long,Map<Integer, TaInstantPointData>> dataVoMap : resultDateMap.entrySet()){
                Map<Integer, TaInstantPointData> value = dataVoMap.getValue();
                for(TaPoint pointVo : taPointList){
                    TaInstantPointData dataVo = value.get(pointVo.getPointId());
                    if(!pointDateMap.containsKey(dataVo.getCreateTime())){
                        ArrayList<String> pointDataList = new ArrayList<>();
                        pointDataList.add(dataVo.getDateShow());
                        pointDataList.add(dataVo.getHourShow());
                        pointDataList.add(dataVo.getPointData());
                        pointDateMap.put(dataVo.getCreateTime(),pointDataList);
                    }else{
                        pointDateMap.get(dataVo.getCreateTime()).add(dataVo.getPointData());
                    }
                }
            }
            for(Map.Entry<Date,ArrayList> map : pointDateMap.entrySet()){
                ArrayList<String> value = map.getValue();
                csvWriter.writeRecord(value.toArray(new String[value.size()]));
            }
            csvWriter.close();
            return exportFilePath;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static void writeCommon(CsvWriter csvWriter,List<TaPoint> taPointList) throws IOException {
        csvWriter.writeRecord(new String[]{"Point list"});
        csvWriter.writeRecord(new String[]{"Index", "Point name", "Type", "Unit", "Block No."});
        csvWriter.writeRecord(new String[]{"序号", "点名", "类型", "计量单位", "栋号"});
        int index = 1;
        for(TaPoint pointvo : taPointList){
            csvWriter.writeRecord(new String[]{String.valueOf(index), pointvo.getPointName(), pointvo.getPointType(), pointvo.getPointUnit(), pointvo.getBlockNo()});
            index++;
        }
        csvWriter.writeRecord(new String[]{"   "});
        csvWriter.writeRecord(new String[]{"Point log"});
        ArrayList<String> pointLogTitle = new ArrayList<>();
        pointLogTitle.add("<>Date");
        pointLogTitle.add("Time");
        for(TaPoint pointvo : taPointList){
            pointLogTitle.add(pointvo.getPointName());
        }
        csvWriter.writeRecord(pointLogTitle.toArray(new String[pointLogTitle.size()]));
    }

    public String writeCSV2(List<TaPoint> taPointList,Map<Date,Map<Integer,Double>> exportResult,String takeTime,String startTime,String endTime) throws Exception {
        try {
            String readBasePath = env.getProperty(ApplicationConsts.SYS_DEMO_USAGE_EXPORT_DATA_BASE_PATH);
            if(toolHelper.isEmpty(readBasePath)){
                throw new Exception(ApplicationConsts.SYS_DEMO_USAGE_EXPORT_DATA_BASE_PATH+",基础路径为空!");
            }
            if(!readBasePath.endsWith(ParamConsts.SEPERRE_STR)){
                readBasePath = readBasePath+ParamConsts.SEPERRE_STR;
            }
            File file1 = new File(readBasePath);
            if(!file1.exists()){
                file1.mkdir();
            }
            //生成文件名
            String hour = takeTime.substring(0,2);
            String fileName = "Usage("+startTime+"_"+endTime+"_"+hour+").CSV";
            String exportFilePath = readBasePath+fileName;
            File file = new File(exportFilePath);
            if(!file.exists()){
                file.createNewFile();
            }
            CsvWriter csvWriter = new CsvWriter(file.getCanonicalPath(), ',', Charset.forName("GBK"));
            //通用部分
            writeCommon(csvWriter,taPointList);
            Map<Date,ArrayList> pointDateMap = new LinkedHashMap<>();
            for(Map.Entry<Date,Map<Integer,Double>> dataVoMap : exportResult.entrySet()){
                Map<Integer,Double> value = dataVoMap.getValue();
                Date dateKey = dataVoMap.getKey();
                String dateKeyStr = toolHelper.dateToStrDate(dateKey, SysConsts.DATE_FORMAT);
                String[] s = dateKeyStr.split(" ");
                ArrayList<String> pointDataList = new ArrayList<>();
                pointDataList.add(s[0]);
                pointDataList.add(s[1]);
                for(TaPoint pointVo : taPointList){
                    if(value.containsKey(pointVo.getPointId())){
                        Double dataVo = value.get(pointVo.getPointId());
                        pointDataList.add(String.valueOf(dataVo));
                    }else{
                        //不包含的时候说明 这个点 在这个事件没有数据  给0
                        pointDataList.add("0");
                    }
                }
                pointDateMap.put(dateKey,pointDataList);
            }
            for(Map.Entry<Date,ArrayList> map : pointDateMap.entrySet()){
                ArrayList<String> value = map.getValue();
                csvWriter.writeRecord(value.toArray(new String[value.size()]));
            }
            csvWriter.close();
            return exportFilePath;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}


