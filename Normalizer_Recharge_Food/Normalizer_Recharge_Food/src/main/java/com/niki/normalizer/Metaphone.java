package com.niki.normalizer;

/*

 * Licensed to the Apache Software Foundation (ASF) under one or more 
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.apache.commons.codec.EncoderException; 
import org.apache.commons.codec.StringEncoder;
import java.io.*;
import java.util.*;

/**
 * Encodes a string into a metaphone value. 
 * <p>
 * Initial Java implementation by <CITE>William B. Brogden. December, 1997. 
 * Permission given by <CITE>wbrogden for code to be used anywhere.
 * </p>
 * <p>
 * <CITE>Hanging on the Metaphone by Lawrence Philips in Computer Language of Dec. 1990, p
 * 39.</CITE>
 * </p>
 * <p>
 * Note, that this does not match the algorithm that ships with PHP, or the algorithm 
 * found in the Perl <a href="http://search.cpan.org/~mschwern/Text-Metaphone-1.96/Metaphone.pm">Text:Metaphone-1.96.
 * They have had undocumented changes from the originally published algorithm. 
 * For more information, see <a href="https://issues.apache.org/jira/browse/CODEC-57">CODEC-57.
 * </p>
 * 
 * @author Apache Software Foundation
 * @version $Id: Metaphone.java 797690 2009-07-24 23:28:35Z ggregory $
 */
public class Metaphone implements StringEncoder {
    private static HashMap <Character, Character> phoneticMap;
    public static HashMap <String, List <String>> phoneticCodeTable;
    public static HashMap <String, List <String>> phoneticCodeTable2;
    public static HashMap <String, Boolean> common;
    static
    {
        phoneticMap = new HashMap<Character, Character> ();
        phoneticMap.put('b', '1');
        phoneticMap.put('p', '1');
        phoneticMap.put('f', '2');
        phoneticMap.put('v', '2');
        phoneticMap.put('c', '3');
        phoneticMap.put('k', '3');
        phoneticMap.put('s', '3');
        phoneticMap.put('g', '4');
        phoneticMap.put('j', '4');
        phoneticMap.put('q', '5');
        phoneticMap.put('x', '5');
        phoneticMap.put('z', '5');
        phoneticMap.put('d', '6');
        phoneticMap.put('t', '6');
        phoneticMap.put('l', '7');
        phoneticMap.put('n', '8');
        phoneticMap.put('m', '8');
        phoneticMap.put('r', '9');
    }

    /**
     * Five values in the English language 
     */
    private static final String VOWELS = "AEIOU" ;

    /**
     * Variable used in Metaphone algorithm
     */
    private static final String FRONTV = "EIY"   ;

    /**
     * Variable used in Metaphone algorithm
     */
    private static final String VARSON = "CSPTG" ;

    /**
     * The max code length for metaphone is 4
     */
    private static final int maxCodeLen = 4 ;

    /**
     * Creates an instance of the Metaphone encoder
     */
    public Metaphone() {
        super();
    }

