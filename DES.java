import java.util.ArrayList;
import java.util.BitSet;
import java.io.File;
import java.awt.*;
import java.io.*;


public class DES {
	public static BitSet initial64 = new BitSet(64);
	public static BitSet key56 = new BitSet(56);
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


	// returns a 64 bit key
	static BitSet initPermutation(BitSet key) {
		BitSet ret = new BitSet();
		byte[] keyArr = key.toByteArray();

		int setIndex;
		for(int i = 0; i < keyArr.length; i ++)
		{
			setIndex = 0;
			// responsible for moving the bits around, uses case fall through
			switch(keyArr[i])
			{
				case 6:  setIndex ++;
				case 14: setIndex ++;
				case 22: setIndex ++;
				case 30: setIndex ++;
				case 38: setIndex ++;
				case 46: setIndex ++;
				case 54: setIndex ++;
				case 62: setIndex ++;
				case 4:  setIndex ++;
				case 12: setIndex ++;
				case 20: setIndex ++;
				case 28: setIndex ++;
				case 36: setIndex ++;
				case 44: setIndex ++;
				case 52: setIndex ++;
				case 60: setIndex ++;
				case 2:  setIndex ++;
				case 10: setIndex ++;
				case 18: setIndex ++;
				case 26: setIndex ++;
				case 34: setIndex ++;
				case 42: setIndex ++;
				case 50: setIndex ++;
				case 58: setIndex ++;
				case 0:  setIndex ++;
				case 8:  setIndex ++;
				case 16: setIndex ++;
				case 24: setIndex ++;
				case 32: setIndex ++;
				case 40: setIndex ++;
				case 48: setIndex ++;
				case 56: setIndex ++;
				case 7:  setIndex ++;
				case 15: setIndex ++;
				case 23: setIndex ++;
				case 31: setIndex ++;
				case 39: setIndex ++;
				case 47: setIndex ++;
				case 55: setIndex ++;
				case 63: setIndex ++;
				case 5:  setIndex ++;
				case 13: setIndex ++;
				case 21: setIndex ++;
				case 29: setIndex ++;
				case 37: setIndex ++;
				case 45: setIndex ++;
				case 53: setIndex ++;
				case 61: setIndex ++;
				case 3:  setIndex ++;
				case 11: setIndex ++;
				case 19: setIndex ++;
				case 27: setIndex ++;
				case 35: setIndex ++;
				case 43: setIndex ++;
				case 51: setIndex ++;
				case 59: setIndex ++;
				case 1:  setIndex ++;
				case 9:  setIndex ++;
				case 17: setIndex ++;
				case 25: setIndex ++;
				case 33: setIndex ++;
				case 41: setIndex ++;
				case 49: setIndex ++;
				case 57: 
					setIndex ++;
					ret.set(setIndex);
					break;

			}
		}

		return ret;
	}

	static BitSet finalPermutation(BitSet key) {
		BitSet ret = new BitSet(64);
		byte[] keyArr = key.toByteArray();

		int setIndex;

		for(int i = 0; i < key.length; i ++)
		{
			setIndex = 0;
			switch(keyArr[i])
			{
				case 25: setIndex++;
				case 57: setIndex++;
				case 17: setIndex++;
				case 49: setIndex++;
				case 9: setIndex++;
				case 41: setIndex++;
				case 1: setIndex++;
				case 33: setIndex++;
				case 26: setIndex++;
				case 58: setIndex++;
				case 18: setIndex++;
				case 50: setIndex++;
				case 10: setIndex++;
				case 42: setIndex++;
				case 2: setIndex++;
				case 34: setIndex++;
				case 27: setIndex++;
				case 59: setIndex++;
				case 19: setIndex++;
				case 51: setIndex++;
				case 11: setIndex++;
				case 43: setIndex++;
				case 3: setIndex++;
				case 35: setIndex++;
				case 28: setIndex++;
				case 60: setIndex++;
				case 20: setIndex++;
				case 52: setIndex++;
				case 12: setIndex++;
				case 44: setIndex++;
				case 4: setIndex++;
				case 36: setIndex++;
				case 29: setIndex++;
				case 61: setIndex++;
				case 21: setIndex++;
				case 53: setIndex++;
				case 13: setIndex++;
				case 45: setIndex++;
				case 5: setIndex++;
				case 37: setIndex++;
				case 30: setIndex++;
				case 62: setIndex++;
				case 22: setIndex++;
				case 54: setIndex++;
				case 14: setIndex++;
				case 46: setIndex++;
				case 6: setIndex++;
				case 38: setIndex++;
				case 31: setIndex++;
				case 63: setIndex++;
				case 23: setIndex++;
				case 55: setIndex++;
				case 15: setIndex++;
				case 47: setIndex++;
				case 7: setIndex++;
				case 39: setIndex++;
				case 32: setIndex++;
				case 64: setIndex++;
				case 24: setIndex++;
				case 56: setIndex++;
				case 16: setIndex++;
				case 48: setIndex++;
				case 8: setIndex++;
				case 40: setIndex++;
					ret.set(setIndex);
					break;
			}
		}
	}

