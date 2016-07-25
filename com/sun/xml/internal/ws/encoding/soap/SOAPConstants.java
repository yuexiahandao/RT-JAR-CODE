/*    */ package com.sun.xml.internal.ws.encoding.soap;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class SOAPConstants
/*    */ {
/*    */   public static final String URI_ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/";
/*    */   public static final String URI_HTTP = "http://schemas.xmlsoap.org/soap/http";
/*    */   public static final String URI_ENCODING = "http://schemas.xmlsoap.org/soap/encoding/";
/*    */   public static final String NS_WSDL_SOAP = "http://schemas.xmlsoap.org/wsdl/soap/";
/* 42 */   public static final QName QNAME_ENVELOPE_ENCODINGSTYLE = new QName("http://schemas.xmlsoap.org/soap/envelope/", "encodingStyle");
/*    */ 
/* 44 */   public static final QName QNAME_SOAP_ENVELOPE = new QName("http://schemas.xmlsoap.org/soap/envelope/", "Envelope");
/* 45 */   public static final QName QNAME_SOAP_HEADER = new QName("http://schemas.xmlsoap.org/soap/envelope/", "Header");
/* 46 */   public static final QName QNAME_MUSTUNDERSTAND = new QName("http://schemas.xmlsoap.org/soap/envelope/", "mustUnderstand");
/* 47 */   public static final QName QNAME_ROLE = new QName("http://schemas.xmlsoap.org/soap/envelope/", "actor");
/* 48 */   public static final QName QNAME_SOAP_BODY = new QName("http://schemas.xmlsoap.org/soap/envelope/", "Body");
/* 49 */   public static final QName QNAME_SOAP_FAULT = new QName("http://schemas.xmlsoap.org/soap/envelope/", "Fault");
/* 50 */   public static final QName QNAME_SOAP_FAULT_CODE = new QName("", "faultcode");
/* 51 */   public static final QName QNAME_SOAP_FAULT_STRING = new QName("", "faultstring");
/* 52 */   public static final QName QNAME_SOAP_FAULT_ACTOR = new QName("", "faultactor");
/* 53 */   public static final QName QNAME_SOAP_FAULT_DETAIL = new QName("", "detail");
/* 54 */   public static final QName FAULT_CODE_MUST_UNDERSTAND = new QName("http://schemas.xmlsoap.org/soap/envelope/", "MustUnderstand");
/*    */ 
/* 56 */   public static final QName FAULT_CODE_VERSION_MISMATCH = new QName("http://schemas.xmlsoap.org/soap/envelope/", "VersionMismatch");
/* 57 */   public static final QName FAULT_CODE_DATA_ENCODING_UNKNOWN = new QName("http://schemas.xmlsoap.org/soap/envelope/", "DataEncodingUnknown");
/* 58 */   public static final QName FAULT_CODE_PROCEDURE_NOT_PRESENT = new QName("http://schemas.xmlsoap.org/soap/envelope/", "ProcedureNotPresent");
/* 59 */   public static final QName FAULT_CODE_BAD_ARGUMENTS = new QName("http://schemas.xmlsoap.org/soap/envelope/", "BadArguments");
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.soap.SOAPConstants
 * JD-Core Version:    0.6.2
 */