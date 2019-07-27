package com.main.data_show.controller;

import cn.com.enorth.utility.Beans;
import com.github.pagehelper.PageInfo;
import com.main.data_show.consts.JspPageConst;
import com.main.data_show.consts.ParamConsts;
import com.main.data_show.consts.SysConsts;
import com.main.data_show.enums.EnumImgReportTypeDefine;
import com.main.data_show.enums.EnumUsageTimeTypeDefine;
import com.main.data_show.helper.*;
import com.main.data_show.mapper.TaPonitDataMapper;
import com.main.data_show.mapper.TaPonitMapper;
import com.main.data_show.mapper.TaUsagePonitDataDateMapper;
import com.main.data_show.pojo.*;
import com.main.data_show.service.TaPointDataService;
import com.main.data_show.service.TaPointService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspPage;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

@Controller
public class WorkController {

    @Autowired
    private TaPointService taPointService;

    @Autowired
    private ToolHelper toolHelper;

    @Resource
    private TaPonitMapper taPonitMapper;

    @Resource
    private TaPonitDataMapper taPonitDataMapper;

    @Resource
    private TaPointDataService taPointDataService;

    @Autowired
    private CSVHelper csvHelper;

    @Autowired
    private JFreeChartHelper jFreeChartHelper;

    @Autowired
    private InstantPointDataHelper instantPointDataHelper;

    @Autowired
    private UsagePointDataHelper usagePointDataHelper;

    @Autowired
    private TaUsagePonitDataDateMapper taUsagePonitDataDateMapper;

    @Autowired
    private UsagePointDataDateHelper usagePointDataDateHelper;

    @Autowired
    private UsagePointDataWeekHelper usagePointDataWeekHelper;

    @Autowired
    private UsagePointDataMonHelper usagePointDataMonHelper;


    @RequestMapping(value = "work/toPointList")
    public String toPointList(HttpServletRequest request) {
        int pageNo = 1;
        int pageSize = 3;
        String curPageNo = request.getParameter("pageNo");
        if(!toolHelper.isEmpty(curPageNo)){
            pageNo = Integer.parseInt(curPageNo);
        }
        String searchPointName = request.getParameter("pointName");
        String searchRemarkName = request.getParameter("remarkName");
        //取所有点的列表
        List<TaPoint> pointsByPage = taPointService.getPointsByPage(pageNo, pageSize,searchPointName,searchRemarkName);
        PageInfo<TaPoint> pageInfo=new PageInfo<>(pointsByPage);
        request.setAttribute("pointList", pointsByPage);
        request.setAttribute("totalPage", pageInfo.getTotal());
        request.setAttribute("lastPage", pageInfo.getLastPage());
        request.setAttribute("pageNum", pageInfo.getPageNum());
        request.setAttribute("pageSize", pageInfo.getPageSize());
        request.setAttribute("firstPage", pageInfo.getFirstPage());
        request.setAttribute("nextPage", pageInfo.getNextPage());
        request.setAttribute("searchPointName", searchPointName);
        request.setAttribute("searchRemarkName", searchRemarkName);

        return JspPageConst.TO_POINT_LIST_JSP_REDIRECT;
    }

    @RequestMapping(value = "work/toEditPoint")
    public String toEditPoint(HttpServletRequest request) {
        String pointId = request.getParameter("pointId");
        TaPoint pointVo = taPonitMapper.findPointByPointId(Integer.parseInt(pointId));
        request.setAttribute("pointVo",pointVo);
        return JspPageConst.TO_EDIT_POINT_JSP_REDIRECT;
    }

    @RequestMapping(value = "work/toAddPoint")
    public String toAddPoint(HttpServletRequest request) {
        return JspPageConst.TO_ADD_POINT_JSP_REDIRECT;
    }

