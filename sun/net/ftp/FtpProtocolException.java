/*    */ package sun.net.ftp;
/*    */ 
/*    */ public class FtpProtocolException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 5978077070276545054L;
/*    */   private final FtpReplyCode code;
/*    */ 
/*    */   public FtpProtocolException(String paramString)
/*    */   {
/* 45 */     super(paramString);
/* 46 */     this.code = FtpReplyCode.UNKNOWN_ERROR;
/*    */   }
/*    */ 
/*    */   public FtpProtocolException(String paramString, FtpReplyCode paramFtpReplyCode)
/*    */   {
/* 57 */     super(paramString);
/* 58 */     this.code = paramFtpReplyCode;
/*    */   }
/*    */ 
/*    */   public FtpReplyCode getReplyCode()
/*    */   {
/* 68 */     return this.code;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.ftp.FtpProtocolException
 * JD-Core Version:    0.6.2
 */