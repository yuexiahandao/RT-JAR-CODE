/*    */ package sun.nio.ch;
/*    */ 
/*    */ import com.sun.nio.sctp.SctpSocketOption;
/*    */ 
/*    */ public class SctpStdSocketOption<T>
/*    */   implements SctpSocketOption<T>
/*    */ {
/*    */   public static final int SCTP_DISABLE_FRAGMENTS = 1;
/*    */   public static final int SCTP_EXPLICIT_COMPLETE = 2;
/*    */   public static final int SCTP_FRAGMENT_INTERLEAVE = 3;
/*    */   public static final int SCTP_NODELAY = 4;
/*    */   public static final int SO_SNDBUF = 5;
/*    */   public static final int SO_RCVBUF = 6;
/*    */   public static final int SO_LINGER = 7;
/*    */   private final String name;
/*    */   private final Class<T> type;
/*    */   private int constValue;
/*    */ 
/*    */   public SctpStdSocketOption(String paramString, Class<T> paramClass)
/*    */   {
/* 48 */     this.name = paramString;
/* 49 */     this.type = paramClass;
/*    */   }
/*    */ 
/*    */   public SctpStdSocketOption(String paramString, Class<T> paramClass, int paramInt) {
/* 53 */     this.name = paramString;
/* 54 */     this.type = paramClass;
/* 55 */     this.constValue = paramInt;
/*    */   }
/*    */ 
/*    */   public String name()
/*    */   {
/* 60 */     return this.name;
/*    */   }
/*    */ 
/*    */   public Class<T> type()
/*    */   {
/* 65 */     return this.type;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 70 */     return this.name;
/*    */   }
/*    */ 
/*    */   int constValue() {
/* 74 */     return this.constValue;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.SctpStdSocketOption
 * JD-Core Version:    0.6.2
 */