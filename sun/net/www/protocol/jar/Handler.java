/*     */ package sun.net.www.protocol.jar;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLStreamHandler;
/*     */ import sun.net.www.ParseUtil;
/*     */ 
/*     */ public class Handler extends URLStreamHandler
/*     */ {
/*     */   private static final String separator = "!/";
/*     */ 
/*     */   protected URLConnection openConnection(URL paramURL)
/*     */     throws IOException
/*     */   {
/*  41 */     return new JarURLConnection(paramURL, this);
/*     */   }
/*     */ 
/*     */   private static int indexOfBangSlash(String paramString) {
/*  45 */     int i = paramString.length();
/*  46 */     while ((i = paramString.lastIndexOf('!', i)) != -1) {
/*  47 */       if ((i != paramString.length() - 1) && (paramString.charAt(i + 1) == '/'))
/*     */       {
/*  49 */         return i + 1;
/*     */       }
/*  51 */       i--;
/*     */     }
/*     */ 
/*  54 */     return -1;
/*     */   }
/*     */ 
/*     */   protected boolean sameFile(URL paramURL1, URL paramURL2)
/*     */   {
/*  62 */     if ((!paramURL1.getProtocol().equals("jar")) || (!paramURL2.getProtocol().equals("jar"))) {
/*  63 */       return false;
/*     */     }
/*  65 */     String str1 = paramURL1.getFile();
/*  66 */     String str2 = paramURL2.getFile();
/*  67 */     int i = str1.indexOf("!/");
/*  68 */     int j = str2.indexOf("!/");
/*     */ 
/*  70 */     if ((i == -1) || (j == -1)) {
/*  71 */       return super.sameFile(paramURL1, paramURL2);
/*     */     }
/*     */ 
/*  74 */     String str3 = str1.substring(i + 2);
/*  75 */     String str4 = str2.substring(j + 2);
/*     */ 
/*  77 */     if (!str3.equals(str4)) {
/*  78 */       return false;
/*     */     }
/*  80 */     URL localURL1 = null; URL localURL2 = null;
/*     */     try {
/*  82 */       localURL1 = new URL(str1.substring(0, i));
/*  83 */       localURL2 = new URL(str2.substring(0, j));
/*     */     } catch (MalformedURLException localMalformedURLException) {
/*  85 */       return super.sameFile(paramURL1, paramURL2);
/*     */     }
/*     */ 
/*  88 */     if (!super.sameFile(localURL1, localURL2)) {
/*  89 */       return false;
/*     */     }
/*     */ 
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   protected int hashCode(URL paramURL)
/*     */   {
/*  97 */     int i = 0;
/*     */ 
/*  99 */     String str1 = paramURL.getProtocol();
/* 100 */     if (str1 != null) {
/* 101 */       i += str1.hashCode();
/*     */     }
/* 103 */     String str2 = paramURL.getFile();
/* 104 */     int j = str2.indexOf("!/");
/*     */ 
/* 106 */     if (j == -1) {
/* 107 */       return i + str2.hashCode();
/*     */     }
/* 109 */     URL localURL = null;
/* 110 */     String str3 = str2.substring(0, j);
/*     */     try {
/* 112 */       localURL = new URL(str3);
/* 113 */       i += localURL.hashCode();
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 115 */       i += str3.hashCode();
/*     */     }
/*     */ 
/* 118 */     String str4 = str2.substring(j + 2);
/* 119 */     i += str4.hashCode();
/*     */ 
/* 121 */     return i;
/*     */   }
/*     */ 
/*     */   protected void parseURL(URL paramURL, String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 128 */     String str1 = null;
/* 129 */     String str2 = null;
/*     */ 
/* 131 */     int i = paramString.indexOf('#', paramInt2);
/* 132 */     int j = i == paramInt1 ? 1 : 0;
/* 133 */     if (i > -1) {
/* 134 */       str2 = paramString.substring(i + 1, paramString.length());
/* 135 */       if (j != 0) {
/* 136 */         str1 = paramURL.getFile();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 143 */     boolean bool = false;
/* 144 */     if (paramString.length() >= 4) {
/* 145 */       bool = paramString.substring(0, 4).equalsIgnoreCase("jar:");
/*     */     }
/* 147 */     paramString = paramString.substring(paramInt1, paramInt2);
/*     */ 
/* 149 */     if (bool) {
/* 150 */       str1 = parseAbsoluteSpec(paramString);
/* 151 */     } else if (j == 0) {
/* 152 */       str1 = parseContextSpec(paramURL, paramString);
/*     */ 
/* 155 */       int k = indexOfBangSlash(str1);
/* 156 */       String str3 = str1.substring(0, k);
/* 157 */       String str4 = str1.substring(k);
/* 158 */       ParseUtil localParseUtil = new ParseUtil();
/* 159 */       str4 = localParseUtil.canonizeString(str4);
/* 160 */       str1 = str3 + str4;
/*     */     }
/* 162 */     setURL(paramURL, "jar", "", -1, str1, str2);
/*     */   }
/*     */ 
/*     */   private String parseAbsoluteSpec(String paramString) {
/* 166 */     URL localURL = null;
/* 167 */     int i = -1;
/*     */ 
/* 169 */     if ((i = indexOfBangSlash(paramString)) == -1) {
/* 170 */       throw new NullPointerException("no !/ in spec");
/*     */     }
/*     */     try
/*     */     {
/* 174 */       String str = paramString.substring(0, i - 1);
/* 175 */       localURL = new URL(str);
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 177 */       throw new NullPointerException("invalid url: " + paramString + " (" + localMalformedURLException + ")");
/*     */     }
/*     */ 
/* 180 */     return paramString;
/*     */   }
/*     */ 
/*     */   private String parseContextSpec(URL paramURL, String paramString) {
/* 184 */     String str = paramURL.getFile();
/*     */     int i;
/* 186 */     if (paramString.startsWith("/")) {
/* 187 */       i = indexOfBangSlash(str);
/* 188 */       if (i == -1) {
/* 189 */         throw new NullPointerException("malformed context url:" + paramURL + ": no !/");
/*     */       }
/*     */ 
/* 194 */       str = str.substring(0, i);
/*     */     }
/* 196 */     if ((!str.endsWith("/")) && (!paramString.startsWith("/")))
/*     */     {
/* 198 */       i = str.lastIndexOf('/');
/* 199 */       if (i == -1) {
/* 200 */         throw new NullPointerException("malformed context url:" + paramURL);
/*     */       }
/*     */ 
/* 204 */       str = str.substring(0, i + 1);
/*     */     }
/* 206 */     return str + paramString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.jar.Handler
 * JD-Core Version:    0.6.2
 */