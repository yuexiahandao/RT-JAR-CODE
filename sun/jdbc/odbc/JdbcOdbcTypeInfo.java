/*    */ package sun.jdbc.odbc;
/*    */ 
/*    */ public class JdbcOdbcTypeInfo extends JdbcOdbcObject
/*    */ {
/*    */   String typeName;
/*    */   int precision;
/*    */ 
/*    */   public void setName(String paramString)
/*    */   {
/* 38 */     this.typeName = paramString;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 48 */     return this.typeName;
/*    */   }
/*    */ 
/*    */   public void setPrec(int paramInt)
/*    */   {
/* 59 */     this.precision = paramInt;
/*    */   }
/*    */ 
/*    */   public int getPrec()
/*    */   {
/* 69 */     return this.precision;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcTypeInfo
 * JD-Core Version:    0.6.2
 */