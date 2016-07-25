/*     */ package org.xml.sax.helpers;
/*     */ 
/*     */ import org.xml.sax.Parser;
/*     */ 
/*     */ /** @deprecated */
/*     */ public class ParserFactory
/*     */ {
/*  69 */   private static SecuritySupport ss = new SecuritySupport();
/*     */ 
/*     */   public static Parser makeParser()
/*     */     throws ClassNotFoundException, IllegalAccessException, InstantiationException, NullPointerException, ClassCastException
/*     */   {
/* 107 */     String className = ss.getSystemProperty("org.xml.sax.parser");
/* 108 */     if (className == null) {
/* 109 */       throw new NullPointerException("No value for sax.parser property");
/*     */     }
/* 111 */     return makeParser(className);
/*     */   }
/*     */ 
/*     */   public static Parser makeParser(String className)
/*     */     throws ClassNotFoundException, IllegalAccessException, InstantiationException, ClassCastException
/*     */   {
/* 143 */     return (Parser)NewInstance.newInstance(ss.getContextClassLoader(), className);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.helpers.ParserFactory
 * JD-Core Version:    0.6.2
 */