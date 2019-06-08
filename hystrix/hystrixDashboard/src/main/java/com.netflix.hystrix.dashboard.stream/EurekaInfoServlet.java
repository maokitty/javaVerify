//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.netflix.hystrix.dashboard.stream;


import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EurekaInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public EurekaInfoServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getParameter("url");
        if(uri == null || "".equals(uri)) {
            response.getOutputStream().write("Error. You need supply a valid eureka URL ".getBytes());
        }

        try {
            response.setContentType("application/xml");
            response.setHeader("Content-Encoding", "gzip");
            IOUtils.copy(UrlUtils.readXmlInputStream(uri), response.getOutputStream());
        } catch (Exception var5) {
            response.getOutputStream().write(("Error. You need supply a valid eureka URL. Ex: " + var5 + "").getBytes());
        }

    }
}
