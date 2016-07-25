/*    */ package sun.tracing.dtrace;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.security.AccessController;
/*    */ import sun.security.action.LoadLibraryAction;
/*    */ 
/*    */ class JVM
/*    */ {
/*    */   static long activate(String paramString, DTraceProvider[] paramArrayOfDTraceProvider)
/*    */   {
/* 43 */     return activate0(paramString, paramArrayOfDTraceProvider);
/*    */   }
/*    */ 
/*    */   static void dispose(long paramLong) {
/* 47 */     dispose0(paramLong);
/*    */   }
/*    */ 
/*    */   static boolean isEnabled(Method paramMethod) {
/* 51 */     return isEnabled0(paramMethod);
/*    */   }
/*    */ 
/*    */   static boolean isSupported() {
/* 55 */     return isSupported0();
/*    */   }
/*    */ 
/*    */   static Class<?> defineClass(ClassLoader paramClassLoader, String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*    */   {
/* 60 */     return defineClass0(paramClassLoader, paramString, paramArrayOfByte, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   private static native long activate0(String paramString, DTraceProvider[] paramArrayOfDTraceProvider);
/*    */ 
/*    */   private static native void dispose0(long paramLong);
/*    */ 
/*    */   private static native boolean isEnabled0(Method paramMethod);
/*    */ 
/*    */   private static native boolean isSupported0();
/*    */ 
/*    */   private static native Class<?> defineClass0(ClassLoader paramClassLoader, String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*    */ 
/*    */   static
/*    */   {
/* 38 */     AccessController.doPrivileged(new LoadLibraryAction("jsdt"));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tracing.dtrace.JVM
 * JD-Core Version:    0.6.2
 */