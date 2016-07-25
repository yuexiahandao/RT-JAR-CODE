/*    */ package java.net;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedExceptionAction;
/*    */ 
/*    */ class NetUtil
/*    */ {
/*    */   private static boolean revealLocalAddress;
/*    */   private static volatile boolean propRevealLocalAddr;
/*    */ 
/*    */   static boolean doRevealLocalAddress()
/*    */   {
/* 43 */     return propRevealLocalAddr ? revealLocalAddress : readRevealLocalAddr();
/*    */   }
/*    */ 
/*    */   private static boolean readRevealLocalAddr()
/*    */   {
/* 49 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 50 */     if (localSecurityManager != null) {
/*    */       try {
/* 52 */         revealLocalAddress = Boolean.parseBoolean((String)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*    */         {
/*    */           public String run()
/*    */           {
/* 57 */             return System.getProperty("jdk.net.revealLocalAddress");
/*    */           }
/*    */         }));
/*    */       }
/*    */       catch (Exception localException)
/*    */       {
/*    */       }
/*    */ 
/* 65 */       propRevealLocalAddr = true;
/*    */     }
/*    */ 
/* 71 */     return revealLocalAddress;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.NetUtil
 * JD-Core Version:    0.6.2
 */