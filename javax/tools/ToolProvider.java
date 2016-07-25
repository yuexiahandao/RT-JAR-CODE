/*     */ package javax.tools;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class ToolProvider
/*     */ {
/*     */   private static final String propertyName = "sun.tools.ToolProvider";
/*     */   private static final String loggerName = "javax.tools";
/*     */   private static final String defaultJavaCompilerName = "com.sun.tools.javac.api.JavacTool";
/*     */   private static ToolProvider instance;
/* 135 */   private Map<String, Reference<Class<?>>> toolClasses = new HashMap();
/*     */ 
/* 139 */   private Reference<ClassLoader> refToolClassLoader = null;
/*     */ 
/* 168 */   private static final String[] defaultToolsLocation = { "lib", "tools.jar" };
/*     */ 
/*     */   static <T> T trace(Level paramLevel, Object paramObject)
/*     */   {
/*     */     try
/*     */     {
/*  63 */       if (System.getProperty("sun.tools.ToolProvider") != null) {
/*  64 */         StackTraceElement[] arrayOfStackTraceElement = Thread.currentThread().getStackTrace();
/*  65 */         String str1 = "???";
/*  66 */         String str2 = ToolProvider.class.getName();
/*  67 */         if (arrayOfStackTraceElement.length > 2) {
/*  68 */           localObject = arrayOfStackTraceElement[2];
/*  69 */           str1 = String.format((Locale)null, "%s(%s:%s)", new Object[] { ((StackTraceElement)localObject).getMethodName(), ((StackTraceElement)localObject).getFileName(), Integer.valueOf(((StackTraceElement)localObject).getLineNumber()) });
/*     */ 
/*  73 */           str2 = ((StackTraceElement)localObject).getClassName();
/*     */         }
/*  75 */         Object localObject = Logger.getLogger("javax.tools");
/*  76 */         if ((paramObject instanceof Throwable)) {
/*  77 */           ((Logger)localObject).logp(paramLevel, str2, str1, paramObject.getClass().getName(), (Throwable)paramObject);
/*     */         }
/*     */         else
/*  80 */           ((Logger)localObject).logp(paramLevel, str2, str1, String.valueOf(paramObject));
/*     */       }
/*     */     }
/*     */     catch (SecurityException localSecurityException) {
/*  84 */       System.err.format((Locale)null, "%s: %s; %s%n", new Object[] { ToolProvider.class.getName(), paramObject, localSecurityException.getLocalizedMessage() });
/*     */     }
/*     */ 
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   public static JavaCompiler getSystemJavaCompiler()
/*     */   {
/* 102 */     return (JavaCompiler)instance().getSystemTool(JavaCompiler.class, "com.sun.tools.javac.api.JavacTool");
/*     */   }
/*     */ 
/*     */   public static ClassLoader getSystemToolClassLoader()
/*     */   {
/*     */     try
/*     */     {
/* 116 */       Class localClass = instance().getSystemToolClass(JavaCompiler.class, "com.sun.tools.javac.api.JavacTool");
/*     */ 
/* 118 */       return localClass.getClassLoader();
/*     */     } catch (Throwable localThrowable) {
/* 120 */       return (ClassLoader)trace(Level.WARNING, localThrowable);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static synchronized ToolProvider instance()
/*     */   {
/* 128 */     if (instance == null)
/* 129 */       instance = new ToolProvider();
/* 130 */     return instance;
/*     */   }
/*     */ 
/*     */   private <T> T getSystemTool(Class<T> paramClass, String paramString)
/*     */   {
/* 145 */     Class localClass = getSystemToolClass(paramClass, paramString);
/*     */     try {
/* 147 */       return localClass.asSubclass(paramClass).newInstance();
/*     */     } catch (Throwable localThrowable) {
/* 149 */       trace(Level.WARNING, localThrowable);
/* 150 */     }return null;
/*     */   }
/*     */ 
/*     */   private <T> Class<? extends T> getSystemToolClass(Class<T> paramClass, String paramString)
/*     */   {
/* 155 */     Reference localReference = (Reference)this.toolClasses.get(paramString);
/* 156 */     Class localClass = localReference == null ? null : (Class)localReference.get();
/* 157 */     if (localClass == null) {
/*     */       try {
/* 159 */         localClass = findSystemToolClass(paramString);
/*     */       } catch (Throwable localThrowable) {
/* 161 */         return (Class)trace(Level.WARNING, localThrowable);
/*     */       }
/* 163 */       this.toolClasses.put(paramString, new WeakReference(localClass));
/*     */     }
/* 165 */     return localClass.asSubclass(paramClass);
/*     */   }
/*     */ 
/*     */   private Class<?> findSystemToolClass(String paramString)
/*     */     throws MalformedURLException, ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 175 */       return Class.forName(paramString, false, null);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 177 */       trace(Level.FINE, localClassNotFoundException);
/*     */ 
/* 180 */       Object localObject1 = this.refToolClassLoader == null ? null : (ClassLoader)this.refToolClassLoader.get();
/* 181 */       if (localObject1 == null) {
/* 182 */         File localFile = new File(System.getProperty("java.home"));
/* 183 */         if (localFile.getName().equalsIgnoreCase("jre"))
/* 184 */           localFile = localFile.getParentFile();
/* 185 */         for (String str : defaultToolsLocation) {
/* 186 */           localFile = new File(localFile, str);
/*     */         }
/*     */ 
/* 190 */         if (!localFile.exists()) {
/* 191 */           throw localClassNotFoundException;
/*     */         }
/* 193 */         ??? = new URL[] { localFile.toURI().toURL() };
/* 194 */         trace(Level.FINE, ???[0].toString());
/*     */ 
/* 196 */         localObject1 = URLClassLoader.newInstance((URL[])???);
/* 197 */         this.refToolClassLoader = new WeakReference(localObject1);
/*     */       }
/*     */ 
/* 200 */       return Class.forName(paramString, false, (ClassLoader)localObject1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.tools.ToolProvider
 * JD-Core Version:    0.6.2
 */