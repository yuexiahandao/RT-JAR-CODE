/*     */ package sun.invoke.anon;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.BufferUnderflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ public class ConstantPoolParser
/*     */ {
/*     */   final byte[] classFile;
/*     */   final byte[] tags;
/*     */   final char[] firstHeader;
/*     */   int endOffset;
/*     */   char[] secondHeader;
/*  47 */   private char[] charArray = new char[80];
/*     */ 
/*     */   public ConstantPoolParser(byte[] paramArrayOfByte)
/*     */     throws InvalidConstantPoolFormatException
/*     */   {
/*  54 */     this.classFile = paramArrayOfByte;
/*  55 */     this.firstHeader = parseHeader(paramArrayOfByte);
/*  56 */     this.tags = new byte[this.firstHeader[4]];
/*     */   }
/*     */ 
/*     */   public ConstantPoolParser(Class<?> paramClass)
/*     */     throws IOException, InvalidConstantPoolFormatException
/*     */   {
/*  72 */     this(AnonymousClassLoader.readClassFile(paramClass));
/*     */   }
/*     */ 
/*     */   public ConstantPoolPatch createPatch()
/*     */   {
/*  80 */     return new ConstantPoolPatch(this);
/*     */   }
/*     */ 
/*     */   public byte getTag(int paramInt)
/*     */   {
/*  88 */     getEndOffset();
/*  89 */     return this.tags[paramInt];
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/*  94 */     return this.firstHeader[4];
/*     */   }
/*     */ 
/*     */   public int getStartOffset()
/*     */   {
/*  99 */     return this.firstHeader.length * 2;
/*     */   }
/*     */ 
/*     */   public int getEndOffset()
/*     */   {
/* 104 */     if (this.endOffset == 0)
/* 105 */       throw new IllegalStateException("class file has not yet been parsed");
/* 106 */     return this.endOffset;
/*     */   }
/*     */ 
/*     */   public int getThisClassIndex()
/*     */   {
/* 111 */     getEndOffset();
/* 112 */     return this.secondHeader[1];
/*     */   }
/*     */ 
/*     */   public int getTailLength()
/*     */   {
/* 117 */     return this.classFile.length - getEndOffset();
/*     */   }
/*     */ 
/*     */   public void writeHead(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 124 */     paramOutputStream.write(this.classFile, 0, getEndOffset());
/*     */   }
/*     */ 
/*     */   void writePatchedHead(OutputStream paramOutputStream, Object[] paramArrayOfObject)
/*     */   {
/* 134 */     throw new UnsupportedOperationException("Not yet implemented");
/*     */   }
/*     */ 
/*     */   public void writeTail(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 141 */     paramOutputStream.write(this.classFile, getEndOffset(), getTailLength());
/*     */   }
/*     */ 
/*     */   private static char[] parseHeader(byte[] paramArrayOfByte) throws InvalidConstantPoolFormatException {
/* 145 */     char[] arrayOfChar = new char[5];
/* 146 */     ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte);
/* 147 */     for (int i = 0; i < arrayOfChar.length; i++)
/* 148 */       arrayOfChar[i] = ((char)getUnsignedShort(localByteBuffer));
/* 149 */     i = arrayOfChar[0] << '\020' | arrayOfChar[1] << '\000';
/* 150 */     if (i != -889275714) {
/* 151 */       throw new InvalidConstantPoolFormatException("invalid magic number " + i);
/*     */     }
/* 153 */     int j = arrayOfChar[4];
/* 154 */     if (j < 1)
/* 155 */       throw new InvalidConstantPoolFormatException("constant pool length < 1");
/* 156 */     return arrayOfChar;
/*     */   }
/*     */ 
/*     */   public void parse(ConstantPoolVisitor paramConstantPoolVisitor)
/*     */     throws InvalidConstantPoolFormatException
/*     */   {
/* 169 */     ByteBuffer localByteBuffer = ByteBuffer.wrap(this.classFile);
/* 170 */     localByteBuffer.position(getStartOffset());
/*     */ 
/* 172 */     Object[] arrayOfObject = new Object[getLength()];
/*     */     try {
/* 174 */       parseConstantPool(localByteBuffer, arrayOfObject, paramConstantPoolVisitor);
/*     */     } catch (BufferUnderflowException localBufferUnderflowException) {
/* 176 */       throw new InvalidConstantPoolFormatException(localBufferUnderflowException);
/*     */     }
/* 178 */     if (this.endOffset == 0) {
/* 179 */       this.endOffset = localByteBuffer.position();
/* 180 */       this.secondHeader = new char[4];
/* 181 */       for (int i = 0; i < this.secondHeader.length; i++) {
/* 182 */         this.secondHeader[i] = ((char)getUnsignedShort(localByteBuffer));
/*     */       }
/*     */     }
/* 185 */     resolveConstantPool(arrayOfObject, paramConstantPoolVisitor);
/*     */   }
/*     */ 
/*     */   private char[] getCharArray(int paramInt) {
/* 189 */     if (paramInt <= this.charArray.length)
/* 190 */       return this.charArray;
/* 191 */     return this.charArray = new char[paramInt];
/*     */   }
/*     */ 
/*     */   private void parseConstantPool(ByteBuffer paramByteBuffer, Object[] paramArrayOfObject, ConstantPoolVisitor paramConstantPoolVisitor) throws InvalidConstantPoolFormatException {
/* 195 */     for (int i = 1; i < this.tags.length; ) {
/* 196 */       byte b = (byte)getUnsignedByte(paramByteBuffer);
/* 197 */       assert ((this.tags[i] == 0) || (this.tags[i] == b));
/* 198 */       this.tags[i] = b;
/* 199 */       switch (b) {
/*     */       case 1:
/* 201 */         int j = getUnsignedShort(paramByteBuffer);
/* 202 */         String str = getUTF8(paramByteBuffer, j, getCharArray(j));
/* 203 */         paramConstantPoolVisitor.visitUTF8(i, (byte)1, str);
/* 204 */         this.tags[i] = b;
/* 205 */         paramArrayOfObject[(i++)] = str;
/* 206 */         break;
/*     */       case 3:
/* 208 */         paramConstantPoolVisitor.visitConstantValue(i, b, Integer.valueOf(paramByteBuffer.getInt()));
/* 209 */         i++;
/* 210 */         break;
/*     */       case 4:
/* 212 */         paramConstantPoolVisitor.visitConstantValue(i, b, Float.valueOf(paramByteBuffer.getFloat()));
/* 213 */         i++;
/* 214 */         break;
/*     */       case 5:
/* 216 */         paramConstantPoolVisitor.visitConstantValue(i, b, Long.valueOf(paramByteBuffer.getLong()));
/* 217 */         i += 2;
/* 218 */         break;
/*     */       case 6:
/* 220 */         paramConstantPoolVisitor.visitConstantValue(i, b, Double.valueOf(paramByteBuffer.getDouble()));
/* 221 */         i += 2;
/* 222 */         break;
/*     */       case 7:
/*     */       case 8:
/* 226 */         this.tags[i] = b;
/* 227 */         paramArrayOfObject[(i++)] = { getUnsignedShort(paramByteBuffer) };
/* 228 */         break;
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/* 234 */         this.tags[i] = b;
/* 235 */         paramArrayOfObject[(i++)] = { getUnsignedShort(paramByteBuffer), getUnsignedShort(paramByteBuffer) };
/* 236 */         break;
/*     */       case 2:
/*     */       default:
/* 238 */         throw new AssertionError("invalid constant " + b);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void resolveConstantPool(Object[] paramArrayOfObject, ConstantPoolVisitor paramConstantPoolVisitor)
/*     */   {
/* 245 */     int i = 1;
/*     */     int m;
/* 245 */     for (int j = paramArrayOfObject.length - 1; 
/* 246 */       i <= j; 
/* 247 */       j = m) {
/* 248 */       int k = j; m = i - 1;
/*     */ 
/* 250 */       for (int n = i; n <= j; n++) {
/* 251 */         Object localObject1 = paramArrayOfObject[n];
/* 252 */         if ((localObject1 instanceof int[]))
/*     */         {
/* 254 */           int[] arrayOfInt = (int[])localObject1;
/* 255 */           byte b = this.tags[n];
/*     */           Object localObject2;
/*     */           Object localObject3;
/* 256 */           switch (b) {
/*     */           case 8:
/* 258 */             String str = (String)paramArrayOfObject[arrayOfInt[0]];
/* 259 */             paramConstantPoolVisitor.visitConstantString(n, b, str, arrayOfInt[0]);
/* 260 */             paramArrayOfObject[n] = null;
/* 261 */             break;
/*     */           case 7:
/* 263 */             localObject2 = (String)paramArrayOfObject[arrayOfInt[0]];
/*     */ 
/* 265 */             localObject2 = ((String)localObject2).replace('/', '.');
/* 266 */             paramConstantPoolVisitor.visitConstantString(n, b, (String)localObject2, arrayOfInt[0]);
/* 267 */             paramArrayOfObject[n] = localObject2;
/* 268 */             break;
/*     */           case 12:
/* 271 */             localObject2 = (String)paramArrayOfObject[arrayOfInt[0]];
/* 272 */             localObject3 = (String)paramArrayOfObject[arrayOfInt[1]];
/* 273 */             paramConstantPoolVisitor.visitDescriptor(n, b, (String)localObject2, (String)localObject3, arrayOfInt[0], arrayOfInt[1]);
/*     */ 
/* 275 */             paramArrayOfObject[n] = { localObject2, localObject3 };
/* 276 */             break;
/*     */           case 9:
/*     */           case 10:
/*     */           case 11:
/* 281 */             localObject2 = paramArrayOfObject[arrayOfInt[0]];
/* 282 */             localObject3 = paramArrayOfObject[arrayOfInt[1]];
/* 283 */             if ((!(localObject2 instanceof String)) || (!(localObject3 instanceof String[])))
/*     */             {
/* 286 */               if (k > n) k = n;
/* 287 */               if (m < n) m = n; 
/*     */             }
/*     */             else
/*     */             {
/* 290 */               String[] arrayOfString = (String[])localObject3;
/* 291 */               paramConstantPoolVisitor.visitMemberRef(n, b, (String)localObject2, arrayOfString[0], arrayOfString[1], arrayOfInt[0], arrayOfInt[1]);
/*     */ 
/* 296 */               paramArrayOfObject[n] = null;
/*     */             }
/* 298 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 247 */       i = k;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int getUnsignedByte(ByteBuffer paramByteBuffer)
/*     */   {
/* 307 */     return paramByteBuffer.get() & 0xFF;
/*     */   }
/*     */ 
/*     */   private static int getUnsignedShort(ByteBuffer paramByteBuffer) {
/* 311 */     int i = getUnsignedByte(paramByteBuffer);
/* 312 */     int j = getUnsignedByte(paramByteBuffer);
/* 313 */     return (i << 8) + (j << 0);
/*     */   }
/*     */ 
/*     */   private static String getUTF8(ByteBuffer paramByteBuffer, int paramInt, char[] paramArrayOfChar) throws InvalidConstantPoolFormatException {
/* 317 */     int i = paramByteBuffer.position() + paramInt;
/* 318 */     int j = 0;
/* 319 */     while (paramByteBuffer.position() < i) {
/* 320 */       int k = paramByteBuffer.get() & 0xFF;
/* 321 */       if (k > 127) {
/* 322 */         paramByteBuffer.position(paramByteBuffer.position() - 1);
/* 323 */         return getUTF8Extended(paramByteBuffer, i, paramArrayOfChar, j);
/*     */       }
/* 325 */       paramArrayOfChar[(j++)] = ((char)k);
/*     */     }
/* 327 */     return new String(paramArrayOfChar, 0, j);
/*     */   }
/*     */ 
/*     */   private static String getUTF8Extended(ByteBuffer paramByteBuffer, int paramInt1, char[] paramArrayOfChar, int paramInt2) throws InvalidConstantPoolFormatException
/*     */   {
/* 332 */     while (paramByteBuffer.position() < paramInt1) {
/* 333 */       int i = paramByteBuffer.get() & 0xFF;
/*     */       int j;
/* 334 */       switch (i >> 4) { case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/* 337 */         paramArrayOfChar[(paramInt2++)] = ((char)i);
/* 338 */         break;
/*     */       case 12:
/*     */       case 13:
/* 341 */         j = paramByteBuffer.get();
/* 342 */         if ((j & 0xC0) != 128) {
/* 343 */           throw new InvalidConstantPoolFormatException("malformed input around byte " + paramByteBuffer.position());
/*     */         }
/* 345 */         paramArrayOfChar[(paramInt2++)] = ((char)((i & 0x1F) << 6 | j & 0x3F));
/*     */ 
/* 347 */         break;
/*     */       case 14:
/* 350 */         j = paramByteBuffer.get();
/* 351 */         int k = paramByteBuffer.get();
/* 352 */         if (((j & 0xC0) != 128) || ((k & 0xC0) != 128)) {
/* 353 */           throw new InvalidConstantPoolFormatException("malformed input around byte " + paramByteBuffer.position());
/*     */         }
/* 355 */         paramArrayOfChar[(paramInt2++)] = ((char)((i & 0xF) << 12 | (j & 0x3F) << 6 | (k & 0x3F) << 0));
/*     */ 
/* 358 */         break;
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/* 361 */       case 11: } throw new InvalidConstantPoolFormatException("malformed input around byte " + paramByteBuffer.position());
/*     */     }
/*     */ 
/* 366 */     return new String(paramArrayOfChar, 0, paramInt2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.invoke.anon.ConstantPoolParser
 * JD-Core Version:    0.6.2
 */