/*     */ package java.net;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.FileChannel;
/*     */ import sun.misc.IoTrace;
/*     */ import sun.net.ConnectionResetException;
/*     */ 
/*     */ class SocketOutputStream extends FileOutputStream
/*     */ {
/*  49 */   private AbstractPlainSocketImpl impl = null;
/*  50 */   private byte[] temp = new byte[1];
/*  51 */   private Socket socket = null;
/*     */ 
/* 165 */   private boolean closing = false;
/*     */ 
/*     */   SocketOutputStream(AbstractPlainSocketImpl paramAbstractPlainSocketImpl)
/*     */     throws IOException
/*     */   {
/*  60 */     super(paramAbstractPlainSocketImpl.getFileDescriptor());
/*  61 */     this.impl = paramAbstractPlainSocketImpl;
/*  62 */     this.socket = paramAbstractPlainSocketImpl.getSocket();
/*     */   }
/*     */ 
/*     */   public final FileChannel getChannel()
/*     */   {
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */   private native void socketWrite0(FileDescriptor paramFileDescriptor, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   private void socketWrite(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 102 */     if ((paramInt2 <= 0) || (paramInt1 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length)) {
/* 103 */       if (paramInt2 == 0) {
/* 104 */         return;
/*     */       }
/* 106 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/*     */ 
/* 109 */     Object localObject1 = IoTrace.socketWriteBegin();
/* 110 */     int i = 0;
/* 111 */     FileDescriptor localFileDescriptor = this.impl.acquireFD();
/*     */     try {
/* 113 */       socketWrite0(localFileDescriptor, paramArrayOfByte, paramInt1, paramInt2);
/* 114 */       i = paramInt2;
/*     */     }
/*     */     catch (SocketException localSocketException1)
/*     */     {
/*     */       SocketException localSocketException2;
/* 116 */       if ((localSocketException1 instanceof ConnectionResetException)) {
/* 117 */         this.impl.setConnectionResetPending();
/* 118 */         localSocketException2 = new SocketException("Connection reset");
/*     */       }
/* 120 */       if (this.impl.isClosedOrPending()) {
/* 121 */         throw new SocketException("Socket closed");
/*     */       }
/* 123 */       throw localSocketException2;
/*     */     }
/*     */     finally {
/* 126 */       this.impl.releaseFD();
/* 127 */       IoTrace.socketWriteEnd(localObject1, this.impl.address, this.impl.port, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/* 137 */     this.temp[0] = ((byte)paramInt);
/* 138 */     socketWrite(this.temp, 0, 1);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 147 */     socketWrite(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 159 */     socketWrite(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 168 */     if (this.closing)
/* 169 */       return;
/* 170 */     this.closing = true;
/* 171 */     if (this.socket != null) {
/* 172 */       if (!this.socket.isClosed())
/* 173 */         this.socket.close();
/*     */     }
/* 175 */     else this.impl.close();
/* 176 */     this.closing = false;
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */   {
/*     */   }
/*     */ 
/*     */   private static native void init();
/*     */ 
/*     */   static
/*     */   {
/*  46 */     init();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.SocketOutputStream
 * JD-Core Version:    0.6.2
 */