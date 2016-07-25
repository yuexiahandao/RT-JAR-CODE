/*     */ package javax.naming.directory;
/*     */ 
/*     */ import javax.naming.Binding;
/*     */ 
/*     */ public class SearchResult extends Binding
/*     */ {
/*     */   private Attributes attrs;
/*     */   private static final long serialVersionUID = -9158063327699723172L;
/*     */ 
/*     */   public SearchResult(String paramString, Object paramObject, Attributes paramAttributes)
/*     */   {
/*  71 */     super(paramString, paramObject);
/*  72 */     this.attrs = paramAttributes;
/*     */   }
/*     */ 
/*     */   public SearchResult(String paramString, Object paramObject, Attributes paramAttributes, boolean paramBoolean)
/*     */   {
/*  96 */     super(paramString, paramObject, paramBoolean);
/*  97 */     this.attrs = paramAttributes;
/*     */   }
/*     */ 
/*     */   public SearchResult(String paramString1, String paramString2, Object paramObject, Attributes paramAttributes)
/*     */   {
/* 120 */     super(paramString1, paramString2, paramObject);
/* 121 */     this.attrs = paramAttributes;
/*     */   }
/*     */ 
/*     */   public SearchResult(String paramString1, String paramString2, Object paramObject, Attributes paramAttributes, boolean paramBoolean)
/*     */   {
/* 145 */     super(paramString1, paramString2, paramObject, paramBoolean);
/* 146 */     this.attrs = paramAttributes;
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes()
/*     */   {
/* 156 */     return this.attrs;
/*     */   }
/*     */ 
/*     */   public void setAttributes(Attributes paramAttributes)
/*     */   {
/* 166 */     this.attrs = paramAttributes;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 182 */     return super.toString() + ":" + getAttributes();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.directory.SearchResult
 * JD-Core Version:    0.6.2
 */