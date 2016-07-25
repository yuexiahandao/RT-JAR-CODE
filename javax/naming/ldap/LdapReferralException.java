/*    */ package javax.naming.ldap;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.ReferralException;
/*    */ 
/*    */ public abstract class LdapReferralException extends ReferralException
/*    */ {
/*    */   private static final long serialVersionUID = -1668992791764950804L;
/*    */ 
/*    */   protected LdapReferralException(String paramString)
/*    */   {
/* 61 */     super(paramString);
/*    */   }
/*    */ 
/*    */   protected LdapReferralException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public abstract Context getReferralContext()
/*    */     throws NamingException;
/*    */ 
/*    */   public abstract Context getReferralContext(Hashtable<?, ?> paramHashtable)
/*    */     throws NamingException;
/*    */ 
/*    */   public abstract Context getReferralContext(Hashtable<?, ?> paramHashtable, Control[] paramArrayOfControl)
/*    */     throws NamingException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.LdapReferralException
 * JD-Core Version:    0.6.2
 */