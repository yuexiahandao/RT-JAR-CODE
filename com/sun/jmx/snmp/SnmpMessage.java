/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class SnmpMessage extends SnmpMsg
/*     */   implements SnmpDefinitions
/*     */ {
/*     */   public byte[] community;
/*     */ 
/*     */   public int encodeMessage(byte[] paramArrayOfByte)
/*     */     throws SnmpTooBigException
/*     */   {
/*  80 */     int i = 0;
/*  81 */     if (this.data == null) {
/*  82 */       throw new IllegalArgumentException("Data field is null");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  88 */       BerEncoder localBerEncoder = new BerEncoder(paramArrayOfByte);
/*  89 */       localBerEncoder.openSequence();
/*  90 */       localBerEncoder.putAny(this.data, this.dataLength);
/*  91 */       localBerEncoder.putOctetString(this.community != null ? this.community : new byte[0]);
/*  92 */       localBerEncoder.putInteger(this.version);
/*  93 */       localBerEncoder.closeSequence();
/*  94 */       i = localBerEncoder.trim();
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*  97 */       throw new SnmpTooBigException();
/*     */     }
/*     */ 
/* 100 */     return i;
/*     */   }
/*     */ 
/*     */   public int getRequestId(byte[] paramArrayOfByte)
/*     */     throws SnmpStatusException
/*     */   {
/* 110 */     int i = 0;
/* 111 */     BerDecoder localBerDecoder1 = null;
/* 112 */     BerDecoder localBerDecoder2 = null;
/* 113 */     byte[] arrayOfByte = null;
/*     */     try {
/* 115 */       localBerDecoder1 = new BerDecoder(paramArrayOfByte);
/* 116 */       localBerDecoder1.openSequence();
/* 117 */       localBerDecoder1.fetchInteger();
/* 118 */       localBerDecoder1.fetchOctetString();
/* 119 */       arrayOfByte = localBerDecoder1.fetchAny();
/* 120 */       localBerDecoder2 = new BerDecoder(arrayOfByte);
/* 121 */       int j = localBerDecoder2.getTag();
/* 122 */       localBerDecoder2.openSequence(j);
/* 123 */       i = localBerDecoder2.fetchInteger();
/*     */     }
/*     */     catch (BerException localBerException1) {
/* 126 */       throw new SnmpStatusException("Invalid encoding");
/*     */     }
/*     */     try {
/* 129 */       localBerDecoder1.closeSequence();
/*     */     }
/*     */     catch (BerException localBerException2) {
/*     */     }
/*     */     try {
/* 134 */       localBerDecoder2.closeSequence();
/*     */     }
/*     */     catch (BerException localBerException3) {
/*     */     }
/* 138 */     return i;
/*     */   }
/*     */ 
/*     */   public void decodeMessage(byte[] paramArrayOfByte, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/*     */     try
/*     */     {
/* 151 */       BerDecoder localBerDecoder = new BerDecoder(paramArrayOfByte);
/* 152 */       localBerDecoder.openSequence();
/* 153 */       this.version = localBerDecoder.fetchInteger();
/* 154 */       this.community = localBerDecoder.fetchOctetString();
/* 155 */       this.data = localBerDecoder.fetchAny();
/* 156 */       this.dataLength = this.data.length;
/* 157 */       localBerDecoder.closeSequence();
/*     */     }
/*     */     catch (BerException localBerException) {
/* 160 */       throw new SnmpStatusException("Invalid encoding");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void encodeSnmpPdu(SnmpPdu paramSnmpPdu, int paramInt)
/*     */     throws SnmpStatusException, SnmpTooBigException
/*     */   {
/* 190 */     SnmpPduPacket localSnmpPduPacket = (SnmpPduPacket)paramSnmpPdu;
/* 191 */     this.version = localSnmpPduPacket.version;
/* 192 */     this.community = localSnmpPduPacket.community;
/* 193 */     this.address = localSnmpPduPacket.address;
/* 194 */     this.port = localSnmpPduPacket.port;
/*     */ 
/* 199 */     this.data = new byte[paramInt];
/*     */     try
/*     */     {
/* 207 */       BerEncoder localBerEncoder = new BerEncoder(this.data);
/* 208 */       localBerEncoder.openSequence();
/* 209 */       encodeVarBindList(localBerEncoder, localSnmpPduPacket.varBindList);
/*     */ 
/* 211 */       switch (localSnmpPduPacket.type)
/*     */       {
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 166:
/*     */       case 167:
/*     */       case 168:
/* 220 */         SnmpPduRequest localSnmpPduRequest = (SnmpPduRequest)localSnmpPduPacket;
/* 221 */         localBerEncoder.putInteger(localSnmpPduRequest.errorIndex);
/* 222 */         localBerEncoder.putInteger(localSnmpPduRequest.errorStatus);
/* 223 */         localBerEncoder.putInteger(localSnmpPduRequest.requestId);
/* 224 */         break;
/*     */       case 165:
/* 227 */         SnmpPduBulk localSnmpPduBulk = (SnmpPduBulk)localSnmpPduPacket;
/* 228 */         localBerEncoder.putInteger(localSnmpPduBulk.maxRepetitions);
/* 229 */         localBerEncoder.putInteger(localSnmpPduBulk.nonRepeaters);
/* 230 */         localBerEncoder.putInteger(localSnmpPduBulk.requestId);
/* 231 */         break;
/*     */       case 164:
/* 234 */         SnmpPduTrap localSnmpPduTrap = (SnmpPduTrap)localSnmpPduPacket;
/* 235 */         localBerEncoder.putInteger(localSnmpPduTrap.timeStamp, 67);
/* 236 */         localBerEncoder.putInteger(localSnmpPduTrap.specificTrap);
/* 237 */         localBerEncoder.putInteger(localSnmpPduTrap.genericTrap);
/* 238 */         if (localSnmpPduTrap.agentAddr != null)
/* 239 */           localBerEncoder.putOctetString(localSnmpPduTrap.agentAddr.byteValue(), 64);
/*     */         else
/* 241 */           localBerEncoder.putOctetString(new byte[0], 64);
/* 242 */         localBerEncoder.putOid(localSnmpPduTrap.enterprise.longValue());
/* 243 */         break;
/*     */       default:
/* 246 */         throw new SnmpStatusException("Invalid pdu type " + String.valueOf(localSnmpPduPacket.type));
/*     */       }
/* 248 */       localBerEncoder.closeSequence(localSnmpPduPacket.type);
/* 249 */       this.dataLength = localBerEncoder.trim();
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 252 */       throw new SnmpTooBigException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpPdu decodeSnmpPdu()
/*     */     throws SnmpStatusException
/*     */   {
/* 270 */     Object localObject = null;
/* 271 */     BerDecoder localBerDecoder = new BerDecoder(this.data);
/*     */     try {
/* 273 */       int i = localBerDecoder.getTag();
/* 274 */       localBerDecoder.openSequence(i);
/* 275 */       switch (i)
/*     */       {
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 166:
/*     */       case 167:
/*     */       case 168:
/* 284 */         SnmpPduRequest localSnmpPduRequest = new SnmpPduRequest();
/* 285 */         localSnmpPduRequest.requestId = localBerDecoder.fetchInteger();
/* 286 */         localSnmpPduRequest.errorStatus = localBerDecoder.fetchInteger();
/* 287 */         localSnmpPduRequest.errorIndex = localBerDecoder.fetchInteger();
/* 288 */         localObject = localSnmpPduRequest;
/* 289 */         break;
/*     */       case 165:
/* 292 */         SnmpPduBulk localSnmpPduBulk = new SnmpPduBulk();
/* 293 */         localSnmpPduBulk.requestId = localBerDecoder.fetchInteger();
/* 294 */         localSnmpPduBulk.nonRepeaters = localBerDecoder.fetchInteger();
/* 295 */         localSnmpPduBulk.maxRepetitions = localBerDecoder.fetchInteger();
/* 296 */         localObject = localSnmpPduBulk;
/* 297 */         break;
/*     */       case 164:
/* 300 */         SnmpPduTrap localSnmpPduTrap = new SnmpPduTrap();
/* 301 */         localSnmpPduTrap.enterprise = new SnmpOid(localBerDecoder.fetchOid());
/* 302 */         byte[] arrayOfByte = localBerDecoder.fetchOctetString(64);
/* 303 */         if (arrayOfByte.length != 0)
/* 304 */           localSnmpPduTrap.agentAddr = new SnmpIpAddress(arrayOfByte);
/*     */         else
/* 306 */           localSnmpPduTrap.agentAddr = null;
/* 307 */         localSnmpPduTrap.genericTrap = localBerDecoder.fetchInteger();
/* 308 */         localSnmpPduTrap.specificTrap = localBerDecoder.fetchInteger();
/* 309 */         localSnmpPduTrap.timeStamp = localBerDecoder.fetchInteger(67);
/* 310 */         localObject = localSnmpPduTrap;
/* 311 */         break;
/*     */       default:
/* 314 */         throw new SnmpStatusException(9);
/*     */       }
/* 316 */       localObject.type = i;
/* 317 */       localObject.varBindList = decodeVarBindList(localBerDecoder);
/* 318 */       localBerDecoder.closeSequence();
/*     */     } catch (BerException localBerException) {
/* 320 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 321 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, SnmpMessage.class.getName(), "decodeSnmpPdu", "BerException", localBerException);
/*     */       }
/*     */ 
/* 324 */       throw new SnmpStatusException(9);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {
/* 327 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 328 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, SnmpMessage.class.getName(), "decodeSnmpPdu", "IllegalArgumentException", localIllegalArgumentException);
/*     */       }
/*     */ 
/* 331 */       throw new SnmpStatusException(9);
/*     */     }
/*     */ 
/* 337 */     localObject.version = this.version;
/* 338 */     localObject.community = this.community;
/* 339 */     localObject.address = this.address;
/* 340 */     localObject.port = this.port;
/*     */ 
/* 342 */     return localObject;
/*     */   }
/*     */ 
/*     */   public String printMessage()
/*     */   {
/* 350 */     StringBuffer localStringBuffer = new StringBuffer();
/* 351 */     if (this.community == null) {
/* 352 */       localStringBuffer.append("Community: null");
/*     */     }
/*     */     else {
/* 355 */       localStringBuffer.append("Community: {\n");
/* 356 */       localStringBuffer.append(dumpHexBuffer(this.community, 0, this.community.length));
/* 357 */       localStringBuffer.append("\n}\n");
/*     */     }
/* 359 */     return super.printMessage();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpMessage
 * JD-Core Version:    0.6.2
 */