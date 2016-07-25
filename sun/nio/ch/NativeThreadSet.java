/*    */ package sun.nio.ch;
/*    */ 
/*    */ class NativeThreadSet
/*    */ {
/*    */   private long[] elts;
/* 35 */   private int used = 0;
/*    */   private boolean waitingToEmpty;
/*    */ 
/*    */   NativeThreadSet(int paramInt)
/*    */   {
/* 39 */     this.elts = new long[paramInt];
/*    */   }
/*    */ 
/*    */   int add()
/*    */   {
/* 46 */     long l = NativeThread.current();
/*    */ 
/* 48 */     if (l == 0L)
/* 49 */       l = -1L;
/* 50 */     synchronized (this) {
/* 51 */       int i = 0;
/* 52 */       if (this.used >= this.elts.length) {
/* 53 */         j = this.elts.length;
/* 54 */         int k = j * 2;
/* 55 */         long[] arrayOfLong = new long[k];
/* 56 */         System.arraycopy(this.elts, 0, arrayOfLong, 0, j);
/* 57 */         this.elts = arrayOfLong;
/* 58 */         i = j;
/*    */       }
/* 60 */       for (int j = i; j < this.elts.length; j++) {
/* 61 */         if (this.elts[j] == 0L) {
/* 62 */           this.elts[j] = l;
/* 63 */           this.used += 1;
/* 64 */           return j;
/*    */         }
/*    */       }
/* 67 */       if (!$assertionsDisabled) throw new AssertionError();
/* 68 */       return -1;
/*    */     }
/*    */   }
/*    */ 
/*    */   void remove(int paramInt)
/*    */   {
/* 75 */     synchronized (this) {
/* 76 */       this.elts[paramInt] = 0L;
/* 77 */       this.used -= 1;
/* 78 */       if ((this.used == 0) && (this.waitingToEmpty))
/* 79 */         notifyAll();
/*    */     }
/*    */   }
/*    */ 
/*    */   void signalAndWait()
/*    */   {
/* 86 */     synchronized (this) {
/* 87 */       int i = this.used;
/* 88 */       int j = this.elts.length;
/* 89 */       for (int k = 0; k < j; k++) {
/* 90 */         long l = this.elts[k];
/* 91 */         if (l != 0L)
/*    */         {
/* 93 */           if (l != -1L)
/* 94 */             NativeThread.signal(l);
/* 95 */           i--; if (i == 0) break;
/*    */         }
/*    */       }
/* 98 */       this.waitingToEmpty = true;
/* 99 */       k = 0;
/* 100 */       while (this.used > 0) {
/*    */         try {
/* 102 */           wait();
/*    */         } catch (InterruptedException localInterruptedException) {
/* 104 */           k = 1;
/*    */         }
/*    */       }
/* 107 */       if (k != 0)
/* 108 */         Thread.currentThread().interrupt();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.NativeThreadSet
 * JD-Core Version:    0.6.2
 */