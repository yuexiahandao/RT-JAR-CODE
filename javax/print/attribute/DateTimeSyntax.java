/*     */ package javax.print.attribute;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ 
/*     */ public abstract class DateTimeSyntax
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = -1400819079791208582L;
/*     */   private Date value;
/*     */ 
/*     */   protected DateTimeSyntax(Date paramDate)
/*     */   {
/*  85 */     if (paramDate == null) {
/*  86 */       throw new NullPointerException("value is null");
/*     */     }
/*  88 */     this.value = paramDate;
/*     */   }
/*     */ 
/*     */   public Date getValue()
/*     */   {
/*  99 */     return new Date(this.value.getTime());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 123 */     return (paramObject != null) && ((paramObject instanceof DateTimeSyntax)) && (this.value.equals(((DateTimeSyntax)paramObject).value));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 133 */     return this.value.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 143 */     return "" + this.value;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.DateTimeSyntax
 * JD-Core Version:    0.6.2
 */