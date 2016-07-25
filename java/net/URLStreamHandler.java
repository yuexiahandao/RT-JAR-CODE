/*     */ package java.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.net.util.IPAddressUtil;
/*     */ 
/*     */ public abstract class URLStreamHandler
/*     */ {
/*     */   protected abstract URLConnection openConnection(URL paramURL)
/*     */     throws IOException;
/*     */ 
/*     */   protected URLConnection openConnection(URL paramURL, Proxy paramProxy)
/*     */     throws IOException
/*     */   {
/*  97 */     throw new UnsupportedOperationException("Method not implemented.");
/*     */   }
/*     */ 
/*     */   protected void parseURL(URL paramURL, String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 127 */     String str1 = paramURL.getProtocol();
/* 128 */     String str2 = paramURL.getAuthority();
/* 129 */     String str3 = paramURL.getUserInfo();
/* 130 */     String str4 = paramURL.getHost();
/* 131 */     int i = paramURL.getPort();
/* 132 */     String str5 = paramURL.getPath();
/* 133 */     String str6 = paramURL.getQuery();
/*     */ 
/* 136 */     String str7 = paramURL.getRef();
/*     */ 
/* 138 */     int j = 0;
/* 139 */     int k = 0;
/*     */ 
/* 143 */     if (paramInt1 < paramInt2) {
/* 144 */       m = paramString.indexOf('?');
/* 145 */       k = m == paramInt1 ? 1 : 0;
/* 146 */       if ((m != -1) && (m < paramInt2)) {
/* 147 */         str6 = paramString.substring(m + 1, paramInt2);
/* 148 */         if (paramInt2 > m)
/* 149 */           paramInt2 = m;
/* 150 */         paramString = paramString.substring(0, m);
/*     */       }
/*     */     }
/*     */ 
/* 154 */     int m = 0;
/*     */ 
/* 156 */     int n = (paramInt1 <= paramInt2 - 4) && (paramString.charAt(paramInt1) == '/') && (paramString.charAt(paramInt1 + 1) == '/') && (paramString.charAt(paramInt1 + 2) == '/') && (paramString.charAt(paramInt1 + 3) == '/') ? 1 : 0;
/*     */     int i1;
/*     */     String str9;
/* 161 */     if ((n == 0) && (paramInt1 <= paramInt2 - 2) && (paramString.charAt(paramInt1) == '/') && (paramString.charAt(paramInt1 + 1) == '/'))
/*     */     {
/* 163 */       paramInt1 += 2;
/* 164 */       m = paramString.indexOf('/', paramInt1);
/* 165 */       if (m < 0) {
/* 166 */         m = paramString.indexOf('?', paramInt1);
/* 167 */         if (m < 0) {
/* 168 */           m = paramInt2;
/*     */         }
/*     */       }
/* 171 */       str4 = str2 = paramString.substring(paramInt1, m);
/*     */ 
/* 173 */       i1 = str2.indexOf('@');
/* 174 */       if (i1 != -1) {
/* 175 */         str3 = str2.substring(0, i1);
/* 176 */         str4 = str2.substring(i1 + 1);
/*     */       } else {
/* 178 */         str3 = null;
/*     */       }
/* 180 */       if (str4 != null)
/*     */       {
/* 183 */         if ((str4.length() > 0) && (str4.charAt(0) == '[')) {
/* 184 */           if ((i1 = str4.indexOf(']')) > 2)
/*     */           {
/* 186 */             str9 = str4;
/* 187 */             str4 = str9.substring(0, i1 + 1);
/* 188 */             if (!IPAddressUtil.isIPv6LiteralAddress(str4.substring(1, i1)))
/*     */             {
/* 190 */               throw new IllegalArgumentException("Invalid host: " + str4);
/*     */             }
/*     */ 
/* 194 */             i = -1;
/* 195 */             if (str9.length() > i1 + 1)
/* 196 */               if (str9.charAt(i1 + 1) == ':') {
/* 197 */                 i1++;
/*     */ 
/* 199 */                 if (str9.length() > i1 + 1)
/* 200 */                   i = Integer.parseInt(str9.substring(i1 + 1));
/*     */               }
/*     */               else {
/* 203 */                 throw new IllegalArgumentException("Invalid authority field: " + str2);
/*     */               }
/*     */           }
/*     */           else
/*     */           {
/* 208 */             throw new IllegalArgumentException("Invalid authority field: " + str2);
/*     */           }
/*     */         }
/*     */         else {
/* 212 */           i1 = str4.indexOf(':');
/* 213 */           i = -1;
/* 214 */           if (i1 >= 0)
/*     */           {
/* 216 */             if (str4.length() > i1 + 1) {
/* 217 */               i = Integer.parseInt(str4.substring(i1 + 1));
/*     */             }
/* 219 */             str4 = str4.substring(0, i1);
/*     */           }
/*     */         }
/*     */       }
/* 223 */       else str4 = "";
/*     */ 
/* 225 */       if (i < -1) {
/* 226 */         throw new IllegalArgumentException("Invalid port number :" + i);
/*     */       }
/* 228 */       paramInt1 = m;
/*     */ 
/* 231 */       if ((str2 != null) && (str2.length() > 0)) {
/* 232 */         str5 = "";
/*     */       }
/*     */     }
/* 235 */     if (str4 == null) {
/* 236 */       str4 = "";
/*     */     }
/*     */ 
/* 240 */     if (paramInt1 < paramInt2) {
/* 241 */       if (paramString.charAt(paramInt1) == '/') {
/* 242 */         str5 = paramString.substring(paramInt1, paramInt2);
/* 243 */       } else if ((str5 != null) && (str5.length() > 0)) {
/* 244 */         j = 1;
/* 245 */         i1 = str5.lastIndexOf('/');
/* 246 */         str9 = "";
/* 247 */         if ((i1 == -1) && (str2 != null))
/* 248 */           str9 = "/";
/* 249 */         str5 = str5.substring(0, i1 + 1) + str9 + paramString.substring(paramInt1, paramInt2);
/*     */       }
/*     */       else
/*     */       {
/* 253 */         String str8 = str2 != null ? "/" : "";
/* 254 */         str5 = str8 + paramString.substring(paramInt1, paramInt2);
/*     */       }
/* 256 */     } else if ((k != 0) && (str5 != null)) {
/* 257 */       int i2 = str5.lastIndexOf('/');
/* 258 */       if (i2 < 0)
/* 259 */         i2 = 0;
/* 260 */       str5 = str5.substring(0, i2) + "/";
/*     */     }
/* 262 */     if (str5 == null) {
/* 263 */       str5 = "";
/*     */     }
/* 265 */     if (j != 0)
/*     */     {
/* 267 */       while ((m = str5.indexOf("/./")) >= 0) {
/* 268 */         str5 = str5.substring(0, m) + str5.substring(m + 2);
/*     */       }
/*     */ 
/* 271 */       m = 0;
/* 272 */       while ((m = str5.indexOf("/../", m)) >= 0)
/*     */       {
/* 279 */         if ((m > 0) && ((paramInt2 = str5.lastIndexOf('/', m - 1)) >= 0) && (str5.indexOf("/../", paramInt2) != 0))
/*     */         {
/* 281 */           str5 = str5.substring(0, paramInt2) + str5.substring(m + 3);
/* 282 */           m = 0;
/*     */         } else {
/* 284 */           m += 3;
/*     */         }
/*     */       }
/*     */ 
/* 288 */       while (str5.endsWith("/..")) {
/* 289 */         m = str5.indexOf("/..");
/* 290 */         if ((paramInt2 = str5.lastIndexOf('/', m - 1)) < 0) break;
/* 291 */         str5 = str5.substring(0, paramInt2 + 1);
/*     */       }
/*     */ 
/* 297 */       if ((str5.startsWith("./")) && (str5.length() > 2)) {
/* 298 */         str5 = str5.substring(2);
/*     */       }
/*     */ 
/* 301 */       if (str5.endsWith("/.")) {
/* 302 */         str5 = str5.substring(0, str5.length() - 1);
/*     */       }
/*     */     }
/* 305 */     setURL(paramURL, str1, str4, i, str2, str3, str5, str6, str7);
/*     */   }
/*     */ 
/*     */   protected int getDefaultPort()
/*     */   {
/* 315 */     return -1;
/*     */   }
/*     */ 
/*     */   protected boolean equals(URL paramURL1, URL paramURL2)
/*     */   {
/* 331 */     String str1 = paramURL1.getRef();
/* 332 */     String str2 = paramURL2.getRef();
/* 333 */     return ((str1 == str2) || ((str1 != null) && (str1.equals(str2)))) && (sameFile(paramURL1, paramURL2));
/*     */   }
/*     */ 
/*     */   protected int hashCode(URL paramURL)
/*     */   {
/* 346 */     int i = 0;
/*     */ 
/* 349 */     String str1 = paramURL.getProtocol();
/* 350 */     if (str1 != null) {
/* 351 */       i += str1.hashCode();
/*     */     }
/*     */ 
/* 354 */     InetAddress localInetAddress = getHostAddress(paramURL);
/* 355 */     if (localInetAddress != null) {
/* 356 */       i += localInetAddress.hashCode();
/*     */     } else {
/* 358 */       str2 = paramURL.getHost();
/* 359 */       if (str2 != null) {
/* 360 */         i += str2.toLowerCase().hashCode();
/*     */       }
/*     */     }
/*     */ 
/* 364 */     String str2 = paramURL.getFile();
/* 365 */     if (str2 != null) {
/* 366 */       i += str2.hashCode();
/*     */     }
/*     */ 
/* 369 */     if (paramURL.getPort() == -1)
/* 370 */       i += getDefaultPort();
/*     */     else {
/* 372 */       i += paramURL.getPort();
/*     */     }
/*     */ 
/* 375 */     String str3 = paramURL.getRef();
/* 376 */     if (str3 != null) {
/* 377 */       i += str3.hashCode();
/*     */     }
/* 379 */     return i;
/*     */   }
/*     */ 
/*     */   protected boolean sameFile(URL paramURL1, URL paramURL2)
/*     */   {
/* 395 */     if ((paramURL1.getProtocol() != paramURL2.getProtocol()) && ((paramURL1.getProtocol() == null) || (!paramURL1.getProtocol().equalsIgnoreCase(paramURL2.getProtocol()))))
/*     */     {
/* 398 */       return false;
/*     */     }
/*     */ 
/* 401 */     if ((paramURL1.getFile() != paramURL2.getFile()) && ((paramURL1.getFile() == null) || (!paramURL1.getFile().equals(paramURL2.getFile()))))
/*     */     {
/* 403 */       return false;
/*     */     }
/*     */ 
/* 407 */     int i = paramURL1.getPort() != -1 ? paramURL1.getPort() : paramURL1.handler.getDefaultPort();
/* 408 */     int j = paramURL2.getPort() != -1 ? paramURL2.getPort() : paramURL2.handler.getDefaultPort();
/* 409 */     if (i != j) {
/* 410 */       return false;
/*     */     }
/*     */ 
/* 413 */     if (!hostsEqual(paramURL1, paramURL2)) {
/* 414 */       return false;
/*     */     }
/* 416 */     return true;
/*     */   }
/*     */ 
/*     */   protected synchronized InetAddress getHostAddress(URL paramURL)
/*     */   {
/* 429 */     if (paramURL.hostAddress != null) {
/* 430 */       return paramURL.hostAddress;
/*     */     }
/* 432 */     String str = paramURL.getHost();
/* 433 */     if ((str == null) || (str.equals("")))
/* 434 */       return null;
/*     */     try
/*     */     {
/* 437 */       paramURL.hostAddress = InetAddress.getByName(str);
/*     */     } catch (UnknownHostException localUnknownHostException) {
/* 439 */       return null;
/*     */     } catch (SecurityException localSecurityException) {
/* 441 */       return null;
/*     */     }
/*     */ 
/* 444 */     return paramURL.hostAddress;
/*     */   }
/*     */ 
/*     */   protected boolean hostsEqual(URL paramURL1, URL paramURL2)
/*     */   {
/* 456 */     InetAddress localInetAddress1 = getHostAddress(paramURL1);
/* 457 */     InetAddress localInetAddress2 = getHostAddress(paramURL2);
/*     */ 
/* 459 */     if ((localInetAddress1 != null) && (localInetAddress2 != null)) {
/* 460 */       return localInetAddress1.equals(localInetAddress2);
/*     */     }
/* 462 */     if ((paramURL1.getHost() != null) && (paramURL2.getHost() != null)) {
/* 463 */       return paramURL1.getHost().equalsIgnoreCase(paramURL2.getHost());
/*     */     }
/* 465 */     return (paramURL1.getHost() == null) && (paramURL2.getHost() == null);
/*     */   }
/*     */ 
/*     */   protected String toExternalForm(URL paramURL)
/*     */   {
/* 478 */     int i = paramURL.getProtocol().length() + 1;
/* 479 */     if ((paramURL.getAuthority() != null) && (paramURL.getAuthority().length() > 0))
/* 480 */       i += 2 + paramURL.getAuthority().length();
/* 481 */     if (paramURL.getPath() != null) {
/* 482 */       i += paramURL.getPath().length();
/*     */     }
/* 484 */     if (paramURL.getQuery() != null) {
/* 485 */       i += 1 + paramURL.getQuery().length();
/*     */     }
/* 487 */     if (paramURL.getRef() != null) {
/* 488 */       i += 1 + paramURL.getRef().length();
/*     */     }
/* 490 */     StringBuffer localStringBuffer = new StringBuffer(i);
/* 491 */     localStringBuffer.append(paramURL.getProtocol());
/* 492 */     localStringBuffer.append(":");
/* 493 */     if ((paramURL.getAuthority() != null) && (paramURL.getAuthority().length() > 0)) {
/* 494 */       localStringBuffer.append("//");
/* 495 */       localStringBuffer.append(paramURL.getAuthority());
/*     */     }
/* 497 */     if (paramURL.getPath() != null) {
/* 498 */       localStringBuffer.append(paramURL.getPath());
/*     */     }
/* 500 */     if (paramURL.getQuery() != null) {
/* 501 */       localStringBuffer.append('?');
/* 502 */       localStringBuffer.append(paramURL.getQuery());
/*     */     }
/* 504 */     if (paramURL.getRef() != null) {
/* 505 */       localStringBuffer.append("#");
/* 506 */       localStringBuffer.append(paramURL.getRef());
/*     */     }
/* 508 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   protected void setURL(URL paramURL, String paramString1, String paramString2, int paramInt, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
/*     */   {
/* 533 */     if (this != paramURL.handler) {
/* 534 */       throw new SecurityException("handler for url different from this handler");
/*     */     }
/*     */ 
/* 538 */     paramURL.set(paramURL.getProtocol(), paramString2, paramInt, paramString3, paramString4, paramString5, paramString6, paramString7);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected void setURL(URL paramURL, String paramString1, String paramString2, int paramInt, String paramString3, String paramString4)
/*     */   {
/* 564 */     String str1 = null;
/* 565 */     String str2 = null;
/* 566 */     if ((paramString2 != null) && (paramString2.length() != 0)) {
/* 567 */       str1 = paramString2 + ":" + paramInt;
/* 568 */       int i = paramString2.lastIndexOf('@');
/* 569 */       if (i != -1) {
/* 570 */         str2 = paramString2.substring(0, i);
/* 571 */         paramString2 = paramString2.substring(i + 1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 578 */     String str3 = null;
/* 579 */     String str4 = null;
/* 580 */     if (paramString3 != null) {
/* 581 */       int j = paramString3.lastIndexOf('?');
/* 582 */       if (j != -1) {
/* 583 */         str4 = paramString3.substring(j + 1);
/* 584 */         str3 = paramString3.substring(0, j);
/*     */       } else {
/* 586 */         str3 = paramString3;
/*     */       }
/*     */     }
/* 588 */     setURL(paramURL, paramString1, paramString2, paramInt, str1, str2, str3, str4, paramString4);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.URLStreamHandler
 * JD-Core Version:    0.6.2
 */