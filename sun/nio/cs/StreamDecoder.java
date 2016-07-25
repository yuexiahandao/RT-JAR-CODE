/*     */ package sun.nio.cs;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ 
/*     */ public class StreamDecoder extends Reader
/*     */ {
/*     */   private static final int MIN_BYTE_BUFFER_SIZE = 32;
/*     */   private static final int DEFAULT_BYTE_BUFFER_SIZE = 8192;
/*  42 */   private volatile boolean isOpen = true;
/*     */ 
/*  53 */   private boolean haveLeftoverChar = false;
/*     */   private char leftoverChar;
/* 208 */   private static volatile boolean channelsAvailable = true;
/*     */   private Charset cs;
/*     */   private CharsetDecoder decoder;
/*     */   private ByteBuffer bb;
/*     */   private InputStream in;
/*     */   private ReadableByteChannel ch;
/*     */ 
/*     */   private void ensureOpen()
/*     */     throws IOException
/*     */   {
/*  45 */     if (!this.isOpen)
/*  46 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   public static StreamDecoder forInputStreamReader(InputStream paramInputStream, Object paramObject, String paramString)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  64 */     String str = paramString;
/*  65 */     if (str == null)
/*  66 */       str = Charset.defaultCharset().name();
/*     */     try {
/*  68 */       if (Charset.isSupported(str))
/*  69 */         return new StreamDecoder(paramInputStream, paramObject, Charset.forName(str)); 
/*     */     } catch (IllegalCharsetNameException localIllegalCharsetNameException) {  }
/*     */ 
/*  71 */     throw new UnsupportedEncodingException(str);
/*     */   }
/*     */ 
/*     */   public static StreamDecoder forInputStreamReader(InputStream paramInputStream, Object paramObject, Charset paramCharset)
/*     */   {
/*  78 */     return new StreamDecoder(paramInputStream, paramObject, paramCharset);
/*     */   }
/*     */ 
/*     */   public static StreamDecoder forInputStreamReader(InputStream paramInputStream, Object paramObject, CharsetDecoder paramCharsetDecoder)
/*     */   {
/*  85 */     return new StreamDecoder(paramInputStream, paramObject, paramCharsetDecoder);
/*     */   }
/*     */ 
/*     */   public static StreamDecoder forDecoder(ReadableByteChannel paramReadableByteChannel, CharsetDecoder paramCharsetDecoder, int paramInt)
/*     */   {
/*  95 */     return new StreamDecoder(paramReadableByteChannel, paramCharsetDecoder, paramInt);
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 106 */     if (isOpen())
/* 107 */       return encodingName();
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */   public int read() throws IOException {
/* 112 */     return read0();
/*     */   }
/*     */ 
/*     */   private int read0() throws IOException {
/* 116 */     synchronized (this.lock)
/*     */     {
/* 119 */       if (this.haveLeftoverChar) {
/* 120 */         this.haveLeftoverChar = false;
/* 121 */         return this.leftoverChar;
/*     */       }
/*     */ 
/* 125 */       char[] arrayOfChar = new char[2];
/* 126 */       int i = read(arrayOfChar, 0, 2);
/* 127 */       switch (i) {
/*     */       case -1:
/* 129 */         return -1;
/*     */       case 2:
/* 131 */         this.leftoverChar = arrayOfChar[1];
/* 132 */         this.haveLeftoverChar = true;
/*     */       case 1:
/* 135 */         return arrayOfChar[0];
/*     */       case 0:
/* 137 */       }if (!$assertionsDisabled) throw new AssertionError(i);
/* 138 */       return -1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 144 */     int i = paramInt1;
/* 145 */     int j = paramInt2;
/* 146 */     synchronized (this.lock) {
/* 147 */       ensureOpen();
/* 148 */       if ((i < 0) || (i > paramArrayOfChar.length) || (j < 0) || (i + j > paramArrayOfChar.length) || (i + j < 0))
/*     */       {
/* 150 */         throw new IndexOutOfBoundsException();
/*     */       }
/* 152 */       if (j == 0) {
/* 153 */         return 0;
/*     */       }
/* 155 */       int k = 0;
/*     */ 
/* 157 */       if (this.haveLeftoverChar)
/*     */       {
/* 159 */         paramArrayOfChar[i] = this.leftoverChar;
/* 160 */         i++; j--;
/* 161 */         this.haveLeftoverChar = false;
/* 162 */         k = 1;
/* 163 */         if ((j == 0) || (!implReady()))
/*     */         {
/* 165 */           return k;
/*     */         }
/*     */       }
/* 168 */       if (j == 1)
/*     */       {
/* 170 */         int m = read0();
/* 171 */         if (m == -1)
/* 172 */           return k == 0 ? -1 : k;
/* 173 */         paramArrayOfChar[i] = ((char)m);
/* 174 */         return k + 1;
/*     */       }
/*     */ 
/* 177 */       return k + implRead(paramArrayOfChar, i, i + j);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean ready() throws IOException {
/* 182 */     synchronized (this.lock) {
/* 183 */       ensureOpen();
/* 184 */       return (this.haveLeftoverChar) || (implReady());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 189 */     synchronized (this.lock) {
/* 190 */       if (!this.isOpen)
/* 191 */         return;
/* 192 */       implClose();
/* 193 */       this.isOpen = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isOpen() {
/* 198 */     return this.isOpen;
/*     */   }
/*     */ 
/*     */   private static FileChannel getChannel(FileInputStream paramFileInputStream)
/*     */   {
/* 211 */     if (!channelsAvailable)
/* 212 */       return null;
/*     */     try {
/* 214 */       return paramFileInputStream.getChannel();
/*     */     } catch (UnsatisfiedLinkError localUnsatisfiedLinkError) {
/* 216 */       channelsAvailable = false;
/* 217 */     }return null;
/*     */   }
/*     */ 
/*     */   StreamDecoder(InputStream paramInputStream, Object paramObject, Charset paramCharset)
/*     */   {
/* 230 */     this(paramInputStream, paramObject, paramCharset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE));
/*     */   }
/*     */ 
/*     */   StreamDecoder(InputStream paramInputStream, Object paramObject, CharsetDecoder paramCharsetDecoder)
/*     */   {
/* 237 */     super(paramObject);
/* 238 */     this.cs = paramCharsetDecoder.charset();
/* 239 */     this.decoder = paramCharsetDecoder;
/*     */ 
/* 247 */     if (this.ch == null) {
/* 248 */       this.in = paramInputStream;
/* 249 */       this.ch = null;
/* 250 */       this.bb = ByteBuffer.allocate(8192);
/*     */     }
/* 252 */     this.bb.flip();
/*     */   }
/*     */ 
/*     */   StreamDecoder(ReadableByteChannel paramReadableByteChannel, CharsetDecoder paramCharsetDecoder, int paramInt) {
/* 256 */     this.in = null;
/* 257 */     this.ch = paramReadableByteChannel;
/* 258 */     this.decoder = paramCharsetDecoder;
/* 259 */     this.cs = paramCharsetDecoder.charset();
/* 260 */     this.bb = ByteBuffer.allocate(paramInt < 32 ? 32 : paramInt < 0 ? 8192 : paramInt);
/*     */ 
/* 265 */     this.bb.flip();
/*     */   }
/*     */ 
/*     */   private int readBytes() throws IOException {
/* 269 */     this.bb.compact();
/*     */     try
/*     */     {
/*     */       int j;
/* 271 */       if (this.ch != null)
/*     */       {
/* 273 */         i = this.ch.read(this.bb);
/* 274 */         if (i < 0)
/* 275 */           return i;
/*     */       }
/*     */       else {
/* 278 */         i = this.bb.limit();
/* 279 */         j = this.bb.position();
/* 280 */         assert (j <= i);
/* 281 */         int k = j <= i ? i - j : 0;
/* 282 */         assert (k > 0);
/* 283 */         int m = this.in.read(this.bb.array(), this.bb.arrayOffset() + j, k);
/* 284 */         if (m < 0)
/* 285 */           return m;
/* 286 */         if (m == 0)
/* 287 */           throw new IOException("Underlying input stream returned zero bytes");
/* 288 */         assert (m <= k) : ("n = " + m + ", rem = " + k);
/* 289 */         this.bb.position(j + m);
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 294 */       this.bb.flip();
/*     */     }
/*     */ 
/* 297 */     int i = this.bb.remaining();
/* 298 */     assert (i != 0) : i;
/* 299 */     return i;
/*     */   }
/*     */ 
/*     */   int implRead(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 308 */     assert (paramInt2 - paramInt1 > 1);
/*     */ 
/* 310 */     CharBuffer localCharBuffer = CharBuffer.wrap(paramArrayOfChar, paramInt1, paramInt2 - paramInt1);
/* 311 */     if (localCharBuffer.position() != 0)
/*     */     {
/* 313 */       localCharBuffer = localCharBuffer.slice();
/*     */     }
/* 315 */     boolean bool = false;
/*     */     while (true) {
/* 317 */       CoderResult localCoderResult = this.decoder.decode(this.bb, localCharBuffer, bool);
/* 318 */       if (localCoderResult.isUnderflow()) {
/* 319 */         if (bool)
/*     */           break;
/* 321 */         if (!localCharBuffer.hasRemaining())
/*     */           break;
/* 323 */         if ((localCharBuffer.position() > 0) && (!inReady()))
/*     */           break;
/* 325 */         int i = readBytes();
/* 326 */         if (i < 0) {
/* 327 */           bool = true;
/* 328 */           if ((localCharBuffer.position() == 0) && (!this.bb.hasRemaining()))
/*     */             break;
/* 330 */           this.decoder.reset();
/*     */         }
/*     */       }
/*     */       else {
/* 334 */         if (localCoderResult.isOverflow()) {
/* 335 */           if (($assertionsDisabled) || (localCharBuffer.position() > 0)) break; throw new AssertionError();
/*     */         }
/*     */ 
/* 338 */         localCoderResult.throwException();
/*     */       }
/*     */     }
/* 341 */     if (bool)
/*     */     {
/* 343 */       this.decoder.reset();
/*     */     }
/*     */ 
/* 346 */     if (localCharBuffer.position() == 0) {
/* 347 */       if (bool)
/* 348 */         return -1;
/* 349 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/* 351 */     return localCharBuffer.position();
/*     */   }
/*     */ 
/*     */   String encodingName() {
/* 355 */     return (this.cs instanceof HistoricallyNamedCharset) ? ((HistoricallyNamedCharset)this.cs).historicalName() : this.cs.name();
/*     */   }
/*     */ 
/*     */   private boolean inReady()
/*     */   {
/*     */     try
/*     */     {
/* 362 */       return ((this.in != null) && (this.in.available() > 0)) || ((this.ch instanceof FileChannel));
/*     */     } catch (IOException localIOException) {
/*     */     }
/* 365 */     return false;
/*     */   }
/*     */ 
/*     */   boolean implReady()
/*     */   {
/* 370 */     return (this.bb.hasRemaining()) || (inReady());
/*     */   }
/*     */ 
/*     */   void implClose() throws IOException {
/* 374 */     if (this.ch != null)
/* 375 */       this.ch.close();
/*     */     else
/* 377 */       this.in.close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.StreamDecoder
 * JD-Core Version:    0.6.2
 */