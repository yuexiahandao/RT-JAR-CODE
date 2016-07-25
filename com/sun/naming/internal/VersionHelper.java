/*     */ package com.sun.naming.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import javax.naming.NamingEnumeration;
/*     */ 
/*     */ public abstract class VersionHelper
/*     */ {
/*  74 */   private static VersionHelper helper = new VersionHelper12();
/*     */ 
/*  52 */   static final String[] PROPS = { "java.naming.factory.initial", "java.naming.factory.object", "java.naming.factory.url.pkgs", "java.naming.factory.state", "java.naming.provider.url", "java.naming.dns.url", "java.naming.factory.control" };
/*     */   public static final int INITIAL_CONTEXT_FACTORY = 0;
/*     */   public static final int OBJECT_FACTORIES = 1;
/*     */   public static final int URL_PKG_PREFIXES = 2;
/*     */   public static final int STATE_FACTORIES = 3;
/*     */   public static final int PROVIDER_URL = 4;
/*     */   public static final int DNS_URL = 5;
/*     */   public static final int CONTROL_FACTORIES = 6;
/*     */ 
/*     */   public static VersionHelper getVersionHelper()
/*     */   {
/*  78 */     return helper;
/*     */   }
/*     */ 
/*     */   public abstract Class loadClass(String paramString)
/*     */     throws ClassNotFoundException;
/*     */ 
/*     */   abstract Class loadClass(String paramString, ClassLoader paramClassLoader)
/*     */     throws ClassNotFoundException;
/*     */ 
/*     */   public abstract Class loadClass(String paramString1, String paramString2)
/*     */     throws ClassNotFoundException, MalformedURLException;
/*     */ 
/*     */   abstract String getJndiProperty(int paramInt);
/*     */ 
/*     */   abstract String[] getJndiProperties();
/*     */ 
/*     */   abstract InputStream getResourceAsStream(Class paramClass, String paramString);
/*     */ 
/*     */   abstract InputStream getJavaHomeLibStream(String paramString);
/*     */ 
/*     */   abstract NamingEnumeration getResources(ClassLoader paramClassLoader, String paramString)
/*     */     throws IOException;
/*     */ 
/*     */   abstract ClassLoader getContextClassLoader();
/*     */ 
/*     */   protected static URL[] getUrlArray(String paramString)
/*     */     throws MalformedURLException
/*     */   {
/* 139 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
/* 140 */     Vector localVector = new Vector(10);
/* 141 */     while (localStringTokenizer.hasMoreTokens()) {
/* 142 */       localVector.addElement(localStringTokenizer.nextToken());
/*     */     }
/* 144 */     String[] arrayOfString = new String[localVector.size()];
/* 145 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 146 */       arrayOfString[i] = ((String)localVector.elementAt(i));
/*     */     }
/*     */ 
/* 149 */     URL[] arrayOfURL = new URL[arrayOfString.length];
/* 150 */     for (int j = 0; j < arrayOfURL.length; j++) {
/* 151 */       arrayOfURL[j] = new URL(arrayOfString[j]);
/*     */     }
/* 153 */     return arrayOfURL;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.naming.internal.VersionHelper
 * JD-Core Version:    0.6.2
 */