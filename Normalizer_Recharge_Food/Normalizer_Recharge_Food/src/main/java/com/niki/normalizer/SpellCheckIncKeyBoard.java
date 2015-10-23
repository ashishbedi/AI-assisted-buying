package com.niki.normalizer;

import java.util.*; 
import java.util.Vector;
import  java.lang.* ; 
import java.io.*;   
import java.io.File;
import java.text.*; 
import java.util.regex.*;
import java.util.Scanner;


class keyboardCorrector
{
    private static HashMap <Character, List <Character>> near;
    static
    {
        near = new HashMap<Character, List <Character>> ();
        near.put('a', Arrays.asList('a','q','w','s','z','x'));
        near.put('b', Arrays.asList('b','v','g','h','n'));
        near.put('c', Arrays.asList('c','x','d','f','v'));
        near.put('d', Arrays.asList('d','s','e','r','f','c','x'));
        near.put('e', Arrays.asList('e','w','s','d','f','r'));
        near.put('f', Arrays.asList('f','d','e', 'r','t','g','v','c'));
        near.put('g', Arrays.asList('g','f','t','y','h','b','v'));
        near.put('h', Arrays.asList('h','g','y','u','j','n','b'));
        near.put('i', Arrays.asList('i','u','j','k','l','o'));
        near.put('j', Arrays.asList('j','h','u','i','k','m','n'));
        near.put('k', Arrays.asList('k','j','i','o','l','m'));
        near.put('l', Arrays.asList('l','k','i','o','p'));
        near.put('m', Arrays.asList('m','n','j','k','l'));
        near.put('n', Arrays.asList('n','b','h','j','m','k'));
        near.put('o', Arrays.asList('o','i','k','l','p'));
        near.put('p', Arrays.asList('p','i','k','l','o'));
        near.put('q', Arrays.asList('q','a','s','w'));
        near.put('r', Arrays.asList('r','e','d','f','g','t'));
        near.put('s', Arrays.asList('s','a','q','w','e','d','x','z'));
        near.put('t', Arrays.asList('t','r','f','g','h','y'));
        near.put('u', Arrays.asList('u','y','h','j','k','i'));
        near.put('v', Arrays.asList('v','c','f','g','b','h','d'));
        near.put('w', Arrays.asList('w','q','a','s','d','e'));
        near.put('x', Arrays.asList('x','z','s','d','f','c'));
        near.put('y', Arrays.asList('y','t','g','h','u','j'));
        near.put('z', Arrays.asList('z','a','s','x','d'));
    }

    public static List <String> proximity(String word, List <String> sugg)
    {
        List <String> ret = new ArrayList <String> ();
        for(String s: sugg)
        {
            int bad = 0;
            if(word.length() == s.length())
            {
                for(int i=0; i<s.length(); i++)
                {
                    Character letter = s.charAt(i);
                    if(near.get(word.charAt(i)).contains(letter) == false)
                        bad++;
                }
                if(bad < 1)
                    ret.add(s);
            }
            else if(word.length() < s.length())
            {
                for(int i=0; i<word.length(); i++)
                {
                    Character letter = s.charAt(i);
                    if(near.get(word.charAt(i)).contains(letter) == false)
                        bad++;
                }
                if(bad < 1)	
                	ret.add(s);
            }
        }
        return ret;
    }

    public static List <String> swaps(String word, List <String> sugg)
    {
        List <String> ret = new ArrayList <String> ();
        List <String> all_swaps = new ArrayList <String> ();
        for(int i=0; i<word.length()-1; i++)
        {
            String now = word.substring(0,i);
            all_swaps.add(now + word.charAt(i+1) + word.charAt(i) + word.substring(i+2, word.length()));
        }
        for(String x: all_swaps)
        {
            if(sugg.contains(x))
                ret.add(x);
        }
        return ret;
    }

    public static List < List <String> > splits(String word)
    {
        List < List <String> > ret = new ArrayList <List <String>> ();
        for (int i=0; i<=word.length(); i++)
        {
            List <String> tem = new ArrayList <String> ();
            tem.add(word.substring(0,i));
            tem.add(word.substring(i, word.length()));
            ret.add(tem);
        }
        return ret;
    }   

