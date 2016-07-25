/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.SetOfIntegerSyntax;
/*     */ import javax.print.attribute.SupportedValuesAttribute;
/*     */ 
/*     */ public final class JobImpressionsSupported extends SetOfIntegerSyntax
/*     */   implements SupportedValuesAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -4887354803843173692L;
/*     */ 
/*     */   public JobImpressionsSupported(int paramInt1, int paramInt2)
/*     */   {
/*  69 */     super(paramInt1, paramInt2);
/*  70 */     if (paramInt1 > paramInt2)
/*  71 */       throw new IllegalArgumentException("Null range specified");
/*  72 */     if (paramInt1 < 0)
/*  73 */       throw new IllegalArgumentException("Job K octets value < 0 specified");
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  99 */     return (super.equals(paramObject)) && ((paramObject instanceof JobImpressionsSupported));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 114 */     return JobImpressionsSupported.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 127 */     return "job-impressions-supported";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobImpressionsSupported
 * JD-Core Version:    0.6.2
 */