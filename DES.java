import java.awt.Point;
import java.awt.PointerInfo;
import java.util.arraylist;

import java.io.File;
public class DES {
	int _keyLen = 56;

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

	public static void encrypt() {
		FileInputStream fs = openInputFile(args[1]);
		PrintWriter fw = openOutputFile(args[2]);

		byte[][] keyArray = getAllKeys(args[0]);

		applyOperations(fs, fw, keyArray);
	}

	private PrintWriter openOutputFile(String filename) {
		PrintWriter fw;

		try {
			fw = new PrintWriter(filename, "UTF-8"));
		} catch (Excepton e)
		{
			System.out.println("Failed to open output file");
			exit();
		}
	}
	private FileInputStream openInputFile(String filename) {
		FileInputStream fs;

		try {
			fs = new FileInputStream(new File(filename));
		} catch (IOException e)
		{
			System.out.println("Failed to open input file");
			exit();
		}
		return fs;
	}

	private byte[] generateShiftKey(int shift, byte[] key) {
		byte[] retain = new byte[shift];
		if(shift == 2)
		{
			retain[0] = key[0];
			retain[1] = key[1];
		} else
			retain[0] = key[0];

		for(int i = shift; i < key.length; i ++)
			key[i-shift] = key[i];

		if(shift == 2)
		{
			key[key.length - 2] = retain[0];
			key[key.length - 1] = retain[1];
		}

		return key;
	}

	private byte[][] getAllKeys(String key) {
		byte[] combinedKey = new byte[56];
		int []removals = {7, 15, 23, 31, 39, 47, 55, 63};
		int i, count = 0, j;

		// retain the proper bytes
		for(i = 0; i < 64; i ++)
		{
			if(removals.contains(i))
				continue;
			else
			{
				combinedKey[count] = (byte) key.charAt(i);
				count ++;
			}
		}

		int[] shifts = {1,1,2,2,
				2,2,2,2,
				2,1,2,2,
				2,2,1};

		byte[][] keyArray = new byte[16][48];

		for(i = 0; i < shifts.length; i ++)
			keyArray[0][i] = combinedKey[i];

		for(i = 1; i < shifts.length; i ++)
		{
			combinedKey = generateShiftKey(shifts[i], combinedKey);

			for(j = 0; j < 48; j ++)
				keyArray[i][j] = combinedKey[j];
		}

		return keyArray;
	}

	private void applyOperations(FileInputStream fs, PrintWriter fw, byte[][] keyArray){

		Arraylist <String> blockList = new Arraylist <String>();
		byte next;
		int count, index = 0;
		while((next = fs.read()) != null) {
			count = (count + 1) % _keyLen;
			if(count == 0){
				index++;
			}
			blockList.set(index, blockList.get(index).concat((String) next));
		}

		String temp = blockList.get(index);
		while(count < _keyLen && count != 0)
		{
			temp = "0".concat(temp);
			count++;
		}

		blockList.set(index, temp);


		int size = blockList.size();
		String temp;
		for(int i = 0; i < size; i++){
			temp = blockList.get(i);

			for(int j = 0; j < _keyLen; j++)
				fw.print("%X", temp.charAt(j) ^ keyArray[i % 16][j]);

			fw.println();
		}
	}

	public static void decrypt(String[] args) {
		FileInputStream fs = openInputFile(args[1]);
		PrintWriter fw = openOutputFile(args[2]);

		byte[][] keyArray = getAllKeys(args[0]);

		applyOperations(fs, fw, keyArray);
	}

	public static void generateDES() {
		// may have to worry about signedness issues. We shall see.
		long time = System.currentTimeMillis(), point, time2;
		Point pt = MouseInfo.getPointerInfo().getLocation();
		point = pt.getY() * (1 + pt.getY());
		time *= time ^ point;

		// don't refactor the pow operations. They're there so time2 has enough
		// difference to matter
		if(time < Math.pow(3, 23))
			time += Math.pow(7, pt.getY());
	   	else if (time < Math.pow(3, 31))
			time += Math.pow(11, pt.getX());

		time2 = System.currentTimeMillis();
		time ^= time2;

		System.out.printf("%X", time);
	}
}