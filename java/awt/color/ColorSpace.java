/*     */ package java.awt.color;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class ColorSpace
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -409452704308689724L;
/*     */   private int type;
/*     */   private int numComponents;
/* 104 */   private transient String[] compName = null;
/*     */   private static ColorSpace sRGBspace;
/*     */   private static ColorSpace XYZspace;
/*     */   private static ColorSpace PYCCspace;
/*     */   private static ColorSpace GRAYspace;
/*     */   private static ColorSpace LINEAR_RGBspace;
/*     */   public static final int TYPE_XYZ = 0;
/*     */   public static final int TYPE_Lab = 1;
/*     */   public static final int TYPE_Luv = 2;
/*     */   public static final int TYPE_YCbCr = 3;
/*     */   public static final int TYPE_Yxy = 4;
/*     */   public static final int TYPE_RGB = 5;
/*     */   public static final int TYPE_GRAY = 6;
/*     */   public static final int TYPE_HSV = 7;
/*     */   public static final int TYPE_HLS = 8;
/*     */   public static final int TYPE_CMYK = 9;
/*     */   public static final int TYPE_CMY = 11;
/*     */   public static final int TYPE_2CLR = 12;
/*     */   public static final int TYPE_3CLR = 13;
/*     */   public static final int TYPE_4CLR = 14;
/*     */   public static final int TYPE_5CLR = 15;
/*     */   public static final int TYPE_6CLR = 16;
/*     */   public static final int TYPE_7CLR = 17;
/*     */   public static final int TYPE_8CLR = 18;
/*     */   public static final int TYPE_9CLR = 19;
/*     */   public static final int TYPE_ACLR = 20;
/*     */   public static final int TYPE_BCLR = 21;
/*     */   public static final int TYPE_CCLR = 22;
/*     */   public static final int TYPE_DCLR = 23;
/*     */   public static final int TYPE_ECLR = 24;
/*     */   public static final int TYPE_FCLR = 25;
/*     */   public static final int CS_sRGB = 1000;
/*     */   public static final int CS_LINEAR_RGB = 1004;
/*     */   public static final int CS_CIEXYZ = 1001;
/*     */   public static final int CS_PYCC = 1002;
/*     */   public static final int CS_GRAY = 1003;
/*     */ 
/*     */   protected ColorSpace(int paramInt1, int paramInt2)
/*     */   {
/* 275 */     this.type = paramInt1;
/* 276 */     this.numComponents = paramInt2;
/*     */   }
/*     */ 
/*     */   public static ColorSpace getInstance(int paramInt)
/*     */   {
/*     */     ICC_Profile localICC_Profile;
/*     */     ColorSpace localColorSpace;
/* 294 */     switch (paramInt) {
/*     */     case 1000:
/* 296 */       synchronized (ColorSpace.class) {
/* 297 */         if (sRGBspace == null) {
/* 298 */           localICC_Profile = ICC_Profile.getInstance(1000);
/* 299 */           sRGBspace = new ICC_ColorSpace(localICC_Profile);
/*     */         }
/*     */ 
/* 302 */         localColorSpace = sRGBspace;
/*     */       }
/* 304 */       break;
/*     */     case 1001:
/* 307 */       synchronized (ColorSpace.class) {
/* 308 */         if (XYZspace == null) {
/* 309 */           localICC_Profile = ICC_Profile.getInstance(1001);
/*     */ 
/* 311 */           XYZspace = new ICC_ColorSpace(localICC_Profile);
/*     */         }
/*     */ 
/* 314 */         localColorSpace = XYZspace;
/*     */       }
/* 316 */       break;
/*     */     case 1002:
/* 319 */       synchronized (ColorSpace.class) {
/* 320 */         if (PYCCspace == null) {
/* 321 */           localICC_Profile = ICC_Profile.getInstance(1002);
/* 322 */           PYCCspace = new ICC_ColorSpace(localICC_Profile);
/*     */         }
/*     */ 
/* 325 */         localColorSpace = PYCCspace;
/*     */       }
/* 327 */       break;
/*     */     case 1003:
/* 331 */       synchronized (ColorSpace.class) {
/* 332 */         if (GRAYspace == null) {
/* 333 */           localICC_Profile = ICC_Profile.getInstance(1003);
/* 334 */           GRAYspace = new ICC_ColorSpace(localICC_Profile);
/*     */ 
/* 336 */           sun.java2d.cmm.CMSManager.GRAYspace = GRAYspace;
/*     */         }
/*     */ 
/* 339 */         localColorSpace = GRAYspace;
/*     */       }
/* 341 */       break;
/*     */     case 1004:
/* 345 */       synchronized (ColorSpace.class) {
/* 346 */         if (LINEAR_RGBspace == null) {
/* 347 */           localICC_Profile = ICC_Profile.getInstance(1004);
/*     */ 
/* 349 */           LINEAR_RGBspace = new ICC_ColorSpace(localICC_Profile);
/*     */ 
/* 351 */           sun.java2d.cmm.CMSManager.LINEAR_RGBspace = LINEAR_RGBspace;
/*     */         }
/*     */ 
/* 354 */         localColorSpace = LINEAR_RGBspace;
/*     */       }
/* 356 */       break;
/*     */     default:
/* 360 */       throw new IllegalArgumentException("Unknown color space");
/*     */     }
/*     */ 
/* 363 */     return localColorSpace;
/*     */   }
/*     */ 
/*     */   public boolean isCS_sRGB()
/*     */   {
/* 374 */     return this == sRGBspace;
/*     */   }
/*     */ 
/*     */   public abstract float[] toRGB(float[] paramArrayOfFloat);
/*     */ 
/*     */   public abstract float[] fromRGB(float[] paramArrayOfFloat);
/*     */ 
/*     */   public abstract float[] toCIEXYZ(float[] paramArrayOfFloat);
/*     */ 
/*     */   public abstract float[] fromCIEXYZ(float[] paramArrayOfFloat);
/*     */ 
/*     */   public int getType()
/*     */   {
/* 492 */     return this.type;
/*     */   }
/*     */ 
/*     */   public int getNumComponents()
/*     */   {
/* 500 */     return this.numComponents;
/*     */   }
/*     */ 
/*     */   public String getName(int paramInt)
/*     */   {
/* 513 */     if ((paramInt < 0) || (paramInt > this.numComponents - 1)) {
/* 514 */       throw new IllegalArgumentException("Component index out of range: " + paramInt);
/*     */     }
/*     */ 
/* 518 */     if (this.compName == null) {
/* 519 */       switch (this.type) {
/*     */       case 0:
/* 521 */         this.compName = new String[] { "X", "Y", "Z" };
/* 522 */         break;
/*     */       case 1:
/* 524 */         this.compName = new String[] { "L", "a", "b" };
/* 525 */         break;
/*     */       case 2:
/* 527 */         this.compName = new String[] { "L", "u", "v" };
/* 528 */         break;
/*     */       case 3:
/* 530 */         this.compName = new String[] { "Y", "Cb", "Cr" };
/* 531 */         break;
/*     */       case 4:
/* 533 */         this.compName = new String[] { "Y", "x", "y" };
/* 534 */         break;
/*     */       case 5:
/* 536 */         this.compName = new String[] { "Red", "Green", "Blue" };
/* 537 */         break;
/*     */       case 6:
/* 539 */         this.compName = new String[] { "Gray" };
/* 540 */         break;
/*     */       case 7:
/* 542 */         this.compName = new String[] { "Hue", "Saturation", "Value" };
/* 543 */         break;
/*     */       case 8:
/* 545 */         this.compName = new String[] { "Hue", "Lightness", "Saturation" };
/*     */ 
/* 547 */         break;
/*     */       case 9:
/* 549 */         this.compName = new String[] { "Cyan", "Magenta", "Yellow", "Black" };
/*     */ 
/* 551 */         break;
/*     */       case 11:
/* 553 */         this.compName = new String[] { "Cyan", "Magenta", "Yellow" };
/* 554 */         break;
/*     */       case 10:
/*     */       default:
/* 556 */         String[] arrayOfString = new String[this.numComponents];
/* 557 */         for (int i = 0; i < arrayOfString.length; i++) {
/* 558 */           arrayOfString[i] = ("Unnamed color component(" + i + ")");
/*     */         }
/* 560 */         this.compName = arrayOfString;
/*     */       }
/*     */     }
/* 563 */     return this.compName[paramInt];
/*     */   }
/*     */ 
/*     */   public float getMinValue(int paramInt)
/*     */   {
/* 579 */     if ((paramInt < 0) || (paramInt > this.numComponents - 1)) {
/* 580 */       throw new IllegalArgumentException("Component index out of range: " + paramInt);
/*     */     }
/*     */ 
/* 583 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public float getMaxValue(int paramInt)
/*     */   {
/* 599 */     if ((paramInt < 0) || (paramInt > this.numComponents - 1)) {
/* 600 */       throw new IllegalArgumentException("Component index out of range: " + paramInt);
/*     */     }
/*     */ 
/* 603 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   static boolean isCS_CIEXYZ(ColorSpace paramColorSpace)
/*     */   {
/* 609 */     return paramColorSpace == XYZspace;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.color.ColorSpace
 * JD-Core Version:    0.6.2
 */