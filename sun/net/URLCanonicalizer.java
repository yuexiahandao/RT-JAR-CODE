/*     */ package sun.net;
/*     */ 
/*     */ public class URLCanonicalizer
/*     */ {
/*     */   public String canonicalize(String paramString)
/*     */   {
/*  58 */     String str = paramString;
/*  59 */     if (paramString.startsWith("ftp.")) {
/*  60 */       str = "ftp://" + paramString;
/*  61 */     } else if (paramString.startsWith("gopher.")) {
/*  62 */       str = "gopher://" + paramString;
/*  63 */     } else if (paramString.startsWith("/")) {
/*  64 */       str = "file:" + paramString;
/*  65 */     } else if (!hasProtocolName(paramString)) {
/*  66 */       if (isSimpleHostName(paramString)) {
/*  67 */         paramString = "www." + paramString + ".com";
/*     */       }
/*  69 */       str = "http://" + paramString;
/*     */     }
/*     */ 
/*  72 */     return str;
/*     */   }
/*     */ 
/*     */   public boolean hasProtocolName(String paramString)
/*     */   {
/*  80 */     int i = paramString.indexOf(':');
/*  81 */     if (i <= 0) {
/*  82 */       return false;
/*     */     }
/*     */ 
/*  85 */     for (int j = 0; j < i; j++) {
/*  86 */       int k = paramString.charAt(j);
/*     */ 
/*  90 */       if (((k < 65) || (k > 90)) && ((k < 97) || (k > 122)) && (k != 45))
/*     */       {
/*  97 */         return false;
/*     */       }
/*     */     }
/* 100 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean isSimpleHostName(String paramString)
/*     */   {
/* 109 */     for (int i = 0; i < paramString.length(); i++) {
/* 110 */       int j = paramString.charAt(i);
/*     */ 
/* 114 */       if (((j < 65) || (j > 90)) && ((j < 97) || (j > 122)) && ((j < 48) || (j > 57)) && (j != 45))
/*     */       {
/* 122 */         return false;
/*     */       }
/*     */     }
/* 125 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.URLCanonicalizer
 * JD-Core Version:    0.6.2
 */