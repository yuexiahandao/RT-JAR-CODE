/*     */ package com.sun.jmx.remote.security;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import javax.security.auth.Subject;
/*     */ 
/*     */ public class MBeanServerFileAccessController extends MBeanServerAccessController
/*     */ {
/*     */   static final String READONLY = "readonly";
/*     */   static final String READWRITE = "readwrite";
/*     */   static final String CREATE = "create";
/*     */   static final String UNREGISTER = "unregister";
/*     */   private Map<String, Access> accessMap;
/*     */   private Properties originalProps;
/*     */   private String accessFileName;
/*     */ 
/*     */   public MBeanServerFileAccessController(String paramString)
/*     */     throws IOException
/*     */   {
/* 132 */     this.accessFileName = paramString;
/* 133 */     Properties localProperties = propertiesFromFile(paramString);
/* 134 */     parseProperties(localProperties);
/*     */   }
/*     */ 
/*     */   public MBeanServerFileAccessController(String paramString, MBeanServer paramMBeanServer)
/*     */     throws IOException
/*     */   {
/* 159 */     this(paramString);
/* 160 */     setMBeanServer(paramMBeanServer);
/*     */   }
/*     */ 
/*     */   public MBeanServerFileAccessController(Properties paramProperties)
/*     */     throws IOException
/*     */   {
/* 188 */     if (paramProperties == null)
/* 189 */       throw new IllegalArgumentException("Null properties");
/* 190 */     this.originalProps = paramProperties;
/* 191 */     parseProperties(paramProperties);
/*     */   }
/*     */ 
/*     */   public MBeanServerFileAccessController(Properties paramProperties, MBeanServer paramMBeanServer)
/*     */     throws IOException
/*     */   {
/* 221 */     this(paramProperties);
/* 222 */     setMBeanServer(paramMBeanServer);
/*     */   }
/*     */ 
/*     */   public void checkRead()
/*     */   {
/* 231 */     checkAccess(AccessType.READ, null);
/*     */   }
/*     */ 
/*     */   public void checkWrite()
/*     */   {
/* 240 */     checkAccess(AccessType.WRITE, null);
/*     */   }
/*     */ 
/*     */   public void checkCreate(String paramString)
/*     */   {
/* 249 */     checkAccess(AccessType.CREATE, paramString);
/*     */   }
/*     */ 
/*     */   public void checkUnregister(ObjectName paramObjectName)
/*     */   {
/* 258 */     checkAccess(AccessType.UNREGISTER, null);
/*     */   }
/*     */ 
/*     */   public synchronized void refresh()
/*     */     throws IOException
/*     */   {
/*     */     Properties localProperties;
/* 285 */     if (this.accessFileName == null)
/* 286 */       localProperties = this.originalProps;
/*     */     else
/* 288 */       localProperties = propertiesFromFile(this.accessFileName);
/* 289 */     parseProperties(localProperties);
/*     */   }
/*     */ 
/*     */   private static Properties propertiesFromFile(String paramString) throws IOException
/*     */   {
/* 294 */     FileInputStream localFileInputStream = new FileInputStream(paramString);
/*     */     try {
/* 296 */       Properties localProperties1 = new Properties();
/* 297 */       localProperties1.load(localFileInputStream);
/* 298 */       return localProperties1;
/*     */     } finally {
/* 300 */       localFileInputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void checkAccess(AccessType paramAccessType, String paramString) {
/* 305 */     final AccessControlContext localAccessControlContext = AccessController.getContext();
/* 306 */     Subject localSubject = (Subject)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Subject run() {
/* 309 */         return Subject.getSubject(localAccessControlContext);
/*     */       }
/*     */     });
/* 312 */     if (localSubject == null) return;
/* 313 */     Set localSet = localSubject.getPrincipals();
/* 314 */     String str = null;
/* 315 */     for (Object localObject1 = localSet.iterator(); ((Iterator)localObject1).hasNext(); ) {
/* 316 */       localObject2 = (Principal)((Iterator)localObject1).next();
/* 317 */       Access localAccess = (Access)this.accessMap.get(((Principal)localObject2).getName());
/* 318 */       if (localAccess != null)
/*     */       {
/*     */         boolean bool;
/* 320 */         switch (2.$SwitchMap$com$sun$jmx$remote$security$MBeanServerFileAccessController$AccessType[paramAccessType.ordinal()]) {
/*     */         case 1:
/* 322 */           bool = true;
/* 323 */           break;
/*     */         case 2:
/* 325 */           bool = localAccess.write;
/* 326 */           break;
/*     */         case 3:
/* 328 */           bool = localAccess.unregister;
/* 329 */           if ((!bool) && (localAccess.write))
/* 330 */             str = "unregister"; break;
/*     */         case 4:
/* 333 */           bool = checkCreateAccess(localAccess, paramString);
/* 334 */           if ((!bool) && (localAccess.write))
/* 335 */             str = "create " + paramString; break;
/*     */         default:
/* 338 */           throw new AssertionError();
/*     */         }
/* 340 */         if (bool)
/*     */           return;
/*     */       }
/*     */     }
/*     */     Object localObject2;
/* 344 */     localObject1 = new SecurityException("Access denied! Invalid access level for requested MBeanServer operation.");
/*     */ 
/* 351 */     if (str != null) {
/* 352 */       localObject2 = new SecurityException("Access property for this identity should be similar to: readwrite " + str);
/*     */ 
/* 355 */       ((SecurityException)localObject1).initCause((Throwable)localObject2);
/*     */     }
/* 357 */     throw ((Throwable)localObject1);
/*     */   }
/*     */ 
/*     */   private static boolean checkCreateAccess(Access paramAccess, String paramString) {
/* 361 */     for (String str : paramAccess.createPatterns) {
/* 362 */       if (classNameMatch(str, paramString))
/* 363 */         return true;
/*     */     }
/* 365 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean classNameMatch(String paramString1, String paramString2)
/*     */   {
/* 378 */     StringBuilder localStringBuilder = new StringBuilder();
/* 379 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString1, "*", true);
/* 380 */     while (localStringTokenizer.hasMoreTokens()) {
/* 381 */       String str = localStringTokenizer.nextToken();
/* 382 */       if (str.equals("*"))
/* 383 */         localStringBuilder.append("[^.]*");
/*     */       else
/* 385 */         localStringBuilder.append(Pattern.quote(str));
/*     */     }
/* 387 */     return paramString2.matches(localStringBuilder.toString());
/*     */   }
/*     */ 
/*     */   private void parseProperties(Properties paramProperties) {
/* 391 */     this.accessMap = new HashMap();
/* 392 */     for (Map.Entry localEntry : paramProperties.entrySet()) {
/* 393 */       String str1 = (String)localEntry.getKey();
/* 394 */       String str2 = (String)localEntry.getValue();
/* 395 */       Access localAccess = Parser.parseAccess(str1, str2);
/* 396 */       this.accessMap.put(str1, localAccess);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Access
/*     */   {
/*     */     final boolean write;
/*     */     final String[] createPatterns;
/*     */     private boolean unregister;
/* 108 */     private final String[] NO_STRINGS = new String[0];
/*     */ 
/*     */     Access(boolean paramBoolean1, boolean paramBoolean2, List<String> paramList)
/*     */     {
/*  99 */       this.write = paramBoolean1;
/* 100 */       int i = paramList == null ? 0 : paramList.size();
/* 101 */       if (i == 0)
/* 102 */         this.createPatterns = this.NO_STRINGS;
/*     */       else
/* 104 */         this.createPatterns = ((String[])paramList.toArray(new String[i]));
/* 105 */       this.unregister = paramBoolean2;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static enum AccessType
/*     */   {
/*  91 */     READ, WRITE, CREATE, UNREGISTER;
/*     */   }
/*     */ 
/*     */   private static class Parser
/*     */   {
/*     */     private static final int EOS = -1;
/*     */     private final String identity;
/*     */     private final String s;
/*     */     private final int len;
/*     */     private int i;
/*     */     private int c;
/*     */ 
/*     */     private Parser(String paramString1, String paramString2)
/*     */     {
/* 418 */       this.identity = paramString1;
/* 419 */       this.s = paramString2;
/* 420 */       this.len = paramString2.length();
/* 421 */       this.i = 0;
/* 422 */       if (this.i < this.len)
/* 423 */         this.c = paramString2.codePointAt(this.i);
/*     */       else
/* 425 */         this.c = -1;
/*     */     }
/*     */ 
/*     */     static MBeanServerFileAccessController.Access parseAccess(String paramString1, String paramString2) {
/* 429 */       return new Parser(paramString1, paramString2).parseAccess();
/*     */     }
/*     */ 
/*     */     private MBeanServerFileAccessController.Access parseAccess() {
/* 433 */       skipSpace();
/* 434 */       String str = parseWord();
/*     */       MBeanServerFileAccessController.Access localAccess;
/* 436 */       if (str.equals("readonly"))
/* 437 */         localAccess = new MBeanServerFileAccessController.Access(false, false, null);
/* 438 */       else if (str.equals("readwrite"))
/* 439 */         localAccess = parseReadWrite();
/*     */       else {
/* 441 */         throw syntax("Expected readonly or readwrite: " + str);
/*     */       }
/*     */ 
/* 444 */       if (this.c != -1)
/* 445 */         throw syntax("Extra text at end of line");
/* 446 */       return localAccess;
/*     */     }
/*     */ 
/*     */     private MBeanServerFileAccessController.Access parseReadWrite() {
/* 450 */       ArrayList localArrayList = new ArrayList();
/* 451 */       boolean bool = false;
/*     */       while (true) {
/* 453 */         skipSpace();
/* 454 */         if (this.c == -1)
/*     */           break;
/* 456 */         String str = parseWord();
/* 457 */         if (str.equals("unregister"))
/* 458 */           bool = true;
/* 459 */         else if (str.equals("create"))
/* 460 */           parseCreate(localArrayList);
/*     */         else
/* 462 */           throw syntax("Unrecognized keyword " + str);
/*     */       }
/* 464 */       return new MBeanServerFileAccessController.Access(true, bool, localArrayList);
/*     */     }
/*     */ 
/*     */     private void parseCreate(List<String> paramList) {
/*     */       while (true) {
/* 469 */         skipSpace();
/* 470 */         paramList.add(parseClassName());
/* 471 */         skipSpace();
/* 472 */         if (this.c != 44) break;
/* 473 */         next();
/*     */       }
/*     */     }
/*     */ 
/*     */     private String parseClassName()
/*     */     {
/* 488 */       int j = this.i;
/* 489 */       int k = 0;
/*     */       while (true) {
/* 491 */         if (this.c == 46) {
/* 492 */           if (k == 0)
/* 493 */             throw syntax("Bad . in class name");
/* 494 */           k = 0; } else {
/* 495 */           if ((this.c != 42) && (!Character.isJavaIdentifierPart(this.c))) break;
/* 496 */           k = 1;
/*     */         }
/*     */ 
/* 499 */         next();
/*     */       }
/* 501 */       String str = this.s.substring(j, this.i);
/* 502 */       if (k == 0)
/* 503 */         throw syntax("Bad class name " + str);
/* 504 */       return str;
/*     */     }
/*     */ 
/*     */     private void next()
/*     */     {
/* 509 */       if (this.c != -1) {
/* 510 */         this.i += Character.charCount(this.c);
/* 511 */         if (this.i < this.len)
/* 512 */           this.c = this.s.codePointAt(this.i);
/*     */         else
/* 514 */           this.c = -1;
/*     */       }
/*     */     }
/*     */ 
/*     */     private void skipSpace() {
/* 519 */       while (Character.isWhitespace(this.c))
/* 520 */         next();
/*     */     }
/*     */ 
/*     */     private String parseWord() {
/* 524 */       skipSpace();
/* 525 */       if (this.c == -1)
/* 526 */         throw syntax("Expected word at end of line");
/* 527 */       int j = this.i;
/* 528 */       while ((this.c != -1) && (!Character.isWhitespace(this.c)))
/* 529 */         next();
/* 530 */       String str = this.s.substring(j, this.i);
/* 531 */       skipSpace();
/* 532 */       return str;
/*     */     }
/*     */ 
/*     */     private IllegalArgumentException syntax(String paramString) {
/* 536 */       return new IllegalArgumentException(paramString + " [" + this.identity + " " + this.s + "]");
/*     */     }
/*     */ 
/*     */     static
/*     */     {
/* 403 */       assert (!Character.isWhitespace(-1));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.security.MBeanServerFileAccessController
 * JD-Core Version:    0.6.2
 */