/**
 * Worker Thread to handle new connection to server
 * 
 * @author 	Chintav Shah
 * @version	4.0, November 4, 2016
 *
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class Worker extends Thread {
	Socket socket;
	public Worker(Socket client) { 																	//default constructor for worker thread

		socket = client;

	}

	public void run() {
		
		try {
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream()); 		//new output stream to send files to client

			Scanner inputStream = new Scanner(socket.getInputStream(), "UTF-8"); 					//used to parse GET request
			String getRequest = inputStream.nextLine(); 											//read the GET request

			String[] parse = getRequest.split(" "); 
			String get = parse[0]; 																	//check if get request starts with "GET"
			String httpVersion = parse[2]; 															//check HTTP version
			System.out.println(".....................................");
			Date currentDate = new Date();
			System.out.println("Date: " + currentDate.toString()); 									//print current date

			//check GET request format
			if ((get.compareTo("GET") != 0) || ((httpVersion.compareTo("HTTP/1.1") != 0) && (httpVersion.compareTo("HTTP/1.0") != 0))) 
			{ 
				System.out.println("400 Bad Request"); 
				System.out.println(".....................................");			
				return;													
			}


			String fileName = parse[1].substring(1); 												//file name from the GET request

			
			FileInputStream fileStream;

			try {

				fileStream = new FileInputStream(fileName); 										//create a FileInputStream if the file name exists otherwise print error
			}

			catch(FileNotFoundException e) {
				System.out.println("404 Not Found"); 												//404 error if file does not exist in current directory
				//System.out.println("Connection: close");
				System.out.println(".....................................");
				//outputStream.write(("HTTP/1.0 404 Not Found\r\n").getBytes()); 
				return;
			}
			


			byte[] fileArray = new byte[fileStream.available()]; 				//byte array to store file content in bytes 												
			int contentLength = fileStream.read(fileArray); 					//read the file byte by byte and store number of bytes read in Content-Length
			System.out.println("Content-Length: " + contentLength); 			//print content length to 

			//Write HTTP Header Response to output stream (client)
			outputStream.write(("HTTP/1.0 200 OK\r\n").getBytes()); 
			outputStream.write(("Server: My First Server\r\n").getBytes()); 
			outputStream.write(("Content-Length: " + contentLength+"\r\n").getBytes());
			outputStream.write(("Connection: close\r\n\r\n").getBytes());
			
			outputStream.write(fileArray); 															//write file contents to client
			outputStream.flush(); 																	//flush output stream
			
			System.out.println("Connection: close"); 												//inform client that connection has closed
			System.out.println(".....................................");
			socket.close(); 																		//close socket


			

		}

		

		catch (IOException e) {
			e.getMessage();
		}



	}
}
