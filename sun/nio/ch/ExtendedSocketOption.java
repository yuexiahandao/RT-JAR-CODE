/*    */ package sun.nio.ch;
/*    */ 
/*    */ import java.net.SocketOption;
/*    */ 
/*    */ class ExtendedSocketOption
/*    */ {
/* 38 */   static final SocketOption<Boolean> SO_OOBINLINE = new SocketOption() {
/*    */     public String name() {
/* 40 */       return "SO_OOBINLINE"; } 
/* 41 */     public Class<Boolean> type() { return Boolean.class; } 
/* 42 */     public String toString() { return name(); }
/*    */ 
/* 38 */   };
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.ExtendedSocketOption
 * JD-Core Version:    0.6.2
 */