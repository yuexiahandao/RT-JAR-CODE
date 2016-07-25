/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class RFC822Name
/*     */   implements GeneralNameInterface
/*     */ {
/*     */   private String name;
/*     */ 
/*     */   public RFC822Name(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  54 */     this.name = paramDerValue.getIA5String();
/*  55 */     parseName(this.name);
/*     */   }
/*     */ 
/*     */   public RFC822Name(String paramString)
/*     */     throws IOException
/*     */   {
/*  65 */     parseName(paramString);
/*  66 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public void parseName(String paramString)
/*     */     throws IOException
/*     */   {
/*  82 */     if ((paramString == null) || (paramString.length() == 0)) {
/*  83 */       throw new IOException("RFC822Name may not be null or empty");
/*     */     }
/*     */ 
/*  86 */     String str = paramString.substring(paramString.indexOf('@') + 1);
/*  87 */     if (str.length() == 0) {
/*  88 */       throw new IOException("RFC822Name may not end with @");
/*     */     }
/*     */ 
/*  92 */     if ((str.startsWith(".")) && 
/*  93 */       (str.length() == 1))
/*  94 */       throw new IOException("RFC822Name domain may not be just .");
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 103 */     return 1;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 110 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 120 */     paramDerOutputStream.putIA5String(this.name);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 127 */     return "RFC822Name: " + this.name;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 137 */     if (this == paramObject) {
/* 138 */       return true;
/*     */     }
/* 140 */     if (!(paramObject instanceof RFC822Name)) {
/* 141 */       return false;
/*     */     }
/* 143 */     RFC822Name localRFC822Name = (RFC822Name)paramObject;
/*     */ 
/* 147 */     return this.name.equalsIgnoreCase(localRFC822Name.name);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 156 */     return this.name.toUpperCase().hashCode();
/*     */   }
/*     */ 
/*     */   public int constrains(GeneralNameInterface paramGeneralNameInterface)
/*     */     throws UnsupportedOperationException
/*     */   {
/*     */     int i;
/* 185 */     if (paramGeneralNameInterface == null) {
/* 186 */       i = -1;
/* 187 */     } else if (paramGeneralNameInterface.getType() != 1) {
/* 188 */       i = -1;
/*     */     }
/*     */     else {
/* 191 */       String str1 = ((RFC822Name)paramGeneralNameInterface).getName().toLowerCase(Locale.ENGLISH);
/*     */ 
/* 193 */       String str2 = this.name.toLowerCase(Locale.ENGLISH);
/* 194 */       if (str1.equals(str2)) {
/* 195 */         i = 0;
/*     */       }
/*     */       else
/*     */       {
/*     */         int j;
/* 196 */         if (str2.endsWith(str1))
/*     */         {
/* 198 */           if (str1.indexOf('@') != -1) {
/* 199 */             i = 3;
/* 200 */           } else if (str1.startsWith(".")) {
/* 201 */             i = 2;
/*     */           } else {
/* 203 */             j = str2.lastIndexOf(str1);
/* 204 */             if (str2.charAt(j - 1) == '@')
/* 205 */               i = 2;
/*     */             else
/* 207 */               i = 3;
/*     */           }
/*     */         }
/* 210 */         else if (str1.endsWith(str2))
/*     */         {
/* 212 */           if (str2.indexOf('@') != -1) {
/* 213 */             i = 3;
/* 214 */           } else if (str2.startsWith(".")) {
/* 215 */             i = 1;
/*     */           } else {
/* 217 */             j = str1.lastIndexOf(str2);
/* 218 */             if (str1.charAt(j - 1) == '@')
/* 219 */               i = 1;
/*     */             else
/* 221 */               i = 3;
/*     */           }
/*     */         }
/*     */         else
/* 225 */           i = 3;
/*     */       }
/*     */     }
/* 228 */     return i;
/*     */   }
/*     */ 
/*     */   public int subtreeDepth()
/*     */     throws UnsupportedOperationException
/*     */   {
/* 239 */     String str = this.name;
/* 240 */     int i = 1;
/*     */ 
/* 243 */     int j = str.lastIndexOf('@');
/* 244 */     if (j >= 0) {
/* 245 */       i++;
/* 246 */       str = str.substring(j + 1);
/*     */     }
/*     */ 
/* 250 */     for (; str.lastIndexOf('.') >= 0; i++) {
/* 251 */       str = str.substring(0, str.lastIndexOf('.'));
/*     */     }
/*     */ 
/* 254 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.RFC822Name
 * JD-Core Version:    0.6.2
 */