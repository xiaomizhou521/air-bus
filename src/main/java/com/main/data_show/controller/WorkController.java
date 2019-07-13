package com.main.data_show.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspPage;
import java.util.List;
import java.util.Map;

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

        return JspPageConst.TO_EDIT_POINT_JSP_REDIRECT;
    }

    @RequestMapping(value = "work/editPointDo")
    public String editPointDo(HttpServletRequest request) {

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
}
