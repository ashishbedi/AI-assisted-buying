package com.niki.normalizer;

import java.util.*; 
import java.util.Vector;
import  java.lang.* ; 
import java.io.*;   
import java.io.File;
import java.text.*; 
import java.util.regex.*;
import java.util.Scanner;

public class keyboardSuggestions
{
	private static HashMap<Character,List<Character> > keyboardProximity;
	private static HashMap<String,Boolean> Dictionary;
	static
	{
		keyboardProximity = new HashMap<Character,List<Character> > ();
		keyboardProximity.put('a',Arrays.asList('a','s'));
		keyboardProximity.put('b',Arrays.asList('b','v','n'));
		keyboardProximity.put('c',Arrays.asList('x','c','v'));
		keyboardProximity.put('d',Arrays.asList('s','d','f'));
		keyboardProximity.put('e',Arrays.asList('w','e','r'));
		keyboardProximity.put('f',Arrays.asList('d','f','g'));
		keyboardProximity.put('g',Arrays.asList('f','g','h'));
		keyboardProximity.put('h',Arrays.asList('g','h','j'));
		keyboardProximity.put('i',Arrays.asList('u','i','o'));
		keyboardProximity.put('j',Arrays.asList('k','j','h'));
		keyboardProximity.put('k',Arrays.asList('j','k','l'));
		keyboardProximity.put('l',Arrays.asList('k','l'));
		keyboardProximity.put('m',Arrays.asList('n','m'));
		keyboardProximity.put('n',Arrays.asList('b','n','m'));
		keyboardProximity.put('o',Arrays.asList('p','o','i'));
		keyboardProximity.put('p',Arrays.asList('o','p'));
		keyboardProximity.put('q',Arrays.asList('q','w'));
		keyboardProximity.put('r',Arrays.asList('e','r','t'));
		keyboardProximity.put('s',Arrays.asList('a','s','d'));
		keyboardProximity.put('t',Arrays.asList('r','t','y'));
		keyboardProximity.put('u',Arrays.asList('i','u','y'));
		keyboardProximity.put('v',Arrays.asList('c','v','b'));
		keyboardProximity.put('w',Arrays.asList('w','q','e'));
		keyboardProximity.put('x',Arrays.asList('c','x','z'));
		keyboardProximity.put('y',Arrays.asList('t','y','u'));
		keyboardProximity.put('z',Arrays.asList('x','z'));
	}

	public static List<String> suggestions(String word)
	{
		List <String> ret = new ArrayList <String> ();
		ret.add("");
		for (int i = 0;i < word.length();i++)
		{
			Character ch = word.charAt(i);
			List<Character> proximity = keyboardProximity.get(ch);
			List<String> temp = new ArrayList<String> ();
			for (int j = 0;j < ret.size();j++)
			{
				String alreadyPresentInRet = ret.get(j);
				for (int k = 0;k < proximity.size();k++)
				{
					String modified = alreadyPresentInRet + proximity.get(k);
					temp.add(modified);
				}
			}
			ret = temp;
		}
		return ret;
	}

	public static List<String> filterSuggestions(List <String> suggestions)
	{
		List <String> filteredList = new ArrayList<String> ();
		for (int i = 0; i < suggestions.size();i++)
		{
			if (Dictionary.containsKey(suggestions.get(i)))
			{
				filteredList.add(suggestions.get(i));
			}
		}
		return filteredList;
	}

	public static List<String> returnKeyBoardSuggestions(String word)
    {
		 List <String> filteredSuggList=new ArrayList<String>();
        if (word != null )
        {
            word = word.trim();
            List<String> suggList = suggestions(word);
            filteredSuggList = filterSuggestions(suggList);
        }
        return  filteredSuggList;
    }

    public static void createDictionary() throws IOException
    {
        Dictionary = new HashMap <String, Boolean> ();
        BufferedReader input = new BufferedReader(new FileReader("new_big.txt"));

        String line = input.readLine();
        while(line != null)
        {
            String word = line;
            if((word != "") || (word != "(\\s+)"))
            {
                Dictionary.put(word,true);
            }
            line = input.readLine();
        }
 
        input.close();
    } 
}