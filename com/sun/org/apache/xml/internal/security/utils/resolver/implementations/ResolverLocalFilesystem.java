/*     */ package com.sun.org.apache.xml.internal.security.utils.resolver.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;
/*     */ import com.sun.org.apache.xml.internal.utils.URI;
/*     */ import com.sun.org.apache.xml.internal.utils.URI.MalformedURIException;
/*     */ import java.io.FileInputStream;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Attr;
/*     */ 
/*     */ public class ResolverLocalFilesystem extends ResourceResolverSpi
/*     */ {
/*  39 */   static Logger log = Logger.getLogger(ResolverLocalFilesystem.class.getName());
/*     */ 
/*  75 */   private static int FILE_URI_LENGTH = "file:/".length();
/*     */ 
/*     */   public boolean engineIsThreadSafe()
/*     */   {
/*  44 */     return true;
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput engineResolve(Attr paramAttr, String paramString)
/*     */     throws ResourceResolverException
/*     */   {
/*     */     try
/*     */     {
/*  53 */       URI localURI1 = getNewURI(paramAttr.getNodeValue(), paramString);
/*     */ 
/*  56 */       URI localURI2 = new URI(localURI1);
/*     */ 
/*  58 */       localURI2.setFragment(null);
/*     */ 
/*  60 */       String str = translateUriToFilename(localURI2.toString());
/*     */ 
/*  63 */       FileInputStream localFileInputStream = new FileInputStream(str);
/*  64 */       XMLSignatureInput localXMLSignatureInput = new XMLSignatureInput(localFileInputStream);
/*     */ 
/*  66 */       localXMLSignatureInput.setSourceURI(localURI1.toString());
/*     */ 
/*  68 */       return localXMLSignatureInput;
/*     */     } catch (Exception localException) {
/*  70 */       throw new ResourceResolverException("generic.EmptyMessage", localException, paramAttr, paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String translateUriToFilename(String paramString)
/*     */   {
/*  84 */     String str = paramString.substring(FILE_URI_LENGTH);
/*     */ 
/*  86 */     if (str.indexOf("%20") > -1)
/*     */     {
/*  88 */       int i = 0;
/*  89 */       int j = 0;
/*  90 */       StringBuffer localStringBuffer = new StringBuffer(str.length());
/*     */       do
/*     */       {
/*  93 */         j = str.indexOf("%20", i);
/*  94 */         if (j == -1) { localStringBuffer.append(str.substring(i));
/*     */         } else
/*     */         {
/*  97 */           localStringBuffer.append(str.substring(i, j));
/*  98 */           localStringBuffer.append(' ');
/*  99 */           i = j + 3;
/*     */         }
/*     */       }
/* 102 */       while (j != -1);
/* 103 */       str = localStringBuffer.toString();
/*     */     }
/*     */ 
/* 106 */     if (str.charAt(1) == ':')
/*     */     {
/* 108 */       return str;
/*     */     }
/*     */ 
/* 111 */     return "/" + str;
/*     */   }
/*     */ 
/*     */   public boolean engineCanResolve(Attr paramAttr, String paramString)
/*     */   {
/* 119 */     if (paramAttr == null) {
/* 120 */       return false;
/*     */     }
/*     */ 
/* 123 */     String str = paramAttr.getNodeValue();
/*     */ 
/* 125 */     if ((str.equals("")) || (str.charAt(0) == '#') || (str.startsWith("http:")))
/*     */     {
/* 127 */       return false;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 132 */       if (log.isLoggable(Level.FINE)) {
/* 133 */         log.log(Level.FINE, "I was asked whether I can resolve " + str);
/*     */       }
/* 135 */       if ((str.startsWith("file:")) || (paramString.startsWith("file:")))
/*     */       {
/* 137 */         if (log.isLoggable(Level.FINE)) {
/* 138 */           log.log(Level.FINE, "I state that I can resolve " + str);
/*     */         }
/* 140 */         return true;
/*     */       }
/*     */     } catch (Exception localException) {
/*     */     }
/* 144 */     log.log(Level.FINE, "But I can't");
/*     */ 
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   private static URI getNewURI(String paramString1, String paramString2)
/*     */     throws URI.MalformedURIException
/*     */   {
/* 152 */     if ((paramString2 == null) || ("".equals(paramString2))) {
/* 153 */       return new URI(paramString1);
/*     */     }
/* 155 */     return new URI(new URI(paramString2), paramString1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverLocalFilesystem
 * JD-Core Version:    0.6.2
 */