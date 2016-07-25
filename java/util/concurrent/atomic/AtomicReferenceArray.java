/*     */ package java.util.concurrent.atomic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class AtomicReferenceArray<E>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6209656149925076980L;
/*     */   private static final Unsafe unsafe;
/*     */   private static final int base;
/*  73 */   private static final int shift = 31 - Integer.numberOfLeadingZeros(i);
/*     */   private static final long arrayFieldOffset;
/*     */   private final Object[] array;
/*     */ 
/*     */   private long checkedByteOffset(int paramInt)
/*     */   {
/*  77 */     if ((paramInt < 0) || (paramInt >= this.array.length)) {
/*  78 */       throw new IndexOutOfBoundsException("index " + paramInt);
/*     */     }
/*  80 */     return byteOffset(paramInt);
/*     */   }
/*     */ 
/*     */   private static long byteOffset(int paramInt) {
/*  84 */     return (paramInt << shift) + base;
/*     */   }
/*     */ 
/*     */   public AtomicReferenceArray(int paramInt)
/*     */   {
/*  94 */     this.array = new Object[paramInt];
/*     */   }
/*     */ 
/*     */   public AtomicReferenceArray(E[] paramArrayOfE)
/*     */   {
/* 106 */     this.array = Arrays.copyOf(paramArrayOfE, paramArrayOfE.length, [Ljava.lang.Object.class);
/*     */   }
/*     */ 
/*     */   public final int length()
/*     */   {
/* 115 */     return this.array.length;
/*     */   }
/*     */ 
/*     */   public final E get(int paramInt)
/*     */   {
/* 125 */     return getRaw(checkedByteOffset(paramInt));
/*     */   }
/*     */ 
/*     */   private E getRaw(long paramLong) {
/* 129 */     return unsafe.getObjectVolatile(this.array, paramLong);
/*     */   }
/*     */ 
/*     */   public final void set(int paramInt, E paramE)
/*     */   {
/* 139 */     unsafe.putObjectVolatile(this.array, checkedByteOffset(paramInt), paramE);
/*     */   }
/*     */ 
/*     */   public final void lazySet(int paramInt, E paramE)
/*     */   {
/* 150 */     unsafe.putOrderedObject(this.array, checkedByteOffset(paramInt), paramE);
/*     */   }
/*     */ 
/*     */   public final E getAndSet(int paramInt, E paramE)
/*     */   {
/* 163 */     long l = checkedByteOffset(paramInt);
/*     */     while (true) {
/* 165 */       Object localObject = getRaw(l);
/* 166 */       if (compareAndSetRaw(l, localObject, paramE))
/* 167 */         return localObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean compareAndSet(int paramInt, E paramE1, E paramE2)
/*     */   {
/* 182 */     return compareAndSetRaw(checkedByteOffset(paramInt), paramE1, paramE2);
/*     */   }
/*     */ 
/*     */   private boolean compareAndSetRaw(long paramLong, E paramE1, E paramE2) {
/* 186 */     return unsafe.compareAndSwapObject(this.array, paramLong, paramE1, paramE2);
/*     */   }
/*     */ 
/*     */   public final boolean weakCompareAndSet(int paramInt, E paramE1, E paramE2)
/*     */   {
/* 203 */     return compareAndSet(paramInt, paramE1, paramE2);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 211 */     int i = this.array.length - 1;
/* 212 */     if (i == -1) {
/* 213 */       return "[]";
/*     */     }
/* 215 */     StringBuilder localStringBuilder = new StringBuilder();
/* 216 */     localStringBuilder.append('[');
/* 217 */     for (int j = 0; ; j++) {
/* 218 */       localStringBuilder.append(getRaw(byteOffset(j)));
/* 219 */       if (j == i)
/* 220 */         return ']';
/* 221 */       localStringBuilder.append(',').append(' ');
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 232 */     Object localObject = paramObjectInputStream.readFields().get("array", null);
/* 233 */     if ((localObject == null) || (!localObject.getClass().isArray()))
/* 234 */       throw new InvalidObjectException("Not array type");
/* 235 */     if (localObject.getClass() != [Ljava.lang.Object.class)
/* 236 */       localObject = Arrays.copyOf((Object[])localObject, Array.getLength(localObject), [Ljava.lang.Object.class);
/* 237 */     unsafe.putObjectVolatile(this, arrayFieldOffset, localObject);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     int i;
/*     */     try
/*     */     {
/*  63 */       unsafe = Unsafe.getUnsafe();
/*  64 */       arrayFieldOffset = unsafe.objectFieldOffset(AtomicReferenceArray.class.getDeclaredField("array"));
/*     */ 
/*  66 */       base = unsafe.arrayBaseOffset([Ljava.lang.Object.class);
/*  67 */       i = unsafe.arrayIndexScale([Ljava.lang.Object.class);
/*     */     } catch (Exception localException) {
/*  69 */       throw new Error(localException);
/*     */     }
/*  71 */     if ((i & i - 1) != 0)
/*  72 */       throw new Error("data type scale not a power of two");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.atomic.AtomicReferenceArray
 * JD-Core Version:    0.6.2
 */