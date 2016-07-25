/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.swing.JComponent;
/*     */ 
/*     */ public class SynthContext
/*     */ {
/*  51 */   private static final Map<Class, List<SynthContext>> contextMap = new HashMap();
/*     */   private JComponent component;
/*     */   private Region region;
/*     */   private SynthStyle style;
/*     */   private int state;
/*     */ 
/*     */   static SynthContext getContext(Class paramClass, JComponent paramJComponent, Region paramRegion, SynthStyle paramSynthStyle, int paramInt)
/*     */   {
/*  58 */     SynthContext localSynthContext = null;
/*     */ 
/*  60 */     synchronized (contextMap) {
/*  61 */       List localList = (List)contextMap.get(paramClass);
/*     */ 
/*  63 */       if (localList != null) {
/*  64 */         int i = localList.size();
/*     */ 
/*  66 */         if (i > 0) {
/*  67 */           localSynthContext = (SynthContext)localList.remove(i - 1);
/*     */         }
/*     */       }
/*     */     }
/*  71 */     if (localSynthContext == null)
/*     */       try {
/*  73 */         localSynthContext = (SynthContext)paramClass.newInstance();
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/*     */       }
/*     */       catch (InstantiationException localInstantiationException) {
/*     */       }
/*  78 */     localSynthContext.reset(paramJComponent, paramRegion, paramSynthStyle, paramInt);
/*  79 */     return localSynthContext;
/*     */   }
/*     */ 
/*     */   static void releaseContext(SynthContext paramSynthContext) {
/*  83 */     synchronized (contextMap) {
/*  84 */       Object localObject1 = (List)contextMap.get(paramSynthContext.getClass());
/*     */ 
/*  86 */       if (localObject1 == null) {
/*  87 */         localObject1 = new ArrayList(5);
/*  88 */         contextMap.put(paramSynthContext.getClass(), localObject1);
/*     */       }
/*  90 */       ((List)localObject1).add(paramSynthContext);
/*     */     }
/*     */   }
/*     */ 
/*     */   SynthContext()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SynthContext(JComponent paramJComponent, Region paramRegion, SynthStyle paramSynthStyle, int paramInt)
/*     */   {
/* 111 */     if ((paramJComponent == null) || (paramRegion == null) || (paramSynthStyle == null)) {
/* 112 */       throw new NullPointerException("You must supply a non-null component, region and style");
/*     */     }
/*     */ 
/* 115 */     reset(paramJComponent, paramRegion, paramSynthStyle, paramInt);
/*     */   }
/*     */ 
/*     */   public JComponent getComponent()
/*     */   {
/* 125 */     return this.component;
/*     */   }
/*     */ 
/*     */   public Region getRegion()
/*     */   {
/* 134 */     return this.region;
/*     */   }
/*     */ 
/*     */   boolean isSubregion()
/*     */   {
/* 141 */     return getRegion().isSubregion();
/*     */   }
/*     */ 
/*     */   void setStyle(SynthStyle paramSynthStyle) {
/* 145 */     this.style = paramSynthStyle;
/*     */   }
/*     */ 
/*     */   public SynthStyle getStyle()
/*     */   {
/* 154 */     return this.style;
/*     */   }
/*     */ 
/*     */   void setComponentState(int paramInt) {
/* 158 */     this.state = paramInt;
/*     */   }
/*     */ 
/*     */   public int getComponentState()
/*     */   {
/* 172 */     return this.state;
/*     */   }
/*     */ 
/*     */   void reset(JComponent paramJComponent, Region paramRegion, SynthStyle paramSynthStyle, int paramInt)
/*     */   {
/* 180 */     this.component = paramJComponent;
/* 181 */     this.region = paramRegion;
/* 182 */     this.style = paramSynthStyle;
/* 183 */     this.state = paramInt;
/*     */   }
/*     */ 
/*     */   void dispose() {
/* 187 */     this.component = null;
/* 188 */     this.style = null;
/* 189 */     releaseContext(this);
/*     */   }
/*     */ 
/*     */   SynthPainter getPainter()
/*     */   {
/* 197 */     SynthPainter localSynthPainter = getStyle().getPainter(this);
/*     */ 
/* 199 */     if (localSynthPainter != null) {
/* 200 */       return localSynthPainter;
/*     */     }
/* 202 */     return SynthPainter.NULL_PAINTER;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthContext
 * JD-Core Version:    0.6.2
 */