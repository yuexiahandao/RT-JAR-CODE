/*    */ package com.sun.xml.internal.messaging.saaj.soap.ver1_1;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.soap.Envelope;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.EnvelopeFactory;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.MessageImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPPartImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.impl.EnvelopeImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.util.XMLDeclarationParser;
/*    */ import java.util.logging.Logger;
/*    */ import javax.xml.soap.SOAPConstants;
/*    */ import javax.xml.soap.SOAPException;
/*    */ import javax.xml.transform.Source;
/*    */ 
/*    */ public class SOAPPart1_1Impl extends SOAPPartImpl
/*    */   implements SOAPConstants
/*    */ {
/* 46 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap.ver1_1", "com.sun.xml.internal.messaging.saaj.soap.ver1_1.LocalStrings");
/*    */ 
/*    */   public SOAPPart1_1Impl()
/*    */   {
/*    */   }
/*    */ 
/*    */   public SOAPPart1_1Impl(MessageImpl message)
/*    */   {
/* 55 */     super(message);
/*    */   }
/*    */ 
/*    */   protected String getContentType() {
/* 59 */     return isFastInfoset() ? "application/fastinfoset" : "text/xml";
/*    */   }
/*    */ 
/*    */   protected Envelope createEnvelopeFromSource()
/*    */     throws SOAPException
/*    */   {
/* 65 */     XMLDeclarationParser parser = lookForXmlDecl();
/* 66 */     Source tmp = this.source;
/* 67 */     this.source = null;
/* 68 */     EnvelopeImpl envelope = (EnvelopeImpl)EnvelopeFactory.createEnvelope(tmp, this);
/*    */ 
/* 71 */     if (!envelope.getNamespaceURI().equals("http://schemas.xmlsoap.org/soap/envelope/")) {
/* 72 */       log.severe("SAAJ0304.ver1_1.msg.invalid.SOAP1.1");
/* 73 */       throw new SOAPException("InputStream does not represent a valid SOAP 1.1 Message");
/*    */     }
/*    */ 
/* 76 */     if ((parser != null) && (!this.omitXmlDecl)) {
/* 77 */       envelope.setOmitXmlDecl("no");
/* 78 */       envelope.setXmlDecl(parser.getXmlDeclaration());
/* 79 */       envelope.setCharsetEncoding(parser.getEncoding());
/*    */     }
/* 81 */     return envelope;
/*    */   }
/*    */ 
/*    */   protected Envelope createEmptyEnvelope(String prefix) throws SOAPException
/*    */   {
/* 86 */     return new Envelope1_1Impl(getDocument(), prefix, true, true);
/*    */   }
/*    */ 
/*    */   protected SOAPPartImpl duplicateType() {
/* 90 */     return new SOAPPart1_1Impl();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPPart1_1Impl
 * JD-Core Version:    0.6.2
 */