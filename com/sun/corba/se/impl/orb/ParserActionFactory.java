/*    */ package com.sun.corba.se.impl.orb;
/*    */ 
/*    */ import com.sun.corba.se.spi.orb.Operation;
/*    */ 
/*    */ public class ParserActionFactory
/*    */ {
/*    */   public static ParserAction makeNormalAction(String paramString1, Operation paramOperation, String paramString2)
/*    */   {
/* 36 */     return new NormalParserAction(paramString1, paramOperation, paramString2);
/*    */   }
/*    */ 
/*    */   public static ParserAction makePrefixAction(String paramString1, Operation paramOperation, String paramString2, Class paramClass)
/*    */   {
/* 42 */     return new PrefixParserAction(paramString1, paramOperation, paramString2, paramClass);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.ParserActionFactory
 * JD-Core Version:    0.6.2
 */