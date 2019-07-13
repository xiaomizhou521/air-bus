package com.main.data_show.helper;

import com.main.data_show.consts.SysConsts;
import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaPointData;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JFreeChartHelper {
    @Resource
    private ToolHelper toolHelper;

    public static void main1(String[] args) {
        // 步骤1：创建CategoryDataset对象（准备数据）
        TimeSeriesCollection dataset = createDataset();
        // 步骤2：根据Dataset 生成JFreeChart对象，以及做相应的设置
        JFreeChart freeChart = createChart(dataset);
        // 步骤3：将JFreeChart对象输出到文件，Servlet输出流等
        saveAsFile(freeChart, "C:\\Users\\Administrator\\Desktop\\call\\testline.png", 500, 300);
    }

    //生成设备图表开始
    public void createDeviceChartStart(java.util.List<TaPoint> taPointList, List<TaPointData> taPointDataList){
         // 步骤1：创建CategoryDataset对象（准备数据）
        TimeSeriesCollection dataset = createDeviceChartDate(taPointList,taPointDataList);
        // 步骤2：根据Dataset 生成JFreeChart对象，以及做相应的设置
        JFreeChart freeChart = createChart(dataset);
        // 步骤3：将JFreeChart对象输出到文件，Servlet输出流等
        saveAsFile(freeChart, "C:\\01work\\priv_work190415\\CSV_test\\testline.png", 1000, 500);
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

    // 创建TimeSeriesCollection对象
    public static TimeSeriesCollection createDataset() {
        TimeSeriesCollection lineDataset = new TimeSeriesCollection();
        TimeSeries timeSeries = new TimeSeries("统计",Month.class);
        timeSeries.add(new Month(1, 2007), 11200);
        timeSeries.add(new Month(2, 2007), 9000);
        timeSeries.add(new Month(3, 2007), 6200);
        timeSeries.add(new Month(4, 2007), 8200);
        timeSeries.add(new Month(5, 2007), 8200);
        timeSeries.add(new Month(6, 2007), 12200);
        timeSeries.add(new Month(7, 2007), 13200);
        timeSeries.add(new Month(8, 2007), 8300);
        timeSeries.add(new Month(9, 2007), 12400);
        timeSeries.add(new Month(10, 2007), 12500);
        timeSeries.add(new Month(11, 2007), 13600);
        timeSeries.add(new Month(12, 2007), 12500);

        TimeSeries timeSeries1 = new TimeSeries("统计1",Month.class);
        timeSeries1.add(new Month(1, 2007), 112001);
        timeSeries1.add(new Month(2, 2007), 90001);
        timeSeries1.add(new Month(3, 2007), 62001);
        timeSeries1.add(new Month(4, 2007), 82001);
        timeSeries1.add(new Month(5, 2007), 82001);
        timeSeries1.add(new Month(6, 2007), 122001);
        timeSeries1.add(new Month(7, 2007), 132001);
        timeSeries1.add(new Month(8, 2007), 83001);
        timeSeries1.add(new Month(9, 2007), 124001);
        timeSeries1.add(new Month(10, 2007), 125001);
        timeSeries1.add(new Month(11, 2007), 136001);
        timeSeries1.add(new Month(12, 2007), 125001);

        lineDataset.addSeries(timeSeries);
        lineDataset.addSeries(timeSeries1);
        return lineDataset;
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

    // 本地测试
    public static void main(String[] args) {
        // 步骤1：创建CategoryDataset对象（准备数据）
        TimeSeriesCollection dataset = createDataset();
//		// 步骤2：根据Dataset 生成JFreeChart对象，以及做相应的设置
//		JFreeChart freeChart = createChart(dataset);
        ChartFrame cf = new ChartFrame("Test", createChart(dataset));
        cf.pack();
        cf.setVisible(true);
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


}
