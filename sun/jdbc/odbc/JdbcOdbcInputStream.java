/*     */ package sun.jdbc.odbc;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.sql.Statement;
/*     */ 
/*     */ public class JdbcOdbcInputStream extends InputStream
/*     */ {
/*     */   protected JdbcOdbc OdbcApi;
/*     */   protected long hStmt;
/*     */   protected int column;
/*     */   protected short type;
/*     */   public static final short ASCII = 1;
/*     */   public static final short UNICODE = 2;
/*     */   public static final short BINARY = 3;
/*     */   public static final short LOCAL = 4;
/*     */   public static final short CHARACTER = 5;
/*     */   protected byte[] localByteArray;
/*     */   protected int localOffset;
/*     */   protected boolean invalid;
/*     */   protected boolean highRead;
/*     */   protected int sqlType;
/*     */   protected byte[] buf;
/*     */   public static final int MAX_BUF_LEN = 5120;
/*     */   protected int convertType;
/*     */   public static final int CONVERT_NONE = 0;
/*     */   public static final int CONVERT_UNICODE = 1;
/*     */   public static final int CONVERT_ASCII = 2;
/*     */   public static final int CONVERT_BOTH = 3;
/*     */   protected int convertMultiplier;
/*     */   protected int bytesInBuf;
/*     */   protected int bufOffset;
/*     */   protected Statement ownerStatement;
/*     */ 
/*     */   public JdbcOdbcInputStream(JdbcOdbc paramJdbcOdbc, long paramLong, int paramInt1, short paramShort, int paramInt2, Statement paramStatement)
/*     */   {
/*  45 */     this.OdbcApi = paramJdbcOdbc;
/*  46 */     this.hStmt = paramLong;
/*  47 */     this.column = paramInt1;
/*  48 */     this.type = paramShort;
/*  49 */     this.invalid = false;
/*     */ 
/*  55 */     this.ownerStatement = paramStatement;
/*     */ 
/*  57 */     this.sqlType = -2;
/*     */ 
/*  59 */     switch (paramInt2) {
/*     */     case -10:
/*     */     case -9:
/*     */     case -8:
/*     */     case -1:
/*     */     case 1:
/*     */     case 12:
/*  66 */       this.sqlType = 1;
/*     */     }
/*     */ 
/*  81 */     this.convertMultiplier = 1;
/*  82 */     this.convertType = 0;
/*     */ 
/*  84 */     switch (this.type)
/*     */     {
/*     */     case 1:
/*  90 */       if (this.sqlType == -2) {
/*  91 */         this.convertMultiplier = 2;
/*  92 */         this.convertType = 2; } break;
/*     */     case 2:
/* 100 */       if (this.sqlType == -2) {
/* 101 */         this.convertType = 3;
/* 102 */         this.convertMultiplier = 4;
/*     */       }
/*     */       else
/*     */       {
/* 106 */         this.convertType = 1;
/* 107 */         this.convertMultiplier = 2;
/*     */       }
/* 109 */       break;
/*     */     case 5:
/* 111 */       this.convertType = 0;
/* 112 */       this.convertMultiplier = 1;
/*     */     case 3:
/*     */     case 4:
/*     */     }
/*     */ 
/* 118 */     this.buf = new byte[5120 * this.convertMultiplier];
/*     */ 
/* 120 */     this.bytesInBuf = 0;
/* 121 */     this.bufOffset = 0;
/*     */   }
/*     */ 
/*     */   public JdbcOdbcInputStream(JdbcOdbc paramJdbcOdbc, long paramLong, int paramInt, byte[] paramArrayOfByte)
/*     */   {
/* 130 */     this.OdbcApi = paramJdbcOdbc;
/* 131 */     this.hStmt = paramLong;
/* 132 */     this.column = paramInt;
/* 133 */     this.type = 4;
/* 134 */     this.localByteArray = paramArrayOfByte;
/* 135 */     this.localOffset = 0;
/* 136 */     this.invalid = false;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 152 */     byte[] arrayOfByte = new byte[1];
/*     */ 
/* 156 */     int i = read(arrayOfByte);
/*     */ 
/* 159 */     if (i != -1) {
/* 160 */       i = arrayOfByte[0] & 0xFF;
/*     */     }
/*     */ 
/* 163 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 175 */     return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public byte[] readAllData() throws IOException {
/* 179 */     int i = 0;
/*     */ 
/* 186 */     if (this.invalid)
/* 187 */       throw new IOException("InputStream is no longer valid - the Statement has been closed, or the cursor has been moved");
/*     */     byte[] arrayOfByte;
/* 191 */     switch (this.type)
/*     */     {
/*     */     case 4:
/* 197 */       if (this.localOffset + i > this.localByteArray.length) {
/* 198 */         i = this.localByteArray.length - this.localOffset;
/*     */       }
/* 200 */       arrayOfByte = new byte[this.localByteArray.length];
/*     */ 
/* 204 */       if (i == 0) {
/* 205 */         i = -1;
/*     */       }
/*     */       else
/*     */       {
/* 209 */         System.arraycopy(this.localByteArray, this.localOffset, arrayOfByte, this.localOffset, i);
/* 210 */         this.localOffset += i;
/*     */       }
/* 212 */       break;
/*     */     default:
/* 215 */       arrayOfByte = readData();
/*     */     }
/*     */ 
/* 220 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 234 */     if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0))
/*     */     {
/* 236 */       throw new IndexOutOfBoundsException();
/* 237 */     }if (paramInt2 == 0) {
/* 238 */       return -1;
/*     */     }
/*     */ 
/* 241 */     int i = 0;
/*     */ 
/* 248 */     if (this.invalid) {
/* 249 */       throw new IOException("InputStream is no longer valid - the Statement has been closed, or the cursor has been moved");
/*     */     }
/*     */ 
/* 253 */     switch (this.type)
/*     */     {
/*     */     case 4:
/* 257 */       i = paramInt2;
/*     */ 
/* 259 */       if (this.localOffset + i > this.localByteArray.length) {
/* 260 */         i = this.localByteArray.length - this.localOffset;
/*     */       }
/*     */ 
/* 266 */       if (i == 0) {
/* 267 */         i = -1;
/*     */       }
/*     */       else
/*     */       {
/* 272 */         for (int j = paramInt1; j < i; j++) {
/* 273 */           paramArrayOfByte[j] = this.localByteArray[(this.localOffset + j)];
/*     */         }
/* 275 */         this.localOffset += i;
/*     */       }
/* 277 */       break;
/*     */     default:
/* 280 */       i = readData(paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*     */ 
/* 285 */     return i;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 299 */     throw new IOException();
/*     */   }
/*     */ 
/*     */   public void invalidate()
/*     */   {
/* 308 */     this.invalid = true;
/*     */   }
/*     */ 
/*     */   public byte[] readData() throws IOException
/*     */   {
/* 313 */     Object localObject = null;
/* 314 */     int i = 0;
/*     */     while (true)
/*     */     {
/* 319 */       this.bytesInBuf = readBinaryData(this.buf, 5120);
/*     */ 
/* 321 */       this.bytesInBuf = convertData(this.buf, this.bytesInBuf);
/* 322 */       if (this.bytesInBuf == -1)
/* 323 */         return localObject;
/*     */       try
/*     */       {
/* 326 */         if (localObject == null) {
/* 327 */           localObject = new byte[this.bytesInBuf];
/*     */         }
/*     */         else {
/* 330 */           byte[] arrayOfByte = new byte[i + this.bytesInBuf];
/* 331 */           System.arraycopy(localObject, 0, arrayOfByte, 0, i);
/* 332 */           localObject = arrayOfByte;
/*     */         }
/*     */       }
/*     */       catch (OutOfMemoryError localOutOfMemoryError)
/*     */       {
/* 337 */         ((JdbcOdbcStatement)this.ownerStatement).setWarning(new SQLWarning("Data has been truncated. " + localOutOfMemoryError.getMessage()));
/*     */ 
/* 339 */         return localObject;
/*     */       }
/* 341 */       System.arraycopy(this.buf, 0, localObject, i, this.bytesInBuf);
/* 342 */       i += this.bytesInBuf;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int readData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 358 */     int i = -1;
/* 359 */     int j = paramInt1;
/*     */ 
/* 364 */     while ((this.bytesInBuf != -1) && (j - paramInt1 < paramInt2))
/*     */     {
/* 369 */       if (this.bufOffset >= this.bytesInBuf)
/*     */       {
/* 373 */         this.bytesInBuf = readBinaryData(this.buf, 5120);
/*     */ 
/* 377 */         this.bytesInBuf = convertData(this.buf, this.bytesInBuf);
/* 378 */         this.bufOffset = 0;
/*     */       }
/*     */       else {
/* 381 */         paramArrayOfByte[j] = this.buf[this.bufOffset];
/* 382 */         j++;
/* 383 */         this.bufOffset += 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 389 */     if (j > paramInt1) {
/* 390 */       i = j;
/*     */     }
/*     */ 
/* 393 */     return i;
/*     */   }
/*     */ 
/*     */   protected int readBinaryData(byte[] paramArrayOfByte, int paramInt)
/*     */     throws IOException
/*     */   {
/* 407 */     int i = 0;
/*     */     try
/*     */     {
/* 412 */       i = this.OdbcApi.SQLGetDataBinary(this.hStmt, this.column, -2, paramArrayOfByte, paramInt);
/*     */     }
/*     */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*     */     {
/* 419 */       Integer localInteger = (Integer)localJdbcOdbcSQLWarning.value;
/* 420 */       i = localInteger.intValue();
/*     */     }
/*     */     catch (SQLException localSQLException) {
/* 423 */       throw new IOException(localSQLException.getMessage());
/*     */     }
/*     */ 
/* 426 */     return i;
/*     */   }
/*     */ 
/*     */   protected int convertData(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 440 */     if (this.convertType == 0) {
/* 441 */       return paramInt;
/*     */     }
/*     */ 
/* 444 */     String str = "0123456789ABCDEF";
/*     */ 
/* 446 */     if (paramInt <= 0) {
/* 447 */       return paramInt;
/*     */     }
/*     */ 
/* 462 */     for (int i = paramInt - 1; i >= 0; i--) {
/* 463 */       if (this.convertType == 3) {
/* 464 */         paramArrayOfByte[(i * 4 + 3)] = ((byte)str.charAt(paramArrayOfByte[i] & 0xF));
/* 465 */         paramArrayOfByte[(i * 4 + 2)] = 0;
/* 466 */         paramArrayOfByte[(i * 4 + 1)] = ((byte)str.charAt(paramArrayOfByte[i] >> 4 & 0xF));
/* 467 */         paramArrayOfByte[(i * 4)] = 0;
/*     */       }
/* 469 */       else if (this.convertType == 2) {
/* 470 */         paramArrayOfByte[(i * 2 + 1)] = ((byte)str.charAt(paramArrayOfByte[i] & 0xF));
/* 471 */         paramArrayOfByte[(i * 2)] = ((byte)str.charAt(paramArrayOfByte[i] >> 4 & 0xF));
/*     */       }
/*     */       else {
/* 474 */         paramArrayOfByte[(i * 2 + 1)] = paramArrayOfByte[i];
/* 475 */         paramArrayOfByte[(i * 2)] = 0;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 480 */     return paramInt * this.convertMultiplier;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcInputStream
 * JD-Core Version:    0.6.2
 */