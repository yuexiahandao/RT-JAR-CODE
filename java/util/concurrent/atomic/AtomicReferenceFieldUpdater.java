/*     */ package java.util.concurrent.atomic;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.Reflection;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public abstract class AtomicReferenceFieldUpdater<T, V>
/*     */ {
/*     */   @CallerSensitive
/*     */   public static <U, W> AtomicReferenceFieldUpdater<U, W> newUpdater(Class<U> paramClass, Class<W> paramClass1, String paramString)
/*     */   {
/*  95 */     return new AtomicReferenceFieldUpdaterImpl(paramClass, paramClass1, paramString, Reflection.getCallerClass());
/*     */   }
/*     */ 
/*     */   public abstract boolean compareAndSet(T paramT, V paramV1, V paramV2);
/*     */ 
/*     */   public abstract boolean weakCompareAndSet(T paramT, V paramV1, V paramV2);
/*     */ 
/*     */   public abstract void set(T paramT, V paramV);
/*     */ 
/*     */   public abstract void lazySet(T paramT, V paramV);
/*     */ 
/*     */   public abstract V get(T paramT);
/*     */ 
/*     */   public V getAndSet(T paramT, V paramV)
/*     */   {
/*     */     while (true)
/*     */     {
/* 178 */       Object localObject = get(paramT);
/* 179 */       if (compareAndSet(paramT, localObject, paramV))
/* 180 */         return localObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class AtomicReferenceFieldUpdaterImpl<T, V> extends AtomicReferenceFieldUpdater<T, V>
/*     */   {
/* 186 */     private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */     private final long offset;
/*     */     private final Class<T> tclass;
/*     */     private final Class<V> vclass;
/*     */     private final Class cclass;
/*     */ 
/*     */     AtomicReferenceFieldUpdaterImpl(Class<T> paramClass, Class<V> paramClass1, String paramString, Class<?> paramClass2)
/*     */     {
/* 208 */       Field localField = null;
/* 209 */       Class localClass = null;
/* 210 */       int i = 0;
/*     */       try {
/* 212 */         localField = paramClass.getDeclaredField(paramString);
/* 213 */         i = localField.getModifiers();
/* 214 */         ReflectUtil.ensureMemberAccess(paramClass2, paramClass, null, i);
/*     */ 
/* 216 */         ReflectUtil.checkPackageAccess(paramClass);
/* 217 */         localClass = localField.getType();
/*     */       } catch (Exception localException) {
/* 219 */         throw new RuntimeException(localException);
/*     */       }
/*     */ 
/* 222 */       if (paramClass1 != localClass)
/* 223 */         throw new ClassCastException();
/* 224 */       if (paramClass1.isPrimitive()) {
/* 225 */         throw new IllegalArgumentException("Must be reference type");
/*     */       }
/* 227 */       if (!Modifier.isVolatile(i)) {
/* 228 */         throw new IllegalArgumentException("Must be volatile type");
/*     */       }
/* 230 */       this.cclass = ((Modifier.isProtected(i)) && (paramClass2 != paramClass) ? paramClass2 : null);
/*     */ 
/* 232 */       this.tclass = paramClass;
/* 233 */       if (paramClass1 == Object.class)
/* 234 */         this.vclass = null;
/*     */       else
/* 236 */         this.vclass = paramClass1;
/* 237 */       this.offset = unsafe.objectFieldOffset(localField);
/*     */     }
/*     */ 
/*     */     void targetCheck(T paramT) {
/* 241 */       if (!this.tclass.isInstance(paramT))
/* 242 */         throw new ClassCastException();
/* 243 */       if (this.cclass != null)
/* 244 */         ensureProtectedAccess(paramT);
/*     */     }
/*     */ 
/*     */     void updateCheck(T paramT, V paramV) {
/* 248 */       if ((!this.tclass.isInstance(paramT)) || ((paramV != null) && (this.vclass != null) && (!this.vclass.isInstance(paramV))))
/*     */       {
/* 250 */         throw new ClassCastException();
/* 251 */       }if (this.cclass != null)
/* 252 */         ensureProtectedAccess(paramT);
/*     */     }
/*     */ 
/*     */     public boolean compareAndSet(T paramT, V paramV1, V paramV2) {
/* 256 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null) || ((paramV2 != null) && (this.vclass != null) && (this.vclass != paramV2.getClass())))
/*     */       {
/* 259 */         updateCheck(paramT, paramV2);
/* 260 */       }return unsafe.compareAndSwapObject(paramT, this.offset, paramV1, paramV2);
/*     */     }
/*     */ 
/*     */     public boolean weakCompareAndSet(T paramT, V paramV1, V paramV2)
/*     */     {
/* 265 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null) || ((paramV2 != null) && (this.vclass != null) && (this.vclass != paramV2.getClass())))
/*     */       {
/* 268 */         updateCheck(paramT, paramV2);
/* 269 */       }return unsafe.compareAndSwapObject(paramT, this.offset, paramV1, paramV2);
/*     */     }
/*     */ 
/*     */     public void set(T paramT, V paramV) {
/* 273 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null) || ((paramV != null) && (this.vclass != null) && (this.vclass != paramV.getClass())))
/*     */       {
/* 276 */         updateCheck(paramT, paramV);
/* 277 */       }unsafe.putObjectVolatile(paramT, this.offset, paramV);
/*     */     }
/*     */ 
/*     */     public void lazySet(T paramT, V paramV) {
/* 281 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null) || ((paramV != null) && (this.vclass != null) && (this.vclass != paramV.getClass())))
/*     */       {
/* 284 */         updateCheck(paramT, paramV);
/* 285 */       }unsafe.putOrderedObject(paramT, this.offset, paramV);
/*     */     }
/*     */ 
/*     */     public V get(T paramT) {
/* 289 */       if ((paramT == null) || (paramT.getClass() != this.tclass) || (this.cclass != null))
/* 290 */         targetCheck(paramT);
/* 291 */       return unsafe.getObjectVolatile(paramT, this.offset);
/*     */     }
/*     */ 
/*     */     private void ensureProtectedAccess(T paramT) {
/* 295 */       if (this.cclass.isInstance(paramT)) {
/* 296 */         return;
/*     */       }
/* 298 */       throw new RuntimeException(new IllegalAccessException("Class " + this.cclass.getName() + " can not access a protected member of class " + this.tclass.getName() + " using an instance of " + paramT.getClass().getName()));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.atomic.AtomicReferenceFieldUpdater
 * JD-Core Version:    0.6.2
 */