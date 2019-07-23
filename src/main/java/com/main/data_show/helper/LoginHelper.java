package com.main.data_show.helper;

import cn.com.enorth.utility.Beans;
import com.main.data_show.DataShowApplication;
import com.main.data_show.bean.LoginUserVo;
import com.main.data_show.consts.LoginConst;
import com.main.data_show.pojo.TaUser;
import com.main.data_show.service.TaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;

@Service
public class LoginHelper {
    private static Logger logger = LoggerFactory.getLogger(LoginHelper.class);

    @Autowired
    private TaUserService taUserService;
    /*
     * 取得当前登陆的userId
     *
     * @author ljg
     *
     *
     */
    public int getCurUserId(HttpServletRequest request) throws Exception {
        //返回-1 表示登录已经失效了
        int userId = -1;
        Object attribute = request.getSession().getAttribute(LoginConst.CURRENT_LOGIN_USER);
        if(attribute != null) {
            LoginUserVo userVo = (LoginUserVo)attribute;
            userId = userVo.getUserId();
        }
        /*else {
            //session 失效后去取cookie 中的refreshtoken 然后去用户中心验证登录信息
            String refreshToken = getRefreshToken(request);
            if(Beans.strUtil.isNotEmpty(refreshToken)) {
                LoginUserVo userInfo = this.getUserInfoByToken(request);
                userId = userInfo.getUserId();
            }
        }*/
        return userId;
    }

    /*
     * 登录信息存入session
     *
     * @author ljg
     *
     */
    public LoginUserVo saveLoginInfoToSession(HttpServletRequest request,TaUser userVo) throws SQLException, Exception {
        //用户信息存入session中
        LoginUserVo users=new LoginUserVo();
        users.setUserId(userVo.getUserId());
        users.setUserName(userVo.getUserName());
        users.setRealName(userVo.getNickName());
        request.getSession().setAttribute(LoginConst.CURRENT_LOGIN_USER, users);
        return users;
    }

    /**
     * 获得当前登录对象的userVo
     *
     * @param request
     * @return
     * @throws Exception
     */
    public TaUser getCurUser(HttpServletRequest request) throws Exception {
        int userId = getCurUserId(request);
        if(userId == -1) {
            return null;
        }
        TaUser userByUserId = taUserService.findUserByUserId(userId);
        return userByUserId;
    }

    /*
     * cookie保存操作
     * @author ljg
     *
     */
    public void saveCookie(HttpServletResponse response, String cookieName, String cookeiValue, int keepTimes)
            throws UnsupportedEncodingException {
        String cookieNameBase64 = URLEncoder.encode(cookieName, Beans.UTF_8);
        System.out.println(cookieName);
        System.out.println(cookeiValue);
        Cookie cookie = new Cookie(cookieNameBase64, cookeiValue);
        cookie.setPath("/");
        cookie.setMaxAge(keepTimes);
        response.addCookie(cookie);
        logger.info("保存cookie成功，cookieName:" + cookieName + " ;cookeiValue:" + cookeiValue + ";keepTimes:" + keepTimes);
    }

    /*
     * cookie清除操作
     * @author ljg
     *
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) throws UnsupportedEncodingException {
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookieName.equals(cookies[i].getName())) {
                    Cookie cookie = new Cookie(cookies[i].getName(), null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
        logger.info("清除cookie key："+cookieName+"成功");
    }
    /*
     * cookie取得操作
     * @author ljg
     *
     */
    public String getRefreshToken(HttpServletRequest request) throws Exception {
        String cookie = getCookie(request, getCookieRefreshTokenKey());
        return cookie;
    }

    private String getCookieRefreshTokenKey() throws Exception {
      /*  String appId=CCB.paramHelper().getString(ParamConsts.SYS_APP_ID);
        StringBuffer sb = new StringBuffer();
        sb.append(UcenterConst.REFRESH_TOKEN_KEY).append("_").append(appId);*/
        return "";
    }

    /*
     * cookie取得操作
     * @author ljg
     *
     */
    public String getCookie(HttpServletRequest request, String cookieName) throws UnsupportedEncodingException {
        Cookie cookies[] = request.getCookies();
        String cookiesValue = "";
        // base64转string
        if (Beans.strUtil.isNotEmpty(cookiesValue)) {
            cookieName = URLDecoder.decode(cookieName, Beans.UTF_8);
        }
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                cookiesValue = cookie.getValue();
            }
        }
        return cookiesValue;
    }
    public void logOut(HttpServletRequest request){
        request.getSession().setAttribute(LoginConst.CURRENT_LOGIN_USER, null);
    }
}
