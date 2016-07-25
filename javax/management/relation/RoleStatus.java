/*    */ package javax.management.relation;
/*    */ 
/*    */ public class RoleStatus
/*    */ {
/*    */   public static final int NO_ROLE_WITH_NAME = 1;
/*    */   public static final int ROLE_NOT_READABLE = 2;
/*    */   public static final int ROLE_NOT_WRITABLE = 3;
/*    */   public static final int LESS_THAN_MIN_ROLE_DEGREE = 4;
/*    */   public static final int MORE_THAN_MAX_ROLE_DEGREE = 5;
/*    */   public static final int REF_MBEAN_OF_INCORRECT_CLASS = 6;
/*    */   public static final int REF_MBEAN_NOT_REGISTERED = 7;
/*    */ 
/*    */   public static boolean isRoleStatus(int paramInt)
/*    */   {
/* 82 */     if ((paramInt != 1) && (paramInt != 2) && (paramInt != 3) && (paramInt != 4) && (paramInt != 5) && (paramInt != 6) && (paramInt != 7))
/*    */     {
/* 89 */       return false;
/*    */     }
/* 91 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.relation.RoleStatus
 * JD-Core Version:    0.6.2
 */