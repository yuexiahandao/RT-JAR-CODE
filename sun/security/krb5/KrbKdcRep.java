/*     */ package sun.security.krb5;
/*     */ 
/*     */ import sun.security.krb5.internal.EncKDCRepPart;
/*     */ import sun.security.krb5.internal.HostAddresses;
/*     */ import sun.security.krb5.internal.KDCOptions;
/*     */ import sun.security.krb5.internal.KDCRep;
/*     */ import sun.security.krb5.internal.KDCReq;
/*     */ import sun.security.krb5.internal.KDCReqBody;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.KrbApErrException;
/*     */ import sun.security.krb5.internal.TicketFlags;
/*     */ 
/*     */ abstract class KrbKdcRep
/*     */ {
/*     */   static void check(KDCReq paramKDCReq, KDCRep paramKDCRep)
/*     */     throws KrbApErrException
/*     */   {
/*  42 */     if (!paramKDCReq.reqBody.cname.equalsWithoutRealm(paramKDCRep.cname)) {
/*  43 */       paramKDCRep.encKDCRepPart.key.destroy();
/*  44 */       throw new KrbApErrException(41);
/*     */     }
/*     */ 
/*  54 */     if (!paramKDCReq.reqBody.sname.equalsWithoutRealm(paramKDCRep.encKDCRepPart.sname)) {
/*  55 */       paramKDCRep.encKDCRepPart.key.destroy();
/*  56 */       throw new KrbApErrException(41);
/*     */     }
/*     */ 
/*  59 */     if (!paramKDCReq.reqBody.crealm.equals(paramKDCRep.encKDCRepPart.srealm)) {
/*  60 */       paramKDCRep.encKDCRepPart.key.destroy();
/*  61 */       throw new KrbApErrException(41);
/*     */     }
/*     */ 
/*  64 */     if (paramKDCReq.reqBody.getNonce() != paramKDCRep.encKDCRepPart.nonce) {
/*  65 */       paramKDCRep.encKDCRepPart.key.destroy();
/*  66 */       throw new KrbApErrException(41);
/*     */     }
/*     */ 
/*  69 */     if ((paramKDCReq.reqBody.addresses != null) && (paramKDCRep.encKDCRepPart.caddr != null) && (!paramKDCReq.reqBody.addresses.equals(paramKDCRep.encKDCRepPart.caddr)))
/*     */     {
/*  72 */       paramKDCRep.encKDCRepPart.key.destroy();
/*  73 */       throw new KrbApErrException(41);
/*     */     }
/*     */ 
/*  77 */     for (int i = 1; i < 6; i++) {
/*  78 */       if (paramKDCReq.reqBody.kdcOptions.get(i) != paramKDCRep.encKDCRepPart.flags.get(i))
/*     */       {
/*  80 */         throw new KrbApErrException(41);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  86 */     if (paramKDCReq.reqBody.kdcOptions.get(8) != paramKDCRep.encKDCRepPart.flags.get(8))
/*     */     {
/*  88 */       throw new KrbApErrException(41);
/*     */     }
/*  90 */     if ((paramKDCReq.reqBody.from == null) || (paramKDCReq.reqBody.from.isZero()))
/*     */     {
/*  92 */       if ((paramKDCRep.encKDCRepPart.starttime != null) && (!paramKDCRep.encKDCRepPart.starttime.inClockSkew()))
/*     */       {
/*  94 */         paramKDCRep.encKDCRepPart.key.destroy();
/*  95 */         throw new KrbApErrException(37);
/*     */       }
/*     */     }
/*  98 */     if ((paramKDCReq.reqBody.from != null) && (!paramKDCReq.reqBody.from.isZero()))
/*     */     {
/* 100 */       if ((paramKDCRep.encKDCRepPart.starttime != null) && (!paramKDCReq.reqBody.from.equals(paramKDCRep.encKDCRepPart.starttime)))
/*     */       {
/* 102 */         paramKDCRep.encKDCRepPart.key.destroy();
/* 103 */         throw new KrbApErrException(41);
/*     */       }
/*     */     }
/* 106 */     if ((!paramKDCReq.reqBody.till.isZero()) && (paramKDCRep.encKDCRepPart.endtime.greaterThan(paramKDCReq.reqBody.till)))
/*     */     {
/* 108 */       paramKDCRep.encKDCRepPart.key.destroy();
/* 109 */       throw new KrbApErrException(41);
/*     */     }
/*     */ 
/* 112 */     if ((paramKDCReq.reqBody.kdcOptions.get(8)) && 
/* 113 */       (paramKDCReq.reqBody.rtime != null) && (!paramKDCReq.reqBody.rtime.isZero()))
/*     */     {
/* 115 */       if ((paramKDCRep.encKDCRepPart.renewTill == null) || (paramKDCRep.encKDCRepPart.renewTill.greaterThan(paramKDCReq.reqBody.rtime)))
/*     */       {
/* 118 */         paramKDCRep.encKDCRepPart.key.destroy();
/* 119 */         throw new KrbApErrException(41);
/*     */       }
/*     */     }
/* 122 */     if ((paramKDCReq.reqBody.kdcOptions.get(27)) && (paramKDCRep.encKDCRepPart.flags.get(8)))
/*     */     {
/* 124 */       if (!paramKDCReq.reqBody.till.isZero())
/*     */       {
/* 126 */         if ((paramKDCRep.encKDCRepPart.renewTill == null) || (paramKDCRep.encKDCRepPart.renewTill.greaterThan(paramKDCReq.reqBody.till)))
/*     */         {
/* 129 */           paramKDCRep.encKDCRepPart.key.destroy();
/* 130 */           throw new KrbApErrException(41);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.KrbKdcRep
 * JD-Core Version:    0.6.2
 */