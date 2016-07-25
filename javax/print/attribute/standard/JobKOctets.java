/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public final class JobKOctets extends IntegerSyntax
/*     */   implements PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -8959710146498202869L;
/*     */ 
/*     */   public JobKOctets(int paramInt)
/*     */   {
/* 143 */     super(paramInt, 0, 2147483647);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 166 */     return (super.equals(paramObject)) && ((paramObject instanceof JobKOctets));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 179 */     return JobKOctets.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 191 */     return "job-k-octets";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobKOctets
 * JD-Core Version:    0.6.2
 */