    @RequestMapping(value = "work/checkPointNameRepeat")
    public void checkPointNameRepeat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(Beans.UTF_8);
        JSONObject result = new JSONObject();
        try {
            String pointName = request.getParameter("pointName");
            TaPoint point = taPointService.findPointByPointName(pointName);
            if(point != null){
                throw new Exception("点名字已经存在！");
            }
            result.put("code",1);
            result.put("data","");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code",-1);
            result.put("data",e.getMessage());
        } finally {
            response.getWriter().print(result);
            response.getWriter().flush();
            response.getWriter().close();
        }
    }

    @RequestMapping(value = "work/editPointDo")
    public String editPointDo(HttpServletRequest request) throws Exception {
        String pointId = request.getParameter("pointId");
        String remarksName = request.getParameter("remarksName");
        String pointType = request.getParameter("pointType");
        String pointUnit = request.getParameter("pointUnit");
        String blockNo = request.getParameter("blockNo");
        String fileRelativePath = request.getParameter("fileRelativePath");
        String filePrefixName = request.getParameter("filePrefixName");
        TaPoint pointVo = taPonitMapper.findPointByPointId(Integer.parseInt(pointId));
        if(pointVo == null){
            throw new Exception("点信息不存在");
        }
        pointVo.setRemarksName(remarksName);
        pointVo.setPointType(pointType);
        pointVo.setPointUnit(pointUnit);
        pointVo.setBlockNo(blockNo);
        pointVo.setFileRelativePath(fileRelativePath);
        pointVo.setFilePrefixName(filePrefixName);
        taPointService.update(pointVo);
        return JspPageConst.REDIRECT_TO_POINT_LIST_JSP_REDIRECT;
    }

    @RequestMapping(value = "work/addPointDo")
    public String addPointDo(HttpServletRequest request) throws Exception {
        String pointName = request.getParameter("pointName");
        String remarksName = request.getParameter("remarksName");
        String pointType = request.getParameter("pointType");
        String pointUnit = request.getParameter("pointUnit");
        String blockNo = request.getParameter("blockNo");
        String fileRelativePath = request.getParameter("fileRelativePath");
        String filePrefixName = request.getParameter("filePrefixName");
        if(toolHelper.isEmpty(pointName)||toolHelper.isEmpty(pointType)||toolHelper.isEmpty(fileRelativePath)||toolHelper.isEmpty(filePrefixName)){
            throw new Exception("提交的点的必填信息存在空值");
        }
        TaPoint pointVo = taPonitMapper.findPointByPointName(pointName);
        if(pointVo != null){
            throw new Exception("点信息已经存在");
        }
        pointVo = new TaPoint();
        pointVo.setPointName(pointName);
        pointVo.setRemarksName(remarksName);
        pointVo.setPointType(pointType);
        pointVo.setPointUnit(pointUnit);
        pointVo.setBlockNo(blockNo);
        pointVo.setFileRelativePath(fileRelativePath);
        pointVo.setFilePrefixName(filePrefixName);
        pointVo.setState(1);
        taPointService.insert(pointVo);
        return JspPageConst.REDIRECT_TO_POINT_LIST_JSP_REDIRECT;
    }

    @RequestMapping(value = "work/toExportDataRecode")
    public String toExportDataRecode(HttpServletRequest request) {
        //取得所有点
        List<TaPoint> pointList = taPonitMapper.getPointsByPage("","","");
        request.setAttribute("pointList",pointList);
        return JspPageConst.EXPORT_DATA_RECODE_JSP_REDIRECT;
    }
    //导出数据记录
    @RequestMapping(value = "work/exportDataRecodeDo")
    public void exportDataRecodeDo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding(Beans.UTF_8);
        JSONObject result = new JSONObject();
        try {
            String pointIds = request.getParameter("pointIds");
            String startExpDate = request.getParameter("startExpDate");
            String endExpDate = request.getParameter("endExpDate");
            String pointsInStr = "";
            if(toolHelper.isEmpty(pointIds)){
                throw new Exception("请选择点信息");
            }
            if(toolHelper.isEmpty(startExpDate)){
                throw new Exception("请选择导出开始时间");
            }
            if(toolHelper.isEmpty(endExpDate)){
                throw new Exception("请选择导出结束时间");
            }
            if(!toolHelper.compareStrDate(startExpDate,endExpDate,SysConsts.DATE_FORMAT_7)){
                throw new Exception("结束时间要大于开始时间");
            }
            for(String str : pointIds.split(";")){
                if(toolHelper.isEmpty(pointsInStr)){
                    pointsInStr = str;
                }else{
                    pointsInStr = pointsInStr+","+str;
                }
            }
            //取点信息
            List<TaPoint> taPointList = taPointService.getPointsByPointIds(pointsInStr);
            Map<Long,Map<Integer,TaInstantPointData>> exportResult = new LinkedHashMap<>();
            for(TaPoint point : taPointList){
                //单个取每个点的数据
                long startExportTimeNum = toolHelper.dateToNumDate(toolHelper.StrToDate(startExpDate, SysConsts.DATE_FORMAT_7), SysConsts.DATE_FORMAT_3);
                long endExportTimeNum = toolHelper.dateToNumDate(toolHelper.StrToDate(endExpDate, SysConsts.DATE_FORMAT_7), SysConsts.DATE_FORMAT_3);
                List<TaInstantPointData> instntPointVoList = instantPointDataHelper.findInstantPointByPointIdAndTime(startExportTimeNum, endExportTimeNum, point.getPointId());
                //循环建出能写进文件的数据格式
                for(TaInstantPointData vo : instntPointVoList){
                    if(exportResult.containsKey(vo.getCreateTimeInt())){
                        exportResult.get(vo.getCreateTimeInt()).put(vo.getPointId(),vo);
                    }else{
                        Map<Integer,TaInstantPointData> exportPointResult = new LinkedHashMap<>();
                        exportPointResult.put(vo.getPointId(),vo);
                        exportResult.put(vo.getCreateTimeInt(),exportPointResult);
                    }
                }
            }
            System.out.println(exportResult.size());
            //取点数据
            String path = csvHelper.writeCSV1(taPointList, exportResult, startExpDate, endExpDate, response);
            result.put("code",1);
            result.put("data",path);
        }catch (Exception e) {
            e.printStackTrace();
            result.put("code",-1);
            result.put("data",e.getMessage());
        } finally {
            response.getWriter().print(result);
            response.getWriter().flush();
            response.getWriter().close();
        }
    }

    @RequestMapping(value = "work/toExportUsageRecode")
    public String toExportUsageRecode(HttpServletRequest request) {
        //取得电表或者水表的点
        List<TaPoint> pointList = taPonitMapper.getPointsByPage("","","");
        request.setAttribute("pointList",pointList);
        return JspPageConst.EXPORT_USEAGE_RECODE_JSP_REDIRECT;
    }

    //导出用量报告
    @RequestMapping(value = "work/exportUsageRecodeDo")
    public void exportUsageRecodeDo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding(Beans.UTF_8);
        JSONObject result = new JSONObject();
        try {
            String pointIds = request.getParameter("pointIds");
            String startExpDate = request.getParameter("startExpDate");
            String endExpDate = request.getParameter("endExpDate");
            String takeTime = request.getParameter("takeTime");
            String pointsInStr = "";
            if(toolHelper.isEmpty(pointIds)){
                throw new Exception("请选择点信息");
            }
            if(toolHelper.isEmpty(startExpDate)){
                throw new Exception("请选择导出开始时间");
            }
            if(toolHelper.isEmpty(endExpDate)){
                throw new Exception("请选择导出结束时间");
            }
            if(!toolHelper.compareStrDate(startExpDate,endExpDate,SysConsts.DATE_FORMAT_7)){
                throw new Exception("结束时间要大于开始时间");
            }
            for(String str : pointIds.split(";")){
                if(toolHelper.isEmpty(pointsInStr)){
                    pointsInStr = str;
                }else{
                    pointsInStr = pointsInStr+","+str;
                }
            }
            //取点信息
            List<TaPoint> taPointList = taPointService.getPointsByPointIds(pointsInStr);
        /*    List<TaUsagePointData> resultList = new ArrayList<>();*/
            Map<Date,Map<Integer,Double>> exportResult = new LinkedHashMap<>();
            for(TaPoint vo : taPointList){
                List<TaUsagePointData> taInstantPointData = usagePointDataHelper.queryUsagePointDataSum(startExpDate, endExpDate, takeTime, vo.getPointId());
                //循环建出能写进文件的数据格式
                //第一个时间分界点  当数据时间大于这个时  要重算分界点 分界点决定数据属于哪个key
                Date firstIntervalTime = toolHelper.makeDateByDateAndHour(taInstantPointData.get(0).getDateShow(), takeTime);
                for(TaUsagePointData usageVo : taInstantPointData){
                    //如果第一条数据小于第一分界点，则数据放入map  key 就是第一份节点
                    if(toolHelper.compareDate(usageVo.getCreateTime(),firstIntervalTime)){
                        if(exportResult.containsKey(firstIntervalTime)){
                            //如果存在这个可以  则取出老数据加上新数据 在放回去
                            //要判断一下点是否已经存在了 不存在要创建的
                            Map<Integer, Double> integerDoubleMap = exportResult.get(firstIntervalTime);
                            if(integerDoubleMap.containsKey(vo.getPointId())){
                                double oldDouble = integerDoubleMap.get(vo.getPointId());
                                double newDouble = toolHelper.doubleSum(oldDouble, usageVo.getPointUsage());
                                integerDoubleMap.put(vo.getPointId(),newDouble);
                            }else{
                                integerDoubleMap.put(vo.getPointId(),usageVo.getPointUsage());
                            }
                        }else{
                            Map<Integer,Double> exportPointResult = new LinkedHashMap<>();
                            exportPointResult.put(vo.getPointId(),usageVo.getPointUsage());
                            //小时应该事 页面查询的时间分界点
                            //先生成当前的key
                            exportResult.put(firstIntervalTime,exportPointResult);
                        }
                    }else{
                        //如果第一条数据大于第一分界点，则日期加一 然后计算新的分界点
                        firstIntervalTime = toolHelper.addSubDate(firstIntervalTime,1);
                        if(exportResult.containsKey(firstIntervalTime)){
                            Map<Integer, Double> integerDoubleMap = exportResult.get(firstIntervalTime);
                            if(integerDoubleMap.containsKey(vo.getPointId())){
                                double oldDouble = integerDoubleMap.get(vo.getPointId());
                                double newDouble = toolHelper.doubleSum(oldDouble, usageVo.getPointUsage());
                                integerDoubleMap.put(vo.getPointId(),newDouble);
                            }else{
                                integerDoubleMap.put(vo.getPointId(),usageVo.getPointUsage());
                            }
                        }else{
                            Map<Integer,Double> exportPointResult = new LinkedHashMap<>();
                            exportPointResult.put(vo.getPointId(),usageVo.getPointUsage());
                            //小时应该事 页面查询的时间分界点
                            //先生成当前的key
                            exportResult.put(firstIntervalTime,exportPointResult);
                        }
                    }
                }
              /*  resultList.addAll(taInstantPointData);*/
            }
            //取点数据
            //List<TaPointData> taPointDataList = taPointDataService.queryPointDataSum(startExpDate, endExpDate, takeTime,pointsInStr);
            String path = csvHelper.writeCSV2(taPointList,exportResult,takeTime,startExpDate,endExpDate);
            result.put("code",1);
            result.put("data",path);
        }catch (Exception e) {
            e.printStackTrace();
            result.put("code",-1);
            result.put("data",e.getMessage());
        } finally {
            response.getWriter().print(result);
            response.getWriter().flush();
            response.getWriter().close();
        }
    }

    @RequestMapping(value = "work/toExportDeviceChart")
    public String toExportDeviceChart(HttpServletRequest request) throws Exception {
        //取得非 电表水表的点
        List<TaPoint> pointList = taPonitMapper.getPointsByPage("","","");
        request.setAttribute("pointList",pointList);
        return JspPageConst.EXPORT_DEVICE_CHAER_JSP_REDIRECT;
    }

    //导出设备图表-设备 折线图
    @RequestMapping(value = "work/exportDeviceChartDo")
    public void exportDeviceChartDo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding(Beans.UTF_8);
        JSONObject result = new JSONObject();
        try {
            String pointIds = request.getParameter("pointIds");
            String startExpDate = request.getParameter("startExpDate");
            String endExpDate = request.getParameter("endExpDate");
            String pointsInStr = "";
            if(toolHelper.isEmpty(pointIds)){
                throw new Exception("请选择点信息");
            }
            if(toolHelper.isEmpty(startExpDate)){
                throw new Exception("请选择导出开始时间");
            }
            if(toolHelper.isEmpty(endExpDate)){
                throw new Exception("请选择导出结束时间");
            }
            if(!toolHelper.compareStrDate(startExpDate,endExpDate,SysConsts.DATE_FORMAT_1)){
                throw new Exception("结束时间要大于开始时间");
            }
            for(String str : pointIds.split(";")){
                if(toolHelper.isEmpty(pointsInStr)){
                    pointsInStr = str;
                }else{
                    pointsInStr = pointsInStr+","+str;
                }
            }
            //取点信息
            List<TaPoint> taPointList = taPointService.getPointsByPointIds(pointsInStr);
            //取点数据
           //t List<TaPointData> taPointDataLis = taPointDataService.queryPointDeviceChart(startExpDate, endExpDate,pointsInStr);
            List<TaInstantPointData> taPointDataLis = new ArrayList<>();
            for(TaPoint point : taPointList){
                //单个取每个点的数据
                long startExportTimeNum = toolHelper.dateToNumDate(toolHelper.StrToDate(startExpDate, SysConsts.DATE_FORMAT_1), SysConsts.DATE_FORMAT_3);
                long endExportTimeNum = toolHelper.dateToNumDate(toolHelper.StrToDate(endExpDate, SysConsts.DATE_FORMAT_1), SysConsts.DATE_FORMAT_3);
                List<TaInstantPointData> instntPointVoList = instantPointDataHelper.findInstantPointByPointIdAndTime(startExportTimeNum, endExportTimeNum, point.getPointId());
                taPointDataLis.addAll(instntPointVoList);
            }
            List<String> hourIntervalAllList = toolHelper.getHourIntervalAllList(startExpDate, endExpDate);
            String path = jFreeChartHelper.createDeviceChartStart(taPointList, taPointDataLis, startExpDate, endExpDate,"设备值统计","日期","值",hourIntervalAllList);
            result.put("code",1);
            result.put("data",path);
        }catch (Exception e) {
            e.printStackTrace();
            result.put("code",-1);
            result.put("data",e.getMessage());
        } finally {
            response.getWriter().print(result);
            response.getWriter().flush();
            response.getWriter().close();
        }
    }

    @RequestMapping(value = "work/toExportUsageDeviceChart")
    public String toExportUsageDeviceChart(HttpServletRequest request) throws Exception {
        //取得非 电表水表的点
        List<TaPoint> pointList = taPonitMapper.getPointsByPage("","","");
        request.setAttribute("pointList",pointList);
        return JspPageConst.EXPORT_USAGE_DEVICE_CHAER_JSP_REDIRECT;
    }


    //导出用量图表-设备 柱形图
    @RequestMapping(value = "work/exportUsageChartDo")
    public void exportUsageChartDo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding(Beans.UTF_8);
        JSONObject result = new JSONObject();
        try {
            String pointIds = request.getParameter("pointIds");
            String dateType = request.getParameter("type");
            String startExpDate = request.getParameter("startExpDate");
            String endExpDate = request.getParameter("endExpDate");
            String reportType = request.getParameter("reportType");
            String pointsInStr = "";
            if(toolHelper.isEmpty(pointIds)){
                throw new Exception("请选择点信息");
            }
            if(toolHelper.isEmpty(startExpDate)){
                throw new Exception("请选择导出开始时间");
            }
            if(toolHelper.isEmpty(endExpDate)){
                throw new Exception("请选择导出结束时间");
            }
            if(toolHelper.isEmpty(reportType)){
                throw new Exception("没有取得报告类型");
            }
            if(EnumUsageTimeTypeDefine.date.toString().equals(dateType)){
                if(!toolHelper.compareStrDate(startExpDate,endExpDate,SysConsts.DATE_FORMAT_7)){
                    throw new Exception("结束时间要大于开始时间");
                }
            }else if(EnumUsageTimeTypeDefine.mon.toString().equals(dateType)){
                if(!toolHelper.compareStrDate(startExpDate,endExpDate,SysConsts.DATE_FORMAT_8)){
                    throw new Exception("结束时间要大于开始时间");
                }
            }else if(EnumUsageTimeTypeDefine.week.toString().equals(dateType)){
                int startDateNum = Integer.parseInt(startExpDate);
                int endDateNum = Integer.parseInt(endExpDate);
                if(startDateNum>endDateNum){
                    throw new Exception("结束时间要大于开始时间");
                }
            }
            for(String str : pointIds.split(";")){
                if(toolHelper.isEmpty(pointsInStr)){
                    pointsInStr = str;
                }else{
                    pointsInStr = pointsInStr+","+str;
                }
            }
            //取点信息
            List<TaPoint> taPointList = taPointService.getPointsByPointIds(pointsInStr);
            //取点数据
            List<TaUsagePointDataDate> taUsagePointDataDates = new ArrayList<>();
            List<String> dateIntervalAllList = null;
            if(EnumUsageTimeTypeDefine.date.toString().equals(dateType)){
                taUsagePointDataDates = usagePointDataDateHelper.queryUsagePointDataSum(startExpDate, endExpDate, pointsInStr);
                //取得所有要显示的日期
                dateIntervalAllList = toolHelper.getDateIntervalAllList(startExpDate, endExpDate);
            }else if(EnumUsageTimeTypeDefine.mon.toString().equals(dateType)){
                List<TaUsagePointDataMon> taUsagePointDataWeek = usagePointDataMonHelper.queryUsagePointDataMonSum(startExpDate, endExpDate, pointsInStr);
                for(TaUsagePointDataMon vo : taUsagePointDataWeek){
                    TaUsagePointDataDate dateVo = new TaUsagePointDataDate();
                    dateVo.setPointId(vo.getPointId());
                    dateVo.setDateShow(vo.getDateShow());
                    dateVo.setPointData(vo.getPointData());
                    taUsagePointDataDates.add(dateVo);
                }
                dateIntervalAllList = toolHelper.getMonIntervalAllList(startExpDate, endExpDate);
            }else if(EnumUsageTimeTypeDefine.week.toString().equals(dateType)){
                List<TaUsagePointDataWeek> taUsagePointDataMon = usagePointDataWeekHelper.queryUsagePointDataWeekSum(startExpDate, endExpDate, pointsInStr);
                for(TaUsagePointDataWeek vo : taUsagePointDataMon){
                    TaUsagePointDataDate dateVo = new TaUsagePointDataDate();
                    dateVo.setPointId(vo.getPointId());
                    dateVo.setDateShow(vo.getDateShow());
                    dateVo.setPointData(vo.getPointData());
                    taUsagePointDataDates.add(dateVo);
                }
                dateIntervalAllList = toolHelper.getWeekIntervalAllList(startExpDate, endExpDate);
            }
            String path ="";
            if(EnumImgReportTypeDefine.bar.toString().equals(reportType)){
                path = jFreeChartHelper.createUsageDeviceBarChartStart(taPointList, taUsagePointDataDates, startExpDate, endExpDate,"水电用量","日期","用量",dateIntervalAllList);
            }else if(EnumImgReportTypeDefine.series.toString().equals(reportType)){
                path = jFreeChartHelper.createUsageDeviceSeriesChartStart(taPointList, taUsagePointDataDates, startExpDate, endExpDate,"水电用量","日期","用量",dateIntervalAllList);
            }
            result.put("code",1);
            result.put("data",path);
        }catch (Exception e) {
            e.printStackTrace();
            result.put("code",-1);
            result.put("data",e.getMessage());
        } finally {
            response.getWriter().print(result);
            response.getWriter().flush();
            response.getWriter().close();
        }
    }

    @RequestMapping(value = "work/toLoadPointSelect")
    public String toLoadPointSelect(HttpServletRequest request) throws Exception {
        String data_recode = request.getParameter("selectId");
        String pointType = request.getParameter("pointType");
        request.setAttribute("selectId",data_recode);
        //取得所有点
        List<TaPoint> pointList = taPonitMapper.getPointsByPage("","",pointType);
        request.setAttribute("pointList",pointList);
        return JspPageConst.LOAD__POINT_SELECT_JSP_REDIRECT;
    }

    @RequestMapping(value = "work/findPoints")
    public void findPoints(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(Beans.UTF_8);
        JSONObject result = new JSONObject();
        try {
            String pointName = request.getParameter("pointName");
            List<TaPoint> points = null;
         /*   if(toolHelper.isEmpty(pointName)){*/
                points = taPointService.likePointByPointName(pointName);
           /* }else{
                points = taPonitMapper.getPointsByPage();
            }*/
            JSONArray jsonarray = new JSONArray();
            for(TaPoint vo : points){
                JSONObject json = new JSONObject();
                json.put("pointId",vo.getPointId());
                json.put("pointName",vo.getPointName());
                jsonarray.add(json);
            }
            result.put("code",1);
            result.put("data",jsonarray);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code",-1);
            result.put("data",e.getMessage());
        } finally {
            response.getWriter().print(result);
            response.getWriter().flush();
            response.getWriter().close();
        }
    }

    /**
     * 下载文件
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "work/downloadCsv")
    public void downloadCsv(HttpServletRequest request, HttpServletResponse response) throws Exception {
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        OutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            String downloadZipPath = request.getParameter("filePath");
            File file = new File(downloadZipPath);
            if(!file.exists()){
                throw new Exception("文件："+downloadZipPath+" 不存在！");
            }
            String packageName = file.getName();
           // String packageName = downloadZipPath.substring(downloadZipPath.lastIndexOf(ParamConsts.SEPERRE_STR),downloadZipPath.length()-1);
            fileInputStream = new FileInputStream(new File(downloadZipPath));
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            outputStream = response.getOutputStream();
            bufferedOutputStream = new BufferedOutputStream(outputStream);

            toolHelper.setFileDownloadHeader(request, response, packageName);
            int byteRead = 0;
            byte[] buffer = new byte[8192];
            while ((byteRead = bufferedInputStream.read(buffer, 0, 8192)) != -1) {
                bufferedOutputStream.write(buffer, 0, byteRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedOutputStream!=null) {
                try {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    System.out.println("bos.close has occur a problem.");
                    e.printStackTrace();
                }
            }
            if (outputStream!=null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    System.out.println("fos.close has occur a problem.");
                    e.printStackTrace();
                }
            }
            if (bufferedInputStream!=null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    System.out.println("bis.close has occur a problem.");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取文件流
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/work/readImgIo", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> readImgIo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BufferedImage bi;
        ByteArrayOutputStream baos =null;
        try {
            BASE64Encoder encoder = new sun.misc.BASE64Encoder();
            String downloadZipPath = request.getParameter("filePath");
            File file = new File(downloadZipPath);
            if(!file.exists()){
                throw new Exception("文件："+downloadZipPath+" 不存在！");
            }
            bi = ImageIO.read(file);
            baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
            byte[] bytes = baos.toByteArray();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(baos!=null){
                baos.close();
            }
        }
        return null;
    }

}
