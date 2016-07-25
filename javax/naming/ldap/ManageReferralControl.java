/*    */ package javax.naming.ldap;
/*    */ 
/*    */ public final class ManageReferralControl extends BasicControl
/*    */ {
/*    */   public static final String OID = "2.16.840.1.113730.3.4.2";
/*    */   private static final long serialVersionUID = 3017756160149982566L;
/*    */ 
/*    */   public ManageReferralControl()
/*    */   {
/* 56 */     super("2.16.840.1.113730.3.4.2", true, null);
/*    */   }
/*    */ 
/*    */   public ManageReferralControl(boolean paramBoolean)
/*    */   {
/* 65 */     super("2.16.840.1.113730.3.4.2", paramBoolean, null);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.ManageReferralControl
 * JD-Core Version:    0.6.2
 */