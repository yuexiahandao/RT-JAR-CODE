/*    */ package com.sun.org.apache.xerces.internal.impl.dv;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public abstract class DTDDVFactory
/*    */ {
/*    */   private static final String DEFAULT_FACTORY_CLASS = "com.sun.org.apache.xerces.internal.impl.dv.dtd.DTDDVFactoryImpl";
/*    */ 
/*    */   public static final DTDDVFactory getInstance()
/*    */     throws DVFactoryException
/*    */   {
/* 49 */     return getInstance("com.sun.org.apache.xerces.internal.impl.dv.dtd.DTDDVFactoryImpl");
/*    */   }
/*    */ 
/*    */   public static final DTDDVFactory getInstance(String factoryClass)
/*    */     throws DVFactoryException
/*    */   {
/*    */     try
/*    */     {
/* 63 */       return (DTDDVFactory)ObjectFactory.newInstance(factoryClass, true);
/*    */     }
/*    */     catch (ClassCastException e) {
/*    */     }
/* 67 */     throw new DVFactoryException("DTD factory class " + factoryClass + " does not extend from DTDDVFactory.");
/*    */   }
/*    */ 
/*    */   public abstract DatatypeValidator getBuiltInDV(String paramString);
/*    */ 
/*    */   public abstract Hashtable getBuiltInTypes();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory
 * JD-Core Version:    0.6.2
 */