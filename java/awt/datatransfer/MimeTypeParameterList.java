/*     */ package java.awt.datatransfer;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ class MimeTypeParameterList
/*     */   implements Cloneable
/*     */ {
/*     */   private Hashtable parameters;
/*     */   private static final String TSPECIALS = "()<>@,;:\\\"/[]?=";
/*     */ 
/*     */   public MimeTypeParameterList()
/*     */   {
/*  47 */     this.parameters = new Hashtable();
/*     */   }
/*     */ 
/*     */   public MimeTypeParameterList(String paramString)
/*     */     throws MimeTypeParseException
/*     */   {
/*  53 */     this.parameters = new Hashtable();
/*     */ 
/*  56 */     parse(paramString);
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  60 */     int i = 47721858;
/*  61 */     String str = null;
/*  62 */     Enumeration localEnumeration = getNames();
/*     */ 
/*  64 */     while (localEnumeration.hasMoreElements()) {
/*  65 */       str = (String)localEnumeration.nextElement();
/*  66 */       i += str.hashCode();
/*  67 */       i += get(str).hashCode();
/*     */     }
/*     */ 
/*  70 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  80 */     if (!(paramObject instanceof MimeTypeParameterList)) {
/*  81 */       return false;
/*     */     }
/*  83 */     MimeTypeParameterList localMimeTypeParameterList = (MimeTypeParameterList)paramObject;
/*  84 */     if (size() != localMimeTypeParameterList.size()) {
/*  85 */       return false;
/*     */     }
/*  87 */     String str1 = null;
/*  88 */     String str2 = null;
/*  89 */     String str3 = null;
/*  90 */     Set localSet = this.parameters.entrySet();
/*  91 */     Iterator localIterator = localSet.iterator();
/*  92 */     Map.Entry localEntry = null;
/*  93 */     while (localIterator.hasNext()) {
/*  94 */       localEntry = (Map.Entry)localIterator.next();
/*  95 */       str1 = (String)localEntry.getKey();
/*  96 */       str2 = (String)localEntry.getValue();
/*  97 */       str3 = (String)localMimeTypeParameterList.parameters.get(str1);
/*  98 */       if ((str2 == null) || (str3 == null))
/*     */       {
/* 100 */         if (str2 != str3)
/* 101 */           return false;
/*     */       }
/* 103 */       else if (!str2.equals(str3)) {
/* 104 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 108 */     return true;
/*     */   }
/*     */ 
/*     */   protected void parse(String paramString)
/*     */     throws MimeTypeParseException
/*     */   {
/* 115 */     int i = paramString.length();
/* 116 */     if (i > 0) {
/* 117 */       int j = skipWhiteSpace(paramString, 0);
/* 118 */       int k = 0;
/*     */ 
/* 120 */       if (j < i) {
/* 121 */         char c = paramString.charAt(j);
/* 122 */         while ((j < i) && (c == ';'))
/*     */         {
/* 128 */           j++;
/*     */ 
/* 133 */           j = skipWhiteSpace(paramString, j);
/*     */ 
/* 135 */           if (j < i)
/*     */           {
/* 137 */             k = j;
/* 138 */             c = paramString.charAt(j);
/* 139 */             while ((j < i) && (isTokenChar(c))) {
/* 140 */               j++;
/* 141 */               c = paramString.charAt(j);
/*     */             }
/* 143 */             String str1 = paramString.substring(k, j).toLowerCase();
/*     */ 
/* 148 */             j = skipWhiteSpace(paramString, j);
/*     */ 
/* 150 */             if ((j < i) && (paramString.charAt(j) == '='))
/*     */             {
/* 152 */               j++;
/*     */ 
/* 155 */               j = skipWhiteSpace(paramString, j);
/*     */ 
/* 157 */               if (j < i)
/*     */               {
/* 159 */                 c = paramString.charAt(j);
/*     */                 int m;
/*     */                 String str2;
/* 160 */                 if (c == '"')
/*     */                 {
/* 162 */                   j++;
/* 163 */                   k = j;
/*     */ 
/* 165 */                   if (j < i)
/*     */                   {
/* 167 */                     m = 0;
/* 168 */                     while ((j < i) && (m == 0)) {
/* 169 */                       c = paramString.charAt(j);
/* 170 */                       if (c == '\\')
/*     */                       {
/* 172 */                         j += 2;
/* 173 */                       } else if (c == '"')
/*     */                       {
/* 175 */                         m = 1;
/*     */                       }
/* 177 */                       else j++;
/*     */                     }
/*     */ 
/* 180 */                     if (c == '"') {
/* 181 */                       str2 = unquote(paramString.substring(k, j));
/*     */ 
/* 183 */                       j++;
/*     */                     } else {
/* 185 */                       throw new MimeTypeParseException("Encountered unterminated quoted parameter value.");
/*     */                     }
/*     */                   } else {
/* 188 */                     throw new MimeTypeParseException("Encountered unterminated quoted parameter value.");
/*     */                   }
/* 190 */                 } else if (isTokenChar(c))
/*     */                 {
/* 192 */                   k = j;
/* 193 */                   m = 0;
/* 194 */                   while ((j < i) && (m == 0)) {
/* 195 */                     c = paramString.charAt(j);
/*     */ 
/* 197 */                     if (isTokenChar(c))
/* 198 */                       j++;
/*     */                     else {
/* 200 */                       m = 1;
/*     */                     }
/*     */                   }
/* 203 */                   str2 = paramString.substring(k, j);
/*     */                 }
/*     */                 else {
/* 206 */                   throw new MimeTypeParseException("Unexpected character encountered at index " + j);
/*     */                 }
/*     */ 
/* 210 */                 this.parameters.put(str1, str2);
/*     */               } else {
/* 212 */                 throw new MimeTypeParseException("Couldn't find a value for parameter named " + str1);
/*     */               }
/*     */             } else {
/* 215 */               throw new MimeTypeParseException("Couldn't find the '=' that separates a parameter name from its value.");
/*     */             }
/*     */           } else {
/* 218 */             throw new MimeTypeParseException("Couldn't find parameter name");
/*     */           }
/*     */ 
/* 222 */           j = skipWhiteSpace(paramString, j);
/* 223 */           if (j < i) {
/* 224 */             c = paramString.charAt(j);
/*     */           }
/*     */         }
/* 227 */         if (j < i)
/* 228 */           throw new MimeTypeParseException("More characters encountered in input than expected.");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 238 */     return this.parameters.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 245 */     return this.parameters.isEmpty();
/*     */   }
/*     */ 
/*     */   public String get(String paramString)
/*     */   {
/* 253 */     return (String)this.parameters.get(paramString.trim().toLowerCase());
/*     */   }
/*     */ 
/*     */   public void set(String paramString1, String paramString2)
/*     */   {
/* 261 */     this.parameters.put(paramString1.trim().toLowerCase(), paramString2);
/*     */   }
/*     */ 
/*     */   public void remove(String paramString)
/*     */   {
/* 268 */     this.parameters.remove(paramString.trim().toLowerCase());
/*     */   }
/*     */ 
/*     */   public Enumeration getNames()
/*     */   {
/* 275 */     return this.parameters.keys();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 280 */     StringBuilder localStringBuilder = new StringBuilder(this.parameters.size() * 16);
/*     */ 
/* 282 */     Enumeration localEnumeration = this.parameters.keys();
/* 283 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 285 */       localStringBuilder.append("; ");
/*     */ 
/* 287 */       String str = (String)localEnumeration.nextElement();
/* 288 */       localStringBuilder.append(str);
/* 289 */       localStringBuilder.append('=');
/* 290 */       localStringBuilder.append(quote((String)this.parameters.get(str)));
/*     */     }
/*     */ 
/* 293 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 301 */     MimeTypeParameterList localMimeTypeParameterList = null;
/*     */     try {
/* 303 */       localMimeTypeParameterList = (MimeTypeParameterList)super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 306 */     localMimeTypeParameterList.parameters = ((Hashtable)this.parameters.clone());
/* 307 */     return localMimeTypeParameterList;
/*     */   }
/*     */ 
/*     */   private static boolean isTokenChar(char paramChar)
/*     */   {
/* 318 */     return (paramChar > ' ') && (paramChar < '') && ("()<>@,;:\\\"/[]?=".indexOf(paramChar) < 0);
/*     */   }
/*     */ 
/*     */   private static int skipWhiteSpace(String paramString, int paramInt)
/*     */   {
/* 326 */     int i = paramString.length();
/* 327 */     if (paramInt < i) {
/* 328 */       char c = paramString.charAt(paramInt);
/* 329 */       while ((paramInt < i) && (Character.isWhitespace(c))) {
/* 330 */         paramInt++;
/* 331 */         c = paramString.charAt(paramInt);
/*     */       }
/*     */     }
/*     */ 
/* 335 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private static String quote(String paramString)
/*     */   {
/* 342 */     int i = 0;
/*     */ 
/* 345 */     int j = paramString.length();
/* 346 */     for (int k = 0; (k < j) && (i == 0); k++) {
/* 347 */       i = !isTokenChar(paramString.charAt(k)) ? 1 : 0;
/*     */     }
/*     */ 
/* 350 */     if (i != 0) {
/* 351 */       StringBuilder localStringBuilder = new StringBuilder((int)(j * 1.5D));
/*     */ 
/* 354 */       localStringBuilder.append('"');
/*     */ 
/* 357 */       for (int m = 0; m < j; m++) {
/* 358 */         char c = paramString.charAt(m);
/* 359 */         if ((c == '\\') || (c == '"')) {
/* 360 */           localStringBuilder.append('\\');
/*     */         }
/* 362 */         localStringBuilder.append(c);
/*     */       }
/*     */ 
/* 366 */       localStringBuilder.append('"');
/*     */ 
/* 368 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/* 372 */     return paramString;
/*     */   }
/*     */ 
/*     */   private static String unquote(String paramString)
/*     */   {
/* 380 */     int i = paramString.length();
/* 381 */     StringBuilder localStringBuilder = new StringBuilder(i);
/*     */ 
/* 383 */     int j = 0;
/* 384 */     for (int k = 0; k < i; k++) {
/* 385 */       char c = paramString.charAt(k);
/* 386 */       if ((j == 0) && (c != '\\')) {
/* 387 */         localStringBuilder.append(c);
/* 388 */       } else if (j != 0) {
/* 389 */         localStringBuilder.append(c);
/* 390 */         j = 0;
/*     */       } else {
/* 392 */         j = 1;
/*     */       }
/*     */     }
/*     */ 
/* 396 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.datatransfer.MimeTypeParameterList
 * JD-Core Version:    0.6.2
 */