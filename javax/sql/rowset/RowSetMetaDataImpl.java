/*      */ package javax.sql.rowset;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Field;
/*      */ import java.math.BigDecimal;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Date;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.sql.Types;
/*      */ import javax.sql.RowSetMetaData;
/*      */ 
/*      */ public class RowSetMetaDataImpl
/*      */   implements RowSetMetaData, Serializable
/*      */ {
/*      */   private int colCount;
/*      */   private ColInfo[] colInfo;
/*      */   static final long serialVersionUID = 6893806403181801867L;
/*      */ 
/*      */   private void checkColRange(int paramInt)
/*      */     throws SQLException
/*      */   {
/*   82 */     if ((paramInt <= 0) || (paramInt > this.colCount))
/*   83 */       throw new SQLException("Invalid column index :" + paramInt);
/*      */   }
/*      */ 
/*      */   private void checkColType(int paramInt)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  100 */       Types localTypes = Types.class;
/*  101 */       Field[] arrayOfField = localTypes.getFields();
/*  102 */       int i = 0;
/*  103 */       for (int j = 0; j < arrayOfField.length; j++) {
/*  104 */         i = arrayOfField[j].getInt(localTypes);
/*  105 */         if (i == paramInt)
/*  106 */           return;
/*      */       }
/*      */     }
/*      */     catch (Exception localException) {
/*  110 */       throw new SQLException(localException.getMessage());
/*      */     }
/*  112 */     throw new SQLException("Invalid SQL type for column");
/*      */   }
/*      */ 
/*      */   public void setColumnCount(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  125 */     if (paramInt <= 0) {
/*  126 */       throw new SQLException("Invalid column count. Cannot be less or equal to zero");
/*      */     }
/*      */ 
/*  130 */     this.colCount = paramInt;
/*      */ 
/*  140 */     if (this.colCount != 2147483647) {
/*  141 */       this.colInfo = new ColInfo[this.colCount + 1];
/*      */ 
/*  143 */       for (int i = 1; i <= this.colCount; i++)
/*  144 */         this.colInfo[i] = new ColInfo(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setAutoIncrement(int paramInt, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/*  166 */     checkColRange(paramInt);
/*  167 */     this.colInfo[paramInt].autoIncrement = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void setCaseSensitive(int paramInt, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/*  183 */     checkColRange(paramInt);
/*  184 */     this.colInfo[paramInt].caseSensitive = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void setSearchable(int paramInt, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/*  203 */     checkColRange(paramInt);
/*  204 */     this.colInfo[paramInt].searchable = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void setCurrency(int paramInt, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/*  220 */     checkColRange(paramInt);
/*  221 */     this.colInfo[paramInt].currency = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void setNullable(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  245 */     if ((paramInt2 < 0) || (paramInt2 > 2))
/*      */     {
/*  247 */       throw new SQLException("Invalid nullable constant set. Must be either columnNoNulls, columnNullable or columnNullableUnknown");
/*      */     }
/*      */ 
/*  250 */     checkColRange(paramInt1);
/*  251 */     this.colInfo[paramInt1].nullable = paramInt2;
/*      */   }
/*      */ 
/*      */   public void setSigned(int paramInt, boolean paramBoolean)
/*      */     throws SQLException
/*      */   {
/*  267 */     checkColRange(paramInt);
/*  268 */     this.colInfo[paramInt].signed = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void setColumnDisplaySize(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  284 */     if (paramInt2 < 0) {
/*  285 */       throw new SQLException("Invalid column display size. Cannot be less than zero");
/*      */     }
/*      */ 
/*  288 */     checkColRange(paramInt1);
/*  289 */     this.colInfo[paramInt1].columnDisplaySize = paramInt2;
/*      */   }
/*      */ 
/*      */   public void setColumnLabel(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/*  307 */     checkColRange(paramInt);
/*  308 */     if (paramString != null)
/*  309 */       this.colInfo[paramInt].columnLabel = paramString;
/*      */     else
/*  311 */       this.colInfo[paramInt].columnLabel = "";
/*      */   }
/*      */ 
/*      */   public void setColumnName(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/*  327 */     checkColRange(paramInt);
/*  328 */     if (paramString != null)
/*  329 */       this.colInfo[paramInt].columnName = paramString;
/*      */     else
/*  331 */       this.colInfo[paramInt].columnName = "";
/*      */   }
/*      */ 
/*      */   public void setSchemaName(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/*  349 */     checkColRange(paramInt);
/*  350 */     if (paramString != null)
/*  351 */       this.colInfo[paramInt].schemaName = paramString;
/*      */     else
/*  353 */       this.colInfo[paramInt].schemaName = "";
/*      */   }
/*      */ 
/*      */   public void setPrecision(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  371 */     if (paramInt2 < 0) {
/*  372 */       throw new SQLException("Invalid precision value. Cannot be less than zero");
/*      */     }
/*      */ 
/*  375 */     checkColRange(paramInt1);
/*  376 */     this.colInfo[paramInt1].colPrecision = paramInt2;
/*      */   }
/*      */ 
/*      */   public void setScale(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  392 */     if (paramInt2 < 0) {
/*  393 */       throw new SQLException("Invalid scale size. Cannot be less than zero");
/*      */     }
/*      */ 
/*  396 */     checkColRange(paramInt1);
/*  397 */     this.colInfo[paramInt1].colScale = paramInt2;
/*      */   }
/*      */ 
/*      */   public void setTableName(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/*  412 */     checkColRange(paramInt);
/*  413 */     if (paramString != null)
/*  414 */       this.colInfo[paramInt].tableName = paramString;
/*      */     else
/*  416 */       this.colInfo[paramInt].tableName = "";
/*      */   }
/*      */ 
/*      */   public void setCatalogName(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/*  433 */     checkColRange(paramInt);
/*  434 */     if (paramString != null)
/*  435 */       this.colInfo[paramInt].catName = paramString;
/*      */     else
/*  437 */       this.colInfo[paramInt].catName = "";
/*      */   }
/*      */ 
/*      */   public void setColumnType(int paramInt1, int paramInt2)
/*      */     throws SQLException
/*      */   {
/*  457 */     checkColType(paramInt2);
/*  458 */     checkColRange(paramInt1);
/*  459 */     this.colInfo[paramInt1].colType = paramInt2;
/*      */   }
/*      */ 
/*      */   public void setColumnTypeName(int paramInt, String paramString)
/*      */     throws SQLException
/*      */   {
/*  475 */     checkColRange(paramInt);
/*  476 */     if (paramString != null)
/*  477 */       this.colInfo[paramInt].colTypeName = paramString;
/*      */     else
/*  479 */       this.colInfo[paramInt].colTypeName = "";
/*      */   }
/*      */ 
/*      */   public int getColumnCount()
/*      */     throws SQLException
/*      */   {
/*  491 */     return this.colCount;
/*      */   }
/*      */ 
/*      */   public boolean isAutoIncrement(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  506 */     checkColRange(paramInt);
/*  507 */     return this.colInfo[paramInt].autoIncrement;
/*      */   }
/*      */ 
/*      */   public boolean isCaseSensitive(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  522 */     checkColRange(paramInt);
/*  523 */     return this.colInfo[paramInt].caseSensitive;
/*      */   }
/*      */ 
/*      */   public boolean isSearchable(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  538 */     checkColRange(paramInt);
/*  539 */     return this.colInfo[paramInt].searchable;
/*      */   }
/*      */ 
/*      */   public boolean isCurrency(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  554 */     checkColRange(paramInt);
/*  555 */     return this.colInfo[paramInt].currency;
/*      */   }
/*      */ 
/*      */   public int isNullable(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  572 */     checkColRange(paramInt);
/*  573 */     return this.colInfo[paramInt].nullable;
/*      */   }
/*      */ 
/*      */   public boolean isSigned(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  588 */     checkColRange(paramInt);
/*  589 */     return this.colInfo[paramInt].signed;
/*      */   }
/*      */ 
/*      */   public int getColumnDisplaySize(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  603 */     checkColRange(paramInt);
/*  604 */     return this.colInfo[paramInt].columnDisplaySize;
/*      */   }
/*      */ 
/*      */   public String getColumnLabel(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  618 */     checkColRange(paramInt);
/*  619 */     return this.colInfo[paramInt].columnLabel;
/*      */   }
/*      */ 
/*      */   public String getColumnName(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  632 */     checkColRange(paramInt);
/*  633 */     return this.colInfo[paramInt].columnName;
/*      */   }
/*      */ 
/*      */   public String getSchemaName(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  649 */     checkColRange(paramInt);
/*  650 */     String str = "";
/*  651 */     if (this.colInfo[paramInt].schemaName != null)
/*      */     {
/*  653 */       str = this.colInfo[paramInt].schemaName;
/*      */     }
/*  655 */     return str;
/*      */   }
/*      */ 
/*      */   public int getPrecision(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  669 */     checkColRange(paramInt);
/*  670 */     return this.colInfo[paramInt].colPrecision;
/*      */   }
/*      */ 
/*      */   public int getScale(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  684 */     checkColRange(paramInt);
/*  685 */     return this.colInfo[paramInt].colScale;
/*      */   }
/*      */ 
/*      */   public String getTableName(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  700 */     checkColRange(paramInt);
/*  701 */     return this.colInfo[paramInt].tableName;
/*      */   }
/*      */ 
/*      */   public String getCatalogName(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  716 */     checkColRange(paramInt);
/*  717 */     String str = "";
/*  718 */     if (this.colInfo[paramInt].catName != null)
/*      */     {
/*  720 */       str = this.colInfo[paramInt].catName;
/*      */     }
/*  722 */     return str;
/*      */   }
/*      */ 
/*      */   public int getColumnType(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  739 */     checkColRange(paramInt);
/*  740 */     return this.colInfo[paramInt].colType;
/*      */   }
/*      */ 
/*      */   public String getColumnTypeName(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  754 */     checkColRange(paramInt);
/*  755 */     return this.colInfo[paramInt].colTypeName;
/*      */   }
/*      */ 
/*      */   public boolean isReadOnly(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  771 */     checkColRange(paramInt);
/*  772 */     return this.colInfo[paramInt].readOnly;
/*      */   }
/*      */ 
/*      */   public boolean isWritable(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  789 */     checkColRange(paramInt);
/*  790 */     return this.colInfo[paramInt].writable;
/*      */   }
/*      */ 
/*      */   public boolean isDefinitelyWritable(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  805 */     return true;
/*      */   }
/*      */ 
/*      */   public String getColumnClassName(int paramInt)
/*      */     throws SQLException
/*      */   {
/*  830 */     String str = String.class.getName();
/*      */ 
/*  832 */     int i = getColumnType(paramInt);
/*      */ 
/*  834 */     switch (i)
/*      */     {
/*      */     case 2:
/*      */     case 3:
/*  838 */       str = BigDecimal.class.getName();
/*  839 */       break;
/*      */     case -7:
/*  842 */       str = Boolean.class.getName();
/*  843 */       break;
/*      */     case -6:
/*  846 */       str = Byte.class.getName();
/*  847 */       break;
/*      */     case 5:
/*  850 */       str = Short.class.getName();
/*  851 */       break;
/*      */     case 4:
/*  854 */       str = Integer.class.getName();
/*  855 */       break;
/*      */     case -5:
/*  858 */       str = Long.class.getName();
/*  859 */       break;
/*      */     case 7:
/*  862 */       str = Float.class.getName();
/*  863 */       break;
/*      */     case 6:
/*      */     case 8:
/*  867 */       str = Double.class.getName();
/*  868 */       break;
/*      */     case -4:
/*      */     case -3:
/*      */     case -2:
/*  873 */       str = "byte[]";
/*  874 */       break;
/*      */     case 91:
/*  877 */       str = Date.class.getName();
/*  878 */       break;
/*      */     case 92:
/*  881 */       str = Time.class.getName();
/*  882 */       break;
/*      */     case 93:
/*  885 */       str = Timestamp.class.getName();
/*  886 */       break;
/*      */     case 2004:
/*  889 */       str = Blob.class.getName();
/*  890 */       break;
/*      */     case 2005:
/*  893 */       str = Clob.class.getName();
/*      */     }
/*      */ 
/*  897 */     return str;
/*      */   }
/*      */ 
/*      */   public <T> T unwrap(Class<T> paramClass)
/*      */     throws SQLException
/*      */   {
/*  916 */     if (isWrapperFor(paramClass)) {
/*  917 */       return paramClass.cast(this);
/*      */     }
/*  919 */     throw new SQLException("unwrap failed for:" + paramClass);
/*      */   }
/*      */ 
/*      */   public boolean isWrapperFor(Class<?> paramClass)
/*      */     throws SQLException
/*      */   {
/*  939 */     return paramClass.isInstance(this);
/*      */   }
/*      */ 
/*      */   private class ColInfo
/*      */     implements Serializable
/*      */   {
/*      */     public boolean autoIncrement;
/*      */     public boolean caseSensitive;
/*      */     public boolean currency;
/*      */     public int nullable;
/*      */     public boolean signed;
/*      */     public boolean searchable;
/*      */     public int columnDisplaySize;
/*      */     public String columnLabel;
/*      */     public String columnName;
/*      */     public String schemaName;
/*      */     public int colPrecision;
/*      */     public int colScale;
/* 1054 */     public String tableName = "";
/*      */     public String catName;
/*      */     public int colType;
/*      */     public String colTypeName;
/* 1086 */     public boolean readOnly = false;
/*      */ 
/* 1093 */     public boolean writable = true;
/*      */ 
/*      */     private ColInfo()
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.RowSetMetaDataImpl
 * JD-Core Version:    0.6.2
 */