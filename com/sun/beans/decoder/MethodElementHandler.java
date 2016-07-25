/*     */ package com.sun.beans.decoder;
/*     */ 
/*     */ import com.sun.beans.finder.MethodFinder;
/*     */ import java.lang.reflect.Method;
/*     */ import sun.reflect.misc.MethodUtil;
/*     */ 
/*     */ final class MethodElementHandler extends NewElementHandler
/*     */ {
/*     */   private String name;
/*     */ 
/*     */   public void addAttribute(String paramString1, String paramString2)
/*     */   {
/*  80 */     if (paramString1.equals("name"))
/*  81 */       this.name = paramString2;
/*     */     else
/*  83 */       super.addAttribute(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   protected ValueObject getValueObject(Class<?> paramClass, Object[] paramArrayOfObject)
/*     */     throws Exception
/*     */   {
/*  97 */     Object localObject1 = getContextBean();
/*  98 */     Class[] arrayOfClass = getArgumentTypes(paramArrayOfObject);
/*  99 */     Method localMethod = paramClass != null ? MethodFinder.findStaticMethod(paramClass, this.name, arrayOfClass) : MethodFinder.findMethod(localObject1.getClass(), this.name, arrayOfClass);
/*     */ 
/* 103 */     if (localMethod.isVarArgs()) {
/* 104 */       paramArrayOfObject = getArguments(paramArrayOfObject, localMethod.getParameterTypes());
/*     */     }
/* 106 */     Object localObject2 = MethodUtil.invoke(localMethod, localObject1, paramArrayOfObject);
/* 107 */     return localMethod.getReturnType().equals(Void.TYPE) ? ValueObjectImpl.VOID : ValueObjectImpl.create(localObject2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.MethodElementHandler
 * JD-Core Version:    0.6.2
 */