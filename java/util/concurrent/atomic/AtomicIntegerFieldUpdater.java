/*     */ package java.util.concurrent.atomic;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.Reflection;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public abstract class AtomicIntegerFieldUpdater<T>
/*     */ {
/*     */   @CallerSensitive
/*     */   public static <U> AtomicIntegerFieldUpdater<U> newUpdater(Class<U> paramClass, String paramString)
/*     */   {
/*  76 */     return new AtomicIntegerFieldUpdaterImpl(paramClass, paramString, Reflection.getCallerClass());
/*     */   }
/*     */ 
/*     */   public abstract boolean compareAndSet(T paramT, int paramInt1, int paramInt2);
/*     */ 
/*     */   public abstract boolean weakCompareAndSet(T paramT, int paramInt1, int paramInt2);
/*     */ 
/*     */   public abstract void set(T paramT, int paramInt);
/*     */ 
/*     */   public abstract void lazySet(T paramT, int paramInt);
/*     */ 
/*     */   public abstract int get(T paramT);
/*     */ 
/*     */   public int getAndSet(T paramT, int paramInt)
/*     */   {
/*     */     while (true)
/*     */     {
/* 161 */       int i = get(paramT);
/* 162 */       if (compareAndSet(paramT, i, paramInt))
/* 163 */         return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getAndIncrement(T paramT)
/*     */   {
/*     */     while (true)
/*     */     {
/* 176 */       int i = get(paramT);
/* 177 */       int j = i + 1;
/* 178 */       if (compareAndSet(paramT, i, j))
/* 179 */         return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getAndDecrement(T paramT)
/*     */   {
/*     */     while (true)
/*     */     {
/* 192 */       int i = get(paramT);
/* 193 */       int j = i - 1;
/* 194 */       if (compareAndSet(paramT, i, j))
/* 195 */         return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getAndAdd(T paramT, int paramInt)
/*     */   {
/*     */     while (true)
/*     */     {
/* 209 */       int i = get(paramT);
/* 210 */       int j = i + paramInt;
/* 211 */       if (compareAndSet(paramT, i, j))
/* 212 */         return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int incrementAndGet(T paramT)
/*     */   {
/*     */     while (true)
/*     */     {
/* 225 */       int i = get(paramT);
/* 226 */       int j = i + 1;
/* 227 */       if (compareAndSet(paramT, i, j))
/* 228 */         return j;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int decrementAndGet(T paramT)
/*     */   {
/*     */     while (true)
/*     */     {
/* 241 */       int i = get(paramT);
/* 242 */       int j = i - 1;
/* 243 */       if (compareAndSet(paramT, i, j))
/* 244 */         return j;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int addAndGet(T paramT, int paramInt)
/*     */   {
/*     */     while (true)
/*     */     {
/* 258 */       int i = get(paramT);
/* 259 */       int j = i + paramInt;
/* 260 */       if (compareAndSet(paramT, i, j))
/* 261 */         return j;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class AtomicIntegerFieldUpdaterImpl<T> extends AtomicIntegerFieldUpdater<T> {
/* 269 */     private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */     private final long offset;
/*     */     private final Class<T> tclass;
/*     */     private final Class cclass;
/*     */ 
/*     */     AtomicIntegerFieldUpdaterImpl(Class<T> paramClass, String paramString, Class<?> paramClass1) {
/* 275 */       Field localField = null;
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
/* 288 */       if (localClass != Integer.TYPE) {
/* 289 */         throw new IllegalArgumentException("Must be integer type");
/*     */       }
/* 291 */       if (!Modifier.isVolatile(i)) {
/* 292 */         throw new IllegalArgumentException("Must be volatile type");
/*     */       }
/* 294 */       this.cclass = ((Modifier.isProtected(i)) && (paramClass1 != paramClass) ? paramClass1 : null);
/*     */ 
/* 296 */       this.tclass = paramClass;
/* 297 */       this.offset = unsafe.objectFieldOffset(localField);
/*     */     }
/*     */ 
/*     */     private void fullCheck(T paramT) {
/* 301 */       if (!this.tclass.isInstance(paramT))
/* 302 */         throw new ClassCastException();
/* 303 */       if (this.cclass != null)
/* 304 */         ensureProtectedAccess(paramT);
/*     */     }
/*     */ 
/*     */     public boolean compareAndSet(T paramT, int paramInt1, int paramInt2) {
/* 308 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null)) fullCheck(paramT);
/* 309 */       return unsafe.compareAndSwapInt(paramT, this.offset, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public boolean weakCompareAndSet(T paramT, int paramInt1, int paramInt2) {
/* 313 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null)) fullCheck(paramT);
/* 314 */       return unsafe.compareAndSwapInt(paramT, this.offset, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public void set(T paramT, int paramInt) {
/* 318 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null)) fullCheck(paramT);
/* 319 */       unsafe.putIntVolatile(paramT, this.offset, paramInt);
/*     */     }
/*     */ 
/*     */     public void lazySet(T paramT, int paramInt) {
/* 323 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null)) fullCheck(paramT);
/* 324 */       unsafe.putOrderedInt(paramT, this.offset, paramInt);
/*     */     }
/*     */ 
/*     */     public final int get(T paramT) {
/* 328 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null)) fullCheck(paramT);
/* 329 */       return unsafe.getIntVolatile(paramT, this.offset);
/*     */     }
/*     */ 
/*     */     private void ensureProtectedAccess(T paramT) {
/* 333 */       if (this.cclass.isInstance(paramT)) {
/* 334 */         return;
/*     */       }
/* 336 */       throw new RuntimeException(new IllegalAccessException("Class " + this.cclass.getName() + " can not access a protected member of class " + this.tclass.getName() + " using an instance of " + paramT.getClass().getName()));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.atomic.AtomicIntegerFieldUpdater
 * JD-Core Version:    0.6.2
 */