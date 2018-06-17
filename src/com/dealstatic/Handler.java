package com.dealstatic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

//请求处理线程
public class Handler implements Runnable {
	
	private Socket socket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private BufferedReader reader;
	private PrintWriter writer;
	private static String WEB_ROOT="D:\\web";//服务器存储文件的目录
	

	public Handler(Socket socket) {
		this.socket = socket;
	}


	@Override
	public void run() {
		
		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			reader=new BufferedReader(new InputStreamReader(inputStream));//包装后的输入缓冲字符流
			writer=new PrintWriter(new OutputStreamWriter(outputStream));//包装后的输出缓冲字符流
			String msg;//接收客户端请求的临时字符串
			StringBuffer req=new StringBuffer();//将请求拼接成完整的请求
			while((msg=reader.readLine())!=null && msg.length()>0) {
				req.append(msg);
				req.append("\n");//HTTP协议的格式
			}
			String[] msgs=req.toString().split(" ");//HTTP协议以空格为分隔符
			//打印出 request请求的全部内容
			System.out.println("请求："+req.toString()+" msgs.length="+msgs.length+" msgs[1]="+msgs[1]);
			//msgs[1]代表了HTTP协议中的第二个字符串，是浏览器请求的文件名
			if (msgs[1].endsWith(".ico")) {//.ico文件是浏览器页面的图标文件，是浏览器默认会向服务器发送的请求
				writer.println("HTTP/1/1 200 OK");//如果服务器不打算对客户端发送.ico结尾的文件，
													//可以这样写，然后直接返回，跳过发送该文件的过程
				writer.println("content-Type:text/html;charset=UTF-8");
				writer.close();
				//如果不发送就直接返回
				return;
			}
			
			
			//将服务器目录下被请求的文件读入程序
			FileInputStream fileInputStream=new FileInputStream(new File(WEB_ROOT+msgs[1]));
			byte[] filebuffer=new byte[1024];//可能是二进制文件，所以需要用byte数组左焕琮
			int length=0;
			while((length=fileInputStream.read(filebuffer))!=-1) {
				outputStream.write(filebuffer, 0, length);//向客户端浏览器发送文件
			}
			outputStream.close();
			inputStream.close();
			reader.close();
			fileInputStream.close();
			
		} catch (IOException e) {
			//如果抛出异常，在这里基本上是未找到被请求的文件，所以返回404
			writer.write("HTTP/1.1 404 ERROR:FILE NOT FOUND");
			writer.close();
		}
		
	}

}
