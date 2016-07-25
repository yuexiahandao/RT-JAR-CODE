/*     */ package com.sun.xml.internal.ws.server.sei;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.api.CompositeStructure;
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.bind.api.RawAccessor;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Messages;
/*     */ import com.sun.xml.internal.ws.message.jaxb.JAXBMessage;
/*     */ import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
/*     */ import com.sun.xml.internal.ws.model.ParameterImpl;
/*     */ import com.sun.xml.internal.ws.model.WrapperParameter;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ abstract class EndpointResponseMessageBuilder
/*     */ {
/*  54 */   static final EndpointResponseMessageBuilder EMPTY_SOAP11 = new Empty(SOAPVersion.SOAP_11);
/*  55 */   static final EndpointResponseMessageBuilder EMPTY_SOAP12 = new Empty(SOAPVersion.SOAP_12);
/*     */ 
/*     */   abstract Message createMessage(Object[] paramArrayOfObject, Object paramObject);
/*     */ 
/*     */   static final class Bare extends EndpointResponseMessageBuilder.JAXB
/*     */   {
/*     */     private final int methodPos;
/*     */     private final ValueGetter getter;
/*     */ 
/*     */     Bare(ParameterImpl p, SOAPVersion soapVersion)
/*     */     {
/* 113 */       super(soapVersion);
/* 114 */       this.methodPos = p.getIndex();
/* 115 */       this.getter = ValueGetter.get(p);
/*     */     }
/*     */ 
/*     */     Object build(Object[] methodArgs, Object returnValue)
/*     */     {
/* 122 */       if (this.methodPos == -1) {
/* 123 */         return returnValue;
/*     */       }
/* 125 */       return this.getter.get(methodArgs[this.methodPos]);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class DocLit extends EndpointResponseMessageBuilder.Wrapped
/*     */   {
/*     */     private final RawAccessor[] accessors;
/*     */     private final Class wrapper;
/*     */ 
/*     */     DocLit(WrapperParameter wp, SOAPVersion soapVersion)
/*     */     {
/* 182 */       super(soapVersion);
/*     */ 
/* 184 */       this.wrapper = ((Class)wp.getBridge().getTypeReference().type);
/*     */ 
/* 186 */       List children = wp.getWrapperChildren();
/*     */ 
/* 188 */       this.accessors = new RawAccessor[children.size()];
/* 189 */       for (int i = 0; i < this.accessors.length; i++) {
/* 190 */         ParameterImpl p = (ParameterImpl)children.get(i);
/* 191 */         QName name = p.getName();
/*     */         try {
/* 193 */           this.accessors[i] = p.getOwner().getJAXBContext().getElementPropertyAccessor(this.wrapper, name.getNamespaceURI(), name.getLocalPart());
/*     */         }
/*     */         catch (JAXBException e) {
/* 196 */           throw new WebServiceException(this.wrapper + " do not have a property of the name " + name, e);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     Object build(Object[] methodArgs, Object returnValue)
/*     */     {
/*     */       try
/*     */       {
/* 208 */         Object bean = this.wrapper.newInstance();
/*     */ 
/* 211 */         for (int i = this.indices.length - 1; i >= 0; i--) {
/* 212 */           if (this.indices[i] == -1)
/* 213 */             this.accessors[i].set(bean, returnValue);
/*     */           else {
/* 215 */             this.accessors[i].set(bean, this.getters[i].get(methodArgs[this.indices[i]]));
/*     */           }
/*     */         }
/*     */ 
/* 219 */         return bean;
/*     */       }
/*     */       catch (InstantiationException e) {
/* 222 */         Error x = new InstantiationError(e.getMessage());
/* 223 */         x.initCause(e);
/* 224 */         throw x;
/*     */       }
/*     */       catch (IllegalAccessException e) {
/* 227 */         Error x = new IllegalAccessError(e.getMessage());
/* 228 */         x.initCause(e);
/* 229 */         throw x;
/*     */       }
/*     */       catch (AccessorException e) {
/* 232 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class Empty extends EndpointResponseMessageBuilder
/*     */   {
/*     */     private final SOAPVersion soapVersion;
/*     */ 
/*     */     public Empty(SOAPVersion soapVersion)
/*     */     {
/*  61 */       this.soapVersion = soapVersion;
/*     */     }
/*     */ 
/*     */     Message createMessage(Object[] methodArgs, Object returnValue) {
/*  65 */       return Messages.createEmpty(this.soapVersion);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class JAXB extends EndpointResponseMessageBuilder
/*     */   {
/*     */     private final Bridge bridge;
/*     */     private final SOAPVersion soapVersion;
/*     */ 
/*     */     protected JAXB(Bridge bridge, SOAPVersion soapVersion)
/*     */     {
/*  82 */       assert (bridge != null);
/*  83 */       this.bridge = bridge;
/*  84 */       this.soapVersion = soapVersion;
/*     */     }
/*     */ 
/*     */     final Message createMessage(Object[] methodArgs, Object returnValue) {
/*  88 */       return JAXBMessage.create(this.bridge, build(methodArgs, returnValue), this.soapVersion);
/*     */     }
/*     */ 
/*     */     abstract Object build(Object[] paramArrayOfObject, Object paramObject);
/*     */   }
/*     */ 
/*     */   static final class RpcLit extends EndpointResponseMessageBuilder.Wrapped
/*     */   {
/*     */     private final Bridge[] parameterBridges;
/*     */     private final List<ParameterImpl> children;
/*     */ 
/*     */     RpcLit(WrapperParameter wp, SOAPVersion soapVersion)
/*     */     {
/* 261 */       super(soapVersion);
/*     */ 
/* 263 */       assert (wp.getTypeReference().type == CompositeStructure.class);
/*     */ 
/* 265 */       this.children = wp.getWrapperChildren();
/*     */ 
/* 267 */       this.parameterBridges = new Bridge[this.children.size()];
/* 268 */       for (int i = 0; i < this.parameterBridges.length; i++)
/* 269 */         this.parameterBridges[i] = ((ParameterImpl)this.children.get(i)).getBridge();
/*     */     }
/*     */ 
/*     */     CompositeStructure build(Object[] methodArgs, Object returnValue)
/*     */     {
/* 276 */       CompositeStructure cs = new CompositeStructure();
/* 277 */       cs.bridges = this.parameterBridges;
/* 278 */       cs.values = new Object[this.parameterBridges.length];
/*     */ 
/* 281 */       for (int i = this.indices.length - 1; i >= 0; i--)
/*     */       {
/*     */         Object v;
/*     */         Object v;
/* 283 */         if (this.indices[i] == -1)
/* 284 */           v = this.getters[i].get(returnValue);
/*     */         else {
/* 286 */           v = this.getters[i].get(methodArgs[this.indices[i]]);
/*     */         }
/* 288 */         if (v == null) {
/* 289 */           throw new WebServiceException("Method Parameter: " + ((ParameterImpl)this.children.get(i)).getName() + " cannot be null. This is BP 1.1 R2211 violation.");
/*     */         }
/*     */ 
/* 292 */         cs.values[i] = v;
/*     */       }
/*     */ 
/* 295 */       return cs;
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class Wrapped extends EndpointResponseMessageBuilder.JAXB
/*     */   {
/*     */     protected final int[] indices;
/*     */     protected final ValueGetter[] getters;
/*     */ 
/*     */     protected Wrapped(WrapperParameter wp, SOAPVersion soapVersion)
/*     */     {
/* 147 */       super(soapVersion);
/*     */ 
/* 149 */       List children = wp.getWrapperChildren();
/*     */ 
/* 151 */       this.indices = new int[children.size()];
/* 152 */       this.getters = new ValueGetter[children.size()];
/* 153 */       for (int i = 0; i < this.indices.length; i++) {
/* 154 */         ParameterImpl p = (ParameterImpl)children.get(i);
/* 155 */         this.indices[i] = p.getIndex();
/* 156 */         this.getters[i] = ValueGetter.get(p);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.sei.EndpointResponseMessageBuilder
 * JD-Core Version:    0.6.2
 */