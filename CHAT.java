import java.net.InetAddress;

public static void main(String[] args) {
	if(args.length != 1 || args.length != 7)
		exit();

	else if(args[0] == "-h")
		print();

	int i, port;
	Socket s;
	String public_key, ip;
	for(i = 0; i < 7; i += 2)
	{
		if(args[i].equals("-e"))
		{
			try
				public_key = File.open(args[i+1])
			catch(FileNotFoundException e)
				exit();
		} else if(args[i].equals("-p"))
			port = Integer.parseInt(args[i+1]);
		else if(args[i].equals("-a"))
			ip = args[i+1];
	}

	BufferedReader stdin, tcpin;
	try {
		stdin = new BufferedReader(new InputStreamReader(System.in));
		tcpin = new BufferedReader(new InputStreamReader(s));
		e = new Socket(ip, port);
		String input;
		while(true)
		{
			if(input = stdin.readLine() != null)
				tcpWrite(s, input);
			else if(input = s.readLine() != null)
				System.out.println(input);
		}

}

void exit() {
	System.out.println("type 'java CHAT -h' for help\n");
	System.exit(1);
}

void print() {
	System.out.println("blah");
	exit(1);
}

static void tcpWrite(Socket s, String input) {

}
