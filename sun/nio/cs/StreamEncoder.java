/*     */ package sun.nio.cs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ 
/*     */ public class StreamEncoder extends Writer
/*     */ {
/*     */   private static final int DEFAULT_BYTE_BUFFER_SIZE = 8192;
/*  41 */   private volatile boolean isOpen = true;
/*     */   private Charset cs;
/*     */   private CharsetEncoder encoder;
/*     */   private ByteBuffer bb;
/*     */   private final OutputStream out;
/*     */   private WritableByteChannel ch;
/* 170 */   private boolean haveLeftoverChar = false;
/*     */   private char leftoverChar;
/* 172 */   private CharBuffer lcb = null;
/*     */ 
/*     */   private void ensureOpen()
/*     */     throws IOException
/*     */   {
/*  44 */     if (!this.isOpen)
/*  45 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   public static StreamEncoder forOutputStreamWriter(OutputStream paramOutputStream, Object paramObject, String paramString)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  54 */     String str = paramString;
/*  55 */     if (str == null)
/*  56 */       str = Charset.defaultCharset().name();
/*     */     try {
/*  58 */       if (Charset.isSupported(str))
/*  59 */         return new StreamEncoder(paramOutputStream, paramObject, Charset.forName(str)); 
/*     */     } catch (IllegalCharsetNameException localIllegalCharsetNameException) {  }
/*     */ 
/*  61 */     throw new UnsupportedEncodingException(str);
/*     */   }
/*     */ 
/*     */   public static StreamEncoder forOutputStreamWriter(OutputStream paramOutputStream, Object paramObject, Charset paramCharset)
/*     */   {
/*  68 */     return new StreamEncoder(paramOutputStream, paramObject, paramCharset);
/*     */   }
/*     */ 
/*     */   public static StreamEncoder forOutputStreamWriter(OutputStream paramOutputStream, Object paramObject, CharsetEncoder paramCharsetEncoder)
/*     */   {
/*  75 */     return new StreamEncoder(paramOutputStream, paramObject, paramCharsetEncoder);
/*     */   }
/*     */ 
/*     */   public static StreamEncoder forEncoder(WritableByteChannel paramWritableByteChannel, CharsetEncoder paramCharsetEncoder, int paramInt)
/*     */   {
/*  85 */     return new StreamEncoder(paramWritableByteChannel, paramCharsetEncoder, paramInt);
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/*  96 */     if (isOpen())
/*  97 */       return encodingName();
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */   public void flushBuffer() throws IOException {
/* 102 */     synchronized (this.lock) {
/* 103 */       if (isOpen())
/* 104 */         implFlushBuffer();
/*     */       else
/* 106 */         throw new IOException("Stream closed");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(int paramInt) throws IOException {
/* 111 */     char[] arrayOfChar = new char[1];
/* 112 */     arrayOfChar[0] = ((char)paramInt);
/* 113 */     write(arrayOfChar, 0, 1);
/*     */   }
/*     */ 
/*     */   public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2) throws IOException {
/* 117 */     synchronized (this.lock) {
/* 118 */       ensureOpen();
/* 119 */       if ((paramInt1 < 0) || (paramInt1 > paramArrayOfChar.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfChar.length) || (paramInt1 + paramInt2 < 0))
/*     */       {
/* 121 */         throw new IndexOutOfBoundsException();
/* 122 */       }if (paramInt2 == 0) {
/* 123 */         return;
/*     */       }
/* 125 */       implWrite(paramArrayOfChar, paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(String paramString, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 131 */     if (paramInt2 < 0)
/* 132 */       throw new IndexOutOfBoundsException();
/* 133 */     char[] arrayOfChar = new char[paramInt2];
/* 134 */     paramString.getChars(paramInt1, paramInt1 + paramInt2, arrayOfChar, 0);
/* 135 */     write(arrayOfChar, 0, paramInt2);
/*     */   }
/*     */ 
/*     */   public void flush() throws IOException {
/* 139 */     synchronized (this.lock) {
/* 140 */       ensureOpen();
/* 141 */       implFlush();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 146 */     synchronized (this.lock) {
/* 147 */       if (!this.isOpen)
/* 148 */         return;
/* 149 */       implClose();
/* 150 */       this.isOpen = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isOpen() {
/* 155 */     return this.isOpen;
/*     */   }
/*     */ 
/*     */   private StreamEncoder(OutputStream paramOutputStream, Object paramObject, Charset paramCharset)
/*     */   {
/* 175 */     this(paramOutputStream, paramObject, paramCharset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE));
/*     */   }
/*     */ 
/*     */   private StreamEncoder(OutputStream paramOutputStream, Object paramObject, CharsetEncoder paramCharsetEncoder)
/*     */   {
/* 182 */     super(paramObject);
/* 183 */     this.out = paramOutputStream;
/* 184 */     this.ch = null;
/* 185 */     this.cs = paramCharsetEncoder.charset();
/* 186 */     this.encoder = paramCharsetEncoder;
/*     */ 
/* 194 */     if (this.ch == null)
/* 195 */       this.bb = ByteBuffer.allocate(8192);
/*     */   }
/*     */ 
/*     */   private StreamEncoder(WritableByteChannel paramWritableByteChannel, CharsetEncoder paramCharsetEncoder, int paramInt)
/*     */   {
/* 200 */     this.out = null;
/* 201 */     this.ch = paramWritableByteChannel;
/* 202 */     this.cs = paramCharsetEncoder.charset();
/* 203 */     this.encoder = paramCharsetEncoder;
/* 204 */     this.bb = ByteBuffer.allocate(paramInt < 0 ? 8192 : paramInt);
/*     */   }
/*     */ 
/*     */   private void writeBytes()
/*     */     throws IOException
/*     */   {
/* 210 */     this.bb.flip();
/* 211 */     int i = this.bb.limit();
/* 212 */     int j = this.bb.position();
/* 213 */     assert (j <= i);
/* 214 */     int k = j <= i ? i - j : 0;
/*     */ 
/* 216 */     if (k > 0) {
/* 217 */       if (this.ch != null) {
/* 218 */         if ((this.ch.write(this.bb) != k) && 
/* 219 */           (!$assertionsDisabled)) throw new AssertionError(k); 
/*     */       }
/*     */       else {
/* 221 */         this.out.write(this.bb.array(), this.bb.arrayOffset() + j, k);
/*     */       }
/*     */     }
/* 224 */     this.bb.clear();
/*     */   }
/*     */ 
/*     */   private void flushLeftoverChar(CharBuffer paramCharBuffer, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 230 */     if ((!this.haveLeftoverChar) && (!paramBoolean))
/* 231 */       return;
/* 232 */     if (this.lcb == null)
/* 233 */       this.lcb = CharBuffer.allocate(2);
/*     */     else
/* 235 */       this.lcb.clear();
/* 236 */     if (this.haveLeftoverChar)
/* 237 */       this.lcb.put(this.leftoverChar);
/* 238 */     if ((paramCharBuffer != null) && (paramCharBuffer.hasRemaining()))
/* 239 */       this.lcb.put(paramCharBuffer.get());
/* 240 */     this.lcb.flip();
/* 241 */     while ((this.lcb.hasRemaining()) || (paramBoolean)) {
/* 242 */       CoderResult localCoderResult = this.encoder.encode(this.lcb, this.bb, paramBoolean);
/* 243 */       if (localCoderResult.isUnderflow()) {
/* 244 */         if (!this.lcb.hasRemaining()) break;
/* 245 */         this.leftoverChar = this.lcb.get();
/* 246 */         if ((paramCharBuffer != null) && (paramCharBuffer.hasRemaining()))
/* 247 */           flushLeftoverChar(paramCharBuffer, paramBoolean);
/* 248 */         return;
/*     */       }
/*     */ 
/* 252 */       if (localCoderResult.isOverflow()) {
/* 253 */         assert (this.bb.position() > 0);
/* 254 */         writeBytes();
/*     */       }
/*     */       else {
/* 257 */         localCoderResult.throwException();
/*     */       }
/*     */     }
/* 259 */     this.haveLeftoverChar = false;
/*     */   }
/*     */ 
/*     */   void implWrite(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 265 */     CharBuffer localCharBuffer = CharBuffer.wrap(paramArrayOfChar, paramInt1, paramInt2);
/*     */ 
/* 267 */     if (this.haveLeftoverChar) {
/* 268 */       flushLeftoverChar(localCharBuffer, false);
/*     */     }
/* 270 */     while (localCharBuffer.hasRemaining()) {
/* 271 */       CoderResult localCoderResult = this.encoder.encode(localCharBuffer, this.bb, false);
/* 272 */       if (localCoderResult.isUnderflow()) {
/* 273 */         assert (localCharBuffer.remaining() <= 1) : localCharBuffer.remaining();
/* 274 */         if (localCharBuffer.remaining() != 1) break;
/* 275 */         this.haveLeftoverChar = true;
/* 276 */         this.leftoverChar = localCharBuffer.get(); break;
/*     */       }
/*     */ 
/* 280 */       if (localCoderResult.isOverflow()) {
/* 281 */         assert (this.bb.position() > 0);
/* 282 */         writeBytes();
/*     */       }
/*     */       else {
/* 285 */         localCoderResult.throwException();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 290 */   void implFlushBuffer() throws IOException { if (this.bb.position() > 0)
/* 291 */       writeBytes(); }
/*     */ 
/*     */   void implFlush() throws IOException
/*     */   {
/* 295 */     implFlushBuffer();
/* 296 */     if (this.out != null)
/* 297 */       this.out.flush();
/*     */   }
/*     */ 
/*     */   void implClose() throws IOException {
/* 301 */     flushLeftoverChar(null, true);
/*     */     try {
/*     */       while (true) {
/* 304 */         CoderResult localCoderResult = this.encoder.flush(this.bb);
/* 305 */         if (localCoderResult.isUnderflow())
/*     */           break;
/* 307 */         if (localCoderResult.isOverflow()) {
/* 308 */           assert (this.bb.position() > 0);
/* 309 */           writeBytes();
/*     */         }
/*     */         else {
/* 312 */           localCoderResult.throwException();
/*     */         }
/*     */       }
/* 315 */       if (this.bb.position() > 0)
/* 316 */         writeBytes();
/* 317 */       if (this.ch != null)
/* 318 */         this.ch.close();
/*     */       else
/* 320 */         this.out.close();
/*     */     } catch (IOException localIOException) {
/* 322 */       this.encoder.reset();
/* 323 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   String encodingName() {
/* 328 */     return (this.cs instanceof HistoricallyNamedCharset) ? ((HistoricallyNamedCharset)this.cs).historicalName() : this.cs.name();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.StreamEncoder
 * JD-Core Version:    0.6.2
 */