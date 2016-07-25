/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class XbmImageDecoder extends ImageDecoder
/*     */ {
/*  47 */   private static byte[] XbmColormap = { -1, -1, -1, 0, 0, 0 };
/*     */ 
/*  49 */   private static int XbmHints = 30;
/*     */ 
/*     */   public XbmImageDecoder(InputStreamImageSource paramInputStreamImageSource, InputStream paramInputStream)
/*     */   {
/*  55 */     super(paramInputStreamImageSource, paramInputStream);
/*  56 */     if (!(this.input instanceof BufferedInputStream))
/*     */     {
/*  59 */       this.input = new BufferedInputStream(this.input, 80);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void error(String paramString)
/*     */     throws ImageFormatException
/*     */   {
/*  68 */     throw new ImageFormatException(paramString);
/*     */   }
/*     */ 
/*     */   public void produceImage()
/*     */     throws IOException, ImageFormatException
/*     */   {
/*  75 */     char[] arrayOfChar = new char[80];
/*     */ 
/*  77 */     int j = 0;
/*  78 */     int k = 0;
/*  79 */     int m = 0;
/*  80 */     int n = 0;
/*  81 */     int i1 = 0;
/*  82 */     int i2 = 0;
/*  83 */     int i3 = 1;
/*  84 */     byte[] arrayOfByte = null;
/*  85 */     Object localObject = null;
/*     */     int i;
/*  86 */     while ((!this.aborted) && ((i = this.input.read()) != -1))
/*  87 */       if (((97 <= i) && (i <= 122)) || ((65 <= i) && (i <= 90)) || ((48 <= i) && (i <= 57)) || (i == 35) || (i == 95))
/*     */       {
/*  90 */         if (j < 78)
/*  91 */           arrayOfChar[(j++)] = ((char)i);
/*  92 */       } else if (j > 0) {
/*  93 */         int i4 = j;
/*  94 */         j = 0;
/*  95 */         if (i3 != 0) {
/*  96 */           if ((i4 != 7) || (arrayOfChar[0] != '#') || (arrayOfChar[1] != 'd') || (arrayOfChar[2] != 'e') || (arrayOfChar[3] != 'f') || (arrayOfChar[4] != 'i') || (arrayOfChar[5] != 'n') || (arrayOfChar[6] != 'e'))
/*     */           {
/* 105 */             error("Not an XBM file");
/*     */           }
/* 107 */           i3 = 0;
/*     */         }
/* 109 */         if (arrayOfChar[(i4 - 1)] == 'h') {
/* 110 */           k = 1;
/* 111 */         } else if ((arrayOfChar[(i4 - 1)] == 't') && (i4 > 1) && (arrayOfChar[(i4 - 2)] == 'h')) {
/* 112 */           k = 2;
/*     */         }
/*     */         else
/*     */         {
/*     */           int i5;
/*     */           int i6;
/* 113 */           if ((i4 > 2) && (k < 0) && (arrayOfChar[0] == '0') && (arrayOfChar[1] == 'x')) {
/* 114 */             i5 = 0;
/* 115 */             for (i6 = 2; i6 < i4; i6++) {
/* 116 */               i = arrayOfChar[i6];
/* 117 */               if ((48 <= i) && (i <= 57))
/* 118 */                 i -= 48;
/* 119 */               else if ((65 <= i) && (i <= 90))
/* 120 */                 i = i - 65 + 10;
/* 121 */               else if ((97 <= i) && (i <= 122))
/* 122 */                 i = i - 97 + 10;
/*     */               else
/* 124 */                 i = 0;
/* 125 */               i5 = i5 * 16 + i;
/*     */             }
/* 127 */             for (i6 = 1; i6 <= 128; i6 <<= 1) {
/* 128 */               if (i1 < n) {
/* 129 */                 if ((i5 & i6) != 0)
/* 130 */                   arrayOfByte[i1] = 1;
/*     */                 else
/* 132 */                   arrayOfByte[i1] = 0;
/*     */               }
/* 134 */               i1++;
/*     */             }
/* 136 */             if (i1 >= n) {
/* 137 */               if (setPixels(0, i2, n, 1, (ColorModel)localObject, arrayOfByte, 0, n) <= 0) {
/* 138 */                 return;
/*     */               }
/* 140 */               i1 = 0;
/* 141 */               if (i2++ >= m)
/*     */                 break;
/*     */             }
/*     */           }
/*     */           else {
/* 146 */             i5 = 0;
/* 147 */             for (i6 = 0; i6 < i4; i6++)
/* 148 */               if (('0' <= (i = arrayOfChar[i6])) && (i <= 57)) {
/* 149 */                 i5 = i5 * 10 + i - 48;
/*     */               } else {
/* 151 */                 i5 = -1;
/* 152 */                 break;
/*     */               }
/* 154 */             if ((i5 > 0) && (k > 0)) {
/* 155 */               if (k == 1)
/* 156 */                 n = i5;
/*     */               else
/* 158 */                 m = i5;
/* 159 */               if ((n == 0) || (m == 0)) {
/* 160 */                 k = 0;
/*     */               } else {
/* 162 */                 localObject = new IndexColorModel(8, 2, XbmColormap, 0, false, 0);
/*     */ 
/* 164 */                 setDimensions(n, m);
/* 165 */                 setColorModel((ColorModel)localObject);
/* 166 */                 setHints(XbmHints);
/* 167 */                 headerComplete();
/* 168 */                 arrayOfByte = new byte[n];
/* 169 */                 k = -1;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 175 */     this.input.close();
/* 176 */     imageComplete(3, true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.XbmImageDecoder
 * JD-Core Version:    0.6.2
 */