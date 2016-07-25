/*     */ package com.sun.xml.internal.org.jvnet.mimepull;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ final class WeakDataFile extends WeakReference<DataFile>
/*     */ {
/*  45 */   private static final Logger LOGGER = Logger.getLogger(WeakDataFile.class.getName());
/*     */   private static final int MAX_ITERATIONS = 2;
/*  47 */   private static ReferenceQueue<DataFile> refQueue = new ReferenceQueue();
/*  48 */   private static List<WeakDataFile> refList = new ArrayList();
/*     */   private final File file;
/*     */   private final RandomAccessFile raf;
/*     */ 
/*     */   WeakDataFile(DataFile df, File file)
/*     */   {
/*  53 */     super(df, refQueue);
/*  54 */     refList.add(this);
/*  55 */     this.file = file;
/*     */     try {
/*  57 */       this.raf = new RandomAccessFile(file, "rw");
/*     */     } catch (IOException ioe) {
/*  59 */       throw new MIMEParsingException(ioe);
/*     */     }
/*  61 */     drainRefQueueBounded();
/*     */   }
/*     */ 
/*     */   synchronized void read(long pointer, byte[] buf, int offset, int length) {
/*     */     try {
/*  66 */       this.raf.seek(pointer);
/*  67 */       this.raf.readFully(buf, offset, length);
/*     */     } catch (IOException ioe) {
/*  69 */       throw new MIMEParsingException(ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized long writeTo(long pointer, byte[] data, int offset, int length) {
/*     */     try {
/*  75 */       this.raf.seek(pointer);
/*  76 */       this.raf.write(data, offset, length);
/*  77 */       return this.raf.getFilePointer();
/*     */     } catch (IOException ioe) {
/*  79 */       throw new MIMEParsingException(ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   void close() {
/*  84 */     LOGGER.fine("Deleting file = " + this.file.getName());
/*  85 */     refList.remove(this);
/*     */     try {
/*  87 */       this.raf.close();
/*  88 */       this.file.delete();
/*     */     } catch (IOException ioe) {
/*  90 */       throw new MIMEParsingException(ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   void renameTo(File f) {
/*  95 */     LOGGER.fine("Moving file=" + this.file + " to=" + f);
/*  96 */     refList.remove(this);
/*     */     try {
/*  98 */       this.raf.close();
/*  99 */       this.file.renameTo(f);
/*     */     } catch (IOException ioe) {
/* 101 */       throw new MIMEParsingException(ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void drainRefQueueBounded()
/*     */   {
/* 107 */     int iterations = 0;
/* 108 */     WeakDataFile weak = (WeakDataFile)refQueue.poll();
/* 109 */     while ((weak != null) && (iterations < 2)) {
/* 110 */       LOGGER.fine("Cleaning file = " + weak.file + " from reference queue.");
/* 111 */       weak.close();
/* 112 */       iterations++;
/* 113 */       weak = (WeakDataFile)refQueue.poll();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.WeakDataFile
 * JD-Core Version:    0.6.2
 */