/*     */ package com.sun.org.apache.xml.internal.serializer.utils;
/*     */ 
/*     */ import java.io.File;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public final class SystemIDResolver
/*     */ {
/*     */   public static String getAbsoluteURIFromRelative(String localPath)
/*     */   {
/*  66 */     if ((localPath == null) || (localPath.length() == 0)) {
/*  67 */       return "";
/*     */     }
/*     */ 
/*  71 */     String absolutePath = localPath;
/*  72 */     if (!isAbsolutePath(localPath))
/*     */     {
/*     */       try
/*     */       {
/*  76 */         absolutePath = getAbsolutePathFromRelativePath(localPath);
/*     */       }
/*     */       catch (SecurityException se)
/*     */       {
/*  81 */         return "file:" + localPath;
/*     */       }
/*     */     }
/*     */     String urlString;
/*     */     String urlString;
/*  86 */     if (null != absolutePath)
/*     */     {
/*     */       String urlString;
/*  88 */       if (absolutePath.startsWith(File.separator))
/*  89 */         urlString = "file://" + absolutePath;
/*     */       else
/*  91 */         urlString = "file:///" + absolutePath;
/*     */     }
/*     */     else {
/*  94 */       urlString = "file:" + localPath;
/*     */     }
/*  96 */     return replaceChars(urlString);
/*     */   }
/*     */ 
/*     */   private static String getAbsolutePathFromRelativePath(String relativePath)
/*     */   {
/* 107 */     return new File(relativePath).getAbsolutePath();
/*     */   }
/*     */ 
/*     */   public static boolean isAbsoluteURI(String systemId)
/*     */   {
/* 129 */     if (isWindowsAbsolutePath(systemId)) {
/* 130 */       return false;
/*     */     }
/*     */ 
/* 133 */     int fragmentIndex = systemId.indexOf('#');
/* 134 */     int queryIndex = systemId.indexOf('?');
/* 135 */     int slashIndex = systemId.indexOf('/');
/* 136 */     int colonIndex = systemId.indexOf(':');
/*     */ 
/* 139 */     int index = systemId.length() - 1;
/* 140 */     if (fragmentIndex > 0)
/* 141 */       index = fragmentIndex;
/* 142 */     if ((queryIndex > 0) && (queryIndex < index))
/* 143 */       index = queryIndex;
/* 144 */     if ((slashIndex > 0) && (slashIndex < index)) {
/* 145 */       index = slashIndex;
/*     */     }
/* 147 */     return (colonIndex > 0) && (colonIndex < index);
/*     */   }
/*     */ 
/*     */   public static boolean isAbsolutePath(String systemId)
/*     */   {
/* 159 */     if (systemId == null)
/* 160 */       return false;
/* 161 */     File file = new File(systemId);
/* 162 */     return file.isAbsolute();
/*     */   }
/*     */ 
/*     */   private static boolean isWindowsAbsolutePath(String systemId)
/*     */   {
/* 174 */     if (!isAbsolutePath(systemId)) {
/* 175 */       return false;
/*     */     }
/* 177 */     if ((systemId.length() > 2) && (systemId.charAt(1) == ':') && (Character.isLetter(systemId.charAt(0))) && ((systemId.charAt(2) == '\\') || (systemId.charAt(2) == '/')))
/*     */     {
/* 181 */       return true;
/*     */     }
/* 183 */     return false;
/*     */   }
/*     */ 
/*     */   private static String replaceChars(String str)
/*     */   {
/* 195 */     StringBuffer buf = new StringBuffer(str);
/* 196 */     int length = buf.length();
/* 197 */     for (int i = 0; i < length; i++)
/*     */     {
/* 199 */       char currentChar = buf.charAt(i);
/*     */ 
/* 201 */       if (currentChar == ' ')
/*     */       {
/* 203 */         buf.setCharAt(i, '%');
/* 204 */         buf.insert(i + 1, "20");
/* 205 */         length += 2;
/* 206 */         i += 2;
/*     */       }
/* 209 */       else if (currentChar == '\\')
/*     */       {
/* 211 */         buf.setCharAt(i, '/');
/*     */       }
/*     */     }
/*     */ 
/* 215 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public static String getAbsoluteURI(String systemId)
/*     */   {
/* 227 */     String absoluteURI = systemId;
/* 228 */     if (isAbsoluteURI(systemId))
/*     */     {
/* 231 */       if (systemId.startsWith("file:"))
/*     */       {
/* 233 */         String str = systemId.substring(5);
/*     */ 
/* 237 */         if ((str != null) && (str.startsWith("/")))
/*     */         {
/* 239 */           if ((str.startsWith("///")) || (!str.startsWith("//")))
/*     */           {
/* 243 */             int secondColonIndex = systemId.indexOf(':', 5);
/* 244 */             if (secondColonIndex > 0)
/*     */             {
/* 246 */               String localPath = systemId.substring(secondColonIndex - 1);
/*     */               try {
/* 248 */                 if (!isAbsolutePath(localPath))
/* 249 */                   absoluteURI = systemId.substring(0, secondColonIndex - 1) + getAbsolutePathFromRelativePath(localPath);
/*     */               }
/*     */               catch (SecurityException se)
/*     */               {
/* 253 */                 return systemId;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 260 */           return getAbsoluteURIFromRelative(systemId.substring(5));
/*     */         }
/*     */ 
/* 263 */         return replaceChars(absoluteURI);
/*     */       }
/*     */ 
/* 266 */       return systemId;
/*     */     }
/*     */ 
/* 269 */     return getAbsoluteURIFromRelative(systemId);
/*     */   }
/*     */ 
/*     */   public static String getAbsoluteURI(String urlString, String base)
/*     */     throws TransformerException
/*     */   {
/* 286 */     if (base == null) {
/* 287 */       return getAbsoluteURI(urlString);
/*     */     }
/* 289 */     String absoluteBase = getAbsoluteURI(base);
/* 290 */     URI uri = null;
/*     */     try
/*     */     {
/* 293 */       URI baseURI = new URI(absoluteBase);
/* 294 */       uri = new URI(baseURI, urlString);
/*     */     }
/*     */     catch (URI.MalformedURIException mue)
/*     */     {
/* 298 */       throw new TransformerException(mue);
/*     */     }
/*     */ 
/* 301 */     return replaceChars(uri.toString());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.utils.SystemIDResolver
 * JD-Core Version:    0.6.2
 */