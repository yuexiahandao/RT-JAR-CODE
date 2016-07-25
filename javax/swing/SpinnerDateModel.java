/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class SpinnerDateModel extends AbstractSpinnerModel
/*     */   implements Serializable
/*     */ {
/*     */   private Comparable start;
/*     */   private Comparable end;
/*     */   private Calendar value;
/*     */   private int calendarField;
/*     */ 
/*     */   private boolean calendarFieldOK(int paramInt)
/*     */   {
/*  97 */     switch (paramInt) {
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 14:
/* 113 */       return true;
/*     */     }
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */   public SpinnerDateModel(Date paramDate, Comparable paramComparable1, Comparable paramComparable2, int paramInt)
/*     */   {
/* 176 */     if (paramDate == null) {
/* 177 */       throw new IllegalArgumentException("value is null");
/*     */     }
/* 179 */     if (!calendarFieldOK(paramInt)) {
/* 180 */       throw new IllegalArgumentException("invalid calendarField");
/*     */     }
/* 182 */     if (((paramComparable1 != null) && (paramComparable1.compareTo(paramDate) > 0)) || ((paramComparable2 != null) && (paramComparable2.compareTo(paramDate) < 0)))
/*     */     {
/* 184 */       throw new IllegalArgumentException("(start <= value <= end) is false");
/*     */     }
/* 186 */     this.value = Calendar.getInstance();
/* 187 */     this.start = paramComparable1;
/* 188 */     this.end = paramComparable2;
/* 189 */     this.calendarField = paramInt;
/*     */ 
/* 191 */     this.value.setTime(paramDate);
/*     */   }
/*     */ 
/*     */   public SpinnerDateModel()
/*     */   {
/* 202 */     this(new Date(), null, null, 5);
/*     */   }
/*     */ 
/*     */   public void setStart(Comparable paramComparable)
/*     */   {
/* 244 */     if (paramComparable == null ? this.start != null : !paramComparable.equals(this.start)) {
/* 245 */       this.start = paramComparable;
/* 246 */       fireStateChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Comparable getStart()
/*     */   {
/* 258 */     return this.start;
/*     */   }
/*     */ 
/*     */   public void setEnd(Comparable paramComparable)
/*     */   {
/* 285 */     if (paramComparable == null ? this.end != null : !paramComparable.equals(this.end)) {
/* 286 */       this.end = paramComparable;
/* 287 */       fireStateChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Comparable getEnd()
/*     */   {
/* 299 */     return this.end;
/*     */   }
/*     */ 
/*     */   public void setCalendarField(int paramInt)
/*     */   {
/* 346 */     if (!calendarFieldOK(paramInt)) {
/* 347 */       throw new IllegalArgumentException("invalid calendarField");
/*     */     }
/* 349 */     if (paramInt != this.calendarField) {
/* 350 */       this.calendarField = paramInt;
/* 351 */       fireStateChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getCalendarField()
/*     */   {
/* 364 */     return this.calendarField;
/*     */   }
/*     */ 
/*     */   public Object getNextValue()
/*     */   {
/* 380 */     Calendar localCalendar = Calendar.getInstance();
/* 381 */     localCalendar.setTime(this.value.getTime());
/* 382 */     localCalendar.add(this.calendarField, 1);
/* 383 */     Date localDate = localCalendar.getTime();
/* 384 */     return (this.end == null) || (this.end.compareTo(localDate) >= 0) ? localDate : null;
/*     */   }
/*     */ 
/*     */   public Object getPreviousValue()
/*     */   {
/* 401 */     Calendar localCalendar = Calendar.getInstance();
/* 402 */     localCalendar.setTime(this.value.getTime());
/* 403 */     localCalendar.add(this.calendarField, -1);
/* 404 */     Date localDate = localCalendar.getTime();
/* 405 */     return (this.start == null) || (this.start.compareTo(localDate) <= 0) ? localDate : null;
/*     */   }
/*     */ 
/*     */   public Date getDate()
/*     */   {
/* 417 */     return this.value.getTime();
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 429 */     return this.value.getTime();
/*     */   }
/*     */ 
/*     */   public void setValue(Object paramObject)
/*     */   {
/* 456 */     if ((paramObject == null) || (!(paramObject instanceof Date))) {
/* 457 */       throw new IllegalArgumentException("illegal value");
/*     */     }
/* 459 */     if (!paramObject.equals(this.value.getTime())) {
/* 460 */       this.value.setTime((Date)paramObject);
/* 461 */       fireStateChanged();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.SpinnerDateModel
 * JD-Core Version:    0.6.2
 */