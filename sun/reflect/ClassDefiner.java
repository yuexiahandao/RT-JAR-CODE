/*    */ package sun.reflect;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import sun.misc.Unsafe;
/*    */ 
/*    */ class ClassDefiner
/*    */ {
/* 37 */   static final Unsafe unsafe = Unsafe.getUnsafe();
/*    */ 
/*    */   static Class defineClass(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2, ClassLoader paramClassLoader)
/*    */   {
/* 57 */     ClassLoader localClassLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public ClassLoader run() {
/* 60 */         return new DelegatingClassLoader(this.val$parentClassLoader);
/*    */       }
/*    */     });
/* 63 */     return unsafe.defineClass(paramString, paramArrayOfByte, paramInt1, paramInt2, localClassLoader, null);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.ClassDefiner
 * JD-Core Version:    0.6.2
 */