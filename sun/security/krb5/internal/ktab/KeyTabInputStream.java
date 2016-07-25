/*     */ package sun.security.krb5.internal.ktab;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.Realm;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.util.KrbDataInputStream;
/*     */ 
/*     */ public class KeyTabInputStream extends KrbDataInputStream
/*     */   implements KeyTabConstants
/*     */ {
/*  51 */   boolean DEBUG = Krb5.DEBUG;
/*     */   int index;
/*     */ 
/*     */   public KeyTabInputStream(InputStream paramInputStream)
/*     */   {
/*  55 */     super(paramInputStream);
/*     */   }
/*     */ 
/*     */   int readEntryLength()
/*     */     throws IOException
/*     */   {
/*  61 */     return read(4);
/*     */   }
/*     */ 
/*     */   KeyTabEntry readEntry(int paramInt1, int paramInt2) throws IOException, RealmException
/*     */   {
/*  66 */     this.index = paramInt1;
/*  67 */     if (this.index == 0) {
/*  68 */       return null;
/*     */     }
/*  70 */     if (this.index < 0) {
/*  71 */       skip(Math.abs(this.index));
/*  72 */       return null;
/*     */     }
/*  74 */     int i = read(2);
/*  75 */     this.index -= 2;
/*  76 */     if (paramInt2 == 1281) {
/*  77 */       i--;
/*     */     }
/*  79 */     Realm localRealm = new Realm(readName());
/*  80 */     String[] arrayOfString = new String[i];
/*  81 */     for (int j = 0; j < i; j++) {
/*  82 */       arrayOfString[j] = readName();
/*     */     }
/*  84 */     j = read(4);
/*  85 */     this.index -= 4;
/*  86 */     PrincipalName localPrincipalName = new PrincipalName(arrayOfString, j);
/*  87 */     localPrincipalName.setRealm(localRealm);
/*  88 */     KerberosTime localKerberosTime = readTimeStamp();
/*     */ 
/*  90 */     int k = read() & 0xFF;
/*  91 */     this.index -= 1;
/*  92 */     int m = read(2);
/*  93 */     this.index -= 2;
/*  94 */     int n = read(2);
/*  95 */     this.index -= 2;
/*  96 */     byte[] arrayOfByte = readKey(n);
/*  97 */     this.index -= n;
/*     */ 
/* 101 */     if (this.index >= 4) {
/* 102 */       int i1 = read(4);
/* 103 */       if (i1 != 0) {
/* 104 */         k = i1;
/*     */       }
/* 106 */       this.index -= 4;
/*     */     }
/*     */ 
/* 110 */     if (this.index < 0) {
/* 111 */       throw new RealmException("Keytab is corrupted");
/*     */     }
/*     */ 
/* 115 */     skip(this.index);
/*     */ 
/* 117 */     return new KeyTabEntry(localPrincipalName, localRealm, localKerberosTime, k, m, arrayOfByte);
/*     */   }
/*     */ 
/*     */   byte[] readKey(int paramInt) throws IOException {
/* 121 */     byte[] arrayOfByte = new byte[paramInt];
/* 122 */     read(arrayOfByte, 0, paramInt);
/* 123 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   KerberosTime readTimeStamp() throws IOException {
/* 127 */     this.index -= 4;
/* 128 */     return new KerberosTime(read(4) * 1000L);
/*     */   }
/*     */ 
/*     */   String readName() throws IOException
/*     */   {
/* 133 */     int i = read(2);
/* 134 */     this.index -= 2;
/* 135 */     byte[] arrayOfByte = new byte[i];
/* 136 */     read(arrayOfByte, 0, i);
/* 137 */     this.index -= i;
/* 138 */     String str = new String(arrayOfByte);
/* 139 */     if (this.DEBUG) {
/* 140 */       System.out.println(">>> KeyTabInputStream, readName(): " + str);
/*     */     }
/* 142 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.ktab.KeyTabInputStream
 * JD-Core Version:    0.6.2
 */