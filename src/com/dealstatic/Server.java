package com.dealstatic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * Title: Server.java    
 * Description: 本简易Tomcat只适合处理静态资源
 * 1. Tomcat开始运行之后，会在主机上开一个端口（默认是80端口），在所开辟的端口上运行一个ServerSocket，执行accept()方法等待浏览器访问。
 * 2. 浏览器访问目标主机的80端口，ServerSocket的accept()方法返回一个运行在服务器端的socket，
 * 	 该socket有getInputStream()方法和getOutputStream方法，可以获得浏览器对服务器发送的内容和响应浏览器并发送内容。
 * 3. 浏览器收到服务器响应的内容，通过浏览器解释，内容显示在用户眼前。
 * 注意：
 * 1. 浏览器发送给服务器的访问请求符合HTTP协议。
 * 2. 浏览器可以像服务器请求一个HTML页面，也可以请求图片，若HTML页面中导入了图片或者css，js，
 * 则这些内容也会像服务器请求，服务器会根据文件名，对浏览器进行响应，若该文件在服务器目录中找不到，则返回404错误。
 * 
 * @author chengge
 * @date 2018年5月28日
 *
 */
public class Server {
	public static void main(String[] args) {
		Server server=new Server();
		server.startServer();
	}
	
	/**
	 * 此类接受请求，然后创建线程，到handler类中去处理浏览器发送的请求
	 */
	
	public void startServer() {
		
		try {
			ServerSocket serverSocket=new ServerSocket(80);//开启80端口
			System.out.println("服务器启动>>>80端口");
			while(true) {//启用一个循环，一直监听80端口，等待客户端访问
				Socket socket=serverSocket.accept();
				Thread thread=new Thread(new Handler(socket));//当有客户端访问时，开辟一个新的线程处理该请求
				System.out.println("收到新的请求");
				thread.start();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
