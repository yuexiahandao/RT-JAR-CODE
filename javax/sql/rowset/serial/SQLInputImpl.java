/*     */ package javax.sql.rowset.serial;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.math.BigDecimal;
/*     */ import java.net.URL;
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Date;
/*     */ import java.sql.NClob;
/*     */ import java.sql.Ref;
/*     */ import java.sql.RowId;
/*     */ import java.sql.SQLData;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLInput;
/*     */ import java.sql.SQLXML;
/*     */ import java.sql.Struct;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Map;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class SQLInputImpl
/*     */   implements SQLInput
/*     */ {
/*     */   private boolean lastValueWasNull;
/*     */   private int idx;
/*     */   private Object[] attrib;
/*     */   private Map map;
/*     */ 
/*     */   public SQLInputImpl(Object[] paramArrayOfObject, Map<String, Class<?>> paramMap)
/*     */     throws SQLException
/*     */   {
/* 122 */     if ((paramArrayOfObject == null) || (paramMap == null)) {
/* 123 */       throw new SQLException("Cannot instantiate a SQLInputImpl object with null parameters");
/*     */     }
/*     */ 
/* 127 */     this.attrib = paramArrayOfObject;
/*     */ 
/* 129 */     this.idx = -1;
/*     */ 
/* 131 */     this.map = paramMap;
/*     */   }
/*     */ 
/*     */   private Object getNextAttribute()
/*     */     throws SQLException
/*     */   {
/* 145 */     if (++this.idx >= this.attrib.length) {
/* 146 */       throw new SQLException("SQLInputImpl exception: Invalid read position");
/*     */     }
/*     */ 
/* 149 */     return this.attrib[this.idx];
/*     */   }
/*     */ 
/*     */   public String readString()
/*     */     throws SQLException
/*     */   {
/* 176 */     String str = (String)getNextAttribute();
/*     */ 
/* 178 */     if (str == null) {
/* 179 */       this.lastValueWasNull = true;
/* 180 */       return null;
/*     */     }
/* 182 */     this.lastValueWasNull = false;
/* 183 */     return str;
/*     */   }
/*     */ 
/*     */   public boolean readBoolean()
/*     */     throws SQLException
/*     */   {
/* 203 */     Boolean localBoolean = (Boolean)getNextAttribute();
/*     */ 
/* 205 */     if (localBoolean == null) {
/* 206 */       this.lastValueWasNull = true;
/* 207 */       return false;
/*     */     }
/* 209 */     this.lastValueWasNull = false;
/* 210 */     return localBoolean.booleanValue();
/*     */   }
/*     */ 
/*     */   public byte readByte()
/*     */     throws SQLException
/*     */   {
/* 229 */     Byte localByte = (Byte)getNextAttribute();
/*     */ 
/* 231 */     if (localByte == null) {
/* 232 */       this.lastValueWasNull = true;
/* 233 */       return 0;
/*     */     }
/* 235 */     this.lastValueWasNull = false;
/* 236 */     return localByte.byteValue();
/*     */   }
/*     */ 
/*     */   public short readShort()
/*     */     throws SQLException
/*     */   {
/* 254 */     Short localShort = (Short)getNextAttribute();
/*     */ 
/* 256 */     if (localShort == null) {
/* 257 */       this.lastValueWasNull = true;
/* 258 */       return 0;
/*     */     }
/* 260 */     this.lastValueWasNull = false;
/* 261 */     return localShort.shortValue();
/*     */   }
/*     */ 
/*     */   public int readInt()
/*     */     throws SQLException
/*     */   {
/* 279 */     Integer localInteger = (Integer)getNextAttribute();
/*     */ 
/* 281 */     if (localInteger == null) {
/* 282 */       this.lastValueWasNull = true;
/* 283 */       return 0;
/*     */     }
/* 285 */     this.lastValueWasNull = false;
/* 286 */     return localInteger.intValue();
/*     */   }
/*     */ 
/*     */   public long readLong()
/*     */     throws SQLException
/*     */   {
/* 304 */     Long localLong = (Long)getNextAttribute();
/*     */ 
/* 306 */     if (localLong == null) {
/* 307 */       this.lastValueWasNull = true;
/* 308 */       return 0L;
/*     */     }
/* 310 */     this.lastValueWasNull = false;
/* 311 */     return localLong.longValue();
/*     */   }
/*     */ 
/*     */   public float readFloat()
/*     */     throws SQLException
/*     */   {
/* 329 */     Float localFloat = (Float)getNextAttribute();
/*     */ 
/* 331 */     if (localFloat == null) {
/* 332 */       this.lastValueWasNull = true;
/* 333 */       return 0.0F;
/*     */     }
/* 335 */     this.lastValueWasNull = false;
/* 336 */     return localFloat.floatValue();
/*     */   }
/*     */ 
/*     */   public double readDouble()
/*     */     throws SQLException
/*     */   {
/* 354 */     Double localDouble = (Double)getNextAttribute();
/*     */ 
/* 356 */     if (localDouble == null) {
/* 357 */       this.lastValueWasNull = true;
/* 358 */       return 0.0D;
/*     */     }
/* 360 */     this.lastValueWasNull = false;
/* 361 */     return localDouble.doubleValue();
/*     */   }
/*     */ 
/*     */   public BigDecimal readBigDecimal()
/*     */     throws SQLException
/*     */   {
/* 379 */     BigDecimal localBigDecimal = (BigDecimal)getNextAttribute();
/*     */ 
/* 381 */     if (localBigDecimal == null) {
/* 382 */       this.lastValueWasNull = true;
/* 383 */       return null;
/*     */     }
/* 385 */     this.lastValueWasNull = false;
/* 386 */     return localBigDecimal;
/*     */   }
/*     */ 
/*     */   public byte[] readBytes()
/*     */     throws SQLException
/*     */   {
/* 404 */     byte[] arrayOfByte = (byte[])getNextAttribute();
/*     */ 
/* 406 */     if (arrayOfByte == null) {
/* 407 */       this.lastValueWasNull = true;
/* 408 */       return null;
/*     */     }
/* 410 */     this.lastValueWasNull = false;
/* 411 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public Date readDate()
/*     */     throws SQLException
/*     */   {
/* 429 */     Date localDate = (Date)getNextAttribute();
/*     */ 
/* 431 */     if (localDate == null) {
/* 432 */       this.lastValueWasNull = true;
/* 433 */       return null;
/*     */     }
/* 435 */     this.lastValueWasNull = false;
/* 436 */     return localDate;
/*     */   }
/*     */ 
/*     */   public Time readTime()
/*     */     throws SQLException
/*     */   {
/* 455 */     Time localTime = (Time)getNextAttribute();
/*     */ 
/* 457 */     if (localTime == null) {
/* 458 */       this.lastValueWasNull = true;
/* 459 */       return null;
/*     */     }
/* 461 */     this.lastValueWasNull = false;
/* 462 */     return localTime;
/*     */   }
/*     */ 
/*     */   public Timestamp readTimestamp()
/*     */     throws SQLException
/*     */   {
/* 476 */     Timestamp localTimestamp = (Timestamp)getNextAttribute();
/*     */ 
/* 478 */     if (localTimestamp == null) {
/* 479 */       this.lastValueWasNull = true;
/* 480 */       return null;
/*     */     }
/* 482 */     this.lastValueWasNull = false;
/* 483 */     return localTimestamp;
/*     */   }
/*     */ 
/*     */   public Reader readCharacterStream()
/*     */     throws SQLException
/*     */   {
/* 501 */     Reader localReader = (Reader)getNextAttribute();
/*     */ 
/* 503 */     if (localReader == null) {
/* 504 */       this.lastValueWasNull = true;
/* 505 */       return null;
/*     */     }
/* 507 */     this.lastValueWasNull = false;
/* 508 */     return localReader;
/*     */   }
/*     */ 
/*     */   public InputStream readAsciiStream()
/*     */     throws SQLException
/*     */   {
/* 527 */     InputStream localInputStream = (InputStream)getNextAttribute();
/*     */ 
/* 529 */     if (localInputStream == null) {
/* 530 */       this.lastValueWasNull = true;
/* 531 */       return null;
/*     */     }
/* 533 */     this.lastValueWasNull = false;
/* 534 */     return localInputStream;
/*     */   }
/*     */ 
/*     */   public InputStream readBinaryStream()
/*     */     throws SQLException
/*     */   {
/* 553 */     InputStream localInputStream = (InputStream)getNextAttribute();
/*     */ 
/* 555 */     if (localInputStream == null) {
/* 556 */       this.lastValueWasNull = true;
/* 557 */       return null;
/*     */     }
/* 559 */     this.lastValueWasNull = false;
/* 560 */     return localInputStream;
/*     */   }
/*     */ 
/*     */   public Object readObject()
/*     */     throws SQLException
/*     */   {
/* 595 */     Object localObject = getNextAttribute();
/*     */ 
/* 597 */     if (localObject == null) {
/* 598 */       this.lastValueWasNull = true;
/* 599 */       return null;
/*     */     }
/* 601 */     this.lastValueWasNull = false;
/* 602 */     if ((localObject instanceof Struct)) {
/* 603 */       Struct localStruct = (Struct)localObject;
/*     */ 
/* 605 */       Class localClass = (Class)this.map.get(localStruct.getSQLTypeName());
/* 606 */       if (localClass != null)
/*     */       {
/* 608 */         SQLData localSQLData = null;
/*     */         try {
/* 610 */           localSQLData = (SQLData)ReflectUtil.newInstance(localClass);
/*     */         } catch (Exception localException) {
/* 612 */           throw new SQLException("Unable to Instantiate: ", localException);
/*     */         }
/*     */ 
/* 615 */         Object[] arrayOfObject = localStruct.getAttributes(this.map);
/*     */ 
/* 617 */         SQLInputImpl localSQLInputImpl = new SQLInputImpl(arrayOfObject, this.map);
/*     */ 
/* 619 */         localSQLData.readSQL(localSQLInputImpl, localStruct.getSQLTypeName());
/* 620 */         return localSQLData;
/*     */       }
/*     */     }
/* 623 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Ref readRef()
/*     */     throws SQLException
/*     */   {
/* 638 */     Ref localRef = (Ref)getNextAttribute();
/*     */ 
/* 640 */     if (localRef == null) {
/* 641 */       this.lastValueWasNull = true;
/* 642 */       return null;
/*     */     }
/* 644 */     this.lastValueWasNull = false;
/* 645 */     return localRef;
/*     */   }
/*     */ 
/*     */   public Blob readBlob()
/*     */     throws SQLException
/*     */   {
/* 667 */     Blob localBlob = (Blob)getNextAttribute();
/*     */ 
/* 669 */     if (localBlob == null) {
/* 670 */       this.lastValueWasNull = true;
/* 671 */       return null;
/*     */     }
/* 673 */     this.lastValueWasNull = false;
/* 674 */     return localBlob;
/*     */   }
/*     */ 
/*     */   public Clob readClob()
/*     */     throws SQLException
/*     */   {
/* 697 */     Clob localClob = (Clob)getNextAttribute();
/* 698 */     if (localClob == null) {
/* 699 */       this.lastValueWasNull = true;
/* 700 */       return null;
/*     */     }
/* 702 */     this.lastValueWasNull = false;
/* 703 */     return localClob;
/*     */   }
/*     */ 
/*     */   public Array readArray()
/*     */     throws SQLException
/*     */   {
/* 726 */     Array localArray = (Array)getNextAttribute();
/*     */ 
/* 728 */     if (localArray == null) {
/* 729 */       this.lastValueWasNull = true;
/* 730 */       return null;
/*     */     }
/* 732 */     this.lastValueWasNull = false;
/* 733 */     return localArray;
/*     */   }
/*     */ 
/*     */   public boolean wasNull()
/*     */     throws SQLException
/*     */   {
/* 748 */     return this.lastValueWasNull;
/*     */   }
/*     */ 
/*     */   public URL readURL()
/*     */     throws SQLException
/*     */   {
/* 769 */     throw new SQLException("Operation not supported");
/*     */   }
/*     */ 
/*     */   public NClob readNClob()
/*     */     throws SQLException
/*     */   {
/* 784 */     throw new UnsupportedOperationException("Operation not supported");
/*     */   }
/*     */ 
/*     */   public String readNString()
/*     */     throws SQLException
/*     */   {
/* 797 */     throw new UnsupportedOperationException("Operation not supported");
/*     */   }
/*     */ 
/*     */   public SQLXML readSQLXML()
/*     */     throws SQLException
/*     */   {
/* 810 */     throw new UnsupportedOperationException("Operation not supported");
/*     */   }
/*     */ 
/*     */   public RowId readRowId()
/*     */     throws SQLException
/*     */   {
/* 823 */     throw new UnsupportedOperationException("Operation not supported");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.serial.SQLInputImpl
 * JD-Core Version:    0.6.2
 */