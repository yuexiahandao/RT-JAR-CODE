/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class GeneralSubtrees
/*     */   implements Cloneable
/*     */ {
/*     */   private final List<GeneralSubtree> trees;
/*     */   private static final int NAME_DIFF_TYPE = -1;
/*     */   private static final int NAME_MATCH = 0;
/*     */   private static final int NAME_NARROWS = 1;
/*     */   private static final int NAME_WIDENS = 2;
/*     */   private static final int NAME_SAME_TYPE = 3;
/*     */ 
/*     */   public GeneralSubtrees()
/*     */   {
/*  62 */     this.trees = new ArrayList();
/*     */   }
/*     */ 
/*     */   private GeneralSubtrees(GeneralSubtrees paramGeneralSubtrees) {
/*  66 */     this.trees = new ArrayList(paramGeneralSubtrees.trees);
/*     */   }
/*     */ 
/*     */   public GeneralSubtrees(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  75 */     this();
/*  76 */     if (paramDerValue.tag != 48) {
/*  77 */       throw new IOException("Invalid encoding of GeneralSubtrees.");
/*     */     }
/*  79 */     while (paramDerValue.data.available() != 0) {
/*  80 */       DerValue localDerValue = paramDerValue.data.getDerValue();
/*  81 */       GeneralSubtree localGeneralSubtree = new GeneralSubtree(localDerValue);
/*  82 */       add(localGeneralSubtree);
/*     */     }
/*     */   }
/*     */ 
/*     */   public GeneralSubtree get(int paramInt) {
/*  87 */     return (GeneralSubtree)this.trees.get(paramInt);
/*     */   }
/*     */ 
/*     */   public void remove(int paramInt) {
/*  91 */     this.trees.remove(paramInt);
/*     */   }
/*     */ 
/*     */   public void add(GeneralSubtree paramGeneralSubtree) {
/*  95 */     if (paramGeneralSubtree == null) {
/*  96 */       throw new NullPointerException();
/*     */     }
/*  98 */     this.trees.add(paramGeneralSubtree);
/*     */   }
/*     */ 
/*     */   public boolean contains(GeneralSubtree paramGeneralSubtree) {
/* 102 */     if (paramGeneralSubtree == null) {
/* 103 */       throw new NullPointerException();
/*     */     }
/* 105 */     return this.trees.contains(paramGeneralSubtree);
/*     */   }
/*     */ 
/*     */   public int size() {
/* 109 */     return this.trees.size();
/*     */   }
/*     */ 
/*     */   public Iterator<GeneralSubtree> iterator() {
/* 113 */     return this.trees.iterator();
/*     */   }
/*     */ 
/*     */   public List<GeneralSubtree> trees() {
/* 117 */     return this.trees;
/*     */   }
/*     */ 
/*     */   public Object clone() {
/* 121 */     return new GeneralSubtrees(this);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 128 */     String str = "   GeneralSubtrees:\n" + this.trees.toString() + "\n";
/* 129 */     return str;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 138 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*     */ 
/* 140 */     int i = 0; for (int j = size(); i < j; i++) {
/* 141 */       get(i).encode(localDerOutputStream);
/*     */     }
/* 143 */     paramDerOutputStream.write((byte)48, localDerOutputStream);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 154 */     if (this == paramObject) {
/* 155 */       return true;
/*     */     }
/* 157 */     if (!(paramObject instanceof GeneralSubtrees)) {
/* 158 */       return false;
/*     */     }
/* 160 */     GeneralSubtrees localGeneralSubtrees = (GeneralSubtrees)paramObject;
/* 161 */     return this.trees.equals(localGeneralSubtrees.trees);
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 165 */     return this.trees.hashCode();
/*     */   }
/*     */ 
/*     */   private GeneralNameInterface getGeneralNameInterface(int paramInt)
/*     */   {
/* 175 */     return getGeneralNameInterface(get(paramInt));
/*     */   }
/*     */ 
/*     */   private static GeneralNameInterface getGeneralNameInterface(GeneralSubtree paramGeneralSubtree) {
/* 179 */     GeneralName localGeneralName = paramGeneralSubtree.getName();
/* 180 */     GeneralNameInterface localGeneralNameInterface = localGeneralName.getName();
/* 181 */     return localGeneralNameInterface;
/*     */   }
/*     */ 
/*     */   private void minimize()
/*     */   {
/* 194 */     for (int i = 0; i < size(); i++) {
/* 195 */       GeneralNameInterface localGeneralNameInterface1 = getGeneralNameInterface(i);
/* 196 */       int j = 0;
/*     */ 
/* 199 */       for (int k = i + 1; k < size(); k++) {
/* 200 */         GeneralNameInterface localGeneralNameInterface2 = getGeneralNameInterface(k);
/* 201 */         switch (localGeneralNameInterface1.constrains(localGeneralNameInterface2))
/*     */         {
/*     */         case -1:
/* 204 */           break;
/*     */         case 0:
/* 207 */           j = 1;
/* 208 */           break;
/*     */         case 1:
/* 212 */           remove(k);
/* 213 */           k--;
/* 214 */           break;
/*     */         case 2:
/* 218 */           j = 1;
/* 219 */           break;
/*     */         case 3:
/* 222 */           break;
/*     */         }
/* 224 */         break;
/*     */       }
/*     */ 
/* 227 */       if (j != 0) {
/* 228 */         remove(i);
/* 229 */         i--;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private GeneralSubtree createWidestSubtree(GeneralNameInterface paramGeneralNameInterface)
/*     */   {
/*     */     try
/*     */     {
/*     */       GeneralName localGeneralName;
/* 246 */       switch (paramGeneralNameInterface.getType())
/*     */       {
/*     */       case 0:
/* 250 */         ObjectIdentifier localObjectIdentifier = ((OtherName)paramGeneralNameInterface).getOID();
/* 251 */         localGeneralName = new GeneralName(new OtherName(localObjectIdentifier, null));
/* 252 */         break;
/*     */       case 1:
/* 254 */         localGeneralName = new GeneralName(new RFC822Name(""));
/* 255 */         break;
/*     */       case 2:
/* 257 */         localGeneralName = new GeneralName(new DNSName(""));
/* 258 */         break;
/*     */       case 3:
/* 260 */         localGeneralName = new GeneralName(new X400Address((byte[])null));
/* 261 */         break;
/*     */       case 4:
/* 263 */         localGeneralName = new GeneralName(new X500Name(""));
/* 264 */         break;
/*     */       case 5:
/* 266 */         localGeneralName = new GeneralName(new EDIPartyName(""));
/* 267 */         break;
/*     */       case 6:
/* 269 */         localGeneralName = new GeneralName(new URIName(""));
/* 270 */         break;
/*     */       case 7:
/* 272 */         localGeneralName = new GeneralName(new IPAddressName((byte[])null));
/* 273 */         break;
/*     */       case 8:
/* 275 */         localGeneralName = new GeneralName(new OIDName(new ObjectIdentifier((int[])null)));
/*     */ 
/* 277 */         break;
/*     */       default:
/* 279 */         throw new IOException("Unsupported GeneralNameInterface type: " + paramGeneralNameInterface.getType());
/*     */       }
/*     */ 
/* 282 */       return new GeneralSubtree(localGeneralName, 0, -1);
/*     */     } catch (IOException localIOException) {
/* 284 */       throw new RuntimeException("Unexpected error: " + localIOException, localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public GeneralSubtrees intersect(GeneralSubtrees paramGeneralSubtrees)
/*     */   {
/* 322 */     if (paramGeneralSubtrees == null) {
/* 323 */       throw new NullPointerException("other GeneralSubtrees must not be null");
/*     */     }
/*     */ 
/* 326 */     GeneralSubtrees localGeneralSubtrees1 = new GeneralSubtrees();
/* 327 */     GeneralSubtrees localGeneralSubtrees2 = null;
/*     */ 
/* 331 */     if (size() == 0) {
/* 332 */       union(paramGeneralSubtrees);
/* 333 */       return null;
/*     */     }
/*     */ 
/* 339 */     minimize();
/* 340 */     paramGeneralSubtrees.minimize();
/*     */     Object localObject1;
/*     */     int k;
/*     */     int m;
/*     */     Object localObject2;
/* 350 */     for (int i = 0; i < size(); i++) {
/* 351 */       localObject1 = getGeneralNameInterface(i);
/* 352 */       int j = 0;
/*     */ 
/* 359 */       k = 0;
/*     */       GeneralNameInterface localGeneralNameInterface2;
/* 360 */       for (m = 0; m < paramGeneralSubtrees.size(); m++) {
/* 361 */         GeneralSubtree localGeneralSubtree = paramGeneralSubtrees.get(m);
/* 362 */         localGeneralNameInterface2 = getGeneralNameInterface(localGeneralSubtree);
/*     */ 
/* 364 */         switch (((GeneralNameInterface)localObject1).constrains(localGeneralNameInterface2)) {
/*     */         case 1:
/* 366 */           remove(i);
/* 367 */           i--;
/* 368 */           localGeneralSubtrees1.add(localGeneralSubtree);
/* 369 */           k = 0;
/* 370 */           break;
/*     */         case 3:
/* 372 */           k = 1;
/* 373 */           break;
/*     */         case 0:
/*     */         case 2:
/* 376 */           k = 0;
/* 377 */           break;
/*     */         case -1:
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 389 */       if (k != 0)
/*     */       {
/* 395 */         m = 0;
/* 396 */         for (int n = 0; n < size(); n++) {
/* 397 */           localGeneralNameInterface2 = getGeneralNameInterface(n);
/*     */ 
/* 399 */           if (localGeneralNameInterface2.getType() == ((GeneralNameInterface)localObject1).getType()) {
/* 400 */             for (int i1 = 0; i1 < paramGeneralSubtrees.size(); i1++) {
/* 401 */               GeneralNameInterface localGeneralNameInterface3 = paramGeneralSubtrees.getGeneralNameInterface(i1);
/*     */ 
/* 404 */               int i2 = localGeneralNameInterface2.constrains(localGeneralNameInterface3);
/*     */ 
/* 406 */               if ((i2 == 0) || (i2 == 2) || (i2 == 1))
/*     */               {
/* 409 */                 m = 1;
/* 410 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 415 */         if (m == 0) {
/* 416 */           if (localGeneralSubtrees2 == null) {
/* 417 */             localGeneralSubtrees2 = new GeneralSubtrees();
/*     */           }
/* 419 */           localObject2 = createWidestSubtree((GeneralNameInterface)localObject1);
/*     */ 
/* 421 */           if (!localGeneralSubtrees2.contains((GeneralSubtree)localObject2)) {
/* 422 */             localGeneralSubtrees2.add((GeneralSubtree)localObject2);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 427 */         remove(i);
/* 428 */         i--;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 433 */     if (localGeneralSubtrees1.size() > 0) {
/* 434 */       union(localGeneralSubtrees1);
/*     */     }
/*     */ 
/* 439 */     for (i = 0; i < paramGeneralSubtrees.size(); i++) {
/* 440 */       localObject1 = paramGeneralSubtrees.get(i);
/* 441 */       GeneralNameInterface localGeneralNameInterface1 = getGeneralNameInterface((GeneralSubtree)localObject1);
/* 442 */       k = 0;
/* 443 */       for (m = 0; m < size(); m++) {
/* 444 */         localObject2 = getGeneralNameInterface(m);
/* 445 */         switch (((GeneralNameInterface)localObject2).constrains(localGeneralNameInterface1)) {
/*     */         case -1:
/* 447 */           k = 1;
/*     */ 
/* 450 */           break;
/*     */         case 0:
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/* 455 */           k = 0;
/*     */ 
/* 458 */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 464 */       if (k != 0) {
/* 465 */         add((GeneralSubtree)localObject1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 470 */     return localGeneralSubtrees2;
/*     */   }
/*     */ 
/*     */   public void union(GeneralSubtrees paramGeneralSubtrees)
/*     */   {
/* 479 */     if (paramGeneralSubtrees != null) {
/* 480 */       int i = 0; for (int j = paramGeneralSubtrees.size(); i < j; i++) {
/* 481 */         add(paramGeneralSubtrees.get(i));
/*     */       }
/*     */ 
/* 484 */       minimize();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reduce(GeneralSubtrees paramGeneralSubtrees)
/*     */   {
/* 497 */     if (paramGeneralSubtrees == null) {
/* 498 */       return;
/*     */     }
/* 500 */     int i = 0; for (int j = paramGeneralSubtrees.size(); i < j; i++) {
/* 501 */       GeneralNameInterface localGeneralNameInterface1 = paramGeneralSubtrees.getGeneralNameInterface(i);
/* 502 */       for (int k = 0; k < size(); k++) {
/* 503 */         GeneralNameInterface localGeneralNameInterface2 = getGeneralNameInterface(k);
/* 504 */         switch (localGeneralNameInterface1.constrains(localGeneralNameInterface2)) {
/*     */         case -1:
/* 506 */           break;
/*     */         case 0:
/* 508 */           remove(k);
/* 509 */           k--;
/* 510 */           break;
/*     */         case 1:
/* 513 */           remove(k);
/* 514 */           k--;
/* 515 */           break;
/*     */         case 2:
/*     */         case 3:
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.GeneralSubtrees
 * JD-Core Version:    0.6.2
 */