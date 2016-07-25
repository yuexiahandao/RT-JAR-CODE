/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.sound.midi.Patch;
/*     */ 
/*     */ public final class DLSInstrument extends ModelInstrument
/*     */ {
/*  45 */   int preset = 0;
/*  46 */   int bank = 0;
/*  47 */   boolean druminstrument = false;
/*  48 */   byte[] guid = null;
/*  49 */   DLSInfo info = new DLSInfo();
/*  50 */   List<DLSRegion> regions = new ArrayList();
/*  51 */   List<DLSModulator> modulators = new ArrayList();
/*     */ 
/*     */   public DLSInstrument() {
/*  54 */     super(null, null, null, null);
/*     */   }
/*     */ 
/*     */   public DLSInstrument(DLSSoundbank paramDLSSoundbank) {
/*  58 */     super(paramDLSSoundbank, null, null, null);
/*     */   }
/*     */ 
/*     */   public DLSInfo getInfo() {
/*  62 */     return this.info;
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  66 */     return this.info.name;
/*     */   }
/*     */ 
/*     */   public void setName(String paramString) {
/*  70 */     this.info.name = paramString;
/*     */   }
/*     */ 
/*     */   public ModelPatch getPatch() {
/*  74 */     return new ModelPatch(this.bank, this.preset, this.druminstrument);
/*     */   }
/*     */ 
/*     */   public void setPatch(Patch paramPatch) {
/*  78 */     if (((paramPatch instanceof ModelPatch)) && (((ModelPatch)paramPatch).isPercussion())) {
/*  79 */       this.druminstrument = true;
/*  80 */       this.bank = paramPatch.getBank();
/*  81 */       this.preset = paramPatch.getProgram();
/*     */     } else {
/*  83 */       this.druminstrument = false;
/*  84 */       this.bank = paramPatch.getBank();
/*  85 */       this.preset = paramPatch.getProgram();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getData() {
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   public List<DLSRegion> getRegions() {
/*  94 */     return this.regions;
/*     */   }
/*     */ 
/*     */   public List<DLSModulator> getModulators() {
/*  98 */     return this.modulators;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 102 */     if (this.druminstrument) {
/* 103 */       return "Drumkit: " + this.info.name + " bank #" + this.bank + " preset #" + this.preset;
/*     */     }
/*     */ 
/* 106 */     return "Instrument: " + this.info.name + " bank #" + this.bank + " preset #" + this.preset;
/*     */   }
/*     */ 
/*     */   private ModelIdentifier convertToModelDest(int paramInt)
/*     */   {
/* 111 */     if (paramInt == 0)
/* 112 */       return null;
/* 113 */     if (paramInt == 1)
/* 114 */       return ModelDestination.DESTINATION_GAIN;
/* 115 */     if (paramInt == 3)
/* 116 */       return ModelDestination.DESTINATION_PITCH;
/* 117 */     if (paramInt == 4) {
/* 118 */       return ModelDestination.DESTINATION_PAN;
/*     */     }
/* 120 */     if (paramInt == 260)
/* 121 */       return ModelDestination.DESTINATION_LFO1_FREQ;
/* 122 */     if (paramInt == 261) {
/* 123 */       return ModelDestination.DESTINATION_LFO1_DELAY;
/*     */     }
/* 125 */     if (paramInt == 518)
/* 126 */       return ModelDestination.DESTINATION_EG1_ATTACK;
/* 127 */     if (paramInt == 519)
/* 128 */       return ModelDestination.DESTINATION_EG1_DECAY;
/* 129 */     if (paramInt == 521)
/* 130 */       return ModelDestination.DESTINATION_EG1_RELEASE;
/* 131 */     if (paramInt == 522) {
/* 132 */       return ModelDestination.DESTINATION_EG1_SUSTAIN;
/*     */     }
/* 134 */     if (paramInt == 778)
/* 135 */       return ModelDestination.DESTINATION_EG2_ATTACK;
/* 136 */     if (paramInt == 779)
/* 137 */       return ModelDestination.DESTINATION_EG2_DECAY;
/* 138 */     if (paramInt == 781)
/* 139 */       return ModelDestination.DESTINATION_EG2_RELEASE;
/* 140 */     if (paramInt == 782) {
/* 141 */       return ModelDestination.DESTINATION_EG2_SUSTAIN;
/*     */     }
/*     */ 
/* 144 */     if (paramInt == 5) {
/* 145 */       return ModelDestination.DESTINATION_KEYNUMBER;
/*     */     }
/* 147 */     if (paramInt == 128)
/* 148 */       return ModelDestination.DESTINATION_CHORUS;
/* 149 */     if (paramInt == 129) {
/* 150 */       return ModelDestination.DESTINATION_REVERB;
/*     */     }
/* 152 */     if (paramInt == 276)
/* 153 */       return ModelDestination.DESTINATION_LFO2_FREQ;
/* 154 */     if (paramInt == 277) {
/* 155 */       return ModelDestination.DESTINATION_LFO2_DELAY;
/*     */     }
/* 157 */     if (paramInt == 523)
/* 158 */       return ModelDestination.DESTINATION_EG1_DELAY;
/* 159 */     if (paramInt == 524)
/* 160 */       return ModelDestination.DESTINATION_EG1_HOLD;
/* 161 */     if (paramInt == 525) {
/* 162 */       return ModelDestination.DESTINATION_EG1_SHUTDOWN;
/*     */     }
/* 164 */     if (paramInt == 783)
/* 165 */       return ModelDestination.DESTINATION_EG2_DELAY;
/* 166 */     if (paramInt == 784) {
/* 167 */       return ModelDestination.DESTINATION_EG2_HOLD;
/*     */     }
/* 169 */     if (paramInt == 1280)
/* 170 */       return ModelDestination.DESTINATION_FILTER_FREQ;
/* 171 */     if (paramInt == 1281) {
/* 172 */       return ModelDestination.DESTINATION_FILTER_Q;
/*     */     }
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   private ModelIdentifier convertToModelSrc(int paramInt) {
/* 178 */     if (paramInt == 0) {
/* 179 */       return null;
/*     */     }
/* 181 */     if (paramInt == 1)
/* 182 */       return ModelSource.SOURCE_LFO1;
/* 183 */     if (paramInt == 2)
/* 184 */       return ModelSource.SOURCE_NOTEON_VELOCITY;
/* 185 */     if (paramInt == 3)
/* 186 */       return ModelSource.SOURCE_NOTEON_KEYNUMBER;
/* 187 */     if (paramInt == 4)
/* 188 */       return ModelSource.SOURCE_EG1;
/* 189 */     if (paramInt == 5)
/* 190 */       return ModelSource.SOURCE_EG2;
/* 191 */     if (paramInt == 6)
/* 192 */       return ModelSource.SOURCE_MIDI_PITCH;
/* 193 */     if (paramInt == 129)
/* 194 */       return new ModelIdentifier("midi_cc", "1", 0);
/* 195 */     if (paramInt == 135)
/* 196 */       return new ModelIdentifier("midi_cc", "7", 0);
/* 197 */     if (paramInt == 138)
/* 198 */       return new ModelIdentifier("midi_cc", "10", 0);
/* 199 */     if (paramInt == 139)
/* 200 */       return new ModelIdentifier("midi_cc", "11", 0);
/* 201 */     if (paramInt == 256)
/* 202 */       return new ModelIdentifier("midi_rpn", "0", 0);
/* 203 */     if (paramInt == 257) {
/* 204 */       return new ModelIdentifier("midi_rpn", "1", 0);
/*     */     }
/* 206 */     if (paramInt == 7)
/* 207 */       return ModelSource.SOURCE_MIDI_POLY_PRESSURE;
/* 208 */     if (paramInt == 8)
/* 209 */       return ModelSource.SOURCE_MIDI_CHANNEL_PRESSURE;
/* 210 */     if (paramInt == 9)
/* 211 */       return ModelSource.SOURCE_LFO2;
/* 212 */     if (paramInt == 10) {
/* 213 */       return ModelSource.SOURCE_MIDI_CHANNEL_PRESSURE;
/*     */     }
/* 215 */     if (paramInt == 219)
/* 216 */       return new ModelIdentifier("midi_cc", "91", 0);
/* 217 */     if (paramInt == 221) {
/* 218 */       return new ModelIdentifier("midi_cc", "93", 0);
/*     */     }
/* 220 */     return null;
/*     */   }
/*     */ 
/*     */   private ModelConnectionBlock convertToModel(DLSModulator paramDLSModulator) {
/* 224 */     ModelIdentifier localModelIdentifier1 = convertToModelSrc(paramDLSModulator.getSource());
/* 225 */     ModelIdentifier localModelIdentifier2 = convertToModelSrc(paramDLSModulator.getControl());
/* 226 */     ModelIdentifier localModelIdentifier3 = convertToModelDest(paramDLSModulator.getDestination());
/*     */ 
/* 229 */     int i = paramDLSModulator.getScale();
/*     */     double d;
/* 231 */     if (i == -2147483648)
/* 232 */       d = (-1.0D / 0.0D);
/*     */     else {
/* 234 */       d = i / 65536.0D;
/*     */     }
/* 236 */     if (localModelIdentifier3 != null) {
/* 237 */       Object localObject1 = null;
/* 238 */       Object localObject2 = null;
/* 239 */       ModelConnectionBlock localModelConnectionBlock = new ModelConnectionBlock();
/* 240 */       if (localModelIdentifier2 != null) {
/* 241 */         localObject3 = new ModelSource();
/* 242 */         if (localModelIdentifier2 == ModelSource.SOURCE_MIDI_PITCH) {
/* 243 */           ((ModelStandardTransform)((ModelSource)localObject3).getTransform()).setPolarity(true);
/*     */         }
/* 245 */         else if ((localModelIdentifier2 == ModelSource.SOURCE_LFO1) || (localModelIdentifier2 == ModelSource.SOURCE_LFO2))
/*     */         {
/* 247 */           ((ModelStandardTransform)((ModelSource)localObject3).getTransform()).setPolarity(true);
/*     */         }
/*     */ 
/* 250 */         ((ModelSource)localObject3).setIdentifier(localModelIdentifier2);
/* 251 */         localModelConnectionBlock.addSource((ModelSource)localObject3);
/* 252 */         localObject2 = localObject3;
/*     */       }
/* 254 */       if (localModelIdentifier1 != null) {
/* 255 */         localObject3 = new ModelSource();
/* 256 */         if (localModelIdentifier1 == ModelSource.SOURCE_MIDI_PITCH) {
/* 257 */           ((ModelStandardTransform)((ModelSource)localObject3).getTransform()).setPolarity(true);
/*     */         }
/* 259 */         else if ((localModelIdentifier1 == ModelSource.SOURCE_LFO1) || (localModelIdentifier1 == ModelSource.SOURCE_LFO2))
/*     */         {
/* 261 */           ((ModelStandardTransform)((ModelSource)localObject3).getTransform()).setPolarity(true);
/*     */         }
/*     */ 
/* 264 */         ((ModelSource)localObject3).setIdentifier(localModelIdentifier1);
/* 265 */         localModelConnectionBlock.addSource((ModelSource)localObject3);
/* 266 */         localObject1 = localObject3;
/*     */       }
/* 268 */       Object localObject3 = new ModelDestination();
/* 269 */       ((ModelDestination)localObject3).setIdentifier(localModelIdentifier3);
/* 270 */       localModelConnectionBlock.setDestination((ModelDestination)localObject3);
/*     */ 
/* 272 */       if (paramDLSModulator.getVersion() == 1)
/*     */       {
/* 278 */         if (paramDLSModulator.getTransform() == 1) {
/* 279 */           if (localObject1 != null) {
/* 280 */             ((ModelStandardTransform)localObject1.getTransform()).setTransform(1);
/*     */ 
/* 283 */             ((ModelStandardTransform)localObject1.getTransform()).setDirection(true);
/*     */           }
/*     */ 
/* 287 */           if (localObject2 != null) {
/* 288 */             ((ModelStandardTransform)localObject2.getTransform()).setTransform(1);
/*     */ 
/* 291 */             ((ModelStandardTransform)localObject2.getTransform()).setDirection(true);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/* 297 */       else if (paramDLSModulator.getVersion() == 2) {
/* 298 */         int j = paramDLSModulator.getTransform();
/* 299 */         int k = j >> 15 & 0x1;
/* 300 */         int m = j >> 14 & 0x1;
/* 301 */         int n = j >> 10 & 0x8;
/* 302 */         int i1 = j >> 9 & 0x1;
/* 303 */         int i2 = j >> 8 & 0x1;
/* 304 */         int i3 = j >> 4 & 0x8;
/*     */         int i4;
/* 307 */         if (localObject1 != null) {
/* 308 */           i4 = 0;
/* 309 */           if (n == 3)
/* 310 */             i4 = 3;
/* 311 */           if (n == 1)
/* 312 */             i4 = 1;
/* 313 */           if (n == 2)
/* 314 */             i4 = 2;
/* 315 */           ((ModelStandardTransform)localObject1.getTransform()).setTransform(i4);
/*     */ 
/* 317 */           ((ModelStandardTransform)localObject1.getTransform()).setPolarity(m == 1);
/*     */ 
/* 319 */           ((ModelStandardTransform)localObject1.getTransform()).setDirection(k == 1);
/*     */         }
/*     */ 
/* 324 */         if (localObject2 != null) {
/* 325 */           i4 = 0;
/* 326 */           if (i3 == 3)
/* 327 */             i4 = 3;
/* 328 */           if (i3 == 1)
/* 329 */             i4 = 1;
/* 330 */           if (i3 == 2)
/* 331 */             i4 = 2;
/* 332 */           ((ModelStandardTransform)localObject2.getTransform()).setTransform(i4);
/*     */ 
/* 334 */           ((ModelStandardTransform)localObject2.getTransform()).setPolarity(i2 == 1);
/*     */ 
/* 336 */           ((ModelStandardTransform)localObject2.getTransform()).setDirection(i1 == 1);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 357 */       localModelConnectionBlock.setScale(d);
/*     */ 
/* 359 */       return localModelConnectionBlock;
/*     */     }
/*     */ 
/* 362 */     return null;
/*     */   }
/*     */ 
/*     */   public ModelPerformer[] getPerformers() {
/* 366 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 368 */     HashMap localHashMap = new HashMap();
/* 369 */     for (Object localObject1 = getModulators().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (DLSModulator)((Iterator)localObject1).next();
/* 370 */       localHashMap.put(((DLSModulator)localObject2).getSource() + "x" + ((DLSModulator)localObject2).getControl() + "=" + ((DLSModulator)localObject2).getDestination(), localObject2);
/*     */     }
/*     */ 
/* 374 */     localObject1 = new HashMap();
/*     */ 
/* 377 */     for (Object localObject2 = this.regions.iterator(); ((Iterator)localObject2).hasNext(); ) { DLSRegion localDLSRegion = (DLSRegion)((Iterator)localObject2).next();
/* 378 */       ModelPerformer localModelPerformer = new ModelPerformer();
/* 379 */       localModelPerformer.setName(localDLSRegion.getSample().getName());
/* 380 */       localModelPerformer.setSelfNonExclusive((localDLSRegion.getFusoptions() & 0x1) != 0);
/*     */ 
/* 382 */       localModelPerformer.setExclusiveClass(localDLSRegion.getExclusiveClass());
/* 383 */       localModelPerformer.setKeyFrom(localDLSRegion.getKeyfrom());
/* 384 */       localModelPerformer.setKeyTo(localDLSRegion.getKeyto());
/* 385 */       localModelPerformer.setVelFrom(localDLSRegion.getVelfrom());
/* 386 */       localModelPerformer.setVelTo(localDLSRegion.getVelto());
/*     */ 
/* 388 */       ((Map)localObject1).clear();
/* 389 */       ((Map)localObject1).putAll(localHashMap);
/* 390 */       for (Object localObject3 = localDLSRegion.getModulators().iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (DLSModulator)((Iterator)localObject3).next();
/* 391 */         ((Map)localObject1).put(((DLSModulator)localObject4).getSource() + "x" + ((DLSModulator)localObject4).getControl() + "=" + ((DLSModulator)localObject4).getDestination(), localObject4);
/*     */       }
/*     */ 
/* 395 */       localObject3 = localModelPerformer.getConnectionBlocks();
/* 396 */       for (Object localObject4 = ((Map)localObject1).values().iterator(); ((Iterator)localObject4).hasNext(); ) { localObject5 = (DLSModulator)((Iterator)localObject4).next();
/* 397 */         localObject6 = convertToModel((DLSModulator)localObject5);
/* 398 */         if (localObject6 != null) {
/* 399 */           ((List)localObject3).add(localObject6);
/*     */         }
/*     */       }
/*     */ 
/* 403 */       localObject4 = localDLSRegion.getSample();
/* 404 */       Object localObject5 = localDLSRegion.getSampleoptions();
/* 405 */       if (localObject5 == null) {
/* 406 */         localObject5 = ((DLSSample)localObject4).getSampleoptions();
/*     */       }
/* 408 */       Object localObject6 = ((DLSSample)localObject4).getDataBuffer();
/*     */ 
/* 410 */       float f = -((DLSSampleOptions)localObject5).unitynote * 100 + ((DLSSampleOptions)localObject5).finetune;
/*     */ 
/* 413 */       ModelByteBufferWavetable localModelByteBufferWavetable = new ModelByteBufferWavetable((ModelByteBuffer)localObject6, ((DLSSample)localObject4).getFormat(), f);
/*     */ 
/* 415 */       localModelByteBufferWavetable.setAttenuation(localModelByteBufferWavetable.getAttenuation() / 65536.0F);
/* 416 */       if (((DLSSampleOptions)localObject5).getLoops().size() != 0) {
/* 417 */         DLSSampleLoop localDLSSampleLoop = (DLSSampleLoop)((DLSSampleOptions)localObject5).getLoops().get(0);
/* 418 */         localModelByteBufferWavetable.setLoopStart((int)localDLSSampleLoop.getStart());
/* 419 */         localModelByteBufferWavetable.setLoopLength((int)localDLSSampleLoop.getLength());
/* 420 */         if (localDLSSampleLoop.getType() == 0L)
/* 421 */           localModelByteBufferWavetable.setLoopType(1);
/* 422 */         if (localDLSSampleLoop.getType() == 1L)
/* 423 */           localModelByteBufferWavetable.setLoopType(2);
/*     */         else {
/* 425 */           localModelByteBufferWavetable.setLoopType(1);
/*     */         }
/*     */       }
/* 428 */       localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(1.0D, new ModelDestination(new ModelIdentifier("filter", "type", 1))));
/*     */ 
/* 433 */       localModelPerformer.getOscillators().add(localModelByteBufferWavetable);
/*     */ 
/* 435 */       localArrayList.add(localModelPerformer);
/*     */     }
/*     */ 
/* 439 */     return (ModelPerformer[])localArrayList.toArray(new ModelPerformer[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   public byte[] getGuid() {
/* 443 */     return this.guid == null ? null : Arrays.copyOf(this.guid, this.guid.length);
/*     */   }
/*     */ 
/*     */   public void setGuid(byte[] paramArrayOfByte) {
/* 447 */     this.guid = (paramArrayOfByte == null ? null : Arrays.copyOf(paramArrayOfByte, paramArrayOfByte.length));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.DLSInstrument
 * JD-Core Version:    0.6.2
 */