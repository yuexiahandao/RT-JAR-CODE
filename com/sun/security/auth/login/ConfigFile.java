/*     */ package com.sun.security.auth.login;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StreamTokenizer;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Security;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.AuthPermission;
/*     */ import javax.security.auth.login.AppConfigurationEntry;
/*     */ import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
/*     */ import javax.security.auth.login.Configuration;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.PropertyExpander;
/*     */ import sun.security.util.PropertyExpander.ExpandException;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public class ConfigFile extends Configuration
/*     */ {
/*     */   private StreamTokenizer st;
/*     */   private int lookahead;
/*     */   private int linenum;
/*     */   private HashMap<String, LinkedList<AppConfigurationEntry>> configuration;
/*  97 */   private boolean expandProp = true;
/*     */   private URL url;
/* 100 */   private static Debug debugConfig = Debug.getInstance("configfile");
/* 101 */   private static Debug debugParser = Debug.getInstance("configparser");
/*     */ 
/*     */   public ConfigFile()
/*     */   {
/*     */     try
/*     */     {
/* 108 */       init(this.url);
/*     */     } catch (IOException localIOException) {
/* 110 */       throw ((SecurityException)new SecurityException(localIOException.getMessage()).initCause(localIOException));
/*     */     }
/*     */   }
/*     */ 
/*     */   public ConfigFile(URI paramURI)
/*     */   {
/*     */     try
/*     */     {
/* 123 */       this.url = paramURI.toURL();
/* 124 */       init(this.url);
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 126 */       throw ((SecurityException)new SecurityException(localMalformedURLException.getMessage()).initCause(localMalformedURLException));
/*     */     }
/*     */     catch (IOException localIOException) {
/* 129 */       throw ((SecurityException)new SecurityException(localIOException.getMessage()).initCause(localIOException));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void init(URL paramURL)
/*     */     throws IOException
/*     */   {
/* 145 */     int i = 0;
/* 146 */     Object localObject1 = null;
/* 147 */     String str1 = File.separator;
/*     */ 
/* 149 */     if ("false".equals(System.getProperty("policy.expandProperties"))) {
/* 150 */       this.expandProp = false;
/*     */     }
/*     */ 
/* 154 */     HashMap localHashMap = new HashMap();
/*     */ 
/* 157 */     if (paramURL != null)
/*     */     {
/* 163 */       if (debugConfig != null) {
/* 164 */         debugConfig.println("reading " + paramURL);
/*     */       }
/* 166 */       init(paramURL, localHashMap);
/* 167 */       this.configuration = localHashMap;
/* 168 */       return;
/*     */     }
/*     */ 
/* 176 */     String str2 = Security.getProperty("policy.allowSystemProperty");
/*     */     Object localObject2;
/* 179 */     if ("true".equalsIgnoreCase(str2)) {
/* 180 */       String str3 = System.getProperty("java.security.auth.login.config");
/*     */ 
/* 182 */       if (str3 != null) {
/* 183 */         int k = 0;
/* 184 */         if (str3.startsWith("=")) {
/* 185 */           k = 1;
/* 186 */           str3 = str3.substring(1);
/*     */         }
/*     */         try {
/* 189 */           str3 = PropertyExpander.expand(str3);
/*     */         } catch (PropertyExpander.ExpandException localExpandException1) {
/* 191 */           MessageFormat localMessageFormat1 = new MessageFormat(ResourcesMgr.getString("Unable.to.properly.expand.config", "sun.security.util.AuthResources"));
/*     */ 
/* 195 */           localObject2 = new Object[] { str3 };
/* 196 */           throw new IOException(localMessageFormat1.format(localObject2));
/*     */         }
/*     */ 
/* 199 */         URL localURL = null;
/*     */         try {
/* 201 */           localURL = new URL(str3);
/*     */         } catch (MalformedURLException localMalformedURLException) {
/* 203 */           localObject2 = new File(str3);
/* 204 */           if (((File)localObject2).exists()) {
/* 205 */             localURL = ((File)localObject2).toURI().toURL();
/*     */           } else {
/* 207 */             MessageFormat localMessageFormat3 = new MessageFormat(ResourcesMgr.getString("extra.config.No.such.file.or.directory.", "sun.security.util.AuthResources"));
/*     */ 
/* 211 */             Object[] arrayOfObject = { str3 };
/* 212 */             throw new IOException(localMessageFormat3.format(arrayOfObject));
/*     */           }
/*     */         }
/*     */ 
/* 216 */         if (debugConfig != null) {
/* 217 */           debugConfig.println("reading " + localURL);
/*     */         }
/* 219 */         init(localURL, localHashMap);
/* 220 */         i = 1;
/* 221 */         if (k != 0) {
/* 222 */           if (debugConfig != null) {
/* 223 */             debugConfig.println("overriding other policies!");
/*     */           }
/* 225 */           this.configuration = localHashMap;
/* 226 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 231 */     int j = 1;
/*     */     String str4;
/* 234 */     while ((str4 = Security.getProperty("login.config.url." + j)) != null) {
/*     */       try {
/* 236 */         str4 = PropertyExpander.expand(str4).replace(File.separatorChar, '/');
/*     */ 
/* 238 */         if (debugConfig != null) {
/* 239 */           debugConfig.println("\tReading config: " + str4);
/*     */         }
/* 241 */         init(new URL(str4), localHashMap);
/* 242 */         i = 1;
/*     */       } catch (PropertyExpander.ExpandException localExpandException2) {
/* 244 */         MessageFormat localMessageFormat2 = new MessageFormat(ResourcesMgr.getString("Unable.to.properly.expand.config", "sun.security.util.AuthResources"));
/*     */ 
/* 248 */         localObject2 = new Object[] { str4 };
/* 249 */         throw new IOException(localMessageFormat2.format(localObject2));
/*     */       }
/* 251 */       j++;
/*     */     }
/*     */ 
/* 254 */     if ((i == 0) && (j == 1) && (str4 == null))
/*     */     {
/* 257 */       if (debugConfig != null) {
/* 258 */         debugConfig.println("\tReading Policy from ~/.java.login.config");
/*     */       }
/*     */ 
/* 261 */       str4 = System.getProperty("user.home");
/* 262 */       String str5 = str4 + File.separatorChar + ".java.login.config";
/*     */ 
/* 267 */       if (new File(str5).exists()) {
/* 268 */         init(new File(str5).toURI().toURL(), localHashMap);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 273 */     this.configuration = localHashMap;
/*     */   }
/*     */ 
/*     */   private void init(URL paramURL, HashMap<String, LinkedList<AppConfigurationEntry>> paramHashMap)
/*     */     throws IOException
/*     */   {
/* 280 */     InputStreamReader localInputStreamReader = null;
/*     */     try {
/* 282 */       localInputStreamReader = new InputStreamReader(getInputStream(paramURL), "UTF-8");
/* 283 */       readConfig(localInputStreamReader, paramHashMap);
/*     */     } catch (FileNotFoundException localFileNotFoundException) {
/* 285 */       if (debugConfig != null) {
/* 286 */         debugConfig.println(localFileNotFoundException.toString());
/*     */       }
/* 288 */       throw new IOException(ResourcesMgr.getString("Configuration.Error.No.such.file.or.directory", "sun.security.util.AuthResources"));
/*     */     }
/*     */     finally
/*     */     {
/* 292 */       if (localInputStreamReader != null)
/* 293 */         localInputStreamReader.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public AppConfigurationEntry[] getAppConfigurationEntry(String paramString)
/*     */   {
/* 313 */     LinkedList localLinkedList = null;
/* 314 */     synchronized (this.configuration) {
/* 315 */       localLinkedList = (LinkedList)this.configuration.get(paramString);
/*     */     }
/*     */ 
/* 318 */     if ((localLinkedList == null) || (localLinkedList.size() == 0)) {
/* 319 */       return null;
/*     */     }
/* 321 */     ??? = new AppConfigurationEntry[localLinkedList.size()];
/*     */ 
/* 323 */     Iterator localIterator = localLinkedList.iterator();
/* 324 */     for (int i = 0; localIterator.hasNext(); i++) {
/* 325 */       AppConfigurationEntry localAppConfigurationEntry = (AppConfigurationEntry)localIterator.next();
/* 326 */       ???[i] = new AppConfigurationEntry(localAppConfigurationEntry.getLoginModuleName(), localAppConfigurationEntry.getControlFlag(), localAppConfigurationEntry.getOptions());
/*     */     }
/*     */ 
/* 330 */     return ???;
/*     */   }
/*     */ 
/*     */   public synchronized void refresh()
/*     */   {
/* 344 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 345 */     if (localSecurityManager != null) {
/* 346 */       localSecurityManager.checkPermission(new AuthPermission("refreshLoginConfiguration"));
/*     */     }
/* 348 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run() {
/*     */         try {
/* 352 */           ConfigFile.this.init(ConfigFile.this.url);
/*     */         } catch (IOException localIOException) {
/* 354 */           throw ((SecurityException)new SecurityException(localIOException.getLocalizedMessage()).initCause(localIOException));
/*     */         }
/*     */ 
/* 357 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void readConfig(Reader paramReader, HashMap<String, LinkedList<AppConfigurationEntry>> paramHashMap)
/*     */     throws IOException
/*     */   {
/* 366 */     int i = 1;
/*     */ 
/* 368 */     if (!(paramReader instanceof BufferedReader)) {
/* 369 */       paramReader = new BufferedReader(paramReader);
/*     */     }
/* 371 */     this.st = new StreamTokenizer(paramReader);
/* 372 */     this.st.quoteChar(34);
/* 373 */     this.st.wordChars(36, 36);
/* 374 */     this.st.wordChars(95, 95);
/* 375 */     this.st.wordChars(45, 45);
/* 376 */     this.st.lowerCaseMode(false);
/* 377 */     this.st.slashSlashComments(true);
/* 378 */     this.st.slashStarComments(true);
/* 379 */     this.st.eolIsSignificant(true);
/*     */ 
/* 381 */     this.lookahead = nextToken();
/* 382 */     while (this.lookahead != -1)
/* 383 */       parseLoginEntry(paramHashMap);
/*     */   }
/*     */ 
/*     */   private void parseLoginEntry(HashMap<String, LinkedList<AppConfigurationEntry>> paramHashMap)
/*     */     throws IOException
/*     */   {
/* 395 */     LinkedList localLinkedList = new LinkedList();
/*     */ 
/* 398 */     String str1 = this.st.sval;
/* 399 */     this.lookahead = nextToken();
/*     */ 
/* 401 */     if (debugParser != null) {
/* 402 */       debugParser.println("\tReading next config entry: " + str1);
/*     */     }
/*     */ 
/* 405 */     match("{");
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 408 */     while (!peek("}"))
/*     */     {
/* 410 */       String str2 = match("module class name");
/*     */ 
/* 413 */       String str3 = match("controlFlag");
/*     */       AppConfigurationEntry.LoginModuleControlFlag localLoginModuleControlFlag;
/* 414 */       if (str3.equalsIgnoreCase("REQUIRED")) {
/* 415 */         localLoginModuleControlFlag = AppConfigurationEntry.LoginModuleControlFlag.REQUIRED;
/*     */       }
/* 417 */       else if (str3.equalsIgnoreCase("REQUISITE")) {
/* 418 */         localLoginModuleControlFlag = AppConfigurationEntry.LoginModuleControlFlag.REQUISITE;
/*     */       }
/* 420 */       else if (str3.equalsIgnoreCase("SUFFICIENT")) {
/* 421 */         localLoginModuleControlFlag = AppConfigurationEntry.LoginModuleControlFlag.SUFFICIENT;
/*     */       }
/* 423 */       else if (str3.equalsIgnoreCase("OPTIONAL")) {
/* 424 */         localLoginModuleControlFlag = AppConfigurationEntry.LoginModuleControlFlag.OPTIONAL;
/*     */       }
/*     */       else {
/* 427 */         localObject1 = new MessageFormat(ResourcesMgr.getString("Configuration.Error.Invalid.control.flag.flag", "sun.security.util.AuthResources"));
/*     */ 
/* 430 */         localObject2 = new Object[] { str3 };
/* 431 */         throw new IOException(((MessageFormat)localObject1).format(localObject2));
/*     */       }
/*     */ 
/* 435 */       localObject1 = new HashMap();
/*     */ 
/* 438 */       while (!peek(";")) { localObject2 = match("option key");
/* 440 */         match("=");
/*     */         String str4;
/*     */         try { str4 = expand(match("option value"));
/*     */         } catch (PropertyExpander.ExpandException localExpandException) {
/* 444 */           throw new IOException(localExpandException.getLocalizedMessage());
/*     */         }
/* 446 */         ((HashMap)localObject1).put(localObject2, str4);
/*     */       }
/*     */ 
/* 449 */       this.lookahead = nextToken();
/*     */ 
/* 452 */       if (debugParser != null) {
/* 453 */         debugParser.println("\t\t" + str2 + ", " + str3);
/* 454 */         localObject3 = ((HashMap)localObject1).keySet().iterator();
/* 455 */         while (((Iterator)localObject3).hasNext()) {
/* 456 */           localObject2 = (String)((Iterator)localObject3).next();
/* 457 */           debugParser.println("\t\t\t" + (String)localObject2 + "=" + (String)((HashMap)localObject1).get(localObject2));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 463 */       Object localObject3 = new AppConfigurationEntry(str2, localLoginModuleControlFlag, (Map)localObject1);
/*     */ 
/* 467 */       localLinkedList.add(localObject3);
/*     */     }
/*     */ 
/* 470 */     match("}");
/* 471 */     match(";");
/*     */ 
/* 474 */     if (paramHashMap.containsKey(str1)) {
/* 475 */       localObject1 = new MessageFormat(ResourcesMgr.getString("Configuration.Error.Can.not.specify.multiple.entries.for.appName", "sun.security.util.AuthResources"));
/*     */ 
/* 478 */       localObject2 = new Object[] { str1 };
/* 479 */       throw new IOException(((MessageFormat)localObject1).format(localObject2));
/*     */     }
/* 481 */     paramHashMap.put(str1, localLinkedList);
/*     */   }
/*     */ 
/*     */   private String match(String paramString) throws IOException
/*     */   {
/* 486 */     String str = null;
/*     */     MessageFormat localMessageFormat2;
/*     */     Object[] arrayOfObject2;
/* 488 */     switch (this.lookahead)
/*     */     {
/*     */     case -1:
/* 491 */       MessageFormat localMessageFormat1 = new MessageFormat(ResourcesMgr.getString("Configuration.Error.expected.expect.read.end.of.file.", "sun.security.util.AuthResources"));
/*     */ 
/* 494 */       Object[] arrayOfObject1 = { paramString };
/* 495 */       throw new IOException(localMessageFormat1.format(arrayOfObject1));
/*     */     case -3:
/*     */     case 34:
/* 500 */       if ((paramString.equalsIgnoreCase("module class name")) || (paramString.equalsIgnoreCase("controlFlag")) || (paramString.equalsIgnoreCase("option key")) || (paramString.equalsIgnoreCase("option value")))
/*     */       {
/* 504 */         str = this.st.sval;
/* 505 */         this.lookahead = nextToken();
/*     */       } else {
/* 507 */         localMessageFormat2 = new MessageFormat(ResourcesMgr.getString("Configuration.Error.Line.line.expected.expect.found.value.", "sun.security.util.AuthResources"));
/*     */ 
/* 510 */         arrayOfObject2 = new Object[] { new Integer(this.linenum), paramString, this.st.sval };
/* 511 */         throw new IOException(localMessageFormat2.format(arrayOfObject2));
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 123:
/* 517 */       if (paramString.equalsIgnoreCase("{")) {
/* 518 */         this.lookahead = nextToken();
/*     */       } else {
/* 520 */         localMessageFormat2 = new MessageFormat(ResourcesMgr.getString("Configuration.Error.Line.line.expected.expect.", "sun.security.util.AuthResources"));
/*     */ 
/* 523 */         arrayOfObject2 = new Object[] { new Integer(this.linenum), paramString, this.st.sval };
/* 524 */         throw new IOException(localMessageFormat2.format(arrayOfObject2));
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 59:
/* 530 */       if (paramString.equalsIgnoreCase(";")) {
/* 531 */         this.lookahead = nextToken();
/*     */       } else {
/* 533 */         localMessageFormat2 = new MessageFormat(ResourcesMgr.getString("Configuration.Error.Line.line.expected.expect.", "sun.security.util.AuthResources"));
/*     */ 
/* 536 */         arrayOfObject2 = new Object[] { new Integer(this.linenum), paramString, this.st.sval };
/* 537 */         throw new IOException(localMessageFormat2.format(arrayOfObject2));
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 125:
/* 543 */       if (paramString.equalsIgnoreCase("}")) {
/* 544 */         this.lookahead = nextToken();
/*     */       } else {
/* 546 */         localMessageFormat2 = new MessageFormat(ResourcesMgr.getString("Configuration.Error.Line.line.expected.expect.", "sun.security.util.AuthResources"));
/*     */ 
/* 549 */         arrayOfObject2 = new Object[] { new Integer(this.linenum), paramString, this.st.sval };
/* 550 */         throw new IOException(localMessageFormat2.format(arrayOfObject2));
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 61:
/* 556 */       if (paramString.equalsIgnoreCase("=")) {
/* 557 */         this.lookahead = nextToken();
/*     */       } else {
/* 559 */         localMessageFormat2 = new MessageFormat(ResourcesMgr.getString("Configuration.Error.Line.line.expected.expect.", "sun.security.util.AuthResources"));
/*     */ 
/* 562 */         arrayOfObject2 = new Object[] { new Integer(this.linenum), paramString, this.st.sval };
/* 563 */         throw new IOException(localMessageFormat2.format(arrayOfObject2));
/*     */       }
/*     */ 
/*     */       break;
/*     */     default:
/* 568 */       localMessageFormat2 = new MessageFormat(ResourcesMgr.getString("Configuration.Error.Line.line.expected.expect.found.value.", "sun.security.util.AuthResources"));
/*     */ 
/* 571 */       arrayOfObject2 = new Object[] { new Integer(this.linenum), paramString, this.st.sval };
/* 572 */       throw new IOException(localMessageFormat2.format(arrayOfObject2));
/*     */     }
/* 574 */     return str;
/*     */   }
/*     */ 
/*     */   private boolean peek(String paramString) {
/* 578 */     boolean bool = false;
/*     */ 
/* 580 */     switch (this.lookahead) {
/*     */     case 44:
/* 582 */       if (paramString.equalsIgnoreCase(","))
/* 583 */         bool = true; break;
/*     */     case 59:
/* 586 */       if (paramString.equalsIgnoreCase(";"))
/* 587 */         bool = true; break;
/*     */     case 123:
/* 590 */       if (paramString.equalsIgnoreCase("{"))
/* 591 */         bool = true; break;
/*     */     case 125:
/* 594 */       if (paramString.equalsIgnoreCase("}"))
/* 595 */         bool = true; break;
/*     */     }
/*     */ 
/* 599 */     return bool;
/*     */   }
/*     */ 
/*     */   private int nextToken()
/*     */     throws IOException
/*     */   {
/*     */     int i;
/* 604 */     while ((i = this.st.nextToken()) == 10) {
/* 605 */       this.linenum += 1;
/*     */     }
/* 607 */     return i;
/*     */   }
/*     */ 
/*     */   private InputStream getInputStream(URL paramURL)
/*     */     throws IOException
/*     */   {
/* 618 */     if ("file".equalsIgnoreCase(paramURL.getProtocol()))
/*     */     {
/*     */       try
/*     */       {
/* 635 */         return paramURL.openStream();
/*     */       } catch (Exception localException) {
/* 637 */         String str = paramURL.getPath();
/* 638 */         if (paramURL.getHost().length() > 0) {
/* 639 */           str = "//" + paramURL.getHost() + str;
/*     */         }
/* 641 */         if (debugConfig != null) {
/* 642 */           debugConfig.println("cannot read " + paramURL + ", try " + str);
/*     */         }
/*     */ 
/* 645 */         return new FileInputStream(str);
/*     */       }
/*     */     }
/* 648 */     return paramURL.openStream();
/*     */   }
/*     */ 
/*     */   private String expand(String paramString)
/*     */     throws PropertyExpander.ExpandException, IOException
/*     */   {
/* 655 */     if ("".equals(paramString)) {
/* 656 */       return paramString;
/*     */     }
/*     */ 
/* 659 */     if (this.expandProp)
/*     */     {
/* 661 */       String str = PropertyExpander.expand(paramString);
/*     */ 
/* 663 */       if ((str == null) || (str.length() == 0)) {
/* 664 */         MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("Configuration.Error.Line.line.system.property.value.expanded.to.empty.value", "sun.security.util.AuthResources"));
/*     */ 
/* 667 */         Object[] arrayOfObject = { new Integer(this.linenum), paramString };
/* 668 */         throw new IOException(localMessageFormat.format(arrayOfObject));
/*     */       }
/* 670 */       return str;
/*     */     }
/* 672 */     return paramString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.login.ConfigFile
 * JD-Core Version:    0.6.2
 */