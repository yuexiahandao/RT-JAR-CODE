/*     */ package sun.io;
/*     */ 
/*     */ import java.io.CharConversionException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class CharToByteConverter
/*     */ {
/*  47 */   protected boolean subMode = true;
/*     */ 
/*  52 */   protected byte[] subBytes = { 63 };
/*     */   protected int charOff;
/*     */   protected int byteOff;
/*     */   protected int badInputLength;
/*     */ 
/*     */   public static CharToByteConverter getDefault()
/*     */   {
/*  74 */     Object localObject = Converters.newDefaultConverter(1);
/*  75 */     return (CharToByteConverter)localObject;
/*     */   }
/*     */ 
/*     */   public static CharToByteConverter getConverter(String paramString)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  86 */     Object localObject = Converters.newConverter(1, paramString);
/*  87 */     return (CharToByteConverter)localObject;
/*     */   }
/*     */ 
/*     */   public abstract String getCharacterEncoding();
/*     */ 
/*     */   public abstract int convert(char[] paramArrayOfChar, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4)
/*     */     throws MalformedInputException, UnknownCharacterException, ConversionBufferFullException;
/*     */ 
/*     */   public int convertAny(char[] paramArrayOfChar, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4)
/*     */     throws ConversionBufferFullException
/*     */   {
/* 151 */     if (!this.subMode) {
/* 152 */       throw new IllegalStateException("Substitution mode is not on");
/*     */     }
/*     */ 
/* 157 */     int i = paramInt1;
/* 158 */     int j = paramInt3;
/* 159 */     while (i < paramInt2) {
/*     */       try {
/* 161 */         int k = convert(paramArrayOfChar, i, paramInt2, paramArrayOfByte, j, paramInt4);
/*     */ 
/* 163 */         return nextByteIndex() - paramInt3;
/*     */       } catch (MalformedInputException localMalformedInputException) {
/* 165 */         byte[] arrayOfByte = this.subBytes;
/* 166 */         int m = arrayOfByte.length;
/* 167 */         j = nextByteIndex();
/* 168 */         if (j + m > paramInt4)
/* 169 */           throw new ConversionBufferFullException();
/* 170 */         for (int n = 0; n < m; n++)
/* 171 */           paramArrayOfByte[(j++)] = arrayOfByte[n];
/* 172 */         i = nextCharIndex();
/* 173 */         i += this.badInputLength;
/* 174 */         this.badInputLength = 0;
/* 175 */         if (i >= paramInt2) {
/* 176 */           this.byteOff = j;
/* 177 */           return this.byteOff - paramInt3;
/*     */         }
/*     */       }
/*     */       catch (UnknownCharacterException localUnknownCharacterException)
/*     */       {
/* 182 */         throw new Error("UnknownCharacterException thrown in substititution mode", localUnknownCharacterException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 187 */     return nextByteIndex() - paramInt3;
/*     */   }
/*     */ 
/*     */   public byte[] convertAll(char[] paramArrayOfChar)
/*     */     throws MalformedInputException
/*     */   {
/* 224 */     reset();
/* 225 */     boolean bool = this.subMode;
/* 226 */     this.subMode = true;
/*     */ 
/* 228 */     byte[] arrayOfByte1 = new byte[getMaxBytesPerChar() * paramArrayOfChar.length];
/*     */     try
/*     */     {
/* 231 */       int i = convert(paramArrayOfChar, 0, paramArrayOfChar.length, arrayOfByte1, 0, arrayOfByte1.length);
/*     */ 
/* 233 */       i += flush(arrayOfByte1, nextByteIndex(), arrayOfByte1.length);
/*     */ 
/* 235 */       byte[] arrayOfByte2 = new byte[i];
/* 236 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i);
/* 237 */       return arrayOfByte2;
/*     */     }
/*     */     catch (ConversionBufferFullException localConversionBufferFullException)
/*     */     {
/* 241 */       throw new InternalError("this.getMaxBytesPerChar returned bad value");
/*     */     }
/*     */     catch (UnknownCharacterException localUnknownCharacterException)
/*     */     {
/* 246 */       throw new InternalError();
/*     */     }
/*     */     finally {
/* 249 */       this.subMode = bool;
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract int flush(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws MalformedInputException, ConversionBufferFullException;
/*     */ 
/*     */   public int flushAny(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws ConversionBufferFullException
/*     */   {
/* 292 */     if (!this.subMode)
/* 293 */       throw new IllegalStateException("Substitution mode is not on");
/*     */     try
/*     */     {
/* 296 */       return flush(paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*     */     catch (MalformedInputException localMalformedInputException)
/*     */     {
/* 304 */       int i = this.subBytes.length;
/* 305 */       byte[] arrayOfByte = this.subBytes;
/* 306 */       int j = paramInt1;
/* 307 */       if (paramInt1 + i > paramInt2)
/* 308 */         throw new ConversionBufferFullException();
/* 309 */       for (int k = 0; k < i; k++)
/* 310 */         paramArrayOfByte[(j++)] = arrayOfByte[k];
/* 311 */       this.byteOff = (this.charOff = 0);
/* 312 */       this.badInputLength = 0;
/* 313 */       return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract void reset();
/*     */ 
/*     */   public boolean canConvert(char paramChar)
/*     */   {
/*     */     try
/*     */     {
/* 331 */       char[] arrayOfChar = new char[1];
/* 332 */       byte[] arrayOfByte = new byte[3];
/* 333 */       arrayOfChar[0] = paramChar;
/* 334 */       convert(arrayOfChar, 0, 1, arrayOfByte, 0, 3);
/* 335 */       return true; } catch (CharConversionException localCharConversionException) {
/*     */     }
/* 337 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract int getMaxBytesPerChar();
/*     */ 
/*     */   public int getBadInputLength()
/*     */   {
/* 355 */     return this.badInputLength;
/*     */   }
/*     */ 
/*     */   public int nextCharIndex()
/*     */   {
/* 364 */     return this.charOff;
/*     */   }
/*     */ 
/*     */   public int nextByteIndex()
/*     */   {
/* 372 */     return this.byteOff;
/*     */   }
/*     */ 
/*     */   public void setSubstitutionMode(boolean paramBoolean)
/*     */   {
/* 386 */     this.subMode = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setSubstitutionBytes(byte[] paramArrayOfByte)
/*     */     throws IllegalArgumentException
/*     */   {
/* 405 */     if (paramArrayOfByte.length > getMaxBytesPerChar()) {
/* 406 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 409 */     this.subBytes = new byte[paramArrayOfByte.length];
/* 410 */     System.arraycopy(paramArrayOfByte, 0, this.subBytes, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 417 */     return "CharToByteConverter: " + getCharacterEncoding();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.CharToByteConverter
 * JD-Core Version:    0.6.2
 */