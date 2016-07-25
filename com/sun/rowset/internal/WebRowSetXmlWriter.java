/*     */ package com.sun.rowset.internal;
/*     */ 
/*     */ import com.sun.rowset.JdbcRowSetResourceBundle;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.io.Writer;
/*     */ import java.math.BigDecimal;
/*     */ import java.sql.Date;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import javax.sql.RowSet;
/*     */ import javax.sql.RowSetInternal;
/*     */ import javax.sql.rowset.WebRowSet;
/*     */ import javax.sql.rowset.spi.SyncProvider;
/*     */ import javax.sql.rowset.spi.XmlWriter;
/*     */ 
/*     */ public class WebRowSetXmlWriter
/*     */   implements XmlWriter, Serializable
/*     */ {
/*     */   private Writer writer;
/*     */   private Stack stack;
/*     */   private JdbcRowSetResourceBundle resBundle;
/*     */   static final long serialVersionUID = 7163134986189677641L;
/*     */ 
/*     */   public WebRowSetXmlWriter()
/*     */   {
/*     */     try
/*     */     {
/*  65 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*     */     } catch (IOException localIOException) {
/*  67 */       throw new RuntimeException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeXML(WebRowSet paramWebRowSet, Writer paramWriter)
/*     */     throws SQLException
/*     */   {
/*  97 */     this.stack = new Stack();
/*  98 */     this.writer = paramWriter;
/*  99 */     writeRowSet(paramWebRowSet);
/*     */   }
/*     */ 
/*     */   public void writeXML(WebRowSet paramWebRowSet, OutputStream paramOutputStream)
/*     */     throws SQLException
/*     */   {
/* 130 */     this.stack = new Stack();
/* 131 */     this.writer = new OutputStreamWriter(paramOutputStream);
/* 132 */     writeRowSet(paramWebRowSet);
/*     */   }
/*     */ 
/*     */   private void writeRowSet(WebRowSet paramWebRowSet)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 144 */       startHeader();
/*     */ 
/* 146 */       writeProperties(paramWebRowSet);
/* 147 */       writeMetaData(paramWebRowSet);
/* 148 */       writeData(paramWebRowSet);
/*     */ 
/* 150 */       endHeader();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 153 */       throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlwriter.ioex").toString(), new Object[] { localIOException.getMessage() }));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void startHeader() throws IOException
/*     */   {
/* 159 */     setTag("webRowSet");
/* 160 */     this.writer.write("<?xml version=\"1.0\"?>\n");
/* 161 */     this.writer.write("<webRowSet xmlns=\"http://java.sun.com/xml/ns/jdbc\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
/* 162 */     this.writer.write("xsi:schemaLocation=\"http://java.sun.com/xml/ns/jdbc http://java.sun.com/xml/ns/jdbc/webrowset.xsd\">\n");
/*     */   }
/*     */ 
/*     */   private void endHeader() throws IOException {
/* 166 */     endTag("webRowSet");
/*     */   }
/*     */ 
/*     */   private void writeProperties(WebRowSet paramWebRowSet)
/*     */     throws IOException
/*     */   {
/* 176 */     beginSection("properties");
/*     */     try
/*     */     {
/* 179 */       propString("command", processSpecialCharacters(paramWebRowSet.getCommand()));
/* 180 */       propInteger("concurrency", paramWebRowSet.getConcurrency());
/* 181 */       propString("datasource", paramWebRowSet.getDataSourceName());
/* 182 */       propBoolean("escape-processing", paramWebRowSet.getEscapeProcessing());
/*     */       try
/*     */       {
/* 186 */         propInteger("fetch-direction", paramWebRowSet.getFetchDirection());
/*     */       }
/*     */       catch (SQLException localSQLException1)
/*     */       {
/*     */       }
/*     */ 
/* 194 */       propInteger("fetch-size", paramWebRowSet.getFetchSize());
/* 195 */       propInteger("isolation-level", paramWebRowSet.getTransactionIsolation());
/*     */ 
/* 198 */       beginSection("key-columns");
/*     */ 
/* 200 */       int[] arrayOfInt = paramWebRowSet.getKeyColumns();
/* 201 */       for (int i = 0; (arrayOfInt != null) && (i < arrayOfInt.length); i++) {
/* 202 */         propInteger("column", arrayOfInt[i]);
/*     */       }
/* 204 */       endSection("key-columns");
/*     */ 
/* 207 */       beginSection("map");
/* 208 */       Map localMap = paramWebRowSet.getTypeMap();
/* 209 */       if (localMap != null) {
/* 210 */         Iterator localIterator = localMap.keySet().iterator();
/*     */ 
/* 213 */         while (localIterator.hasNext()) {
/* 214 */           str1 = (String)localIterator.next();
/* 215 */           localObject = (Class)localMap.get(str1);
/* 216 */           propString("type", str1);
/* 217 */           propString("class", ((Class)localObject).getName());
/*     */         }
/*     */       }
/* 220 */       endSection("map");
/*     */ 
/* 222 */       propInteger("max-field-size", paramWebRowSet.getMaxFieldSize());
/* 223 */       propInteger("max-rows", paramWebRowSet.getMaxRows());
/* 224 */       propInteger("query-timeout", paramWebRowSet.getQueryTimeout());
/* 225 */       propBoolean("read-only", paramWebRowSet.isReadOnly());
/*     */ 
/* 227 */       int j = paramWebRowSet.getType();
/* 228 */       Object localObject = "";
/*     */ 
/* 230 */       if (j == 1003)
/* 231 */         localObject = "ResultSet.TYPE_FORWARD_ONLY";
/* 232 */       else if (j == 1004)
/* 233 */         localObject = "ResultSet.TYPE_SCROLL_INSENSITIVE";
/* 234 */       else if (j == 1005) {
/* 235 */         localObject = "ResultSet.TYPE_SCROLL_SENSITIVE";
/*     */       }
/*     */ 
/* 238 */       propString("rowset-type", (String)localObject);
/*     */ 
/* 240 */       propBoolean("show-deleted", paramWebRowSet.getShowDeleted());
/* 241 */       propString("table-name", paramWebRowSet.getTableName());
/* 242 */       propString("url", paramWebRowSet.getUrl());
/*     */ 
/* 244 */       beginSection("sync-provider");
/*     */ 
/* 247 */       String str1 = paramWebRowSet.getSyncProvider().toString();
/* 248 */       String str2 = str1.substring(0, paramWebRowSet.getSyncProvider().toString().indexOf("@"));
/*     */ 
/* 250 */       propString("sync-provider-name", str2);
/* 251 */       propString("sync-provider-vendor", "Oracle Corporation");
/* 252 */       propString("sync-provider-version", "1.0");
/* 253 */       propInteger("sync-provider-grade", paramWebRowSet.getSyncProvider().getProviderGrade());
/* 254 */       propInteger("data-source-lock", paramWebRowSet.getSyncProvider().getDataSourceLock());
/*     */ 
/* 256 */       endSection("sync-provider");
/*     */     }
/*     */     catch (SQLException localSQLException2) {
/* 259 */       throw new IOException(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlwriter.sqlex").toString(), new Object[] { localSQLException2.getMessage() }));
/*     */     }
/*     */ 
/* 262 */     endSection("properties");
/*     */   }
/*     */ 
/*     */   private void writeMetaData(WebRowSet paramWebRowSet)
/*     */     throws IOException
/*     */   {
/* 273 */     beginSection("metadata");
/*     */     try
/*     */     {
/* 277 */       ResultSetMetaData localResultSetMetaData = paramWebRowSet.getMetaData();
/* 278 */       int i = localResultSetMetaData.getColumnCount();
/* 279 */       propInteger("column-count", i);
/*     */ 
/* 281 */       for (int j = 1; j <= i; j++) {
/* 282 */         beginSection("column-definition");
/*     */ 
/* 284 */         propInteger("column-index", j);
/* 285 */         propBoolean("auto-increment", localResultSetMetaData.isAutoIncrement(j));
/* 286 */         propBoolean("case-sensitive", localResultSetMetaData.isCaseSensitive(j));
/* 287 */         propBoolean("currency", localResultSetMetaData.isCurrency(j));
/* 288 */         propInteger("nullable", localResultSetMetaData.isNullable(j));
/* 289 */         propBoolean("signed", localResultSetMetaData.isSigned(j));
/* 290 */         propBoolean("searchable", localResultSetMetaData.isSearchable(j));
/* 291 */         propInteger("column-display-size", localResultSetMetaData.getColumnDisplaySize(j));
/* 292 */         propString("column-label", localResultSetMetaData.getColumnLabel(j));
/* 293 */         propString("column-name", localResultSetMetaData.getColumnName(j));
/* 294 */         propString("schema-name", localResultSetMetaData.getSchemaName(j));
/* 295 */         propInteger("column-precision", localResultSetMetaData.getPrecision(j));
/* 296 */         propInteger("column-scale", localResultSetMetaData.getScale(j));
/* 297 */         propString("table-name", localResultSetMetaData.getTableName(j));
/* 298 */         propString("catalog-name", localResultSetMetaData.getCatalogName(j));
/* 299 */         propInteger("column-type", localResultSetMetaData.getColumnType(j));
/* 300 */         propString("column-type-name", localResultSetMetaData.getColumnTypeName(j));
/*     */ 
/* 302 */         endSection("column-definition");
/*     */       }
/*     */     } catch (SQLException localSQLException) {
/* 305 */       throw new IOException(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlwriter.sqlex").toString(), new Object[] { localSQLException.getMessage() }));
/*     */     }
/*     */ 
/* 308 */     endSection("metadata");
/*     */   }
/*     */ 
/*     */   private void writeData(WebRowSet paramWebRowSet)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 320 */       ResultSetMetaData localResultSetMetaData = paramWebRowSet.getMetaData();
/* 321 */       int i = localResultSetMetaData.getColumnCount();
/*     */ 
/* 324 */       beginSection("data");
/*     */ 
/* 326 */       paramWebRowSet.beforeFirst();
/* 327 */       paramWebRowSet.setShowDeleted(true);
/* 328 */       while (paramWebRowSet.next()) {
/* 329 */         if ((paramWebRowSet.rowDeleted()) && (paramWebRowSet.rowInserted()))
/* 330 */           beginSection("modifyRow");
/* 331 */         else if (paramWebRowSet.rowDeleted())
/* 332 */           beginSection("deleteRow");
/* 333 */         else if (paramWebRowSet.rowInserted())
/* 334 */           beginSection("insertRow");
/*     */         else {
/* 336 */           beginSection("currentRow");
/*     */         }
/*     */ 
/* 339 */         for (int j = 1; j <= i; j++) {
/* 340 */           if (paramWebRowSet.columnUpdated(j)) {
/* 341 */             ResultSet localResultSet = paramWebRowSet.getOriginalRow();
/* 342 */             localResultSet.next();
/* 343 */             beginTag("columnValue");
/* 344 */             writeValue(j, (RowSet)localResultSet);
/* 345 */             endTag("columnValue");
/* 346 */             beginTag("updateRow");
/* 347 */             writeValue(j, paramWebRowSet);
/* 348 */             endTag("updateRow");
/*     */           } else {
/* 350 */             beginTag("columnValue");
/* 351 */             writeValue(j, paramWebRowSet);
/* 352 */             endTag("columnValue");
/*     */           }
/*     */         }
/*     */ 
/* 356 */         endSection();
/*     */       }
/* 358 */       endSection("data");
/*     */     } catch (SQLException localSQLException) {
/* 360 */       throw new IOException(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlwriter.sqlex").toString(), new Object[] { localSQLException.getMessage() }));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeValue(int paramInt, RowSet paramRowSet) throws IOException {
/*     */     try {
/* 366 */       int i = paramRowSet.getMetaData().getColumnType(paramInt);
/*     */ 
/* 368 */       switch (i) {
/*     */       case -7:
/*     */       case 16:
/* 371 */         boolean bool = paramRowSet.getBoolean(paramInt);
/* 372 */         if (paramRowSet.wasNull())
/* 373 */           writeNull();
/*     */         else
/* 375 */           writeBoolean(bool);
/* 376 */         break;
/*     */       case -6:
/*     */       case 5:
/* 379 */         short s = paramRowSet.getShort(paramInt);
/* 380 */         if (paramRowSet.wasNull())
/* 381 */           writeNull();
/*     */         else
/* 383 */           writeShort(s);
/* 384 */         break;
/*     */       case 4:
/* 386 */         int j = paramRowSet.getInt(paramInt);
/* 387 */         if (paramRowSet.wasNull())
/* 388 */           writeNull();
/*     */         else
/* 390 */           writeInteger(j);
/* 391 */         break;
/*     */       case -5:
/* 393 */         long l = paramRowSet.getLong(paramInt);
/* 394 */         if (paramRowSet.wasNull())
/* 395 */           writeNull();
/*     */         else
/* 397 */           writeLong(l);
/* 398 */         break;
/*     */       case 6:
/*     */       case 7:
/* 401 */         float f = paramRowSet.getFloat(paramInt);
/* 402 */         if (paramRowSet.wasNull())
/* 403 */           writeNull();
/*     */         else
/* 405 */           writeFloat(f);
/* 406 */         break;
/*     */       case 8:
/* 408 */         double d = paramRowSet.getDouble(paramInt);
/* 409 */         if (paramRowSet.wasNull())
/* 410 */           writeNull();
/*     */         else
/* 412 */           writeDouble(d);
/* 413 */         break;
/*     */       case 2:
/*     */       case 3:
/* 416 */         writeBigDecimal(paramRowSet.getBigDecimal(paramInt));
/* 417 */         break;
/*     */       case -4:
/*     */       case -3:
/*     */       case -2:
/* 421 */         break;
/*     */       case 91:
/* 423 */         Date localDate = paramRowSet.getDate(paramInt);
/* 424 */         if (paramRowSet.wasNull())
/* 425 */           writeNull();
/*     */         else
/* 427 */           writeLong(localDate.getTime());
/* 428 */         break;
/*     */       case 92:
/* 430 */         Time localTime = paramRowSet.getTime(paramInt);
/* 431 */         if (paramRowSet.wasNull())
/* 432 */           writeNull();
/*     */         else
/* 434 */           writeLong(localTime.getTime());
/* 435 */         break;
/*     */       case 93:
/* 437 */         Timestamp localTimestamp = paramRowSet.getTimestamp(paramInt);
/* 438 */         if (paramRowSet.wasNull())
/* 439 */           writeNull();
/*     */         else
/* 441 */           writeLong(localTimestamp.getTime());
/* 442 */         break;
/*     */       case -1:
/*     */       case 1:
/*     */       case 12:
/* 446 */         writeStringData(paramRowSet.getString(paramInt));
/* 447 */         break;
/*     */       default:
/* 449 */         System.out.println(this.resBundle.handleGetObject("wsrxmlwriter.notproper").toString());
/*     */       }
/*     */     }
/*     */     catch (SQLException localSQLException) {
/* 453 */       throw new IOException(this.resBundle.handleGetObject("wrsxmlwriter.failedwrite").toString() + localSQLException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void beginSection(String paramString)
/*     */     throws IOException
/*     */   {
/* 463 */     setTag(paramString);
/*     */ 
/* 465 */     writeIndent(this.stack.size());
/*     */ 
/* 468 */     this.writer.write("<" + paramString + ">\n");
/*     */   }
/*     */ 
/*     */   private void endSection(String paramString)
/*     */     throws IOException
/*     */   {
/* 476 */     writeIndent(this.stack.size());
/*     */ 
/* 478 */     String str = getTag();
/*     */ 
/* 480 */     if (str.indexOf("webRowSet") != -1) {
/* 481 */       str = "webRowSet";
/*     */     }
/*     */ 
/* 484 */     if (paramString.equals(str))
/*     */     {
/* 486 */       this.writer.write("</" + str + ">\n");
/*     */     }
/*     */ 
/* 490 */     this.writer.flush();
/*     */   }
/*     */ 
/*     */   private void endSection() throws IOException {
/* 494 */     writeIndent(this.stack.size());
/*     */ 
/* 497 */     String str = getTag();
/* 498 */     this.writer.write("</" + str + ">\n");
/*     */ 
/* 500 */     this.writer.flush();
/*     */   }
/*     */ 
/*     */   private void beginTag(String paramString) throws IOException
/*     */   {
/* 505 */     setTag(paramString);
/*     */ 
/* 507 */     writeIndent(this.stack.size());
/*     */ 
/* 510 */     this.writer.write("<" + paramString + ">");
/*     */   }
/*     */ 
/*     */   private void endTag(String paramString) throws IOException {
/* 514 */     String str = getTag();
/* 515 */     if (paramString.equals(str))
/*     */     {
/* 517 */       this.writer.write("</" + str + ">\n");
/*     */     }
/*     */ 
/* 521 */     this.writer.flush();
/*     */   }
/*     */ 
/*     */   private void emptyTag(String paramString) throws IOException
/*     */   {
/* 526 */     this.writer.write("<" + paramString + "/>");
/*     */   }
/*     */ 
/*     */   private void setTag(String paramString)
/*     */   {
/* 531 */     this.stack.push(paramString);
/*     */   }
/*     */ 
/*     */   private String getTag() {
/* 535 */     return (String)this.stack.pop();
/*     */   }
/*     */ 
/*     */   private void writeNull() throws IOException {
/* 539 */     emptyTag("null");
/*     */   }
/*     */ 
/*     */   private void writeStringData(String paramString) throws IOException {
/* 543 */     if (paramString == null) {
/* 544 */       writeNull();
/* 545 */     } else if (paramString.equals("")) {
/* 546 */       writeEmptyString();
/*     */     }
/*     */     else {
/* 549 */       paramString = processSpecialCharacters(paramString);
/*     */ 
/* 551 */       this.writer.write(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeString(String paramString) throws IOException {
/* 556 */     if (paramString != null)
/* 557 */       this.writer.write(paramString);
/*     */     else
/* 559 */       writeNull();
/*     */   }
/*     */ 
/*     */   private void writeShort(short paramShort)
/*     */     throws IOException
/*     */   {
/* 565 */     this.writer.write(Short.toString(paramShort));
/*     */   }
/*     */ 
/*     */   private void writeLong(long paramLong) throws IOException {
/* 569 */     this.writer.write(Long.toString(paramLong));
/*     */   }
/*     */ 
/*     */   private void writeInteger(int paramInt) throws IOException {
/* 573 */     this.writer.write(Integer.toString(paramInt));
/*     */   }
/*     */ 
/*     */   private void writeBoolean(boolean paramBoolean) throws IOException {
/* 577 */     this.writer.write(Boolean.valueOf(paramBoolean).toString());
/*     */   }
/*     */ 
/*     */   private void writeFloat(float paramFloat) throws IOException {
/* 581 */     this.writer.write(Float.toString(paramFloat));
/*     */   }
/*     */ 
/*     */   private void writeDouble(double paramDouble) throws IOException {
/* 585 */     this.writer.write(Double.toString(paramDouble));
/*     */   }
/*     */ 
/*     */   private void writeBigDecimal(BigDecimal paramBigDecimal) throws IOException {
/* 589 */     if (paramBigDecimal != null)
/* 590 */       this.writer.write(paramBigDecimal.toString());
/*     */     else
/* 592 */       emptyTag("null");
/*     */   }
/*     */ 
/*     */   private void writeIndent(int paramInt) throws IOException
/*     */   {
/* 597 */     for (int i = 1; i < paramInt; i++)
/* 598 */       this.writer.write("  ");
/*     */   }
/*     */ 
/*     */   private void propString(String paramString1, String paramString2) throws IOException
/*     */   {
/* 603 */     beginTag(paramString1);
/* 604 */     writeString(paramString2);
/* 605 */     endTag(paramString1);
/*     */   }
/*     */ 
/*     */   private void propInteger(String paramString, int paramInt) throws IOException {
/* 609 */     beginTag(paramString);
/* 610 */     writeInteger(paramInt);
/* 611 */     endTag(paramString);
/*     */   }
/*     */ 
/*     */   private void propBoolean(String paramString, boolean paramBoolean) throws IOException {
/* 615 */     beginTag(paramString);
/* 616 */     writeBoolean(paramBoolean);
/* 617 */     endTag(paramString);
/*     */   }
/*     */ 
/*     */   private void writeEmptyString() throws IOException {
/* 621 */     emptyTag("emptyString");
/*     */   }
/*     */ 
/*     */   public boolean writeData(RowSetInternal paramRowSetInternal)
/*     */   {
/* 627 */     return false;
/*     */   }
/*     */ 
/*     */   private String processSpecialCharacters(String paramString)
/*     */   {
/* 640 */     if (paramString == null) {
/* 641 */       return null;
/*     */     }
/* 643 */     char[] arrayOfChar = paramString.toCharArray();
/* 644 */     String str = "";
/*     */ 
/* 646 */     for (int i = 0; i < arrayOfChar.length; i++) {
/* 647 */       if (arrayOfChar[i] == '&')
/* 648 */         str = str.concat("&amp;");
/* 649 */       else if (arrayOfChar[i] == '<')
/* 650 */         str = str.concat("&lt;");
/* 651 */       else if (arrayOfChar[i] == '>')
/* 652 */         str = str.concat("&gt;");
/* 653 */       else if (arrayOfChar[i] == '\'')
/* 654 */         str = str.concat("&apos;");
/* 655 */       else if (arrayOfChar[i] == '"')
/* 656 */         str = str.concat("&quot;");
/*     */       else {
/* 658 */         str = str.concat(String.valueOf(arrayOfChar[i]));
/*     */       }
/*     */     }
/*     */ 
/* 662 */     paramString = str;
/* 663 */     return paramString;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 674 */     paramObjectInputStream.defaultReadObject();
/*     */     try
/*     */     {
/* 677 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*     */     } catch (IOException localIOException) {
/* 679 */       throw new RuntimeException(localIOException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.internal.WebRowSetXmlWriter
 * JD-Core Version:    0.6.2
 */