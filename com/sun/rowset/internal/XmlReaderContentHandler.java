/*      */ package com.sun.rowset.internal;
/*      */ 
/*      */ import com.sun.rowset.JdbcRowSetResourceBundle;
/*      */ import com.sun.rowset.WebRowSetImpl;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.math.BigDecimal;
/*      */ import java.sql.Date;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Vector;
/*      */ import javax.sql.RowSet;
/*      */ import javax.sql.RowSetMetaData;
/*      */ import javax.sql.rowset.RowSetMetaDataImpl;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.helpers.DefaultHandler;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ 
/*      */ public class XmlReaderContentHandler extends DefaultHandler
/*      */ {
/*      */   private HashMap propMap;
/*      */   private HashMap colDefMap;
/*      */   private HashMap dataMap;
/*      */   private HashMap typeMap;
/*      */   private Vector updates;
/*      */   private Vector keyCols;
/*      */   private String columnValue;
/*      */   private String propertyValue;
/*      */   private String metaDataValue;
/*      */   private int tag;
/*      */   private int state;
/*      */   private WebRowSetImpl rs;
/*      */   private boolean nullVal;
/*      */   private boolean emptyStringVal;
/*      */   private RowSetMetaData md;
/*      */   private int idx;
/*      */   private String lastval;
/*      */   private String Key_map;
/*      */   private String Value_map;
/*      */   private String tempStr;
/*      */   private String tempUpdate;
/*      */   private String tempCommand;
/*      */   private Object[] upd;
/*  109 */   private String[] properties = { "command", "concurrency", "datasource", "escape-processing", "fetch-direction", "fetch-size", "isolation-level", "key-columns", "map", "max-field-size", "max-rows", "query-timeout", "read-only", "rowset-type", "show-deleted", "table-name", "url", "null", "column", "type", "class", "sync-provider", "sync-provider-name", "sync-provider-vendor", "sync-provider-version", "sync-provider-grade", "data-source-lock" };
/*      */   private static final int CommandTag = 0;
/*      */   private static final int ConcurrencyTag = 1;
/*      */   private static final int DatasourceTag = 2;
/*      */   private static final int EscapeProcessingTag = 3;
/*      */   private static final int FetchDirectionTag = 4;
/*      */   private static final int FetchSizeTag = 5;
/*      */   private static final int IsolationLevelTag = 6;
/*      */   private static final int KeycolsTag = 7;
/*      */   private static final int MapTag = 8;
/*      */   private static final int MaxFieldSizeTag = 9;
/*      */   private static final int MaxRowsTag = 10;
/*      */   private static final int QueryTimeoutTag = 11;
/*      */   private static final int ReadOnlyTag = 12;
/*      */   private static final int RowsetTypeTag = 13;
/*      */   private static final int ShowDeletedTag = 14;
/*      */   private static final int TableNameTag = 15;
/*      */   private static final int UrlTag = 16;
/*      */   private static final int PropNullTag = 17;
/*      */   private static final int PropColumnTag = 18;
/*      */   private static final int PropTypeTag = 19;
/*      */   private static final int PropClassTag = 20;
/*      */   private static final int SyncProviderTag = 21;
/*      */   private static final int SyncProviderNameTag = 22;
/*      */   private static final int SyncProviderVendorTag = 23;
/*      */   private static final int SyncProviderVersionTag = 24;
/*      */   private static final int SyncProviderGradeTag = 25;
/*      */   private static final int DataSourceLock = 26;
/*  264 */   private String[] colDef = { "column-count", "column-definition", "column-index", "auto-increment", "case-sensitive", "currency", "nullable", "signed", "searchable", "column-display-size", "column-label", "column-name", "schema-name", "column-precision", "column-scale", "table-name", "catalog-name", "column-type", "column-type-name", "null" };
/*      */   private static final int ColumnCountTag = 0;
/*      */   private static final int ColumnDefinitionTag = 1;
/*      */   private static final int ColumnIndexTag = 2;
/*      */   private static final int AutoIncrementTag = 3;
/*      */   private static final int CaseSensitiveTag = 4;
/*      */   private static final int CurrencyTag = 5;
/*      */   private static final int NullableTag = 6;
/*      */   private static final int SignedTag = 7;
/*      */   private static final int SearchableTag = 8;
/*      */   private static final int ColumnDisplaySizeTag = 9;
/*      */   private static final int ColumnLabelTag = 10;
/*      */   private static final int ColumnNameTag = 11;
/*      */   private static final int SchemaNameTag = 12;
/*      */   private static final int ColumnPrecisionTag = 13;
/*      */   private static final int ColumnScaleTag = 14;
/*      */   private static final int MetaTableNameTag = 15;
/*      */   private static final int CatalogNameTag = 16;
/*      */   private static final int ColumnTypeTag = 17;
/*      */   private static final int ColumnTypeNameTag = 18;
/*      */   private static final int MetaNullTag = 19;
/*  373 */   private String[] data = { "currentRow", "columnValue", "insertRow", "deleteRow", "insdel", "updateRow", "null", "emptyString" };
/*      */   private static final int RowTag = 0;
/*      */   private static final int ColTag = 1;
/*      */   private static final int InsTag = 2;
/*      */   private static final int DelTag = 3;
/*      */   private static final int InsDelTag = 4;
/*      */   private static final int UpdTag = 5;
/*      */   private static final int NullTag = 6;
/*      */   private static final int EmptyStringTag = 7;
/*      */   private static final int INITIAL = 0;
/*      */   private static final int PROPERTIES = 1;
/*      */   private static final int METADATA = 2;
/*      */   private static final int DATA = 3;
/*      */   private JdbcRowSetResourceBundle resBundle;
/*      */ 
/*      */   public XmlReaderContentHandler(RowSet paramRowSet)
/*      */   {
/*  435 */     this.rs = ((WebRowSetImpl)paramRowSet);
/*      */ 
/*  438 */     initMaps();
/*      */ 
/*  441 */     this.updates = new Vector();
/*      */ 
/*  444 */     this.columnValue = "";
/*  445 */     this.propertyValue = "";
/*  446 */     this.metaDataValue = "";
/*      */ 
/*  448 */     this.nullVal = false;
/*  449 */     this.idx = 0;
/*  450 */     this.tempStr = "";
/*  451 */     this.tempUpdate = "";
/*  452 */     this.tempCommand = "";
/*      */     try
/*      */     {
/*  455 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*      */     } catch (IOException localIOException) {
/*  457 */       throw new RuntimeException(localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initMaps()
/*      */   {
/*  480 */     this.propMap = new HashMap();
/*  481 */     int i = this.properties.length;
/*      */ 
/*  483 */     for (int j = 0; j < i; j++) {
/*  484 */       this.propMap.put(this.properties[j], Integer.valueOf(j));
/*      */     }
/*      */ 
/*  487 */     this.colDefMap = new HashMap();
/*  488 */     i = this.colDef.length;
/*      */ 
/*  490 */     for (j = 0; j < i; j++) {
/*  491 */       this.colDefMap.put(this.colDef[j], Integer.valueOf(j));
/*      */     }
/*      */ 
/*  494 */     this.dataMap = new HashMap();
/*  495 */     i = this.data.length;
/*      */ 
/*  497 */     for (j = 0; j < i; j++) {
/*  498 */       this.dataMap.put(this.data[j], Integer.valueOf(j));
/*      */     }
/*      */ 
/*  502 */     this.typeMap = new HashMap();
/*      */   }
/*      */ 
/*      */   public void startDocument()
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endDocument()
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
/*      */     throws SAXException
/*      */   {
/*  544 */     String str = "";
/*      */ 
/*  546 */     str = paramString2;
/*      */     int i;
/*  548 */     switch (getState())
/*      */     {
/*      */     case 1:
/*  551 */       this.tempCommand = "";
/*  552 */       i = ((Integer)this.propMap.get(str)).intValue();
/*  553 */       if (i == 17)
/*  554 */         setNullValue(true);
/*      */       else
/*  556 */         setTag(i);
/*  557 */       break;
/*      */     case 2:
/*  559 */       i = ((Integer)this.colDefMap.get(str)).intValue();
/*      */ 
/*  561 */       if (i == 19)
/*  562 */         setNullValue(true);
/*      */       else
/*  564 */         setTag(i);
/*  565 */       break;
/*      */     case 3:
/*  572 */       this.tempStr = "";
/*  573 */       this.tempUpdate = "";
/*  574 */       if (this.dataMap.get(str) == null)
/*  575 */         i = 6;
/*  576 */       else if (((Integer)this.dataMap.get(str)).intValue() == 7)
/*  577 */         i = 7;
/*      */       else {
/*  579 */         i = ((Integer)this.dataMap.get(str)).intValue();
/*      */       }
/*      */ 
/*  582 */       if (i == 6) {
/*  583 */         setNullValue(true);
/*  584 */       } else if (i == 7) {
/*  585 */         setEmptyStringValue(true);
/*      */       } else {
/*  587 */         setTag(i);
/*      */ 
/*  589 */         if ((i == 0) || (i == 3) || (i == 2)) {
/*  590 */           this.idx = 0;
/*      */           try {
/*  592 */             this.rs.moveToInsertRow();
/*      */           }
/*      */           catch (SQLException localSQLException)
/*      */           {
/*      */           }
/*      */         }
/*      */       }
/*      */       break;
/*      */     default:
/*  601 */       setState(str);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endElement(String paramString1, String paramString2, String paramString3)
/*      */     throws SAXException
/*      */   {
/*  636 */     String str = "";
/*  637 */     str = paramString2;
/*      */     int i;
/*  639 */     switch (getState()) {
/*      */     case 1:
/*  641 */       if (str.equals("properties")) {
/*  642 */         this.state = 0;
/*      */       }
/*      */       else
/*      */       {
/*      */         try {
/*  647 */           i = ((Integer)this.propMap.get(str)).intValue();
/*  648 */           switch (i) {
/*      */           case 7:
/*  650 */             if (this.keyCols != null) {
/*  651 */               int[] arrayOfInt = new int[this.keyCols.size()];
/*  652 */               for (int j = 0; j < arrayOfInt.length; j++)
/*  653 */                 arrayOfInt[j] = Integer.parseInt((String)this.keyCols.elementAt(j));
/*  654 */               this.rs.setKeyColumns(arrayOfInt);
/*  655 */             }break;
/*      */           case 20:
/*      */             try
/*      */             {
/*  662 */               this.typeMap.put(this.Key_map, ReflectUtil.forName(this.Value_map));
/*      */             }
/*      */             catch (ClassNotFoundException localClassNotFoundException) {
/*  665 */               throw new SAXException(MessageFormat.format(this.resBundle.handleGetObject("xmlrch.errmap").toString(), new Object[] { localClassNotFoundException.getMessage() }));
/*      */             }
/*      */ 
/*      */           case 8:
/*  671 */             this.rs.setTypeMap(this.typeMap);
/*  672 */             break;
/*      */           }
/*      */ 
/*  678 */           if (getNullValue()) {
/*  679 */             setPropertyValue(null);
/*  680 */             setNullValue(false);
/*      */           } else {
/*  682 */             setPropertyValue(this.propertyValue);
/*      */           }
/*      */         } catch (SQLException localSQLException1) {
/*  685 */           throw new SAXException(localSQLException1.getMessage());
/*      */         }
/*      */ 
/*  689 */         this.propertyValue = "";
/*  690 */         setTag(-1);
/*  691 */       }break;
/*      */     case 2:
/*  693 */       if (str.equals("metadata")) {
/*      */         try {
/*  695 */           this.rs.setMetaData(this.md);
/*  696 */           this.state = 0;
/*      */         } catch (SQLException localSQLException2) {
/*  698 */           throw new SAXException(MessageFormat.format(this.resBundle.handleGetObject("xmlrch.errmetadata").toString(), new Object[] { localSQLException2.getMessage() }));
/*      */         }
/*      */       } else {
/*      */         try {
/*  702 */           if (getNullValue()) {
/*  703 */             setMetaDataValue(null);
/*  704 */             setNullValue(false);
/*      */           } else {
/*  706 */             setMetaDataValue(this.metaDataValue);
/*      */           }
/*      */         } catch (SQLException localSQLException3) {
/*  709 */           throw new SAXException(MessageFormat.format(this.resBundle.handleGetObject("xmlrch.errmetadata").toString(), new Object[] { localSQLException3.getMessage() }));
/*      */         }
/*      */ 
/*  713 */         this.metaDataValue = "";
/*      */       }
/*  715 */       setTag(-1);
/*  716 */       break;
/*      */     case 3:
/*  718 */       if (str.equals("data")) {
/*  719 */         this.state = 0;
/*  720 */         return;
/*      */       }
/*      */ 
/*  723 */       if (this.dataMap.get(str) == null)
/*  724 */         i = 6;
/*      */       else {
/*  726 */         i = ((Integer)this.dataMap.get(str)).intValue();
/*      */       }
/*  728 */       switch (i) {
/*      */       case 1:
/*      */         try {
/*  731 */           this.idx += 1;
/*  732 */           if (getNullValue()) {
/*  733 */             insertValue(null);
/*  734 */             setNullValue(false);
/*      */           } else {
/*  736 */             insertValue(this.tempStr);
/*      */           }
/*      */ 
/*  739 */           this.columnValue = "";
/*      */         } catch (SQLException localSQLException4) {
/*  741 */           throw new SAXException(MessageFormat.format(this.resBundle.handleGetObject("xmlrch.errinsertval").toString(), new Object[] { localSQLException4.getMessage() }));
/*      */         }
/*      */       case 0:
/*      */         try
/*      */         {
/*  746 */           this.rs.insertRow();
/*  747 */           this.rs.moveToCurrentRow();
/*  748 */           this.rs.next();
/*      */ 
/*  752 */           this.rs.setOriginalRow();
/*      */ 
/*  754 */           applyUpdates();
/*      */         } catch (SQLException localSQLException5) {
/*  756 */           throw new SAXException(MessageFormat.format(this.resBundle.handleGetObject("xmlrch.errconstr").toString(), new Object[] { localSQLException5.getMessage() }));
/*      */         }
/*      */       case 3:
/*      */         try
/*      */         {
/*  761 */           this.rs.insertRow();
/*  762 */           this.rs.moveToCurrentRow();
/*  763 */           this.rs.next();
/*  764 */           this.rs.setOriginalRow();
/*  765 */           applyUpdates();
/*      */         } catch (SQLException localSQLException6) {
/*  767 */           throw new SAXException(MessageFormat.format(this.resBundle.handleGetObject("xmlrch.errdel").toString(), new Object[] { localSQLException6.getMessage() }));
/*      */         }
/*      */       case 2:
/*      */         try
/*      */         {
/*  772 */           this.rs.insertRow();
/*  773 */           this.rs.moveToCurrentRow();
/*  774 */           this.rs.next();
/*  775 */           applyUpdates();
/*      */         } catch (SQLException localSQLException7) {
/*  777 */           throw new SAXException(MessageFormat.format(this.resBundle.handleGetObject("xmlrch.errinsert").toString(), new Object[] { localSQLException7.getMessage() }));
/*      */         }
/*      */ 
/*      */       case 4:
/*      */         try
/*      */         {
/*  783 */           this.rs.insertRow();
/*  784 */           this.rs.moveToCurrentRow();
/*  785 */           this.rs.next();
/*  786 */           this.rs.setOriginalRow();
/*  787 */           applyUpdates();
/*      */         } catch (SQLException localSQLException8) {
/*  789 */           throw new SAXException(MessageFormat.format(this.resBundle.handleGetObject("xmlrch.errinsdel").toString(), new Object[] { localSQLException8.getMessage() }));
/*      */         }
/*      */ 
/*      */       case 5:
/*      */         try
/*      */         {
/*  795 */           if (getNullValue())
/*      */           {
/*  797 */             insertValue(null);
/*  798 */             setNullValue(false);
/*  799 */           } else if (getEmptyStringValue()) {
/*  800 */             insertValue("");
/*  801 */             setEmptyStringValue(false);
/*      */           } else {
/*  803 */             this.updates.add(this.upd);
/*      */           }
/*      */         } catch (SQLException localSQLException9) {
/*  806 */           throw new SAXException(MessageFormat.format(this.resBundle.handleGetObject("xmlrch.errupdate").toString(), new Object[] { localSQLException9.getMessage() }));
/*      */         }
/*      */       }
/*      */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void applyUpdates()
/*      */     throws SAXException
/*      */   {
/*  820 */     if (this.updates.size() > 0)
/*      */     {
/*      */       try {
/*  823 */         Iterator localIterator = this.updates.iterator();
/*  824 */         while (localIterator.hasNext()) {
/*  825 */           Object[] arrayOfObject = (Object[])localIterator.next();
/*  826 */           this.idx = ((Integer)arrayOfObject[0]).intValue();
/*      */ 
/*  828 */           if (!this.lastval.equals(arrayOfObject[1])) {
/*  829 */             insertValue((String)arrayOfObject[1]);
/*      */           }
/*      */         }
/*      */ 
/*  833 */         this.rs.updateRow();
/*      */       } catch (SQLException localSQLException) {
/*  835 */         throw new SAXException(MessageFormat.format(this.resBundle.handleGetObject("xmlrch.errupdrow").toString(), new Object[] { localSQLException.getMessage() }));
/*      */       }
/*  837 */       this.updates.removeAllElements();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  861 */       switch (getState()) {
/*      */       case 1:
/*  863 */         this.propertyValue = new String(paramArrayOfChar, paramInt1, paramInt2);
/*      */ 
/*  872 */         this.tempCommand = this.tempCommand.concat(this.propertyValue);
/*  873 */         this.propertyValue = this.tempCommand;
/*      */ 
/*  876 */         if (this.tag == 19)
/*      */         {
/*  878 */           this.Key_map = this.propertyValue;
/*      */         }
/*  882 */         else if (this.tag == 20)
/*      */         {
/*  884 */           this.Value_map = this.propertyValue; } break;
/*      */       case 2:
/*  895 */         if (this.tag != -1)
/*      */         {
/*  900 */           this.metaDataValue = new String(paramArrayOfChar, paramInt1, paramInt2);
/*  901 */         }break;
/*      */       case 3:
/*  903 */         setDataValue(paramArrayOfChar, paramInt1, paramInt2);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*  909 */       throw new SAXException(this.resBundle.handleGetObject("xmlrch.chars").toString() + localSQLException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setState(String paramString) throws SAXException {
/*  914 */     if (paramString.equals("webRowSet"))
/*  915 */       this.state = 0;
/*  916 */     else if (paramString.equals("properties")) {
/*  917 */       if (this.state != 1)
/*  918 */         this.state = 1;
/*      */       else
/*  920 */         this.state = 0;
/*  921 */     } else if (paramString.equals("metadata")) {
/*  922 */       if (this.state != 2)
/*  923 */         this.state = 2;
/*      */       else
/*  925 */         this.state = 0;
/*  926 */     } else if (paramString.equals("data"))
/*  927 */       if (this.state != 3)
/*  928 */         this.state = 3;
/*      */       else
/*  930 */         this.state = 0;
/*      */   }
/*      */ 
/*      */   private int getState()
/*      */   {
/*  947 */     return this.state;
/*      */   }
/*      */ 
/*      */   private void setTag(int paramInt) {
/*  951 */     this.tag = paramInt;
/*      */   }
/*      */ 
/*      */   private int getTag() {
/*  955 */     return this.tag;
/*      */   }
/*      */ 
/*      */   private void setNullValue(boolean paramBoolean) {
/*  959 */     this.nullVal = paramBoolean;
/*      */   }
/*      */ 
/*      */   private boolean getNullValue() {
/*  963 */     return this.nullVal;
/*      */   }
/*      */ 
/*      */   private void setEmptyStringValue(boolean paramBoolean) {
/*  967 */     this.emptyStringVal = paramBoolean;
/*      */   }
/*      */ 
/*      */   private boolean getEmptyStringValue() {
/*  971 */     return this.emptyStringVal;
/*      */   }
/*      */ 
/*      */   private String getStringValue(String paramString) {
/*  975 */     return paramString;
/*      */   }
/*      */ 
/*      */   private int getIntegerValue(String paramString) {
/*  979 */     return Integer.parseInt(paramString);
/*      */   }
/*      */ 
/*      */   private boolean getBooleanValue(String paramString)
/*      */   {
/*  984 */     return Boolean.valueOf(paramString).booleanValue();
/*      */   }
/*      */ 
/*      */   private BigDecimal getBigDecimalValue(String paramString) {
/*  988 */     return new BigDecimal(paramString);
/*      */   }
/*      */ 
/*      */   private byte getByteValue(String paramString) {
/*  992 */     return Byte.parseByte(paramString);
/*      */   }
/*      */ 
/*      */   private short getShortValue(String paramString) {
/*  996 */     return Short.parseShort(paramString);
/*      */   }
/*      */ 
/*      */   private long getLongValue(String paramString) {
/* 1000 */     return Long.parseLong(paramString);
/*      */   }
/*      */ 
/*      */   private float getFloatValue(String paramString) {
/* 1004 */     return Float.parseFloat(paramString);
/*      */   }
/*      */ 
/*      */   private double getDoubleValue(String paramString) {
/* 1008 */     return Double.parseDouble(paramString);
/*      */   }
/*      */ 
/*      */   private byte[] getBinaryValue(String paramString) {
/* 1012 */     return paramString.getBytes();
/*      */   }
/*      */ 
/*      */   private Date getDateValue(String paramString) {
/* 1016 */     return new Date(getLongValue(paramString));
/*      */   }
/*      */ 
/*      */   private Time getTimeValue(String paramString) {
/* 1020 */     return new Time(getLongValue(paramString));
/*      */   }
/*      */ 
/*      */   private Timestamp getTimestampValue(String paramString) {
/* 1024 */     return new Timestamp(getLongValue(paramString));
/*      */   }
/*      */ 
/*      */   private void setPropertyValue(String paramString) throws SQLException
/*      */   {
/* 1029 */     boolean bool = getNullValue();
/*      */     String str;
/* 1031 */     switch (getTag()) {
/*      */     case 0:
/* 1033 */       if (!bool)
/*      */       {
/* 1036 */         this.rs.setCommand(paramString);
/* 1037 */       }break;
/*      */     case 1:
/* 1039 */       if (bool) {
/* 1040 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
/*      */       }
/* 1042 */       this.rs.setConcurrency(getIntegerValue(paramString));
/* 1043 */       break;
/*      */     case 2:
/* 1045 */       if (bool)
/* 1046 */         this.rs.setDataSourceName(null);
/*      */       else
/* 1048 */         this.rs.setDataSourceName(paramString);
/* 1049 */       break;
/*      */     case 3:
/* 1051 */       if (bool) {
/* 1052 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
/*      */       }
/* 1054 */       this.rs.setEscapeProcessing(getBooleanValue(paramString));
/* 1055 */       break;
/*      */     case 4:
/* 1057 */       if (bool) {
/* 1058 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
/*      */       }
/* 1060 */       this.rs.setFetchDirection(getIntegerValue(paramString));
/* 1061 */       break;
/*      */     case 5:
/* 1063 */       if (bool) {
/* 1064 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
/*      */       }
/* 1066 */       this.rs.setFetchSize(getIntegerValue(paramString));
/* 1067 */       break;
/*      */     case 6:
/* 1069 */       if (bool) {
/* 1070 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
/*      */       }
/* 1072 */       this.rs.setTransactionIsolation(getIntegerValue(paramString));
/* 1073 */       break;
/*      */     case 7:
/* 1075 */       break;
/*      */     case 18:
/* 1077 */       if (this.keyCols == null)
/* 1078 */         this.keyCols = new Vector();
/* 1079 */       this.keyCols.add(paramString);
/* 1080 */       break;
/*      */     case 8:
/* 1082 */       break;
/*      */     case 9:
/* 1084 */       if (bool) {
/* 1085 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
/*      */       }
/* 1087 */       this.rs.setMaxFieldSize(getIntegerValue(paramString));
/* 1088 */       break;
/*      */     case 10:
/* 1090 */       if (bool) {
/* 1091 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
/*      */       }
/* 1093 */       this.rs.setMaxRows(getIntegerValue(paramString));
/* 1094 */       break;
/*      */     case 11:
/* 1096 */       if (bool) {
/* 1097 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
/*      */       }
/* 1099 */       this.rs.setQueryTimeout(getIntegerValue(paramString));
/* 1100 */       break;
/*      */     case 12:
/* 1102 */       if (bool) {
/* 1103 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
/*      */       }
/* 1105 */       this.rs.setReadOnly(getBooleanValue(paramString));
/* 1106 */       break;
/*      */     case 13:
/* 1108 */       if (bool) {
/* 1109 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
/*      */       }
/*      */ 
/* 1112 */       str = getStringValue(paramString);
/* 1113 */       int i = 0;
/*      */ 
/* 1115 */       if (str.trim().equals("ResultSet.TYPE_SCROLL_INSENSITIVE"))
/* 1116 */         i = 1004;
/* 1117 */       else if (str.trim().equals("ResultSet.TYPE_SCROLL_SENSITIVE"))
/* 1118 */         i = 1005;
/* 1119 */       else if (str.trim().equals("ResultSet.TYPE_FORWARD_ONLY")) {
/* 1120 */         i = 1003;
/*      */       }
/* 1122 */       this.rs.setType(i);
/*      */ 
/* 1124 */       break;
/*      */     case 14:
/* 1126 */       if (bool) {
/* 1127 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
/*      */       }
/* 1129 */       this.rs.setShowDeleted(getBooleanValue(paramString));
/* 1130 */       break;
/*      */     case 15:
/* 1132 */       if (!bool)
/*      */       {
/* 1136 */         this.rs.setTableName(paramString);
/* 1137 */       }break;
/*      */     case 16:
/* 1139 */       if (bool)
/* 1140 */         this.rs.setUrl(null);
/*      */       else
/* 1142 */         this.rs.setUrl(paramString);
/* 1143 */       break;
/*      */     case 22:
/* 1145 */       if (bool) {
/* 1146 */         this.rs.setSyncProvider(null);
/*      */       } else {
/* 1148 */         str = paramString.substring(0, paramString.indexOf("@") + 1);
/* 1149 */         this.rs.setSyncProvider(str);
/*      */       }
/* 1151 */       break;
/*      */     case 23:
/* 1154 */       break;
/*      */     case 24:
/* 1157 */       break;
/*      */     case 25:
/* 1160 */       break;
/*      */     case 26:
/* 1163 */       break;
/*      */     case 17:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setMetaDataValue(String paramString) throws SQLException {
/* 1172 */     boolean bool = getNullValue();
/*      */ 
/* 1174 */     switch (getTag()) {
/*      */     case 0:
/* 1176 */       this.md = new RowSetMetaDataImpl();
/* 1177 */       this.idx = 0;
/*      */ 
/* 1179 */       if (bool) {
/* 1180 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
/*      */       }
/* 1182 */       this.md.setColumnCount(getIntegerValue(paramString));
/*      */ 
/* 1184 */       break;
/*      */     case 1:
/* 1186 */       break;
/*      */     case 2:
/* 1188 */       this.idx += 1;
/* 1189 */       break;
/*      */     case 3:
/* 1191 */       if (bool) {
/* 1192 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
/*      */       }
/* 1194 */       this.md.setAutoIncrement(this.idx, getBooleanValue(paramString));
/* 1195 */       break;
/*      */     case 4:
/* 1197 */       if (bool) {
/* 1198 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
/*      */       }
/* 1200 */       this.md.setCaseSensitive(this.idx, getBooleanValue(paramString));
/* 1201 */       break;
/*      */     case 5:
/* 1203 */       if (bool) {
/* 1204 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
/*      */       }
/* 1206 */       this.md.setCurrency(this.idx, getBooleanValue(paramString));
/* 1207 */       break;
/*      */     case 6:
/* 1209 */       if (bool) {
/* 1210 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
/*      */       }
/* 1212 */       this.md.setNullable(this.idx, getIntegerValue(paramString));
/* 1213 */       break;
/*      */     case 7:
/* 1215 */       if (bool) {
/* 1216 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
/*      */       }
/* 1218 */       this.md.setSigned(this.idx, getBooleanValue(paramString));
/* 1219 */       break;
/*      */     case 8:
/* 1221 */       if (bool) {
/* 1222 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
/*      */       }
/* 1224 */       this.md.setSearchable(this.idx, getBooleanValue(paramString));
/* 1225 */       break;
/*      */     case 9:
/* 1227 */       if (bool) {
/* 1228 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
/*      */       }
/* 1230 */       this.md.setColumnDisplaySize(this.idx, getIntegerValue(paramString));
/* 1231 */       break;
/*      */     case 10:
/* 1233 */       if (bool)
/* 1234 */         this.md.setColumnLabel(this.idx, null);
/*      */       else
/* 1236 */         this.md.setColumnLabel(this.idx, paramString);
/* 1237 */       break;
/*      */     case 11:
/* 1239 */       if (bool)
/* 1240 */         this.md.setColumnName(this.idx, null);
/*      */       else {
/* 1242 */         this.md.setColumnName(this.idx, paramString);
/*      */       }
/* 1244 */       break;
/*      */     case 12:
/* 1246 */       if (bool)
/* 1247 */         this.md.setSchemaName(this.idx, null);
/*      */       else
/* 1249 */         this.md.setSchemaName(this.idx, paramString);
/* 1250 */       break;
/*      */     case 13:
/* 1252 */       if (bool) {
/* 1253 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
/*      */       }
/* 1255 */       this.md.setPrecision(this.idx, getIntegerValue(paramString));
/* 1256 */       break;
/*      */     case 14:
/* 1258 */       if (bool) {
/* 1259 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
/*      */       }
/* 1261 */       this.md.setScale(this.idx, getIntegerValue(paramString));
/* 1262 */       break;
/*      */     case 15:
/* 1264 */       if (bool)
/* 1265 */         this.md.setTableName(this.idx, null);
/*      */       else
/* 1267 */         this.md.setTableName(this.idx, paramString);
/* 1268 */       break;
/*      */     case 16:
/* 1270 */       if (bool)
/* 1271 */         this.md.setCatalogName(this.idx, null);
/*      */       else
/* 1273 */         this.md.setCatalogName(this.idx, paramString);
/* 1274 */       break;
/*      */     case 17:
/* 1276 */       if (bool) {
/* 1277 */         throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
/*      */       }
/* 1279 */       this.md.setColumnType(this.idx, getIntegerValue(paramString));
/* 1280 */       break;
/*      */     case 18:
/* 1282 */       if (bool)
/* 1283 */         this.md.setColumnTypeName(this.idx, null);
/*      */       else
/* 1285 */         this.md.setColumnTypeName(this.idx, paramString);
/* 1286 */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setDataValue(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/* 1295 */     switch (getTag()) {
/*      */     case 1:
/* 1297 */       this.columnValue = new String(paramArrayOfChar, paramInt1, paramInt2);
/*      */ 
/* 1305 */       this.tempStr = this.tempStr.concat(this.columnValue);
/* 1306 */       break;
/*      */     case 5:
/* 1308 */       this.upd = new Object[2];
/*      */ 
/* 1318 */       this.tempUpdate = this.tempUpdate.concat(new String(paramArrayOfChar, paramInt1, paramInt2));
/* 1319 */       this.upd[0] = Integer.valueOf(this.idx);
/* 1320 */       this.upd[1] = this.tempUpdate;
/*      */ 
/* 1323 */       this.lastval = ((String)this.upd[1]);
/*      */ 
/* 1325 */       break;
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     }
/*      */   }
/*      */ 
/*      */   private void insertValue(String paramString) throws SQLException {
/* 1333 */     if (getNullValue()) {
/* 1334 */       this.rs.updateNull(this.idx);
/* 1335 */       return;
/*      */     }
/*      */ 
/* 1339 */     int i = this.rs.getMetaData().getColumnType(this.idx);
/* 1340 */     switch (i) {
/*      */     case -7:
/* 1342 */       this.rs.updateBoolean(this.idx, getBooleanValue(paramString));
/* 1343 */       break;
/*      */     case 16:
/* 1345 */       this.rs.updateBoolean(this.idx, getBooleanValue(paramString));
/* 1346 */       break;
/*      */     case -6:
/*      */     case 5:
/* 1349 */       this.rs.updateShort(this.idx, getShortValue(paramString));
/* 1350 */       break;
/*      */     case 4:
/* 1352 */       this.rs.updateInt(this.idx, getIntegerValue(paramString));
/* 1353 */       break;
/*      */     case -5:
/* 1355 */       this.rs.updateLong(this.idx, getLongValue(paramString));
/* 1356 */       break;
/*      */     case 6:
/*      */     case 7:
/* 1359 */       this.rs.updateFloat(this.idx, getFloatValue(paramString));
/* 1360 */       break;
/*      */     case 8:
/* 1362 */       this.rs.updateDouble(this.idx, getDoubleValue(paramString));
/* 1363 */       break;
/*      */     case 2:
/*      */     case 3:
/* 1366 */       this.rs.updateObject(this.idx, getBigDecimalValue(paramString));
/* 1367 */       break;
/*      */     case -4:
/*      */     case -3:
/*      */     case -2:
/* 1371 */       this.rs.updateBytes(this.idx, getBinaryValue(paramString));
/* 1372 */       break;
/*      */     case 91:
/* 1374 */       this.rs.updateDate(this.idx, getDateValue(paramString));
/* 1375 */       break;
/*      */     case 92:
/* 1377 */       this.rs.updateTime(this.idx, getTimeValue(paramString));
/* 1378 */       break;
/*      */     case 93:
/* 1380 */       this.rs.updateTimestamp(this.idx, getTimestampValue(paramString));
/* 1381 */       break;
/*      */     case -1:
/*      */     case 1:
/*      */     case 12:
/* 1385 */       this.rs.updateString(this.idx, getStringValue(paramString));
/* 1386 */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void error(SAXParseException paramSAXParseException)
/*      */     throws SAXParseException
/*      */   {
/* 1401 */     throw paramSAXParseException;
/*      */   }
/*      */ 
/*      */   public void warning(SAXParseException paramSAXParseException)
/*      */     throws SAXParseException
/*      */   {
/* 1413 */     System.out.println(MessageFormat.format(this.resBundle.handleGetObject("xmlrch.warning").toString(), new Object[] { paramSAXParseException.getMessage(), Integer.valueOf(paramSAXParseException.getLineNumber()), paramSAXParseException.getSystemId() }));
/*      */   }
/*      */ 
/*      */   public void notationDecl(String paramString1, String paramString2, String paramString3)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void unparsedEntityDecl(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */   {
/*      */   }
/*      */ 
/*      */   private Row getPresentRow(WebRowSetImpl paramWebRowSetImpl)
/*      */     throws SQLException
/*      */   {
/* 1446 */     return null;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.internal.XmlReaderContentHandler
 * JD-Core Version:    0.6.2
 */