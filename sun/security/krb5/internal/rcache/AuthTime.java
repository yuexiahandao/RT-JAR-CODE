/*    */ package sun.security.krb5.internal.rcache;
/*    */ 
/*    */ public class AuthTime
/*    */ {
/*    */   long kerberosTime;
/*    */   int cusec;
/*    */ 
/*    */   public AuthTime(long paramLong, int paramInt)
/*    */   {
/* 50 */     this.kerberosTime = paramLong;
/* 51 */     this.cusec = paramInt;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 60 */     if (((paramObject instanceof AuthTime)) && 
/* 61 */       (((AuthTime)paramObject).kerberosTime == this.kerberosTime) && (((AuthTime)paramObject).cusec == this.cusec))
/*    */     {
/* 63 */       return true;
/*    */     }
/*    */ 
/* 66 */     return false;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 75 */     int i = 17;
/*    */ 
/* 77 */     i = 37 * i + (int)(this.kerberosTime ^ this.kerberosTime >>> 32);
/* 78 */     i = 37 * i + this.cusec;
/*    */ 
/* 80 */     return i;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.rcache.AuthTime
 * JD-Core Version:    0.6.2
 */