/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import javax.sound.midi.MidiDevice;
/*     */ import javax.sound.midi.MidiDevice.Info;
/*     */ import javax.sound.midi.spi.MidiDeviceProvider;
/*     */ 
/*     */ public abstract class AbstractMidiDeviceProvider extends MidiDeviceProvider
/*     */ {
/*  47 */   private static final boolean enabled = Platform.isMidiIOEnabled();
/*     */ 
/*     */   final synchronized void readDeviceInfos()
/*     */   {
/*  56 */     Info[] arrayOfInfo1 = getInfoCache();
/*  57 */     MidiDevice[] arrayOfMidiDevice1 = getDeviceCache();
/*  58 */     if (!enabled) {
/*  59 */       if ((arrayOfInfo1 == null) || (arrayOfInfo1.length != 0)) {
/*  60 */         setInfoCache(new Info[0]);
/*     */       }
/*  62 */       if ((arrayOfMidiDevice1 == null) || (arrayOfMidiDevice1.length != 0)) {
/*  63 */         setDeviceCache(new MidiDevice[0]);
/*     */       }
/*  65 */       return;
/*     */     }
/*     */ 
/*  68 */     int i = arrayOfInfo1 == null ? -1 : arrayOfInfo1.length;
/*  69 */     int j = getNumDevices();
/*  70 */     if (i != j)
/*     */     {
/*  76 */       Info[] arrayOfInfo2 = new Info[j];
/*  77 */       MidiDevice[] arrayOfMidiDevice2 = new MidiDevice[j];
/*     */ 
/*  79 */       for (int k = 0; k < j; k++) {
/*  80 */         Info localInfo1 = createInfo(k);
/*     */ 
/*  84 */         if (arrayOfInfo1 != null) {
/*  85 */           for (int m = 0; m < arrayOfInfo1.length; m++) {
/*  86 */             Info localInfo2 = arrayOfInfo1[m];
/*  87 */             if ((localInfo2 != null) && (localInfo2.equalStrings(localInfo1)))
/*     */             {
/*  89 */               arrayOfInfo2[k] = localInfo2;
/*  90 */               localInfo2.setIndex(k);
/*  91 */               arrayOfInfo1[m] = null;
/*  92 */               arrayOfMidiDevice2[k] = arrayOfMidiDevice1[m];
/*  93 */               arrayOfMidiDevice1[m] = null;
/*  94 */               break;
/*     */             }
/*     */           }
/*     */         }
/*  98 */         if (arrayOfInfo2[k] == null) {
/*  99 */           arrayOfInfo2[k] = localInfo1;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 104 */       if (arrayOfInfo1 != null) {
/* 105 */         for (k = 0; k < arrayOfInfo1.length; k++) {
/* 106 */           if (arrayOfInfo1[k] != null)
/*     */           {
/* 108 */             arrayOfInfo1[k].setIndex(-1);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 115 */       setInfoCache(arrayOfInfo2);
/* 116 */       setDeviceCache(arrayOfMidiDevice2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final MidiDevice.Info[] getDeviceInfo()
/*     */   {
/* 122 */     readDeviceInfos();
/* 123 */     Info[] arrayOfInfo = getInfoCache();
/* 124 */     MidiDevice.Info[] arrayOfInfo1 = new MidiDevice.Info[arrayOfInfo.length];
/* 125 */     System.arraycopy(arrayOfInfo, 0, arrayOfInfo1, 0, arrayOfInfo.length);
/* 126 */     return arrayOfInfo1;
/*     */   }
/*     */ 
/*     */   public final MidiDevice getDevice(MidiDevice.Info paramInfo)
/*     */   {
/* 131 */     if ((paramInfo instanceof Info)) {
/* 132 */       readDeviceInfos();
/* 133 */       MidiDevice[] arrayOfMidiDevice = getDeviceCache();
/* 134 */       Info[] arrayOfInfo = getInfoCache();
/* 135 */       Info localInfo = (Info)paramInfo;
/* 136 */       int i = localInfo.getIndex();
/* 137 */       if ((i >= 0) && (i < arrayOfMidiDevice.length) && (arrayOfInfo[i] == paramInfo)) {
/* 138 */         if (arrayOfMidiDevice[i] == null) {
/* 139 */           arrayOfMidiDevice[i] = createDevice(localInfo);
/*     */         }
/* 141 */         if (arrayOfMidiDevice[i] != null) {
/* 142 */           return arrayOfMidiDevice[i];
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 147 */     throw new IllegalArgumentException("MidiDevice " + paramInfo.toString() + " not supported by this provider.");
/*     */   }
/*     */ 
/*     */   abstract int getNumDevices();
/*     */ 
/*     */   abstract MidiDevice[] getDeviceCache();
/*     */ 
/*     */   abstract void setDeviceCache(MidiDevice[] paramArrayOfMidiDevice);
/*     */ 
/*     */   abstract Info[] getInfoCache();
/*     */ 
/*     */   abstract void setInfoCache(Info[] paramArrayOfInfo);
/*     */ 
/*     */   abstract Info createInfo(int paramInt);
/*     */ 
/*     */   abstract MidiDevice createDevice(Info paramInfo);
/*     */ 
/*     */   static
/*     */   {
/*  46 */     Platform.initialize();
/*     */   }
/*     */ 
/*     */   static class Info extends MidiDevice.Info
/*     */   {
/*     */     private int index;
/*     */ 
/*     */     Info(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt)
/*     */     {
/* 163 */       super(paramString2, paramString3, paramString4);
/* 164 */       this.index = paramInt;
/*     */     }
/*     */ 
/*     */     final boolean equalStrings(Info paramInfo) {
/* 168 */       return (paramInfo != null) && (getName().equals(paramInfo.getName())) && (getVendor().equals(paramInfo.getVendor())) && (getDescription().equals(paramInfo.getDescription())) && (getVersion().equals(paramInfo.getVersion()));
/*     */     }
/*     */ 
/*     */     final int getIndex()
/*     */     {
/* 176 */       return this.index;
/*     */     }
/*     */ 
/*     */     final void setIndex(int paramInt) {
/* 180 */       this.index = paramInt;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.AbstractMidiDeviceProvider
 * JD-Core Version:    0.6.2
 */