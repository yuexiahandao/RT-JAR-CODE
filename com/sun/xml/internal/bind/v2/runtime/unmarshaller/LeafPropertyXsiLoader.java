/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.internal.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.ClassBeanInfoImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor.CompositeTransducedAccessorImpl;
/*     */ import java.util.Collection;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class LeafPropertyXsiLoader extends Loader
/*     */ {
/*     */   private final Loader defaultLoader;
/*     */   private final TransducedAccessor xacc;
/*     */   private final Accessor acc;
/*     */ 
/*     */   public LeafPropertyXsiLoader(Loader defaultLoader, TransducedAccessor xacc, Accessor acc)
/*     */   {
/*  49 */     this.defaultLoader = defaultLoader;
/*  50 */     this.expectText = true;
/*  51 */     this.xacc = xacc;
/*  52 */     this.acc = acc;
/*     */   }
/*     */ 
/*     */   public void startElement(UnmarshallingContext.State state, TagName ea)
/*     */     throws SAXException
/*     */   {
/*  58 */     Loader loader = selectLoader(state, ea);
/*  59 */     state.setLoader(loader);
/*  60 */     loader.startElement(state, ea);
/*     */   }
/*     */ 
/*     */   protected Loader selectLoader(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */   {
/*  65 */     UnmarshallingContext context = state.getContext();
/*  66 */     JaxBeanInfo beanInfo = null;
/*     */ 
/*  69 */     Attributes atts = ea.atts;
/*  70 */     int idx = atts.getIndex("http://www.w3.org/2001/XMLSchema-instance", "type");
/*     */ 
/*  72 */     if (idx >= 0) {
/*  73 */       String value = atts.getValue(idx);
/*     */ 
/*  75 */       QName type = DatatypeConverterImpl._parseQName(value, context);
/*     */ 
/*  77 */       if (type == null) {
/*  78 */         return this.defaultLoader;
/*     */       }
/*  80 */       beanInfo = context.getJAXBContext().getGlobalType(type);
/*  81 */       if (beanInfo == null)
/*  82 */         return this.defaultLoader;
/*     */       ClassBeanInfoImpl cbii;
/*     */       try {
/*  85 */         cbii = (ClassBeanInfoImpl)beanInfo;
/*     */       } catch (ClassCastException cce) {
/*  87 */         return this.defaultLoader;
/*     */       }
/*     */ 
/*  90 */       if (null == cbii.getTransducer()) {
/*  91 */         return this.defaultLoader;
/*     */       }
/*     */ 
/*  94 */       return new LeafPropertyLoader(new TransducedAccessor.CompositeTransducedAccessorImpl(state.getContext().getJAXBContext(), cbii.getTransducer(), this.acc));
/*     */     }
/*     */ 
/* 101 */     return this.defaultLoader;
/*     */   }
/*     */ 
/*     */   public Collection<QName> getExpectedChildElements()
/*     */   {
/* 106 */     return this.defaultLoader.getExpectedChildElements();
/*     */   }
/*     */ 
/*     */   public Collection<QName> getExpectedAttributes()
/*     */   {
/* 111 */     return this.defaultLoader.getExpectedAttributes();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.LeafPropertyXsiLoader
 * JD-Core Version:    0.6.2
 */