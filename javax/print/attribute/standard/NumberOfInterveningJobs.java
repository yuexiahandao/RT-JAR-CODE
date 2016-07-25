/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ 
/*     */ public final class NumberOfInterveningJobs extends IntegerSyntax
/*     */   implements PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 2568141124844982746L;
/*     */ 
/*     */   public NumberOfInterveningJobs(int paramInt)
/*     */   {
/*  59 */     super(paramInt, 0, 2147483647);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  82 */     return (super.equals(paramObject)) && ((paramObject instanceof NumberOfInterveningJobs));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/*  97 */     return NumberOfInterveningJobs.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 110 */     return "number-of-intervening-jobs";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.NumberOfInterveningJobs
 * JD-Core Version:    0.6.2
 */