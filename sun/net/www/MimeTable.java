/*     */ package sun.net.www;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.FileNameMap;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class MimeTable
/*     */   implements FileNameMap
/*     */ {
/*  40 */   private Hashtable<String, MimeEntry> entries = new Hashtable();
/*     */ 
/*  44 */   private Hashtable<String, MimeEntry> extensionMap = new Hashtable();
/*     */   private static String tempFileTemplate;
/*     */   private static final String filePreamble = "sun.net.www MIME content-types table";
/*     */   private static final String fileMagic = "#sun.net.www MIME content-types table";
/*     */   protected static String[] mailcapLocations;
/*     */ 
/*     */   MimeTable()
/*     */   {
/*  78 */     load();
/*     */   }
/*     */ 
/*     */   public static MimeTable getDefaultTable()
/*     */   {
/* 101 */     return DefaultInstanceHolder.defaultInstance;
/*     */   }
/*     */ 
/*     */   public static FileNameMap loadTable()
/*     */   {
/* 108 */     MimeTable localMimeTable = getDefaultTable();
/* 109 */     return localMimeTable;
/*     */   }
/*     */ 
/*     */   public synchronized int getSize() {
/* 113 */     return this.entries.size();
/*     */   }
/*     */ 
/*     */   public synchronized String getContentTypeFor(String paramString) {
/* 117 */     MimeEntry localMimeEntry = findByFileName(paramString);
/* 118 */     if (localMimeEntry != null) {
/* 119 */       return localMimeEntry.getType();
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized void add(MimeEntry paramMimeEntry)
/*     */   {
/* 126 */     this.entries.put(paramMimeEntry.getType(), paramMimeEntry);
/*     */ 
/* 128 */     String[] arrayOfString = paramMimeEntry.getExtensions();
/* 129 */     if (arrayOfString == null) {
/* 130 */       return;
/*     */     }
/*     */ 
/* 133 */     for (int i = 0; i < arrayOfString.length; i++)
/* 134 */       this.extensionMap.put(arrayOfString[i], paramMimeEntry);
/*     */   }
/*     */ 
/*     */   public synchronized MimeEntry remove(String paramString)
/*     */   {
/* 139 */     MimeEntry localMimeEntry = (MimeEntry)this.entries.get(paramString);
/* 140 */     return remove(localMimeEntry);
/*     */   }
/*     */ 
/*     */   public synchronized MimeEntry remove(MimeEntry paramMimeEntry) {
/* 144 */     String[] arrayOfString = paramMimeEntry.getExtensions();
/* 145 */     if (arrayOfString != null) {
/* 146 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 147 */         this.extensionMap.remove(arrayOfString[i]);
/*     */       }
/*     */     }
/*     */ 
/* 151 */     return (MimeEntry)this.entries.remove(paramMimeEntry.getType());
/*     */   }
/*     */ 
/*     */   public synchronized MimeEntry find(String paramString) {
/* 155 */     MimeEntry localMimeEntry1 = (MimeEntry)this.entries.get(paramString);
/* 156 */     if (localMimeEntry1 == null)
/*     */     {
/* 158 */       Enumeration localEnumeration = this.entries.elements();
/* 159 */       while (localEnumeration.hasMoreElements()) {
/* 160 */         MimeEntry localMimeEntry2 = (MimeEntry)localEnumeration.nextElement();
/* 161 */         if (localMimeEntry2.matches(paramString)) {
/* 162 */           return localMimeEntry2;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 167 */     return localMimeEntry1;
/*     */   }
/*     */ 
/*     */   public MimeEntry findByFileName(String paramString)
/*     */   {
/* 175 */     String str = "";
/*     */ 
/* 177 */     int i = paramString.lastIndexOf('#');
/*     */ 
/* 179 */     if (i > 0) {
/* 180 */       paramString = paramString.substring(0, i - 1);
/*     */     }
/*     */ 
/* 183 */     i = paramString.lastIndexOf('.');
/*     */ 
/* 185 */     i = Math.max(i, paramString.lastIndexOf('/'));
/* 186 */     i = Math.max(i, paramString.lastIndexOf('?'));
/*     */ 
/* 188 */     if ((i != -1) && (paramString.charAt(i) == '.')) {
/* 189 */       str = paramString.substring(i).toLowerCase();
/*     */     }
/*     */ 
/* 192 */     return findByExt(str);
/*     */   }
/*     */ 
/*     */   public synchronized MimeEntry findByExt(String paramString)
/*     */   {
/* 200 */     return (MimeEntry)this.extensionMap.get(paramString);
/*     */   }
/*     */ 
/*     */   public synchronized MimeEntry findByDescription(String paramString) {
/* 204 */     Enumeration localEnumeration = elements();
/* 205 */     while (localEnumeration.hasMoreElements()) {
/* 206 */       MimeEntry localMimeEntry = (MimeEntry)localEnumeration.nextElement();
/* 207 */       if (paramString.equals(localMimeEntry.getDescription())) {
/* 208 */         return localMimeEntry;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 213 */     return find(paramString);
/*     */   }
/*     */ 
/*     */   String getTempFileTemplate() {
/* 217 */     return tempFileTemplate;
/*     */   }
/*     */ 
/*     */   public synchronized Enumeration<MimeEntry> elements() {
/* 221 */     return this.entries.elements();
/*     */   }
/*     */ 
/*     */   public synchronized void load()
/*     */   {
/* 230 */     Properties localProperties = new Properties();
/* 231 */     File localFile = null;
/*     */     try
/*     */     {
/* 235 */       String str = System.getProperty("content.types.user.table");
/*     */ 
/* 237 */       if (str != null) {
/* 238 */         localFile = new File(str);
/* 239 */         if (!localFile.exists())
/*     */         {
/* 241 */           localFile = new File(System.getProperty("java.home") + File.separator + "lib" + File.separator + "content-types.properties");
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 250 */         localFile = new File(System.getProperty("java.home") + File.separator + "lib" + File.separator + "content-types.properties");
/*     */       }
/*     */ 
/* 257 */       BufferedInputStream localBufferedInputStream = new BufferedInputStream(new FileInputStream(localFile));
/* 258 */       localProperties.load(localBufferedInputStream);
/* 259 */       localBufferedInputStream.close();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 262 */       System.err.println("Warning: default mime table not found: " + localFile.getPath());
/*     */ 
/* 264 */       return;
/*     */     }
/* 266 */     parse(localProperties);
/*     */   }
/*     */ 
/*     */   void parse(Properties paramProperties)
/*     */   {
/* 271 */     String str1 = (String)paramProperties.get("temp.file.template");
/* 272 */     if (str1 != null) {
/* 273 */       paramProperties.remove("temp.file.template");
/* 274 */       tempFileTemplate = str1;
/*     */     }
/*     */ 
/* 278 */     Enumeration localEnumeration = paramProperties.propertyNames();
/* 279 */     while (localEnumeration.hasMoreElements()) {
/* 280 */       String str2 = (String)localEnumeration.nextElement();
/* 281 */       String str3 = paramProperties.getProperty(str2);
/* 282 */       parse(str2, str3);
/*     */     }
/*     */   }
/*     */ 
/*     */   void parse(String paramString1, String paramString2)
/*     */   {
/* 314 */     MimeEntry localMimeEntry = new MimeEntry(paramString1);
/*     */ 
/* 317 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString2, ";");
/* 318 */     while (localStringTokenizer.hasMoreTokens()) {
/* 319 */       String str = localStringTokenizer.nextToken();
/* 320 */       parse(str, localMimeEntry);
/*     */     }
/*     */ 
/* 323 */     add(localMimeEntry);
/*     */   }
/*     */ 
/*     */   void parse(String paramString, MimeEntry paramMimeEntry)
/*     */   {
/* 328 */     String str1 = null;
/* 329 */     String str2 = null;
/*     */ 
/* 331 */     int i = 0;
/* 332 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "=");
/* 333 */     while (localStringTokenizer.hasMoreTokens()) {
/* 334 */       if (i != 0) {
/* 335 */         str2 = localStringTokenizer.nextToken().trim();
/*     */       }
/*     */       else {
/* 338 */         str1 = localStringTokenizer.nextToken().trim();
/* 339 */         i = 1;
/*     */       }
/*     */     }
/*     */ 
/* 343 */     fill(paramMimeEntry, str1, str2);
/*     */   }
/*     */ 
/*     */   void fill(MimeEntry paramMimeEntry, String paramString1, String paramString2) {
/* 347 */     if ("description".equalsIgnoreCase(paramString1)) {
/* 348 */       paramMimeEntry.setDescription(paramString2);
/*     */     }
/* 350 */     else if ("action".equalsIgnoreCase(paramString1)) {
/* 351 */       paramMimeEntry.setAction(getActionCode(paramString2));
/*     */     }
/* 353 */     else if ("application".equalsIgnoreCase(paramString1)) {
/* 354 */       paramMimeEntry.setCommand(paramString2);
/*     */     }
/* 356 */     else if ("icon".equalsIgnoreCase(paramString1)) {
/* 357 */       paramMimeEntry.setImageFileName(paramString2);
/*     */     }
/* 359 */     else if ("file_extensions".equalsIgnoreCase(paramString1))
/* 360 */       paramMimeEntry.setExtensions(paramString2);
/*     */   }
/*     */ 
/*     */   String[] getExtensions(String paramString)
/*     */   {
/* 367 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ",");
/* 368 */     int i = localStringTokenizer.countTokens();
/* 369 */     String[] arrayOfString = new String[i];
/* 370 */     for (int j = 0; j < i; j++) {
/* 371 */       arrayOfString[j] = localStringTokenizer.nextToken();
/*     */     }
/*     */ 
/* 374 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   int getActionCode(String paramString) {
/* 378 */     for (int i = 0; i < MimeEntry.actionKeywords.length; i++) {
/* 379 */       if (paramString.equalsIgnoreCase(MimeEntry.actionKeywords[i])) {
/* 380 */         return i;
/*     */       }
/*     */     }
/*     */ 
/* 384 */     return 0;
/*     */   }
/*     */ 
/*     */   public synchronized boolean save(String paramString) {
/* 388 */     if (paramString == null) {
/* 389 */       paramString = System.getProperty("user.home" + File.separator + "lib" + File.separator + "content-types.properties");
/*     */     }
/*     */ 
/* 396 */     return saveAsProperties(new File(paramString));
/*     */   }
/*     */ 
/*     */   public Properties getAsProperties() {
/* 400 */     Properties localProperties = new Properties();
/* 401 */     Enumeration localEnumeration = elements();
/* 402 */     while (localEnumeration.hasMoreElements()) {
/* 403 */       MimeEntry localMimeEntry = (MimeEntry)localEnumeration.nextElement();
/* 404 */       localProperties.put(localMimeEntry.getType(), localMimeEntry.toProperty());
/*     */     }
/*     */ 
/* 407 */     return localProperties;
/*     */   }
/*     */ 
/*     */   protected boolean saveAsProperties(File paramFile) {
/* 411 */     FileOutputStream localFileOutputStream = null;
/*     */     try {
/* 413 */       localFileOutputStream = new FileOutputStream(paramFile);
/* 414 */       Properties localProperties = getAsProperties();
/* 415 */       localProperties.put("temp.file.template", tempFileTemplate);
/*     */ 
/* 417 */       String str2 = System.getProperty("user.name");
/* 418 */       if (str2 != null) {
/* 419 */         String str1 = "; customized for " + str2;
/* 420 */         localProperties.save(localFileOutputStream, "sun.net.www MIME content-types table" + str1);
/*     */       }
/*     */       else {
/* 423 */         localProperties.save(localFileOutputStream, "sun.net.www MIME content-types table");
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException2) {
/* 427 */       localIOException2.printStackTrace();
/* 428 */       return false;
/*     */     }
/*     */     finally {
/* 431 */       if (localFileOutputStream != null) try {
/* 432 */           localFileOutputStream.close();
/*     */         }
/*     */         catch (IOException localIOException4) {  }
/*     */  
/*     */     }
/* 436 */     return true;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  51 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run() {
/*  54 */         MimeTable.access$002(System.getProperty("content.types.temp.file.template", "/tmp/%s"));
/*     */ 
/*  58 */         MimeTable.mailcapLocations = new String[] { System.getProperty("user.mailcap"), System.getProperty("user.home") + "/.mailcap", "/etc/mailcap", "/usr/etc/mailcap", "/usr/local/etc/mailcap", System.getProperty("hotjava.home", "/usr/local/hotjava") + "/lib/mailcap" };
/*     */ 
/*  68 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static class DefaultInstanceHolder
/*     */   {
/*  82 */     static final MimeTable defaultInstance = getDefaultInstance();
/*     */ 
/*     */     static MimeTable getDefaultInstance() {
/*  85 */       return (MimeTable)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public MimeTable run() {
/*  88 */           MimeTable localMimeTable = new MimeTable();
/*  89 */           URLConnection.setFileNameMap(localMimeTable);
/*  90 */           return localMimeTable;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.MimeTable
 * JD-Core Version:    0.6.2
 */