package com.main.data_show.helper;

import com.main.data_show.consts.ApplicationConsts;
import com.main.data_show.consts.ParamConsts;
import com.main.data_show.consts.SysConsts;
import com.main.data_show.pojo.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.RangeType;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Service
public class JFreeChartHelper {
    @Autowired
    private ToolHelper toolHelper;
    @Autowired
    private Environment env;

    //生成设备图表开始 折线图
    public String createDeviceChartStart(java.util.List<TaPoint> taPointList, List<TaInstantPointData> taPointDataList,String startTime,String endTime,String title,String xLabel,String yLabel,List<String> dateIntervalAllList) throws Exception {
         // 步骤1：创建CategoryDataset对象（准备数据）
        TimeSeriesCollection dataset = createDeviceChartDate(taPointList,taPointDataList,dateIntervalAllList);
        // 步骤2：根据Dataset 生成JFreeChart对象，以及做相应的设置
        JFreeChart freeChart = createChart(dataset,title,xLabel,yLabel);
       /* // 步骤1：创建CategoryDataset对象（准备数据）
        DefaultCategoryDataset dataset = createDeviceInstantChartDate(taPointList,taPointDataList,dateIntervalAllList);
        // 步骤2：根据Dataset 生成JFreeChart对象，以及做相应的设置
        JFreeChart freeChart = createUsageChart(dataset,title,xLabel,yLabel);*/
        // 步骤3：将JFreeChart对象输出到文件，Servlet输出流等
        //生成文件名
        String readBasePath = env.getProperty(ApplicationConsts.SYS_DEMO_INSTANT_EXPORT_IMG_BASE_PATH);
        if(toolHelper.isEmpty(readBasePath)){
            throw new Exception(ApplicationConsts.SYS_DEMO_INSTANT_EXPORT_IMG_BASE_PATH+",基础路径为空!");
        }
        String imgWidth = env.getProperty(ApplicationConsts.SYS_INSTANT_DEMO_EXPORT_IMG_WIDTH);
        if(toolHelper.isEmpty(imgWidth)){
            throw new Exception(ApplicationConsts.SYS_INSTANT_DEMO_EXPORT_IMG_WIDTH+",导出图片宽设置为空!");
        }
        String imgHeight = env.getProperty(ApplicationConsts.SYS_DEMO_EXPORT_IMG_HEIGHT);
        if(toolHelper.isEmpty(imgHeight)){
            throw new Exception(ApplicationConsts.SYS_DEMO_EXPORT_IMG_HEIGHT+",导出图片高设置为空!");
        }
        if(!readBasePath.endsWith(ParamConsts.SEPERRE_STR)){
            readBasePath = readBasePath+ParamConsts.SEPERRE_STR;
        }
        File file1 = new File(readBasePath);
        if(!file1.exists()){
            file1.mkdir();
        }
        String startStr = startTime.substring(0,startTime.length()-6);
        String endStr = endTime.substring(0,endTime.length()-6);
        long time = System.currentTimeMillis();
        String fileName = "Instant("+startStr+"_"+endStr+")"+time+".png";
        String exportFilePath = readBasePath+fileName;
        int showWidth = dateIntervalAllList.size()*Integer.parseInt(imgWidth);
        if(showWidth<1000){
            showWidth = 1000;
        }
        saveAsFile(freeChart, exportFilePath, showWidth, Integer.parseInt(imgHeight));
        return exportFilePath;
    }

