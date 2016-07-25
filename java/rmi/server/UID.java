/*     */ package java.rmi.server;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.security.SecureRandom;
/*     */ 
/*     */ public final class UID
/*     */   implements Serializable
/*     */ {
/*     */   private static int hostUnique;
/*  74 */   private static boolean hostUniqueSet = false;
/*     */ 
/*  76 */   private static final Object lock = new Object();
/*  77 */   private static long lastTime = System.currentTimeMillis();
/*  78 */   private static short lastCount = -32768;
/*     */   private static final long serialVersionUID = 1086053664494604050L;
/*     */   private final int unique;
/*     */   private final long time;
/*     */   private final short count;
/*     */ 
/*     */   public UID()
/*     */   {
/* 110 */     synchronized (lock) {
/* 111 */       if (!hostUniqueSet) {
/* 112 */         hostUnique = new SecureRandom().nextInt();
/* 113 */         hostUniqueSet = true;
/*     */       }
/* 115 */       this.unique = hostUnique;
/* 116 */       if (lastCount == 32767) {
/* 117 */         boolean bool = Thread.interrupted();
/* 118 */         int i = 0;
/* 119 */         while (i == 0) {
/* 120 */           long l = System.currentTimeMillis();
/* 121 */           if (l == lastTime)
/*     */           {
/*     */             try {
/* 124 */               Thread.sleep(1L);
/*     */             } catch (InterruptedException localInterruptedException) {
/* 126 */               bool = true;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 131 */             lastTime = l < lastTime ? lastTime + 1L : l;
/* 132 */             lastCount = -32768;
/* 133 */             i = 1;
/*     */           }
/*     */         }
/* 136 */         if (bool) {
/* 137 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       }
/* 140 */       this.time = lastTime;
/* 141 */       this.count = (lastCount++);
/*     */     }
/*     */   }
/*     */ 
/*     */   public UID(short paramShort)
/*     */   {
/* 157 */     this.unique = 0;
/* 158 */     this.time = 0L;
/* 159 */     this.count = paramShort;
/*     */   }
/*     */ 
/*     */   private UID(int paramInt, long paramLong, short paramShort)
/*     */   {
/* 166 */     this.unique = paramInt;
/* 167 */     this.time = paramLong;
/* 168 */     this.count = paramShort;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 177 */     return (int)this.time + this.count;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 195 */     if ((paramObject instanceof UID)) {
/* 196 */       UID localUID = (UID)paramObject;
/* 197 */       return (this.unique == localUID.unique) && (this.count == localUID.count) && (this.time == localUID.time);
/*     */     }
/*     */ 
/* 201 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 211 */     return Integer.toString(this.unique, 16) + ":" + Long.toString(this.time, 16) + ":" + Integer.toString(this.count, 16);
/*     */   }
/*     */ 
/*     */   public void write(DataOutput paramDataOutput)
/*     */     throws IOException
/*     */   {
/* 235 */     paramDataOutput.writeInt(this.unique);
/* 236 */     paramDataOutput.writeLong(this.time);
/* 237 */     paramDataOutput.writeShort(this.count);
/*     */   }
/*     */ 
/*     */   public static UID read(DataInput paramDataInput)
/*     */     throws IOException
/*     */   {
/* 264 */     int i = paramDataInput.readInt();
/* 265 */     long l = paramDataInput.readLong();
/* 266 */     short s = paramDataInput.readShort();
/* 267 */     return new UID(i, l, s);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.UID
 * JD-Core Version:    0.6.2
 */