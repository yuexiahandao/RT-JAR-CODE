/*     */ package sun.awt.datatransfer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ final class ClassLoaderObjectOutputStream extends ObjectOutputStream
/*     */ {
/* 106 */   private final Map<Set<String>, ClassLoader> map = new HashMap();
/*     */ 
/*     */   ClassLoaderObjectOutputStream(OutputStream paramOutputStream) throws IOException
/*     */   {
/* 110 */     super(paramOutputStream);
/*     */   }
/*     */ 
/*     */   protected void annotateClass(final Class<?> paramClass) throws IOException {
/* 114 */     ClassLoader localClassLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 117 */         return paramClass.getClassLoader();
/*     */       }
/*     */     });
/* 121 */     HashSet localHashSet = new HashSet(1);
/* 122 */     localHashSet.add(paramClass.getName());
/*     */ 
/* 124 */     this.map.put(localHashSet, localClassLoader);
/*     */   }
/*     */   protected void annotateProxyClass(final Class<?> paramClass) throws IOException {
/* 127 */     ClassLoader localClassLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 130 */         return paramClass.getClassLoader();
/*     */       }
/*     */     });
/* 134 */     Class[] arrayOfClass = paramClass.getInterfaces();
/* 135 */     HashSet localHashSet = new HashSet(arrayOfClass.length);
/* 136 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 137 */       localHashSet.add(arrayOfClass[i].getName());
/*     */     }
/*     */ 
/* 140 */     this.map.put(localHashSet, localClassLoader);
/*     */   }
/*     */ 
/*     */   Map<Set<String>, ClassLoader> getClassLoaderMap() {
/* 144 */     return new HashMap(this.map);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.datatransfer.ClassLoaderObjectOutputStream
 * JD-Core Version:    0.6.2
 */