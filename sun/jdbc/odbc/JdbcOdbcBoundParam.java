/*     */ package sun.jdbc.odbc;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class JdbcOdbcBoundParam extends JdbcOdbcObject
/*     */ {
/*     */   protected byte[] binaryData;
/*     */   protected byte[] paramLength;
/*     */   protected InputStream paramInputStream;
/*     */   protected int paramInputStreamLen;
/*     */   protected int sqlType;
/*     */   protected int streamType;
/*     */   public static final short ASCII = 1;
/*     */   public static final short UNICODE = 2;
/*     */   public static final short BINARY = 3;
/*     */   protected boolean outputParameter;
/* 284 */   protected boolean inputParameter = false;
/* 285 */   protected int scale = 0;
/*     */ 
/* 288 */   protected long pA1 = 0L;
/* 289 */   protected long pA2 = 0L;
/* 290 */   protected long pB1 = 0L;
/* 291 */   protected long pB2 = 0L;
/* 292 */   protected long pC1 = 0L;
/* 293 */   protected long pC2 = 0L;
/* 294 */   protected long pS1 = 0L;
/* 295 */   protected long pS2 = 0L;
/*     */   protected int boundType;
/*     */   protected Object boundValue;
/*     */ 
/*     */   public void initialize()
/*     */   {
/*  45 */     this.paramLength = new byte[4];
/*     */   }
/*     */ 
/*     */   public byte[] allocBindDataBuffer(int paramInt)
/*     */   {
/*  61 */     if (this.binaryData == null) {
/*  62 */       this.binaryData = new byte[paramInt];
/*     */     }
/*     */     else
/*     */     {
/*  67 */       return getBindDataBuffer();
/*     */     }
/*  69 */     return this.binaryData;
/*     */   }
/*     */ 
/*     */   public byte[] getBindDataBuffer()
/*     */   {
/*  79 */     if (this.pA1 != 0L)
/*     */     {
/*  82 */       JdbcOdbc.ReleaseStoredBytes(this.pA1, this.pA2);
/*  83 */       this.pA1 = 0L;
/*  84 */       this.pA2 = 0L;
/*     */     }
/*  86 */     if (this.pB1 != 0L)
/*     */     {
/*  89 */       JdbcOdbc.ReleaseStoredBytes(this.pB1, this.pB2);
/*  90 */       this.pB1 = 0L;
/*  91 */       this.pB2 = 0L;
/*     */     }
/*  93 */     if (this.pC1 != 0L)
/*     */     {
/*  95 */       JdbcOdbc.ReleaseStoredBytes(this.pC1, this.pC2);
/*  96 */       this.pC1 = 0L;
/*  97 */       this.pC2 = 0L;
/*     */     }
/*  99 */     if (this.pS1 != 0L)
/*     */     {
/* 101 */       JdbcOdbc.ReleaseStoredChars(this.pS1, this.pS2);
/* 102 */       this.pS1 = 0L;
/* 103 */       this.pS2 = 0L;
/*     */     }
/* 105 */     return this.binaryData;
/*     */   }
/*     */ 
/*     */   public byte[] getBindLengthBuffer()
/*     */   {
/* 114 */     if (this.pA1 != 0L)
/*     */     {
/* 117 */       JdbcOdbc.ReleaseStoredBytes(this.pA1, this.pA2);
/* 118 */       this.pA1 = 0L;
/*     */     }
/* 120 */     if (this.pB1 != 0L)
/*     */     {
/* 123 */       JdbcOdbc.ReleaseStoredBytes(this.pB1, this.pB2);
/* 124 */       this.pB1 = 0L;
/*     */     }
/* 126 */     if (this.pC1 != 0L)
/*     */     {
/* 128 */       JdbcOdbc.ReleaseStoredBytes(this.pC1, this.pC2);
/* 129 */       this.pC1 = 0L;
/* 130 */       this.pC2 = 0L;
/*     */     }
/* 132 */     if (this.pS1 != 0L)
/*     */     {
/* 134 */       JdbcOdbc.ReleaseStoredChars(this.pS1, this.pS2);
/* 135 */       this.pS1 = 0L;
/* 136 */       this.pS2 = 0L;
/*     */     }
/* 138 */     return this.paramLength;
/*     */   }
/*     */ 
/*     */   public void resetBindDataBuffer(byte[] paramArrayOfByte)
/*     */   {
/* 143 */     this.binaryData = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public void setInputStream(InputStream paramInputStream1, int paramInt)
/*     */   {
/* 153 */     this.paramInputStream = paramInputStream1;
/* 154 */     this.paramInputStreamLen = paramInt;
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */   {
/* 163 */     return this.paramInputStream;
/*     */   }
/*     */ 
/*     */   public int getInputStreamLen()
/*     */   {
/* 172 */     return this.paramInputStreamLen;
/*     */   }
/*     */ 
/*     */   public void setSqlType(int paramInt)
/*     */   {
/* 183 */     this.sqlType = paramInt;
/*     */   }
/*     */ 
/*     */   public int getSqlType()
/*     */   {
/* 193 */     return this.sqlType;
/*     */   }
/*     */ 
/*     */   public void setStreamType(int paramInt)
/*     */   {
/* 204 */     this.streamType = paramInt;
/*     */   }
/*     */ 
/*     */   public int getStreamType()
/*     */   {
/* 214 */     return this.streamType;
/*     */   }
/*     */ 
/*     */   public void setOutputParameter(boolean paramBoolean)
/*     */   {
/* 225 */     this.outputParameter = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setInputParameter(boolean paramBoolean)
/*     */   {
/* 236 */     this.inputParameter = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isOutputParameter()
/*     */   {
/* 246 */     return this.outputParameter;
/*     */   }
/*     */ 
/*     */   public boolean isInOutParameter()
/*     */   {
/* 251 */     return (this.inputParameter) && (this.outputParameter);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcBoundParam
 * JD-Core Version:    0.6.2
 */