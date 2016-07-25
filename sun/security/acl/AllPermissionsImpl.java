/*    */ package sun.security.acl;
/*    */ 
/*    */ import java.security.acl.Permission;
/*    */ 
/*    */ public class AllPermissionsImpl extends PermissionImpl
/*    */ {
/*    */   public AllPermissionsImpl(String paramString)
/*    */   {
/* 38 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public boolean equals(Permission paramPermission)
/*    */   {
/* 48 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.acl.AllPermissionsImpl
 * JD-Core Version:    0.6.2
 */