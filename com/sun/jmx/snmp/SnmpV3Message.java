/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class SnmpV3Message extends SnmpMsg
/*     */ {
/* 101 */   public int msgId = 0;
/*     */ 
/* 106 */   public int msgMaxSize = 0;
/*     */ 
/* 120 */   public byte msgFlags = 0;
/*     */ 
/* 124 */   public int msgSecurityModel = 0;
/*     */ 
/* 128 */   public byte[] msgSecurityParameters = null;
/*     */ 
/* 132 */   public byte[] contextEngineId = null;
/*     */ 
/* 136 */   public byte[] contextName = null;
/*     */ 
/* 139 */   public byte[] encryptedPdu = null;
/*     */ 
/*     */   public int encodeMessage(byte[] paramArrayOfByte)
/*     */     throws SnmpTooBigException
/*     */   {
/* 158 */     int i = 0;
/* 159 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER)) {
/* 160 */       JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpV3Message.class.getName(), "encodeMessage", "Can't encode directly V3Message! Need a SecuritySubSystem");
/*     */     }
/*     */ 
/* 164 */     throw new IllegalArgumentException("Can't encode");
/*     */   }
/*     */ 
/*     */   public void decodeMessage(byte[] paramArrayOfByte, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/*     */     try
/*     */     {
/* 179 */       BerDecoder localBerDecoder = new BerDecoder(paramArrayOfByte);
/* 180 */       localBerDecoder.openSequence();
/* 181 */       this.version = localBerDecoder.fetchInteger();
/* 182 */       localBerDecoder.openSequence();
/* 183 */       this.msgId = localBerDecoder.fetchInteger();
/* 184 */       this.msgMaxSize = localBerDecoder.fetchInteger();
/* 185 */       this.msgFlags = localBerDecoder.fetchOctetString()[0];
/* 186 */       this.msgSecurityModel = localBerDecoder.fetchInteger();
/* 187 */       localBerDecoder.closeSequence();
/* 188 */       this.msgSecurityParameters = localBerDecoder.fetchOctetString();
/* 189 */       if ((this.msgFlags & 0x2) == 0) {
/* 190 */         localBerDecoder.openSequence();
/* 191 */         this.contextEngineId = localBerDecoder.fetchOctetString();
/* 192 */         this.contextName = localBerDecoder.fetchOctetString();
/* 193 */         this.data = localBerDecoder.fetchAny();
/* 194 */         this.dataLength = this.data.length;
/* 195 */         localBerDecoder.closeSequence();
/*     */       }
/*     */       else {
/* 198 */         this.encryptedPdu = localBerDecoder.fetchOctetString();
/*     */       }
/* 200 */       localBerDecoder.closeSequence();
/*     */     }
/*     */     catch (BerException localBerException) {
/* 203 */       localBerException.printStackTrace();
/* 204 */       throw new SnmpStatusException("Invalid encoding");
/*     */     }
/*     */ 
/* 207 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER)) {
/* 208 */       StringBuilder localStringBuilder = new StringBuilder().append("Unmarshalled message : \n").append("version : ").append(this.version).append("\n").append("msgId : ").append(this.msgId).append("\n").append("msgMaxSize : ").append(this.msgMaxSize).append("\n").append("msgFlags : ").append(this.msgFlags).append("\n").append("msgSecurityModel : ").append(this.msgSecurityModel).append("\n").append("contextEngineId : ").append(this.contextEngineId == null ? null : SnmpEngineId.createEngineId(this.contextEngineId)).append("\n").append("contextName : ").append(this.contextName).append("\n").append("data : ").append(this.data).append("\n").append("dat len : ").append(this.data == null ? 0 : this.data.length).append("\n").append("encryptedPdu : ").append(this.encryptedPdu).append("\n");
/*     */ 
/* 231 */       JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpV3Message.class.getName(), "decodeMessage", localStringBuilder.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getRequestId(byte[] paramArrayOfByte)
/*     */     throws SnmpStatusException
/*     */   {
/* 242 */     BerDecoder localBerDecoder = null;
/* 243 */     int i = 0;
/*     */     try {
/* 245 */       localBerDecoder = new BerDecoder(paramArrayOfByte);
/* 246 */       localBerDecoder.openSequence();
/* 247 */       localBerDecoder.fetchInteger();
/* 248 */       localBerDecoder.openSequence();
/* 249 */       i = localBerDecoder.fetchInteger();
/*     */     } catch (BerException localBerException1) {
/* 251 */       throw new SnmpStatusException("Invalid encoding");
/*     */     }
/*     */     try {
/* 254 */       localBerDecoder.closeSequence();
/*     */     }
/*     */     catch (BerException localBerException2)
/*     */     {
/*     */     }
/* 259 */     return i;
/*     */   }
/*     */ 
/*     */   public void encodeSnmpPdu(SnmpPdu paramSnmpPdu, int paramInt)
/*     */     throws SnmpStatusException, SnmpTooBigException
/*     */   {
/* 287 */     SnmpScopedPduPacket localSnmpScopedPduPacket = (SnmpScopedPduPacket)paramSnmpPdu;
/*     */     Object localObject;
/* 289 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER)) {
/* 290 */       localObject = new StringBuilder().append("PDU to marshall: \n").append("security parameters : ").append(localSnmpScopedPduPacket.securityParameters).append("\n").append("type : ").append(localSnmpScopedPduPacket.type).append("\n").append("version : ").append(localSnmpScopedPduPacket.version).append("\n").append("requestId : ").append(localSnmpScopedPduPacket.requestId).append("\n").append("msgId : ").append(localSnmpScopedPduPacket.msgId).append("\n").append("msgMaxSize : ").append(localSnmpScopedPduPacket.msgMaxSize).append("\n").append("msgFlags : ").append(localSnmpScopedPduPacket.msgFlags).append("\n").append("msgSecurityModel : ").append(localSnmpScopedPduPacket.msgSecurityModel).append("\n").append("contextEngineId : ").append(localSnmpScopedPduPacket.contextEngineId).append("\n").append("contextName : ").append(localSnmpScopedPduPacket.contextName).append("\n");
/*     */ 
/* 312 */       JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpV3Message.class.getName(), "encodeSnmpPdu", ((StringBuilder)localObject).toString());
/*     */     }
/*     */ 
/* 316 */     this.version = localSnmpScopedPduPacket.version;
/* 317 */     this.address = localSnmpScopedPduPacket.address;
/* 318 */     this.port = localSnmpScopedPduPacket.port;
/* 319 */     this.msgId = localSnmpScopedPduPacket.msgId;
/* 320 */     this.msgMaxSize = localSnmpScopedPduPacket.msgMaxSize;
/* 321 */     this.msgFlags = localSnmpScopedPduPacket.msgFlags;
/* 322 */     this.msgSecurityModel = localSnmpScopedPduPacket.msgSecurityModel;
/*     */ 
/* 324 */     this.contextEngineId = localSnmpScopedPduPacket.contextEngineId;
/* 325 */     this.contextName = localSnmpScopedPduPacket.contextName;
/*     */ 
/* 327 */     this.securityParameters = localSnmpScopedPduPacket.securityParameters;
/*     */ 
/* 332 */     this.data = new byte[paramInt];
/*     */     try
/*     */     {
/* 340 */       localObject = new BerEncoder(this.data);
/* 341 */       ((BerEncoder)localObject).openSequence();
/* 342 */       encodeVarBindList((BerEncoder)localObject, localSnmpScopedPduPacket.varBindList);
/*     */ 
/* 344 */       switch (localSnmpScopedPduPacket.type)
/*     */       {
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 166:
/*     */       case 167:
/*     */       case 168:
/* 353 */         SnmpPduRequestType localSnmpPduRequestType = (SnmpPduRequestType)localSnmpScopedPduPacket;
/* 354 */         ((BerEncoder)localObject).putInteger(localSnmpPduRequestType.getErrorIndex());
/* 355 */         ((BerEncoder)localObject).putInteger(localSnmpPduRequestType.getErrorStatus());
/* 356 */         ((BerEncoder)localObject).putInteger(localSnmpScopedPduPacket.requestId);
/* 357 */         break;
/*     */       case 165:
/* 360 */         SnmpPduBulkType localSnmpPduBulkType = (SnmpPduBulkType)localSnmpScopedPduPacket;
/* 361 */         ((BerEncoder)localObject).putInteger(localSnmpPduBulkType.getMaxRepetitions());
/* 362 */         ((BerEncoder)localObject).putInteger(localSnmpPduBulkType.getNonRepeaters());
/* 363 */         ((BerEncoder)localObject).putInteger(localSnmpScopedPduPacket.requestId);
/* 364 */         break;
/*     */       case 164:
/*     */       default:
/* 367 */         throw new SnmpStatusException("Invalid pdu type " + String.valueOf(localSnmpScopedPduPacket.type));
/*     */       }
/* 369 */       ((BerEncoder)localObject).closeSequence(localSnmpScopedPduPacket.type);
/* 370 */       this.dataLength = ((BerEncoder)localObject).trim();
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 373 */       throw new SnmpTooBigException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpPdu decodeSnmpPdu()
/*     */     throws SnmpStatusException
/*     */   {
/* 390 */     Object localObject = null;
/*     */ 
/* 392 */     BerDecoder localBerDecoder = new BerDecoder(this.data);
/*     */     try {
/* 394 */       int i = localBerDecoder.getTag();
/* 395 */       localBerDecoder.openSequence(i);
/* 396 */       switch (i)
/*     */       {
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 166:
/*     */       case 167:
/*     */       case 168:
/* 405 */         SnmpScopedPduRequest localSnmpScopedPduRequest = new SnmpScopedPduRequest();
/* 406 */         localSnmpScopedPduRequest.requestId = localBerDecoder.fetchInteger();
/* 407 */         localSnmpScopedPduRequest.setErrorStatus(localBerDecoder.fetchInteger());
/* 408 */         localSnmpScopedPduRequest.setErrorIndex(localBerDecoder.fetchInteger());
/* 409 */         localObject = localSnmpScopedPduRequest;
/* 410 */         break;
/*     */       case 165:
/* 413 */         SnmpScopedPduBulk localSnmpScopedPduBulk = new SnmpScopedPduBulk();
/* 414 */         localSnmpScopedPduBulk.requestId = localBerDecoder.fetchInteger();
/* 415 */         localSnmpScopedPduBulk.setNonRepeaters(localBerDecoder.fetchInteger());
/* 416 */         localSnmpScopedPduBulk.setMaxRepetitions(localBerDecoder.fetchInteger());
/* 417 */         localObject = localSnmpScopedPduBulk;
/* 418 */         break;
/*     */       case 164:
/*     */       default:
/* 420 */         throw new SnmpStatusException(9);
/*     */       }
/* 422 */       localObject.type = i;
/* 423 */       localObject.varBindList = decodeVarBindList(localBerDecoder);
/* 424 */       localBerDecoder.closeSequence();
/*     */     } catch (BerException localBerException) {
/* 426 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 427 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, SnmpV3Message.class.getName(), "decodeSnmpPdu", "BerException", localBerException);
/*     */       }
/*     */ 
/* 430 */       throw new SnmpStatusException(9);
/*     */     }
/*     */ 
/* 436 */     localObject.address = this.address;
/* 437 */     localObject.port = this.port;
/* 438 */     localObject.msgFlags = this.msgFlags;
/* 439 */     localObject.version = this.version;
/* 440 */     localObject.msgId = this.msgId;
/* 441 */     localObject.msgMaxSize = this.msgMaxSize;
/* 442 */     localObject.msgSecurityModel = this.msgSecurityModel;
/* 443 */     localObject.contextEngineId = this.contextEngineId;
/* 444 */     localObject.contextName = this.contextName;
/*     */ 
/* 446 */     localObject.securityParameters = this.securityParameters;
/*     */ 
/* 448 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER)) {
/* 449 */       StringBuilder localStringBuilder = new StringBuilder().append("Unmarshalled PDU : \n").append("type : ").append(localObject.type).append("\n").append("version : ").append(localObject.version).append("\n").append("requestId : ").append(localObject.requestId).append("\n").append("msgId : ").append(localObject.msgId).append("\n").append("msgMaxSize : ").append(localObject.msgMaxSize).append("\n").append("msgFlags : ").append(localObject.msgFlags).append("\n").append("msgSecurityModel : ").append(localObject.msgSecurityModel).append("\n").append("contextEngineId : ").append(localObject.contextEngineId).append("\n").append("contextName : ").append(localObject.contextName).append("\n");
/*     */ 
/* 469 */       JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpV3Message.class.getName(), "decodeSnmpPdu", localStringBuilder.toString());
/*     */     }
/*     */ 
/* 472 */     return localObject;
/*     */   }
/*     */ 
/*     */   public String printMessage()
/*     */   {
/* 481 */     StringBuffer localStringBuffer = new StringBuffer();
/* 482 */     localStringBuffer.append("msgId : " + this.msgId + "\n");
/* 483 */     localStringBuffer.append("msgMaxSize : " + this.msgMaxSize + "\n");
/* 484 */     localStringBuffer.append("msgFlags : " + this.msgFlags + "\n");
/* 485 */     localStringBuffer.append("msgSecurityModel : " + this.msgSecurityModel + "\n");
/*     */ 
/* 487 */     if (this.contextEngineId == null) {
/* 488 */       localStringBuffer.append("contextEngineId : null");
/*     */     }
/*     */     else {
/* 491 */       localStringBuffer.append("contextEngineId : {\n");
/* 492 */       localStringBuffer.append(dumpHexBuffer(this.contextEngineId, 0, this.contextEngineId.length));
/*     */ 
/* 495 */       localStringBuffer.append("\n}\n");
/*     */     }
/*     */ 
/* 498 */     if (this.contextName == null) {
/* 499 */       localStringBuffer.append("contextName : null");
/*     */     }
/*     */     else {
/* 502 */       localStringBuffer.append("contextName : {\n");
/* 503 */       localStringBuffer.append(dumpHexBuffer(this.contextName, 0, this.contextName.length));
/*     */ 
/* 506 */       localStringBuffer.append("\n}\n");
/*     */     }
/* 508 */     return super.printMessage();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpV3Message
 * JD-Core Version:    0.6.2
 */