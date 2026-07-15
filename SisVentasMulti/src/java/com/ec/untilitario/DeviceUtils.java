/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.untilitario;
import javax.servlet.http.HttpServletRequest;
import org.zkoss.zk.ui.Executions;

public class DeviceUtils {

    public static boolean isMobileDevice() {
        // Obtener el HttpServletRequest desde ZK
        HttpServletRequest request = (HttpServletRequest) 
            Executions.getCurrent().getNativeRequest();
        
        if (request == null) return false;
        
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isEmpty()) return false;

        // Palabras clave para detectar móviles
        String[] mobileKeywords = {
            "Android", "iPhone", "iPad", "iPod", "BlackBerry",
            "Windows Phone", "Mobile", "Opera Mini", "IEMobile", "webOS"
        };

        userAgent = userAgent.toLowerCase();
        for (String keyword : mobileKeywords) {
            if (userAgent.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}