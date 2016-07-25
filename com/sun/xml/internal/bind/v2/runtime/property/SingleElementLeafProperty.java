/*     */ package com.sun.xml.internal.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeRef;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.NameBuilder;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.DefaultValueLoaderDecorator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LeafPropertyLoader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LeafPropertyXsiLoader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Single;
/*     */ import com.sun.xml.internal.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class SingleElementLeafProperty<BeanT> extends PropertyImpl<BeanT>
/*     */ {
/*     */   private final Name tagName;
/*     */   private final boolean nillable;
/*     */   private final Accessor acc;
/*     */   private final String defaultValue;
/*     */   private final TransducedAccessor<BeanT> xacc;
/*     */   private final boolean improvedXsiTypeHandling;
/*     */ 
/*     */   public SingleElementLeafProperty(JAXBContextImpl context, RuntimeElementPropertyInfo prop)
/*     */   {
/*  66 */     super(context, prop);
/*  67 */     RuntimeTypeRef ref = (RuntimeTypeRef)prop.getTypes().get(0);
/*  68 */     this.tagName = context.nameBuilder.createElementName(ref.getTagName());
/*  69 */     assert (this.tagName != null);
/*  70 */     this.nillable = ref.isNillable();
/*  71 */     this.defaultValue = ref.getDefaultValue();
/*  72 */     this.acc = prop.getAccessor().optimize(context);
/*     */ 
/*  74 */     this.xacc = TransducedAccessor.get(context, ref);
/*  75 */     assert (this.xacc != null);
/*     */ 
/*  77 */     this.improvedXsiTypeHandling = context.improvedXsiTypeHandling;
/*     */   }
/*     */ 
/*     */   public void reset(BeanT o) throws AccessorException {
/*  81 */     this.acc.set(o, null);
/*     */   }
/*     */ 
/*     */   public String getIdValue(BeanT bean) throws AccessorException, SAXException {
/*  85 */     return this.xacc.print(bean).toString();
/*     */   }
/*     */ 
/*     */   public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException
/*     */   {
/*  90 */     boolean hasValue = this.xacc.hasValue(o);
/*     */ 
/*  92 */     Object obj = null;
/*     */     try
/*     */     {
/*  95 */       obj = this.acc.getUnadapted(o);
/*     */     }
/*     */     catch (AccessorException ae)
/*     */     {
/*     */     }
/* 100 */     Class valueType = this.acc.getValueType();
/*     */ 
/* 103 */     if ((this.improvedXsiTypeHandling) && (!this.acc.isAdapted()) && (obj != null) && (!obj.getClass().equals(valueType)) && (!valueType.isPrimitive()) && (this.acc.isValueTypeAbstractable()))
/*     */     {
/* 107 */       w.startElement(this.tagName, outerPeer);
/* 108 */       w.childAsXsiType(obj, this.fieldName, w.grammar.getBeanInfo(valueType), this.nillable);
/* 109 */       w.endElement();
/*     */     }
/* 113 */     else if (hasValue) {
/* 114 */       this.xacc.writeLeafElement(w, this.tagName, o, this.fieldName);
/* 115 */     } else if (this.nillable) {
/* 116 */       w.startElement(this.tagName, null);
/* 117 */       w.writeXsiNilTrue();
/* 118 */       w.endElement();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers)
/*     */   {
/* 124 */     Loader l = new LeafPropertyLoader(this.xacc);
/* 125 */     if (this.defaultValue != null)
/* 126 */       l = new DefaultValueLoaderDecorator(l, this.defaultValue);
/* 127 */     if ((this.nillable) || (chain.context.allNillable)) {
/* 128 */       l = new XsiNilLoader.Single(l, this.acc);
/*     */     }
/*     */ 
/* 131 */     if ((this.improvedXsiTypeHandling) && (!this.nillable)) {
/* 132 */       l = new LeafPropertyXsiLoader(l, this.xacc, this.acc);
/*     */     }
/* 134 */     handlers.put(this.tagName, new ChildLoader(l, null));
/*     */   }
/*     */ 
/*     */   public PropertyKind getKind()
/*     */   {
/* 139 */     return PropertyKind.ELEMENT;
/*     */   }
/*     */ 
/*     */   public Accessor getElementPropertyAccessor(String nsUri, String localName)
/*     */   {
/* 144 */     if (this.tagName.equals(nsUri, localName)) {
/* 145 */       return this.acc;
/*     */     }
/* 147 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.SingleElementLeafProperty
 * JD-Core Version:    0.6.2
 */