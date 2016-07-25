/*    */ package sun.security.krb5.internal.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.security.AccessController;
/*    */ import sun.security.action.GetBooleanAction;
/*    */ import sun.security.util.DerValue;
/*    */ 
/*    */ public final class KerberosString
/*    */ {
/* 55 */   public static final boolean MSNAME = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.security.krb5.msinterop.kstring"))).booleanValue();
/*    */   private final String s;
/*    */ 
/*    */   public KerberosString(String paramString)
/*    */   {
/* 61 */     this.s = paramString;
/*    */   }
/*    */ 
/*    */   public KerberosString(DerValue paramDerValue) throws IOException {
/* 65 */     if (paramDerValue.tag != 27) {
/* 66 */       throw new IOException("KerberosString's tag is incorrect: " + paramDerValue.tag);
/*    */     }
/*    */ 
/* 69 */     this.s = new String(paramDerValue.getDataBytes(), MSNAME ? "UTF8" : "ASCII");
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 73 */     return this.s;
/*    */   }
/*    */ 
/*    */   public DerValue toDerValue()
/*    */     throws IOException
/*    */   {
/* 79 */     return new DerValue((byte)27, this.s.getBytes(MSNAME ? "UTF8" : "ASCII"));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.util.KerberosString
 * JD-Core Version:    0.6.2
 */