import java.awt.*;
import java.math.BigInteger;
import java.util.Random;
import java.io.*;

public class RSA {
	public static void main(String []args) {
		if(args.length < 1)
			exit();
		else if(args.length == 1 && args[0].equals("-h"))
		{
			System.out.println("Usage:\njava RSA -h\njava RSA -k <key-file> -b <bit-size>");
			System.out.println("java RSA -e <key-file>.public -i <input-file> -o <output-file>");
			System.out.println("java RSA -d <key-file>.private -i <input-file> -o <output-file>");
			System.exit(0);
		}

		else if(args.length <= 4)
		{
			String[] arguments = {null, "1024"};
			int i;
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

		} /*else if(args.length == 6)
		{
			boolean encrypt = false;
			int i;
			String[] arguments = {null, null, null};
			for(i = 0; i < 6; i += 2)
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
*/
		else exit();
	}

	static void exit() {
		System.out.println("Type 'java RSA -h' for help\n");
		System.exit(1);
	}
/*
	static void encrypt(String[] args) {

	}

	static void decrypt(String[] args) {

	}
*/
	static long generateLong() {
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

		return time ^ System.currentTimeMillis();
	}

	static BigInteger calculate_d(BigInteger key, BigInteger phi) {
		BigInteger left = BigInteger.ONE, nextLeft = BigInteger.ONE, right = phi, 
				nextRight = key, quotient;
		System.out.printf("%x\n", key);

		while(!nextRight.equals(BigInteger.ZERO))
		{
			quotient = right.divide(nextRight);
			right = nextRight;
			nextRight = right.subtract(quotient.multiply(nextRight));
			left = nextLeft;
			nextLeft = left.subtract(quotient.multiply(nextLeft));
		}

		return left.divide(right);
	}

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

	//generate a public and private key, using the filenames in args
	static void generateKey(String []args) {
		Random rnd = new Random(generateLong());
		BigInteger public_key = BigInteger.probablePrime(Integer.parseInt(args[1]), rnd),
		possiblePrime_q = BigInteger.probablePrime(Integer.parseInt(args[1]), rnd),
		totient, private_key, possiblePrime_p = public_key.nextProbablePrime();
	
		totient = (possiblePrime_p.subtract(BigInteger.ONE)).
				multiply(possiblePrime_q.subtract(BigInteger.ONE));

		// calculate the public key

		// calculate the private key
		private_key = public_key.modInverse(totient);

		// write them both to file
		String pubf = args[0].concat(".public");
		String privf = args[0].concat(".private");
		PrintWriter pub_fw = openOutputFile(pubf);
		PrintWriter priv_fw = openOutputFile(privf);

		pub_fw.write(public_key.toString(16));
		priv_fw.write(private_key.toString(16));

		pub_fw.close();
		priv_fw.close();
	}
}