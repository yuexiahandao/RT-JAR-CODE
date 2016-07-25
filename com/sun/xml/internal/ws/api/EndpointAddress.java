/*     */ package com.sun.xml.internal.ws.api;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Proxy;
/*     */ import java.net.ProxySelector;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public final class EndpointAddress
/*     */ {
/*     */ 
/*     */   @Nullable
/*     */   private URL url;
/*     */   private final URI uri;
/*     */   private final String stringForm;
/*     */   private volatile boolean dontUseProxyMethod;
/*     */   private Proxy proxy;
/*     */ 
/*     */   public EndpointAddress(URI uri)
/*     */   {
/* 101 */     this.uri = uri;
/* 102 */     this.stringForm = uri.toString();
/*     */     try {
/* 104 */       this.url = uri.toURL();
/* 105 */       this.proxy = chooseProxy();
/*     */     }
/*     */     catch (MalformedURLException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public EndpointAddress(String url)
/*     */     throws URISyntaxException
/*     */   {
/* 116 */     this.uri = new URI(url);
/* 117 */     this.stringForm = url;
/*     */     try {
/* 119 */       this.url = new URL(url);
/* 120 */       this.proxy = chooseProxy();
/*     */     }
/*     */     catch (MalformedURLException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static EndpointAddress create(String url)
/*     */   {
/*     */     try
/*     */     {
/* 132 */       return new EndpointAddress(url);
/*     */     } catch (URISyntaxException e) {
/* 134 */       throw new WebServiceException("Illegal endpoint address: " + url, e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Proxy chooseProxy() {
/* 139 */     ProxySelector sel = (ProxySelector)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public ProxySelector run()
/*     */       {
/* 143 */         return ProxySelector.getDefault();
/*     */       }
/*     */     });
/* 147 */     if (sel == null) {
/* 148 */       return Proxy.NO_PROXY;
/*     */     }
/*     */ 
/* 151 */     if (!sel.getClass().getName().equals("sun.net.spi.DefaultProxySelector"))
/*     */     {
/* 153 */       return null;
/*     */     }
/* 155 */     Iterator it = sel.select(this.uri).iterator();
/* 156 */     if (it.hasNext()) {
/* 157 */       return (Proxy)it.next();
/*     */     }
/* 159 */     return Proxy.NO_PROXY;
/*     */   }
/*     */ 
/*     */   public URL getURL()
/*     */   {
/* 169 */     return this.url;
/*     */   }
/*     */ 
/*     */   public URI getURI()
/*     */   {
/* 179 */     return this.uri;
/*     */   }
/*     */ 
/*     */   public URLConnection openConnection()
/*     */     throws IOException
/*     */   {
/* 196 */     assert (this.url != null) : (this.uri + " doesn't have the corresponding URL");
/* 197 */     if (this.url == null) {
/* 198 */       throw new WebServiceException("URI=" + this.uri + " doesn't have the corresponding URL");
/*     */     }
/* 200 */     if ((this.proxy != null) && (!this.dontUseProxyMethod)) {
/*     */       try {
/* 202 */         return this.url.openConnection(this.proxy);
/*     */       }
/*     */       catch (UnsupportedOperationException e)
/*     */       {
/* 207 */         this.dontUseProxyMethod = true;
/*     */       }
/*     */     }
/* 210 */     return this.url.openConnection();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 214 */     return this.stringForm;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.EndpointAddress
 * JD-Core Version:    0.6.2
 */