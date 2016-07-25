/*    */ package sun.security.smartcardio;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import sun.security.action.LoadLibraryAction;
/*    */ 
/*    */ class PlatformPCSC
/*    */ {
/* 42 */   static final Throwable initException = loadLibrary();
/*    */   static final int SCARD_PROTOCOL_T0 = 1;
/*    */   static final int SCARD_PROTOCOL_T1 = 2;
/*    */   static final int SCARD_PROTOCOL_RAW = 65536;
/*    */   static final int SCARD_UNKNOWN = 0;
/*    */   static final int SCARD_ABSENT = 1;
/*    */   static final int SCARD_PRESENT = 2;
/*    */   static final int SCARD_SWALLOWED = 3;
/*    */   static final int SCARD_POWERED = 4;
/*    */   static final int SCARD_NEGOTIABLE = 5;
/*    */   static final int SCARD_SPECIFIC = 6;
/*    */ 
/*    */   private static Throwable loadLibrary()
/*    */   {
/*    */     try
/*    */     {
/* 47 */       AccessController.doPrivileged(new LoadLibraryAction("j2pcsc"));
/* 48 */       return null;
/*    */     } catch (Throwable localThrowable) {
/* 50 */       return localThrowable;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.smartcardio.PlatformPCSC
 * JD-Core Version:    0.6.2
 */