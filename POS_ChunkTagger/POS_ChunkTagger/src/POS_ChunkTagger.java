import java.io.IOException;
import java.util.Collection;

/**
 * @author shankar kr chaudhary
 *
 */
//!@#$%^&()_+}{:"?><.,/[]\*|
//.?!@#$^&*()_+}{:" ><, []=O
//%=B-NP
//*=\*=O
// /=\/=O
//| =blank
// \=blank
public class POS_ChunkTagger
{
	public static void main(String[] args) throws ClassNotFoundException, IOException
	{
		ChunkerTagging chunkobj=new ChunkerTagging();
		POS posobj=new POS();
		System.out.println("I am using the pos tagging from our trained data 3Combined.tagger");
		// TODO Auto-generated method stub
		String str="my mail id is shankar4612@gmail.com and website is http://shanky.com .";
		//String str1[]=str.split("\\s+");
		String postag1=POS.PosTagging(str);
		String post2[]=postag1.split("\\t");
		System.out.println("Resultant postag Based on 3combined.tagger:\n"+post2[0]+"\n--------------------------");
		//System.out.println("Resultant postag Based on bidirectional-distsim-wsj-0-18.tagger:\n"+post2[1]+"\n--------------------------");
		String chunktag1=ChunkerTagging.chunker(post2[0]);
		//String chunktag2=ChunkerTagging.chunker(post2[1]);
		System.out.println("Resultant chunktag using 3combined.tagger in pos:\n"+chunktag1+"\n--------------------------");
		//System.out.println("Resultant chunktag using bidirectional-distsim-wsj-0-18.tagger in pos:\n"+chunktag2+"\n--------------------------");
//		Collection Dependencies=DependencyParse.DependencyParsing(str);
//		System.out.println(Dependencies);
	}

}
