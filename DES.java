import java.util.ArrayList;
import java.util.BitSet;
import java.io.File;
import java.awt.*;
import java.io.*;


public class DES {
	public static BitSet initial64 = new BitSet(64);
	public static BitSet key56 = new BitSet(56);
	static int _keyLen = 16;
	BitSet[] allKeys = new BitSet[16];

	static int box1[][] = new int[][]{
		{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
		{0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
		{4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
		{15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}
	 };

	 static int box2[][] = new int[][] {
		{15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
		{3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
		{0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
		{13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9}
	 };

	 static int box3[][] = new int[][] {
		{10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},
		{13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},
		{13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},
		{1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12}
	 };

	 static int box4[][] = new int[][] {
		{7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
		{13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
		{10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
		{3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14}
	 };

	 static int box5[][] = new int[][] {
		{2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},
		{14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6},
		{4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14},
		{11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3}
	 };

	 static int box6[][] = new int[][] {
		{12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},
		{10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},
		{9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6},
		{4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13}
	 };

	 static int box7[][] = new int[][] {
		{4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},
		{13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},
		{1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},
		{6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12}
	 };

 	 static int box8[][] = new int[][] {
		{13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},
		{1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2},
		{7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8},
		{2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}
 	};

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
	static BitSet initPermutation(BitSet key64) {
		BitSet ret = new BitSet();
		byte[] keyArr = key64.toByteArray();

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

	// returns a 48 bit key
	static BitSet expansionPermutation(BitSet key36) {
		BitSet ret = new BitSet(64);
		byte[] keyArr = key36.toByteArray();

		for(int i = 0; i < key.length; i ++)
		{
			switch(keyArr[i])
			{
				case 0: ret.set(1); ret.set(47); break;
				case 1: ret.set(2); break;
				case 2: ret.set(3); break;
				case 3: ret.set(4); ret.set(6); break;
				case 4: ret.set(5); ret.set(7); break;
				case 5: ret.set(8); break;
				case 6: ret.set(9); break;
				case 7: ret.set(10); ret.set(12); break;
				case 8: ret.set(11); ret.set(13); break;
				case 9: ret.set(14); break;
				case 10: ret.set(15); break;
				case 11: ret.set(16); ret.set(18); break;
				case 12: ret.set(17); ret.set(19); break;
				case 13: ret.set(20); break;
				case 14: ret.set(21); break;
				case 15: ret.set(22); ret.set(24); break;
				case 16: ret.set(23); ret.set(25); break;
				case 17: ret.set(26); break;
				case 18: ret.set(27); break;
				case 19: ret.set(28); ret.set(30); break;
				case 20: ret.set(29); ret.set(31); break;
				case 21: ret.set(32); break;
				case 22: ret.set(33); break;
				case 23: ret.set(34); ret.set(36); break;
				case 24: ret.set(35); ret.set(37); break;
				case 25: ret.set(38);
				case 26: ret.set(39);
				case 27: ret.set(40); ret.set(42); break;
				case 28: ret.set(41); ret.set(43); break;
				case 29: ret.set(44); break;
				case 30: ret.set(45); break;
				case 31: ret.set(46); ret.set(0); break;
			}
		}
	}

	static int[] getIndices(BitSet lookUp) {
		int[] ret = new int[] {0, 0};
	
		if(lookUp.get(5) == true)
			ret[0] += 2;
		if(lookUp.get(0) == true)
			ret[0] += 1;

		if(lookUp.get(1) == true)
			ret[1] += 1;
		if(lookUp.get(2) == true)
			ret[1] += 2;
		if(lookUp.get(3) == true)
			ret[1] += 4;

		return ret;
	}

	static int sBoxLookUp(int box, BitSet lookUp) {
		int[] indices = getIndices(lookUp);
		if(box == 1)
			return sBox1[indices[0]][indices[1]];
		else if(box == 2)
			return sBox2[indices[0]][indices[1]];
		else if(box == 3)
			return sBox3[indices[0]][indices[1]];
		else if(box == 4)
			return sBox4[indices[0]][indices[1]];
		else if(box == 5)
			return sBox5[indices[0]][indices[1]];
		else if(box == 6)
			return sBox6[indices[0]][indices[1]];
		else if(box == 7)
			return sBox7[indices[0]][indices[1]];
		else
			return sBox8[indices[0]][indices[1]];
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
				case 40:
					ret.set(setIndex);
					break;
			}
		}
	}

	// returns a 56 bit key
	static BitSet permutatedChoice_1(BitSet key64){
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

	static BitSet permutatedChoice_2(BitSet key56) {
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

<<<<<<< HEAD
	/*-------------------------------------------------------------------------------------
	 |	Purpose:	this iterates through the textfile, getting all the proper sized
	 |				blocks. 
	 |
	 |	Pre-Cond:	the file exists, is a valid format, and that a command line file was 
	 |				provided
	 |
	 |	Post-Cond:	The message is put into properly sized blocks, with consideration for
	 |				the last one, padding being placed at the end of the block
	 |
	 |	Parameters:	fs:			the FileInputStream that is being read from, byte by byte
	 |
	 |	Returns:	ArrayList:	the ascii Strings that are read in
	 |
	 `-------------------------------------------------------------------------------------*/

	static ArrayList<String> getBlocks(FileInputStream fs) {
		ArrayList<String> blocks = new ArrayList<String>();
		int listIndex = -1, strIndex = 0;
		char next;

		// this adds the proper blocks to the arraylist
		try 
		{
			while((next = (char)fs.read()) != -1)
			{
				if(strIndex % ((_keyLen) - 1) == 0)
				{
					listIndex += 1;
					blocks.add("");
					strIndex = 0;
				}
				blocks.set(listIndex, blocks.get(listIndex) + next);
				strIndex ++;
			}
		} catch(IOException e) {};

		// padding on the last block
		if(strIndex != 0)
			while(strIndex < (_keyLen))
			{
				blocks.set(listIndex, blocks.get(listIndex) + '0');
				strIndex ++;
			}

		return blocks;
=======
	static BitSet pBoxPermutation(BitSet key56) {
		BitSet ret = new BitSet(56);
		byte[] keyArr = key.toByteArray();

		int setIndex;
		for(int i = 0; i < keyArr.length; i ++)
		{
			setIndex = 0;
			switch(keyArr[i]) {
				case 24: setIndex ++;
				case 3: setIndex ++;
				case 10: setIndex ++;
				case 21: setIndex ++;
				case 5: setIndex ++;
				case 29: setIndex ++;
				case 12: setIndex ++;
				case 18: setIndex ++;
				case 8: setIndex ++;
				case 2: setIndex ++;
				case 26: setIndex ++;
				case 31: setIndex ++;
				case 13: setIndex ++;
				case 23: setIndex ++;
				case 7: setIndex ++;
				case 1: setIndex ++;
				case 9: setIndex ++;
				case 30: setIndex ++;
				case 17: setIndex ++;
				case 4: setIndex ++;
				case 25: setIndex ++;
				case 22: setIndex ++;
				case 14: setIndex ++;
				case 0: setIndex ++;
				case 16: setIndex ++;
				case 27: setIndex ++;
				case 11: setIndex ++;
				case 28: setIndex ++;
				case 20: setIndex ++;
				case 19: setIndex ++;
				case 6: setIndex ++;
				case 15: 
					ret.set(setIndex);
					break;
			}
		}

		return ret;
	}

	static BitSet[] getAllKeys(BitSet key64) {
		BitSet[] keys = new BitSet[16];
		BitSet stage1, stage2, left, right;
		keys[0] = key56;

		int[] shifts = new int[] {
			1,1,2,2,
			2,2,2,2
			1,2,2,2
			2,2,2,1 };
		for(int i = 1; i < 16; i ++)
		{
			stage1 = permutatedChoice_1(keys[i-1]);
<<<<<<< HEAD
			left = generateShiftKey(shifts[i], stage1.set(0, 27));
			right = generateShiftKey(shifts[i], stage1.set(28,55));
=======
			left = shift(shifts[i], stage1.set(0, 27));
			right = shift(shifts[i], stage1.set(28,55));
>>>>>>> FETCH_HEAD
>>>>>>> FETCH_HEAD

			keys[i] = permutatedChoice2(left.xor(right));
		}
	}

	/*-------------------------------------------------------------------------------------
	 |	Purpose:	Performs/readies encryption steps. 
	 |
	 |	Pre-Cond:	the file exists, is a valid format, and that a command line file was 
	 |				provided
	 |
	 |	Post-Cond:	Message was properly encrypted following encryption steps.
	 |
	 |	Parameters:	args:	Input and output files for message and encrypted message.
	 |
	 |	Returns:	void.
	 |
	 `-------------------------------------------------------------------------------------*/

	static void encrypt(String[] args) throws Exception{
		PrintWriter fw = openOutputFile(args[2]);

		// get keys needed
		initial64 = BitSet.valueOf(new long[] {Long.parseLong(args[0])});
		key56 = initPermutation(initial64);
		allKeys = getAllKeys(key56);

		// get messageBlocks
		FileInputStream fs = openInputFile(args[1]);
		ArrayList<String> blocks = getBlocks(fs);

		// apply the perumtation functions on each block, with each key and write
		for(String msg : blocks)
			fw.write(fiestelFlow(msg));

		fw.close();
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

	static BitSet fiestelFn(BitSet right32, BitSet key_i) {
		BitSet ret32 = new BitSet(32);

		// expands the 32 bit key into 48 bits
		BitSet expansion = expansionPermutation(right32);

<<<<<<< HEAD
		String temp = blockList.get(index);
		count = count % _keyLen;
		System.out.println(count);
		while(count < _keyLen && count != 0) //padding
		{
			temp = "0" + temp;
			count++;
		}
=======
		// xors the expansion with the 48 bit key
		BitSet sBoxFeed = key_i.xor(expansion);
		long lookup;
>>>>>>> FETCH_HEAD

		// perform S-Box substitution
		for(i = 0; i < 8; i ++)
		{
			lookup = (long) sBoxLookUp(sBoxFeed.set(i * 6, (i+1) * 6), i) << (4*i);
			ret32.valueOf(new long[] {lookup});
		}

		// returns the pBox permutation of the S-Box substitution
		return pBoxPermutation(ret32);
	}

	static String fiestelFlow(String text) {
		BitSet[][] fiestelRound = new BitSet[16][],
			textBits = BitSet.valueOf(text.toByteArray()),
			left32 = new BitSet(32) right32 = new BitSet(32);

		fiestelRound[0][0] = left;
		fiestelRound[0][1] = right;


		for(int i = 1; i < 16; i ++)
		{
			left32 = fiestelRound[i-1][0];
			right32 = fiestelRound[i-1][1];

			fiestelRound[i][0] = right32;
			fiestelRound[i][1] = fiestelFn(right32, allKeys[i]);
		}

		return fiestelRound[15][1].or(fiestelRound[15][0]);
	}

	public static void decrypt(String[] args) throws Exception {
		FileInputStream fs = openInputFile(args[1]);
		PrintWriter fw = openOutputFile(args[2]);


		// get blocks from file
		FileInputStream fs = openInputFile(args[1]);
		ArrayList<String> blocks = getBlocks(fs);
		fs.close();

		// get keys needed
		initial64 = BitSet.valueOf(new long[] {Long.parseLong(key)});
		key56 = finalPermutation(initial64);
		BitSet[] keyArray = getAllKeys(key56);

		decryptOperations(blocks, fw, keyArray);
		fw.close();
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
