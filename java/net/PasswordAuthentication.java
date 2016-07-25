/*    */ package java.net;
/*    */ 
/*    */ public final class PasswordAuthentication
/*    */ {
/*    */   private String userName;
/*    */   private char[] password;
/*    */ 
/*    */   public PasswordAuthentication(String paramString, char[] paramArrayOfChar)
/*    */   {
/* 56 */     this.userName = paramString;
/* 57 */     this.password = ((char[])paramArrayOfChar.clone());
/*    */   }
/*    */ 
/*    */   public String getUserName()
/*    */   {
/* 66 */     return this.userName;
/*    */   }
/*    */ 
/*    */   public char[] getPassword()
/*    */   {
/* 79 */     return this.password;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.PasswordAuthentication
 * JD-Core Version:    0.6.2
 */