    /**
     * Find the metaphone value of a String. This is similar to the
     * soundex algorithm, but better at finding similar sounding words.
     * All input is converted to upper case.
     * Limitations: Input format is expected to be a single ASCII word
     * with only characters in the A - Z range, no punctuation or numbers.
     *
     * @param txt String to find the metaphone code for
     * @return A metaphone code corresponding to the String supplied
     */
    public static String metaphone(String txt) {
        boolean hard = false ;
        if ((txt == null) || (txt.length() == 0)) {
            return "" ;
        }
        // single character is itself
        if (txt.length() == 1) {
            return txt.toUpperCase(java.util.Locale.ENGLISH) ;
        }
      
        char[] inwd = txt.toUpperCase(java.util.Locale.ENGLISH).toCharArray() ;
      
        StringBuffer local = new StringBuffer(40); // manipulate
        StringBuffer code = new StringBuffer(10) ; //   output
        // handle initial 2 characters exceptions
        switch(inwd[0]) {
        case 'K' : 
        case 'G' : 
        case 'P' : /* looking for KN, etc*/
            if (inwd[1] == 'N') {
                local.append(inwd, 1, inwd.length - 1);
            } else {
                local.append(inwd);
            }
            break;
        case 'A': /* looking for AE */
            if (inwd[1] == 'E') {
                local.append(inwd, 1, inwd.length - 1);
            } else {
                local.append(inwd);
            }
            break;
        case 'W' : /* looking for WR or WH */
            if (inwd[1] == 'R') {   // WR -> R
                local.append(inwd, 1, inwd.length - 1); 
                break ;
            }
            if (inwd[1] == 'H') {
                local.append(inwd, 1, inwd.length - 1);
                local.setCharAt(0, 'W'); // WH -> W
            } else {
                local.append(inwd);
            }
            break;
        case 'X' : /* initial X becomes S */
            inwd[0] = 'S';
            local.append(inwd);
            break ;
        default :
            local.append(inwd);
        } // now local has working string with initials fixed

        int wdsz = local.length();
        int n = 0 ;

        while ((code.length() < 4) && 
               (n < wdsz) ) { // max code size of 4 works well
            char symb = local.charAt(n) ;
            // remove duplicate letters except C
            if ((symb != 'C') && (isPreviousChar( local, n, symb )) ) {
                n++ ;
            } else { // not dup
                switch(symb) {
                case 'A' : case 'E' : case 'I' : case 'O' : case 'U' :
                    if (n == 0) { 
                        code.append(symb);
                    }
                    break ; // only use vowel if leading char
                case 'B' :
                    if ( isPreviousChar(local, n, 'M') && 
                         isLastChar(wdsz, n) ) { // B is silent if word ends in MB
                        break;
                    }
                    code.append(symb);
                    break;
                case 'C' : // lots of C special cases
                    /* discard if SCI, SCE or SCY */
                    if ( isPreviousChar(local, n, 'S') && 
                         !isLastChar(wdsz, n) && 
                         (FRONTV.indexOf(local.charAt(n + 1)) >= 0) ) { 
                        break;
                    }
                    if (regionMatch(local, n, "CIA")) { // "CIA" -> X
                        code.append('X'); 
                        break;
                    }
                    if (!isLastChar(wdsz, n) && 
                        (FRONTV.indexOf(local.charAt(n + 1)) >= 0)) {
                        code.append('S');
                        break; // CI,CE,CY -> S
                    }
                    if (isPreviousChar(local, n, 'S') &&
                        isNextChar(local, n, 'H') ) { // SCH->sk
                        code.append('K') ; 
                        break ;
                    }
                    if (isNextChar(local, n, 'H')) { // detect CH
                        if ((n == 0) && 
                            (wdsz >= 3) && 
                            isVowel(local,2) ) { // CH consonant -> K consonant
                            code.append('K');
                        } else { 
                            code.append('X'); // CHvowel -> X
                        }
                    } else { 
                        code.append('K');
                    }
                    break ;
                case 'D' :
                    if (!isLastChar(wdsz, n + 1) && 
                        isNextChar(local, n, 'G') && 
                        (FRONTV.indexOf(local.charAt(n + 2)) >= 0)) { // DGE DGI DGY -> J 
                        code.append('J'); n += 2 ;
                    } else { 
                        code.append('T');
                    }
                    break ;
                case 'G' : // GH silent at end or before consonant
                    if (isLastChar(wdsz, n + 1) && 
                        isNextChar(local, n, 'H')) {
                        break;
                    }
                    if (!isLastChar(wdsz, n + 1) &&  
                        isNextChar(local,n,'H') && 
                        !isVowel(local,n+2)) {
                        break;
                    }
                    if ((n > 0) && 
                        ( regionMatch(local, n, "GN") ||
                          regionMatch(local, n, "GNED") ) ) {
                        break; // silent G
                    }
                    if (isPreviousChar(local, n, 'G')) {
                        // NOTE: Given that duplicated chars are removed, I don't see how this can ever be true
                        hard = true ;
                    } else {
                        hard = false ;
                    }
                    if (!isLastChar(wdsz, n) && 
                        (FRONTV.indexOf(local.charAt(n + 1)) >= 0) && 
                        (!hard)) {
                        code.append('J');
                    } else {
                        code.append('K');
                    }
                    break ;
                case 'H':
                    if (isLastChar(wdsz, n)) {
                        break ; // terminal H
                    }
                    if ((n > 0) && 
                        (VARSON.indexOf(local.charAt(n - 1)) >= 0)) {
                        break;
                    }
                    if (isVowel(local,n+1)) {
                        code.append('H'); // Hvowel
                    }
                    break;
                case 'F': 
                case 'J' : 
                case 'L' :
                case 'M': 
                case 'N' : 
                case 'R' :
                    code.append(symb); 
                    break;
                case 'K' :
                    if (n > 0) { // not initial
                        if (!isPreviousChar(local, n, 'C')) {
                            code.append(symb);
                        }
                    } else {
                        code.append(symb); // initial K
                    }
                    break ;
                case 'P' :
                    if (isNextChar(local,n,'H')) {
                        // PH -> F
                        code.append('F');
                    } else {
                        code.append(symb);
                    }
                    break ;
                case 'Q' :
                    code.append('K');
                    break;
                case 'S' :
                    if (regionMatch(local,n,"SH") || 
                        regionMatch(local,n,"SIO") || 
                        regionMatch(local,n,"SIA")) {
                        code.append('X');
                    } else {
                        code.append('S');
                    }
                    break;
                case 'T' :
                    if (regionMatch(local,n,"TIA") || 
                        regionMatch(local,n,"TIO")) {
                        code.append('X'); 
                        break;
                    }
                    if (regionMatch(local,n,"TCH")) {
                        // Silent if in "TCH"
                        break;
                    }
                    // substitute numeral 0 for TH (resembles theta after all)
                    if (regionMatch(local,n,"TH")) {
                        code.append('0');
                    } else {
                        code.append('T');
                    }
                    break ;
                case 'V' :
                    code.append('F'); break ;
                case 'W' : case 'Y' : // silent if not followed by vowel
                    if (!isLastChar(wdsz,n) && 
                        isVowel(local,n+1)) {
                        code.append(symb);
                    }
                    break ;
                case 'X' :
                    code.append('K'); code.append('S');
                    break ;
                case 'Z' :
                    code.append('S'); break ;
                } // end switch
                n++ ;
            } // end else from symb != 'C'
            if (code.length() > 4) { 
                code.setLength(4); 
            }
        }
        return code.toString();
    }

