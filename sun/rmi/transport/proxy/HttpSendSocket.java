/*     */ package sun.rmi.transport.proxy;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketImpl;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.AccessController;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ class HttpSendSocket extends Socket
/*     */   implements RMISocketInfo
/*     */ {
/*     */   protected String host;
/*     */   protected int port;
/*     */   protected URL url;
/*  62 */   protected URLConnection conn = null;
/*     */ 
/*  65 */   protected InputStream in = null;
/*     */ 
/*  68 */   protected OutputStream out = null;
/*     */   protected HttpSendInputStream inNotifier;
/*     */   protected HttpSendOutputStream outNotifier;
/*  80 */   private String lineSeparator = (String)AccessController.doPrivileged(new GetPropertyAction("line.separator"));
/*     */ 
/*     */   public HttpSendSocket(String paramString, int paramInt, URL paramURL)
/*     */     throws IOException
/*     */   {
/*  92 */     super((SocketImpl)null);
/*     */ 
/*  94 */     if (RMIMasterSocketFactory.proxyLog.isLoggable(Log.VERBOSE)) {
/*  95 */       RMIMasterSocketFactory.proxyLog.log(Log.VERBOSE, "host = " + paramString + ", port = " + paramInt + ", url = " + paramURL);
/*     */     }
/*     */ 
/*  99 */     this.host = paramString;
/* 100 */     this.port = paramInt;
/* 101 */     this.url = paramURL;
/*     */ 
/* 103 */     this.inNotifier = new HttpSendInputStream(null, this);
/* 104 */     this.outNotifier = new HttpSendOutputStream(writeNotify(), this);
/*     */   }
/*     */ 
/*     */   public HttpSendSocket(String paramString, int paramInt)
/*     */     throws IOException
/*     */   {
/* 115 */     this(paramString, paramInt, new URL("http", paramString, paramInt, "/"));
/*     */   }
/*     */ 
/*     */   public HttpSendSocket(InetAddress paramInetAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 126 */     this(paramInetAddress.getHostName(), paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isReusable()
/*     */   {
/* 134 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized OutputStream writeNotify()
/*     */     throws IOException
/*     */   {
/* 143 */     if (this.conn != null) {
/* 144 */       throw new IOException("attempt to write on HttpSendSocket after request has been sent");
/*     */     }
/*     */ 
/* 148 */     this.conn = this.url.openConnection();
/* 149 */     this.conn.setDoOutput(true);
/* 150 */     this.conn.setUseCaches(false);
/* 151 */     this.conn.setRequestProperty("Content-type", "application/octet-stream");
/*     */ 
/* 153 */     this.inNotifier.deactivate();
/* 154 */     this.in = null;
/*     */ 
/* 156 */     return this.out = this.conn.getOutputStream();
/*     */   }
/*     */ 
/*     */   public synchronized InputStream readNotify()
/*     */     throws IOException
/*     */   {
/* 164 */     RMIMasterSocketFactory.proxyLog.log(Log.VERBOSE, "sending request and activating input stream");
/*     */ 
/* 167 */     this.outNotifier.deactivate();
/* 168 */     this.out.close();
/* 169 */     this.out = null;
/*     */     try
/*     */     {
/* 172 */       this.in = this.conn.getInputStream();
/*     */     } catch (IOException localIOException1) {
/* 174 */       RMIMasterSocketFactory.proxyLog.log(Log.BRIEF, "failed to get input stream, exception: ", localIOException1);
/*     */ 
/* 177 */       throw new IOException("HTTP request failed");
/*     */     }
/*     */ 
/* 190 */     String str1 = this.conn.getContentType();
/* 191 */     if ((str1 == null) || (!this.conn.getContentType().equals("application/octet-stream")))
/*     */     {
/* 194 */       if (RMIMasterSocketFactory.proxyLog.isLoggable(Log.BRIEF))
/*     */       {
/* 196 */         if (str1 == null) {
/* 197 */           str2 = "missing content type in response" + this.lineSeparator;
/*     */         }
/*     */         else {
/* 200 */           str2 = "invalid content type in response: " + str1 + this.lineSeparator;
/*     */         }
/*     */ 
/* 204 */         String str2 = str2 + "HttpSendSocket.readNotify: response body: ";
/*     */         try {
/* 206 */           BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(this.in));
/*     */           String str3;
/* 208 */           while ((str3 = localBufferedReader.readLine()) != null)
/* 209 */             str2 = str2 + str3 + this.lineSeparator;
/*     */         } catch (IOException localIOException2) {
/*     */         }
/* 212 */         RMIMasterSocketFactory.proxyLog.log(Log.BRIEF, str2);
/*     */       }
/*     */ 
/* 215 */       throw new IOException("HTTP request failed");
/*     */     }
/*     */ 
/* 218 */     return this.in;
/*     */   }
/*     */ 
/*     */   public InetAddress getInetAddress()
/*     */   {
/*     */     try
/*     */     {
/* 227 */       return InetAddress.getByName(this.host); } catch (UnknownHostException localUnknownHostException) {
/*     */     }
/* 229 */     return null;
/*     */   }
/*     */ 
/*     */   public InetAddress getLocalAddress()
/*     */   {
/*     */     try
/*     */     {
/* 239 */       return InetAddress.getLocalHost(); } catch (UnknownHostException localUnknownHostException) {
/*     */     }
/* 241 */     return null;
/*     */   }
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 250 */     return this.port;
/*     */   }
/*     */ 
/*     */   public int getLocalPort()
/*     */   {
/* 258 */     return -1;
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 266 */     return this.inNotifier;
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 274 */     return this.outNotifier;
/*     */   }
/*     */ 
/*     */   public void setTcpNoDelay(boolean paramBoolean)
/*     */     throws SocketException
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean getTcpNoDelay()
/*     */     throws SocketException
/*     */   {
/* 290 */     return false;
/*     */   }
/*     */ 
/*     */   public void setSoLinger(boolean paramBoolean, int paramInt)
/*     */     throws SocketException
/*     */   {
/*     */   }
/*     */ 
/*     */   public int getSoLinger()
/*     */     throws SocketException
/*     */   {
/* 306 */     return -1;
/*     */   }
/*     */ 
/*     */   public synchronized void setSoTimeout(int paramInt)
/*     */     throws SocketException
/*     */   {
/*     */   }
/*     */ 
/*     */   public synchronized int getSoTimeout()
/*     */     throws SocketException
/*     */   {
/* 322 */     return 0;
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */     throws IOException
/*     */   {
/* 330 */     if (this.out != null)
/* 331 */       this.out.close();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 339 */     return "HttpSendSocket[host=" + this.host + ",port=" + this.port + ",url=" + this.url + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.HttpSendSocket
 * JD-Core Version:    0.6.2
 */