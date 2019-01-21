package com.xiaoke1256.common.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class ResponseUtils {
	public static void writeToResponse(HttpServletResponse response,String s) {
		try {
	        //设置页面不缓�?
	        response.setContentType("application/json");
	        response.setHeader("Pragma", "No-cache");
	        response.setHeader("Cache-Control", "no-cache");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter out= null;
	        out = response.getWriter();
	        out.print(s);
	        out.flush();
	        out.close();
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
}
