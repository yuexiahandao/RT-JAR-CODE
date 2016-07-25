/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.ImmutableDescriptor;
/*     */ import javax.management.InvalidAttributeValueException;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ import javax.management.MBeanConstructorInfo;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.MBeanOperationInfo;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ import javax.management.NotificationBroadcaster;
/*     */ import javax.management.ReflectionException;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ abstract class MBeanIntrospector<M>
/*     */ {
/*     */   abstract PerInterfaceMap<M> getPerInterfaceMap();
/*     */ 
/*     */   abstract MBeanInfoMap getMBeanInfoMap();
/*     */ 
/*     */   abstract MBeanAnalyzer<M> getAnalyzer(Class<?> paramClass)
/*     */     throws NotCompliantMBeanException;
/*     */ 
/*     */   abstract boolean isMXBean();
/*     */ 
/*     */   abstract M mFrom(Method paramMethod);
/*     */ 
/*     */   abstract String getName(M paramM);
/*     */ 
/*     */   abstract Type getGenericReturnType(M paramM);
/*     */ 
/*     */   abstract Type[] getGenericParameterTypes(M paramM);
/*     */ 
/*     */   abstract String[] getSignature(M paramM);
/*     */ 
/*     */   abstract void checkMethod(M paramM);
/*     */ 
/*     */   abstract Object invokeM2(M paramM, Object paramObject1, Object[] paramArrayOfObject, Object paramObject2)
/*     */     throws InvocationTargetException, IllegalAccessException, MBeanException;
/*     */ 
/*     */   abstract boolean validParameter(M paramM, Object paramObject1, int paramInt, Object paramObject2);
/*     */ 
/*     */   abstract MBeanAttributeInfo getMBeanAttributeInfo(String paramString, M paramM1, M paramM2);
/*     */ 
/*     */   abstract MBeanOperationInfo getMBeanOperationInfo(String paramString, M paramM);
/*     */ 
/*     */   abstract Descriptor getBasicMBeanDescriptor();
/*     */ 
/*     */   abstract Descriptor getMBeanDescriptor(Class<?> paramClass);
/*     */ 
/*     */   final List<Method> getMethods(Class<?> paramClass)
/*     */   {
/* 180 */     ReflectUtil.checkPackageAccess(paramClass);
/* 181 */     return Arrays.asList(paramClass.getMethods());
/*     */   }
/*     */ 
/*     */   final PerInterface<M> getPerInterface(Class<?> paramClass) throws NotCompliantMBeanException
/*     */   {
/* 186 */     PerInterfaceMap localPerInterfaceMap = getPerInterfaceMap();
/* 187 */     synchronized (localPerInterfaceMap) {
/* 188 */       WeakReference localWeakReference = (WeakReference)localPerInterfaceMap.get(paramClass);
/* 189 */       PerInterface localPerInterface = localWeakReference == null ? null : (PerInterface)localWeakReference.get();
/* 190 */       if (localPerInterface == null) {
/*     */         try {
/* 192 */           MBeanAnalyzer localMBeanAnalyzer = getAnalyzer(paramClass);
/* 193 */           MBeanInfo localMBeanInfo = makeInterfaceMBeanInfo(paramClass, localMBeanAnalyzer);
/*     */ 
/* 195 */           localPerInterface = new PerInterface(paramClass, this, localMBeanAnalyzer, localMBeanInfo);
/*     */ 
/* 197 */           localWeakReference = new WeakReference(localPerInterface);
/* 198 */           localPerInterfaceMap.put(paramClass, localWeakReference);
/*     */         } catch (Exception localException) {
/* 200 */           throw Introspector.throwException(paramClass, localException);
/*     */         }
/*     */       }
/* 203 */       return localPerInterface;
/*     */     }
/*     */   }
/*     */ 
/*     */   private MBeanInfo makeInterfaceMBeanInfo(Class<?> paramClass, MBeanAnalyzer<M> paramMBeanAnalyzer)
/*     */   {
/* 217 */     MBeanInfoMaker localMBeanInfoMaker = new MBeanInfoMaker(null);
/* 218 */     paramMBeanAnalyzer.visit(localMBeanInfoMaker);
/*     */ 
/* 221 */     return localMBeanInfoMaker.makeMBeanInfo(paramClass, "Information on the management interface of the MBean");
/*     */   }
/*     */ 
/*     */   final boolean consistent(M paramM1, M paramM2)
/*     */   {
/* 226 */     return (paramM1 == null) || (paramM2 == null) || (getGenericReturnType(paramM1).equals(getGenericParameterTypes(paramM2)[0]));
/*     */   }
/*     */ 
/*     */   final Object invokeM(M paramM, Object paramObject1, Object[] paramArrayOfObject, Object paramObject2)
/*     */     throws MBeanException, ReflectionException
/*     */   {
/*     */     try
/*     */     {
/* 237 */       return invokeM2(paramM, paramObject1, paramArrayOfObject, paramObject2);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 239 */       unwrapInvocationTargetException(localInvocationTargetException);
/* 240 */       throw new RuntimeException(localInvocationTargetException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 242 */       throw new ReflectionException(localIllegalAccessException, localIllegalAccessException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   final void invokeSetter(String paramString, M paramM, Object paramObject1, Object paramObject2, Object paramObject3)
/*     */     throws MBeanException, ReflectionException, InvalidAttributeValueException
/*     */   {
/*     */     try
/*     */     {
/* 267 */       invokeM2(paramM, paramObject1, new Object[] { paramObject2 }, paramObject3);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 269 */       throw new ReflectionException(localIllegalAccessException, localIllegalAccessException.toString());
/*     */     } catch (RuntimeException localRuntimeException) {
/* 271 */       maybeInvalidParameter(paramString, paramM, paramObject2, paramObject3);
/* 272 */       throw localRuntimeException;
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 274 */       maybeInvalidParameter(paramString, paramM, paramObject2, paramObject3);
/* 275 */       unwrapInvocationTargetException(localInvocationTargetException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void maybeInvalidParameter(String paramString, M paramM, Object paramObject1, Object paramObject2)
/*     */     throws InvalidAttributeValueException
/*     */   {
/* 282 */     if (!validParameter(paramM, paramObject1, 0, paramObject2)) {
/* 283 */       String str = "Invalid value for attribute " + paramString + ": " + paramObject1;
/*     */ 
/* 285 */       throw new InvalidAttributeValueException(str);
/*     */     }
/*     */   }
/*     */ 
/*     */   static boolean isValidParameter(Method paramMethod, Object paramObject, int paramInt) {
/* 290 */     Class localClass = paramMethod.getParameterTypes()[paramInt];
/*     */     try
/*     */     {
/* 295 */       Object localObject = Array.newInstance(localClass, 1);
/* 296 */       Array.set(localObject, 0, paramObject);
/* 297 */       return true; } catch (IllegalArgumentException localIllegalArgumentException) {
/*     */     }
/* 299 */     return false;
/*     */   }
/*     */ 
/*     */   private static void unwrapInvocationTargetException(InvocationTargetException paramInvocationTargetException)
/*     */     throws MBeanException
/*     */   {
/* 306 */     Throwable localThrowable = paramInvocationTargetException.getCause();
/* 307 */     if ((localThrowable instanceof RuntimeException))
/* 308 */       throw ((RuntimeException)localThrowable);
/* 309 */     if ((localThrowable instanceof Error)) {
/* 310 */       throw ((Error)localThrowable);
/*     */     }
/* 312 */     throw new MBeanException((Exception)localThrowable, localThrowable == null ? null : localThrowable.toString());
/*     */   }
/*     */ 
/*     */   final MBeanInfo getMBeanInfo(Object paramObject, PerInterface<M> paramPerInterface)
/*     */   {
/* 391 */     MBeanInfo localMBeanInfo = getClassMBeanInfo(paramObject.getClass(), paramPerInterface);
/*     */ 
/* 393 */     MBeanNotificationInfo[] arrayOfMBeanNotificationInfo = findNotifications(paramObject);
/* 394 */     if ((arrayOfMBeanNotificationInfo == null) || (arrayOfMBeanNotificationInfo.length == 0)) {
/* 395 */       return localMBeanInfo;
/*     */     }
/* 397 */     return new MBeanInfo(localMBeanInfo.getClassName(), localMBeanInfo.getDescription(), localMBeanInfo.getAttributes(), localMBeanInfo.getConstructors(), localMBeanInfo.getOperations(), arrayOfMBeanNotificationInfo, localMBeanInfo.getDescriptor());
/*     */   }
/*     */ 
/*     */   final MBeanInfo getClassMBeanInfo(Class<?> paramClass, PerInterface<M> paramPerInterface)
/*     */   {
/* 416 */     MBeanInfoMap localMBeanInfoMap = getMBeanInfoMap();
/* 417 */     synchronized (localMBeanInfoMap) {
/* 418 */       WeakHashMap localWeakHashMap = (WeakHashMap)localMBeanInfoMap.get(paramClass);
/* 419 */       if (localWeakHashMap == null) {
/* 420 */         localWeakHashMap = new WeakHashMap();
/* 421 */         localMBeanInfoMap.put(paramClass, localWeakHashMap);
/*     */       }
/* 423 */       Class localClass = paramPerInterface.getMBeanInterface();
/* 424 */       MBeanInfo localMBeanInfo1 = (MBeanInfo)localWeakHashMap.get(localClass);
/* 425 */       if (localMBeanInfo1 == null) {
/* 426 */         MBeanInfo localMBeanInfo2 = paramPerInterface.getMBeanInfo();
/* 427 */         ImmutableDescriptor localImmutableDescriptor = ImmutableDescriptor.union(new Descriptor[] { localMBeanInfo2.getDescriptor(), getMBeanDescriptor(paramClass) });
/*     */ 
/* 430 */         localMBeanInfo1 = new MBeanInfo(paramClass.getName(), localMBeanInfo2.getDescription(), localMBeanInfo2.getAttributes(), findConstructors(paramClass), localMBeanInfo2.getOperations(), (MBeanNotificationInfo[])null, localImmutableDescriptor);
/*     */ 
/* 437 */         localWeakHashMap.put(localClass, localMBeanInfo1);
/*     */       }
/* 439 */       return localMBeanInfo1;
/*     */     }
/*     */   }
/*     */ 
/*     */   static MBeanNotificationInfo[] findNotifications(Object paramObject) {
/* 444 */     if (!(paramObject instanceof NotificationBroadcaster))
/* 445 */       return null;
/* 446 */     MBeanNotificationInfo[] arrayOfMBeanNotificationInfo1 = ((NotificationBroadcaster)paramObject).getNotificationInfo();
/*     */ 
/* 448 */     if (arrayOfMBeanNotificationInfo1 == null)
/* 449 */       return null;
/* 450 */     MBeanNotificationInfo[] arrayOfMBeanNotificationInfo2 = new MBeanNotificationInfo[arrayOfMBeanNotificationInfo1.length];
/*     */ 
/* 452 */     for (int i = 0; i < arrayOfMBeanNotificationInfo1.length; i++) {
/* 453 */       MBeanNotificationInfo localMBeanNotificationInfo = arrayOfMBeanNotificationInfo1[i];
/* 454 */       if (localMBeanNotificationInfo.getClass() != MBeanNotificationInfo.class)
/* 455 */         localMBeanNotificationInfo = (MBeanNotificationInfo)localMBeanNotificationInfo.clone();
/* 456 */       arrayOfMBeanNotificationInfo2[i] = localMBeanNotificationInfo;
/*     */     }
/* 458 */     return arrayOfMBeanNotificationInfo2;
/*     */   }
/*     */ 
/*     */   private static MBeanConstructorInfo[] findConstructors(Class<?> paramClass) {
/* 462 */     Constructor[] arrayOfConstructor = paramClass.getConstructors();
/* 463 */     MBeanConstructorInfo[] arrayOfMBeanConstructorInfo = new MBeanConstructorInfo[arrayOfConstructor.length];
/* 464 */     for (int i = 0; i < arrayOfConstructor.length; i++)
/*     */     {
/* 466 */       arrayOfMBeanConstructorInfo[i] = new MBeanConstructorInfo("Public constructor of the MBean", arrayOfConstructor[i]);
/*     */     }
/* 468 */     return arrayOfMBeanConstructorInfo;
/*     */   }
/*     */ 
/*     */   private class MBeanInfoMaker
/*     */     implements MBeanAnalyzer.MBeanVisitor<M>
/*     */   {
/* 367 */     private final List<MBeanAttributeInfo> attrs = Util.newList();
/* 368 */     private final List<MBeanOperationInfo> ops = Util.newList();
/*     */ 
/*     */     private MBeanInfoMaker()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void visitAttribute(String paramString, M paramM1, M paramM2)
/*     */     {
/* 323 */       MBeanAttributeInfo localMBeanAttributeInfo = MBeanIntrospector.this.getMBeanAttributeInfo(paramString, paramM1, paramM2);
/*     */ 
/* 326 */       this.attrs.add(localMBeanAttributeInfo);
/*     */     }
/*     */ 
/*     */     public void visitOperation(String paramString, M paramM)
/*     */     {
/* 331 */       MBeanOperationInfo localMBeanOperationInfo = MBeanIntrospector.this.getMBeanOperationInfo(paramString, paramM);
/*     */ 
/* 334 */       this.ops.add(localMBeanOperationInfo);
/*     */     }
/*     */ 
/*     */     MBeanInfo makeMBeanInfo(Class<?> paramClass, String paramString)
/*     */     {
/* 341 */       MBeanAttributeInfo[] arrayOfMBeanAttributeInfo = (MBeanAttributeInfo[])this.attrs.toArray(new MBeanAttributeInfo[0]);
/*     */ 
/* 343 */       MBeanOperationInfo[] arrayOfMBeanOperationInfo = (MBeanOperationInfo[])this.ops.toArray(new MBeanOperationInfo[0]);
/*     */ 
/* 345 */       String str = "interfaceClassName=" + paramClass.getName();
/*     */ 
/* 347 */       ImmutableDescriptor localImmutableDescriptor1 = new ImmutableDescriptor(new String[] { str });
/*     */ 
/* 349 */       Descriptor localDescriptor1 = MBeanIntrospector.this.getBasicMBeanDescriptor();
/* 350 */       Descriptor localDescriptor2 = Introspector.descriptorForElement(paramClass);
/*     */ 
/* 352 */       ImmutableDescriptor localImmutableDescriptor2 = DescriptorCache.getInstance().union(new Descriptor[] { localImmutableDescriptor1, localDescriptor1, localDescriptor2 });
/*     */ 
/* 358 */       return new MBeanInfo(paramClass.getName(), paramString, arrayOfMBeanAttributeInfo, null, arrayOfMBeanOperationInfo, null, localImmutableDescriptor2);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class MBeanInfoMap extends WeakHashMap<Class<?>, WeakHashMap<Class<?>, MBeanInfo>>
/*     */   {
/*     */   }
/*     */ 
/*     */   static final class PerInterfaceMap<M> extends WeakHashMap<Class<?>, WeakReference<PerInterface<M>>>
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.MBeanIntrospector
 * JD-Core Version:    0.6.2
 */