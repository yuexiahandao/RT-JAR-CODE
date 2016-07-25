/*     */ package java.rmi.server;
/*     */ 
/*     */ public class ServerCloneException extends CloneNotSupportedException
/*     */ {
/*     */   public Exception detail;
/*     */   private static final long serialVersionUID = 6617456357664815945L;
/*     */ 
/*     */   public ServerCloneException(String paramString)
/*     */   {
/*  70 */     super(paramString);
/*  71 */     initCause(null);
/*     */   }
/*     */ 
/*     */   public ServerCloneException(String paramString, Exception paramException)
/*     */   {
/*  82 */     super(paramString);
/*  83 */     initCause(null);
/*  84 */     this.detail = paramException;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/*  94 */     if (this.detail == null) {
/*  95 */       return super.getMessage();
/*     */     }
/*  97 */     return super.getMessage() + "; nested exception is: \n\t" + this.detail.toString();
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 110 */     return this.detail;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.ServerCloneException
 * JD-Core Version:    0.6.2
 */