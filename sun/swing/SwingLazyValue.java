/*     */ package sun.swing;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIDefaults.LazyValue;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class SwingLazyValue
/*     */   implements UIDefaults.LazyValue
/*     */ {
/*     */   private String className;
/*     */   private String methodName;
/*     */   private Object[] args;
/*     */ 
/*     */   public SwingLazyValue(String paramString)
/*     */   {
/*  49 */     this(paramString, (String)null);
/*     */   }
/*     */   public SwingLazyValue(String paramString1, String paramString2) {
/*  52 */     this(paramString1, paramString2, null);
/*     */   }
/*     */   public SwingLazyValue(String paramString, Object[] paramArrayOfObject) {
/*  55 */     this(paramString, null, paramArrayOfObject);
/*     */   }
/*     */   public SwingLazyValue(String paramString1, String paramString2, Object[] paramArrayOfObject) {
/*  58 */     this.className = paramString1;
/*  59 */     this.methodName = paramString2;
/*  60 */     if (paramArrayOfObject != null)
/*  61 */       this.args = ((Object[])paramArrayOfObject.clone());
/*     */   }
/*     */ 
/*     */   public Object createValue(UIDefaults paramUIDefaults)
/*     */   {
/*     */     try {
/*  67 */       ReflectUtil.checkPackageAccess(this.className);
/*  68 */       Class localClass = Class.forName(this.className, true, null);
/*  69 */       if (this.methodName != null) {
/*  70 */         arrayOfClass = getClassArray(this.args);
/*  71 */         localObject = localClass.getMethod(this.methodName, arrayOfClass);
/*  72 */         makeAccessible((AccessibleObject)localObject);
/*  73 */         return ((Method)localObject).invoke(localClass, this.args);
/*     */       }
/*  75 */       Class[] arrayOfClass = getClassArray(this.args);
/*  76 */       Object localObject = localClass.getConstructor(arrayOfClass);
/*  77 */       makeAccessible((AccessibleObject)localObject);
/*  78 */       return ((Constructor)localObject).newInstance(this.args);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   private void makeAccessible(final AccessibleObject paramAccessibleObject) {
/*  91 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Void run() {
/*  93 */         paramAccessibleObject.setAccessible(true);
/*  94 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private Class[] getClassArray(Object[] paramArrayOfObject) {
/* 100 */     Class[] arrayOfClass = null;
/* 101 */     if (paramArrayOfObject != null) {
/* 102 */       arrayOfClass = new Class[paramArrayOfObject.length];
/* 103 */       for (int i = 0; i < paramArrayOfObject.length; i++)
/*     */       {
/* 107 */         if ((paramArrayOfObject[i] instanceof Integer))
/* 108 */           arrayOfClass[i] = Integer.TYPE;
/* 109 */         else if ((paramArrayOfObject[i] instanceof Boolean))
/* 110 */           arrayOfClass[i] = Boolean.TYPE;
/* 111 */         else if ((paramArrayOfObject[i] instanceof ColorUIResource))
/*     */         {
/* 120 */           arrayOfClass[i] = Color.class;
/*     */         }
/* 122 */         else arrayOfClass[i] = paramArrayOfObject[i].getClass();
/*     */       }
/*     */     }
/*     */ 
/* 126 */     return arrayOfClass;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.SwingLazyValue
 * JD-Core Version:    0.6.2
 */