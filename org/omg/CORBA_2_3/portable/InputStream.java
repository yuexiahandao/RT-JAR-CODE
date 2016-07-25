/*     */ package org.omg.CORBA_2_3.portable;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.io.SerializablePermission;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import org.omg.CORBA.NO_IMPLEMENT;
/*     */ import org.omg.CORBA.portable.BoxedValueHelper;
/*     */ 
/*     */ public abstract class InputStream extends org.omg.CORBA.portable.InputStream
/*     */ {
/*     */   private static final String ALLOW_SUBCLASS_PROP = "jdk.corba.allowInputStreamSubclass";
/*  53 */   private static final boolean allowSubclass = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public Boolean run()
/*     */     {
/*  57 */       String str = System.getProperty("jdk.corba.allowInputStreamSubclass");
/*  58 */       return Boolean.valueOf(str != null);
/*     */     }
/*     */   })).booleanValue();
/*     */ 
/*     */   private static Void checkPermission()
/*     */   {
/*  64 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  65 */     if ((localSecurityManager != null) && 
/*  66 */       (!allowSubclass)) {
/*  67 */       localSecurityManager.checkPermission(new SerializablePermission("enableSubclassImplementation"));
/*     */     }
/*     */ 
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */   private InputStream(Void paramVoid)
/*     */   {
/*     */   }
/*     */ 
/*     */   public InputStream()
/*     */   {
/*  84 */     this(checkPermission());
/*     */   }
/*     */ 
/*     */   public Serializable read_value()
/*     */   {
/*  92 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public Serializable read_value(Class paramClass)
/*     */   {
/* 101 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public Serializable read_value(BoxedValueHelper paramBoxedValueHelper)
/*     */   {
/* 111 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public Serializable read_value(String paramString)
/*     */   {
/* 120 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public Serializable read_value(Serializable paramSerializable)
/*     */   {
/* 131 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public Object read_abstract_interface()
/*     */   {
/* 139 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public Object read_abstract_interface(Class paramClass)
/*     */   {
/* 149 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA_2_3.portable.InputStream
 * JD-Core Version:    0.6.2
 */