    //生成用量折线图
    public String createUsageDeviceSeriesChartStart(List<TaPoint> taPointList, List<TaUsagePointDataDate> taPointDataList,String startTime,String endTime,String title,String xLabel,String yLabel,List<String> dateIntervalAllList) throws Exception {
        // 步骤1：创建CategoryDataset对象（准备数据）
        DefaultCategoryDataset dataset = createDeviceUsageChartDate(taPointList,taPointDataList,dateIntervalAllList);
        // 步骤2：根据Dataset 生成JFreeChart对象，以及做相应的设置
        JFreeChart freeChart = createUsageChart(dataset,title,xLabel,yLabel);
        // 步骤3：将JFreeChart对象输出到文件，Servlet输出流等
        //生成文件名
        String readBasePath = env.getProperty(ApplicationConsts.SYS_DEMO_INSTANT_EXPORT_IMG_BASE_PATH);
        if(toolHelper.isEmpty(readBasePath)){
            throw new Exception(ApplicationConsts.SYS_DEMO_INSTANT_EXPORT_IMG_BASE_PATH+",基础路径为空!");
        }
        String imgWidth = env.getProperty(ApplicationConsts.SYS_DEMO_EXPORT_IMG_WIDTH);
        if(toolHelper.isEmpty(imgWidth)){
            throw new Exception(ApplicationConsts.SYS_DEMO_EXPORT_IMG_WIDTH+",导出图片宽设置为空!");
        }
        String imgHeight = env.getProperty(ApplicationConsts.SYS_DEMO_EXPORT_IMG_HEIGHT);
        if(toolHelper.isEmpty(imgHeight)){
            throw new Exception(ApplicationConsts.SYS_DEMO_EXPORT_IMG_HEIGHT+",导出图片高设置为空!");
        }
        if(!readBasePath.endsWith(ParamConsts.SEPERRE_STR)){
            readBasePath = readBasePath+ParamConsts.SEPERRE_STR;
        }
        File file1 = new File(readBasePath);
        if(!file1.exists()){
            file1.mkdir();
        }
        long time = System.currentTimeMillis();
        String fileName = "Instant("+startTime+"_"+endTime+")"+time+".png";
        String exportFilePath = readBasePath+fileName;
        int showWidth = dateIntervalAllList.size()*Integer.parseInt(imgWidth);
        if(showWidth<1000){
            showWidth = 1000;
        }
        saveAsFile(freeChart, exportFilePath, showWidth, Integer.parseInt(imgHeight));
        return exportFilePath;
    }

    //生成设备图表开始 柱形图
    public String createUsageDeviceBarChartStart(List<TaPoint> taPointList, List<TaUsagePointDataDate> taPointDataList,String startStr,String endStr,String title,String xLabel,String yLabel,List<String> dateIntervalAllList) throws Exception {
        //生成文件名
        String readBasePath = env.getProperty(ApplicationConsts.SYS_DEMO_USAGE_EXPORT_IMG_BASE_PATH);
        if(toolHelper.isEmpty(readBasePath)){
            throw new Exception(ApplicationConsts.SYS_DEMO_USAGE_EXPORT_IMG_BASE_PATH+",基础路径为空!");
        }
        String imgWidth = env.getProperty(ApplicationConsts.SYS_DEMO_EXPORT_IMG_WIDTH);
        if(toolHelper.isEmpty(imgWidth)){
            throw new Exception(ApplicationConsts.SYS_DEMO_EXPORT_IMG_WIDTH+",导出图片宽设置为空!");
        }
        String imgHeight = env.getProperty(ApplicationConsts.SYS_DEMO_EXPORT_IMG_HEIGHT);
        if(toolHelper.isEmpty(imgHeight)){
            throw new Exception(ApplicationConsts.SYS_DEMO_EXPORT_IMG_HEIGHT+",导出图片高设置为空!");
        }
        // 步骤1：创建CategoryDataset对象（准备数据）
        DefaultCategoryDataset usageDeviceChartData = createUsageDeviceChartData(taPointList, taPointDataList, dateIntervalAllList);
        // 步骤2：根据Dataset 生成JFreeChart对象，以及做相应的设置
        JFreeChart freeChart = createUageChart(usageDeviceChartData, title, xLabel, yLabel);
        // 步骤3：将JFreeChart对象输出到文件，Servlet输出流等
        if(!readBasePath.endsWith(ParamConsts.SEPERRE_STR)){
            readBasePath = readBasePath+ParamConsts.SEPERRE_STR;
        }
        File file1 = new File(readBasePath);
        if(!file1.exists()){
            file1.mkdir();
        }
        long time = System.currentTimeMillis();
        String fileName = "Usage("+startStr+"_"+endStr+")"+time+".png";
        String exportFilePath = readBasePath+fileName;
        int showWidth = dateIntervalAllList.size()*Integer.parseInt(imgWidth);
        if(showWidth<1000){
            showWidth = 1000;
        }
        saveAsFile(freeChart, exportFilePath, showWidth, Integer.parseInt(imgHeight));
        return exportFilePath;
    }

