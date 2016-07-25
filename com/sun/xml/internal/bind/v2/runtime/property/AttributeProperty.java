/*     */ package com.sun.xml.internal.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeAttributePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.NameBuilder;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.internal.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class AttributeProperty<BeanT> extends PropertyImpl<BeanT>
/*     */   implements Comparable<AttributeProperty>
/*     */ {
/*     */   public final Name attName;
/*     */   public final TransducedAccessor<BeanT> xacc;
/*     */   private final Accessor acc;
/*     */ 
/*     */   public AttributeProperty(JAXBContextImpl context, RuntimeAttributePropertyInfo prop)
/*     */   {
/*  74 */     super(context, prop);
/*  75 */     this.attName = context.nameBuilder.createAttributeName(prop.getXmlName());
/*  76 */     this.xacc = TransducedAccessor.get(context, prop);
/*  77 */     this.acc = prop.getAccessor();
/*     */   }
/*     */ 
/*     */   public void serializeAttributes(BeanT o, XMLSerializer w)
/*     */     throws SAXException, AccessorException, IOException, XMLStreamException
/*     */   {
/*  86 */     CharSequence value = this.xacc.print(o);
/*  87 */     if (value != null)
/*  88 */       w.attribute(this.attName, value.toString());
/*     */   }
/*     */ 
/*     */   public void serializeURIs(BeanT o, XMLSerializer w) throws AccessorException, SAXException {
/*  92 */     this.xacc.declareNamespace(o, w);
/*     */   }
/*     */ 
/*     */   public boolean hasSerializeURIAction() {
/*  96 */     return this.xacc.useNamespace();
/*     */   }
/*     */ 
/*     */   public void buildChildElementUnmarshallers(UnmarshallerChain chainElem, QNameMap<ChildLoader> handlers) {
/* 100 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   public PropertyKind getKind()
/*     */   {
/* 105 */     return PropertyKind.ATTRIBUTE;
/*     */   }
/*     */ 
/*     */   public void reset(BeanT o) throws AccessorException {
/* 109 */     this.acc.set(o, null);
/*     */   }
/*     */ 
/*     */   public String getIdValue(BeanT bean) throws AccessorException, SAXException {
/* 113 */     return this.xacc.print(bean).toString();
/*     */   }
/*     */ 
/*     */   public int compareTo(AttributeProperty that) {
/* 117 */     return this.attName.compareTo(that.attName);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.AttributeProperty
 * JD-Core Version:    0.6.2
 */