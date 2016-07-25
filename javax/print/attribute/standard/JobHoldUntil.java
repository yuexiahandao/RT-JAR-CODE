/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Date;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DateTimeSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public final class JobHoldUntil extends DateTimeSyntax
/*     */   implements PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -1664471048860415024L;
/*     */ 
/*     */   public JobHoldUntil(Date paramDate)
/*     */   {
/* 101 */     super(paramDate);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 124 */     return (super.equals(paramObject)) && ((paramObject instanceof JobHoldUntil));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 138 */     return JobHoldUntil.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 150 */     return "job-hold-until";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobHoldUntil
 * JD-Core Version:    0.6.2
 */