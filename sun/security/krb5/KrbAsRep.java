/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Objects;
/*     */ import javax.security.auth.kerberos.KeyTab;
/*     */ import sun.security.jgss.krb5.Krb5Util;
/*     */ import sun.security.krb5.internal.ASRep;
/*     */ import sun.security.krb5.internal.ASReq;
/*     */ import sun.security.krb5.internal.EncASRepPart;
/*     */ import sun.security.krb5.internal.KDCReqBody;
/*     */ import sun.security.krb5.internal.KRBError;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.PAData;
/*     */ import sun.security.krb5.internal.Ticket;
/*     */ import sun.security.krb5.internal.crypto.EType;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ class KrbAsRep extends KrbKdcRep
/*     */ {
/*     */   private ASRep rep;
/*     */   private Credentials creds;
/*  54 */   private boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   KrbAsRep(byte[] paramArrayOfByte) throws KrbException, Asn1Exception, IOException
/*     */   {
/*  58 */     DerValue localDerValue = new DerValue(paramArrayOfByte);
/*     */     try {
/*  60 */       this.rep = new ASRep(localDerValue);
/*     */     } catch (Asn1Exception localAsn1Exception) {
/*  62 */       this.rep = null;
/*  63 */       KRBError localKRBError = new KRBError(localDerValue);
/*  64 */       String str1 = localKRBError.getErrorString();
/*  65 */       String str2 = null;
/*     */ 
/*  67 */       if ((str1 != null) && (str1.length() > 0))
/*  68 */         if (str1.charAt(str1.length() - 1) == 0)
/*  69 */           str2 = str1.substring(0, str1.length() - 1);
/*     */         else
/*  71 */           str2 = str1;
/*     */       KrbException localKrbException;
/*  74 */       if (str2 == null)
/*     */       {
/*  76 */         localKrbException = new KrbException(localKRBError);
/*     */       } else {
/*  78 */         if (this.DEBUG) {
/*  79 */           System.out.println("KRBError received: " + str2);
/*     */         }
/*     */ 
/*  82 */         localKrbException = new KrbException(localKRBError, str2);
/*     */       }
/*  84 */       localKrbException.initCause(localAsn1Exception);
/*  85 */       throw localKrbException;
/*     */     }
/*     */   }
/*     */ 
/*     */   PAData[] getPA()
/*     */   {
/*  91 */     return this.rep.pAData;
/*     */   }
/*     */ 
/*     */   void decryptUsingKeyTab(KeyTab paramKeyTab, KrbAsReq paramKrbAsReq, PrincipalName paramPrincipalName)
/*     */     throws KrbException, Asn1Exception, IOException
/*     */   {
/* 102 */     EncryptionKey localEncryptionKey = null;
/* 103 */     int i = this.rep.encPart.getEType();
/* 104 */     Integer localInteger = this.rep.encPart.kvno;
/*     */     try {
/* 106 */       localEncryptionKey = EncryptionKey.findKey(i, localInteger, Krb5Util.keysFromJavaxKeyTab(paramKeyTab, paramPrincipalName));
/*     */     }
/*     */     catch (KrbException localKrbException) {
/* 109 */       if (localKrbException.returnCode() == 44)
/*     */       {
/* 112 */         localEncryptionKey = EncryptionKey.findKey(i, Krb5Util.keysFromJavaxKeyTab(paramKeyTab, paramPrincipalName));
/*     */       }
/*     */     }
/*     */ 
/* 116 */     if (localEncryptionKey == null) {
/* 117 */       throw new KrbException(400, "Cannot find key for type/kvno to decrypt AS REP - " + EType.toString(i) + "/" + localInteger);
/*     */     }
/*     */ 
/* 121 */     decrypt(localEncryptionKey, paramKrbAsReq);
/*     */   }
/*     */ 
/*     */   void decryptUsingPassword(char[] paramArrayOfChar, KrbAsReq paramKrbAsReq, PrincipalName paramPrincipalName)
/*     */     throws KrbException, Asn1Exception, IOException
/*     */   {
/* 133 */     int i = this.rep.encPart.getEType();
/* 134 */     EncryptionKey localEncryptionKey = EncryptionKey.acquireSecretKey(paramPrincipalName, paramArrayOfChar, i, PAData.getSaltAndParams(i, this.rep.pAData));
/*     */ 
/* 139 */     decrypt(localEncryptionKey, paramKrbAsReq);
/*     */   }
/*     */ 
/*     */   private void decrypt(EncryptionKey paramEncryptionKey, KrbAsReq paramKrbAsReq)
/*     */     throws KrbException, Asn1Exception, IOException
/*     */   {
/* 149 */     byte[] arrayOfByte1 = this.rep.encPart.decrypt(paramEncryptionKey, 3);
/*     */ 
/* 151 */     byte[] arrayOfByte2 = this.rep.encPart.reset(arrayOfByte1);
/*     */ 
/* 153 */     DerValue localDerValue = new DerValue(arrayOfByte2);
/* 154 */     EncASRepPart localEncASRepPart = new EncASRepPart(localDerValue);
/* 155 */     this.rep.ticket.sname.setRealm(this.rep.ticket.realm);
/* 156 */     this.rep.encKDCRepPart = localEncASRepPart;
/*     */ 
/* 158 */     ASReq localASReq = paramKrbAsReq.getMessage();
/* 159 */     check(localASReq, this.rep);
/*     */ 
/* 161 */     this.creds = new Credentials(this.rep.ticket, localASReq.reqBody.cname, this.rep.ticket.sname, localEncASRepPart.key, localEncASRepPart.flags, localEncASRepPart.authtime, localEncASRepPart.starttime, localEncASRepPart.endtime, localEncASRepPart.renewTill, localEncASRepPart.caddr);
/*     */ 
/* 172 */     if (this.DEBUG)
/* 173 */       System.out.println(">>> KrbAsRep cons in KrbAsReq.getReply " + localASReq.reqBody.cname.getNameString());
/*     */   }
/*     */ 
/*     */   Credentials getCreds()
/*     */   {
/* 179 */     return (Credentials)Objects.requireNonNull(this.creds, "Creds not available yet.");
/*     */   }
/*     */ 
/*     */   sun.security.krb5.internal.ccache.Credentials getCCreds() {
/* 183 */     return new sun.security.krb5.internal.ccache.Credentials(this.rep);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.KrbAsRep
 * JD-Core Version:    0.6.2
 */