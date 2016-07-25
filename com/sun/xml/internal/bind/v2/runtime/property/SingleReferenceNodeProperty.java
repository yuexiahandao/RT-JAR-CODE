/*     */ package com.sun.xml.internal.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.ClassFactory;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.core.WildcardMode;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElement;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeReferencePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.ElementBeanInfoImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.WildcardLoader;
/*     */ import com.sun.xml.internal.bind.v2.util.QNameMap;
/*     */ import com.sun.xml.internal.bind.v2.util.QNameMap.Entry;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.annotation.DomHandler;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class SingleReferenceNodeProperty<BeanT, ValueT> extends PropertyImpl<BeanT>
/*     */ {
/*     */   private final Accessor<BeanT, ValueT> acc;
/*  60 */   private final QNameMap<JaxBeanInfo> expectedElements = new QNameMap();
/*     */   private final DomHandler domHandler;
/*     */   private final WildcardMode wcMode;
/*     */ 
/*     */   public SingleReferenceNodeProperty(JAXBContextImpl context, RuntimeReferencePropertyInfo prop)
/*     */   {
/*  66 */     super(context, prop);
/*  67 */     this.acc = prop.getAccessor().optimize(context);
/*     */ 
/*  69 */     for (RuntimeElement e : prop.getElements()) {
/*  70 */       this.expectedElements.put(e.getElementName(), context.getOrCreate(e));
/*     */     }
/*     */ 
/*  73 */     if (prop.getWildcard() != null) {
/*  74 */       this.domHandler = ((DomHandler)ClassFactory.create((Class)prop.getDOMHandler()));
/*  75 */       this.wcMode = prop.getWildcard();
/*     */     } else {
/*  77 */       this.domHandler = null;
/*  78 */       this.wcMode = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset(BeanT bean) throws AccessorException {
/*  83 */     this.acc.set(bean, null);
/*     */   }
/*     */ 
/*     */   public String getIdValue(BeanT beanT) {
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException {
/*  91 */     Object v = this.acc.get(o);
/*  92 */     if (v != null)
/*     */       try {
/*  94 */         JaxBeanInfo bi = w.grammar.getBeanInfo(v, true);
/*  95 */         if ((bi.jaxbType == Object.class) && (this.domHandler != null))
/*     */         {
/*  98 */           w.writeDom(v, this.domHandler, o, this.fieldName);
/*     */         }
/* 100 */         else bi.serializeRoot(v, w); 
/*     */       }
/* 102 */       catch (JAXBException e) { w.reportError(this.fieldName, e); }
/*     */ 
/*     */   }
/*     */ 
/*     */   public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers)
/*     */   {
/* 109 */     for (QNameMap.Entry n : this.expectedElements.entrySet()) {
/* 110 */       handlers.put(n.nsUri, n.localName, new ChildLoader(((JaxBeanInfo)n.getValue()).getLoader(chain.context, true), this.acc));
/*     */     }
/* 112 */     if (this.domHandler != null)
/* 113 */       handlers.put(CATCH_ALL, new ChildLoader(new WildcardLoader(this.domHandler, this.wcMode), this.acc));
/*     */   }
/*     */ 
/*     */   public PropertyKind getKind()
/*     */   {
/* 118 */     return PropertyKind.REFERENCE;
/*     */   }
/*     */ 
/*     */   public Accessor getElementPropertyAccessor(String nsUri, String localName)
/*     */   {
/* 123 */     JaxBeanInfo bi = (JaxBeanInfo)this.expectedElements.get(nsUri, localName);
/* 124 */     if (bi != null) {
/* 125 */       if ((bi instanceof ElementBeanInfoImpl)) {
/* 126 */         final ElementBeanInfoImpl ebi = (ElementBeanInfoImpl)bi;
/*     */ 
/* 128 */         return new Accessor(ebi.expectedType) {
/*     */           public Object get(BeanT bean) throws AccessorException {
/* 130 */             Object r = SingleReferenceNodeProperty.this.acc.get(bean);
/* 131 */             if ((r instanceof JAXBElement)) {
/* 132 */               return ((JAXBElement)r).getValue();
/*     */             }
/*     */ 
/* 135 */             return r;
/*     */           }
/*     */ 
/*     */           public void set(BeanT bean, Object value) throws AccessorException {
/* 139 */             if (value != null) {
/*     */               try {
/* 141 */                 value = ebi.createInstanceFromValue(value);
/*     */               } catch (IllegalAccessException e) {
/* 143 */                 throw new AccessorException(e);
/*     */               } catch (InvocationTargetException e) {
/* 145 */                 throw new AccessorException(e);
/*     */               } catch (InstantiationException e) {
/* 147 */                 throw new AccessorException(e);
/*     */               }
/*     */             }
/* 150 */             SingleReferenceNodeProperty.this.acc.set(bean, value);
/*     */           }
/*     */         };
/*     */       }
/*     */ 
/* 155 */       return this.acc;
/*     */     }
/*     */ 
/* 158 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.SingleReferenceNodeProperty
 * JD-Core Version:    0.6.2
 */