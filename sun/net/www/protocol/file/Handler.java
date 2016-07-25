/*     */ package sun.net.www.protocol.file;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLStreamHandler;
/*     */ import sun.net.www.ParseUtil;
/*     */ 
/*     */ public class Handler extends URLStreamHandler
/*     */ {
/*     */   private String getHost(URL paramURL)
/*     */   {
/*  46 */     String str = paramURL.getHost();
/*  47 */     if (str == null)
/*  48 */       str = "";
/*  49 */     return str;
/*     */   }
/*     */ 
/*     */   protected void parseURL(URL paramURL, String paramString, int paramInt1, int paramInt2)
/*     */   {
/*  67 */     super.parseURL(paramURL, paramString.replace(File.separatorChar, '/'), paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public synchronized URLConnection openConnection(URL paramURL) throws IOException
/*     */   {
/*  72 */     return openConnection(paramURL, null);
/*     */   }
/*     */ 
/*     */   public synchronized URLConnection openConnection(URL paramURL, Proxy paramProxy)
/*     */     throws IOException
/*     */   {
/*  79 */     String str2 = paramURL.getFile();
/*  80 */     String str3 = paramURL.getHost();
/*     */ 
/*  82 */     String str1 = ParseUtil.decode(str2);
/*  83 */     str1 = str1.replace('/', '\\');
/*  84 */     str1 = str1.replace('|', ':');
/*     */ 
/*  86 */     if ((str3 == null) || (str3.equals("")) || (str3.equalsIgnoreCase("localhost")) || (str3.equals("~")))
/*     */     {
/*  89 */       return createFileURLConnection(paramURL, new File(str1));
/*     */     }
/*     */ 
/*  95 */     str1 = "\\\\" + str3 + str1;
/*  96 */     File localFile = new File(str1);
/*  97 */     if (localFile.exists()) {
/*  98 */       return createFileURLConnection(paramURL, localFile);
/*     */     }
/*     */ 
/*     */     URLConnection localURLConnection;
/*     */     try
/*     */     {
/* 108 */       URL localURL = new URL("ftp", str3, str2 + (paramURL.getRef() == null ? "" : new StringBuilder().append("#").append(paramURL.getRef()).toString()));
/*     */ 
/* 111 */       if (paramProxy != null)
/* 112 */         localURLConnection = localURL.openConnection(paramProxy);
/*     */       else
/* 114 */         localURLConnection = localURL.openConnection();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 117 */       localURLConnection = null;
/*     */     }
/* 119 */     if (localURLConnection == null) {
/* 120 */       throw new IOException("Unable to connect to: " + paramURL.toExternalForm());
/*     */     }
/*     */ 
/* 123 */     return localURLConnection;
/*     */   }
/*     */ 
/*     */   protected URLConnection createFileURLConnection(URL paramURL, File paramFile)
/*     */   {
/* 130 */     return new FileURLConnection(paramURL, paramFile);
/*     */   }
/*     */ 
/*     */   protected boolean hostsEqual(URL paramURL1, URL paramURL2)
/*     */   {
/* 146 */     String str1 = paramURL1.getHost();
/* 147 */     String str2 = paramURL2.getHost();
/* 148 */     if (("localhost".equalsIgnoreCase(str1)) && ((str2 == null) || ("".equals(str2))))
/* 149 */       return true;
/* 150 */     if (("localhost".equalsIgnoreCase(str2)) && ((str1 == null) || ("".equals(str1))))
/* 151 */       return true;
/* 152 */     return super.hostsEqual(paramURL1, paramURL2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.file.Handler
 * JD-Core Version:    0.6.2
 */