    public static List <String> inserts(String word, List <String> sugg)
    {
        List <String> ret = new ArrayList <String> ();
        List <String> all_deletes = new ArrayList <String> ();
        for(List <String> x: splits(word))
        {
            String a = x.get(0);
            String b = x.get(1);
            if((b.length() > 0) && (a.length() > 0))
            {
                if(near.get(a.charAt(a.length()-1)).contains(b.charAt(0)))
                    all_deletes.add(a + b.substring(1,b.length()));
            }
        }
        Set <String> set = new HashSet<>();
        set.addAll(all_deletes);
        all_deletes.clear();
        all_deletes.addAll(set);
        for (String x: all_deletes)
        {
            if(sugg.contains(x))
                ret.add(x);
        }
        return ret;
    }

    public static List <String> containing(String word, List <String>  sugg)
    {
        List <String> ret = new ArrayList <String> ();
        for(String s: sugg)
        {
            int ok = 1;
            for(int i=0; i<word.length(); i++)
            {
                if(s.indexOf(word.charAt(i)) == -1)
                    ok = 0;
            }
            if(ok == 1)
                ret.add(s);
        }
        return ret;
    }

    public static List <String> solve(String word, List <String> sugg)
    {
        List < String > ret = new ArrayList <String> ();
        for(String s: proximity(word, sugg))
        {
            ret.add(s);
        }
        for(String s: swaps(word, sugg))
        {
            ret.add(s);
        }
        for(String s: inserts(word, sugg))
        {
            ret.add(s);
        }
        for(String s: containing(word, sugg))
        {
            ret.add(s);
        }
        Set <String> set = new HashSet<>();
        set.addAll(ret);
        ret.clear();
        ret.addAll(set);
        return ret;
    }
}

class dictionaryItem
{
    public ArrayList<Integer> suggestions = new ArrayList<Integer> ();
    public int count = 0;
}

class suggestItem implements Comparable<suggestItem>
{
    public String term = "";
    public int distance = 0;
    public int count = 0;

    @Override
    public boolean equals(Object obj)
    {
        return term.equals(((suggestItem)obj).term);
    }

    @Override
    public int hashCode()
    {
        return term.hashCode();
    }

    @Override
    public int compareTo(suggestItem s1)
    {
        if (this.count > s1.count)
            return -1;
        else if (this.count == s1.count)
            return 0;
        else 
            return 1;
    }      
}

public class SpellCheckIncKeyBoard
{
    public static int editDistanceMax = 2;
    
    public static int verbose = 0;
    
    public static int maxlength = 0; //maximum dictionary term length

    public static int finDictNum = 0;

    public static HashMap<String, Object> dictionary = new HashMap<String, Object>(); 

    public static ArrayList<String> wordlist = new ArrayList<String>();

