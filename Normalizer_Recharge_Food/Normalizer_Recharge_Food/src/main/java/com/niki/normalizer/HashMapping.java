package com.niki.normalizer;

import java.io.IOException;    

import java.util.Vector;
import java.util.*;

// Y = {   -_+=:;'}{|?/.,><!@#$%^&*()   }
// X = {   "\[]  }

public class HashMapping
{
    SpellCheck objSpell = new SpellCheck();
    NameCheck objName = new NameCheck();
    LoadN_Gram LoadN_Gramobj = new LoadN_Gram();
    NounCheck objNoun = new NounCheck();
    Metaphone objMetaphone = new Metaphone();
    keyboardSuggestions objKeyboardSuggestions = new keyboardSuggestions();
    SlangCorrection objSlang = new SlangCorrection();
    
    public HashMapping()throws IOException
    {
        objSpell.BuildDict();    //to build the Dictionary
        objName.BuildDict();    //to build the Namelist

        LoadN_Gramobj.Load4GramData();
        LoadN_Gramobj.Load3GramData();
        LoadN_Gramobj.Load2GramData();
        LoadN_Gramobj.Load1GramData();

        SpellCheckIncKeyBoard.CreateDictionary();

        objNoun.BuildDict();
        System.out.println("metaphone dictionary...");
        Metaphone.createPhoneticCodeDictionary();
        System.out.println("keyboard dictionary...");
        keyboardSuggestions.createDictionary();
        objSlang.BuildSlangMap();
    }

