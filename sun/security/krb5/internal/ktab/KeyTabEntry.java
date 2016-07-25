/*     */ package sun.security.krb5.internal.ktab;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.Realm;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ 
/*     */ public class KeyTabEntry
/*     */   implements KeyTabConstants
/*     */ {
/*     */   PrincipalName service;
/*     */   Realm realm;
/*     */   KerberosTime timestamp;
/*     */   int keyVersion;
/*     */   int keyType;
/*  49 */   byte[] keyblock = null;
/*  50 */   boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   public KeyTabEntry(PrincipalName paramPrincipalName, Realm paramRealm, KerberosTime paramKerberosTime, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
/*     */   {
/*  54 */     this.service = paramPrincipalName;
/*  55 */     this.realm = paramRealm;
/*  56 */     this.timestamp = paramKerberosTime;
/*  57 */     this.keyVersion = paramInt1;
/*  58 */     this.keyType = paramInt2;
/*  59 */     if (paramArrayOfByte != null)
/*  60 */       this.keyblock = ((byte[])paramArrayOfByte.clone());
/*     */   }
/*     */ 
/*     */   public PrincipalName getService()
/*     */   {
/*  65 */     return this.service;
/*     */   }
/*     */ 
/*     */   public EncryptionKey getKey() {
/*  69 */     EncryptionKey localEncryptionKey = new EncryptionKey(this.keyblock, this.keyType, new Integer(this.keyVersion));
/*     */ 
/*  72 */     return localEncryptionKey;
/*     */   }
/*     */ 
/*     */   public String getKeyString() {
/*  76 */     StringBuffer localStringBuffer = new StringBuffer("0x");
/*  77 */     for (int i = 0; i < this.keyblock.length; i++) {
/*  78 */       localStringBuffer.append(String.format("%02x", new Object[] { Integer.valueOf(this.keyblock[i] & 0xFF) }));
/*     */     }
/*  80 */     return localStringBuffer.toString();
/*     */   }
/*     */   public int entryLength() {
/*  83 */     int i = 0;
/*  84 */     String[] arrayOfString = this.service.getNameStrings();
/*  85 */     for (int j = 0; j < arrayOfString.length; j++)
/*     */       try {
/*  87 */         i += 2 + arrayOfString[j].getBytes("8859_1").length;
/*     */       }
/*     */       catch (UnsupportedEncodingException localUnsupportedEncodingException1)
/*     */       {
/*     */       }
/*  92 */     j = 0;
/*     */     try {
/*  94 */       j = this.realm.toString().getBytes("8859_1").length;
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
/*     */     }
/*  98 */     int k = 4 + j + i + 4 + 4 + 1 + 2 + 2 + this.keyblock.length;
/*     */ 
/* 103 */     if (this.DEBUG) {
/* 104 */       System.out.println(">>> KeyTabEntry: key tab entry size is " + k);
/*     */     }
/* 106 */     return k;
/*     */   }
/*     */ 
/*     */   public KerberosTime getTimeStamp() {
/* 110 */     return this.timestamp;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.ktab.KeyTabEntry
 * JD-Core Version:    0.6.2
 */