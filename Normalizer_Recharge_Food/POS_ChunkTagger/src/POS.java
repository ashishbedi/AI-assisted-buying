//i am taking pos tagging from our trained data
import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POS 
{
	static MaxentTagger tagger;
	//static MaxentTagger tagger1;
	public POS()throws IOException,ClassNotFoundException
	{
		tagger = new MaxentTagger("3combined.tagger");
		//tagger1 = new MaxentTagger("bidirectional-distsim-wsj-0-18.tagger");
		
	}
	public static String PosTagging(String sample) throws IOException,ClassNotFoundException
	{
		//System.out.println("loading....");
//		MaxentTagger tagger = new MaxentTagger("bidirectional-distsim-wsj-0-18.tagger");
		
		//String sample = "I am travelling from hwh to ndls by Shatabdi Express and would like to order a veg thali from rajwadi dhaba by 4pm.";
		//sample = "I want to see a movie shankar .";
		//System.out.println("taggging done");
		//System.out.println("Based on mymodel.tagger:\n"+tagger.tagString(sample)+"\n--------------------------");
		//System.out.println("Based on bidirectional-distsim-wsj-0-18.tagger:\n"+tagger1.tagString(sample)+"\n--------------------------");
		//return tagger.tagString(sample)+"\t"+tagger1.tagString(sample);
		return tagger.tagString(sample);
	}
}	
