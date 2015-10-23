package com.niki.normalizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class SpellCheck
{
	HashMap<String,String> Dictionary=new HashMap<String,String>();
	public HashMap<String,String> BuildDict()throws IOException
	{
		BufferedReader input=new BufferedReader(new FileReader("Dictionary183719.txt"));
		String line=input.readLine();

		while(line!=null)
		{
			Dictionary.put(line.toLowerCase(), null);
			line=input.readLine();
		}

		input.close();
		return Dictionary;
	}
	
	public boolean IsinDictionary(String str)
	{
		if(Dictionary.containsKey(str.toLowerCase()))
				return true;
		else
		return false;
	}
}
