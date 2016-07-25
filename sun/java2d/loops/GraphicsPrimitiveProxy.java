/*     */ package sun.java2d.loops;
/*     */ 
/*     */ public class GraphicsPrimitiveProxy extends GraphicsPrimitive
/*     */ {
/*     */   private Class owner;
/*     */   private String relativeClassName;
/*     */ 
/*     */   public GraphicsPrimitiveProxy(Class paramClass, String paramString1, String paramString2, int paramInt, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  63 */     super(paramString2, paramInt, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  64 */     this.owner = paramClass;
/*  65 */     this.relativeClassName = paramString1;
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  72 */     throw new InternalError("makePrimitive called on a Proxy!");
/*     */   }
/*     */ 
/*     */   GraphicsPrimitive instantiate()
/*     */   {
/*  80 */     String str = getPackageName(this.owner.getName()) + "." + this.relativeClassName;
/*     */     try
/*     */     {
/*  83 */       Class localClass = Class.forName(str);
/*  84 */       GraphicsPrimitive localGraphicsPrimitive = (GraphicsPrimitive)localClass.newInstance();
/*  85 */       if (!satisfiesSameAs(localGraphicsPrimitive)) {
/*  86 */         throw new RuntimeException("Primitive " + localGraphicsPrimitive + " incompatible with proxy for " + str);
/*     */       }
/*     */ 
/*  90 */       return localGraphicsPrimitive;
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*  92 */       throw new RuntimeException(localClassNotFoundException.toString());
/*     */     } catch (InstantiationException localInstantiationException) {
/*  94 */       throw new RuntimeException(localInstantiationException.toString());
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*  96 */       throw new RuntimeException(localIllegalAccessException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getPackageName(String paramString)
/*     */   {
/* 104 */     int i = paramString.lastIndexOf('.');
/* 105 */     if (i < 0) {
/* 106 */       return paramString;
/*     */     }
/* 108 */     return paramString.substring(0, i);
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap() {
/* 112 */     return instantiate().traceWrap();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.GraphicsPrimitiveProxy
 * JD-Core Version:    0.6.2
 */