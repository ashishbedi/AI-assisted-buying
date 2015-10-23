import java.io.*;

/**
 * @author shankar kr chaudhary
 *
 */
public class Testing
{
	public static void main(String[] args)  throws ClassNotFoundException, IOException
	{
		ChunkerTagging chunkobj=new ChunkerTagging();
		POS posobj=new POS();
		BufferedWriter bw=new BufferedWriter(new FileWriter("outtag.txt"));
		System.out.println("\t # \t I am using the pos tagging from our trained data 3Combined.tagger\t #\n");
		//bw.write("\t # \t I am using the pos tagging from our trained data 3Combined.tagger\t #");
		//bw.newLine();
		//System.out.println("-------------------------------------------------------------");
		//bw.write("-------------------------------------------------------------");
		//bw.newLine();
		BufferedReader inp=new BufferedReader(new FileReader("cLines.txt"));

		String str =inp.readLine();
		int num=0;
		while(str!=null)
		{
			//str="my mail id is shankar4612@gmail.com and website is http://shanky.com .";
			str=str.trim();
			System.out.println(str);
			bw.write(str);
			bw.newLine();
			String postag1=POS.PosTagging(str);
			System.out.println("-> POS:\t"+postag1);
			bw.write("-> POS:\t"+postag1);
			bw.newLine();
			String chunktag1=ChunkerTagging.chunker(postag1);
			System.out.println("-> CHUNKTAG:\t"+chunktag1);
			bw.write("-> CHUNKTAG:\t"+chunktag1);
			bw.newLine();
			System.out.println("-------------------------------------------------------------");
			bw.write("-------------------------------------------------------------");
			bw.newLine();
			num++;
			if(num==1000)
				break;
			str=inp.readLine();
		}
		inp.close();
		bw.close();

	}

}
