package com.xiaoke1256.jvmtest.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xiaoke1256.jvmtest.bo.SomeBo;

@Controller
@RequestMapping("/oom")
public class OoMController {
	private Map<SomeBo,String> cacheMap = new HashMap<SomeBo,String>();
	
	@RequestMapping(value="/")
	public String toIndex(){
		return "jvmtest/index";
	}
	
	/**
	 * 有内存泄漏漏洞的方法
	 * @param response 
	 * @return
	 */
	@RequestMapping(value="/someBussiness")
	public void someBussiness(HttpServletResponse response) {
		for(int i=1;i<=100;i++) {
			SomeBo s = new SomeBo();
			s.setId(1);
			s.setBy(new byte[1024*1024]);
			
			@SuppressWarnings("unused")
			byte[] temp1 = new byte[1024*1024];
			@SuppressWarnings("unused")
			byte[] temp2 = new byte[1024*1024];
			
			Random r = new Random();
			cacheMap.put(s, "sswe"+r.nextInt(100*100) );
			System.out.println("added "+i+".");
		}
		try {
	        //设置页面不缓存
	        response.setContentType("application/json");
	        response.setHeader("Pragma", "No-cache");
	        response.setHeader("Cache-Control", "no-cache");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter out= null;
	        out = response.getWriter();
	        out.print("success!");
	        out.flush();
	        out.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
