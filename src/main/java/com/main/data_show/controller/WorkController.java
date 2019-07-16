package com.main.data_show.controller;

import cn.com.enorth.utility.Beans;
import com.github.pagehelper.PageInfo;
import com.main.data_show.consts.JspPageConst;
import com.main.data_show.helper.CSVHelper;
import com.main.data_show.helper.JFreeChartHelper;
import com.main.data_show.helper.ToolHelper;
import com.main.data_show.mapper.TaPonitDataMapper;
import com.main.data_show.mapper.TaPonitMapper;
import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaPointData;
import com.main.data_show.service.TaPointDataService;
import com.main.data_show.service.TaPointService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspPage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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


    @RequestMapping(value = "work/toPointList")
    public String toPointList(HttpServletRequest request) {
        int pageNo = 1;
        int pageSize = 3;
        String curPageNo = request.getParameter("pageNo");
        if(!toolHelper.isEmpty(curPageNo)){
            pageNo = Integer.parseInt(curPageNo);
        }
        //取所有点的列表
        List<TaPoint> pointsByPage = taPointService.getPointsByPage(pageNo, pageSize);
        PageInfo<TaPoint> pageInfo=new PageInfo<>(pointsByPage);
        request.setAttribute("pointList", pointsByPage);
        request.setAttribute("totalPage", pageInfo.getTotal());
        request.setAttribute("lastPage", pageInfo.getLastPage());
        request.setAttribute("pageNum", pageInfo.getPageNum());
        request.setAttribute("pageSize", pageInfo.getPageSize());
        request.setAttribute("firstPage", pageInfo.getFirstPage());
        request.setAttribute("nextPage", pageInfo.getNextPage());

        return JspPageConst.TO_POINT_LIST_JSP_REDIRECT;
    }

    @RequestMapping(value = "work/toEditPoint")
    public String toEditPoint(HttpServletRequest request) {
        String pointId = request.getParameter("pointId");
        TaPoint pointVo = taPonitMapper.findPointByPointId(Integer.parseInt(pointId));
        request.setAttribute("pointVo",pointVo);
        return JspPageConst.TO_EDIT_POINT_JSP_REDIRECT;
    }

    @RequestMapping(value = "work/editPointDo")
    public String editPointDo(HttpServletRequest request) throws Exception {
        String pointId = request.getParameter("pointId");
        String remarksName = request.getParameter("remarksName");
        String pointType = request.getParameter("pointType");
        String pointUnit = request.getParameter("pointUnit");
        String blockNo = request.getParameter("blockNo");
        TaPoint pointVo = taPonitMapper.findPointByPointId(Integer.parseInt(pointId));
        if(pointVo == null){
            throw new Exception("点信息不存在");
        }
        pointVo.setRemarksName(remarksName);
        pointVo.setPointType(pointType);
        pointVo.setPointUnit(pointUnit);
        pointVo.setBlockNo(blockNo);
        taPointService.update(pointVo);
        return JspPageConst.REDIRECT_TO_POINT_LIST_JSP_REDIRECT;
    }

    @RequestMapping(value = "work/toExportDataRecode")
    public String toExportDataRecode(HttpServletRequest request) {
        //取得所有点
        List<TaPoint> pointList = taPonitMapper.getPointsByPage();
        request.setAttribute("pointList",pointList);
        return JspPageConst.EXPORT_DATA_RECODE_JSP_REDIRECT;
    }
    //导出数据记录
    @RequestMapping(value = "work/exportDataRecodeDo")
    public void exportDataRecodeDo(HttpServletRequest request) throws Exception {
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
        List<TaPointData> taPointDataList = taPonitDataMapper.queryPointData(startExpDate, endExpDate, pointsInStr);
        csvHelper.writeCSV1(taPointList,taPointDataList);
    }

    @RequestMapping(value = "work/toExportUsageRecode")
    public String toExportUsageRecode(HttpServletRequest request) {
        //取得电表或者水表的点
        List<TaPoint> pointList = taPonitMapper.getPointsByPage();
        request.setAttribute("pointList",pointList);
        return JspPageConst.EXPORT_USEAGE_RECODE_JSP_REDIRECT;
    }

    //导出用量报告
    @RequestMapping(value = "work/exportUsageRecodeDo")
    public void exportUsageRecodeDo(HttpServletRequest request) throws Exception {
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
        List<TaPointData> taPointDataList = taPointDataService.queryPointDataSum(startExpDate, endExpDate, takeTime,pointsInStr);
        csvHelper.writeCSV2(taPointList,taPointDataList,takeTime);
    }

    @RequestMapping(value = "work/toExportDeviceChart")
    public String toExportDeviceChart(HttpServletRequest request) throws Exception {
        //取得非 电表水表的点
        List<TaPoint> pointList = taPonitMapper.getPointsByPage();
        request.setAttribute("pointList",pointList);
        return JspPageConst.EXPORT_DEVICE_CHAER_JSP_REDIRECT;
    }

    //导出设备图表-设备
    @RequestMapping(value = "work/exportDeviceChartDo")
    public void exportDeviceChartDo(HttpServletRequest request) throws Exception {
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
        List<TaPointData> taPointDataList = taPointDataService.queryPointDeviceChart(startExpDate, endExpDate,pointsInStr);

        jFreeChartHelper.createDeviceChartStart(taPointList,taPointDataList);

        jFreeChartHelper.createUsageDeviceChartStart(taPointList,taPointDataList);
    }

    @RequestMapping(value = "work/toExportUsageDeviceChart")
    public String toExportUsageDeviceChart(HttpServletRequest request) throws Exception {
        //取得非 电表水表的点
        List<TaPoint> pointList = taPonitMapper.getPointsByPage();
        request.setAttribute("pointList",pointList);
        return JspPageConst.EXPORT_USAGE_DEVICE_CHAER_JSP_REDIRECT;
    }

    @RequestMapping(value = "work/toLoadPointSelect")
    public String toLoadPointSelect(HttpServletRequest request) throws Exception {
        String data_recode = request.getParameter("selectId");
        request.setAttribute("selectId",data_recode);
        //取得所有点
        List<TaPoint> pointList = taPonitMapper.getPointsByPage();
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

}
