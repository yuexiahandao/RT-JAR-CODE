/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import javax.sound.midi.MidiDevice;
/*     */ 
/*     */ public final class MidiOutDeviceProvider extends AbstractMidiDeviceProvider
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
/*  70 */     return new MidiOutDeviceInfo(paramInt, MidiOutDeviceProvider.class, null);
/*     */   }
/*     */ 
/*     */   MidiDevice createDevice(AbstractMidiDeviceProvider.Info paramInfo) {
/*  74 */     if ((enabled) && ((paramInfo instanceof MidiOutDeviceInfo))) {
/*  75 */       return new MidiOutDevice(paramInfo);
/*     */     }
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */   int getNumDevices() {
/*  81 */     if (!enabled)
/*     */     {
/*  83 */       return 0;
/*     */     }
/*  85 */     return nGetNumDevices();
/*     */   }
/*     */   MidiDevice[] getDeviceCache() {
/*  88 */     return devices; } 
/*  89 */   void setDeviceCache(MidiDevice[] paramArrayOfMidiDevice) { devices = paramArrayOfMidiDevice; } 
/*  90 */   AbstractMidiDeviceProvider.Info[] getInfoCache() { return infos; } 
/*  91 */   void setInfoCache(AbstractMidiDeviceProvider.Info[] paramArrayOfInfo) { infos = paramArrayOfInfo; }
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
/*     */   static final class MidiOutDeviceInfo extends AbstractMidiDeviceProvider.Info
/*     */   {
/*     */     private final Class providerClass;
/*     */ 
/*     */     private MidiOutDeviceInfo(int paramInt, Class paramClass)
/*     */     {
/* 110 */       super(MidiOutDeviceProvider.nGetVendor(paramInt), MidiOutDeviceProvider.nGetDescription(paramInt), MidiOutDeviceProvider.nGetVersion(paramInt), paramInt);
/* 111 */       this.providerClass = paramClass;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.MidiOutDeviceProvider
 * JD-Core Version:    0.6.2
 */