    private static boolean isVowel(StringBuffer string, int index) {
        return VOWELS.indexOf(string.charAt(index)) >= 0;
    }

    private static boolean isPreviousChar(StringBuffer string, int index, char c) {
        boolean matches = false;
        if( index > 0 &&
            index < string.length() ) {
            matches = string.charAt(index - 1) == c;
        }
        return matches;
    }

    private static boolean isNextChar(StringBuffer string, int index, char c) {
        boolean matches = false;
        if( index >= 0 &&
            index < string.length() - 1 ) {
            matches = string.charAt(index + 1) == c;
        }
        return matches;
    }

    private static boolean regionMatch(StringBuffer string, int index, String test) {
        boolean matches = false;
        if( index >= 0 &&
            (index + test.length() - 1) < string.length() ) {
            String substring = string.substring( index, index + test.length());
            matches = substring.equals( test );
        }
        return matches;
    }

    private static boolean isLastChar(int wdsz, int n) {
        return n + 1 == wdsz;
    } 
    
    
    /**
     * Encodes an Object using the metaphone algorithm.  This method
     * is provided in order to satisfy the requirements of the
     * Encoder interface, and will throw an EncoderException if the
     * supplied object is not of type java.lang.String.
     *
     * @param pObject Object to encode
     * @return An object (or type java.lang.String) containing the 
     *         metaphone code which corresponds to the String supplied.
     * @throws EncoderException if the parameter supplied is not
     *                          of type java.lang.String
     */
    public Object encode(Object pObject) throws EncoderException {
        if (!(pObject instanceof String)) {
            throw new EncoderException("Parameter supplied to Metaphone encode is not of type java.lang.String"); 
        }
        return metaphone((String) pObject);
    }

    /**
     * Encodes a String using the Metaphone algorithm. 
     *
     * @param pString String object to encode
     * @return The metaphone code corresponding to the String supplied
     */
    public String encode(String pString) {
        return metaphone(pString);   
    }

    /**
     * Tests is the metaphones of two strings are identical.
     *
     * @param str1 First of two strings to compare
     * @param str2 Second of two strings to compare
     * @return <code>true if the metaphones of these strings are identical, 
     *        <code>false otherwise.
     */
    public static boolean isMetaphoneEqual(String str1, String str2) {
        return metaphone(str1).equals(metaphone(str2));
    }

    /**
     * Returns the maxCodeLen.
     * @return int
     */
    public int getMaxCodeLen() { return this.maxCodeLen; }

    /**
     * Sets the maxCodeLen.
     * @param maxCodeLen The maxCodeLen to set
     */
    public static String encodeWord(String s)
    {
    	String encoded = "";
    	encoded += s.charAt(0);
    	Character prev_phonetic = phoneticMap.get(s.charAt(0));
    	Character prev = s.charAt(0);
    	if(s.length() >= 2)
    	{
    		Character pre_previous = phoneticMap.get(s.charAt(1));
	    	for(int i=1; i<s.length(); i++)
	    	{
	    		char current = s.charAt(i);
	    		if ((current == 'a') || (current == 'e') || (current == 'i') || (current == 'o') || (current == 'u') || (current == 'y') || (current == 'h'))
	    		{

	    		}
	    		else
	    		{
	    			if(phoneticMap.get(pre_previous) != phoneticMap.get(current))
	    			{
	    				if(prev_phonetic != phoneticMap.get(current))
	    				{
	    					encoded += phoneticMap.get(current);
	    				}
	    			}
	    			else
	    			{
	    				if((prev == 'a') || (prev == 'e') || (prev == 'i') || (prev == 'o') || (prev== 'u'))
	    				{
	    					encoded += phoneticMap.get(current);
	    				}
	    				else if((prev == 'h') || (prev == 'w'))
	    				{	

	    				}
	    				else
	    				{
	    					if(prev_phonetic != phoneticMap.get(current))
	    					{	    				
	    						encoded += phoneticMap.get(current);
	    					}
	    				}
	    			}
	    		}
				pre_previous = prev;
			    prev = current;
				prev_phonetic = phoneticMap.get(prev);
	    	}
	    }

    	if(encoded.length() < 4)
    	{
    		while(encoded.length() != 4)
    		{
    			encoded += '0';
    		}
    	}
    	else if (encoded.length() > 4)
    	{
    		encoded = encoded.substring(0,4);
    	}
  		return encoded;
    }
    
