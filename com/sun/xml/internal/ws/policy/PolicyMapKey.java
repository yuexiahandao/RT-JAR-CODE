/*     */ package com.sun.xml.internal.ws.policy;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class PolicyMapKey
/*     */ {
/*  46 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(PolicyMapKey.class);
/*     */   private final QName service;
/*     */   private final QName port;
/*     */   private final QName operation;
/*     */   private final QName faultMessage;
/*     */   private PolicyMapKeyHandler handler;
/*     */ 
/*     */   PolicyMapKey(QName service, QName port, QName operation, PolicyMapKeyHandler handler)
/*     */   {
/*  56 */     this(service, port, operation, null, handler);
/*     */   }
/*     */ 
/*     */   PolicyMapKey(QName service, QName port, QName operation, QName faultMessage, PolicyMapKeyHandler handler) {
/*  60 */     if (handler == null) {
/*  61 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0046_POLICY_MAP_KEY_HANDLER_NOT_SET())));
/*     */     }
/*     */ 
/*  64 */     this.service = service;
/*  65 */     this.port = port;
/*  66 */     this.operation = operation;
/*  67 */     this.faultMessage = faultMessage;
/*  68 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */   PolicyMapKey(PolicyMapKey that) {
/*  72 */     this.service = that.service;
/*  73 */     this.port = that.port;
/*  74 */     this.operation = that.operation;
/*  75 */     this.faultMessage = that.faultMessage;
/*  76 */     this.handler = that.handler;
/*     */   }
/*     */ 
/*     */   public QName getOperation() {
/*  80 */     return this.operation;
/*     */   }
/*     */ 
/*     */   public QName getPort() {
/*  84 */     return this.port;
/*     */   }
/*     */ 
/*     */   public QName getService() {
/*  88 */     return this.service;
/*     */   }
/*     */ 
/*     */   void setHandler(PolicyMapKeyHandler handler) {
/*  92 */     if (handler == null) {
/*  93 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0046_POLICY_MAP_KEY_HANDLER_NOT_SET())));
/*     */     }
/*     */ 
/*  96 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */   public QName getFaultMessage() {
/* 100 */     return this.faultMessage;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/* 105 */     if (this == that) {
/* 106 */       return true;
/*     */     }
/*     */ 
/* 109 */     if (that == null) {
/* 110 */       return false;
/*     */     }
/*     */ 
/* 113 */     if ((that instanceof PolicyMapKey)) {
/* 114 */       return this.handler.areEqual(this, (PolicyMapKey)that);
/*     */     }
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 122 */     return this.handler.generateHashCode(this);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 127 */     StringBuffer result = new StringBuffer("PolicyMapKey(");
/* 128 */     result.append(this.service).append(", ").append(this.port).append(", ").append(this.operation).append(", ").append(this.faultMessage);
/* 129 */     return ")";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.PolicyMapKey
 * JD-Core Version:    0.6.2
 */