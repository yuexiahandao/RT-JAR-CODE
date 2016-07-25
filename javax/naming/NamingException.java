/*     */ package javax.naming;
/*     */ 
/*     */ public class NamingException extends Exception
/*     */ {
/*     */   protected Name resolvedName;
/*     */   protected Object resolvedObj;
/*     */   protected Name remainingName;
/* 115 */   protected Throwable rootException = null;
/*     */   private static final long serialVersionUID = -1299181962103167177L;
/*     */ 
/*     */   public NamingException(String paramString)
/*     */   {
/* 126 */     super(paramString);
/* 127 */     this.resolvedName = (this.remainingName = null);
/* 128 */     this.resolvedObj = null;
/*     */   }
/*     */ 
/*     */   public NamingException()
/*     */   {
/* 137 */     this.resolvedName = (this.remainingName = null);
/* 138 */     this.resolvedObj = null;
/*     */   }
/*     */ 
/*     */   public Name getResolvedName()
/*     */   {
/* 152 */     return this.resolvedName;
/*     */   }
/*     */ 
/*     */   public Name getRemainingName()
/*     */   {
/* 165 */     return this.remainingName;
/*     */   }
/*     */ 
/*     */   public Object getResolvedObj()
/*     */   {
/* 178 */     return this.resolvedObj;
/*     */   }
/*     */ 
/*     */   public String getExplanation()
/*     */   {
/* 191 */     return getMessage();
/*     */   }
/*     */ 
/*     */   public void setResolvedName(Name paramName)
/*     */   {
/* 212 */     if (paramName != null)
/* 213 */       this.resolvedName = ((Name)paramName.clone());
/*     */     else
/* 215 */       this.resolvedName = null;
/*     */   }
/*     */ 
/*     */   public void setRemainingName(Name paramName)
/*     */   {
/* 237 */     if (paramName != null)
/* 238 */       this.remainingName = ((Name)paramName.clone());
/*     */     else
/* 240 */       this.remainingName = null;
/*     */   }
/*     */ 
/*     */   public void setResolvedObj(Object paramObject)
/*     */   {
/* 250 */     this.resolvedObj = paramObject;
/*     */   }
/*     */ 
/*     */   public void appendRemainingComponent(String paramString)
/*     */   {
/* 262 */     if (paramString != null)
/*     */       try {
/* 264 */         if (this.remainingName == null) {
/* 265 */           this.remainingName = new CompositeName();
/*     */         }
/* 267 */         this.remainingName.add(paramString);
/*     */       } catch (NamingException localNamingException) {
/* 269 */         throw new IllegalArgumentException(localNamingException.toString());
/*     */       }
/*     */   }
/*     */ 
/*     */   public void appendRemainingName(Name paramName)
/*     */   {
/* 291 */     if (paramName == null) {
/* 292 */       return;
/*     */     }
/* 294 */     if (this.remainingName != null)
/*     */       try {
/* 296 */         this.remainingName.addAll(paramName);
/*     */       } catch (NamingException localNamingException) {
/* 298 */         throw new IllegalArgumentException(localNamingException.toString());
/*     */       }
/*     */     else
/* 301 */       this.remainingName = ((Name)paramName.clone());
/*     */   }
/*     */ 
/*     */   public Throwable getRootCause()
/*     */   {
/* 324 */     return this.rootException;
/*     */   }
/*     */ 
/*     */   public void setRootCause(Throwable paramThrowable)
/*     */   {
/* 343 */     if (paramThrowable != this)
/* 344 */       this.rootException = paramThrowable;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 360 */     return getRootCause();
/*     */   }
/*     */ 
/*     */   public Throwable initCause(Throwable paramThrowable)
/*     */   {
/* 382 */     super.initCause(paramThrowable);
/* 383 */     setRootCause(paramThrowable);
/* 384 */     return this;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 400 */     String str = super.toString();
/*     */ 
/* 402 */     if (this.rootException != null) {
/* 403 */       str = str + " [Root exception is " + this.rootException + "]";
/*     */     }
/* 405 */     if (this.remainingName != null) {
/* 406 */       str = str + "; remaining name '" + this.remainingName + "'";
/*     */     }
/* 408 */     return str;
/*     */   }
/*     */ 
/*     */   public String toString(boolean paramBoolean)
/*     */   {
/* 424 */     if ((!paramBoolean) || (this.resolvedObj == null)) {
/* 425 */       return toString();
/*     */     }
/* 427 */     return toString() + "; resolved object " + this.resolvedObj;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.NamingException
 * JD-Core Version:    0.6.2
 */