/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import javax.sound.sampled.Mixer;
/*     */ import javax.sound.sampled.Mixer.Info;
/*     */ import javax.sound.sampled.spi.MixerProvider;
/*     */ 
/*     */ public final class DirectAudioDeviceProvider extends MixerProvider
/*     */ {
/*     */   private static DirectAudioDeviceInfo[] infos;
/*     */   private static DirectAudioDevice[] devices;
/*     */ 
/*     */   public DirectAudioDeviceProvider()
/*     */   {
/*  67 */     synchronized (DirectAudioDeviceProvider.class) {
/*  68 */       if (Platform.isDirectAudioEnabled()) {
/*  69 */         init();
/*     */       } else {
/*  71 */         infos = new DirectAudioDeviceInfo[0];
/*  72 */         devices = new DirectAudioDevice[0];
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void init()
/*     */   {
/*  79 */     int i = nGetNumDevices();
/*     */ 
/*  81 */     if ((infos == null) || (infos.length != i))
/*     */     {
/*  84 */       infos = new DirectAudioDeviceInfo[i];
/*  85 */       devices = new DirectAudioDevice[i];
/*     */ 
/*  88 */       for (int j = 0; j < infos.length; j++)
/*  89 */         infos[j] = nNewDirectAudioDeviceInfo(j);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Mixer.Info[] getMixerInfo()
/*     */   {
/*  96 */     synchronized (DirectAudioDeviceProvider.class) {
/*  97 */       Mixer.Info[] arrayOfInfo = new Mixer.Info[infos.length];
/*  98 */       System.arraycopy(infos, 0, arrayOfInfo, 0, infos.length);
/*  99 */       return arrayOfInfo;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Mixer getMixer(Mixer.Info paramInfo)
/*     */   {
/* 105 */     synchronized (DirectAudioDeviceProvider.class)
/*     */     {
/* 108 */       if (paramInfo == null) {
/* 109 */         for (i = 0; i < infos.length; i++) {
/* 110 */           Mixer localMixer = getDevice(infos[i]);
/* 111 */           if (localMixer.getSourceLineInfo().length > 0) {
/* 112 */             return localMixer;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 118 */       for (int i = 0; i < infos.length; i++) {
/* 119 */         if (infos[i].equals(paramInfo)) {
/* 120 */           return getDevice(infos[i]);
/*     */         }
/*     */       }
/*     */     }
/* 124 */     throw new IllegalArgumentException("Mixer " + paramInfo.toString() + " not supported by this provider.");
/*     */   }
/*     */ 
/*     */   private static Mixer getDevice(DirectAudioDeviceInfo paramDirectAudioDeviceInfo)
/*     */   {
/* 129 */     int i = paramDirectAudioDeviceInfo.getIndex();
/* 130 */     if (devices[i] == null) {
/* 131 */       devices[i] = new DirectAudioDevice(paramDirectAudioDeviceInfo);
/*     */     }
/* 133 */     return devices[i];
/*     */   }
/*     */ 
/*     */   private static native int nGetNumDevices();
/*     */ 
/*     */   private static native DirectAudioDeviceInfo nNewDirectAudioDeviceInfo(int paramInt);
/*     */ 
/*     */   static
/*     */   {
/*  56 */     Platform.initialize();
/*     */   }
/*     */ 
/*     */   static final class DirectAudioDeviceInfo extends Mixer.Info
/*     */   {
/*     */     private final int index;
/*     */     private final int maxSimulLines;
/*     */     private final int deviceID;
/*     */ 
/*     */     private DirectAudioDeviceInfo(int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2, String paramString3, String paramString4)
/*     */     {
/* 154 */       super(paramString2, "Direct Audio Device: " + paramString3, paramString4);
/* 155 */       this.index = paramInt1;
/* 156 */       this.maxSimulLines = paramInt3;
/* 157 */       this.deviceID = paramInt2;
/*     */     }
/*     */ 
/*     */     int getIndex() {
/* 161 */       return this.index;
/*     */     }
/*     */ 
/*     */     int getMaxSimulLines() {
/* 165 */       return this.maxSimulLines;
/*     */     }
/*     */ 
/*     */     int getDeviceID() {
/* 169 */       return this.deviceID;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.DirectAudioDeviceProvider
 * JD-Core Version:    0.6.2
 */