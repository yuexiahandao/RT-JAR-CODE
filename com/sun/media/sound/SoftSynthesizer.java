/*      */ package com.sun.media.sound;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.prefs.BackingStoreException;
/*      */ import java.util.prefs.Preferences;
/*      */ import javax.sound.midi.Instrument;
/*      */ import javax.sound.midi.MidiChannel;
/*      */ import javax.sound.midi.MidiDevice.Info;
/*      */ import javax.sound.midi.MidiSystem;
/*      */ import javax.sound.midi.MidiUnavailableException;
/*      */ import javax.sound.midi.Patch;
/*      */ import javax.sound.midi.Receiver;
/*      */ import javax.sound.midi.Soundbank;
/*      */ import javax.sound.midi.Transmitter;
/*      */ import javax.sound.midi.VoiceStatus;
/*      */ import javax.sound.sampled.AudioFormat;
/*      */ import javax.sound.sampled.AudioInputStream;
/*      */ import javax.sound.sampled.AudioSystem;
/*      */ import javax.sound.sampled.LineUnavailableException;
/*      */ import javax.sound.sampled.SourceDataLine;
/*      */ 
/*      */ public final class SoftSynthesizer
/*      */   implements AudioSynthesizer, ReferenceCountingDevice
/*      */ {
/*      */   static final String INFO_NAME = "Gervill";
/*      */   static final String INFO_VENDOR = "OpenJDK";
/*      */   static final String INFO_DESCRIPTION = "Software MIDI Synthesizer";
/*      */   static final String INFO_VERSION = "1.0";
/*  178 */   static final MidiDevice.Info info = new Info();
/*      */ 
/*  180 */   private static SourceDataLine testline = null;
/*      */ 
/*  182 */   private static Soundbank defaultSoundBank = null;
/*      */ 
/*  184 */   WeakAudioStream weakstream = null;
/*      */ 
/*  186 */   final Object control_mutex = this;
/*      */ 
/*  188 */   int voiceIDCounter = 0;
/*      */ 
/*  192 */   int voice_allocation_mode = 0;
/*      */ 
/*  194 */   boolean load_default_soundbank = false;
/*  195 */   boolean reverb_light = true;
/*  196 */   boolean reverb_on = true;
/*  197 */   boolean chorus_on = true;
/*  198 */   boolean agc_on = true;
/*      */   SoftChannel[] channels;
/*  201 */   SoftChannelProxy[] external_channels = null;
/*      */ 
/*  203 */   private boolean largemode = false;
/*      */ 
/*  208 */   private int gmmode = 0;
/*      */ 
/*  210 */   private int deviceid = 0;
/*      */ 
/*  212 */   private AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
/*      */ 
/*  214 */   private SourceDataLine sourceDataLine = null;
/*      */ 
/*  216 */   private SoftAudioPusher pusher = null;
/*  217 */   private AudioInputStream pusher_stream = null;
/*      */ 
/*  219 */   private float controlrate = 147.0F;
/*      */ 
/*  221 */   private boolean open = false;
/*  222 */   private boolean implicitOpen = false;
/*      */ 
/*  224 */   private String resamplerType = "linear";
/*  225 */   private SoftResampler resampler = new SoftLinearResampler();
/*      */ 
/*  227 */   private int number_of_midi_channels = 16;
/*  228 */   private int maxpoly = 64;
/*  229 */   private long latency = 200000L;
/*  230 */   private boolean jitter_correction = false;
/*      */   private SoftMainMixer mainmixer;
/*      */   private SoftVoice[] voices;
/*  235 */   private Map<String, SoftTuning> tunings = new HashMap();
/*      */ 
/*  237 */   private Map<String, SoftInstrument> inslist = new HashMap();
/*      */ 
/*  239 */   private Map<String, ModelInstrument> loadedlist = new HashMap();
/*      */ 
/*  242 */   private ArrayList<Receiver> recvslist = new ArrayList();
/*      */ 
/*      */   private void getBuffers(ModelInstrument paramModelInstrument, List<ModelByteBuffer> paramList)
/*      */   {
/*  246 */     for (ModelPerformer localModelPerformer : paramModelInstrument.getPerformers())
/*  247 */       if (localModelPerformer.getOscillators() != null)
/*  248 */         for (ModelOscillator localModelOscillator : localModelPerformer.getOscillators())
/*  249 */           if ((localModelOscillator instanceof ModelByteBufferWavetable)) {
/*  250 */             ModelByteBufferWavetable localModelByteBufferWavetable = (ModelByteBufferWavetable)localModelOscillator;
/*  251 */             ModelByteBuffer localModelByteBuffer = localModelByteBufferWavetable.getBuffer();
/*  252 */             if (localModelByteBuffer != null)
/*  253 */               paramList.add(localModelByteBuffer);
/*  254 */             localModelByteBuffer = localModelByteBufferWavetable.get8BitExtensionBuffer();
/*  255 */             if (localModelByteBuffer != null)
/*  256 */               paramList.add(localModelByteBuffer);
/*      */           }
/*      */   }
/*      */ 
/*      */   private boolean loadSamples(List<ModelInstrument> paramList)
/*      */   {
/*  264 */     if (this.largemode)
/*  265 */       return true;
/*  266 */     ArrayList localArrayList = new ArrayList();
/*  267 */     for (ModelInstrument localModelInstrument : paramList)
/*  268 */       getBuffers(localModelInstrument, localArrayList);
/*      */     try {
/*  270 */       ModelByteBuffer.loadAll(localArrayList);
/*      */     } catch (IOException localIOException) {
/*  272 */       return false;
/*      */     }
/*  274 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean loadInstruments(List<ModelInstrument> paramList) {
/*  278 */     if (!isOpen())
/*  279 */       return false;
/*  280 */     if (!loadSamples(paramList))
/*  281 */       return false;
/*      */     SoftInstrument localSoftInstrument;
/*  283 */     synchronized (this.control_mutex) {
/*  284 */       if (this.channels != null)
/*  285 */         for (localSoftInstrument : this.channels)
/*      */         {
/*  287 */           localSoftInstrument.current_instrument = null;
/*  288 */           localSoftInstrument.current_director = null;
/*      */         }
/*  290 */       for (??? = paramList.iterator(); ((Iterator)???).hasNext(); ) { Instrument localInstrument = (Instrument)((Iterator)???).next();
/*  291 */         String str = patchToString(localInstrument.getPatch());
/*  292 */         localSoftInstrument = new SoftInstrument((ModelInstrument)localInstrument);
/*      */ 
/*  294 */         this.inslist.put(str, localSoftInstrument);
/*  295 */         this.loadedlist.put(str, (ModelInstrument)localInstrument);
/*      */       }
/*      */     }
/*      */ 
/*  299 */     return true;
/*      */   }
/*      */ 
/*      */   private void processPropertyInfo(Map<String, Object> paramMap) {
/*  303 */     AudioSynthesizerPropertyInfo[] arrayOfAudioSynthesizerPropertyInfo = getPropertyInfo(paramMap);
/*      */ 
/*  305 */     String str = (String)arrayOfAudioSynthesizerPropertyInfo[0].value;
/*  306 */     if (str.equalsIgnoreCase("point"))
/*      */     {
/*  308 */       this.resampler = new SoftPointResampler();
/*  309 */       this.resamplerType = "point";
/*      */     }
/*  311 */     else if (str.equalsIgnoreCase("linear"))
/*      */     {
/*  313 */       this.resampler = new SoftLinearResampler2();
/*  314 */       this.resamplerType = "linear";
/*      */     }
/*  316 */     else if (str.equalsIgnoreCase("linear1"))
/*      */     {
/*  318 */       this.resampler = new SoftLinearResampler();
/*  319 */       this.resamplerType = "linear1";
/*      */     }
/*  321 */     else if (str.equalsIgnoreCase("linear2"))
/*      */     {
/*  323 */       this.resampler = new SoftLinearResampler2();
/*  324 */       this.resamplerType = "linear2";
/*      */     }
/*  326 */     else if (str.equalsIgnoreCase("cubic"))
/*      */     {
/*  328 */       this.resampler = new SoftCubicResampler();
/*  329 */       this.resamplerType = "cubic";
/*      */     }
/*  331 */     else if (str.equalsIgnoreCase("lanczos"))
/*      */     {
/*  333 */       this.resampler = new SoftLanczosResampler();
/*  334 */       this.resamplerType = "lanczos";
/*      */     }
/*  336 */     else if (str.equalsIgnoreCase("sinc"))
/*      */     {
/*  338 */       this.resampler = new SoftSincResampler();
/*  339 */       this.resamplerType = "sinc";
/*      */     }
/*      */ 
/*  342 */     setFormat((AudioFormat)arrayOfAudioSynthesizerPropertyInfo[2].value);
/*  343 */     this.controlrate = ((Float)arrayOfAudioSynthesizerPropertyInfo[1].value).floatValue();
/*  344 */     this.latency = ((Long)arrayOfAudioSynthesizerPropertyInfo[3].value).longValue();
/*  345 */     this.deviceid = ((Integer)arrayOfAudioSynthesizerPropertyInfo[4].value).intValue();
/*  346 */     this.maxpoly = ((Integer)arrayOfAudioSynthesizerPropertyInfo[5].value).intValue();
/*  347 */     this.reverb_on = ((Boolean)arrayOfAudioSynthesizerPropertyInfo[6].value).booleanValue();
/*  348 */     this.chorus_on = ((Boolean)arrayOfAudioSynthesizerPropertyInfo[7].value).booleanValue();
/*  349 */     this.agc_on = ((Boolean)arrayOfAudioSynthesizerPropertyInfo[8].value).booleanValue();
/*  350 */     this.largemode = ((Boolean)arrayOfAudioSynthesizerPropertyInfo[9].value).booleanValue();
/*  351 */     this.number_of_midi_channels = ((Integer)arrayOfAudioSynthesizerPropertyInfo[10].value).intValue();
/*  352 */     this.jitter_correction = ((Boolean)arrayOfAudioSynthesizerPropertyInfo[11].value).booleanValue();
/*  353 */     this.reverb_light = ((Boolean)arrayOfAudioSynthesizerPropertyInfo[12].value).booleanValue();
/*  354 */     this.load_default_soundbank = ((Boolean)arrayOfAudioSynthesizerPropertyInfo[13].value).booleanValue();
/*      */   }
/*      */ 
/*      */   private String patchToString(Patch paramPatch) {
/*  358 */     if (((paramPatch instanceof ModelPatch)) && (((ModelPatch)paramPatch).isPercussion())) {
/*  359 */       return "p." + paramPatch.getProgram() + "." + paramPatch.getBank();
/*      */     }
/*  361 */     return paramPatch.getProgram() + "." + paramPatch.getBank();
/*      */   }
/*      */ 
/*      */   private void setFormat(AudioFormat paramAudioFormat) {
/*  365 */     if (paramAudioFormat.getChannels() > 2) {
/*  366 */       throw new IllegalArgumentException("Only mono and stereo audio supported.");
/*      */     }
/*      */ 
/*  369 */     if (AudioFloatConverter.getConverter(paramAudioFormat) == null)
/*  370 */       throw new IllegalArgumentException("Audio format not supported.");
/*  371 */     this.format = paramAudioFormat;
/*      */   }
/*      */ 
/*      */   void removeReceiver(Receiver paramReceiver) {
/*  375 */     int i = 0;
/*  376 */     synchronized (this.control_mutex) {
/*  377 */       if ((this.recvslist.remove(paramReceiver)) && 
/*  378 */         (this.implicitOpen) && (this.recvslist.isEmpty())) {
/*  379 */         i = 1;
/*      */       }
/*      */     }
/*  382 */     if (i != 0)
/*  383 */       close();
/*      */   }
/*      */ 
/*      */   SoftMainMixer getMainMixer() {
/*  387 */     if (!isOpen())
/*  388 */       return null;
/*  389 */     return this.mainmixer;
/*      */   }
/*      */ 
/*      */   SoftInstrument findInstrument(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*      */     Object localObject1;
/*  398 */     if ((paramInt2 >> 7 == 120) || (paramInt2 >> 7 == 121)) {
/*  399 */       localObject1 = (SoftInstrument)this.inslist.get(paramInt1 + "." + paramInt2);
/*      */ 
/*  401 */       if (localObject1 != null) {
/*  402 */         return localObject1;
/*      */       }
/*      */ 
/*  405 */       if (paramInt2 >> 7 == 120)
/*  406 */         localObject2 = "p.";
/*      */       else {
/*  408 */         localObject2 = "";
/*      */       }
/*      */ 
/*  411 */       localObject1 = (SoftInstrument)this.inslist.get((String)localObject2 + paramInt1 + "." + ((paramInt2 & 0x80) << 7));
/*      */ 
/*  413 */       if (localObject1 != null) {
/*  414 */         return localObject1;
/*      */       }
/*  416 */       localObject1 = (SoftInstrument)this.inslist.get((String)localObject2 + paramInt1 + "." + (paramInt2 & 0x80));
/*      */ 
/*  418 */       if (localObject1 != null) {
/*  419 */         return localObject1;
/*      */       }
/*  421 */       localObject1 = (SoftInstrument)this.inslist.get((String)localObject2 + paramInt1 + ".0");
/*  422 */       if (localObject1 != null) {
/*  423 */         return localObject1;
/*      */       }
/*  425 */       localObject1 = (SoftInstrument)this.inslist.get((String)localObject2 + paramInt1 + "0.0");
/*  426 */       if (localObject1 != null)
/*  427 */         return localObject1;
/*  428 */       return null;
/*      */     }
/*      */ 
/*  433 */     if (paramInt3 == 9)
/*  434 */       localObject1 = "p.";
/*      */     else {
/*  436 */       localObject1 = "";
/*      */     }
/*  438 */     Object localObject2 = (SoftInstrument)this.inslist.get((String)localObject1 + paramInt1 + "." + paramInt2);
/*      */ 
/*  440 */     if (localObject2 != null) {
/*  441 */       return localObject2;
/*      */     }
/*  443 */     localObject2 = (SoftInstrument)this.inslist.get((String)localObject1 + paramInt1 + ".0");
/*  444 */     if (localObject2 != null) {
/*  445 */       return localObject2;
/*      */     }
/*  447 */     localObject2 = (SoftInstrument)this.inslist.get((String)localObject1 + "0.0");
/*  448 */     if (localObject2 != null)
/*  449 */       return localObject2;
/*  450 */     return null;
/*      */   }
/*      */ 
/*      */   int getVoiceAllocationMode() {
/*  454 */     return this.voice_allocation_mode;
/*      */   }
/*      */ 
/*      */   int getGeneralMidiMode() {
/*  458 */     return this.gmmode;
/*      */   }
/*      */ 
/*      */   void setGeneralMidiMode(int paramInt) {
/*  462 */     this.gmmode = paramInt;
/*      */   }
/*      */ 
/*      */   int getDeviceID() {
/*  466 */     return this.deviceid;
/*      */   }
/*      */ 
/*      */   float getControlRate() {
/*  470 */     return this.controlrate;
/*      */   }
/*      */ 
/*      */   SoftVoice[] getVoices() {
/*  474 */     return this.voices;
/*      */   }
/*      */ 
/*      */   SoftTuning getTuning(Patch paramPatch) {
/*  478 */     String str = patchToString(paramPatch);
/*  479 */     SoftTuning localSoftTuning = (SoftTuning)this.tunings.get(str);
/*  480 */     if (localSoftTuning == null) {
/*  481 */       localSoftTuning = new SoftTuning(paramPatch);
/*  482 */       this.tunings.put(str, localSoftTuning);
/*      */     }
/*  484 */     return localSoftTuning;
/*      */   }
/*      */ 
/*      */   public long getLatency() {
/*  488 */     synchronized (this.control_mutex) {
/*  489 */       return this.latency;
/*      */     }
/*      */   }
/*      */ 
/*      */   public AudioFormat getFormat() {
/*  494 */     synchronized (this.control_mutex) {
/*  495 */       return this.format;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getMaxPolyphony() {
/*  500 */     synchronized (this.control_mutex) {
/*  501 */       return this.maxpoly;
/*      */     }
/*      */   }
/*      */ 
/*      */   public MidiChannel[] getChannels()
/*      */   {
/*  507 */     synchronized (this.control_mutex)
/*      */     {
/*  511 */       if (this.external_channels == null) {
/*  512 */         this.external_channels = new SoftChannelProxy[16];
/*  513 */         for (int i = 0; i < this.external_channels.length; i++)
/*  514 */           this.external_channels[i] = new SoftChannelProxy();
/*      */       }
/*      */       MidiChannel[] arrayOfMidiChannel;
/*  517 */       if (isOpen())
/*  518 */         arrayOfMidiChannel = new MidiChannel[this.channels.length];
/*      */       else
/*  520 */         arrayOfMidiChannel = new MidiChannel[16];
/*  521 */       for (int j = 0; j < arrayOfMidiChannel.length; j++)
/*  522 */         arrayOfMidiChannel[j] = this.external_channels[j];
/*  523 */       return arrayOfMidiChannel;
/*      */     }
/*      */   }
/*      */ 
/*      */   public VoiceStatus[] getVoiceStatus() {
/*  528 */     if (!isOpen()) {
/*  529 */       VoiceStatus[] arrayOfVoiceStatus1 = new VoiceStatus[getMaxPolyphony()];
/*      */ 
/*  531 */       for (int i = 0; i < arrayOfVoiceStatus1.length; i++) {
/*  532 */         VoiceStatus localVoiceStatus1 = new VoiceStatus();
/*  533 */         localVoiceStatus1.active = false;
/*  534 */         localVoiceStatus1.bank = 0;
/*  535 */         localVoiceStatus1.channel = 0;
/*  536 */         localVoiceStatus1.note = 0;
/*  537 */         localVoiceStatus1.program = 0;
/*  538 */         localVoiceStatus1.volume = 0;
/*  539 */         arrayOfVoiceStatus1[i] = localVoiceStatus1;
/*      */       }
/*  541 */       return arrayOfVoiceStatus1;
/*      */     }
/*      */ 
/*  544 */     synchronized (this.control_mutex) {
/*  545 */       VoiceStatus[] arrayOfVoiceStatus2 = new VoiceStatus[this.voices.length];
/*  546 */       for (int j = 0; j < this.voices.length; j++) {
/*  547 */         SoftVoice localSoftVoice = this.voices[j];
/*  548 */         VoiceStatus localVoiceStatus2 = new VoiceStatus();
/*  549 */         localVoiceStatus2.active = localSoftVoice.active;
/*  550 */         localVoiceStatus2.bank = localSoftVoice.bank;
/*  551 */         localVoiceStatus2.channel = localSoftVoice.channel;
/*  552 */         localVoiceStatus2.note = localSoftVoice.note;
/*  553 */         localVoiceStatus2.program = localSoftVoice.program;
/*  554 */         localVoiceStatus2.volume = localSoftVoice.volume;
/*  555 */         arrayOfVoiceStatus2[j] = localVoiceStatus2;
/*      */       }
/*  557 */       return arrayOfVoiceStatus2;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isSoundbankSupported(Soundbank paramSoundbank) {
/*  562 */     for (Instrument localInstrument : paramSoundbank.getInstruments())
/*  563 */       if (!(localInstrument instanceof ModelInstrument))
/*  564 */         return false;
/*  565 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean loadInstrument(Instrument paramInstrument) {
/*  569 */     if ((paramInstrument == null) || (!(paramInstrument instanceof ModelInstrument))) {
/*  570 */       throw new IllegalArgumentException("Unsupported instrument: " + paramInstrument);
/*      */     }
/*      */ 
/*  573 */     ArrayList localArrayList = new ArrayList();
/*  574 */     localArrayList.add((ModelInstrument)paramInstrument);
/*  575 */     return loadInstruments(localArrayList);
/*      */   }
/*      */ 
/*      */   public void unloadInstrument(Instrument paramInstrument) {
/*  579 */     if ((paramInstrument == null) || (!(paramInstrument instanceof ModelInstrument))) {
/*  580 */       throw new IllegalArgumentException("Unsupported instrument: " + paramInstrument);
/*      */     }
/*      */ 
/*  583 */     if (!isOpen()) {
/*  584 */       return;
/*      */     }
/*  586 */     String str = patchToString(paramInstrument.getPatch());
/*  587 */     synchronized (this.control_mutex) {
/*  588 */       for (SoftChannel localSoftChannel : this.channels)
/*  589 */         localSoftChannel.current_instrument = null;
/*  590 */       this.inslist.remove(str);
/*  591 */       this.loadedlist.remove(str);
/*  592 */       for (int i = 0; i < this.channels.length; i++)
/*  593 */         this.channels[i].allSoundOff();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean remapInstrument(Instrument paramInstrument1, Instrument paramInstrument2)
/*      */   {
/*  600 */     if (paramInstrument1 == null)
/*  601 */       throw new NullPointerException();
/*  602 */     if (paramInstrument2 == null)
/*  603 */       throw new NullPointerException();
/*  604 */     if (!(paramInstrument1 instanceof ModelInstrument)) {
/*  605 */       throw new IllegalArgumentException("Unsupported instrument: " + paramInstrument1.toString());
/*      */     }
/*      */ 
/*  608 */     if (!(paramInstrument2 instanceof ModelInstrument)) {
/*  609 */       throw new IllegalArgumentException("Unsupported instrument: " + paramInstrument2.toString());
/*      */     }
/*      */ 
/*  612 */     if (!isOpen()) {
/*  613 */       return false;
/*      */     }
/*  615 */     synchronized (this.control_mutex) {
/*  616 */       if (!this.loadedlist.containsValue(paramInstrument2))
/*  617 */         throw new IllegalArgumentException("Instrument to is not loaded.");
/*  618 */       unloadInstrument(paramInstrument1);
/*  619 */       ModelMappedInstrument localModelMappedInstrument = new ModelMappedInstrument((ModelInstrument)paramInstrument2, paramInstrument1.getPatch());
/*      */ 
/*  621 */       return loadInstrument(localModelMappedInstrument);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Soundbank getDefaultSoundbank() {
/*  626 */     synchronized (SoftSynthesizer.class) {
/*  627 */       if (defaultSoundBank != null) {
/*  628 */         return defaultSoundBank;
/*      */       }
/*  630 */       ArrayList localArrayList = new ArrayList();
/*      */ 
/*  633 */       localArrayList.add(new PrivilegedAction() {
/*      */         public InputStream run() {
/*  635 */           File localFile1 = new File(System.getProperties().getProperty("java.home"));
/*      */ 
/*  637 */           File localFile2 = new File(new File(localFile1, "lib"), "audio");
/*  638 */           if (localFile2.exists()) {
/*  639 */             Object localObject = null;
/*  640 */             File[] arrayOfFile = localFile2.listFiles();
/*  641 */             if (arrayOfFile != null) {
/*  642 */               for (int i = 0; i < arrayOfFile.length; i++) {
/*  643 */                 File localFile3 = arrayOfFile[i];
/*  644 */                 if (localFile3.isFile()) {
/*  645 */                   String str = localFile3.getName().toLowerCase();
/*  646 */                   if ((str.endsWith(".sf2")) || (str.endsWith(".dls")))
/*      */                   {
/*  648 */                     if ((localObject == null) || (localFile3.length() > localObject.length()))
/*      */                     {
/*  651 */                       localObject = localFile3;
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*  657 */             if (localObject != null)
/*      */               try {
/*  659 */                 return new FileInputStream(localObject);
/*      */               }
/*      */               catch (IOException localIOException) {
/*      */               }
/*      */           }
/*  664 */           return null;
/*      */         }
/*      */       });
/*  668 */       localArrayList.add(new PrivilegedAction() {
/*      */         public InputStream run() {
/*  670 */           if (System.getProperties().getProperty("os.name").startsWith("Windows"))
/*      */           {
/*  672 */             File localFile = new File(System.getenv("SystemRoot") + "\\system32\\drivers\\gm.dls");
/*      */ 
/*  674 */             if (localFile.exists())
/*      */               try {
/*  676 */                 return new FileInputStream(localFile);
/*      */               }
/*      */               catch (IOException localIOException) {
/*      */               }
/*      */           }
/*  681 */           return null;
/*      */         }
/*      */       });
/*  685 */       localArrayList.add(new PrivilegedAction()
/*      */       {
/*      */         public InputStream run()
/*      */         {
/*  690 */           File localFile1 = new File(System.getProperty("user.home"), ".gervill");
/*      */ 
/*  692 */           File localFile2 = new File(localFile1, "soundbank-emg.sf2");
/*      */ 
/*  694 */           if (localFile2.exists())
/*      */             try {
/*  696 */               return new FileInputStream(localFile2);
/*      */             }
/*      */             catch (IOException localIOException) {
/*      */             }
/*  700 */           return null;
/*      */         }
/*      */       });
/*  704 */       for (PrivilegedAction localPrivilegedAction : localArrayList) {
/*      */         try {
/*  706 */           InputStream localInputStream = (InputStream)AccessController.doPrivileged(localPrivilegedAction);
/*  707 */           if (localInputStream != null) {
/*      */             Soundbank localSoundbank;
/*      */             try {
/*  710 */               localSoundbank = MidiSystem.getSoundbank(new BufferedInputStream(localInputStream));
/*      */             } finally {
/*  712 */               localInputStream.close();
/*      */             }
/*  714 */             if (localSoundbank != null) {
/*  715 */               defaultSoundBank = localSoundbank;
/*  716 */               return defaultSoundBank;
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (Exception localException2)
/*      */         {
/*      */         }
/*      */       }
/*      */       try
/*      */       {
/*  726 */         defaultSoundBank = EmergencySoundbank.createSoundbank();
/*      */       }
/*      */       catch (Exception localException1) {
/*      */       }
/*  730 */       if (defaultSoundBank != null)
/*      */       {
/*  734 */         OutputStream localOutputStream = (OutputStream)AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public OutputStream run() {
/*      */             try {
/*  738 */               File localFile1 = new File(System.getProperty("user.home"), ".gervill");
/*      */ 
/*  741 */               if (!localFile1.exists())
/*  742 */                 localFile1.mkdirs();
/*  743 */               File localFile2 = new File(localFile1, "soundbank-emg.sf2");
/*      */ 
/*  745 */               if (localFile2.exists())
/*  746 */                 return null;
/*  747 */               return new FileOutputStream(localFile2);
/*      */             } catch (IOException localIOException) {
/*      */             }
/*      */             catch (SecurityException localSecurityException) {
/*      */             }
/*  752 */             return null;
/*      */           }
/*      */         });
/*  755 */         if (localOutputStream != null)
/*      */           try {
/*  757 */             ((SF2Soundbank)defaultSoundBank).save(localOutputStream);
/*  758 */             localOutputStream.close();
/*      */           }
/*      */           catch (IOException localIOException) {
/*      */           }
/*      */       }
/*      */     }
/*  764 */     return defaultSoundBank;
/*      */   }
/*      */ 
/*      */   public Instrument[] getAvailableInstruments() {
/*  768 */     Soundbank localSoundbank = getDefaultSoundbank();
/*  769 */     if (localSoundbank == null)
/*  770 */       return new Instrument[0];
/*  771 */     Instrument[] arrayOfInstrument = localSoundbank.getInstruments();
/*  772 */     Arrays.sort(arrayOfInstrument, new ModelInstrumentComparator());
/*  773 */     return arrayOfInstrument;
/*      */   }
/*      */ 
/*      */   public Instrument[] getLoadedInstruments() {
/*  777 */     if (!isOpen()) {
/*  778 */       return new Instrument[0];
/*      */     }
/*  780 */     synchronized (this.control_mutex) {
/*  781 */       ModelInstrument[] arrayOfModelInstrument = new ModelInstrument[this.loadedlist.values().size()];
/*      */ 
/*  783 */       this.loadedlist.values().toArray(arrayOfModelInstrument);
/*  784 */       Arrays.sort(arrayOfModelInstrument, new ModelInstrumentComparator());
/*  785 */       return arrayOfModelInstrument;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean loadAllInstruments(Soundbank paramSoundbank) {
/*  790 */     ArrayList localArrayList = new ArrayList();
/*  791 */     for (Instrument localInstrument : paramSoundbank.getInstruments()) {
/*  792 */       if ((localInstrument == null) || (!(localInstrument instanceof ModelInstrument))) {
/*  793 */         throw new IllegalArgumentException("Unsupported instrument: " + localInstrument);
/*      */       }
/*      */ 
/*  796 */       localArrayList.add((ModelInstrument)localInstrument);
/*      */     }
/*  798 */     return loadInstruments(localArrayList);
/*      */   }
/*      */ 
/*      */   public void unloadAllInstruments(Soundbank paramSoundbank) {
/*  802 */     if ((paramSoundbank == null) || (!isSoundbankSupported(paramSoundbank))) {
/*  803 */       throw new IllegalArgumentException("Unsupported soundbank: " + paramSoundbank);
/*      */     }
/*  805 */     if (!isOpen()) {
/*  806 */       return;
/*      */     }
/*  808 */     for (Instrument localInstrument : paramSoundbank.getInstruments())
/*  809 */       if ((localInstrument instanceof ModelInstrument))
/*  810 */         unloadInstrument(localInstrument);
/*      */   }
/*      */ 
/*      */   public boolean loadInstruments(Soundbank paramSoundbank, Patch[] paramArrayOfPatch)
/*      */   {
/*  816 */     ArrayList localArrayList = new ArrayList();
/*  817 */     for (Patch localPatch : paramArrayOfPatch) {
/*  818 */       Instrument localInstrument = paramSoundbank.getInstrument(localPatch);
/*  819 */       if ((localInstrument == null) || (!(localInstrument instanceof ModelInstrument))) {
/*  820 */         throw new IllegalArgumentException("Unsupported instrument: " + localInstrument);
/*      */       }
/*      */ 
/*  823 */       localArrayList.add((ModelInstrument)localInstrument);
/*      */     }
/*  825 */     return loadInstruments(localArrayList);
/*      */   }
/*      */ 
/*      */   public void unloadInstruments(Soundbank paramSoundbank, Patch[] paramArrayOfPatch) {
/*  829 */     if ((paramSoundbank == null) || (!isSoundbankSupported(paramSoundbank))) {
/*  830 */       throw new IllegalArgumentException("Unsupported soundbank: " + paramSoundbank);
/*      */     }
/*  832 */     if (!isOpen()) {
/*  833 */       return;
/*      */     }
/*  835 */     for (Patch localPatch : paramArrayOfPatch) {
/*  836 */       Instrument localInstrument = paramSoundbank.getInstrument(localPatch);
/*  837 */       if ((localInstrument instanceof ModelInstrument))
/*  838 */         unloadInstrument(localInstrument);
/*      */     }
/*      */   }
/*      */ 
/*      */   public MidiDevice.Info getDeviceInfo()
/*      */   {
/*  844 */     return info;
/*      */   }
/*      */ 
/*      */   private Properties getStoredProperties() {
/*  848 */     return (Properties)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Properties run() {
/*  851 */         Properties localProperties = new Properties();
/*  852 */         String str1 = "/com/sun/media/sound/softsynthesizer";
/*      */         try {
/*  854 */           Preferences localPreferences1 = Preferences.userRoot();
/*  855 */           if (localPreferences1.nodeExists(str1)) {
/*  856 */             Preferences localPreferences2 = localPreferences1.node(str1);
/*  857 */             String[] arrayOfString1 = localPreferences2.keys();
/*  858 */             for (String str2 : arrayOfString1) {
/*  859 */               String str3 = localPreferences2.get(str2, null);
/*  860 */               if (str3 != null)
/*  861 */                 localProperties.setProperty(str2, str3);
/*      */             }
/*      */           }
/*      */         } catch (BackingStoreException localBackingStoreException) {
/*      */         } catch (SecurityException localSecurityException) {
/*      */         }
/*  867 */         return localProperties;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public AudioSynthesizerPropertyInfo[] getPropertyInfo(Map<String, Object> paramMap) {
/*  873 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/*  882 */     int i = (paramMap == null) && (this.open) ? 1 : 0;
/*      */ 
/*  884 */     AudioSynthesizerPropertyInfo localAudioSynthesizerPropertyInfo1 = new AudioSynthesizerPropertyInfo("interpolation", i != 0 ? this.resamplerType : "linear");
/*  885 */     localAudioSynthesizerPropertyInfo1.choices = new String[] { "linear", "linear1", "linear2", "cubic", "lanczos", "sinc", "point" };
/*      */ 
/*  887 */     localAudioSynthesizerPropertyInfo1.description = "Interpolation method";
/*  888 */     localArrayList.add(localAudioSynthesizerPropertyInfo1);
/*      */ 
/*  890 */     localAudioSynthesizerPropertyInfo1 = new AudioSynthesizerPropertyInfo("control rate", Float.valueOf(i != 0 ? this.controlrate : 147.0F));
/*  891 */     localAudioSynthesizerPropertyInfo1.description = "Control rate";
/*  892 */     localArrayList.add(localAudioSynthesizerPropertyInfo1);
/*      */ 
/*  894 */     localAudioSynthesizerPropertyInfo1 = new AudioSynthesizerPropertyInfo("format", i != 0 ? this.format : new AudioFormat(44100.0F, 16, 2, true, false));
/*      */ 
/*  896 */     localAudioSynthesizerPropertyInfo1.description = "Default audio format";
/*  897 */     localArrayList.add(localAudioSynthesizerPropertyInfo1);
/*      */ 
/*  899 */     localAudioSynthesizerPropertyInfo1 = new AudioSynthesizerPropertyInfo("latency", Long.valueOf(i != 0 ? this.latency : 120000L));
/*  900 */     localAudioSynthesizerPropertyInfo1.description = "Default latency";
/*  901 */     localArrayList.add(localAudioSynthesizerPropertyInfo1);
/*      */ 
/*  903 */     localAudioSynthesizerPropertyInfo1 = new AudioSynthesizerPropertyInfo("device id", Integer.valueOf(i != 0 ? this.deviceid : 0));
/*  904 */     localAudioSynthesizerPropertyInfo1.description = "Device ID for SysEx Messages";
/*  905 */     localArrayList.add(localAudioSynthesizerPropertyInfo1);
/*      */ 
/*  907 */     localAudioSynthesizerPropertyInfo1 = new AudioSynthesizerPropertyInfo("max polyphony", Integer.valueOf(i != 0 ? this.maxpoly : 64));
/*  908 */     localAudioSynthesizerPropertyInfo1.description = "Maximum polyphony";
/*  909 */     localArrayList.add(localAudioSynthesizerPropertyInfo1);
/*      */ 
/*  911 */     localAudioSynthesizerPropertyInfo1 = new AudioSynthesizerPropertyInfo("reverb", Boolean.valueOf(i != 0 ? this.reverb_on : true));
/*  912 */     localAudioSynthesizerPropertyInfo1.description = "Turn reverb effect on or off";
/*  913 */     localArrayList.add(localAudioSynthesizerPropertyInfo1);
/*      */ 
/*  915 */     localAudioSynthesizerPropertyInfo1 = new AudioSynthesizerPropertyInfo("chorus", Boolean.valueOf(i != 0 ? this.chorus_on : true));
/*  916 */     localAudioSynthesizerPropertyInfo1.description = "Turn chorus effect on or off";
/*  917 */     localArrayList.add(localAudioSynthesizerPropertyInfo1);
/*      */ 
/*  919 */     localAudioSynthesizerPropertyInfo1 = new AudioSynthesizerPropertyInfo("auto gain control", Boolean.valueOf(i != 0 ? this.agc_on : true));
/*  920 */     localAudioSynthesizerPropertyInfo1.description = "Turn auto gain control on or off";
/*  921 */     localArrayList.add(localAudioSynthesizerPropertyInfo1);
/*      */ 
/*  923 */     localAudioSynthesizerPropertyInfo1 = new AudioSynthesizerPropertyInfo("large mode", Boolean.valueOf(i != 0 ? this.largemode : false));
/*  924 */     localAudioSynthesizerPropertyInfo1.description = "Turn large mode on or off.";
/*  925 */     localArrayList.add(localAudioSynthesizerPropertyInfo1);
/*      */ 
/*  927 */     localAudioSynthesizerPropertyInfo1 = new AudioSynthesizerPropertyInfo("midi channels", Integer.valueOf(i != 0 ? this.channels.length : 16));
/*  928 */     localAudioSynthesizerPropertyInfo1.description = "Number of midi channels.";
/*  929 */     localArrayList.add(localAudioSynthesizerPropertyInfo1);
/*      */ 
/*  931 */     localAudioSynthesizerPropertyInfo1 = new AudioSynthesizerPropertyInfo("jitter correction", Boolean.valueOf(i != 0 ? this.jitter_correction : true));
/*  932 */     localAudioSynthesizerPropertyInfo1.description = "Turn jitter correction on or off.";
/*  933 */     localArrayList.add(localAudioSynthesizerPropertyInfo1);
/*      */ 
/*  935 */     localAudioSynthesizerPropertyInfo1 = new AudioSynthesizerPropertyInfo("light reverb", Boolean.valueOf(i != 0 ? this.reverb_light : true));
/*  936 */     localAudioSynthesizerPropertyInfo1.description = "Turn light reverb mode on or off";
/*  937 */     localArrayList.add(localAudioSynthesizerPropertyInfo1);
/*      */ 
/*  939 */     localAudioSynthesizerPropertyInfo1 = new AudioSynthesizerPropertyInfo("load default soundbank", Boolean.valueOf(i != 0 ? this.load_default_soundbank : true));
/*  940 */     localAudioSynthesizerPropertyInfo1.description = "Enabled/disable loading default soundbank";
/*  941 */     localArrayList.add(localAudioSynthesizerPropertyInfo1);
/*      */ 
/*  944 */     AudioSynthesizerPropertyInfo[] arrayOfAudioSynthesizerPropertyInfo1 = (AudioSynthesizerPropertyInfo[])localArrayList.toArray(new AudioSynthesizerPropertyInfo[localArrayList.size()]);
/*      */ 
/*  946 */     Properties localProperties = getStoredProperties();
/*      */ 
/*  948 */     for (AudioSynthesizerPropertyInfo localAudioSynthesizerPropertyInfo2 : arrayOfAudioSynthesizerPropertyInfo1) {
/*  949 */       Object localObject1 = paramMap == null ? null : paramMap.get(localAudioSynthesizerPropertyInfo2.name);
/*  950 */       localObject1 = localObject1 != null ? localObject1 : localProperties.getProperty(localAudioSynthesizerPropertyInfo2.name);
/*  951 */       if (localObject1 != null) {
/*  952 */         Class localClass = localAudioSynthesizerPropertyInfo2.valueClass;
/*  953 */         if (localClass.isInstance(localObject1)) {
/*  954 */           localAudioSynthesizerPropertyInfo2.value = localObject1;
/*      */         }
/*      */         else
/*      */         {
/*      */           Object localObject2;
/*  955 */           if ((localObject1 instanceof String)) {
/*  956 */             localObject2 = (String)localObject1;
/*  957 */             if (localClass == Boolean.class) {
/*  958 */               if (((String)localObject2).equalsIgnoreCase("true"))
/*  959 */                 localAudioSynthesizerPropertyInfo2.value = Boolean.TRUE;
/*  960 */               if (((String)localObject2).equalsIgnoreCase("false"))
/*  961 */                 localAudioSynthesizerPropertyInfo2.value = Boolean.FALSE;
/*  962 */             } else if (localClass == AudioFormat.class) {
/*  963 */               int m = 2;
/*  964 */               boolean bool1 = true;
/*  965 */               boolean bool2 = false;
/*  966 */               int n = 16;
/*  967 */               float f = 44100.0F;
/*      */               try {
/*  969 */                 StringTokenizer localStringTokenizer = new StringTokenizer((String)localObject2, ", ");
/*  970 */                 Object localObject3 = "";
/*  971 */                 while (localStringTokenizer.hasMoreTokens()) {
/*  972 */                   String str = localStringTokenizer.nextToken().toLowerCase();
/*  973 */                   if (str.equals("mono"))
/*  974 */                     m = 1;
/*  975 */                   if (str.startsWith("channel"))
/*  976 */                     m = Integer.parseInt((String)localObject3);
/*  977 */                   if (str.contains("unsigned"))
/*  978 */                     bool1 = false;
/*  979 */                   if (str.equals("big-endian"))
/*  980 */                     bool2 = true;
/*  981 */                   if (str.equals("bit"))
/*  982 */                     n = Integer.parseInt((String)localObject3);
/*  983 */                   if (str.equals("hz"))
/*  984 */                     f = Float.parseFloat((String)localObject3);
/*  985 */                   localObject3 = str;
/*      */                 }
/*  987 */                 localAudioSynthesizerPropertyInfo2.value = new AudioFormat(f, n, m, bool1, bool2);
/*      */               }
/*      */               catch (NumberFormatException localNumberFormatException2) {
/*      */               }
/*      */             }
/*      */             else {
/*      */               try {
/*  994 */                 if (localClass == Byte.class)
/*  995 */                   localAudioSynthesizerPropertyInfo2.value = Byte.valueOf((String)localObject2);
/*  996 */                 else if (localClass == Short.class)
/*  997 */                   localAudioSynthesizerPropertyInfo2.value = Short.valueOf((String)localObject2);
/*  998 */                 else if (localClass == Integer.class)
/*  999 */                   localAudioSynthesizerPropertyInfo2.value = Integer.valueOf((String)localObject2);
/* 1000 */                 else if (localClass == Long.class)
/* 1001 */                   localAudioSynthesizerPropertyInfo2.value = Long.valueOf((String)localObject2);
/* 1002 */                 else if (localClass == Float.class)
/* 1003 */                   localAudioSynthesizerPropertyInfo2.value = Float.valueOf((String)localObject2);
/* 1004 */                 else if (localClass == Double.class)
/* 1005 */                   localAudioSynthesizerPropertyInfo2.value = Double.valueOf((String)localObject2); 
/*      */               } catch (NumberFormatException localNumberFormatException1) {  }
/*      */ 
/*      */             }
/* 1008 */           } else if ((localObject1 instanceof Number)) {
/* 1009 */             localObject2 = (Number)localObject1;
/* 1010 */             if (localClass == Byte.class)
/* 1011 */               localAudioSynthesizerPropertyInfo2.value = Byte.valueOf(((Number)localObject2).byteValue());
/* 1012 */             if (localClass == Short.class)
/* 1013 */               localAudioSynthesizerPropertyInfo2.value = Short.valueOf(((Number)localObject2).shortValue());
/* 1014 */             if (localClass == Integer.class)
/* 1015 */               localAudioSynthesizerPropertyInfo2.value = Integer.valueOf(((Number)localObject2).intValue());
/* 1016 */             if (localClass == Long.class)
/* 1017 */               localAudioSynthesizerPropertyInfo2.value = Long.valueOf(((Number)localObject2).longValue());
/* 1018 */             if (localClass == Float.class)
/* 1019 */               localAudioSynthesizerPropertyInfo2.value = Float.valueOf(((Number)localObject2).floatValue());
/* 1020 */             if (localClass == Double.class)
/* 1021 */               localAudioSynthesizerPropertyInfo2.value = Double.valueOf(((Number)localObject2).doubleValue());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1026 */     return arrayOfAudioSynthesizerPropertyInfo1;
/*      */   }
/*      */ 
/*      */   public void open() throws MidiUnavailableException {
/* 1030 */     if (isOpen()) {
/* 1031 */       synchronized (this.control_mutex) {
/* 1032 */         this.implicitOpen = false;
/*      */       }
/* 1034 */       return;
/*      */     }
/* 1036 */     open(null, null);
/*      */   }
/*      */ 
/*      */   public void open(SourceDataLine paramSourceDataLine, Map<String, Object> paramMap) throws MidiUnavailableException {
/* 1040 */     if (isOpen()) {
/* 1041 */       synchronized (this.control_mutex) {
/* 1042 */         this.implicitOpen = false;
/*      */       }
/* 1044 */       return;
/*      */     }
/* 1046 */     synchronized (this.control_mutex) {
/* 1047 */       Object localObject2 = null;
/*      */       try {
/* 1049 */         if (paramSourceDataLine != null)
/*      */         {
/* 1051 */           setFormat(paramSourceDataLine.getFormat());
/*      */         }
/*      */ 
/* 1054 */         Object localObject3 = openStream(getFormat(), paramMap);
/*      */ 
/* 1056 */         this.weakstream = new WeakAudioStream((AudioInputStream)localObject3);
/* 1057 */         localObject3 = this.weakstream.getAudioInputStream();
/*      */ 
/* 1059 */         if (paramSourceDataLine == null)
/*      */         {
/* 1061 */           if (testline != null) {
/* 1062 */             paramSourceDataLine = testline;
/*      */           }
/*      */           else
/*      */           {
/* 1066 */             paramSourceDataLine = AudioSystem.getSourceDataLine(getFormat());
/*      */           }
/*      */         }
/*      */ 
/* 1070 */         double d = this.latency;
/*      */ 
/* 1072 */         if (!paramSourceDataLine.isOpen()) {
/* 1073 */           i = getFormat().getFrameSize() * (int)(getFormat().getFrameRate() * (d / 1000000.0D));
/*      */ 
/* 1077 */           paramSourceDataLine.open(getFormat(), i);
/*      */ 
/* 1081 */           this.sourceDataLine = paramSourceDataLine;
/*      */         }
/* 1083 */         if (!paramSourceDataLine.isActive()) {
/* 1084 */           paramSourceDataLine.start();
/*      */         }
/* 1086 */         int i = 512;
/*      */         try {
/* 1088 */           i = ((AudioInputStream)localObject3).available();
/*      */         }
/*      */         catch (IOException localIOException)
/*      */         {
/*      */         }
/*      */ 
/* 1098 */         int j = paramSourceDataLine.getBufferSize();
/* 1099 */         j -= j % i;
/*      */ 
/* 1101 */         if (j < 3 * i) {
/* 1102 */           j = 3 * i;
/*      */         }
/* 1104 */         if (this.jitter_correction) {
/* 1105 */           localObject3 = new SoftJitterCorrector((AudioInputStream)localObject3, j, i);
/*      */ 
/* 1107 */           if (this.weakstream != null)
/* 1108 */             this.weakstream.jitter_stream = ((AudioInputStream)localObject3);
/*      */         }
/* 1110 */         this.pusher = new SoftAudioPusher(paramSourceDataLine, (AudioInputStream)localObject3, i);
/* 1111 */         this.pusher_stream = ((AudioInputStream)localObject3);
/* 1112 */         this.pusher.start();
/*      */ 
/* 1114 */         if (this.weakstream != null)
/*      */         {
/* 1116 */           this.weakstream.pusher = this.pusher;
/* 1117 */           this.weakstream.sourceDataLine = this.sourceDataLine;
/*      */         }
/*      */       }
/*      */       catch (LineUnavailableException localLineUnavailableException) {
/* 1121 */         localObject2 = localLineUnavailableException;
/*      */       } catch (IllegalArgumentException localIllegalArgumentException) {
/* 1123 */         localObject2 = localIllegalArgumentException;
/*      */       } catch (SecurityException localSecurityException) {
/* 1125 */         localObject2 = localSecurityException;
/*      */       }
/*      */ 
/* 1128 */       if (localObject2 != null) {
/* 1129 */         if (isOpen()) {
/* 1130 */           close();
/*      */         }
/* 1132 */         MidiUnavailableException localMidiUnavailableException = new MidiUnavailableException("Can not open line");
/*      */ 
/* 1134 */         localMidiUnavailableException.initCause(localObject2);
/* 1135 */         throw localMidiUnavailableException;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public AudioInputStream openStream(AudioFormat paramAudioFormat, Map<String, Object> paramMap)
/*      */     throws MidiUnavailableException
/*      */   {
/* 1144 */     if (isOpen()) {
/* 1145 */       throw new MidiUnavailableException("Synthesizer is already open");
/*      */     }
/* 1147 */     synchronized (this.control_mutex)
/*      */     {
/* 1149 */       this.gmmode = 0;
/* 1150 */       this.voice_allocation_mode = 0;
/*      */ 
/* 1152 */       processPropertyInfo(paramMap);
/*      */ 
/* 1154 */       this.open = true;
/* 1155 */       this.implicitOpen = false;
/*      */ 
/* 1157 */       if (paramAudioFormat != null) {
/* 1158 */         setFormat(paramAudioFormat);
/*      */       }
/* 1160 */       if (this.load_default_soundbank)
/*      */       {
/* 1162 */         Soundbank localSoundbank = getDefaultSoundbank();
/* 1163 */         if (localSoundbank != null) {
/* 1164 */           loadAllInstruments(localSoundbank);
/*      */         }
/*      */       }
/*      */ 
/* 1168 */       this.voices = new SoftVoice[this.maxpoly];
/* 1169 */       for (int i = 0; i < this.maxpoly; i++) {
/* 1170 */         this.voices[i] = new SoftVoice(this);
/*      */       }
/* 1172 */       this.mainmixer = new SoftMainMixer(this);
/*      */ 
/* 1174 */       this.channels = new SoftChannel[this.number_of_midi_channels];
/* 1175 */       for (i = 0; i < this.channels.length; i++)
/* 1176 */         this.channels[i] = new SoftChannel(this, i);
/*      */       int k;
/* 1178 */       if (this.external_channels == null)
/*      */       {
/* 1183 */         if (this.channels.length < 16)
/* 1184 */           this.external_channels = new SoftChannelProxy[16];
/*      */         else
/* 1186 */           this.external_channels = new SoftChannelProxy[this.channels.length];
/* 1187 */         for (i = 0; i < this.external_channels.length; i++) {
/* 1188 */           this.external_channels[i] = new SoftChannelProxy();
/*      */         }
/*      */ 
/*      */       }
/* 1193 */       else if (this.channels.length > this.external_channels.length) {
/* 1194 */         SoftChannelProxy[] arrayOfSoftChannelProxy = new SoftChannelProxy[this.channels.length];
/*      */ 
/* 1196 */         for (k = 0; k < this.external_channels.length; k++)
/* 1197 */           arrayOfSoftChannelProxy[k] = this.external_channels[k];
/* 1198 */         for (k = this.external_channels.length; 
/* 1199 */           k < arrayOfSoftChannelProxy.length; k++) {
/* 1200 */           arrayOfSoftChannelProxy[k] = new SoftChannelProxy();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1205 */       for (int j = 0; j < this.channels.length; j++) {
/* 1206 */         this.external_channels[j].setChannel(this.channels[j]);
/*      */       }
/* 1208 */       for (Object localObject2 : getVoices()) {
/* 1209 */         localObject2.resampler = this.resampler.openStreamer();
/*      */       }
/* 1211 */       for (??? = getReceivers().iterator(); ((Iterator)???).hasNext(); ) { Receiver localReceiver = (Receiver)((Iterator)???).next();
/* 1212 */         SoftReceiver localSoftReceiver = (SoftReceiver)localReceiver;
/* 1213 */         localSoftReceiver.open = this.open;
/* 1214 */         localSoftReceiver.mainmixer = this.mainmixer;
/* 1215 */         localSoftReceiver.midimessages = this.mainmixer.midimessages;
/*      */       }
/*      */ 
/* 1218 */       return this.mainmixer.getInputStream();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/* 1224 */     if (!isOpen()) {
/* 1225 */       return;
/*      */     }
/* 1227 */     SoftAudioPusher localSoftAudioPusher = null;
/* 1228 */     AudioInputStream localAudioInputStream = null;
/* 1229 */     synchronized (this.control_mutex) {
/* 1230 */       if (this.pusher != null) {
/* 1231 */         localSoftAudioPusher = this.pusher;
/* 1232 */         localAudioInputStream = this.pusher_stream;
/* 1233 */         this.pusher = null;
/* 1234 */         this.pusher_stream = null;
/*      */       }
/*      */     }
/*      */ 
/* 1238 */     if (localSoftAudioPusher != null)
/*      */     {
/* 1242 */       localSoftAudioPusher.stop();
/*      */       try
/*      */       {
/* 1245 */         localAudioInputStream.close();
/*      */       }
/*      */       catch (IOException )
/*      */       {
/*      */       }
/*      */     }
/* 1251 */     synchronized (this.control_mutex)
/*      */     {
/* 1253 */       if (this.mainmixer != null)
/* 1254 */         this.mainmixer.close();
/* 1255 */       this.open = false;
/* 1256 */       this.implicitOpen = false;
/* 1257 */       this.mainmixer = null;
/* 1258 */       this.voices = null;
/* 1259 */       this.channels = null;
/*      */ 
/* 1261 */       if (this.external_channels != null) {
/* 1262 */         for (int i = 0; i < this.external_channels.length; i++)
/* 1263 */           this.external_channels[i].setChannel(null);
/*      */       }
/* 1265 */       if (this.sourceDataLine != null) {
/* 1266 */         this.sourceDataLine.close();
/* 1267 */         this.sourceDataLine = null;
/*      */       }
/*      */ 
/* 1270 */       this.inslist.clear();
/* 1271 */       this.loadedlist.clear();
/* 1272 */       this.tunings.clear();
/*      */ 
/* 1274 */       while (this.recvslist.size() != 0)
/* 1275 */         ((Receiver)this.recvslist.get(this.recvslist.size() - 1)).close();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isOpen()
/*      */   {
/* 1281 */     synchronized (this.control_mutex) {
/* 1282 */       return this.open;
/*      */     }
/*      */   }
/*      */ 
/*      */   public long getMicrosecondPosition()
/*      */   {
/* 1288 */     if (!isOpen()) {
/* 1289 */       return 0L;
/*      */     }
/* 1291 */     synchronized (this.control_mutex) {
/* 1292 */       return this.mainmixer.getMicrosecondPosition();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getMaxReceivers() {
/* 1297 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getMaxTransmitters() {
/* 1301 */     return 0;
/*      */   }
/*      */ 
/*      */   public Receiver getReceiver() throws MidiUnavailableException
/*      */   {
/* 1306 */     synchronized (this.control_mutex) {
/* 1307 */       SoftReceiver localSoftReceiver = new SoftReceiver(this);
/* 1308 */       localSoftReceiver.open = this.open;
/* 1309 */       this.recvslist.add(localSoftReceiver);
/* 1310 */       return localSoftReceiver;
/*      */     }
/*      */   }
/*      */ 
/*      */   public List<Receiver> getReceivers()
/*      */   {
/* 1316 */     synchronized (this.control_mutex) {
/* 1317 */       ArrayList localArrayList = new ArrayList();
/* 1318 */       localArrayList.addAll(this.recvslist);
/* 1319 */       return localArrayList;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Transmitter getTransmitter() throws MidiUnavailableException
/*      */   {
/* 1325 */     throw new MidiUnavailableException("No transmitter available");
/*      */   }
/*      */ 
/*      */   public List<Transmitter> getTransmitters()
/*      */   {
/* 1330 */     return new ArrayList();
/*      */   }
/*      */ 
/*      */   public Receiver getReceiverReferenceCounting()
/*      */     throws MidiUnavailableException
/*      */   {
/* 1336 */     if (!isOpen()) {
/* 1337 */       open();
/* 1338 */       synchronized (this.control_mutex) {
/* 1339 */         this.implicitOpen = true;
/*      */       }
/*      */     }
/*      */ 
/* 1343 */     return getReceiver();
/*      */   }
/*      */ 
/*      */   public Transmitter getTransmitterReferenceCounting()
/*      */     throws MidiUnavailableException
/*      */   {
/* 1349 */     throw new MidiUnavailableException("No transmitter available");
/*      */   }
/*      */ 
/*      */   private static class Info extends MidiDevice.Info
/*      */   {
/*      */     Info()
/*      */     {
/*  170 */       super("OpenJDK", "Software MIDI Synthesizer", "1.0");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static final class WeakAudioStream extends InputStream
/*      */   {
/*      */     private volatile AudioInputStream stream;
/*   75 */     public SoftAudioPusher pusher = null;
/*   76 */     public AudioInputStream jitter_stream = null;
/*   77 */     public SourceDataLine sourceDataLine = null;
/*   78 */     public volatile long silent_samples = 0L;
/*   79 */     private int framesize = 0;
/*      */     private WeakReference<AudioInputStream> weak_stream_link;
/*      */     private AudioFloatConverter converter;
/*   82 */     private float[] silentbuffer = null;
/*      */     private int samplesize;
/*      */ 
/*      */     public void setInputStream(AudioInputStream paramAudioInputStream)
/*      */     {
/*   87 */       this.stream = paramAudioInputStream;
/*      */     }
/*      */ 
/*      */     public int available() throws IOException {
/*   91 */       AudioInputStream localAudioInputStream = this.stream;
/*   92 */       if (localAudioInputStream != null)
/*   93 */         return localAudioInputStream.available();
/*   94 */       return 0;
/*      */     }
/*      */ 
/*      */     public int read() throws IOException {
/*   98 */       byte[] arrayOfByte = new byte[1];
/*   99 */       if (read(arrayOfByte) == -1)
/*  100 */         return -1;
/*  101 */       return arrayOfByte[0] & 0xFF;
/*      */     }
/*      */ 
/*      */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/*  105 */       AudioInputStream localAudioInputStream = this.stream;
/*  106 */       if (localAudioInputStream != null) {
/*  107 */         return localAudioInputStream.read(paramArrayOfByte, paramInt1, paramInt2);
/*      */       }
/*      */ 
/*  110 */       int i = paramInt2 / this.samplesize;
/*  111 */       if ((this.silentbuffer == null) || (this.silentbuffer.length < i))
/*  112 */         this.silentbuffer = new float[i];
/*  113 */       this.converter.toByteArray(this.silentbuffer, i, paramArrayOfByte, paramInt1);
/*      */ 
/*  115 */       this.silent_samples += paramInt2 / this.framesize;
/*      */ 
/*  117 */       if ((this.pusher != null) && 
/*  118 */         (this.weak_stream_link.get() == null))
/*      */       {
/*  120 */         Runnable local1 = new Runnable()
/*      */         {
/*  122 */           SoftAudioPusher _pusher = SoftSynthesizer.WeakAudioStream.this.pusher;
/*  123 */           AudioInputStream _jitter_stream = SoftSynthesizer.WeakAudioStream.this.jitter_stream;
/*  124 */           SourceDataLine _sourceDataLine = SoftSynthesizer.WeakAudioStream.this.sourceDataLine;
/*      */ 
/*      */           public void run() {
/*  127 */             this._pusher.stop();
/*  128 */             if (this._jitter_stream != null)
/*      */               try {
/*  130 */                 this._jitter_stream.close();
/*      */               } catch (IOException localIOException) {
/*  132 */                 localIOException.printStackTrace();
/*      */               }
/*  134 */             if (this._sourceDataLine != null)
/*  135 */               this._sourceDataLine.close();
/*      */           }
/*      */         };
/*  138 */         this.pusher = null;
/*  139 */         this.jitter_stream = null;
/*  140 */         this.sourceDataLine = null;
/*  141 */         new Thread(local1).start();
/*      */       }
/*  143 */       return paramInt2;
/*      */     }
/*      */ 
/*      */     public WeakAudioStream(AudioInputStream paramAudioInputStream)
/*      */     {
/*  148 */       this.stream = paramAudioInputStream;
/*  149 */       this.weak_stream_link = new WeakReference(paramAudioInputStream);
/*  150 */       this.converter = AudioFloatConverter.getConverter(paramAudioInputStream.getFormat());
/*  151 */       this.samplesize = (paramAudioInputStream.getFormat().getFrameSize() / paramAudioInputStream.getFormat().getChannels());
/*  152 */       this.framesize = paramAudioInputStream.getFormat().getFrameSize();
/*      */     }
/*      */ 
/*      */     public AudioInputStream getAudioInputStream()
/*      */     {
/*  157 */       return new AudioInputStream(this, this.stream.getFormat(), -1L);
/*      */     }
/*      */ 
/*      */     public void close() throws IOException
/*      */     {
/*  162 */       AudioInputStream localAudioInputStream = (AudioInputStream)this.weak_stream_link.get();
/*  163 */       if (localAudioInputStream != null)
/*  164 */         localAudioInputStream.close();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftSynthesizer
 * JD-Core Version:    0.6.2
 */