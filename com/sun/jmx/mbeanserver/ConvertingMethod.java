/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import java.io.InvalidObjectException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.openmbean.OpenDataException;
/*     */ import javax.management.openmbean.OpenType;
/*     */ import sun.reflect.misc.MethodUtil;
/*     */ 
/*     */ final class ConvertingMethod
/*     */ {
/* 223 */   private static final String[] noStrings = new String[0];
/*     */   private final Method method;
/*     */   private final MXBeanMapping returnMapping;
/*     */   private final MXBeanMapping[] paramMappings;
/*     */   private final boolean paramConversionIsIdentity;
/*     */ 
/*     */   static ConvertingMethod from(Method paramMethod)
/*     */   {
/*     */     try
/*     */     {
/*  41 */       return new ConvertingMethod(paramMethod);
/*     */     } catch (OpenDataException localOpenDataException) {
/*  43 */       String str = "Method " + paramMethod.getDeclaringClass().getName() + "." + paramMethod.getName() + " has parameter or return type that " + "cannot be translated into an open type";
/*     */ 
/*  46 */       throw new IllegalArgumentException(str, localOpenDataException);
/*     */     }
/*     */   }
/*     */ 
/*     */   Method getMethod() {
/*  51 */     return this.method;
/*     */   }
/*     */ 
/*     */   Descriptor getDescriptor() {
/*  55 */     return Introspector.descriptorForElement(this.method);
/*     */   }
/*     */ 
/*     */   Type getGenericReturnType() {
/*  59 */     return this.method.getGenericReturnType();
/*     */   }
/*     */ 
/*     */   Type[] getGenericParameterTypes() {
/*  63 */     return this.method.getGenericParameterTypes();
/*     */   }
/*     */ 
/*     */   String getName() {
/*  67 */     return this.method.getName();
/*     */   }
/*     */ 
/*     */   OpenType<?> getOpenReturnType() {
/*  71 */     return this.returnMapping.getOpenType();
/*     */   }
/*     */ 
/*     */   OpenType<?>[] getOpenParameterTypes() {
/*  75 */     OpenType[] arrayOfOpenType = new OpenType[this.paramMappings.length];
/*  76 */     for (int i = 0; i < this.paramMappings.length; i++)
/*  77 */       arrayOfOpenType[i] = this.paramMappings[i].getOpenType();
/*  78 */     return arrayOfOpenType;
/*     */   }
/*     */ 
/*     */   void checkCallFromOpen()
/*     */   {
/*     */     try
/*     */     {
/*  91 */       for (MXBeanMapping localMXBeanMapping : this.paramMappings)
/*  92 */         localMXBeanMapping.checkReconstructible();
/*     */     } catch (InvalidObjectException localInvalidObjectException) {
/*  94 */       throw new IllegalArgumentException(localInvalidObjectException);
/*     */     }
/*     */   }
/*     */ 
/*     */   void checkCallToOpen()
/*     */   {
/*     */     try
/*     */     {
/* 108 */       this.returnMapping.checkReconstructible();
/*     */     } catch (InvalidObjectException localInvalidObjectException) {
/* 110 */       throw new IllegalArgumentException(localInvalidObjectException);
/*     */     }
/*     */   }
/*     */ 
/*     */   String[] getOpenSignature() {
/* 115 */     if (this.paramMappings.length == 0) {
/* 116 */       return noStrings;
/*     */     }
/* 118 */     String[] arrayOfString = new String[this.paramMappings.length];
/* 119 */     for (int i = 0; i < this.paramMappings.length; i++)
/* 120 */       arrayOfString[i] = this.paramMappings[i].getOpenClass().getName();
/* 121 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   final Object toOpenReturnValue(MXBeanLookup paramMXBeanLookup, Object paramObject) throws OpenDataException
/*     */   {
/* 126 */     return this.returnMapping.toOpenValue(paramObject);
/*     */   }
/*     */ 
/*     */   final Object fromOpenReturnValue(MXBeanLookup paramMXBeanLookup, Object paramObject) throws InvalidObjectException
/*     */   {
/* 131 */     return this.returnMapping.fromOpenValue(paramObject);
/*     */   }
/*     */ 
/*     */   final Object[] toOpenParameters(MXBeanLookup paramMXBeanLookup, Object[] paramArrayOfObject) throws OpenDataException
/*     */   {
/* 136 */     if ((this.paramConversionIsIdentity) || (paramArrayOfObject == null))
/* 137 */       return paramArrayOfObject;
/* 138 */     Object[] arrayOfObject = new Object[paramArrayOfObject.length];
/* 139 */     for (int i = 0; i < paramArrayOfObject.length; i++)
/* 140 */       arrayOfObject[i] = this.paramMappings[i].toOpenValue(paramArrayOfObject[i]);
/* 141 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   final Object[] fromOpenParameters(Object[] paramArrayOfObject) throws InvalidObjectException
/*     */   {
/* 146 */     if ((this.paramConversionIsIdentity) || (paramArrayOfObject == null))
/* 147 */       return paramArrayOfObject;
/* 148 */     Object[] arrayOfObject = new Object[paramArrayOfObject.length];
/* 149 */     for (int i = 0; i < paramArrayOfObject.length; i++)
/* 150 */       arrayOfObject[i] = this.paramMappings[i].fromOpenValue(paramArrayOfObject[i]);
/* 151 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   final Object toOpenParameter(MXBeanLookup paramMXBeanLookup, Object paramObject, int paramInt)
/*     */     throws OpenDataException
/*     */   {
/* 158 */     return this.paramMappings[paramInt].toOpenValue(paramObject);
/*     */   }
/*     */ 
/*     */   final Object fromOpenParameter(MXBeanLookup paramMXBeanLookup, Object paramObject, int paramInt)
/*     */     throws InvalidObjectException
/*     */   {
/* 165 */     return this.paramMappings[paramInt].fromOpenValue(paramObject);
/*     */   }
/*     */ 
/*     */   Object invokeWithOpenReturn(MXBeanLookup paramMXBeanLookup, Object paramObject, Object[] paramArrayOfObject)
/*     */     throws MBeanException, IllegalAccessException, InvocationTargetException
/*     */   {
/* 172 */     MXBeanLookup localMXBeanLookup = MXBeanLookup.getLookup();
/*     */     try {
/* 174 */       MXBeanLookup.setLookup(paramMXBeanLookup);
/* 175 */       return invokeWithOpenReturn(paramObject, paramArrayOfObject);
/*     */     } finally {
/* 177 */       MXBeanLookup.setLookup(localMXBeanLookup);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Object invokeWithOpenReturn(Object paramObject, Object[] paramArrayOfObject) throws MBeanException, IllegalAccessException, InvocationTargetException
/*     */   {
/*     */     Object[] arrayOfObject;
/*     */     try
/*     */     {
/* 186 */       arrayOfObject = fromOpenParameters(paramArrayOfObject);
/*     */     }
/*     */     catch (InvalidObjectException localInvalidObjectException) {
/* 189 */       String str1 = methodName() + ": cannot convert parameters " + "from open values: " + localInvalidObjectException;
/*     */ 
/* 191 */       throw new MBeanException(localInvalidObjectException, str1);
/*     */     }
/* 193 */     Object localObject = MethodUtil.invoke(this.method, paramObject, arrayOfObject);
/*     */     try {
/* 195 */       return this.returnMapping.toOpenValue(localObject);
/*     */     }
/*     */     catch (OpenDataException localOpenDataException) {
/* 198 */       String str2 = methodName() + ": cannot convert return " + "value to open value: " + localOpenDataException;
/*     */ 
/* 200 */       throw new MBeanException(localOpenDataException, str2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String methodName() {
/* 205 */     return this.method.getDeclaringClass() + "." + this.method.getName();
/*     */   }
/*     */ 
/*     */   private ConvertingMethod(Method paramMethod) throws OpenDataException {
/* 209 */     this.method = paramMethod;
/* 210 */     MXBeanMappingFactory localMXBeanMappingFactory = MXBeanMappingFactory.DEFAULT;
/* 211 */     this.returnMapping = localMXBeanMappingFactory.mappingForType(paramMethod.getGenericReturnType(), localMXBeanMappingFactory);
/*     */ 
/* 213 */     Type[] arrayOfType = paramMethod.getGenericParameterTypes();
/* 214 */     this.paramMappings = new MXBeanMapping[arrayOfType.length];
/* 215 */     boolean bool = true;
/* 216 */     for (int i = 0; i < arrayOfType.length; i++) {
/* 217 */       this.paramMappings[i] = localMXBeanMappingFactory.mappingForType(arrayOfType[i], localMXBeanMappingFactory);
/* 218 */       bool &= DefaultMXBeanMappingFactory.isIdentity(this.paramMappings[i]);
/*     */     }
/* 220 */     this.paramConversionIsIdentity = bool;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.ConvertingMethod
 * JD-Core Version:    0.6.2
 */