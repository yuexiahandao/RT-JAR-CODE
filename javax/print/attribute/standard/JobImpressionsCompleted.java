/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ 
/*     */ public final class JobImpressionsCompleted extends IntegerSyntax
/*     */   implements PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 6722648442432393294L;
/*     */ 
/*     */   public JobImpressionsCompleted(int paramInt)
/*     */   {
/*  74 */     super(paramInt, 0, 2147483647);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  97 */     return (super.equals(paramObject)) && ((paramObject instanceof JobImpressionsCompleted));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 112 */     return JobImpressionsCompleted.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 125 */     return "job-impressions-completed";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobImpressionsCompleted
 * JD-Core Version:    0.6.2
 */