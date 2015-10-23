package com.niki.normalizer;

import java.io.*;           
import java.util.HashMap;
import java.util.*;
import java.util.Scanner;

public class temp
{
	public static void main(String[] args)throws IOException
	{
		Scanner scan=new Scanner(System.in);
		ModificationClass obj = new ModificationClass();
		while(true)
		{
			String str[]={ };
			obj.socialhash(scan.nextLine(), str);
		}
	}
}