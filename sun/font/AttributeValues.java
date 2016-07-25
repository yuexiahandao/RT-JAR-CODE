/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.font.GraphicAttribute;
/*     */ import java.awt.font.NumericShaper;
/*     */ import java.awt.font.TextAttribute;
/*     */ import java.awt.font.TransformAttribute;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.NoninvertibleTransformException;
/*     */ import java.awt.geom.Point2D.Double;
/*     */ import java.awt.im.InputMethodHighlight;
/*     */ import java.io.Serializable;
/*     */ import java.text.Annotation;
/*     */ import java.text.AttributedCharacterIterator.Attribute;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public final class AttributeValues
/*     */   implements Cloneable
/*     */ {
/*     */   private int defined;
/*     */   private int nondefault;
/*  64 */   private String family = "Default";
/*  65 */   private float weight = 1.0F;
/*  66 */   private float width = 1.0F;
/*     */   private float posture;
/*  68 */   private float size = 12.0F;
/*     */   private float tracking;
/*     */   private NumericShaper numericShaping;
/*     */   private AffineTransform transform;
/*     */   private GraphicAttribute charReplacement;
/*     */   private Paint foreground;
/*     */   private Paint background;
/*  75 */   private float justification = 1.0F;
/*     */   private Object imHighlight;
/*     */   private Font font;
/*  79 */   private byte imUnderline = -1;
/*     */   private byte superscript;
/*  81 */   private byte underline = -1;
/*  82 */   private byte runDirection = -2;
/*     */   private byte bidiEmbedding;
/*     */   private byte kerning;
/*     */   private byte ligatures;
/*     */   private boolean strikethrough;
/*     */   private boolean swapColors;
/*     */   private AffineTransform baselineTransform;
/*     */   private AffineTransform charTransform;
/*  92 */   private static final AttributeValues DEFAULT = new AttributeValues();
/*     */ 
/* 213 */   public static final int MASK_ALL = getMask((EAttribute[])EAttribute.class.getEnumConstants());
/*     */   private static final String DEFINED_KEY = "sun.font.attributevalues.defined_key";
/*     */ 
/*     */   public String getFamily()
/*     */   {
/*  95 */     return this.family; } 
/*  96 */   public void setFamily(String paramString) { this.family = paramString; update(EAttribute.EFAMILY); } 
/*     */   public float getWeight() {
/*  98 */     return this.weight; } 
/*  99 */   public void setWeight(float paramFloat) { this.weight = paramFloat; update(EAttribute.EWEIGHT); } 
/*     */   public float getWidth() {
/* 101 */     return this.width; } 
/* 102 */   public void setWidth(float paramFloat) { this.width = paramFloat; update(EAttribute.EWIDTH); } 
/*     */   public float getPosture() {
/* 104 */     return this.posture; } 
/* 105 */   public void setPosture(float paramFloat) { this.posture = paramFloat; update(EAttribute.EPOSTURE); } 
/*     */   public float getSize() {
/* 107 */     return this.size; } 
/* 108 */   public void setSize(float paramFloat) { this.size = paramFloat; update(EAttribute.ESIZE); } 
/*     */   public AffineTransform getTransform() {
/* 110 */     return this.transform;
/*     */   }
/* 112 */   public void setTransform(AffineTransform paramAffineTransform) { this.transform = ((paramAffineTransform == null) || (paramAffineTransform.isIdentity()) ? DEFAULT.transform : new AffineTransform(paramAffineTransform));
/*     */ 
/* 115 */     updateDerivedTransforms();
/* 116 */     update(EAttribute.ETRANSFORM); }
/*     */ 
/*     */   public void setTransform(TransformAttribute paramTransformAttribute) {
/* 119 */     this.transform = ((paramTransformAttribute == null) || (paramTransformAttribute.isIdentity()) ? DEFAULT.transform : paramTransformAttribute.getTransform());
/*     */ 
/* 122 */     updateDerivedTransforms();
/* 123 */     update(EAttribute.ETRANSFORM);
/*     */   }
/*     */   public int getSuperscript() {
/* 126 */     return this.superscript;
/*     */   }
/* 128 */   public void setSuperscript(int paramInt) { this.superscript = ((byte)paramInt); update(EAttribute.ESUPERSCRIPT); } 
/*     */   public Font getFont() {
/* 130 */     return this.font; } 
/* 131 */   public void setFont(Font paramFont) { this.font = paramFont; update(EAttribute.EFONT); } 
/*     */   public GraphicAttribute getCharReplacement() {
/* 133 */     return this.charReplacement;
/*     */   }
/* 135 */   public void setCharReplacement(GraphicAttribute paramGraphicAttribute) { this.charReplacement = paramGraphicAttribute; update(EAttribute.ECHAR_REPLACEMENT); } 
/*     */   public Paint getForeground() {
/* 137 */     return this.foreground;
/*     */   }
/* 139 */   public void setForeground(Paint paramPaint) { this.foreground = paramPaint; update(EAttribute.EFOREGROUND); } 
/*     */   public Paint getBackground() {
/* 141 */     return this.background;
/*     */   }
/* 143 */   public void setBackground(Paint paramPaint) { this.background = paramPaint; update(EAttribute.EBACKGROUND); } 
/*     */   public int getUnderline() {
/* 145 */     return this.underline;
/*     */   }
/* 147 */   public void setUnderline(int paramInt) { this.underline = ((byte)paramInt); update(EAttribute.EUNDERLINE); } 
/*     */   public boolean getStrikethrough() {
/* 149 */     return this.strikethrough;
/*     */   }
/* 151 */   public void setStrikethrough(boolean paramBoolean) { this.strikethrough = paramBoolean; update(EAttribute.ESTRIKETHROUGH); } 
/*     */   public int getRunDirection() {
/* 153 */     return this.runDirection;
/*     */   }
/* 155 */   public void setRunDirection(int paramInt) { this.runDirection = ((byte)paramInt); update(EAttribute.ERUN_DIRECTION); } 
/*     */   public int getBidiEmbedding() {
/* 157 */     return this.bidiEmbedding;
/*     */   }
/* 159 */   public void setBidiEmbedding(int paramInt) { this.bidiEmbedding = ((byte)paramInt); update(EAttribute.EBIDI_EMBEDDING); } 
/*     */   public float getJustification() {
/* 161 */     return this.justification;
/*     */   }
/* 163 */   public void setJustification(float paramFloat) { this.justification = paramFloat; update(EAttribute.EJUSTIFICATION); } 
/*     */   public Object getInputMethodHighlight() {
/* 165 */     return this.imHighlight;
/*     */   }
/* 167 */   public void setInputMethodHighlight(Annotation paramAnnotation) { this.imHighlight = paramAnnotation; update(EAttribute.EINPUT_METHOD_HIGHLIGHT); } 
/*     */   public void setInputMethodHighlight(InputMethodHighlight paramInputMethodHighlight) {
/* 169 */     this.imHighlight = paramInputMethodHighlight; update(EAttribute.EINPUT_METHOD_HIGHLIGHT);
/*     */   }
/* 171 */   public int getInputMethodUnderline() { return this.imUnderline; } 
/*     */   public void setInputMethodUnderline(int paramInt) {
/* 173 */     this.imUnderline = ((byte)paramInt); update(EAttribute.EINPUT_METHOD_UNDERLINE);
/*     */   }
/* 175 */   public boolean getSwapColors() { return this.swapColors; } 
/*     */   public void setSwapColors(boolean paramBoolean) {
/* 177 */     this.swapColors = paramBoolean; update(EAttribute.ESWAP_COLORS);
/*     */   }
/* 179 */   public NumericShaper getNumericShaping() { return this.numericShaping; } 
/*     */   public void setNumericShaping(NumericShaper paramNumericShaper) {
/* 181 */     this.numericShaping = paramNumericShaper; update(EAttribute.ENUMERIC_SHAPING);
/*     */   }
/* 183 */   public int getKerning() { return this.kerning; } 
/*     */   public void setKerning(int paramInt) {
/* 185 */     this.kerning = ((byte)paramInt); update(EAttribute.EKERNING);
/*     */   }
/* 187 */   public float getTracking() { return this.tracking; } 
/*     */   public void setTracking(float paramFloat) {
/* 189 */     this.tracking = ((byte)(int)paramFloat); update(EAttribute.ETRACKING);
/*     */   }
/* 191 */   public int getLigatures() { return this.ligatures; } 
/*     */   public void setLigatures(int paramInt) {
/* 193 */     this.ligatures = ((byte)paramInt); update(EAttribute.ELIGATURES);
/*     */   }
/*     */   public AffineTransform getBaselineTransform() {
/* 196 */     return this.baselineTransform; } 
/* 197 */   public AffineTransform getCharTransform() { return this.charTransform; }
/*     */ 
/*     */ 
/*     */   public static int getMask(EAttribute paramEAttribute)
/*     */   {
/* 202 */     return paramEAttribute.mask;
/*     */   }
/*     */ 
/*     */   public static int getMask(EAttribute[] paramArrayOfEAttribute) {
/* 206 */     int i = 0;
/* 207 */     for (EAttribute localEAttribute : paramArrayOfEAttribute) {
/* 208 */       i |= localEAttribute.mask;
/*     */     }
/* 210 */     return i;
/*     */   }
/*     */ 
/*     */   public void unsetDefault()
/*     */   {
/* 217 */     this.defined &= this.nondefault;
/*     */   }
/*     */ 
/*     */   public void defineAll(int paramInt) {
/* 221 */     this.defined |= paramInt;
/* 222 */     if ((this.defined & EAttribute.EBASELINE_TRANSFORM.mask) != 0)
/* 223 */       throw new InternalError("can't define derived attribute");
/*     */   }
/*     */ 
/*     */   public boolean allDefined(int paramInt)
/*     */   {
/* 228 */     return (this.defined & paramInt) == paramInt;
/*     */   }
/*     */ 
/*     */   public boolean anyDefined(int paramInt) {
/* 232 */     return (this.defined & paramInt) != 0;
/*     */   }
/*     */ 
/*     */   public boolean anyNonDefault(int paramInt) {
/* 236 */     return (this.nondefault & paramInt) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isDefined(EAttribute paramEAttribute)
/*     */   {
/* 242 */     return (this.defined & paramEAttribute.mask) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isNonDefault(EAttribute paramEAttribute) {
/* 246 */     return (this.nondefault & paramEAttribute.mask) != 0;
/*     */   }
/*     */ 
/*     */   public void setDefault(EAttribute paramEAttribute) {
/* 250 */     if (paramEAttribute.att == null) {
/* 251 */       throw new InternalError("can't set default derived attribute: " + paramEAttribute);
/*     */     }
/* 253 */     i_set(paramEAttribute, DEFAULT);
/* 254 */     this.defined |= paramEAttribute.mask;
/* 255 */     this.nondefault &= (paramEAttribute.mask ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   public void unset(EAttribute paramEAttribute) {
/* 259 */     if (paramEAttribute.att == null) {
/* 260 */       throw new InternalError("can't unset derived attribute: " + paramEAttribute);
/*     */     }
/* 262 */     i_set(paramEAttribute, DEFAULT);
/* 263 */     this.defined &= (paramEAttribute.mask ^ 0xFFFFFFFF);
/* 264 */     this.nondefault &= (paramEAttribute.mask ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   public void set(EAttribute paramEAttribute, AttributeValues paramAttributeValues) {
/* 268 */     if (paramEAttribute.att == null) {
/* 269 */       throw new InternalError("can't set derived attribute: " + paramEAttribute);
/*     */     }
/* 271 */     if ((paramAttributeValues == null) || (paramAttributeValues == DEFAULT)) {
/* 272 */       setDefault(paramEAttribute);
/*     */     }
/* 274 */     else if ((paramAttributeValues.defined & paramEAttribute.mask) != 0) {
/* 275 */       i_set(paramEAttribute, paramAttributeValues);
/* 276 */       update(paramEAttribute);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void set(EAttribute paramEAttribute, Object paramObject)
/*     */   {
/* 282 */     if (paramEAttribute.att == null) {
/* 283 */       throw new InternalError("can't set derived attribute: " + paramEAttribute);
/*     */     }
/* 285 */     if (paramObject != null)
/*     */       try {
/* 287 */         i_set(paramEAttribute, paramObject);
/* 288 */         update(paramEAttribute);
/* 289 */         return;
/*     */       }
/*     */       catch (Exception localException) {
/*     */       }
/* 293 */     setDefault(paramEAttribute);
/*     */   }
/*     */ 
/*     */   public Object get(EAttribute paramEAttribute) {
/* 297 */     if (paramEAttribute.att == null) {
/* 298 */       throw new InternalError("can't get derived attribute: " + paramEAttribute);
/*     */     }
/* 300 */     if ((this.nondefault & paramEAttribute.mask) != 0) {
/* 301 */       return i_get(paramEAttribute);
/*     */     }
/* 303 */     return null;
/*     */   }
/*     */ 
/*     */   public AttributeValues merge(Map<? extends AttributedCharacterIterator.Attribute, ?> paramMap)
/*     */   {
/* 309 */     return merge(paramMap, MASK_ALL);
/*     */   }
/*     */ 
/*     */   public AttributeValues merge(Map<? extends AttributedCharacterIterator.Attribute, ?> paramMap, int paramInt)
/*     */   {
/* 314 */     if (((paramMap instanceof AttributeMap)) && (((AttributeMap)paramMap).getValues() != null))
/*     */     {
/* 316 */       merge(((AttributeMap)paramMap).getValues(), paramInt);
/* 317 */     } else if ((paramMap != null) && (!paramMap.isEmpty())) {
/* 318 */       for (Map.Entry localEntry : paramMap.entrySet()) {
/*     */         try {
/* 320 */           EAttribute localEAttribute = EAttribute.forAttribute((AttributedCharacterIterator.Attribute)localEntry.getKey());
/* 321 */           if ((localEAttribute != null) && ((paramInt & localEAttribute.mask) != 0))
/* 322 */             set(localEAttribute, localEntry.getValue());
/*     */         }
/*     */         catch (ClassCastException localClassCastException)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/* 329 */     return this;
/*     */   }
/*     */ 
/*     */   public AttributeValues merge(AttributeValues paramAttributeValues) {
/* 333 */     return merge(paramAttributeValues, MASK_ALL);
/*     */   }
/*     */ 
/*     */   public AttributeValues merge(AttributeValues paramAttributeValues, int paramInt) {
/* 337 */     int i = paramInt & paramAttributeValues.defined;
/* 338 */     for (EAttribute localEAttribute : EAttribute.atts) {
/* 339 */       if (i == 0) {
/*     */         break;
/*     */       }
/* 342 */       if ((i & localEAttribute.mask) != 0) {
/* 343 */         i &= (localEAttribute.mask ^ 0xFFFFFFFF);
/* 344 */         i_set(localEAttribute, paramAttributeValues);
/* 345 */         update(localEAttribute);
/*     */       }
/*     */     }
/* 348 */     return this;
/*     */   }
/*     */ 
/*     */   public static AttributeValues fromMap(Map<? extends AttributedCharacterIterator.Attribute, ?> paramMap)
/*     */   {
/* 354 */     return fromMap(paramMap, MASK_ALL);
/*     */   }
/*     */ 
/*     */   public static AttributeValues fromMap(Map<? extends AttributedCharacterIterator.Attribute, ?> paramMap, int paramInt)
/*     */   {
/* 359 */     return new AttributeValues().merge(paramMap, paramInt);
/*     */   }
/*     */ 
/*     */   public Map<TextAttribute, Object> toMap(Map<TextAttribute, Object> paramMap) {
/* 363 */     if (paramMap == null) {
/* 364 */       paramMap = new HashMap();
/*     */     }
/*     */ 
/* 367 */     int i = this.defined; for (int j = 0; i != 0; j++) {
/* 368 */       EAttribute localEAttribute = EAttribute.atts[j];
/* 369 */       if ((i & localEAttribute.mask) != 0) {
/* 370 */         i &= (localEAttribute.mask ^ 0xFFFFFFFF);
/* 371 */         paramMap.put(localEAttribute.att, get(localEAttribute));
/*     */       }
/*     */     }
/*     */ 
/* 375 */     return paramMap;
/*     */   }
/*     */ 
/*     */   public static boolean is16Hashtable(Hashtable<Object, Object> paramHashtable)
/*     */   {
/* 383 */     return paramHashtable.containsKey("sun.font.attributevalues.defined_key");
/*     */   }
/*     */ 
/*     */   public static AttributeValues fromSerializableHashtable(Hashtable<Object, Object> paramHashtable)
/*     */   {
/* 389 */     AttributeValues localAttributeValues = new AttributeValues();
/* 390 */     if ((paramHashtable != null) && (!paramHashtable.isEmpty())) {
/* 391 */       for (Map.Entry localEntry : paramHashtable.entrySet()) {
/* 392 */         Object localObject1 = localEntry.getKey();
/* 393 */         Object localObject2 = localEntry.getValue();
/* 394 */         if (localObject1.equals("sun.font.attributevalues.defined_key"))
/* 395 */           localAttributeValues.defineAll(((Integer)localObject2).intValue());
/*     */         else {
/*     */           try {
/* 398 */             EAttribute localEAttribute = EAttribute.forAttribute((AttributedCharacterIterator.Attribute)localObject1);
/*     */ 
/* 400 */             if (localEAttribute != null)
/* 401 */               localAttributeValues.set(localEAttribute, localObject2);
/*     */           }
/*     */           catch (ClassCastException localClassCastException)
/*     */           {
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 409 */     return localAttributeValues;
/*     */   }
/*     */ 
/*     */   public Hashtable<Object, Object> toSerializableHashtable() {
/* 413 */     Hashtable localHashtable = new Hashtable();
/* 414 */     int i = this.defined;
/* 415 */     int j = this.defined; for (int k = 0; j != 0; k++) {
/* 416 */       EAttribute localEAttribute = EAttribute.atts[k];
/* 417 */       if ((j & localEAttribute.mask) != 0) {
/* 418 */         j &= (localEAttribute.mask ^ 0xFFFFFFFF);
/* 419 */         Object localObject = get(localEAttribute);
/* 420 */         if (localObject != null)
/*     */         {
/* 422 */           if ((localObject instanceof Serializable))
/* 423 */             localHashtable.put(localEAttribute.att, localObject);
/*     */           else
/* 425 */             i &= (localEAttribute.mask ^ 0xFFFFFFFF);
/*     */         }
/*     */       }
/*     */     }
/* 429 */     localHashtable.put("sun.font.attributevalues.defined_key", Integer.valueOf(i));
/*     */ 
/* 431 */     return localHashtable;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 436 */     return this.defined << 8 ^ this.nondefault;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*     */     try {
/* 441 */       return equals((AttributeValues)paramObject);
/*     */     }
/*     */     catch (ClassCastException localClassCastException) {
/*     */     }
/* 445 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(AttributeValues paramAttributeValues)
/*     */   {
/* 453 */     if (paramAttributeValues == null) return false;
/* 454 */     if (paramAttributeValues == this) return true;
/*     */ 
/* 456 */     return (this.defined == paramAttributeValues.defined) && (this.nondefault == paramAttributeValues.nondefault) && (this.underline == paramAttributeValues.underline) && (this.strikethrough == paramAttributeValues.strikethrough) && (this.superscript == paramAttributeValues.superscript) && (this.width == paramAttributeValues.width) && (this.kerning == paramAttributeValues.kerning) && (this.tracking == paramAttributeValues.tracking) && (this.ligatures == paramAttributeValues.ligatures) && (this.runDirection == paramAttributeValues.runDirection) && (this.bidiEmbedding == paramAttributeValues.bidiEmbedding) && (this.swapColors == paramAttributeValues.swapColors) && (equals(this.transform, paramAttributeValues.transform)) && (equals(this.foreground, paramAttributeValues.foreground)) && (equals(this.background, paramAttributeValues.background)) && (equals(this.numericShaping, paramAttributeValues.numericShaping)) && (equals(Float.valueOf(this.justification), Float.valueOf(paramAttributeValues.justification))) && (equals(this.charReplacement, paramAttributeValues.charReplacement)) && (this.size == paramAttributeValues.size) && (this.weight == paramAttributeValues.weight) && (this.posture == paramAttributeValues.posture) && (equals(this.family, paramAttributeValues.family)) && (equals(this.font, paramAttributeValues.font)) && (this.imUnderline == paramAttributeValues.imUnderline) && (equals(this.imHighlight, paramAttributeValues.imHighlight));
/*     */   }
/*     */ 
/*     */   public AttributeValues clone()
/*     */   {
/*     */     try
/*     */     {
/* 485 */       AttributeValues localAttributeValues = (AttributeValues)super.clone();
/* 486 */       if (this.transform != null) {
/* 487 */         localAttributeValues.transform = new AffineTransform(this.transform);
/* 488 */         localAttributeValues.updateDerivedTransforms();
/*     */       }
/*     */ 
/* 492 */       return localAttributeValues;
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 496 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 501 */     StringBuilder localStringBuilder = new StringBuilder();
/* 502 */     localStringBuilder.append('{');
/* 503 */     int i = this.defined; for (int j = 0; i != 0; j++) {
/* 504 */       EAttribute localEAttribute = EAttribute.atts[j];
/* 505 */       if ((i & localEAttribute.mask) != 0) {
/* 506 */         i &= (localEAttribute.mask ^ 0xFFFFFFFF);
/* 507 */         if (localStringBuilder.length() > 1) {
/* 508 */           localStringBuilder.append(", ");
/*     */         }
/* 510 */         localStringBuilder.append(localEAttribute);
/* 511 */         localStringBuilder.append('=');
/* 512 */         switch (1.$SwitchMap$sun$font$EAttribute[localEAttribute.ordinal()]) { case 1:
/* 513 */           localStringBuilder.append('"');
/* 514 */           localStringBuilder.append(this.family);
/* 515 */           localStringBuilder.append('"'); break;
/*     */         case 2:
/* 516 */           localStringBuilder.append(this.weight); break;
/*     */         case 3:
/* 517 */           localStringBuilder.append(this.width); break;
/*     */         case 4:
/* 518 */           localStringBuilder.append(this.posture); break;
/*     */         case 5:
/* 519 */           localStringBuilder.append(this.size); break;
/*     */         case 6:
/* 520 */           localStringBuilder.append(this.transform); break;
/*     */         case 7:
/* 521 */           localStringBuilder.append(this.superscript); break;
/*     */         case 8:
/* 522 */           localStringBuilder.append(this.font); break;
/*     */         case 9:
/* 523 */           localStringBuilder.append(this.charReplacement); break;
/*     */         case 10:
/* 524 */           localStringBuilder.append(this.foreground); break;
/*     */         case 11:
/* 525 */           localStringBuilder.append(this.background); break;
/*     */         case 12:
/* 526 */           localStringBuilder.append(this.underline); break;
/*     */         case 13:
/* 527 */           localStringBuilder.append(this.strikethrough); break;
/*     */         case 14:
/* 528 */           localStringBuilder.append(this.runDirection); break;
/*     */         case 15:
/* 529 */           localStringBuilder.append(this.bidiEmbedding); break;
/*     */         case 16:
/* 530 */           localStringBuilder.append(this.justification); break;
/*     */         case 17:
/* 531 */           localStringBuilder.append(this.imHighlight); break;
/*     */         case 18:
/* 532 */           localStringBuilder.append(this.imUnderline); break;
/*     */         case 19:
/* 533 */           localStringBuilder.append(this.swapColors); break;
/*     */         case 20:
/* 534 */           localStringBuilder.append(this.numericShaping); break;
/*     */         case 21:
/* 535 */           localStringBuilder.append(this.kerning); break;
/*     */         case 22:
/* 536 */           localStringBuilder.append(this.ligatures); break;
/*     */         case 23:
/* 537 */           localStringBuilder.append(this.tracking); break;
/*     */         default:
/* 538 */           throw new InternalError();
/*     */         }
/* 540 */         if ((this.nondefault & localEAttribute.mask) == 0) {
/* 541 */           localStringBuilder.append('*');
/*     */         }
/*     */       }
/*     */     }
/* 545 */     localStringBuilder.append("[btx=" + this.baselineTransform + ", ctx=" + this.charTransform + "]");
/* 546 */     localStringBuilder.append('}');
/* 547 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static boolean equals(Object paramObject1, Object paramObject2)
/*     */   {
/* 553 */     return paramObject1 == null ? false : paramObject2 == null ? true : paramObject1.equals(paramObject2);
/*     */   }
/*     */ 
/*     */   private void update(EAttribute paramEAttribute) {
/* 557 */     this.defined |= paramEAttribute.mask;
/* 558 */     if (i_validate(paramEAttribute)) {
/* 559 */       if (i_equals(paramEAttribute, DEFAULT))
/* 560 */         this.nondefault &= (paramEAttribute.mask ^ 0xFFFFFFFF);
/*     */       else
/* 562 */         this.nondefault |= paramEAttribute.mask;
/*     */     }
/*     */     else
/* 565 */       setDefault(paramEAttribute);
/*     */   }
/*     */ 
/*     */   private void i_set(EAttribute paramEAttribute, AttributeValues paramAttributeValues)
/*     */   {
/* 572 */     switch (1.$SwitchMap$sun$font$EAttribute[paramEAttribute.ordinal()]) { case 1:
/* 573 */       this.family = paramAttributeValues.family; break;
/*     */     case 2:
/* 574 */       this.weight = paramAttributeValues.weight; break;
/*     */     case 3:
/* 575 */       this.width = paramAttributeValues.width; break;
/*     */     case 4:
/* 576 */       this.posture = paramAttributeValues.posture; break;
/*     */     case 5:
/* 577 */       this.size = paramAttributeValues.size; break;
/*     */     case 6:
/* 578 */       this.transform = paramAttributeValues.transform; updateDerivedTransforms(); break;
/*     */     case 7:
/* 579 */       this.superscript = paramAttributeValues.superscript; break;
/*     */     case 8:
/* 580 */       this.font = paramAttributeValues.font; break;
/*     */     case 9:
/* 581 */       this.charReplacement = paramAttributeValues.charReplacement; break;
/*     */     case 10:
/* 582 */       this.foreground = paramAttributeValues.foreground; break;
/*     */     case 11:
/* 583 */       this.background = paramAttributeValues.background; break;
/*     */     case 12:
/* 584 */       this.underline = paramAttributeValues.underline; break;
/*     */     case 13:
/* 585 */       this.strikethrough = paramAttributeValues.strikethrough; break;
/*     */     case 14:
/* 586 */       this.runDirection = paramAttributeValues.runDirection; break;
/*     */     case 15:
/* 587 */       this.bidiEmbedding = paramAttributeValues.bidiEmbedding; break;
/*     */     case 16:
/* 588 */       this.justification = paramAttributeValues.justification; break;
/*     */     case 17:
/* 589 */       this.imHighlight = paramAttributeValues.imHighlight; break;
/*     */     case 18:
/* 590 */       this.imUnderline = paramAttributeValues.imUnderline; break;
/*     */     case 19:
/* 591 */       this.swapColors = paramAttributeValues.swapColors; break;
/*     */     case 20:
/* 592 */       this.numericShaping = paramAttributeValues.numericShaping; break;
/*     */     case 21:
/* 593 */       this.kerning = paramAttributeValues.kerning; break;
/*     */     case 22:
/* 594 */       this.ligatures = paramAttributeValues.ligatures; break;
/*     */     case 23:
/* 595 */       this.tracking = paramAttributeValues.tracking; break;
/*     */     default:
/* 596 */       throw new InternalError(); }
/*     */   }
/*     */ 
/*     */   private boolean i_equals(EAttribute paramEAttribute, AttributeValues paramAttributeValues)
/*     */   {
/* 601 */     switch (1.$SwitchMap$sun$font$EAttribute[paramEAttribute.ordinal()]) { case 1:
/* 602 */       return equals(this.family, paramAttributeValues.family);
/*     */     case 2:
/* 603 */       return this.weight == paramAttributeValues.weight;
/*     */     case 3:
/* 604 */       return this.width == paramAttributeValues.width;
/*     */     case 4:
/* 605 */       return this.posture == paramAttributeValues.posture;
/*     */     case 5:
/* 606 */       return this.size == paramAttributeValues.size;
/*     */     case 6:
/* 607 */       return equals(this.transform, paramAttributeValues.transform);
/*     */     case 7:
/* 608 */       return this.superscript == paramAttributeValues.superscript;
/*     */     case 8:
/* 609 */       return equals(this.font, paramAttributeValues.font);
/*     */     case 9:
/* 610 */       return equals(this.charReplacement, paramAttributeValues.charReplacement);
/*     */     case 10:
/* 611 */       return equals(this.foreground, paramAttributeValues.foreground);
/*     */     case 11:
/* 612 */       return equals(this.background, paramAttributeValues.background);
/*     */     case 12:
/* 613 */       return this.underline == paramAttributeValues.underline;
/*     */     case 13:
/* 614 */       return this.strikethrough == paramAttributeValues.strikethrough;
/*     */     case 14:
/* 615 */       return this.runDirection == paramAttributeValues.runDirection;
/*     */     case 15:
/* 616 */       return this.bidiEmbedding == paramAttributeValues.bidiEmbedding;
/*     */     case 16:
/* 617 */       return this.justification == paramAttributeValues.justification;
/*     */     case 17:
/* 618 */       return equals(this.imHighlight, paramAttributeValues.imHighlight);
/*     */     case 18:
/* 619 */       return this.imUnderline == paramAttributeValues.imUnderline;
/*     */     case 19:
/* 620 */       return this.swapColors == paramAttributeValues.swapColors;
/*     */     case 20:
/* 621 */       return equals(this.numericShaping, paramAttributeValues.numericShaping);
/*     */     case 21:
/* 622 */       return this.kerning == paramAttributeValues.kerning;
/*     */     case 22:
/* 623 */       return this.ligatures == paramAttributeValues.ligatures;
/*     */     case 23:
/* 624 */       return this.tracking == paramAttributeValues.tracking; }
/* 625 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   private void i_set(EAttribute paramEAttribute, Object paramObject)
/*     */   {
/*     */     Object localObject;
/* 630 */     switch (1.$SwitchMap$sun$font$EAttribute[paramEAttribute.ordinal()]) { case 1:
/* 631 */       this.family = ((String)paramObject).trim(); break;
/*     */     case 2:
/* 632 */       this.weight = ((Number)paramObject).floatValue(); break;
/*     */     case 3:
/* 633 */       this.width = ((Number)paramObject).floatValue(); break;
/*     */     case 4:
/* 634 */       this.posture = ((Number)paramObject).floatValue(); break;
/*     */     case 5:
/* 635 */       this.size = ((Number)paramObject).floatValue(); break;
/*     */     case 6:
/* 637 */       if ((paramObject instanceof TransformAttribute)) {
/* 638 */         localObject = (TransformAttribute)paramObject;
/* 639 */         if (((TransformAttribute)localObject).isIdentity())
/* 640 */           this.transform = null;
/*     */         else
/* 642 */           this.transform = ((TransformAttribute)localObject).getTransform();
/*     */       }
/*     */       else {
/* 645 */         this.transform = new AffineTransform((AffineTransform)paramObject);
/*     */       }
/* 647 */       updateDerivedTransforms();
/* 648 */       break;
/*     */     case 7:
/* 649 */       this.superscript = ((byte)((Integer)paramObject).intValue()); break;
/*     */     case 8:
/* 650 */       this.font = ((Font)paramObject); break;
/*     */     case 9:
/* 651 */       this.charReplacement = ((GraphicAttribute)paramObject); break;
/*     */     case 10:
/* 652 */       this.foreground = ((Paint)paramObject); break;
/*     */     case 11:
/* 653 */       this.background = ((Paint)paramObject); break;
/*     */     case 12:
/* 654 */       this.underline = ((byte)((Integer)paramObject).intValue()); break;
/*     */     case 13:
/* 655 */       this.strikethrough = ((Boolean)paramObject).booleanValue(); break;
/*     */     case 14:
/* 657 */       if ((paramObject instanceof Boolean))
/* 658 */         this.runDirection = ((byte)(TextAttribute.RUN_DIRECTION_LTR.equals(paramObject) ? 0 : 1));
/*     */       else {
/* 660 */         this.runDirection = ((byte)((Integer)paramObject).intValue());
/*     */       }
/* 662 */       break;
/*     */     case 15:
/* 663 */       this.bidiEmbedding = ((byte)((Integer)paramObject).intValue()); break;
/*     */     case 16:
/* 664 */       this.justification = ((Number)paramObject).floatValue(); break;
/*     */     case 17:
/* 666 */       if ((paramObject instanceof Annotation)) {
/* 667 */         localObject = (Annotation)paramObject;
/* 668 */         this.imHighlight = ((InputMethodHighlight)((Annotation)localObject).getValue());
/*     */       } else {
/* 670 */         this.imHighlight = ((InputMethodHighlight)paramObject);
/*     */       }
/* 672 */       break;
/*     */     case 18:
/* 673 */       this.imUnderline = ((byte)((Integer)paramObject).intValue());
/* 674 */       break;
/*     */     case 19:
/* 675 */       this.swapColors = ((Boolean)paramObject).booleanValue(); break;
/*     */     case 20:
/* 676 */       this.numericShaping = ((NumericShaper)paramObject); break;
/*     */     case 21:
/* 677 */       this.kerning = ((byte)((Integer)paramObject).intValue()); break;
/*     */     case 22:
/* 678 */       this.ligatures = ((byte)((Integer)paramObject).intValue()); break;
/*     */     case 23:
/* 679 */       this.tracking = ((Number)paramObject).floatValue(); break;
/*     */     default:
/* 680 */       throw new InternalError(); }
/*     */   }
/*     */ 
/*     */   private Object i_get(EAttribute paramEAttribute)
/*     */   {
/* 685 */     switch (1.$SwitchMap$sun$font$EAttribute[paramEAttribute.ordinal()]) { case 1:
/* 686 */       return this.family;
/*     */     case 2:
/* 687 */       return Float.valueOf(this.weight);
/*     */     case 3:
/* 688 */       return Float.valueOf(this.width);
/*     */     case 4:
/* 689 */       return Float.valueOf(this.posture);
/*     */     case 5:
/* 690 */       return Float.valueOf(this.size);
/*     */     case 6:
/* 692 */       return this.transform == null ? TransformAttribute.IDENTITY : new TransformAttribute(this.transform);
/*     */     case 7:
/* 695 */       return Integer.valueOf(this.superscript);
/*     */     case 8:
/* 696 */       return this.font;
/*     */     case 9:
/* 697 */       return this.charReplacement;
/*     */     case 10:
/* 698 */       return this.foreground;
/*     */     case 11:
/* 699 */       return this.background;
/*     */     case 12:
/* 700 */       return Integer.valueOf(this.underline);
/*     */     case 13:
/* 701 */       return Boolean.valueOf(this.strikethrough);
/*     */     case 14:
/* 703 */       switch (this.runDirection)
/*     */       {
/*     */       case 0:
/* 706 */         return TextAttribute.RUN_DIRECTION_LTR;
/*     */       case 1:
/* 707 */         return TextAttribute.RUN_DIRECTION_RTL;
/* 708 */       }return null;
/*     */     case 15:
/* 711 */       return Integer.valueOf(this.bidiEmbedding);
/*     */     case 16:
/* 712 */       return Float.valueOf(this.justification);
/*     */     case 17:
/* 713 */       return this.imHighlight;
/*     */     case 18:
/* 714 */       return Integer.valueOf(this.imUnderline);
/*     */     case 19:
/* 715 */       return Boolean.valueOf(this.swapColors);
/*     */     case 20:
/* 716 */       return this.numericShaping;
/*     */     case 21:
/* 717 */       return Integer.valueOf(this.kerning);
/*     */     case 22:
/* 718 */       return Integer.valueOf(this.ligatures);
/*     */     case 23:
/* 719 */       return Float.valueOf(this.tracking); }
/* 720 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   private boolean i_validate(EAttribute paramEAttribute)
/*     */   {
/* 725 */     switch (1.$SwitchMap$sun$font$EAttribute[paramEAttribute.ordinal()]) { case 1:
/* 726 */       if ((this.family == null) || (this.family.length() == 0))
/* 727 */         this.family = DEFAULT.family; return true;
/*     */     case 2:
/* 728 */       return (this.weight > 0.0F) && (this.weight < 10.0F);
/*     */     case 3:
/* 729 */       return (this.width >= 0.5F) && (this.width < 10.0F);
/*     */     case 4:
/* 730 */       return (this.posture >= -1.0F) && (this.posture <= 1.0F);
/*     */     case 5:
/* 731 */       return this.size >= 0.0F;
/*     */     case 6:
/* 732 */       if ((this.transform != null) && (this.transform.isIdentity()))
/* 733 */         this.transform = DEFAULT.transform; return true;
/*     */     case 7:
/* 734 */       return (this.superscript >= -7) && (this.superscript <= 7);
/*     */     case 8:
/* 735 */       return true;
/*     */     case 9:
/* 736 */       return true;
/*     */     case 10:
/* 737 */       return true;
/*     */     case 11:
/* 738 */       return true;
/*     */     case 12:
/* 739 */       return (this.underline >= -1) && (this.underline < 6);
/*     */     case 13:
/* 740 */       return true;
/*     */     case 14:
/* 741 */       return (this.runDirection >= -2) && (this.runDirection <= 1);
/*     */     case 15:
/* 742 */       return (this.bidiEmbedding >= -61) && (this.bidiEmbedding < 62);
/*     */     case 16:
/* 743 */       this.justification = Math.max(0.0F, Math.min(this.justification, 1.0F));
/* 744 */       return true;
/*     */     case 17:
/* 745 */       return true;
/*     */     case 18:
/* 746 */       return (this.imUnderline >= -1) && (this.imUnderline < 6);
/*     */     case 19:
/* 747 */       return true;
/*     */     case 20:
/* 748 */       return true;
/*     */     case 21:
/* 749 */       return (this.kerning >= 0) && (this.kerning <= 1);
/*     */     case 22:
/* 750 */       return (this.ligatures >= 0) && (this.ligatures <= 1);
/*     */     case 23:
/* 751 */       return (this.tracking >= -1.0F) && (this.tracking <= 10.0F); }
/* 752 */     throw new InternalError("unknown attribute: " + paramEAttribute);
/*     */   }
/*     */ 
/*     */   public static float getJustification(Map<?, ?> paramMap)
/*     */   {
/* 761 */     if (paramMap != null) {
/* 762 */       if (((paramMap instanceof AttributeMap)) && (((AttributeMap)paramMap).getValues() != null))
/*     */       {
/* 764 */         return ((AttributeMap)paramMap).getValues().justification;
/*     */       }
/* 766 */       Object localObject = paramMap.get(TextAttribute.JUSTIFICATION);
/* 767 */       if ((localObject != null) && ((localObject instanceof Number))) {
/* 768 */         return Math.max(0.0F, Math.min(1.0F, ((Number)localObject).floatValue()));
/*     */       }
/*     */     }
/* 771 */     return DEFAULT.justification;
/*     */   }
/*     */ 
/*     */   public static NumericShaper getNumericShaping(Map<?, ?> paramMap) {
/* 775 */     if (paramMap != null) {
/* 776 */       if (((paramMap instanceof AttributeMap)) && (((AttributeMap)paramMap).getValues() != null))
/*     */       {
/* 778 */         return ((AttributeMap)paramMap).getValues().numericShaping;
/*     */       }
/* 780 */       Object localObject = paramMap.get(TextAttribute.NUMERIC_SHAPING);
/* 781 */       if ((localObject != null) && ((localObject instanceof NumericShaper))) {
/* 782 */         return (NumericShaper)localObject;
/*     */       }
/*     */     }
/* 785 */     return DEFAULT.numericShaping;
/*     */   }
/*     */ 
/*     */   public AttributeValues applyIMHighlight()
/*     */   {
/* 793 */     if (this.imHighlight != null) {
/* 794 */       InputMethodHighlight localInputMethodHighlight = null;
/* 795 */       if ((this.imHighlight instanceof InputMethodHighlight))
/* 796 */         localInputMethodHighlight = (InputMethodHighlight)this.imHighlight;
/*     */       else {
/* 798 */         localInputMethodHighlight = (InputMethodHighlight)((Annotation)this.imHighlight).getValue();
/*     */       }
/*     */ 
/* 801 */       Map localMap = localInputMethodHighlight.getStyle();
/* 802 */       if (localMap == null) {
/* 803 */         Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 804 */         localMap = localToolkit.mapInputMethodHighlight(localInputMethodHighlight);
/*     */       }
/*     */ 
/* 807 */       if (localMap != null) {
/* 808 */         return clone().merge(localMap);
/*     */       }
/*     */     }
/*     */ 
/* 812 */     return this;
/*     */   }
/*     */ 
/*     */   public static AffineTransform getBaselineTransform(Map<?, ?> paramMap) {
/* 816 */     if (paramMap != null) {
/* 817 */       AttributeValues localAttributeValues = null;
/* 818 */       if (((paramMap instanceof AttributeMap)) && (((AttributeMap)paramMap).getValues() != null))
/*     */       {
/* 820 */         localAttributeValues = ((AttributeMap)paramMap).getValues();
/* 821 */       } else if (paramMap.get(TextAttribute.TRANSFORM) != null) {
/* 822 */         localAttributeValues = fromMap(paramMap);
/*     */       }
/* 824 */       if (localAttributeValues != null) {
/* 825 */         return localAttributeValues.baselineTransform;
/*     */       }
/*     */     }
/* 828 */     return null;
/*     */   }
/*     */ 
/*     */   public static AffineTransform getCharTransform(Map<?, ?> paramMap) {
/* 832 */     if (paramMap != null) {
/* 833 */       AttributeValues localAttributeValues = null;
/* 834 */       if (((paramMap instanceof AttributeMap)) && (((AttributeMap)paramMap).getValues() != null))
/*     */       {
/* 836 */         localAttributeValues = ((AttributeMap)paramMap).getValues();
/* 837 */       } else if (paramMap.get(TextAttribute.TRANSFORM) != null) {
/* 838 */         localAttributeValues = fromMap(paramMap);
/*     */       }
/* 840 */       if (localAttributeValues != null) {
/* 841 */         return localAttributeValues.charTransform;
/*     */       }
/*     */     }
/* 844 */     return null;
/*     */   }
/*     */ 
/*     */   public void updateDerivedTransforms()
/*     */   {
/* 849 */     if (this.transform == null) {
/* 850 */       this.baselineTransform = null;
/* 851 */       this.charTransform = null;
/*     */     } else {
/* 853 */       this.charTransform = new AffineTransform(this.transform);
/* 854 */       this.baselineTransform = extractXRotation(this.charTransform, true);
/*     */ 
/* 856 */       if (this.charTransform.isIdentity()) {
/* 857 */         this.charTransform = null;
/*     */       }
/*     */ 
/* 860 */       if (this.baselineTransform.isIdentity()) {
/* 861 */         this.baselineTransform = null;
/*     */       }
/*     */     }
/*     */ 
/* 865 */     if (this.baselineTransform == null)
/* 866 */       this.nondefault &= (EAttribute.EBASELINE_TRANSFORM.mask ^ 0xFFFFFFFF);
/*     */     else
/* 868 */       this.nondefault |= EAttribute.EBASELINE_TRANSFORM.mask;
/*     */   }
/*     */ 
/*     */   public static AffineTransform extractXRotation(AffineTransform paramAffineTransform, boolean paramBoolean)
/*     */   {
/* 874 */     return extractRotation(new Point2D.Double(1.0D, 0.0D), paramAffineTransform, paramBoolean);
/*     */   }
/*     */ 
/*     */   public static AffineTransform extractYRotation(AffineTransform paramAffineTransform, boolean paramBoolean)
/*     */   {
/* 879 */     return extractRotation(new Point2D.Double(0.0D, 1.0D), paramAffineTransform, paramBoolean);
/*     */   }
/*     */ 
/*     */   private static AffineTransform extractRotation(Point2D.Double paramDouble, AffineTransform paramAffineTransform, boolean paramBoolean)
/*     */   {
/* 885 */     paramAffineTransform.deltaTransform(paramDouble, paramDouble);
/* 886 */     AffineTransform localAffineTransform1 = AffineTransform.getRotateInstance(paramDouble.x, paramDouble.y);
/*     */     try
/*     */     {
/* 889 */       AffineTransform localAffineTransform2 = localAffineTransform1.createInverse();
/* 890 */       double d1 = paramAffineTransform.getTranslateX();
/* 891 */       double d2 = paramAffineTransform.getTranslateY();
/* 892 */       paramAffineTransform.preConcatenate(localAffineTransform2);
/* 893 */       if ((paramBoolean) && (
/* 894 */         (d1 != 0.0D) || (d2 != 0.0D))) {
/* 895 */         paramAffineTransform.setTransform(paramAffineTransform.getScaleX(), paramAffineTransform.getShearY(), paramAffineTransform.getShearX(), paramAffineTransform.getScaleY(), 0.0D, 0.0D);
/*     */ 
/* 897 */         localAffineTransform1.setTransform(localAffineTransform1.getScaleX(), localAffineTransform1.getShearY(), localAffineTransform1.getShearX(), localAffineTransform1.getScaleY(), d1, d2);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (NoninvertibleTransformException localNoninvertibleTransformException)
/*     */     {
/* 903 */       return null;
/*     */     }
/* 905 */     return localAffineTransform1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.AttributeValues
 * JD-Core Version:    0.6.2
 */