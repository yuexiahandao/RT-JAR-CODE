/*     */ package com.sun.beans.decoder;
/*     */ 
/*     */ import com.sun.beans.finder.MethodFinder;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.IndexedPropertyDescriptor;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import sun.reflect.misc.MethodUtil;
/*     */ 
/*     */ final class PropertyElementHandler extends AccessorElementHandler
/*     */ {
/*     */   static final String GETTER = "get";
/*     */   static final String SETTER = "set";
/*     */   private Integer index;
/*     */ 
/*     */   public void addAttribute(String paramString1, String paramString2)
/*     */   {
/* 103 */     if (paramString1.equals("index"))
/* 104 */       this.index = Integer.valueOf(paramString2);
/*     */     else
/* 106 */       super.addAttribute(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   protected boolean isArgument()
/*     */   {
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   protected Object getValue(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 132 */       return getPropertyValue(getContextBean(), paramString, this.index);
/*     */     }
/*     */     catch (Exception localException) {
/* 135 */       getOwner().handleException(localException);
/*     */     }
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   protected void setValue(String paramString, Object paramObject)
/*     */   {
/*     */     try
/*     */     {
/* 149 */       setPropertyValue(getContextBean(), paramString, this.index, paramObject);
/*     */     }
/*     */     catch (Exception localException) {
/* 152 */       getOwner().handleException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Object getPropertyValue(Object paramObject, String paramString, Integer paramInteger)
/*     */     throws IllegalAccessException, IntrospectionException, InvocationTargetException, NoSuchMethodException
/*     */   {
/* 171 */     Class localClass = paramObject.getClass();
/* 172 */     if (paramInteger == null)
/* 173 */       return MethodUtil.invoke(findGetter(localClass, paramString, new Class[0]), paramObject, new Object[0]);
/* 174 */     if ((localClass.isArray()) && (paramString == null)) {
/* 175 */       return Array.get(paramObject, paramInteger.intValue());
/*     */     }
/* 177 */     return MethodUtil.invoke(findGetter(localClass, paramString, new Class[] { Integer.TYPE }), paramObject, new Object[] { paramInteger });
/*     */   }
/*     */ 
/*     */   private static void setPropertyValue(Object paramObject1, String paramString, Integer paramInteger, Object paramObject2)
/*     */     throws IllegalAccessException, IntrospectionException, InvocationTargetException, NoSuchMethodException
/*     */   {
/* 196 */     Class localClass = paramObject1.getClass();
/* 197 */     Object localObject = paramObject2 != null ? paramObject2.getClass() : null;
/*     */ 
/* 201 */     if (paramInteger == null)
/* 202 */       MethodUtil.invoke(findSetter(localClass, paramString, new Class[] { localObject }), paramObject1, new Object[] { paramObject2 });
/* 203 */     else if ((localClass.isArray()) && (paramString == null))
/* 204 */       Array.set(paramObject1, paramInteger.intValue(), paramObject2);
/*     */     else
/* 206 */       MethodUtil.invoke(findSetter(localClass, paramString, new Class[] { Integer.TYPE, localObject }), paramObject1, new Object[] { paramInteger, paramObject2 });
/*     */   }
/*     */ 
/*     */   private static Method findGetter(Class<?> paramClass, String paramString, Class<?>[] paramArrayOfClass)
/*     */     throws IntrospectionException, NoSuchMethodException
/*     */   {
/* 222 */     if (paramString == null) {
/* 223 */       return MethodFinder.findInstanceMethod(paramClass, "get", paramArrayOfClass);
/*     */     }
/* 225 */     PropertyDescriptor localPropertyDescriptor = getProperty(paramClass, paramString);
/*     */     Object localObject;
/* 226 */     if (paramArrayOfClass.length == 0) {
/* 227 */       localObject = localPropertyDescriptor.getReadMethod();
/* 228 */       if (localObject != null)
/* 229 */         return localObject;
/*     */     }
/* 231 */     else if ((localPropertyDescriptor instanceof IndexedPropertyDescriptor)) {
/* 232 */       localObject = (IndexedPropertyDescriptor)localPropertyDescriptor;
/* 233 */       Method localMethod = ((IndexedPropertyDescriptor)localObject).getIndexedReadMethod();
/* 234 */       if (localMethod != null) {
/* 235 */         return localMethod;
/*     */       }
/*     */     }
/* 238 */     throw new IntrospectionException("Could not find getter for the " + paramString + " property");
/*     */   }
/*     */ 
/*     */   private static Method findSetter(Class<?> paramClass, String paramString, Class<?>[] paramArrayOfClass)
/*     */     throws IntrospectionException, NoSuchMethodException
/*     */   {
/* 253 */     if (paramString == null) {
/* 254 */       return MethodFinder.findInstanceMethod(paramClass, "set", paramArrayOfClass);
/*     */     }
/* 256 */     PropertyDescriptor localPropertyDescriptor = getProperty(paramClass, paramString);
/*     */     Object localObject;
/* 257 */     if (paramArrayOfClass.length == 1) {
/* 258 */       localObject = localPropertyDescriptor.getWriteMethod();
/* 259 */       if (localObject != null)
/* 260 */         return localObject;
/*     */     }
/* 262 */     else if ((localPropertyDescriptor instanceof IndexedPropertyDescriptor)) {
/* 263 */       localObject = (IndexedPropertyDescriptor)localPropertyDescriptor;
/* 264 */       Method localMethod = ((IndexedPropertyDescriptor)localObject).getIndexedWriteMethod();
/* 265 */       if (localMethod != null) {
/* 266 */         return localMethod;
/*     */       }
/*     */     }
/* 269 */     throw new IntrospectionException("Could not find setter for the " + paramString + " property");
/*     */   }
/*     */ 
/*     */   private static PropertyDescriptor getProperty(Class<?> paramClass, String paramString)
/*     */     throws IntrospectionException
/*     */   {
/* 282 */     for (PropertyDescriptor localPropertyDescriptor : Introspector.getBeanInfo(paramClass).getPropertyDescriptors()) {
/* 283 */       if (paramString.equals(localPropertyDescriptor.getName())) {
/* 284 */         return localPropertyDescriptor;
/*     */       }
/*     */     }
/* 287 */     throw new IntrospectionException("Could not find the " + paramString + " property descriptor");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.PropertyElementHandler
 * JD-Core Version:    0.6.2
 */