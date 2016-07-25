/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Path2D;
/*     */ import java.awt.geom.Path2D.Float;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import sun.awt.SunHints;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ 
/*     */ public final class GraphicsPrimitiveMgr
/*     */ {
/*     */   private static final boolean debugTrace = false;
/*     */   private static GraphicsPrimitive[] primitives;
/*     */   private static GraphicsPrimitive[] generalPrimitives;
/*  46 */   private static boolean needssort = true;
/*     */ 
/*  76 */   private static Comparator primSorter = new Comparator() {
/*     */     public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/*  78 */       int i = ((GraphicsPrimitive)paramAnonymousObject1).getUniqueID();
/*  79 */       int j = ((GraphicsPrimitive)paramAnonymousObject2).getUniqueID();
/*     */ 
/*  81 */       return i < j ? -1 : i == j ? 0 : 1;
/*     */     }
/*  76 */   };
/*     */ 
/*  85 */   private static Comparator primFinder = new Comparator() {
/*     */     public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/*  87 */       int i = ((GraphicsPrimitive)paramAnonymousObject1).getUniqueID();
/*  88 */       int j = ((GraphicsPrimitiveMgr.PrimitiveSpec)paramAnonymousObject2).uniqueID;
/*     */ 
/*  90 */       return i < j ? -1 : i == j ? 0 : 1;
/*     */     }
/*  85 */   };
/*     */ 
/*     */   private static native void initIDs(Class paramClass1, Class paramClass2, Class paramClass3, Class paramClass4, Class paramClass5, Class paramClass6, Class paramClass7, Class paramClass8, Class paramClass9, Class paramClass10, Class paramClass11);
/*     */ 
/*     */   private static native void registerNativeLoops();
/*     */ 
/*     */   public static synchronized void register(GraphicsPrimitive[] paramArrayOfGraphicsPrimitive)
/*     */   {
/* 102 */     GraphicsPrimitive[] arrayOfGraphicsPrimitive1 = primitives;
/* 103 */     int i = 0;
/* 104 */     int j = paramArrayOfGraphicsPrimitive.length;
/*     */ 
/* 111 */     if (arrayOfGraphicsPrimitive1 != null) {
/* 112 */       i = arrayOfGraphicsPrimitive1.length;
/*     */     }
/* 114 */     GraphicsPrimitive[] arrayOfGraphicsPrimitive2 = new GraphicsPrimitive[i + j];
/* 115 */     if (arrayOfGraphicsPrimitive1 != null) {
/* 116 */       System.arraycopy(arrayOfGraphicsPrimitive1, 0, arrayOfGraphicsPrimitive2, 0, i);
/*     */     }
/* 118 */     System.arraycopy(paramArrayOfGraphicsPrimitive, 0, arrayOfGraphicsPrimitive2, i, j);
/* 119 */     needssort = true;
/* 120 */     primitives = arrayOfGraphicsPrimitive2;
/*     */   }
/*     */ 
/*     */   public static synchronized void registerGeneral(GraphicsPrimitive paramGraphicsPrimitive) {
/* 124 */     if (generalPrimitives == null) {
/* 125 */       generalPrimitives = new GraphicsPrimitive[] { paramGraphicsPrimitive };
/* 126 */       return;
/*     */     }
/* 128 */     int i = generalPrimitives.length;
/* 129 */     GraphicsPrimitive[] arrayOfGraphicsPrimitive = new GraphicsPrimitive[i + 1];
/* 130 */     System.arraycopy(generalPrimitives, 0, arrayOfGraphicsPrimitive, 0, i);
/* 131 */     arrayOfGraphicsPrimitive[i] = paramGraphicsPrimitive;
/* 132 */     generalPrimitives = arrayOfGraphicsPrimitive;
/*     */   }
/*     */ 
/*     */   public static synchronized GraphicsPrimitive locate(int paramInt, SurfaceType paramSurfaceType)
/*     */   {
/* 138 */     return locate(paramInt, SurfaceType.OpaqueColor, CompositeType.Src, paramSurfaceType);
/*     */   }
/*     */ 
/*     */   public static synchronized GraphicsPrimitive locate(int paramInt, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 156 */     GraphicsPrimitive localGraphicsPrimitive = locatePrim(paramInt, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */ 
/* 159 */     if (localGraphicsPrimitive == null)
/*     */     {
/* 161 */       localGraphicsPrimitive = locateGeneral(paramInt);
/* 162 */       if (localGraphicsPrimitive != null) {
/* 163 */         localGraphicsPrimitive = localGraphicsPrimitive.makePrimitive(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/* 164 */         if ((localGraphicsPrimitive != null) && (GraphicsPrimitive.traceflags != 0)) {
/* 165 */           localGraphicsPrimitive = localGraphicsPrimitive.traceWrap();
/*     */         }
/*     */       }
/*     */     }
/* 169 */     return localGraphicsPrimitive;
/*     */   }
/*     */ 
/*     */   public static synchronized GraphicsPrimitive locatePrim(int paramInt, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 188 */     PrimitiveSpec localPrimitiveSpec = new PrimitiveSpec(null);
/*     */ 
/* 190 */     for (SurfaceType localSurfaceType2 = paramSurfaceType2; localSurfaceType2 != null; localSurfaceType2 = localSurfaceType2.getSuperType()) {
/* 191 */       for (SurfaceType localSurfaceType1 = paramSurfaceType1; localSurfaceType1 != null; localSurfaceType1 = localSurfaceType1.getSuperType()) {
/* 192 */         for (CompositeType localCompositeType = paramCompositeType; localCompositeType != null; localCompositeType = localCompositeType.getSuperType())
/*     */         {
/* 201 */           localPrimitiveSpec.uniqueID = GraphicsPrimitive.makeUniqueID(paramInt, localSurfaceType1, localCompositeType, localSurfaceType2);
/*     */ 
/* 203 */           GraphicsPrimitive localGraphicsPrimitive = locate(localPrimitiveSpec);
/* 204 */           if (localGraphicsPrimitive != null)
/*     */           {
/* 206 */             return localGraphicsPrimitive;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 211 */     return null;
/*     */   }
/*     */ 
/*     */   private static GraphicsPrimitive locateGeneral(int paramInt) {
/* 215 */     if (generalPrimitives == null) {
/* 216 */       return null;
/*     */     }
/* 218 */     for (int i = 0; i < generalPrimitives.length; i++) {
/* 219 */       GraphicsPrimitive localGraphicsPrimitive = generalPrimitives[i];
/* 220 */       if (localGraphicsPrimitive.getPrimTypeID() == paramInt) {
/* 221 */         return localGraphicsPrimitive;
/*     */       }
/*     */     }
/* 224 */     return null;
/*     */   }
/*     */ 
/*     */   private static GraphicsPrimitive locate(PrimitiveSpec paramPrimitiveSpec)
/*     */   {
/* 229 */     if (needssort) {
/* 230 */       if (GraphicsPrimitive.traceflags != 0) {
/* 231 */         for (int i = 0; i < primitives.length; i++) {
/* 232 */           primitives[i] = primitives[i].traceWrap();
/*     */         }
/*     */       }
/* 235 */       Arrays.sort(primitives, primSorter);
/* 236 */       needssort = false;
/*     */     }
/* 238 */     GraphicsPrimitive[] arrayOfGraphicsPrimitive = primitives;
/* 239 */     if (arrayOfGraphicsPrimitive == null) {
/* 240 */       return null;
/*     */     }
/* 242 */     int j = Arrays.binarySearch(arrayOfGraphicsPrimitive, paramPrimitiveSpec, primFinder);
/* 243 */     if (j >= 0) {
/* 244 */       GraphicsPrimitive localGraphicsPrimitive = arrayOfGraphicsPrimitive[j];
/* 245 */       if ((localGraphicsPrimitive instanceof GraphicsPrimitiveProxy)) {
/* 246 */         localGraphicsPrimitive = ((GraphicsPrimitiveProxy)localGraphicsPrimitive).instantiate();
/* 247 */         arrayOfGraphicsPrimitive[j] = localGraphicsPrimitive;
/*     */       }
/*     */ 
/* 255 */       return localGraphicsPrimitive;
/*     */     }
/*     */ 
/* 261 */     return null;
/*     */   }
/*     */ 
/*     */   private static void writeLog(String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   public static void testPrimitiveInstantiation()
/*     */   {
/* 280 */     testPrimitiveInstantiation(false);
/*     */   }
/*     */ 
/*     */   public static void testPrimitiveInstantiation(boolean paramBoolean) {
/* 284 */     int i = 0;
/* 285 */     int j = 0;
/* 286 */     GraphicsPrimitive[] arrayOfGraphicsPrimitive = primitives;
/* 287 */     for (int k = 0; k < arrayOfGraphicsPrimitive.length; k++) {
/* 288 */       Object localObject = arrayOfGraphicsPrimitive[k];
/* 289 */       if ((localObject instanceof GraphicsPrimitiveProxy)) {
/* 290 */         GraphicsPrimitive localGraphicsPrimitive = ((GraphicsPrimitiveProxy)localObject).instantiate();
/* 291 */         if ((!localGraphicsPrimitive.getSignature().equals(((GraphicsPrimitive)localObject).getSignature())) || (localGraphicsPrimitive.getUniqueID() != ((GraphicsPrimitive)localObject).getUniqueID()))
/*     */         {
/* 293 */           System.out.println("r.getSignature == " + localGraphicsPrimitive.getSignature());
/* 294 */           System.out.println("r.getUniqueID == " + localGraphicsPrimitive.getUniqueID());
/* 295 */           System.out.println("p.getSignature == " + ((GraphicsPrimitive)localObject).getSignature());
/* 296 */           System.out.println("p.getUniqueID == " + ((GraphicsPrimitive)localObject).getUniqueID());
/* 297 */           throw new RuntimeException("Primitive " + localObject + " returns wrong signature for " + localGraphicsPrimitive.getClass());
/*     */         }
/*     */ 
/* 302 */         j++;
/* 303 */         localObject = localGraphicsPrimitive;
/* 304 */         if (paramBoolean)
/* 305 */           System.out.println(localObject);
/*     */       }
/*     */       else {
/* 308 */         if (paramBoolean) {
/* 309 */           System.out.println(localObject + " (not proxied).");
/*     */         }
/* 311 */         i++;
/*     */       }
/*     */     }
/* 314 */     System.out.println(i + " graphics primitives were not proxied.");
/*     */ 
/* 316 */     System.out.println(j + " proxied graphics primitives resolved correctly.");
/*     */ 
/* 318 */     System.out.println(i + j + " total graphics primitives");
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 324 */     if (needssort) {
/* 325 */       Arrays.sort(primitives, primSorter);
/* 326 */       needssort = false;
/*     */     }
/* 328 */     testPrimitiveInstantiation(paramArrayOfString.length > 0);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  56 */     initIDs(GraphicsPrimitive.class, SurfaceType.class, CompositeType.class, SunGraphics2D.class, Color.class, AffineTransform.class, XORComposite.class, AlphaComposite.class, Path2D.class, Path2D.Float.class, SunHints.class);
/*     */ 
/*  67 */     CustomComponent.register();
/*  68 */     GeneralRenderer.register();
/*  69 */     registerNativeLoops();
/*     */   }
/*     */ 
/*     */   private static class PrimitiveSpec
/*     */   {
/*     */     public int uniqueID;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.GraphicsPrimitiveMgr
 * JD-Core Version:    0.6.2
 */