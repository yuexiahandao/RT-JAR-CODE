/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import sun.security.krb5.internal.HostAddress;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.KrbApErrException;
/*     */ import sun.security.krb5.internal.SeqNumber;
/*     */ 
/*     */ abstract class KrbAppMessage
/*     */ {
/*  37 */   private static boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   void check(KerberosTime paramKerberosTime, Integer paramInteger1, Integer paramInteger2, HostAddress paramHostAddress1, HostAddress paramHostAddress2, SeqNumber paramSeqNumber, HostAddress paramHostAddress3, HostAddress paramHostAddress4, boolean paramBoolean1, boolean paramBoolean2, PrincipalName paramPrincipalName, Realm paramRealm)
/*     */     throws KrbApErrException
/*     */   {
/*  55 */     if ((paramHostAddress3 != null) && (
/*  56 */       (paramHostAddress1 == null) || (paramHostAddress3 == null) || (!paramHostAddress1.equals(paramHostAddress3))))
/*     */     {
/*  58 */       if ((DEBUG) && (paramHostAddress1 == null)) {
/*  59 */         System.out.println("packetSAddress is null");
/*     */       }
/*  61 */       if ((DEBUG) && (paramHostAddress3 == null)) {
/*  62 */         System.out.println("sAddress is null");
/*     */       }
/*  64 */       throw new KrbApErrException(38);
/*     */     }
/*     */ 
/*  68 */     if ((paramHostAddress4 != null) && (
/*  69 */       (paramHostAddress2 == null) || (paramHostAddress4 == null) || (!paramHostAddress2.equals(paramHostAddress4))))
/*     */     {
/*  71 */       throw new KrbApErrException(38);
/*     */     }
/*     */ 
/*  74 */     if (paramKerberosTime != null) {
/*  75 */       paramKerberosTime.setMicroSeconds(paramInteger1);
/*  76 */       if (!paramKerberosTime.inClockSkew())
/*  77 */         throw new KrbApErrException(37);
/*     */     }
/*  79 */     else if (paramBoolean1) {
/*  80 */       throw new KrbApErrException(37);
/*     */     }
/*     */ 
/*  87 */     if ((paramSeqNumber == null) && (paramBoolean2 == true)) {
/*  88 */       throw new KrbApErrException(400);
/*     */     }
/*  90 */     if ((paramInteger2 != null) && (paramSeqNumber != null)) {
/*  91 */       if (paramInteger2.intValue() != paramSeqNumber.current()) {
/*  92 */         throw new KrbApErrException(42);
/*     */       }
/*  94 */       paramSeqNumber.step();
/*     */     }
/*  96 */     else if (paramBoolean2) {
/*  97 */       throw new KrbApErrException(42);
/*     */     }
/*     */ 
/* 102 */     if ((paramKerberosTime == null) && (paramInteger2 == null))
/* 103 */       throw new KrbApErrException(41);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.KrbAppMessage
 * JD-Core Version:    0.6.2
 */