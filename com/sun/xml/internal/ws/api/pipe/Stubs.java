/*     */ package com.sun.xml.internal.ws.api.pipe;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.WSService;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.client.WSPortInfo;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.client.WSServiceDelegate;
/*     */ import com.sun.xml.internal.ws.client.dispatch.DataSourceDispatch;
/*     */ import com.sun.xml.internal.ws.client.dispatch.DispatchImpl;
/*     */ import com.sun.xml.internal.ws.client.dispatch.JAXBDispatch;
/*     */ import com.sun.xml.internal.ws.client.dispatch.MessageDispatch;
/*     */ import com.sun.xml.internal.ws.client.dispatch.SOAPMessageDispatch;
/*     */ import com.sun.xml.internal.ws.client.sei.SEIStub;
/*     */ import com.sun.xml.internal.ws.developer.WSBindingProvider;
/*     */ import com.sun.xml.internal.ws.model.SOAPSEIModel;
/*     */ import java.lang.reflect.Proxy;
/*     */ import javax.activation.DataSource;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.ws.Dispatch;
/*     */ import javax.xml.ws.Service.Mode;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public abstract class Stubs
/*     */ {
/*     */   @Deprecated
/*     */   public static Dispatch<SOAPMessage> createSAAJDispatch(QName portName, WSService owner, WSBinding binding, Service.Mode mode, Tube next, @Nullable WSEndpointReference epr)
/*     */   {
/* 118 */     DispatchImpl.checkValidSOAPMessageDispatch(binding, mode);
/* 119 */     return new SOAPMessageDispatch(portName, mode, (WSServiceDelegate)owner, next, (BindingImpl)binding, epr);
/*     */   }
/*     */ 
/*     */   public static Dispatch<SOAPMessage> createSAAJDispatch(WSPortInfo portInfo, WSBinding binding, Service.Mode mode, @Nullable WSEndpointReference epr)
/*     */   {
/* 131 */     DispatchImpl.checkValidSOAPMessageDispatch(binding, mode);
/* 132 */     return new SOAPMessageDispatch(portInfo, mode, (BindingImpl)binding, epr);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static Dispatch<DataSource> createDataSourceDispatch(QName portName, WSService owner, WSBinding binding, Service.Mode mode, Tube next, @Nullable WSEndpointReference epr)
/*     */   {
/* 145 */     DispatchImpl.checkValidDataSourceDispatch(binding, mode);
/* 146 */     return new DataSourceDispatch(portName, mode, (WSServiceDelegate)owner, next, (BindingImpl)binding, epr);
/*     */   }
/*     */ 
/*     */   public static Dispatch<DataSource> createDataSourceDispatch(WSPortInfo portInfo, WSBinding binding, Service.Mode mode, @Nullable WSEndpointReference epr)
/*     */   {
/* 158 */     DispatchImpl.checkValidDataSourceDispatch(binding, mode);
/* 159 */     return new DataSourceDispatch(portInfo, mode, (BindingImpl)binding, epr);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static Dispatch<Source> createSourceDispatch(QName portName, WSService owner, WSBinding binding, Service.Mode mode, Tube next, @Nullable WSEndpointReference epr)
/*     */   {
/* 172 */     return DispatchImpl.createSourceDispatch(portName, mode, (WSServiceDelegate)owner, next, (BindingImpl)binding, epr);
/*     */   }
/*     */ 
/*     */   public static Dispatch<Source> createSourceDispatch(WSPortInfo portInfo, WSBinding binding, Service.Mode mode, @Nullable WSEndpointReference epr)
/*     */   {
/* 184 */     return DispatchImpl.createSourceDispatch(portInfo, mode, (BindingImpl)binding, epr);
/*     */   }
/*     */ 
/*     */   public static <T> Dispatch<T> createDispatch(QName portName, WSService owner, WSBinding binding, Class<T> clazz, Service.Mode mode, Tube next, @Nullable WSEndpointReference epr)
/*     */   {
/* 213 */     if (clazz == SOAPMessage.class)
/* 214 */       return createSAAJDispatch(portName, owner, binding, mode, next, epr);
/* 215 */     if (clazz == Source.class)
/* 216 */       return createSourceDispatch(portName, owner, binding, mode, next, epr);
/* 217 */     if (clazz == DataSource.class)
/* 218 */       return createDataSourceDispatch(portName, owner, binding, mode, next, epr);
/* 219 */     if (clazz == Message.class) {
/* 220 */       if (mode == Service.Mode.MESSAGE) {
/* 221 */         return createMessageDispatch(portName, owner, binding, next, epr);
/*     */       }
/* 223 */       throw new WebServiceException(mode + " not supported with Dispatch<Message>");
/*     */     }
/* 225 */     throw new WebServiceException("Unknown class type " + clazz.getName());
/*     */   }
/*     */ 
/*     */   public static <T> Dispatch<T> createDispatch(WSPortInfo portInfo, WSService owner, WSBinding binding, Class<T> clazz, Service.Mode mode, @Nullable WSEndpointReference epr)
/*     */   {
/* 252 */     if (clazz == SOAPMessage.class)
/* 253 */       return createSAAJDispatch(portInfo, binding, mode, epr);
/* 254 */     if (clazz == Source.class)
/* 255 */       return createSourceDispatch(portInfo, binding, mode, epr);
/* 256 */     if (clazz == DataSource.class)
/* 257 */       return createDataSourceDispatch(portInfo, binding, mode, epr);
/* 258 */     if (clazz == Message.class) {
/* 259 */       if (mode == Service.Mode.MESSAGE) {
/* 260 */         return createMessageDispatch(portInfo, binding, epr);
/*     */       }
/* 262 */       throw new WebServiceException(mode + " not supported with Dispatch<Message>");
/*     */     }
/* 264 */     throw new WebServiceException("Unknown class type " + clazz.getName());
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static Dispatch<Object> createJAXBDispatch(QName portName, WSService owner, WSBinding binding, JAXBContext jaxbContext, Service.Mode mode, Tube next, @Nullable WSEndpointReference epr)
/*     */   {
/* 291 */     return new JAXBDispatch(portName, jaxbContext, mode, (WSServiceDelegate)owner, next, (BindingImpl)binding, epr);
/*     */   }
/*     */ 
/*     */   public static Dispatch<Object> createJAXBDispatch(WSPortInfo portInfo, WSBinding binding, JAXBContext jaxbContext, Service.Mode mode, @Nullable WSEndpointReference epr)
/*     */   {
/* 308 */     return new JAXBDispatch(portInfo, jaxbContext, mode, (BindingImpl)binding, epr);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static Dispatch<Message> createMessageDispatch(QName portName, WSService owner, WSBinding binding, Tube next, @Nullable WSEndpointReference epr)
/*     */   {
/* 331 */     return new MessageDispatch(portName, (WSServiceDelegate)owner, next, (BindingImpl)binding, epr);
/*     */   }
/*     */ 
/*     */   public static Dispatch<Message> createMessageDispatch(WSPortInfo portInfo, WSBinding binding, @Nullable WSEndpointReference epr)
/*     */   {
/* 349 */     return new MessageDispatch(portInfo, (BindingImpl)binding, epr);
/*     */   }
/*     */ 
/*     */   public <T> T createPortProxy(WSService service, WSBinding binding, SEIModel model, Class<T> portInterface, Tube next, @Nullable WSEndpointReference epr)
/*     */   {
/* 373 */     SEIStub ps = new SEIStub((WSServiceDelegate)service, (BindingImpl)binding, (SOAPSEIModel)model, next, epr);
/* 374 */     return portInterface.cast(Proxy.newProxyInstance(portInterface.getClassLoader(), new Class[] { portInterface, WSBindingProvider.class }, ps));
/*     */   }
/*     */ 
/*     */   public <T> T createPortProxy(WSPortInfo portInfo, WSBinding binding, SEIModel model, Class<T> portInterface, @Nullable WSEndpointReference epr)
/*     */   {
/* 398 */     SEIStub ps = new SEIStub(portInfo, (BindingImpl)binding, (SOAPSEIModel)model, epr);
/* 399 */     return portInterface.cast(Proxy.newProxyInstance(portInterface.getClassLoader(), new Class[] { portInterface, WSBindingProvider.class }, ps));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.Stubs
 * JD-Core Version:    0.6.2
 */