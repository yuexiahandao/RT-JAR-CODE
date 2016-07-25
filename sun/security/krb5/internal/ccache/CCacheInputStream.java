/*     */ package sun.security.krb5.internal.ccache;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Date;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.Realm;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.krb5.internal.AuthorizationData;
/*     */ import sun.security.krb5.internal.AuthorizationDataEntry;
/*     */ import sun.security.krb5.internal.HostAddress;
/*     */ import sun.security.krb5.internal.HostAddresses;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.KrbApErrException;
/*     */ import sun.security.krb5.internal.Ticket;
/*     */ import sun.security.krb5.internal.TicketFlags;
/*     */ import sun.security.krb5.internal.util.KrbDataInputStream;
/*     */ 
/*     */ public class CCacheInputStream extends KrbDataInputStream
/*     */   implements FileCCacheConstants
/*     */ {
/*  67 */   private static boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   public CCacheInputStream(InputStream paramInputStream) {
/*  70 */     super(paramInputStream);
/*     */   }
/*     */ 
/*     */   public Tag readTag()
/*     */     throws IOException
/*     */   {
/*  76 */     char[] arrayOfChar = new char[1024];
/*     */ 
/*  79 */     int j = -1;
/*     */ 
/*  81 */     Integer localInteger1 = null;
/*  82 */     Integer localInteger2 = null;
/*     */ 
/*  84 */     int i = read(2);
/*  85 */     if (i < 0) {
/*  86 */       throw new IOException("stop.");
/*     */     }
/*  88 */     byte[] arrayOfByte = new byte[i + 2];
/*  89 */     if (i > arrayOfChar.length) {
/*  90 */       throw new IOException("Invalid tag length.");
/*     */     }
/*  92 */     while (i > 0) {
/*  93 */       j = read(2);
/*  94 */       int k = read(2);
/*  95 */       switch (j) {
/*     */       case 1:
/*  97 */         localInteger1 = new Integer(read(4));
/*  98 */         localInteger2 = new Integer(read(4));
/*  99 */         break;
/*     */       }
/*     */ 
/* 102 */       i -= 4 + k;
/*     */     }
/*     */ 
/* 105 */     if (j == -1);
/* 107 */     Tag localTag = new Tag(i, j, localInteger1, localInteger2);
/* 108 */     return localTag;
/*     */   }
/*     */ 
/*     */   public PrincipalName readPrincipal(int paramInt)
/*     */     throws IOException, RealmException
/*     */   {
/* 118 */     String[] arrayOfString1 = null;
/*     */     int i;
/* 121 */     if (paramInt == 1281)
/* 122 */       i = 0;
/*     */     else {
/* 124 */       i = read(4);
/*     */     }
/* 126 */     int j = readLength4();
/* 127 */     String[] arrayOfString2 = new String[j + 1];
/*     */ 
/* 132 */     if (paramInt == 1281)
/* 133 */       j--;
/* 134 */     for (int m = 0; m <= j; m++) {
/* 135 */       int k = readLength4();
/* 136 */       if (k > 1024) {
/* 137 */         throw new IOException("Invalid name length in principal name.");
/*     */       }
/* 139 */       byte[] arrayOfByte = new byte[k];
/* 140 */       read(arrayOfByte, 0, k);
/* 141 */       arrayOfString2[m] = new String(arrayOfByte);
/*     */     }
/*     */     PrincipalName localPrincipalName;
/* 143 */     if (isRealm(arrayOfString2[0])) {
/* 144 */       String str = arrayOfString2[0];
/* 145 */       arrayOfString1 = new String[j];
/* 146 */       System.arraycopy(arrayOfString2, 1, arrayOfString1, 0, j);
/* 147 */       localPrincipalName = new PrincipalName(arrayOfString1, i);
/* 148 */       localPrincipalName.setRealm(str);
/*     */     } else {
/* 150 */       localPrincipalName = new PrincipalName(arrayOfString2, i);
/* 151 */     }return localPrincipalName;
/*     */   }
/*     */ 
/*     */   boolean isRealm(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 162 */       Realm localRealm = new Realm(paramString);
/*     */     }
/*     */     catch (Exception localException) {
/* 165 */       return false;
/*     */     }
/* 167 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ".");
/*     */ 
/* 169 */     while (localStringTokenizer.hasMoreTokens()) {
/* 170 */       String str = localStringTokenizer.nextToken();
/* 171 */       for (int i = 0; i < str.length(); i++) {
/* 172 */         if (str.charAt(i) >= '') {
/* 173 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 177 */     return true;
/*     */   }
/*     */ 
/*     */   EncryptionKey readKey(int paramInt) throws IOException
/*     */   {
/* 182 */     int i = read(2);
/* 183 */     if (paramInt == 1283)
/* 184 */       read(2);
/* 185 */     int j = readLength4();
/* 186 */     byte[] arrayOfByte = new byte[j];
/* 187 */     for (int k = 0; k < j; k++) {
/* 188 */       arrayOfByte[k] = ((byte)read());
/*     */     }
/* 190 */     return new EncryptionKey(arrayOfByte, i, new Integer(paramInt));
/*     */   }
/*     */ 
/*     */   long[] readTimes() throws IOException {
/* 194 */     long[] arrayOfLong = new long[4];
/* 195 */     arrayOfLong[0] = (read(4) * 1000L);
/* 196 */     arrayOfLong[1] = (read(4) * 1000L);
/* 197 */     arrayOfLong[2] = (read(4) * 1000L);
/* 198 */     arrayOfLong[3] = (read(4) * 1000L);
/* 199 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   boolean readskey() throws IOException {
/* 203 */     if (read() == 0) {
/* 204 */       return false;
/*     */     }
/* 206 */     return true;
/*     */   }
/*     */ 
/*     */   HostAddress[] readAddr() throws IOException, KrbApErrException
/*     */   {
/* 211 */     int i = readLength4();
/* 212 */     if (i > 0) {
/* 213 */       HostAddress[] arrayOfHostAddress = new HostAddress[i];
/* 214 */       for (int m = 0; m < i; m++) {
/* 215 */         int j = read(2);
/* 216 */         int k = readLength4();
/* 217 */         if ((k != 4) && (k != 16)) {
/* 218 */           if (DEBUG) {
/* 219 */             System.out.println("Incorrect address format.");
/*     */           }
/* 221 */           return null;
/*     */         }
/* 223 */         byte[] arrayOfByte = new byte[k];
/* 224 */         for (int n = 0; n < k; n++)
/* 225 */           arrayOfByte[n] = ((byte)read(1));
/* 226 */         arrayOfHostAddress[m] = new HostAddress(j, arrayOfByte);
/*     */       }
/* 228 */       return arrayOfHostAddress;
/*     */     }
/* 230 */     return null;
/*     */   }
/*     */ 
/*     */   AuthorizationDataEntry[] readAuth() throws IOException
/*     */   {
/* 235 */     int i = readLength4();
/* 236 */     if (i > 0) {
/* 237 */       AuthorizationDataEntry[] arrayOfAuthorizationDataEntry = new AuthorizationDataEntry[i];
/* 238 */       byte[] arrayOfByte = null;
/* 239 */       for (int m = 0; m < i; m++) {
/* 240 */         int j = read(2);
/* 241 */         int k = readLength4();
/* 242 */         arrayOfByte = new byte[k];
/* 243 */         for (int n = 0; n < k; n++) {
/* 244 */           arrayOfByte[n] = ((byte)read());
/*     */         }
/* 246 */         arrayOfAuthorizationDataEntry[m] = new AuthorizationDataEntry(j, arrayOfByte);
/*     */       }
/* 248 */       return arrayOfAuthorizationDataEntry;
/*     */     }
/* 250 */     return null;
/*     */   }
/*     */ 
/*     */   byte[] readData() throws IOException
/*     */   {
/* 255 */     int i = readLength4();
/* 256 */     if (i == 0) {
/* 257 */       return null;
/*     */     }
/* 259 */     byte[] arrayOfByte = new byte[i];
/* 260 */     read(arrayOfByte, 0, i);
/* 261 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   boolean[] readFlags() throws IOException
/*     */   {
/* 266 */     boolean[] arrayOfBoolean = new boolean[32];
/*     */ 
/* 268 */     int i = read(4);
/* 269 */     if ((i & 0x40000000) == 1073741824)
/* 270 */       arrayOfBoolean[1] = true;
/* 271 */     if ((i & 0x20000000) == 536870912)
/* 272 */       arrayOfBoolean[2] = true;
/* 273 */     if ((i & 0x10000000) == 268435456)
/* 274 */       arrayOfBoolean[3] = true;
/* 275 */     if ((i & 0x8000000) == 134217728)
/* 276 */       arrayOfBoolean[4] = true;
/* 277 */     if ((i & 0x4000000) == 67108864)
/* 278 */       arrayOfBoolean[5] = true;
/* 279 */     if ((i & 0x2000000) == 33554432)
/* 280 */       arrayOfBoolean[6] = true;
/* 281 */     if ((i & 0x1000000) == 16777216)
/* 282 */       arrayOfBoolean[7] = true;
/* 283 */     if ((i & 0x800000) == 8388608)
/* 284 */       arrayOfBoolean[8] = true;
/* 285 */     if ((i & 0x400000) == 4194304)
/* 286 */       arrayOfBoolean[9] = true;
/* 287 */     if ((i & 0x200000) == 2097152)
/* 288 */       arrayOfBoolean[10] = true;
/* 289 */     if ((i & 0x100000) == 1048576)
/* 290 */       arrayOfBoolean[11] = true;
/* 291 */     if (DEBUG) {
/* 292 */       String str = ">>> CCacheInputStream: readFlags() ";
/* 293 */       if (arrayOfBoolean[1] == 1) {
/* 294 */         str = str + " FORWARDABLE;";
/*     */       }
/* 296 */       if (arrayOfBoolean[2] == 1) {
/* 297 */         str = str + " FORWARDED;";
/*     */       }
/* 299 */       if (arrayOfBoolean[3] == 1) {
/* 300 */         str = str + " PROXIABLE;";
/*     */       }
/* 302 */       if (arrayOfBoolean[4] == 1) {
/* 303 */         str = str + " PROXY;";
/*     */       }
/* 305 */       if (arrayOfBoolean[5] == 1) {
/* 306 */         str = str + " MAY_POSTDATE;";
/*     */       }
/* 308 */       if (arrayOfBoolean[6] == 1) {
/* 309 */         str = str + " POSTDATED;";
/*     */       }
/* 311 */       if (arrayOfBoolean[7] == 1) {
/* 312 */         str = str + " INVALID;";
/*     */       }
/* 314 */       if (arrayOfBoolean[8] == 1) {
/* 315 */         str = str + " RENEWABLE;";
/*     */       }
/*     */ 
/* 318 */       if (arrayOfBoolean[9] == 1) {
/* 319 */         str = str + " INITIAL;";
/*     */       }
/* 321 */       if (arrayOfBoolean[10] == 1) {
/* 322 */         str = str + " PRE_AUTH;";
/*     */       }
/* 324 */       if (arrayOfBoolean[11] == 1) {
/* 325 */         str = str + " HW_AUTH;";
/*     */       }
/* 327 */       System.out.println(str);
/*     */     }
/* 329 */     return arrayOfBoolean;
/*     */   }
/*     */ 
/*     */   Credentials readCred(int paramInt)
/*     */     throws IOException, RealmException, KrbApErrException, Asn1Exception
/*     */   {
/* 343 */     PrincipalName localPrincipalName1 = readPrincipal(paramInt);
/* 344 */     if (DEBUG)
/* 345 */       System.out.println(">>>DEBUG <CCacheInputStream>  client principal is " + localPrincipalName1.toString());
/* 346 */     PrincipalName localPrincipalName2 = readPrincipal(paramInt);
/* 347 */     if (DEBUG)
/* 348 */       System.out.println(">>>DEBUG <CCacheInputStream> server principal is " + localPrincipalName2.toString());
/* 349 */     EncryptionKey localEncryptionKey = readKey(paramInt);
/* 350 */     if (DEBUG)
/* 351 */       System.out.println(">>>DEBUG <CCacheInputStream> key type: " + localEncryptionKey.getEType());
/* 352 */     long[] arrayOfLong = readTimes();
/* 353 */     KerberosTime localKerberosTime1 = new KerberosTime(arrayOfLong[0]);
/* 354 */     KerberosTime localKerberosTime2 = arrayOfLong[1] == 0L ? null : new KerberosTime(arrayOfLong[1]);
/*     */ 
/* 356 */     KerberosTime localKerberosTime3 = new KerberosTime(arrayOfLong[2]);
/* 357 */     KerberosTime localKerberosTime4 = arrayOfLong[3] == 0L ? null : new KerberosTime(arrayOfLong[3]);
/*     */ 
/* 360 */     if (DEBUG) {
/* 361 */       System.out.println(">>>DEBUG <CCacheInputStream> auth time: " + localKerberosTime1.toDate().toString());
/* 362 */       System.out.println(">>>DEBUG <CCacheInputStream> start time: " + (localKerberosTime2 == null ? "null" : localKerberosTime2.toDate().toString()));
/*     */ 
/* 364 */       System.out.println(">>>DEBUG <CCacheInputStream> end time: " + localKerberosTime3.toDate().toString());
/* 365 */       System.out.println(">>>DEBUG <CCacheInputStream> renew_till time: " + (localKerberosTime4 == null ? "null" : localKerberosTime4.toDate().toString()));
/*     */     }
/*     */ 
/* 368 */     boolean bool = readskey();
/* 369 */     boolean[] arrayOfBoolean = readFlags();
/* 370 */     TicketFlags localTicketFlags = new TicketFlags(arrayOfBoolean);
/* 371 */     HostAddress[] arrayOfHostAddress = readAddr();
/* 372 */     HostAddresses localHostAddresses = null;
/* 373 */     if (arrayOfHostAddress != null) {
/* 374 */       localHostAddresses = new HostAddresses(arrayOfHostAddress);
/*     */     }
/* 376 */     AuthorizationDataEntry[] arrayOfAuthorizationDataEntry = readAuth();
/* 377 */     AuthorizationData localAuthorizationData = null;
/* 378 */     if (localAuthorizationData != null) {
/* 379 */       localAuthorizationData = new AuthorizationData(arrayOfAuthorizationDataEntry);
/*     */     }
/* 381 */     byte[] arrayOfByte1 = readData();
/* 382 */     byte[] arrayOfByte2 = readData();
/*     */     try
/*     */     {
/* 385 */       return new Credentials(localPrincipalName1, localPrincipalName2, localEncryptionKey, localKerberosTime1, localKerberosTime2, localKerberosTime3, localKerberosTime4, bool, localTicketFlags, localHostAddresses, localAuthorizationData, arrayOfByte1 != null ? new Ticket(arrayOfByte1) : null, arrayOfByte2 != null ? new Ticket(arrayOfByte2) : null);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/* 391 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.ccache.CCacheInputStream
 * JD-Core Version:    0.6.2
 */