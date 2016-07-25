/*     */ package java.awt.print;
/*     */ 
/*     */ public class PageFormat
/*     */   implements Cloneable
/*     */ {
/*     */   public static final int LANDSCAPE = 0;
/*     */   public static final int PORTRAIT = 1;
/*     */   public static final int REVERSE_LANDSCAPE = 2;
/*     */   private Paper mPaper;
/*  75 */   private int mOrientation = 1;
/*     */ 
/*     */   public PageFormat()
/*     */   {
/*  85 */     this.mPaper = new Paper();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     PageFormat localPageFormat;
/*     */     try
/*     */     {
/*  99 */       localPageFormat = (PageFormat)super.clone();
/* 100 */       localPageFormat.mPaper = ((Paper)this.mPaper.clone());
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 103 */       localCloneNotSupportedException.printStackTrace();
/* 104 */       localPageFormat = null;
/*     */     }
/*     */ 
/* 107 */     return localPageFormat;
/*     */   }
/*     */ 
/*     */   public double getWidth()
/*     */   {
/* 119 */     int i = getOrientation();
/*     */     double d;
/* 121 */     if (i == 1)
/* 122 */       d = this.mPaper.getWidth();
/*     */     else {
/* 124 */       d = this.mPaper.getHeight();
/*     */     }
/*     */ 
/* 127 */     return d;
/*     */   }
/*     */ 
/*     */   public double getHeight()
/*     */   {
/* 138 */     int i = getOrientation();
/*     */     double d;
/* 140 */     if (i == 1)
/* 141 */       d = this.mPaper.getHeight();
/*     */     else {
/* 143 */       d = this.mPaper.getWidth();
/*     */     }
/*     */ 
/* 146 */     return d;
/*     */   }
/*     */ 
/*     */   public double getImageableX()
/*     */   {
/*     */     double d;
/* 162 */     switch (getOrientation())
/*     */     {
/*     */     case 0:
/* 165 */       d = this.mPaper.getHeight() - (this.mPaper.getImageableY() + this.mPaper.getImageableHeight());
/*     */ 
/* 167 */       break;
/*     */     case 1:
/* 170 */       d = this.mPaper.getImageableX();
/* 171 */       break;
/*     */     case 2:
/* 174 */       d = this.mPaper.getImageableY();
/* 175 */       break;
/*     */     default:
/* 181 */       throw new InternalError("unrecognized orientation");
/*     */     }
/*     */ 
/* 185 */     return d;
/*     */   }
/*     */ 
/*     */   public double getImageableY()
/*     */   {
/*     */     double d;
/* 201 */     switch (getOrientation())
/*     */     {
/*     */     case 0:
/* 204 */       d = this.mPaper.getImageableX();
/* 205 */       break;
/*     */     case 1:
/* 208 */       d = this.mPaper.getImageableY();
/* 209 */       break;
/*     */     case 2:
/* 212 */       d = this.mPaper.getWidth() - (this.mPaper.getImageableX() + this.mPaper.getImageableWidth());
/*     */ 
/* 214 */       break;
/*     */     default:
/* 220 */       throw new InternalError("unrecognized orientation");
/*     */     }
/*     */ 
/* 224 */     return d;
/*     */   }
/*     */ 
/*     */   public double getImageableWidth()
/*     */   {
/*     */     double d;
/* 236 */     if (getOrientation() == 1)
/* 237 */       d = this.mPaper.getImageableWidth();
/*     */     else {
/* 239 */       d = this.mPaper.getImageableHeight();
/*     */     }
/*     */ 
/* 242 */     return d;
/*     */   }
/*     */ 
/*     */   public double getImageableHeight()
/*     */   {
/*     */     double d;
/* 254 */     if (getOrientation() == 1)
/* 255 */       d = this.mPaper.getImageableHeight();
/*     */     else {
/* 257 */       d = this.mPaper.getImageableWidth();
/*     */     }
/*     */ 
/* 260 */     return d;
/*     */   }
/*     */ 
/*     */   public Paper getPaper()
/*     */   {
/* 279 */     return (Paper)this.mPaper.clone();
/*     */   }
/*     */ 
/*     */   public void setPaper(Paper paramPaper)
/*     */   {
/* 292 */     this.mPaper = ((Paper)paramPaper.clone());
/*     */   }
/*     */ 
/*     */   public void setOrientation(int paramInt)
/*     */     throws IllegalArgumentException
/*     */   {
/* 306 */     if ((0 <= paramInt) && (paramInt <= 2))
/* 307 */       this.mOrientation = paramInt;
/*     */     else
/* 309 */       throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public int getOrientation()
/*     */   {
/* 319 */     return this.mOrientation;
/*     */   }
/*     */ 
/*     */   public double[] getMatrix()
/*     */   {
/* 335 */     double[] arrayOfDouble = new double[6];
/*     */ 
/* 337 */     switch (this.mOrientation)
/*     */     {
/*     */     case 0:
/* 340 */       arrayOfDouble[0] = 0.0D; arrayOfDouble[1] = -1.0D;
/* 341 */       arrayOfDouble[2] = 1.0D; arrayOfDouble[3] = 0.0D;
/* 342 */       arrayOfDouble[4] = 0.0D; arrayOfDouble[5] = this.mPaper.getHeight();
/* 343 */       break;
/*     */     case 1:
/* 346 */       arrayOfDouble[0] = 1.0D; arrayOfDouble[1] = 0.0D;
/* 347 */       arrayOfDouble[2] = 0.0D; arrayOfDouble[3] = 1.0D;
/* 348 */       arrayOfDouble[4] = 0.0D; arrayOfDouble[5] = 0.0D;
/* 349 */       break;
/*     */     case 2:
/* 352 */       arrayOfDouble[0] = 0.0D; arrayOfDouble[1] = 1.0D;
/* 353 */       arrayOfDouble[2] = -1.0D; arrayOfDouble[3] = 0.0D;
/* 354 */       arrayOfDouble[4] = this.mPaper.getWidth(); arrayOfDouble[5] = 0.0D;
/* 355 */       break;
/*     */     default:
/* 358 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 361 */     return arrayOfDouble;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.print.PageFormat
 * JD-Core Version:    0.6.2
 */