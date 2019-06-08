//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.netflix.hystrix.dashboard.stream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

public class ProxyStreamServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ProxyStreamServlet.class);

    public ProxyStreamServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String origin = request.getParameter("origin");
        String authorization = request.getParameter("authorization");
        if(origin == null) {
            response.setStatus(500);
            response.getWriter().println("Required parameter \'origin\' missing. Example: 107.20.175.135:7001");
        } else {
            origin = origin.trim();
            HttpGet httpget = null;
            InputStream is = null;
            boolean hasFirstParameter = false;
            StringBuilder url = new StringBuilder();
            if(!origin.startsWith("http")) {
                url.append("http://");
            }

            url.append(origin);
            if(origin.contains("?")) {
                hasFirstParameter = true;
            }

            Map params = request.getParameterMap();
            Iterator proxyUrl = params.keySet().iterator();

            while(proxyUrl.hasNext()) {
                String e = (String)proxyUrl.next();
                if(!e.equals("origin") && !e.equals("authorization")) {
                    String[] httpResponse = (String[])params.get(e);
                    String statusCode = httpResponse[0].trim();
                    if(hasFirstParameter) {
                        url.append("&");
                    } else {
                        url.append("?");
                        hasFirstParameter = true;
                    }

                    url.append(e).append("=").append(statusCode);
                }
            }

            String var34 = url.toString();
            logger.info("\n\nProxy opening connection to: {}\n\n", var34);

            try {
                httpget = new HttpGet(var34);
                if(authorization != null) {
                    httpget.addHeader("Authorization", authorization);
                }

                HttpClient var35 = ProxyStreamServlet.ProxyConnectionManager.httpClient;
                HttpResponse var36 = var35.execute(httpget);
                int var37 = var36.getStatusLine().getStatusCode();
                if(var37 == 200) {
                    is = var36.getEntity().getContent();
                    Header[] os = var36.getAllHeaders();
                    int b = os.length;

                    for(int e1 = 0; e1 < b; ++e1) {
                        Header header = os[e1];
                        if(!"Transfer-Encoding".equals(header.getName())) {
                            response.addHeader(header.getName(), header.getValue());
                        }
                    }

                    ServletOutputStream var38 = response.getOutputStream();
                    boolean var39 = true;

                    while((b = is.read()) != -1) {
                        try {
                            var38.write(b);
                            if(b == 10) {
                                var38.flush();
                            }
                        } catch (Exception var31) {
                            if(var31.getClass().getSimpleName().equalsIgnoreCase("ClientAbortException")) {
                                logger.debug("Connection closed by client. Will stop proxying ...");
                                break;
                            }

                            throw new RuntimeException(var31);
                        }
                    }
                }
            } catch (Exception var32) {
                logger.error("Error proxying request: " + url, var32);
            } finally {
                if(httpget != null) {
                    try {
                        httpget.abort();
                    } catch (Exception var30) {
                        logger.error("failed aborting proxy connection.", var30);
                    }
                }

                if(is != null) {
                    try {
                        is.close();
                    } catch (Exception var29) {
                        ;
                    }
                }

            }

        }
    }

    private static class ProxyConnectionManager {
        private static final PoolingClientConnectionManager threadSafeConnectionManager = new PoolingClientConnectionManager();
        private static final HttpClient httpClient;

        private ProxyConnectionManager() {
        }

        static {
            httpClient = new DefaultHttpClient(threadSafeConnectionManager);
            ProxyStreamServlet.logger.debug("Initialize ProxyConnectionManager");
            HttpParams httpParams = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
            HttpConnectionParams.setSoTimeout(httpParams, 10000);
            threadSafeConnectionManager.setDefaultMaxPerRoute(400);
            threadSafeConnectionManager.setMaxTotal(400);
        }
    }
}
