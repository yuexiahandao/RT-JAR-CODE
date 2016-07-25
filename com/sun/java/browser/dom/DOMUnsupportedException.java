/*    */ package com.sun.java.browser.dom;
/*    */ 
/*    */ public class DOMUnsupportedException extends Exception
/*    */ {
/*    */   private Throwable ex;
/*    */   private String msg;
/*    */ 
/*    */   public DOMUnsupportedException()
/*    */   {
/* 36 */     this(null, null);
/*    */   }
/*    */ 
/*    */   public DOMUnsupportedException(String paramString)
/*    */   {
/* 46 */     this(null, paramString);
/*    */   }
/*    */ 
/*    */   public DOMUnsupportedException(Exception paramException)
/*    */   {
/* 56 */     this(paramException, null);
/*    */   }
/*    */ 
/*    */   public DOMUnsupportedException(Exception paramException, String paramString)
/*    */   {
/* 67 */     this.ex = paramException;
/* 68 */     this.msg = paramString;
/*    */   }
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 76 */     return this.msg;
/*    */   }
/*    */ 
/*    */   public Throwable getCause()
/*    */   {
/* 84 */     return this.ex;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.browser.dom.DOMUnsupportedException
 * JD-Core Version:    0.6.2
 */