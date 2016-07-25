/*    */ package com.sun.org.apache.xml.internal.dtm;
/*    */ 
/*    */ import javax.xml.transform.SourceLocator;
/*    */ 
/*    */ public class DTMConfigurationException extends DTMException
/*    */ {
/*    */   static final long serialVersionUID = -4607874078818418046L;
/*    */ 
/*    */   public DTMConfigurationException()
/*    */   {
/* 38 */     super("Configuration Error");
/*    */   }
/*    */ 
/*    */   public DTMConfigurationException(String msg)
/*    */   {
/* 48 */     super(msg);
/*    */   }
/*    */ 
/*    */   public DTMConfigurationException(Throwable e)
/*    */   {
/* 59 */     super(e);
/*    */   }
/*    */ 
/*    */   public DTMConfigurationException(String msg, Throwable e)
/*    */   {
/* 70 */     super(msg, e);
/*    */   }
/*    */ 
/*    */   public DTMConfigurationException(String message, SourceLocator locator)
/*    */   {
/* 85 */     super(message, locator);
/*    */   }
/*    */ 
/*    */   public DTMConfigurationException(String message, SourceLocator locator, Throwable e)
/*    */   {
/* 99 */     super(message, locator, e);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.DTMConfigurationException
 * JD-Core Version:    0.6.2
 */