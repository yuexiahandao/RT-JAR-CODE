/*    */ package javax.xml.soap;
/*    */ 
/*    */ public class MimeHeader
/*    */ {
/*    */   private String name;
/*    */   private String value;
/*    */ 
/*    */   public MimeHeader(String name, String value)
/*    */   {
/* 49 */     this.name = name;
/* 50 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 59 */     return this.name;
/*    */   }
/*    */ 
/*    */   public String getValue()
/*    */   {
/* 68 */     return this.value;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.MimeHeader
 * JD-Core Version:    0.6.2
 */