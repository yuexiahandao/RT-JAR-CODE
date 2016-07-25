/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ public class ThreadContext
/*     */   implements Cloneable
/*     */ {
/*     */   private ThreadContext previous;
/*     */   private String key;
/*     */   private Object value;
/* 324 */   private static ThreadLocal<ThreadContext> localContext = new ThreadLocal();
/*     */ 
/*     */   private ThreadContext(ThreadContext paramThreadContext, String paramString, Object paramObject)
/*     */   {
/* 104 */     this.previous = paramThreadContext;
/* 105 */     this.key = paramString;
/* 106 */     this.value = paramObject;
/*     */   }
/*     */ 
/*     */   public static Object get(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 125 */     ThreadContext localThreadContext = contextContaining(paramString);
/* 126 */     if (localThreadContext == null) {
/* 127 */       return null;
/*     */     }
/* 129 */     return localThreadContext.value;
/*     */   }
/*     */ 
/*     */   public static boolean contains(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 148 */     return contextContaining(paramString) != null;
/*     */   }
/*     */ 
/*     */   private static ThreadContext contextContaining(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 159 */     if (paramString == null)
/* 160 */       throw new IllegalArgumentException("null key");
/* 161 */     for (ThreadContext localThreadContext = getContext(); 
/* 162 */       localThreadContext != null; 
/* 163 */       localThreadContext = localThreadContext.previous) {
/* 164 */       if (paramString.equals(localThreadContext.key)) {
/* 165 */         return localThreadContext;
/*     */       }
/*     */     }
/*     */ 
/* 169 */     return null;
/*     */   }
/*     */ 
/*     */   public static ThreadContext push(String paramString, Object paramObject)
/*     */     throws IllegalArgumentException
/*     */   {
/* 213 */     if (paramString == null) {
/* 214 */       throw new IllegalArgumentException("null key");
/*     */     }
/* 216 */     ThreadContext localThreadContext1 = getContext();
/* 217 */     if (localThreadContext1 == null)
/* 218 */       localThreadContext1 = new ThreadContext(null, null, null);
/* 219 */     ThreadContext localThreadContext2 = new ThreadContext(localThreadContext1, paramString, paramObject);
/* 220 */     setContext(localThreadContext2);
/* 221 */     return localThreadContext1;
/*     */   }
/*     */ 
/*     */   public static ThreadContext getThreadContext()
/*     */   {
/* 232 */     return getContext();
/*     */   }
/*     */ 
/*     */   public static void restore(ThreadContext paramThreadContext)
/*     */     throws NullPointerException, IllegalArgumentException
/*     */   {
/* 255 */     if (paramThreadContext == null) {
/* 256 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 259 */     for (ThreadContext localThreadContext = getContext(); 
/* 260 */       localThreadContext != paramThreadContext; 
/* 261 */       localThreadContext = localThreadContext.previous) {
/* 262 */       if (localThreadContext == null) {
/* 263 */         throw new IllegalArgumentException("Restored context is not contained in current context");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 274 */     if (paramThreadContext.key == null) {
/* 275 */       paramThreadContext = null;
/*     */     }
/* 277 */     setContext(paramThreadContext);
/*     */   }
/*     */ 
/*     */   public void setInitialContext(ThreadContext paramThreadContext)
/*     */     throws IllegalArgumentException
/*     */   {
/* 311 */     if (getContext() != null)
/* 312 */       throw new IllegalArgumentException("previous context not empty");
/* 313 */     setContext(paramThreadContext);
/*     */   }
/*     */ 
/*     */   private static ThreadContext getContext() {
/* 317 */     return (ThreadContext)localContext.get();
/*     */   }
/*     */ 
/*     */   private static void setContext(ThreadContext paramThreadContext) {
/* 321 */     localContext.set(paramThreadContext);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.ThreadContext
 * JD-Core Version:    0.6.2
 */