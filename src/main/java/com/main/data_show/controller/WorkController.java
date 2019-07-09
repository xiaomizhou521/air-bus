package com.main.data_show.controller;

import com.github.pagehelper.PageInfo;
import com.main.data_show.consts.JspPageConst;
import com.main.data_show.helper.ToolHelper;
import com.main.data_show.pojo.TaPoint;
import com.main.data_show.service.TaPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class WorkController {

    @Autowired
    private TaPointService taPointService;

    @Autowired
    private ToolHelper toolHelper;

    @RequestMapping(value = "work/toPointList")
    public String toPointList(HttpServletRequest request) {
        int pageNo = 1;
        int pageSize = 3;
        String curPageNo = request.getParameter("pageNo");
        if(!toolHelper.isEmpty(curPageNo)){
            pageNo = Integer.parseInt(curPageNo);
        }
        //取所有点的列表
        List<TaPoint> pointsByPage = taPointService.getPointsByPage(pageNo, pageSize);
        PageInfo<TaPoint> pageInfo=new PageInfo<>(pointsByPage);
        request.setAttribute("pointList", pointsByPage);
        request.setAttribute("totalPage", pageInfo.getTotal());
        request.setAttribute("lastPage", pageInfo.getLastPage());
        request.setAttribute("pageNum", pageInfo.getPageNum());
        request.setAttribute("pageSize", pageInfo.getPageSize());
        request.setAttribute("firstPage", pageInfo.getFirstPage());
        request.setAttribute("nextPage", pageInfo.getNextPage());

        return JspPageConst.TO_POINT_LIST_JSP_REDIRECT;
    }
}
