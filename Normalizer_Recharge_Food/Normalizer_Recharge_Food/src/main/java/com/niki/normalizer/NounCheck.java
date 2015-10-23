package com.niki.normalizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class NounCheck
{
	HashMap<String,String> NounDictionary=new HashMap<String,String>();
	public HashMap<String,String> BuildDict()throws IOException
	{
		BufferedReader input=new BufferedReader(new FileReader("Noun.txt"));

		String line=input.readLine();

		while(line!=null)
		{
			 NounDictionary.put(line.toLowerCase(), null);
			line=input.readLine();
		}

		input.close();

		return NounDictionary;
	}
	
	public boolean IsNoun(String str)
	{
		if( NounDictionary.containsKey(str.toLowerCase()))
			return true;
		else
			return false;
	}
}
