/*     */ package sun.text.normalizer;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ final class UCharacterPropertyReader
/*     */   implements ICUBinary.Authenticate
/*     */ {
/*     */   private static final int INDEX_SIZE_ = 16;
/*     */   private DataInputStream m_dataInputStream_;
/*     */   private int m_propertyOffset_;
/*     */   private int m_exceptionOffset_;
/*     */   private int m_caseOffset_;
/*     */   private int m_additionalOffset_;
/*     */   private int m_additionalVectorsOffset_;
/*     */   private int m_additionalColumnsCount_;
/*     */   private int m_reservedOffset_;
/*     */   private byte[] m_unicodeVersion_;
/* 181 */   private static final byte[] DATA_FORMAT_ID_ = { 85, 80, 114, 111 };
/*     */ 
/* 187 */   private static final byte[] DATA_FORMAT_VERSION_ = { 5, 0, 5, 2 };
/*     */ 
/*     */   public boolean isDataVersionAcceptable(byte[] paramArrayOfByte)
/*     */   {
/*  65 */     return (paramArrayOfByte[0] == DATA_FORMAT_VERSION_[0]) && (paramArrayOfByte[2] == DATA_FORMAT_VERSION_[2]) && (paramArrayOfByte[3] == DATA_FORMAT_VERSION_[3]);
/*     */   }
/*     */ 
/*     */   protected UCharacterPropertyReader(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  80 */     this.m_unicodeVersion_ = ICUBinary.readHeader(paramInputStream, DATA_FORMAT_ID_, this);
/*     */ 
/*  82 */     this.m_dataInputStream_ = new DataInputStream(paramInputStream);
/*     */   }
/*     */ 
/*     */   protected void read(UCharacterProperty paramUCharacterProperty)
/*     */     throws IOException
/*     */   {
/*  96 */     int i = 16;
/*  97 */     this.m_propertyOffset_ = this.m_dataInputStream_.readInt();
/*  98 */     i--;
/*  99 */     this.m_exceptionOffset_ = this.m_dataInputStream_.readInt();
/* 100 */     i--;
/* 101 */     this.m_caseOffset_ = this.m_dataInputStream_.readInt();
/* 102 */     i--;
/* 103 */     this.m_additionalOffset_ = this.m_dataInputStream_.readInt();
/* 104 */     i--;
/* 105 */     this.m_additionalVectorsOffset_ = this.m_dataInputStream_.readInt();
/* 106 */     i--;
/* 107 */     this.m_additionalColumnsCount_ = this.m_dataInputStream_.readInt();
/* 108 */     i--;
/* 109 */     this.m_reservedOffset_ = this.m_dataInputStream_.readInt();
/* 110 */     i--;
/* 111 */     this.m_dataInputStream_.skipBytes(12);
/* 112 */     i -= 3;
/* 113 */     paramUCharacterProperty.m_maxBlockScriptValue_ = this.m_dataInputStream_.readInt();
/* 114 */     i--;
/* 115 */     paramUCharacterProperty.m_maxJTGValue_ = this.m_dataInputStream_.readInt();
/* 116 */     i--;
/* 117 */     this.m_dataInputStream_.skipBytes(i << 2);
/*     */ 
/* 121 */     paramUCharacterProperty.m_trie_ = new CharTrie(this.m_dataInputStream_, null);
/*     */ 
/* 124 */     int j = this.m_exceptionOffset_ - this.m_propertyOffset_;
/* 125 */     this.m_dataInputStream_.skipBytes(j * 4);
/*     */ 
/* 128 */     j = this.m_caseOffset_ - this.m_exceptionOffset_;
/* 129 */     this.m_dataInputStream_.skipBytes(j * 4);
/*     */ 
/* 132 */     j = this.m_additionalOffset_ - this.m_caseOffset_ << 1;
/* 133 */     this.m_dataInputStream_.skipBytes(j * 2);
/*     */ 
/* 135 */     if (this.m_additionalColumnsCount_ > 0)
/*     */     {
/* 137 */       paramUCharacterProperty.m_additionalTrie_ = new CharTrie(this.m_dataInputStream_, null);
/*     */ 
/* 140 */       j = this.m_reservedOffset_ - this.m_additionalVectorsOffset_;
/* 141 */       paramUCharacterProperty.m_additionalVectors_ = new int[j];
/* 142 */       for (int k = 0; k < j; k++) {
/* 143 */         paramUCharacterProperty.m_additionalVectors_[k] = this.m_dataInputStream_.readInt();
/*     */       }
/*     */     }
/*     */ 
/* 147 */     this.m_dataInputStream_.close();
/* 148 */     paramUCharacterProperty.m_additionalColumnsCount_ = this.m_additionalColumnsCount_;
/* 149 */     paramUCharacterProperty.m_unicodeVersion_ = VersionInfo.getInstance(this.m_unicodeVersion_[0], this.m_unicodeVersion_[1], this.m_unicodeVersion_[2], this.m_unicodeVersion_[3]);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.UCharacterPropertyReader
 * JD-Core Version:    0.6.2
 */