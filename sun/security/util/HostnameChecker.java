/*     */ package sun.security.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.Principal;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateParsingException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.net.util.IPAddressUtil;
/*     */ import sun.security.ssl.Krb5Helper;
/*     */ import sun.security.x509.X500Name;
/*     */ 
/*     */ public class HostnameChecker
/*     */ {
/*     */   public static final byte TYPE_TLS = 1;
/*  50 */   private static final HostnameChecker INSTANCE_TLS = new HostnameChecker((byte)1);
/*     */   public static final byte TYPE_LDAP = 2;
/*  55 */   private static final HostnameChecker INSTANCE_LDAP = new HostnameChecker((byte)2);
/*     */   private static final int ALTNAME_DNS = 2;
/*     */   private static final int ALTNAME_IP = 7;
/*     */   private final byte checkType;
/*     */ 
/*     */   private HostnameChecker(byte paramByte)
/*     */   {
/*  66 */     this.checkType = paramByte;
/*     */   }
/*     */ 
/*     */   public static HostnameChecker getInstance(byte paramByte)
/*     */   {
/*  74 */     if (paramByte == 1)
/*  75 */       return INSTANCE_TLS;
/*  76 */     if (paramByte == 2) {
/*  77 */       return INSTANCE_LDAP;
/*     */     }
/*  79 */     throw new IllegalArgumentException("Unknown check type: " + paramByte);
/*     */   }
/*     */ 
/*     */   public void match(String paramString, X509Certificate paramX509Certificate)
/*     */     throws CertificateException
/*     */   {
/*  90 */     if (isIpAddress(paramString))
/*  91 */       matchIP(paramString, paramX509Certificate);
/*     */     else
/*  93 */       matchDNS(paramString, paramX509Certificate);
/*     */   }
/*     */ 
/*     */   public static boolean match(String paramString, Principal paramPrincipal)
/*     */   {
/* 101 */     String str = getServerName(paramPrincipal);
/* 102 */     return paramString.equalsIgnoreCase(str);
/*     */   }
/*     */ 
/*     */   public static String getServerName(Principal paramPrincipal)
/*     */   {
/* 109 */     return Krb5Helper.getPrincipalHostName(paramPrincipal);
/*     */   }
/*     */ 
/*     */   private static boolean isIpAddress(String paramString)
/*     */   {
/* 122 */     if ((IPAddressUtil.isIPv4LiteralAddress(paramString)) || (IPAddressUtil.isIPv6LiteralAddress(paramString)))
/*     */     {
/* 124 */       return true;
/*     */     }
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   private static void matchIP(String paramString, X509Certificate paramX509Certificate)
/*     */     throws CertificateException
/*     */   {
/* 140 */     Collection localCollection = paramX509Certificate.getSubjectAlternativeNames();
/* 141 */     if (localCollection == null) {
/* 142 */       throw new CertificateException("No subject alternative names present");
/*     */     }
/*     */ 
/* 145 */     for (List localList : localCollection)
/*     */     {
/* 147 */       if (((Integer)localList.get(0)).intValue() == 7) {
/* 148 */         String str = (String)localList.get(1);
/* 149 */         if (paramString.equalsIgnoreCase(str)) {
/* 150 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 154 */     throw new CertificateException("No subject alternative names matching IP address " + paramString + " found");
/*     */   }
/*     */ 
/*     */   private void matchDNS(String paramString, X509Certificate paramX509Certificate)
/*     */     throws CertificateException
/*     */   {
/* 176 */     Collection localCollection = paramX509Certificate.getSubjectAlternativeNames();
/* 177 */     if (localCollection != null) {
/* 178 */       int i = 0;
/* 179 */       for (localObject = localCollection.iterator(); ((Iterator)localObject).hasNext(); ) { List localList = (List)((Iterator)localObject).next();
/* 180 */         if (((Integer)localList.get(0)).intValue() == 2) {
/* 181 */           i = 1;
/* 182 */           String str2 = (String)localList.get(1);
/* 183 */           if (isMatched(paramString, str2)) {
/* 184 */             return;
/*     */           }
/*     */         }
/*     */       }
/* 188 */       if (i != 0)
/*     */       {
/* 191 */         throw new CertificateException("No subject alternative DNS name matching " + paramString + " found.");
/*     */       }
/*     */     }
/*     */ 
/* 195 */     X500Name localX500Name = getSubjectX500Name(paramX509Certificate);
/* 196 */     Object localObject = localX500Name.findMostSpecificAttribute(X500Name.commonName_oid);
/*     */ 
/* 198 */     if (localObject != null) {
/*     */       try {
/* 200 */         if (isMatched(paramString, ((DerValue)localObject).getAsString()))
/* 201 */           return;
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */     }
/* 207 */     String str1 = "No name matching " + paramString + " found";
/* 208 */     throw new CertificateException(str1);
/*     */   }
/*     */ 
/*     */   public static X500Name getSubjectX500Name(X509Certificate paramX509Certificate)
/*     */     throws CertificateParsingException
/*     */   {
/*     */     try
/*     */     {
/* 222 */       Principal localPrincipal = paramX509Certificate.getSubjectDN();
/* 223 */       if ((localPrincipal instanceof X500Name)) {
/* 224 */         return (X500Name)localPrincipal;
/*     */       }
/* 226 */       X500Principal localX500Principal = paramX509Certificate.getSubjectX500Principal();
/* 227 */       return new X500Name(localX500Principal.getEncoded());
/*     */     }
/*     */     catch (IOException localIOException) {
/* 230 */       throw ((CertificateParsingException)new CertificateParsingException().initCause(localIOException));
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isMatched(String paramString1, String paramString2)
/*     */   {
/* 247 */     if (this.checkType == 1)
/* 248 */       return matchAllWildcards(paramString1, paramString2);
/* 249 */     if (this.checkType == 2) {
/* 250 */       return matchLeftmostWildcard(paramString1, paramString2);
/*     */     }
/* 252 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean matchAllWildcards(String paramString1, String paramString2)
/*     */   {
/* 269 */     paramString1 = paramString1.toLowerCase();
/* 270 */     paramString2 = paramString2.toLowerCase();
/* 271 */     StringTokenizer localStringTokenizer1 = new StringTokenizer(paramString1, ".");
/* 272 */     StringTokenizer localStringTokenizer2 = new StringTokenizer(paramString2, ".");
/*     */ 
/* 274 */     if (localStringTokenizer1.countTokens() != localStringTokenizer2.countTokens()) {
/* 275 */       return false;
/*     */     }
/*     */ 
/* 278 */     while (localStringTokenizer1.hasMoreTokens()) {
/* 279 */       if (!matchWildCards(localStringTokenizer1.nextToken(), localStringTokenizer2.nextToken()))
/*     */       {
/* 281 */         return false;
/*     */       }
/*     */     }
/* 284 */     return true;
/*     */   }
/*     */ 
/*     */   private static boolean matchLeftmostWildcard(String paramString1, String paramString2)
/*     */   {
/* 299 */     paramString1 = paramString1.toLowerCase();
/* 300 */     paramString2 = paramString2.toLowerCase();
/*     */ 
/* 303 */     int i = paramString2.indexOf(".");
/* 304 */     int j = paramString1.indexOf(".");
/*     */ 
/* 306 */     if (i == -1)
/* 307 */       i = paramString2.length();
/* 308 */     if (j == -1) {
/* 309 */       j = paramString1.length();
/*     */     }
/* 311 */     if (matchWildCards(paramString1.substring(0, j), paramString2.substring(0, i)))
/*     */     {
/* 315 */       return paramString2.substring(i).equals(paramString1.substring(j));
/*     */     }
/*     */ 
/* 318 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean matchWildCards(String paramString1, String paramString2)
/*     */   {
/* 329 */     int i = paramString2.indexOf("*");
/* 330 */     if (i == -1) {
/* 331 */       return paramString1.equals(paramString2);
/*     */     }
/* 333 */     int j = 1;
/* 334 */     String str1 = "";
/* 335 */     String str2 = paramString2;
/*     */ 
/* 337 */     while (i != -1)
/*     */     {
/* 340 */       str1 = str2.substring(0, i);
/* 341 */       str2 = str2.substring(i + 1);
/*     */ 
/* 343 */       int k = paramString1.indexOf(str1);
/* 344 */       if ((k == -1) || ((j != 0) && (k != 0)))
/*     */       {
/* 346 */         return false;
/*     */       }
/* 348 */       j = 0;
/*     */ 
/* 351 */       paramString1 = paramString1.substring(k + str1.length());
/* 352 */       i = str2.indexOf("*");
/*     */     }
/* 354 */     return paramString1.endsWith(str2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.HostnameChecker
 * JD-Core Version:    0.6.2
 */