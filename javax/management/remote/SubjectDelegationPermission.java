/*    */ package javax.management.remote;
/*    */ 
/*    */ import java.security.BasicPermission;
/*    */ 
/*    */ public final class SubjectDelegationPermission extends BasicPermission
/*    */ {
/*    */   private static final long serialVersionUID = 1481618113008682343L;
/*    */ 
/*    */   public SubjectDelegationPermission(String paramString)
/*    */   {
/* 73 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public SubjectDelegationPermission(String paramString1, String paramString2)
/*    */   {
/* 91 */     super(paramString1, paramString2);
/*    */ 
/* 93 */     if (paramString2 != null)
/* 94 */       throw new IllegalArgumentException("Non-null actions");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.SubjectDelegationPermission
 * JD-Core Version:    0.6.2
 */