package com.webService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.webService package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _SendTestResponse_QNAME = new QName(
			"http://impl.queryMap.cxf.control.ws.com/", "sendTestResponse");
	private final static QName _SendTest_QNAME = new QName(
			"http://impl.queryMap.cxf.control.ws.com/", "sendTest");
	private final static QName _SendAlertResponse_QNAME = new QName(
			"http://impl.queryMap.cxf.control.ws.com/", "sendAlertResponse");
	private final static QName _SendAlert_QNAME = new QName(
			"http://impl.queryMap.cxf.control.ws.com/", "sendAlert");
	private final static QName _QueryMapEntity_QNAME = new QName(
			"http://impl.queryMap.cxf.control.ws.com/", "QueryMapEntity");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.webService
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link SendTest }
	 * 
	 */
	public SendTest createSendTest() {
		return new SendTest();
	}

	/**
	 * Create an instance of {@link SendAlertResponse }
	 * 
	 */
	public SendAlertResponse createSendAlertResponse() {
		return new SendAlertResponse();
	}

	/**
	 * Create an instance of {@link SendAlert }
	 * 
	 */
	public SendAlert createSendAlert() {
		return new SendAlert();
	}

	/**
	 * Create an instance of {@link QueryMapEntity }
	 * 
	 */
	public QueryMapEntity createQueryMapEntity() {
		return new QueryMapEntity();
	}

	/**
	 * Create an instance of {@link SendTestResponse }
	 * 
	 */
	public SendTestResponse createSendTestResponse() {
		return new SendTestResponse();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SendTestResponse }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://impl.queryMap.cxf.control.ws.com/", name = "sendTestResponse")
	public JAXBElement<SendTestResponse> createSendTestResponse(
			SendTestResponse value) {
		return new JAXBElement<SendTestResponse>(_SendTestResponse_QNAME,
				SendTestResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SendTest }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://impl.queryMap.cxf.control.ws.com/", name = "sendTest")
	public JAXBElement<SendTest> createSendTest(SendTest value) {
		return new JAXBElement<SendTest>(_SendTest_QNAME, SendTest.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SendAlertResponse }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://impl.queryMap.cxf.control.ws.com/", name = "sendAlertResponse")
	public JAXBElement<SendAlertResponse> createSendAlertResponse(
			SendAlertResponse value) {
		return new JAXBElement<SendAlertResponse>(_SendAlertResponse_QNAME,
				SendAlertResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SendAlert }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://impl.queryMap.cxf.control.ws.com/", name = "sendAlert")
	public JAXBElement<SendAlert> createSendAlert(SendAlert value) {
		return new JAXBElement<SendAlert>(_SendAlert_QNAME, SendAlert.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link QueryMapEntity }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://impl.queryMap.cxf.control.ws.com/", name = "QueryMapEntity")
	public JAXBElement<QueryMapEntity> createQueryMapEntity(QueryMapEntity value) {
		return new JAXBElement<QueryMapEntity>(_QueryMapEntity_QNAME,
				QueryMapEntity.class, null, value);
	}

}
