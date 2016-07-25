/*    */ package com.sun.xml.internal.ws.wsdl.parser;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public abstract interface WSDLConstants
/*    */ {
/*    */   public static final String PREFIX_NS_WSDL = "wsdl";
/*    */   public static final String NS_XMLNS = "http://www.w3.org/2001/XMLSchema";
/*    */   public static final String NS_WSDL = "http://schemas.xmlsoap.org/wsdl/";
/*    */   public static final String NS_SOAP11_HTTP_BINDING = "http://schemas.xmlsoap.org/soap/http";
/* 43 */   public static final QName QNAME_SCHEMA = new QName("http://www.w3.org/2001/XMLSchema", "schema");
/*    */ 
/* 46 */   public static final QName QNAME_BINDING = new QName("http://schemas.xmlsoap.org/wsdl/", "binding");
/* 47 */   public static final QName QNAME_DEFINITIONS = new QName("http://schemas.xmlsoap.org/wsdl/", "definitions");
/* 48 */   public static final QName QNAME_DOCUMENTATION = new QName("http://schemas.xmlsoap.org/wsdl/", "documentation");
/* 49 */   public static final QName NS_SOAP_BINDING_ADDRESS = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "address");
/*    */ 
/* 51 */   public static final QName NS_SOAP_BINDING = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "binding");
/*    */ 
/* 53 */   public static final QName NS_SOAP12_BINDING = new QName("http://schemas.xmlsoap.org/wsdl/soap12/", "binding");
/*    */ 
/* 55 */   public static final QName NS_SOAP12_BINDING_ADDRESS = new QName("http://schemas.xmlsoap.org/wsdl/soap12/", "address");
/*    */ 
/* 59 */   public static final QName QNAME_IMPORT = new QName("http://schemas.xmlsoap.org/wsdl/", "import");
/*    */ 
/* 62 */   public static final QName QNAME_MESSAGE = new QName("http://schemas.xmlsoap.org/wsdl/", "message");
/* 63 */   public static final QName QNAME_PART = new QName("http://schemas.xmlsoap.org/wsdl/", "part");
/* 64 */   public static final QName QNAME_OPERATION = new QName("http://schemas.xmlsoap.org/wsdl/", "operation");
/* 65 */   public static final QName QNAME_INPUT = new QName("http://schemas.xmlsoap.org/wsdl/", "input");
/* 66 */   public static final QName QNAME_OUTPUT = new QName("http://schemas.xmlsoap.org/wsdl/", "output");
/*    */ 
/* 70 */   public static final QName QNAME_PORT = new QName("http://schemas.xmlsoap.org/wsdl/", "port");
/* 71 */   public static final QName QNAME_ADDRESS = new QName("http://schemas.xmlsoap.org/wsdl/", "address");
/* 72 */   public static final QName QNAME_PORT_TYPE = new QName("http://schemas.xmlsoap.org/wsdl/", "portType");
/* 73 */   public static final QName QNAME_FAULT = new QName("http://schemas.xmlsoap.org/wsdl/", "fault");
/* 74 */   public static final QName QNAME_SERVICE = new QName("http://schemas.xmlsoap.org/wsdl/", "service");
/* 75 */   public static final QName QNAME_TYPES = new QName("http://schemas.xmlsoap.org/wsdl/", "types");
/*    */   public static final String ATTR_TRANSPORT = "transport";
/*    */   public static final String ATTR_LOCATION = "location";
/*    */   public static final String ATTR_NAME = "name";
/*    */   public static final String ATTR_TNS = "targetNamespace";
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.parser.WSDLConstants
 * JD-Core Version:    0.6.2
 */