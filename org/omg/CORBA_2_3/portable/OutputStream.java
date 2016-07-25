/*     */ package org.omg.CORBA_2_3.portable;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.io.SerializablePermission;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import org.omg.CORBA.NO_IMPLEMENT;
/*     */ import org.omg.CORBA.portable.BoxedValueHelper;
/*     */ 
/*     */ public abstract class OutputStream extends org.omg.CORBA.portable.OutputStream
/*     */ {
/*     */   private static final String ALLOW_SUBCLASS_PROP = "jdk.corba.allowOutputStreamSubclass";
/*  51 */   private static final boolean allowSubclass = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public Boolean run()
/*     */     {
/*  55 */       String str = System.getProperty("jdk.corba.allowOutputStreamSubclass");
/*  56 */       return Boolean.valueOf(str != null);
/*     */     }
/*     */   })).booleanValue();
/*     */ 
/*     */   private static Void checkPermission()
/*     */   {
/*  62 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  63 */     if ((localSecurityManager != null) && 
/*  64 */       (!allowSubclass)) {
/*  65 */       localSecurityManager.checkPermission(new SerializablePermission("enableSubclassImplementation"));
/*     */     }
/*     */ 
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */   private OutputStream(Void paramVoid)
/*     */   {
/*     */   }
/*     */ 
/*     */   public OutputStream()
/*     */   {
/*  81 */     this(checkPermission());
/*     */   }
/*     */ 
/*     */   public void write_value(Serializable paramSerializable)
/*     */   {
/*  89 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public void write_value(Serializable paramSerializable, Class paramClass)
/*     */   {
/*  98 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public void write_value(Serializable paramSerializable, String paramString)
/*     */   {
/* 108 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public void write_value(Serializable paramSerializable, BoxedValueHelper paramBoxedValueHelper)
/*     */   {
/* 118 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public void write_abstract_interface(Object paramObject)
/*     */   {
/* 126 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA_2_3.portable.OutputStream
 * JD-Core Version:    0.6.2
 */