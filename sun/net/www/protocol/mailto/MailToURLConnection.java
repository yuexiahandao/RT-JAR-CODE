/*     */ package sun.net.www.protocol.mailto;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketPermission;
/*     */ import java.net.URL;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.Permission;
/*     */ import sun.net.smtp.SmtpClient;
/*     */ import sun.net.www.MessageHeader;
/*     */ import sun.net.www.ParseUtil;
/*     */ import sun.net.www.URLConnection;
/*     */ 
/*     */ public class MailToURLConnection extends URLConnection
/*     */ {
/*  45 */   InputStream is = null;
/*  46 */   OutputStream os = null;
/*     */   SmtpClient client;
/*     */   Permission permission;
/*  50 */   private int connectTimeout = -1;
/*  51 */   private int readTimeout = -1;
/*     */ 
/*     */   MailToURLConnection(URL paramURL) {
/*  54 */     super(paramURL);
/*     */ 
/*  56 */     MessageHeader localMessageHeader = new MessageHeader();
/*  57 */     localMessageHeader.add("content-type", "text/html");
/*  58 */     setProperties(localMessageHeader);
/*     */   }
/*     */ 
/*     */   String getFromAddress()
/*     */   {
/*  66 */     String str1 = System.getProperty("user.fromaddr");
/*  67 */     if (str1 == null) {
/*  68 */       str1 = System.getProperty("user.name");
/*  69 */       if (str1 != null) {
/*  70 */         String str2 = System.getProperty("mail.host");
/*  71 */         if (str2 == null)
/*     */           try {
/*  73 */             str2 = InetAddress.getLocalHost().getHostName();
/*     */           }
/*     */           catch (UnknownHostException localUnknownHostException) {
/*     */           }
/*  77 */         str1 = str1 + "@" + str2;
/*     */       } else {
/*  79 */         str1 = "";
/*     */       }
/*     */     }
/*  82 */     return str1;
/*     */   }
/*     */ 
/*     */   public void connect() throws IOException {
/*  86 */     this.client = new SmtpClient(this.connectTimeout);
/*  87 */     this.client.setReadTimeout(this.readTimeout);
/*     */   }
/*     */ 
/*     */   public synchronized OutputStream getOutputStream() throws IOException
/*     */   {
/*  92 */     if (this.os != null)
/*  93 */       return this.os;
/*  94 */     if (this.is != null) {
/*  95 */       throw new IOException("Cannot write output after reading input.");
/*     */     }
/*  97 */     connect();
/*     */ 
/*  99 */     String str = ParseUtil.decode(this.url.getPath());
/* 100 */     this.client.from(getFromAddress());
/* 101 */     this.client.to(str);
/*     */ 
/* 103 */     this.os = this.client.startMessage();
/* 104 */     return this.os;
/*     */   }
/*     */ 
/*     */   public Permission getPermission() throws IOException
/*     */   {
/* 109 */     if (this.permission == null) {
/* 110 */       connect();
/* 111 */       String str = this.client.getMailHost() + ":" + 25;
/* 112 */       this.permission = new SocketPermission(str, "connect");
/*     */     }
/* 114 */     return this.permission;
/*     */   }
/*     */ 
/*     */   public void setConnectTimeout(int paramInt)
/*     */   {
/* 119 */     if (paramInt < 0)
/* 120 */       throw new IllegalArgumentException("timeouts can't be negative");
/* 121 */     this.connectTimeout = paramInt;
/*     */   }
/*     */ 
/*     */   public int getConnectTimeout()
/*     */   {
/* 126 */     return this.connectTimeout < 0 ? 0 : this.connectTimeout;
/*     */   }
/*     */ 
/*     */   public void setReadTimeout(int paramInt)
/*     */   {
/* 131 */     if (paramInt < 0)
/* 132 */       throw new IllegalArgumentException("timeouts can't be negative");
/* 133 */     this.readTimeout = paramInt;
/*     */   }
/*     */ 
/*     */   public int getReadTimeout()
/*     */   {
/* 138 */     return this.readTimeout < 0 ? 0 : this.readTimeout;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.mailto.MailToURLConnection
 * JD-Core Version:    0.6.2
 */