/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ public class SnmpStatusException extends Exception
/*     */   implements SnmpDefinitions
/*     */ {
/*     */   private static final long serialVersionUID = 5809485694133115675L;
/*     */   public static final int noSuchName = 2;
/*     */   public static final int badValue = 3;
/*     */   public static final int readOnly = 4;
/*     */   public static final int noAccess = 6;
/*     */   public static final int noSuchInstance = 224;
/*     */   public static final int noSuchObject = 225;
/* 137 */   private int errorStatus = 0;
/*     */ 
/* 144 */   private int errorIndex = -1;
/*     */ 
/*     */   public SnmpStatusException(int paramInt)
/*     */   {
/*  79 */     this.errorStatus = paramInt;
/*     */   }
/*     */ 
/*     */   public SnmpStatusException(int paramInt1, int paramInt2)
/*     */   {
/*  88 */     this.errorStatus = paramInt1;
/*  89 */     this.errorIndex = paramInt2;
/*     */   }
/*     */ 
/*     */   public SnmpStatusException(String paramString)
/*     */   {
/*  98 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SnmpStatusException(SnmpStatusException paramSnmpStatusException, int paramInt)
/*     */   {
/* 107 */     super(paramSnmpStatusException.getMessage());
/* 108 */     this.errorStatus = paramSnmpStatusException.errorStatus;
/* 109 */     this.errorIndex = paramInt;
/*     */   }
/*     */ 
/*     */   public int getStatus()
/*     */   {
/* 117 */     return this.errorStatus;
/*     */   }
/*     */ 
/*     */   public int getErrorIndex()
/*     */   {
/* 126 */     return this.errorIndex;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpStatusException
 * JD-Core Version:    0.6.2
 */