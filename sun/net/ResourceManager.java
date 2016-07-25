/*    */ package sun.net;
/*    */ 
/*    */ import java.net.SocketException;
/*    */ import java.security.AccessController;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import sun.security.action.GetPropertyAction;
/*    */ 
/*    */ public class ResourceManager
/*    */ {
/*    */   private static final int DEFAULT_MAX_SOCKETS = 25;
/* 65 */   private static final int maxSockets = i;
/* 66 */   private static final AtomicInteger numSockets = new AtomicInteger(0);
/*    */ 
/*    */   public static void beforeUdpCreate() throws SocketException
/*    */   {
/* 70 */     if ((System.getSecurityManager() != null) && 
/* 71 */       (numSockets.incrementAndGet() > maxSockets)) {
/* 72 */       numSockets.decrementAndGet();
/* 73 */       throw new SocketException("maximum number of DatagramSockets reached");
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void afterUdpClose()
/*    */   {
/* 79 */     if (System.getSecurityManager() != null)
/* 80 */       numSockets.decrementAndGet();
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 56 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.net.maxDatagramSockets"));
/*    */ 
/* 59 */     int i = 25;
/*    */     try {
/* 61 */       if (str != null)
/* 62 */         i = Integer.parseInt(str);
/*    */     }
/*    */     catch (NumberFormatException localNumberFormatException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.ResourceManager
 * JD-Core Version:    0.6.2
 */