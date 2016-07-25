/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.SetOfIntegerSyntax;
/*     */ import javax.print.attribute.SupportedValuesAttribute;
/*     */ 
/*     */ public final class JobMediaSheetsSupported extends SetOfIntegerSyntax
/*     */   implements SupportedValuesAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 2953685470388672940L;
/*     */ 
/*     */   public JobMediaSheetsSupported(int paramInt1, int paramInt2)
/*     */   {
/*  68 */     super(paramInt1, paramInt2);
/*  69 */     if (paramInt1 > paramInt2)
/*  70 */       throw new IllegalArgumentException("Null range specified");
/*  71 */     if (paramInt1 < 0)
/*  72 */       throw new IllegalArgumentException("Job K octets value < 0 specified");
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  97 */     return (super.equals(paramObject)) && ((paramObject instanceof JobMediaSheetsSupported));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 112 */     return JobMediaSheetsSupported.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 125 */     return "job-media-sheets-supported";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobMediaSheetsSupported
 * JD-Core Version:    0.6.2
 */