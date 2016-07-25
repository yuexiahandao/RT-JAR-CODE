/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.Polygon;
/*     */ import java.io.Serializable;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import javax.swing.text.AttributeSet;
/*     */ 
/*     */ class Map
/*     */   implements Serializable
/*     */ {
/*     */   private String name;
/*     */   private Vector<AttributeSet> areaAttributes;
/*     */   private Vector<RegionContainment> areas;
/*     */ 
/*     */   public Map()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Map(String paramString)
/*     */   {
/*  53 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  60 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void addArea(AttributeSet paramAttributeSet)
/*     */   {
/*  67 */     if (paramAttributeSet == null) {
/*  68 */       return;
/*     */     }
/*  70 */     if (this.areaAttributes == null) {
/*  71 */       this.areaAttributes = new Vector(2);
/*     */     }
/*  73 */     this.areaAttributes.addElement(paramAttributeSet.copyAttributes());
/*     */   }
/*     */ 
/*     */   public void removeArea(AttributeSet paramAttributeSet)
/*     */   {
/*  80 */     if ((paramAttributeSet != null) && (this.areaAttributes != null)) {
/*  81 */       int i = this.areas != null ? this.areas.size() : 0;
/*  82 */       for (int j = this.areaAttributes.size() - 1; j >= 0; 
/*  83 */         j--)
/*  84 */         if (((AttributeSet)this.areaAttributes.elementAt(j)).isEqual(paramAttributeSet)) {
/*  85 */           this.areaAttributes.removeElementAt(j);
/*  86 */           if (j < i)
/*  87 */             this.areas.removeElementAt(j);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public AttributeSet[] getAreas()
/*     */   {
/*  98 */     int i = this.areaAttributes != null ? this.areaAttributes.size() : 0;
/*     */ 
/* 100 */     if (i != 0) {
/* 101 */       AttributeSet[] arrayOfAttributeSet = new AttributeSet[i];
/*     */ 
/* 103 */       this.areaAttributes.copyInto(arrayOfAttributeSet);
/* 104 */       return arrayOfAttributeSet;
/*     */     }
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   public AttributeSet getArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 116 */     int i = this.areaAttributes != null ? this.areaAttributes.size() : 0;
/*     */ 
/* 119 */     if (i > 0) {
/* 120 */       int j = this.areas != null ? this.areas.size() : 0;
/*     */ 
/* 122 */       if (this.areas == null) {
/* 123 */         this.areas = new Vector(i);
/*     */       }
/* 125 */       for (int k = 0; k < i; k++) {
/* 126 */         if (k >= j) {
/* 127 */           this.areas.addElement(createRegionContainment((AttributeSet)this.areaAttributes.elementAt(k)));
/*     */         }
/*     */ 
/* 130 */         RegionContainment localRegionContainment = (RegionContainment)this.areas.elementAt(k);
/* 131 */         if ((localRegionContainment != null) && (localRegionContainment.contains(paramInt1, paramInt2, paramInt3, paramInt4))) {
/* 132 */           return (AttributeSet)this.areaAttributes.elementAt(k);
/*     */         }
/*     */       }
/*     */     }
/* 136 */     return null;
/*     */   }
/*     */ 
/*     */   protected RegionContainment createRegionContainment(AttributeSet paramAttributeSet)
/*     */   {
/* 145 */     Object localObject1 = paramAttributeSet.getAttribute(HTML.Attribute.SHAPE);
/*     */ 
/* 147 */     if (localObject1 == null) {
/* 148 */       localObject1 = "rect";
/*     */     }
/* 150 */     if ((localObject1 instanceof String)) {
/* 151 */       String str = ((String)localObject1).toLowerCase();
/* 152 */       Object localObject2 = null;
/*     */       try
/*     */       {
/* 155 */         if (str.equals("rect")) {
/* 156 */           localObject2 = new RectangleRegionContainment(paramAttributeSet);
/*     */         }
/* 158 */         else if (str.equals("circle")) {
/* 159 */           localObject2 = new CircleRegionContainment(paramAttributeSet);
/*     */         }
/* 161 */         else if (str.equals("poly")) {
/* 162 */           localObject2 = new PolygonRegionContainment(paramAttributeSet);
/*     */         }
/* 164 */         else if (str.equals("default"))
/* 165 */           localObject2 = DefaultRegionContainment.sharedInstance();
/*     */       }
/*     */       catch (RuntimeException localRuntimeException)
/*     */       {
/* 169 */         localObject2 = null;
/*     */       }
/* 171 */       return localObject2;
/*     */     }
/* 173 */     return null;
/*     */   }
/*     */ 
/*     */   protected static int[] extractCoords(Object paramObject)
/*     */   {
/* 183 */     if ((paramObject == null) || (!(paramObject instanceof String))) {
/* 184 */       return null;
/*     */     }
/*     */ 
/* 187 */     StringTokenizer localStringTokenizer = new StringTokenizer((String)paramObject, ", \t\n\r");
/*     */ 
/* 189 */     Object localObject1 = null;
/* 190 */     int i = 0;
/*     */     Object localObject2;
/* 192 */     while (localStringTokenizer.hasMoreElements()) {
/* 193 */       localObject2 = localStringTokenizer.nextToken();
/*     */       int j;
/* 196 */       if (((String)localObject2).endsWith("%")) {
/* 197 */         j = -1;
/* 198 */         localObject2 = ((String)localObject2).substring(0, ((String)localObject2).length() - 1);
/*     */       }
/*     */       else {
/* 201 */         j = 1;
/*     */       }
/*     */       try {
/* 204 */         int k = Integer.parseInt((String)localObject2);
/*     */ 
/* 206 */         if (localObject1 == null) {
/* 207 */           localObject1 = new int[4];
/*     */         }
/* 209 */         else if (i == localObject1.length) {
/* 210 */           int[] arrayOfInt = new int[localObject1.length * 2];
/*     */ 
/* 212 */           System.arraycopy(localObject1, 0, arrayOfInt, 0, localObject1.length);
/* 213 */           localObject1 = arrayOfInt;
/*     */         }
/* 215 */         localObject1[(i++)] = (k * j);
/*     */       } catch (NumberFormatException localNumberFormatException) {
/* 217 */         return null;
/*     */       }
/*     */     }
/* 220 */     if ((i > 0) && (i != localObject1.length)) {
/* 221 */       localObject2 = new int[i];
/*     */ 
/* 223 */       System.arraycopy(localObject1, 0, localObject2, 0, i);
/* 224 */       localObject1 = localObject2;
/*     */     }
/* 226 */     return localObject1;
/*     */   }
/*     */ 
/*     */   static class CircleRegionContainment
/*     */     implements Map.RegionContainment
/*     */   {
/*     */     int x;
/*     */     int y;
/*     */     int radiusSquared;
/*     */     float[] percentValues;
/*     */     int lastWidth;
/*     */     int lastHeight;
/*     */ 
/*     */     public CircleRegionContainment(AttributeSet paramAttributeSet)
/*     */     {
/* 431 */       int[] arrayOfInt = Map.extractCoords(paramAttributeSet.getAttribute(HTML.Attribute.COORDS));
/*     */ 
/* 434 */       if ((arrayOfInt == null) || (arrayOfInt.length != 3)) {
/* 435 */         throw new RuntimeException("Unable to parse circular area");
/*     */       }
/* 437 */       this.x = arrayOfInt[0];
/* 438 */       this.y = arrayOfInt[1];
/* 439 */       this.radiusSquared = (arrayOfInt[2] * arrayOfInt[2]);
/* 440 */       if ((arrayOfInt[0] < 0) || (arrayOfInt[1] < 0) || (arrayOfInt[2] < 0)) {
/* 441 */         this.lastWidth = (this.lastHeight = -1);
/* 442 */         this.percentValues = new float[3];
/* 443 */         for (int i = 0; i < 3; i++)
/* 444 */           if (arrayOfInt[i] < 0) {
/* 445 */             this.percentValues[i] = (arrayOfInt[i] / -100.0F);
/*     */           }
/*     */           else
/*     */           {
/* 449 */             this.percentValues[i] = -1.0F;
/*     */           }
/*     */       }
/*     */       else
/*     */       {
/* 454 */         this.percentValues = null;
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean contains(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 459 */       if ((this.percentValues != null) && ((this.lastWidth != paramInt3) || (this.lastHeight != paramInt4)))
/*     */       {
/* 461 */         int i = Math.min(paramInt3, paramInt4) / 2;
/*     */ 
/* 463 */         this.lastWidth = paramInt3;
/* 464 */         this.lastHeight = paramInt4;
/* 465 */         if (this.percentValues[0] != -1.0F) {
/* 466 */           this.x = ((int)(this.percentValues[0] * paramInt3));
/*     */         }
/* 468 */         if (this.percentValues[1] != -1.0F) {
/* 469 */           this.y = ((int)(this.percentValues[1] * paramInt4));
/*     */         }
/* 471 */         if (this.percentValues[2] != -1.0F) {
/* 472 */           this.radiusSquared = ((int)(this.percentValues[2] * Math.min(paramInt3, paramInt4)));
/*     */ 
/* 474 */           this.radiusSquared *= this.radiusSquared;
/*     */         }
/*     */       }
/* 477 */       return (paramInt1 - this.x) * (paramInt1 - this.x) + (paramInt2 - this.y) * (paramInt2 - this.y) <= this.radiusSquared;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class DefaultRegionContainment
/*     */     implements Map.RegionContainment
/*     */   {
/* 490 */     static DefaultRegionContainment si = null;
/*     */ 
/*     */     public static DefaultRegionContainment sharedInstance() {
/* 493 */       if (si == null) {
/* 494 */         si = new DefaultRegionContainment();
/*     */       }
/* 496 */       return si;
/*     */     }
/*     */ 
/*     */     public boolean contains(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 500 */       return (paramInt1 <= paramInt3) && (paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt2 <= paramInt3);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class PolygonRegionContainment extends Polygon
/*     */     implements Map.RegionContainment
/*     */   {
/*     */     float[] percentValues;
/*     */     int[] percentIndexs;
/*     */     int lastWidth;
/*     */     int lastHeight;
/*     */ 
/*     */     public PolygonRegionContainment(AttributeSet paramAttributeSet)
/*     */     {
/* 338 */       int[] arrayOfInt = Map.extractCoords(paramAttributeSet.getAttribute(HTML.Attribute.COORDS));
/*     */ 
/* 341 */       if ((arrayOfInt == null) || (arrayOfInt.length == 0) || (arrayOfInt.length % 2 != 0))
/*     */       {
/* 343 */         throw new RuntimeException("Unable to parse polygon area");
/*     */       }
/*     */ 
/* 346 */       int i = 0;
/*     */ 
/* 348 */       this.lastWidth = (this.lastHeight = -1);
/* 349 */       for (int j = arrayOfInt.length - 1; j >= 0; 
/* 350 */         j--) {
/* 351 */         if (arrayOfInt[j] < 0) {
/* 352 */           i++;
/*     */         }
/*     */       }
/*     */ 
/* 356 */       if (i > 0) {
/* 357 */         this.percentIndexs = new int[i];
/* 358 */         this.percentValues = new float[i];
/* 359 */         j = arrayOfInt.length - 1; int k = 0;
/* 360 */         for (; j >= 0; j--)
/* 361 */           if (arrayOfInt[j] < 0) {
/* 362 */             this.percentValues[k] = (arrayOfInt[j] / -100.0F);
/*     */ 
/* 364 */             this.percentIndexs[k] = j;
/* 365 */             k++;
/*     */           }
/*     */       }
/*     */       else
/*     */       {
/* 370 */         this.percentIndexs = null;
/* 371 */         this.percentValues = null;
/*     */       }
/* 373 */       this.npoints = (arrayOfInt.length / 2);
/* 374 */       this.xpoints = new int[this.npoints];
/* 375 */       this.ypoints = new int[this.npoints];
/*     */ 
/* 377 */       for (j = 0; j < this.npoints; j++) {
/* 378 */         this.xpoints[j] = arrayOfInt[(j + j)];
/* 379 */         this.ypoints[j] = arrayOfInt[(j + j + 1)];
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean contains(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 385 */       if ((this.percentValues == null) || ((this.lastWidth == paramInt3) && (this.lastHeight == paramInt4)))
/*     */       {
/* 387 */         return contains(paramInt1, paramInt2);
/*     */       }
/*     */ 
/* 390 */       this.bounds = null;
/* 391 */       this.lastWidth = paramInt3;
/* 392 */       this.lastHeight = paramInt4;
/* 393 */       float f1 = paramInt3;
/* 394 */       float f2 = paramInt4;
/* 395 */       for (int i = this.percentValues.length - 1; i >= 0; 
/* 396 */         i--) {
/* 397 */         if (this.percentIndexs[i] % 2 == 0)
/*     */         {
/* 399 */           this.xpoints[(this.percentIndexs[i] / 2)] = ((int)(this.percentValues[i] * f1));
/*     */         }
/*     */         else
/*     */         {
/* 404 */           this.ypoints[(this.percentIndexs[i] / 2)] = ((int)(this.percentValues[i] * f2));
/*     */         }
/*     */       }
/*     */ 
/* 408 */       return contains(paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class RectangleRegionContainment
/*     */     implements Map.RegionContainment
/*     */   {
/*     */     float[] percents;
/*     */     int lastWidth;
/*     */     int lastHeight;
/*     */     int x0;
/*     */     int y0;
/*     */     int x1;
/*     */     int y1;
/*     */ 
/*     */     public RectangleRegionContainment(AttributeSet paramAttributeSet)
/*     */     {
/* 265 */       int[] arrayOfInt = Map.extractCoords(paramAttributeSet.getAttribute(HTML.Attribute.COORDS));
/*     */ 
/* 268 */       this.percents = null;
/* 269 */       if ((arrayOfInt == null) || (arrayOfInt.length != 4)) {
/* 270 */         throw new RuntimeException("Unable to parse rectangular area");
/*     */       }
/*     */ 
/* 273 */       this.x0 = arrayOfInt[0];
/* 274 */       this.y0 = arrayOfInt[1];
/* 275 */       this.x1 = arrayOfInt[2];
/* 276 */       this.y1 = arrayOfInt[3];
/* 277 */       if ((this.x0 < 0) || (this.y0 < 0) || (this.x1 < 0) || (this.y1 < 0)) {
/* 278 */         this.percents = new float[4];
/* 279 */         this.lastWidth = (this.lastHeight = -1);
/* 280 */         for (int i = 0; i < 4; i++)
/* 281 */           if (arrayOfInt[i] < 0) {
/* 282 */             this.percents[i] = (Math.abs(arrayOfInt[i]) / 100.0F);
/*     */           }
/*     */           else
/*     */           {
/* 286 */             this.percents[i] = -1.0F;
/*     */           }
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean contains(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 294 */       if (this.percents == null) {
/* 295 */         return contains(paramInt1, paramInt2);
/*     */       }
/* 297 */       if ((this.lastWidth != paramInt3) || (this.lastHeight != paramInt4)) {
/* 298 */         this.lastWidth = paramInt3;
/* 299 */         this.lastHeight = paramInt4;
/* 300 */         if (this.percents[0] != -1.0F) {
/* 301 */           this.x0 = ((int)(this.percents[0] * paramInt3));
/*     */         }
/* 303 */         if (this.percents[1] != -1.0F) {
/* 304 */           this.y0 = ((int)(this.percents[1] * paramInt4));
/*     */         }
/* 306 */         if (this.percents[2] != -1.0F) {
/* 307 */           this.x1 = ((int)(this.percents[2] * paramInt3));
/*     */         }
/* 309 */         if (this.percents[3] != -1.0F) {
/* 310 */           this.y1 = ((int)(this.percents[3] * paramInt4));
/*     */         }
/*     */       }
/* 313 */       return contains(paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public boolean contains(int paramInt1, int paramInt2) {
/* 317 */       return (paramInt1 >= this.x0) && (paramInt1 <= this.x1) && (paramInt2 >= this.y0) && (paramInt2 <= this.y1);
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract interface RegionContainment
/*     */   {
/*     */     public abstract boolean contains(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.Map
 * JD-Core Version:    0.6.2
 */