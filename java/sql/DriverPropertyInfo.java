/*    */ package java.sql;
/*    */ 
/*    */ public class DriverPropertyInfo
/*    */ {
/*    */   public String name;
/* 60 */   public String description = null;
/*    */ 
/* 67 */   public boolean required = false;
/*    */ 
/* 76 */   public String value = null;
/*    */ 
/* 83 */   public String[] choices = null;
/*    */ 
/*    */   public DriverPropertyInfo(String paramString1, String paramString2)
/*    */   {
/* 48 */     this.name = paramString1;
/* 49 */     this.value = paramString2;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.DriverPropertyInfo
 * JD-Core Version:    0.6.2
 */