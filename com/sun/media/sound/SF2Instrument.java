/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.sound.midi.Patch;
/*     */ 
/*     */ public final class SF2Instrument extends ModelInstrument
/*     */ {
/*  41 */   String name = "";
/*  42 */   int preset = 0;
/*  43 */   int bank = 0;
/*  44 */   long library = 0L;
/*  45 */   long genre = 0L;
/*  46 */   long morphology = 0L;
/*  47 */   SF2GlobalRegion globalregion = null;
/*  48 */   List<SF2InstrumentRegion> regions = new ArrayList();
/*     */ 
/*     */   public SF2Instrument()
/*     */   {
/*  52 */     super(null, null, null, null);
/*     */   }
/*     */ 
/*     */   public SF2Instrument(SF2Soundbank paramSF2Soundbank) {
/*  56 */     super(paramSF2Soundbank, null, null, null);
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  60 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String paramString) {
/*  64 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public Patch getPatch() {
/*  68 */     if (this.bank == 128) {
/*  69 */       return new ModelPatch(0, this.preset, true);
/*     */     }
/*  71 */     return new ModelPatch(this.bank << 7, this.preset, false);
/*     */   }
/*     */ 
/*     */   public void setPatch(Patch paramPatch) {
/*  75 */     if (((paramPatch instanceof ModelPatch)) && (((ModelPatch)paramPatch).isPercussion())) {
/*  76 */       this.bank = 128;
/*  77 */       this.preset = paramPatch.getProgram();
/*     */     } else {
/*  79 */       this.bank = (paramPatch.getBank() >> 7);
/*  80 */       this.preset = paramPatch.getProgram();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getData() {
/*  85 */     return null;
/*     */   }
/*     */ 
/*     */   public long getGenre() {
/*  89 */     return this.genre;
/*     */   }
/*     */ 
/*     */   public void setGenre(long paramLong) {
/*  93 */     this.genre = paramLong;
/*     */   }
/*     */ 
/*     */   public long getLibrary() {
/*  97 */     return this.library;
/*     */   }
/*     */ 
/*     */   public void setLibrary(long paramLong) {
/* 101 */     this.library = paramLong;
/*     */   }
/*     */ 
/*     */   public long getMorphology() {
/* 105 */     return this.morphology;
/*     */   }
/*     */ 
/*     */   public void setMorphology(long paramLong) {
/* 109 */     this.morphology = paramLong;
/*     */   }
/*     */ 
/*     */   public List<SF2InstrumentRegion> getRegions() {
/* 113 */     return this.regions;
/*     */   }
/*     */ 
/*     */   public SF2GlobalRegion getGlobalRegion() {
/* 117 */     return this.globalregion;
/*     */   }
/*     */ 
/*     */   public void setGlobalZone(SF2GlobalRegion paramSF2GlobalRegion) {
/* 121 */     this.globalregion = paramSF2GlobalRegion;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 125 */     if (this.bank == 128) {
/* 126 */       return "Drumkit: " + this.name + " preset #" + this.preset;
/*     */     }
/* 128 */     return "Instrument: " + this.name + " bank #" + this.bank + " preset #" + this.preset;
/*     */   }
/*     */ 
/*     */   public ModelPerformer[] getPerformers()
/*     */   {
/* 133 */     int i = 0;
/* 134 */     for (Object localObject1 = this.regions.iterator(); ((Iterator)localObject1).hasNext(); ) { SF2InstrumentRegion localSF2InstrumentRegion1 = (SF2InstrumentRegion)((Iterator)localObject1).next();
/* 135 */       i += localSF2InstrumentRegion1.getLayer().getRegions().size(); }
/* 136 */     localObject1 = new ModelPerformer[i];
/* 137 */     int j = 0;
/*     */ 
/* 139 */     SF2GlobalRegion localSF2GlobalRegion1 = this.globalregion;
/* 140 */     for (Iterator localIterator1 = this.regions.iterator(); localIterator1.hasNext(); ) { localSF2InstrumentRegion2 = (SF2InstrumentRegion)localIterator1.next();
/* 141 */       localHashMap1 = new HashMap();
/* 142 */       localHashMap1.putAll(localSF2InstrumentRegion2.getGenerators());
/* 143 */       if (localSF2GlobalRegion1 != null) {
/* 144 */         localHashMap1.putAll(localSF2GlobalRegion1.getGenerators());
/*     */       }
/* 146 */       localSF2Layer = localSF2InstrumentRegion2.getLayer();
/* 147 */       localSF2GlobalRegion2 = localSF2Layer.getGlobalRegion();
/* 148 */       for (SF2LayerRegion localSF2LayerRegion : localSF2Layer.getRegions()) {
/* 149 */         localModelPerformer = new ModelPerformer();
/* 150 */         if (localSF2LayerRegion.getSample() != null)
/* 151 */           localModelPerformer.setName(localSF2LayerRegion.getSample().getName());
/*     */         else {
/* 153 */           localModelPerformer.setName(localSF2Layer.getName());
/*     */         }
/* 155 */         localObject1[(j++)] = localModelPerformer;
/*     */ 
/* 157 */         int k = 0;
/* 158 */         int m = 127;
/* 159 */         int n = 0;
/* 160 */         int i1 = 127;
/*     */ 
/* 162 */         if (localSF2LayerRegion.contains(57))
/* 163 */           localModelPerformer.setExclusiveClass(localSF2LayerRegion.getInteger(57));
/*     */         byte[] arrayOfByte;
/* 166 */         if (localSF2LayerRegion.contains(43)) {
/* 167 */           arrayOfByte = localSF2LayerRegion.getBytes(43);
/*     */ 
/* 169 */           if ((arrayOfByte[0] >= 0) && 
/* 170 */             (arrayOfByte[0] > k))
/* 171 */             k = arrayOfByte[0];
/* 172 */           if ((arrayOfByte[1] >= 0) && 
/* 173 */             (arrayOfByte[1] < m))
/* 174 */             m = arrayOfByte[1];
/*     */         }
/* 176 */         if (localSF2LayerRegion.contains(44)) {
/* 177 */           arrayOfByte = localSF2LayerRegion.getBytes(44);
/*     */ 
/* 179 */           if ((arrayOfByte[0] >= 0) && 
/* 180 */             (arrayOfByte[0] > n))
/* 181 */             n = arrayOfByte[0];
/* 182 */           if ((arrayOfByte[1] >= 0) && 
/* 183 */             (arrayOfByte[1] < i1))
/* 184 */             i1 = arrayOfByte[1];
/*     */         }
/* 186 */         if (localSF2InstrumentRegion2.contains(43)) {
/* 187 */           arrayOfByte = localSF2InstrumentRegion2.getBytes(43);
/*     */ 
/* 189 */           if (arrayOfByte[0] > k)
/* 190 */             k = arrayOfByte[0];
/* 191 */           if (arrayOfByte[1] < m)
/* 192 */             m = arrayOfByte[1];
/*     */         }
/* 194 */         if (localSF2InstrumentRegion2.contains(44)) {
/* 195 */           arrayOfByte = localSF2InstrumentRegion2.getBytes(44);
/*     */ 
/* 197 */           if (arrayOfByte[0] > n)
/* 198 */             n = arrayOfByte[0];
/* 199 */           if (arrayOfByte[1] < i1)
/* 200 */             i1 = arrayOfByte[1];
/*     */         }
/* 202 */         localModelPerformer.setKeyFrom(k);
/* 203 */         localModelPerformer.setKeyTo(m);
/* 204 */         localModelPerformer.setVelFrom(n);
/* 205 */         localModelPerformer.setVelTo(i1);
/*     */ 
/* 207 */         int i2 = localSF2LayerRegion.getShort(0);
/*     */ 
/* 209 */         int i3 = localSF2LayerRegion.getShort(1);
/*     */ 
/* 211 */         int i4 = localSF2LayerRegion.getShort(2);
/*     */ 
/* 213 */         int i5 = localSF2LayerRegion.getShort(3);
/*     */ 
/* 216 */         i2 += localSF2LayerRegion.getShort(4) * 32768;
/*     */ 
/* 218 */         i3 += localSF2LayerRegion.getShort(12) * 32768;
/*     */ 
/* 220 */         i4 += localSF2LayerRegion.getShort(45) * 32768;
/*     */ 
/* 222 */         i5 += localSF2LayerRegion.getShort(50) * 32768;
/*     */ 
/* 224 */         i4 -= i2;
/* 225 */         i5 -= i2;
/*     */ 
/* 227 */         SF2Sample localSF2Sample = localSF2LayerRegion.getSample();
/* 228 */         int i6 = localSF2Sample.originalPitch;
/* 229 */         if (localSF2LayerRegion.getShort(58) != -1) {
/* 230 */           i6 = localSF2LayerRegion.getShort(58);
/*     */         }
/*     */ 
/* 233 */         float f1 = -i6 * 100 + localSF2Sample.pitchCorrection;
/* 234 */         ModelByteBuffer localModelByteBuffer1 = localSF2Sample.getDataBuffer();
/* 235 */         ModelByteBuffer localModelByteBuffer2 = localSF2Sample.getData24Buffer();
/*     */ 
/* 237 */         if ((i2 != 0) || (i3 != 0)) {
/* 238 */           localModelByteBuffer1 = localModelByteBuffer1.subbuffer(i2 * 2, localModelByteBuffer1.capacity() + i3 * 2);
/*     */ 
/* 240 */           if (localModelByteBuffer2 != null) {
/* 241 */             localModelByteBuffer2 = localModelByteBuffer2.subbuffer(i2, localModelByteBuffer2.capacity() + i3);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 265 */         ModelByteBufferWavetable localModelByteBufferWavetable = new ModelByteBufferWavetable(localModelByteBuffer1, localSF2Sample.getFormat(), f1);
/*     */ 
/* 267 */         if (localModelByteBuffer2 != null) {
/* 268 */           localModelByteBufferWavetable.set8BitExtensionBuffer(localModelByteBuffer2);
/*     */         }
/* 270 */         HashMap localHashMap2 = new HashMap();
/* 271 */         if (localSF2GlobalRegion2 != null)
/* 272 */           localHashMap2.putAll(localSF2GlobalRegion2.getGenerators());
/* 273 */         localHashMap2.putAll(localSF2LayerRegion.getGenerators());
/* 274 */         for (Map.Entry localEntry : localHashMap1.entrySet())
/*     */         {
/* 276 */           if (!localHashMap2.containsKey(localEntry.getKey()))
/* 277 */             s2 = localSF2LayerRegion.getShort(((Integer)localEntry.getKey()).intValue());
/*     */           else
/* 279 */             s2 = ((Short)localHashMap2.get(localEntry.getKey())).shortValue();
/* 280 */           s2 = (short)(s2 + ((Short)localEntry.getValue()).shortValue());
/* 281 */           localHashMap2.put(localEntry.getKey(), Short.valueOf(s2));
/*     */         }
/*     */ 
/* 290 */         int i7 = getGeneratorValue(localHashMap2, 54);
/*     */ 
/* 292 */         if (((i7 == 1) || (i7 == 3)) && 
/* 293 */           (localSF2Sample.startLoop >= 0L) && (localSF2Sample.endLoop > 0L)) {
/* 294 */           localModelByteBufferWavetable.setLoopStart((int)(localSF2Sample.startLoop + i4));
/*     */ 
/* 296 */           localModelByteBufferWavetable.setLoopLength((int)(localSF2Sample.endLoop - localSF2Sample.startLoop + i5 - i4));
/*     */ 
/* 298 */           if (i7 == 1)
/* 299 */             localModelByteBufferWavetable.setLoopType(1);
/* 300 */           if (i7 == 3) {
/* 301 */             localModelByteBufferWavetable.setLoopType(2);
/*     */           }
/*     */         }
/* 304 */         localModelPerformer.getOscillators().add(localModelByteBufferWavetable);
/*     */ 
/* 307 */         short s1 = getGeneratorValue(localHashMap2, 33);
/*     */ 
/* 309 */         short s2 = getGeneratorValue(localHashMap2, 34);
/*     */ 
/* 311 */         int i8 = getGeneratorValue(localHashMap2, 35);
/*     */ 
/* 313 */         int i9 = getGeneratorValue(localHashMap2, 36);
/*     */ 
/* 315 */         short s3 = getGeneratorValue(localHashMap2, 37);
/*     */ 
/* 317 */         short s4 = getGeneratorValue(localHashMap2, 38);
/*     */         float f2;
/*     */         ModelIdentifier localModelIdentifier1;
/*     */         ModelIdentifier localModelIdentifier2;
/* 320 */         if (i8 != -12000) {
/* 321 */           s5 = getGeneratorValue(localHashMap2, 39);
/*     */ 
/* 323 */           i8 = (short)(i8 + 60 * s5);
/* 324 */           f2 = -s5 * 128;
/* 325 */           localModelIdentifier1 = ModelSource.SOURCE_NOTEON_KEYNUMBER;
/* 326 */           localModelIdentifier2 = ModelDestination.DESTINATION_EG1_HOLD;
/* 327 */           localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(localModelIdentifier1), f2, new ModelDestination(localModelIdentifier2)));
/*     */         }
/*     */ 
/* 331 */         if (i9 != -12000) {
/* 332 */           s5 = getGeneratorValue(localHashMap2, 40);
/*     */ 
/* 334 */           i9 = (short)(i9 + 60 * s5);
/* 335 */           f2 = -s5 * 128;
/* 336 */           localModelIdentifier1 = ModelSource.SOURCE_NOTEON_KEYNUMBER;
/* 337 */           localModelIdentifier2 = ModelDestination.DESTINATION_EG1_DECAY;
/* 338 */           localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(localModelIdentifier1), f2, new ModelDestination(localModelIdentifier2)));
/*     */         }
/*     */ 
/* 343 */         addTimecentValue(localModelPerformer, ModelDestination.DESTINATION_EG1_DELAY, s1);
/*     */ 
/* 345 */         addTimecentValue(localModelPerformer, ModelDestination.DESTINATION_EG1_ATTACK, s2);
/*     */ 
/* 347 */         addTimecentValue(localModelPerformer, ModelDestination.DESTINATION_EG1_HOLD, i8);
/*     */ 
/* 349 */         addTimecentValue(localModelPerformer, ModelDestination.DESTINATION_EG1_DECAY, i9);
/*     */ 
/* 353 */         s3 = (short)(1000 - s3);
/* 354 */         if (s3 < 0)
/* 355 */           s3 = 0;
/* 356 */         if (s3 > 1000) {
/* 357 */           s3 = 1000;
/*     */         }
/* 359 */         addValue(localModelPerformer, ModelDestination.DESTINATION_EG1_SUSTAIN, s3);
/*     */ 
/* 361 */         addTimecentValue(localModelPerformer, ModelDestination.DESTINATION_EG1_RELEASE, s4);
/*     */ 
/* 364 */         if ((getGeneratorValue(localHashMap2, 11) != 0) || (getGeneratorValue(localHashMap2, 7) != 0))
/*     */         {
/* 368 */           s5 = getGeneratorValue(localHashMap2, 25);
/*     */ 
/* 370 */           s6 = getGeneratorValue(localHashMap2, 26);
/*     */ 
/* 372 */           int i10 = getGeneratorValue(localHashMap2, 27);
/*     */ 
/* 374 */           int i12 = getGeneratorValue(localHashMap2, 28);
/*     */ 
/* 376 */           int i13 = getGeneratorValue(localHashMap2, 29);
/*     */ 
/* 378 */           short s9 = getGeneratorValue(localHashMap2, 30);
/*     */           int i14;
/*     */           float f3;
/*     */           ModelIdentifier localModelIdentifier4;
/*     */           ModelIdentifier localModelIdentifier5;
/* 382 */           if (i10 != -12000) {
/* 383 */             i14 = getGeneratorValue(localHashMap2, 31);
/*     */ 
/* 385 */             i10 = (short)(i10 + 60 * i14);
/* 386 */             f3 = -i14 * 128;
/* 387 */             localModelIdentifier4 = ModelSource.SOURCE_NOTEON_KEYNUMBER;
/* 388 */             localModelIdentifier5 = ModelDestination.DESTINATION_EG2_HOLD;
/* 389 */             localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(localModelIdentifier4), f3, new ModelDestination(localModelIdentifier5)));
/*     */           }
/*     */ 
/* 393 */           if (i12 != -12000) {
/* 394 */             i14 = getGeneratorValue(localHashMap2, 32);
/*     */ 
/* 396 */             i12 = (short)(i12 + 60 * i14);
/* 397 */             f3 = -i14 * 128;
/* 398 */             localModelIdentifier4 = ModelSource.SOURCE_NOTEON_KEYNUMBER;
/* 399 */             localModelIdentifier5 = ModelDestination.DESTINATION_EG2_DECAY;
/* 400 */             localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(localModelIdentifier4), f3, new ModelDestination(localModelIdentifier5)));
/*     */           }
/*     */ 
/* 405 */           addTimecentValue(localModelPerformer, ModelDestination.DESTINATION_EG2_DELAY, s5);
/*     */ 
/* 407 */           addTimecentValue(localModelPerformer, ModelDestination.DESTINATION_EG2_ATTACK, s6);
/*     */ 
/* 409 */           addTimecentValue(localModelPerformer, ModelDestination.DESTINATION_EG2_HOLD, i10);
/*     */ 
/* 411 */           addTimecentValue(localModelPerformer, ModelDestination.DESTINATION_EG2_DECAY, i12);
/*     */ 
/* 413 */           if (i13 < 0)
/* 414 */             i13 = 0;
/* 415 */           if (i13 > 1000)
/* 416 */             i13 = 1000;
/* 417 */           addValue(localModelPerformer, ModelDestination.DESTINATION_EG2_SUSTAIN, 1000 - i13);
/*     */ 
/* 419 */           addTimecentValue(localModelPerformer, ModelDestination.DESTINATION_EG2_RELEASE, s9);
/*     */           double d2;
/* 422 */           if (getGeneratorValue(localHashMap2, 11) != 0)
/*     */           {
/* 424 */             d2 = getGeneratorValue(localHashMap2, 11);
/*     */ 
/* 426 */             localModelIdentifier4 = ModelSource.SOURCE_EG2;
/* 427 */             localModelIdentifier5 = ModelDestination.DESTINATION_FILTER_FREQ;
/*     */ 
/* 429 */             localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(localModelIdentifier4), d2, new ModelDestination(localModelIdentifier5)));
/*     */           }
/*     */ 
/* 434 */           if (getGeneratorValue(localHashMap2, 7) != 0)
/*     */           {
/* 436 */             d2 = getGeneratorValue(localHashMap2, 7);
/*     */ 
/* 438 */             localModelIdentifier4 = ModelSource.SOURCE_EG2;
/* 439 */             localModelIdentifier5 = ModelDestination.DESTINATION_PITCH;
/* 440 */             localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(localModelIdentifier4), d2, new ModelDestination(localModelIdentifier5)));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 447 */         if ((getGeneratorValue(localHashMap2, 10) != 0) || (getGeneratorValue(localHashMap2, 5) != 0) || (getGeneratorValue(localHashMap2, 13) != 0))
/*     */         {
/* 453 */           s5 = getGeneratorValue(localHashMap2, 22);
/*     */ 
/* 455 */           s6 = getGeneratorValue(localHashMap2, 21);
/*     */ 
/* 457 */           addTimecentValue(localModelPerformer, ModelDestination.DESTINATION_LFO1_DELAY, s6);
/*     */ 
/* 459 */           addValue(localModelPerformer, ModelDestination.DESTINATION_LFO1_FREQ, s5);
/*     */         }
/*     */ 
/* 463 */         short s5 = getGeneratorValue(localHashMap2, 24);
/*     */ 
/* 465 */         short s6 = getGeneratorValue(localHashMap2, 23);
/*     */ 
/* 467 */         addTimecentValue(localModelPerformer, ModelDestination.DESTINATION_LFO2_DELAY, s6);
/*     */ 
/* 469 */         addValue(localModelPerformer, ModelDestination.DESTINATION_LFO2_FREQ, s5);
/*     */         double d1;
/*     */         ModelIdentifier localModelIdentifier3;
/* 473 */         if (getGeneratorValue(localHashMap2, 6) != 0)
/*     */         {
/* 475 */           d1 = getGeneratorValue(localHashMap2, 6);
/*     */ 
/* 477 */           localObject2 = ModelSource.SOURCE_LFO2;
/* 478 */           localModelIdentifier3 = ModelDestination.DESTINATION_PITCH;
/* 479 */           localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource((ModelIdentifier)localObject2, false, true), d1, new ModelDestination(localModelIdentifier3)));
/*     */         }
/*     */ 
/* 487 */         if (getGeneratorValue(localHashMap2, 10) != 0)
/*     */         {
/* 489 */           d1 = getGeneratorValue(localHashMap2, 10);
/*     */ 
/* 491 */           localObject2 = ModelSource.SOURCE_LFO1;
/* 492 */           localModelIdentifier3 = ModelDestination.DESTINATION_FILTER_FREQ;
/* 493 */           localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource((ModelIdentifier)localObject2, false, true), d1, new ModelDestination(localModelIdentifier3)));
/*     */         }
/*     */ 
/* 501 */         if (getGeneratorValue(localHashMap2, 5) != 0)
/*     */         {
/* 503 */           d1 = getGeneratorValue(localHashMap2, 5);
/*     */ 
/* 505 */           localObject2 = ModelSource.SOURCE_LFO1;
/* 506 */           localModelIdentifier3 = ModelDestination.DESTINATION_PITCH;
/* 507 */           localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource((ModelIdentifier)localObject2, false, true), d1, new ModelDestination(localModelIdentifier3)));
/*     */         }
/*     */ 
/* 515 */         if (getGeneratorValue(localHashMap2, 13) != 0)
/*     */         {
/* 517 */           d1 = getGeneratorValue(localHashMap2, 13);
/*     */ 
/* 519 */           localObject2 = ModelSource.SOURCE_LFO1;
/* 520 */           localModelIdentifier3 = ModelDestination.DESTINATION_GAIN;
/* 521 */           localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource((ModelIdentifier)localObject2, false, true), d1, new ModelDestination(localModelIdentifier3)));
/*     */         }
/*     */ 
/* 529 */         if (localSF2LayerRegion.getShort(46) != -1) {
/* 530 */           d1 = localSF2LayerRegion.getShort(46) / 128.0D;
/* 531 */           addValue(localModelPerformer, ModelDestination.DESTINATION_KEYNUMBER, d1);
/*     */         }
/*     */ 
/* 534 */         if (localSF2LayerRegion.getShort(47) != -1) {
/* 535 */           d1 = localSF2LayerRegion.getShort(47) / 128.0D;
/*     */ 
/* 537 */           addValue(localModelPerformer, ModelDestination.DESTINATION_VELOCITY, d1);
/*     */         }
/*     */         short s8;
/* 540 */         if (getGeneratorValue(localHashMap2, 8) < 13500)
/*     */         {
/* 542 */           short s7 = getGeneratorValue(localHashMap2, 8);
/*     */ 
/* 544 */           s8 = getGeneratorValue(localHashMap2, 9);
/*     */ 
/* 546 */           addValue(localModelPerformer, ModelDestination.DESTINATION_FILTER_FREQ, s7);
/*     */ 
/* 548 */           addValue(localModelPerformer, ModelDestination.DESTINATION_FILTER_Q, s8);
/*     */         }
/*     */ 
/* 552 */         int i11 = 100 * getGeneratorValue(localHashMap2, 51);
/*     */ 
/* 554 */         i11 += getGeneratorValue(localHashMap2, 52);
/*     */ 
/* 556 */         if (i11 != 0) {
/* 557 */           addValue(localModelPerformer, ModelDestination.DESTINATION_PITCH, (short)i11);
/*     */         }
/*     */ 
/* 560 */         if (getGeneratorValue(localHashMap2, 17) != 0) {
/* 561 */           s8 = getGeneratorValue(localHashMap2, 17);
/*     */ 
/* 563 */           addValue(localModelPerformer, ModelDestination.DESTINATION_PAN, s8);
/*     */         }
/* 565 */         if (getGeneratorValue(localHashMap2, 48) != 0) {
/* 566 */           s8 = getGeneratorValue(localHashMap2, 48);
/*     */ 
/* 568 */           addValue(localModelPerformer, ModelDestination.DESTINATION_GAIN, -0.376287F * s8);
/*     */         }
/*     */ 
/* 571 */         if (getGeneratorValue(localHashMap2, 15) != 0)
/*     */         {
/* 573 */           s8 = getGeneratorValue(localHashMap2, 15);
/*     */ 
/* 575 */           addValue(localModelPerformer, ModelDestination.DESTINATION_CHORUS, s8);
/*     */         }
/* 577 */         if (getGeneratorValue(localHashMap2, 16) != 0)
/*     */         {
/* 579 */           s8 = getGeneratorValue(localHashMap2, 16);
/*     */ 
/* 581 */           addValue(localModelPerformer, ModelDestination.DESTINATION_REVERB, s8);
/*     */         }
/* 583 */         if (getGeneratorValue(localHashMap2, 56) != 100)
/*     */         {
/* 585 */           s8 = getGeneratorValue(localHashMap2, 56);
/*     */ 
/* 587 */           if (s8 == 0) {
/* 588 */             localObject2 = ModelDestination.DESTINATION_PITCH;
/* 589 */             localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(null, i6 * 100, new ModelDestination((ModelIdentifier)localObject2)));
/*     */           }
/*     */           else
/*     */           {
/* 593 */             localObject2 = ModelDestination.DESTINATION_PITCH;
/* 594 */             localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(null, i6 * (100 - s8), new ModelDestination((ModelIdentifier)localObject2)));
/*     */           }
/*     */ 
/* 599 */           localObject2 = ModelSource.SOURCE_NOTEON_KEYNUMBER;
/* 600 */           localModelIdentifier3 = ModelDestination.DESTINATION_PITCH;
/* 601 */           localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource((ModelIdentifier)localObject2), 128 * s8, new ModelDestination(localModelIdentifier3)));
/*     */         }
/*     */ 
/* 607 */         localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_NOTEON_VELOCITY, new ModelTransform()
/*     */         {
/*     */           public double transform(double paramAnonymousDouble)
/*     */           {
/* 612 */             if (paramAnonymousDouble < 0.5D) {
/* 613 */               return 1.0D - paramAnonymousDouble * 2.0D;
/*     */             }
/* 615 */             return 0.0D;
/*     */           }
/*     */         }), -2400.0D, new ModelDestination(ModelDestination.DESTINATION_FILTER_FREQ)));
/*     */ 
/* 623 */         localModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_LFO2, false, true, 0), new ModelSource(new ModelIdentifier("midi_cc", "1", 0), false, false, 0), 50.0D, new ModelDestination(ModelDestination.DESTINATION_PITCH)));
/*     */ 
/* 636 */         if (localSF2Layer.getGlobalRegion() != null)
/*     */         {
/* 638 */           for (localIterator4 = localSF2Layer.getGlobalRegion().getModulators().iterator(); localIterator4.hasNext(); ) { localObject2 = (SF2Modulator)localIterator4.next();
/* 639 */             convertModulator(localModelPerformer, (SF2Modulator)localObject2);
/*     */           }
/*     */         }
/* 642 */         for (localIterator4 = localSF2LayerRegion.getModulators().iterator(); localIterator4.hasNext(); ) { localObject2 = (SF2Modulator)localIterator4.next();
/* 643 */           convertModulator(localModelPerformer, (SF2Modulator)localObject2);
/*     */         }
/* 645 */         if (localSF2GlobalRegion1 != null)
/* 646 */           for (localIterator4 = localSF2GlobalRegion1.getModulators().iterator(); localIterator4.hasNext(); ) { localObject2 = (SF2Modulator)localIterator4.next();
/* 647 */             convertModulator(localModelPerformer, (SF2Modulator)localObject2);
/*     */           }
/* 649 */         for (localIterator4 = localSF2InstrumentRegion2.getModulators().iterator(); localIterator4.hasNext(); ) { localObject2 = (SF2Modulator)localIterator4.next();
/* 650 */           convertModulator(localModelPerformer, (SF2Modulator)localObject2);
/*     */         }
/*     */       }
/*     */     }
/*     */     SF2InstrumentRegion localSF2InstrumentRegion2;
/*     */     HashMap localHashMap1;
/*     */     SF2Layer localSF2Layer;
/*     */     SF2GlobalRegion localSF2GlobalRegion2;
/*     */     ModelPerformer localModelPerformer;
/*     */     Object localObject2;
/*     */     Iterator localIterator4;
/* 654 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private void convertModulator(ModelPerformer paramModelPerformer, SF2Modulator paramSF2Modulator)
/*     */   {
/* 659 */     ModelSource localModelSource1 = convertSource(paramSF2Modulator.getSourceOperator());
/* 660 */     ModelSource localModelSource2 = convertSource(paramSF2Modulator.getAmountSourceOperator());
/* 661 */     if ((localModelSource1 == null) && (paramSF2Modulator.getSourceOperator() != 0))
/* 662 */       return;
/* 663 */     if ((localModelSource2 == null) && (paramSF2Modulator.getAmountSourceOperator() != 0))
/* 664 */       return;
/* 665 */     double d = paramSF2Modulator.getAmount();
/* 666 */     double[] arrayOfDouble = new double[1];
/* 667 */     ModelSource[] arrayOfModelSource = new ModelSource[1];
/* 668 */     arrayOfDouble[0] = 1.0D;
/* 669 */     ModelDestination localModelDestination = convertDestination(paramSF2Modulator.getDestinationOperator(), arrayOfDouble, arrayOfModelSource);
/*     */ 
/* 671 */     d *= arrayOfDouble[0];
/* 672 */     if (localModelDestination == null)
/* 673 */       return;
/* 674 */     if (paramSF2Modulator.getTransportOperator() == 2) {
/* 675 */       ((ModelStandardTransform)localModelDestination.getTransform()).setTransform(4);
/*     */     }
/*     */ 
/* 678 */     ModelConnectionBlock localModelConnectionBlock = new ModelConnectionBlock(localModelSource1, localModelSource2, d, localModelDestination);
/* 679 */     if (arrayOfModelSource[0] != null)
/* 680 */       localModelConnectionBlock.addSource(arrayOfModelSource[0]);
/* 681 */     paramModelPerformer.getConnectionBlocks().add(localModelConnectionBlock);
/*     */   }
/*     */ 
/*     */   private static ModelSource convertSource(int paramInt)
/*     */   {
/* 686 */     if (paramInt == 0)
/* 687 */       return null;
/* 688 */     ModelIdentifier localModelIdentifier = null;
/* 689 */     int i = paramInt & 0x7F;
/* 690 */     if ((paramInt & 0x80) != 0) {
/* 691 */       localModelIdentifier = new ModelIdentifier("midi_cc", Integer.toString(i));
/*     */     } else {
/* 693 */       if (i == 2)
/* 694 */         localModelIdentifier = ModelSource.SOURCE_NOTEON_VELOCITY;
/* 695 */       if (i == 3)
/* 696 */         localModelIdentifier = ModelSource.SOURCE_NOTEON_KEYNUMBER;
/* 697 */       if (i == 10)
/* 698 */         localModelIdentifier = ModelSource.SOURCE_MIDI_POLY_PRESSURE;
/* 699 */       if (i == 13)
/* 700 */         localModelIdentifier = ModelSource.SOURCE_MIDI_CHANNEL_PRESSURE;
/* 701 */       if (i == 14)
/* 702 */         localModelIdentifier = ModelSource.SOURCE_MIDI_PITCH;
/* 703 */       if (i == 16)
/* 704 */         localModelIdentifier = new ModelIdentifier("midi_rpn", "0");
/*     */     }
/* 706 */     if (localModelIdentifier == null) {
/* 707 */       return null;
/*     */     }
/* 709 */     ModelSource localModelSource = new ModelSource(localModelIdentifier);
/* 710 */     ModelStandardTransform localModelStandardTransform = (ModelStandardTransform)localModelSource.getTransform();
/*     */ 
/* 713 */     if ((0x100 & paramInt) != 0)
/* 714 */       localModelStandardTransform.setDirection(true);
/*     */     else {
/* 716 */       localModelStandardTransform.setDirection(false);
/*     */     }
/* 718 */     if ((0x200 & paramInt) != 0)
/* 719 */       localModelStandardTransform.setPolarity(true);
/*     */     else {
/* 721 */       localModelStandardTransform.setPolarity(false);
/*     */     }
/* 723 */     if ((0x400 & paramInt) != 0)
/* 724 */       localModelStandardTransform.setTransform(1);
/* 725 */     if ((0x800 & paramInt) != 0)
/* 726 */       localModelStandardTransform.setTransform(2);
/* 727 */     if ((0xC00 & paramInt) != 0) {
/* 728 */       localModelStandardTransform.setTransform(3);
/*     */     }
/* 730 */     return localModelSource;
/*     */   }
/*     */ 
/*     */   static ModelDestination convertDestination(int paramInt, double[] paramArrayOfDouble, ModelSource[] paramArrayOfModelSource)
/*     */   {
/* 735 */     ModelIdentifier localModelIdentifier = null;
/* 736 */     switch (paramInt) {
/*     */     case 8:
/* 738 */       localModelIdentifier = ModelDestination.DESTINATION_FILTER_FREQ;
/* 739 */       break;
/*     */     case 9:
/* 741 */       localModelIdentifier = ModelDestination.DESTINATION_FILTER_Q;
/* 742 */       break;
/*     */     case 15:
/* 744 */       localModelIdentifier = ModelDestination.DESTINATION_CHORUS;
/* 745 */       break;
/*     */     case 16:
/* 747 */       localModelIdentifier = ModelDestination.DESTINATION_REVERB;
/* 748 */       break;
/*     */     case 17:
/* 750 */       localModelIdentifier = ModelDestination.DESTINATION_PAN;
/* 751 */       break;
/*     */     case 21:
/* 753 */       localModelIdentifier = ModelDestination.DESTINATION_LFO1_DELAY;
/* 754 */       break;
/*     */     case 22:
/* 756 */       localModelIdentifier = ModelDestination.DESTINATION_LFO1_FREQ;
/* 757 */       break;
/*     */     case 23:
/* 759 */       localModelIdentifier = ModelDestination.DESTINATION_LFO2_DELAY;
/* 760 */       break;
/*     */     case 24:
/* 762 */       localModelIdentifier = ModelDestination.DESTINATION_LFO2_FREQ;
/* 763 */       break;
/*     */     case 25:
/* 766 */       localModelIdentifier = ModelDestination.DESTINATION_EG2_DELAY;
/* 767 */       break;
/*     */     case 26:
/* 769 */       localModelIdentifier = ModelDestination.DESTINATION_EG2_ATTACK;
/* 770 */       break;
/*     */     case 27:
/* 772 */       localModelIdentifier = ModelDestination.DESTINATION_EG2_HOLD;
/* 773 */       break;
/*     */     case 28:
/* 775 */       localModelIdentifier = ModelDestination.DESTINATION_EG2_DECAY;
/* 776 */       break;
/*     */     case 29:
/* 778 */       localModelIdentifier = ModelDestination.DESTINATION_EG2_SUSTAIN;
/* 779 */       paramArrayOfDouble[0] = -1.0D;
/* 780 */       break;
/*     */     case 30:
/* 782 */       localModelIdentifier = ModelDestination.DESTINATION_EG2_RELEASE;
/* 783 */       break;
/*     */     case 33:
/* 785 */       localModelIdentifier = ModelDestination.DESTINATION_EG1_DELAY;
/* 786 */       break;
/*     */     case 34:
/* 788 */       localModelIdentifier = ModelDestination.DESTINATION_EG1_ATTACK;
/* 789 */       break;
/*     */     case 35:
/* 791 */       localModelIdentifier = ModelDestination.DESTINATION_EG1_HOLD;
/* 792 */       break;
/*     */     case 36:
/* 794 */       localModelIdentifier = ModelDestination.DESTINATION_EG1_DECAY;
/* 795 */       break;
/*     */     case 37:
/* 797 */       localModelIdentifier = ModelDestination.DESTINATION_EG1_SUSTAIN;
/* 798 */       paramArrayOfDouble[0] = -1.0D;
/* 799 */       break;
/*     */     case 38:
/* 801 */       localModelIdentifier = ModelDestination.DESTINATION_EG1_RELEASE;
/* 802 */       break;
/*     */     case 46:
/* 804 */       localModelIdentifier = ModelDestination.DESTINATION_KEYNUMBER;
/* 805 */       break;
/*     */     case 47:
/* 807 */       localModelIdentifier = ModelDestination.DESTINATION_VELOCITY;
/* 808 */       break;
/*     */     case 51:
/* 811 */       paramArrayOfDouble[0] = 100.0D;
/* 812 */       localModelIdentifier = ModelDestination.DESTINATION_PITCH;
/* 813 */       break;
/*     */     case 52:
/* 816 */       localModelIdentifier = ModelDestination.DESTINATION_PITCH;
/* 817 */       break;
/*     */     case 48:
/* 820 */       localModelIdentifier = ModelDestination.DESTINATION_GAIN;
/* 821 */       paramArrayOfDouble[0] = -0.3762870132923126D;
/* 822 */       break;
/*     */     case 6:
/* 825 */       localModelIdentifier = ModelDestination.DESTINATION_PITCH;
/* 826 */       paramArrayOfModelSource[0] = new ModelSource(ModelSource.SOURCE_LFO2, false, true);
/*     */ 
/* 830 */       break;
/*     */     case 5:
/* 833 */       localModelIdentifier = ModelDestination.DESTINATION_PITCH;
/* 834 */       paramArrayOfModelSource[0] = new ModelSource(ModelSource.SOURCE_LFO1, false, true);
/*     */ 
/* 838 */       break;
/*     */     case 10:
/* 841 */       localModelIdentifier = ModelDestination.DESTINATION_FILTER_FREQ;
/* 842 */       paramArrayOfModelSource[0] = new ModelSource(ModelSource.SOURCE_LFO1, false, true);
/*     */ 
/* 846 */       break;
/*     */     case 13:
/* 849 */       localModelIdentifier = ModelDestination.DESTINATION_GAIN;
/* 850 */       paramArrayOfDouble[0] = -0.3762870132923126D;
/* 851 */       paramArrayOfModelSource[0] = new ModelSource(ModelSource.SOURCE_LFO1, false, true);
/*     */ 
/* 855 */       break;
/*     */     case 7:
/* 858 */       localModelIdentifier = ModelDestination.DESTINATION_PITCH;
/* 859 */       paramArrayOfModelSource[0] = new ModelSource(ModelSource.SOURCE_EG2, false, true);
/*     */ 
/* 863 */       break;
/*     */     case 11:
/* 866 */       localModelIdentifier = ModelDestination.DESTINATION_FILTER_FREQ;
/* 867 */       paramArrayOfModelSource[0] = new ModelSource(ModelSource.SOURCE_EG2, false, true);
/*     */ 
/* 871 */       break;
/*     */     case 12:
/*     */     case 14:
/*     */     case 18:
/*     */     case 19:
/*     */     case 20:
/*     */     case 31:
/*     */     case 32:
/*     */     case 39:
/*     */     case 40:
/*     */     case 41:
/*     */     case 42:
/*     */     case 43:
/*     */     case 44:
/*     */     case 45:
/*     */     case 49:
/* 876 */     case 50: } if (localModelIdentifier != null)
/* 877 */       return new ModelDestination(localModelIdentifier);
/* 878 */     return null;
/*     */   }
/*     */ 
/*     */   private void addTimecentValue(ModelPerformer paramModelPerformer, ModelIdentifier paramModelIdentifier, short paramShort)
/*     */   {
/*     */     double d;
/* 884 */     if (paramShort == -12000)
/* 885 */       d = (-1.0D / 0.0D);
/*     */     else
/* 887 */       d = paramShort;
/* 888 */     paramModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(d, new ModelDestination(paramModelIdentifier)));
/*     */   }
/*     */ 
/*     */   private void addValue(ModelPerformer paramModelPerformer, ModelIdentifier paramModelIdentifier, short paramShort)
/*     */   {
/* 894 */     double d = paramShort;
/* 895 */     paramModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(d, new ModelDestination(paramModelIdentifier)));
/*     */   }
/*     */ 
/*     */   private void addValue(ModelPerformer paramModelPerformer, ModelIdentifier paramModelIdentifier, double paramDouble)
/*     */   {
/* 901 */     double d = paramDouble;
/* 902 */     paramModelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(d, new ModelDestination(paramModelIdentifier)));
/*     */   }
/*     */ 
/*     */   private short getGeneratorValue(Map<Integer, Short> paramMap, int paramInt)
/*     */   {
/* 907 */     if (paramMap.containsKey(Integer.valueOf(paramInt)))
/* 908 */       return ((Short)paramMap.get(Integer.valueOf(paramInt))).shortValue();
/* 909 */     return SF2Region.getDefaultValue(paramInt);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SF2Instrument
 * JD-Core Version:    0.6.2
 */