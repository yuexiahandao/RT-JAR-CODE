/*    */
package java.lang;
/*    */ 
/*    */

import sun.misc.Signal;
/*    */ import sun.misc.SignalHandler;

/*    */
/*    */ class Terminator
/*    */ {
    /* 42 */   private static SignalHandler handler = null;

    /*    */
/*    */
    static void setup()
/*    */ {
/* 49 */
        if (handler != null) return;
/* 50 */
        SignalHandler local1 = new SignalHandler() {
            /*    */
            public void handle(Signal paramAnonymousSignal) {
/* 52 */
                Shutdown.exit(paramAnonymousSignal.getNumber() + 128);
/*    */
            }
/*    */
        };
/* 55 */
        handler = local1;
/*    */
        try
/*    */ {
/* 61 */
            Signal.handle(new Signal("INT"), local1);
/*    */
        } catch (IllegalArgumentException localIllegalArgumentException1) {
/*    */
        }
/*    */
        try {
/* 65 */
            Signal.handle(new Signal("TERM"), local1);
/*    */
        }
/*    */ catch (IllegalArgumentException localIllegalArgumentException2)
/*    */ {
/*    */
        }
/*    */
    }

    /*    */
/*    */
    static void teardown()
/*    */ {
/*    */
    }
/*    */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Terminator
 * JD-Core Version:    0.6.2
 */