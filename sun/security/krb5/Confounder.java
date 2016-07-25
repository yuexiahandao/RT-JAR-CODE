/*    */ package sun.security.krb5;
/*    */ 
/*    */ import java.security.SecureRandom;
/*    */ 
/*    */ public final class Confounder
/*    */ {
/* 37 */   private static SecureRandom srand = new SecureRandom();
/*    */ 
/*    */   public static byte[] bytes(int paramInt)
/*    */   {
/* 43 */     byte[] arrayOfByte = new byte[paramInt];
/* 44 */     srand.nextBytes(arrayOfByte);
/* 45 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   public static int intValue() {
/* 49 */     return srand.nextInt();
/*    */   }
/*    */ 
/*    */   public static long longValue() {
/* 53 */     return srand.nextLong();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.Confounder
 * JD-Core Version:    0.6.2
 */