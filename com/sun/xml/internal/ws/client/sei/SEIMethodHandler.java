/*     */ package com.sun.xml.internal.ws.client.sei;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.model.JavaMethod;
/*     */ import com.sun.xml.internal.ws.api.model.MEP;
/*     */ import com.sun.xml.internal.ws.api.model.ParameterBinding.Kind;
/*     */ import com.sun.xml.internal.ws.api.model.soap.SOAPBinding;
/*     */ import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
/*     */ import com.sun.xml.internal.ws.model.JavaMethodImpl;
/*     */ import com.sun.xml.internal.ws.model.ParameterImpl;
/*     */ import com.sun.xml.internal.ws.model.WrapperParameter;
/*     */ import com.sun.xml.internal.ws.model.soap.SOAPBindingImpl;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ abstract class SEIMethodHandler extends MethodHandler
/*     */ {
/*     */   private final BodyBuilder bodyBuilder;
/*     */   private final MessageFiller[] inFillers;
/*     */   protected final String soapAction;
/*     */   protected final boolean isOneWay;
/*     */   protected final JavaMethodImpl javaMethod;
/*     */   protected final Map<QName, CheckedExceptionImpl> checkedExceptions;
/*     */ 
/*     */   SEIMethodHandler(SEIStub owner, JavaMethodImpl method)
/*     */   {
/*  76 */     super(owner);
/*     */ 
/*  79 */     this.checkedExceptions = new HashMap();
/*  80 */     for (CheckedExceptionImpl ce : method.getCheckedExceptions()) {
/*  81 */       this.checkedExceptions.put(ce.getBridge().getTypeReference().tagName, ce);
/*     */     }
/*     */ 
/*  84 */     if ((method.getInputAction() != null) && (!method.getBinding().getSOAPAction().equals("")))
/*  85 */       this.soapAction = method.getInputAction();
/*     */     else {
/*  87 */       this.soapAction = method.getBinding().getSOAPAction();
/*     */     }
/*  89 */     this.javaMethod = method;
/*     */ 
/*  92 */     List rp = method.getRequestParameters();
/*     */ 
/*  94 */     BodyBuilder bodyBuilder = null;
/*  95 */     List fillers = new ArrayList();
/*     */ 
/*  97 */     for (ParameterImpl param : rp) {
/*  98 */       ValueGetter getter = getValueGetterFactory().get(param);
/*     */ 
/* 100 */       switch (1.$SwitchMap$com$sun$xml$internal$ws$api$model$ParameterBinding$Kind[param.getInBinding().kind.ordinal()]) {
/*     */       case 1:
/* 102 */         if (param.isWrapperStyle()) {
/* 103 */           if (param.getParent().getBinding().isRpcLit())
/* 104 */             bodyBuilder = new BodyBuilder.RpcLit((WrapperParameter)param, owner.soapVersion, getValueGetterFactory());
/*     */           else
/* 106 */             bodyBuilder = new BodyBuilder.DocLit((WrapperParameter)param, owner.soapVersion, getValueGetterFactory());
/*     */         }
/* 108 */         else bodyBuilder = new BodyBuilder.Bare(param, owner.soapVersion, getter);
/*     */ 
/* 110 */         break;
/*     */       case 2:
/* 112 */         fillers.add(new MessageFiller.Header(param.getIndex(), param.getBridge(), getter));
/*     */ 
/* 116 */         break;
/*     */       case 3:
/* 118 */         fillers.add(MessageFiller.AttachmentFiller.createAttachmentFiller(param, getter));
/* 119 */         break;
/*     */       case 4:
/* 121 */         break;
/*     */       default:
/* 123 */         throw new AssertionError();
/*     */       }
/*     */     }
/*     */ 
/* 127 */     if (bodyBuilder == null)
/*     */     {
/* 129 */       switch (owner.soapVersion) {
/*     */       case SOAP_11:
/* 131 */         bodyBuilder = BodyBuilder.EMPTY_SOAP11;
/* 132 */         break;
/*     */       case SOAP_12:
/* 134 */         bodyBuilder = BodyBuilder.EMPTY_SOAP12;
/* 135 */         break;
/*     */       default:
/* 137 */         throw new AssertionError();
/*     */       }
/*     */     }
/*     */ 
/* 141 */     this.bodyBuilder = bodyBuilder;
/* 142 */     this.inFillers = ((MessageFiller[])fillers.toArray(new MessageFiller[fillers.size()]));
/*     */ 
/* 145 */     this.isOneWay = method.getMEP().isOneWay();
/*     */   }
/*     */ 
/*     */   ResponseBuilder buildResponseBuilder(JavaMethodImpl method, ValueSetterFactory setterFactory)
/*     */   {
/* 150 */     List rp = method.getResponseParameters();
/* 151 */     List builders = new ArrayList();
/*     */ 
/* 153 */     for (ParameterImpl param : rp)
/*     */     {
/*     */       ValueSetter setter;
/* 155 */       switch (1.$SwitchMap$com$sun$xml$internal$ws$api$model$ParameterBinding$Kind[param.getOutBinding().kind.ordinal()]) {
/*     */       case 1:
/* 157 */         if (param.isWrapperStyle()) {
/* 158 */           if (param.getParent().getBinding().isRpcLit())
/* 159 */             builders.add(new ResponseBuilder.RpcLit((WrapperParameter)param, setterFactory));
/*     */           else
/* 161 */             builders.add(new ResponseBuilder.DocLit((WrapperParameter)param, setterFactory));
/*     */         } else {
/* 163 */           setter = setterFactory.get(param);
/* 164 */           builders.add(new ResponseBuilder.Body(param.getBridge(), setter));
/*     */         }
/* 166 */         break;
/*     */       case 2:
/* 168 */         setter = setterFactory.get(param);
/* 169 */         builders.add(new ResponseBuilder.Header(this.owner.soapVersion, param, setter));
/* 170 */         break;
/*     */       case 3:
/* 172 */         setter = setterFactory.get(param);
/* 173 */         builders.add(ResponseBuilder.AttachmentBuilder.createAttachmentBuilder(param, setter));
/* 174 */         break;
/*     */       case 4:
/* 176 */         setter = setterFactory.get(param);
/* 177 */         builders.add(new ResponseBuilder.NullSetter(setter, ResponseBuilder.getVMUninitializedValue(param.getTypeReference().type)));
/*     */ 
/* 179 */         break;
/*     */       default:
/* 181 */         throw new AssertionError();
/*     */       }
/*     */     }
/*     */     ResponseBuilder rb;
/* 185 */     switch (builders.size()) {
/*     */     case 0:
/* 187 */       rb = ResponseBuilder.NONE;
/* 188 */       break;
/*     */     case 1:
/* 190 */       rb = (ResponseBuilder)builders.get(0);
/* 191 */       break;
/*     */     default:
/* 193 */       rb = new ResponseBuilder.Composite(builders);
/*     */     }
/* 195 */     return rb;
/*     */   }
/*     */ 
/*     */   Message createRequestMessage(Object[] args)
/*     */   {
/* 205 */     Message msg = this.bodyBuilder.createMessage(args);
/*     */ 
/* 207 */     for (MessageFiller filler : this.inFillers) {
/* 208 */       filler.fillIn(args, msg);
/*     */     }
/* 210 */     return msg;
/*     */   }
/*     */ 
/*     */   abstract ValueGetterFactory getValueGetterFactory();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.sei.SEIMethodHandler
 * JD-Core Version:    0.6.2
 */