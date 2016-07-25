/*     */ package sun.security.util;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.AlgorithmConstraints;
/*     */ import java.security.AlgorithmParameters;
/*     */ import java.security.CryptoPrimitive;
/*     */ import java.security.Key;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Security;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class DisabledAlgorithmConstraints
/*     */   implements AlgorithmConstraints
/*     */ {
/*     */   public static final String PROPERTY_CERTPATH_DISABLED_ALGS = "jdk.certpath.disabledAlgorithms";
/*     */   public static final String PROPERTY_TLS_DISABLED_ALGS = "jdk.tls.disabledAlgorithms";
/*  62 */   private static Map<String, String[]> disabledAlgorithmsMap = Collections.synchronizedMap(new HashMap());
/*     */ 
/*  64 */   private static Map<String, KeySizeConstraints> keySizeConstraintsMap = Collections.synchronizedMap(new HashMap());
/*     */   private String[] disabledAlgorithms;
/*     */   private KeySizeConstraints keySizeConstraints;
/*     */ 
/*     */   public DisabledAlgorithmConstraints(String paramString)
/*     */   {
/*  77 */     synchronized (disabledAlgorithmsMap) {
/*  78 */       if (!disabledAlgorithmsMap.containsKey(paramString)) {
/*  79 */         loadDisabledAlgorithmsMap(paramString);
/*     */       }
/*     */ 
/*  82 */       this.disabledAlgorithms = ((String[])disabledAlgorithmsMap.get(paramString));
/*  83 */       this.keySizeConstraints = ((KeySizeConstraints)keySizeConstraintsMap.get(paramString));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean permits(Set<CryptoPrimitive> paramSet, String paramString, AlgorithmParameters paramAlgorithmParameters)
/*     */   {
/*  91 */     if ((paramString == null) || (paramString.length() == 0)) {
/*  92 */       throw new IllegalArgumentException("No algorithm name specified");
/*     */     }
/*     */ 
/*  95 */     if ((paramSet == null) || (paramSet.isEmpty())) {
/*  96 */       throw new IllegalArgumentException("No cryptographic primitive specified");
/*     */     }
/*     */ 
/* 100 */     Set localSet = null;
/*     */     String str1;
/* 101 */     for (str1 : this.disabledAlgorithms) {
/* 102 */       if ((str1 != null) && (!str1.isEmpty()))
/*     */       {
/* 107 */         if (str1.equalsIgnoreCase(paramString)) {
/* 108 */           return false;
/*     */         }
/*     */ 
/* 112 */         if (localSet == null) {
/* 113 */           localSet = decomposes(paramString);
/*     */         }
/*     */ 
/* 117 */         for (String str2 : localSet) {
/* 118 */           if (str1.equalsIgnoreCase(str2)) {
/* 119 */             return false;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 124 */     return true;
/*     */   }
/*     */ 
/*     */   public final boolean permits(Set<CryptoPrimitive> paramSet, Key paramKey)
/*     */   {
/* 129 */     return checkConstraints(paramSet, "", paramKey, null);
/*     */   }
/*     */ 
/*     */   public final boolean permits(Set<CryptoPrimitive> paramSet, String paramString, Key paramKey, AlgorithmParameters paramAlgorithmParameters)
/*     */   {
/* 136 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 137 */       throw new IllegalArgumentException("No algorithm name specified");
/*     */     }
/*     */ 
/* 140 */     return checkConstraints(paramSet, paramString, paramKey, paramAlgorithmParameters);
/*     */   }
/*     */ 
/*     */   protected Set<String> decomposes(String paramString)
/*     */   {
/* 153 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 154 */       return new HashSet();
/*     */     }
/*     */ 
/* 158 */     Pattern localPattern1 = Pattern.compile("/");
/* 159 */     String[] arrayOfString1 = localPattern1.split(paramString);
/*     */ 
/* 161 */     HashSet localHashSet = new HashSet();
/* 162 */     for (String str1 : arrayOfString1) {
/* 163 */       if ((str1 != null) && (str1.length() != 0))
/*     */       {
/* 172 */         Pattern localPattern2 = Pattern.compile("with|and", 2);
/*     */ 
/* 174 */         String[] arrayOfString3 = localPattern2.split(str1);
/*     */ 
/* 176 */         for (String str2 : arrayOfString3) {
/* 177 */           if ((str2 != null) && (str2.length() != 0))
/*     */           {
/* 181 */             localHashSet.add(str2);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 193 */     if ((localHashSet.contains("SHA1")) && (!localHashSet.contains("SHA-1"))) {
/* 194 */       localHashSet.add("SHA-1");
/*     */     }
/* 196 */     if ((localHashSet.contains("SHA-1")) && (!localHashSet.contains("SHA1"))) {
/* 197 */       localHashSet.add("SHA1");
/*     */     }
/*     */ 
/* 201 */     if ((localHashSet.contains("SHA224")) && (!localHashSet.contains("SHA-224"))) {
/* 202 */       localHashSet.add("SHA-224");
/*     */     }
/* 204 */     if ((localHashSet.contains("SHA-224")) && (!localHashSet.contains("SHA224"))) {
/* 205 */       localHashSet.add("SHA224");
/*     */     }
/*     */ 
/* 209 */     if ((localHashSet.contains("SHA256")) && (!localHashSet.contains("SHA-256"))) {
/* 210 */       localHashSet.add("SHA-256");
/*     */     }
/* 212 */     if ((localHashSet.contains("SHA-256")) && (!localHashSet.contains("SHA256"))) {
/* 213 */       localHashSet.add("SHA256");
/*     */     }
/*     */ 
/* 217 */     if ((localHashSet.contains("SHA384")) && (!localHashSet.contains("SHA-384"))) {
/* 218 */       localHashSet.add("SHA-384");
/*     */     }
/* 220 */     if ((localHashSet.contains("SHA-384")) && (!localHashSet.contains("SHA384"))) {
/* 221 */       localHashSet.add("SHA384");
/*     */     }
/*     */ 
/* 225 */     if ((localHashSet.contains("SHA512")) && (!localHashSet.contains("SHA-512"))) {
/* 226 */       localHashSet.add("SHA-512");
/*     */     }
/* 228 */     if ((localHashSet.contains("SHA-512")) && (!localHashSet.contains("SHA512"))) {
/* 229 */       localHashSet.add("SHA512");
/*     */     }
/*     */ 
/* 232 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   private boolean checkConstraints(Set<CryptoPrimitive> paramSet, String paramString, Key paramKey, AlgorithmParameters paramAlgorithmParameters)
/*     */   {
/* 240 */     if (paramKey == null) {
/* 241 */       throw new IllegalArgumentException("The key cannot be null");
/*     */     }
/*     */ 
/* 245 */     if ((paramString != null) && (paramString.length() != 0) && 
/* 246 */       (!permits(paramSet, paramString, paramAlgorithmParameters))) {
/* 247 */       return false;
/*     */     }
/*     */ 
/* 252 */     if (!permits(paramSet, paramKey.getAlgorithm(), null)) {
/* 253 */       return false;
/*     */     }
/*     */ 
/* 257 */     if (this.keySizeConstraints.disables(paramKey)) {
/* 258 */       return false;
/*     */     }
/*     */ 
/* 261 */     return true;
/*     */   }
/*     */ 
/*     */   private static void loadDisabledAlgorithmsMap(String paramString)
/*     */   {
/* 268 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public String run() {
/* 271 */         return Security.getProperty(this.val$propertyName);
/*     */       }
/*     */     });
/* 275 */     String[] arrayOfString = null;
/*     */ 
/* 277 */     if ((str != null) && (!str.isEmpty()))
/*     */     {
/* 280 */       if ((str.charAt(0) == '"') && (str.charAt(str.length() - 1) == '"'))
/*     */       {
/* 282 */         str = str.substring(1, str.length() - 1);
/*     */       }
/*     */ 
/* 285 */       arrayOfString = str.split(",");
/* 286 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 287 */         arrayOfString[i] = arrayOfString[i].trim();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 292 */     if (arrayOfString == null) {
/* 293 */       arrayOfString = new String[0];
/*     */     }
/* 295 */     disabledAlgorithmsMap.put(paramString, arrayOfString);
/*     */ 
/* 298 */     KeySizeConstraints localKeySizeConstraints = new KeySizeConstraints(arrayOfString);
/*     */ 
/* 300 */     keySizeConstraintsMap.put(paramString, localKeySizeConstraints);
/*     */   }
/*     */ 
/*     */   private static class KeySizeConstraint
/*     */   {
/*     */     private int minSize;
/*     */     private int maxSize;
/* 403 */     private int prohibitedSize = -1;
/*     */ 
/*     */     public KeySizeConstraint(Operator paramOperator, int paramInt) {
/* 406 */       switch (DisabledAlgorithmConstraints.2.$SwitchMap$sun$security$util$DisabledAlgorithmConstraints$KeySizeConstraint$Operator[paramOperator.ordinal()]) {
/*     */       case 1:
/* 408 */         this.minSize = 0;
/* 409 */         this.maxSize = 2147483647;
/* 410 */         this.prohibitedSize = paramInt;
/* 411 */         break;
/*     */       case 2:
/* 413 */         this.minSize = paramInt;
/* 414 */         this.maxSize = paramInt;
/* 415 */         break;
/*     */       case 3:
/* 417 */         this.minSize = paramInt;
/* 418 */         this.maxSize = 2147483647;
/* 419 */         break;
/*     */       case 4:
/* 421 */         this.minSize = (paramInt + 1);
/* 422 */         this.maxSize = 2147483647;
/* 423 */         break;
/*     */       case 5:
/* 425 */         this.minSize = 0;
/* 426 */         this.maxSize = paramInt;
/* 427 */         break;
/*     */       case 6:
/* 429 */         this.minSize = 0;
/* 430 */         this.maxSize = (paramInt > 1 ? paramInt - 1 : 0);
/* 431 */         break;
/*     */       default:
/* 434 */         this.minSize = 2147483647;
/* 435 */         this.maxSize = -1;
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean disables(Key paramKey)
/*     */     {
/* 441 */       int i = KeyUtil.getKeySize(paramKey);
/*     */ 
/* 443 */       if (i == 0)
/* 444 */         return true;
/* 445 */       if (i > 0) {
/* 446 */         return (i < this.minSize) || (i > this.maxSize) || (this.prohibitedSize == i);
/*     */       }
/*     */ 
/* 451 */       return false;
/*     */     }
/*     */ 
/*     */     static enum Operator
/*     */     {
/* 373 */       EQ, 
/* 374 */       NE, 
/* 375 */       LT, 
/* 376 */       LE, 
/* 377 */       GT, 
/* 378 */       GE;
/*     */ 
/*     */       static Operator of(String paramString) {
/* 381 */         switch (paramString) {
/*     */         case "==":
/* 383 */           return EQ;
/*     */         case "!=":
/* 385 */           return NE;
/*     */         case "<":
/* 387 */           return LT;
/*     */         case "<=":
/* 389 */           return LE;
/*     */         case ">":
/* 391 */           return GT;
/*     */         case ">=":
/* 393 */           return GE;
/*     */         }
/*     */ 
/* 396 */         throw new IllegalArgumentException(paramString + " is not a legal Operator");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class KeySizeConstraints
/*     */   {
/* 307 */     private static final Pattern pattern = Pattern.compile("(\\S+)\\s+keySize\\s*(<=|<|==|!=|>|>=)\\s*(\\d+)");
/*     */ 
/* 310 */     private Map<String, Set<DisabledAlgorithmConstraints.KeySizeConstraint>> constraintsMap = Collections.synchronizedMap(new HashMap());
/*     */ 
/*     */     public KeySizeConstraints(String[] paramArrayOfString)
/*     */     {
/* 315 */       for (String str1 : paramArrayOfString)
/* 316 */         if ((str1 != null) && (!str1.isEmpty()))
/*     */         {
/* 320 */           Matcher localMatcher = pattern.matcher(str1);
/* 321 */           if (localMatcher.matches()) {
/* 322 */             String str2 = localMatcher.group(1);
/*     */ 
/* 324 */             DisabledAlgorithmConstraints.KeySizeConstraint.Operator localOperator = DisabledAlgorithmConstraints.KeySizeConstraint.Operator.of(localMatcher.group(2));
/*     */ 
/* 326 */             int k = Integer.parseInt(localMatcher.group(3));
/*     */ 
/* 328 */             str2 = str2.toLowerCase(Locale.ENGLISH);
/*     */ 
/* 330 */             synchronized (this.constraintsMap) {
/* 331 */               if (!this.constraintsMap.containsKey(str2)) {
/* 332 */                 this.constraintsMap.put(str2, new HashSet());
/*     */               }
/*     */ 
/* 336 */               Set localSet = (Set)this.constraintsMap.get(str2);
/*     */ 
/* 338 */               DisabledAlgorithmConstraints.KeySizeConstraint localKeySizeConstraint = new DisabledAlgorithmConstraints.KeySizeConstraint(localOperator, k);
/*     */ 
/* 340 */               localSet.add(localKeySizeConstraint);
/*     */             }
/*     */           }
/*     */         }
/*     */     }
/*     */ 
/*     */     public boolean disables(Key paramKey)
/*     */     {
/* 348 */       String str = paramKey.getAlgorithm().toLowerCase(Locale.ENGLISH);
/* 349 */       synchronized (this.constraintsMap) {
/* 350 */         if (this.constraintsMap.containsKey(str)) {
/* 351 */           Set localSet = (Set)this.constraintsMap.get(str);
/*     */ 
/* 353 */           for (DisabledAlgorithmConstraints.KeySizeConstraint localKeySizeConstraint : localSet) {
/* 354 */             if (localKeySizeConstraint.disables(paramKey)) {
/* 355 */               return true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 361 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.DisabledAlgorithmConstraints
 * JD-Core Version:    0.6.2
 */