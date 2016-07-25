/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.Pipe;
/*     */ import java.nio.channels.Pipe.SinkChannel;
/*     */ import java.nio.channels.Pipe.SourceChannel;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Random;
/*     */ 
/*     */ class PipeImpl extends Pipe
/*     */ {
/*     */   private Pipe.SourceChannel source;
/*     */   private Pipe.SinkChannel sink;
/*     */   private static final Random rnd;
/*     */ 
/*     */   PipeImpl(SelectorProvider paramSelectorProvider)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 141 */       AccessController.doPrivileged(new Initializer(paramSelectorProvider, null));
/*     */     } catch (PrivilegedActionException localPrivilegedActionException) {
/* 143 */       throw ((IOException)localPrivilegedActionException.getCause());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Pipe.SourceChannel source()
/*     */   {
/* 149 */     return this.source;
/*     */   }
/*     */ 
/*     */   public Pipe.SinkChannel sink() {
/* 153 */     return this.sink;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  59 */     Util.load();
/*  60 */     byte[] arrayOfByte = new byte[8];
/*  61 */     boolean bool = IOUtil.randomBytes(arrayOfByte);
/*  62 */     if (bool)
/*  63 */       rnd = new Random(ByteBuffer.wrap(arrayOfByte).getLong());
/*     */     else
/*  65 */       rnd = new Random();
/*     */   }
/*     */ 
/*     */   private class Initializer
/*     */     implements PrivilegedExceptionAction<Void>
/*     */   {
/*     */     private final SelectorProvider sp;
/*     */ 
/*     */     private Initializer(SelectorProvider arg2)
/*     */     {
/*     */       Object localObject;
/*  76 */       this.sp = localObject;
/*     */     }
/*     */ 
/*     */     public Void run() throws IOException {
/*  80 */       ServerSocketChannel localServerSocketChannel = null;
/*  81 */       SocketChannel localSocketChannel1 = null;
/*  82 */       SocketChannel localSocketChannel2 = null;
/*     */       try
/*     */       {
/*  86 */         InetAddress localInetAddress = InetAddress.getByName("127.0.0.1");
/*  87 */         assert (localInetAddress.isLoopbackAddress());
/*     */ 
/*  90 */         localServerSocketChannel = ServerSocketChannel.open();
/*  91 */         localServerSocketChannel.socket().bind(new InetSocketAddress(localInetAddress, 0));
/*     */ 
/*  95 */         InetSocketAddress localInetSocketAddress = new InetSocketAddress(localInetAddress, localServerSocketChannel.socket().getLocalPort());
/*     */ 
/*  97 */         localSocketChannel1 = SocketChannel.open(localInetSocketAddress);
/*     */ 
/*  99 */         ByteBuffer localByteBuffer = ByteBuffer.allocate(8);
/* 100 */         long l = PipeImpl.rnd.nextLong();
/* 101 */         localByteBuffer.putLong(l).flip();
/* 102 */         localSocketChannel1.write(localByteBuffer);
/*     */         while (true)
/*     */         {
/* 106 */           localSocketChannel2 = localServerSocketChannel.accept();
/* 107 */           localByteBuffer.clear();
/* 108 */           localSocketChannel2.read(localByteBuffer);
/* 109 */           localByteBuffer.rewind();
/* 110 */           if (localByteBuffer.getLong() == l)
/*     */             break;
/* 112 */           localSocketChannel2.close();
/*     */         }
/*     */ 
/* 116 */         PipeImpl.this.source = new SourceChannelImpl(this.sp, localSocketChannel1);
/* 117 */         PipeImpl.this.sink = new SinkChannelImpl(this.sp, localSocketChannel2);
/*     */       } catch (IOException localIOException2) {
/*     */         try {
/* 120 */           if (localSocketChannel1 != null)
/* 121 */             localSocketChannel1.close();
/* 122 */           if (localSocketChannel2 != null)
/* 123 */             localSocketChannel2.close(); 
/*     */         } catch (IOException localIOException3) {  }
/*     */ 
/* 125 */         IOException localIOException4 = new IOException("Unable to establish loopback connection");
/*     */ 
/* 127 */         localIOException4.initCause(localIOException2);
/* 128 */         throw localIOException4;
/*     */       } finally {
/*     */         try {
/* 131 */           if (localServerSocketChannel != null)
/* 132 */             localServerSocketChannel.close(); 
/*     */         } catch (IOException localIOException5) {  }
/*     */ 
/*     */       }
/* 135 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.PipeImpl
 * JD-Core Version:    0.6.2
 */