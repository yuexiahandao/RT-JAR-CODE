/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.awt.geom.Rectangle2D.Double;
/*      */ import java.beans.Transient;
/*      */ import java.io.Serializable;
/*      */ 
/*      */ public class Rectangle extends Rectangle2D
/*      */   implements Shape, Serializable
/*      */ {
/*      */   public int x;
/*      */   public int y;
/*      */   public int width;
/*      */   public int height;
/*      */   private static final long serialVersionUID = -4345857070255674764L;
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   public Rectangle()
/*      */   {
/*  186 */     this(0, 0, 0, 0);
/*      */   }
/*      */ 
/*      */   public Rectangle(Rectangle paramRectangle)
/*      */   {
/*  197 */     this(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*      */   }
/*      */ 
/*      */   public Rectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  212 */     this.x = paramInt1;
/*  213 */     this.y = paramInt2;
/*  214 */     this.width = paramInt3;
/*  215 */     this.height = paramInt4;
/*      */   }
/*      */ 
/*      */   public Rectangle(int paramInt1, int paramInt2)
/*      */   {
/*  226 */     this(0, 0, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public Rectangle(Point paramPoint, Dimension paramDimension)
/*      */   {
/*  240 */     this(paramPoint.x, paramPoint.y, paramDimension.width, paramDimension.height);
/*      */   }
/*      */ 
/*      */   public Rectangle(Point paramPoint)
/*      */   {
/*  250 */     this(paramPoint.x, paramPoint.y, 0, 0);
/*      */   }
/*      */ 
/*      */   public Rectangle(Dimension paramDimension)
/*      */   {
/*  260 */     this(0, 0, paramDimension.width, paramDimension.height);
/*      */   }
/*      */ 
/*      */   public double getX()
/*      */   {
/*  269 */     return this.x;
/*      */   }
/*      */ 
/*      */   public double getY()
/*      */   {
/*  278 */     return this.y;
/*      */   }
/*      */ 
/*      */   public double getWidth()
/*      */   {
/*  287 */     return this.width;
/*      */   }
/*      */ 
/*      */   public double getHeight()
/*      */   {
/*  296 */     return this.height;
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public Rectangle getBounds()
/*      */   {
/*  314 */     return new Rectangle(this.x, this.y, this.width, this.height);
/*      */   }
/*      */ 
/*      */   public Rectangle2D getBounds2D()
/*      */   {
/*  322 */     return new Rectangle(this.x, this.y, this.width, this.height);
/*      */   }
/*      */ 
/*      */   public void setBounds(Rectangle paramRectangle)
/*      */   {
/*  337 */     setBounds(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*      */   }
/*      */ 
/*      */   public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  359 */     reshape(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void setRect(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/*      */     int i;
/*      */     int k;
/*  380 */     if (paramDouble1 > 4294967294.0D)
/*      */     {
/*  388 */       i = 2147483647;
/*  389 */       k = -1;
/*      */     } else {
/*  391 */       i = clip(paramDouble1, false);
/*  392 */       if (paramDouble3 >= 0.0D) paramDouble3 += paramDouble1 - i;
/*  393 */       k = clip(paramDouble3, paramDouble3 >= 0.0D);
/*      */     }
/*      */     int j;
/*      */     int m;
/*  396 */     if (paramDouble2 > 4294967294.0D)
/*      */     {
/*  398 */       j = 2147483647;
/*  399 */       m = -1;
/*      */     } else {
/*  401 */       j = clip(paramDouble2, false);
/*  402 */       if (paramDouble4 >= 0.0D) paramDouble4 += paramDouble2 - j;
/*  403 */       m = clip(paramDouble4, paramDouble4 >= 0.0D);
/*      */     }
/*      */ 
/*  406 */     reshape(i, j, k, m);
/*      */   }
/*      */ 
/*      */   private static int clip(double paramDouble, boolean paramBoolean)
/*      */   {
/*  411 */     if (paramDouble <= -2147483648.0D) {
/*  412 */       return -2147483648;
/*      */     }
/*  414 */     if (paramDouble >= 2147483647.0D) {
/*  415 */       return 2147483647;
/*      */     }
/*  417 */     return (int)(paramBoolean ? Math.ceil(paramDouble) : Math.floor(paramDouble));
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  437 */     this.x = paramInt1;
/*  438 */     this.y = paramInt2;
/*  439 */     this.width = paramInt3;
/*  440 */     this.height = paramInt4;
/*      */   }
/*      */ 
/*      */   public Point getLocation()
/*      */   {
/*  456 */     return new Point(this.x, this.y);
/*      */   }
/*      */ 
/*      */   public void setLocation(Point paramPoint)
/*      */   {
/*  471 */     setLocation(paramPoint.x, paramPoint.y);
/*      */   }
/*      */ 
/*      */   public void setLocation(int paramInt1, int paramInt2)
/*      */   {
/*  486 */     move(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void move(int paramInt1, int paramInt2)
/*      */   {
/*  499 */     this.x = paramInt1;
/*  500 */     this.y = paramInt2;
/*      */   }
/*      */ 
/*      */   public void translate(int paramInt1, int paramInt2)
/*      */   {
/*  515 */     int i = this.x;
/*  516 */     int j = i + paramInt1;
/*  517 */     if (paramInt1 < 0)
/*      */     {
/*  519 */       if (j > i)
/*      */       {
/*  522 */         if (this.width >= 0)
/*      */         {
/*  530 */           this.width += j - -2147483648;
/*      */         }
/*      */ 
/*  536 */         j = -2147483648;
/*      */       }
/*      */ 
/*      */     }
/*  540 */     else if (j < i)
/*      */     {
/*  542 */       if (this.width >= 0)
/*      */       {
/*  545 */         this.width += j - 2147483647;
/*      */ 
/*  548 */         if (this.width < 0) this.width = 2147483647;
/*      */       }
/*  550 */       j = 2147483647;
/*      */     }
/*      */ 
/*  553 */     this.x = j;
/*      */ 
/*  555 */     i = this.y;
/*  556 */     j = i + paramInt2;
/*  557 */     if (paramInt2 < 0)
/*      */     {
/*  559 */       if (j > i)
/*      */       {
/*  561 */         if (this.height >= 0) {
/*  562 */           this.height += j - -2147483648;
/*      */         }
/*      */ 
/*  565 */         j = -2147483648;
/*      */       }
/*      */ 
/*      */     }
/*  569 */     else if (j < i)
/*      */     {
/*  571 */       if (this.height >= 0) {
/*  572 */         this.height += j - 2147483647;
/*  573 */         if (this.height < 0) this.height = 2147483647;
/*      */       }
/*  575 */       j = 2147483647;
/*      */     }
/*      */ 
/*  578 */     this.y = j;
/*      */   }
/*      */ 
/*      */   public Dimension getSize()
/*      */   {
/*  595 */     return new Dimension(this.width, this.height);
/*      */   }
/*      */ 
/*      */   public void setSize(Dimension paramDimension)
/*      */   {
/*  610 */     setSize(paramDimension.width, paramDimension.height);
/*      */   }
/*      */ 
/*      */   public void setSize(int paramInt1, int paramInt2)
/*      */   {
/*  626 */     resize(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void resize(int paramInt1, int paramInt2)
/*      */   {
/*  640 */     this.width = paramInt1;
/*  641 */     this.height = paramInt2;
/*      */   }
/*      */ 
/*      */   public boolean contains(Point paramPoint)
/*      */   {
/*  654 */     return contains(paramPoint.x, paramPoint.y);
/*      */   }
/*      */ 
/*      */   public boolean contains(int paramInt1, int paramInt2)
/*      */   {
/*  670 */     return inside(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public boolean contains(Rectangle paramRectangle)
/*      */   {
/*  684 */     return contains(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*      */   }
/*      */ 
/*      */   public boolean contains(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  703 */     int i = this.width;
/*  704 */     int j = this.height;
/*  705 */     if ((i | j | paramInt3 | paramInt4) < 0)
/*      */     {
/*  707 */       return false;
/*      */     }
/*      */ 
/*  710 */     int k = this.x;
/*  711 */     int m = this.y;
/*  712 */     if ((paramInt1 < k) || (paramInt2 < m)) {
/*  713 */       return false;
/*      */     }
/*  715 */     i += k;
/*  716 */     paramInt3 += paramInt1;
/*  717 */     if (paramInt3 <= paramInt1)
/*      */     {
/*  722 */       if ((i >= k) || (paramInt3 > i)) return false;
/*      */ 
/*      */     }
/*  727 */     else if ((i >= k) && (paramInt3 > i)) return false;
/*      */ 
/*  729 */     j += m;
/*  730 */     paramInt4 += paramInt2;
/*  731 */     if (paramInt4 <= paramInt2) {
/*  732 */       if ((j >= m) || (paramInt4 > j)) return false;
/*      */     }
/*  734 */     else if ((j >= m) && (paramInt4 > j)) return false;
/*      */ 
/*  736 */     return true;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public boolean inside(int paramInt1, int paramInt2)
/*      */   {
/*  754 */     int i = this.width;
/*  755 */     int j = this.height;
/*  756 */     if ((i | j) < 0)
/*      */     {
/*  758 */       return false;
/*      */     }
/*      */ 
/*  761 */     int k = this.x;
/*  762 */     int m = this.y;
/*  763 */     if ((paramInt1 < k) || (paramInt2 < m)) {
/*  764 */       return false;
/*      */     }
/*  766 */     i += k;
/*  767 */     j += m;
/*      */ 
/*  769 */     return ((i < k) || (i > paramInt1)) && ((j < m) || (j > paramInt2));
/*      */   }
/*      */ 
/*      */   public boolean intersects(Rectangle paramRectangle)
/*      */   {
/*  784 */     int i = this.width;
/*  785 */     int j = this.height;
/*  786 */     int k = paramRectangle.width;
/*  787 */     int m = paramRectangle.height;
/*  788 */     if ((k <= 0) || (m <= 0) || (i <= 0) || (j <= 0)) {
/*  789 */       return false;
/*      */     }
/*  791 */     int n = this.x;
/*  792 */     int i1 = this.y;
/*  793 */     int i2 = paramRectangle.x;
/*  794 */     int i3 = paramRectangle.y;
/*  795 */     k += i2;
/*  796 */     m += i3;
/*  797 */     i += n;
/*  798 */     j += i1;
/*      */ 
/*  800 */     return ((k < i2) || (k > n)) && ((m < i3) || (m > i1)) && ((i < n) || (i > i2)) && ((j < i1) || (j > i3));
/*      */   }
/*      */ 
/*      */   public Rectangle intersection(Rectangle paramRectangle)
/*      */   {
/*  820 */     int i = this.x;
/*  821 */     int j = this.y;
/*  822 */     int k = paramRectangle.x;
/*  823 */     int m = paramRectangle.y;
/*  824 */     long l1 = i; l1 += this.width;
/*  825 */     long l2 = j; l2 += this.height;
/*  826 */     long l3 = k; l3 += paramRectangle.width;
/*  827 */     long l4 = m; l4 += paramRectangle.height;
/*  828 */     if (i < k) i = k;
/*  829 */     if (j < m) j = m;
/*  830 */     if (l1 > l3) l1 = l3;
/*  831 */     if (l2 > l4) l2 = l4;
/*  832 */     l1 -= i;
/*  833 */     l2 -= j;
/*      */ 
/*  837 */     if (l1 < -2147483648L) l1 = -2147483648L;
/*  838 */     if (l2 < -2147483648L) l2 = -2147483648L;
/*  839 */     return new Rectangle(i, j, (int)l1, (int)l2);
/*      */   }
/*      */ 
/*      */   public Rectangle union(Rectangle paramRectangle)
/*      */   {
/*  866 */     long l1 = this.width;
/*  867 */     long l2 = this.height;
/*  868 */     if ((l1 | l2) < 0L)
/*      */     {
/*  875 */       return new Rectangle(paramRectangle);
/*      */     }
/*  877 */     long l3 = paramRectangle.width;
/*  878 */     long l4 = paramRectangle.height;
/*  879 */     if ((l3 | l4) < 0L) {
/*  880 */       return new Rectangle(this);
/*      */     }
/*  882 */     int i = this.x;
/*  883 */     int j = this.y;
/*  884 */     l1 += i;
/*  885 */     l2 += j;
/*  886 */     int k = paramRectangle.x;
/*  887 */     int m = paramRectangle.y;
/*  888 */     l3 += k;
/*  889 */     l4 += m;
/*  890 */     if (i > k) i = k;
/*  891 */     if (j > m) j = m;
/*  892 */     if (l1 < l3) l1 = l3;
/*  893 */     if (l2 < l4) l2 = l4;
/*  894 */     l1 -= i;
/*  895 */     l2 -= j;
/*      */ 
/*  899 */     if (l1 > 2147483647L) l1 = 2147483647L;
/*  900 */     if (l2 > 2147483647L) l2 = 2147483647L;
/*  901 */     return new Rectangle(i, j, (int)l1, (int)l2);
/*      */   }
/*      */ 
/*      */   public void add(int paramInt1, int paramInt2)
/*      */   {
/*  932 */     if ((this.width | this.height) < 0) {
/*  933 */       this.x = paramInt1;
/*  934 */       this.y = paramInt2;
/*  935 */       this.width = (this.height = 0);
/*  936 */       return;
/*      */     }
/*  938 */     int i = this.x;
/*  939 */     int j = this.y;
/*  940 */     long l1 = this.width;
/*  941 */     long l2 = this.height;
/*  942 */     l1 += i;
/*  943 */     l2 += j;
/*  944 */     if (i > paramInt1) i = paramInt1;
/*  945 */     if (j > paramInt2) j = paramInt2;
/*  946 */     if (l1 < paramInt1) l1 = paramInt1;
/*  947 */     if (l2 < paramInt2) l2 = paramInt2;
/*  948 */     l1 -= i;
/*  949 */     l2 -= j;
/*  950 */     if (l1 > 2147483647L) l1 = 2147483647L;
/*  951 */     if (l2 > 2147483647L) l2 = 2147483647L;
/*  952 */     reshape(i, j, (int)l1, (int)l2);
/*      */   }
/*      */ 
/*      */   public void add(Point paramPoint)
/*      */   {
/*  983 */     add(paramPoint.x, paramPoint.y);
/*      */   }
/*      */ 
/*      */   public void add(Rectangle paramRectangle)
/*      */   {
/* 1010 */     long l1 = this.width;
/* 1011 */     long l2 = this.height;
/* 1012 */     if ((l1 | l2) < 0L) {
/* 1013 */       reshape(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*      */     }
/* 1015 */     long l3 = paramRectangle.width;
/* 1016 */     long l4 = paramRectangle.height;
/* 1017 */     if ((l3 | l4) < 0L) {
/* 1018 */       return;
/*      */     }
/* 1020 */     int i = this.x;
/* 1021 */     int j = this.y;
/* 1022 */     l1 += i;
/* 1023 */     l2 += j;
/* 1024 */     int k = paramRectangle.x;
/* 1025 */     int m = paramRectangle.y;
/* 1026 */     l3 += k;
/* 1027 */     l4 += m;
/* 1028 */     if (i > k) i = k;
/* 1029 */     if (j > m) j = m;
/* 1030 */     if (l1 < l3) l1 = l3;
/* 1031 */     if (l2 < l4) l2 = l4;
/* 1032 */     l1 -= i;
/* 1033 */     l2 -= j;
/*      */ 
/* 1037 */     if (l1 > 2147483647L) l1 = 2147483647L;
/* 1038 */     if (l2 > 2147483647L) l2 = 2147483647L;
/* 1039 */     reshape(i, j, (int)l1, (int)l2);
/*      */   }
/*      */ 
/*      */   public void grow(int paramInt1, int paramInt2)
/*      */   {
/* 1066 */     long l1 = this.x;
/* 1067 */     long l2 = this.y;
/* 1068 */     long l3 = this.width;
/* 1069 */     long l4 = this.height;
/* 1070 */     l3 += l1;
/* 1071 */     l4 += l2;
/*      */ 
/* 1073 */     l1 -= paramInt1;
/* 1074 */     l2 -= paramInt2;
/* 1075 */     l3 += paramInt1;
/* 1076 */     l4 += paramInt2;
/*      */ 
/* 1078 */     if (l3 < l1)
/*      */     {
/* 1083 */       l3 -= l1;
/* 1084 */       if (l3 < -2147483648L) l3 = -2147483648L;
/* 1085 */       if (l1 < -2147483648L) l1 = -2147483648L;
/* 1086 */       else if (l1 > 2147483647L) l1 = 2147483647L;
/*      */     }
/*      */     else
/*      */     {
/* 1090 */       if (l1 < -2147483648L) l1 = -2147483648L;
/* 1091 */       else if (l1 > 2147483647L) l1 = 2147483647L;
/* 1092 */       l3 -= l1;
/*      */ 
/* 1097 */       if (l3 < -2147483648L) l3 = -2147483648L;
/* 1098 */       else if (l3 > 2147483647L) l3 = 2147483647L;
/*      */     }
/*      */ 
/* 1101 */     if (l4 < l2)
/*      */     {
/* 1103 */       l4 -= l2;
/* 1104 */       if (l4 < -2147483648L) l4 = -2147483648L;
/* 1105 */       if (l2 < -2147483648L) l2 = -2147483648L;
/* 1106 */       else if (l2 > 2147483647L) l2 = 2147483647L; 
/*      */     }
/* 1108 */     else { if (l2 < -2147483648L) l2 = -2147483648L;
/* 1109 */       else if (l2 > 2147483647L) l2 = 2147483647L;
/* 1110 */       l4 -= l2;
/* 1111 */       if (l4 < -2147483648L) l4 = -2147483648L;
/* 1112 */       else if (l4 > 2147483647L) l4 = 2147483647L;
/*      */     }
/*      */ 
/* 1115 */     reshape((int)l1, (int)l2, (int)l3, (int)l4);
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/* 1123 */     return (this.width <= 0) || (this.height <= 0);
/*      */   }
/*      */ 
/*      */   public int outcode(double paramDouble1, double paramDouble2)
/*      */   {
/* 1140 */     int i = 0;
/* 1141 */     if (this.width <= 0)
/* 1142 */       i |= 5;
/* 1143 */     else if (paramDouble1 < this.x)
/* 1144 */       i |= 1;
/* 1145 */     else if (paramDouble1 > this.x + this.width) {
/* 1146 */       i |= 4;
/*      */     }
/* 1148 */     if (this.height <= 0)
/* 1149 */       i |= 10;
/* 1150 */     else if (paramDouble2 < this.y)
/* 1151 */       i |= 2;
/* 1152 */     else if (paramDouble2 > this.y + this.height) {
/* 1153 */       i |= 8;
/*      */     }
/* 1155 */     return i;
/*      */   }
/*      */ 
/*      */   public Rectangle2D createIntersection(Rectangle2D paramRectangle2D)
/*      */   {
/* 1163 */     if ((paramRectangle2D instanceof Rectangle)) {
/* 1164 */       return intersection((Rectangle)paramRectangle2D);
/*      */     }
/* 1166 */     Rectangle2D.Double localDouble = new Rectangle2D.Double();
/* 1167 */     Rectangle2D.intersect(this, paramRectangle2D, localDouble);
/* 1168 */     return localDouble;
/*      */   }
/*      */ 
/*      */   public Rectangle2D createUnion(Rectangle2D paramRectangle2D)
/*      */   {
/* 1176 */     if ((paramRectangle2D instanceof Rectangle)) {
/* 1177 */       return union((Rectangle)paramRectangle2D);
/*      */     }
/* 1179 */     Rectangle2D.Double localDouble = new Rectangle2D.Double();
/* 1180 */     Rectangle2D.union(this, paramRectangle2D, localDouble);
/* 1181 */     return localDouble;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1197 */     if ((paramObject instanceof Rectangle)) {
/* 1198 */       Rectangle localRectangle = (Rectangle)paramObject;
/* 1199 */       return (this.x == localRectangle.x) && (this.y == localRectangle.y) && (this.width == localRectangle.width) && (this.height == localRectangle.height);
/*      */     }
/*      */ 
/* 1204 */     return super.equals(paramObject);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1214 */     return getClass().getName() + "[x=" + this.x + ",y=" + this.y + ",width=" + this.width + ",height=" + this.height + "]";
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  174 */     Toolkit.loadLibraries();
/*  175 */     if (!GraphicsEnvironment.isHeadless())
/*  176 */       initIDs();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Rectangle
 * JD-Core Version:    0.6.2
 */