    public static void createPhoneticCodeDictionary() throws IOException
    {
        phoneticCodeTable = new HashMap <String, List <String>>();
        phoneticCodeTable2 = new HashMap <String, List <String> >();
        BufferedReader input = new BufferedReader(new FileReader("new_big.txt"));

        String line = input.readLine();
        while(line != null)
        {
            String word = line;
            if((word != "") || (word != "(\\s+)"))
            {  
                String encoding = Metaphone.metaphone(word);
                if(phoneticCodeTable.containsKey(encoding))
                {
                    List <String> temp = phoneticCodeTable.get(encoding);
                    temp.add(word);
                    phoneticCodeTable.put(encoding, temp);
                }
                else
                {
                    List <String> temp = new ArrayList <String> ();
                    temp.add(word);
                    phoneticCodeTable.put(encoding, temp);
                }
            }
            
            if((word != "") || (word != "(\\s+)"))
            {  
                String encoding = Metaphone.encodeWord(word);
                if(phoneticCodeTable2.containsKey(encoding))
                {
                    List <String> temp = phoneticCodeTable2.get(encoding);
                    temp.add(word);
                    phoneticCodeTable2.put(encoding, temp);
                }
                else
                {
                    List <String> temp = new ArrayList <String> ();
                    temp.add(word);
                    phoneticCodeTable2.put(encoding, temp);
                }
            }
            
            line = input.readLine();
        }

        input.close();
    }
    
    public static int DamerauLevenshteinDistance(String source, String target)
    {
        int m = source.length();
        int n = target.length();
        int[][] H = new int[m + 2][n + 2];

        int INF = m + n;
        H[0][0] = INF;
        for (int i = 0; i <= m; i++) 
        { 
            H[i + 1][1] = i;
            H[i + 1][0] = INF; 
        }
        for (int j = 0; j <= n; j++) 
        { 
            H[1][j + 1] = j; 
            H[0][j + 1] = INF; 
        }
        TreeMap<Character, Integer> sd = new TreeMap<Character, Integer>();
        for (int iter = 0;iter < (source + target).length();iter++)
        {
            if (!sd.containsKey((source + target).charAt(iter)))
            {
                sd.put((source + target).charAt(iter), 0);
            }
        }

        for (int i = 1; i <= m; i++)
        {
            int DB = 0;
            for (int j = 1; j <= n; j++)
            {
                int i1 = sd.get(target.charAt(j - 1));
                int j1 = DB;

                if (source.charAt(i - 1) == target.charAt(j - 1))
                {
                    H[i + 1][j + 1] = H[i][j];
                    DB = j;
                }
                else
                {
                    H[i + 1][j + 1] = Math.min(H[i][j], Math.min(H[i + 1][j], H[i][j + 1])) + 1;
                }

                H[i + 1][j + 1] = Math.min(H[i + 1][j + 1], H[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1));
            }

            sd.put(source.charAt(i - 1),i);
        }
        return H[m + 1][n + 1];
    }
    
    public static List <String> returnSuggestions(String word)
    {
    	List <String> filteredList = new ArrayList <String> ();
    	if(word != null)
        {
        	word = word.trim();
        	String encoding = Metaphone.metaphone(word);
        	String encoding2 = Metaphone.encodeWord(word);
        	common = new HashMap <String, Boolean>();
        	if(phoneticCodeTable.containsKey(encoding))
	        	for(int i=0; i<phoneticCodeTable.get(encoding).size(); i++)
	        	{
	        		String s = phoneticCodeTable.get(encoding).get(i);
	        		common.put(s, true);
	        	}
        	List <String> commonList = new ArrayList <String> ();
        	if(phoneticCodeTable2.containsKey(encoding2))
	        	for(int i=0; i<phoneticCodeTable2.get(encoding2).size(); i++)
	        	{
	        		String s = phoneticCodeTable2.get(encoding2).get(i);
	        		if(common.containsKey(s))
	        			commonList.add(s);
	        	}
        	
        	for(int i=0; i<commonList.size(); i++)
        	{
        		String s = commonList.get(i);
        		if((s.length() <= word.length() + 2) && ((DamerauLevenshteinDistance(s, word) < 3)))
        			filteredList.add(s);
        	}
        }
        return filteredList;
    }
}