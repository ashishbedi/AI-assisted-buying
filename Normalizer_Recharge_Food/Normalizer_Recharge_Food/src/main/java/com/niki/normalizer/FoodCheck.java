package com.niki.normalizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class FoodCheck
{
	HashMap<String,String> FoodDictionary=new HashMap<String,String>();

	public HashMap<String,String> BuildDict()throws IOException
	{
		BufferedReader input = new BufferedReader(new FileReader("FoodDictionary.txt"));
		String line = input.readLine();

		System.out.println("Building FoodDictionary...");
		while(line != null)
		{
			FoodDictionary.put(line.toLowerCase(), null);
			line=input.readLine();
		}
		input.close();

		System.out.println("Food Dictionary Built.");
		return FoodDictionary;
	}
	
	public boolean IsFood(String str)
	{
		if(FoodDictionary.containsKey(str.toLowerCase()))
			return true;
		else
		return false;
	}
}
