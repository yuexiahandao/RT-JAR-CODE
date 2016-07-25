/*     */ package javax.swing;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public abstract class RowFilter<M, I>
/*     */ {
/*     */   private static void checkIndices(int[] paramArrayOfInt)
/*     */   {
/* 135 */     for (int i = paramArrayOfInt.length - 1; i >= 0; i--)
/* 136 */       if (paramArrayOfInt[i] < 0)
/* 137 */         throw new IllegalArgumentException("Index must be >= 0");
/*     */   }
/*     */ 
/*     */   public static <M, I> RowFilter<M, I> regexFilter(String paramString, int[] paramArrayOfInt)
/*     */   {
/* 175 */     return new RegexFilter(Pattern.compile(paramString), paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public static <M, I> RowFilter<M, I> dateFilter(ComparisonType paramComparisonType, Date paramDate, int[] paramArrayOfInt)
/*     */   {
/* 203 */     return new DateFilter(paramComparisonType, paramDate.getTime(), paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public static <M, I> RowFilter<M, I> numberFilter(ComparisonType paramComparisonType, Number paramNumber, int[] paramArrayOfInt)
/*     */   {
/* 226 */     return new NumberFilter(paramComparisonType, paramNumber, paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public static <M, I> RowFilter<M, I> orFilter(Iterable<? extends RowFilter<? super M, ? super I>> paramIterable)
/*     */   {
/* 252 */     return new OrFilter(paramIterable);
/*     */   }
/*     */ 
/*     */   public static <M, I> RowFilter<M, I> andFilter(Iterable<? extends RowFilter<? super M, ? super I>> paramIterable)
/*     */   {
/* 278 */     return new AndFilter(paramIterable);
/*     */   }
/*     */ 
/*     */   public static <M, I> RowFilter<M, I> notFilter(RowFilter<M, I> paramRowFilter)
/*     */   {
/* 291 */     return new NotFilter(paramRowFilter);
/*     */   }
/*     */ 
/*     */   public abstract boolean include(Entry<? extends M, ? extends I> paramEntry);
/*     */ 
/*     */   private static class AndFilter<M, I> extends RowFilter.OrFilter<M, I>
/*     */   {
/*     */     AndFilter(Iterable<? extends RowFilter<? super M, ? super I>> paramIterable)
/*     */     {
/* 580 */       super();
/*     */     }
/*     */ 
/*     */     public boolean include(RowFilter.Entry<? extends M, ? extends I> paramEntry) {
/* 584 */       for (RowFilter localRowFilter : this.filters) {
/* 585 */         if (!localRowFilter.include(paramEntry)) {
/* 586 */           return false;
/*     */         }
/*     */       }
/* 589 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum ComparisonType
/*     */   {
/* 109 */     BEFORE, 
/*     */ 
/* 115 */     AFTER, 
/*     */ 
/* 121 */     EQUAL, 
/*     */ 
/* 127 */     NOT_EQUAL;
/*     */   }
/*     */ 
/*     */   private static class DateFilter extends RowFilter.GeneralFilter
/*     */   {
/*     */     private long date;
/*     */     private RowFilter.ComparisonType type;
/*     */ 
/*     */     DateFilter(RowFilter.ComparisonType paramComparisonType, long paramLong, int[] paramArrayOfInt)
/*     */     {
/* 458 */       super();
/* 459 */       if (paramComparisonType == null) {
/* 460 */         throw new IllegalArgumentException("type must be non-null");
/*     */       }
/* 462 */       this.type = paramComparisonType;
/* 463 */       this.date = paramLong;
/*     */     }
/*     */ 
/*     */     protected boolean include(RowFilter.Entry<? extends Object, ? extends Object> paramEntry, int paramInt)
/*     */     {
/* 468 */       Object localObject = paramEntry.getValue(paramInt);
/*     */ 
/* 470 */       if ((localObject instanceof Date)) {
/* 471 */         long l = ((Date)localObject).getTime();
/* 472 */         switch (RowFilter.1.$SwitchMap$javax$swing$RowFilter$ComparisonType[this.type.ordinal()]) {
/*     */         case 1:
/* 474 */           return l < this.date;
/*     */         case 2:
/* 476 */           return l > this.date;
/*     */         case 3:
/* 478 */           return l == this.date;
/*     */         case 4:
/* 480 */           return l != this.date;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 485 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class Entry<M, I>
/*     */   {
/*     */     public abstract M getModel();
/*     */ 
/*     */     public abstract int getValueCount();
/*     */ 
/*     */     public abstract Object getValue(int paramInt);
/*     */ 
/*     */     public String getStringValue(int paramInt)
/*     */     {
/* 383 */       Object localObject = getValue(paramInt);
/* 384 */       return localObject == null ? "" : localObject.toString();
/*     */     }
/*     */ 
/*     */     public abstract I getIdentifier();
/*     */   }
/*     */ 
/*     */   private static abstract class GeneralFilter extends RowFilter<Object, Object>
/*     */   {
/*     */     private int[] columns;
/*     */ 
/*     */     GeneralFilter(int[] paramArrayOfInt)
/*     */     {
/* 403 */       RowFilter.checkIndices(paramArrayOfInt);
/* 404 */       this.columns = paramArrayOfInt;
/*     */     }
/*     */ 
/*     */     public boolean include(RowFilter.Entry<? extends Object, ? extends Object> paramEntry) {
/* 408 */       int i = paramEntry.getValueCount();
/* 409 */       if (this.columns.length > 0) {
/* 410 */         for (int j = this.columns.length - 1; j >= 0; j--) {
/* 411 */           int k = this.columns[j];
/* 412 */           if ((k < i) && 
/* 413 */             (include(paramEntry, k)))
/* 414 */             return true;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*     */         do {
/* 420 */           i--; if (i < 0) break; 
/* 421 */         }while (!include(paramEntry, i));
/* 422 */         return true;
/*     */       }
/*     */ 
/* 426 */       return false;
/*     */     }
/*     */ 
/*     */     protected abstract boolean include(RowFilter.Entry<? extends Object, ? extends Object> paramEntry, int paramInt);
/*     */   }
/*     */ 
/*     */   private static class NotFilter<M, I> extends RowFilter<M, I>
/*     */   {
/*     */     private RowFilter<M, I> filter;
/*     */ 
/*     */     NotFilter(RowFilter<M, I> paramRowFilter)
/*     */     {
/* 598 */       if (paramRowFilter == null) {
/* 599 */         throw new IllegalArgumentException("filter must be non-null");
/*     */       }
/*     */ 
/* 602 */       this.filter = paramRowFilter;
/*     */     }
/*     */ 
/*     */     public boolean include(RowFilter.Entry<? extends M, ? extends I> paramEntry) {
/* 606 */       return !this.filter.include(paramEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class NumberFilter extends RowFilter.GeneralFilter
/*     */   {
/*     */     private boolean isComparable;
/*     */     private Number number;
/*     */     private RowFilter.ComparisonType type;
/*     */ 
/*     */     NumberFilter(RowFilter.ComparisonType paramComparisonType, Number paramNumber, int[] paramArrayOfInt)
/*     */     {
/* 498 */       super();
/* 499 */       if ((paramComparisonType == null) || (paramNumber == null)) {
/* 500 */         throw new IllegalArgumentException("type and number must be non-null");
/*     */       }
/*     */ 
/* 503 */       this.type = paramComparisonType;
/* 504 */       this.number = paramNumber;
/* 505 */       this.isComparable = (paramNumber instanceof Comparable);
/*     */     }
/*     */ 
/*     */     protected boolean include(RowFilter.Entry<? extends Object, ? extends Object> paramEntry, int paramInt)
/*     */     {
/* 511 */       Object localObject = paramEntry.getValue(paramInt);
/*     */ 
/* 513 */       if ((localObject instanceof Number)) {
/* 514 */         int i = 1;
/*     */ 
/* 516 */         Class localClass = localObject.getClass();
/*     */         int j;
/* 517 */         if ((this.number.getClass() == localClass) && (this.isComparable)) {
/* 518 */           j = ((Comparable)this.number).compareTo(localObject);
/*     */         }
/*     */         else {
/* 521 */           j = longCompare((Number)localObject);
/*     */         }
/* 523 */         switch (RowFilter.1.$SwitchMap$javax$swing$RowFilter$ComparisonType[this.type.ordinal()]) {
/*     */         case 1:
/* 525 */           return j > 0;
/*     */         case 2:
/* 527 */           return j < 0;
/*     */         case 3:
/* 529 */           return j == 0;
/*     */         case 4:
/* 531 */           return j != 0;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 536 */       return false;
/*     */     }
/*     */ 
/*     */     private int longCompare(Number paramNumber) {
/* 540 */       long l = this.number.longValue() - paramNumber.longValue();
/*     */ 
/* 542 */       if (l < 0L) {
/* 543 */         return -1;
/*     */       }
/* 545 */       if (l > 0L) {
/* 546 */         return 1;
/*     */       }
/* 548 */       return 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class OrFilter<M, I> extends RowFilter<M, I>
/*     */   {
/*     */     List<RowFilter<? super M, ? super I>> filters;
/*     */ 
/*     */     OrFilter(Iterable<? extends RowFilter<? super M, ? super I>> paramIterable) {
/* 557 */       this.filters = new ArrayList();
/* 558 */       for (RowFilter localRowFilter : paramIterable) {
/* 559 */         if (localRowFilter == null) {
/* 560 */           throw new IllegalArgumentException("Filter must be non-null");
/*     */         }
/*     */ 
/* 563 */         this.filters.add(localRowFilter);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean include(RowFilter.Entry<? extends M, ? extends I> paramEntry) {
/* 568 */       for (RowFilter localRowFilter : this.filters) {
/* 569 */         if (localRowFilter.include(paramEntry)) {
/* 570 */           return true;
/*     */         }
/*     */       }
/* 573 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class RegexFilter extends RowFilter.GeneralFilter
/*     */   {
/*     */     private Matcher matcher;
/*     */ 
/*     */     RegexFilter(Pattern paramPattern, int[] paramArrayOfInt)
/*     */     {
/* 438 */       super();
/* 439 */       if (paramPattern == null) {
/* 440 */         throw new IllegalArgumentException("Pattern must be non-null");
/*     */       }
/* 442 */       this.matcher = paramPattern.matcher("");
/*     */     }
/*     */ 
/*     */     protected boolean include(RowFilter.Entry<? extends Object, ? extends Object> paramEntry, int paramInt)
/*     */     {
/* 447 */       this.matcher.reset(paramEntry.getStringValue(paramInt));
/* 448 */       return this.matcher.find();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.RowFilter
 * JD-Core Version:    0.6.2
 */