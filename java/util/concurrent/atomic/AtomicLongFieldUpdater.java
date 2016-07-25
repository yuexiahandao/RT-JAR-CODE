/*     */ package java.util.concurrent.atomic;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.Reflection;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public abstract class AtomicLongFieldUpdater<T>
/*     */ {
/*     */   @CallerSensitive
/*     */   public static <U> AtomicLongFieldUpdater<U> newUpdater(Class<U> paramClass, String paramString)
/*     */   {
/*  76 */     Class localClass = Reflection.getCallerClass();
/*  77 */     if (AtomicLong.VM_SUPPORTS_LONG_CAS) {
/*  78 */       return new CASUpdater(paramClass, paramString, localClass);
/*     */     }
/*  80 */     return new LockedUpdater(paramClass, paramString, localClass);
/*     */   }
/*     */ 
/*     */   public abstract boolean compareAndSet(T paramT, long paramLong1, long paramLong2);
/*     */ 
/*     */   public abstract boolean weakCompareAndSet(T paramT, long paramLong1, long paramLong2);
/*     */ 
/*     */   public abstract void set(T paramT, long paramLong);
/*     */ 
/*     */   public abstract void lazySet(T paramT, long paramLong);
/*     */ 
/*     */   public abstract long get(T paramT);
/*     */ 
/*     */   public long getAndSet(T paramT, long paramLong)
/*     */   {
/*     */     while (true)
/*     */     {
/* 164 */       long l = get(paramT);
/* 165 */       if (compareAndSet(paramT, l, paramLong))
/* 166 */         return l;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long getAndIncrement(T paramT)
/*     */   {
/*     */     while (true)
/*     */     {
/* 179 */       long l1 = get(paramT);
/* 180 */       long l2 = l1 + 1L;
/* 181 */       if (compareAndSet(paramT, l1, l2))
/* 182 */         return l1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long getAndDecrement(T paramT)
/*     */   {
/*     */     while (true)
/*     */     {
/* 195 */       long l1 = get(paramT);
/* 196 */       long l2 = l1 - 1L;
/* 197 */       if (compareAndSet(paramT, l1, l2))
/* 198 */         return l1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long getAndAdd(T paramT, long paramLong)
/*     */   {
/*     */     while (true)
/*     */     {
/* 212 */       long l1 = get(paramT);
/* 213 */       long l2 = l1 + paramLong;
/* 214 */       if (compareAndSet(paramT, l1, l2))
/* 215 */         return l1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long incrementAndGet(T paramT)
/*     */   {
/*     */     while (true)
/*     */     {
/* 228 */       long l1 = get(paramT);
/* 229 */       long l2 = l1 + 1L;
/* 230 */       if (compareAndSet(paramT, l1, l2))
/* 231 */         return l2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long decrementAndGet(T paramT)
/*     */   {
/*     */     while (true)
/*     */     {
/* 244 */       long l1 = get(paramT);
/* 245 */       long l2 = l1 - 1L;
/* 246 */       if (compareAndSet(paramT, l1, l2))
/* 247 */         return l2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long addAndGet(T paramT, long paramLong)
/*     */   {
/*     */     while (true)
/*     */     {
/* 261 */       long l1 = get(paramT);
/* 262 */       long l2 = l1 + paramLong;
/* 263 */       if (compareAndSet(paramT, l1, l2))
/* 264 */         return l2;  } 
/*     */   }
/*     */ 
/* 269 */   private static class CASUpdater<T> extends AtomicLongFieldUpdater<T> { private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */     private final long offset;
/*     */     private final Class<T> tclass;
/*     */     private final Class cclass;
/*     */ 
/* 275 */     CASUpdater(Class<T> paramClass, String paramString, Class<?> paramClass1) { Field localField = null;
/* 276 */       int i = 0;
/*     */       try {
/* 278 */         localField = paramClass.getDeclaredField(paramString);
/* 279 */         i = localField.getModifiers();
/* 280 */         ReflectUtil.ensureMemberAccess(paramClass1, paramClass, null, i);
/*     */ 
/* 282 */         ReflectUtil.checkPackageAccess(paramClass);
/*     */       } catch (Exception localException) {
/* 284 */         throw new RuntimeException(localException);
/*     */       }
/*     */ 
/* 287 */       Class localClass = localField.getType();
/* 288 */       if (localClass != Long.TYPE) {
/* 289 */         throw new IllegalArgumentException("Must be long type");
/*     */       }
/* 291 */       if (!Modifier.isVolatile(i)) {
/* 292 */         throw new IllegalArgumentException("Must be volatile type");
/*     */       }
/* 294 */       this.cclass = ((Modifier.isProtected(i)) && (paramClass1 != paramClass) ? paramClass1 : null);
/*     */ 
/* 296 */       this.tclass = paramClass;
/* 297 */       this.offset = unsafe.objectFieldOffset(localField); }
/*     */ 
/*     */     private void fullCheck(T paramT)
/*     */     {
/* 301 */       if (!this.tclass.isInstance(paramT))
/* 302 */         throw new ClassCastException();
/* 303 */       if (this.cclass != null)
/* 304 */         ensureProtectedAccess(paramT);
/*     */     }
/*     */ 
/*     */     public boolean compareAndSet(T paramT, long paramLong1, long paramLong2) {
/* 308 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null)) fullCheck(paramT);
/* 309 */       return unsafe.compareAndSwapLong(paramT, this.offset, paramLong1, paramLong2);
/*     */     }
/*     */ 
/*     */     public boolean weakCompareAndSet(T paramT, long paramLong1, long paramLong2) {
/* 313 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null)) fullCheck(paramT);
/* 314 */       return unsafe.compareAndSwapLong(paramT, this.offset, paramLong1, paramLong2);
/*     */     }
/*     */ 
/*     */     public void set(T paramT, long paramLong) {
/* 318 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null)) fullCheck(paramT);
/* 319 */       unsafe.putLongVolatile(paramT, this.offset, paramLong);
/*     */     }
/*     */ 
/*     */     public void lazySet(T paramT, long paramLong) {
/* 323 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null)) fullCheck(paramT);
/* 324 */       unsafe.putOrderedLong(paramT, this.offset, paramLong);
/*     */     }
/*     */ 
/*     */     public long get(T paramT) {
/* 328 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null)) fullCheck(paramT);
/* 329 */       return unsafe.getLongVolatile(paramT, this.offset);
/*     */     }
/*     */ 
/*     */     private void ensureProtectedAccess(T paramT) {
/* 333 */       if (this.cclass.isInstance(paramT)) {
/* 334 */         return;
/*     */       }
/* 336 */       throw new RuntimeException(new IllegalAccessException("Class " + this.cclass.getName() + " can not access a protected member of class " + this.tclass.getName() + " using an instance of " + paramT.getClass().getName()));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class LockedUpdater<T> extends AtomicLongFieldUpdater<T>
/*     */   {
/* 350 */     private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */     private final long offset;
/*     */     private final Class<T> tclass;
/*     */     private final Class cclass;
/*     */ 
/*     */     LockedUpdater(Class<T> paramClass, String paramString, Class<?> paramClass1)
/*     */     {
/* 356 */       Field localField = null;
/* 357 */       int i = 0;
/*     */       try {
/* 359 */         localField = paramClass.getDeclaredField(paramString);
/* 360 */         i = localField.getModifiers();
/* 361 */         ReflectUtil.ensureMemberAccess(paramClass1, paramClass, null, i);
/*     */ 
/* 363 */         ReflectUtil.checkPackageAccess(paramClass);
/*     */       } catch (Exception localException) {
/* 365 */         throw new RuntimeException(localException);
/*     */       }
/*     */ 
/* 368 */       Class localClass = localField.getType();
/* 369 */       if (localClass != Long.TYPE) {
/* 370 */         throw new IllegalArgumentException("Must be long type");
/*     */       }
/* 372 */       if (!Modifier.isVolatile(i)) {
/* 373 */         throw new IllegalArgumentException("Must be volatile type");
/*     */       }
/* 375 */       this.cclass = ((Modifier.isProtected(i)) && (paramClass1 != paramClass) ? paramClass1 : null);
/*     */ 
/* 377 */       this.tclass = paramClass;
/* 378 */       this.offset = unsafe.objectFieldOffset(localField);
/*     */     }
/*     */ 
/*     */     private void fullCheck(T paramT) {
/* 382 */       if (!this.tclass.isInstance(paramT))
/* 383 */         throw new ClassCastException();
/* 384 */       if (this.cclass != null)
/* 385 */         ensureProtectedAccess(paramT);
/*     */     }
/*     */ 
/*     */     public boolean compareAndSet(T paramT, long paramLong1, long paramLong2) {
/* 389 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null)) fullCheck(paramT);
/* 390 */       synchronized (this) {
/* 391 */         long l = unsafe.getLong(paramT, this.offset);
/* 392 */         if (l != paramLong1)
/* 393 */           return false;
/* 394 */         unsafe.putLong(paramT, this.offset, paramLong2);
/* 395 */         return true;
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean weakCompareAndSet(T paramT, long paramLong1, long paramLong2) {
/* 400 */       return compareAndSet(paramT, paramLong1, paramLong2);
/*     */     }
/*     */ 
/*     */     public void set(T paramT, long paramLong) {
/* 404 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null)) fullCheck(paramT);
/* 405 */       synchronized (this) {
/* 406 */         unsafe.putLong(paramT, this.offset, paramLong);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void lazySet(T paramT, long paramLong) {
/* 411 */       set(paramT, paramLong);
/*     */     }
/*     */ 
/*     */     public long get(T paramT) {
/* 415 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null)) fullCheck(paramT);
/* 416 */       synchronized (this) {
/* 417 */         return unsafe.getLong(paramT, this.offset);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void ensureProtectedAccess(T paramT) {
/* 422 */       if (this.cclass.isInstance(paramT)) {
/* 423 */         return;
/*     */       }
/* 425 */       throw new RuntimeException(new IllegalAccessException("Class " + this.cclass.getName() + " can not access a protected member of class " + this.tclass.getName() + " using an instance of " + paramT.getClass().getName()));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.atomic.AtomicLongFieldUpdater
 * JD-Core Version:    0.6.2
 */