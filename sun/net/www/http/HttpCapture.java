/*     */ package sun.net.www.http;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Random;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import sun.net.NetProperties;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public class HttpCapture
/*     */ {
/*  57 */   private File file = null;
/*  58 */   private boolean incoming = true;
/*  59 */   private BufferedWriter out = null;
/*  60 */   private static boolean initialized = false;
/*  61 */   private static volatile ArrayList<Pattern> patterns = null;
/*  62 */   private static volatile ArrayList<String> capFiles = null;
/*     */ 
/*     */   private static synchronized void init() {
/*  65 */     initialized = true;
/*  66 */     String str1 = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public String run() {
/*  69 */         return NetProperties.get("sun.net.http.captureRules");
/*     */       }
/*     */     });
/*  72 */     if ((str1 != null) && (!str1.isEmpty())) {
/*     */       BufferedReader localBufferedReader;
/*     */       try {
/*  75 */         localBufferedReader = new BufferedReader(new FileReader(str1));
/*     */       } catch (FileNotFoundException localFileNotFoundException) {
/*  77 */         return;
/*     */       }
/*     */       try {
/*  80 */         String str2 = localBufferedReader.readLine();
/*  81 */         while (str2 != null) {
/*  82 */           str2 = str2.trim();
/*  83 */           if (!str2.startsWith("#"))
/*     */           {
/*  85 */             String[] arrayOfString = str2.split(",");
/*  86 */             if (arrayOfString.length == 2) {
/*  87 */               if (patterns == null) {
/*  88 */                 patterns = new ArrayList();
/*  89 */                 capFiles = new ArrayList();
/*     */               }
/*  91 */               patterns.add(Pattern.compile(arrayOfString[0].trim()));
/*  92 */               capFiles.add(arrayOfString[1].trim());
/*     */             }
/*     */           }
/*  95 */           str2 = localBufferedReader.readLine();
/*     */         }
/*     */       } catch (IOException localIOException2) {
/*     */       }
/*     */       finally {
/*     */         try {
/* 101 */           localBufferedReader.close();
/*     */         } catch (IOException localIOException4) {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static synchronized boolean isInitialized() {
/* 109 */     return initialized;
/*     */   }
/*     */ 
/*     */   private HttpCapture(File paramFile, URL paramURL) {
/* 113 */     this.file = paramFile;
/*     */     try {
/* 115 */       this.out = new BufferedWriter(new FileWriter(this.file, true));
/* 116 */       this.out.write("URL: " + paramURL + "\n");
/*     */     } catch (IOException localIOException) {
/* 118 */       PlatformLogger.getLogger(HttpCapture.class.getName()).severe(null, localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void sent(int paramInt) throws IOException {
/* 123 */     if (this.incoming) {
/* 124 */       this.out.write("\n------>\n");
/* 125 */       this.incoming = false;
/* 126 */       this.out.flush();
/*     */     }
/* 128 */     this.out.write(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized void received(int paramInt) throws IOException {
/* 132 */     if (!this.incoming) {
/* 133 */       this.out.write("\n<------\n");
/* 134 */       this.incoming = true;
/* 135 */       this.out.flush();
/*     */     }
/* 137 */     this.out.write(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized void flush() throws IOException {
/* 141 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   public static HttpCapture getCapture(URL paramURL) {
/* 145 */     if (!isInitialized()) {
/* 146 */       init();
/*     */     }
/* 148 */     if ((patterns == null) || (patterns.isEmpty())) {
/* 149 */       return null;
/*     */     }
/* 151 */     String str1 = paramURL.toString();
/* 152 */     for (int i = 0; i < patterns.size(); i++) {
/* 153 */       Pattern localPattern = (Pattern)patterns.get(i);
/* 154 */       if (localPattern.matcher(str1).find()) {
/* 155 */         String str2 = (String)capFiles.get(i);
/*     */         File localFile;
/* 157 */         if (str2.indexOf("%d") >= 0) {
/* 158 */           Random localRandom = new Random();
/*     */           do {
/* 160 */             String str3 = str2.replace("%d", Integer.toString(localRandom.nextInt()));
/* 161 */             localFile = new File(str3);
/* 162 */           }while (localFile.exists());
/*     */         } else {
/* 164 */           localFile = new File(str2);
/*     */         }
/* 166 */         return new HttpCapture(localFile, paramURL);
/*     */       }
/*     */     }
/* 169 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.http.HttpCapture
 * JD-Core Version:    0.6.2
 */