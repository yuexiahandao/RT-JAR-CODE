/*     */ package com.sun.xml.internal.org.jvnet.mimepull;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class TempFiles
/*     */ {
/*  41 */   private static final Logger LOGGER = Logger.getLogger(TempFiles.class.getName());
/*     */ 
/*  58 */   private static final Class<?> CLASS_FILES = safeGetClass("java.nio.file.Files");
/*  59 */   private static final Class<?> CLASS_PATH = safeGetClass("java.nio.file.Path");
/*  60 */   private static final Class<?> CLASS_FILE_ATTRIBUTE = safeGetClass("java.nio.file.attribute.FileAttribute");
/*  61 */   private static final Class<?> CLASS_FILE_ATTRIBUTES = safeGetClass("[Ljava.nio.file.attribute.FileAttribute;");
/*  62 */   private static final Method METHOD_FILE_TO_PATH = safeGetMethod(File.class, "toPath", new Class[0]);
/*  63 */   private static final Method METHOD_FILES_CREATE_TEMP_FILE = safeGetMethod(CLASS_FILES, "createTempFile", new Class[] { String.class, String.class, CLASS_FILE_ATTRIBUTES });
/*  64 */   private static final Method METHOD_FILES_CREATE_TEMP_FILE_WITHPATH = safeGetMethod(CLASS_FILES, "createTempFile", new Class[] { CLASS_PATH, String.class, String.class, CLASS_FILE_ATTRIBUTES });
/*  65 */   private static final Method METHOD_PATH_TO_FILE = safeGetMethod(CLASS_PATH, "toFile", new Class[0]);
/*     */ 
/*  56 */   private static boolean useJdk6API = isJdk6();
/*     */ 
/*     */   private static boolean isJdk6()
/*     */   {
/*  69 */     String javaVersion = System.getProperty("java.version");
/*  70 */     LOGGER.log(Level.FINEST, "Detected java version = {0}", javaVersion);
/*  71 */     return javaVersion.startsWith("1.6.");
/*     */   }
/*     */ 
/*     */   private static Class<?> safeGetClass(String className)
/*     */   {
/*  76 */     if (useJdk6API) return null; try
/*     */     {
/*  78 */       return Class.forName(className);
/*     */     } catch (ClassNotFoundException e) {
/*  80 */       LOGGER.log(Level.SEVERE, "Exception cought", e);
/*  81 */       LOGGER.log(Level.WARNING, "Class {0} not found. Temp files will be created using old java.io API.", className);
/*  82 */       useJdk6API = true;
/*  83 */     }return null;
/*     */   }
/*     */ 
/*     */   private static Method safeGetMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes)
/*     */   {
/*  89 */     if (useJdk6API) return null; try
/*     */     {
/*  91 */       return clazz.getMethod(methodName, parameterTypes);
/*     */     } catch (NoSuchMethodException e) {
/*  93 */       LOGGER.log(Level.SEVERE, "Exception cought", e);
/*  94 */       LOGGER.log(Level.WARNING, "Method {0} not found. Temp files will be created using old java.io API.", methodName);
/*  95 */       useJdk6API = true;
/*  96 */     }return null;
/*     */   }
/*     */ 
/*     */   static Object toPath(File f)
/*     */     throws InvocationTargetException, IllegalAccessException
/*     */   {
/* 102 */     return METHOD_FILE_TO_PATH.invoke(f, new Object[0]);
/*     */   }
/*     */ 
/*     */   static File toFile(Object path) throws InvocationTargetException, IllegalAccessException {
/* 106 */     return (File)METHOD_PATH_TO_FILE.invoke(path, new Object[0]);
/*     */   }
/*     */ 
/*     */   static File createTempFile(String prefix, String suffix, File dir) throws IOException
/*     */   {
/* 111 */     if (useJdk6API) {
/* 112 */       LOGGER.log(Level.FINEST, "Jdk6 detected, temp file (prefix:{0}, suffix:{1}) being created using old java.io API.", new Object[] { prefix, suffix });
/* 113 */       return File.createTempFile(prefix, suffix, dir);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 118 */       if (dir != null) {
/* 119 */         Object path = toPath(dir);
/* 120 */         LOGGER.log(Level.FINEST, "Temp file (path: {0}, prefix:{1}, suffix:{2}) being created using NIO API.", new Object[] { dir.getAbsolutePath(), prefix, suffix });
/* 121 */         return toFile(METHOD_FILES_CREATE_TEMP_FILE_WITHPATH.invoke(null, new Object[] { path, prefix, suffix, Array.newInstance(CLASS_FILE_ATTRIBUTE, 0) }));
/*     */       }
/* 123 */       LOGGER.log(Level.FINEST, "Temp file (prefix:{0}, suffix:{1}) being created using NIO API.", new Object[] { prefix, suffix });
/* 124 */       return toFile(METHOD_FILES_CREATE_TEMP_FILE.invoke(null, new Object[] { prefix, suffix, Array.newInstance(CLASS_FILE_ATTRIBUTE, 0) }));
/*     */     }
/*     */     catch (IllegalAccessException e)
/*     */     {
/* 128 */       LOGGER.log(Level.SEVERE, "Exception caught", e);
/* 129 */       LOGGER.log(Level.WARNING, "Error invoking java.nio API, temp file (path: {0}, prefix:{1}, suffix:{2}) being created using old java.io API.", new Object[] { dir != null ? dir.getAbsolutePath() : null, prefix, suffix });
/*     */ 
/* 131 */       return File.createTempFile(prefix, suffix, dir);
/*     */     }
/*     */     catch (InvocationTargetException e) {
/* 134 */       LOGGER.log(Level.SEVERE, "Exception caught", e);
/* 135 */       LOGGER.log(Level.WARNING, "Error invoking java.nio API, temp file (path: {0}, prefix:{1}, suffix:{2}) being created using old java.io API.", new Object[] { dir != null ? dir.getAbsolutePath() : null, prefix, suffix });
/*     */     }
/* 137 */     return File.createTempFile(prefix, suffix, dir);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.TempFiles
 * JD-Core Version:    0.6.2
 */