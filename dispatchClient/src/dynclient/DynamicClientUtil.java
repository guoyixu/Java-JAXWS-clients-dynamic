package dynclient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

/*
 * Utility class for marshaling and de-marshaling between Xml string <-> Xml DOM <-> Java value objects
 */
public class DynamicClientUtil {
	
	// load an XML string -> w3c DOM tree 
	//
	public static Document buildDocument(String xmlFileName) throws Exception
	{
		return buildDocument(new File(xmlFileName));
		
	}
	public static Document buildDocument(File xmlFile) throws Exception
	{
		return buildDocument(new FileInputStream(xmlFile));
	}
	
	public static Document buildDocument(InputStream is) throws Exception
	{
		return buildDocument(new InputStreamReader(is));
	}
	
	public static Document buildDocument(Reader isr) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document document = builder.parse(new InputSource(isr));
		return document;
	}
	
	// convert XML Dom -> a XML string via L3 DOM
	//
	public static String serializeDoc(Document document) throws Exception
	{
		DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
		DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
		StringWriter writer = new StringWriter();
		LSOutput domOutput = impl.createLSOutput();
		domOutput.setEncoding("UTF-8");
		domOutput.setCharacterStream(writer);
		LSSerializer serializer = impl.createLSSerializer();
		serializer.getDomConfig().setParameter("format-pretty-print", new Boolean(true));
		serializer.write(document, domOutput);

		return writer.toString();
	}
	
	// convert a XML string to a w3c DOM tree
	public static Document deserializeString(String xmlString) throws Exception
	{
		return buildDocument(new StringReader(xmlString));
	}
	
	// get a formatted XML string
	//
	public String formatXMLString(File xmlFile) throws Exception
	{
		return formatXMLString(new BufferedReader(new FileReader(xmlFile)));
	}

	public String formatXMLString(String xml) throws Exception
	{
		return formatXMLString(new StringReader(xml));
	}
	
	public String formatXMLString(InputStream is) throws Exception
	{
		return formatXMLString(new InputStreamReader(is));
	}
	
	public String formatXMLString(Reader isr) throws Exception
	{
		StringWriter stringWriter = new StringWriter();
		StreamResult streamResult = new StreamResult(stringWriter);

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		
		Source source = new StreamSource(isr);
		transformer.transform(source, streamResult);

		return stringWriter.toString();
	}
	
	// From Source to Document or XML string
	//
	public static Document sourceToDocument(Source source) throws Exception
	{
		DOMResult domResult = new DOMResult();
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(source, domResult);
		Document document = (Document) domResult.getNode();

		return document;
	}
	
	public static String sourceToXMLString(Source source) throws Exception
	{
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		
		OutputStream out = new ByteArrayOutputStream();
		StreamResult streamResult = new StreamResult();
		streamResult.setOutputStream(out);
		
		transformer.transform(source, streamResult);
		String xmlResult = streamResult.getOutputStream().toString();
		
		return xmlResult;
	}
	
}
