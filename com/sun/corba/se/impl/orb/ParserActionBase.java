/*    */ package com.sun.corba.se.impl.orb;
/*    */ 
/*    */ import com.sun.corba.se.spi.orb.Operation;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public abstract class ParserActionBase
/*    */   implements ParserAction
/*    */ {
/*    */   private String propertyName;
/*    */   private boolean prefix;
/*    */   private Operation operation;
/*    */   private String fieldName;
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 40 */     return this.propertyName.hashCode() ^ this.operation.hashCode() ^ this.fieldName.hashCode() ^ (this.prefix ? 0 : 1);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 46 */     if (paramObject == this) {
/* 47 */       return true;
/*    */     }
/* 49 */     if (!(paramObject instanceof ParserActionBase)) {
/* 50 */       return false;
/*    */     }
/* 52 */     ParserActionBase localParserActionBase = (ParserActionBase)paramObject;
/*    */ 
/* 54 */     return (this.propertyName.equals(localParserActionBase.propertyName)) && (this.prefix == localParserActionBase.prefix) && (this.operation.equals(localParserActionBase.operation)) && (this.fieldName.equals(localParserActionBase.fieldName));
/*    */   }
/*    */ 
/*    */   public ParserActionBase(String paramString1, boolean paramBoolean, Operation paramOperation, String paramString2)
/*    */   {
/* 63 */     this.propertyName = paramString1;
/* 64 */     this.prefix = paramBoolean;
/* 65 */     this.operation = paramOperation;
/* 66 */     this.fieldName = paramString2;
/*    */   }
/*    */ 
/*    */   public String getPropertyName()
/*    */   {
/* 71 */     return this.propertyName;
/*    */   }
/*    */ 
/*    */   public boolean isPrefix()
/*    */   {
/* 76 */     return this.prefix;
/*    */   }
/*    */ 
/*    */   public String getFieldName()
/*    */   {
/* 81 */     return this.fieldName;
/*    */   }
/*    */ 
/*    */   public abstract Object apply(Properties paramProperties);
/*    */ 
/*    */   protected Operation getOperation()
/*    */   {
/* 88 */     return this.operation;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.ParserActionBase
 * JD-Core Version:    0.6.2
 */