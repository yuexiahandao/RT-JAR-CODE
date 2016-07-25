/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Arrays;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.util.BitArray;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class IPAddressName
/*     */   implements GeneralNameInterface
/*     */ {
/*     */   private byte[] address;
/*     */   private boolean isIPv4;
/*     */   private String name;
/*     */   private static final int MASKSIZE = 16;
/*     */ 
/*     */   public IPAddressName(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  82 */     this(paramDerValue.getOctetString());
/*     */   }
/*     */ 
/*     */   public IPAddressName(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  97 */     if ((paramArrayOfByte.length == 4) || (paramArrayOfByte.length == 8))
/*  98 */       this.isIPv4 = true;
/*  99 */     else if ((paramArrayOfByte.length == 16) || (paramArrayOfByte.length == 32))
/* 100 */       this.isIPv4 = false;
/*     */     else {
/* 102 */       throw new IOException("Invalid IPAddressName");
/*     */     }
/* 104 */     this.address = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public IPAddressName(String paramString)
/*     */     throws IOException
/*     */   {
/* 128 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 129 */       throw new IOException("IPAddress cannot be null or empty");
/*     */     }
/* 131 */     if (paramString.charAt(paramString.length() - 1) == '/') {
/* 132 */       throw new IOException("Invalid IPAddress: " + paramString);
/*     */     }
/*     */ 
/* 135 */     if (paramString.indexOf(':') >= 0)
/*     */     {
/* 139 */       parseIPv6(paramString);
/* 140 */       this.isIPv4 = false;
/* 141 */     } else if (paramString.indexOf('.') >= 0)
/*     */     {
/* 143 */       parseIPv4(paramString);
/* 144 */       this.isIPv4 = true;
/*     */     } else {
/* 146 */       throw new IOException("Invalid IPAddress: " + paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseIPv4(String paramString)
/*     */     throws IOException
/*     */   {
/* 159 */     int i = paramString.indexOf('/');
/* 160 */     if (i == -1) {
/* 161 */       this.address = InetAddress.getByName(paramString).getAddress();
/*     */     } else {
/* 163 */       this.address = new byte[8];
/*     */ 
/* 166 */       byte[] arrayOfByte1 = InetAddress.getByName(paramString.substring(i + 1)).getAddress();
/*     */ 
/* 170 */       byte[] arrayOfByte2 = InetAddress.getByName(paramString.substring(0, i)).getAddress();
/*     */ 
/* 173 */       System.arraycopy(arrayOfByte2, 0, this.address, 0, 4);
/* 174 */       System.arraycopy(arrayOfByte1, 0, this.address, 4, 4);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseIPv6(String paramString)
/*     */     throws IOException
/*     */   {
/* 189 */     int i = paramString.indexOf('/');
/* 190 */     if (i == -1) {
/* 191 */       this.address = InetAddress.getByName(paramString).getAddress();
/*     */     } else {
/* 193 */       this.address = new byte[32];
/* 194 */       byte[] arrayOfByte1 = InetAddress.getByName(paramString.substring(0, i)).getAddress();
/*     */ 
/* 196 */       System.arraycopy(arrayOfByte1, 0, this.address, 0, 16);
/*     */ 
/* 199 */       int j = Integer.parseInt(paramString.substring(i + 1));
/* 200 */       if (j > 128) {
/* 201 */         throw new IOException("IPv6Address prefix is longer than 128");
/*     */       }
/*     */ 
/* 204 */       BitArray localBitArray = new BitArray(128);
/*     */ 
/* 207 */       for (int k = 0; k < j; k++)
/* 208 */         localBitArray.set(k, true);
/* 209 */       byte[] arrayOfByte2 = localBitArray.toByteArray();
/*     */ 
/* 212 */       for (int m = 0; m < 16; m++)
/* 213 */         this.address[(16 + m)] = arrayOfByte2[m];
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 221 */     return 7;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 231 */     paramDerOutputStream.putOctetString(this.address);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*     */     try
/*     */     {
/* 239 */       return "IPAddress: " + getName();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 242 */       HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/* 243 */       return "IPAddress: " + localHexDumpEncoder.encodeBuffer(this.address);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */     throws IOException
/*     */   {
/* 255 */     if (this.name != null)
/* 256 */       return this.name;
/*     */     byte[] arrayOfByte1;
/*     */     byte[] arrayOfByte2;
/* 258 */     if (this.isIPv4)
/*     */     {
/* 260 */       arrayOfByte1 = new byte[4];
/* 261 */       System.arraycopy(this.address, 0, arrayOfByte1, 0, 4);
/* 262 */       this.name = InetAddress.getByAddress(arrayOfByte1).getHostAddress();
/* 263 */       if (this.address.length == 8) {
/* 264 */         arrayOfByte2 = new byte[4];
/* 265 */         System.arraycopy(this.address, 4, arrayOfByte2, 0, 4);
/* 266 */         this.name = (this.name + "/" + InetAddress.getByAddress(arrayOfByte2).getHostAddress());
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 271 */       arrayOfByte1 = new byte[16];
/* 272 */       System.arraycopy(this.address, 0, arrayOfByte1, 0, 16);
/* 273 */       this.name = InetAddress.getByAddress(arrayOfByte1).getHostAddress();
/* 274 */       if (this.address.length == 32)
/*     */       {
/* 278 */         arrayOfByte2 = new byte[16];
/* 279 */         for (int i = 16; i < 32; i++)
/* 280 */           arrayOfByte2[(i - 16)] = this.address[i];
/* 281 */         BitArray localBitArray = new BitArray(128, arrayOfByte2);
/*     */ 
/* 283 */         int j = 0;
/* 284 */         while ((j < 128) && 
/* 285 */           (localBitArray.get(j))) {
/* 284 */           j++;
/*     */         }
/*     */ 
/* 288 */         this.name = (this.name + "/" + j);
/*     */ 
/* 290 */         for (; j < 128; j++) {
/* 291 */           if (localBitArray.get(j)) {
/* 292 */             throw new IOException("Invalid IPv6 subdomain - set bit " + j + " not contiguous");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 298 */     return this.name;
/*     */   }
/*     */ 
/*     */   public byte[] getBytes()
/*     */   {
/* 305 */     return (byte[])this.address.clone();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 314 */     if (this == paramObject) {
/* 315 */       return true;
/*     */     }
/* 317 */     if (!(paramObject instanceof IPAddressName)) {
/* 318 */       return false;
/*     */     }
/* 320 */     byte[] arrayOfByte1 = ((IPAddressName)paramObject).getBytes();
/*     */ 
/* 322 */     if (arrayOfByte1.length != this.address.length) {
/* 323 */       return false;
/*     */     }
/* 325 */     if ((this.address.length == 8) || (this.address.length == 32))
/*     */     {
/* 328 */       int i = this.address.length / 2;
/* 329 */       byte[] arrayOfByte2 = new byte[i];
/* 330 */       byte[] arrayOfByte3 = new byte[i];
/* 331 */       for (int j = 0; j < i; j++) {
/* 332 */         arrayOfByte2[j] = ((byte)(this.address[j] & this.address[(j + i)]));
/* 333 */         arrayOfByte3[j] = ((byte)(arrayOfByte1[j] & arrayOfByte1[(j + i)]));
/* 334 */         if (arrayOfByte2[j] != arrayOfByte3[j]) {
/* 335 */           return false;
/*     */         }
/*     */       }
/*     */ 
/* 339 */       for (j = i; j < this.address.length; j++)
/* 340 */         if (this.address[j] != arrayOfByte1[j])
/* 341 */           return false;
/* 342 */       return true;
/*     */     }
/*     */ 
/* 346 */     return Arrays.equals(arrayOfByte1, this.address);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 356 */     int i = 0;
/*     */ 
/* 358 */     for (int j = 0; j < this.address.length; j++) {
/* 359 */       i += this.address[j] * j;
/*     */     }
/* 361 */     return i;
/*     */   }
/*     */ 
/*     */   public int constrains(GeneralNameInterface paramGeneralNameInterface)
/*     */     throws UnsupportedOperationException
/*     */   {
/*     */     int i;
/* 396 */     if (paramGeneralNameInterface == null) {
/* 397 */       i = -1;
/* 398 */     } else if (paramGeneralNameInterface.getType() != 7) {
/* 399 */       i = -1;
/* 400 */     } else if (((IPAddressName)paramGeneralNameInterface).equals(this)) {
/* 401 */       i = 0;
/*     */     } else {
/* 403 */       byte[] arrayOfByte = ((IPAddressName)paramGeneralNameInterface).getBytes();
/* 404 */       if ((arrayOfByte.length == 4) && (this.address.length == 4))
/*     */       {
/* 406 */         i = 3;
/*     */       }
/*     */       else
/*     */       {
/*     */         int j;
/*     */         int k;
/* 407 */         if (((arrayOfByte.length == 8) && (this.address.length == 8)) || ((arrayOfByte.length == 32) && (this.address.length == 32)))
/*     */         {
/* 411 */           j = 1;
/* 412 */           k = 1;
/* 413 */           int m = 0;
/* 414 */           int n = 0;
/* 415 */           int i1 = this.address.length / 2;
/* 416 */           for (int i2 = 0; i2 < i1; i2++) {
/* 417 */             if ((byte)(this.address[i2] & this.address[(i2 + i1)]) != this.address[i2])
/* 418 */               m = 1;
/* 419 */             if ((byte)(arrayOfByte[i2] & arrayOfByte[(i2 + i1)]) != arrayOfByte[i2])
/* 420 */               n = 1;
/* 421 */             if (((byte)(this.address[(i2 + i1)] & arrayOfByte[(i2 + i1)]) != this.address[(i2 + i1)]) || ((byte)(this.address[i2] & this.address[(i2 + i1)]) != (byte)(arrayOfByte[i2] & this.address[(i2 + i1)])))
/*     */             {
/* 423 */               j = 0;
/*     */             }
/* 425 */             if (((byte)(arrayOfByte[(i2 + i1)] & this.address[(i2 + i1)]) != arrayOfByte[(i2 + i1)]) || ((byte)(arrayOfByte[i2] & arrayOfByte[(i2 + i1)]) != (byte)(this.address[i2] & arrayOfByte[(i2 + i1)])))
/*     */             {
/* 427 */               k = 0;
/*     */             }
/*     */           }
/* 430 */           if ((m != 0) || (n != 0)) {
/* 431 */             if ((m != 0) && (n != 0))
/* 432 */               i = 0;
/* 433 */             else if (m != 0)
/* 434 */               i = 2;
/*     */             else
/* 436 */               i = 1;
/* 437 */           } else if (j != 0)
/* 438 */             i = 1;
/* 439 */           else if (k != 0)
/* 440 */             i = 2;
/*     */           else
/* 442 */             i = 3;
/* 443 */         } else if ((arrayOfByte.length == 8) || (arrayOfByte.length == 32))
/*     */         {
/* 445 */           j = 0;
/* 446 */           k = arrayOfByte.length / 2;
/* 447 */           for (; j < k; j++)
/*     */           {
/* 450 */             if ((this.address[j] & arrayOfByte[(j + k)]) != arrayOfByte[j])
/*     */               break;
/*     */           }
/* 453 */           if (j == k)
/* 454 */             i = 2;
/*     */           else
/* 456 */             i = 3;
/* 457 */         } else if ((this.address.length == 8) || (this.address.length == 32))
/*     */         {
/* 459 */           j = 0;
/* 460 */           k = this.address.length / 2;
/* 461 */           for (; j < k; j++)
/*     */           {
/* 463 */             if ((arrayOfByte[j] & this.address[(j + k)]) != this.address[j])
/*     */               break;
/*     */           }
/* 466 */           if (j == k)
/* 467 */             i = 1;
/*     */           else
/* 469 */             i = 3;
/*     */         } else {
/* 471 */           i = 3;
/*     */         }
/*     */       }
/*     */     }
/* 474 */     return i;
/*     */   }
/*     */ 
/*     */   public int subtreeDepth()
/*     */     throws UnsupportedOperationException
/*     */   {
/* 486 */     throw new UnsupportedOperationException("subtreeDepth() not defined for IPAddressName");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.IPAddressName
 * JD-Core Version:    0.6.2
 */