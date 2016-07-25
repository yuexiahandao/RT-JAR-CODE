/*     */ package com.sun.xml.internal.ws.transport.http.client;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.client.ClientTransportException;
/*     */ import com.sun.xml.internal.ws.resources.ClientMessages;
/*     */ import com.sun.xml.internal.ws.transport.Headers;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ final class HttpClientTransport
/*     */ {
/*  65 */   private static final byte[] THROW_AWAY_BUFFER = new byte[8192];
/*     */   int statusCode;
/*     */   String statusMessage;
/*     */   int contentLength;
/*     */   private final Map<String, List<String>> reqHeaders;
/*  80 */   private Map<String, List<String>> respHeaders = null;
/*     */   private OutputStream outputStream;
/*     */   private boolean https;
/*  84 */   private HttpURLConnection httpConnection = null;
/*     */   private final EndpointAddress endpoint;
/*     */   private final Packet context;
/*     */   private final Integer chunkSize;
/*     */ 
/*     */   public HttpClientTransport(@NotNull Packet packet, @NotNull Map<String, List<String>> reqHeaders)
/*     */   {
/*  91 */     this.endpoint = packet.endpointAddress;
/*  92 */     this.context = packet;
/*  93 */     this.reqHeaders = reqHeaders;
/*  94 */     this.chunkSize = ((Integer)this.context.invocationProperties.get("com.sun.xml.internal.ws.transport.http.client.streaming.chunk.size"));
/*     */   }
/*     */ 
/*     */   OutputStream getOutput()
/*     */   {
/*     */     try
/*     */     {
/* 102 */       createHttpConnection();
/*     */ 
/* 104 */       if (requiresOutputStream()) {
/* 105 */         this.outputStream = this.httpConnection.getOutputStream();
/* 106 */         if (this.chunkSize != null) {
/* 107 */           this.outputStream = new WSChunkedOuputStream(this.outputStream, this.chunkSize.intValue());
/*     */         }
/* 109 */         List contentEncoding = (List)this.reqHeaders.get("Content-Encoding");
/*     */ 
/* 111 */         if ((contentEncoding != null) && (((String)contentEncoding.get(0)).contains("gzip"))) {
/* 112 */           this.outputStream = new GZIPOutputStream(this.outputStream);
/*     */         }
/*     */       }
/* 115 */       this.httpConnection.connect();
/*     */     } catch (Exception ex) {
/* 117 */       throw new ClientTransportException(ClientMessages.localizableHTTP_CLIENT_FAILED(ex), ex);
/*     */     }
/*     */ 
/* 121 */     return this.outputStream;
/*     */   }
/*     */ 
/*     */   void closeOutput() throws IOException {
/* 125 */     if (this.outputStream != null) {
/* 126 */       this.outputStream.close();
/* 127 */       this.outputStream = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   InputStream getInput()
/*     */   {
/*     */     InputStream in;
/*     */     try
/*     */     {
/* 139 */       in = readResponse();
/* 140 */       if (in != null) {
/* 141 */         String contentEncoding = this.httpConnection.getContentEncoding();
/* 142 */         if ((contentEncoding != null) && (contentEncoding.contains("gzip")))
/* 143 */           in = new GZIPInputStream(in);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 147 */       throw new ClientTransportException(ClientMessages.localizableHTTP_STATUS_CODE(Integer.valueOf(this.statusCode), this.statusMessage), e);
/*     */     }
/* 149 */     return in;
/*     */   }
/*     */ 
/*     */   public Map<String, List<String>> getHeaders() {
/* 153 */     if (this.respHeaders != null) {
/* 154 */       return this.respHeaders;
/*     */     }
/* 156 */     this.respHeaders = new Headers();
/* 157 */     this.respHeaders.putAll(this.httpConnection.getHeaderFields());
/* 158 */     return this.respHeaders;
/*     */   }
/*     */   @Nullable
/*     */   protected InputStream readResponse() {
/*     */     InputStream is;
/*     */     try {
/* 164 */       is = this.httpConnection.getInputStream();
/*     */     } catch (IOException ioe) {
/* 166 */       is = this.httpConnection.getErrorStream();
/*     */     }
/* 168 */     if (is == null) {
/* 169 */       return is;
/*     */     }
/*     */ 
/* 174 */     final InputStream temp = is;
/* 175 */     return new FilterInputStream(temp)
/*     */     {
/*     */       boolean closed;
/*     */ 
/*     */       public void close() throws IOException
/*     */       {
/* 181 */         if (!this.closed) {
/* 182 */           this.closed = true;
/* 183 */           while (temp.read(HttpClientTransport.THROW_AWAY_BUFFER) != -1);
/* 184 */           super.close();
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   protected void readResponseCodeAndMessage() {
/*     */     try {
/* 192 */       this.statusCode = this.httpConnection.getResponseCode();
/* 193 */       this.statusMessage = this.httpConnection.getResponseMessage();
/* 194 */       this.contentLength = this.httpConnection.getContentLength();
/*     */     } catch (IOException ioe) {
/* 196 */       throw new WebServiceException(ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void createHttpConnection() throws IOException
/*     */   {
/* 202 */     this.httpConnection = ((HttpURLConnection)this.endpoint.openConnection());
/* 203 */     String scheme = this.endpoint.getURI().getScheme();
/* 204 */     if (scheme.equals("https")) {
/* 205 */       this.https = true;
/*     */     }
/* 207 */     if ((this.httpConnection instanceof HttpsURLConnection)) {
/* 208 */       this.https = true;
/*     */ 
/* 210 */       boolean verification = false;
/*     */ 
/* 215 */       String verificationProperty = (String)this.context.invocationProperties.get("com.sun.xml.internal.ws.client.http.HostnameVerificationProperty");
/*     */ 
/* 217 */       if ((verificationProperty != null) && 
/* 218 */         (verificationProperty.equalsIgnoreCase("true"))) {
/* 219 */         verification = true;
/*     */       }
/*     */ 
/* 222 */       if (verification) {
/* 223 */         ((HttpsURLConnection)this.httpConnection).setHostnameVerifier(new HttpClientVerifier(null));
/*     */       }
/*     */ 
/* 227 */       HostnameVerifier verifier = (HostnameVerifier)this.context.invocationProperties.get("com.sun.xml.internal.ws.transport.https.client.hostname.verifier");
/*     */ 
/* 229 */       if (verifier != null) {
/* 230 */         ((HttpsURLConnection)this.httpConnection).setHostnameVerifier(verifier);
/*     */       }
/*     */ 
/* 234 */       SSLSocketFactory sslSocketFactory = (SSLSocketFactory)this.context.invocationProperties.get("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory");
/*     */ 
/* 236 */       if (sslSocketFactory != null) {
/* 237 */         ((HttpsURLConnection)this.httpConnection).setSSLSocketFactory(sslSocketFactory);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 244 */     this.httpConnection.setAllowUserInteraction(true);
/*     */ 
/* 247 */     this.httpConnection.setDoOutput(true);
/* 248 */     this.httpConnection.setDoInput(true);
/*     */ 
/* 250 */     String requestMethod = (String)this.context.invocationProperties.get("javax.xml.ws.http.request.method");
/* 251 */     String method = requestMethod != null ? requestMethod : "POST";
/* 252 */     this.httpConnection.setRequestMethod(method);
/*     */ 
/* 263 */     Integer reqTimeout = (Integer)this.context.invocationProperties.get("com.sun.xml.internal.ws.request.timeout");
/* 264 */     if (reqTimeout != null) {
/* 265 */       this.httpConnection.setReadTimeout(reqTimeout.intValue());
/*     */     }
/*     */ 
/* 268 */     Integer connectTimeout = (Integer)this.context.invocationProperties.get("com.sun.xml.internal.ws.connect.timeout");
/* 269 */     if (connectTimeout != null) {
/* 270 */       this.httpConnection.setConnectTimeout(connectTimeout.intValue());
/*     */     }
/*     */ 
/* 273 */     Integer chunkSize = (Integer)this.context.invocationProperties.get("com.sun.xml.internal.ws.transport.http.client.streaming.chunk.size");
/* 274 */     if (chunkSize != null) {
/* 275 */       this.httpConnection.setChunkedStreamingMode(chunkSize.intValue());
/*     */     }
/*     */ 
/* 279 */     for (Iterator i$ = this.reqHeaders.entrySet().iterator(); i$.hasNext(); ) { entry = (Map.Entry)i$.next();
/* 280 */       for (String value : (List)entry.getValue())
/* 281 */         this.httpConnection.addRequestProperty((String)entry.getKey(), value); }
/*     */     Map.Entry entry;
/*     */   }
/*     */ 
/*     */   boolean isSecure()
/*     */   {
/* 287 */     return this.https;
/*     */   }
/*     */ 
/*     */   private boolean requiresOutputStream() {
/* 291 */     return (!this.httpConnection.getRequestMethod().equalsIgnoreCase("GET")) && (!this.httpConnection.getRequestMethod().equalsIgnoreCase("HEAD")) && (!this.httpConnection.getRequestMethod().equalsIgnoreCase("DELETE"));
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   String getContentType()
/*     */   {
/* 297 */     return this.httpConnection.getContentType();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  70 */       JAXBContext.newInstance(new Class[0]).createUnmarshaller();
/*     */     }
/*     */     catch (JAXBException je)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class HttpClientVerifier
/*     */     implements HostnameVerifier
/*     */   {
/*     */     public boolean verify(String s, SSLSession sslSession)
/*     */     {
/* 304 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class WSChunkedOuputStream extends FilterOutputStream
/*     */   {
/*     */     final int chunkSize;
/*     */ 
/*     */     WSChunkedOuputStream(OutputStream actual, int chunkSize)
/*     */     {
/* 318 */       super();
/* 319 */       this.chunkSize = chunkSize;
/*     */     }
/*     */ 
/*     */     public void write(byte[] b, int off, int len) throws IOException
/*     */     {
/* 324 */       while (len > 0) {
/* 325 */         int sent = len > this.chunkSize ? this.chunkSize : len;
/* 326 */         this.out.write(b, off, sent);
/* 327 */         len -= sent;
/* 328 */         off += sent;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.client.HttpClientTransport
 * JD-Core Version:    0.6.2
 */