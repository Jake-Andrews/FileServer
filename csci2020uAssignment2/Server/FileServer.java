import java.io.*;
import java.net.*;

public class FileServer {
	protected Socket clientSocket           = null;
	protected ServerSocket serverSocket     = null;
	protected FileServerThread[] threads    = null;
	protected int numClients                = 0;

	public static int SERVER_PORT = 16789;
	public static int MAX_CLIENTS = 25;

	public FileServer() {
        /**
         * This class represents a server object that passively listens 
         * to a port for messages from a client
         */
		try {
			serverSocket = new ServerSocket(SERVER_PORT);                   //initialze and connect with a client socket
			System.out.println("---------------------------");
			System.out.println("File Server Application is running");
			System.out.println("---------------------------");
			System.out.println("Listening to port: "+SERVER_PORT);
			threads = new FileServerThread[MAX_CLIENTS];
			while(true) {
				clientSocket = serverSocket.accept();
				System.out.println("Client #"+(numClients+1)+" connected.");
				threads[numClients] = new FileServerThread(clientSocket);     //each client gets their own thread
				threads[numClients].start();
				numClients++;
			}
		} catch (IOException e) {
			System.err.println("IOException while creating server connection");
		}
	}

	public static void main(String[] args) {
		FileServer app = new FileServer();
	}
}