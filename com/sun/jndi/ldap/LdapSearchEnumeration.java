/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import com.sun.jndi.toolkit.ctx.Continuation;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Vector;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameClassPair;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.BasicAttributes;
/*     */ import javax.naming.directory.SearchControls;
/*     */ import javax.naming.directory.SearchResult;
/*     */ import javax.naming.ldap.LdapName;
/*     */ import javax.naming.spi.DirectoryManager;
/*     */ 
/*     */ final class LdapSearchEnumeration extends LdapNamingEnumeration
/*     */ {
/*     */   private Name startName;
/*  44 */   private LdapCtx.SearchArgs searchArgs = null;
/*     */ 
/*  46 */   private final AccessControlContext acc = AccessController.getContext();
/*     */ 
/*     */   LdapSearchEnumeration(LdapCtx paramLdapCtx, LdapResult paramLdapResult, String paramString, LdapCtx.SearchArgs paramSearchArgs, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/*  52 */     super(paramLdapCtx, paramLdapResult, paramSearchArgs.name, paramContinuation);
/*     */ 
/*  57 */     this.startName = new LdapName(paramString);
/*  58 */     this.searchArgs = paramSearchArgs;
/*     */   }
/*     */ 
/*     */   protected NameClassPair createItem(String paramString, final Attributes paramAttributes, Vector paramVector)
/*     */     throws NamingException
/*     */   {
/*  65 */     Object localObject1 = null;
/*     */ 
/*  69 */     boolean bool = true;
/*     */     String str1;
/*     */     String str2;
/*     */     try
/*     */     {
/*  75 */       LdapName localLdapName = new LdapName(paramString);
/*     */ 
/*  79 */       if ((this.startName != null) && (localLdapName.startsWith(this.startName))) {
/*  80 */         str1 = localLdapName.getSuffix(this.startName.size()).toString();
/*  81 */         str2 = localLdapName.getSuffix(this.homeCtx.currentParsedDN.size()).toString();
/*     */       } else {
/*  83 */         bool = false;
/*  84 */         str2 = str1 = LdapURL.toUrlString(this.homeCtx.hostname, this.homeCtx.port_number, paramString, this.homeCtx.hasLdapsScheme);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (NamingException localNamingException1)
/*     */     {
/*  90 */       bool = false;
/*  91 */       str2 = str1 = LdapURL.toUrlString(this.homeCtx.hostname, this.homeCtx.port_number, paramString, this.homeCtx.hasLdapsScheme);
/*     */     }
/*     */ 
/*  97 */     CompositeName localCompositeName1 = new CompositeName();
/*  98 */     if (!str1.equals("")) {
/*  99 */       localCompositeName1.add(str1);
/*     */     }
/*     */ 
/* 103 */     CompositeName localCompositeName2 = new CompositeName();
/* 104 */     if (!str2.equals("")) {
/* 105 */       localCompositeName2.add(str2);
/*     */     }
/*     */ 
/* 111 */     this.homeCtx.setParents(paramAttributes, localCompositeName2);
/*     */     Object localObject2;
/* 114 */     if (this.searchArgs.cons.getReturningObjFlag())
/*     */     {
/* 116 */       if (paramAttributes.get(Obj.JAVA_ATTRIBUTES[2]) != null)
/*     */       {
/*     */         try
/*     */         {
/* 120 */           localObject1 = AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */           {
/*     */             public Object run() throws NamingException {
/* 123 */               return Obj.decodeObject(paramAttributes);
/*     */             }
/*     */           }
/*     */           , this.acc);
/*     */         }
/*     */         catch (PrivilegedActionException localPrivilegedActionException)
/*     */         {
/* 127 */           throw ((NamingException)localPrivilegedActionException.getException());
/*     */         }
/*     */       }
/* 130 */       if (localObject1 == null) {
/* 131 */         localObject1 = new LdapCtx(this.homeCtx, paramString);
/*     */       }
/*     */ 
/*     */       Object localObject3;
/*     */       try
/*     */       {
/* 137 */         localObject1 = DirectoryManager.getObjectInstance(localObject1, localCompositeName2, bool ? this.homeCtx : null, this.homeCtx.envprops, paramAttributes);
/*     */       }
/*     */       catch (NamingException localNamingException2)
/*     */       {
/* 141 */         throw localNamingException2;
/*     */       } catch (Exception localException) {
/* 143 */         localObject3 = new NamingException("problem generating object using object factory");
/*     */ 
/* 146 */         ((NamingException)localObject3).setRootCause(localException);
/* 147 */         throw ((Throwable)localObject3);
/*     */       }
/*     */ 
/* 155 */       if ((localObject2 = this.searchArgs.reqAttrs) != null)
/*     */       {
/* 157 */         localObject3 = new BasicAttributes(true);
/* 158 */         for (int i = 0; i < localObject2.length; i++) {
/* 159 */           ((Attributes)localObject3).put(localObject2[i], null);
/*     */         }
/* 161 */         for (i = 0; i < Obj.JAVA_ATTRIBUTES.length; i++)
/*     */         {
/* 163 */           if (((Attributes)localObject3).get(Obj.JAVA_ATTRIBUTES[i]) == null) {
/* 164 */             paramAttributes.remove(Obj.JAVA_ATTRIBUTES[i]);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 178 */     if (paramVector != null) {
/* 179 */       localObject2 = new SearchResultWithControls(bool ? localCompositeName1.toString() : str1, localObject1, paramAttributes, bool, this.homeCtx.convertControls(paramVector));
/*     */     }
/*     */     else
/*     */     {
/* 183 */       localObject2 = new SearchResult(bool ? localCompositeName1.toString() : str1, localObject1, paramAttributes, bool);
/*     */     }
/*     */ 
/* 187 */     ((SearchResult)localObject2).setNameInNamespace(paramString);
/* 188 */     return localObject2;
/*     */   }
/*     */ 
/*     */   public void appendUnprocessedReferrals(LdapReferralException paramLdapReferralException)
/*     */   {
/* 194 */     this.startName = null;
/* 195 */     super.appendUnprocessedReferrals(paramLdapReferralException);
/*     */   }
/*     */ 
/*     */   protected LdapNamingEnumeration getReferredResults(LdapReferralContext paramLdapReferralContext)
/*     */     throws NamingException
/*     */   {
/* 201 */     return (LdapSearchEnumeration)paramLdapReferralContext.search(this.searchArgs.name, this.searchArgs.filter, this.searchArgs.cons);
/*     */   }
/*     */ 
/*     */   protected void update(LdapNamingEnumeration paramLdapNamingEnumeration)
/*     */   {
/* 206 */     super.update(paramLdapNamingEnumeration);
/*     */ 
/* 209 */     LdapSearchEnumeration localLdapSearchEnumeration = (LdapSearchEnumeration)paramLdapNamingEnumeration;
/* 210 */     this.startName = localLdapSearchEnumeration.startName;
/*     */   }
/*     */ 
/*     */   void setStartName(Name paramName)
/*     */   {
/* 216 */     this.startName = paramName;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapSearchEnumeration
 * JD-Core Version:    0.6.2
 */