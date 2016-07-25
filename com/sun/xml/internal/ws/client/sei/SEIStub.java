/*     */ package com.sun.xml.internal.ws.client.sei;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.client.WSPortInfo;
/*     */ import com.sun.xml.internal.ws.api.message.Header;
/*     */ import com.sun.xml.internal.ws.api.message.Headers;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.MEP;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.client.RequestContext;
/*     */ import com.sun.xml.internal.ws.client.ResponseContextReceiver;
/*     */ import com.sun.xml.internal.ws.client.Stub;
/*     */ import com.sun.xml.internal.ws.client.WSServiceDelegate;
/*     */ import com.sun.xml.internal.ws.model.JavaMethodImpl;
/*     */ import com.sun.xml.internal.ws.model.SOAPSEIModel;
/*     */ import com.sun.xml.internal.ws.wsdl.OperationDispatcher;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class SEIStub extends Stub
/*     */   implements InvocationHandler
/*     */ {
/*     */   public final SOAPSEIModel seiModel;
/*     */   public final SOAPVersion soapVersion;
/* 129 */   private final Map<Method, MethodHandler> methodHandlers = new HashMap();
/*     */ 
/*     */   @Deprecated
/*     */   public SEIStub(WSServiceDelegate owner, BindingImpl binding, SOAPSEIModel seiModel, Tube master, WSEndpointReference epr)
/*     */   {
/*  68 */     super(owner, master, binding, seiModel.getPort(), seiModel.getPort().getAddress(), epr);
/*  69 */     this.seiModel = seiModel;
/*  70 */     this.soapVersion = binding.getSOAPVersion();
/*  71 */     initMethodHandlers();
/*     */   }
/*     */ 
/*     */   public SEIStub(WSPortInfo portInfo, BindingImpl binding, SOAPSEIModel seiModel, WSEndpointReference epr) {
/*  75 */     super(portInfo, binding, seiModel.getPort().getAddress(), epr);
/*  76 */     this.seiModel = seiModel;
/*  77 */     this.soapVersion = binding.getSOAPVersion();
/*  78 */     initMethodHandlers();
/*     */   }
/*     */ 
/*     */   private void initMethodHandlers() {
/*  82 */     Map syncs = new HashMap();
/*     */ 
/*  86 */     for (JavaMethodImpl m : this.seiModel.getJavaMethods()) {
/*  87 */       if (!m.getMEP().isAsync) {
/*  88 */         SyncMethodHandler handler = new SyncMethodHandler(this, m);
/*  89 */         syncs.put(m.getOperation(), m);
/*  90 */         this.methodHandlers.put(m.getMethod(), handler);
/*     */       }
/*     */     }
/*     */ 
/*  94 */     for (JavaMethodImpl jm : this.seiModel.getJavaMethods()) {
/*  95 */       JavaMethodImpl sync = (JavaMethodImpl)syncs.get(jm.getOperation());
/*  96 */       if (jm.getMEP() == MEP.ASYNC_CALLBACK) {
/*  97 */         Method m = jm.getMethod();
/*  98 */         CallbackMethodHandler handler = new CallbackMethodHandler(this, jm, sync, m.getParameterTypes().length - 1);
/*     */ 
/* 100 */         this.methodHandlers.put(m, handler);
/*     */       }
/* 102 */       if (jm.getMEP() == MEP.ASYNC_POLL) {
/* 103 */         Method m = jm.getMethod();
/* 104 */         PollingMethodHandler handler = new PollingMethodHandler(this, jm, sync);
/* 105 */         this.methodHandlers.put(m, handler);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public OperationDispatcher getOperationDispatcher()
/*     */   {
/* 120 */     if ((this.operationDispatcher == null) && (this.wsdlPort != null))
/* 121 */       this.operationDispatcher = new OperationDispatcher(this.wsdlPort, this.binding, this.seiModel);
/* 122 */     return this.operationDispatcher;
/*     */   }
/*     */ 
/*     */   public Object invoke(Object proxy, Method method, Object[] args)
/*     */     throws Throwable
/*     */   {
/* 132 */     validateInputs(proxy, method);
/* 133 */     MethodHandler handler = (MethodHandler)this.methodHandlers.get(method);
/* 134 */     if (handler != null) {
/* 135 */       return handler.invoke(proxy, args);
/*     */     }
/*     */     try
/*     */     {
/* 139 */       return MethodUtil.invoke(this, method, args);
/*     */     }
/*     */     catch (IllegalAccessException e) {
/* 142 */       throw new AssertionError(e);
/*     */     } catch (IllegalArgumentException e) {
/* 144 */       throw new AssertionError(e);
/*     */     } catch (InvocationTargetException e) {
/* 146 */       throw e.getCause();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void validateInputs(Object proxy, Method method)
/*     */   {
/* 152 */     if ((proxy == null) || (!Proxy.isProxyClass(proxy.getClass()))) {
/* 153 */       throw new IllegalStateException("Passed object is not proxy!");
/*     */     }
/* 155 */     Class declaringClass = method.getDeclaringClass();
/* 156 */     if ((method == null) || (declaringClass == null) || (Modifier.isStatic(method.getModifiers())))
/*     */     {
/* 158 */       throw new IllegalStateException("Invoking static method is not allowed!");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Packet doProcess(Packet request, RequestContext rc, ResponseContextReceiver receiver) {
/* 163 */     return super.process(request, rc, receiver);
/*     */   }
/*     */ 
/*     */   public final void doProcessAsync(Packet request, RequestContext rc, Fiber.CompletionCallback callback) {
/* 167 */     super.processAsync(request, rc, callback);
/*     */   }
/*     */   @NotNull
/*     */   protected final QName getPortName() {
/* 171 */     return this.wsdlPort.getName();
/*     */   }
/*     */ 
/*     */   public void setOutboundHeaders(Object[] headers)
/*     */   {
/* 176 */     if (headers == null)
/* 177 */       throw new IllegalArgumentException();
/* 178 */     Header[] hl = new Header[headers.length];
/* 179 */     for (int i = 0; i < hl.length; i++) {
/* 180 */       if (headers[i] == null)
/* 181 */         throw new IllegalArgumentException();
/* 182 */       hl[i] = Headers.create(this.seiModel.getJAXBContext(), headers[i]);
/*     */     }
/* 184 */     super.setOutboundHeaders(hl);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.sei.SEIStub
 * JD-Core Version:    0.6.2
 */