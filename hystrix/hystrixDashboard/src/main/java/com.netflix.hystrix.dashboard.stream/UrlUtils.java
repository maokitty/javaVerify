//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.netflix.hystrix.dashboard.stream;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlUtils {
    public UrlUtils() {
    }

    public static InputStream readXmlInputStream(String uri) {
        if(uri != null && !"".equals(uri)) {
            try {
                URL e = new URL(uri);
                HttpURLConnection connection = (HttpURLConnection)e.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/xml");
                return connection.getInputStream();
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        } else {
            throw new IllegalArgumentException("Invalid uri. URI cannot be null or blank. ");
        }
    }
}
