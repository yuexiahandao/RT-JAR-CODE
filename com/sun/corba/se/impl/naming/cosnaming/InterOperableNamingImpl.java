/*     */ package com.sun.corba.se.impl.naming.cosnaming;
/*     */ 
/*     */ import java.io.StringWriter;
/*     */ import org.omg.CosNaming.NameComponent;
/*     */ import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
/*     */ import org.omg.CosNaming.NamingContextPackage.InvalidName;
/*     */ 
/*     */ public class InterOperableNamingImpl
/*     */ {
/*     */   public String convertToString(NameComponent[] paramArrayOfNameComponent)
/*     */   {
/*  59 */     String str1 = convertNameComponentToString(paramArrayOfNameComponent[0]);
/*     */ 
/*  62 */     for (int i = 1; i < paramArrayOfNameComponent.length; i++) {
/*  63 */       String str2 = convertNameComponentToString(paramArrayOfNameComponent[i]);
/*  64 */       if (str2 != null) {
/*  65 */         str1 = str1 + "/" + convertNameComponentToString(paramArrayOfNameComponent[i]);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  70 */     return str1;
/*     */   }
/*     */ 
/*     */   private String convertNameComponentToString(NameComponent paramNameComponent)
/*     */   {
/*  79 */     if (((paramNameComponent.id == null) || (paramNameComponent.id.length() == 0)) && ((paramNameComponent.kind == null) || (paramNameComponent.kind.length() == 0)))
/*     */     {
/*  84 */       return ".";
/*     */     }
/*  86 */     if ((paramNameComponent.id == null) || (paramNameComponent.id.length() == 0))
/*     */     {
/*  89 */       str1 = addEscape(paramNameComponent.kind);
/*  90 */       return "." + str1;
/*     */     }
/*  92 */     if ((paramNameComponent.kind == null) || (paramNameComponent.kind.length() == 0))
/*     */     {
/*  95 */       str1 = addEscape(paramNameComponent.id);
/*  96 */       return str1;
/*     */     }
/*     */ 
/*  99 */     String str1 = addEscape(paramNameComponent.id);
/* 100 */     String str2 = addEscape(paramNameComponent.kind);
/* 101 */     return str1 + "." + str2;
/*     */   }
/*     */ 
/*     */   private String addEscape(String paramString)
/*     */   {
/*     */     StringBuffer localStringBuffer;
/* 111 */     if ((paramString != null) && ((paramString.indexOf('.') != -1) || (paramString.indexOf('/') != -1)))
/*     */     {
/* 115 */       localStringBuffer = new StringBuffer();
/* 116 */       for (int i = 0; i < paramString.length(); i++) {
/* 117 */         char c = paramString.charAt(i);
/* 118 */         if ((c != '.') && (c != '/'))
/*     */         {
/* 120 */           localStringBuffer.append(c);
/*     */         }
/*     */         else
/*     */         {
/* 124 */           localStringBuffer.append('\\');
/* 125 */           localStringBuffer.append(c);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 130 */       return paramString;
/*     */     }
/* 132 */     return new String(localStringBuffer);
/*     */   }
/*     */ 
/*     */   public NameComponent[] convertToNameComponent(String paramString)
/*     */     throws InvalidName
/*     */   {
/* 145 */     String[] arrayOfString = breakStringToNameComponents(paramString);
/*     */ 
/* 147 */     if ((arrayOfString == null) || (arrayOfString.length == 0))
/*     */     {
/* 150 */       return null;
/*     */     }
/* 152 */     NameComponent[] arrayOfNameComponent = new NameComponent[arrayOfString.length];
/*     */ 
/* 154 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 155 */       arrayOfNameComponent[i] = createNameComponentFromString(arrayOfString[i]);
/*     */     }
/*     */ 
/* 158 */     return arrayOfNameComponent;
/*     */   }
/*     */ 
/*     */   private String[] breakStringToNameComponents(String paramString)
/*     */   {
/* 165 */     int[] arrayOfInt = new int[100];
/* 166 */     int i = 0;
/*     */ 
/* 168 */     for (int j = 0; j <= paramString.length(); ) {
/* 169 */       arrayOfInt[i] = paramString.indexOf(47, j);
/*     */ 
/* 171 */       if (arrayOfInt[i] == -1)
/*     */       {
/* 174 */         j = paramString.length() + 1;
/*     */       }
/* 181 */       else if ((arrayOfInt[i] > 0) && (paramString.charAt(arrayOfInt[i] - 1) == '\\'))
/*     */       {
/* 185 */         j = arrayOfInt[i] + 1;
/* 186 */         arrayOfInt[i] = -1;
/*     */       }
/*     */       else {
/* 189 */         j = arrayOfInt[i] + 1;
/* 190 */         i++;
/*     */       }
/*     */     }
/*     */ 
/* 194 */     if (i == 0) {
/* 195 */       String[] arrayOfString = new String[1];
/* 196 */       arrayOfString[0] = paramString;
/* 197 */       return arrayOfString;
/*     */     }
/* 199 */     if (i != 0) {
/* 200 */       i++;
/*     */     }
/* 202 */     return StringComponentsFromIndices(arrayOfInt, i, paramString);
/*     */   }
/*     */ 
/*     */   private String[] StringComponentsFromIndices(int[] paramArrayOfInt, int paramInt, String paramString)
/*     */   {
/* 212 */     String[] arrayOfString = new String[paramInt];
/* 213 */     int i = 0;
/* 214 */     int j = paramArrayOfInt[0];
/* 215 */     for (int k = 0; k < paramInt; k++) {
/* 216 */       arrayOfString[k] = paramString.substring(i, j);
/*     */ 
/* 218 */       if ((paramArrayOfInt[k] < paramString.length() - 1) && (paramArrayOfInt[k] != -1))
/*     */       {
/* 221 */         i = paramArrayOfInt[k] + 1;
/*     */       }
/*     */       else {
/* 224 */         i = 0;
/* 225 */         k = paramInt;
/*     */       }
/* 227 */       if ((k + 1 < paramArrayOfInt.length) && (paramArrayOfInt[(k + 1)] < paramString.length() - 1) && (paramArrayOfInt[(k + 1)] != -1))
/*     */       {
/* 231 */         j = paramArrayOfInt[(k + 1)];
/*     */       }
/*     */       else {
/* 234 */         k = paramInt;
/*     */       }
/*     */ 
/* 237 */       if ((i != 0) && (k == paramInt)) {
/* 238 */         arrayOfString[(paramInt - 1)] = paramString.substring(i);
/*     */       }
/*     */     }
/*     */ 
/* 242 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private NameComponent createNameComponentFromString(String paramString)
/*     */     throws InvalidName
/*     */   {
/* 254 */     String str1 = null;
/* 255 */     String str2 = null;
/* 256 */     if ((paramString == null) || (paramString.length() == 0) || (paramString.endsWith(".")))
/*     */     {
/* 262 */       throw new InvalidName();
/*     */     }
/*     */ 
/* 265 */     int i = paramString.indexOf('.', 0);
/*     */ 
/* 267 */     if (i == -1) {
/* 268 */       str1 = paramString;
/*     */     }
/* 271 */     else if (i == 0)
/*     */     {
/* 274 */       if (paramString.length() != 1) {
/* 275 */         str2 = paramString.substring(1);
/*     */       }
/*     */ 
/*     */     }
/* 280 */     else if (paramString.charAt(i - 1) != '\\') {
/* 281 */       str1 = paramString.substring(0, i);
/* 282 */       str2 = paramString.substring(i + 1);
/*     */     }
/*     */     else {
/* 285 */       int j = 0;
/*     */ 
/* 287 */       while ((i < paramString.length()) && (j != 1))
/*     */       {
/* 289 */         i = paramString.indexOf('.', i + 1);
/* 290 */         if (i > 0) {
/* 291 */           if (paramString.charAt(i - 1) != '\\')
/*     */           {
/* 294 */             j = 1;
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 300 */           i = paramString.length();
/*     */         }
/*     */       }
/* 303 */       if (j == 1) {
/* 304 */         str1 = paramString.substring(0, i);
/* 305 */         str2 = paramString.substring(i + 1);
/*     */       }
/*     */       else {
/* 308 */         str1 = paramString;
/*     */       }
/*     */     }
/*     */ 
/* 312 */     str1 = cleanEscapeCharacter(str1);
/* 313 */     str2 = cleanEscapeCharacter(str2);
/* 314 */     if (str1 == null) {
/* 315 */       str1 = "";
/*     */     }
/* 317 */     if (str2 == null) {
/* 318 */       str2 = "";
/*     */     }
/* 320 */     return new NameComponent(str1, str2);
/*     */   }
/*     */ 
/*     */   private String cleanEscapeCharacter(String paramString)
/*     */   {
/* 329 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 330 */       return paramString;
/*     */     }
/* 332 */     int i = paramString.indexOf('\\');
/* 333 */     if (i == 0) {
/* 334 */       return paramString;
/*     */     }
/*     */ 
/* 337 */     StringBuffer localStringBuffer1 = new StringBuffer(paramString);
/* 338 */     StringBuffer localStringBuffer2 = new StringBuffer();
/*     */ 
/* 340 */     for (int j = 0; j < paramString.length(); j++) {
/* 341 */       char c1 = localStringBuffer1.charAt(j);
/* 342 */       if (c1 != '\\') {
/* 343 */         localStringBuffer2.append(c1);
/*     */       }
/* 345 */       else if (j + 1 < paramString.length()) {
/* 346 */         char c2 = localStringBuffer1.charAt(j + 1);
/*     */ 
/* 350 */         if (Character.isLetterOrDigit(c2)) {
/* 351 */           localStringBuffer2.append(c1);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 356 */     return new String(localStringBuffer2);
/*     */   }
/*     */ 
/*     */   public String createURLBasedAddress(String paramString1, String paramString2)
/*     */     throws InvalidAddress
/*     */   {
/* 371 */     String str = null;
/* 372 */     if ((paramString1 == null) || (paramString1.length() == 0))
/*     */     {
/* 374 */       throw new InvalidAddress();
/*     */     }
/*     */ 
/* 377 */     str = "corbaname:" + paramString1 + "#" + encode(paramString2);
/*     */ 
/* 379 */     return str;
/*     */   }
/*     */ 
/*     */   private String encode(String paramString)
/*     */   {
/* 385 */     StringWriter localStringWriter = new StringWriter();
/* 386 */     int i = 0;
/* 387 */     for (int j = 0; j < paramString.length(); j++)
/*     */     {
/* 389 */       char c = paramString.charAt(j);
/* 390 */       if (Character.isLetterOrDigit(c)) {
/* 391 */         localStringWriter.write(c);
/*     */       }
/* 395 */       else if ((c == ';') || (c == '/') || (c == '?') || (c == ':') || (c == '@') || (c == '&') || (c == '=') || (c == '+') || (c == '$') || (c == ';') || (c == '-') || (c == '_') || (c == '.') || (c == '!') || (c == '~') || (c == '*') || (c == ' ') || (c == '(') || (c == ')'))
/*     */       {
/* 401 */         localStringWriter.write(c);
/*     */       }
/*     */       else
/*     */       {
/* 405 */         localStringWriter.write(37);
/* 406 */         String str = Integer.toHexString(c);
/* 407 */         localStringWriter.write(str);
/*     */       }
/*     */     }
/* 410 */     return localStringWriter.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.cosnaming.InterOperableNamingImpl
 * JD-Core Version:    0.6.2
 */