import java.net.InetAddress;

public static void main(String[] args) {
	if(args.length != 1 || args.length != 9)
		exit();

	else if(args[0] == "-h")
		print();

	boolean client = true;

	for(i = 0; i < 7; i++)
	{
		if(args[i].equals("-alice"))
			client = false;
	}

	if(client)
		client(args);

	else server(args);
	BufferedReader stdin, tcpin;
	try {
		stdin = new BufferedReader(new InputStreamReader(System.in));
		tcpstream = s.getInputStream();
		tcpin = new BufferedReader(new InputStreamReader(tcpstream));

		OutputStream osstream = s.getOutputStream();
		PrintWriter pwrite = new PrintWriter(osstream, true);


		e = new Socket(ip, port);
		String input;
		while(true)
		{
			if(thruPut = stdin.readLine() != null)
				tcpWrite(s, thruPut);
			else if(thruPut = osstream != null)
				System.out.println(thruPut);
		}

}

void exit() {
	System.out.println("type 'java CHAT -h' for help\n");
	System.exit(1);
}

void print() {
	System.out.println("blah");
	exit(0);
}

static void client(String[] args) {
	int i, port;
	Socket s;
	String public_key, ip;
	InputStream tcpstream;
	
	boolean proper = false;
	for(int i = 0; i < 7; i += 2)
	{
		if(args[i].equals("-bob"))
		{
			proper = true;
			i--;
		} else if(args[i].equals("-e"))
		{
			try
				public_key = File.open(args[i+1])
			catch(FileNotFoundException e)
				exit();
		} else if(args[i].equals("-d"))
		{
			try 
				private_key = File.open(args[i+1]);
			catch(FileNotFoundException e)
				exit();

		} else if(args[i].equals("-p"))
			port = Integer.parseInt(args[i+1]);
		
		else if(args[i].equals("-a"))
			ip = args[i+1];
	}

	if(!proper)
		exit();

	// START PSEUDOCODE HERE
	generateDES(); // import DES.class
	encryptWithPublic() // handled with RSA.java
	send();	// handled here
	while(1)
	{
		if(/*receive ok encrypted key Alice received*/)
		{
			startCommunications();	// handled here
			break;
		}
	}
}

static void server(String[] args) {
	for(int i = 0; i < 7; i += 2)
	{
		if(args[i].equals("-bob"))
		{
			proper = true;
			i--;
		} else if(args[i].equals("-e"))
		{
			try
				public_key = File.open(args[i+1])
			catch(FileNotFoundException e)
				exit();
		} else if(args[i].equals("-d"))
		{
			try 
				private_key = File.open(args[i+1]);
			catch(FileNotFoundException e)
				exit();

		} else if(args[i].equals("-p"))
			port = Integer.parseInt(args[i+1]);
		
		else if(args[i].equals("-a"))
			ip = args[i+1];
	}
}

static void startCommunications() {

}

static void tcpWrite(Socket s, String input) {

}
