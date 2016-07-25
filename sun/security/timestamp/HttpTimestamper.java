/*     */ package sun.security.timestamp;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import sun.misc.IOUtils;
/*     */ 
/*     */ public class HttpTimestamper
/*     */   implements Timestamper
/*     */ {
/*     */   private static final int CONNECT_TIMEOUT = 15000;
/*     */   private static final String TS_QUERY_MIME_TYPE = "application/timestamp-query";
/*     */   private static final String TS_REPLY_MIME_TYPE = "application/timestamp-reply";
/*     */   private static final boolean DEBUG = false;
/*  67 */   private String tsaUrl = null;
/*     */ 
/*     */   public HttpTimestamper(String paramString)
/*     */   {
/*  75 */     this.tsaUrl = paramString;
/*     */   }
/*     */ 
/*     */   public TSResponse generateTimestamp(TSRequest paramTSRequest)
/*     */     throws IOException
/*     */   {
/*  88 */     HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(this.tsaUrl).openConnection();
/*     */ 
/*  90 */     localHttpURLConnection.setDoOutput(true);
/*  91 */     localHttpURLConnection.setUseCaches(false);
/*  92 */     localHttpURLConnection.setRequestProperty("Content-Type", "application/timestamp-query");
/*  93 */     localHttpURLConnection.setRequestMethod("POST");
/*     */ 
/*  95 */     localHttpURLConnection.setConnectTimeout(15000);
/*     */ 
/* 106 */     localHttpURLConnection.connect();
/*     */ 
/* 109 */     DataOutputStream localDataOutputStream = null;
/*     */     try {
/* 111 */       localDataOutputStream = new DataOutputStream(localHttpURLConnection.getOutputStream());
/* 112 */       localObject1 = paramTSRequest.encode();
/* 113 */       localDataOutputStream.write((byte[])localObject1, 0, localObject1.length);
/* 114 */       localDataOutputStream.flush();
/*     */     }
/*     */     finally
/*     */     {
/* 120 */       if (localDataOutputStream != null) {
/* 121 */         localDataOutputStream.close();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 126 */     Object localObject1 = null;
/* 127 */     byte[] arrayOfByte = null;
/*     */     try {
/* 129 */       localObject1 = new BufferedInputStream(localHttpURLConnection.getInputStream());
/*     */ 
/* 142 */       verifyMimeType(localHttpURLConnection.getContentType());
/*     */ 
/* 144 */       int i = 0;
/* 145 */       int j = localHttpURLConnection.getContentLength();
/* 146 */       arrayOfByte = IOUtils.readFully((InputStream)localObject1, j, false);
/*     */     }
/*     */     finally
/*     */     {
/* 153 */       if (localObject1 != null) {
/* 154 */         ((BufferedInputStream)localObject1).close();
/*     */       }
/*     */     }
/* 157 */     return new TSResponse(arrayOfByte);
/*     */   }
/*     */ 
/*     */   private static void verifyMimeType(String paramString)
/*     */     throws IOException
/*     */   {
/* 167 */     if (!"application/timestamp-reply".equalsIgnoreCase(paramString))
/* 168 */       throw new IOException("MIME Content-Type is not application/timestamp-reply");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.timestamp.HttpTimestamper
 * JD-Core Version:    0.6.2
 */