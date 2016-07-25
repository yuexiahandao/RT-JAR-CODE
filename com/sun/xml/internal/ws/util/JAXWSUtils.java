/*     */ package com.sun.xml.internal.ws.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class JAXWSUtils
/*     */ {
/*     */   public static String getUUID()
/*     */   {
/*  46 */     return UUID.randomUUID().toString();
/*     */   }
/*     */ 
/*     */   public static String getFileOrURLName(String fileOrURL)
/*     */   {
/*     */     try
/*     */     {
/*  54 */       return escapeSpace(new URL(fileOrURL).toExternalForm());
/*     */     } catch (MalformedURLException e) {
/*  56 */       return new File(fileOrURL).getCanonicalFile().toURL().toExternalForm();
/*     */     }
/*     */     catch (Exception e) {
/*     */     }
/*  60 */     return fileOrURL;
/*     */   }
/*     */ 
/*     */   public static URL getFileOrURL(String fileOrURL) throws IOException
/*     */   {
/*     */     try {
/*  66 */       return new URL(fileOrURL); } catch (MalformedURLException e) {
/*     */     }
/*  68 */     return new File(fileOrURL).toURL();
/*     */   }
/*     */ 
/*     */   private static String escapeSpace(String url)
/*     */   {
/*  73 */     StringBuffer buf = new StringBuffer();
/*  74 */     for (int i = 0; i < url.length(); i++)
/*     */     {
/*  76 */       if (url.charAt(i) == ' ')
/*  77 */         buf.append("%20");
/*     */       else
/*  79 */         buf.append(url.charAt(i));
/*     */     }
/*  81 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public static String absolutize(String name)
/*     */   {
/*     */     try
/*     */     {
/*  88 */       URL baseURL = new File(".").getCanonicalFile().toURL();
/*  89 */       return new URL(baseURL, name).toExternalForm();
/*     */     }
/*     */     catch (IOException e) {
/*     */     }
/*  93 */     return name;
/*     */   }
/*     */ 
/*     */   public static void checkAbsoluteness(String systemId)
/*     */   {
/*     */     try
/*     */     {
/* 105 */       new URL(systemId);
/*     */     } catch (MalformedURLException _) {
/*     */       try {
/* 108 */         new URI(systemId);
/*     */       } catch (URISyntaxException e) {
/* 110 */         throw new IllegalArgumentException("system ID '" + systemId + "' isn't absolute", e);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean matchQNames(QName target, QName pattern)
/*     */   {
/* 121 */     if ((target == null) || (pattern == null))
/*     */     {
/* 123 */       return false;
/*     */     }
/* 125 */     if (pattern.getNamespaceURI().equals(target.getNamespaceURI())) {
/* 126 */       String regex = pattern.getLocalPart().replaceAll("\\*", ".*");
/* 127 */       return Pattern.matches(regex, target.getLocalPart());
/*     */     }
/* 129 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.JAXWSUtils
 * JD-Core Version:    0.6.2
 */