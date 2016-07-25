/*     */ package com.sun.xml.internal.ws.policy.spi;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.PolicyAssertion;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public abstract class AbstractQNameValidator
/*     */   implements PolicyAssertionValidator
/*     */ {
/*  58 */   private final Set<String> supportedDomains = new HashSet();
/*     */   private final Collection<QName> serverAssertions;
/*     */   private final Collection<QName> clientAssertions;
/*     */ 
/*     */   protected AbstractQNameValidator(Collection<QName> serverSideAssertions, Collection<QName> clientSideAssertions)
/*     */   {
/*  72 */     if (serverSideAssertions != null) {
/*  73 */       this.serverAssertions = new HashSet(serverSideAssertions);
/*  74 */       for (QName assertion : this.serverAssertions)
/*  75 */         this.supportedDomains.add(assertion.getNamespaceURI());
/*     */     }
/*     */     else {
/*  78 */       this.serverAssertions = new HashSet(0);
/*     */     }
/*     */ 
/*  81 */     if (clientSideAssertions != null) {
/*  82 */       this.clientAssertions = new HashSet(clientSideAssertions);
/*  83 */       for (QName assertion : this.clientAssertions)
/*  84 */         this.supportedDomains.add(assertion.getNamespaceURI());
/*     */     }
/*     */     else {
/*  87 */       this.clientAssertions = new HashSet(0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] declareSupportedDomains() {
/*  92 */     return (String[])this.supportedDomains.toArray(new String[this.supportedDomains.size()]);
/*     */   }
/*     */ 
/*     */   public PolicyAssertionValidator.Fitness validateClientSide(PolicyAssertion assertion) {
/*  96 */     return validateAssertion(assertion, this.clientAssertions, this.serverAssertions);
/*     */   }
/*     */ 
/*     */   public PolicyAssertionValidator.Fitness validateServerSide(PolicyAssertion assertion) {
/* 100 */     return validateAssertion(assertion, this.serverAssertions, this.clientAssertions);
/*     */   }
/*     */ 
/*     */   private PolicyAssertionValidator.Fitness validateAssertion(PolicyAssertion assertion, Collection<QName> thisSideAssertions, Collection<QName> otherSideAssertions) {
/* 104 */     QName assertionName = assertion.getName();
/* 105 */     if (thisSideAssertions.contains(assertionName))
/* 106 */       return PolicyAssertionValidator.Fitness.SUPPORTED;
/* 107 */     if (otherSideAssertions.contains(assertionName)) {
/* 108 */       return PolicyAssertionValidator.Fitness.UNSUPPORTED;
/*     */     }
/* 110 */     return PolicyAssertionValidator.Fitness.UNKNOWN;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.spi.AbstractQNameValidator
 * JD-Core Version:    0.6.2
 */