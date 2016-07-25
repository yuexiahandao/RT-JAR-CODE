/*     */ package com.sun.xml.internal.ws.policy.sourcemodel;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ 
/*     */ final class PolicyReferenceData
/*     */ {
/*  39 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(PolicyReferenceData.class);
/*     */   private static final URI DEFAULT_DIGEST_ALGORITHM_URI;
/*     */   private static final URISyntaxException CLASS_INITIALIZATION_EXCEPTION;
/*     */   private final URI referencedModelUri;
/*     */   private final String digest;
/*     */   private final URI digestAlgorithmUri;
/*     */ 
/*     */   public PolicyReferenceData(URI referencedModelUri)
/*     */   {
/*  62 */     this.referencedModelUri = referencedModelUri;
/*  63 */     this.digest = null;
/*  64 */     this.digestAlgorithmUri = null;
/*     */   }
/*     */ 
/*     */   public PolicyReferenceData(URI referencedModelUri, String expectedDigest, URI usedDigestAlgorithm) {
/*  68 */     if (CLASS_INITIALIZATION_EXCEPTION != null) {
/*  69 */       throw ((IllegalStateException)LOGGER.logSevereException(new IllegalStateException(LocalizationMessages.WSP_0015_UNABLE_TO_INSTANTIATE_DIGEST_ALG_URI_FIELD(), CLASS_INITIALIZATION_EXCEPTION)));
/*     */     }
/*     */ 
/*  72 */     if ((usedDigestAlgorithm != null) && (expectedDigest == null)) {
/*  73 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0072_DIGEST_MUST_NOT_BE_NULL_WHEN_ALG_DEFINED())));
/*     */     }
/*     */ 
/*  76 */     this.referencedModelUri = referencedModelUri;
/*  77 */     if (expectedDigest == null) {
/*  78 */       this.digest = null;
/*  79 */       this.digestAlgorithmUri = null;
/*     */     } else {
/*  81 */       this.digest = expectedDigest;
/*     */ 
/*  83 */       if (usedDigestAlgorithm == null)
/*  84 */         this.digestAlgorithmUri = DEFAULT_DIGEST_ALGORITHM_URI;
/*     */       else
/*  86 */         this.digestAlgorithmUri = usedDigestAlgorithm;
/*     */     }
/*     */   }
/*     */ 
/*     */   public URI getReferencedModelUri()
/*     */   {
/*  92 */     return this.referencedModelUri;
/*     */   }
/*     */ 
/*     */   public String getDigest() {
/*  96 */     return this.digest;
/*     */   }
/*     */ 
/*     */   public URI getDigestAlgorithmUri() {
/* 100 */     return this.digestAlgorithmUri;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 108 */     return toString(0, new StringBuffer()).toString();
/*     */   }
/*     */ 
/*     */   public StringBuffer toString(int indentLevel, StringBuffer buffer)
/*     */   {
/* 119 */     String indent = PolicyUtils.Text.createIndent(indentLevel);
/* 120 */     String innerIndent = PolicyUtils.Text.createIndent(indentLevel + 1);
/*     */ 
/* 122 */     buffer.append(indent).append("reference data {").append(PolicyUtils.Text.NEW_LINE);
/* 123 */     buffer.append(innerIndent).append("referenced policy model URI = '").append(this.referencedModelUri).append('\'').append(PolicyUtils.Text.NEW_LINE);
/* 124 */     if (this.digest == null) {
/* 125 */       buffer.append(innerIndent).append("no digest specified").append(PolicyUtils.Text.NEW_LINE);
/*     */     } else {
/* 127 */       buffer.append(innerIndent).append("digest algorith URI = '").append(this.digestAlgorithmUri).append('\'').append(PolicyUtils.Text.NEW_LINE);
/* 128 */       buffer.append(innerIndent).append("digest = '").append(this.digest).append('\'').append(PolicyUtils.Text.NEW_LINE);
/*     */     }
/* 130 */     buffer.append(indent).append('}');
/*     */ 
/* 132 */     return buffer;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  44 */     URISyntaxException tempEx = null;
/*  45 */     URI tempUri = null;
/*     */     try {
/*  47 */       tempUri = new URI("http://schemas.xmlsoap.org/ws/2004/09/policy/Sha1Exc");
/*     */     } catch (URISyntaxException e) {
/*  49 */       tempEx = e;
/*     */     } finally {
/*  51 */       DEFAULT_DIGEST_ALGORITHM_URI = tempUri;
/*  52 */       CLASS_INITIALIZATION_EXCEPTION = tempEx;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.sourcemodel.PolicyReferenceData
 * JD-Core Version:    0.6.2
 */