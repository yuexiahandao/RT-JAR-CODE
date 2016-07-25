/*    */ package com.sun.xml.internal.bind.v2.runtime.property;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Type;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ final class Utils
/*    */ {
/* 48 */   private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());
/*    */   static final Navigator<Type, Class, Field, Method> REFLECTION_NAVIGATOR;
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 57 */       Class refNav = Class.forName("com.sun.xml.internal.bind.v2.model.nav.ReflectionNavigator");
/*    */ 
/* 60 */       Method getInstance = (Method)AccessController.doPrivileged(new PrivilegedAction()
/*    */       {
/*    */         public Method run()
/*    */         {
/*    */           try {
/* 65 */             Method getInstance = this.val$refNav.getDeclaredMethod("getInstance", new Class[0]);
/* 66 */             getInstance.setAccessible(true);
/* 67 */             return getInstance; } catch (NoSuchMethodException e) {
/*    */           }
/* 69 */           throw new IllegalStateException("ReflectionNavigator.getInstance can't be found");
/*    */         }
/*    */       });
/* 76 */       REFLECTION_NAVIGATOR = (Navigator)getInstance.invoke(null, new Object[0]);
/*    */     } catch (ClassNotFoundException e) {
/* 78 */       throw new IllegalStateException("Can't find ReflectionNavigator class");
/*    */     } catch (InvocationTargetException e) {
/* 80 */       throw new IllegalStateException("ReflectionNavigator.getInstance throws the exception");
/*    */     } catch (IllegalAccessException e) {
/* 82 */       throw new IllegalStateException("ReflectionNavigator.getInstance method is inaccessible");
/*    */     } catch (SecurityException e) {
/* 84 */       LOGGER.log(Level.FINE, "Unable to access ReflectionNavigator.getInstance", e);
/* 85 */       throw e;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.Utils
 * JD-Core Version:    0.6.2
 */