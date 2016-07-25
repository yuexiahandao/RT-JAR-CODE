/*    */ package sun.security.krb5.internal;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.math.BigInteger;
/*    */ import sun.security.krb5.Asn1Exception;
/*    */ import sun.security.util.DerInputStream;
/*    */ import sun.security.util.DerOutputStream;
/*    */ import sun.security.util.DerValue;
/*    */ 
/*    */ public class LastReqEntry
/*    */ {
/*    */   private int lrType;
/*    */   private KerberosTime lrValue;
/*    */ 
/*    */   private LastReqEntry()
/*    */   {
/*    */   }
/*    */ 
/*    */   public LastReqEntry(int paramInt, KerberosTime paramKerberosTime)
/*    */   {
/* 45 */     this.lrType = paramInt;
/* 46 */     this.lrValue = paramKerberosTime;
/*    */   }
/*    */ 
/*    */   public LastReqEntry(DerValue paramDerValue)
/*    */     throws Asn1Exception, IOException
/*    */   {
/* 57 */     if (paramDerValue.getTag() != 48) {
/* 58 */       throw new Asn1Exception(906);
/*    */     }
/*    */ 
/* 61 */     DerValue localDerValue = paramDerValue.getData().getDerValue();
/* 62 */     if ((localDerValue.getTag() & 0x1F) == 0) {
/* 63 */       this.lrType = localDerValue.getData().getBigInteger().intValue();
/*    */     }
/*    */     else {
/* 66 */       throw new Asn1Exception(906);
/*    */     }
/* 68 */     this.lrValue = KerberosTime.parse(paramDerValue.getData(), (byte)1, false);
/* 69 */     if (paramDerValue.getData().available() > 0)
/* 70 */       throw new Asn1Exception(906);
/*    */   }
/*    */ 
/*    */   public byte[] asn1Encode()
/*    */     throws Asn1Exception, IOException
/*    */   {
/* 80 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 81 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 82 */     localDerOutputStream2.putInteger(this.lrType);
/* 83 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/* 84 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), this.lrValue.asn1Encode());
/* 85 */     localDerOutputStream2 = new DerOutputStream();
/* 86 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 87 */     return localDerOutputStream2.toByteArray();
/*    */   }
/*    */ 
/*    */   public Object clone() {
/* 91 */     LastReqEntry localLastReqEntry = new LastReqEntry();
/* 92 */     localLastReqEntry.lrType = this.lrType;
/* 93 */     localLastReqEntry.lrValue = ((KerberosTime)this.lrValue.clone());
/* 94 */     return localLastReqEntry;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.LastReqEntry
 * JD-Core Version:    0.6.2
 */