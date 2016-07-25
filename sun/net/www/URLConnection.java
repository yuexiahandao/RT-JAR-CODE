/*     */ package sun.net.www;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract class URLConnection extends java.net.URLConnection
/*     */ {
/*     */   private String contentType;
/*  47 */   private int contentLength = -1;
/*     */   protected MessageHeader properties;
/* 242 */   private static HashMap<String, Void> proxiedHosts = new HashMap();
/*     */ 
/*     */   public URLConnection(URL paramURL)
/*     */   {
/*  57 */     super(paramURL);
/*  58 */     this.properties = new MessageHeader();
/*     */   }
/*     */ 
/*     */   public MessageHeader getProperties()
/*     */   {
/*  65 */     return this.properties;
/*     */   }
/*     */ 
/*     */   public void setProperties(MessageHeader paramMessageHeader)
/*     */   {
/*  70 */     this.properties = paramMessageHeader;
/*     */   }
/*     */ 
/*     */   public void setRequestProperty(String paramString1, String paramString2) {
/*  74 */     if (this.connected)
/*  75 */       throw new IllegalAccessError("Already connected");
/*  76 */     if (paramString1 == null)
/*  77 */       throw new NullPointerException("key cannot be null");
/*  78 */     this.properties.set(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public void addRequestProperty(String paramString1, String paramString2)
/*     */   {
/*  87 */     if (this.connected)
/*  88 */       throw new IllegalStateException("Already connected");
/*  89 */     if (paramString1 == null)
/*  90 */       throw new NullPointerException("key is null");
/*     */   }
/*     */ 
/*     */   public String getRequestProperty(String paramString) {
/*  94 */     if (this.connected)
/*  95 */       throw new IllegalStateException("Already connected");
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */   public Map<String, List<String>> getRequestProperties() {
/* 100 */     if (this.connected)
/* 101 */       throw new IllegalStateException("Already connected");
/* 102 */     return Collections.EMPTY_MAP;
/*     */   }
/*     */ 
/*     */   public String getHeaderField(String paramString) {
/*     */     try {
/* 107 */       getInputStream();
/*     */     } catch (Exception localException) {
/* 109 */       return null;
/*     */     }
/* 111 */     return this.properties == null ? null : this.properties.findValue(paramString);
/*     */   }
/*     */ 
/*     */   public String getHeaderFieldKey(int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 121 */       getInputStream();
/*     */     } catch (Exception localException) {
/* 123 */       return null;
/*     */     }
/* 125 */     MessageHeader localMessageHeader = this.properties;
/* 126 */     return localMessageHeader == null ? null : localMessageHeader.getKey(paramInt);
/*     */   }
/*     */ 
/*     */   public String getHeaderField(int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 136 */       getInputStream();
/*     */     } catch (Exception localException) {
/* 138 */       return null;
/*     */     }
/* 140 */     MessageHeader localMessageHeader = this.properties;
/* 141 */     return localMessageHeader == null ? null : localMessageHeader.getValue(paramInt);
/*     */   }
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 148 */     if (this.contentType == null)
/* 149 */       this.contentType = getHeaderField("content-type");
/* 150 */     if (this.contentType == null) {
/* 151 */       String str1 = null;
/*     */       try {
/* 153 */         str1 = guessContentTypeFromStream(getInputStream());
/*     */       } catch (IOException localIOException) {
/*     */       }
/* 156 */       String str2 = this.properties.findValue("content-encoding");
/* 157 */       if (str1 == null) {
/* 158 */         str1 = this.properties.findValue("content-type");
/*     */ 
/* 160 */         if (str1 == null) {
/* 161 */           if (this.url.getFile().endsWith("/"))
/* 162 */             str1 = "text/html";
/*     */           else {
/* 164 */             str1 = guessContentTypeFromName(this.url.getFile());
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 176 */       if ((str1 == null) || ((str2 != null) && (!str2.equalsIgnoreCase("7bit")) && (!str2.equalsIgnoreCase("8bit")) && (!str2.equalsIgnoreCase("binary"))))
/*     */       {
/* 180 */         str1 = "content/unknown";
/* 181 */       }setContentType(str1);
/*     */     }
/* 183 */     return this.contentType;
/*     */   }
/*     */ 
/*     */   public void setContentType(String paramString)
/*     */   {
/* 194 */     this.contentType = paramString;
/* 195 */     this.properties.set("content-type", paramString);
/*     */   }
/*     */ 
/*     */   public int getContentLength()
/*     */   {
/*     */     try
/*     */     {
/* 203 */       getInputStream();
/*     */     } catch (Exception localException1) {
/* 205 */       return -1;
/*     */     }
/* 207 */     int i = this.contentLength;
/* 208 */     if (i < 0)
/*     */       try {
/* 210 */         i = Integer.parseInt(this.properties.findValue("content-length"));
/* 211 */         setContentLength(i);
/*     */       }
/*     */       catch (Exception localException2) {
/*     */       }
/* 215 */     return i;
/*     */   }
/*     */ 
/*     */   protected void setContentLength(int paramInt)
/*     */   {
/* 222 */     this.contentLength = paramInt;
/* 223 */     this.properties.set("content-length", String.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   public boolean canCache()
/*     */   {
/* 230 */     return this.url.getFile().indexOf('?') < 0;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 239 */     this.url = null;
/*     */   }
/*     */ 
/*     */   public static synchronized void setProxiedHost(String paramString)
/*     */   {
/* 245 */     proxiedHosts.put(paramString.toLowerCase(), null);
/*     */   }
/*     */ 
/*     */   public static synchronized boolean isProxiedHost(String paramString) {
/* 249 */     return proxiedHosts.containsKey(paramString.toLowerCase());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.URLConnection
 * JD-Core Version:    0.6.2
 */