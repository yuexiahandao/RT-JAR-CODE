/*     */ package sun.awt.datatransfer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ final class ClassLoaderObjectInputStream extends ObjectInputStream
/*     */ {
/*     */   private final Map<Set<String>, ClassLoader> map;
/*     */ 
/*     */   ClassLoaderObjectInputStream(InputStream paramInputStream, Map<Set<String>, ClassLoader> paramMap)
/*     */     throws IOException
/*     */   {
/* 154 */     super(paramInputStream);
/* 155 */     if (paramMap == null) {
/* 156 */       throw new NullPointerException("Null map");
/*     */     }
/* 158 */     this.map = paramMap;
/*     */   }
/*     */ 
/*     */   protected Class<?> resolveClass(ObjectStreamClass paramObjectStreamClass) throws IOException, ClassNotFoundException
/*     */   {
/* 163 */     String str = paramObjectStreamClass.getName();
/*     */ 
/* 165 */     HashSet localHashSet = new HashSet(1);
/* 166 */     localHashSet.add(str);
/*     */ 
/* 168 */     ClassLoader localClassLoader = (ClassLoader)this.map.get(localHashSet);
/* 169 */     if (localClassLoader != null) {
/* 170 */       return Class.forName(str, false, localClassLoader);
/*     */     }
/* 172 */     return super.resolveClass(paramObjectStreamClass);
/*     */   }
/*     */ 
/*     */   protected Class<?> resolveProxyClass(String[] paramArrayOfString)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 179 */     HashSet localHashSet = new HashSet(paramArrayOfString.length);
/* 180 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 181 */       localHashSet.add(paramArrayOfString[i]);
/*     */     }
/*     */ 
/* 184 */     ClassLoader localClassLoader1 = (ClassLoader)this.map.get(localHashSet);
/* 185 */     if (localClassLoader1 == null) {
/* 186 */       return super.resolveProxyClass(paramArrayOfString);
/*     */     }
/*     */ 
/* 190 */     ClassLoader localClassLoader2 = null;
/* 191 */     int j = 0;
/*     */ 
/* 194 */     Class[] arrayOfClass = new Class[paramArrayOfString.length];
/* 195 */     for (int k = 0; k < paramArrayOfString.length; k++) {
/* 196 */       Class localClass = Class.forName(paramArrayOfString[k], false, localClassLoader1);
/* 197 */       if ((localClass.getModifiers() & 0x1) == 0) {
/* 198 */         if (j != 0) {
/* 199 */           if (localClassLoader2 != localClass.getClassLoader())
/* 200 */             throw new IllegalAccessError("conflicting non-public interface class loaders");
/*     */         }
/*     */         else
/*     */         {
/* 204 */           localClassLoader2 = localClass.getClassLoader();
/* 205 */           j = 1;
/*     */         }
/*     */       }
/* 208 */       arrayOfClass[k] = localClass;
/*     */     }
/*     */     try {
/* 211 */       return Proxy.getProxyClass(j != 0 ? localClassLoader2 : localClassLoader1, arrayOfClass);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException)
/*     */     {
/* 215 */       throw new ClassNotFoundException(null, localIllegalArgumentException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.datatransfer.ClassLoaderObjectInputStream
 * JD-Core Version:    0.6.2
 */