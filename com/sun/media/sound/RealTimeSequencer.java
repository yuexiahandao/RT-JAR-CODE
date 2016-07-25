/*      */ package com.sun.media.sound;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.WeakHashMap;
/*      */ import javax.sound.midi.ControllerEventListener;
/*      */ import javax.sound.midi.InvalidMidiDataException;
/*      */ import javax.sound.midi.MetaEventListener;
/*      */ import javax.sound.midi.MetaMessage;
/*      */ import javax.sound.midi.MidiDevice.Info;
/*      */ import javax.sound.midi.MidiEvent;
/*      */ import javax.sound.midi.MidiMessage;
/*      */ import javax.sound.midi.MidiSystem;
/*      */ import javax.sound.midi.MidiUnavailableException;
/*      */ import javax.sound.midi.Receiver;
/*      */ import javax.sound.midi.Sequence;
/*      */ import javax.sound.midi.Sequencer;
/*      */ import javax.sound.midi.Sequencer.SyncMode;
/*      */ import javax.sound.midi.ShortMessage;
/*      */ import javax.sound.midi.Synthesizer;
/*      */ import javax.sound.midi.Track;
/*      */ import javax.sound.midi.Transmitter;
/*      */ 
/*      */ final class RealTimeSequencer extends AbstractMidiDevice
/*      */   implements Sequencer, AutoConnectSequencer
/*      */ {
/*      */   private static final boolean DEBUG_PUMP = false;
/*      */   private static final boolean DEBUG_PUMP_ALL = false;
/*   61 */   private static final Map<ThreadGroup, EventDispatcher> dispatchers = new WeakHashMap();
/*      */ 
/*   67 */   static final RealTimeSequencerInfo info = new RealTimeSequencerInfo(null);
/*      */ 
/*   70 */   private static final Sequencer.SyncMode[] masterSyncModes = { Sequencer.SyncMode.INTERNAL_CLOCK };
/*   71 */   private static final Sequencer.SyncMode[] slaveSyncModes = { Sequencer.SyncMode.NO_SYNC };
/*      */ 
/*   73 */   private static final Sequencer.SyncMode masterSyncMode = Sequencer.SyncMode.INTERNAL_CLOCK;
/*   74 */   private static final Sequencer.SyncMode slaveSyncMode = Sequencer.SyncMode.NO_SYNC;
/*      */ 
/*   80 */   private Sequence sequence = null;
/*      */ 
/*   88 */   private double cacheTempoMPQ = -1.0D;
/*      */ 
/*   95 */   private float cacheTempoFactor = -1.0F;
/*      */ 
/*   99 */   private boolean[] trackMuted = null;
/*      */ 
/*  101 */   private boolean[] trackSolo = null;
/*      */ 
/*  104 */   private final MidiUtils.TempoCache tempoCache = new MidiUtils.TempoCache();
/*      */ 
/*  109 */   private boolean running = false;
/*      */   private PlayThread playThread;
/*  119 */   private boolean recording = false;
/*      */ 
/*  125 */   private final List recordingTracks = new ArrayList();
/*      */ 
/*  128 */   private long loopStart = 0L;
/*  129 */   private long loopEnd = -1L;
/*  130 */   private int loopCount = 0;
/*      */ 
/*  136 */   private final ArrayList metaEventListeners = new ArrayList();
/*      */ 
/*  142 */   private final ArrayList controllerEventListeners = new ArrayList();
/*      */ 
/*  146 */   private boolean autoConnect = false;
/*      */ 
/*  149 */   private boolean doAutoConnectAtNextOpen = false;
/*      */ 
/*  152 */   Receiver autoConnectedReceiver = null;
/*      */ 
/*      */   RealTimeSequencer()
/*      */     throws MidiUnavailableException
/*      */   {
/*  158 */     super(info);
/*      */   }
/*      */ 
/*      */   public synchronized void setSequence(Sequence paramSequence)
/*      */     throws InvalidMidiDataException
/*      */   {
/*  172 */     if (paramSequence != this.sequence) {
/*  173 */       if ((this.sequence != null) && (paramSequence == null)) {
/*  174 */         setCaches();
/*  175 */         stop();
/*      */ 
/*  177 */         this.trackMuted = null;
/*  178 */         this.trackSolo = null;
/*  179 */         this.loopStart = 0L;
/*  180 */         this.loopEnd = -1L;
/*  181 */         this.loopCount = 0;
/*  182 */         if (getDataPump() != null) {
/*  183 */           getDataPump().setTickPos(0L);
/*  184 */           getDataPump().resetLoopCount();
/*      */         }
/*      */       }
/*      */ 
/*  188 */       if (this.playThread != null) {
/*  189 */         this.playThread.setSequence(paramSequence);
/*      */       }
/*      */ 
/*  194 */       this.sequence = paramSequence;
/*      */ 
/*  196 */       if (paramSequence != null) {
/*  197 */         this.tempoCache.refresh(paramSequence);
/*      */ 
/*  199 */         setTickPosition(0L);
/*      */ 
/*  201 */         propagateCaches();
/*      */       }
/*      */     }
/*  204 */     else if (paramSequence != null) {
/*  205 */       this.tempoCache.refresh(paramSequence);
/*  206 */       if (this.playThread != null)
/*  207 */         this.playThread.setSequence(paramSequence);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void setSequence(InputStream paramInputStream)
/*      */     throws IOException, InvalidMidiDataException
/*      */   {
/*  219 */     if (paramInputStream == null) {
/*  220 */       setSequence((Sequence)null);
/*  221 */       return;
/*      */     }
/*      */ 
/*  224 */     Sequence localSequence = MidiSystem.getSequence(paramInputStream);
/*      */ 
/*  226 */     setSequence(localSequence);
/*      */   }
/*      */ 
/*      */   public Sequence getSequence()
/*      */   {
/*  234 */     return this.sequence;
/*      */   }
/*      */ 
/*      */   public synchronized void start()
/*      */   {
/*  242 */     if (!isOpen()) {
/*  243 */       throw new IllegalStateException("sequencer not open");
/*      */     }
/*      */ 
/*  247 */     if (this.sequence == null) {
/*  248 */       throw new IllegalStateException("sequence not set");
/*      */     }
/*      */ 
/*  252 */     if (this.running == true) {
/*  253 */       return;
/*      */     }
/*      */ 
/*  257 */     implStart();
/*      */   }
/*      */ 
/*      */   public synchronized void stop()
/*      */   {
/*  266 */     if (!isOpen()) {
/*  267 */       throw new IllegalStateException("sequencer not open");
/*      */     }
/*  269 */     stopRecording();
/*      */ 
/*  272 */     if (!this.running)
/*      */     {
/*  274 */       return;
/*      */     }
/*      */ 
/*  278 */     implStop();
/*      */   }
/*      */ 
/*      */   public boolean isRunning()
/*      */   {
/*  285 */     return this.running;
/*      */   }
/*      */ 
/*      */   public void startRecording()
/*      */   {
/*  290 */     if (!isOpen()) {
/*  291 */       throw new IllegalStateException("Sequencer not open");
/*      */     }
/*      */ 
/*  294 */     start();
/*  295 */     this.recording = true;
/*      */   }
/*      */ 
/*      */   public void stopRecording()
/*      */   {
/*  300 */     if (!isOpen()) {
/*  301 */       throw new IllegalStateException("Sequencer not open");
/*      */     }
/*  303 */     this.recording = false;
/*      */   }
/*      */ 
/*      */   public boolean isRecording()
/*      */   {
/*  308 */     return this.recording;
/*      */   }
/*      */ 
/*      */   public void recordEnable(Track paramTrack, int paramInt)
/*      */   {
/*  313 */     if (!findTrack(paramTrack)) {
/*  314 */       throw new IllegalArgumentException("Track does not exist in the current sequence");
/*      */     }
/*      */ 
/*  317 */     synchronized (this.recordingTracks) {
/*  318 */       RecordingTrack localRecordingTrack = RecordingTrack.get(this.recordingTracks, paramTrack);
/*  319 */       if (localRecordingTrack != null)
/*  320 */         localRecordingTrack.channel = paramInt;
/*      */       else
/*  322 */         this.recordingTracks.add(new RecordingTrack(paramTrack, paramInt));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void recordDisable(Track paramTrack)
/*      */   {
/*  330 */     synchronized (this.recordingTracks) {
/*  331 */       RecordingTrack localRecordingTrack = RecordingTrack.get(this.recordingTracks, paramTrack);
/*  332 */       if (localRecordingTrack != null)
/*  333 */         this.recordingTracks.remove(localRecordingTrack);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean findTrack(Track paramTrack)
/*      */   {
/*  341 */     boolean bool = false;
/*  342 */     if (this.sequence != null) {
/*  343 */       Track[] arrayOfTrack = this.sequence.getTracks();
/*  344 */       for (int i = 0; i < arrayOfTrack.length; i++) {
/*  345 */         if (paramTrack == arrayOfTrack[i]) {
/*  346 */           bool = true;
/*  347 */           break;
/*      */         }
/*      */       }
/*      */     }
/*  351 */     return bool;
/*      */   }
/*      */ 
/*      */   public float getTempoInBPM()
/*      */   {
/*  358 */     return (float)MidiUtils.convertTempo(getTempoInMPQ());
/*      */   }
/*      */ 
/*      */   public void setTempoInBPM(float paramFloat)
/*      */   {
/*  364 */     if (paramFloat <= 0.0F)
/*      */     {
/*  366 */       paramFloat = 1.0F;
/*      */     }
/*      */ 
/*  369 */     setTempoInMPQ((float)MidiUtils.convertTempo(paramFloat));
/*      */   }
/*      */ 
/*      */   public float getTempoInMPQ()
/*      */   {
/*  376 */     if (needCaching())
/*      */     {
/*  378 */       if (this.cacheTempoMPQ != -1.0D) {
/*  379 */         return (float)this.cacheTempoMPQ;
/*      */       }
/*      */ 
/*  382 */       if (this.sequence != null) {
/*  383 */         return this.tempoCache.getTempoMPQAt(getTickPosition());
/*      */       }
/*      */ 
/*  387 */       return 500000.0F;
/*      */     }
/*  389 */     return getDataPump().getTempoMPQ();
/*      */   }
/*      */ 
/*      */   public void setTempoInMPQ(float paramFloat)
/*      */   {
/*  394 */     if (paramFloat <= 0.0F)
/*      */     {
/*  396 */       paramFloat = 1.0F;
/*      */     }
/*      */ 
/*  401 */     if (needCaching())
/*      */     {
/*  403 */       this.cacheTempoMPQ = paramFloat;
/*      */     }
/*      */     else {
/*  406 */       getDataPump().setTempoMPQ(paramFloat);
/*      */ 
/*  409 */       this.cacheTempoMPQ = -1.0D;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setTempoFactor(float paramFloat)
/*      */   {
/*  415 */     if (paramFloat <= 0.0F)
/*      */     {
/*  417 */       return;
/*      */     }
/*      */ 
/*  422 */     if (needCaching()) {
/*  423 */       this.cacheTempoFactor = paramFloat;
/*      */     } else {
/*  425 */       getDataPump().setTempoFactor(paramFloat);
/*      */ 
/*  427 */       this.cacheTempoFactor = -1.0F;
/*      */     }
/*      */   }
/*      */ 
/*      */   public float getTempoFactor()
/*      */   {
/*  435 */     if (needCaching()) {
/*  436 */       if (this.cacheTempoFactor != -1.0F) {
/*  437 */         return this.cacheTempoFactor;
/*      */       }
/*  439 */       return 1.0F;
/*      */     }
/*  441 */     return getDataPump().getTempoFactor();
/*      */   }
/*      */ 
/*      */   public long getTickLength()
/*      */   {
/*  448 */     if (this.sequence == null) {
/*  449 */       return 0L;
/*      */     }
/*      */ 
/*  452 */     return this.sequence.getTickLength();
/*      */   }
/*      */ 
/*      */   public synchronized long getTickPosition()
/*      */   {
/*  459 */     if ((getDataPump() == null) || (this.sequence == null)) {
/*  460 */       return 0L;
/*      */     }
/*      */ 
/*  463 */     return getDataPump().getTickPos();
/*      */   }
/*      */ 
/*      */   public synchronized void setTickPosition(long paramLong)
/*      */   {
/*  468 */     if (paramLong < 0L)
/*      */     {
/*  470 */       return;
/*      */     }
/*      */ 
/*  475 */     if (getDataPump() == null)
/*      */     {
/*  476 */       if (paramLong == 0L);
/*      */     }
/*  480 */     else if (this.sequence == null)
/*      */     {
/*  481 */       if (paramLong == 0L);
/*      */     }
/*      */     else
/*  485 */       getDataPump().setTickPos(paramLong);
/*      */   }
/*      */ 
/*      */   public long getMicrosecondLength()
/*      */   {
/*  493 */     if (this.sequence == null) {
/*  494 */       return 0L;
/*      */     }
/*      */ 
/*  497 */     return this.sequence.getMicrosecondLength();
/*      */   }
/*      */ 
/*      */   public long getMicrosecondPosition()
/*      */   {
/*  504 */     if ((getDataPump() == null) || (this.sequence == null)) {
/*  505 */       return 0L;
/*      */     }
/*  507 */     synchronized (this.tempoCache) {
/*  508 */       return MidiUtils.tick2microsecond(this.sequence, getDataPump().getTickPos(), this.tempoCache);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setMicrosecondPosition(long paramLong)
/*      */   {
/*  514 */     if (paramLong < 0L)
/*      */     {
/*  516 */       return;
/*      */     }
/*      */ 
/*  521 */     if (getDataPump() == null)
/*      */     {
/*  522 */       if (paramLong == 0L);
/*      */     }
/*  526 */     else if (this.sequence == null)
/*      */     {
/*  527 */       if (paramLong == 0L);
/*      */     }
/*      */     else
/*  531 */       synchronized (this.tempoCache) {
/*  532 */         setTickPosition(MidiUtils.microsecond2tick(this.sequence, paramLong, this.tempoCache));
/*      */       }
/*      */   }
/*      */ 
/*      */   public void setMasterSyncMode(Sequencer.SyncMode paramSyncMode)
/*      */   {
/*      */   }
/*      */ 
/*      */   public Sequencer.SyncMode getMasterSyncMode()
/*      */   {
/*  544 */     return masterSyncMode;
/*      */   }
/*      */ 
/*      */   public Sequencer.SyncMode[] getMasterSyncModes()
/*      */   {
/*  549 */     Sequencer.SyncMode[] arrayOfSyncMode = new Sequencer.SyncMode[masterSyncModes.length];
/*  550 */     System.arraycopy(masterSyncModes, 0, arrayOfSyncMode, 0, masterSyncModes.length);
/*  551 */     return arrayOfSyncMode;
/*      */   }
/*      */ 
/*      */   public void setSlaveSyncMode(Sequencer.SyncMode paramSyncMode)
/*      */   {
/*      */   }
/*      */ 
/*      */   public Sequencer.SyncMode getSlaveSyncMode()
/*      */   {
/*  561 */     return slaveSyncMode;
/*      */   }
/*      */ 
/*      */   public Sequencer.SyncMode[] getSlaveSyncModes()
/*      */   {
/*  566 */     Sequencer.SyncMode[] arrayOfSyncMode = new Sequencer.SyncMode[slaveSyncModes.length];
/*  567 */     System.arraycopy(slaveSyncModes, 0, arrayOfSyncMode, 0, slaveSyncModes.length);
/*  568 */     return arrayOfSyncMode;
/*      */   }
/*      */ 
/*      */   int getTrackCount() {
/*  572 */     Sequence localSequence = getSequence();
/*  573 */     if (localSequence != null)
/*      */     {
/*  575 */       return this.sequence.getTracks().length;
/*      */     }
/*  577 */     return 0;
/*      */   }
/*      */ 
/*      */   public synchronized void setTrackMute(int paramInt, boolean paramBoolean)
/*      */   {
/*  583 */     int i = getTrackCount();
/*  584 */     if ((paramInt < 0) || (paramInt >= getTrackCount())) return;
/*  585 */     this.trackMuted = ensureBoolArraySize(this.trackMuted, i);
/*  586 */     this.trackMuted[paramInt] = paramBoolean;
/*  587 */     if (getDataPump() != null)
/*  588 */       getDataPump().muteSoloChanged();
/*      */   }
/*      */ 
/*      */   public synchronized boolean getTrackMute(int paramInt)
/*      */   {
/*  594 */     if ((paramInt < 0) || (paramInt >= getTrackCount())) return false;
/*  595 */     if ((this.trackMuted == null) || (this.trackMuted.length <= paramInt)) return false;
/*  596 */     return this.trackMuted[paramInt];
/*      */   }
/*      */ 
/*      */   public synchronized void setTrackSolo(int paramInt, boolean paramBoolean)
/*      */   {
/*  601 */     int i = getTrackCount();
/*  602 */     if ((paramInt < 0) || (paramInt >= getTrackCount())) return;
/*  603 */     this.trackSolo = ensureBoolArraySize(this.trackSolo, i);
/*  604 */     this.trackSolo[paramInt] = paramBoolean;
/*  605 */     if (getDataPump() != null)
/*  606 */       getDataPump().muteSoloChanged();
/*      */   }
/*      */ 
/*      */   public synchronized boolean getTrackSolo(int paramInt)
/*      */   {
/*  612 */     if ((paramInt < 0) || (paramInt >= getTrackCount())) return false;
/*  613 */     if ((this.trackSolo == null) || (this.trackSolo.length <= paramInt)) return false;
/*  614 */     return this.trackSolo[paramInt];
/*      */   }
/*      */ 
/*      */   public boolean addMetaEventListener(MetaEventListener paramMetaEventListener)
/*      */   {
/*  619 */     synchronized (this.metaEventListeners) {
/*  620 */       if (!this.metaEventListeners.contains(paramMetaEventListener))
/*      */       {
/*  622 */         this.metaEventListeners.add(paramMetaEventListener);
/*      */       }
/*  624 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeMetaEventListener(MetaEventListener paramMetaEventListener)
/*      */   {
/*  630 */     synchronized (this.metaEventListeners) {
/*  631 */       int i = this.metaEventListeners.indexOf(paramMetaEventListener);
/*  632 */       if (i >= 0)
/*  633 */         this.metaEventListeners.remove(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int[] addControllerEventListener(ControllerEventListener paramControllerEventListener, int[] paramArrayOfInt)
/*      */   {
/*  640 */     synchronized (this.controllerEventListeners)
/*      */     {
/*  644 */       ControllerListElement localControllerListElement = null;
/*  645 */       int i = 0;
/*  646 */       for (int j = 0; j < this.controllerEventListeners.size(); j++)
/*      */       {
/*  648 */         localControllerListElement = (ControllerListElement)this.controllerEventListeners.get(j);
/*      */ 
/*  650 */         if (localControllerListElement.listener.equals(paramControllerEventListener)) {
/*  651 */           localControllerListElement.addControllers(paramArrayOfInt);
/*  652 */           i = 1;
/*  653 */           break;
/*      */         }
/*      */       }
/*  656 */       if (i == 0) {
/*  657 */         localControllerListElement = new ControllerListElement(paramControllerEventListener, paramArrayOfInt, null);
/*  658 */         this.controllerEventListeners.add(localControllerListElement);
/*      */       }
/*      */ 
/*  662 */       return localControllerListElement.getControllers();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int[] removeControllerEventListener(ControllerEventListener paramControllerEventListener, int[] paramArrayOfInt)
/*      */   {
/*  668 */     synchronized (this.controllerEventListeners) {
/*  669 */       ControllerListElement localControllerListElement = null;
/*  670 */       int i = 0;
/*  671 */       for (int j = 0; j < this.controllerEventListeners.size(); j++) {
/*  672 */         localControllerListElement = (ControllerListElement)this.controllerEventListeners.get(j);
/*  673 */         if (localControllerListElement.listener.equals(paramControllerEventListener)) {
/*  674 */           localControllerListElement.removeControllers(paramArrayOfInt);
/*  675 */           i = 1;
/*  676 */           break;
/*      */         }
/*      */       }
/*  679 */       if (i == 0) {
/*  680 */         return new int[0];
/*      */       }
/*  682 */       if (paramArrayOfInt == null) {
/*  683 */         j = this.controllerEventListeners.indexOf(localControllerListElement);
/*  684 */         if (j >= 0) {
/*  685 */           this.controllerEventListeners.remove(j);
/*      */         }
/*  687 */         return new int[0];
/*      */       }
/*  689 */       return localControllerListElement.getControllers();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setLoopStartPoint(long paramLong)
/*      */   {
/*  697 */     if ((paramLong > getTickLength()) || ((this.loopEnd != -1L) && (paramLong > this.loopEnd)) || (paramLong < 0L))
/*      */     {
/*  700 */       throw new IllegalArgumentException("invalid loop start point: " + paramLong);
/*      */     }
/*  702 */     this.loopStart = paramLong;
/*      */   }
/*      */ 
/*      */   public long getLoopStartPoint() {
/*  706 */     return this.loopStart;
/*      */   }
/*      */ 
/*      */   public void setLoopEndPoint(long paramLong) {
/*  710 */     if ((paramLong > getTickLength()) || ((this.loopStart > paramLong) && (paramLong != -1L)) || (paramLong < -1L))
/*      */     {
/*  713 */       throw new IllegalArgumentException("invalid loop end point: " + paramLong);
/*      */     }
/*  715 */     this.loopEnd = paramLong;
/*      */   }
/*      */ 
/*      */   public long getLoopEndPoint() {
/*  719 */     return this.loopEnd;
/*      */   }
/*      */ 
/*      */   public void setLoopCount(int paramInt) {
/*  723 */     if ((paramInt != -1) && (paramInt < 0))
/*      */     {
/*  725 */       throw new IllegalArgumentException("illegal value for loop count: " + paramInt);
/*      */     }
/*  727 */     this.loopCount = paramInt;
/*  728 */     if (getDataPump() != null)
/*  729 */       getDataPump().resetLoopCount();
/*      */   }
/*      */ 
/*      */   public int getLoopCount()
/*      */   {
/*  734 */     return this.loopCount;
/*      */   }
/*      */ 
/*      */   protected void implOpen()
/*      */     throws MidiUnavailableException
/*      */   {
/*  748 */     this.playThread = new PlayThread();
/*      */ 
/*  754 */     if (this.sequence != null) {
/*  755 */       this.playThread.setSequence(this.sequence);
/*      */     }
/*      */ 
/*  759 */     propagateCaches();
/*      */ 
/*  761 */     if (this.doAutoConnectAtNextOpen)
/*  762 */       doAutoConnect();
/*      */   }
/*      */ 
/*      */   private void doAutoConnect()
/*      */   {
/*  769 */     Receiver localReceiver = null;
/*      */     try
/*      */     {
/*  775 */       Synthesizer localSynthesizer = MidiSystem.getSynthesizer();
/*  776 */       if ((localSynthesizer instanceof ReferenceCountingDevice)) {
/*  777 */         localReceiver = ((ReferenceCountingDevice)localSynthesizer).getReceiverReferenceCounting();
/*      */       } else {
/*  779 */         localSynthesizer.open();
/*      */         try {
/*  781 */           localReceiver = localSynthesizer.getReceiver();
/*      */         }
/*      */         finally {
/*  784 */           if (localReceiver == null)
/*  785 */             localSynthesizer.close();
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception localException1)
/*      */     {
/*      */     }
/*  792 */     if (localReceiver == null)
/*      */       try
/*      */       {
/*  795 */         localReceiver = MidiSystem.getReceiver();
/*      */       }
/*      */       catch (Exception localException2)
/*      */       {
/*      */       }
/*  800 */     if (localReceiver != null) {
/*  801 */       this.autoConnectedReceiver = localReceiver;
/*      */       try {
/*  803 */         getTransmitter().setReceiver(localReceiver);
/*      */       }
/*      */       catch (Exception localException3) {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void propagateCaches() {
/*  811 */     if ((this.sequence != null) && (isOpen())) {
/*  812 */       if (this.cacheTempoFactor != -1.0F) {
/*  813 */         setTempoFactor(this.cacheTempoFactor);
/*      */       }
/*  815 */       if (this.cacheTempoMPQ == -1.0D)
/*  816 */         setTempoInMPQ(new MidiUtils.TempoCache(this.sequence).getTempoMPQAt(getTickPosition()));
/*      */       else
/*  818 */         setTempoInMPQ((float)this.cacheTempoMPQ);
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void setCaches()
/*      */   {
/*  825 */     this.cacheTempoFactor = getTempoFactor();
/*  826 */     this.cacheTempoMPQ = getTempoInMPQ();
/*      */   }
/*      */ 
/*      */   protected synchronized void implClose()
/*      */   {
/*  834 */     if (this.playThread != null)
/*      */     {
/*  838 */       this.playThread.close();
/*  839 */       this.playThread = null;
/*      */     }
/*      */ 
/*  842 */     super.implClose();
/*      */ 
/*  844 */     this.sequence = null;
/*  845 */     this.running = false;
/*  846 */     this.cacheTempoMPQ = -1.0D;
/*  847 */     this.cacheTempoFactor = -1.0F;
/*  848 */     this.trackMuted = null;
/*  849 */     this.trackSolo = null;
/*  850 */     this.loopStart = 0L;
/*  851 */     this.loopEnd = -1L;
/*  852 */     this.loopCount = 0;
/*      */ 
/*  857 */     this.doAutoConnectAtNextOpen = this.autoConnect;
/*      */ 
/*  859 */     if (this.autoConnectedReceiver != null) {
/*      */       try {
/*  861 */         this.autoConnectedReceiver.close(); } catch (Exception localException) {
/*      */       }
/*  863 */       this.autoConnectedReceiver = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   void implStart()
/*      */   {
/*  872 */     if (this.playThread == null)
/*      */     {
/*  874 */       return;
/*      */     }
/*      */ 
/*  877 */     this.tempoCache.refresh(this.sequence);
/*  878 */     if (!this.running) {
/*  879 */       this.running = true;
/*  880 */       this.playThread.start();
/*      */     }
/*      */   }
/*      */ 
/*      */   void implStop()
/*      */   {
/*  889 */     if (this.playThread == null)
/*      */     {
/*  891 */       return;
/*      */     }
/*      */ 
/*  894 */     this.recording = false;
/*  895 */     if (this.running) {
/*  896 */       this.running = false;
/*  897 */       this.playThread.stop();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static EventDispatcher getEventDispatcher()
/*      */   {
/*  905 */     ThreadGroup localThreadGroup = Thread.currentThread().getThreadGroup();
/*  906 */     synchronized (dispatchers) {
/*  907 */       EventDispatcher localEventDispatcher = (EventDispatcher)dispatchers.get(localThreadGroup);
/*  908 */       if (localEventDispatcher == null) {
/*  909 */         localEventDispatcher = new EventDispatcher();
/*  910 */         dispatchers.put(localThreadGroup, localEventDispatcher);
/*  911 */         localEventDispatcher.start();
/*      */       }
/*  913 */       return localEventDispatcher;
/*      */     }
/*      */   }
/*      */ 
/*      */   void sendMetaEvents(MidiMessage paramMidiMessage)
/*      */   {
/*  922 */     if (this.metaEventListeners.size() == 0) return;
/*      */ 
/*  925 */     getEventDispatcher().sendAudioEvents(paramMidiMessage, this.metaEventListeners);
/*      */   }
/*      */ 
/*      */   void sendControllerEvents(MidiMessage paramMidiMessage)
/*      */   {
/*  932 */     int i = this.controllerEventListeners.size();
/*  933 */     if (i == 0) return;
/*      */ 
/*  937 */     if (!(paramMidiMessage instanceof ShortMessage))
/*      */     {
/*  939 */       return;
/*      */     }
/*  941 */     ShortMessage localShortMessage = (ShortMessage)paramMidiMessage;
/*  942 */     int j = localShortMessage.getData1();
/*  943 */     ArrayList localArrayList = new ArrayList();
/*  944 */     for (int k = 0; k < i; k++) {
/*  945 */       ControllerListElement localControllerListElement = (ControllerListElement)this.controllerEventListeners.get(k);
/*  946 */       for (int m = 0; m < localControllerListElement.controllers.length; m++) {
/*  947 */         if (localControllerListElement.controllers[m] == j) {
/*  948 */           localArrayList.add(localControllerListElement.listener);
/*  949 */           break;
/*      */         }
/*      */       }
/*      */     }
/*  953 */     getEventDispatcher().sendAudioEvents(paramMidiMessage, localArrayList);
/*      */   }
/*      */ 
/*      */   private boolean needCaching()
/*      */   {
/*  959 */     return (!isOpen()) || (this.sequence == null) || (this.playThread == null);
/*      */   }
/*      */ 
/*      */   private DataPump getDataPump()
/*      */   {
/*  969 */     if (this.playThread != null) {
/*  970 */       return this.playThread.getDataPump();
/*      */     }
/*  972 */     return null;
/*      */   }
/*      */ 
/*      */   private MidiUtils.TempoCache getTempoCache() {
/*  976 */     return this.tempoCache;
/*      */   }
/*      */ 
/*      */   private static boolean[] ensureBoolArraySize(boolean[] paramArrayOfBoolean, int paramInt) {
/*  980 */     if (paramArrayOfBoolean == null) {
/*  981 */       return new boolean[paramInt];
/*      */     }
/*  983 */     if (paramArrayOfBoolean.length < paramInt) {
/*  984 */       boolean[] arrayOfBoolean = new boolean[paramInt];
/*  985 */       System.arraycopy(paramArrayOfBoolean, 0, arrayOfBoolean, 0, paramArrayOfBoolean.length);
/*  986 */       return arrayOfBoolean;
/*      */     }
/*  988 */     return paramArrayOfBoolean;
/*      */   }
/*      */ 
/*      */   protected boolean hasReceivers()
/*      */   {
/*  995 */     return true;
/*      */   }
/*      */ 
/*      */   protected Receiver createReceiver() throws MidiUnavailableException
/*      */   {
/* 1000 */     return new SequencerReceiver();
/*      */   }
/*      */ 
/*      */   protected boolean hasTransmitters()
/*      */   {
/* 1005 */     return true;
/*      */   }
/*      */ 
/*      */   protected Transmitter createTransmitter() throws MidiUnavailableException
/*      */   {
/* 1010 */     return new SequencerTransmitter(null);
/*      */   }
/*      */ 
/*      */   public void setAutoConnect(Receiver paramReceiver)
/*      */   {
/* 1016 */     this.autoConnect = (paramReceiver != null);
/* 1017 */     this.autoConnectedReceiver = paramReceiver;
/*      */   }
/*      */ 
/*      */   private class ControllerListElement
/*      */   {
/*      */     int[] controllers;
/*      */     final ControllerEventListener listener;
/*      */ 
/*      */     private ControllerListElement(ControllerEventListener paramArrayOfInt, int[] arg3)
/*      */     {
/* 1107 */       this.listener = paramArrayOfInt;
/*      */       int[] arrayOfInt;
/* 1108 */       if (arrayOfInt == null) {
/* 1109 */         arrayOfInt = new int[''];
/* 1110 */         for (int i = 0; i < 128; i++) {
/* 1111 */           arrayOfInt[i] = i;
/*      */         }
/*      */       }
/* 1114 */       this.controllers = arrayOfInt;
/*      */     }
/*      */ 
/*      */     private void addControllers(int[] paramArrayOfInt)
/*      */     {
/* 1119 */       if (paramArrayOfInt == null) {
/* 1120 */         this.controllers = new int[''];
/* 1121 */         for (int i = 0; i < 128; i++) {
/* 1122 */           this.controllers[i] = i;
/*      */         }
/* 1124 */         return;
/*      */       }
/* 1126 */       int[] arrayOfInt1 = new int[this.controllers.length + paramArrayOfInt.length];
/*      */ 
/* 1130 */       for (int k = 0; k < this.controllers.length; k++) {
/* 1131 */         arrayOfInt1[k] = this.controllers[k];
/*      */       }
/* 1133 */       int j = this.controllers.length;
/*      */ 
/* 1135 */       for (k = 0; k < paramArrayOfInt.length; k++) {
/* 1136 */         m = 0;
/*      */ 
/* 1138 */         for (int n = 0; n < this.controllers.length; n++) {
/* 1139 */           if (paramArrayOfInt[k] == this.controllers[n]) {
/* 1140 */             m = 1;
/* 1141 */             break;
/*      */           }
/*      */         }
/* 1144 */         if (m == 0) {
/* 1145 */           arrayOfInt1[(j++)] = paramArrayOfInt[k];
/*      */         }
/*      */       }
/*      */ 
/* 1149 */       int[] arrayOfInt2 = new int[j];
/* 1150 */       for (int m = 0; m < j; m++) {
/* 1151 */         arrayOfInt2[m] = arrayOfInt1[m];
/*      */       }
/* 1153 */       this.controllers = arrayOfInt2;
/*      */     }
/*      */ 
/*      */     private void removeControllers(int[] paramArrayOfInt)
/*      */     {
/* 1158 */       if (paramArrayOfInt == null) {
/* 1159 */         this.controllers = new int[0];
/*      */       } else {
/* 1161 */         int[] arrayOfInt1 = new int[this.controllers.length];
/* 1162 */         int i = 0;
/*      */ 
/* 1165 */         for (int j = 0; j < this.controllers.length; j++) {
/* 1166 */           k = 0;
/* 1167 */           for (int m = 0; m < paramArrayOfInt.length; m++) {
/* 1168 */             if (this.controllers[j] == paramArrayOfInt[m]) {
/* 1169 */               k = 1;
/* 1170 */               break;
/*      */             }
/*      */           }
/* 1173 */           if (k == 0) {
/* 1174 */             arrayOfInt1[(i++)] = this.controllers[j];
/*      */           }
/*      */         }
/*      */ 
/* 1178 */         int[] arrayOfInt2 = new int[i];
/* 1179 */         for (int k = 0; k < i; k++) {
/* 1180 */           arrayOfInt2[k] = arrayOfInt1[k];
/*      */         }
/* 1182 */         this.controllers = arrayOfInt2;
/*      */       }
/*      */     }
/*      */ 
/*      */     private int[] getControllers()
/*      */     {
/* 1191 */       if (this.controllers == null) {
/* 1192 */         return null;
/*      */       }
/*      */ 
/* 1195 */       int[] arrayOfInt = new int[this.controllers.length];
/*      */ 
/* 1197 */       for (int i = 0; i < this.controllers.length; i++) {
/* 1198 */         arrayOfInt[i] = this.controllers[i];
/*      */       }
/* 1200 */       return arrayOfInt;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DataPump
/*      */   {
/*      */     private float currTempo;
/*      */     private float tempoFactor;
/*      */     private float inverseTempoFactor;
/*      */     private long ignoreTempoEventAt;
/*      */     private int resolution;
/*      */     private float divisionType;
/*      */     private long checkPointMillis;
/*      */     private long checkPointTick;
/*      */     private int[] noteOnCache;
/*      */     private Track[] tracks;
/*      */     private boolean[] trackDisabled;
/*      */     private int[] trackReadPos;
/*      */     private long lastTick;
/* 1431 */     private boolean needReindex = false;
/* 1432 */     private int currLoopCounter = 0;
/*      */ 
/*      */     DataPump()
/*      */     {
/* 1439 */       init();
/*      */     }
/*      */ 
/*      */     synchronized void init() {
/* 1443 */       this.ignoreTempoEventAt = -1L;
/* 1444 */       this.tempoFactor = 1.0F;
/* 1445 */       this.inverseTempoFactor = 1.0F;
/* 1446 */       this.noteOnCache = new int[''];
/* 1447 */       this.tracks = null;
/* 1448 */       this.trackDisabled = null;
/*      */     }
/*      */ 
/*      */     synchronized void setTickPos(long paramLong) {
/* 1452 */       long l = paramLong;
/* 1453 */       this.lastTick = paramLong;
/* 1454 */       if (RealTimeSequencer.this.running) {
/* 1455 */         notesOff(false);
/*      */       }
/* 1457 */       if ((RealTimeSequencer.this.running) || (paramLong > 0L))
/*      */       {
/* 1459 */         chaseEvents(l, paramLong);
/*      */       }
/* 1461 */       else this.needReindex = true;
/*      */ 
/* 1463 */       if (!hasCachedTempo()) {
/* 1464 */         setTempoMPQ(RealTimeSequencer.this.getTempoCache().getTempoMPQAt(this.lastTick, this.currTempo));
/*      */ 
/* 1466 */         this.ignoreTempoEventAt = -1L;
/*      */       }
/*      */ 
/* 1469 */       this.checkPointMillis = 0L;
/*      */     }
/*      */ 
/*      */     long getTickPos() {
/* 1473 */       return this.lastTick;
/*      */     }
/*      */ 
/*      */     boolean hasCachedTempo()
/*      */     {
/* 1478 */       if (this.ignoreTempoEventAt != this.lastTick) {
/* 1479 */         this.ignoreTempoEventAt = -1L;
/*      */       }
/* 1481 */       return this.ignoreTempoEventAt >= 0L;
/*      */     }
/*      */ 
/*      */     synchronized void setTempoMPQ(float paramFloat)
/*      */     {
/* 1486 */       if ((paramFloat > 0.0F) && (paramFloat != this.currTempo)) {
/* 1487 */         this.ignoreTempoEventAt = this.lastTick;
/* 1488 */         this.currTempo = paramFloat;
/*      */ 
/* 1490 */         this.checkPointMillis = 0L;
/*      */       }
/*      */     }
/*      */ 
/*      */     float getTempoMPQ() {
/* 1495 */       return this.currTempo;
/*      */     }
/*      */ 
/*      */     synchronized void setTempoFactor(float paramFloat) {
/* 1499 */       if ((paramFloat > 0.0F) && (paramFloat != this.tempoFactor)) {
/* 1500 */         this.tempoFactor = paramFloat;
/* 1501 */         this.inverseTempoFactor = (1.0F / paramFloat);
/*      */ 
/* 1503 */         this.checkPointMillis = 0L;
/*      */       }
/*      */     }
/*      */ 
/*      */     float getTempoFactor() {
/* 1508 */       return this.tempoFactor;
/*      */     }
/*      */ 
/*      */     synchronized void muteSoloChanged() {
/* 1512 */       boolean[] arrayOfBoolean = makeDisabledArray();
/* 1513 */       if (RealTimeSequencer.this.running) {
/* 1514 */         applyDisabledTracks(this.trackDisabled, arrayOfBoolean);
/*      */       }
/* 1516 */       this.trackDisabled = arrayOfBoolean;
/*      */     }
/*      */ 
/*      */     synchronized void setSequence(Sequence paramSequence)
/*      */     {
/* 1522 */       if (paramSequence == null) {
/* 1523 */         init();
/* 1524 */         return;
/*      */       }
/* 1526 */       this.tracks = paramSequence.getTracks();
/* 1527 */       muteSoloChanged();
/* 1528 */       this.resolution = paramSequence.getResolution();
/* 1529 */       this.divisionType = paramSequence.getDivisionType();
/* 1530 */       this.trackReadPos = new int[this.tracks.length];
/*      */ 
/* 1532 */       this.checkPointMillis = 0L;
/* 1533 */       this.needReindex = true;
/*      */     }
/*      */ 
/*      */     synchronized void resetLoopCount() {
/* 1537 */       this.currLoopCounter = RealTimeSequencer.this.loopCount;
/*      */     }
/*      */ 
/*      */     void clearNoteOnCache() {
/* 1541 */       for (int i = 0; i < 128; i++)
/* 1542 */         this.noteOnCache[i] = 0;
/*      */     }
/*      */ 
/*      */     void notesOff(boolean paramBoolean)
/*      */     {
/* 1547 */       int i = 0;
/* 1548 */       for (int j = 0; j < 16; j++) {
/* 1549 */         int k = 1 << j;
/* 1550 */         for (int m = 0; m < 128; m++) {
/* 1551 */           if ((this.noteOnCache[m] & k) != 0) {
/* 1552 */             this.noteOnCache[m] ^= k;
/*      */ 
/* 1554 */             RealTimeSequencer.this.getTransmitterList().sendMessage(0x90 | j | m << 8, -1L);
/* 1555 */             i++;
/*      */           }
/*      */         }
/*      */ 
/* 1559 */         RealTimeSequencer.this.getTransmitterList().sendMessage(0xB0 | j | 0x7B00, -1L);
/*      */ 
/* 1561 */         RealTimeSequencer.this.getTransmitterList().sendMessage(0xB0 | j | 0x4000, -1L);
/* 1562 */         if (paramBoolean)
/*      */         {
/* 1564 */           RealTimeSequencer.this.getTransmitterList().sendMessage(0xB0 | j | 0x7900, -1L);
/* 1565 */           i++;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private boolean[] makeDisabledArray()
/*      */     {
/* 1573 */       if (this.tracks == null) {
/* 1574 */         return null;
/*      */       }
/* 1576 */       boolean[] arrayOfBoolean1 = new boolean[this.tracks.length];
/*      */       boolean[] arrayOfBoolean3;
/*      */       boolean[] arrayOfBoolean2;
/* 1579 */       synchronized (RealTimeSequencer.this) {
/* 1580 */         arrayOfBoolean3 = RealTimeSequencer.this.trackMuted;
/* 1581 */         arrayOfBoolean2 = RealTimeSequencer.this.trackSolo;
/*      */       }
/*      */ 
/* 1584 */       int i = 0;
/*      */       int j;
/* 1585 */       if (arrayOfBoolean2 != null) {
/* 1586 */         for (j = 0; j < arrayOfBoolean2.length; j++) {
/* 1587 */           if (arrayOfBoolean2[j] != 0) {
/* 1588 */             i = 1;
/* 1589 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1593 */       if (i != 0)
/*      */       {
/* 1595 */         for (j = 0; j < arrayOfBoolean1.length; j++) {
/* 1596 */           arrayOfBoolean1[j] = ((j >= arrayOfBoolean2.length) || (arrayOfBoolean2[j] == 0) ? 1 : false);
/*      */         }
/*      */       }
/*      */       else {
/* 1600 */         for (j = 0; j < arrayOfBoolean1.length; j++) {
/* 1601 */           arrayOfBoolean1[j] = ((arrayOfBoolean3 != null) && (j < arrayOfBoolean3.length) && (arrayOfBoolean3[j] != 0) ? 1 : false);
/*      */         }
/*      */       }
/* 1604 */       return arrayOfBoolean1;
/*      */     }
/*      */ 
/*      */     private void sendNoteOffIfOn(Track paramTrack, long paramLong)
/*      */     {
/* 1616 */       int i = paramTrack.size();
/* 1617 */       int j = 0;
/*      */       try {
/* 1619 */         for (int k = 0; k < i; k++) {
/* 1620 */           MidiEvent localMidiEvent = paramTrack.get(k);
/* 1621 */           if (localMidiEvent.getTick() > paramLong) break;
/* 1622 */           MidiMessage localMidiMessage = localMidiEvent.getMessage();
/* 1623 */           int m = localMidiMessage.getStatus();
/* 1624 */           int n = localMidiMessage.getLength();
/* 1625 */           if ((n == 3) && ((m & 0xF0) == 144)) {
/* 1626 */             int i1 = -1;
/*      */             Object localObject;
/* 1627 */             if ((localMidiMessage instanceof ShortMessage)) {
/* 1628 */               localObject = (ShortMessage)localMidiMessage;
/* 1629 */               if (((ShortMessage)localObject).getData2() > 0)
/*      */               {
/* 1631 */                 i1 = ((ShortMessage)localObject).getData1();
/*      */               }
/*      */             } else {
/* 1634 */               localObject = localMidiMessage.getMessage();
/* 1635 */               if ((localObject[2] & 0x7F) > 0)
/*      */               {
/* 1637 */                 i1 = localObject[1] & 0x7F;
/*      */               }
/*      */             }
/* 1640 */             if (i1 >= 0) {
/* 1641 */               int i2 = 1 << (m & 0xF);
/* 1642 */               if ((this.noteOnCache[i1] & i2) != 0)
/*      */               {
/* 1644 */                 RealTimeSequencer.this.getTransmitterList().sendMessage(m | i1 << 8, -1L);
/*      */ 
/* 1646 */                 this.noteOnCache[i1] &= (0xFFFF ^ i2);
/* 1647 */                 j++;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*      */     private void applyDisabledTracks(boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2)
/*      */     {
/* 1666 */       byte[][] arrayOfByte = (byte[][])null;
/* 1667 */       synchronized (RealTimeSequencer.this) {
/* 1668 */         for (int i = 0; i < paramArrayOfBoolean2.length; i++)
/* 1669 */           if (((paramArrayOfBoolean1 == null) || (i >= paramArrayOfBoolean1.length) || (paramArrayOfBoolean1[i] == 0)) && (paramArrayOfBoolean2[i] != 0))
/*      */           {
/* 1677 */             if (this.tracks.length > i) {
/* 1678 */               sendNoteOffIfOn(this.tracks[i], this.lastTick);
/*      */             }
/*      */           }
/* 1681 */           else if ((paramArrayOfBoolean1 != null) && (i < paramArrayOfBoolean1.length) && (paramArrayOfBoolean1[i] != 0) && (paramArrayOfBoolean2[i] == 0))
/*      */           {
/* 1687 */             if (arrayOfByte == null) {
/* 1688 */               arrayOfByte = new byte[''][16];
/*      */             }
/* 1690 */             chaseTrackEvents(i, 0L, this.lastTick, true, arrayOfByte);
/*      */           }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void chaseTrackEvents(int paramInt, long paramLong1, long paramLong2, boolean paramBoolean, byte[][] paramArrayOfByte)
/*      */     {
/* 1708 */       if (paramLong1 > paramLong2)
/*      */       {
/* 1710 */         paramLong1 = 0L;
/*      */       }
/* 1712 */       byte[] arrayOfByte = new byte[16];
/*      */ 
/* 1714 */       for (int i = 0; i < 16; i++) {
/* 1715 */         arrayOfByte[i] = -1;
/* 1716 */         for (j = 0; j < 128; j++) {
/* 1717 */           paramArrayOfByte[j][i] = -1; }  } Track localTrack = this.tracks[paramInt];
/* 1721 */       int j = localTrack.size();
/*      */       int i2;
/*      */       int i3;
/*      */       try { for (int k = 0; k < j; k++) {
/* 1724 */           MidiEvent localMidiEvent = localTrack.get(k);
/* 1725 */           if (localMidiEvent.getTick() >= paramLong2) {
/* 1726 */             if ((!paramBoolean) || (paramInt >= this.trackReadPos.length)) break;
/* 1727 */             this.trackReadPos[paramInt] = (k > 0 ? k - 1 : 0); break;
/*      */           }
/*      */ 
/* 1732 */           MidiMessage localMidiMessage = localMidiEvent.getMessage();
/* 1733 */           i2 = localMidiMessage.getStatus();
/* 1734 */           i3 = localMidiMessage.getLength();
/*      */           Object localObject;
/* 1735 */           if ((i3 == 3) && ((i2 & 0xF0) == 176)) {
/* 1736 */             if ((localMidiMessage instanceof ShortMessage)) {
/* 1737 */               localObject = (ShortMessage)localMidiMessage;
/* 1738 */               paramArrayOfByte[(localObject.getData1() & 0x7F)][(i2 & 0xF)] = ((byte)((ShortMessage)localObject).getData2());
/*      */             } else {
/* 1740 */               localObject = localMidiMessage.getMessage();
/* 1741 */               paramArrayOfByte[(localObject[1] & 0x7F)][(i2 & 0xF)] = localObject[2];
/*      */             }
/*      */           }
/* 1744 */           if ((i3 == 2) && ((i2 & 0xF0) == 192)) {
/* 1745 */             if ((localMidiMessage instanceof ShortMessage)) {
/* 1746 */               localObject = (ShortMessage)localMidiMessage;
/* 1747 */               arrayOfByte[(i2 & 0xF)] = ((byte)((ShortMessage)localObject).getData1());
/*      */             } else {
/* 1749 */               localObject = localMidiMessage.getMessage();
/* 1750 */               arrayOfByte[(i2 & 0xF)] = localObject[1];
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*      */       {
/*      */       }
/* 1758 */       int m = 0;
/*      */ 
/* 1760 */       for (int n = 0; n < 16; n++) {
/* 1761 */         for (int i1 = 0; i1 < 128; i1++) {
/* 1762 */           i2 = paramArrayOfByte[i1][n];
/* 1763 */           if (i2 >= 0) {
/* 1764 */             i3 = 0xB0 | n | i1 << 8 | i2 << 16;
/* 1765 */             RealTimeSequencer.this.getTransmitterList().sendMessage(i3, -1L);
/* 1766 */             m++;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1771 */         if (arrayOfByte[n] >= 0) {
/* 1772 */           RealTimeSequencer.this.getTransmitterList().sendMessage(0xC0 | n | arrayOfByte[n] << 8, -1L);
/*      */         }
/* 1774 */         if ((arrayOfByte[n] >= 0) || (paramLong1 == 0L) || (paramLong2 == 0L))
/*      */         {
/* 1776 */           RealTimeSequencer.this.getTransmitterList().sendMessage(0xE0 | n | 0x400000, -1L);
/*      */ 
/* 1778 */           RealTimeSequencer.this.getTransmitterList().sendMessage(0xB0 | n | 0x4000, -1L);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     synchronized void chaseEvents(long paramLong1, long paramLong2)
/*      */     {
/* 1788 */       byte[][] arrayOfByte = new byte[''][16];
/* 1789 */       for (int i = 0; i < this.tracks.length; i++)
/* 1790 */         if ((this.trackDisabled == null) || (this.trackDisabled.length <= i) || (this.trackDisabled[i] == 0))
/*      */         {
/* 1794 */           chaseTrackEvents(i, paramLong1, paramLong2, true, arrayOfByte);
/*      */         }
/*      */     }
/*      */ 
/*      */     private long getCurrentTimeMillis()
/*      */     {
/* 1804 */       return System.nanoTime() / 1000000L;
/*      */     }
/*      */ 
/*      */     private long millis2tick(long paramLong)
/*      */     {
/* 1809 */       if (this.divisionType != 0.0F) {
/* 1810 */         double d = paramLong * this.tempoFactor * this.divisionType * this.resolution / 1000.0D;
/*      */ 
/* 1814 */         return ()d;
/*      */       }
/* 1816 */       return MidiUtils.microsec2ticks(paramLong * 1000L, this.currTempo * this.inverseTempoFactor, this.resolution);
/*      */     }
/*      */ 
/*      */     private long tick2millis(long paramLong)
/*      */     {
/* 1822 */       if (this.divisionType != 0.0F) {
/* 1823 */         double d = paramLong * 1000.0D / (this.tempoFactor * this.divisionType * this.resolution);
/*      */ 
/* 1825 */         return ()d;
/*      */       }
/* 1827 */       return MidiUtils.ticks2microsec(paramLong, this.currTempo * this.inverseTempoFactor, this.resolution) / 1000L;
/*      */     }
/*      */ 
/*      */     private void ReindexTrack(int paramInt, long paramLong)
/*      */     {
/* 1833 */       if ((paramInt < this.trackReadPos.length) && (paramInt < this.tracks.length))
/* 1834 */         this.trackReadPos[paramInt] = MidiUtils.tick2index(this.tracks[paramInt], paramLong);
/*      */     }
/*      */ 
/*      */     private boolean dispatchMessage(int paramInt, MidiEvent paramMidiEvent)
/*      */     {
/* 1841 */       boolean bool = false;
/* 1842 */       MidiMessage localMidiMessage = paramMidiEvent.getMessage();
/* 1843 */       int i = localMidiMessage.getStatus();
/* 1844 */       int j = localMidiMessage.getLength();
/*      */       int k;
/* 1845 */       if ((i == 255) && (j >= 2))
/*      */       {
/* 1852 */         if (paramInt == 0) {
/* 1853 */           k = MidiUtils.getTempoMPQ(localMidiMessage);
/* 1854 */           if (k > 0) {
/* 1855 */             if (paramMidiEvent.getTick() != this.ignoreTempoEventAt) {
/* 1856 */               setTempoMPQ(k);
/* 1857 */               bool = true;
/*      */             }
/*      */ 
/* 1860 */             this.ignoreTempoEventAt = -1L;
/*      */           }
/*      */         }
/*      */ 
/* 1864 */         RealTimeSequencer.this.sendMetaEvents(localMidiMessage);
/*      */       }
/*      */       else
/*      */       {
/* 1868 */         RealTimeSequencer.this.getTransmitterList().sendMessage(localMidiMessage, -1L);
/*      */ 
/* 1870 */         switch (i & 0xF0)
/*      */         {
/*      */         case 128:
/* 1873 */           k = ((ShortMessage)localMidiMessage).getData1() & 0x7F;
/* 1874 */           this.noteOnCache[k] &= (0xFFFF ^ 1 << (i & 0xF));
/* 1875 */           break;
/*      */         case 144:
/* 1880 */           ShortMessage localShortMessage = (ShortMessage)localMidiMessage;
/* 1881 */           int m = localShortMessage.getData1() & 0x7F;
/* 1882 */           int n = localShortMessage.getData2() & 0x7F;
/* 1883 */           if (n > 0)
/*      */           {
/* 1885 */             this.noteOnCache[m] |= 1 << (i & 0xF);
/*      */           }
/*      */           else {
/* 1888 */             this.noteOnCache[m] &= (0xFFFF ^ 1 << (i & 0xF));
/*      */           }
/* 1890 */           break;
/*      */         case 176:
/* 1895 */           RealTimeSequencer.this.sendControllerEvents(localMidiMessage);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1900 */       return bool;
/*      */     }
/*      */ 
/*      */     synchronized boolean pump()
/*      */     {
/* 1909 */       long l2 = this.lastTick;
/*      */ 
/* 1911 */       boolean bool1 = false;
/* 1912 */       int i = 0;
/* 1913 */       boolean bool2 = false;
/*      */ 
/* 1915 */       long l1 = getCurrentTimeMillis();
/* 1916 */       int j = 0;
/*      */       do {
/* 1918 */         bool1 = false;
/*      */ 
/* 1921 */         if (this.needReindex)
/*      */         {
/* 1923 */           if (this.trackReadPos.length < this.tracks.length) {
/* 1924 */             this.trackReadPos = new int[this.tracks.length];
/*      */           }
/* 1926 */           for (k = 0; k < this.tracks.length; k++) {
/* 1927 */             ReindexTrack(k, l2);
/*      */           }
/*      */ 
/* 1930 */           this.needReindex = false;
/* 1931 */           this.checkPointMillis = 0L;
/*      */         }
/*      */ 
/* 1935 */         if (this.checkPointMillis == 0L)
/*      */         {
/* 1937 */           l1 = getCurrentTimeMillis();
/* 1938 */           this.checkPointMillis = l1;
/* 1939 */           l2 = this.lastTick;
/* 1940 */           this.checkPointTick = l2;
/*      */         }
/*      */         else
/*      */         {
/* 1946 */           l2 = this.checkPointTick + millis2tick(l1 - this.checkPointMillis);
/*      */ 
/* 1948 */           if ((RealTimeSequencer.this.loopEnd != -1L) && (((RealTimeSequencer.this.loopCount > 0) && (this.currLoopCounter > 0)) || (RealTimeSequencer.this.loopCount == -1)))
/*      */           {
/* 1951 */             if ((this.lastTick <= RealTimeSequencer.this.loopEnd) && (l2 >= RealTimeSequencer.this.loopEnd))
/*      */             {
/* 1954 */               l2 = RealTimeSequencer.this.loopEnd - 1L;
/* 1955 */               i = 1;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1967 */           this.lastTick = l2;
/*      */         }
/*      */ 
/* 1970 */         j = 0;
/*      */ 
/* 1972 */         for (int k = 0; k < this.tracks.length; k++) {
/*      */           try {
/* 1974 */             int m = this.trackDisabled[k];
/* 1975 */             Track localTrack = this.tracks[k];
/* 1976 */             int n = this.trackReadPos[k];
/* 1977 */             int i1 = localTrack.size();
/*      */             MidiEvent localMidiEvent;
/* 1980 */             while ((!bool1) && (n < i1) && ((localMidiEvent = localTrack.get(n)).getTick() <= l2))
/*      */             {
/* 1982 */               if ((n == i1 - 1) && (MidiUtils.isMetaEndOfTrack(localMidiEvent.getMessage())))
/*      */               {
/* 1984 */                 n = i1;
/* 1985 */                 break;
/*      */               }
/*      */ 
/* 1990 */               n++;
/*      */ 
/* 1995 */               if ((m == 0) || ((k == 0) && (MidiUtils.isMetaTempo(localMidiEvent.getMessage()))))
/*      */               {
/* 1997 */                 bool1 = dispatchMessage(k, localMidiEvent);
/*      */               }
/*      */             }
/* 2000 */             if (n >= i1) {
/* 2001 */               j++;
/*      */             }
/*      */ 
/* 2021 */             this.trackReadPos[k] = n;
/*      */           }
/*      */           catch (Exception localException)
/*      */           {
/* 2025 */             if ((localException instanceof ArrayIndexOutOfBoundsException)) {
/* 2026 */               this.needReindex = true;
/* 2027 */               bool1 = true;
/*      */             }
/*      */           }
/* 2030 */           if (bool1) {
/*      */             break;
/*      */           }
/*      */         }
/* 2034 */         bool2 = j == this.tracks.length;
/* 2035 */         if ((i != 0) || (((RealTimeSequencer.this.loopCount > 0) && (this.currLoopCounter > 0)) || ((RealTimeSequencer.this.loopCount == -1) && (!bool1) && (RealTimeSequencer.this.loopEnd == -1L) && (bool2))))
/*      */         {
/* 2042 */           long l3 = this.checkPointMillis;
/* 2043 */           long l4 = RealTimeSequencer.this.loopEnd;
/* 2044 */           if (l4 == -1L) {
/* 2045 */             l4 = this.lastTick;
/*      */           }
/*      */ 
/* 2049 */           if (RealTimeSequencer.this.loopCount != -1) {
/* 2050 */             this.currLoopCounter -= 1;
/*      */           }
/*      */ 
/* 2056 */           setTickPos(RealTimeSequencer.this.loopStart);
/*      */ 
/* 2065 */           this.checkPointMillis = (l3 + tick2millis(l4 - this.checkPointTick));
/* 2066 */           this.checkPointTick = RealTimeSequencer.this.loopStart;
/*      */ 
/* 2071 */           this.needReindex = false;
/* 2072 */           bool1 = false;
/*      */ 
/* 2074 */           i = 0;
/* 2075 */           bool2 = false;
/*      */         }
/*      */       }
/* 2077 */       while (bool1);
/*      */ 
/* 2079 */       return bool2;
/*      */     }
/*      */   }
/*      */ 
/*      */   final class PlayThread
/*      */     implements Runnable
/*      */   {
/*      */     private Thread thread;
/* 1250 */     private final Object lock = new Object();
/*      */ 
/* 1253 */     boolean interrupted = false;
/* 1254 */     boolean isPumping = false;
/*      */ 
/* 1256 */     private final RealTimeSequencer.DataPump dataPump = new RealTimeSequencer.DataPump(RealTimeSequencer.this);
/*      */ 
/*      */     PlayThread()
/*      */     {
/* 1261 */       int i = 8;
/*      */ 
/* 1263 */       this.thread = JSSecurityManager.createThread(this, "Java Sound Sequencer", false, i, true);
/*      */     }
/*      */ 
/*      */     RealTimeSequencer.DataPump getDataPump()
/*      */     {
/* 1271 */       return this.dataPump;
/*      */     }
/*      */ 
/*      */     synchronized void setSequence(Sequence paramSequence) {
/* 1275 */       this.dataPump.setSequence(paramSequence);
/*      */     }
/*      */ 
/*      */     synchronized void start()
/*      */     {
/* 1282 */       RealTimeSequencer.this.running = true;
/*      */ 
/* 1284 */       if (!this.dataPump.hasCachedTempo()) {
/* 1285 */         long l = RealTimeSequencer.this.getTickPosition();
/* 1286 */         this.dataPump.setTempoMPQ(RealTimeSequencer.this.tempoCache.getTempoMPQAt(l));
/*      */       }
/* 1288 */       this.dataPump.checkPointMillis = 0L;
/* 1289 */       this.dataPump.clearNoteOnCache();
/* 1290 */       this.dataPump.needReindex = true;
/*      */ 
/* 1292 */       this.dataPump.resetLoopCount();
/*      */ 
/* 1295 */       synchronized (this.lock) {
/* 1296 */         this.lock.notifyAll();
/*      */       }
/*      */     }
/*      */ 
/*      */     synchronized void stop()
/*      */     {
/* 1305 */       playThreadImplStop();
/* 1306 */       long l = System.nanoTime() / 1000000L;
/* 1307 */       while (this.isPumping) {
/* 1308 */         synchronized (this.lock) {
/*      */           try {
/* 1310 */             this.lock.wait(2000L);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException)
/*      */           {
/*      */           }
/*      */         }
/* 1316 */         if (System.nanoTime() / 1000000L - l <= 1900L);
/*      */       }
/*      */     }
/*      */ 
/*      */     void playThreadImplStop()
/*      */     {
/* 1325 */       RealTimeSequencer.this.running = false;
/* 1326 */       synchronized (this.lock) {
/* 1327 */         this.lock.notifyAll();
/*      */       }
/*      */     }
/*      */ 
/*      */     void close() {
/* 1332 */       Thread localThread = null;
/* 1333 */       synchronized (this)
/*      */       {
/* 1335 */         this.interrupted = true;
/* 1336 */         localThread = this.thread;
/* 1337 */         this.thread = null;
/*      */       }
/* 1339 */       if (localThread != null)
/*      */       {
/* 1341 */         synchronized (this.lock) {
/* 1342 */           this.lock.notifyAll();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1347 */       if (localThread != null)
/*      */         try {
/* 1349 */           localThread.join(2000L);
/*      */         }
/*      */         catch (InterruptedException localInterruptedException)
/*      */         {
/*      */         }
/*      */     }
/*      */ 
/*      */     public void run()
/*      */     {
/* 1363 */       while (!this.interrupted) {
/* 1364 */         boolean bool1 = false;
/* 1365 */         boolean bool2 = RealTimeSequencer.this.running;
/* 1366 */         this.isPumping = ((!this.interrupted) && (RealTimeSequencer.this.running));
/* 1367 */         while ((!bool1) && (!this.interrupted) && (RealTimeSequencer.this.running)) {
/* 1368 */           bool1 = this.dataPump.pump();
/*      */           try
/*      */           {
/* 1371 */             Thread.sleep(1L);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException)
/*      */           {
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1383 */         playThreadImplStop();
/* 1384 */         if (bool2) {
/* 1385 */           this.dataPump.notesOff(true);
/*      */         }
/* 1387 */         if (bool1) {
/* 1388 */           this.dataPump.setTickPos(RealTimeSequencer.this.sequence.getTickLength());
/*      */ 
/* 1391 */           MetaMessage localMetaMessage = new MetaMessage();
/*      */           try {
/* 1393 */             localMetaMessage.setMessage(47, new byte[0], 0); } catch (InvalidMidiDataException localInvalidMidiDataException) {
/*      */           }
/* 1395 */           RealTimeSequencer.this.sendMetaEvents(localMetaMessage);
/*      */         }
/* 1397 */         synchronized (this.lock) {
/* 1398 */           this.isPumping = false;
/*      */ 
/* 1400 */           this.lock.notifyAll();
/* 1401 */           while ((!RealTimeSequencer.this.running) && (!this.interrupted))
/*      */             try {
/* 1403 */               this.lock.wait();
/*      */             }
/*      */             catch (Exception localException)
/*      */             {
/*      */             }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class RealTimeSequencerInfo extends MidiDevice.Info
/*      */   {
/*      */     private static final String name = "Real Time Sequencer";
/*      */     private static final String vendor = "Oracle Corporation";
/*      */     private static final String description = "Software sequencer";
/*      */     private static final String version = "Version 1.0";
/*      */ 
/*      */     private RealTimeSequencerInfo()
/*      */     {
/* 1092 */       super("Oracle Corporation", "Software sequencer", "Version 1.0");
/*      */     }
/*      */   }
/*      */ 
/*      */   static class RecordingTrack
/*      */   {
/*      */     private final Track track;
/*      */     private int channel;
/*      */ 
/*      */     RecordingTrack(Track paramTrack, int paramInt)
/*      */     {
/* 1212 */       this.track = paramTrack;
/* 1213 */       this.channel = paramInt;
/*      */     }
/*      */ 
/*      */     static RecordingTrack get(List paramList, Track paramTrack)
/*      */     {
/* 1218 */       synchronized (paramList) {
/* 1219 */         int i = paramList.size();
/*      */ 
/* 1221 */         for (int j = 0; j < i; j++) {
/* 1222 */           RecordingTrack localRecordingTrack = (RecordingTrack)paramList.get(j);
/* 1223 */           if (localRecordingTrack.track == paramTrack) {
/* 1224 */             return localRecordingTrack;
/*      */           }
/*      */         }
/*      */       }
/* 1228 */       return null;
/*      */     }
/*      */ 
/*      */     static Track get(List paramList, int paramInt)
/*      */     {
/* 1233 */       synchronized (paramList) {
/* 1234 */         int i = paramList.size();
/* 1235 */         for (int j = 0; j < i; j++) {
/* 1236 */           RecordingTrack localRecordingTrack = (RecordingTrack)paramList.get(j);
/* 1237 */           if ((localRecordingTrack.channel == paramInt) || (localRecordingTrack.channel == -1)) {
/* 1238 */             return localRecordingTrack.track;
/*      */           }
/*      */         }
/*      */       }
/* 1242 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   final class SequencerReceiver extends AbstractMidiDevice.AbstractReceiver
/*      */   {
/*      */     SequencerReceiver()
/*      */     {
/* 1035 */       super();
/*      */     }
/*      */     void implSend(MidiMessage paramMidiMessage, long paramLong) {
/* 1038 */       if (RealTimeSequencer.this.recording) {
/* 1039 */         long l = 0L;
/*      */ 
/* 1042 */         if (paramLong < 0L)
/* 1043 */           l = RealTimeSequencer.this.getTickPosition();
/*      */         else {
/* 1045 */           synchronized (RealTimeSequencer.this.tempoCache) {
/* 1046 */             l = MidiUtils.microsecond2tick(RealTimeSequencer.this.sequence, paramLong, RealTimeSequencer.this.tempoCache);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1051 */         ??? = null;
/*      */ 
/* 1054 */         if (paramMidiMessage.getLength() > 1)
/*      */         {
/*      */           Object localObject2;
/* 1055 */           if ((paramMidiMessage instanceof ShortMessage)) {
/* 1056 */             localObject2 = (ShortMessage)paramMidiMessage;
/*      */ 
/* 1058 */             if ((((ShortMessage)localObject2).getStatus() & 0xF0) != 240) {
/* 1059 */               ??? = RealTimeSequencer.RecordingTrack.get(RealTimeSequencer.this.recordingTracks, ((ShortMessage)localObject2).getChannel());
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1064 */             ??? = RealTimeSequencer.RecordingTrack.get(RealTimeSequencer.this.recordingTracks, -1);
/*      */           }
/* 1066 */           if (??? != null)
/*      */           {
/* 1068 */             if ((paramMidiMessage instanceof ShortMessage))
/* 1069 */               paramMidiMessage = new FastShortMessage((ShortMessage)paramMidiMessage);
/*      */             else {
/* 1071 */               paramMidiMessage = (MidiMessage)paramMidiMessage.clone();
/*      */             }
/*      */ 
/* 1075 */             localObject2 = new MidiEvent(paramMidiMessage, l);
/* 1076 */             ((Track)???).add((MidiEvent)localObject2);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class SequencerTransmitter extends AbstractMidiDevice.BasicTransmitter
/*      */   {
/*      */     private SequencerTransmitter()
/*      */     {
/* 1030 */       super();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.RealTimeSequencer
 * JD-Core Version:    0.6.2
 */