import java.io.*; 
import java.net.*; 
public class EncryptServer { 

	public static void main(String[] args) throws Exception { 

		int i;
		if(args.length != 1 || args.length != 9)
			exit();
		else if(args.length == 1 && args[0].equals("-h"))
		{
			System.out.println("java CHAT -alice -e bob.public -d alice.private -p <Bob's port> -a address:<Bob's IP Address>");
			System.out.println("java CHAT -bob -e alice.public -d bob.private -p <Alice's port> -a address:<Alice's IP Address>");
			System.exit(0);
		}

		ServerSocket sersock = new ServerSocket(3000); 
		System.out.println("Server ready for chatting"); 
		Socket sock = sersock.accept( ); // reading from keyboard (keyRead object) 
		BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in)); 
		// sending to client (pwrite object) 
		OutputStream ostream = sock.getOutputStream(); 
		PrintWriter pwrite = new PrintWriter(ostream, true);   
		// receiving from server ( receiveRead object) 
		InputStream istream = sock.getInputStream(); 
		BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));   
		String receiveMessage, sendMessage; 

		while(true) { 

			if((receiveMessage = receiveRead.readLine()) != null) 
			{ 
				System.out.println(receiveMessage); 
			} 
			sendMessage = keyRead.readLine(); 

			pwrite.println(sendMessage); 
			pwrite.flush(); 
		}
	} 
}