/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.istack.internal.FinalArrayList;
/*     */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext.State;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class ValueListBeanInfoImpl extends JaxBeanInfo
/*     */ {
/*     */   private final Class itemType;
/*     */   private final Transducer xducer;
/*  62 */   private final Loader loader = new Loader(true)
/*     */   {
/*     */     public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
/*  65 */       List r = new FinalArrayList();
/*     */ 
/*  67 */       int idx = 0;
/*  68 */       int len = text.length();
/*     */       while (true)
/*     */       {
/*  71 */         int p = idx;
/*  72 */         while ((p < len) && (!WhiteSpaceProcessor.isWhiteSpace(text.charAt(p)))) {
/*  73 */           p++;
/*     */         }
/*  75 */         CharSequence token = text.subSequence(idx, p);
/*  76 */         if (!token.equals("")) {
/*     */           try {
/*  78 */             r.add(ValueListBeanInfoImpl.this.xducer.parse(token));
/*     */           } catch (AccessorException e) {
/*  80 */             handleGenericException(e, true);
/*     */           }
/*     */         }
/*     */         else {
/*  84 */           if (p == len)
/*     */             break;
/*  86 */           while ((p < len) && (WhiteSpaceProcessor.isWhiteSpace(text.charAt(p))))
/*  87 */             p++;
/*  88 */           if (p == len)
/*     */             break;
/*  90 */           idx = p;
/*     */         }
/*     */       }
/*  93 */       state.setTarget(ValueListBeanInfoImpl.this.toArray(r));
/*     */     }
/*  62 */   };
/*     */ 
/*     */   public ValueListBeanInfoImpl(JAXBContextImpl owner, Class arrayType)
/*     */     throws JAXBException
/*     */   {
/*  56 */     super(owner, null, arrayType, false, true, false);
/*  57 */     this.itemType = this.jaxbType.getComponentType();
/*  58 */     this.xducer = owner.getBeanInfo(arrayType.getComponentType(), true).getTransducer();
/*  59 */     assert (this.xducer != null);
/*     */   }
/*     */ 
/*     */   private Object toArray(List list)
/*     */   {
/*  98 */     int len = list.size();
/*  99 */     Object array = Array.newInstance(this.itemType, len);
/* 100 */     for (int i = 0; i < len; i++)
/* 101 */       Array.set(array, i, list.get(i));
/* 102 */     return array;
/*     */   }
/*     */ 
/*     */   public void serializeBody(Object array, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 106 */     int len = Array.getLength(array);
/* 107 */     for (int i = 0; i < len; i++) {
/* 108 */       Object item = Array.get(array, i);
/*     */       try {
/* 110 */         this.xducer.writeText(target, item, "arrayItem");
/*     */       } catch (AccessorException e) {
/* 112 */         target.reportError("arrayItem", e);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void serializeURIs(Object array, XMLSerializer target) throws SAXException {
/* 118 */     if (this.xducer.useNamespace()) {
/* 119 */       int len = Array.getLength(array);
/* 120 */       for (int i = 0; i < len; i++) {
/* 121 */         Object item = Array.get(array, i);
/*     */         try {
/* 123 */           this.xducer.declareNamespace(item, target);
/*     */         } catch (AccessorException e) {
/* 125 */           target.reportError("arrayItem", e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final String getElementNamespaceURI(Object array) {
/* 132 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public final String getElementLocalName(Object array) {
/* 136 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public final Object createInstance(UnmarshallingContext context) {
/* 140 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public final boolean reset(Object array, UnmarshallingContext context) {
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   public final String getId(Object array, XMLSerializer target) {
/* 148 */     return null;
/*     */   }
/*     */ 
/*     */   public final void serializeAttributes(Object array, XMLSerializer target)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void serializeRoot(Object array, XMLSerializer target) throws SAXException {
/* 156 */     target.reportError(new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(new Object[] { array.getClass().getName() }), null, null));
/*     */   }
/*     */ 
/*     */   public final Transducer getTransducer()
/*     */   {
/* 165 */     return null;
/*     */   }
/*     */ 
/*     */   public final Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable)
/*     */   {
/* 170 */     return this.loader;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.ValueListBeanInfoImpl
 * JD-Core Version:    0.6.2
 */