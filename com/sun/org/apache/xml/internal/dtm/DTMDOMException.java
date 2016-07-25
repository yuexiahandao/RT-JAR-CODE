/*    */ package com.sun.org.apache.xml.internal.dtm;
/*    */ 
/*    */ import org.w3c.dom.DOMException;
/*    */ 
/*    */ public class DTMDOMException extends DOMException
/*    */ {
/*    */   static final long serialVersionUID = 1895654266613192414L;
/*    */ 
/*    */   public DTMDOMException(short code, String message)
/*    */   {
/* 43 */     super(code, message);
/*    */   }
/*    */ 
/*    */   public DTMDOMException(short code)
/*    */   {
/* 54 */     super(code, "");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.DTMDOMException
 * JD-Core Version:    0.6.2
 */