/*     */ package com.sun.xml.internal.ws.server.sei;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.JavaMethod;
/*     */ import com.sun.xml.internal.ws.api.model.MEP;
/*     */ import com.sun.xml.internal.ws.api.model.ParameterBinding.Kind;
/*     */ import com.sun.xml.internal.ws.api.model.soap.SOAPBinding;
/*     */ import com.sun.xml.internal.ws.api.server.Invoker;
/*     */ import com.sun.xml.internal.ws.api.server.TransportBackChannel;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
/*     */ import com.sun.xml.internal.ws.model.JavaMethodImpl;
/*     */ import com.sun.xml.internal.ws.model.ParameterImpl;
/*     */ import com.sun.xml.internal.ws.model.WrapperParameter;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.jws.WebParam.Mode;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.ws.ProtocolException;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ final class EndpointMethodHandler
/*     */ {
/*     */   private final SOAPVersion soapVersion;
/*     */   private final Method method;
/*     */   private final int noOfArgs;
/*     */   private final JavaMethodImpl javaMethodModel;
/*     */   private final Boolean isOneWay;
/*     */   private final EndpointArgumentsBuilder argumentsBuilder;
/*     */   private final EndpointResponseMessageBuilder bodyBuilder;
/*     */   private final MessageFiller[] outFillers;
/*     */   private final SEIInvokerTube owner;
/* 287 */   private static final Logger LOGGER = Logger.getLogger(EndpointMethodHandler.class.getName());
/*     */ 
/*     */   public EndpointMethodHandler(SEIInvokerTube owner, JavaMethodImpl method, WSBinding binding)
/*     */   {
/*  91 */     this.owner = owner;
/*  92 */     this.soapVersion = binding.getSOAPVersion();
/*  93 */     this.method = method.getMethod();
/*  94 */     this.javaMethodModel = method;
/*  95 */     this.argumentsBuilder = createArgumentsBuilder();
/*  96 */     List fillers = new ArrayList();
/*  97 */     this.bodyBuilder = createResponseMessageBuilder(fillers);
/*  98 */     this.outFillers = ((MessageFiller[])fillers.toArray(new MessageFiller[fillers.size()]));
/*  99 */     this.isOneWay = Boolean.valueOf(method.getMEP().isOneWay());
/* 100 */     this.noOfArgs = this.method.getParameterTypes().length;
/*     */   }
/*     */ 
/*     */   private EndpointArgumentsBuilder createArgumentsBuilder()
/*     */   {
/* 111 */     List rp = this.javaMethodModel.getRequestParameters();
/* 112 */     List builders = new ArrayList();
/*     */ 
/* 114 */     for (ParameterImpl param : rp) {
/* 115 */       EndpointValueSetter setter = EndpointValueSetter.get(param);
/* 116 */       switch (1.$SwitchMap$com$sun$xml$internal$ws$api$model$ParameterBinding$Kind[param.getInBinding().kind.ordinal()]) {
/*     */       case 1:
/* 118 */         if (param.isWrapperStyle()) {
/* 119 */           if (param.getParent().getBinding().isRpcLit())
/* 120 */             builders.add(new EndpointArgumentsBuilder.RpcLit((WrapperParameter)param));
/*     */           else
/* 122 */             builders.add(new EndpointArgumentsBuilder.DocLit((WrapperParameter)param, WebParam.Mode.OUT));
/*     */         }
/* 124 */         else builders.add(new EndpointArgumentsBuilder.Body(param.getBridge(), setter));
/*     */ 
/* 126 */         break;
/*     */       case 2:
/* 128 */         builders.add(new EndpointArgumentsBuilder.Header(this.soapVersion, param, setter));
/* 129 */         break;
/*     */       case 3:
/* 131 */         builders.add(EndpointArgumentsBuilder.AttachmentBuilder.createAttachmentBuilder(param, setter));
/* 132 */         break;
/*     */       case 4:
/* 134 */         builders.add(new EndpointArgumentsBuilder.NullSetter(setter, EndpointArgumentsBuilder.getVMUninitializedValue(param.getTypeReference().type)));
/*     */ 
/* 136 */         break;
/*     */       default:
/* 138 */         throw new AssertionError();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 143 */     List resp = this.javaMethodModel.getResponseParameters();
/* 144 */     for (ParameterImpl param : resp)
/* 145 */       if (param.isWrapperStyle()) {
/* 146 */         WrapperParameter wp = (WrapperParameter)param;
/* 147 */         List children = wp.getWrapperChildren();
/* 148 */         for (ParameterImpl p : children)
/* 149 */           if ((p.isOUT()) && (p.getIndex() != -1)) {
/* 150 */             EndpointValueSetter setter = EndpointValueSetter.get(p);
/* 151 */             builders.add(new EndpointArgumentsBuilder.NullSetter(setter, null));
/*     */           }
/*     */       }
/* 154 */       else if ((param.isOUT()) && (param.getIndex() != -1)) {
/* 155 */         EndpointValueSetter setter = EndpointValueSetter.get(param);
/* 156 */         builders.add(new EndpointArgumentsBuilder.NullSetter(setter, null));
/*     */       }
/*     */     EndpointArgumentsBuilder argsBuilder;
/* 160 */     switch (builders.size()) {
/*     */     case 0:
/* 162 */       argsBuilder = EndpointArgumentsBuilder.NONE;
/* 163 */       break;
/*     */     case 1:
/* 165 */       argsBuilder = (EndpointArgumentsBuilder)builders.get(0);
/* 166 */       break;
/*     */     default:
/* 168 */       argsBuilder = new EndpointArgumentsBuilder.Composite(builders);
/*     */     }
/* 170 */     return argsBuilder;
/*     */   }
/*     */ 
/*     */   private EndpointResponseMessageBuilder createResponseMessageBuilder(List<MessageFiller> fillers)
/*     */   {
/* 178 */     EndpointResponseMessageBuilder bodyBuilder = null;
/* 179 */     List rp = this.javaMethodModel.getResponseParameters();
/*     */ 
/* 181 */     for (ParameterImpl param : rp) {
/* 182 */       ValueGetter getter = ValueGetter.get(param);
/*     */ 
/* 184 */       switch (1.$SwitchMap$com$sun$xml$internal$ws$api$model$ParameterBinding$Kind[param.getOutBinding().kind.ordinal()]) {
/*     */       case 1:
/* 186 */         if (param.isWrapperStyle()) {
/* 187 */           if (param.getParent().getBinding().isRpcLit()) {
/* 188 */             bodyBuilder = new EndpointResponseMessageBuilder.RpcLit((WrapperParameter)param, this.soapVersion);
/*     */           }
/*     */           else {
/* 191 */             bodyBuilder = new EndpointResponseMessageBuilder.DocLit((WrapperParameter)param, this.soapVersion);
/*     */           }
/*     */         }
/*     */         else {
/* 195 */           bodyBuilder = new EndpointResponseMessageBuilder.Bare(param, this.soapVersion);
/*     */         }
/* 197 */         break;
/*     */       case 2:
/* 199 */         fillers.add(new MessageFiller.Header(param.getIndex(), param.getBridge(), getter));
/* 200 */         break;
/*     */       case 3:
/* 202 */         fillers.add(MessageFiller.AttachmentFiller.createAttachmentFiller(param, getter));
/* 203 */         break;
/*     */       case 4:
/* 205 */         break;
/*     */       default:
/* 207 */         throw new AssertionError();
/*     */       }
/*     */     }
/*     */ 
/* 211 */     if (bodyBuilder == null)
/*     */     {
/* 213 */       switch (this.soapVersion) {
/*     */       case SOAP_11:
/* 215 */         bodyBuilder = EndpointResponseMessageBuilder.EMPTY_SOAP11;
/* 216 */         break;
/*     */       case SOAP_12:
/* 218 */         bodyBuilder = EndpointResponseMessageBuilder.EMPTY_SOAP12;
/* 219 */         break;
/*     */       default:
/* 221 */         throw new AssertionError();
/*     */       }
/*     */     }
/* 224 */     return bodyBuilder;
/*     */   }
/*     */ 
/*     */   public Packet invoke(Packet req)
/*     */   {
/* 229 */     Message reqMsg = req.getMessage();
/* 230 */     Object[] args = new Object[this.noOfArgs];
/*     */     try {
/* 232 */       this.argumentsBuilder.readRequest(reqMsg, args);
/*     */     } catch (JAXBException e) {
/* 234 */       throw new WebServiceException(e);
/*     */     } catch (XMLStreamException e) {
/* 236 */       throw new WebServiceException(e);
/*     */     }
/*     */ 
/* 241 */     if ((this.isOneWay.booleanValue()) && (req.transportBackChannel != null))
/* 242 */       req.transportBackChannel.close();
/*     */     Message responseMessage;
/*     */     try
/*     */     {
/* 246 */       Object ret = this.owner.getInvoker(req).invoke(req, this.method, args);
/* 247 */       responseMessage = this.isOneWay.booleanValue() ? null : createResponseMessage(args, ret);
/*     */     }
/*     */     catch (InvocationTargetException e)
/*     */     {
/*     */       Message responseMessage;
/* 249 */       Throwable cause = e.getCause();
/*     */ 
/* 251 */       if ((!(cause instanceof RuntimeException)) && ((cause instanceof Exception)))
/*     */       {
/* 253 */         LOGGER.log(Level.FINE, cause.getMessage(), cause);
/* 254 */         responseMessage = SOAPFaultBuilder.createSOAPFaultMessage(this.soapVersion, this.javaMethodModel.getCheckedException(cause.getClass()), cause);
/*     */       }
/*     */       else {
/* 257 */         if ((cause instanceof ProtocolException))
/*     */         {
/* 259 */           LOGGER.log(Level.FINE, cause.getMessage(), cause);
/*     */         }
/*     */         else {
/* 262 */           LOGGER.log(Level.SEVERE, cause.getMessage(), cause);
/*     */         }
/* 264 */         responseMessage = SOAPFaultBuilder.createSOAPFaultMessage(this.soapVersion, null, cause);
/*     */       }
/*     */     } catch (Exception e) {
/* 267 */       LOGGER.log(Level.SEVERE, e.getMessage(), e);
/* 268 */       responseMessage = SOAPFaultBuilder.createSOAPFaultMessage(this.soapVersion, null, e);
/*     */     }
/* 270 */     return req.createServerResponse(responseMessage, req.endpoint.getPort(), this.javaMethodModel.getOwner(), req.endpoint.getBinding());
/*     */   }
/*     */ 
/*     */   private Message createResponseMessage(Object[] args, Object returnValue)
/*     */   {
/* 279 */     Message msg = this.bodyBuilder.createMessage(args, returnValue);
/*     */ 
/* 281 */     for (MessageFiller filler : this.outFillers) {
/* 282 */       filler.fillIn(args, returnValue, msg);
/*     */     }
/* 284 */     return msg;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.sei.EndpointMethodHandler
 * JD-Core Version:    0.6.2
 */