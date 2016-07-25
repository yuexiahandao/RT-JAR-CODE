/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import javax.sound.midi.Instrument;
/*     */ import javax.sound.midi.MidiChannel;
/*     */ import javax.sound.midi.Patch;
/*     */ import javax.sound.midi.Soundbank;
/*     */ import javax.sound.midi.SoundbankResource;
/*     */ import javax.sound.midi.VoiceStatus;
/*     */ 
/*     */ public abstract class ModelAbstractOscillator
/*     */   implements ModelOscillator, ModelOscillatorStream, Soundbank
/*     */ {
/*  43 */   protected float pitch = 6000.0F;
/*     */   protected float samplerate;
/*     */   protected MidiChannel channel;
/*     */   protected VoiceStatus voice;
/*     */   protected int noteNumber;
/*     */   protected int velocity;
/*  49 */   protected boolean on = false;
/*     */ 
/*     */   public void init() {
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/*     */   }
/*     */ 
/*     */   public void noteOff(int paramInt) {
/*  58 */     this.on = false;
/*     */   }
/*     */ 
/*     */   public void noteOn(MidiChannel paramMidiChannel, VoiceStatus paramVoiceStatus, int paramInt1, int paramInt2)
/*     */   {
/*  63 */     this.channel = paramMidiChannel;
/*  64 */     this.voice = paramVoiceStatus;
/*  65 */     this.noteNumber = paramInt1;
/*  66 */     this.velocity = paramInt2;
/*  67 */     this.on = true;
/*     */   }
/*     */ 
/*     */   public int read(float[][] paramArrayOfFloat, int paramInt1, int paramInt2) throws IOException {
/*  71 */     return -1;
/*     */   }
/*     */ 
/*     */   public MidiChannel getChannel() {
/*  75 */     return this.channel;
/*     */   }
/*     */ 
/*     */   public VoiceStatus getVoice() {
/*  79 */     return this.voice;
/*     */   }
/*     */ 
/*     */   public int getNoteNumber() {
/*  83 */     return this.noteNumber;
/*     */   }
/*     */ 
/*     */   public int getVelocity() {
/*  87 */     return this.velocity;
/*     */   }
/*     */ 
/*     */   public boolean isOn() {
/*  91 */     return this.on;
/*     */   }
/*     */ 
/*     */   public void setPitch(float paramFloat) {
/*  95 */     this.pitch = paramFloat;
/*     */   }
/*     */ 
/*     */   public float getPitch() {
/*  99 */     return this.pitch;
/*     */   }
/*     */ 
/*     */   public void setSampleRate(float paramFloat) {
/* 103 */     this.samplerate = paramFloat;
/*     */   }
/*     */ 
/*     */   public float getSampleRate() {
/* 107 */     return this.samplerate;
/*     */   }
/*     */ 
/*     */   public float getAttenuation() {
/* 111 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public int getChannels() {
/* 115 */     return 1;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 119 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   public Patch getPatch() {
/* 123 */     return new Patch(0, 0);
/*     */   }
/*     */ 
/*     */   public ModelOscillatorStream open(float paramFloat) {
/*     */     ModelAbstractOscillator localModelAbstractOscillator;
/*     */     try {
/* 129 */       localModelAbstractOscillator = (ModelAbstractOscillator)getClass().newInstance();
/*     */     } catch (InstantiationException localInstantiationException) {
/* 131 */       throw new IllegalArgumentException(localInstantiationException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 133 */       throw new IllegalArgumentException(localIllegalAccessException);
/*     */     }
/* 135 */     localModelAbstractOscillator.setSampleRate(paramFloat);
/* 136 */     localModelAbstractOscillator.init();
/* 137 */     return localModelAbstractOscillator;
/*     */   }
/*     */ 
/*     */   public ModelPerformer getPerformer()
/*     */   {
/* 142 */     ModelPerformer localModelPerformer = new ModelPerformer();
/* 143 */     localModelPerformer.getOscillators().add(this);
/* 144 */     return localModelPerformer;
/*     */   }
/*     */ 
/*     */   public ModelInstrument getInstrument()
/*     */   {
/* 150 */     SimpleInstrument localSimpleInstrument = new SimpleInstrument();
/* 151 */     localSimpleInstrument.setName(getName());
/* 152 */     localSimpleInstrument.add(getPerformer());
/* 153 */     localSimpleInstrument.setPatch(getPatch());
/* 154 */     return localSimpleInstrument;
/*     */   }
/*     */ 
/*     */   public Soundbank getSoundBank()
/*     */   {
/* 160 */     SimpleSoundbank localSimpleSoundbank = new SimpleSoundbank();
/* 161 */     localSimpleSoundbank.addInstrument(getInstrument());
/* 162 */     return localSimpleSoundbank;
/*     */   }
/*     */ 
/*     */   public String getDescription() {
/* 166 */     return getName();
/*     */   }
/*     */ 
/*     */   public Instrument getInstrument(Patch paramPatch) {
/* 170 */     ModelInstrument localModelInstrument = getInstrument();
/* 171 */     Patch localPatch = localModelInstrument.getPatch();
/* 172 */     if (localPatch.getBank() != paramPatch.getBank())
/* 173 */       return null;
/* 174 */     if (localPatch.getProgram() != paramPatch.getProgram())
/* 175 */       return null;
/* 176 */     if (((localPatch instanceof ModelPatch)) && ((paramPatch instanceof ModelPatch)) && 
/* 177 */       (((ModelPatch)localPatch).isPercussion() != ((ModelPatch)paramPatch).isPercussion()))
/*     */     {
/* 179 */       return null;
/*     */     }
/*     */ 
/* 182 */     return localModelInstrument;
/*     */   }
/*     */ 
/*     */   public Instrument[] getInstruments() {
/* 186 */     return new Instrument[] { getInstrument() };
/*     */   }
/*     */ 
/*     */   public SoundbankResource[] getResources() {
/* 190 */     return new SoundbankResource[0];
/*     */   }
/*     */ 
/*     */   public String getVendor() {
/* 194 */     return null;
/*     */   }
/*     */ 
/*     */   public String getVersion() {
/* 198 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelAbstractOscillator
 * JD-Core Version:    0.6.2
 */