/*     */ package java.io;
/*     */ 
/*     */ public class PipedWriter extends Writer
/*     */ {
/*     */   private PipedReader sink;
/*  48 */   private boolean closed = false;
/*     */ 
/*     */   public PipedWriter(PipedReader paramPipedReader)
/*     */     throws IOException
/*     */   {
/*  59 */     connect(paramPipedReader);
/*     */   }
/*     */ 
/*     */   public PipedWriter()
/*     */   {
/*     */   }
/*     */ 
/*     */   public synchronized void connect(PipedReader paramPipedReader)
/*     */     throws IOException
/*     */   {
/*  92 */     if (paramPipedReader == null)
/*  93 */       throw new NullPointerException();
/*  94 */     if ((this.sink != null) || (paramPipedReader.connected))
/*  95 */       throw new IOException("Already connected");
/*  96 */     if ((paramPipedReader.closedByReader) || (this.closed)) {
/*  97 */       throw new IOException("Pipe closed");
/*     */     }
/*     */ 
/* 100 */     this.sink = paramPipedReader;
/* 101 */     paramPipedReader.in = -1;
/* 102 */     paramPipedReader.out = 0;
/* 103 */     paramPipedReader.connected = true;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/* 121 */     if (this.sink == null) {
/* 122 */       throw new IOException("Pipe not connected");
/*     */     }
/* 124 */     this.sink.receive(paramInt);
/*     */   }
/*     */ 
/*     */   public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 145 */     if (this.sink == null)
/* 146 */       throw new IOException("Pipe not connected");
/* 147 */     if ((paramInt1 | paramInt2 | paramInt1 + paramInt2 | paramArrayOfChar.length - (paramInt1 + paramInt2)) < 0) {
/* 148 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 150 */     this.sink.receive(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public synchronized void flush()
/*     */     throws IOException
/*     */   {
/* 161 */     if (this.sink != null) {
/* 162 */       if ((this.sink.closedByReader) || (this.closed)) {
/* 163 */         throw new IOException("Pipe closed");
/*     */       }
/* 165 */       synchronized (this.sink) {
/* 166 */         this.sink.notifyAll();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 179 */     this.closed = true;
/* 180 */     if (this.sink != null)
/* 181 */       this.sink.receivedLast();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.PipedWriter
 * JD-Core Version:    0.6.2
 */