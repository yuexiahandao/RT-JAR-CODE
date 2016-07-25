/*    */ package sun.net;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ public final class PortConfig
/*    */ {
/* 52 */   private static final int upper = getUpper0();
/*    */   private static final int lower;
/*    */ 
/*    */   static native int getLower0();
/*    */ 
/*    */   static native int getUpper0();
/*    */ 
/*    */   public static int getLower()
/*    */   {
/* 59 */     return lower;
/*    */   }
/*    */ 
/*    */   public static int getUpper() {
/* 63 */     return upper;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 43 */     AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Void run() {
/* 46 */         System.loadLibrary("net");
/* 47 */         return null;
/*    */       }
/*    */     });
/* 51 */     lower = getLower0();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.PortConfig
 * JD-Core Version:    0.6.2
 */