/*     */ package java.beans;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.EventListener;
/*     */ import java.util.Objects;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import sun.reflect.misc.MethodUtil;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class DefaultPersistenceDelegate extends PersistenceDelegate
/*     */ {
/*     */   private String[] constructor;
/*     */   private Boolean definesEquals;
/*     */ 
/*     */   public DefaultPersistenceDelegate()
/*     */   {
/*  70 */     this(new String[0]);
/*     */   }
/*     */ 
/*     */   public DefaultPersistenceDelegate(String[] paramArrayOfString)
/*     */   {
/*  95 */     this.constructor = paramArrayOfString;
/*     */   }
/*     */ 
/*     */   private static boolean definesEquals(Class paramClass) {
/*     */     try {
/* 100 */       return paramClass == paramClass.getMethod("equals", new Class[] { Object.class }).getDeclaringClass();
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/*     */     }
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean definesEquals(Object paramObject)
/*     */   {
/* 108 */     if (this.definesEquals != null) {
/* 109 */       return this.definesEquals == Boolean.TRUE;
/*     */     }
/*     */ 
/* 112 */     boolean bool = definesEquals(paramObject.getClass());
/* 113 */     this.definesEquals = (bool ? Boolean.TRUE : Boolean.FALSE);
/* 114 */     return bool;
/*     */   }
/*     */ 
/*     */   protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*     */   {
/* 135 */     return (this.constructor.length == 0) || (!definesEquals(paramObject1)) ? super.mutatesTo(paramObject1, paramObject2) : paramObject1.equals(paramObject2);
/*     */   }
/*     */ 
/*     */   protected Expression instantiate(Object paramObject, Encoder paramEncoder)
/*     */   {
/* 155 */     int i = this.constructor.length;
/* 156 */     Class localClass = paramObject.getClass();
/* 157 */     Object[] arrayOfObject = new Object[i];
/* 158 */     for (int j = 0; j < i; j++) {
/*     */       try {
/* 160 */         Method localMethod = findMethod(localClass, this.constructor[j]);
/* 161 */         arrayOfObject[j] = MethodUtil.invoke(localMethod, paramObject, new Object[0]);
/*     */       }
/*     */       catch (Exception localException) {
/* 164 */         paramEncoder.getExceptionListener().exceptionThrown(localException);
/*     */       }
/*     */     }
/* 167 */     return new Expression(paramObject, paramObject.getClass(), "new", arrayOfObject);
/*     */   }
/*     */ 
/*     */   private Method findMethod(Class paramClass, String paramString) {
/* 171 */     if (paramString == null) {
/* 172 */       throw new IllegalArgumentException("Property name is null");
/*     */     }
/* 174 */     PropertyDescriptor localPropertyDescriptor = getPropertyDescriptor(paramClass, paramString);
/* 175 */     if (localPropertyDescriptor == null) {
/* 176 */       throw new IllegalStateException("Could not find property by the name " + paramString);
/*     */     }
/* 178 */     Method localMethod = localPropertyDescriptor.getReadMethod();
/* 179 */     if (localMethod == null) {
/* 180 */       throw new IllegalStateException("Could not find getter for the property " + paramString);
/*     */     }
/* 182 */     return localMethod;
/*     */   }
/*     */ 
/*     */   private void doProperty(Class paramClass, PropertyDescriptor paramPropertyDescriptor, Object paramObject1, Object paramObject2, Encoder paramEncoder) throws Exception {
/* 186 */     Method localMethod1 = paramPropertyDescriptor.getReadMethod();
/* 187 */     Method localMethod2 = paramPropertyDescriptor.getWriteMethod();
/*     */ 
/* 189 */     if ((localMethod1 != null) && (localMethod2 != null)) {
/* 190 */       Expression localExpression1 = new Expression(paramObject1, localMethod1.getName(), new Object[0]);
/* 191 */       Expression localExpression2 = new Expression(paramObject2, localMethod1.getName(), new Object[0]);
/* 192 */       Object localObject1 = localExpression1.getValue();
/* 193 */       Object localObject2 = localExpression2.getValue();
/* 194 */       paramEncoder.writeExpression(localExpression1);
/* 195 */       if (!Objects.equals(localObject2, paramEncoder.get(localObject1)))
/*     */       {
/* 197 */         Object[] arrayOfObject1 = (Object[])paramPropertyDescriptor.getValue("enumerationValues");
/* 198 */         if (((arrayOfObject1 instanceof Object[])) && (Array.getLength(arrayOfObject1) % 3 == 0)) {
/* 199 */           Object[] arrayOfObject2 = (Object[])arrayOfObject1;
/* 200 */           for (int i = 0; i < arrayOfObject2.length; i += 3)
/*     */             try {
/* 202 */               Field localField = paramClass.getField((String)arrayOfObject2[i]);
/* 203 */               if (localField.get(null).equals(localObject1)) {
/* 204 */                 paramEncoder.remove(localObject1);
/* 205 */                 paramEncoder.writeExpression(new Expression(localObject1, localField, "get", new Object[] { null }));
/*     */               }
/*     */             }
/*     */             catch (Exception localException) {
/*     */             }
/*     */         }
/* 211 */         invokeStatement(paramObject1, localMethod2.getName(), new Object[] { localObject1 }, paramEncoder);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void invokeStatement(Object paramObject, String paramString, Object[] paramArrayOfObject, Encoder paramEncoder) {
/* 217 */     paramEncoder.writeStatement(new Statement(paramObject, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   private void initBean(Class paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*     */   {
/*     */     Object localObject4;
/*     */     Object localObject5;
/*     */     Object localObject6;
/* 222 */     for (Object localObject3 : paramClass.getFields())
/* 223 */       if (ReflectUtil.isPackageAccessible(localObject3.getDeclaringClass()))
/*     */       {
/* 226 */         int m = localObject3.getModifiers();
/* 227 */         if ((!Modifier.isFinal(m)) && (!Modifier.isStatic(m)) && (!Modifier.isTransient(m)))
/*     */         {
/*     */           try
/*     */           {
/* 231 */             Expression localExpression = new Expression(localObject3, "get", new Object[] { paramObject1 });
/* 232 */             localObject4 = new Expression(localObject3, "get", new Object[] { paramObject2 });
/* 233 */             localObject5 = localExpression.getValue();
/* 234 */             localObject6 = ((Expression)localObject4).getValue();
/* 235 */             paramEncoder.writeExpression(localExpression);
/* 236 */             if (!Objects.equals(localObject6, paramEncoder.get(localObject5)))
/* 237 */               paramEncoder.writeStatement(new Statement(localObject3, "set", new Object[] { paramObject1, localObject5 }));
/*     */           }
/*     */           catch (Exception localException1)
/*     */           {
/* 241 */             paramEncoder.getExceptionListener().exceptionThrown(localException1);
/*     */           }
/*     */         }
/*     */       }
/*     */     try {
/* 246 */       ??? = Introspector.getBeanInfo(paramClass);
/*     */     }
/*     */     catch (IntrospectionException localIntrospectionException)
/*     */     {
/*     */       return;
/*     */     }
/*     */     PropertyDescriptor localPropertyDescriptor;
/* 251 */     for (localPropertyDescriptor : ((BeanInfo)???).getPropertyDescriptors()) {
/* 252 */       if (!localPropertyDescriptor.isTransient())
/*     */       {
/*     */         try
/*     */         {
/* 256 */           doProperty(paramClass, localPropertyDescriptor, paramObject1, paramObject2, paramEncoder);
/*     */         }
/*     */         catch (Exception localException2) {
/* 259 */           paramEncoder.getExceptionListener().exceptionThrown(localException2);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 287 */     if (!Component.class.isAssignableFrom(paramClass)) {
/* 288 */       return;
/*     */     }
/* 290 */     for (localPropertyDescriptor : ((BeanInfo)???).getEventSetDescriptors())
/* 291 */       if (!localPropertyDescriptor.isTransient())
/*     */       {
/* 294 */         Class localClass = localPropertyDescriptor.getListenerType();
/*     */ 
/* 299 */         if (localClass != ComponentListener.class)
/*     */         {
/* 310 */           if ((localClass != ChangeListener.class) || (paramClass != JMenuItem.class))
/*     */           {
/* 315 */             localObject4 = new EventListener[0];
/* 316 */             localObject5 = new EventListener[0];
/*     */             try {
/* 318 */               localObject6 = localPropertyDescriptor.getGetListenerMethod();
/* 319 */               localObject4 = (EventListener[])MethodUtil.invoke((Method)localObject6, paramObject1, new Object[0]);
/* 320 */               localObject5 = (EventListener[])MethodUtil.invoke((Method)localObject6, paramObject2, new Object[0]);
/*     */             }
/*     */             catch (Exception localException3) {
/*     */               try {
/* 324 */                 Method localMethod = paramClass.getMethod("getListeners", new Class[] { Class.class });
/* 325 */                 localObject4 = (EventListener[])MethodUtil.invoke(localMethod, paramObject1, new Object[] { localClass });
/* 326 */                 localObject5 = (EventListener[])MethodUtil.invoke(localMethod, paramObject2, new Object[] { localClass });
/*     */               }
/*     */               catch (Exception localException4) {
/* 329 */                 return;
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 335 */             String str1 = localPropertyDescriptor.getAddListenerMethod().getName();
/* 336 */             for (int n = localObject5.length; n < localObject4.length; n++)
/*     */             {
/* 338 */               invokeStatement(paramObject1, str1, new Object[] { localObject4[n] }, paramEncoder);
/*     */             }
/*     */ 
/* 341 */             String str2 = localPropertyDescriptor.getRemoveListenerMethod().getName();
/* 342 */             for (int i1 = localObject4.length; i1 < localObject5.length; i1++)
/* 343 */               invokeStatement(paramObject1, str2, new Object[] { localObject5[i1] }, paramEncoder);
/*     */           }
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*     */   {
/* 401 */     super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/* 402 */     if (paramObject1.getClass() == paramClass)
/* 403 */       initBean(paramClass, paramObject1, paramObject2, paramEncoder);
/*     */   }
/*     */ 
/*     */   private static PropertyDescriptor getPropertyDescriptor(Class paramClass, String paramString)
/*     */   {
/*     */     try {
/* 409 */       for (PropertyDescriptor localPropertyDescriptor : Introspector.getBeanInfo(paramClass).getPropertyDescriptors())
/* 410 */         if (paramString.equals(localPropertyDescriptor.getName()))
/* 411 */           return localPropertyDescriptor;
/*     */     }
/*     */     catch (IntrospectionException localIntrospectionException) {
/*     */     }
/* 415 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.DefaultPersistenceDelegate
 * JD-Core Version:    0.6.2
 */