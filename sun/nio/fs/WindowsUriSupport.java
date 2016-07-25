/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ 
/*     */ class WindowsUriSupport
/*     */ {
/*     */   private static final String IPV6_LITERAL_SUFFIX = ".ipv6-literal.net";
/*     */ 
/*     */   private static URI toUri(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*     */     String str1;
/*     */     String str2;
/*  49 */     if (paramBoolean1) {
/*  50 */       int i = paramString.indexOf('\\', 2);
/*  51 */       str1 = paramString.substring(2, i);
/*  52 */       str2 = paramString.substring(i).replace('\\', '/');
/*     */ 
/*  58 */       if (str1.endsWith(".ipv6-literal.net")) {
/*  59 */         str1 = str1.substring(0, str1.length() - ".ipv6-literal.net".length()).replace('-', ':').replace('s', '%');
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*  65 */       str1 = "";
/*  66 */       str2 = "/" + paramString.replace('\\', '/');
/*     */     }
/*     */ 
/*  70 */     if (paramBoolean2) {
/*  71 */       str2 = str2 + "/";
/*     */     }
/*     */     try
/*     */     {
/*  75 */       return new URI("file", str1, str2, null);
/*     */     } catch (URISyntaxException localURISyntaxException1) {
/*  77 */       if (!paramBoolean1) {
/*  78 */         throw new AssertionError(localURISyntaxException1);
/*     */       }
/*     */ 
/*  85 */       str2 = "//" + paramString.replace('\\', '/');
/*  86 */       if (paramBoolean2)
/*  87 */         str2 = str2 + "/";
/*     */       try {
/*  89 */         return new URI("file", null, str2, null);
/*     */       } catch (URISyntaxException localURISyntaxException2) {
/*  91 */         throw new AssertionError(localURISyntaxException2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static URI toUri(WindowsPath paramWindowsPath)
/*     */   {
/*  99 */     paramWindowsPath = paramWindowsPath.toAbsolutePath();
/* 100 */     String str = paramWindowsPath.toString();
/*     */ 
/* 104 */     boolean bool = false;
/* 105 */     if (!str.endsWith("\\"))
/*     */       try {
/* 107 */         bool = WindowsFileAttributes.get(paramWindowsPath, true).isDirectory();
/*     */       }
/*     */       catch (WindowsException localWindowsException)
/*     */       {
/*     */       }
/* 112 */     return toUri(str, paramWindowsPath.isUnc(), bool);
/*     */   }
/*     */ 
/*     */   static WindowsPath fromUri(WindowsFileSystem paramWindowsFileSystem, URI paramURI)
/*     */   {
/* 119 */     if (!paramURI.isAbsolute())
/* 120 */       throw new IllegalArgumentException("URI is not absolute");
/* 121 */     if (paramURI.isOpaque())
/* 122 */       throw new IllegalArgumentException("URI is not hierarchical");
/* 123 */     String str1 = paramURI.getScheme();
/* 124 */     if ((str1 == null) || (!str1.equalsIgnoreCase("file")))
/* 125 */       throw new IllegalArgumentException("URI scheme is not \"file\"");
/* 126 */     if (paramURI.getFragment() != null)
/* 127 */       throw new IllegalArgumentException("URI has a fragment component");
/* 128 */     if (paramURI.getQuery() != null)
/* 129 */       throw new IllegalArgumentException("URI has a query component");
/* 130 */     String str2 = paramURI.getPath();
/* 131 */     if (str2.equals("")) {
/* 132 */       throw new IllegalArgumentException("URI path component is empty");
/*     */     }
/*     */ 
/* 135 */     String str3 = paramURI.getAuthority();
/* 136 */     if ((str3 != null) && (!str3.equals(""))) {
/* 137 */       String str4 = paramURI.getHost();
/* 138 */       if (str4 == null)
/* 139 */         throw new IllegalArgumentException("URI authority component has undefined host");
/* 140 */       if (paramURI.getUserInfo() != null)
/* 141 */         throw new IllegalArgumentException("URI authority component has user-info");
/* 142 */       if (paramURI.getPort() != -1) {
/* 143 */         throw new IllegalArgumentException("URI authority component has port number");
/*     */       }
/*     */ 
/* 150 */       if (str4.startsWith("[")) {
/* 151 */         str4 = str4.substring(1, str4.length() - 1).replace(':', '-').replace('%', 's');
/*     */ 
/* 154 */         str4 = str4 + ".ipv6-literal.net";
/*     */       }
/*     */ 
/* 158 */       str2 = "\\\\" + str4 + str2;
/*     */     }
/* 160 */     else if ((str2.length() > 2) && (str2.charAt(2) == ':'))
/*     */     {
/* 162 */       str2 = str2.substring(1);
/*     */     }
/*     */ 
/* 165 */     return WindowsPath.parse(paramWindowsFileSystem, str2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsUriSupport
 * JD-Core Version:    0.6.2
 */