import java.io.*;
import java.net.*;
import java.util.*;

public class FileServerThread extends Thread {
	protected Socket socket       = null;
	protected PrintWriter out     = null;
	protected BufferedReader in   = null;


	public FileServerThread(Socket socket) {
		super();
		this.socket = socket;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);						//writing out to the socket
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));	//reading in from the socket
		} catch (IOException e) {
			System.err.println("IOEXception while opening a read/write connection");
		}
	}

	public void run() {	
		giveClientFileList();						

		boolean endOfSession = false;

		while(!endOfSession) {
			//endOfSession = processCommand();
			//Just keep looking for a message. 
			//If the server every sends a message, then we should close the socket.
			String message = null;
			try {
				if ((message = in.readLine()) != null) {								//pasively reading messages until client sends one
					StringTokenizer st = new StringTokenizer(message);					//Divides message into filename, and command
					String fileName = st.nextToken();
					String recieveOrSend = st.nextToken();
					endOfSession = true; 												//after message has been read, quit waiting trying to read more

					if (recieveOrSend.equalsIgnoreCase("receieve")) {					//uploading or downloading the file
						receieveFile(fileName);
					} else {sendFile(fileName);}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			socket.close();																//disconnect after action
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	protected void sendFile(String fileName) throws IOException {
		/**
		 * Downloads a file from the server.
		 * Writes filename and file contents to from the server 
		 * folder, to the client socket.
		 * 
		 * @param filename String name of the file to download
		 */

		System.out.println("Sending " + fileName + " to client!");
        String pathey = System.getProperty("user.dir");									//build path to client folder
        pathey = pathey + "/TextFiles/" + fileName;

        File file = new File(pathey);										

        OutputStream os = socket.getOutputStream();											//writes to client socket
        BufferedInputStream into = new BufferedInputStream(new FileInputStream(file));		//reads through file to be downloaded
        byte[] buffer = new byte[1024];

        int count;
        while ((count = into.read(buffer)) >= 0) {										//while bytes are being read in, write to client socket.
            os.write(buffer, 0, count);
            os.flush();
        }	
		into.close();
	}

	protected void receieveFile(String fileName) throws IOException { 
		/**
		 * Uploads a file to the server.
		 * Reads in a file from the client socket and  
		 * writes to a new file created in server folder.
		 * 
		 * @param filename String name of the file to download
		 */

		System.out.println(fileName + " has been uploaded to the Server!");			
		String pathey = System.getProperty("user.dir");									//building path to client folder
		pathey = pathey + "/TextFiles/" + fileName;


		FileOutputStream fos = new FileOutputStream(pathey);							//streams to write to file
        BufferedOutputStream bos = new BufferedOutputStream(fos);		
        byte[] buffer = new byte[1024];
	
        InputStream into = socket.getInputStream();										//reading in from client
        int count;
        while ((count = into.read(buffer)) >=  0){										//while bytes are being read in, write content to new file
            fos.write(buffer,0,count);
        }
        fos.close();											

	}

	protected void giveClientFileList() {
		/**
		 * Shows the clients file list.
		 * Writes all the files in the clients folder   
		 * to the client socket to display.
		 */

		String pathey = System.getProperty("user.dir");									//building path to client folder
		pathey = pathey + "/TextFiles";

		File file = new File(pathey);													
		File[] files = file.listFiles();												//adds all files in clients folder to array
		String fileNames = ""; 

		for (int i = 0; i < files.length; i++) {										
			if (i+1 == files.length) {													//if last element, dont append the space
				fileNames = fileNames + files[i].getName();	
			} else {			
				fileNames = fileNames + files[i].getName() + ",";}						//add each file name to the string
		}
		out.println(fileNames);															//write names to client socket
	}
}