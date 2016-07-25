/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import javax.sound.midi.MidiDevice;
/*     */ 
/*     */ public final class MidiInDeviceProvider extends AbstractMidiDeviceProvider
/*     */ {
/*  40 */   private static AbstractMidiDeviceProvider.Info[] infos = null;
/*     */ 
/*  43 */   private static MidiDevice[] devices = null;
/*     */ 
/*  52 */   private static final boolean enabled = Platform.isMidiIOEnabled();
/*     */ 
/*     */   AbstractMidiDeviceProvider.Info createInfo(int paramInt)
/*     */   {
/*  67 */     if (!enabled) {
/*  68 */       return null;
/*     */     }
/*  70 */     return new MidiInDeviceInfo(paramInt, MidiInDeviceProvider.class, null);
/*     */   }
/*     */ 
/*     */   MidiDevice createDevice(AbstractMidiDeviceProvider.Info paramInfo) {
/*  74 */     if ((enabled) && ((paramInfo instanceof MidiInDeviceInfo))) {
/*  75 */       return new MidiInDevice(paramInfo);
/*     */     }
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */   int getNumDevices() {
/*  81 */     if (!enabled)
/*     */     {
/*  83 */       return 0;
/*     */     }
/*  85 */     int i = nGetNumDevices();
/*     */ 
/*  87 */     return i;
/*     */   }
/*     */   MidiDevice[] getDeviceCache() {
/*  90 */     return devices; } 
/*  91 */   void setDeviceCache(MidiDevice[] paramArrayOfMidiDevice) { devices = paramArrayOfMidiDevice; } 
/*  92 */   AbstractMidiDeviceProvider.Info[] getInfoCache() { return infos; } 
/*  93 */   void setInfoCache(AbstractMidiDeviceProvider.Info[] paramArrayOfInfo) { infos = paramArrayOfInfo; }
/*     */ 
/*     */ 
/*     */   private static native int nGetNumDevices();
/*     */ 
/*     */   private static native String nGetName(int paramInt);
/*     */ 
/*     */   private static native String nGetVendor(int paramInt);
/*     */ 
/*     */   private static native String nGetDescription(int paramInt);
/*     */ 
/*     */   private static native String nGetVersion(int paramInt);
/*     */ 
/*     */   static
/*     */   {
/*  51 */     Platform.initialize();
/*     */   }
/*     */ 
/*     */   static final class MidiInDeviceInfo extends AbstractMidiDeviceProvider.Info
/*     */   {
/*     */     private final Class providerClass;
/*     */ 
/*     */     private MidiInDeviceInfo(int paramInt, Class paramClass)
/*     */     {
/* 112 */       super(MidiInDeviceProvider.nGetVendor(paramInt), MidiInDeviceProvider.nGetDescription(paramInt), MidiInDeviceProvider.nGetVersion(paramInt), paramInt);
/* 113 */       this.providerClass = paramClass;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.MidiInDeviceProvider
 * JD-Core Version:    0.6.2
 */