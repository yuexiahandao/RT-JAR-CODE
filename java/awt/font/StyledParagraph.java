/*     */ package java.awt.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.im.InputMethodHighlight;
/*     */ import java.text.Annotation;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import sun.font.Decoration;
/*     */ import sun.font.FontResolver;
/*     */ import sun.text.CodePointIterator;
/*     */ 
/*     */ final class StyledParagraph
/*     */ {
/*     */   private int length;
/*     */   private Decoration decoration;
/*     */   private Object font;
/*     */   private Vector decorations;
/*     */   int[] decorationStarts;
/*     */   private Vector fonts;
/*     */   int[] fontStarts;
/*  86 */   private static int INITIAL_SIZE = 8;
/*     */ 
/*     */   public StyledParagraph(AttributedCharacterIterator paramAttributedCharacterIterator, char[] paramArrayOfChar)
/*     */   {
/*  96 */     int i = paramAttributedCharacterIterator.getBeginIndex();
/*  97 */     int j = paramAttributedCharacterIterator.getEndIndex();
/*  98 */     this.length = (j - i);
/*     */ 
/* 100 */     int k = i;
/* 101 */     paramAttributedCharacterIterator.first();
/*     */     do
/*     */     {
/* 104 */       int m = paramAttributedCharacterIterator.getRunLimit();
/* 105 */       int n = k - i;
/*     */ 
/* 107 */       Map localMap = paramAttributedCharacterIterator.getAttributes();
/* 108 */       localMap = addInputMethodAttrs(localMap);
/* 109 */       Decoration localDecoration = Decoration.getDecoration(localMap);
/* 110 */       addDecoration(localDecoration, n);
/*     */ 
/* 112 */       Object localObject = getGraphicOrFont(localMap);
/* 113 */       if (localObject == null) {
/* 114 */         addFonts(paramArrayOfChar, localMap, n, m - i);
/*     */       }
/*     */       else {
/* 117 */         addFont(localObject, n);
/*     */       }
/*     */ 
/* 120 */       paramAttributedCharacterIterator.setIndex(m);
/* 121 */       k = m;
/*     */     }
/* 123 */     while (k < j);
/*     */ 
/* 128 */     if (this.decorations != null) {
/* 129 */       this.decorationStarts = addToVector(this, this.length, this.decorations, this.decorationStarts);
/*     */     }
/* 131 */     if (this.fonts != null)
/* 132 */       this.fontStarts = addToVector(this, this.length, this.fonts, this.fontStarts);
/*     */   }
/*     */ 
/*     */   private static void insertInto(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*     */   {
/* 142 */     while (paramArrayOfInt[(--paramInt2)] > paramInt1)
/* 143 */       paramArrayOfInt[paramInt2] += 1;
/*     */   }
/*     */ 
/*     */   public static StyledParagraph insertChar(AttributedCharacterIterator paramAttributedCharacterIterator, char[] paramArrayOfChar, int paramInt, StyledParagraph paramStyledParagraph)
/*     */   {
/* 168 */     char c = paramAttributedCharacterIterator.setIndex(paramInt);
/* 169 */     int i = Math.max(paramInt - paramAttributedCharacterIterator.getBeginIndex() - 1, 0);
/*     */ 
/* 171 */     Map localMap = addInputMethodAttrs(paramAttributedCharacterIterator.getAttributes());
/* 172 */     Decoration localDecoration = Decoration.getDecoration(localMap);
/* 173 */     if (!paramStyledParagraph.getDecorationAt(i).equals(localDecoration)) {
/* 174 */       return new StyledParagraph(paramAttributedCharacterIterator, paramArrayOfChar);
/*     */     }
/* 176 */     Object localObject = getGraphicOrFont(localMap);
/* 177 */     if (localObject == null) {
/* 178 */       FontResolver localFontResolver = FontResolver.getInstance();
/* 179 */       int j = localFontResolver.getFontIndex(c);
/* 180 */       localObject = localFontResolver.getFont(j, localMap);
/*     */     }
/* 182 */     if (!paramStyledParagraph.getFontOrGraphicAt(i).equals(localObject)) {
/* 183 */       return new StyledParagraph(paramAttributedCharacterIterator, paramArrayOfChar);
/*     */     }
/*     */ 
/* 187 */     paramStyledParagraph.length += 1;
/* 188 */     if (paramStyledParagraph.decorations != null) {
/* 189 */       insertInto(i, paramStyledParagraph.decorationStarts, paramStyledParagraph.decorations.size());
/*     */     }
/*     */ 
/* 193 */     if (paramStyledParagraph.fonts != null) {
/* 194 */       insertInto(i, paramStyledParagraph.fontStarts, paramStyledParagraph.fonts.size());
/*     */     }
/*     */ 
/* 198 */     return paramStyledParagraph;
/*     */   }
/*     */ 
/*     */   private static void deleteFrom(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*     */   {
/* 209 */     while (paramArrayOfInt[(--paramInt2)] > paramInt1)
/* 210 */       paramArrayOfInt[paramInt2] -= 1;
/*     */   }
/*     */ 
/*     */   public static StyledParagraph deleteChar(AttributedCharacterIterator paramAttributedCharacterIterator, char[] paramArrayOfChar, int paramInt, StyledParagraph paramStyledParagraph)
/*     */   {
/* 234 */     paramInt -= paramAttributedCharacterIterator.getBeginIndex();
/*     */ 
/* 236 */     if ((paramStyledParagraph.decorations == null) && (paramStyledParagraph.fonts == null)) {
/* 237 */       paramStyledParagraph.length -= 1;
/* 238 */       return paramStyledParagraph;
/*     */     }
/*     */ 
/* 241 */     if ((paramStyledParagraph.getRunLimit(paramInt) == paramInt + 1) && (
/* 242 */       (paramInt == 0) || (paramStyledParagraph.getRunLimit(paramInt - 1) == paramInt))) {
/* 243 */       return new StyledParagraph(paramAttributedCharacterIterator, paramArrayOfChar);
/*     */     }
/*     */ 
/* 247 */     paramStyledParagraph.length -= 1;
/* 248 */     if (paramStyledParagraph.decorations != null) {
/* 249 */       deleteFrom(paramInt, paramStyledParagraph.decorationStarts, paramStyledParagraph.decorations.size());
/*     */     }
/*     */ 
/* 253 */     if (paramStyledParagraph.fonts != null) {
/* 254 */       deleteFrom(paramInt, paramStyledParagraph.fontStarts, paramStyledParagraph.fonts.size());
/*     */     }
/*     */ 
/* 258 */     return paramStyledParagraph;
/*     */   }
/*     */ 
/*     */   public int getRunLimit(int paramInt)
/*     */   {
/* 270 */     if ((paramInt < 0) || (paramInt >= this.length)) {
/* 271 */       throw new IllegalArgumentException("index out of range");
/*     */     }
/* 273 */     int i = this.length;
/* 274 */     if (this.decorations != null) {
/* 275 */       j = findRunContaining(paramInt, this.decorationStarts);
/* 276 */       i = this.decorationStarts[(j + 1)];
/*     */     }
/* 278 */     int j = this.length;
/* 279 */     if (this.fonts != null) {
/* 280 */       int k = findRunContaining(paramInt, this.fontStarts);
/* 281 */       j = this.fontStarts[(k + 1)];
/*     */     }
/* 283 */     return Math.min(i, j);
/*     */   }
/*     */ 
/*     */   public Decoration getDecorationAt(int paramInt)
/*     */   {
/* 293 */     if ((paramInt < 0) || (paramInt >= this.length)) {
/* 294 */       throw new IllegalArgumentException("index out of range");
/*     */     }
/* 296 */     if (this.decorations == null) {
/* 297 */       return this.decoration;
/*     */     }
/* 299 */     int i = findRunContaining(paramInt, this.decorationStarts);
/* 300 */     return (Decoration)this.decorations.elementAt(i);
/*     */   }
/*     */ 
/*     */   public Object getFontOrGraphicAt(int paramInt)
/*     */   {
/* 312 */     if ((paramInt < 0) || (paramInt >= this.length)) {
/* 313 */       throw new IllegalArgumentException("index out of range");
/*     */     }
/* 315 */     if (this.fonts == null) {
/* 316 */       return this.font;
/*     */     }
/* 318 */     int i = findRunContaining(paramInt, this.fontStarts);
/* 319 */     return this.fonts.elementAt(i);
/*     */   }
/*     */ 
/*     */   private static int findRunContaining(int paramInt, int[] paramArrayOfInt)
/*     */   {
/* 329 */     for (int i = 1; ; i++)
/* 330 */       if (paramArrayOfInt[i] > paramInt)
/* 331 */         return i - 1;
/*     */   }
/*     */ 
/*     */   private static int[] addToVector(Object paramObject, int paramInt, Vector paramVector, int[] paramArrayOfInt)
/*     */   {
/* 347 */     if (!paramVector.lastElement().equals(paramObject)) {
/* 348 */       paramVector.addElement(paramObject);
/* 349 */       int i = paramVector.size();
/* 350 */       if (paramArrayOfInt.length == i) {
/* 351 */         int[] arrayOfInt = new int[paramArrayOfInt.length * 2];
/* 352 */         System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, paramArrayOfInt.length);
/* 353 */         paramArrayOfInt = arrayOfInt;
/*     */       }
/* 355 */       paramArrayOfInt[(i - 1)] = paramInt;
/*     */     }
/* 357 */     return paramArrayOfInt;
/*     */   }
/*     */ 
/*     */   private void addDecoration(Decoration paramDecoration, int paramInt)
/*     */   {
/* 366 */     if (this.decorations != null) {
/* 367 */       this.decorationStarts = addToVector(paramDecoration, paramInt, this.decorations, this.decorationStarts);
/*     */     }
/* 372 */     else if (this.decoration == null) {
/* 373 */       this.decoration = paramDecoration;
/*     */     }
/* 376 */     else if (!this.decoration.equals(paramDecoration)) {
/* 377 */       this.decorations = new Vector(INITIAL_SIZE);
/* 378 */       this.decorations.addElement(this.decoration);
/* 379 */       this.decorations.addElement(paramDecoration);
/* 380 */       this.decorationStarts = new int[INITIAL_SIZE];
/* 381 */       this.decorationStarts[0] = 0;
/* 382 */       this.decorationStarts[1] = paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addFont(Object paramObject, int paramInt)
/*     */   {
/* 393 */     if (this.fonts != null) {
/* 394 */       this.fontStarts = addToVector(paramObject, paramInt, this.fonts, this.fontStarts);
/*     */     }
/* 396 */     else if (this.font == null) {
/* 397 */       this.font = paramObject;
/*     */     }
/* 400 */     else if (!this.font.equals(paramObject)) {
/* 401 */       this.fonts = new Vector(INITIAL_SIZE);
/* 402 */       this.fonts.addElement(this.font);
/* 403 */       this.fonts.addElement(paramObject);
/* 404 */       this.fontStarts = new int[INITIAL_SIZE];
/* 405 */       this.fontStarts[0] = 0;
/* 406 */       this.fontStarts[1] = paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addFonts(char[] paramArrayOfChar, Map paramMap, int paramInt1, int paramInt2)
/*     */   {
/* 417 */     FontResolver localFontResolver = FontResolver.getInstance();
/* 418 */     CodePointIterator localCodePointIterator = CodePointIterator.create(paramArrayOfChar, paramInt1, paramInt2);
/* 419 */     for (int i = localCodePointIterator.charIndex(); i < paramInt2; i = localCodePointIterator.charIndex()) {
/* 420 */       int j = localFontResolver.nextFontRunIndex(localCodePointIterator);
/* 421 */       addFont(localFontResolver.getFont(j, paramMap), i);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Map addInputMethodAttrs(Map paramMap)
/*     */   {
/* 431 */     Object localObject1 = paramMap.get(TextAttribute.INPUT_METHOD_HIGHLIGHT);
/*     */     try
/*     */     {
/* 434 */       if (localObject1 != null) {
/* 435 */         if ((localObject1 instanceof Annotation)) {
/* 436 */           localObject1 = ((Annotation)localObject1).getValue();
/*     */         }
/*     */ 
/* 440 */         InputMethodHighlight localInputMethodHighlight = (InputMethodHighlight)localObject1;
/*     */ 
/* 442 */         Map localMap = null;
/*     */         try {
/* 444 */           localMap = localInputMethodHighlight.getStyle();
/*     */         }
/*     */         catch (NoSuchMethodError localNoSuchMethodError)
/*     */         {
/*     */         }
/*     */         Object localObject2;
/* 448 */         if (localMap == null) {
/* 449 */           localObject2 = Toolkit.getDefaultToolkit();
/* 450 */           localMap = ((Toolkit)localObject2).mapInputMethodHighlight(localInputMethodHighlight);
/*     */         }
/*     */ 
/* 453 */         if (localMap != null) {
/* 454 */           localObject2 = new HashMap(5, 0.9F);
/* 455 */           ((HashMap)localObject2).putAll(paramMap);
/*     */ 
/* 457 */           ((HashMap)localObject2).putAll(localMap);
/*     */ 
/* 459 */           return localObject2;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/*     */     }
/* 466 */     return paramMap;
/*     */   }
/*     */ 
/*     */   private static Object getGraphicOrFont(Map paramMap)
/*     */   {
/* 476 */     Object localObject = paramMap.get(TextAttribute.CHAR_REPLACEMENT);
/* 477 */     if (localObject != null) {
/* 478 */       return localObject;
/*     */     }
/* 480 */     localObject = paramMap.get(TextAttribute.FONT);
/* 481 */     if (localObject != null) {
/* 482 */       return localObject;
/*     */     }
/*     */ 
/* 485 */     if (paramMap.get(TextAttribute.FAMILY) != null) {
/* 486 */       return Font.getFont(paramMap);
/*     */     }
/*     */ 
/* 489 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.StyledParagraph
 * JD-Core Version:    0.6.2
 */