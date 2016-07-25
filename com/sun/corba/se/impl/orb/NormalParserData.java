/*    */ package com.sun.corba.se.impl.orb;
/*    */ 
/*    */ import com.sun.corba.se.spi.orb.Operation;
/*    */ import com.sun.corba.se.spi.orb.PropertyParser;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class NormalParserData extends ParserDataBase
/*    */ {
/*    */   private String testData;
/*    */ 
/*    */   public NormalParserData(String paramString1, Operation paramOperation, String paramString2, Object paramObject1, Object paramObject2, String paramString3)
/*    */   {
/* 40 */     super(paramString1, paramOperation, paramString2, paramObject1, paramObject2);
/* 41 */     this.testData = paramString3;
/*    */   }
/*    */ 
/*    */   public void addToParser(PropertyParser paramPropertyParser) {
/* 45 */     paramPropertyParser.add(getPropertyName(), getOperation(), getFieldName());
/*    */   }
/*    */ 
/*    */   public void addToProperties(Properties paramProperties)
/*    */   {
/* 50 */     paramProperties.setProperty(getPropertyName(), this.testData);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.NormalParserData
 * JD-Core Version:    0.6.2
 */