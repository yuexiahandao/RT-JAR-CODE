/*    */ package com.sun.org.glassfish.gmbal;
/*    */ 
/*    */ import javax.management.Attribute;
/*    */ import javax.management.AttributeList;
/*    */ import javax.management.AttributeNotFoundException;
/*    */ import javax.management.InvalidAttributeValueException;
/*    */ import javax.management.ListenerNotFoundException;
/*    */ import javax.management.MBeanException;
/*    */ import javax.management.MBeanInfo;
/*    */ import javax.management.MBeanNotificationInfo;
/*    */ import javax.management.NotificationFilter;
/*    */ import javax.management.NotificationListener;
/*    */ import javax.management.ReflectionException;
/*    */ 
/*    */ public class GmbalMBeanNOPImpl
/*    */   implements GmbalMBean
/*    */ {
/*    */   public Object getAttribute(String attribute)
/*    */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*    */   {
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */   public void setAttribute(Attribute attribute)
/*    */     throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
/*    */   {
/*    */   }
/*    */ 
/*    */   public AttributeList getAttributes(String[] attributes)
/*    */   {
/* 61 */     return null;
/*    */   }
/*    */ 
/*    */   public AttributeList setAttributes(AttributeList attributes) {
/* 65 */     return null;
/*    */   }
/*    */ 
/*    */   public Object invoke(String actionName, Object[] params, String[] signature)
/*    */     throws MBeanException, ReflectionException
/*    */   {
/* 71 */     return null;
/*    */   }
/*    */ 
/*    */   public MBeanInfo getMBeanInfo() {
/* 75 */     return null;
/*    */   }
/*    */ 
/*    */   public void removeNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback)
/*    */     throws ListenerNotFoundException
/*    */   {
/*    */   }
/*    */ 
/*    */   public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback)
/*    */     throws IllegalArgumentException
/*    */   {
/*    */   }
/*    */ 
/*    */   public void removeNotificationListener(NotificationListener listener)
/*    */     throws ListenerNotFoundException
/*    */   {
/*    */   }
/*    */ 
/*    */   public MBeanNotificationInfo[] getNotificationInfo()
/*    */   {
/* 98 */     return new MBeanNotificationInfo[0];
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.gmbal.GmbalMBeanNOPImpl
 * JD-Core Version:    0.6.2
 */