    public TimeSeriesCollection createDeviceChartDate(java.util.List<TaPoint> taPointList, List<TaInstantPointData> taPointDataList,List<String> hourIntervalAllList){
        Map<Integer,String> ponitMap = new HashMap<>();
        for(TaPoint pointVo : taPointList){
            ponitMap.put(pointVo.getPointId(),"点名："+pointVo.getPointName()+"("+pointVo.getRemarksName()+")"+",类型："+pointVo.getPointType()+",单位："+pointVo.getPointUnit());
        }
        Map<Integer,TimeSeries> pointDateMap = new HashMap<>();
        for(TaPoint point : taPointList){
            TimeSeries timeSeries = null;
            int pointId = point.getPointId();
            for(String str : hourIntervalAllList){
                boolean flg = false;
                for(TaInstantPointData pointDateVo : taPointDataList){
                    String curCreateTime = String.valueOf(pointDateVo.getCreateTimeInt());
                    if(pointId == pointDateVo.getPointId()&&str.equals(curCreateTime)){
                        flg = true;
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
                }
                //每个日期都应该有记录 没有查到 就占位
                if(!flg){
                    Date date = toolHelper.StrToDate(str, SysConsts.DATE_FORMAT_3);
                    if(pointDateMap.containsKey(pointId)){
                        timeSeries = pointDateMap.get(pointId);
                        timeSeries.add(new Hour(date), 0);
                    }else{
                        timeSeries = new TimeSeries(ponitMap.get(pointId), Hour.class);
                        timeSeries.add(new Hour(date), 0);
                        pointDateMap.put(pointId,timeSeries);
                    }
                }
            }
        }
        //创造图标数据
        /*for(TaInstantPointData pointDateVo : taPointDataList){
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
        }*/
        //遍历 map 每个都是一条时间折线
        TimeSeriesCollection lineDataset = new TimeSeriesCollection();
        for(Map.Entry<Integer,TimeSeries> map : pointDateMap.entrySet()){
            lineDataset.addSeries(map.getValue());
        }
        return lineDataset;
    }
//用量折线图
    public DefaultCategoryDataset createDeviceUsageChartDate(List<TaPoint> taPointList, List<TaUsagePointDataDate> taPointDataList,List<String> dateIntervalAllList){
        Map<Integer,String> ponitMap = new HashMap<>();
        for(TaPoint pointVo : taPointList){
            ponitMap.put(pointVo.getPointId(),"点名："+pointVo.getPointName()+"("+pointVo.getRemarksName()+")"+",单位："+pointVo.getPointUnit());
        }
        DefaultCategoryDataset mDataset = new DefaultCategoryDataset();
        for(String str : dateIntervalAllList){
            for(Map.Entry<Integer,String> map: ponitMap.entrySet()){
                mDataset.setValue(0,map.getValue(),str);
            }
        }
        //创造图标数据
        for(TaUsagePointDataDate pointDateVo : taPointDataList){
            mDataset.addValue(pointDateVo.getPointData(), ponitMap.get(pointDateVo.getPointId()), String.valueOf(pointDateVo.getDateShow()));
        }
        return mDataset;
    }
    //设备折线图
    public DefaultCategoryDataset createDeviceInstantChartDate(List<TaPoint> taPointList,List<TaInstantPointData> taPointDataList,List<String> dateIntervalAllList){
        Map<Integer,String> ponitMap = new HashMap<>();
        for(TaPoint pointVo : taPointList){
            ponitMap.put(pointVo.getPointId(),"点名："+pointVo.getPointName()+"("+pointVo.getRemarksName()+")"+",单位："+pointVo.getPointUnit());
        }
        DefaultCategoryDataset mDataset = new DefaultCategoryDataset();
        for(String str : dateIntervalAllList){
            for(Map.Entry<Integer,String> map: ponitMap.entrySet()){
                mDataset.setValue(0,map.getValue(),str);
            }
        }
        //创造图标数据
        for(TaInstantPointData pointDateVo : taPointDataList){
            if(toolHelper.isNumeric(pointDateVo.getPointData())){
                mDataset.addValue(Double.valueOf(pointDateVo.getPointData()), ponitMap.get(pointDateVo.getPointId()), String.valueOf(pointDateVo.getCreateTimeInt()));
            }else{
                mDataset.addValue(0, ponitMap.get(pointDateVo.getPointId()), String.valueOf(pointDateVo.getCreateTimeInt()));
            }
        }
        return mDataset;
    }

    public DefaultCategoryDataset createUsageDeviceChartData(java.util.List<TaPoint> taPointList, List<TaUsagePointDataDate> taPointDataList,List<String> dateIntervalAllList){
        Map<Integer,String> ponitMap = new LinkedHashMap<>();
        for(TaPoint pointVo : taPointList){
            ponitMap.put(pointVo.getPointId(),"点名："+pointVo.getPointName()+"("+pointVo.getRemarksName()+")"+",单位："+pointVo.getPointUnit());
        }
        DefaultCategoryDataset dateSet = new DefaultCategoryDataset();
        for(String str : dateIntervalAllList){
            for(Map.Entry<Integer,String> map: ponitMap.entrySet()){
                dateSet.setValue(0,map.getValue(),str);
            }
        }
        //创造图标数据
        for(TaUsagePointDataDate pointDateVo : taPointDataList){
            dateSet.setValue(pointDateVo.getPointData(),ponitMap.get(pointDateVo.getPointId()),String.valueOf(pointDateVo.getDateShow()));
        }
        return dateSet;
    }

    // 根据CategoryDataset生成JFreeChart对象
    public JFreeChart createChart(TimeSeriesCollection lineDataset,String title,String xLabel,String yLabel) {
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(
                title, 		// 标题
                xLabel, 		// categoryAxisLabel （category轴，横轴，X轴的标签）
                yLabel, 	// valueAxisLabel（value轴，纵轴，Y轴的标签）
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
		xylineandshaperenderer.setBaseShapesVisible(true);
        // 设置曲线显示各数据点的值
		XYItemRenderer xyitem = plot.getRenderer();
		xyitem.setBaseItemLabelsVisible(true);
		xyitem.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
		xyitem.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		xyitem.setBaseItemLabelFont(new Font("Dialog", 1, 14));
		plot.setRenderer(xyitem);

        return jfreechart;
    }

    // 根据CategoryDataset生成JFreeChart对象
    public JFreeChart createUsageChart(DefaultCategoryDataset lineDataset,String title,String xLabel,String yLabel) {
        JFreeChart mChart = ChartFactory.createLineChart(
                title,//图名字
                xLabel,//横坐标
                yLabel,//纵坐标
                lineDataset,//数据集
                PlotOrientation.VERTICAL,
                true, // 显示图例
                true, // 采用标准生成器
                false);// 是否生成超链接

        CategoryPlot mPlot = (CategoryPlot)mChart.getPlot();
        mPlot.setBackgroundPaint(Color.LIGHT_GRAY);
        mPlot.setRangeGridlinePaint(Color.BLUE);//背景底部横虚线
        mPlot.setOutlinePaint(Color.RED);//边界线
        setCategoryPlot(mChart);
        CategoryPlot plot=mChart.getCategoryPlot();
// 获取渲染对象
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseItemLabelsVisible(true);
//renderer.setDrawShapes(true);
//renderer.setShapesFilled(true);
//设置数据显示位置
//ItemLabelPosition p = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER_LEFT,TextAnchor.CENTER_LEFT, -Math.PI / 2.0 );
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
//显示折点相应数据
        //StandardCategoryLabelGenerator st = new StandardCategoryLabelGenerator();
       // renderer.setBaseLabelGenerator(new StandardCategoryLabelGenerator());
        renderer.setBaseLinesVisible(true);
        renderer.setBaseShapesFilled(true);
        renderer.setBaseCreateEntities(true);
        return mChart;
    }


    // 保存为文件
    public static void saveAsFile(JFreeChart chart, String outputPath,
                                  int weight, int height) throws IOException {
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
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
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

    public JFreeChart createUageChart(DefaultCategoryDataset dateSet,String title,String xLabel,String yLabel) throws IOException {
        /*JFreeChart chart = ChartFactory.createBarChart("柱状图",//标题
                "用量",//目录轴的显示标签
                "kmh/",//数值的显示标签
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
        renderer.setMinimumBarLength(0.00); //设置柱子高度*/
        //创建图形实体对象
        JFreeChart chart=ChartFactory.createBarChart3D(//工厂模式
                title, //图形的标题
                xLabel, //目录轴的显示标签(X轴)
                yLabel,  //数据轴的显示标签(Y轴)
                dateSet, //数据集
                PlotOrientation.VERTICAL, //垂直显示图形
                true,  //是否生成图样
                false, //是否生成提示工具
                false);//是否生成URL链接
        setCategoryPlot(chart);
        CategoryPlot plot=chart.getCategoryPlot();
        BarRenderer3D renderer = new BarRenderer3D();//3D属性修改
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT));
        //下面可以进一步调整数值的位置，但是得根据ItemLabelAnchor选择情况，例
        // 如我选的是OUTSIDE12，那么下面设置为正数时，数值就会向上调整，负数则向下
        renderer.setItemLabelAnchorOffset(10);
        //最后还得将renderer 放到plot 中
        plot.setRenderer(renderer);//将修改后的属性值保存到图中
        return chart;
    }

    //设置图表的通用属性
    public void setCategoryPlot(JFreeChart chart){
        /*----------设置消除字体的锯齿渲染（解决中文问题）--------------*/
        chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        TextTitle textTitle = chart.getTitle();
        textTitle.setFont(new Font("宋体",Font.BOLD,20));
        CategoryPlot plot=chart.getCategoryPlot();//获取图形区域对象
        //设置网格竖线颜色
        plot.setDomainGridlinePaint(Color.black);
        //设置网格横线颜色
        plot.setRangeGridlinePaint(Color.black);
        //------------------------------------------获取X轴
        CategoryAxis domainAxis=plot.getDomainAxis();
        //设置X轴坐标上的文字
        domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 16));
//设置X轴的标题文字
        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 16));
        domainAxis.setTickLabelsVisible(true);//X轴的标题文字是否显示
        domainAxis.setAxisLinePaint(Color.red);//X轴横线颜色
        domainAxis.setTickMarksVisible(true);//标记线是否显示
        domainAxis.setTickMarkOutsideLength(3);//标记线向外长度
        domainAxis.setTickMarkInsideLength(3);//标记线向内长度
        domainAxis.setTickMarkPaint(Color.red);//标记线颜色
        domainAxis.setTickLabelsVisible(true);//刻度数值是否显示
    /*    domainAxis.setUpperMargin(0.2);//设置距离图片左端距离
        domainAxis.setLowerMargin(0.2); //设置距离图片右端距离*/
        //横轴上的 Lable 是否完整显示
        domainAxis.setMaximumCategoryLabelWidthRatio(0.6f);
         //横轴上的 Lable 45度倾斜
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
        //-----------------------------------------获取Y轴
        ValueAxis rAxis = plot.getRangeAxis();//对Y轴做操作
        //设置Y轴坐标上的文字
        rAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 16));
