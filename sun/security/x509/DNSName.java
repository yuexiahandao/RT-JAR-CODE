/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class DNSName
/*     */   implements GeneralNameInterface
/*     */ {
/*     */   private String name;
/*     */   private static final String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
/*     */   private static final String digitsAndHyphen = "0123456789-";
/*     */   private static final String alphaDigitsAndHyphen = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-";
/*     */ 
/*     */   public DNSName(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  65 */     this.name = paramDerValue.getIA5String();
/*     */   }
/*     */ 
/*     */   public DNSName(String paramString)
/*     */     throws IOException
/*     */   {
/*  75 */     if ((paramString == null) || (paramString.length() == 0))
/*  76 */       throw new IOException("DNS name must not be null");
/*  77 */     if (paramString.indexOf(' ') != -1)
/*  78 */       throw new IOException("DNS names or NameConstraints with blank components are not permitted");
/*  79 */     if ((paramString.charAt(0) == '.') || (paramString.charAt(paramString.length() - 1) == '.'))
/*  80 */       throw new IOException("DNS names or NameConstraints may not begin or end with a .");
/*     */     int i;
/*  84 */     for (int j = 0; j < paramString.length(); j = i + 1) {
/*  85 */       i = paramString.indexOf('.', j);
/*  86 */       if (i < 0) {
/*  87 */         i = paramString.length();
/*     */       }
/*  89 */       if (i - j < 1) {
/*  90 */         throw new IOException("DNSName SubjectAltNames with empty components are not permitted");
/*     */       }
/*     */ 
/*  93 */       if ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".indexOf(paramString.charAt(j)) < 0) {
/*  94 */         throw new IOException("DNSName components must begin with a letter");
/*     */       }
/*  96 */       for (int k = j + 1; k < i; k++) {
/*  97 */         int m = paramString.charAt(k);
/*  98 */         if ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-".indexOf(m) < 0)
/*  99 */           throw new IOException("DNSName components must consist of letters, digits, and hyphens");
/*     */       }
/*     */     }
/* 102 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 109 */     return 2;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 116 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 126 */     paramDerOutputStream.putIA5String(this.name);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 133 */     return "DNSName: " + this.name;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 143 */     if (this == paramObject) {
/* 144 */       return true;
/*     */     }
/* 146 */     if (!(paramObject instanceof DNSName)) {
/* 147 */       return false;
/*     */     }
/* 149 */     DNSName localDNSName = (DNSName)paramObject;
/*     */ 
/* 153 */     return this.name.equalsIgnoreCase(localDNSName.name);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 162 */     return this.name.toUpperCase().hashCode();
/*     */   }
/*     */ 
/*     */   public int constrains(GeneralNameInterface paramGeneralNameInterface)
/*     */     throws UnsupportedOperationException
/*     */   {
/*     */     int i;
/* 197 */     if (paramGeneralNameInterface == null) {
/* 198 */       i = -1;
/* 199 */     } else if (paramGeneralNameInterface.getType() != 2) {
/* 200 */       i = -1;
/*     */     } else {
/* 202 */       String str1 = ((DNSName)paramGeneralNameInterface).getName().toLowerCase(Locale.ENGLISH);
/*     */ 
/* 204 */       String str2 = this.name.toLowerCase(Locale.ENGLISH);
/* 205 */       if (str1.equals(str2)) {
/* 206 */         i = 0;
/*     */       }
/*     */       else
/*     */       {
/*     */         int j;
/* 207 */         if (str2.endsWith(str1)) {
/* 208 */           j = str2.lastIndexOf(str1);
/* 209 */           if (str2.charAt(j - 1) == '.')
/* 210 */             i = 2;
/*     */           else
/* 212 */             i = 3;
/* 213 */         } else if (str1.endsWith(str2)) {
/* 214 */           j = str1.lastIndexOf(str2);
/* 215 */           if (str1.charAt(j - 1) == '.')
/* 216 */             i = 1;
/*     */           else
/* 218 */             i = 3;
/*     */         } else {
/* 220 */           i = 3;
/*     */         }
/*     */       }
/*     */     }
/* 223 */     return i;
/*     */   }
/*     */ 
/*     */   public int subtreeDepth()
/*     */     throws UnsupportedOperationException
/*     */   {
/* 235 */     String str = this.name;
/* 236 */     for (int i = 1; 
/* 239 */       str.lastIndexOf('.') >= 0; i++) {
/* 240 */       str = str.substring(0, str.lastIndexOf('.'));
/*     */     }
/*     */ 
/* 243 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.DNSName
 * JD-Core Version:    0.6.2
 */