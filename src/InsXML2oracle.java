//java -classpath "lib\*;." InsXML2oracle test.xml

import java.io.File;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 

public class InsXML2oracle{

	// Constructor
	public InsXML2oracle () throws Exception {
	}
    
	public static void main (String argv[]) throws Exception {
		String fileXML;
		if (argv.length > 0)
			{fileXML = String.valueOf(argv[0]);}
			else {
			System.out.println("please insert xml fie \nUsage\t: java ReadXML somexmlfilename.xml \nUsing\t: test.xml (default parameter)\n");
			//fileXML="test.xml";}
			fileXML="test.xml";}
		InsXML2oracle insXML2oracle = new InsXML2oracle();
		//OracleConnection db = new OracleConnection();
		
		insXML2oracle.bacaDataXML(fileXML);

	}

	//method buat baca node xml
	public String bacaNode(Element firstPersonElement, String namaNode) {
		//-------
		NodeList namaNodeList = firstPersonElement.getElementsByTagName(namaNode);
		Element namaNodeElement = (Element)namaNodeList.item(0);
		if ((namaNodeElement)!=null) { // dibikin if karena ada data yang null, pas mo diparsing dari sini ke method insert, datanya error krn null
			NodeList textNamaNodeList = namaNodeElement.getChildNodes();
			String nodeValue = ((Node)textNamaNodeList.item(0)).getNodeValue().trim();
			return nodeValue;
		} else {return "0";}
	}//end of bacaNode

	//method buat baca data pada node xml
	protected void bacaDataXML(String fileXMLdata)throws Exception{
	OracleConnection db = new OracleConnection();
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
				
				String n1="'"+bacaNode(firstPersonElement, "receiptNo")+"'";
				String n2="to_date('"+(
				bacaNode(firstPersonElement, "date")+" "+
				bacaNode(firstPersonElement, "time"))+"','yyyymmdd hh24:mi:ss')";
				String n3="to_date('"+(
				bacaNode(firstPersonElement, "date")+" "+
				bacaNode(firstPersonElement, "endTime").substring(0,2)+":"+
				bacaNode(firstPersonElement, "endTime").substring(2,4)+":"+
				bacaNode(firstPersonElement, "endTime").substring(4,6))+"','yyyymmdd hh24:mi:ss')";
				String n4="'"+bacaNode(firstPersonElement, "registerNo")+"'";
				String n5="'"+bacaNode(firstPersonElement, "postype")+"'";
				String n6="'"+bacaNode(firstPersonElement, "buffer")+"'";
				String n7=bacaNode(firstPersonElement, "taxamt");
				String n8=bacaNode(firstPersonElement, "totalamt");
				String n9="'"+bacaNode(firstPersonElement, "type")+"'";
				String n10="'"+bacaNode(firstPersonElement, "saletype")+"'";
				String n11="'"+bacaNode(firstPersonElement, "noitems")+"'";
				//String n12="'"+bacaNode(firstPersonElement, "transactionid")+"'";
				String n13="'"+bacaNode(firstPersonElement, "loyaltycardid")+"'";
				String n14="'"+bacaNode(firstPersonElement, "storeid")+"'";
				String n15="'"+bacaNode(firstPersonElement, "redemptionno")+"'";
				String n16="'"+bacaNode(firstPersonElement, "notenders")+"'";
				String n17="'"+bacaNode(firstPersonElement, "tendertype")+"'";
				String n18=bacaNode(firstPersonElement, "tenderamt");

				//------
				NodeList listOfItems = firstPersonElement.getElementsByTagName("item");
				int totalItems = listOfItems.getLength();
				System.out.println("Total items \t: " + totalItems + "\n");
				for(int p=0; p<listOfItems.getLength() ; p++){

					Node firstItemNode = listOfItems.item(p);
					if(firstItemNode.getNodeType() == Node.ELEMENT_NODE){
						Element firstItemElement = (Element)firstItemNode;
						//String nurut=p;
						String n19="'"+bacaNode(firstItemElement, "menuitemno")+"'";
						String n20=bacaNode(firstItemElement, "price");
						String n21=bacaNode(firstItemElement, "qty");
						String n22="'"+bacaNode(firstPersonElement, "itemtype")+"'";
						String n23=bacaNode(firstPersonElement, "promqty");
						String n24=bacaNode(firstPersonElement, "voidqty");
						String n25="'"+bacaNode(firstPersonElement, "baseplu")+"'";

						//String insval=(n1+",'"+n2+"','"+n4+"','"+n5+"','"+n6+"',"+n7+","+n8+",'"+n9+"','"+n10+"','"+n11+"','"+n12+"','','"+n14+"','"+n15+"','"+n16+"','"+n17+"',"+n18+",'"+n19+"',"+n20+","+n21+",'"+n22+"',"+n23+","+n24+",'"+n25+"',"+n26);
						//String insval=(n1+","+(p+1)+","+n2+","+n3+","+n4+","+n5+","+n6+","+n7+","+n8+","+n9+","+n10+","+n11+",'',"+n13+","+n14+","+n15+","+n16+","+n17+","+n18+","+n19+","+n20+","+n21+","+n22+","+n23+","+n24+","+n25+",'',sysdate,'InsXML2oracle.class','','','',''");
						String insval=(n1+","+(p+1)+","+n2+","+n3+","+n4+","+n5+","+n6+","+n7+","+n8+","+n9+","+n10+","+n11+",'',"+n13+","+n14+","+n15+","+n16+","+n17+","+n18+","+n19+","+n20+","+n21+","+n22+","+n23+","+n24+","+n25+",'',sysdate,'"+fileXMLdata+"','','','',''");
						System.out.println(insval);
						db.update("INSERT INTO T_SALEPOS_D VALUES(" + insval + ")");
						
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

	db.closeConnection();
	}//end of bacaDataXML
    
}