//设置Y轴的标题文字
        rAxis.setLabelFont(new Font("黑体", Font.PLAIN, 16));
        rAxis.setAutoRange(true);
        rAxis.setLabelAngle(1.6);//标题内容显示角度（1.6时候水平）
        rAxis.setTickMarkInsideLength(3);//外刻度线向内长度
        rAxis.setTickLabelsVisible(true);//刻度数值是否显示
// 所有Y标记线是否显示（如果前面设置rAxis.setMinorTickMarksVisible(true); 则其照样显示）
        rAxis.setTickMarksVisible(true);
        rAxis.setAxisLinePaint(Color.red);//Y轴竖线颜色
        rAxis.setAxisLineVisible(true);//Y轴竖线是否显示
/*//设置最高的一个 Item 与图片顶端的距离 (在设置rAxis.setRange(100, 600);情况下不起作用)
        rAxis.setUpperMargin(0.15);
//设置最低的一个 Item 与图片底端的距离
        rAxis.setLowerMargin(0.15);*/
        rAxis.setAutoRange(true);//是否自动适应范围
        rAxis.setVisible(true);//Y轴内容是否显示
        //设置图样的文字样式
        chart.getLegend().setItemFont(new Font("黑体",Font.BOLD ,15));
        //设置图形的标题
        chart.getTitle().setFont(new Font("宋体",Font.BOLD ,20));

    }

}
