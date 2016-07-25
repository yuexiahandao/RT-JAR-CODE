/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Date;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DateTimeSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ 
/*     */ public final class DateTimeAtCompleted extends DateTimeSyntax
/*     */   implements PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 6497399708058490000L;
/*     */ 
/*     */   public DateTimeAtCompleted(Date paramDate)
/*     */   {
/*  69 */     super(paramDate);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  92 */     return (super.equals(paramObject)) && ((paramObject instanceof DateTimeAtCompleted));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 109 */     return DateTimeAtCompleted.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 122 */     return "date-time-at-completed";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.DateTimeAtCompleted
 * JD-Core Version:    0.6.2
 */