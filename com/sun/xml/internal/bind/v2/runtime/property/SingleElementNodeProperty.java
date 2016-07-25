/*     */ package com.sun.xml.internal.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeRef;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeRef;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.NameBuilder;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.DefaultValueLoaderDecorator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Single;
/*     */ import com.sun.xml.internal.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class SingleElementNodeProperty<BeanT, ValueT> extends PropertyImpl<BeanT>
/*     */ {
/*     */   private final Accessor<BeanT, ValueT> acc;
/*     */   private final boolean nillable;
/*     */   private final QName[] acceptedElements;
/*  68 */   private final Map<Class, TagAndType> typeNames = new HashMap();
/*     */   private RuntimeElementPropertyInfo prop;
/*     */   private final Name nullTagName;
/*     */ 
/*     */   public SingleElementNodeProperty(JAXBContextImpl context, RuntimeElementPropertyInfo prop)
/*     */   {
/*  78 */     super(context, prop);
/*  79 */     this.acc = prop.getAccessor().optimize(context);
/*  80 */     this.prop = prop;
/*     */ 
/*  82 */     QName nt = null;
/*  83 */     boolean nil = false;
/*     */ 
/*  85 */     this.acceptedElements = new QName[prop.getTypes().size()];
/*  86 */     for (int i = 0; i < this.acceptedElements.length; i++) {
/*  87 */       this.acceptedElements[i] = ((RuntimeTypeRef)prop.getTypes().get(i)).getTagName();
/*     */     }
/*  89 */     for (RuntimeTypeRef e : prop.getTypes()) {
/*  90 */       JaxBeanInfo beanInfo = context.getOrCreate(e.getTarget());
/*  91 */       if (nt == null) nt = e.getTagName();
/*  92 */       this.typeNames.put(beanInfo.jaxbType, new TagAndType(context.nameBuilder.createElementName(e.getTagName()), beanInfo));
/*     */ 
/*  94 */       nil |= e.isNillable();
/*     */     }
/*     */ 
/*  97 */     this.nullTagName = context.nameBuilder.createElementName(nt);
/*     */ 
/*  99 */     this.nillable = nil;
/*     */   }
/*     */ 
/*     */   public void wrapUp()
/*     */   {
/* 104 */     super.wrapUp();
/* 105 */     this.prop = null;
/*     */   }
/*     */ 
/*     */   public void reset(BeanT bean) throws AccessorException {
/* 109 */     this.acc.set(bean, null);
/*     */   }
/*     */ 
/*     */   public String getIdValue(BeanT beanT) {
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */   public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException
/*     */   {
/* 118 */     Object v = this.acc.get(o);
/* 119 */     if (v != null) {
/* 120 */       Class vtype = v.getClass();
/* 121 */       TagAndType tt = (TagAndType)this.typeNames.get(vtype);
/*     */ 
/* 123 */       if (tt == null) {
/* 124 */         for (Map.Entry e : this.typeNames.entrySet()) {
/* 125 */           if (((Class)e.getKey()).isAssignableFrom(vtype)) {
/* 126 */             tt = (TagAndType)e.getValue();
/* 127 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 132 */       boolean addNilDecl = ((o instanceof JAXBElement)) && (((JAXBElement)o).isNil());
/* 133 */       if (tt == null)
/*     */       {
/* 139 */         w.startElement(((TagAndType)this.typeNames.values().iterator().next()).tagName, null);
/* 140 */         w.childAsXsiType(v, this.fieldName, w.grammar.getBeanInfo(Object.class), (addNilDecl) && (this.nillable));
/*     */       } else {
/* 142 */         w.startElement(tt.tagName, null);
/* 143 */         w.childAsXsiType(v, this.fieldName, tt.beanInfo, (addNilDecl) && (this.nillable));
/*     */       }
/* 145 */       w.endElement();
/* 146 */     } else if (this.nillable) {
/* 147 */       w.startElement(this.nullTagName, null);
/* 148 */       w.writeXsiNilTrue();
/* 149 */       w.endElement();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
/* 154 */     JAXBContextImpl context = chain.context;
/*     */ 
/* 156 */     for (TypeRef e : this.prop.getTypes()) {
/* 157 */       JaxBeanInfo bi = context.getOrCreate((RuntimeTypeInfo)e.getTarget());
/*     */ 
/* 160 */       Loader l = bi.getLoader(context, !Modifier.isFinal(bi.jaxbType.getModifiers()));
/* 161 */       if (e.getDefaultValue() != null)
/* 162 */         l = new DefaultValueLoaderDecorator(l, e.getDefaultValue());
/* 163 */       if ((this.nillable) || (chain.context.allNillable))
/* 164 */         l = new XsiNilLoader.Single(l, this.acc);
/* 165 */       handlers.put(e.getTagName(), new ChildLoader(l, this.acc));
/*     */     }
/*     */   }
/*     */ 
/*     */   public PropertyKind getKind() {
/* 170 */     return PropertyKind.ELEMENT;
/*     */   }
/*     */ 
/*     */   public Accessor getElementPropertyAccessor(String nsUri, String localName)
/*     */   {
/* 175 */     for (QName n : this.acceptedElements) {
/* 176 */       if ((n.getNamespaceURI().equals(nsUri)) && (n.getLocalPart().equals(localName)))
/* 177 */         return this.acc;
/*     */     }
/* 179 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.SingleElementNodeProperty
 * JD-Core Version:    0.6.2
 */