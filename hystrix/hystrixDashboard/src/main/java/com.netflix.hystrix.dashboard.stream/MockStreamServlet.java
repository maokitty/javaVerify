//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.netflix.hystrix.dashboard.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;

public class MockStreamServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(MockStreamServlet.class);

    public MockStreamServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filename = request.getParameter("file");
        if(filename == null) {
            filename = "hystrix.stream";
        } else {
            filename = filename.replaceAll("\\.\\.", "");
            filename = filename.replaceAll("/", "");
        }

        int delay = 500;
        String delayArg = request.getParameter("delay");
        if(delayArg != null) {
            delay = Integer.parseInt(delayArg);
        }

        int batch = 1;
        String batchArg = request.getParameter("batch");
        if(batchArg != null) {
            batch = Integer.parseInt(batchArg);
        }

        String data = this.getFileFromPackage(filename);
        String[] lines = data.split("\n");
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        int batchCount = 0;

        while(true) {
            String[] var11 = lines;
            int var12 = lines.length;

            for(int var13 = 0; var13 < var12; ++var13) {
                String s = var11[var13];
                s = s.trim();
                if(s.length() > 0) {
                    try {
                        response.getWriter().println(s);
                        response.getWriter().println("");
                        response.getWriter().flush();
                        ++batchCount;
                    } catch (Exception var17) {
                        logger.warn("Exception writing mock data to output.", var17);
                        return;
                    }

                    if(batchCount == batch) {
                        try {
                            Thread.sleep((long) delay);
                        } catch (InterruptedException var16) {
                            ;
                        }

                        batchCount = 0;
                    }
                }
            }
        }
    }

    private String getFileFromPackage(String filename) {
        try {
            String e = "/" + this.getClass().getPackage().getName().replace('.', '/') + "/" + filename;
            InputStream is = this.getClass().getResourceAsStream(e);

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                StringWriter s = new StringWriter();
                boolean c = true;

                int c1;
                while((c1 = in.read()) > -1) {
                    s.write(c1);
                }

                String var7 = s.toString();
                return var7;
            } finally {
                is.close();
            }
        } catch (Exception var12) {
            throw new RuntimeException("Could not find file: " + filename, var12);
        }
    }
}
