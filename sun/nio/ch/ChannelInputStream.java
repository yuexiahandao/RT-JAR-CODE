/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.IllegalBlockingModeException;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ 
/*     */ public class ChannelInputStream extends InputStream
/*     */ {
/*     */   protected final ReadableByteChannel ch;
/*  70 */   private ByteBuffer bb = null;
/*  71 */   private byte[] bs = null;
/*  72 */   private byte[] b1 = null;
/*     */ 
/*     */   public static int read(ReadableByteChannel paramReadableByteChannel, ByteBuffer paramByteBuffer, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  51 */     if ((paramReadableByteChannel instanceof SelectableChannel)) {
/*  52 */       SelectableChannel localSelectableChannel = (SelectableChannel)paramReadableByteChannel;
/*  53 */       synchronized (localSelectableChannel.blockingLock()) {
/*  54 */         boolean bool = localSelectableChannel.isBlocking();
/*  55 */         if (!bool)
/*  56 */           throw new IllegalBlockingModeException();
/*  57 */         if (bool != paramBoolean)
/*  58 */           localSelectableChannel.configureBlocking(paramBoolean);
/*  59 */         int i = paramReadableByteChannel.read(paramByteBuffer);
/*  60 */         if (bool != paramBoolean)
/*  61 */           localSelectableChannel.configureBlocking(bool);
/*  62 */         return i;
/*     */       }
/*     */     }
/*  65 */     return paramReadableByteChannel.read(paramByteBuffer);
/*     */   }
/*     */ 
/*     */   public ChannelInputStream(ReadableByteChannel paramReadableByteChannel)
/*     */   {
/*  75 */     this.ch = paramReadableByteChannel;
/*     */   }
/*     */ 
/*     */   public synchronized int read() throws IOException {
/*  79 */     if (this.b1 == null)
/*  80 */       this.b1 = new byte[1];
/*  81 */     int i = read(this.b1);
/*  82 */     if (i == 1)
/*  83 */       return this.b1[0] & 0xFF;
/*  84 */     return -1;
/*     */   }
/*     */ 
/*     */   public synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  90 */     if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0))
/*     */     {
/*  92 */       throw new IndexOutOfBoundsException();
/*  93 */     }if (paramInt2 == 0) {
/*  94 */       return 0;
/*     */     }
/*  96 */     ByteBuffer localByteBuffer = this.bs == paramArrayOfByte ? this.bb : ByteBuffer.wrap(paramArrayOfByte);
/*     */ 
/*  99 */     localByteBuffer.limit(Math.min(paramInt1 + paramInt2, localByteBuffer.capacity()));
/* 100 */     localByteBuffer.position(paramInt1);
/* 101 */     this.bb = localByteBuffer;
/* 102 */     this.bs = paramArrayOfByte;
/* 103 */     return read(localByteBuffer);
/*     */   }
/*     */ 
/*     */   protected int read(ByteBuffer paramByteBuffer)
/*     */     throws IOException
/*     */   {
/* 109 */     return read(this.ch, paramByteBuffer, true);
/*     */   }
/*     */ 
/*     */   public int available() throws IOException
/*     */   {
/* 114 */     if ((this.ch instanceof SeekableByteChannel)) {
/* 115 */       SeekableByteChannel localSeekableByteChannel = (SeekableByteChannel)this.ch;
/* 116 */       long l = Math.max(0L, localSeekableByteChannel.size() - localSeekableByteChannel.position());
/* 117 */       return l > 2147483647L ? 2147483647 : (int)l;
/*     */     }
/* 119 */     return 0;
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 123 */     this.ch.close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.ChannelInputStream
 * JD-Core Version:    0.6.2
 */