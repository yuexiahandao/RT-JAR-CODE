/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.sound.midi.Instrument;
/*     */ import javax.sound.midi.Patch;
/*     */ import javax.sound.midi.Soundbank;
/*     */ import javax.sound.midi.SoundbankResource;
/*     */ 
/*     */ public final class SF2Soundbank
/*     */   implements Soundbank
/*     */ {
/*  56 */   int major = 2;
/*  57 */   int minor = 1;
/*     */ 
/*  59 */   String targetEngine = "EMU8000";
/*     */ 
/*  61 */   String name = "untitled";
/*     */ 
/*  63 */   String romName = null;
/*     */ 
/*  65 */   int romVersionMajor = -1;
/*  66 */   int romVersionMinor = -1;
/*     */ 
/*  68 */   String creationDate = null;
/*     */ 
/*  70 */   String engineers = null;
/*     */ 
/*  72 */   String product = null;
/*     */ 
/*  74 */   String copyright = null;
/*     */ 
/*  76 */   String comments = null;
/*     */ 
/*  78 */   String tools = null;
/*     */ 
/*  80 */   private ModelByteBuffer sampleData = null;
/*  81 */   private ModelByteBuffer sampleData24 = null;
/*  82 */   private File sampleFile = null;
/*  83 */   private boolean largeFormat = false;
/*  84 */   private final List<SF2Instrument> instruments = new ArrayList();
/*  85 */   private final List<SF2Layer> layers = new ArrayList();
/*  86 */   private final List<SF2Sample> samples = new ArrayList();
/*     */ 
/*     */   public SF2Soundbank()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SF2Soundbank(URL paramURL) throws IOException {
/*  93 */     InputStream localInputStream = paramURL.openStream();
/*     */     try {
/*  95 */       readSoundbank(localInputStream);
/*     */     } finally {
/*  97 */       localInputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public SF2Soundbank(File paramFile) throws IOException {
/* 102 */     this.largeFormat = true;
/* 103 */     this.sampleFile = paramFile;
/* 104 */     FileInputStream localFileInputStream = new FileInputStream(paramFile);
/*     */     try {
/* 106 */       readSoundbank(localFileInputStream);
/*     */     } finally {
/* 108 */       localFileInputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public SF2Soundbank(InputStream paramInputStream) throws IOException {
/* 113 */     readSoundbank(paramInputStream);
/*     */   }
/*     */ 
/*     */   private void readSoundbank(InputStream paramInputStream) throws IOException {
/* 117 */     RIFFReader localRIFFReader1 = new RIFFReader(paramInputStream);
/* 118 */     if (!localRIFFReader1.getFormat().equals("RIFF")) {
/* 119 */       throw new RIFFInvalidFormatException("Input stream is not a valid RIFF stream!");
/*     */     }
/*     */ 
/* 122 */     if (!localRIFFReader1.getType().equals("sfbk")) {
/* 123 */       throw new RIFFInvalidFormatException("Input stream is not a valid SoundFont!");
/*     */     }
/*     */ 
/* 126 */     while (localRIFFReader1.hasNextChunk()) {
/* 127 */       RIFFReader localRIFFReader2 = localRIFFReader1.nextChunk();
/* 128 */       if (localRIFFReader2.getFormat().equals("LIST")) {
/* 129 */         if (localRIFFReader2.getType().equals("INFO"))
/* 130 */           readInfoChunk(localRIFFReader2);
/* 131 */         if (localRIFFReader2.getType().equals("sdta"))
/* 132 */           readSdtaChunk(localRIFFReader2);
/* 133 */         if (localRIFFReader2.getType().equals("pdta"))
/* 134 */           readPdtaChunk(localRIFFReader2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readInfoChunk(RIFFReader paramRIFFReader) throws IOException {
/* 140 */     while (paramRIFFReader.hasNextChunk()) {
/* 141 */       RIFFReader localRIFFReader = paramRIFFReader.nextChunk();
/* 142 */       String str = localRIFFReader.getFormat();
/* 143 */       if (str.equals("ifil")) {
/* 144 */         this.major = localRIFFReader.readUnsignedShort();
/* 145 */         this.minor = localRIFFReader.readUnsignedShort();
/* 146 */       } else if (str.equals("isng")) {
/* 147 */         this.targetEngine = localRIFFReader.readString(localRIFFReader.available());
/* 148 */       } else if (str.equals("INAM")) {
/* 149 */         this.name = localRIFFReader.readString(localRIFFReader.available());
/* 150 */       } else if (str.equals("irom")) {
/* 151 */         this.romName = localRIFFReader.readString(localRIFFReader.available());
/* 152 */       } else if (str.equals("iver")) {
/* 153 */         this.romVersionMajor = localRIFFReader.readUnsignedShort();
/* 154 */         this.romVersionMinor = localRIFFReader.readUnsignedShort();
/* 155 */       } else if (str.equals("ICRD")) {
/* 156 */         this.creationDate = localRIFFReader.readString(localRIFFReader.available());
/* 157 */       } else if (str.equals("IENG")) {
/* 158 */         this.engineers = localRIFFReader.readString(localRIFFReader.available());
/* 159 */       } else if (str.equals("IPRD")) {
/* 160 */         this.product = localRIFFReader.readString(localRIFFReader.available());
/* 161 */       } else if (str.equals("ICOP")) {
/* 162 */         this.copyright = localRIFFReader.readString(localRIFFReader.available());
/* 163 */       } else if (str.equals("ICMT")) {
/* 164 */         this.comments = localRIFFReader.readString(localRIFFReader.available());
/* 165 */       } else if (str.equals("ISFT")) {
/* 166 */         this.tools = localRIFFReader.readString(localRIFFReader.available());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readSdtaChunk(RIFFReader paramRIFFReader) throws IOException
/*     */   {
/* 173 */     while (paramRIFFReader.hasNextChunk()) {
/* 174 */       RIFFReader localRIFFReader = paramRIFFReader.nextChunk();
/*     */       byte[] arrayOfByte;
/*     */       int i;
/*     */       int j;
/* 175 */       if (localRIFFReader.getFormat().equals("smpl")) {
/* 176 */         if (!this.largeFormat) {
/* 177 */           arrayOfByte = new byte[localRIFFReader.available()];
/*     */ 
/* 179 */           i = 0;
/* 180 */           j = localRIFFReader.available();
/* 181 */           while (i != j) {
/* 182 */             if (j - i > 65536) {
/* 183 */               localRIFFReader.readFully(arrayOfByte, i, 65536);
/* 184 */               i += 65536;
/*     */             } else {
/* 186 */               localRIFFReader.readFully(arrayOfByte, i, j - i);
/* 187 */               i = j;
/*     */             }
/*     */           }
/*     */ 
/* 191 */           this.sampleData = new ModelByteBuffer(arrayOfByte);
/*     */         }
/*     */         else {
/* 194 */           this.sampleData = new ModelByteBuffer(this.sampleFile, localRIFFReader.getFilePointer(), localRIFFReader.available());
/*     */         }
/*     */       }
/*     */ 
/* 198 */       if (localRIFFReader.getFormat().equals("sm24"))
/* 199 */         if (!this.largeFormat) {
/* 200 */           arrayOfByte = new byte[localRIFFReader.available()];
/*     */ 
/* 203 */           i = 0;
/* 204 */           j = localRIFFReader.available();
/* 205 */           while (i != j) {
/* 206 */             if (j - i > 65536) {
/* 207 */               localRIFFReader.readFully(arrayOfByte, i, 65536);
/* 208 */               i += 65536;
/*     */             } else {
/* 210 */               localRIFFReader.readFully(arrayOfByte, i, j - i);
/* 211 */               i = j;
/*     */             }
/*     */           }
/*     */ 
/* 215 */           this.sampleData24 = new ModelByteBuffer(arrayOfByte);
/*     */         } else {
/* 217 */           this.sampleData24 = new ModelByteBuffer(this.sampleFile, localRIFFReader.getFilePointer(), localRIFFReader.available());
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readPdtaChunk(RIFFReader paramRIFFReader)
/*     */     throws IOException
/*     */   {
/* 227 */     ArrayList localArrayList1 = new ArrayList();
/* 228 */     ArrayList localArrayList2 = new ArrayList();
/* 229 */     ArrayList localArrayList3 = new ArrayList();
/*     */ 
/* 231 */     ArrayList localArrayList4 = new ArrayList();
/*     */ 
/* 234 */     ArrayList localArrayList5 = new ArrayList();
/* 235 */     ArrayList localArrayList6 = new ArrayList();
/* 236 */     ArrayList localArrayList7 = new ArrayList();
/*     */ 
/* 238 */     ArrayList localArrayList8 = new ArrayList();
/*     */     Object localObject5;
/* 241 */     while (paramRIFFReader.hasNextChunk()) {
/* 242 */       localObject1 = paramRIFFReader.nextChunk();
/* 243 */       localObject2 = ((RIFFReader)localObject1).getFormat();
/*     */       int i;
/*     */       int j;
/* 244 */       if (((String)localObject2).equals("phdr"))
/*     */       {
/* 246 */         if (((RIFFReader)localObject1).available() % 38 != 0)
/* 247 */           throw new RIFFInvalidDataException();
/* 248 */         i = ((RIFFReader)localObject1).available() / 38;
/* 249 */         for (j = 0; j < i; j++) {
/* 250 */           SF2Instrument localSF2Instrument1 = new SF2Instrument(this);
/* 251 */           localSF2Instrument1.name = ((RIFFReader)localObject1).readString(20);
/* 252 */           localSF2Instrument1.preset = ((RIFFReader)localObject1).readUnsignedShort();
/* 253 */           localSF2Instrument1.bank = ((RIFFReader)localObject1).readUnsignedShort();
/* 254 */           localArrayList2.add(Integer.valueOf(((RIFFReader)localObject1).readUnsignedShort()));
/* 255 */           localSF2Instrument1.library = ((RIFFReader)localObject1).readUnsignedInt();
/* 256 */           localSF2Instrument1.genre = ((RIFFReader)localObject1).readUnsignedInt();
/* 257 */           localSF2Instrument1.morphology = ((RIFFReader)localObject1).readUnsignedInt();
/* 258 */           localArrayList1.add(localSF2Instrument1);
/* 259 */           if (j != i - 1)
/* 260 */             this.instruments.add(localSF2Instrument1);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*     */         int i8;
/*     */         int i9;
/*     */         int i10;
/*     */         Object localObject7;
/* 262 */         if (((String)localObject2).equals("pbag"))
/*     */         {
/* 264 */           if (((RIFFReader)localObject1).available() % 4 != 0)
/* 265 */             throw new RIFFInvalidDataException();
/* 266 */           i = ((RIFFReader)localObject1).available() / 4;
/*     */ 
/* 270 */           j = ((RIFFReader)localObject1).readUnsignedShort();
/* 271 */           int n = ((RIFFReader)localObject1).readUnsignedShort();
/* 272 */           while (localArrayList3.size() < j)
/* 273 */             localArrayList3.add(null);
/* 274 */           while (localArrayList4.size() < n)
/* 275 */             localArrayList4.add(null);
/* 276 */           i--;
/*     */ 
/* 279 */           j = ((Integer)localArrayList2.get(0)).intValue();
/*     */           int i2;
/* 281 */           for (n = 0; n < j; n++) {
/* 282 */             if (i == 0)
/* 283 */               throw new RIFFInvalidDataException();
/* 284 */             i2 = ((RIFFReader)localObject1).readUnsignedShort();
/* 285 */             int i5 = ((RIFFReader)localObject1).readUnsignedShort();
/* 286 */             while (localArrayList3.size() < i2)
/* 287 */               localArrayList3.add(null);
/* 288 */             while (localArrayList4.size() < i5)
/* 289 */               localArrayList4.add(null);
/* 290 */             i--;
/*     */           }
/*     */ 
/* 293 */           for (n = 0; n < localArrayList2.size() - 1; n++) {
/* 294 */             i2 = ((Integer)localArrayList2.get(n + 1)).intValue() - ((Integer)localArrayList2.get(n)).intValue();
/*     */ 
/* 296 */             SF2Instrument localSF2Instrument2 = (SF2Instrument)localArrayList1.get(n);
/* 297 */             for (i8 = 0; i8 < i2; i8++) {
/* 298 */               if (i == 0)
/* 299 */                 throw new RIFFInvalidDataException();
/* 300 */               i9 = ((RIFFReader)localObject1).readUnsignedShort();
/* 301 */               i10 = ((RIFFReader)localObject1).readUnsignedShort();
/* 302 */               localObject7 = new SF2InstrumentRegion();
/* 303 */               localSF2Instrument2.regions.add(localObject7);
/* 304 */               while (localArrayList3.size() < i9)
/* 305 */                 localArrayList3.add(localObject7);
/* 306 */               while (localArrayList4.size() < i10)
/* 307 */                 localArrayList4.add(localObject7);
/* 308 */               i--;
/*     */             }
/*     */           }
/* 311 */         } else if (((String)localObject2).equals("pmod"))
/*     */         {
/* 313 */           for (i = 0; i < localArrayList4.size(); i++) {
/* 314 */             SF2Modulator localSF2Modulator1 = new SF2Modulator();
/* 315 */             localSF2Modulator1.sourceOperator = ((RIFFReader)localObject1).readUnsignedShort();
/* 316 */             localSF2Modulator1.destinationOperator = ((RIFFReader)localObject1).readUnsignedShort();
/* 317 */             localSF2Modulator1.amount = ((RIFFReader)localObject1).readShort();
/* 318 */             localSF2Modulator1.amountSourceOperator = ((RIFFReader)localObject1).readUnsignedShort();
/* 319 */             localSF2Modulator1.transportOperator = ((RIFFReader)localObject1).readUnsignedShort();
/* 320 */             SF2InstrumentRegion localSF2InstrumentRegion1 = (SF2InstrumentRegion)localArrayList4.get(i);
/* 321 */             if (localSF2InstrumentRegion1 != null)
/* 322 */               localSF2InstrumentRegion1.modulators.add(localSF2Modulator1);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*     */           int k;
/* 324 */           if (((String)localObject2).equals("pgen"))
/*     */           {
/* 326 */             for (i = 0; i < localArrayList3.size(); i++) {
/* 327 */               k = ((RIFFReader)localObject1).readUnsignedShort();
/* 328 */               short s1 = ((RIFFReader)localObject1).readShort();
/* 329 */               SF2InstrumentRegion localSF2InstrumentRegion2 = (SF2InstrumentRegion)localArrayList3.get(i);
/* 330 */               if (localSF2InstrumentRegion2 != null)
/* 331 */                 localSF2InstrumentRegion2.generators.put(Integer.valueOf(k), Short.valueOf(s1));
/*     */             }
/* 333 */           } else if (((String)localObject2).equals("inst"))
/*     */           {
/* 335 */             if (((RIFFReader)localObject1).available() % 22 != 0)
/* 336 */               throw new RIFFInvalidDataException();
/* 337 */             i = ((RIFFReader)localObject1).available() / 22;
/* 338 */             for (k = 0; k < i; k++) {
/* 339 */               SF2Layer localSF2Layer1 = new SF2Layer(this);
/* 340 */               localSF2Layer1.name = ((RIFFReader)localObject1).readString(20);
/* 341 */               localArrayList6.add(Integer.valueOf(((RIFFReader)localObject1).readUnsignedShort()));
/* 342 */               localArrayList5.add(localSF2Layer1);
/* 343 */               if (k != i - 1)
/* 344 */                 this.layers.add(localSF2Layer1);
/*     */             }
/* 346 */           } else if (((String)localObject2).equals("ibag"))
/*     */           {
/* 348 */             if (((RIFFReader)localObject1).available() % 4 != 0)
/* 349 */               throw new RIFFInvalidDataException();
/* 350 */             i = ((RIFFReader)localObject1).available() / 4;
/*     */ 
/* 354 */             k = ((RIFFReader)localObject1).readUnsignedShort();
/* 355 */             int i1 = ((RIFFReader)localObject1).readUnsignedShort();
/* 356 */             while (localArrayList7.size() < k)
/* 357 */               localArrayList7.add(null);
/* 358 */             while (localArrayList8.size() < i1)
/* 359 */               localArrayList8.add(null);
/* 360 */             i--;
/*     */ 
/* 363 */             k = ((Integer)localArrayList6.get(0)).intValue();
/*     */             int i3;
/* 365 */             for (i1 = 0; i1 < k; i1++) {
/* 366 */               if (i == 0)
/* 367 */                 throw new RIFFInvalidDataException();
/* 368 */               i3 = ((RIFFReader)localObject1).readUnsignedShort();
/* 369 */               int i6 = ((RIFFReader)localObject1).readUnsignedShort();
/* 370 */               while (localArrayList7.size() < i3)
/* 371 */                 localArrayList7.add(null);
/* 372 */               while (localArrayList8.size() < i6)
/* 373 */                 localArrayList8.add(null);
/* 374 */               i--;
/*     */             }
/*     */ 
/* 377 */             for (i1 = 0; i1 < localArrayList6.size() - 1; i1++) {
/* 378 */               i3 = ((Integer)localArrayList6.get(i1 + 1)).intValue() - ((Integer)localArrayList6.get(i1)).intValue();
/* 379 */               SF2Layer localSF2Layer2 = (SF2Layer)this.layers.get(i1);
/* 380 */               for (i8 = 0; i8 < i3; i8++) {
/* 381 */                 if (i == 0)
/* 382 */                   throw new RIFFInvalidDataException();
/* 383 */                 i9 = ((RIFFReader)localObject1).readUnsignedShort();
/* 384 */                 i10 = ((RIFFReader)localObject1).readUnsignedShort();
/* 385 */                 localObject7 = new SF2LayerRegion();
/* 386 */                 localSF2Layer2.regions.add(localObject7);
/* 387 */                 while (localArrayList7.size() < i9)
/* 388 */                   localArrayList7.add(localObject7);
/* 389 */                 while (localArrayList8.size() < i10)
/* 390 */                   localArrayList8.add(localObject7);
/* 391 */                 i--;
/*     */               }
/*     */             }
/*     */           }
/* 395 */           else if (((String)localObject2).equals("imod"))
/*     */           {
/* 397 */             for (i = 0; i < localArrayList8.size(); i++) {
/* 398 */               SF2Modulator localSF2Modulator2 = new SF2Modulator();
/* 399 */               localSF2Modulator2.sourceOperator = ((RIFFReader)localObject1).readUnsignedShort();
/* 400 */               localSF2Modulator2.destinationOperator = ((RIFFReader)localObject1).readUnsignedShort();
/* 401 */               localSF2Modulator2.amount = ((RIFFReader)localObject1).readShort();
/* 402 */               localSF2Modulator2.amountSourceOperator = ((RIFFReader)localObject1).readUnsignedShort();
/* 403 */               localSF2Modulator2.transportOperator = ((RIFFReader)localObject1).readUnsignedShort();
/* 404 */               SF2LayerRegion localSF2LayerRegion1 = (SF2LayerRegion)localArrayList7.get(i);
/* 405 */               if (localSF2LayerRegion1 != null)
/* 406 */                 localSF2LayerRegion1.modulators.add(localSF2Modulator2);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/*     */             int m;
/* 408 */             if (((String)localObject2).equals("igen"))
/*     */             {
/* 410 */               for (i = 0; i < localArrayList7.size(); i++) {
/* 411 */                 m = ((RIFFReader)localObject1).readUnsignedShort();
/* 412 */                 short s2 = ((RIFFReader)localObject1).readShort();
/* 413 */                 SF2LayerRegion localSF2LayerRegion2 = (SF2LayerRegion)localArrayList7.get(i);
/* 414 */                 if (localSF2LayerRegion2 != null)
/* 415 */                   localSF2LayerRegion2.generators.put(Integer.valueOf(m), Short.valueOf(s2));
/*     */               }
/* 417 */             } else if (((String)localObject2).equals("shdr"))
/*     */             {
/* 419 */               if (((RIFFReader)localObject1).available() % 46 != 0)
/* 420 */                 throw new RIFFInvalidDataException();
/* 421 */               i = ((RIFFReader)localObject1).available() / 46;
/* 422 */               for (m = 0; m < i; m++) {
/* 423 */                 localObject5 = new SF2Sample(this);
/* 424 */                 ((SF2Sample)localObject5).name = ((RIFFReader)localObject1).readString(20);
/* 425 */                 long l1 = ((RIFFReader)localObject1).readUnsignedInt();
/* 426 */                 long l2 = ((RIFFReader)localObject1).readUnsignedInt();
/* 427 */                 ((SF2Sample)localObject5).data = this.sampleData.subbuffer(l1 * 2L, l2 * 2L, true);
/* 428 */                 if (this.sampleData24 != null) {
/* 429 */                   ((SF2Sample)localObject5).data24 = this.sampleData24.subbuffer(l1, l2, true);
/*     */                 }
/*     */ 
/* 437 */                 ((SF2Sample)localObject5).startLoop = (((RIFFReader)localObject1).readUnsignedInt() - l1);
/* 438 */                 ((SF2Sample)localObject5).endLoop = (((RIFFReader)localObject1).readUnsignedInt() - l1);
/* 439 */                 if (((SF2Sample)localObject5).startLoop < 0L)
/* 440 */                   ((SF2Sample)localObject5).startLoop = -1L;
/* 441 */                 if (((SF2Sample)localObject5).endLoop < 0L)
/* 442 */                   ((SF2Sample)localObject5).endLoop = -1L;
/* 443 */                 ((SF2Sample)localObject5).sampleRate = ((RIFFReader)localObject1).readUnsignedInt();
/* 444 */                 ((SF2Sample)localObject5).originalPitch = ((RIFFReader)localObject1).readUnsignedByte();
/* 445 */                 ((SF2Sample)localObject5).pitchCorrection = ((RIFFReader)localObject1).readByte();
/* 446 */                 ((SF2Sample)localObject5).sampleLink = ((RIFFReader)localObject1).readUnsignedShort();
/* 447 */                 ((SF2Sample)localObject5).sampleType = ((RIFFReader)localObject1).readUnsignedShort();
/* 448 */                 if (m != i - 1)
/* 449 */                   this.samples.add(localObject5); 
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 454 */     Object localObject1 = this.layers.iterator();
/*     */     Object localObject3;
/*     */     Object localObject4;
/* 455 */     while (((Iterator)localObject1).hasNext()) {
/* 456 */       localObject2 = (SF2Layer)((Iterator)localObject1).next();
/* 457 */       localObject3 = ((SF2Layer)localObject2).regions.iterator();
/* 458 */       localObject4 = null;
/* 459 */       while (((Iterator)localObject3).hasNext()) {
/* 460 */         localObject5 = (SF2LayerRegion)((Iterator)localObject3).next();
/* 461 */         if (((SF2LayerRegion)localObject5).generators.get(Integer.valueOf(53)) != null) {
/* 462 */           int i4 = ((Short)((SF2LayerRegion)localObject5).generators.get(Integer.valueOf(53))).shortValue();
/*     */ 
/* 464 */           ((SF2LayerRegion)localObject5).generators.remove(Integer.valueOf(53));
/* 465 */           ((SF2LayerRegion)localObject5).sample = ((SF2Sample)this.samples.get(i4));
/*     */         } else {
/* 467 */           localObject4 = localObject5;
/*     */         }
/*     */       }
/* 470 */       if (localObject4 != null) {
/* 471 */         ((SF2Layer)localObject2).getRegions().remove(localObject4);
/* 472 */         localObject5 = new SF2GlobalRegion();
/* 473 */         ((SF2GlobalRegion)localObject5).generators = ((SF2Region)localObject4).generators;
/* 474 */         ((SF2GlobalRegion)localObject5).modulators = ((SF2Region)localObject4).modulators;
/* 475 */         ((SF2Layer)localObject2).setGlobalZone((SF2GlobalRegion)localObject5);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 480 */     Object localObject2 = this.instruments.iterator();
/* 481 */     while (((Iterator)localObject2).hasNext()) {
/* 482 */       localObject3 = (SF2Instrument)((Iterator)localObject2).next();
/* 483 */       localObject4 = ((SF2Instrument)localObject3).regions.iterator();
/* 484 */       localObject5 = null;
/*     */       Object localObject6;
/* 485 */       while (((Iterator)localObject4).hasNext()) {
/* 486 */         localObject6 = (SF2InstrumentRegion)((Iterator)localObject4).next();
/* 487 */         if (((SF2InstrumentRegion)localObject6).generators.get(Integer.valueOf(41)) != null) {
/* 488 */           int i7 = ((Short)((SF2InstrumentRegion)localObject6).generators.get(Integer.valueOf(41))).shortValue();
/*     */ 
/* 490 */           ((SF2InstrumentRegion)localObject6).generators.remove(Integer.valueOf(41));
/* 491 */           ((SF2InstrumentRegion)localObject6).layer = ((SF2Layer)this.layers.get(i7));
/*     */         } else {
/* 493 */           localObject5 = localObject6;
/*     */         }
/*     */       }
/*     */ 
/* 497 */       if (localObject5 != null) {
/* 498 */         ((SF2Instrument)localObject3).getRegions().remove(localObject5);
/* 499 */         localObject6 = new SF2GlobalRegion();
/* 500 */         ((SF2GlobalRegion)localObject6).generators = ((SF2Region)localObject5).generators;
/* 501 */         ((SF2GlobalRegion)localObject6).modulators = ((SF2Region)localObject5).modulators;
/* 502 */         ((SF2Instrument)localObject3).setGlobalZone((SF2GlobalRegion)localObject6);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void save(String paramString) throws IOException
/*     */   {
/* 509 */     writeSoundbank(new RIFFWriter(paramString, "sfbk"));
/*     */   }
/*     */ 
/*     */   public void save(File paramFile) throws IOException {
/* 513 */     writeSoundbank(new RIFFWriter(paramFile, "sfbk"));
/*     */   }
/*     */ 
/*     */   public void save(OutputStream paramOutputStream) throws IOException {
/* 517 */     writeSoundbank(new RIFFWriter(paramOutputStream, "sfbk"));
/*     */   }
/*     */ 
/*     */   private void writeSoundbank(RIFFWriter paramRIFFWriter) throws IOException {
/* 521 */     writeInfo(paramRIFFWriter.writeList("INFO"));
/* 522 */     writeSdtaChunk(paramRIFFWriter.writeList("sdta"));
/* 523 */     writePdtaChunk(paramRIFFWriter.writeList("pdta"));
/* 524 */     paramRIFFWriter.close();
/*     */   }
/*     */ 
/*     */   private void writeInfoStringChunk(RIFFWriter paramRIFFWriter, String paramString1, String paramString2) throws IOException
/*     */   {
/* 529 */     if (paramString2 == null)
/* 530 */       return;
/* 531 */     RIFFWriter localRIFFWriter = paramRIFFWriter.writeChunk(paramString1);
/* 532 */     localRIFFWriter.writeString(paramString2);
/* 533 */     int i = paramString2.getBytes("ascii").length;
/* 534 */     localRIFFWriter.write(0);
/* 535 */     i++;
/* 536 */     if (i % 2 != 0)
/* 537 */       localRIFFWriter.write(0);
/*     */   }
/*     */ 
/*     */   private void writeInfo(RIFFWriter paramRIFFWriter) throws IOException {
/* 541 */     if (this.targetEngine == null)
/* 542 */       this.targetEngine = "EMU8000";
/* 543 */     if (this.name == null) {
/* 544 */       this.name = "";
/*     */     }
/* 546 */     RIFFWriter localRIFFWriter1 = paramRIFFWriter.writeChunk("ifil");
/* 547 */     localRIFFWriter1.writeUnsignedShort(this.major);
/* 548 */     localRIFFWriter1.writeUnsignedShort(this.minor);
/* 549 */     writeInfoStringChunk(paramRIFFWriter, "isng", this.targetEngine);
/* 550 */     writeInfoStringChunk(paramRIFFWriter, "INAM", this.name);
/* 551 */     writeInfoStringChunk(paramRIFFWriter, "irom", this.romName);
/* 552 */     if (this.romVersionMajor != -1) {
/* 553 */       RIFFWriter localRIFFWriter2 = paramRIFFWriter.writeChunk("iver");
/* 554 */       localRIFFWriter2.writeUnsignedShort(this.romVersionMajor);
/* 555 */       localRIFFWriter2.writeUnsignedShort(this.romVersionMinor);
/*     */     }
/* 557 */     writeInfoStringChunk(paramRIFFWriter, "ICRD", this.creationDate);
/* 558 */     writeInfoStringChunk(paramRIFFWriter, "IENG", this.engineers);
/* 559 */     writeInfoStringChunk(paramRIFFWriter, "IPRD", this.product);
/* 560 */     writeInfoStringChunk(paramRIFFWriter, "ICOP", this.copyright);
/* 561 */     writeInfoStringChunk(paramRIFFWriter, "ICMT", this.comments);
/* 562 */     writeInfoStringChunk(paramRIFFWriter, "ISFT", this.tools);
/*     */ 
/* 564 */     paramRIFFWriter.close();
/*     */   }
/*     */ 
/*     */   private void writeSdtaChunk(RIFFWriter paramRIFFWriter) throws IOException
/*     */   {
/* 569 */     byte[] arrayOfByte = new byte[32];
/*     */ 
/* 571 */     RIFFWriter localRIFFWriter = paramRIFFWriter.writeChunk("smpl");
/* 572 */     for (Object localObject1 = this.samples.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (SF2Sample)((Iterator)localObject1).next();
/* 573 */       localObject3 = ((SF2Sample)localObject2).getDataBuffer();
/* 574 */       ((ModelByteBuffer)localObject3).writeTo(localRIFFWriter);
/*     */ 
/* 580 */       localRIFFWriter.write(arrayOfByte);
/* 581 */       localRIFFWriter.write(arrayOfByte);
/*     */     }
/*     */     Object localObject3;
/* 583 */     if (this.major < 2)
/* 584 */       return;
/* 585 */     if ((this.major == 2) && (this.minor < 4)) {
/* 586 */       return;
/*     */     }
/*     */ 
/* 589 */     for (localObject1 = this.samples.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (SF2Sample)((Iterator)localObject1).next();
/* 590 */       localObject3 = ((SF2Sample)localObject2).getData24Buffer();
/* 591 */       if (localObject3 == null) {
/* 592 */         return;
/*     */       }
/*     */     }
/* 595 */     localObject1 = paramRIFFWriter.writeChunk("sm24");
/* 596 */     for (Object localObject2 = this.samples.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (SF2Sample)((Iterator)localObject2).next();
/* 597 */       ModelByteBuffer localModelByteBuffer = ((SF2Sample)localObject3).getData24Buffer();
/* 598 */       localModelByteBuffer.writeTo((OutputStream)localObject1);
/*     */ 
/* 603 */       localRIFFWriter.write(arrayOfByte); }
/*     */   }
/*     */ 
/*     */   private void writeModulators(RIFFWriter paramRIFFWriter, List<SF2Modulator> paramList)
/*     */     throws IOException
/*     */   {
/* 609 */     for (SF2Modulator localSF2Modulator : paramList) {
/* 610 */       paramRIFFWriter.writeUnsignedShort(localSF2Modulator.sourceOperator);
/* 611 */       paramRIFFWriter.writeUnsignedShort(localSF2Modulator.destinationOperator);
/* 612 */       paramRIFFWriter.writeShort(localSF2Modulator.amount);
/* 613 */       paramRIFFWriter.writeUnsignedShort(localSF2Modulator.amountSourceOperator);
/* 614 */       paramRIFFWriter.writeUnsignedShort(localSF2Modulator.transportOperator);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeGenerators(RIFFWriter paramRIFFWriter, Map<Integer, Short> paramMap) throws IOException
/*     */   {
/* 620 */     Short localShort1 = (Short)paramMap.get(Integer.valueOf(43));
/* 621 */     Short localShort2 = (Short)paramMap.get(Integer.valueOf(44));
/* 622 */     if (localShort1 != null) {
/* 623 */       paramRIFFWriter.writeUnsignedShort(43);
/* 624 */       paramRIFFWriter.writeShort(localShort1.shortValue());
/*     */     }
/* 626 */     if (localShort2 != null) {
/* 627 */       paramRIFFWriter.writeUnsignedShort(44);
/* 628 */       paramRIFFWriter.writeShort(localShort2.shortValue());
/*     */     }
/* 630 */     for (Map.Entry localEntry : paramMap.entrySet())
/* 631 */       if ((((Integer)localEntry.getKey()).intValue() != 43) && 
/* 633 */         (((Integer)localEntry.getKey()).intValue() != 44))
/*     */       {
/* 635 */         paramRIFFWriter.writeUnsignedShort(((Integer)localEntry.getKey()).intValue());
/* 636 */         paramRIFFWriter.writeShort(((Short)localEntry.getValue()).shortValue());
/*     */       }
/*     */   }
/*     */ 
/*     */   private void writePdtaChunk(RIFFWriter paramRIFFWriter) throws IOException
/*     */   {
/* 642 */     RIFFWriter localRIFFWriter = paramRIFFWriter.writeChunk("phdr");
/* 643 */     int i = 0;
/* 644 */     for (Object localObject1 = this.instruments.iterator(); ((Iterator)localObject1).hasNext(); ) { SF2Instrument localSF2Instrument = (SF2Instrument)((Iterator)localObject1).next();
/* 645 */       localRIFFWriter.writeString(localSF2Instrument.name, 20);
/* 646 */       localRIFFWriter.writeUnsignedShort(localSF2Instrument.preset);
/* 647 */       localRIFFWriter.writeUnsignedShort(localSF2Instrument.bank);
/* 648 */       localRIFFWriter.writeUnsignedShort(i);
/* 649 */       if (localSF2Instrument.getGlobalRegion() != null)
/* 650 */         i++;
/* 651 */       i += localSF2Instrument.getRegions().size();
/* 652 */       localRIFFWriter.writeUnsignedInt(localSF2Instrument.library);
/* 653 */       localRIFFWriter.writeUnsignedInt(localSF2Instrument.genre);
/* 654 */       localRIFFWriter.writeUnsignedInt(localSF2Instrument.morphology);
/*     */     }
/* 656 */     localRIFFWriter.writeString("EOP", 20);
/* 657 */     localRIFFWriter.writeUnsignedShort(0);
/* 658 */     localRIFFWriter.writeUnsignedShort(0);
/* 659 */     localRIFFWriter.writeUnsignedShort(i);
/* 660 */     localRIFFWriter.writeUnsignedInt(0L);
/* 661 */     localRIFFWriter.writeUnsignedInt(0L);
/* 662 */     localRIFFWriter.writeUnsignedInt(0L);
/*     */ 
/* 665 */     localObject1 = paramRIFFWriter.writeChunk("pbag");
/* 666 */     int j = 0;
/* 667 */     int k = 0;
/* 668 */     for (Object localObject2 = this.instruments.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (SF2Instrument)((Iterator)localObject2).next();
/* 669 */       if (((SF2Instrument)localObject3).getGlobalRegion() != null) {
/* 670 */         ((RIFFWriter)localObject1).writeUnsignedShort(j);
/* 671 */         ((RIFFWriter)localObject1).writeUnsignedShort(k);
/* 672 */         j += ((SF2Instrument)localObject3).getGlobalRegion().getGenerators().size();
/* 673 */         k += ((SF2Instrument)localObject3).getGlobalRegion().getModulators().size();
/*     */       }
/* 675 */       for (localObject4 = ((SF2Instrument)localObject3).getRegions().iterator(); ((Iterator)localObject4).hasNext(); ) { localObject5 = (SF2InstrumentRegion)((Iterator)localObject4).next();
/* 676 */         ((RIFFWriter)localObject1).writeUnsignedShort(j);
/* 677 */         ((RIFFWriter)localObject1).writeUnsignedShort(k);
/* 678 */         if (this.layers.indexOf(((SF2InstrumentRegion)localObject5).layer) != -1)
/*     */         {
/* 680 */           j++;
/*     */         }
/* 682 */         j += ((SF2InstrumentRegion)localObject5).getGenerators().size();
/* 683 */         k += ((SF2InstrumentRegion)localObject5).getModulators().size();
/*     */       }
/*     */     }
/* 691 */     Object localObject5;
/* 687 */     ((RIFFWriter)localObject1).writeUnsignedShort(j);
/* 688 */     ((RIFFWriter)localObject1).writeUnsignedShort(k);
/*     */ 
/* 690 */     localObject2 = paramRIFFWriter.writeChunk("pmod");
/* 691 */     for (Object localObject3 = this.instruments.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (SF2Instrument)((Iterator)localObject3).next();
/* 692 */       if (((SF2Instrument)localObject4).getGlobalRegion() != null) {
/* 693 */         writeModulators((RIFFWriter)localObject2, ((SF2Instrument)localObject4).getGlobalRegion().getModulators());
/*     */       }
/*     */ 
/* 696 */       for (localObject5 = ((SF2Instrument)localObject4).getRegions().iterator(); ((Iterator)localObject5).hasNext(); ) { localObject6 = (SF2InstrumentRegion)((Iterator)localObject5).next();
/* 697 */         writeModulators((RIFFWriter)localObject2, ((SF2InstrumentRegion)localObject6).getModulators()); }
/*     */     }
/* 699 */     ((RIFFWriter)localObject2).write(new byte[10]);
/*     */ 
/* 701 */     localObject3 = paramRIFFWriter.writeChunk("pgen");
/* 702 */     for (Object localObject4 = this.instruments.iterator(); ((Iterator)localObject4).hasNext(); ) { localObject5 = (SF2Instrument)((Iterator)localObject4).next();
/* 703 */       if (((SF2Instrument)localObject5).getGlobalRegion() != null) {
/* 704 */         writeGenerators((RIFFWriter)localObject3, ((SF2Instrument)localObject5).getGlobalRegion().getGenerators());
/*     */       }
/*     */ 
/* 707 */       for (localObject6 = ((SF2Instrument)localObject5).getRegions().iterator(); ((Iterator)localObject6).hasNext(); ) { localObject7 = (SF2InstrumentRegion)((Iterator)localObject6).next();
/* 708 */         writeGenerators((RIFFWriter)localObject3, ((SF2InstrumentRegion)localObject7).getGenerators());
/* 709 */         i1 = this.layers.indexOf(((SF2InstrumentRegion)localObject7).layer);
/* 710 */         if (i1 != -1) {
/* 711 */           ((RIFFWriter)localObject3).writeUnsignedShort(41);
/* 712 */           ((RIFFWriter)localObject3).writeShort((short)i1);
/*     */         }
/*     */       }
/*     */     }
/* 720 */     Object localObject7;
/* 716 */     ((RIFFWriter)localObject3).write(new byte[4]);
/*     */ 
/* 718 */     localObject4 = paramRIFFWriter.writeChunk("inst");
/* 719 */     int m = 0;
/* 720 */     for (Object localObject6 = this.layers.iterator(); ((Iterator)localObject6).hasNext(); ) { localObject7 = (SF2Layer)((Iterator)localObject6).next();
/* 721 */       ((RIFFWriter)localObject4).writeString(((SF2Layer)localObject7).name, 20);
/* 722 */       ((RIFFWriter)localObject4).writeUnsignedShort(m);
/* 723 */       if (((SF2Layer)localObject7).getGlobalRegion() != null)
/* 724 */         m++;
/* 725 */       m += ((SF2Layer)localObject7).getRegions().size();
/*     */     }
/* 727 */     ((RIFFWriter)localObject4).writeString("EOI", 20);
/* 728 */     ((RIFFWriter)localObject4).writeUnsignedShort(m);
/*     */ 
/* 731 */     localObject6 = paramRIFFWriter.writeChunk("ibag");
/* 732 */     int n = 0;
/* 733 */     int i1 = 0;
/* 734 */     for (Object localObject8 = this.layers.iterator(); ((Iterator)localObject8).hasNext(); ) { localObject9 = (SF2Layer)((Iterator)localObject8).next();
/* 735 */       if (((SF2Layer)localObject9).getGlobalRegion() != null) {
/* 736 */         ((RIFFWriter)localObject6).writeUnsignedShort(n);
/* 737 */         ((RIFFWriter)localObject6).writeUnsignedShort(i1);
/* 738 */         n += ((SF2Layer)localObject9).getGlobalRegion().getGenerators().size();
/*     */ 
/* 740 */         i1 += ((SF2Layer)localObject9).getGlobalRegion().getModulators().size();
/*     */       }
/*     */ 
/* 743 */       for (localObject10 = ((SF2Layer)localObject9).getRegions().iterator(); ((Iterator)localObject10).hasNext(); ) { localObject11 = (SF2LayerRegion)((Iterator)localObject10).next();
/* 744 */         ((RIFFWriter)localObject6).writeUnsignedShort(n);
/* 745 */         ((RIFFWriter)localObject6).writeUnsignedShort(i1);
/* 746 */         if (this.samples.indexOf(((SF2LayerRegion)localObject11).sample) != -1)
/*     */         {
/* 748 */           n++;
/*     */         }
/* 750 */         n += ((SF2LayerRegion)localObject11).getGenerators().size();
/* 751 */         i1 += ((SF2LayerRegion)localObject11).getModulators().size();
/*     */       }
/*     */     }
/* 760 */     Object localObject11;
/* 755 */     ((RIFFWriter)localObject6).writeUnsignedShort(n);
/* 756 */     ((RIFFWriter)localObject6).writeUnsignedShort(i1);
/*     */ 
/* 759 */     localObject8 = paramRIFFWriter.writeChunk("imod");
/* 760 */     for (Object localObject9 = this.layers.iterator(); ((Iterator)localObject9).hasNext(); ) { localObject10 = (SF2Layer)((Iterator)localObject9).next();
/* 761 */       if (((SF2Layer)localObject10).getGlobalRegion() != null) {
/* 762 */         writeModulators((RIFFWriter)localObject8, ((SF2Layer)localObject10).getGlobalRegion().getModulators());
/*     */       }
/*     */ 
/* 765 */       for (localObject11 = ((SF2Layer)localObject10).getRegions().iterator(); ((Iterator)localObject11).hasNext(); ) { localObject12 = (SF2LayerRegion)((Iterator)localObject11).next();
/* 766 */         writeModulators((RIFFWriter)localObject8, ((SF2LayerRegion)localObject12).getModulators());
/*     */       }
/*     */     }
/* 771 */     Object localObject12;
/* 768 */     ((RIFFWriter)localObject8).write(new byte[10]);
/*     */ 
/* 770 */     localObject9 = paramRIFFWriter.writeChunk("igen");
/* 771 */     for (Object localObject10 = this.layers.iterator(); ((Iterator)localObject10).hasNext(); ) { localObject11 = (SF2Layer)((Iterator)localObject10).next();
/* 772 */       if (((SF2Layer)localObject11).getGlobalRegion() != null) {
/* 773 */         writeGenerators((RIFFWriter)localObject9, ((SF2Layer)localObject11).getGlobalRegion().getGenerators());
/*     */       }
/*     */ 
/* 776 */       for (localObject12 = ((SF2Layer)localObject11).getRegions().iterator(); ((Iterator)localObject12).hasNext(); ) { localObject13 = (SF2LayerRegion)((Iterator)localObject12).next();
/* 777 */         writeGenerators((RIFFWriter)localObject9, ((SF2LayerRegion)localObject13).getGenerators());
/* 778 */         int i2 = this.samples.indexOf(((SF2LayerRegion)localObject13).sample);
/* 779 */         if (i2 != -1) {
/* 780 */           ((RIFFWriter)localObject9).writeUnsignedShort(53);
/* 781 */           ((RIFFWriter)localObject9).writeShort((short)i2);
/*     */         }
/*     */       }
/*     */     }
/* 785 */     ((RIFFWriter)localObject9).write(new byte[4]);
/*     */ 
/* 788 */     localObject10 = paramRIFFWriter.writeChunk("shdr");
/* 789 */     long l1 = 0L;
/* 790 */     for (Object localObject13 = this.samples.iterator(); ((Iterator)localObject13).hasNext(); ) { SF2Sample localSF2Sample = (SF2Sample)((Iterator)localObject13).next();
/* 791 */       ((RIFFWriter)localObject10).writeString(localSF2Sample.name, 20);
/* 792 */       long l2 = l1;
/* 793 */       l1 += localSF2Sample.data.capacity() / 2L;
/* 794 */       long l3 = l1;
/* 795 */       long l4 = localSF2Sample.startLoop + l2;
/* 796 */       long l5 = localSF2Sample.endLoop + l2;
/* 797 */       if (l4 < l2)
/* 798 */         l4 = l2;
/* 799 */       if (l5 > l3)
/* 800 */         l5 = l3;
/* 801 */       ((RIFFWriter)localObject10).writeUnsignedInt(l2);
/* 802 */       ((RIFFWriter)localObject10).writeUnsignedInt(l3);
/* 803 */       ((RIFFWriter)localObject10).writeUnsignedInt(l4);
/* 804 */       ((RIFFWriter)localObject10).writeUnsignedInt(l5);
/* 805 */       ((RIFFWriter)localObject10).writeUnsignedInt(localSF2Sample.sampleRate);
/* 806 */       ((RIFFWriter)localObject10).writeUnsignedByte(localSF2Sample.originalPitch);
/* 807 */       ((RIFFWriter)localObject10).writeByte(localSF2Sample.pitchCorrection);
/* 808 */       ((RIFFWriter)localObject10).writeUnsignedShort(localSF2Sample.sampleLink);
/* 809 */       ((RIFFWriter)localObject10).writeUnsignedShort(localSF2Sample.sampleType);
/* 810 */       l1 += 32L;
/*     */     }
/* 812 */     ((RIFFWriter)localObject10).writeString("EOS", 20);
/* 813 */     ((RIFFWriter)localObject10).write(new byte[26]);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 818 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getVersion() {
/* 822 */     return this.major + "." + this.minor;
/*     */   }
/*     */ 
/*     */   public String getVendor() {
/* 826 */     return this.engineers;
/*     */   }
/*     */ 
/*     */   public String getDescription() {
/* 830 */     return this.comments;
/*     */   }
/*     */ 
/*     */   public void setName(String paramString) {
/* 834 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public void setVendor(String paramString) {
/* 838 */     this.engineers = paramString;
/*     */   }
/*     */ 
/*     */   public void setDescription(String paramString) {
/* 842 */     this.comments = paramString;
/*     */   }
/*     */ 
/*     */   public SoundbankResource[] getResources() {
/* 846 */     SoundbankResource[] arrayOfSoundbankResource = new SoundbankResource[this.layers.size() + this.samples.size()];
/*     */ 
/* 848 */     int i = 0;
/* 849 */     for (int j = 0; j < this.layers.size(); j++)
/* 850 */       arrayOfSoundbankResource[(i++)] = ((SoundbankResource)this.layers.get(j));
/* 851 */     for (j = 0; j < this.samples.size(); j++)
/* 852 */       arrayOfSoundbankResource[(i++)] = ((SoundbankResource)this.samples.get(j));
/* 853 */     return arrayOfSoundbankResource;
/*     */   }
/*     */ 
/*     */   public SF2Instrument[] getInstruments() {
/* 857 */     SF2Instrument[] arrayOfSF2Instrument = (SF2Instrument[])this.instruments.toArray(new SF2Instrument[this.instruments.size()]);
/*     */ 
/* 859 */     Arrays.sort(arrayOfSF2Instrument, new ModelInstrumentComparator());
/* 860 */     return arrayOfSF2Instrument;
/*     */   }
/*     */ 
/*     */   public SF2Layer[] getLayers() {
/* 864 */     return (SF2Layer[])this.layers.toArray(new SF2Layer[this.layers.size()]);
/*     */   }
/*     */ 
/*     */   public SF2Sample[] getSamples() {
/* 868 */     return (SF2Sample[])this.samples.toArray(new SF2Sample[this.samples.size()]);
/*     */   }
/*     */ 
/*     */   public Instrument getInstrument(Patch paramPatch) {
/* 872 */     int i = paramPatch.getProgram();
/* 873 */     int j = paramPatch.getBank();
/* 874 */     boolean bool1 = false;
/* 875 */     if ((paramPatch instanceof ModelPatch))
/* 876 */       bool1 = ((ModelPatch)paramPatch).isPercussion();
/* 877 */     for (Instrument localInstrument : this.instruments) {
/* 878 */       Patch localPatch = localInstrument.getPatch();
/* 879 */       int k = localPatch.getProgram();
/* 880 */       int m = localPatch.getBank();
/* 881 */       if ((i == k) && (j == m)) {
/* 882 */         boolean bool2 = false;
/* 883 */         if ((localPatch instanceof ModelPatch))
/* 884 */           bool2 = ((ModelPatch)localPatch).isPercussion();
/* 885 */         if (bool1 == bool2)
/* 886 */           return localInstrument;
/*     */       }
/*     */     }
/* 889 */     return null;
/*     */   }
/*     */ 
/*     */   public String getCreationDate() {
/* 893 */     return this.creationDate;
/*     */   }
/*     */ 
/*     */   public void setCreationDate(String paramString) {
/* 897 */     this.creationDate = paramString;
/*     */   }
/*     */ 
/*     */   public String getProduct() {
/* 901 */     return this.product;
/*     */   }
/*     */ 
/*     */   public void setProduct(String paramString) {
/* 905 */     this.product = paramString;
/*     */   }
/*     */ 
/*     */   public String getRomName() {
/* 909 */     return this.romName;
/*     */   }
/*     */ 
/*     */   public void setRomName(String paramString) {
/* 913 */     this.romName = paramString;
/*     */   }
/*     */ 
/*     */   public int getRomVersionMajor() {
/* 917 */     return this.romVersionMajor;
/*     */   }
/*     */ 
/*     */   public void setRomVersionMajor(int paramInt) {
/* 921 */     this.romVersionMajor = paramInt;
/*     */   }
/*     */ 
/*     */   public int getRomVersionMinor() {
/* 925 */     return this.romVersionMinor;
/*     */   }
/*     */ 
/*     */   public void setRomVersionMinor(int paramInt) {
/* 929 */     this.romVersionMinor = paramInt;
/*     */   }
/*     */ 
/*     */   public String getTargetEngine() {
/* 933 */     return this.targetEngine;
/*     */   }
/*     */ 
/*     */   public void setTargetEngine(String paramString) {
/* 937 */     this.targetEngine = paramString;
/*     */   }
/*     */ 
/*     */   public String getTools() {
/* 941 */     return this.tools;
/*     */   }
/*     */ 
/*     */   public void setTools(String paramString) {
/* 945 */     this.tools = paramString;
/*     */   }
/*     */ 
/*     */   public void addResource(SoundbankResource paramSoundbankResource) {
/* 949 */     if ((paramSoundbankResource instanceof SF2Instrument))
/* 950 */       this.instruments.add((SF2Instrument)paramSoundbankResource);
/* 951 */     if ((paramSoundbankResource instanceof SF2Layer))
/* 952 */       this.layers.add((SF2Layer)paramSoundbankResource);
/* 953 */     if ((paramSoundbankResource instanceof SF2Sample))
/* 954 */       this.samples.add((SF2Sample)paramSoundbankResource);
/*     */   }
/*     */ 
/*     */   public void removeResource(SoundbankResource paramSoundbankResource) {
/* 958 */     if ((paramSoundbankResource instanceof SF2Instrument))
/* 959 */       this.instruments.remove((SF2Instrument)paramSoundbankResource);
/* 960 */     if ((paramSoundbankResource instanceof SF2Layer))
/* 961 */       this.layers.remove((SF2Layer)paramSoundbankResource);
/* 962 */     if ((paramSoundbankResource instanceof SF2Sample))
/* 963 */       this.samples.remove((SF2Sample)paramSoundbankResource);
/*     */   }
/*     */ 
/*     */   public void addInstrument(SF2Instrument paramSF2Instrument) {
/* 967 */     this.instruments.add(paramSF2Instrument);
/*     */   }
/*     */ 
/*     */   public void removeInstrument(SF2Instrument paramSF2Instrument) {
/* 971 */     this.instruments.remove(paramSF2Instrument);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SF2Soundbank
 * JD-Core Version:    0.6.2
 */