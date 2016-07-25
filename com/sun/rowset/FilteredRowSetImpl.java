/*      */ package com.sun.rowset;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.math.BigDecimal;
/*      */ import java.sql.Date;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Hashtable;
/*      */ import javax.sql.rowset.FilteredRowSet;
/*      */ import javax.sql.rowset.Predicate;
/*      */ 
/*      */ public class FilteredRowSetImpl extends WebRowSetImpl
/*      */   implements Serializable, Cloneable, FilteredRowSet
/*      */ {
/*      */   private Predicate p;
/*   52 */   private boolean onInsertRow = false;
/*      */   static final long serialVersionUID = 6178454588413509360L;
/*      */ 
/*      */   public FilteredRowSetImpl()
/*      */     throws SQLException
/*      */   {
/*      */   }
/*      */ 
/*      */   public FilteredRowSetImpl(Hashtable paramHashtable)
/*      */     throws SQLException
/*      */   {
/*   70 */     super(paramHashtable);
/*      */   }
/*      */ 
/*      */   public void setFilter(Predicate paramPredicate)
/*      */     throws SQLException
/*      */   {
/*   79 */     this.p = paramPredicate;
/*      */   }
/*      */ 
/*      */   public Predicate getFilter()
/*      */   {
/*   88 */     return this.p;
/*      */   }
/*      */ 
/*      */   protected boolean internalNext()
/*      */     throws SQLException
/*      */   {
/*  125 */     boolean bool = false;
/*      */ 
/*  127 */     for (int i = getRow(); i <= size(); i++) {
/*  128 */       bool = super.internalNext();
/*      */ 
/*  130 */       if (this.p == null) {
/*  131 */         return bool;
/*      */       }
/*  133 */       if (this.p.evaluate(this))
/*      */       {
/*      */         break;
/*      */       }
/*      */     }
/*      */ 
/*  139 */     return bool;
/*      */   }
/*      */ 
/*      */   protected boolean internalPrevious()
/*      */     throws SQLException
/*      */   {
/*  154 */     boolean bool = false;
/*      */ 
/*  158 */     for (int i = getRow(); i > 0; i--)
/*      */     {
/*  160 */       bool = super.internalPrevious();
/*      */ 
/*  162 */       if (this.p == null) {
/*  163 */         return bool;
/*      */       }
/*      */ 
/*  166 */       if (this.p.evaluate(this))
/*      */       {
/*      */         break;
/*      */       }
/*      */     }
/*      */ 
/*  172 */     return bool;
/*      */   }
/*      */ 
/*      */   protected boolean internalFirst()
/*      */     throws SQLException
/*      */   {
/*  191 */     boolean bool = super.internalFirst();
/*      */ 
/*  193 */     if (this.p == null) {
/*  194 */       return bool;
/*      */     }
/*      */ 
/*  197 */     while (bool)
/*      */     {
/*  199 */       if (this.p.evaluate(this)) {
/*      */         break;
/*      */       }
/*  202 */       bool = super.internalNext();
/*      */     }
/*  204 */     return bool;
/*      */   }
/*      */ 
/*      */   protected boolean internalLast()
/*      */     throws SQLException
/*      */   {
/*  222 */     boolean bool = super.internalLast();
/*      */ 
/*  224 */     if (this.p == null) {
/*  225 */       return bool;
/*      */     }
/*      */ 
/*  228 */     while (bool)
/*      */     {
/*  230 */       if (this.p.evaluate(this))
/*      */       {
/*      */         break;
/*      */       }
/*  234 */       bool = super.internalPrevious();
/*      */     }
/*      */ 
/*  237 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean relative(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  296 */     boolean bool2 = false;
/*  297 */     boolean bool3 = false;
/*      */ 
/*  299 */     if (getType() == 1003)
/*  300 */       throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.relative").toString());
/*      */     int i;
/*      */     boolean bool1;
/*  303 */     if (paramInt > 0)
/*      */     {
/*  305 */       i = 0;
/*  306 */       while (i < paramInt)
/*      */       {
/*  308 */         if (isAfterLast()) {
/*  309 */           return false;
/*      */         }
/*  311 */         bool2 = internalNext();
/*  312 */         i++;
/*      */       }
/*      */ 
/*  315 */       bool1 = bool2;
/*      */     } else {
/*  317 */       i = paramInt;
/*  318 */       while (i < 0)
/*      */       {
/*  320 */         if (isBeforeFirst()) {
/*  321 */           return false;
/*      */         }
/*  323 */         bool3 = internalPrevious();
/*  324 */         i++;
/*      */       }
/*  326 */       bool1 = bool3;
/*      */     }
/*  328 */     if (paramInt != 0)
/*  329 */       notifyCursorMoved();
/*  330 */     return bool1;
/*      */   }
/*      */ 
/*      */   public boolean absolute(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  383 */     boolean bool2 = false;
/*      */ 
/*  385 */     if ((paramInt == 0) || (getType() == 1003))
/*  386 */       throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.absolute").toString());
/*      */     int i;
/*      */     boolean bool1;
/*  389 */     if (paramInt > 0) {
/*  390 */       bool2 = internalFirst();
/*      */ 
/*  392 */       i = 0;
/*  393 */       while (i < paramInt - 1) {
/*  394 */         if (isAfterLast()) {
/*  395 */           return false;
/*      */         }
/*  397 */         bool2 = internalNext();
/*  398 */         i++;
/*      */       }
/*  400 */       bool1 = bool2;
/*      */     } else {
/*  402 */       bool2 = internalLast();
/*      */ 
/*  404 */       i = paramInt;
/*  405 */       while (i + 1 < 0) {
/*  406 */         if (isBeforeFirst()) {
/*  407 */           return false;
/*      */         }
/*  409 */         bool2 = internalPrevious();
/*  410 */         i++;
/*      */       }
/*  412 */       bool1 = bool2;
/*      */     }
/*  414 */     notifyCursorMoved();
/*  415 */     return bool1;
/*      */   }
/*      */ 
/*      */   public void moveToInsertRow()
/*      */     throws SQLException
/*      */   {
/*  446 */     this.onInsertRow = true;
/*  447 */     super.moveToInsertRow();
/*      */   }
/*      */ 
/*      */   public void updateInt(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  500 */     if ((this.onInsertRow) && 
/*  501 */       (this.p != null)) {
/*  502 */       boolean bool = this.p.evaluate(Integer.valueOf(paramInt2), paramInt1);
/*      */ 
/*  504 */       if (!bool) {
/*  505 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  510 */     super.updateInt(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateInt(String paramString, int paramInt)
/*      */     throws SQLException
/*      */   {
/*  537 */     updateInt(findColumn(paramString), paramInt);
/*      */   }
/*      */ 
/*      */   public void updateBoolean(int paramInt, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/*  567 */     if ((this.onInsertRow) && 
/*  568 */       (this.p != null)) {
/*  569 */       boolean bool = this.p.evaluate(Boolean.valueOf(paramBoolean), paramInt);
/*      */ 
/*  571 */       if (!bool) {
/*  572 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  577 */     super.updateBoolean(paramInt, paramBoolean);
/*      */   }
/*      */ 
/*      */   public void updateBoolean(String paramString, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/*  604 */     updateBoolean(findColumn(paramString), paramBoolean);
/*      */   }
/*      */ 
/*      */   public void updateByte(int paramInt, byte paramByte)
/*      */     throws SQLException
/*      */   {
/*  635 */     if ((this.onInsertRow) && 
/*  636 */       (this.p != null)) {
/*  637 */       boolean bool = this.p.evaluate(Byte.valueOf(paramByte), paramInt);
/*      */ 
/*  639 */       if (!bool) {
/*  640 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  645 */     super.updateByte(paramInt, paramByte);
/*      */   }
/*      */ 
/*      */   public void updateByte(String paramString, byte paramByte)
/*      */     throws SQLException
/*      */   {
/*  673 */     updateByte(findColumn(paramString), paramByte);
/*      */   }
/*      */ 
/*      */   public void updateShort(int paramInt, short paramShort)
/*      */     throws SQLException
/*      */   {
/*  704 */     if ((this.onInsertRow) && 
/*  705 */       (this.p != null)) {
/*  706 */       boolean bool = this.p.evaluate(Short.valueOf(paramShort), paramInt);
/*      */ 
/*  708 */       if (!bool) {
/*  709 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  714 */     super.updateShort(paramInt, paramShort);
/*      */   }
/*      */ 
/*      */   public void updateShort(String paramString, short paramShort)
/*      */     throws SQLException
/*      */   {
/*  741 */     updateShort(findColumn(paramString), paramShort);
/*      */   }
/*      */ 
/*      */   public void updateLong(int paramInt, long paramLong)
/*      */     throws SQLException
/*      */   {
/*  772 */     if ((this.onInsertRow) && 
/*  773 */       (this.p != null)) {
/*  774 */       boolean bool = this.p.evaluate(Long.valueOf(paramLong), paramInt);
/*      */ 
/*  776 */       if (!bool) {
/*  777 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  782 */     super.updateLong(paramInt, paramLong);
/*      */   }
/*      */ 
/*      */   public void updateLong(String paramString, long paramLong)
/*      */     throws SQLException
/*      */   {
/*  809 */     updateLong(findColumn(paramString), paramLong);
/*      */   }
/*      */ 
/*      */   public void updateFloat(int paramInt, float paramFloat)
/*      */     throws SQLException
/*      */   {
/*  839 */     if ((this.onInsertRow) && 
/*  840 */       (this.p != null)) {
/*  841 */       boolean bool = this.p.evaluate(new Float(paramFloat), paramInt);
/*      */ 
/*  843 */       if (!bool) {
/*  844 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  849 */     super.updateFloat(paramInt, paramFloat);
/*      */   }
/*      */ 
/*      */   public void updateFloat(String paramString, float paramFloat)
/*      */     throws SQLException
/*      */   {
/*  876 */     updateFloat(findColumn(paramString), paramFloat);
/*      */   }
/*      */ 
/*      */   public void updateDouble(int paramInt, double paramDouble)
/*      */     throws SQLException
/*      */   {
/*  906 */     if ((this.onInsertRow) && 
/*  907 */       (this.p != null)) {
/*  908 */       boolean bool = this.p.evaluate(new Double(paramDouble), paramInt);
/*      */ 
/*  910 */       if (!bool) {
/*  911 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  916 */     super.updateDouble(paramInt, paramDouble);
/*      */   }
/*      */ 
/*      */   public void updateDouble(String paramString, double paramDouble)
/*      */     throws SQLException
/*      */   {
/*  943 */     updateDouble(findColumn(paramString), paramDouble);
/*      */   }
/*      */ 
/*      */   public void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/*  973 */     if ((this.onInsertRow) && 
/*  974 */       (this.p != null)) {
/*  975 */       boolean bool = this.p.evaluate(paramBigDecimal, paramInt);
/*      */ 
/*  977 */       if (!bool) {
/*  978 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  983 */     super.updateBigDecimal(paramInt, paramBigDecimal);
/*      */   }
/*      */ 
/*      */   public void updateBigDecimal(String paramString, BigDecimal paramBigDecimal)
/*      */     throws SQLException
/*      */   {
/* 1010 */     updateBigDecimal(findColumn(paramString), paramBigDecimal);
/*      */   }
/*      */ 
/*      */   public void updateString(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/* 1043 */     if ((this.onInsertRow) && 
/* 1044 */       (this.p != null)) {
/* 1045 */       boolean bool = this.p.evaluate(paramString, paramInt);
/*      */ 
/* 1047 */       if (!bool) {
/* 1048 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1053 */     super.updateString(paramInt, paramString);
/*      */   }
/*      */ 
/*      */   public void updateString(String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 1080 */     updateString(findColumn(paramString1), paramString2);
/*      */   }
/*      */ 
/*      */   public void updateBytes(int paramInt, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 1109 */     String str = "";
/*      */ 
/* 1111 */     Byte[] arrayOfByte = new Byte[paramArrayOfByte.length];
/*      */ 
/* 1113 */     for (int i = 0; i < paramArrayOfByte.length; i++) {
/* 1114 */       arrayOfByte[i] = Byte.valueOf(paramArrayOfByte[i]);
/* 1115 */       str = str.concat(arrayOfByte[i].toString());
/*      */     }
/*      */ 
/* 1119 */     if ((this.onInsertRow) && 
/* 1120 */       (this.p != null)) {
/* 1121 */       boolean bool = this.p.evaluate(str, paramInt);
/*      */ 
/* 1123 */       if (!bool) {
/* 1124 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1129 */     super.updateBytes(paramInt, paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   public void updateBytes(String paramString, byte[] paramArrayOfByte)
/*      */     throws SQLException
/*      */   {
/* 1156 */     updateBytes(findColumn(paramString), paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   public void updateDate(int paramInt, Date paramDate)
/*      */     throws SQLException
/*      */   {
/* 1187 */     if ((this.onInsertRow) && 
/* 1188 */       (this.p != null)) {
/* 1189 */       boolean bool = this.p.evaluate(paramDate, paramInt);
/*      */ 
/* 1191 */       if (!bool) {
/* 1192 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1197 */     super.updateDate(paramInt, paramDate);
/*      */   }
/*      */ 
/*      */   public void updateDate(String paramString, Date paramDate)
/*      */     throws SQLException
/*      */   {
/* 1226 */     updateDate(findColumn(paramString), paramDate);
/*      */   }
/*      */ 
/*      */   public void updateTime(int paramInt, Time paramTime)
/*      */     throws SQLException
/*      */   {
/* 1257 */     if ((this.onInsertRow) && 
/* 1258 */       (this.p != null)) {
/* 1259 */       boolean bool = this.p.evaluate(paramTime, paramInt);
/*      */ 
/* 1261 */       if (!bool) {
/* 1262 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1267 */     super.updateTime(paramInt, paramTime);
/*      */   }
/*      */ 
/*      */   public void updateTime(String paramString, Time paramTime)
/*      */     throws SQLException
/*      */   {
/* 1296 */     updateTime(findColumn(paramString), paramTime);
/*      */   }
/*      */ 
/*      */   public void updateTimestamp(int paramInt, Timestamp paramTimestamp)
/*      */     throws SQLException
/*      */   {
/* 1328 */     if ((this.onInsertRow) && 
/* 1329 */       (this.p != null)) {
/* 1330 */       boolean bool = this.p.evaluate(paramTimestamp, paramInt);
/*      */ 
/* 1332 */       if (!bool) {
/* 1333 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1338 */     super.updateTimestamp(paramInt, paramTimestamp);
/*      */   }
/*      */ 
/*      */   public void updateTimestamp(String paramString, Timestamp paramTimestamp)
/*      */     throws SQLException
/*      */   {
/* 1370 */     updateTimestamp(findColumn(paramString), paramTimestamp);
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 1398 */     if ((this.onInsertRow) && 
/* 1399 */       (this.p != null)) {
/* 1400 */       boolean bool = this.p.evaluate(paramInputStream, paramInt1);
/*      */ 
/* 1402 */       if (!bool) {
/* 1403 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1408 */     super.updateAsciiStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1432 */     updateAsciiStream(findColumn(paramString), paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 1467 */     if ((this.onInsertRow) && 
/* 1468 */       (this.p != null)) {
/* 1469 */       boolean bool = this.p.evaluate(paramReader, paramInt1);
/*      */ 
/* 1471 */       if (!bool) {
/* 1472 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1477 */     super.updateCharacterStream(paramInt1, paramReader, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateCharacterStream(String paramString, Reader paramReader, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1508 */     updateCharacterStream(findColumn(paramString), paramReader, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 1541 */     if ((this.onInsertRow) && 
/* 1542 */       (this.p != null)) {
/* 1543 */       boolean bool = this.p.evaluate(paramInputStream, paramInt1);
/*      */ 
/* 1545 */       if (!bool) {
/* 1546 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1551 */     super.updateBinaryStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1582 */     updateBinaryStream(findColumn(paramString), paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   public void updateObject(int paramInt, Object paramObject)
/*      */     throws SQLException
/*      */   {
/* 1612 */     if ((this.onInsertRow) && 
/* 1613 */       (this.p != null)) {
/* 1614 */       boolean bool = this.p.evaluate(paramObject, paramInt);
/*      */ 
/* 1616 */       if (!bool) {
/* 1617 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1622 */     super.updateObject(paramInt, paramObject);
/*      */   }
/*      */ 
/*      */   public void updateObject(String paramString, Object paramObject)
/*      */     throws SQLException
/*      */   {
/* 1649 */     updateObject(findColumn(paramString), paramObject);
/*      */   }
/*      */ 
/*      */   public void updateObject(int paramInt1, Object paramObject, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 1684 */     if ((this.onInsertRow) && 
/* 1685 */       (this.p != null)) {
/* 1686 */       boolean bool = this.p.evaluate(paramObject, paramInt1);
/*      */ 
/* 1688 */       if (!bool) {
/* 1689 */         throw new SQLException(this.resBundle.handleGetObject("filteredrowsetimpl.notallowed").toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1694 */     super.updateObject(paramInt1, paramObject, paramInt2);
/*      */   }
/*      */ 
/*      */   public void updateObject(String paramString, Object paramObject, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 1726 */     updateObject(findColumn(paramString), paramObject, paramInt);
/*      */   }
/*      */ 
/*      */   public void insertRow()
/*      */     throws SQLException
/*      */   {
/* 1746 */     this.onInsertRow = false;
/* 1747 */     super.insertRow();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1757 */     paramObjectInputStream.defaultReadObject();
/*      */     try
/*      */     {
/* 1760 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*      */     } catch (IOException localIOException) {
/* 1762 */       throw new RuntimeException(localIOException);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.FilteredRowSetImpl
 * JD-Core Version:    0.6.2
 */