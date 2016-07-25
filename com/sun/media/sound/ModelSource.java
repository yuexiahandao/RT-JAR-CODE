/*     */ package com.sun.media.sound;
/*     */ 
/*     */ public final class ModelSource
/*     */ {
/*  35 */   public static final ModelIdentifier SOURCE_NONE = null;
/*  36 */   public static final ModelIdentifier SOURCE_NOTEON_KEYNUMBER = new ModelIdentifier("noteon", "keynumber");
/*     */ 
/*  38 */   public static final ModelIdentifier SOURCE_NOTEON_VELOCITY = new ModelIdentifier("noteon", "velocity");
/*     */ 
/*  40 */   public static final ModelIdentifier SOURCE_EG1 = new ModelIdentifier("eg", null, 0);
/*     */ 
/*  42 */   public static final ModelIdentifier SOURCE_EG2 = new ModelIdentifier("eg", null, 1);
/*     */ 
/*  44 */   public static final ModelIdentifier SOURCE_LFO1 = new ModelIdentifier("lfo", null, 0);
/*     */ 
/*  46 */   public static final ModelIdentifier SOURCE_LFO2 = new ModelIdentifier("lfo", null, 1);
/*     */ 
/*  48 */   public static final ModelIdentifier SOURCE_MIDI_PITCH = new ModelIdentifier("midi", "pitch", 0);
/*     */ 
/*  50 */   public static final ModelIdentifier SOURCE_MIDI_CHANNEL_PRESSURE = new ModelIdentifier("midi", "channel_pressure", 0);
/*     */ 
/*  54 */   public static final ModelIdentifier SOURCE_MIDI_POLY_PRESSURE = new ModelIdentifier("midi", "poly_pressure", 0);
/*     */ 
/*  56 */   public static final ModelIdentifier SOURCE_MIDI_CC_0 = new ModelIdentifier("midi_cc", "0", 0);
/*     */ 
/*  58 */   public static final ModelIdentifier SOURCE_MIDI_RPN_0 = new ModelIdentifier("midi_rpn", "0", 0);
/*     */ 
/*  60 */   private ModelIdentifier source = SOURCE_NONE;
/*     */   private ModelTransform transform;
/*     */ 
/*     */   public ModelSource()
/*     */   {
/*  64 */     this.transform = new ModelStandardTransform();
/*     */   }
/*     */ 
/*     */   public ModelSource(ModelIdentifier paramModelIdentifier) {
/*  68 */     this.source = paramModelIdentifier;
/*  69 */     this.transform = new ModelStandardTransform();
/*     */   }
/*     */ 
/*     */   public ModelSource(ModelIdentifier paramModelIdentifier, boolean paramBoolean) {
/*  73 */     this.source = paramModelIdentifier;
/*  74 */     this.transform = new ModelStandardTransform(paramBoolean);
/*     */   }
/*     */ 
/*     */   public ModelSource(ModelIdentifier paramModelIdentifier, boolean paramBoolean1, boolean paramBoolean2) {
/*  78 */     this.source = paramModelIdentifier;
/*  79 */     this.transform = new ModelStandardTransform(paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   public ModelSource(ModelIdentifier paramModelIdentifier, boolean paramBoolean1, boolean paramBoolean2, int paramInt)
/*     */   {
/*  84 */     this.source = paramModelIdentifier;
/*  85 */     this.transform = new ModelStandardTransform(paramBoolean1, paramBoolean2, paramInt);
/*     */   }
/*     */ 
/*     */   public ModelSource(ModelIdentifier paramModelIdentifier, ModelTransform paramModelTransform)
/*     */   {
/*  90 */     this.source = paramModelIdentifier;
/*  91 */     this.transform = paramModelTransform;
/*     */   }
/*     */ 
/*     */   public ModelIdentifier getIdentifier() {
/*  95 */     return this.source;
/*     */   }
/*     */ 
/*     */   public void setIdentifier(ModelIdentifier paramModelIdentifier) {
/*  99 */     this.source = paramModelIdentifier;
/*     */   }
/*     */ 
/*     */   public ModelTransform getTransform() {
/* 103 */     return this.transform;
/*     */   }
/*     */ 
/*     */   public void setTransform(ModelTransform paramModelTransform) {
/* 107 */     this.transform = paramModelTransform;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelSource
 * JD-Core Version:    0.6.2
 */