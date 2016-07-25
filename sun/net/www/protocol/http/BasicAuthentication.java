/*     */ package sun.net.www.protocol.http;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import sun.misc.BASE64Encoder;
/*     */ import sun.net.www.HeaderParser;
/*     */ 
/*     */ class BasicAuthentication extends AuthenticationInfo
/*     */ {
/*     */   private static final long serialVersionUID = 100L;
/*     */   String auth;
/*     */ 
/*     */   public BasicAuthentication(boolean paramBoolean, String paramString1, int paramInt, String paramString2, PasswordAuthentication paramPasswordAuthentication)
/*     */   {
/*  58 */     super(paramBoolean ? 'p' : 's', AuthScheme.BASIC, paramString1, paramInt, paramString2);
/*     */ 
/*  60 */     String str = paramPasswordAuthentication.getUserName() + ":";
/*  61 */     byte[] arrayOfByte1 = null;
/*     */     try {
/*  63 */       arrayOfByte1 = str.getBytes("ISO-8859-1");
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*  65 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */ 
/*     */     }
/*     */ 
/*  69 */     char[] arrayOfChar = paramPasswordAuthentication.getPassword();
/*  70 */     byte[] arrayOfByte2 = new byte[arrayOfChar.length];
/*  71 */     for (int i = 0; i < arrayOfChar.length; i++) {
/*  72 */       arrayOfByte2[i] = ((byte)arrayOfChar[i]);
/*     */     }
/*     */ 
/*  75 */     byte[] arrayOfByte3 = new byte[arrayOfByte1.length + arrayOfByte2.length];
/*  76 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, arrayOfByte1.length);
/*  77 */     System.arraycopy(arrayOfByte2, 0, arrayOfByte3, arrayOfByte1.length, arrayOfByte2.length);
/*     */ 
/*  79 */     this.auth = ("Basic " + new BasicBASE64Encoder(null).encode(arrayOfByte3));
/*  80 */     this.pw = paramPasswordAuthentication;
/*     */   }
/*     */ 
/*     */   public BasicAuthentication(boolean paramBoolean, String paramString1, int paramInt, String paramString2, String paramString3)
/*     */   {
/*  88 */     super(paramBoolean ? 'p' : 's', AuthScheme.BASIC, paramString1, paramInt, paramString2);
/*     */ 
/*  90 */     this.auth = ("Basic " + paramString3);
/*     */   }
/*     */ 
/*     */   public BasicAuthentication(boolean paramBoolean, URL paramURL, String paramString, PasswordAuthentication paramPasswordAuthentication)
/*     */   {
/*  98 */     super(paramBoolean ? 'p' : 's', AuthScheme.BASIC, paramURL, paramString);
/*     */ 
/* 100 */     String str = paramPasswordAuthentication.getUserName() + ":";
/* 101 */     byte[] arrayOfByte1 = null;
/*     */     try {
/* 103 */       arrayOfByte1 = str.getBytes("ISO-8859-1");
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 105 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */ 
/*     */     }
/*     */ 
/* 109 */     char[] arrayOfChar = paramPasswordAuthentication.getPassword();
/* 110 */     byte[] arrayOfByte2 = new byte[arrayOfChar.length];
/* 111 */     for (int i = 0; i < arrayOfChar.length; i++) {
/* 112 */       arrayOfByte2[i] = ((byte)arrayOfChar[i]);
/*     */     }
/*     */ 
/* 115 */     byte[] arrayOfByte3 = new byte[arrayOfByte1.length + arrayOfByte2.length];
/* 116 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, arrayOfByte1.length);
/* 117 */     System.arraycopy(arrayOfByte2, 0, arrayOfByte3, arrayOfByte1.length, arrayOfByte2.length);
/*     */ 
/* 119 */     this.auth = ("Basic " + new BasicBASE64Encoder(null).encode(arrayOfByte3));
/* 120 */     this.pw = paramPasswordAuthentication;
/*     */   }
/*     */ 
/*     */   public BasicAuthentication(boolean paramBoolean, URL paramURL, String paramString1, String paramString2)
/*     */   {
/* 128 */     super(paramBoolean ? 'p' : 's', AuthScheme.BASIC, paramURL, paramString1);
/*     */ 
/* 130 */     this.auth = ("Basic " + paramString2);
/*     */   }
/*     */ 
/*     */   public boolean supportsPreemptiveAuthorization()
/*     */   {
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean setHeaders(HttpURLConnection paramHttpURLConnection, HeaderParser paramHeaderParser, String paramString)
/*     */   {
/* 151 */     paramHttpURLConnection.setAuthenticationProperty(getHeaderName(), getHeaderValue(null, null));
/* 152 */     return true;
/*     */   }
/*     */ 
/*     */   public String getHeaderValue(URL paramURL, String paramString)
/*     */   {
/* 163 */     return this.auth;
/*     */   }
/*     */ 
/*     */   public boolean isAuthorizationStale(String paramString)
/*     */   {
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */   static String getRootPath(String paramString1, String paramString2)
/*     */   {
/* 183 */     int i = 0;
/*     */     try
/*     */     {
/* 188 */       paramString1 = new URI(paramString1).normalize().getPath();
/* 189 */       paramString2 = new URI(paramString2).normalize().getPath();
/*     */     }
/*     */     catch (URISyntaxException localURISyntaxException)
/*     */     {
/*     */     }
/* 194 */     while (i < paramString2.length()) {
/* 195 */       int j = paramString2.indexOf('/', i + 1);
/* 196 */       if ((j != -1) && (paramString2.regionMatches(0, paramString1, 0, j + 1)))
/* 197 */         i = j;
/*     */       else {
/* 199 */         return paramString2.substring(0, i + 1);
/*     */       }
/*     */     }
/* 202 */     return paramString1;
/*     */   }
/*     */   private class BasicBASE64Encoder extends BASE64Encoder {
/*     */     private BasicBASE64Encoder() {
/*     */     }
/*     */ 
/*     */     protected int bytesPerLine() {
/* 209 */       return 10000;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.BasicAuthentication
 * JD-Core Version:    0.6.2
 */