/*    */ package sun.awt;
/*    */ 
/*    */ import java.awt.AWTPermission;
/*    */ import sun.security.util.PermissionFactory;
/*    */ 
/*    */ public class AWTPermissionFactory
/*    */   implements PermissionFactory<AWTPermission>
/*    */ {
/*    */   public AWTPermission newPermission(String paramString)
/*    */   {
/* 40 */     return new AWTPermission(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.AWTPermissionFactory
 * JD-Core Version:    0.6.2
 */