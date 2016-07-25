/*     */ package sun.net.www.protocol.file;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FilePermission;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.FileNameMap;
/*     */ import java.net.URL;
/*     */ import java.security.Permission;
/*     */ import java.text.Collator;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import sun.net.ProgressMonitor;
/*     */ import sun.net.ProgressSource;
/*     */ import sun.net.www.MessageHeader;
/*     */ import sun.net.www.MeteredStream;
/*     */ import sun.net.www.ParseUtil;
/*     */ 
/*     */ public class FileURLConnection extends sun.net.www.URLConnection
/*     */ {
/*  50 */   static String CONTENT_LENGTH = "content-length";
/*  51 */   static String CONTENT_TYPE = "content-type";
/*  52 */   static String TEXT_PLAIN = "text/plain";
/*  53 */   static String LAST_MODIFIED = "last-modified";
/*     */   String contentType;
/*     */   InputStream is;
/*     */   File file;
/*     */   String filename;
/*  60 */   boolean isDirectory = false;
/*  61 */   boolean exists = false;
/*     */   List<String> files;
/*  64 */   long length = -1L;
/*  65 */   long lastModified = 0L;
/*     */ 
/* 106 */   private boolean initializedHeaders = false;
/*     */   Permission permission;
/*     */ 
/*     */   protected FileURLConnection(URL paramURL, File paramFile)
/*     */   {
/*  68 */     super(paramURL);
/*  69 */     this.file = paramFile;
/*     */   }
/*     */ 
/*     */   public void connect()
/*     */     throws IOException
/*     */   {
/*  79 */     if (!this.connected) {
/*     */       try {
/*  81 */         this.filename = this.file.toString();
/*  82 */         this.isDirectory = this.file.isDirectory();
/*  83 */         if (this.isDirectory) {
/*  84 */           String[] arrayOfString = this.file.list();
/*  85 */           if (arrayOfString == null)
/*  86 */             throw new FileNotFoundException(this.filename + " exists, but is not accessible");
/*  87 */           this.files = Arrays.asList(arrayOfString);
/*     */         }
/*     */         else {
/*  90 */           this.is = new BufferedInputStream(new FileInputStream(this.filename));
/*     */ 
/*  93 */           boolean bool = ProgressMonitor.getDefault().shouldMeterInput(this.url, "GET");
/*  94 */           if (bool) {
/*  95 */             ProgressSource localProgressSource = new ProgressSource(this.url, "GET", this.file.length());
/*  96 */             this.is = new MeteredStream(this.is, localProgressSource, this.file.length());
/*     */           }
/*     */         }
/*     */       } catch (IOException localIOException) {
/* 100 */         throw localIOException;
/*     */       }
/* 102 */       this.connected = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initializeHeaders()
/*     */   {
/*     */     try
/*     */     {
/* 110 */       connect();
/* 111 */       this.exists = this.file.exists();
/*     */     } catch (IOException localIOException) {
/*     */     }
/* 114 */     if ((!this.initializedHeaders) || (!this.exists)) {
/* 115 */       this.length = this.file.length();
/* 116 */       this.lastModified = this.file.lastModified();
/*     */ 
/* 118 */       if (!this.isDirectory) {
/* 119 */         FileNameMap localFileNameMap = java.net.URLConnection.getFileNameMap();
/* 120 */         this.contentType = localFileNameMap.getContentTypeFor(this.filename);
/* 121 */         if (this.contentType != null) {
/* 122 */           this.properties.add(CONTENT_TYPE, this.contentType);
/*     */         }
/* 124 */         this.properties.add(CONTENT_LENGTH, String.valueOf(this.length));
/*     */ 
/* 131 */         if (this.lastModified != 0L) {
/* 132 */           Date localDate = new Date(this.lastModified);
/* 133 */           SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
/*     */ 
/* 135 */           localSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
/* 136 */           this.properties.add(LAST_MODIFIED, localSimpleDateFormat.format(localDate));
/*     */         }
/*     */       } else {
/* 139 */         this.properties.add(CONTENT_TYPE, TEXT_PLAIN);
/*     */       }
/* 141 */       this.initializedHeaders = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getHeaderField(String paramString) {
/* 146 */     initializeHeaders();
/* 147 */     return super.getHeaderField(paramString);
/*     */   }
/*     */ 
/*     */   public String getHeaderField(int paramInt) {
/* 151 */     initializeHeaders();
/* 152 */     return super.getHeaderField(paramInt);
/*     */   }
/*     */ 
/*     */   public int getContentLength() {
/* 156 */     initializeHeaders();
/* 157 */     if (this.length > 2147483647L)
/* 158 */       return -1;
/* 159 */     return (int)this.length;
/*     */   }
/*     */ 
/*     */   public long getContentLengthLong() {
/* 163 */     initializeHeaders();
/* 164 */     return this.length;
/*     */   }
/*     */ 
/*     */   public String getHeaderFieldKey(int paramInt) {
/* 168 */     initializeHeaders();
/* 169 */     return super.getHeaderFieldKey(paramInt);
/*     */   }
/*     */ 
/*     */   public MessageHeader getProperties() {
/* 173 */     initializeHeaders();
/* 174 */     return super.getProperties();
/*     */   }
/*     */ 
/*     */   public long getLastModified() {
/* 178 */     initializeHeaders();
/* 179 */     return this.lastModified;
/*     */   }
/*     */ 
/*     */   public synchronized InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 188 */     connect();
/*     */ 
/* 190 */     if (this.is == null) {
/* 191 */       if (this.isDirectory) {
/* 192 */         FileNameMap localFileNameMap = java.net.URLConnection.getFileNameMap();
/*     */ 
/* 194 */         StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 196 */         if (this.files == null) {
/* 197 */           throw new FileNotFoundException(this.filename);
/*     */         }
/*     */ 
/* 200 */         Collections.sort(this.files, Collator.getInstance());
/*     */ 
/* 202 */         for (int i = 0; i < this.files.size(); i++) {
/* 203 */           String str = (String)this.files.get(i);
/* 204 */           localStringBuffer.append(str);
/* 205 */           localStringBuffer.append("\n");
/*     */         }
/*     */ 
/* 208 */         this.is = new ByteArrayInputStream(localStringBuffer.toString().getBytes());
/*     */       } else {
/* 210 */         throw new FileNotFoundException(this.filename);
/*     */       }
/*     */     }
/* 213 */     return this.is;
/*     */   }
/*     */ 
/*     */   public Permission getPermission()
/*     */     throws IOException
/*     */   {
/* 222 */     if (this.permission == null) {
/* 223 */       String str = ParseUtil.decode(this.url.getPath());
/* 224 */       if (File.separatorChar == '/')
/* 225 */         this.permission = new FilePermission(str, "read");
/*     */       else {
/* 227 */         this.permission = new FilePermission(str.replace('/', File.separatorChar), "read");
/*     */       }
/*     */     }
/*     */ 
/* 231 */     return this.permission;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.file.FileURLConnection
 * JD-Core Version:    0.6.2
 */