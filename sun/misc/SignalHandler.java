/*    */ package sun.misc;
/*    */ 
/*    */ public abstract interface SignalHandler
/*    */ {
/* 42 */   public static final SignalHandler SIG_DFL = new NativeSignalHandler(0L);
/*    */ 
/* 46 */   public static final SignalHandler SIG_IGN = new NativeSignalHandler(1L);
/*    */ 
/*    */   public abstract void handle(Signal paramSignal);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.SignalHandler
 * JD-Core Version:    0.6.2
 */