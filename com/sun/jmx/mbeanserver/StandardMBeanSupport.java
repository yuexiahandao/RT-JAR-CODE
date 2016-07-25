/*    */ package com.sun.jmx.mbeanserver;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import javax.management.MBeanInfo;
/*    */ import javax.management.MBeanServer;
/*    */ import javax.management.NotCompliantMBeanException;
/*    */ import javax.management.ObjectName;
/*    */ 
/*    */ public class StandardMBeanSupport extends MBeanSupport<Method>
/*    */ {
/*    */   public <T> StandardMBeanSupport(T paramT, Class<T> paramClass)
/*    */     throws NotCompliantMBeanException
/*    */   {
/* 60 */     super(paramT, paramClass);
/*    */   }
/*    */ 
/*    */   MBeanIntrospector<Method> getMBeanIntrospector()
/*    */   {
/* 65 */     return StandardMBeanIntrospector.getInstance();
/*    */   }
/*    */ 
/*    */   Object getCookie()
/*    */   {
/* 70 */     return null;
/*    */   }
/*    */ 
/*    */   public void register(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void unregister()
/*    */   {
/*    */   }
/*    */ 
/*    */   public MBeanInfo getMBeanInfo()
/*    */   {
/* 85 */     MBeanInfo localMBeanInfo = super.getMBeanInfo();
/* 86 */     Class localClass = getResource().getClass();
/* 87 */     if (StandardMBeanIntrospector.isDefinitelyImmutableInfo(localClass))
/* 88 */       return localMBeanInfo;
/* 89 */     return new MBeanInfo(localMBeanInfo.getClassName(), localMBeanInfo.getDescription(), localMBeanInfo.getAttributes(), localMBeanInfo.getConstructors(), localMBeanInfo.getOperations(), MBeanIntrospector.findNotifications(getResource()), localMBeanInfo.getDescriptor());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.StandardMBeanSupport
 * JD-Core Version:    0.6.2
 */