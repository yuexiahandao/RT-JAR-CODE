/*      */ package com.sun.media.sound;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.sound.midi.MidiChannel;
/*      */ import javax.sound.midi.Patch;
/*      */ 
/*      */ public final class SoftChannel
/*      */   implements MidiChannel, ModelDirectedPlayer
/*      */ {
/*   44 */   private static boolean[] dontResetControls = new boolean[''];
/*      */   private static final int RPN_NULL_VALUE = 16383;
/*   91 */   private int rpn_control = 16383;
/*   92 */   private int nrpn_control = 16383;
/*   93 */   double portamento_time = 1.0D;
/*   94 */   int[] portamento_lastnote = new int[''];
/*   95 */   int portamento_lastnote_ix = 0;
/*   96 */   private boolean portamento = false;
/*   97 */   private boolean mono = false;
/*   98 */   private boolean mute = false;
/*   99 */   private boolean solo = false;
/*  100 */   private boolean solomute = false;
/*      */   private final Object control_mutex;
/*      */   private int channel;
/*      */   private SoftVoice[] voices;
/*      */   private int bank;
/*      */   private int program;
/*      */   private SoftSynthesizer synthesizer;
/*      */   private SoftMainMixer mainmixer;
/*  108 */   private int[] polypressure = new int[''];
/*  109 */   private int channelpressure = 0;
/*  110 */   private int[] controller = new int[''];
/*      */   private int pitchbend;
/*  112 */   private double[] co_midi_pitch = new double[1];
/*  113 */   private double[] co_midi_channel_pressure = new double[1];
/*  114 */   SoftTuning tuning = new SoftTuning();
/*  115 */   int tuning_bank = 0;
/*  116 */   int tuning_program = 0;
/*  117 */   SoftInstrument current_instrument = null;
/*  118 */   ModelChannelMixer current_mixer = null;
/*  119 */   ModelDirector current_director = null;
/*      */ 
/*  122 */   int cds_control_number = -1;
/*  123 */   ModelConnectionBlock[] cds_control_connections = null;
/*  124 */   ModelConnectionBlock[] cds_channelpressure_connections = null;
/*  125 */   ModelConnectionBlock[] cds_polypressure_connections = null;
/*  126 */   boolean sustain = false;
/*  127 */   boolean[][] keybasedcontroller_active = (boolean[][])null;
/*  128 */   double[][] keybasedcontroller_value = (double[][])null;
/*      */ 
/*  148 */   private SoftControl[] co_midi = new SoftControl[''];
/*      */   private double[][] co_midi_cc_cc;
/*      */   private SoftControl co_midi_cc;
/*      */   Map<Integer, int[]> co_midi_rpn_rpn_i;
/*      */   Map<Integer, double[]> co_midi_rpn_rpn;
/*      */   private SoftControl co_midi_rpn;
/*      */   Map<Integer, int[]> co_midi_nrpn_nrpn_i;
/*      */   Map<Integer, double[]> co_midi_nrpn_nrpn;
/*      */   private SoftControl co_midi_nrpn;
/*      */   private int[] lastVelocity;
/*      */   private int prevVoiceID;
/*      */   private boolean firstVoice;
/*      */   private int voiceNo;
/*      */   private int play_noteNumber;
/*      */   private int play_velocity;
/*      */   private int play_delay;
/*      */   private boolean play_releasetriggered;
/*      */ 
/*      */   private static int restrict7Bit(int paramInt)
/*      */   {
/*  199 */     if (paramInt < 0) return 0;
/*  200 */     if (paramInt > 127) return 127;
/*  201 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private static int restrict14Bit(int paramInt)
/*      */   {
/*  206 */     if (paramInt < 0) return 0;
/*  207 */     if (paramInt > 16256) return 16256;
/*  208 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public SoftChannel(SoftSynthesizer paramSoftSynthesizer, int paramInt)
/*      */   {
/*  150 */     for (int i = 0; i < this.co_midi.length; i++) {
/*  151 */       this.co_midi[i] = new MidiControlObject(null);
/*      */     }
/*      */ 
/*  155 */     this.co_midi_cc_cc = new double[''][1];
/*  156 */     this.co_midi_cc = new SoftControl() {
/*  157 */       double[][] cc = SoftChannel.this.co_midi_cc_cc;
/*      */ 
/*  159 */       public double[] get(int paramAnonymousInt, String paramAnonymousString) { if (paramAnonymousString == null)
/*  160 */           return null;
/*  161 */         return this.cc[Integer.parseInt(paramAnonymousString)];
/*      */       }
/*      */     };
/*  164 */     this.co_midi_rpn_rpn_i = new HashMap();
/*  165 */     this.co_midi_rpn_rpn = new HashMap();
/*  166 */     this.co_midi_rpn = new SoftControl() {
/*  167 */       Map<Integer, double[]> rpn = SoftChannel.this.co_midi_rpn_rpn;
/*      */ 
/*  169 */       public double[] get(int paramAnonymousInt, String paramAnonymousString) { if (paramAnonymousString == null)
/*  170 */           return null;
/*  171 */         int i = Integer.parseInt(paramAnonymousString);
/*  172 */         double[] arrayOfDouble = (double[])this.rpn.get(Integer.valueOf(i));
/*  173 */         if (arrayOfDouble == null) {
/*  174 */           arrayOfDouble = new double[1];
/*  175 */           this.rpn.put(Integer.valueOf(i), arrayOfDouble);
/*      */         }
/*  177 */         return arrayOfDouble;
/*      */       }
/*      */     };
/*  180 */     this.co_midi_nrpn_nrpn_i = new HashMap();
/*  181 */     this.co_midi_nrpn_nrpn = new HashMap();
/*  182 */     this.co_midi_nrpn = new SoftControl() {
/*  183 */       Map<Integer, double[]> nrpn = SoftChannel.this.co_midi_nrpn_nrpn;
/*      */ 
/*  185 */       public double[] get(int paramAnonymousInt, String paramAnonymousString) { if (paramAnonymousString == null)
/*  186 */           return null;
/*  187 */         int i = Integer.parseInt(paramAnonymousString);
/*  188 */         double[] arrayOfDouble = (double[])this.nrpn.get(Integer.valueOf(i));
/*  189 */         if (arrayOfDouble == null) {
/*  190 */           arrayOfDouble = new double[1];
/*  191 */           this.nrpn.put(Integer.valueOf(i), arrayOfDouble);
/*      */         }
/*  193 */         return arrayOfDouble;
/*      */       }
/*      */     };
/*  619 */     this.lastVelocity = new int[''];
/*      */ 
/*  621 */     this.firstVoice = true;
/*  622 */     this.voiceNo = 0;
/*  623 */     this.play_noteNumber = 0;
/*  624 */     this.play_velocity = 0;
/*  625 */     this.play_delay = 0;
/*  626 */     this.play_releasetriggered = false;
/*      */ 
/*  212 */     this.channel = paramInt;
/*  213 */     this.voices = paramSoftSynthesizer.getVoices();
/*  214 */     this.synthesizer = paramSoftSynthesizer;
/*  215 */     this.mainmixer = paramSoftSynthesizer.getMainMixer();
/*  216 */     this.control_mutex = paramSoftSynthesizer.control_mutex;
/*  217 */     resetAllControllers(true);
/*      */   }
/*      */ 
/*      */   private int findFreeVoice(int paramInt) {
/*  221 */     if (paramInt == -1)
/*      */     {
/*  228 */       return -1;
/*      */     }
/*  230 */     for (int i = paramInt; i < this.voices.length; i++) {
/*  231 */       if (!this.voices[i].active) {
/*  232 */         return i;
/*      */       }
/*      */     }
/*      */ 
/*  236 */     i = this.synthesizer.getVoiceAllocationMode();
/*  237 */     if (i == 1)
/*      */     {
/*  242 */       j = this.channel;
/*  243 */       for (int k = 0; k < this.voices.length; k++) {
/*  244 */         if (this.voices[k].stealer_channel == null) {
/*  245 */           if (j == 9) {
/*  246 */             j = this.voices[k].channel;
/*      */           }
/*  248 */           else if ((this.voices[k].channel != 9) && 
/*  249 */             (this.voices[k].channel > j)) {
/*  250 */             j = this.voices[k].channel;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  256 */       k = -1;
/*      */ 
/*  258 */       SoftVoice localSoftVoice2 = null;
/*      */ 
/*  260 */       for (int n = 0; n < this.voices.length; n++) {
/*  261 */         if ((this.voices[n].channel == j) && 
/*  262 */           (this.voices[n].stealer_channel == null) && (!this.voices[n].on)) {
/*  263 */           if (localSoftVoice2 == null) {
/*  264 */             localSoftVoice2 = this.voices[n];
/*  265 */             k = n;
/*      */           }
/*  267 */           if (this.voices[n].voiceID < localSoftVoice2.voiceID) {
/*  268 */             localSoftVoice2 = this.voices[n];
/*  269 */             k = n;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  275 */       if (k == -1) {
/*  276 */         for (n = 0; n < this.voices.length; n++) {
/*  277 */           if ((this.voices[n].channel == j) && 
/*  278 */             (this.voices[n].stealer_channel == null)) {
/*  279 */             if (localSoftVoice2 == null) {
/*  280 */               localSoftVoice2 = this.voices[n];
/*  281 */               k = n;
/*      */             }
/*  283 */             if (this.voices[n].voiceID < localSoftVoice2.voiceID) {
/*  284 */               localSoftVoice2 = this.voices[n];
/*  285 */               k = n;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  292 */       return k;
/*      */     }
/*      */ 
/*  301 */     int j = -1;
/*      */ 
/*  303 */     SoftVoice localSoftVoice1 = null;
/*      */ 
/*  305 */     for (int m = 0; m < this.voices.length; m++) {
/*  306 */       if ((this.voices[m].stealer_channel == null) && (!this.voices[m].on)) {
/*  307 */         if (localSoftVoice1 == null) {
/*  308 */           localSoftVoice1 = this.voices[m];
/*  309 */           j = m;
/*      */         }
/*  311 */         if (this.voices[m].voiceID < localSoftVoice1.voiceID) {
/*  312 */           localSoftVoice1 = this.voices[m];
/*  313 */           j = m;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  318 */     if (j == -1)
/*      */     {
/*  320 */       for (m = 0; m < this.voices.length; m++) {
/*  321 */         if (this.voices[m].stealer_channel == null) {
/*  322 */           if (localSoftVoice1 == null) {
/*  323 */             localSoftVoice1 = this.voices[m];
/*  324 */             j = m;
/*      */           }
/*  326 */           if (this.voices[m].voiceID < localSoftVoice1.voiceID) {
/*  327 */             localSoftVoice1 = this.voices[m];
/*  328 */             j = m;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  334 */     return j;
/*      */   }
/*      */ 
/*      */   void initVoice(SoftVoice paramSoftVoice, SoftPerformer paramSoftPerformer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, ModelConnectionBlock[] paramArrayOfModelConnectionBlock, ModelChannelMixer paramModelChannelMixer, boolean paramBoolean)
/*      */   {
/*  342 */     if (paramSoftVoice.active)
/*      */     {
/*  344 */       paramSoftVoice.stealer_channel = this;
/*  345 */       paramSoftVoice.stealer_performer = paramSoftPerformer;
/*  346 */       paramSoftVoice.stealer_voiceID = paramInt1;
/*  347 */       paramSoftVoice.stealer_noteNumber = paramInt2;
/*  348 */       paramSoftVoice.stealer_velocity = paramInt3;
/*  349 */       paramSoftVoice.stealer_extendedConnectionBlocks = paramArrayOfModelConnectionBlock;
/*  350 */       paramSoftVoice.stealer_channelmixer = paramModelChannelMixer;
/*  351 */       paramSoftVoice.stealer_releaseTriggered = paramBoolean;
/*  352 */       for (int i = 0; i < this.voices.length; i++)
/*  353 */         if ((this.voices[i].active) && (this.voices[i].voiceID == paramSoftVoice.voiceID))
/*  354 */           this.voices[i].soundOff();
/*  355 */       return;
/*      */     }
/*      */ 
/*  358 */     paramSoftVoice.extendedConnectionBlocks = paramArrayOfModelConnectionBlock;
/*  359 */     paramSoftVoice.channelmixer = paramModelChannelMixer;
/*  360 */     paramSoftVoice.releaseTriggered = paramBoolean;
/*  361 */     paramSoftVoice.voiceID = paramInt1;
/*  362 */     paramSoftVoice.tuning = this.tuning;
/*  363 */     paramSoftVoice.exclusiveClass = paramSoftPerformer.exclusiveClass;
/*  364 */     paramSoftVoice.softchannel = this;
/*  365 */     paramSoftVoice.channel = this.channel;
/*  366 */     paramSoftVoice.bank = this.bank;
/*  367 */     paramSoftVoice.program = this.program;
/*  368 */     paramSoftVoice.instrument = this.current_instrument;
/*  369 */     paramSoftVoice.performer = paramSoftPerformer;
/*  370 */     paramSoftVoice.objects.clear();
/*  371 */     paramSoftVoice.objects.put("midi", this.co_midi[paramInt2]);
/*  372 */     paramSoftVoice.objects.put("midi_cc", this.co_midi_cc);
/*  373 */     paramSoftVoice.objects.put("midi_rpn", this.co_midi_rpn);
/*  374 */     paramSoftVoice.objects.put("midi_nrpn", this.co_midi_nrpn);
/*  375 */     paramSoftVoice.noteOn(paramInt2, paramInt3, paramInt4);
/*  376 */     paramSoftVoice.setMute(this.mute);
/*  377 */     paramSoftVoice.setSoloMute(this.solomute);
/*  378 */     if (paramBoolean)
/*  379 */       return;
/*  380 */     if (this.controller[84] != 0) {
/*  381 */       paramSoftVoice.co_noteon_keynumber[0] = (this.tuning.getTuning(this.controller[84]) / 100.0D * 0.0078125D);
/*      */ 
/*  384 */       paramSoftVoice.portamento = true;
/*  385 */       controlChange(84, 0);
/*  386 */     } else if (this.portamento) {
/*  387 */       if (this.mono) {
/*  388 */         if (this.portamento_lastnote[0] != -1) {
/*  389 */           paramSoftVoice.co_noteon_keynumber[0] = (this.tuning.getTuning(this.portamento_lastnote[0]) / 100.0D * 0.0078125D);
/*      */ 
/*  392 */           paramSoftVoice.portamento = true;
/*  393 */           controlChange(84, 0);
/*      */         }
/*  395 */         this.portamento_lastnote[0] = paramInt2;
/*      */       }
/*  397 */       else if (this.portamento_lastnote_ix != 0) {
/*  398 */         this.portamento_lastnote_ix -= 1;
/*  399 */         paramSoftVoice.co_noteon_keynumber[0] = (this.tuning.getTuning(this.portamento_lastnote[this.portamento_lastnote_ix]) / 100.0D * 0.0078125D);
/*      */ 
/*  404 */         paramSoftVoice.portamento = true;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void noteOn(int paramInt1, int paramInt2)
/*      */   {
/*  411 */     noteOn(paramInt1, paramInt2, 0);
/*      */   }
/*      */ 
/*      */   void noteOn(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  418 */     paramInt1 = restrict7Bit(paramInt1);
/*  419 */     paramInt2 = restrict7Bit(paramInt2);
/*  420 */     noteOn_internal(paramInt1, paramInt2, paramInt3);
/*  421 */     if (this.current_mixer != null)
/*  422 */       this.current_mixer.noteOn(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private void noteOn_internal(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  427 */     if (paramInt2 == 0) {
/*  428 */       noteOff_internal(paramInt1, 64);
/*  429 */       return;
/*      */     }
/*      */ 
/*  432 */     synchronized (this.control_mutex) {
/*  433 */       if (this.sustain) {
/*  434 */         this.sustain = false;
/*  435 */         for (i = 0; i < this.voices.length; i++) {
/*  436 */           if (((this.voices[i].sustain) || (this.voices[i].on)) && (this.voices[i].channel == this.channel) && (this.voices[i].active) && (this.voices[i].note == paramInt1))
/*      */           {
/*  439 */             this.voices[i].sustain = false;
/*  440 */             this.voices[i].on = true;
/*  441 */             this.voices[i].noteOff(0);
/*      */           }
/*      */         }
/*  444 */         this.sustain = true;
/*      */       }
/*      */ 
/*  447 */       this.mainmixer.activity();
/*      */ 
/*  449 */       if (this.mono)
/*      */       {
/*      */         int j;
/*  450 */         if (this.portamento) {
/*  451 */           i = 0;
/*  452 */           for (j = 0; j < this.voices.length; j++) {
/*  453 */             if ((this.voices[j].on) && (this.voices[j].channel == this.channel) && (this.voices[j].active) && (!this.voices[j].releaseTriggered))
/*      */             {
/*  456 */               this.voices[j].portamento = true;
/*  457 */               this.voices[j].setNote(paramInt1);
/*  458 */               i = 1;
/*      */             }
/*      */           }
/*  461 */           if (i != 0) {
/*  462 */             this.portamento_lastnote[0] = paramInt1;
/*  463 */             return;
/*      */           }
/*      */         }
/*      */ 
/*  467 */         if (this.controller[84] != 0) {
/*  468 */           i = 0;
/*  469 */           for (j = 0; j < this.voices.length; j++) {
/*  470 */             if ((this.voices[j].on) && (this.voices[j].channel == this.channel) && (this.voices[j].active) && (this.voices[j].note == this.controller[84]) && (!this.voices[j].releaseTriggered))
/*      */             {
/*  474 */               this.voices[j].portamento = true;
/*  475 */               this.voices[j].setNote(paramInt1);
/*  476 */               i = 1;
/*      */             }
/*      */           }
/*  479 */           controlChange(84, 0);
/*  480 */           if (i != 0) {
/*  481 */             return;
/*      */           }
/*      */         }
/*      */       }
/*  485 */       if (this.mono) {
/*  486 */         allNotesOff();
/*      */       }
/*  488 */       if (this.current_instrument == null) {
/*  489 */         this.current_instrument = this.synthesizer.findInstrument(this.program, this.bank, this.channel);
/*      */ 
/*  491 */         if (this.current_instrument == null)
/*  492 */           return;
/*  493 */         if (this.current_mixer != null)
/*  494 */           this.mainmixer.stopMixer(this.current_mixer);
/*  495 */         this.current_mixer = this.current_instrument.getSourceInstrument().getChannelMixer(this, this.synthesizer.getFormat());
/*      */ 
/*  497 */         if (this.current_mixer != null)
/*  498 */           this.mainmixer.registerMixer(this.current_mixer);
/*  499 */         this.current_director = this.current_instrument.getDirector(this, this);
/*  500 */         applyInstrumentCustomization();
/*      */       }
/*  502 */       this.prevVoiceID = (this.synthesizer.voiceIDCounter++);
/*  503 */       this.firstVoice = true;
/*  504 */       this.voiceNo = 0;
/*      */ 
/*  506 */       int i = (int)Math.round(this.tuning.getTuning(paramInt1) / 100.0D);
/*  507 */       this.play_noteNumber = paramInt1;
/*  508 */       this.play_velocity = paramInt2;
/*  509 */       this.play_delay = paramInt3;
/*  510 */       this.play_releasetriggered = false;
/*  511 */       this.lastVelocity[paramInt1] = paramInt2;
/*  512 */       this.current_director.noteOn(i, paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void noteOff(int paramInt1, int paramInt2)
/*      */   {
/*  548 */     paramInt1 = restrict7Bit(paramInt1);
/*  549 */     paramInt2 = restrict7Bit(paramInt2);
/*  550 */     noteOff_internal(paramInt1, paramInt2);
/*      */ 
/*  552 */     if (this.current_mixer != null)
/*  553 */       this.current_mixer.noteOff(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private void noteOff_internal(int paramInt1, int paramInt2) {
/*  557 */     synchronized (this.control_mutex)
/*      */     {
/*  559 */       if ((!this.mono) && 
/*  560 */         (this.portamento) && 
/*  561 */         (this.portamento_lastnote_ix != 127)) {
/*  562 */         this.portamento_lastnote[this.portamento_lastnote_ix] = paramInt1;
/*  563 */         this.portamento_lastnote_ix += 1;
/*      */       }
/*      */ 
/*  568 */       this.mainmixer.activity();
/*  569 */       for (int i = 0; i < this.voices.length; i++) {
/*  570 */         if ((this.voices[i].on) && (this.voices[i].channel == this.channel) && (this.voices[i].note == paramInt1) && (!this.voices[i].releaseTriggered))
/*      */         {
/*  573 */           this.voices[i].noteOff(paramInt2);
/*      */         }
/*      */ 
/*  576 */         if ((this.voices[i].stealer_channel == this) && (this.voices[i].stealer_noteNumber == paramInt1)) {
/*  577 */           SoftVoice localSoftVoice = this.voices[i];
/*  578 */           localSoftVoice.stealer_releaseTriggered = false;
/*  579 */           localSoftVoice.stealer_channel = null;
/*  580 */           localSoftVoice.stealer_performer = null;
/*  581 */           localSoftVoice.stealer_voiceID = -1;
/*  582 */           localSoftVoice.stealer_noteNumber = 0;
/*  583 */           localSoftVoice.stealer_velocity = 0;
/*  584 */           localSoftVoice.stealer_extendedConnectionBlocks = null;
/*  585 */           localSoftVoice.stealer_channelmixer = null;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  591 */       if (this.current_instrument == null) {
/*  592 */         this.current_instrument = this.synthesizer.findInstrument(this.program, this.bank, this.channel);
/*      */ 
/*  594 */         if (this.current_instrument == null)
/*  595 */           return;
/*  596 */         if (this.current_mixer != null)
/*  597 */           this.mainmixer.stopMixer(this.current_mixer);
/*  598 */         this.current_mixer = this.current_instrument.getSourceInstrument().getChannelMixer(this, this.synthesizer.getFormat());
/*      */ 
/*  600 */         if (this.current_mixer != null)
/*  601 */           this.mainmixer.registerMixer(this.current_mixer);
/*  602 */         this.current_director = this.current_instrument.getDirector(this, this);
/*  603 */         applyInstrumentCustomization();
/*      */       }
/*      */ 
/*  606 */       this.prevVoiceID = (this.synthesizer.voiceIDCounter++);
/*  607 */       this.firstVoice = true;
/*  608 */       this.voiceNo = 0;
/*      */ 
/*  610 */       i = (int)Math.round(this.tuning.getTuning(paramInt1) / 100.0D);
/*  611 */       this.play_noteNumber = paramInt1;
/*  612 */       this.play_velocity = this.lastVelocity[paramInt1];
/*  613 */       this.play_releasetriggered = true;
/*  614 */       this.play_delay = 0;
/*  615 */       this.current_director.noteOff(i, paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void play(int paramInt, ModelConnectionBlock[] paramArrayOfModelConnectionBlock)
/*      */   {
/*  630 */     int i = this.play_noteNumber;
/*  631 */     int j = this.play_velocity;
/*  632 */     int k = this.play_delay;
/*  633 */     boolean bool = this.play_releasetriggered;
/*      */ 
/*  635 */     SoftPerformer localSoftPerformer = this.current_instrument.getPerformer(paramInt);
/*      */ 
/*  637 */     if (this.firstVoice) {
/*  638 */       this.firstVoice = false;
/*  639 */       if (localSoftPerformer.exclusiveClass != 0) {
/*  640 */         int m = localSoftPerformer.exclusiveClass;
/*  641 */         for (int n = 0; n < this.voices.length; n++) {
/*  642 */           if ((this.voices[n].active) && (this.voices[n].channel == this.channel) && (this.voices[n].exclusiveClass == m))
/*      */           {
/*  644 */             if ((!localSoftPerformer.selfNonExclusive) || (this.voices[n].note != i)) {
/*  645 */               this.voices[n].shutdown();
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  651 */     this.voiceNo = findFreeVoice(this.voiceNo);
/*      */ 
/*  653 */     if (this.voiceNo == -1) {
/*  654 */       return;
/*      */     }
/*  656 */     initVoice(this.voices[this.voiceNo], localSoftPerformer, this.prevVoiceID, i, j, k, paramArrayOfModelConnectionBlock, this.current_mixer, bool);
/*      */   }
/*      */ 
/*      */   public void noteOff(int paramInt)
/*      */   {
/*  661 */     if ((paramInt < 0) || (paramInt > 127)) return;
/*  662 */     noteOff_internal(paramInt, 64);
/*      */   }
/*      */ 
/*      */   public void setPolyPressure(int paramInt1, int paramInt2) {
/*  666 */     paramInt1 = restrict7Bit(paramInt1);
/*  667 */     paramInt2 = restrict7Bit(paramInt2);
/*      */ 
/*  669 */     if (this.current_mixer != null) {
/*  670 */       this.current_mixer.setPolyPressure(paramInt1, paramInt2);
/*      */     }
/*  672 */     synchronized (this.control_mutex) {
/*  673 */       this.mainmixer.activity();
/*  674 */       this.co_midi[paramInt1].get(0, "poly_pressure")[0] = (paramInt2 * 0.0078125D);
/*  675 */       this.polypressure[paramInt1] = paramInt2;
/*  676 */       for (int i = 0; i < this.voices.length; i++)
/*  677 */         if ((this.voices[i].active) && (this.voices[i].note == paramInt1))
/*  678 */           this.voices[i].setPolyPressure(paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getPolyPressure(int paramInt)
/*      */   {
/*  684 */     synchronized (this.control_mutex) {
/*  685 */       return this.polypressure[paramInt];
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setChannelPressure(int paramInt) {
/*  690 */     paramInt = restrict7Bit(paramInt);
/*  691 */     if (this.current_mixer != null)
/*  692 */       this.current_mixer.setChannelPressure(paramInt);
/*  693 */     synchronized (this.control_mutex) {
/*  694 */       this.mainmixer.activity();
/*  695 */       this.co_midi_channel_pressure[0] = (paramInt * 0.0078125D);
/*  696 */       this.channelpressure = paramInt;
/*  697 */       for (int i = 0; i < this.voices.length; i++)
/*  698 */         if (this.voices[i].active)
/*  699 */           this.voices[i].setChannelPressure(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getChannelPressure()
/*      */   {
/*  705 */     synchronized (this.control_mutex) {
/*  706 */       return this.channelpressure;
/*      */     }
/*      */   }
/*      */ 
/*      */   void applyInstrumentCustomization() {
/*  711 */     if ((this.cds_control_connections == null) && (this.cds_channelpressure_connections == null) && (this.cds_polypressure_connections == null))
/*      */     {
/*  714 */       return;
/*      */     }
/*      */ 
/*  717 */     ModelInstrument localModelInstrument = this.current_instrument.getSourceInstrument();
/*  718 */     ModelPerformer[] arrayOfModelPerformer1 = localModelInstrument.getPerformers();
/*  719 */     ModelPerformer[] arrayOfModelPerformer2 = new ModelPerformer[arrayOfModelPerformer1.length];
/*  720 */     for (int i = 0; i < arrayOfModelPerformer2.length; i++) {
/*  721 */       ModelPerformer localModelPerformer1 = arrayOfModelPerformer1[i];
/*  722 */       ModelPerformer localModelPerformer2 = new ModelPerformer();
/*  723 */       localModelPerformer2.setName(localModelPerformer1.getName());
/*  724 */       localModelPerformer2.setExclusiveClass(localModelPerformer1.getExclusiveClass());
/*  725 */       localModelPerformer2.setKeyFrom(localModelPerformer1.getKeyFrom());
/*  726 */       localModelPerformer2.setKeyTo(localModelPerformer1.getKeyTo());
/*  727 */       localModelPerformer2.setVelFrom(localModelPerformer1.getVelFrom());
/*  728 */       localModelPerformer2.setVelTo(localModelPerformer1.getVelTo());
/*  729 */       localModelPerformer2.getOscillators().addAll(localModelPerformer1.getOscillators());
/*  730 */       localModelPerformer2.getConnectionBlocks().addAll(localModelPerformer1.getConnectionBlocks());
/*      */ 
/*  732 */       arrayOfModelPerformer2[i] = localModelPerformer2;
/*      */ 
/*  734 */       List localList = localModelPerformer2.getConnectionBlocks();
/*      */       Object localObject1;
/*      */       Object localObject2;
/*      */       int i1;
/*  737 */       if (this.cds_control_connections != null) {
/*  738 */         localObject1 = Integer.toString(this.cds_control_number);
/*  739 */         localObject2 = localList.iterator();
/*  740 */         while (((Iterator)localObject2).hasNext()) {
/*  741 */           ModelConnectionBlock localModelConnectionBlock2 = (ModelConnectionBlock)((Iterator)localObject2).next();
/*  742 */           ModelSource[] arrayOfModelSource2 = localModelConnectionBlock2.getSources();
/*  743 */           i1 = 0;
/*  744 */           if (arrayOfModelSource2 != null) {
/*  745 */             for (int i2 = 0; i2 < arrayOfModelSource2.length; i2++) {
/*  746 */               ModelSource localModelSource = arrayOfModelSource2[i2];
/*  747 */               if (("midi_cc".equals(localModelSource.getIdentifier().getObject())) && (((String)localObject1).equals(localModelSource.getIdentifier().getVariable())))
/*      */               {
/*  749 */                 i1 = 1;
/*      */               }
/*      */             }
/*      */           }
/*  753 */           if (i1 != 0)
/*  754 */             ((Iterator)localObject2).remove();
/*      */         }
/*  756 */         for (int m = 0; m < this.cds_control_connections.length; m++)
/*  757 */           localList.add(this.cds_control_connections[m]);
/*      */       }
/*      */       ModelSource[] arrayOfModelSource1;
/*      */       int n;
/*      */       Object localObject3;
/*  760 */       if (this.cds_polypressure_connections != null) {
/*  761 */         localObject1 = localList.iterator();
/*  762 */         while (((Iterator)localObject1).hasNext()) {
/*  763 */           localObject2 = (ModelConnectionBlock)((Iterator)localObject1).next();
/*  764 */           arrayOfModelSource1 = ((ModelConnectionBlock)localObject2).getSources();
/*  765 */           n = 0;
/*  766 */           if (arrayOfModelSource1 != null) {
/*  767 */             for (i1 = 0; i1 < arrayOfModelSource1.length; i1++) {
/*  768 */               localObject3 = arrayOfModelSource1[i1];
/*  769 */               if (("midi".equals(((ModelSource)localObject3).getIdentifier().getObject())) && ("poly_pressure".equals(((ModelSource)localObject3).getIdentifier().getVariable())))
/*      */               {
/*  772 */                 n = 1;
/*      */               }
/*      */             }
/*      */           }
/*  776 */           if (n != 0)
/*  777 */             ((Iterator)localObject1).remove();
/*      */         }
/*  779 */         for (int j = 0; j < this.cds_polypressure_connections.length; j++) {
/*  780 */           localList.add(this.cds_polypressure_connections[j]);
/*      */         }
/*      */       }
/*      */ 
/*  784 */       if (this.cds_channelpressure_connections != null) {
/*  785 */         localObject1 = localList.iterator();
/*  786 */         while (((Iterator)localObject1).hasNext()) {
/*  787 */           ModelConnectionBlock localModelConnectionBlock1 = (ModelConnectionBlock)((Iterator)localObject1).next();
/*  788 */           arrayOfModelSource1 = localModelConnectionBlock1.getSources();
/*  789 */           n = 0;
/*  790 */           if (arrayOfModelSource1 != null) {
/*  791 */             for (i1 = 0; i1 < arrayOfModelSource1.length; i1++) {
/*  792 */               localObject3 = arrayOfModelSource1[i1].getIdentifier();
/*  793 */               if (("midi".equals(((ModelIdentifier)localObject3).getObject())) && ("channel_pressure".equals(((ModelIdentifier)localObject3).getVariable())))
/*      */               {
/*  795 */                 n = 1;
/*      */               }
/*      */             }
/*      */           }
/*  799 */           if (n != 0)
/*  800 */             ((Iterator)localObject1).remove();
/*      */         }
/*  802 */         for (int k = 0; k < this.cds_channelpressure_connections.length; k++) {
/*  803 */           localList.add(this.cds_channelpressure_connections[k]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  808 */     this.current_instrument = new SoftInstrument(localModelInstrument, arrayOfModelPerformer2);
/*      */   }
/*      */ 
/*      */   private ModelConnectionBlock[] createModelConnections(ModelIdentifier paramModelIdentifier, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */   {
/*  826 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/*  828 */     for (int i = 0; i < paramArrayOfInt1.length; i++) {
/*  829 */       int j = paramArrayOfInt1[i];
/*  830 */       int k = paramArrayOfInt2[i];
/*      */       final double d;
/*      */       Object localObject;
/*  831 */       if (j == 0) {
/*  832 */         d = (k - 64) * 100;
/*  833 */         localObject = new ModelConnectionBlock(new ModelSource(paramModelIdentifier, false, false, 0), d, new ModelDestination(new ModelIdentifier("osc", "pitch")));
/*      */ 
/*  841 */         localArrayList.add(localObject);
/*      */       }
/*      */ 
/*  844 */       if (j == 1) {
/*  845 */         d = (k / 64.0D - 1.0D) * 9600.0D;
/*      */ 
/*  847 */         if (d > 0.0D) {
/*  848 */           localObject = new ModelConnectionBlock(new ModelSource(paramModelIdentifier, true, false, 0), -d, new ModelDestination(ModelDestination.DESTINATION_FILTER_FREQ));
/*      */         }
/*      */         else
/*      */         {
/*  857 */           localObject = new ModelConnectionBlock(new ModelSource(paramModelIdentifier, false, false, 0), d, new ModelDestination(ModelDestination.DESTINATION_FILTER_FREQ));
/*      */         }
/*      */ 
/*  866 */         localArrayList.add(localObject);
/*      */       }
/*      */       ModelConnectionBlock localModelConnectionBlock;
/*  868 */       if (j == 2) {
/*  869 */         d = k / 64.0D;
/*  870 */         localObject = new ModelTransform() {
/*  871 */           double s = d;
/*      */ 
/*  873 */           public double transform(double paramAnonymousDouble) { if (this.s < 1.0D)
/*  874 */               paramAnonymousDouble = this.s + paramAnonymousDouble * (1.0D - this.s);
/*  875 */             else if (this.s > 1.0D)
/*  876 */               paramAnonymousDouble = 1.0D + paramAnonymousDouble * (this.s - 1.0D);
/*      */             else
/*  878 */               return 0.0D;
/*  879 */             return -(0.4166666666666667D / Math.log(10.0D)) * Math.log(paramAnonymousDouble);
/*      */           }
/*      */         };
/*  883 */         localModelConnectionBlock = new ModelConnectionBlock(new ModelSource(paramModelIdentifier, (ModelTransform)localObject), -960.0D, new ModelDestination(ModelDestination.DESTINATION_GAIN));
/*      */ 
/*  886 */         localArrayList.add(localModelConnectionBlock);
/*      */       }
/*      */ 
/*  889 */       if (j == 3) {
/*  890 */         d = (k / 64.0D - 1.0D) * 9600.0D;
/*  891 */         localObject = new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_LFO1, false, true, 0), new ModelSource(paramModelIdentifier, false, false, 0), d, new ModelDestination(ModelDestination.DESTINATION_PITCH));
/*      */ 
/*  903 */         localArrayList.add(localObject);
/*      */       }
/*  905 */       if (j == 4) {
/*  906 */         d = k / 128.0D * 2400.0D;
/*  907 */         localObject = new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_LFO1, false, true, 0), new ModelSource(paramModelIdentifier, false, false, 0), d, new ModelDestination(ModelDestination.DESTINATION_FILTER_FREQ));
/*      */ 
/*  919 */         localArrayList.add(localObject);
/*      */       }
/*  921 */       if (j == 5) {
/*  922 */         d = k / 127.0D;
/*      */ 
/*  924 */         localObject = new ModelTransform() {
/*  925 */           double s = d;
/*      */ 
/*  927 */           public double transform(double paramAnonymousDouble) { return -(0.4166666666666667D / Math.log(10.0D)) * Math.log(1.0D - paramAnonymousDouble * this.s); }
/*      */ 
/*      */         };
/*  932 */         localModelConnectionBlock = new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_LFO1, false, false, 0), new ModelSource(paramModelIdentifier, (ModelTransform)localObject), -960.0D, new ModelDestination(ModelDestination.DESTINATION_GAIN));
/*      */ 
/*  941 */         localArrayList.add(localModelConnectionBlock);
/*      */       }
/*      */     }
/*      */ 
/*  945 */     return (ModelConnectionBlock[])localArrayList.toArray(new ModelConnectionBlock[localArrayList.size()]);
/*      */   }
/*      */ 
/*      */   public void mapPolyPressureToDestination(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
/*  949 */     this.current_instrument = null;
/*  950 */     if (paramArrayOfInt1.length == 0) {
/*  951 */       this.cds_polypressure_connections = null;
/*  952 */       return;
/*      */     }
/*  954 */     this.cds_polypressure_connections = createModelConnections(new ModelIdentifier("midi", "poly_pressure"), paramArrayOfInt1, paramArrayOfInt2);
/*      */   }
/*      */ 
/*      */   public void mapChannelPressureToDestination(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */   {
/*  961 */     this.current_instrument = null;
/*  962 */     if (paramArrayOfInt1.length == 0) {
/*  963 */       this.cds_channelpressure_connections = null;
/*  964 */       return;
/*      */     }
/*  966 */     this.cds_channelpressure_connections = createModelConnections(new ModelIdentifier("midi", "channel_pressure"), paramArrayOfInt1, paramArrayOfInt2);
/*      */   }
/*      */ 
/*      */   public void mapControlToDestination(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */   {
/*  974 */     if (((paramInt < 1) || (paramInt > 31)) && ((paramInt < 64) || (paramInt > 95)))
/*      */     {
/*  976 */       this.cds_control_connections = null;
/*  977 */       return;
/*      */     }
/*      */ 
/*  980 */     this.current_instrument = null;
/*  981 */     this.cds_control_number = paramInt;
/*  982 */     if (paramArrayOfInt1.length == 0) {
/*  983 */       this.cds_control_connections = null;
/*  984 */       return;
/*      */     }
/*  986 */     this.cds_control_connections = createModelConnections(new ModelIdentifier("midi_cc", Integer.toString(paramInt)), paramArrayOfInt1, paramArrayOfInt2);
/*      */   }
/*      */ 
/*      */   public void controlChangePerNote(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1014 */     if (this.keybasedcontroller_active == null) {
/* 1015 */       this.keybasedcontroller_active = new boolean[''][];
/* 1016 */       this.keybasedcontroller_value = new double[''][];
/*      */     }
/* 1018 */     if (this.keybasedcontroller_active[paramInt1] == null) {
/* 1019 */       this.keybasedcontroller_active[paramInt1] = new boolean[''];
/* 1020 */       Arrays.fill(this.keybasedcontroller_active[paramInt1], false);
/* 1021 */       this.keybasedcontroller_value[paramInt1] = new double[''];
/* 1022 */       Arrays.fill(this.keybasedcontroller_value[paramInt1], 0.0D);
/*      */     }
/*      */ 
/* 1025 */     if (paramInt3 == -1) {
/* 1026 */       this.keybasedcontroller_active[paramInt1][paramInt2] = 0;
/*      */     } else {
/* 1028 */       this.keybasedcontroller_active[paramInt1][paramInt2] = 1;
/* 1029 */       this.keybasedcontroller_value[paramInt1][paramInt2] = (paramInt3 / 128.0D);
/*      */     }
/*      */     int i;
/* 1032 */     if (paramInt2 < 120)
/* 1033 */       for (i = 0; i < this.voices.length; i++)
/* 1034 */         if (this.voices[i].active)
/* 1035 */           this.voices[i].controlChange(paramInt2, -1);
/* 1036 */     else if (paramInt2 == 120)
/* 1037 */       for (i = 0; i < this.voices.length; i++)
/* 1038 */         if (this.voices[i].active)
/* 1039 */           this.voices[i].rpnChange(1, -1);
/* 1040 */     else if (paramInt2 == 121)
/* 1041 */       for (i = 0; i < this.voices.length; i++)
/* 1042 */         if (this.voices[i].active)
/* 1043 */           this.voices[i].rpnChange(2, -1);
/*      */   }
/*      */ 
/*      */   public int getControlPerNote(int paramInt1, int paramInt2)
/*      */   {
/* 1049 */     if (this.keybasedcontroller_active == null)
/* 1050 */       return -1;
/* 1051 */     if (this.keybasedcontroller_active[paramInt1] == null)
/* 1052 */       return -1;
/* 1053 */     if (this.keybasedcontroller_active[paramInt1][paramInt2] == 0)
/* 1054 */       return -1;
/* 1055 */     return (int)(this.keybasedcontroller_value[paramInt1][paramInt2] * 128.0D);
/*      */   }
/*      */ 
/*      */   public void controlChange(int paramInt1, int paramInt2) {
/* 1059 */     paramInt1 = restrict7Bit(paramInt1);
/* 1060 */     paramInt2 = restrict7Bit(paramInt2);
/* 1061 */     if (this.current_mixer != null) {
/* 1062 */       this.current_mixer.controlChange(paramInt1, paramInt2);
/*      */     }
/* 1064 */     synchronized (this.control_mutex)
/*      */     {
/*      */       int k;
/*      */       int m;
/* 1065 */       switch (paramInt1)
/*      */       {
/*      */       case 5:
/* 1076 */         double d = -Math.asin(paramInt2 / 128.0D * 2.0D - 1.0D) / 3.141592653589793D + 0.5D;
/* 1077 */         d = Math.pow(100000.0D, d) / 100.0D;
/*      */ 
/* 1079 */         d /= 100.0D;
/* 1080 */         d *= 1000.0D;
/* 1081 */         d /= this.synthesizer.getControlRate();
/* 1082 */         this.portamento_time = d;
/* 1083 */         break;
/*      */       case 6:
/*      */       case 38:
/*      */       case 96:
/*      */       case 97:
/* 1088 */         int j = 0;
/*      */         int[] arrayOfInt;
/* 1089 */         if (this.nrpn_control != 16383) {
/* 1090 */           arrayOfInt = (int[])this.co_midi_nrpn_nrpn_i.get(Integer.valueOf(this.nrpn_control));
/* 1091 */           if (arrayOfInt != null)
/* 1092 */             j = arrayOfInt[0];
/*      */         }
/* 1094 */         if (this.rpn_control != 16383) {
/* 1095 */           arrayOfInt = (int[])this.co_midi_rpn_rpn_i.get(Integer.valueOf(this.rpn_control));
/* 1096 */           if (arrayOfInt != null) {
/* 1097 */             j = arrayOfInt[0];
/*      */           }
/*      */         }
/* 1100 */         if (paramInt1 == 6) {
/* 1101 */           j = (j & 0x7F) + (paramInt2 << 7);
/* 1102 */         } else if (paramInt1 == 38) {
/* 1103 */           j = (j & 0x3F80) + paramInt2;
/* 1104 */         } else if ((paramInt1 == 96) || (paramInt1 == 97)) {
/* 1105 */           k = 1;
/* 1106 */           if ((this.rpn_control == 2) || (this.rpn_control == 3) || (this.rpn_control == 4))
/* 1107 */             k = 128;
/* 1108 */           if (paramInt1 == 96)
/* 1109 */             j += k;
/* 1110 */           if (paramInt1 == 97) {
/* 1111 */             j -= k;
/*      */           }
/*      */         }
/* 1114 */         if (this.nrpn_control != 16383)
/* 1115 */           nrpnChange(this.nrpn_control, j);
/* 1116 */         if (this.rpn_control != 16383)
/* 1117 */           rpnChange(this.rpn_control, j); break;
/*      */       case 64:
/* 1121 */         k = paramInt2 >= 64 ? 1 : 0;
/* 1122 */         if (this.sustain != k) {
/* 1123 */           this.sustain = k;
/* 1124 */           if (k == 0) {
/* 1125 */             for (m = 0; m < this.voices.length; m++)
/* 1126 */               if ((this.voices[m].active) && (this.voices[m].sustain) && (this.voices[m].channel == this.channel))
/*      */               {
/* 1128 */                 this.voices[m].sustain = false;
/* 1129 */                 if (!this.voices[m].on) {
/* 1130 */                   this.voices[m].on = true;
/* 1131 */                   this.voices[m].noteOff(0);
/*      */                 }
/*      */               }
/*      */           }
/*      */           else
/* 1136 */             for (m = 0; m < this.voices.length; m++)
/* 1137 */               if ((this.voices[m].active) && (this.voices[m].channel == this.channel))
/* 1138 */                 this.voices[m].redamp();
/*      */         }
/* 1136 */         break;
/*      */       case 65:
/* 1144 */         this.portamento = (paramInt2 >= 64);
/* 1145 */         this.portamento_lastnote[0] = -1;
/*      */ 
/* 1150 */         this.portamento_lastnote_ix = 0;
/* 1151 */         break;
/*      */       case 66:
/* 1153 */         k = paramInt2 >= 64 ? 1 : 0;
/* 1154 */         if (k != 0) {
/* 1155 */           for (m = 0; m < this.voices.length; m++) {
/* 1156 */             if ((this.voices[m].active) && (this.voices[m].on) && (this.voices[m].channel == this.channel))
/*      */             {
/* 1158 */               this.voices[m].sostenuto = true;
/*      */             }
/*      */           }
/*      */         }
/* 1162 */         if (k == 0)
/* 1163 */           for (m = 0; m < this.voices.length; m++)
/* 1164 */             if ((this.voices[m].active) && (this.voices[m].sostenuto) && (this.voices[m].channel == this.channel))
/*      */             {
/* 1166 */               this.voices[m].sostenuto = false;
/* 1167 */               if (!this.voices[m].on) {
/* 1168 */                 this.voices[m].on = true;
/* 1169 */                 this.voices[m].noteOff(0);
/*      */               }
/*      */             }
/* 1163 */         break;
/*      */       case 98:
/* 1176 */         this.nrpn_control = ((this.nrpn_control & 0x3F80) + paramInt2);
/* 1177 */         this.rpn_control = 16383;
/* 1178 */         break;
/*      */       case 99:
/* 1180 */         this.nrpn_control = ((this.nrpn_control & 0x7F) + (paramInt2 << 7));
/* 1181 */         this.rpn_control = 16383;
/* 1182 */         break;
/*      */       case 100:
/* 1184 */         this.rpn_control = ((this.rpn_control & 0x3F80) + paramInt2);
/* 1185 */         this.nrpn_control = 16383;
/* 1186 */         break;
/*      */       case 101:
/* 1188 */         this.rpn_control = ((this.rpn_control & 0x7F) + (paramInt2 << 7));
/* 1189 */         this.nrpn_control = 16383;
/* 1190 */         break;
/*      */       case 120:
/* 1192 */         allSoundOff();
/* 1193 */         break;
/*      */       case 121:
/* 1195 */         resetAllControllers(paramInt2 == 127);
/* 1196 */         break;
/*      */       case 122:
/* 1198 */         localControl(paramInt2 >= 64);
/* 1199 */         break;
/*      */       case 123:
/* 1201 */         allNotesOff();
/* 1202 */         break;
/*      */       case 124:
/* 1204 */         setOmni(false);
/* 1205 */         break;
/*      */       case 125:
/* 1207 */         setOmni(true);
/* 1208 */         break;
/*      */       case 126:
/* 1210 */         if (paramInt2 == 1)
/* 1211 */           setMono(true); break;
/*      */       case 127:
/* 1214 */         setMono(false);
/* 1215 */         break;
/*      */       }
/*      */ 
/* 1221 */       this.co_midi_cc_cc[paramInt1][0] = (paramInt2 * 0.0078125D);
/*      */ 
/* 1223 */       if (paramInt1 == 0) {
/* 1224 */         this.bank = (paramInt2 << 7);
/* 1225 */         return;
/*      */       }
/*      */ 
/* 1228 */       if (paramInt1 == 32) {
/* 1229 */         this.bank = ((this.bank & 0x3F80) + paramInt2);
/* 1230 */         return;
/*      */       }
/*      */ 
/* 1233 */       this.controller[paramInt1] = paramInt2;
/* 1234 */       if (paramInt1 < 32) {
/* 1235 */         this.controller[(paramInt1 + 32)] = 0;
/*      */       }
/* 1237 */       for (int i = 0; i < this.voices.length; i++)
/* 1238 */         if (this.voices[i].active)
/* 1239 */           this.voices[i].controlChange(paramInt1, paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getController(int paramInt)
/*      */   {
/* 1245 */     synchronized (this.control_mutex)
/*      */     {
/* 1248 */       return this.controller[paramInt] & 0x7F;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void tuningChange(int paramInt) {
/* 1253 */     tuningChange(0, paramInt);
/*      */   }
/*      */ 
/*      */   public void tuningChange(int paramInt1, int paramInt2) {
/* 1257 */     synchronized (this.control_mutex) {
/* 1258 */       this.tuning = this.synthesizer.getTuning(new Patch(paramInt1, paramInt2));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void programChange(int paramInt) {
/* 1263 */     programChange(this.bank, paramInt);
/*      */   }
/*      */ 
/*      */   public void programChange(int paramInt1, int paramInt2) {
/* 1267 */     paramInt1 = restrict14Bit(paramInt1);
/* 1268 */     paramInt2 = restrict7Bit(paramInt2);
/* 1269 */     synchronized (this.control_mutex) {
/* 1270 */       this.mainmixer.activity();
/* 1271 */       if ((this.bank != paramInt1) || (this.program != paramInt2))
/*      */       {
/* 1273 */         this.bank = paramInt1;
/* 1274 */         this.program = paramInt2;
/* 1275 */         this.current_instrument = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getProgram() {
/* 1281 */     synchronized (this.control_mutex) {
/* 1282 */       return this.program;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setPitchBend(int paramInt) {
/* 1287 */     paramInt = restrict14Bit(paramInt);
/* 1288 */     if (this.current_mixer != null)
/* 1289 */       this.current_mixer.setPitchBend(paramInt);
/* 1290 */     synchronized (this.control_mutex) {
/* 1291 */       this.mainmixer.activity();
/* 1292 */       this.co_midi_pitch[0] = (paramInt * 6.103515625E-005D);
/* 1293 */       this.pitchbend = paramInt;
/* 1294 */       for (int i = 0; i < this.voices.length; i++)
/* 1295 */         if (this.voices[i].active)
/* 1296 */           this.voices[i].setPitchBend(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getPitchBend() {
/* 1301 */     synchronized (this.control_mutex) {
/* 1302 */       return this.pitchbend;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void nrpnChange(int paramInt1, int paramInt2)
/*      */   {
/* 1316 */     if (this.synthesizer.getGeneralMidiMode() == 0) {
/* 1317 */       if (paramInt1 == 136)
/* 1318 */         controlChange(76, paramInt2 >> 7);
/* 1319 */       if (paramInt1 == 137)
/* 1320 */         controlChange(77, paramInt2 >> 7);
/* 1321 */       if (paramInt1 == 138)
/* 1322 */         controlChange(78, paramInt2 >> 7);
/* 1323 */       if (paramInt1 == 160)
/* 1324 */         controlChange(74, paramInt2 >> 7);
/* 1325 */       if (paramInt1 == 161)
/* 1326 */         controlChange(71, paramInt2 >> 7);
/* 1327 */       if (paramInt1 == 227)
/* 1328 */         controlChange(73, paramInt2 >> 7);
/* 1329 */       if (paramInt1 == 228)
/* 1330 */         controlChange(75, paramInt2 >> 7);
/* 1331 */       if (paramInt1 == 230) {
/* 1332 */         controlChange(72, paramInt2 >> 7);
/*      */       }
/* 1334 */       if (paramInt1 >> 7 == 24)
/* 1335 */         controlChangePerNote(paramInt1 % 128, 120, paramInt2 >> 7);
/* 1336 */       if (paramInt1 >> 7 == 26)
/* 1337 */         controlChangePerNote(paramInt1 % 128, 7, paramInt2 >> 7);
/* 1338 */       if (paramInt1 >> 7 == 28)
/* 1339 */         controlChangePerNote(paramInt1 % 128, 10, paramInt2 >> 7);
/* 1340 */       if (paramInt1 >> 7 == 29)
/* 1341 */         controlChangePerNote(paramInt1 % 128, 91, paramInt2 >> 7);
/* 1342 */       if (paramInt1 >> 7 == 30) {
/* 1343 */         controlChangePerNote(paramInt1 % 128, 93, paramInt2 >> 7);
/*      */       }
/*      */     }
/* 1346 */     int[] arrayOfInt = (int[])this.co_midi_nrpn_nrpn_i.get(Integer.valueOf(paramInt1));
/* 1347 */     double[] arrayOfDouble = (double[])this.co_midi_nrpn_nrpn.get(Integer.valueOf(paramInt1));
/* 1348 */     if (arrayOfInt == null) {
/* 1349 */       arrayOfInt = new int[1];
/* 1350 */       this.co_midi_nrpn_nrpn_i.put(Integer.valueOf(paramInt1), arrayOfInt);
/*      */     }
/* 1352 */     if (arrayOfDouble == null) {
/* 1353 */       arrayOfDouble = new double[1];
/* 1354 */       this.co_midi_nrpn_nrpn.put(Integer.valueOf(paramInt1), arrayOfDouble);
/*      */     }
/* 1356 */     arrayOfInt[0] = paramInt2;
/* 1357 */     arrayOfDouble[0] = (arrayOfInt[0] * 6.103515625E-005D);
/*      */ 
/* 1359 */     for (int i = 0; i < this.voices.length; i++)
/* 1360 */       if (this.voices[i].active)
/* 1361 */         this.voices[i].nrpnChange(paramInt1, arrayOfInt[0]);
/*      */   }
/*      */ 
/*      */   public void rpnChange(int paramInt1, int paramInt2)
/*      */   {
/* 1375 */     if (paramInt1 == 3) {
/* 1376 */       this.tuning_program = (paramInt2 >> 7 & 0x7F);
/* 1377 */       tuningChange(this.tuning_bank, this.tuning_program);
/*      */     }
/* 1379 */     if (paramInt1 == 4) {
/* 1380 */       this.tuning_bank = (paramInt2 >> 7 & 0x7F);
/*      */     }
/*      */ 
/* 1383 */     int[] arrayOfInt = (int[])this.co_midi_rpn_rpn_i.get(Integer.valueOf(paramInt1));
/* 1384 */     double[] arrayOfDouble = (double[])this.co_midi_rpn_rpn.get(Integer.valueOf(paramInt1));
/* 1385 */     if (arrayOfInt == null) {
/* 1386 */       arrayOfInt = new int[1];
/* 1387 */       this.co_midi_rpn_rpn_i.put(Integer.valueOf(paramInt1), arrayOfInt);
/*      */     }
/* 1389 */     if (arrayOfDouble == null) {
/* 1390 */       arrayOfDouble = new double[1];
/* 1391 */       this.co_midi_rpn_rpn.put(Integer.valueOf(paramInt1), arrayOfDouble);
/*      */     }
/* 1393 */     arrayOfInt[0] = paramInt2;
/* 1394 */     arrayOfDouble[0] = (arrayOfInt[0] * 6.103515625E-005D);
/*      */ 
/* 1396 */     for (int i = 0; i < this.voices.length; i++)
/* 1397 */       if (this.voices[i].active)
/* 1398 */         this.voices[i].rpnChange(paramInt1, arrayOfInt[0]);
/*      */   }
/*      */ 
/*      */   public void resetAllControllers() {
/* 1402 */     resetAllControllers(false);
/*      */   }
/*      */ 
/*      */   public void resetAllControllers(boolean paramBoolean) {
/* 1406 */     synchronized (this.control_mutex) {
/* 1407 */       this.mainmixer.activity();
/*      */ 
/* 1409 */       for (int i = 0; i < 128; i++) {
/* 1410 */         setPolyPressure(i, 0);
/*      */       }
/* 1412 */       setChannelPressure(0);
/* 1413 */       setPitchBend(8192);
/* 1414 */       for (i = 0; i < 128; i++) {
/* 1415 */         if (dontResetControls[i] == 0) {
/* 1416 */           controlChange(i, 0);
/*      */         }
/*      */       }
/* 1419 */       controlChange(71, 64);
/* 1420 */       controlChange(72, 64);
/* 1421 */       controlChange(73, 64);
/* 1422 */       controlChange(74, 64);
/* 1423 */       controlChange(75, 64);
/* 1424 */       controlChange(76, 64);
/* 1425 */       controlChange(77, 64);
/* 1426 */       controlChange(78, 64);
/*      */ 
/* 1428 */       controlChange(8, 64);
/* 1429 */       controlChange(11, 127);
/* 1430 */       controlChange(98, 127);
/* 1431 */       controlChange(99, 127);
/* 1432 */       controlChange(100, 127);
/* 1433 */       controlChange(101, 127);
/*      */ 
/* 1436 */       if (paramBoolean)
/*      */       {
/* 1438 */         this.keybasedcontroller_active = ((boolean[][])null);
/* 1439 */         this.keybasedcontroller_value = ((double[][])null);
/*      */ 
/* 1441 */         controlChange(7, 100);
/* 1442 */         controlChange(10, 64);
/* 1443 */         controlChange(91, 40);
/*      */ 
/* 1445 */         for (Iterator localIterator = this.co_midi_rpn_rpn.keySet().iterator(); localIterator.hasNext(); ) { j = ((Integer)localIterator.next()).intValue();
/*      */ 
/* 1447 */           if ((j != 3) && (j != 4))
/* 1448 */             rpnChange(j, 0);
/*      */         }
/* 1450 */         int j;
/* 1450 */         for (localIterator = this.co_midi_nrpn_nrpn.keySet().iterator(); localIterator.hasNext(); ) { j = ((Integer)localIterator.next()).intValue();
/* 1451 */           nrpnChange(j, 0); }
/* 1452 */         rpnChange(0, 256);
/* 1453 */         rpnChange(1, 8192);
/* 1454 */         rpnChange(2, 8192);
/* 1455 */         rpnChange(5, 64);
/*      */ 
/* 1457 */         this.tuning_bank = 0;
/* 1458 */         this.tuning_program = 0;
/* 1459 */         this.tuning = new SoftTuning();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void allNotesOff()
/*      */   {
/* 1467 */     if (this.current_mixer != null)
/* 1468 */       this.current_mixer.allNotesOff();
/* 1469 */     synchronized (this.control_mutex) {
/* 1470 */       for (int i = 0; i < this.voices.length; i++)
/* 1471 */         if ((this.voices[i].on) && (this.voices[i].channel == this.channel) && (!this.voices[i].releaseTriggered))
/*      */         {
/* 1473 */           this.voices[i].noteOff(0);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void allSoundOff() {
/* 1479 */     if (this.current_mixer != null)
/* 1480 */       this.current_mixer.allSoundOff();
/* 1481 */     synchronized (this.control_mutex) {
/* 1482 */       for (int i = 0; i < this.voices.length; i++)
/* 1483 */         if ((this.voices[i].on) && (this.voices[i].channel == this.channel))
/* 1484 */           this.voices[i].soundOff();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean localControl(boolean paramBoolean) {
/* 1489 */     return false;
/*      */   }
/*      */ 
/*      */   public void setMono(boolean paramBoolean) {
/* 1493 */     if (this.current_mixer != null)
/* 1494 */       this.current_mixer.setMono(paramBoolean);
/* 1495 */     synchronized (this.control_mutex) {
/* 1496 */       allNotesOff();
/* 1497 */       this.mono = paramBoolean;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getMono() {
/* 1502 */     synchronized (this.control_mutex) {
/* 1503 */       return this.mono;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setOmni(boolean paramBoolean) {
/* 1508 */     if (this.current_mixer != null)
/* 1509 */       this.current_mixer.setOmni(paramBoolean);
/* 1510 */     allNotesOff();
/*      */   }
/*      */ 
/*      */   public boolean getOmni()
/*      */   {
/* 1515 */     return false;
/*      */   }
/*      */ 
/*      */   public void setMute(boolean paramBoolean) {
/* 1519 */     if (this.current_mixer != null)
/* 1520 */       this.current_mixer.setMute(paramBoolean);
/* 1521 */     synchronized (this.control_mutex) {
/* 1522 */       this.mute = paramBoolean;
/* 1523 */       for (int i = 0; i < this.voices.length; i++)
/* 1524 */         if ((this.voices[i].active) && (this.voices[i].channel == this.channel))
/* 1525 */           this.voices[i].setMute(paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getMute() {
/* 1530 */     synchronized (this.control_mutex) {
/* 1531 */       return this.mute;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setSolo(boolean paramBoolean) {
/* 1536 */     if (this.current_mixer != null) {
/* 1537 */       this.current_mixer.setSolo(paramBoolean);
/*      */     }
/* 1539 */     synchronized (this.control_mutex) {
/* 1540 */       this.solo = paramBoolean;
/*      */ 
/* 1542 */       int i = 0;
/*      */       SoftChannel localSoftChannel;
/* 1543 */       for (localSoftChannel : this.synthesizer.channels) {
/* 1544 */         if (localSoftChannel.solo) {
/* 1545 */           i = 1;
/* 1546 */           break;
/*      */         }
/*      */       }
/*      */ 
/* 1550 */       if (i == 0) {
/* 1551 */         for (localSoftChannel : this.synthesizer.channels)
/* 1552 */           localSoftChannel.setSoloMute(false);
/* 1553 */         return;
/*      */       }
/*      */ 
/* 1556 */       for (localSoftChannel : this.synthesizer.channels)
/* 1557 */         localSoftChannel.setSoloMute(!localSoftChannel.solo);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setSoloMute(boolean paramBoolean)
/*      */   {
/* 1564 */     synchronized (this.control_mutex) {
/* 1565 */       if (this.solomute == paramBoolean)
/* 1566 */         return;
/* 1567 */       this.solomute = paramBoolean;
/* 1568 */       for (int i = 0; i < this.voices.length; i++)
/* 1569 */         if ((this.voices[i].active) && (this.voices[i].channel == this.channel))
/* 1570 */           this.voices[i].setSoloMute(this.solomute);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getSolo() {
/* 1575 */     synchronized (this.control_mutex) {
/* 1576 */       return this.solo;
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   46 */     for (int i = 0; i < dontResetControls.length; i++) {
/*   47 */       dontResetControls[i] = false;
/*      */     }
/*   49 */     dontResetControls[0] = true;
/*   50 */     dontResetControls[32] = true;
/*   51 */     dontResetControls[7] = true;
/*   52 */     dontResetControls[8] = true;
/*   53 */     dontResetControls[10] = true;
/*   54 */     dontResetControls[11] = true;
/*   55 */     dontResetControls[91] = true;
/*   56 */     dontResetControls[92] = true;
/*   57 */     dontResetControls[93] = true;
/*   58 */     dontResetControls[94] = true;
/*   59 */     dontResetControls[95] = true;
/*   60 */     dontResetControls[70] = true;
/*   61 */     dontResetControls[71] = true;
/*   62 */     dontResetControls[72] = true;
/*   63 */     dontResetControls[73] = true;
/*   64 */     dontResetControls[74] = true;
/*   65 */     dontResetControls[75] = true;
/*   66 */     dontResetControls[76] = true;
/*   67 */     dontResetControls[77] = true;
/*   68 */     dontResetControls[78] = true;
/*   69 */     dontResetControls[79] = true;
/*   70 */     dontResetControls[120] = true;
/*   71 */     dontResetControls[121] = true;
/*   72 */     dontResetControls[122] = true;
/*   73 */     dontResetControls[123] = true;
/*   74 */     dontResetControls[124] = true;
/*   75 */     dontResetControls[125] = true;
/*   76 */     dontResetControls[126] = true;
/*   77 */     dontResetControls[127] = true;
/*      */ 
/*   79 */     dontResetControls[6] = true;
/*   80 */     dontResetControls[38] = true;
/*   81 */     dontResetControls[96] = true;
/*   82 */     dontResetControls[97] = true;
/*   83 */     dontResetControls[98] = true;
/*   84 */     dontResetControls[99] = true;
/*   85 */     dontResetControls[100] = true;
/*   86 */     dontResetControls[101] = true;
/*      */   }
/*      */ 
/*      */   private class MidiControlObject
/*      */     implements SoftControl
/*      */   {
/*  131 */     double[] pitch = SoftChannel.this.co_midi_pitch;
/*  132 */     double[] channel_pressure = SoftChannel.this.co_midi_channel_pressure;
/*  133 */     double[] poly_pressure = new double[1];
/*      */ 
/*      */     private MidiControlObject() {  } 
/*  136 */     public double[] get(int paramInt, String paramString) { if (paramString == null)
/*  137 */         return null;
/*  138 */       if (paramString.equals("pitch"))
/*  139 */         return this.pitch;
/*  140 */       if (paramString.equals("channel_pressure"))
/*  141 */         return this.channel_pressure;
/*  142 */       if (paramString.equals("poly_pressure"))
/*  143 */         return this.poly_pressure;
/*  144 */       return null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftChannel
 * JD-Core Version:    0.6.2
 */