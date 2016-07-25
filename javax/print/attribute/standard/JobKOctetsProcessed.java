/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ 
/*     */ public final class JobKOctetsProcessed extends IntegerSyntax
/*     */   implements PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -6265238509657881806L;
/*     */ 
/*     */   public JobKOctetsProcessed(int paramInt)
/*     */   {
/*  86 */     super(paramInt, 0, 2147483647);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 109 */     return (super.equals(paramObject)) && ((paramObject instanceof JobKOctetsProcessed));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 124 */     return JobKOctetsProcessed.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 137 */     return "job-k-octets-processed";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobKOctetsProcessed
 * JD-Core Version:    0.6.2
 */