/*     */ package com.sun.beans.decoder;
/*     */ 
/*     */ import com.sun.beans.finder.ConstructorFinder;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ class NewElementHandler extends ElementHandler
/*     */ {
/*  59 */   private List<Object> arguments = new ArrayList();
/*  60 */   private ValueObject value = ValueObjectImpl.VOID;
/*     */   private Class<?> type;
/*     */ 
/*     */   public void addAttribute(String paramString1, String paramString2)
/*     */   {
/*  79 */     if (paramString1.equals("class"))
/*  80 */       this.type = getOwner().findClass(paramString2);
/*     */     else
/*  82 */       super.addAttribute(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   protected final void addArgument(Object paramObject)
/*     */   {
/*  94 */     if (this.arguments == null) {
/*  95 */       throw new IllegalStateException("Could not add argument to evaluated element");
/*     */     }
/*  97 */     this.arguments.add(paramObject);
/*     */   }
/*     */ 
/*     */   protected final Object getContextBean()
/*     */   {
/* 109 */     return this.type != null ? this.type : super.getContextBean();
/*     */   }
/*     */ 
/*     */   protected final ValueObject getValueObject()
/*     */   {
/* 121 */     if (this.arguments != null) {
/*     */       try {
/* 123 */         this.value = getValueObject(this.type, this.arguments.toArray());
/*     */       }
/*     */       catch (Exception localException) {
/* 126 */         getOwner().handleException(localException);
/*     */       }
/*     */       finally {
/* 129 */         this.arguments = null;
/*     */       }
/*     */     }
/* 132 */     return this.value;
/*     */   }
/*     */ 
/*     */   ValueObject getValueObject(Class<?> paramClass, Object[] paramArrayOfObject)
/*     */     throws Exception
/*     */   {
/* 148 */     if (paramClass == null) {
/* 149 */       throw new IllegalArgumentException("Class name is not set");
/*     */     }
/* 151 */     Class[] arrayOfClass = getArgumentTypes(paramArrayOfObject);
/* 152 */     Constructor localConstructor = ConstructorFinder.findConstructor(paramClass, arrayOfClass);
/* 153 */     if (localConstructor.isVarArgs()) {
/* 154 */       paramArrayOfObject = getArguments(paramArrayOfObject, localConstructor.getParameterTypes());
/*     */     }
/* 156 */     return ValueObjectImpl.create(localConstructor.newInstance(paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   static Class<?>[] getArgumentTypes(Object[] paramArrayOfObject)
/*     */   {
/* 167 */     Class[] arrayOfClass = new Class[paramArrayOfObject.length];
/* 168 */     for (int i = 0; i < paramArrayOfObject.length; i++) {
/* 169 */       if (paramArrayOfObject[i] != null) {
/* 170 */         arrayOfClass[i] = paramArrayOfObject[i].getClass();
/*     */       }
/*     */     }
/* 173 */     return arrayOfClass;
/*     */   }
/*     */ 
/*     */   static Object[] getArguments(Object[] paramArrayOfObject, Class<?>[] paramArrayOfClass)
/*     */   {
/* 184 */     int i = paramArrayOfClass.length - 1;
/* 185 */     if (paramArrayOfClass.length == paramArrayOfObject.length) {
/* 186 */       Object localObject1 = paramArrayOfObject[i];
/* 187 */       if (localObject1 == null) {
/* 188 */         return paramArrayOfObject;
/*     */       }
/* 190 */       localObject2 = paramArrayOfClass[i];
/* 191 */       if (((Class)localObject2).isAssignableFrom(localObject1.getClass())) {
/* 192 */         return paramArrayOfObject;
/*     */       }
/*     */     }
/* 195 */     int j = paramArrayOfObject.length - i;
/* 196 */     Object localObject2 = paramArrayOfClass[i].getComponentType();
/* 197 */     Object localObject3 = Array.newInstance((Class)localObject2, j);
/* 198 */     System.arraycopy(paramArrayOfObject, i, localObject3, 0, j);
/*     */ 
/* 200 */     Object[] arrayOfObject = new Object[paramArrayOfClass.length];
/* 201 */     System.arraycopy(paramArrayOfObject, 0, arrayOfObject, 0, i);
/* 202 */     arrayOfObject[i] = localObject3;
/* 203 */     return arrayOfObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.NewElementHandler
 * JD-Core Version:    0.6.2
 */