/*    */ package com.sun.xml.internal.ws.util;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ public class FastInfosetReflection
/*    */ {
/* 70 */   public static final Constructor fiStAXDocumentParser_new = tmp_new;
/* 71 */   public static final Method fiStAXDocumentParser_setInputStream = tmp_setInputStream;
/* 72 */   public static final Method fiStAXDocumentParser_setStringInterning = tmp_setStringInterning;
/*    */ 
/*    */   static
/*    */   {
/* 54 */     Constructor tmp_new = null;
/* 55 */     Method tmp_setInputStream = null;
/* 56 */     Method tmp_setStringInterning = null;
/*    */     try
/*    */     {
/* 60 */       Class clazz = Class.forName("com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser");
/* 61 */       tmp_new = clazz.getConstructor(new Class[0]);
/* 62 */       tmp_setInputStream = clazz.getMethod("setInputStream", new Class[] { InputStream.class });
/*    */ 
/* 64 */       tmp_setStringInterning = clazz.getMethod("setStringInterning", new Class[] { Boolean.TYPE });
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.FastInfosetReflection
 * JD-Core Version:    0.6.2
 */