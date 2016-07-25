/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class Timestamp
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -242456119149401823L;
/*     */   private long sysUpTime;
/*     */   private long crtime;
/*  42 */   private Date dateCache = null;
/*     */ 
/*  47 */   private SnmpTimeticks uptimeCache = null;
/*     */ 
/*     */   public Timestamp()
/*     */   {
/*  58 */     this.crtime = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   public Timestamp(long paramLong1, long paramLong2)
/*     */   {
/*  68 */     this.sysUpTime = paramLong1;
/*  69 */     this.crtime = paramLong2;
/*     */   }
/*     */ 
/*     */   public Timestamp(long paramLong)
/*     */   {
/*  78 */     this.sysUpTime = paramLong;
/*  79 */     this.crtime = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   public final synchronized SnmpTimeticks getTimeTicks()
/*     */   {
/*  91 */     if (this.uptimeCache == null)
/*  92 */       this.uptimeCache = new SnmpTimeticks((int)this.sysUpTime);
/*  93 */     return this.uptimeCache;
/*     */   }
/*     */ 
/*     */   public final long getSysUpTime()
/*     */   {
/* 102 */     return this.sysUpTime;
/*     */   }
/*     */ 
/*     */   public final synchronized Date getDate()
/*     */   {
/* 110 */     if (this.dateCache == null)
/* 111 */       this.dateCache = new Date(this.crtime);
/* 112 */     return this.dateCache;
/*     */   }
/*     */ 
/*     */   public final long getDateTime()
/*     */   {
/* 120 */     return this.crtime;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 128 */     StringBuffer localStringBuffer = new StringBuffer();
/* 129 */     localStringBuffer.append("{SysUpTime = " + SnmpTimeticks.printTimeTicks(this.sysUpTime));
/* 130 */     localStringBuffer.append("} {Timestamp = " + getDate().toString() + "}");
/* 131 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.Timestamp
 * JD-Core Version:    0.6.2
 */