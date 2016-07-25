/*     */ package com.sun.xml.internal.ws.client.sei;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback;
/*     */ import com.sun.xml.internal.ws.client.AsyncInvoker;
/*     */ import com.sun.xml.internal.ws.client.AsyncResponseImpl;
/*     */ import com.sun.xml.internal.ws.client.RequestContext;
/*     */ import com.sun.xml.internal.ws.client.ResponseContext;
/*     */ import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
/*     */ import com.sun.xml.internal.ws.model.JavaMethodImpl;
/*     */ import com.sun.xml.internal.ws.model.ParameterImpl;
/*     */ import com.sun.xml.internal.ws.model.WrapperParameter;
/*     */ import com.sun.xml.internal.ws.model.soap.SOAPBindingImpl;
/*     */ import java.util.List;
/*     */ import javax.jws.soap.SOAPBinding.Style;
/*     */ import javax.xml.ws.AsyncHandler;
/*     */ import javax.xml.ws.Response;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ abstract class AsyncMethodHandler extends SEIMethodHandler
/*     */ {
/*     */   private final ResponseBuilder responseBuilder;
/*     */ 
/*     */   @Nullable
/*     */   private final Class asyncBeanClass;
/*     */ 
/*     */   AsyncMethodHandler(SEIStub owner, JavaMethodImpl jm, JavaMethodImpl sync)
/*     */   {
/*  65 */     super(owner, sync);
/*     */ 
/*  67 */     List rp = sync.getResponseParameters();
/*  68 */     int size = 0;
/*  69 */     for (ParameterImpl param : rp) {
/*  70 */       if (param.isWrapperStyle()) {
/*  71 */         WrapperParameter wrapParam = (WrapperParameter)param;
/*  72 */         size += wrapParam.getWrapperChildren().size();
/*  73 */         if (sync.getBinding().getStyle() == SOAPBinding.Style.DOCUMENT)
/*     */         {
/*  76 */           size += 2;
/*     */         }
/*     */       } else {
/*  79 */         size++;
/*     */       }
/*     */     }
/*     */ 
/*  83 */     Class tempWrap = null;
/*  84 */     if (size > 1) {
/*  85 */       rp = jm.getResponseParameters();
/*  86 */       for (ParameterImpl param : rp) {
/*  87 */         if (param.isWrapperStyle()) {
/*  88 */           WrapperParameter wrapParam = (WrapperParameter)param;
/*  89 */           if (sync.getBinding().getStyle() == SOAPBinding.Style.DOCUMENT)
/*     */           {
/*  91 */             tempWrap = (Class)wrapParam.getTypeReference().type;
/*     */           }
/*     */           else {
/*  94 */             for (ParameterImpl p : wrapParam.getWrapperChildren()) {
/*  95 */               if (p.getIndex() == -1) {
/*  96 */                 tempWrap = (Class)p.getTypeReference().type;
/*  97 */                 break;
/*     */               }
/*     */             }
/* 100 */             if (tempWrap != null)
/*     */               break;
/*     */           }
/*     */         }
/* 104 */         else if (param.getIndex() == -1) {
/* 105 */           tempWrap = (Class)param.getTypeReference().type;
/* 106 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 111 */     this.asyncBeanClass = tempWrap;
/*     */ 
/* 113 */     switch (size) {
/*     */     case 0:
/* 115 */       this.responseBuilder = buildResponseBuilder(sync, ValueSetterFactory.NONE);
/* 116 */       break;
/*     */     case 1:
/* 118 */       this.responseBuilder = buildResponseBuilder(sync, ValueSetterFactory.SINGLE);
/* 119 */       break;
/*     */     default:
/* 121 */       this.responseBuilder = buildResponseBuilder(sync, new ValueSetterFactory.AsyncBeanValueSetterFactory(this.asyncBeanClass));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final Response<Object> doInvoke(Object proxy, Object[] args, AsyncHandler handler)
/*     */   {
/* 128 */     AsyncInvoker invoker = new SEIAsyncInvoker(proxy, args);
/* 129 */     AsyncResponseImpl ft = new AsyncResponseImpl(invoker, handler);
/* 130 */     invoker.setReceiver(ft);
/* 131 */     ft.run();
/* 132 */     return ft;
/*     */   }
/*     */ 
/*     */   ValueGetterFactory getValueGetterFactory()
/*     */   {
/* 203 */     return ValueGetterFactory.ASYNC;
/*     */   }
/*     */ 
/*     */   private class SEIAsyncInvoker extends AsyncInvoker
/*     */   {
/* 138 */     private final RequestContext rc = AsyncMethodHandler.this.owner.requestContext.copy();
/*     */     private final Object[] args;
/*     */ 
/*     */     SEIAsyncInvoker(Object proxy, Object[] args)
/*     */     {
/* 142 */       this.args = args;
/*     */     }
/*     */ 
/*     */     public void do_run() {
/* 146 */       Packet req = new Packet(AsyncMethodHandler.this.createRequestMessage(this.args));
/* 147 */       req.soapAction = AsyncMethodHandler.this.soapAction;
/* 148 */       req.expectReply = Boolean.valueOf(!AsyncMethodHandler.this.isOneWay);
/* 149 */       req.getMessage().assertOneWay(AsyncMethodHandler.this.isOneWay);
/*     */ 
/* 151 */       Fiber.CompletionCallback callback = new Fiber.CompletionCallback()
/*     */       {
/*     */         public void onCompletion(@NotNull Packet response) {
/* 154 */           AsyncMethodHandler.SEIAsyncInvoker.this.responseImpl.setResponseContext(new ResponseContext(response));
/* 155 */           Message msg = response.getMessage();
/* 156 */           if (msg == null)
/* 157 */             return;
/*     */           try
/*     */           {
/* 160 */             if (msg.isFault()) {
/* 161 */               SOAPFaultBuilder faultBuilder = SOAPFaultBuilder.create(msg);
/* 162 */               throw faultBuilder.createException(AsyncMethodHandler.this.checkedExceptions);
/*     */             }
/* 164 */             Object[] rargs = new Object[1];
/* 165 */             if (AsyncMethodHandler.this.asyncBeanClass != null) {
/* 166 */               rargs[0] = AsyncMethodHandler.this.asyncBeanClass.newInstance();
/*     */             }
/* 168 */             AsyncMethodHandler.this.responseBuilder.readResponse(msg, rargs);
/* 169 */             AsyncMethodHandler.SEIAsyncInvoker.this.responseImpl.set(rargs[0], null);
/*     */           }
/*     */           catch (Throwable t) {
/* 172 */             if ((t instanceof RuntimeException)) {
/* 173 */               if ((t instanceof WebServiceException)) {
/* 174 */                 AsyncMethodHandler.SEIAsyncInvoker.this.responseImpl.set(null, t);
/*     */               }
/*     */             }
/* 177 */             else if ((t instanceof Exception)) {
/* 178 */               AsyncMethodHandler.SEIAsyncInvoker.this.responseImpl.set(null, t);
/* 179 */               return;
/*     */             }
/*     */ 
/* 183 */             AsyncMethodHandler.SEIAsyncInvoker.this.responseImpl.set(null, new WebServiceException(t));
/*     */           }
/*     */         }
/*     */ 
/*     */         public void onCompletion(@NotNull Throwable error)
/*     */         {
/* 189 */           if ((error instanceof WebServiceException)) {
/* 190 */             AsyncMethodHandler.SEIAsyncInvoker.this.responseImpl.set(null, error);
/*     */           }
/*     */           else
/*     */           {
/* 194 */             AsyncMethodHandler.SEIAsyncInvoker.this.responseImpl.set(null, new WebServiceException(error));
/*     */           }
/*     */         }
/*     */       };
/* 198 */       AsyncMethodHandler.this.owner.doProcessAsync(req, this.rc, callback);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.sei.AsyncMethodHandler
 * JD-Core Version:    0.6.2
 */