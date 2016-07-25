/*      */ package sun.print;
/*      */ 
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.JobAttributes;
/*      */ import java.awt.JobAttributes.DefaultSelectionType;
/*      */ import java.awt.JobAttributes.DestinationType;
/*      */ import java.awt.JobAttributes.DialogType;
/*      */ import java.awt.JobAttributes.MultipleDocumentHandlingType;
/*      */ import java.awt.JobAttributes.SidesType;
/*      */ import java.awt.PageAttributes;
/*      */ import java.awt.PageAttributes.ColorType;
/*      */ import java.awt.PageAttributes.MediaType;
/*      */ import java.awt.PageAttributes.OrientationRequestedType;
/*      */ import java.awt.PageAttributes.OriginType;
/*      */ import java.awt.PageAttributes.PrintQualityType;
/*      */ import java.awt.PrintJob;
/*      */ import java.awt.print.PageFormat;
/*      */ import java.awt.print.Paper;
/*      */ import java.awt.print.Printable;
/*      */ import java.awt.print.PrinterException;
/*      */ import java.awt.print.PrinterJob;
/*      */ import java.io.File;
/*      */ import java.io.FilePermission;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Properties;
/*      */ import javax.print.PrintService;
/*      */ import javax.print.attribute.Attribute;
/*      */ import javax.print.attribute.HashPrintRequestAttributeSet;
/*      */ import javax.print.attribute.PrintRequestAttributeSet;
/*      */ import javax.print.attribute.standard.Chromaticity;
/*      */ import javax.print.attribute.standard.Copies;
/*      */ import javax.print.attribute.standard.Destination;
/*      */ import javax.print.attribute.standard.DialogTypeSelection;
/*      */ import javax.print.attribute.standard.JobName;
/*      */ import javax.print.attribute.standard.Media;
/*      */ import javax.print.attribute.standard.MediaSize;
/*      */ import javax.print.attribute.standard.MediaSizeName;
/*      */ import javax.print.attribute.standard.OrientationRequested;
/*      */ import javax.print.attribute.standard.PageRanges;
/*      */ import javax.print.attribute.standard.PrintQuality;
/*      */ import javax.print.attribute.standard.SheetCollate;
/*      */ import javax.print.attribute.standard.Sides;
/*      */ 
/*      */ public class PrintJob2D extends PrintJob
/*      */   implements Printable, Runnable
/*      */ {
/*   86 */   private static final PageAttributes.MediaType[] SIZES = { PageAttributes.MediaType.ISO_4A0, PageAttributes.MediaType.ISO_2A0, PageAttributes.MediaType.ISO_A0, PageAttributes.MediaType.ISO_A1, PageAttributes.MediaType.ISO_A2, PageAttributes.MediaType.ISO_A3, PageAttributes.MediaType.ISO_A4, PageAttributes.MediaType.ISO_A5, PageAttributes.MediaType.ISO_A6, PageAttributes.MediaType.ISO_A7, PageAttributes.MediaType.ISO_A8, PageAttributes.MediaType.ISO_A9, PageAttributes.MediaType.ISO_A10, PageAttributes.MediaType.ISO_B0, PageAttributes.MediaType.ISO_B1, PageAttributes.MediaType.ISO_B2, PageAttributes.MediaType.ISO_B3, PageAttributes.MediaType.ISO_B4, PageAttributes.MediaType.ISO_B5, PageAttributes.MediaType.ISO_B6, PageAttributes.MediaType.ISO_B7, PageAttributes.MediaType.ISO_B8, PageAttributes.MediaType.ISO_B9, PageAttributes.MediaType.ISO_B10, PageAttributes.MediaType.JIS_B0, PageAttributes.MediaType.JIS_B1, PageAttributes.MediaType.JIS_B2, PageAttributes.MediaType.JIS_B3, PageAttributes.MediaType.JIS_B4, PageAttributes.MediaType.JIS_B5, PageAttributes.MediaType.JIS_B6, PageAttributes.MediaType.JIS_B7, PageAttributes.MediaType.JIS_B8, PageAttributes.MediaType.JIS_B9, PageAttributes.MediaType.JIS_B10, PageAttributes.MediaType.ISO_C0, PageAttributes.MediaType.ISO_C1, PageAttributes.MediaType.ISO_C2, PageAttributes.MediaType.ISO_C3, PageAttributes.MediaType.ISO_C4, PageAttributes.MediaType.ISO_C5, PageAttributes.MediaType.ISO_C6, PageAttributes.MediaType.ISO_C7, PageAttributes.MediaType.ISO_C8, PageAttributes.MediaType.ISO_C9, PageAttributes.MediaType.ISO_C10, PageAttributes.MediaType.ISO_DESIGNATED_LONG, PageAttributes.MediaType.EXECUTIVE, PageAttributes.MediaType.FOLIO, PageAttributes.MediaType.INVOICE, PageAttributes.MediaType.LEDGER, PageAttributes.MediaType.NA_LETTER, PageAttributes.MediaType.NA_LEGAL, PageAttributes.MediaType.QUARTO, PageAttributes.MediaType.A, PageAttributes.MediaType.B, PageAttributes.MediaType.C, PageAttributes.MediaType.D, PageAttributes.MediaType.E, PageAttributes.MediaType.NA_10X15_ENVELOPE, PageAttributes.MediaType.NA_10X14_ENVELOPE, PageAttributes.MediaType.NA_10X13_ENVELOPE, PageAttributes.MediaType.NA_9X12_ENVELOPE, PageAttributes.MediaType.NA_9X11_ENVELOPE, PageAttributes.MediaType.NA_7X9_ENVELOPE, PageAttributes.MediaType.NA_6X9_ENVELOPE, PageAttributes.MediaType.NA_NUMBER_9_ENVELOPE, PageAttributes.MediaType.NA_NUMBER_10_ENVELOPE, PageAttributes.MediaType.NA_NUMBER_11_ENVELOPE, PageAttributes.MediaType.NA_NUMBER_12_ENVELOPE, PageAttributes.MediaType.NA_NUMBER_14_ENVELOPE, PageAttributes.MediaType.INVITE_ENVELOPE, PageAttributes.MediaType.ITALY_ENVELOPE, PageAttributes.MediaType.MONARCH_ENVELOPE, PageAttributes.MediaType.PERSONAL_ENVELOPE };
/*      */ 
/*  120 */   private static final MediaSizeName[] JAVAXSIZES = { null, null, MediaSizeName.ISO_A0, MediaSizeName.ISO_A1, MediaSizeName.ISO_A2, MediaSizeName.ISO_A3, MediaSizeName.ISO_A4, MediaSizeName.ISO_A5, MediaSizeName.ISO_A6, MediaSizeName.ISO_A7, MediaSizeName.ISO_A8, MediaSizeName.ISO_A9, MediaSizeName.ISO_A10, MediaSizeName.ISO_B0, MediaSizeName.ISO_B1, MediaSizeName.ISO_B2, MediaSizeName.ISO_B3, MediaSizeName.ISO_B4, MediaSizeName.ISO_B5, MediaSizeName.ISO_B6, MediaSizeName.ISO_B7, MediaSizeName.ISO_B8, MediaSizeName.ISO_B9, MediaSizeName.ISO_B10, MediaSizeName.JIS_B0, MediaSizeName.JIS_B1, MediaSizeName.JIS_B2, MediaSizeName.JIS_B3, MediaSizeName.JIS_B4, MediaSizeName.JIS_B5, MediaSizeName.JIS_B6, MediaSizeName.JIS_B7, MediaSizeName.JIS_B8, MediaSizeName.JIS_B9, MediaSizeName.JIS_B10, MediaSizeName.ISO_C0, MediaSizeName.ISO_C1, MediaSizeName.ISO_C2, MediaSizeName.ISO_C3, MediaSizeName.ISO_C4, MediaSizeName.ISO_C5, MediaSizeName.ISO_C6, null, null, null, null, MediaSizeName.ISO_DESIGNATED_LONG, MediaSizeName.EXECUTIVE, MediaSizeName.FOLIO, MediaSizeName.INVOICE, MediaSizeName.LEDGER, MediaSizeName.NA_LETTER, MediaSizeName.NA_LEGAL, MediaSizeName.QUARTO, MediaSizeName.A, MediaSizeName.B, MediaSizeName.C, MediaSizeName.D, MediaSizeName.E, MediaSizeName.NA_10X15_ENVELOPE, MediaSizeName.NA_10X14_ENVELOPE, MediaSizeName.NA_10X13_ENVELOPE, MediaSizeName.NA_9X12_ENVELOPE, MediaSizeName.NA_9X11_ENVELOPE, MediaSizeName.NA_7X9_ENVELOPE, MediaSizeName.NA_6X9_ENVELOPE, MediaSizeName.NA_NUMBER_9_ENVELOPE, MediaSizeName.NA_NUMBER_10_ENVELOPE, MediaSizeName.NA_NUMBER_11_ENVELOPE, MediaSizeName.NA_NUMBER_12_ENVELOPE, MediaSizeName.NA_NUMBER_14_ENVELOPE, null, MediaSizeName.ITALY_ENVELOPE, MediaSizeName.MONARCH_ENVELOPE, MediaSizeName.PERSONAL_ENVELOPE };
/*      */ 
/*  156 */   private static final int[] WIDTHS = { 4768, 3370, 2384, 1684, 1191, 842, 595, 420, 298, 210, 147, 105, 74, 2835, 2004, 1417, 1001, 709, 499, 354, 249, 176, 125, 88, 2920, 2064, 1460, 1032, 729, 516, 363, 258, 181, 128, 91, 2599, 1837, 1298, 918, 649, 459, 323, 230, 162, 113, 79, 312, 522, 612, 396, 792, 612, 612, 609, 612, 792, 1224, 1584, 2448, 720, 720, 720, 648, 648, 504, 432, 279, 297, 324, 342, 360, 624, 312, 279, 261 };
/*      */ 
/*  181 */   private static final int[] LENGTHS = { 6741, 4768, 3370, 2384, 1684, 1191, 842, 595, 420, 298, 210, 147, 105, 4008, 2835, 2004, 1417, 1001, 729, 499, 354, 249, 176, 125, 4127, 2920, 2064, 1460, 1032, 729, 516, 363, 258, 181, 128, 3677, 2599, 1837, 1298, 918, 649, 459, 323, 230, 162, 113, 624, 756, 936, 612, 1224, 792, 1008, 780, 792, 1224, 1584, 2448, 3168, 1080, 1008, 936, 864, 792, 648, 648, 639, 684, 747, 792, 828, 624, 652, 540, 468 };
/*      */   private Frame frame;
/*  209 */   private String docTitle = "";
/*      */   private JobAttributes jobAttributes;
/*      */   private PageAttributes pageAttributes;
/*      */   private PrintRequestAttributeSet attributes;
/*      */   private PrinterJob printerJob;
/*      */   private PageFormat pageFormat;
/*  238 */   private MessageQ graphicsToBeDrawn = new MessageQ("tobedrawn");
/*      */ 
/*  248 */   private MessageQ graphicsDrawn = new MessageQ("drawn");
/*      */   private Graphics2D currentGraphics;
/*  261 */   private int pageIndex = -1;
/*      */   private static final String DEST_PROP = "awt.print.destination";
/*      */   private static final String PRINTER = "printer";
/*      */   private static final String FILE = "file";
/*      */   private static final String PRINTER_PROP = "awt.print.printer";
/*      */   private static final String FILENAME_PROP = "awt.print.fileName";
/*      */   private static final String NUMCOPIES_PROP = "awt.print.numCopies";
/*      */   private static final String OPTIONS_PROP = "awt.print.options";
/*      */   private static final String ORIENT_PROP = "awt.print.orientation";
/*      */   private static final String PORTRAIT = "portrait";
/*      */   private static final String LANDSCAPE = "landscape";
/*      */   private static final String PAPERSIZE_PROP = "awt.print.paperSize";
/*      */   private static final String LETTER = "letter";
/*      */   private static final String LEGAL = "legal";
/*      */   private static final String EXECUTIVE = "executive";
/*      */   private static final String A4 = "a4";
/*      */   private Properties props;
/*  289 */   private String options = "";
/*      */   private Thread printerJobThread;
/*      */ 
/*      */   public PrintJob2D(Frame paramFrame, String paramString, Properties paramProperties)
/*      */   {
/*  299 */     this.props = paramProperties;
/*  300 */     this.jobAttributes = new JobAttributes();
/*  301 */     this.pageAttributes = new PageAttributes();
/*  302 */     translateInputProps();
/*  303 */     initPrintJob2D(paramFrame, paramString, this.jobAttributes, this.pageAttributes);
/*      */   }
/*      */ 
/*      */   public PrintJob2D(Frame paramFrame, String paramString, JobAttributes paramJobAttributes, PageAttributes paramPageAttributes)
/*      */   {
/*  310 */     initPrintJob2D(paramFrame, paramString, paramJobAttributes, paramPageAttributes);
/*      */   }
/*      */ 
/*      */   private void initPrintJob2D(Frame paramFrame, String paramString, JobAttributes paramJobAttributes, PageAttributes paramPageAttributes)
/*      */   {
/*  317 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  318 */     if (localSecurityManager != null) {
/*  319 */       localSecurityManager.checkPrintJobAccess();
/*      */     }
/*      */ 
/*  322 */     if ((paramFrame == null) && ((paramJobAttributes == null) || (paramJobAttributes.getDialog() == JobAttributes.DialogType.NATIVE)))
/*      */     {
/*  325 */       throw new NullPointerException("Frame must not be null");
/*      */     }
/*  327 */     this.frame = paramFrame;
/*      */ 
/*  329 */     this.docTitle = (paramString == null ? "" : paramString);
/*  330 */     this.jobAttributes = (paramJobAttributes != null ? paramJobAttributes : new JobAttributes());
/*      */ 
/*  332 */     this.pageAttributes = (paramPageAttributes != null ? paramPageAttributes : new PageAttributes());
/*      */ 
/*  336 */     int[][] arrayOfInt = this.jobAttributes.getPageRanges();
/*  337 */     int i = arrayOfInt[0][0];
/*  338 */     int j = arrayOfInt[(arrayOfInt.length - 1)][1];
/*  339 */     this.jobAttributes.setPageRanges(new int[][] { { i, j } });
/*      */ 
/*  342 */     this.jobAttributes.setToPage(j);
/*  343 */     this.jobAttributes.setFromPage(i);
/*      */ 
/*  347 */     int[] arrayOfInt1 = this.pageAttributes.getPrinterResolution();
/*  348 */     if (arrayOfInt1[0] != arrayOfInt1[1]) {
/*  349 */       throw new IllegalArgumentException("Differing cross feed and feed resolutions not supported.");
/*      */     }
/*      */ 
/*  354 */     JobAttributes.DestinationType localDestinationType = this.jobAttributes.getDestination();
/*  355 */     if (localDestinationType == JobAttributes.DestinationType.FILE) {
/*  356 */       throwPrintToFile();
/*      */ 
/*  359 */       String str = paramJobAttributes.getFileName();
/*  360 */       if ((str != null) && (paramJobAttributes.getDialog() == JobAttributes.DialogType.NONE))
/*      */       {
/*  363 */         File localFile1 = new File(str);
/*      */         try
/*      */         {
/*  367 */           if (localFile1.createNewFile())
/*  368 */             localFile1.delete();
/*      */         }
/*      */         catch (IOException localIOException) {
/*  371 */           throw new IllegalArgumentException("Cannot write to file:" + str);
/*      */         }
/*      */         catch (SecurityException localSecurityException)
/*      */         {
/*      */         }
/*      */ 
/*  380 */         File localFile2 = localFile1.getParentFile();
/*  381 */         if (((localFile1.exists()) && ((!localFile1.isFile()) || (!localFile1.canWrite()))) || ((localFile2 != null) && ((!localFile2.exists()) || ((localFile2.exists()) && (!localFile2.canWrite())))))
/*      */         {
/*  385 */           throw new IllegalArgumentException("Cannot write to file:" + str);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean printDialog()
/*      */   {
/*  394 */     boolean bool = false;
/*      */ 
/*  396 */     this.printerJob = PrinterJob.getPrinterJob();
/*  397 */     if (this.printerJob == null) {
/*  398 */       return false;
/*      */     }
/*  400 */     JobAttributes.DialogType localDialogType = this.jobAttributes.getDialog();
/*  401 */     PrintService localPrintService = this.printerJob.getPrintService();
/*  402 */     if ((localPrintService == null) && (localDialogType == JobAttributes.DialogType.NONE)) {
/*  403 */       return false;
/*      */     }
/*  405 */     copyAttributes(localPrintService);
/*      */ 
/*  407 */     JobAttributes.DefaultSelectionType localDefaultSelectionType = this.jobAttributes.getDefaultSelection();
/*      */ 
/*  409 */     if (localDefaultSelectionType == JobAttributes.DefaultSelectionType.RANGE)
/*  410 */       this.attributes.add(SunPageSelection.RANGE);
/*  411 */     else if (localDefaultSelectionType == JobAttributes.DefaultSelectionType.SELECTION)
/*  412 */       this.attributes.add(SunPageSelection.SELECTION);
/*      */     else {
/*  414 */       this.attributes.add(SunPageSelection.ALL);
/*      */     }
/*      */ 
/*  417 */     if (this.frame != null) {
/*  418 */       this.attributes.add(new DialogOwner(this.frame));
/*      */     }
/*      */ 
/*  421 */     if (localDialogType == JobAttributes.DialogType.NONE) {
/*  422 */       bool = true;
/*      */     } else {
/*  424 */       if (localDialogType == JobAttributes.DialogType.NATIVE)
/*  425 */         this.attributes.add(DialogTypeSelection.NATIVE);
/*      */       else {
/*  427 */         this.attributes.add(DialogTypeSelection.COMMON);
/*      */       }
/*  429 */       if ((bool = this.printerJob.printDialog(this.attributes))) {
/*  430 */         if (localPrintService == null)
/*      */         {
/*  434 */           localPrintService = this.printerJob.getPrintService();
/*  435 */           if (localPrintService == null) {
/*  436 */             return false;
/*      */           }
/*      */         }
/*  439 */         updateAttributes();
/*  440 */         translateOutputProps();
/*      */       }
/*      */     }
/*      */ 
/*  444 */     if (bool)
/*      */     {
/*  446 */       JobName localJobName = (JobName)this.attributes.get(JobName.class);
/*  447 */       if (localJobName != null) {
/*  448 */         this.printerJob.setJobName(localJobName.toString());
/*      */       }
/*      */ 
/*  451 */       this.pageFormat = new PageFormat();
/*      */ 
/*  453 */       Media localMedia = (Media)this.attributes.get(Media.class);
/*  454 */       MediaSize localMediaSize = null;
/*  455 */       if ((localMedia != null) && ((localMedia instanceof MediaSizeName))) {
/*  456 */         localMediaSize = MediaSize.getMediaSizeForName((MediaSizeName)localMedia);
/*      */       }
/*      */ 
/*  459 */       Paper localPaper = this.pageFormat.getPaper();
/*  460 */       if (localMediaSize != null) {
/*  461 */         localPaper.setSize(localMediaSize.getX(25400) * 72.0D, localMediaSize.getY(25400) * 72.0D);
/*      */       }
/*      */ 
/*  465 */       if (this.pageAttributes.getOrigin() == PageAttributes.OriginType.PRINTABLE)
/*      */       {
/*  467 */         localPaper.setImageableArea(18.0D, 18.0D, localPaper.getWidth() - 36.0D, localPaper.getHeight() - 36.0D);
/*      */       }
/*      */       else
/*      */       {
/*  471 */         localPaper.setImageableArea(0.0D, 0.0D, localPaper.getWidth(), localPaper.getHeight());
/*      */       }
/*      */ 
/*  474 */       this.pageFormat.setPaper(localPaper);
/*      */ 
/*  476 */       OrientationRequested localOrientationRequested = (OrientationRequested)this.attributes.get(OrientationRequested.class);
/*      */ 
/*  478 */       if ((localOrientationRequested != null) && (localOrientationRequested == OrientationRequested.REVERSE_LANDSCAPE))
/*      */       {
/*  480 */         this.pageFormat.setOrientation(2);
/*  481 */       } else if (localOrientationRequested == OrientationRequested.LANDSCAPE)
/*  482 */         this.pageFormat.setOrientation(0);
/*      */       else {
/*  484 */         this.pageFormat.setOrientation(1);
/*      */       }
/*      */ 
/*  487 */       this.printerJob.setPrintable(this, this.pageFormat);
/*      */     }
/*      */ 
/*  491 */     return bool;
/*      */   }
/*      */ 
/*      */   private void updateAttributes() {
/*  495 */     Copies localCopies = (Copies)this.attributes.get(Copies.class);
/*  496 */     this.jobAttributes.setCopies(localCopies.getValue());
/*      */ 
/*  498 */     SunPageSelection localSunPageSelection = (SunPageSelection)this.attributes.get(SunPageSelection.class);
/*      */ 
/*  500 */     if (localSunPageSelection == SunPageSelection.RANGE)
/*  501 */       this.jobAttributes.setDefaultSelection(JobAttributes.DefaultSelectionType.RANGE);
/*  502 */     else if (localSunPageSelection == SunPageSelection.SELECTION)
/*  503 */       this.jobAttributes.setDefaultSelection(JobAttributes.DefaultSelectionType.SELECTION);
/*      */     else {
/*  505 */       this.jobAttributes.setDefaultSelection(JobAttributes.DefaultSelectionType.ALL);
/*      */     }
/*      */ 
/*  508 */     Destination localDestination = (Destination)this.attributes.get(Destination.class);
/*  509 */     if (localDestination != null) {
/*  510 */       this.jobAttributes.setDestination(JobAttributes.DestinationType.FILE);
/*  511 */       this.jobAttributes.setFileName(localDestination.getURI().getPath());
/*      */     } else {
/*  513 */       this.jobAttributes.setDestination(JobAttributes.DestinationType.PRINTER);
/*      */     }
/*      */ 
/*  516 */     PrintService localPrintService = this.printerJob.getPrintService();
/*  517 */     if (localPrintService != null) {
/*  518 */       this.jobAttributes.setPrinter(localPrintService.getName());
/*      */     }
/*      */ 
/*  521 */     PageRanges localPageRanges = (PageRanges)this.attributes.get(PageRanges.class);
/*  522 */     int[][] arrayOfInt = localPageRanges.getMembers();
/*  523 */     this.jobAttributes.setPageRanges(arrayOfInt);
/*      */ 
/*  525 */     SheetCollate localSheetCollate = (SheetCollate)this.attributes.get(SheetCollate.class);
/*      */ 
/*  527 */     if (localSheetCollate == SheetCollate.COLLATED) {
/*  528 */       this.jobAttributes.setMultipleDocumentHandling(JobAttributes.MultipleDocumentHandlingType.SEPARATE_DOCUMENTS_COLLATED_COPIES);
/*      */     }
/*      */     else {
/*  531 */       this.jobAttributes.setMultipleDocumentHandling(JobAttributes.MultipleDocumentHandlingType.SEPARATE_DOCUMENTS_UNCOLLATED_COPIES);
/*      */     }
/*      */ 
/*  535 */     Sides localSides = (Sides)this.attributes.get(Sides.class);
/*  536 */     if (localSides == Sides.TWO_SIDED_LONG_EDGE)
/*  537 */       this.jobAttributes.setSides(JobAttributes.SidesType.TWO_SIDED_LONG_EDGE);
/*  538 */     else if (localSides == Sides.TWO_SIDED_SHORT_EDGE)
/*  539 */       this.jobAttributes.setSides(JobAttributes.SidesType.TWO_SIDED_SHORT_EDGE);
/*      */     else {
/*  541 */       this.jobAttributes.setSides(JobAttributes.SidesType.ONE_SIDED);
/*      */     }
/*      */ 
/*  546 */     Chromaticity localChromaticity = (Chromaticity)this.attributes.get(Chromaticity.class);
/*      */ 
/*  548 */     if (localChromaticity == Chromaticity.COLOR)
/*  549 */       this.pageAttributes.setColor(PageAttributes.ColorType.COLOR);
/*      */     else {
/*  551 */       this.pageAttributes.setColor(PageAttributes.ColorType.MONOCHROME);
/*      */     }
/*      */ 
/*  554 */     OrientationRequested localOrientationRequested = (OrientationRequested)this.attributes.get(OrientationRequested.class);
/*      */ 
/*  556 */     if (localOrientationRequested == OrientationRequested.LANDSCAPE) {
/*  557 */       this.pageAttributes.setOrientationRequested(PageAttributes.OrientationRequestedType.LANDSCAPE);
/*      */     }
/*      */     else {
/*  560 */       this.pageAttributes.setOrientationRequested(PageAttributes.OrientationRequestedType.PORTRAIT);
/*      */     }
/*      */ 
/*  564 */     PrintQuality localPrintQuality = (PrintQuality)this.attributes.get(PrintQuality.class);
/*  565 */     if (localPrintQuality == PrintQuality.DRAFT)
/*  566 */       this.pageAttributes.setPrintQuality(PageAttributes.PrintQualityType.DRAFT);
/*  567 */     else if (localPrintQuality == PrintQuality.HIGH)
/*  568 */       this.pageAttributes.setPrintQuality(PageAttributes.PrintQualityType.HIGH);
/*      */     else {
/*  570 */       this.pageAttributes.setPrintQuality(PageAttributes.PrintQualityType.NORMAL);
/*      */     }
/*      */ 
/*  573 */     Media localMedia = (Media)this.attributes.get(Media.class);
/*  574 */     if ((localMedia != null) && ((localMedia instanceof MediaSizeName))) {
/*  575 */       PageAttributes.MediaType localMediaType = unMapMedia((MediaSizeName)localMedia);
/*      */ 
/*  577 */       if (localMediaType != null) {
/*  578 */         this.pageAttributes.setMedia(localMediaType);
/*      */       }
/*      */     }
/*  581 */     debugPrintAttributes(false, false);
/*      */   }
/*      */ 
/*      */   private void debugPrintAttributes(boolean paramBoolean1, boolean paramBoolean2) {
/*  585 */     if (paramBoolean1) {
/*  586 */       System.out.println("new Attributes\ncopies = " + this.jobAttributes.getCopies() + "\nselection = " + this.jobAttributes.getDefaultSelection() + "\ndest " + this.jobAttributes.getDestination() + "\nfile " + this.jobAttributes.getFileName() + "\nfromPage " + this.jobAttributes.getFromPage() + "\ntoPage " + this.jobAttributes.getToPage() + "\ncollation " + this.jobAttributes.getMultipleDocumentHandling() + "\nPrinter " + this.jobAttributes.getPrinter() + "\nSides2 " + this.jobAttributes.getSides());
/*      */     }
/*      */ 
/*  601 */     if (paramBoolean2)
/*  602 */       System.out.println("new Attributes\ncolor = " + this.pageAttributes.getColor() + "\norientation = " + this.pageAttributes.getOrientationRequested() + "\nquality " + this.pageAttributes.getPrintQuality() + "\nMedia2 " + this.pageAttributes.getMedia());
/*      */   }
/*      */ 
/*      */   private void copyAttributes(PrintService paramPrintService)
/*      */   {
/*  627 */     this.attributes = new HashPrintRequestAttributeSet();
/*  628 */     this.attributes.add(new JobName(this.docTitle, null));
/*  629 */     PrintService localPrintService = paramPrintService;
/*      */ 
/*  631 */     String str = this.jobAttributes.getPrinter();
/*  632 */     if ((str != null) && (str != "") && (!str.equals(localPrintService.getName())))
/*      */     {
/*  636 */       localObject1 = PrinterJob.lookupPrintServices();
/*      */       try {
/*  638 */         for (int i = 0; i < localObject1.length; i++)
/*  639 */           if (str.equals(localObject1[i].getName())) {
/*  640 */             this.printerJob.setPrintService(localObject1[i]);
/*  641 */             localPrintService = localObject1[i];
/*  642 */             break;
/*      */           }
/*      */       }
/*      */       catch (PrinterException localPrinterException)
/*      */       {
/*      */       }
/*      */     }
/*  649 */     Object localObject1 = this.jobAttributes.getDestination();
/*  650 */     if ((localObject1 == JobAttributes.DestinationType.FILE) && (localPrintService.isAttributeCategorySupported(Destination.class)))
/*      */     {
/*  653 */       localObject2 = this.jobAttributes.getFileName();
/*      */ 
/*  656 */       if ((localObject2 == null) && ((localObject3 = (Destination)localPrintService.getDefaultAttributeValue(Destination.class)) != null))
/*      */       {
/*  658 */         this.attributes.add((Attribute)localObject3);
/*      */       } else {
/*  660 */         localObject4 = null;
/*      */         try {
/*  662 */           if (localObject2 != null) {
/*  663 */             if (((String)localObject2).equals("")) {
/*  664 */               localObject2 = ".";
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  671 */             localObject2 = "out.prn";
/*      */           }
/*  673 */           localObject4 = new File((String)localObject2).toURI();
/*      */         }
/*      */         catch (SecurityException localSecurityException)
/*      */         {
/*      */           try
/*      */           {
/*  679 */             localObject2 = ((String)localObject2).replace('\\', '/');
/*  680 */             localObject4 = new URI("file:" + (String)localObject2);
/*      */           } catch (URISyntaxException localURISyntaxException) {
/*      */           }
/*      */         }
/*  684 */         if (localObject4 != null) {
/*  685 */           this.attributes.add(new Destination((URI)localObject4));
/*      */         }
/*      */       }
/*      */     }
/*  689 */     this.attributes.add(new SunMinMaxPage(this.jobAttributes.getMinPage(), this.jobAttributes.getMaxPage()));
/*      */ 
/*  691 */     Object localObject2 = this.jobAttributes.getSides();
/*  692 */     if (localObject2 == JobAttributes.SidesType.TWO_SIDED_LONG_EDGE)
/*  693 */       this.attributes.add(Sides.TWO_SIDED_LONG_EDGE);
/*  694 */     else if (localObject2 == JobAttributes.SidesType.TWO_SIDED_SHORT_EDGE)
/*  695 */       this.attributes.add(Sides.TWO_SIDED_SHORT_EDGE);
/*  696 */     else if (localObject2 == JobAttributes.SidesType.ONE_SIDED) {
/*  697 */       this.attributes.add(Sides.ONE_SIDED);
/*      */     }
/*      */ 
/*  700 */     Object localObject3 = this.jobAttributes.getMultipleDocumentHandling();
/*      */ 
/*  702 */     if (localObject3 == JobAttributes.MultipleDocumentHandlingType.SEPARATE_DOCUMENTS_COLLATED_COPIES)
/*      */     {
/*  704 */       this.attributes.add(SheetCollate.COLLATED);
/*      */     }
/*  706 */     else this.attributes.add(SheetCollate.UNCOLLATED);
/*      */ 
/*  709 */     this.attributes.add(new Copies(this.jobAttributes.getCopies()));
/*      */ 
/*  711 */     this.attributes.add(new PageRanges(this.jobAttributes.getFromPage(), this.jobAttributes.getToPage()));
/*      */ 
/*  714 */     if (this.pageAttributes.getColor() == PageAttributes.ColorType.COLOR)
/*  715 */       this.attributes.add(Chromaticity.COLOR);
/*      */     else {
/*  717 */       this.attributes.add(Chromaticity.MONOCHROME);
/*      */     }
/*      */ 
/*  720 */     this.pageFormat = this.printerJob.defaultPage();
/*  721 */     if (this.pageAttributes.getOrientationRequested() == PageAttributes.OrientationRequestedType.LANDSCAPE)
/*      */     {
/*  723 */       this.pageFormat.setOrientation(0);
/*  724 */       this.attributes.add(OrientationRequested.LANDSCAPE);
/*      */     } else {
/*  726 */       this.pageFormat.setOrientation(1);
/*  727 */       this.attributes.add(OrientationRequested.PORTRAIT);
/*      */     }
/*      */ 
/*  730 */     Object localObject4 = this.pageAttributes.getMedia();
/*  731 */     MediaSizeName localMediaSizeName = mapMedia((PageAttributes.MediaType)localObject4);
/*  732 */     if (localMediaSizeName != null) {
/*  733 */       this.attributes.add(localMediaSizeName);
/*      */     }
/*      */ 
/*  736 */     PageAttributes.PrintQualityType localPrintQualityType = this.pageAttributes.getPrintQuality();
/*      */ 
/*  738 */     if (localPrintQualityType == PageAttributes.PrintQualityType.DRAFT)
/*  739 */       this.attributes.add(PrintQuality.DRAFT);
/*  740 */     else if (localPrintQualityType == PageAttributes.PrintQualityType.NORMAL)
/*  741 */       this.attributes.add(PrintQuality.NORMAL);
/*  742 */     else if (localPrintQualityType == PageAttributes.PrintQualityType.HIGH)
/*  743 */       this.attributes.add(PrintQuality.HIGH);
/*      */   }
/*      */ 
/*      */   public Graphics getGraphics()
/*      */   {
/*  756 */     ProxyPrintGraphics localProxyPrintGraphics = null;
/*      */ 
/*  758 */     synchronized (this) {
/*  759 */       this.pageIndex += 1;
/*      */ 
/*  764 */       if ((this.pageIndex == 0) && (!this.graphicsToBeDrawn.isClosed()))
/*      */       {
/*  773 */         startPrinterJobThread();
/*      */       }
/*      */ 
/*  776 */       notify();
/*      */     }
/*      */ 
/*  784 */     if (this.currentGraphics != null) {
/*  785 */       this.graphicsDrawn.append(this.currentGraphics);
/*  786 */       this.currentGraphics = null;
/*      */     }
/*      */ 
/*  793 */     this.currentGraphics = this.graphicsToBeDrawn.pop();
/*      */ 
/*  795 */     if ((this.currentGraphics instanceof PeekGraphics)) {
/*  796 */       ((PeekGraphics)this.currentGraphics).setAWTDrawingOnly();
/*  797 */       this.graphicsDrawn.append(this.currentGraphics);
/*  798 */       this.currentGraphics = this.graphicsToBeDrawn.pop();
/*      */     }
/*      */ 
/*  802 */     if (this.currentGraphics != null)
/*      */     {
/*  812 */       this.currentGraphics.translate(this.pageFormat.getImageableX(), this.pageFormat.getImageableY());
/*      */ 
/*  816 */       double d = 72.0D / getPageResolutionInternal();
/*  817 */       this.currentGraphics.scale(d, d);
/*      */ 
/*  827 */       localProxyPrintGraphics = new ProxyPrintGraphics(this.currentGraphics.create(), this);
/*      */     }
/*      */ 
/*  832 */     return localProxyPrintGraphics;
/*      */   }
/*      */ 
/*      */   public Dimension getPageDimension()
/*      */   {
/*      */     double d1;
/*      */     double d2;
/*  844 */     if ((this.pageAttributes != null) && (this.pageAttributes.getOrigin() == PageAttributes.OriginType.PRINTABLE))
/*      */     {
/*  846 */       d1 = this.pageFormat.getImageableWidth();
/*  847 */       d2 = this.pageFormat.getImageableHeight();
/*      */     } else {
/*  849 */       d1 = this.pageFormat.getWidth();
/*  850 */       d2 = this.pageFormat.getHeight();
/*      */     }
/*  852 */     double d3 = getPageResolutionInternal() / 72.0D;
/*  853 */     return new Dimension((int)(d1 * d3), (int)(d2 * d3));
/*      */   }
/*      */ 
/*      */   private double getPageResolutionInternal() {
/*  857 */     if (this.pageAttributes != null) {
/*  858 */       int[] arrayOfInt = this.pageAttributes.getPrinterResolution();
/*  859 */       if (arrayOfInt[2] == 3) {
/*  860 */         return arrayOfInt[0];
/*      */       }
/*  862 */       return arrayOfInt[0] * 2.54D;
/*      */     }
/*      */ 
/*  865 */     return 72.0D;
/*      */   }
/*      */ 
/*      */   public int getPageResolution()
/*      */   {
/*  875 */     return (int)getPageResolutionInternal();
/*      */   }
/*      */ 
/*      */   public boolean lastPageFirst()
/*      */   {
/*  882 */     return false;
/*      */   }
/*      */ 
/*      */   public synchronized void end()
/*      */   {
/*  893 */     this.graphicsToBeDrawn.close();
/*      */ 
/*  902 */     if (this.currentGraphics != null) {
/*  903 */       this.graphicsDrawn.append(this.currentGraphics);
/*      */     }
/*  905 */     this.graphicsDrawn.closeWhenEmpty();
/*      */ 
/*  911 */     if ((this.printerJobThread != null) && (this.printerJobThread.isAlive()))
/*      */       try {
/*  913 */         this.printerJobThread.join();
/*      */       }
/*      */       catch (InterruptedException localInterruptedException)
/*      */       {
/*      */       }
/*      */   }
/*      */ 
/*      */   public void finalize()
/*      */   {
/*  924 */     end();
/*      */   }
/*      */ 
/*      */   public int print(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt)
/*      */     throws PrinterException
/*      */   {
/*  970 */     this.graphicsToBeDrawn.append((Graphics2D)paramGraphics);
/*      */     int i;
/*  979 */     if (this.graphicsDrawn.pop() != null)
/*  980 */       i = 0;
/*      */     else {
/*  982 */       i = 1;
/*      */     }
/*      */ 
/*  985 */     return i;
/*      */   }
/*      */ 
/*      */   private void startPrinterJobThread()
/*      */   {
/*  990 */     this.printerJobThread = new Thread(this, "printerJobThread");
/*  991 */     this.printerJobThread.start();
/*      */   }
/*      */ 
/*      */   public void run()
/*      */   {
/*      */     try
/*      */     {
/*  998 */       this.printerJob.print(this.attributes);
/*      */     }
/*      */     catch (PrinterException localPrinterException)
/*      */     {
/*      */     }
/*      */ 
/* 1006 */     this.graphicsToBeDrawn.closeWhenEmpty();
/* 1007 */     this.graphicsDrawn.close();
/*      */   }
/*      */ 
/*      */   private static int[] getSize(PageAttributes.MediaType paramMediaType)
/*      */   {
/* 1081 */     int[] arrayOfInt = new int[2];
/* 1082 */     arrayOfInt[0] = 612;
/* 1083 */     arrayOfInt[1] = 792;
/*      */ 
/* 1085 */     for (int i = 0; i < SIZES.length; i++) {
/* 1086 */       if (SIZES[i] == paramMediaType) {
/* 1087 */         arrayOfInt[0] = WIDTHS[i];
/* 1088 */         arrayOfInt[1] = LENGTHS[i];
/* 1089 */         break;
/*      */       }
/*      */     }
/* 1092 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public static MediaSizeName mapMedia(PageAttributes.MediaType paramMediaType) {
/* 1096 */     Object localObject = null;
/*      */ 
/* 1100 */     int i = Math.min(SIZES.length, JAVAXSIZES.length);
/*      */ 
/* 1102 */     for (int j = 0; j < i; j++) {
/* 1103 */       if (SIZES[j] == paramMediaType) {
/* 1104 */         if ((JAVAXSIZES[j] != null) && (MediaSize.getMediaSizeForName(JAVAXSIZES[j]) != null))
/*      */         {
/* 1106 */           localObject = JAVAXSIZES[j];
/* 1107 */           break;
/*      */         }
/*      */ 
/* 1110 */         localObject = new CustomMediaSizeName(SIZES[j].toString());
/*      */ 
/* 1112 */         float f1 = (float)Math.rint(WIDTHS[j] / 72.0D);
/* 1113 */         float f2 = (float)Math.rint(LENGTHS[j] / 72.0D);
/* 1114 */         if ((f1 <= 0.0D) || (f2 <= 0.0D)) {
/*      */           break;
/*      */         }
/* 1117 */         new MediaSize(f1, f2, 25400, (MediaSizeName)localObject); break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1124 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static PageAttributes.MediaType unMapMedia(MediaSizeName paramMediaSizeName)
/*      */   {
/* 1129 */     PageAttributes.MediaType localMediaType = null;
/*      */ 
/* 1133 */     int i = Math.min(SIZES.length, JAVAXSIZES.length);
/*      */ 
/* 1135 */     for (int j = 0; j < i; j++) {
/* 1136 */       if ((JAVAXSIZES[j] == paramMediaSizeName) && 
/* 1137 */         (SIZES[j] != null)) {
/* 1138 */         localMediaType = SIZES[j];
/* 1139 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1143 */     return localMediaType;
/*      */   }
/*      */ 
/*      */   private void translateInputProps() {
/* 1147 */     if (this.props == null) {
/* 1148 */       return;
/*      */     }
/*      */ 
/* 1153 */     String str = this.props.getProperty("awt.print.destination");
/* 1154 */     if (str != null) {
/* 1155 */       if (str.equals("printer"))
/* 1156 */         this.jobAttributes.setDestination(JobAttributes.DestinationType.PRINTER);
/* 1157 */       else if (str.equals("file")) {
/* 1158 */         this.jobAttributes.setDestination(JobAttributes.DestinationType.FILE);
/*      */       }
/*      */     }
/* 1161 */     str = this.props.getProperty("awt.print.printer");
/* 1162 */     if (str != null) {
/* 1163 */       this.jobAttributes.setPrinter(str);
/*      */     }
/* 1165 */     str = this.props.getProperty("awt.print.fileName");
/* 1166 */     if (str != null) {
/* 1167 */       this.jobAttributes.setFileName(str);
/*      */     }
/* 1169 */     str = this.props.getProperty("awt.print.numCopies");
/* 1170 */     if (str != null) {
/* 1171 */       this.jobAttributes.setCopies(Integer.parseInt(str));
/*      */     }
/*      */ 
/* 1174 */     this.options = this.props.getProperty("awt.print.options", "");
/*      */ 
/* 1176 */     str = this.props.getProperty("awt.print.orientation");
/* 1177 */     if (str != null) {
/* 1178 */       if (str.equals("portrait")) {
/* 1179 */         this.pageAttributes.setOrientationRequested(PageAttributes.OrientationRequestedType.PORTRAIT);
/*      */       }
/* 1181 */       else if (str.equals("landscape")) {
/* 1182 */         this.pageAttributes.setOrientationRequested(PageAttributes.OrientationRequestedType.LANDSCAPE);
/*      */       }
/*      */     }
/*      */ 
/* 1186 */     str = this.props.getProperty("awt.print.paperSize");
/* 1187 */     if (str != null)
/* 1188 */       if (str.equals("letter"))
/* 1189 */         this.pageAttributes.setMedia(SIZES[PageAttributes.MediaType.LETTER.hashCode()]);
/* 1190 */       else if (str.equals("legal"))
/* 1191 */         this.pageAttributes.setMedia(SIZES[PageAttributes.MediaType.LEGAL.hashCode()]);
/* 1192 */       else if (str.equals("executive"))
/* 1193 */         this.pageAttributes.setMedia(SIZES[PageAttributes.MediaType.EXECUTIVE.hashCode()]);
/* 1194 */       else if (str.equals("a4"))
/* 1195 */         this.pageAttributes.setMedia(SIZES[PageAttributes.MediaType.A4.hashCode()]);
/*      */   }
/*      */ 
/*      */   private void translateOutputProps()
/*      */   {
/* 1201 */     if (this.props == null) {
/* 1202 */       return;
/*      */     }
/*      */ 
/* 1207 */     this.props.setProperty("awt.print.destination", this.jobAttributes.getDestination() == JobAttributes.DestinationType.PRINTER ? "printer" : "file");
/*      */ 
/* 1210 */     String str = this.jobAttributes.getPrinter();
/* 1211 */     if ((str != null) && (!str.equals(""))) {
/* 1212 */       this.props.setProperty("awt.print.printer", str);
/*      */     }
/* 1214 */     str = this.jobAttributes.getFileName();
/* 1215 */     if ((str != null) && (!str.equals(""))) {
/* 1216 */       this.props.setProperty("awt.print.fileName", str);
/*      */     }
/* 1218 */     int i = this.jobAttributes.getCopies();
/* 1219 */     if (i > 0) {
/* 1220 */       this.props.setProperty("awt.print.numCopies", "" + i);
/*      */     }
/* 1222 */     str = this.options;
/* 1223 */     if ((str != null) && (!str.equals(""))) {
/* 1224 */       this.props.setProperty("awt.print.options", str);
/*      */     }
/* 1226 */     this.props.setProperty("awt.print.orientation", this.pageAttributes.getOrientationRequested() == PageAttributes.OrientationRequestedType.PORTRAIT ? "portrait" : "landscape");
/*      */ 
/* 1230 */     PageAttributes.MediaType localMediaType = SIZES[this.pageAttributes.getMedia().hashCode()];
/* 1231 */     if (localMediaType == PageAttributes.MediaType.LETTER)
/* 1232 */       str = "letter";
/* 1233 */     else if (localMediaType == PageAttributes.MediaType.LEGAL)
/* 1234 */       str = "legal";
/* 1235 */     else if (localMediaType == PageAttributes.MediaType.EXECUTIVE)
/* 1236 */       str = "executive";
/* 1237 */     else if (localMediaType == PageAttributes.MediaType.A4)
/* 1238 */       str = "a4";
/*      */     else {
/* 1240 */       str = localMediaType.toString();
/*      */     }
/* 1242 */     this.props.setProperty("awt.print.paperSize", str);
/*      */   }
/*      */ 
/*      */   private void throwPrintToFile() {
/* 1246 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1247 */     FilePermission localFilePermission = null;
/* 1248 */     if (localSecurityManager != null) {
/* 1249 */       if (localFilePermission == null) {
/* 1250 */         localFilePermission = new FilePermission("<<ALL FILES>>", "read,write");
/*      */       }
/*      */ 
/* 1253 */       localSecurityManager.checkPermission(localFilePermission);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class MessageQ
/*      */   {
/* 1012 */     private String qid = "noname";
/*      */ 
/* 1014 */     private ArrayList queue = new ArrayList();
/*      */ 
/*      */     MessageQ(String arg2)
/*      */     {
/*      */       Object localObject;
/* 1017 */       this.qid = localObject;
/*      */     }
/*      */ 
/*      */     synchronized void closeWhenEmpty()
/*      */     {
/* 1022 */       while ((this.queue != null) && (this.queue.size() > 0)) {
/*      */         try {
/* 1024 */           wait(1000L);
/*      */         }
/*      */         catch (InterruptedException localInterruptedException)
/*      */         {
/*      */         }
/*      */       }
/* 1030 */       this.queue = null;
/* 1031 */       notifyAll();
/*      */     }
/*      */ 
/*      */     synchronized void close() {
/* 1035 */       this.queue = null;
/* 1036 */       notifyAll();
/*      */     }
/*      */ 
/*      */     synchronized boolean append(Graphics2D paramGraphics2D)
/*      */     {
/* 1041 */       boolean bool = false;
/*      */ 
/* 1043 */       if (this.queue != null) {
/* 1044 */         this.queue.add(paramGraphics2D);
/* 1045 */         bool = true;
/* 1046 */         notify();
/*      */       }
/*      */ 
/* 1049 */       return bool;
/*      */     }
/*      */ 
/*      */     synchronized Graphics2D pop() {
/* 1053 */       Graphics2D localGraphics2D = null;
/*      */ 
/* 1055 */       while ((localGraphics2D == null) && (this.queue != null))
/*      */       {
/* 1057 */         if (this.queue.size() > 0) {
/* 1058 */           localGraphics2D = (Graphics2D)this.queue.remove(0);
/* 1059 */           notify();
/*      */         }
/*      */         else {
/*      */           try {
/* 1063 */             wait(2000L);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException)
/*      */           {
/*      */           }
/*      */         }
/*      */       }
/* 1070 */       return localGraphics2D;
/*      */     }
/*      */ 
/*      */     synchronized boolean isClosed() {
/* 1074 */       return this.queue == null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.PrintJob2D
 * JD-Core Version:    0.6.2
 */