/*    */ package com.sun.xml.internal.messaging.saaj.soap.ver1_2;
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
/*    */ public class SOAPPart1_2Impl extends SOAPPartImpl
/*    */   implements SOAPConstants
/*    */ {
/* 45 */   protected static final Logger log = Logger.getLogger(SOAPPart1_2Impl.class.getName(), "com.sun.xml.internal.messaging.saaj.soap.ver1_2.LocalStrings");
/*    */ 
/*    */   public SOAPPart1_2Impl()
/*    */   {
/*    */   }
/*    */ 
/*    */   public SOAPPart1_2Impl(MessageImpl message)
/*    */   {
/* 54 */     super(message);
/*    */   }
/*    */ 
/*    */   protected String getContentType() {
/* 58 */     return "application/soap+xml";
/*    */   }
/*    */ 
/*    */   protected Envelope createEmptyEnvelope(String prefix) throws SOAPException {
/* 62 */     return new Envelope1_2Impl(getDocument(), prefix, true, true);
/*    */   }
/*    */ 
/*    */   protected Envelope createEnvelopeFromSource() throws SOAPException {
/* 66 */     XMLDeclarationParser parser = lookForXmlDecl();
/* 67 */     Source tmp = this.source;
/* 68 */     this.source = null;
/* 69 */     EnvelopeImpl envelope = (EnvelopeImpl)EnvelopeFactory.createEnvelope(tmp, this);
/* 70 */     if (!envelope.getNamespaceURI().equals("http://www.w3.org/2003/05/soap-envelope")) {
/* 71 */       log.severe("SAAJ0415.ver1_2.msg.invalid.soap1.2");
/* 72 */       throw new SOAPException("InputStream does not represent a valid SOAP 1.2 Message");
/*    */     }
/*    */ 
/* 75 */     if ((parser != null) && 
/* 76 */       (!this.omitXmlDecl)) {
/* 77 */       envelope.setOmitXmlDecl("no");
/* 78 */       envelope.setXmlDecl(parser.getXmlDeclaration());
/* 79 */       envelope.setCharsetEncoding(parser.getEncoding());
/*    */     }
/*    */ 
/* 82 */     return envelope;
/*    */   }
/*    */ 
/*    */   protected SOAPPartImpl duplicateType()
/*    */   {
/* 87 */     return new SOAPPart1_2Impl();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_2.SOAPPart1_2Impl
 * JD-Core Version:    0.6.2
 */