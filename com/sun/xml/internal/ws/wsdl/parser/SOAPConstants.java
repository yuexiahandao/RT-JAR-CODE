/*    */ package com.sun.xml.internal.ws.wsdl.parser;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public abstract interface SOAPConstants
/*    */ {
/*    */   public static final String URI_ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/";
/*    */   public static final String URI_ENVELOPE12 = "http://www.w3.org/2003/05/soap-envelope";
/*    */   public static final String NS_WSDL_SOAP = "http://schemas.xmlsoap.org/wsdl/soap/";
/*    */   public static final String NS_WSDL_SOAP12 = "http://schemas.xmlsoap.org/wsdl/soap12/";
/*    */   public static final String NS_SOAP_ENCODING = "http://schemas.xmlsoap.org/soap/encoding/";
/*    */   public static final String URI_SOAP_TRANSPORT_HTTP = "http://schemas.xmlsoap.org/soap/http";
/* 52 */   public static final QName QNAME_ADDRESS = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "address");
/*    */ 
/* 54 */   public static final QName QNAME_SOAP12ADDRESS = new QName("http://schemas.xmlsoap.org/wsdl/soap12/", "address");
/*    */ 
/* 56 */   public static final QName QNAME_BINDING = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "binding");
/*    */ 
/* 58 */   public static final QName QNAME_BODY = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "body");
/* 59 */   public static final QName QNAME_SOAP12BODY = new QName("http://schemas.xmlsoap.org/wsdl/soap12/", "body");
/* 60 */   public static final QName QNAME_FAULT = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "fault");
/* 61 */   public static final QName QNAME_HEADER = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "header");
/* 62 */   public static final QName QNAME_SOAP12HEADER = new QName("http://schemas.xmlsoap.org/wsdl/soap12/", "header");
/* 63 */   public static final QName QNAME_HEADERFAULT = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "headerfault");
/*    */ 
/* 65 */   public static final QName QNAME_OPERATION = new QName("http://schemas.xmlsoap.org/wsdl/soap/", "operation");
/*    */ 
/* 67 */   public static final QName QNAME_SOAP12OPERATION = new QName("http://schemas.xmlsoap.org/wsdl/soap12/", "operation");
/*    */ 
/* 69 */   public static final QName QNAME_MUSTUNDERSTAND = new QName("http://schemas.xmlsoap.org/soap/envelope/", "mustUnderstand");
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.parser.SOAPConstants
 * JD-Core Version:    0.6.2
 */