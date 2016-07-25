/*    */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*    */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*    */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
/*    */ import javax.xml.transform.stream.StreamSource;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ final class Util
/*    */ {
/*    */   public static final XMLInputSource toXMLInputSource(StreamSource in)
/*    */   {
/* 44 */     if (in.getReader() != null) {
/* 45 */       return new XMLInputSource(in.getPublicId(), in.getSystemId(), in.getSystemId(), in.getReader(), null);
/*    */     }
/*    */ 
/* 48 */     if (in.getInputStream() != null) {
/* 49 */       return new XMLInputSource(in.getPublicId(), in.getSystemId(), in.getSystemId(), in.getInputStream(), null);
/*    */     }
/*    */ 
/* 53 */     return new XMLInputSource(in.getPublicId(), in.getSystemId(), in.getSystemId());
/*    */   }
/*    */ 
/*    */   public static SAXException toSAXException(XNIException e)
/*    */   {
/* 61 */     if ((e instanceof XMLParseException))
/* 62 */       return toSAXParseException((XMLParseException)e);
/* 63 */     if ((e.getException() instanceof SAXException))
/* 64 */       return (SAXException)e.getException();
/* 65 */     return new SAXException(e.getMessage(), e.getException());
/*    */   }
/*    */ 
/*    */   public static SAXParseException toSAXParseException(XMLParseException e) {
/* 69 */     if ((e.getException() instanceof SAXParseException))
/* 70 */       return (SAXParseException)e.getException();
/* 71 */     return new SAXParseException(e.getMessage(), e.getPublicId(), e.getExpandedSystemId(), e.getLineNumber(), e.getColumnNumber(), e.getException());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.Util
 * JD-Core Version:    0.6.2
 */