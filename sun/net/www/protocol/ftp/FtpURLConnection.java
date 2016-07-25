/*     */ package sun.net.www.protocol.ftp;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.Proxy.Type;
/*     */ import java.net.ProxySelector;
/*     */ import java.net.SocketPermission;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.net.ProgressMonitor;
/*     */ import sun.net.ProgressSource;
/*     */ import sun.net.ftp.FtpClient;
/*     */ import sun.net.ftp.FtpLoginException;
/*     */ import sun.net.ftp.FtpProtocolException;
/*     */ import sun.net.www.MessageHeader;
/*     */ import sun.net.www.MeteredStream;
/*     */ import sun.net.www.ParseUtil;
/*     */ import sun.net.www.URLConnection;
/*     */ import sun.net.www.protocol.http.HttpURLConnection;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class FtpURLConnection extends URLConnection
/*     */ {
/*  84 */   HttpURLConnection http = null;
/*     */   private Proxy instProxy;
/*  87 */   InputStream is = null;
/*  88 */   OutputStream os = null;
/*     */ 
/*  90 */   FtpClient ftp = null;
/*     */   Permission permission;
/*     */   String password;
/*     */   String user;
/*     */   String host;
/*     */   String pathname;
/*     */   String filename;
/*     */   String fullpath;
/*     */   int port;
/*     */   static final int NONE = 0;
/*     */   static final int ASCII = 1;
/*     */   static final int BIN = 2;
/*     */   static final int DIR = 3;
/* 105 */   int type = 0;
/*     */ 
/* 109 */   private int connectTimeout = -1;
/* 110 */   private int readTimeout = -1;
/*     */ 
/*     */   public FtpURLConnection(URL paramURL)
/*     */   {
/* 164 */     this(paramURL, null);
/*     */   }
/*     */ 
/*     */   FtpURLConnection(URL paramURL, Proxy paramProxy)
/*     */   {
/* 171 */     super(paramURL);
/* 172 */     this.instProxy = paramProxy;
/* 173 */     this.host = paramURL.getHost();
/* 174 */     this.port = paramURL.getPort();
/* 175 */     String str = paramURL.getUserInfo();
/*     */ 
/* 177 */     if (str != null) {
/* 178 */       int i = str.indexOf(':');
/* 179 */       if (i == -1) {
/* 180 */         this.user = ParseUtil.decode(str);
/* 181 */         this.password = null;
/*     */       } else {
/* 183 */         this.user = ParseUtil.decode(str.substring(0, i++));
/* 184 */         this.password = ParseUtil.decode(str.substring(i));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setTimeouts() {
/* 190 */     if (this.ftp != null) {
/* 191 */       if (this.connectTimeout >= 0) {
/* 192 */         this.ftp.setConnectTimeout(this.connectTimeout);
/*     */       }
/* 194 */       if (this.readTimeout >= 0)
/* 195 */         this.ftp.setReadTimeout(this.readTimeout);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void connect()
/*     */     throws IOException
/*     */   {
/* 209 */     if (this.connected) {
/* 210 */       return;
/*     */     }
/*     */ 
/* 213 */     Proxy localProxy = null;
/*     */     Object localObject;
/* 214 */     if (this.instProxy == null)
/*     */     {
/* 218 */       localObject = (ProxySelector)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public ProxySelector run() {
/* 221 */           return ProxySelector.getDefault();
/*     */         }
/*     */       });
/* 224 */       if (localObject != null) {
/* 225 */         URI localURI = ParseUtil.toURI(this.url);
/* 226 */         Iterator localIterator = ((ProxySelector)localObject).select(localURI).iterator();
/* 227 */         while (localIterator.hasNext()) {
/* 228 */           localProxy = (Proxy)localIterator.next();
/* 229 */           if ((localProxy == null) || (localProxy == Proxy.NO_PROXY) || (localProxy.type() == Proxy.Type.SOCKS))
/*     */           {
/*     */             break;
/*     */           }
/* 233 */           if ((localProxy.type() != Proxy.Type.HTTP) || (!(localProxy.address() instanceof InetSocketAddress)))
/*     */           {
/* 235 */             ((ProxySelector)localObject).connectFailed(localURI, localProxy.address(), new IOException("Wrong proxy type"));
/*     */           }
/*     */           else
/*     */           {
/* 239 */             InetSocketAddress localInetSocketAddress = (InetSocketAddress)localProxy.address();
/*     */             try {
/* 241 */               this.http = new HttpURLConnection(this.url, localProxy);
/* 242 */               this.http.setDoInput(getDoInput());
/* 243 */               this.http.setDoOutput(getDoOutput());
/* 244 */               if (this.connectTimeout >= 0) {
/* 245 */                 this.http.setConnectTimeout(this.connectTimeout);
/*     */               }
/* 247 */               if (this.readTimeout >= 0) {
/* 248 */                 this.http.setReadTimeout(this.readTimeout);
/*     */               }
/* 250 */               this.http.connect();
/* 251 */               this.connected = true;
/* 252 */               return;
/*     */             } catch (IOException localIOException) {
/* 254 */               ((ProxySelector)localObject).connectFailed(localURI, localInetSocketAddress, localIOException);
/* 255 */               this.http = null;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     } else { localProxy = this.instProxy;
/* 261 */       if (localProxy.type() == Proxy.Type.HTTP) {
/* 262 */         this.http = new HttpURLConnection(this.url, this.instProxy);
/* 263 */         this.http.setDoInput(getDoInput());
/* 264 */         this.http.setDoOutput(getDoOutput());
/* 265 */         if (this.connectTimeout >= 0) {
/* 266 */           this.http.setConnectTimeout(this.connectTimeout);
/*     */         }
/* 268 */         if (this.readTimeout >= 0) {
/* 269 */           this.http.setReadTimeout(this.readTimeout);
/*     */         }
/* 271 */         this.http.connect();
/* 272 */         this.connected = true;
/* 273 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 277 */     if (this.user == null) {
/* 278 */       this.user = "anonymous";
/* 279 */       localObject = (String)AccessController.doPrivileged(new GetPropertyAction("java.version"));
/*     */ 
/* 281 */       this.password = ((String)AccessController.doPrivileged(new GetPropertyAction("ftp.protocol.user", "Java" + (String)localObject + "@")));
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 286 */       this.ftp = FtpClient.create();
/* 287 */       if (localProxy != null) {
/* 288 */         this.ftp.setProxy(localProxy);
/*     */       }
/* 290 */       setTimeouts();
/* 291 */       if (this.port != -1)
/* 292 */         this.ftp.connect(new InetSocketAddress(this.host, this.port));
/*     */       else {
/* 294 */         this.ftp.connect(new InetSocketAddress(this.host, FtpClient.defaultPort()));
/*     */       }
/*     */     }
/*     */     catch (UnknownHostException localUnknownHostException)
/*     */     {
/* 299 */       throw localUnknownHostException;
/*     */     } catch (FtpProtocolException localFtpProtocolException1) {
/* 301 */       throw new IOException(localFtpProtocolException1);
/*     */     }
/*     */     try {
/* 304 */       this.ftp.login(this.user, this.password == null ? null : this.password.toCharArray());
/*     */     } catch (FtpProtocolException localFtpProtocolException2) {
/* 306 */       this.ftp.close();
/*     */ 
/* 308 */       throw new FtpLoginException("Invalid username/password");
/*     */     }
/* 310 */     this.connected = true;
/*     */   }
/*     */ 
/*     */   private void decodePath(String paramString)
/*     */   {
/* 318 */     int i = paramString.indexOf(";type=");
/* 319 */     if (i >= 0) {
/* 320 */       String str = paramString.substring(i + 6, paramString.length());
/* 321 */       if ("i".equalsIgnoreCase(str)) {
/* 322 */         this.type = 2;
/*     */       }
/* 324 */       if ("a".equalsIgnoreCase(str)) {
/* 325 */         this.type = 1;
/*     */       }
/* 327 */       if ("d".equalsIgnoreCase(str)) {
/* 328 */         this.type = 3;
/*     */       }
/* 330 */       paramString = paramString.substring(0, i);
/*     */     }
/* 332 */     if ((paramString != null) && (paramString.length() > 1) && (paramString.charAt(0) == '/'))
/*     */     {
/* 334 */       paramString = paramString.substring(1);
/*     */     }
/* 336 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 337 */       paramString = "./";
/*     */     }
/* 339 */     if (!paramString.endsWith("/")) {
/* 340 */       i = paramString.lastIndexOf('/');
/* 341 */       if (i > 0) {
/* 342 */         this.filename = paramString.substring(i + 1, paramString.length());
/* 343 */         this.filename = ParseUtil.decode(this.filename);
/* 344 */         this.pathname = paramString.substring(0, i);
/*     */       } else {
/* 346 */         this.filename = ParseUtil.decode(paramString);
/* 347 */         this.pathname = null;
/*     */       }
/*     */     } else {
/* 350 */       this.pathname = paramString.substring(0, paramString.length() - 1);
/* 351 */       this.filename = null;
/*     */     }
/* 353 */     if (this.pathname != null)
/* 354 */       this.fullpath = (this.pathname + "/" + (this.filename != null ? this.filename : ""));
/*     */     else
/* 356 */       this.fullpath = this.filename;
/*     */   }
/*     */ 
/*     */   private void cd(String paramString)
/*     */     throws FtpProtocolException, IOException
/*     */   {
/* 367 */     if ((paramString == null) || (paramString.isEmpty())) {
/* 368 */       return;
/*     */     }
/* 370 */     if (paramString.indexOf('/') == -1) {
/* 371 */       this.ftp.changeDirectory(ParseUtil.decode(paramString));
/* 372 */       return;
/*     */     }
/*     */ 
/* 375 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "/");
/* 376 */     while (localStringTokenizer.hasMoreTokens())
/* 377 */       this.ftp.changeDirectory(ParseUtil.decode(localStringTokenizer.nextToken()));
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 392 */     if (!this.connected) {
/* 393 */       connect();
/*     */     }
/*     */ 
/* 396 */     if (this.http != null) {
/* 397 */       return this.http.getInputStream();
/*     */     }
/*     */ 
/* 400 */     if (this.os != null) {
/* 401 */       throw new IOException("Already opened for output");
/*     */     }
/*     */ 
/* 404 */     if (this.is != null) {
/* 405 */       return this.is;
/*     */     }
/*     */ 
/* 408 */     MessageHeader localMessageHeader = new MessageHeader();
/*     */ 
/* 410 */     int i = 0;
/*     */     try {
/* 412 */       decodePath(this.url.getPath());
/* 413 */       if ((this.filename == null) || (this.type == 3)) {
/* 414 */         this.ftp.setAsciiType();
/* 415 */         cd(this.pathname);
/* 416 */         if (this.filename == null)
/* 417 */           this.is = new FtpInputStream(this.ftp, this.ftp.list(null));
/*     */         else
/* 419 */           this.is = new FtpInputStream(this.ftp, this.ftp.nameList(this.filename));
/*     */       }
/*     */       else {
/* 422 */         if (this.type == 1)
/* 423 */           this.ftp.setAsciiType();
/*     */         else {
/* 425 */           this.ftp.setBinaryType();
/*     */         }
/* 427 */         cd(this.pathname);
/* 428 */         this.is = new FtpInputStream(this.ftp, this.ftp.getFileStream(this.filename));
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 434 */         long l = this.ftp.getLastTransferSize();
/* 435 */         localMessageHeader.add("content-length", Long.toString(l));
/* 436 */         if (l > 0L)
/*     */         {
/* 442 */           boolean bool = ProgressMonitor.getDefault().shouldMeterInput(this.url, "GET");
/* 443 */           ProgressSource localProgressSource = null;
/*     */ 
/* 445 */           if (bool) {
/* 446 */             localProgressSource = new ProgressSource(this.url, "GET", l);
/* 447 */             localProgressSource.beginTracking();
/*     */           }
/*     */ 
/* 450 */           this.is = new MeteredStream(this.is, localProgressSource, l);
/*     */         }
/*     */       } catch (Exception localException) {
/* 453 */         localException.printStackTrace();
/*     */       }
/*     */ 
/* 458 */       if (i != 0) {
/* 459 */         localMessageHeader.add("content-type", "text/plain");
/* 460 */         localMessageHeader.add("access-type", "directory");
/*     */       } else {
/* 462 */         localMessageHeader.add("access-type", "file");
/* 463 */         String str = guessContentTypeFromName(this.fullpath);
/* 464 */         if ((str == null) && (this.is.markSupported())) {
/* 465 */           str = guessContentTypeFromStream(this.is);
/*     */         }
/* 467 */         if (str != null)
/* 468 */           localMessageHeader.add("content-type", str);
/*     */       }
/*     */     }
/*     */     catch (FileNotFoundException localFileNotFoundException) {
/*     */       try {
/* 473 */         cd(this.fullpath);
/*     */ 
/* 477 */         this.ftp.setAsciiType();
/*     */ 
/* 479 */         this.is = new FtpInputStream(this.ftp, this.ftp.list(null));
/* 480 */         localMessageHeader.add("content-type", "text/plain");
/* 481 */         localMessageHeader.add("access-type", "directory");
/*     */       } catch (IOException localIOException) {
/* 483 */         throw new FileNotFoundException(this.fullpath);
/*     */       } catch (FtpProtocolException localFtpProtocolException2) {
/* 485 */         throw new FileNotFoundException(this.fullpath);
/*     */       }
/*     */     } catch (FtpProtocolException localFtpProtocolException1) {
/* 488 */       throw new IOException(localFtpProtocolException1);
/*     */     }
/* 490 */     setProperties(localMessageHeader);
/* 491 */     return this.is;
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 506 */     if (!this.connected) {
/* 507 */       connect();
/*     */     }
/*     */ 
/* 510 */     if (this.http != null) {
/* 511 */       OutputStream localOutputStream = this.http.getOutputStream();
/*     */ 
/* 514 */       this.http.getInputStream();
/* 515 */       return localOutputStream;
/*     */     }
/*     */ 
/* 518 */     if (this.is != null) {
/* 519 */       throw new IOException("Already opened for input");
/*     */     }
/*     */ 
/* 522 */     if (this.os != null) {
/* 523 */       return this.os;
/*     */     }
/*     */ 
/* 526 */     decodePath(this.url.getPath());
/* 527 */     if ((this.filename == null) || (this.filename.length() == 0))
/* 528 */       throw new IOException("illegal filename for a PUT");
/*     */     try
/*     */     {
/* 531 */       if (this.pathname != null) {
/* 532 */         cd(this.pathname);
/*     */       }
/* 534 */       if (this.type == 1)
/* 535 */         this.ftp.setAsciiType();
/*     */       else {
/* 537 */         this.ftp.setBinaryType();
/*     */       }
/* 539 */       this.os = new FtpOutputStream(this.ftp, this.ftp.putFileStream(this.filename, false));
/*     */     } catch (FtpProtocolException localFtpProtocolException) {
/* 541 */       throw new IOException(localFtpProtocolException);
/*     */     }
/* 543 */     return this.os;
/*     */   }
/*     */ 
/*     */   String guessContentTypeFromFilename(String paramString) {
/* 547 */     return guessContentTypeFromName(paramString);
/*     */   }
/*     */ 
/*     */   public Permission getPermission()
/*     */   {
/* 557 */     if (this.permission == null) {
/* 558 */       int i = this.url.getPort();
/* 559 */       i = i < 0 ? FtpClient.defaultPort() : i;
/* 560 */       String str = this.host + ":" + i;
/* 561 */       this.permission = new SocketPermission(str, "connect");
/*     */     }
/* 563 */     return this.permission;
/*     */   }
/*     */ 
/*     */   public void setRequestProperty(String paramString1, String paramString2)
/*     */   {
/* 578 */     super.setRequestProperty(paramString1, paramString2);
/* 579 */     if ("type".equals(paramString1))
/* 580 */       if ("i".equalsIgnoreCase(paramString2))
/* 581 */         this.type = 2;
/* 582 */       else if ("a".equalsIgnoreCase(paramString2))
/* 583 */         this.type = 1;
/* 584 */       else if ("d".equalsIgnoreCase(paramString2))
/* 585 */         this.type = 3;
/*     */       else
/* 587 */         throw new IllegalArgumentException("Value of '" + paramString1 + "' request property was '" + paramString2 + "' when it must be either 'i', 'a' or 'd'");
/*     */   }
/*     */ 
/*     */   public String getRequestProperty(String paramString)
/*     */   {
/* 607 */     String str = super.getRequestProperty(paramString);
/*     */ 
/* 609 */     if ((str == null) && 
/* 610 */       ("type".equals(paramString))) {
/* 611 */       str = this.type == 3 ? "d" : this.type == 1 ? "a" : "i";
/*     */     }
/*     */ 
/* 615 */     return str;
/*     */   }
/*     */ 
/*     */   public void setConnectTimeout(int paramInt)
/*     */   {
/* 620 */     if (paramInt < 0) {
/* 621 */       throw new IllegalArgumentException("timeouts can't be negative");
/*     */     }
/* 623 */     this.connectTimeout = paramInt;
/*     */   }
/*     */ 
/*     */   public int getConnectTimeout()
/*     */   {
/* 628 */     return this.connectTimeout < 0 ? 0 : this.connectTimeout;
/*     */   }
/*     */ 
/*     */   public void setReadTimeout(int paramInt)
/*     */   {
/* 633 */     if (paramInt < 0) {
/* 634 */       throw new IllegalArgumentException("timeouts can't be negative");
/*     */     }
/* 636 */     this.readTimeout = paramInt;
/*     */   }
/*     */ 
/*     */   public int getReadTimeout()
/*     */   {
/* 641 */     return this.readTimeout < 0 ? 0 : this.readTimeout;
/*     */   }
/*     */ 
/*     */   protected class FtpInputStream extends FilterInputStream
/*     */   {
/*     */     FtpClient ftp;
/*     */ 
/*     */     FtpInputStream(FtpClient paramInputStream, InputStream arg3)
/*     */     {
/* 122 */       super();
/* 123 */       this.ftp = paramInputStream;
/*     */     }
/*     */ 
/*     */     public void close() throws IOException
/*     */     {
/* 128 */       super.close();
/* 129 */       if (this.ftp != null)
/* 130 */         this.ftp.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class FtpOutputStream extends FilterOutputStream
/*     */   {
/*     */     FtpClient ftp;
/*     */ 
/*     */     FtpOutputStream(FtpClient paramOutputStream, OutputStream arg3)
/*     */     {
/* 145 */       super();
/* 146 */       this.ftp = paramOutputStream;
/*     */     }
/*     */ 
/*     */     public void close() throws IOException
/*     */     {
/* 151 */       super.close();
/* 152 */       if (this.ftp != null)
/* 153 */         this.ftp.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.ftp.FtpURLConnection
 * JD-Core Version:    0.6.2
 */