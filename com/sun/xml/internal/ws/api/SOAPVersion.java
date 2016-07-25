/*     */ package com.sun.xml.internal.ws.api;
/*     */ 
/*     */ import com.sun.xml.internal.bind.util.Which;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.MessageFactory;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPFactory;
/*     */ 
/*     */ public enum SOAPVersion
/*     */ {
/*  71 */   SOAP_11("http://schemas.xmlsoap.org/wsdl/soap/http", "http://schemas.xmlsoap.org/soap/envelope/", "text/xml", "http://schemas.xmlsoap.org/soap/actor/next", "actor", "SOAP 1.1 Protocol", new QName("http://schemas.xmlsoap.org/soap/envelope/", "MustUnderstand"), "Client", "Server", Collections.singleton("http://schemas.xmlsoap.org/soap/actor/next")), 
/*     */ 
/*  81 */   SOAP_12("http://www.w3.org/2003/05/soap/bindings/HTTP/", "http://www.w3.org/2003/05/soap-envelope", "application/soap+xml", "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver", "role", "SOAP 1.2 Protocol", new QName("http://www.w3.org/2003/05/soap-envelope", "MustUnderstand"), "Sender", "Receiver", new HashSet(Arrays.asList(new String[] { "http://www.w3.org/2003/05/soap-envelope/role/next", "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver" })));
/*     */ 
/*     */   public final String httpBindingId;
/*     */   public final String nsUri;
/*     */   public final String contentType;
/*     */   public final QName faultCodeMustUnderstand;
/*     */   public final MessageFactory saajMessageFactory;
/*     */   public final SOAPFactory saajSoapFactory;
/*     */   public final String implicitRole;
/*     */   public final Set<String> implicitRoleSet;
/*     */   public final Set<String> requiredRoles;
/*     */   public final String roleAttributeName;
/*     */   public final QName faultCodeClient;
/*     */   public final QName faultCodeServer;
/*     */ 
/*     */   private SOAPVersion(String httpBindingId, String nsUri, String contentType, String implicitRole, String roleAttributeName, String saajFactoryString, QName faultCodeMustUnderstand, String faultCodeClientLocalName, String faultCodeServerLocalName, Set<String> requiredRoles)
/*     */   {
/* 158 */     this.httpBindingId = httpBindingId;
/* 159 */     this.nsUri = nsUri;
/* 160 */     this.contentType = contentType;
/* 161 */     this.implicitRole = implicitRole;
/* 162 */     this.implicitRoleSet = Collections.singleton(implicitRole);
/* 163 */     this.roleAttributeName = roleAttributeName;
/*     */     try {
/* 165 */       this.saajMessageFactory = MessageFactory.newInstance(saajFactoryString);
/* 166 */       this.saajSoapFactory = SOAPFactory.newInstance(saajFactoryString);
/*     */     } catch (SOAPException e) {
/* 168 */       throw new Error(e);
/*     */     }
/*     */     catch (NoSuchMethodError e) {
/* 171 */       LinkageError x = new LinkageError("You are loading old SAAJ from " + Which.which(MessageFactory.class));
/* 172 */       x.initCause(e);
/* 173 */       throw x;
/*     */     }
/* 175 */     this.faultCodeMustUnderstand = faultCodeMustUnderstand;
/* 176 */     this.requiredRoles = requiredRoles;
/* 177 */     this.faultCodeClient = new QName(nsUri, faultCodeClientLocalName);
/* 178 */     this.faultCodeServer = new QName(nsUri, faultCodeServerLocalName);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 183 */     return this.httpBindingId;
/*     */   }
/*     */ 
/*     */   public static SOAPVersion fromHttpBinding(String binding)
/*     */   {
/* 198 */     if (binding == null) {
/* 199 */       return SOAP_11;
/*     */     }
/* 201 */     if (binding.equals(SOAP_12.httpBindingId)) {
/* 202 */       return SOAP_12;
/*     */     }
/* 204 */     return SOAP_11;
/*     */   }
/*     */ 
/*     */   public static SOAPVersion fromNsUri(String nsUri)
/*     */   {
/* 218 */     if (nsUri.equals(SOAP_12.nsUri)) {
/* 219 */       return SOAP_12;
/*     */     }
/* 221 */     return SOAP_11;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.SOAPVersion
 * JD-Core Version:    0.6.2
 */