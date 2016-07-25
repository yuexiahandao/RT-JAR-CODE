/*     */ package com.sun.org.apache.xml.internal.security.utils.resolver;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import java.io.File;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Attr;
/*     */ 
/*     */ public abstract class ResourceResolverSpi
/*     */ {
/*  39 */   static Logger log = Logger.getLogger(ResourceResolverSpi.class.getName());
/*     */ 
/*  44 */   protected Map<String, String> _properties = null;
/*     */   protected boolean secureValidation;
/*     */ 
/*     */   public abstract XMLSignatureInput engineResolve(Attr paramAttr, String paramString)
/*     */     throws ResourceResolverException;
/*     */ 
/*     */   public void engineSetProperty(String paramString1, String paramString2)
/*     */   {
/*  67 */     if (this._properties == null) {
/*  68 */       this._properties = new HashMap();
/*     */     }
/*  70 */     this._properties.put(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public String engineGetProperty(String paramString)
/*     */   {
/*  80 */     if (this._properties == null) {
/*  81 */       return null;
/*     */     }
/*  83 */     return (String)this._properties.get(paramString);
/*     */   }
/*     */ 
/*     */   public void engineAddProperies(Map<String, String> paramMap)
/*     */   {
/*  91 */     if (paramMap != null) {
/*  92 */       if (this._properties == null) {
/*  93 */         this._properties = new HashMap();
/*     */       }
/*  95 */       this._properties.putAll(paramMap);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean engineIsThreadSafe()
/*     */   {
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract boolean engineCanResolve(Attr paramAttr, String paramString);
/*     */ 
/*     */   public String[] engineGetPropertyKeys()
/*     */   {
/* 123 */     return new String[0];
/*     */   }
/*     */ 
/*     */   public boolean understandsProperty(String paramString)
/*     */   {
/* 134 */     String[] arrayOfString = engineGetPropertyKeys();
/*     */ 
/* 136 */     if (arrayOfString != null) {
/* 137 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 138 */         if (arrayOfString[i].equals(paramString)) {
/* 139 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   public static String fixURI(String paramString)
/*     */   {
/* 158 */     paramString = paramString.replace(File.separatorChar, '/');
/*     */     int i;
/*     */     int j;
/* 160 */     if (paramString.length() >= 4)
/*     */     {
/* 163 */       i = Character.toUpperCase(paramString.charAt(0));
/* 164 */       j = paramString.charAt(1);
/* 165 */       int k = paramString.charAt(2);
/* 166 */       int m = paramString.charAt(3);
/* 167 */       int n = (65 <= i) && (i <= 90) && (j == 58) && (k == 47) && (m != 47) ? 1 : 0;
/*     */ 
/* 171 */       if ((n != 0) && 
/* 172 */         (log.isLoggable(Level.FINE))) {
/* 173 */         log.log(Level.FINE, "Found DOS filename: " + paramString);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 178 */     if (paramString.length() >= 2) {
/* 179 */       i = paramString.charAt(1);
/*     */ 
/* 181 */       if (i == 58) {
/* 182 */         j = Character.toUpperCase(paramString.charAt(0));
/*     */ 
/* 184 */         if ((65 <= j) && (j <= 90)) {
/* 185 */           paramString = "/" + paramString;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 191 */     return paramString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
 * JD-Core Version:    0.6.2
 */