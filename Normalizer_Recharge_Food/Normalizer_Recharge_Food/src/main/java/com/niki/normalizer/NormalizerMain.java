/**
 * @author shankar kr chaudhary   
 * 
 *
 */
//
//problem in handling with doctype comments in xml si i used xml file without comments
//NormalizerMain.java is the main file where i am reading the xml file (file.xml) and aftr changing saving it to same file (file.xml)
//i am working on the individuals message of xml file
//so for that first i am getting n the no. of total messages using DOM parsers in java
//i looped through n times to get the messages individually
//i am extracting the text from individual message at 94 line as string and from here i am calling function to normalise it 
//i called cleaner to normalise the special characters 
//then i have called socialmap method in hash mapping where it is updating string according to slang list
//i will continue to work on Hashmapping class(to implement spell checker levesthein and n-gram by different methods )
//
//
package com.niki.normalizer;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class NormalizerMain
{
	public NormalizerMain()throws IOException
	{
		// do nothing
	}
	public static void main(String[] args)throws IOException
	{
		Cleaner cleanobj = new Cleaner();
		HashMapping MappingObj=new HashMapping();

		int n = 0;
		String filepath = "file.xml";
		try
		{
			DocumentBuilderFactory cdocFactory = DocumentBuilderFactory.newInstance();
			cdocFactory.setIgnoringComments(false);
			DocumentBuilder cdocBuilder = cdocFactory.newDocumentBuilder();
			Document cdoc = cdocBuilder.parse(new File(filepath));
			Node croot = cdoc.getFirstChild();
			NodeList cmsglist = croot.getChildNodes();
			n = cmsglist.getLength();
		}

		catch (ParserConfigurationException pce)
		{
			pce.printStackTrace();
		}

		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}

		catch (SAXException sae)
		{
			sae.printStackTrace();
		}

		for(int i=0;i<n;i++)
		{
			try
			{
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				docFactory.setIgnoringComments(true);
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(new File(filepath));

				// Get the root element
				Node root = doc.getFirstChild();//root=smscorpus

				// Get the message element , it may not working if tag has spaces, or
				NodeList msglist = root.getChildNodes();
				Node upnode = msglist.item(i);
				if ("message".equals(upnode.getNodeName()))
				{
					Node Text = upnode.getFirstChild();			// Make sure that the first element is text.
					String tempstr=Text.getTextContent();	
					tempstr=cleanobj.cleaning1(tempstr);
					String punctype[]={" "};
					tempstr=MappingObj.socialhash(tempstr,punctype);
		
					Text.setTextContent(tempstr);
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File(filepath));
					transformer.transform(source, result);
				}			
			} 
			catch (ParserConfigurationException pce)
			{
				pce.printStackTrace();
			} 
			catch (TransformerException tfe)
			{
				tfe.printStackTrace();
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
			catch (SAXException sae)
			{
				sae.printStackTrace();
			}
		}
	}
}
