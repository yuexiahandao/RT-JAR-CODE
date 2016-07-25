/*     */ package com.sun.xml.internal.bind.v2.runtime.reflect;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.istack.internal.SAXException2;
/*     */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.model.impl.RuntimeModelBuilder;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElementRef;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.opt.OptimizedTransducedAccessorFactory;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx.Snapshot;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Patcher;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.concurrent.Callable;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class TransducedAccessor<BeanT>
/*     */ {
/*     */   public boolean useNamespace()
/*     */   {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   public void declareNamespace(BeanT o, XMLSerializer w)
/*     */     throws AccessorException, SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public abstract CharSequence print(@NotNull BeanT paramBeanT)
/*     */     throws AccessorException, SAXException;
/*     */ 
/*     */   public abstract void parse(BeanT paramBeanT, CharSequence paramCharSequence)
/*     */     throws AccessorException, SAXException;
/*     */ 
/*     */   public abstract boolean hasValue(BeanT paramBeanT)
/*     */     throws AccessorException;
/*     */ 
/*     */   public static <T> TransducedAccessor<T> get(JAXBContextImpl context, RuntimeNonElementRef ref)
/*     */   {
/* 141 */     Transducer xducer = RuntimeModelBuilder.createTransducer(ref);
/* 142 */     RuntimePropertyInfo prop = ref.getSource();
/*     */ 
/* 144 */     if (prop.isCollection()) {
/* 145 */       return new ListTransducedAccessorImpl(xducer, prop.getAccessor(), Lister.create((Type)Utils.REFLECTION_NAVIGATOR.erasure(prop.getRawType()), prop.id(), prop.getAdapter()));
/*     */     }
/*     */ 
/* 149 */     if (prop.id() == ID.IDREF) {
/* 150 */       return new IDREFTransducedAccessorImpl(prop.getAccessor());
/*     */     }
/* 152 */     if ((xducer.isDefault()) && (context != null) && (!context.fastBoot)) {
/* 153 */       TransducedAccessor xa = OptimizedTransducedAccessorFactory.get(prop);
/* 154 */       if (xa != null) return xa;
/*     */     }
/*     */ 
/* 157 */     if (xducer.useNamespace()) {
/* 158 */       return new CompositeContextDependentTransducedAccessorImpl(context, xducer, prop.getAccessor());
/*     */     }
/* 160 */     return new CompositeTransducedAccessorImpl(context, xducer, prop.getAccessor());
/*     */   }
/*     */ 
/*     */   public abstract void writeLeafElement(XMLSerializer paramXMLSerializer, Name paramName, BeanT paramBeanT, String paramString)
/*     */     throws SAXException, AccessorException, IOException, XMLStreamException;
/*     */ 
/*     */   public abstract void writeText(XMLSerializer paramXMLSerializer, BeanT paramBeanT, String paramString)
/*     */     throws AccessorException, SAXException, IOException, XMLStreamException;
/*     */ 
/*     */   static class CompositeContextDependentTransducedAccessorImpl<BeanT, ValueT> extends TransducedAccessor.CompositeTransducedAccessorImpl<BeanT, ValueT>
/*     */   {
/*     */     public CompositeContextDependentTransducedAccessorImpl(JAXBContextImpl context, Transducer<ValueT> xducer, Accessor<BeanT, ValueT> acc)
/*     */     {
/* 182 */       super(xducer, acc);
/* 183 */       assert (xducer.useNamespace());
/*     */     }
/*     */ 
/*     */     public boolean useNamespace()
/*     */     {
/* 188 */       return true;
/*     */     }
/*     */ 
/*     */     public void declareNamespace(BeanT bean, XMLSerializer w) throws AccessorException
/*     */     {
/* 193 */       Object o = this.acc.get(bean);
/* 194 */       if (o != null)
/* 195 */         this.xducer.declareNamespace(o, w);
/*     */     }
/*     */ 
/*     */     public void writeLeafElement(XMLSerializer w, Name tagName, BeanT o, String fieldName) throws SAXException, AccessorException, IOException, XMLStreamException
/*     */     {
/* 200 */       w.startElement(tagName, null);
/* 201 */       declareNamespace(o, w);
/* 202 */       w.endNamespaceDecls(null);
/* 203 */       w.endAttributes();
/* 204 */       this.xducer.writeText(w, this.acc.get(o), fieldName);
/* 205 */       w.endElement();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class CompositeTransducedAccessorImpl<BeanT, ValueT> extends TransducedAccessor<BeanT>
/*     */   {
/*     */     protected final Transducer<ValueT> xducer;
/*     */     protected final Accessor<BeanT, ValueT> acc;
/*     */ 
/*     */     public CompositeTransducedAccessorImpl(JAXBContextImpl context, Transducer<ValueT> xducer, Accessor<BeanT, ValueT> acc)
/*     */     {
/* 219 */       this.xducer = xducer;
/* 220 */       this.acc = acc.optimize(context);
/*     */     }
/*     */ 
/*     */     public CharSequence print(BeanT bean) throws AccessorException {
/* 224 */       Object o = this.acc.get(bean);
/* 225 */       if (o == null) return null;
/* 226 */       return this.xducer.print(o);
/*     */     }
/*     */ 
/*     */     public void parse(BeanT bean, CharSequence lexical) throws AccessorException, SAXException {
/* 230 */       this.acc.set(bean, this.xducer.parse(lexical));
/*     */     }
/*     */ 
/*     */     public boolean hasValue(BeanT bean) throws AccessorException {
/* 234 */       return this.acc.getUnadapted(bean) != null;
/*     */     }
/*     */ 
/*     */     public void writeLeafElement(XMLSerializer w, Name tagName, BeanT o, String fieldName) throws SAXException, AccessorException, IOException, XMLStreamException
/*     */     {
/* 239 */       this.xducer.writeLeafElement(w, tagName, this.acc.get(o), fieldName);
/*     */     }
/*     */ 
/*     */     public void writeText(XMLSerializer w, BeanT o, String fieldName) throws AccessorException, SAXException, IOException, XMLStreamException
/*     */     {
/* 244 */       this.xducer.writeText(w, this.acc.get(o), fieldName);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class IDREFTransducedAccessorImpl<BeanT, TargetT> extends DefaultTransducedAccessor<BeanT>
/*     */   {
/*     */     private final Accessor<BeanT, TargetT> acc;
/*     */     private final Class<TargetT> targetType;
/*     */ 
/*     */     public IDREFTransducedAccessorImpl(Accessor<BeanT, TargetT> acc)
/*     */     {
/* 263 */       this.acc = acc;
/* 264 */       this.targetType = acc.getValueType();
/*     */     }
/*     */ 
/*     */     public String print(BeanT bean) throws AccessorException, SAXException {
/* 268 */       Object target = this.acc.get(bean);
/* 269 */       if (target == null) return null;
/*     */ 
/* 271 */       XMLSerializer w = XMLSerializer.getInstance();
/*     */       try {
/* 273 */         String id = w.grammar.getBeanInfo(target, true).getId(target, w);
/* 274 */         if (id == null)
/* 275 */           w.errorMissingId(target);
/* 276 */         return id;
/*     */       } catch (JAXBException e) {
/* 278 */         w.reportError(null, e);
/* 279 */       }return null;
/*     */     }
/*     */ 
/*     */     private void assign(BeanT bean, TargetT t, UnmarshallingContext context) throws AccessorException
/*     */     {
/* 284 */       if (!this.targetType.isInstance(t))
/* 285 */         context.handleError(Messages.UNASSIGNABLE_TYPE.format(new Object[] { this.targetType, t.getClass() }));
/*     */       else
/* 287 */         this.acc.set(bean, t);
/*     */     }
/*     */ 
/*     */     public void parse(final BeanT bean, CharSequence lexical) throws AccessorException, SAXException {
/* 291 */       final String idref = WhiteSpaceProcessor.trim(lexical).toString();
/* 292 */       final UnmarshallingContext context = UnmarshallingContext.getInstance();
/*     */ 
/* 294 */       final Callable callable = context.getObjectFromId(idref, this.acc.valueType);
/* 295 */       if (callable == null)
/*     */       {
/* 297 */         context.errorUnresolvedIDREF(bean, idref, context.getLocator());
/*     */         return;
/*     */       }
/*     */       Object t;
/*     */       try {
/* 303 */         t = callable.call();
/*     */       } catch (SAXException e) {
/* 305 */         throw e;
/*     */       } catch (RuntimeException e) {
/* 307 */         throw e;
/*     */       } catch (Exception e) {
/* 309 */         throw new SAXException2(e);
/*     */       }
/* 311 */       if (t != null) {
/* 312 */         assign(bean, t, context);
/*     */       }
/*     */       else {
/* 315 */         final LocatorEx loc = new LocatorEx.Snapshot(context.getLocator());
/* 316 */         context.addPatcher(new Patcher() {
/*     */           public void run() throws SAXException {
/*     */             try {
/* 319 */               Object t = callable.call();
/* 320 */               if (t == null)
/* 321 */                 context.errorUnresolvedIDREF(bean, idref, loc);
/*     */               else
/* 323 */                 TransducedAccessor.IDREFTransducedAccessorImpl.this.assign(bean, t, context);
/*     */             }
/*     */             catch (AccessorException e) {
/* 326 */               context.handleError(e);
/*     */             } catch (SAXException e) {
/* 328 */               throw e;
/*     */             } catch (RuntimeException e) {
/* 330 */               throw e;
/*     */             } catch (Exception e) {
/* 332 */               throw new SAXException2(e);
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean hasValue(BeanT bean) throws AccessorException {
/* 340 */       return this.acc.get(bean) != null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
 * JD-Core Version:    0.6.2
 */