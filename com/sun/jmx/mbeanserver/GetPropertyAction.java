/*    */ package com.sun.jmx.mbeanserver;
/*    */ 
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ public class GetPropertyAction
/*    */   implements PrivilegedAction<String>
/*    */ {
/*    */   private final String key;
/*    */ 
/*    */   public GetPropertyAction(String paramString)
/*    */   {
/* 40 */     this.key = paramString;
/*    */   }
/*    */ 
/*    */   public String run() {
/* 44 */     return System.getProperty(this.key);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.GetPropertyAction
 * JD-Core Version:    0.6.2
 */