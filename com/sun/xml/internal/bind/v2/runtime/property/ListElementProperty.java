/*     */ package com.sun.xml.internal.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeRef;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.NameBuilder;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.ListTransducedAccessorImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LeafPropertyLoader;
/*     */ import com.sun.xml.internal.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class ListElementProperty<BeanT, ListT, ItemT> extends ArrayProperty<BeanT, ListT, ItemT>
/*     */ {
/*     */   private final Name tagName;
/*     */   private final TransducedAccessor<BeanT> xacc;
/*     */ 
/*     */   public ListElementProperty(JAXBContextImpl grammar, RuntimeElementPropertyInfo prop)
/*     */   {
/*  65 */     super(grammar, prop);
/*     */ 
/*  67 */     assert (prop.isValueList());
/*  68 */     assert (prop.getTypes().size() == 1);
/*  69 */     RuntimeTypeRef ref = (RuntimeTypeRef)prop.getTypes().get(0);
/*     */ 
/*  71 */     this.tagName = grammar.nameBuilder.createElementName(ref.getTagName());
/*     */ 
/*  74 */     Transducer xducer = ref.getTransducer();
/*     */ 
/*  76 */     this.xacc = new ListTransducedAccessorImpl(xducer, this.acc, this.lister);
/*     */   }
/*     */ 
/*     */   public PropertyKind getKind() {
/*  80 */     return PropertyKind.ELEMENT;
/*     */   }
/*     */ 
/*     */   public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
/*  84 */     handlers.put(this.tagName, new ChildLoader(new LeafPropertyLoader(this.xacc), null));
/*     */   }
/*     */ 
/*     */   public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException
/*     */   {
/*  89 */     Object list = this.acc.get(o);
/*     */ 
/*  91 */     if (list != null)
/*  92 */       if (this.xacc.useNamespace()) {
/*  93 */         w.startElement(this.tagName, null);
/*  94 */         this.xacc.declareNamespace(o, w);
/*  95 */         w.endNamespaceDecls(list);
/*  96 */         w.endAttributes();
/*  97 */         this.xacc.writeText(w, o, this.fieldName);
/*  98 */         w.endElement();
/*     */       } else {
/* 100 */         this.xacc.writeLeafElement(w, this.tagName, o, this.fieldName);
/*     */       }
/*     */   }
/*     */ 
/*     */   public Accessor getElementPropertyAccessor(String nsUri, String localName)
/*     */   {
/* 107 */     if ((this.tagName != null) && 
/* 108 */       (this.tagName.equals(nsUri, localName))) {
/* 109 */       return this.acc;
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.ListElementProperty
 * JD-Core Version:    0.6.2
 */