/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import javax.sound.midi.MidiEvent;
/*     */ import javax.sound.midi.MidiMessage;
/*     */ import javax.sound.midi.Sequence;
/*     */ import javax.sound.midi.Track;
/*     */ 
/*     */ public final class MidiUtils
/*     */ {
/*     */   public static final int DEFAULT_TEMPO_MPQ = 500000;
/*     */   public static final int META_END_OF_TRACK_TYPE = 47;
/*     */   public static final int META_TEMPO_TYPE = 81;
/*     */ 
/*     */   public static boolean isMetaEndOfTrack(MidiMessage paramMidiMessage)
/*     */   {
/*  54 */     if ((paramMidiMessage.getLength() != 3) || (paramMidiMessage.getStatus() != 255))
/*     */     {
/*  56 */       return false;
/*     */     }
/*     */ 
/*  59 */     byte[] arrayOfByte = paramMidiMessage.getMessage();
/*  60 */     return ((arrayOfByte[1] & 0xFF) == 47) && (arrayOfByte[2] == 0);
/*     */   }
/*     */ 
/*     */   public static boolean isMetaTempo(MidiMessage paramMidiMessage)
/*     */   {
/*  67 */     if ((paramMidiMessage.getLength() != 6) || (paramMidiMessage.getStatus() != 255))
/*     */     {
/*  69 */       return false;
/*     */     }
/*     */ 
/*  72 */     byte[] arrayOfByte = paramMidiMessage.getMessage();
/*     */ 
/*  74 */     return ((arrayOfByte[1] & 0xFF) == 81) && (arrayOfByte[2] == 3);
/*     */   }
/*     */ 
/*     */   public static int getTempoMPQ(MidiMessage paramMidiMessage)
/*     */   {
/*  83 */     if ((paramMidiMessage.getLength() != 6) || (paramMidiMessage.getStatus() != 255))
/*     */     {
/*  85 */       return -1;
/*     */     }
/*  87 */     byte[] arrayOfByte = paramMidiMessage.getMessage();
/*  88 */     if (((arrayOfByte[1] & 0xFF) != 81) || (arrayOfByte[2] != 3)) {
/*  89 */       return -1;
/*     */     }
/*  91 */     int i = arrayOfByte[5] & 0xFF | (arrayOfByte[4] & 0xFF) << 8 | (arrayOfByte[3] & 0xFF) << 16;
/*     */ 
/*  94 */     return i;
/*     */   }
/*     */ 
/*     */   public static double convertTempo(double paramDouble)
/*     */   {
/* 104 */     if (paramDouble <= 0.0D) {
/* 105 */       paramDouble = 1.0D;
/*     */     }
/* 107 */     return 60000000.0D / paramDouble;
/*     */   }
/*     */ 
/*     */   public static long ticks2microsec(long paramLong, double paramDouble, int paramInt)
/*     */   {
/* 117 */     return ()(paramLong * paramDouble / paramInt);
/*     */   }
/*     */ 
/*     */   public static long microsec2ticks(long paramLong, double paramDouble, int paramInt)
/*     */   {
/* 128 */     return ()(paramLong * paramInt / paramDouble);
/*     */   }
/*     */ 
/*     */   public static long tick2microsecond(Sequence paramSequence, long paramLong, TempoCache paramTempoCache)
/*     */   {
/* 137 */     if (paramSequence.getDivisionType() != 0.0F) {
/* 138 */       double d = paramLong / (paramSequence.getDivisionType() * paramSequence.getResolution());
/* 139 */       return ()(1000000.0D * d);
/*     */     }
/*     */ 
/* 142 */     if (paramTempoCache == null) {
/* 143 */       paramTempoCache = new TempoCache(paramSequence);
/*     */     }
/*     */ 
/* 146 */     int i = paramSequence.getResolution();
/*     */ 
/* 148 */     long[] arrayOfLong = paramTempoCache.ticks;
/* 149 */     int[] arrayOfInt = paramTempoCache.tempos;
/* 150 */     int j = arrayOfInt.length;
/*     */ 
/* 153 */     int k = paramTempoCache.snapshotIndex;
/* 154 */     int m = paramTempoCache.snapshotMicro;
/*     */ 
/* 157 */     long l = 0L;
/*     */ 
/* 159 */     if ((k <= 0) || (k >= j) || (arrayOfLong[k] > paramLong))
/*     */     {
/* 162 */       m = 0;
/* 163 */       k = 0;
/*     */     }
/* 165 */     if (j > 0)
/*     */     {
/* 167 */       int n = k + 1;
/* 168 */       while ((n < j) && (arrayOfLong[n] <= paramLong)) {
/* 169 */         m = (int)(m + ticks2microsec(arrayOfLong[n] - arrayOfLong[(n - 1)], arrayOfInt[(n - 1)], i));
/* 170 */         k = n;
/* 171 */         n++;
/*     */       }
/* 173 */       l = m + ticks2microsec(paramLong - arrayOfLong[k], arrayOfInt[k], i);
/*     */     }
/*     */ 
/* 178 */     paramTempoCache.snapshotIndex = k;
/* 179 */     paramTempoCache.snapshotMicro = m;
/* 180 */     return l;
/*     */   }
/*     */ 
/*     */   public static long microsecond2tick(Sequence paramSequence, long paramLong, TempoCache paramTempoCache)
/*     */   {
/* 188 */     if (paramSequence.getDivisionType() != 0.0F) {
/* 189 */       double d = paramLong * paramSequence.getDivisionType() * paramSequence.getResolution() / 1000000.0D;
/*     */ 
/* 193 */       long l1 = ()d;
/* 194 */       if (paramTempoCache != null) {
/* 195 */         paramTempoCache.currTempo = ((int)paramTempoCache.getTempoMPQAt(l1));
/*     */       }
/* 197 */       return l1;
/*     */     }
/*     */ 
/* 200 */     if (paramTempoCache == null) {
/* 201 */       paramTempoCache = new TempoCache(paramSequence);
/*     */     }
/* 203 */     long[] arrayOfLong = paramTempoCache.ticks;
/* 204 */     int[] arrayOfInt = paramTempoCache.tempos;
/* 205 */     int i = arrayOfInt.length;
/*     */ 
/* 207 */     int j = paramSequence.getResolution();
/*     */ 
/* 209 */     long l2 = 0L; long l3 = 0L; int k = 0; int m = 1;
/*     */ 
/* 213 */     if ((paramLong > 0L) && (i > 0))
/*     */     {
/* 215 */       while (m < i) {
/* 216 */         long l4 = l2 + ticks2microsec(arrayOfLong[m] - arrayOfLong[(m - 1)], arrayOfInt[(m - 1)], j);
/*     */ 
/* 218 */         if (l4 > paramLong) {
/*     */           break;
/*     */         }
/* 221 */         l2 = l4;
/* 222 */         m++;
/*     */       }
/* 224 */       l3 = arrayOfLong[(m - 1)] + microsec2ticks(paramLong - l2, arrayOfInt[(m - 1)], j);
/*     */     }
/*     */ 
/* 228 */     paramTempoCache.currTempo = arrayOfInt[(m - 1)];
/* 229 */     return l3;
/*     */   }
/*     */ 
/*     */   public static int tick2index(Track paramTrack, long paramLong)
/*     */   {
/* 241 */     int i = 0;
/* 242 */     if (paramLong > 0L) {
/* 243 */       int j = 0;
/* 244 */       int k = paramTrack.size() - 1;
/* 245 */       while (j < k)
/*     */       {
/* 247 */         i = j + k >> 1;
/*     */ 
/* 249 */         long l = paramTrack.get(i).getTick();
/* 250 */         if (l == paramLong)
/*     */           break;
/* 252 */         if (l < paramLong)
/*     */         {
/* 254 */           if (j == k - 1)
/*     */           {
/* 256 */             i++;
/* 257 */             break;
/*     */           }
/* 259 */           j = i;
/*     */         }
/*     */         else {
/* 262 */           k = i;
/*     */         }
/*     */       }
/*     */     }
/* 266 */     return i;
/*     */   }
/*     */ 
/*     */   public static final class TempoCache
/*     */   {
/*     */     long[] ticks;
/*     */     int[] tempos;
/* 274 */     int snapshotIndex = 0;
/*     */ 
/* 276 */     int snapshotMicro = 0;
/*     */     int currTempo;
/* 280 */     private boolean firstTempoIsFake = false;
/*     */ 
/*     */     public TempoCache()
/*     */     {
/* 284 */       this.ticks = new long[1];
/* 285 */       this.tempos = new int[1];
/* 286 */       this.tempos[0] = 500000;
/* 287 */       this.snapshotIndex = 0;
/* 288 */       this.snapshotMicro = 0;
/*     */     }
/*     */ 
/*     */     public TempoCache(Sequence paramSequence) {
/* 292 */       this();
/* 293 */       refresh(paramSequence);
/*     */     }
/*     */ 
/*     */     public synchronized void refresh(Sequence paramSequence)
/*     */     {
/* 298 */       ArrayList localArrayList = new ArrayList();
/* 299 */       Track[] arrayOfTrack = paramSequence.getTracks();
/*     */       MidiEvent localMidiEvent;
/* 300 */       if (arrayOfTrack.length > 0)
/*     */       {
/* 302 */         Track localTrack = arrayOfTrack[0];
/* 303 */         j = localTrack.size();
/* 304 */         for (k = 0; k < j; k++) {
/* 305 */           localMidiEvent = localTrack.get(k);
/* 306 */           MidiMessage localMidiMessage = localMidiEvent.getMessage();
/* 307 */           if (MidiUtils.isMetaTempo(localMidiMessage))
/*     */           {
/* 309 */             localArrayList.add(localMidiEvent);
/*     */           }
/*     */         }
/*     */       }
/* 313 */       int i = localArrayList.size() + 1;
/* 314 */       this.firstTempoIsFake = true;
/* 315 */       if ((i > 1) && (((MidiEvent)localArrayList.get(0)).getTick() == 0L))
/*     */       {
/* 318 */         i--;
/* 319 */         this.firstTempoIsFake = false;
/*     */       }
/* 321 */       this.ticks = new long[i];
/* 322 */       this.tempos = new int[i];
/* 323 */       int j = 0;
/* 324 */       if (this.firstTempoIsFake)
/*     */       {
/* 326 */         this.ticks[0] = 0L;
/* 327 */         this.tempos[0] = 500000;
/* 328 */         j++;
/*     */       }
/* 330 */       for (int k = 0; k < localArrayList.size(); j++) {
/* 331 */         localMidiEvent = (MidiEvent)localArrayList.get(k);
/* 332 */         this.ticks[j] = localMidiEvent.getTick();
/* 333 */         this.tempos[j] = MidiUtils.getTempoMPQ(localMidiEvent.getMessage());
/*     */ 
/* 330 */         k++;
/*     */       }
/*     */ 
/* 335 */       this.snapshotIndex = 0;
/* 336 */       this.snapshotMicro = 0;
/*     */     }
/*     */ 
/*     */     public int getCurrTempoMPQ() {
/* 340 */       return this.currTempo;
/*     */     }
/*     */ 
/*     */     float getTempoMPQAt(long paramLong) {
/* 344 */       return getTempoMPQAt(paramLong, -1.0F);
/*     */     }
/*     */ 
/*     */     synchronized float getTempoMPQAt(long paramLong, float paramFloat) {
/* 348 */       for (int i = 0; i < this.ticks.length; i++) {
/* 349 */         if (this.ticks[i] > paramLong) {
/* 350 */           if (i > 0) i--;
/* 351 */           if ((paramFloat > 0.0F) && (i == 0) && (this.firstTempoIsFake)) {
/* 352 */             return paramFloat;
/*     */           }
/* 354 */           return this.tempos[i];
/*     */         }
/*     */       }
/* 357 */       return this.tempos[(this.tempos.length - 1)];
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.MidiUtils
 * JD-Core Version:    0.6.2
 */