/*     */ package com.sun.xml.internal.ws.policy.sourcemodel.wspolicy;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public enum NamespaceVersion
/*     */ {
/*  38 */   v1_2("http://schemas.xmlsoap.org/ws/2004/09/policy", "wsp1_2", new XmlToken[] { XmlToken.Policy, XmlToken.ExactlyOne, XmlToken.All, XmlToken.PolicyReference, XmlToken.UsingPolicy, XmlToken.Name, XmlToken.Optional, XmlToken.Ignorable, XmlToken.PolicyUris, XmlToken.Uri, XmlToken.Digest, XmlToken.DigestAlgorithm }), 
/*     */ 
/*  52 */   v1_5("http://www.w3.org/ns/ws-policy", "wsp", new XmlToken[] { XmlToken.Policy, XmlToken.ExactlyOne, XmlToken.All, XmlToken.PolicyReference, XmlToken.UsingPolicy, XmlToken.Name, XmlToken.Optional, XmlToken.Ignorable, XmlToken.PolicyUris, XmlToken.Uri, XmlToken.Digest, XmlToken.DigestAlgorithm });
/*     */ 
/*     */   private final String nsUri;
/*     */   private final String defaultNsPrefix;
/*     */   private final Map<XmlToken, QName> tokenToQNameCache;
/*     */ 
/*     */   public static NamespaceVersion resolveVersion(String uri)
/*     */   {
/*  77 */     for (NamespaceVersion namespaceVersion : values()) {
/*  78 */       if (namespaceVersion.toString().equalsIgnoreCase(uri)) {
/*  79 */         return namespaceVersion;
/*     */       }
/*     */     }
/*     */ 
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */   public static NamespaceVersion resolveVersion(QName name)
/*     */   {
/*  96 */     return resolveVersion(name.getNamespaceURI());
/*     */   }
/*     */ 
/*     */   public static NamespaceVersion getLatestVersion()
/*     */   {
/* 105 */     return v1_5;
/*     */   }
/*     */ 
/*     */   public static XmlToken resolveAsToken(QName name)
/*     */   {
/* 118 */     NamespaceVersion nsVersion = resolveVersion(name);
/* 119 */     if (nsVersion != null) {
/* 120 */       XmlToken token = XmlToken.resolveToken(name.getLocalPart());
/* 121 */       if (nsVersion.tokenToQNameCache.containsKey(token)) {
/* 122 */         return token;
/*     */       }
/*     */     }
/* 125 */     return XmlToken.UNKNOWN;
/*     */   }
/*     */ 
/*     */   private NamespaceVersion(String uri, String prefix, XmlToken[] supportedTokens)
/*     */   {
/* 133 */     this.nsUri = uri;
/* 134 */     this.defaultNsPrefix = prefix;
/*     */ 
/* 136 */     Map temp = new HashMap();
/* 137 */     for (XmlToken token : supportedTokens) {
/* 138 */       temp.put(token, new QName(this.nsUri, token.toString()));
/*     */     }
/* 140 */     this.tokenToQNameCache = Collections.unmodifiableMap(temp);
/*     */   }
/*     */ 
/*     */   public String getDefaultNamespacePrefix()
/*     */   {
/* 149 */     return this.defaultNsPrefix;
/*     */   }
/*     */ 
/*     */   public QName asQName(XmlToken token)
/*     */     throws IllegalArgumentException
/*     */   {
/* 162 */     return (QName)this.tokenToQNameCache.get(token);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 167 */     return this.nsUri;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion
 * JD-Core Version:    0.6.2
 */