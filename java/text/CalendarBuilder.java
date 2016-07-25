/*     */ package java.text;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ 
/*     */ class CalendarBuilder
/*     */ {
/*     */   private static final int UNSET = 0;
/*     */   private static final int COMPUTED = 1;
/*     */   private static final int MINIMUM_USER_STAMP = 2;
/*     */   private static final int MAX_FIELD = 18;
/*     */   public static final int WEEK_YEAR = 17;
/*     */   public static final int ISO_DAY_OF_WEEK = 1000;
/*     */   private final int[] field;
/*     */   private int nextStamp;
/*     */   private int maxFieldIndex;
/*     */ 
/*     */   CalendarBuilder()
/*     */   {
/*  63 */     this.field = new int[36];
/*  64 */     this.nextStamp = 2;
/*  65 */     this.maxFieldIndex = -1;
/*     */   }
/*     */ 
/*     */   CalendarBuilder set(int paramInt1, int paramInt2) {
/*  69 */     if (paramInt1 == 1000) {
/*  70 */       paramInt1 = 7;
/*  71 */       paramInt2 = toCalendarDayOfWeek(paramInt2);
/*     */     }
/*  73 */     this.field[paramInt1] = (this.nextStamp++);
/*  74 */     this.field[(18 + paramInt1)] = paramInt2;
/*  75 */     if ((paramInt1 > this.maxFieldIndex) && (paramInt1 < 17)) {
/*  76 */       this.maxFieldIndex = paramInt1;
/*     */     }
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */   CalendarBuilder addYear(int paramInt) {
/*  82 */     this.field[19] += paramInt;
/*  83 */     this.field[35] += paramInt;
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */   boolean isSet(int paramInt) {
/*  88 */     if (paramInt == 1000) {
/*  89 */       paramInt = 7;
/*     */     }
/*  91 */     return this.field[paramInt] > 0;
/*     */   }
/*     */ 
/*     */   CalendarBuilder clear(int paramInt) {
/*  95 */     if (paramInt == 1000) {
/*  96 */       paramInt = 7;
/*     */     }
/*  98 */     this.field[paramInt] = 0;
/*  99 */     this.field[(18 + paramInt)] = 0;
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   Calendar establish(Calendar paramCalendar) {
/* 104 */     int i = (isSet(17)) && (this.field[17] > this.field[1]) ? 1 : 0;
/*     */ 
/* 106 */     if ((i != 0) && (!paramCalendar.isWeekDateSupported()))
/*     */     {
/* 108 */       if (!isSet(1)) {
/* 109 */         set(1, this.field[35]);
/*     */       }
/* 111 */       i = 0;
/*     */     }
/*     */ 
/* 114 */     paramCalendar.clear();
/*     */     int k;
/* 117 */     for (int j = 2; j < this.nextStamp; j++) {
/* 118 */       for (k = 0; k <= this.maxFieldIndex; k++) {
/* 119 */         if (this.field[k] == j) {
/* 120 */           paramCalendar.set(k, this.field[(18 + k)]);
/* 121 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 126 */     if (i != 0) {
/* 127 */       j = isSet(3) ? this.field[21] : 1;
/* 128 */       k = isSet(7) ? this.field[25] : paramCalendar.getFirstDayOfWeek();
/*     */ 
/* 130 */       if ((!isValidDayOfWeek(k)) && (paramCalendar.isLenient())) {
/* 131 */         if (k >= 8) {
/* 132 */           k--;
/* 133 */           j += k / 7;
/* 134 */           k = k % 7 + 1;
/*     */         } else {
/* 136 */           while (k <= 0) {
/* 137 */             k += 7;
/* 138 */             j--;
/*     */           }
/*     */         }
/* 141 */         k = toCalendarDayOfWeek(k);
/*     */       }
/* 143 */       paramCalendar.setWeekDate(this.field[35], j, k);
/*     */     }
/* 145 */     return paramCalendar;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 149 */     StringBuilder localStringBuilder = new StringBuilder();
/* 150 */     localStringBuilder.append("CalendarBuilder:[");
/* 151 */     for (int i = 0; i < this.field.length; i++) {
/* 152 */       if (isSet(i)) {
/* 153 */         localStringBuilder.append(i).append('=').append(this.field[(18 + i)]).append(',');
/*     */       }
/*     */     }
/* 156 */     i = localStringBuilder.length() - 1;
/* 157 */     if (localStringBuilder.charAt(i) == ',') {
/* 158 */       localStringBuilder.setLength(i);
/*     */     }
/* 160 */     localStringBuilder.append(']');
/* 161 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   static int toISODayOfWeek(int paramInt) {
/* 165 */     return paramInt == 1 ? 7 : paramInt - 1;
/*     */   }
/*     */ 
/*     */   static int toCalendarDayOfWeek(int paramInt) {
/* 169 */     if (!isValidDayOfWeek(paramInt))
/*     */     {
/* 171 */       return paramInt;
/*     */     }
/* 173 */     return paramInt == 7 ? 1 : paramInt + 1;
/*     */   }
/*     */ 
/*     */   static boolean isValidDayOfWeek(int paramInt) {
/* 177 */     return (paramInt > 0) && (paramInt <= 7);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.CalendarBuilder
 * JD-Core Version:    0.6.2
 */