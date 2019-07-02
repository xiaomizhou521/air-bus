package com.main.data_show.helper;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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
    public static String getMD5(String str) {
        String base = str +"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
