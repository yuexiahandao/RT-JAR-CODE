/*    */ package com.sun.corba.se.spi.orb;
/*    */ 
/*    */ import com.sun.corba.se.impl.orb.NormalParserData;
/*    */ import com.sun.corba.se.impl.orb.PrefixParserData;
/*    */ 
/*    */ public class ParserDataFactory
/*    */ {
/*    */   public static ParserData make(String paramString1, Operation paramOperation, String paramString2, Object paramObject1, Object paramObject2, String paramString3)
/*    */   {
/* 35 */     return new NormalParserData(paramString1, paramOperation, paramString2, paramObject1, paramObject2, paramString3);
/*    */   }
/*    */ 
/*    */   public static ParserData make(String paramString1, Operation paramOperation, String paramString2, Object paramObject1, Object paramObject2, StringPair[] paramArrayOfStringPair, Class paramClass)
/*    */   {
/* 43 */     return new PrefixParserData(paramString1, paramOperation, paramString2, paramObject1, paramObject2, paramArrayOfStringPair, paramClass);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orb.ParserDataFactory
 * JD-Core Version:    0.6.2
 */