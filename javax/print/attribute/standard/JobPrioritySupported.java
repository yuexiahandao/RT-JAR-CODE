/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.SupportedValuesAttribute;
/*     */ 
/*     */ public final class JobPrioritySupported extends IntegerSyntax
/*     */   implements SupportedValuesAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 2564840378013555894L;
/*     */ 
/*     */   public JobPrioritySupported(int paramInt)
/*     */   {
/*  68 */     super(paramInt, 1, 100);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  92 */     return (super.equals(paramObject)) && ((paramObject instanceof JobPrioritySupported));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 108 */     return JobPrioritySupported.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 121 */     return "job-priority-supported";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobPrioritySupported
 * JD-Core Version:    0.6.2
 */