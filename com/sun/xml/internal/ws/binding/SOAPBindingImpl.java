/*     */ package com.sun.xml.internal.ws.binding;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.client.HandlerConfiguration;
/*     */ import com.sun.xml.internal.ws.resources.ClientMessages;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.MessageFactory;
/*     */ import javax.xml.soap.SOAPFactory;
/*     */ import javax.xml.ws.Service.Mode;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.handler.Handler;
/*     */ import javax.xml.ws.soap.MTOMFeature;
/*     */ import javax.xml.ws.soap.SOAPBinding;
/*     */ 
/*     */ public final class SOAPBindingImpl extends BindingImpl
/*     */   implements SOAPBinding
/*     */ {
/*     */   public static final String X_SOAP12HTTP_BINDING = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/";
/*     */   private static final String ROLE_NONE = "http://www.w3.org/2003/05/soap-envelope/role/none";
/*     */   protected final SOAPVersion soapVersion;
/*  64 */   private Set<QName> portKnownHeaders = Collections.emptySet();
/*  65 */   private Set<QName> bindingUnderstoodHeaders = new HashSet();
/*     */ 
/* 190 */   private static final WebServiceFeature[] EMPTY_FEATURES = new WebServiceFeature[0];
/*     */ 
/*     */   SOAPBindingImpl(BindingID bindingId)
/*     */   {
/*  71 */     this(bindingId, EMPTY_FEATURES);
/*     */   }
/*     */ 
/*     */   SOAPBindingImpl(BindingID bindingId, WebServiceFeature[] features)
/*     */   {
/*  83 */     super(bindingId);
/*  84 */     this.soapVersion = bindingId.getSOAPVersion();
/*     */ 
/*  86 */     setRoles(new HashSet());
/*     */ 
/*  90 */     setFeatures(features);
/*  91 */     this.features.addAll(bindingId.createBuiltinFeatureList());
/*  92 */     populateBindingUnderstoodHeaders();
/*     */   }
/*     */ 
/*     */   public void setPortKnownHeaders(@NotNull Set<QName> headers)
/*     */   {
/* 103 */     this.portKnownHeaders = headers;
/*     */   }
/*     */ 
/*     */   public boolean understandsHeader(QName header) {
/* 107 */     if (this.serviceMode == Service.Mode.MESSAGE)
/* 108 */       return true;
/* 109 */     if (this.portKnownHeaders.contains(header))
/* 110 */       return true;
/* 111 */     if (this.bindingUnderstoodHeaders.contains(header)) {
/* 112 */       return true;
/*     */     }
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   private void populateBindingUnderstoodHeaders()
/*     */   {
/* 122 */     AddressingVersion addressingVersion = getAddressingVersion();
/* 123 */     if (addressingVersion != null) {
/* 124 */       this.bindingUnderstoodHeaders.add(addressingVersion.actionTag);
/* 125 */       this.bindingUnderstoodHeaders.add(addressingVersion.faultToTag);
/* 126 */       this.bindingUnderstoodHeaders.add(addressingVersion.fromTag);
/* 127 */       this.bindingUnderstoodHeaders.add(addressingVersion.messageIDTag);
/* 128 */       this.bindingUnderstoodHeaders.add(addressingVersion.relatesToTag);
/* 129 */       this.bindingUnderstoodHeaders.add(addressingVersion.replyToTag);
/* 130 */       this.bindingUnderstoodHeaders.add(addressingVersion.toTag);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setHandlerChain(List<Handler> chain)
/*     */   {
/* 140 */     this.handlerConfig = new HandlerConfiguration(this.handlerConfig.getRoles(), chain);
/*     */   }
/*     */ 
/*     */   protected void addRequiredRoles(Set<String> roles) {
/* 144 */     roles.addAll(this.soapVersion.requiredRoles);
/*     */   }
/*     */ 
/*     */   public Set<String> getRoles() {
/* 148 */     return this.handlerConfig.getRoles();
/*     */   }
/*     */ 
/*     */   public void setRoles(Set<String> roles)
/*     */   {
/* 157 */     if (roles == null) {
/* 158 */       roles = new HashSet();
/*     */     }
/* 160 */     if (roles.contains("http://www.w3.org/2003/05/soap-envelope/role/none")) {
/* 161 */       throw new WebServiceException(ClientMessages.INVALID_SOAP_ROLE_NONE());
/*     */     }
/* 163 */     addRequiredRoles(roles);
/* 164 */     this.handlerConfig = new HandlerConfiguration(roles, getHandlerConfig());
/*     */   }
/*     */ 
/*     */   public boolean isMTOMEnabled()
/*     */   {
/* 172 */     return isFeatureEnabled(MTOMFeature.class);
/*     */   }
/*     */ 
/*     */   public void setMTOMEnabled(boolean b)
/*     */   {
/* 179 */     setFeatures(new WebServiceFeature[] { new MTOMFeature(b) });
/*     */   }
/*     */ 
/*     */   public SOAPFactory getSOAPFactory() {
/* 183 */     return this.soapVersion.saajSoapFactory;
/*     */   }
/*     */ 
/*     */   public MessageFactory getMessageFactory() {
/* 187 */     return this.soapVersion.saajMessageFactory;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.binding.SOAPBindingImpl
 * JD-Core Version:    0.6.2
 */