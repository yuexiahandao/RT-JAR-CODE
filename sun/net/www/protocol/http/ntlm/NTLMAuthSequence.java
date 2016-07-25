/*    */ package sun.net.www.protocol.http.ntlm;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import sun.misc.BASE64Decoder;
/*    */ 
/*    */ public class NTLMAuthSequence
/*    */ {
/*    */   private String username;
/*    */   private String password;
/*    */   private String ntdomain;
/*    */   private int state;
/*    */   private long crdHandle;
/*    */   private long ctxHandle;
/*    */   Status status;
/*    */ 
/*    */   NTLMAuthSequence(String paramString1, String paramString2, String paramString3)
/*    */     throws IOException
/*    */   {
/* 63 */     this.username = paramString1;
/* 64 */     this.password = paramString2;
/* 65 */     this.ntdomain = paramString3;
/* 66 */     this.status = new Status();
/* 67 */     this.state = 0;
/* 68 */     this.crdHandle = getCredentialsHandle(paramString1, paramString3, paramString2);
/* 69 */     if (this.crdHandle == 0L)
/* 70 */       throw new IOException("could not get credentials handle");
/*    */   }
/*    */ 
/*    */   public String getAuthHeader(String paramString) throws IOException
/*    */   {
/* 75 */     byte[] arrayOfByte1 = null;
/*    */ 
/* 77 */     assert (!this.status.sequenceComplete);
/*    */ 
/* 79 */     if (paramString != null)
/* 80 */       arrayOfByte1 = new BASE64Decoder().decodeBuffer(paramString);
/* 81 */     byte[] arrayOfByte2 = getNextToken(this.crdHandle, arrayOfByte1, this.status);
/* 82 */     if (arrayOfByte2 == null)
/* 83 */       throw new IOException("Internal authentication error");
/* 84 */     return new B64Encoder().encode(arrayOfByte2);
/*    */   }
/*    */ 
/*    */   public boolean isComplete() {
/* 88 */     return this.status.sequenceComplete;
/*    */   }
/*    */ 
/*    */   private static native void initFirst(Class<Status> paramClass);
/*    */ 
/*    */   private native long getCredentialsHandle(String paramString1, String paramString2, String paramString3);
/*    */ 
/*    */   private native byte[] getNextToken(long paramLong, byte[] paramArrayOfByte, Status paramStatus);
/*    */ 
/*    */   static
/*    */   {
/* 48 */     initFirst(Status.class);
/*    */   }
/*    */ 
/*    */   class Status
/*    */   {
/*    */     boolean sequenceComplete;
/*    */ 
/*    */     Status()
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.ntlm.NTLMAuthSequence
 * JD-Core Version:    0.6.2
 */