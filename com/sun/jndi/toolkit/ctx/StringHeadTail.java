/*    */ package com.sun.jndi.toolkit.ctx;
/*    */ 
/*    */ public class StringHeadTail
/*    */ {
/*    */   private int status;
/*    */   private String head;
/*    */   private String tail;
/*    */ 
/*    */   public StringHeadTail(String paramString1, String paramString2)
/*    */   {
/* 39 */     this(paramString1, paramString2, 0);
/*    */   }
/*    */ 
/*    */   public StringHeadTail(String paramString1, String paramString2, int paramInt) {
/* 43 */     this.status = paramInt;
/* 44 */     this.head = paramString1;
/* 45 */     this.tail = paramString2;
/*    */   }
/*    */ 
/*    */   public void setStatus(int paramInt) {
/* 49 */     this.status = paramInt;
/*    */   }
/*    */ 
/*    */   public String getHead() {
/* 53 */     return this.head;
/*    */   }
/*    */ 
/*    */   public String getTail() {
/* 57 */     return this.tail;
/*    */   }
/*    */ 
/*    */   public int getStatus() {
/* 61 */     return this.status;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.ctx.StringHeadTail
 * JD-Core Version:    0.6.2
 */