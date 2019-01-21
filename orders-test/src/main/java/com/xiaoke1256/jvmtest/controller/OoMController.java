package com.xiaoke1256.jvmtest.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.common.utils.ResponseUtils;
import com.xiaoke1256.jvmtest.bo.SomeBo;

@RestController
@RequestMapping("/oom")
public class OoMController {
	private final Map<SomeBo,String> cacheMap = new HashMap<SomeBo,String>();
	
	private final Map<Integer,byte[]> map2 = new HashMap<Integer,byte[]>();
	
	
	/**
	 * 有内存泄漏漏洞的方法
	 * @param response 
	 * @return
	 */
	@RequestMapping(value="/someBussiness")
	public void someBussiness(HttpServletResponse response) {
		for(int i=1;i<=100;i++) {
			SomeBo s = new SomeBo();
			Random r = new Random();
			int id = r.nextInt(100);
			s.setId(id);
			s.setBy(new byte[1024*1024]);

			cacheMap.put(s, "sswe"+r.nextInt(100*100) );
			map2.put(id, new byte[1024*1024]);
			System.out.println("added "+i+".");
		}
		ResponseUtils.writeToResponse(response, "success!");
	}
}
