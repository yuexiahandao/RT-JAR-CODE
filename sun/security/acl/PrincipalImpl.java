/*    */ package sun.security.acl;
/*    */ 
/*    */ import java.security.Principal;
/*    */ 
/*    */ public class PrincipalImpl
/*    */   implements Principal
/*    */ {
/*    */   private String user;
/*    */ 
/*    */   public PrincipalImpl(String paramString)
/*    */   {
/* 44 */     this.user = paramString;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 55 */     if ((paramObject instanceof PrincipalImpl)) {
/* 56 */       PrincipalImpl localPrincipalImpl = (PrincipalImpl)paramObject;
/* 57 */       return this.user.equals(localPrincipalImpl.toString());
/*    */     }
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 66 */     return this.user;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 73 */     return this.user.hashCode();
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 80 */     return this.user;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.acl.PrincipalImpl
 * JD-Core Version:    0.6.2
 */