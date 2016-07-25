/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.io.File;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class SystemIDResolver
/*     */ {
/*     */   public static String getAbsoluteURIFromRelative(String localPath)
/*     */   {
/*  59 */     if ((localPath == null) || (localPath.length() == 0)) {
/*  60 */       return "";
/*     */     }
/*     */ 
/*  64 */     String absolutePath = localPath;
/*  65 */     if (!isAbsolutePath(localPath))
/*     */     {
/*     */       try
/*     */       {
/*  69 */         absolutePath = getAbsolutePathFromRelativePath(localPath);
/*     */       }
/*     */       catch (SecurityException se)
/*     */       {
/*  74 */         return "file:" + localPath;
/*     */       }
/*     */     }
/*     */     String urlString;
/*     */     String urlString;
/*  79 */     if (null != absolutePath)
/*     */     {
/*     */       String urlString;
/*  81 */       if (absolutePath.startsWith(File.separator))
/*  82 */         urlString = "file://" + absolutePath;
/*     */       else
/*  84 */         urlString = "file:///" + absolutePath;
/*     */     }
/*     */     else {
/*  87 */       urlString = "file:" + localPath;
/*     */     }
/*  89 */     return replaceChars(urlString);
/*     */   }
/*     */ 
/*     */   private static String getAbsolutePathFromRelativePath(String relativePath)
/*     */   {
/* 100 */     return new File(relativePath).getAbsolutePath();
/*     */   }
/*     */ 
/*     */   public static boolean isAbsoluteURI(String systemId)
/*     */   {
/* 122 */     if (isWindowsAbsolutePath(systemId)) {
/* 123 */       return false;
/*     */     }
/*     */ 
/* 126 */     int fragmentIndex = systemId.indexOf('#');
/* 127 */     int queryIndex = systemId.indexOf('?');
/* 128 */     int slashIndex = systemId.indexOf('/');
/* 129 */     int colonIndex = systemId.indexOf(':');
/*     */ 
/* 132 */     int index = systemId.length() - 1;
/* 133 */     if (fragmentIndex > 0)
/* 134 */       index = fragmentIndex;
/* 135 */     if ((queryIndex > 0) && (queryIndex < index))
/* 136 */       index = queryIndex;
/* 137 */     if ((slashIndex > 0) && (slashIndex < index)) {
/* 138 */       index = slashIndex;
/*     */     }
/* 140 */     return (colonIndex > 0) && (colonIndex < index);
/*     */   }
/*     */ 
/*     */   public static boolean isAbsolutePath(String systemId)
/*     */   {
/* 152 */     if (systemId == null)
/* 153 */       return false;
/* 154 */     File file = new File(systemId);
/* 155 */     return file.isAbsolute();
/*     */   }
/*     */ 
/*     */   private static boolean isWindowsAbsolutePath(String systemId)
/*     */   {
/* 167 */     if (!isAbsolutePath(systemId)) {
/* 168 */       return false;
/*     */     }
/* 170 */     if ((systemId.length() > 2) && (systemId.charAt(1) == ':') && (Character.isLetter(systemId.charAt(0))) && ((systemId.charAt(2) == '\\') || (systemId.charAt(2) == '/')))
/*     */     {
/* 174 */       return true;
/*     */     }
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */   private static String replaceChars(String str)
/*     */   {
/* 188 */     StringBuffer buf = new StringBuffer(str);
/* 189 */     int length = buf.length();
/* 190 */     for (int i = 0; i < length; i++)
/*     */     {
/* 192 */       char currentChar = buf.charAt(i);
/*     */ 
/* 194 */       if (currentChar == ' ')
/*     */       {
/* 196 */         buf.setCharAt(i, '%');
/* 197 */         buf.insert(i + 1, "20");
/* 198 */         length += 2;
/* 199 */         i += 2;
/*     */       }
/* 202 */       else if (currentChar == '\\')
/*     */       {
/* 204 */         buf.setCharAt(i, '/');
/*     */       }
/*     */     }
/*     */ 
/* 208 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public static String getAbsoluteURI(String systemId)
/*     */   {
/* 220 */     String absoluteURI = systemId;
/* 221 */     if (isAbsoluteURI(systemId))
/*     */     {
/* 224 */       if (systemId.startsWith("file:"))
/*     */       {
/* 226 */         String str = systemId.substring(5);
/*     */ 
/* 230 */         if ((str != null) && (str.startsWith("/")))
/*     */         {
/* 232 */           if ((str.startsWith("///")) || (!str.startsWith("//")))
/*     */           {
/* 236 */             int secondColonIndex = systemId.indexOf(':', 5);
/* 237 */             if (secondColonIndex > 0)
/*     */             {
/* 239 */               String localPath = systemId.substring(secondColonIndex - 1);
/*     */               try {
/* 241 */                 if (!isAbsolutePath(localPath))
/* 242 */                   absoluteURI = systemId.substring(0, secondColonIndex - 1) + getAbsolutePathFromRelativePath(localPath);
/*     */               }
/*     */               catch (SecurityException se)
/*     */               {
/* 246 */                 return systemId;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 253 */           return getAbsoluteURIFromRelative(systemId.substring(5));
/*     */         }
/*     */ 
/* 256 */         return replaceChars(absoluteURI);
/*     */       }
/*     */ 
/* 259 */       return systemId;
/*     */     }
/*     */ 
/* 262 */     return getAbsoluteURIFromRelative(systemId);
/*     */   }
/*     */ 
/*     */   public static String getAbsoluteURI(String urlString, String base)
/*     */     throws TransformerException
/*     */   {
/* 279 */     if (base == null) {
/* 280 */       return getAbsoluteURI(urlString);
/*     */     }
/* 282 */     String absoluteBase = getAbsoluteURI(base);
/* 283 */     URI uri = null;
/*     */     try
/*     */     {
/* 286 */       URI baseURI = new URI(absoluteBase);
/* 287 */       uri = new URI(baseURI, urlString);
/*     */     }
/*     */     catch (URI.MalformedURIException mue)
/*     */     {
/* 291 */       throw new TransformerException(mue);
/*     */     }
/*     */ 
/* 294 */     return replaceChars(uri.toString());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.SystemIDResolver
 * JD-Core Version:    0.6.2
 */