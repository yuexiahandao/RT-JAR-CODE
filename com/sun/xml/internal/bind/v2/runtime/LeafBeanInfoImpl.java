/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeLeafInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TextLoader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiTypeLoader;
/*     */ import java.io.IOException;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class LeafBeanInfoImpl<BeanT> extends JaxBeanInfo<BeanT>
/*     */ {
/*     */   private final Loader loader;
/*     */   private final Loader loaderWithSubst;
/*     */   private final Transducer<BeanT> xducer;
/*     */   private final Name tagName;
/*     */ 
/*     */   public LeafBeanInfoImpl(JAXBContextImpl grammar, RuntimeLeafInfo li)
/*     */   {
/*  70 */     super(grammar, li, li.getClazz(), li.getTypeNames(), li.isElement(), true, false);
/*     */ 
/*  72 */     this.xducer = li.getTransducer();
/*  73 */     this.loader = new TextLoader(this.xducer);
/*  74 */     this.loaderWithSubst = new XsiTypeLoader(this);
/*     */ 
/*  76 */     if (isElement())
/*  77 */       this.tagName = grammar.nameBuilder.createElementName(li.getElementName());
/*     */     else
/*  79 */       this.tagName = null;
/*     */   }
/*     */ 
/*     */   public QName getTypeName(BeanT instance) {
/*  83 */     QName tn = this.xducer.getTypeName(instance);
/*  84 */     if (tn != null) return tn;
/*     */ 
/*  86 */     return super.getTypeName(instance);
/*     */   }
/*     */ 
/*     */   public final String getElementNamespaceURI(BeanT _) {
/*  90 */     return this.tagName.nsUri;
/*     */   }
/*     */ 
/*     */   public final String getElementLocalName(BeanT _) {
/*  94 */     return this.tagName.localName;
/*     */   }
/*     */ 
/*     */   public BeanT createInstance(UnmarshallingContext context) {
/*  98 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public final boolean reset(BeanT bean, UnmarshallingContext context) {
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   public final String getId(BeanT bean, XMLSerializer target) {
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   public final void serializeBody(BeanT bean, XMLSerializer w)
/*     */     throws SAXException, IOException, XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 114 */       this.xducer.writeText(w, bean, null);
/*     */     } catch (AccessorException e) {
/* 116 */       w.reportError(null, e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void serializeAttributes(BeanT bean, XMLSerializer target)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void serializeRoot(BeanT bean, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 125 */     if (this.tagName == null) {
/* 126 */       target.reportError(new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(new Object[] { bean.getClass().getName() }), null, null));
/*     */     }
/*     */     else
/*     */     {
/* 134 */       target.startElement(this.tagName, bean);
/* 135 */       target.childAsSoleContent(bean, null);
/* 136 */       target.endElement();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void serializeURIs(BeanT bean, XMLSerializer target)
/*     */     throws SAXException
/*     */   {
/* 143 */     if (this.xducer.useNamespace())
/*     */       try {
/* 145 */         this.xducer.declareNamespace(bean, target);
/*     */       } catch (AccessorException e) {
/* 147 */         target.reportError(null, e);
/*     */       }
/*     */   }
/*     */ 
/*     */   public final Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable)
/*     */   {
/* 153 */     if (typeSubstitutionCapable) {
/* 154 */       return this.loaderWithSubst;
/*     */     }
/* 156 */     return this.loader;
/*     */   }
/*     */ 
/*     */   public Transducer<BeanT> getTransducer() {
/* 160 */     return this.xducer;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.LeafBeanInfoImpl
 * JD-Core Version:    0.6.2
 */