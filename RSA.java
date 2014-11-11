import java.lang.StringBuilder;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Base64.Encoder;
import java.awt.*;
import java.io.*;

public class RSA {
	private static final BigInteger e = new BigInteger("65537");		

	// check arguments, but straightforward otherwise
	public static void main(String []args) {
		int i;
		if(args.length < 1)
			exit();
		else if(args.length == 1 && args[0].equals("-h"))
		{
			System.out.println("Usage:\njava RSA -h\njava RSA -k <key-file> -b <bit-size>");
			System.out.println("java RSA -e <key-file>.public -i <input-file> -o <output-file>");
			System.out.println("java RSA -d <key-file>.private -i <input-file> -o <output-file>");
			System.exit(0);
		}

		else if(args.length == 4 || args.length == 2)
		{
			String[] arguments = {null, "1024"};
			for(i = 0; i < args.length; i += 2)
			{
				if(args[i].equals("-k"))
					arguments[0] = args[i+1];
				else if(args[i].equals("-b"))
					arguments[1] = args[i+1];
			}
			
			if(arguments[0] == null)
				exit();
			else generateKey(arguments);

		} else if(args.length == 6)
		{
			boolean encrypt = false;
			String[] arguments = {null, null, null};
			for(i = 0; i < 6; i += 2)
			{
				if(args[i].equals("-e"))
				{
					encrypt = true;
					arguments[0] = args[i+1];
				} else if(args[i].equals("-d"))
					arguments[0] = args[i+1];
				 else if(args[i].equals("-i"))
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

	// method header later
	static void exit() {
		System.out.println("Type 'java RSA -h' for help\n");
		System.exit(1);
	}

	/*
	 * msg = M (encryption) or C (decryption)
	 * modulus = modulus from the totient
	 * exp = e (encryption, global) or d (decryption, private key)
	 */
	static String cipher(String msg, BigInteger modulus, BigInteger exp) {
		System.out.println(msg);
		BigInteger convertedMsg = new BigInteger(msg);
		String ret = convertedMsg.modPow(modulus, exp).toString();
		System.out.println(ret);
		return ret;
	}

	static ArrayList<String> getPayloads(FileInputStream fs, int keyLength) {
		ArrayList<String> payload = new ArrayList<String>();
		int listIndex = -1, strIndex = 0;
		char next;

		// this adds the proper payloads to the arraylist
		try 
		{
			while((next = (char)fs.read()) != 0xffff)
			{
				// keyLength accounts for hex(4 bits), but you are now reading chars(8 bits)
				if(strIndex % (keyLength / 2) == 0)
				{
					listIndex += 1;
					payload.add("");
				}
				payload.set(listIndex, payload.get(listIndex) + next);
				strIndex ++;
			}
		} catch(IOException e) {};

		System.out.println("hello");
		// padding on the last payload
		if(strIndex > 0)
			while(strIndex < (keyLength / 2))
			{
				payload.set(listIndex, payload.get(listIndex) + '0');
				strIndex ++;
			}
		return payload;
	}

	// fairly straightforward, method header later
	static String readKey(FileInputStream fs) {
		StringBuilder keyBuilder = new StringBuilder();
		char next;

		try 
		{
			while((next = (char) fs.read()) != 0xffff)
				keyBuilder.append(next);
			
		} catch(IOException e) {};
		System.out.println(keyBuilder.toString());
		return keyBuilder.toString();
	}

	// commented inline, method header later
	static void encrypt(String[] args) {
		// gets the key into a BigInteger
		FileInputStream pubKey_fs = openInputFile(args[0].concat(".public"));
		String key = readKey(pubKey_fs);
		BigInteger bigKey = new BigInteger(key, 16);

		System.out.println("hi");
		// gets all the payloads into an ArrayList
		FileInputStream input_fs = openInputFile(args[1]);
		ArrayList<String> payload = getPayloads(input_fs, key.length());

		// writes the encoded text into the output file
		PrintWriter output_fw = openOutputFile(args[2]);
		for(String msg : payload)
			output_fw.write(cipher(msg, bigKey, e));

		output_fw.close();
	}

	static void decrypt(String[] args) {
		// get the d from private key 
		FileInputStream privKey_fs = openInputFile(args[0].concat(".private"));
		String key = readKey(privKey_fs);
		BigInteger bigKey = new BigInteger(key, 16);

		// get the n from public key
		FileInputStream pubKey_fs = openInputFile(args[0].concat(".public"));
		String pubKey = readKey(pubKey_fs);
		BigInteger n = new BigInteger(pubKey, 16);

		// gets all the payloads into an ArrayList
		FileInputStream input_fs = openInputFile(args[1]);
		ArrayList<String> payload = getPayloads(input_fs, key.length());

		// writes the encoded text into the output file
		PrintWriter output_fw = openOutputFile(args[2]);
		for(String msg : payload)
			output_fw.write(cipher(msg, bigKey, n));
	}

	// uses randomness from system and oddity
	static long generateLong() {
		long time = System.currentTimeMillis(), point, time2;
		Point pt = MouseInfo.getPointerInfo().getLocation();
		point = (long) (pt.getY() * (1 + pt.getY()));
		time *= time ^ point;

		if(time < Math.pow(3, 23))
			time += Math.pow(7, pt.getY());
	   	else if (time < Math.pow(3, 31))
			time += Math.pow(11, pt.getX());

		return time ^ System.currentTimeMillis();
	}

	// straightforward, make method header later
	static FileInputStream openInputFile(String filename) {
		FileInputStream fs;

		try 
		{
			fs = new FileInputStream(new File(filename));
			return fs;
		} catch (IOException e)
		{
			System.out.println("Failed to open input file");
			exit();
		}
		return null;
	}

	// straightforward, make method header later
	static PrintWriter openOutputFile(String filename) {
		PrintWriter fw;

		try 
		{
			fw = new PrintWriter(filename, "UTF-8");
			return fw;

		} catch (Exception e)
		{
			System.out.println("Failed to open output file");
			exit();
		}
		return null;
	}

	// generate a public and private key, using the filenames in args
	static void generateKey(String[] args) {
		Random rnd = new Random(generateLong());
		BigInteger public_key, totient, private_key,
		possiblePrime_q = BigInteger.probablePrime(Integer.parseInt(args[1]) / 2, rnd),
		possiblePrime_p = BigInteger.probablePrime(Integer.parseInt(args[1]) / 2, rnd);

		// file set up
		String pubf = args[0].concat(".public");
		String privf = args[0].concat(".private");
		PrintWriter pub_fw = openOutputFile(pubf);
		PrintWriter priv_fw = openOutputFile(privf);

		// calculate public key
		public_key = possiblePrime_q.multiply(possiblePrime_p);

		// calculate the private key
		totient = public_key.subtract(possiblePrime_p).add(possiblePrime_q).subtract(BigInteger.ONE);
		private_key = public_key.modInverse(totient);

		// write them both to file and close
		pub_fw.write(public_key.toString(16));
		priv_fw.write(private_key.toString(16));
		pub_fw.close();
		priv_fw.close();
	}
}