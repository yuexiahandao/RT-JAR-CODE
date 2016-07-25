/*     */ package java.lang.invoke;
/*     */ 
/*     */ import sun.invoke.empty.Empty;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public abstract class CallSite
/*     */ {
/*     */   MethodHandle target;
/*     */   private static final MethodHandle GET_TARGET;
/*     */   private static final long TARGET_OFFSET;
/*     */ 
/*     */   CallSite(MethodType paramMethodType)
/*     */   {
/* 105 */     this.target = paramMethodType.invokers().uninitializedCallSite();
/*     */   }
/*     */ 
/*     */   CallSite(MethodHandle paramMethodHandle)
/*     */   {
/* 115 */     paramMethodHandle.type();
/* 116 */     this.target = paramMethodHandle;
/*     */   }
/*     */ 
/*     */   CallSite(MethodType paramMethodType, MethodHandle paramMethodHandle)
/*     */     throws Throwable
/*     */   {
/* 131 */     this(paramMethodType);
/* 132 */     ConstantCallSite localConstantCallSite = (ConstantCallSite)this;
/* 133 */     MethodHandle localMethodHandle = (MethodHandle)paramMethodHandle.invokeWithArguments(new Object[] { localConstantCallSite });
/* 134 */     checkTargetChange(this.target, localMethodHandle);
/* 135 */     this.target = localMethodHandle;
/*     */   }
/*     */ 
/*     */   public MethodType type()
/*     */   {
/* 147 */     return this.target.type();
/*     */   }
/*     */ 
/*     */   public abstract MethodHandle getTarget();
/*     */ 
/*     */   public abstract void setTarget(MethodHandle paramMethodHandle);
/*     */ 
/*     */   void checkTargetChange(MethodHandle paramMethodHandle1, MethodHandle paramMethodHandle2)
/*     */   {
/* 187 */     MethodType localMethodType1 = paramMethodHandle1.type();
/* 188 */     MethodType localMethodType2 = paramMethodHandle2.type();
/* 189 */     if (!localMethodType2.equals(localMethodType1))
/* 190 */       throw wrongTargetType(paramMethodHandle2, localMethodType1);
/*     */   }
/*     */ 
/*     */   private static WrongMethodTypeException wrongTargetType(MethodHandle paramMethodHandle, MethodType paramMethodType) {
/* 194 */     return new WrongMethodTypeException(String.valueOf(paramMethodHandle) + " should be of type " + paramMethodType);
/*     */   }
/*     */ 
/*     */   public abstract MethodHandle dynamicInvoker();
/*     */ 
/*     */   MethodHandle makeDynamicInvoker()
/*     */   {
/* 214 */     MethodHandle localMethodHandle1 = GET_TARGET.bindReceiver(this);
/* 215 */     MethodHandle localMethodHandle2 = MethodHandles.exactInvoker(type());
/* 216 */     return MethodHandles.foldArguments(localMethodHandle2, localMethodHandle1);
/*     */   }
/*     */ 
/*     */   static Empty uninitializedCallSite()
/*     */   {
/* 232 */     throw new IllegalStateException("uninitialized call site");
/*     */   }
/*     */ 
/*     */   void setTargetNormal(MethodHandle paramMethodHandle)
/*     */   {
/* 245 */     MethodHandleNatives.setCallSiteTargetNormal(this, paramMethodHandle);
/*     */   }
/*     */ 
/*     */   MethodHandle getTargetVolatile() {
/* 249 */     return (MethodHandle)MethodHandleStatics.UNSAFE.getObjectVolatile(this, TARGET_OFFSET);
/*     */   }
/*     */ 
/*     */   void setTargetVolatile(MethodHandle paramMethodHandle) {
/* 253 */     MethodHandleNatives.setCallSiteTargetVolatile(this, paramMethodHandle);
/*     */   }
/*     */ 
/*     */   static CallSite makeSite(MethodHandle paramMethodHandle, String paramString, MethodType paramMethodType, Object paramObject, Class<?> paramClass)
/*     */   {
/* 264 */     MethodHandles.Lookup localLookup = MethodHandles.Lookup.IMPL_LOOKUP.in(paramClass);
/*     */     CallSite localCallSite;
/*     */     try
/*     */     {
/* 268 */       paramObject = maybeReBox(paramObject);
/*     */       Object localObject1;
/* 269 */       if (paramObject == null) {
/* 270 */         localObject1 = paramMethodHandle.invoke(localLookup, paramString, paramMethodType);
/* 271 */       } else if (!paramObject.getClass().isArray()) {
/* 272 */         localObject1 = paramMethodHandle.invoke(localLookup, paramString, paramMethodType, paramObject);
/*     */       } else {
/* 274 */         localObject2 = (Object[])paramObject;
/* 275 */         maybeReBoxElements((Object[])localObject2);
/* 276 */         if (3 + localObject2.length > 255)
/* 277 */           throw new BootstrapMethodError("too many bootstrap method arguments");
/* 278 */         MethodType localMethodType = paramMethodHandle.type();
/* 279 */         if ((localMethodType.parameterCount() == 4) && (localMethodType.parameterType(3) == [Ljava.lang.Object.class))
/* 280 */           localObject1 = paramMethodHandle.invoke(localLookup, paramString, paramMethodType, (Object[])localObject2);
/*     */         else {
/* 282 */           localObject1 = MethodHandles.spreadInvoker(localMethodType, 3).invoke(paramMethodHandle, localLookup, paramString, paramMethodType, (Object[])localObject2);
/*     */         }
/*     */       }
/*     */ 
/* 286 */       if ((localObject1 instanceof CallSite))
/* 287 */         localCallSite = (CallSite)localObject1;
/*     */       else {
/* 289 */         throw new ClassCastException("bootstrap method failed to produce a CallSite");
/*     */       }
/* 291 */       if (!localCallSite.getTarget().type().equals(paramMethodType))
/* 292 */         throw new WrongMethodTypeException("wrong type: " + localCallSite.getTarget());
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/*     */       Object localObject2;
/* 295 */       if ((localThrowable instanceof BootstrapMethodError))
/* 296 */         localObject2 = (BootstrapMethodError)localThrowable;
/*     */       else
/* 298 */         localObject2 = new BootstrapMethodError("call site initialization exception", localThrowable);
/* 299 */       throw ((Throwable)localObject2);
/*     */     }
/* 301 */     return localCallSite;
/*     */   }
/*     */ 
/*     */   private static Object maybeReBox(Object paramObject) {
/* 305 */     if ((paramObject instanceof Integer)) {
/* 306 */       int i = ((Integer)paramObject).intValue();
/* 307 */       if (i == (byte)i)
/* 308 */         paramObject = Integer.valueOf(i);
/*     */     }
/* 310 */     return paramObject;
/*     */   }
/*     */   private static void maybeReBoxElements(Object[] paramArrayOfObject) {
/* 313 */     for (int i = 0; i < paramArrayOfObject.length; i++)
/* 314 */       paramArrayOfObject[i] = maybeReBox(paramArrayOfObject[i]);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  87 */     MethodHandleImpl.initStatics();
/*     */     try
/*     */     {
/* 222 */       GET_TARGET = MethodHandles.Lookup.IMPL_LOOKUP.findVirtual(CallSite.class, "getTarget", MethodType.methodType(MethodHandle.class));
/*     */     }
/*     */     catch (ReflectiveOperationException localReflectiveOperationException) {
/* 225 */       throw MethodHandleStatics.newInternalError(localReflectiveOperationException);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 239 */       TARGET_OFFSET = MethodHandleStatics.UNSAFE.objectFieldOffset(CallSite.class.getDeclaredField("target")); } catch (Exception localException) {
/* 240 */       throw new Error(localException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.CallSite
 * JD-Core Version:    0.6.2
 */