/*      */ package java.awt;
/*      */ 
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import sun.awt.SunHints;
/*      */ 
/*      */ public class RenderingHints
/*      */   implements Map<Object, Object>, Cloneable
/*      */ {
/*  198 */   HashMap hintmap = new HashMap(7);
/*      */ 
/*  219 */   public static final Key KEY_ANTIALIASING = SunHints.KEY_ANTIALIASING;
/*      */ 
/*  226 */   public static final Object VALUE_ANTIALIAS_ON = SunHints.VALUE_ANTIALIAS_ON;
/*      */ 
/*  233 */   public static final Object VALUE_ANTIALIAS_OFF = SunHints.VALUE_ANTIALIAS_OFF;
/*      */ 
/*  241 */   public static final Object VALUE_ANTIALIAS_DEFAULT = SunHints.VALUE_ANTIALIAS_DEFAULT;
/*      */ 
/*  260 */   public static final Key KEY_RENDERING = SunHints.KEY_RENDERING;
/*      */ 
/*  268 */   public static final Object VALUE_RENDER_SPEED = SunHints.VALUE_RENDER_SPEED;
/*      */ 
/*  276 */   public static final Object VALUE_RENDER_QUALITY = SunHints.VALUE_RENDER_QUALITY;
/*      */ 
/*  285 */   public static final Object VALUE_RENDER_DEFAULT = SunHints.VALUE_RENDER_DEFAULT;
/*      */ 
/*  311 */   public static final Key KEY_DITHERING = SunHints.KEY_DITHERING;
/*      */ 
/*  318 */   public static final Object VALUE_DITHER_DISABLE = SunHints.VALUE_DITHER_DISABLE;
/*      */ 
/*  325 */   public static final Object VALUE_DITHER_ENABLE = SunHints.VALUE_DITHER_ENABLE;
/*      */ 
/*  333 */   public static final Object VALUE_DITHER_DEFAULT = SunHints.VALUE_DITHER_DEFAULT;
/*      */ 
/*  364 */   public static final Key KEY_TEXT_ANTIALIASING = SunHints.KEY_TEXT_ANTIALIASING;
/*      */ 
/*  372 */   public static final Object VALUE_TEXT_ANTIALIAS_ON = SunHints.VALUE_TEXT_ANTIALIAS_ON;
/*      */ 
/*  380 */   public static final Object VALUE_TEXT_ANTIALIAS_OFF = SunHints.VALUE_TEXT_ANTIALIAS_OFF;
/*      */ 
/*  389 */   public static final Object VALUE_TEXT_ANTIALIAS_DEFAULT = SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
/*      */ 
/*  416 */   public static final Object VALUE_TEXT_ANTIALIAS_GASP = SunHints.VALUE_TEXT_ANTIALIAS_GASP;
/*      */ 
/*  463 */   public static final Object VALUE_TEXT_ANTIALIAS_LCD_HRGB = SunHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
/*      */ 
/*  480 */   public static final Object VALUE_TEXT_ANTIALIAS_LCD_HBGR = SunHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR;
/*      */ 
/*  498 */   public static final Object VALUE_TEXT_ANTIALIAS_LCD_VRGB = SunHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB;
/*      */ 
/*  516 */   public static final Object VALUE_TEXT_ANTIALIAS_LCD_VBGR = SunHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR;
/*      */ 
/*  548 */   public static final Key KEY_TEXT_LCD_CONTRAST = SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST;
/*      */ 
/*  636 */   public static final Key KEY_FRACTIONALMETRICS = SunHints.KEY_FRACTIONALMETRICS;
/*      */ 
/*  644 */   public static final Object VALUE_FRACTIONALMETRICS_OFF = SunHints.VALUE_FRACTIONALMETRICS_OFF;
/*      */ 
/*  652 */   public static final Object VALUE_FRACTIONALMETRICS_ON = SunHints.VALUE_FRACTIONALMETRICS_ON;
/*      */ 
/*  660 */   public static final Object VALUE_FRACTIONALMETRICS_DEFAULT = SunHints.VALUE_FRACTIONALMETRICS_DEFAULT;
/*      */ 
/*  693 */   public static final Key KEY_INTERPOLATION = SunHints.KEY_INTERPOLATION;
/*      */ 
/*  710 */   public static final Object VALUE_INTERPOLATION_NEAREST_NEIGHBOR = SunHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
/*      */ 
/*  737 */   public static final Object VALUE_INTERPOLATION_BILINEAR = SunHints.VALUE_INTERPOLATION_BILINEAR;
/*      */ 
/*  760 */   public static final Object VALUE_INTERPOLATION_BICUBIC = SunHints.VALUE_INTERPOLATION_BICUBIC;
/*      */ 
/*  785 */   public static final Key KEY_ALPHA_INTERPOLATION = SunHints.KEY_ALPHA_INTERPOLATION;
/*      */ 
/*  793 */   public static final Object VALUE_ALPHA_INTERPOLATION_SPEED = SunHints.VALUE_ALPHA_INTERPOLATION_SPEED;
/*      */ 
/*  801 */   public static final Object VALUE_ALPHA_INTERPOLATION_QUALITY = SunHints.VALUE_ALPHA_INTERPOLATION_QUALITY;
/*      */ 
/*  810 */   public static final Object VALUE_ALPHA_INTERPOLATION_DEFAULT = SunHints.VALUE_ALPHA_INTERPOLATION_DEFAULT;
/*      */ 
/*  861 */   public static final Key KEY_COLOR_RENDERING = SunHints.KEY_COLOR_RENDERING;
/*      */ 
/*  869 */   public static final Object VALUE_COLOR_RENDER_SPEED = SunHints.VALUE_COLOR_RENDER_SPEED;
/*      */ 
/*  877 */   public static final Object VALUE_COLOR_RENDER_QUALITY = SunHints.VALUE_COLOR_RENDER_QUALITY;
/*      */ 
/*  887 */   public static final Object VALUE_COLOR_RENDER_DEFAULT = SunHints.VALUE_COLOR_RENDER_DEFAULT;
/*      */ 
/*  916 */   public static final Key KEY_STROKE_CONTROL = SunHints.KEY_STROKE_CONTROL;
/*      */ 
/*  931 */   public static final Object VALUE_STROKE_DEFAULT = SunHints.VALUE_STROKE_DEFAULT;
/*      */ 
/*  944 */   public static final Object VALUE_STROKE_NORMALIZE = SunHints.VALUE_STROKE_NORMALIZE;
/*      */ 
/*  954 */   public static final Object VALUE_STROKE_PURE = SunHints.VALUE_STROKE_PURE;
/*      */ 
/*      */   public RenderingHints(Map<Key, ?> paramMap)
/*      */   {
/*  964 */     if (paramMap != null)
/*  965 */       this.hintmap.putAll(paramMap);
/*      */   }
/*      */ 
/*      */   public RenderingHints(Key paramKey, Object paramObject)
/*      */   {
/*  976 */     this.hintmap.put(paramKey, paramObject);
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  987 */     return this.hintmap.size();
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  998 */     return this.hintmap.isEmpty();
/*      */   }
/*      */ 
/*      */   public boolean containsKey(Object paramObject)
/*      */   {
/* 1013 */     return this.hintmap.containsKey((Key)paramObject);
/*      */   }
/*      */ 
/*      */   public boolean containsValue(Object paramObject)
/*      */   {
/* 1035 */     return this.hintmap.containsValue(paramObject);
/*      */   }
/*      */ 
/*      */   public Object get(Object paramObject)
/*      */   {
/* 1049 */     return this.hintmap.get((Key)paramObject);
/*      */   }
/*      */ 
/*      */   public Object put(Object paramObject1, Object paramObject2)
/*      */   {
/* 1074 */     if (!((Key)paramObject1).isCompatibleValue(paramObject2)) {
/* 1075 */       throw new IllegalArgumentException(paramObject2 + " incompatible with " + paramObject1);
/*      */     }
/*      */ 
/* 1079 */     return this.hintmap.put((Key)paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public void add(RenderingHints paramRenderingHints)
/*      */   {
/* 1092 */     this.hintmap.putAll(paramRenderingHints.hintmap);
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/* 1100 */     this.hintmap.clear();
/*      */   }
/*      */ 
/*      */   public Object remove(Object paramObject)
/*      */   {
/* 1115 */     return this.hintmap.remove((Key)paramObject);
/*      */   }
/*      */ 
/*      */   public void putAll(Map<?, ?> paramMap)
/*      */   {
/*      */     Iterator localIterator;
/*      */     Map.Entry localEntry;
/* 1135 */     if (RenderingHints.class.isInstance(paramMap))
/*      */     {
/* 1137 */       for (localIterator = paramMap.entrySet().iterator(); localIterator.hasNext(); ) { localEntry = (Map.Entry)localIterator.next();
/* 1138 */         this.hintmap.put(localEntry.getKey(), localEntry.getValue()); }
/*      */     }
/*      */     else
/* 1141 */       for (localIterator = paramMap.entrySet().iterator(); localIterator.hasNext(); ) { localEntry = (Map.Entry)localIterator.next();
/* 1142 */         put(localEntry.getKey(), localEntry.getValue());
/*      */       }
/*      */   }
/*      */ 
/*      */   public Set<Object> keySet()
/*      */   {
/* 1165 */     return this.hintmap.keySet();
/*      */   }
/*      */ 
/*      */   public Collection<Object> values()
/*      */   {
/* 1191 */     return this.hintmap.values();
/*      */   }
/*      */ 
/*      */   public Set<Map.Entry<Object, Object>> entrySet()
/*      */   {
/* 1212 */     return Collections.unmodifiableMap(this.hintmap).entrySet();
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1236 */     if ((paramObject instanceof RenderingHints))
/* 1237 */       return this.hintmap.equals(((RenderingHints)paramObject).hintmap);
/* 1238 */     if ((paramObject instanceof Map)) {
/* 1239 */       return this.hintmap.equals(paramObject);
/*      */     }
/* 1241 */     return false;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1261 */     return this.hintmap.hashCode();
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     RenderingHints localRenderingHints;
/*      */     try
/*      */     {
/* 1273 */       localRenderingHints = (RenderingHints)super.clone();
/* 1274 */       if (this.hintmap != null)
/* 1275 */         localRenderingHints.hintmap = ((HashMap)this.hintmap.clone());
/*      */     }
/*      */     catch (CloneNotSupportedException localCloneNotSupportedException)
/*      */     {
/* 1279 */       throw new InternalError();
/*      */     }
/*      */ 
/* 1282 */     return localRenderingHints;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1292 */     if (this.hintmap == null) {
/* 1293 */       return getClass().getName() + "@" + Integer.toHexString(hashCode()) + " (0 hints)";
/*      */     }
/*      */ 
/* 1298 */     return this.hintmap.toString();
/*      */   }
/*      */ 
/*      */   public static abstract class Key
/*      */   {
/*   95 */     private static HashMap identitymap = new HashMap(17);
/*      */     private int privatekey;
/*      */ 
/*      */     private String getIdentity()
/*      */     {
/*  108 */       return getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(getClass())) + ":" + Integer.toHexString(this.privatekey);
/*      */     }
/*      */ 
/*      */     private static synchronized void recordIdentity(Key paramKey)
/*      */     {
/*  114 */       String str = paramKey.getIdentity();
/*  115 */       Object localObject = identitymap.get(str);
/*  116 */       if (localObject != null) {
/*  117 */         Key localKey = (Key)((WeakReference)localObject).get();
/*  118 */         if ((localKey != null) && (localKey.getClass() == paramKey.getClass())) {
/*  119 */           throw new IllegalArgumentException(str + " already registered");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  141 */       identitymap.put(str, new WeakReference(paramKey));
/*      */     }
/*      */ 
/*      */     protected Key(int paramInt)
/*      */     {
/*  157 */       this.privatekey = paramInt;
/*  158 */       recordIdentity(this);
/*      */     }
/*      */ 
/*      */     public abstract boolean isCompatibleValue(Object paramObject);
/*      */ 
/*      */     protected final int intKey()
/*      */     {
/*  177 */       return this.privatekey;
/*      */     }
/*      */ 
/*      */     public final int hashCode()
/*      */     {
/*  186 */       return super.hashCode();
/*      */     }
/*      */ 
/*      */     public final boolean equals(Object paramObject)
/*      */     {
/*  194 */       return this == paramObject;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.RenderingHints
 * JD-Core Version:    0.6.2
 */