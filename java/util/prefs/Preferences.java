/*      */ package java.util.prefs;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.security.AccessController;
/*      */ import java.security.AllPermission;
/*      */ import java.security.Permission;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Iterator;
/*      */ import java.util.ServiceConfigurationError;
/*      */ import java.util.ServiceLoader;
/*      */ 
/*      */ public abstract class Preferences
/*      */ {
/*  226 */   private static final PreferencesFactory factory = factory();
/*      */   public static final int MAX_KEY_LENGTH = 80;
/*      */   public static final int MAX_VALUE_LENGTH = 8192;
/*      */   public static final int MAX_NAME_LENGTH = 80;
/*  442 */   private static Permission prefsPerm = new RuntimePermission("preferences");
/*      */ 
/*      */   private static PreferencesFactory factory()
/*      */   {
/*  230 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public String run() {
/*  233 */         return System.getProperty("java.util.prefs.PreferencesFactory");
/*      */       }
/*      */     });
/*  235 */     if (str != null)
/*      */     {
/*      */       try
/*      */       {
/*  241 */         return (PreferencesFactory)Class.forName(str, false, ClassLoader.getSystemClassLoader()).newInstance();
/*      */       }
/*      */       catch (Exception localException1)
/*      */       {
/*      */         try
/*      */         {
/*  249 */           SecurityManager localSecurityManager = System.getSecurityManager();
/*  250 */           if (localSecurityManager != null) {
/*  251 */             localSecurityManager.checkPermission(new AllPermission());
/*      */           }
/*  253 */           return (PreferencesFactory)Class.forName(str, false, Thread.currentThread().getContextClassLoader()).newInstance();
/*      */         }
/*      */         catch (Exception localException2)
/*      */         {
/*  259 */           InternalError localInternalError = new InternalError("Can't instantiate Preferences factory " + str);
/*      */ 
/*  262 */           localInternalError.initCause(localException2);
/*  263 */           throw localInternalError;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  268 */     return (PreferencesFactory)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public PreferencesFactory run() {
/*  271 */         return Preferences.access$000();
/*      */       } } );
/*      */   }
/*      */ 
/*      */   private static PreferencesFactory factory1() {
/*  276 */     Iterator localIterator = ServiceLoader.load(PreferencesFactory.class, ClassLoader.getSystemClassLoader()).iterator();
/*      */ 
/*  281 */     while (localIterator.hasNext()) {
/*      */       try {
/*  283 */         return (PreferencesFactory)localIterator.next(); } catch (ServiceConfigurationError localServiceConfigurationError) {
/*      */       }
/*  285 */       if (!(localServiceConfigurationError.getCause() instanceof SecurityException))
/*      */       {
/*  289 */         throw localServiceConfigurationError;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  294 */     String str1 = System.getProperty("os.name");
/*      */     String str2;
/*  296 */     if (str1.startsWith("Windows"))
/*  297 */       str2 = "java.util.prefs.WindowsPreferencesFactory";
/*  298 */     else if (str1.contains("OS X"))
/*  299 */       str2 = "java.util.prefs.MacOSXPreferencesFactory";
/*      */     else
/*  301 */       str2 = "java.util.prefs.FileSystemPreferencesFactory";
/*      */     try
/*      */     {
/*  304 */       return (PreferencesFactory)Class.forName(str2, false, null).newInstance();
/*      */     }
/*      */     catch (Exception localException) {
/*  307 */       InternalError localInternalError = new InternalError("Can't instantiate platform default Preferences factory " + str2);
/*      */ 
/*  310 */       localInternalError.initCause(localException);
/*  311 */       throw localInternalError;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static Preferences userNodeForPackage(Class<?> paramClass)
/*      */   {
/*  371 */     return userRoot().node(nodeName(paramClass));
/*      */   }
/*      */ 
/*      */   public static Preferences systemNodeForPackage(Class<?> paramClass)
/*      */   {
/*  415 */     return systemRoot().node(nodeName(paramClass));
/*      */   }
/*      */ 
/*      */   private static String nodeName(Class paramClass)
/*      */   {
/*  426 */     if (paramClass.isArray()) {
/*  427 */       throw new IllegalArgumentException("Arrays have no associated preferences node.");
/*      */     }
/*  429 */     String str1 = paramClass.getName();
/*  430 */     int i = str1.lastIndexOf('.');
/*  431 */     if (i < 0)
/*  432 */       return "/<unnamed>";
/*  433 */     String str2 = str1.substring(0, i);
/*  434 */     return "/" + str2.replace('.', '/');
/*      */   }
/*      */ 
/*      */   public static Preferences userRoot()
/*      */   {
/*  453 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  454 */     if (localSecurityManager != null) {
/*  455 */       localSecurityManager.checkPermission(prefsPerm);
/*      */     }
/*  457 */     return factory.userRoot();
/*      */   }
/*      */ 
/*      */   public static Preferences systemRoot()
/*      */   {
/*  469 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  470 */     if (localSecurityManager != null) {
/*  471 */       localSecurityManager.checkPermission(prefsPerm);
/*      */     }
/*  473 */     return factory.systemRoot();
/*      */   }
/*      */ 
/*      */   public abstract void put(String paramString1, String paramString2);
/*      */ 
/*      */   public abstract String get(String paramString1, String paramString2);
/*      */ 
/*      */   public abstract void remove(String paramString);
/*      */ 
/*      */   public abstract void clear()
/*      */     throws BackingStoreException;
/*      */ 
/*      */   public abstract void putInt(String paramString, int paramInt);
/*      */ 
/*      */   public abstract int getInt(String paramString, int paramInt);
/*      */ 
/*      */   public abstract void putLong(String paramString, long paramLong);
/*      */ 
/*      */   public abstract long getLong(String paramString, long paramLong);
/*      */ 
/*      */   public abstract void putBoolean(String paramString, boolean paramBoolean);
/*      */ 
/*      */   public abstract boolean getBoolean(String paramString, boolean paramBoolean);
/*      */ 
/*      */   public abstract void putFloat(String paramString, float paramFloat);
/*      */ 
/*      */   public abstract float getFloat(String paramString, float paramFloat);
/*      */ 
/*      */   public abstract void putDouble(String paramString, double paramDouble);
/*      */ 
/*      */   public abstract double getDouble(String paramString, double paramDouble);
/*      */ 
/*      */   public abstract void putByteArray(String paramString, byte[] paramArrayOfByte);
/*      */ 
/*      */   public abstract byte[] getByteArray(String paramString, byte[] paramArrayOfByte);
/*      */ 
/*      */   public abstract String[] keys()
/*      */     throws BackingStoreException;
/*      */ 
/*      */   public abstract String[] childrenNames()
/*      */     throws BackingStoreException;
/*      */ 
/*      */   public abstract Preferences parent();
/*      */ 
/*      */   public abstract Preferences node(String paramString);
/*      */ 
/*      */   public abstract boolean nodeExists(String paramString)
/*      */     throws BackingStoreException;
/*      */ 
/*      */   public abstract void removeNode()
/*      */     throws BackingStoreException;
/*      */ 
/*      */   public abstract String name();
/*      */ 
/*      */   public abstract String absolutePath();
/*      */ 
/*      */   public abstract boolean isUserNode();
/*      */ 
/*      */   public abstract String toString();
/*      */ 
/*      */   public abstract void flush()
/*      */     throws BackingStoreException;
/*      */ 
/*      */   public abstract void sync()
/*      */     throws BackingStoreException;
/*      */ 
/*      */   public abstract void addPreferenceChangeListener(PreferenceChangeListener paramPreferenceChangeListener);
/*      */ 
/*      */   public abstract void removePreferenceChangeListener(PreferenceChangeListener paramPreferenceChangeListener);
/*      */ 
/*      */   public abstract void addNodeChangeListener(NodeChangeListener paramNodeChangeListener);
/*      */ 
/*      */   public abstract void removeNodeChangeListener(NodeChangeListener paramNodeChangeListener);
/*      */ 
/*      */   public abstract void exportNode(OutputStream paramOutputStream)
/*      */     throws IOException, BackingStoreException;
/*      */ 
/*      */   public abstract void exportSubtree(OutputStream paramOutputStream)
/*      */     throws IOException, BackingStoreException;
/*      */ 
/*      */   public static void importPreferences(InputStream paramInputStream)
/*      */     throws IOException, InvalidPreferencesFormatException
/*      */   {
/* 1259 */     XmlSupport.importPreferences(paramInputStream);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.prefs.Preferences
 * JD-Core Version:    0.6.2
 */