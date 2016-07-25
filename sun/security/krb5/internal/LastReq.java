/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Vector;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class LastReq
/*     */ {
/*  56 */   private LastReqEntry[] entry = null;
/*     */ 
/*     */   public LastReq(LastReqEntry[] paramArrayOfLastReqEntry) throws IOException {
/*  59 */     if (paramArrayOfLastReqEntry != null) {
/*  60 */       this.entry = new LastReqEntry[paramArrayOfLastReqEntry.length];
/*  61 */       for (int i = 0; i < paramArrayOfLastReqEntry.length; i++) {
/*  62 */         if (paramArrayOfLastReqEntry[i] == null) {
/*  63 */           throw new IOException("Cannot create a LastReqEntry");
/*     */         }
/*  65 */         this.entry[i] = ((LastReqEntry)paramArrayOfLastReqEntry[i].clone());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public LastReq(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/*  80 */     Vector localVector = new Vector();
/*  81 */     if (paramDerValue.getTag() != 48) {
/*  82 */       throw new Asn1Exception(906);
/*     */     }
/*  84 */     while (paramDerValue.getData().available() > 0) {
/*  85 */       localVector.addElement(new LastReqEntry(paramDerValue.getData().getDerValue()));
/*     */     }
/*  87 */     if (localVector.size() > 0) {
/*  88 */       this.entry = new LastReqEntry[localVector.size()];
/*  89 */       localVector.copyInto(this.entry);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 100 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 101 */     if ((this.entry != null) && (this.entry.length > 0)) {
/* 102 */       DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 103 */       for (int i = 0; i < this.entry.length; i++)
/* 104 */         localDerOutputStream2.write(this.entry[i].asn1Encode());
/* 105 */       localDerOutputStream1.write((byte)48, localDerOutputStream2);
/* 106 */       return localDerOutputStream1.toByteArray();
/*     */     }
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */   public static LastReq parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 125 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte))
/* 126 */       return null;
/* 127 */     DerValue localDerValue1 = paramDerInputStream.getDerValue();
/* 128 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/* 129 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/* 132 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 133 */     return new LastReq(localDerValue2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.LastReq
 * JD-Core Version:    0.6.2
 */