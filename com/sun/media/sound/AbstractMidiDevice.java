/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.sound.midi.InvalidMidiDataException;
/*     */ import javax.sound.midi.MidiDevice;
/*     */ import javax.sound.midi.MidiDevice.Info;
/*     */ import javax.sound.midi.MidiDeviceReceiver;
/*     */ import javax.sound.midi.MidiDeviceTransmitter;
/*     */ import javax.sound.midi.MidiMessage;
/*     */ import javax.sound.midi.MidiUnavailableException;
/*     */ import javax.sound.midi.Receiver;
/*     */ import javax.sound.midi.Transmitter;
/*     */ 
/*     */ abstract class AbstractMidiDevice
/*     */   implements MidiDevice, ReferenceCountingDevice
/*     */ {
/*     */   private static final boolean TRACE_TRANSMITTER = false;
/*     */   private ArrayList<Receiver> receiverList;
/*     */   private TransmitterList transmitterList;
/*  59 */   private final Object traRecLock = new Object();
/*     */   private final MidiDevice.Info info;
/*  68 */   private boolean open = false;
/*     */   private int openRefCount;
/*     */   private List openKeepingObjects;
/*  78 */   protected long id = 0L;
/*     */ 
/*     */   protected AbstractMidiDevice(MidiDevice.Info paramInfo)
/*     */   {
/*  96 */     this.info = paramInfo;
/*  97 */     this.openRefCount = 0;
/*     */   }
/*     */ 
/*     */   public final MidiDevice.Info getDeviceInfo()
/*     */   {
/* 106 */     return this.info;
/*     */   }
/*     */ 
/*     */   public final void open()
/*     */     throws MidiUnavailableException
/*     */   {
/* 116 */     synchronized (this) {
/* 117 */       this.openRefCount = -1;
/* 118 */       doOpen();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void openInternal(Object paramObject)
/*     */     throws MidiUnavailableException
/*     */   {
/* 138 */     synchronized (this) {
/* 139 */       if (this.openRefCount != -1) {
/* 140 */         this.openRefCount += 1;
/* 141 */         getOpenKeepingObjects().add(paramObject);
/*     */       }
/*     */ 
/* 144 */       doOpen();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void doOpen()
/*     */     throws MidiUnavailableException
/*     */   {
/* 152 */     synchronized (this) {
/* 153 */       if (!isOpen()) {
/* 154 */         implOpen();
/* 155 */         this.open = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void close()
/*     */   {
/* 164 */     synchronized (this) {
/* 165 */       doClose();
/* 166 */       this.openRefCount = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void closeInternal(Object paramObject)
/*     */   {
/* 186 */     synchronized (this) {
/* 187 */       if ((getOpenKeepingObjects().remove(paramObject)) && 
/* 188 */         (this.openRefCount > 0)) {
/* 189 */         this.openRefCount -= 1;
/* 190 */         if (this.openRefCount == 0)
/* 191 */           doClose();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void doClose()
/*     */   {
/* 202 */     synchronized (this) {
/* 203 */       if (isOpen()) {
/* 204 */         implClose();
/* 205 */         this.open = false;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean isOpen()
/*     */   {
/* 213 */     return this.open;
/*     */   }
/*     */ 
/*     */   protected void implClose()
/*     */   {
/* 218 */     synchronized (this.traRecLock) {
/* 219 */       if (this.receiverList != null)
/*     */       {
/* 221 */         for (int i = 0; i < this.receiverList.size(); i++) {
/* 222 */           ((Receiver)this.receiverList.get(i)).close();
/*     */         }
/* 224 */         this.receiverList.clear();
/*     */       }
/* 226 */       if (this.transmitterList != null)
/*     */       {
/* 228 */         this.transmitterList.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public long getMicrosecondPosition()
/*     */   {
/* 240 */     return -1L;
/*     */   }
/*     */ 
/*     */   public final int getMaxReceivers()
/*     */   {
/* 249 */     if (hasReceivers()) {
/* 250 */       return -1;
/*     */     }
/* 252 */     return 0;
/*     */   }
/*     */ 
/*     */   public final int getMaxTransmitters()
/*     */   {
/* 262 */     if (hasTransmitters()) {
/* 263 */       return -1;
/*     */     }
/* 265 */     return 0;
/*     */   }
/*     */ 
/*     */   public final Receiver getReceiver()
/*     */     throws MidiUnavailableException
/*     */   {
/*     */     Receiver localReceiver;
/* 279 */     synchronized (this.traRecLock) {
/* 280 */       localReceiver = createReceiver();
/* 281 */       getReceiverList().add(localReceiver);
/*     */     }
/* 283 */     return localReceiver;
/*     */   }
/*     */ 
/*     */   public final List<Receiver> getReceivers()
/*     */   {
/*     */     List localList;
/* 289 */     synchronized (this.traRecLock) {
/* 290 */       if (this.receiverList == null)
/* 291 */         localList = Collections.unmodifiableList(new ArrayList(0));
/*     */       else {
/* 293 */         localList = Collections.unmodifiableList((List)this.receiverList.clone());
/*     */       }
/*     */     }
/*     */ 
/* 297 */     return localList;
/*     */   }
/*     */ 
/*     */   public final Transmitter getTransmitter()
/*     */     throws MidiUnavailableException
/*     */   {
/*     */     Transmitter localTransmitter;
/* 308 */     synchronized (this.traRecLock) {
/* 309 */       localTransmitter = createTransmitter();
/* 310 */       getTransmitterList().add(localTransmitter);
/*     */     }
/* 312 */     return localTransmitter;
/*     */   }
/*     */ 
/*     */   public final List<Transmitter> getTransmitters()
/*     */   {
/*     */     List localList;
/* 318 */     synchronized (this.traRecLock) {
/* 319 */       if ((this.transmitterList == null) || (this.transmitterList.transmitters.size() == 0))
/*     */       {
/* 321 */         localList = Collections.unmodifiableList(new ArrayList(0));
/*     */       }
/* 323 */       else localList = Collections.unmodifiableList((List)this.transmitterList.transmitters.clone());
/*     */     }
/*     */ 
/* 326 */     return localList;
/*     */   }
/*     */ 
/*     */   final long getId()
/*     */   {
/* 333 */     return this.id;
/*     */   }
/*     */ 
/*     */   public final Receiver getReceiverReferenceCounting()
/*     */     throws MidiUnavailableException
/*     */   {
/*     */     Receiver localReceiver;
/* 348 */     synchronized (this.traRecLock) {
/* 349 */       localReceiver = getReceiver();
/* 350 */       openInternal(localReceiver);
/*     */     }
/* 352 */     return localReceiver;
/*     */   }
/*     */ 
/*     */   public final Transmitter getTransmitterReferenceCounting()
/*     */     throws MidiUnavailableException
/*     */   {
/*     */     Transmitter localTransmitter;
/* 365 */     synchronized (this.traRecLock) {
/* 366 */       localTransmitter = getTransmitter();
/* 367 */       openInternal(localTransmitter);
/*     */     }
/* 369 */     return localTransmitter;
/*     */   }
/*     */ 
/*     */   private synchronized List getOpenKeepingObjects()
/*     */   {
/* 376 */     if (this.openKeepingObjects == null) {
/* 377 */       this.openKeepingObjects = new ArrayList();
/*     */     }
/* 379 */     return this.openKeepingObjects;
/*     */   }
/*     */ 
/*     */   private List<Receiver> getReceiverList()
/*     */   {
/* 390 */     synchronized (this.traRecLock) {
/* 391 */       if (this.receiverList == null) {
/* 392 */         this.receiverList = new ArrayList();
/*     */       }
/*     */     }
/* 395 */     return this.receiverList;
/*     */   }
/*     */ 
/*     */   protected boolean hasReceivers()
/*     */   {
/* 406 */     return false;
/*     */   }
/*     */ 
/*     */   protected Receiver createReceiver()
/*     */     throws MidiUnavailableException
/*     */   {
/* 418 */     throw new MidiUnavailableException("MIDI IN receiver not available");
/*     */   }
/*     */ 
/*     */   final TransmitterList getTransmitterList()
/*     */   {
/* 428 */     synchronized (this.traRecLock) {
/* 429 */       if (this.transmitterList == null) {
/* 430 */         this.transmitterList = new TransmitterList();
/*     */       }
/*     */     }
/* 433 */     return this.transmitterList;
/*     */   }
/*     */ 
/*     */   protected boolean hasTransmitters()
/*     */   {
/* 444 */     return false;
/*     */   }
/*     */ 
/*     */   protected Transmitter createTransmitter()
/*     */     throws MidiUnavailableException
/*     */   {
/* 456 */     throw new MidiUnavailableException("MIDI OUT transmitter not available");
/*     */   }
/*     */ 
/*     */   protected abstract void implOpen()
/*     */     throws MidiUnavailableException;
/*     */ 
/*     */   protected final void finalize()
/*     */   {
/* 468 */     close();
/*     */   }
/*     */ 
/*     */   abstract class AbstractReceiver
/*     */     implements MidiDeviceReceiver
/*     */   {
/* 480 */     private boolean open = true;
/*     */ 
/*     */     AbstractReceiver()
/*     */     {
/*     */     }
/*     */ 
/*     */     public final synchronized void send(MidiMessage paramMidiMessage, long paramLong)
/*     */     {
/* 491 */       if (!this.open) {
/* 492 */         throw new IllegalStateException("Receiver is not open");
/*     */       }
/* 494 */       implSend(paramMidiMessage, paramLong);
/*     */     }
/*     */ 
/*     */     abstract void implSend(MidiMessage paramMidiMessage, long paramLong);
/*     */ 
/*     */     public final void close()
/*     */     {
/* 506 */       this.open = false;
/* 507 */       synchronized (AbstractMidiDevice.this.traRecLock) {
/* 508 */         AbstractMidiDevice.this.getReceiverList().remove(this);
/*     */       }
/* 510 */       AbstractMidiDevice.this.closeInternal(this);
/*     */     }
/*     */ 
/*     */     public final MidiDevice getMidiDevice()
/*     */     {
/* 515 */       return AbstractMidiDevice.this;
/*     */     }
/*     */ 
/*     */     final boolean isOpen() {
/* 519 */       return this.open;
/*     */     }
/*     */   }
/*     */ 
/*     */   class BasicTransmitter
/*     */     implements MidiDeviceTransmitter
/*     */   {
/* 541 */     private Receiver receiver = null;
/* 542 */     AbstractMidiDevice.TransmitterList tlist = null;
/*     */ 
/*     */     protected BasicTransmitter() {
/*     */     }
/*     */ 
/*     */     private void setTransmitterList(AbstractMidiDevice.TransmitterList paramTransmitterList) {
/* 548 */       this.tlist = paramTransmitterList;
/*     */     }
/*     */ 
/*     */     public final void setReceiver(Receiver paramReceiver) {
/* 552 */       if ((this.tlist != null) && (this.receiver != paramReceiver))
/*     */       {
/* 554 */         AbstractMidiDevice.TransmitterList.access$400(this.tlist, this, this.receiver, paramReceiver);
/* 555 */         this.receiver = paramReceiver;
/*     */       }
/*     */     }
/*     */ 
/*     */     public final Receiver getReceiver() {
/* 560 */       return this.receiver;
/*     */     }
/*     */ 
/*     */     public final void close()
/*     */     {
/* 570 */       AbstractMidiDevice.this.closeInternal(this);
/* 571 */       if (this.tlist != null) {
/* 572 */         AbstractMidiDevice.TransmitterList.access$400(this.tlist, this, this.receiver, null);
/* 573 */         AbstractMidiDevice.TransmitterList.access$500(this.tlist, this);
/* 574 */         this.tlist = null;
/*     */       }
/*     */     }
/*     */ 
/*     */     public final MidiDevice getMidiDevice() {
/* 579 */       return AbstractMidiDevice.this;
/*     */     }
/*     */   }
/*     */ 
/*     */   final class TransmitterList
/*     */   {
/* 590 */     private final ArrayList<Transmitter> transmitters = new ArrayList();
/*     */     private MidiOutDevice.MidiOutReceiver midiOutReceiver;
/* 595 */     private int optimizedReceiverCount = 0;
/*     */ 
/*     */     TransmitterList() {
/*     */     }
/* 599 */     private void add(Transmitter paramTransmitter) { synchronized (this.transmitters) {
/* 600 */         this.transmitters.add(paramTransmitter);
/*     */       }
/* 602 */       if ((paramTransmitter instanceof AbstractMidiDevice.BasicTransmitter))
/* 603 */         ((AbstractMidiDevice.BasicTransmitter)paramTransmitter).setTransmitterList(this);
/*     */     }
/*     */ 
/*     */     private void remove(Transmitter paramTransmitter)
/*     */     {
/* 609 */       synchronized (this.transmitters) {
/* 610 */         int i = this.transmitters.indexOf(paramTransmitter);
/* 611 */         if (i >= 0)
/* 612 */           this.transmitters.remove(i);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void receiverChanged(AbstractMidiDevice.BasicTransmitter paramBasicTransmitter, Receiver paramReceiver1, Receiver paramReceiver2)
/*     */     {
/* 621 */       synchronized (this.transmitters)
/*     */       {
/* 623 */         if (this.midiOutReceiver == paramReceiver1) {
/* 624 */           this.midiOutReceiver = null;
/*     */         }
/* 626 */         if ((paramReceiver2 != null) && 
/* 627 */           ((paramReceiver2 instanceof MidiOutDevice.MidiOutReceiver)) && (this.midiOutReceiver == null))
/*     */         {
/* 629 */           this.midiOutReceiver = ((MidiOutDevice.MidiOutReceiver)paramReceiver2);
/*     */         }
/*     */ 
/* 632 */         this.optimizedReceiverCount = (this.midiOutReceiver != null ? 1 : 0);
/*     */       }
/*     */     }
/*     */ 
/*     */     void close()
/*     */     {
/* 641 */       synchronized (this.transmitters) {
/* 642 */         for (int i = 0; i < this.transmitters.size(); i++) {
/* 643 */           ((Transmitter)this.transmitters.get(i)).close();
/*     */         }
/* 645 */         this.transmitters.clear();
/*     */       }
/*     */     }
/*     */ 
/*     */     void sendMessage(int paramInt, long paramLong)
/*     */     {
/*     */       try
/*     */       {
/* 660 */         synchronized (this.transmitters) {
/* 661 */           int i = this.transmitters.size();
/* 662 */           if (this.optimizedReceiverCount == i) {
/* 663 */             if (this.midiOutReceiver != null)
/*     */             {
/* 665 */               this.midiOutReceiver.sendPackedMidiMessage(paramInt, paramLong);
/*     */             }
/*     */           }
/*     */           else
/* 669 */             for (int j = 0; j < i; j++) {
/* 670 */               Receiver localReceiver = ((Transmitter)this.transmitters.get(j)).getReceiver();
/* 671 */               if (localReceiver != null)
/* 672 */                 if (this.optimizedReceiverCount > 0) {
/* 673 */                   if ((localReceiver instanceof MidiOutDevice.MidiOutReceiver))
/* 674 */                     ((MidiOutDevice.MidiOutReceiver)localReceiver).sendPackedMidiMessage(paramInt, paramLong);
/*     */                   else
/* 676 */                     localReceiver.send(new FastShortMessage(paramInt), paramLong);
/*     */                 }
/*     */                 else
/* 679 */                   localReceiver.send(new FastShortMessage(paramInt), paramLong);
/*     */             }
/*     */         }
/*     */       }
/*     */       catch (InvalidMidiDataException localInvalidMidiDataException)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/*     */     void sendMessage(byte[] paramArrayOfByte, long paramLong)
/*     */     {
/*     */       try
/*     */       {
/* 692 */         synchronized (this.transmitters) {
/* 693 */           int i = this.transmitters.size();
/*     */ 
/* 695 */           for (int j = 0; j < i; j++) {
/* 696 */             Receiver localReceiver = ((Transmitter)this.transmitters.get(j)).getReceiver();
/* 697 */             if (localReceiver != null)
/*     */             {
/* 703 */               localReceiver.send(new FastSysexMessage(paramArrayOfByte), paramLong);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (InvalidMidiDataException localInvalidMidiDataException)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/*     */     void sendMessage(MidiMessage paramMidiMessage, long paramLong)
/*     */     {
/* 718 */       if ((paramMidiMessage instanceof FastShortMessage)) {
/* 719 */         sendMessage(((FastShortMessage)paramMidiMessage).getPackedMsg(), paramLong);
/* 720 */         return;
/*     */       }
/* 722 */       synchronized (this.transmitters) {
/* 723 */         int i = this.transmitters.size();
/* 724 */         if (this.optimizedReceiverCount == i) {
/* 725 */           if (this.midiOutReceiver != null)
/*     */           {
/* 727 */             this.midiOutReceiver.send(paramMidiMessage, paramLong);
/*     */           }
/*     */         }
/*     */         else
/* 731 */           for (int j = 0; j < i; j++) {
/* 732 */             Receiver localReceiver = ((Transmitter)this.transmitters.get(j)).getReceiver();
/* 733 */             if (localReceiver != null)
/*     */             {
/* 741 */               localReceiver.send(paramMidiMessage, paramLong);
/*     */             }
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.AbstractMidiDevice
 * JD-Core Version:    0.6.2
 */