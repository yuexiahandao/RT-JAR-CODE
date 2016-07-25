/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ 
/*     */ public class SnmpString extends SnmpValue
/*     */ {
/*     */   private static final long serialVersionUID = -7011986973225194188L;
/*     */   static final String name = "String";
/* 277 */   protected byte[] value = null;
/*     */ 
/*     */   public SnmpString()
/*     */   {
/*  48 */     this.value = new byte[0];
/*     */   }
/*     */ 
/*     */   public SnmpString(byte[] paramArrayOfByte)
/*     */   {
/*  56 */     this.value = ((byte[])paramArrayOfByte.clone());
/*     */   }
/*     */ 
/*     */   public SnmpString(Byte[] paramArrayOfByte)
/*     */   {
/*  64 */     this.value = new byte[paramArrayOfByte.length];
/*  65 */     for (int i = 0; i < paramArrayOfByte.length; i++)
/*  66 */       this.value[i] = paramArrayOfByte[i].byteValue();
/*     */   }
/*     */ 
/*     */   public SnmpString(String paramString)
/*     */   {
/*  75 */     this.value = paramString.getBytes();
/*     */   }
/*     */ 
/*     */   public SnmpString(InetAddress paramInetAddress)
/*     */   {
/*  85 */     this.value = paramInetAddress.getAddress();
/*     */   }
/*     */ 
/*     */   public InetAddress inetAddressValue()
/*     */     throws UnknownHostException
/*     */   {
/*  99 */     return InetAddress.getByAddress(this.value);
/*     */   }
/*     */ 
/*     */   public static String BinToChar(String paramString)
/*     */   {
/* 108 */     char[] arrayOfChar = new char[paramString.length() / 8];
/* 109 */     int i = arrayOfChar.length;
/* 110 */     for (int j = 0; j < i; j++)
/* 111 */       arrayOfChar[j] = ((char)Integer.parseInt(paramString.substring(8 * j, 8 * j + 8), 2));
/* 112 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   public static String HexToChar(String paramString)
/*     */   {
/* 121 */     char[] arrayOfChar = new char[paramString.length() / 2];
/* 122 */     int i = arrayOfChar.length;
/* 123 */     for (int j = 0; j < i; j++)
/* 124 */       arrayOfChar[j] = ((char)Integer.parseInt(paramString.substring(2 * j, 2 * j + 2), 16));
/* 125 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   public byte[] byteValue()
/*     */   {
/* 133 */     return this.value;
/*     */   }
/*     */ 
/*     */   public Byte[] toByte()
/*     */   {
/* 141 */     Byte[] arrayOfByte = new Byte[this.value.length];
/* 142 */     for (int i = 0; i < this.value.length; i++) {
/* 143 */       arrayOfByte[i] = new Byte(this.value[i]);
/*     */     }
/* 145 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 153 */     return new String(this.value);
/*     */   }
/*     */ 
/*     */   public SnmpOid toOid()
/*     */   {
/* 161 */     long[] arrayOfLong = new long[this.value.length];
/* 162 */     for (int i = 0; i < this.value.length; i++) {
/* 163 */       arrayOfLong[i] = (this.value[i] & 0xFF);
/*     */     }
/* 165 */     return new SnmpOid(arrayOfLong);
/*     */   }
/*     */ 
/*     */   public static SnmpOid toOid(long[] paramArrayOfLong, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/*     */     try
/*     */     {
/* 179 */       if (paramArrayOfLong[paramInt] > 2147483647L) {
/* 180 */         throw new SnmpStatusException(2);
/*     */       }
/* 182 */       int i = (int)paramArrayOfLong[(paramInt++)];
/* 183 */       long[] arrayOfLong = new long[i];
/* 184 */       for (int j = 0; j < i; j++) {
/* 185 */         arrayOfLong[j] = paramArrayOfLong[(paramInt + j)];
/*     */       }
/* 187 */       return new SnmpOid(arrayOfLong);
/*     */     } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*     */     }
/* 190 */     throw new SnmpStatusException(2);
/*     */   }
/*     */ 
/*     */   public static int nextOid(long[] paramArrayOfLong, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/*     */     try
/*     */     {
/* 205 */       if (paramArrayOfLong[paramInt] > 2147483647L) {
/* 206 */         throw new SnmpStatusException(2);
/*     */       }
/* 208 */       int i = (int)paramArrayOfLong[(paramInt++)];
/* 209 */       paramInt += i;
/* 210 */       if (paramInt <= paramArrayOfLong.length) {
/* 211 */         return paramInt;
/*     */       }
/*     */ 
/* 214 */       throw new SnmpStatusException(2);
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*     */     }
/* 218 */     throw new SnmpStatusException(2);
/*     */   }
/*     */ 
/*     */   public static void appendToOid(SnmpOid paramSnmpOid1, SnmpOid paramSnmpOid2)
/*     */   {
/* 228 */     paramSnmpOid2.append(paramSnmpOid1.getLength());
/* 229 */     paramSnmpOid2.append(paramSnmpOid1);
/*     */   }
/*     */ 
/*     */   public final synchronized SnmpValue duplicate()
/*     */   {
/* 238 */     return (SnmpValue)clone();
/*     */   }
/*     */ 
/*     */   public synchronized Object clone()
/*     */   {
/* 246 */     SnmpString localSnmpString = null;
/*     */     try
/*     */     {
/* 249 */       localSnmpString = (SnmpString)super.clone();
/* 250 */       localSnmpString.value = new byte[this.value.length];
/* 251 */       System.arraycopy(this.value, 0, localSnmpString.value, 0, this.value.length);
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 253 */       throw new InternalError();
/*     */     }
/* 255 */     return localSnmpString;
/*     */   }
/*     */ 
/*     */   public String getTypeName()
/*     */   {
/* 263 */     return "String";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpString
 * JD-Core Version:    0.6.2
 */