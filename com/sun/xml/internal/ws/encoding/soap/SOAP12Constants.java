/*    */ package com.sun.xml.internal.ws.encoding.soap;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class SOAP12Constants
/*    */ {
/*    */   public static final String URI_ENVELOPE = "http://www.w3.org/2003/05/soap-envelope";
/*    */   public static final String URI_ENCODING = "http://schemas.xmlsoap.org/soap/encoding/";
/*    */   public static final String URI_HTTP = "http://www.w3.org/2003/05/soap/bindings/HTTP/";
/*    */   public static final String URI_SOAP_RPC = "http://www.w3.org/2002/06/soap-rpc";
/* 44 */   public static final QName QNAME_SOAP_RPC = new QName("http://www.w3.org/2002/06/soap-rpc", "rpc");
/* 45 */   public static final QName QNAME_SOAP_RESULT = new QName("http://www.w3.org/2002/06/soap-rpc", "result");
/*    */ 
/* 47 */   public static final QName QNAME_SOAP_ENVELOPE = new QName("http://www.w3.org/2003/05/soap-envelope", "Envelope");
/* 48 */   public static final QName QNAME_SOAP_BODY = new QName("http://www.w3.org/2003/05/soap-envelope", "Body");
/* 49 */   public static final QName QNAME_SOAP_HEADER = new QName("http://www.w3.org/2003/05/soap-envelope", "Header");
/* 50 */   public static final QName QNAME_ENVELOPE_ENCODINGSTYLE = new QName("http://www.w3.org/2003/05/soap-envelope", "encodingStyle");
/* 51 */   public static final QName QNAME_SOAP_FAULT = new QName("http://www.w3.org/2003/05/soap-envelope", "Fault");
/* 52 */   public static final QName QNAME_MUSTUNDERSTAND = new QName("http://www.w3.org/2003/05/soap-envelope", "mustUnderstand");
/* 53 */   public static final QName QNAME_ROLE = new QName("http://www.w3.org/2003/05/soap-envelope", "role");
/*    */ 
/* 55 */   public static final QName QNAME_NOT_UNDERSTOOD = new QName("http://www.w3.org/2003/05/soap-envelope", "NotUnderstood");
/*    */ 
/* 58 */   public static final QName QNAME_FAULT_CODE = new QName("http://www.w3.org/2003/05/soap-envelope", "Code");
/* 59 */   public static final QName QNAME_FAULT_SUBCODE = new QName("http://www.w3.org/2003/05/soap-envelope", "Subcode");
/* 60 */   public static final QName QNAME_FAULT_VALUE = new QName("http://www.w3.org/2003/05/soap-envelope", "Value");
/* 61 */   public static final QName QNAME_FAULT_REASON = new QName("http://www.w3.org/2003/05/soap-envelope", "Reason");
/* 62 */   public static final QName QNAME_FAULT_NODE = new QName("http://www.w3.org/2003/05/soap-envelope", "Node");
/* 63 */   public static final QName QNAME_FAULT_ROLE = new QName("http://www.w3.org/2003/05/soap-envelope", "Role");
/* 64 */   public static final QName QNAME_FAULT_DETAIL = new QName("http://www.w3.org/2003/05/soap-envelope", "Detail");
/* 65 */   public static final QName QNAME_FAULT_REASON_TEXT = new QName("http://www.w3.org/2003/05/soap-envelope", "Text");
/* 66 */   public static final QName QNAME_UPGRADE = new QName("http://www.w3.org/2003/05/soap-envelope", "Upgrade");
/* 67 */   public static final QName QNAME_UPGRADE_SUPPORTED_ENVELOPE = new QName("http://www.w3.org/2003/05/soap-envelope", "SupportedEnvelope");
/*    */ 
/* 71 */   public static final QName FAULT_CODE_MUST_UNDERSTAND = new QName("http://www.w3.org/2003/05/soap-envelope", "MustUnderstand");
/* 72 */   public static final QName FAULT_CODE_MISUNDERSTOOD = new QName("http://www.w3.org/2003/05/soap-envelope", "Misunderstood");
/* 73 */   public static final QName FAULT_CODE_VERSION_MISMATCH = new QName("http://www.w3.org/2003/05/soap-envelope", "VersionMismatch");
/* 74 */   public static final QName FAULT_CODE_DATA_ENCODING_UNKNOWN = new QName("http://www.w3.org/2003/05/soap-envelope", "DataEncodingUnknown");
/* 75 */   public static final QName FAULT_CODE_PROCEDURE_NOT_PRESENT = new QName("http://www.w3.org/2003/05/soap-envelope", "ProcedureNotPresent");
/* 76 */   public static final QName FAULT_CODE_BAD_ARGUMENTS = new QName("http://www.w3.org/2003/05/soap-envelope", "BadArguments");
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.soap.SOAP12Constants
 * JD-Core Version:    0.6.2
 */