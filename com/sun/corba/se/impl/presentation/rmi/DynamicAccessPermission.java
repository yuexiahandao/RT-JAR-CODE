/*    */ package com.sun.corba.se.impl.presentation.rmi;
/*    */ 
/*    */ import java.security.BasicPermission;
/*    */ 
/*    */ public final class DynamicAccessPermission extends BasicPermission
/*    */ {
/*    */   public DynamicAccessPermission(String paramString)
/*    */   {
/* 47 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public DynamicAccessPermission(String paramString1, String paramString2)
/*    */   {
/* 60 */     super(paramString1, paramString2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.DynamicAccessPermission
 * JD-Core Version:    0.6.2
 */