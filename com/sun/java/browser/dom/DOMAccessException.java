/*    */ package com.sun.java.browser.dom;
/*    */ 
/*    */ public class DOMAccessException extends Exception
/*    */ {
/*    */   private Throwable ex;
/*    */   private String msg;
/*    */ 
/*    */   public DOMAccessException()
/*    */   {
/* 35 */     this(null, null);
/*    */   }
/*    */ 
/*    */   public DOMAccessException(String paramString)
/*    */   {
/* 46 */     this(null, paramString);
/*    */   }
/*    */ 
/*    */   public DOMAccessException(Exception paramException)
/*    */   {
/* 56 */     this(paramException, null);
/*    */   }
/*    */ 
/*    */   public DOMAccessException(Exception paramException, String paramString)
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
 * Qualified Name:     com.sun.java.browser.dom.DOMAccessException
 * JD-Core Version:    0.6.2
 */