/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.misc.JavaAWTAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ public class SnmpOid extends SnmpValue
/*     */ {
/* 632 */   protected long[] components = null;
/*     */ 
/* 638 */   protected int componentCount = 0;
/*     */   static final String name = "Object Identifier";
/* 649 */   private static SnmpOidTable meta = null;
/*     */   static final long serialVersionUID = 8956237235607885096L;
/*     */ 
/*     */   public SnmpOid()
/*     */   {
/*  34 */     this.components = new long[15];
/*  35 */     this.componentCount = 0;
/*     */   }
/*     */ 
/*     */   public SnmpOid(long[] paramArrayOfLong)
/*     */   {
/*  43 */     this.components = ((long[])paramArrayOfLong.clone());
/*  44 */     this.componentCount = this.components.length;
/*     */   }
/*     */ 
/*     */   public SnmpOid(long paramLong)
/*     */   {
/*  53 */     this.components = new long[1];
/*  54 */     this.components[0] = paramLong;
/*  55 */     this.componentCount = this.components.length;
/*     */   }
/*     */ 
/*     */   public SnmpOid(long paramLong1, long paramLong2, long paramLong3, long paramLong4)
/*     */   {
/*  67 */     this.components = new long[4];
/*  68 */     this.components[0] = paramLong1;
/*  69 */     this.components[1] = paramLong2;
/*  70 */     this.components[2] = paramLong3;
/*  71 */     this.components[3] = paramLong4;
/*  72 */     this.componentCount = this.components.length;
/*     */   }
/*     */ 
/*     */   public SnmpOid(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/*  86 */     String str = paramString;
/*     */ 
/*  88 */     if (!paramString.startsWith(".")) {
/*     */       try {
/*  90 */         str = resolveVarName(paramString);
/*     */       } catch (SnmpStatusException localSnmpStatusException) {
/*  92 */         throw new IllegalArgumentException(localSnmpStatusException.getMessage());
/*     */       }
/*     */     }
/*     */ 
/*  96 */     StringTokenizer localStringTokenizer = new StringTokenizer(str, ".", false);
/*  97 */     this.componentCount = localStringTokenizer.countTokens();
/*     */ 
/* 101 */     if (this.componentCount == 0) {
/* 102 */       this.components = new long[15];
/*     */     } else {
/* 104 */       this.components = new long[this.componentCount];
/*     */       try {
/* 106 */         for (int i = 0; i < this.componentCount; i++)
/*     */           try {
/* 108 */             this.components[i] = Long.parseLong(localStringTokenizer.nextToken());
/*     */           }
/*     */           catch (NoSuchElementException localNoSuchElementException) {
/*     */           }
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException) {
/* 114 */         throw new IllegalArgumentException(paramString);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 126 */     return this.componentCount;
/*     */   }
/*     */ 
/*     */   public long[] longValue()
/*     */   {
/* 134 */     long[] arrayOfLong = new long[this.componentCount];
/* 135 */     System.arraycopy(this.components, 0, arrayOfLong, 0, this.componentCount);
/* 136 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   public final long[] longValue(boolean paramBoolean)
/*     */   {
/* 155 */     if (paramBoolean) return longValue();
/* 156 */     if (this.componentCount == this.components.length) return this.components;
/* 157 */     this.components = longValue();
/* 158 */     this.componentCount = this.components.length;
/* 159 */     return this.components;
/*     */   }
/*     */ 
/*     */   public final long getOidArc(int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/*     */     try
/*     */     {
/* 176 */       return this.components[paramInt]; } catch (Exception localException) {
/*     */     }
/* 178 */     throw new SnmpStatusException(6);
/*     */   }
/*     */ 
/*     */   public Long toLong()
/*     */   {
/* 187 */     if (this.componentCount != 1) {
/* 188 */       throw new IllegalArgumentException();
/*     */     }
/* 190 */     return new Long(this.components[0]);
/*     */   }
/*     */ 
/*     */   public Integer toInteger()
/*     */   {
/* 198 */     if ((this.componentCount != 1) || (this.components[0] > 2147483647L)) {
/* 199 */       throw new IllegalArgumentException();
/*     */     }
/* 201 */     return new Integer((int)this.components[0]);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 209 */     String str = "";
/* 210 */     if (this.componentCount >= 1) {
/* 211 */       for (int i = 0; i < this.componentCount - 1; i++) {
/* 212 */         str = str + this.components[i] + ".";
/*     */       }
/* 214 */       str = str + this.components[(this.componentCount - 1)];
/*     */     }
/* 216 */     return str;
/*     */   }
/*     */ 
/*     */   public Boolean toBoolean()
/*     */   {
/* 224 */     if ((this.componentCount != 1) && (this.components[0] != 1L) && (this.components[0] != 2L)) {
/* 225 */       throw new IllegalArgumentException();
/*     */     }
/* 227 */     return Boolean.valueOf(this.components[0] == 1L);
/*     */   }
/*     */ 
/*     */   public Byte[] toByte()
/*     */   {
/* 235 */     Byte[] arrayOfByte = new Byte[this.componentCount];
/* 236 */     for (int i = 0; i < this.componentCount; i++) {
/* 237 */       if (this.components[0] > 255L) {
/* 238 */         throw new IllegalArgumentException();
/*     */       }
/* 240 */       arrayOfByte[i] = new Byte((byte)(int)this.components[i]);
/*     */     }
/* 242 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public SnmpOid toOid()
/*     */   {
/* 250 */     long[] arrayOfLong = new long[this.componentCount];
/* 251 */     for (int i = 0; i < this.componentCount; i++) {
/* 252 */       arrayOfLong[i] = this.components[i];
/*     */     }
/* 254 */     return new SnmpOid(arrayOfLong);
/*     */   }
/*     */ 
/*     */   public static SnmpOid toOid(long[] paramArrayOfLong, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/*     */     try
/*     */     {
/* 268 */       if (paramArrayOfLong[paramInt] > 2147483647L) {
/* 269 */         throw new SnmpStatusException(2);
/*     */       }
/* 271 */       int i = (int)paramArrayOfLong[(paramInt++)];
/* 272 */       long[] arrayOfLong = new long[i];
/* 273 */       for (int j = 0; j < i; j++) {
/* 274 */         arrayOfLong[j] = paramArrayOfLong[(paramInt + j)];
/*     */       }
/* 276 */       return new SnmpOid(arrayOfLong);
/*     */     } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*     */     }
/* 279 */     throw new SnmpStatusException(2);
/*     */   }
/*     */ 
/*     */   public static int nextOid(long[] paramArrayOfLong, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/*     */     try
/*     */     {
/* 294 */       if (paramArrayOfLong[paramInt] > 2147483647L) {
/* 295 */         throw new SnmpStatusException(2);
/*     */       }
/* 297 */       int i = (int)paramArrayOfLong[(paramInt++)];
/* 298 */       paramInt += i;
/* 299 */       if (paramInt <= paramArrayOfLong.length) {
/* 300 */         return paramInt;
/*     */       }
/*     */ 
/* 303 */       throw new SnmpStatusException(2);
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*     */     }
/* 307 */     throw new SnmpStatusException(2);
/*     */   }
/*     */ 
/*     */   public static void appendToOid(SnmpOid paramSnmpOid1, SnmpOid paramSnmpOid2)
/*     */   {
/* 317 */     paramSnmpOid2.append(paramSnmpOid1.getLength());
/* 318 */     paramSnmpOid2.append(paramSnmpOid1);
/*     */   }
/*     */ 
/*     */   public final synchronized SnmpValue duplicate()
/*     */   {
/* 327 */     return (SnmpValue)clone();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 336 */       SnmpOid localSnmpOid = (SnmpOid)super.clone();
/* 337 */       localSnmpOid.components = new long[this.componentCount];
/*     */ 
/* 339 */       System.arraycopy(this.components, 0, localSnmpOid.components, 0, this.componentCount);
/*     */ 
/* 341 */       return localSnmpOid; } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 343 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   public void insert(long paramLong)
/*     */   {
/* 352 */     enlargeIfNeeded(1);
/* 353 */     for (int i = this.componentCount - 1; i >= 0; i--) {
/* 354 */       this.components[(i + 1)] = this.components[i];
/*     */     }
/* 356 */     this.components[0] = paramLong;
/* 357 */     this.componentCount += 1;
/*     */   }
/*     */ 
/*     */   public void insert(int paramInt)
/*     */   {
/* 365 */     insert(paramInt);
/*     */   }
/*     */ 
/*     */   public void append(SnmpOid paramSnmpOid)
/*     */   {
/* 373 */     enlargeIfNeeded(paramSnmpOid.componentCount);
/* 374 */     for (int i = 0; i < paramSnmpOid.componentCount; i++) {
/* 375 */       this.components[(this.componentCount + i)] = paramSnmpOid.components[i];
/*     */     }
/* 377 */     this.componentCount += paramSnmpOid.componentCount;
/*     */   }
/*     */ 
/*     */   public void append(long paramLong)
/*     */   {
/* 385 */     enlargeIfNeeded(1);
/* 386 */     this.components[this.componentCount] = paramLong;
/* 387 */     this.componentCount += 1;
/*     */   }
/*     */ 
/*     */   public void addToOid(String paramString)
/*     */     throws SnmpStatusException
/*     */   {
/* 399 */     SnmpOid localSnmpOid = new SnmpOid(paramString);
/* 400 */     append(localSnmpOid);
/*     */   }
/*     */ 
/*     */   public void addToOid(long[] paramArrayOfLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 409 */     SnmpOid localSnmpOid = new SnmpOid(paramArrayOfLong);
/* 410 */     append(localSnmpOid);
/*     */   }
/*     */ 
/*     */   public boolean isValid()
/*     */   {
/* 418 */     return (this.componentCount >= 2) && (0L <= this.components[0]) && (this.components[0] < 3L) && (0L <= this.components[1]) && (this.components[1] < 40L);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 429 */     boolean bool = false;
/*     */ 
/* 431 */     if ((paramObject instanceof SnmpOid)) {
/* 432 */       SnmpOid localSnmpOid = (SnmpOid)paramObject;
/* 433 */       if (localSnmpOid.componentCount == this.componentCount) {
/* 434 */         int i = 0;
/* 435 */         long[] arrayOfLong = localSnmpOid.components;
/* 436 */         while ((i < this.componentCount) && (this.components[i] == arrayOfLong[i]))
/* 437 */           i++;
/* 438 */         bool = i == this.componentCount;
/*     */       }
/*     */     }
/* 441 */     return bool;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 449 */     long l = 0L;
/* 450 */     for (int i = 0; i < this.componentCount; i++) {
/* 451 */       l = l * 31L + this.components[i];
/*     */     }
/* 453 */     return (int)l;
/*     */   }
/*     */ 
/*     */   public int compareTo(SnmpOid paramSnmpOid)
/*     */   {
/* 465 */     int i = 0;
/* 466 */     int j = 0;
/* 467 */     int k = Math.min(this.componentCount, paramSnmpOid.componentCount);
/* 468 */     long[] arrayOfLong = paramSnmpOid.components;
/*     */ 
/* 470 */     for (j = 0; (j < k) && 
/* 471 */       (this.components[j] == arrayOfLong[j]); j++);
/* 475 */     if ((j == this.componentCount) && (j == paramSnmpOid.componentCount)) {
/* 476 */       i = 0;
/*     */     }
/* 478 */     else if (j == this.componentCount) {
/* 479 */       i = -1;
/*     */     }
/* 481 */     else if (j == paramSnmpOid.componentCount) {
/* 482 */       i = 1;
/*     */     }
/*     */     else {
/* 485 */       i = this.components[j] < arrayOfLong[j] ? -1 : 1;
/*     */     }
/* 487 */     return i;
/*     */   }
/*     */ 
/*     */   public String resolveVarName(String paramString)
/*     */     throws SnmpStatusException
/*     */   {
/* 496 */     int i = paramString.indexOf('.');
/*     */     try
/*     */     {
/* 501 */       return handleLong(paramString, i);
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException) {
/* 504 */       SnmpOidTable localSnmpOidTable = getSnmpOidTable();
/*     */ 
/* 507 */       if (localSnmpOidTable == null) {
/* 508 */         throw new SnmpStatusException(2);
/*     */       }
/*     */ 
/* 512 */       if (i <= 0) {
/* 513 */         localSnmpOidRecord = localSnmpOidTable.resolveVarName(paramString);
/* 514 */         return localSnmpOidRecord.getOid();
/*     */       }
/*     */ 
/* 517 */       SnmpOidRecord localSnmpOidRecord = localSnmpOidTable.resolveVarName(paramString.substring(0, i));
/* 518 */       return localSnmpOidRecord.getOid() + paramString.substring(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getTypeName()
/*     */   {
/* 528 */     return "Object Identifier";
/*     */   }
/*     */ 
/*     */   public static SnmpOidTable getSnmpOidTable()
/*     */   {
/* 536 */     JavaAWTAccess localJavaAWTAccess = SharedSecrets.getJavaAWTAccess();
/* 537 */     if (localJavaAWTAccess == null) {
/* 538 */       return meta;
/*     */     }
/* 540 */     return (SnmpOidTable)localJavaAWTAccess.get(SnmpOidTable.class);
/*     */   }
/*     */ 
/*     */   public static void setSnmpOidTable(SnmpOidTable paramSnmpOidTable)
/*     */   {
/* 551 */     JavaAWTAccess localJavaAWTAccess = SharedSecrets.getJavaAWTAccess();
/* 552 */     if (localJavaAWTAccess == null) {
/* 553 */       meta = paramSnmpOidTable;
/*     */     }
/* 555 */     else if (paramSnmpOidTable == null)
/* 556 */       localJavaAWTAccess.remove(SnmpOidTable.class);
/*     */     else
/* 558 */       localJavaAWTAccess.put(SnmpOidTable.class, paramSnmpOidTable);
/*     */   }
/*     */ 
/*     */   public String toOctetString()
/*     */   {
/* 568 */     return new String(tobyte());
/*     */   }
/*     */ 
/*     */   private byte[] tobyte()
/*     */   {
/* 579 */     byte[] arrayOfByte = new byte[this.componentCount];
/* 580 */     for (int i = 0; i < this.componentCount; i++) {
/* 581 */       if (this.components[0] > 255L) {
/* 582 */         throw new IllegalArgumentException();
/*     */       }
/* 584 */       arrayOfByte[i] = ((byte)(int)this.components[i]);
/*     */     }
/* 586 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private void enlargeIfNeeded(int paramInt)
/*     */   {
/* 597 */     int i = this.components.length;
/* 598 */     while (this.componentCount + paramInt > i) {
/* 599 */       i *= 2;
/*     */     }
/* 601 */     if (i > this.components.length) {
/* 602 */       long[] arrayOfLong = new long[i];
/* 603 */       for (int j = 0; j < this.components.length; j++) {
/* 604 */         arrayOfLong[j] = this.components[j];
/*     */       }
/* 606 */       this.components = arrayOfLong;
/*     */     }
/*     */   }
/*     */ 
/*     */   private String handleLong(String paramString, int paramInt)
/*     */     throws NumberFormatException, SnmpStatusException
/*     */   {
/*     */     String str;
/* 614 */     if (paramInt > 0)
/* 615 */       str = paramString.substring(0, paramInt);
/*     */     else {
/* 617 */       str = paramString;
/*     */     }
/*     */ 
/* 622 */     Long.parseLong(str);
/* 623 */     return paramString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpOid
 * JD-Core Version:    0.6.2
 */