/*     */ package sun.jdbc.odbc;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class JdbcOdbcBoundCol extends JdbcOdbcObject
/*     */ {
/*     */   protected int type;
/*     */   protected int len;
/*     */   protected JdbcOdbcInputStream inputStream;
/*     */   protected boolean isRenamed;
/*     */   protected String aliasName;
/*     */   protected int rowSetSize;
/*     */   protected Object colObj;
/*     */   protected Object[] columnWiseData;
/*     */   protected byte[] columnWiseLength;
/*     */   protected byte[] binaryData;
/*     */   protected int streamType;
/*     */   public static final short ASCII = 1;
/*     */   public static final short UNICODE = 2;
/*     */   public static final short BINARY = 3;
/* 410 */   protected long pA1 = 0L;
/* 411 */   protected long pA2 = 0L;
/* 412 */   protected long pB1 = 0L;
/* 413 */   protected long pB2 = 0L;
/* 414 */   protected long pC1 = 0L;
/* 415 */   protected long pC2 = 0L;
/* 416 */   protected long pS1 = 0L;
/* 417 */   protected long pS2 = 0L;
/*     */ 
/*     */   public JdbcOdbcBoundCol()
/*     */   {
/*  42 */     this.type = 9999;
/*  43 */     this.len = -1;
/*  44 */     this.isRenamed = false;
/*  45 */     this.aliasName = null;
/*     */   }
/*     */ 
/*     */   public void setInputStream(JdbcOdbcInputStream paramJdbcOdbcInputStream)
/*     */   {
/*  55 */     this.inputStream = paramJdbcOdbcInputStream;
/*     */   }
/*     */ 
/*     */   public void closeInputStream()
/*     */   {
/*  66 */     if (this.inputStream != null) {
/*  67 */       this.inputStream.invalidate();
/*  68 */       this.inputStream = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setType(int paramInt)
/*     */   {
/*  80 */     this.type = paramInt;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/*  90 */     return this.type;
/*     */   }
/*     */ 
/*     */   public void setLength(int paramInt)
/*     */   {
/* 101 */     this.len = paramInt;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 111 */     return this.len;
/*     */   }
/*     */ 
/*     */   public void setAliasName(String paramString)
/*     */   {
/* 122 */     this.aliasName = paramString;
/* 123 */     this.isRenamed = true;
/*     */   }
/*     */ 
/*     */   public String mapAliasName(String paramString)
/*     */   {
/* 133 */     if (this.isRenamed == true) {
/* 134 */       return this.aliasName;
/*     */     }
/* 136 */     return paramString;
/*     */   }
/*     */ 
/*     */   public void setColumnValue(Object paramObject, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 149 */       if ((this.type == -1) || (this.type == -4))
/*     */       {
/* 151 */         if ((InputStream)paramObject != null)
/* 152 */           setInputStream((JdbcOdbcInputStream)paramObject);
/*     */         else
/* 154 */           this.colObj = paramObject;
/*     */       }
/*     */       else {
/* 157 */         this.colObj = paramObject;
/*     */       }
/* 159 */       setLength(paramInt);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getColumnValue()
/*     */   {
/* 174 */     if ((this.type == -1) || (this.type == -4))
/*     */     {
/* 176 */       if (this.inputStream != null) {
/* 177 */         return this.inputStream;
/*     */       }
/* 179 */       return this.colObj;
/*     */     }
/*     */ 
/* 182 */     return this.colObj;
/*     */   }
/*     */ 
/*     */   public JdbcOdbcInputStream getInputStream()
/*     */   {
/* 193 */     return this.inputStream;
/*     */   }
/*     */ 
/*     */   public void initStagingArea(int paramInt)
/*     */   {
/* 204 */     this.rowSetSize = paramInt;
/*     */ 
/* 206 */     this.columnWiseData = new Object[this.rowSetSize + 1];
/*     */ 
/* 209 */     this.columnWiseLength = new byte[(this.rowSetSize + 1) * JdbcOdbcPlatform.getLengthBufferSize()];
/*     */ 
/* 219 */     byte[] arrayOfByte = JdbcOdbcPlatform.convertIntToByteArray(-6);
/*     */ 
/* 221 */     for (int i = 0; i < (this.rowSetSize + 1) * arrayOfByte.length; i += arrayOfByte.length)
/* 222 */       for (int j = 0; j < arrayOfByte.length; j++)
/* 223 */         this.columnWiseLength[(i + j)] = arrayOfByte[j];
/*     */   }
/*     */ 
/*     */   public void resetColumnToIgnoreData()
/*     */   {
/* 245 */     byte[] arrayOfByte = JdbcOdbcPlatform.convertIntToByteArray(-6);
/*     */ 
/* 247 */     for (int i = 0; i < (this.rowSetSize + 1) * arrayOfByte.length; i += arrayOfByte.length)
/* 248 */       for (int j = 0; j < arrayOfByte.length; j++)
/* 249 */         this.columnWiseLength[(i + j)] = arrayOfByte[j];
/*     */   }
/*     */ 
/*     */   public void setRowValues(int paramInt1, Object paramObject, int paramInt2)
/*     */   {
/* 262 */     this.columnWiseData[paramInt1] = paramObject;
/*     */ 
/* 275 */     byte[] arrayOfByte = JdbcOdbcPlatform.convertIntToByteArray(paramInt2);
/*     */ 
/* 277 */     int i = paramInt1 * arrayOfByte.length;
/* 278 */     for (int j = i; j < i + arrayOfByte.length; j++)
/* 279 */       this.columnWiseLength[j] = arrayOfByte[(j - i)];
/*     */   }
/*     */ 
/*     */   public Object getRowValue(int paramInt)
/*     */   {
/* 290 */     return this.columnWiseData[paramInt];
/*     */   }
/*     */ 
/*     */   public int getRowLenInd(int paramInt)
/*     */   {
/* 300 */     return this.columnWiseLength[paramInt];
/*     */   }
/*     */ 
/*     */   public Object[] getRowValues()
/*     */   {
/* 310 */     return this.columnWiseData;
/*     */   }
/*     */ 
/*     */   public byte[] getRowLengths()
/*     */   {
/* 321 */     return this.columnWiseLength;
/*     */   }
/*     */ 
/*     */   public byte[] allocBindDataBuffer(int paramInt)
/*     */   {
/* 332 */     this.binaryData = new byte[paramInt];
/*     */ 
/* 337 */     return this.binaryData;
/*     */   }
/*     */ 
/*     */   public void setStreamType(int paramInt)
/*     */   {
/* 348 */     this.streamType = paramInt;
/*     */   }
/*     */ 
/*     */   public int getStreamType()
/*     */   {
/* 358 */     return this.streamType;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcBoundCol
 * JD-Core Version:    0.6.2
 */