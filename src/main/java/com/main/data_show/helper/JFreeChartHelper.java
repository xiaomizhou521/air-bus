package com.main.data_show.helper;

import com.main.data_show.consts.SysConsts;
import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaPointData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.RangeType;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JFreeChartHelper {
    @Resource
    private ToolHelper toolHelper;

    //生成设备图表开始 折线图
    public void createDeviceChartStart(java.util.List<TaPoint> taPointList, List<TaPointData> taPointDataList){
         // 步骤1：创建CategoryDataset对象（准备数据）
        TimeSeriesCollection dataset = createDeviceChartDate(taPointList,taPointDataList);
        // 步骤2：根据Dataset 生成JFreeChart对象，以及做相应的设置
        JFreeChart freeChart = createChart(dataset);
        // 步骤3：将JFreeChart对象输出到文件，Servlet输出流等
        saveAsFile(freeChart, "C:\\01work\\priv_work190415\\CSV_test\\zhexin.png", 1000, 500);
    }

    //生成设备图表开始 柱形图
    public void createUsageDeviceChartStart(java.util.List<TaPoint> taPointList, List<TaPointData> taPointDataList) throws IOException {
        // 步骤1：创建CategoryDataset对象（准备数据）
        DefaultCategoryDataset usageDeviceChartData = createUsageDeviceChartData(taPointList, taPointDataList);
        // 步骤2：根据Dataset 生成JFreeChart对象，以及做相应的设置
        JFreeChart freeChart = createUageChart(usageDeviceChartData);
        // 步骤3：将JFreeChart对象输出到文件，Servlet输出流等
        saveAsFile(freeChart, "C:\\01work\\priv_work190415\\CSV_test\\zhuxing.png", 1000, 500);
    }

    public TimeSeriesCollection createDeviceChartDate(java.util.List<TaPoint> taPointList, List<TaPointData> taPointDataList){
        Map<Integer,String> ponitMap = new HashMap<>();
        for(TaPoint pointVo : taPointList){
            ponitMap.put(pointVo.getPointId(),"点名："+pointVo.getPointName()+"("+pointVo.getRemarksName()+")"+",类型："+pointVo.getPointType()+",单位："+pointVo.getPointUnit());
        }
        Map<Integer,TimeSeries> pointDateMap = new HashMap<>();
        //创造图标数据
        for(TaPointData pointDateVo : taPointDataList){
            TimeSeries timeSeries = null;
            if(pointDateMap.containsKey(pointDateVo.getPointId())){
                 timeSeries = pointDateMap.get(pointDateVo.getPointId());
                if(toolHelper.isNumeric(pointDateVo.getPointData())){
                    timeSeries.add(new Hour(pointDateVo.getCreateTime()), Double.valueOf(pointDateVo.getPointData()));
                }else{
                    timeSeries.add(new Hour(pointDateVo.getCreateTime()), 0);
                }
            }else{
                 timeSeries = new TimeSeries(ponitMap.get(pointDateVo.getPointId()), Hour.class);
                if(toolHelper.isNumeric(pointDateVo.getPointData())){
                    timeSeries.add(new Hour(pointDateVo.getCreateTime()), Double.valueOf(pointDateVo.getPointData()));
                }else{
                    timeSeries.add(new Hour(pointDateVo.getCreateTime()), 0);
                }
                pointDateMap.put(pointDateVo.getPointId(),timeSeries);
            }
        }
        //遍历 map 每个都是一条时间折线
        TimeSeriesCollection lineDataset = new TimeSeriesCollection();
        for(Map.Entry<Integer,TimeSeries> map : pointDateMap.entrySet()){
            lineDataset.addSeries(map.getValue());
        }
        return lineDataset;
    }

    public DefaultCategoryDataset createUsageDeviceChartData(java.util.List<TaPoint> taPointList, List<TaPointData> taPointDataList){
        Map<Integer,String> ponitMap = new HashMap<>();
        for(TaPoint pointVo : taPointList){
            ponitMap.put(pointVo.getPointId(),"点名："+pointVo.getPointName()+"("+pointVo.getRemarksName()+")"+",类型："+pointVo.getPointType()+",单位："+pointVo.getPointUnit());
        }
        DefaultCategoryDataset dateSet = new DefaultCategoryDataset();
        dateSet.setValue(100,"好","苹果");
        //创造图标数据
        for(TaPointData pointDateVo : taPointDataList){
            if(toolHelper.isNumeric(pointDateVo.getPointData())){
                dateSet.setValue(Double.valueOf(pointDateVo.getPointData()),ponitMap.get(pointDateVo.getPointId()),pointDateVo.getDateShow()+pointDateVo.getHourShow());
            }else{
                dateSet.setValue(0,ponitMap.get(pointDateVo.getPointId()),pointDateVo.getDateShow()+pointDateVo.getHourShow());
            }
        }
        return dateSet;
    }

    // 根据CategoryDataset生成JFreeChart对象
    public static JFreeChart createChart(TimeSeriesCollection lineDataset) {
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(
                "设备图表", 		// 标题
                "时间", 		// categoryAxisLabel （category轴，横轴，X轴的标签）
                "值", 	// valueAxisLabel（value轴，纵轴，Y轴的标签）
                lineDataset,// dataset
                true, 		// legend
                true, 		// tooltips
                true); 		// URLs

        // 配置字体（解决中文乱码的通用方法）
        Font xfont = new Font("宋体", Font.PLAIN, 16); // X轴
        Font yfont = new Font("宋体", Font.PLAIN, 16); // Y轴
        Font kfont = new Font("宋体", Font.PLAIN, 14); // 底部
        Font titleFont = new Font("隶书", Font.BOLD, 20); // 图片标题

        jfreechart.setBackgroundPaint(Color.white);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot(); // 获得 plot：XYPlot！
        xyplot.getDomainAxis().setLabelFont(xfont);
        xyplot.getRangeAxis().setLabelFont(yfont);
        jfreechart.getLegend().setItemFont(kfont);
        jfreechart.getTitle().setFont(titleFont);
        //设置时间格式，及间隔,同时也解决了乱码问题
        DateAxis dateaxis = (DateAxis)xyplot.getDomainAxis();
        SimpleDateFormat sfd = new SimpleDateFormat(SysConsts.DATE_FORMAT_2);
        dateaxis.setDateFormatOverride(sfd);
        dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.HOUR, 1, new SimpleDateFormat(SysConsts.DATE_FORMAT_2)));
        dateaxis.setVerticalTickLabels(true);
        xyplot.setDomainAxis(dateaxis);

        // 以下的设置可以由用户定制，也可以省略
        XYPlot plot = (XYPlot) jfreechart.getPlot();
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) plot.getRenderer();
        // 设置网格背景颜色
        plot.setBackgroundPaint(Color.white);
        // 设置网格竖线颜色
        plot.setDomainGridlinePaint(Color.pink);
        // 设置网格横线颜色
        plot.setRangeGridlinePaint(Color.pink);
        // 设置曲线图与xy轴的距离
        plot.setAxisOffset(new RectangleInsets(0D, 0D, 0D, 10D));
        // 设置曲线是否显示数据点
