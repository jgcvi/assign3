public static void main(String []args) {
	if(args.length < 1)
		exit();

	else if(args.length == 3)
	{
		String[] arguments = {null, null};
		int i;
		for(i = 0; i < 3; i += 2)
		{
			if(args[i].equals("-k"))
				arguments[0] = args[i+1];
			else if(args[i].equals("-b"))
				arguments[1] = args[i+1];
		}
		
		if(arguments[0] == null || arguments[1] == null)
			exit();
		else generateKey(arguments);

	} else if(args.length == 5)
	{
		boolean encrypt = false;
		int i;
		String[] arguments = {null, null, null};
		for(i = 0; i < 5; i += 2)
		{
			if(args[i].equals("-e"))
			{
				encrypt = true;
				arguments[0] = args[i+1];
			} else if(args[i].equals("-i"))
				arguments[1] = args[i+1];
			else if(args[i].equals("-o"))
				arguments[2] = args[i+1];
		}

		for(String el : arguments)
			if(el == null)
				exit();

		if(encrypt)
			encrypt(arguments);
		else decrypt(arguments);
	}

	else exit();
}

void exit() {
	System.out.println("Type 'java RSA -h' for help\n");
	System.exit(1);
}

static void encrypt(String[] args) {

}

static void decrypt(String[] args) {

}

void generateKey(String []args) {

}
