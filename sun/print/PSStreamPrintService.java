/*     */ package sun.print;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.util.Locale;
/*     */ import javax.print.DocFlavor;
/*     */ import javax.print.DocFlavor.SERVICE_FORMATTED;
/*     */ import javax.print.DocPrintJob;
/*     */ import javax.print.ServiceUIFactory;
/*     */ import javax.print.StreamPrintService;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.AttributeSet;
/*     */ import javax.print.attribute.AttributeSetUtilities;
/*     */ import javax.print.attribute.HashAttributeSet;
/*     */ import javax.print.attribute.HashPrintServiceAttributeSet;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ import javax.print.attribute.PrintServiceAttributeSet;
/*     */ import javax.print.attribute.standard.Chromaticity;
/*     */ import javax.print.attribute.standard.ColorSupported;
/*     */ import javax.print.attribute.standard.Copies;
/*     */ import javax.print.attribute.standard.CopiesSupported;
/*     */ import javax.print.attribute.standard.Fidelity;
/*     */ import javax.print.attribute.standard.JobName;
/*     */ import javax.print.attribute.standard.Media;
/*     */ import javax.print.attribute.standard.MediaPrintableArea;
/*     */ import javax.print.attribute.standard.MediaSize;
/*     */ import javax.print.attribute.standard.MediaSize.ISO;
/*     */ import javax.print.attribute.standard.MediaSize.NA;
/*     */ import javax.print.attribute.standard.MediaSizeName;
/*     */ import javax.print.attribute.standard.OrientationRequested;
/*     */ import javax.print.attribute.standard.PageRanges;
/*     */ import javax.print.attribute.standard.RequestingUserName;
/*     */ import javax.print.attribute.standard.SheetCollate;
/*     */ import javax.print.attribute.standard.Sides;
/*     */ import javax.print.event.PrintServiceAttributeListener;
/*     */ 
/*     */ public class PSStreamPrintService extends StreamPrintService
/*     */   implements SunPrinterJobService
/*     */ {
/*  65 */   private static final Class[] suppAttrCats = { Chromaticity.class, Copies.class, Fidelity.class, JobName.class, Media.class, MediaPrintableArea.class, OrientationRequested.class, PageRanges.class, RequestingUserName.class, SheetCollate.class, Sides.class };
/*     */ 
/*  79 */   private static int MAXCOPIES = 1000;
/*     */ 
/*  81 */   private static final MediaSizeName[] mediaSizes = { MediaSizeName.NA_LETTER, MediaSizeName.TABLOID, MediaSizeName.LEDGER, MediaSizeName.NA_LEGAL, MediaSizeName.EXECUTIVE, MediaSizeName.ISO_A3, MediaSizeName.ISO_A4, MediaSizeName.ISO_A5, MediaSizeName.ISO_B4, MediaSizeName.ISO_B5 };
/*     */ 
/*     */   public PSStreamPrintService(OutputStream paramOutputStream)
/*     */   {
/*  95 */     super(paramOutputStream);
/*     */   }
/*     */ 
/*     */   public String getOutputFormat() {
/*  99 */     return "application/postscript";
/*     */   }
/*     */ 
/*     */   public DocFlavor[] getSupportedDocFlavors()
/*     */   {
/* 104 */     return PSStreamPrinterFactory.getFlavors();
/*     */   }
/*     */ 
/*     */   public DocPrintJob createPrintJob() {
/* 108 */     return new PSStreamPrintJob(this);
/*     */   }
/*     */ 
/*     */   public boolean usesClass(Class paramClass) {
/* 112 */     return paramClass == PSPrinterJob.class;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 116 */     return "Postscript output";
/*     */   }
/*     */ 
/*     */   public void addPrintServiceAttributeListener(PrintServiceAttributeListener paramPrintServiceAttributeListener)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void removePrintServiceAttributeListener(PrintServiceAttributeListener paramPrintServiceAttributeListener)
/*     */   {
/*     */   }
/*     */ 
/*     */   public <T extends PrintServiceAttribute> T getAttribute(Class<T> paramClass)
/*     */   {
/* 133 */     if (paramClass == null) {
/* 134 */       throw new NullPointerException("category");
/*     */     }
/* 136 */     if (!PrintServiceAttribute.class.isAssignableFrom(paramClass)) {
/* 137 */       throw new IllegalArgumentException("Not a PrintServiceAttribute");
/*     */     }
/* 139 */     if (paramClass == ColorSupported.class) {
/* 140 */       return ColorSupported.SUPPORTED;
/*     */     }
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */   public PrintServiceAttributeSet getAttributes() {
/* 146 */     HashPrintServiceAttributeSet localHashPrintServiceAttributeSet = new HashPrintServiceAttributeSet();
/* 147 */     localHashPrintServiceAttributeSet.add(ColorSupported.SUPPORTED);
/*     */ 
/* 149 */     return AttributeSetUtilities.unmodifiableView(localHashPrintServiceAttributeSet);
/*     */   }
/*     */ 
/*     */   public boolean isDocFlavorSupported(DocFlavor paramDocFlavor) {
/* 153 */     DocFlavor[] arrayOfDocFlavor = getSupportedDocFlavors();
/* 154 */     for (int i = 0; i < arrayOfDocFlavor.length; i++) {
/* 155 */       if (paramDocFlavor.equals(arrayOfDocFlavor[i])) {
/* 156 */         return true;
/*     */       }
/*     */     }
/* 159 */     return false;
/*     */   }
/*     */ 
/*     */   public Class<?>[] getSupportedAttributeCategories()
/*     */   {
/* 164 */     Class[] arrayOfClass = new Class[suppAttrCats.length];
/* 165 */     System.arraycopy(suppAttrCats, 0, arrayOfClass, 0, arrayOfClass.length);
/* 166 */     return arrayOfClass;
/*     */   }
/*     */ 
/*     */   public boolean isAttributeCategorySupported(Class<? extends Attribute> paramClass)
/*     */   {
/* 172 */     if (paramClass == null) {
/* 173 */       throw new NullPointerException("null category");
/*     */     }
/* 175 */     if (!Attribute.class.isAssignableFrom(paramClass)) {
/* 176 */       throw new IllegalArgumentException(paramClass + " is not an Attribute");
/*     */     }
/*     */ 
/* 180 */     for (int i = 0; i < suppAttrCats.length; i++) {
/* 181 */       if (paramClass == suppAttrCats[i]) {
/* 182 */         return true;
/*     */       }
/*     */     }
/* 185 */     return false;
/*     */   }
/*     */ 
/*     */   public Object getDefaultAttributeValue(Class<? extends Attribute> paramClass)
/*     */   {
/* 192 */     if (paramClass == null) {
/* 193 */       throw new NullPointerException("null category");
/*     */     }
/* 195 */     if (!Attribute.class.isAssignableFrom(paramClass)) {
/* 196 */       throw new IllegalArgumentException(paramClass + " is not an Attribute");
/*     */     }
/*     */ 
/* 200 */     if (!isAttributeCategorySupported(paramClass)) {
/* 201 */       return null;
/*     */     }
/*     */ 
/* 204 */     if (paramClass == Copies.class)
/* 205 */       return new Copies(1);
/* 206 */     if (paramClass == Chromaticity.class)
/* 207 */       return Chromaticity.COLOR;
/* 208 */     if (paramClass == Fidelity.class)
/* 209 */       return Fidelity.FIDELITY_FALSE;
/*     */     String str;
/* 210 */     if (paramClass == Media.class) {
/* 211 */       str = Locale.getDefault().getCountry();
/* 212 */       if ((str != null) && ((str.equals("")) || (str.equals(Locale.US.getCountry())) || (str.equals(Locale.CANADA.getCountry()))))
/*     */       {
/* 216 */         return MediaSizeName.NA_LETTER;
/*     */       }
/* 218 */       return MediaSizeName.ISO_A4;
/*     */     }
/* 220 */     if (paramClass == MediaPrintableArea.class) {
/* 221 */       str = Locale.getDefault().getCountry();
/*     */ 
/* 223 */       float f3 = 0.5F;
/*     */       float f1;
/*     */       float f2;
/* 224 */       if ((str != null) && ((str.equals("")) || (str.equals(Locale.US.getCountry())) || (str.equals(Locale.CANADA.getCountry()))))
/*     */       {
/* 228 */         f1 = MediaSize.NA.LETTER.getX(25400) - 2.0F * f3;
/* 229 */         f2 = MediaSize.NA.LETTER.getY(25400) - 2.0F * f3;
/*     */       } else {
/* 231 */         f1 = MediaSize.ISO.A4.getX(25400) - 2.0F * f3;
/* 232 */         f2 = MediaSize.ISO.A4.getY(25400) - 2.0F * f3;
/*     */       }
/* 234 */       return new MediaPrintableArea(f3, f3, f1, f2, 25400);
/*     */     }
/* 236 */     if (paramClass == OrientationRequested.class)
/* 237 */       return OrientationRequested.PORTRAIT;
/* 238 */     if (paramClass == PageRanges.class)
/* 239 */       return new PageRanges(1, 2147483647);
/* 240 */     if (paramClass == SheetCollate.class)
/* 241 */       return SheetCollate.UNCOLLATED;
/* 242 */     if (paramClass == Sides.class) {
/* 243 */       return Sides.ONE_SIDED;
/*     */     }
/*     */ 
/* 246 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getSupportedAttributeValues(Class<? extends Attribute> paramClass, DocFlavor paramDocFlavor, AttributeSet paramAttributeSet)
/*     */   {
/* 256 */     if (paramClass == null) {
/* 257 */       throw new NullPointerException("null category");
/*     */     }
/* 259 */     if (!Attribute.class.isAssignableFrom(paramClass)) {
/* 260 */       throw new IllegalArgumentException(paramClass + " does not implement Attribute");
/*     */     }
/*     */ 
/* 263 */     if ((paramDocFlavor != null) && (!isDocFlavorSupported(paramDocFlavor))) {
/* 264 */       throw new IllegalArgumentException(paramDocFlavor + " is an unsupported flavor");
/*     */     }
/*     */ 
/* 268 */     if (!isAttributeCategorySupported(paramClass))
/* 269 */       return null;
/*     */     Object localObject1;
/* 272 */     if (paramClass == Chromaticity.class) {
/* 273 */       localObject1 = new Chromaticity[1];
/* 274 */       localObject1[0] = Chromaticity.COLOR;
/*     */ 
/* 276 */       return localObject1;
/* 277 */     }if (paramClass == JobName.class)
/* 278 */       return new JobName("", null);
/* 279 */     if (paramClass == RequestingUserName.class)
/* 280 */       return new RequestingUserName("", null);
/* 281 */     if (paramClass == OrientationRequested.class) {
/* 282 */       if ((paramDocFlavor == null) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)))
/*     */       {
/* 285 */         localObject1 = new OrientationRequested[3];
/* 286 */         localObject1[0] = OrientationRequested.PORTRAIT;
/* 287 */         localObject1[1] = OrientationRequested.LANDSCAPE;
/* 288 */         localObject1[2] = OrientationRequested.REVERSE_LANDSCAPE;
/* 289 */         return localObject1;
/*     */       }
/* 291 */       return null;
/*     */     }
/* 293 */     if ((paramClass == Copies.class) || (paramClass == CopiesSupported.class))
/*     */     {
/* 295 */       return new CopiesSupported(1, MAXCOPIES);
/* 296 */     }if (paramClass == Media.class) {
/* 297 */       localObject1 = new Media[mediaSizes.length];
/* 298 */       System.arraycopy(mediaSizes, 0, localObject1, 0, mediaSizes.length);
/* 299 */       return localObject1;
/* 300 */     }if (paramClass == Fidelity.class) {
/* 301 */       localObject1 = new Fidelity[2];
/* 302 */       localObject1[0] = Fidelity.FIDELITY_FALSE;
/* 303 */       localObject1[1] = Fidelity.FIDELITY_TRUE;
/* 304 */       return localObject1;
/* 305 */     }if (paramClass == MediaPrintableArea.class) {
/* 306 */       if (paramAttributeSet == null) {
/* 307 */         return null;
/*     */       }
/* 309 */       localObject1 = (MediaSize)paramAttributeSet.get(MediaSize.class);
/* 310 */       if (localObject1 == null) {
/* 311 */         localObject2 = (Media)paramAttributeSet.get(Media.class);
/* 312 */         if ((localObject2 != null) && ((localObject2 instanceof MediaSizeName))) {
/* 313 */           MediaSizeName localMediaSizeName = (MediaSizeName)localObject2;
/* 314 */           localObject1 = MediaSize.getMediaSizeForName(localMediaSizeName);
/*     */         }
/*     */       }
/* 317 */       if (localObject1 == null) {
/* 318 */         return null;
/*     */       }
/* 320 */       Object localObject2 = new MediaPrintableArea[1];
/* 321 */       float f1 = ((MediaSize)localObject1).getX(25400);
/* 322 */       float f2 = ((MediaSize)localObject1).getY(25400);
/*     */ 
/* 326 */       float f3 = 0.5F;
/* 327 */       float f4 = 0.5F;
/* 328 */       if (f1 < 5.0F) {
/* 329 */         f3 = f1 / 10.0F;
/*     */       }
/* 331 */       if (f2 < 5.0F) {
/* 332 */         f4 = f2 / 10.0F;
/*     */       }
/* 334 */       localObject2[0] = new MediaPrintableArea(f3, f4, f1 - 2.0F * f3, f2 - 2.0F * f4, 25400);
/*     */ 
/* 338 */       return localObject2;
/*     */     }
/* 340 */     if (paramClass == PageRanges.class) {
/* 341 */       if ((paramDocFlavor == null) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)))
/*     */       {
/* 344 */         localObject1 = new PageRanges[1];
/* 345 */         localObject1[0] = new PageRanges(1, 2147483647);
/* 346 */         return localObject1;
/*     */       }
/* 348 */       return null;
/*     */     }
/* 350 */     if (paramClass == SheetCollate.class) {
/* 351 */       if ((paramDocFlavor == null) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)))
/*     */       {
/* 354 */         localObject1 = new SheetCollate[2];
/* 355 */         localObject1[0] = SheetCollate.UNCOLLATED;
/* 356 */         localObject1[1] = SheetCollate.COLLATED;
/* 357 */         return localObject1;
/*     */       }
/* 359 */       localObject1 = new SheetCollate[1];
/* 360 */       localObject1[0] = SheetCollate.UNCOLLATED;
/* 361 */       return localObject1;
/*     */     }
/* 363 */     if (paramClass == Sides.class) {
/* 364 */       if ((paramDocFlavor == null) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)))
/*     */       {
/* 367 */         localObject1 = new Sides[3];
/* 368 */         localObject1[0] = Sides.ONE_SIDED;
/* 369 */         localObject1[1] = Sides.TWO_SIDED_LONG_EDGE;
/* 370 */         localObject1[2] = Sides.TWO_SIDED_SHORT_EDGE;
/* 371 */         return localObject1;
/*     */       }
/* 373 */       return null;
/*     */     }
/*     */ 
/* 376 */     return null;
/*     */   }
/*     */ 
/*     */   private boolean isSupportedCopies(Copies paramCopies)
/*     */   {
/* 381 */     int i = paramCopies.getValue();
/* 382 */     return (i > 0) && (i < MAXCOPIES);
/*     */   }
/*     */ 
/*     */   private boolean isSupportedMedia(MediaSizeName paramMediaSizeName) {
/* 386 */     for (int i = 0; i < mediaSizes.length; i++) {
/* 387 */       if (paramMediaSizeName.equals(mediaSizes[i])) {
/* 388 */         return true;
/*     */       }
/*     */     }
/* 391 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isAttributeValueSupported(Attribute paramAttribute, DocFlavor paramDocFlavor, AttributeSet paramAttributeSet)
/*     */   {
/* 397 */     if (paramAttribute == null) {
/* 398 */       throw new NullPointerException("null attribute");
/*     */     }
/* 400 */     if ((paramDocFlavor != null) && (!isDocFlavorSupported(paramDocFlavor))) {
/* 401 */       throw new IllegalArgumentException(paramDocFlavor + " is an unsupported flavor");
/*     */     }
/*     */ 
/* 404 */     Class localClass = paramAttribute.getCategory();
/* 405 */     if (!isAttributeCategorySupported(localClass)) {
/* 406 */       return false;
/*     */     }
/* 408 */     if (paramAttribute.getCategory() == Chromaticity.class) {
/* 409 */       return paramAttribute == Chromaticity.COLOR;
/*     */     }
/* 411 */     if (paramAttribute.getCategory() == Copies.class)
/* 412 */       return isSupportedCopies((Copies)paramAttribute);
/* 413 */     if ((paramAttribute.getCategory() == Media.class) && ((paramAttribute instanceof MediaSizeName)))
/*     */     {
/* 415 */       return isSupportedMedia((MediaSizeName)paramAttribute);
/* 416 */     }if (paramAttribute.getCategory() == OrientationRequested.class) {
/* 417 */       if ((paramAttribute == OrientationRequested.REVERSE_PORTRAIT) || ((paramDocFlavor != null) && (!paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) && (!paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE))))
/*     */       {
/* 421 */         return false;
/*     */       }
/* 423 */     } else if (paramAttribute.getCategory() == PageRanges.class) {
/* 424 */       if ((paramDocFlavor != null) && (!paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) && (!paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)))
/*     */       {
/* 427 */         return false;
/*     */       }
/* 429 */     } else if (paramAttribute.getCategory() == SheetCollate.class) {
/* 430 */       if ((paramDocFlavor != null) && (!paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) && (!paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)))
/*     */       {
/* 433 */         return false;
/*     */       }
/* 435 */     } else if ((paramAttribute.getCategory() == Sides.class) && 
/* 436 */       (paramDocFlavor != null) && (!paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) && (!paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)))
/*     */     {
/* 439 */       return false;
/*     */     }
/*     */ 
/* 442 */     return true;
/*     */   }
/*     */ 
/*     */   public AttributeSet getUnsupportedAttributes(DocFlavor paramDocFlavor, AttributeSet paramAttributeSet)
/*     */   {
/* 448 */     if ((paramDocFlavor != null) && (!isDocFlavorSupported(paramDocFlavor))) {
/* 449 */       throw new IllegalArgumentException("flavor " + paramDocFlavor + "is not supported");
/*     */     }
/*     */ 
/* 453 */     if (paramAttributeSet == null) {
/* 454 */       return null;
/*     */     }
/*     */ 
/* 458 */     HashAttributeSet localHashAttributeSet = new HashAttributeSet();
/* 459 */     Attribute[] arrayOfAttribute = paramAttributeSet.toArray();
/* 460 */     for (int i = 0; i < arrayOfAttribute.length; i++)
/*     */       try {
/* 462 */         Attribute localAttribute = arrayOfAttribute[i];
/* 463 */         if (!isAttributeCategorySupported(localAttribute.getCategory()))
/* 464 */           localHashAttributeSet.add(localAttribute);
/* 465 */         else if (!isAttributeValueSupported(localAttribute, paramDocFlavor, paramAttributeSet))
/*     */         {
/* 467 */           localHashAttributeSet.add(localAttribute);
/*     */         }
/*     */       }
/*     */       catch (ClassCastException localClassCastException) {
/*     */       }
/* 472 */     if (localHashAttributeSet.isEmpty()) {
/* 473 */       return null;
/*     */     }
/* 475 */     return localHashAttributeSet;
/*     */   }
/*     */ 
/*     */   public ServiceUIFactory getServiceUIFactory()
/*     */   {
/* 480 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 484 */     return "PSStreamPrintService: " + getName();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 491 */     return (paramObject == this) || (((paramObject instanceof PSStreamPrintService)) && (((PSStreamPrintService)paramObject).getName().equals(getName())));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 497 */     return getClass().hashCode() + getName().hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.PSStreamPrintService
 * JD-Core Version:    0.6.2
 */