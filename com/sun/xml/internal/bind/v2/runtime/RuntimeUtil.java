/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.internal.bind.util.ValidationEventLocatorExImpl;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.helpers.PrintConversionEventImpl;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
/*     */ import javax.xml.bind.helpers.ValidationEventLocatorImpl;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class RuntimeUtil
/*     */ {
/*  92 */   public static final Map<Class, Class> boxToPrimitive = Collections.unmodifiableMap(p);
/*     */   public static final Map<Class, Class> primitiveToBox;
/*     */ 
/*     */   public static void handlePrintConversionException(Object caller, Exception e, XMLSerializer serializer)
/*     */     throws SAXException
/*     */   {
/* 101 */     if ((e instanceof SAXException))
/*     */     {
/* 105 */       throw ((SAXException)e);
/*     */     }
/* 107 */     ValidationEvent ve = new PrintConversionEventImpl(1, e.getMessage(), new ValidationEventLocatorImpl(caller), e);
/*     */ 
/* 110 */     serializer.reportError(ve);
/*     */   }
/*     */ 
/*     */   public static void handleTypeMismatchError(XMLSerializer serializer, Object parentObject, String fieldName, Object childObject)
/*     */     throws SAXException
/*     */   {
/* 119 */     ValidationEvent ve = new ValidationEventImpl(1, Messages.TYPE_MISMATCH.format(new Object[] { getTypeName(parentObject), fieldName, getTypeName(childObject) }), new ValidationEventLocatorExImpl(parentObject, fieldName));
/*     */ 
/* 127 */     serializer.reportError(ve);
/*     */   }
/*     */ 
/*     */   private static String getTypeName(Object o) {
/* 131 */     return o.getClass().getName();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  75 */     Map b = new HashMap();
/*  76 */     b.put(Byte.TYPE, Byte.class);
/*  77 */     b.put(Short.TYPE, Short.class);
/*  78 */     b.put(Integer.TYPE, Integer.class);
/*  79 */     b.put(Long.TYPE, Long.class);
/*  80 */     b.put(Character.TYPE, Character.class);
/*  81 */     b.put(Boolean.TYPE, Boolean.class);
/*  82 */     b.put(Float.TYPE, Float.class);
/*  83 */     b.put(Double.TYPE, Double.class);
/*  84 */     b.put(Void.TYPE, Void.class);
/*     */ 
/*  86 */     primitiveToBox = Collections.unmodifiableMap(b);
/*     */ 
/*  88 */     Map p = new HashMap();
/*  89 */     for (Map.Entry e : b.entrySet())
/*  90 */       p.put(e.getValue(), e.getKey());
/*     */   }
/*     */ 
/*     */   public static final class ToStringAdapter extends XmlAdapter<String, Object>
/*     */   {
/*     */     public Object unmarshal(String s)
/*     */     {
/*  52 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String marshal(Object o) {
/*  56 */       if (o == null) return null;
/*  57 */       return o.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.RuntimeUtil
 * JD-Core Version:    0.6.2
 */