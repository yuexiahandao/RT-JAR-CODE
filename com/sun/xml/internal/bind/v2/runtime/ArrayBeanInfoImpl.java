/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeArrayInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext.State;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class ArrayBeanInfoImpl extends JaxBeanInfo
/*     */ {
/*     */   private final Class itemType;
/*     */   private final JaxBeanInfo itemBeanInfo;
/*     */   private Loader loader;
/*     */ 
/*     */   public ArrayBeanInfoImpl(JAXBContextImpl owner, RuntimeArrayInfo rai)
/*     */   {
/*  61 */     super(owner, rai, rai.getType(), rai.getTypeName(), false, true, false);
/*  62 */     this.itemType = this.jaxbType.getComponentType();
/*  63 */     this.itemBeanInfo = owner.getOrCreate(rai.getItemType());
/*     */   }
/*     */ 
/*     */   protected void link(JAXBContextImpl grammar)
/*     */   {
/*  68 */     getLoader(grammar, false);
/*  69 */     super.link(grammar);
/*     */   }
/*     */ 
/*     */   protected Object toArray(List list)
/*     */   {
/* 111 */     int len = list.size();
/* 112 */     Object array = Array.newInstance(this.itemType, len);
/* 113 */     for (int i = 0; i < len; i++)
/* 114 */       Array.set(array, i, list.get(i));
/* 115 */     return array;
/*     */   }
/*     */ 
/*     */   public void serializeBody(Object array, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 119 */     int len = Array.getLength(array);
/* 120 */     for (int i = 0; i < len; i++) {
/* 121 */       Object item = Array.get(array, i);
/*     */ 
/* 123 */       target.startElement("", "item", null, null);
/* 124 */       if (item == null)
/* 125 */         target.writeXsiNilTrue();
/*     */       else {
/* 127 */         target.childAsXsiType(item, "arrayItem", this.itemBeanInfo, false);
/*     */       }
/* 129 */       target.endElement();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final String getElementNamespaceURI(Object array) {
/* 134 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public final String getElementLocalName(Object array) {
/* 138 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public final Object createInstance(UnmarshallingContext context)
/*     */   {
/* 143 */     return new ArrayList();
/*     */   }
/*     */ 
/*     */   public final boolean reset(Object array, UnmarshallingContext context) {
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */   public final String getId(Object array, XMLSerializer target) {
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */   public final void serializeAttributes(Object array, XMLSerializer target)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void serializeRoot(Object array, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 159 */     target.reportError(new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(new Object[] { array.getClass().getName() }), null, null));
/*     */   }
/*     */ 
/*     */   public final void serializeURIs(Object array, XMLSerializer target)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final Transducer getTransducer()
/*     */   {
/* 172 */     return null;
/*     */   }
/*     */ 
/*     */   public final Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
/* 176 */     if (this.loader == null) {
/* 177 */       this.loader = new ArrayLoader(context);
/*     */     }
/*     */ 
/* 180 */     return this.loader;
/*     */   }
/*     */ 
/*     */   private final class ArrayLoader extends Loader
/*     */     implements Receiver
/*     */   {
/*     */     private final Loader itemLoader;
/*     */ 
/*     */     public ArrayLoader(JAXBContextImpl owner)
/*     */     {
/*  74 */       super();
/*  75 */       this.itemLoader = ArrayBeanInfoImpl.this.itemBeanInfo.getLoader(owner, true);
/*     */     }
/*     */ 
/*     */     public void startElement(UnmarshallingContext.State state, TagName ea)
/*     */     {
/*  82 */       state.setTarget(new ArrayList());
/*     */     }
/*     */ 
/*     */     public void leaveElement(UnmarshallingContext.State state, TagName ea)
/*     */     {
/*  87 */       state.setTarget(ArrayBeanInfoImpl.this.toArray((List)state.getTarget()));
/*     */     }
/*     */ 
/*     */     public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */     {
/*  92 */       if (ea.matches("", "item")) {
/*  93 */         state.setLoader(this.itemLoader);
/*  94 */         state.setReceiver(this);
/*     */       } else {
/*  96 */         super.childElement(state, ea);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Collection<QName> getExpectedChildElements()
/*     */     {
/* 102 */       return Collections.singleton(new QName("", "item"));
/*     */     }
/*     */ 
/*     */     public void receive(UnmarshallingContext.State state, Object o) {
/* 106 */       ((List)state.getTarget()).add(o);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.ArrayBeanInfoImpl
 * JD-Core Version:    0.6.2
 */