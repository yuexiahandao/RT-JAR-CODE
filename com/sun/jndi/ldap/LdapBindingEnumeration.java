/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import com.sun.jndi.toolkit.ctx.Continuation;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Vector;
/*     */ import javax.naming.Binding;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameClassPair;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.spi.DirectoryManager;
/*     */ 
/*     */ final class LdapBindingEnumeration extends LdapNamingEnumeration
/*     */ {
/*  41 */   private final AccessControlContext acc = AccessController.getContext();
/*     */ 
/*     */   LdapBindingEnumeration(LdapCtx paramLdapCtx, LdapResult paramLdapResult, Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/*  46 */     super(paramLdapCtx, paramLdapResult, paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   protected NameClassPair createItem(String paramString, final Attributes paramAttributes, Vector paramVector)
/*     */     throws NamingException
/*     */   {
/*  53 */     Object localObject1 = null;
/*  54 */     String str = getAtom(paramString);
/*     */ 
/*  56 */     if (paramAttributes.get(Obj.JAVA_ATTRIBUTES[2]) != null) {
/*     */       try
/*     */       {
/*  59 */         localObject1 = AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Object run() throws NamingException {
/*  62 */             return Obj.decodeObject(paramAttributes);
/*     */           }
/*     */         }
/*     */         , this.acc);
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException)
/*     */       {
/*  66 */         throw ((NamingException)localPrivilegedActionException.getException());
/*     */       }
/*     */     }
/*  69 */     if (localObject1 == null)
/*     */     {
/*  71 */       localObject1 = new LdapCtx(this.homeCtx, paramString);
/*     */     }
/*     */ 
/*  74 */     CompositeName localCompositeName = new CompositeName();
/*  75 */     localCompositeName.add(str);
/*     */     try
/*     */     {
/*  78 */       localObject1 = DirectoryManager.getObjectInstance(localObject1, localCompositeName, this.homeCtx, this.homeCtx.envprops, paramAttributes);
/*     */     }
/*     */     catch (NamingException localNamingException1)
/*     */     {
/*  82 */       throw localNamingException1;
/*     */     }
/*     */     catch (Exception localException) {
/*  85 */       NamingException localNamingException2 = new NamingException("problem generating object using object factory");
/*     */ 
/*  88 */       localNamingException2.setRootCause(localException);
/*  89 */       throw localNamingException2;
/*     */     }
/*     */     Object localObject2;
/*  93 */     if (paramVector != null) {
/*  94 */       localObject2 = new BindingWithControls(localCompositeName.toString(), localObject1, this.homeCtx.convertControls(paramVector));
/*     */     }
/*     */     else {
/*  97 */       localObject2 = new Binding(localCompositeName.toString(), localObject1);
/*     */     }
/*  99 */     ((Binding)localObject2).setNameInNamespace(paramString);
/* 100 */     return localObject2;
/*     */   }
/*     */ 
/*     */   protected LdapNamingEnumeration getReferredResults(LdapReferralContext paramLdapReferralContext)
/*     */     throws NamingException
/*     */   {
/* 106 */     return (LdapNamingEnumeration)paramLdapReferralContext.listBindings(this.listArg);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapBindingEnumeration
 * JD-Core Version:    0.6.2
 */