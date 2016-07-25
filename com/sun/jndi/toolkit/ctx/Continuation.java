/*     */ package com.sun.jndi.toolkit.ctx;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.CannotProceedException;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.LinkRef;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.spi.ResolveResult;
/*     */ 
/*     */ public class Continuation extends ResolveResult
/*     */ {
/*     */   protected Name starter;
/*  54 */   protected Object followingLink = null;
/*     */ 
/*  60 */   protected Hashtable environment = null;
/*     */ 
/*  68 */   protected boolean continuing = false;
/*     */ 
/*  74 */   protected Context resolvedContext = null;
/*     */ 
/*  80 */   protected Name relativeResolvedName = null;
/*     */   private static final long serialVersionUID = 8162530656132624308L;
/*     */ 
/*     */   public Continuation()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Continuation(Name paramName, Hashtable paramHashtable)
/*     */   {
/*  99 */     this.starter = paramName;
/* 100 */     this.environment = paramHashtable;
/*     */   }
/*     */ 
/*     */   public boolean isContinue()
/*     */   {
/* 111 */     return this.continuing;
/*     */   }
/*     */ 
/*     */   public void setSuccess()
/*     */   {
/* 127 */     this.continuing = false;
/*     */   }
/*     */ 
/*     */   public NamingException fillInException(NamingException paramNamingException)
/*     */   {
/* 143 */     paramNamingException.setRemainingName(this.remainingName);
/* 144 */     paramNamingException.setResolvedObj(this.resolvedObj);
/*     */ 
/* 146 */     if ((this.starter == null) || (this.starter.isEmpty()))
/* 147 */       paramNamingException.setResolvedName(null);
/* 148 */     else if (this.remainingName == null)
/* 149 */       paramNamingException.setResolvedName(this.starter);
/*     */     else {
/* 151 */       paramNamingException.setResolvedName(this.starter.getPrefix(this.starter.size() - this.remainingName.size()));
/*     */     }
/*     */ 
/* 155 */     if ((paramNamingException instanceof CannotProceedException)) {
/* 156 */       CannotProceedException localCannotProceedException = (CannotProceedException)paramNamingException;
/* 157 */       Hashtable localHashtable = this.environment == null ? new Hashtable(11) : (Hashtable)this.environment.clone();
/*     */ 
/* 159 */       localCannotProceedException.setEnvironment(localHashtable);
/* 160 */       localCannotProceedException.setAltNameCtx(this.resolvedContext);
/* 161 */       localCannotProceedException.setAltName(this.relativeResolvedName);
/*     */     }
/*     */ 
/* 164 */     return paramNamingException;
/*     */   }
/*     */ 
/*     */   public void setErrorNNS(Object paramObject, Name paramName)
/*     */   {
/* 184 */     Name localName = (Name)paramName.clone();
/*     */     try {
/* 186 */       localName.add("");
/*     */     }
/*     */     catch (InvalidNameException localInvalidNameException) {
/*     */     }
/* 190 */     setErrorAux(paramObject, localName);
/*     */   }
/*     */ 
/*     */   public void setErrorNNS(Object paramObject, String paramString)
/*     */   {
/* 202 */     CompositeName localCompositeName = new CompositeName();
/*     */     try {
/* 204 */       if ((paramString != null) && (!paramString.equals(""))) {
/* 205 */         localCompositeName.add(paramString);
/*     */       }
/* 207 */       localCompositeName.add("");
/*     */     }
/*     */     catch (InvalidNameException localInvalidNameException) {
/*     */     }
/* 211 */     setErrorAux(paramObject, localCompositeName);
/*     */   }
/*     */ 
/*     */   public void setError(Object paramObject, Name paramName)
/*     */   {
/* 229 */     if (paramName != null)
/* 230 */       this.remainingName = ((Name)paramName.clone());
/*     */     else {
/* 232 */       this.remainingName = null;
/*     */     }
/* 234 */     setErrorAux(paramObject, this.remainingName);
/*     */   }
/*     */ 
/*     */   public void setError(Object paramObject, String paramString)
/*     */   {
/* 247 */     CompositeName localCompositeName = new CompositeName();
/* 248 */     if ((paramString != null) && (!paramString.equals("")))
/*     */       try {
/* 250 */         localCompositeName.add(paramString);
/*     */       }
/*     */       catch (InvalidNameException localInvalidNameException)
/*     */       {
/*     */       }
/* 255 */     setErrorAux(paramObject, localCompositeName);
/*     */   }
/*     */ 
/*     */   private void setErrorAux(Object paramObject, Name paramName) {
/* 259 */     this.remainingName = paramName;
/* 260 */     this.resolvedObj = paramObject;
/* 261 */     this.continuing = false;
/*     */   }
/*     */ 
/*     */   private void setContinueAux(Object paramObject, Name paramName1, Context paramContext, Name paramName2)
/*     */   {
/* 266 */     if ((paramObject instanceof LinkRef)) {
/* 267 */       setContinueLink(paramObject, paramName1, paramContext, paramName2);
/*     */     } else {
/* 269 */       this.remainingName = paramName2;
/* 270 */       this.resolvedObj = paramObject;
/*     */ 
/* 272 */       this.relativeResolvedName = paramName1;
/* 273 */       this.resolvedContext = paramContext;
/*     */ 
/* 275 */       this.continuing = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setContinueNNS(Object paramObject, Name paramName, Context paramContext)
/*     */   {
/* 295 */     CompositeName localCompositeName = new CompositeName();
/*     */ 
/* 297 */     setContinue(paramObject, paramName, paramContext, PartialCompositeContext._NNS_NAME);
/*     */   }
/*     */ 
/*     */   public void setContinueNNS(Object paramObject, String paramString, Context paramContext)
/*     */   {
/* 309 */     CompositeName localCompositeName = new CompositeName();
/*     */     try {
/* 311 */       localCompositeName.add(paramString);
/*     */     } catch (NamingException localNamingException) {
/*     */     }
/* 314 */     setContinue(paramObject, localCompositeName, paramContext, PartialCompositeContext._NNS_NAME);
/*     */   }
/*     */ 
/*     */   public void setContinue(Object paramObject, Name paramName, Context paramContext)
/*     */   {
/* 335 */     setContinueAux(paramObject, paramName, paramContext, (Name)PartialCompositeContext._EMPTY_NAME.clone());
/*     */   }
/*     */ 
/*     */   public void setContinue(Object paramObject, Name paramName1, Context paramContext, Name paramName2)
/*     */   {
/* 356 */     if (paramName2 != null)
/* 357 */       this.remainingName = ((Name)paramName2.clone());
/*     */     else {
/* 359 */       this.remainingName = new CompositeName();
/*     */     }
/* 361 */     setContinueAux(paramObject, paramName1, paramContext, this.remainingName);
/*     */   }
/*     */ 
/*     */   public void setContinue(Object paramObject, String paramString1, Context paramContext, String paramString2)
/*     */   {
/* 375 */     CompositeName localCompositeName1 = new CompositeName();
/* 376 */     if (!paramString1.equals(""))
/*     */       try {
/* 378 */         localCompositeName1.add(paramString1);
/*     */       }
/*     */       catch (NamingException localNamingException1) {
/*     */       }
/* 382 */     CompositeName localCompositeName2 = new CompositeName();
/* 383 */     if (!paramString2.equals(""))
/*     */       try {
/* 385 */         localCompositeName2.add(paramString2);
/*     */       }
/*     */       catch (NamingException localNamingException2)
/*     */       {
/*     */       }
/* 390 */     setContinueAux(paramObject, localCompositeName1, paramContext, localCompositeName2);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setContinue(Object paramObject1, Object paramObject2)
/*     */   {
/* 403 */     setContinue(paramObject1, null, (Context)paramObject2);
/*     */   }
/*     */ 
/*     */   private void setContinueLink(Object paramObject, Name paramName1, Context paramContext, Name paramName2)
/*     */   {
/* 413 */     this.followingLink = paramObject;
/*     */ 
/* 415 */     this.remainingName = paramName2;
/* 416 */     this.resolvedObj = paramContext;
/*     */ 
/* 418 */     this.relativeResolvedName = PartialCompositeContext._EMPTY_NAME;
/* 419 */     this.resolvedContext = paramContext;
/*     */ 
/* 421 */     this.continuing = true;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 425 */     if (this.remainingName != null) {
/* 426 */       return this.starter.toString() + "; remainingName: '" + this.remainingName + "'";
/*     */     }
/* 428 */     return this.starter.toString();
/*     */   }
/*     */ 
/*     */   public String toString(boolean paramBoolean) {
/* 432 */     if ((!paramBoolean) || (this.resolvedObj == null))
/* 433 */       return toString();
/* 434 */     return toString() + "; resolvedObj: " + this.resolvedObj + "; relativeResolvedName: " + this.relativeResolvedName + "; resolvedContext: " + this.resolvedContext;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.ctx.Continuation
 * JD-Core Version:    0.6.2
 */