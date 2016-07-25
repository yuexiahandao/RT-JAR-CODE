/*      */ package sun.jdbc.odbc;
/*      */ 
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.Clob;
/*      */ import java.sql.NClob;
/*      */ import java.sql.Ref;
/*      */ import java.sql.RowId;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLFeatureNotSupportedException;
/*      */ import java.sql.SQLXML;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Map;
/*      */ 
/*      */ public class JdbcOdbcCallableStatement extends JdbcOdbcPreparedStatement
/*      */   implements CallableStatement
/*      */ {
/*   39 */   public byte[] scalez = new byte['È'];
/*      */   private boolean lastParameterNull;
/*      */ 
/*      */   public JdbcOdbcCallableStatement(JdbcOdbcConnectionInterface paramJdbcOdbcConnectionInterface)
/*      */   {
/*   54 */     super(paramJdbcOdbcConnectionInterface);
/*   55 */     this.lastParameterNull = false;
/*      */   }
/*      */ 
/*      */   public void registerOutParameter(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*   75 */     registerOutParameter(paramInt1, paramInt2, 0);
/*      */   }
/*      */ 
/*      */   public void registerOutParameter(int paramInt1, int paramInt2, int paramInt3)
/*      */     throws SQLException
/*      */   {
/*   87 */     setSqlType(paramInt1, paramInt2);
/*      */ 
/*   90 */     if (paramInt1 <= 200) {
/*   91 */       this.scalez[paramInt1] = ((byte)paramInt3);
/*      */     }
/*      */ 
/*   94 */     setOutputParameter(paramInt1, true);
/*      */     int i;
/*  101 */     switch (paramInt2) {
/*      */     case 91:
/*  103 */       i = 10;
/*  104 */       break;
/*      */     case 92:
/*  106 */       i = 8;
/*  107 */       break;
/*      */     case 93:
/*  109 */       i = 15;
/*  110 */       if (paramInt3 > 0)
/*  111 */         i += paramInt3 + 1; break;
/*      */     case -7:
/*  115 */       i = 3;
/*  116 */       break;
/*      */     case -6:
/*  118 */       i = 4;
/*  119 */       break;
/*      */     case 5:
/*  121 */       i = 6;
/*  122 */       break;
/*      */     case 4:
/*  124 */       i = 11;
/*  125 */       break;
/*      */     case -5:
/*  127 */       i = 20;
/*  128 */       break;
/*      */     case 7:
/*  130 */       i = 13;
/*  131 */       break;
/*      */     case 6:
/*      */     case 8:
/*  134 */       i = 22;
/*  135 */       break;
/*      */     case 2:
/*      */     case 3:
/*  138 */       i = 38;
/*  139 */       break;
/*      */     default:
/*  142 */       i = getPrecision(paramInt2);
/*  143 */       if ((i <= 0) || (i > 8000))
/*      */       {
/*  145 */         i = 8000;
/*      */       }
/*      */ 
/*      */       break;
/*      */     }
/*      */ 
/*  153 */     byte[] arrayOfByte1 = getLengthBuf(paramInt1);
/*      */ 
/*  156 */     paramInt2 = OdbcDef.jdbcTypeToOdbc(paramInt2);
/*  157 */     long[] arrayOfLong = new long[4];
/*  158 */     arrayOfLong[0] = 0L;
/*  159 */     arrayOfLong[1] = 0L;
/*  160 */     arrayOfLong[2] = 0L;
/*  161 */     arrayOfLong[3] = 0L;
/*  162 */     byte[] arrayOfByte2 = null;
/*      */ 
/*  164 */     if ((this.boundParams[(paramInt1 - 1)].isInOutParameter()) && (this.boundParams[(paramInt1 - 1)].boundValue == null))
/*  165 */       arrayOfByte2 = null;
/*      */     else {
/*  167 */       arrayOfByte2 = allocBindBuf(paramInt1, i + 1);
/*      */     }
/*      */ 
/*  170 */     if (this.boundParams[(paramInt1 - 1)].isInOutParameter()) {
/*  171 */       if (this.boundParams[(paramInt1 - 1)].boundValue == null) {
/*  172 */         this.OdbcApi.SQLBindInOutParameterNull(this.hStmt, paramInt1, paramInt2, i, this.boundParams[(paramInt1 - 1)].scale, arrayOfByte1, arrayOfLong);
/*      */       }
/*  175 */       else if (paramInt2 == 4) {
/*  176 */         if (this.boundParams[(paramInt1 - 1)].boundType == 4) {
/*  177 */           this.OdbcApi.SQLBindInOutParameterFixed(this.hStmt, paramInt1, paramInt2, i, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */         }
/*      */         else
/*  180 */           throw new SQLException("Type mismatch between the set function and registerOutParameter");
/*      */       }
/*  182 */       else if (paramInt2 == -7) {
/*  183 */         if (this.boundParams[(paramInt1 - 1)].boundType == -7) {
/*  184 */           this.OdbcApi.SQLBindInOutParameterFixed(this.hStmt, paramInt1, paramInt2, i, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */         }
/*      */         else
/*  187 */           throw new SQLException("Type mismatch between the set function and registerOutParameter");
/*      */       }
/*  189 */       else if (paramInt2 == -6) {
/*  190 */         if (this.boundParams[(paramInt1 - 1)].boundType == -6) {
/*  191 */           this.OdbcApi.SQLBindInOutParameterFixed(this.hStmt, paramInt1, paramInt2, i, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */         }
/*      */         else
/*  194 */           throw new SQLException("Type mismatch between the set function and registerOutParameter");
/*      */       }
/*  196 */       else if (paramInt2 == 5) {
/*  197 */         if (this.boundParams[(paramInt1 - 1)].boundType == 5) {
/*  198 */           this.OdbcApi.SQLBindInOutParameterFixed(this.hStmt, paramInt1, paramInt2, i, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */         }
/*      */         else {
/*  201 */           throw new SQLException("Type mismatch between the set function and registerOutParameter");
/*      */         }
/*      */       }
/*  204 */       else if ((paramInt2 == 8) || (paramInt2 == 6) || (paramInt2 == 7)) {
/*  205 */         if ((this.boundParams[(paramInt1 - 1)].boundType == 8) || (this.boundParams[(paramInt1 - 1)].boundType == 6) || (this.boundParams[(paramInt1 - 1)].boundType == 7))
/*      */         {
/*  208 */           this.OdbcApi.SQLBindInOutParameterFixed(this.hStmt, paramInt1, 8, i, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */         }
/*      */         else
/*  211 */           throw new SQLException("Type mismatch between the set function and registerOutParameter");
/*      */       }
/*  213 */       else if (paramInt2 == -5) {
/*  214 */         if (this.boundParams[(paramInt1 - 1)].boundType == -5) {
/*  215 */           this.OdbcApi.SQLBindInOutParameterFixed(this.hStmt, paramInt1, paramInt2, i, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */         }
/*      */         else
/*  218 */           throw new SQLException("Type mismatch between the set function and registerOutParameter");
/*      */       }
/*      */       else
/*      */       {
/*      */         Object localObject1;
/*      */         int k;
/*  220 */         if ((paramInt2 == 1) || (paramInt2 == 12)) {
/*  221 */           if (this.boundParams[(paramInt1 - 1)].boundType == 1) {
/*  222 */             localObject1 = new byte[i + 1];
/*  223 */             for (k = 0; k < localObject1.length; k++) {
/*  224 */               localObject1[k] = 0;
/*      */             }
/*  226 */             for (k = 0; k < arrayOfByte2.length; k++) {
/*  227 */               localObject1[k] = arrayOfByte2[k];
/*      */             }
/*  229 */             this.boundParams[(paramInt1 - 1)].resetBindDataBuffer((byte[])localObject1);
/*  230 */             this.OdbcApi.SQLBindInOutParameterStr(this.hStmt, paramInt1, paramInt2, i, (byte[])localObject1, arrayOfByte1, arrayOfLong, -3);
/*      */           }
/*      */           else {
/*  233 */             throw new SQLException("Type mismatch between the set function and registerOutParameter");
/*      */           }
/*  235 */         } else if ((paramInt2 == -2) || (paramInt2 == -3)) {
/*  236 */           if ((this.boundParams[(paramInt1 - 1)].boundType == -2) || (this.boundParams[(paramInt1 - 1)].boundType == -3))
/*      */           {
/*  238 */             localObject1 = new byte[i + 1];
/*  239 */             for (k = 0; k < localObject1.length; k++) {
/*  240 */               localObject1[k] = 0;
/*      */             }
/*  242 */             for (k = 0; k < arrayOfByte2.length; k++) {
/*  243 */               localObject1[k] = arrayOfByte2[k];
/*      */             }
/*  245 */             this.boundParams[(paramInt1 - 1)].resetBindDataBuffer((byte[])localObject1);
/*  246 */             this.OdbcApi.SQLBindInOutParameterBin(this.hStmt, paramInt1, paramInt2, i, (byte[])localObject1, arrayOfByte1, arrayOfLong, arrayOfByte2.length);
/*      */           }
/*      */           else {
/*  249 */             throw new SQLException("Type mismatch between the set function and registerOutParameter");
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*      */           Object localObject3;
/*      */           Object localObject4;
/*  251 */           if ((paramInt2 == 2) || (paramInt2 == 3)) {
/*  252 */             localObject1 = null;
/*      */             try {
/*  254 */               localObject1 = BytesToChars(this.OdbcApi.charSet, arrayOfByte2);
/*      */             }
/*      */             catch (UnsupportedEncodingException localUnsupportedEncodingException1)
/*      */             {
/*      */             }
/*  259 */             BigDecimal localBigDecimal1 = new BigDecimal((String)localObject1);
/*      */ 
/*  261 */             localObject3 = null;
/*  262 */             localObject4 = null;
/*      */ 
/*  264 */             if (paramInt3 >= localBigDecimal1.scale())
/*      */             {
/*  266 */               BigDecimal localBigDecimal2 = localBigDecimal1.movePointRight(paramInt3).movePointLeft(paramInt3);
/*      */ 
/*  269 */               localObject3 = new byte[i];
/*  270 */               for (int i6 = 0; i6 < localObject3.length; i6++) {
/*  271 */                 localObject3[i6] = 48;
/*      */               }
/*      */ 
/*      */               try
/*      */               {
/*  280 */                 localObject4 = CharsToBytes(this.OdbcApi.charSet, localBigDecimal2.toString().toCharArray());
/*  281 */                 for (i6 = localObject3.length - localObject4.length; i6 < localObject3.length; i6++) {
/*  282 */                   localObject3[i6] = localObject4[(i6 - (localObject3.length - localObject4.length))];
/*      */                 }
/*      */               }
/*      */               catch (UnsupportedEncodingException localUnsupportedEncodingException3)
/*      */               {
/*      */               }
/*  288 */               this.boundParams[(paramInt1 - 1)].resetBindDataBuffer((byte[])localObject3);
/*  289 */               this.boundParams[(paramInt1 - 1)].scale = localBigDecimal2.scale();
/*      */             }
/*  291 */             this.OdbcApi.SQLBindInOutParameterString(this.hStmt, paramInt1, paramInt2, i, this.boundParams[(paramInt1 - 1)].scale, (byte[])localObject3, arrayOfByte1, arrayOfLong);
/*      */           }
/*      */           else
/*      */           {
/*      */             int i5;
/*  293 */             if (paramInt2 == 11) {
/*  294 */               if (this.boundParams[(paramInt1 - 1)].boundType == 93)
/*      */               {
/*  300 */                 int j = 0; int m = 0;
/*  301 */                 localObject3 = (Timestamp)this.boundParams[(paramInt1 - 1)].boundValue;
/*      */ 
/*  303 */                 localObject4 = Calendar.getInstance();
/*  304 */                 ((Calendar)localObject4).setTime((java.util.Date)localObject3);
/*      */ 
/*  306 */                 i5 = ((Calendar)localObject4).get(1);
/*  307 */                 int i7 = ((Calendar)localObject4).get(2);
/*  308 */                 int i8 = ((Calendar)localObject4).get(5);
/*  309 */                 int i9 = ((Calendar)localObject4).get(11);
/*  310 */                 int i10 = ((Calendar)localObject4).get(12);
/*  311 */                 int i11 = ((Calendar)localObject4).get(13);
/*  312 */                 int i12 = ((Timestamp)localObject3).getNanos();
/*      */ 
/*  314 */                 i7 += 1;
/*      */ 
/*  316 */                 byte[] arrayOfByte9 = new byte[16];
/*  317 */                 this.OdbcApi.getTimestampStruct(arrayOfByte9, i5, i7, i8, i9, i10, i11, i12);
/*      */ 
/*  319 */                 this.boundParams[(paramInt1 - 1)].resetBindDataBuffer(arrayOfByte9);
/*      */ 
/*  321 */                 Integer localInteger = new Integer(i12);
/*  322 */                 String str = localInteger.toString();
/*      */ 
/*  324 */                 char[] arrayOfChar = str.toCharArray();
/*      */ 
/*  326 */                 for (j = arrayOfChar.length; (j > 0) && 
/*  327 */                   (arrayOfChar[(j - 1)] == '0'); j--);
/*  332 */                 if (i12 == 0) {
/*  333 */                   j = 1;
/*      */                 }
/*      */ 
/*  336 */                 m = 20 + j;
/*      */ 
/*  338 */                 this.OdbcApi.SQLBindInOutParameterTimestamp(this.hStmt, paramInt1, 29, 9, arrayOfByte9, arrayOfByte1, arrayOfLong);
/*      */               }
/*      */               else
/*      */               {
/*  342 */                 throw new SQLException("Type mismatch between the set function and registerOutParameter");
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/*      */               Object localObject2;
/*      */               Calendar localCalendar;
/*      */               int i2;
/*      */               int i4;
/*      */               byte[] arrayOfByte8;
/*  344 */               if (paramInt2 == 9) {
/*  345 */                 if (this.boundParams[(paramInt1 - 1)].boundType == 91)
/*      */                 {
/*  349 */                   localObject2 = (java.sql.Date)this.boundParams[(paramInt1 - 1)].boundValue;
/*  350 */                   localCalendar = Calendar.getInstance();
/*  351 */                   localCalendar.setTime((java.util.Date)localObject2);
/*      */ 
/*  353 */                   i2 = localCalendar.get(1);
/*  354 */                   i4 = localCalendar.get(2);
/*  355 */                   i5 = localCalendar.get(5);
/*      */ 
/*  357 */                   i4 += 1;
/*      */ 
/*  359 */                   arrayOfByte8 = new byte[6];
/*  360 */                   this.OdbcApi.getDateStruct(arrayOfByte8, i2, i4, i5);
/*      */ 
/*  362 */                   this.boundParams[(paramInt1 - 1)].resetBindDataBuffer(arrayOfByte8);
/*      */ 
/*  364 */                   this.OdbcApi.SQLBindInOutParameterDate(this.hStmt, paramInt1, this.boundParams[(paramInt1 - 1)].scale, arrayOfByte8, arrayOfByte1, arrayOfLong);
/*      */                 }
/*      */                 else
/*      */                 {
/*  368 */                   throw new SQLException("Type mismatch between the set function and registerOutParameter");
/*      */                 }
/*  370 */               } else if (paramInt2 == 10) {
/*  371 */                 if (this.boundParams[(paramInt1 - 1)].boundType == 92)
/*      */                 {
/*  375 */                   localObject2 = (Time)this.boundParams[(paramInt1 - 1)].boundValue;
/*  376 */                   localCalendar = Calendar.getInstance();
/*  377 */                   localCalendar.setTime((java.util.Date)localObject2);
/*      */ 
/*  379 */                   i2 = localCalendar.get(11);
/*  380 */                   i4 = localCalendar.get(12);
/*  381 */                   i5 = localCalendar.get(13);
/*      */ 
/*  384 */                   arrayOfByte8 = new byte[6];
/*  385 */                   this.OdbcApi.getTimeStruct(arrayOfByte8, i2, i4, i5);
/*      */ 
/*  387 */                   this.boundParams[(paramInt1 - 1)].resetBindDataBuffer(arrayOfByte8);
/*      */ 
/*  389 */                   this.OdbcApi.SQLBindInOutParameterTime(this.hStmt, paramInt1, this.boundParams[(paramInt1 - 1)].scale, arrayOfByte8, arrayOfByte1, arrayOfLong);
/*      */                 }
/*      */                 else
/*      */                 {
/*  393 */                   throw new SQLException("Type mismatch between the set function and registerOutParameter");
/*      */                 }
/*  395 */               } else if (paramInt2 == -1) {
/*  396 */                 if ((this.boundParams[(paramInt1 - 1)].boundType == -1) || (this.boundParams[(paramInt1 - 1)].boundType == 12) || (this.boundParams[(paramInt1 - 1)].boundType == 1))
/*      */                 {
/*  400 */                   if ((this.boundParams[(paramInt1 - 1)].boundValue instanceof InputStream)) {
/*  401 */                     localObject2 = new byte[8000];
/*  402 */                     for (int n = 0; n < localObject2.length; n++) {
/*  403 */                       localObject2[n] = 0;
/*      */                     }
/*  405 */                     for (n = 0; n < arrayOfByte2.length; n++) {
/*  406 */                       localObject2[n] = arrayOfByte2[n];
/*      */                     }
/*      */ 
/*  409 */                     this.boundParams[(paramInt1 - 1)].resetBindDataBuffer((byte[])localObject2);
/*      */ 
/*  411 */                     byte[] arrayOfByte3 = getLengthBuf(paramInt1);
/*      */ 
/*  413 */                     this.OdbcApi.SQLBindInOutParameterAtExec(this.hStmt, paramInt1, 1, -1, localObject2.length, (byte[])localObject2, this.boundParams[(paramInt1 - 1)].getInputStreamLen(), arrayOfByte3, arrayOfLong);
/*      */                   }
/*  416 */                   else if ((this.boundParams[(paramInt1 - 1)].boundValue instanceof String)) {
/*  417 */                     localObject2 = null;
/*      */                     try {
/*  419 */                       localObject2 = ((String)this.boundParams[(paramInt1 - 1)].boundValue).getBytes(this.OdbcApi.charSet);
/*      */                     } catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
/*  421 */                       throw new SQLException(localUnsupportedEncodingException2.getMessage());
/*      */                     }
/*  423 */                     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream((byte[])localObject2);
/*  424 */                     this.boundParams[(paramInt1 - 1)].setInputStream(localByteArrayInputStream, localObject2.length);
/*      */ 
/*  426 */                     byte[] arrayOfByte5 = new byte[8000];
/*  427 */                     for (i4 = 0; i4 < arrayOfByte5.length; i4++) {
/*  428 */                       arrayOfByte5[i4] = 0;
/*      */                     }
/*      */ 
/*  431 */                     JdbcOdbc.intTo4Bytes(paramInt1, arrayOfByte5);
/*      */ 
/*  433 */                     this.boundParams[(paramInt1 - 1)].resetBindDataBuffer(arrayOfByte5);
/*      */ 
/*  435 */                     byte[] arrayOfByte7 = getLengthBuf(paramInt1);
/*      */ 
/*  437 */                     this.OdbcApi.SQLBindInOutParameterAtExec(this.hStmt, paramInt1, 1, -1, arrayOfByte5.length, arrayOfByte5, this.boundParams[(paramInt1 - 1)].getInputStreamLen(), arrayOfByte7, arrayOfLong);
/*      */                   }
/*      */                   else
/*      */                   {
/*  441 */                     throw new UnsupportedOperationException();
/*      */                   }
/*      */                 }
/*  444 */                 else throw new SQLException("Type mismatch between the set function and registerOutParameter");
/*      */               }
/*  446 */               else if (paramInt2 == -4) {
/*  447 */                 if ((this.boundParams[(paramInt1 - 1)].boundType == -4) || (this.boundParams[(paramInt1 - 1)].boundType == -3) || (this.boundParams[(paramInt1 - 1)].boundType == -2))
/*      */                 {
/*      */                   byte[] arrayOfByte4;
/*  451 */                   if ((this.boundParams[(paramInt1 - 1)].boundValue instanceof InputStream)) {
/*  452 */                     localObject2 = new byte[8000];
/*  453 */                     for (int i1 = 0; i1 < localObject2.length; i1++) {
/*  454 */                       localObject2[i1] = 48;
/*      */                     }
/*  456 */                     for (i1 = 0; i1 < arrayOfByte2.length; i1++) {
/*  457 */                       localObject2[i1] = arrayOfByte2[i1];
/*      */                     }
/*      */ 
/*  460 */                     this.boundParams[(paramInt1 - 1)].resetBindDataBuffer((byte[])localObject2);
/*      */ 
/*  462 */                     arrayOfByte4 = getLengthBuf(paramInt1);
/*      */ 
/*  464 */                     this.OdbcApi.SQLBindInOutParameterAtExec(this.hStmt, paramInt1, -2, -4, localObject2.length, (byte[])localObject2, this.boundParams[(paramInt1 - 1)].getInputStreamLen(), arrayOfByte4, arrayOfLong);
/*      */                   }
/*  467 */                   else if ((this.boundParams[(paramInt1 - 1)].boundValue instanceof byte[])) {
/*  468 */                     localObject2 = (byte[])this.boundParams[(paramInt1 - 1)].boundValue;
/*  469 */                     this.boundParams[(paramInt1 - 1)].setInputStream(new ByteArrayInputStream((byte[])localObject2), localObject2.length);
/*      */ 
/*  471 */                     arrayOfByte4 = new byte[8000];
/*  472 */                     for (int i3 = 0; i3 < arrayOfByte4.length; i3++) {
/*  473 */                       arrayOfByte4[i3] = 48;
/*      */                     }
/*      */ 
/*  476 */                     JdbcOdbc.intTo4Bytes(paramInt1, arrayOfByte4);
/*      */ 
/*  478 */                     this.boundParams[(paramInt1 - 1)].resetBindDataBuffer(arrayOfByte4);
/*      */ 
/*  480 */                     byte[] arrayOfByte6 = getLengthBuf(paramInt1);
/*      */ 
/*  482 */                     this.OdbcApi.SQLBindInOutParameterAtExec(this.hStmt, paramInt1, -2, -4, arrayOfByte4.length, arrayOfByte4, this.boundParams[(paramInt1 - 1)].getInputStreamLen(), arrayOfByte6, arrayOfLong);
/*      */                   }
/*      */                   else
/*      */                   {
/*  487 */                     throw new UnsupportedOperationException();
/*      */                   }
/*      */                 } else {
/*  490 */                   throw new SQLException("Type mismatch between the set function and registerOutParameter");
/*      */                 }
/*      */               }
/*  493 */               else throw new UnsupportedOperationException(); 
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  498 */     else if (paramInt2 == 0) {
/*  499 */       this.OdbcApi.SQLBindOutParameterNull(this.hStmt, paramInt1, paramInt2, i, this.boundParams[(paramInt1 - 1)].scale, arrayOfByte1, arrayOfLong);
/*      */     }
/*  503 */     else if ((paramInt2 == 8) || (paramInt2 == 6) || (paramInt2 == 7)) {
/*  504 */       this.OdbcApi.SQLBindOutParameterFixed(this.hStmt, paramInt1, 8, i, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */     }
/*  507 */     else if (paramInt2 == 4) {
/*  508 */       this.OdbcApi.SQLBindOutParameterFixed(this.hStmt, paramInt1, paramInt2, i, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */     }
/*  511 */     else if (paramInt2 == -6) {
/*  512 */       this.OdbcApi.SQLBindOutParameterFixed(this.hStmt, paramInt1, paramInt2, i, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */     }
/*  515 */     else if (paramInt2 == 5) {
/*  516 */       this.OdbcApi.SQLBindOutParameterFixed(this.hStmt, paramInt1, paramInt2, i, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */     }
/*  519 */     else if (paramInt2 == -7) {
/*  520 */       this.OdbcApi.SQLBindOutParameterFixed(this.hStmt, paramInt1, paramInt2, 1, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */     }
/*  523 */     else if (paramInt2 == -5) {
/*  524 */       this.OdbcApi.SQLBindOutParameterFixed(this.hStmt, paramInt1, paramInt2, i, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */     }
/*  527 */     else if ((paramInt2 == 2) || (paramInt2 == 3))
/*      */     {
/*  530 */       this.OdbcApi.SQLBindOutParameterString(this.hStmt, paramInt1, paramInt2, paramInt3, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */     }
/*  533 */     else if (paramInt2 == 11) {
/*  534 */       this.OdbcApi.SQLBindOutParameterTimestamp(this.hStmt, paramInt1, i, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */     }
/*  537 */     else if (paramInt2 == 9) {
/*  538 */       this.OdbcApi.SQLBindOutParameterDate(this.hStmt, paramInt1, this.boundParams[(paramInt1 - 1)].scale, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */     }
/*  541 */     else if (paramInt2 == 10) {
/*  542 */       this.OdbcApi.SQLBindOutParameterTime(this.hStmt, paramInt1, this.boundParams[(paramInt1 - 1)].scale, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */     }
/*  545 */     else if ((paramInt2 == -2) || (paramInt2 == -3) || (paramInt2 == -4)) {
/*  546 */       this.OdbcApi.SQLBindOutParameterBinary(this.hStmt, paramInt1, paramInt2, i, paramInt3, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */     }
/*  550 */     else if ((paramInt2 == 1) || (paramInt2 == 12) || (paramInt2 == -1)) {
/*  551 */       this.OdbcApi.SQLBindOutParameterString(this.hStmt, paramInt1, paramInt2, paramInt3, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */     }
/*      */     else
/*      */     {
/*  555 */       this.OdbcApi.SQLBindOutParameterString(this.hStmt, paramInt1, paramInt2, paramInt3, arrayOfByte2, arrayOfByte1, arrayOfLong);
/*      */     }
/*      */ 
/*  562 */     this.boundParams[(paramInt1 - 1)].pA1 = arrayOfLong[0];
/*  563 */     this.boundParams[(paramInt1 - 1)].pA2 = arrayOfLong[1];
/*  564 */     this.boundParams[(paramInt1 - 1)].pB1 = arrayOfLong[2];
/*  565 */     this.boundParams[(paramInt1 - 1)].pB2 = arrayOfLong[3];
/*      */   }
/*      */ 
/*      */   public boolean wasNull()
/*      */     throws SQLException
/*      */   {
/*  577 */     return this.lastParameterNull;
/*      */   }
/*      */ 
/*      */   public String getString(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  589 */     if (isNull(paramInt)) {
/*  590 */       return null;
/*      */     }
/*      */ 
/*  595 */     int i = getSqlType(paramInt);
/*      */ 
/*  599 */     String str = null;
/*      */     try
/*      */     {
/*  602 */       byte[] arrayOfByte = getDataBuf(paramInt);
/*  603 */       if (arrayOfByte != null)
/*      */       {
/*  605 */         str = BytesToChars(this.OdbcApi.charSet, arrayOfByte);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*      */     {
/*      */     }
/*      */ 
/*  613 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  614 */       this.OdbcApi.getTracer().trace("String value for OUT parameter " + paramInt + "=" + str);
/*      */     }
/*      */ 
/*  617 */     return str;
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  635 */     if (isNull(paramInt)) {
/*  636 */       return false;
/*      */     }
/*      */ 
/*  640 */     if (getInt(paramInt) == 1) {
/*  641 */       return true;
/*      */     }
/*  643 */     return false;
/*      */   }
/*      */ 
/*      */   public byte getByte(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  656 */     return (byte)getInt(paramInt);
/*      */   }
/*      */ 
/*      */   public short getShort(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  669 */     return (short)getInt(paramInt);
/*      */   }
/*      */ 
/*      */   public int getInt(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  681 */     if (isNull(paramInt)) {
/*  682 */       return 0;
/*      */     }
/*      */ 
/*  686 */     return this.OdbcApi.bufferToInt(getDataBuf(paramInt));
/*      */   }
/*      */ 
/*      */   public long getLong(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  698 */     if (isNull(paramInt)) {
/*  699 */       return 0L;
/*      */     }
/*      */ 
/*  703 */     return this.OdbcApi.bufferToLong(getDataBuf(paramInt));
/*      */   }
/*      */ 
/*      */   public float getFloat(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  716 */     if (isNull(paramInt)) {
/*  717 */       return 0.0F;
/*      */     }
/*      */ 
/*  721 */     return (float)this.OdbcApi.bufferToDouble(getDataBuf(paramInt));
/*      */   }
/*      */ 
/*      */   public double getDouble(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  733 */     if (isNull(paramInt)) {
/*  734 */       return 0.0D;
/*      */     }
/*      */ 
/*  738 */     return this.OdbcApi.bufferToDouble(getDataBuf(paramInt));
/*      */   }
/*      */ 
/*      */   public BigDecimal getBigDecimal(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  751 */     if (isNull(paramInt1)) {
/*  752 */       return null;
/*      */     }
/*      */ 
/*  755 */     BigDecimal localBigDecimal = new BigDecimal(getString(paramInt1).trim());
/*      */ 
/*  759 */     return localBigDecimal.setScale(paramInt2, 6);
/*      */   }
/*      */ 
/*      */   public byte[] getBytes(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  772 */     if (isNull(paramInt)) {
/*  773 */       return null;
/*      */     }
/*      */ 
/*  776 */     if ((this.boundParams[(paramInt - 1)].isInOutParameter()) || (this.boundParams[(paramInt - 1)].isOutputParameter()))
/*      */     {
/*  779 */       int i = getParamLength(paramInt);
/*  780 */       byte[] arrayOfByte1 = getDataBuf(paramInt);
/*  781 */       if (i < arrayOfByte1.length)
/*      */       {
/*  783 */         byte[] arrayOfByte2 = new byte[i];
/*  784 */         for (int j = 0; j < getParamLength(paramInt); j++)
/*  785 */           arrayOfByte2[j] = arrayOfByte1[j];
/*  786 */         this.boundParams[(paramInt - 1)].resetBindDataBuffer(arrayOfByte2);
/*  787 */         return arrayOfByte2;
/*      */       }
/*  789 */       return arrayOfByte1;
/*      */     }
/*      */ 
/*  793 */     return hexStringToByteArray(getString(paramInt).trim());
/*      */   }
/*      */ 
/*      */   public java.sql.Date getDate(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  806 */     if (isNull(paramInt)) {
/*  807 */       return null;
/*      */     }
/*      */ 
/*  811 */     byte[] arrayOfByte1 = getDataBuf(paramInt);
/*  812 */     byte[] arrayOfByte2 = new byte[11];
/*      */ 
/*  814 */     this.OdbcApi.convertDateString(arrayOfByte1, arrayOfByte2);
/*  815 */     return java.sql.Date.valueOf(new String(arrayOfByte2).trim());
/*      */   }
/*      */ 
/*      */   public Time getTime(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  827 */     if (isNull(paramInt)) {
/*  828 */       return null;
/*      */     }
/*      */ 
/*  831 */     byte[] arrayOfByte1 = getDataBuf(paramInt);
/*  832 */     byte[] arrayOfByte2 = new byte[9];
/*      */ 
/*  834 */     this.OdbcApi.convertTimeString(arrayOfByte1, arrayOfByte2);
/*  835 */     return Time.valueOf(new String(arrayOfByte2).trim());
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  846 */     if (isNull(paramInt)) {
/*  847 */       return null;
/*      */     }
/*      */ 
/*  850 */     byte[] arrayOfByte1 = getDataBuf(paramInt);
/*  851 */     byte[] arrayOfByte2 = new byte[30];
/*      */ 
/*  853 */     this.OdbcApi.convertTimestampString(arrayOfByte1, arrayOfByte2);
/*  854 */     return Timestamp.valueOf(new String(arrayOfByte2).trim());
/*      */   }
/*      */ 
/*      */   public Object getObject(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  869 */     Object localObject = null;
/*      */ 
/*  872 */     int i = getSqlType(paramInt);
/*      */ 
/*  875 */     if (isNull(paramInt)) {
/*  876 */       return null;
/*      */     }
/*      */ 
/*  882 */     switch (i)
/*      */     {
/*      */     case -1:
/*      */     case 1:
/*      */     case 12:
/*  899 */       localObject = getString(paramInt);
/*  900 */       break;
/*      */     case 2:
/*      */     case 3:
/*  910 */       if (paramInt <= 200)
/*  911 */         localObject = getBigDecimal(paramInt, this.scalez[paramInt]);
/*      */       else
/*  913 */         localObject = getBigDecimal(paramInt, 4);
/*  914 */       break;
/*      */     case -7:
/*  917 */       localObject = new Boolean(getBoolean(paramInt));
/*      */ 
/*  919 */       break;
/*      */     case -6:
/*  922 */       localObject = new Integer(getByte(paramInt));
/*      */ 
/*  924 */       break;
/*      */     case 5:
/*  927 */       localObject = new Integer(getShort(paramInt));
/*      */ 
/*  929 */       break;
/*      */     case 4:
/*  932 */       localObject = new Integer(getInt(paramInt));
/*      */ 
/*  934 */       break;
/*      */     case -5:
/*  937 */       localObject = new Long(getLong(paramInt));
/*  938 */       break;
/*      */     case 7:
/*  941 */       localObject = new Float(getFloat(paramInt));
/*  942 */       break;
/*      */     case 6:
/*      */     case 8:
/*  946 */       localObject = new Double(getDouble(paramInt));
/*      */ 
/*  948 */       break;
/*      */     case -4:
/*      */     case -3:
/*      */     case -2:
/*  962 */       localObject = getBytes(paramInt);
/*  963 */       break;
/*      */     case 91:
/*  966 */       localObject = getDate(paramInt);
/*  967 */       break;
/*      */     case 92:
/*  970 */       localObject = getTime(paramInt);
/*  971 */       break;
/*      */     case 93:
/*  974 */       localObject = getTimestamp(paramInt);
/*      */     }
/*      */ 
/*  979 */     return localObject;
/*      */   }
/*      */ 
/*      */   public BigDecimal getBigDecimal(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  990 */     if (isNull(paramInt))
/*      */     {
/*  992 */       return null;
/*      */     }
/*      */ 
/*  995 */     BigDecimal localBigDecimal = new BigDecimal(getString(paramInt).trim());
/*      */ 
/* 1000 */     return localBigDecimal;
/*      */   }
/*      */ 
/*      */   public Object getObject(int paramInt, Map<String, Class<?>> paramMap)
/*      */     throws SQLException
/*      */   {
/* 1008 */     return null;
/*      */   }
/*      */ 
/*      */   public Ref getRef(int paramInt) throws SQLException
/*      */   {
/* 1013 */     return null;
/*      */   }
/*      */ 
/*      */   public Blob getBlob(int paramInt) throws SQLException
/*      */   {
/* 1018 */     return null;
/*      */   }
/*      */ 
/*      */   public Clob getClob(int paramInt) throws SQLException
/*      */   {
/* 1023 */     return null;
/*      */   }
/*      */ 
/*      */   public Array getArray(int paramInt) throws SQLException
/*      */   {
/* 1028 */     return null;
/*      */   }
/*      */ 
/*      */   public java.sql.Date getDate(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 1035 */     long l = 0L;
/*      */ 
/* 1038 */     if (getDate(paramInt) == null) {
/* 1039 */       return null;
/*      */     }
/* 1041 */     if (getDate(paramInt) != null)
/*      */     {
/* 1043 */       l = this.utils.convertFromGMT(getDate(paramInt), paramCalendar);
/*      */     }
/*      */ 
/* 1046 */     if (l == 0L) {
/* 1047 */       return null;
/*      */     }
/*      */ 
/* 1050 */     return new java.sql.Date(l);
/*      */   }
/*      */ 
/*      */   public Time getTime(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 1058 */     long l = 0L;
/*      */ 
/* 1060 */     if (getTime(paramInt) == null) {
/* 1061 */       return null;
/*      */     }
/* 1063 */     if (getTime(paramInt) != null)
/*      */       try
/*      */       {
/* 1066 */         l = this.utils.convertFromGMT(getTime(paramInt), paramCalendar);
/*      */       }
/*      */       catch (Exception localException) {
/*      */       }
/* 1070 */     if (l == 0L) {
/* 1071 */       return null;
/*      */     }
/*      */ 
/* 1074 */     return new Time(l);
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(int paramInt, Calendar paramCalendar)
/*      */     throws SQLException
/*      */   {
/* 1083 */     long l = 0L;
/*      */ 
/* 1085 */     if (getTimestamp(paramInt) == null) {
/* 1086 */       return null;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1091 */       l = this.utils.convertFromGMT(getTimestamp(paramInt), paramCalendar);
/*      */     } catch (Exception localException) {
/*      */     }
/* 1094 */     if (l == 0L) {
/* 1095 */       return null;
/*      */     }
/*      */ 
/* 1098 */     return new Timestamp(l);
/*      */   }
/*      */ 
/*      */   public void registerOutParameter(int paramInt1, int paramInt2, String paramString)
/*      */     throws SQLException
/*      */   {
/* 1107 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   protected boolean isNull(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1122 */     if (!isOutputParameter(paramInt)) {
/* 1123 */       throw new SQLException("Parameter " + paramInt + " is not an OUTPUT parameter");
/*      */     }
/*      */ 
/* 1127 */     boolean bool = false;
/* 1128 */     bool = getParamLength(paramInt) == -1;
/* 1129 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1130 */       this.OdbcApi.getTracer().trace("Output Parameter " + paramInt + " null: " + bool);
/*      */     }
/*      */ 
/* 1133 */     this.lastParameterNull = bool;
/* 1134 */     return bool;
/*      */   }
/*      */ 
/*      */   protected void setOutputParameter(int paramInt, boolean paramBoolean)
/*      */   {
/* 1191 */     if ((paramInt >= 1) && (paramInt <= this.numParams))
/*      */     {
/* 1193 */       this.boundParams[(paramInt - 1)].setOutputParameter(paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean isOutputParameter(int paramInt)
/*      */   {
/* 1205 */     boolean bool = false;
/*      */ 
/* 1209 */     if ((paramInt >= 1) && (paramInt <= this.numParams))
/*      */     {
/* 1211 */       bool = this.boundParams[(paramInt - 1)].isOutputParameter();
/*      */     }
/* 1213 */     return bool;
/*      */   }
/*      */ 
/*      */   public synchronized void close()
/*      */     throws SQLException
/*      */   {
/* 1221 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1222 */       this.OdbcApi.getTracer().trace("*Statement.close");
/*      */     }
/*      */ 
/* 1227 */     clearMyResultSet();
/*      */     try
/*      */     {
/* 1232 */       clearWarnings();
/* 1233 */       if (this.hStmt != 0L)
/*      */       {
/* 1235 */         if (this.closeCalledFromFinalize == true) {
/* 1236 */           if (!this.myConnection.isFreeStmtsFromConnectionOnly()) {
/* 1237 */             this.OdbcApi.SQLFreeStmt(this.hStmt, 1);
/*      */           }
/*      */         }
/*      */         else {
/* 1241 */           this.OdbcApi.SQLFreeStmt(this.hStmt, 1);
/*      */         }
/* 1243 */         this.hStmt = 0L;
/* 1244 */         FreeParams();
/* 1245 */         for (int i = 1; (this.boundParams != null) && (i <= this.boundParams.length); i++)
/*      */         {
/* 1247 */           this.boundParams[(i - 1)].binaryData = null;
/* 1248 */           this.boundParams[(i - 1)].initialize();
/* 1249 */           this.boundParams[(i - 1)].paramInputStream = null;
/* 1250 */           this.boundParams[(i - 1)].inputParameter = false;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*      */     }
/*      */ 
/* 1261 */     this.myConnection.deregisterStatement(this);
/*      */   }
/*      */ 
/*      */   public synchronized void FreeParams() throws NullPointerException
/*      */   {
/*      */     try
/*      */     {
/* 1268 */       for (int i = 1; i <= this.boundParams.length; i++)
/*      */       {
/* 1270 */         if (this.boundParams[(i - 1)].pA1 != 0L)
/*      */         {
/* 1272 */           JdbcOdbc.ReleaseStoredBytes(this.boundParams[(i - 1)].pA1, this.boundParams[(i - 1)].pA2);
/* 1273 */           this.boundParams[(i - 1)].pA1 = 0L;
/* 1274 */           this.boundParams[(i - 1)].pA2 = 0L;
/*      */         }
/* 1276 */         if (this.boundParams[(i - 1)].pB1 != 0L)
/*      */         {
/* 1278 */           JdbcOdbc.ReleaseStoredBytes(this.boundParams[(i - 1)].pB1, this.boundParams[(i - 1)].pB2);
/* 1279 */           this.boundParams[(i - 1)].pB1 = 0L;
/* 1280 */           this.boundParams[(i - 1)].pB2 = 0L;
/*      */         }
/* 1282 */         if (this.boundParams[(i - 1)].pC1 != 0L)
/*      */         {
/* 1284 */           JdbcOdbc.ReleaseStoredBytes(this.boundParams[(i - 1)].pC1, this.boundParams[(i - 1)].pC2);
/* 1285 */           this.boundParams[(i - 1)].pC1 = 0L;
/* 1286 */           this.boundParams[(i - 1)].pC2 = 0L;
/*      */         }
/* 1288 */         if (this.boundParams[(i - 1)].pS1 != 0L)
/*      */         {
/* 1290 */           JdbcOdbc.ReleaseStoredChars(this.boundParams[(i - 1)].pS1, this.boundParams[(i - 1)].pS2);
/* 1291 */           this.boundParams[(i - 1)].pS1 = 0L;
/* 1292 */           this.boundParams[(i - 1)].pS2 = 0L;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (NullPointerException localNullPointerException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public URL getURL(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1308 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setURL(String paramString, URL paramURL) throws SQLException {
/* 1312 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setNull(String paramString, int paramInt) throws SQLException {
/* 1316 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setBoolean(String paramString, boolean paramBoolean) throws SQLException {
/* 1320 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setByte(String paramString, byte paramByte)
/*      */     throws SQLException
/*      */   {
/* 1326 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setShort(String paramString, short paramShort) throws SQLException {
/* 1330 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setInt(String paramString, int paramInt) throws SQLException {
/* 1334 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setLong(String paramString, long paramLong) throws SQLException {
/* 1338 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setFloat(String paramString, float paramFloat) throws SQLException {
/* 1342 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setDouble(String paramString, double paramDouble) throws SQLException {
/* 1346 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setBigDecimal(String paramString, BigDecimal paramBigDecimal) throws SQLException {
/* 1350 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setString(String paramString1, String paramString2) throws SQLException {
/* 1354 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setBytes(String paramString, byte[] paramArrayOfByte) throws SQLException {
/* 1358 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setDate(String paramString, java.sql.Date paramDate) throws SQLException
/*      */   {
/* 1363 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setTime(String paramString, Time paramTime) throws SQLException
/*      */   {
/* 1368 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setTimestamp(String paramString, Timestamp paramTimestamp) throws SQLException
/*      */   {
/* 1373 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setAsciiStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException
/*      */   {
/* 1378 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setBinaryStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException
/*      */   {
/* 1383 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setObject(String paramString, Object paramObject, int paramInt1, int paramInt2) throws SQLException
/*      */   {
/* 1388 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setObject(String paramString, Object paramObject, int paramInt) throws SQLException
/*      */   {
/* 1393 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setObject(String paramString, Object paramObject) throws SQLException {
/* 1397 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setCharacterStream(String paramString, Reader paramReader, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1403 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setDate(String paramString, java.sql.Date paramDate, Calendar paramCalendar) throws SQLException
/*      */   {
/* 1408 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setTime(String paramString, Time paramTime, Calendar paramCalendar) throws SQLException
/*      */   {
/* 1413 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setTimestamp(String paramString, Timestamp paramTimestamp, Calendar paramCalendar) throws SQLException
/*      */   {
/* 1418 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setNull(String paramString1, int paramInt, String paramString2) throws SQLException
/*      */   {
/* 1423 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void registerOutParameter(String paramString, int paramInt) throws SQLException
/*      */   {
/* 1428 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void registerOutParameter(String paramString, int paramInt1, int paramInt2) throws SQLException
/*      */   {
/* 1433 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void registerOutParameter(String paramString1, int paramInt, String paramString2) throws SQLException
/*      */   {
/* 1438 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public String getString(String paramString) throws SQLException {
/* 1442 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(String paramString) throws SQLException {
/* 1446 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public byte getByte(String paramString) throws SQLException {
/* 1450 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public short getShort(String paramString) throws SQLException {
/* 1454 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int getInt(String paramString) throws SQLException {
/* 1458 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public long getLong(String paramString) throws SQLException {
/* 1462 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public float getFloat(String paramString) throws SQLException {
/* 1466 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public double getDouble(String paramString) throws SQLException {
/* 1470 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public byte[] getBytes(String paramString) throws SQLException {
/* 1474 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public java.sql.Date getDate(String paramString) throws SQLException {
/* 1478 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Time getTime(String paramString) throws SQLException {
/* 1482 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(String paramString) throws SQLException {
/* 1486 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Object getObject(String paramString) throws SQLException {
/* 1490 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public BigDecimal getBigDecimal(String paramString) throws SQLException {
/* 1494 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Object getObject(String paramString, Map<String, Class<?>> paramMap)
/*      */     throws SQLException
/*      */   {
/* 1501 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Ref getRef(String paramString) throws SQLException {
/* 1505 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Blob getBlob(String paramString) throws SQLException {
/* 1509 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Clob getClob(String paramString) throws SQLException {
/* 1513 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Array getArray(String paramString) throws SQLException {
/* 1517 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public java.sql.Date getDate(String paramString, Calendar paramCalendar) throws SQLException
/*      */   {
/* 1522 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Time getTime(String paramString, Calendar paramCalendar) throws SQLException
/*      */   {
/* 1527 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Timestamp getTimestamp(String paramString, Calendar paramCalendar) throws SQLException
/*      */   {
/* 1532 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public URL getURL(String paramString) throws SQLException
/*      */   {
/* 1537 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public RowId getRowId(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1552 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public RowId getRowId(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1567 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setRowId(String paramString, RowId paramRowId)
/*      */     throws SQLException
/*      */   {
/* 1581 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setNString(String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 1597 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setNCharacterStream(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 1616 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setNClob(String paramString, NClob paramNClob)
/*      */     throws SQLException
/*      */   {
/* 1632 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setClob(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 1654 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setBlob(String paramString, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 1680 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setNClob(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/* 1701 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public NClob getNClob(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1716 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public NClob getNClob(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1731 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void setSQLXML(String paramString, SQLXML paramSQLXML)
/*      */     throws SQLException
/*      */   {
/* 1743 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public SQLXML getSQLXML(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1755 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public SQLXML getSQLXML(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1767 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean isClosed()
/*      */     throws SQLException
/*      */   {
/* 1778 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public String getNString(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1803 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public String getNString(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1827 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public Reader getNCharacterStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1846 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public Reader getNCharacterStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1864 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public Reader getCharacterStream(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1879 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public Reader getCharacterStream(String paramString)
/*      */     throws SQLException
/*      */   {
/* 1894 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void setBlob(String paramString, Blob paramBlob)
/*      */     throws SQLException
/*      */   {
/* 1908 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void setClob(String paramString, Clob paramClob)
/*      */     throws SQLException
/*      */   {
/* 1922 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public void setCharacterStream(String paramString, Reader paramReader, long paramLong)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setBinaryStream(String paramString, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setAsciiStream(String paramString, InputStream paramInputStream, long paramLong)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setAsciiStream(String paramString, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setBinaryStream(String paramString, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setCharacterStream(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setNCharacterStream(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setClob(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setBlob(String paramString, InputStream paramInputStream)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setNClob(String paramString, Reader paramReader)
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public <T> T getObject(int paramInt, Class<T> paramClass)
/*      */     throws SQLException
/*      */   {
/* 1985 */     throw new SQLFeatureNotSupportedException();
/*      */   }
/*      */ 
/*      */   public <T> T getObject(String paramString, Class<T> paramClass)
/*      */     throws SQLException
/*      */   {
/* 2015 */     throw new SQLFeatureNotSupportedException();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcCallableStatement
 * JD-Core Version:    0.6.2
 */