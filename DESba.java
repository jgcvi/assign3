import java.awt.*;
import java.util.ArrayList;
import java.io.*;
import java.io.File;

public class DES {
	static int _keyLen = 16;

	public static void main(String []args) throws Exception {
		if(args.length < 1)
			exit();

		if(args[0].equals("-h") && args.length == 1)
		{
			System.out.printf("java DES -h\njava DES -k\njava DES -e <64_bit_key_in_hex> -i <input_file> -o <output_file>\njava DES -d <64_bit_key_in_hex> -i <input_file> -o <output_file>\n");
			return;
		} else if(args[0].equals("-k") && args.length == 1)
		{
			generateDES();
		} else if(args.length == 1)
			exit();

		boolean encrypt = false;
		if(args.length == 6)
		{
			String[] arguments = {null, null, null};
			int i;
			for(i = 0; i < 6; i += 2)
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
		else exit();
	}

	static void exit() {
		System.out.println("Type 'java DES -h' for help\n");
		System.exit(1);
	}

	static void encrypt(String[] args) throws Exception{
		FileInputStream fs = openInputFile(args[1]);
		PrintWriter fw = openOutputFile(args[2]);
		byte[][] keyArray = getAllKeys(args[0]);

		applyOperations(fs, fw, keyArray);
		fw.close();
		fs.close();
	}

	/*-------------------------------------------------------------------------------------
	 |	Purpose:	opens an output stream
	 |
	 |	Pre-Cond:	filename is valid
	 |
	 |	Post-Cond:	the file is opened
	 |
	 |	Parameters:	filename:	the file to be opened
	 |
	 |	Returns:	PrintWriter:	a stream to write to
	 |
	 `-------------------------------------------------------------------------------------*/
	static PrintWriter openOutputFile(String filename) {
		PrintWriter fw;

		try {
			fw = new PrintWriter(filename, "UTF-8");
			return fw;

		} catch (Exception e)
		{
			System.out.println("Failed to open output file");
			exit();
		}
		return null;
	}

	/*-------------------------------------------------------------------------------------
	 |	Purpose:	opens a FileInputStream
	 |
	 |	Pre-Cond:	filename is valid
	 |
	 |	Post-Cond:	the file is opened
	 |
	 |	Parameters:	filename:	the string name to be opened
	 |
	 |	Returns:	FileInputStream:	a stream to read from
	 |
	 `-------------------------------------------------------------------------------------*/
	static FileInputStream openInputFile(String filename) {
		FileInputStream fs;

		try {
			fs = new FileInputStream(new File(filename));
			return fs;
		} catch (IOException e)
		{
			System.out.println("Failed to open input file");
			exit();
		}
		return null;
	}

	/*-------------------------------------------------------------------------------------
	 |	Purpose:	Generates a key shifted by n bytes
	 |
	 |	Pre-Cond:	the key is fully initialized
	 |
	 |	Post-Cond:	the key is shifted, with wrap-around
	 |
	 |	Parameters:	shift:	the number of indices shifted left
	 |				key:	the array of bytes that needs to be shifted
	 |
	 |	Returns:	byte[]:	the key, post shift
	 |
	 `-------------------------------------------------------------------------------------*/
	static byte[] generateShiftKey(int shift, byte[] key) {
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

	static byte[][] getAllKeys(String key) {
		byte[] combinedKey = new byte[_keyLen];
		int[] removals = {7, 15, 23, 31, 39, 47, 55, 63};
		int i, count = 0, j;

		// retain the proper bytes
		for(i = 0; i < _keyLen; i ++)
		{
		/*	if(i == 7 || i == 15 || i == 23 || i == 31 || i == 39 || 
				i == 47 || i == 55 || i == 63)
				continue;
			else*/
			{
				System.out.print(key.charAt(i));
				combinedKey[count] = (byte) key.charAt(i);
				count ++;
			}
		}

		int[] shifts = {1,1,2,2,
				2,2,2,2,
				2,1,2,2,
				2,2,1};

		byte[][] keyArray = new byte[16][_keyLen];

		for(i = 0; i < shifts.length; i ++)
			keyArray[0][i] = combinedKey[i];

		for(i = 1; i < shifts.length; i ++)
		{
			combinedKey = generateShiftKey(shifts[i], combinedKey);

			for(j = 0; j < _keyLen; j ++)
				keyArray[i][j] = combinedKey[j];
		}

		return keyArray;
	}

	static void applyOperations(FileInputStream fs, PrintWriter fw, byte[][] keyArray) {
		ArrayList <String> blockList = new ArrayList <String>();
		byte next;
		int count = -1, index = -1;
		try {
			while((next = (byte) fs.read()) != -1 && count ++ > -2) {
				if(count % _keyLen == 0){
					index++;
					blockList.add("");
				}
				blockList.set(index, blockList.get(index) + (char) next);
			}
		} catch(Exception e){
			e.printStackTrace(System.out);
		}

		String temp = blockList.get(index);
		count = count % _keyLen;
		System.out.println(count);
		while(count < _keyLen && count != 0)
		{
			temp = "0" + temp;
			count++;
		}

		
		blockList.set(index, temp);
		
		for(int i = 0; i < blockList.size(); i ++)
		{
			System.out.println(blockList.get(i));
		}

		int size = blockList.size();
		for(int i = 0; i < size; i++){
			temp = blockList.get(i);


			for(int j = 0; j < _keyLen; j++)
			{
				//System.out.printf("%X", temp.charAt(j) ^ keyArray[i % 16][j]);
				try {
					System.out.printf("%c xor %c is %x", temp.charAt(j), keyArray[i % 16][j], 
						temp.charAt(j) ^ keyArray[i%16][j]);
					fw.printf("%X", temp.charAt(j) ^ keyArray[i % 16][j]);
				} catch(Exception e){
					e.printStackTrace(System.out);
				}
			}
		//		System.out.println(temp.length());
		//		System.out.println(blockList.size());
			System.out.println();
			fw.println();
		}
	}

	static void decApplyOperations(FileInputStream fs, PrintWriter fw, byte[][] keyArray) {
		ArrayList <String> blockList = new ArrayList <String>();
		byte next;
		int count = -1, index = -1;
		try {
			while((next = (byte) fs.read()) != -1 && count ++ > -2) {
				if(count % (_keyLen * 2) == 0){
					index++;
					blockList.add("");
				}

				if((byte) next == 10 && count == 63)
				{
					count --;
					continue;
				}
				else if(blockList.get(index).equals("10"))
				{
					blockList.set(index, "");
					count --;
				}
				blockList.set(index, blockList.get(index) + (byte) next);
			}
		} catch(Exception e){
			e.printStackTrace(System.out);
		}

		String temp, hexStr;

		System.out.println("blocklist length: " + blockList.get(1));
		for(int i = 0; i < blockList.size(); i++){
			temp = blockList.get(i);
			int convert, strJump = 0;
			String concat = "";
		//		int j = 0;

			for(int j = 0; j < _keyLen; j++)
			{
				//System.out.printf("%X", temp.charAt(j) ^ keyArray[i % 16][j]);
				try {

					for(int k = 0; k < 2; k ++)
					{
			//						if(concat.length() == 2)
			//						{
			//							j = 0;
			//							break;
			//						}
						hexStr = temp.substring(strJump, strJump + 2);
						convert = Integer.parseInt(hexStr);
			//						if((char)convert != '\n')
							concat = concat + (char)convert;
						strJump +=2;
					}

			//					if(concat.length() == 2)
			//					{
						System.out.printf("%x xor %c is %c\n", Integer.parseInt(concat,16), 
							keyArray[i % 16][j], Integer.parseInt(concat,16) ^ keyArray[i % 16][j]);
							//you need to store the hex value in concat;
						fw.write((char)(Integer.parseInt(concat,16) ^ keyArray[i % 16][j]));
						concat = "";
			//					}
			//					System.out.println("Converted str is " + (char) convert);
			//			fw.printf("%c", convert ^ keyArray[i % 16][j]);
				} catch(Exception e){
					e.printStackTrace(System.out);
				}
			}
		}
	}

	public static void decrypt(String[] args) throws Exception {
		FileInputStream fs = openInputFile(args[1]);
		PrintWriter fw = openOutputFile(args[2]);

		byte[][] keyArray = getAllKeys(args[0]);

		decApplyOperations(fs, fw, keyArray);
		fw.close();
		fs.close();
	}

	/*-------------------------------------------------------------------------------------
	 |	Purpose:	print a DES key
	 |
	 |	Pre-Cond:	MouseInfo is imported
	 |
	 |	Post-Cond:	a DES key was printed to console
	 |
	 |	Parameters:	n/a
	 |
	 |	Returns:	void
	 |
	 `-------------------------------------------------------------------------------------*/
	public static void generateDES() {
		// may have to worry about signedness issues. We shall see.
		long time = System.currentTimeMillis(), point, time2;
		Point pt = MouseInfo.getPointerInfo().getLocation();
		point = (long) (pt.getY() * (1 + pt.getY()));
		time *= time ^ point;

		// don't refactor the pow operations. They're there so time2 has enough
		// difference to matter
		if(time < Math.pow(3, 23))
			time += Math.pow(7, pt.getY());
	   	else if (time < Math.pow(3, 31))
			time += Math.pow(11, pt.getX());

		time2 = System.currentTimeMillis();
		time ^= time2;

		System.out.printf("%X\n", generateLong());
	}
}
