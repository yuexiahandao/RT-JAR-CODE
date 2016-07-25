/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ public class SnmpTimeticks extends SnmpUnsignedInt
/*     */ {
/*     */   static final String name = "TimeTicks";
/*     */   private static final long serialVersionUID = -5486435222360030630L;
/*     */ 
/*     */   public SnmpTimeticks(int paramInt)
/*     */     throws IllegalArgumentException
/*     */   {
/*  30 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public SnmpTimeticks(Integer paramInteger)
/*     */     throws IllegalArgumentException
/*     */   {
/*  40 */     super(paramInteger);
/*     */   }
/*     */ 
/*     */   public SnmpTimeticks(long paramLong)
/*     */     throws IllegalArgumentException
/*     */   {
/*  53 */     super(paramLong > 0L ? paramLong & 0xFFFFFFFF : paramLong);
/*     */   }
/*     */ 
/*     */   public SnmpTimeticks(Long paramLong)
/*     */     throws IllegalArgumentException
/*     */   {
/*  66 */     this(paramLong.longValue());
/*     */   }
/*     */ 
/*     */   public static final String printTimeTicks(long paramLong)
/*     */   {
/*  79 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/*  81 */     paramLong /= 100L;
/*  82 */     int m = (int)(paramLong / 86400L);
/*  83 */     paramLong %= 86400L;
/*     */ 
/*  85 */     int k = (int)(paramLong / 3600L);
/*  86 */     paramLong %= 3600L;
/*     */ 
/*  88 */     int j = (int)(paramLong / 60L);
/*  89 */     int i = (int)(paramLong % 60L);
/*     */ 
/*  91 */     if (m == 0) {
/*  92 */       localStringBuffer.append(k + ":" + j + ":" + i);
/*  93 */       return localStringBuffer.toString();
/*     */     }
/*  95 */     if (m == 1)
/*  96 */       localStringBuffer.append("1 day ");
/*     */     else {
/*  98 */       localStringBuffer.append(m + " days ");
/*     */     }
/* 100 */     localStringBuffer.append(k + ":" + j + ":" + i);
/* 101 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 111 */     return printTimeTicks(this.value);
/*     */   }
/*     */ 
/*     */   public final String getTypeName()
/*     */   {
/* 119 */     return "TimeTicks";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpTimeticks
 * JD-Core Version:    0.6.2
 */