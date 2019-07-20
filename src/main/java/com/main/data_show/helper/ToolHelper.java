package com.main.data_show.helper;

import com.main.data_show.consts.ApplicationConsts;
import com.main.data_show.consts.SysConsts;
import com.main.data_show.enums.EnumUsageTimeTypeDefine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ToolHelper {
    //盐，用于混交md5
    private static final String slat = "&%5123***&&%%$$#@";

    @Autowired
    private Environment env;

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
    public static long dateToNumDate(Date date,String format){
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
                if(c<=defHour){
                    return getWeekNumByDate(checkDate);
                }else {
                    //加一天
                    Date addDate = addSubDate(checkDate, 1);
                    return getWeekNumByDate(addDate);
                }
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
    public long getWeekNumByDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置周一为一周的第一天
        cal.setTime(date);
        int weekNum = cal.get(Calendar.WEEK_OF_YEAR);
        int yearNum = cal.get(Calendar.YEAR);
        return Long.parseLong(yearNum+""+weekNum);
    }

    public static void main(String[] args) throws ParseException {
        Date date = makeStrToDate("2019-07-31 00:54:21", SysConsts.DATE_FORMAT_1);
        Date addDate = addSubDate(date, 1);
        System.out.println(dateToNumDate(addDate,SysConsts.DATE_FORMAT_5));
    }

}
