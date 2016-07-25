/*     */ package com.sun.xml.internal.ws.model.wsdl;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
/*     */ import com.sun.xml.internal.ws.resources.ClientMessages;
/*     */ import com.sun.xml.internal.ws.util.exception.LocatableWebServiceException;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public final class WSDLPortImpl extends AbstractFeaturedObjectImpl
/*     */   implements WSDLPort
/*     */ {
/*     */   private final QName name;
/*     */   private EndpointAddress address;
/*     */   private final QName bindingName;
/*     */   private final WSDLServiceImpl owner;
/*     */   private WSEndpointReference epr;
/*     */   private WSDLBoundPortTypeImpl boundPortType;
/*     */ 
/*     */   public WSDLPortImpl(XMLStreamReader xsr, WSDLServiceImpl owner, QName name, QName binding)
/*     */   {
/*  60 */     super(xsr);
/*  61 */     this.owner = owner;
/*  62 */     this.name = name;
/*  63 */     this.bindingName = binding;
/*     */   }
/*     */ 
/*     */   public QName getName() {
/*  67 */     return this.name;
/*     */   }
/*     */ 
/*     */   public QName getBindingName() {
/*  71 */     return this.bindingName;
/*     */   }
/*     */ 
/*     */   public EndpointAddress getAddress() {
/*  75 */     return this.address;
/*     */   }
/*     */ 
/*     */   public WSDLServiceImpl getOwner() {
/*  79 */     return this.owner;
/*     */   }
/*     */ 
/*     */   public void setAddress(EndpointAddress address)
/*     */   {
/*  86 */     assert (address != null);
/*  87 */     this.address = address;
/*     */   }
/*     */ 
/*     */   public void setEPR(@NotNull WSEndpointReference epr)
/*     */   {
/*  94 */     assert (epr != null);
/*  95 */     addExtension(epr);
/*  96 */     this.epr = epr;
/*     */   }
/*     */   @Nullable
/*     */   public WSEndpointReference getEPR() {
/* 100 */     return this.epr;
/*     */   }
/*     */   public WSDLBoundPortTypeImpl getBinding() {
/* 103 */     return this.boundPortType;
/*     */   }
/*     */ 
/*     */   public SOAPVersion getSOAPVersion() {
/* 107 */     return this.boundPortType.getSOAPVersion();
/*     */   }
/*     */ 
/*     */   void freeze(WSDLModelImpl root) {
/* 111 */     this.boundPortType = root.getBinding(this.bindingName);
/* 112 */     if (this.boundPortType == null) {
/* 113 */       throw new LocatableWebServiceException(ClientMessages.UNDEFINED_BINDING(this.bindingName), new Locator[] { getLocation() });
/*     */     }
/*     */ 
/* 116 */     if (this.features == null)
/* 117 */       this.features = new WebServiceFeatureList();
/* 118 */     this.features.setParentFeaturedObject(this.boundPortType);
/* 119 */     this.notUnderstoodExtensions.addAll(this.boundPortType.notUnderstoodExtensions);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLPortImpl
 * JD-Core Version:    0.6.2
 */