	static BitSet permutatedChoice_1(BitSet key){
		BitSet ret = new BitSet(56);
		byte[] keyArr = key.toByteArray();

		for(int i = 0; i < keyArr.length; i ++)
		{
			switch(keyArr[i]) 
			{
				case 57:
					ret.set(0); break;
				case 49:
					ret.set(1); break;
				case 41:
					ret.set(2); break;
				case 33:
					ret.set(3); break;
				case 25:
					ret.set(4); break;
				case 17:
					ret.set(5); break;
				case 9:
					ret.set(6); break;
				case 1:
					ret.set(7); break;
				case 58:
					ret.set(8); break;
				case 50:
					ret.set(9); break;
				case 42:
					ret.set(10); break;
				case 34:
					ret.set(11); break;
				case 26:
					ret.set(12); break;
				case 18:
					ret.set(13); break;
				case 10:
					ret.set(14); break;
				case 2:
					ret.set(15); break;
				case 59:
					ret.set(16); break;
				case 51:
					ret.set(17); break;
				case 43:
					ret.set(18); break;
				case 35:
					ret.set(19); break;
				case 27:
					ret.set(20); break;
				case 19:
					ret.set(21); break;
				case 11:
					ret.set(22); break;
				case 3:
					ret.set(23); break;
				case 60:
					ret.set(24); break;
				case 52:
					ret.set(25); break;
				case 44:
					ret.set(26); break;
				case 36:
					ret.set(27); break;
				case 63:
					ret.set(28); break;
				case 55:
					ret.set(29); break;
				case 47:
					ret.set(30); break;
				case 39:
					ret.set(31); break;
				case 31:
					ret.set(32); break;
				case 23:
					ret.set(33); break;
				case 15:
					ret.set(34); break;
				case 7:
					ret.set(35); break;
				case 62:
					ret.set(36); break;
				case 54:
					ret.set(37); break;
				case 46:
					ret.set(38); break;
				case 38:
					ret.set(39); break;
				case 30:
					ret.set(40); break;
				case 22:
					ret.set(41); break;
				case 14:
					ret.set(42); break;
				case 6:
					ret.set(43); break;
				case 61:
					ret.set(44); break;
				case 53:
					ret.set(45); break;
				case 45:
					ret.set(46); break;
				case 37:
					ret.set(47); break;
				case 29:
					ret.set(48); break;
				case 21:
					ret.set(49); break;
				case 13:
					ret.set(50); break;
				case 5:
					ret.set(51); break;
				case 28:
					ret.set(52); break;
				case 20:
					ret.set(53); break;
				case 12:
					ret.set(54); break;
				case 4:
					ret.set(55); break;				
			}
		}

		return ret;
	}

	static BitSet permutatedChoice_2(BitSet key) {
		BitSet ret = new BitSet(48);
		byte[] keyArr = key.toByteArray();

		int setIndex;
		for(int i = 0; i < keyArr.length; i ++)
		{
			setIndex = 0;
			switch(keyArr)
			{
				case(32):  setIndex ++;
				case(29):  setIndex ++;
				case(36):  setIndex ++;
				case(50):  setIndex ++;
				case(42):  setIndex ++;
				case(46):  setIndex ++;
				case(53):  setIndex ++;
				case(34):  setIndex ++;
				case(56):  setIndex ++;
				case(39):  setIndex ++;
				case(49):  setIndex ++;
				case(44):  setIndex ++;
				case(48):  setIndex ++;
				case(33):  setIndex ++;
				case(45):  setIndex ++;
				case(51):  setIndex ++;
				case(40):  setIndex ++;
				case(30):  setIndex ++;
				case(55):  setIndex ++;
				case(47):  setIndex ++;
				case(37):  setIndex ++;
				case(31):  setIndex ++;
				case(52):  setIndex ++;
				case(41):  setIndex ++;
				case(2):   setIndex ++;
				case(13):  setIndex ++;
				case(20):  setIndex ++;
				case(27):  setIndex ++;
				case(7):   setIndex ++;
				case(16):  setIndex ++;
				case(8):   setIndex ++;
				case(26):  setIndex ++;
				case(4):   setIndex ++;
				case(12):  setIndex ++;
				case(19):  setIndex ++;
				case(23):  setIndex ++;
				case(10):  setIndex ++;
				case(21):  setIndex ++;
				case(6):   setIndex ++;
				case(15):  setIndex ++;
				case(28):  setIndex ++;
				case(3):   setIndex ++;
				case(5):   setIndex ++;
				case(1):   setIndex ++;
				case(24):  setIndex ++;
				case(11):  setIndex ++;
				case(17):  setIndex ++;
				case(14):  
					setIndex ++;
					ret.set(index);
			}
		}
	}


