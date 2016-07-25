/*      */ package sun.print;
/*      */ 
/*      */ import java.awt.Window;
/*      */ import java.awt.print.PrinterJob;
/*      */ import java.io.File;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import javax.print.DocFlavor;
/*      */ import javax.print.DocFlavor.BYTE_ARRAY;
/*      */ import javax.print.DocFlavor.INPUT_STREAM;
/*      */ import javax.print.DocFlavor.SERVICE_FORMATTED;
/*      */ import javax.print.DocFlavor.URL;
/*      */ import javax.print.DocPrintJob;
/*      */ import javax.print.PrintService;
/*      */ import javax.print.ServiceUIFactory;
/*      */ import javax.print.attribute.Attribute;
/*      */ import javax.print.attribute.AttributeSet;
/*      */ import javax.print.attribute.AttributeSetUtilities;
/*      */ import javax.print.attribute.HashAttributeSet;
/*      */ import javax.print.attribute.HashPrintServiceAttributeSet;
/*      */ import javax.print.attribute.PrintRequestAttributeSet;
/*      */ import javax.print.attribute.PrintServiceAttribute;
/*      */ import javax.print.attribute.PrintServiceAttributeSet;
/*      */ import javax.print.attribute.standard.Chromaticity;
/*      */ import javax.print.attribute.standard.ColorSupported;
/*      */ import javax.print.attribute.standard.Copies;
/*      */ import javax.print.attribute.standard.CopiesSupported;
/*      */ import javax.print.attribute.standard.Destination;
/*      */ import javax.print.attribute.standard.Fidelity;
/*      */ import javax.print.attribute.standard.JobName;
/*      */ import javax.print.attribute.standard.Media;
/*      */ import javax.print.attribute.standard.MediaPrintableArea;
/*      */ import javax.print.attribute.standard.MediaSize;
/*      */ import javax.print.attribute.standard.MediaSizeName;
/*      */ import javax.print.attribute.standard.MediaTray;
/*      */ import javax.print.attribute.standard.OrientationRequested;
/*      */ import javax.print.attribute.standard.PageRanges;
/*      */ import javax.print.attribute.standard.PrintQuality;
/*      */ import javax.print.attribute.standard.PrinterIsAcceptingJobs;
/*      */ import javax.print.attribute.standard.PrinterName;
/*      */ import javax.print.attribute.standard.PrinterResolution;
/*      */ import javax.print.attribute.standard.PrinterState;
/*      */ import javax.print.attribute.standard.PrinterStateReason;
/*      */ import javax.print.attribute.standard.PrinterStateReasons;
/*      */ import javax.print.attribute.standard.QueuedJobCount;
/*      */ import javax.print.attribute.standard.RequestingUserName;
/*      */ import javax.print.attribute.standard.Severity;
/*      */ import javax.print.attribute.standard.SheetCollate;
/*      */ import javax.print.attribute.standard.Sides;
/*      */ import javax.print.event.PrintServiceAttributeListener;
/*      */ import sun.awt.windows.WPrinterJob;
/*      */ 
/*      */ public class Win32PrintService
/*      */   implements PrintService, AttributeUpdater, SunPrinterJobService
/*      */ {
/*   80 */   public static MediaSize[] predefMedia = Win32MediaSize.getPredefMedia();
/*      */ 
/*   82 */   private static final DocFlavor[] supportedFlavors = { DocFlavor.BYTE_ARRAY.GIF, DocFlavor.INPUT_STREAM.GIF, DocFlavor.URL.GIF, DocFlavor.BYTE_ARRAY.JPEG, DocFlavor.INPUT_STREAM.JPEG, DocFlavor.URL.JPEG, DocFlavor.BYTE_ARRAY.PNG, DocFlavor.INPUT_STREAM.PNG, DocFlavor.URL.PNG, DocFlavor.SERVICE_FORMATTED.PAGEABLE, DocFlavor.SERVICE_FORMATTED.PRINTABLE, DocFlavor.BYTE_ARRAY.AUTOSENSE, DocFlavor.URL.AUTOSENSE, DocFlavor.INPUT_STREAM.AUTOSENSE };
/*      */ 
/*  100 */   private static final Class[] serviceAttrCats = { PrinterName.class, PrinterIsAcceptingJobs.class, QueuedJobCount.class, ColorSupported.class };
/*      */ 
/*  110 */   private static Class[] otherAttrCats = { JobName.class, RequestingUserName.class, Copies.class, Destination.class, OrientationRequested.class, PageRanges.class, Media.class, MediaPrintableArea.class, Fidelity.class, SheetCollate.class, SunAlternateMedia.class, Chromaticity.class };
/*      */ 
/*  132 */   public static final MediaSizeName[] dmPaperToPrintService = { MediaSizeName.NA_LETTER, MediaSizeName.NA_LETTER, MediaSizeName.TABLOID, MediaSizeName.LEDGER, MediaSizeName.NA_LEGAL, MediaSizeName.INVOICE, MediaSizeName.EXECUTIVE, MediaSizeName.ISO_A3, MediaSizeName.ISO_A4, MediaSizeName.ISO_A4, MediaSizeName.ISO_A5, MediaSizeName.JIS_B4, MediaSizeName.JIS_B5, MediaSizeName.FOLIO, MediaSizeName.QUARTO, MediaSizeName.NA_10X14_ENVELOPE, MediaSizeName.B, MediaSizeName.NA_LETTER, MediaSizeName.NA_NUMBER_9_ENVELOPE, MediaSizeName.NA_NUMBER_10_ENVELOPE, MediaSizeName.NA_NUMBER_11_ENVELOPE, MediaSizeName.NA_NUMBER_12_ENVELOPE, MediaSizeName.NA_NUMBER_14_ENVELOPE, MediaSizeName.C, MediaSizeName.D, MediaSizeName.E, MediaSizeName.ISO_DESIGNATED_LONG, MediaSizeName.ISO_C5, MediaSizeName.ISO_C3, MediaSizeName.ISO_C4, MediaSizeName.ISO_C6, MediaSizeName.ITALY_ENVELOPE, MediaSizeName.ISO_B4, MediaSizeName.ISO_B5, MediaSizeName.ISO_B6, MediaSizeName.ITALY_ENVELOPE, MediaSizeName.MONARCH_ENVELOPE, MediaSizeName.PERSONAL_ENVELOPE, MediaSizeName.NA_10X15_ENVELOPE, MediaSizeName.NA_9X12_ENVELOPE, MediaSizeName.FOLIO, MediaSizeName.ISO_B4, MediaSizeName.JAPANESE_POSTCARD, MediaSizeName.NA_9X11_ENVELOPE };
/*      */ 
/*  157 */   private static final MediaTray[] dmPaperBinToPrintService = { MediaTray.TOP, MediaTray.BOTTOM, MediaTray.MIDDLE, MediaTray.MANUAL, MediaTray.ENVELOPE, Win32MediaTray.ENVELOPE_MANUAL, Win32MediaTray.AUTO, Win32MediaTray.TRACTOR, Win32MediaTray.SMALL_FORMAT, Win32MediaTray.LARGE_FORMAT, MediaTray.LARGE_CAPACITY, null, null, MediaTray.MAIN, Win32MediaTray.FORMSOURCE };
/*      */ 
/*  167 */   private static int DM_PAPERSIZE = 2;
/*  168 */   private static int DM_PRINTQUALITY = 1024;
/*  169 */   private static int DM_YRESOLUTION = 8192;
/*      */   private static final int DMRES_MEDIUM = -3;
/*      */   private static final int DMRES_HIGH = -4;
/*      */   private static final int DMORIENT_LANDSCAPE = 2;
/*      */   private static final int DMDUP_VERTICAL = 2;
/*      */   private static final int DMDUP_HORIZONTAL = 3;
/*      */   private static final int DMCOLLATE_TRUE = 1;
/*      */   private static final int DMPAPER_A2 = 66;
/*      */   private static final int DMPAPER_A6 = 70;
/*      */   private static final int DMPAPER_B6_JIS = 88;
/*      */   private static final int DEVCAP_COLOR = 1;
/*      */   private static final int DEVCAP_DUPLEX = 2;
/*      */   private static final int DEVCAP_COLLATE = 4;
/*      */   private static final int DEVCAP_QUALITY = 8;
/*      */   private static final int DEVCAP_POSTSCRIPT = 16;
/*      */   private String printer;
/*      */   private PrinterName name;
/*      */   private String port;
/*      */   private transient PrintServiceAttributeSet lastSet;
/*  196 */   private transient ServiceNotifier notifier = null;
/*      */   private MediaSizeName[] mediaSizeNames;
/*      */   private MediaPrintableArea[] mediaPrintables;
/*      */   private MediaTray[] mediaTrays;
/*      */   private PrinterResolution[] printRes;
/*      */   private HashMap mpaMap;
/*      */   private int nCopies;
/*      */   private int prnCaps;
/*      */   private int[] defaultSettings;
/*      */   private boolean gotTrays;
/*      */   private boolean gotCopies;
/*      */   private boolean mediaInitialized;
/*      */   private boolean mpaListInitialized;
/*      */   private ArrayList idList;
/*      */   private MediaSize[] mediaSizes;
/*      */   private boolean isInvalid;
/* 1615 */   private Win32DocumentPropertiesUI docPropertiesUI = null;
/*      */ 
/* 1678 */   private Win32ServiceUIFactory uiFactory = null;
/*      */ 
/*      */   Win32PrintService(String paramString)
/*      */   {
/*  218 */     if (paramString == null) {
/*  219 */       throw new IllegalArgumentException("null printer name");
/*      */     }
/*  221 */     this.printer = paramString;
/*      */ 
/*  224 */     this.mediaInitialized = false;
/*  225 */     this.gotTrays = false;
/*  226 */     this.gotCopies = false;
/*  227 */     this.isInvalid = false;
/*  228 */     this.printRes = null;
/*  229 */     this.prnCaps = 0;
/*  230 */     this.defaultSettings = null;
/*  231 */     this.port = null;
/*      */   }
/*      */ 
/*      */   public void invalidateService() {
/*  235 */     this.isInvalid = true;
/*      */   }
/*      */ 
/*      */   public String getName() {
/*  239 */     return this.printer;
/*      */   }
/*      */ 
/*      */   private PrinterName getPrinterName() {
/*  243 */     if (this.name == null) {
/*  244 */       this.name = new PrinterName(this.printer, null);
/*      */     }
/*  246 */     return this.name;
/*      */   }
/*      */ 
/*      */   public int findPaperID(MediaSizeName paramMediaSizeName) {
/*  250 */     if ((paramMediaSizeName instanceof Win32MediaSize)) {
/*  251 */       Win32MediaSize localWin32MediaSize = (Win32MediaSize)paramMediaSizeName;
/*  252 */       return localWin32MediaSize.getDMPaper();
/*      */     }
/*  254 */     for (int i = 0; i < dmPaperToPrintService.length; i++) {
/*  255 */       if (dmPaperToPrintService[i].equals(paramMediaSizeName)) {
/*  256 */         return i + 1;
/*      */       }
/*      */     }
/*  259 */     if (paramMediaSizeName.equals(MediaSizeName.ISO_A2)) {
/*  260 */       return 66;
/*      */     }
/*  262 */     if (paramMediaSizeName.equals(MediaSizeName.ISO_A6)) {
/*  263 */       return 70;
/*      */     }
/*  265 */     if (paramMediaSizeName.equals(MediaSizeName.JIS_B6)) {
/*  266 */       return 88;
/*      */     }
/*      */ 
/*  273 */     initMedia();
/*      */ 
/*  275 */     if ((this.idList != null) && (this.mediaSizes != null) && (this.idList.size() == this.mediaSizes.length))
/*      */     {
/*  277 */       for (i = 0; i < this.idList.size(); i++) {
/*  278 */         if (this.mediaSizes[i].getMediaSizeName() == paramMediaSizeName) {
/*  279 */           return ((Integer)this.idList.get(i)).intValue();
/*      */         }
/*      */       }
/*      */     }
/*  283 */     return 0;
/*      */   }
/*      */ 
/*      */   public int findTrayID(MediaTray paramMediaTray)
/*      */   {
/*  288 */     getMediaTrays();
/*      */ 
/*  290 */     if ((paramMediaTray instanceof Win32MediaTray)) {
/*  291 */       Win32MediaTray localWin32MediaTray = (Win32MediaTray)paramMediaTray;
/*  292 */       return localWin32MediaTray.getDMBinID();
/*      */     }
/*  294 */     for (int i = 0; i < dmPaperBinToPrintService.length; i++) {
/*  295 */       if (paramMediaTray.equals(dmPaperBinToPrintService[i])) {
/*  296 */         return i + 1;
/*      */       }
/*      */     }
/*  299 */     return 0;
/*      */   }
/*      */ 
/*      */   public MediaTray findMediaTray(int paramInt) {
/*  303 */     if ((paramInt >= 1) && (paramInt <= dmPaperBinToPrintService.length)) {
/*  304 */       return dmPaperBinToPrintService[(paramInt - 1)];
/*      */     }
/*  306 */     MediaTray[] arrayOfMediaTray = getMediaTrays();
/*  307 */     if (arrayOfMediaTray != null) {
/*  308 */       for (int i = 0; i < arrayOfMediaTray.length; i++) {
/*  309 */         if ((arrayOfMediaTray[i] instanceof Win32MediaTray)) {
/*  310 */           Win32MediaTray localWin32MediaTray = (Win32MediaTray)arrayOfMediaTray[i];
/*  311 */           if (localWin32MediaTray.winID == paramInt) {
/*  312 */             return localWin32MediaTray;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  317 */     return Win32MediaTray.AUTO;
/*      */   }
/*      */ 
/*      */   public MediaSizeName findWin32Media(int paramInt) {
/*  321 */     if ((paramInt >= 1) && (paramInt <= dmPaperToPrintService.length)) {
/*  322 */       return dmPaperToPrintService[(paramInt - 1)];
/*      */     }
/*  324 */     switch (paramInt)
/*      */     {
/*      */     case 66:
/*  328 */       return MediaSizeName.ISO_A2;
/*      */     case 70:
/*  330 */       return MediaSizeName.ISO_A6;
/*      */     case 88:
/*  332 */       return MediaSizeName.JIS_B6;
/*      */     }
/*  334 */     return null;
/*      */   }
/*      */ 
/*      */   private boolean addToUniqueList(ArrayList paramArrayList, MediaSizeName paramMediaSizeName)
/*      */   {
/*  340 */     for (int i = 0; i < paramArrayList.size(); i++) {
/*  341 */       MediaSizeName localMediaSizeName = (MediaSizeName)paramArrayList.get(i);
/*  342 */       if (localMediaSizeName == paramMediaSizeName) {
/*  343 */         return false;
/*      */       }
/*      */     }
/*  346 */     paramArrayList.add(paramMediaSizeName);
/*  347 */     return true;
/*      */   }
/*      */ 
/*      */   private synchronized void initMedia() {
/*  351 */     if (this.mediaInitialized == true) {
/*  352 */       return;
/*      */     }
/*  354 */     this.mediaInitialized = true;
/*  355 */     int[] arrayOfInt = getAllMediaIDs(this.printer, getPort());
/*  356 */     if (arrayOfInt == null) {
/*  357 */       return;
/*      */     }
/*      */ 
/*  360 */     ArrayList localArrayList1 = new ArrayList();
/*  361 */     ArrayList localArrayList2 = new ArrayList();
/*  362 */     ArrayList localArrayList3 = new ArrayList();
/*      */ 
/*  365 */     int i = 0;
/*      */ 
/*  375 */     this.idList = new ArrayList();
/*  376 */     for (int j = 0; j < arrayOfInt.length; j++) {
/*  377 */       this.idList.add(Integer.valueOf(arrayOfInt[j]));
/*      */     }
/*      */ 
/*  380 */     ArrayList localArrayList4 = new ArrayList();
/*  381 */     this.mediaSizes = getMediaSizes(this.idList, arrayOfInt, localArrayList4);
/*      */     boolean bool;
/*  382 */     for (int k = 0; k < this.idList.size(); k++)
/*      */     {
/*  385 */       Object localObject1 = findWin32Media(((Integer)this.idList.get(k)).intValue());
/*      */       Object localObject2;
/*  389 */       if ((localObject1 != null) && (this.idList.size() == this.mediaSizes.length))
/*      */       {
/*  391 */         MediaSize localMediaSize = MediaSize.getMediaSizeForName((MediaSizeName)localObject1);
/*  392 */         localObject2 = this.mediaSizes[k];
/*  393 */         int n = 2540;
/*  394 */         if ((Math.abs(localMediaSize.getX(1) - ((MediaSize)localObject2).getX(1)) > n) || (Math.abs(localMediaSize.getY(1) - ((MediaSize)localObject2).getY(1)) > n))
/*      */         {
/*  397 */           localObject1 = null;
/*      */         }
/*      */       }
/*  400 */       int m = localObject1 != null ? 1 : 0;
/*      */ 
/*  404 */       if ((localObject1 == null) && (this.idList.size() == this.mediaSizes.length)) {
/*  405 */         localObject1 = this.mediaSizes[k].getMediaSizeName();
/*      */       }
/*      */ 
/*  409 */       bool = false;
/*  410 */       if (localObject1 != null) {
/*  411 */         bool = addToUniqueList(localArrayList1, (MediaSizeName)localObject1);
/*      */       }
/*  413 */       if (((m == 0) || (!bool)) && (this.idList.size() == localArrayList4.size()))
/*      */       {
/*  418 */         localObject2 = Win32MediaSize.findMediaName((String)localArrayList4.get(k));
/*  419 */         if ((localObject2 == null) && (this.idList.size() == this.mediaSizes.length)) {
/*  420 */           localObject2 = new Win32MediaSize((String)localArrayList4.get(k), ((Integer)this.idList.get(k)).intValue());
/*  421 */           this.mediaSizes[k] = new MediaSize(this.mediaSizes[k].getX(1000), this.mediaSizes[k].getY(1000), 1000, (MediaSizeName)localObject2);
/*      */         }
/*      */ 
/*  424 */         if ((localObject2 != null) && (localObject2 != localObject1)) {
/*  425 */           if (!bool)
/*  426 */             bool = addToUniqueList(localArrayList1, localObject1 = localObject2);
/*      */           else {
/*  428 */             localArrayList2.add(localObject2);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  433 */     for (Win32MediaSize localWin32MediaSize : localArrayList2) {
/*  434 */       bool = addToUniqueList(localArrayList1, localWin32MediaSize);
/*      */     }
/*      */ 
/*  438 */     this.mediaSizeNames = new MediaSizeName[localArrayList1.size()];
/*  439 */     localArrayList1.toArray(this.mediaSizeNames);
/*      */   }
/*      */ 
/*      */   private synchronized MediaPrintableArea[] getMediaPrintables(MediaSizeName paramMediaSizeName)
/*      */   {
/*      */     Object localObject1;
/*  450 */     if (paramMediaSizeName == null) {
/*  451 */       if (this.mpaListInitialized == true) {
/*  452 */         return this.mediaPrintables;
/*      */       }
/*      */ 
/*      */     }
/*  456 */     else if ((this.mpaMap != null) && (this.mpaMap.get(paramMediaSizeName) != null)) {
/*  457 */       localObject1 = new MediaPrintableArea[1];
/*  458 */       localObject1[0] = ((MediaPrintableArea)this.mpaMap.get(paramMediaSizeName));
/*  459 */       return localObject1;
/*      */     }
/*      */ 
/*  463 */     initMedia();
/*      */ 
/*  465 */     if ((this.mediaSizeNames == null) && (this.mediaSizeNames.length == 0)) {
/*  466 */       return null;
/*      */     }
/*      */ 
/*  470 */     if (paramMediaSizeName != null) {
/*  471 */       localObject1 = new MediaSizeName[1];
/*  472 */       localObject1[0] = paramMediaSizeName;
/*      */     } else {
/*  474 */       localObject1 = this.mediaSizeNames;
/*      */     }
/*      */ 
/*  477 */     if (this.mpaMap == null) {
/*  478 */       this.mpaMap = new HashMap();
/*      */     }
/*      */ 
/*  481 */     for (int i = 0; i < localObject1.length; i++) {
/*  482 */       Object localObject2 = localObject1[i];
/*      */ 
/*  484 */       if (this.mpaMap.get(localObject2) == null)
/*      */       {
/*  488 */         if (localObject2 != null) {
/*  489 */           int j = findPaperID(localObject2);
/*  490 */           Object localObject3 = j != 0 ? getMediaPrintableArea(this.printer, j) : null;
/*  491 */           MediaPrintableArea localMediaPrintableArea = null;
/*  492 */           if (localObject3 != null) {
/*      */             try {
/*  494 */               localMediaPrintableArea = new MediaPrintableArea(localObject3[0], localObject3[1], localObject3[2], localObject3[3], 25400);
/*      */ 
/*  500 */               this.mpaMap.put(localObject2, localMediaPrintableArea);
/*      */             }
/*      */             catch (IllegalArgumentException localIllegalArgumentException1) {
/*      */             }
/*      */           }
/*      */           else {
/*  506 */             MediaSize localMediaSize = MediaSize.getMediaSizeForName(localObject2);
/*      */ 
/*  509 */             if (localMediaSize != null)
/*      */               try {
/*  511 */                 localMediaPrintableArea = new MediaPrintableArea(0.0F, 0.0F, localMediaSize.getX(25400), localMediaSize.getY(25400), 25400);
/*      */ 
/*  515 */                 this.mpaMap.put(localObject2, localMediaPrintableArea);
/*      */               }
/*      */               catch (IllegalArgumentException localIllegalArgumentException2) {
/*      */               }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  523 */     if (this.mpaMap.size() == 0) {
/*  524 */       return null;
/*      */     }
/*      */ 
/*  527 */     if (paramMediaSizeName != null) {
/*  528 */       if (this.mpaMap.get(paramMediaSizeName) == null) {
/*  529 */         return null;
/*      */       }
/*  531 */       MediaPrintableArea[] arrayOfMediaPrintableArea = new MediaPrintableArea[1];
/*      */ 
/*  533 */       arrayOfMediaPrintableArea[0] = ((MediaPrintableArea)this.mpaMap.get(paramMediaSizeName));
/*  534 */       return arrayOfMediaPrintableArea;
/*      */     }
/*  536 */     this.mediaPrintables = ((MediaPrintableArea[])this.mpaMap.values().toArray(new MediaPrintableArea[0]));
/*  537 */     this.mpaListInitialized = true;
/*  538 */     return this.mediaPrintables;
/*      */   }
/*      */ 
/*      */   private synchronized MediaTray[] getMediaTrays()
/*      */   {
/*  544 */     if ((this.gotTrays == true) && (this.mediaTrays != null)) {
/*  545 */       return this.mediaTrays;
/*      */     }
/*  547 */     String str = getPort();
/*  548 */     int[] arrayOfInt = getAllMediaTrays(this.printer, str);
/*  549 */     String[] arrayOfString = getAllMediaTrayNames(this.printer, str);
/*      */ 
/*  551 */     if ((arrayOfInt == null) || (arrayOfString == null)) {
/*  552 */       return null;
/*      */     }
/*      */ 
/*  558 */     int i = 0;
/*  559 */     for (int j = 0; j < arrayOfInt.length; j++) {
/*  560 */       if (arrayOfInt[j] > 0) i++;
/*      */     }
/*      */ 
/*  563 */     MediaTray[] arrayOfMediaTray = new MediaTray[i];
/*      */ 
/*  572 */     int m = 0; for (int n = 0; m < Math.min(arrayOfInt.length, arrayOfString.length); m++) {
/*  573 */       int k = arrayOfInt[m];
/*  574 */       if (k > 0)
/*      */       {
/*  576 */         if ((k > dmPaperBinToPrintService.length) || (dmPaperBinToPrintService[(k - 1)] == null))
/*      */         {
/*  578 */           arrayOfMediaTray[(n++)] = new Win32MediaTray(k, arrayOfString[m]);
/*      */         }
/*  580 */         else arrayOfMediaTray[(n++)] = dmPaperBinToPrintService[(k - 1)];
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  586 */     this.mediaTrays = arrayOfMediaTray;
/*  587 */     this.gotTrays = true;
/*  588 */     return this.mediaTrays;
/*      */   }
/*      */ 
/*      */   private boolean isSameSize(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
/*  592 */     float f1 = paramFloat1 - paramFloat3;
/*  593 */     float f2 = paramFloat2 - paramFloat4;
/*      */ 
/*  596 */     float f3 = paramFloat1 - paramFloat4;
/*  597 */     float f4 = paramFloat2 - paramFloat3;
/*      */ 
/*  599 */     if (((Math.abs(f1) <= 1.0F) && (Math.abs(f2) <= 1.0F)) || ((Math.abs(f3) <= 1.0F) && (Math.abs(f4) <= 1.0F)))
/*      */     {
/*  601 */       return true;
/*      */     }
/*  603 */     return false;
/*      */   }
/*      */ 
/*      */   public MediaSizeName findMatchingMediaSizeNameMM(float paramFloat1, float paramFloat2)
/*      */   {
/*  608 */     if (predefMedia != null) {
/*  609 */       for (int i = 0; i < predefMedia.length; i++)
/*  610 */         if (predefMedia[i] != null)
/*      */         {
/*  614 */           if (isSameSize(predefMedia[i].getX(1000), predefMedia[i].getY(1000), paramFloat1, paramFloat2))
/*      */           {
/*  617 */             return predefMedia[i].getMediaSizeName();
/*      */           }
/*      */         }
/*      */     }
/*  621 */     return null;
/*      */   }
/*      */ 
/*      */   private MediaSize[] getMediaSizes(ArrayList paramArrayList, int[] paramArrayOfInt, ArrayList<String> paramArrayList1)
/*      */   {
/*  626 */     if (paramArrayList1 == null) {
/*  627 */       paramArrayList1 = new ArrayList();
/*      */     }
/*      */ 
/*  630 */     String str = getPort();
/*  631 */     int[] arrayOfInt = getAllMediaSizes(this.printer, str);
/*  632 */     String[] arrayOfString = getAllMediaNames(this.printer, str);
/*  633 */     MediaSizeName localMediaSizeName = null;
/*  634 */     MediaSize localMediaSize = null;
/*      */ 
/*  637 */     if ((arrayOfInt == null) || (arrayOfString == null)) {
/*  638 */       return null;
/*      */     }
/*      */ 
/*  641 */     int i = arrayOfInt.length / 2;
/*  642 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/*  644 */     for (int j = 0; j < i; localMediaSize = null) {
/*  645 */       float f1 = arrayOfInt[(j * 2)] / 10.0F;
/*  646 */       float f2 = arrayOfInt[(j * 2 + 1)] / 10.0F;
/*      */       Object localObject;
/*  651 */       if ((f1 <= 0.0F) || (f2 <= 0.0F))
/*      */       {
/*  653 */         if (i == paramArrayOfInt.length) {
/*  654 */           localObject = Integer.valueOf(paramArrayOfInt[j]);
/*  655 */           paramArrayList.remove(paramArrayList.indexOf(localObject));
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  661 */         localMediaSizeName = findMatchingMediaSizeNameMM(f1, f2);
/*  662 */         if (localMediaSizeName != null) {
/*  663 */           localMediaSize = MediaSize.getMediaSizeForName(localMediaSizeName);
/*      */         }
/*      */ 
/*  666 */         if (localMediaSize != null) {
/*  667 */           localArrayList.add(localMediaSize);
/*  668 */           paramArrayList1.add(arrayOfString[j]);
/*      */         } else {
/*  670 */           localObject = Win32MediaSize.findMediaName(arrayOfString[j]);
/*  671 */           if (localObject == null)
/*  672 */             localObject = new Win32MediaSize(arrayOfString[j], paramArrayOfInt[j]);
/*      */           try
/*      */           {
/*  675 */             localMediaSize = new MediaSize(f1, f2, 1000, (MediaSizeName)localObject);
/*  676 */             localArrayList.add(localMediaSize);
/*  677 */             paramArrayList1.add(arrayOfString[j]);
/*      */           } catch (IllegalArgumentException localIllegalArgumentException) {
/*  679 */             if (i == paramArrayOfInt.length) {
/*  680 */               Integer localInteger = Integer.valueOf(paramArrayOfInt[j]);
/*  681 */               paramArrayList.remove(paramArrayList.indexOf(localInteger));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  644 */       j++;
/*      */     }
/*      */ 
/*  687 */     MediaSize[] arrayOfMediaSize = new MediaSize[localArrayList.size()];
/*  688 */     localArrayList.toArray(arrayOfMediaSize);
/*      */ 
/*  690 */     return arrayOfMediaSize;
/*      */   }
/*      */ 
/*      */   private PrinterIsAcceptingJobs getPrinterIsAcceptingJobs() {
/*  694 */     if (getJobStatus(this.printer, 2) != 1) {
/*  695 */       return PrinterIsAcceptingJobs.NOT_ACCEPTING_JOBS;
/*      */     }
/*      */ 
/*  698 */     return PrinterIsAcceptingJobs.ACCEPTING_JOBS;
/*      */   }
/*      */ 
/*      */   private PrinterState getPrinterState()
/*      */   {
/*  703 */     if (this.isInvalid) {
/*  704 */       return PrinterState.STOPPED;
/*      */     }
/*  706 */     return null;
/*      */   }
/*      */ 
/*      */   private PrinterStateReasons getPrinterStateReasons()
/*      */   {
/*  711 */     if (this.isInvalid) {
/*  712 */       PrinterStateReasons localPrinterStateReasons = new PrinterStateReasons();
/*  713 */       localPrinterStateReasons.put(PrinterStateReason.SHUTDOWN, Severity.ERROR);
/*  714 */       return localPrinterStateReasons;
/*      */     }
/*  716 */     return null;
/*      */   }
/*      */ 
/*      */   private QueuedJobCount getQueuedJobCount()
/*      */   {
/*  722 */     int i = getJobStatus(this.printer, 1);
/*  723 */     if (i != -1) {
/*  724 */       return new QueuedJobCount(i);
/*      */     }
/*      */ 
/*  727 */     return new QueuedJobCount(0);
/*      */   }
/*      */ 
/*      */   private boolean isSupportedCopies(Copies paramCopies)
/*      */   {
/*  732 */     synchronized (this) {
/*  733 */       if (!this.gotCopies) {
/*  734 */         this.nCopies = getCopiesSupported(this.printer, getPort());
/*  735 */         this.gotCopies = true;
/*      */       }
/*      */     }
/*  738 */     int i = paramCopies.getValue();
/*  739 */     return (i > 0) && (i <= this.nCopies);
/*      */   }
/*      */ 
/*      */   private boolean isSupportedMedia(MediaSizeName paramMediaSizeName)
/*      */   {
/*  744 */     initMedia();
/*      */ 
/*  746 */     if (this.mediaSizeNames != null) {
/*  747 */       for (int i = 0; i < this.mediaSizeNames.length; i++) {
/*  748 */         if (paramMediaSizeName.equals(this.mediaSizeNames[i])) {
/*  749 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  753 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isSupportedMediaPrintableArea(MediaPrintableArea paramMediaPrintableArea)
/*      */   {
/*  758 */     getMediaPrintables(null);
/*      */ 
/*  760 */     if (this.mediaPrintables != null) {
/*  761 */       for (int i = 0; i < this.mediaPrintables.length; i++) {
/*  762 */         if (paramMediaPrintableArea.equals(this.mediaPrintables[i])) {
/*  763 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  767 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isSupportedMediaTray(MediaTray paramMediaTray) {
/*  771 */     MediaTray[] arrayOfMediaTray = getMediaTrays();
/*      */ 
/*  773 */     if (arrayOfMediaTray != null) {
/*  774 */       for (int i = 0; i < arrayOfMediaTray.length; i++) {
/*  775 */         if (paramMediaTray.equals(arrayOfMediaTray[i])) {
/*  776 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  780 */     return false;
/*      */   }
/*      */ 
/*      */   private int getPrinterCapabilities() {
/*  784 */     if (this.prnCaps == 0) {
/*  785 */       this.prnCaps = getCapabilities(this.printer, getPort());
/*      */     }
/*  787 */     return this.prnCaps;
/*      */   }
/*      */ 
/*      */   private String getPort() {
/*  791 */     if (this.port == null) {
/*  792 */       this.port = getPrinterPort(this.printer);
/*      */     }
/*  794 */     return this.port;
/*      */   }
/*      */ 
/*      */   private int[] getDefaultPrinterSettings()
/*      */   {
/*  801 */     if (this.defaultSettings == null) {
/*  802 */       this.defaultSettings = getDefaultSettings(this.printer, getPort());
/*      */     }
/*  804 */     return this.defaultSettings;
/*      */   }
/*      */ 
/*      */   private PrinterResolution[] getPrintResolutions() {
/*  808 */     if (this.printRes == null) {
/*  809 */       int[] arrayOfInt = getAllResolutions(this.printer, getPort());
/*  810 */       if (arrayOfInt == null) {
/*  811 */         this.printRes = new PrinterResolution[0];
/*      */       } else {
/*  813 */         int i = arrayOfInt.length / 2;
/*      */ 
/*  815 */         ArrayList localArrayList = new ArrayList();
/*      */ 
/*  818 */         for (int j = 0; j < i; j++)
/*      */           try {
/*  820 */             PrinterResolution localPrinterResolution = new PrinterResolution(arrayOfInt[(j * 2)], arrayOfInt[(j * 2 + 1)], 100);
/*      */ 
/*  822 */             localArrayList.add(localPrinterResolution);
/*      */           }
/*      */           catch (IllegalArgumentException localIllegalArgumentException)
/*      */           {
/*      */           }
/*  827 */         this.printRes = ((PrinterResolution[])localArrayList.toArray(new PrinterResolution[localArrayList.size()]));
/*      */       }
/*      */     }
/*      */ 
/*  831 */     return this.printRes;
/*      */   }
/*      */ 
/*      */   private boolean isSupportedResolution(PrinterResolution paramPrinterResolution) {
/*  835 */     PrinterResolution[] arrayOfPrinterResolution = getPrintResolutions();
/*  836 */     if (arrayOfPrinterResolution != null) {
/*  837 */       for (int i = 0; i < arrayOfPrinterResolution.length; i++) {
/*  838 */         if (paramPrinterResolution.equals(arrayOfPrinterResolution[i])) {
/*  839 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  843 */     return false;
/*      */   }
/*      */ 
/*      */   public DocPrintJob createPrintJob() {
/*  847 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  848 */     if (localSecurityManager != null) {
/*  849 */       localSecurityManager.checkPrintJobAccess();
/*      */     }
/*  851 */     return new Win32PrintJob(this);
/*      */   }
/*      */ 
/*      */   private PrintServiceAttributeSet getDynamicAttributes() {
/*  855 */     HashPrintServiceAttributeSet localHashPrintServiceAttributeSet = new HashPrintServiceAttributeSet();
/*  856 */     localHashPrintServiceAttributeSet.add(getPrinterIsAcceptingJobs());
/*  857 */     localHashPrintServiceAttributeSet.add(getQueuedJobCount());
/*  858 */     return localHashPrintServiceAttributeSet;
/*      */   }
/*      */ 
/*      */   public PrintServiceAttributeSet getUpdatedAttributes() {
/*  862 */     PrintServiceAttributeSet localPrintServiceAttributeSet = getDynamicAttributes();
/*  863 */     if (this.lastSet == null) {
/*  864 */       this.lastSet = localPrintServiceAttributeSet;
/*  865 */       return AttributeSetUtilities.unmodifiableView(localPrintServiceAttributeSet);
/*      */     }
/*  867 */     HashPrintServiceAttributeSet localHashPrintServiceAttributeSet = new HashPrintServiceAttributeSet();
/*      */ 
/*  869 */     Attribute[] arrayOfAttribute = localPrintServiceAttributeSet.toArray();
/*  870 */     for (int i = 0; i < arrayOfAttribute.length; i++) {
/*  871 */       Attribute localAttribute = arrayOfAttribute[i];
/*  872 */       if (!this.lastSet.containsValue(localAttribute)) {
/*  873 */         localHashPrintServiceAttributeSet.add(localAttribute);
/*      */       }
/*      */     }
/*  876 */     this.lastSet = localPrintServiceAttributeSet;
/*  877 */     return AttributeSetUtilities.unmodifiableView(localHashPrintServiceAttributeSet);
/*      */   }
/*      */ 
/*      */   public void wakeNotifier()
/*      */   {
/*  882 */     synchronized (this) {
/*  883 */       if (this.notifier != null)
/*  884 */         this.notifier.wake();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addPrintServiceAttributeListener(PrintServiceAttributeListener paramPrintServiceAttributeListener)
/*      */   {
/*  891 */     synchronized (this) {
/*  892 */       if (paramPrintServiceAttributeListener == null) {
/*  893 */         return;
/*      */       }
/*  895 */       if (this.notifier == null) {
/*  896 */         this.notifier = new ServiceNotifier(this);
/*      */       }
/*  898 */       this.notifier.addListener(paramPrintServiceAttributeListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removePrintServiceAttributeListener(PrintServiceAttributeListener paramPrintServiceAttributeListener)
/*      */   {
/*  904 */     synchronized (this) {
/*  905 */       if ((paramPrintServiceAttributeListener == null) || (this.notifier == null)) {
/*  906 */         return;
/*      */       }
/*  908 */       this.notifier.removeListener(paramPrintServiceAttributeListener);
/*  909 */       if (this.notifier.isEmpty()) {
/*  910 */         this.notifier.stopNotifier();
/*  911 */         this.notifier = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public <T extends PrintServiceAttribute> T getAttribute(Class<T> paramClass)
/*      */   {
/*  919 */     if (paramClass == null) {
/*  920 */       throw new NullPointerException("category");
/*      */     }
/*  922 */     if (!PrintServiceAttribute.class.isAssignableFrom(paramClass)) {
/*  923 */       throw new IllegalArgumentException("Not a PrintServiceAttribute");
/*      */     }
/*  925 */     if (paramClass == ColorSupported.class) {
/*  926 */       int i = getPrinterCapabilities();
/*  927 */       if ((i & 0x1) != 0) {
/*  928 */         return ColorSupported.SUPPORTED;
/*      */       }
/*  930 */       return ColorSupported.NOT_SUPPORTED;
/*      */     }
/*  932 */     if (paramClass == PrinterName.class)
/*  933 */       return getPrinterName();
/*  934 */     if (paramClass == PrinterState.class)
/*  935 */       return getPrinterState();
/*  936 */     if (paramClass == PrinterStateReasons.class)
/*  937 */       return getPrinterStateReasons();
/*  938 */     if (paramClass == QueuedJobCount.class)
/*  939 */       return getQueuedJobCount();
/*  940 */     if (paramClass == PrinterIsAcceptingJobs.class) {
/*  941 */       return getPrinterIsAcceptingJobs();
/*      */     }
/*  943 */     return null;
/*      */   }
/*      */ 
/*      */   public PrintServiceAttributeSet getAttributes()
/*      */   {
/*  949 */     HashPrintServiceAttributeSet localHashPrintServiceAttributeSet = new HashPrintServiceAttributeSet();
/*  950 */     localHashPrintServiceAttributeSet.add(getPrinterName());
/*  951 */     localHashPrintServiceAttributeSet.add(getPrinterIsAcceptingJobs());
/*  952 */     PrinterState localPrinterState = getPrinterState();
/*  953 */     if (localPrinterState != null) {
/*  954 */       localHashPrintServiceAttributeSet.add(localPrinterState);
/*      */     }
/*  956 */     PrinterStateReasons localPrinterStateReasons = getPrinterStateReasons();
/*  957 */     if (localPrinterStateReasons != null) {
/*  958 */       localHashPrintServiceAttributeSet.add(localPrinterStateReasons);
/*      */     }
/*  960 */     localHashPrintServiceAttributeSet.add(getQueuedJobCount());
/*  961 */     int i = getPrinterCapabilities();
/*  962 */     if ((i & 0x1) != 0)
/*  963 */       localHashPrintServiceAttributeSet.add(ColorSupported.SUPPORTED);
/*      */     else {
/*  965 */       localHashPrintServiceAttributeSet.add(ColorSupported.NOT_SUPPORTED);
/*      */     }
/*      */ 
/*  968 */     return AttributeSetUtilities.unmodifiableView(localHashPrintServiceAttributeSet);
/*      */   }
/*      */ 
/*      */   public DocFlavor[] getSupportedDocFlavors() {
/*  972 */     int i = supportedFlavors.length;
/*      */ 
/*  974 */     int j = getPrinterCapabilities();
/*      */     DocFlavor[] arrayOfDocFlavor;
/*  977 */     if ((j & 0x10) != 0) {
/*  978 */       arrayOfDocFlavor = new DocFlavor[i + 3];
/*  979 */       System.arraycopy(supportedFlavors, 0, arrayOfDocFlavor, 0, i);
/*  980 */       arrayOfDocFlavor[i] = DocFlavor.BYTE_ARRAY.POSTSCRIPT;
/*  981 */       arrayOfDocFlavor[(i + 1)] = DocFlavor.INPUT_STREAM.POSTSCRIPT;
/*  982 */       arrayOfDocFlavor[(i + 2)] = DocFlavor.URL.POSTSCRIPT;
/*      */     } else {
/*  984 */       arrayOfDocFlavor = new DocFlavor[i];
/*  985 */       System.arraycopy(supportedFlavors, 0, arrayOfDocFlavor, 0, i);
/*      */     }
/*  987 */     return arrayOfDocFlavor;
/*      */   }
/*      */ 
/*      */   public boolean isDocFlavorSupported(DocFlavor paramDocFlavor)
/*      */   {
/*      */     DocFlavor[] arrayOfDocFlavor;
/*  996 */     if (isPostScriptFlavor(paramDocFlavor))
/*  997 */       arrayOfDocFlavor = getSupportedDocFlavors();
/*      */     else {
/*  999 */       arrayOfDocFlavor = supportedFlavors;
/*      */     }
/* 1001 */     for (int i = 0; i < arrayOfDocFlavor.length; i++) {
/* 1002 */       if (paramDocFlavor.equals(arrayOfDocFlavor[i])) {
/* 1003 */         return true;
/*      */       }
/*      */     }
/* 1006 */     return false;
/*      */   }
/*      */ 
/*      */   public Class<?>[] getSupportedAttributeCategories() {
/* 1010 */     ArrayList localArrayList = new ArrayList(otherAttrCats.length + 3);
/* 1011 */     for (int i = 0; i < otherAttrCats.length; i++) {
/* 1012 */       localArrayList.add(otherAttrCats[i]);
/*      */     }
/*      */ 
/* 1015 */     i = getPrinterCapabilities();
/*      */ 
/* 1017 */     if ((i & 0x2) != 0) {
/* 1018 */       localArrayList.add(Sides.class);
/*      */     }
/*      */ 
/* 1021 */     if ((i & 0x8) != 0) {
/* 1022 */       localObject = getDefaultPrinterSettings();
/*      */ 
/* 1024 */       if ((localObject[3] >= -4) && (localObject[3] < 0)) {
/* 1025 */         localArrayList.add(PrintQuality.class);
/*      */       }
/*      */     }
/*      */ 
/* 1029 */     Object localObject = getPrintResolutions();
/* 1030 */     if ((localObject != null) && (localObject.length > 0)) {
/* 1031 */       localArrayList.add(PrinterResolution.class);
/*      */     }
/*      */ 
/* 1034 */     return (Class[])localArrayList.toArray(new Class[localArrayList.size()]);
/*      */   }
/*      */ 
/*      */   public boolean isAttributeCategorySupported(Class<? extends Attribute> paramClass)
/*      */   {
/* 1041 */     if (paramClass == null) {
/* 1042 */       throw new NullPointerException("null category");
/*      */     }
/*      */ 
/* 1045 */     if (!Attribute.class.isAssignableFrom(paramClass)) {
/* 1046 */       throw new IllegalArgumentException(paramClass + " is not an Attribute");
/*      */     }
/*      */ 
/* 1050 */     Class[] arrayOfClass = getSupportedAttributeCategories();
/* 1051 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 1052 */       if (paramClass.equals(arrayOfClass[i])) {
/* 1053 */         return true;
/*      */       }
/*      */     }
/*      */ 
/* 1057 */     return false;
/*      */   }
/*      */ 
/*      */   public Object getDefaultAttributeValue(Class<? extends Attribute> paramClass)
/*      */   {
/* 1063 */     if (paramClass == null) {
/* 1064 */       throw new NullPointerException("null category");
/*      */     }
/* 1066 */     if (!Attribute.class.isAssignableFrom(paramClass)) {
/* 1067 */       throw new IllegalArgumentException(paramClass + " is not an Attribute");
/*      */     }
/*      */ 
/* 1071 */     if (!isAttributeCategorySupported(paramClass)) {
/* 1072 */       return null;
/*      */     }
/*      */ 
/* 1075 */     int[] arrayOfInt = getDefaultPrinterSettings();
/*      */ 
/* 1077 */     int i = arrayOfInt[0];
/* 1078 */     SecurityException localSecurityException1 = arrayOfInt[2];
/* 1079 */     URISyntaxException localURISyntaxException1 = arrayOfInt[3];
/* 1080 */     int j = arrayOfInt[4];
/* 1081 */     int k = arrayOfInt[5];
/* 1082 */     int m = arrayOfInt[6];
/* 1083 */     int n = arrayOfInt[7];
/*      */ 
/* 1085 */     if (paramClass == Copies.class) {
/* 1086 */       if (j > 0) {
/* 1087 */         return new Copies(j);
/*      */       }
/* 1089 */       return new Copies(1);
/*      */     }
/* 1091 */     if (paramClass == Chromaticity.class) {
/* 1092 */       int i1 = getPrinterCapabilities();
/* 1093 */       if ((i1 & 0x1) == 0) {
/* 1094 */         return Chromaticity.MONOCHROME;
/*      */       }
/* 1096 */       return Chromaticity.COLOR;
/*      */     }
/* 1098 */     if (paramClass == JobName.class)
/* 1099 */       return new JobName("Java Printing", null);
/* 1100 */     if (paramClass == OrientationRequested.class) {
/* 1101 */       if (k == 2) {
/* 1102 */         return OrientationRequested.LANDSCAPE;
/*      */       }
/* 1104 */       return OrientationRequested.PORTRAIT;
/*      */     }
/* 1106 */     if (paramClass == PageRanges.class)
/* 1107 */       return new PageRanges(1, 2147483647);
/*      */     MediaSizeName localMediaSizeName;
/*      */     Object localObject;
/* 1108 */     if (paramClass == Media.class) {
/* 1109 */       localMediaSizeName = findWin32Media(i);
/* 1110 */       if (localMediaSizeName != null) {
/* 1111 */         if ((!isSupportedMedia(localMediaSizeName)) && (this.mediaSizeNames != null)) {
/* 1112 */           localMediaSizeName = this.mediaSizeNames[0];
/* 1113 */           i = findPaperID(localMediaSizeName);
/*      */         }
/* 1115 */         return localMediaSizeName;
/*      */       }
/* 1117 */       initMedia();
/* 1118 */       if ((this.mediaSizeNames != null) && (this.mediaSizeNames.length > 0))
/*      */       {
/* 1121 */         if ((this.idList != null) && (this.mediaSizes != null) && (this.idList.size() == this.mediaSizes.length))
/*      */         {
/* 1123 */           localObject = Integer.valueOf(i);
/* 1124 */           int i3 = this.idList.indexOf(localObject);
/* 1125 */           if ((i3 >= 0) && (i3 < this.mediaSizes.length)) {
/* 1126 */             return this.mediaSizes[i3].getMediaSizeName();
/*      */           }
/*      */         }
/*      */ 
/* 1130 */         return this.mediaSizeNames[0];
/*      */       }
/*      */     } else {
/* 1133 */       if (paramClass == MediaPrintableArea.class)
/*      */       {
/* 1135 */         localMediaSizeName = findWin32Media(i);
/* 1136 */         if ((localMediaSizeName != null) && (!isSupportedMedia(localMediaSizeName)) && (this.mediaSizeNames != null))
/*      */         {
/* 1138 */           i = findPaperID(this.mediaSizeNames[0]);
/*      */         }
/* 1140 */         localObject = getMediaPrintableArea(this.printer, i);
/* 1141 */         if (localObject != null) {
/* 1142 */           MediaPrintableArea localMediaPrintableArea = null;
/*      */           try {
/* 1144 */             localMediaPrintableArea = new MediaPrintableArea(localObject[0], localObject[1], localObject[2], localObject[3], 25400);
/*      */           }
/*      */           catch (IllegalArgumentException localIllegalArgumentException)
/*      */           {
/*      */           }
/*      */ 
/* 1151 */           return localMediaPrintableArea;
/*      */         }
/* 1153 */         return null;
/* 1154 */       }if (paramClass == SunAlternateMedia.class)
/* 1155 */         return null;
/* 1156 */       if (paramClass == Destination.class)
/*      */         try {
/* 1158 */           return new Destination(new File("out.prn").toURI());
/*      */         } catch (SecurityException localSecurityException2) {
/*      */           try {
/* 1161 */             return new Destination(new URI("file:out.prn"));
/*      */           } catch (URISyntaxException localURISyntaxException2) {
/* 1163 */             return null;
/*      */           }
/*      */         }
/* 1166 */       if (paramClass == Sides.class) {
/* 1167 */         switch (m) {
/*      */         case 2:
/* 1169 */           return Sides.TWO_SIDED_LONG_EDGE;
/*      */         case 3:
/* 1171 */           return Sides.TWO_SIDED_SHORT_EDGE;
/*      */         }
/* 1173 */         return Sides.ONE_SIDED;
/*      */       }
/* 1175 */       if (paramClass == PrinterResolution.class) {
/* 1176 */         localSecurityException2 = localSecurityException1;
/* 1177 */         localURISyntaxException2 = localURISyntaxException1;
/* 1178 */         if ((localURISyntaxException2 < 0) || (localSecurityException2 < 0)) {
/* 1179 */           int i4 = localSecurityException2 > localURISyntaxException2 ? localSecurityException2 : localURISyntaxException2;
/* 1180 */           if (i4 > 0)
/* 1181 */             return new PrinterResolution(i4, i4, 100);
/*      */         }
/*      */         else
/*      */         {
/* 1185 */           return new PrinterResolution(localURISyntaxException2, localSecurityException2, 100);
/*      */         }
/*      */       } else { if (paramClass == ColorSupported.class) {
/* 1188 */           int i2 = getPrinterCapabilities();
/* 1189 */           if ((i2 & 0x1) != 0) {
/* 1190 */             return ColorSupported.SUPPORTED;
/*      */           }
/* 1192 */           return ColorSupported.NOT_SUPPORTED;
/*      */         }
/* 1194 */         if (paramClass == PrintQuality.class) {
/* 1195 */           if ((localURISyntaxException1 < 0) && (localURISyntaxException1 >= -4)) {
/* 1196 */             switch (localURISyntaxException1) {
/*      */             case -4:
/* 1198 */               return PrintQuality.HIGH;
/*      */             case -3:
/* 1200 */               return PrintQuality.NORMAL;
/*      */             }
/* 1202 */             return PrintQuality.DRAFT;
/*      */           }
/*      */         } else {
/* 1205 */           if (paramClass == RequestingUserName.class) {
/* 1206 */             String str = "";
/*      */             try {
/* 1208 */               str = System.getProperty("user.name", "");
/*      */             } catch (SecurityException localSecurityException3) {
/*      */             }
/* 1211 */             return new RequestingUserName(str, null);
/* 1212 */           }if (paramClass == SheetCollate.class) {
/* 1213 */             if (n == 1) {
/* 1214 */               return SheetCollate.COLLATED;
/*      */             }
/* 1216 */             return SheetCollate.UNCOLLATED;
/*      */           }
/* 1218 */           if (paramClass == Fidelity.class)
/* 1219 */             return Fidelity.FIDELITY_FALSE; 
/*      */         } } 
/*      */     }
/* 1221 */     return null;
/*      */   }
/*      */ 
/*      */   private boolean isPostScriptFlavor(DocFlavor paramDocFlavor) {
/* 1225 */     if ((paramDocFlavor.equals(DocFlavor.BYTE_ARRAY.POSTSCRIPT)) || (paramDocFlavor.equals(DocFlavor.INPUT_STREAM.POSTSCRIPT)) || (paramDocFlavor.equals(DocFlavor.URL.POSTSCRIPT)))
/*      */     {
/* 1228 */       return true;
/*      */     }
/*      */ 
/* 1231 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isPSDocAttr(Class paramClass)
/*      */   {
/* 1236 */     if ((paramClass == OrientationRequested.class) || (paramClass == Copies.class)) {
/* 1237 */       return true;
/*      */     }
/*      */ 
/* 1240 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isAutoSense(DocFlavor paramDocFlavor)
/*      */   {
/* 1245 */     if ((paramDocFlavor.equals(DocFlavor.BYTE_ARRAY.AUTOSENSE)) || (paramDocFlavor.equals(DocFlavor.INPUT_STREAM.AUTOSENSE)) || (paramDocFlavor.equals(DocFlavor.URL.AUTOSENSE)))
/*      */     {
/* 1248 */       return true;
/*      */     }
/*      */ 
/* 1251 */     return false;
/*      */   }
/*      */ 
/*      */   public Object getSupportedAttributeValues(Class<? extends Attribute> paramClass, DocFlavor paramDocFlavor, AttributeSet paramAttributeSet)
/*      */   {
/* 1260 */     if (paramClass == null) {
/* 1261 */       throw new NullPointerException("null category");
/*      */     }
/* 1263 */     if (!Attribute.class.isAssignableFrom(paramClass)) {
/* 1264 */       throw new IllegalArgumentException(paramClass + " does not implement Attribute");
/*      */     }
/*      */ 
/* 1267 */     if (paramDocFlavor != null) {
/* 1268 */       if (!isDocFlavorSupported(paramDocFlavor)) {
/* 1269 */         throw new IllegalArgumentException(paramDocFlavor + " is an unsupported flavor");
/*      */       }
/*      */ 
/* 1273 */       if ((isAutoSense(paramDocFlavor)) || ((isPostScriptFlavor(paramDocFlavor)) && (isPSDocAttr(paramClass))))
/*      */       {
/* 1275 */         return null;
/*      */       }
/*      */     }
/* 1278 */     if (!isAttributeCategorySupported(paramClass)) {
/* 1279 */       return null;
/*      */     }
/*      */ 
/* 1282 */     if (paramClass == JobName.class)
/* 1283 */       return new JobName("Java Printing", null);
/* 1284 */     if (paramClass == RequestingUserName.class) {
/* 1285 */       String str = "";
/*      */       try {
/* 1287 */         str = System.getProperty("user.name", "");
/*      */       } catch (SecurityException localSecurityException2) {
/*      */       }
/* 1290 */       return new RequestingUserName(str, null);
/*      */     }
/*      */     int i;
/* 1291 */     if (paramClass == ColorSupported.class) {
/* 1292 */       i = getPrinterCapabilities();
/* 1293 */       if ((i & 0x1) != 0) {
/* 1294 */         return ColorSupported.SUPPORTED;
/*      */       }
/* 1296 */       return ColorSupported.NOT_SUPPORTED;
/*      */     }
/* 1298 */     if (paramClass == Chromaticity.class) {
/* 1299 */       if ((paramDocFlavor == null) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) || (paramDocFlavor.equals(DocFlavor.BYTE_ARRAY.GIF)) || (paramDocFlavor.equals(DocFlavor.INPUT_STREAM.GIF)) || (paramDocFlavor.equals(DocFlavor.URL.GIF)) || (paramDocFlavor.equals(DocFlavor.BYTE_ARRAY.JPEG)) || (paramDocFlavor.equals(DocFlavor.INPUT_STREAM.JPEG)) || (paramDocFlavor.equals(DocFlavor.URL.JPEG)) || (paramDocFlavor.equals(DocFlavor.BYTE_ARRAY.PNG)) || (paramDocFlavor.equals(DocFlavor.INPUT_STREAM.PNG)) || (paramDocFlavor.equals(DocFlavor.URL.PNG)))
/*      */       {
/* 1311 */         i = getPrinterCapabilities();
/* 1312 */         if ((i & 0x1) == 0) {
/* 1313 */           arrayOfChromaticity = new Chromaticity[1];
/* 1314 */           arrayOfChromaticity[0] = Chromaticity.MONOCHROME;
/* 1315 */           return arrayOfChromaticity;
/*      */         }
/* 1317 */         Chromaticity[] arrayOfChromaticity = new Chromaticity[2];
/* 1318 */         arrayOfChromaticity[0] = Chromaticity.MONOCHROME;
/* 1319 */         arrayOfChromaticity[1] = Chromaticity.COLOR;
/* 1320 */         return arrayOfChromaticity;
/*      */       }
/*      */ 
/* 1323 */       return null;
/*      */     }
/* 1325 */     if (paramClass == Destination.class)
/*      */       try {
/* 1327 */         return new Destination(new File("out.prn").toURI());
/*      */       } catch (SecurityException localSecurityException1) {
/*      */         try {
/* 1330 */           return new Destination(new URI("file:out.prn"));
/*      */         } catch (URISyntaxException localURISyntaxException) {
/* 1332 */           return null;
/*      */         }
/*      */       }
/* 1335 */     if (paramClass == OrientationRequested.class) {
/* 1336 */       if ((paramDocFlavor == null) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) || (paramDocFlavor.equals(DocFlavor.INPUT_STREAM.GIF)) || (paramDocFlavor.equals(DocFlavor.INPUT_STREAM.JPEG)) || (paramDocFlavor.equals(DocFlavor.INPUT_STREAM.PNG)) || (paramDocFlavor.equals(DocFlavor.BYTE_ARRAY.GIF)) || (paramDocFlavor.equals(DocFlavor.BYTE_ARRAY.JPEG)) || (paramDocFlavor.equals(DocFlavor.BYTE_ARRAY.PNG)) || (paramDocFlavor.equals(DocFlavor.URL.GIF)) || (paramDocFlavor.equals(DocFlavor.URL.JPEG)) || (paramDocFlavor.equals(DocFlavor.URL.PNG)))
/*      */       {
/* 1348 */         OrientationRequested[] arrayOfOrientationRequested = new OrientationRequested[3];
/* 1349 */         arrayOfOrientationRequested[0] = OrientationRequested.PORTRAIT;
/* 1350 */         arrayOfOrientationRequested[1] = OrientationRequested.LANDSCAPE;
/* 1351 */         arrayOfOrientationRequested[2] = OrientationRequested.REVERSE_LANDSCAPE;
/* 1352 */         return arrayOfOrientationRequested;
/*      */       }
/* 1354 */       return null;
/*      */     }
/* 1356 */     if ((paramClass == Copies.class) || (paramClass == CopiesSupported.class))
/*      */     {
/* 1358 */       synchronized (this) {
/* 1359 */         if (!this.gotCopies) {
/* 1360 */           this.nCopies = getCopiesSupported(this.printer, getPort());
/* 1361 */           this.gotCopies = true;
/*      */         }
/*      */       }
/* 1364 */       return new CopiesSupported(1, this.nCopies);
/*      */     }
/*      */     Object localObject2;
/*      */     Object localObject4;
/* 1365 */     if (paramClass == Media.class)
/*      */     {
/* 1367 */       initMedia();
/*      */ 
/* 1369 */       int j = this.mediaSizeNames == null ? 0 : this.mediaSizeNames.length;
/*      */ 
/* 1371 */       localObject2 = getMediaTrays();
/*      */ 
/* 1373 */       j += (localObject2 == null ? 0 : localObject2.length);
/*      */ 
/* 1375 */       localObject4 = new Media[j];
/* 1376 */       if (this.mediaSizeNames != null) {
/* 1377 */         System.arraycopy(this.mediaSizeNames, 0, localObject4, 0, this.mediaSizeNames.length);
/*      */       }
/*      */ 
/* 1380 */       if (localObject2 != null) {
/* 1381 */         System.arraycopy(localObject2, 0, localObject4, j - localObject2.length, localObject2.length);
/*      */       }
/*      */ 
/* 1384 */       return localObject4;
/*      */     }
/*      */     Object localObject1;
/* 1385 */     if (paramClass == MediaPrintableArea.class)
/*      */     {
/* 1387 */       localObject1 = null;
/* 1388 */       if ((paramAttributeSet != null) && ((localObject1 = (Media)paramAttributeSet.get(Media.class)) != null))
/*      */       {
/* 1392 */         if (!(localObject1 instanceof MediaSizeName))
/*      */         {
/* 1395 */           localObject1 = null;
/*      */         }
/*      */       }
/*      */ 
/* 1399 */       localObject2 = getMediaPrintables((MediaSizeName)localObject1);
/*      */ 
/* 1401 */       if (localObject2 != null) {
/* 1402 */         localObject4 = new MediaPrintableArea[localObject2.length];
/* 1403 */         System.arraycopy(localObject2, 0, localObject4, 0, localObject2.length);
/* 1404 */         return localObject4;
/*      */       }
/* 1406 */       return null;
/*      */     }
/* 1408 */     if (paramClass == SunAlternateMedia.class) {
/* 1409 */       return new SunAlternateMedia((Media)getDefaultAttributeValue(Media.class));
/*      */     }
/* 1411 */     if (paramClass == PageRanges.class) {
/* 1412 */       if ((paramDocFlavor == null) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)))
/*      */       {
/* 1415 */         localObject1 = new PageRanges[1];
/* 1416 */         localObject1[0] = new PageRanges(1, 2147483647);
/* 1417 */         return localObject1;
/*      */       }
/* 1419 */       return null;
/*      */     }
/* 1421 */     if (paramClass == PrinterResolution.class) {
/* 1422 */       localObject1 = getPrintResolutions();
/* 1423 */       if (localObject1 == null) {
/* 1424 */         return null;
/*      */       }
/* 1426 */       localObject2 = new PrinterResolution[localObject1.length];
/*      */ 
/* 1428 */       System.arraycopy(localObject1, 0, localObject2, 0, localObject1.length);
/* 1429 */       return localObject2;
/* 1430 */     }if (paramClass == Sides.class) {
/* 1431 */       if ((paramDocFlavor == null) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)))
/*      */       {
/* 1434 */         localObject1 = new Sides[3];
/* 1435 */         localObject1[0] = Sides.ONE_SIDED;
/* 1436 */         localObject1[1] = Sides.TWO_SIDED_LONG_EDGE;
/* 1437 */         localObject1[2] = Sides.TWO_SIDED_SHORT_EDGE;
/* 1438 */         return localObject1;
/*      */       }
/* 1440 */       return null;
/*      */     }
/* 1442 */     if (paramClass == PrintQuality.class) {
/* 1443 */       localObject1 = new PrintQuality[3];
/* 1444 */       localObject1[0] = PrintQuality.DRAFT;
/* 1445 */       localObject1[1] = PrintQuality.HIGH;
/* 1446 */       localObject1[2] = PrintQuality.NORMAL;
/* 1447 */       return localObject1;
/* 1448 */     }if (paramClass == SheetCollate.class) {
/* 1449 */       if ((paramDocFlavor == null) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)))
/*      */       {
/* 1452 */         localObject1 = new SheetCollate[2];
/* 1453 */         localObject1[0] = SheetCollate.COLLATED;
/* 1454 */         localObject1[1] = SheetCollate.UNCOLLATED;
/* 1455 */         return localObject1;
/*      */       }
/* 1457 */       return null;
/*      */     }
/* 1459 */     if (paramClass == Fidelity.class) {
/* 1460 */       localObject1 = new Fidelity[2];
/* 1461 */       localObject1[0] = Fidelity.FIDELITY_FALSE;
/* 1462 */       localObject1[1] = Fidelity.FIDELITY_TRUE;
/* 1463 */       return localObject1;
/*      */     }
/* 1465 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isAttributeValueSupported(Attribute paramAttribute, DocFlavor paramDocFlavor, AttributeSet paramAttributeSet)
/*      */   {
/* 1473 */     if (paramAttribute == null) {
/* 1474 */       throw new NullPointerException("null attribute");
/*      */     }
/* 1476 */     Class localClass = paramAttribute.getCategory();
/* 1477 */     if (paramDocFlavor != null) {
/* 1478 */       if (!isDocFlavorSupported(paramDocFlavor)) {
/* 1479 */         throw new IllegalArgumentException(paramDocFlavor + " is an unsupported flavor");
/*      */       }
/*      */ 
/* 1483 */       if ((isAutoSense(paramDocFlavor)) || ((isPostScriptFlavor(paramDocFlavor)) && (isPSDocAttr(localClass))))
/*      */       {
/* 1485 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 1489 */     if (!isAttributeCategorySupported(localClass)) {
/* 1490 */       return false;
/*      */     }
/* 1492 */     if (localClass == Chromaticity.class) {
/* 1493 */       if ((paramDocFlavor == null) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) || (paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) || (paramDocFlavor.equals(DocFlavor.BYTE_ARRAY.GIF)) || (paramDocFlavor.equals(DocFlavor.INPUT_STREAM.GIF)) || (paramDocFlavor.equals(DocFlavor.URL.GIF)) || (paramDocFlavor.equals(DocFlavor.BYTE_ARRAY.JPEG)) || (paramDocFlavor.equals(DocFlavor.INPUT_STREAM.JPEG)) || (paramDocFlavor.equals(DocFlavor.URL.JPEG)) || (paramDocFlavor.equals(DocFlavor.BYTE_ARRAY.PNG)) || (paramDocFlavor.equals(DocFlavor.INPUT_STREAM.PNG)) || (paramDocFlavor.equals(DocFlavor.URL.PNG)))
/*      */       {
/* 1505 */         int i = getPrinterCapabilities();
/* 1506 */         if ((i & 0x1) != 0) {
/* 1507 */           return true;
/*      */         }
/* 1509 */         return paramAttribute == Chromaticity.MONOCHROME;
/*      */       }
/*      */ 
/* 1512 */       return false;
/*      */     }
/* 1514 */     if (localClass == Copies.class)
/* 1515 */       return isSupportedCopies((Copies)paramAttribute);
/*      */     Object localObject;
/* 1517 */     if (localClass == Destination.class) {
/* 1518 */       localObject = ((Destination)paramAttribute).getURI();
/* 1519 */       if (("file".equals(((URI)localObject).getScheme())) && (!((URI)localObject).getSchemeSpecificPart().equals("")))
/*      */       {
/* 1521 */         return true;
/*      */       }
/* 1523 */       return false;
/*      */     }
/*      */ 
/* 1526 */     if (localClass == Media.class) {
/* 1527 */       if ((paramAttribute instanceof MediaSizeName)) {
/* 1528 */         return isSupportedMedia((MediaSizeName)paramAttribute);
/*      */       }
/* 1530 */       if ((paramAttribute instanceof MediaTray))
/* 1531 */         return isSupportedMediaTray((MediaTray)paramAttribute);
/*      */     }
/*      */     else {
/* 1534 */       if (localClass == MediaPrintableArea.class) {
/* 1535 */         return isSupportedMediaPrintableArea((MediaPrintableArea)paramAttribute);
/*      */       }
/* 1537 */       if (localClass == SunAlternateMedia.class) {
/* 1538 */         localObject = ((SunAlternateMedia)paramAttribute).getMedia();
/* 1539 */         return isAttributeValueSupported((Attribute)localObject, paramDocFlavor, paramAttributeSet);
/*      */       }
/* 1541 */       if ((localClass == PageRanges.class) || (localClass == SheetCollate.class) || (localClass == Sides.class))
/*      */       {
/* 1544 */         if ((paramDocFlavor != null) && (!paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) && (!paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)))
/*      */         {
/* 1547 */           return false;
/*      */         }
/* 1549 */       } else if (localClass == PrinterResolution.class) {
/* 1550 */         if ((paramAttribute instanceof PrinterResolution))
/* 1551 */           return isSupportedResolution((PrinterResolution)paramAttribute);
/*      */       }
/* 1553 */       else if (localClass == OrientationRequested.class) {
/* 1554 */         if ((paramAttribute == OrientationRequested.REVERSE_PORTRAIT) || ((paramDocFlavor != null) && (!paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) && (!paramDocFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) && (!paramDocFlavor.equals(DocFlavor.INPUT_STREAM.GIF)) && (!paramDocFlavor.equals(DocFlavor.INPUT_STREAM.JPEG)) && (!paramDocFlavor.equals(DocFlavor.INPUT_STREAM.PNG)) && (!paramDocFlavor.equals(DocFlavor.BYTE_ARRAY.GIF)) && (!paramDocFlavor.equals(DocFlavor.BYTE_ARRAY.JPEG)) && (!paramDocFlavor.equals(DocFlavor.BYTE_ARRAY.PNG)) && (!paramDocFlavor.equals(DocFlavor.URL.GIF)) && (!paramDocFlavor.equals(DocFlavor.URL.JPEG)) && (!paramDocFlavor.equals(DocFlavor.URL.PNG))))
/*      */         {
/* 1567 */           return false;
/*      */         }
/*      */       }
/* 1570 */       else if (localClass == ColorSupported.class) {
/* 1571 */         int j = getPrinterCapabilities();
/* 1572 */         int k = (j & 0x1) != 0 ? 1 : 0;
/* 1573 */         if (((k == 0) && (paramAttribute == ColorSupported.SUPPORTED)) || ((k != 0) && (paramAttribute == ColorSupported.NOT_SUPPORTED)))
/*      */         {
/* 1575 */           return false;
/*      */         }
/*      */       }
/*      */     }
/* 1578 */     return true;
/*      */   }
/*      */ 
/*      */   public AttributeSet getUnsupportedAttributes(DocFlavor paramDocFlavor, AttributeSet paramAttributeSet)
/*      */   {
/* 1584 */     if ((paramDocFlavor != null) && (!isDocFlavorSupported(paramDocFlavor))) {
/* 1585 */       throw new IllegalArgumentException("flavor " + paramDocFlavor + "is not supported");
/*      */     }
/*      */ 
/* 1589 */     if (paramAttributeSet == null) {
/* 1590 */       return null;
/*      */     }
/*      */ 
/* 1594 */     HashAttributeSet localHashAttributeSet = new HashAttributeSet();
/* 1595 */     Attribute[] arrayOfAttribute = paramAttributeSet.toArray();
/* 1596 */     for (int i = 0; i < arrayOfAttribute.length; i++)
/*      */       try {
/* 1598 */         Attribute localAttribute = arrayOfAttribute[i];
/* 1599 */         if (!isAttributeCategorySupported(localAttribute.getCategory())) {
/* 1600 */           localHashAttributeSet.add(localAttribute);
/*      */         }
/* 1602 */         else if (!isAttributeValueSupported(localAttribute, paramDocFlavor, paramAttributeSet))
/* 1603 */           localHashAttributeSet.add(localAttribute);
/*      */       }
/*      */       catch (ClassCastException localClassCastException)
/*      */       {
/*      */       }
/* 1608 */     if (localHashAttributeSet.isEmpty()) {
/* 1609 */       return null;
/*      */     }
/* 1611 */     return localHashAttributeSet;
/*      */   }
/*      */ 
/*      */   private synchronized DocumentPropertiesUI getDocumentPropertiesUI()
/*      */   {
/* 1641 */     return new Win32DocumentPropertiesUI(this, null);
/*      */   }
/*      */ 
/*      */   public synchronized ServiceUIFactory getServiceUIFactory()
/*      */   {
/* 1681 */     if (this.uiFactory == null) {
/* 1682 */       this.uiFactory = new Win32ServiceUIFactory(this);
/*      */     }
/* 1684 */     return this.uiFactory;
/*      */   }
/*      */ 
/*      */   public String toString() {
/* 1688 */     return "Win32 Printer : " + getName();
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject) {
/* 1692 */     return (paramObject == this) || (((paramObject instanceof Win32PrintService)) && (((Win32PrintService)paramObject).getName().equals(getName())));
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1698 */     return getClass().hashCode() + getName().hashCode();
/*      */   }
/*      */ 
/*      */   public boolean usesClass(Class paramClass) {
/* 1702 */     return paramClass == WPrinterJob.class;
/*      */   }
/*      */ 
/*      */   private native int[] getAllMediaIDs(String paramString1, String paramString2);
/*      */ 
/*      */   private native int[] getAllMediaSizes(String paramString1, String paramString2);
/*      */ 
/*      */   private native int[] getAllMediaTrays(String paramString1, String paramString2);
/*      */ 
/*      */   private native float[] getMediaPrintableArea(String paramString, int paramInt);
/*      */ 
/*      */   private native String[] getAllMediaNames(String paramString1, String paramString2);
/*      */ 
/*      */   private native String[] getAllMediaTrayNames(String paramString1, String paramString2);
/*      */ 
/*      */   private native int getCopiesSupported(String paramString1, String paramString2);
/*      */ 
/*      */   private native int[] getAllResolutions(String paramString1, String paramString2);
/*      */ 
/*      */   private native int getCapabilities(String paramString1, String paramString2);
/*      */ 
/*      */   private native int[] getDefaultSettings(String paramString1, String paramString2);
/*      */ 
/*      */   private native int getJobStatus(String paramString, int paramInt);
/*      */ 
/*      */   private native String getPrinterPort(String paramString);
/*      */ 
/*      */   private static class Win32DocumentPropertiesUI extends DocumentPropertiesUI
/*      */   {
/*      */     Win32PrintService service;
/*      */ 
/*      */     private Win32DocumentPropertiesUI(Win32PrintService paramWin32PrintService)
/*      */     {
/* 1623 */       this.service = paramWin32PrintService;
/*      */     }
/*      */ 
/*      */     public PrintRequestAttributeSet showDocumentProperties(PrinterJob paramPrinterJob, Window paramWindow, PrintService paramPrintService, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */     {
/* 1632 */       if (!(paramPrinterJob instanceof WPrinterJob)) {
/* 1633 */         return null;
/*      */       }
/* 1635 */       WPrinterJob localWPrinterJob = (WPrinterJob)paramPrinterJob;
/* 1636 */       return localWPrinterJob.showDocumentProperties(paramWindow, paramPrintService, paramPrintRequestAttributeSet);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Win32ServiceUIFactory extends ServiceUIFactory
/*      */   {
/*      */     Win32PrintService service;
/*      */ 
/*      */     Win32ServiceUIFactory(Win32PrintService paramWin32PrintService)
/*      */     {
/* 1649 */       this.service = paramWin32PrintService;
/*      */     }
/*      */ 
/*      */     public Object getUI(int paramInt, String paramString) {
/* 1653 */       if (paramInt <= 3) {
/* 1654 */         return null;
/*      */       }
/* 1656 */       if ((paramInt == 199) && (DocumentPropertiesUI.DOCPROPERTIESCLASSNAME.equals(paramString)))
/*      */       {
/* 1659 */         return this.service.getDocumentPropertiesUI();
/*      */       }
/* 1661 */       throw new IllegalArgumentException("Unsupported role");
/*      */     }
/*      */ 
/*      */     public String[] getUIClassNamesForRole(int paramInt)
/*      */     {
/* 1666 */       if (paramInt <= 3) {
/* 1667 */         return null;
/*      */       }
/* 1669 */       if (paramInt == 199) {
/* 1670 */         String[] arrayOfString = new String[0];
/* 1671 */         arrayOfString[0] = DocumentPropertiesUI.DOCPROPERTIESCLASSNAME;
/* 1672 */         return arrayOfString;
/*      */       }
/* 1674 */       throw new IllegalArgumentException("Unsupported role");
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.Win32PrintService
 * JD-Core Version:    0.6.2
 */