/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public final class CompositeType
/*     */ {
/*  56 */   private static int unusedUID = 1;
/*  57 */   private static final HashMap<String, Integer> compositeUIDMap = new HashMap(100);
/*     */   public static final String DESC_ANY = "Any CompositeContext";
/*     */   public static final String DESC_XOR = "XOR mode";
/*     */   public static final String DESC_CLEAR = "Porter-Duff Clear";
/*     */   public static final String DESC_SRC = "Porter-Duff Src";
/*     */   public static final String DESC_DST = "Porter-Duff Dst";
/*     */   public static final String DESC_SRC_OVER = "Porter-Duff Src Over Dst";
/*     */   public static final String DESC_DST_OVER = "Porter-Duff Dst Over Src";
/*     */   public static final String DESC_SRC_IN = "Porter-Duff Src In Dst";
/*     */   public static final String DESC_DST_IN = "Porter-Duff Dst In Src";
/*     */   public static final String DESC_SRC_OUT = "Porter-Duff Src HeldOutBy Dst";
/*     */   public static final String DESC_DST_OUT = "Porter-Duff Dst HeldOutBy Src";
/*     */   public static final String DESC_SRC_ATOP = "Porter-Duff Src Atop Dst";
/*     */   public static final String DESC_DST_ATOP = "Porter-Duff Dst Atop Src";
/*     */   public static final String DESC_ALPHA_XOR = "Porter-Duff Xor";
/*     */   public static final String DESC_SRC_NO_EA = "Porter-Duff Src, No Extra Alpha";
/*     */   public static final String DESC_SRC_OVER_NO_EA = "Porter-Duff SrcOverDst, No Extra Alpha";
/*     */   public static final String DESC_ANY_ALPHA = "Any AlphaComposite Rule";
/* 117 */   public static final CompositeType Any = new CompositeType(null, "Any CompositeContext");
/*     */ 
/* 124 */   public static final CompositeType General = Any;
/*     */ 
/* 127 */   public static final CompositeType AnyAlpha = General.deriveSubType("Any AlphaComposite Rule");
/*     */ 
/* 129 */   public static final CompositeType Xor = General.deriveSubType("XOR mode");
/*     */ 
/* 132 */   public static final CompositeType Clear = AnyAlpha.deriveSubType("Porter-Duff Clear");
/*     */ 
/* 134 */   public static final CompositeType Src = AnyAlpha.deriveSubType("Porter-Duff Src");
/*     */ 
/* 136 */   public static final CompositeType Dst = AnyAlpha.deriveSubType("Porter-Duff Dst");
/*     */ 
/* 138 */   public static final CompositeType SrcOver = AnyAlpha.deriveSubType("Porter-Duff Src Over Dst");
/*     */ 
/* 140 */   public static final CompositeType DstOver = AnyAlpha.deriveSubType("Porter-Duff Dst Over Src");
/*     */ 
/* 142 */   public static final CompositeType SrcIn = AnyAlpha.deriveSubType("Porter-Duff Src In Dst");
/*     */ 
/* 144 */   public static final CompositeType DstIn = AnyAlpha.deriveSubType("Porter-Duff Dst In Src");
/*     */ 
/* 146 */   public static final CompositeType SrcOut = AnyAlpha.deriveSubType("Porter-Duff Src HeldOutBy Dst");
/*     */ 
/* 148 */   public static final CompositeType DstOut = AnyAlpha.deriveSubType("Porter-Duff Dst HeldOutBy Src");
/*     */ 
/* 150 */   public static final CompositeType SrcAtop = AnyAlpha.deriveSubType("Porter-Duff Src Atop Dst");
/*     */ 
/* 152 */   public static final CompositeType DstAtop = AnyAlpha.deriveSubType("Porter-Duff Dst Atop Src");
/*     */ 
/* 154 */   public static final CompositeType AlphaXor = AnyAlpha.deriveSubType("Porter-Duff Xor");
/*     */ 
/* 157 */   public static final CompositeType SrcNoEa = Src.deriveSubType("Porter-Duff Src, No Extra Alpha");
/*     */ 
/* 159 */   public static final CompositeType SrcOverNoEa = SrcOver.deriveSubType("Porter-Duff SrcOverDst, No Extra Alpha");
/*     */ 
/* 174 */   public static final CompositeType OpaqueSrcOverNoEa = SrcOverNoEa.deriveSubType("Porter-Duff Src").deriveSubType("Porter-Duff Src, No Extra Alpha");
/*     */   private int uniqueID;
/*     */   private String desc;
/*     */   private CompositeType next;
/*     */ 
/*     */   public CompositeType deriveSubType(String paramString)
/*     */   {
/* 189 */     return new CompositeType(this, paramString);
/*     */   }
/*     */ 
/*     */   public static CompositeType forAlphaComposite(AlphaComposite paramAlphaComposite)
/*     */   {
/* 197 */     switch (paramAlphaComposite.getRule()) {
/*     */     case 1:
/* 199 */       return Clear;
/*     */     case 2:
/* 201 */       if (paramAlphaComposite.getAlpha() >= 1.0F) {
/* 202 */         return SrcNoEa;
/*     */       }
/* 204 */       return Src;
/*     */     case 9:
/* 207 */       return Dst;
/*     */     case 3:
/* 209 */       if (paramAlphaComposite.getAlpha() >= 1.0F) {
/* 210 */         return SrcOverNoEa;
/*     */       }
/* 212 */       return SrcOver;
/*     */     case 4:
/* 215 */       return DstOver;
/*     */     case 5:
/* 217 */       return SrcIn;
/*     */     case 6:
/* 219 */       return DstIn;
/*     */     case 7:
/* 221 */       return SrcOut;
/*     */     case 8:
/* 223 */       return DstOut;
/*     */     case 10:
/* 225 */       return SrcAtop;
/*     */     case 11:
/* 227 */       return DstAtop;
/*     */     case 12:
/* 229 */       return AlphaXor;
/*     */     }
/* 231 */     throw new InternalError("Unrecognized alpha rule");
/*     */   }
/*     */ 
/*     */   private CompositeType(CompositeType paramCompositeType, String paramString)
/*     */   {
/* 240 */     this.next = paramCompositeType;
/* 241 */     this.desc = paramString;
/* 242 */     this.uniqueID = makeUniqueID(paramString);
/*     */   }
/*     */ 
/*     */   public static final synchronized int makeUniqueID(String paramString) {
/* 246 */     Integer localInteger = (Integer)compositeUIDMap.get(paramString);
/*     */ 
/* 248 */     if (localInteger == null) {
/* 249 */       if (unusedUID > 255) {
/* 250 */         throw new InternalError("composite type id overflow");
/*     */       }
/* 252 */       localInteger = Integer.valueOf(unusedUID++);
/* 253 */       compositeUIDMap.put(paramString, localInteger);
/*     */     }
/* 255 */     return localInteger.intValue();
/*     */   }
/*     */ 
/*     */   public int getUniqueID() {
/* 259 */     return this.uniqueID;
/*     */   }
/*     */ 
/*     */   public String getDescriptor() {
/* 263 */     return this.desc;
/*     */   }
/*     */ 
/*     */   public CompositeType getSuperType() {
/* 267 */     return this.next;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 271 */     return this.desc.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean isDerivedFrom(CompositeType paramCompositeType) {
/* 275 */     CompositeType localCompositeType = this;
/*     */     do {
/* 277 */       if (localCompositeType.desc == paramCompositeType.desc) {
/* 278 */         return true;
/*     */       }
/* 280 */       localCompositeType = localCompositeType.next;
/* 281 */     }while (localCompositeType != null);
/* 282 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 286 */     if ((paramObject instanceof CompositeType)) {
/* 287 */       return ((CompositeType)paramObject).uniqueID == this.uniqueID;
/*     */     }
/* 289 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 293 */     return this.desc;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.CompositeType
 * JD-Core Version:    0.6.2
 */