/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Date;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DateTimeSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ 
/*     */ public final class DateTimeAtCreation extends DateTimeSyntax
/*     */   implements PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -2923732231056647903L;
/*     */ 
/*     */   public DateTimeAtCreation(Date paramDate)
/*     */   {
/*  69 */     super(paramDate);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  92 */     return (super.equals(paramObject)) && ((paramObject instanceof DateTimeAtCreation));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 107 */     return DateTimeAtCreation.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 120 */     return "date-time-at-creation";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.DateTimeAtCreation
 * JD-Core Version:    0.6.2
 */