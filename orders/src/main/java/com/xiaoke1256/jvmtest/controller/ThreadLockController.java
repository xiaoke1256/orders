package com.xiaoke1256.jvmtest.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/deadLock")
public class ThreadLockController {
	private final ReentrantLock lock1 = new ReentrantLock();
	
	private final ReentrantLock lock2 = new ReentrantLock();
	
	@RequestMapping(value="/")
	public String toIndex(){
		return "threadLock/index";
	}
	
	@RequestMapping(value="/bussieness1")
	public void bussieness1(HttpServletResponse response) {
		try {
			lock1.lock();
			//do something.
			Thread.sleep(100);
			lock2.lock();
			//do something.
			Thread.sleep(500);
			writeToResponse(response,"success!");
		} catch (InterruptedException e) {
			e.printStackTrace();
			writeToResponse(response,"error!");
		}finally {
			lock2.unlock();
			lock1.unlock();
		}
		
	}
	
	@RequestMapping(value="/bussieness2")
	public void bussieness2(HttpServletResponse response) {
		try {
			lock2.lock();
			//do something.
			Thread.sleep(100);
			lock1.lock();
			//do something.
			Thread.sleep(500);
			writeToResponse(response,"success!");
		} catch (InterruptedException e) {
			e.printStackTrace();
			writeToResponse(response,"error!");
		}finally {
			lock1.unlock();
			lock2.unlock();
		}
		
	}
	
	private void writeToResponse(HttpServletResponse response,String s) {
		try {
	        //设置页面不缓存
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
	        e.printStackTrace();
	    }
	}
}
