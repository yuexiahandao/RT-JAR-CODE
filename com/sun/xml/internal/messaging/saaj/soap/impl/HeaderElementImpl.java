/*     */ package com.sun.xml.internal.messaging.saaj.soap.impl;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Name;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPHeader;
/*     */ import javax.xml.soap.SOAPHeaderElement;
/*     */ 
/*     */ public abstract class HeaderElementImpl extends ElementImpl
/*     */   implements SOAPHeaderElement
/*     */ {
/*  38 */   protected static Name RELAY_ATTRIBUTE_LOCAL_NAME = NameImpl.createFromTagName("relay");
/*     */ 
/*  40 */   protected static Name MUST_UNDERSTAND_ATTRIBUTE_LOCAL_NAME = NameImpl.createFromTagName("mustUnderstand");
/*     */ 
/*  86 */   Name actorAttNameWithoutNS = NameImpl.createFromTagName("actor");
/*     */ 
/*  93 */   Name roleAttNameWithoutNS = NameImpl.createFromTagName("role");
/*     */ 
/*     */   public HeaderElementImpl(SOAPDocumentImpl ownerDoc, Name qname)
/*     */   {
/*  44 */     super(ownerDoc, qname);
/*     */   }
/*     */   public HeaderElementImpl(SOAPDocumentImpl ownerDoc, QName qname) {
/*  47 */     super(ownerDoc, qname); } 
/*     */   protected abstract NameImpl getActorAttributeName();
/*     */ 
/*     */   protected abstract NameImpl getRoleAttributeName();
/*     */ 
/*     */   protected abstract NameImpl getMustunderstandAttributeName();
/*     */ 
/*     */   protected abstract boolean getMustunderstandAttributeValue(String paramString);
/*     */ 
/*     */   protected abstract String getMustunderstandLiteralValue(boolean paramBoolean);
/*     */ 
/*     */   protected abstract NameImpl getRelayAttributeName();
/*     */ 
/*     */   protected abstract boolean getRelayAttributeValue(String paramString);
/*     */ 
/*     */   protected abstract String getRelayLiteralValue(boolean paramBoolean);
/*     */ 
/*     */   protected abstract String getActorOrRole();
/*     */ 
/*  62 */   public void setParentElement(SOAPElement element) throws SOAPException { if (!(element instanceof SOAPHeader)) {
/*  63 */       log.severe("SAAJ0130.impl.header.elem.parent.mustbe.header");
/*  64 */       throw new SOAPException("Parent of a SOAPHeaderElement has to be a SOAPHeader");
/*     */     }
/*     */ 
/*  67 */     super.setParentElement(element); }
/*     */ 
/*     */   public void setActor(String actorUri)
/*     */   {
/*     */     try {
/*  72 */       removeAttribute(getActorAttributeName());
/*  73 */       addAttribute(getActorAttributeName(), actorUri);
/*     */     }
/*     */     catch (SOAPException ex)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setRole(String roleUri) throws SOAPException {
/*  81 */     removeAttribute(getRoleAttributeName());
/*  82 */     addAttribute(getRoleAttributeName(), roleUri);
/*     */   }
/*     */ 
/*     */   public String getActor()
/*     */   {
/*  89 */     String actor = getAttributeValue(getActorAttributeName());
/*  90 */     return actor;
/*     */   }
/*     */ 
/*     */   public String getRole()
/*     */   {
/*  97 */     String role = getAttributeValue(getRoleAttributeName());
/*  98 */     return role;
/*     */   }
/*     */ 
/*     */   public void setMustUnderstand(boolean mustUnderstand) {
/*     */     try {
/* 103 */       removeAttribute(getMustunderstandAttributeName());
/* 104 */       addAttribute(getMustunderstandAttributeName(), getMustunderstandLiteralValue(mustUnderstand));
/*     */     }
/*     */     catch (SOAPException ex)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean getMustUnderstand() {
/* 112 */     String mu = getAttributeValue(getMustunderstandAttributeName());
/*     */ 
/* 114 */     if (mu != null) {
/* 115 */       return getMustunderstandAttributeValue(mu);
/*     */     }
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */   public void setRelay(boolean relay) throws SOAPException
/*     */   {
/* 122 */     removeAttribute(getRelayAttributeName());
/* 123 */     addAttribute(getRelayAttributeName(), getRelayLiteralValue(relay));
/*     */   }
/*     */ 
/*     */   public boolean getRelay()
/*     */   {
/* 129 */     String mu = getAttributeValue(getRelayAttributeName());
/* 130 */     if (mu != null) {
/* 131 */       return getRelayAttributeValue(mu);
/*     */     }
/* 133 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
 * JD-Core Version:    0.6.2
 */