/*    */ package sun.security.krb5.internal.crypto;
/*    */ 
/*    */ import sun.security.krb5.Confounder;
/*    */ 
/*    */ public class Nonce
/*    */ {
/*    */   public static synchronized int value()
/*    */   {
/* 37 */     return Confounder.intValue() & 0x7FFFFFFF;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.Nonce
 * JD-Core Version:    0.6.2
 */