/*     */ package sun.invoke.anon;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import sun.misc.IOUtils;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class AnonymousClassLoader
/*     */ {
/*     */   final Class<?> hostClass;
/* 189 */   private static int fakeNameCounter = 99999;
/*     */ 
/* 192 */   private static Unsafe unsafe = Unsafe.getUnsafe();
/*     */ 
/* 207 */   private static final Method defineAnonymousClass = localMethod;
/*     */ 
/*     */   private AnonymousClassLoader(Class<?> paramClass)
/*     */   {
/*  78 */     this.hostClass = paramClass;
/*     */   }
/*     */ 
/*     */   public static AnonymousClassLoader make(Unsafe paramUnsafe, Class<?> paramClass) {
/*  82 */     if (paramUnsafe == null) throw new NullPointerException();
/*  83 */     return new AnonymousClassLoader(paramClass);
/*     */   }
/*     */ 
/*     */   public Class<?> loadClass(byte[] paramArrayOfByte) {
/*  87 */     if (defineAnonymousClass == null) {
/*     */       try
/*     */       {
/*  90 */         return fakeLoadClass(new ConstantPoolParser(paramArrayOfByte).createPatch());
/*     */       } catch (InvalidConstantPoolFormatException localInvalidConstantPoolFormatException) {
/*  92 */         throw new IllegalArgumentException(localInvalidConstantPoolFormatException);
/*     */       }
/*     */     }
/*  95 */     return loadClass(paramArrayOfByte, null);
/*     */   }
/*     */ 
/*     */   public Class<?> loadClass(ConstantPoolPatch paramConstantPoolPatch) {
/*  99 */     if (defineAnonymousClass == null)
/*     */     {
/* 101 */       return fakeLoadClass(paramConstantPoolPatch);
/*     */     }
/* 103 */     Object[] arrayOfObject = paramConstantPoolPatch.patchArray;
/*     */ 
/* 107 */     for (int i = 0; i < arrayOfObject.length; i++) {
/* 108 */       Object localObject = arrayOfObject[i];
/* 109 */       if (localObject != null) {
/* 110 */         int j = paramConstantPoolPatch.getTag(i);
/* 111 */         switch (j) {
/*     */         case 7:
/* 113 */           if ((localObject instanceof String)) {
/* 114 */             if (arrayOfObject == paramConstantPoolPatch.patchArray)
/* 115 */               arrayOfObject = (Object[])arrayOfObject.clone();
/* 116 */             arrayOfObject[i] = ((String)localObject).replace('.', '/');
/*     */           }
/*     */           break;
/*     */         case 8:
/*     */         case 9:
/*     */         case 10:
/*     */         case 11:
/*     */         case 12:
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 130 */     return loadClass(paramConstantPoolPatch.outer.classFile, paramConstantPoolPatch.patchArray);
/*     */   }
/*     */ 
/*     */   private Class<?> loadClass(byte[] paramArrayOfByte, Object[] paramArrayOfObject) {
/*     */     try {
/* 135 */       return (Class)defineAnonymousClass.invoke(unsafe, new Object[] { this.hostClass, paramArrayOfByte, paramArrayOfObject });
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 139 */       throwReflectedException(localException);
/* 140 */       throw new RuntimeException("error loading into " + this.hostClass, localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void throwReflectedException(Exception paramException) {
/* 145 */     if ((paramException instanceof InvocationTargetException)) {
/* 146 */       Throwable localThrowable = ((InvocationTargetException)paramException).getTargetException();
/* 147 */       if ((localThrowable instanceof Error))
/* 148 */         throw ((Error)localThrowable);
/* 149 */       paramException = (Exception)localThrowable;
/*     */     }
/* 151 */     if ((paramException instanceof RuntimeException))
/* 152 */       throw ((RuntimeException)paramException);
/*     */   }
/*     */ 
/*     */   private Class<?> fakeLoadClass(ConstantPoolPatch paramConstantPoolPatch)
/*     */   {
/* 171 */     throw new UnsupportedOperationException("NYI");
/*     */   }
/*     */ 
/*     */   private static void noJVMSupport()
/*     */   {
/* 211 */     throw new UnsupportedOperationException("no JVM support for anonymous classes");
/*     */   }
/*     */ 
/*     */   private static native Class<?> loadClassInternal(Class<?> paramClass, byte[] paramArrayOfByte, Object[] paramArrayOfObject);
/*     */ 
/*     */   public static byte[] readClassFile(Class<?> paramClass)
/*     */     throws IOException
/*     */   {
/* 220 */     String str = paramClass.getName();
/* 221 */     int i = str.lastIndexOf('.');
/* 222 */     URL localURL = paramClass.getResource(str.substring(i + 1) + ".class");
/* 223 */     URLConnection localURLConnection = localURL.openConnection();
/* 224 */     int j = localURLConnection.getContentLength();
/* 225 */     if (j < 0) {
/* 226 */       throw new IOException("invalid content length " + j);
/*     */     }
/* 228 */     return IOUtils.readFully(localURLConnection.getInputStream(), j, true);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 197 */     Method localMethod = null;
/* 198 */     Class localClass = unsafe.getClass();
/*     */     try {
/* 200 */       localMethod = localClass.getMethod("defineAnonymousClass", new Class[] { Class.class, [B.class, [Ljava.lang.Object.class });
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 205 */       localMethod = null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.invoke.anon.AnonymousClassLoader
 * JD-Core Version:    0.6.2
 */