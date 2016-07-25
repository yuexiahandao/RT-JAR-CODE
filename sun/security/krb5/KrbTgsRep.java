/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.krb5.internal.EncKDCRepPart;
/*     */ import sun.security.krb5.internal.EncTGSRepPart;
/*     */ import sun.security.krb5.internal.KDCReqBody;
/*     */ import sun.security.krb5.internal.KRBError;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.TGSRep;
/*     */ import sun.security.krb5.internal.TGSReq;
/*     */ import sun.security.krb5.internal.Ticket;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KrbTgsRep extends KrbKdcRep
/*     */ {
/*     */   private TGSRep rep;
/*     */   private Credentials creds;
/*     */   private Ticket secondTicket;
/*  47 */   private static final boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   KrbTgsRep(byte[] paramArrayOfByte, KrbTgsReq paramKrbTgsReq) throws KrbException, IOException
/*     */   {
/*  51 */     DerValue localDerValue = new DerValue(paramArrayOfByte);
/*  52 */     TGSReq localTGSReq = paramKrbTgsReq.getMessage();
/*  53 */     TGSRep localTGSRep = null;
/*     */     try {
/*  55 */       localTGSRep = new TGSRep(localDerValue);
/*     */     } catch (Asn1Exception localAsn1Exception) {
/*  57 */       localTGSRep = null;
/*  58 */       localObject1 = new KRBError(localDerValue);
/*  59 */       localObject2 = ((KRBError)localObject1).getErrorString();
/*  60 */       Object localObject3 = null;
/*  61 */       if ((localObject2 != null) && (((String)localObject2).length() > 0))
/*  62 */         if (((String)localObject2).charAt(((String)localObject2).length() - 1) == 0)
/*  63 */           localObject3 = ((String)localObject2).substring(0, ((String)localObject2).length() - 1);
/*     */         else
/*  65 */           localObject3 = localObject2;
/*     */       KrbException localKrbException;
/*  68 */       if (localObject3 == null)
/*     */       {
/*  70 */         localKrbException = new KrbException(((KRBError)localObject1).getErrorCode());
/*     */       }
/*     */       else {
/*  73 */         localKrbException = new KrbException(((KRBError)localObject1).getErrorCode(), (String)localObject3);
/*     */       }
/*  75 */       localKrbException.initCause(localAsn1Exception);
/*  76 */       throw localKrbException;
/*     */     }
/*  78 */     byte[] arrayOfByte = localTGSRep.encPart.decrypt(paramKrbTgsReq.tgsReqKey, paramKrbTgsReq.usedSubkey() ? 9 : 8);
/*     */ 
/*  82 */     Object localObject1 = localTGSRep.encPart.reset(arrayOfByte);
/*  83 */     localDerValue = new DerValue((byte[])localObject1);
/*  84 */     Object localObject2 = new EncTGSRepPart(localDerValue);
/*  85 */     localTGSRep.ticket.sname.setRealm(localTGSRep.ticket.realm);
/*  86 */     localTGSRep.encKDCRepPart = ((EncKDCRepPart)localObject2);
/*     */ 
/*  88 */     check(localTGSReq, localTGSRep);
/*     */ 
/*  90 */     this.creds = new Credentials(localTGSRep.ticket, localTGSReq.reqBody.cname, localTGSRep.ticket.sname, ((EncTGSRepPart)localObject2).key, ((EncTGSRepPart)localObject2).flags, ((EncTGSRepPart)localObject2).authtime, ((EncTGSRepPart)localObject2).starttime, ((EncTGSRepPart)localObject2).endtime, ((EncTGSRepPart)localObject2).renewTill, ((EncTGSRepPart)localObject2).caddr);
/*     */ 
/* 101 */     this.rep = localTGSRep;
/* 102 */     this.creds = this.creds;
/* 103 */     this.secondTicket = paramKrbTgsReq.getSecondTicket();
/*     */   }
/*     */ 
/*     */   public Credentials getCreds()
/*     */   {
/* 110 */     return this.creds;
/*     */   }
/*     */ 
/*     */   sun.security.krb5.internal.ccache.Credentials setCredentials() {
/* 114 */     return new sun.security.krb5.internal.ccache.Credentials(this.rep, this.secondTicket);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.KrbTgsRep
 * JD-Core Version:    0.6.2
 */