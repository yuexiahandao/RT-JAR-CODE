/*    */ package com.sun.xml.internal.ws.wsdl.parser;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ abstract interface MIMEConstants
/*    */ {
/*    */   public static final String NS_WSDL_MIME = "http://schemas.xmlsoap.org/wsdl/mime/";
/* 36 */   public static final QName QNAME_CONTENT = new QName("http://schemas.xmlsoap.org/wsdl/mime/", "content");
/* 37 */   public static final QName QNAME_MULTIPART_RELATED = new QName("http://schemas.xmlsoap.org/wsdl/mime/", "multipartRelated");
/*    */ 
/* 39 */   public static final QName QNAME_PART = new QName("http://schemas.xmlsoap.org/wsdl/mime/", "part");
/* 40 */   public static final QName QNAME_MIME_XML = new QName("http://schemas.xmlsoap.org/wsdl/mime/", "mimeXml");
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.parser.MIMEConstants
 * JD-Core Version:    0.6.2
 */