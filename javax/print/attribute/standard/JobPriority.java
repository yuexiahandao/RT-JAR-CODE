/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public final class JobPriority extends IntegerSyntax
/*     */   implements PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -4599900369040602769L;
/*     */ 
/*     */   public JobPriority(int paramInt)
/*     */   {
/*  76 */     super(paramInt, 1, 100);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  99 */     return (super.equals(paramObject)) && ((paramObject instanceof JobPriority));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 112 */     return JobPriority.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 124 */     return "job-priority";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobPriority
 * JD-Core Version:    0.6.2
 */