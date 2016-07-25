/*     */ package sun.rmi.transport.proxy;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ class HttpSendInputStream extends FilterInputStream
/*     */ {
/*     */   HttpSendSocket owner;
/*     */ 
/*     */   public HttpSendInputStream(InputStream paramInputStream, HttpSendSocket paramHttpSendSocket)
/*     */     throws IOException
/*     */   {
/*  48 */     super(paramInputStream);
/*     */ 
/*  50 */     this.owner = paramHttpSendSocket;
/*     */   }
/*     */ 
/*     */   public void deactivate()
/*     */   {
/*  60 */     this.in = null;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  68 */     if (this.in == null)
/*  69 */       this.in = this.owner.readNotify();
/*  70 */     return this.in.read();
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  81 */     if (paramInt2 == 0)
/*  82 */       return 0;
/*  83 */     if (this.in == null)
/*  84 */       this.in = this.owner.readNotify();
/*  85 */     return this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/*  94 */     if (paramLong == 0L)
/*  95 */       return 0L;
/*  96 */     if (this.in == null)
/*  97 */       this.in = this.owner.readNotify();
/*  98 */     return this.in.skip(paramLong);
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 106 */     if (this.in == null)
/* 107 */       this.in = this.owner.readNotify();
/* 108 */     return this.in.available();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 116 */     this.owner.close();
/*     */   }
/*     */ 
/*     */   public synchronized void mark(int paramInt)
/*     */   {
/* 125 */     if (this.in == null) {
/*     */       try {
/* 127 */         this.in = this.owner.readNotify();
/*     */       }
/*     */       catch (IOException localIOException) {
/* 130 */         return;
/*     */       }
/*     */     }
/* 133 */     this.in.mark(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */     throws IOException
/*     */   {
/* 141 */     if (this.in == null)
/* 142 */       this.in = this.owner.readNotify();
/* 143 */     this.in.reset();
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 151 */     if (this.in == null) {
/*     */       try {
/* 153 */         this.in = this.owner.readNotify();
/*     */       }
/*     */       catch (IOException localIOException) {
/* 156 */         return false;
/*     */       }
/*     */     }
/* 159 */     return this.in.markSupported();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.HttpSendInputStream
 * JD-Core Version:    0.6.2
 */