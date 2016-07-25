/*    */ package com.sun.beans.infos;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.beans.IntrospectionException;
/*    */ import java.beans.PropertyDescriptor;
/*    */ import java.beans.SimpleBeanInfo;
/*    */ 
/*    */ public class ComponentBeanInfo extends SimpleBeanInfo
/*    */ {
/* 35 */   private static final Class beanClass = Component.class;
/*    */ 
/*    */   public PropertyDescriptor[] getPropertyDescriptors()
/*    */   {
/*    */     try {
/* 40 */       PropertyDescriptor localPropertyDescriptor1 = new PropertyDescriptor("name", beanClass);
/* 41 */       PropertyDescriptor localPropertyDescriptor2 = new PropertyDescriptor("background", beanClass);
/* 42 */       PropertyDescriptor localPropertyDescriptor3 = new PropertyDescriptor("foreground", beanClass);
/* 43 */       PropertyDescriptor localPropertyDescriptor4 = new PropertyDescriptor("font", beanClass);
/* 44 */       PropertyDescriptor localPropertyDescriptor5 = new PropertyDescriptor("enabled", beanClass);
/* 45 */       PropertyDescriptor localPropertyDescriptor6 = new PropertyDescriptor("visible", beanClass);
/* 46 */       PropertyDescriptor localPropertyDescriptor7 = new PropertyDescriptor("focusable", beanClass);
/*    */ 
/* 48 */       localPropertyDescriptor5.setExpert(true);
/* 49 */       localPropertyDescriptor6.setHidden(true);
/*    */ 
/* 51 */       localPropertyDescriptor2.setBound(true);
/* 52 */       localPropertyDescriptor3.setBound(true);
/* 53 */       localPropertyDescriptor4.setBound(true);
/* 54 */       localPropertyDescriptor7.setBound(true);
/*    */ 
/* 56 */       return new PropertyDescriptor[] { localPropertyDescriptor1, localPropertyDescriptor2, localPropertyDescriptor3, localPropertyDescriptor4, localPropertyDescriptor5, localPropertyDescriptor6, localPropertyDescriptor7 };
/*    */     }
/*    */     catch (IntrospectionException localIntrospectionException) {
/* 59 */       throw new Error(localIntrospectionException.toString());
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.infos.ComponentBeanInfo
 * JD-Core Version:    0.6.2
 */