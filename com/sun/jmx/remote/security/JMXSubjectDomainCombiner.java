/*     */ package com.sun.jmx.remote.security;
/*     */ 
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSource;
/*     */ import java.security.Permissions;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.security.cert.Certificate;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.SubjectDomainCombiner;
/*     */ 
/*     */ public class JMXSubjectDomainCombiner extends SubjectDomainCombiner
/*     */ {
/*  77 */   private static final CodeSource nullCodeSource = new CodeSource(null, (Certificate[])null);
/*     */ 
/*  83 */   private static final ProtectionDomain pdNoPerms = new ProtectionDomain(nullCodeSource, new Permissions());
/*     */ 
/*     */   public JMXSubjectDomainCombiner(Subject paramSubject)
/*     */   {
/*  49 */     super(paramSubject);
/*     */   }
/*     */ 
/*     */   public ProtectionDomain[] combine(ProtectionDomain[] paramArrayOfProtectionDomain1, ProtectionDomain[] paramArrayOfProtectionDomain2)
/*     */   {
/*     */     ProtectionDomain[] arrayOfProtectionDomain;
/*  61 */     if ((paramArrayOfProtectionDomain1 == null) || (paramArrayOfProtectionDomain1.length == 0)) {
/*  62 */       arrayOfProtectionDomain = new ProtectionDomain[1];
/*  63 */       arrayOfProtectionDomain[0] = pdNoPerms;
/*     */     } else {
/*  65 */       arrayOfProtectionDomain = new ProtectionDomain[paramArrayOfProtectionDomain1.length + 1];
/*  66 */       for (int i = 0; i < paramArrayOfProtectionDomain1.length; i++) {
/*  67 */         arrayOfProtectionDomain[i] = paramArrayOfProtectionDomain1[i];
/*     */       }
/*  69 */       arrayOfProtectionDomain[paramArrayOfProtectionDomain1.length] = pdNoPerms;
/*     */     }
/*  71 */     return super.combine(arrayOfProtectionDomain, paramArrayOfProtectionDomain2);
/*     */   }
/*     */ 
/*     */   public static AccessControlContext getContext(Subject paramSubject)
/*     */   {
/*  90 */     return new AccessControlContext(AccessController.getContext(), new JMXSubjectDomainCombiner(paramSubject));
/*     */   }
/*     */ 
/*     */   public static AccessControlContext getDomainCombinerContext(Subject paramSubject)
/*     */   {
/* 102 */     return new AccessControlContext(new AccessControlContext(new ProtectionDomain[0]), new JMXSubjectDomainCombiner(paramSubject));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.security.JMXSubjectDomainCombiner
 * JD-Core Version:    0.6.2
 */