    //for every word there all deletes with an edit distance of 1..editDistanceMax created and added to the dictionary
    //every delete entry has a suggestions list, which points to the original term/s it was created from
    //The dictionary may be dynamically updated (word frequency and new words) at any time by calling createDictionaryEntry
    public static boolean CreateDictionaryEntry(String key)
    {
        boolean result = false;
        dictionaryItem value = null;
        Object valueo;
        if (dictionary.containsKey(key))
        {
            valueo = dictionary.get(key);
            if (valueo instanceof Integer)
            {
                int tmp = (int)valueo;
                value = new dictionaryItem();
                value.suggestions.add(tmp); 
                dictionary.put(key,value); 
            }
            else
            {
                value = (dictionaryItem)valueo;
            }
            //prevent overflow
            if (value.count < Integer.MAX_VALUE) 
                value.count++;
        }
        else if (wordlist.size() < Integer.MAX_VALUE)
        {
            value = new dictionaryItem();
            ((dictionaryItem)value).count++;
            dictionary.put(key,(dictionaryItem)value);
            finDictNum++;
            if (key.length() > maxlength) 
            {
                maxlength = key.length();
            }
        }
        //edits/suggestions are created only once, no matter how often word occurs
        //edits/suggestions are created only as soon as the word occurs in the corpus, 
        //even if the same term existed before in the dictionary as an edit from another word
        //a threshold might be specifid, when a term occurs so frequently in the corpus that it is considered a valid word for spelling correction
        if (((dictionaryItem)value).count == 1)
        {
            wordlist.add(key);
            int keyint = (wordlist.size() - 1);

            result = true;
            //create deletes
            for (String delete : Edits(key, 0, new ArrayList<String>()))
            {
                Object value2;
                if (dictionary.containsKey(delete))
                {
                    value2 = dictionary.get(delete);
                    if (value2 instanceof Integer)
                    {
                        int tmp = (int)value2;
                        dictionaryItem di = new dictionaryItem();
                        di.suggestions.add(tmp);
                        dictionary.put(delete,di);  
                        if (!(di.suggestions.contains(keyint)))
                        {
                            AddLowestDistance(di,key,keyint,delete);
                        }
                    }
                    else if (!((dictionaryItem)value2).suggestions.contains(keyint))
                    {
                        AddLowestDistance((dictionaryItem)value2, key, keyint, delete);
                    }
                }
                else
                {
                    finDictNum++;
                    dictionary.put(delete,keyint);
                }
            }
        }
        return result;
    }

    public static void AddLowestDistance(dictionaryItem item, String suggestion, int suggestionint, String delete)
    {
        if ((verbose == 2) || (item.suggestions.size() == 0) || ((wordlist.get((int)(item.suggestions.get(0)))).length() - delete.length() >= suggestion.length() - delete.length())) 
        {
            item.suggestions.add(suggestionint);
        } 
    }

    public static ArrayList<String> Edits(String word, int editDistance, ArrayList<String> deletes)
    {
        editDistance++;
        if (word.length() > 1)
        {
            for (int i = 0; i < word.length(); i++)
            {
                StringBuffer sBuffer = new StringBuffer(word);
                sBuffer = sBuffer.delete(i,i+1);
                String delete = sBuffer.toString();
                if (deletes.add(delete))
                {
                    //recursion, if maximum edit distance not yet reached
                    if (editDistance < editDistanceMax) 
                    {
                        Edits(delete, editDistance, deletes);
                    }
                }
            }
        }
        return deletes;
    }

