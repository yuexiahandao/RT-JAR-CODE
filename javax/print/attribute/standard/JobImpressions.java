/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public final class JobImpressions extends IntegerSyntax
/*     */   implements PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 8225537206784322464L;
/*     */ 
/*     */   public JobImpressions(int paramInt)
/*     */   {
/*  92 */     super(paramInt, 0, 2147483647);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 115 */     return (super.equals(paramObject)) && ((paramObject instanceof JobImpressions));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 128 */     return JobImpressions.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 141 */     return "job-impressions";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobImpressions
 * JD-Core Version:    0.6.2
 */