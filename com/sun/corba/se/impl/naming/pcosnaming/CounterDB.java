/*     */ package com.sun.corba.se.impl.naming.pcosnaming;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ class CounterDB
/*     */   implements Serializable
/*     */ {
/*     */   private Integer counter;
/* 256 */   private static String counterFileName = "counter";
/*     */   private transient File counterFile;
/*     */   public static final int rootCounter = 0;
/*     */ 
/*     */   CounterDB(File paramFile)
/*     */   {
/* 208 */     counterFileName = "counter";
/* 209 */     this.counterFile = new File(paramFile, counterFileName);
/* 210 */     if (!this.counterFile.exists()) {
/* 211 */       this.counter = new Integer(0);
/* 212 */       writeCounter();
/*     */     } else {
/* 214 */       readCounter();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readCounter()
/*     */   {
/*     */     try {
/* 221 */       FileInputStream localFileInputStream = new FileInputStream(this.counterFile);
/* 222 */       ObjectInputStream localObjectInputStream = new ObjectInputStream(localFileInputStream);
/* 223 */       this.counter = ((Integer)localObjectInputStream.readObject());
/* 224 */       localObjectInputStream.close();
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeCounter() {
/*     */     try {
/* 232 */       this.counterFile.delete();
/* 233 */       FileOutputStream localFileOutputStream = new FileOutputStream(this.counterFile);
/* 234 */       ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localFileOutputStream);
/* 235 */       localObjectOutputStream.writeObject(this.counter);
/* 236 */       localObjectOutputStream.flush();
/* 237 */       localObjectOutputStream.close();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized int getNextCounter() {
/* 245 */     int i = this.counter.intValue();
/* 246 */     this.counter = new Integer(++i);
/* 247 */     writeCounter();
/*     */ 
/* 249 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.pcosnaming.CounterDB
 * JD-Core Version:    0.6.2
 */