    public static List<suggestItem> Lookup(String input, int editDistanceMax)
    {
        if (input.length() - editDistanceMax > maxlength) 
        {
            //returning empty result back
            List<suggestItem> retList = new ArrayList<suggestItem>();
            return retList;
        }
        ArrayList<String> candidates = new ArrayList<String>();
        ArrayList<String> hashset1 = new ArrayList<String>();
 
        ArrayList<suggestItem> suggestions = new ArrayList<suggestItem>();
        ArrayList<String> hashset2 = new ArrayList<String>();

        Object valueo;

        //add original term
        candidates.add(input);
        while (candidates.size() > 0)
        {
            String candidate = candidates.get(0);
            candidates.remove(0); 
            if (dictionary.containsKey(candidate))
            {
                valueo = dictionary.get(candidate);
                dictionaryItem value = new dictionaryItem();
                if (valueo instanceof Integer)
                {
                    value.suggestions.add((int)valueo);
                }
                else
                {
                    value = (dictionaryItem)valueo;
                }
                if ((value.count > 0) && hashset2.add(candidate))
                {
                   //add correct dictionary term term to suggestion list
                    suggestItem si = new suggestItem();
                    si.term = candidate;
                    si.count = value.count;
                    si.distance = input.length() - candidate.length();
                    if (!suggestions.contains(si))
                    suggestions.add(si); 
                }
                //iterate through suggestions (to other correct dictionary items) of delete item and add them to suggestion list
                Object value2;
                for (int suggestionint : value.suggestions)
                {
                    String suggestion = wordlist.get(suggestionint);
                    if (hashset2.add(suggestion))
                    {
                        //We allow simultaneous edits (deletes) of editDistanceMax on on both the dictionary and the input term. 
                        //For replaces and adjacent transposes the resulting edit distance stays <= editDistanceMax.
                        //For inserts and deletes the resulting edit distance might exceed editDistanceMax.
                        //To prevent suggestions of a higher edit distance, we need to calculate the resulting edit distance, if there are simultaneous edits on both sides.
                        int distance = 0;
                        if (suggestion != input)
                        {
                            if (suggestion.length() == candidate.length()) 
                            {
                                distance = input.length() - candidate.length();
                            }
                            else if (input.length() == candidate.length()) 
                            {
                                distance = suggestion.length() - candidate.length();
                            }
                            else
                            {
                                //common prefixes and suffixes are ignored, because this speeds up the Damerau-levenshtein-Distance calculation without changing it.
                                int ii = 0;
                                int jj = 0;
                                while ((ii < suggestion.length()) && (ii < input.length()) && (suggestion.charAt(ii) == input.charAt(ii))) 
                                {
                                    ii++;
                                }
                                while ((jj < suggestion.length() - ii) && (jj < input.length() - ii) && (suggestion.charAt(suggestion.length() - jj - 1) == input.charAt(input.length() - jj - 1))) 
                                {
                                    jj++;
                                }
                                if ((ii > 0) || (jj > 0)) 
                                {
                                    distance = DamerauLevenshteinDistance(suggestion.substring(ii, suggestion.length() - jj), input.substring(ii, input.length() - jj)); 
                                }
                                else
                                { 
                                    distance = DamerauLevenshteinDistance(suggestion, input);
                                }
                            }
                        }
                        //do not process higher distances than those already found, if verbose<2
                        if ((verbose < 2) && (suggestions.size() > 0) && (distance > suggestions.get(0).distance)) 
                        {
                            continue;
                        }
                        if (distance <= editDistanceMax)
                        {
                            if (dictionary.containsKey(suggestion))
                            {
                                value2 = dictionary.get(suggestion);
                                suggestItem si = new suggestItem();
                                si.term = suggestion;
                                si.count = ((dictionaryItem)value2).count;
                                si.distance = distance;
                                if (!suggestions.contains(si))
                                suggestions.add(si);
                            }
                        }
                    }
                }//end foreach
            }//end if         
            //add edits 
            //derive edits (deletes) from candidate (input) and add them to candidates list
            //this is a recursive process until the maximum edit distance has been reached
            if (input.length() - candidate.length() < editDistanceMax)
            {
                if ((verbose < 2) && (suggestions.size() > 0) && (input.length() - candidate.length() >= suggestions.get(0).distance)) 
                {
                    continue;
                }
                for (int i = 0; i < candidate.length(); i++)
                {
                    StringBuffer sBuffer = new StringBuffer(candidate);
                    sBuffer = sBuffer.delete(i,i+1);
                    String delete = sBuffer.toString();
                    if (hashset1.add(delete)) 
                    {
                        candidates.add(delete);
                    }
                }
            }
        }//end while
        return suggestions;
    }

    public static List<String> Correct(String input)
    {
        List<suggestItem> suggestions = null;
        suggestions = Lookup(input, editDistanceMax);
        List <String> suggestionsOut = new ArrayList<String> ();

        for(suggestItem suggestion : suggestions)
        {
            suggestionsOut.add(suggestion.term);
        }
        return suggestionsOut;
    }

    //Create a frequency dictionary from a corpus
    public static void CreateDictionary()throws IOException
    {
        System.out.println("Creating Dictionary...");
        long wordCount = 0;
        BufferedReader input = new BufferedReader(new FileReader("new_big.txt"));
        String line = input.readLine();

        while (line != null)
        {
            String key = line;
                if (key != "" || key != "(\\s+)")
                    {
                        if (CreateDictionaryEntry(key)) 
                        {
                            wordCount++;
                        }
                    }           
            line = input.readLine();
        }

        input.close();
        System.out.println("Dictionary Created Successfully.");
        System.out.println("Initial Word Count: " + wordCount);
        System.out.println("Word Count After Processing: " + finDictNum);
    }

    // DamerauLevenshtein distance algorithm and code 
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
}