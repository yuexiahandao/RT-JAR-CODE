/*     */ package com.sun.media.sound;
/*     */ 
/*     */ public final class ModelDestination
/*     */ {
/*  35 */   public static final ModelIdentifier DESTINATION_NONE = null;
/*  36 */   public static final ModelIdentifier DESTINATION_KEYNUMBER = new ModelIdentifier("noteon", "keynumber");
/*     */ 
/*  38 */   public static final ModelIdentifier DESTINATION_VELOCITY = new ModelIdentifier("noteon", "velocity");
/*     */ 
/*  40 */   public static final ModelIdentifier DESTINATION_PITCH = new ModelIdentifier("osc", "pitch");
/*     */ 
/*  42 */   public static final ModelIdentifier DESTINATION_GAIN = new ModelIdentifier("mixer", "gain");
/*     */ 
/*  44 */   public static final ModelIdentifier DESTINATION_PAN = new ModelIdentifier("mixer", "pan");
/*     */ 
/*  46 */   public static final ModelIdentifier DESTINATION_REVERB = new ModelIdentifier("mixer", "reverb");
/*     */ 
/*  48 */   public static final ModelIdentifier DESTINATION_CHORUS = new ModelIdentifier("mixer", "chorus");
/*     */ 
/*  50 */   public static final ModelIdentifier DESTINATION_LFO1_DELAY = new ModelIdentifier("lfo", "delay", 0);
/*     */ 
/*  52 */   public static final ModelIdentifier DESTINATION_LFO1_FREQ = new ModelIdentifier("lfo", "freq", 0);
/*     */ 
/*  54 */   public static final ModelIdentifier DESTINATION_LFO2_DELAY = new ModelIdentifier("lfo", "delay", 1);
/*     */ 
/*  56 */   public static final ModelIdentifier DESTINATION_LFO2_FREQ = new ModelIdentifier("lfo", "freq", 1);
/*     */ 
/*  58 */   public static final ModelIdentifier DESTINATION_EG1_DELAY = new ModelIdentifier("eg", "delay", 0);
/*     */ 
/*  60 */   public static final ModelIdentifier DESTINATION_EG1_ATTACK = new ModelIdentifier("eg", "attack", 0);
/*     */ 
/*  62 */   public static final ModelIdentifier DESTINATION_EG1_HOLD = new ModelIdentifier("eg", "hold", 0);
/*     */ 
/*  64 */   public static final ModelIdentifier DESTINATION_EG1_DECAY = new ModelIdentifier("eg", "decay", 0);
/*     */ 
/*  66 */   public static final ModelIdentifier DESTINATION_EG1_SUSTAIN = new ModelIdentifier("eg", "sustain", 0);
/*     */ 
/*  69 */   public static final ModelIdentifier DESTINATION_EG1_RELEASE = new ModelIdentifier("eg", "release", 0);
/*     */ 
/*  71 */   public static final ModelIdentifier DESTINATION_EG1_SHUTDOWN = new ModelIdentifier("eg", "shutdown", 0);
/*     */ 
/*  73 */   public static final ModelIdentifier DESTINATION_EG2_DELAY = new ModelIdentifier("eg", "delay", 1);
/*     */ 
/*  75 */   public static final ModelIdentifier DESTINATION_EG2_ATTACK = new ModelIdentifier("eg", "attack", 1);
/*     */ 
/*  77 */   public static final ModelIdentifier DESTINATION_EG2_HOLD = new ModelIdentifier("eg", "hold", 1);
/*     */ 
/*  79 */   public static final ModelIdentifier DESTINATION_EG2_DECAY = new ModelIdentifier("eg", "decay", 1);
/*     */ 
/*  81 */   public static final ModelIdentifier DESTINATION_EG2_SUSTAIN = new ModelIdentifier("eg", "sustain", 1);
/*     */ 
/*  84 */   public static final ModelIdentifier DESTINATION_EG2_RELEASE = new ModelIdentifier("eg", "release", 1);
/*     */ 
/*  86 */   public static final ModelIdentifier DESTINATION_EG2_SHUTDOWN = new ModelIdentifier("eg", "shutdown", 1);
/*     */ 
/*  88 */   public static final ModelIdentifier DESTINATION_FILTER_FREQ = new ModelIdentifier("filter", "freq", 0);
/*     */ 
/*  90 */   public static final ModelIdentifier DESTINATION_FILTER_Q = new ModelIdentifier("filter", "q", 0);
/*     */ 
/*  92 */   private ModelIdentifier destination = DESTINATION_NONE;
/*  93 */   private ModelTransform transform = new ModelStandardTransform();
/*     */ 
/*     */   public ModelDestination() {
/*     */   }
/*     */ 
/*     */   public ModelDestination(ModelIdentifier paramModelIdentifier) {
/*  99 */     this.destination = paramModelIdentifier;
/*     */   }
/*     */ 
/*     */   public ModelIdentifier getIdentifier() {
/* 103 */     return this.destination;
/*     */   }
/*     */ 
/*     */   public void setIdentifier(ModelIdentifier paramModelIdentifier) {
/* 107 */     this.destination = paramModelIdentifier;
/*     */   }
/*     */ 
/*     */   public ModelTransform getTransform() {
/* 111 */     return this.transform;
/*     */   }
/*     */ 
/*     */   public void setTransform(ModelTransform paramModelTransform) {
/* 115 */     this.transform = paramModelTransform;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelDestination
 * JD-Core Version:    0.6.2
 */