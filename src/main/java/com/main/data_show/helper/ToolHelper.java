package com.main.data_show.helper;

import com.main.data_show.consts.SysConsts;
import org.apache.catalina.startup.Catalina;
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

    public boolean isEmpty(String words) {
        return words == null || words.trim().length() == 0;
    }

    /**
     * 生成md5
     * @param seckillId
     * @return
     */
    public String getMD5(String str) {
        String base = str +"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    //日期字符串和小时字符串拼成时间
    public Date makeDateByDateAndHour(String dateStr,String hourStr) throws ParseException {
        Date date = makeStrToDate(dateStr+" "+hourStr,SysConsts.DATE_FORMAT);
        return date;
    }
    //日期字符串转化为date
    public Date makeStrToDate(String dateStr,String date_formate) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(date_formate);
        Date date = df.parse(dateStr);
        return date;
    }
    //日期 加 减
    public Date addSubDate(Date date, int index,String date_formate){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DAY_OF_MONTH, index);
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

    public static void main(String[] args) throws ParseException {
        System.out.println(getWeekMonday(2019,30));
    }

    //取得某年某周的 周一 0点的时间
    public static Date getWeekMonday(int year,int week){
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

}
