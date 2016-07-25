/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import javax.sound.sampled.Mixer;
/*     */ import javax.sound.sampled.Mixer.Info;
/*     */ import javax.sound.sampled.spi.MixerProvider;
/*     */ 
/*     */ public final class PortMixerProvider extends MixerProvider
/*     */ {
/*     */   private static PortMixerInfo[] infos;
/*     */   private static PortMixer[] devices;
/*     */ 
/*     */   public PortMixerProvider()
/*     */   {
/*  67 */     synchronized (PortMixerProvider.class) {
/*  68 */       if (Platform.isPortsEnabled()) {
/*  69 */         init();
/*     */       } else {
/*  71 */         infos = new PortMixerInfo[0];
/*  72 */         devices = new PortMixer[0];
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
/*  84 */       infos = new PortMixerInfo[i];
/*  85 */       devices = new PortMixer[i];
/*     */ 
/*  89 */       for (int j = 0; j < infos.length; j++)
/*  90 */         infos[j] = nNewPortMixerInfo(j);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Mixer.Info[] getMixerInfo()
/*     */   {
/*  97 */     synchronized (PortMixerProvider.class) {
/*  98 */       Mixer.Info[] arrayOfInfo = new Mixer.Info[infos.length];
/*  99 */       System.arraycopy(infos, 0, arrayOfInfo, 0, infos.length);
/* 100 */       return arrayOfInfo;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Mixer getMixer(Mixer.Info paramInfo)
/*     */   {
/* 106 */     synchronized (PortMixerProvider.class) {
/* 107 */       for (int i = 0; i < infos.length; i++) {
/* 108 */         if (infos[i].equals(paramInfo)) {
/* 109 */           return getDevice(infos[i]);
/*     */         }
/*     */       }
/*     */     }
/* 113 */     throw new IllegalArgumentException("Mixer " + paramInfo.toString() + " not supported by this provider.");
/*     */   }
/*     */ 
/*     */   private static Mixer getDevice(PortMixerInfo paramPortMixerInfo)
/*     */   {
/* 119 */     int i = paramPortMixerInfo.getIndex();
/* 120 */     if (devices[i] == null) {
/* 121 */       devices[i] = new PortMixer(paramPortMixerInfo);
/*     */     }
/* 123 */     return devices[i];
/*     */   }
/*     */ 
/*     */   private static native int nGetNumDevices();
/*     */ 
/*     */   private static native PortMixerInfo nNewPortMixerInfo(int paramInt);
/*     */ 
/*     */   static
/*     */   {
/*  56 */     Platform.initialize();
/*     */   }
/*     */ 
/*     */   static final class PortMixerInfo extends Mixer.Info
/*     */   {
/*     */     private final int index;
/*     */ 
/*     */     private PortMixerInfo(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4)
/*     */     {
/* 138 */       super(paramString2, paramString3, paramString4);
/* 139 */       this.index = paramInt;
/*     */     }
/*     */ 
/*     */     int getIndex() {
/* 143 */       return this.index;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.PortMixerProvider
 * JD-Core Version:    0.6.2
 */