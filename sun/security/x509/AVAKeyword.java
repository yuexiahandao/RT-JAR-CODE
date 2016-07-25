/*      */ package sun.security.x509;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import sun.security.pkcs.PKCS9Attribute;
/*      */ import sun.security.util.ObjectIdentifier;
/*      */ 
/*      */ class AVAKeyword
/*      */ {
/* 1355 */   private static final Map<ObjectIdentifier, AVAKeyword> oidMap = new HashMap();
/* 1356 */   private static final Map<String, AVAKeyword> keywordMap = new HashMap();
/*      */   private String keyword;
/*      */   private ObjectIdentifier oid;
/*      */   private boolean rfc1779Compliant;
/*      */   private boolean rfc2253Compliant;
/*      */ 
/*      */   private AVAKeyword(String paramString, ObjectIdentifier paramObjectIdentifier, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1189 */     this.keyword = paramString;
/* 1190 */     this.oid = paramObjectIdentifier;
/* 1191 */     this.rfc1779Compliant = paramBoolean1;
/* 1192 */     this.rfc2253Compliant = paramBoolean2;
/*      */ 
/* 1195 */     oidMap.put(paramObjectIdentifier, this);
/* 1196 */     keywordMap.put(paramString, this);
/*      */   }
/*      */ 
/*      */   private boolean isCompliant(int paramInt) {
/* 1200 */     switch (paramInt) {
/*      */     case 2:
/* 1202 */       return this.rfc1779Compliant;
/*      */     case 3:
/* 1204 */       return this.rfc2253Compliant;
/*      */     case 1:
/* 1206 */       return true;
/*      */     }
/*      */ 
/* 1209 */     throw new IllegalArgumentException("Invalid standard " + paramInt);
/*      */   }
/*      */ 
/*      */   static ObjectIdentifier getOID(String paramString, int paramInt)
/*      */     throws IOException
/*      */   {
/* 1221 */     return getOID(paramString, paramInt, Collections.emptyMap());
/*      */   }
/*      */ 
/*      */   static ObjectIdentifier getOID(String paramString, int paramInt, Map<String, String> paramMap)
/*      */     throws IOException
/*      */   {
/* 1240 */     paramString = paramString.toUpperCase(Locale.ENGLISH);
/* 1241 */     if (paramInt == 3) {
/* 1242 */       if ((paramString.startsWith(" ")) || (paramString.endsWith(" "))) {
/* 1243 */         throw new IOException("Invalid leading or trailing space in keyword \"" + paramString + "\"");
/*      */       }
/*      */     }
/*      */     else {
/* 1247 */       paramString = paramString.trim();
/*      */     }
/*      */ 
/* 1252 */     String str = (String)paramMap.get(paramString);
/* 1253 */     if (str == null) {
/* 1254 */       AVAKeyword localAVAKeyword = (AVAKeyword)keywordMap.get(paramString);
/* 1255 */       if ((localAVAKeyword != null) && (localAVAKeyword.isCompliant(paramInt)))
/* 1256 */         return localAVAKeyword.oid;
/*      */     }
/*      */     else {
/* 1259 */       return new ObjectIdentifier(str);
/*      */     }
/*      */ 
/* 1265 */     if (paramInt == 2) {
/* 1266 */       if (!paramString.startsWith("OID.")) {
/* 1267 */         throw new IOException("Invalid RFC1779 keyword: " + paramString);
/*      */       }
/* 1269 */       paramString = paramString.substring(4);
/* 1270 */     } else if ((paramInt == 1) && 
/* 1271 */       (paramString.startsWith("OID."))) {
/* 1272 */       paramString = paramString.substring(4);
/*      */     }
/*      */ 
/* 1275 */     int i = 0;
/* 1276 */     if (paramString.length() != 0) {
/* 1277 */       int j = paramString.charAt(0);
/* 1278 */       if ((j >= 48) && (j <= 57)) {
/* 1279 */         i = 1;
/*      */       }
/*      */     }
/* 1282 */     if (i == 0) {
/* 1283 */       throw new IOException("Invalid keyword \"" + paramString + "\"");
/*      */     }
/* 1285 */     return new ObjectIdentifier(paramString);
/*      */   }
/*      */ 
/*      */   static String getKeyword(ObjectIdentifier paramObjectIdentifier, int paramInt)
/*      */   {
/* 1294 */     return getKeyword(paramObjectIdentifier, paramInt, Collections.emptyMap());
/*      */   }
/*      */ 
/*      */   static String getKeyword(ObjectIdentifier paramObjectIdentifier, int paramInt, Map<String, String> paramMap)
/*      */   {
/* 1308 */     String str1 = paramObjectIdentifier.toString();
/* 1309 */     String str2 = (String)paramMap.get(str1);
/* 1310 */     if (str2 == null) {
/* 1311 */       AVAKeyword localAVAKeyword = (AVAKeyword)oidMap.get(paramObjectIdentifier);
/* 1312 */       if ((localAVAKeyword != null) && (localAVAKeyword.isCompliant(paramInt)))
/* 1313 */         return localAVAKeyword.keyword;
/*      */     }
/*      */     else {
/* 1316 */       if (str2.length() == 0) {
/* 1317 */         throw new IllegalArgumentException("keyword cannot be empty");
/*      */       }
/* 1319 */       str2 = str2.trim();
/* 1320 */       int i = str2.charAt(0);
/* 1321 */       if ((i < 65) || (i > 122) || ((i > 90) && (i < 97))) {
/* 1322 */         throw new IllegalArgumentException("keyword does not start with letter");
/*      */       }
/*      */ 
/* 1325 */       for (int j = 1; j < str2.length(); j++) {
/* 1326 */         i = str2.charAt(j);
/* 1327 */         if (((i < 65) || (i > 122) || ((i > 90) && (i < 97))) && ((i < 48) || (i > 57)) && (i != 95))
/*      */         {
/* 1329 */           throw new IllegalArgumentException("keyword character is not a letter, digit, or underscore");
/*      */         }
/*      */       }
/*      */ 
/* 1333 */       return str2;
/*      */     }
/*      */ 
/* 1336 */     if (paramInt == 3) {
/* 1337 */       return str1;
/*      */     }
/* 1339 */     return "OID." + str1;
/*      */   }
/*      */ 
/*      */   static boolean hasKeyword(ObjectIdentifier paramObjectIdentifier, int paramInt)
/*      */   {
/* 1347 */     AVAKeyword localAVAKeyword = (AVAKeyword)oidMap.get(paramObjectIdentifier);
/* 1348 */     if (localAVAKeyword == null) {
/* 1349 */       return false;
/*      */     }
/* 1351 */     return localAVAKeyword.isCompliant(paramInt);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1360 */     new AVAKeyword("CN", X500Name.commonName_oid, true, true);
/* 1361 */     new AVAKeyword("C", X500Name.countryName_oid, true, true);
/* 1362 */     new AVAKeyword("L", X500Name.localityName_oid, true, true);
/* 1363 */     new AVAKeyword("S", X500Name.stateName_oid, false, false);
/* 1364 */     new AVAKeyword("ST", X500Name.stateName_oid, true, true);
/* 1365 */     new AVAKeyword("O", X500Name.orgName_oid, true, true);
/* 1366 */     new AVAKeyword("OU", X500Name.orgUnitName_oid, true, true);
/* 1367 */     new AVAKeyword("T", X500Name.title_oid, false, false);
/* 1368 */     new AVAKeyword("IP", X500Name.ipAddress_oid, false, false);
/* 1369 */     new AVAKeyword("STREET", X500Name.streetAddress_oid, true, true);
/* 1370 */     new AVAKeyword("DC", X500Name.DOMAIN_COMPONENT_OID, false, true);
/*      */ 
/* 1372 */     new AVAKeyword("DNQUALIFIER", X500Name.DNQUALIFIER_OID, false, false);
/* 1373 */     new AVAKeyword("DNQ", X500Name.DNQUALIFIER_OID, false, false);
/* 1374 */     new AVAKeyword("SURNAME", X500Name.SURNAME_OID, false, false);
/* 1375 */     new AVAKeyword("GIVENNAME", X500Name.GIVENNAME_OID, false, false);
/* 1376 */     new AVAKeyword("INITIALS", X500Name.INITIALS_OID, false, false);
/* 1377 */     new AVAKeyword("GENERATION", X500Name.GENERATIONQUALIFIER_OID, false, false);
/*      */ 
/* 1379 */     new AVAKeyword("EMAIL", PKCS9Attribute.EMAIL_ADDRESS_OID, false, false);
/* 1380 */     new AVAKeyword("EMAILADDRESS", PKCS9Attribute.EMAIL_ADDRESS_OID, false, false);
/*      */ 
/* 1382 */     new AVAKeyword("UID", X500Name.userid_oid, false, true);
/* 1383 */     new AVAKeyword("SERIALNUMBER", X500Name.SERIALNUMBER_OID, false, false);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.AVAKeyword
 * JD-Core Version:    0.6.2
 */