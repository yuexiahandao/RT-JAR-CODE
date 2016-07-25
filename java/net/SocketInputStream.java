/*     */ package java.net;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.FileChannel;
/*     */ import sun.misc.IoTrace;
/*     */ import sun.net.ConnectionResetException;
/*     */ 
/*     */ class SocketInputStream extends FileInputStream
/*     */ {
/*     */   private boolean eof;
/*  51 */   private AbstractPlainSocketImpl impl = null;
/*     */   private byte[] temp;
/*  53 */   private Socket socket = null;
/*     */ 
/* 251 */   private boolean closing = false;
/*     */ 
/*     */   SocketInputStream(AbstractPlainSocketImpl paramAbstractPlainSocketImpl)
/*     */     throws IOException
/*     */   {
/*  62 */     super(paramAbstractPlainSocketImpl.getFileDescriptor());
/*  63 */     this.impl = paramAbstractPlainSocketImpl;
/*  64 */     this.socket = paramAbstractPlainSocketImpl.getSocket();
/*     */   }
/*     */ 
/*     */   public final FileChannel getChannel()
/*     */   {
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */   private native int socketRead0(FileDescriptor paramFileDescriptor, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws IOException;
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 108 */     return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 122 */     return read(paramArrayOfByte, paramInt1, paramInt2, this.impl.getTimeout());
/*     */   }
/*     */ 
/*     */   int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3) throws IOException {
/* 126 */     ConnectionResetException localConnectionResetException1 = 0;
/*     */ 
/* 129 */     if (this.eof) {
/* 130 */       return -1;
/*     */     }
/*     */ 
/* 134 */     if (this.impl.isConnectionReset()) {
/* 135 */       throw new SocketException("Connection reset");
/*     */     }
/*     */ 
/* 139 */     if ((paramInt2 <= 0) || (paramInt1 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length)) {
/* 140 */       if (paramInt2 == 0) {
/* 141 */         return 0;
/*     */       }
/* 143 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/*     */ 
/* 146 */     int i = 0;
/*     */ 
/* 148 */     Object localObject1 = IoTrace.socketReadBegin();
/*     */ 
/* 150 */     FileDescriptor localFileDescriptor = this.impl.acquireFD();
/*     */     try {
/* 152 */       localConnectionResetException1 = socketRead0(localFileDescriptor, paramArrayOfByte, paramInt1, paramInt2, paramInt3);
/* 153 */       if (localConnectionResetException1 > 0)
/* 154 */         return localConnectionResetException1;
/*     */     }
/*     */     catch (ConnectionResetException localConnectionResetException2) {
/* 157 */       i = 1;
/*     */     } finally {
/* 159 */       this.impl.releaseFD();
/* 160 */       IoTrace.socketReadEnd(localObject1, this.impl.address, this.impl.port, paramInt3, localConnectionResetException1 > 0 ? localConnectionResetException1 : 0L);
/*     */     }
/*     */ 
/* 168 */     if (i != 0) {
/* 169 */       localObject1 = IoTrace.socketReadBegin();
/* 170 */       this.impl.setConnectionResetPending();
/* 171 */       this.impl.acquireFD();
/*     */       try {
/* 173 */         localConnectionResetException1 = socketRead0(localFileDescriptor, paramArrayOfByte, paramInt1, paramInt2, paramInt3);
/* 174 */         if (localConnectionResetException1 > 0)
/* 175 */           return localConnectionResetException1;
/*     */       } catch (ConnectionResetException localConnectionResetException3) {
/*     */       }
/*     */       finally {
/* 179 */         this.impl.releaseFD();
/* 180 */         IoTrace.socketReadEnd(localObject1, this.impl.address, this.impl.port, paramInt3, localConnectionResetException1 > 0 ? localConnectionResetException1 : 0L);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 189 */     if (this.impl.isClosedOrPending()) {
/* 190 */       throw new SocketException("Socket closed");
/*     */     }
/* 192 */     if (this.impl.isConnectionResetPending()) {
/* 193 */       this.impl.setConnectionReset();
/*     */     }
/* 195 */     if (this.impl.isConnectionReset()) {
/* 196 */       throw new SocketException("Connection reset");
/*     */     }
/* 198 */     this.eof = true;
/* 199 */     return -1;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 206 */     if (this.eof) {
/* 207 */       return -1;
/*     */     }
/* 209 */     this.temp = new byte[1];
/* 210 */     int i = read(this.temp, 0, 1);
/* 211 */     if (i <= 0) {
/* 212 */       return -1;
/*     */     }
/* 214 */     return this.temp[0] & 0xFF;
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 224 */     if (paramLong <= 0L) {
/* 225 */       return 0L;
/*     */     }
/* 227 */     long l = paramLong;
/* 228 */     int i = (int)Math.min(1024L, l);
/* 229 */     byte[] arrayOfByte = new byte[i];
/* 230 */     while (l > 0L) {
/* 231 */       int j = read(arrayOfByte, 0, (int)Math.min(i, l));
/* 232 */       if (j < 0) {
/*     */         break;
/*     */       }
/* 235 */       l -= j;
/*     */     }
/* 237 */     return paramLong - l;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 245 */     return this.impl.available();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 254 */     if (this.closing)
/* 255 */       return;
/* 256 */     this.closing = true;
/* 257 */     if (this.socket != null) {
/* 258 */       if (!this.socket.isClosed())
/* 259 */         this.socket.close();
/*     */     }
/* 261 */     else this.impl.close();
/* 262 */     this.closing = false;
/*     */   }
/*     */ 
/*     */   void setEOF(boolean paramBoolean) {
/* 266 */     this.eof = paramBoolean;
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
/*  47 */     init();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.SocketInputStream
 * JD-Core Version:    0.6.2
 */