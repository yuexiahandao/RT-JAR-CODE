/*      */ package java.awt.geom;
/*      */ 
/*      */ import java.awt.Shape;
/*      */ import java.beans.ConstructorProperties;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ 
/*      */ public class AffineTransform
/*      */   implements Cloneable, Serializable
/*      */ {
/*      */   private static final int TYPE_UNKNOWN = -1;
/*      */   public static final int TYPE_IDENTITY = 0;
/*      */   public static final int TYPE_TRANSLATION = 1;
/*      */   public static final int TYPE_UNIFORM_SCALE = 2;
/*      */   public static final int TYPE_GENERAL_SCALE = 4;
/*      */   public static final int TYPE_MASK_SCALE = 6;
/*      */   public static final int TYPE_FLIP = 64;
/*      */   public static final int TYPE_QUADRANT_ROTATION = 8;
/*      */   public static final int TYPE_GENERAL_ROTATION = 16;
/*      */   public static final int TYPE_MASK_ROTATION = 24;
/*      */   public static final int TYPE_GENERAL_TRANSFORM = 32;
/*      */   static final int APPLY_IDENTITY = 0;
/*      */   static final int APPLY_TRANSLATE = 1;
/*      */   static final int APPLY_SCALE = 2;
/*      */   static final int APPLY_SHEAR = 4;
/*      */   private static final int HI_SHIFT = 3;
/*      */   private static final int HI_IDENTITY = 0;
/*      */   private static final int HI_TRANSLATE = 8;
/*      */   private static final int HI_SCALE = 16;
/*      */   private static final int HI_SHEAR = 32;
/*      */   double m00;
/*      */   double m10;
/*      */   double m01;
/*      */   double m11;
/*      */   double m02;
/*      */   double m12;
/*      */   transient int state;
/*      */   private transient int type;
/* 1329 */   private static final int[] rot90conversion = { 4, 5, 4, 5, 2, 3, 6, 7 };
/*      */   private static final long serialVersionUID = 1330973210523860834L;
/*      */ 
/*      */   private AffineTransform(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, int paramInt)
/*      */   {
/*  460 */     this.m00 = paramDouble1;
/*  461 */     this.m10 = paramDouble2;
/*  462 */     this.m01 = paramDouble3;
/*  463 */     this.m11 = paramDouble4;
/*  464 */     this.m02 = paramDouble5;
/*  465 */     this.m12 = paramDouble6;
/*  466 */     this.state = paramInt;
/*  467 */     this.type = -1;
/*      */   }
/*      */ 
/*      */   public AffineTransform()
/*      */   {
/*  476 */     this.m00 = (this.m11 = 1.0D);
/*      */   }
/*      */ 
/*      */   public AffineTransform(AffineTransform paramAffineTransform)
/*      */   {
/*  489 */     this.m00 = paramAffineTransform.m00;
/*  490 */     this.m10 = paramAffineTransform.m10;
/*  491 */     this.m01 = paramAffineTransform.m01;
/*  492 */     this.m11 = paramAffineTransform.m11;
/*  493 */     this.m02 = paramAffineTransform.m02;
/*  494 */     this.m12 = paramAffineTransform.m12;
/*  495 */     this.state = paramAffineTransform.state;
/*  496 */     this.type = paramAffineTransform.type;
/*      */   }
/*      */ 
/*      */   @ConstructorProperties({"scaleX", "shearY", "shearX", "scaleY", "translateX", "translateY"})
/*      */   public AffineTransform(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
/*      */   {
/*  516 */     this.m00 = paramFloat1;
/*  517 */     this.m10 = paramFloat2;
/*  518 */     this.m01 = paramFloat3;
/*  519 */     this.m11 = paramFloat4;
/*  520 */     this.m02 = paramFloat5;
/*  521 */     this.m12 = paramFloat6;
/*  522 */     updateState();
/*      */   }
/*      */ 
/*      */   public AffineTransform(float[] paramArrayOfFloat)
/*      */   {
/*  539 */     this.m00 = paramArrayOfFloat[0];
/*  540 */     this.m10 = paramArrayOfFloat[1];
/*  541 */     this.m01 = paramArrayOfFloat[2];
/*  542 */     this.m11 = paramArrayOfFloat[3];
/*  543 */     if (paramArrayOfFloat.length > 5) {
/*  544 */       this.m02 = paramArrayOfFloat[4];
/*  545 */       this.m12 = paramArrayOfFloat[5];
/*      */     }
/*  547 */     updateState();
/*      */   }
/*      */ 
/*      */   public AffineTransform(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
/*      */   {
/*  566 */     this.m00 = paramDouble1;
/*  567 */     this.m10 = paramDouble2;
/*  568 */     this.m01 = paramDouble3;
/*  569 */     this.m11 = paramDouble4;
/*  570 */     this.m02 = paramDouble5;
/*  571 */     this.m12 = paramDouble6;
/*  572 */     updateState();
/*      */   }
/*      */ 
/*      */   public AffineTransform(double[] paramArrayOfDouble)
/*      */   {
/*  589 */     this.m00 = paramArrayOfDouble[0];
/*  590 */     this.m10 = paramArrayOfDouble[1];
/*  591 */     this.m01 = paramArrayOfDouble[2];
/*  592 */     this.m11 = paramArrayOfDouble[3];
/*  593 */     if (paramArrayOfDouble.length > 5) {
/*  594 */       this.m02 = paramArrayOfDouble[4];
/*  595 */       this.m12 = paramArrayOfDouble[5];
/*      */     }
/*  597 */     updateState();
/*      */   }
/*      */ 
/*      */   public static AffineTransform getTranslateInstance(double paramDouble1, double paramDouble2)
/*      */   {
/*  617 */     AffineTransform localAffineTransform = new AffineTransform();
/*  618 */     localAffineTransform.setToTranslation(paramDouble1, paramDouble2);
/*  619 */     return localAffineTransform;
/*      */   }
/*      */ 
/*      */   public static AffineTransform getRotateInstance(double paramDouble)
/*      */   {
/*  641 */     AffineTransform localAffineTransform = new AffineTransform();
/*  642 */     localAffineTransform.setToRotation(paramDouble);
/*  643 */     return localAffineTransform;
/*      */   }
/*      */ 
/*      */   public static AffineTransform getRotateInstance(double paramDouble1, double paramDouble2, double paramDouble3)
/*      */   {
/*  685 */     AffineTransform localAffineTransform = new AffineTransform();
/*  686 */     localAffineTransform.setToRotation(paramDouble1, paramDouble2, paramDouble3);
/*  687 */     return localAffineTransform;
/*      */   }
/*      */ 
/*      */   public static AffineTransform getRotateInstance(double paramDouble1, double paramDouble2)
/*      */   {
/*  711 */     AffineTransform localAffineTransform = new AffineTransform();
/*  712 */     localAffineTransform.setToRotation(paramDouble1, paramDouble2);
/*  713 */     return localAffineTransform;
/*      */   }
/*      */ 
/*      */   public static AffineTransform getRotateInstance(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/*  746 */     AffineTransform localAffineTransform = new AffineTransform();
/*  747 */     localAffineTransform.setToRotation(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*  748 */     return localAffineTransform;
/*      */   }
/*      */ 
/*      */   public static AffineTransform getQuadrantRotateInstance(int paramInt)
/*      */   {
/*  766 */     AffineTransform localAffineTransform = new AffineTransform();
/*  767 */     localAffineTransform.setToQuadrantRotation(paramInt);
/*  768 */     return localAffineTransform;
/*      */   }
/*      */ 
/*      */   public static AffineTransform getQuadrantRotateInstance(int paramInt, double paramDouble1, double paramDouble2)
/*      */   {
/*  794 */     AffineTransform localAffineTransform = new AffineTransform();
/*  795 */     localAffineTransform.setToQuadrantRotation(paramInt, paramDouble1, paramDouble2);
/*  796 */     return localAffineTransform;
/*      */   }
/*      */ 
/*      */   public static AffineTransform getScaleInstance(double paramDouble1, double paramDouble2)
/*      */   {
/*  816 */     AffineTransform localAffineTransform = new AffineTransform();
/*  817 */     localAffineTransform.setToScale(paramDouble1, paramDouble2);
/*  818 */     return localAffineTransform;
/*      */   }
/*      */ 
/*      */   public static AffineTransform getShearInstance(double paramDouble1, double paramDouble2)
/*      */   {
/*  838 */     AffineTransform localAffineTransform = new AffineTransform();
/*  839 */     localAffineTransform.setToShear(paramDouble1, paramDouble2);
/*  840 */     return localAffineTransform;
/*      */   }
/*      */ 
/*      */   public int getType()
/*      */   {
/*  868 */     if (this.type == -1) {
/*  869 */       calculateType();
/*      */     }
/*  871 */     return this.type;
/*      */   }
/*      */ 
/*      */   private void calculateType()
/*      */   {
/*  880 */     int i = 0;
/*      */ 
/*  883 */     updateState();
/*      */     double d1;
/*      */     double d2;
/*      */     int j;
/*      */     int k;
/*  884 */     switch (this.state) {
/*      */     default:
/*  886 */       stateError();
/*      */     case 7:
/*  889 */       i = 1;
/*      */     case 6:
/*      */       double d3;
/*      */       double d4;
/*  892 */       if ((d1 = this.m00) * (d3 = this.m01) + (d4 = this.m10) * (d2 = this.m11) != 0.0D)
/*      */       {
/*  894 */         this.type = 32;
/*  895 */         return;
/*      */       }
/*  897 */       j = d1 >= 0.0D ? 1 : 0;
/*  898 */       k = d2 >= 0.0D ? 1 : 0;
/*  899 */       if (j == k)
/*      */       {
/*  902 */         if ((d1 != d2) || (d3 != -d4))
/*  903 */           i |= 20;
/*  904 */         else if (d1 * d2 - d3 * d4 != 1.0D)
/*  905 */           i |= 18;
/*      */         else {
/*  907 */           i |= 16;
/*      */         }
/*      */ 
/*      */       }
/*  912 */       else if ((d1 != -d2) || (d3 != d4)) {
/*  913 */         i |= 84;
/*      */       }
/*  916 */       else if (d1 * d2 - d3 * d4 != 1.0D) {
/*  917 */         i |= 82;
/*      */       }
/*      */       else
/*      */       {
/*  921 */         i |= 80;
/*      */       }
/*      */ 
/*  924 */       break;
/*      */     case 5:
/*  926 */       i = 1;
/*      */     case 4:
/*  929 */       j = (d1 = this.m01) >= 0.0D ? 1 : 0;
/*  930 */       k = (d2 = this.m10) >= 0.0D ? 1 : 0;
/*  931 */       if (j != k)
/*      */       {
/*  933 */         if (d1 != -d2)
/*  934 */           i |= 12;
/*  935 */         else if ((d1 != 1.0D) && (d1 != -1.0D))
/*  936 */           i |= 10;
/*      */         else {
/*  938 */           i |= 8;
/*      */         }
/*      */ 
/*      */       }
/*  942 */       else if (d1 == d2) {
/*  943 */         i |= 74;
/*      */       }
/*      */       else
/*      */       {
/*  947 */         i |= 76;
/*      */       }
/*      */ 
/*  952 */       break;
/*      */     case 3:
/*  954 */       i = 1;
/*      */     case 2:
/*  957 */       j = (d1 = this.m00) >= 0.0D ? 1 : 0;
/*  958 */       k = (d2 = this.m11) >= 0.0D ? 1 : 0;
/*  959 */       if (j == k) {
/*  960 */         if (j != 0)
/*      */         {
/*  963 */           if (d1 == d2)
/*  964 */             i |= 2;
/*      */           else {
/*  966 */             i |= 4;
/*      */           }
/*      */ 
/*      */         }
/*  970 */         else if (d1 != d2)
/*  971 */           i |= 12;
/*  972 */         else if (d1 != -1.0D)
/*  973 */           i |= 10;
/*      */         else {
/*  975 */           i |= 8;
/*      */         }
/*      */ 
/*      */       }
/*  980 */       else if (d1 == -d2) {
/*  981 */         if ((d1 == 1.0D) || (d1 == -1.0D))
/*  982 */           i |= 64;
/*      */         else
/*  984 */           i |= 66;
/*      */       }
/*      */       else {
/*  987 */         i |= 68;
/*      */       }
/*      */ 
/*  990 */       break;
/*      */     case 1:
/*  992 */       i = 1;
/*  993 */       break;
/*      */     case 0:
/*      */     }
/*      */ 
/*  997 */     this.type = i;
/*      */   }
/*      */ 
/*      */   public double getDeterminant()
/*      */   {
/* 1042 */     switch (this.state) {
/*      */     default:
/* 1044 */       stateError();
/*      */     case 6:
/*      */     case 7:
/* 1048 */       return this.m00 * this.m11 - this.m01 * this.m10;
/*      */     case 4:
/*      */     case 5:
/* 1051 */       return -(this.m01 * this.m10);
/*      */     case 2:
/*      */     case 3:
/* 1054 */       return this.m00 * this.m11;
/*      */     case 0:
/*      */     case 1:
/* 1057 */     }return 1.0D;
/*      */   }
/*      */ 
/*      */   void updateState()
/*      */   {
/* 1084 */     if ((this.m01 == 0.0D) && (this.m10 == 0.0D)) {
/* 1085 */       if ((this.m00 == 1.0D) && (this.m11 == 1.0D)) {
/* 1086 */         if ((this.m02 == 0.0D) && (this.m12 == 0.0D)) {
/* 1087 */           this.state = 0;
/* 1088 */           this.type = 0;
/*      */         } else {
/* 1090 */           this.state = 1;
/* 1091 */           this.type = 1;
/*      */         }
/*      */       }
/* 1094 */       else if ((this.m02 == 0.0D) && (this.m12 == 0.0D)) {
/* 1095 */         this.state = 2;
/* 1096 */         this.type = -1;
/*      */       } else {
/* 1098 */         this.state = 3;
/* 1099 */         this.type = -1;
/*      */       }
/*      */ 
/*      */     }
/* 1103 */     else if ((this.m00 == 0.0D) && (this.m11 == 0.0D)) {
/* 1104 */       if ((this.m02 == 0.0D) && (this.m12 == 0.0D)) {
/* 1105 */         this.state = 4;
/* 1106 */         this.type = -1;
/*      */       } else {
/* 1108 */         this.state = 5;
/* 1109 */         this.type = -1;
/*      */       }
/*      */     }
/* 1112 */     else if ((this.m02 == 0.0D) && (this.m12 == 0.0D)) {
/* 1113 */       this.state = 6;
/* 1114 */       this.type = -1;
/*      */     } else {
/* 1116 */       this.state = 7;
/* 1117 */       this.type = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void stateError()
/*      */   {
/* 1128 */     throw new InternalError("missing case in transform state switch");
/*      */   }
/*      */ 
/*      */   public void getMatrix(double[] paramArrayOfDouble)
/*      */   {
/* 1151 */     paramArrayOfDouble[0] = this.m00;
/* 1152 */     paramArrayOfDouble[1] = this.m10;
/* 1153 */     paramArrayOfDouble[2] = this.m01;
/* 1154 */     paramArrayOfDouble[3] = this.m11;
/* 1155 */     if (paramArrayOfDouble.length > 5) {
/* 1156 */       paramArrayOfDouble[4] = this.m02;
/* 1157 */       paramArrayOfDouble[5] = this.m12;
/*      */     }
/*      */   }
/*      */ 
/*      */   public double getScaleX()
/*      */   {
/* 1170 */     return this.m00;
/*      */   }
/*      */ 
/*      */   public double getScaleY()
/*      */   {
/* 1182 */     return this.m11;
/*      */   }
/*      */ 
/*      */   public double getShearX()
/*      */   {
/* 1194 */     return this.m01;
/*      */   }
/*      */ 
/*      */   public double getShearY()
/*      */   {
/* 1206 */     return this.m10;
/*      */   }
/*      */ 
/*      */   public double getTranslateX()
/*      */   {
/* 1218 */     return this.m02;
/*      */   }
/*      */ 
/*      */   public double getTranslateY()
/*      */   {
/* 1230 */     return this.m12;
/*      */   }
/*      */ 
/*      */   public void translate(double paramDouble1, double paramDouble2)
/*      */   {
/* 1249 */     switch (this.state) {
/*      */     default:
/* 1251 */       stateError();
/*      */     case 7:
/* 1254 */       this.m02 = (paramDouble1 * this.m00 + paramDouble2 * this.m01 + this.m02);
/* 1255 */       this.m12 = (paramDouble1 * this.m10 + paramDouble2 * this.m11 + this.m12);
/* 1256 */       if ((this.m02 == 0.0D) && (this.m12 == 0.0D)) {
/* 1257 */         this.state = 6;
/* 1258 */         if (this.type != -1) {
/* 1259 */           this.type -= 1;
/*      */         }
/*      */       }
/* 1262 */       return;
/*      */     case 6:
/* 1264 */       this.m02 = (paramDouble1 * this.m00 + paramDouble2 * this.m01);
/* 1265 */       this.m12 = (paramDouble1 * this.m10 + paramDouble2 * this.m11);
/* 1266 */       if ((this.m02 != 0.0D) || (this.m12 != 0.0D)) {
/* 1267 */         this.state = 7;
/* 1268 */         this.type |= 1;
/*      */       }
/* 1270 */       return;
/*      */     case 5:
/* 1272 */       this.m02 = (paramDouble2 * this.m01 + this.m02);
/* 1273 */       this.m12 = (paramDouble1 * this.m10 + this.m12);
/* 1274 */       if ((this.m02 == 0.0D) && (this.m12 == 0.0D)) {
/* 1275 */         this.state = 4;
/* 1276 */         if (this.type != -1) {
/* 1277 */           this.type -= 1;
/*      */         }
/*      */       }
/* 1280 */       return;
/*      */     case 4:
/* 1282 */       this.m02 = (paramDouble2 * this.m01);
/* 1283 */       this.m12 = (paramDouble1 * this.m10);
/* 1284 */       if ((this.m02 != 0.0D) || (this.m12 != 0.0D)) {
/* 1285 */         this.state = 5;
/* 1286 */         this.type |= 1;
/*      */       }
/* 1288 */       return;
/*      */     case 3:
/* 1290 */       this.m02 = (paramDouble1 * this.m00 + this.m02);
/* 1291 */       this.m12 = (paramDouble2 * this.m11 + this.m12);
/* 1292 */       if ((this.m02 == 0.0D) && (this.m12 == 0.0D)) {
/* 1293 */         this.state = 2;
/* 1294 */         if (this.type != -1) {
/* 1295 */           this.type -= 1;
/*      */         }
/*      */       }
/* 1298 */       return;
/*      */     case 2:
/* 1300 */       this.m02 = (paramDouble1 * this.m00);
/* 1301 */       this.m12 = (paramDouble2 * this.m11);
/* 1302 */       if ((this.m02 != 0.0D) || (this.m12 != 0.0D)) {
/* 1303 */         this.state = 3;
/* 1304 */         this.type |= 1;
/*      */       }
/* 1306 */       return;
/*      */     case 1:
/* 1308 */       this.m02 = (paramDouble1 + this.m02);
/* 1309 */       this.m12 = (paramDouble2 + this.m12);
/* 1310 */       if ((this.m02 == 0.0D) && (this.m12 == 0.0D)) {
/* 1311 */         this.state = 0;
/* 1312 */         this.type = 0;
/*      */       }
/* 1314 */       return;
/*      */     case 0:
/* 1316 */     }this.m02 = paramDouble1;
/* 1317 */     this.m12 = paramDouble2;
/* 1318 */     if ((paramDouble1 != 0.0D) || (paramDouble2 != 0.0D)) {
/* 1319 */       this.state = 1;
/* 1320 */       this.type = 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void rotate90()
/*      */   {
/* 1340 */     double d = this.m00;
/* 1341 */     this.m00 = this.m01;
/* 1342 */     this.m01 = (-d);
/* 1343 */     d = this.m10;
/* 1344 */     this.m10 = this.m11;
/* 1345 */     this.m11 = (-d);
/* 1346 */     int i = rot90conversion[this.state];
/* 1347 */     if (((i & 0x6) == 2) && (this.m00 == 1.0D) && (this.m11 == 1.0D))
/*      */     {
/* 1350 */       i -= 2;
/*      */     }
/* 1352 */     this.state = i;
/* 1353 */     this.type = -1;
/*      */   }
/*      */   private final void rotate180() {
/* 1356 */     this.m00 = (-this.m00);
/* 1357 */     this.m11 = (-this.m11);
/* 1358 */     int i = this.state;
/* 1359 */     if ((i & 0x4) != 0)
/*      */     {
/* 1362 */       this.m01 = (-this.m01);
/* 1363 */       this.m10 = (-this.m10);
/*      */     }
/* 1367 */     else if ((this.m00 == 1.0D) && (this.m11 == 1.0D)) {
/* 1368 */       this.state = (i & 0xFFFFFFFD);
/*      */     } else {
/* 1370 */       this.state = (i | 0x2);
/*      */     }
/*      */ 
/* 1373 */     this.type = -1;
/*      */   }
/*      */   private final void rotate270() {
/* 1376 */     double d = this.m00;
/* 1377 */     this.m00 = (-this.m01);
/* 1378 */     this.m01 = d;
/* 1379 */     d = this.m10;
/* 1380 */     this.m10 = (-this.m11);
/* 1381 */     this.m11 = d;
/* 1382 */     int i = rot90conversion[this.state];
/* 1383 */     if (((i & 0x6) == 2) && (this.m00 == 1.0D) && (this.m11 == 1.0D))
/*      */     {
/* 1386 */       i -= 2;
/*      */     }
/* 1388 */     this.state = i;
/* 1389 */     this.type = -1;
/*      */   }
/*      */ 
/*      */   public void rotate(double paramDouble)
/*      */   {
/* 1410 */     double d1 = Math.sin(paramDouble);
/* 1411 */     if (d1 == 1.0D) {
/* 1412 */       rotate90();
/* 1413 */     } else if (d1 == -1.0D) {
/* 1414 */       rotate270();
/*      */     } else {
/* 1416 */       double d2 = Math.cos(paramDouble);
/* 1417 */       if (d2 == -1.0D) {
/* 1418 */         rotate180();
/* 1419 */       } else if (d2 != 1.0D)
/*      */       {
/* 1421 */         double d3 = this.m00;
/* 1422 */         double d4 = this.m01;
/* 1423 */         this.m00 = (d2 * d3 + d1 * d4);
/* 1424 */         this.m01 = (-d1 * d3 + d2 * d4);
/* 1425 */         d3 = this.m10;
/* 1426 */         d4 = this.m11;
/* 1427 */         this.m10 = (d2 * d3 + d1 * d4);
/* 1428 */         this.m11 = (-d1 * d3 + d2 * d4);
/* 1429 */         updateState();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rotate(double paramDouble1, double paramDouble2, double paramDouble3)
/*      */   {
/* 1462 */     translate(paramDouble2, paramDouble3);
/* 1463 */     rotate(paramDouble1);
/* 1464 */     translate(-paramDouble2, -paramDouble3);
/*      */   }
/*      */ 
/*      */   public void rotate(double paramDouble1, double paramDouble2)
/*      */   {
/* 1486 */     if (paramDouble2 == 0.0D) {
/* 1487 */       if (paramDouble1 < 0.0D) {
/* 1488 */         rotate180();
/*      */       }
/*      */ 
/*      */     }
/* 1492 */     else if (paramDouble1 == 0.0D) {
/* 1493 */       if (paramDouble2 > 0.0D)
/* 1494 */         rotate90();
/*      */       else
/* 1496 */         rotate270();
/*      */     }
/*      */     else {
/* 1499 */       double d1 = Math.sqrt(paramDouble1 * paramDouble1 + paramDouble2 * paramDouble2);
/* 1500 */       double d2 = paramDouble2 / d1;
/* 1501 */       double d3 = paramDouble1 / d1;
/*      */ 
/* 1503 */       double d4 = this.m00;
/* 1504 */       double d5 = this.m01;
/* 1505 */       this.m00 = (d3 * d4 + d2 * d5);
/* 1506 */       this.m01 = (-d2 * d4 + d3 * d5);
/* 1507 */       d4 = this.m10;
/* 1508 */       d5 = this.m11;
/* 1509 */       this.m10 = (d3 * d4 + d2 * d5);
/* 1510 */       this.m11 = (-d2 * d4 + d3 * d5);
/* 1511 */       updateState();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rotate(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/* 1541 */     translate(paramDouble3, paramDouble4);
/* 1542 */     rotate(paramDouble1, paramDouble2);
/* 1543 */     translate(-paramDouble3, -paramDouble4);
/*      */   }
/*      */ 
/*      */   public void quadrantRotate(int paramInt)
/*      */   {
/* 1559 */     switch (paramInt & 0x3) {
/*      */     case 0:
/* 1561 */       break;
/*      */     case 1:
/* 1563 */       rotate90();
/* 1564 */       break;
/*      */     case 2:
/* 1566 */       rotate180();
/* 1567 */       break;
/*      */     case 3:
/* 1569 */       rotate270();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void quadrantRotate(int paramInt, double paramDouble1, double paramDouble2)
/*      */   {
/* 1593 */     switch (paramInt & 0x3) {
/*      */     case 0:
/* 1595 */       return;
/*      */     case 1:
/* 1597 */       this.m02 += paramDouble1 * (this.m00 - this.m01) + paramDouble2 * (this.m01 + this.m00);
/* 1598 */       this.m12 += paramDouble1 * (this.m10 - this.m11) + paramDouble2 * (this.m11 + this.m10);
/* 1599 */       rotate90();
/* 1600 */       break;
/*      */     case 2:
/* 1602 */       this.m02 += paramDouble1 * (this.m00 + this.m00) + paramDouble2 * (this.m01 + this.m01);
/* 1603 */       this.m12 += paramDouble1 * (this.m10 + this.m10) + paramDouble2 * (this.m11 + this.m11);
/* 1604 */       rotate180();
/* 1605 */       break;
/*      */     case 3:
/* 1607 */       this.m02 += paramDouble1 * (this.m00 + this.m01) + paramDouble2 * (this.m01 - this.m00);
/* 1608 */       this.m12 += paramDouble1 * (this.m10 + this.m11) + paramDouble2 * (this.m11 - this.m10);
/* 1609 */       rotate270();
/*      */     }
/*      */ 
/* 1612 */     if ((this.m02 == 0.0D) && (this.m12 == 0.0D))
/* 1613 */       this.state &= -2;
/*      */     else
/* 1615 */       this.state |= 1;
/*      */   }
/*      */ 
/*      */   public void scale(double paramDouble1, double paramDouble2)
/*      */   {
/* 1635 */     int i = this.state;
/* 1636 */     switch (i) {
/*      */     default:
/* 1638 */       stateError();
/*      */     case 6:
/*      */     case 7:
/* 1642 */       this.m00 *= paramDouble1;
/* 1643 */       this.m11 *= paramDouble2;
/*      */     case 4:
/*      */     case 5:
/* 1647 */       this.m01 *= paramDouble2;
/* 1648 */       this.m10 *= paramDouble1;
/* 1649 */       if ((this.m01 == 0.0D) && (this.m10 == 0.0D)) {
/* 1650 */         i &= 1;
/* 1651 */         if ((this.m00 == 1.0D) && (this.m11 == 1.0D)) {
/* 1652 */           this.type = (i == 0 ? 0 : 1);
/*      */         }
/*      */         else
/*      */         {
/* 1656 */           i |= 2;
/* 1657 */           this.type = -1;
/*      */         }
/* 1659 */         this.state = i;
/*      */       }
/* 1661 */       return;
/*      */     case 2:
/*      */     case 3:
/* 1664 */       this.m00 *= paramDouble1;
/* 1665 */       this.m11 *= paramDouble2;
/* 1666 */       if ((this.m00 == 1.0D) && (this.m11 == 1.0D)) {
/* 1667 */         this.state = (i &= 1);
/* 1668 */         this.type = (i == 0 ? 0 : 1);
/*      */       }
/*      */       else
/*      */       {
/* 1672 */         this.type = -1;
/*      */       }
/* 1674 */       return;
/*      */     case 0:
/*      */     case 1:
/* 1677 */     }this.m00 = paramDouble1;
/* 1678 */     this.m11 = paramDouble2;
/* 1679 */     if ((paramDouble1 != 1.0D) || (paramDouble2 != 1.0D)) {
/* 1680 */       this.state = (i | 0x2);
/* 1681 */       this.type = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void shear(double paramDouble1, double paramDouble2)
/*      */   {
/* 1703 */     int i = this.state;
/* 1704 */     switch (i) {
/*      */     default:
/* 1706 */       stateError();
/*      */     case 6:
/*      */     case 7:
/* 1711 */       double d1 = this.m00;
/* 1712 */       double d2 = this.m01;
/* 1713 */       this.m00 = (d1 + d2 * paramDouble2);
/* 1714 */       this.m01 = (d1 * paramDouble1 + d2);
/*      */ 
/* 1716 */       d1 = this.m10;
/* 1717 */       d2 = this.m11;
/* 1718 */       this.m10 = (d1 + d2 * paramDouble2);
/* 1719 */       this.m11 = (d1 * paramDouble1 + d2);
/* 1720 */       updateState();
/* 1721 */       return;
/*      */     case 4:
/*      */     case 5:
/* 1724 */       this.m00 = (this.m01 * paramDouble2);
/* 1725 */       this.m11 = (this.m10 * paramDouble1);
/* 1726 */       if ((this.m00 != 0.0D) || (this.m11 != 0.0D)) {
/* 1727 */         this.state = (i | 0x2);
/*      */       }
/* 1729 */       this.type = -1;
/* 1730 */       return;
/*      */     case 2:
/*      */     case 3:
/* 1733 */       this.m01 = (this.m00 * paramDouble1);
/* 1734 */       this.m10 = (this.m11 * paramDouble2);
/* 1735 */       if ((this.m01 != 0.0D) || (this.m10 != 0.0D)) {
/* 1736 */         this.state = (i | 0x4);
/*      */       }
/* 1738 */       this.type = -1;
/* 1739 */       return;
/*      */     case 0:
/*      */     case 1:
/* 1742 */     }this.m01 = paramDouble1;
/* 1743 */     this.m10 = paramDouble2;
/* 1744 */     if ((this.m01 != 0.0D) || (this.m10 != 0.0D)) {
/* 1745 */       this.state = (i | 0x2 | 0x4);
/* 1746 */       this.type = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setToIdentity()
/*      */   {
/* 1757 */     this.m00 = (this.m11 = 1.0D);
/* 1758 */     this.m10 = (this.m01 = this.m02 = this.m12 = 0.0D);
/* 1759 */     this.state = 0;
/* 1760 */     this.type = 0;
/*      */   }
/*      */ 
/*      */   public void setToTranslation(double paramDouble1, double paramDouble2)
/*      */   {
/* 1778 */     this.m00 = 1.0D;
/* 1779 */     this.m10 = 0.0D;
/* 1780 */     this.m01 = 0.0D;
/* 1781 */     this.m11 = 1.0D;
/* 1782 */     this.m02 = paramDouble1;
/* 1783 */     this.m12 = paramDouble2;
/* 1784 */     if ((paramDouble1 != 0.0D) || (paramDouble2 != 0.0D)) {
/* 1785 */       this.state = 1;
/* 1786 */       this.type = 1;
/*      */     } else {
/* 1788 */       this.state = 0;
/* 1789 */       this.type = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setToRotation(double paramDouble)
/*      */   {
/* 1810 */     double d1 = Math.sin(paramDouble);
/*      */     double d2;
/* 1812 */     if ((d1 == 1.0D) || (d1 == -1.0D)) {
/* 1813 */       d2 = 0.0D;
/* 1814 */       this.state = 4;
/* 1815 */       this.type = 8;
/*      */     } else {
/* 1817 */       d2 = Math.cos(paramDouble);
/* 1818 */       if (d2 == -1.0D) {
/* 1819 */         d1 = 0.0D;
/* 1820 */         this.state = 2;
/* 1821 */         this.type = 8;
/* 1822 */       } else if (d2 == 1.0D) {
/* 1823 */         d1 = 0.0D;
/* 1824 */         this.state = 0;
/* 1825 */         this.type = 0;
/*      */       } else {
/* 1827 */         this.state = 6;
/* 1828 */         this.type = 16;
/*      */       }
/*      */     }
/* 1831 */     this.m00 = d2;
/* 1832 */     this.m10 = d1;
/* 1833 */     this.m01 = (-d1);
/* 1834 */     this.m11 = d2;
/* 1835 */     this.m02 = 0.0D;
/* 1836 */     this.m12 = 0.0D;
/*      */   }
/*      */ 
/*      */   public void setToRotation(double paramDouble1, double paramDouble2, double paramDouble3)
/*      */   {
/* 1871 */     setToRotation(paramDouble1);
/* 1872 */     double d1 = this.m10;
/* 1873 */     double d2 = 1.0D - this.m00;
/* 1874 */     this.m02 = (paramDouble2 * d2 + paramDouble3 * d1);
/* 1875 */     this.m12 = (paramDouble3 * d2 - paramDouble2 * d1);
/* 1876 */     if ((this.m02 != 0.0D) || (this.m12 != 0.0D)) {
/* 1877 */       this.state |= 1;
/* 1878 */       this.type |= 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setToRotation(double paramDouble1, double paramDouble2)
/*      */   {
/*      */     double d1;
/*      */     double d2;
/* 1902 */     if (paramDouble2 == 0.0D) {
/* 1903 */       d1 = 0.0D;
/* 1904 */       if (paramDouble1 < 0.0D) {
/* 1905 */         d2 = -1.0D;
/* 1906 */         this.state = 2;
/* 1907 */         this.type = 8;
/*      */       } else {
/* 1909 */         d2 = 1.0D;
/* 1910 */         this.state = 0;
/* 1911 */         this.type = 0;
/*      */       }
/* 1913 */     } else if (paramDouble1 == 0.0D) {
/* 1914 */       d2 = 0.0D;
/* 1915 */       d1 = paramDouble2 > 0.0D ? 1.0D : -1.0D;
/* 1916 */       this.state = 4;
/* 1917 */       this.type = 8;
/*      */     } else {
/* 1919 */       double d3 = Math.sqrt(paramDouble1 * paramDouble1 + paramDouble2 * paramDouble2);
/* 1920 */       d2 = paramDouble1 / d3;
/* 1921 */       d1 = paramDouble2 / d3;
/* 1922 */       this.state = 6;
/* 1923 */       this.type = 16;
/*      */     }
/* 1925 */     this.m00 = d2;
/* 1926 */     this.m10 = d1;
/* 1927 */     this.m01 = (-d1);
/* 1928 */     this.m11 = d2;
/* 1929 */     this.m02 = 0.0D;
/* 1930 */     this.m12 = 0.0D;
/*      */   }
/*      */ 
/*      */   public void setToRotation(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/* 1958 */     setToRotation(paramDouble1, paramDouble2);
/* 1959 */     double d1 = this.m10;
/* 1960 */     double d2 = 1.0D - this.m00;
/* 1961 */     this.m02 = (paramDouble3 * d2 + paramDouble4 * d1);
/* 1962 */     this.m12 = (paramDouble4 * d2 - paramDouble3 * d1);
/* 1963 */     if ((this.m02 != 0.0D) || (this.m12 != 0.0D)) {
/* 1964 */       this.state |= 1;
/* 1965 */       this.type |= 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setToQuadrantRotation(int paramInt)
/*      */   {
/* 1982 */     switch (paramInt & 0x3) {
/*      */     case 0:
/* 1984 */       this.m00 = 1.0D;
/* 1985 */       this.m10 = 0.0D;
/* 1986 */       this.m01 = 0.0D;
/* 1987 */       this.m11 = 1.0D;
/* 1988 */       this.m02 = 0.0D;
/* 1989 */       this.m12 = 0.0D;
/* 1990 */       this.state = 0;
/* 1991 */       this.type = 0;
/* 1992 */       break;
/*      */     case 1:
/* 1994 */       this.m00 = 0.0D;
/* 1995 */       this.m10 = 1.0D;
/* 1996 */       this.m01 = -1.0D;
/* 1997 */       this.m11 = 0.0D;
/* 1998 */       this.m02 = 0.0D;
/* 1999 */       this.m12 = 0.0D;
/* 2000 */       this.state = 4;
/* 2001 */       this.type = 8;
/* 2002 */       break;
/*      */     case 2:
/* 2004 */       this.m00 = -1.0D;
/* 2005 */       this.m10 = 0.0D;
/* 2006 */       this.m01 = 0.0D;
/* 2007 */       this.m11 = -1.0D;
/* 2008 */       this.m02 = 0.0D;
/* 2009 */       this.m12 = 0.0D;
/* 2010 */       this.state = 2;
/* 2011 */       this.type = 8;
/* 2012 */       break;
/*      */     case 3:
/* 2014 */       this.m00 = 0.0D;
/* 2015 */       this.m10 = -1.0D;
/* 2016 */       this.m01 = 1.0D;
/* 2017 */       this.m11 = 0.0D;
/* 2018 */       this.m02 = 0.0D;
/* 2019 */       this.m12 = 0.0D;
/* 2020 */       this.state = 4;
/* 2021 */       this.type = 8;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setToQuadrantRotation(int paramInt, double paramDouble1, double paramDouble2)
/*      */   {
/* 2045 */     switch (paramInt & 0x3) {
/*      */     case 0:
/* 2047 */       this.m00 = 1.0D;
/* 2048 */       this.m10 = 0.0D;
/* 2049 */       this.m01 = 0.0D;
/* 2050 */       this.m11 = 1.0D;
/* 2051 */       this.m02 = 0.0D;
/* 2052 */       this.m12 = 0.0D;
/* 2053 */       this.state = 0;
/* 2054 */       this.type = 0;
/* 2055 */       break;
/*      */     case 1:
/* 2057 */       this.m00 = 0.0D;
/* 2058 */       this.m10 = 1.0D;
/* 2059 */       this.m01 = -1.0D;
/* 2060 */       this.m11 = 0.0D;
/* 2061 */       this.m02 = (paramDouble1 + paramDouble2);
/* 2062 */       this.m12 = (paramDouble2 - paramDouble1);
/* 2063 */       if ((this.m02 == 0.0D) && (this.m12 == 0.0D)) {
/* 2064 */         this.state = 4;
/* 2065 */         this.type = 8;
/*      */       } else {
/* 2067 */         this.state = 5;
/* 2068 */         this.type = 9;
/*      */       }
/* 2070 */       break;
/*      */     case 2:
/* 2072 */       this.m00 = -1.0D;
/* 2073 */       this.m10 = 0.0D;
/* 2074 */       this.m01 = 0.0D;
/* 2075 */       this.m11 = -1.0D;
/* 2076 */       this.m02 = (paramDouble1 + paramDouble1);
/* 2077 */       this.m12 = (paramDouble2 + paramDouble2);
/* 2078 */       if ((this.m02 == 0.0D) && (this.m12 == 0.0D)) {
/* 2079 */         this.state = 2;
/* 2080 */         this.type = 8;
/*      */       } else {
/* 2082 */         this.state = 3;
/* 2083 */         this.type = 9;
/*      */       }
/* 2085 */       break;
/*      */     case 3:
/* 2087 */       this.m00 = 0.0D;
/* 2088 */       this.m10 = -1.0D;
/* 2089 */       this.m01 = 1.0D;
/* 2090 */       this.m11 = 0.0D;
/* 2091 */       this.m02 = (paramDouble1 - paramDouble2);
/* 2092 */       this.m12 = (paramDouble2 + paramDouble1);
/* 2093 */       if ((this.m02 == 0.0D) && (this.m12 == 0.0D)) {
/* 2094 */         this.state = 4;
/* 2095 */         this.type = 8;
/*      */       } else {
/* 2097 */         this.state = 5;
/* 2098 */         this.type = 9;
/*      */       }
/*      */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setToScale(double paramDouble1, double paramDouble2)
/*      */   {
/* 2119 */     this.m00 = paramDouble1;
/* 2120 */     this.m10 = 0.0D;
/* 2121 */     this.m01 = 0.0D;
/* 2122 */     this.m11 = paramDouble2;
/* 2123 */     this.m02 = 0.0D;
/* 2124 */     this.m12 = 0.0D;
/* 2125 */     if ((paramDouble1 != 1.0D) || (paramDouble2 != 1.0D)) {
/* 2126 */       this.state = 2;
/* 2127 */       this.type = -1;
/*      */     } else {
/* 2129 */       this.state = 0;
/* 2130 */       this.type = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setToShear(double paramDouble1, double paramDouble2)
/*      */   {
/* 2149 */     this.m00 = 1.0D;
/* 2150 */     this.m01 = paramDouble1;
/* 2151 */     this.m10 = paramDouble2;
/* 2152 */     this.m11 = 1.0D;
/* 2153 */     this.m02 = 0.0D;
/* 2154 */     this.m12 = 0.0D;
/* 2155 */     if ((paramDouble1 != 0.0D) || (paramDouble2 != 0.0D)) {
/* 2156 */       this.state = 6;
/* 2157 */       this.type = -1;
/*      */     } else {
/* 2159 */       this.state = 0;
/* 2160 */       this.type = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setTransform(AffineTransform paramAffineTransform)
/*      */   {
/* 2172 */     this.m00 = paramAffineTransform.m00;
/* 2173 */     this.m10 = paramAffineTransform.m10;
/* 2174 */     this.m01 = paramAffineTransform.m01;
/* 2175 */     this.m11 = paramAffineTransform.m11;
/* 2176 */     this.m02 = paramAffineTransform.m02;
/* 2177 */     this.m12 = paramAffineTransform.m12;
/* 2178 */     this.state = paramAffineTransform.state;
/* 2179 */     this.type = paramAffineTransform.type;
/*      */   }
/*      */ 
/*      */   public void setTransform(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
/*      */   {
/* 2197 */     this.m00 = paramDouble1;
/* 2198 */     this.m10 = paramDouble2;
/* 2199 */     this.m01 = paramDouble3;
/* 2200 */     this.m11 = paramDouble4;
/* 2201 */     this.m02 = paramDouble5;
/* 2202 */     this.m12 = paramDouble6;
/* 2203 */     updateState();
/*      */   }
/*      */ 
/*      */   public void concatenate(AffineTransform paramAffineTransform)
/*      */   {
/* 2231 */     int i = this.state;
/* 2232 */     int j = paramAffineTransform.state;
/*      */     double d1;
/* 2233 */     switch (j << 3 | i)
/*      */     {
/*      */     case 0:
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/* 2244 */       return;
/*      */     case 56:
/* 2248 */       this.m01 = paramAffineTransform.m01;
/* 2249 */       this.m10 = paramAffineTransform.m10;
/*      */     case 24:
/* 2252 */       this.m00 = paramAffineTransform.m00;
/* 2253 */       this.m11 = paramAffineTransform.m11;
/*      */     case 8:
/* 2256 */       this.m02 = paramAffineTransform.m02;
/* 2257 */       this.m12 = paramAffineTransform.m12;
/* 2258 */       this.state = j;
/* 2259 */       this.type = paramAffineTransform.type;
/* 2260 */       return;
/*      */     case 48:
/* 2262 */       this.m01 = paramAffineTransform.m01;
/* 2263 */       this.m10 = paramAffineTransform.m10;
/*      */     case 16:
/* 2266 */       this.m00 = paramAffineTransform.m00;
/* 2267 */       this.m11 = paramAffineTransform.m11;
/* 2268 */       this.state = j;
/* 2269 */       this.type = paramAffineTransform.type;
/* 2270 */       return;
/*      */     case 40:
/* 2272 */       this.m02 = paramAffineTransform.m02;
/* 2273 */       this.m12 = paramAffineTransform.m12;
/*      */     case 32:
/* 2276 */       this.m01 = paramAffineTransform.m01;
/* 2277 */       this.m10 = paramAffineTransform.m10;
/* 2278 */       this.m00 = (this.m11 = 0.0D);
/* 2279 */       this.state = j;
/* 2280 */       this.type = paramAffineTransform.type;
/* 2281 */       return;
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/* 2291 */       translate(paramAffineTransform.m02, paramAffineTransform.m12);
/* 2292 */       return;
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/* 2302 */       scale(paramAffineTransform.m00, paramAffineTransform.m11);
/* 2303 */       return;
/*      */     case 38:
/*      */     case 39:
/* 2308 */       d4 = paramAffineTransform.m01; d5 = paramAffineTransform.m10;
/* 2309 */       d1 = this.m00;
/* 2310 */       this.m00 = (this.m01 * d5);
/* 2311 */       this.m01 = (d1 * d4);
/* 2312 */       d1 = this.m10;
/* 2313 */       this.m10 = (this.m11 * d5);
/* 2314 */       this.m11 = (d1 * d4);
/* 2315 */       this.type = -1;
/* 2316 */       return;
/*      */     case 36:
/*      */     case 37:
/* 2319 */       this.m00 = (this.m01 * paramAffineTransform.m10);
/* 2320 */       this.m01 = 0.0D;
/* 2321 */       this.m11 = (this.m10 * paramAffineTransform.m01);
/* 2322 */       this.m10 = 0.0D;
/* 2323 */       this.state = (i ^ 0x6);
/* 2324 */       this.type = -1;
/* 2325 */       return;
/*      */     case 34:
/*      */     case 35:
/* 2328 */       this.m01 = (this.m00 * paramAffineTransform.m01);
/* 2329 */       this.m00 = 0.0D;
/* 2330 */       this.m10 = (this.m11 * paramAffineTransform.m10);
/* 2331 */       this.m11 = 0.0D;
/* 2332 */       this.state = (i ^ 0x6);
/* 2333 */       this.type = -1;
/* 2334 */       return;
/*      */     case 33:
/* 2336 */       this.m00 = 0.0D;
/* 2337 */       this.m01 = paramAffineTransform.m01;
/* 2338 */       this.m10 = paramAffineTransform.m10;
/* 2339 */       this.m11 = 0.0D;
/* 2340 */       this.state = 5;
/* 2341 */       this.type = -1;
/* 2342 */       return;
/*      */     case 25:
/*      */     case 26:
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 41:
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*      */     case 45:
/*      */     case 46:
/*      */     case 47:
/*      */     case 49:
/*      */     case 50:
/*      */     case 51:
/*      */     case 52:
/*      */     case 53:
/*      */     case 54:
/* 2346 */     case 55: } double d3 = paramAffineTransform.m00; double d4 = paramAffineTransform.m01; double d7 = paramAffineTransform.m02;
/* 2347 */     double d5 = paramAffineTransform.m10; double d6 = paramAffineTransform.m11; double d8 = paramAffineTransform.m12;
/* 2348 */     switch (i) {
/*      */     default:
/* 2350 */       stateError();
/*      */     case 6:
/* 2353 */       this.state = (i | j);
/*      */     case 7:
/* 2356 */       d1 = this.m00;
/* 2357 */       double d2 = this.m01;
/* 2358 */       this.m00 = (d3 * d1 + d5 * d2);
/* 2359 */       this.m01 = (d4 * d1 + d6 * d2);
/* 2360 */       this.m02 += d7 * d1 + d8 * d2;
/*      */ 
/* 2362 */       d1 = this.m10;
/* 2363 */       d2 = this.m11;
/* 2364 */       this.m10 = (d3 * d1 + d5 * d2);
/* 2365 */       this.m11 = (d4 * d1 + d6 * d2);
/* 2366 */       this.m12 += d7 * d1 + d8 * d2;
/* 2367 */       this.type = -1;
/* 2368 */       return;
/*      */     case 4:
/*      */     case 5:
/* 2372 */       d1 = this.m01;
/* 2373 */       this.m00 = (d5 * d1);
/* 2374 */       this.m01 = (d6 * d1);
/* 2375 */       this.m02 += d8 * d1;
/*      */ 
/* 2377 */       d1 = this.m10;
/* 2378 */       this.m10 = (d3 * d1);
/* 2379 */       this.m11 = (d4 * d1);
/* 2380 */       this.m12 += d7 * d1;
/* 2381 */       break;
/*      */     case 2:
/*      */     case 3:
/* 2385 */       d1 = this.m00;
/* 2386 */       this.m00 = (d3 * d1);
/* 2387 */       this.m01 = (d4 * d1);
/* 2388 */       this.m02 += d7 * d1;
/*      */ 
/* 2390 */       d1 = this.m11;
/* 2391 */       this.m10 = (d5 * d1);
/* 2392 */       this.m11 = (d6 * d1);
/* 2393 */       this.m12 += d8 * d1;
/* 2394 */       break;
/*      */     case 1:
/* 2397 */       this.m00 = d3;
/* 2398 */       this.m01 = d4;
/* 2399 */       this.m02 += d7;
/*      */ 
/* 2401 */       this.m10 = d5;
/* 2402 */       this.m11 = d6;
/* 2403 */       this.m12 += d8;
/* 2404 */       this.state = (j | 0x1);
/* 2405 */       this.type = -1;
/* 2406 */       return;
/*      */     }
/* 2408 */     updateState();
/*      */   }
/*      */ 
/*      */   public void preConcatenate(AffineTransform paramAffineTransform)
/*      */   {
/* 2439 */     int i = this.state;
/* 2440 */     int j = paramAffineTransform.state;
/*      */     double d1;
/* 2441 */     switch (j << 3 | i)
/*      */     {
/*      */     case 0:
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/* 2451 */       return;
/*      */     case 8:
/*      */     case 10:
/*      */     case 12:
/*      */     case 14:
/* 2458 */       this.m02 = paramAffineTransform.m02;
/* 2459 */       this.m12 = paramAffineTransform.m12;
/* 2460 */       this.state = (i | 0x1);
/* 2461 */       this.type |= 1;
/* 2462 */       return;
/*      */     case 9:
/*      */     case 11:
/*      */     case 13:
/*      */     case 15:
/* 2469 */       this.m02 += paramAffineTransform.m02;
/* 2470 */       this.m12 += paramAffineTransform.m12;
/* 2471 */       return;
/*      */     case 16:
/*      */     case 17:
/* 2476 */       this.state = (i | 0x2);
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/* 2485 */       d3 = paramAffineTransform.m00;
/* 2486 */       d6 = paramAffineTransform.m11;
/* 2487 */       if ((i & 0x4) != 0) {
/* 2488 */         this.m01 *= d3;
/* 2489 */         this.m10 *= d6;
/* 2490 */         if ((i & 0x2) != 0) {
/* 2491 */           this.m00 *= d3;
/* 2492 */           this.m11 *= d6;
/*      */         }
/*      */       } else {
/* 2495 */         this.m00 *= d3;
/* 2496 */         this.m11 *= d6;
/*      */       }
/* 2498 */       if ((i & 0x1) != 0) {
/* 2499 */         this.m02 *= d3;
/* 2500 */         this.m12 *= d6;
/*      */       }
/* 2502 */       this.type = -1;
/* 2503 */       return;
/*      */     case 36:
/*      */     case 37:
/* 2506 */       i |= 2;
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/* 2512 */       this.state = (i ^ 0x4);
/*      */     case 38:
/*      */     case 39:
/* 2517 */       d4 = paramAffineTransform.m01;
/* 2518 */       d5 = paramAffineTransform.m10;
/*      */ 
/* 2520 */       d1 = this.m00;
/* 2521 */       this.m00 = (this.m10 * d4);
/* 2522 */       this.m10 = (d1 * d5);
/*      */ 
/* 2524 */       d1 = this.m01;
/* 2525 */       this.m01 = (this.m11 * d4);
/* 2526 */       this.m11 = (d1 * d5);
/*      */ 
/* 2528 */       d1 = this.m02;
/* 2529 */       this.m02 = (this.m12 * d4);
/* 2530 */       this.m12 = (d1 * d5);
/* 2531 */       this.type = -1;
/* 2532 */       return;
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/* 2536 */     case 31: } double d3 = paramAffineTransform.m00; double d4 = paramAffineTransform.m01; double d7 = paramAffineTransform.m02;
/* 2537 */     double d5 = paramAffineTransform.m10; double d6 = paramAffineTransform.m11; double d8 = paramAffineTransform.m12;
/*      */     double d2;
/* 2538 */     switch (i) {
/*      */     default:
/* 2540 */       stateError();
/*      */     case 7:
/* 2543 */       d1 = this.m02;
/* 2544 */       d2 = this.m12;
/* 2545 */       d7 += d1 * d3 + d2 * d4;
/* 2546 */       d8 += d1 * d5 + d2 * d6;
/*      */     case 6:
/* 2550 */       this.m02 = d7;
/* 2551 */       this.m12 = d8;
/*      */ 
/* 2553 */       d1 = this.m00;
/* 2554 */       d2 = this.m10;
/* 2555 */       this.m00 = (d1 * d3 + d2 * d4);
/* 2556 */       this.m10 = (d1 * d5 + d2 * d6);
/*      */ 
/* 2558 */       d1 = this.m01;
/* 2559 */       d2 = this.m11;
/* 2560 */       this.m01 = (d1 * d3 + d2 * d4);
/* 2561 */       this.m11 = (d1 * d5 + d2 * d6);
/* 2562 */       break;
/*      */     case 5:
/* 2565 */       d1 = this.m02;
/* 2566 */       d2 = this.m12;
/* 2567 */       d7 += d1 * d3 + d2 * d4;
/* 2568 */       d8 += d1 * d5 + d2 * d6;
/*      */     case 4:
/* 2572 */       this.m02 = d7;
/* 2573 */       this.m12 = d8;
/*      */ 
/* 2575 */       d1 = this.m10;
/* 2576 */       this.m00 = (d1 * d4);
/* 2577 */       this.m10 = (d1 * d6);
/*      */ 
/* 2579 */       d1 = this.m01;
/* 2580 */       this.m01 = (d1 * d3);
/* 2581 */       this.m11 = (d1 * d5);
/* 2582 */       break;
/*      */     case 3:
/* 2585 */       d1 = this.m02;
/* 2586 */       d2 = this.m12;
/* 2587 */       d7 += d1 * d3 + d2 * d4;
/* 2588 */       d8 += d1 * d5 + d2 * d6;
/*      */     case 2:
/* 2592 */       this.m02 = d7;
/* 2593 */       this.m12 = d8;
/*      */ 
/* 2595 */       d1 = this.m00;
/* 2596 */       this.m00 = (d1 * d3);
/* 2597 */       this.m10 = (d1 * d5);
/*      */ 
/* 2599 */       d1 = this.m11;
/* 2600 */       this.m01 = (d1 * d4);
/* 2601 */       this.m11 = (d1 * d6);
/* 2602 */       break;
/*      */     case 1:
/* 2605 */       d1 = this.m02;
/* 2606 */       d2 = this.m12;
/* 2607 */       d7 += d1 * d3 + d2 * d4;
/* 2608 */       d8 += d1 * d5 + d2 * d6;
/*      */     case 0:
/* 2612 */       this.m02 = d7;
/* 2613 */       this.m12 = d8;
/*      */ 
/* 2615 */       this.m00 = d3;
/* 2616 */       this.m10 = d5;
/*      */ 
/* 2618 */       this.m01 = d4;
/* 2619 */       this.m11 = d6;
/*      */ 
/* 2621 */       this.state = (i | j);
/* 2622 */       this.type = -1;
/* 2623 */       return;
/*      */     }
/* 2625 */     updateState();
/*      */   }
/*      */ 
/*      */   public AffineTransform createInverse()
/*      */     throws NoninvertibleTransformException
/*      */   {
/*      */     double d;
/* 2654 */     switch (this.state) {
/*      */     default:
/* 2656 */       stateError();
/*      */     case 7:
/* 2659 */       d = this.m00 * this.m11 - this.m01 * this.m10;
/* 2660 */       if (Math.abs(d) <= 4.9E-324D) {
/* 2661 */         throw new NoninvertibleTransformException("Determinant is " + d);
/*      */       }
/*      */ 
/* 2664 */       return new AffineTransform(this.m11 / d, -this.m10 / d, -this.m01 / d, this.m00 / d, (this.m01 * this.m12 - this.m11 * this.m02) / d, (this.m10 * this.m02 - this.m00 * this.m12) / d, 7);
/*      */     case 6:
/* 2672 */       d = this.m00 * this.m11 - this.m01 * this.m10;
/* 2673 */       if (Math.abs(d) <= 4.9E-324D) {
/* 2674 */         throw new NoninvertibleTransformException("Determinant is " + d);
/*      */       }
/*      */ 
/* 2677 */       return new AffineTransform(this.m11 / d, -this.m10 / d, -this.m01 / d, this.m00 / d, 0.0D, 0.0D, 6);
/*      */     case 5:
/* 2682 */       if ((this.m01 == 0.0D) || (this.m10 == 0.0D)) {
/* 2683 */         throw new NoninvertibleTransformException("Determinant is 0");
/*      */       }
/* 2685 */       return new AffineTransform(0.0D, 1.0D / this.m01, 1.0D / this.m10, 0.0D, -this.m12 / this.m10, -this.m02 / this.m01, 5);
/*      */     case 4:
/* 2690 */       if ((this.m01 == 0.0D) || (this.m10 == 0.0D)) {
/* 2691 */         throw new NoninvertibleTransformException("Determinant is 0");
/*      */       }
/* 2693 */       return new AffineTransform(0.0D, 1.0D / this.m01, 1.0D / this.m10, 0.0D, 0.0D, 0.0D, 4);
/*      */     case 3:
/* 2698 */       if ((this.m00 == 0.0D) || (this.m11 == 0.0D)) {
/* 2699 */         throw new NoninvertibleTransformException("Determinant is 0");
/*      */       }
/* 2701 */       return new AffineTransform(1.0D / this.m00, 0.0D, 0.0D, 1.0D / this.m11, -this.m02 / this.m00, -this.m12 / this.m11, 3);
/*      */     case 2:
/* 2706 */       if ((this.m00 == 0.0D) || (this.m11 == 0.0D)) {
/* 2707 */         throw new NoninvertibleTransformException("Determinant is 0");
/*      */       }
/* 2709 */       return new AffineTransform(1.0D / this.m00, 0.0D, 0.0D, 1.0D / this.m11, 0.0D, 0.0D, 2);
/*      */     case 1:
/* 2714 */       return new AffineTransform(1.0D, 0.0D, 0.0D, 1.0D, -this.m02, -this.m12, 1);
/*      */     case 0:
/*      */     }
/*      */ 
/* 2719 */     return new AffineTransform();
/*      */   }
/*      */ 
/*      */   public void invert()
/*      */     throws NoninvertibleTransformException
/*      */   {
/*      */     double d1;
/*      */     double d2;
/*      */     double d3;
/*      */     double d4;
/*      */     double d5;
/*      */     double d6;
/*      */     double d7;
/* 2750 */     switch (this.state) {
/*      */     default:
/* 2752 */       stateError();
/*      */     case 7:
/* 2755 */       d1 = this.m00; d2 = this.m01; d3 = this.m02;
/* 2756 */       d4 = this.m10; d5 = this.m11; d6 = this.m12;
/* 2757 */       d7 = d1 * d5 - d2 * d4;
/* 2758 */       if (Math.abs(d7) <= 4.9E-324D) {
/* 2759 */         throw new NoninvertibleTransformException("Determinant is " + d7);
/*      */       }
/*      */ 
/* 2762 */       this.m00 = (d5 / d7);
/* 2763 */       this.m10 = (-d4 / d7);
/* 2764 */       this.m01 = (-d2 / d7);
/* 2765 */       this.m11 = (d1 / d7);
/* 2766 */       this.m02 = ((d2 * d6 - d5 * d3) / d7);
/* 2767 */       this.m12 = ((d4 * d3 - d1 * d6) / d7);
/* 2768 */       break;
/*      */     case 6:
/* 2770 */       d1 = this.m00; d2 = this.m01;
/* 2771 */       d4 = this.m10; d5 = this.m11;
/* 2772 */       d7 = d1 * d5 - d2 * d4;
/* 2773 */       if (Math.abs(d7) <= 4.9E-324D) {
/* 2774 */         throw new NoninvertibleTransformException("Determinant is " + d7);
/*      */       }
/*      */ 
/* 2777 */       this.m00 = (d5 / d7);
/* 2778 */       this.m10 = (-d4 / d7);
/* 2779 */       this.m01 = (-d2 / d7);
/* 2780 */       this.m11 = (d1 / d7);
/*      */ 
/* 2783 */       break;
/*      */     case 5:
/* 2785 */       d2 = this.m01; d3 = this.m02;
/* 2786 */       d4 = this.m10; d6 = this.m12;
/* 2787 */       if ((d2 == 0.0D) || (d4 == 0.0D)) {
/* 2788 */         throw new NoninvertibleTransformException("Determinant is 0");
/*      */       }
/*      */ 
/* 2791 */       this.m10 = (1.0D / d2);
/* 2792 */       this.m01 = (1.0D / d4);
/*      */ 
/* 2794 */       this.m02 = (-d6 / d4);
/* 2795 */       this.m12 = (-d3 / d2);
/* 2796 */       break;
/*      */     case 4:
/* 2798 */       d2 = this.m01;
/* 2799 */       d4 = this.m10;
/* 2800 */       if ((d2 == 0.0D) || (d4 == 0.0D)) {
/* 2801 */         throw new NoninvertibleTransformException("Determinant is 0");
/*      */       }
/*      */ 
/* 2804 */       this.m10 = (1.0D / d2);
/* 2805 */       this.m01 = (1.0D / d4);
/*      */ 
/* 2809 */       break;
/*      */     case 3:
/* 2811 */       d1 = this.m00; d3 = this.m02;
/* 2812 */       d5 = this.m11; d6 = this.m12;
/* 2813 */       if ((d1 == 0.0D) || (d5 == 0.0D)) {
/* 2814 */         throw new NoninvertibleTransformException("Determinant is 0");
/*      */       }
/* 2816 */       this.m00 = (1.0D / d1);
/*      */ 
/* 2819 */       this.m11 = (1.0D / d5);
/* 2820 */       this.m02 = (-d3 / d1);
/* 2821 */       this.m12 = (-d6 / d5);
/* 2822 */       break;
/*      */     case 2:
/* 2824 */       d1 = this.m00;
/* 2825 */       d5 = this.m11;
/* 2826 */       if ((d1 == 0.0D) || (d5 == 0.0D)) {
/* 2827 */         throw new NoninvertibleTransformException("Determinant is 0");
/*      */       }
/* 2829 */       this.m00 = (1.0D / d1);
/*      */ 
/* 2832 */       this.m11 = (1.0D / d5);
/*      */ 
/* 2835 */       break;
/*      */     case 1:
/* 2841 */       this.m02 = (-this.m02);
/* 2842 */       this.m12 = (-this.m12);
/* 2843 */       break;
/*      */     case 0:
/*      */     }
/*      */   }
/*      */ 
/*      */   public Point2D transform(Point2D paramPoint2D1, Point2D paramPoint2D2)
/*      */   {
/* 2874 */     if (paramPoint2D2 == null) {
/* 2875 */       if ((paramPoint2D1 instanceof Point2D.Double))
/* 2876 */         paramPoint2D2 = new Point2D.Double();
/*      */       else {
/* 2878 */         paramPoint2D2 = new Point2D.Float();
/*      */       }
/*      */     }
/*      */ 
/* 2882 */     double d1 = paramPoint2D1.getX();
/* 2883 */     double d2 = paramPoint2D1.getY();
/* 2884 */     switch (this.state) {
/*      */     default:
/* 2886 */       stateError();
/*      */     case 7:
/* 2889 */       paramPoint2D2.setLocation(d1 * this.m00 + d2 * this.m01 + this.m02, d1 * this.m10 + d2 * this.m11 + this.m12);
/*      */ 
/* 2891 */       return paramPoint2D2;
/*      */     case 6:
/* 2893 */       paramPoint2D2.setLocation(d1 * this.m00 + d2 * this.m01, d1 * this.m10 + d2 * this.m11);
/* 2894 */       return paramPoint2D2;
/*      */     case 5:
/* 2896 */       paramPoint2D2.setLocation(d2 * this.m01 + this.m02, d1 * this.m10 + this.m12);
/* 2897 */       return paramPoint2D2;
/*      */     case 4:
/* 2899 */       paramPoint2D2.setLocation(d2 * this.m01, d1 * this.m10);
/* 2900 */       return paramPoint2D2;
/*      */     case 3:
/* 2902 */       paramPoint2D2.setLocation(d1 * this.m00 + this.m02, d2 * this.m11 + this.m12);
/* 2903 */       return paramPoint2D2;
/*      */     case 2:
/* 2905 */       paramPoint2D2.setLocation(d1 * this.m00, d2 * this.m11);
/* 2906 */       return paramPoint2D2;
/*      */     case 1:
/* 2908 */       paramPoint2D2.setLocation(d1 + this.m02, d2 + this.m12);
/* 2909 */       return paramPoint2D2;
/*      */     case 0:
/* 2911 */     }paramPoint2D2.setLocation(d1, d2);
/* 2912 */     return paramPoint2D2;
/*      */   }
/*      */ 
/*      */   public void transform(Point2D[] paramArrayOfPoint2D1, int paramInt1, Point2D[] paramArrayOfPoint2D2, int paramInt2, int paramInt3)
/*      */   {
/* 2952 */     int i = this.state;
/*      */     while (true) { paramInt3--; if (paramInt3 < 0)
/*      */         break;
/* 2955 */       Point2D localPoint2D = paramArrayOfPoint2D1[(paramInt1++)];
/* 2956 */       double d1 = localPoint2D.getX();
/* 2957 */       double d2 = localPoint2D.getY();
/* 2958 */       Object localObject = paramArrayOfPoint2D2[(paramInt2++)];
/* 2959 */       if (localObject == null) {
/* 2960 */         if ((localPoint2D instanceof Point2D.Double))
/* 2961 */           localObject = new Point2D.Double();
/*      */         else {
/* 2963 */           localObject = new Point2D.Float();
/*      */         }
/* 2965 */         paramArrayOfPoint2D2[(paramInt2 - 1)] = localObject;
/*      */       }
/* 2967 */       switch (i) {
/*      */       default:
/* 2969 */         stateError();
/*      */       case 7:
/* 2972 */         ((Point2D)localObject).setLocation(d1 * this.m00 + d2 * this.m01 + this.m02, d1 * this.m10 + d2 * this.m11 + this.m12);
/*      */ 
/* 2974 */         break;
/*      */       case 6:
/* 2976 */         ((Point2D)localObject).setLocation(d1 * this.m00 + d2 * this.m01, d1 * this.m10 + d2 * this.m11);
/* 2977 */         break;
/*      */       case 5:
/* 2979 */         ((Point2D)localObject).setLocation(d2 * this.m01 + this.m02, d1 * this.m10 + this.m12);
/* 2980 */         break;
/*      */       case 4:
/* 2982 */         ((Point2D)localObject).setLocation(d2 * this.m01, d1 * this.m10);
/* 2983 */         break;
/*      */       case 3:
/* 2985 */         ((Point2D)localObject).setLocation(d1 * this.m00 + this.m02, d2 * this.m11 + this.m12);
/* 2986 */         break;
/*      */       case 2:
/* 2988 */         ((Point2D)localObject).setLocation(d1 * this.m00, d2 * this.m11);
/* 2989 */         break;
/*      */       case 1:
/* 2991 */         ((Point2D)localObject).setLocation(d1 + this.m02, d2 + this.m12);
/* 2992 */         break;
/*      */       case 0:
/* 2994 */         ((Point2D)localObject).setLocation(d1, d2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void transform(float[] paramArrayOfFloat1, int paramInt1, float[] paramArrayOfFloat2, int paramInt2, int paramInt3)
/*      */   {
/* 3027 */     if ((paramArrayOfFloat2 == paramArrayOfFloat1) && (paramInt2 > paramInt1) && (paramInt2 < paramInt1 + paramInt3 * 2))
/*      */     {
/* 3038 */       System.arraycopy(paramArrayOfFloat1, paramInt1, paramArrayOfFloat2, paramInt2, paramInt3 * 2);
/*      */ 
/* 3040 */       paramInt1 = paramInt2;
/*      */     }
/*      */     double d1;
/*      */     double d2;
/*      */     double d3;
/*      */     double d4;
/*      */     double d5;
/*      */     double d6;
/*      */     double d7;
/*      */     double d8;
/* 3042 */     switch (this.state) {
/*      */     default:
/* 3044 */       stateError();
/*      */     case 7:
/* 3047 */       d1 = this.m00; d2 = this.m01; d3 = this.m02;
/* 3048 */       d4 = this.m10; d5 = this.m11; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3050 */         d7 = paramArrayOfFloat1[(paramInt1++)];
/* 3051 */         d8 = paramArrayOfFloat1[(paramInt1++)];
/* 3052 */         paramArrayOfFloat2[(paramInt2++)] = ((float)(d1 * d7 + d2 * d8 + d3));
/* 3053 */         paramArrayOfFloat2[(paramInt2++)] = ((float)(d4 * d7 + d5 * d8 + d6));
/*      */       }
/* 3055 */       return;
/*      */     case 6:
/* 3057 */       d1 = this.m00; d2 = this.m01;
/* 3058 */       d4 = this.m10; d5 = this.m11;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3060 */         d7 = paramArrayOfFloat1[(paramInt1++)];
/* 3061 */         d8 = paramArrayOfFloat1[(paramInt1++)];
/* 3062 */         paramArrayOfFloat2[(paramInt2++)] = ((float)(d1 * d7 + d2 * d8));
/* 3063 */         paramArrayOfFloat2[(paramInt2++)] = ((float)(d4 * d7 + d5 * d8));
/*      */       }
/* 3065 */       return;
/*      */     case 5:
/* 3067 */       d2 = this.m01; d3 = this.m02;
/* 3068 */       d4 = this.m10; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3070 */         d7 = paramArrayOfFloat1[(paramInt1++)];
/* 3071 */         paramArrayOfFloat2[(paramInt2++)] = ((float)(d2 * paramArrayOfFloat1[(paramInt1++)] + d3));
/* 3072 */         paramArrayOfFloat2[(paramInt2++)] = ((float)(d4 * d7 + d6));
/*      */       }
/* 3074 */       return;
/*      */     case 4:
/* 3076 */       d2 = this.m01; d4 = this.m10;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3078 */         d7 = paramArrayOfFloat1[(paramInt1++)];
/* 3079 */         paramArrayOfFloat2[(paramInt2++)] = ((float)(d2 * paramArrayOfFloat1[(paramInt1++)]));
/* 3080 */         paramArrayOfFloat2[(paramInt2++)] = ((float)(d4 * d7));
/*      */       }
/* 3082 */       return;
/*      */     case 3:
/* 3084 */       d1 = this.m00; d3 = this.m02;
/* 3085 */       d5 = this.m11; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3087 */         paramArrayOfFloat2[(paramInt2++)] = ((float)(d1 * paramArrayOfFloat1[(paramInt1++)] + d3));
/* 3088 */         paramArrayOfFloat2[(paramInt2++)] = ((float)(d5 * paramArrayOfFloat1[(paramInt1++)] + d6));
/*      */       }
/* 3090 */       return;
/*      */     case 2:
/* 3092 */       d1 = this.m00; d5 = this.m11;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3094 */         paramArrayOfFloat2[(paramInt2++)] = ((float)(d1 * paramArrayOfFloat1[(paramInt1++)]));
/* 3095 */         paramArrayOfFloat2[(paramInt2++)] = ((float)(d5 * paramArrayOfFloat1[(paramInt1++)]));
/*      */       }
/* 3097 */       return;
/*      */     case 1:
/* 3099 */       d3 = this.m02; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3101 */         paramArrayOfFloat2[(paramInt2++)] = ((float)(paramArrayOfFloat1[(paramInt1++)] + d3));
/* 3102 */         paramArrayOfFloat2[(paramInt2++)] = ((float)(paramArrayOfFloat1[(paramInt1++)] + d6));
/*      */       }
/* 3104 */       return;
/*      */     case 0:
/* 3106 */     }if ((paramArrayOfFloat1 != paramArrayOfFloat2) || (paramInt1 != paramInt2))
/* 3107 */       System.arraycopy(paramArrayOfFloat1, paramInt1, paramArrayOfFloat2, paramInt2, paramInt3 * 2);
/*      */   }
/*      */ 
/*      */   public void transform(double[] paramArrayOfDouble1, int paramInt1, double[] paramArrayOfDouble2, int paramInt2, int paramInt3)
/*      */   {
/* 3141 */     if ((paramArrayOfDouble2 == paramArrayOfDouble1) && (paramInt2 > paramInt1) && (paramInt2 < paramInt1 + paramInt3 * 2))
/*      */     {
/* 3152 */       System.arraycopy(paramArrayOfDouble1, paramInt1, paramArrayOfDouble2, paramInt2, paramInt3 * 2);
/*      */ 
/* 3154 */       paramInt1 = paramInt2;
/*      */     }
/*      */     double d1;
/*      */     double d2;
/*      */     double d3;
/*      */     double d4;
/*      */     double d5;
/*      */     double d6;
/*      */     double d7;
/*      */     double d8;
/* 3156 */     switch (this.state) {
/*      */     default:
/* 3158 */       stateError();
/*      */     case 7:
/* 3161 */       d1 = this.m00; d2 = this.m01; d3 = this.m02;
/* 3162 */       d4 = this.m10; d5 = this.m11; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3164 */         d7 = paramArrayOfDouble1[(paramInt1++)];
/* 3165 */         d8 = paramArrayOfDouble1[(paramInt1++)];
/* 3166 */         paramArrayOfDouble2[(paramInt2++)] = (d1 * d7 + d2 * d8 + d3);
/* 3167 */         paramArrayOfDouble2[(paramInt2++)] = (d4 * d7 + d5 * d8 + d6);
/*      */       }
/* 3169 */       return;
/*      */     case 6:
/* 3171 */       d1 = this.m00; d2 = this.m01;
/* 3172 */       d4 = this.m10; d5 = this.m11;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3174 */         d7 = paramArrayOfDouble1[(paramInt1++)];
/* 3175 */         d8 = paramArrayOfDouble1[(paramInt1++)];
/* 3176 */         paramArrayOfDouble2[(paramInt2++)] = (d1 * d7 + d2 * d8);
/* 3177 */         paramArrayOfDouble2[(paramInt2++)] = (d4 * d7 + d5 * d8);
/*      */       }
/* 3179 */       return;
/*      */     case 5:
/* 3181 */       d2 = this.m01; d3 = this.m02;
/* 3182 */       d4 = this.m10; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3184 */         d7 = paramArrayOfDouble1[(paramInt1++)];
/* 3185 */         paramArrayOfDouble2[(paramInt2++)] = (d2 * paramArrayOfDouble1[(paramInt1++)] + d3);
/* 3186 */         paramArrayOfDouble2[(paramInt2++)] = (d4 * d7 + d6);
/*      */       }
/* 3188 */       return;
/*      */     case 4:
/* 3190 */       d2 = this.m01; d4 = this.m10;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3192 */         d7 = paramArrayOfDouble1[(paramInt1++)];
/* 3193 */         paramArrayOfDouble2[(paramInt2++)] = (d2 * paramArrayOfDouble1[(paramInt1++)]);
/* 3194 */         paramArrayOfDouble2[(paramInt2++)] = (d4 * d7);
/*      */       }
/* 3196 */       return;
/*      */     case 3:
/* 3198 */       d1 = this.m00; d3 = this.m02;
/* 3199 */       d5 = this.m11; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3201 */         paramArrayOfDouble2[(paramInt2++)] = (d1 * paramArrayOfDouble1[(paramInt1++)] + d3);
/* 3202 */         paramArrayOfDouble2[(paramInt2++)] = (d5 * paramArrayOfDouble1[(paramInt1++)] + d6);
/*      */       }
/* 3204 */       return;
/*      */     case 2:
/* 3206 */       d1 = this.m00; d5 = this.m11;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3208 */         paramArrayOfDouble2[(paramInt2++)] = (d1 * paramArrayOfDouble1[(paramInt1++)]);
/* 3209 */         paramArrayOfDouble2[(paramInt2++)] = (d5 * paramArrayOfDouble1[(paramInt1++)]);
/*      */       }
/* 3211 */       return;
/*      */     case 1:
/* 3213 */       d3 = this.m02; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3215 */         paramArrayOfDouble1[(paramInt1++)] += d3;
/* 3216 */         paramArrayOfDouble1[(paramInt1++)] += d6;
/*      */       }
/* 3218 */       return;
/*      */     case 0:
/* 3220 */     }if ((paramArrayOfDouble1 != paramArrayOfDouble2) || (paramInt1 != paramInt2))
/* 3221 */       System.arraycopy(paramArrayOfDouble1, paramInt1, paramArrayOfDouble2, paramInt2, paramInt3 * 2);
/*      */   }
/*      */ 
/*      */   public void transform(float[] paramArrayOfFloat, int paramInt1, double[] paramArrayOfDouble, int paramInt2, int paramInt3)
/*      */   {
/*      */     double d1;
/*      */     double d2;
/*      */     double d3;
/*      */     double d4;
/*      */     double d5;
/*      */     double d6;
/*      */     double d7;
/*      */     double d8;
/* 3251 */     switch (this.state) {
/*      */     default:
/* 3253 */       stateError();
/*      */     case 7:
/* 3256 */       d1 = this.m00; d2 = this.m01; d3 = this.m02;
/* 3257 */       d4 = this.m10; d5 = this.m11; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3259 */         d7 = paramArrayOfFloat[(paramInt1++)];
/* 3260 */         d8 = paramArrayOfFloat[(paramInt1++)];
/* 3261 */         paramArrayOfDouble[(paramInt2++)] = (d1 * d7 + d2 * d8 + d3);
/* 3262 */         paramArrayOfDouble[(paramInt2++)] = (d4 * d7 + d5 * d8 + d6);
/*      */       }
/* 3264 */       return;
/*      */     case 6:
/* 3266 */       d1 = this.m00; d2 = this.m01;
/* 3267 */       d4 = this.m10; d5 = this.m11;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3269 */         d7 = paramArrayOfFloat[(paramInt1++)];
/* 3270 */         d8 = paramArrayOfFloat[(paramInt1++)];
/* 3271 */         paramArrayOfDouble[(paramInt2++)] = (d1 * d7 + d2 * d8);
/* 3272 */         paramArrayOfDouble[(paramInt2++)] = (d4 * d7 + d5 * d8);
/*      */       }
/* 3274 */       return;
/*      */     case 5:
/* 3276 */       d2 = this.m01; d3 = this.m02;
/* 3277 */       d4 = this.m10; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3279 */         d7 = paramArrayOfFloat[(paramInt1++)];
/* 3280 */         paramArrayOfDouble[(paramInt2++)] = (d2 * paramArrayOfFloat[(paramInt1++)] + d3);
/* 3281 */         paramArrayOfDouble[(paramInt2++)] = (d4 * d7 + d6);
/*      */       }
/* 3283 */       return;
/*      */     case 4:
/* 3285 */       d2 = this.m01; d4 = this.m10;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3287 */         d7 = paramArrayOfFloat[(paramInt1++)];
/* 3288 */         paramArrayOfDouble[(paramInt2++)] = (d2 * paramArrayOfFloat[(paramInt1++)]);
/* 3289 */         paramArrayOfDouble[(paramInt2++)] = (d4 * d7);
/*      */       }
/* 3291 */       return;
/*      */     case 3:
/* 3293 */       d1 = this.m00; d3 = this.m02;
/* 3294 */       d5 = this.m11; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3296 */         paramArrayOfDouble[(paramInt2++)] = (d1 * paramArrayOfFloat[(paramInt1++)] + d3);
/* 3297 */         paramArrayOfDouble[(paramInt2++)] = (d5 * paramArrayOfFloat[(paramInt1++)] + d6);
/*      */       }
/* 3299 */       return;
/*      */     case 2:
/* 3301 */       d1 = this.m00; d5 = this.m11;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3303 */         paramArrayOfDouble[(paramInt2++)] = (d1 * paramArrayOfFloat[(paramInt1++)]);
/* 3304 */         paramArrayOfDouble[(paramInt2++)] = (d5 * paramArrayOfFloat[(paramInt1++)]);
/*      */       }
/* 3306 */       return;
/*      */     case 1:
/* 3308 */       d3 = this.m02; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3310 */         paramArrayOfDouble[(paramInt2++)] = (paramArrayOfFloat[(paramInt1++)] + d3);
/* 3311 */         paramArrayOfDouble[(paramInt2++)] = (paramArrayOfFloat[(paramInt1++)] + d6); } return;
/*      */     case 0:
/*      */     }
/*      */     while (true) {
/* 3315 */       paramInt3--; if (paramInt3 < 0) break;
/* 3316 */       paramArrayOfDouble[(paramInt2++)] = paramArrayOfFloat[(paramInt1++)];
/* 3317 */       paramArrayOfDouble[(paramInt2++)] = paramArrayOfFloat[(paramInt1++)];
/*      */     }
/*      */   }
/*      */ 
/*      */   public void transform(double[] paramArrayOfDouble, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */   {
/*      */     double d1;
/*      */     double d2;
/*      */     double d3;
/*      */     double d4;
/*      */     double d5;
/*      */     double d6;
/*      */     double d7;
/*      */     double d8;
/* 3346 */     switch (this.state) {
/*      */     default:
/* 3348 */       stateError();
/*      */     case 7:
/* 3351 */       d1 = this.m00; d2 = this.m01; d3 = this.m02;
/* 3352 */       d4 = this.m10; d5 = this.m11; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3354 */         d7 = paramArrayOfDouble[(paramInt1++)];
/* 3355 */         d8 = paramArrayOfDouble[(paramInt1++)];
/* 3356 */         paramArrayOfFloat[(paramInt2++)] = ((float)(d1 * d7 + d2 * d8 + d3));
/* 3357 */         paramArrayOfFloat[(paramInt2++)] = ((float)(d4 * d7 + d5 * d8 + d6));
/*      */       }
/* 3359 */       return;
/*      */     case 6:
/* 3361 */       d1 = this.m00; d2 = this.m01;
/* 3362 */       d4 = this.m10; d5 = this.m11;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3364 */         d7 = paramArrayOfDouble[(paramInt1++)];
/* 3365 */         d8 = paramArrayOfDouble[(paramInt1++)];
/* 3366 */         paramArrayOfFloat[(paramInt2++)] = ((float)(d1 * d7 + d2 * d8));
/* 3367 */         paramArrayOfFloat[(paramInt2++)] = ((float)(d4 * d7 + d5 * d8));
/*      */       }
/* 3369 */       return;
/*      */     case 5:
/* 3371 */       d2 = this.m01; d3 = this.m02;
/* 3372 */       d4 = this.m10; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3374 */         d7 = paramArrayOfDouble[(paramInt1++)];
/* 3375 */         paramArrayOfFloat[(paramInt2++)] = ((float)(d2 * paramArrayOfDouble[(paramInt1++)] + d3));
/* 3376 */         paramArrayOfFloat[(paramInt2++)] = ((float)(d4 * d7 + d6));
/*      */       }
/* 3378 */       return;
/*      */     case 4:
/* 3380 */       d2 = this.m01; d4 = this.m10;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3382 */         d7 = paramArrayOfDouble[(paramInt1++)];
/* 3383 */         paramArrayOfFloat[(paramInt2++)] = ((float)(d2 * paramArrayOfDouble[(paramInt1++)]));
/* 3384 */         paramArrayOfFloat[(paramInt2++)] = ((float)(d4 * d7));
/*      */       }
/* 3386 */       return;
/*      */     case 3:
/* 3388 */       d1 = this.m00; d3 = this.m02;
/* 3389 */       d5 = this.m11; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3391 */         paramArrayOfFloat[(paramInt2++)] = ((float)(d1 * paramArrayOfDouble[(paramInt1++)] + d3));
/* 3392 */         paramArrayOfFloat[(paramInt2++)] = ((float)(d5 * paramArrayOfDouble[(paramInt1++)] + d6));
/*      */       }
/* 3394 */       return;
/*      */     case 2:
/* 3396 */       d1 = this.m00; d5 = this.m11;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3398 */         paramArrayOfFloat[(paramInt2++)] = ((float)(d1 * paramArrayOfDouble[(paramInt1++)]));
/* 3399 */         paramArrayOfFloat[(paramInt2++)] = ((float)(d5 * paramArrayOfDouble[(paramInt1++)]));
/*      */       }
/* 3401 */       return;
/*      */     case 1:
/* 3403 */       d3 = this.m02; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3405 */         paramArrayOfFloat[(paramInt2++)] = ((float)(paramArrayOfDouble[(paramInt1++)] + d3));
/* 3406 */         paramArrayOfFloat[(paramInt2++)] = ((float)(paramArrayOfDouble[(paramInt1++)] + d6)); } return;
/*      */     case 0:
/*      */     }
/*      */     while (true) {
/* 3410 */       paramInt3--; if (paramInt3 < 0) break;
/* 3411 */       paramArrayOfFloat[(paramInt2++)] = ((float)paramArrayOfDouble[(paramInt1++)]);
/* 3412 */       paramArrayOfFloat[(paramInt2++)] = ((float)paramArrayOfDouble[(paramInt1++)]);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Point2D inverseTransform(Point2D paramPoint2D1, Point2D paramPoint2D2)
/*      */     throws NoninvertibleTransformException
/*      */   {
/* 3442 */     if (paramPoint2D2 == null) {
/* 3443 */       if ((paramPoint2D1 instanceof Point2D.Double))
/* 3444 */         paramPoint2D2 = new Point2D.Double();
/*      */       else {
/* 3446 */         paramPoint2D2 = new Point2D.Float();
/*      */       }
/*      */     }
/*      */ 
/* 3450 */     double d1 = paramPoint2D1.getX();
/* 3451 */     double d2 = paramPoint2D1.getY();
/* 3452 */     switch (this.state) {
/*      */     default:
/* 3454 */       stateError();
/*      */     case 7:
/* 3457 */       d1 -= this.m02;
/* 3458 */       d2 -= this.m12;
/*      */     case 6:
/* 3461 */       double d3 = this.m00 * this.m11 - this.m01 * this.m10;
/* 3462 */       if (Math.abs(d3) <= 4.9E-324D) {
/* 3463 */         throw new NoninvertibleTransformException("Determinant is " + d3);
/*      */       }
/*      */ 
/* 3466 */       paramPoint2D2.setLocation((d1 * this.m11 - d2 * this.m01) / d3, (d2 * this.m00 - d1 * this.m10) / d3);
/*      */ 
/* 3468 */       return paramPoint2D2;
/*      */     case 5:
/* 3470 */       d1 -= this.m02;
/* 3471 */       d2 -= this.m12;
/*      */     case 4:
/* 3474 */       if ((this.m01 == 0.0D) || (this.m10 == 0.0D)) {
/* 3475 */         throw new NoninvertibleTransformException("Determinant is 0");
/*      */       }
/* 3477 */       paramPoint2D2.setLocation(d2 / this.m10, d1 / this.m01);
/* 3478 */       return paramPoint2D2;
/*      */     case 3:
/* 3480 */       d1 -= this.m02;
/* 3481 */       d2 -= this.m12;
/*      */     case 2:
/* 3484 */       if ((this.m00 == 0.0D) || (this.m11 == 0.0D)) {
/* 3485 */         throw new NoninvertibleTransformException("Determinant is 0");
/*      */       }
/* 3487 */       paramPoint2D2.setLocation(d1 / this.m00, d2 / this.m11);
/* 3488 */       return paramPoint2D2;
/*      */     case 1:
/* 3490 */       paramPoint2D2.setLocation(d1 - this.m02, d2 - this.m12);
/* 3491 */       return paramPoint2D2;
/*      */     case 0:
/* 3493 */     }paramPoint2D2.setLocation(d1, d2);
/* 3494 */     return paramPoint2D2;
/*      */   }
/*      */ 
/*      */   public void inverseTransform(double[] paramArrayOfDouble1, int paramInt1, double[] paramArrayOfDouble2, int paramInt2, int paramInt3)
/*      */     throws NoninvertibleTransformException
/*      */   {
/* 3531 */     if ((paramArrayOfDouble2 == paramArrayOfDouble1) && (paramInt2 > paramInt1) && (paramInt2 < paramInt1 + paramInt3 * 2))
/*      */     {
/* 3542 */       System.arraycopy(paramArrayOfDouble1, paramInt1, paramArrayOfDouble2, paramInt2, paramInt3 * 2);
/*      */ 
/* 3544 */       paramInt1 = paramInt2;
/*      */     }
/*      */     double d1;
/*      */     double d2;
/*      */     double d3;
/*      */     double d4;
/*      */     double d5;
/*      */     double d6;
/*      */     double d7;
/*      */     double d8;
/*      */     double d9;
/* 3546 */     switch (this.state) {
/*      */     default:
/* 3548 */       stateError();
/*      */     case 7:
/* 3551 */       d1 = this.m00; d2 = this.m01; d3 = this.m02;
/* 3552 */       d4 = this.m10; d5 = this.m11; d6 = this.m12;
/* 3553 */       d7 = d1 * d5 - d2 * d4;
/* 3554 */       if (Math.abs(d7) <= 4.9E-324D)
/* 3555 */         throw new NoninvertibleTransformException("Determinant is " + d7);
/*      */       while (true)
/*      */       {
/* 3558 */         paramInt3--; if (paramInt3 < 0) break;
/* 3559 */         d8 = paramArrayOfDouble1[(paramInt1++)] - d3;
/* 3560 */         d9 = paramArrayOfDouble1[(paramInt1++)] - d6;
/* 3561 */         paramArrayOfDouble2[(paramInt2++)] = ((d8 * d5 - d9 * d2) / d7);
/* 3562 */         paramArrayOfDouble2[(paramInt2++)] = ((d9 * d1 - d8 * d4) / d7);
/*      */       }
/* 3564 */       return;
/*      */     case 6:
/* 3566 */       d1 = this.m00; d2 = this.m01;
/* 3567 */       d4 = this.m10; d5 = this.m11;
/* 3568 */       d7 = d1 * d5 - d2 * d4;
/* 3569 */       if (Math.abs(d7) <= 4.9E-324D)
/* 3570 */         throw new NoninvertibleTransformException("Determinant is " + d7);
/*      */       while (true)
/*      */       {
/* 3573 */         paramInt3--; if (paramInt3 < 0) break;
/* 3574 */         d8 = paramArrayOfDouble1[(paramInt1++)];
/* 3575 */         d9 = paramArrayOfDouble1[(paramInt1++)];
/* 3576 */         paramArrayOfDouble2[(paramInt2++)] = ((d8 * d5 - d9 * d2) / d7);
/* 3577 */         paramArrayOfDouble2[(paramInt2++)] = ((d9 * d1 - d8 * d4) / d7);
/*      */       }
/* 3579 */       return;
/*      */     case 5:
/* 3581 */       d2 = this.m01; d3 = this.m02;
/* 3582 */       d4 = this.m10; d6 = this.m12;
/* 3583 */       if ((d2 == 0.0D) || (d4 == 0.0D))
/* 3584 */         throw new NoninvertibleTransformException("Determinant is 0");
/*      */       while (true) {
/* 3586 */         paramInt3--; if (paramInt3 < 0) break;
/* 3587 */         d8 = paramArrayOfDouble1[(paramInt1++)] - d3;
/* 3588 */         paramArrayOfDouble2[(paramInt2++)] = ((paramArrayOfDouble1[(paramInt1++)] - d6) / d4);
/* 3589 */         paramArrayOfDouble2[(paramInt2++)] = (d8 / d2);
/*      */       }
/* 3591 */       return;
/*      */     case 4:
/* 3593 */       d2 = this.m01; d4 = this.m10;
/* 3594 */       if ((d2 == 0.0D) || (d4 == 0.0D))
/* 3595 */         throw new NoninvertibleTransformException("Determinant is 0");
/*      */       while (true) {
/* 3597 */         paramInt3--; if (paramInt3 < 0) break;
/* 3598 */         d8 = paramArrayOfDouble1[(paramInt1++)];
/* 3599 */         paramArrayOfDouble1[(paramInt1++)] /= d4;
/* 3600 */         paramArrayOfDouble2[(paramInt2++)] = (d8 / d2);
/*      */       }
/* 3602 */       return;
/*      */     case 3:
/* 3604 */       d1 = this.m00; d3 = this.m02;
/* 3605 */       d5 = this.m11; d6 = this.m12;
/* 3606 */       if ((d1 == 0.0D) || (d5 == 0.0D))
/* 3607 */         throw new NoninvertibleTransformException("Determinant is 0");
/*      */       while (true) {
/* 3609 */         paramInt3--; if (paramInt3 < 0) break;
/* 3610 */         paramArrayOfDouble2[(paramInt2++)] = ((paramArrayOfDouble1[(paramInt1++)] - d3) / d1);
/* 3611 */         paramArrayOfDouble2[(paramInt2++)] = ((paramArrayOfDouble1[(paramInt1++)] - d6) / d5);
/*      */       }
/* 3613 */       return;
/*      */     case 2:
/* 3615 */       d1 = this.m00; d5 = this.m11;
/* 3616 */       if ((d1 == 0.0D) || (d5 == 0.0D))
/* 3617 */         throw new NoninvertibleTransformException("Determinant is 0");
/*      */       while (true) {
/* 3619 */         paramInt3--; if (paramInt3 < 0) break;
/* 3620 */         paramArrayOfDouble1[(paramInt1++)] /= d1;
/* 3621 */         paramArrayOfDouble1[(paramInt1++)] /= d5;
/*      */       }
/* 3623 */       return;
/*      */     case 1:
/* 3625 */       d3 = this.m02; d6 = this.m12;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3627 */         paramArrayOfDouble1[(paramInt1++)] -= d3;
/* 3628 */         paramArrayOfDouble1[(paramInt1++)] -= d6;
/*      */       }
/* 3630 */       return;
/*      */     case 0:
/* 3632 */     }if ((paramArrayOfDouble1 != paramArrayOfDouble2) || (paramInt1 != paramInt2))
/* 3633 */       System.arraycopy(paramArrayOfDouble1, paramInt1, paramArrayOfDouble2, paramInt2, paramInt3 * 2);
/*      */   }
/*      */ 
/*      */   public Point2D deltaTransform(Point2D paramPoint2D1, Point2D paramPoint2D2)
/*      */   {
/* 3668 */     if (paramPoint2D2 == null) {
/* 3669 */       if ((paramPoint2D1 instanceof Point2D.Double))
/* 3670 */         paramPoint2D2 = new Point2D.Double();
/*      */       else {
/* 3672 */         paramPoint2D2 = new Point2D.Float();
/*      */       }
/*      */     }
/*      */ 
/* 3676 */     double d1 = paramPoint2D1.getX();
/* 3677 */     double d2 = paramPoint2D1.getY();
/* 3678 */     switch (this.state) {
/*      */     default:
/* 3680 */       stateError();
/*      */     case 6:
/*      */     case 7:
/* 3684 */       paramPoint2D2.setLocation(d1 * this.m00 + d2 * this.m01, d1 * this.m10 + d2 * this.m11);
/* 3685 */       return paramPoint2D2;
/*      */     case 4:
/*      */     case 5:
/* 3688 */       paramPoint2D2.setLocation(d2 * this.m01, d1 * this.m10);
/* 3689 */       return paramPoint2D2;
/*      */     case 2:
/*      */     case 3:
/* 3692 */       paramPoint2D2.setLocation(d1 * this.m00, d2 * this.m11);
/* 3693 */       return paramPoint2D2;
/*      */     case 0:
/*      */     case 1:
/* 3696 */     }paramPoint2D2.setLocation(d1, d2);
/* 3697 */     return paramPoint2D2;
/*      */   }
/*      */ 
/*      */   public void deltaTransform(double[] paramArrayOfDouble1, int paramInt1, double[] paramArrayOfDouble2, int paramInt2, int paramInt3)
/*      */   {
/* 3738 */     if ((paramArrayOfDouble2 == paramArrayOfDouble1) && (paramInt2 > paramInt1) && (paramInt2 < paramInt1 + paramInt3 * 2))
/*      */     {
/* 3749 */       System.arraycopy(paramArrayOfDouble1, paramInt1, paramArrayOfDouble2, paramInt2, paramInt3 * 2);
/*      */ 
/* 3751 */       paramInt1 = paramInt2;
/*      */     }
/*      */     double d1;
/*      */     double d2;
/*      */     double d3;
/*      */     double d4;
/*      */     double d5;
/* 3753 */     switch (this.state) {
/*      */     default:
/* 3755 */       stateError();
/*      */     case 6:
/*      */     case 7:
/* 3759 */       d1 = this.m00; d2 = this.m01;
/* 3760 */       d3 = this.m10; d4 = this.m11;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3762 */         d5 = paramArrayOfDouble1[(paramInt1++)];
/* 3763 */         double d6 = paramArrayOfDouble1[(paramInt1++)];
/* 3764 */         paramArrayOfDouble2[(paramInt2++)] = (d5 * d1 + d6 * d2);
/* 3765 */         paramArrayOfDouble2[(paramInt2++)] = (d5 * d3 + d6 * d4);
/*      */       }
/* 3767 */       return;
/*      */     case 4:
/*      */     case 5:
/* 3770 */       d2 = this.m01; d3 = this.m10;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3772 */         d5 = paramArrayOfDouble1[(paramInt1++)];
/* 3773 */         paramArrayOfDouble1[(paramInt1++)] *= d2;
/* 3774 */         paramArrayOfDouble2[(paramInt2++)] = (d5 * d3);
/*      */       }
/* 3776 */       return;
/*      */     case 2:
/*      */     case 3:
/* 3779 */       d1 = this.m00; d4 = this.m11;
/*      */       while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 3781 */         paramArrayOfDouble1[(paramInt1++)] *= d1;
/* 3782 */         paramArrayOfDouble1[(paramInt1++)] *= d4;
/*      */       }
/* 3784 */       return;
/*      */     case 0:
/*      */     case 1:
/* 3787 */     }if ((paramArrayOfDouble1 != paramArrayOfDouble2) || (paramInt1 != paramInt2))
/* 3788 */       System.arraycopy(paramArrayOfDouble1, paramInt1, paramArrayOfDouble2, paramInt2, paramInt3 * 2);
/*      */   }
/*      */ 
/*      */   public Shape createTransformedShape(Shape paramShape)
/*      */   {
/* 3808 */     if (paramShape == null) {
/* 3809 */       return null;
/*      */     }
/* 3811 */     return new Path2D.Double(paramShape, this);
/*      */   }
/*      */ 
/*      */   private static double _matround(double paramDouble)
/*      */   {
/* 3817 */     return Math.rint(paramDouble * 1000000000000000.0D) / 1000000000000000.0D;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 3828 */     return "AffineTransform[[" + _matround(this.m00) + ", " + _matround(this.m01) + ", " + _matround(this.m02) + "], [" + _matround(this.m10) + ", " + _matround(this.m11) + ", " + _matround(this.m12) + "]]";
/*      */   }
/*      */ 
/*      */   public boolean isIdentity()
/*      */   {
/* 3845 */     return (this.state == 0) || (getType() == 0);
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/* 3856 */       return super.clone();
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/* 3859 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 3869 */     long l = Double.doubleToLongBits(this.m00);
/* 3870 */     l = l * 31L + Double.doubleToLongBits(this.m01);
/* 3871 */     l = l * 31L + Double.doubleToLongBits(this.m02);
/* 3872 */     l = l * 31L + Double.doubleToLongBits(this.m10);
/* 3873 */     l = l * 31L + Double.doubleToLongBits(this.m11);
/* 3874 */     l = l * 31L + Double.doubleToLongBits(this.m12);
/* 3875 */     return (int)l ^ (int)(l >> 32);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 3889 */     if (!(paramObject instanceof AffineTransform)) {
/* 3890 */       return false;
/*      */     }
/*      */ 
/* 3893 */     AffineTransform localAffineTransform = (AffineTransform)paramObject;
/*      */ 
/* 3895 */     return (this.m00 == localAffineTransform.m00) && (this.m01 == localAffineTransform.m01) && (this.m02 == localAffineTransform.m02) && (this.m10 == localAffineTransform.m10) && (this.m11 == localAffineTransform.m11) && (this.m12 == localAffineTransform.m12);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 3914 */     paramObjectOutputStream.defaultWriteObject();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 3920 */     paramObjectInputStream.defaultReadObject();
/* 3921 */     updateState();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.AffineTransform
 * JD-Core Version:    0.6.2
 */