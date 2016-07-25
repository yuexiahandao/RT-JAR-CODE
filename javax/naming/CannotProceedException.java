/*     */ package javax.naming;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class CannotProceedException extends NamingException
/*     */ {
/*  78 */   protected Name remainingNewName = null;
/*     */ 
/*  92 */   protected Hashtable<?, ?> environment = null;
/*     */ 
/* 111 */   protected Name altName = null;
/*     */ 
/* 130 */   protected Context altNameCtx = null;
/*     */   private static final long serialVersionUID = 1219724816191576813L;
/*     */ 
/*     */   public CannotProceedException(String paramString)
/*     */   {
/* 142 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public CannotProceedException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Hashtable<?, ?> getEnvironment()
/*     */   {
/* 161 */     return this.environment;
/*     */   }
/*     */ 
/*     */   public void setEnvironment(Hashtable<?, ?> paramHashtable)
/*     */   {
/* 173 */     this.environment = paramHashtable;
/*     */   }
/*     */ 
/*     */   public Name getRemainingNewName()
/*     */   {
/* 187 */     return this.remainingNewName;
/*     */   }
/*     */ 
/*     */   public void setRemainingNewName(Name paramName)
/*     */   {
/* 210 */     if (paramName != null)
/* 211 */       this.remainingNewName = ((Name)paramName.clone());
/*     */     else
/* 213 */       this.remainingNewName = null;
/*     */   }
/*     */ 
/*     */   public Name getAltName()
/*     */   {
/* 231 */     return this.altName;
/*     */   }
/*     */ 
/*     */   public void setAltName(Name paramName)
/*     */   {
/* 246 */     this.altName = paramName;
/*     */   }
/*     */ 
/*     */   public Context getAltNameCtx()
/*     */   {
/* 263 */     return this.altNameCtx;
/*     */   }
/*     */ 
/*     */   public void setAltNameCtx(Context paramContext)
/*     */   {
/* 278 */     this.altNameCtx = paramContext;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.CannotProceedException
 * JD-Core Version:    0.6.2
 */