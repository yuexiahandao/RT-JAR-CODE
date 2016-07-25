/*     */ package sun.awt;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.StringBufferInputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import java.util.Vector;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ final class DebugSettings
/*     */ {
/*  75 */   private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.debug.DebugSettings");
/*     */   static final String PREFIX = "awtdebug";
/*     */   static final String PROP_FILE = "properties";
/*  82 */   private static final String[] DEFAULT_PROPS = { "awtdebug.assert=true", "awtdebug.trace=false", "awtdebug.on=true", "awtdebug.ctrace=false" };
/*     */ 
/*  90 */   private static DebugSettings instance = null;
/*     */ 
/*  92 */   private Properties props = new Properties();
/*     */   private static final String PROP_CTRACE = "ctrace";
/* 267 */   private static final int PROP_CTRACE_LEN = "ctrace".length();
/*     */ 
/*     */   static void init()
/*     */   {
/*  95 */     if (instance != null) {
/*  96 */       return;
/*     */     }
/*     */ 
/*  99 */     NativeLibLoader.loadLibraries();
/* 100 */     instance = new DebugSettings();
/* 101 */     instance.loadNativeSettings();
/*     */   }
/*     */ 
/*     */   private DebugSettings() {
/* 105 */     new PrivilegedAction() {
/*     */       public Object run() {
/* 107 */         DebugSettings.this.loadProperties();
/* 108 */         return null;
/*     */       }
/*     */     }
/* 105 */     .run();
/*     */   }
/*     */ 
/*     */   private synchronized void loadProperties()
/*     */   {
/* 119 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/* 123 */         DebugSettings.this.loadDefaultProperties();
/* 124 */         DebugSettings.this.loadFileProperties();
/* 125 */         DebugSettings.this.loadSystemProperties();
/* 126 */         return null;
/*     */       }
/*     */     });
/* 131 */     if (log.isLoggable(500))
/* 132 */       log.fine("DebugSettings:\n{0}" + this);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 137 */     Enumeration localEnumeration = this.props.propertyNames();
/* 138 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 139 */     PrintStream localPrintStream = new PrintStream(localByteArrayOutputStream);
/*     */ 
/* 141 */     while (localEnumeration.hasMoreElements()) {
/* 142 */       String str1 = (String)localEnumeration.nextElement();
/* 143 */       String str2 = this.props.getProperty(str1, "");
/* 144 */       localPrintStream.println(str1 + " = " + str2);
/*     */     }
/* 146 */     return new String(localByteArrayOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   private void loadDefaultProperties()
/*     */   {
/*     */     try
/*     */     {
/* 156 */       for (int i = 0; i < DEFAULT_PROPS.length; i++) {
/* 157 */         StringBufferInputStream localStringBufferInputStream = new StringBufferInputStream(DEFAULT_PROPS[i]);
/* 158 */         this.props.load(localStringBufferInputStream);
/* 159 */         localStringBufferInputStream.close();
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private void loadFileProperties()
/*     */   {
/* 173 */     String str = System.getProperty("awtdebug.properties", "");
/* 174 */     if (str.equals(""))
/*     */     {
/* 176 */       str = System.getProperty("user.home", "") + File.separator + "awtdebug" + "." + "properties";
/*     */     }
/*     */ 
/* 181 */     File localFile = new File(str);
/*     */     try {
/* 183 */       println("Reading debug settings from '" + localFile.getCanonicalPath() + "'...");
/* 184 */       FileInputStream localFileInputStream = new FileInputStream(localFile);
/* 185 */       this.props.load(localFileInputStream);
/* 186 */       localFileInputStream.close();
/*     */     } catch (FileNotFoundException localFileNotFoundException) {
/* 188 */       println("Did not find settings file.");
/*     */     } catch (IOException localIOException) {
/* 190 */       println("Problem reading settings, IOException: " + localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void loadSystemProperties()
/*     */   {
/* 200 */     Properties localProperties = System.getProperties();
/* 201 */     Enumeration localEnumeration = localProperties.propertyNames();
/* 202 */     while (localEnumeration.hasMoreElements()) {
/* 203 */       String str1 = (String)localEnumeration.nextElement();
/* 204 */       String str2 = localProperties.getProperty(str1, "");
/*     */ 
/* 206 */       if (str1.startsWith("awtdebug"))
/* 207 */         this.props.setProperty(str1, str2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean getBoolean(String paramString, boolean paramBoolean)
/*     */   {
/* 219 */     String str = getString(paramString, String.valueOf(paramBoolean));
/* 220 */     return str.equalsIgnoreCase("true");
/*     */   }
/*     */ 
/*     */   public synchronized int getInt(String paramString, int paramInt)
/*     */   {
/* 230 */     String str = getString(paramString, String.valueOf(paramInt));
/* 231 */     return Integer.parseInt(str);
/*     */   }
/*     */ 
/*     */   public synchronized String getString(String paramString1, String paramString2)
/*     */   {
/* 241 */     String str1 = "awtdebug." + paramString1;
/* 242 */     String str2 = this.props.getProperty(str1, paramString2);
/*     */ 
/* 244 */     return str2;
/*     */   }
/*     */ 
/*     */   public synchronized Enumeration getPropertyNames() {
/* 248 */     Vector localVector = new Vector();
/* 249 */     Enumeration localEnumeration = this.props.propertyNames();
/*     */ 
/* 252 */     while (localEnumeration.hasMoreElements()) {
/* 253 */       String str = (String)localEnumeration.nextElement();
/* 254 */       str = str.substring("awtdebug".length() + 1);
/* 255 */       localVector.addElement(str);
/*     */     }
/* 257 */     return localVector.elements();
/*     */   }
/*     */ 
/*     */   private void println(Object paramObject) {
/* 261 */     if (log.isLoggable(400))
/* 262 */       log.finer(paramObject.toString());
/*     */   }
/*     */ 
/*     */   private synchronized native void setCTracingOn(boolean paramBoolean);
/*     */ 
/*     */   private synchronized native void setCTracingOn(boolean paramBoolean, String paramString);
/*     */ 
/*     */   private synchronized native void setCTracingOn(boolean paramBoolean, String paramString, int paramInt);
/*     */ 
/*     */   private void loadNativeSettings()
/*     */   {
/* 276 */     boolean bool1 = getBoolean("ctrace", false);
/* 277 */     setCTracingOn(bool1);
/*     */ 
/* 282 */     Vector localVector = new Vector();
/* 283 */     Enumeration localEnumeration = getPropertyNames();
/*     */ 
/* 285 */     while (localEnumeration.hasMoreElements()) {
/* 286 */       localObject = (String)localEnumeration.nextElement();
/* 287 */       if ((((String)localObject).startsWith("ctrace")) && (((String)localObject).length() > PROP_CTRACE_LEN)) {
/* 288 */         localVector.addElement(localObject);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 293 */     Collections.sort(localVector);
/*     */ 
/* 298 */     Object localObject = localVector.elements();
/*     */ 
/* 300 */     while (((Enumeration)localObject).hasMoreElements()) {
/* 301 */       String str1 = (String)((Enumeration)localObject).nextElement();
/* 302 */       String str2 = str1.substring(PROP_CTRACE_LEN + 1);
/*     */ 
/* 305 */       int i = str2.indexOf('@');
/*     */ 
/* 309 */       String str3 = i != -1 ? str2.substring(0, i) : str2;
/* 310 */       String str4 = i != -1 ? str2.substring(i + 1) : "";
/* 311 */       boolean bool2 = getBoolean(str1, false);
/*     */ 
/* 314 */       if (str4.length() == 0)
/*     */       {
/* 316 */         setCTracingOn(bool2, str3);
/*     */       }
/*     */       else {
/* 319 */         int j = Integer.parseInt(str4, 10);
/* 320 */         setCTracingOn(bool2, str3, j);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.DebugSettings
 * JD-Core Version:    0.6.2
 */