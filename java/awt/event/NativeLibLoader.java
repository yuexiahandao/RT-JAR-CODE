/*    */ package java.awt.event;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import sun.security.action.LoadLibraryAction;
/*    */ 
/*    */ class NativeLibLoader
/*    */ {
/*    */   static void loadLibraries()
/*    */   {
/* 56 */     AccessController.doPrivileged(new LoadLibraryAction("awt"));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.NativeLibLoader
 * JD-Core Version:    0.6.2
 */