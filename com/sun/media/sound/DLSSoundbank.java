/*      */ package com.sun.media.sound;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import javax.sound.midi.Instrument;
/*      */ import javax.sound.midi.Patch;
/*      */ import javax.sound.midi.Soundbank;
/*      */ import javax.sound.midi.SoundbankResource;
/*      */ import javax.sound.sampled.AudioFormat;
/*      */ import javax.sound.sampled.AudioFormat.Encoding;
/*      */ import javax.sound.sampled.AudioInputStream;
/*      */ import javax.sound.sampled.AudioSystem;
/*      */ 
/*      */ public final class DLSSoundbank
/*      */   implements Soundbank
/*      */ {
/*      */   private static final int DLS_CDL_AND = 1;
/*      */   private static final int DLS_CDL_OR = 2;
/*      */   private static final int DLS_CDL_XOR = 3;
/*      */   private static final int DLS_CDL_ADD = 4;
/*      */   private static final int DLS_CDL_SUBTRACT = 5;
/*      */   private static final int DLS_CDL_MULTIPLY = 6;
/*      */   private static final int DLS_CDL_DIVIDE = 7;
/*      */   private static final int DLS_CDL_LOGICAL_AND = 8;
/*      */   private static final int DLS_CDL_LOGICAL_OR = 9;
/*      */   private static final int DLS_CDL_LT = 10;
/*      */   private static final int DLS_CDL_LE = 11;
/*      */   private static final int DLS_CDL_GT = 12;
/*      */   private static final int DLS_CDL_GE = 13;
/*      */   private static final int DLS_CDL_EQ = 14;
/*      */   private static final int DLS_CDL_NOT = 15;
/*      */   private static final int DLS_CDL_CONST = 16;
/*      */   private static final int DLS_CDL_QUERY = 17;
/*      */   private static final int DLS_CDL_QUERYSUPPORTED = 18;
/*  155 */   private static final DLSID DLSID_GMInHardware = new DLSID(395259684L, 50020, 4561, 167, 96, 0, 0, 248, 117, 172, 18);
/*      */ 
/*  157 */   private static final DLSID DLSID_GSInHardware = new DLSID(395259685L, 50020, 4561, 167, 96, 0, 0, 248, 117, 172, 18);
/*      */ 
/*  159 */   private static final DLSID DLSID_XGInHardware = new DLSID(395259686L, 50020, 4561, 167, 96, 0, 0, 248, 117, 172, 18);
/*      */ 
/*  161 */   private static final DLSID DLSID_SupportsDLS1 = new DLSID(395259687L, 50020, 4561, 167, 96, 0, 0, 248, 117, 172, 18);
/*      */ 
/*  163 */   private static final DLSID DLSID_SupportsDLS2 = new DLSID(-247096859L, 18057, 4562, 175, 166, 0, 170, 0, 36, 216, 182);
/*      */ 
/*  165 */   private static final DLSID DLSID_SampleMemorySize = new DLSID(395259688L, 50020, 4561, 167, 96, 0, 0, 248, 117, 172, 18);
/*      */ 
/*  167 */   private static final DLSID DLSID_ManufacturersID = new DLSID(-1338109567L, 32917, 4562, 161, 239, 0, 96, 8, 51, 219, 216);
/*      */ 
/*  169 */   private static final DLSID DLSID_ProductID = new DLSID(-1338109566L, 32917, 4562, 161, 239, 0, 96, 8, 51, 219, 216);
/*      */ 
/*  171 */   private static final DLSID DLSID_SamplePlaybackRate = new DLSID(714209043L, 42175, 4562, 187, 223, 0, 96, 8, 51, 219, 216);
/*      */ 
/*  174 */   private long major = -1L;
/*  175 */   private long minor = -1L;
/*      */ 
/*  177 */   private final DLSInfo info = new DLSInfo();
/*      */ 
/*  179 */   private final List<DLSInstrument> instruments = new ArrayList();
/*  180 */   private final List<DLSSample> samples = new ArrayList();
/*      */ 
/*  182 */   private boolean largeFormat = false;
/*      */   private File sampleFile;
/*  585 */   private Map<DLSRegion, Long> temp_rgnassign = new HashMap();
/*      */ 
/*      */   public DLSSoundbank()
/*      */   {
/*      */   }
/*      */ 
/*      */   public DLSSoundbank(URL paramURL)
/*      */     throws IOException
/*      */   {
/*  189 */     InputStream localInputStream = paramURL.openStream();
/*      */     try {
/*  191 */       readSoundbank(localInputStream);
/*      */     } finally {
/*  193 */       localInputStream.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   public DLSSoundbank(File paramFile) throws IOException {
/*  198 */     this.largeFormat = true;
/*  199 */     this.sampleFile = paramFile;
/*  200 */     FileInputStream localFileInputStream = new FileInputStream(paramFile);
/*      */     try {
/*  202 */       readSoundbank(localFileInputStream);
/*      */     } finally {
/*  204 */       localFileInputStream.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   public DLSSoundbank(InputStream paramInputStream) throws IOException {
/*  209 */     readSoundbank(paramInputStream);
/*      */   }
/*      */ 
/*      */   private void readSoundbank(InputStream paramInputStream) throws IOException {
/*  213 */     RIFFReader localRIFFReader = new RIFFReader(paramInputStream);
/*  214 */     if (!localRIFFReader.getFormat().equals("RIFF")) {
/*  215 */       throw new RIFFInvalidFormatException("Input stream is not a valid RIFF stream!");
/*      */     }
/*      */ 
/*  218 */     if (!localRIFFReader.getType().equals("DLS ")) {
/*  219 */       throw new RIFFInvalidFormatException("Input stream is not a valid DLS soundbank!");
/*      */     }
/*      */ 
/*  222 */     while (localRIFFReader.hasNextChunk()) {
/*  223 */       localObject = localRIFFReader.nextChunk();
/*  224 */       if (((RIFFReader)localObject).getFormat().equals("LIST")) {
/*  225 */         if (((RIFFReader)localObject).getType().equals("INFO"))
/*  226 */           readInfoChunk((RIFFReader)localObject);
/*  227 */         if (((RIFFReader)localObject).getType().equals("lins"))
/*  228 */           readLinsChunk((RIFFReader)localObject);
/*  229 */         if (((RIFFReader)localObject).getType().equals("wvpl"))
/*  230 */           readWvplChunk((RIFFReader)localObject);
/*      */       } else {
/*  232 */         if ((((RIFFReader)localObject).getFormat().equals("cdl ")) && 
/*  233 */           (!readCdlChunk((RIFFReader)localObject))) {
/*  234 */           throw new RIFFInvalidFormatException("DLS file isn't supported!");
/*      */         }
/*      */ 
/*  238 */         if ((!((RIFFReader)localObject).getFormat().equals("colh")) || (
/*  243 */           (!((RIFFReader)localObject).getFormat().equals("ptbl")) || 
/*  247 */           (((RIFFReader)localObject).getFormat().equals("vers")))) {
/*  248 */           this.major = ((RIFFReader)localObject).readUnsignedInt();
/*  249 */           this.minor = ((RIFFReader)localObject).readUnsignedInt();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  254 */     for (Object localObject = this.temp_rgnassign.entrySet().iterator(); ((Iterator)localObject).hasNext(); ) { Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
/*  255 */       ((DLSRegion)localEntry.getKey()).sample = ((DLSSample)this.samples.get((int)((Long)localEntry.getValue()).longValue()));
/*      */     }
/*      */ 
/*  258 */     this.temp_rgnassign = null;
/*      */   }
/*      */ 
/*      */   private boolean cdlIsQuerySupported(DLSID paramDLSID) {
/*  262 */     return (paramDLSID.equals(DLSID_GMInHardware)) || (paramDLSID.equals(DLSID_GSInHardware)) || (paramDLSID.equals(DLSID_XGInHardware)) || (paramDLSID.equals(DLSID_SupportsDLS1)) || (paramDLSID.equals(DLSID_SupportsDLS2)) || (paramDLSID.equals(DLSID_SampleMemorySize)) || (paramDLSID.equals(DLSID_ManufacturersID)) || (paramDLSID.equals(DLSID_ProductID)) || (paramDLSID.equals(DLSID_SamplePlaybackRate));
/*      */   }
/*      */ 
/*      */   private long cdlQuery(DLSID paramDLSID)
/*      */   {
/*  274 */     if (paramDLSID.equals(DLSID_GMInHardware))
/*  275 */       return 1L;
/*  276 */     if (paramDLSID.equals(DLSID_GSInHardware))
/*  277 */       return 0L;
/*  278 */     if (paramDLSID.equals(DLSID_XGInHardware))
/*  279 */       return 0L;
/*  280 */     if (paramDLSID.equals(DLSID_SupportsDLS1))
/*  281 */       return 1L;
/*  282 */     if (paramDLSID.equals(DLSID_SupportsDLS2))
/*  283 */       return 1L;
/*  284 */     if (paramDLSID.equals(DLSID_SampleMemorySize))
/*  285 */       return Runtime.getRuntime().totalMemory();
/*  286 */     if (paramDLSID.equals(DLSID_ManufacturersID))
/*  287 */       return 0L;
/*  288 */     if (paramDLSID.equals(DLSID_ProductID))
/*  289 */       return 0L;
/*  290 */     if (paramDLSID.equals(DLSID_SamplePlaybackRate))
/*  291 */       return 44100L;
/*  292 */     return 0L;
/*      */   }
/*      */ 
/*      */   private boolean readCdlChunk(RIFFReader paramRIFFReader)
/*      */     throws IOException
/*      */   {
/*  303 */     Stack localStack = new Stack();
/*      */ 
/*  305 */     while (paramRIFFReader.available() != 0) {
/*  306 */       int i = paramRIFFReader.readUnsignedShort();
/*      */       long l1;
/*      */       long l2;
/*      */       DLSID localDLSID;
/*  307 */       switch (i) {
/*      */       case 1:
/*  309 */         l1 = ((Long)localStack.pop()).longValue();
/*  310 */         l2 = ((Long)localStack.pop()).longValue();
/*  311 */         localStack.push(Long.valueOf((l1 != 0L) && (l2 != 0L) ? 1L : 0L));
/*  312 */         break;
/*      */       case 2:
/*  314 */         l1 = ((Long)localStack.pop()).longValue();
/*  315 */         l2 = ((Long)localStack.pop()).longValue();
/*  316 */         localStack.push(Long.valueOf((l1 != 0L) || (l2 != 0L) ? 1L : 0L));
/*  317 */         break;
/*      */       case 3:
/*  319 */         l1 = ((Long)localStack.pop()).longValue();
/*  320 */         l2 = ((Long)localStack.pop()).longValue();
/*  321 */         localStack.push(Long.valueOf(((l1 != 0L ? 1 : 0) ^ (l2 != 0L ? 1 : 0)) != 0 ? 1L : 0L));
/*  322 */         break;
/*      */       case 4:
/*  324 */         l1 = ((Long)localStack.pop()).longValue();
/*  325 */         l2 = ((Long)localStack.pop()).longValue();
/*  326 */         localStack.push(Long.valueOf(l1 + l2));
/*  327 */         break;
/*      */       case 5:
/*  329 */         l1 = ((Long)localStack.pop()).longValue();
/*  330 */         l2 = ((Long)localStack.pop()).longValue();
/*  331 */         localStack.push(Long.valueOf(l1 - l2));
/*  332 */         break;
/*      */       case 6:
/*  334 */         l1 = ((Long)localStack.pop()).longValue();
/*  335 */         l2 = ((Long)localStack.pop()).longValue();
/*  336 */         localStack.push(Long.valueOf(l1 * l2));
/*  337 */         break;
/*      */       case 7:
/*  339 */         l1 = ((Long)localStack.pop()).longValue();
/*  340 */         l2 = ((Long)localStack.pop()).longValue();
/*  341 */         localStack.push(Long.valueOf(l1 / l2));
/*  342 */         break;
/*      */       case 8:
/*  344 */         l1 = ((Long)localStack.pop()).longValue();
/*  345 */         l2 = ((Long)localStack.pop()).longValue();
/*  346 */         localStack.push(Long.valueOf((l1 != 0L) && (l2 != 0L) ? 1L : 0L));
/*  347 */         break;
/*      */       case 9:
/*  349 */         l1 = ((Long)localStack.pop()).longValue();
/*  350 */         l2 = ((Long)localStack.pop()).longValue();
/*  351 */         localStack.push(Long.valueOf((l1 != 0L) || (l2 != 0L) ? 1L : 0L));
/*  352 */         break;
/*      */       case 10:
/*  354 */         l1 = ((Long)localStack.pop()).longValue();
/*  355 */         l2 = ((Long)localStack.pop()).longValue();
/*  356 */         localStack.push(Long.valueOf(l1 < l2 ? 1L : 0L));
/*  357 */         break;
/*      */       case 11:
/*  359 */         l1 = ((Long)localStack.pop()).longValue();
/*  360 */         l2 = ((Long)localStack.pop()).longValue();
/*  361 */         localStack.push(Long.valueOf(l1 <= l2 ? 1L : 0L));
/*  362 */         break;
/*      */       case 12:
/*  364 */         l1 = ((Long)localStack.pop()).longValue();
/*  365 */         l2 = ((Long)localStack.pop()).longValue();
/*  366 */         localStack.push(Long.valueOf(l1 > l2 ? 1L : 0L));
/*  367 */         break;
/*      */       case 13:
/*  369 */         l1 = ((Long)localStack.pop()).longValue();
/*  370 */         l2 = ((Long)localStack.pop()).longValue();
/*  371 */         localStack.push(Long.valueOf(l1 >= l2 ? 1L : 0L));
/*  372 */         break;
/*      */       case 14:
/*  374 */         l1 = ((Long)localStack.pop()).longValue();
/*  375 */         l2 = ((Long)localStack.pop()).longValue();
/*  376 */         localStack.push(Long.valueOf(l1 == l2 ? 1L : 0L));
/*  377 */         break;
/*      */       case 15:
/*  379 */         l1 = ((Long)localStack.pop()).longValue();
/*  380 */         l2 = ((Long)localStack.pop()).longValue();
/*  381 */         localStack.push(Long.valueOf(l1 == 0L ? 1L : 0L));
/*  382 */         break;
/*      */       case 16:
/*  384 */         localStack.push(Long.valueOf(paramRIFFReader.readUnsignedInt()));
/*  385 */         break;
/*      */       case 17:
/*  387 */         localDLSID = DLSID.read(paramRIFFReader);
/*  388 */         localStack.push(Long.valueOf(cdlQuery(localDLSID)));
/*  389 */         break;
/*      */       case 18:
/*  391 */         localDLSID = DLSID.read(paramRIFFReader);
/*  392 */         localStack.push(Long.valueOf(cdlIsQuerySupported(localDLSID) ? 1L : 0L));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  398 */     if (localStack.isEmpty()) {
/*  399 */       return false;
/*      */     }
/*  401 */     return ((Long)localStack.pop()).longValue() == 1L;
/*      */   }
/*      */ 
/*      */   private void readInfoChunk(RIFFReader paramRIFFReader) throws IOException {
/*  405 */     this.info.name = null;
/*  406 */     while (paramRIFFReader.hasNextChunk()) {
/*  407 */       RIFFReader localRIFFReader = paramRIFFReader.nextChunk();
/*  408 */       String str = localRIFFReader.getFormat();
/*  409 */       if (str.equals("INAM"))
/*  410 */         this.info.name = localRIFFReader.readString(localRIFFReader.available());
/*  411 */       else if (str.equals("ICRD"))
/*  412 */         this.info.creationDate = localRIFFReader.readString(localRIFFReader.available());
/*  413 */       else if (str.equals("IENG"))
/*  414 */         this.info.engineers = localRIFFReader.readString(localRIFFReader.available());
/*  415 */       else if (str.equals("IPRD"))
/*  416 */         this.info.product = localRIFFReader.readString(localRIFFReader.available());
/*  417 */       else if (str.equals("ICOP"))
/*  418 */         this.info.copyright = localRIFFReader.readString(localRIFFReader.available());
/*  419 */       else if (str.equals("ICMT"))
/*  420 */         this.info.comments = localRIFFReader.readString(localRIFFReader.available());
/*  421 */       else if (str.equals("ISFT"))
/*  422 */         this.info.tools = localRIFFReader.readString(localRIFFReader.available());
/*  423 */       else if (str.equals("IARL"))
/*  424 */         this.info.archival_location = localRIFFReader.readString(localRIFFReader.available());
/*  425 */       else if (str.equals("IART"))
/*  426 */         this.info.artist = localRIFFReader.readString(localRIFFReader.available());
/*  427 */       else if (str.equals("ICMS"))
/*  428 */         this.info.commissioned = localRIFFReader.readString(localRIFFReader.available());
/*  429 */       else if (str.equals("IGNR"))
/*  430 */         this.info.genre = localRIFFReader.readString(localRIFFReader.available());
/*  431 */       else if (str.equals("IKEY"))
/*  432 */         this.info.keywords = localRIFFReader.readString(localRIFFReader.available());
/*  433 */       else if (str.equals("IMED"))
/*  434 */         this.info.medium = localRIFFReader.readString(localRIFFReader.available());
/*  435 */       else if (str.equals("ISBJ"))
/*  436 */         this.info.subject = localRIFFReader.readString(localRIFFReader.available());
/*  437 */       else if (str.equals("ISRC"))
/*  438 */         this.info.source = localRIFFReader.readString(localRIFFReader.available());
/*  439 */       else if (str.equals("ISRF"))
/*  440 */         this.info.source_form = localRIFFReader.readString(localRIFFReader.available());
/*  441 */       else if (str.equals("ITCH"))
/*  442 */         this.info.technician = localRIFFReader.readString(localRIFFReader.available());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readLinsChunk(RIFFReader paramRIFFReader) throws IOException {
/*  447 */     while (paramRIFFReader.hasNextChunk()) {
/*  448 */       RIFFReader localRIFFReader = paramRIFFReader.nextChunk();
/*  449 */       if ((localRIFFReader.getFormat().equals("LIST")) && 
/*  450 */         (localRIFFReader.getType().equals("ins ")))
/*  451 */         readInsChunk(localRIFFReader);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readInsChunk(RIFFReader paramRIFFReader) throws IOException
/*      */   {
/*  457 */     DLSInstrument localDLSInstrument = new DLSInstrument(this);
/*      */ 
/*  459 */     while (paramRIFFReader.hasNextChunk()) {
/*  460 */       RIFFReader localRIFFReader = paramRIFFReader.nextChunk();
/*  461 */       String str = localRIFFReader.getFormat();
/*  462 */       if (str.equals("LIST")) {
/*  463 */         if (localRIFFReader.getType().equals("INFO"))
/*  464 */           readInsInfoChunk(localDLSInstrument, localRIFFReader);
/*      */         Object localObject1;
/*      */         Object localObject2;
/*  466 */         if (localRIFFReader.getType().equals("lrgn")) {
/*  467 */           while (localRIFFReader.hasNextChunk()) {
/*  468 */             localObject1 = localRIFFReader.nextChunk();
/*  469 */             if (((RIFFReader)localObject1).getFormat().equals("LIST")) {
/*  470 */               if (((RIFFReader)localObject1).getType().equals("rgn ")) {
/*  471 */                 localObject2 = new DLSRegion();
/*  472 */                 if (readRgnChunk((DLSRegion)localObject2, (RIFFReader)localObject1))
/*  473 */                   localDLSInstrument.getRegions().add(localObject2);
/*      */               }
/*  475 */               if (((RIFFReader)localObject1).getType().equals("rgn2"))
/*      */               {
/*  477 */                 localObject2 = new DLSRegion();
/*  478 */                 if (readRgnChunk((DLSRegion)localObject2, (RIFFReader)localObject1))
/*  479 */                   localDLSInstrument.getRegions().add(localObject2);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*  484 */         if (localRIFFReader.getType().equals("lart")) {
/*  485 */           localObject1 = new ArrayList();
/*  486 */           while (localRIFFReader.hasNextChunk()) {
/*  487 */             localObject2 = localRIFFReader.nextChunk();
/*  488 */             if ((localRIFFReader.getFormat().equals("cdl ")) && 
/*  489 */               (!readCdlChunk(localRIFFReader))) {
/*  490 */               ((List)localObject1).clear();
/*  491 */               break;
/*      */             }
/*      */ 
/*  494 */             if (((RIFFReader)localObject2).getFormat().equals("art1"))
/*  495 */               readArt1Chunk((List)localObject1, (RIFFReader)localObject2);
/*      */           }
/*  497 */           localDLSInstrument.getModulators().addAll((Collection)localObject1);
/*      */         }
/*  499 */         if (localRIFFReader.getType().equals("lar2"))
/*      */         {
/*  501 */           localObject1 = new ArrayList();
/*  502 */           while (localRIFFReader.hasNextChunk()) {
/*  503 */             localObject2 = localRIFFReader.nextChunk();
/*  504 */             if ((localRIFFReader.getFormat().equals("cdl ")) && 
/*  505 */               (!readCdlChunk(localRIFFReader))) {
/*  506 */               ((List)localObject1).clear();
/*  507 */               break;
/*      */             }
/*      */ 
/*  510 */             if (((RIFFReader)localObject2).getFormat().equals("art2"))
/*  511 */               readArt2Chunk((List)localObject1, (RIFFReader)localObject2);
/*      */           }
/*  513 */           localDLSInstrument.getModulators().addAll((Collection)localObject1);
/*      */         }
/*      */       } else {
/*  516 */         if (str.equals("dlid")) {
/*  517 */           localDLSInstrument.guid = new byte[16];
/*  518 */           localRIFFReader.readFully(localDLSInstrument.guid);
/*      */         }
/*  520 */         if (str.equals("insh")) {
/*  521 */           localRIFFReader.readUnsignedInt();
/*      */ 
/*  523 */           int i = localRIFFReader.read();
/*  524 */           i += ((localRIFFReader.read() & 0x7F) << 7);
/*  525 */           localRIFFReader.read();
/*  526 */           int j = localRIFFReader.read();
/*      */ 
/*  528 */           int k = localRIFFReader.read() & 0x7F;
/*  529 */           localRIFFReader.read();
/*  530 */           localRIFFReader.read();
/*  531 */           localRIFFReader.read();
/*      */ 
/*  533 */           localDLSInstrument.bank = i;
/*  534 */           localDLSInstrument.preset = k;
/*  535 */           localDLSInstrument.druminstrument = ((j & 0x80) > 0);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  542 */     this.instruments.add(localDLSInstrument);
/*      */   }
/*      */ 
/*      */   private void readArt1Chunk(List<DLSModulator> paramList, RIFFReader paramRIFFReader) throws IOException
/*      */   {
/*  547 */     long l1 = paramRIFFReader.readUnsignedInt();
/*  548 */     long l2 = paramRIFFReader.readUnsignedInt();
/*      */ 
/*  550 */     if (l1 - 8L != 0L) {
/*  551 */       paramRIFFReader.skipBytes(l1 - 8L);
/*      */     }
/*  553 */     for (int i = 0; i < l2; i++) {
/*  554 */       DLSModulator localDLSModulator = new DLSModulator();
/*  555 */       localDLSModulator.version = 1;
/*  556 */       localDLSModulator.source = paramRIFFReader.readUnsignedShort();
/*  557 */       localDLSModulator.control = paramRIFFReader.readUnsignedShort();
/*  558 */       localDLSModulator.destination = paramRIFFReader.readUnsignedShort();
/*  559 */       localDLSModulator.transform = paramRIFFReader.readUnsignedShort();
/*  560 */       localDLSModulator.scale = paramRIFFReader.readInt();
/*  561 */       paramList.add(localDLSModulator);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readArt2Chunk(List<DLSModulator> paramList, RIFFReader paramRIFFReader) throws IOException
/*      */   {
/*  567 */     long l1 = paramRIFFReader.readUnsignedInt();
/*  568 */     long l2 = paramRIFFReader.readUnsignedInt();
/*      */ 
/*  570 */     if (l1 - 8L != 0L) {
/*  571 */       paramRIFFReader.skipBytes(l1 - 8L);
/*      */     }
/*  573 */     for (int i = 0; i < l2; i++) {
/*  574 */       DLSModulator localDLSModulator = new DLSModulator();
/*  575 */       localDLSModulator.version = 2;
/*  576 */       localDLSModulator.source = paramRIFFReader.readUnsignedShort();
/*  577 */       localDLSModulator.control = paramRIFFReader.readUnsignedShort();
/*  578 */       localDLSModulator.destination = paramRIFFReader.readUnsignedShort();
/*  579 */       localDLSModulator.transform = paramRIFFReader.readUnsignedShort();
/*  580 */       localDLSModulator.scale = paramRIFFReader.readInt();
/*  581 */       paramList.add(localDLSModulator);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean readRgnChunk(DLSRegion paramDLSRegion, RIFFReader paramRIFFReader)
/*      */     throws IOException
/*      */   {
/*  589 */     while (paramRIFFReader.hasNextChunk()) {
/*  590 */       RIFFReader localRIFFReader1 = paramRIFFReader.nextChunk();
/*  591 */       String str = localRIFFReader1.getFormat();
/*  592 */       if (str.equals("LIST"))
/*      */       {
/*      */         ArrayList localArrayList;
/*      */         RIFFReader localRIFFReader2;
/*  593 */         if (localRIFFReader1.getType().equals("lart")) {
/*  594 */           localArrayList = new ArrayList();
/*  595 */           while (localRIFFReader1.hasNextChunk()) {
/*  596 */             localRIFFReader2 = localRIFFReader1.nextChunk();
/*  597 */             if ((localRIFFReader1.getFormat().equals("cdl ")) && 
/*  598 */               (!readCdlChunk(localRIFFReader1))) {
/*  599 */               localArrayList.clear();
/*  600 */               break;
/*      */             }
/*      */ 
/*  603 */             if (localRIFFReader2.getFormat().equals("art1"))
/*  604 */               readArt1Chunk(localArrayList, localRIFFReader2);
/*      */           }
/*  606 */           paramDLSRegion.getModulators().addAll(localArrayList);
/*      */         }
/*  608 */         if (localRIFFReader1.getType().equals("lar2"))
/*      */         {
/*  610 */           localArrayList = new ArrayList();
/*  611 */           while (localRIFFReader1.hasNextChunk()) {
/*  612 */             localRIFFReader2 = localRIFFReader1.nextChunk();
/*  613 */             if ((localRIFFReader1.getFormat().equals("cdl ")) && 
/*  614 */               (!readCdlChunk(localRIFFReader1))) {
/*  615 */               localArrayList.clear();
/*  616 */               break;
/*      */             }
/*      */ 
/*  619 */             if (localRIFFReader2.getFormat().equals("art2"))
/*  620 */               readArt2Chunk(localArrayList, localRIFFReader2);
/*      */           }
/*  622 */           paramDLSRegion.getModulators().addAll(localArrayList);
/*      */         }
/*      */       }
/*      */       else {
/*  626 */         if ((str.equals("cdl ")) && 
/*  627 */           (!readCdlChunk(localRIFFReader1))) {
/*  628 */           return false;
/*      */         }
/*  630 */         if (str.equals("rgnh")) {
/*  631 */           paramDLSRegion.keyfrom = localRIFFReader1.readUnsignedShort();
/*  632 */           paramDLSRegion.keyto = localRIFFReader1.readUnsignedShort();
/*  633 */           paramDLSRegion.velfrom = localRIFFReader1.readUnsignedShort();
/*  634 */           paramDLSRegion.velto = localRIFFReader1.readUnsignedShort();
/*  635 */           paramDLSRegion.options = localRIFFReader1.readUnsignedShort();
/*  636 */           paramDLSRegion.exclusiveClass = localRIFFReader1.readUnsignedShort();
/*      */         }
/*  638 */         if (str.equals("wlnk")) {
/*  639 */           paramDLSRegion.fusoptions = localRIFFReader1.readUnsignedShort();
/*  640 */           paramDLSRegion.phasegroup = localRIFFReader1.readUnsignedShort();
/*  641 */           paramDLSRegion.channel = localRIFFReader1.readUnsignedInt();
/*  642 */           long l = localRIFFReader1.readUnsignedInt();
/*  643 */           this.temp_rgnassign.put(paramDLSRegion, Long.valueOf(l));
/*      */         }
/*  645 */         if (str.equals("wsmp")) {
/*  646 */           paramDLSRegion.sampleoptions = new DLSSampleOptions();
/*  647 */           readWsmpChunk(paramDLSRegion.sampleoptions, localRIFFReader1);
/*      */         }
/*      */       }
/*      */     }
/*  651 */     return true;
/*      */   }
/*      */ 
/*      */   private void readWsmpChunk(DLSSampleOptions paramDLSSampleOptions, RIFFReader paramRIFFReader) throws IOException
/*      */   {
/*  656 */     long l1 = paramRIFFReader.readUnsignedInt();
/*  657 */     paramDLSSampleOptions.unitynote = paramRIFFReader.readUnsignedShort();
/*  658 */     paramDLSSampleOptions.finetune = paramRIFFReader.readShort();
/*  659 */     paramDLSSampleOptions.attenuation = paramRIFFReader.readInt();
/*  660 */     paramDLSSampleOptions.options = paramRIFFReader.readUnsignedInt();
/*  661 */     long l2 = paramRIFFReader.readInt();
/*      */ 
/*  663 */     if (l1 > 20L) {
/*  664 */       paramRIFFReader.skipBytes(l1 - 20L);
/*      */     }
/*  666 */     for (int i = 0; i < l2; i++) {
/*  667 */       DLSSampleLoop localDLSSampleLoop = new DLSSampleLoop();
/*  668 */       long l3 = paramRIFFReader.readUnsignedInt();
/*  669 */       localDLSSampleLoop.type = paramRIFFReader.readUnsignedInt();
/*  670 */       localDLSSampleLoop.start = paramRIFFReader.readUnsignedInt();
/*  671 */       localDLSSampleLoop.length = paramRIFFReader.readUnsignedInt();
/*  672 */       paramDLSSampleOptions.loops.add(localDLSSampleLoop);
/*  673 */       if (l3 > 16L)
/*  674 */         paramRIFFReader.skipBytes(l3 - 16L);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readInsInfoChunk(DLSInstrument paramDLSInstrument, RIFFReader paramRIFFReader) throws IOException
/*      */   {
/*  680 */     paramDLSInstrument.info.name = null;
/*  681 */     while (paramRIFFReader.hasNextChunk()) {
/*  682 */       RIFFReader localRIFFReader = paramRIFFReader.nextChunk();
/*  683 */       String str = localRIFFReader.getFormat();
/*  684 */       if (str.equals("INAM"))
/*  685 */         paramDLSInstrument.info.name = localRIFFReader.readString(localRIFFReader.available());
/*  686 */       else if (str.equals("ICRD")) {
/*  687 */         paramDLSInstrument.info.creationDate = localRIFFReader.readString(localRIFFReader.available());
/*      */       }
/*  689 */       else if (str.equals("IENG")) {
/*  690 */         paramDLSInstrument.info.engineers = localRIFFReader.readString(localRIFFReader.available());
/*      */       }
/*  692 */       else if (str.equals("IPRD"))
/*  693 */         paramDLSInstrument.info.product = localRIFFReader.readString(localRIFFReader.available());
/*  694 */       else if (str.equals("ICOP")) {
/*  695 */         paramDLSInstrument.info.copyright = localRIFFReader.readString(localRIFFReader.available());
/*      */       }
/*  697 */       else if (str.equals("ICMT")) {
/*  698 */         paramDLSInstrument.info.comments = localRIFFReader.readString(localRIFFReader.available());
/*      */       }
/*  700 */       else if (str.equals("ISFT"))
/*  701 */         paramDLSInstrument.info.tools = localRIFFReader.readString(localRIFFReader.available());
/*  702 */       else if (str.equals("IARL")) {
/*  703 */         paramDLSInstrument.info.archival_location = localRIFFReader.readString(localRIFFReader.available());
/*      */       }
/*  705 */       else if (str.equals("IART"))
/*  706 */         paramDLSInstrument.info.artist = localRIFFReader.readString(localRIFFReader.available());
/*  707 */       else if (str.equals("ICMS")) {
/*  708 */         paramDLSInstrument.info.commissioned = localRIFFReader.readString(localRIFFReader.available());
/*      */       }
/*  710 */       else if (str.equals("IGNR"))
/*  711 */         paramDLSInstrument.info.genre = localRIFFReader.readString(localRIFFReader.available());
/*  712 */       else if (str.equals("IKEY")) {
/*  713 */         paramDLSInstrument.info.keywords = localRIFFReader.readString(localRIFFReader.available());
/*      */       }
/*  715 */       else if (str.equals("IMED"))
/*  716 */         paramDLSInstrument.info.medium = localRIFFReader.readString(localRIFFReader.available());
/*  717 */       else if (str.equals("ISBJ"))
/*  718 */         paramDLSInstrument.info.subject = localRIFFReader.readString(localRIFFReader.available());
/*  719 */       else if (str.equals("ISRC"))
/*  720 */         paramDLSInstrument.info.source = localRIFFReader.readString(localRIFFReader.available());
/*  721 */       else if (str.equals("ISRF")) {
/*  722 */         paramDLSInstrument.info.source_form = localRIFFReader.readString(localRIFFReader.available());
/*      */       }
/*  724 */       else if (str.equals("ITCH"))
/*  725 */         paramDLSInstrument.info.technician = localRIFFReader.readString(localRIFFReader.available());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readWvplChunk(RIFFReader paramRIFFReader)
/*      */     throws IOException
/*      */   {
/*  732 */     while (paramRIFFReader.hasNextChunk()) {
/*  733 */       RIFFReader localRIFFReader = paramRIFFReader.nextChunk();
/*  734 */       if ((localRIFFReader.getFormat().equals("LIST")) && 
/*  735 */         (localRIFFReader.getType().equals("wave")))
/*  736 */         readWaveChunk(localRIFFReader);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readWaveChunk(RIFFReader paramRIFFReader) throws IOException
/*      */   {
/*  742 */     DLSSample localDLSSample = new DLSSample(this);
/*      */ 
/*  744 */     while (paramRIFFReader.hasNextChunk()) {
/*  745 */       RIFFReader localRIFFReader = paramRIFFReader.nextChunk();
/*  746 */       String str = localRIFFReader.getFormat();
/*  747 */       if (str.equals("LIST")) {
/*  748 */         if (localRIFFReader.getType().equals("INFO"))
/*  749 */           readWaveInfoChunk(localDLSSample, localRIFFReader);
/*      */       }
/*      */       else {
/*  752 */         if (str.equals("dlid")) {
/*  753 */           localDLSSample.guid = new byte[16];
/*  754 */           localRIFFReader.readFully(localDLSSample.guid);
/*      */         }
/*      */         int j;
/*  757 */         if (str.equals("fmt ")) {
/*  758 */           int i = localRIFFReader.readUnsignedShort();
/*  759 */           if ((i != 1) && (i != 3)) {
/*  760 */             throw new RIFFInvalidDataException("Only PCM samples are supported!");
/*      */           }
/*      */ 
/*  763 */           j = localRIFFReader.readUnsignedShort();
/*  764 */           long l = localRIFFReader.readUnsignedInt();
/*      */ 
/*  766 */           localRIFFReader.readUnsignedInt();
/*      */ 
/*  768 */           int m = localRIFFReader.readUnsignedShort();
/*  769 */           int n = localRIFFReader.readUnsignedShort();
/*  770 */           AudioFormat localAudioFormat = null;
/*  771 */           if (i == 1) {
/*  772 */             if (n == 8) {
/*  773 */               localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, (float)l, n, j, m, (float)l, false);
/*      */             }
/*      */             else
/*      */             {
/*  777 */               localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, (float)l, n, j, m, (float)l, false);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  782 */           if (i == 3) {
/*  783 */             localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, (float)l, n, j, m, (float)l, false);
/*      */           }
/*      */ 
/*  788 */           localDLSSample.format = localAudioFormat;
/*      */         }
/*      */ 
/*  791 */         if (str.equals("data")) {
/*  792 */           if (this.largeFormat) {
/*  793 */             localDLSSample.setData(new ModelByteBuffer(this.sampleFile, localRIFFReader.getFilePointer(), localRIFFReader.available()));
/*      */           }
/*      */           else {
/*  796 */             byte[] arrayOfByte = new byte[localRIFFReader.available()];
/*      */ 
/*  798 */             localDLSSample.setData(arrayOfByte);
/*      */ 
/*  800 */             j = 0;
/*  801 */             int k = localRIFFReader.available();
/*  802 */             while (j != k) {
/*  803 */               if (k - j > 65536) {
/*  804 */                 localRIFFReader.readFully(arrayOfByte, j, 65536);
/*  805 */                 j += 65536;
/*      */               } else {
/*  807 */                 localRIFFReader.readFully(arrayOfByte, j, k - j);
/*  808 */                 j = k;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  814 */         if (str.equals("wsmp")) {
/*  815 */           localDLSSample.sampleoptions = new DLSSampleOptions();
/*  816 */           readWsmpChunk(localDLSSample.sampleoptions, localRIFFReader);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  821 */     this.samples.add(localDLSSample);
/*      */   }
/*      */ 
/*      */   private void readWaveInfoChunk(DLSSample paramDLSSample, RIFFReader paramRIFFReader)
/*      */     throws IOException
/*      */   {
/*  827 */     paramDLSSample.info.name = null;
/*  828 */     while (paramRIFFReader.hasNextChunk()) {
/*  829 */       RIFFReader localRIFFReader = paramRIFFReader.nextChunk();
/*  830 */       String str = localRIFFReader.getFormat();
/*  831 */       if (str.equals("INAM"))
/*  832 */         paramDLSSample.info.name = localRIFFReader.readString(localRIFFReader.available());
/*  833 */       else if (str.equals("ICRD")) {
/*  834 */         paramDLSSample.info.creationDate = localRIFFReader.readString(localRIFFReader.available());
/*      */       }
/*  836 */       else if (str.equals("IENG"))
/*  837 */         paramDLSSample.info.engineers = localRIFFReader.readString(localRIFFReader.available());
/*  838 */       else if (str.equals("IPRD"))
/*  839 */         paramDLSSample.info.product = localRIFFReader.readString(localRIFFReader.available());
/*  840 */       else if (str.equals("ICOP"))
/*  841 */         paramDLSSample.info.copyright = localRIFFReader.readString(localRIFFReader.available());
/*  842 */       else if (str.equals("ICMT"))
/*  843 */         paramDLSSample.info.comments = localRIFFReader.readString(localRIFFReader.available());
/*  844 */       else if (str.equals("ISFT"))
/*  845 */         paramDLSSample.info.tools = localRIFFReader.readString(localRIFFReader.available());
/*  846 */       else if (str.equals("IARL")) {
/*  847 */         paramDLSSample.info.archival_location = localRIFFReader.readString(localRIFFReader.available());
/*      */       }
/*  849 */       else if (str.equals("IART"))
/*  850 */         paramDLSSample.info.artist = localRIFFReader.readString(localRIFFReader.available());
/*  851 */       else if (str.equals("ICMS")) {
/*  852 */         paramDLSSample.info.commissioned = localRIFFReader.readString(localRIFFReader.available());
/*      */       }
/*  854 */       else if (str.equals("IGNR"))
/*  855 */         paramDLSSample.info.genre = localRIFFReader.readString(localRIFFReader.available());
/*  856 */       else if (str.equals("IKEY"))
/*  857 */         paramDLSSample.info.keywords = localRIFFReader.readString(localRIFFReader.available());
/*  858 */       else if (str.equals("IMED"))
/*  859 */         paramDLSSample.info.medium = localRIFFReader.readString(localRIFFReader.available());
/*  860 */       else if (str.equals("ISBJ"))
/*  861 */         paramDLSSample.info.subject = localRIFFReader.readString(localRIFFReader.available());
/*  862 */       else if (str.equals("ISRC"))
/*  863 */         paramDLSSample.info.source = localRIFFReader.readString(localRIFFReader.available());
/*  864 */       else if (str.equals("ISRF"))
/*  865 */         paramDLSSample.info.source_form = localRIFFReader.readString(localRIFFReader.available());
/*  866 */       else if (str.equals("ITCH"))
/*  867 */         paramDLSSample.info.technician = localRIFFReader.readString(localRIFFReader.available());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void save(String paramString) throws IOException
/*      */   {
/*  873 */     writeSoundbank(new RIFFWriter(paramString, "DLS "));
/*      */   }
/*      */ 
/*      */   public void save(File paramFile) throws IOException {
/*  877 */     writeSoundbank(new RIFFWriter(paramFile, "DLS "));
/*      */   }
/*      */ 
/*      */   public void save(OutputStream paramOutputStream) throws IOException {
/*  881 */     writeSoundbank(new RIFFWriter(paramOutputStream, "DLS "));
/*      */   }
/*      */ 
/*      */   private void writeSoundbank(RIFFWriter paramRIFFWriter) throws IOException {
/*  885 */     RIFFWriter localRIFFWriter1 = paramRIFFWriter.writeChunk("colh");
/*  886 */     localRIFFWriter1.writeUnsignedInt(this.instruments.size());
/*      */ 
/*  888 */     if ((this.major != -1L) && (this.minor != -1L)) {
/*  889 */       localRIFFWriter2 = paramRIFFWriter.writeChunk("vers");
/*  890 */       localRIFFWriter2.writeUnsignedInt(this.major);
/*  891 */       localRIFFWriter2.writeUnsignedInt(this.minor);
/*      */     }
/*      */ 
/*  894 */     writeInstruments(paramRIFFWriter.writeList("lins"));
/*      */ 
/*  896 */     RIFFWriter localRIFFWriter2 = paramRIFFWriter.writeChunk("ptbl");
/*  897 */     localRIFFWriter2.writeUnsignedInt(8L);
/*  898 */     localRIFFWriter2.writeUnsignedInt(this.samples.size());
/*  899 */     long l1 = paramRIFFWriter.getFilePointer();
/*  900 */     for (int i = 0; i < this.samples.size(); i++) {
/*  901 */       localRIFFWriter2.writeUnsignedInt(0L);
/*      */     }
/*  903 */     RIFFWriter localRIFFWriter3 = paramRIFFWriter.writeList("wvpl");
/*  904 */     long l2 = localRIFFWriter3.getFilePointer();
/*  905 */     ArrayList localArrayList = new ArrayList();
/*  906 */     for (DLSSample localDLSSample : this.samples) {
/*  907 */       localArrayList.add(Long.valueOf(localRIFFWriter3.getFilePointer() - l2));
/*  908 */       writeSample(localRIFFWriter3.writeList("wave"), localDLSSample);
/*      */     }
/*      */ 
/*  912 */     long l3 = paramRIFFWriter.getFilePointer();
/*  913 */     paramRIFFWriter.seek(l1);
/*  914 */     paramRIFFWriter.setWriteOverride(true);
/*  915 */     for (Long localLong : localArrayList)
/*  916 */       paramRIFFWriter.writeUnsignedInt(localLong.longValue());
/*  917 */     paramRIFFWriter.setWriteOverride(false);
/*  918 */     paramRIFFWriter.seek(l3);
/*      */ 
/*  920 */     writeInfo(paramRIFFWriter.writeList("INFO"), this.info);
/*      */ 
/*  922 */     paramRIFFWriter.close();
/*      */   }
/*      */ 
/*      */   private void writeSample(RIFFWriter paramRIFFWriter, DLSSample paramDLSSample)
/*      */     throws IOException
/*      */   {
/*  928 */     AudioFormat localAudioFormat = paramDLSSample.getFormat();
/*      */ 
/*  930 */     AudioFormat.Encoding localEncoding = localAudioFormat.getEncoding();
/*  931 */     float f1 = localAudioFormat.getSampleRate();
/*  932 */     int i = localAudioFormat.getSampleSizeInBits();
/*  933 */     int j = localAudioFormat.getChannels();
/*  934 */     int k = localAudioFormat.getFrameSize();
/*  935 */     float f2 = localAudioFormat.getFrameRate();
/*  936 */     boolean bool = localAudioFormat.isBigEndian();
/*      */ 
/*  938 */     int m = 0;
/*      */ 
/*  940 */     if (localAudioFormat.getSampleSizeInBits() == 8) {
/*  941 */       if (!localEncoding.equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
/*  942 */         localEncoding = AudioFormat.Encoding.PCM_UNSIGNED;
/*  943 */         m = 1;
/*      */       }
/*      */     } else {
/*  946 */       if (!localEncoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
/*  947 */         localEncoding = AudioFormat.Encoding.PCM_SIGNED;
/*  948 */         m = 1;
/*      */       }
/*  950 */       if (bool) {
/*  951 */         bool = false;
/*  952 */         m = 1;
/*      */       }
/*      */     }
/*      */ 
/*  956 */     if (m != 0) {
/*  957 */       localAudioFormat = new AudioFormat(localEncoding, f1, i, j, k, f2, bool);
/*      */     }
/*      */ 
/*  962 */     RIFFWriter localRIFFWriter1 = paramRIFFWriter.writeChunk("fmt ");
/*  963 */     int n = 0;
/*  964 */     if (localAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED))
/*  965 */       n = 1;
/*  966 */     else if (localAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED))
/*  967 */       n = 1;
/*  968 */     else if (localAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_FLOAT)) {
/*  969 */       n = 3;
/*      */     }
/*  971 */     localRIFFWriter1.writeUnsignedShort(n);
/*  972 */     localRIFFWriter1.writeUnsignedShort(localAudioFormat.getChannels());
/*  973 */     localRIFFWriter1.writeUnsignedInt(()localAudioFormat.getSampleRate());
/*  974 */     long l = ()localAudioFormat.getFrameRate() * localAudioFormat.getFrameSize();
/*  975 */     localRIFFWriter1.writeUnsignedInt(l);
/*  976 */     localRIFFWriter1.writeUnsignedShort(localAudioFormat.getFrameSize());
/*  977 */     localRIFFWriter1.writeUnsignedShort(localAudioFormat.getSampleSizeInBits());
/*  978 */     localRIFFWriter1.write(0);
/*  979 */     localRIFFWriter1.write(0);
/*      */ 
/*  981 */     writeSampleOptions(paramRIFFWriter.writeChunk("wsmp"), paramDLSSample.sampleoptions);
/*      */     RIFFWriter localRIFFWriter2;
/*      */     Object localObject;
/*  983 */     if (m != 0) {
/*  984 */       localRIFFWriter2 = paramRIFFWriter.writeChunk("data");
/*  985 */       localObject = AudioSystem.getAudioInputStream(localAudioFormat, (AudioInputStream)paramDLSSample.getData());
/*      */ 
/*  987 */       byte[] arrayOfByte = new byte[1024];
/*      */       int i1;
/*  989 */       while ((i1 = ((AudioInputStream)localObject).read(arrayOfByte)) != -1)
/*  990 */         localRIFFWriter2.write(arrayOfByte, 0, i1);
/*      */     }
/*      */     else {
/*  993 */       localRIFFWriter2 = paramRIFFWriter.writeChunk("data");
/*  994 */       localObject = paramDLSSample.getDataBuffer();
/*  995 */       ((ModelByteBuffer)localObject).writeTo(localRIFFWriter2);
/*      */     }
/*      */ 
/* 1003 */     writeInfo(paramRIFFWriter.writeList("INFO"), paramDLSSample.info);
/*      */   }
/*      */ 
/*      */   private void writeInstruments(RIFFWriter paramRIFFWriter) throws IOException {
/* 1007 */     for (DLSInstrument localDLSInstrument : this.instruments)
/* 1008 */       writeInstrument(paramRIFFWriter.writeList("ins "), localDLSInstrument);
/*      */   }
/*      */ 
/*      */   private void writeInstrument(RIFFWriter paramRIFFWriter, DLSInstrument paramDLSInstrument)
/*      */     throws IOException
/*      */   {
/* 1015 */     int i = 0;
/* 1016 */     int j = 0;
/* 1017 */     for (Iterator localIterator = paramDLSInstrument.getModulators().iterator(); localIterator.hasNext(); ) { localObject1 = (DLSModulator)localIterator.next();
/* 1018 */       if (((DLSModulator)localObject1).version == 1)
/* 1019 */         i++;
/* 1020 */       if (((DLSModulator)localObject1).version == 2)
/* 1021 */         j++;
/*      */     }
/* 1023 */     for (localIterator = paramDLSInstrument.regions.iterator(); localIterator.hasNext(); ) { localObject1 = (DLSRegion)localIterator.next();
/* 1024 */       for (localObject2 = ((DLSRegion)localObject1).getModulators().iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (DLSModulator)((Iterator)localObject2).next();
/* 1025 */         if (((DLSModulator)localObject3).version == 1)
/* 1026 */           i++;
/* 1027 */         if (((DLSModulator)localObject3).version == 2) {
/* 1028 */           j++;
/*      */         }
/*      */       }
/*      */     }
/* 1032 */     int k = 1;
/* 1033 */     if (j > 0) {
/* 1034 */       k = 2;
/*      */     }
/* 1036 */     Object localObject1 = paramRIFFWriter.writeChunk("insh");
/* 1037 */     ((RIFFWriter)localObject1).writeUnsignedInt(paramDLSInstrument.getRegions().size());
/* 1038 */     ((RIFFWriter)localObject1).writeUnsignedInt(paramDLSInstrument.bank + (paramDLSInstrument.druminstrument ? 2147483648L : 0L));
/*      */ 
/* 1040 */     ((RIFFWriter)localObject1).writeUnsignedInt(paramDLSInstrument.preset);
/*      */ 
/* 1042 */     Object localObject2 = paramRIFFWriter.writeList("lrgn");
/* 1043 */     for (Object localObject3 = paramDLSInstrument.regions.iterator(); ((Iterator)localObject3).hasNext(); ) { DLSRegion localDLSRegion = (DLSRegion)((Iterator)localObject3).next();
/* 1044 */       writeRegion((RIFFWriter)localObject2, localDLSRegion, k);
/*      */     }
/* 1046 */     writeArticulators(paramRIFFWriter, paramDLSInstrument.getModulators());
/*      */ 
/* 1048 */     writeInfo(paramRIFFWriter.writeList("INFO"), paramDLSInstrument.info);
/*      */   }
/*      */ 
/*      */   private void writeArticulators(RIFFWriter paramRIFFWriter, List<DLSModulator> paramList)
/*      */     throws IOException
/*      */   {
/* 1054 */     int i = 0;
/* 1055 */     int j = 0;
/* 1056 */     for (Object localObject1 = paramList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (DLSModulator)((Iterator)localObject1).next();
/* 1057 */       if (((DLSModulator)localObject2).version == 1)
/* 1058 */         i++;
/* 1059 */       if (((DLSModulator)localObject2).version == 2)
/* 1060 */         j++;
/*      */     }
/*      */     Object localObject2;
/*      */     Iterator localIterator;
/* 1062 */     if (i > 0) {
/* 1063 */       localObject1 = paramRIFFWriter.writeList("lart");
/* 1064 */       localObject2 = ((RIFFWriter)localObject1).writeChunk("art1");
/* 1065 */       ((RIFFWriter)localObject2).writeUnsignedInt(8L);
/* 1066 */       ((RIFFWriter)localObject2).writeUnsignedInt(i);
/* 1067 */       for (localIterator = paramList.iterator(); localIterator.hasNext(); ) { localDLSModulator = (DLSModulator)localIterator.next();
/* 1068 */         if (localDLSModulator.version == 1) {
/* 1069 */           ((RIFFWriter)localObject2).writeUnsignedShort(localDLSModulator.source);
/* 1070 */           ((RIFFWriter)localObject2).writeUnsignedShort(localDLSModulator.control);
/* 1071 */           ((RIFFWriter)localObject2).writeUnsignedShort(localDLSModulator.destination);
/* 1072 */           ((RIFFWriter)localObject2).writeUnsignedShort(localDLSModulator.transform);
/* 1073 */           ((RIFFWriter)localObject2).writeInt(localDLSModulator.scale);
/*      */         }
/*      */       }
/*      */     }
/*      */     DLSModulator localDLSModulator;
/* 1077 */     if (j > 0) {
/* 1078 */       localObject1 = paramRIFFWriter.writeList("lar2");
/* 1079 */       localObject2 = ((RIFFWriter)localObject1).writeChunk("art2");
/* 1080 */       ((RIFFWriter)localObject2).writeUnsignedInt(8L);
/* 1081 */       ((RIFFWriter)localObject2).writeUnsignedInt(j);
/* 1082 */       for (localIterator = paramList.iterator(); localIterator.hasNext(); ) { localDLSModulator = (DLSModulator)localIterator.next();
/* 1083 */         if (localDLSModulator.version == 2) {
/* 1084 */           ((RIFFWriter)localObject2).writeUnsignedShort(localDLSModulator.source);
/* 1085 */           ((RIFFWriter)localObject2).writeUnsignedShort(localDLSModulator.control);
/* 1086 */           ((RIFFWriter)localObject2).writeUnsignedShort(localDLSModulator.destination);
/* 1087 */           ((RIFFWriter)localObject2).writeUnsignedShort(localDLSModulator.transform);
/* 1088 */           ((RIFFWriter)localObject2).writeInt(localDLSModulator.scale);
/*      */         } }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeRegion(RIFFWriter paramRIFFWriter, DLSRegion paramDLSRegion, int paramInt)
/*      */     throws IOException
/*      */   {
/* 1096 */     RIFFWriter localRIFFWriter1 = null;
/* 1097 */     if (paramInt == 1)
/* 1098 */       localRIFFWriter1 = paramRIFFWriter.writeList("rgn ");
/* 1099 */     if (paramInt == 2)
/* 1100 */       localRIFFWriter1 = paramRIFFWriter.writeList("rgn2");
/* 1101 */     if (localRIFFWriter1 == null) {
/* 1102 */       return;
/*      */     }
/* 1104 */     RIFFWriter localRIFFWriter2 = localRIFFWriter1.writeChunk("rgnh");
/* 1105 */     localRIFFWriter2.writeUnsignedShort(paramDLSRegion.keyfrom);
/* 1106 */     localRIFFWriter2.writeUnsignedShort(paramDLSRegion.keyto);
/* 1107 */     localRIFFWriter2.writeUnsignedShort(paramDLSRegion.velfrom);
/* 1108 */     localRIFFWriter2.writeUnsignedShort(paramDLSRegion.velto);
/* 1109 */     localRIFFWriter2.writeUnsignedShort(paramDLSRegion.options);
/* 1110 */     localRIFFWriter2.writeUnsignedShort(paramDLSRegion.exclusiveClass);
/*      */ 
/* 1112 */     if (paramDLSRegion.sampleoptions != null) {
/* 1113 */       writeSampleOptions(localRIFFWriter1.writeChunk("wsmp"), paramDLSRegion.sampleoptions);
/*      */     }
/* 1115 */     if ((paramDLSRegion.sample != null) && 
/* 1116 */       (this.samples.indexOf(paramDLSRegion.sample) != -1)) {
/* 1117 */       RIFFWriter localRIFFWriter3 = localRIFFWriter1.writeChunk("wlnk");
/* 1118 */       localRIFFWriter3.writeUnsignedShort(paramDLSRegion.fusoptions);
/* 1119 */       localRIFFWriter3.writeUnsignedShort(paramDLSRegion.phasegroup);
/* 1120 */       localRIFFWriter3.writeUnsignedInt(paramDLSRegion.channel);
/* 1121 */       localRIFFWriter3.writeUnsignedInt(this.samples.indexOf(paramDLSRegion.sample));
/*      */     }
/*      */ 
/* 1124 */     writeArticulators(localRIFFWriter1, paramDLSRegion.getModulators());
/* 1125 */     localRIFFWriter1.close();
/*      */   }
/*      */ 
/*      */   private void writeSampleOptions(RIFFWriter paramRIFFWriter, DLSSampleOptions paramDLSSampleOptions) throws IOException
/*      */   {
/* 1130 */     paramRIFFWriter.writeUnsignedInt(20L);
/* 1131 */     paramRIFFWriter.writeUnsignedShort(paramDLSSampleOptions.unitynote);
/* 1132 */     paramRIFFWriter.writeShort(paramDLSSampleOptions.finetune);
/* 1133 */     paramRIFFWriter.writeInt(paramDLSSampleOptions.attenuation);
/* 1134 */     paramRIFFWriter.writeUnsignedInt(paramDLSSampleOptions.options);
/* 1135 */     paramRIFFWriter.writeInt(paramDLSSampleOptions.loops.size());
/*      */ 
/* 1137 */     for (DLSSampleLoop localDLSSampleLoop : paramDLSSampleOptions.loops) {
/* 1138 */       paramRIFFWriter.writeUnsignedInt(16L);
/* 1139 */       paramRIFFWriter.writeUnsignedInt(localDLSSampleLoop.type);
/* 1140 */       paramRIFFWriter.writeUnsignedInt(localDLSSampleLoop.start);
/* 1141 */       paramRIFFWriter.writeUnsignedInt(localDLSSampleLoop.length);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeInfoStringChunk(RIFFWriter paramRIFFWriter, String paramString1, String paramString2) throws IOException
/*      */   {
/* 1147 */     if (paramString2 == null)
/* 1148 */       return;
/* 1149 */     RIFFWriter localRIFFWriter = paramRIFFWriter.writeChunk(paramString1);
/* 1150 */     localRIFFWriter.writeString(paramString2);
/* 1151 */     int i = paramString2.getBytes("ascii").length;
/* 1152 */     localRIFFWriter.write(0);
/* 1153 */     i++;
/* 1154 */     if (i % 2 != 0)
/* 1155 */       localRIFFWriter.write(0);
/*      */   }
/*      */ 
/*      */   private void writeInfo(RIFFWriter paramRIFFWriter, DLSInfo paramDLSInfo) throws IOException {
/* 1159 */     writeInfoStringChunk(paramRIFFWriter, "INAM", paramDLSInfo.name);
/* 1160 */     writeInfoStringChunk(paramRIFFWriter, "ICRD", paramDLSInfo.creationDate);
/* 1161 */     writeInfoStringChunk(paramRIFFWriter, "IENG", paramDLSInfo.engineers);
/* 1162 */     writeInfoStringChunk(paramRIFFWriter, "IPRD", paramDLSInfo.product);
/* 1163 */     writeInfoStringChunk(paramRIFFWriter, "ICOP", paramDLSInfo.copyright);
/* 1164 */     writeInfoStringChunk(paramRIFFWriter, "ICMT", paramDLSInfo.comments);
/* 1165 */     writeInfoStringChunk(paramRIFFWriter, "ISFT", paramDLSInfo.tools);
/* 1166 */     writeInfoStringChunk(paramRIFFWriter, "IARL", paramDLSInfo.archival_location);
/* 1167 */     writeInfoStringChunk(paramRIFFWriter, "IART", paramDLSInfo.artist);
/* 1168 */     writeInfoStringChunk(paramRIFFWriter, "ICMS", paramDLSInfo.commissioned);
/* 1169 */     writeInfoStringChunk(paramRIFFWriter, "IGNR", paramDLSInfo.genre);
/* 1170 */     writeInfoStringChunk(paramRIFFWriter, "IKEY", paramDLSInfo.keywords);
/* 1171 */     writeInfoStringChunk(paramRIFFWriter, "IMED", paramDLSInfo.medium);
/* 1172 */     writeInfoStringChunk(paramRIFFWriter, "ISBJ", paramDLSInfo.subject);
/* 1173 */     writeInfoStringChunk(paramRIFFWriter, "ISRC", paramDLSInfo.source);
/* 1174 */     writeInfoStringChunk(paramRIFFWriter, "ISRF", paramDLSInfo.source_form);
/* 1175 */     writeInfoStringChunk(paramRIFFWriter, "ITCH", paramDLSInfo.technician);
/*      */   }
/*      */ 
/*      */   public DLSInfo getInfo() {
/* 1179 */     return this.info;
/*      */   }
/*      */ 
/*      */   public String getName() {
/* 1183 */     return this.info.name;
/*      */   }
/*      */ 
/*      */   public String getVersion() {
/* 1187 */     return this.major + "." + this.minor;
/*      */   }
/*      */ 
/*      */   public String getVendor() {
/* 1191 */     return this.info.engineers;
/*      */   }
/*      */ 
/*      */   public String getDescription() {
/* 1195 */     return this.info.comments;
/*      */   }
/*      */ 
/*      */   public void setName(String paramString) {
/* 1199 */     this.info.name = paramString;
/*      */   }
/*      */ 
/*      */   public void setVendor(String paramString) {
/* 1203 */     this.info.engineers = paramString;
/*      */   }
/*      */ 
/*      */   public void setDescription(String paramString) {
/* 1207 */     this.info.comments = paramString;
/*      */   }
/*      */ 
/*      */   public SoundbankResource[] getResources() {
/* 1211 */     SoundbankResource[] arrayOfSoundbankResource = new SoundbankResource[this.samples.size()];
/* 1212 */     int i = 0;
/* 1213 */     for (int j = 0; j < this.samples.size(); j++)
/* 1214 */       arrayOfSoundbankResource[(i++)] = ((SoundbankResource)this.samples.get(j));
/* 1215 */     return arrayOfSoundbankResource;
/*      */   }
/*      */ 
/*      */   public DLSInstrument[] getInstruments() {
/* 1219 */     DLSInstrument[] arrayOfDLSInstrument = (DLSInstrument[])this.instruments.toArray(new DLSInstrument[this.instruments.size()]);
/*      */ 
/* 1221 */     Arrays.sort(arrayOfDLSInstrument, new ModelInstrumentComparator());
/* 1222 */     return arrayOfDLSInstrument;
/*      */   }
/*      */ 
/*      */   public DLSSample[] getSamples() {
/* 1226 */     return (DLSSample[])this.samples.toArray(new DLSSample[this.samples.size()]);
/*      */   }
/*      */ 
/*      */   public Instrument getInstrument(Patch paramPatch) {
/* 1230 */     int i = paramPatch.getProgram();
/* 1231 */     int j = paramPatch.getBank();
/* 1232 */     boolean bool1 = false;
/* 1233 */     if ((paramPatch instanceof ModelPatch))
/* 1234 */       bool1 = ((ModelPatch)paramPatch).isPercussion();
/* 1235 */     for (Instrument localInstrument : this.instruments) {
/* 1236 */       Patch localPatch = localInstrument.getPatch();
/* 1237 */       int k = localPatch.getProgram();
/* 1238 */       int m = localPatch.getBank();
/* 1239 */       if ((i == k) && (j == m)) {
/* 1240 */         boolean bool2 = false;
/* 1241 */         if ((localPatch instanceof ModelPatch))
/* 1242 */           bool2 = ((ModelPatch)localPatch).isPercussion();
/* 1243 */         if (bool1 == bool2)
/* 1244 */           return localInstrument;
/*      */       }
/*      */     }
/* 1247 */     return null;
/*      */   }
/*      */ 
/*      */   public void addResource(SoundbankResource paramSoundbankResource) {
/* 1251 */     if ((paramSoundbankResource instanceof DLSInstrument))
/* 1252 */       this.instruments.add((DLSInstrument)paramSoundbankResource);
/* 1253 */     if ((paramSoundbankResource instanceof DLSSample))
/* 1254 */       this.samples.add((DLSSample)paramSoundbankResource);
/*      */   }
/*      */ 
/*      */   public void removeResource(SoundbankResource paramSoundbankResource) {
/* 1258 */     if ((paramSoundbankResource instanceof DLSInstrument))
/* 1259 */       this.instruments.remove((DLSInstrument)paramSoundbankResource);
/* 1260 */     if ((paramSoundbankResource instanceof DLSSample))
/* 1261 */       this.samples.remove((DLSSample)paramSoundbankResource);
/*      */   }
/*      */ 
/*      */   public void addInstrument(DLSInstrument paramDLSInstrument) {
/* 1265 */     this.instruments.add(paramDLSInstrument);
/*      */   }
/*      */ 
/*      */   public void removeInstrument(DLSInstrument paramDLSInstrument) {
/* 1269 */     this.instruments.remove(paramDLSInstrument);
/*      */   }
/*      */ 
/*      */   public long getMajor() {
/* 1273 */     return this.major;
/*      */   }
/*      */ 
/*      */   public void setMajor(long paramLong) {
/* 1277 */     this.major = paramLong;
/*      */   }
/*      */ 
/*      */   public long getMinor() {
/* 1281 */     return this.minor;
/*      */   }
/*      */ 
/*      */   public void setMinor(long paramLong) {
/* 1285 */     this.minor = paramLong;
/*      */   }
/*      */ 
/*      */   private static class DLSID
/*      */   {
/*      */     long i1;
/*      */     int s1;
/*      */     int s2;
/*      */     int x1;
/*      */     int x2;
/*      */     int x3;
/*      */     int x4;
/*      */     int x5;
/*      */     int x6;
/*      */     int x7;
/*      */     int x8;
/*      */ 
/*      */     private DLSID()
/*      */     {
/*      */     }
/*      */ 
/*      */     DLSID(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10)
/*      */     {
/*   74 */       this.i1 = paramLong;
/*   75 */       this.s1 = paramInt1;
/*   76 */       this.s2 = paramInt2;
/*   77 */       this.x1 = paramInt3;
/*   78 */       this.x2 = paramInt4;
/*   79 */       this.x3 = paramInt5;
/*   80 */       this.x4 = paramInt6;
/*   81 */       this.x5 = paramInt7;
/*   82 */       this.x6 = paramInt8;
/*   83 */       this.x7 = paramInt9;
/*   84 */       this.x8 = paramInt10;
/*      */     }
/*      */ 
/*      */     public static DLSID read(RIFFReader paramRIFFReader) throws IOException {
/*   88 */       DLSID localDLSID = new DLSID();
/*   89 */       localDLSID.i1 = paramRIFFReader.readUnsignedInt();
/*   90 */       localDLSID.s1 = paramRIFFReader.readUnsignedShort();
/*   91 */       localDLSID.s2 = paramRIFFReader.readUnsignedShort();
/*   92 */       localDLSID.x1 = paramRIFFReader.readUnsignedByte();
/*   93 */       localDLSID.x2 = paramRIFFReader.readUnsignedByte();
/*   94 */       localDLSID.x3 = paramRIFFReader.readUnsignedByte();
/*   95 */       localDLSID.x4 = paramRIFFReader.readUnsignedByte();
/*   96 */       localDLSID.x5 = paramRIFFReader.readUnsignedByte();
/*   97 */       localDLSID.x6 = paramRIFFReader.readUnsignedByte();
/*   98 */       localDLSID.x7 = paramRIFFReader.readUnsignedByte();
/*   99 */       localDLSID.x8 = paramRIFFReader.readUnsignedByte();
/*  100 */       return localDLSID;
/*      */     }
/*      */ 
/*      */     public int hashCode() {
/*  104 */       return (int)this.i1;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/*  108 */       if (!(paramObject instanceof DLSID)) {
/*  109 */         return false;
/*      */       }
/*  111 */       DLSID localDLSID = (DLSID)paramObject;
/*  112 */       return (this.i1 == localDLSID.i1) && (this.s1 == localDLSID.s1) && (this.s2 == localDLSID.s2) && (this.x1 == localDLSID.x1) && (this.x2 == localDLSID.x2) && (this.x3 == localDLSID.x3) && (this.x4 == localDLSID.x4) && (this.x5 == localDLSID.x5) && (this.x6 == localDLSID.x6) && (this.x7 == localDLSID.x7) && (this.x8 == localDLSID.x8);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.DLSSoundbank
 * JD-Core Version:    0.6.2
 */