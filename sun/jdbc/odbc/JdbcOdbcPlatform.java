/*    */ package sun.jdbc.odbc;
/*    */ 
/*    */ public class JdbcOdbcPlatform
/*    */ {
/* 80 */   static final int sizeofSQLLEN = JdbcOdbc.getSQLLENSize();
/*    */ 
/*    */   public static boolean is32BitPlatform()
/*    */   {
/* 30 */     if (sizeofSQLLEN == 4) {
/* 31 */       return true;
/*    */     }
/* 33 */     return false;
/*    */   }
/*    */ 
/*    */   public static boolean is64BitPlatform()
/*    */   {
/* 42 */     if (sizeofSQLLEN == 8) {
/* 43 */       return true;
/*    */     }
/* 45 */     return false;
/*    */   }
/*    */ 
/*    */   public static int getLengthBufferSize()
/*    */   {
/* 53 */     return sizeofSQLLEN;
/*    */   }
/*    */ 
/*    */   public static byte[] convertIntToByteArray(int paramInt)
/*    */   {
/* 63 */     byte[] arrayOfByte = new byte[sizeofSQLLEN];
/* 64 */     JdbcOdbc.intToBytes(paramInt, arrayOfByte);
/* 65 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   public static byte[] convertLongToByteArray(long paramLong)
/*    */   {
/* 75 */     byte[] arrayOfByte = new byte[sizeofSQLLEN];
/* 76 */     JdbcOdbc.longToBytes(paramLong, arrayOfByte);
/* 77 */     return arrayOfByte;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcPlatform
 * JD-Core Version:    0.6.2
 */