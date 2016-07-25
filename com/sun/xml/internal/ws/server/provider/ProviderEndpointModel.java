/*     */ package com.sun.xml.internal.ws.server.provider;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.server.AsyncProvider;
/*     */ import com.sun.xml.internal.ws.resources.ServerMessages;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import javax.activation.DataSource;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.ws.Provider;
/*     */ import javax.xml.ws.Service.Mode;
/*     */ import javax.xml.ws.ServiceMode;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.soap.SOAPBinding;
/*     */ 
/*     */ final class ProviderEndpointModel<T>
/*     */ {
/*     */   final boolean isAsync;
/*     */ 
/*     */   @NotNull
/*     */   final Service.Mode mode;
/*     */ 
/*     */   @NotNull
/*     */   final Class datatype;
/*     */ 
/*     */   @NotNull
/*     */   final Class implClass;
/*     */ 
/*     */   ProviderEndpointModel(Class<T> implementorClass, WSBinding binding)
/*     */   {
/*  75 */     assert (implementorClass != null);
/*  76 */     assert (binding != null);
/*     */ 
/*  78 */     this.implClass = implementorClass;
/*  79 */     this.mode = getServiceMode(implementorClass);
/*  80 */     Class otherClass = (binding instanceof SOAPBinding) ? SOAPMessage.class : DataSource.class;
/*     */ 
/*  82 */     this.isAsync = AsyncProvider.class.isAssignableFrom(implementorClass);
/*     */ 
/*  85 */     Class baseType = this.isAsync ? AsyncProvider.class : Provider.class;
/*  86 */     Type baseParam = JAXBRIContext.getBaseType(implementorClass, baseType);
/*  87 */     if (baseParam == null)
/*  88 */       throw new WebServiceException(ServerMessages.NOT_IMPLEMENT_PROVIDER(implementorClass.getName()));
/*  89 */     if (!(baseParam instanceof ParameterizedType)) {
/*  90 */       throw new WebServiceException(ServerMessages.PROVIDER_NOT_PARAMETERIZED(implementorClass.getName()));
/*     */     }
/*  92 */     ParameterizedType pt = (ParameterizedType)baseParam;
/*  93 */     Type[] types = pt.getActualTypeArguments();
/*  94 */     if (!(types[0] instanceof Class))
/*  95 */       throw new WebServiceException(ServerMessages.PROVIDER_INVALID_PARAMETER_TYPE(implementorClass.getName(), types[0]));
/*  96 */     this.datatype = ((Class)types[0]);
/*     */ 
/*  98 */     if ((this.mode == Service.Mode.PAYLOAD) && (this.datatype != Source.class))
/*     */     {
/* 101 */       throw new IllegalArgumentException("Illeagal combination - Mode.PAYLOAD and Provider<" + otherClass.getName() + ">");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Service.Mode getServiceMode(Class<?> c)
/*     */   {
/* 113 */     ServiceMode mode = (ServiceMode)c.getAnnotation(ServiceMode.class);
/* 114 */     return mode == null ? Service.Mode.PAYLOAD : mode.value();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.provider.ProviderEndpointModel
 * JD-Core Version:    0.6.2
 */