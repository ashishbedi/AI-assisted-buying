import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
public class ChunkerTagging
{
	InputStream is;
	static ChunkerModel cModel;
	public ChunkerTagging()throws ClassNotFoundException, IOException
	{
		is = new FileInputStream("en-chunker.bin");
		cModel = new ChunkerModel(is);
	}
	public static String chunker(String str) throws ClassNotFoundException, IOException
	{
		//System.out.println("This is ChunkTagger");
		String whitespaceTokenizerLine[] = null;
		whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE
				.tokenize(str);
		String[] tags = new String[whitespaceTokenizerLine.length];
		for(int i=0;i<whitespaceTokenizerLine.length;i++)
		{
			whitespaceTokenizerLine[i]=whitespaceTokenizerLine[i].replaceAll("\\//", "###");//handling url
			String temp[]=whitespaceTokenizerLine[i].split("/");
			whitespaceTokenizerLine[i]=temp[0].replaceAll("\\###+","//");
			tags[i]=temp[1].replaceAll("\\###+","/");;
			//System.out.println(i+"_=_"+whitespaceTokenizerLine[i]+"|"+tags[i]);
		}		
		// chunker
		
		ChunkerME chunkerME = new ChunkerME(cModel);
		String result[] = chunkerME.chunk(whitespaceTokenizerLine, tags);
		
		String RetChunk="";
		for (int i=0;i<result.length;i++)
		{
			RetChunk=RetChunk+whitespaceTokenizerLine[i]+"/"+result[i]+" ";
			//System.out.println(whitespaceTokenizerLine[i]+"/"+result[i]);
		}
	 
		//Span[] span = chunkerME.chunkAsSpans(whitespaceTokenizerLine, tags);
		//for (Span s : span)
			//System.out.println(s.toString());
		return RetChunk;
	}
}
