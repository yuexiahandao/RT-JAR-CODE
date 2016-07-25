/*    */ package com.sun.corba.se.impl.orb;
/*    */ 
/*    */ import com.sun.corba.se.spi.orb.Operation;
/*    */ import com.sun.corba.se.spi.orb.PropertyParser;
/*    */ import com.sun.corba.se.spi.orb.StringPair;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class PrefixParserData extends ParserDataBase
/*    */ {
/*    */   private StringPair[] testData;
/*    */   private Class componentType;
/*    */ 
/*    */   public PrefixParserData(String paramString1, Operation paramOperation, String paramString2, Object paramObject1, Object paramObject2, StringPair[] paramArrayOfStringPair, Class paramClass)
/*    */   {
/* 43 */     super(paramString1, paramOperation, paramString2, paramObject1, paramObject2);
/* 44 */     this.testData = paramArrayOfStringPair;
/* 45 */     this.componentType = paramClass;
/*    */   }
/*    */ 
/*    */   public void addToParser(PropertyParser paramPropertyParser)
/*    */   {
/* 50 */     paramPropertyParser.addPrefix(getPropertyName(), getOperation(), getFieldName(), this.componentType);
/*    */   }
/*    */ 
/*    */   public void addToProperties(Properties paramProperties)
/*    */   {
/* 56 */     for (int i = 0; i < this.testData.length; i++) {
/* 57 */       StringPair localStringPair = this.testData[i];
/*    */ 
/* 59 */       String str = getPropertyName();
/* 60 */       if (str.charAt(str.length() - 1) != '.') {
/* 61 */         str = str + ".";
/*    */       }
/* 63 */       paramProperties.setProperty(str + localStringPair.getFirst(), localStringPair.getSecond());
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.PrefixParserData
 * JD-Core Version:    0.6.2
 */