/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Vector;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.internal.ccache.CCacheOutputStream;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class AuthorizationData
/*     */   implements Cloneable
/*     */ {
/*  57 */   private AuthorizationDataEntry[] entry = null;
/*     */ 
/*     */   private AuthorizationData()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AuthorizationData(AuthorizationDataEntry[] paramArrayOfAuthorizationDataEntry) throws IOException {
/*  64 */     if (paramArrayOfAuthorizationDataEntry != null) {
/*  65 */       this.entry = new AuthorizationDataEntry[paramArrayOfAuthorizationDataEntry.length];
/*  66 */       for (int i = 0; i < paramArrayOfAuthorizationDataEntry.length; i++) {
/*  67 */         if (paramArrayOfAuthorizationDataEntry[i] == null) {
/*  68 */           throw new IOException("Cannot create an AuthorizationData");
/*     */         }
/*  70 */         this.entry[i] = ((AuthorizationDataEntry)paramArrayOfAuthorizationDataEntry[i].clone());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public AuthorizationData(AuthorizationDataEntry paramAuthorizationDataEntry)
/*     */   {
/*  77 */     this.entry = new AuthorizationDataEntry[1];
/*  78 */     this.entry[0] = paramAuthorizationDataEntry;
/*     */   }
/*     */ 
/*     */   public Object clone() {
/*  82 */     AuthorizationData localAuthorizationData = new AuthorizationData();
/*     */ 
/*  84 */     if (this.entry != null) {
/*  85 */       localAuthorizationData.entry = new AuthorizationDataEntry[this.entry.length];
/*     */ 
/*  87 */       for (int i = 0; i < this.entry.length; i++) {
/*  88 */         localAuthorizationData.entry[i] = ((AuthorizationDataEntry)this.entry[i].clone());
/*     */       }
/*     */     }
/*     */ 
/*  92 */     return localAuthorizationData;
/*     */   }
/*     */ 
/*     */   public AuthorizationData(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 102 */     Vector localVector = new Vector();
/* 103 */     if (paramDerValue.getTag() != 48) {
/* 104 */       throw new Asn1Exception(906);
/*     */     }
/* 106 */     while (paramDerValue.getData().available() > 0) {
/* 107 */       localVector.addElement(new AuthorizationDataEntry(paramDerValue.getData().getDerValue()));
/*     */     }
/* 109 */     if (localVector.size() > 0) {
/* 110 */       this.entry = new AuthorizationDataEntry[localVector.size()];
/* 111 */       localVector.copyInto(this.entry);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 122 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 123 */     DerValue[] arrayOfDerValue = new DerValue[this.entry.length];
/* 124 */     for (int i = 0; i < this.entry.length; i++) {
/* 125 */       arrayOfDerValue[i] = new DerValue(this.entry[i].asn1Encode());
/*     */     }
/* 127 */     localDerOutputStream.putSequence(arrayOfDerValue);
/* 128 */     return localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public static AuthorizationData parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 145 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte)) {
/* 146 */       return null;
/*     */     }
/* 148 */     DerValue localDerValue1 = paramDerInputStream.getDerValue();
/* 149 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/* 150 */       throw new Asn1Exception(906);
/*     */     }
/* 152 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 153 */     return new AuthorizationData(localDerValue2);
/*     */   }
/*     */ 
/*     */   public void writeAuth(CCacheOutputStream paramCCacheOutputStream)
/*     */     throws IOException
/*     */   {
/* 164 */     for (int i = 0; i < this.entry.length; i++)
/* 165 */       this.entry[i].writeEntry(paramCCacheOutputStream);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 170 */     String str = "AuthorizationData:\n";
/* 171 */     for (int i = 0; i < this.entry.length; i++) {
/* 172 */       str = str + this.entry[i].toString();
/*     */     }
/* 174 */     return str;
/*     */   }
/*     */ 
/*     */   public int count() {
/* 178 */     return this.entry.length;
/*     */   }
/*     */ 
/*     */   public AuthorizationDataEntry item(int paramInt) {
/* 182 */     return (AuthorizationDataEntry)this.entry[paramInt].clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.AuthorizationData
 * JD-Core Version:    0.6.2
 */