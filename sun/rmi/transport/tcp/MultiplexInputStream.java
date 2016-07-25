/*     */ package sun.rmi.transport.tcp;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ final class MultiplexInputStream extends InputStream
/*     */ {
/*     */   private ConnectionMultiplexer manager;
/*     */   private MultiplexConnectionInfo info;
/*     */   private byte[] buffer;
/*  49 */   private int present = 0;
/*     */ 
/*  52 */   private int pos = 0;
/*     */ 
/*  55 */   private int requested = 0;
/*     */ 
/*  58 */   private boolean disconnected = false;
/*     */ 
/*  69 */   private Object lock = new Object();
/*     */   private int waterMark;
/*  75 */   private byte[] temp = new byte[1];
/*     */ 
/*     */   MultiplexInputStream(ConnectionMultiplexer paramConnectionMultiplexer, MultiplexConnectionInfo paramMultiplexConnectionInfo, int paramInt)
/*     */   {
/*  88 */     this.manager = paramConnectionMultiplexer;
/*  89 */     this.info = paramMultiplexConnectionInfo;
/*     */ 
/*  91 */     this.buffer = new byte[paramInt];
/*  92 */     this.waterMark = (paramInt / 2);
/*     */   }
/*     */ 
/*     */   public synchronized int read()
/*     */     throws IOException
/*     */   {
/* 100 */     int i = read(this.temp, 0, 1);
/* 101 */     if (i != 1)
/* 102 */       return -1;
/* 103 */     return this.temp[0] & 0xFF;
/*     */   }
/*     */ 
/*     */   public synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 116 */     if (paramInt2 <= 0)
/* 117 */       return 0;
/*     */     int i;
/* 120 */     synchronized (this.lock) {
/* 121 */       if (this.pos >= this.present) {
/* 122 */         this.pos = (this.present = 0);
/* 123 */       } else if (this.pos >= this.waterMark) {
/* 124 */         System.arraycopy(this.buffer, this.pos, this.buffer, 0, this.present - this.pos);
/* 125 */         this.present -= this.pos;
/* 126 */         this.pos = 0;
/*     */       }
/* 128 */       int j = this.buffer.length - this.present;
/* 129 */       i = Math.max(j - this.requested, 0);
/*     */     }
/* 131 */     if (i > 0)
/* 132 */       this.manager.sendRequest(this.info, i);
/* 133 */     synchronized (this.lock) {
/* 134 */       this.requested += i;
/* 135 */       while ((this.pos >= this.present) && (!this.disconnected))
/*     */         try {
/* 137 */           this.lock.wait();
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {
/*     */         }
/* 141 */       if ((this.disconnected) && (this.pos >= this.present)) {
/* 142 */         return -1;
/*     */       }
/* 144 */       int k = this.present - this.pos;
/* 145 */       if (paramInt2 < k) {
/* 146 */         System.arraycopy(this.buffer, this.pos, paramArrayOfByte, paramInt1, paramInt2);
/* 147 */         this.pos += paramInt2;
/* 148 */         return paramInt2;
/*     */       }
/*     */ 
/* 151 */       System.arraycopy(this.buffer, this.pos, paramArrayOfByte, paramInt1, k);
/* 152 */       this.pos = (this.present = 0);
/*     */ 
/* 154 */       return k;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 164 */     synchronized (this.lock) {
/* 165 */       return this.present - this.pos;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 174 */     this.manager.sendClose(this.info);
/*     */   }
/*     */ 
/*     */   void receive(int paramInt, DataInputStream paramDataInputStream)
/*     */     throws IOException
/*     */   {
/* 188 */     synchronized (this.lock) {
/* 189 */       if ((this.pos > 0) && (this.buffer.length - this.present < paramInt)) {
/* 190 */         System.arraycopy(this.buffer, this.pos, this.buffer, 0, this.present - this.pos);
/* 191 */         this.present -= this.pos;
/* 192 */         this.pos = 0;
/*     */       }
/* 194 */       if (this.buffer.length - this.present < paramInt)
/* 195 */         throw new IOException("Receive buffer overflow");
/* 196 */       paramDataInputStream.readFully(this.buffer, this.present, paramInt);
/* 197 */       this.present += paramInt;
/* 198 */       this.requested -= paramInt;
/* 199 */       this.lock.notifyAll();
/*     */     }
/*     */   }
/*     */ 
/*     */   void disconnect()
/*     */   {
/* 208 */     synchronized (this.lock) {
/* 209 */       this.disconnected = true;
/* 210 */       this.lock.notifyAll();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.tcp.MultiplexInputStream
 * JD-Core Version:    0.6.2
 */