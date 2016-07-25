/*    */ package sun.jdbc.odbc;
/*    */ 
/*    */ import java.sql.BatchUpdateException;
/*    */ 
/*    */ public class JdbcOdbcBatchUpdateException extends BatchUpdateException
/*    */ {
/*    */   int[] exceptionCounts;
/*    */ 
/*    */   public JdbcOdbcBatchUpdateException(String paramString1, String paramString2, int paramInt, int[] paramArrayOfInt)
/*    */   {
/* 39 */     super(paramString1, paramString2, paramInt, paramArrayOfInt);
/* 40 */     this.exceptionCounts = paramArrayOfInt;
/*    */   }
/*    */ 
/*    */   public JdbcOdbcBatchUpdateException(String paramString1, String paramString2, int[] paramArrayOfInt)
/*    */   {
/* 45 */     super(paramString1, paramString2, paramArrayOfInt);
/* 46 */     this.exceptionCounts = paramArrayOfInt;
/*    */   }
/*    */ 
/*    */   public JdbcOdbcBatchUpdateException(String paramString, int[] paramArrayOfInt)
/*    */   {
/* 51 */     super(paramString, paramArrayOfInt);
/* 52 */     this.exceptionCounts = paramArrayOfInt;
/*    */   }
/*    */ 
/*    */   public JdbcOdbcBatchUpdateException(int[] paramArrayOfInt)
/*    */   {
/* 57 */     super(paramArrayOfInt);
/* 58 */     this.exceptionCounts = paramArrayOfInt;
/*    */   }
/*    */ 
/*    */   public JdbcOdbcBatchUpdateException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public int[] getUpdateCounts()
/*    */   {
/* 68 */     return this.exceptionCounts;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcBatchUpdateException
 * JD-Core Version:    0.6.2
 */