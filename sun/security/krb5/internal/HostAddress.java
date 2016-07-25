/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.math.BigInteger;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class HostAddress
/*     */   implements Cloneable
/*     */ {
/*     */   int addrType;
/*  62 */   byte[] address = null;
/*     */   private static InetAddress localInetAddress;
/*  65 */   private static final boolean DEBUG = Krb5.DEBUG;
/*  66 */   private volatile int hashCode = 0;
/*     */ 
/*     */   private HostAddress(int paramInt) {
/*     */   }
/*     */   public Object clone() {
/*  71 */     HostAddress localHostAddress = new HostAddress(0);
/*  72 */     localHostAddress.addrType = this.addrType;
/*  73 */     if (this.address != null) {
/*  74 */       localHostAddress.address = ((byte[])this.address.clone());
/*     */     }
/*  76 */     return localHostAddress;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  81 */     if (this.hashCode == 0) {
/*  82 */       int i = 17;
/*  83 */       i = 37 * i + this.addrType;
/*  84 */       if (this.address != null) {
/*  85 */         for (int j = 0; j < this.address.length; j++) {
/*  86 */           i = 37 * i + this.address[j];
/*     */         }
/*     */       }
/*  89 */       this.hashCode = i;
/*     */     }
/*  91 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  96 */     if (this == paramObject) {
/*  97 */       return true;
/*     */     }
/*     */ 
/* 100 */     if (!(paramObject instanceof HostAddress)) {
/* 101 */       return false;
/*     */     }
/*     */ 
/* 104 */     HostAddress localHostAddress = (HostAddress)paramObject;
/* 105 */     if ((this.addrType != localHostAddress.addrType) || ((this.address != null) && (localHostAddress.address == null)) || ((this.address == null) && (localHostAddress.address != null)))
/*     */     {
/* 108 */       return false;
/* 109 */     }if ((this.address != null) && (localHostAddress.address != null)) {
/* 110 */       if (this.address.length != localHostAddress.address.length)
/* 111 */         return false;
/* 112 */       for (int i = 0; i < this.address.length; i++)
/* 113 */         if (this.address[i] != localHostAddress.address[i])
/* 114 */           return false;
/*     */     }
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */   private static synchronized InetAddress getLocalInetAddress()
/*     */     throws UnknownHostException
/*     */   {
/* 122 */     if (localInetAddress == null) {
/* 123 */       localInetAddress = InetAddress.getLocalHost();
/*     */     }
/* 125 */     if (localInetAddress == null) {
/* 126 */       throw new UnknownHostException();
/*     */     }
/* 128 */     return localInetAddress;
/*     */   }
/*     */ 
/*     */   public InetAddress getInetAddress()
/*     */     throws UnknownHostException
/*     */   {
/* 139 */     if ((this.addrType == 2) || (this.addrType == 24))
/*     */     {
/* 141 */       return InetAddress.getByAddress(this.address);
/*     */     }
/*     */ 
/* 144 */     return null;
/*     */   }
/*     */ 
/*     */   private int getAddrType(InetAddress paramInetAddress)
/*     */   {
/* 149 */     int i = 0;
/* 150 */     if ((paramInetAddress instanceof Inet4Address))
/* 151 */       i = 2;
/* 152 */     else if ((paramInetAddress instanceof Inet6Address))
/* 153 */       i = 24;
/* 154 */     return i;
/*     */   }
/*     */ 
/*     */   public HostAddress() throws UnknownHostException
/*     */   {
/* 159 */     InetAddress localInetAddress1 = getLocalInetAddress();
/* 160 */     this.addrType = getAddrType(localInetAddress1);
/* 161 */     this.address = localInetAddress1.getAddress();
/*     */   }
/*     */ 
/*     */   public HostAddress(int paramInt, byte[] paramArrayOfByte)
/*     */     throws KrbApErrException, UnknownHostException
/*     */   {
/* 176 */     switch (paramInt) {
/*     */     case 2:
/* 178 */       if (paramArrayOfByte.length != 4)
/* 179 */         throw new KrbApErrException(0, "Invalid Internet address");
/*     */       break;
/*     */     case 5:
/* 182 */       if (paramArrayOfByte.length != 2)
/* 183 */         throw new KrbApErrException(0, "Invalid CHAOSnet address");
/*     */       break;
/*     */     case 7:
/* 186 */       break;
/*     */     case 6:
/* 188 */       if (paramArrayOfByte.length != 6)
/* 189 */         throw new KrbApErrException(0, "Invalid XNS address");
/*     */       break;
/*     */     case 16:
/* 192 */       if (paramArrayOfByte.length != 3)
/* 193 */         throw new KrbApErrException(0, "Invalid DDP address");
/*     */       break;
/*     */     case 12:
/* 196 */       if (paramArrayOfByte.length != 2)
/* 197 */         throw new KrbApErrException(0, "Invalid DECnet Phase IV address");
/*     */       break;
/*     */     case 24:
/* 200 */       if (paramArrayOfByte.length != 16)
/* 201 */         throw new KrbApErrException(0, "Invalid Internet IPv6 address"); break;
/*     */     case 3:
/*     */     case 4:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     case 17:
/*     */     case 18:
/*     */     case 19:
/*     */     case 20:
/*     */     case 21:
/*     */     case 22:
/* 205 */     case 23: } this.addrType = paramInt;
/* 206 */     if (paramArrayOfByte != null) {
/* 207 */       this.address = ((byte[])paramArrayOfByte.clone());
/*     */     }
/* 209 */     if ((DEBUG) && (
/* 210 */       (this.addrType == 2) || (this.addrType == 24)))
/*     */     {
/* 212 */       System.out.println("Host address is " + InetAddress.getByAddress(this.address));
/*     */     }
/*     */   }
/*     */ 
/*     */   public HostAddress(InetAddress paramInetAddress)
/*     */   {
/* 219 */     this.addrType = getAddrType(paramInetAddress);
/* 220 */     this.address = paramInetAddress.getAddress();
/*     */   }
/*     */ 
/*     */   public HostAddress(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 231 */     DerValue localDerValue = paramDerValue.getData().getDerValue();
/* 232 */     if ((localDerValue.getTag() & 0x1F) == 0) {
/* 233 */       this.addrType = localDerValue.getData().getBigInteger().intValue();
/*     */     }
/*     */     else
/* 236 */       throw new Asn1Exception(906);
/* 237 */     localDerValue = paramDerValue.getData().getDerValue();
/* 238 */     if ((localDerValue.getTag() & 0x1F) == 1) {
/* 239 */       this.address = localDerValue.getData().getOctetString();
/*     */     }
/*     */     else
/* 242 */       throw new Asn1Exception(906);
/* 243 */     if (paramDerValue.getData().available() > 0)
/* 244 */       throw new Asn1Exception(906);
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 256 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 257 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 258 */     localDerOutputStream2.putInteger(this.addrType);
/* 259 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/* 260 */     localDerOutputStream2 = new DerOutputStream();
/* 261 */     localDerOutputStream2.putOctetString(this.address);
/* 262 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/* 263 */     localDerOutputStream2 = new DerOutputStream();
/* 264 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 265 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public static HostAddress parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 284 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte))
/*     */     {
/* 286 */       return null;
/*     */     }
/* 288 */     DerValue localDerValue1 = paramDerInputStream.getDerValue();
/* 289 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/* 290 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/* 293 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 294 */     return new HostAddress(localDerValue2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.HostAddress
 * JD-Core Version:    0.6.2
 */