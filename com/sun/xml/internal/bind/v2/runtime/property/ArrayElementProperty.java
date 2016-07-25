/*     */ package com.sun.xml.internal.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeRef;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeRef;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.NameBuilder;
/*     */ import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Lister.IDREFSIterator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.NullSafeAccessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.DefaultValueLoaderDecorator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TextLoader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
/*     */ import com.sun.xml.internal.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ abstract class ArrayElementProperty<BeanT, ListT, ItemT> extends ArrayERProperty<BeanT, ListT, ItemT>
/*     */ {
/*  69 */   private final Map<Class, TagAndType> typeMap = new HashMap();
/*     */ 
/*  73 */   private Map<TypeRef<Type, Class>, JaxBeanInfo> refs = new HashMap();
/*     */   protected RuntimeElementPropertyInfo prop;
/*     */   private final Name nillableTagName;
/*     */ 
/*     */   protected ArrayElementProperty(JAXBContextImpl grammar, RuntimeElementPropertyInfo prop)
/*     */   {
/*  85 */     super(grammar, prop, prop.getXmlName(), prop.isCollectionNillable());
/*  86 */     this.prop = prop;
/*     */ 
/*  88 */     List types = prop.getTypes();
/*     */ 
/*  90 */     Name n = null;
/*     */ 
/*  92 */     for (RuntimeTypeRef typeRef : types) {
/*  93 */       Class type = (Class)typeRef.getTarget().getType();
/*  94 */       if (type.isPrimitive()) {
/*  95 */         type = (Class)RuntimeUtil.primitiveToBox.get(type);
/*     */       }
/*  97 */       JaxBeanInfo beanInfo = grammar.getOrCreate(typeRef.getTarget());
/*  98 */       TagAndType tt = new TagAndType(grammar.nameBuilder.createElementName(typeRef.getTagName()), beanInfo);
/*     */ 
/* 101 */       this.typeMap.put(type, tt);
/* 102 */       this.refs.put(typeRef, beanInfo);
/* 103 */       if ((typeRef.isNillable()) && (n == null)) {
/* 104 */         n = tt.tagName;
/*     */       }
/*     */     }
/* 107 */     this.nillableTagName = n;
/*     */   }
/*     */ 
/*     */   public void wrapUp()
/*     */   {
/* 112 */     super.wrapUp();
/* 113 */     this.refs = null;
/* 114 */     this.prop = null;
/*     */   }
/*     */ 
/*     */   protected void serializeListBody(BeanT beanT, XMLSerializer w, ListT list) throws IOException, XMLStreamException, SAXException, AccessorException {
/* 118 */     ListIterator itr = this.lister.iterator(list, w);
/*     */ 
/* 120 */     boolean isIdref = itr instanceof Lister.IDREFSIterator;
/*     */ 
/* 122 */     while (itr.hasNext())
/*     */       try {
/* 124 */         Object item = itr.next();
/* 125 */         if (item != null) {
/* 126 */           Class itemType = item.getClass();
/* 127 */           if (isIdref)
/*     */           {
/* 130 */             itemType = ((Lister.IDREFSIterator)itr).last().getClass();
/*     */           }
/*     */ 
/* 133 */           TagAndType tt = (TagAndType)this.typeMap.get(itemType);
/* 134 */           while ((tt == null) && (itemType != null))
/*     */           {
/* 136 */             itemType = itemType.getSuperclass();
/* 137 */             tt = (TagAndType)this.typeMap.get(itemType);
/*     */           }
/*     */ 
/* 140 */           if (tt == null)
/*     */           {
/* 153 */             w.startElement(((TagAndType)this.typeMap.values().iterator().next()).tagName, null);
/* 154 */             w.childAsXsiType(item, this.fieldName, w.grammar.getBeanInfo(Object.class), false);
/*     */           } else {
/* 156 */             w.startElement(tt.tagName, null);
/* 157 */             serializeItem(tt.beanInfo, item, w);
/*     */           }
/*     */ 
/* 160 */           w.endElement();
/*     */         }
/* 162 */         else if (this.nillableTagName != null) {
/* 163 */           w.startElement(this.nillableTagName, null);
/* 164 */           w.writeXsiNilTrue();
/* 165 */           w.endElement();
/*     */         }
/*     */       }
/*     */       catch (JAXBException e) {
/* 169 */         w.reportError(this.fieldName, e);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected abstract void serializeItem(JaxBeanInfo paramJaxBeanInfo, ItemT paramItemT, XMLSerializer paramXMLSerializer)
/*     */     throws SAXException, AccessorException, IOException, XMLStreamException;
/*     */ 
/*     */   public void createBodyUnmarshaller(UnmarshallerChain chain, QNameMap<ChildLoader> loaders)
/*     */   {
/* 185 */     int offset = chain.allocateOffset();
/* 186 */     Receiver recv = new ArrayERProperty.ReceiverImpl(this, offset);
/*     */ 
/* 188 */     for (RuntimeTypeRef typeRef : this.prop.getTypes())
/*     */     {
/* 190 */       Name tagName = chain.context.nameBuilder.createElementName(typeRef.getTagName());
/* 191 */       Loader item = createItemUnmarshaller(chain, typeRef);
/*     */ 
/* 193 */       if ((typeRef.isNillable()) || (chain.context.allNillable))
/* 194 */         item = new XsiNilLoader.Array(item);
/* 195 */       if (typeRef.getDefaultValue() != null) {
/* 196 */         item = new DefaultValueLoaderDecorator(item, typeRef.getDefaultValue());
/*     */       }
/* 198 */       loaders.put(tagName, new ChildLoader(item, recv));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final PropertyKind getKind() {
/* 203 */     return PropertyKind.ELEMENT;
/*     */   }
/*     */ 
/*     */   private Loader createItemUnmarshaller(UnmarshallerChain chain, RuntimeTypeRef typeRef)
/*     */   {
/* 220 */     if (PropertyFactory.isLeaf(typeRef.getSource())) {
/* 221 */       Transducer xducer = typeRef.getTransducer();
/* 222 */       return new TextLoader(xducer);
/*     */     }
/* 224 */     return ((JaxBeanInfo)this.refs.get(typeRef)).getLoader(chain.context, true);
/*     */   }
/*     */ 
/*     */   public Accessor getElementPropertyAccessor(String nsUri, String localName)
/*     */   {
/* 229 */     if (this.wrapperTagName != null) {
/* 230 */       if (this.wrapperTagName.equals(nsUri, localName))
/* 231 */         return this.acc;
/*     */     }
/* 233 */     else for (TagAndType tt : this.typeMap.values()) {
/* 234 */         if (tt.tagName.equals(nsUri, localName))
/*     */         {
/* 239 */           return new NullSafeAccessor(this.acc, this.lister);
/*     */         }
/*     */       }
/* 242 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.ArrayElementProperty
 * JD-Core Version:    0.6.2
 */