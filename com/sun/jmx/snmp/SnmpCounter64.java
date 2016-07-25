/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ public class SnmpCounter64 extends SnmpValue
/*     */ {
/*     */   private static final long serialVersionUID = 8784850650494679937L;
/*     */   static final String name = "Counter64";
/* 213 */   private long value = 0L;
/*     */ 
/*     */   public SnmpCounter64(long paramLong)
/*     */     throws IllegalArgumentException
/*     */   {
/*  56 */     if ((paramLong < 0L) || (paramLong > 9223372036854775807L)) {
/*  57 */       throw new IllegalArgumentException();
/*     */     }
/*  59 */     this.value = paramLong;
/*     */   }
/*     */ 
/*     */   public SnmpCounter64(Long paramLong)
/*     */     throws IllegalArgumentException
/*     */   {
/*  69 */     this(paramLong.longValue());
/*     */   }
/*     */ 
/*     */   public long longValue()
/*     */   {
/*  79 */     return this.value;
/*     */   }
/*     */ 
/*     */   public Long toLong()
/*     */   {
/*  87 */     return new Long(this.value);
/*     */   }
/*     */ 
/*     */   public int intValue()
/*     */   {
/*  95 */     return (int)this.value;
/*     */   }
/*     */ 
/*     */   public Integer toInteger()
/*     */   {
/* 103 */     return new Integer((int)this.value);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 111 */     return String.valueOf(this.value);
/*     */   }
/*     */ 
/*     */   public SnmpOid toOid()
/*     */   {
/* 119 */     return new SnmpOid(this.value);
/*     */   }
/*     */ 
/*     */   public static SnmpOid toOid(long[] paramArrayOfLong, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/*     */     try
/*     */     {
/* 133 */       return new SnmpOid(paramArrayOfLong[paramInt]);
/*     */     } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*     */     }
/* 136 */     throw new SnmpStatusException(2);
/*     */   }
/*     */ 
/*     */   public static int nextOid(long[] paramArrayOfLong, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 150 */     if (paramInt >= paramArrayOfLong.length) {
/* 151 */       throw new SnmpStatusException(2);
/*     */     }
/*     */ 
/* 154 */     return paramInt + 1;
/*     */   }
/*     */ 
/*     */   public static void appendToOid(SnmpOid paramSnmpOid1, SnmpOid paramSnmpOid2)
/*     */   {
/* 164 */     if (paramSnmpOid1.getLength() != 1) {
/* 165 */       throw new IllegalArgumentException();
/*     */     }
/* 167 */     paramSnmpOid2.append(paramSnmpOid1);
/*     */   }
/*     */ 
/*     */   public final synchronized SnmpValue duplicate()
/*     */   {
/* 176 */     return (SnmpValue)clone();
/*     */   }
/*     */ 
/*     */   public final synchronized Object clone()
/*     */   {
/* 184 */     SnmpCounter64 localSnmpCounter64 = null;
/*     */     try {
/* 186 */       localSnmpCounter64 = (SnmpCounter64)super.clone();
/* 187 */       localSnmpCounter64.value = this.value;
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 189 */       throw new InternalError();
/*     */     }
/* 191 */     return localSnmpCounter64;
/*     */   }
/*     */ 
/*     */   public final String getTypeName()
/*     */   {
/* 199 */     return "Counter64";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpCounter64
 * JD-Core Version:    0.6.2
 */