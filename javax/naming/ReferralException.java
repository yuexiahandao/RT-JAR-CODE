/*    */ package javax.naming;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public abstract class ReferralException extends NamingException
/*    */ {
/*    */   private static final long serialVersionUID = -2881363844695698876L;
/*    */ 
/*    */   protected ReferralException(String paramString)
/*    */   {
/* 80 */     super(paramString);
/*    */   }
/*    */ 
/*    */   protected ReferralException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public abstract Object getReferralInfo();
/*    */ 
/*    */   public abstract Context getReferralContext()
/*    */     throws NamingException;
/*    */ 
/*    */   public abstract Context getReferralContext(Hashtable<?, ?> paramHashtable)
/*    */     throws NamingException;
/*    */ 
/*    */   public abstract boolean skipReferral();
/*    */ 
/*    */   public abstract void retryReferral();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ReferralException
 * JD-Core Version:    0.6.2
 */