/*     */ package javax.sql.rowset;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ public class RowSetWarning extends SQLException
/*     */ {
/*     */   private RowSetWarning rwarning;
/*     */   static final long serialVersionUID = 6678332766434564774L;
/*     */ 
/*     */   public RowSetWarning(String paramString)
/*     */   {
/*  72 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public RowSetWarning()
/*     */   {
/*     */   }
/*     */ 
/*     */   public RowSetWarning(String paramString1, String paramString2)
/*     */   {
/*  98 */     super(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public RowSetWarning(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 116 */     super(paramString1, paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   public RowSetWarning getNextWarning()
/*     */   {
/* 129 */     return this.rwarning;
/*     */   }
/*     */ 
/*     */   public void setNextWarning(RowSetWarning paramRowSetWarning)
/*     */   {
/* 142 */     this.rwarning = paramRowSetWarning;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.RowSetWarning
 * JD-Core Version:    0.6.2
 */