/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class URIName
/*     */   implements GeneralNameInterface
/*     */ {
/*     */   private URI uri;
/*     */   private String host;
/*     */   private DNSName hostDNS;
/*     */   private IPAddressName hostIP;
/*     */ 
/*     */   public URIName(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  96 */     this(paramDerValue.getIA5String());
/*     */   }
/*     */ 
/*     */   public URIName(String paramString)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 107 */       this.uri = new URI(paramString);
/*     */     } catch (URISyntaxException localURISyntaxException) {
/* 109 */       throw ((IOException)new IOException("invalid URI name:" + paramString).initCause(localURISyntaxException));
/*     */     }
/*     */ 
/* 112 */     if (this.uri.getScheme() == null) {
/* 113 */       throw new IOException("URI name must include scheme:" + paramString);
/*     */     }
/*     */ 
/* 116 */     this.host = this.uri.getHost();
/*     */ 
/* 121 */     if (this.host != null)
/* 122 */       if (this.host.charAt(0) == '[')
/*     */       {
/* 124 */         String str = this.host.substring(1, this.host.length() - 1);
/*     */         try {
/* 126 */           this.hostIP = new IPAddressName(str);
/*     */         } catch (IOException localIOException2) {
/* 128 */           throw new IOException("invalid URI name (host portion is not a valid IPv6 address):" + paramString);
/*     */         }
/*     */       }
/*     */       else {
/*     */         try {
/* 133 */           this.hostDNS = new DNSName(this.host);
/*     */         }
/*     */         catch (IOException localIOException1)
/*     */         {
/*     */           try {
/* 138 */             this.hostIP = new IPAddressName(this.host);
/*     */           } catch (Exception localException) {
/* 140 */             throw new IOException("invalid URI name (host portion is not a valid DNS name, IPv4 address, or IPv6 address):" + paramString);
/*     */           }
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public static URIName nameConstraint(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/* 159 */     String str1 = paramDerValue.getIA5String();
/*     */     URI localURI;
/*     */     try
/*     */     {
/* 161 */       localURI = new URI(str1);
/*     */     } catch (URISyntaxException localURISyntaxException) {
/* 163 */       throw ((IOException)new IOException("invalid URI name constraint:" + str1).initCause(localURISyntaxException));
/*     */     }
/*     */ 
/* 166 */     if (localURI.getScheme() == null) {
/* 167 */       String str2 = localURI.getSchemeSpecificPart();
/*     */       try
/*     */       {
/*     */         DNSName localDNSName;
/* 170 */         if (str2.charAt(0) == '.')
/* 171 */           localDNSName = new DNSName(str2.substring(1));
/*     */         else {
/* 173 */           localDNSName = new DNSName(str2);
/*     */         }
/* 175 */         return new URIName(localURI, str2, localDNSName);
/*     */       } catch (IOException localIOException) {
/* 177 */         throw ((IOException)new IOException("invalid URI name constraint:" + str1).initCause(localIOException));
/*     */       }
/*     */     }
/*     */ 
/* 181 */     throw new IOException("invalid URI name constraint (should not include scheme):" + str1);
/*     */   }
/*     */ 
/*     */   URIName(URI paramURI, String paramString, DNSName paramDNSName)
/*     */   {
/* 187 */     this.uri = paramURI;
/* 188 */     this.host = paramString;
/* 189 */     this.hostDNS = paramDNSName;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 196 */     return 6;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 206 */     paramDerOutputStream.putIA5String(this.uri.toASCIIString());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 213 */     return "URIName: " + this.uri.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 222 */     if (this == paramObject) {
/* 223 */       return true;
/*     */     }
/*     */ 
/* 226 */     if (!(paramObject instanceof URIName)) {
/* 227 */       return false;
/*     */     }
/*     */ 
/* 230 */     URIName localURIName = (URIName)paramObject;
/*     */ 
/* 232 */     return this.uri.equals(localURIName.getURI());
/*     */   }
/*     */ 
/*     */   public URI getURI()
/*     */   {
/* 239 */     return this.uri;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 246 */     return this.uri.toString();
/*     */   }
/*     */ 
/*     */   public String getScheme()
/*     */   {
/* 255 */     return this.uri.getScheme();
/*     */   }
/*     */ 
/*     */   public String getHost()
/*     */   {
/* 264 */     return this.host;
/*     */   }
/*     */ 
/*     */   public Object getHostObject()
/*     */   {
/* 275 */     if (this.hostIP != null) {
/* 276 */       return this.hostIP;
/*     */     }
/* 278 */     return this.hostDNS;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 288 */     return this.uri.hashCode();
/*     */   }
/*     */ 
/*     */   public int constrains(GeneralNameInterface paramGeneralNameInterface)
/*     */     throws UnsupportedOperationException
/*     */   {
/*     */     int i;
/* 323 */     if (paramGeneralNameInterface == null) {
/* 324 */       i = -1;
/* 325 */     } else if (paramGeneralNameInterface.getType() != 6) {
/* 326 */       i = -1;
/*     */     }
/*     */     else
/*     */     {
/* 332 */       String str = ((URIName)paramGeneralNameInterface).getHost();
/*     */ 
/* 335 */       if (str.equalsIgnoreCase(this.host)) {
/* 336 */         i = 0;
/*     */       } else {
/* 338 */         Object localObject = ((URIName)paramGeneralNameInterface).getHostObject();
/*     */ 
/* 340 */         if ((this.hostDNS == null) || (!(localObject instanceof DNSName)))
/*     */         {
/* 343 */           i = 3;
/*     */         }
/*     */         else {
/* 346 */           int j = this.host.charAt(0) == '.' ? 1 : 0;
/* 347 */           int k = str.charAt(0) == '.' ? 1 : 0;
/* 348 */           DNSName localDNSName = (DNSName)localObject;
/*     */ 
/* 351 */           i = this.hostDNS.constrains(localDNSName);
/*     */ 
/* 354 */           if ((j == 0) && (k == 0) && ((i == 2) || (i == 1)))
/*     */           {
/* 357 */             i = 3;
/*     */           }
/*     */ 
/* 364 */           if ((j != k) && (i == 0))
/*     */           {
/* 366 */             if (j != 0)
/* 367 */               i = 2;
/*     */             else {
/* 369 */               i = 1;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 375 */     return i;
/*     */   }
/*     */ 
/*     */   public int subtreeDepth()
/*     */     throws UnsupportedOperationException
/*     */   {
/* 387 */     DNSName localDNSName = null;
/*     */     try {
/* 389 */       localDNSName = new DNSName(this.host);
/*     */     } catch (IOException localIOException) {
/* 391 */       throw new UnsupportedOperationException(localIOException.getMessage());
/*     */     }
/* 393 */     return localDNSName.subtreeDepth();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.URIName
 * JD-Core Version:    0.6.2
 */