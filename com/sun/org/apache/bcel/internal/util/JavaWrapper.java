/*     */ package com.sun.org.apache.bcel.internal.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ public class JavaWrapper
/*     */ {
/*     */   private ClassLoader loader;
/*     */ 
/*     */   private static ClassLoader getClassLoader()
/*     */   {
/*  83 */     String s = SecuritySupport.getSystemProperty("bcel.classloader");
/*     */ 
/*  85 */     if ((s == null) || ("".equals(s)))
/*  86 */       s = "com.sun.org.apache.bcel.internal.util.ClassLoader";
/*     */     try
/*     */     {
/*  89 */       return (ClassLoader)Class.forName(s).newInstance();
/*     */     } catch (Exception e) {
/*  91 */       throw new RuntimeException(e.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public JavaWrapper(ClassLoader loader) {
/*  96 */     this.loader = loader;
/*     */   }
/*     */ 
/*     */   public JavaWrapper() {
/* 100 */     this(getClassLoader());
/*     */   }
/*     */ 
/*     */   public void runMain(String class_name, String[] argv)
/*     */     throws ClassNotFoundException
/*     */   {
/* 110 */     Class cl = this.loader.loadClass(class_name);
/* 111 */     Method method = null;
/*     */     try
/*     */     {
/* 114 */       method = cl.getMethod("_main", new Class[] { argv.getClass() });
/*     */ 
/* 118 */       int m = method.getModifiers();
/* 119 */       Class r = method.getReturnType();
/*     */ 
/* 121 */       if ((!Modifier.isPublic(m)) || (!Modifier.isStatic(m)) || (Modifier.isAbstract(m)) || (r != Void.TYPE))
/*     */       {
/* 123 */         throw new NoSuchMethodException();
/*     */       }
/*     */     } catch (NoSuchMethodException no) { System.out.println("In class " + class_name + ": public static void _main(String[] argv) is not defined");
/*     */ 
/* 127 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 131 */       method.invoke(null, new Object[] { argv });
/*     */     } catch (Exception ex) {
/* 133 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void _main(String[] argv)
/*     */     throws Exception
/*     */   {
/* 143 */     if (argv.length == 0) {
/* 144 */       System.out.println("Missing class name.");
/* 145 */       return;
/*     */     }
/*     */ 
/* 148 */     String class_name = argv[0];
/* 149 */     String[] new_argv = new String[argv.length - 1];
/* 150 */     System.arraycopy(argv, 1, new_argv, 0, new_argv.length);
/*     */ 
/* 152 */     JavaWrapper wrapper = new JavaWrapper();
/* 153 */     wrapper.runMain(class_name, new_argv);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.JavaWrapper
 * JD-Core Version:    0.6.2
 */