/*    */ package sun.security.provider;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.Provider;
/*    */ import java.util.LinkedHashMap;
/*    */ import sun.security.action.PutAllAction;
/*    */ import sun.security.rsa.SunRsaSignEntries;
/*    */ 
/*    */ public final class VerificationProvider extends Provider
/*    */ {
/*    */   private static final long serialVersionUID = 7482667077568930381L;
/* 60 */   private static final boolean ACTIVE = bool;
/*    */ 
/*    */   public VerificationProvider()
/*    */   {
/* 64 */     super("SunJarVerification", 1.7D, "Jar Verification Provider");
/*    */ 
/* 67 */     if (!ACTIVE) {
/* 68 */       return;
/*    */     }
/*    */ 
/* 74 */     if (System.getSecurityManager() == null) {
/* 75 */       SunEntries.putEntries(this);
/* 76 */       SunRsaSignEntries.putEntries(this);
/*    */     }
/*    */     else {
/* 79 */       LinkedHashMap localLinkedHashMap = new LinkedHashMap();
/* 80 */       SunEntries.putEntries(localLinkedHashMap);
/* 81 */       SunRsaSignEntries.putEntries(localLinkedHashMap);
/* 82 */       AccessController.doPrivileged(new PutAllAction(this, localLinkedHashMap));
/*    */     }
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/*    */     boolean bool;
/*    */     try
/*    */     {
/* 54 */       Class.forName("sun.security.provider.Sun");
/* 55 */       Class.forName("sun.security.rsa.SunRsaSign");
/* 56 */       bool = false;
/*    */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 58 */       bool = true;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.VerificationProvider
 * JD-Core Version:    0.6.2
 */