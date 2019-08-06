package com.main.data_show.helper;

import com.main.data_show.consts.ApplicationConsts;
import com.main.data_show.consts.SysConsts;
import com.main.data_show.enums.EnumUsageTimeTypeDefine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ToolHelper {
    //盐，用于混交md5
    private static final String slat = "&%5123***&&%%$$#@";

    @Autowired
    private Environment env;

    @Autowired
    private StartupHelper startupHelper;

    public boolean isEmpty(String words) {
        return words == null || words.trim().length() == 0;
    }

    /**
     * 生成md5
     * @param str
     * @return
     */
    public String getMD5(String str) {
        String base = str +"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    //7/2/2019  变   2019/7/2
    public static String dateStrFormatStr(String dateStr) throws Exception {
        String[] dateList = dateStr.split("/");
        if(dateList.length!=3){
           throw new Exception("日期字符串："+dateStr+",格式不正确");
       }
        StringBuffer sb = new StringBuffer();
        sb.append(dateList[2]).append("/");
        sb.append(dateList[0]).append("/");
        sb.append(dateList[1]);
        return sb.toString();
    }

    //日期字符串和小时字符串拼成时间
    public Date makeDateByDateAndHour(String dateStr,String hourStr) throws ParseException {
        Date date = makeStrToDate(dateStr+" "+hourStr,SysConsts.DATE_FORMAT);
        return date;
    }

    //日期字符串转化为date
    public static Date makeStrToDate(String dateStr,String date_formate) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(date_formate);
        Date date = df.parse(dateStr);
        return date;
    }
    //日期 加 减
    public static  Date addSubDate(Date date, int index){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DAY_OF_MONTH, index);
        Date dt1 = rightNow.getTime();
        return dt1;
    }

    //小时 加 减
    public static Date addSubHour(Date date, int index){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.HOUR_OF_DAY, index);
        Date dt1 = rightNow.getTime();
        return dt1;
    }

    /**
     * 字符串转换成日期
     * @param str
     * @return date
     */
    public Date StrToDate(String str,String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /***
      * 判断字符串是否为数字（包含小数点）
      * @param str
      * @return
      */
    public boolean isNumeric(String str){
         Pattern pattern = Pattern.compile("[0-9]+.*[0-9]*");
         Matcher isNum = pattern.matcher(str);
         if( !isNum.matches() ){
             return false;
         }
         return true;
    }

    //小时  如果是1位 前面补0
    //time 格式  9:00:00  或者  21:00:00
    public String hourCover(String time) throws Exception {
         if(isEmpty(time)){
             throw new Exception("补位时间不能为空");
         }
         if(time.length() == 7){
             time = "0"+time;
         }
         return time;
    }

    //double 数字四舍五入保留 2 位 返回字符产
    public String  roundHafUp (double f) {
        BigDecimal b = new BigDecimal(f);
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return String.valueOf(f1);
    }

    //取得某年某周的 周一 0点的时间
    public  Date getWeekMonday(int year,int week){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year); // 2016年
        cal.set(Calendar.WEEK_OF_YEAR, week); // 设置为2016年的第10周
        cal.set(Calendar.DAY_OF_WEEK, 1); // 1表示周日，2表示周一，7表示周六
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        Date date = cal.getTime();
        return date;
    }
    //日期转成数字 例如  20190706010000
    public long dateToNumDate(Date date,String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        String c=sdf.format(date);
        long numDate = Long.parseLong(c);
        return numDate;
    }

     //double减法
    public double doubleSubtract(String v1,String v2){
     BigDecimal b1 = new BigDecimal(v1);
     BigDecimal b2 = new BigDecimal(v2);
     return b1.subtract(b2).doubleValue();
    }

    //double加法
    public  double doubleSum(double d1, double d2) {
     BigDecimal bd1 = new BigDecimal(Double.toString(d1));
     BigDecimal bd2 = new BigDecimal(Double.toString(d2));
     return bd1.add(bd2).doubleValue();
   }

    //判断某个时间属于哪一天
    // 根据配置的时间点来决定  数据库存的时间应该就是 年月日
    //返回年月日的数字
    // type ：  date,week,mon三种
    public long  getUsageDateWeekMonNum(Date checkDate,String type) throws Exception {
         //判断规则为 取得checkDate的小时数和配置的小时说比较
         //如果 checkDate的小时数<=配置的小时   即为今天
         //如果 checkDate的小时数>配置的小时   即为明天
        //取得配置的小时数
        int defHour = Integer.parseInt(env.getProperty(ApplicationConsts.SYS_POINT_USAGE_RECORD_DEF_HOUR));
        //如果 配置的是 0 点 那么日期就算今天的日期 不需要处理
        if(defHour == 0){
            if(EnumUsageTimeTypeDefine.date.toString().equals(type)){
                return dateToNumDate(checkDate,SysConsts.DATE_FORMAT_4);
            }else if(EnumUsageTimeTypeDefine.week.toString().equals(type)){
                return getWeekNumByDate(checkDate);
            }else if(EnumUsageTimeTypeDefine.mon.toString().equals(type)){
                return dateToNumDate(checkDate,SysConsts.DATE_FORMAT_5);
            }
        }else if(defHour>0){
            SimpleDateFormat sdf=new SimpleDateFormat("HH");
            int c=Integer.parseInt(sdf.format(checkDate));
            if(EnumUsageTimeTypeDefine.date.toString().equals(type)){
                if(c<=defHour){
                    return dateToNumDate(checkDate,SysConsts.DATE_FORMAT_4);
                }else {
                    //加一天
                    Date addDate = addSubDate(checkDate, 1);
                    return dateToNumDate(addDate,SysConsts.DATE_FORMAT_4);
                }
            }else if(EnumUsageTimeTypeDefine.week.toString().equals(type)){
               /* if(c<=defHour){*/
                //周事按配置的某个星期几为间隔 当前时间减去今年的第一个周几来计算周数的 应该不用加一天
                    return getWeekNumByDate(checkDate);
              /*  }else {
                    //加一天
                    Date addDate = addSubDate(checkDate, 1);
                    return getWeekNumByDate(addDate);
                }*/
            }else if(EnumUsageTimeTypeDefine.mon.toString().equals(type)){
                if(c<=defHour){
                    return dateToNumDate(checkDate,SysConsts.DATE_FORMAT_5);
                }else {
                    //加一天
                    Date addDate = addSubDate(checkDate, 1);
                    return dateToNumDate(addDate,SysConsts.DATE_FORMAT_5);
                }
            }
        }else{
            throw new Exception("时间间隔不能是负数！！！");
        }
        return 0;
    }
    //取周数
    public long getWeekNumByDate(Date date) throws Exception {
        //先判断传进来的日期的年  与  参数日期的年是不是相同 不相同 要拿着新的年 重算
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int checkYear = cal.get(Calendar.YEAR);
        return Long.parseLong(dateToWeekLong(date,checkYear));
    }

    //通过日期取周数
    public String dateToWeekLong(Date date,int checkYear) throws Exception {
        if(checkYear < 0){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            checkYear = cal.get(Calendar.YEAR);
        }
        //SysConsts.DEF_YEAE 默认是-1 没有初始化过 或者更当前验证日期的年不一致 要拿新的年重算一下
        //因为周数 每年的开始那周的日期都要计算
        if(SysConsts.DEF_YEAE < 0||SysConsts.DEF_YEAE!=checkYear){
            startupHelper.initYearFirstWeekDate(checkYear);
        }
        //计算一次之后 肯定相等了 如果还不想等 那肯定就是有问题了  抛异常
        if(SysConsts.DEF_YEAE!=checkYear){
            throw new Exception("当前系统内存中的用来计算的年份异常");
        }
        //计算周数规则  小于SysConsts.YEAR_FIRT_WEEK_STRART_DATE 的为第一周
        //(验证时间 - SysConsts.YEAR_FIRT_WEEK_STRART_DATE)/7  + 1 为真正的周数
        long betweenDate = (date.getTime() - SysConsts.YEAR_FIRT_WEEK_STRART_DATE.getTime())/(60*60*24*1000);
        long weekNum = (betweenDate/7)+1;
        String weekStr = weekNum+"";
        if(weekStr.length()<2){
            weekStr = "0"+weekStr;
        }
        return checkYear+""+weekStr;
    }

    //取文件的相对路径
    public String getPointRelativePath(String allPath,String basePath){
        allPath = allPath.replace(basePath,"");
        return allPath;
    }

    //取文件的前缀
    public String getPointRFilenNamePrefix(String fileName){
        String prefix = fileName.substring(0, fileName.length() - 12);
        return prefix;
    }

    //日期转成字符串 例如  20190706010000
    public String dateToStrDate(Date date,String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        String c=sdf.format(date);
        return c;
    }

    /**
     * 中文文件名支持
     *
     * @param request
     * @param response
     * @param fileName
     * @throws UnsupportedEncodingException
     */
    public void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        String encodedfileName = null;
        String agent = request.getHeader("USER-AGENT");
        if (null != agent && -1 != agent.indexOf("MSIE")) {
            encodedfileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else if (null != agent && -1 != agent.indexOf("Mozilla")) {
            encodedfileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        } else {
            encodedfileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
    }

    //比较两个字符串时间的大小 true 正确 false 错误
    public boolean compareStrDate(String startDateStr,String endDateStr,String format){
        Date startDate = StrToDate(startDateStr, format);
        Date endDate = StrToDate(endDateStr, format);
        return compareDate(startDate,endDate);
    }

    //比较两个日期的大小 startDate <= endDate  true  startDate > endDate false 错误
    public boolean compareDate(Date startDate,Date endDate){
        if(startDate.before(endDate)||(startDate.getTime()==endDate.getTime())){
            return true;
        }else{
            return false;
        }
    }

    //取某个日期间隔的所有时间 可以按天  按周  按月
    public List<String> getDateIntervalAllList(String startTime,String endTime){
        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        try {
            DateFormat dateFormat = new SimpleDateFormat(SysConsts.DATE_FORMAT_7);
            DateFormat resultDateFormat = new SimpleDateFormat(SysConsts.DATE_FORMAT_4);
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(resultDateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    //取某个日期间隔的所有月份 可以按天  按周  按月
    public List<String> getMonIntervalAllList(String startTime,String endTime){
        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        try {
            DateFormat dateFormat = new SimpleDateFormat(SysConsts.DATE_FORMAT_8);
            DateFormat resultDateFormat = new SimpleDateFormat(SysConsts.DATE_FORMAT_5);
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.MONTH, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(resultDateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }
    //取某个日期间隔的所有月份 可以按天  按周  按月
    public List<String> getHourIntervalAllList(String startTime,String endTime,String jinFormat,String chuFormat){
        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        try {
            DateFormat dateFormat = new SimpleDateFormat(jinFormat);
            DateFormat resultDateFormat = new SimpleDateFormat(chuFormat);
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.HOUR_OF_DAY, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(resultDateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.HOUR_OF_DAY, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    //取某个某个时间段里面的 每天的同一个时间点
    public List<Date> getDateHourIntervalAllList(String startExpDate, String endExpDate, String takeTime){
        // 返回的日期集合
        List<Date> days = new ArrayList<Date>();
        try {
            Date startExp = makeStrToDate(startExpDate+" "+takeTime, SysConsts.DATE_FORMAT_1);
            //开始时间应该 时选择的时间前一天开始统计  选择日期的用量
            Date start = addSubDate(startExp, -1);
            Date end = makeStrToDate(endExpDate+" "+takeTime, SysConsts.DATE_FORMAT_1);

            DateFormat resultDateFormat = new SimpleDateFormat(SysConsts.DATE_FORMAT_3);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DAY_OF_MONTH, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(tempStart.getTime());
                tempStart.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public Date weekStrToDate(String weekStr){
        //星期几
        int defWeek = Integer.parseInt(env.getProperty(ApplicationConsts.SYS_POINT_USAGE_RECORD_DEF_WEEK_NUM));
        String year = weekStr.substring(0, 4);
        String week = weekStr.substring(4);
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.YEAR, Integer.valueOf(year));
        cal.set(Calendar.WEEK_OF_YEAR, Integer.valueOf(week));
        cal.set(Calendar.DAY_OF_WEEK, defWeek);
        Date time = cal.getTime();
        return time;
    }

    //取某个日期间隔的所有周数 可以按天  按周  按月
    public List<String> getWeekIntervalAllList(String startTime,String endTime){
        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        try {
            Date start = weekStrToDate(startTime);
            Date end = weekStrToDate(endTime);
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);
           // tempStart.add(Calendar.WEEK_OF_YEAR, 1);// 日期加1(包含结束)
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.WEEK_OF_YEAR, 2);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateToWeekLong(tempStart.getTime(),-1));
                tempStart.add(Calendar.WEEK_OF_YEAR, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }
}
