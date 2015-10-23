package com.niki.normalizer;

import java.util.ArrayList;
import java.util.List;
//	Y={   -_+=:;'}{|?/.,><!@#$%^&*()   }
//	X={   "\[]  }

public class ModificationClass
{
	BooleanChecking bool=new BooleanChecking();

	public String socialhash(String str,String []punctype)//remove parameters HashSlangList []hashtable,int haslength,int HashKey
	{
		str=str.toLowerCase().trim();
		
		String str11=str.replaceAll("[^A-Za-z0-9]"," ").trim();
		String str1[]=str11.split("\\s+");
		String str22=str.replaceAll("[A-Za-z0-9]","a").replaceAll("a+","a");
		
		str22=str22.replaceAll("\\s+","s");
		str22=str22.replaceAll("a"," ").trim();
		str22=str22.replaceAll(" ","a").replaceAll("s"," ");
		
		String str2[]=str22.split("a");
		
		int length=0;
		
		if(str1.length>str2.length)
			length=str1.length;
		else
			length=str2.length;
			
		String check=""+str.charAt(0);
		
		int checked=0;
		
		if(check.matches("[a-zA-Z0-9]+"))
		{
			checked = 1;
		}
		else
		{
			// do nothing
		}

		List<String> Arraystr=new ArrayList<String>();
		if(checked==1)
		{
			for(int i=0;i<length;i++)
			{
				if(i<str1.length)
				{
					System.out.print(str1[i]+"_");
					Arraystr.add(str1[i]);
				}
				if(i<str2.length)
				{
					System.out.print(str2[i]+"_");
					String str2temp[]=str2[i].split("");
					for(int k=0;k<str2temp.length;k++)
					{
						Arraystr.add(str2temp[k]);
					}
				}
			}
			
		}
		else
		{
			for(int i=0;i<length;i++)
			{
				if(i<str2.length)
				{
					System.out.print(str2[i]+"_");
					String str2temp[]=str2[i].split("");
					for(int k=0;k<str2temp.length;k++)
					{
						Arraystr.add(str2temp[k]);
					}
				}
				if(i<str1.length)
				{
					System.out.print(str1[i]+"_");
					Arraystr.add(str1[i]);
				}
			}
		}
		List<String> Updatedstr = new ArrayList<String >(); 
		int count = 0;
		return str;
	}
}
