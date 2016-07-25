/*      */ package com.sun.media.sound;
/*      */ 
/*      */ import [F;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ import javax.sound.midi.MidiMessage;
/*      */ import javax.sound.midi.Patch;
/*      */ import javax.sound.midi.ShortMessage;
/*      */ import javax.sound.sampled.AudioFormat;
/*      */ import javax.sound.sampled.AudioInputStream;
/*      */ 
/*      */ public final class SoftMainMixer
/*      */ {
/*      */   public static final int CHANNEL_LEFT = 0;
/*      */   public static final int CHANNEL_RIGHT = 1;
/*      */   public static final int CHANNEL_MONO = 2;
/*      */   public static final int CHANNEL_DELAY_LEFT = 3;
/*      */   public static final int CHANNEL_DELAY_RIGHT = 4;
/*      */   public static final int CHANNEL_DELAY_MONO = 5;
/*      */   public static final int CHANNEL_EFFECT1 = 6;
/*      */   public static final int CHANNEL_EFFECT2 = 7;
/*      */   public static final int CHANNEL_DELAY_EFFECT1 = 8;
/*      */   public static final int CHANNEL_DELAY_EFFECT2 = 9;
/*      */   public static final int CHANNEL_LEFT_DRY = 10;
/*      */   public static final int CHANNEL_RIGHT_DRY = 11;
/*      */   public static final int CHANNEL_SCRATCH1 = 12;
/*      */   public static final int CHANNEL_SCRATCH2 = 13;
/*   70 */   boolean active_sensing_on = false;
/*   71 */   private long msec_last_activity = -1L;
/*   72 */   private boolean pusher_silent = false;
/*   73 */   private int pusher_silent_count = 0;
/*   74 */   private long sample_pos = 0L;
/*   75 */   boolean readfully = true;
/*      */   private final Object control_mutex;
/*      */   private SoftSynthesizer synth;
/*   78 */   private float samplerate = 44100.0F;
/*   79 */   private int nrofchannels = 2;
/*   80 */   private SoftVoice[] voicestatus = null;
/*      */   private SoftAudioBuffer[] buffers;
/*      */   private SoftReverb reverb;
/*      */   private SoftAudioProcessor chorus;
/*      */   private SoftAudioProcessor agc;
/*   85 */   private long msec_buffer_len = 0L;
/*   86 */   private int buffer_len = 0;
/*   87 */   TreeMap<Long, Object> midimessages = new TreeMap();
/*   88 */   private int delay_midievent = 0;
/*   89 */   private int max_delay_midievent = 0;
/*   90 */   double last_volume_left = 1.0D;
/*   91 */   double last_volume_right = 1.0D;
/*   92 */   private double[] co_master_balance = new double[1];
/*   93 */   private double[] co_master_volume = new double[1];
/*   94 */   private double[] co_master_coarse_tuning = new double[1];
/*   95 */   private double[] co_master_fine_tuning = new double[1];
/*      */   private AudioInputStream ais;
/*   97 */   private Set<SoftChannelMixerContainer> registeredMixers = null;
/*   98 */   private Set<ModelChannelMixer> stoppedMixers = null;
/*   99 */   private SoftChannelMixerContainer[] cur_registeredMixers = null;
/*  100 */   SoftControl co_master = new SoftControl()
/*      */   {
/*  102 */     double[] balance = SoftMainMixer.this.co_master_balance;
/*  103 */     double[] volume = SoftMainMixer.this.co_master_volume;
/*  104 */     double[] coarse_tuning = SoftMainMixer.this.co_master_coarse_tuning;
/*  105 */     double[] fine_tuning = SoftMainMixer.this.co_master_fine_tuning;
/*      */ 
/*      */     public double[] get(int paramAnonymousInt, String paramAnonymousString) {
/*  108 */       if (paramAnonymousString == null)
/*  109 */         return null;
/*  110 */       if (paramAnonymousString.equals("balance"))
/*  111 */         return this.balance;
/*  112 */       if (paramAnonymousString.equals("volume"))
/*  113 */         return this.volume;
/*  114 */       if (paramAnonymousString.equals("coarse_tuning"))
/*  115 */         return this.coarse_tuning;
/*  116 */       if (paramAnonymousString.equals("fine_tuning"))
/*  117 */         return this.fine_tuning;
/*  118 */       return null;
/*      */     }
/*  100 */   };
/*      */ 
/*      */   private void processSystemExclusiveMessage(byte[] paramArrayOfByte)
/*      */   {
/*  123 */     synchronized (this.synth.control_mutex) {
/*  124 */       activity();
/*      */       int i;
/*      */       int j;
/*      */       int k;
/*      */       int i1;
/*      */       int i6;
/*  127 */       if ((paramArrayOfByte[1] & 0xFF) == 126) {
/*  128 */         i = paramArrayOfByte[2] & 0xFF;
/*  129 */         if ((i == 127) || (i == this.synth.getDeviceID())) {
/*  130 */           j = paramArrayOfByte[3] & 0xFF;
/*      */ 
/*  132 */           switch (j) {
/*      */           case 8:
/*  134 */             k = paramArrayOfByte[4] & 0xFF;
/*      */             SoftTuning localSoftTuning;
/*  135 */             switch (k)
/*      */             {
/*      */             case 1:
/*  139 */               localSoftTuning = this.synth.getTuning(new Patch(0, paramArrayOfByte[5] & 0xFF));
/*      */ 
/*  141 */               localSoftTuning.load(paramArrayOfByte);
/*  142 */               break;
/*      */             case 4:
/*      */             case 5:
/*      */             case 6:
/*      */             case 7:
/*  151 */               localSoftTuning = this.synth.getTuning(new Patch(paramArrayOfByte[5] & 0xFF, paramArrayOfByte[6] & 0xFF));
/*      */ 
/*  153 */               localSoftTuning.load(paramArrayOfByte);
/*  154 */               break;
/*      */             case 8:
/*      */             case 9:
/*  162 */               localSoftTuning = new SoftTuning(paramArrayOfByte);
/*  163 */               i1 = (paramArrayOfByte[5] & 0xFF) * 16384 + (paramArrayOfByte[6] & 0xFF) * 128 + (paramArrayOfByte[7] & 0xFF);
/*      */ 
/*  165 */               SoftChannel[] arrayOfSoftChannel1 = this.synth.channels;
/*  166 */               for (i6 = 0; i6 < arrayOfSoftChannel1.length; i6++)
/*  167 */                 if ((i1 & 1 << i6) != 0)
/*  168 */                   arrayOfSoftChannel1[i6].tuning = localSoftTuning;
/*      */             case 2:
/*      */             case 3:
/*      */             }
/*  172 */             break;
/*      */           case 9:
/*  176 */             k = paramArrayOfByte[4] & 0xFF;
/*  177 */             switch (k) {
/*      */             case 1:
/*  179 */               this.synth.setGeneralMidiMode(1);
/*  180 */               reset();
/*  181 */               break;
/*      */             case 2:
/*  183 */               this.synth.setGeneralMidiMode(0);
/*  184 */               reset();
/*  185 */               break;
/*      */             case 3:
/*  187 */               this.synth.setGeneralMidiMode(2);
/*  188 */               reset();
/*      */             }
/*      */ 
/*  191 */             break;
/*      */           case 10:
/*  195 */             k = paramArrayOfByte[4] & 0xFF;
/*  196 */             switch (k) {
/*      */             case 1:
/*  198 */               if (this.synth.getGeneralMidiMode() == 0)
/*  199 */                 this.synth.setGeneralMidiMode(1);
/*  200 */               this.synth.voice_allocation_mode = 1;
/*  201 */               reset();
/*  202 */               break;
/*      */             case 2:
/*  204 */               this.synth.setGeneralMidiMode(0);
/*  205 */               this.synth.voice_allocation_mode = 0;
/*  206 */               reset();
/*  207 */               break;
/*      */             case 3:
/*  209 */               this.synth.voice_allocation_mode = 0;
/*  210 */               break;
/*      */             case 4:
/*  212 */               this.synth.voice_allocation_mode = 1;
/*      */             }
/*      */ 
/*  215 */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  226 */       if ((paramArrayOfByte[1] & 0xFF) == 127) {
/*  227 */         i = paramArrayOfByte[2] & 0xFF;
/*  228 */         if ((i == 127) || (i == this.synth.getDeviceID())) {
/*  229 */           j = paramArrayOfByte[3] & 0xFF;
/*      */           int i4;
/*      */           int i8;
/*      */           Object localObject1;
/*      */           int i7;
/*      */           int i10;
/*  231 */           switch (j)
/*      */           {
/*      */           case 4:
/*  234 */             k = paramArrayOfByte[4] & 0xFF;
/*  235 */             switch (k) {
/*      */             case 1:
/*      */             case 2:
/*      */             case 3:
/*      */             case 4:
/*  240 */               int m = (paramArrayOfByte[5] & 0x7F) + (paramArrayOfByte[6] & 0x7F) * 128;
/*      */ 
/*  242 */               if (k == 1)
/*  243 */                 setVolume(m);
/*  244 */               else if (k == 2)
/*  245 */                 setBalance(m);
/*  246 */               else if (k == 3)
/*  247 */                 setFineTuning(m);
/*  248 */               else if (k == 4)
/*  249 */                 setCoarseTuning(m); break;
/*      */             case 5:
/*  252 */               i1 = 5;
/*  253 */               i4 = paramArrayOfByte[(i1++)] & 0xFF;
/*  254 */               i6 = paramArrayOfByte[(i1++)] & 0xFF;
/*  255 */               i8 = paramArrayOfByte[(i1++)] & 0xFF;
/*  256 */               int[] arrayOfInt2 = new int[i4];
/*  257 */               for (int i11 = 0; i11 < i4; i11++) {
/*  258 */                 int i12 = paramArrayOfByte[(i1++)] & 0xFF;
/*  259 */                 int i13 = paramArrayOfByte[(i1++)] & 0xFF;
/*  260 */                 arrayOfInt2[i11] = (i12 * 128 + i13);
/*      */               }
/*  262 */               i11 = (paramArrayOfByte.length - 1 - i1) / (i6 + i8);
/*      */ 
/*  264 */               long[] arrayOfLong1 = new long[i11];
/*  265 */               long[] arrayOfLong2 = new long[i11];
/*  266 */               for (int i14 = 0; i14 < i11; i14++) {
/*  267 */                 arrayOfLong2[i14] = 0L;
/*  268 */                 for (int i15 = 0; i15 < i6; i15++) {
/*  269 */                   arrayOfLong1[i14] = (arrayOfLong1[i14] * 128L + (paramArrayOfByte[(i1++)] & 0xFF));
/*      */                 }
/*  271 */                 for (i15 = 0; i15 < i8; i15++) {
/*  272 */                   arrayOfLong2[i14] = (arrayOfLong2[i14] * 128L + (paramArrayOfByte[(i1++)] & 0xFF));
/*      */                 }
/*      */               }
/*      */ 
/*  276 */               globalParameterControlChange(arrayOfInt2, arrayOfLong1, arrayOfLong2);
/*      */             }
/*      */ 
/*  281 */             break;
/*      */           case 8:
/*  284 */             k = paramArrayOfByte[4] & 0xFF;
/*      */             SoftVoice[] arrayOfSoftVoice1;
/*  285 */             switch (k)
/*      */             {
/*      */             case 2:
/*  289 */               localObject1 = this.synth.getTuning(new Patch(0, paramArrayOfByte[5] & 0xFF));
/*      */ 
/*  291 */               ((SoftTuning)localObject1).load(paramArrayOfByte);
/*  292 */               arrayOfSoftVoice1 = this.synth.getVoices();
/*  293 */               for (i4 = 0; i4 < arrayOfSoftVoice1.length; i4++)
/*  294 */                 if ((arrayOfSoftVoice1[i4].active) && 
/*  295 */                   (arrayOfSoftVoice1[i4].tuning == localObject1))
/*  296 */                   arrayOfSoftVoice1[i4].updateTuning((SoftTuning)localObject1);
/*  297 */               break;
/*      */             case 7:
/*  303 */               localObject1 = this.synth.getTuning(new Patch(paramArrayOfByte[5] & 0xFF, paramArrayOfByte[6] & 0xFF));
/*      */ 
/*  305 */               ((SoftTuning)localObject1).load(paramArrayOfByte);
/*  306 */               arrayOfSoftVoice1 = this.synth.getVoices();
/*  307 */               for (i4 = 0; i4 < arrayOfSoftVoice1.length; i4++)
/*  308 */                 if ((arrayOfSoftVoice1[i4].active) && 
/*  309 */                   (arrayOfSoftVoice1[i4].tuning == localObject1))
/*  310 */                   arrayOfSoftVoice1[i4].updateTuning((SoftTuning)localObject1);
/*  311 */               break;
/*      */             case 8:
/*      */             case 9:
/*  319 */               localObject1 = new SoftTuning(paramArrayOfByte);
/*  320 */               int i2 = (paramArrayOfByte[5] & 0xFF) * 16384 + (paramArrayOfByte[6] & 0xFF) * 128 + (paramArrayOfByte[7] & 0xFF);
/*      */ 
/*  322 */               SoftChannel[] arrayOfSoftChannel2 = this.synth.channels;
/*  323 */               for (i6 = 0; i6 < arrayOfSoftChannel2.length; i6++)
/*  324 */                 if ((i2 & 1 << i6) != 0)
/*  325 */                   arrayOfSoftChannel2[i6].tuning = ((SoftTuning)localObject1);
/*  326 */               SoftVoice[] arrayOfSoftVoice2 = this.synth.getVoices();
/*  327 */               for (i8 = 0; i8 < arrayOfSoftVoice2.length; i8++)
/*  328 */                 if ((arrayOfSoftVoice2[i8].active) && 
/*  329 */                   ((i2 & 1 << arrayOfSoftVoice2[i8].channel) != 0))
/*  330 */                   arrayOfSoftVoice2[i8].updateTuning((SoftTuning)localObject1);  case 3:
/*      */             case 4:
/*      */             case 5:
/*      */             case 6:
/*  334 */             }break;
/*      */           case 9:
/*  338 */             k = paramArrayOfByte[4] & 0xFF;
/*      */             int[] arrayOfInt1;
/*      */             int i5;
/*      */             SoftChannel localSoftChannel2;
/*  339 */             switch (k)
/*      */             {
/*      */             case 1:
/*  342 */               localObject1 = new int[(paramArrayOfByte.length - 7) / 2];
/*  343 */               arrayOfInt1 = new int[(paramArrayOfByte.length - 7) / 2];
/*  344 */               i5 = 0;
/*  345 */               for (i7 = 6; i7 < paramArrayOfByte.length - 1; i7 += 2) {
/*  346 */                 paramArrayOfByte[i7] &= 255;
/*  347 */                 arrayOfInt1[i5] = (paramArrayOfByte[(i7 + 1)] & 0xFF);
/*  348 */                 i5++;
/*      */               }
/*  350 */               i7 = paramArrayOfByte[5] & 0xFF;
/*  351 */               localSoftChannel2 = this.synth.channels[i7];
/*  352 */               localSoftChannel2.mapChannelPressureToDestination((int[])localObject1, arrayOfInt1);
/*      */ 
/*  354 */               break;
/*      */             case 2:
/*  358 */               localObject1 = new int[(paramArrayOfByte.length - 7) / 2];
/*  359 */               arrayOfInt1 = new int[(paramArrayOfByte.length - 7) / 2];
/*  360 */               i5 = 0;
/*  361 */               for (i7 = 6; i7 < paramArrayOfByte.length - 1; i7 += 2) {
/*  362 */                 paramArrayOfByte[i7] &= 255;
/*  363 */                 arrayOfInt1[i5] = (paramArrayOfByte[(i7 + 1)] & 0xFF);
/*  364 */                 i5++;
/*      */               }
/*  366 */               i7 = paramArrayOfByte[5] & 0xFF;
/*  367 */               localSoftChannel2 = this.synth.channels[i7];
/*  368 */               localSoftChannel2.mapPolyPressureToDestination((int[])localObject1, arrayOfInt1);
/*      */ 
/*  370 */               break;
/*      */             case 3:
/*  374 */               localObject1 = new int[(paramArrayOfByte.length - 7) / 2];
/*  375 */               arrayOfInt1 = new int[(paramArrayOfByte.length - 7) / 2];
/*  376 */               i5 = 0;
/*  377 */               for (i7 = 7; i7 < paramArrayOfByte.length - 1; i7 += 2) {
/*  378 */                 paramArrayOfByte[i7] &= 255;
/*  379 */                 arrayOfInt1[i5] = (paramArrayOfByte[(i7 + 1)] & 0xFF);
/*  380 */                 i5++;
/*      */               }
/*  382 */               i7 = paramArrayOfByte[5] & 0xFF;
/*  383 */               localSoftChannel2 = this.synth.channels[i7];
/*  384 */               i10 = paramArrayOfByte[6] & 0xFF;
/*  385 */               localSoftChannel2.mapControlToDestination(i10, (int[])localObject1, arrayOfInt1);
/*      */             }
/*      */ 
/*  390 */             break;
/*      */           case 10:
/*  396 */             k = paramArrayOfByte[4] & 0xFF;
/*  397 */             switch (k) {
/*      */             case 1:
/*  399 */               int n = paramArrayOfByte[5] & 0xFF;
/*  400 */               int i3 = paramArrayOfByte[6] & 0xFF;
/*  401 */               SoftChannel localSoftChannel1 = this.synth.channels[n];
/*  402 */               for (i7 = 7; i7 < paramArrayOfByte.length - 1; i7 += 2) {
/*  403 */                 int i9 = paramArrayOfByte[i7] & 0xFF;
/*  404 */                 i10 = paramArrayOfByte[(i7 + 1)] & 0xFF;
/*  405 */                 localSoftChannel1.controlChangePerNote(i3, i9, i10);
/*      */               }
/*      */ 
/*  408 */               break;
/*      */             }
/*      */           case 5:
/*      */           case 6:
/*      */           case 7:
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void processMessages(long paramLong)
/*      */   {
/*  424 */     Iterator localIterator = this.midimessages.entrySet().iterator();
/*  425 */     while (localIterator.hasNext()) {
/*  426 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/*  427 */       if (((Long)localEntry.getKey()).longValue() >= paramLong + this.msec_buffer_len)
/*  428 */         return;
/*  429 */       long l = ((Long)localEntry.getKey()).longValue() - paramLong;
/*  430 */       this.delay_midievent = ((int)(l * (this.samplerate / 1000000.0D) + 0.5D));
/*  431 */       if (this.delay_midievent > this.max_delay_midievent)
/*  432 */         this.delay_midievent = this.max_delay_midievent;
/*  433 */       if (this.delay_midievent < 0)
/*  434 */         this.delay_midievent = 0;
/*  435 */       processMessage(localEntry.getValue());
/*  436 */       localIterator.remove();
/*      */     }
/*  438 */     this.delay_midievent = 0;
/*      */   }
/*      */ 
/*      */   void processAudioBuffers()
/*      */   {
/*  443 */     if ((this.synth.weakstream != null) && (this.synth.weakstream.silent_samples != 0L))
/*      */     {
/*  445 */       this.sample_pos += this.synth.weakstream.silent_samples;
/*  446 */       this.synth.weakstream.silent_samples = 0L;
/*      */     }
/*      */ 
/*  449 */     for (int i = 0; i < this.buffers.length; i++) {
/*  450 */       if ((i != 3) && (i != 4) && (i != 5) && (i != 8) && (i != 9))
/*      */       {
/*  455 */         this.buffers[i].clear();
/*      */       }
/*      */     }
/*  458 */     if (!this.buffers[3].isSilent())
/*      */     {
/*  460 */       this.buffers[0].swap(this.buffers[3]);
/*      */     }
/*  462 */     if (!this.buffers[4].isSilent())
/*      */     {
/*  464 */       this.buffers[1].swap(this.buffers[4]);
/*      */     }
/*  466 */     if (!this.buffers[5].isSilent())
/*      */     {
/*  468 */       this.buffers[2].swap(this.buffers[5]);
/*      */     }
/*  470 */     if (!this.buffers[8].isSilent())
/*      */     {
/*  472 */       this.buffers[6].swap(this.buffers[8]);
/*      */     }
/*  474 */     if (!this.buffers[9].isSilent())
/*      */     {
/*  476 */       this.buffers[7].swap(this.buffers[9]);
/*      */     }
/*      */     double d1;
/*      */     double d2;
/*      */     SoftChannelMixerContainer[] arrayOfSoftChannelMixerContainer1;
/*  485 */     synchronized (this.control_mutex)
/*      */     {
/*  487 */       long l = ()(this.sample_pos * (1000000.0D / this.samplerate));
/*      */ 
/*  489 */       processMessages(l);
/*      */ 
/*  491 */       if (this.active_sensing_on)
/*      */       {
/*  495 */         if (l - this.msec_last_activity > 1000000L) {
/*  496 */           this.active_sensing_on = false;
/*  497 */           for (SoftChannel localSoftChannel : this.synth.channels) {
/*  498 */             localSoftChannel.allSoundOff();
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  503 */       for (int n = 0; n < this.voicestatus.length; n++)
/*  504 */         if (this.voicestatus[n].active)
/*  505 */           this.voicestatus[n].processControlLogic();
/*  506 */       this.sample_pos += this.buffer_len;
/*      */ 
/*  508 */       double d3 = this.co_master_volume[0];
/*  509 */       d1 = d3;
/*  510 */       d2 = d3;
/*      */ 
/*  512 */       double d4 = this.co_master_balance[0];
/*  513 */       if (d4 > 0.5D)
/*  514 */         d1 *= (1.0D - d4) * 2.0D;
/*      */       else {
/*  516 */         d2 *= d4 * 2.0D;
/*      */       }
/*  518 */       this.chorus.processControlLogic();
/*  519 */       this.reverb.processControlLogic();
/*  520 */       this.agc.processControlLogic();
/*      */ 
/*  522 */       if ((this.cur_registeredMixers == null) && 
/*  523 */         (this.registeredMixers != null)) {
/*  524 */         this.cur_registeredMixers = new SoftChannelMixerContainer[this.registeredMixers.size()];
/*      */ 
/*  526 */         this.registeredMixers.toArray(this.cur_registeredMixers);
/*      */       }
/*      */ 
/*  530 */       arrayOfSoftChannelMixerContainer1 = this.cur_registeredMixers;
/*  531 */       if ((arrayOfSoftChannelMixerContainer1 != null) && 
/*  532 */         (arrayOfSoftChannelMixerContainer1.length == 0))
/*  533 */         arrayOfSoftChannelMixerContainer1 = null;
/*      */     }
/*      */     Object localObject1;
/*      */     Object localObject2;
/*  537 */     if (arrayOfSoftChannelMixerContainer1 != null)
/*      */     {
/*  540 */       ??? = this.buffers[0];
/*  541 */       localObject1 = this.buffers[1];
/*  542 */       SoftAudioBuffer localSoftAudioBuffer1 = this.buffers[2];
/*  543 */       localObject2 = this.buffers[3];
/*  544 */       SoftAudioBuffer localSoftAudioBuffer2 = this.buffers[4];
/*  545 */       SoftAudioBuffer localSoftAudioBuffer3 = this.buffers[5];
/*      */ 
/*  547 */       int i7 = this.buffers[0].getSize();
/*      */ 
/*  549 */       float[][] arrayOfFloat2 = new float[this.nrofchannels][];
/*  550 */       float[][] arrayOfFloat3 = new float[this.nrofchannels][];
/*  551 */       arrayOfFloat3[0] = ((SoftAudioBuffer)???).array();
/*  552 */       if (this.nrofchannels != 1) {
/*  553 */         arrayOfFloat3[1] = ((SoftAudioBuffer)localObject1).array();
/*      */       }
/*  555 */       for (SoftChannelMixerContainer localSoftChannelMixerContainer : arrayOfSoftChannelMixerContainer1)
/*      */       {
/*  559 */         this.buffers[0] = localSoftChannelMixerContainer.buffers[0];
/*  560 */         this.buffers[1] = localSoftChannelMixerContainer.buffers[1];
/*  561 */         this.buffers[2] = localSoftChannelMixerContainer.buffers[2];
/*  562 */         this.buffers[3] = localSoftChannelMixerContainer.buffers[3];
/*  563 */         this.buffers[4] = localSoftChannelMixerContainer.buffers[4];
/*  564 */         this.buffers[5] = localSoftChannelMixerContainer.buffers[5];
/*      */ 
/*  566 */         this.buffers[0].clear();
/*  567 */         this.buffers[1].clear();
/*  568 */         this.buffers[2].clear();
/*      */ 
/*  570 */         if (!this.buffers[3].isSilent())
/*      */         {
/*  572 */           this.buffers[0].swap(this.buffers[3]);
/*      */         }
/*  574 */         if (!this.buffers[4].isSilent())
/*      */         {
/*  576 */           this.buffers[1].swap(this.buffers[4]);
/*      */         }
/*  578 */         if (!this.buffers[5].isSilent())
/*      */         {
/*  580 */           this.buffers[2].swap(this.buffers[5]);
/*      */         }
/*      */ 
/*  583 */         arrayOfFloat2[0] = this.buffers[0].array();
/*  584 */         if (this.nrofchannels != 1) {
/*  585 */           arrayOfFloat2[1] = this.buffers[1].array();
/*      */         }
/*  587 */         int i10 = 0;
/*  588 */         for (int i11 = 0; i11 < this.voicestatus.length; i11++)
/*  589 */           if ((this.voicestatus[i11].active) && 
/*  590 */             (this.voicestatus[i11].channelmixer == localSoftChannelMixerContainer.mixer)) {
/*  591 */             this.voicestatus[i11].processAudioLogic(this.buffers);
/*  592 */             i10 = 1;
/*      */           }
/*      */         Object localObject5;
/*      */         int i14;
/*  595 */         if (!this.buffers[2].isSilent())
/*      */         {
/*  597 */           float[] arrayOfFloat4 = this.buffers[2].array();
/*  598 */           localObject5 = this.buffers[0].array();
/*  599 */           if (this.nrofchannels != 1) {
/*  600 */             float[] arrayOfFloat5 = this.buffers[1].array();
/*  601 */             for (i14 = 0; i14 < i7; i14++) {
/*  602 */               float f4 = arrayOfFloat4[i14];
/*  603 */               localObject5[i14] += f4;
/*  604 */               arrayOfFloat5[i14] += f4;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  609 */             for (int i13 = 0; i13 < i7; i13++) {
/*  610 */               localObject5[i13] += arrayOfFloat4[i13];
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  615 */         if (!localSoftChannelMixerContainer.mixer.process(arrayOfFloat2, 0, i7)) {
/*  616 */           synchronized (this.control_mutex) {
/*  617 */             this.registeredMixers.remove(localSoftChannelMixerContainer);
/*  618 */             this.cur_registeredMixers = null;
/*      */           }
/*      */         }
/*      */ 
/*  622 */         for (int i12 = 0; i12 < arrayOfFloat2.length; i12++) {
/*  623 */           localObject5 = arrayOfFloat2[i12];
/*  624 */           [F local[F = arrayOfFloat3[i12];
/*  625 */           for (i14 = 0; i14 < i7; i14++) {
/*  626 */             local[F[i14] += localObject5[i14];
/*      */           }
/*      */         }
/*  629 */         if (i10 == 0) {
/*  630 */           synchronized (this.control_mutex) {
/*  631 */             if ((this.stoppedMixers != null) && 
/*  632 */               (this.stoppedMixers.contains(localSoftChannelMixerContainer))) {
/*  633 */               this.stoppedMixers.remove(localSoftChannelMixerContainer);
/*  634 */               localSoftChannelMixerContainer.mixer.stop();
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  642 */       this.buffers[0] = ???;
/*  643 */       this.buffers[1] = localObject1;
/*  644 */       this.buffers[2] = localSoftAudioBuffer1;
/*  645 */       this.buffers[3] = localObject2;
/*  646 */       this.buffers[4] = localSoftAudioBuffer2;
/*  647 */       this.buffers[5] = localSoftAudioBuffer3;
/*      */     }
/*      */ 
/*  651 */     for (int j = 0; j < this.voicestatus.length; j++)
/*  652 */       if ((this.voicestatus[j].active) && 
/*  653 */         (this.voicestatus[j].channelmixer == null))
/*  654 */         this.voicestatus[j].processAudioLogic(this.buffers);
/*      */     float[] arrayOfFloat1;
/*      */     int m;
/*  656 */     if (!this.buffers[2].isSilent())
/*      */     {
/*  658 */       arrayOfFloat1 = this.buffers[2].array();
/*  659 */       localObject1 = this.buffers[0].array();
/*  660 */       m = this.buffers[0].getSize();
/*  661 */       if (this.nrofchannels != 1) {
/*  662 */         localObject2 = this.buffers[1].array();
/*  663 */         for (int i3 = 0; i3 < m; i3++) {
/*  664 */           float f3 = arrayOfFloat1[i3];
/*  665 */           localObject1[i3] += f3;
/*  666 */           localObject2[i3] += f3;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  671 */         for (int i1 = 0; i1 < m; i1++) {
/*  672 */           localObject1[i1] += arrayOfFloat1[i1];
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  678 */     if (this.synth.chorus_on) {
/*  679 */       this.chorus.processAudio();
/*      */     }
/*  681 */     if (this.synth.reverb_on) {
/*  682 */       this.reverb.processAudio();
/*      */     }
/*  684 */     if (this.nrofchannels == 1)
/*  685 */       d1 = (d1 + d2) / 2.0D;
/*      */     float f1;
/*  688 */     if ((this.last_volume_left != d1) || (this.last_volume_right != d2)) {
/*  689 */       arrayOfFloat1 = this.buffers[0].array();
/*  690 */       localObject1 = this.buffers[1].array();
/*  691 */       m = this.buffers[0].getSize();
/*      */ 
/*  695 */       f1 = (float)(this.last_volume_left * this.last_volume_left);
/*  696 */       float f2 = (float)((d1 * d1 - f1) / m);
/*  697 */       for (int i6 = 0; i6 < m; i6++) {
/*  698 */         f1 += f2;
/*  699 */         arrayOfFloat1[i6] *= f1;
/*      */       }
/*  701 */       if (this.nrofchannels != 1) {
/*  702 */         f1 = (float)(this.last_volume_right * this.last_volume_right);
/*  703 */         f2 = (float)((d2 * d2 - f1) / m);
/*  704 */         for (i6 = 0; i6 < m; i6++) {
/*  705 */           f1 += f2;
/*      */           int tmp1770_1768 = i6;
/*      */           Object tmp1770_1766 = localObject1; tmp1770_1766[tmp1770_1768] = ((float)(tmp1770_1766[tmp1770_1768] * d2));
/*      */         }
/*      */       }
/*  709 */       this.last_volume_left = d1;
/*  710 */       this.last_volume_right = d2;
/*      */     }
/*  713 */     else if ((d1 != 1.0D) || (d2 != 1.0D)) {
/*  714 */       arrayOfFloat1 = this.buffers[0].array();
/*  715 */       localObject1 = this.buffers[1].array();
/*  716 */       m = this.buffers[0].getSize();
/*      */ 
/*  718 */       f1 = (float)(d1 * d1);
/*  719 */       for (int i4 = 0; i4 < m; i4++)
/*  720 */         arrayOfFloat1[i4] *= f1;
/*  721 */       if (this.nrofchannels != 1) {
/*  722 */         f1 = (float)(d2 * d2);
/*  723 */         for (i4 = 0; i4 < m; i4++) {
/*  724 */           localObject1[i4] *= f1;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  730 */     if ((this.buffers[0].isSilent()) && (this.buffers[1].isSilent()))
/*      */     {
/*      */       int k;
/*  735 */       synchronized (this.control_mutex) {
/*  736 */         k = this.midimessages.size();
/*      */       }
/*      */ 
/*  739 */       if (k == 0)
/*      */       {
/*  741 */         this.pusher_silent_count += 1;
/*  742 */         if (this.pusher_silent_count > 5)
/*      */         {
/*  744 */           this.pusher_silent_count = 0;
/*  745 */           synchronized (this.control_mutex) {
/*  746 */             this.pusher_silent = true;
/*  747 */             if (this.synth.weakstream != null)
/*  748 */               this.synth.weakstream.setInputStream(null);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  754 */       this.pusher_silent_count = 0;
/*      */     }
/*  756 */     if (this.synth.agc_on)
/*  757 */       this.agc.processAudio();
/*      */   }
/*      */ 
/*      */   public void activity()
/*      */   {
/*  764 */     long l = 0L;
/*  765 */     if (this.pusher_silent)
/*      */     {
/*  767 */       this.pusher_silent = false;
/*  768 */       if (this.synth.weakstream != null)
/*      */       {
/*  770 */         this.synth.weakstream.setInputStream(this.ais);
/*  771 */         l = this.synth.weakstream.silent_samples;
/*      */       }
/*      */     }
/*  774 */     this.msec_last_activity = (()((this.sample_pos + l) * (1000000.0D / this.samplerate)));
/*      */   }
/*      */ 
/*      */   public void stopMixer(ModelChannelMixer paramModelChannelMixer)
/*      */   {
/*  779 */     if (this.stoppedMixers == null)
/*  780 */       this.stoppedMixers = new HashSet();
/*  781 */     this.stoppedMixers.add(paramModelChannelMixer);
/*      */   }
/*      */ 
/*      */   public void registerMixer(ModelChannelMixer paramModelChannelMixer) {
/*  785 */     if (this.registeredMixers == null)
/*  786 */       this.registeredMixers = new HashSet();
/*  787 */     SoftChannelMixerContainer localSoftChannelMixerContainer = new SoftChannelMixerContainer(null);
/*  788 */     localSoftChannelMixerContainer.buffers = new SoftAudioBuffer[6];
/*  789 */     for (int i = 0; i < localSoftChannelMixerContainer.buffers.length; i++) {
/*  790 */       localSoftChannelMixerContainer.buffers[i] = new SoftAudioBuffer(this.buffer_len, this.synth.getFormat());
/*      */     }
/*      */ 
/*  793 */     localSoftChannelMixerContainer.mixer = paramModelChannelMixer;
/*  794 */     this.registeredMixers.add(localSoftChannelMixerContainer);
/*  795 */     this.cur_registeredMixers = null;
/*      */   }
/*      */ 
/*      */   public SoftMainMixer(SoftSynthesizer paramSoftSynthesizer) {
/*  799 */     this.synth = paramSoftSynthesizer;
/*      */ 
/*  801 */     this.sample_pos = 0L;
/*      */ 
/*  803 */     this.co_master_balance[0] = 0.5D;
/*  804 */     this.co_master_volume[0] = 1.0D;
/*  805 */     this.co_master_coarse_tuning[0] = 0.5D;
/*  806 */     this.co_master_fine_tuning[0] = 0.5D;
/*      */ 
/*  808 */     this.msec_buffer_len = (()(1000000.0D / paramSoftSynthesizer.getControlRate()));
/*  809 */     this.samplerate = paramSoftSynthesizer.getFormat().getSampleRate();
/*  810 */     this.nrofchannels = paramSoftSynthesizer.getFormat().getChannels();
/*      */ 
/*  812 */     int i = (int)(paramSoftSynthesizer.getFormat().getSampleRate() / paramSoftSynthesizer.getControlRate());
/*      */ 
/*  815 */     this.buffer_len = i;
/*      */ 
/*  817 */     this.max_delay_midievent = i;
/*      */ 
/*  819 */     this.control_mutex = paramSoftSynthesizer.control_mutex;
/*  820 */     this.buffers = new SoftAudioBuffer[14];
/*  821 */     for (int j = 0; j < this.buffers.length; j++) {
/*  822 */       this.buffers[j] = new SoftAudioBuffer(i, paramSoftSynthesizer.getFormat());
/*      */     }
/*  824 */     this.voicestatus = paramSoftSynthesizer.getVoices();
/*      */ 
/*  826 */     this.reverb = new SoftReverb();
/*  827 */     this.chorus = new SoftChorus();
/*  828 */     this.agc = new SoftLimiter();
/*      */ 
/*  830 */     float f1 = paramSoftSynthesizer.getFormat().getSampleRate();
/*  831 */     float f2 = paramSoftSynthesizer.getControlRate();
/*  832 */     this.reverb.init(f1, f2);
/*  833 */     this.chorus.init(f1, f2);
/*  834 */     this.agc.init(f1, f2);
/*      */ 
/*  836 */     this.reverb.setLightMode(paramSoftSynthesizer.reverb_light);
/*      */ 
/*  838 */     this.reverb.setMixMode(true);
/*  839 */     this.chorus.setMixMode(true);
/*  840 */     this.agc.setMixMode(false);
/*      */ 
/*  842 */     this.chorus.setInput(0, this.buffers[7]);
/*  843 */     this.chorus.setOutput(0, this.buffers[0]);
/*  844 */     if (this.nrofchannels != 1)
/*  845 */       this.chorus.setOutput(1, this.buffers[1]);
/*  846 */     this.chorus.setOutput(2, this.buffers[6]);
/*      */ 
/*  848 */     this.reverb.setInput(0, this.buffers[6]);
/*  849 */     this.reverb.setOutput(0, this.buffers[0]);
/*  850 */     if (this.nrofchannels != 1) {
/*  851 */       this.reverb.setOutput(1, this.buffers[1]);
/*      */     }
/*  853 */     this.agc.setInput(0, this.buffers[0]);
/*  854 */     if (this.nrofchannels != 1)
/*  855 */       this.agc.setInput(1, this.buffers[1]);
/*  856 */     this.agc.setOutput(0, this.buffers[0]);
/*  857 */     if (this.nrofchannels != 1) {
/*  858 */       this.agc.setOutput(1, this.buffers[1]);
/*      */     }
/*  860 */     InputStream local2 = new InputStream()
/*      */     {
/*  862 */       private final SoftAudioBuffer[] buffers = SoftMainMixer.this.buffers;
/*  863 */       private final int nrofchannels = SoftMainMixer.this.synth.getFormat().getChannels();
/*      */ 
/*  865 */       private final int buffersize = this.buffers[0].getSize();
/*  866 */       private final byte[] bbuffer = new byte[this.buffersize * (SoftMainMixer.this.synth.getFormat().getSampleSizeInBits() / 8) * this.nrofchannels];
/*      */ 
/*  870 */       private int bbuffer_pos = 0;
/*  871 */       private final byte[] single = new byte[1];
/*      */ 
/*      */       public void fillBuffer()
/*      */       {
/*  880 */         SoftMainMixer.this.processAudioBuffers();
/*  881 */         for (int i = 0; i < this.nrofchannels; i++)
/*  882 */           this.buffers[i].get(this.bbuffer, i);
/*  883 */         this.bbuffer_pos = 0;
/*      */       }
/*      */ 
/*      */       public int read(byte[] paramAnonymousArrayOfByte, int paramAnonymousInt1, int paramAnonymousInt2) {
/*  887 */         int i = this.bbuffer.length;
/*  888 */         int j = paramAnonymousInt1 + paramAnonymousInt2;
/*  889 */         int k = paramAnonymousInt1;
/*  890 */         byte[] arrayOfByte = this.bbuffer;
/*  891 */         while (paramAnonymousInt1 < j) {
/*  892 */           if (available() == 0) {
/*  893 */             fillBuffer();
/*      */           } else {
/*  895 */             int m = this.bbuffer_pos;
/*  896 */             while ((paramAnonymousInt1 < j) && (m < i))
/*  897 */               paramAnonymousArrayOfByte[(paramAnonymousInt1++)] = arrayOfByte[(m++)];
/*  898 */             this.bbuffer_pos = m;
/*  899 */             if (!SoftMainMixer.this.readfully)
/*  900 */               return paramAnonymousInt1 - k;
/*      */           }
/*      */         }
/*  903 */         return paramAnonymousInt2;
/*      */       }
/*      */ 
/*      */       public int read() throws IOException {
/*  907 */         int i = read(this.single);
/*  908 */         if (i == -1)
/*  909 */           return -1;
/*  910 */         return this.single[0] & 0xFF;
/*      */       }
/*      */ 
/*      */       public int available() {
/*  914 */         return this.bbuffer.length - this.bbuffer_pos;
/*      */       }
/*      */ 
/*      */       public void close() {
/*  918 */         SoftMainMixer.this.synth.close();
/*      */       }
/*      */     };
/*  922 */     this.ais = new AudioInputStream(local2, paramSoftSynthesizer.getFormat(), -1L);
/*      */   }
/*      */ 
/*      */   public AudioInputStream getInputStream()
/*      */   {
/*  927 */     return this.ais;
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  932 */     SoftChannel[] arrayOfSoftChannel = this.synth.channels;
/*  933 */     for (int i = 0; i < arrayOfSoftChannel.length; i++) {
/*  934 */       arrayOfSoftChannel[i].allSoundOff();
/*  935 */       arrayOfSoftChannel[i].resetAllControllers(true);
/*      */ 
/*  937 */       if (this.synth.getGeneralMidiMode() == 2) {
/*  938 */         if (i == 9)
/*  939 */           arrayOfSoftChannel[i].programChange(0, 15360);
/*      */         else
/*  941 */           arrayOfSoftChannel[i].programChange(0, 15488);
/*      */       }
/*  943 */       else arrayOfSoftChannel[i].programChange(0, 0);
/*      */     }
/*  945 */     setVolume(16383);
/*  946 */     setBalance(8192);
/*  947 */     setCoarseTuning(8192);
/*  948 */     setFineTuning(8192);
/*      */ 
/*  950 */     globalParameterControlChange(new int[] { 129 }, new long[] { 0L }, new long[] { 4L });
/*      */ 
/*  953 */     globalParameterControlChange(new int[] { 130 }, new long[] { 0L }, new long[] { 2L });
/*      */   }
/*      */ 
/*      */   public void setVolume(int paramInt)
/*      */   {
/*  958 */     synchronized (this.control_mutex) {
/*  959 */       this.co_master_volume[0] = (paramInt / 16384.0D);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setBalance(int paramInt) {
/*  964 */     synchronized (this.control_mutex) {
/*  965 */       this.co_master_balance[0] = (paramInt / 16384.0D);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFineTuning(int paramInt) {
/*  970 */     synchronized (this.control_mutex) {
/*  971 */       this.co_master_fine_tuning[0] = (paramInt / 16384.0D);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setCoarseTuning(int paramInt) {
/*  976 */     synchronized (this.control_mutex) {
/*  977 */       this.co_master_coarse_tuning[0] = (paramInt / 16384.0D);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getVolume() {
/*  982 */     synchronized (this.control_mutex) {
/*  983 */       return (int)(this.co_master_volume[0] * 16384.0D);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getBalance() {
/*  988 */     synchronized (this.control_mutex) {
/*  989 */       return (int)(this.co_master_balance[0] * 16384.0D);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getFineTuning() {
/*  994 */     synchronized (this.control_mutex) {
/*  995 */       return (int)(this.co_master_fine_tuning[0] * 16384.0D);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getCoarseTuning() {
/* 1000 */     synchronized (this.control_mutex) {
/* 1001 */       return (int)(this.co_master_coarse_tuning[0] * 16384.0D);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void globalParameterControlChange(int[] paramArrayOfInt, long[] paramArrayOfLong1, long[] paramArrayOfLong2)
/*      */   {
/* 1007 */     if (paramArrayOfInt.length == 0) {
/* 1008 */       return;
/*      */     }
/* 1010 */     synchronized (this.control_mutex)
/*      */     {
/*      */       int i;
/* 1014 */       if (paramArrayOfInt[0] == 129) {
/* 1015 */         for (i = 0; i < paramArrayOfLong2.length; i++) {
/* 1016 */           this.reverb.globalParameterControlChange(paramArrayOfInt, paramArrayOfLong1[i], paramArrayOfLong2[i]);
/*      */         }
/*      */       }
/*      */ 
/* 1020 */       if (paramArrayOfInt[0] == 130)
/* 1021 */         for (i = 0; i < paramArrayOfLong2.length; i++)
/* 1022 */           this.chorus.globalParameterControlChange(paramArrayOfInt, paramArrayOfLong1[i], paramArrayOfLong2[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void processMessage(Object paramObject)
/*      */   {
/* 1032 */     if ((paramObject instanceof byte[]))
/* 1033 */       processMessage((byte[])paramObject);
/* 1034 */     if ((paramObject instanceof MidiMessage))
/* 1035 */       processMessage((MidiMessage)paramObject);
/*      */   }
/*      */ 
/*      */   public void processMessage(MidiMessage paramMidiMessage) {
/* 1039 */     if ((paramMidiMessage instanceof ShortMessage)) {
/* 1040 */       ShortMessage localShortMessage = (ShortMessage)paramMidiMessage;
/* 1041 */       processMessage(localShortMessage.getChannel(), localShortMessage.getCommand(), localShortMessage.getData1(), localShortMessage.getData2());
/*      */ 
/* 1043 */       return;
/*      */     }
/* 1045 */     processMessage(paramMidiMessage.getMessage());
/*      */   }
/*      */ 
/*      */   public void processMessage(byte[] paramArrayOfByte) {
/* 1049 */     int i = 0;
/* 1050 */     if (paramArrayOfByte.length > 0) {
/* 1051 */       i = paramArrayOfByte[0] & 0xFF;
/*      */     }
/* 1053 */     if (i == 240) {
/* 1054 */       processSystemExclusiveMessage(paramArrayOfByte);
/* 1055 */       return;
/*      */     }
/*      */ 
/* 1058 */     int j = i & 0xF0;
/* 1059 */     int k = i & 0xF;
/*      */     int m;
/* 1063 */     if (paramArrayOfByte.length > 1)
/* 1064 */       m = paramArrayOfByte[1] & 0xFF;
/*      */     else
/* 1066 */       m = 0;
/*      */     int n;
/* 1067 */     if (paramArrayOfByte.length > 2)
/* 1068 */       n = paramArrayOfByte[2] & 0xFF;
/*      */     else {
/* 1070 */       n = 0;
/*      */     }
/* 1072 */     processMessage(k, j, m, n);
/*      */   }
/*      */ 
/*      */   public void processMessage(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1077 */     synchronized (this.synth.control_mutex) {
/* 1078 */       activity();
/*      */     }
/*      */ 
/* 1081 */     if (paramInt2 == 240) {
/* 1082 */       int i = paramInt2 | paramInt1;
/* 1083 */       switch (i) {
/*      */       case 254:
/* 1085 */         synchronized (this.synth.control_mutex) {
/* 1086 */           this.active_sensing_on = true;
/*      */         }
/* 1088 */         break;
/*      */       }
/*      */ 
/* 1092 */       return;
/*      */     }
/*      */ 
/* 1095 */     SoftChannel[] arrayOfSoftChannel = this.synth.channels;
/* 1096 */     if (paramInt1 >= arrayOfSoftChannel.length)
/* 1097 */       return;
/* 1098 */     ??? = arrayOfSoftChannel[paramInt1];
/*      */ 
/* 1100 */     switch (paramInt2) {
/*      */     case 144:
/* 1102 */       if (this.delay_midievent != 0)
/* 1103 */         ???.noteOn(paramInt3, paramInt4, this.delay_midievent);
/*      */       else
/* 1105 */         ???.noteOn(paramInt3, paramInt4);
/* 1106 */       break;
/*      */     case 128:
/* 1108 */       ???.noteOff(paramInt3, paramInt4);
/* 1109 */       break;
/*      */     case 160:
/* 1111 */       ???.setPolyPressure(paramInt3, paramInt4);
/* 1112 */       break;
/*      */     case 176:
/* 1114 */       ???.controlChange(paramInt3, paramInt4);
/* 1115 */       break;
/*      */     case 192:
/* 1117 */       ???.programChange(paramInt3);
/* 1118 */       break;
/*      */     case 208:
/* 1120 */       ???.setChannelPressure(paramInt3);
/* 1121 */       break;
/*      */     case 224:
/* 1123 */       ???.setPitchBend(paramInt3 + paramInt4 * 128);
/* 1124 */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   public long getMicrosecondPosition()
/*      */   {
/* 1132 */     if (this.pusher_silent)
/*      */     {
/* 1134 */       if (this.synth.weakstream != null)
/*      */       {
/* 1136 */         return ()((this.sample_pos + this.synth.weakstream.silent_samples) * (1000000.0D / this.samplerate));
/*      */       }
/*      */     }
/*      */ 
/* 1140 */     return ()(this.sample_pos * (1000000.0D / this.samplerate));
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/*      */   }
/*      */ 
/*      */   private class SoftChannelMixerContainer
/*      */   {
/*      */     ModelChannelMixer mixer;
/*      */     SoftAudioBuffer[] buffers;
/*      */ 
/*      */     private SoftChannelMixerContainer()
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftMainMixer
 * JD-Core Version:    0.6.2
 */