/*     */ package com.sun.xml.internal.ws.client.sei;
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
/*     */ abstract class BodyBuilder
/*     */ {
/*  54 */   static final BodyBuilder EMPTY_SOAP11 = new Empty(SOAPVersion.SOAP_11);
/*  55 */   static final BodyBuilder EMPTY_SOAP12 = new Empty(SOAPVersion.SOAP_12);
/*     */ 
/*     */   abstract Message createMessage(Object[] paramArrayOfObject);
/*     */ 
/*     */   static final class Bare extends BodyBuilder.JAXB
/*     */   {
/*     */     private final int methodPos;
/*     */     private final ValueGetter getter;
/*     */ 
/*     */     Bare(ParameterImpl p, SOAPVersion soapVersion, ValueGetter getter)
/*     */     {
/* 113 */       super(soapVersion);
/* 114 */       this.methodPos = p.getIndex();
/* 115 */       this.getter = getter;
/*     */     }
/*     */ 
/*     */     Object build(Object[] methodArgs)
/*     */     {
/* 122 */       return this.getter.get(methodArgs[this.methodPos]);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class DocLit extends BodyBuilder.Wrapped
/*     */   {
/*     */     private final RawAccessor[] accessors;
/*     */     private final Class wrapper;
/*     */ 
/*     */     DocLit(WrapperParameter wp, SOAPVersion soapVersion, ValueGetterFactory getter)
/*     */     {
/* 177 */       super(soapVersion, getter);
/*     */ 
/* 179 */       this.wrapper = ((Class)wp.getBridge().getTypeReference().type);
/*     */ 
/* 181 */       List children = wp.getWrapperChildren();
/*     */ 
/* 183 */       this.accessors = new RawAccessor[children.size()];
/* 184 */       for (int i = 0; i < this.accessors.length; i++) {
/* 185 */         ParameterImpl p = (ParameterImpl)children.get(i);
/* 186 */         QName name = p.getName();
/*     */         try {
/* 188 */           this.accessors[i] = p.getOwner().getJAXBContext().getElementPropertyAccessor(this.wrapper, name.getNamespaceURI(), name.getLocalPart());
/*     */         }
/*     */         catch (JAXBException e) {
/* 191 */           throw new WebServiceException(this.wrapper + " do not have a property of the name " + name, e);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     Object build(Object[] methodArgs)
/*     */     {
/*     */       try
/*     */       {
/* 203 */         Object bean = this.wrapper.newInstance();
/*     */ 
/* 206 */         for (int i = this.indices.length - 1; i >= 0; i--) {
/* 207 */           this.accessors[i].set(bean, this.getters[i].get(methodArgs[this.indices[i]]));
/*     */         }
/*     */ 
/* 210 */         return bean;
/*     */       }
/*     */       catch (InstantiationException e) {
/* 213 */         Error x = new InstantiationError(e.getMessage());
/* 214 */         x.initCause(e);
/* 215 */         throw x;
/*     */       }
/*     */       catch (IllegalAccessException e) {
/* 218 */         Error x = new IllegalAccessError(e.getMessage());
/* 219 */         x.initCause(e);
/* 220 */         throw x;
/*     */       }
/*     */       catch (AccessorException e) {
/* 223 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class Empty extends BodyBuilder
/*     */   {
/*     */     private final SOAPVersion soapVersion;
/*     */ 
/*     */     public Empty(SOAPVersion soapVersion)
/*     */     {
/*  61 */       this.soapVersion = soapVersion;
/*     */     }
/*     */ 
/*     */     Message createMessage(Object[] methodArgs) {
/*  65 */       return Messages.createEmpty(this.soapVersion);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class JAXB extends BodyBuilder
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
/*     */     final Message createMessage(Object[] methodArgs) {
/*  88 */       return JAXBMessage.create(this.bridge, build(methodArgs), this.soapVersion);
/*     */     }
/*     */ 
/*     */     abstract Object build(Object[] paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   static final class RpcLit extends BodyBuilder.Wrapped
/*     */   {
/*     */     private final Bridge[] parameterBridges;
/*     */     private final List<ParameterImpl> children;
/*     */ 
/*     */     RpcLit(WrapperParameter wp, SOAPVersion soapVersion, ValueGetterFactory getter)
/*     */     {
/* 253 */       super(soapVersion, getter);
/*     */ 
/* 255 */       assert (wp.getTypeReference().type == CompositeStructure.class);
/*     */ 
/* 257 */       this.children = wp.getWrapperChildren();
/*     */ 
/* 259 */       this.parameterBridges = new Bridge[this.children.size()];
/* 260 */       for (int i = 0; i < this.parameterBridges.length; i++)
/* 261 */         this.parameterBridges[i] = ((ParameterImpl)this.children.get(i)).getBridge();
/*     */     }
/*     */ 
/*     */     CompositeStructure build(Object[] methodArgs)
/*     */     {
/* 268 */       CompositeStructure cs = new CompositeStructure();
/* 269 */       cs.bridges = this.parameterBridges;
/* 270 */       cs.values = new Object[this.parameterBridges.length];
/*     */ 
/* 273 */       for (int i = this.indices.length - 1; i >= 0; i--) {
/* 274 */         Object arg = this.getters[i].get(methodArgs[this.indices[i]]);
/* 275 */         if (arg == null) {
/* 276 */           throw new WebServiceException("Method Parameter: " + ((ParameterImpl)this.children.get(i)).getName() + " cannot be null. This is BP 1.1 R2211 violation.");
/*     */         }
/*     */ 
/* 279 */         cs.values[i] = arg;
/*     */       }
/*     */ 
/* 282 */       return cs;
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class Wrapped extends BodyBuilder.JAXB
/*     */   {
/*     */     protected final int[] indices;
/*     */     protected final ValueGetter[] getters;
/*     */ 
/*     */     protected Wrapped(WrapperParameter wp, SOAPVersion soapVersion, ValueGetterFactory getter)
/*     */     {
/* 144 */       super(soapVersion);
/*     */ 
/* 146 */       List children = wp.getWrapperChildren();
/*     */ 
/* 148 */       this.indices = new int[children.size()];
/* 149 */       this.getters = new ValueGetter[children.size()];
/* 150 */       for (int i = 0; i < this.indices.length; i++) {
/* 151 */         ParameterImpl p = (ParameterImpl)children.get(i);
/* 152 */         this.indices[i] = p.getIndex();
/* 153 */         this.getters[i] = getter.get(p);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.sei.BodyBuilder
 * JD-Core Version:    0.6.2
 */