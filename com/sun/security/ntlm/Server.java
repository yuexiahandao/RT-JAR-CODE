/*     */ package com.sun.security.ntlm;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public abstract class Server extends NTLM
/*     */ {
/*     */   private final String domain;
/*     */   private final boolean allVersion;
/*     */ 
/*     */   public Server(String paramString1, String paramString2)
/*     */     throws NTLMException
/*     */   {
/*  68 */     super(paramString1);
/*  69 */     if (paramString2 == null) {
/*  70 */       throw new NTLMException(6, "domain cannot be null");
/*     */     }
/*     */ 
/*  73 */     this.allVersion = (paramString1 == null);
/*  74 */     this.domain = paramString2;
/*  75 */     debug("NTLM Server: (t,version) = (%s,%s)\n", new Object[] { paramString2, paramString1 });
/*     */   }
/*     */ 
/*     */   public byte[] type2(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws NTLMException
/*     */   {
/*  88 */     if (paramArrayOfByte2 == null) {
/*  89 */       throw new NTLMException(6, "nonce cannot be null");
/*     */     }
/*     */ 
/*  92 */     debug("NTLM Server: Type 1 received\n", new Object[0]);
/*  93 */     if (paramArrayOfByte1 != null) debug(paramArrayOfByte1);
/*  94 */     NTLM.Writer localWriter = new NTLM.Writer(2, 32);
/*  95 */     int i = 524805;
/*  96 */     localWriter.writeSecurityBuffer(12, this.domain, true);
/*  97 */     localWriter.writeInt(20, i);
/*  98 */     localWriter.writeBytes(24, paramArrayOfByte2);
/*  99 */     debug("NTLM Server: Type 2 created\n", new Object[0]);
/* 100 */     debug(localWriter.getBytes());
/* 101 */     return localWriter.getBytes();
/*     */   }
/*     */ 
/*     */   public String[] verify(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws NTLMException
/*     */   {
/* 115 */     if ((paramArrayOfByte1 == null) || (paramArrayOfByte2 == null)) {
/* 116 */       throw new NTLMException(6, "type1 or nonce cannot be null");
/*     */     }
/*     */ 
/* 119 */     debug("NTLM Server: Type 3 received\n", new Object[0]);
/* 120 */     if (paramArrayOfByte1 != null) debug(paramArrayOfByte1);
/* 121 */     NTLM.Reader localReader = new NTLM.Reader(paramArrayOfByte1);
/* 122 */     String str1 = localReader.readSecurityBuffer(36, true);
/* 123 */     String str2 = localReader.readSecurityBuffer(44, true);
/* 124 */     String str3 = localReader.readSecurityBuffer(28, true);
/*     */ 
/* 130 */     boolean bool = false;
/* 131 */     char[] arrayOfChar = getPassword(this.domain, str1);
/* 132 */     if (arrayOfChar == null) {
/* 133 */       throw new NTLMException(3, "Unknown user");
/*     */     }
/*     */ 
/* 136 */     byte[] arrayOfByte1 = localReader.readSecurityBuffer(12);
/* 137 */     byte[] arrayOfByte2 = localReader.readSecurityBuffer(20);
/*     */     byte[] arrayOfByte3;
/*     */     byte[] arrayOfByte4;
/*     */     byte[] arrayOfByte5;
/* 139 */     if ((!bool) && ((this.allVersion) || (this.v == Version.NTLM))) {
/* 140 */       if (arrayOfByte1.length > 0) {
/* 141 */         arrayOfByte3 = getP1(arrayOfChar);
/* 142 */         arrayOfByte4 = calcLMHash(arrayOfByte3);
/* 143 */         arrayOfByte5 = calcResponse(arrayOfByte4, paramArrayOfByte2);
/* 144 */         if (Arrays.equals(arrayOfByte5, arrayOfByte1)) {
/* 145 */           bool = true;
/*     */         }
/*     */       }
/* 148 */       if (arrayOfByte2.length > 0) {
/* 149 */         arrayOfByte3 = getP2(arrayOfChar);
/* 150 */         arrayOfByte4 = calcNTHash(arrayOfByte3);
/* 151 */         arrayOfByte5 = calcResponse(arrayOfByte4, paramArrayOfByte2);
/* 152 */         if (Arrays.equals(arrayOfByte5, arrayOfByte2)) {
/* 153 */           bool = true;
/*     */         }
/*     */       }
/* 156 */       debug("NTLM Server: verify using NTLM: " + bool + "\n", new Object[0]);
/*     */     }
/*     */     byte[] arrayOfByte6;
/* 158 */     if ((!bool) && ((this.allVersion) || (this.v == Version.NTLM2))) {
/* 159 */       arrayOfByte3 = getP2(arrayOfChar);
/* 160 */       arrayOfByte4 = calcNTHash(arrayOfByte3);
/* 161 */       arrayOfByte5 = Arrays.copyOf(arrayOfByte1, 8);
/* 162 */       arrayOfByte6 = ntlm2NTLM(arrayOfByte4, arrayOfByte5, paramArrayOfByte2);
/* 163 */       if (Arrays.equals(arrayOfByte2, arrayOfByte6)) {
/* 164 */         bool = true;
/*     */       }
/* 166 */       debug("NTLM Server: verify using NTLM2: " + bool + "\n", new Object[0]);
/*     */     }
/* 168 */     if ((!bool) && ((this.allVersion) || (this.v == Version.NTLMv2))) {
/* 169 */       arrayOfByte3 = getP2(arrayOfChar);
/* 170 */       arrayOfByte4 = calcNTHash(arrayOfByte3);
/* 171 */       if (arrayOfByte1.length > 0) {
/* 172 */         arrayOfByte5 = Arrays.copyOfRange(arrayOfByte1, 16, arrayOfByte1.length);
/*     */ 
/* 174 */         arrayOfByte6 = calcV2(arrayOfByte4, str1.toUpperCase(Locale.US) + str3, arrayOfByte5, paramArrayOfByte2);
/*     */ 
/* 177 */         if (Arrays.equals(arrayOfByte6, arrayOfByte1)) {
/* 178 */           bool = true;
/*     */         }
/*     */       }
/* 181 */       if (arrayOfByte2.length > 0) {
/* 182 */         arrayOfByte5 = Arrays.copyOfRange(arrayOfByte2, 16, arrayOfByte2.length);
/*     */ 
/* 184 */         arrayOfByte6 = calcV2(arrayOfByte4, str1.toUpperCase(Locale.US) + str3, arrayOfByte5, paramArrayOfByte2);
/*     */ 
/* 187 */         if (Arrays.equals(arrayOfByte6, arrayOfByte2)) {
/* 188 */           bool = true;
/*     */         }
/*     */       }
/* 191 */       debug("NTLM Server: verify using NTLMv2: " + bool + "\n", new Object[0]);
/*     */     }
/* 193 */     if (!bool) {
/* 194 */       throw new NTLMException(4, "None of LM and NTLM verified");
/*     */     }
/*     */ 
/* 197 */     return new String[] { str1, str2 };
/*     */   }
/*     */ 
/*     */   public abstract char[] getPassword(String paramString1, String paramString2);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.ntlm.Server
 * JD-Core Version:    0.6.2
 */