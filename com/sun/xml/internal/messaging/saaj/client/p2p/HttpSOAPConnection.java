/*     */ package com.sun.xml.internal.messaging.saaj.client.p2p;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import com.sun.xml.internal.messaging.saaj.util.Base64;
/*     */ import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
/*     */ import com.sun.xml.internal.messaging.saaj.util.JaxmURI;
/*     */ import com.sun.xml.internal.messaging.saaj.util.ParseUtil;
/*     */ import com.sun.xml.internal.messaging.saaj.util.SAAJUtil;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import java.util.Iterator;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.soap.MessageFactory;
/*     */ import javax.xml.soap.MimeHeader;
/*     */ import javax.xml.soap.MimeHeaders;
/*     */ import javax.xml.soap.SOAPConnection;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ 
/*     */ class HttpSOAPConnection extends SOAPConnection
/*     */ {
/*  52 */   public static final String vmVendor = SAAJUtil.getSystemProperty("java.vendor.url");
/*     */   private static final String sunVmVendor = "http://java.sun.com/";
/*     */   private static final String ibmVmVendor = "http://www.ibm.com/";
/*  55 */   private static final boolean isSunVM = "http://java.sun.com/".equals(vmVendor);
/*  56 */   private static final boolean isIBMVM = "http://www.ibm.com/".equals(vmVendor);
/*     */   private static final String JAXM_URLENDPOINT = "javax.xml.messaging.URLEndpoint";
/*  59 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.client.p2p", "com.sun.xml.internal.messaging.saaj.client.p2p.LocalStrings");
/*     */ 
/*  64 */   MessageFactory messageFactory = null;
/*     */ 
/*  66 */   boolean closed = false;
/*     */   private static final String SSL_PKG;
/*     */   private static final String SSL_PROVIDER;
/*     */   private static final int dL = 0;
/*     */ 
/*     */   public HttpSOAPConnection()
/*     */     throws SOAPException
/*     */   {
/*     */     try
/*     */     {
/*  71 */       this.messageFactory = MessageFactory.newInstance("Dynamic Protocol");
/*     */     }
/*     */     catch (NoSuchMethodError ex) {
/*  74 */       this.messageFactory = MessageFactory.newInstance();
/*     */     } catch (Exception ex) {
/*  76 */       log.log(Level.SEVERE, "SAAJ0001.p2p.cannot.create.msg.factory", ex);
/*  77 */       throw new SOAPExceptionImpl("Unable to create message factory", ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() throws SOAPException {
/*  82 */     if (this.closed) {
/*  83 */       log.severe("SAAJ0002.p2p.close.already.closed.conn");
/*  84 */       throw new SOAPExceptionImpl("Connection already closed");
/*     */     }
/*     */ 
/*  87 */     this.messageFactory = null;
/*  88 */     this.closed = true;
/*     */   }
/*     */ 
/*     */   public SOAPMessage call(SOAPMessage message, Object endPoint) throws SOAPException
/*     */   {
/*  93 */     if (this.closed) {
/*  94 */       log.severe("SAAJ0003.p2p.call.already.closed.conn");
/*  95 */       throw new SOAPExceptionImpl("Connection is closed");
/*     */     }
/*     */ 
/*  98 */     Class urlEndpointClass = null;
/*  99 */     ClassLoader loader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 101 */       if (loader != null)
/* 102 */         urlEndpointClass = loader.loadClass("javax.xml.messaging.URLEndpoint");
/*     */       else
/* 104 */         urlEndpointClass = Class.forName("javax.xml.messaging.URLEndpoint");
/*     */     }
/*     */     catch (ClassNotFoundException ex)
/*     */     {
/* 108 */       log.finest("SAAJ0090.p2p.endpoint.available.only.for.JAXM");
/*     */     }
/*     */ 
/* 111 */     if ((urlEndpointClass != null) && 
/* 112 */       (urlEndpointClass.isInstance(endPoint))) {
/* 113 */       String url = null;
/*     */       try
/*     */       {
/* 116 */         Method m = urlEndpointClass.getMethod("getURL", (Class[])null);
/* 117 */         url = (String)m.invoke(endPoint, (Object[])null);
/*     */       }
/*     */       catch (Exception ex) {
/* 120 */         log.log(Level.SEVERE, "SAAJ0004.p2p.internal.err", ex);
/* 121 */         throw new SOAPExceptionImpl("Internal error: " + ex.getMessage());
/*     */       }
/*     */       try
/*     */       {
/* 125 */         endPoint = new URL(url);
/*     */       } catch (MalformedURLException mex) {
/* 127 */         log.log(Level.SEVERE, "SAAJ0005.p2p.", mex);
/* 128 */         throw new SOAPExceptionImpl("Bad URL: " + mex.getMessage());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 133 */     if ((endPoint instanceof String)) {
/*     */       try {
/* 135 */         endPoint = new URL((String)endPoint);
/*     */       } catch (MalformedURLException mex) {
/* 137 */         log.log(Level.SEVERE, "SAAJ0006.p2p.bad.URL", mex);
/* 138 */         throw new SOAPExceptionImpl("Bad URL: " + mex.getMessage());
/*     */       }
/*     */     }
/*     */ 
/* 142 */     if ((endPoint instanceof URL))
/*     */       try {
/* 144 */         return post(message, (URL)endPoint);
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 148 */         throw new SOAPExceptionImpl(ex);
/*     */       }
/* 150 */     log.severe("SAAJ0007.p2p.bad.endPoint.type");
/* 151 */     throw new SOAPExceptionImpl("Bad endPoint type " + endPoint);
/*     */   }
/*     */ 
/*     */   SOAPMessage post(SOAPMessage message, URL endPoint) throws SOAPException
/*     */   {
/* 156 */     boolean isFailure = false;
/*     */ 
/* 158 */     URL url = null;
/* 159 */     HttpURLConnection httpConnection = null;
/*     */ 
/* 161 */     int responseCode = 0;
/*     */     try {
/* 163 */       if (endPoint.getProtocol().equals("https"))
/*     */       {
/* 165 */         initHttps();
/*     */       }
/* 167 */       JaxmURI uri = new JaxmURI(endPoint.toString());
/* 168 */       String userInfo = uri.getUserinfo();
/*     */ 
/* 170 */       url = endPoint;
/*     */ 
/* 177 */       if ((!url.getProtocol().equalsIgnoreCase("http")) && (!url.getProtocol().equalsIgnoreCase("https")))
/*     */       {
/* 179 */         log.severe("SAAJ0052.p2p.protocol.mustbe.http.or.https");
/* 180 */         throw new IllegalArgumentException("Protocol " + url.getProtocol() + " not supported in URL " + url);
/*     */       }
/*     */ 
/* 186 */       httpConnection = createConnection(url);
/*     */ 
/* 188 */       httpConnection.setRequestMethod("POST");
/*     */ 
/* 190 */       httpConnection.setDoOutput(true);
/* 191 */       httpConnection.setDoInput(true);
/* 192 */       httpConnection.setUseCaches(false);
/* 193 */       httpConnection.setInstanceFollowRedirects(true);
/*     */ 
/* 195 */       if (message.saveRequired()) {
/* 196 */         message.saveChanges();
/*     */       }
/* 198 */       MimeHeaders headers = message.getMimeHeaders();
/*     */ 
/* 200 */       Iterator it = headers.getAllHeaders();
/* 201 */       boolean hasAuth = false;
/* 202 */       while (it.hasNext()) {
/* 203 */         MimeHeader header = (MimeHeader)it.next();
/*     */ 
/* 205 */         String[] values = headers.getHeader(header.getName());
/* 206 */         if (values.length == 1) {
/* 207 */           httpConnection.setRequestProperty(header.getName(), header.getValue());
/*     */         }
/*     */         else
/*     */         {
/* 211 */           StringBuffer concat = new StringBuffer();
/* 212 */           int i = 0;
/* 213 */           while (i < values.length) {
/* 214 */             if (i != 0)
/* 215 */               concat.append(',');
/* 216 */             concat.append(values[i]);
/* 217 */             i++;
/*     */           }
/*     */ 
/* 220 */           httpConnection.setRequestProperty(header.getName(), concat.toString());
/*     */         }
/*     */ 
/* 225 */         if ("Authorization".equals(header.getName())) {
/* 226 */           hasAuth = true;
/* 227 */           log.fine("SAAJ0091.p2p.https.auth.in.POST.true");
/*     */         }
/*     */       }
/*     */ 
/* 231 */       if ((!hasAuth) && (userInfo != null)) {
/* 232 */         initAuthUserInfo(httpConnection, userInfo);
/*     */       }
/*     */ 
/* 235 */       OutputStream out = httpConnection.getOutputStream();
/* 236 */       message.writeTo(out);
/*     */ 
/* 238 */       out.flush();
/* 239 */       out.close();
/*     */ 
/* 241 */       httpConnection.connect();
/*     */       try
/*     */       {
/* 245 */         responseCode = httpConnection.getResponseCode();
/*     */ 
/* 248 */         if (responseCode == 500) {
/* 249 */           isFailure = true;
/*     */         }
/* 253 */         else if (responseCode / 100 != 2) {
/* 254 */           log.log(Level.SEVERE, "SAAJ0008.p2p.bad.response", new String[] { httpConnection.getResponseMessage() });
/*     */ 
/* 257 */           throw new SOAPExceptionImpl("Bad response: (" + responseCode + httpConnection.getResponseMessage());
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 265 */         responseCode = httpConnection.getResponseCode();
/* 266 */         if (responseCode == 500)
/* 267 */           isFailure = true;
/*     */         else {
/* 269 */           throw e;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (SOAPException ex)
/*     */     {
/* 275 */       throw ex;
/*     */     } catch (Exception ex) {
/* 277 */       log.severe("SAAJ0009.p2p.msg.send.failed");
/* 278 */       throw new SOAPExceptionImpl("Message send failed", ex);
/*     */     }
/*     */ 
/* 281 */     SOAPMessage response = null;
/* 282 */     if ((responseCode == 200) || (isFailure)) {
/*     */       try {
/* 284 */         MimeHeaders headers = new MimeHeaders();
/*     */ 
/* 290 */         int i = 1;
/*     */         while (true)
/*     */         {
/* 293 */           String key = httpConnection.getHeaderFieldKey(i);
/* 294 */           String value = httpConnection.getHeaderField(i);
/*     */ 
/* 296 */           if ((key == null) && (value == null)) {
/*     */             break;
/*     */           }
/* 299 */           if (key != null) {
/* 300 */             StringTokenizer values = new StringTokenizer(value, ",");
/*     */ 
/* 302 */             while (values.hasMoreTokens())
/* 303 */               headers.addHeader(key, values.nextToken().trim());
/*     */           }
/* 305 */           i++;
/*     */         }
/*     */ 
/* 308 */         InputStream httpIn = isFailure ? httpConnection.getErrorStream() : httpConnection.getInputStream();
/*     */ 
/* 313 */         byte[] bytes = readFully(httpIn);
/*     */ 
/* 315 */         int length = httpConnection.getContentLength() == -1 ? bytes.length : httpConnection.getContentLength();
/*     */ 
/* 322 */         if (length == 0) {
/* 323 */           response = null;
/* 324 */           log.warning("SAAJ0014.p2p.content.zero");
/*     */         } else {
/* 326 */           ByteInputStream in = new ByteInputStream(bytes, length);
/* 327 */           response = this.messageFactory.createMessage(headers, in);
/*     */         }
/*     */ 
/* 330 */         httpIn.close();
/* 331 */         httpConnection.disconnect();
/*     */       }
/*     */       catch (SOAPException ex) {
/* 334 */         throw ex;
/*     */       } catch (Exception ex) {
/* 336 */         log.log(Level.SEVERE, "SAAJ0010.p2p.cannot.read.resp", ex);
/* 337 */         throw new SOAPExceptionImpl("Unable to read response: " + ex.getMessage());
/*     */       }
/*     */     }
/*     */ 
/* 341 */     return response;
/*     */   }
/*     */ 
/*     */   public SOAPMessage get(Object endPoint)
/*     */     throws SOAPException
/*     */   {
/* 348 */     if (this.closed) {
/* 349 */       log.severe("SAAJ0011.p2p.get.already.closed.conn");
/* 350 */       throw new SOAPExceptionImpl("Connection is closed");
/*     */     }
/* 352 */     Class urlEndpointClass = null;
/*     */     try
/*     */     {
/* 355 */       urlEndpointClass = Class.forName("javax.xml.messaging.URLEndpoint");
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*     */     }
/* 360 */     if ((urlEndpointClass != null) && 
/* 361 */       (urlEndpointClass.isInstance(endPoint))) {
/* 362 */       String url = null;
/*     */       try
/*     */       {
/* 365 */         Method m = urlEndpointClass.getMethod("getURL", (Class[])null);
/* 366 */         url = (String)m.invoke(endPoint, (Object[])null);
/*     */       } catch (Exception ex) {
/* 368 */         log.severe("SAAJ0004.p2p.internal.err");
/* 369 */         throw new SOAPExceptionImpl("Internal error: " + ex.getMessage());
/*     */       }
/*     */       try
/*     */       {
/* 373 */         endPoint = new URL(url);
/*     */       } catch (MalformedURLException mex) {
/* 375 */         log.severe("SAAJ0005.p2p.");
/* 376 */         throw new SOAPExceptionImpl("Bad URL: " + mex.getMessage());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 381 */     if ((endPoint instanceof String)) {
/*     */       try {
/* 383 */         endPoint = new URL((String)endPoint);
/*     */       } catch (MalformedURLException mex) {
/* 385 */         log.severe("SAAJ0006.p2p.bad.URL");
/* 386 */         throw new SOAPExceptionImpl("Bad URL: " + mex.getMessage());
/*     */       }
/*     */     }
/*     */ 
/* 390 */     if ((endPoint instanceof URL))
/*     */       try {
/* 392 */         return doGet((URL)endPoint);
/*     */       }
/*     */       catch (Exception ex) {
/* 395 */         throw new SOAPExceptionImpl(ex);
/*     */       }
/* 397 */     throw new SOAPExceptionImpl("Bad endPoint type " + endPoint);
/*     */   }
/*     */ 
/*     */   SOAPMessage doGet(URL endPoint) throws SOAPException {
/* 401 */     boolean isFailure = false;
/*     */ 
/* 403 */     URL url = null;
/* 404 */     HttpURLConnection httpConnection = null;
/*     */ 
/* 406 */     int responseCode = 0;
/*     */     try
/*     */     {
/* 409 */       if (endPoint.getProtocol().equals("https")) {
/* 410 */         initHttps();
/*     */       }
/* 412 */       JaxmURI uri = new JaxmURI(endPoint.toString());
/* 413 */       String userInfo = uri.getUserinfo();
/*     */ 
/* 415 */       url = endPoint;
/*     */ 
/* 422 */       if ((!url.getProtocol().equalsIgnoreCase("http")) && (!url.getProtocol().equalsIgnoreCase("https")))
/*     */       {
/* 424 */         log.severe("SAAJ0052.p2p.protocol.mustbe.http.or.https");
/* 425 */         throw new IllegalArgumentException("Protocol " + url.getProtocol() + " not supported in URL " + url);
/*     */       }
/*     */ 
/* 431 */       httpConnection = createConnection(url);
/*     */ 
/* 433 */       httpConnection.setRequestMethod("GET");
/*     */ 
/* 435 */       httpConnection.setDoOutput(true);
/* 436 */       httpConnection.setDoInput(true);
/* 437 */       httpConnection.setUseCaches(false);
/* 438 */       HttpURLConnection.setFollowRedirects(true);
/*     */ 
/* 440 */       httpConnection.connect();
/*     */       try
/*     */       {
/* 444 */         responseCode = httpConnection.getResponseCode();
/*     */ 
/* 447 */         if (responseCode == 500) {
/* 448 */           isFailure = true;
/* 449 */         } else if (responseCode / 100 != 2) {
/* 450 */           log.log(Level.SEVERE, "SAAJ0008.p2p.bad.response", new String[] { httpConnection.getResponseMessage() });
/*     */ 
/* 453 */           throw new SOAPExceptionImpl("Bad response: (" + responseCode + httpConnection.getResponseMessage());
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 461 */         responseCode = httpConnection.getResponseCode();
/* 462 */         if (responseCode == 500)
/* 463 */           isFailure = true;
/*     */         else {
/* 465 */           throw e;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (SOAPException ex)
/*     */     {
/* 471 */       throw ex;
/*     */     } catch (Exception ex) {
/* 473 */       log.severe("SAAJ0012.p2p.get.failed");
/* 474 */       throw new SOAPExceptionImpl("Get failed", ex);
/*     */     }
/*     */ 
/* 477 */     SOAPMessage response = null;
/* 478 */     if ((responseCode == 200) || (isFailure)) {
/*     */       try {
/* 480 */         MimeHeaders headers = new MimeHeaders();
/*     */ 
/* 486 */         int i = 1;
/*     */         while (true)
/*     */         {
/* 489 */           String key = httpConnection.getHeaderFieldKey(i);
/* 490 */           String value = httpConnection.getHeaderField(i);
/*     */ 
/* 492 */           if ((key == null) && (value == null)) {
/*     */             break;
/*     */           }
/* 495 */           if (key != null) {
/* 496 */             StringTokenizer values = new StringTokenizer(value, ",");
/*     */ 
/* 498 */             while (values.hasMoreTokens())
/* 499 */               headers.addHeader(key, values.nextToken().trim());
/*     */           }
/* 501 */           i++;
/*     */         }
/*     */ 
/* 504 */         InputStream httpIn = isFailure ? httpConnection.getErrorStream() : httpConnection.getInputStream();
/*     */ 
/* 514 */         if ((httpIn == null) || (httpConnection.getContentLength() == 0) || (httpIn.available() == 0))
/*     */         {
/* 517 */           response = null;
/* 518 */           log.warning("SAAJ0014.p2p.content.zero");
/*     */         } else {
/* 520 */           response = this.messageFactory.createMessage(headers, httpIn);
/*     */         }
/*     */ 
/* 523 */         httpIn.close();
/* 524 */         httpConnection.disconnect();
/*     */       }
/*     */       catch (SOAPException ex) {
/* 527 */         throw ex;
/*     */       } catch (Exception ex) {
/* 529 */         log.log(Level.SEVERE, "SAAJ0010.p2p.cannot.read.resp", ex);
/*     */ 
/* 532 */         throw new SOAPExceptionImpl("Unable to read response: " + ex.getMessage());
/*     */       }
/*     */     }
/*     */ 
/* 536 */     return response;
/*     */   }
/*     */ 
/*     */   private byte[] readFully(InputStream istream) throws IOException {
/* 540 */     ByteArrayOutputStream bout = new ByteArrayOutputStream();
/* 541 */     byte[] buf = new byte[1024];
/* 542 */     int num = 0;
/*     */ 
/* 544 */     while ((num = istream.read(buf)) != -1) {
/* 545 */       bout.write(buf, 0, num);
/*     */     }
/*     */ 
/* 548 */     byte[] ret = bout.toByteArray();
/*     */ 
/* 550 */     return ret;
/*     */   }
/*     */ 
/*     */   private void initHttps()
/*     */   {
/* 572 */     String pkgs = SAAJUtil.getSystemProperty("java.protocol.handler.pkgs");
/* 573 */     log.log(Level.FINE, "SAAJ0053.p2p.providers", new String[] { pkgs });
/*     */ 
/* 577 */     if ((pkgs == null) || (pkgs.indexOf(SSL_PKG) < 0)) {
/* 578 */       if (pkgs == null)
/* 579 */         pkgs = SSL_PKG;
/*     */       else
/* 581 */         pkgs = pkgs + "|" + SSL_PKG;
/* 582 */       System.setProperty("java.protocol.handler.pkgs", pkgs);
/* 583 */       log.log(Level.FINE, "SAAJ0054.p2p.set.providers", new String[] { pkgs });
/*     */       try
/*     */       {
/* 587 */         Class c = Class.forName(SSL_PROVIDER);
/* 588 */         Provider p = (Provider)c.newInstance();
/* 589 */         Security.addProvider(p);
/* 590 */         log.log(Level.FINE, "SAAJ0055.p2p.added.ssl.provider", new String[] { SSL_PROVIDER });
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initAuthUserInfo(HttpURLConnection conn, String userInfo)
/*     */   {
/* 604 */     if (userInfo != null)
/*     */     {
/* 606 */       int delimiter = userInfo.indexOf(':');
/*     */       String password;
/*     */       String user;
/*     */       String password;
/* 607 */       if (delimiter == -1) {
/* 608 */         String user = ParseUtil.decode(userInfo);
/* 609 */         password = null;
/*     */       } else {
/* 611 */         user = ParseUtil.decode(userInfo.substring(0, delimiter++));
/* 612 */         password = ParseUtil.decode(userInfo.substring(delimiter));
/*     */       }
/*     */ 
/* 615 */       String plain = user + ":";
/* 616 */       byte[] nameBytes = plain.getBytes();
/* 617 */       byte[] passwdBytes = password.getBytes();
/*     */ 
/* 620 */       byte[] concat = new byte[nameBytes.length + passwdBytes.length];
/*     */ 
/* 622 */       System.arraycopy(nameBytes, 0, concat, 0, nameBytes.length);
/* 623 */       System.arraycopy(passwdBytes, 0, concat, nameBytes.length, passwdBytes.length);
/*     */ 
/* 629 */       String auth = "Basic " + new String(Base64.encode(concat));
/* 630 */       conn.setRequestProperty("Authorization", auth);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void d(String s)
/*     */   {
/* 638 */     log.log(Level.SEVERE, "SAAJ0013.p2p.HttpSOAPConnection", new String[] { s });
/*     */ 
/* 641 */     System.err.println("HttpSOAPConnection: " + s);
/*     */   }
/*     */ 
/*     */   private HttpURLConnection createConnection(URL endpoint) throws IOException
/*     */   {
/* 646 */     return (HttpURLConnection)endpoint.openConnection();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 560 */     if (isIBMVM) {
/* 561 */       SSL_PKG = "com.ibm.net.ssl.internal.www.protocol";
/* 562 */       SSL_PROVIDER = "com.ibm.net.ssl.internal.ssl.Provider";
/*     */     }
/*     */     else {
/* 565 */       SSL_PKG = "com.sun.net.ssl.internal.www.protocol";
/* 566 */       SSL_PROVIDER = "com.sun.net.ssl.internal.ssl.Provider";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.client.p2p.HttpSOAPConnection
 * JD-Core Version:    0.6.2
 */