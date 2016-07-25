/*     */ package com.sun.beans.decoder;
/*     */ 
/*     */ import com.sun.beans.finder.FieldFinder;
/*     */ import java.lang.reflect.Field;
/*     */ 
/*     */ final class FieldElementHandler extends AccessorElementHandler
/*     */ {
/*     */   private Class<?> type;
/*     */ 
/*     */   public void addAttribute(String paramString1, String paramString2)
/*     */   {
/*  80 */     if (paramString1.equals("class"))
/*  81 */       this.type = getOwner().findClass(paramString2);
/*     */     else
/*  83 */       super.addAttribute(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   protected boolean isArgument()
/*     */   {
/*  97 */     return (super.isArgument()) && (this.type != null);
/*     */   }
/*     */ 
/*     */   protected Object getContextBean()
/*     */   {
/* 109 */     return this.type != null ? this.type : super.getContextBean();
/*     */   }
/*     */ 
/*     */   protected Object getValue(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 123 */       return getFieldValue(getContextBean(), paramString);
/*     */     }
/*     */     catch (Exception localException) {
/* 126 */       getOwner().handleException(localException);
/*     */     }
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */   protected void setValue(String paramString, Object paramObject)
/*     */   {
/*     */     try
/*     */     {
/* 140 */       setFieldValue(getContextBean(), paramString, paramObject);
/*     */     }
/*     */     catch (Exception localException) {
/* 143 */       getOwner().handleException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object getFieldValue(Object paramObject, String paramString)
/*     */     throws IllegalAccessException, NoSuchFieldException
/*     */   {
/* 158 */     return findField(paramObject, paramString).get(paramObject);
/*     */   }
/*     */ 
/*     */   private static void setFieldValue(Object paramObject1, String paramString, Object paramObject2)
/*     */     throws IllegalAccessException, NoSuchFieldException
/*     */   {
/* 172 */     findField(paramObject1, paramString).set(paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   private static Field findField(Object paramObject, String paramString)
/*     */     throws NoSuchFieldException
/*     */   {
/* 185 */     return (paramObject instanceof Class) ? FieldFinder.findStaticField((Class)paramObject, paramString) : FieldFinder.findField(paramObject.getClass(), paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.FieldElementHandler
 * JD-Core Version:    0.6.2
 */