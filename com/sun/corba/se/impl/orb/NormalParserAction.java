/*    */ package com.sun.corba.se.impl.orb;
/*    */ 
/*    */ import com.sun.corba.se.spi.orb.Operation;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class NormalParserAction extends ParserActionBase
/*    */ {
/*    */   public NormalParserAction(String paramString1, Operation paramOperation, String paramString2)
/*    */   {
/* 36 */     super(paramString1, false, paramOperation, paramString2);
/*    */   }
/*    */ 
/*    */   public Object apply(Properties paramProperties)
/*    */   {
/* 45 */     String str = paramProperties.getProperty(getPropertyName());
/* 46 */     if (str != null) {
/* 47 */       return getOperation().operate(str);
/*    */     }
/* 49 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.NormalParserAction
 * JD-Core Version:    0.6.2
 */