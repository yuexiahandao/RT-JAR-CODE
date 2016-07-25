/*     */ package javax.sql.rowset.serial;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Vector;
/*     */ import javax.sql.rowset.RowSetWarning;
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.Reflection;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class SerialJavaObject
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private final Object obj;
/*     */   private transient Field[] fields;
/*     */   static final long serialVersionUID = -1465795139032831023L;
/*     */   Vector chain;
/*     */ 
/*     */   public SerialJavaObject(Object paramObject)
/*     */     throws SerialException
/*     */   {
/*  77 */     Class localClass = paramObject.getClass();
/*     */ 
/*  80 */     if (!(paramObject instanceof Cloneable)) {
/*  81 */       setWarning(new RowSetWarning("Warning, the object passed to the constructor does not implement Serializable"));
/*     */     }
/*     */ 
/*  89 */     int i = 0;
/*  90 */     this.fields = localClass.getFields();
/*     */ 
/*  92 */     for (int j = 0; j < this.fields.length; j++) {
/*  93 */       if (this.fields[j].getModifiers() == 8) {
/*  94 */         i = 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  99 */     if (i != 0) {
/* 100 */       throw new SerialException("Located static fields in object instance. Cannot serialize");
/*     */     }
/*     */ 
/* 104 */     this.obj = paramObject;
/*     */   }
/*     */ 
/*     */   public Object getObject()
/*     */     throws SerialException
/*     */   {
/* 116 */     return this.obj;
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public Field[] getFields()
/*     */     throws SerialException
/*     */   {
/* 130 */     if (this.fields != null) {
/* 131 */       Class localClass1 = this.obj.getClass();
/* 132 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 133 */       if (localSecurityManager != null)
/*     */       {
/* 138 */         Class localClass2 = Reflection.getCallerClass();
/* 139 */         if (ReflectUtil.needsPackageAccessCheck(localClass2.getClassLoader(), localClass1.getClassLoader()))
/*     */         {
/* 141 */           ReflectUtil.checkPackageAccess(localClass1);
/*     */         }
/*     */       }
/* 144 */       return localClass1.getFields();
/*     */     }
/* 146 */     throw new SerialException("SerialJavaObject does not contain a serialized object instance");
/*     */   }
/*     */ 
/*     */   private void setWarning(RowSetWarning paramRowSetWarning)
/*     */   {
/* 168 */     if (this.chain == null) {
/* 169 */       this.chain = new Vector();
/*     */     }
/* 171 */     this.chain.add(paramRowSetWarning);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.serial.SerialJavaObject
 * JD-Core Version:    0.6.2
 */