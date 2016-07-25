/*     */ package javax.sql.rowset.serial;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
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
/*     */ import java.sql.SQLOutput;
/*     */ import java.sql.SQLXML;
/*     */ import java.sql.Struct;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SQLOutputImpl
/*     */   implements SQLOutput
/*     */ {
/*     */   private Vector attribs;
/*     */   private Map map;
/*     */ 
/*     */   public SQLOutputImpl(Vector<?> paramVector, Map<String, ?> paramMap)
/*     */     throws SQLException
/*     */   {
/*  99 */     if ((paramVector == null) || (paramMap == null)) {
/* 100 */       throw new SQLException("Cannot instantiate a SQLOutputImpl instance with null parameters");
/*     */     }
/*     */ 
/* 103 */     this.attribs = paramVector;
/* 104 */     this.map = paramMap;
/*     */   }
/*     */ 
/*     */   public void writeString(String paramString)
/*     */     throws SQLException
/*     */   {
/* 126 */     this.attribs.add(paramString);
/*     */   }
/*     */ 
/*     */   public void writeBoolean(boolean paramBoolean)
/*     */     throws SQLException
/*     */   {
/* 140 */     this.attribs.add(Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   public void writeByte(byte paramByte)
/*     */     throws SQLException
/*     */   {
/* 154 */     this.attribs.add(Byte.valueOf(paramByte));
/*     */   }
/*     */ 
/*     */   public void writeShort(short paramShort)
/*     */     throws SQLException
/*     */   {
/* 168 */     this.attribs.add(Short.valueOf(paramShort));
/*     */   }
/*     */ 
/*     */   public void writeInt(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 182 */     this.attribs.add(Integer.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   public void writeLong(long paramLong)
/*     */     throws SQLException
/*     */   {
/* 196 */     this.attribs.add(Long.valueOf(paramLong));
/*     */   }
/*     */ 
/*     */   public void writeFloat(float paramFloat)
/*     */     throws SQLException
/*     */   {
/* 210 */     this.attribs.add(new Float(paramFloat));
/*     */   }
/*     */ 
/*     */   public void writeDouble(double paramDouble)
/*     */     throws SQLException
/*     */   {
/* 224 */     this.attribs.add(new Double(paramDouble));
/*     */   }
/*     */ 
/*     */   public void writeBigDecimal(BigDecimal paramBigDecimal)
/*     */     throws SQLException
/*     */   {
/* 238 */     this.attribs.add(paramBigDecimal);
/*     */   }
/*     */ 
/*     */   public void writeBytes(byte[] paramArrayOfByte)
/*     */     throws SQLException
/*     */   {
/* 253 */     this.attribs.add(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public void writeDate(Date paramDate)
/*     */     throws SQLException
/*     */   {
/* 267 */     this.attribs.add(paramDate);
/*     */   }
/*     */ 
/*     */   public void writeTime(Time paramTime)
/*     */     throws SQLException
/*     */   {
/* 281 */     this.attribs.add(paramTime);
/*     */   }
/*     */ 
/*     */   public void writeTimestamp(Timestamp paramTimestamp)
/*     */     throws SQLException
/*     */   {
/* 295 */     this.attribs.add(paramTimestamp);
/*     */   }
/*     */ 
/*     */   public void writeCharacterStream(Reader paramReader)
/*     */     throws SQLException
/*     */   {
/* 309 */     BufferedReader localBufferedReader = new BufferedReader(paramReader);
/*     */     try
/*     */     {
/*     */       int i;
/* 312 */       while ((i = localBufferedReader.read()) != -1) {
/* 313 */         char c = (char)i;
/* 314 */         StringBuffer localStringBuffer = new StringBuffer();
/* 315 */         localStringBuffer.append(c);
/*     */ 
/* 317 */         String str1 = new String(localStringBuffer);
/* 318 */         String str2 = localBufferedReader.readLine();
/*     */ 
/* 320 */         writeString(str1.concat(str2));
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeAsciiStream(InputStream paramInputStream)
/*     */     throws SQLException
/*     */   {
/* 338 */     BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
/*     */     try
/*     */     {
/*     */       int i;
/* 341 */       while ((i = localBufferedReader.read()) != -1) {
/* 342 */         char c = (char)i;
/*     */ 
/* 344 */         StringBuffer localStringBuffer = new StringBuffer();
/* 345 */         localStringBuffer.append(c);
/*     */ 
/* 347 */         String str1 = new String(localStringBuffer);
/* 348 */         String str2 = localBufferedReader.readLine();
/*     */ 
/* 350 */         writeString(str1.concat(str2));
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 353 */       throw new SQLException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeBinaryStream(InputStream paramInputStream)
/*     */     throws SQLException
/*     */   {
/* 367 */     BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
/*     */     try
/*     */     {
/*     */       int i;
/* 370 */       while ((i = localBufferedReader.read()) != -1) {
/* 371 */         char c = (char)i;
/*     */ 
/* 373 */         StringBuffer localStringBuffer = new StringBuffer();
/* 374 */         localStringBuffer.append(c);
/*     */ 
/* 376 */         String str1 = new String(localStringBuffer);
/* 377 */         String str2 = localBufferedReader.readLine();
/*     */ 
/* 379 */         writeString(str1.concat(str2));
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 382 */       throw new SQLException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeObject(SQLData paramSQLData)
/*     */     throws SQLException
/*     */   {
/* 427 */     if (paramSQLData == null) {
/* 428 */       this.attribs.add(paramSQLData);
/* 429 */       return;
/*     */     }
/*     */ 
/* 437 */     this.attribs.add(new SerialStruct(paramSQLData, this.map));
/*     */   }
/*     */ 
/*     */   public void writeRef(Ref paramRef)
/*     */     throws SQLException
/*     */   {
/* 452 */     if (paramRef == null) {
/* 453 */       this.attribs.add(paramRef);
/* 454 */       return;
/*     */     }
/* 456 */     this.attribs.add(new SerialRef(paramRef));
/*     */   }
/*     */ 
/*     */   public void writeBlob(Blob paramBlob)
/*     */     throws SQLException
/*     */   {
/* 471 */     if (paramBlob == null) {
/* 472 */       this.attribs.add(paramBlob);
/* 473 */       return;
/*     */     }
/* 475 */     this.attribs.add(new SerialBlob(paramBlob));
/*     */   }
/*     */ 
/*     */   public void writeClob(Clob paramClob)
/*     */     throws SQLException
/*     */   {
/* 490 */     if (paramClob == null) {
/* 491 */       this.attribs.add(paramClob);
/* 492 */       return;
/*     */     }
/* 494 */     this.attribs.add(new SerialClob(paramClob));
/*     */   }
/*     */ 
/*     */   public void writeStruct(Struct paramStruct)
/*     */     throws SQLException
/*     */   {
/* 515 */     SerialStruct localSerialStruct = new SerialStruct(paramStruct, this.map);
/* 516 */     this.attribs.add(localSerialStruct);
/*     */   }
/*     */ 
/*     */   public void writeArray(Array paramArray)
/*     */     throws SQLException
/*     */   {
/* 532 */     if (paramArray == null) {
/* 533 */       this.attribs.add(paramArray);
/* 534 */       return;
/*     */     }
/* 536 */     this.attribs.add(new SerialArray(paramArray, this.map));
/*     */   }
/*     */ 
/*     */   public void writeURL(URL paramURL)
/*     */     throws SQLException
/*     */   {
/* 551 */     if (paramURL == null) {
/* 552 */       this.attribs.add(paramURL);
/* 553 */       return;
/*     */     }
/* 555 */     this.attribs.add(new SerialDatalink(paramURL));
/*     */   }
/*     */ 
/*     */   public void writeNString(String paramString)
/*     */     throws SQLException
/*     */   {
/* 574 */     throw new UnsupportedOperationException("Operation not supported");
/*     */   }
/*     */ 
/*     */   public void writeNClob(NClob paramNClob)
/*     */     throws SQLException
/*     */   {
/* 587 */     throw new UnsupportedOperationException("Operation not supported");
/*     */   }
/*     */ 
/*     */   public void writeRowId(RowId paramRowId)
/*     */     throws SQLException
/*     */   {
/* 601 */     throw new UnsupportedOperationException("Operation not supported");
/*     */   }
/*     */ 
/*     */   public void writeSQLXML(SQLXML paramSQLXML)
/*     */     throws SQLException
/*     */   {
/* 615 */     throw new UnsupportedOperationException("Operation not supported");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.serial.SQLOutputImpl
 * JD-Core Version:    0.6.2
 */