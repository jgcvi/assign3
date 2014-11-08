import java.awt.Point;
import java.awt.PointerInfo;
import java.util.arraylist;

import java.io.File;
public static void main(String []args) {
	if(args.length < 1)
		exit();

	if(args[1].equals("-h") && args.length == 1)
	{
		System.out.println("java DES -h\njava DES -k\njava DES -e <64_bit_key_in_hex> -i <input_file> -o <output_file>\njava DES -d <64_bit_key_in_hex> -i <input_file> -o <output_file>\n");
		return;
	} else
		exit();

	if(args[0].equals("-k") && args.length == 1)
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
	// figure out how to efficiently represent 24 bits. This isn't C
	long key = Long.ParseLong(args[0]), lowRet;
	int []removals = {7, 15, 23, 31, 39, 47, 55, 63};
	int i;

	FileInputStream fs;

	try {
		fs = new FileInputStream(new File(args[1]));
	} catch (IOException e) {
		System.out.println("Failed opening input in encrypt");
		exit();
	}

	for(i = 0; i < 8; i ++)
	{
		// lowRet retains all bits smaller than the nth bit
		lowRet = key % (1 << (removals[i] + 1));
		// key is now retained bits + larger bits shifted right one
		key = lowRet + (key - (lowRet) >> 1);
	}

	int[] shifts = {1,1,2,2,
			2,2,2,2,
			2,1,2,2,
			2,2,1}
	long right_key = key % (1 << 24);
	// you don't technically need to subtract, whatever
	long left_key = (key - right_key) >> 23;

	for(int i = 0; i < 15; i ++)
	{
		applyOperations(fs, keyArray); // pseudocoding

		// first operand is the low bits, second is the higher bits. 
		right_key = right_key >> (24 - shifts[i]) + right_key << shifts[i];
		left_key = left_key >> (24 - shifts[i]) + left_key << shifts[i];
	}
}

private void applyOperations(FileInputStream fs, byte[][] keyArray){

	Arraylist <String> blockList = new Arraylist <String>();
	byte next;
	int count, 
	int index = 0;
	while((next = fs.read()) != null) {
		count = count + 1 % 64;
		if(count == 0){
			index++;
		}
		blockList.set(index, blockList.get(index).concat((String)next));
	}

	String temp = blockList.get(index);
	while(count < 64 && count != 0)
	{
		temp = '0' + temp;
		count++;
	}

	blockList.set(index, temp);


	int size = blockList.size();

	for(int i = 0; i < size; i++){
		for(int j = 0; j < 64; j++){
			System.out.printf("%X", blockList.set(i, blockList.get(i) ^ keyArray[i][j]);
		}
	}



}

static void decrypt(String[] args) {

}

void generateDES() {
	long time = System.currentTimeMillis(), point, time2;
	Point pt = MouseInfo.getPointerInfo().getLocation();
	point = pt.getY() * (1 + pt.getY());
	time *= time ^ point;

	if(time < Math.pow(2, 31)
		time += Math.pow(7, pt.getY());
   	else if (time < Math.pow(2, 53)
		time += Math.pow(11, pt.getX());

	time2 = System.currentTimeMillis();
	time ^= time2;

	System.out.printf("%x", time);

}