	static ArrayList<String> getBlocks(FileInputStream fs) {

	}

	static void encrypt(String[] args) throws Exception{
		PrintWriter fw = openOutputFile(args[2]);

		// get blocks from file
		FileInputStream fs = openInputFile(args[1]);
		ArrayList<String> getBlocks = getBlocks(fs);

		// get keys needed
		key56 = initPermutation(initial64);
		BitSet[] keyArray = getAllKeys(key56);

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
	 |	Post-Cond:	the key is shifted, with wrap-around and the splits implemented
	 |
	 |	Parameters:	shift:	the number of indices shifted each half is shifted left
	 |				key:	the BitSet of bits that needs to be split & shifted
	 |
	 |	Returns:	BitSet:	the key, after splits, shifts, and merge post shift
	 |
	 `-------------------------------------------------------------------------------------*/
	static BitSet generateShiftKey(int shift, BitSet key) {
		BitSet left = key.get(0, 27), 
			right = key.get(28,55);

		byte[] leftArr = left.toByteArray();
		byte[] rightArr = right.toByteArray();

		int i;

		// add the shift and implement loop around with the modulo
		for(i = 0; i < leftArr.length; i ++)
			leftArr[i] = (leftArr[i] + shift) % 28;

		// must account for indices being 'shifted' by 28 in the right side;
		for(i = 0; i < rightArr.length; i ++)
			rightArr[i] = ((leftArr[i] - 28 + shift) % 28) + 28;

		return BitSet.valueOf(leftArr).or(BitSet.valueOf(rightArr));
	}

	static BitSet[] getAllKeys(BitSet key) {
		BitSet[] allKeys = new BitSet[16];
		allKeys[0] = key;
		for(int i = 1; i < 16; i ++)
		{
			allKeys[i] = keySchedule(allKeys[i-1]);
		}
		// byte[] combinedKey = new byte[_keyLen];
		// int[] removals = {7, 15, 23, 31, 39, 47, 55, 63};
		// int i, count = 0, j;

		// // retain the proper bytes
		// for(i = 0; i < _keyLen; i ++)
		// {
		// /*	if(i == 7 || i == 15 || i == 23 || i == 31 || i == 39 || 
		// 		i == 47 || i == 55 || i == 63)
		// 		continue;
		// 	else*/
		// 	{
		// 		System.out.print(key.charAt(i));
		// 		combinedKey[count] = (byte) key.charAt(i);
		// 		count ++;
		// 	}
		// }

		// int[] shifts = {1,1,2,2,
		// 		2,2,2,2,
		// 		2,1,2,2,
		// 		2,2,1};

		// byte[][] keyArray = new byte[16][_keyLen];

		// for(i = 0; i < shifts.length; i ++)
		// 	keyArray[0][i] = combinedKey[i];

		// for(i = 1; i < shifts.length; i ++)
		// {
		// 	combinedKey = generateShiftKey(shifts[i], combinedKey);

		// 	for(j = 0; j < _keyLen; j ++)
		// 		keyArray[i][j] = combinedKey[j];
		// }

		// return keyArray;
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

	static void generateInitialKey(String key) {
		initialKey = BitSet.valueOf(new long[] {time});
	}

	/*-------------------------------------------------------------------------------------
	 |	Purpose:	print a DES key
	 |
	 |	Pre-Cond:	MouseInfo is imported
	 |
	 |	Post-Cond:	a DES key (64 bits) was printed to console
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

		if(time < Math.pow(3, 23))
			time += Math.pow(7, pt.getY());
	   	else if (time < Math.pow(3, 31))
			time += Math.pow(11, pt.getX());

		time2 = System.currentTimeMillis();
		time ^= time2;

		System.out.printf("%X\n", time);

		//initialKey = BitSet.valueOf(new long[] {time});
		//System.out.println(initialKey.toString());
		
		System.exit(0);
	}
}
