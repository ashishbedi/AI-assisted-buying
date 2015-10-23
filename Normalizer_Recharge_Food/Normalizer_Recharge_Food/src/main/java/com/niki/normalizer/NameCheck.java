package com.niki.normalizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class NameCheck
{
	HashMap<String,String> NameDictionary=new HashMap<String,String>();
	public HashMap<String,String> BuildDict()throws IOException
	{
		BufferedReader input=new BufferedReader(new FileReader("Names.txt"));
		String line=input.readLine();

		while(line!=null)
		{
			NameDictionary.put(line.toLowerCase(), null);
			line=input.readLine();
		}

		input.close();

		return NameDictionary;
	}
	
	public boolean IsName(String str)
	{
		if( NameDictionary.containsKey(str.toLowerCase()))
			return true;
		else
			return false;
	}
}
