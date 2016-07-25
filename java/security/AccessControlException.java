/*    */ package java.security;
/*    */ 
/*    */ public class AccessControlException extends SecurityException
/*    */ {
/*    */   private static final long serialVersionUID = 5138225684096988535L;
/*    */   private Permission perm;
/*    */ 
/*    */   public AccessControlException(String paramString)
/*    */   {
/* 57 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public AccessControlException(String paramString, Permission paramPermission)
/*    */   {
/* 69 */     super(paramString);
/* 70 */     this.perm = paramPermission;
/*    */   }
/*    */ 
/*    */   public Permission getPermission()
/*    */   {
/* 80 */     return this.perm;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.AccessControlException
 * JD-Core Version:    0.6.2
 */