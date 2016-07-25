/*     */ package com.sun.xml.internal.ws.server;
/*     */ 
/*     */ import com.sun.org.glassfish.gmbal.AMXClient;
/*     */ import com.sun.org.glassfish.gmbal.GmbalMBean;
/*     */ import com.sun.org.glassfish.gmbal.ManagedObjectManager;
/*     */ import com.sun.org.glassfish.gmbal.ManagedObjectManager.RegistrationDebugLevel;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ class RewritingMOM
/*     */   implements ManagedObjectManager
/*     */ {
/*     */   private final ManagedObjectManager mom;
/*     */   private static final String gmbalQuotingCharsRegex = "\n|\\|\"|\\*|\\?|:|=|,";
/*     */   private static final String jmxQuotingCharsRegex = ",|=|:|\"";
/*     */   private static final String replacementChar = "-";
/*     */ 
/*     */   RewritingMOM(ManagedObjectManager mom)
/*     */   {
/* 382 */     this.mom = mom;
/*     */   }
/*     */   private String rewrite(String x) {
/* 385 */     return x.replaceAll("\n|\\|\"|\\*|\\?|:|=|,", "-");
/*     */   }
/*     */ 
/*     */   public void suspendJMXRegistration()
/*     */   {
/* 390 */     this.mom.suspendJMXRegistration(); } 
/* 391 */   public void resumeJMXRegistration() { this.mom.resumeJMXRegistration(); } 
/* 392 */   public GmbalMBean createRoot() { return this.mom.createRoot(); } 
/* 393 */   public GmbalMBean createRoot(Object root) { return this.mom.createRoot(root); } 
/*     */   public GmbalMBean createRoot(Object root, String name) {
/* 395 */     return this.mom.createRoot(root, rewrite(name));
/*     */   }
/* 397 */   public Object getRoot() { return this.mom.getRoot(); } 
/*     */   public GmbalMBean register(Object parent, Object obj, String name) {
/* 399 */     return this.mom.register(parent, obj, rewrite(name));
/*     */   }
/* 401 */   public GmbalMBean register(Object parent, Object obj) { return this.mom.register(parent, obj); } 
/*     */   public GmbalMBean registerAtRoot(Object obj, String name) {
/* 403 */     return this.mom.registerAtRoot(obj, rewrite(name));
/*     */   }
/* 405 */   public GmbalMBean registerAtRoot(Object obj) { return this.mom.registerAtRoot(obj); } 
/* 406 */   public void unregister(Object obj) { this.mom.unregister(obj); } 
/* 407 */   public ObjectName getObjectName(Object obj) { return this.mom.getObjectName(obj); } 
/* 408 */   public AMXClient getAMXClient(Object obj) { return this.mom.getAMXClient(obj); } 
/* 409 */   public Object getObject(ObjectName oname) { return this.mom.getObject(oname); } 
/* 410 */   public void stripPrefix(String[] str) { this.mom.stripPrefix(str); } 
/* 411 */   public void stripPackagePrefix() { this.mom.stripPackagePrefix(); } 
/* 412 */   public String getDomain() { return this.mom.getDomain(); } 
/* 413 */   public void setMBeanServer(MBeanServer server) { this.mom.setMBeanServer(server); } 
/* 414 */   public MBeanServer getMBeanServer() { return this.mom.getMBeanServer(); } 
/* 415 */   public void setResourceBundle(ResourceBundle rb) { this.mom.setResourceBundle(rb); } 
/* 416 */   public ResourceBundle getResourceBundle() { return this.mom.getResourceBundle(); } 
/* 417 */   public void addAnnotation(AnnotatedElement element, Annotation annotation) { this.mom.addAnnotation(element, annotation); } 
/* 418 */   public void setRegistrationDebug(ManagedObjectManager.RegistrationDebugLevel level) { this.mom.setRegistrationDebug(level); } 
/* 419 */   public void setRuntimeDebug(boolean flag) { this.mom.setRuntimeDebug(flag); } 
/* 420 */   public void setTypelibDebug(int level) { this.mom.setTypelibDebug(level); } 
/* 421 */   public String dumpSkeleton(Object obj) { return this.mom.dumpSkeleton(obj); } 
/* 422 */   public void suppressDuplicateRootReport(boolean suppressReport) { this.mom.suppressDuplicateRootReport(suppressReport); } 
/* 423 */   public void close() throws IOException { this.mom.close(); } 
/* 424 */   public void setJMXRegistrationDebug(boolean x) { this.mom.setJMXRegistrationDebug(x); } 
/* 425 */   public boolean isManagedObject(Object x) { return this.mom.isManagedObject(x); }
/*     */ 
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.RewritingMOM
 * JD-Core Version:    0.6.2
 */