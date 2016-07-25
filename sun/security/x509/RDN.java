/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class RDN
/*     */ {
/*     */   final AVA[] assertion;
/*     */   private volatile List<AVA> avaList;
/*     */   private volatile String canonicalString;
/*     */ 
/*     */   public RDN(String paramString)
/*     */     throws IOException
/*     */   {
/*  92 */     this(paramString, Collections.emptyMap());
/*     */   }
/*     */ 
/*     */   public RDN(String paramString, Map<String, String> paramMap)
/*     */     throws IOException
/*     */   {
/* 108 */     int i = 0;
/* 109 */     int j = 0;
/* 110 */     int k = 0;
/* 111 */     ArrayList localArrayList = new ArrayList(3);
/* 112 */     int m = paramString.indexOf('+');
/* 113 */     while (m >= 0) {
/* 114 */       i += X500Name.countQuotes(paramString, j, m);
/*     */ 
/* 122 */       if ((m > 0) && (paramString.charAt(m - 1) != '\\') && (i != 1))
/*     */       {
/* 127 */         str = paramString.substring(k, m);
/* 128 */         if (str.length() == 0) {
/* 129 */           throw new IOException("empty AVA in RDN \"" + paramString + "\"");
/*     */         }
/*     */ 
/* 133 */         localAVA = new AVA(new StringReader(str), paramMap);
/* 134 */         localArrayList.add(localAVA);
/*     */ 
/* 137 */         k = m + 1;
/*     */ 
/* 140 */         i = 0;
/*     */       }
/* 142 */       j = m + 1;
/* 143 */       m = paramString.indexOf('+', j);
/*     */     }
/*     */ 
/* 147 */     String str = paramString.substring(k);
/* 148 */     if (str.length() == 0) {
/* 149 */       throw new IOException("empty AVA in RDN \"" + paramString + "\"");
/*     */     }
/* 151 */     AVA localAVA = new AVA(new StringReader(str), paramMap);
/* 152 */     localArrayList.add(localAVA);
/*     */ 
/* 154 */     this.assertion = ((AVA[])localArrayList.toArray(new AVA[localArrayList.size()]));
/*     */   }
/*     */ 
/*     */   RDN(String paramString1, String paramString2)
/*     */     throws IOException
/*     */   {
/* 169 */     this(paramString1, paramString2, Collections.emptyMap());
/*     */   }
/*     */ 
/*     */   RDN(String paramString1, String paramString2, Map<String, String> paramMap)
/*     */     throws IOException
/*     */   {
/* 186 */     if (!paramString2.equalsIgnoreCase("RFC2253")) {
/* 187 */       throw new IOException("Unsupported format " + paramString2);
/*     */     }
/* 189 */     int i = 0;
/* 190 */     int j = 0;
/* 191 */     ArrayList localArrayList = new ArrayList(3);
/* 192 */     int k = paramString1.indexOf('+');
/* 193 */     while (k >= 0)
/*     */     {
/* 201 */       if ((k > 0) && (paramString1.charAt(k - 1) != '\\'))
/*     */       {
/* 205 */         str = paramString1.substring(j, k);
/* 206 */         if (str.length() == 0) {
/* 207 */           throw new IOException("empty AVA in RDN \"" + paramString1 + "\"");
/*     */         }
/*     */ 
/* 211 */         localAVA = new AVA(new StringReader(str), 3, paramMap);
/*     */ 
/* 213 */         localArrayList.add(localAVA);
/*     */ 
/* 216 */         j = k + 1;
/*     */       }
/* 218 */       i = k + 1;
/* 219 */       k = paramString1.indexOf('+', i);
/*     */     }
/*     */ 
/* 223 */     String str = paramString1.substring(j);
/* 224 */     if (str.length() == 0) {
/* 225 */       throw new IOException("empty AVA in RDN \"" + paramString1 + "\"");
/*     */     }
/* 227 */     AVA localAVA = new AVA(new StringReader(str), 3, paramMap);
/* 228 */     localArrayList.add(localAVA);
/*     */ 
/* 230 */     this.assertion = ((AVA[])localArrayList.toArray(new AVA[localArrayList.size()]));
/*     */   }
/*     */ 
/*     */   RDN(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/* 241 */     if (paramDerValue.tag != 49) {
/* 242 */       throw new IOException("X500 RDN");
/*     */     }
/* 244 */     DerInputStream localDerInputStream = new DerInputStream(paramDerValue.toByteArray());
/* 245 */     DerValue[] arrayOfDerValue = localDerInputStream.getSet(5);
/*     */ 
/* 247 */     this.assertion = new AVA[arrayOfDerValue.length];
/* 248 */     for (int i = 0; i < arrayOfDerValue.length; i++)
/* 249 */       this.assertion[i] = new AVA(arrayOfDerValue[i]);
/*     */   }
/*     */ 
/*     */   RDN(int paramInt)
/*     */   {
/* 259 */     this.assertion = new AVA[paramInt];
/*     */   }
/*     */   public RDN(AVA paramAVA) {
/* 262 */     if (paramAVA == null) {
/* 263 */       throw new NullPointerException();
/*     */     }
/* 265 */     this.assertion = new AVA[] { paramAVA };
/*     */   }
/*     */ 
/*     */   public RDN(AVA[] paramArrayOfAVA) {
/* 269 */     this.assertion = ((AVA[])paramArrayOfAVA.clone());
/* 270 */     for (int i = 0; i < this.assertion.length; i++)
/* 271 */       if (this.assertion[i] == null)
/* 272 */         throw new NullPointerException();
/*     */   }
/*     */ 
/*     */   public List<AVA> avas()
/*     */   {
/* 281 */     List localList = this.avaList;
/* 282 */     if (localList == null) {
/* 283 */       localList = Collections.unmodifiableList(Arrays.asList(this.assertion));
/* 284 */       this.avaList = localList;
/*     */     }
/* 286 */     return localList;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 293 */     return this.assertion.length;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 297 */     if (this == paramObject) {
/* 298 */       return true;
/*     */     }
/* 300 */     if (!(paramObject instanceof RDN)) {
/* 301 */       return false;
/*     */     }
/* 303 */     RDN localRDN = (RDN)paramObject;
/* 304 */     if (this.assertion.length != localRDN.assertion.length) {
/* 305 */       return false;
/*     */     }
/* 307 */     String str1 = toRFC2253String(true);
/* 308 */     String str2 = localRDN.toRFC2253String(true);
/* 309 */     return str1.equals(str2);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 319 */     return toRFC2253String(true).hashCode();
/*     */   }
/*     */ 
/*     */   DerValue findAttribute(ObjectIdentifier paramObjectIdentifier)
/*     */   {
/* 329 */     for (int i = 0; i < this.assertion.length; i++) {
/* 330 */       if (this.assertion[i].oid.equals(paramObjectIdentifier)) {
/* 331 */         return this.assertion[i].value;
/*     */       }
/*     */     }
/* 334 */     return null;
/*     */   }
/*     */ 
/*     */   void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 344 */     paramDerOutputStream.putOrderedSetOf((byte)49, this.assertion);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 353 */     if (this.assertion.length == 1) {
/* 354 */       return this.assertion[0].toString();
/*     */     }
/*     */ 
/* 357 */     StringBuilder localStringBuilder = new StringBuilder();
/* 358 */     for (int i = 0; i < this.assertion.length; i++) {
/* 359 */       if (i != 0) {
/* 360 */         localStringBuilder.append(" + ");
/*     */       }
/* 362 */       localStringBuilder.append(this.assertion[i].toString());
/*     */     }
/* 364 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String toRFC1779String()
/*     */   {
/* 372 */     return toRFC1779String(Collections.emptyMap());
/*     */   }
/*     */ 
/*     */   public String toRFC1779String(Map<String, String> paramMap)
/*     */   {
/* 381 */     if (this.assertion.length == 1) {
/* 382 */       return this.assertion[0].toRFC1779String(paramMap);
/*     */     }
/*     */ 
/* 385 */     StringBuilder localStringBuilder = new StringBuilder();
/* 386 */     for (int i = 0; i < this.assertion.length; i++) {
/* 387 */       if (i != 0) {
/* 388 */         localStringBuilder.append(" + ");
/*     */       }
/* 390 */       localStringBuilder.append(this.assertion[i].toRFC1779String(paramMap));
/*     */     }
/* 392 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String toRFC2253String()
/*     */   {
/* 400 */     return toRFC2253StringInternal(false, Collections.emptyMap());
/*     */   }
/*     */ 
/*     */   public String toRFC2253String(Map<String, String> paramMap)
/*     */   {
/* 410 */     return toRFC2253StringInternal(false, paramMap);
/*     */   }
/*     */ 
/*     */   public String toRFC2253String(boolean paramBoolean)
/*     */   {
/* 420 */     if (!paramBoolean) {
/* 421 */       return toRFC2253StringInternal(false, Collections.emptyMap());
/*     */     }
/*     */ 
/* 424 */     String str = this.canonicalString;
/* 425 */     if (str == null) {
/* 426 */       str = toRFC2253StringInternal(true, Collections.emptyMap());
/*     */ 
/* 428 */       this.canonicalString = str;
/*     */     }
/* 430 */     return str;
/*     */   }
/*     */ 
/*     */   private String toRFC2253StringInternal(boolean paramBoolean, Map<String, String> paramMap)
/*     */   {
/* 446 */     if (this.assertion.length == 1) {
/* 447 */       return paramBoolean ? this.assertion[0].toRFC2253CanonicalString() : this.assertion[0].toRFC2253String(paramMap);
/*     */     }
/*     */ 
/* 451 */     StringBuilder localStringBuilder = new StringBuilder();
/* 452 */     if (!paramBoolean) {
/* 453 */       for (int i = 0; i < this.assertion.length; i++) {
/* 454 */         if (i > 0) {
/* 455 */           localStringBuilder.append('+');
/*     */         }
/* 457 */         localStringBuilder.append(this.assertion[i].toRFC2253String(paramMap));
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 462 */       ArrayList localArrayList = new ArrayList(this.assertion.length);
/* 463 */       for (int j = 0; j < this.assertion.length; j++) {
/* 464 */         localArrayList.add(this.assertion[j]);
/*     */       }
/* 466 */       Collections.sort(localArrayList, AVAComparator.getInstance());
/*     */ 
/* 468 */       for (j = 0; j < localArrayList.size(); j++) {
/* 469 */         if (j > 0) {
/* 470 */           localStringBuilder.append('+');
/*     */         }
/* 472 */         localStringBuilder.append(((AVA)localArrayList.get(j)).toRFC2253CanonicalString());
/*     */       }
/*     */     }
/* 475 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.RDN
 * JD-Core Version:    0.6.2
 */