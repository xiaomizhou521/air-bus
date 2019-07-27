package com.main.data_show.consts;

import java.util.Date;

public class SysConsts {

    public static int DEF_SYS_USER_ID = -1;

    public static String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public static String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    public static String DATE_FORMAT_2 = "yy-MM-dd HH";

    public static String DATE_FORMAT_3 = "yyyyMMddHHmmss";

    public static String DATE_FORMAT_4 = "yyyyMMdd";

    public static String DATE_FORMAT_5 = "yyyyMM";

    public static String DATE_FORMAT_6 = "MM-dd-yy";

    public static String DATE_FORMAT_7 = "yyyy-MM-dd";

    public static String DATE_FORMAT_8 = "yyyy-MM";

    public static String DATE_FORMAT_9 = "yyyy/MM/dd";

    public static String DATE_FORMAT_10 = "HH:mm:ss";

//路径中包含这个的是 用量的点
    public static String POINT_USAGE_DEF_FILE_PATH = "REPORTS/POWER/KWH";

    //按照配置的 周间隔从周几开始的系统参数  先计算出一年中第一个这个周几的日期
    //为了计算周数用 每年都应该重新算一个
    public static Date YEAR_FIRT_WEEK_STRART_DATE = null;
    //当前计算周数 所用的年  为了每次比较用  放入内存  不用每次都算
    public static int DEF_YEAE = -1;
}
