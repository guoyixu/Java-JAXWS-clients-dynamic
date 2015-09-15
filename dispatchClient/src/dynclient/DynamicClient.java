package dynclient;

import java.io.StringReader;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

public class DynamicClient {

	private Dispatch<Source> _sourceDispatch;
	
	// Constructor
	// ==========
	
	public DynamicClient(String targetNamespace, String serviceName, String portName, String serviceURL) throws Exception
	{
		QName serviceQName = new QName(targetNamespace, serviceName);
		Service service = Service.create(serviceQName);

		URL TESTENTRYPOINTSERVICE_WSDL_LOCATION = new URL(serviceURL + "?wsdl");
		QName portQName = new QName(targetNamespace, portName);
		String endpointAddress = TESTENTRYPOINTSERVICE_WSDL_LOCATION.toString();
		service.addPort(portQName, SOAPBinding.SOAP11HTTP_BINDING, endpointAddress);

		_sourceDispatch = service.createDispatch(portQName,	Source.class, Service.Mode.PAYLOAD);
	}
	
	
	// call the target web service
	// ===========================
	
	String invoke(String xmlRequest) throws Exception
	{
		Source result = _sourceDispatch.invoke(new StreamSource(new StringReader(xmlRequest)));
		return DynamicClientUtil.sourceToXMLString(result);
	}
	
	void trace(String m)
	{
		System.out.println(m);
	}
}

