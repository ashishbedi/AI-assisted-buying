package com.niki.normalizer;

public class BooleanChecking
{
	public boolean Isurl(String str)
	{
		if(str.toLowerCase().contains("http")||str.toLowerCase().contains("https")||str.toLowerCase().contains("www"))
			return true;
		else if(str.toLowerCase().contains(".com")||str.toLowerCase().contains(".in")||str.toLowerCase().contains(".org"))
			return true;
		else
			return false;
	}
	public boolean Ismail(String str)
	{
		if( (str.toLowerCase().contains("@")) &&( (str.toLowerCase().contains(".com")) || (str.toLowerCase().contains(".in")) ))
			return true;
		else
			return false;
	}
	public boolean SpecialChar(String s,char P)
	{
		if(P=='Y' && P!='X')
		{
			if(s.contains("?") || s.contains("+") || s.contains("-")||s.contains("=") || s.contains(".")||s.contains(","))
				return true;
			else if(s.contains("!") || s.contains("@") || s.contains("#")||s.contains("$") || s.contains("%"))
				return true;
			else if(s.contains("^") || s.contains("&") || s.contains("*")||s.contains("(") || s.contains(")"))
				return true;
			else if(s.contains("_") || s.contains("{") || s.contains("}")||s.contains("|") || s.contains("/"))
				return true;
			else if(s.contains(":") || s.contains(";") || s.contains("'")||s.contains("<") || s.contains(">"))
				return true;
		}
		if(P=='X' && P!='Y')
		{
			if(s.contains("\"")|| s.contains("[") || s.contains("]") || s.contains("\\"))
			{
				return true;
			}
		}
		return false;
	}
}
