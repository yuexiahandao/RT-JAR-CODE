/*     */ package sun.security.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.security.GeneralSecurityException;
/*     */ import sun.net.www.ParseUtil;
/*     */ 
/*     */ public class PropertyExpander
/*     */ {
/*     */   public static String expand(String paramString)
/*     */     throws PropertyExpander.ExpandException
/*     */   {
/*  56 */     return expand(paramString, false);
/*     */   }
/*     */ 
/*     */   public static String expand(String paramString, boolean paramBoolean)
/*     */     throws PropertyExpander.ExpandException
/*     */   {
/*  62 */     if (paramString == null) {
/*  63 */       return null;
/*     */     }
/*  65 */     int i = paramString.indexOf("${", 0);
/*     */ 
/*  68 */     if (i == -1) return paramString;
/*     */ 
/*  70 */     StringBuffer localStringBuffer = new StringBuffer(paramString.length());
/*  71 */     int j = paramString.length();
/*  72 */     int k = 0;
/*     */ 
/*  75 */     while (i < j) {
/*  76 */       if (i > k)
/*     */       {
/*  78 */         localStringBuffer.append(paramString.substring(k, i));
/*  79 */         k = i;
/*     */       }
/*  81 */       int m = i + 2;
/*     */ 
/*  84 */       if ((m < j) && (paramString.charAt(m) == '{')) {
/*  85 */         m = paramString.indexOf("}}", m);
/*  86 */         if ((m == -1) || (m + 2 == j))
/*     */         {
/*  88 */           localStringBuffer.append(paramString.substring(i));
/*  89 */           break;
/*     */         }
/*     */ 
/*  92 */         m++;
/*  93 */         localStringBuffer.append(paramString.substring(i, m + 1));
/*     */       }
/*     */       else {
/*  96 */         while ((m < j) && (paramString.charAt(m) != '}')) {
/*  97 */           m++;
/*     */         }
/*  99 */         if (m == j)
/*     */         {
/* 101 */           localStringBuffer.append(paramString.substring(i, m));
/* 102 */           break;
/*     */         }
/* 104 */         String str1 = paramString.substring(i + 2, m);
/* 105 */         if (str1.equals("/")) {
/* 106 */           localStringBuffer.append(File.separatorChar);
/*     */         } else {
/* 108 */           String str2 = System.getProperty(str1);
/* 109 */           if (str2 != null) {
/* 110 */             if (paramBoolean)
/*     */             {
/*     */               try
/*     */               {
/* 114 */                 if ((localStringBuffer.length() > 0) || (!new URI(str2).isAbsolute()))
/*     */                 {
/* 116 */                   str2 = ParseUtil.encodePath(str2);
/*     */                 }
/*     */               } catch (URISyntaxException localURISyntaxException) {
/* 119 */                 str2 = ParseUtil.encodePath(str2);
/*     */               }
/*     */             }
/* 122 */             localStringBuffer.append(str2);
/*     */           } else {
/* 124 */             throw new ExpandException("unable to expand property " + str1);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 130 */       k = m + 1;
/* 131 */       i = paramString.indexOf("${", k);
/* 132 */       if (i == -1)
/*     */       {
/* 134 */         if (k >= j) break;
/* 135 */         localStringBuffer.append(paramString.substring(k, j)); break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 141 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public static class ExpandException extends GeneralSecurityException
/*     */   {
/*     */     private static final long serialVersionUID = -7941948581406161702L;
/*     */ 
/*     */     public ExpandException(String paramString)
/*     */     {
/*  49 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.PropertyExpander
 * JD-Core Version:    0.6.2
 */