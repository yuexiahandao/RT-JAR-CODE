/*    */ package com.sun.corba.se.impl.naming.namingutil;
/*    */ 
/*    */ public class INSURLHandler
/*    */ {
/* 38 */   private static INSURLHandler insURLHandler = null;
/*    */   private static final int CORBALOC_PREFIX_LENGTH = 9;
/*    */   private static final int CORBANAME_PREFIX_LENGTH = 10;
/*    */ 
/*    */   public static synchronized INSURLHandler getINSURLHandler()
/*    */   {
/* 50 */     if (insURLHandler == null) {
/* 51 */       insURLHandler = new INSURLHandler();
/*    */     }
/* 53 */     return insURLHandler;
/*    */   }
/*    */ 
/*    */   public INSURL parseURL(String paramString) {
/* 57 */     String str = paramString;
/* 58 */     if (str.startsWith("corbaloc:") == true)
/* 59 */       return new CorbalocURL(str.substring(9));
/* 60 */     if (str.startsWith("corbaname:") == true) {
/* 61 */       return new CorbanameURL(str.substring(10));
/*    */     }
/* 63 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.namingutil.INSURLHandler
 * JD-Core Version:    0.6.2
 */