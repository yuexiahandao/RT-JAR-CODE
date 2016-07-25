/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.sound.midi.VoiceStatus;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ 
/*     */ public final class SoftVoice extends VoiceStatus
/*     */ {
/*  41 */   public int exclusiveClass = 0;
/*  42 */   public boolean releaseTriggered = false;
/*  43 */   private int noteOn_noteNumber = 0;
/*  44 */   private int noteOn_velocity = 0;
/*  45 */   private int noteOff_velocity = 0;
/*  46 */   private int delay = 0;
/*  47 */   ModelChannelMixer channelmixer = null;
/*  48 */   double tunedKey = 0.0D;
/*  49 */   SoftTuning tuning = null;
/*  50 */   SoftChannel stealer_channel = null;
/*  51 */   ModelConnectionBlock[] stealer_extendedConnectionBlocks = null;
/*  52 */   SoftPerformer stealer_performer = null;
/*  53 */   ModelChannelMixer stealer_channelmixer = null;
/*  54 */   int stealer_voiceID = -1;
/*  55 */   int stealer_noteNumber = 0;
/*  56 */   int stealer_velocity = 0;
/*  57 */   boolean stealer_releaseTriggered = false;
/*  58 */   int voiceID = -1;
/*  59 */   boolean sustain = false;
/*  60 */   boolean sostenuto = false;
/*  61 */   boolean portamento = false;
/*     */   private final SoftFilter filter_left;
/*     */   private final SoftFilter filter_right;
/*  64 */   private final SoftProcess eg = new SoftEnvelopeGenerator();
/*  65 */   private final SoftProcess lfo = new SoftLowFrequencyOscillator();
/*  66 */   Map<String, SoftControl> objects = new HashMap();
/*     */   SoftSynthesizer synthesizer;
/*     */   SoftInstrument instrument;
/*     */   SoftPerformer performer;
/*  71 */   SoftChannel softchannel = null;
/*  72 */   boolean on = false;
/*  73 */   private boolean audiostarted = false;
/*  74 */   private boolean started = false;
/*  75 */   private boolean stopping = false;
/*  76 */   private float osc_attenuation = 0.0F;
/*     */   private ModelOscillatorStream osc_stream;
/*     */   private int osc_stream_nrofchannels;
/*  79 */   private float[][] osc_buff = new float[2][];
/*  80 */   private boolean osc_stream_off_transmitted = false;
/*  81 */   private boolean out_mixer_end = false;
/*  82 */   private float out_mixer_left = 0.0F;
/*  83 */   private float out_mixer_right = 0.0F;
/*  84 */   private float out_mixer_effect1 = 0.0F;
/*  85 */   private float out_mixer_effect2 = 0.0F;
/*  86 */   private float last_out_mixer_left = 0.0F;
/*  87 */   private float last_out_mixer_right = 0.0F;
/*  88 */   private float last_out_mixer_effect1 = 0.0F;
/*  89 */   private float last_out_mixer_effect2 = 0.0F;
/*  90 */   ModelConnectionBlock[] extendedConnectionBlocks = null;
/*     */   private ModelConnectionBlock[] connections;
/*  93 */   private double[] connections_last = new double[50];
/*     */ 
/*  95 */   private double[][][] connections_src = new double[50][3];
/*     */ 
/*  97 */   private int[][] connections_src_kc = new int[50][3];
/*     */ 
/*  99 */   private double[][] connections_dst = new double[50][];
/* 100 */   private boolean soundoff = false;
/* 101 */   private float lastMuteValue = 0.0F;
/* 102 */   private float lastSoloMuteValue = 0.0F;
/* 103 */   double[] co_noteon_keynumber = new double[1];
/* 104 */   double[] co_noteon_velocity = new double[1];
/* 105 */   double[] co_noteon_on = new double[1];
/* 106 */   private final SoftControl co_noteon = new SoftControl() {
/* 107 */     double[] keynumber = SoftVoice.this.co_noteon_keynumber;
/* 108 */     double[] velocity = SoftVoice.this.co_noteon_velocity;
/* 109 */     double[] on = SoftVoice.this.co_noteon_on;
/*     */ 
/* 111 */     public double[] get(int paramAnonymousInt, String paramAnonymousString) { if (paramAnonymousString == null)
/* 112 */         return null;
/* 113 */       if (paramAnonymousString.equals("keynumber"))
/* 114 */         return this.keynumber;
/* 115 */       if (paramAnonymousString.equals("velocity"))
/* 116 */         return this.velocity;
/* 117 */       if (paramAnonymousString.equals("on"))
/* 118 */         return this.on;
/* 119 */       return null;
/*     */     }
/* 106 */   };
/*     */ 
/* 122 */   private final double[] co_mixer_active = new double[1];
/* 123 */   private final double[] co_mixer_gain = new double[1];
/* 124 */   private final double[] co_mixer_pan = new double[1];
/* 125 */   private final double[] co_mixer_balance = new double[1];
/* 126 */   private final double[] co_mixer_reverb = new double[1];
/* 127 */   private final double[] co_mixer_chorus = new double[1];
/* 128 */   private final SoftControl co_mixer = new SoftControl() {
/* 129 */     double[] active = SoftVoice.this.co_mixer_active;
/* 130 */     double[] gain = SoftVoice.this.co_mixer_gain;
/* 131 */     double[] pan = SoftVoice.this.co_mixer_pan;
/* 132 */     double[] balance = SoftVoice.this.co_mixer_balance;
/* 133 */     double[] reverb = SoftVoice.this.co_mixer_reverb;
/* 134 */     double[] chorus = SoftVoice.this.co_mixer_chorus;
/*     */ 
/* 136 */     public double[] get(int paramAnonymousInt, String paramAnonymousString) { if (paramAnonymousString == null)
/* 137 */         return null;
/* 138 */       if (paramAnonymousString.equals("active"))
/* 139 */         return this.active;
/* 140 */       if (paramAnonymousString.equals("gain"))
/* 141 */         return this.gain;
/* 142 */       if (paramAnonymousString.equals("pan"))
/* 143 */         return this.pan;
/* 144 */       if (paramAnonymousString.equals("balance"))
/* 145 */         return this.balance;
/* 146 */       if (paramAnonymousString.equals("reverb"))
/* 147 */         return this.reverb;
/* 148 */       if (paramAnonymousString.equals("chorus"))
/* 149 */         return this.chorus;
/* 150 */       return null;
/*     */     }
/* 128 */   };
/*     */ 
/* 153 */   private final double[] co_osc_pitch = new double[1];
/* 154 */   private final SoftControl co_osc = new SoftControl() {
/* 155 */     double[] pitch = SoftVoice.this.co_osc_pitch;
/*     */ 
/* 157 */     public double[] get(int paramAnonymousInt, String paramAnonymousString) { if (paramAnonymousString == null)
/* 158 */         return null;
/* 159 */       if (paramAnonymousString.equals("pitch"))
/* 160 */         return this.pitch;
/* 161 */       return null;
/*     */     }
/* 154 */   };
/*     */ 
/* 164 */   private final double[] co_filter_freq = new double[1];
/* 165 */   private final double[] co_filter_type = new double[1];
/* 166 */   private final double[] co_filter_q = new double[1];
/* 167 */   private final SoftControl co_filter = new SoftControl() {
/* 168 */     double[] freq = SoftVoice.this.co_filter_freq;
/* 169 */     double[] ftype = SoftVoice.this.co_filter_type;
/* 170 */     double[] q = SoftVoice.this.co_filter_q;
/*     */ 
/* 172 */     public double[] get(int paramAnonymousInt, String paramAnonymousString) { if (paramAnonymousString == null)
/* 173 */         return null;
/* 174 */       if (paramAnonymousString.equals("freq"))
/* 175 */         return this.freq;
/* 176 */       if (paramAnonymousString.equals("type"))
/* 177 */         return this.ftype;
/* 178 */       if (paramAnonymousString.equals("q"))
/* 179 */         return this.q;
/* 180 */       return null; } } ;
/*     */   SoftResamplerStreamer resampler;
/*     */   private final int nrofchannels;
/*     */ 
/*     */   public SoftVoice(SoftSynthesizer paramSoftSynthesizer) {
/* 187 */     this.synthesizer = paramSoftSynthesizer;
/* 188 */     this.filter_left = new SoftFilter(paramSoftSynthesizer.getFormat().getSampleRate());
/* 189 */     this.filter_right = new SoftFilter(paramSoftSynthesizer.getFormat().getSampleRate());
/* 190 */     this.nrofchannels = paramSoftSynthesizer.getFormat().getChannels();
/*     */   }
/*     */ 
/*     */   private int getValueKC(ModelIdentifier paramModelIdentifier) {
/* 194 */     if (paramModelIdentifier.getObject().equals("midi_cc")) {
/* 195 */       int i = Integer.parseInt(paramModelIdentifier.getVariable());
/* 196 */       if ((i != 0) && (i != 32) && 
/* 197 */         (i < 120))
/* 198 */         return i;
/*     */     }
/* 200 */     else if (paramModelIdentifier.getObject().equals("midi_rpn")) {
/* 201 */       if (paramModelIdentifier.getVariable().equals("1"))
/* 202 */         return 120;
/* 203 */       if (paramModelIdentifier.getVariable().equals("2"))
/* 204 */         return 121;
/*     */     }
/* 206 */     return -1;
/*     */   }
/*     */ 
/*     */   private double[] getValue(ModelIdentifier paramModelIdentifier) {
/* 210 */     SoftControl localSoftControl = (SoftControl)this.objects.get(paramModelIdentifier.getObject());
/* 211 */     if (localSoftControl == null)
/* 212 */       return null;
/* 213 */     return localSoftControl.get(paramModelIdentifier.getInstance(), paramModelIdentifier.getVariable());
/*     */   }
/*     */ 
/*     */   private double transformValue(double paramDouble, ModelSource paramModelSource) {
/* 217 */     if (paramModelSource.getTransform() != null) {
/* 218 */       return paramModelSource.getTransform().transform(paramDouble);
/*     */     }
/* 220 */     return paramDouble;
/*     */   }
/*     */ 
/*     */   private double transformValue(double paramDouble, ModelDestination paramModelDestination) {
/* 224 */     if (paramModelDestination.getTransform() != null) {
/* 225 */       return paramModelDestination.getTransform().transform(paramDouble);
/*     */     }
/* 227 */     return paramDouble;
/*     */   }
/*     */ 
/*     */   private double processKeyBasedController(double paramDouble, int paramInt) {
/* 231 */     if (paramInt == -1)
/* 232 */       return paramDouble;
/* 233 */     if ((this.softchannel.keybasedcontroller_active != null) && 
/* 234 */       (this.softchannel.keybasedcontroller_active[this.note] != null) && 
/* 235 */       (this.softchannel.keybasedcontroller_active[this.note][paramInt] != 0)) {
/* 236 */       double d = this.softchannel.keybasedcontroller_value[this.note][paramInt];
/*     */ 
/* 238 */       if ((paramInt == 10) || (paramInt == 91) || (paramInt == 93))
/* 239 */         return d;
/* 240 */       paramDouble += d * 2.0D - 1.0D;
/* 241 */       if (paramDouble > 1.0D)
/* 242 */         paramDouble = 1.0D;
/* 243 */       else if (paramDouble < 0.0D)
/* 244 */         paramDouble = 0.0D;
/*     */     }
/* 246 */     return paramDouble;
/*     */   }
/*     */ 
/*     */   private void processConnection(int paramInt) {
/* 250 */     ModelConnectionBlock localModelConnectionBlock = this.connections[paramInt];
/* 251 */     double[][] arrayOfDouble = this.connections_src[paramInt];
/* 252 */     double[] arrayOfDouble1 = this.connections_dst[paramInt];
/* 253 */     if ((arrayOfDouble1 == null) || (Double.isInfinite(arrayOfDouble1[0]))) {
/* 254 */       return;
/*     */     }
/* 256 */     double d = localModelConnectionBlock.getScale();
/*     */     ModelSource[] arrayOfModelSource;
/* 257 */     if (this.softchannel.keybasedcontroller_active == null) {
/* 258 */       arrayOfModelSource = localModelConnectionBlock.getSources();
/* 259 */       for (int i = 0; i < arrayOfModelSource.length; i++) {
/* 260 */         d *= transformValue(arrayOfDouble[i][0], arrayOfModelSource[i]);
/* 261 */         if (d == 0.0D) break;
/*     */       }
/*     */     }
/*     */     else {
/* 265 */       arrayOfModelSource = localModelConnectionBlock.getSources();
/* 266 */       int[] arrayOfInt = this.connections_src_kc[paramInt];
/* 267 */       for (int j = 0; j < arrayOfModelSource.length; j++) {
/* 268 */         d *= transformValue(processKeyBasedController(arrayOfDouble[j][0], arrayOfInt[j]), arrayOfModelSource[j]);
/*     */ 
/* 270 */         if (d == 0.0D) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 275 */     d = transformValue(d, localModelConnectionBlock.getDestination());
/* 276 */     arrayOfDouble1[0] = (arrayOfDouble1[0] - this.connections_last[paramInt] + d);
/* 277 */     this.connections_last[paramInt] = d;
/*     */   }
/*     */ 
/*     */   void updateTuning(SoftTuning paramSoftTuning)
/*     */   {
/* 282 */     this.tuning = paramSoftTuning;
/* 283 */     this.tunedKey = (this.tuning.getTuning(this.note) / 100.0D);
/* 284 */     if (!this.portamento) {
/* 285 */       this.co_noteon_keynumber[0] = (this.tunedKey * 0.0078125D);
/* 286 */       if (this.performer == null)
/* 287 */         return;
/* 288 */       int[] arrayOfInt = this.performer.midi_connections[4];
/* 289 */       if (arrayOfInt == null)
/* 290 */         return;
/* 291 */       for (int i = 0; i < arrayOfInt.length; i++)
/* 292 */         processConnection(arrayOfInt[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   void setNote(int paramInt) {
/* 297 */     this.note = paramInt;
/* 298 */     this.tunedKey = (this.tuning.getTuning(paramInt) / 100.0D);
/*     */   }
/*     */ 
/*     */   void noteOn(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 303 */     this.sustain = false;
/* 304 */     this.sostenuto = false;
/* 305 */     this.portamento = false;
/*     */ 
/* 307 */     this.soundoff = false;
/* 308 */     this.on = true;
/* 309 */     this.active = true;
/* 310 */     this.started = true;
/*     */ 
/* 313 */     this.noteOn_noteNumber = paramInt1;
/* 314 */     this.noteOn_velocity = paramInt2;
/* 315 */     this.delay = paramInt3;
/*     */ 
/* 317 */     this.lastMuteValue = 0.0F;
/* 318 */     this.lastSoloMuteValue = 0.0F;
/*     */ 
/* 320 */     setNote(paramInt1);
/*     */ 
/* 322 */     if (this.performer.forcedKeynumber)
/* 323 */       this.co_noteon_keynumber[0] = 0.0D;
/*     */     else
/* 325 */       this.co_noteon_keynumber[0] = (this.tunedKey * 0.0078125D);
/* 326 */     if (this.performer.forcedVelocity)
/* 327 */       this.co_noteon_velocity[0] = 0.0D;
/*     */     else
/* 329 */       this.co_noteon_velocity[0] = (paramInt2 * 0.007813F);
/* 330 */     this.co_mixer_active[0] = 0.0D;
/* 331 */     this.co_mixer_gain[0] = 0.0D;
/* 332 */     this.co_mixer_pan[0] = 0.0D;
/* 333 */     this.co_mixer_balance[0] = 0.0D;
/* 334 */     this.co_mixer_reverb[0] = 0.0D;
/* 335 */     this.co_mixer_chorus[0] = 0.0D;
/* 336 */     this.co_osc_pitch[0] = 0.0D;
/* 337 */     this.co_filter_freq[0] = 0.0D;
/* 338 */     this.co_filter_q[0] = 0.0D;
/* 339 */     this.co_filter_type[0] = 0.0D;
/* 340 */     this.co_noteon_on[0] = 1.0D;
/*     */ 
/* 342 */     this.eg.reset();
/* 343 */     this.lfo.reset();
/* 344 */     this.filter_left.reset();
/* 345 */     this.filter_right.reset();
/*     */ 
/* 347 */     this.objects.put("master", this.synthesizer.getMainMixer().co_master);
/* 348 */     this.objects.put("eg", this.eg);
/* 349 */     this.objects.put("lfo", this.lfo);
/* 350 */     this.objects.put("noteon", this.co_noteon);
/* 351 */     this.objects.put("osc", this.co_osc);
/* 352 */     this.objects.put("mixer", this.co_mixer);
/* 353 */     this.objects.put("filter", this.co_filter);
/*     */ 
/* 355 */     this.connections = this.performer.connections;
/*     */ 
/* 357 */     if ((this.connections_last == null) || (this.connections_last.length < this.connections.length))
/*     */     {
/* 359 */       this.connections_last = new double[this.connections.length];
/*     */     }
/* 361 */     if ((this.connections_src == null) || (this.connections_src.length < this.connections.length))
/*     */     {
/* 363 */       this.connections_src = new double[this.connections.length][][];
/* 364 */       this.connections_src_kc = new int[this.connections.length][];
/*     */     }
/* 366 */     if ((this.connections_dst == null) || (this.connections_dst.length < this.connections.length))
/*     */     {
/* 368 */       this.connections_dst = new double[this.connections.length][];
/*     */     }
/*     */     Object localObject1;
/* 370 */     for (int i = 0; i < this.connections.length; i++) {
/* 371 */       ModelConnectionBlock localModelConnectionBlock = this.connections[i];
/* 372 */       this.connections_last[i] = 0.0D;
/* 373 */       if (localModelConnectionBlock.getSources() != null) {
/* 374 */         ModelSource[] arrayOfModelSource = localModelConnectionBlock.getSources();
/* 375 */         if ((this.connections_src[i] == null) || (this.connections_src[i].length < arrayOfModelSource.length))
/*     */         {
/* 377 */           this.connections_src[i] = new double[arrayOfModelSource.length][];
/* 378 */           this.connections_src_kc[i] = new int[arrayOfModelSource.length];
/*     */         }
/* 380 */         localObject1 = this.connections_src[i];
/* 381 */         int[] arrayOfInt = this.connections_src_kc[i];
/* 382 */         this.connections_src[i] = localObject1;
/* 383 */         for (int m = 0; m < arrayOfModelSource.length; m++) {
/* 384 */           arrayOfInt[m] = getValueKC(arrayOfModelSource[m].getIdentifier());
/* 385 */           localObject1[m] = getValue(arrayOfModelSource[m].getIdentifier());
/*     */         }
/*     */       }
/*     */ 
/* 389 */       if (localModelConnectionBlock.getDestination() != null) {
/* 390 */         this.connections_dst[i] = getValue(localModelConnectionBlock.getDestination().getIdentifier());
/*     */       }
/*     */       else {
/* 393 */         this.connections_dst[i] = null;
/*     */       }
/*     */     }
/* 396 */     for (i = 0; i < this.connections.length; i++) {
/* 397 */       processConnection(i);
/*     */     }
/* 399 */     if (this.extendedConnectionBlocks != null) {
/* 400 */       for (localObject1 : this.extendedConnectionBlocks) {
/* 401 */         double d1 = 0.0D;
/*     */         Object localObject3;
/*     */         double d2;
/*     */         ModelTransform localModelTransform2;
/* 403 */         if (this.softchannel.keybasedcontroller_active == null)
/* 404 */           for (localObject3 : ((ModelConnectionBlock)localObject1).getSources()) {
/* 405 */             d2 = getValue(localObject3.getIdentifier())[0];
/* 406 */             localModelTransform2 = localObject3.getTransform();
/* 407 */             if (localModelTransform2 == null)
/* 408 */               d1 += d2;
/*     */             else
/* 410 */               d1 += localModelTransform2.transform(d2);
/*     */           }
/*     */         else {
/* 413 */           for (localObject3 : ((ModelConnectionBlock)localObject1).getSources()) {
/* 414 */             d2 = getValue(localObject3.getIdentifier())[0];
/* 415 */             d2 = processKeyBasedController(d2, getValueKC(localObject3.getIdentifier()));
/*     */ 
/* 417 */             localModelTransform2 = localObject3.getTransform();
/* 418 */             if (localModelTransform2 == null)
/* 419 */               d1 += d2;
/*     */             else {
/* 421 */               d1 += localModelTransform2.transform(d2);
/*     */             }
/*     */           }
/*     */         }
/* 425 */         ??? = ((ModelConnectionBlock)localObject1).getDestination();
/* 426 */         ModelTransform localModelTransform1 = ((ModelDestination)???).getTransform();
/* 427 */         if (localModelTransform1 != null)
/* 428 */           d1 = localModelTransform1.transform(d1);
/* 429 */         getValue(((ModelDestination)???).getIdentifier())[0] += d1;
/*     */       }
/*     */     }
/*     */ 
/* 433 */     this.eg.init(this.synthesizer);
/* 434 */     this.lfo.init(this.synthesizer);
/*     */   }
/*     */ 
/*     */   void setPolyPressure(int paramInt)
/*     */   {
/* 439 */     if (this.performer == null)
/* 440 */       return;
/* 441 */     int[] arrayOfInt = this.performer.midi_connections[2];
/* 442 */     if (arrayOfInt == null)
/* 443 */       return;
/* 444 */     for (int i = 0; i < arrayOfInt.length; i++)
/* 445 */       processConnection(arrayOfInt[i]);
/*     */   }
/*     */ 
/*     */   void setChannelPressure(int paramInt) {
/* 449 */     if (this.performer == null)
/* 450 */       return;
/* 451 */     int[] arrayOfInt = this.performer.midi_connections[1];
/* 452 */     if (arrayOfInt == null)
/* 453 */       return;
/* 454 */     for (int i = 0; i < arrayOfInt.length; i++)
/* 455 */       processConnection(arrayOfInt[i]);
/*     */   }
/*     */ 
/*     */   void controlChange(int paramInt1, int paramInt2) {
/* 459 */     if (this.performer == null)
/* 460 */       return;
/* 461 */     int[] arrayOfInt = this.performer.midi_ctrl_connections[paramInt1];
/* 462 */     if (arrayOfInt == null)
/* 463 */       return;
/* 464 */     for (int i = 0; i < arrayOfInt.length; i++)
/* 465 */       processConnection(arrayOfInt[i]);
/*     */   }
/*     */ 
/*     */   void nrpnChange(int paramInt1, int paramInt2) {
/* 469 */     if (this.performer == null)
/* 470 */       return;
/* 471 */     int[] arrayOfInt = (int[])this.performer.midi_nrpn_connections.get(Integer.valueOf(paramInt1));
/* 472 */     if (arrayOfInt == null)
/* 473 */       return;
/* 474 */     for (int i = 0; i < arrayOfInt.length; i++)
/* 475 */       processConnection(arrayOfInt[i]);
/*     */   }
/*     */ 
/*     */   void rpnChange(int paramInt1, int paramInt2) {
/* 479 */     if (this.performer == null)
/* 480 */       return;
/* 481 */     int[] arrayOfInt = (int[])this.performer.midi_rpn_connections.get(Integer.valueOf(paramInt1));
/* 482 */     if (arrayOfInt == null)
/* 483 */       return;
/* 484 */     for (int i = 0; i < arrayOfInt.length; i++)
/* 485 */       processConnection(arrayOfInt[i]);
/*     */   }
/*     */ 
/*     */   void setPitchBend(int paramInt) {
/* 489 */     if (this.performer == null)
/* 490 */       return;
/* 491 */     int[] arrayOfInt = this.performer.midi_connections[0];
/* 492 */     if (arrayOfInt == null)
/* 493 */       return;
/* 494 */     for (int i = 0; i < arrayOfInt.length; i++)
/* 495 */       processConnection(arrayOfInt[i]);
/*     */   }
/*     */ 
/*     */   void setMute(boolean paramBoolean) {
/* 499 */     this.co_mixer_gain[0] -= this.lastMuteValue;
/* 500 */     this.lastMuteValue = (paramBoolean ? -960.0F : 0.0F);
/* 501 */     this.co_mixer_gain[0] += this.lastMuteValue;
/*     */   }
/*     */ 
/*     */   void setSoloMute(boolean paramBoolean) {
/* 505 */     this.co_mixer_gain[0] -= this.lastSoloMuteValue;
/* 506 */     this.lastSoloMuteValue = (paramBoolean ? -960.0F : 0.0F);
/* 507 */     this.co_mixer_gain[0] += this.lastSoloMuteValue;
/*     */   }
/*     */ 
/*     */   void shutdown() {
/* 511 */     if (this.co_noteon_on[0] < -0.5D)
/* 512 */       return;
/* 513 */     this.on = false;
/*     */ 
/* 515 */     this.co_noteon_on[0] = -1.0D;
/*     */ 
/* 517 */     if (this.performer == null)
/* 518 */       return;
/* 519 */     int[] arrayOfInt = this.performer.midi_connections[3];
/* 520 */     if (arrayOfInt == null)
/* 521 */       return;
/* 522 */     for (int i = 0; i < arrayOfInt.length; i++)
/* 523 */       processConnection(arrayOfInt[i]);
/*     */   }
/*     */ 
/*     */   void soundOff() {
/* 527 */     this.on = false;
/* 528 */     this.soundoff = true;
/*     */   }
/*     */ 
/*     */   void noteOff(int paramInt) {
/* 532 */     if (!this.on)
/* 533 */       return;
/* 534 */     this.on = false;
/*     */ 
/* 536 */     this.noteOff_velocity = paramInt;
/*     */ 
/* 538 */     if (this.softchannel.sustain) {
/* 539 */       this.sustain = true;
/* 540 */       return;
/*     */     }
/* 542 */     if (this.sostenuto) {
/* 543 */       return;
/*     */     }
/* 545 */     this.co_noteon_on[0] = 0.0D;
/*     */ 
/* 547 */     if (this.performer == null)
/* 548 */       return;
/* 549 */     int[] arrayOfInt = this.performer.midi_connections[3];
/* 550 */     if (arrayOfInt == null)
/* 551 */       return;
/* 552 */     for (int i = 0; i < arrayOfInt.length; i++)
/* 553 */       processConnection(arrayOfInt[i]);
/*     */   }
/*     */ 
/*     */   void redamp() {
/* 557 */     if (this.co_noteon_on[0] > 0.5D)
/* 558 */       return;
/* 559 */     if (this.co_noteon_on[0] < -0.5D) {
/* 560 */       return;
/*     */     }
/* 562 */     this.sustain = true;
/* 563 */     this.co_noteon_on[0] = 1.0D;
/*     */ 
/* 565 */     if (this.performer == null)
/* 566 */       return;
/* 567 */     int[] arrayOfInt = this.performer.midi_connections[3];
/* 568 */     if (arrayOfInt == null)
/* 569 */       return;
/* 570 */     for (int i = 0; i < arrayOfInt.length; i++)
/* 571 */       processConnection(arrayOfInt[i]);
/*     */   }
/*     */ 
/*     */   void processControlLogic() {
/* 575 */     if (this.stopping) {
/* 576 */       this.active = false;
/* 577 */       this.stopping = false;
/* 578 */       this.audiostarted = false;
/* 579 */       this.instrument = null;
/* 580 */       this.performer = null;
/* 581 */       this.connections = null;
/* 582 */       this.extendedConnectionBlocks = null;
/* 583 */       this.channelmixer = null;
/* 584 */       if (this.osc_stream != null)
/*     */         try {
/* 586 */           this.osc_stream.close();
/*     */         }
/*     */         catch (IOException localIOException1)
/*     */         {
/*     */         }
/* 591 */       if (this.stealer_channel != null) {
/* 592 */         this.stealer_channel.initVoice(this, this.stealer_performer, this.stealer_voiceID, this.stealer_noteNumber, this.stealer_velocity, 0, this.stealer_extendedConnectionBlocks, this.stealer_channelmixer, this.stealer_releaseTriggered);
/*     */ 
/* 596 */         this.stealer_releaseTriggered = false;
/* 597 */         this.stealer_channel = null;
/* 598 */         this.stealer_performer = null;
/* 599 */         this.stealer_voiceID = -1;
/* 600 */         this.stealer_noteNumber = 0;
/* 601 */         this.stealer_velocity = 0;
/* 602 */         this.stealer_extendedConnectionBlocks = null;
/* 603 */         this.stealer_channelmixer = null;
/*     */       }
/*     */     }
/* 606 */     if (this.started) {
/* 607 */       this.audiostarted = true;
/*     */ 
/* 609 */       ModelOscillator localModelOscillator = this.performer.oscillators[0];
/*     */ 
/* 611 */       this.osc_stream_off_transmitted = false;
/* 612 */       if ((localModelOscillator instanceof ModelWavetable))
/*     */         try {
/* 614 */           this.resampler.open((ModelWavetable)localModelOscillator, this.synthesizer.getFormat().getSampleRate());
/*     */ 
/* 616 */           this.osc_stream = this.resampler;
/*     */         }
/*     */         catch (IOException localIOException2) {
/*     */         }
/*     */       else {
/* 621 */         this.osc_stream = localModelOscillator.open(this.synthesizer.getFormat().getSampleRate());
/*     */       }
/* 623 */       this.osc_attenuation = localModelOscillator.getAttenuation();
/* 624 */       this.osc_stream_nrofchannels = localModelOscillator.getChannels();
/* 625 */       if ((this.osc_buff == null) || (this.osc_buff.length < this.osc_stream_nrofchannels)) {
/* 626 */         this.osc_buff = new float[this.osc_stream_nrofchannels][];
/*     */       }
/* 628 */       if (this.osc_stream != null) {
/* 629 */         this.osc_stream.noteOn(this.softchannel, this, this.noteOn_noteNumber, this.noteOn_velocity);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 634 */     if (this.audiostarted) {
/* 635 */       if (this.portamento) {
/* 636 */         double d1 = this.tunedKey - this.co_noteon_keynumber[0] * 128.0D;
/* 637 */         double d3 = Math.abs(d1);
/* 638 */         if (d3 < 1.0E-010D) {
/* 639 */           this.co_noteon_keynumber[0] = (this.tunedKey * 0.0078125D);
/* 640 */           this.portamento = false;
/*     */         } else {
/* 642 */           if (d3 > this.softchannel.portamento_time) {
/* 643 */             d1 = Math.signum(d1) * this.softchannel.portamento_time;
/*     */           }
/* 645 */           this.co_noteon_keynumber[0] += d1 * 0.0078125D;
/*     */         }
/*     */ 
/* 648 */         int[] arrayOfInt = this.performer.midi_connections[4];
/* 649 */         if (arrayOfInt == null)
/* 650 */           return;
/* 651 */         for (int j = 0; j < arrayOfInt.length; j++) {
/* 652 */           processConnection(arrayOfInt[j]);
/*     */         }
/*     */       }
/* 655 */       this.eg.processControlLogic();
/* 656 */       this.lfo.processControlLogic();
/*     */ 
/* 658 */       for (int i = 0; i < this.performer.ctrl_connections.length; i++) {
/* 659 */         processConnection(this.performer.ctrl_connections[i]);
/*     */       }
/* 661 */       this.osc_stream.setPitch((float)this.co_osc_pitch[0]);
/*     */ 
/* 663 */       i = (int)this.co_filter_type[0];
/*     */       double d2;
/* 666 */       if (this.co_filter_freq[0] == 13500.0D)
/* 667 */         d2 = 19912.126958213175D;
/*     */       else {
/* 669 */         d2 = 440.0D * Math.exp((this.co_filter_freq[0] - 6900.0D) * (Math.log(2.0D) / 1200.0D));
/*     */       }
/*     */ 
/* 680 */       double d4 = this.co_filter_q[0] / 10.0D;
/* 681 */       this.filter_left.setFilterType(i);
/* 682 */       this.filter_left.setFrequency(d2);
/* 683 */       this.filter_left.setResonance(d4);
/* 684 */       this.filter_right.setFilterType(i);
/* 685 */       this.filter_right.setFrequency(d2);
/* 686 */       this.filter_right.setResonance(d4);
/*     */ 
/* 691 */       float f = (float)Math.exp((-this.osc_attenuation + this.co_mixer_gain[0]) * (Math.log(10.0D) / 200.0D));
/*     */ 
/* 694 */       if (this.co_mixer_gain[0] <= -960.0D) {
/* 695 */         f = 0.0F;
/*     */       }
/* 697 */       if (this.soundoff) {
/* 698 */         this.stopping = true;
/* 699 */         f = 0.0F;
/*     */       }
/*     */ 
/* 706 */       this.volume = ((int)(Math.sqrt(f) * 128.0D));
/*     */ 
/* 710 */       double d5 = this.co_mixer_pan[0] * 0.001D;
/*     */ 
/* 712 */       if (d5 < 0.0D)
/* 713 */         d5 = 0.0D;
/* 714 */       else if (d5 > 1.0D) {
/* 715 */         d5 = 1.0D;
/*     */       }
/* 717 */       if (d5 == 0.5D) {
/* 718 */         this.out_mixer_left = (f * 0.7071068F);
/* 719 */         this.out_mixer_right = this.out_mixer_left;
/*     */       } else {
/* 721 */         this.out_mixer_left = (f * (float)Math.cos(d5 * 3.141592653589793D * 0.5D));
/* 722 */         this.out_mixer_right = (f * (float)Math.sin(d5 * 3.141592653589793D * 0.5D));
/*     */       }
/*     */ 
/* 725 */       double d6 = this.co_mixer_balance[0] * 0.001D;
/* 726 */       if (d6 != 0.5D) {
/* 727 */         if (d6 > 0.5D)
/* 728 */           this.out_mixer_left = ((float)(this.out_mixer_left * ((1.0D - d6) * 2.0D)));
/*     */         else {
/* 730 */           this.out_mixer_right = ((float)(this.out_mixer_right * (d6 * 2.0D)));
/*     */         }
/*     */       }
/* 733 */       if (this.synthesizer.reverb_on) {
/* 734 */         this.out_mixer_effect1 = ((float)(this.co_mixer_reverb[0] * 0.001D));
/* 735 */         this.out_mixer_effect1 *= f;
/*     */       } else {
/* 737 */         this.out_mixer_effect1 = 0.0F;
/* 738 */       }if (this.synthesizer.chorus_on) {
/* 739 */         this.out_mixer_effect2 = ((float)(this.co_mixer_chorus[0] * 0.001D));
/* 740 */         this.out_mixer_effect2 *= f;
/*     */       } else {
/* 742 */         this.out_mixer_effect2 = 0.0F;
/* 743 */       }this.out_mixer_end = (this.co_mixer_active[0] < 0.5D);
/*     */ 
/* 745 */       if ((!this.on) && 
/* 746 */         (!this.osc_stream_off_transmitted)) {
/* 747 */         this.osc_stream_off_transmitted = true;
/* 748 */         if (this.osc_stream != null) {
/* 749 */           this.osc_stream.noteOff(this.noteOff_velocity);
/*     */         }
/*     */       }
/*     */     }
/* 753 */     if (this.started) {
/* 754 */       this.last_out_mixer_left = this.out_mixer_left;
/* 755 */       this.last_out_mixer_right = this.out_mixer_right;
/* 756 */       this.last_out_mixer_effect1 = this.out_mixer_effect1;
/* 757 */       this.last_out_mixer_effect2 = this.out_mixer_effect2;
/* 758 */       this.started = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   void mixAudioStream(SoftAudioBuffer paramSoftAudioBuffer1, SoftAudioBuffer paramSoftAudioBuffer2, SoftAudioBuffer paramSoftAudioBuffer3, float paramFloat1, float paramFloat2)
/*     */   {
/* 766 */     int i = paramSoftAudioBuffer1.getSize();
/* 767 */     if ((paramFloat1 < 1.E-009D) && (paramFloat2 < 1.E-009D))
/*     */       return;
/*     */     float[] arrayOfFloat7;
/*     */     int n;
/* 769 */     if ((paramSoftAudioBuffer3 != null) && (this.delay != 0))
/*     */     {
/* 771 */       if (paramFloat1 == paramFloat2) {
/* 772 */         float[] arrayOfFloat1 = paramSoftAudioBuffer2.array();
/* 773 */         float[] arrayOfFloat3 = paramSoftAudioBuffer1.array();
/* 774 */         int j = 0;
/* 775 */         for (int m = this.delay; m < i; m++)
/* 776 */           arrayOfFloat1[m] += arrayOfFloat3[(j++)] * paramFloat2;
/* 777 */         arrayOfFloat1 = paramSoftAudioBuffer3.array();
/* 778 */         for (m = 0; m < this.delay; m++)
/* 779 */           arrayOfFloat1[m] += arrayOfFloat3[(j++)] * paramFloat2;
/*     */       } else {
/* 781 */         float f1 = paramFloat1;
/* 782 */         float f3 = (paramFloat2 - paramFloat1) / i;
/* 783 */         float[] arrayOfFloat5 = paramSoftAudioBuffer2.array();
/* 784 */         arrayOfFloat7 = paramSoftAudioBuffer1.array();
/* 785 */         n = 0;
/* 786 */         for (int i1 = this.delay; i1 < i; i1++) {
/* 787 */           f1 += f3;
/* 788 */           arrayOfFloat5[i1] += arrayOfFloat7[(n++)] * f1;
/*     */         }
/* 790 */         arrayOfFloat5 = paramSoftAudioBuffer3.array();
/* 791 */         for (i1 = 0; i1 < this.delay; i1++) {
/* 792 */           f1 += f3;
/* 793 */           arrayOfFloat5[i1] += arrayOfFloat7[(n++)] * f1;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/* 799 */     else if (paramFloat1 == paramFloat2) {
/* 800 */       float[] arrayOfFloat2 = paramSoftAudioBuffer2.array();
/* 801 */       float[] arrayOfFloat4 = paramSoftAudioBuffer1.array();
/* 802 */       for (int k = 0; k < i; k++)
/* 803 */         arrayOfFloat2[k] += arrayOfFloat4[k] * paramFloat2;
/*     */     } else {
/* 805 */       float f2 = paramFloat1;
/* 806 */       float f4 = (paramFloat2 - paramFloat1) / i;
/* 807 */       float[] arrayOfFloat6 = paramSoftAudioBuffer2.array();
/* 808 */       arrayOfFloat7 = paramSoftAudioBuffer1.array();
/* 809 */       for (n = 0; n < i; n++) {
/* 810 */         f2 += f4;
/* 811 */         arrayOfFloat6[n] += arrayOfFloat7[n] * f2;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void processAudioLogic(SoftAudioBuffer[] paramArrayOfSoftAudioBuffer)
/*     */   {
/* 819 */     if (!this.audiostarted) {
/* 820 */       return;
/*     */     }
/* 822 */     int i = paramArrayOfSoftAudioBuffer[0].getSize();
/*     */     try
/*     */     {
/* 825 */       this.osc_buff[0] = paramArrayOfSoftAudioBuffer[10].array();
/* 826 */       if (this.nrofchannels != 1)
/* 827 */         this.osc_buff[1] = paramArrayOfSoftAudioBuffer[11].array();
/* 828 */       int j = this.osc_stream.read(this.osc_buff, 0, i);
/* 829 */       if (j == -1) {
/* 830 */         this.stopping = true;
/* 831 */         return;
/*     */       }
/* 833 */       if (j != i) {
/* 834 */         Arrays.fill(this.osc_buff[0], j, i, 0.0F);
/* 835 */         if (this.nrofchannels != 1) {
/* 836 */           Arrays.fill(this.osc_buff[1], j, i, 0.0F);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/* 843 */     SoftAudioBuffer localSoftAudioBuffer1 = paramArrayOfSoftAudioBuffer[0];
/* 844 */     SoftAudioBuffer localSoftAudioBuffer2 = paramArrayOfSoftAudioBuffer[1];
/* 845 */     SoftAudioBuffer localSoftAudioBuffer3 = paramArrayOfSoftAudioBuffer[2];
/* 846 */     SoftAudioBuffer localSoftAudioBuffer4 = paramArrayOfSoftAudioBuffer[6];
/* 847 */     SoftAudioBuffer localSoftAudioBuffer5 = paramArrayOfSoftAudioBuffer[7];
/*     */ 
/* 849 */     SoftAudioBuffer localSoftAudioBuffer6 = paramArrayOfSoftAudioBuffer[3];
/* 850 */     SoftAudioBuffer localSoftAudioBuffer7 = paramArrayOfSoftAudioBuffer[4];
/* 851 */     SoftAudioBuffer localSoftAudioBuffer8 = paramArrayOfSoftAudioBuffer[5];
/* 852 */     SoftAudioBuffer localSoftAudioBuffer9 = paramArrayOfSoftAudioBuffer[8];
/* 853 */     SoftAudioBuffer localSoftAudioBuffer10 = paramArrayOfSoftAudioBuffer[9];
/*     */ 
/* 855 */     SoftAudioBuffer localSoftAudioBuffer11 = paramArrayOfSoftAudioBuffer[10];
/* 856 */     SoftAudioBuffer localSoftAudioBuffer12 = paramArrayOfSoftAudioBuffer[11];
/*     */ 
/* 858 */     if (this.osc_stream_nrofchannels == 1) {
/* 859 */       localSoftAudioBuffer12 = null;
/*     */     }
/* 861 */     if (!Double.isInfinite(this.co_filter_freq[0])) {
/* 862 */       this.filter_left.processAudio(localSoftAudioBuffer11);
/* 863 */       if (localSoftAudioBuffer12 != null) {
/* 864 */         this.filter_right.processAudio(localSoftAudioBuffer12);
/*     */       }
/*     */     }
/* 867 */     if (this.nrofchannels == 1) {
/* 868 */       this.out_mixer_left = ((this.out_mixer_left + this.out_mixer_right) / 2.0F);
/* 869 */       mixAudioStream(localSoftAudioBuffer11, localSoftAudioBuffer1, localSoftAudioBuffer6, this.last_out_mixer_left, this.out_mixer_left);
/* 870 */       if (localSoftAudioBuffer12 != null) {
/* 871 */         mixAudioStream(localSoftAudioBuffer12, localSoftAudioBuffer1, localSoftAudioBuffer6, this.last_out_mixer_left, this.out_mixer_left);
/*     */       }
/*     */     }
/* 874 */     else if ((localSoftAudioBuffer12 == null) && (this.last_out_mixer_left == this.last_out_mixer_right) && (this.out_mixer_left == this.out_mixer_right))
/*     */     {
/* 878 */       mixAudioStream(localSoftAudioBuffer11, localSoftAudioBuffer3, localSoftAudioBuffer8, this.last_out_mixer_left, this.out_mixer_left);
/*     */     }
/*     */     else
/*     */     {
/* 882 */       mixAudioStream(localSoftAudioBuffer11, localSoftAudioBuffer1, localSoftAudioBuffer6, this.last_out_mixer_left, this.out_mixer_left);
/* 883 */       if (localSoftAudioBuffer12 != null) {
/* 884 */         mixAudioStream(localSoftAudioBuffer12, localSoftAudioBuffer2, localSoftAudioBuffer7, this.last_out_mixer_right, this.out_mixer_right);
/*     */       }
/*     */       else {
/* 887 */         mixAudioStream(localSoftAudioBuffer11, localSoftAudioBuffer2, localSoftAudioBuffer7, this.last_out_mixer_right, this.out_mixer_right);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 892 */     if (localSoftAudioBuffer12 == null) {
/* 893 */       mixAudioStream(localSoftAudioBuffer11, localSoftAudioBuffer4, localSoftAudioBuffer9, this.last_out_mixer_effect1, this.out_mixer_effect1);
/*     */ 
/* 895 */       mixAudioStream(localSoftAudioBuffer11, localSoftAudioBuffer5, localSoftAudioBuffer10, this.last_out_mixer_effect2, this.out_mixer_effect2);
/*     */     }
/*     */     else {
/* 898 */       mixAudioStream(localSoftAudioBuffer11, localSoftAudioBuffer4, localSoftAudioBuffer9, this.last_out_mixer_effect1 * 0.5F, this.out_mixer_effect1 * 0.5F);
/*     */ 
/* 900 */       mixAudioStream(localSoftAudioBuffer11, localSoftAudioBuffer5, localSoftAudioBuffer10, this.last_out_mixer_effect2 * 0.5F, this.out_mixer_effect2 * 0.5F);
/*     */ 
/* 902 */       mixAudioStream(localSoftAudioBuffer12, localSoftAudioBuffer4, localSoftAudioBuffer9, this.last_out_mixer_effect1 * 0.5F, this.out_mixer_effect1 * 0.5F);
/*     */ 
/* 904 */       mixAudioStream(localSoftAudioBuffer12, localSoftAudioBuffer5, localSoftAudioBuffer10, this.last_out_mixer_effect2 * 0.5F, this.out_mixer_effect2 * 0.5F);
/*     */     }
/*     */ 
/* 908 */     this.last_out_mixer_left = this.out_mixer_left;
/* 909 */     this.last_out_mixer_right = this.out_mixer_right;
/* 910 */     this.last_out_mixer_effect1 = this.out_mixer_effect1;
/* 911 */     this.last_out_mixer_effect2 = this.out_mixer_effect2;
/*     */ 
/* 913 */     if (this.out_mixer_end)
/* 914 */       this.stopping = true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftVoice
 * JD-Core Version:    0.6.2
 */