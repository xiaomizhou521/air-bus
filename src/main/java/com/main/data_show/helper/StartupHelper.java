package com.main.data_show.helper;

import com.main.data_show.consts.ApplicationConsts;
import com.main.data_show.consts.SysConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StartupHelper {

    @Autowired
    private Environment env;

    //取今年的第一个周间隔的日期
    public void initYearFirstWeekDate(int year){
       /* //取得配置的小时数
        int defHour = Integer.parseInt(env.getProperty(ApplicationConsts.SYS_POINT_USAGE_RECORD_DEF_HOUR));
        //星期几
        int defWeek = Integer.parseInt(env.getProperty(ApplicationConsts.SYS_POINT_USAGE_RECORD_DEF_WEEK_NUM));*/
        //取得配置的小时数
        int defHour = 16;
        //星期几
        int defWeek = 5;
        Calendar calendar = new GregorianCalendar();//定义一个日历，变量作为年初
        Calendar calendarEnd = new GregorianCalendar();//定义一个日历，变量作为年末
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);//设置年初的日期为1月1日
        calendarEnd.set(Calendar.YEAR, year);
        calendarEnd.set(Calendar.MONTH, 11);
        calendarEnd.set(Calendar.DAY_OF_MONTH, 31);//设置年末的日期为12月31日
        SimpleDateFormat sf = new SimpleDateFormat(SysConsts.DATE_FORMAT_3);
        while(calendar.getTime().getTime()<=calendarEnd.getTime().getTime()){//用一整年的日期循环
            if(calendar.get(Calendar.DAY_OF_WEEK)==defWeek){//判断如果为星期四时，打印
                calendar.set(Calendar.HOUR_OF_DAY,defHour);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                System.out.println(sf.format(calendar.getTime()));
                SysConsts.YEAR_FIRT_WEEK_STRART_DATE = calendar.getTime();
                SysConsts.DEF_YEAE = year;
                break;
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);//日期+1
        }
        //取当前年 中属于 下一年第一周的某几天的日期 放入内存中
        if(!SysConsts.NEXT_YEAR_FIRST_WEEK_DATE_MAP.containsKey(year)){
            //取下一年的第一个系统定义的周几 是几号
            Calendar calendar1 = new GregorianCalendar();//定义一个日历，变量作为年初
            Calendar calendarEnd1 = new GregorianCalendar();//定义一个日历，变量作为年末
            calendar1.set(Calendar.YEAR, year);
            calendar1.set(Calendar.MONTH, 0);
            calendar1.set(Calendar.DAY_OF_MONTH, 1);//设置年初的日期为1月1日
            calendarEnd1.set(Calendar.YEAR, year);
            calendarEnd1.set(Calendar.MONTH, 11);
            calendarEnd1.set(Calendar.DAY_OF_MONTH, 31);//设置年末的日期为12月31日
            while(calendar1.getTime().getTime()<=calendarEnd1.getTime().getTime()) {//用一整年的日期循环
                if(calendarEnd1.get(Calendar.DAY_OF_WEEK)==defWeek){
                    calendarEnd1.set(Calendar.HOUR_OF_DAY,defHour);
                    calendarEnd1.set(Calendar.MINUTE,0);
                    calendarEnd1.set(Calendar.SECOND,0);
                    calendarEnd1.set(Calendar.MILLISECOND,0);
                    SysConsts.NEXT_YEAR_FIRST_WEEK_DATE_MAP.put(year,calendarEnd1.getTime());
                    break;
                }
                calendarEnd1.add(Calendar.DAY_OF_MONTH, -1);//日期+1
            }
        }
    }

}