    public String socialhash(String str,String []punctype)    
    {
        BooleanChecking bool=new BooleanChecking();

        //split the words according to the punctype
        
        char DetectSpecialChar = 'N';        //whether space or Y set or X set
        String [] words={};
        if((punctype.length == 1) && (punctype[0] == " "))        //split with the spaces
        {
            words=str.split("[ ]");
            DetectSpecialChar=' ';
        }
        else if(bool.SpecialChar(str, 'Y'))        
        {
            DetectSpecialChar='Y';
            words=str.split("#");
        }
        else if(bool.SpecialChar(str, 'X'))
        {
            DetectSpecialChar='X';
            words=str.split("\"");
        }

        String newstr="";        //to store the changing and concating the all words and return
        int handY = 0;
		boolean boolvalue=false;
        for(int i=0; i<words.length; i++)        
        {    
			boolvalue=false;
            //collecting back and forth words

            //String []Tempword=new String[3];
            String []TempFrontword=new String[2];
            int countf = 0;
            for(int j=i+1; ((countf < 2) && (j < words.length)); j++)
            {
                if(bool.SpecialChar(words[j],'Y') || bool.SpecialChar(words[j], 'X') ||  !objSpell.IsinDictionary(words[j]))
                    break;
                else
                {
                    TempFrontword[countf]=words[j];
                    countf++;
                }
            }
            String []Nfrontword = new String[countf];

            for(int j=0; j<countf; j++)
            {
                Nfrontword[j] = TempFrontword[j];
            }
            
/*          int count = 0;
            for(int j=i-1; ((j>=0) && (count<3)); j--)
            {
                if(bool.SpecialChar(words[j],'Y') || bool.SpecialChar(words[j], 'X'))
                    break;
                else
                {
                    Tempword[count]=words[j];
                    count++;
                }
            }
            String []Nword = new String[count];

            for(int j=0; j<count; j++)
            {
                Nword[j] = Tempword[count-1-j];
            }
            								*/
            String []Nword={};
            newstr=newstr.replaceAll("\\s+", " ");
            //System.out.println(i+" newstr :"+newstr.replaceAll(" ", "_"));
            if(i>=1)
            {
            	Nword=findNword(newstr);
            	System.out.print("nbackword of "+ words[i]+" : ");
            	for(String v:Nword)
            		System.out.print(v+" , " );
            	System.out.println();
            	//words[i-1]=find1word(newstr);
            	//System.out.println(words[i-1]+" = "+find1word(newstr));
            }
            
            if(bool.Isurl(words[i])|| bool.Ismail(words[i]))
            {
                //do nothing
            }
            else if(words[i].matches("[0-9]+"))
            {
                //do nothing it will be digits and numerical value only
            }
            else if(objSlang.IsSlang(words[i]))//slang word found process it further
            {
                if(i>0 && objNoun.IsNoun(words[i-1]+words[i]))
                {
                    //do nothing
					boolvalue=true;
                }
                else if((i+1<words.length) && objNoun.IsNoun(words[i]+words[i+1]))
                {
                    words[i+1] = words[i]+words[i+1];
                    words[i] = "";
                }
                else if((i+1<words.length) && (i>0) && (objNoun.IsNoun(words[i-1]+words[i]+words[i+1])))
                {
					boolvalue=true;
                    words[i+1] = words[i]+words[i+1];
                    words[i] = "";
                }
                else
                {
                    List<String> slangList = objSlang.SlangSuggestions(words[i]);
                    if(slangList.size() == 1)
                        words[i]=slangList.get(0);
                    else
                    {
                        Vector<String> SuggestionsFromSlang=new Vector<String>(slangList);

                        //    System.out.println("Suggestions from Slang is: "+SuggestionsFromSlang);
                        String updated=LoadN_Gramobj.SuggestionN_Gram(Nword,Nfrontword,SuggestionsFromSlang);
                        //    System.out.println(Nword.length+1 +"gram and corrected word of "+words[i]+" = "+updated);
                        words[i]=updated;    
                    }
                }
            }
            else if(words[i].matches("[a-zA-Z]+") && objSpell.IsinDictionary(words[i]))
            {
                //do nothing
				if(i>0 && objNoun.IsNoun(words[i-1]+words[i]))
                {
                    //do nothing
					boolvalue=true;
                }
                else if((i+1<words.length) && objNoun.IsNoun(words[i]+words[i+1]))
                {
                    words[i+1] = words[i]+words[i+1];
                    words[i] = "";
                }
                else if((i+1<words.length) && (i>0) && (objNoun.IsNoun(words[i-1]+words[i]+words[i+1])))
                {
					boolvalue=true;
                    words[i+1] = words[i]+words[i+1];
                    words[i] = "";
                }
            }
            else if(words[i].matches("[a-zA-Z]+") && ( objName.IsName(words[i]) || objNoun.IsNoun(words[i])  ) )
            {
                //do nothing
            }
            else if(i>0 && objNoun.IsNoun(words[i-1]+words[i]))
            {
                //do nothing
				boolvalue=true;
            }
            else if(i+1<words.length && (objNoun.IsNoun(words[i]+words[i+1])))
            {
                words[i+1] = words[i]+words[i+1];
                words[i] = "";
            }
            else if((i+1<words.length) && (i>0) && (objNoun.IsNoun(words[i-1]+words[i]+words[i+1])))
            {
                words[i+1]=words[i]+words[i+1];
                words[i]="";
				boolvalue=true;
            }
            else if(bool.SpecialChar(words[i],'Y'))        
            {
                String temp=words[i].replaceAll("[-_+=:;'}{|?/.,><!@#$%^&*()]","#");
                temp = temp.replaceAll("\\#+","#");
                String nnstr = "";
                for(int k=0; k<words[i].length(); k++)
                {
                    if(bool.SpecialChar(Character.toString(words[i].charAt(k)), 'Y'))
                        nnstr=nnstr+Character.toString(words[i].charAt(k));
                    else
                        nnstr=nnstr+" ";
                }
                nnstr=nnstr.replaceAll("\\s+"," ");
                String []npunctype=nnstr.split(" ");
                words[i]=socialhash(temp,npunctype);        
            }
            else if(bool.SpecialChar(words[i], 'X'))
            {
                String temp = words[i].replaceAll("\\[+", "\"").replaceAll("\\]+","\"").replaceAll("\\\\+","\"").replaceAll("\"+", "\"");
                String nnstr = "";
                for(int k=0; k<words[i].length(); k++)
                {
                    if(bool.SpecialChar(Character.toString(words[i].charAt(k)), 'X'))
                        nnstr=nnstr+Character.toString(words[i].charAt(k));
                    else
                        nnstr=nnstr+" ";
                }
                nnstr = nnstr.replaceAll("\\s+"," ");
                String []npunctype = nnstr.split(" ");
                words[i]=socialhash(temp,npunctype);
            }
            else if(words[i].matches("[a-zA-Z]+") )
            {
                int k1 = 0;
                if(k1==0)
                {            
                    Vector<String> SuggestionsFromDict = new Vector<String>(SpellCheckIncKeyBoard.Correct(words[i]));
                    Vector<String> SuggestionsFromKey = new Vector<String>(keyboardCorrector.solve(words[i],SpellCheckIncKeyBoard.Correct(words[i])));
                    Vector<String> SuggestionsFromkeyboard = new Vector<String>(keyboardSuggestions.returnKeyBoardSuggestions(words[i]));
                    Vector<String> SuggestionsFromMetaphone = new Vector<String>(Metaphone.returnSuggestions(words[i]));
                    
                    //    For debugging purpose 

/*                    System.out.println("Suggestions from Dictionary is: "+ SuggestionsFromDict);
                    System.out.println("Suggestions from Key is: "+ SuggestionsFromKey);
                    System.out.println("Suggestions from Keyboard is: "+ SuggestionsFromkeyboard);
                    System.out.println("Suggestions from Metaphone is: "+ SuggestionsFromMetaphone); */
                    
                    SuggestionsFromKey.addAll(SuggestionsFromkeyboard);
                    SuggestionsFromKey.addAll(SuggestionsFromMetaphone);
                    
                    if(SuggestionsFromKey.size()==0)
                    {
                        SuggestionsFromKey.addAll(SuggestionsFromDict);
                    }

                    if(SuggestionsFromKey.size()!=0)
                    {
                        String updated = LoadN_Gramobj.SuggestionN_Gram(Nword,Nfrontword,SuggestionsFromKey);
                        words[i]=updated;
                    }
                    else
                    {
                        // For debugging purpose 
                        System.out.println("Unable to get any suggestions for : "+ words[i] + " from the dictionary!");
                    }
                }    
            }
            
            if((DetectSpecialChar == ' ') && (i<words.length-1))
			{
				if(boolvalue==false)
                newstr = newstr + words[i] + " ";
				else
				newstr = newstr.trim() + words[i] + " ";
			}
            else if(DetectSpecialChar==' ' && i==words.length-1)
			{
				if(boolvalue==false)
                newstr = newstr + words[i];
				else
				newstr = newstr.trim() + words[i];
			}
            else if(DetectSpecialChar == 'Y')
            {
                if(words[i].length()!=0)
                {
					if(boolvalue==false)
                    newstr = newstr + punctype[handY] + words[i];
					else
					newstr = newstr.trim() + punctype[handY] + words[i];
                    handY++;
                }
            }
            else if(DetectSpecialChar == 'X')
            {
                if(words[i].length()!=0)
                {
					if(boolvalue==false)
                    newstr = newstr + punctype[handY] + words[i];
					else
					newstr = newstr.trim() + punctype[handY] + words[i];
                    handY++;
                }
            }
            else
            {
                    // no correction
            }
        }
        if((DetectSpecialChar == 'Y') && (handY<punctype.length))
        {
			if(boolvalue==false)
            newstr = newstr + punctype[handY];
			else
			newstr = newstr.trim() + punctype[handY];
        }
        else if((DetectSpecialChar == 'X') && (handY < punctype.length))
        {
			if(boolvalue==false)
            newstr=newstr+punctype[handY];
			else
			newstr=newstr.trim()+punctype[handY];
        }
        
        return newstr.trim();
    }
    public static String[] findNword(String str)
    {
    	String Nwords[]=str.split("\\s+");
    	int l=Nwords.length;
    	if(l>=3)
    	{
    		String words[]={Nwords[l-3],Nwords[l-2],Nwords[l-1]};
    		return words;
    	}
    	else if(l==0)
    	{
    		String temp[]={""};
    		return temp;
    	}
    	else
    		return Nwords;
    }
    public static String find1word(String str)
    {
    	String Nwords[]=str.trim().split("\\s+");
    	int l=Nwords.length;
    	if(l>=1)
    	{
    		return Nwords[l-1];
    	}
    	else
    	return "";
    }
}