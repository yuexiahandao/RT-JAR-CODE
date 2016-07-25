/*    */ package com.sun.beans.decoder;
/*    */ 
/*    */ final class VarElementHandler extends ElementHandler
/*    */ {
/*    */   private ValueObject value;
/*    */ 
/*    */   public void addAttribute(String paramString1, String paramString2)
/*    */   {
/* 63 */     if (paramString1.equals("idref"))
/* 64 */       this.value = ValueObjectImpl.create(getVariable(paramString2));
/*    */     else
/* 66 */       super.addAttribute(paramString1, paramString2);
/*    */   }
/*    */ 
/*    */   protected ValueObject getValueObject()
/*    */   {
/* 77 */     if (this.value == null) {
/* 78 */       throw new IllegalArgumentException("Variable name is not set");
/*    */     }
/* 80 */     return this.value;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.VarElementHandler
 * JD-Core Version:    0.6.2
 */