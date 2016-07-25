/*     */ package sun.misc;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public final class Signal
/*     */ {
/*  75 */   private static Hashtable handlers = new Hashtable(4);
/*  76 */   private static Hashtable signals = new Hashtable(4);
/*     */   private int number;
/*     */   private String name;
/*     */ 
/*     */   public int getNumber()
/*     */   {
/*  83 */     return this.number;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  93 */     return this.name;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 103 */     if (this == paramObject) {
/* 104 */       return true;
/*     */     }
/* 106 */     if ((paramObject == null) || (!(paramObject instanceof Signal))) {
/* 107 */       return false;
/*     */     }
/* 109 */     Signal localSignal = (Signal)paramObject;
/* 110 */     return (this.name.equals(localSignal.name)) && (this.number == localSignal.number);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 119 */     return this.number;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 129 */     return "SIG" + this.name;
/*     */   }
/*     */ 
/*     */   public Signal(String paramString)
/*     */   {
/* 140 */     this.number = findSignal(paramString);
/* 141 */     this.name = paramString;
/* 142 */     if (this.number < 0)
/* 143 */       throw new IllegalArgumentException("Unknown signal: " + paramString);
/*     */   }
/*     */ 
/*     */   public static synchronized SignalHandler handle(Signal paramSignal, SignalHandler paramSignalHandler)
/*     */     throws IllegalArgumentException
/*     */   {
/* 162 */     long l1 = (paramSignalHandler instanceof NativeSignalHandler) ? ((NativeSignalHandler)paramSignalHandler).getHandler() : 2L;
/*     */ 
/* 164 */     long l2 = handle0(paramSignal.number, l1);
/* 165 */     if (l2 == -1L) {
/* 166 */       throw new IllegalArgumentException("Signal already used by VM or OS: " + paramSignal);
/*     */     }
/*     */ 
/* 169 */     signals.put(new Integer(paramSignal.number), paramSignal);
/* 170 */     synchronized (handlers) {
/* 171 */       SignalHandler localSignalHandler = (SignalHandler)handlers.get(paramSignal);
/* 172 */       handlers.remove(paramSignal);
/* 173 */       if (l1 == 2L) {
/* 174 */         handlers.put(paramSignal, paramSignalHandler);
/*     */       }
/* 176 */       if (l2 == 0L)
/* 177 */         return SignalHandler.SIG_DFL;
/* 178 */       if (l2 == 1L)
/* 179 */         return SignalHandler.SIG_IGN;
/* 180 */       if (l2 == 2L) {
/* 181 */         return localSignalHandler;
/*     */       }
/* 183 */       return new NativeSignalHandler(l2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void raise(Signal paramSignal)
/*     */     throws IllegalArgumentException
/*     */   {
/* 195 */     if (handlers.get(paramSignal) == null) {
/* 196 */       throw new IllegalArgumentException("Unhandled signal: " + paramSignal);
/*     */     }
/* 198 */     raise0(paramSignal.number);
/*     */   }
/*     */ 
/*     */   private static void dispatch(int paramInt)
/*     */   {
/* 203 */     final Signal localSignal = (Signal)signals.get(new Integer(paramInt));
/* 204 */     SignalHandler localSignalHandler = (SignalHandler)handlers.get(localSignal);
/*     */ 
/* 206 */     Runnable local1 = new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 212 */         this.val$handler.handle(localSignal);
/*     */       }
/*     */     };
/* 215 */     if (localSignalHandler != null)
/* 216 */       new Thread(local1, localSignal + " handler").start();
/*     */   }
/*     */ 
/*     */   private static native int findSignal(String paramString);
/*     */ 
/*     */   private static native long handle0(int paramInt, long paramLong);
/*     */ 
/*     */   private static native void raise0(int paramInt);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.Signal
 * JD-Core Version:    0.6.2
 */