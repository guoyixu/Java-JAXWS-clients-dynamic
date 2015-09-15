package dynclient;

import org.w3c.dom.Document;

public class TestRunner {

	public static final String TARGET_NAMESPACE = "http://package.name.in.reverse.order";
	public static final String WEB_SERVICE_URL = "http://localhost:8080/TestEntryPointServer/TestEntryPointService";
	public static final String WEB_SERVICE_NAME= "TestEntryPointService";
	public static final String WEB_SERVICE_PORT_NAME = "TestEntryPointPort";
	
	public static void main(String[] args) throws Exception
	{
		if (args == null || args.length < 1) {
			usage();
			return;
		}
		
		String xmlFileName = args[0];
		
		DynamicClient client = new DynamicClient(TARGET_NAMESPACE, WEB_SERVICE_NAME, WEB_SERVICE_PORT_NAME, WEB_SERVICE_URL);
		Document requestDoc = DynamicClientUtil.buildDocument(xmlFileName);
		String xmlRequest = DynamicClientUtil.serializeDoc(requestDoc);
		String response = client.invoke(xmlRequest);
		trace(response);
	}
	
	static void usage()
	{
		trace("Warning: supply the xml data file in the first argument");
	}
	
	static void trace(String m)
	{
		System.out.println(m);
	}
}
