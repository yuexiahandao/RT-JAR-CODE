/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.DateFormat.Field;
/*     */ import java.text.Format;
/*     */ import java.text.ParseException;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class DateFormatter extends InternationalFormatter
/*     */ {
/*     */   public DateFormatter()
/*     */   {
/*  56 */     this(DateFormat.getDateInstance());
/*     */   }
/*     */ 
/*     */   public DateFormatter(DateFormat paramDateFormat)
/*     */   {
/*  66 */     super(paramDateFormat);
/*  67 */     setFormat(paramDateFormat);
/*     */   }
/*     */ 
/*     */   public void setFormat(DateFormat paramDateFormat)
/*     */   {
/*  81 */     super.setFormat(paramDateFormat);
/*     */   }
/*     */ 
/*     */   private Calendar getCalendar()
/*     */   {
/*  90 */     Format localFormat = getFormat();
/*     */ 
/*  92 */     if ((localFormat instanceof DateFormat)) {
/*  93 */       return ((DateFormat)localFormat).getCalendar();
/*     */     }
/*  95 */     return Calendar.getInstance();
/*     */   }
/*     */ 
/*     */   boolean getSupportsIncrement()
/*     */   {
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   Object getAdjustField(int paramInt, Map paramMap)
/*     */   {
/* 111 */     Iterator localIterator = paramMap.keySet().iterator();
/*     */ 
/* 113 */     while (localIterator.hasNext()) {
/* 114 */       Object localObject = localIterator.next();
/*     */ 
/* 116 */       if (((localObject instanceof DateFormat.Field)) && ((localObject == DateFormat.Field.HOUR1) || (((DateFormat.Field)localObject).getCalendarField() != -1)))
/*     */       {
/* 119 */         return localObject;
/*     */       }
/*     */     }
/* 122 */     return null;
/*     */   }
/*     */ 
/*     */   Object adjustValue(Object paramObject1, Map paramMap, Object paramObject2, int paramInt)
/*     */     throws BadLocationException, ParseException
/*     */   {
/* 132 */     if (paramObject2 != null)
/*     */     {
/* 137 */       if (paramObject2 == DateFormat.Field.HOUR1) {
/* 138 */         paramObject2 = DateFormat.Field.HOUR0;
/*     */       }
/* 140 */       int i = ((DateFormat.Field)paramObject2).getCalendarField();
/*     */ 
/* 142 */       Calendar localCalendar = getCalendar();
/*     */ 
/* 144 */       if (localCalendar != null) {
/* 145 */         localCalendar.setTime((Date)paramObject1);
/*     */ 
/* 147 */         int j = localCalendar.get(i);
/*     */         try
/*     */         {
/* 150 */           localCalendar.add(i, paramInt);
/* 151 */           paramObject1 = localCalendar.getTime();
/*     */         } catch (Throwable localThrowable) {
/* 153 */           paramObject1 = null;
/*     */         }
/* 155 */         return paramObject1;
/*     */       }
/*     */     }
/* 158 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.DateFormatter
 * JD-Core Version:    0.6.2
 */