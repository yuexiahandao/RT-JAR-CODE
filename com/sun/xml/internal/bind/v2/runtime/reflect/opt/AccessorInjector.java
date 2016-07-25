/*     */ package com.sun.xml.internal.bind.v2.runtime.reflect.opt;
/*     */ 
/*     */ import com.sun.xml.internal.bind.Util;
/*     */ import com.sun.xml.internal.bind.v2.bytecode.ClassTailor;
/*     */ import java.io.InputStream;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class AccessorInjector
/*     */ {
/*  40 */   private static final Logger logger = Util.getClassLogger();
/*     */ 
/*  42 */   protected static final boolean noOptimize = Util.getSystemProperty(ClassTailor.class.getName() + ".noOptimize") != null;
/*     */ 
/* 114 */   private static final ClassLoader CLASS_LOADER = AccessorInjector.class.getClassLoader();
/*     */ 
/*     */   public static Class<?> prepare(Class beanClass, String templateClassName, String newClassName, String[] replacements)
/*     */   {
/*  59 */     if (noOptimize)
/*  60 */       return null;
/*     */     try
/*     */     {
/*  63 */       ClassLoader cl = beanClass.getClassLoader();
/*  64 */       if (cl == null) return null;
/*     */ 
/*  66 */       Class c = null;
/*  67 */       synchronized (AccessorInjector.class) {
/*  68 */         c = Injector.find(cl, newClassName);
/*  69 */         if (c == null) {
/*  70 */           byte[] image = tailor(templateClassName, newClassName, replacements);
/*     */ 
/*  76 */           if (image == null)
/*  77 */             return null;
/*  78 */           c = Injector.inject(cl, newClassName, image);
/*     */         }
/*     */       }
/*  81 */       return c;
/*     */     }
/*     */     catch (SecurityException e) {
/*  84 */       logger.log(Level.INFO, "Unable to create an optimized TransducedAccessor ", e);
/*  85 */     }return null;
/*     */   }
/*     */ 
/*     */   private static byte[] tailor(String templateClassName, String newClassName, String[] replacements)
/*     */   {
/*     */     InputStream resource;
/*     */     InputStream resource;
/* 104 */     if (CLASS_LOADER != null)
/* 105 */       resource = CLASS_LOADER.getResourceAsStream(templateClassName + ".class");
/*     */     else
/* 107 */       resource = ClassLoader.getSystemResourceAsStream(templateClassName + ".class");
/* 108 */     if (resource == null) {
/* 109 */       return null;
/*     */     }
/* 111 */     return ClassTailor.tailor(resource, templateClassName, newClassName, replacements);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  46 */     if (noOptimize)
/*  47 */       logger.info("The optimized code generation is disabled");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.opt.AccessorInjector
 * JD-Core Version:    0.6.2
 */