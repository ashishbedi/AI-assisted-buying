package com.niki.normalizer;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

public class LoadN_Gram
{

	HashMap<String,Integer> Data4Gram;
	HashMap<String,Integer> Data3Gram;
	HashMap<String,Integer> Data2Gram;
	HashMap<String,Integer> Data1Gram;
	public HashMap<String,Integer> Load4GramData()throws IOException
	{
		Data4Gram=new HashMap<String,Integer>();
		BufferedReader input=new BufferedReader(new FileReader("w4.txt"));
		String line=input.readLine().toLowerCase();
		
		while(line!=null)
		{
			String StrArray[]=line.split("[\\s+]");
			Data4Gram.put(StrArray[1]+StrArray[2]+StrArray[3]+StrArray[4],Integer.parseInt(StrArray[0]));
			line=input.readLine();
			if(line != null)
				line=line.toLowerCase();
		}

		input.close();

		return Data4Gram;
	}

	public HashMap<String,Integer> Load3GramData()throws IOException
	{
		Data3Gram=new HashMap<String,Integer>();
		BufferedReader input=new BufferedReader(new FileReader("w3.txt"));
		String line=input.readLine().toLowerCase();
		
		while(line!=null)
		{
			String StrArray[]=line.split("[\\s+]");
			Data3Gram.put(StrArray[1]+StrArray[2]+StrArray[3],Integer.parseInt(StrArray[0]));
			line=input.readLine();
			if(line!=null)
				line=line.toLowerCase();
		}

		input.close();

		return Data3Gram;
	}

	public HashMap<String,Integer> Load2GramData()throws IOException
	{
		Data2Gram=new HashMap<String,Integer>();
		BufferedReader input=new BufferedReader(new FileReader("w2.txt"));
		String line=input.readLine().toLowerCase();
		
		while(line!=null)
		{
			String StrArray[]=line.split("[\\s+]");
			Data2Gram.put(StrArray[1]+StrArray[2],Integer.parseInt(StrArray[0]));
			line=input.readLine();
			if(line!=null)
				line=line.toLowerCase();
		}

		input.close();

		return Data2Gram;
	}

	public HashMap<String,Integer> Load1GramData()throws IOException
	{
		Data1Gram=new HashMap<String,Integer>();
		BufferedReader input=new BufferedReader(new FileReader("w1.txt"));
		String line=input.readLine().toLowerCase();
		
		while(line!=null)
		{
			String StrArray[]=line.split("[\\s+]");
			Data1Gram.put(StrArray[0],Integer.parseInt(StrArray[1]));
			line=input.readLine();
			if(line!=null)
				line=line.toLowerCase();

		}

		input.close();

		return Data1Gram;
	}

