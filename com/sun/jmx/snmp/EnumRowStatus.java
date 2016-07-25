/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class EnumRowStatus extends Enumerated
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8966519271130162420L;
/*     */   public static final int unspecified = 0;
/*     */   public static final int active = 1;
/*     */   public static final int notInService = 2;
/*     */   public static final int notReady = 3;
/*     */   public static final int createAndGo = 4;
/*     */   public static final int createAndWait = 5;
/*     */   public static final int destroy = 6;
/* 286 */   static final Hashtable<Integer, String> intTable = new Hashtable();
/*     */ 
/* 288 */   static final Hashtable<String, Integer> stringTable = new Hashtable();
/*     */ 
/*     */   public EnumRowStatus(int paramInt)
/*     */     throws IllegalArgumentException
/*     */   {
/* 146 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public EnumRowStatus(Enumerated paramEnumerated)
/*     */     throws IllegalArgumentException
/*     */   {
/* 158 */     this(paramEnumerated.intValue());
/*     */   }
/*     */ 
/*     */   public EnumRowStatus(long paramLong)
/*     */     throws IllegalArgumentException
/*     */   {
/* 170 */     this((int)paramLong);
/*     */   }
/*     */ 
/*     */   public EnumRowStatus(Integer paramInteger)
/*     */     throws IllegalArgumentException
/*     */   {
/* 182 */     super(paramInteger);
/*     */   }
/*     */ 
/*     */   public EnumRowStatus(Long paramLong)
/*     */     throws IllegalArgumentException
/*     */   {
/* 194 */     this(paramLong.longValue());
/*     */   }
/*     */ 
/*     */   public EnumRowStatus()
/*     */     throws IllegalArgumentException
/*     */   {
/* 202 */     this(0);
/*     */   }
/*     */ 
/*     */   public EnumRowStatus(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 214 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public EnumRowStatus(SnmpInt paramSnmpInt)
/*     */     throws IllegalArgumentException
/*     */   {
/* 226 */     this(paramSnmpInt.intValue());
/*     */   }
/*     */ 
/*     */   public SnmpInt toSnmpValue()
/*     */     throws IllegalArgumentException
/*     */   {
/* 238 */     if (this.value == 0) {
/* 239 */       throw new IllegalArgumentException("`unspecified' is not a valid SNMP value.");
/*     */     }
/* 241 */     return new SnmpInt(this.value);
/*     */   }
/*     */ 
/*     */   public static boolean isValidValue(int paramInt)
/*     */   {
/* 259 */     if (paramInt < 0) return false;
/* 260 */     if (paramInt > 6) return false;
/* 261 */     return true;
/*     */   }
/*     */ 
/*     */   protected Hashtable getIntTable()
/*     */   {
/* 267 */     return getRSIntTable();
/*     */   }
/*     */ 
/*     */   protected Hashtable getStringTable()
/*     */   {
/* 273 */     return getRSStringTable();
/*     */   }
/*     */ 
/*     */   static final Hashtable getRSIntTable() {
/* 277 */     return intTable;
/*     */   }
/*     */ 
/*     */   static final Hashtable getRSStringTable() {
/* 281 */     return stringTable;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 291 */     intTable.put(new Integer(0), "unspecified");
/* 292 */     intTable.put(new Integer(3), "notReady");
/* 293 */     intTable.put(new Integer(6), "destroy");
/* 294 */     intTable.put(new Integer(2), "notInService");
/* 295 */     intTable.put(new Integer(5), "createAndWait");
/* 296 */     intTable.put(new Integer(1), "active");
/* 297 */     intTable.put(new Integer(4), "createAndGo");
/* 298 */     stringTable.put("unspecified", new Integer(0));
/* 299 */     stringTable.put("notReady", new Integer(3));
/* 300 */     stringTable.put("destroy", new Integer(6));
/* 301 */     stringTable.put("notInService", new Integer(2));
/* 302 */     stringTable.put("createAndWait", new Integer(5));
/* 303 */     stringTable.put("active", new Integer(1));
/* 304 */     stringTable.put("createAndGo", new Integer(4));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.EnumRowStatus
 * JD-Core Version:    0.6.2
 */