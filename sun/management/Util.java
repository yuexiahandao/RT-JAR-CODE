/*    */ package sun.management;
/*    */ 
/*    */ import java.lang.management.ManagementPermission;
/*    */ import java.util.List;
/*    */ import javax.management.MalformedObjectNameException;
/*    */ import javax.management.ObjectName;
/*    */ 
/*    */ public class Util
/*    */ {
/* 41 */   private static final String[] EMPTY_STRING_ARRAY = new String[0];
/*    */ 
/* 58 */   private static ManagementPermission monitorPermission = new ManagementPermission("monitor");
/*    */ 
/* 60 */   private static ManagementPermission controlPermission = new ManagementPermission("control");
/*    */ 
/*    */   static RuntimeException newException(Exception paramException)
/*    */   {
/* 38 */     throw new RuntimeException(paramException);
/*    */   }
/*    */ 
/*    */   static String[] toStringArray(List<String> paramList)
/*    */   {
/* 43 */     return (String[])paramList.toArray(EMPTY_STRING_ARRAY);
/*    */   }
/*    */ 
/*    */   public static ObjectName newObjectName(String paramString1, String paramString2) {
/* 47 */     return newObjectName(paramString1 + ",name=" + paramString2);
/*    */   }
/*    */ 
/*    */   public static ObjectName newObjectName(String paramString) {
/*    */     try {
/* 52 */       return ObjectName.getInstance(paramString);
/*    */     } catch (MalformedObjectNameException localMalformedObjectNameException) {
/* 54 */       throw new IllegalArgumentException(localMalformedObjectNameException);
/*    */     }
/*    */   }
/*    */ 
/*    */   static void checkAccess(ManagementPermission paramManagementPermission)
/*    */     throws SecurityException
/*    */   {
/* 75 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 76 */     if (localSecurityManager != null)
/* 77 */       localSecurityManager.checkPermission(paramManagementPermission);
/*    */   }
/*    */ 
/*    */   static void checkMonitorAccess() throws SecurityException
/*    */   {
/* 82 */     checkAccess(monitorPermission);
/*    */   }
/*    */   static void checkControlAccess() throws SecurityException {
/* 85 */     checkAccess(controlPermission);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.Util
 * JD-Core Version:    0.6.2
 */