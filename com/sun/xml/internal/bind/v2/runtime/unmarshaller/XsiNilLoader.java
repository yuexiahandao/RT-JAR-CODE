/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.internal.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import java.util.Collection;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class XsiNilLoader extends ProxyLoader
/*     */ {
/*     */   private final Loader defaultLoader;
/*     */ 
/*     */   public XsiNilLoader(Loader defaultLoader)
/*     */   {
/*  51 */     this.defaultLoader = defaultLoader;
/*  52 */     assert (defaultLoader != null);
/*     */   }
/*     */ 
/*     */   protected Loader selectLoader(UnmarshallingContext.State state, TagName ea) throws SAXException {
/*  56 */     int idx = ea.atts.getIndex("http://www.w3.org/2001/XMLSchema-instance", "nil");
/*     */ 
/*  58 */     if (idx != -1) {
/*  59 */       Boolean b = DatatypeConverterImpl._parseBoolean(ea.atts.getValue(idx));
/*     */ 
/*  61 */       if ((b != null) && (b.booleanValue())) {
/*  62 */         onNil(state);
/*  63 */         boolean hasOtherAttributes = ea.atts.getLength() - 1 > 0;
/*     */ 
/*  65 */         if ((!hasOtherAttributes) || (!(state.getPrev().getTarget() instanceof JAXBElement))) {
/*  66 */           return Discarder.INSTANCE;
/*     */         }
/*     */       }
/*     */     }
/*  70 */     return this.defaultLoader;
/*     */   }
/*     */ 
/*     */   public Collection<QName> getExpectedChildElements()
/*     */   {
/*  75 */     return this.defaultLoader.getExpectedChildElements();
/*     */   }
/*     */ 
/*     */   public Collection<QName> getExpectedAttributes()
/*     */   {
/*  80 */     return this.defaultLoader.getExpectedAttributes();
/*     */   }
/*     */ 
/*     */   protected void onNil(UnmarshallingContext.State state)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public static final class Array extends XsiNilLoader
/*     */   {
/*     */     public Array(Loader core)
/*     */     {
/* 110 */       super();
/*     */     }
/*     */ 
/*     */     protected void onNil(UnmarshallingContext.State state)
/*     */     {
/* 116 */       state.setTarget(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class Single extends XsiNilLoader
/*     */   {
/*     */     private final Accessor acc;
/*     */ 
/*     */     public Single(Loader l, Accessor acc)
/*     */     {
/*  92 */       super();
/*  93 */       this.acc = acc;
/*     */     }
/*     */ 
/*     */     protected void onNil(UnmarshallingContext.State state) throws SAXException
/*     */     {
/*     */       try {
/*  99 */         this.acc.set(state.getPrev().getTarget(), null);
/* 100 */         state.getPrev().setNil(true);
/*     */       } catch (AccessorException e) {
/* 102 */         handleGenericException(e, true);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader
 * JD-Core Version:    0.6.2
 */