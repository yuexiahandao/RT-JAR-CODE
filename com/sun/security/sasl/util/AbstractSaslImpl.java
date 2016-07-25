/*     */ package com.sun.security.sasl.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.security.sasl.SaslException;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ 
/*     */ public abstract class AbstractSaslImpl
/*     */ {
/*  52 */   protected boolean completed = false;
/*  53 */   protected boolean privacy = false;
/*  54 */   protected boolean integrity = false;
/*     */   protected byte[] qop;
/*     */   protected byte allQop;
/*     */   protected byte[] strength;
/*  60 */   protected int sendMaxBufSize = 0;
/*  61 */   protected int recvMaxBufSize = 65536;
/*     */   protected int rawSendSize;
/*     */   protected String myClassName;
/*     */   private static final String SASL_LOGGER_NAME = "javax.security.sasl";
/*     */   protected static final String MAX_SEND_BUF = "javax.security.sasl.sendmaxbuffer";
/* 338 */   protected static final Logger logger = Logger.getLogger("javax.security.sasl");
/*     */   protected static final byte NO_PROTECTION = 1;
/*     */   protected static final byte INTEGRITY_ONLY_PROTECTION = 2;
/*     */   protected static final byte PRIVACY_PROTECTION = 4;
/*     */   protected static final byte LOW_STRENGTH = 1;
/*     */   protected static final byte MEDIUM_STRENGTH = 2;
/*     */   protected static final byte HIGH_STRENGTH = 4;
/* 349 */   private static final byte[] DEFAULT_QOP = { 1 };
/* 350 */   private static final String[] QOP_TOKENS = { "auth-conf", "auth-int", "auth" };
/*     */ 
/* 353 */   private static final byte[] QOP_MASKS = { 4, 2, 1 };
/*     */ 
/* 357 */   private static final byte[] DEFAULT_STRENGTH = { 4, 2, 1 };
/*     */ 
/* 359 */   private static final String[] STRENGTH_TOKENS = { "low", "medium", "high" };
/*     */ 
/* 362 */   private static final byte[] STRENGTH_MASKS = { 1, 2, 4 };
/*     */ 
/*     */   protected AbstractSaslImpl(Map paramMap, String paramString)
/*     */     throws SaslException
/*     */   {
/*  67 */     this.myClassName = paramString;
/*     */ 
/*  70 */     if (paramMap != null)
/*     */     {
/*  74 */       this.qop = parseQop(str = (String)paramMap.get("javax.security.sasl.qop"));
/*  75 */       logger.logp(Level.FINE, this.myClassName, "constructor", "SASLIMPL01:Preferred qop property: {0}", str);
/*     */ 
/*  77 */       this.allQop = combineMasks(this.qop);
/*     */       StringBuffer localStringBuffer;
/*     */       int i;
/*  79 */       if (logger.isLoggable(Level.FINE)) {
/*  80 */         logger.logp(Level.FINE, this.myClassName, "constructor", "SASLIMPL02:Preferred qop mask: {0}", new Byte(this.allQop));
/*     */ 
/*  83 */         if (this.qop.length > 0) {
/*  84 */           localStringBuffer = new StringBuffer();
/*  85 */           for (i = 0; i < this.qop.length; i++) {
/*  86 */             localStringBuffer.append(Byte.toString(this.qop[i]));
/*  87 */             localStringBuffer.append(' ');
/*     */           }
/*  89 */           logger.logp(Level.FINE, this.myClassName, "constructor", "SASLIMPL03:Preferred qops : {0}", localStringBuffer.toString());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*  95 */       this.strength = parseStrength(str = (String)paramMap.get("javax.security.sasl.strength"));
/*  96 */       logger.logp(Level.FINE, this.myClassName, "constructor", "SASLIMPL04:Preferred strength property: {0}", str);
/*     */ 
/*  98 */       if ((logger.isLoggable(Level.FINE)) && (this.strength.length > 0)) {
/*  99 */         localStringBuffer = new StringBuffer();
/* 100 */         for (i = 0; i < this.strength.length; i++) {
/* 101 */           localStringBuffer.append(Byte.toString(this.strength[i]));
/* 102 */           localStringBuffer.append(' ');
/*     */         }
/* 104 */         logger.logp(Level.FINE, this.myClassName, "constructor", "SASLIMPL05:Cipher strengths: {0}", localStringBuffer.toString());
/*     */       }
/*     */ 
/* 109 */       String str = (String)paramMap.get("javax.security.sasl.maxbuffer");
/* 110 */       if (str != null) {
/*     */         try {
/* 112 */           logger.logp(Level.FINE, this.myClassName, "constructor", "SASLIMPL06:Max receive buffer size: {0}", str);
/*     */ 
/* 114 */           this.recvMaxBufSize = Integer.parseInt(str);
/*     */         } catch (NumberFormatException localNumberFormatException1) {
/* 116 */           throw new SaslException("Property must be string representation of integer: javax.security.sasl.maxbuffer");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 123 */       str = (String)paramMap.get("javax.security.sasl.sendmaxbuffer");
/* 124 */       if (str != null) {
/*     */         try {
/* 126 */           logger.logp(Level.FINE, this.myClassName, "constructor", "SASLIMPL07:Max send buffer size: {0}", str);
/*     */ 
/* 128 */           this.sendMaxBufSize = Integer.parseInt(str);
/*     */         } catch (NumberFormatException localNumberFormatException2) {
/* 130 */           throw new SaslException("Property must be string representation of integer: javax.security.sasl.sendmaxbuffer");
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 136 */       this.qop = DEFAULT_QOP;
/* 137 */       this.allQop = 1;
/* 138 */       this.strength = STRENGTH_MASKS;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isComplete()
/*     */   {
/* 148 */     return this.completed;
/*     */   }
/*     */ 
/*     */   public Object getNegotiatedProperty(String paramString)
/*     */   {
/* 156 */     if (!this.completed) {
/* 157 */       throw new IllegalStateException("SASL authentication not completed");
/*     */     }
/*     */ 
/* 160 */     if (paramString.equals("javax.security.sasl.qop")) {
/* 161 */       if (this.privacy)
/* 162 */         return "auth-conf";
/* 163 */       if (this.integrity) {
/* 164 */         return "auth-int";
/*     */       }
/* 166 */       return "auth";
/*     */     }
/* 168 */     if (paramString.equals("javax.security.sasl.maxbuffer"))
/* 169 */       return Integer.toString(this.recvMaxBufSize);
/* 170 */     if (paramString.equals("javax.security.sasl.rawsendsize"))
/* 171 */       return Integer.toString(this.rawSendSize);
/* 172 */     if (paramString.equals("javax.security.sasl.sendmaxbuffer")) {
/* 173 */       return Integer.toString(this.sendMaxBufSize);
/*     */     }
/* 175 */     return null;
/*     */   }
/*     */ 
/*     */   protected static final byte combineMasks(byte[] paramArrayOfByte)
/*     */   {
/* 180 */     byte b = 0;
/* 181 */     for (int i = 0; i < paramArrayOfByte.length; i++) {
/* 182 */       b = (byte)(b | paramArrayOfByte[i]);
/*     */     }
/* 184 */     return b;
/*     */   }
/*     */ 
/*     */   protected static final byte findPreferredMask(byte paramByte, byte[] paramArrayOfByte) {
/* 188 */     for (int i = 0; i < paramArrayOfByte.length; i++) {
/* 189 */       if ((paramArrayOfByte[i] & paramByte) != 0) {
/* 190 */         return paramArrayOfByte[i];
/*     */       }
/*     */     }
/* 193 */     return 0;
/*     */   }
/*     */ 
/*     */   private static final byte[] parseQop(String paramString) throws SaslException {
/* 197 */     return parseQop(paramString, null, false);
/*     */   }
/*     */ 
/*     */   protected static final byte[] parseQop(String paramString, String[] paramArrayOfString, boolean paramBoolean) throws SaslException
/*     */   {
/* 202 */     if (paramString == null) {
/* 203 */       return DEFAULT_QOP;
/*     */     }
/*     */ 
/* 206 */     return parseProp("javax.security.sasl.qop", paramString, QOP_TOKENS, QOP_MASKS, paramArrayOfString, paramBoolean);
/*     */   }
/*     */ 
/*     */   private static final byte[] parseStrength(String paramString) throws SaslException
/*     */   {
/* 211 */     if (paramString == null) {
/* 212 */       return DEFAULT_STRENGTH;
/*     */     }
/*     */ 
/* 215 */     return parseProp("javax.security.sasl.strength", paramString, STRENGTH_TOKENS, STRENGTH_MASKS, null, false);
/*     */   }
/*     */ 
/*     */   private static final byte[] parseProp(String paramString1, String paramString2, String[] paramArrayOfString1, byte[] paramArrayOfByte, String[] paramArrayOfString2, boolean paramBoolean)
/*     */     throws SaslException
/*     */   {
/* 223 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString2, ", \t\n");
/*     */ 
/* 225 */     byte[] arrayOfByte = new byte[paramArrayOfString1.length];
/* 226 */     int i = 0;
/*     */ 
/* 229 */     while ((localStringTokenizer.hasMoreTokens()) && (i < arrayOfByte.length)) {
/* 230 */       String str = localStringTokenizer.nextToken();
/* 231 */       int j = 0;
/* 232 */       for (k = 0; (j == 0) && (k < paramArrayOfString1.length); k++) {
/* 233 */         if (str.equalsIgnoreCase(paramArrayOfString1[k])) {
/* 234 */           j = 1;
/* 235 */           arrayOfByte[(i++)] = paramArrayOfByte[k];
/* 236 */           if (paramArrayOfString2 != null) {
/* 237 */             paramArrayOfString2[k] = str;
/*     */           }
/*     */         }
/*     */       }
/* 241 */       if ((j == 0) && (!paramBoolean)) {
/* 242 */         throw new SaslException("Invalid token in " + paramString1 + ": " + paramString2);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 247 */     for (int k = i; k < arrayOfByte.length; k++) {
/* 248 */       arrayOfByte[k] = 0;
/*     */     }
/* 250 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   protected static final void traceOutput(String paramString1, String paramString2, String paramString3, byte[] paramArrayOfByte)
/*     */   {
/* 259 */     traceOutput(paramString1, paramString2, paramString3, paramArrayOfByte, 0, paramArrayOfByte == null ? 0 : paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   protected static final void traceOutput(String paramString1, String paramString2, String paramString3, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*     */     try
/*     */     {
/* 266 */       int i = paramInt2;
/*     */       Level localLevel;
/* 269 */       if (!logger.isLoggable(Level.FINEST)) {
/* 270 */         paramInt2 = Math.min(16, paramInt2);
/* 271 */         localLevel = Level.FINER;
/*     */       } else {
/* 273 */         localLevel = Level.FINEST;
/*     */       }
/*     */       String str;
/* 278 */       if (paramArrayOfByte != null) {
/* 279 */         ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(paramInt2);
/* 280 */         new HexDumpEncoder().encodeBuffer(new ByteArrayInputStream(paramArrayOfByte, paramInt1, paramInt2), localByteArrayOutputStream);
/*     */ 
/* 282 */         str = localByteArrayOutputStream.toString();
/*     */       } else {
/* 284 */         str = "NULL";
/*     */       }
/*     */ 
/* 288 */       logger.logp(localLevel, paramString1, paramString2, "{0} ( {1} ): {2}", new Object[] { paramString3, new Integer(i), str });
/*     */     }
/*     */     catch (Exception localException) {
/* 291 */       logger.logp(Level.WARNING, paramString1, paramString2, "SASLIMPL09:Error generating trace output: {0}", localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static final int networkByteOrderToInt(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 302 */     if (paramInt2 > 4) {
/* 303 */       throw new IllegalArgumentException("Cannot handle more than 4 bytes");
/*     */     }
/*     */ 
/* 306 */     int i = 0;
/*     */ 
/* 308 */     for (int j = 0; j < paramInt2; j++) {
/* 309 */       i <<= 8;
/* 310 */       i |= paramArrayOfByte[(paramInt1 + j)] & 0xFF;
/*     */     }
/* 312 */     return i;
/*     */   }
/*     */ 
/*     */   protected static final void intToNetworkByteOrder(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
/*     */   {
/* 321 */     if (paramInt3 > 4) {
/* 322 */       throw new IllegalArgumentException("Cannot handle more than 4 bytes");
/*     */     }
/*     */ 
/* 325 */     for (int i = paramInt3 - 1; i >= 0; i--) {
/* 326 */       paramArrayOfByte[(paramInt2 + i)] = ((byte)(paramInt1 & 0xFF));
/* 327 */       paramInt1 >>>= 8;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.util.AbstractSaslImpl
 * JD-Core Version:    0.6.2
 */