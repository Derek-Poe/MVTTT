package com.unstoppapoenguyen.mvttt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.io.IOUtils;

import jakarta.servlet.*;             
import jakarta.servlet.http.*;        
import jakarta.servlet.annotation.*;

import java.sql.SQLException;

@WebServlet(name = "WebController", urlPatterns = {"/"})
public class WebController extends HttpServlet {
    public void init(){
        try {
            DataController.connectDatabase();
        }
        catch (SQLException|ClassNotFoundException|InstantiationException|IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        switch(req.getRequestURI()){
            case "/MVTTT":
            case "/MVTTT/":
                res.setContentType("text/html;charset=UTF-8");
                res.getWriter().print(Files.readString(Paths.get("MVTT_WD\\welcome.html")));
                break;
            case "/MVTTT/testPage":
                res.setContentType("text/html;charset=UTF-8");
                res.getWriter().print(Files.readString(Paths.get("MVTT_WD\\testPage.html")));
                break;
            default:
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        switch(req.getRequestURI()){
            case "/MVTTT/getMatch":
                Match match = null;
                try {
                    match = DataController.getMatch(Integer.parseInt(IOUtils.toString(req.getReader())));
                }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println(match.match_status);
                break;
            default:
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}