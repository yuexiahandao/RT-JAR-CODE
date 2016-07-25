/*    */ package java.net;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class HttpRetryException extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = -9186022286469111381L;
/*    */   private int responseCode;
/*    */   private String location;
/*    */ 
/*    */   public HttpRetryException(String paramString, int paramInt)
/*    */   {
/* 53 */     super(paramString);
/* 54 */     this.responseCode = paramInt;
/*    */   }
/*    */ 
/*    */   public HttpRetryException(String paramString1, int paramInt, String paramString2)
/*    */   {
/* 66 */     super(paramString1);
/* 67 */     this.responseCode = paramInt;
/* 68 */     this.location = paramString2;
/*    */   }
/*    */ 
/*    */   public int responseCode()
/*    */   {
/* 77 */     return this.responseCode;
/*    */   }
/*    */ 
/*    */   public String getReason()
/*    */   {
/* 87 */     return super.getMessage();
/*    */   }
/*    */ 
/*    */   public String getLocation()
/*    */   {
/* 97 */     return this.location;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.HttpRetryException
 * JD-Core Version:    0.6.2
 */