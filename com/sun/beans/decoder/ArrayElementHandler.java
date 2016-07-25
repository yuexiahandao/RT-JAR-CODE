/*     */ package com.sun.beans.decoder;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ 
/*     */ final class ArrayElementHandler extends NewElementHandler
/*     */ {
/*     */   private Integer length;
/*     */ 
/*     */   public void addAttribute(String paramString1, String paramString2)
/*     */   {
/*  94 */     if (paramString1.equals("length"))
/*  95 */       this.length = Integer.valueOf(paramString2);
/*     */     else
/*  97 */       super.addAttribute(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public void startElement()
/*     */   {
/* 107 */     if (this.length != null)
/* 108 */       getValueObject();
/*     */   }
/*     */ 
/*     */   protected boolean isArgument()
/*     */   {
/* 122 */     return true;
/*     */   }
/*     */ 
/*     */   protected ValueObject getValueObject(Class<?> paramClass, Object[] paramArrayOfObject)
/*     */   {
/* 135 */     if (paramClass == null) {
/* 136 */       paramClass = Object.class;
/*     */     }
/* 138 */     if (this.length != null) {
/* 139 */       return ValueObjectImpl.create(Array.newInstance(paramClass, this.length.intValue()));
/*     */     }
/* 141 */     Object localObject = Array.newInstance(paramClass, paramArrayOfObject.length);
/* 142 */     for (int i = 0; i < paramArrayOfObject.length; i++) {
/* 143 */       Array.set(localObject, i, paramArrayOfObject[i]);
/*     */     }
/* 145 */     return ValueObjectImpl.create(localObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.ArrayElementHandler
 * JD-Core Version:    0.6.2
 */