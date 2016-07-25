/*     */ package com.sun.jmx.snmp.IPAcl;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.Principal;
/*     */ import java.security.acl.Group;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class NetMaskImpl extends PrincipalImpl
/*     */   implements Group, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7332541893877932896L;
/*  51 */   protected byte[] subnet = null;
/*  52 */   protected int prefix = -1;
/*     */ 
/*     */   public NetMaskImpl()
/*     */     throws UnknownHostException
/*     */   {
/*     */   }
/*     */ 
/*     */   private byte[] extractSubNet(byte[] paramArrayOfByte)
/*     */   {
/*  61 */     int i = paramArrayOfByte.length;
/*  62 */     byte[] arrayOfByte = null;
/*  63 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/*  64 */       JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "extractSubNet", "BINARY ARRAY :");
/*     */ 
/*  66 */       StringBuffer localStringBuffer = new StringBuffer();
/*  67 */       for (k = 0; k < i; k++) {
/*  68 */         localStringBuffer.append((paramArrayOfByte[k] & 0xFF) + ":");
/*     */       }
/*  70 */       JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "extractSubNet", localStringBuffer.toString());
/*     */     }
/*     */ 
/*  75 */     int j = this.prefix / 8;
/*  76 */     if (j == i) {
/*  77 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/*  78 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "extractSubNet", "The mask is the complete address, strange..." + i);
/*     */       }
/*     */ 
/*  81 */       arrayOfByte = paramArrayOfByte;
/*  82 */       return arrayOfByte;
/*     */     }
/*  84 */     if (j > i) {
/*  85 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/*  86 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "extractSubNet", "The number of covered byte is longer than the address. BUG");
/*     */       }
/*     */ 
/*  89 */       throw new IllegalArgumentException("The number of covered byte is longer than the address.");
/*     */     }
/*  91 */     int k = j;
/*  92 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/*  93 */       JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "extractSubNet", "Partially covered index : " + k);
/*     */     }
/*     */ 
/*  96 */     int m = paramArrayOfByte[k];
/*  97 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/*  98 */       JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "extractSubNet", "Partially covered byte : " + m);
/*     */     }
/*     */ 
/* 103 */     int n = this.prefix % 8;
/* 104 */     int i1 = 0;
/*     */ 
/* 106 */     if (n == 0)
/* 107 */       i1 = k;
/*     */     else {
/* 109 */       i1 = k + 1;
/*     */     }
/* 111 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 112 */       JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "extractSubNet", "Remains : " + n);
/*     */     }
/*     */ 
/* 116 */     int i2 = 0;
/* 117 */     for (int i3 = 0; i3 < n; i3++) {
/* 118 */       i2 = (byte)(i2 | 1 << 7 - i3);
/*     */     }
/* 120 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 121 */       JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "extractSubNet", "Mask value : " + (i2 & 0xFF));
/*     */     }
/*     */ 
/* 125 */     i3 = (byte)(m & i2);
/*     */ 
/* 127 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 128 */       JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "extractSubNet", "Masked byte : " + (i3 & 0xFF));
/*     */     }
/*     */ 
/* 131 */     arrayOfByte = new byte[i1];
/* 132 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 133 */       JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "extractSubNet", "Resulting subnet : ");
/*     */     }
/*     */ 
/* 136 */     for (int i4 = 0; i4 < k; i4++) {
/* 137 */       arrayOfByte[i4] = paramArrayOfByte[i4];
/*     */ 
/* 139 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 140 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "extractSubNet", (arrayOfByte[i4] & 0xFF) + ":");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 145 */     if (n != 0) {
/* 146 */       arrayOfByte[k] = i3;
/* 147 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 148 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "extractSubNet", "Last subnet byte : " + (arrayOfByte[k] & 0xFF));
/*     */       }
/*     */     }
/*     */ 
/* 152 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public NetMaskImpl(String paramString, int paramInt)
/*     */     throws UnknownHostException
/*     */   {
/* 162 */     super(paramString);
/* 163 */     this.prefix = paramInt;
/* 164 */     this.subnet = extractSubNet(getAddress().getAddress());
/*     */   }
/*     */ 
/*     */   public boolean addMember(Principal paramPrincipal)
/*     */   {
/* 176 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 180 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 192 */     if (((paramObject instanceof PrincipalImpl)) || ((paramObject instanceof NetMaskImpl))) {
/* 193 */       PrincipalImpl localPrincipalImpl = (PrincipalImpl)paramObject;
/* 194 */       InetAddress localInetAddress = localPrincipalImpl.getAddress();
/* 195 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 196 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "equals", "Received Address : " + localInetAddress);
/*     */       }
/*     */ 
/* 199 */       byte[] arrayOfByte = localInetAddress.getAddress();
/* 200 */       for (int i = 0; i < this.subnet.length; i++) {
/* 201 */         if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 202 */           JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "equals", "(recAddr[i]) : " + (arrayOfByte[i] & 0xFF));
/*     */ 
/* 204 */           JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "equals", "(recAddr[i] & subnet[i]) : " + (arrayOfByte[i] & this.subnet[i] & 0xFF) + " subnet[i] : " + (this.subnet[i] & 0xFF));
/*     */         }
/*     */ 
/* 209 */         if ((arrayOfByte[i] & this.subnet[i]) != this.subnet[i]) {
/* 210 */           if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 211 */             JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "equals", "FALSE");
/*     */           }
/*     */ 
/* 214 */           return false;
/*     */         }
/*     */       }
/* 217 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 218 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, NetMaskImpl.class.getName(), "equals", "TRUE");
/*     */       }
/*     */ 
/* 221 */       return true;
/*     */     }
/* 223 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isMember(Principal paramPrincipal)
/*     */   {
/* 232 */     if ((paramPrincipal.hashCode() & super.hashCode()) == paramPrincipal.hashCode()) return true;
/* 233 */     return false;
/*     */   }
/*     */ 
/*     */   public Enumeration<? extends Principal> members()
/*     */   {
/* 242 */     Vector localVector = new Vector(1);
/* 243 */     localVector.addElement(this);
/* 244 */     return localVector.elements();
/*     */   }
/*     */ 
/*     */   public boolean removeMember(Principal paramPrincipal)
/*     */   {
/* 254 */     return true;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 263 */     return "NetMaskImpl :" + super.getAddress().toString() + "/" + this.prefix;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.NetMaskImpl
 * JD-Core Version:    0.6.2
 */