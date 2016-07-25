/*     */ package sun.security.jgss;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.ietf.jgss.GSSCredential;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.jgss.spi.GSSCredentialSpi;
/*     */ import sun.security.jgss.spi.GSSNameSpi;
/*     */ 
/*     */ public class GSSCredentialImpl
/*     */   implements GSSCredential
/*     */ {
/*  34 */   private GSSManagerImpl gssManager = null;
/*  35 */   private boolean destroyed = false;
/*     */ 
/*  43 */   private Hashtable<SearchKey, GSSCredentialSpi> hashtable = null;
/*     */ 
/*  46 */   private GSSCredentialSpi tempCred = null;
/*     */ 
/*     */   GSSCredentialImpl(GSSManagerImpl paramGSSManagerImpl, int paramInt) throws GSSException
/*     */   {
/*  50 */     this(paramGSSManagerImpl, null, 0, (Oid[])null, paramInt);
/*     */   }
/*     */ 
/*     */   GSSCredentialImpl(GSSManagerImpl paramGSSManagerImpl, GSSName paramGSSName, int paramInt1, Oid paramOid, int paramInt2)
/*     */     throws GSSException
/*     */   {
/*  57 */     if (paramOid == null) paramOid = ProviderList.DEFAULT_MECH_OID;
/*     */ 
/*  59 */     init(paramGSSManagerImpl);
/*  60 */     add(paramGSSName, paramInt1, paramInt1, paramOid, paramInt2);
/*     */   }
/*     */ 
/*     */   GSSCredentialImpl(GSSManagerImpl paramGSSManagerImpl, GSSName paramGSSName, int paramInt1, Oid[] paramArrayOfOid, int paramInt2)
/*     */     throws GSSException
/*     */   {
/*  66 */     init(paramGSSManagerImpl);
/*  67 */     int i = 0;
/*  68 */     if (paramArrayOfOid == null) {
/*  69 */       paramArrayOfOid = paramGSSManagerImpl.getMechs();
/*  70 */       i = 1;
/*     */     }
/*     */ 
/*  73 */     for (int j = 0; j < paramArrayOfOid.length; j++) {
/*     */       try {
/*  75 */         add(paramGSSName, paramInt1, paramInt1, paramArrayOfOid[j], paramInt2);
/*     */       } catch (GSSException localGSSException) {
/*  77 */         if (i != 0)
/*     */         {
/*  79 */           GSSUtil.debug("Ignore " + localGSSException + " while acquring cred for " + paramArrayOfOid[j]);
/*     */         }
/*     */         else
/*  82 */           throw localGSSException;
/*     */       }
/*     */     }
/*  85 */     if ((this.hashtable.size() == 0) || (paramInt2 != getUsage()))
/*  86 */       throw new GSSException(13);
/*     */   }
/*     */ 
/*     */   public GSSCredentialImpl(GSSManagerImpl paramGSSManagerImpl, GSSCredentialSpi paramGSSCredentialSpi)
/*     */     throws GSSException
/*     */   {
/*  92 */     init(paramGSSManagerImpl);
/*  93 */     int i = 2;
/*  94 */     if (paramGSSCredentialSpi.isInitiatorCredential()) {
/*  95 */       if (paramGSSCredentialSpi.isAcceptorCredential())
/*  96 */         i = 0;
/*     */       else {
/*  98 */         i = 1;
/*     */       }
/*     */     }
/* 101 */     SearchKey localSearchKey = new SearchKey(paramGSSCredentialSpi.getMechanism(), i);
/*     */ 
/* 103 */     this.tempCred = paramGSSCredentialSpi;
/* 104 */     this.hashtable.put(localSearchKey, this.tempCred);
/*     */   }
/*     */ 
/*     */   void init(GSSManagerImpl paramGSSManagerImpl) {
/* 108 */     this.gssManager = paramGSSManagerImpl;
/* 109 */     this.hashtable = new Hashtable(paramGSSManagerImpl.getMechs().length);
/*     */   }
/*     */ 
/*     */   public void dispose() throws GSSException
/*     */   {
/* 114 */     if (!this.destroyed)
/*     */     {
/* 116 */       Enumeration localEnumeration = this.hashtable.elements();
/* 117 */       while (localEnumeration.hasMoreElements()) {
/* 118 */         GSSCredentialSpi localGSSCredentialSpi = (GSSCredentialSpi)localEnumeration.nextElement();
/* 119 */         localGSSCredentialSpi.dispose();
/*     */       }
/* 121 */       this.destroyed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public GSSName getName() throws GSSException {
/* 126 */     if (this.destroyed) {
/* 127 */       throw new IllegalStateException("This credential is no longer valid");
/*     */     }
/*     */ 
/* 130 */     return GSSNameImpl.wrapElement(this.gssManager, this.tempCred.getName());
/*     */   }
/*     */ 
/*     */   public GSSName getName(Oid paramOid) throws GSSException
/*     */   {
/* 135 */     if (this.destroyed) {
/* 136 */       throw new IllegalStateException("This credential is no longer valid");
/*     */     }
/*     */ 
/* 140 */     SearchKey localSearchKey = null;
/* 141 */     GSSCredentialSpi localGSSCredentialSpi = null;
/*     */ 
/* 143 */     if (paramOid == null) paramOid = ProviderList.DEFAULT_MECH_OID;
/*     */ 
/* 145 */     localSearchKey = new SearchKey(paramOid, 1);
/* 146 */     localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/*     */ 
/* 148 */     if (localGSSCredentialSpi == null) {
/* 149 */       localSearchKey = new SearchKey(paramOid, 2);
/* 150 */       localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/*     */     }
/*     */ 
/* 153 */     if (localGSSCredentialSpi == null) {
/* 154 */       localSearchKey = new SearchKey(paramOid, 0);
/* 155 */       localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/*     */     }
/*     */ 
/* 158 */     if (localGSSCredentialSpi == null) {
/* 159 */       throw new GSSExceptionImpl(2, paramOid);
/*     */     }
/*     */ 
/* 162 */     return GSSNameImpl.wrapElement(this.gssManager, localGSSCredentialSpi.getName());
/*     */   }
/*     */ 
/*     */   public int getRemainingLifetime()
/*     */     throws GSSException
/*     */   {
/* 174 */     if (this.destroyed) {
/* 175 */       throw new IllegalStateException("This credential is no longer valid");
/*     */     }
/*     */ 
/* 181 */     int i = 0; int j = 0; int k = 0;
/* 182 */     int m = 2147483647;
/*     */ 
/* 184 */     Enumeration localEnumeration = this.hashtable.keys();
/* 185 */     while (localEnumeration.hasMoreElements()) {
/* 186 */       SearchKey localSearchKey = (SearchKey)localEnumeration.nextElement();
/* 187 */       GSSCredentialSpi localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/* 188 */       if (localSearchKey.getUsage() == 1) {
/* 189 */         i = localGSSCredentialSpi.getInitLifetime();
/* 190 */       } else if (localSearchKey.getUsage() == 2) {
/* 191 */         i = localGSSCredentialSpi.getAcceptLifetime();
/*     */       } else {
/* 193 */         j = localGSSCredentialSpi.getInitLifetime();
/* 194 */         k = localGSSCredentialSpi.getAcceptLifetime();
/* 195 */         i = j < k ? j : k;
/*     */       }
/*     */ 
/* 199 */       if (m > i) {
/* 200 */         m = i;
/*     */       }
/*     */     }
/* 203 */     return m;
/*     */   }
/*     */ 
/*     */   public int getRemainingInitLifetime(Oid paramOid) throws GSSException
/*     */   {
/* 208 */     if (this.destroyed) {
/* 209 */       throw new IllegalStateException("This credential is no longer valid");
/*     */     }
/*     */ 
/* 213 */     GSSCredentialSpi localGSSCredentialSpi = null;
/* 214 */     SearchKey localSearchKey = null;
/* 215 */     int i = 0;
/* 216 */     int j = 0;
/*     */ 
/* 218 */     if (paramOid == null) paramOid = ProviderList.DEFAULT_MECH_OID;
/*     */ 
/* 220 */     localSearchKey = new SearchKey(paramOid, 1);
/* 221 */     localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/*     */ 
/* 223 */     if (localGSSCredentialSpi != null) {
/* 224 */       i = 1;
/* 225 */       if (j < localGSSCredentialSpi.getInitLifetime()) {
/* 226 */         j = localGSSCredentialSpi.getInitLifetime();
/*     */       }
/*     */     }
/* 229 */     localSearchKey = new SearchKey(paramOid, 0);
/* 230 */     localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/*     */ 
/* 232 */     if (localGSSCredentialSpi != null) {
/* 233 */       i = 1;
/* 234 */       if (j < localGSSCredentialSpi.getInitLifetime()) {
/* 235 */         j = localGSSCredentialSpi.getInitLifetime();
/*     */       }
/*     */     }
/* 238 */     if (i == 0) {
/* 239 */       throw new GSSExceptionImpl(2, paramOid);
/*     */     }
/*     */ 
/* 242 */     return j;
/*     */   }
/*     */ 
/*     */   public int getRemainingAcceptLifetime(Oid paramOid)
/*     */     throws GSSException
/*     */   {
/* 248 */     if (this.destroyed) {
/* 249 */       throw new IllegalStateException("This credential is no longer valid");
/*     */     }
/*     */ 
/* 253 */     GSSCredentialSpi localGSSCredentialSpi = null;
/* 254 */     SearchKey localSearchKey = null;
/* 255 */     int i = 0;
/* 256 */     int j = 0;
/*     */ 
/* 258 */     if (paramOid == null) paramOid = ProviderList.DEFAULT_MECH_OID;
/*     */ 
/* 260 */     localSearchKey = new SearchKey(paramOid, 2);
/* 261 */     localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/*     */ 
/* 263 */     if (localGSSCredentialSpi != null) {
/* 264 */       i = 1;
/* 265 */       if (j < localGSSCredentialSpi.getAcceptLifetime()) {
/* 266 */         j = localGSSCredentialSpi.getAcceptLifetime();
/*     */       }
/*     */     }
/* 269 */     localSearchKey = new SearchKey(paramOid, 0);
/* 270 */     localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/*     */ 
/* 272 */     if (localGSSCredentialSpi != null) {
/* 273 */       i = 1;
/* 274 */       if (j < localGSSCredentialSpi.getAcceptLifetime()) {
/* 275 */         j = localGSSCredentialSpi.getAcceptLifetime();
/*     */       }
/*     */     }
/* 278 */     if (i == 0) {
/* 279 */       throw new GSSExceptionImpl(2, paramOid);
/*     */     }
/*     */ 
/* 282 */     return j;
/*     */   }
/*     */ 
/*     */   public int getUsage()
/*     */     throws GSSException
/*     */   {
/* 294 */     if (this.destroyed) {
/* 295 */       throw new IllegalStateException("This credential is no longer valid");
/*     */     }
/*     */ 
/* 300 */     int i = 0;
/* 301 */     int j = 0;
/*     */ 
/* 303 */     Enumeration localEnumeration = this.hashtable.keys();
/* 304 */     while (localEnumeration.hasMoreElements()) {
/* 305 */       SearchKey localSearchKey = (SearchKey)localEnumeration.nextElement();
/* 306 */       if (localSearchKey.getUsage() == 1)
/* 307 */         i = 1;
/* 308 */       else if (localSearchKey.getUsage() == 2)
/* 309 */         j = 1;
/*     */       else
/* 311 */         return 0;
/*     */     }
/* 313 */     if (i != 0) {
/* 314 */       if (j != 0) {
/* 315 */         return 0;
/*     */       }
/* 317 */       return 1;
/*     */     }
/* 319 */     return 2;
/*     */   }
/*     */ 
/*     */   public int getUsage(Oid paramOid) throws GSSException
/*     */   {
/* 324 */     if (this.destroyed) {
/* 325 */       throw new IllegalStateException("This credential is no longer valid");
/*     */     }
/*     */ 
/* 329 */     GSSCredentialSpi localGSSCredentialSpi = null;
/* 330 */     SearchKey localSearchKey = null;
/* 331 */     int i = 0;
/* 332 */     int j = 0;
/*     */ 
/* 334 */     if (paramOid == null) paramOid = ProviderList.DEFAULT_MECH_OID;
/*     */ 
/* 336 */     localSearchKey = new SearchKey(paramOid, 1);
/* 337 */     localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/*     */ 
/* 339 */     if (localGSSCredentialSpi != null) {
/* 340 */       i = 1;
/*     */     }
/*     */ 
/* 343 */     localSearchKey = new SearchKey(paramOid, 2);
/* 344 */     localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/*     */ 
/* 346 */     if (localGSSCredentialSpi != null) {
/* 347 */       j = 1;
/*     */     }
/*     */ 
/* 350 */     localSearchKey = new SearchKey(paramOid, 0);
/* 351 */     localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/*     */ 
/* 353 */     if (localGSSCredentialSpi != null) {
/* 354 */       i = 1;
/* 355 */       j = 1;
/*     */     }
/*     */ 
/* 358 */     if ((i != 0) && (j != 0))
/* 359 */       return 0;
/* 360 */     if (i != 0)
/* 361 */       return 1;
/* 362 */     if (j != 0) {
/* 363 */       return 2;
/*     */     }
/* 365 */     throw new GSSExceptionImpl(2, paramOid);
/*     */   }
/*     */ 
/*     */   public Oid[] getMechs()
/*     */     throws GSSException
/*     */   {
/* 371 */     if (this.destroyed) {
/* 372 */       throw new IllegalStateException("This credential is no longer valid");
/*     */     }
/*     */ 
/* 375 */     Vector localVector = new Vector(this.hashtable.size());
/*     */ 
/* 377 */     Enumeration localEnumeration = this.hashtable.keys();
/* 378 */     while (localEnumeration.hasMoreElements()) {
/* 379 */       SearchKey localSearchKey = (SearchKey)localEnumeration.nextElement();
/* 380 */       localVector.addElement(localSearchKey.getMech());
/*     */     }
/* 382 */     return (Oid[])localVector.toArray(new Oid[0]);
/*     */   }
/*     */ 
/*     */   public void add(GSSName paramGSSName, int paramInt1, int paramInt2, Oid paramOid, int paramInt3)
/*     */     throws GSSException
/*     */   {
/* 388 */     if (this.destroyed) {
/* 389 */       throw new IllegalStateException("This credential is no longer valid");
/*     */     }
/*     */ 
/* 392 */     if (paramOid == null) paramOid = ProviderList.DEFAULT_MECH_OID;
/*     */ 
/* 394 */     SearchKey localSearchKey = new SearchKey(paramOid, paramInt3);
/* 395 */     if (this.hashtable.containsKey(localSearchKey)) {
/* 396 */       throw new GSSExceptionImpl(17, "Duplicate element found: " + getElementStr(paramOid, paramInt3));
/*     */     }
/*     */ 
/* 403 */     GSSNameSpi localGSSNameSpi = paramGSSName == null ? null : ((GSSNameImpl)paramGSSName).getElement(paramOid);
/*     */ 
/* 406 */     this.tempCred = this.gssManager.getCredentialElement(localGSSNameSpi, paramInt1, paramInt2, paramOid, paramInt3);
/*     */ 
/* 425 */     if (this.tempCred != null)
/* 426 */       if ((paramInt3 == 0) && ((!this.tempCred.isAcceptorCredential()) || (!this.tempCred.isInitiatorCredential())))
/*     */       {
/*     */         int i;
/*     */         int j;
/* 433 */         if (!this.tempCred.isInitiatorCredential()) {
/* 434 */           i = 2;
/* 435 */           j = 1;
/*     */         } else {
/* 437 */           i = 1;
/* 438 */           j = 2;
/*     */         }
/*     */ 
/* 441 */         localSearchKey = new SearchKey(paramOid, i);
/* 442 */         this.hashtable.put(localSearchKey, this.tempCred);
/*     */ 
/* 444 */         this.tempCred = this.gssManager.getCredentialElement(localGSSNameSpi, paramInt1, paramInt2, paramOid, j);
/*     */ 
/* 450 */         localSearchKey = new SearchKey(paramOid, j);
/* 451 */         this.hashtable.put(localSearchKey, this.tempCred);
/*     */       } else {
/* 453 */         this.hashtable.put(localSearchKey, this.tempCred);
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 460 */     if (this.destroyed) {
/* 461 */       throw new IllegalStateException("This credential is no longer valid");
/*     */     }
/*     */ 
/* 465 */     if (this == paramObject) {
/* 466 */       return true;
/*     */     }
/*     */ 
/* 469 */     if (!(paramObject instanceof GSSCredentialImpl)) {
/* 470 */       return false;
/*     */     }
/*     */ 
/* 489 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 500 */     if (this.destroyed) {
/* 501 */       throw new IllegalStateException("This credential is no longer valid");
/*     */     }
/*     */ 
/* 511 */     return 1;
/*     */   }
/*     */ 
/*     */   public GSSCredentialSpi getElement(Oid paramOid, boolean paramBoolean)
/*     */     throws GSSException
/*     */   {
/* 527 */     if (this.destroyed)
/* 528 */       throw new IllegalStateException("This credential is no longer valid");
/*     */     SearchKey localSearchKey;
/*     */     GSSCredentialSpi localGSSCredentialSpi;
/* 535 */     if (paramOid == null)
/*     */     {
/* 540 */       paramOid = ProviderList.DEFAULT_MECH_OID;
/* 541 */       localSearchKey = new SearchKey(paramOid, paramBoolean ? 1 : 2);
/*     */ 
/* 543 */       localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/* 544 */       if (localGSSCredentialSpi == null) {
/* 545 */         localSearchKey = new SearchKey(paramOid, 0);
/* 546 */         localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/* 547 */         if (localGSSCredentialSpi == null)
/*     */         {
/* 552 */           Object[] arrayOfObject = this.hashtable.entrySet().toArray();
/* 553 */           for (int i = 0; i < arrayOfObject.length; i++) {
/* 554 */             localGSSCredentialSpi = (GSSCredentialSpi)((Map.Entry)arrayOfObject[i]).getValue();
/*     */ 
/* 556 */             if (localGSSCredentialSpi.isInitiatorCredential() == paramBoolean)
/*     */               break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 563 */       if (paramBoolean)
/* 564 */         localSearchKey = new SearchKey(paramOid, 1);
/*     */       else {
/* 566 */         localSearchKey = new SearchKey(paramOid, 2);
/*     */       }
/* 568 */       localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/*     */ 
/* 570 */       if (localGSSCredentialSpi == null) {
/* 571 */         localSearchKey = new SearchKey(paramOid, 0);
/* 572 */         localGSSCredentialSpi = (GSSCredentialSpi)this.hashtable.get(localSearchKey);
/*     */       }
/*     */     }
/*     */ 
/* 576 */     if (localGSSCredentialSpi == null) {
/* 577 */       throw new GSSExceptionImpl(13, "No credential found for: " + paramOid + getElementStr(paramOid, paramBoolean ? 1 : 2));
/*     */     }
/*     */ 
/* 581 */     return localGSSCredentialSpi;
/*     */   }
/*     */ 
/*     */   Set<GSSCredentialSpi> getElements() {
/* 585 */     HashSet localHashSet = new HashSet(this.hashtable.size());
/*     */ 
/* 587 */     Enumeration localEnumeration = this.hashtable.elements();
/* 588 */     while (localEnumeration.hasMoreElements()) {
/* 589 */       GSSCredentialSpi localGSSCredentialSpi = (GSSCredentialSpi)localEnumeration.nextElement();
/* 590 */       localHashSet.add(localGSSCredentialSpi);
/*     */     }
/* 592 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   private static String getElementStr(Oid paramOid, int paramInt) {
/* 596 */     String str = paramOid.toString();
/* 597 */     if (paramInt == 1) {
/* 598 */       str = str.concat(" usage: Initiate");
/*     */     }
/* 600 */     else if (paramInt == 2) {
/* 601 */       str = str.concat(" usage: Accept");
/*     */     }
/*     */     else {
/* 604 */       str = str.concat(" usage: Initiate and Accept");
/*     */     }
/*     */ 
/* 607 */     return str;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 612 */     if (this.destroyed) {
/* 613 */       throw new IllegalStateException("This credential is no longer valid");
/*     */     }
/*     */ 
/* 617 */     GSSCredentialSpi localGSSCredentialSpi = null;
/* 618 */     StringBuffer localStringBuffer = new StringBuffer("[GSSCredential: ");
/* 619 */     Object[] arrayOfObject = this.hashtable.entrySet().toArray();
/* 620 */     for (int i = 0; i < arrayOfObject.length; i++)
/*     */       try {
/* 622 */         localStringBuffer.append('\n');
/* 623 */         localGSSCredentialSpi = (GSSCredentialSpi)((Map.Entry)arrayOfObject[i]).getValue();
/*     */ 
/* 625 */         localStringBuffer.append(localGSSCredentialSpi.getName());
/* 626 */         localStringBuffer.append(' ');
/* 627 */         localStringBuffer.append(localGSSCredentialSpi.getMechanism());
/* 628 */         localStringBuffer.append(localGSSCredentialSpi.isInitiatorCredential() ? " Initiate" : "");
/*     */ 
/* 630 */         localStringBuffer.append(localGSSCredentialSpi.isAcceptorCredential() ? " Accept" : "");
/*     */ 
/* 632 */         localStringBuffer.append(" [");
/* 633 */         localStringBuffer.append(localGSSCredentialSpi.getClass());
/* 634 */         localStringBuffer.append(']');
/*     */       }
/*     */       catch (GSSException localGSSException)
/*     */       {
/*     */       }
/* 639 */     localStringBuffer.append(']');
/* 640 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static class SearchKey {
/* 644 */     private Oid mechOid = null;
/* 645 */     private int usage = 0;
/*     */ 
/*     */     public SearchKey(Oid paramOid, int paramInt) {
/* 648 */       this.mechOid = paramOid;
/* 649 */       this.usage = paramInt;
/*     */     }
/*     */     public Oid getMech() {
/* 652 */       return this.mechOid;
/*     */     }
/*     */     public int getUsage() {
/* 655 */       return this.usage;
/*     */     }
/*     */     public boolean equals(Object paramObject) {
/* 658 */       if (!(paramObject instanceof SearchKey))
/* 659 */         return false;
/* 660 */       SearchKey localSearchKey = (SearchKey)paramObject;
/* 661 */       return (this.mechOid.equals(localSearchKey.mechOid)) && (this.usage == localSearchKey.usage);
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 665 */       return this.mechOid.hashCode();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.GSSCredentialImpl
 * JD-Core Version:    0.6.2
 */