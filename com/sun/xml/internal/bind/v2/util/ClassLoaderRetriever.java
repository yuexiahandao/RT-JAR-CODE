/*    */ package com.sun.xml.internal.bind.v2.util;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*    */ 
/*    */ public class ClassLoaderRetriever
/*    */ {
/*    */   public static ClassLoader getClassLoader()
/*    */   {
/* 37 */     ClassLoader cl = UnmarshallerImpl.class.getClassLoader();
/* 38 */     if (cl == null) {
/* 39 */       cl = Thread.currentThread().getContextClassLoader();
/*    */     }
/* 41 */     return cl;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.util.ClassLoaderRetriever
 * JD-Core Version:    0.6.2
 */