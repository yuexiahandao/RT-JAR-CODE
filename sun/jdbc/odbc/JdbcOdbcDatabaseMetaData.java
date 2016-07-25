/*      */ package sun.jdbc.odbc;
/*      */ 
/*      */ import java.sql.Connection;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.RowIdLifetime;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLFeatureNotSupportedException;
/*      */ import java.sql.SQLWarning;
/*      */ 
/*      */ public class JdbcOdbcDatabaseMetaData extends JdbcOdbcObject
/*      */   implements DatabaseMetaData
/*      */ {
/*      */   protected JdbcOdbc OdbcApi;
/*      */   protected JdbcOdbcConnectionInterface Con;
/*      */   protected long hDbc;
/*      */ 
/*      */   public JdbcOdbcDatabaseMetaData(JdbcOdbc paramJdbcOdbc, JdbcOdbcConnectionInterface paramJdbcOdbcConnectionInterface)
/*      */   {
/*   46 */     this.OdbcApi = paramJdbcOdbc;
/*   47 */     this.Con = paramJdbcOdbcConnectionInterface;
/*      */ 
/*   51 */     this.hDbc = this.Con.getHDBC();
/*      */   }
/*      */ 
/*      */   public boolean allProceduresAreCallable()
/*      */     throws SQLException
/*      */   {
/*   64 */     if (this.OdbcApi.getTracer().isTracing()) {
/*   65 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.allProceduresAreCallable");
/*      */     }
/*   67 */     return getInfoBooleanString((short)20);
/*      */   }
/*      */ 
/*      */   public boolean allTablesAreSelectable()
/*      */     throws SQLException
/*      */   {
/*   80 */     if (this.OdbcApi.getTracer().isTracing()) {
/*   81 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.allTablesAreSelectable");
/*      */     }
/*   83 */     return getInfoBooleanString((short)19);
/*      */   }
/*      */ 
/*      */   public String getURL()
/*      */     throws SQLException
/*      */   {
/*   96 */     if (this.OdbcApi.getTracer().isTracing()) {
/*   97 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getURL");
/*      */     }
/*   99 */     return this.Con.getURL();
/*      */   }
/*      */ 
/*      */   public String getUserName()
/*      */     throws SQLException
/*      */   {
/*  110 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  111 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getUserName");
/*      */     }
/*  113 */     return getInfoString((short)47);
/*      */   }
/*      */ 
/*      */   public boolean isReadOnly()
/*      */     throws SQLException
/*      */   {
/*  124 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  125 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.isReadOnly");
/*      */     }
/*  127 */     return getInfoBooleanString((short)25);
/*      */   }
/*      */ 
/*      */   public boolean nullsAreSortedHigh()
/*      */     throws SQLException
/*      */   {
/*  139 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  140 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.nullsAreSortedHigh");
/*      */     }
/*  142 */     int i = getInfoShort((short)85);
/*      */ 
/*  144 */     return i == 0;
/*      */   }
/*      */ 
/*      */   public boolean nullsAreSortedLow()
/*      */     throws SQLException
/*      */   {
/*  155 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  156 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.nullsAreSortedLow");
/*      */     }
/*  158 */     int i = getInfo((short)85);
/*      */ 
/*  160 */     return i == 1;
/*      */   }
/*      */ 
/*      */   public boolean nullsAreSortedAtStart()
/*      */     throws SQLException
/*      */   {
/*  172 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  173 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.nullsAreSortedAtStart");
/*      */     }
/*  175 */     int i = getInfo((short)85);
/*      */ 
/*  177 */     return i == 2;
/*      */   }
/*      */ 
/*      */   public boolean nullsAreSortedAtEnd()
/*      */     throws SQLException
/*      */   {
/*  189 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  190 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.nullsAreSortedAtEnd");
/*      */     }
/*  192 */     int i = getInfo((short)85);
/*      */ 
/*  194 */     return i == 4;
/*      */   }
/*      */ 
/*      */   public String getDatabaseProductName()
/*      */     throws SQLException
/*      */   {
/*  205 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  206 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getDatabaseProductName");
/*      */     }
/*  208 */     return getInfoString((short)17);
/*      */   }
/*      */ 
/*      */   public String getDatabaseProductVersion()
/*      */     throws SQLException
/*      */   {
/*  219 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  220 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getDatabaseProductVersion");
/*      */     }
/*  222 */     return getInfoString((short)18);
/*      */   }
/*      */ 
/*      */   public String getDriverName()
/*      */     throws SQLException
/*      */   {
/*  234 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  235 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getDriverName");
/*      */     }
/*  237 */     return "JDBC-ODBC Bridge (" + getInfoString((short)6) + ")";
/*      */   }
/*      */ 
/*      */   public String getDriverVersion()
/*      */     throws SQLException
/*      */   {
/*  251 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  252 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getDriverVersion");
/*      */     }
/*  254 */     int i = getDriverMinorVersion();
/*  255 */     String str = "";
/*      */ 
/*  260 */     if (i < 1000) str = str + "0";
/*  261 */     if (i < 100) str = str + "0";
/*  262 */     if (i < 10) str = str + "0";
/*  263 */     str = str + "" + i;
/*      */ 
/*  265 */     return "" + getDriverMajorVersion() + "." + str + " (" + getInfoString((short)7) + ")";
/*      */   }
/*      */ 
/*      */   public int getDriverMajorVersion()
/*      */   {
/*  277 */     return 2;
/*      */   }
/*      */ 
/*      */   public int getDriverMinorVersion()
/*      */   {
/*  287 */     return 1;
/*      */   }
/*      */ 
/*      */   public boolean usesLocalFiles()
/*      */     throws SQLException
/*      */   {
/*  298 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  299 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.usesLocalFiles");
/*      */     }
/*  301 */     int i = getInfoShort((short)84);
/*      */ 
/*  303 */     return i == 2;
/*      */   }
/*      */ 
/*      */   public boolean usesLocalFilePerTable()
/*      */     throws SQLException
/*      */   {
/*  310 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  311 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.usesLocalFilePerTable");
/*      */     }
/*  313 */     int i = getInfoShort((short)84);
/*      */ 
/*  315 */     return i == 1;
/*      */   }
/*      */ 
/*      */   public boolean supportsMixedCaseIdentifiers()
/*      */     throws SQLException
/*      */   {
/*  326 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  327 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsMixedCaseIdentifiers");
/*      */     }
/*  329 */     int i = getInfoShort((short)28);
/*      */ 
/*  341 */     return i == 3;
/*      */   }
/*      */ 
/*      */   public boolean storesUpperCaseIdentifiers()
/*      */     throws SQLException
/*      */   {
/*  353 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  354 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.storesUpperCaseIdentifiers");
/*      */     }
/*  356 */     int i = getInfoShort((short)28);
/*      */ 
/*  358 */     return i == 1;
/*      */   }
/*      */ 
/*      */   public boolean storesLowerCaseIdentifiers()
/*      */     throws SQLException
/*      */   {
/*  369 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  370 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.storesLowerCaseIdentifiers");
/*      */     }
/*  372 */     int i = getInfoShort((short)28);
/*      */ 
/*  374 */     return i == 2;
/*      */   }
/*      */ 
/*      */   public boolean storesMixedCaseIdentifiers()
/*      */     throws SQLException
/*      */   {
/*  385 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  386 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.storesMixedCaseIdentifiers");
/*      */     }
/*  388 */     int i = getInfoShort((short)28);
/*      */ 
/*  390 */     return i == 4;
/*      */   }
/*      */ 
/*      */   public boolean supportsMixedCaseQuotedIdentifiers()
/*      */     throws SQLException
/*      */   {
/*  401 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  402 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsMixedCaseQuotedIdentifiers");
/*      */     }
/*  404 */     int i = getInfoShort((short)93);
/*      */ 
/*  416 */     return i == 3;
/*      */   }
/*      */ 
/*      */   public boolean storesUpperCaseQuotedIdentifiers()
/*      */     throws SQLException
/*      */   {
/*  428 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  429 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.storesUpperCaseQuotedIdentifiers");
/*      */     }
/*  431 */     int i = getInfoShort((short)93);
/*      */ 
/*  433 */     return i == 1;
/*      */   }
/*      */ 
/*      */   public boolean storesLowerCaseQuotedIdentifiers()
/*      */     throws SQLException
/*      */   {
/*  444 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  445 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.storesLowerCaseQuotedIdentifiers");
/*      */     }
/*  447 */     int i = getInfoShort((short)93);
/*      */ 
/*  449 */     return i == 2;
/*      */   }
/*      */ 
/*      */   public boolean storesMixedCaseQuotedIdentifiers()
/*      */     throws SQLException
/*      */   {
/*  460 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  461 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.storesMixedCaseQuotedIdentifiers");
/*      */     }
/*  463 */     int i = getInfoShort((short)93);
/*      */ 
/*  465 */     return i == 4;
/*      */   }
/*      */ 
/*      */   public String getIdentifierQuoteString()
/*      */     throws SQLException
/*      */   {
/*  476 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  477 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getIdentifierQuoteString");
/*      */     }
/*  479 */     return getInfoString((short)29);
/*      */   }
/*      */ 
/*      */   public String getSQLKeywords()
/*      */     throws SQLException
/*      */   {
/*  490 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  491 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getSQLKeywords");
/*      */     }
/*  493 */     return getInfoString((short)89, 16383);
/*      */   }
/*      */ 
/*      */   public String getNumericFunctions()
/*      */     throws SQLException
/*      */   {
/*  505 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  506 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getNumericFunctions");
/*      */     }
/*      */ 
/*  511 */     String str = "";
/*      */ 
/*  513 */     int i = getInfo((short)49);
/*      */ 
/*  518 */     if ((i & 0x1) != 0) str = str + "ABS,";
/*  519 */     if ((i & 0x2) != 0) str = str + "ACOS,";
/*  520 */     if ((i & 0x4) != 0) str = str + "ASIN,";
/*  521 */     if ((i & 0x8) != 0) str = str + "ATAN,";
/*  522 */     if ((i & 0x10) != 0) str = str + "ATAN2,";
/*  523 */     if ((i & 0x20) != 0) str = str + "CEILING,";
/*  524 */     if ((i & 0x40) != 0) str = str + "COS,";
/*  525 */     if ((i & 0x80) != 0) str = str + "COT,";
/*  526 */     if ((i & 0x40000) != 0) str = str + "DEGREES,";
/*  527 */     if ((i & 0x100) != 0) str = str + "EXP,";
/*  528 */     if ((i & 0x200) != 0) str = str + "FLOOR,";
/*  529 */     if ((i & 0x400) != 0) str = str + "LOG,";
/*  530 */     if ((i & 0x80000) != 0) str = str + "LOG10,";
/*  531 */     if ((i & 0x800) != 0) str = str + "MOD,";
/*  532 */     if ((i & 0x10000) != 0) str = str + "PI,";
/*  533 */     if ((i & 0x100000) != 0) str = str + "POWER,";
/*  534 */     if ((i & 0x200000) != 0) str = str + "RADIANS,";
/*  535 */     if ((i & 0x20000) != 0) str = str + "RAND,";
/*  536 */     if ((i & 0x400000) != 0) str = str + "ROUND,";
/*  537 */     if ((i & 0x1000) != 0) str = str + "SIGN,";
/*  538 */     if ((i & 0x2000) != 0) str = str + "SIN,";
/*  539 */     if ((i & 0x4000) != 0) str = str + "SQRT,";
/*  540 */     if ((i & 0x8000) != 0) str = str + "TAN,";
/*  541 */     if ((i & 0x800000) != 0) str = str + "TRUNCATE,";
/*      */ 
/*  545 */     if (str.length() > 0) {
/*  546 */       str = str.substring(0, str.length() - 1);
/*      */     }
/*  548 */     return str;
/*      */   }
/*      */ 
/*      */   public String getStringFunctions()
/*      */     throws SQLException
/*      */   {
/*  559 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  560 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getStringFunctions");
/*      */     }
/*      */ 
/*  565 */     String str = "";
/*      */ 
/*  567 */     int i = getInfo((short)50);
/*      */ 
/*  572 */     if ((i & 0x2000) != 0) str = str + "ASCII,";
/*  573 */     if ((i & 0x4000) != 0) str = str + "CHAR,";
/*  574 */     if ((i & 0x1) != 0) str = str + "CONCAT,";
/*  575 */     if ((i & 0x8000) != 0) str = str + "DIFFERENCE,";
/*  576 */     if ((i & 0x2) != 0) str = str + "INSERT,";
/*  577 */     if ((i & 0x40) != 0) str = str + "LCASE,";
/*  578 */     if ((i & 0x4) != 0) str = str + "LEFT,";
/*  579 */     if ((i & 0x10) != 0) str = str + "LENGTH,";
/*  580 */     if ((i & 0x20) != 0) str = str + "LOCATE,";
/*  581 */     if ((i & 0x10000) != 0) str = str + "LOCATE_2,";
/*  582 */     if ((i & 0x8) != 0) str = str + "LTRIM,";
/*  583 */     if ((i & 0x80) != 0) str = str + "REPEAT,";
/*  584 */     if ((i & 0x100) != 0) str = str + "REPLACE,";
/*  585 */     if ((i & 0x200) != 0) str = str + "RIGHT,";
/*  586 */     if ((i & 0x400) != 0) str = str + "RTRIM,";
/*  587 */     if ((i & 0x20000) != 0) str = str + "SOUNDEX,";
/*  588 */     if ((i & 0x40000) != 0) str = str + "SPACE,";
/*  589 */     if ((i & 0x800) != 0) str = str + "SUBSTRING,";
/*  590 */     if ((i & 0x1000) != 0) str = str + "UCASE,";
/*      */ 
/*  594 */     if (str.length() > 0) {
/*  595 */       str = str.substring(0, str.length() - 1);
/*      */     }
/*  597 */     return str;
/*      */   }
/*      */ 
/*      */   public String getSystemFunctions()
/*      */     throws SQLException
/*      */   {
/*  608 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  609 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getSystemFunctions");
/*      */     }
/*      */ 
/*  614 */     String str = "";
/*      */ 
/*  616 */     int i = getInfo((short)51);
/*      */ 
/*  621 */     if ((i & 0x2) != 0) str = str + "DBNAME,";
/*  622 */     if ((i & 0x4) != 0) str = str + "IFNULL,";
/*  623 */     if ((i & 0x1) != 0) str = str + "USERNAME,";
/*      */ 
/*  627 */     if (str.length() > 0) {
/*  628 */       str = str.substring(0, str.length() - 1);
/*      */     }
/*  630 */     return str;
/*      */   }
/*      */ 
/*      */   public String getTimeDateFunctions()
/*      */     throws SQLException
/*      */   {
/*  641 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  642 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getTimeDateFunctions");
/*      */     }
/*      */ 
/*  647 */     String str = "";
/*      */ 
/*  649 */     int i = getInfo((short)52);
/*      */ 
/*  654 */     if ((i & 0x2) != 0) str = str + "CURDATE,";
/*  655 */     if ((i & 0x200) != 0) str = str + "CURTIME,";
/*  656 */     if ((i & 0x8000) != 0) str = str + "DAYNAME,";
/*  657 */     if ((i & 0x4) != 0) str = str + "DAYOFMONTH,";
/*  658 */     if ((i & 0x8) != 0) str = str + "DAYOFWEEK,";
/*  659 */     if ((i & 0x10) != 0) str = str + "DAYOFYEAR,";
/*  660 */     if ((i & 0x400) != 0) str = str + "HOUR,";
/*  661 */     if ((i & 0x800) != 0) str = str + "MINUTE,";
/*  662 */     if ((i & 0x20) != 0) str = str + "MONTH,";
/*  663 */     if ((i & 0x10000) != 0) str = str + "MONTHNAME,";
/*  664 */     if ((i & 0x1) != 0) str = str + "NOW,";
/*  665 */     if ((i & 0x40) != 0) str = str + "QUARTER,";
/*  666 */     if ((i & 0x1000) != 0) str = str + "SECOND,";
/*  667 */     if ((i & 0x2000) != 0)
/*  668 */       str = str + "TIMESTAMPADD,";
/*  669 */     if ((i & 0x4000) != 0)
/*  670 */       str = str + "TIMESTAMPDIFF,";
/*  671 */     if ((i & 0x80) != 0) str = str + "WEEK,";
/*  672 */     if ((i & 0x100) != 0) str = str + "YEAR,";
/*      */ 
/*  677 */     if (str.length() > 0) {
/*  678 */       str = str.substring(0, str.length() - 1);
/*      */     }
/*  680 */     return str;
/*      */   }
/*      */ 
/*      */   public String getSearchStringEscape()
/*      */     throws SQLException
/*      */   {
/*  692 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  693 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getSearchStringEscape");
/*      */     }
/*  695 */     return getInfoString((short)14);
/*      */   }
/*      */ 
/*      */   public String getExtraNameCharacters()
/*      */     throws SQLException
/*      */   {
/*  707 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  708 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getExtraNameCharacters");
/*      */     }
/*  710 */     return getInfoString((short)94);
/*      */   }
/*      */ 
/*      */   public boolean supportsAlterTableWithAddColumn()
/*      */     throws SQLException
/*      */   {
/*  721 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  722 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsAlterTableWithAddColumn");
/*      */     }
/*  724 */     int i = getInfo((short)86);
/*      */ 
/*  726 */     return (i & 0x1) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsAlterTableWithDropColumn()
/*      */     throws SQLException
/*      */   {
/*  737 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  738 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsAlterTableWithDropColumn");
/*      */     }
/*  740 */     int i = getInfo((short)86);
/*      */ 
/*  742 */     return (i & 0x2) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsColumnAliasing()
/*      */     throws SQLException
/*      */   {
/*  753 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  754 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsColumnAliasing");
/*      */     }
/*  756 */     return getInfoBooleanString((short)87);
/*      */   }
/*      */ 
/*      */   public boolean nullPlusNonNullIsNull()
/*      */     throws SQLException
/*      */   {
/*  767 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  768 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.nullPlusNullIsNull");
/*      */     }
/*  770 */     int i = getInfoShort((short)22);
/*      */ 
/*  772 */     return i == 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsConvert()
/*      */     throws SQLException
/*      */   {
/*  783 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  784 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsConvert");
/*      */     }
/*  786 */     int i = getInfo((short)48);
/*      */ 
/*  788 */     return i == 1;
/*      */   }
/*      */ 
/*      */   public boolean supportsConvert(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  801 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  802 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsConvert (" + paramInt1 + "," + paramInt2 + ")");
/*      */     }
/*      */ 
/*  806 */     short s = 0;
/*  807 */     int i = 0;
/*  808 */     int j = 0;
/*      */ 
/*  812 */     switch (paramInt1) {
/*      */     case -7:
/*  814 */       s = 55;
/*  815 */       break;
/*      */     case -6:
/*  817 */       s = 68;
/*  818 */       break;
/*      */     case 5:
/*  820 */       s = 65;
/*  821 */       break;
/*      */     case 4:
/*  823 */       s = 61;
/*  824 */       break;
/*      */     case -5:
/*  826 */       s = 53;
/*  827 */       break;
/*      */     case 6:
/*  829 */       s = 60;
/*  830 */       break;
/*      */     case 7:
/*  832 */       s = 64;
/*  833 */       break;
/*      */     case 8:
/*  835 */       s = 59;
/*  836 */       break;
/*      */     case 2:
/*  838 */       s = 63;
/*  839 */       break;
/*      */     case 3:
/*  841 */       s = 58;
/*  842 */       break;
/*      */     case 1:
/*  844 */       s = 56;
/*  845 */       break;
/*      */     case 12:
/*  847 */       s = 70;
/*  848 */       break;
/*      */     case -1:
/*  850 */       s = 62;
/*  851 */       break;
/*      */     case 91:
/*  853 */       s = 57;
/*  854 */       break;
/*      */     case 92:
/*  856 */       s = 66;
/*  857 */       break;
/*      */     case 93:
/*  859 */       s = 67;
/*  860 */       break;
/*      */     case -2:
/*  862 */       s = 54;
/*  863 */       break;
/*      */     case -3:
/*  865 */       s = 69;
/*  866 */       break;
/*      */     case -4:
/*  868 */       s = 71;
/*      */     }
/*      */ 
/*  875 */     int k = getInfo(s);
/*      */ 
/*  877 */     switch (paramInt2) {
/*      */     case -7:
/*  879 */       j = 4096;
/*  880 */       break;
/*      */     case -6:
/*  882 */       j = 8192;
/*  883 */       break;
/*      */     case 5:
/*  885 */       j = 16;
/*  886 */       break;
/*      */     case 4:
/*  888 */       j = 8;
/*  889 */       break;
/*      */     case -5:
/*  891 */       j = 16384;
/*  892 */       break;
/*      */     case 6:
/*  894 */       j = 32;
/*  895 */       break;
/*      */     case 7:
/*  897 */       j = 64;
/*  898 */       break;
/*      */     case 8:
/*  900 */       j = 128;
/*  901 */       break;
/*      */     case 2:
/*  903 */       j = 2;
/*  904 */       break;
/*      */     case 3:
/*  906 */       j = 4;
/*  907 */       break;
/*      */     case 1:
/*  909 */       j = 1;
/*  910 */       break;
/*      */     case 12:
/*  912 */       j = 256;
/*  913 */       break;
/*      */     case -1:
/*  915 */       j = 512;
/*  916 */       break;
/*      */     case 91:
/*  918 */       j = 32768;
/*  919 */       break;
/*      */     case 92:
/*  921 */       j = 65536;
/*  922 */       break;
/*      */     case 93:
/*  924 */       j = 131072;
/*  925 */       break;
/*      */     case -2:
/*  927 */       j = 1024;
/*  928 */       break;
/*      */     case -3:
/*  930 */       j = 2048;
/*  931 */       break;
/*      */     case -4:
/*  933 */       j = 262144;
/*      */     }
/*      */ 
/*  937 */     return (k & j) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsTableCorrelationNames()
/*      */     throws SQLException
/*      */   {
/*  948 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  949 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsTableCorrelationNames");
/*      */     }
/*  951 */     int i = getInfoShort((short)74);
/*      */ 
/*  953 */     return (i == 1) || (i == 2);
/*      */   }
/*      */ 
/*      */   public boolean supportsDifferentTableCorrelationNames()
/*      */     throws SQLException
/*      */   {
/*  966 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  967 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsDifferentTableCorrelationNames");
/*      */     }
/*  969 */     int i = getInfoShort((short)74);
/*      */ 
/*  971 */     return i == 1;
/*      */   }
/*      */ 
/*      */   public boolean supportsExpressionsInOrderBy()
/*      */     throws SQLException
/*      */   {
/*  982 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  983 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsExpressionsInOrderBy");
/*      */     }
/*  985 */     return getInfoBooleanString((short)27);
/*      */   }
/*      */ 
/*      */   public boolean supportsOrderByUnrelated()
/*      */     throws SQLException
/*      */   {
/*  997 */     if (this.OdbcApi.getTracer().isTracing()) {
/*  998 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsOrderByUnrelated");
/*      */     }
/* 1000 */     return getInfoBooleanString((short)90);
/*      */   }
/*      */ 
/*      */   public boolean supportsGroupBy()
/*      */     throws SQLException
/*      */   {
/* 1012 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1013 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsGroupBy");
/*      */     }
/* 1015 */     int i = getInfoShort((short)88);
/*      */ 
/* 1017 */     return i != 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsGroupByUnrelated()
/*      */     throws SQLException
/*      */   {
/* 1028 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1029 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsGroupByUnrelated");
/*      */     }
/* 1031 */     int i = getInfoShort((short)88);
/*      */ 
/* 1033 */     return i == 3;
/*      */   }
/*      */ 
/*      */   public boolean supportsGroupByBeyondSelect()
/*      */     throws SQLException
/*      */   {
/* 1044 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1045 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsGroupByBeyondSelect");
/*      */     }
/* 1047 */     int i = getInfoShort((short)88);
/*      */ 
/* 1049 */     return i == 2;
/*      */   }
/*      */ 
/*      */   public boolean supportsLikeEscapeClause()
/*      */     throws SQLException
/*      */   {
/* 1060 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1061 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsLikeEscapeClause");
/*      */     }
/* 1063 */     return getInfoBooleanString((short)113);
/*      */   }
/*      */ 
/*      */   public boolean supportsMultipleResultSets()
/*      */     throws SQLException
/*      */   {
/* 1075 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1076 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsMultipleResultSets");
/*      */     }
/* 1078 */     return getInfoBooleanString((short)36);
/*      */   }
/*      */ 
/*      */   public boolean supportsMultipleTransactions()
/*      */     throws SQLException
/*      */   {
/* 1091 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1092 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsMultipleTransactions");
/*      */     }
/* 1094 */     return getInfoBooleanString((short)37);
/*      */   }
/*      */ 
/*      */   public boolean supportsNonNullableColumns()
/*      */     throws SQLException
/*      */   {
/* 1106 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1107 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsNonNullableColumns");
/*      */     }
/* 1109 */     int i = getInfoShort((short)75);
/*      */ 
/* 1111 */     return i == 1;
/*      */   }
/*      */ 
/*      */   public boolean supportsMinimumSQLGrammar()
/*      */     throws SQLException
/*      */   {
/* 1122 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1123 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsMinimumSQLGrammar");
/*      */     }
/* 1125 */     int i = getInfoShort((short)15);
/*      */ 
/* 1130 */     return (i == 0) || (i == 1) || (i == 2);
/*      */   }
/*      */ 
/*      */   public boolean supportsCoreSQLGrammar()
/*      */     throws SQLException
/*      */   {
/* 1142 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1143 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsCoreSQLGrammar");
/*      */     }
/* 1145 */     int i = getInfoShort((short)15);
/*      */ 
/* 1149 */     return (i == 1) || (i == 2);
/*      */   }
/*      */ 
/*      */   public boolean supportsANSI92EntryLevelSQL()
/*      */     throws SQLException
/*      */   {
/* 1161 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean supportsANSI92IntermediateSQL()
/*      */     throws SQLException
/*      */   {
/* 1172 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean supportsANSI92FullSQL()
/*      */     throws SQLException
/*      */   {
/* 1183 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean supportsExtendedSQLGrammar()
/*      */     throws SQLException
/*      */   {
/* 1193 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1194 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsExtendedSQLGrammar");
/*      */     }
/* 1196 */     int i = getInfoShort((short)15);
/*      */ 
/* 1198 */     return i == 2;
/*      */   }
/*      */ 
/*      */   public boolean supportsIntegrityEnhancementFacility()
/*      */     throws SQLException
/*      */   {
/* 1209 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1210 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsIntegrityEnhancementFacility");
/*      */     }
/* 1212 */     return getInfoBooleanString((short)73);
/*      */   }
/*      */ 
/*      */   public boolean supportsOuterJoins()
/*      */     throws SQLException
/*      */   {
/* 1224 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1225 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsOuterJoins");
/*      */     }
/*      */ 
/* 1229 */     String str = getInfoString((short)38);
/*      */ 
/* 1231 */     return !str.equalsIgnoreCase("N");
/*      */   }
/*      */ 
/*      */   public boolean supportsFullOuterJoins()
/*      */     throws SQLException
/*      */   {
/* 1242 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1243 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsFullOuterJoins");
/*      */     }
/*      */ 
/* 1247 */     String str = getInfoString((short)38);
/*      */ 
/* 1249 */     return str.equalsIgnoreCase("F");
/*      */   }
/*      */ 
/*      */   public boolean supportsLimitedOuterJoins()
/*      */     throws SQLException
/*      */   {
/* 1260 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1261 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsLimitedOuterJoins");
/*      */     }
/*      */ 
/* 1265 */     String str = getInfoString((short)38);
/*      */ 
/* 1267 */     return str.equalsIgnoreCase("P");
/*      */   }
/*      */ 
/*      */   public String getSchemaTerm()
/*      */     throws SQLException
/*      */   {
/* 1278 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1279 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getSchemaTerm");
/*      */     }
/* 1281 */     return getInfoString((short)39);
/*      */   }
/*      */ 
/*      */   public String getProcedureTerm()
/*      */     throws SQLException
/*      */   {
/* 1292 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1293 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getProcedureTerm");
/*      */     }
/* 1295 */     return getInfoString((short)40);
/*      */   }
/*      */ 
/*      */   public String getCatalogTerm()
/*      */     throws SQLException
/*      */   {
/* 1306 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1307 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getCatalogTerm");
/*      */     }
/* 1309 */     return getInfoString((short)42);
/*      */   }
/*      */ 
/*      */   public boolean isCatalogAtStart()
/*      */     throws SQLException
/*      */   {
/* 1321 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1322 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.isCatalogAtStart");
/*      */     }
/* 1324 */     int i = getInfoShort((short)114);
/*      */ 
/* 1326 */     return i == 1;
/*      */   }
/*      */ 
/*      */   public String getCatalogSeparator()
/*      */     throws SQLException
/*      */   {
/* 1337 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1338 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getCatalogSeparator");
/*      */     }
/* 1340 */     return getInfoString((short)41);
/*      */   }
/*      */ 
/*      */   public boolean supportsSchemasInDataManipulation()
/*      */     throws SQLException
/*      */   {
/* 1350 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1351 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsSchemasInDataManipulation");
/*      */     }
/* 1353 */     int i = getInfo((short)91);
/*      */ 
/* 1355 */     return (i & 0x1) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsSchemasInProcedureCalls()
/*      */     throws SQLException
/*      */   {
/* 1365 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1366 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsSchemasInProcedureCalls");
/*      */     }
/* 1368 */     int i = getInfo((short)91);
/*      */ 
/* 1370 */     return (i & 0x2) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsSchemasInTableDefinitions()
/*      */     throws SQLException
/*      */   {
/* 1380 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1381 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsSchemasInTableDefinitions");
/*      */     }
/* 1383 */     int i = getInfo((short)91);
/*      */ 
/* 1385 */     return (i & 0x4) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsSchemasInIndexDefinitions()
/*      */     throws SQLException
/*      */   {
/* 1395 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1396 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsSchemasInIndexDefintions");
/*      */     }
/* 1398 */     int i = getInfo((short)91);
/*      */ 
/* 1400 */     return (i & 0x8) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsSchemasInPrivilegeDefinitions()
/*      */     throws SQLException
/*      */   {
/* 1410 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1411 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsSchemasInPrivilegeDefintions");
/*      */     }
/* 1413 */     int i = getInfo((short)91);
/*      */ 
/* 1415 */     return (i & 0x10) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsCatalogsInDataManipulation()
/*      */     throws SQLException
/*      */   {
/* 1425 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1426 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsCatalogsInDataManipulation");
/*      */     }
/* 1428 */     int i = getInfo((short)92);
/*      */ 
/* 1430 */     return (i & 0x1) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsCatalogsInProcedureCalls()
/*      */     throws SQLException
/*      */   {
/* 1440 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1441 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsCatalogsInProcedureCalls");
/*      */     }
/* 1443 */     int i = getInfo((short)92);
/*      */ 
/* 1445 */     return (i & 0x2) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsCatalogsInTableDefinitions()
/*      */     throws SQLException
/*      */   {
/* 1455 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1456 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsCatalogsInTableDefinitions");
/*      */     }
/* 1458 */     int i = getInfo((short)92);
/*      */ 
/* 1460 */     return (i & 0x4) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsCatalogsInIndexDefinitions()
/*      */     throws SQLException
/*      */   {
/* 1470 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1471 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsCatalogsInIndexDefinitions");
/*      */     }
/* 1473 */     int i = getInfo((short)92);
/*      */ 
/* 1475 */     return (i & 0x8) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsCatalogsInPrivilegeDefinitions()
/*      */     throws SQLException
/*      */   {
/* 1485 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1486 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsCatalogsInPrivilegeDefintions");
/*      */     }
/* 1488 */     int i = getInfo((short)92);
/*      */ 
/* 1490 */     return (i & 0x10) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsPositionedDelete()
/*      */     throws SQLException
/*      */   {
/* 1501 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1502 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsPositionedDelete");
/*      */     }
/* 1504 */     int i = getInfo((short)80);
/*      */ 
/* 1506 */     return (i & 0x1) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsPositionedUpdate()
/*      */     throws SQLException
/*      */   {
/* 1517 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1518 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsPositionedUpdate");
/*      */     }
/* 1520 */     int i = getInfo((short)80);
/*      */ 
/* 1522 */     return (i & 0x2) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsSelectForUpdate()
/*      */     throws SQLException
/*      */   {
/* 1533 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1534 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsSelectForUpdate");
/*      */     }
/* 1536 */     int i = getInfo((short)80);
/*      */ 
/* 1538 */     return (i & 0x4) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsStoredProcedures()
/*      */     throws SQLException
/*      */   {
/* 1549 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1550 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsStoredProcedures");
/*      */     }
/* 1552 */     return getInfoBooleanString((short)21);
/*      */   }
/*      */ 
/*      */   public boolean supportsSubqueriesInComparisons()
/*      */     throws SQLException
/*      */   {
/* 1562 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1563 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsSubqueriesInComparisions");
/*      */     }
/* 1565 */     int i = getInfo((short)95);
/*      */ 
/* 1567 */     return (i & 0x1) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsSubqueriesInExists()
/*      */     throws SQLException
/*      */   {
/* 1577 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1578 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsSubqueriesInExists");
/*      */     }
/* 1580 */     int i = getInfo((short)95);
/*      */ 
/* 1582 */     return (i & 0x2) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsSubqueriesInIns()
/*      */     throws SQLException
/*      */   {
/* 1592 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1593 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsSubqueriesInIns");
/*      */     }
/* 1595 */     int i = getInfo((short)95);
/*      */ 
/* 1597 */     return (i & 0x4) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsSubqueriesInQuantifieds()
/*      */     throws SQLException
/*      */   {
/* 1607 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1608 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsSubqueriesInQuantifieds");
/*      */     }
/* 1610 */     int i = getInfo((short)95);
/*      */ 
/* 1612 */     return (i & 0x8) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsCorrelatedSubqueries()
/*      */     throws SQLException
/*      */   {
/* 1622 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1623 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsCorrelatedSubqueries");
/*      */     }
/* 1625 */     int i = getInfo((short)95);
/*      */ 
/* 1627 */     return (i & 0x10) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsUnion()
/*      */     throws SQLException
/*      */   {
/* 1638 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1639 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsUnion");
/*      */     }
/* 1641 */     int i = getInfo((short)96);
/*      */ 
/* 1643 */     return (i & 0x1) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsUnionAll()
/*      */     throws SQLException
/*      */   {
/* 1655 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1656 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsUnionAll");
/*      */     }
/* 1658 */     int i = getInfo((short)96);
/*      */ 
/* 1660 */     return (i & 0x2) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsOpenCursorsAcrossCommit()
/*      */     throws SQLException
/*      */   {
/* 1675 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1676 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsOpenCursorsAcrossCommit");
/*      */     }
/* 1678 */     int i = getInfoShort((short)23);
/*      */ 
/* 1680 */     return i == 2;
/*      */   }
/*      */ 
/*      */   public boolean supportsOpenCursorsAcrossRollback()
/*      */     throws SQLException
/*      */   {
/* 1686 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1687 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsOpenCursorsAcrossRollback");
/*      */     }
/* 1689 */     int i = getInfoShort((short)24);
/*      */ 
/* 1692 */     return i == 2;
/*      */   }
/*      */ 
/*      */   public boolean supportsOpenStatementsAcrossCommit()
/*      */     throws SQLException
/*      */   {
/* 1698 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1699 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsOpenStatementsAcrossCommit");
/*      */     }
/* 1701 */     int i = getInfoShort((short)23);
/*      */ 
/* 1703 */     return (i == 2) || (i == 1);
/*      */   }
/*      */ 
/*      */   public boolean supportsOpenStatementsAcrossRollback()
/*      */     throws SQLException
/*      */   {
/* 1710 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1711 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsOpenStatementsAcrossRollback");
/*      */     }
/* 1713 */     int i = getInfoShort((short)24);
/*      */ 
/* 1716 */     return (i == 2) || (i == 1);
/*      */   }
/*      */ 
/*      */   public int getMaxBinaryLiteralLength()
/*      */     throws SQLException
/*      */   {
/* 1729 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1730 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxBinaryLiteralLength");
/*      */     }
/* 1732 */     return getInfo((short)112);
/*      */   }
/*      */ 
/*      */   public int getMaxCharLiteralLength()
/*      */     throws SQLException
/*      */   {
/* 1743 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1744 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxCharLiteralLength");
/*      */     }
/* 1746 */     return getInfo((short)108);
/*      */   }
/*      */ 
/*      */   public int getMaxColumnNameLength()
/*      */     throws SQLException
/*      */   {
/* 1757 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1758 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxColumnNameLength");
/*      */     }
/* 1760 */     return getInfoShort((short)30);
/*      */   }
/*      */ 
/*      */   public int getMaxColumnsInGroupBy()
/*      */     throws SQLException
/*      */   {
/* 1771 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1772 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxColumnsInGroupBy");
/*      */     }
/* 1774 */     return getInfoShort((short)97);
/*      */   }
/*      */ 
/*      */   public int getMaxColumnsInIndex()
/*      */     throws SQLException
/*      */   {
/* 1785 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1786 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxColumnsInIndex");
/*      */     }
/* 1788 */     return getInfoShort((short)98);
/*      */   }
/*      */ 
/*      */   public int getMaxColumnsInOrderBy()
/*      */     throws SQLException
/*      */   {
/* 1799 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1800 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxColumnsInOrderBy");
/*      */     }
/* 1802 */     return getInfoShort((short)99);
/*      */   }
/*      */ 
/*      */   public int getMaxColumnsInSelect()
/*      */     throws SQLException
/*      */   {
/* 1813 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1814 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxColumnsInSeleted");
/*      */     }
/* 1816 */     return getInfoShort((short)100);
/*      */   }
/*      */ 
/*      */   public int getMaxColumnsInTable()
/*      */     throws SQLException
/*      */   {
/* 1827 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1828 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxColumnsInTable");
/*      */     }
/* 1830 */     return getInfoShort((short)101);
/*      */   }
/*      */ 
/*      */   public int getMaxConnections()
/*      */     throws SQLException
/*      */   {
/* 1841 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1842 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxConnections");
/*      */     }
/* 1844 */     return getInfoShort((short)0);
/*      */   }
/*      */ 
/*      */   public int getMaxCursorNameLength()
/*      */     throws SQLException
/*      */   {
/* 1855 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1856 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxCursorNameLength");
/*      */     }
/* 1858 */     return getInfo((short)31);
/*      */   }
/*      */ 
/*      */   public int getMaxIndexLength()
/*      */     throws SQLException
/*      */   {
/* 1869 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1870 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxIndexLength");
/*      */     }
/* 1872 */     return getInfo((short)102);
/*      */   }
/*      */ 
/*      */   public int getMaxSchemaNameLength()
/*      */     throws SQLException
/*      */   {
/* 1883 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1884 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxSchemaNameLength");
/*      */     }
/* 1886 */     return getInfoShort((short)32);
/*      */   }
/*      */ 
/*      */   public int getMaxProcedureNameLength()
/*      */     throws SQLException
/*      */   {
/* 1897 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1898 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxProcedureNameLength");
/*      */     }
/* 1900 */     return getInfoShort((short)33);
/*      */   }
/*      */ 
/*      */   public int getMaxCatalogNameLength()
/*      */     throws SQLException
/*      */   {
/* 1911 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1912 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxCatalogNameLength");
/*      */     }
/* 1914 */     return getInfoShort((short)34);
/*      */   }
/*      */ 
/*      */   public int getMaxRowSize()
/*      */     throws SQLException
/*      */   {
/* 1925 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1926 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxRowSize");
/*      */     }
/* 1928 */     return getInfo((short)104);
/*      */   }
/*      */ 
/*      */   public boolean doesMaxRowSizeIncludeBlobs()
/*      */     throws SQLException
/*      */   {
/* 1940 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1941 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.doesMaxRowSizeIncludeBlobs");
/*      */     }
/* 1943 */     return getInfoBooleanString((short)103);
/*      */   }
/*      */ 
/*      */   public int getMaxStatementLength()
/*      */     throws SQLException
/*      */   {
/* 1955 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1956 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxStatementLength");
/*      */     }
/* 1958 */     return getInfo((short)105);
/*      */   }
/*      */ 
/*      */   public int getMaxStatements()
/*      */     throws SQLException
/*      */   {
/* 1969 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1970 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxStatements");
/*      */     }
/* 1972 */     return getInfoShort((short)1);
/*      */   }
/*      */ 
/*      */   public int getMaxTableNameLength()
/*      */     throws SQLException
/*      */   {
/* 1983 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1984 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxTableNameLength");
/*      */     }
/* 1986 */     return getInfoShort((short)35);
/*      */   }
/*      */ 
/*      */   public int getMaxTablesInSelect()
/*      */     throws SQLException
/*      */   {
/* 1997 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 1998 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxTablesInSelect");
/*      */     }
/* 2000 */     return getInfoShort((short)106);
/*      */   }
/*      */ 
/*      */   public int getMaxUserNameLength()
/*      */     throws SQLException
/*      */   {
/* 2011 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2012 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getMaxUserNameLength");
/*      */     }
/* 2014 */     return getInfoShort((short)107);
/*      */   }
/*      */ 
/*      */   public int getDefaultTransactionIsolation()
/*      */     throws SQLException
/*      */   {
/* 2028 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2029 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getDefaultTransactionIsolation");
/*      */     }
/* 2031 */     int i = getInfo((short)26);
/* 2032 */     int j = 0;
/*      */ 
/* 2036 */     switch (i) {
/*      */     case 1:
/* 2038 */       j = 1;
/* 2039 */       break;
/*      */     case 2:
/* 2041 */       j = 2;
/* 2042 */       break;
/*      */     case 4:
/* 2044 */       j = 4;
/* 2045 */       break;
/*      */     case 8:
/* 2047 */       j = 8;
/*      */     case 3:
/*      */     case 5:
/*      */     case 6:
/* 2050 */     case 7: } return j;
/*      */   }
/*      */ 
/*      */   public boolean supportsTransactions()
/*      */     throws SQLException
/*      */   {
/* 2063 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2064 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsTransactions");
/*      */     }
/* 2066 */     int i = getInfoShort((short)46);
/*      */ 
/* 2068 */     return i != 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsTransactionIsolationLevel(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 2081 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2082 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsTransactionIsolationLevel (" + paramInt + ")");
/*      */     }
/*      */ 
/* 2088 */     if (paramInt == 0) {
/* 2089 */       return !supportsTransactions();
/*      */     }
/*      */ 
/* 2092 */     int i = getInfo((short)72);
/* 2093 */     boolean bool = false;
/*      */ 
/* 2095 */     switch (paramInt) {
/*      */     case 1:
/* 2097 */       bool = (i & 0x1) > 0;
/*      */ 
/* 2099 */       break;
/*      */     case 2:
/* 2101 */       bool = (i & 0x2) > 0;
/*      */ 
/* 2103 */       break;
/*      */     case 4:
/* 2105 */       bool = (i & 0x4) > 0;
/*      */ 
/* 2107 */       break;
/*      */     case 8:
/* 2109 */       bool = (i & 0x8) > 0;
/*      */     case 3:
/*      */     case 5:
/*      */     case 6:
/* 2113 */     case 7: } return bool;
/*      */   }
/*      */ 
/*      */   public boolean supportsDataDefinitionAndDataManipulationTransactions()
/*      */     throws SQLException
/*      */   {
/* 2125 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2126 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsDataDefinitionAndDataManipulationTransactions");
/*      */     }
/* 2128 */     int i = getInfoShort((short)46);
/*      */ 
/* 2130 */     return (i & 0x2) > 0;
/*      */   }
/*      */ 
/*      */   public boolean supportsDataManipulationTransactionsOnly()
/*      */     throws SQLException
/*      */   {
/* 2136 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2137 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsDataManipulationTransactionsOnly");
/*      */     }
/* 2139 */     int i = getInfoShort((short)46);
/*      */ 
/* 2141 */     return (i & 0x1) > 0;
/*      */   }
/*      */ 
/*      */   public boolean dataDefinitionCausesTransactionCommit()
/*      */     throws SQLException
/*      */   {
/* 2147 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2148 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.dataDefintionCausesTransactionCommit");
/*      */     }
/* 2150 */     int i = getInfoShort((short)46);
/*      */ 
/* 2152 */     return (i & 0x3) > 0;
/*      */   }
/*      */ 
/*      */   public boolean dataDefinitionIgnoredInTransactions()
/*      */     throws SQLException
/*      */   {
/* 2158 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2159 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.dataDefintionIgnoredInTransactions");
/*      */     }
/* 2161 */     int i = getInfoShort((short)46);
/*      */ 
/* 2163 */     return (i & 0x4) > 0;
/*      */   }
/*      */ 
/*      */   public ResultSet getProcedures(String paramString1, String paramString2, String paramString3)
/*      */     throws SQLException
/*      */   {
/* 2191 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2192 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getProcedures (" + paramString1 + "," + paramString2 + "," + paramString3 + ")");
/*      */     }
/*      */ 
/* 2197 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/* 2198 */     Object localObject = null;
/*      */ 
/* 2202 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */     try
/*      */     {
/* 2206 */       this.OdbcApi.SQLProcedures(l, paramString1, paramString2, paramString3);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 2214 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 2222 */       this.OdbcApi.SQLFreeStmt(l, 1);
/*      */ 
/* 2224 */       throw localSQLException;
/*      */     }
/*      */ 
/* 2229 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 2230 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, l, false, null);
/* 2231 */     localJdbcOdbcResultSet.setWarning(localObject);
/*      */ 
/* 2235 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getProcedureColumns(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */     throws SQLException
/*      */   {
/* 2272 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2273 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getProcedureColumns (" + paramString1 + "," + paramString2 + "," + paramString3 + "," + paramString4 + ")");
/*      */     }
/*      */ 
/* 2279 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/* 2280 */     Object localObject = null;
/*      */ 
/* 2284 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */     try
/*      */     {
/* 2288 */       this.OdbcApi.SQLProcedureColumns(l, paramString1, paramString2, paramString3, paramString4);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 2297 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 2305 */       this.OdbcApi.SQLFreeStmt(l, 1);
/*      */ 
/* 2307 */       throw localSQLException;
/*      */     }
/*      */ 
/* 2312 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 2313 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, l, false, null);
/* 2314 */     localJdbcOdbcResultSet.setWarning(localObject);
/*      */ 
/* 2322 */     if (this.Con.getODBCVer() >= 2) {
/* 2323 */       localJdbcOdbcResultSet.setSQLTypeColumn(6);
/*      */ 
/* 2326 */       localJdbcOdbcResultSet.setAliasColumnName("PRECISION", 8);
/* 2327 */       localJdbcOdbcResultSet.setAliasColumnName("LENGTH", 9);
/* 2328 */       localJdbcOdbcResultSet.setAliasColumnName("SCALE", 10);
/* 2329 */       localJdbcOdbcResultSet.setAliasColumnName("RADIX", 11);
/*      */     }
/*      */ 
/* 2334 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getTables(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString)
/*      */     throws SQLException
/*      */   {
/* 2363 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/* 2364 */     String str1 = null;
/* 2365 */     Object localObject = null;
/*      */ 
/* 2368 */     if (paramArrayOfString != null) {
/* 2369 */       str1 = "";
/* 2370 */       int i = 0;
/* 2371 */       for (i = 0; i < paramArrayOfString.length; i = (short)(i + 1)) {
/* 2372 */         String str2 = paramArrayOfString[i];
/* 2373 */         if (i > 0)
/*      */         {
/* 2375 */           str1 = str1 + ",";
/*      */         }
/* 2377 */         str1 = str1 + str2;
/*      */       }
/*      */     }
/*      */ 
/* 2381 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2382 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getTables (" + paramString1 + "," + paramString2 + "," + paramString3 + "," + str1 + ")");
/*      */     }
/*      */ 
/* 2389 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */     try
/*      */     {
/* 2394 */       this.OdbcApi.SQLTables(l, paramString1, paramString2, paramString3, str1);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 2402 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 2410 */       this.OdbcApi.SQLFreeStmt(l, 1);
/*      */ 
/* 2412 */       throw localSQLException;
/*      */     }
/*      */ 
/* 2417 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 2418 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, l, false, null);
/* 2419 */     localJdbcOdbcResultSet.setWarning(localObject);
/*      */ 
/* 2423 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getSchemas()
/*      */     throws SQLException
/*      */   {
/* 2437 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2438 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getSchemas");
/*      */     }
/*      */ 
/* 2443 */     JdbcOdbcResultSet localJdbcOdbcResultSet = (JdbcOdbcResultSet)getTables("", "%", "", null);
/*      */ 
/* 2448 */     int[] arrayOfInt = new int[1];
/* 2449 */     arrayOfInt[0] = 2;
/* 2450 */     localJdbcOdbcResultSet.setColumnMappings(arrayOfInt);
/* 2451 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getCatalogs()
/*      */     throws SQLException
/*      */   {
/* 2465 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2466 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getCatalogs");
/*      */     }
/*      */ 
/* 2470 */     JdbcOdbcResultSet localJdbcOdbcResultSet = (JdbcOdbcResultSet)getTables("%", "", "", null);
/*      */ 
/* 2474 */     int[] arrayOfInt = new int[1];
/* 2475 */     arrayOfInt[0] = 1;
/* 2476 */     localJdbcOdbcResultSet.setColumnMappings(arrayOfInt);
/* 2477 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getTableTypes()
/*      */     throws SQLException
/*      */   {
/* 2491 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2492 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getTableTypes");
/*      */     }
/*      */ 
/* 2500 */     String[] arrayOfString = new String[1];
/* 2501 */     arrayOfString[0] = "%";
/*      */ 
/* 2504 */     JdbcOdbcResultSet localJdbcOdbcResultSet = (JdbcOdbcResultSet)getTables(null, null, "%", null);
/*      */ 
/* 2509 */     int[] arrayOfInt = new int[1];
/* 2510 */     arrayOfInt[0] = 4;
/* 2511 */     localJdbcOdbcResultSet.setColumnMappings(arrayOfInt);
/* 2512 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getColumns(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */     throws SQLException
/*      */   {
/* 2557 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2558 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getColumns (" + paramString1 + "," + paramString2 + "," + paramString3 + "," + paramString4 + ")");
/*      */     }
/*      */ 
/* 2564 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/* 2565 */     Object localObject = null;
/*      */ 
/* 2569 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */     try
/*      */     {
/* 2573 */       this.OdbcApi.SQLColumns(l, paramString1, paramString2, paramString3, paramString4);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 2581 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 2589 */       this.OdbcApi.SQLFreeStmt(l, 1);
/*      */ 
/* 2591 */       throw localSQLException;
/*      */     }
/*      */ 
/* 2596 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 2597 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, l, false, null);
/* 2598 */     localJdbcOdbcResultSet.setWarning(localObject);
/*      */ 
/* 2604 */     if (this.Con.getODBCVer() == 2) {
/* 2605 */       JdbcOdbcPseudoCol[] arrayOfJdbcOdbcPseudoCol = new JdbcOdbcPseudoCol[6];
/*      */ 
/* 2607 */       arrayOfJdbcOdbcPseudoCol[0] = new JdbcOdbcPseudoCol("COLUMN_DEF", 12, 254);
/*      */ 
/* 2609 */       arrayOfJdbcOdbcPseudoCol[1] = new JdbcOdbcPseudoCol("SQL_DATA_TYPE", 5, 0);
/*      */ 
/* 2611 */       arrayOfJdbcOdbcPseudoCol[2] = new JdbcOdbcPseudoCol("SQL_DATETIME_SUB", 5, 0);
/*      */ 
/* 2613 */       arrayOfJdbcOdbcPseudoCol[3] = new JdbcOdbcPseudoCol("CHAR_OCTET_LENGTH", 4, 0);
/*      */ 
/* 2615 */       arrayOfJdbcOdbcPseudoCol[4] = new JdbcOdbcPseudoCol("ORDINAL_POSITION", 4, 0);
/*      */ 
/* 2617 */       arrayOfJdbcOdbcPseudoCol[5] = new JdbcOdbcPseudoCol("IS_NULLABLE", 12, 254);
/*      */ 
/* 2624 */       localJdbcOdbcResultSet.setPseudoCols(13, 18, arrayOfJdbcOdbcPseudoCol);
/* 2625 */       localJdbcOdbcResultSet.setSQLTypeColumn(5);
/*      */     }
/* 2627 */     else if (this.Con.getODBCVer() >= 3) {
/* 2628 */       localJdbcOdbcResultSet.setSQLTypeColumn(5);
/* 2629 */       localJdbcOdbcResultSet.setAliasColumnName("SQL_DATETIME_SUB", 15);
/*      */     }
/*      */ 
/* 2634 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getColumnPrivileges(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */     throws SQLException
/*      */   {
/* 2662 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2663 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getColumnPrivileges (" + paramString1 + "," + paramString2 + "," + paramString3 + "," + paramString4 + ")");
/*      */     }
/*      */ 
/* 2668 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/* 2669 */     Object localObject = null;
/*      */ 
/* 2673 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */     try
/*      */     {
/* 2677 */       this.OdbcApi.SQLColumnPrivileges(l, paramString1, paramString2, paramString3, paramString4);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 2685 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 2693 */       this.OdbcApi.SQLFreeStmt(l, 1);
/*      */ 
/* 2695 */       throw localSQLException;
/*      */     }
/*      */ 
/* 2700 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 2701 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, l, false, null);
/* 2702 */     localJdbcOdbcResultSet.setWarning(localObject);
/*      */ 
/* 2706 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getTablePrivileges(String paramString1, String paramString2, String paramString3)
/*      */     throws SQLException
/*      */   {
/* 2732 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2733 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getTablePrivileges (" + paramString1 + "," + paramString2 + "," + paramString3 + ")");
/*      */     }
/*      */ 
/* 2739 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/* 2740 */     Object localObject = null;
/*      */ 
/* 2744 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */     try
/*      */     {
/* 2748 */       this.OdbcApi.SQLTablePrivileges(l, paramString1, paramString2, paramString3);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 2756 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 2764 */       this.OdbcApi.SQLFreeStmt(l, 1);
/*      */ 
/* 2766 */       throw localSQLException;
/*      */     }
/*      */ 
/* 2771 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 2772 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, l, false, null);
/* 2773 */     localJdbcOdbcResultSet.setWarning(localObject);
/*      */ 
/* 2777 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getBestRowIdentifier(String paramString1, String paramString2, String paramString3, int paramInt, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/* 2814 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2815 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getBestRowIdentifier (" + paramString1 + "," + paramString2 + "," + paramString3 + "," + paramInt + "," + paramBoolean + ")");
/*      */     }
/*      */ 
/* 2820 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/* 2821 */     Object localObject = null;
/*      */ 
/* 2825 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */     try
/*      */     {
/* 2829 */       this.OdbcApi.SQLSpecialColumns(l, (short)1, paramString1, paramString2, paramString3, paramInt, paramBoolean);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 2838 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 2846 */       this.OdbcApi.SQLFreeStmt(l, 1);
/*      */ 
/* 2848 */       throw localSQLException;
/*      */     }
/*      */ 
/* 2853 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 2854 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, l, false, null);
/* 2855 */     localJdbcOdbcResultSet.setWarning(localObject);
/*      */ 
/* 2857 */     if (this.Con.getODBCVer() >= 2) {
/* 2858 */       localJdbcOdbcResultSet.setSQLTypeColumn(3);
/*      */     }
/*      */ 
/* 2863 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getVersionColumns(String paramString1, String paramString2, String paramString3)
/*      */     throws SQLException
/*      */   {
/* 2890 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2891 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getVersionColumns (" + paramString1 + "," + paramString2 + "," + paramString3 + ")");
/*      */     }
/*      */ 
/* 2895 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/* 2896 */     Object localObject = null;
/*      */ 
/* 2900 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */     try
/*      */     {
/* 2904 */       this.OdbcApi.SQLSpecialColumns(l, (short)2, paramString1, paramString2, paramString3, 0, false);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 2913 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 2921 */       this.OdbcApi.SQLFreeStmt(l, 1);
/*      */ 
/* 2923 */       throw localSQLException;
/*      */     }
/*      */ 
/* 2928 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 2929 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, l, false, null);
/* 2930 */     localJdbcOdbcResultSet.setWarning(localObject);
/*      */ 
/* 2932 */     if (this.Con.getODBCVer() >= 2) {
/* 2933 */       localJdbcOdbcResultSet.setSQLTypeColumn(3);
/*      */     }
/*      */ 
/* 2938 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getPrimaryKeys(String paramString1, String paramString2, String paramString3)
/*      */     throws SQLException
/*      */   {
/* 2961 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 2962 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getPrimaryKeys (" + paramString1 + "," + paramString2 + "," + paramString3 + ")");
/*      */     }
/*      */ 
/* 2966 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/* 2967 */     Object localObject = null;
/*      */ 
/* 2971 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */     try
/*      */     {
/* 2975 */       this.OdbcApi.SQLPrimaryKeys(l, paramString1, paramString2, paramString3);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 2983 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 2991 */       this.OdbcApi.SQLFreeStmt(l, 1);
/*      */ 
/* 2993 */       throw localSQLException;
/*      */     }
/*      */ 
/* 2998 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 2999 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, l, false, null);
/* 3000 */     localJdbcOdbcResultSet.setWarning(localObject);
/*      */ 
/* 3004 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getImportedKeys(String paramString1, String paramString2, String paramString3)
/*      */     throws SQLException
/*      */   {
/* 3044 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 3045 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getImportedKeys (" + paramString1 + "," + paramString2 + "," + paramString3 + ")");
/*      */     }
/*      */ 
/* 3049 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/* 3050 */     Object localObject = null;
/*      */ 
/* 3054 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */     try
/*      */     {
/* 3058 */       this.OdbcApi.SQLForeignKeys(l, null, null, null, paramString1, paramString2, paramString3);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 3067 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 3075 */       this.OdbcApi.SQLFreeStmt(l, 1);
/*      */ 
/* 3077 */       throw localSQLException;
/*      */     }
/*      */ 
/* 3082 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 3083 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, l, false, null);
/* 3084 */     localJdbcOdbcResultSet.setWarning(localObject);
/*      */ 
/* 3090 */     if (this.Con.getODBCVer() >= 2) {
/* 3091 */       JdbcOdbcPseudoCol[] arrayOfJdbcOdbcPseudoCol = new JdbcOdbcPseudoCol[1];
/*      */ 
/* 3093 */       arrayOfJdbcOdbcPseudoCol[0] = new JdbcOdbcPseudoCol("DEFERRABILITY", 5, 0);
/*      */ 
/* 3100 */       localJdbcOdbcResultSet.setPseudoCols(14, 14, arrayOfJdbcOdbcPseudoCol);
/*      */     }
/*      */ 
/* 3105 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getExportedKeys(String paramString1, String paramString2, String paramString3)
/*      */     throws SQLException
/*      */   {
/* 3124 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 3125 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getExportedKeys (" + paramString1 + "," + paramString2 + "," + paramString3 + ")");
/*      */     }
/*      */ 
/* 3129 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/* 3130 */     Object localObject = null;
/*      */ 
/* 3134 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */     try
/*      */     {
/* 3138 */       this.OdbcApi.SQLForeignKeys(l, paramString1, paramString2, paramString3, null, null, null);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 3147 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 3155 */       this.OdbcApi.SQLFreeStmt(l, 1);
/*      */ 
/* 3157 */       throw localSQLException;
/*      */     }
/*      */ 
/* 3162 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 3163 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, l, false, null);
/* 3164 */     localJdbcOdbcResultSet.setWarning(localObject);
/*      */ 
/* 3170 */     if (this.Con.getODBCVer() >= 2) {
/* 3171 */       JdbcOdbcPseudoCol[] arrayOfJdbcOdbcPseudoCol = new JdbcOdbcPseudoCol[1];
/*      */ 
/* 3173 */       arrayOfJdbcOdbcPseudoCol[0] = new JdbcOdbcPseudoCol("DEFERRABILITY", 5, 0);
/*      */ 
/* 3180 */       localJdbcOdbcResultSet.setPseudoCols(14, 14, arrayOfJdbcOdbcPseudoCol);
/*      */     }
/*      */ 
/* 3185 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getCrossReference(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
/*      */     throws SQLException
/*      */   {
/* 3207 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 3208 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getCrossReference (" + paramString1 + "," + paramString2 + "," + paramString3 + "," + paramString4 + "," + paramString5 + "," + paramString6 + ")");
/*      */     }
/*      */ 
/* 3215 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/* 3216 */     Object localObject = null;
/*      */ 
/* 3220 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */     try
/*      */     {
/* 3224 */       this.OdbcApi.SQLForeignKeys(l, paramString1, paramString2, paramString3, paramString4, paramString5, paramString6);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 3234 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 3242 */       this.OdbcApi.SQLFreeStmt(l, 1);
/*      */ 
/* 3244 */       throw localSQLException;
/*      */     }
/*      */ 
/* 3249 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 3250 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, l, false, null);
/* 3251 */     localJdbcOdbcResultSet.setWarning(localObject);
/*      */ 
/* 3257 */     if (this.Con.getODBCVer() >= 2) {
/* 3258 */       JdbcOdbcPseudoCol[] arrayOfJdbcOdbcPseudoCol = new JdbcOdbcPseudoCol[1];
/*      */ 
/* 3260 */       arrayOfJdbcOdbcPseudoCol[0] = new JdbcOdbcPseudoCol("DEFERRABILITY", 5, 0);
/*      */ 
/* 3267 */       localJdbcOdbcResultSet.setPseudoCols(14, 14, arrayOfJdbcOdbcPseudoCol);
/*      */     }
/*      */ 
/* 3272 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public boolean supportsResultSetType(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3287 */     short s = getConnectionSupportType(paramInt);
/*      */ 
/* 3289 */     switch (paramInt)
/*      */     {
/*      */     case 1003:
/* 3293 */       return s == 0;
/*      */     case 1004:
/* 3295 */       return (s == 3) || (s == 1);
/*      */     case 1005:
/* 3299 */       if (s == 1)
/*      */       {
/* 3301 */         int i = this.Con.getOdbcCursorAttr2(s);
/*      */ 
/* 3303 */         if ((i & 0x40) != 0) {
/* 3304 */           return true;
/*      */         }
/* 3306 */         return false;
/*      */       }
/*      */ 
/* 3309 */       return s == 2;
/*      */     }
/*      */ 
/* 3312 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean supportsResultSetConcurrency(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 3329 */     if (supportsResultSetType(paramInt1))
/*      */     {
/* 3331 */       int i = this.Con.getOdbcConcurrency(paramInt2);
/*      */ 
/* 3333 */       switch (paramInt2)
/*      */       {
/*      */       case 1007:
/* 3336 */         return i == 1;
/*      */       case 1008:
/* 3338 */         if (paramInt1 != 1003) {
/* 3339 */           return i == 2;
/*      */         }
/* 3341 */         return false;
/*      */       }
/* 3343 */       return false;
/*      */     }
/*      */ 
/* 3347 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean ownUpdatesAreVisible(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3360 */     if (paramInt != 1003)
/*      */     {
/* 3362 */       return updatesAreDetected(paramInt);
/*      */     }
/*      */ 
/* 3365 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean ownDeletesAreVisible(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3377 */     if (paramInt != 1003)
/*      */     {
/* 3379 */       return deletesAreDetected(paramInt);
/*      */     }
/*      */ 
/* 3382 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean ownInsertsAreVisible(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3393 */     if (paramInt != 1003)
/*      */     {
/* 3395 */       return insertsAreDetected(paramInt);
/*      */     }
/*      */ 
/* 3398 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean othersUpdatesAreVisible(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3410 */     if (paramInt == 1005)
/*      */     {
/* 3412 */       return updatesAreDetected(paramInt);
/*      */     }
/*      */ 
/* 3415 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean othersDeletesAreVisible(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3427 */     if (paramInt == 1005)
/*      */     {
/* 3429 */       return deletesAreDetected(paramInt);
/*      */     }
/*      */ 
/* 3432 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean othersInsertsAreVisible(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3444 */     if (paramInt == 1005)
/*      */     {
/* 3446 */       return insertsAreDetected(paramInt);
/*      */     }
/*      */ 
/* 3449 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean updatesAreDetected(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3461 */     short s = getCursorAttribute(paramInt);
/*      */ 
/* 3463 */     if (s > 0)
/*      */     {
/*      */       try
/*      */       {
/* 3467 */         int i = this.OdbcApi.SQLGetInfo(this.hDbc, s);
/*      */ 
/* 3469 */         return (i & 0x40) > 0;
/*      */       }
/*      */       catch (SQLException localSQLException)
/*      */       {
/* 3474 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 3478 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean deletesAreDetected(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3489 */     short s = getCursorAttribute(paramInt);
/*      */ 
/* 3491 */     if (s > 0)
/*      */     {
/*      */       try
/*      */       {
/* 3495 */         int i = this.OdbcApi.SQLGetInfo(this.hDbc, s);
/*      */ 
/* 3497 */         return (i & 0x20) > 0;
/*      */       }
/*      */       catch (SQLException localSQLException)
/*      */       {
/* 3502 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 3506 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean insertsAreDetected(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3518 */     short s = getCursorAttribute(paramInt);
/*      */ 
/* 3520 */     if (s > 0)
/*      */     {
/*      */       try
/*      */       {
/* 3524 */         int i = this.OdbcApi.SQLGetInfo(this.hDbc, s);
/*      */ 
/* 3526 */         return (i & 0x10) > 0;
/*      */       }
/*      */       catch (SQLException localSQLException)
/*      */       {
/* 3531 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 3535 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean supportsBatchUpdates()
/*      */     throws SQLException
/*      */   {
/* 3547 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 3548 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.supportsBatchUpdates");
/*      */     }
/*      */ 
/* 3552 */     int i = 0;
/*      */ 
/* 3555 */     int j = 0;
/* 3556 */     int k = 0;
/* 3557 */     int m = 0;
/*      */     try
/*      */     {
/* 3562 */       i = this.OdbcApi.SQLGetInfo(this.hDbc, (short)121);
/*      */ 
/* 3564 */       if ((i & 0x2) > 0)
/*      */       {
/* 3566 */         j = 1;
/*      */       }
/*      */ 
/* 3572 */       i = this.OdbcApi.SQLGetInfo(this.hDbc, (short)153);
/*      */ 
/* 3574 */       if ((i & 0x1) > 0)
/*      */       {
/* 3576 */         m = 1;
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 3587 */       j = 0;
/* 3588 */       m = 0;
/*      */     }
/*      */ 
/* 3591 */     if ((j == 1) && (m == 1))
/*      */     {
/* 3593 */       return true;
/*      */     }
/*      */ 
/* 3599 */     return true;
/*      */   }
/*      */ 
/*      */   public ResultSet getUDTs(String paramString1, String paramString2, String paramString3, int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 3611 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public Connection getConnection()
/*      */     throws SQLException
/*      */   {
/* 3618 */     if ((this.Con != null) && (this.hDbc > 0L))
/*      */     {
/* 3620 */       return this.Con;
/*      */     }
/*      */ 
/* 3623 */     return null;
/*      */   }
/*      */ 
/*      */   public ResultSet getTypeInfo()
/*      */     throws SQLException
/*      */   {
/* 3660 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 3661 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getTypeInfo");
/*      */     }
/*      */ 
/* 3664 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/* 3665 */     Object localObject = null;
/*      */ 
/* 3669 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */     try
/*      */     {
/* 3673 */       this.OdbcApi.SQLGetTypeInfo(l, (short)0);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 3681 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 3689 */       this.OdbcApi.SQLFreeStmt(l, 1);
/*      */ 
/* 3691 */       throw localSQLException;
/*      */     }
/*      */ 
/* 3696 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 3697 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, l, false, null);
/* 3698 */     localJdbcOdbcResultSet.setWarning(localObject);
/*      */ 
/* 3704 */     if (this.Con.getODBCVer() == 2) {
/* 3705 */       JdbcOdbcPseudoCol[] arrayOfJdbcOdbcPseudoCol = new JdbcOdbcPseudoCol[5];
/*      */ 
/* 3707 */       arrayOfJdbcOdbcPseudoCol[0] = new JdbcOdbcPseudoCol("SQL_DATA_TYPE", 5, 0);
/*      */ 
/* 3709 */       arrayOfJdbcOdbcPseudoCol[1] = new JdbcOdbcPseudoCol("SQL_DATETIME_SUB", 5, 0);
/*      */ 
/* 3711 */       arrayOfJdbcOdbcPseudoCol[2] = new JdbcOdbcPseudoCol("NUM_PREC_RADIX", 5, 0);
/*      */ 
/* 3718 */       localJdbcOdbcResultSet.setPseudoCols(16, 18, arrayOfJdbcOdbcPseudoCol);
/* 3719 */       localJdbcOdbcResultSet.setSQLTypeColumn(2);
/*      */     }
/* 3721 */     else if (this.Con.getODBCVer() >= 3) {
/* 3722 */       localJdbcOdbcResultSet.setSQLTypeColumn(2);
/*      */     }
/*      */ 
/* 3725 */     if (this.Con.getODBCVer() >= 2)
/*      */     {
/* 3727 */       localJdbcOdbcResultSet.setAliasColumnName("PRECISION", 3);
/* 3728 */       localJdbcOdbcResultSet.setAliasColumnName("AUTO_INCREMENT", 12);
/*      */     }
/*      */ 
/* 3733 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   public ResultSet getIndexInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2)
/*      */     throws SQLException
/*      */   {
/* 3781 */     if (this.OdbcApi.getTracer().isTracing()) {
/* 3782 */       this.OdbcApi.getTracer().trace("*DatabaseMetaData.getIndexInfo (" + paramString1 + "," + paramString2 + "," + paramString3 + paramBoolean1 + "," + paramBoolean2 + ")");
/*      */     }
/*      */ 
/* 3788 */     JdbcOdbcResultSet localJdbcOdbcResultSet = null;
/* 3789 */     Object localObject = null;
/*      */ 
/* 3793 */     long l = this.OdbcApi.SQLAllocStmt(this.hDbc);
/*      */     try
/*      */     {
/* 3797 */       this.OdbcApi.SQLStatistics(l, paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2);
/*      */     }
/*      */     catch (SQLWarning localSQLWarning)
/*      */     {
/* 3806 */       localObject = localSQLWarning;
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/* 3814 */       this.OdbcApi.SQLFreeStmt(l, 1);
/*      */ 
/* 3816 */       throw localSQLException;
/*      */     }
/*      */ 
/* 3821 */     localJdbcOdbcResultSet = new JdbcOdbcResultSet();
/* 3822 */     localJdbcOdbcResultSet.initialize(this.OdbcApi, this.hDbc, l, false, null);
/* 3823 */     localJdbcOdbcResultSet.setWarning(localObject);
/*      */ 
/* 3827 */     return localJdbcOdbcResultSet;
/*      */   }
/*      */ 
/*      */   protected void validateConnection()
/*      */     throws SQLException
/*      */   {
/* 3840 */     this.Con.validateConnection();
/*      */   }
/*      */ 
/*      */   protected int getInfo(short paramShort)
/*      */     throws SQLException
/*      */   {
/* 3858 */     validateConnection();
/*      */ 
/* 3860 */     return this.OdbcApi.SQLGetInfo(this.hDbc, paramShort);
/*      */   }
/*      */ 
/*      */   protected int getInfoShort(short paramShort)
/*      */     throws SQLException
/*      */   {
/* 3874 */     validateConnection();
/*      */ 
/* 3876 */     return this.OdbcApi.SQLGetInfoShort(this.hDbc, paramShort);
/*      */   }
/*      */ 
/*      */   protected boolean getInfoBooleanString(short paramShort)
/*      */     throws SQLException
/*      */   {
/* 3893 */     validateConnection();
/*      */ 
/* 3895 */     String str = this.OdbcApi.SQLGetInfoString(this.hDbc, paramShort);
/*      */ 
/* 3897 */     return str.equalsIgnoreCase("Y");
/*      */   }
/*      */ 
/*      */   protected String getInfoString(short paramShort)
/*      */     throws SQLException
/*      */   {
/* 3911 */     validateConnection();
/*      */ 
/* 3913 */     return this.OdbcApi.SQLGetInfoString(this.hDbc, paramShort);
/*      */   }
/*      */ 
/*      */   protected String getInfoString(short paramShort, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3929 */     validateConnection();
/*      */ 
/* 3931 */     return this.OdbcApi.SQLGetInfoString(this.hDbc, paramShort, paramInt);
/*      */   }
/*      */ 
/*      */   protected short getConnectionSupportType(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3943 */     short s = this.Con.getOdbcCursorType(paramInt);
/*      */ 
/* 3945 */     if (s == -1)
/*      */     {
/* 3947 */       s = this.Con.getBestOdbcCursorType();
/*      */     }
/* 3949 */     return s;
/*      */   }
/*      */ 
/*      */   protected short getCursorAttribute(int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3960 */     short s = 0;
/*      */ 
/* 3962 */     if (supportsResultSetType(paramInt))
/*      */     {
/* 3964 */       int i = getConnectionSupportType(paramInt);
/*      */ 
/* 3966 */       switch (i)
/*      */       {
/*      */       case 1:
/* 3969 */         s = 151;
/* 3970 */         break;
/*      */       case 2:
/* 3972 */         s = 145;
/* 3973 */         break;
/*      */       case 3:
/* 3975 */         s = 168;
/*      */       }
/*      */ 
/* 3978 */       return s;
/*      */     }
/*      */ 
/* 3981 */     return s;
/*      */   }
/*      */ 
/*      */   public boolean supportsSavepoints()
/*      */     throws SQLException
/*      */   {
/* 3989 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean supportsNamedParameters() throws SQLException {
/* 3993 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean supportsMultipleOpenResults() throws SQLException {
/* 3997 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean supportsGetGeneratedKeys() throws SQLException {
/* 4001 */     return false;
/*      */   }
/*      */ 
/*      */   public ResultSet getSuperTypes(String paramString1, String paramString2, String paramString3) throws SQLException
/*      */   {
/* 4006 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public ResultSet getSuperTables(String paramString1, String paramString2, String paramString3) throws SQLException
/*      */   {
/* 4011 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public ResultSet getAttributes(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */     throws SQLException
/*      */   {
/* 4017 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean supportsResultSetHoldability(int paramInt) throws SQLException
/*      */   {
/* 4022 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int getResultSetHoldability() throws SQLException {
/* 4026 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int getDatabaseMajorVersion() throws SQLException {
/* 4030 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int getDatabaseMinorVersion() throws SQLException {
/* 4034 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int getSQLStateType() throws SQLException {
/* 4038 */     return 1;
/*      */   }
/*      */ 
/*      */   public int getJDBCMajorVersion() throws SQLException {
/* 4042 */     return 2;
/*      */   }
/*      */ 
/*      */   public int getJDBCMinorVersion() throws SQLException {
/* 4046 */     return 0;
/*      */   }
/*      */ 
/*      */   public boolean locatorsUpdateCopy() throws SQLException {
/* 4050 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean supportsStatementPooling() throws SQLException {
/* 4054 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public RowIdLifetime getRowIdLifetime()
/*      */     throws SQLException
/*      */   {
/* 4077 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public ResultSet getSchemas(String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/* 4103 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean supportsStoredFunctionsUsingCallSyntax()
/*      */     throws SQLException
/*      */   {
/* 4115 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean autoCommitFailureClosesAllResultSets()
/*      */     throws SQLException
/*      */   {
/* 4130 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public ResultSet getClientInfoProperties()
/*      */     throws SQLException
/*      */   {
/* 4154 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public <T> T unwrap(Class<T> paramClass)
/*      */     throws SQLException
/*      */   {
/* 4171 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isWrapperFor(Class<?> paramClass)
/*      */     throws SQLException
/*      */   {
/* 4191 */     return false;
/*      */   }
/*      */ 
/*      */   public ResultSet getFunctions(String paramString1, String paramString2, String paramString3)
/*      */     throws SQLException
/*      */   {
/* 4234 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public ResultSet getFunctionParameters(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */     throws SQLException
/*      */   {
/* 4323 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public ResultSet getFunctionColumns(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */     throws SQLException
/*      */   {
/* 4330 */     throw new UnsupportedOperationException("Operation not yet supported");
/*      */   }
/*      */ 
/*      */   public ResultSet getPseudoColumns(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */     throws SQLException
/*      */   {
/* 4400 */     throw new SQLFeatureNotSupportedException();
/*      */   }
/*      */ 
/*      */   public boolean generatedKeyAlwaysReturned()
/*      */     throws SQLException
/*      */   {
/* 4414 */     return false;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcDatabaseMetaData
 * JD-Core Version:    0.6.2
 */