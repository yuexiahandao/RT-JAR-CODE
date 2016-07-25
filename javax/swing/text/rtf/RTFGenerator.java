/*     */ package javax.swing.text.rtf;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Dictionary;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.MutableAttributeSet;
/*     */ import javax.swing.text.Segment;
/*     */ import javax.swing.text.SimpleAttributeSet;
/*     */ import javax.swing.text.Style;
/*     */ import javax.swing.text.StyleConstants;
/*     */ import javax.swing.text.TabStop;
/*     */ 
/*     */ class RTFGenerator
/*     */ {
/*     */   Dictionary<Object, Integer> colorTable;
/*     */   int colorCount;
/*     */   Dictionary<String, Integer> fontTable;
/*     */   int fontCount;
/*     */   Dictionary<AttributeSet, Integer> styleTable;
/*     */   int styleCount;
/*     */   OutputStream outputStream;
/*     */   boolean afterKeyword;
/*     */   MutableAttributeSet outputAttributes;
/*     */   int unicodeCount;
/*     */   private Segment workingSegment;
/*     */   int[] outputConversion;
/*  79 */   public static final Color defaultRTFColor = Color.black;
/*     */   public static final float defaultFontSize = 12.0F;
/*     */   public static final String defaultFontFamily = "Helvetica";
/*  97 */   private static final Object MagicToken = new Object();
/*     */   protected static CharacterKeywordPair[] textKeywords;
/* 112 */   static final char[] hexdigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/*     */   public static void writeDocument(Document paramDocument, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 118 */     RTFGenerator localRTFGenerator = new RTFGenerator(paramOutputStream);
/* 119 */     Element localElement = paramDocument.getDefaultRootElement();
/*     */ 
/* 121 */     localRTFGenerator.examineElement(localElement);
/* 122 */     localRTFGenerator.writeRTFHeader();
/* 123 */     localRTFGenerator.writeDocumentProperties(paramDocument);
/*     */ 
/* 127 */     int i = localElement.getElementCount();
/* 128 */     for (int j = 0; j < i; j++) {
/* 129 */       localRTFGenerator.writeParagraphElement(localElement.getElement(j));
/*     */     }
/* 131 */     localRTFGenerator.writeRTFTrailer();
/*     */   }
/*     */ 
/*     */   public RTFGenerator(OutputStream paramOutputStream)
/*     */   {
/* 136 */     this.colorTable = new Hashtable();
/* 137 */     this.colorTable.put(defaultRTFColor, Integer.valueOf(0));
/* 138 */     this.colorCount = 1;
/*     */ 
/* 140 */     this.fontTable = new Hashtable();
/* 141 */     this.fontCount = 0;
/*     */ 
/* 143 */     this.styleTable = new Hashtable();
/*     */ 
/* 145 */     this.styleCount = 0;
/*     */ 
/* 147 */     this.workingSegment = new Segment();
/*     */ 
/* 149 */     this.outputStream = paramOutputStream;
/*     */ 
/* 151 */     this.unicodeCount = 1;
/*     */   }
/*     */ 
/*     */   public void examineElement(Element paramElement)
/*     */   {
/* 156 */     AttributeSet localAttributeSet = paramElement.getAttributes();
/*     */ 
/* 160 */     tallyStyles(localAttributeSet);
/*     */ 
/* 162 */     if (localAttributeSet != null)
/*     */     {
/* 165 */       Color localColor = StyleConstants.getForeground(localAttributeSet);
/* 166 */       if ((localColor != null) && (this.colorTable.get(localColor) == null))
/*     */       {
/* 168 */         this.colorTable.put(localColor, new Integer(this.colorCount));
/* 169 */         this.colorCount += 1;
/*     */       }
/*     */ 
/* 172 */       Object localObject = localAttributeSet.getAttribute(StyleConstants.Background);
/* 173 */       if ((localObject != null) && (this.colorTable.get(localObject) == null))
/*     */       {
/* 175 */         this.colorTable.put(localObject, new Integer(this.colorCount));
/* 176 */         this.colorCount += 1;
/*     */       }
/*     */ 
/* 179 */       String str = StyleConstants.getFontFamily(localAttributeSet);
/*     */ 
/* 181 */       if (str == null) {
/* 182 */         str = "Helvetica";
/*     */       }
/* 184 */       if ((str != null) && (this.fontTable.get(str) == null))
/*     */       {
/* 186 */         this.fontTable.put(str, new Integer(this.fontCount));
/* 187 */         this.fontCount += 1;
/*     */       }
/*     */     }
/*     */ 
/* 191 */     int i = paramElement.getElementCount();
/* 192 */     for (int j = 0; j < i; j++)
/* 193 */       examineElement(paramElement.getElement(j));
/*     */   }
/*     */ 
/*     */   private void tallyStyles(AttributeSet paramAttributeSet)
/*     */   {
/* 198 */     while (paramAttributeSet != null) {
/* 199 */       if ((paramAttributeSet instanceof Style)) {
/* 200 */         Integer localInteger = (Integer)this.styleTable.get(paramAttributeSet);
/* 201 */         if (localInteger == null) {
/* 202 */           this.styleCount += 1;
/* 203 */           localInteger = new Integer(this.styleCount);
/* 204 */           this.styleTable.put(paramAttributeSet, localInteger);
/*     */         }
/*     */       }
/* 207 */       paramAttributeSet = paramAttributeSet.getResolveParent();
/*     */     }
/*     */   }
/*     */ 
/*     */   private Style findStyle(AttributeSet paramAttributeSet)
/*     */   {
/* 213 */     while (paramAttributeSet != null) {
/* 214 */       if ((paramAttributeSet instanceof Style)) {
/* 215 */         Object localObject = this.styleTable.get(paramAttributeSet);
/* 216 */         if (localObject != null)
/* 217 */           return (Style)paramAttributeSet;
/*     */       }
/* 219 */       paramAttributeSet = paramAttributeSet.getResolveParent();
/*     */     }
/* 221 */     return null;
/*     */   }
/*     */ 
/*     */   private Integer findStyleNumber(AttributeSet paramAttributeSet, String paramString)
/*     */   {
/* 226 */     while (paramAttributeSet != null) {
/* 227 */       if ((paramAttributeSet instanceof Style)) {
/* 228 */         Integer localInteger = (Integer)this.styleTable.get(paramAttributeSet);
/* 229 */         if ((localInteger != null) && (
/* 230 */           (paramString == null) || (paramString.equals(paramAttributeSet.getAttribute("style:type")))))
/*     */         {
/* 232 */           return localInteger;
/*     */         }
/*     */       }
/*     */ 
/* 236 */       paramAttributeSet = paramAttributeSet.getResolveParent();
/*     */     }
/* 238 */     return null;
/*     */   }
/*     */ 
/*     */   private static Object attrDiff(MutableAttributeSet paramMutableAttributeSet, AttributeSet paramAttributeSet, Object paramObject1, Object paramObject2)
/*     */   {
/* 248 */     Object localObject1 = paramMutableAttributeSet.getAttribute(paramObject1);
/* 249 */     Object localObject2 = paramAttributeSet.getAttribute(paramObject1);
/*     */ 
/* 251 */     if (localObject2 == localObject1)
/* 252 */       return null;
/* 253 */     if (localObject2 == null) {
/* 254 */       paramMutableAttributeSet.removeAttribute(paramObject1);
/* 255 */       if ((paramObject2 != null) && (!paramObject2.equals(localObject1))) {
/* 256 */         return paramObject2;
/*     */       }
/* 258 */       return null;
/*     */     }
/* 260 */     if ((localObject1 == null) || (!equalArraysOK(localObject1, localObject2)))
/*     */     {
/* 262 */       paramMutableAttributeSet.addAttribute(paramObject1, localObject2);
/* 263 */       return localObject2;
/*     */     }
/* 265 */     return null;
/*     */   }
/*     */ 
/*     */   private static boolean equalArraysOK(Object paramObject1, Object paramObject2)
/*     */   {
/* 271 */     if (paramObject1 == paramObject2)
/* 272 */       return true;
/* 273 */     if ((paramObject1 == null) || (paramObject2 == null))
/* 274 */       return false;
/* 275 */     if (paramObject1.equals(paramObject2))
/* 276 */       return true;
/* 277 */     if ((!paramObject1.getClass().isArray()) || (!paramObject2.getClass().isArray()))
/* 278 */       return false;
/* 279 */     Object[] arrayOfObject1 = (Object[])paramObject1;
/* 280 */     Object[] arrayOfObject2 = (Object[])paramObject2;
/* 281 */     if (arrayOfObject1.length != arrayOfObject2.length) {
/* 282 */       return false;
/*     */     }
/*     */ 
/* 285 */     int j = arrayOfObject1.length;
/* 286 */     for (int i = 0; i < j; i++) {
/* 287 */       if (!equalArraysOK(arrayOfObject1[i], arrayOfObject2[i])) {
/* 288 */         return false;
/*     */       }
/*     */     }
/* 291 */     return true;
/*     */   }
/*     */ 
/*     */   public void writeLineBreak()
/*     */     throws IOException
/*     */   {
/* 298 */     writeRawString("\n");
/* 299 */     this.afterKeyword = false;
/*     */   }
/*     */ 
/*     */   public void writeRTFHeader()
/*     */     throws IOException
/*     */   {
/* 314 */     writeBegingroup();
/* 315 */     writeControlWord("rtf", 1);
/* 316 */     writeControlWord("ansi");
/* 317 */     this.outputConversion = outputConversionForName("ansi");
/* 318 */     writeLineBreak();
/*     */ 
/* 321 */     String[] arrayOfString = new String[this.fontCount];
/* 322 */     Enumeration localEnumeration = this.fontTable.keys();
/*     */     Object localObject1;
/* 324 */     while (localEnumeration.hasMoreElements()) {
/* 325 */       String str = (String)localEnumeration.nextElement();
/* 326 */       localObject1 = (Integer)this.fontTable.get(str);
/* 327 */       arrayOfString[localObject1.intValue()] = str;
/*     */     }
/* 329 */     writeBegingroup();
/* 330 */     writeControlWord("fonttbl");
/* 331 */     for (int i = 0; i < this.fontCount; i++) {
/* 332 */       writeControlWord("f", i);
/* 333 */       writeControlWord("fnil");
/* 334 */       writeText(arrayOfString[i]);
/* 335 */       writeText(";");
/*     */     }
/* 337 */     writeEndgroup();
/* 338 */     writeLineBreak();
/*     */     Object localObject2;
/*     */     Object localObject3;
/* 341 */     if (this.colorCount > 1) {
/* 342 */       localObject1 = new Color[this.colorCount];
/* 343 */       localObject2 = this.colorTable.keys();
/*     */       Color localColor;
/* 345 */       while (((Enumeration)localObject2).hasMoreElements()) {
/* 346 */         localColor = (Color)((Enumeration)localObject2).nextElement();
/* 347 */         localObject3 = (Integer)this.colorTable.get(localColor);
/* 348 */         localObject1[localObject3.intValue()] = localColor;
/*     */       }
/* 350 */       writeBegingroup();
/* 351 */       writeControlWord("colortbl");
/* 352 */       for (i = 0; i < this.colorCount; i++) {
/* 353 */         localColor = localObject1[i];
/* 354 */         if (localColor != null) {
/* 355 */           writeControlWord("red", localColor.getRed());
/* 356 */           writeControlWord("green", localColor.getGreen());
/* 357 */           writeControlWord("blue", localColor.getBlue());
/*     */         }
/* 359 */         writeRawString(";");
/*     */       }
/* 361 */       writeEndgroup();
/* 362 */       writeLineBreak();
/*     */     }
/*     */ 
/* 366 */     if (this.styleCount > 1) {
/* 367 */       writeBegingroup();
/* 368 */       writeControlWord("stylesheet");
/* 369 */       localObject1 = this.styleTable.keys();
/* 370 */       while (((Enumeration)localObject1).hasMoreElements()) {
/* 371 */         localObject2 = (Style)((Enumeration)localObject1).nextElement();
/* 372 */         int j = ((Integer)this.styleTable.get(localObject2)).intValue();
/* 373 */         writeBegingroup();
/* 374 */         localObject3 = (String)((Style)localObject2).getAttribute("style:type");
/* 375 */         if (localObject3 == null)
/* 376 */           localObject3 = "paragraph";
/* 377 */         if (((String)localObject3).equals("character")) {
/* 378 */           writeControlWord("*");
/* 379 */           writeControlWord("cs", j);
/* 380 */         } else if (((String)localObject3).equals("section")) {
/* 381 */           writeControlWord("*");
/* 382 */           writeControlWord("ds", j);
/*     */         } else {
/* 384 */           writeControlWord("s", j);
/*     */         }
/*     */ 
/* 387 */         AttributeSet localAttributeSet = ((Style)localObject2).getResolveParent();
/*     */         SimpleAttributeSet localSimpleAttributeSet;
/* 389 */         if (localAttributeSet == null)
/* 390 */           localSimpleAttributeSet = new SimpleAttributeSet();
/*     */         else {
/* 392 */           localSimpleAttributeSet = new SimpleAttributeSet(localAttributeSet);
/*     */         }
/*     */ 
/* 395 */         updateSectionAttributes(localSimpleAttributeSet, (AttributeSet)localObject2, false);
/* 396 */         updateParagraphAttributes(localSimpleAttributeSet, (AttributeSet)localObject2, false);
/* 397 */         updateCharacterAttributes(localSimpleAttributeSet, (AttributeSet)localObject2, false);
/*     */ 
/* 399 */         localAttributeSet = ((Style)localObject2).getResolveParent();
/* 400 */         if ((localAttributeSet != null) && ((localAttributeSet instanceof Style))) {
/* 401 */           localObject4 = (Integer)this.styleTable.get(localAttributeSet);
/* 402 */           if (localObject4 != null) {
/* 403 */             writeControlWord("sbasedon", ((Integer)localObject4).intValue());
/*     */           }
/*     */         }
/*     */ 
/* 407 */         Object localObject4 = (Style)((Style)localObject2).getAttribute("style:nextStyle");
/* 408 */         if (localObject4 != null) {
/* 409 */           localObject5 = (Integer)this.styleTable.get(localObject4);
/* 410 */           if (localObject5 != null) {
/* 411 */             writeControlWord("snext", ((Integer)localObject5).intValue());
/*     */           }
/*     */         }
/*     */ 
/* 415 */         Object localObject5 = (Boolean)((Style)localObject2).getAttribute("style:hidden");
/* 416 */         if ((localObject5 != null) && (((Boolean)localObject5).booleanValue())) {
/* 417 */           writeControlWord("shidden");
/*     */         }
/* 419 */         Boolean localBoolean = (Boolean)((Style)localObject2).getAttribute("style:additive");
/* 420 */         if ((localBoolean != null) && (localBoolean.booleanValue())) {
/* 421 */           writeControlWord("additive");
/*     */         }
/*     */ 
/* 424 */         writeText(((Style)localObject2).getName());
/* 425 */         writeText(";");
/* 426 */         writeEndgroup();
/*     */       }
/* 428 */       writeEndgroup();
/* 429 */       writeLineBreak();
/*     */     }
/*     */ 
/* 432 */     this.outputAttributes = new SimpleAttributeSet();
/*     */   }
/*     */ 
/*     */   void writeDocumentProperties(Document paramDocument)
/*     */     throws IOException
/*     */   {
/* 440 */     int j = 0;
/*     */ 
/* 442 */     for (int i = 0; i < RTFAttributes.attributes.length; i++) {
/* 443 */       RTFAttribute localRTFAttribute = RTFAttributes.attributes[i];
/* 444 */       if (localRTFAttribute.domain() == 3)
/*     */       {
/* 446 */         Object localObject = paramDocument.getProperty(localRTFAttribute.swingName());
/* 447 */         boolean bool = localRTFAttribute.writeValue(localObject, this, false);
/* 448 */         if (bool)
/* 449 */           j = 1;
/*     */       }
/*     */     }
/* 452 */     if (j != 0)
/* 453 */       writeLineBreak();
/*     */   }
/*     */ 
/*     */   public void writeRTFTrailer()
/*     */     throws IOException
/*     */   {
/* 459 */     writeEndgroup();
/* 460 */     writeLineBreak();
/*     */   }
/*     */ 
/*     */   protected void checkNumericControlWord(MutableAttributeSet paramMutableAttributeSet, AttributeSet paramAttributeSet, Object paramObject, String paramString, float paramFloat1, float paramFloat2)
/*     */     throws IOException
/*     */   {
/*     */     Object localObject;
/* 472 */     if ((localObject = attrDiff(paramMutableAttributeSet, paramAttributeSet, paramObject, MagicToken)) != null)
/*     */     {
/*     */       float f;
/* 475 */       if (localObject == MagicToken)
/* 476 */         f = paramFloat1;
/*     */       else
/* 478 */         f = ((Number)localObject).floatValue();
/* 479 */       writeControlWord(paramString, Math.round(f * paramFloat2));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void checkControlWord(MutableAttributeSet paramMutableAttributeSet, AttributeSet paramAttributeSet, RTFAttribute paramRTFAttribute)
/*     */     throws IOException
/*     */   {
/*     */     Object localObject;
/* 490 */     if ((localObject = attrDiff(paramMutableAttributeSet, paramAttributeSet, paramRTFAttribute.swingName(), MagicToken)) != null)
/*     */     {
/* 492 */       if (localObject == MagicToken)
/* 493 */         localObject = null;
/* 494 */       paramRTFAttribute.writeValue(localObject, this, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void checkControlWords(MutableAttributeSet paramMutableAttributeSet, AttributeSet paramAttributeSet, RTFAttribute[] paramArrayOfRTFAttribute, int paramInt)
/*     */     throws IOException
/*     */   {
/* 505 */     int j = paramArrayOfRTFAttribute.length;
/* 506 */     for (int i = 0; i < j; i++) {
/* 507 */       RTFAttribute localRTFAttribute = paramArrayOfRTFAttribute[i];
/* 508 */       if (localRTFAttribute.domain() == paramInt)
/* 509 */         checkControlWord(paramMutableAttributeSet, paramAttributeSet, localRTFAttribute);
/*     */     }
/*     */   }
/*     */ 
/*     */   void updateSectionAttributes(MutableAttributeSet paramMutableAttributeSet, AttributeSet paramAttributeSet, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 518 */     if (paramBoolean) {
/* 519 */       Object localObject = paramMutableAttributeSet.getAttribute("sectionStyle");
/* 520 */       Integer localInteger = findStyleNumber(paramAttributeSet, "section");
/* 521 */       if (localObject != localInteger) {
/* 522 */         if (localObject != null) {
/* 523 */           resetSectionAttributes(paramMutableAttributeSet);
/*     */         }
/* 525 */         if (localInteger != null) {
/* 526 */           writeControlWord("ds", ((Integer)localInteger).intValue());
/* 527 */           paramMutableAttributeSet.addAttribute("sectionStyle", localInteger);
/*     */         } else {
/* 529 */           paramMutableAttributeSet.removeAttribute("sectionStyle");
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 534 */     checkControlWords(paramMutableAttributeSet, paramAttributeSet, RTFAttributes.attributes, 2);
/*     */   }
/*     */ 
/*     */   protected void resetSectionAttributes(MutableAttributeSet paramMutableAttributeSet)
/*     */     throws IOException
/*     */   {
/* 541 */     writeControlWord("sectd");
/*     */ 
/* 544 */     int j = RTFAttributes.attributes.length;
/* 545 */     for (int i = 0; i < j; i++) {
/* 546 */       RTFAttribute localRTFAttribute = RTFAttributes.attributes[i];
/* 547 */       if (localRTFAttribute.domain() == 2) {
/* 548 */         localRTFAttribute.setDefault(paramMutableAttributeSet);
/*     */       }
/*     */     }
/* 551 */     paramMutableAttributeSet.removeAttribute("sectionStyle");
/*     */   }
/*     */ 
/*     */   void updateParagraphAttributes(MutableAttributeSet paramMutableAttributeSet, AttributeSet paramAttributeSet, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*     */     Object localObject1;
/*     */     Integer localInteger;
/* 567 */     if (paramBoolean) {
/* 568 */       localObject1 = paramMutableAttributeSet.getAttribute("paragraphStyle");
/* 569 */       localInteger = findStyleNumber(paramAttributeSet, "paragraph");
/* 570 */       if ((localObject1 != localInteger) && 
/* 571 */         (localObject1 != null)) {
/* 572 */         resetParagraphAttributes(paramMutableAttributeSet);
/* 573 */         localObject1 = null;
/*     */       }
/*     */     }
/*     */     else {
/* 577 */       localObject1 = null;
/* 578 */       localInteger = null;
/*     */     }
/*     */ 
/* 581 */     Object localObject2 = paramMutableAttributeSet.getAttribute("tabs");
/* 582 */     Object localObject3 = paramAttributeSet.getAttribute("tabs");
/* 583 */     if ((localObject2 != localObject3) && 
/* 584 */       (localObject2 != null)) {
/* 585 */       resetParagraphAttributes(paramMutableAttributeSet);
/* 586 */       localObject2 = null;
/* 587 */       localObject1 = null;
/*     */     }
/*     */ 
/* 591 */     if ((localObject1 != localInteger) && (localInteger != null)) {
/* 592 */       writeControlWord("s", ((Integer)localInteger).intValue());
/* 593 */       paramMutableAttributeSet.addAttribute("paragraphStyle", localInteger);
/*     */     }
/*     */ 
/* 596 */     checkControlWords(paramMutableAttributeSet, paramAttributeSet, RTFAttributes.attributes, 1);
/*     */ 
/* 599 */     if ((localObject2 != localObject3) && (localObject3 != null)) {
/* 600 */       TabStop[] arrayOfTabStop = (TabStop[])localObject3;
/*     */ 
/* 602 */       for (int i = 0; i < arrayOfTabStop.length; i++) {
/* 603 */         TabStop localTabStop = arrayOfTabStop[i];
/* 604 */         switch (localTabStop.getAlignment()) {
/*     */         case 0:
/*     */         case 5:
/* 607 */           break;
/*     */         case 1:
/* 609 */           writeControlWord("tqr");
/* 610 */           break;
/*     */         case 2:
/* 612 */           writeControlWord("tqc");
/* 613 */           break;
/*     */         case 4:
/* 615 */           writeControlWord("tqdec");
/*     */         case 3:
/*     */         }
/* 618 */         switch (localTabStop.getLeader()) {
/*     */         case 0:
/* 620 */           break;
/*     */         case 1:
/* 622 */           writeControlWord("tldot");
/* 623 */           break;
/*     */         case 2:
/* 625 */           writeControlWord("tlhyph");
/* 626 */           break;
/*     */         case 3:
/* 628 */           writeControlWord("tlul");
/* 629 */           break;
/*     */         case 4:
/* 631 */           writeControlWord("tlth");
/* 632 */           break;
/*     */         case 5:
/* 634 */           writeControlWord("tleq");
/*     */         }
/*     */ 
/* 637 */         int j = Math.round(20.0F * localTabStop.getPosition());
/* 638 */         if (localTabStop.getAlignment() == 5)
/* 639 */           writeControlWord("tb", j);
/*     */         else {
/* 641 */           writeControlWord("tx", j);
/*     */         }
/*     */       }
/* 644 */       paramMutableAttributeSet.addAttribute("tabs", arrayOfTabStop);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeParagraphElement(Element paramElement)
/*     */     throws IOException
/*     */   {
/* 651 */     updateParagraphAttributes(this.outputAttributes, paramElement.getAttributes(), true);
/*     */ 
/* 653 */     int i = paramElement.getElementCount();
/* 654 */     for (int j = 0; j < i; j++) {
/* 655 */       writeTextElement(paramElement.getElement(j));
/*     */     }
/*     */ 
/* 658 */     writeControlWord("par");
/* 659 */     writeLineBreak();
/*     */   }
/*     */ 
/*     */   protected void resetParagraphAttributes(MutableAttributeSet paramMutableAttributeSet)
/*     */     throws IOException
/*     */   {
/* 686 */     writeControlWord("pard");
/*     */ 
/* 688 */     paramMutableAttributeSet.addAttribute(StyleConstants.Alignment, Integer.valueOf(0));
/*     */ 
/* 691 */     int j = RTFAttributes.attributes.length;
/* 692 */     for (int i = 0; i < j; i++) {
/* 693 */       RTFAttribute localRTFAttribute = RTFAttributes.attributes[i];
/* 694 */       if (localRTFAttribute.domain() == 1) {
/* 695 */         localRTFAttribute.setDefault(paramMutableAttributeSet);
/*     */       }
/*     */     }
/* 698 */     paramMutableAttributeSet.removeAttribute("paragraphStyle");
/* 699 */     paramMutableAttributeSet.removeAttribute("tabs");
/*     */   }
/*     */ 
/*     */   void updateCharacterAttributes(MutableAttributeSet paramMutableAttributeSet, AttributeSet paramAttributeSet, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*     */     Object localObject2;
/* 709 */     if (paramBoolean) {
/* 710 */       localObject2 = paramMutableAttributeSet.getAttribute("characterStyle");
/* 711 */       Integer localInteger = findStyleNumber(paramAttributeSet, "character");
/*     */ 
/* 713 */       if (localObject2 != localInteger) {
/* 714 */         if (localObject2 != null) {
/* 715 */           resetCharacterAttributes(paramMutableAttributeSet);
/*     */         }
/* 717 */         if (localInteger != null) {
/* 718 */           writeControlWord("cs", ((Integer)localInteger).intValue());
/* 719 */           paramMutableAttributeSet.addAttribute("characterStyle", localInteger);
/*     */         } else {
/* 721 */           paramMutableAttributeSet.removeAttribute("characterStyle");
/*     */         }
/*     */       }
/*     */     }
/*     */     Object localObject1;
/* 726 */     if ((localObject1 = attrDiff(paramMutableAttributeSet, paramAttributeSet, StyleConstants.FontFamily, null)) != null)
/*     */     {
/* 728 */       localObject2 = (Integer)this.fontTable.get(localObject1);
/* 729 */       writeControlWord("f", ((Integer)localObject2).intValue());
/*     */     }
/*     */ 
/* 732 */     checkNumericControlWord(paramMutableAttributeSet, paramAttributeSet, StyleConstants.FontSize, "fs", 12.0F, 2.0F);
/*     */ 
/* 736 */     checkControlWords(paramMutableAttributeSet, paramAttributeSet, RTFAttributes.attributes, 0);
/*     */ 
/* 739 */     checkNumericControlWord(paramMutableAttributeSet, paramAttributeSet, StyleConstants.LineSpacing, "sl", 0.0F, 20.0F);
/*     */     int i;
/* 743 */     if ((localObject1 = attrDiff(paramMutableAttributeSet, paramAttributeSet, StyleConstants.Background, MagicToken)) != null)
/*     */     {
/* 746 */       if (localObject1 == MagicToken)
/* 747 */         i = 0;
/*     */       else
/* 749 */         i = ((Integer)this.colorTable.get(localObject1)).intValue();
/* 750 */       writeControlWord("cb", i);
/*     */     }
/*     */ 
/* 753 */     if ((localObject1 = attrDiff(paramMutableAttributeSet, paramAttributeSet, StyleConstants.Foreground, null)) != null)
/*     */     {
/* 756 */       if (localObject1 == MagicToken)
/* 757 */         i = 0;
/*     */       else
/* 759 */         i = ((Integer)this.colorTable.get(localObject1)).intValue();
/* 760 */       writeControlWord("cf", i);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void resetCharacterAttributes(MutableAttributeSet paramMutableAttributeSet)
/*     */     throws IOException
/*     */   {
/* 767 */     writeControlWord("plain");
/*     */ 
/* 770 */     int j = RTFAttributes.attributes.length;
/* 771 */     for (int i = 0; i < j; i++) {
/* 772 */       RTFAttribute localRTFAttribute = RTFAttributes.attributes[i];
/* 773 */       if (localRTFAttribute.domain() == 0) {
/* 774 */         localRTFAttribute.setDefault(paramMutableAttributeSet);
/*     */       }
/*     */     }
/* 777 */     StyleConstants.setFontFamily(paramMutableAttributeSet, "Helvetica");
/* 778 */     paramMutableAttributeSet.removeAttribute(StyleConstants.FontSize);
/* 779 */     paramMutableAttributeSet.removeAttribute(StyleConstants.Background);
/* 780 */     paramMutableAttributeSet.removeAttribute(StyleConstants.Foreground);
/* 781 */     paramMutableAttributeSet.removeAttribute(StyleConstants.LineSpacing);
/* 782 */     paramMutableAttributeSet.removeAttribute("characterStyle");
/*     */   }
/*     */ 
/*     */   public void writeTextElement(Element paramElement)
/*     */     throws IOException
/*     */   {
/* 788 */     updateCharacterAttributes(this.outputAttributes, paramElement.getAttributes(), true);
/*     */ 
/* 790 */     if (paramElement.isLeaf()) {
/*     */       try {
/* 792 */         paramElement.getDocument().getText(paramElement.getStartOffset(), paramElement.getEndOffset() - paramElement.getStartOffset(), this.workingSegment);
/*     */       }
/*     */       catch (BadLocationException localBadLocationException)
/*     */       {
/* 797 */         localBadLocationException.printStackTrace();
/* 798 */         throw new InternalError(localBadLocationException.getMessage());
/*     */       }
/* 800 */       writeText(this.workingSegment);
/*     */     } else {
/* 802 */       int i = paramElement.getElementCount();
/* 803 */       for (int j = 0; j < i; j++)
/* 804 */         writeTextElement(paramElement.getElement(j));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeText(Segment paramSegment)
/*     */     throws IOException
/*     */   {
/* 814 */     int i = paramSegment.offset;
/* 815 */     int j = i + paramSegment.count;
/* 816 */     char[] arrayOfChar = paramSegment.array;
/* 817 */     for (; i < j; i++)
/* 818 */       writeCharacter(arrayOfChar[i]);
/*     */   }
/*     */ 
/*     */   public void writeText(String paramString)
/*     */     throws IOException
/*     */   {
/* 826 */     int i = 0;
/* 827 */     int j = paramString.length();
/* 828 */     for (; i < j; i++)
/* 829 */       writeCharacter(paramString.charAt(i));
/*     */   }
/*     */ 
/*     */   public void writeRawString(String paramString)
/*     */     throws IOException
/*     */   {
/* 835 */     int i = paramString.length();
/* 836 */     for (int j = 0; j < i; j++)
/* 837 */       this.outputStream.write(paramString.charAt(j));
/*     */   }
/*     */ 
/*     */   public void writeControlWord(String paramString)
/*     */     throws IOException
/*     */   {
/* 843 */     this.outputStream.write(92);
/* 844 */     writeRawString(paramString);
/* 845 */     this.afterKeyword = true;
/*     */   }
/*     */ 
/*     */   public void writeControlWord(String paramString, int paramInt)
/*     */     throws IOException
/*     */   {
/* 851 */     this.outputStream.write(92);
/* 852 */     writeRawString(paramString);
/* 853 */     writeRawString(String.valueOf(paramInt));
/* 854 */     this.afterKeyword = true;
/*     */   }
/*     */ 
/*     */   public void writeBegingroup()
/*     */     throws IOException
/*     */   {
/* 860 */     this.outputStream.write(123);
/* 861 */     this.afterKeyword = false;
/*     */   }
/*     */ 
/*     */   public void writeEndgroup()
/*     */     throws IOException
/*     */   {
/* 867 */     this.outputStream.write(125);
/* 868 */     this.afterKeyword = false;
/*     */   }
/*     */ 
/*     */   public void writeCharacter(char paramChar)
/*     */     throws IOException
/*     */   {
/* 876 */     if (paramChar == ' ') {
/* 877 */       this.outputStream.write(92);
/* 878 */       this.outputStream.write(126);
/* 879 */       this.afterKeyword = false;
/* 880 */       return;
/*     */     }
/*     */ 
/* 883 */     if (paramChar == '\t') {
/* 884 */       writeControlWord("tab");
/* 885 */       return;
/*     */     }
/*     */ 
/* 888 */     if ((paramChar == '\n') || (paramChar == '\r'))
/*     */     {
/* 890 */       return;
/*     */     }
/*     */ 
/* 893 */     int i = convertCharacter(this.outputConversion, paramChar);
/*     */     int j;
/* 894 */     if (i == 0)
/*     */     {
/* 897 */       for (j = 0; j < textKeywords.length; j++) {
/* 898 */         if (textKeywords[j].character == paramChar) {
/* 899 */           writeControlWord(textKeywords[j].keyword);
/* 900 */           return;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 908 */       String str = approximationForUnicode(paramChar);
/* 909 */       if (str.length() != this.unicodeCount) {
/* 910 */         this.unicodeCount = str.length();
/* 911 */         writeControlWord("uc", this.unicodeCount);
/*     */       }
/* 913 */       writeControlWord("u", paramChar);
/* 914 */       writeRawString(" ");
/* 915 */       writeRawString(str);
/* 916 */       this.afterKeyword = false;
/* 917 */       return;
/*     */     }
/*     */ 
/* 920 */     if (i > 127)
/*     */     {
/* 922 */       this.outputStream.write(92);
/* 923 */       this.outputStream.write(39);
/* 924 */       j = (i & 0xF0) >>> 4;
/* 925 */       this.outputStream.write(hexdigits[j]);
/* 926 */       j = i & 0xF;
/* 927 */       this.outputStream.write(hexdigits[j]);
/* 928 */       this.afterKeyword = false;
/* 929 */       return;
/*     */     }
/*     */ 
/* 932 */     switch (i) {
/*     */     case 92:
/*     */     case 123:
/*     */     case 125:
/* 936 */       this.outputStream.write(92);
/* 937 */       this.afterKeyword = false;
/*     */     }
/*     */ 
/* 940 */     if (this.afterKeyword) {
/* 941 */       this.outputStream.write(32);
/* 942 */       this.afterKeyword = false;
/*     */     }
/* 944 */     this.outputStream.write(i);
/*     */   }
/*     */ 
/*     */   String approximationForUnicode(char paramChar)
/*     */   {
/* 953 */     return "?";
/*     */   }
/*     */ 
/*     */   static int[] outputConversionFromTranslationTable(char[] paramArrayOfChar)
/*     */   {
/* 964 */     int[] arrayOfInt = new int[2 * paramArrayOfChar.length];
/*     */ 
/* 968 */     for (int i = 0; i < paramArrayOfChar.length; i++) {
/* 969 */       arrayOfInt[(i * 2)] = paramArrayOfChar[i];
/* 970 */       arrayOfInt[(i * 2 + 1)] = i;
/*     */     }
/*     */ 
/* 973 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   static int[] outputConversionForName(String paramString)
/*     */     throws IOException
/*     */   {
/* 979 */     char[] arrayOfChar = (char[])RTFReader.getCharacterSet(paramString);
/* 980 */     return outputConversionFromTranslationTable(arrayOfChar);
/*     */   }
/*     */ 
/*     */   protected static int convertCharacter(int[] paramArrayOfInt, char paramChar)
/*     */   {
/* 993 */     for (int i = 0; i < paramArrayOfInt.length; i += 2) {
/* 994 */       if (paramArrayOfInt[i] == paramChar) {
/* 995 */         return paramArrayOfInt[(i + 1)];
/*     */       }
/*     */     }
/* 998 */     return 0;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  99 */     Dictionary localDictionary = RTFReader.textKeywords;
/* 100 */     Enumeration localEnumeration = localDictionary.keys();
/* 101 */     Vector localVector = new Vector();
/* 102 */     while (localEnumeration.hasMoreElements()) {
/* 103 */       CharacterKeywordPair localCharacterKeywordPair = new CharacterKeywordPair();
/* 104 */       localCharacterKeywordPair.keyword = ((String)localEnumeration.nextElement());
/* 105 */       localCharacterKeywordPair.character = ((String)localDictionary.get(localCharacterKeywordPair.keyword)).charAt(0);
/* 106 */       localVector.addElement(localCharacterKeywordPair);
/*     */     }
/* 108 */     textKeywords = new CharacterKeywordPair[localVector.size()];
/* 109 */     localVector.copyInto(textKeywords);
/*     */   }
/*     */ 
/*     */   static class CharacterKeywordPair
/*     */   {
/*     */     public char character;
/*     */     public String keyword;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.rtf.RTFGenerator
 * JD-Core Version:    0.6.2
 */