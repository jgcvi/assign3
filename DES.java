public static void main(String []args) {
	if(args.length < 1)
		exit();

	if(args[1].equals("-h") && args.length == 1)
	{
		System.out.println("java DES -h\njava DES -k\njava DES -e <64_bit_key_in_hex> -i <input_file> -o <output_file>\njava DES -d <64_bit_key_in_hex> -i <input_file> -o <output_file>\n");
		return;
	} else
		exit();

	if(args[1].equals("-k") && args.length == 1)
	{
		generateDES();
	} else
		exit();

	boolean encrypt = false;
	if(args.length == 5)
	{
		String[] arguments = {null, null, null};
		int i;
		for(i = 0; i < 5; i += 2)
		{
			if(args[i].equals("-e"))
			{
				arguments[0] = args[i+1];
				encrypt = true;
			} else if(args[i].equals("-d"))
				arguments[0] = args[i+1];
			else if(args[i].equals("-i"))
				arguments[1] = args[i+1];
			else if(args[i].equals("-o"))
				arguments[2] = args[i+1];
		}
		for(i = 0; i < 3; i ++)
			if(arguments[i] == null)
				exit();
		if(encrypt)
			encrypt(arguments);
		else
			decrypt(arguments);
	}
}

void exit() {
	System.out.println("Type 'java DES -h' for help\n");
	System.exit(1);
}

static void encrypt(String[] args) {
	
}

static void decrypt(String[] args) {

}
