/*     */ package com.sun.xml.internal.ws.policy.jaxws;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.PolicyException;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMapExtender;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMapKey;
/*     */ import com.sun.xml.internal.ws.policy.PolicySubject;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ final class BuilderHandlerMessageScope extends BuilderHandler
/*     */ {
/*     */   private final QName service;
/*     */   private final QName port;
/*     */   private final QName operation;
/*     */   private final QName message;
/*     */   private final Scope scope;
/*     */ 
/*     */   BuilderHandlerMessageScope(Collection<String> policyURIs, Map<String, PolicySourceModel> policyStore, Object policySubject, Scope scope, QName service, QName port, QName operation, QName message)
/*     */   {
/*  65 */     super(policyURIs, policyStore, policySubject);
/*  66 */     this.service = service;
/*  67 */     this.port = port;
/*  68 */     this.operation = operation;
/*  69 */     this.scope = scope;
/*  70 */     this.message = message;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  80 */     if (this == obj) {
/*  81 */       return true;
/*     */     }
/*     */ 
/*  84 */     if (!(obj instanceof BuilderHandlerMessageScope)) {
/*  85 */       return false;
/*     */     }
/*     */ 
/*  88 */     BuilderHandlerMessageScope that = (BuilderHandlerMessageScope)obj;
/*  89 */     boolean result = true;
/*     */ 
/*  91 */     result = (result) && (this.policySubject == null ? that.policySubject == null : this.policySubject.equals(that.policySubject));
/*  92 */     result = (result) && (this.scope == null ? that.scope == null : this.scope.equals(that.scope));
/*  93 */     result = (result) && (this.message == null ? that.message == null : this.message.equals(that.message));
/*  94 */     if (this.scope != Scope.FaultMessageScope) {
/*  95 */       result = (result) && (this.service == null ? that.service == null : this.service.equals(that.service));
/*  96 */       result = (result) && (this.port == null ? that.port == null : this.port.equals(that.port));
/*  97 */       result = (result) && (this.operation == null ? that.operation == null : this.operation.equals(that.operation));
/*     */     }
/*     */ 
/* 100 */     return result;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 105 */     int hashCode = 19;
/* 106 */     hashCode = 31 * hashCode + (this.policySubject == null ? 0 : this.policySubject.hashCode());
/* 107 */     hashCode = 31 * hashCode + (this.message == null ? 0 : this.message.hashCode());
/* 108 */     hashCode = 31 * hashCode + (this.scope == null ? 0 : this.scope.hashCode());
/* 109 */     if (this.scope != Scope.FaultMessageScope) {
/* 110 */       hashCode = 31 * hashCode + (this.service == null ? 0 : this.service.hashCode());
/* 111 */       hashCode = 31 * hashCode + (this.port == null ? 0 : this.port.hashCode());
/* 112 */       hashCode = 31 * hashCode + (this.operation == null ? 0 : this.operation.hashCode());
/*     */     }
/* 114 */     return hashCode;
/*     */   }
/*     */ 
/*     */   protected void doPopulate(PolicyMapExtender policyMapExtender)
/*     */     throws PolicyException
/*     */   {
/*     */     PolicyMapKey mapKey;
/*     */     PolicyMapKey mapKey;
/* 120 */     if (Scope.FaultMessageScope == this.scope)
/* 121 */       mapKey = PolicyMap.createWsdlFaultMessageScopeKey(this.service, this.port, this.operation, this.message);
/*     */     else {
/* 123 */       mapKey = PolicyMap.createWsdlMessageScopeKey(this.service, this.port, this.operation);
/*     */     }
/*     */ 
/* 126 */     if (Scope.InputMessageScope == this.scope) {
/* 127 */       for (PolicySubject subject : getPolicySubjects())
/* 128 */         policyMapExtender.putInputMessageSubject(mapKey, subject);
/*     */     }
/* 130 */     else if (Scope.OutputMessageScope == this.scope) {
/* 131 */       for (PolicySubject subject : getPolicySubjects())
/* 132 */         policyMapExtender.putOutputMessageSubject(mapKey, subject);
/*     */     }
/* 134 */     else if (Scope.FaultMessageScope == this.scope)
/* 135 */       for (PolicySubject subject : getPolicySubjects())
/* 136 */         policyMapExtender.putFaultMessageSubject(mapKey, subject);
/*     */   }
/*     */ 
/*     */   static enum Scope
/*     */   {
/*  51 */     InputMessageScope, 
/*  52 */     OutputMessageScope, 
/*  53 */     FaultMessageScope;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.jaxws.BuilderHandlerMessageScope
 * JD-Core Version:    0.6.2
 */