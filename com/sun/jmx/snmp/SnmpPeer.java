/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ 
/*     */ public class SnmpPeer
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5554565062847175999L;
/*     */   public static final int defaultSnmpRequestPktSize = 2048;
/*     */   public static final int defaultSnmpResponsePktSize = 8192;
/*  86 */   private int maxVarBindLimit = 25;
/*     */ 
/*  92 */   private int portNum = 161;
/*     */ 
/*  98 */   private int maxTries = 3;
/*     */ 
/* 104 */   private int timeout = 3000;
/*     */ 
/* 109 */   private SnmpPduFactory pduFactory = new SnmpPduFactoryBER();
/*     */   private long _maxrtt;
/*     */   private long _minrtt;
/*     */   private long _avgrtt;
/* 128 */   private SnmpParams _snmpParameter = new SnmpParameters();
/*     */ 
/* 133 */   private InetAddress _devAddr = null;
/*     */ 
/* 141 */   private int maxSnmpPacketSize = 2048;
/*     */ 
/* 146 */   InetAddress[] _devAddrList = null;
/*     */ 
/* 151 */   int _addrIndex = 0;
/*     */ 
/* 154 */   private boolean customPduFactory = false;
/*     */ 
/*     */   public SnmpPeer(String paramString)
/*     */     throws UnknownHostException
/*     */   {
/* 165 */     this(paramString, 161);
/*     */   }
/*     */ 
/*     */   public SnmpPeer(InetAddress paramInetAddress, int paramInt)
/*     */   {
/* 174 */     this._devAddr = paramInetAddress;
/* 175 */     this.portNum = paramInt;
/*     */   }
/*     */ 
/*     */   public SnmpPeer(InetAddress paramInetAddress)
/*     */   {
/* 183 */     this._devAddr = paramInetAddress;
/*     */   }
/*     */ 
/*     */   public SnmpPeer(String paramString, int paramInt)
/*     */     throws UnknownHostException
/*     */   {
/* 193 */     useIPAddress(paramString);
/* 194 */     this.portNum = paramInt;
/*     */   }
/*     */ 
/*     */   public final synchronized void useIPAddress(String paramString)
/*     */     throws UnknownHostException
/*     */   {
/* 210 */     this._devAddr = InetAddress.getByName(paramString);
/*     */   }
/*     */ 
/*     */   public final synchronized String ipAddressInUse()
/*     */   {
/* 220 */     byte[] arrayOfByte = this._devAddr.getAddress();
/* 221 */     return (arrayOfByte[0] & 0xFF) + "." + (arrayOfByte[1] & 0xFF) + "." + (arrayOfByte[2] & 0xFF) + "." + (arrayOfByte[3] & 0xFF);
/*     */   }
/*     */ 
/*     */   public final synchronized void useAddressList(InetAddress[] paramArrayOfInetAddress)
/*     */   {
/* 232 */     this._devAddrList = paramArrayOfInetAddress;
/* 233 */     this._addrIndex = 0;
/* 234 */     useNextAddress();
/*     */   }
/*     */ 
/*     */   public final synchronized void useNextAddress()
/*     */   {
/* 243 */     if (this._devAddrList == null) {
/* 244 */       return;
/*     */     }
/*     */ 
/* 247 */     if (this._addrIndex > this._devAddrList.length - 1)
/*     */     {
/* 249 */       this._addrIndex = 0;
/* 250 */     }this._devAddr = this._devAddrList[(this._addrIndex++)];
/*     */   }
/*     */ 
/*     */   public boolean allowSnmpSets()
/*     */   {
/* 260 */     return this._snmpParameter.allowSnmpSets();
/*     */   }
/*     */ 
/*     */   public final InetAddress[] getDestAddrList()
/*     */   {
/* 268 */     return this._devAddrList;
/*     */   }
/*     */ 
/*     */   public final InetAddress getDestAddr()
/*     */   {
/* 276 */     return this._devAddr;
/*     */   }
/*     */ 
/*     */   public final int getDestPort()
/*     */   {
/* 284 */     return this.portNum;
/*     */   }
/*     */ 
/*     */   public final synchronized void setDestPort(int paramInt)
/*     */   {
/* 292 */     this.portNum = paramInt;
/*     */   }
/*     */ 
/*     */   public final int getTimeout()
/*     */   {
/* 300 */     return this.timeout;
/*     */   }
/*     */ 
/*     */   public final synchronized void setTimeout(int paramInt)
/*     */   {
/* 308 */     if (paramInt < 0)
/* 309 */       throw new IllegalArgumentException();
/* 310 */     this.timeout = paramInt;
/*     */   }
/*     */ 
/*     */   public final int getMaxTries()
/*     */   {
/* 318 */     return this.maxTries;
/*     */   }
/*     */ 
/*     */   public final synchronized void setMaxTries(int paramInt)
/*     */   {
/* 326 */     if (paramInt < 0)
/* 327 */       throw new IllegalArgumentException();
/* 328 */     this.maxTries = paramInt;
/*     */   }
/*     */ 
/*     */   public final String getDevName()
/*     */   {
/* 336 */     return getDestAddr().getHostName();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 347 */     return "Peer/Port : " + getDestAddr().getHostAddress() + "/" + getDestPort();
/*     */   }
/*     */ 
/*     */   public final synchronized int getVarBindLimit()
/*     */   {
/* 355 */     return this.maxVarBindLimit;
/*     */   }
/*     */ 
/*     */   public final synchronized void setVarBindLimit(int paramInt)
/*     */   {
/* 363 */     this.maxVarBindLimit = paramInt;
/*     */   }
/*     */ 
/*     */   public void setParams(SnmpParams paramSnmpParams)
/*     */   {
/* 371 */     this._snmpParameter = paramSnmpParams;
/*     */   }
/*     */ 
/*     */   public SnmpParams getParams()
/*     */   {
/* 378 */     return this._snmpParameter;
/*     */   }
/*     */ 
/*     */   public final int getMaxSnmpPktSize()
/*     */   {
/* 386 */     return this.maxSnmpPacketSize;
/*     */   }
/*     */ 
/*     */   public final synchronized void setMaxSnmpPktSize(int paramInt)
/*     */   {
/* 394 */     this.maxSnmpPacketSize = paramInt;
/*     */   }
/*     */ 
/*     */   boolean isCustomPduFactory() {
/* 398 */     return this.customPduFactory;
/*     */   }
/*     */ 
/*     */   public void finalize()
/*     */   {
/* 409 */     this._devAddr = null;
/* 410 */     this._devAddrList = null;
/* 411 */     this._snmpParameter = null;
/*     */   }
/*     */ 
/*     */   public long getMinRtt()
/*     */   {
/* 419 */     return this._minrtt;
/*     */   }
/*     */ 
/*     */   public long getMaxRtt()
/*     */   {
/* 427 */     return this._maxrtt;
/*     */   }
/*     */ 
/*     */   public long getAvgRtt()
/*     */   {
/* 435 */     return this._avgrtt;
/*     */   }
/*     */ 
/*     */   private void updateRttStats(long paramLong)
/*     */   {
/* 443 */     if (this._minrtt > paramLong)
/* 444 */       this._minrtt = paramLong;
/* 445 */     else if (this._maxrtt < paramLong)
/* 446 */       this._maxrtt = paramLong;
/*     */     else
/* 448 */       this._avgrtt = paramLong;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpPeer
 * JD-Core Version:    0.6.2
 */