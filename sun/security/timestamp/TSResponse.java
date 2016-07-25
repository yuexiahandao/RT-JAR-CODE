/*     */ package sun.security.timestamp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.pkcs.PKCS7;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class TSResponse
/*     */ {
/*     */   public static final int GRANTED = 0;
/*     */   public static final int GRANTED_WITH_MODS = 1;
/*     */   public static final int REJECTION = 2;
/*     */   public static final int WAITING = 3;
/*     */   public static final int REVOCATION_WARNING = 4;
/*     */   public static final int REVOCATION_NOTIFICATION = 5;
/*     */   public static final int BAD_ALG = 0;
/*     */   public static final int BAD_REQUEST = 2;
/*     */   public static final int BAD_DATA_FORMAT = 5;
/*     */   public static final int TIME_NOT_AVAILABLE = 14;
/*     */   public static final int UNACCEPTED_POLICY = 15;
/*     */   public static final int UNACCEPTED_EXTENSION = 16;
/*     */   public static final int ADD_INFO_NOT_AVAILABLE = 17;
/*     */   public static final int SYSTEM_FAILURE = 25;
/*     */   private static final boolean DEBUG = false;
/*     */   private int status;
/* 189 */   private String[] statusString = null;
/*     */ 
/* 191 */   private int failureInfo = -1;
/*     */ 
/* 193 */   private byte[] encodedTsToken = null;
/*     */ 
/* 195 */   private PKCS7 tsToken = null;
/*     */ 
/*     */   TSResponse(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 205 */     parse(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public int getStatusCode()
/*     */   {
/* 212 */     return this.status;
/*     */   }
/*     */ 
/*     */   public String[] getStatusMessages()
/*     */   {
/* 221 */     return this.statusString;
/*     */   }
/*     */ 
/*     */   public int getFailureCode()
/*     */   {
/* 230 */     return this.failureInfo;
/*     */   }
/*     */ 
/*     */   public String getStatusCodeAsText()
/*     */   {
/* 235 */     switch (this.status) {
/*     */     case 0:
/* 237 */       return "the timestamp request was granted.";
/*     */     case 1:
/* 240 */       return "the timestamp request was granted with some modifications.";
/*     */     case 2:
/* 244 */       return "the timestamp request was rejected.";
/*     */     case 3:
/* 247 */       return "the timestamp request has not yet been processed.";
/*     */     case 4:
/* 250 */       return "warning: a certificate revocation is imminent.";
/*     */     case 5:
/* 253 */       return "notification: a certificate revocation has occurred.";
/*     */     }
/*     */ 
/* 256 */     return "unknown status code " + this.status + ".";
/*     */   }
/*     */ 
/*     */   public String getFailureCodeAsText()
/*     */   {
/* 262 */     if (this.failureInfo == -1) {
/* 263 */       return null;
/*     */     }
/*     */ 
/* 266 */     switch (this.failureInfo)
/*     */     {
/*     */     case 0:
/* 269 */       return "Unrecognized or unsupported alrorithm identifier.";
/*     */     case 2:
/* 272 */       return "The requested transaction is not permitted or supported.";
/*     */     case 5:
/* 275 */       return "The data submitted has the wrong format.";
/*     */     case 14:
/* 278 */       return "The TSA's time source is not available.";
/*     */     case 15:
/* 281 */       return "The requested TSA policy is not supported by the TSA.";
/*     */     case 16:
/* 284 */       return "The requested extension is not supported by the TSA.";
/*     */     case 17:
/* 287 */       return "The additional information requested could not be understood or is not available.";
/*     */     case 25:
/* 291 */       return "The request cannot be handled due to system failure.";
/*     */     case 1:
/*     */     case 3:
/*     */     case 4:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 18:
/*     */     case 19:
/*     */     case 20:
/*     */     case 21:
/*     */     case 22:
/*     */     case 23:
/* 294 */     case 24: } return "unknown status code " + this.status;
/*     */   }
/*     */ 
/*     */   public PKCS7 getToken()
/*     */   {
/* 304 */     return this.tsToken;
/*     */   }
/*     */ 
/*     */   public byte[] getEncodedToken()
/*     */   {
/* 313 */     return this.encodedTsToken;
/*     */   }
/*     */ 
/*     */   private void parse(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 326 */     DerValue localDerValue1 = new DerValue(paramArrayOfByte);
/* 327 */     if (localDerValue1.tag != 48) {
/* 328 */       throw new IOException("Bad encoding for timestamp response");
/*     */     }
/*     */ 
/* 333 */     DerValue localDerValue2 = localDerValue1.data.getDerValue();
/*     */ 
/* 335 */     this.status = localDerValue2.data.getInteger();
/*     */     Object localObject;
/*     */     int i;
/* 340 */     if (localDerValue2.data.available() > 0) {
/* 341 */       localObject = localDerValue2.data.getSequence(1);
/* 342 */       this.statusString = new String[localObject.length];
/* 343 */       for (i = 0; i < localObject.length; i++) {
/* 344 */         this.statusString[i] = localObject[i].getUTF8String();
/*     */       }
/*     */     }
/*     */ 
/* 348 */     if (localDerValue2.data.available() > 0) {
/* 349 */       localObject = localDerValue2.data.getBitString();
/* 350 */       i = new Byte(localObject[0]).intValue();
/* 351 */       if ((i < 0) || (i > 25) || (localObject.length != 1)) {
/* 352 */         throw new IOException("Bad encoding for timestamp response: unrecognized value for the failInfo element");
/*     */       }
/*     */ 
/* 355 */       this.failureInfo = i;
/*     */     }
/*     */ 
/* 359 */     if (localDerValue1.data.available() > 0) {
/* 360 */       localObject = localDerValue1.data.getDerValue();
/* 361 */       this.encodedTsToken = ((DerValue)localObject).toByteArray();
/* 362 */       this.tsToken = new PKCS7(this.encodedTsToken);
/*     */     }
/*     */ 
/* 366 */     if ((this.status == 0) || (this.status == 1)) {
/* 367 */       if (this.tsToken == null) {
/* 368 */         throw new TimestampException("Bad encoding for timestamp response: expected a timeStampToken element to be present");
/*     */       }
/*     */ 
/*     */     }
/* 372 */     else if (this.tsToken != null)
/* 373 */       throw new TimestampException("Bad encoding for timestamp response: expected no timeStampToken element to be present");
/*     */   }
/*     */ 
/*     */   static final class TimestampException extends IOException
/*     */   {
/*     */     TimestampException(String paramString)
/*     */     {
/* 381 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.timestamp.TSResponse
 * JD-Core Version:    0.6.2
 */