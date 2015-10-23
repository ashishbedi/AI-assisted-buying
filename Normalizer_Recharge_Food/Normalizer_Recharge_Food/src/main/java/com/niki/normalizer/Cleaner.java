package com.niki.normalizer;

import java.text.Normalizer;  
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Cleaner
{
	public String cleaning1(String str)throws IOException
	{	
		str=str.toLowerCase();
		str=str.replaceAll("\\.+", ".").replaceAll("\\,+",",").replaceAll("\\s+", " ");

		//to normalise "?" type of character for further use
		str=Normalizer.normalize(str, Normalizer.Form.NFD);		

		//to remove "?" type of character
		str=str.replaceAll("[^\\p{ASCII}]", "");

		str=str.replaceAll("\\!+", "!").replaceAll("\\?+", "?").replaceAll("\\@+", "@").replaceAll("\\#+", "#");
		str=str.replaceAll("\\>+", ">").replaceAll("\\<+", "<").replaceAll("\\:+", ":").replaceAll("\\;+", ";");
		str=str.replaceAll("\\=+", "=").replaceAll("\\_+", "_").replaceAll("\\^+", "^").replaceAll("\\(+", "(");
		str=str.replaceAll("\\)+", ")").replaceAll("\\|+", "|").replaceAll("\\}+", "}").replaceAll("\\{+", "{");
		str=str.replaceAll("\\[+", "[").replaceAll("\\]+", "]").replaceAll("\\#+", "#");
		str=str.replaceAll("\\\\\\\\+", "\\\\\\\\");
		str=str.replaceAll("\\++", "+").replaceAll("\\-+", "-").replaceAll("\\*+", "*").replaceAll("\\//+", "//").replaceAll("\\%+", "%").replaceAll("\\^+", "^").replaceAll("\\=+", "=");//airthmetic operation
		str=str.replaceAll("\\$+", "\\$");
		str=str.replaceAll("aa+", "aa");
		str=str.replaceAll("bb+", "bb");
		str=str.replaceAll("cc+","cc");
		str=str.replaceAll("dd+", "dd");
		str=str.replaceAll("ee+", "ee");
		str=str.replaceAll("ff+", "ff");
		str=str.replaceAll("gg+", "gg");
		str=str.replaceAll("hh+", "hh");
		str=str.replaceAll("ii+", "ii");
		str=str.replaceAll("jj+", "jj");
		str=str.replaceAll("kk+", "kk");
		str=str.replaceAll("ll+", "ll");
		str=str.replaceAll("mm+", "mm");
		str=str.replaceAll("nn+", "nn");
		str=str.replaceAll("oo+", "oo");
		str=str.replaceAll("pp+", "pp");
		str=str.replaceAll("qq+", "qq");
		str=str.replaceAll("rr+", "rr");
		str=str.replaceAll("ss+", "ss");
		str=str.replaceAll("tt+", "tt");
		str=str.replaceAll("uu+", "uu");
		str=str.replaceAll("vv+", "vv");
		str=str.replaceAll("ww+", "ww");
		str=str.replaceAll("xx+", "xx");
		str=str.replaceAll("yy+", "yy");
		str=str.replaceAll("zz+", "zz");
		str=str.replaceAll("\\s+", " ");
		str=str.replaceAll("\\s+", " ").trim();
		
		return str;
	}

}
