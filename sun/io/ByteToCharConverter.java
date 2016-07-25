/*     */ package sun.io;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class ByteToCharConverter
/*     */ {
/*  47 */   protected boolean subMode = true;
/*     */ 
/*  52 */   protected char[] subChars = { 65533 };
/*     */   protected int charOff;
/*     */   protected int byteOff;
/*     */   protected int badInputLength;
/*     */ 
/*     */   public static ByteToCharConverter getDefault()
/*     */   {
/*  74 */     Object localObject = Converters.newDefaultConverter(0);
/*  75 */     return (ByteToCharConverter)localObject;
/*     */   }
/*     */ 
/*     */   public static ByteToCharConverter getConverter(String paramString)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  86 */     Object localObject = Converters.newConverter(0, paramString);
/*  87 */     return (ByteToCharConverter)localObject;
/*     */   }
/*     */ 
/*     */   public abstract String getCharacterEncoding();
/*     */ 
/*     */   public abstract int convert(byte[] paramArrayOfByte, int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4)
/*     */     throws MalformedInputException, UnknownCharacterException, ConversionBufferFullException;
/*     */ 
/*     */   public char[] convertAll(byte[] paramArrayOfByte)
/*     */     throws MalformedInputException
/*     */   {
/* 158 */     reset();
/* 159 */     boolean bool = this.subMode;
/* 160 */     this.subMode = true;
/*     */ 
/* 162 */     char[] arrayOfChar1 = new char[getMaxCharsPerByte() * paramArrayOfByte.length];
/*     */     try
/*     */     {
/* 165 */       int i = convert(paramArrayOfByte, 0, paramArrayOfByte.length, arrayOfChar1, 0, arrayOfChar1.length);
/*     */ 
/* 167 */       i += flush(arrayOfChar1, i, arrayOfChar1.length);
/*     */ 
/* 169 */       char[] arrayOfChar2 = new char[i];
/* 170 */       System.arraycopy(arrayOfChar1, 0, arrayOfChar2, 0, i);
/* 171 */       return arrayOfChar2;
/*     */     }
/*     */     catch (ConversionBufferFullException localConversionBufferFullException)
/*     */     {
/* 175 */       throw new InternalError("this.getMaxCharsBerByte returned bad value");
/*     */     }
/*     */     catch (UnknownCharacterException localUnknownCharacterException)
/*     */     {
/* 180 */       throw new InternalError();
/*     */     }
/*     */     finally {
/* 183 */       this.subMode = bool;
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract int flush(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws MalformedInputException, ConversionBufferFullException;
/*     */ 
/*     */   public abstract void reset();
/*     */ 
/*     */   public int getMaxCharsPerByte()
/*     */   {
/* 218 */     return 1;
/*     */   }
/*     */ 
/*     */   public int getBadInputLength()
/*     */   {
/* 228 */     return this.badInputLength;
/*     */   }
/*     */ 
/*     */   public int nextCharIndex()
/*     */   {
/* 236 */     return this.charOff;
/*     */   }
/*     */ 
/*     */   public int nextByteIndex()
/*     */   {
/* 244 */     return this.byteOff;
/*     */   }
/*     */ 
/*     */   public void setSubstitutionMode(boolean paramBoolean)
/*     */   {
/* 258 */     this.subMode = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setSubstitutionChars(char[] paramArrayOfChar)
/*     */     throws IllegalArgumentException
/*     */   {
/* 280 */     if (paramArrayOfChar.length > getMaxCharsPerByte()) {
/* 281 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 284 */     this.subChars = new char[paramArrayOfChar.length];
/* 285 */     System.arraycopy(paramArrayOfChar, 0, this.subChars, 0, paramArrayOfChar.length);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 292 */     return "ByteToCharConverter: " + getCharacterEncoding();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.ByteToCharConverter
 * JD-Core Version:    0.6.2
 */