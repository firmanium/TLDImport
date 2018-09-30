import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 

public class ReadXML{

	// Constructor
	public ReadXML () {}
    
	public static void main (String argv[]) {
		String fileXML;
		if (argv.length > 0)
			{fileXML = String.valueOf(argv[0]);}
			else
			System.out.println("please insert xml fie \nUsage\t: java ReadXML somexmlfilename.xml \nUsing\t: test.xml (default parameter)\n");
			{fileXML="test.xml";}
		ReadXML readXML = new ReadXML();
		readXML.bacaDataXML(fileXML);
	}

	//method buat baca node xml
	public String bacaNode(Element firstPersonElement, String namaNode) {
		//-------
		NodeList namaNodeList = firstPersonElement.getElementsByTagName(namaNode);
		Element namaNodeElement = (Element)namaNodeList.item(0);
		NodeList textNamaNodeList = namaNodeElement.getChildNodes();
		String val = namaNode + "\t : " + ((Node)textNamaNodeList.item(0)).getNodeValue().trim();
		System.out.println(val);
		return val;
	}//end of bacaNode

	//method buat baca data pada node xml
	protected void bacaDataXML(String fileXMLdata){
	try {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse (new File(fileXMLdata));
		
		// normalize text representation
		doc.getDocumentElement ().normalize ();
		System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
		
		NodeList listOfTransactions = doc.getElementsByTagName("transaction");
		int totalTransactions = listOfTransactions.getLength();
		System.out.println("Total no of transaction : " + totalTransactions);
		
		for(int s=0; s<listOfTransactions.getLength() ; s++){
			
			Node firstPersonNode = listOfTransactions.item(s);
			if(firstPersonNode.getNodeType() == Node.ELEMENT_NODE){
				Element firstPersonElement = (Element)firstPersonNode;
				bacaNode(firstPersonElement, "receiptNo");
				bacaNode(firstPersonElement, "date");
				bacaNode(firstPersonElement, "time");
				bacaNode(firstPersonElement, "endTime");
				bacaNode(firstPersonElement, "registerNo");
				bacaNode(firstPersonElement, "postype");
				bacaNode(firstPersonElement, "redemptionno");
				bacaNode(firstPersonElement, "notenders");

				//------
				NodeList listOfItems = firstPersonElement.getElementsByTagName("item");
				int totalItems = listOfItems.getLength();
				System.out.println("Total items \t: " + totalItems);
				for(int p=0; p<listOfItems.getLength() ; p++){

					Node firstItemNode = listOfItems.item(p);
					if(firstItemNode.getNodeType() == Node.ELEMENT_NODE){
						Element firstItemElement = (Element)firstItemNode;
						bacaNode(firstItemElement, "menuitemno");
						bacaNode(firstItemElement, "price");
						bacaNode(firstItemElement, "qty");
					} //end if clause

				} //end loop with p var


			}//end of if clause
			
		}//end of for loop with s var

	}catch (SAXParseException err) {
		System.out.println ("** Parsing error" + ", line " 
			+ err.getLineNumber () + ", uri " + err.getSystemId ());
		System.out.println(" " + err.getMessage ());
	}catch (SAXException e) {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	}catch (Throwable t) {
		t.printStackTrace ();
	} //System.exit (0);

	}//end of bacaDataXML
    
}