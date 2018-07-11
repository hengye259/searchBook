package org.hengsir.searchBook.controller;

import net.sf.json.JSONObject;
import org.hengsir.searchBook.entity.Book;
import org.hengsir.searchBook.service.BookSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author 周宇恒
 * @version v1.0 创建时间：2017年11月4日 上午10:21:38 类说明:
 */

@Controller
public class BookSearchController {

    @Autowired
    private BookSearchService bss;

    @RequestMapping("/")
    public String index() {
        return "/index";
    }

    /**
     * 这是从index.jsp中点击搜索请求的controller，这里使用反转ajax技术.
     * 这里是通过等待页面的等待，后台处理完数据再使用dwr让客户端做响应
     *
     * @param bookName
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject searchFromIndex(String bookName, HttpSession session, HttpServletResponse resp) {
        Map<String, List<Book>> bookMap = bss.search(bookName);
        session.setAttribute("bm",bookMap);
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("result",bookMap != null);
        return jsonObject;
    }

    @RequestMapping(value = "/searchAjax", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, List<Book>> searchFromAjax(String bookName,HttpSession session) {
        Map<String, List<Book>> bookMap = bss.search(bookName);
        session.setAttribute("bm",bookMap);
        return bookMap;
    }

    @RequestMapping("/show")
    public ModelAndView show(HttpSession session){
        Map<String, List<Book>> bookMap = (Map<String, List<Book>>) session.getAttribute("bm");
        ModelAndView modelAndView = new ModelAndView();
        if (bookMap != null) {
            modelAndView.setViewName("/show");
            modelAndView.addObject("bookMap", bookMap);
        } else {
            modelAndView.setViewName("/index");
        }
        return modelAndView;
    }

    @RequestMapping("/loadImg")
    public void loadImg(String url, HttpServletResponse resp) {
        resp.setContentType("image/jpg;charset=utf-8");
        try {
            OutputStream out = resp.getOutputStream();
            URL u = new URL(url);
            System.out.println("url:==========" + url);
            URLConnection conn = u.openConnection();
            InputStream in = conn.getInputStream();
            if (in != null) {
                int len;
                byte[] b = new byte[1024];
                while ((len = in.read(b)) != -1) {
                    out.write(b, 0, len);
                }
                out.flush();
                out.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
