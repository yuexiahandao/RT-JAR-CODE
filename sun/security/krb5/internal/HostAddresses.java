/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Vector;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.internal.ccache.CCacheOutputStream;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class HostAddresses
/*     */   implements Cloneable
/*     */ {
/*  69 */   private static boolean DEBUG = Krb5.DEBUG;
/*  70 */   private HostAddress[] addresses = null;
/*  71 */   private volatile int hashCode = 0;
/*     */ 
/*     */   public HostAddresses(HostAddress[] paramArrayOfHostAddress) throws IOException {
/*  74 */     if (paramArrayOfHostAddress != null) {
/*  75 */       this.addresses = new HostAddress[paramArrayOfHostAddress.length];
/*  76 */       for (int i = 0; i < paramArrayOfHostAddress.length; i++) {
/*  77 */         if (paramArrayOfHostAddress[i] == null) {
/*  78 */           throw new IOException("Cannot create a HostAddress");
/*     */         }
/*  80 */         this.addresses[i] = ((HostAddress)paramArrayOfHostAddress[i].clone());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public HostAddresses() throws UnknownHostException
/*     */   {
/*  87 */     this.addresses = new HostAddress[1];
/*  88 */     this.addresses[0] = new HostAddress();
/*     */   }
/*     */ 
/*     */   private HostAddresses(int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   public HostAddresses(PrincipalName paramPrincipalName) throws UnknownHostException, KrbException {
/*  96 */     String[] arrayOfString = paramPrincipalName.getNameStrings();
/*     */ 
/*  98 */     if ((paramPrincipalName.getNameType() != 3) || (arrayOfString.length < 2))
/*     */     {
/* 100 */       throw new KrbException(60, "Bad name");
/*     */     }
/* 102 */     String str = arrayOfString[1];
/* 103 */     InetAddress[] arrayOfInetAddress = InetAddress.getAllByName(str);
/* 104 */     HostAddress[] arrayOfHostAddress = new HostAddress[arrayOfInetAddress.length];
/*     */ 
/* 106 */     for (int i = 0; i < arrayOfInetAddress.length; i++) {
/* 107 */       arrayOfHostAddress[i] = new HostAddress(arrayOfInetAddress[i]);
/*     */     }
/*     */ 
/* 110 */     this.addresses = arrayOfHostAddress;
/*     */   }
/*     */ 
/*     */   public Object clone() {
/* 114 */     HostAddresses localHostAddresses = new HostAddresses(0);
/* 115 */     if (this.addresses != null) {
/* 116 */       localHostAddresses.addresses = new HostAddress[this.addresses.length];
/* 117 */       for (int i = 0; i < this.addresses.length; i++) {
/* 118 */         localHostAddresses.addresses[i] = ((HostAddress)this.addresses[i].clone());
/*     */       }
/*     */     }
/*     */ 
/* 122 */     return localHostAddresses;
/*     */   }
/*     */ 
/*     */   public boolean inList(HostAddress paramHostAddress) {
/* 126 */     if (this.addresses != null) {
/* 127 */       for (int i = 0; i < this.addresses.length; i++)
/* 128 */         if (this.addresses[i].equals(paramHostAddress))
/* 129 */           return true;
/*     */     }
/* 131 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 135 */     if (this.hashCode == 0) {
/* 136 */       int i = 17;
/* 137 */       if (this.addresses != null) {
/* 138 */         for (int j = 0; j < this.addresses.length; j++) {
/* 139 */           i = 37 * i + this.addresses[j].hashCode();
/*     */         }
/*     */       }
/* 142 */       this.hashCode = i;
/*     */     }
/* 144 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 150 */     if (this == paramObject) {
/* 151 */       return true;
/*     */     }
/*     */ 
/* 154 */     if (!(paramObject instanceof HostAddresses)) {
/* 155 */       return false;
/*     */     }
/*     */ 
/* 158 */     HostAddresses localHostAddresses = (HostAddresses)paramObject;
/* 159 */     if (((this.addresses == null) && (localHostAddresses.addresses != null)) || ((this.addresses != null) && (localHostAddresses.addresses == null)))
/*     */     {
/* 161 */       return false;
/* 162 */     }if ((this.addresses != null) && (localHostAddresses.addresses != null)) {
/* 163 */       if (this.addresses.length != localHostAddresses.addresses.length)
/* 164 */         return false;
/* 165 */       for (int i = 0; i < this.addresses.length; i++)
/* 166 */         if (!this.addresses[i].equals(localHostAddresses.addresses[i]))
/* 167 */           return false;
/*     */     }
/* 169 */     return true;
/*     */   }
/*     */ 
/*     */   public HostAddresses(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 182 */     Vector localVector = new Vector();
/* 183 */     DerValue localDerValue = null;
/* 184 */     while (paramDerValue.getData().available() > 0) {
/* 185 */       localDerValue = paramDerValue.getData().getDerValue();
/* 186 */       localVector.addElement(new HostAddress(localDerValue));
/*     */     }
/* 188 */     if (localVector.size() > 0) {
/* 189 */       this.addresses = new HostAddress[localVector.size()];
/* 190 */       localVector.copyInto(this.addresses);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 204 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 205 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */ 
/* 207 */     if ((this.addresses != null) && (this.addresses.length > 0)) {
/* 208 */       for (int i = 0; i < this.addresses.length; i++)
/* 209 */         localDerOutputStream1.write(this.addresses[i].asn1Encode());
/*     */     }
/* 211 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 212 */     return localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public static HostAddresses parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 231 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte))
/*     */     {
/* 233 */       return null;
/* 234 */     }DerValue localDerValue1 = paramDerInputStream.getDerValue();
/* 235 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/* 236 */       throw new Asn1Exception(906);
/*     */     }
/* 238 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 239 */     return new HostAddresses(localDerValue2);
/*     */   }
/*     */ 
/*     */   public void writeAddrs(CCacheOutputStream paramCCacheOutputStream)
/*     */     throws IOException
/*     */   {
/* 253 */     paramCCacheOutputStream.write32(this.addresses.length);
/* 254 */     for (int i = 0; i < this.addresses.length; i++) {
/* 255 */       paramCCacheOutputStream.write16(this.addresses[i].addrType);
/* 256 */       paramCCacheOutputStream.write32(this.addresses[i].address.length);
/* 257 */       paramCCacheOutputStream.write(this.addresses[i].address, 0, this.addresses[i].address.length);
/*     */     }
/*     */   }
/*     */ 
/*     */   public InetAddress[] getInetAddresses()
/*     */   {
/* 265 */     if ((this.addresses == null) || (this.addresses.length == 0)) {
/* 266 */       return null;
/*     */     }
/* 268 */     ArrayList localArrayList = new ArrayList(this.addresses.length);
/*     */ 
/* 270 */     for (int i = 0; i < this.addresses.length; i++) {
/*     */       try {
/* 272 */         if ((this.addresses[i].addrType == 2) || (this.addresses[i].addrType == 24))
/*     */         {
/* 274 */           localArrayList.add(this.addresses[i].getInetAddress());
/*     */         }
/*     */       }
/*     */       catch (UnknownHostException localUnknownHostException) {
/* 278 */         return null;
/*     */       }
/*     */     }
/*     */ 
/* 282 */     InetAddress[] arrayOfInetAddress = new InetAddress[localArrayList.size()];
/* 283 */     return (InetAddress[])localArrayList.toArray(arrayOfInetAddress);
/*     */   }
/*     */ 
/*     */   public static HostAddresses getLocalAddresses()
/*     */     throws IOException
/*     */   {
/* 292 */     String str = null;
/* 293 */     InetAddress[] arrayOfInetAddress = null;
/*     */     try {
/* 295 */       InetAddress localInetAddress = InetAddress.getLocalHost();
/* 296 */       str = localInetAddress.getHostName();
/* 297 */       arrayOfInetAddress = InetAddress.getAllByName(str);
/* 298 */       HostAddress[] arrayOfHostAddress = new HostAddress[arrayOfInetAddress.length];
/* 299 */       for (int i = 0; i < arrayOfInetAddress.length; i++)
/*     */       {
/* 301 */         arrayOfHostAddress[i] = new HostAddress(arrayOfInetAddress[i]);
/*     */       }
/* 303 */       if (DEBUG) {
/* 304 */         System.out.println(">>> KrbKdcReq local addresses for " + str + " are: ");
/*     */ 
/* 307 */         for (i = 0; i < arrayOfInetAddress.length; i++) {
/* 308 */           System.out.println("\n\t" + arrayOfInetAddress[i]);
/* 309 */           if ((arrayOfInetAddress[i] instanceof Inet4Address))
/* 310 */             System.out.println("IPv4 address");
/* 311 */           if ((arrayOfInetAddress[i] instanceof Inet6Address))
/* 312 */             System.out.println("IPv6 address");
/*     */         }
/*     */       }
/* 315 */       return new HostAddresses(arrayOfHostAddress);
/*     */     } catch (Exception localException) {
/* 317 */       throw new IOException(localException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public HostAddresses(InetAddress[] paramArrayOfInetAddress)
/*     */   {
/* 328 */     if (paramArrayOfInetAddress == null)
/*     */     {
/* 330 */       this.addresses = null;
/* 331 */       return;
/*     */     }
/*     */ 
/* 334 */     this.addresses = new HostAddress[paramArrayOfInetAddress.length];
/* 335 */     for (int i = 0; i < paramArrayOfInetAddress.length; i++)
/* 336 */       this.addresses[i] = new HostAddress(paramArrayOfInetAddress[i]);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.HostAddresses
 * JD-Core Version:    0.6.2
 */