//		xylineandshaperenderer.setBaseShapesVisible(true);
        // 设置曲线显示各数据点的值
//		XYItemRenderer xyitem = plot.getRenderer();
//		xyitem.setBaseItemLabelsVisible(true);
//		xyitem.setBasePositiveItemLabelPosition(new ItemLabelPosition(
//				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
//		xyitem.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
//		xyitem.setBaseItemLabelFont(new Font("Dialog", 1, 14));
//		plot.setRenderer(xyitem);

        return jfreechart;
    }


    // 保存为文件
    public static void saveAsFile(JFreeChart chart, String outputPath,
                                  int weight, int height) {
        FileOutputStream out = null;
        try {
            File outFile = new File(outputPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(outputPath);
            // 保存为PNG文件
            ChartUtilities.writeChartAsPNG(out, chart, weight, height);
            // 保存为JPEG文件
            //ChartUtilities.writeChartAsJPEG(out, chart, 500, 400);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }

    public JFreeChart createUageChart(DefaultCategoryDataset dateSet) throws IOException {
        JFreeChart chart = ChartFactory.createBarChart("柱状图",//标题
                "水果",//目录轴的显示标签
                "单位:百万",//数值的显示标签
                dateSet,//数据
                PlotOrientation.VERTICAL,//图标方向  水平/垂直
                true,//是否显示图例
                false,//是否生成工具
                false); //是否生成URL链接

        CategoryPlot categoryPlot = chart.getCategoryPlot();//图部分
        CategoryAxis domainAxis = categoryPlot.getDomainAxis();//X轴

        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);//X轴下标  90°显示
        domainAxis.setMaximumCategoryLabelLines(10);//自动换行    最多显示多少行
        domainAxis.setLabelFont(new Font("黑体",Font.BOLD,20));//下标
        domainAxis.setTickLabelFont(new Font("宋体",Font.BOLD,20));//X轴标题
        ValueAxis rangeAxis = categoryPlot.getRangeAxis();//Y轴
        rangeAxis.setLabelFont(new Font("黑体",Font.BOLD,20));//下标
        rangeAxis.setTickLabelFont(new Font("宋体",Font.BOLD,20));//Y轴标题

        NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();
        numberAxis.setAutoTickUnitSelection(false);//取消自动设置Y轴刻度
        numberAxis.setTickUnit(new NumberTickUnit(10));//刻度大小
        numberAxis.setAutoRangeStickyZero(true);//和下面一行搭配使用   设置Y轴都是正数
        numberAxis.setRangeType(RangeType.POSITIVE);
        numberAxis.setNumberFormatOverride(new DecimalFormat("0.00"));//设置Y轴上的数值精度

        chart.getLegend().setItemFont( new Font("黑体",Font.BOLD,20));//图标字体
        chart.getTitle().setFont( new Font("黑体",Font.BOLD,20));
        BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();//图形修改
      //  renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",new DecimalFormat("0.00")));//设置柱状图上的数值精度
        renderer.setItemMargin(0);//设置柱子之间的距离
        renderer.setPositiveItemLabelPositionFallback(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));
       // renderer.setDefaultItemLabelFont(new Font("黑体",Font.BOLD,20));
        renderer.setDrawBarOutline(false);
        renderer.setMaximumBarWidth(0.4); //设置柱子宽度
        renderer.setMinimumBarLength(0.00); //设置柱子高度
        return chart;
    }

}
