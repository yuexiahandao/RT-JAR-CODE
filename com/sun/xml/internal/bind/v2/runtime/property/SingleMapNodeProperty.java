/*     */ package com.sun.xml.internal.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.ClassFactory;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeMapPropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.NameBuilder;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext.State;
/*     */ import com.sun.xml.internal.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class SingleMapNodeProperty<BeanT, ValueT extends Map> extends PropertyImpl<BeanT>
/*     */ {
/*     */   private final Accessor<BeanT, ValueT> acc;
/*     */   private final Name tagName;
/*     */   private final Name entryTag;
/*     */   private final Name keyTag;
/*     */   private final Name valueTag;
/*     */   private final boolean nillable;
/*     */   private JaxBeanInfo keyBeanInfo;
/*     */   private JaxBeanInfo valueBeanInfo;
/*     */   private final Class<? extends ValueT> mapImplClass;
/* 107 */   private static final Class[] knownImplClasses = { HashMap.class, TreeMap.class, LinkedHashMap.class };
/*     */   private Loader keyLoader;
/*     */   private Loader valueLoader;
/* 141 */   private final Loader itemsLoader = new Loader(false)
/*     */   {
/* 143 */     private ThreadLocal<BeanT> target = new ThreadLocal();
/* 144 */     private ThreadLocal<ValueT> map = new ThreadLocal();
/*     */ 
/*     */     public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */     {
/*     */       try
/*     */       {
/* 150 */         this.target.set(state.getPrev().getTarget());
/* 151 */         this.map.set(SingleMapNodeProperty.this.acc.get(this.target.get()));
/* 152 */         if (this.map.get() == null) {
/* 153 */           this.map.set(ClassFactory.create(SingleMapNodeProperty.this.mapImplClass));
/*     */         }
/* 155 */         ((Map)this.map.get()).clear();
/* 156 */         state.setTarget(this.map.get());
/*     */       }
/*     */       catch (AccessorException e) {
/* 159 */         handleGenericException(e, true);
/* 160 */         state.setTarget(new HashMap());
/*     */       }
/*     */     }
/*     */ 
/*     */     public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */     {
/* 166 */       super.leaveElement(state, ea);
/*     */       try {
/* 168 */         SingleMapNodeProperty.this.acc.set(this.target.get(), this.map.get());
/* 169 */         this.target.remove();
/*     */       } catch (AccessorException ex) {
/* 171 */         handleGenericException(ex, true);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */     {
/* 177 */       if (ea.matches(SingleMapNodeProperty.this.entryTag))
/* 178 */         state.setLoader(SingleMapNodeProperty.this.entryLoader);
/*     */       else
/* 180 */         super.childElement(state, ea);
/*     */     }
/*     */ 
/*     */     public Collection<QName> getExpectedChildElements()
/*     */     {
/* 186 */       return Collections.singleton(SingleMapNodeProperty.this.entryTag.toQName());
/*     */     }
/* 141 */   };
/*     */ 
/* 195 */   private final Loader entryLoader = new Loader(false)
/*     */   {
/*     */     public void startElement(UnmarshallingContext.State state, TagName ea) {
/* 198 */       state.setTarget(new Object[2]);
/*     */     }
/*     */ 
/*     */     public void leaveElement(UnmarshallingContext.State state, TagName ea)
/*     */     {
/* 203 */       Object[] keyValue = (Object[])state.getTarget();
/* 204 */       Map map = (Map)state.getPrev().getTarget();
/* 205 */       map.put(keyValue[0], keyValue[1]);
/*     */     }
/*     */ 
/*     */     public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */     {
/* 210 */       if (ea.matches(SingleMapNodeProperty.this.keyTag)) {
/* 211 */         state.setLoader(SingleMapNodeProperty.this.keyLoader);
/* 212 */         state.setReceiver(SingleMapNodeProperty.keyReceiver);
/* 213 */         return;
/*     */       }
/* 215 */       if (ea.matches(SingleMapNodeProperty.this.valueTag)) {
/* 216 */         state.setLoader(SingleMapNodeProperty.this.valueLoader);
/* 217 */         state.setReceiver(SingleMapNodeProperty.valueReceiver);
/* 218 */         return;
/*     */       }
/* 220 */       super.childElement(state, ea);
/*     */     }
/*     */ 
/*     */     public Collection<QName> getExpectedChildElements()
/*     */     {
/* 225 */       return Arrays.asList(new QName[] { SingleMapNodeProperty.this.keyTag.toQName(), SingleMapNodeProperty.this.valueTag.toQName() });
/*     */     }
/* 195 */   };
/*     */ 
/* 239 */   private static final Receiver keyReceiver = new ReceiverImpl(0);
/* 240 */   private static final Receiver valueReceiver = new ReceiverImpl(1);
/*     */ 
/*     */   public SingleMapNodeProperty(JAXBContextImpl context, RuntimeMapPropertyInfo prop)
/*     */   {
/*  89 */     super(context, prop);
/*  90 */     this.acc = prop.getAccessor().optimize(context);
/*  91 */     this.tagName = context.nameBuilder.createElementName(prop.getXmlName());
/*  92 */     this.entryTag = context.nameBuilder.createElementName("", "entry");
/*  93 */     this.keyTag = context.nameBuilder.createElementName("", "key");
/*  94 */     this.valueTag = context.nameBuilder.createElementName("", "value");
/*  95 */     this.nillable = prop.isCollectionNillable();
/*  96 */     this.keyBeanInfo = context.getOrCreate(prop.getKeyType());
/*  97 */     this.valueBeanInfo = context.getOrCreate(prop.getValueType());
/*     */ 
/* 101 */     Class sig = (Class)Utils.REFLECTION_NAVIGATOR.erasure(prop.getRawType());
/* 102 */     this.mapImplClass = ClassFactory.inferImplClass(sig, knownImplClasses);
/*     */   }
/*     */ 
/*     */   public void reset(BeanT bean)
/*     */     throws AccessorException
/*     */   {
/* 112 */     this.acc.set(bean, null);
/*     */   }
/*     */ 
/*     */   public String getIdValue(BeanT bean)
/*     */   {
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   public PropertyKind getKind() {
/* 124 */     return PropertyKind.MAP;
/*     */   }
/*     */ 
/*     */   public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
/* 128 */     this.keyLoader = this.keyBeanInfo.getLoader(chain.context, true);
/* 129 */     this.valueLoader = this.valueBeanInfo.getLoader(chain.context, true);
/* 130 */     handlers.put(this.tagName, new ChildLoader(this.itemsLoader, null));
/*     */   }
/*     */ 
/*     */   public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer)
/*     */     throws SAXException, AccessorException, IOException, XMLStreamException
/*     */   {
/* 244 */     Map v = (Map)this.acc.get(o);
/* 245 */     if (v != null) {
/* 246 */       bareStartTag(w, this.tagName, v);
/* 247 */       for (Map.Entry e : v.entrySet()) {
/* 248 */         bareStartTag(w, this.entryTag, null);
/*     */ 
/* 250 */         Object key = e.getKey();
/* 251 */         if (key != null) {
/* 252 */           w.startElement(this.keyTag, key);
/* 253 */           w.childAsXsiType(key, this.fieldName, this.keyBeanInfo, false);
/* 254 */           w.endElement();
/*     */         }
/*     */ 
/* 257 */         Object value = e.getValue();
/* 258 */         if (value != null) {
/* 259 */           w.startElement(this.valueTag, value);
/* 260 */           w.childAsXsiType(value, this.fieldName, this.valueBeanInfo, false);
/* 261 */           w.endElement();
/*     */         }
/*     */ 
/* 264 */         w.endElement();
/*     */       }
/* 266 */       w.endElement();
/*     */     }
/* 268 */     else if (this.nillable) {
/* 269 */       w.startElement(this.tagName, null);
/* 270 */       w.writeXsiNilTrue();
/* 271 */       w.endElement();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void bareStartTag(XMLSerializer w, Name tagName, Object peer) throws IOException, XMLStreamException, SAXException {
/* 276 */     w.startElement(tagName, peer);
/* 277 */     w.endNamespaceDecls(peer);
/* 278 */     w.endAttributes();
/*     */   }
/*     */ 
/*     */   public Accessor getElementPropertyAccessor(String nsUri, String localName)
/*     */   {
/* 283 */     if (this.tagName.equals(nsUri, localName))
/* 284 */       return this.acc;
/* 285 */     return null;
/*     */   }
/*     */ 
/*     */   private static final class ReceiverImpl
/*     */     implements Receiver
/*     */   {
/*     */     private final int index;
/*     */ 
/*     */     public ReceiverImpl(int index)
/*     */     {
/* 232 */       this.index = index;
/*     */     }
/*     */     public void receive(UnmarshallingContext.State state, Object o) {
/* 235 */       ((Object[])state.getTarget())[this.index] = o;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.SingleMapNodeProperty
 * JD-Core Version:    0.6.2
 */