/*     */ package java.nio.channels;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.spi.AbstractInterruptibleChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import sun.nio.ch.ChannelInputStream;
/*     */ import sun.nio.cs.StreamDecoder;
/*     */ import sun.nio.cs.StreamEncoder;
/*     */ 
/*     */ public final class Channels
/*     */ {
/*     */   private static void checkNotNull(Object paramObject, String paramString)
/*     */   {
/*  66 */     if (paramObject == null)
/*  67 */       throw new NullPointerException("\"" + paramString + "\" is null!");
/*     */   }
/*     */ 
/*     */   private static void writeFullyImpl(WritableByteChannel paramWritableByteChannel, ByteBuffer paramByteBuffer)
/*     */     throws IOException
/*     */   {
/*  77 */     while (paramByteBuffer.remaining() > 0) {
/*  78 */       int i = paramWritableByteChannel.write(paramByteBuffer);
/*  79 */       if (i <= 0)
/*  80 */         throw new RuntimeException("no bytes written");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void writeFully(WritableByteChannel paramWritableByteChannel, ByteBuffer paramByteBuffer)
/*     */     throws IOException
/*     */   {
/*  93 */     if ((paramWritableByteChannel instanceof SelectableChannel)) {
/*  94 */       SelectableChannel localSelectableChannel = (SelectableChannel)paramWritableByteChannel;
/*  95 */       synchronized (localSelectableChannel.blockingLock()) {
/*  96 */         if (!localSelectableChannel.isBlocking())
/*  97 */           throw new IllegalBlockingModeException();
/*  98 */         writeFullyImpl(paramWritableByteChannel, paramByteBuffer);
/*     */       }
/*     */     } else {
/* 101 */       writeFullyImpl(paramWritableByteChannel, paramByteBuffer);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static InputStream newInputStream(ReadableByteChannel paramReadableByteChannel)
/*     */   {
/* 124 */     checkNotNull(paramReadableByteChannel, "ch");
/* 125 */     return new ChannelInputStream(paramReadableByteChannel);
/*     */   }
/*     */ 
/*     */   public static OutputStream newOutputStream(WritableByteChannel paramWritableByteChannel)
/*     */   {
/* 143 */     checkNotNull(paramWritableByteChannel, "ch");
/*     */ 
/* 145 */     return new OutputStream()
/*     */     {
/* 147 */       private ByteBuffer bb = null;
/* 148 */       private byte[] bs = null;
/* 149 */       private byte[] b1 = null;
/*     */ 
/*     */       public synchronized void write(int paramAnonymousInt) throws IOException {
/* 152 */         if (this.b1 == null)
/* 153 */           this.b1 = new byte[1];
/* 154 */         this.b1[0] = ((byte)paramAnonymousInt);
/* 155 */         write(this.b1);
/*     */       }
/*     */ 
/*     */       public synchronized void write(byte[] paramAnonymousArrayOfByte, int paramAnonymousInt1, int paramAnonymousInt2)
/*     */         throws IOException
/*     */       {
/* 161 */         if ((paramAnonymousInt1 < 0) || (paramAnonymousInt1 > paramAnonymousArrayOfByte.length) || (paramAnonymousInt2 < 0) || (paramAnonymousInt1 + paramAnonymousInt2 > paramAnonymousArrayOfByte.length) || (paramAnonymousInt1 + paramAnonymousInt2 < 0))
/*     */         {
/* 163 */           throw new IndexOutOfBoundsException();
/* 164 */         }if (paramAnonymousInt2 == 0) {
/* 165 */           return;
/*     */         }
/* 167 */         ByteBuffer localByteBuffer = this.bs == paramAnonymousArrayOfByte ? this.bb : ByteBuffer.wrap(paramAnonymousArrayOfByte);
/*     */ 
/* 170 */         localByteBuffer.limit(Math.min(paramAnonymousInt1 + paramAnonymousInt2, localByteBuffer.capacity()));
/* 171 */         localByteBuffer.position(paramAnonymousInt1);
/* 172 */         this.bb = localByteBuffer;
/* 173 */         this.bs = paramAnonymousArrayOfByte;
/* 174 */         Channels.writeFully(this.val$ch, localByteBuffer);
/*     */       }
/*     */ 
/*     */       public void close() throws IOException {
/* 178 */         this.val$ch.close();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static InputStream newInputStream(AsynchronousByteChannel paramAsynchronousByteChannel)
/*     */   {
/* 200 */     checkNotNull(paramAsynchronousByteChannel, "ch");
/* 201 */     return new InputStream()
/*     */     {
/* 203 */       private ByteBuffer bb = null;
/* 204 */       private byte[] bs = null;
/* 205 */       private byte[] b1 = null;
/*     */ 
/*     */       public synchronized int read() throws IOException
/*     */       {
/* 209 */         if (this.b1 == null)
/* 210 */           this.b1 = new byte[1];
/* 211 */         int i = read(this.b1);
/* 212 */         if (i == 1)
/* 213 */           return this.b1[0] & 0xFF;
/* 214 */         return -1;
/*     */       }
/*     */ 
/*     */       public synchronized int read(byte[] paramAnonymousArrayOfByte, int paramAnonymousInt1, int paramAnonymousInt2)
/*     */         throws IOException
/*     */       {
/* 221 */         if ((paramAnonymousInt1 < 0) || (paramAnonymousInt1 > paramAnonymousArrayOfByte.length) || (paramAnonymousInt2 < 0) || (paramAnonymousInt1 + paramAnonymousInt2 > paramAnonymousArrayOfByte.length) || (paramAnonymousInt1 + paramAnonymousInt2 < 0))
/*     */         {
/* 223 */           throw new IndexOutOfBoundsException();
/* 224 */         }if (paramAnonymousInt2 == 0) {
/* 225 */           return 0;
/*     */         }
/* 227 */         ByteBuffer localByteBuffer = this.bs == paramAnonymousArrayOfByte ? this.bb : ByteBuffer.wrap(paramAnonymousArrayOfByte);
/*     */ 
/* 230 */         localByteBuffer.position(paramAnonymousInt1);
/* 231 */         localByteBuffer.limit(Math.min(paramAnonymousInt1 + paramAnonymousInt2, localByteBuffer.capacity()));
/* 232 */         this.bb = localByteBuffer;
/* 233 */         this.bs = paramAnonymousArrayOfByte;
/*     */ 
/* 235 */         int i = 0;
/*     */         try
/*     */         {
/* 239 */           return ((Integer)this.val$ch.read(localByteBuffer).get()).intValue();
/*     */         } catch (ExecutionException localExecutionException) {
/* 241 */           throw new IOException(localExecutionException.getCause());
/*     */         } catch (InterruptedException localInterruptedException) {
/*     */           while (true) i = 1;
/*     */         }
/*     */         finally
/*     */         {
/* 247 */           if (i != 0)
/* 248 */             Thread.currentThread().interrupt();
/*     */         }
/*     */       }
/*     */ 
/*     */       public void close() throws IOException
/*     */       {
/* 254 */         this.val$ch.close();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static OutputStream newOutputStream(AsynchronousByteChannel paramAsynchronousByteChannel)
/*     */   {
/* 274 */     checkNotNull(paramAsynchronousByteChannel, "ch");
/* 275 */     return new OutputStream()
/*     */     {
/* 277 */       private ByteBuffer bb = null;
/* 278 */       private byte[] bs = null;
/* 279 */       private byte[] b1 = null;
/*     */ 
/*     */       public synchronized void write(int paramAnonymousInt) throws IOException
/*     */       {
/* 283 */         if (this.b1 == null)
/* 284 */           this.b1 = new byte[1];
/* 285 */         this.b1[0] = ((byte)paramAnonymousInt);
/* 286 */         write(this.b1);
/*     */       }
/*     */ 
/*     */       public synchronized void write(byte[] paramAnonymousArrayOfByte, int paramAnonymousInt1, int paramAnonymousInt2)
/*     */         throws IOException
/*     */       {
/* 293 */         if ((paramAnonymousInt1 < 0) || (paramAnonymousInt1 > paramAnonymousArrayOfByte.length) || (paramAnonymousInt2 < 0) || (paramAnonymousInt1 + paramAnonymousInt2 > paramAnonymousArrayOfByte.length) || (paramAnonymousInt1 + paramAnonymousInt2 < 0))
/*     */         {
/* 295 */           throw new IndexOutOfBoundsException();
/* 296 */         }if (paramAnonymousInt2 == 0) {
/* 297 */           return;
/*     */         }
/* 299 */         ByteBuffer localByteBuffer = this.bs == paramAnonymousArrayOfByte ? this.bb : ByteBuffer.wrap(paramAnonymousArrayOfByte);
/*     */ 
/* 302 */         localByteBuffer.limit(Math.min(paramAnonymousInt1 + paramAnonymousInt2, localByteBuffer.capacity()));
/* 303 */         localByteBuffer.position(paramAnonymousInt1);
/* 304 */         this.bb = localByteBuffer;
/* 305 */         this.bs = paramAnonymousArrayOfByte;
/*     */ 
/* 307 */         int i = 0;
/*     */         try {
/* 309 */           while (localByteBuffer.remaining() > 0)
/*     */             try {
/* 311 */               this.val$ch.write(localByteBuffer).get();
/*     */             } catch (ExecutionException localExecutionException) {
/* 313 */               throw new IOException(localExecutionException.getCause());
/*     */             } catch (InterruptedException localInterruptedException) {
/* 315 */               i = 1;
/*     */             }
/*     */         }
/*     */         finally {
/* 319 */           if (i != 0)
/* 320 */             Thread.currentThread().interrupt();
/*     */         }
/*     */       }
/*     */ 
/*     */       public void close() throws IOException
/*     */       {
/* 326 */         this.val$ch.close();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static ReadableByteChannel newChannel(InputStream paramInputStream)
/*     */   {
/* 347 */     checkNotNull(paramInputStream, "in");
/*     */ 
/* 349 */     if (((paramInputStream instanceof FileInputStream)) && (FileInputStream.class.equals(paramInputStream.getClass())))
/*     */     {
/* 351 */       return ((FileInputStream)paramInputStream).getChannel();
/*     */     }
/*     */ 
/* 354 */     return new ReadableByteChannelImpl(paramInputStream);
/*     */   }
/*     */ 
/*     */   public static WritableByteChannel newChannel(OutputStream paramOutputStream)
/*     */   {
/* 422 */     checkNotNull(paramOutputStream, "out");
/*     */ 
/* 424 */     if (((paramOutputStream instanceof FileOutputStream)) && (FileOutputStream.class.equals(paramOutputStream.getClass())))
/*     */     {
/* 426 */       return ((FileOutputStream)paramOutputStream).getChannel();
/*     */     }
/*     */ 
/* 429 */     return new WritableByteChannelImpl(paramOutputStream);
/*     */   }
/*     */ 
/*     */   public static Reader newReader(ReadableByteChannel paramReadableByteChannel, CharsetDecoder paramCharsetDecoder, int paramInt)
/*     */   {
/* 507 */     checkNotNull(paramReadableByteChannel, "ch");
/* 508 */     return StreamDecoder.forDecoder(paramReadableByteChannel, paramCharsetDecoder.reset(), paramInt);
/*     */   }
/*     */ 
/*     */   public static Reader newReader(ReadableByteChannel paramReadableByteChannel, String paramString)
/*     */   {
/* 543 */     checkNotNull(paramString, "csName");
/* 544 */     return newReader(paramReadableByteChannel, Charset.forName(paramString).newDecoder(), -1);
/*     */   }
/*     */ 
/*     */   public static Writer newWriter(WritableByteChannel paramWritableByteChannel, CharsetEncoder paramCharsetEncoder, int paramInt)
/*     */   {
/* 576 */     checkNotNull(paramWritableByteChannel, "ch");
/* 577 */     return StreamEncoder.forEncoder(paramWritableByteChannel, paramCharsetEncoder.reset(), paramInt);
/*     */   }
/*     */ 
/*     */   public static Writer newWriter(WritableByteChannel paramWritableByteChannel, String paramString)
/*     */   {
/* 612 */     checkNotNull(paramString, "csName");
/* 613 */     return newWriter(paramWritableByteChannel, Charset.forName(paramString).newEncoder(), -1);
/*     */   }
/*     */ 
/*     */   private static class ReadableByteChannelImpl extends AbstractInterruptibleChannel
/*     */     implements ReadableByteChannel
/*     */   {
/*     */     InputStream in;
/*     */     private static final int TRANSFER_SIZE = 8192;
/* 363 */     private byte[] buf = new byte[0];
/* 364 */     private boolean open = true;
/* 365 */     private Object readLock = new Object();
/*     */ 
/*     */     ReadableByteChannelImpl(InputStream paramInputStream) {
/* 368 */       this.in = paramInputStream;
/*     */     }
/*     */ 
/*     */     public int read(ByteBuffer paramByteBuffer) throws IOException {
/* 372 */       int i = paramByteBuffer.remaining();
/* 373 */       int j = 0;
/* 374 */       int k = 0;
/* 375 */       synchronized (this.readLock) {
/* 376 */         while (j < i) {
/* 377 */           int m = Math.min(i - j, 8192);
/*     */ 
/* 379 */           if (this.buf.length < m)
/* 380 */             this.buf = new byte[m];
/* 381 */           if ((j > 0) && (this.in.available() <= 0))
/*     */             break;
/*     */           try {
/* 384 */             begin();
/* 385 */             k = this.in.read(this.buf, 0, m);
/*     */           } finally {
/* 387 */             end(k > 0);
/*     */           }
/* 389 */           if (k < 0) {
/*     */             break;
/*     */           }
/* 392 */           j += k;
/* 393 */           paramByteBuffer.put(this.buf, 0, k);
/*     */         }
/* 395 */         if ((k < 0) && (j == 0)) {
/* 396 */           return -1;
/*     */         }
/* 398 */         return j;
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void implCloseChannel() throws IOException {
/* 403 */       this.in.close();
/* 404 */       this.open = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class WritableByteChannelImpl extends AbstractInterruptibleChannel
/*     */     implements WritableByteChannel
/*     */   {
/*     */     OutputStream out;
/*     */     private static final int TRANSFER_SIZE = 8192;
/* 438 */     private byte[] buf = new byte[0];
/* 439 */     private boolean open = true;
/* 440 */     private Object writeLock = new Object();
/*     */ 
/*     */     WritableByteChannelImpl(OutputStream paramOutputStream) {
/* 443 */       this.out = paramOutputStream;
/*     */     }
/*     */ 
/*     */     public int write(ByteBuffer paramByteBuffer) throws IOException {
/* 447 */       int i = paramByteBuffer.remaining();
/* 448 */       int j = 0;
/* 449 */       synchronized (this.writeLock) {
/* 450 */         while (j < i) {
/* 451 */           int k = Math.min(i - j, 8192);
/*     */ 
/* 453 */           if (this.buf.length < k)
/* 454 */             this.buf = new byte[k];
/* 455 */           paramByteBuffer.get(this.buf, 0, k);
/*     */           try {
/* 457 */             begin();
/* 458 */             this.out.write(this.buf, 0, k);
/*     */           } finally {
/* 460 */             end(k > 0);
/*     */           }
/* 462 */           j += k;
/*     */         }
/* 464 */         return j;
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void implCloseChannel() throws IOException {
/* 469 */       this.out.close();
/* 470 */       this.open = false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.Channels
 * JD-Core Version:    0.6.2
 */