/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.ImmutableDescriptor;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanOperationInfo;
/*     */ import javax.management.MBeanParameterInfo;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
/*     */ import javax.management.openmbean.OpenMBeanOperationInfoSupport;
/*     */ import javax.management.openmbean.OpenMBeanParameterInfo;
/*     */ import javax.management.openmbean.OpenMBeanParameterInfoSupport;
/*     */ import javax.management.openmbean.OpenType;
/*     */ 
/*     */ class MXBeanIntrospector extends MBeanIntrospector<ConvertingMethod>
/*     */ {
/*  55 */   private static final MXBeanIntrospector instance = new MXBeanIntrospector();
/*     */ 
/* 361 */   private final MBeanIntrospector.PerInterfaceMap<ConvertingMethod> perInterfaceMap = new MBeanIntrospector.PerInterfaceMap();
/*     */ 
/* 364 */   private static final MBeanIntrospector.MBeanInfoMap mbeanInfoMap = new MBeanIntrospector.MBeanInfoMap();
/*     */ 
/*     */   static MXBeanIntrospector getInstance()
/*     */   {
/*  58 */     return instance;
/*     */   }
/*     */ 
/*     */   MBeanIntrospector.PerInterfaceMap<ConvertingMethod> getPerInterfaceMap()
/*     */   {
/*  63 */     return this.perInterfaceMap;
/*     */   }
/*     */ 
/*     */   MBeanIntrospector.MBeanInfoMap getMBeanInfoMap()
/*     */   {
/*  68 */     return mbeanInfoMap;
/*     */   }
/*     */ 
/*     */   MBeanAnalyzer<ConvertingMethod> getAnalyzer(Class<?> paramClass)
/*     */     throws NotCompliantMBeanException
/*     */   {
/*  74 */     return MBeanAnalyzer.analyzer(paramClass, this);
/*     */   }
/*     */ 
/*     */   boolean isMXBean()
/*     */   {
/*  79 */     return true;
/*     */   }
/*     */ 
/*     */   ConvertingMethod mFrom(Method paramMethod)
/*     */   {
/*  84 */     return ConvertingMethod.from(paramMethod);
/*     */   }
/*     */ 
/*     */   String getName(ConvertingMethod paramConvertingMethod)
/*     */   {
/*  89 */     return paramConvertingMethod.getName();
/*     */   }
/*     */ 
/*     */   Type getGenericReturnType(ConvertingMethod paramConvertingMethod)
/*     */   {
/*  94 */     return paramConvertingMethod.getGenericReturnType();
/*     */   }
/*     */ 
/*     */   Type[] getGenericParameterTypes(ConvertingMethod paramConvertingMethod)
/*     */   {
/*  99 */     return paramConvertingMethod.getGenericParameterTypes();
/*     */   }
/*     */ 
/*     */   String[] getSignature(ConvertingMethod paramConvertingMethod)
/*     */   {
/* 104 */     return paramConvertingMethod.getOpenSignature();
/*     */   }
/*     */ 
/*     */   void checkMethod(ConvertingMethod paramConvertingMethod)
/*     */   {
/* 109 */     paramConvertingMethod.checkCallFromOpen();
/*     */   }
/*     */ 
/*     */   Object invokeM2(ConvertingMethod paramConvertingMethod, Object paramObject1, Object[] paramArrayOfObject, Object paramObject2)
/*     */     throws InvocationTargetException, IllegalAccessException, MBeanException
/*     */   {
/* 117 */     return paramConvertingMethod.invokeWithOpenReturn((MXBeanLookup)paramObject2, paramObject1, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   boolean validParameter(ConvertingMethod paramConvertingMethod, Object paramObject1, int paramInt, Object paramObject2)
/*     */   {
/*     */     Object localObject;
/* 123 */     if (paramObject1 == null)
/*     */     {
/* 128 */       localObject = paramConvertingMethod.getGenericParameterTypes()[paramInt];
/* 129 */       return (!(localObject instanceof Class)) || (!((Class)localObject).isPrimitive());
/*     */     }
/*     */     try
/*     */     {
/* 133 */       localObject = paramConvertingMethod.fromOpenParameter((MXBeanLookup)paramObject2, paramObject1, paramInt);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 137 */       return true;
/*     */     }
/* 139 */     return isValidParameter(paramConvertingMethod.getMethod(), localObject, paramInt);
/*     */   }
/*     */ 
/*     */   MBeanAttributeInfo getMBeanAttributeInfo(String paramString, ConvertingMethod paramConvertingMethod1, ConvertingMethod paramConvertingMethod2)
/*     */   {
/* 147 */     boolean bool1 = paramConvertingMethod1 != null;
/* 148 */     boolean bool2 = paramConvertingMethod2 != null;
/* 149 */     boolean bool3 = (bool1) && (getName(paramConvertingMethod1).startsWith("is"));
/*     */ 
/* 151 */     String str = paramString;
/*     */     OpenType localOpenType;
/*     */     Type localType;
/* 155 */     if (bool1) {
/* 156 */       localOpenType = paramConvertingMethod1.getOpenReturnType();
/* 157 */       localType = paramConvertingMethod1.getGenericReturnType();
/*     */     } else {
/* 159 */       localOpenType = paramConvertingMethod2.getOpenParameterTypes()[0];
/* 160 */       localType = paramConvertingMethod2.getGenericParameterTypes()[0];
/*     */     }
/* 162 */     Object localObject1 = typeDescriptor(localOpenType, localType);
/* 163 */     if (bool1) {
/* 164 */       localObject1 = ImmutableDescriptor.union(new Descriptor[] { localObject1, paramConvertingMethod1.getDescriptor() });
/*     */     }
/*     */ 
/* 167 */     if (bool2)
/* 168 */       localObject1 = ImmutableDescriptor.union(new Descriptor[] { localObject1, paramConvertingMethod2.getDescriptor() });
/*     */     Object localObject2;
/* 173 */     if (canUseOpenInfo(localType)) {
/* 174 */       localObject2 = new OpenMBeanAttributeInfoSupport(paramString, str, localOpenType, bool1, bool2, bool3, (Descriptor)localObject1);
/*     */     }
/*     */     else
/*     */     {
/* 182 */       localObject2 = new MBeanAttributeInfo(paramString, originalTypeString(localType), str, bool1, bool2, bool3, (Descriptor)localObject1);
/*     */     }
/*     */ 
/* 193 */     return localObject2;
/*     */   }
/*     */ 
/*     */   MBeanOperationInfo getMBeanOperationInfo(String paramString, ConvertingMethod paramConvertingMethod)
/*     */   {
/* 199 */     Method localMethod = paramConvertingMethod.getMethod();
/* 200 */     String str = paramString;
/*     */ 
/* 208 */     OpenType localOpenType1 = paramConvertingMethod.getOpenReturnType();
/* 209 */     Type localType1 = paramConvertingMethod.getGenericReturnType();
/* 210 */     OpenType[] arrayOfOpenType = paramConvertingMethod.getOpenParameterTypes();
/* 211 */     Type[] arrayOfType = paramConvertingMethod.getGenericParameterTypes();
/* 212 */     MBeanParameterInfo[] arrayOfMBeanParameterInfo = new MBeanParameterInfo[arrayOfOpenType.length];
/*     */ 
/* 214 */     boolean bool = canUseOpenInfo(localType1);
/* 215 */     int i = 1;
/* 216 */     Annotation[][] arrayOfAnnotation = localMethod.getParameterAnnotations();
/*     */     Object localObject2;
/*     */     Object localObject3;
/* 217 */     for (int j = 0; j < arrayOfOpenType.length; j++) {
/* 218 */       localObject2 = "p" + j;
/* 219 */       localObject3 = localObject2;
/* 220 */       OpenType localOpenType2 = arrayOfOpenType[j];
/* 221 */       Type localType2 = arrayOfType[j];
/* 222 */       Object localObject4 = typeDescriptor(localOpenType2, localType2);
/*     */ 
/* 224 */       localObject4 = ImmutableDescriptor.union(new Descriptor[] { localObject4, Introspector.descriptorForAnnotations(arrayOfAnnotation[j]) });
/*     */       Object localObject5;
/* 227 */       if (canUseOpenInfo(localType2)) {
/* 228 */         localObject5 = new OpenMBeanParameterInfoSupport((String)localObject2, (String)localObject3, localOpenType2, (Descriptor)localObject4);
/*     */       }
/*     */       else
/*     */       {
/* 233 */         i = 0;
/* 234 */         localObject5 = new MBeanParameterInfo((String)localObject2, originalTypeString(localType2), (String)localObject3, (Descriptor)localObject4);
/*     */       }
/*     */ 
/* 240 */       arrayOfMBeanParameterInfo[j] = localObject5;
/*     */     }
/*     */ 
/* 243 */     Object localObject1 = typeDescriptor(localOpenType1, localType1);
/*     */ 
/* 245 */     localObject1 = ImmutableDescriptor.union(new Descriptor[] { localObject1, Introspector.descriptorForElement(localMethod) });
/*     */ 
/* 248 */     if ((bool) && (i != 0))
/*     */     {
/* 257 */       localObject3 = new OpenMBeanParameterInfo[arrayOfMBeanParameterInfo.length];
/*     */ 
/* 259 */       System.arraycopy(arrayOfMBeanParameterInfo, 0, localObject3, 0, arrayOfMBeanParameterInfo.length);
/* 260 */       localObject2 = new OpenMBeanOperationInfoSupport(paramString, str, (OpenMBeanParameterInfo[])localObject3, localOpenType1, 3, (Descriptor)localObject1);
/*     */     }
/*     */     else
/*     */     {
/* 267 */       localObject2 = new MBeanOperationInfo(paramString, str, arrayOfMBeanParameterInfo, bool ? localOpenType1.getClassName() : originalTypeString(localType1), 3, (Descriptor)localObject1);
/*     */     }
/*     */ 
/* 277 */     return localObject2;
/*     */   }
/*     */ 
/*     */   Descriptor getBasicMBeanDescriptor()
/*     */   {
/* 282 */     return new ImmutableDescriptor(new String[] { "mxbean=true", "immutableInfo=true" });
/*     */   }
/*     */ 
/*     */   Descriptor getMBeanDescriptor(Class<?> paramClass)
/*     */   {
/* 294 */     return ImmutableDescriptor.EMPTY_DESCRIPTOR;
/*     */   }
/*     */ 
/*     */   private static Descriptor typeDescriptor(OpenType<?> paramOpenType, Type paramType)
/*     */   {
/* 299 */     return new ImmutableDescriptor(new String[] { "openType", "originalType" }, new Object[] { paramOpenType, originalTypeString(paramType) });
/*     */   }
/*     */ 
/*     */   private static boolean canUseOpenInfo(Type paramType)
/*     */   {
/* 320 */     if ((paramType instanceof GenericArrayType)) {
/* 321 */       return canUseOpenInfo(((GenericArrayType)paramType).getGenericComponentType());
/*     */     }
/* 323 */     if (((paramType instanceof Class)) && (((Class)paramType).isArray())) {
/* 324 */       return canUseOpenInfo(((Class)paramType).getComponentType());
/*     */     }
/*     */ 
/* 327 */     return (!(paramType instanceof Class)) || (!((Class)paramType).isPrimitive());
/*     */   }
/*     */ 
/*     */   private static String originalTypeString(Type paramType) {
/* 331 */     if ((paramType instanceof Class)) {
/* 332 */       return ((Class)paramType).getName();
/*     */     }
/* 334 */     return typeName(paramType);
/*     */   }
/*     */ 
/*     */   static String typeName(Type paramType)
/*     */   {
/*     */     Object localObject;
/* 338 */     if ((paramType instanceof Class)) {
/* 339 */       localObject = (Class)paramType;
/* 340 */       if (((Class)localObject).isArray()) {
/* 341 */         return typeName(((Class)localObject).getComponentType()) + "[]";
/*     */       }
/* 343 */       return ((Class)localObject).getName();
/* 344 */     }if ((paramType instanceof GenericArrayType)) {
/* 345 */       localObject = (GenericArrayType)paramType;
/* 346 */       return typeName(((GenericArrayType)localObject).getGenericComponentType()) + "[]";
/* 347 */     }if ((paramType instanceof ParameterizedType)) {
/* 348 */       localObject = (ParameterizedType)paramType;
/* 349 */       StringBuilder localStringBuilder = new StringBuilder();
/* 350 */       localStringBuilder.append(typeName(((ParameterizedType)localObject).getRawType())).append("<");
/* 351 */       String str = "";
/* 352 */       for (Type localType : ((ParameterizedType)localObject).getActualTypeArguments()) {
/* 353 */         localStringBuilder.append(str).append(typeName(localType));
/* 354 */         str = ", ";
/*     */       }
/* 356 */       return ">";
/*     */     }
/* 358 */     return "???";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.MXBeanIntrospector
 * JD-Core Version:    0.6.2
 */