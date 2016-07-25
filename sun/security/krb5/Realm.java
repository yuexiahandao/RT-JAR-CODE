/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.LinkedList;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.util.KerberosString;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class Realm
/*     */   implements Cloneable
/*     */ {
/*     */   private String realm;
/*  53 */   private static boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   private Realm() {
/*     */   }
/*     */ 
/*     */   public Realm(String paramString) throws RealmException {
/*  59 */     this.realm = parseRealm(paramString);
/*     */   }
/*     */ 
/*     */   public Object clone() {
/*  63 */     Realm localRealm = new Realm();
/*  64 */     if (this.realm != null) {
/*  65 */       localRealm.realm = new String(this.realm);
/*     */     }
/*  67 */     return localRealm;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*  71 */     if (this == paramObject) {
/*  72 */       return true;
/*     */     }
/*     */ 
/*  75 */     if (!(paramObject instanceof Realm)) {
/*  76 */       return false;
/*     */     }
/*     */ 
/*  79 */     Realm localRealm = (Realm)paramObject;
/*  80 */     if ((this.realm != null) && (localRealm.realm != null)) {
/*  81 */       return this.realm.equals(localRealm.realm);
/*     */     }
/*  83 */     return (this.realm == null) && (localRealm.realm == null);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  88 */     int i = 17;
/*     */ 
/*  90 */     if (this.realm != null) {
/*  91 */       i = 37 * i + this.realm.hashCode();
/*     */     }
/*     */ 
/*  94 */     return i;
/*     */   }
/*     */ 
/*     */   public Realm(DerValue paramDerValue)
/*     */     throws Asn1Exception, RealmException, IOException
/*     */   {
/* 106 */     if (paramDerValue == null) {
/* 107 */       throw new IllegalArgumentException("encoding can not be null");
/*     */     }
/* 109 */     this.realm = new KerberosString(paramDerValue).toString();
/* 110 */     if ((this.realm == null) || (this.realm.length() == 0))
/* 111 */       throw new RealmException(601);
/* 112 */     if (!isValidRealmString(this.realm))
/* 113 */       throw new RealmException(600);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 117 */     return this.realm;
/*     */   }
/*     */ 
/*     */   public static String parseRealmAtSeparator(String paramString) throws RealmException
/*     */   {
/* 122 */     if (paramString == null) {
/* 123 */       throw new IllegalArgumentException("null input name is not allowed");
/*     */     }
/*     */ 
/* 126 */     String str1 = new String(paramString);
/* 127 */     String str2 = null;
/* 128 */     int i = 0;
/* 129 */     while (i < str1.length()) {
/* 130 */       if ((str1.charAt(i) == '@') && (
/* 131 */         (i == 0) || (str1.charAt(i - 1) != '\\'))) {
/* 132 */         if (i + 1 >= str1.length()) break;
/* 133 */         str2 = str1.substring(i + 1, str1.length()); break;
/*     */       }
/*     */ 
/* 137 */       i++;
/*     */     }
/* 139 */     if (str2 != null) {
/* 140 */       if (str2.length() == 0)
/* 141 */         throw new RealmException(601);
/* 142 */       if (!isValidRealmString(str2))
/* 143 */         throw new RealmException(600);
/*     */     }
/* 145 */     return str2;
/*     */   }
/*     */ 
/*     */   public static String parseRealmComponent(String paramString) {
/* 149 */     if (paramString == null) {
/* 150 */       throw new IllegalArgumentException("null input name is not allowed");
/*     */     }
/*     */ 
/* 153 */     String str1 = new String(paramString);
/* 154 */     String str2 = null;
/* 155 */     int i = 0;
/* 156 */     while (i < str1.length()) {
/* 157 */       if ((str1.charAt(i) == '.') && (
/* 158 */         (i == 0) || (str1.charAt(i - 1) != '\\'))) {
/* 159 */         if (i + 1 >= str1.length()) break;
/* 160 */         str2 = str1.substring(i + 1, str1.length()); break;
/*     */       }
/*     */ 
/* 164 */       i++;
/*     */     }
/* 166 */     return str2;
/*     */   }
/*     */ 
/*     */   protected static String parseRealm(String paramString) throws RealmException {
/* 170 */     String str = parseRealmAtSeparator(paramString);
/* 171 */     if (str == null)
/* 172 */       str = paramString;
/* 173 */     if ((str == null) || (str.length() == 0))
/* 174 */       throw new RealmException(601);
/* 175 */     if (!isValidRealmString(str))
/* 176 */       throw new RealmException(600);
/* 177 */     return str;
/*     */   }
/*     */ 
/*     */   protected static boolean isValidRealmString(String paramString)
/*     */   {
/* 183 */     if (paramString == null)
/* 184 */       return false;
/* 185 */     if (paramString.length() == 0)
/* 186 */       return false;
/* 187 */     for (int i = 0; i < paramString.length(); i++) {
/* 188 */       if ((paramString.charAt(i) == '/') || (paramString.charAt(i) == ':') || (paramString.charAt(i) == 0))
/*     */       {
/* 191 */         return false;
/*     */       }
/*     */     }
/* 194 */     return true;
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 205 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 206 */     localDerOutputStream.putDerValue(new KerberosString(this.realm).toDerValue());
/* 207 */     return localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public static Realm parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException, RealmException
/*     */   {
/* 224 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte)) {
/* 225 */       return null;
/*     */     }
/* 227 */     DerValue localDerValue1 = paramDerInputStream.getDerValue();
/* 228 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/* 229 */       throw new Asn1Exception(906);
/*     */     }
/* 231 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 232 */     return new Realm(localDerValue2);
/*     */   }
/*     */ 
/*     */   private static String[] doInitialParse(String paramString1, String paramString2)
/*     */     throws KrbException
/*     */   {
/* 241 */     if ((paramString1 == null) || (paramString2 == null)) {
/* 242 */       throw new KrbException(400);
/*     */     }
/* 244 */     if (DEBUG) {
/* 245 */       System.out.println(">>> Realm doInitialParse: cRealm=[" + paramString1 + "], sRealm=[" + paramString2 + "]");
/*     */     }
/*     */ 
/* 248 */     if (paramString1.equals(paramString2)) {
/* 249 */       String[] arrayOfString = null;
/* 250 */       arrayOfString = new String[1];
/* 251 */       arrayOfString[0] = new String(paramString1);
/*     */ 
/* 253 */       if (DEBUG) {
/* 254 */         System.out.println(">>> Realm doInitialParse: " + arrayOfString[0]);
/*     */       }
/*     */ 
/* 257 */       return arrayOfString;
/*     */     }
/* 259 */     return null;
/*     */   }
/*     */ 
/*     */   public static String[] getRealmsList(String paramString1, String paramString2)
/*     */     throws KrbException
/*     */   {
/* 286 */     String[] arrayOfString = doInitialParse(paramString1, paramString2);
/* 287 */     if ((arrayOfString != null) && (arrayOfString.length != 0)) {
/* 288 */       return arrayOfString;
/*     */     }
/*     */ 
/* 293 */     arrayOfString = parseCapaths(paramString1, paramString2);
/* 294 */     if ((arrayOfString != null) && (arrayOfString.length != 0)) {
/* 295 */       return arrayOfString;
/*     */     }
/*     */ 
/* 300 */     arrayOfString = parseHierarchy(paramString1, paramString2);
/* 301 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static String[] parseCapaths(String paramString1, String paramString2)
/*     */   {
/* 351 */     Config localConfig = null;
/*     */     try {
/* 353 */       localConfig = Config.getInstance();
/*     */     } catch (Exception localException) {
/* 355 */       if (DEBUG) {
/* 356 */         System.out.println("Configuration information can not be obtained " + localException.getMessage());
/*     */       }
/*     */ 
/* 359 */       return null;
/*     */     }
/*     */ 
/* 362 */     String str1 = localConfig.getDefault(paramString2, paramString1);
/*     */ 
/* 364 */     if (str1 == null) {
/* 365 */       if (DEBUG) {
/* 366 */         System.out.println(">>> Realm parseCapaths: no cfg entry");
/*     */       }
/* 368 */       return null;
/*     */     }
/*     */ 
/* 371 */     LinkedList localLinkedList = new LinkedList();
/*     */ 
/* 373 */     String str2 = paramString2;
/*     */     while (true) {
/* 375 */       String str3 = localConfig.getDefault(str2, paramString1);
/* 376 */       if (str3 == null) {
/*     */         break;
/*     */       }
/* 379 */       String[] arrayOfString = str3.split("\\s+");
/* 380 */       int i = 0;
/* 381 */       for (int j = arrayOfString.length - 1; j >= 0; j--)
/* 382 */         if ((!localLinkedList.contains(arrayOfString[j])) && (!arrayOfString[j].equals(".")) && (!arrayOfString[j].equals(paramString1)) && (!arrayOfString[j].equals(paramString2)) && (!arrayOfString[j].equals(str2)))
/*     */         {
/* 390 */           i = 1;
/* 391 */           localLinkedList.addFirst(arrayOfString[j]);
/*     */         }
/* 393 */       if (i == 0) break;
/* 394 */       str2 = (String)localLinkedList.getFirst();
/*     */     }
/* 396 */     localLinkedList.addFirst(paramString1);
/* 397 */     return (String[])localLinkedList.toArray(new String[localLinkedList.size()]);
/*     */   }
/*     */ 
/*     */   private static String[] parseHierarchy(String paramString1, String paramString2)
/*     */   {
/* 410 */     String[] arrayOfString1 = paramString1.split("\\.");
/* 411 */     String[] arrayOfString2 = paramString2.split("\\.");
/*     */ 
/* 413 */     int i = arrayOfString1.length;
/* 414 */     int j = arrayOfString2.length;
/*     */ 
/* 416 */     int k = 0;
/* 417 */     j--; for (i--; (j >= 0) && (i >= 0) && (arrayOfString2[j].equals(arrayOfString1[i])); 
/* 419 */       i--) {
/* 420 */       k = 1;
/*     */ 
/* 419 */       j--;
/*     */     }
/*     */ 
/* 433 */     LinkedList localLinkedList = new LinkedList();
/*     */ 
/* 436 */     for (int m = 0; m <= i; m++) {
/* 437 */       localLinkedList.addLast(subStringFrom(arrayOfString1, m));
/*     */     }
/*     */ 
/* 441 */     if (k != 0) {
/* 442 */       localLinkedList.addLast(subStringFrom(arrayOfString1, i + 1));
/*     */     }
/*     */ 
/* 446 */     for (m = j; m >= 0; m--) {
/* 447 */       localLinkedList.addLast(subStringFrom(arrayOfString2, m));
/*     */     }
/*     */ 
/* 452 */     localLinkedList.removeLast();
/*     */ 
/* 454 */     return (String[])localLinkedList.toArray(new String[localLinkedList.size()]);
/*     */   }
/*     */ 
/*     */   private static String subStringFrom(String[] paramArrayOfString, int paramInt)
/*     */   {
/* 462 */     StringBuilder localStringBuilder = new StringBuilder();
/* 463 */     for (int i = paramInt; i < paramArrayOfString.length; i++) {
/* 464 */       if (localStringBuilder.length() != 0) localStringBuilder.append('.');
/* 465 */       localStringBuilder.append(paramArrayOfString[i]);
/*     */     }
/* 467 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.Realm
 * JD-Core Version:    0.6.2
 */