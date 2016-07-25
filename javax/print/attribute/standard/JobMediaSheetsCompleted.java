/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ 
/*     */ public final class JobMediaSheetsCompleted extends IntegerSyntax
/*     */   implements PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 1739595973810840475L;
/*     */ 
/*     */   public JobMediaSheetsCompleted(int paramInt)
/*     */   {
/*  75 */     super(paramInt, 0, 2147483647);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  98 */     return (super.equals(paramObject)) && ((paramObject instanceof JobMediaSheetsCompleted));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 113 */     return JobMediaSheetsCompleted.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 126 */     return "job-media-sheets-completed";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobMediaSheetsCompleted
 * JD-Core Version:    0.6.2
 */