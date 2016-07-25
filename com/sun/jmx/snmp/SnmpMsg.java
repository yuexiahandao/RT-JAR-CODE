/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public abstract class SnmpMsg
/*     */   implements SnmpDefinitions
/*     */ {
/*  52 */   public int version = 0;
/*     */ 
/*  61 */   public byte[] data = null;
/*     */ 
/*  66 */   public int dataLength = 0;
/*     */ 
/*  73 */   public InetAddress address = null;
/*     */ 
/*  80 */   public int port = 0;
/*     */ 
/*  84 */   public SnmpSecurityParameters securityParameters = null;
/*     */ 
/*     */   public static int getProtocolVersion(byte[] paramArrayOfByte)
/*     */     throws SnmpStatusException
/*     */   {
/*  92 */     int i = 0;
/*  93 */     BerDecoder localBerDecoder = null;
/*     */     try {
/*  95 */       localBerDecoder = new BerDecoder(paramArrayOfByte);
/*  96 */       localBerDecoder.openSequence();
/*  97 */       i = localBerDecoder.fetchInteger();
/*     */     }
/*     */     catch (BerException localBerException1) {
/* 100 */       throw new SnmpStatusException("Invalid encoding");
/*     */     }
/*     */     try {
/* 103 */       localBerDecoder.closeSequence();
/*     */     }
/*     */     catch (BerException localBerException2) {
/*     */     }
/* 107 */     return i;
/*     */   }
/*     */ 
/*     */   public abstract int getRequestId(byte[] paramArrayOfByte)
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   public abstract int encodeMessage(byte[] paramArrayOfByte)
/*     */     throws SnmpTooBigException;
/*     */ 
/*     */   public abstract void decodeMessage(byte[] paramArrayOfByte, int paramInt)
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   public abstract void encodeSnmpPdu(SnmpPdu paramSnmpPdu, int paramInt)
/*     */     throws SnmpStatusException, SnmpTooBigException;
/*     */ 
/*     */   public abstract SnmpPdu decodeSnmpPdu()
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   public static String dumpHexBuffer(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 184 */     StringBuffer localStringBuffer = new StringBuffer(paramInt2 << 1);
/* 185 */     int i = 1;
/* 186 */     int j = paramInt1 + paramInt2;
/*     */ 
/* 188 */     for (int k = paramInt1; k < j; k++) {
/* 189 */       int m = paramArrayOfByte[k] & 0xFF;
/* 190 */       localStringBuffer.append(Character.forDigit(m >>> 4, 16));
/* 191 */       localStringBuffer.append(Character.forDigit(m & 0xF, 16));
/* 192 */       i++;
/* 193 */       if (i % 16 == 0) {
/* 194 */         localStringBuffer.append('\n');
/* 195 */         i = 1;
/*     */       } else {
/* 197 */         localStringBuffer.append(' ');
/*     */       }
/*     */     }
/* 199 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String printMessage()
/*     */   {
/* 208 */     StringBuffer localStringBuffer = new StringBuffer();
/* 209 */     localStringBuffer.append("Version: ");
/* 210 */     localStringBuffer.append(this.version);
/* 211 */     localStringBuffer.append("\n");
/* 212 */     if (this.data == null) {
/* 213 */       localStringBuffer.append("Data: null");
/*     */     }
/*     */     else {
/* 216 */       localStringBuffer.append("Data: {\n");
/* 217 */       localStringBuffer.append(dumpHexBuffer(this.data, 0, this.dataLength));
/* 218 */       localStringBuffer.append("\n}\n");
/*     */     }
/*     */ 
/* 221 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public void encodeVarBindList(BerEncoder paramBerEncoder, SnmpVarBind[] paramArrayOfSnmpVarBind)
/*     */     throws SnmpStatusException, SnmpTooBigException
/*     */   {
/* 233 */     int i = 0;
/*     */     try {
/* 235 */       paramBerEncoder.openSequence();
/* 236 */       if (paramArrayOfSnmpVarBind != null) {
/* 237 */         for (int j = paramArrayOfSnmpVarBind.length - 1; j >= 0; j--) {
/* 238 */           SnmpVarBind localSnmpVarBind = paramArrayOfSnmpVarBind[j];
/* 239 */           if (localSnmpVarBind != null) {
/* 240 */             paramBerEncoder.openSequence();
/* 241 */             encodeVarBindValue(paramBerEncoder, localSnmpVarBind.value);
/* 242 */             paramBerEncoder.putOid(localSnmpVarBind.oid.longValue());
/* 243 */             paramBerEncoder.closeSequence();
/* 244 */             i++;
/*     */           }
/*     */         }
/*     */       }
/* 248 */       paramBerEncoder.closeSequence();
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 251 */       throw new SnmpTooBigException(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   void encodeVarBindValue(BerEncoder paramBerEncoder, SnmpValue paramSnmpValue)
/*     */     throws SnmpStatusException
/*     */   {
/* 260 */     if (paramSnmpValue == null) {
/* 261 */       paramBerEncoder.putNull();
/*     */     }
/* 263 */     else if ((paramSnmpValue instanceof SnmpIpAddress)) {
/* 264 */       paramBerEncoder.putOctetString(((SnmpIpAddress)paramSnmpValue).byteValue(), 64);
/*     */     }
/* 266 */     else if ((paramSnmpValue instanceof SnmpCounter)) {
/* 267 */       paramBerEncoder.putInteger(((SnmpCounter)paramSnmpValue).longValue(), 65);
/*     */     }
/* 269 */     else if ((paramSnmpValue instanceof SnmpGauge)) {
/* 270 */       paramBerEncoder.putInteger(((SnmpGauge)paramSnmpValue).longValue(), 66);
/*     */     }
/* 272 */     else if ((paramSnmpValue instanceof SnmpTimeticks)) {
/* 273 */       paramBerEncoder.putInteger(((SnmpTimeticks)paramSnmpValue).longValue(), 67);
/*     */     }
/* 275 */     else if ((paramSnmpValue instanceof SnmpOpaque)) {
/* 276 */       paramBerEncoder.putOctetString(((SnmpOpaque)paramSnmpValue).byteValue(), 68);
/*     */     }
/* 278 */     else if ((paramSnmpValue instanceof SnmpInt)) {
/* 279 */       paramBerEncoder.putInteger(((SnmpInt)paramSnmpValue).intValue());
/*     */     }
/* 281 */     else if ((paramSnmpValue instanceof SnmpString)) {
/* 282 */       paramBerEncoder.putOctetString(((SnmpString)paramSnmpValue).byteValue());
/*     */     }
/* 284 */     else if ((paramSnmpValue instanceof SnmpOid)) {
/* 285 */       paramBerEncoder.putOid(((SnmpOid)paramSnmpValue).longValue());
/*     */     }
/* 287 */     else if ((paramSnmpValue instanceof SnmpCounter64)) {
/* 288 */       if (this.version == 0) {
/* 289 */         throw new SnmpStatusException("Invalid value for SNMP v1 : " + paramSnmpValue);
/*     */       }
/* 291 */       paramBerEncoder.putInteger(((SnmpCounter64)paramSnmpValue).longValue(), 70);
/*     */     }
/* 293 */     else if ((paramSnmpValue instanceof SnmpNull)) {
/* 294 */       int i = ((SnmpNull)paramSnmpValue).getTag();
/* 295 */       if ((this.version == 0) && (i != 5)) {
/* 296 */         throw new SnmpStatusException("Invalid value for SNMP v1 : " + paramSnmpValue);
/*     */       }
/* 298 */       if ((this.version == 1) && (i != 5) && (i != 128) && (i != 129) && (i != 130))
/*     */       {
/* 303 */         throw new SnmpStatusException("Invalid value " + paramSnmpValue);
/*     */       }
/* 305 */       paramBerEncoder.putNull(i);
/*     */     }
/*     */     else {
/* 308 */       throw new SnmpStatusException("Invalid value " + paramSnmpValue);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpVarBind[] decodeVarBindList(BerDecoder paramBerDecoder)
/*     */     throws BerException
/*     */   {
/* 319 */     paramBerDecoder.openSequence();
/* 320 */     Vector localVector = new Vector();
/* 321 */     while (paramBerDecoder.cannotCloseSequence()) {
/* 322 */       localObject = new SnmpVarBind();
/* 323 */       paramBerDecoder.openSequence();
/* 324 */       ((SnmpVarBind)localObject).oid = new SnmpOid(paramBerDecoder.fetchOid());
/* 325 */       ((SnmpVarBind)localObject).setSnmpValue(decodeVarBindValue(paramBerDecoder));
/* 326 */       paramBerDecoder.closeSequence();
/* 327 */       localVector.addElement(localObject);
/*     */     }
/* 329 */     paramBerDecoder.closeSequence();
/* 330 */     Object localObject = new SnmpVarBind[localVector.size()];
/* 331 */     localVector.copyInto((Object[])localObject);
/* 332 */     return localObject;
/*     */   }
/*     */ 
/*     */   SnmpValue decodeVarBindValue(BerDecoder paramBerDecoder)
/*     */     throws BerException
/*     */   {
/* 341 */     Object localObject = null;
/* 342 */     int i = paramBerDecoder.getTag();
/*     */ 
/* 346 */     switch (i)
/*     */     {
/*     */     case 2:
/*     */       try
/*     */       {
/* 353 */         localObject = new SnmpInt(paramBerDecoder.fetchInteger());
/*     */       } catch (RuntimeException localRuntimeException1) {
/* 355 */         throw new BerException();
/*     */       }
/*     */ 
/*     */     case 4:
/*     */       try
/*     */       {
/* 361 */         localObject = new SnmpString(paramBerDecoder.fetchOctetString());
/*     */       } catch (RuntimeException localRuntimeException2) {
/* 363 */         throw new BerException();
/*     */       }
/*     */ 
/*     */     case 6:
/*     */       try
/*     */       {
/* 369 */         localObject = new SnmpOid(paramBerDecoder.fetchOid());
/*     */       } catch (RuntimeException localRuntimeException3) {
/* 371 */         throw new BerException();
/*     */       }
/*     */ 
/*     */     case 5:
/* 376 */       paramBerDecoder.fetchNull();
/*     */       try {
/* 378 */         localObject = new SnmpNull();
/*     */       } catch (RuntimeException localRuntimeException4) {
/* 380 */         throw new BerException();
/*     */       }
/*     */ 
/*     */     case 64:
/*     */       try
/*     */       {
/* 390 */         localObject = new SnmpIpAddress(paramBerDecoder.fetchOctetString(i));
/*     */       } catch (RuntimeException localRuntimeException5) {
/* 392 */         throw new BerException();
/*     */       }
/*     */ 
/*     */     case 65:
/*     */       try
/*     */       {
/* 398 */         localObject = new SnmpCounter(paramBerDecoder.fetchIntegerAsLong(i));
/*     */       } catch (RuntimeException localRuntimeException6) {
/* 400 */         throw new BerException();
/*     */       }
/*     */ 
/*     */     case 66:
/*     */       try
/*     */       {
/* 406 */         localObject = new SnmpGauge(paramBerDecoder.fetchIntegerAsLong(i));
/*     */       } catch (RuntimeException localRuntimeException7) {
/* 408 */         throw new BerException();
/*     */       }
/*     */ 
/*     */     case 67:
/*     */       try
/*     */       {
/* 414 */         localObject = new SnmpTimeticks(paramBerDecoder.fetchIntegerAsLong(i));
/*     */       } catch (RuntimeException localRuntimeException8) {
/* 416 */         throw new BerException();
/*     */       }
/*     */ 
/*     */     case 68:
/*     */       try
/*     */       {
/* 422 */         localObject = new SnmpOpaque(paramBerDecoder.fetchOctetString(i));
/*     */       } catch (RuntimeException localRuntimeException9) {
/* 424 */         throw new BerException();
/*     */       }
/*     */ 
/*     */     case 70:
/* 433 */       if (this.version == 0)
/* 434 */         throw new BerException(1);
/*     */       try
/*     */       {
/* 437 */         localObject = new SnmpCounter64(paramBerDecoder.fetchIntegerAsLong(i));
/*     */       } catch (RuntimeException localRuntimeException10) {
/* 439 */         throw new BerException();
/*     */       }
/*     */ 
/*     */     case 128:
/* 445 */       if (this.version == 0) {
/* 446 */         throw new BerException(1);
/*     */       }
/* 448 */       paramBerDecoder.fetchNull(i);
/* 449 */       localObject = SnmpVarBind.noSuchObject;
/* 450 */       break;
/*     */     case 129:
/* 453 */       if (this.version == 0) {
/* 454 */         throw new BerException(1);
/*     */       }
/* 456 */       paramBerDecoder.fetchNull(i);
/* 457 */       localObject = SnmpVarBind.noSuchInstance;
/* 458 */       break;
/*     */     case 130:
/* 461 */       if (this.version == 0) {
/* 462 */         throw new BerException(1);
/*     */       }
/* 464 */       paramBerDecoder.fetchNull(i);
/* 465 */       localObject = SnmpVarBind.endOfMibView;
/* 466 */       break;
/*     */     default:
/* 469 */       throw new BerException();
/*     */     }
/*     */ 
/* 473 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpMsg
 * JD-Core Version:    0.6.2
 */