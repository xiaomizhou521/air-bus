package com.main.data_show.helper;

import com.main.data_show.consts.SysConsts;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        SimpleDateFormat df = new SimpleDateFormat(SysConsts.DATE_FORMAT);
        Date date = df.parse(dateStr+" "+hourStr);
        System.out.println("abc:"+dateStr+" "+hourStr);
        System.out.println(df.format(date));
        return date;
    }

    public static void main(String[] args) throws ParseException {
      //  System.out.println(makeDateByDateAndHour("2019/3/11","2:00:00\n"));
    }

}
