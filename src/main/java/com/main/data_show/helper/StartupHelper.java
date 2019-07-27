package com.main.data_show.helper;

import com.main.data_show.consts.ApplicationConsts;
import com.main.data_show.consts.SysConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class StartupHelper {

    @Autowired
    private Environment env;

    //取今年的第一个周间隔的日期
    public void initYearFirstWeekDate(int year){
        //取得配置的小时数
        int defHour = Integer.parseInt(env.getProperty(ApplicationConsts.SYS_POINT_USAGE_RECORD_DEF_HOUR));
        //星期几
        int defWeek = Integer.parseInt(env.getProperty(ApplicationConsts.SYS_POINT_USAGE_RECORD_DEF_WEEK_NUM));
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
                System.out.println(sf.format(calendar.getTime()));
                SysConsts.YEAR_FIRT_WEEK_STRART_DATE = calendar.getTime();
                SysConsts.DEF_YEAE = year;
                break;
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);//日期+1
        }
    }

}
