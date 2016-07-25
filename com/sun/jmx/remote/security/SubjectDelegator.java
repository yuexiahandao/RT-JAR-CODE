/*     */ package com.sun.jmx.remote.security;
/*     */ 
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.management.remote.SubjectDelegationPermission;
/*     */ import javax.security.auth.Subject;
/*     */ 
/*     */ public class SubjectDelegator
/*     */ {
/*     */   public AccessControlContext delegatedContext(AccessControlContext paramAccessControlContext, Subject paramSubject, boolean paramBoolean)
/*     */     throws SecurityException
/*     */   {
/*  50 */     if ((System.getSecurityManager() != null) && (paramAccessControlContext == null)) {
/*  51 */       throw new SecurityException("Illegal AccessControlContext: null");
/*     */     }
/*     */ 
/*  58 */     Collection localCollection = getSubjectPrincipals(paramSubject);
/*  59 */     final ArrayList localArrayList = new ArrayList(localCollection.size());
/*  60 */     for (Object localObject = localCollection.iterator(); ((Iterator)localObject).hasNext(); ) { Principal localPrincipal = (Principal)((Iterator)localObject).next();
/*  61 */       String str = localPrincipal.getClass().getName() + "." + localPrincipal.getName();
/*  62 */       localArrayList.add(new SubjectDelegationPermission(str));
/*     */     }
/*  64 */     localObject = new PrivilegedAction()
/*     */     {
/*     */       public Void run() {
/*  67 */         for (Permission localPermission : localArrayList) {
/*  68 */           AccessController.checkPermission(localPermission);
/*     */         }
/*  70 */         return null;
/*     */       }
/*     */     };
/*  73 */     AccessController.doPrivileged((PrivilegedAction)localObject, paramAccessControlContext);
/*     */ 
/*  75 */     return getDelegatedAcc(paramSubject, paramBoolean);
/*     */   }
/*     */ 
/*     */   private AccessControlContext getDelegatedAcc(Subject paramSubject, boolean paramBoolean) {
/*  79 */     if (paramBoolean) {
/*  80 */       return JMXSubjectDomainCombiner.getDomainCombinerContext(paramSubject);
/*     */     }
/*  82 */     return JMXSubjectDomainCombiner.getContext(paramSubject);
/*     */   }
/*     */ 
/*     */   public static synchronized boolean checkRemoveCallerContext(Subject paramSubject)
/*     */   {
/*     */     try
/*     */     {
/*  98 */       for (Principal localPrincipal : getSubjectPrincipals(paramSubject)) {
/*  99 */         String str = localPrincipal.getClass().getName() + "." + localPrincipal.getName();
/*     */ 
/* 101 */         SubjectDelegationPermission localSubjectDelegationPermission = new SubjectDelegationPermission(str);
/*     */ 
/* 103 */         AccessController.checkPermission(localSubjectDelegationPermission);
/*     */       }
/*     */     } catch (SecurityException localSecurityException) {
/* 106 */       return false;
/*     */     }
/* 108 */     return true;
/*     */   }
/*     */ 
/*     */   private static Collection<Principal> getSubjectPrincipals(Subject paramSubject)
/*     */   {
/* 118 */     if (paramSubject.isReadOnly()) {
/* 119 */       return paramSubject.getPrincipals();
/*     */     }
/*     */ 
/* 122 */     List localList = Arrays.asList(paramSubject.getPrincipals().toArray(new Principal[0]));
/* 123 */     return Collections.unmodifiableList(localList);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.security.SubjectDelegator
 * JD-Core Version:    0.6.2
 */