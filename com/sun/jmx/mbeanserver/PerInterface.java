/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.InvalidAttributeValueException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.ReflectionException;
/*     */ 
/*     */ final class PerInterface<M>
/*     */ {
/*     */   private final Class<?> mbeanInterface;
/*     */   private final MBeanIntrospector<M> introspector;
/*     */   private final MBeanInfo mbeanInfo;
/* 277 */   private final Map<String, M> getters = Util.newMap();
/* 278 */   private final Map<String, M> setters = Util.newMap();
/* 279 */   private final Map<String, List<PerInterface<M>.MethodAndSig>> ops = Util.newMap();
/*     */ 
/*     */   PerInterface(Class<?> paramClass, MBeanIntrospector<M> paramMBeanIntrospector, MBeanAnalyzer<M> paramMBeanAnalyzer, MBeanInfo paramMBeanInfo)
/*     */   {
/*  51 */     this.mbeanInterface = paramClass;
/*  52 */     this.introspector = paramMBeanIntrospector;
/*  53 */     this.mbeanInfo = paramMBeanInfo;
/*  54 */     paramMBeanAnalyzer.visit(new InitMaps(null));
/*     */   }
/*     */ 
/*     */   Class<?> getMBeanInterface() {
/*  58 */     return this.mbeanInterface;
/*     */   }
/*     */ 
/*     */   MBeanInfo getMBeanInfo() {
/*  62 */     return this.mbeanInfo;
/*     */   }
/*     */ 
/*     */   boolean isMXBean() {
/*  66 */     return this.introspector.isMXBean();
/*     */   }
/*     */ 
/*     */   Object getAttribute(Object paramObject1, String paramString, Object paramObject2)
/*     */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*     */   {
/*  74 */     Object localObject = this.getters.get(paramString);
/*  75 */     if (localObject == null)
/*     */     {
/*     */       String str;
/*  77 */       if (this.setters.containsKey(paramString))
/*  78 */         str = "Write-only attribute: " + paramString;
/*     */       else
/*  80 */         str = "No such attribute: " + paramString;
/*  81 */       throw new AttributeNotFoundException(str);
/*     */     }
/*  83 */     return this.introspector.invokeM(localObject, paramObject1, (Object[])null, paramObject2);
/*     */   }
/*     */ 
/*     */   void setAttribute(Object paramObject1, String paramString, Object paramObject2, Object paramObject3)
/*     */     throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
/*     */   {
/*  93 */     Object localObject = this.setters.get(paramString);
/*  94 */     if (localObject == null)
/*     */     {
/*     */       String str;
/*  96 */       if (this.getters.containsKey(paramString))
/*  97 */         str = "Read-only attribute: " + paramString;
/*     */       else
/*  99 */         str = "No such attribute: " + paramString;
/* 100 */       throw new AttributeNotFoundException(str);
/*     */     }
/* 102 */     this.introspector.invokeSetter(paramString, localObject, paramObject1, paramObject2, paramObject3);
/*     */   }
/*     */ 
/*     */   Object invoke(Object paramObject1, String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString, Object paramObject2)
/*     */     throws MBeanException, ReflectionException
/*     */   {
/* 109 */     List localList = (List)this.ops.get(paramString);
/* 110 */     if (localList == null) {
/* 111 */       localObject1 = "No such operation: " + paramString;
/* 112 */       return noSuchMethod((String)localObject1, paramObject1, paramString, paramArrayOfObject, paramArrayOfString, paramObject2);
/*     */     }
/*     */ 
/* 115 */     if (paramArrayOfString == null)
/* 116 */       paramArrayOfString = new String[0];
/* 117 */     Object localObject1 = null;
/* 118 */     for (Object localObject2 = localList.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (MethodAndSig)((Iterator)localObject2).next();
/* 119 */       if (Arrays.equals(((MethodAndSig)localObject3).signature, paramArrayOfString)) {
/* 120 */         localObject1 = localObject3;
/* 121 */         break;
/*     */       }
/*     */     }
/*     */     Object localObject3;
/* 124 */     if (localObject1 == null) {
/* 125 */       localObject2 = sigString(paramArrayOfString);
/*     */ 
/* 127 */       if (localList.size() == 1) {
/* 128 */         localObject3 = "Signature mismatch for operation " + paramString + ": " + (String)localObject2 + " should be " + sigString(((MethodAndSig)localList.get(0)).signature);
/*     */       }
/*     */       else
/*     */       {
/* 132 */         localObject3 = "Operation " + paramString + " exists but not with " + "this signature: " + (String)localObject2;
/*     */       }
/*     */ 
/* 135 */       return noSuchMethod((String)localObject3, paramObject1, paramString, paramArrayOfObject, paramArrayOfString, paramObject2);
/*     */     }
/*     */ 
/* 138 */     return this.introspector.invokeM(((MethodAndSig)localObject1).method, paramObject1, paramArrayOfObject, paramObject2);
/*     */   }
/*     */ 
/*     */   private Object noSuchMethod(String paramString1, Object paramObject1, String paramString2, Object[] paramArrayOfObject, String[] paramArrayOfString, Object paramObject2)
/*     */     throws MBeanException, ReflectionException
/*     */   {
/* 168 */     NoSuchMethodException localNoSuchMethodException = new NoSuchMethodException(paramString2 + sigString(paramArrayOfString));
/*     */ 
/* 170 */     ReflectionException localReflectionException = new ReflectionException(localNoSuchMethodException, paramString1);
/*     */ 
/* 173 */     if (this.introspector.isMXBean()) {
/* 174 */       throw localReflectionException;
/* 177 */     }
/*     */ GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.invoke.getters");
/*     */     String str1;
/*     */     try {
/* 180 */       str1 = (String)AccessController.doPrivileged(localGetPropertyAction);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 184 */       str1 = null;
/*     */     }
/* 186 */     if (str1 == null) {
/* 187 */       throw localReflectionException;
/*     */     }
/* 189 */     int i = 0;
/* 190 */     Map localMap = null;
/* 191 */     if ((paramArrayOfString == null) || (paramArrayOfString.length == 0)) {
/* 192 */       if (paramString2.startsWith("get"))
/* 193 */         i = 3;
/* 194 */       else if (paramString2.startsWith("is"))
/* 195 */         i = 2;
/* 196 */       if (i != 0)
/* 197 */         localMap = this.getters;
/* 198 */     } else if ((paramArrayOfString.length == 1) && (paramString2.startsWith("set")))
/*     */     {
/* 200 */       i = 3;
/* 201 */       localMap = this.setters;
/*     */     }
/*     */ 
/* 204 */     if (i != 0) {
/* 205 */       String str2 = paramString2.substring(i);
/* 206 */       Object localObject = localMap.get(str2);
/* 207 */       if ((localObject != null) && (this.introspector.getName(localObject).equals(paramString2))) {
/* 208 */         String[] arrayOfString = this.introspector.getSignature(localObject);
/* 209 */         if (((paramArrayOfString == null) && (arrayOfString.length == 0)) || (Arrays.equals(paramArrayOfString, arrayOfString)))
/*     */         {
/* 211 */           return this.introspector.invokeM(localObject, paramObject1, paramArrayOfObject, paramObject2);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 216 */     throw localReflectionException;
/*     */   }
/*     */ 
/*     */   private String sigString(String[] paramArrayOfString) {
/* 220 */     StringBuilder localStringBuilder = new StringBuilder("(");
/* 221 */     if (paramArrayOfString != null) {
/* 222 */       for (String str : paramArrayOfString) {
/* 223 */         if (localStringBuilder.length() > 1)
/* 224 */           localStringBuilder.append(", ");
/* 225 */         localStringBuilder.append(str);
/*     */       }
/*     */     }
/* 228 */     return ")";
/*     */   }
/*     */ 
/*     */   private class InitMaps
/*     */     implements MBeanAnalyzer.MBeanVisitor<M>
/*     */   {
/*     */     private InitMaps()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void visitAttribute(String paramString, M paramM1, M paramM2)
/*     */     {
/*     */       Object localObject;
/* 238 */       if (paramM1 != null) {
/* 239 */         PerInterface.this.introspector.checkMethod(paramM1);
/* 240 */         localObject = PerInterface.this.getters.put(paramString, paramM1);
/* 241 */         assert (localObject == null);
/*     */       }
/* 243 */       if (paramM2 != null) {
/* 244 */         PerInterface.this.introspector.checkMethod(paramM2);
/* 245 */         localObject = PerInterface.this.setters.put(paramString, paramM2);
/* 246 */         assert (localObject == null);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void visitOperation(String paramString, M paramM)
/*     */     {
/* 252 */       PerInterface.this.introspector.checkMethod(paramM);
/* 253 */       String[] arrayOfString = PerInterface.this.introspector.getSignature(paramM);
/* 254 */       PerInterface.MethodAndSig localMethodAndSig = new PerInterface.MethodAndSig(PerInterface.this, null);
/* 255 */       localMethodAndSig.method = paramM;
/* 256 */       localMethodAndSig.signature = arrayOfString;
/* 257 */       List localList = (List)PerInterface.this.ops.get(paramString);
/* 258 */       if (localList == null) {
/* 259 */         localList = Collections.singletonList(localMethodAndSig);
/*     */       } else {
/* 261 */         if (localList.size() == 1)
/* 262 */           localList = Util.newList(localList);
/* 263 */         localList.add(localMethodAndSig);
/*     */       }
/* 265 */       PerInterface.this.ops.put(paramString, localList);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class MethodAndSig
/*     */   {
/*     */     M method;
/*     */     String[] signature;
/*     */ 
/*     */     private MethodAndSig()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.PerInterface
 * JD-Core Version:    0.6.2
 */