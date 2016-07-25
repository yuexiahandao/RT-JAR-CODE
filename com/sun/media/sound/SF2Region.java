/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class SF2Region
/*     */ {
/*     */   public static final int GENERATOR_STARTADDRSOFFSET = 0;
/*     */   public static final int GENERATOR_ENDADDRSOFFSET = 1;
/*     */   public static final int GENERATOR_STARTLOOPADDRSOFFSET = 2;
/*     */   public static final int GENERATOR_ENDLOOPADDRSOFFSET = 3;
/*     */   public static final int GENERATOR_STARTADDRSCOARSEOFFSET = 4;
/*     */   public static final int GENERATOR_MODLFOTOPITCH = 5;
/*     */   public static final int GENERATOR_VIBLFOTOPITCH = 6;
/*     */   public static final int GENERATOR_MODENVTOPITCH = 7;
/*     */   public static final int GENERATOR_INITIALFILTERFC = 8;
/*     */   public static final int GENERATOR_INITIALFILTERQ = 9;
/*     */   public static final int GENERATOR_MODLFOTOFILTERFC = 10;
/*     */   public static final int GENERATOR_MODENVTOFILTERFC = 11;
/*     */   public static final int GENERATOR_ENDADDRSCOARSEOFFSET = 12;
/*     */   public static final int GENERATOR_MODLFOTOVOLUME = 13;
/*     */   public static final int GENERATOR_UNUSED1 = 14;
/*     */   public static final int GENERATOR_CHORUSEFFECTSSEND = 15;
/*     */   public static final int GENERATOR_REVERBEFFECTSSEND = 16;
/*     */   public static final int GENERATOR_PAN = 17;
/*     */   public static final int GENERATOR_UNUSED2 = 18;
/*     */   public static final int GENERATOR_UNUSED3 = 19;
/*     */   public static final int GENERATOR_UNUSED4 = 20;
/*     */   public static final int GENERATOR_DELAYMODLFO = 21;
/*     */   public static final int GENERATOR_FREQMODLFO = 22;
/*     */   public static final int GENERATOR_DELAYVIBLFO = 23;
/*     */   public static final int GENERATOR_FREQVIBLFO = 24;
/*     */   public static final int GENERATOR_DELAYMODENV = 25;
/*     */   public static final int GENERATOR_ATTACKMODENV = 26;
/*     */   public static final int GENERATOR_HOLDMODENV = 27;
/*     */   public static final int GENERATOR_DECAYMODENV = 28;
/*     */   public static final int GENERATOR_SUSTAINMODENV = 29;
/*     */   public static final int GENERATOR_RELEASEMODENV = 30;
/*     */   public static final int GENERATOR_KEYNUMTOMODENVHOLD = 31;
/*     */   public static final int GENERATOR_KEYNUMTOMODENVDECAY = 32;
/*     */   public static final int GENERATOR_DELAYVOLENV = 33;
/*     */   public static final int GENERATOR_ATTACKVOLENV = 34;
/*     */   public static final int GENERATOR_HOLDVOLENV = 35;
/*     */   public static final int GENERATOR_DECAYVOLENV = 36;
/*     */   public static final int GENERATOR_SUSTAINVOLENV = 37;
/*     */   public static final int GENERATOR_RELEASEVOLENV = 38;
/*     */   public static final int GENERATOR_KEYNUMTOVOLENVHOLD = 39;
/*     */   public static final int GENERATOR_KEYNUMTOVOLENVDECAY = 40;
/*     */   public static final int GENERATOR_INSTRUMENT = 41;
/*     */   public static final int GENERATOR_RESERVED1 = 42;
/*     */   public static final int GENERATOR_KEYRANGE = 43;
/*     */   public static final int GENERATOR_VELRANGE = 44;
/*     */   public static final int GENERATOR_STARTLOOPADDRSCOARSEOFFSET = 45;
/*     */   public static final int GENERATOR_KEYNUM = 46;
/*     */   public static final int GENERATOR_VELOCITY = 47;
/*     */   public static final int GENERATOR_INITIALATTENUATION = 48;
/*     */   public static final int GENERATOR_RESERVED2 = 49;
/*     */   public static final int GENERATOR_ENDLOOPADDRSCOARSEOFFSET = 50;
/*     */   public static final int GENERATOR_COARSETUNE = 51;
/*     */   public static final int GENERATOR_FINETUNE = 52;
/*     */   public static final int GENERATOR_SAMPLEID = 53;
/*     */   public static final int GENERATOR_SAMPLEMODES = 54;
/*     */   public static final int GENERATOR_RESERVED3 = 55;
/*     */   public static final int GENERATOR_SCALETUNING = 56;
/*     */   public static final int GENERATOR_EXCLUSIVECLASS = 57;
/*     */   public static final int GENERATOR_OVERRIDINGROOTKEY = 58;
/*     */   public static final int GENERATOR_UNUSED5 = 59;
/*     */   public static final int GENERATOR_ENDOPR = 60;
/* 100 */   protected Map<Integer, Short> generators = new HashMap();
/* 101 */   protected List<SF2Modulator> modulators = new ArrayList();
/*     */ 
/*     */   public Map<Integer, Short> getGenerators() {
/* 104 */     return this.generators;
/*     */   }
/*     */ 
/*     */   public boolean contains(int paramInt) {
/* 108 */     return this.generators.containsKey(Integer.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   public static short getDefaultValue(int paramInt) {
/* 112 */     if (paramInt == 8) return 13500;
/* 113 */     if (paramInt == 21) return -12000;
/* 114 */     if (paramInt == 23) return -12000;
/* 115 */     if (paramInt == 25) return -12000;
/* 116 */     if (paramInt == 26) return -12000;
/* 117 */     if (paramInt == 27) return -12000;
/* 118 */     if (paramInt == 28) return -12000;
/* 119 */     if (paramInt == 30) return -12000;
/* 120 */     if (paramInt == 33) return -12000;
/* 121 */     if (paramInt == 34) return -12000;
/* 122 */     if (paramInt == 35) return -12000;
/* 123 */     if (paramInt == 36) return -12000;
/* 124 */     if (paramInt == 38) return -12000;
/* 125 */     if (paramInt == 43) return 32512;
/* 126 */     if (paramInt == 44) return 32512;
/* 127 */     if (paramInt == 46) return -1;
/* 128 */     if (paramInt == 47) return -1;
/* 129 */     if (paramInt == 56) return 100;
/* 130 */     if (paramInt == 58) return -1;
/* 131 */     return 0;
/*     */   }
/*     */ 
/*     */   public short getShort(int paramInt) {
/* 135 */     if (!contains(paramInt))
/* 136 */       return getDefaultValue(paramInt);
/* 137 */     return ((Short)this.generators.get(Integer.valueOf(paramInt))).shortValue();
/*     */   }
/*     */ 
/*     */   public void putShort(int paramInt, short paramShort) {
/* 141 */     this.generators.put(Integer.valueOf(paramInt), Short.valueOf(paramShort));
/*     */   }
/*     */ 
/*     */   public byte[] getBytes(int paramInt) {
/* 145 */     int i = getInteger(paramInt);
/* 146 */     byte[] arrayOfByte = new byte[2];
/* 147 */     arrayOfByte[0] = ((byte)(0xFF & i));
/* 148 */     arrayOfByte[1] = ((byte)((0xFF00 & i) >> 8));
/* 149 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public void putBytes(int paramInt, byte[] paramArrayOfByte) {
/* 153 */     this.generators.put(Integer.valueOf(paramInt), Short.valueOf((short)(paramArrayOfByte[0] + (paramArrayOfByte[1] << 8))));
/*     */   }
/*     */ 
/*     */   public int getInteger(int paramInt) {
/* 157 */     return 0xFFFF & getShort(paramInt);
/*     */   }
/*     */ 
/*     */   public void putInteger(int paramInt1, int paramInt2) {
/* 161 */     this.generators.put(Integer.valueOf(paramInt1), Short.valueOf((short)paramInt2));
/*     */   }
/*     */ 
/*     */   public List<SF2Modulator> getModulators() {
/* 165 */     return this.modulators;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SF2Region
 * JD-Core Version:    0.6.2
 */