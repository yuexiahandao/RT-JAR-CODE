/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.awt.image.BufImgSurfaceData;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public abstract class GraphicsPrimitive
/*     */ {
/*     */   private String methodSignature;
/*     */   private int uniqueID;
/* 111 */   private static int unusedPrimID = 1;
/*     */   private SurfaceType sourceType;
/*     */   private CompositeType compositeType;
/*     */   private SurfaceType destType;
/*     */   private long pNativePrim;
/*     */   static HashMap traceMap;
/*     */   public static int traceflags;
/*     */   public static String tracefile;
/*     */   public static PrintStream traceout;
/*     */   public static final int TRACELOG = 1;
/*     */   public static final int TRACETIMESTAMP = 2;
/*     */   public static final int TRACECOUNTS = 4;
/*     */   private String cachedname;
/*     */ 
/*     */   public static final synchronized int makePrimTypeID()
/*     */   {
/* 120 */     if (unusedPrimID > 255) {
/* 121 */       throw new InternalError("primitive id overflow");
/*     */     }
/* 123 */     return unusedPrimID++;
/*     */   }
/*     */ 
/*     */   public static final synchronized int makeUniqueID(int paramInt, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 131 */     return paramInt << 24 | paramSurfaceType2.getUniqueID() << 16 | paramCompositeType.getUniqueID() << 8 | paramSurfaceType1.getUniqueID();
/*     */   }
/*     */ 
/*     */   protected GraphicsPrimitive(String paramString, int paramInt, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 147 */     this.methodSignature = paramString;
/* 148 */     this.sourceType = paramSurfaceType1;
/* 149 */     this.compositeType = paramCompositeType;
/* 150 */     this.destType = paramSurfaceType2;
/*     */ 
/* 152 */     if ((paramSurfaceType1 == null) || (paramCompositeType == null) || (paramSurfaceType2 == null))
/* 153 */       this.uniqueID = (paramInt << 24);
/*     */     else
/* 155 */       this.uniqueID = makeUniqueID(paramInt, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   protected GraphicsPrimitive(long paramLong, String paramString, int paramInt, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 173 */     this.pNativePrim = paramLong;
/* 174 */     this.methodSignature = paramString;
/* 175 */     this.sourceType = paramSurfaceType1;
/* 176 */     this.compositeType = paramCompositeType;
/* 177 */     this.destType = paramSurfaceType2;
/*     */ 
/* 179 */     if ((paramSurfaceType1 == null) || (paramCompositeType == null) || (paramSurfaceType2 == null))
/* 180 */       this.uniqueID = (paramInt << 24);
/*     */     else
/* 182 */       this.uniqueID = makeUniqueID(paramInt, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public final int getUniqueID()
/*     */   {
/* 209 */     return this.uniqueID;
/*     */   }
/*     */ 
/*     */   public final String getSignature()
/*     */   {
/* 215 */     return this.methodSignature;
/*     */   }
/*     */ 
/*     */   public final int getPrimTypeID()
/*     */   {
/* 227 */     return this.uniqueID >>> 24;
/*     */   }
/*     */ 
/*     */   public final long getNativePrim()
/*     */   {
/* 233 */     return this.pNativePrim;
/*     */   }
/*     */ 
/*     */   public final SurfaceType getSourceType()
/*     */   {
/* 239 */     return this.sourceType;
/*     */   }
/*     */ 
/*     */   public final CompositeType getCompositeType()
/*     */   {
/* 245 */     return this.compositeType;
/*     */   }
/*     */ 
/*     */   public final SurfaceType getDestType()
/*     */   {
/* 251 */     return this.destType;
/*     */   }
/*     */ 
/*     */   public final boolean satisfies(String paramString, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 270 */     if (paramString != this.methodSignature)
/* 271 */       return false;
/*     */     while (true)
/*     */     {
/* 274 */       if (paramSurfaceType1 == null) {
/* 275 */         return false;
/*     */       }
/* 277 */       if (paramSurfaceType1.equals(this.sourceType)) {
/*     */         break;
/*     */       }
/* 280 */       paramSurfaceType1 = paramSurfaceType1.getSuperType();
/*     */     }
/*     */     while (true) {
/* 283 */       if (paramCompositeType == null) {
/* 284 */         return false;
/*     */       }
/* 286 */       if (paramCompositeType.equals(this.compositeType)) {
/*     */         break;
/*     */       }
/* 289 */       paramCompositeType = paramCompositeType.getSuperType();
/*     */     }
/*     */     while (true) {
/* 292 */       if (paramSurfaceType2 == null) {
/* 293 */         return false;
/*     */       }
/* 295 */       if (paramSurfaceType2.equals(this.destType)) {
/*     */         break;
/*     */       }
/* 298 */       paramSurfaceType2 = paramSurfaceType2.getSuperType();
/*     */     }
/* 300 */     return true;
/*     */   }
/*     */ 
/*     */   final boolean satisfiesSameAs(GraphicsPrimitive paramGraphicsPrimitive)
/*     */   {
/* 307 */     return (this.methodSignature == paramGraphicsPrimitive.methodSignature) && (this.sourceType.equals(paramGraphicsPrimitive.sourceType)) && (this.compositeType.equals(paramGraphicsPrimitive.compositeType)) && (this.destType.equals(paramGraphicsPrimitive.destType));
/*     */   }
/*     */ 
/*     */   public abstract GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2);
/*     */ 
/*     */   public abstract GraphicsPrimitive traceWrap();
/*     */ 
/*     */   public static boolean tracingEnabled()
/*     */   {
/* 388 */     return traceflags != 0;
/*     */   }
/*     */ 
/*     */   private static PrintStream getTraceOutputFile() {
/* 392 */     if (traceout == null) {
/* 393 */       if (tracefile != null) {
/* 394 */         Object localObject = AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/*     */             try {
/* 398 */               return new FileOutputStream(GraphicsPrimitive.tracefile); } catch (FileNotFoundException localFileNotFoundException) {
/*     */             }
/* 400 */             return null;
/*     */           }
/*     */         });
/* 404 */         if (localObject != null)
/* 405 */           traceout = new PrintStream((OutputStream)localObject);
/*     */         else
/* 407 */           traceout = System.err;
/*     */       }
/*     */       else {
/* 410 */         traceout = System.err;
/*     */       }
/*     */     }
/* 413 */     return traceout;
/*     */   }
/*     */ 
/*     */   public static synchronized void tracePrimitive(Object paramObject)
/*     */   {
/*     */     Object localObject;
/* 456 */     if ((traceflags & 0x4) != 0) {
/* 457 */       if (traceMap == null) {
/* 458 */         traceMap = new HashMap();
/* 459 */         TraceReporter.setShutdownHook();
/*     */       }
/* 461 */       localObject = traceMap.get(paramObject);
/* 462 */       if (localObject == null) {
/* 463 */         localObject = new int[1];
/* 464 */         traceMap.put(paramObject, localObject);
/*     */       }
/* 466 */       ((int[])localObject)[0] += 1;
/*     */     }
/* 468 */     if ((traceflags & 0x1) != 0) {
/* 469 */       localObject = getTraceOutputFile();
/* 470 */       if ((traceflags & 0x2) != 0) {
/* 471 */         ((PrintStream)localObject).print(System.currentTimeMillis() + ": ");
/*     */       }
/* 473 */       ((PrintStream)localObject).println(paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setupGeneralBinaryOp(GeneralBinaryOp paramGeneralBinaryOp) {
/* 478 */     int i = paramGeneralBinaryOp.getPrimTypeID();
/* 479 */     String str = paramGeneralBinaryOp.getSignature();
/* 480 */     SurfaceType localSurfaceType1 = paramGeneralBinaryOp.getSourceType();
/* 481 */     CompositeType localCompositeType = paramGeneralBinaryOp.getCompositeType();
/* 482 */     SurfaceType localSurfaceType2 = paramGeneralBinaryOp.getDestType();
/*     */ 
/* 486 */     Blit localBlit1 = createConverter(localSurfaceType1, SurfaceType.IntArgb);
/* 487 */     GraphicsPrimitive localGraphicsPrimitive = GraphicsPrimitiveMgr.locatePrim(i, SurfaceType.IntArgb, localCompositeType, localSurfaceType2);
/*     */     Blit localBlit2;
/*     */     Blit localBlit3;
/* 490 */     if (localGraphicsPrimitive != null) {
/* 491 */       localBlit2 = null;
/* 492 */       localBlit3 = null;
/*     */     } else {
/* 494 */       localGraphicsPrimitive = getGeneralOp(i, localCompositeType);
/* 495 */       if (localGraphicsPrimitive == null) {
/* 496 */         throw new InternalError("Cannot construct general op for " + str + " " + localCompositeType);
/*     */       }
/*     */ 
/* 499 */       localBlit2 = createConverter(localSurfaceType2, SurfaceType.IntArgb);
/* 500 */       localBlit3 = createConverter(SurfaceType.IntArgb, localSurfaceType2);
/*     */     }
/*     */ 
/* 503 */     paramGeneralBinaryOp.setPrimitives(localBlit1, localBlit2, localGraphicsPrimitive, localBlit3);
/*     */   }
/*     */ 
/*     */   protected void setupGeneralUnaryOp(GeneralUnaryOp paramGeneralUnaryOp) {
/* 507 */     int i = paramGeneralUnaryOp.getPrimTypeID();
/* 508 */     String str = paramGeneralUnaryOp.getSignature();
/* 509 */     CompositeType localCompositeType = paramGeneralUnaryOp.getCompositeType();
/* 510 */     SurfaceType localSurfaceType = paramGeneralUnaryOp.getDestType();
/*     */ 
/* 512 */     Blit localBlit1 = createConverter(localSurfaceType, SurfaceType.IntArgb);
/* 513 */     GraphicsPrimitive localGraphicsPrimitive = getGeneralOp(i, localCompositeType);
/* 514 */     Blit localBlit2 = createConverter(SurfaceType.IntArgb, localSurfaceType);
/* 515 */     if ((localBlit1 == null) || (localGraphicsPrimitive == null) || (localBlit2 == null)) {
/* 516 */       throw new InternalError("Cannot construct binary op for " + localCompositeType + " " + localSurfaceType);
/*     */     }
/*     */ 
/* 520 */     paramGeneralUnaryOp.setPrimitives(localBlit1, localGraphicsPrimitive, localBlit2);
/*     */   }
/*     */ 
/*     */   protected static Blit createConverter(SurfaceType paramSurfaceType1, SurfaceType paramSurfaceType2)
/*     */   {
/* 526 */     if (paramSurfaceType1.equals(paramSurfaceType2)) {
/* 527 */       return null;
/*     */     }
/* 529 */     Blit localBlit = Blit.getFromCache(paramSurfaceType1, CompositeType.SrcNoEa, paramSurfaceType2);
/* 530 */     if (localBlit == null) {
/* 531 */       throw new InternalError("Cannot construct converter for " + paramSurfaceType1 + "=>" + paramSurfaceType2);
/*     */     }
/*     */ 
/* 534 */     return localBlit;
/*     */   }
/*     */ 
/*     */   protected static SurfaceData convertFrom(Blit paramBlit, SurfaceData paramSurfaceData1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, SurfaceData paramSurfaceData2)
/*     */   {
/* 541 */     return convertFrom(paramBlit, paramSurfaceData1, paramInt1, paramInt2, paramInt3, paramInt4, paramSurfaceData2, 2);
/*     */   }
/*     */ 
/*     */   protected static SurfaceData convertFrom(Blit paramBlit, SurfaceData paramSurfaceData1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, SurfaceData paramSurfaceData2, int paramInt5)
/*     */   {
/*     */     Object localObject;
/* 550 */     if (paramSurfaceData2 != null) {
/* 551 */       localObject = paramSurfaceData2.getBounds();
/* 552 */       if ((paramInt3 > ((Rectangle)localObject).width) || (paramInt4 > ((Rectangle)localObject).height)) {
/* 553 */         paramSurfaceData2 = null;
/*     */       }
/*     */     }
/* 556 */     if (paramSurfaceData2 == null) {
/* 557 */       localObject = new BufferedImage(paramInt3, paramInt4, paramInt5);
/* 558 */       paramSurfaceData2 = BufImgSurfaceData.createData((BufferedImage)localObject);
/*     */     }
/* 560 */     paramBlit.Blit(paramSurfaceData1, paramSurfaceData2, AlphaComposite.Src, null, paramInt1, paramInt2, 0, 0, paramInt3, paramInt4);
/*     */ 
/* 562 */     return paramSurfaceData2;
/*     */   }
/*     */ 
/*     */   protected static void convertTo(Blit paramBlit, SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 570 */     if (paramBlit != null)
/* 571 */       paramBlit.Blit(paramSurfaceData1, paramSurfaceData2, AlphaComposite.Src, paramRegion, 0, 0, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   protected static GraphicsPrimitive getGeneralOp(int paramInt, CompositeType paramCompositeType)
/*     */   {
/* 579 */     return GraphicsPrimitiveMgr.locatePrim(paramInt, SurfaceType.IntArgb, paramCompositeType, SurfaceType.IntArgb);
/*     */   }
/*     */ 
/*     */   public static String simplename(Field[] paramArrayOfField, Object paramObject)
/*     */   {
/* 586 */     for (int i = 0; i < paramArrayOfField.length; i++) {
/* 587 */       Field localField = paramArrayOfField[i];
/*     */       try {
/* 589 */         if (paramObject == localField.get(null))
/* 590 */           return localField.getName();
/*     */       }
/*     */       catch (Exception localException) {
/*     */       }
/*     */     }
/* 595 */     return "\"" + paramObject.toString() + "\"";
/*     */   }
/*     */ 
/*     */   public static String simplename(SurfaceType paramSurfaceType) {
/* 599 */     return simplename(SurfaceType.class.getDeclaredFields(), paramSurfaceType);
/*     */   }
/*     */ 
/*     */   public static String simplename(CompositeType paramCompositeType) {
/* 603 */     return simplename(CompositeType.class.getDeclaredFields(), paramCompositeType);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 609 */     if (this.cachedname == null) {
/* 610 */       String str = this.methodSignature;
/* 611 */       int i = str.indexOf('(');
/* 612 */       if (i >= 0) {
/* 613 */         str = str.substring(0, i);
/*     */       }
/* 615 */       this.cachedname = (getClass().getName() + "::" + str + "(" + simplename(this.sourceType) + ", " + simplename(this.compositeType) + ", " + simplename(this.destType) + ")");
/*     */     }
/*     */ 
/* 621 */     return this.cachedname;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 330 */     GetPropertyAction localGetPropertyAction = new GetPropertyAction("sun.java2d.trace");
/* 331 */     String str1 = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 332 */     if (str1 != null) {
/* 333 */       int i = 0;
/* 334 */       int j = 0;
/* 335 */       StringTokenizer localStringTokenizer = new StringTokenizer(str1, ",");
/* 336 */       while (localStringTokenizer.hasMoreTokens()) {
/* 337 */         String str2 = localStringTokenizer.nextToken();
/* 338 */         if (str2.equalsIgnoreCase("count")) {
/* 339 */           j |= 4;
/* 340 */         } else if (str2.equalsIgnoreCase("log")) {
/* 341 */           j |= 1;
/* 342 */         } else if (str2.equalsIgnoreCase("timestamp")) {
/* 343 */           j |= 2;
/* 344 */         } else if (str2.equalsIgnoreCase("verbose")) {
/* 345 */           i = 1;
/* 346 */         } else if (str2.regionMatches(true, 0, "out:", 0, 4)) {
/* 347 */           tracefile = str2.substring(4);
/*     */         } else {
/* 349 */           if (!str2.equalsIgnoreCase("help")) {
/* 350 */             System.err.println("unrecognized token: " + str2);
/*     */           }
/* 352 */           System.err.println("usage: -Dsun.java2d.trace=[log[,timestamp]],[count],[out:<filename>],[help],[verbose]");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 357 */       if (i != 0) {
/* 358 */         System.err.print("GraphicsPrimitive logging ");
/* 359 */         if ((j & 0x1) != 0) {
/* 360 */           System.err.println("enabled");
/* 361 */           System.err.print("GraphicsPrimitive timetamps ");
/* 362 */           if ((j & 0x2) != 0)
/* 363 */             System.err.println("enabled");
/*     */           else
/* 365 */             System.err.println("disabled");
/*     */         }
/*     */         else {
/* 368 */           System.err.println("[and timestamps] disabled");
/*     */         }
/* 370 */         System.err.print("GraphicsPrimitive invocation counts ");
/* 371 */         if ((j & 0x4) != 0)
/* 372 */           System.err.println("enabled");
/*     */         else {
/* 374 */           System.err.println("disabled");
/*     */         }
/* 376 */         System.err.print("GraphicsPrimitive trace output to ");
/* 377 */         if (tracefile == null)
/* 378 */           System.err.println("System.err");
/*     */         else {
/* 380 */           System.err.println("file '" + tracefile + "'");
/*     */         }
/*     */       }
/* 383 */       traceflags = j;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static abstract interface GeneralBinaryOp
/*     */   {
/*     */     public abstract void setPrimitives(Blit paramBlit1, Blit paramBlit2, GraphicsPrimitive paramGraphicsPrimitive, Blit paramBlit3);
/*     */ 
/*     */     public abstract SurfaceType getSourceType();
/*     */ 
/*     */     public abstract CompositeType getCompositeType();
/*     */ 
/*     */     public abstract SurfaceType getDestType();
/*     */ 
/*     */     public abstract String getSignature();
/*     */ 
/*     */     public abstract int getPrimTypeID();
/*     */   }
/*     */ 
/*     */   protected static abstract interface GeneralUnaryOp
/*     */   {
/*     */     public abstract void setPrimitives(Blit paramBlit1, GraphicsPrimitive paramGraphicsPrimitive, Blit paramBlit2);
/*     */ 
/*     */     public abstract CompositeType getCompositeType();
/*     */ 
/*     */     public abstract SurfaceType getDestType();
/*     */ 
/*     */     public abstract String getSignature();
/*     */ 
/*     */     public abstract int getPrimTypeID();
/*     */   }
/*     */ 
/*     */   public static class TraceReporter extends Thread
/*     */   {
/*     */     public static void setShutdownHook() {
/* 418 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Object run() {
/* 420 */           GraphicsPrimitive.TraceReporter localTraceReporter = new GraphicsPrimitive.TraceReporter();
/* 421 */           localTraceReporter.setContextClassLoader(null);
/* 422 */           Runtime.getRuntime().addShutdownHook(localTraceReporter);
/* 423 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*     */     public void run() {
/* 429 */       PrintStream localPrintStream = GraphicsPrimitive.access$000();
/* 430 */       Iterator localIterator = GraphicsPrimitive.traceMap.entrySet().iterator();
/* 431 */       long l = 0L;
/* 432 */       int i = 0;
/* 433 */       while (localIterator.hasNext()) {
/* 434 */         Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 435 */         Object localObject = localEntry.getKey();
/* 436 */         int[] arrayOfInt = (int[])localEntry.getValue();
/* 437 */         if (arrayOfInt[0] == 1)
/* 438 */           localPrintStream.print("1 call to ");
/*     */         else {
/* 440 */           localPrintStream.print(arrayOfInt[0] + " calls to ");
/*     */         }
/* 442 */         localPrintStream.println(localObject);
/* 443 */         i++;
/* 444 */         l += arrayOfInt[0];
/*     */       }
/* 446 */       if (i == 0)
/* 447 */         localPrintStream.println("No graphics primitives executed");
/* 448 */       else if (i > 1)
/* 449 */         localPrintStream.println(l + " total calls to " + i + " different primitives");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.GraphicsPrimitive
 * JD-Core Version:    0.6.2
 */