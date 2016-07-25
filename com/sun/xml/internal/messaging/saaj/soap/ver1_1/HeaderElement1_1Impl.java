/*     */ package com.sun.xml.internal.messaging.saaj.soap.ver1_1;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Name;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPException;
/*     */ 
/*     */ public class HeaderElement1_1Impl extends HeaderElementImpl
/*     */ {
/*  47 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap.ver1_1", "com.sun.xml.internal.messaging.saaj.soap.ver1_1.LocalStrings");
/*     */ 
/*     */   public HeaderElement1_1Impl(SOAPDocumentImpl ownerDoc, Name qname)
/*     */   {
/*  52 */     super(ownerDoc, qname);
/*     */   }
/*     */   public HeaderElement1_1Impl(SOAPDocumentImpl ownerDoc, QName qname) {
/*  55 */     super(ownerDoc, qname);
/*     */   }
/*     */ 
/*     */   public SOAPElement setElementQName(QName newName) throws SOAPException {
/*  59 */     HeaderElementImpl copy = new HeaderElement1_1Impl((SOAPDocumentImpl)getOwnerDocument(), newName);
/*     */ 
/*  61 */     return replaceElementWithSOAPElement(this, copy);
/*     */   }
/*     */ 
/*     */   protected NameImpl getActorAttributeName() {
/*  65 */     return NameImpl.create("actor", null, "http://schemas.xmlsoap.org/soap/envelope/");
/*     */   }
/*     */ 
/*     */   protected NameImpl getRoleAttributeName()
/*     */   {
/*  70 */     log.log(Level.SEVERE, "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1", new String[] { "Role" });
/*     */ 
/*  74 */     throw new UnsupportedOperationException("Role not supported by SOAP 1.1");
/*     */   }
/*     */ 
/*     */   protected NameImpl getMustunderstandAttributeName() {
/*  78 */     return NameImpl.create("mustUnderstand", null, "http://schemas.xmlsoap.org/soap/envelope/");
/*     */   }
/*     */ 
/*     */   protected String getMustunderstandLiteralValue(boolean mustUnderstand)
/*     */   {
/*  83 */     return mustUnderstand == true ? "1" : "0";
/*     */   }
/*     */ 
/*     */   protected boolean getMustunderstandAttributeValue(String mu) {
/*  87 */     if (("1".equals(mu)) || ("true".equalsIgnoreCase(mu)))
/*  88 */       return true;
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */   protected NameImpl getRelayAttributeName()
/*     */   {
/*  94 */     log.log(Level.SEVERE, "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1", new String[] { "Relay" });
/*     */ 
/*  98 */     throw new UnsupportedOperationException("Relay not supported by SOAP 1.1");
/*     */   }
/*     */ 
/*     */   protected String getRelayLiteralValue(boolean relayAttr) {
/* 102 */     log.log(Level.SEVERE, "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1", new String[] { "Relay" });
/*     */ 
/* 106 */     throw new UnsupportedOperationException("Relay not supported by SOAP 1.1");
/*     */   }
/*     */ 
/*     */   protected boolean getRelayAttributeValue(String mu) {
/* 110 */     log.log(Level.SEVERE, "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1", new String[] { "Relay" });
/*     */ 
/* 114 */     throw new UnsupportedOperationException("Relay not supported by SOAP 1.1");
/*     */   }
/*     */ 
/*     */   protected String getActorOrRole() {
/* 118 */     return getActor();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.ver1_1.HeaderElement1_1Impl
 * JD-Core Version:    0.6.2
 */