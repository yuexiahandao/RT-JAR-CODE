/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.Shape;
/*     */ import java.awt.font.LayoutPath;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.NoninvertibleTransformException;
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Point2D.Double;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Formatter;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public abstract class LayoutPathImpl extends LayoutPath
/*     */ {
/*     */   private static final boolean LOGMAP = false;
/*  92 */   private static final Formatter LOG = new Formatter(System.out);
/*     */ 
/*     */   public Point2D pointToPath(double paramDouble1, double paramDouble2)
/*     */   {
/*  57 */     Point2D.Double localDouble = new Point2D.Double(paramDouble1, paramDouble2);
/*  58 */     pointToPath(localDouble, localDouble);
/*  59 */     return localDouble;
/*     */   }
/*     */ 
/*     */   public Point2D pathToPoint(double paramDouble1, double paramDouble2, boolean paramBoolean) {
/*  63 */     Point2D.Double localDouble = new Point2D.Double(paramDouble1, paramDouble2);
/*  64 */     pathToPoint(localDouble, paramBoolean, localDouble);
/*  65 */     return localDouble;
/*     */   }
/*     */ 
/*     */   public void pointToPath(double paramDouble1, double paramDouble2, Point2D paramPoint2D) {
/*  69 */     paramPoint2D.setLocation(paramDouble1, paramDouble2);
/*  70 */     pointToPath(paramPoint2D, paramPoint2D);
/*     */   }
/*     */ 
/*     */   public void pathToPoint(double paramDouble1, double paramDouble2, boolean paramBoolean, Point2D paramPoint2D) {
/*  74 */     paramPoint2D.setLocation(paramDouble1, paramDouble2);
/*  75 */     pathToPoint(paramPoint2D, paramBoolean, paramPoint2D);
/*     */   }
/*     */ 
/*     */   public abstract double start();
/*     */ 
/*     */   public abstract double end();
/*     */ 
/*     */   public abstract double length();
/*     */ 
/*     */   public abstract Shape mapShape(Shape paramShape);
/*     */ 
/*     */   public static LayoutPathImpl getPath(EndType paramEndType, double[] paramArrayOfDouble)
/*     */   {
/* 117 */     if ((paramArrayOfDouble.length & 0x1) != 0) {
/* 118 */       throw new IllegalArgumentException("odd number of points not allowed");
/*     */     }
/*     */ 
/* 121 */     return SegmentPath.get(paramEndType, paramArrayOfDouble);
/*     */   }
/*     */ 
/*     */   public static class EmptyPath extends LayoutPathImpl
/*     */   {
/*     */     private AffineTransform tx;
/*     */ 
/*     */     public EmptyPath(AffineTransform paramAffineTransform)
/*     */     {
/* 962 */       this.tx = paramAffineTransform;
/*     */     }
/*     */ 
/*     */     public void pathToPoint(Point2D paramPoint2D1, boolean paramBoolean, Point2D paramPoint2D2) {
/* 966 */       if (this.tx != null)
/* 967 */         this.tx.transform(paramPoint2D1, paramPoint2D2);
/*     */       else
/* 969 */         paramPoint2D2.setLocation(paramPoint2D1);
/*     */     }
/*     */ 
/*     */     public boolean pointToPath(Point2D paramPoint2D1, Point2D paramPoint2D2)
/*     */     {
/* 974 */       paramPoint2D2.setLocation(paramPoint2D1);
/* 975 */       if (this.tx != null)
/*     */         try {
/* 977 */           this.tx.inverseTransform(paramPoint2D1, paramPoint2D2);
/*     */         }
/*     */         catch (NoninvertibleTransformException localNoninvertibleTransformException)
/*     */         {
/*     */         }
/* 982 */       return paramPoint2D2.getX() > 0.0D;
/*     */     }
/*     */     public double start() {
/* 985 */       return 0.0D;
/*     */     }
/* 987 */     public double end() { return 0.0D; } 
/*     */     public double length() {
/* 989 */       return 0.0D;
/*     */     }
/*     */     public Shape mapShape(Shape paramShape) {
/* 992 */       if (this.tx != null) {
/* 993 */         return this.tx.createTransformedShape(paramShape);
/*     */       }
/* 995 */       return paramShape;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum EndType
/*     */   {
/* 103 */     PINNED, EXTENDED, CLOSED;
/*     */ 
/* 104 */     public boolean isPinned() { return this == PINNED; } 
/* 105 */     public boolean isExtended() { return this == EXTENDED; } 
/* 106 */     public boolean isClosed() { return this == CLOSED; }
/*     */ 
/*     */   }
/*     */ 
/*     */   public static final class SegmentPath extends LayoutPathImpl
/*     */   {
/*     */     private double[] data;
/*     */     LayoutPathImpl.EndType etype;
/*     */ 
/*     */     public static SegmentPath get(LayoutPathImpl.EndType paramEndType, double[] paramArrayOfDouble)
/*     */     {
/* 317 */       return new LayoutPathImpl.SegmentPathBuilder().build(paramEndType, paramArrayOfDouble);
/*     */     }
/*     */ 
/*     */     SegmentPath(double[] paramArrayOfDouble, LayoutPathImpl.EndType paramEndType)
/*     */     {
/* 325 */       this.data = paramArrayOfDouble;
/* 326 */       this.etype = paramEndType;
/*     */     }
/*     */ 
/*     */     public void pathToPoint(Point2D paramPoint2D1, boolean paramBoolean, Point2D paramPoint2D2)
/*     */     {
/* 334 */       locateAndGetIndex(paramPoint2D1, paramBoolean, paramPoint2D2);
/*     */     }
/*     */ 
/*     */     public boolean pointToPath(Point2D paramPoint2D1, Point2D paramPoint2D2)
/*     */     {
/* 397 */       double d1 = paramPoint2D1.getX();
/* 398 */       double d2 = paramPoint2D1.getY();
/*     */ 
/* 400 */       double d3 = this.data[0];
/* 401 */       double d4 = this.data[1];
/* 402 */       double d5 = this.data[2];
/*     */ 
/* 405 */       double d6 = 1.7976931348623157E+308D;
/* 406 */       double d7 = 0.0D;
/* 407 */       double d8 = 0.0D;
/* 408 */       double d9 = 0.0D;
/* 409 */       int i = 0;
/*     */ 
/* 411 */       for (int j = 3; j < this.data.length; j += 3) {
/* 412 */         double d11 = this.data[j];
/* 413 */         double d13 = this.data[(j + 1)];
/* 414 */         double d15 = this.data[(j + 2)];
/*     */ 
/* 416 */         double d16 = d11 - d3;
/* 417 */         double d17 = d13 - d4;
/* 418 */         double d18 = d15 - d5;
/*     */ 
/* 420 */         double d19 = d1 - d3;
/* 421 */         double d20 = d2 - d4;
/*     */ 
/* 426 */         double d21 = d16 * d19 + d17 * d20;
/*     */         double d22;
/*     */         double d23;
/*     */         double d24;
/*     */         int n;
/* 430 */         if ((d18 == 0.0D) || ((d21 < 0.0D) && ((!this.etype.isExtended()) || (j != 3))))
/*     */         {
/* 434 */           d22 = d3;
/* 435 */           d23 = d4;
/* 436 */           d24 = d5;
/* 437 */           n = j;
/*     */         } else {
/* 439 */           d25 = d18 * d18;
/* 440 */           if ((d21 <= d25) || ((this.etype.isExtended()) && (j == this.data.length - 3)))
/*     */           {
/* 443 */             d26 = d21 / d25;
/* 444 */             d22 = d3 + d26 * d16;
/* 445 */             d23 = d4 + d26 * d17;
/* 446 */             d24 = d5 + d26 * d18;
/* 447 */             n = j;
/*     */           } else {
/* 449 */             if (j != this.data.length - 3) break label358;
/* 450 */             d22 = d11;
/* 451 */             d23 = d13;
/* 452 */             d24 = d15;
/* 453 */             n = this.data.length;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 460 */         double d25 = d1 - d22;
/* 461 */         double d26 = d2 - d23;
/* 462 */         double d27 = d25 * d25 + d26 * d26;
/* 463 */         if (d27 <= d6) {
/* 464 */           d6 = d27;
/* 465 */           d7 = d22;
/* 466 */           d8 = d23;
/* 467 */           d9 = d24;
/* 468 */           i = n;
/*     */         }
/*     */ 
/* 472 */         label358: d3 = d11;
/* 473 */         d4 = d13;
/* 474 */         d5 = d15;
/*     */       }
/*     */ 
/* 478 */       d3 = this.data[(i - 3)];
/* 479 */       d4 = this.data[(i - 2)];
/* 480 */       if ((d7 != d3) || (d8 != d4)) {
/* 481 */         double d10 = this.data[i];
/* 482 */         double d12 = this.data[(i + 1)];
/* 483 */         double d14 = Math.sqrt(d6);
/* 484 */         if ((d1 - d7) * (d12 - d4) > (d2 - d8) * (d10 - d3)) {
/* 485 */           d14 = -d14;
/*     */         }
/* 487 */         paramPoint2D2.setLocation(d9, d14);
/* 488 */         return false;
/*     */       }
/* 490 */       int k = (i != 3) && (this.data[(i - 1)] != this.data[(i - 4)]) ? 1 : 0;
/* 491 */       int m = (i != this.data.length) && (this.data[(i - 1)] != this.data[(i + 2)]) ? 1 : 0;
/* 492 */       boolean bool = (this.etype.isExtended()) && ((i == 3) || (i == this.data.length));
/* 493 */       if ((k != 0) && (m != 0)) {
/* 494 */         Point2D.Double localDouble1 = new Point2D.Double(d1, d2);
/* 495 */         calcoffset(i - 3, bool, localDouble1);
/* 496 */         Point2D.Double localDouble2 = new Point2D.Double(d1, d2);
/* 497 */         calcoffset(i, bool, localDouble2);
/* 498 */         if (Math.abs(localDouble1.y) > Math.abs(localDouble2.y)) {
/* 499 */           paramPoint2D2.setLocation(localDouble1);
/* 500 */           return true;
/*     */         }
/* 502 */         paramPoint2D2.setLocation(localDouble2);
/* 503 */         return false;
/*     */       }
/* 505 */       if (k != 0) {
/* 506 */         paramPoint2D2.setLocation(d1, d2);
/* 507 */         calcoffset(i - 3, bool, paramPoint2D2);
/* 508 */         return true;
/*     */       }
/* 510 */       paramPoint2D2.setLocation(d1, d2);
/* 511 */       calcoffset(i, bool, paramPoint2D2);
/* 512 */       return false;
/*     */     }
/*     */ 
/*     */     private void calcoffset(int paramInt, boolean paramBoolean, Point2D paramPoint2D)
/*     */     {
/* 525 */       double d1 = this.data[(paramInt - 3)];
/* 526 */       double d2 = this.data[(paramInt - 2)];
/* 527 */       double d3 = paramPoint2D.getX() - d1;
/* 528 */       double d4 = paramPoint2D.getY() - d2;
/* 529 */       double d5 = this.data[paramInt] - d1;
/* 530 */       double d6 = this.data[(paramInt + 1)] - d2;
/* 531 */       double d7 = this.data[(paramInt + 2)] - this.data[(paramInt - 1)];
/*     */ 
/* 535 */       double d8 = (d3 * d5 + d4 * d6) / d7;
/* 536 */       double d9 = (d3 * -d6 + d4 * d5) / d7;
/* 537 */       if (!paramBoolean) {
/* 538 */         if (d8 < 0.0D) d8 = 0.0D;
/* 539 */         else if (d8 > d7) d8 = d7;
/*     */       }
/* 541 */       d8 += this.data[(paramInt - 1)];
/* 542 */       paramPoint2D.setLocation(d8, d9);
/*     */     }
/*     */ 
/*     */     public Shape mapShape(Shape paramShape)
/*     */     {
/* 550 */       return new Mapper().mapShape(paramShape);
/*     */     }
/*     */ 
/*     */     public double start() {
/* 554 */       return this.data[2];
/*     */     }
/*     */ 
/*     */     public double end() {
/* 558 */       return this.data[(this.data.length - 1)];
/*     */     }
/*     */ 
/*     */     public double length() {
/* 562 */       return this.data[(this.data.length - 1)] - this.data[2];
/*     */     }
/*     */ 
/*     */     private double getClosedAdvance(double paramDouble, boolean paramBoolean)
/*     */     {
/* 573 */       if (this.etype.isClosed()) {
/* 574 */         paramDouble -= this.data[2];
/* 575 */         int i = (int)(paramDouble / length());
/* 576 */         paramDouble -= i * length();
/* 577 */         if ((paramDouble < 0.0D) || ((paramDouble == 0.0D) && (paramBoolean))) {
/* 578 */           paramDouble += length();
/*     */         }
/*     */ 
/* 581 */         paramDouble += this.data[2];
/*     */       }
/* 583 */       return paramDouble;
/*     */     }
/*     */ 
/*     */     private int getSegmentIndexForAdvance(double paramDouble, boolean paramBoolean)
/*     */     {
/* 605 */       paramDouble = getClosedAdvance(paramDouble, paramBoolean);
/*     */ 
/* 610 */       int i = 5; for (int j = this.data.length - 1; i < j; i += 3) {
/* 611 */         double d = this.data[i];
/* 612 */         if ((paramDouble < d) || ((paramDouble == d) && (paramBoolean))) {
/*     */           break;
/*     */         }
/*     */       }
/* 616 */       return i - 2;
/*     */     }
/*     */ 
/*     */     private void map(int paramInt, double paramDouble1, double paramDouble2, Point2D paramPoint2D)
/*     */     {
/* 625 */       double d1 = this.data[paramInt] - this.data[(paramInt - 3)];
/* 626 */       double d2 = this.data[(paramInt + 1)] - this.data[(paramInt - 2)];
/* 627 */       double d3 = this.data[(paramInt + 2)] - this.data[(paramInt - 1)];
/*     */ 
/* 629 */       double d4 = d1 / d3;
/* 630 */       double d5 = d2 / d3;
/*     */ 
/* 632 */       paramDouble1 -= this.data[(paramInt - 1)];
/*     */ 
/* 634 */       paramPoint2D.setLocation(this.data[(paramInt - 3)] + paramDouble1 * d4 - paramDouble2 * d5, this.data[(paramInt - 2)] + paramDouble1 * d5 + paramDouble2 * d4);
/*     */     }
/*     */ 
/*     */     private int locateAndGetIndex(Point2D paramPoint2D1, boolean paramBoolean, Point2D paramPoint2D2)
/*     */     {
/* 642 */       double d1 = paramPoint2D1.getX();
/* 643 */       double d2 = paramPoint2D1.getY();
/* 644 */       int i = getSegmentIndexForAdvance(d1, paramBoolean);
/* 645 */       map(i, d1, d2, paramPoint2D2);
/*     */ 
/* 647 */       return i;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 933 */       StringBuilder localStringBuilder = new StringBuilder();
/* 934 */       localStringBuilder.append("{");
/* 935 */       localStringBuilder.append(this.etype.toString());
/* 936 */       localStringBuilder.append(" ");
/* 937 */       for (int i = 0; i < this.data.length; i += 3) {
/* 938 */         if (i > 0) {
/* 939 */           localStringBuilder.append(",");
/*     */         }
/* 941 */         float f1 = (int)(this.data[i] * 100.0D) / 100.0F;
/* 942 */         float f2 = (int)(this.data[(i + 1)] * 100.0D) / 100.0F;
/* 943 */         float f3 = (int)(this.data[(i + 2)] * 10.0D) / 10.0F;
/* 944 */         localStringBuilder.append("{");
/* 945 */         localStringBuilder.append(f1);
/* 946 */         localStringBuilder.append(",");
/* 947 */         localStringBuilder.append(f2);
/* 948 */         localStringBuilder.append(",");
/* 949 */         localStringBuilder.append(f3);
/* 950 */         localStringBuilder.append("}");
/*     */       }
/* 952 */       localStringBuilder.append("}");
/* 953 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/*     */     class LineInfo
/*     */     {
/*     */       double sx;
/*     */       double sy;
/*     */       double lx;
/*     */       double ly;
/*     */       double m;
/*     */ 
/*     */       LineInfo()
/*     */       {
/*     */       }
/*     */ 
/*     */       void set(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */       {
/* 671 */         this.sx = paramDouble1;
/* 672 */         this.sy = paramDouble2;
/* 673 */         this.lx = paramDouble3;
/* 674 */         this.ly = paramDouble4;
/* 675 */         double d1 = paramDouble3 - paramDouble1;
/* 676 */         if (d1 == 0.0D) {
/* 677 */           this.m = 0.0D;
/*     */         } else {
/* 679 */           double d2 = paramDouble4 - paramDouble2;
/* 680 */           this.m = (d2 / d1);
/*     */         }
/*     */       }
/*     */ 
/*     */       void set(LineInfo paramLineInfo) {
/* 685 */         this.sx = paramLineInfo.sx;
/* 686 */         this.sy = paramLineInfo.sy;
/* 687 */         this.lx = paramLineInfo.lx;
/* 688 */         this.ly = paramLineInfo.ly;
/* 689 */         this.m = paramLineInfo.m;
/*     */       }
/*     */ 
/*     */       boolean pin(double paramDouble1, double paramDouble2, LineInfo paramLineInfo)
/*     */       {
/* 698 */         paramLineInfo.set(this);
/* 699 */         if (this.lx >= this.sx) {
/* 700 */           if ((this.sx < paramDouble2) && (this.lx >= paramDouble1)) {
/* 701 */             if (this.sx < paramDouble1) {
/* 702 */               if (this.m != 0.0D) this.sy += this.m * (paramDouble1 - this.sx);
/* 703 */               paramLineInfo.sx = paramDouble1;
/*     */             }
/* 705 */             if (this.lx > paramDouble2) {
/* 706 */               if (this.m != 0.0D) this.ly += this.m * (paramDouble2 - this.lx);
/* 707 */               paramLineInfo.lx = paramDouble2;
/*     */             }
/* 709 */             return true;
/*     */           }
/*     */         }
/* 712 */         else if ((this.lx < paramDouble2) && (this.sx >= paramDouble1)) {
/* 713 */           if (this.lx < paramDouble1) {
/* 714 */             if (this.m != 0.0D) this.ly += this.m * (paramDouble1 - this.lx);
/* 715 */             paramLineInfo.lx = paramDouble1;
/*     */           }
/* 717 */           if (this.sx > paramDouble2) {
/* 718 */             if (this.m != 0.0D) this.sy += this.m * (paramDouble2 - this.sx);
/* 719 */             paramLineInfo.sx = paramDouble2;
/*     */           }
/* 721 */           return true;
/*     */         }
/*     */ 
/* 724 */         return false;
/*     */       }
/*     */ 
/*     */       boolean pin(int paramInt, LineInfo paramLineInfo)
/*     */       {
/* 733 */         double d1 = LayoutPathImpl.SegmentPath.this.data[(paramInt - 1)];
/* 734 */         double d2 = LayoutPathImpl.SegmentPath.this.data[(paramInt + 2)];
/* 735 */         switch (LayoutPathImpl.1.$SwitchMap$sun$font$LayoutPathImpl$EndType[LayoutPathImpl.SegmentPath.this.etype.ordinal()]) {
/*     */         case 1:
/* 737 */           break;
/*     */         case 2:
/* 739 */           if (paramInt == 3) d1 = (-1.0D / 0.0D);
/* 740 */           if (paramInt == LayoutPathImpl.SegmentPath.this.data.length - 3) d2 = (1.0D / 0.0D); break;
/*     */         case 3:
/*     */         }
/*     */ 
/* 747 */         return pin(d1, d2, paramLineInfo);
/*     */       }
/*     */     }
/*     */ 
/*     */     class Mapper
/*     */     {
/*     */       final LayoutPathImpl.SegmentPath.LineInfo li;
/*     */       final ArrayList<LayoutPathImpl.SegmentPath.Segment> segments;
/*     */       final Point2D.Double mpt;
/*     */       final Point2D.Double cpt;
/*     */       boolean haveMT;
/*     */ 
/*     */       Mapper()
/*     */       {
/* 834 */         this.li = new LayoutPathImpl.SegmentPath.LineInfo(LayoutPathImpl.SegmentPath.this);
/* 835 */         this.segments = new ArrayList();
/* 836 */         for (int i = 3; i < LayoutPathImpl.SegmentPath.this.data.length; i += 3) {
/* 837 */           if (LayoutPathImpl.SegmentPath.this.data[(i + 2)] != LayoutPathImpl.SegmentPath.this.data[(i - 1)]) {
/* 838 */             this.segments.add(new LayoutPathImpl.SegmentPath.Segment(LayoutPathImpl.SegmentPath.this, i));
/*     */           }
/*     */         }
/*     */ 
/* 842 */         this.mpt = new Point2D.Double();
/* 843 */         this.cpt = new Point2D.Double();
/*     */       }
/*     */ 
/*     */       void init()
/*     */       {
/* 848 */         this.haveMT = false;
/* 849 */         for (LayoutPathImpl.SegmentPath.Segment localSegment : this.segments)
/* 850 */           localSegment.init();
/*     */       }
/*     */ 
/*     */       void moveTo(double paramDouble1, double paramDouble2)
/*     */       {
/* 856 */         this.mpt.x = paramDouble1;
/* 857 */         this.mpt.y = paramDouble2;
/* 858 */         this.haveMT = true;
/*     */       }
/*     */ 
/*     */       void lineTo(double paramDouble1, double paramDouble2)
/*     */       {
/* 864 */         if (this.haveMT)
/*     */         {
/* 866 */           this.cpt.x = this.mpt.x;
/* 867 */           this.cpt.y = this.mpt.y;
/*     */         }
/*     */ 
/* 870 */         if ((paramDouble1 == this.cpt.x) && (paramDouble2 == this.cpt.y))
/*     */         {
/* 872 */           return;
/*     */         }
/*     */ 
/* 875 */         if (this.haveMT)
/*     */         {
/* 877 */           this.haveMT = false;
/* 878 */           for (localIterator = this.segments.iterator(); localIterator.hasNext(); ) { localSegment = (LayoutPathImpl.SegmentPath.Segment)localIterator.next();
/* 879 */             localSegment.move();
/*     */           }
/*     */         }
/* 884 */         LayoutPathImpl.SegmentPath.Segment localSegment;
/* 883 */         this.li.set(this.cpt.x, this.cpt.y, paramDouble1, paramDouble2);
/* 884 */         for (Iterator localIterator = this.segments.iterator(); localIterator.hasNext(); ) { localSegment = (LayoutPathImpl.SegmentPath.Segment)localIterator.next();
/* 885 */           localSegment.line(this.li);
/*     */         }
/*     */ 
/* 888 */         this.cpt.x = paramDouble1;
/* 889 */         this.cpt.y = paramDouble2;
/*     */       }
/*     */ 
/*     */       void close()
/*     */       {
/* 894 */         lineTo(this.mpt.x, this.mpt.y);
/* 895 */         for (LayoutPathImpl.SegmentPath.Segment localSegment : this.segments)
/* 896 */           localSegment.close();
/*     */       }
/*     */ 
/*     */       public Shape mapShape(Shape paramShape)
/*     */       {
/* 902 */         PathIterator localPathIterator = paramShape.getPathIterator(null, 1.0D);
/*     */ 
/* 905 */         init();
/*     */ 
/* 907 */         double[] arrayOfDouble = new double[2];
/* 908 */         while (!localPathIterator.isDone()) {
/* 909 */           switch (localPathIterator.currentSegment(arrayOfDouble)) { case 4:
/* 910 */             close(); break;
/*     */           case 0:
/* 911 */             moveTo(arrayOfDouble[0], arrayOfDouble[1]); break;
/*     */           case 1:
/* 912 */             lineTo(arrayOfDouble[0], arrayOfDouble[1]); break;
/*     */           case 2:
/*     */           case 3:
/*     */           }
/* 916 */           localPathIterator.next();
/*     */         }
/*     */ 
/* 920 */         GeneralPath localGeneralPath = new GeneralPath();
/* 921 */         for (LayoutPathImpl.SegmentPath.Segment localSegment : this.segments) {
/* 922 */           localGeneralPath.append(localSegment.gp, false);
/*     */         }
/* 924 */         return localGeneralPath;
/*     */       }
/*     */     }
/*     */ 
/*     */     class Segment
/*     */     {
/*     */       final int ix;
/*     */       final double ux;
/*     */       final double uy;
/*     */       final LayoutPathImpl.SegmentPath.LineInfo temp;
/*     */       boolean broken;
/*     */       double cx;
/*     */       double cy;
/*     */       GeneralPath gp;
/*     */ 
/*     */       Segment(int arg2)
/*     */       {
/*     */         int i;
/* 766 */         this.ix = i;
/* 767 */         double d = LayoutPathImpl.SegmentPath.this.data[(i + 2)] - LayoutPathImpl.SegmentPath.this.data[(i - 1)];
/* 768 */         this.ux = ((LayoutPathImpl.SegmentPath.this.data[i] - LayoutPathImpl.SegmentPath.this.data[(i - 3)]) / d);
/* 769 */         this.uy = ((LayoutPathImpl.SegmentPath.this.data[(i + 1)] - LayoutPathImpl.SegmentPath.this.data[(i - 2)]) / d);
/* 770 */         this.temp = new LayoutPathImpl.SegmentPath.LineInfo(LayoutPathImpl.SegmentPath.this);
/*     */       }
/*     */ 
/*     */       void init()
/*     */       {
/* 775 */         this.broken = true;
/* 776 */         this.cx = (this.cy = 4.9E-324D);
/* 777 */         this.gp = new GeneralPath();
/*     */       }
/*     */ 
/*     */       void move()
/*     */       {
/* 782 */         this.broken = true;
/*     */       }
/*     */ 
/*     */       void close() {
/* 786 */         if (!this.broken)
/*     */         {
/* 788 */           this.gp.closePath();
/*     */         }
/*     */       }
/*     */ 
/*     */       void line(LayoutPathImpl.SegmentPath.LineInfo paramLineInfo)
/*     */       {
/* 795 */         if (paramLineInfo.pin(this.ix, this.temp))
/*     */         {
/* 798 */           this.temp.sx -= LayoutPathImpl.SegmentPath.this.data[(this.ix - 1)];
/* 799 */           double d1 = LayoutPathImpl.SegmentPath.this.data[(this.ix - 3)] + this.temp.sx * this.ux - this.temp.sy * this.uy;
/* 800 */           double d2 = LayoutPathImpl.SegmentPath.this.data[(this.ix - 2)] + this.temp.sx * this.uy + this.temp.sy * this.ux;
/* 801 */           this.temp.lx -= LayoutPathImpl.SegmentPath.this.data[(this.ix - 1)];
/* 802 */           double d3 = LayoutPathImpl.SegmentPath.this.data[(this.ix - 3)] + this.temp.lx * this.ux - this.temp.ly * this.uy;
/* 803 */           double d4 = LayoutPathImpl.SegmentPath.this.data[(this.ix - 2)] + this.temp.lx * this.uy + this.temp.ly * this.ux;
/*     */ 
/* 807 */           if ((d1 != this.cx) || (d2 != this.cy)) {
/* 808 */             if (this.broken)
/*     */             {
/* 810 */               this.gp.moveTo((float)d1, (float)d2);
/*     */             }
/*     */             else {
/* 813 */               this.gp.lineTo((float)d1, (float)d2);
/*     */             }
/*     */           }
/*     */ 
/* 817 */           this.gp.lineTo((float)d3, (float)d4);
/*     */ 
/* 819 */           this.broken = false;
/* 820 */           this.cx = d3;
/* 821 */           this.cy = d4;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class SegmentPathBuilder
/*     */   {
/*     */     private double[] data;
/*     */     private int w;
/*     */     private double px;
/*     */     private double py;
/*     */     private double a;
/*     */     private boolean pconnect;
/*     */ 
/*     */     public void reset(int paramInt)
/*     */     {
/* 151 */       if ((this.data == null) || (paramInt > this.data.length))
/* 152 */         this.data = new double[paramInt];
/* 153 */       else if (paramInt == 0) {
/* 154 */         this.data = null;
/*     */       }
/* 156 */       this.w = 0;
/* 157 */       this.px = (this.py = 0.0D);
/* 158 */       this.pconnect = false;
/*     */     }
/*     */ 
/*     */     public LayoutPathImpl.SegmentPath build(LayoutPathImpl.EndType paramEndType, double[] paramArrayOfDouble)
/*     */     {
/* 166 */       assert (paramArrayOfDouble.length % 2 == 0);
/*     */ 
/* 168 */       reset(paramArrayOfDouble.length / 2 * 3);
/*     */ 
/* 170 */       for (int i = 0; i < paramArrayOfDouble.length; i += 2) {
/* 171 */         nextPoint(paramArrayOfDouble[i], paramArrayOfDouble[(i + 1)], i != 0);
/*     */       }
/*     */ 
/* 174 */       return complete(paramEndType);
/*     */     }
/*     */ 
/*     */     public void moveTo(double paramDouble1, double paramDouble2)
/*     */     {
/* 189 */       nextPoint(paramDouble1, paramDouble2, false);
/*     */     }
/*     */ 
/*     */     public void lineTo(double paramDouble1, double paramDouble2)
/*     */     {
/* 200 */       nextPoint(paramDouble1, paramDouble2, true);
/*     */     }
/*     */ 
/*     */     private void nextPoint(double paramDouble1, double paramDouble2, boolean paramBoolean)
/*     */     {
/* 211 */       if ((paramDouble1 == this.px) && (paramDouble2 == this.py)) {
/* 212 */         return;
/*     */       }
/*     */ 
/* 215 */       if (this.w == 0) {
/* 216 */         if (this.data == null) {
/* 217 */           this.data = new double[6];
/*     */         }
/* 219 */         if (paramBoolean) {
/* 220 */           this.w = 3;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 225 */       if ((this.w != 0) && (!paramBoolean) && (!this.pconnect))
/*     */       {
/*     */         double tmp82_81 = paramDouble1; this.px = tmp82_81; this.data[(this.w - 3)] = tmp82_81;
/*     */         double tmp99_98 = paramDouble2; this.py = tmp99_98; this.data[(this.w - 2)] = tmp99_98;
/* 228 */         return;
/*     */       }
/*     */ 
/* 232 */       if (this.w == this.data.length) {
/* 233 */         double[] arrayOfDouble = new double[this.w * 2];
/* 234 */         System.arraycopy(this.data, 0, arrayOfDouble, 0, this.w);
/* 235 */         this.data = arrayOfDouble;
/*     */       }
/*     */ 
/* 238 */       if (paramBoolean) {
/* 239 */         double d1 = paramDouble1 - this.px;
/* 240 */         double d2 = paramDouble2 - this.py;
/* 241 */         this.a += Math.sqrt(d1 * d1 + d2 * d2);
/*     */       }
/*     */ 
/* 245 */       this.data[(this.w++)] = paramDouble1;
/* 246 */       this.data[(this.w++)] = paramDouble2;
/* 247 */       this.data[(this.w++)] = this.a;
/*     */ 
/* 250 */       this.px = paramDouble1;
/* 251 */       this.py = paramDouble2;
/* 252 */       this.pconnect = paramBoolean;
/*     */     }
/*     */ 
/*     */     public LayoutPathImpl.SegmentPath complete() {
/* 256 */       return complete(LayoutPathImpl.EndType.EXTENDED);
/*     */     }
/*     */ 
/*     */     public LayoutPathImpl.SegmentPath complete(LayoutPathImpl.EndType paramEndType)
/*     */     {
/* 267 */       if ((this.data == null) || (this.w < 6))
/* 268 */         return null;
/*     */       LayoutPathImpl.SegmentPath localSegmentPath;
/* 271 */       if (this.w == this.data.length) {
/* 272 */         localSegmentPath = new LayoutPathImpl.SegmentPath(this.data, paramEndType);
/* 273 */         reset(0);
/*     */       } else {
/* 275 */         double[] arrayOfDouble = new double[this.w];
/* 276 */         System.arraycopy(this.data, 0, arrayOfDouble, 0, this.w);
/* 277 */         localSegmentPath = new LayoutPathImpl.SegmentPath(arrayOfDouble, paramEndType);
/* 278 */         reset(2);
/*     */       }
/*     */ 
/* 281 */       return localSegmentPath;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.LayoutPathImpl
 * JD-Core Version:    0.6.2
 */