package com.niki.normalizer;

import java.io.*; 
import java.util.*;

public class SlangCorrection
{
	HashMap<String,List<String>> SlangMap=new HashMap<String,List<String>>();
	public HashMap<String,List<String>> BuildSlangMap()throws IOException
	{
		SlangMap = new HashMap<String,List<String>>();
		BufferedReader input = new BufferedReader(new FileReader("slang.txt"));
		String line=input.readLine();
		
		while(line!=null)
		{
			String []str=line.split("=");
			if(SlangMap.get(str[0])==null)
			{
				List<String> temp=new ArrayList<String>();
				temp.add(str[1].toLowerCase());
				SlangMap.put(str[0].toLowerCase(), temp);
			}
			else
			{
				List<String> temp=SlangMap.get(str[0]);
				temp.add(str[1].toLowerCase());
				SlangMap.put(str[0].toLowerCase(), temp);
			}
			line=input.readLine();
		}
		
		return SlangMap;
	}

	public boolean IsSlang(String str)
	{
		if(SlangMap.get(str.toLowerCase())==null)
			return false;
		else
			return true;
	}
	
	public List SlangSuggestions(String str)
	{
		return SlangMap.get(str.toLowerCase());
	}
}
