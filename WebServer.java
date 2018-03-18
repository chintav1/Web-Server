/**
 * Main thread for server
 * 
 * @author 	Chintav Shah
 * @version	2.0 November 4, 2016
 *
 */

import java.io.*;
import java.net.*;


public class WebServer extends Thread {
	
	private volatile boolean shutdown = false;										//shutdown flag
	ServerSocket server;

	public WebServer(int port) {
		try {
			server = new ServerSocket(port); 										//initialize new server socket
		}

		catch (SocketException e) {
			e.getMessage();
		}

		catch (IOException e) {
			e.getMessage();
		}
	}

	public void run() {

		while (!shutdown) {
			try {
				Socket socket = server.accept(); 								//accept a new connection
				Worker client = new Worker(socket); 							//worker thread to handle new connection
				client.start(); 												//start the worker thread
			}


			catch (IOException e) {
				e.getMessage();
			}
		}
	}

	public void shutdown() {
		shutdown = true; 														 //set shutdown flag to true
		try {
			server.close();														//close the server
		}
		catch (IOException e) {
			e.getMessage();
		}
	}
}
