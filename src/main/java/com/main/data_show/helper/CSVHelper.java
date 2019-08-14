package com.main.data_show.helper;

import cn.com.enorth.utility.Beans;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.main.data_show.consts.ApplicationConsts;
import com.main.data_show.consts.ParamConsts;
import com.main.data_show.consts.PointConst;
import com.main.data_show.consts.SysConsts;
import com.main.data_show.enums.EnumPointTypeDefine;
import com.main.data_show.pojo.TaInstantPointData;
import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaUsagePointData;
import com.main.data_show.pojo.TaUsagePointDataDate;
import com.main.data_show.service.TaPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    @Autowired
    private ImportCsvLogsHelper importCsvLogsHelper;

    //导入点的基础数据
    //遍历文件夹读取所有文件
    public  void exportPointBaseData(String readBasePath) throws Exception {
        long startTime = System.currentTimeMillis();
        File file=new File(readBasePath);
        if(!file.isDirectory()){
            //file不是文件夹
            System.out.println(file.getName()+"不是一个文件夹");
        }else{
            traversalFile(readBasePath,readBasePath,"basePointDate");
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
    public  void exportPointBaseRemarkData(String readBasePath) throws Exception {
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
        if("CSV".equals(suffix)||"csv".equals(suffix)){
            return true;
        }else{
            return false;
        }
    }

    public void startTimerImportCSVFile(String dateStr){
        long startTime = System.currentTimeMillis();
        String readBasePath = env.getProperty(ApplicationConsts.SYS_DEMO_IMPORT_DATA_BASE_PATH);
        if(!readBasePath.endsWith(ParamConsts.SEPERRE_STR)){
            readBasePath = readBasePath+ParamConsts.SEPERRE_STR;
        }
        //遍历所有的点  取相对路径和 文件名前缀 要去重
        List<TaPoint> allPointRelativePathList = taPointService.getAllPointRelativePath();
        //计算文件名称 每次定时任务只取一次文件名称的后缀 就可以
        if(toolHelper.isEmpty(dateStr)){
            //例： 生成的文件   文件名叫  xxxx-08-12-19.CSV      里面的数据是  19年8月11日的
            dateStr = toolHelper.dateToStrDate(new Date(), SysConsts.DATE_FORMAT_6);
        }
        String allPath ="";
        for(TaPoint pointVo : allPointRelativePathList){
            try {
                String relativePath = pointVo.getFileRelativePath();
                //记录日志用
                allPath=relativePath;
                if(!relativePath.endsWith(ParamConsts.SEPERRE_STR)){
                    relativePath = relativePath + ParamConsts.SEPERRE_STR;
                }
                String fileNamePrefix = pointVo.getFilePrefixName()+dateStr+".CSV";
                allPath = readBasePath + relativePath + fileNamePrefix;
                logger.info("当前读取的文件名全路径为:"+allPath);
                readCSV(allPath,relativePath,fileNamePrefix);
                importCsvLogsHelper.saveImportCsvLogs(allPath,1,"");
                //读取完文件后 把文件搬到指定的地方  刘工说不需要了
                /*String mvBasePath = env.getProperty(ApplicationConsts.SYS_DEMO_MV_NEW_PATH);
                if(!mvBasePath.endsWith(ParamConsts.SEPERRE_STR)){
                    mvBasePath = mvBasePath+ParamConsts.SEPERRE_STR;
                }
                String mvNewPath= mvBasePath + relativePath + fileNamePrefix;*/
                copyFile(allPath,"");
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
                importCsvLogsHelper.saveImportCsvLogs(allPath,-1,e.getMessage());
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
        CsvReader csvReader =null;
        try {
            logger.warn(filePath);
            File file = new File(filePath);
            if(!file.exists()){
              logger.error("文件："+filePath+" 不存在！！");
              throw new Exception("文件："+filePath+" 不存在！！");
            }
            //取当前点的类型 路径中包含 "REPORTS/POWER/KWH"的是 用量的点
            String pointType = EnumPointTypeDefine.instant.toString();
            if(filePath.indexOf(SysConsts.POINT_USAGE_DEF_FILE_PATH)>-1||filePath.indexOf(SysConsts.POINT_USAGE_DEF_FILE_PATH_2)>-1){
                pointType = EnumPointTypeDefine.usage.toString();
            }
            //用量的点 需要保存上一小时的用量算差值，文档第一条需要查数据库取得 上一小时的抄表数据。如果没有给0
            Map<Integer,String> lastPointUsageMap = new HashMap<Integer,String>();
            csvReader = new CsvReader(filePath, ',', Charset.forName("GBK"));
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
                        logger.warn("-------------------pointName:"+ponitVo.getPointName()+",pointId:"+pointId+",dateTimeInt:"+dateTimeInt+" dateStr:"+dateStr+" hourStr:"+hourStr);
                        allPointDataHelper.insertAllPoint(pointId,dateStr,hourStr,ponitValue,0,dateTime,dateTimeInt);
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
        } finally{
            if(csvReader != null){
                csvReader.close();
            }
        }
    }

    //读取csv数据后要放入到数据库中
    public void insertToDateBase(TaPoint pointVo,String pointData,String dateStr,String hourStr,double pointUsage,Date dateTime, long dateTimeInt) throws Exception {
           
           //点瞬时数据保存到 instantPointDate中
           if(EnumPointTypeDefine.instant.toString().equals(pointVo.getPointType())){
                instantPointDataHelper.insertAllPoint(pointVo.getPointId(),dateStr,hourStr,pointData,0,dateTime,dateTimeInt);
           //点用量数据保存到 usagePointDate中
           }else if(EnumPointTypeDefine.usage.toString().equals(pointVo.getPointType())){
              usagePointDataHelper.insertAllPoint(pointVo.getPointId(),dateStr,hourStr,pointData,pointUsage,dateTime,dateTimeInt);
              usagePointDataDateHelper.makeUsagePointDate(pointVo.getPointId(),pointUsage,dateTime);
              usagePointDataWeekHelper.makeUsagePointWeek(pointVo.getPointId(),pointUsage,dateTime);
              usagePointDataMonHelper.makeUsagePointMon(pointVo.getPointId(),pointUsage,dateTime);
           }else{
               System.out.println("错误的点类型啊！！！！！！！");
           }
    }


    //具体读取点的备注
    public void readPointRemarkCSV(String filePath){
        CsvReader csvReader = null;
        try {
            csvReader = new CsvReader(filePath, ',', Charset.forName("GBK"));
            // 读表头
            int pointIndexFlg = 1;
            boolean dateIndexFlg = false;
            // 读内容
            while (csvReader.readRecord()) {
                // 读一整行
                int length = csvReader.getRawRecord().length();
                String pointName = csvReader.get(0);
                String pointRemark = csvReader.get(1);
                String unit = csvReader.get(3);
                String blockNo = csvReader.get(11);
                //保存点的备注到数据库  根据点名查找
                TaPoint tapoint = new TaPoint();
                tapoint.setPointUnit(unit);
                tapoint.setBlockNo(blockNo);
                tapoint.setState(1);
                tapoint.setPointName(pointName);
                tapoint.setRemarksName(pointRemark);
                tapoint.setModUser(-1);
                taPointService.updateTapointByName(tapoint);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("文件："+filePath+",导入时异常，原因："+e.getMessage());
        } finally {
            if(csvReader != null){
                csvReader.close();
            }
        }
    }


    public String writeCSV1(List<TaPoint> taPointList, Map<Long,Map<Integer, TaInstantPointData>> resultDateMap, String startTime, String endTime, HttpServletResponse response) throws Exception {
        CsvWriter csvWriter = null;
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
            long curSystem = System.currentTimeMillis();
            String fileName = "Instant("+startTime+"_"+endTime+")"+curSystem+".CSV";
            String exportFilePath = readBasePath+fileName;
            File file = new File(exportFilePath);
            if(!file.exists()){
                file.createNewFile();
            }
            csvWriter = new CsvWriter(file.getCanonicalPath(), ',', Charset.forName("GBK"));
            //通用部分
            writeCommon(csvWriter,taPointList);
            Map<Date,List> pointDateMap = new LinkedHashMap<Date,List>();
            List<String> hourIntervalAllList = toolHelper.getHourIntervalAllList(startTime, endTime, SysConsts.DATE_FORMAT_7, SysConsts.DATE_FORMAT_3);
            for(String str : hourIntervalAllList){
                long longTime = Long.parseLong(str);
                if(resultDateMap.containsKey(longTime)) {
                  /*  for (Map.Entry<Long, Map<Integer, TaInstantPointData>> dataVoMap : resultDateMap.entrySet()) {
                        Map<Integer, TaInstantPointData> value = dataVoMap.getValue();*/
                        Map<Integer, TaInstantPointData> value = resultDateMap.get(longTime);
                        for (TaPoint pointVo : taPointList) {
                        TaInstantPointData dataVo = value.get(pointVo.getPointId());
                        if (!pointDateMap.containsKey(dataVo.getCreateTime())) {
                            List<String> pointDataList = new ArrayList<String>();
                            pointDataList.add(dataVo.getDateShow());
                            pointDataList.add(dataVo.getHourShow());
                            pointDataList.add(dataVo.getPointData());
                            pointDateMap.put(dataVo.getCreateTime(), pointDataList);
                        } else {
                            pointDateMap.get(dataVo.getCreateTime()).add(dataVo.getPointData());
                        }
                    }
                   // }
                }else{
                    for (TaPoint pointVo : taPointList) {
                        Date curDate = toolHelper.StrToDate(str, SysConsts.DATE_FORMAT_3);
                        if (!pointDateMap.containsKey(curDate)) {
                            List<String> pointDataList = new ArrayList<String>();
                            String showDate = toolHelper.dateToStrDate(curDate, SysConsts.DATE_FORMAT_9);
                            pointDataList.add(showDate);
                            String showHour = toolHelper.dateToStrDate(curDate, SysConsts.DATE_FORMAT_10);
                            pointDataList.add(showHour);
                            pointDataList.add("-");
                            pointDateMap.put(curDate, pointDataList);
                        } else {
                            pointDateMap.get(curDate).add("-");
                        }
                    }
                }
            }
            for(Map.Entry<Date,List> map : pointDateMap.entrySet()){
                List<String> value = map.getValue();
                csvWriter.writeRecord(value.toArray(new String[value.size()]));
            }
            return exportFilePath;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(csvWriter!=null){
                csvWriter.close();
            }
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
        List<String> pointLogTitle = new ArrayList<String>();
        pointLogTitle.add("<>Date");
        pointLogTitle.add("Time");
        for(TaPoint pointvo : taPointList){
            pointLogTitle.add(pointvo.getPointName());
        }
        csvWriter.writeRecord(pointLogTitle.toArray(new String[pointLogTitle.size()]));
    }

    public String writeCSV2(List<TaPoint> taPointList,Map<Date,Map<Integer,Double>> exportResult,String takeTime,String startTime,String endTime) throws Exception {
        CsvWriter csvWriter = null;
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
            long curSysTime = System.currentTimeMillis();
            String hour = takeTime.substring(0,2);
            String fileName = "Usage("+startTime+"_"+endTime+"_"+hour+")"+curSysTime+".CSV";
            String exportFilePath = readBasePath+fileName;
            File file = new File(exportFilePath);
            if(!file.exists()){
                file.createNewFile();
            }
            csvWriter = new CsvWriter(file.getCanonicalPath(), ',', Charset.forName("GBK"));
            //通用部分
            writeCommon(csvWriter,taPointList);
            Map<Date,List> pointDateMap = new LinkedHashMap<Date,List>();
            for(Map.Entry<Date,Map<Integer,Double>> dataVoMap : exportResult.entrySet()){
                Map<Integer,Double> value = dataVoMap.getValue();
                Date dateKey = dataVoMap.getKey();
                String dateKeyStr = toolHelper.dateToStrDate(dateKey, SysConsts.DATE_FORMAT);
                String[] s = dateKeyStr.split(" ");
                List<String> pointDataList = new ArrayList<String>();
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
            for(Map.Entry<Date,List> map : pointDateMap.entrySet()){
                List<String> value = map.getValue();
                csvWriter.writeRecord(value.toArray(new String[value.size()]));
            }
            return exportFilePath;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            if(csvWriter != null){
                csvWriter.close();
            }
        }
    }

    //用量图表报告   表格
    public String writeCSV3(List<TaPoint> taPointList, List<TaUsagePointDataDate> exportResult, List<String> dateIntervalAllList,String startTime,String endTime) throws Exception {
        CsvWriter csvWriter = null;
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
            long curSysTime = System.currentTimeMillis();
            String fileName = "Usage("+startTime+"_"+endTime+")"+curSysTime+".CSV";
            String exportFilePath = readBasePath+fileName;
            File file = new File(exportFilePath);
            if(!file.exists()){
                file.createNewFile();
            }
            csvWriter = new CsvWriter(file.getCanonicalPath(), ',', Charset.forName("GBK"));
            List<String> title = new ArrayList<String>();
            title.add(" ");
            for(String str : dateIntervalAllList){
                title.add(str);
            }
            Map<Integer,List<String>> pointDateMap = new HashMap<Integer,List<String>>();
            Map<Integer,String> map = new LinkedHashMap<Integer,String>();
            for(TaPoint point : taPointList){
                int pointId = point.getPointId();
                for(String str : dateIntervalAllList){
                    boolean flg = false;
                    for(TaUsagePointDataDate usagePointData : exportResult){
                        if(usagePointData.getPointId() == pointId&&str.equals(String.valueOf(usagePointData.getDateShow()))){
                            flg = true;
                            if(pointDateMap.containsKey(usagePointData.getPointId())){
                                pointDateMap.get(usagePointData.getPointId()).add(String.valueOf(usagePointData.getPointData()));
                            }else{
                                List<String> list =new ArrayList<String>();
                                list.add(point.getPointName()+"("+point.getRemarksName()+")");
                                list.add(String.valueOf(usagePointData.getPointData()));
                                pointDateMap.put(usagePointData.getPointId(),list);
                            }
                        }
                    }
                    //如果循环一轮发现没有数据 补位-
                    if(!flg){
                        if(pointDateMap.containsKey(pointId)){
                            pointDateMap.get(pointId).add("-");
                        }else{
                            List<String> list =new ArrayList<String>();
                            list.add(point.getPointName()+"("+point.getRemarksName()+")");
                            list.add("-");
                            pointDateMap.put(pointId,list);
                        }
                    }
                }
            }
            csvWriter.writeRecord(title.toArray(new String[title.size()]));
            for(Map.Entry<Integer,List<String>> writeMap : pointDateMap.entrySet()){
                List<String> value = writeMap.getValue();
                csvWriter.writeRecord(value.toArray(new String[value.size()]));
            }
            return exportFilePath;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(csvWriter!=null){
                csvWriter.close();
            }
        }
    }



    /**
    * 复制单个文件
    * @param oldPath String 原文件路径 如：c:/fqf.txt
    * @param newPath String 复制后路径 如：f:/fqf.txt
    * @return boolean
    */
    public void copyFile(String oldPath, String newPath) throws Exception {
        try {
            File oldfile = new File(oldPath);
            //删除老文件
            if(oldfile!=null&&oldfile.exists()){
                System.gc();
                if(oldfile.delete()){
                    logger.info("删除成功,"+oldPath);
                    System.out.println(oldPath+",删除成功");
                }else{
                    logger.error("删除失败,"+oldPath);
                    System.out.println(oldPath+",删除失败");
                }
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
            throw e;
        }finally {
        }
    }

    //固定点固定的计算规则，
    public String writeCSV4(List<TaUsagePointDataDate> exportResult, List<String> dateIntervalAllList,String startTime,String endTime) throws Exception {
        CsvWriter csvWriter = null;
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
            long curSysTime = System.currentTimeMillis();
            String fileName = "fixPointUsage("+startTime+"_"+endTime+")"+curSysTime+".CSV";
            String exportFilePath = readBasePath+fileName;
            File file = new File(exportFilePath);
            if(!file.exists()){
                file.createNewFile();
            }
            csvWriter = new CsvWriter(file.getCanonicalPath(), ',', Charset.forName("GBK"));
            List<String> title = new ArrayList<String>();
            title.add(" ");
            for(String str : dateIntervalAllList){
                title.add(str);
            }
            Map<String,Double> resultMap = new HashMap<String,Double>();
            //数据放入map
            for(TaUsagePointDataDate dataDateVo : exportResult){
                resultMap.put(dataDateVo.getPointId()+"_"+dataDateVo.getDateShow(),dataDateVo.getPointData());
            }

            Map<String,List<String>> pointDateMap = new HashMap<String,List<String>>();
            Map<Integer,String> map = new LinkedHashMap<Integer,String>();
            for(String str : dateIntervalAllList){
            //第一个点计算 21.2  需要 WH.LV11.M1:KWH     WH.WAP.M1:KWH
                String key212a = toolHelper.strConct(pointHelper.getPointIdByPointName("WH.LV11.M1:KWH"), str);
                String key212b = toolHelper.strConct(pointHelper.getPointIdByPointName("WH.WAP.M1:KWH"), str);
                String aDouble = "-";
                if(resultMap.containsKey(key212a)&&resultMap.containsKey(key212b)){
                    aDouble = String.valueOf(toolHelper.doubleSum(resultMap.get(key212a),resultMap.get(key212b)));
                }else if(resultMap.containsKey(key212b)){
                    aDouble = String.valueOf(resultMap.get(key212b));
                }else if(resultMap.containsKey(key212a)){
                    aDouble = String.valueOf(resultMap.get(key212a));
                }
                if(pointDateMap.containsKey(PointConst.DIAN_212)){
                    pointDateMap.get(PointConst.DIAN_212).add(aDouble);
                }else{
                    List<String> list =new ArrayList<String>();
                    list.add(PointConst.DIAN_212);
                    list.add(aDouble);
                    pointDateMap.put(PointConst.DIAN_212,list);
                }
            //第二个点计算  22.2   DL.LV2.M1:KWH    DL.L2R.M1:KWH
                String key222a = toolHelper.strConct(pointHelper.getPointIdByPointName("DL.LV2.M1:KWH"), str);
                String key222b = toolHelper.strConct(pointHelper.getPointIdByPointName("DL.L2R.M1:KWH"), str);
                String a222Double = "-";
                if(resultMap.containsKey(key222a)&&resultMap.containsKey(key222b)){
                    a222Double = String.valueOf(toolHelper.doubleSum(resultMap.get(key222a),resultMap.get(key222b)));
                }else if(resultMap.containsKey(key222b)){
                    a222Double = String.valueOf(resultMap.get(key222b));
                }else if(resultMap.containsKey(key222a)){
                    a222Double = String.valueOf(resultMap.get(key222a));
                }
                if(pointDateMap.containsKey(PointConst.DIAN_222)){
                    pointDateMap.get(PointConst.DIAN_222).add(a222Double);
                }else{
                    List<String> list =new ArrayList<String>();
                    list.add(PointConst.DIAN_222);
                    list.add(a222Double);
                    pointDateMap.put(PointConst.DIAN_222,list);
                }
                //第三个点 112 CC.LV34.M9:KWH   CC.LV35.M5:KWH    CC.LV35.M9:KWH  CC.LV45.M8:KWH
                String key112a = toolHelper.strConct(pointHelper.getPointIdByPointName("CC.LV34.M9:KWH"), str);
                String key112b = toolHelper.strConct(pointHelper.getPointIdByPointName("CC.LV35.M5:KWH"), str);
                String key112c = toolHelper.strConct(pointHelper.getPointIdByPointName("CC.LV35.M9:KWH"), str);
                String key112d = toolHelper.strConct(pointHelper.getPointIdByPointName("CC.LV45.M8:KWH"), str);
                String a112Double = "-";
                if(resultMap.containsKey(key112a)||resultMap.containsKey(key112b)
                        ||resultMap.containsKey(key112c)||resultMap.containsKey(key112d)){
                    double d1 = 0;
                    if(resultMap.containsKey(key112a)){
                        d1 = toolHelper.doubleSum(d1, resultMap.get(key112a));
                    }
                    if(resultMap.containsKey(key112b)){
                        d1 = toolHelper.doubleSum(d1, resultMap.get(key112b));
                    }
                    if(resultMap.containsKey(key112c)){
                        d1 = toolHelper.doubleSum(d1, resultMap.get(key112c));
                    }
                    if(resultMap.containsKey(key112d)){
                        d1 = toolHelper.doubleSum(d1, resultMap.get(key112d));
                    }
                    a112Double =  String.valueOf(d1);
                }
                if(pointDateMap.containsKey(PointConst.DIAN_112)){
                    pointDateMap.get(PointConst.DIAN_112).add(a112Double);
                }else{
                    List<String> list =new ArrayList<String>();
                    list.add(PointConst.DIAN_112);
                    list.add(a112Double);
                    pointDateMap.put(PointConst.DIAN_112,list);
                }
                //第四个点   100    CC.LV52.M1:KWH    CC.LV62.M1:KWH   CC.LV12.M1:KWH   CC.LV22.M1:KWH   CC.LV33.M1:KWH    CC.LV42.M1:KWH
            }
            csvWriter.writeRecord(title.toArray(new String[title.size()]));
            for(Map.Entry<String,List<String>> writeMap : pointDateMap.entrySet()){
                List<String> value = writeMap.getValue();
                csvWriter.writeRecord(value.toArray(new String[value.size()]));
            }
            return exportFilePath;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(csvWriter!=null){
                csvWriter.close();
            }
        }
    }
}


