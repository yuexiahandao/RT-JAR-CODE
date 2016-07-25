/*     */ package com.sun.org.glassfish.gmbal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ class ManagedObjectManagerNOPImpl
/*     */   implements ManagedObjectManager
/*     */ {
/*  49 */   static final ManagedObjectManager self = new ManagedObjectManagerNOPImpl();
/*     */ 
/*  51 */   private static final GmbalMBean gmb = new GmbalMBeanNOPImpl();
/*     */ 
/*     */   public void suspendJMXRegistration()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void resumeJMXRegistration()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isManagedObject(Object obj)
/*     */   {
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */   public GmbalMBean createRoot() {
/*  69 */     return gmb;
/*     */   }
/*     */ 
/*     */   public GmbalMBean createRoot(Object root) {
/*  73 */     return gmb;
/*     */   }
/*     */ 
/*     */   public GmbalMBean createRoot(Object root, String name) {
/*  77 */     return gmb;
/*     */   }
/*     */ 
/*     */   public Object getRoot() {
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   public GmbalMBean register(Object parent, Object obj, String name) {
/*  85 */     return gmb;
/*     */   }
/*     */ 
/*     */   public GmbalMBean register(Object parent, Object obj) {
/*  89 */     return gmb;
/*     */   }
/*     */ 
/*     */   public GmbalMBean registerAtRoot(Object obj, String name) {
/*  93 */     return gmb;
/*     */   }
/*     */ 
/*     */   public GmbalMBean registerAtRoot(Object obj) {
/*  97 */     return gmb;
/*     */   }
/*     */ 
/*     */   public void unregister(Object obj)
/*     */   {
/*     */   }
/*     */ 
/*     */   public ObjectName getObjectName(Object obj) {
/* 105 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getObject(ObjectName oname) {
/* 109 */     return null;
/*     */   }
/*     */ 
/*     */   public void stripPrefix(String[] str)
/*     */   {
/*     */   }
/*     */ 
/*     */   public String getDomain() {
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   public void setMBeanServer(MBeanServer server)
/*     */   {
/*     */   }
/*     */ 
/*     */   public MBeanServer getMBeanServer() {
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */   public void setResourceBundle(ResourceBundle rb)
/*     */   {
/*     */   }
/*     */ 
/*     */   public ResourceBundle getResourceBundle() {
/* 133 */     return null;
/*     */   }
/*     */ 
/*     */   public void addAnnotation(AnnotatedElement element, Annotation annotation)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setRegistrationDebug(ManagedObjectManager.RegistrationDebugLevel level)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setRuntimeDebug(boolean flag)
/*     */   {
/*     */   }
/*     */ 
/*     */   public String dumpSkeleton(Object obj) {
/* 149 */     return "";
/*     */   }
/*     */ 
/*     */   public void close() throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setTypelibDebug(int level)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void stripPackagePrefix()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void suppressDuplicateRootReport(boolean suppressReport)
/*     */   {
/*     */   }
/*     */ 
/*     */   public AMXClient getAMXClient(Object obj) {
/* 169 */     return null;
/*     */   }
/*     */ 
/*     */   public void setJMXRegistrationDebug(boolean flag)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.gmbal.ManagedObjectManagerNOPImpl
 * JD-Core Version:    0.6.2
 */