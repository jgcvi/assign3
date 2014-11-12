import java.net.*;
import java.io.*;

public class CHAT{

	ServerSocket sersock;
	//System.out.println("Server ready for chatting"); 
	Socket sock;
	// reading from keyboard (keyRead object) 
	static BufferedReader keyRead; 
	// sending to client (pwrite object) 
	OutputStream ostream; 
	PrintWriter pwrite;   
	// receiving from server ( receiveRead object) 
	InputStream istream; 
	static BufferedReader receiveRead;   
	String receiveMessage, sendMessage;

	public static void main(String[] args) {

		RSA myRSA = new RSA();
		DES myDES = new DES();

		boolean client = true;
		String[] arguments = new String[5];

		if(args.length != 1 || args.length != 9)
			System.exit(0);

		else if(args[0] == "-h")
			print();

		else if(args.length == 9){

			for(int i = 0; i < args.length; i++){
				if(args[i].equals("-alice"))
				{
					client = false;
					arguments[0] = "-alice";
					i++;
				}
				else if(args[i].equals("-bob")){
					arguments[0] = "-bob";
					i++;
				}
				else if(args[i].equals("-e")){
					arguments[1] = args[i+1];
					i++;
				}
				else if(args[i].equals("-d")){
					arguments[2] = args[i+1];
					i++;
				}
				else if(args[i].equals("-p")){
					arguments[3] = args[i+1];
					i++;
				}
				else if(args[i].equals("-a")){
					arguments[4] = args[i+1];
					i++;
				}

			}

			if(client){
				client(arguments);
			}
			else{
				server(arguments);
			}
		}

			
			//server(args);
				// try {
				// 	keyRead = new BufferedReader(new InputStreamReader(System.in));
				// 	istream = s.getInputStream();
				// 	receiveRead = new BufferedReader(new InputStreamReader(tcpstream));

				// 	OutputStream osstream = s.getOutputStream();
				// 	PrintWriter pwrite = new PrintWriter(osstream, true);


				// 	e = new Socket(ip, port);
				// 	String input;
				// 	while(true)
				// 	{
				// 		if(thruPut = stdin.readLine() != null)
				// 			tcpWrite(s, thruPut);
				// 		else if(thruPut = osstream != null)
				// 			System.out.println(thruPut);
				// 	}
				// } catch(Exception e){}
			
		else{
			System.exit(0);
		}
	}

	// static void exit() {
	// 	System.out.println("If Alice: java CHAT -alice -e bob.public -d alice.private -p <Bob's port> -a address:<Bob's IP Address>");
	// 	System.out.println("If Bob: java CHAT -bob -e alice.public -d bob.private -p <Alice's port> -a address:<Alice's IP Address>");
	// 	System.exit(0);
	// }

	static void print() {
		System.out.println("If Alice: java CHAT -alice -e bob.public -d alice.private -p <Bob's port> -a address:<Bob's IP Address>");
		System.out.println("If Bob: java CHAT -bob -e alice.public -d bob.private -p <Alice's port> -a address:<Alice's IP Address>");
		System.exit(0);
	}

	static void client(String[] args) {
		int i, port;
		Socket s;
		String public_key, ip;
		InputStream tcpstream;
		
		boolean proper = false;
		
		if(args[0].equals("-bob"))
		{
			proper = true;
			i--;
		} 

		try{
			public_key = File.open(args[1]);
		}
		catch(FileNotFoundException e){}
			System.exit(0);

		try {
				private_key = File.open(args[2]);
			}
		catch(FileNotFoundException e){}
				System.exit(0);

		port = Integer.parseInt(args[3]);
		
		ip = args[4];
		

		if(!proper)
			System.exit(0);

		// START PSEUDOCODE HERE
		myDES.generateDES(); // import DES.class
		myRSA.encryptWithPublic(); // handled with RSA.java
		send();	// handled HERE
		

		Socket sock = new Socket("127.0.0.1", 3000); 
		// reading from keyboard (keyRead object) 
		BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in)); 
		// sending to client (pwrite object) 
		OutputStream ostream = sock.getOutputStream(); 
		PrintWriter pwrite = new PrintWriter(ostream, true);   
		// receiving from server ( receiveRead object) 
		InputStream istream = sock.getInputStream(); 
		BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));   
		System.out.println("Start the chitchat, type and press Enter key");   
		String receiveMessage, sendMessage; 
		while(true) { 
			sendMessage = keyRead.readLine(); 
			// keyboard reading pwrite.println(sendMessage); // sending to server 
			pwrite.flush(); // flush the data 
			if((receiveMessage = receiveRead.readLine()) != null) //receive from server 
			{ 
				startCommunications();
				System.out.println(receiveMessage); 
		// displaying at DOS prompt 
			} 
		} 
	}

	static void server(String[] args) {

		int i, port;
		Socket s;
		String public_key, ip;
		InputStream tcpstream;
		
		boolean proper = false;

		if(args[0].equals("-alice"))
		{
			proper = true;
			i--;
		} 

		try{
			public_key = File.open(args[1]);
		}
		catch(FileNotFoundException e){}
			System.exit(0);

		try {
				private_key = File.open(args[2]);
			}
		catch(FileNotFoundException e){}
				System.exit(0);

		port = Integer.parseInt(args[3]);
		
		ip = args[4];

		ServerSocket sersock = new ServerSocket(3000); 
		System.out.println("Server ready for chatting"); 
		Socket sock = sersock.accept( ); // reading from keyboard (keyRead object) 
		BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in)); 
		// sending to client (pwrite object) 
		OutputStream ostream = sock.getOutputStream(); 
		PrintWriter pwrite = new PrintWriter(ostream, true);   
		// receiving from server ( receiveRead object) 
		InputStream istream = sock.getInputStream(); 
		
		String receiveMessage, sendMessage; 

		receiveRead = new BufferedReader(new InputStreamReader(istream));
		while(true)
		{
			if(receiveRead != null)
			{
				pwrite.println(RSAencrpyt("OK", args[1])); // sending to server 
				pwrite.flush();
				startCommunications();
				break;
			}
		}

	}

	static void startCommunications() {
		keyRead = new BufferedReader(new InputStreamReader(System.in));
		istream = s.getInputStream();
		receiveRead = new BufferedReader(new InputStreamReader(istream));
			
		while(true){
			sendMessage = keyRead.readLine(); // keyboard reading 

			if(sendMessage != null){
				pwrite.println(sendMessage); // sending to server 
				pwrite.flush();
			}

			if((receiveMessage = receiveRead.readLine()) != null) //receive from server 
				System.out.println(receiveMessage); // displaying at DOS prompt
		}
	}
}
