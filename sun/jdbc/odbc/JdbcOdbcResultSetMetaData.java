/*     */ package sun.jdbc.odbc;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.sql.Date;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ 
/*     */ public class JdbcOdbcResultSetMetaData extends JdbcOdbcObject
/*     */   implements ResultSetMetaData
/*     */ {
/*     */   protected JdbcOdbc OdbcApi;
/*     */   protected JdbcOdbcResultSetInterface resultSet;
/*     */   protected long hStmt;
/*     */ 
/*     */   public JdbcOdbcResultSetMetaData(JdbcOdbc paramJdbcOdbc, JdbcOdbcResultSetInterface paramJdbcOdbcResultSetInterface)
/*     */   {
/*  45 */     this.OdbcApi = paramJdbcOdbc;
/*  46 */     this.resultSet = paramJdbcOdbcResultSetInterface;
/*     */ 
/*  50 */     this.hStmt = paramJdbcOdbcResultSetInterface.getHSTMT();
/*     */   }
/*     */ 
/*     */   public int getColumnCount()
/*     */     throws SQLException
/*     */   {
/*  61 */     return this.resultSet.getColumnCount();
/*     */   }
/*     */ 
/*     */   public boolean isAutoIncrement(int paramInt)
/*     */     throws SQLException
/*     */   {
/*  73 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  74 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.isAutoIncrement (" + paramInt + ")");
/*     */     }
/*     */ 
/*  81 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */     boolean bool;
/*  85 */     if (this.resultSet.getPseudoCol(paramInt) != null) {
/*  86 */       bool = false;
/*     */     }
/*     */     else {
/*  89 */       bool = getColAttributeBoolean(paramInt, 11);
/*     */     }
/*     */ 
/*  92 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean isCaseSensitive(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 104 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 105 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.isCaseSensitive (" + paramInt + ")");
/*     */     }
/*     */ 
/* 112 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */     boolean bool;
/* 116 */     if (this.resultSet.getPseudoCol(paramInt) != null) {
/* 117 */       bool = false;
/*     */     }
/*     */     else {
/* 120 */       bool = getColAttributeBoolean(paramInt, 12);
/*     */     }
/*     */ 
/* 123 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean isSearchable(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 136 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 137 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.isSearchable (" + paramInt + ")");
/*     */     }
/*     */ 
/* 144 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */     boolean bool;
/* 148 */     if (this.resultSet.getPseudoCol(paramInt) != null) {
/* 149 */       bool = false;
/*     */     }
/*     */     else {
/* 152 */       int i = getColAttribute(paramInt, 13);
/*     */ 
/* 154 */       bool = i != 0;
/*     */     }
/* 156 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean isCurrency(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 169 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 170 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.isCurrency (" + paramInt + ")");
/*     */     }
/*     */ 
/* 177 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */     boolean bool;
/* 181 */     if (this.resultSet.getPseudoCol(paramInt) != null) {
/* 182 */       bool = false;
/*     */     }
/*     */     else {
/* 185 */       bool = getColAttributeBoolean(paramInt, 9);
/*     */     }
/*     */ 
/* 188 */     return bool;
/*     */   }
/*     */ 
/*     */   public int isNullable(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 200 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 201 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.isNullable (" + paramInt + ")");
/*     */     }
/*     */ 
/* 208 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */     int i;
/* 212 */     if (this.resultSet.getPseudoCol(paramInt) != null) {
/* 213 */       i = 0;
/*     */     }
/*     */     else {
/* 216 */       i = getColAttribute(paramInt, 7);
/*     */     }
/*     */ 
/* 219 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean isSigned(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 231 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 232 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.isSigned (" + paramInt + ")");
/*     */     }
/*     */ 
/* 239 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */     boolean bool;
/* 243 */     if (this.resultSet.getPseudoCol(paramInt) != null) {
/* 244 */       bool = false;
/*     */     }
/*     */     else {
/* 247 */       bool = !getColAttributeBoolean(paramInt, 8);
/*     */     }
/*     */ 
/* 250 */     return bool;
/*     */   }
/*     */ 
/*     */   public int getColumnDisplaySize(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 262 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 263 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.getColumnDisplaySize (" + paramInt + ")");
/*     */     }
/*     */ 
/* 268 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */ 
/* 270 */     JdbcOdbcPseudoCol localJdbcOdbcPseudoCol = this.resultSet.getPseudoCol(paramInt);
/*     */     int i;
/* 276 */     if (localJdbcOdbcPseudoCol != null) {
/* 277 */       i = localJdbcOdbcPseudoCol.getColumnDisplaySize();
/*     */     }
/*     */     else
/*     */     {
/* 282 */       i = getColAttribute(paramInt, 6);
/*     */     }
/*     */ 
/* 286 */     return i;
/*     */   }
/*     */ 
/*     */   public String getColumnLabel(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 298 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 299 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.getColumnLabel (" + paramInt + ")");
/*     */     }
/*     */ 
/* 304 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */ 
/* 306 */     JdbcOdbcPseudoCol localJdbcOdbcPseudoCol = this.resultSet.getPseudoCol(paramInt);
/*     */ 
/* 312 */     if (localJdbcOdbcPseudoCol != null) {
/* 313 */       str = localJdbcOdbcPseudoCol.getColumnLabel();
/*     */     }
/*     */     else
/*     */     {
/* 317 */       str = getColAttributeString(paramInt, 18);
/*     */     }
/*     */ 
/* 321 */     String str = this.resultSet.mapColumnName(str, paramInt);
/*     */ 
/* 323 */     return str;
/*     */   }
/*     */ 
/*     */   public String getColumnName(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 335 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 336 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.getColumnName (" + paramInt + ")");
/*     */     }
/*     */ 
/* 341 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */ 
/* 343 */     JdbcOdbcPseudoCol localJdbcOdbcPseudoCol = this.resultSet.getPseudoCol(paramInt);
/*     */ 
/* 349 */     if (localJdbcOdbcPseudoCol != null) {
/* 350 */       str = localJdbcOdbcPseudoCol.getColumnLabel();
/*     */     }
/*     */     else
/*     */     {
/* 354 */       str = getColAttributeString(paramInt, 1);
/*     */     }
/*     */ 
/* 358 */     String str = this.resultSet.mapColumnName(str, paramInt);
/*     */ 
/* 360 */     return str;
/*     */   }
/*     */ 
/*     */   public String getSchemaName(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 373 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 374 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.getSchemaName (" + paramInt + ")");
/*     */     }
/*     */ 
/* 379 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */ 
/* 384 */     if (this.resultSet.getPseudoCol(paramInt) != null) {
/* 385 */       paramInt = 1;
/*     */     }
/*     */ 
/* 388 */     return getColAttributeString(paramInt, 16);
/*     */   }
/*     */ 
/*     */   public int getPrecision(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 401 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 402 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.getPrecision (" + paramInt + ")");
/*     */     }
/*     */ 
/* 407 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */ 
/* 409 */     JdbcOdbcPseudoCol localJdbcOdbcPseudoCol = this.resultSet.getPseudoCol(paramInt);
/*     */     int i;
/* 415 */     if (localJdbcOdbcPseudoCol != null) {
/* 416 */       i = localJdbcOdbcPseudoCol.getColumnDisplaySize() - 1;
/*     */     }
/*     */     else
/*     */     {
/* 420 */       i = getColAttribute(paramInt, 4);
/*     */     }
/*     */ 
/* 423 */     return i;
/*     */   }
/*     */ 
/*     */   public int getScale(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 435 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 436 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.getScale (" + paramInt + ")");
/*     */     }
/*     */ 
/* 439 */     return this.resultSet.getScale(paramInt);
/*     */   }
/*     */ 
/*     */   public String getTableName(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 451 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 452 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.getTableName (" + paramInt + ")");
/*     */     }
/*     */ 
/* 457 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */ 
/* 462 */     if (this.resultSet.getPseudoCol(paramInt) != null) {
/* 463 */       paramInt = 1;
/*     */     }
/*     */ 
/* 466 */     return getColAttributeString(paramInt, 15);
/*     */   }
/*     */ 
/*     */   public String getCatalogName(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 480 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 481 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.getCatalogName (" + paramInt + ")");
/*     */     }
/*     */ 
/* 486 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */ 
/* 491 */     if (this.resultSet.getPseudoCol(paramInt) != null) {
/* 492 */       paramInt = 1;
/*     */     }
/*     */ 
/* 495 */     return getColAttributeString(paramInt, 17);
/*     */   }
/*     */ 
/*     */   public int getColumnType(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 508 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 509 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.getColumnType (" + paramInt + ")");
/*     */     }
/*     */ 
/* 512 */     JdbcOdbcPseudoCol localJdbcOdbcPseudoCol = this.resultSet.getPseudoCol(paramInt);
/*     */     int i;
/* 518 */     if (localJdbcOdbcPseudoCol != null) {
/* 519 */       i = localJdbcOdbcPseudoCol.getColumnType() - 1;
/*     */     }
/*     */     else
/*     */     {
/* 526 */       i = this.resultSet.getColumnType(paramInt);
/*     */     }
/* 528 */     return i;
/*     */   }
/*     */ 
/*     */   public String getColumnTypeName(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 540 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 541 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.getColumnTypeName (" + paramInt + ")");
/*     */     }
/*     */ 
/* 548 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */     String str;
/* 552 */     if (this.resultSet.getPseudoCol(paramInt) != null) {
/* 553 */       str = "";
/*     */     }
/*     */     else {
/* 556 */       str = getColAttributeString(paramInt, 14);
/*     */     }
/*     */ 
/* 559 */     return str;
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 571 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 572 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.isReadOnly (" + paramInt + ")");
/*     */     }
/*     */ 
/* 579 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */     boolean bool;
/* 583 */     if (this.resultSet.getPseudoCol(paramInt) != null) {
/* 584 */       bool = true;
/*     */     }
/*     */     else {
/* 587 */       int i = getColAttribute(paramInt, 10);
/*     */ 
/* 589 */       bool = i == 0;
/*     */     }
/* 591 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean isWritable(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 603 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 604 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.isWritable (" + paramInt + ")");
/*     */     }
/*     */ 
/* 611 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */     boolean bool;
/* 615 */     if (this.resultSet.getPseudoCol(paramInt) != null) {
/* 616 */       bool = false;
/*     */     }
/*     */     else {
/* 619 */       int i = getColAttribute(paramInt, 10);
/*     */ 
/* 621 */       bool = i == 2;
/*     */     }
/* 623 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean isDefinitelyWritable(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 635 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 636 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.isDefinitelyWritable (" + paramInt + ")");
/*     */     }
/*     */ 
/* 643 */     paramInt = this.resultSet.mapColumn(paramInt);
/*     */     boolean bool;
/* 647 */     if (this.resultSet.getPseudoCol(paramInt) != null) {
/* 648 */       bool = false;
/*     */     }
/*     */     else {
/* 651 */       int i = getColAttribute(paramInt, 10);
/*     */ 
/* 653 */       bool = i == 1;
/*     */     }
/* 655 */     return bool;
/*     */   }
/*     */ 
/*     */   public String getColumnClassName(int paramInt)
/*     */     throws SQLException
/*     */   {
/* 672 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 673 */       this.OdbcApi.getTracer().trace("*ResultSetMetaData.getColumnClassName (" + paramInt + ")");
/*     */     }
/*     */ 
/* 677 */     String str = new String().getClass().getName();
/*     */ 
/* 679 */     int i = getColumnType(paramInt);
/*     */ 
/* 683 */     switch (i)
/*     */     {
/*     */     case 2:
/*     */     case 3:
/* 688 */       str = new BigDecimal(0).getClass().getName();
/* 689 */       break;
/*     */     case -7:
/* 692 */       str = new Boolean(false).getClass().getName();
/* 693 */       break;
/*     */     case -6:
/* 696 */       str = new Byte("0").getClass().getName();
/* 697 */       break;
/*     */     case 5:
/* 700 */       str = new Short("0").getClass().getName();
/* 701 */       break;
/*     */     case 4:
/* 704 */       str = new Integer(0).getClass().getName();
/* 705 */       break;
/*     */     case -5:
/* 708 */       str = new Long(0L).getClass().getName();
/* 709 */       break;
/*     */     case 7:
/* 712 */       str = new Float(0.0F).getClass().getName();
/* 713 */       break;
/*     */     case 6:
/*     */     case 8:
/* 717 */       str = new Double(0.0D).getClass().getName();
/* 718 */       break;
/*     */     case -4:
/*     */     case -3:
/*     */     case -2:
/* 727 */       byte[] arrayOfByte = new byte[0];
/* 728 */       str = arrayOfByte.getClass().getName();
/* 729 */       break;
/*     */     case 91:
/* 732 */       str = new Date(123456L).getClass().getName();
/* 733 */       break;
/*     */     case 92:
/* 736 */       str = new Time(123456L).getClass().getName();
/* 737 */       break;
/*     */     case 93:
/* 740 */       str = new Timestamp(123456L).getClass().getName();
/*     */     }
/*     */ 
/* 746 */     return str;
/*     */   }
/*     */ 
/*     */   protected int getColAttribute(int paramInt1, int paramInt2)
/*     */     throws SQLException
/*     */   {
/* 761 */     return this.resultSet.getColAttribute(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected boolean getColAttributeBoolean(int paramInt1, int paramInt2)
/*     */     throws SQLException
/*     */   {
/* 769 */     int i = getColAttribute(paramInt1, paramInt2);
/* 770 */     boolean bool = false;
/*     */ 
/* 774 */     if (i == 1) {
/* 775 */       bool = true;
/*     */     }
/* 777 */     return bool;
/*     */   }
/*     */ 
/*     */   protected String getColAttributeString(int paramInt1, int paramInt2)
/*     */     throws SQLException
/*     */   {
/* 785 */     String str = "";
/*     */ 
/* 789 */     this.resultSet.clearWarnings();
/*     */     try
/*     */     {
/* 792 */       str = this.OdbcApi.SQLColAttributesString(this.hStmt, paramInt1, paramInt2);
/*     */     }
/*     */     catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*     */     {
/* 801 */       str = (String)localJdbcOdbcSQLWarning.value;
/* 802 */       this.resultSet.setWarning(JdbcOdbc.convertWarning(localJdbcOdbcSQLWarning));
/*     */     }
/* 804 */     return str.trim();
/*     */   }
/*     */ 
/*     */   public <T> T unwrap(Class<T> paramClass)
/*     */     throws SQLException
/*     */   {
/* 822 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isWrapperFor(Class<?> paramClass)
/*     */     throws SQLException
/*     */   {
/* 841 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcResultSetMetaData
 * JD-Core Version:    0.6.2
 */