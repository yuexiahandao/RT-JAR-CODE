/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Date;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DateTimeSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ 
/*     */ public final class DateTimeAtProcessing extends DateTimeSyntax
/*     */   implements PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -3710068197278263244L;
/*     */ 
/*     */   public DateTimeAtProcessing(Date paramDate)
/*     */   {
/*  69 */     super(paramDate);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  93 */     return (super.equals(paramObject)) && ((paramObject instanceof DateTimeAtProcessing));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 108 */     return DateTimeAtProcessing.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 121 */     return "date-time-at-processing";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.DateTimeAtProcessing
 * JD-Core Version:    0.6.2
 */