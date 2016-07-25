/*     */ package com.sun.jndi.dns;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import javax.naming.InvalidNameException;
/*     */ 
/*     */ public class ResourceRecord
/*     */ {
/*     */   static final int TYPE_A = 1;
/*     */   static final int TYPE_NS = 2;
/*     */   static final int TYPE_CNAME = 5;
/*     */   static final int TYPE_SOA = 6;
/*     */   static final int TYPE_PTR = 12;
/*     */   static final int TYPE_HINFO = 13;
/*     */   static final int TYPE_MX = 15;
/*     */   static final int TYPE_TXT = 16;
/*     */   static final int TYPE_AAAA = 28;
/*     */   static final int TYPE_SRV = 33;
/*     */   static final int TYPE_NAPTR = 35;
/*     */   static final int QTYPE_AXFR = 252;
/*     */   static final int QTYPE_STAR = 255;
/*  62 */   static final String[] rrTypeNames = { null, "A", "NS", null, null, "CNAME", "SOA", null, null, null, null, null, "PTR", "HINFO", null, "MX", "TXT", null, null, null, null, null, null, null, null, null, null, null, "AAAA", null, null, null, null, "SRV", null, "NAPTR" };
/*     */   static final int CLASS_INTERNET = 1;
/*     */   static final int CLASS_HESIOD = 2;
/*     */   static final int QCLASS_STAR = 255;
/*  83 */   static final String[] rrClassNames = { null, "IN", null, null, "HS" };
/*     */   byte[] msg;
/*     */   int msgLen;
/*     */   boolean qSection;
/*     */   int offset;
/*     */   int rrlen;
/*     */   DnsName name;
/*     */   int rrtype;
/*     */   String rrtypeName;
/*     */   int rrclass;
/*     */   String rrclassName;
/*  99 */   int ttl = 0;
/* 100 */   int rdlen = 0;
/* 101 */   Object rdata = null;
/*     */ 
/*     */   ResourceRecord(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws InvalidNameException
/*     */   {
/* 120 */     this.msg = paramArrayOfByte;
/* 121 */     this.msgLen = paramInt1;
/* 122 */     this.offset = paramInt2;
/* 123 */     this.qSection = paramBoolean1;
/* 124 */     decode(paramBoolean2);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 128 */     String str = this.name + " " + this.rrclassName + " " + this.rrtypeName;
/* 129 */     if (!this.qSection) {
/* 130 */       str = str + " " + this.ttl + " " + (this.rdata != null ? this.rdata : "[n/a]");
/*     */     }
/*     */ 
/* 133 */     return str;
/*     */   }
/*     */ 
/*     */   public DnsName getName()
/*     */   {
/* 140 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 147 */     return this.rrlen;
/*     */   }
/*     */ 
/*     */   public int getType() {
/* 151 */     return this.rrtype;
/*     */   }
/*     */ 
/*     */   public int getRrclass() {
/* 155 */     return this.rrclass;
/*     */   }
/*     */ 
/*     */   public Object getRdata() {
/* 159 */     return this.rdata;
/*     */   }
/*     */ 
/*     */   public static String getTypeName(int paramInt)
/*     */   {
/* 164 */     return valueToName(paramInt, rrTypeNames);
/*     */   }
/*     */ 
/*     */   public static int getType(String paramString) {
/* 168 */     return nameToValue(paramString, rrTypeNames);
/*     */   }
/*     */ 
/*     */   public static String getRrclassName(int paramInt) {
/* 172 */     return valueToName(paramInt, rrClassNames);
/*     */   }
/*     */ 
/*     */   public static int getRrclass(String paramString) {
/* 176 */     return nameToValue(paramString, rrClassNames);
/*     */   }
/*     */ 
/*     */   private static String valueToName(int paramInt, String[] paramArrayOfString) {
/* 180 */     String str = null;
/* 181 */     if ((paramInt > 0) && (paramInt < paramArrayOfString.length))
/* 182 */       str = paramArrayOfString[paramInt];
/* 183 */     else if (paramInt == 255) {
/* 184 */       str = "*";
/*     */     }
/* 186 */     if (str == null) {
/* 187 */       str = Integer.toString(paramInt);
/*     */     }
/* 189 */     return str;
/*     */   }
/*     */ 
/*     */   private static int nameToValue(String paramString, String[] paramArrayOfString) {
/* 193 */     if (paramString.equals(""))
/* 194 */       return -1;
/* 195 */     if (paramString.equals("*")) {
/* 196 */       return 255;
/*     */     }
/* 198 */     if (Character.isDigit(paramString.charAt(0)))
/*     */       try {
/* 200 */         return Integer.parseInt(paramString);
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException) {
/*     */       }
/* 204 */     for (int i = 1; i < paramArrayOfString.length; i++) {
/* 205 */       if ((paramArrayOfString[i] != null) && (paramString.equalsIgnoreCase(paramArrayOfString[i])))
/*     */       {
/* 207 */         return i;
/*     */       }
/*     */     }
/* 210 */     return -1;
/*     */   }
/*     */ 
/*     */   public static int compareSerialNumbers(long paramLong1, long paramLong2)
/*     */   {
/* 222 */     long l = paramLong2 - paramLong1;
/* 223 */     if (l == 0L)
/* 224 */       return 0;
/* 225 */     if (((l > 0L) && (l <= 2147483647L)) || ((l < 0L) && (-l > 2147483647L)))
/*     */     {
/* 227 */       return -1;
/*     */     }
/* 229 */     return 1;
/*     */   }
/*     */ 
/*     */   private void decode(boolean paramBoolean)
/*     */     throws InvalidNameException
/*     */   {
/* 239 */     int i = this.offset;
/*     */ 
/* 241 */     this.name = new DnsName();
/* 242 */     i = decodeName(i, this.name);
/*     */ 
/* 244 */     this.rrtype = getUShort(i);
/* 245 */     this.rrtypeName = (this.rrtype < rrTypeNames.length ? rrTypeNames[this.rrtype] : null);
/*     */ 
/* 248 */     if (this.rrtypeName == null) {
/* 249 */       this.rrtypeName = Integer.toString(this.rrtype);
/*     */     }
/* 251 */     i += 2;
/*     */ 
/* 253 */     this.rrclass = getUShort(i);
/* 254 */     this.rrclassName = (this.rrclass < rrClassNames.length ? rrClassNames[this.rrclass] : null);
/*     */ 
/* 257 */     if (this.rrclassName == null) {
/* 258 */       this.rrclassName = Integer.toString(this.rrclass);
/*     */     }
/* 260 */     i += 2;
/*     */ 
/* 262 */     if (!this.qSection) {
/* 263 */       this.ttl = getInt(i);
/* 264 */       i += 4;
/*     */ 
/* 266 */       this.rdlen = getUShort(i);
/* 267 */       i += 2;
/*     */ 
/* 269 */       this.rdata = ((paramBoolean) || (this.rrtype == 6) ? decodeRdata(i) : null);
/*     */ 
/* 273 */       if ((this.rdata instanceof DnsName)) {
/* 274 */         this.rdata = this.rdata.toString();
/*     */       }
/* 276 */       i += this.rdlen;
/*     */     }
/*     */ 
/* 279 */     this.rrlen = (i - this.offset);
/*     */ 
/* 281 */     this.msg = null;
/*     */   }
/*     */ 
/*     */   private int getUByte(int paramInt)
/*     */   {
/* 288 */     return this.msg[paramInt] & 0xFF;
/*     */   }
/*     */ 
/*     */   private int getUShort(int paramInt)
/*     */   {
/* 296 */     return (this.msg[paramInt] & 0xFF) << 8 | this.msg[(paramInt + 1)] & 0xFF;
/*     */   }
/*     */ 
/*     */   private int getInt(int paramInt)
/*     */   {
/* 305 */     return getUShort(paramInt) << 16 | getUShort(paramInt + 2);
/*     */   }
/*     */ 
/*     */   private long getUInt(int paramInt)
/*     */   {
/* 313 */     return getInt(paramInt) & 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */   private DnsName decodeName(int paramInt)
/*     */     throws InvalidNameException
/*     */   {
/* 320 */     DnsName localDnsName = new DnsName();
/* 321 */     decodeName(paramInt, localDnsName);
/* 322 */     return localDnsName;
/*     */   }
/*     */ 
/*     */   private int decodeName(int paramInt, DnsName paramDnsName)
/*     */     throws InvalidNameException
/*     */   {
/* 330 */     if (this.msg[paramInt] == 0) {
/* 331 */       paramDnsName.add(0, "");
/* 332 */       return paramInt + 1;
/* 333 */     }if ((this.msg[paramInt] & 0xC0) != 0) {
/* 334 */       decodeName(getUShort(paramInt) & 0x3FFF, paramDnsName);
/* 335 */       return paramInt + 2;
/*     */     }
/* 337 */     int i = this.msg[(paramInt++)];
/*     */     try {
/* 339 */       paramDnsName.add(0, new String(this.msg, paramInt, i, "ISO-8859-1"));
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */     }
/* 343 */     return decodeName(paramInt + i, paramDnsName);
/*     */   }
/*     */ 
/*     */   private Object decodeRdata(int paramInt)
/*     */     throws InvalidNameException
/*     */   {
/* 356 */     if (this.rrclass == 1)
/* 357 */       switch (this.rrtype) {
/*     */       case 1:
/* 359 */         return decodeA(paramInt);
/*     */       case 28:
/* 361 */         return decodeAAAA(paramInt);
/*     */       case 2:
/*     */       case 5:
/*     */       case 12:
/* 365 */         return decodeName(paramInt);
/*     */       case 15:
/* 367 */         return decodeMx(paramInt);
/*     */       case 6:
/* 369 */         return decodeSoa(paramInt);
/*     */       case 33:
/* 371 */         return decodeSrv(paramInt);
/*     */       case 35:
/* 373 */         return decodeNaptr(paramInt);
/*     */       case 16:
/* 375 */         return decodeTxt(paramInt);
/*     */       case 13:
/* 377 */         return decodeHinfo(paramInt);
/*     */       case 3:
/*     */       case 4:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 14:
/*     */       case 17:
/*     */       case 18:
/*     */       case 19:
/*     */       case 20:
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/*     */       case 26:
/*     */       case 27:
/*     */       case 29:
/*     */       case 30:
/*     */       case 31:
/*     */       case 32:
/* 381 */       case 34: }  byte[] arrayOfByte = new byte[this.rdlen];
/* 382 */     System.arraycopy(this.msg, paramInt, arrayOfByte, 0, this.rdlen);
/* 383 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private String decodeMx(int paramInt)
/*     */     throws InvalidNameException
/*     */   {
/* 390 */     int i = getUShort(paramInt);
/* 391 */     paramInt += 2;
/* 392 */     DnsName localDnsName = decodeName(paramInt);
/* 393 */     return i + " " + localDnsName;
/*     */   }
/*     */ 
/*     */   private String decodeSoa(int paramInt)
/*     */     throws InvalidNameException
/*     */   {
/* 400 */     DnsName localDnsName1 = new DnsName();
/* 401 */     paramInt = decodeName(paramInt, localDnsName1);
/* 402 */     DnsName localDnsName2 = new DnsName();
/* 403 */     paramInt = decodeName(paramInt, localDnsName2);
/*     */ 
/* 405 */     long l1 = getUInt(paramInt);
/* 406 */     paramInt += 4;
/* 407 */     long l2 = getUInt(paramInt);
/* 408 */     paramInt += 4;
/* 409 */     long l3 = getUInt(paramInt);
/* 410 */     paramInt += 4;
/* 411 */     long l4 = getUInt(paramInt);
/* 412 */     paramInt += 4;
/* 413 */     long l5 = getUInt(paramInt);
/* 414 */     paramInt += 4;
/*     */ 
/* 416 */     return localDnsName1 + " " + localDnsName2 + " " + l1 + " " + l2 + " " + l3 + " " + l4 + " " + l5;
/*     */   }
/*     */ 
/*     */   private String decodeSrv(int paramInt)
/*     */     throws InvalidNameException
/*     */   {
/* 425 */     int i = getUShort(paramInt);
/* 426 */     paramInt += 2;
/* 427 */     int j = getUShort(paramInt);
/* 428 */     paramInt += 2;
/* 429 */     int k = getUShort(paramInt);
/* 430 */     paramInt += 2;
/* 431 */     DnsName localDnsName = decodeName(paramInt);
/* 432 */     return i + " " + j + " " + k + " " + localDnsName;
/*     */   }
/*     */ 
/*     */   private String decodeNaptr(int paramInt)
/*     */     throws InvalidNameException
/*     */   {
/* 440 */     int i = getUShort(paramInt);
/* 441 */     paramInt += 2;
/* 442 */     int j = getUShort(paramInt);
/* 443 */     paramInt += 2;
/* 444 */     StringBuffer localStringBuffer1 = new StringBuffer();
/* 445 */     paramInt += decodeCharString(paramInt, localStringBuffer1);
/* 446 */     StringBuffer localStringBuffer2 = new StringBuffer();
/* 447 */     paramInt += decodeCharString(paramInt, localStringBuffer2);
/* 448 */     StringBuffer localStringBuffer3 = new StringBuffer(this.rdlen);
/* 449 */     paramInt += decodeCharString(paramInt, localStringBuffer3);
/* 450 */     DnsName localDnsName = decodeName(paramInt);
/*     */ 
/* 452 */     return i + " " + j + " " + localStringBuffer1 + " " + localStringBuffer2 + " " + localStringBuffer3 + " " + localDnsName;
/*     */   }
/*     */ 
/*     */   private String decodeTxt(int paramInt)
/*     */   {
/* 461 */     StringBuffer localStringBuffer = new StringBuffer(this.rdlen);
/* 462 */     int i = paramInt + this.rdlen;
/* 463 */     while (paramInt < i) {
/* 464 */       paramInt += decodeCharString(paramInt, localStringBuffer);
/* 465 */       if (paramInt < i) {
/* 466 */         localStringBuffer.append(' ');
/*     */       }
/*     */     }
/* 469 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private String decodeHinfo(int paramInt)
/*     */   {
/* 477 */     StringBuffer localStringBuffer = new StringBuffer(this.rdlen);
/* 478 */     paramInt += decodeCharString(paramInt, localStringBuffer);
/* 479 */     localStringBuffer.append(' ');
/* 480 */     paramInt += decodeCharString(paramInt, localStringBuffer);
/* 481 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private int decodeCharString(int paramInt, StringBuffer paramStringBuffer)
/*     */   {
/* 493 */     int i = paramStringBuffer.length();
/* 494 */     int j = getUByte(paramInt++);
/* 495 */     int k = j == 0 ? 1 : 0;
/* 496 */     for (int m = 0; m < j; m++) {
/* 497 */       int n = getUByte(paramInt++);
/* 498 */       k |= (n == 32 ? 1 : 0);
/* 499 */       if ((n == 92) || (n == 34)) {
/* 500 */         k = 1;
/* 501 */         paramStringBuffer.append('\\');
/*     */       }
/* 503 */       paramStringBuffer.append((char)n);
/*     */     }
/* 505 */     if (k != 0) {
/* 506 */       paramStringBuffer.insert(i, '"');
/* 507 */       paramStringBuffer.append('"');
/*     */     }
/* 509 */     return j + 1;
/*     */   }
/*     */ 
/*     */   private String decodeA(int paramInt)
/*     */   {
/* 517 */     return (this.msg[paramInt] & 0xFF) + "." + (this.msg[(paramInt + 1)] & 0xFF) + "." + (this.msg[(paramInt + 2)] & 0xFF) + "." + (this.msg[(paramInt + 3)] & 0xFF);
/*     */   }
/*     */ 
/*     */   private String decodeAAAA(int paramInt)
/*     */   {
/* 529 */     int[] arrayOfInt = new int[8];
/* 530 */     for (int i = 0; i < 8; i++) {
/* 531 */       arrayOfInt[i] = getUShort(paramInt);
/* 532 */       paramInt += 2;
/*     */     }
/*     */ 
/* 536 */     i = -1;
/* 537 */     int j = 0;
/* 538 */     int k = -1;
/* 539 */     int m = 0;
/* 540 */     for (int n = 0; n < 8; n++) {
/* 541 */       if (arrayOfInt[n] == 0) {
/* 542 */         if (i == -1) {
/* 543 */           i = n;
/* 544 */           j = 1;
/*     */         } else {
/* 546 */           j++;
/* 547 */           if ((j >= 2) && (j > m)) {
/* 548 */             k = i;
/* 549 */             m = j;
/*     */           }
/*     */         }
/*     */       }
/* 553 */       else i = -1;
/*     */ 
/*     */     }
/*     */ 
/* 560 */     if (k == 0) {
/* 561 */       if ((m == 6) || ((m == 7) && (arrayOfInt[7] > 1)))
/*     */       {
/* 563 */         return "::" + decodeA(paramInt - 4);
/* 564 */       }if ((m == 5) && (arrayOfInt[5] == 65535)) {
/* 565 */         return "::ffff:" + decodeA(paramInt - 4);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 570 */     n = k != -1 ? 1 : 0;
/*     */ 
/* 572 */     StringBuffer localStringBuffer = new StringBuffer(40);
/* 573 */     if (k == 0) {
/* 574 */       localStringBuffer.append(':');
/*     */     }
/* 576 */     for (int i1 = 0; i1 < 8; i1++) {
/* 577 */       if ((n == 0) || (i1 < k) || (i1 >= k + m)) {
/* 578 */         localStringBuffer.append(Integer.toHexString(arrayOfInt[i1]));
/* 579 */         if (i1 < 7)
/* 580 */           localStringBuffer.append(':');
/*     */       }
/* 582 */       else if ((n != 0) && (i1 == k)) {
/* 583 */         localStringBuffer.append(':');
/*     */       }
/*     */     }
/*     */ 
/* 587 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.ResourceRecord
 * JD-Core Version:    0.6.2
 */