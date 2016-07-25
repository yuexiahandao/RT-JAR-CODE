/*     */ package sun.corba;
/*     */ 
/*     */ import java.io.ObjectInputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import java.security.PrivilegedAction;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.reflect.ReflectionFactory;
/*     */ import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;
/*     */ 
/*     */ public final class Bridge
/*     */ {
/*  74 */   private static final Class[] NO_ARGS = new Class[0];
/*  75 */   private static final Permission getBridgePermission = new BridgePermission("getBridge");
/*     */ 
/*  77 */   private static Bridge bridge = null;
/*     */   private final Method latestUserDefinedLoaderMethod;
/*     */   private final Unsafe unsafe;
/*     */   private final ReflectionFactory reflectionFactory;
/*     */   public static final long INVALID_FIELD_OFFSET = -1L;
/*     */ 
/*     */   private Method getLatestUserDefinedLoaderMethod()
/*     */   {
/*  89 */     return (Method)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*  94 */         Method localMethod = null;
/*     */         try
/*     */         {
/*  97 */           ObjectInputStream localObjectInputStream = ObjectInputStream.class;
/*  98 */           localMethod = localObjectInputStream.getDeclaredMethod("latestUserDefinedLoader", Bridge.NO_ARGS);
/*     */ 
/* 100 */           localMethod.setAccessible(true);
/*     */         } catch (NoSuchMethodException localNoSuchMethodException) {
/* 102 */           Error localError = new Error("java.io.ObjectInputStream latestUserDefinedLoader " + localNoSuchMethodException);
/*     */ 
/* 104 */           localError.initCause(localNoSuchMethodException);
/* 105 */           throw localError;
/*     */         }
/*     */ 
/* 108 */         return localMethod;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private Unsafe getUnsafe()
/*     */   {
/* 115 */     Field localField = (Field)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/* 120 */         Field localField = null;
/*     */         try
/*     */         {
/* 123 */           Unsafe localUnsafe = Unsafe.class;
/* 124 */           localField = localUnsafe.getDeclaredField("theUnsafe");
/* 125 */           localField.setAccessible(true);
/* 126 */           return localField;
/*     */         } catch (NoSuchFieldException localNoSuchFieldException) {
/* 128 */           Error localError = new Error("Could not access Unsafe");
/* 129 */           localError.initCause(localNoSuchFieldException);
/* 130 */           throw localError;
/*     */         }
/*     */       }
/*     */     });
/* 136 */     Unsafe localUnsafe = null;
/*     */     try
/*     */     {
/* 139 */       localUnsafe = (Unsafe)localField.get(null);
/*     */     } catch (Throwable localThrowable) {
/* 141 */       Error localError = new Error("Could not access Unsafe");
/* 142 */       localError.initCause(localThrowable);
/* 143 */       throw localError;
/*     */     }
/*     */ 
/* 146 */     return localUnsafe;
/*     */   }
/*     */ 
/*     */   private Bridge()
/*     */   {
/* 152 */     this.latestUserDefinedLoaderMethod = getLatestUserDefinedLoaderMethod();
/* 153 */     this.unsafe = getUnsafe();
/* 154 */     this.reflectionFactory = ((ReflectionFactory)AccessController.doPrivileged(new ReflectionFactory.GetReflectionFactoryAction()));
/*     */   }
/*     */ 
/*     */   public static final synchronized Bridge get()
/*     */   {
/* 171 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 172 */     if (localSecurityManager != null) {
/* 173 */       localSecurityManager.checkPermission(getBridgePermission);
/*     */     }
/* 175 */     if (bridge == null) {
/* 176 */       bridge = new Bridge();
/*     */     }
/*     */ 
/* 179 */     return bridge;
/*     */   }
/*     */ 
/*     */   public final ClassLoader getLatestUserDefinedLoader()
/*     */   {
/*     */     Error localError;
/*     */     try
/*     */     {
/* 189 */       return (ClassLoader)this.latestUserDefinedLoaderMethod.invoke(null, (Object[])NO_ARGS);
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException) {
/* 192 */       localError = new Error("sun.corba.Bridge.latestUserDefinedLoader: " + localInvocationTargetException);
/*     */ 
/* 194 */       localError.initCause(localInvocationTargetException);
/* 195 */       throw localError;
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 197 */       localError = new Error("sun.corba.Bridge.latestUserDefinedLoader: " + localIllegalAccessException);
/*     */ 
/* 199 */       localError.initCause(localIllegalAccessException);
/* 200 */     }throw localError;
/*     */   }
/*     */ 
/*     */   public final int getInt(Object paramObject, long paramLong)
/*     */   {
/* 221 */     return this.unsafe.getInt(paramObject, paramLong);
/*     */   }
/*     */ 
/*     */   public final void putInt(Object paramObject, long paramLong, int paramInt)
/*     */   {
/* 244 */     this.unsafe.putInt(paramObject, paramLong, paramInt);
/*     */   }
/*     */ 
/*     */   public final Object getObject(Object paramObject, long paramLong)
/*     */   {
/* 252 */     return this.unsafe.getObject(paramObject, paramLong);
/*     */   }
/*     */ 
/*     */   public final void putObject(Object paramObject1, long paramLong, Object paramObject2)
/*     */   {
/* 260 */     this.unsafe.putObject(paramObject1, paramLong, paramObject2);
/*     */   }
/*     */ 
/*     */   public final boolean getBoolean(Object paramObject, long paramLong)
/*     */   {
/* 266 */     return this.unsafe.getBoolean(paramObject, paramLong);
/*     */   }
/*     */ 
/*     */   public final void putBoolean(Object paramObject, long paramLong, boolean paramBoolean)
/*     */   {
/* 271 */     this.unsafe.putBoolean(paramObject, paramLong, paramBoolean);
/*     */   }
/*     */ 
/*     */   public final byte getByte(Object paramObject, long paramLong)
/*     */   {
/* 276 */     return this.unsafe.getByte(paramObject, paramLong);
/*     */   }
/*     */ 
/*     */   public final void putByte(Object paramObject, long paramLong, byte paramByte)
/*     */   {
/* 281 */     this.unsafe.putByte(paramObject, paramLong, paramByte);
/*     */   }
/*     */ 
/*     */   public final short getShort(Object paramObject, long paramLong)
/*     */   {
/* 286 */     return this.unsafe.getShort(paramObject, paramLong);
/*     */   }
/*     */ 
/*     */   public final void putShort(Object paramObject, long paramLong, short paramShort)
/*     */   {
/* 291 */     this.unsafe.putShort(paramObject, paramLong, paramShort);
/*     */   }
/*     */ 
/*     */   public final char getChar(Object paramObject, long paramLong)
/*     */   {
/* 296 */     return this.unsafe.getChar(paramObject, paramLong);
/*     */   }
/*     */ 
/*     */   public final void putChar(Object paramObject, long paramLong, char paramChar)
/*     */   {
/* 301 */     this.unsafe.putChar(paramObject, paramLong, paramChar);
/*     */   }
/*     */ 
/*     */   public final long getLong(Object paramObject, long paramLong)
/*     */   {
/* 306 */     return this.unsafe.getLong(paramObject, paramLong);
/*     */   }
/*     */ 
/*     */   public final void putLong(Object paramObject, long paramLong1, long paramLong2)
/*     */   {
/* 311 */     this.unsafe.putLong(paramObject, paramLong1, paramLong2);
/*     */   }
/*     */ 
/*     */   public final float getFloat(Object paramObject, long paramLong)
/*     */   {
/* 316 */     return this.unsafe.getFloat(paramObject, paramLong);
/*     */   }
/*     */ 
/*     */   public final void putFloat(Object paramObject, long paramLong, float paramFloat)
/*     */   {
/* 321 */     this.unsafe.putFloat(paramObject, paramLong, paramFloat);
/*     */   }
/*     */ 
/*     */   public final double getDouble(Object paramObject, long paramLong)
/*     */   {
/* 326 */     return this.unsafe.getDouble(paramObject, paramLong);
/*     */   }
/*     */ 
/*     */   public final void putDouble(Object paramObject, long paramLong, double paramDouble)
/*     */   {
/* 331 */     this.unsafe.putDouble(paramObject, paramLong, paramDouble);
/*     */   }
/*     */ 
/*     */   public final long objectFieldOffset(Field paramField)
/*     */   {
/* 345 */     return this.unsafe.objectFieldOffset(paramField);
/*     */   }
/*     */ 
/*     */   public final void throwException(Throwable paramThrowable)
/*     */   {
/* 353 */     this.unsafe.throwException(paramThrowable);
/*     */   }
/*     */ 
/*     */   public final Constructor newConstructorForSerialization(Class paramClass, Constructor paramConstructor)
/*     */   {
/* 366 */     return this.reflectionFactory.newConstructorForSerialization(paramClass, paramConstructor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.corba.Bridge
 * JD-Core Version:    0.6.2
 */