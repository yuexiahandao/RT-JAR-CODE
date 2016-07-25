/*    */ package com.sun.jndi.ldap;
/*    */ 
/*    */ public final class ManageReferralControl extends BasicControl
/*    */ {
/*    */   public static final String OID = "2.16.840.1.113730.3.4.2";
/*    */   private static final long serialVersionUID = 909382692585717224L;
/*    */ 
/*    */   public ManageReferralControl()
/*    */   {
/* 55 */     super("2.16.840.1.113730.3.4.2", true, null);
/*    */   }
/*    */ 
/*    */   public ManageReferralControl(boolean paramBoolean)
/*    */   {
/* 64 */     super("2.16.840.1.113730.3.4.2", paramBoolean, null);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.ManageReferralControl
 * JD-Core Version:    0.6.2
 */