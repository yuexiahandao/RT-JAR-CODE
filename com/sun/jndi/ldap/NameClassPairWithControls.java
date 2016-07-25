/*    */ package com.sun.jndi.ldap;
/*    */ 
/*    */ import javax.naming.NameClassPair;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.ldap.Control;
/*    */ import javax.naming.ldap.HasControls;
/*    */ 
/*    */ class NameClassPairWithControls extends NameClassPair
/*    */   implements HasControls
/*    */ {
/*    */   private Control[] controls;
/*    */   private static final long serialVersionUID = 2010738921219112944L;
/*    */ 
/*    */   public NameClassPairWithControls(String paramString1, String paramString2, Control[] paramArrayOfControl)
/*    */   {
/* 36 */     super(paramString1, paramString2);
/* 37 */     this.controls = paramArrayOfControl;
/*    */   }
/*    */ 
/*    */   public Control[] getControls() throws NamingException {
/* 41 */     return this.controls;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.NameClassPairWithControls
 * JD-Core Version:    0.6.2
 */