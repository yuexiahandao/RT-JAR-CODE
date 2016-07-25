/*     */ package sun.net.www;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.AccessController;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ class MimeLauncher extends Thread
/*     */ {
/*     */   URLConnection uc;
/*     */   MimeEntry m;
/*     */   String genericTempFileTemplate;
/*     */   InputStream is;
/*     */   String execPath;
/*     */ 
/*     */   MimeLauncher(MimeEntry paramMimeEntry, URLConnection paramURLConnection, InputStream paramInputStream, String paramString1, String paramString2)
/*     */     throws ApplicationLaunchException
/*     */   {
/*  40 */     super(paramString2);
/*  41 */     this.m = paramMimeEntry;
/*  42 */     this.uc = paramURLConnection;
/*  43 */     this.is = paramInputStream;
/*  44 */     this.genericTempFileTemplate = paramString1;
/*     */ 
/*  47 */     String str1 = this.m.getLaunchString();
/*     */ 
/*  52 */     if (!findExecutablePath(str1))
/*     */     {
/*  55 */       int i = str1.indexOf(' ');
/*     */       String str2;
/*  56 */       if (i != -1) {
/*  57 */         str2 = str1.substring(0, i);
/*     */       }
/*     */       else {
/*  60 */         str2 = str1;
/*     */       }
/*  62 */       throw new ApplicationLaunchException(str2);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String getTempFileName(URL paramURL, String paramString) {
/*  67 */     String str1 = paramString;
/*     */ 
/*  73 */     int i = str1.lastIndexOf("%s");
/*  74 */     String str2 = str1.substring(0, i);
/*     */ 
/*  76 */     String str3 = "";
/*  77 */     if (i < str1.length() - 2) {
/*  78 */       str3 = str1.substring(i + 2);
/*     */     }
/*     */ 
/*  81 */     long l = System.currentTimeMillis() / 1000L;
/*  82 */     int j = 0;
/*  83 */     while ((j = str2.indexOf("%s")) >= 0) {
/*  84 */       str2 = str2.substring(0, j) + l + str2.substring(j + 2);
/*     */     }
/*     */ 
/*  90 */     String str4 = paramURL.getFile();
/*     */ 
/*  92 */     String str5 = "";
/*  93 */     int k = str4.lastIndexOf('.');
/*     */ 
/*  97 */     if ((k >= 0) && (k > str4.lastIndexOf('/'))) {
/*  98 */       str5 = str4.substring(k);
/*     */     }
/*     */ 
/* 101 */     str4 = "HJ" + paramURL.hashCode();
/*     */ 
/* 103 */     str1 = str2 + str4 + l + str5 + str3;
/*     */ 
/* 105 */     return str1;
/*     */   }
/*     */ 
/*     */   public void run() {
/*     */     try {
/* 110 */       String str = this.m.getTempFileTemplate();
/* 111 */       if (str == null) {
/* 112 */         str = this.genericTempFileTemplate;
/*     */       }
/*     */ 
/* 115 */       str = getTempFileName(this.uc.getURL(), str);
/*     */       try {
/* 117 */         FileOutputStream localFileOutputStream = new FileOutputStream(str);
/* 118 */         localObject1 = new byte[2048];
/* 119 */         j = 0;
/*     */         try {
/* 121 */           while ((j = this.is.read((byte[])localObject1)) >= 0)
/* 122 */             localFileOutputStream.write((byte[])localObject1, 0, j);
/*     */         }
/*     */         catch (IOException localIOException3)
/*     */         {
/*     */         }
/*     */         finally {
/* 128 */           localFileOutputStream.close();
/* 129 */           this.is.close();
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException2)
/*     */       {
/*     */       }
/*     */ 
/* 136 */       int i = 0;
/* 137 */       Object localObject1 = this.execPath;
/* 138 */       while ((i = ((String)localObject1).indexOf("%t")) >= 0) {
/* 139 */         localObject1 = ((String)localObject1).substring(0, i) + this.uc.getContentType() + ((String)localObject1).substring(i + 2);
/*     */       }
/*     */ 
/* 143 */       int j = 0;
/* 144 */       while ((i = ((String)localObject1).indexOf("%s")) >= 0) {
/* 145 */         localObject1 = ((String)localObject1).substring(0, i) + str + ((String)localObject1).substring(i + 2);
/* 146 */         j = 1;
/*     */       }
/* 148 */       if (j == 0) {
/* 149 */         localObject1 = (String)localObject1 + " <" + str;
/*     */       }
/*     */ 
/* 153 */       Runtime.getRuntime().exec((String)localObject1);
/*     */     }
/*     */     catch (IOException localIOException1)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean findExecutablePath(String paramString)
/*     */   {
/* 164 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 165 */       return false;
/*     */     }
/*     */ 
/* 169 */     int i = paramString.indexOf(' ');
/*     */     String str1;
/* 170 */     if (i != -1) {
/* 171 */       str1 = paramString.substring(0, i);
/*     */     }
/*     */     else {
/* 174 */       str1 = paramString;
/*     */     }
/*     */ 
/* 177 */     File localFile = new File(str1);
/* 178 */     if (localFile.isFile())
/*     */     {
/* 180 */       this.execPath = paramString;
/* 181 */       return true;
/*     */     }
/*     */ 
/* 185 */     String str2 = (String)AccessController.doPrivileged(new GetPropertyAction("exec.path"));
/*     */ 
/* 187 */     if (str2 == null)
/*     */     {
/* 189 */       return false;
/*     */     }
/*     */ 
/* 192 */     StringTokenizer localStringTokenizer = new StringTokenizer(str2, "|");
/* 193 */     while (localStringTokenizer.hasMoreElements()) {
/* 194 */       String str3 = (String)localStringTokenizer.nextElement();
/* 195 */       String str4 = str3 + File.separator + str1;
/* 196 */       localFile = new File(str4);
/* 197 */       if (localFile.isFile()) {
/* 198 */         this.execPath = (str3 + File.separator + paramString);
/* 199 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 203 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.MimeLauncher
 * JD-Core Version:    0.6.2
 */