	public String SuggestionN_Gram(String []Nword,String Nfrontword[],Vector<String> SuggestionsVect)
	{
		HashMap<String,Double> ContextBasedProbability=new HashMap<String,Double>();
		HashMap<String,Double> ContextBasedProbabilityfront=new HashMap<String,Double>();

		if(Nword.length == 0)
		{
			for(int i=0; i<SuggestionsVect.size(); i++)
        	{
        		ContextBasedProbability.put(SuggestionsVect.get(i),Math.log( Suggestion1Gram(SuggestionsVect.get(i).toLowerCase()) ) );
        	}
		}
		else if(Nword.length == 1)
		{
			for(int i=0;i<SuggestionsVect.size();i++)
        	{
				double value=Math.log( Suggestion1Gram(SuggestionsVect.get(i).toLowerCase()) )+Math.log( Suggestion2Gram(Nword[0].toLowerCase()+SuggestionsVect.get(i).toLowerCase()) );
        		ContextBasedProbability.put(SuggestionsVect.get(i),value );
        	}
		}
		else if(Nword.length == 2)
		{
			for(int i=0;i<SuggestionsVect.size();i++)		
        	{	
				double value=Math.log( Suggestion1Gram(SuggestionsVect.get(i)) )+Math.log( Suggestion2Gram(Nword[1].toLowerCase()+SuggestionsVect.get(i).toLowerCase()) )+Math.log( Suggestion3Gram(Nword[0].toLowerCase()+Nword[1].toLowerCase()+SuggestionsVect.get(i).toLowerCase()) );
        		ContextBasedProbability.put(SuggestionsVect.get(i),value );
        	}
		}
		else if(Nword.length == 3)
		{
			for(int i=0;i<SuggestionsVect.size();i++)
        	{
				double value=Math.log( Suggestion1Gram(SuggestionsVect.get(i).toLowerCase()) )+Math.log( Suggestion2Gram(Nword[2].toLowerCase()+SuggestionsVect.get(i).toLowerCase()) )+Math.log( Suggestion3Gram(Nword[1].toLowerCase()+Nword[2].toLowerCase()+SuggestionsVect.get(i).toLowerCase()) )+Math.log( Suggestion4Gram(Nword[0].toLowerCase()+Nword[1].toLowerCase()+Nword[2].toLowerCase()+SuggestionsVect.get(i).toLowerCase()) );
				ContextBasedProbability.put(SuggestionsVect.get(i),value );
        	}
		}

		if(Nfrontword.length==1)
		{
			for(int i=0;i<SuggestionsVect.size();i++)
        	{
				double value=Math.log( Suggestion2Gram(  SuggestionsVect.get(i).toLowerCase()+Nfrontword[0].toLowerCase()) );
        		ContextBasedProbabilityfront.put(SuggestionsVect.get(i),value );
        	}
		}
		else if(Nfrontword.length==2)
		{
			for(int i=0;i<SuggestionsVect.size();i++)//just to print all mindistance suggestions
        	{
				double value=Math.log( Suggestion2Gram(SuggestionsVect.get(i).toLowerCase()+Nfrontword[0].toLowerCase()) )+Math.log( Suggestion3Gram(SuggestionsVect.get(i).toLowerCase()+Nfrontword[0].toLowerCase()+Nfrontword[1].toLowerCase()) );
        		ContextBasedProbabilityfront.put(SuggestionsVect.get(i),value );
        	}
		}
		else if(Nfrontword.length==3)
		{
			for(int i=0;i<SuggestionsVect.size();i++)
        	{
				double value=Math.log( Suggestion2Gram(SuggestionsVect.get(i).toLowerCase()+Nfrontword[0].toLowerCase()) )+Math.log( Suggestion3Gram(SuggestionsVect.get(i).toLowerCase()+Nfrontword[0].toLowerCase()+Nfrontword[1].toLowerCase()) )+Math.log( Suggestion4Gram(SuggestionsVect.get(i).toLowerCase()+Nfrontword[0].toLowerCase()+Nfrontword[1].toLowerCase()+Nfrontword[2].toLowerCase()) );
				ContextBasedProbabilityfront.put(SuggestionsVect.get(i),value );
        	}
		}

		for (HashMap.Entry<String,Double> entry :ContextBasedProbability.entrySet())
		{
			if(ContextBasedProbabilityfront.get(entry.getKey())!=null)
			{
				Double val=(ContextBasedProbabilityfront.get(entry.getKey()))+entry.getValue();
				ContextBasedProbability.put(entry.getKey(),val);
			}
		}

		Set<Entry<String, Double>> set = ContextBasedProbability.entrySet();
	    List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(set);

	    //sort the hashmap
		Collections.sort( list, new Comparator<Map.Entry<String, Double>>()			
		{
			public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
			{
		        return (o2.getValue()).compareTo( o1.getValue() );//to sort in increasing order
		    }
		} );

		String updated = "";
		
		for(Map.Entry<String, Double> entry:list)
        {
        		updated=entry.getKey();
        		break;
        }
		return updated;
	}

	public Double Suggestion4Gram(String str)
	{
		if(Data4Gram.get(str)==null)
		return 1.0;
		else
		{
			if(Data4Gram.get(str)*1.0==0)
				return 1.0;
			else
			return Data4Gram.get(str)*1.0;
		}
	}

	public Double Suggestion3Gram(String str)
	{
		if(Data3Gram.get(str)==null)
		return 1.0;
		else
		{
			if(Data3Gram.get(str)*1.0==0)
			return 1.0;
			else
			return Data3Gram.get(str)*1.0;
		}
	}

	public Double Suggestion2Gram(String str)
	{
		if(Data2Gram.get(str)==null)
		return 1.0;
		else
		{
			if(Data2Gram.get(str)*1.0==0)
			return 1.0;
			else
				return Data2Gram.get(str)*1.0;
		}
	}

	public Double Suggestion1Gram(String str)
	{
		if(Data1Gram.get(str)==null)
		return 1.0;
		else
		{
			if(Data1Gram.get(str)*1.0==0)
				return 1.0;
			else
			return Data1Gram.get(str)*1.0;
		}
	}
}
