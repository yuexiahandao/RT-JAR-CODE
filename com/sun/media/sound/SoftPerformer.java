/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class SoftPerformer
/*     */ {
/*  42 */   static ModelConnectionBlock[] defaultconnections = new ModelConnectionBlock[42];
/*     */ 
/* 301 */   public int keyFrom = 0;
/* 302 */   public int keyTo = 127;
/* 303 */   public int velFrom = 0;
/* 304 */   public int velTo = 127;
/* 305 */   public int exclusiveClass = 0;
/* 306 */   public boolean selfNonExclusive = false;
/* 307 */   public boolean forcedVelocity = false;
/* 308 */   public boolean forcedKeynumber = false;
/*     */   public ModelPerformer performer;
/*     */   public ModelConnectionBlock[] connections;
/*     */   public ModelOscillator[] oscillators;
/* 312 */   public Map<Integer, int[]> midi_rpn_connections = new HashMap();
/* 313 */   public Map<Integer, int[]> midi_nrpn_connections = new HashMap();
/*     */   public int[][] midi_ctrl_connections;
/*     */   public int[][] midi_connections;
/*     */   public int[] ctrl_connections;
/* 317 */   private List<Integer> ctrl_connections_list = new ArrayList();
/*     */ 
/* 326 */   private static KeySortComparator keySortComparator = new KeySortComparator(null);
/*     */ 
/*     */   private String extractKeys(ModelConnectionBlock paramModelConnectionBlock) {
/* 329 */     StringBuffer localStringBuffer = new StringBuffer();
/* 330 */     if (paramModelConnectionBlock.getSources() != null) {
/* 331 */       localStringBuffer.append("[");
/* 332 */       ModelSource[] arrayOfModelSource1 = paramModelConnectionBlock.getSources();
/* 333 */       ModelSource[] arrayOfModelSource2 = new ModelSource[arrayOfModelSource1.length];
/* 334 */       for (int i = 0; i < arrayOfModelSource1.length; i++)
/* 335 */         arrayOfModelSource2[i] = arrayOfModelSource1[i];
/* 336 */       Arrays.sort(arrayOfModelSource2, keySortComparator);
/* 337 */       for (i = 0; i < arrayOfModelSource1.length; i++) {
/* 338 */         localStringBuffer.append(arrayOfModelSource1[i].getIdentifier());
/* 339 */         localStringBuffer.append(";");
/*     */       }
/* 341 */       localStringBuffer.append("]");
/*     */     }
/* 343 */     localStringBuffer.append(";");
/* 344 */     if (paramModelConnectionBlock.getDestination() != null) {
/* 345 */       localStringBuffer.append(paramModelConnectionBlock.getDestination().getIdentifier());
/*     */     }
/* 347 */     localStringBuffer.append(";");
/* 348 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private void processSource(ModelSource paramModelSource, int paramInt) {
/* 352 */     ModelIdentifier localModelIdentifier = paramModelSource.getIdentifier();
/* 353 */     String str = localModelIdentifier.getObject();
/* 354 */     if (str.equals("midi_cc")) {
/* 355 */       processMidiControlSource(paramModelSource, paramInt);
/* 356 */     } else if (str.equals("midi_rpn")) {
/* 357 */       processMidiRpnSource(paramModelSource, paramInt);
/* 358 */     } else if (str.equals("midi_nrpn")) {
/* 359 */       processMidiNrpnSource(paramModelSource, paramInt);
/* 360 */     } else if (str.equals("midi")) {
/* 361 */       processMidiSource(paramModelSource, paramInt);
/* 362 */     } else if (str.equals("noteon")) {
/* 363 */       processNoteOnSource(paramModelSource, paramInt); } else {
/* 364 */       if (str.equals("osc"))
/* 365 */         return;
/* 366 */       if (str.equals("mixer")) {
/* 367 */         return;
/*     */       }
/* 369 */       this.ctrl_connections_list.add(Integer.valueOf(paramInt));
/*     */     }
/*     */   }
/*     */ 
/* 373 */   private void processMidiControlSource(ModelSource paramModelSource, int paramInt) { String str = paramModelSource.getIdentifier().getVariable();
/* 374 */     if (str == null)
/* 375 */       return;
/* 376 */     int i = Integer.parseInt(str);
/* 377 */     if (this.midi_ctrl_connections[i] == null) {
/* 378 */       this.midi_ctrl_connections[i] = { paramInt };
/*     */     } else {
/* 380 */       int[] arrayOfInt1 = this.midi_ctrl_connections[i];
/* 381 */       int[] arrayOfInt2 = new int[arrayOfInt1.length + 1];
/* 382 */       for (int j = 0; j < arrayOfInt1.length; j++)
/* 383 */         arrayOfInt2[j] = arrayOfInt1[j];
/* 384 */       arrayOfInt2[(arrayOfInt2.length - 1)] = paramInt;
/* 385 */       this.midi_ctrl_connections[i] = arrayOfInt2;
/*     */     } }
/*     */ 
/*     */   private void processNoteOnSource(ModelSource paramModelSource, int paramInt)
/*     */   {
/* 390 */     String str = paramModelSource.getIdentifier().getVariable();
/* 391 */     int i = -1;
/* 392 */     if (str.equals("on"))
/* 393 */       i = 3;
/* 394 */     if (str.equals("keynumber"))
/* 395 */       i = 4;
/* 396 */     if (i == -1)
/* 397 */       return;
/* 398 */     if (this.midi_connections[i] == null) {
/* 399 */       this.midi_connections[i] = { paramInt };
/*     */     } else {
/* 401 */       int[] arrayOfInt1 = this.midi_connections[i];
/* 402 */       int[] arrayOfInt2 = new int[arrayOfInt1.length + 1];
/* 403 */       for (int j = 0; j < arrayOfInt1.length; j++)
/* 404 */         arrayOfInt2[j] = arrayOfInt1[j];
/* 405 */       arrayOfInt2[(arrayOfInt2.length - 1)] = paramInt;
/* 406 */       this.midi_connections[i] = arrayOfInt2;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void processMidiSource(ModelSource paramModelSource, int paramInt) {
/* 411 */     String str = paramModelSource.getIdentifier().getVariable();
/* 412 */     int i = -1;
/* 413 */     if (str.equals("pitch"))
/* 414 */       i = 0;
/* 415 */     if (str.equals("channel_pressure"))
/* 416 */       i = 1;
/* 417 */     if (str.equals("poly_pressure"))
/* 418 */       i = 2;
/* 419 */     if (i == -1)
/* 420 */       return;
/* 421 */     if (this.midi_connections[i] == null) {
/* 422 */       this.midi_connections[i] = { paramInt };
/*     */     } else {
/* 424 */       int[] arrayOfInt1 = this.midi_connections[i];
/* 425 */       int[] arrayOfInt2 = new int[arrayOfInt1.length + 1];
/* 426 */       for (int j = 0; j < arrayOfInt1.length; j++)
/* 427 */         arrayOfInt2[j] = arrayOfInt1[j];
/* 428 */       arrayOfInt2[(arrayOfInt2.length - 1)] = paramInt;
/* 429 */       this.midi_connections[i] = arrayOfInt2;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void processMidiRpnSource(ModelSource paramModelSource, int paramInt) {
/* 434 */     String str = paramModelSource.getIdentifier().getVariable();
/* 435 */     if (str == null)
/* 436 */       return;
/* 437 */     int i = Integer.parseInt(str);
/* 438 */     if (this.midi_rpn_connections.get(Integer.valueOf(i)) == null) {
/* 439 */       this.midi_rpn_connections.put(Integer.valueOf(i), new int[] { paramInt });
/*     */     } else {
/* 441 */       int[] arrayOfInt1 = (int[])this.midi_rpn_connections.get(Integer.valueOf(i));
/* 442 */       int[] arrayOfInt2 = new int[arrayOfInt1.length + 1];
/* 443 */       for (int j = 0; j < arrayOfInt1.length; j++)
/* 444 */         arrayOfInt2[j] = arrayOfInt1[j];
/* 445 */       arrayOfInt2[(arrayOfInt2.length - 1)] = paramInt;
/* 446 */       this.midi_rpn_connections.put(Integer.valueOf(i), arrayOfInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void processMidiNrpnSource(ModelSource paramModelSource, int paramInt) {
/* 451 */     String str = paramModelSource.getIdentifier().getVariable();
/* 452 */     if (str == null)
/* 453 */       return;
/* 454 */     int i = Integer.parseInt(str);
/* 455 */     if (this.midi_nrpn_connections.get(Integer.valueOf(i)) == null) {
/* 456 */       this.midi_nrpn_connections.put(Integer.valueOf(i), new int[] { paramInt });
/*     */     } else {
/* 458 */       int[] arrayOfInt1 = (int[])this.midi_nrpn_connections.get(Integer.valueOf(i));
/* 459 */       int[] arrayOfInt2 = new int[arrayOfInt1.length + 1];
/* 460 */       for (int j = 0; j < arrayOfInt1.length; j++)
/* 461 */         arrayOfInt2[j] = arrayOfInt1[j];
/* 462 */       arrayOfInt2[(arrayOfInt2.length - 1)] = paramInt;
/* 463 */       this.midi_nrpn_connections.put(Integer.valueOf(i), arrayOfInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SoftPerformer(ModelPerformer paramModelPerformer) {
/* 468 */     this.performer = paramModelPerformer;
/*     */ 
/* 470 */     this.keyFrom = paramModelPerformer.getKeyFrom();
/* 471 */     this.keyTo = paramModelPerformer.getKeyTo();
/* 472 */     this.velFrom = paramModelPerformer.getVelFrom();
/* 473 */     this.velTo = paramModelPerformer.getVelTo();
/* 474 */     this.exclusiveClass = paramModelPerformer.getExclusiveClass();
/* 475 */     this.selfNonExclusive = paramModelPerformer.isSelfNonExclusive();
/*     */ 
/* 477 */     HashMap localHashMap = new HashMap();
/*     */ 
/* 479 */     ArrayList localArrayList = new ArrayList();
/* 480 */     localArrayList.addAll(paramModelPerformer.getConnectionBlocks());
/*     */     int k;
/*     */     Object localObject3;
/* 482 */     if (paramModelPerformer.isDefaultConnectionsEnabled())
/*     */     {
/* 486 */       int i = 0;
/* 487 */       for (int j = 0; j < localArrayList.size(); j++) {
/* 488 */         ModelConnectionBlock localModelConnectionBlock3 = (ModelConnectionBlock)localArrayList.get(j);
/* 489 */         localObject2 = localModelConnectionBlock3.getSources();
/* 490 */         ModelDestination localModelDestination1 = localModelConnectionBlock3.getDestination();
/* 491 */         int i3 = 0;
/* 492 */         if ((localModelDestination1 != null) && (localObject2 != null) && (localObject2.length > 1)) {
/* 493 */           for (int i4 = 0; i4 < localObject2.length; i4++)
/*     */           {
/* 496 */             if (localObject2[i4].getIdentifier().getObject().equals("midi_cc"))
/*     */             {
/* 498 */               if (localObject2[i4].getIdentifier().getVariable().equals("1"))
/*     */               {
/* 500 */                 i3 = 1;
/* 501 */                 i = 1;
/* 502 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 507 */         if (i3 != 0)
/*     */         {
/* 509 */           localObject4 = new ModelConnectionBlock();
/* 510 */           ((ModelConnectionBlock)localObject4).setSources(localModelConnectionBlock3.getSources());
/* 511 */           ((ModelConnectionBlock)localObject4).setDestination(localModelConnectionBlock3.getDestination());
/* 512 */           ((ModelConnectionBlock)localObject4).addSource(new ModelSource(new ModelIdentifier("midi_rpn", "5")));
/*     */ 
/* 514 */           ((ModelConnectionBlock)localObject4).setScale(localModelConnectionBlock3.getScale() * 256.0D);
/* 515 */           localArrayList.set(j, localObject4);
/*     */         }
/*     */       }
/*     */ 
/* 519 */       if (i == 0) {
/* 520 */         ModelConnectionBlock localModelConnectionBlock1 = new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_LFO1, false, true, 0), new ModelSource(new ModelIdentifier("midi_cc", "1", 0), false, false, 0), 50.0D, new ModelDestination(ModelDestination.DESTINATION_PITCH));
/*     */ 
/* 531 */         localModelConnectionBlock1.addSource(new ModelSource(new ModelIdentifier("midi_rpn", "5")));
/*     */ 
/* 533 */         localModelConnectionBlock1.setScale(localModelConnectionBlock1.getScale() * 256.0D);
/* 534 */         localArrayList.add(localModelConnectionBlock1);
/*     */       }
/*     */ 
/* 539 */       k = 0;
/* 540 */       n = 0;
/* 541 */       localObject2 = null;
/* 542 */       int i2 = 0;
/*     */ 
/* 544 */       for (localObject3 = localArrayList.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (ModelConnectionBlock)((Iterator)localObject3).next();
/* 545 */         localObject5 = ((ModelConnectionBlock)localObject4).getSources();
/* 546 */         ModelDestination localModelDestination2 = ((ModelConnectionBlock)localObject4).getDestination();
/*     */ 
/* 548 */         if ((localModelDestination2 != null) && (localObject5 != null)) {
/* 549 */           for (int i9 = 0; i9 < localObject5.length; i9++) {
/* 550 */             ModelIdentifier localModelIdentifier2 = localObject5[i9].getIdentifier();
/*     */ 
/* 553 */             if ((localModelIdentifier2.getObject().equals("midi_cc")) && 
/* 554 */               (localModelIdentifier2.getVariable().equals("1"))) {
/* 555 */               localObject2 = localObject4;
/* 556 */               i2 = i9;
/*     */             }
/*     */ 
/* 561 */             if (localModelIdentifier2.getObject().equals("midi")) {
/* 562 */               if (localModelIdentifier2.getVariable().equals("channel_pressure"))
/* 563 */                 k = 1;
/* 564 */               if (localModelIdentifier2.getVariable().equals("poly_pressure")) {
/* 565 */                 n = 1;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 572 */       if (localObject2 != null)
/*     */       {
/*     */         int i8;
/* 573 */         if (k == 0) {
/* 574 */           localObject3 = new ModelConnectionBlock();
/* 575 */           ((ModelConnectionBlock)localObject3).setDestination(((ModelConnectionBlock)localObject2).getDestination());
/* 576 */           ((ModelConnectionBlock)localObject3).setScale(((ModelConnectionBlock)localObject2).getScale());
/* 577 */           localObject4 = ((ModelConnectionBlock)localObject2).getSources();
/* 578 */           localObject5 = new ModelSource[localObject4.length];
/* 579 */           for (i8 = 0; i8 < localObject5.length; i8++)
/* 580 */             localObject5[i8] = localObject4[i8];
/* 581 */           localObject5[i2] = new ModelSource(new ModelIdentifier("midi", "channel_pressure"));
/*     */ 
/* 583 */           ((ModelConnectionBlock)localObject3).setSources((ModelSource[])localObject5);
/* 584 */           localHashMap.put(extractKeys((ModelConnectionBlock)localObject3), localObject3);
/*     */         }
/* 586 */         if (n == 0) {
/* 587 */           localObject3 = new ModelConnectionBlock();
/* 588 */           ((ModelConnectionBlock)localObject3).setDestination(((ModelConnectionBlock)localObject2).getDestination());
/* 589 */           ((ModelConnectionBlock)localObject3).setScale(((ModelConnectionBlock)localObject2).getScale());
/* 590 */           localObject4 = ((ModelConnectionBlock)localObject2).getSources();
/* 591 */           localObject5 = new ModelSource[localObject4.length];
/* 592 */           for (i8 = 0; i8 < localObject5.length; i8++)
/* 593 */             localObject5[i8] = localObject4[i8];
/* 594 */           localObject5[i2] = new ModelSource(new ModelIdentifier("midi", "poly_pressure"));
/*     */ 
/* 596 */           ((ModelConnectionBlock)localObject3).setSources((ModelSource[])localObject5);
/* 597 */           localHashMap.put(extractKeys((ModelConnectionBlock)localObject3), localObject3);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 602 */       localObject3 = null;
/* 603 */       for (Object localObject4 = localArrayList.iterator(); ((Iterator)localObject4).hasNext(); ) { localObject5 = (ModelConnectionBlock)((Iterator)localObject4).next();
/* 604 */         ModelSource[] arrayOfModelSource = ((ModelConnectionBlock)localObject5).getSources();
/* 605 */         if ((arrayOfModelSource.length != 0) && (arrayOfModelSource[0].getIdentifier().getObject().equals("lfo")))
/*     */         {
/* 607 */           if (((ModelConnectionBlock)localObject5).getDestination().getIdentifier().equals(ModelDestination.DESTINATION_PITCH))
/*     */           {
/* 609 */             if (localObject3 == null) {
/* 610 */               localObject3 = localObject5;
/*     */             }
/* 612 */             else if (((ModelConnectionBlock)localObject3).getSources().length > arrayOfModelSource.length)
/* 613 */               localObject3 = localObject5;
/* 614 */             else if (localObject3.getSources()[0].getIdentifier().getInstance() < 1)
/*     */             {
/* 616 */               if (localObject3.getSources()[0].getIdentifier().getInstance() > arrayOfModelSource[0].getIdentifier().getInstance())
/*     */               {
/* 619 */                 localObject3 = localObject5;
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 628 */       int i5 = 1;
/*     */ 
/* 630 */       if (localObject3 != null) {
/* 631 */         i5 = localObject3.getSources()[0].getIdentifier().getInstance();
/*     */       }
/*     */ 
/* 636 */       Object localObject5 = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "78"), false, true, 0), 2000.0D, new ModelDestination(new ModelIdentifier("lfo", "delay2", i5)));
/*     */ 
/* 643 */       localHashMap.put(extractKeys((ModelConnectionBlock)localObject5), localObject5);
/*     */ 
/* 645 */       final double d = localObject3 == null ? 0.0D : ((ModelConnectionBlock)localObject3).getScale();
/*     */ 
/* 647 */       localObject5 = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("lfo", i5)), new ModelSource(new ModelIdentifier("midi_cc", "77"), new ModelTransform()
/*     */       {
/* 651 */         double s = d;
/*     */ 
/* 653 */         public double transform(double paramAnonymousDouble) { paramAnonymousDouble = paramAnonymousDouble * 2.0D - 1.0D;
/* 654 */           paramAnonymousDouble *= 600.0D;
/* 655 */           if (this.s == 0.0D)
/* 656 */             return paramAnonymousDouble;
/* 657 */           if (this.s > 0.0D) {
/* 658 */             if (paramAnonymousDouble < -this.s)
/* 659 */               paramAnonymousDouble = -this.s;
/* 660 */             return paramAnonymousDouble;
/*     */           }
/* 662 */           if (paramAnonymousDouble < this.s)
/* 663 */             paramAnonymousDouble = -this.s;
/* 664 */           return -paramAnonymousDouble;
/*     */         }
/*     */       }), new ModelDestination(ModelDestination.DESTINATION_PITCH));
/*     */ 
/* 668 */       localHashMap.put(extractKeys((ModelConnectionBlock)localObject5), localObject5);
/*     */ 
/* 670 */       localObject5 = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "76"), false, true, 0), 2400.0D, new ModelDestination(new ModelIdentifier("lfo", "freq", i5)));
/*     */ 
/* 677 */       localHashMap.put(extractKeys((ModelConnectionBlock)localObject5), localObject5);
/*     */     }
/*     */ 
/* 682 */     if (paramModelPerformer.isDefaultConnectionsEnabled()) {
/* 683 */       for (localObject2 : defaultconnections)
/* 684 */         localHashMap.put(extractKeys((ModelConnectionBlock)localObject2), localObject2);
/*     */     }
/* 686 */     for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) { ModelConnectionBlock localModelConnectionBlock2 = (ModelConnectionBlock)((Iterator)???).next();
/* 687 */       localHashMap.put(extractKeys(localModelConnectionBlock2), localModelConnectionBlock2);
/*     */     }
/*     */ 
/* 690 */     ??? = new ArrayList();
/*     */ 
/* 692 */     this.midi_ctrl_connections = new int[''][];
/* 693 */     for (int m = 0; m < this.midi_ctrl_connections.length; m++) {
/* 694 */       this.midi_ctrl_connections[m] = null;
/*     */     }
/* 696 */     this.midi_connections = new int[5][];
/* 697 */     for (m = 0; m < this.midi_connections.length; m++) {
/* 698 */       this.midi_connections[m] = null;
/*     */     }
/*     */ 
/* 701 */     m = 0;
/* 702 */     int n = 0;
/*     */ 
/* 704 */     for (Object localObject2 = localHashMap.values().iterator(); ((Iterator)localObject2).hasNext(); ) { localModelConnectionBlock4 = (ModelConnectionBlock)((Iterator)localObject2).next();
/* 705 */       if (localModelConnectionBlock4.getDestination() != null) {
/* 706 */         localObject3 = localModelConnectionBlock4.getDestination();
/* 707 */         ModelIdentifier localModelIdentifier1 = ((ModelDestination)localObject3).getIdentifier();
/* 708 */         if (localModelIdentifier1.getObject().equals("noteon")) {
/* 709 */           n = 1;
/* 710 */           if (localModelIdentifier1.getVariable().equals("keynumber"))
/* 711 */             this.forcedKeynumber = true;
/* 712 */           if (localModelIdentifier1.getVariable().equals("velocity"))
/* 713 */             this.forcedVelocity = true;
/*     */         }
/*     */       }
/* 716 */       if (n != 0) {
/* 717 */         ((List)???).add(0, localModelConnectionBlock4);
/* 718 */         n = 0;
/*     */       } else {
/* 720 */         ((List)???).add(localModelConnectionBlock4);
/*     */       }
/*     */     }
/* 723 */     ModelConnectionBlock localModelConnectionBlock4;
/* 723 */     for (localObject2 = ((List)???).iterator(); ((Iterator)localObject2).hasNext(); ) { localModelConnectionBlock4 = (ModelConnectionBlock)((Iterator)localObject2).next();
/* 724 */       if (localModelConnectionBlock4.getSources() != null) {
/* 725 */         localObject3 = localModelConnectionBlock4.getSources();
/* 726 */         for (i6 = 0; i6 < localObject3.length; i6++) {
/* 727 */           processSource(localObject3[i6], m);
/*     */         }
/*     */       }
/* 730 */       m++;
/*     */     }
/*     */     int i6;
/* 733 */     this.connections = new ModelConnectionBlock[((List)???).size()];
/* 734 */     ((List)???).toArray(this.connections);
/*     */ 
/* 736 */     this.ctrl_connections = new int[this.ctrl_connections_list.size()];
/*     */ 
/* 738 */     for (int i1 = 0; i1 < this.ctrl_connections.length; i1++) {
/* 739 */       this.ctrl_connections[i1] = ((Integer)this.ctrl_connections_list.get(i1)).intValue();
/*     */     }
/* 741 */     this.oscillators = new ModelOscillator[paramModelPerformer.getOscillators().size()];
/* 742 */     paramModelPerformer.getOscillators().toArray(this.oscillators);
/*     */ 
/* 744 */     for (Iterator localIterator = ((List)???).iterator(); localIterator.hasNext(); ) { localModelConnectionBlock4 = (ModelConnectionBlock)localIterator.next();
/* 745 */       if ((localModelConnectionBlock4.getDestination() != null) && 
/* 746 */         (isUnnecessaryTransform(localModelConnectionBlock4.getDestination().getTransform()))) {
/* 747 */         localModelConnectionBlock4.getDestination().setTransform(null);
/*     */       }
/*     */ 
/* 750 */       if (localModelConnectionBlock4.getSources() != null)
/* 751 */         for (Object localObject6 : localModelConnectionBlock4.getSources())
/* 752 */           if (isUnnecessaryTransform(localObject6.getTransform()))
/* 753 */             localObject6.setTransform(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isUnnecessaryTransform(ModelTransform paramModelTransform)
/*     */   {
/* 762 */     if (paramModelTransform == null)
/* 763 */       return false;
/* 764 */     if (!(paramModelTransform instanceof ModelStandardTransform))
/* 765 */       return false;
/* 766 */     ModelStandardTransform localModelStandardTransform = (ModelStandardTransform)paramModelTransform;
/* 767 */     if (localModelStandardTransform.getDirection())
/* 768 */       return false;
/* 769 */     if (localModelStandardTransform.getPolarity())
/* 770 */       return false;
/* 771 */     if (localModelStandardTransform.getTransform() != 0)
/* 772 */       return false;
/* 773 */     return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  46 */     int i = 0;
/*  47 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("noteon", "on", 0), false, false, 0), 1.0D, new ModelDestination(new ModelIdentifier("eg", "on", 0)));
/*     */ 
/*  55 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("noteon", "on", 0), false, false, 0), 1.0D, new ModelDestination(new ModelIdentifier("eg", "on", 1)));
/*     */ 
/*  63 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("eg", "active", 0), false, false, 0), 1.0D, new ModelDestination(new ModelIdentifier("mixer", "active", 0)));
/*     */ 
/*  71 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("eg", 0), true, false, 0), -960.0D, new ModelDestination(new ModelIdentifier("mixer", "gain")));
/*     */ 
/*  79 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("noteon", "velocity"), true, false, 1), -960.0D, new ModelDestination(new ModelIdentifier("mixer", "gain")));
/*     */ 
/*  87 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi", "pitch"), false, true, 0), new ModelSource(new ModelIdentifier("midi_rpn", "0"), new ModelTransform()
/*     */     {
/*     */       public double transform(double paramAnonymousDouble)
/*     */       {
/*  96 */         int i = (int)(paramAnonymousDouble * 16384.0D);
/*  97 */         int j = i >> 7;
/*  98 */         int k = i & 0x7F;
/*  99 */         return j * 100 + k;
/*     */       }
/*     */     }), new ModelDestination(new ModelIdentifier("osc", "pitch")));
/*     */ 
/* 104 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("noteon", "keynumber"), false, false, 0), 12800.0D, new ModelDestination(new ModelIdentifier("osc", "pitch")));
/*     */ 
/* 112 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "7"), true, false, 1), -960.0D, new ModelDestination(new ModelIdentifier("mixer", "gain")));
/*     */ 
/* 120 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "8"), false, false, 0), 1000.0D, new ModelDestination(new ModelIdentifier("mixer", "balance")));
/*     */ 
/* 128 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "10"), false, false, 0), 1000.0D, new ModelDestination(new ModelIdentifier("mixer", "pan")));
/*     */ 
/* 136 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "11"), true, false, 1), -960.0D, new ModelDestination(new ModelIdentifier("mixer", "gain")));
/*     */ 
/* 144 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "91"), false, false, 0), 1000.0D, new ModelDestination(new ModelIdentifier("mixer", "reverb")));
/*     */ 
/* 152 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "93"), false, false, 0), 1000.0D, new ModelDestination(new ModelIdentifier("mixer", "chorus")));
/*     */ 
/* 160 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "71"), false, true, 0), 200.0D, new ModelDestination(new ModelIdentifier("filter", "q")));
/*     */ 
/* 167 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "74"), false, true, 0), 9600.0D, new ModelDestination(new ModelIdentifier("filter", "freq")));
/*     */ 
/* 175 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "72"), false, true, 0), 6000.0D, new ModelDestination(new ModelIdentifier("eg", "release2")));
/*     */ 
/* 183 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "73"), false, true, 0), 2000.0D, new ModelDestination(new ModelIdentifier("eg", "attack2")));
/*     */ 
/* 191 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "75"), false, true, 0), 6000.0D, new ModelDestination(new ModelIdentifier("eg", "decay2")));
/*     */ 
/* 199 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "67"), false, false, 3), -50.0D, new ModelDestination(ModelDestination.DESTINATION_GAIN));
/*     */ 
/* 207 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_cc", "67"), false, false, 3), -2400.0D, new ModelDestination(ModelDestination.DESTINATION_FILTER_FREQ));
/*     */ 
/* 215 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_rpn", "1"), false, true, 0), 100.0D, new ModelDestination(new ModelIdentifier("osc", "pitch")));
/*     */ 
/* 223 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("midi_rpn", "2"), false, true, 0), 12800.0D, new ModelDestination(new ModelIdentifier("osc", "pitch")));
/*     */ 
/* 231 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("master", "fine_tuning"), false, true, 0), 100.0D, new ModelDestination(new ModelIdentifier("osc", "pitch")));
/*     */ 
/* 239 */     defaultconnections[(i++)] = new ModelConnectionBlock(new ModelSource(new ModelIdentifier("master", "coarse_tuning"), false, true, 0), 12800.0D, new ModelDestination(new ModelIdentifier("osc", "pitch")));
/*     */ 
/* 247 */     defaultconnections[(i++)] = new ModelConnectionBlock(13500.0D, new ModelDestination(new ModelIdentifier("filter", "freq", 0)));
/*     */ 
/* 250 */     defaultconnections[(i++)] = new ModelConnectionBlock((-1.0D / 0.0D), new ModelDestination(new ModelIdentifier("eg", "delay", 0)));
/*     */ 
/* 253 */     defaultconnections[(i++)] = new ModelConnectionBlock((-1.0D / 0.0D), new ModelDestination(new ModelIdentifier("eg", "attack", 0)));
/*     */ 
/* 256 */     defaultconnections[(i++)] = new ModelConnectionBlock((-1.0D / 0.0D), new ModelDestination(new ModelIdentifier("eg", "hold", 0)));
/*     */ 
/* 259 */     defaultconnections[(i++)] = new ModelConnectionBlock((-1.0D / 0.0D), new ModelDestination(new ModelIdentifier("eg", "decay", 0)));
/*     */ 
/* 262 */     defaultconnections[(i++)] = new ModelConnectionBlock(1000.0D, new ModelDestination(new ModelIdentifier("eg", "sustain", 0)));
/*     */ 
/* 264 */     defaultconnections[(i++)] = new ModelConnectionBlock((-1.0D / 0.0D), new ModelDestination(new ModelIdentifier("eg", "release", 0)));
/*     */ 
/* 267 */     defaultconnections[(i++)] = new ModelConnectionBlock(1200.0D * Math.log(0.015D) / Math.log(2.0D), new ModelDestination(new ModelIdentifier("eg", "shutdown", 0)));
/*     */ 
/* 271 */     defaultconnections[(i++)] = new ModelConnectionBlock((-1.0D / 0.0D), new ModelDestination(new ModelIdentifier("eg", "delay", 1)));
/*     */ 
/* 274 */     defaultconnections[(i++)] = new ModelConnectionBlock((-1.0D / 0.0D), new ModelDestination(new ModelIdentifier("eg", "attack", 1)));
/*     */ 
/* 277 */     defaultconnections[(i++)] = new ModelConnectionBlock((-1.0D / 0.0D), new ModelDestination(new ModelIdentifier("eg", "hold", 1)));
/*     */ 
/* 280 */     defaultconnections[(i++)] = new ModelConnectionBlock((-1.0D / 0.0D), new ModelDestination(new ModelIdentifier("eg", "decay", 1)));
/*     */ 
/* 283 */     defaultconnections[(i++)] = new ModelConnectionBlock(1000.0D, new ModelDestination(new ModelIdentifier("eg", "sustain", 1)));
/*     */ 
/* 285 */     defaultconnections[(i++)] = new ModelConnectionBlock((-1.0D / 0.0D), new ModelDestination(new ModelIdentifier("eg", "release", 1)));
/*     */ 
/* 289 */     defaultconnections[(i++)] = new ModelConnectionBlock(-8.51318D, new ModelDestination(new ModelIdentifier("lfo", "freq", 0)));
/*     */ 
/* 291 */     defaultconnections[(i++)] = new ModelConnectionBlock((-1.0D / 0.0D), new ModelDestination(new ModelIdentifier("lfo", "delay", 0)));
/*     */ 
/* 294 */     defaultconnections[(i++)] = new ModelConnectionBlock(-8.51318D, new ModelDestination(new ModelIdentifier("lfo", "freq", 1)));
/*     */ 
/* 296 */     defaultconnections[(i++)] = new ModelConnectionBlock((-1.0D / 0.0D), new ModelDestination(new ModelIdentifier("lfo", "delay", 1)));
/*     */   }
/*     */ 
/*     */   private static class KeySortComparator
/*     */     implements Comparator<ModelSource>
/*     */   {
/*     */     public int compare(ModelSource paramModelSource1, ModelSource paramModelSource2)
/*     */     {
/* 322 */       return paramModelSource1.getIdentifier().toString().compareTo(paramModelSource2.getIdentifier().toString());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftPerformer
 * JD-Core Version:    0.6.2
 */