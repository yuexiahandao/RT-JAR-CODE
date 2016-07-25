/*    */ package com.sun.security.ntlm;
/*    */ 
/*    */ import java.security.GeneralSecurityException;
/*    */ 
/*    */ public final class NTLMException extends GeneralSecurityException
/*    */ {
/*    */   public static final int PACKET_READ_ERROR = 1;
/*    */   public static final int NO_DOMAIN_INFO = 2;
/*    */   public static final int USER_UNKNOWN = 3;
/*    */   public static final int AUTH_FAILED = 4;
/*    */   public static final int BAD_VERSION = 5;
/*    */   public static final int PROTOCOL = 6;
/*    */   private int errorCode;
/*    */ 
/*    */   public NTLMException(int paramInt, String paramString)
/*    */   {
/* 82 */     super(paramString);
/* 83 */     this.errorCode = paramInt;
/*    */   }
/*    */ 
/*    */   public int errorCode()
/*    */   {
/* 91 */     return this.errorCode;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.ntlm.NTLMException
 * JD-Core Version:    0.6.2
 */