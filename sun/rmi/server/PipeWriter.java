/*      */ package sun.rmi.server;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.security.AccessController;
/*      */ import java.util.Date;
/*      */ import sun.rmi.runtime.NewThreadAction;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ class PipeWriter
/*      */   implements Runnable
/*      */ {
/*      */   private ByteArrayOutputStream bufOut;
/*      */   private int cLast;
/*      */   private byte[] currSep;
/*      */   private PrintWriter out;
/*      */   private InputStream in;
/*      */   private String pipeString;
/*      */   private String execString;
/* 2357 */   private static String lineSeparator = (String)AccessController.doPrivileged(new GetPropertyAction("line.separator"));
/*      */ 
/* 2359 */   private static int lineSeparatorLength = lineSeparator.length();
/*      */ 
/* 2354 */   private static int numExecs = 0;
/*      */ 
/*      */   private PipeWriter(InputStream paramInputStream, OutputStream paramOutputStream, String paramString, int paramInt)
/*      */   {
/* 2376 */     this.in = paramInputStream;
/* 2377 */     this.out = new PrintWriter(paramOutputStream);
/*      */ 
/* 2379 */     this.bufOut = new ByteArrayOutputStream();
/* 2380 */     this.currSep = new byte[lineSeparatorLength];
/*      */ 
/* 2383 */     this.execString = (":ExecGroup-" + Integer.toString(paramInt) + ':' + paramString + ':');
/*      */   }
/*      */ 
/*      */   public void run()
/*      */   {
/* 2395 */     byte[] arrayOfByte = new byte[256];
/*      */     try
/*      */     {
/*      */       int i;
/* 2400 */       while ((i = this.in.read(arrayOfByte)) != -1) {
/* 2401 */         write(arrayOfByte, 0, i);
/*      */       }
/*      */ 
/* 2408 */       String str = this.bufOut.toString();
/* 2409 */       this.bufOut.reset();
/* 2410 */       if (str.length() > 0) {
/* 2411 */         this.out.println(createAnnotation() + str);
/* 2412 */         this.out.flush();
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/* 2425 */     if (paramInt2 < 0) {
/* 2426 */       throw new ArrayIndexOutOfBoundsException(paramInt2);
/*      */     }
/* 2428 */     for (int i = 0; i < paramInt2; i++)
/* 2429 */       write(paramArrayOfByte[(paramInt1 + i)]);
/*      */   }
/*      */ 
/*      */   private void write(byte paramByte)
/*      */     throws IOException
/*      */   {
/* 2441 */     int i = 0;
/*      */ 
/* 2444 */     for (i = 1; i < this.currSep.length; i++) {
/* 2445 */       this.currSep[(i - 1)] = this.currSep[i];
/*      */     }
/* 2447 */     this.currSep[(i - 1)] = paramByte;
/* 2448 */     this.bufOut.write(paramByte);
/*      */ 
/* 2451 */     if ((this.cLast >= lineSeparatorLength - 1) && (lineSeparator.equals(new String(this.currSep))))
/*      */     {
/* 2454 */       this.cLast = 0;
/*      */ 
/* 2457 */       this.out.print(createAnnotation() + this.bufOut.toString());
/* 2458 */       this.out.flush();
/* 2459 */       this.bufOut.reset();
/*      */ 
/* 2461 */       if (this.out.checkError()) {
/* 2462 */         throw new IOException("PipeWriter: IO Exception when writing to output stream.");
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 2468 */       this.cLast += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private String createAnnotation()
/*      */   {
/* 2481 */     return new Date().toString() + this.execString;
/*      */   }
/*      */ 
/*      */   static void plugTogetherPair(InputStream paramInputStream1, OutputStream paramOutputStream1, InputStream paramInputStream2, OutputStream paramOutputStream2)
/*      */   {
/* 2502 */     Thread localThread1 = null;
/* 2503 */     Thread localThread2 = null;
/*      */ 
/* 2505 */     int i = getNumExec();
/*      */ 
/* 2508 */     localThread1 = (Thread)AccessController.doPrivileged(new NewThreadAction(new PipeWriter(paramInputStream1, paramOutputStream1, "out", i), "out", true));
/*      */ 
/* 2511 */     localThread2 = (Thread)AccessController.doPrivileged(new NewThreadAction(new PipeWriter(paramInputStream2, paramOutputStream2, "err", i), "err", true));
/*      */ 
/* 2514 */     localThread1.start();
/* 2515 */     localThread2.start();
/*      */   }
/*      */ 
/*      */   private static synchronized int getNumExec() {
/* 2519 */     return numExecs++;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.server.PipeWriter
 * JD-Core Version:    0.6.2
 */