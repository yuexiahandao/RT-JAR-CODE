/*      */ package sun.print;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.GraphicsDevice;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.geom.Point2D.Double;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.awt.geom.Rectangle2D.Double;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.print.Book;
/*      */ import java.awt.print.PageFormat;
/*      */ import java.awt.print.Pageable;
/*      */ import java.awt.print.Paper;
/*      */ import java.awt.print.Printable;
/*      */ import java.awt.print.PrinterAbortException;
/*      */ import java.awt.print.PrinterException;
/*      */ import java.awt.print.PrinterJob;
/*      */ import java.io.File;
/*      */ import java.io.FilePermission;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.net.URI;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Locale;
/*      */ import javax.print.DocFlavor.SERVICE_FORMATTED;
/*      */ import javax.print.DocPrintJob;
/*      */ import javax.print.PrintException;
/*      */ import javax.print.PrintService;
/*      */ import javax.print.PrintServiceLookup;
/*      */ import javax.print.ServiceUI;
/*      */ import javax.print.StreamPrintService;
/*      */ import javax.print.StreamPrintServiceFactory;
/*      */ import javax.print.attribute.Attribute;
/*      */ import javax.print.attribute.HashPrintRequestAttributeSet;
/*      */ import javax.print.attribute.PrintRequestAttributeSet;
/*      */ import javax.print.attribute.standard.Copies;
/*      */ import javax.print.attribute.standard.Destination;
/*      */ import javax.print.attribute.standard.DialogTypeSelection;
/*      */ import javax.print.attribute.standard.Fidelity;
/*      */ import javax.print.attribute.standard.JobName;
/*      */ import javax.print.attribute.standard.JobSheets;
/*      */ import javax.print.attribute.standard.Media;
/*      */ import javax.print.attribute.standard.MediaPrintableArea;
/*      */ import javax.print.attribute.standard.MediaSize;
/*      */ import javax.print.attribute.standard.MediaSize.NA;
/*      */ import javax.print.attribute.standard.MediaSizeName;
/*      */ import javax.print.attribute.standard.OrientationRequested;
/*      */ import javax.print.attribute.standard.PageRanges;
/*      */ import javax.print.attribute.standard.PrinterIsAcceptingJobs;
/*      */ import javax.print.attribute.standard.PrinterState;
/*      */ import javax.print.attribute.standard.PrinterStateReason;
/*      */ import javax.print.attribute.standard.PrinterStateReasons;
/*      */ import javax.print.attribute.standard.RequestingUserName;
/*      */ import javax.print.attribute.standard.SheetCollate;
/*      */ import javax.print.attribute.standard.Sides;
/*      */ import sun.awt.image.ByteInterleavedRaster;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public abstract class RasterPrinterJob extends PrinterJob
/*      */ {
/*      */   protected static final int PRINTER = 0;
/*      */   protected static final int FILE = 1;
/*      */   protected static final int STREAM = 2;
/*      */   private static final int MAX_BAND_SIZE = 4194304;
/*      */   private static final float DPI = 72.0F;
/*      */   private static final String FORCE_PIPE_PROP = "sun.java2d.print.pipeline";
/*      */   private static final String FORCE_RASTER = "raster";
/*      */   private static final String FORCE_PDL = "pdl";
/*      */   private static final String SHAPE_TEXT_PROP = "sun.java2d.print.shapetext";
/*  164 */   public static boolean forcePDL = false;
/*  165 */   public static boolean forceRaster = false;
/*  166 */   public static boolean shapeTextProp = false;
/*      */ 
/*  200 */   private int cachedBandWidth = 0;
/*  201 */   private int cachedBandHeight = 0;
/*  202 */   private BufferedImage cachedBand = null;
/*      */ 
/*  207 */   private int mNumCopies = 1;
/*      */ 
/*  216 */   private boolean mCollate = false;
/*      */ 
/*  226 */   private int mFirstPage = -1;
/*  227 */   private int mLastPage = -1;
/*      */   private Paper previousPaper;
/*  244 */   protected Pageable mDocument = new Book();
/*      */ 
/*  249 */   private String mDocName = "Java Printing";
/*      */ 
/*  256 */   protected boolean performingPrinting = false;
/*      */ 
/*  258 */   protected boolean userCancelled = false;
/*      */   private FilePermission printToFilePermission;
/*  268 */   private ArrayList redrawList = new ArrayList();
/*      */   private int copiesAttr;
/*      */   private String jobNameAttr;
/*      */   private String userNameAttr;
/*      */   private PageRanges pageRangesAttr;
/*      */   protected Sides sidesAttr;
/*      */   protected String destinationAttr;
/*  280 */   protected boolean noJobSheet = false;
/*  281 */   protected int mDestType = 1;
/*  282 */   protected String mDestination = "";
/*  283 */   protected boolean collateAttReq = false;
/*      */ 
/*  288 */   protected boolean landscapeRotates270 = false;
/*      */ 
/*  294 */   protected PrintRequestAttributeSet attributes = null;
/*      */   protected PrintService myService;
/* 1284 */   public static boolean debugPrint = false;
/*      */   private int deviceWidth;
/*      */   private int deviceHeight;
/*      */   private AffineTransform defaultDeviceTransform;
/*      */   private PrinterGraphicsConfig pgConfig;
/*      */ 
/*      */   protected abstract double getXRes();
/*      */ 
/*      */   protected abstract double getYRes();
/*      */ 
/*      */   protected abstract double getPhysicalPrintableX(Paper paramPaper);
/*      */ 
/*      */   protected abstract double getPhysicalPrintableY(Paper paramPaper);
/*      */ 
/*      */   protected abstract double getPhysicalPrintableWidth(Paper paramPaper);
/*      */ 
/*      */   protected abstract double getPhysicalPrintableHeight(Paper paramPaper);
/*      */ 
/*      */   protected abstract double getPhysicalPageWidth(Paper paramPaper);
/*      */ 
/*      */   protected abstract double getPhysicalPageHeight(Paper paramPaper);
/*      */ 
/*      */   protected abstract void startPage(PageFormat paramPageFormat, Printable paramPrintable, int paramInt, boolean paramBoolean)
/*      */     throws PrinterException;
/*      */ 
/*      */   protected abstract void endPage(PageFormat paramPageFormat, Printable paramPrintable, int paramInt)
/*      */     throws PrinterException;
/*      */ 
/*      */   protected abstract void printBand(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     throws PrinterException;
/*      */ 
/*      */   public void saveState(AffineTransform paramAffineTransform, Shape paramShape, Rectangle2D paramRectangle2D, double paramDouble1, double paramDouble2)
/*      */   {
/*  411 */     GraphicsState localGraphicsState = new GraphicsState(null);
/*  412 */     localGraphicsState.theTransform = paramAffineTransform;
/*  413 */     localGraphicsState.theClip = paramShape;
/*  414 */     localGraphicsState.region = paramRectangle2D;
/*  415 */     localGraphicsState.sx = paramDouble1;
/*  416 */     localGraphicsState.sy = paramDouble2;
/*  417 */     this.redrawList.add(localGraphicsState);
/*      */   }
/*      */ 
/*      */   protected static PrintService lookupDefaultPrintService()
/*      */   {
/*  430 */     PrintService localPrintService = PrintServiceLookup.lookupDefaultPrintService();
/*      */ 
/*  433 */     if ((localPrintService != null) && (localPrintService.isDocFlavorSupported(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) && (localPrintService.isDocFlavorSupported(DocFlavor.SERVICE_FORMATTED.PRINTABLE)))
/*      */     {
/*  438 */       return localPrintService;
/*      */     }
/*  440 */     PrintService[] arrayOfPrintService = PrintServiceLookup.lookupPrintServices(DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
/*      */ 
/*  443 */     if (arrayOfPrintService.length > 0) {
/*  444 */       return arrayOfPrintService[0];
/*      */     }
/*      */ 
/*  447 */     return null;
/*      */   }
/*      */ 
/*      */   public PrintService getPrintService()
/*      */   {
/*  458 */     if (this.myService == null) {
/*  459 */       PrintService localPrintService = PrintServiceLookup.lookupDefaultPrintService();
/*  460 */       if ((localPrintService != null) && (localPrintService.isDocFlavorSupported(DocFlavor.SERVICE_FORMATTED.PAGEABLE)))
/*      */       {
/*      */         try
/*      */         {
/*  464 */           setPrintService(localPrintService);
/*  465 */           this.myService = localPrintService;
/*      */         } catch (PrinterException localPrinterException1) {
/*      */         }
/*      */       }
/*  469 */       if (this.myService == null) {
/*  470 */         PrintService[] arrayOfPrintService = PrintServiceLookup.lookupPrintServices(DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
/*      */ 
/*  472 */         if (arrayOfPrintService.length > 0)
/*      */           try {
/*  474 */             setPrintService(arrayOfPrintService[0]);
/*  475 */             this.myService = arrayOfPrintService[0];
/*      */           }
/*      */           catch (PrinterException localPrinterException2) {
/*      */           }
/*      */       }
/*      */     }
/*  481 */     return this.myService;
/*      */   }
/*      */ 
/*      */   public void setPrintService(PrintService paramPrintService)
/*      */     throws PrinterException
/*      */   {
/*  497 */     if (paramPrintService == null)
/*  498 */       throw new PrinterException("Service cannot be null");
/*  499 */     if ((!(paramPrintService instanceof StreamPrintService)) && (paramPrintService.getName() == null))
/*      */     {
/*  501 */       throw new PrinterException("Null PrintService name.");
/*      */     }
/*      */ 
/*  505 */     PrinterState localPrinterState = (PrinterState)paramPrintService.getAttribute(PrinterState.class);
/*      */ 
/*  507 */     if (localPrinterState == PrinterState.STOPPED) {
/*  508 */       PrinterStateReasons localPrinterStateReasons = (PrinterStateReasons)paramPrintService.getAttribute(PrinterStateReasons.class);
/*      */ 
/*  511 */       if ((localPrinterStateReasons != null) && (localPrinterStateReasons.containsKey(PrinterStateReason.SHUTDOWN)))
/*      */       {
/*  514 */         throw new PrinterException("PrintService is no longer available.");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  519 */     if ((paramPrintService.isDocFlavorSupported(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) && (paramPrintService.isDocFlavorSupported(DocFlavor.SERVICE_FORMATTED.PRINTABLE)))
/*      */     {
/*  523 */       this.myService = paramPrintService;
/*      */     }
/*  525 */     else throw new PrinterException("Not a 2D print service: " + paramPrintService);
/*      */   }
/*      */ 
/*      */   protected void updatePageAttributes(PrintService paramPrintService, PageFormat paramPageFormat)
/*      */   {
/*  533 */     if ((paramPrintService == null) || (paramPageFormat == null)) {
/*  534 */       return;
/*      */     }
/*      */ 
/*  537 */     float f1 = (float)Math.rint(paramPageFormat.getPaper().getWidth() * 25400.0D / 72.0D) / 25400.0F;
/*      */ 
/*  540 */     float f2 = (float)Math.rint(paramPageFormat.getPaper().getHeight() * 25400.0D / 72.0D) / 25400.0F;
/*      */ 
/*  548 */     Media[] arrayOfMedia = (Media[])paramPrintService.getSupportedAttributeValues(Media.class, null, null);
/*      */ 
/*  550 */     Object localObject = null;
/*      */     try {
/*  552 */       localObject = CustomMediaSizeName.findMedia(arrayOfMedia, f1, f2, 25400);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException1) {
/*      */     }
/*  556 */     if ((localObject == null) || (!paramPrintService.isAttributeValueSupported((Attribute)localObject, null, null)))
/*      */     {
/*  558 */       localObject = (Media)paramPrintService.getDefaultAttributeValue(Media.class);
/*      */     }
/*      */     OrientationRequested localOrientationRequested;
/*  562 */     switch (paramPageFormat.getOrientation()) {
/*      */     case 0:
/*  564 */       localOrientationRequested = OrientationRequested.LANDSCAPE;
/*  565 */       break;
/*      */     case 2:
/*  567 */       localOrientationRequested = OrientationRequested.REVERSE_LANDSCAPE;
/*  568 */       break;
/*      */     default:
/*  570 */       localOrientationRequested = OrientationRequested.PORTRAIT;
/*      */     }
/*      */ 
/*  573 */     if (this.attributes == null) {
/*  574 */       this.attributes = new HashPrintRequestAttributeSet();
/*      */     }
/*  576 */     if (localObject != null) {
/*  577 */       this.attributes.add((Attribute)localObject);
/*      */     }
/*  579 */     this.attributes.add(localOrientationRequested);
/*      */ 
/*  581 */     float f3 = (float)(paramPageFormat.getPaper().getImageableX() / 72.0D);
/*  582 */     float f4 = (float)(paramPageFormat.getPaper().getImageableWidth() / 72.0D);
/*  583 */     float f5 = (float)(paramPageFormat.getPaper().getImageableY() / 72.0D);
/*  584 */     float f6 = (float)(paramPageFormat.getPaper().getImageableHeight() / 72.0D);
/*  585 */     if (f3 < 0.0F) f3 = 0.0F; if (f5 < 0.0F) f5 = 0.0F; try
/*      */     {
/*  587 */       this.attributes.add(new MediaPrintableArea(f3, f5, f4, f6, 25400));
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException2)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public PageFormat pageDialog(PageFormat paramPageFormat)
/*      */     throws HeadlessException
/*      */   {
/*  617 */     if (GraphicsEnvironment.isHeadless()) {
/*  618 */       throw new HeadlessException();
/*      */     }
/*      */ 
/*  621 */     final GraphicsConfiguration localGraphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*      */ 
/*  625 */     PrintService localPrintService = (PrintService)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run()
/*      */       {
/*  629 */         PrintService localPrintService = RasterPrinterJob.this.getPrintService();
/*  630 */         if (localPrintService == null) {
/*  631 */           ServiceDialog.showNoPrintService(localGraphicsConfiguration);
/*  632 */           return null;
/*      */         }
/*  634 */         return localPrintService;
/*      */       }
/*      */     });
/*  638 */     if (localPrintService == null) {
/*  639 */       return paramPageFormat;
/*      */     }
/*  641 */     updatePageAttributes(localPrintService, paramPageFormat);
/*      */ 
/*  643 */     PageFormat localPageFormat = pageDialog(this.attributes);
/*      */ 
/*  645 */     if (localPageFormat == null) {
/*  646 */       return paramPageFormat;
/*      */     }
/*  648 */     return localPageFormat;
/*      */   }
/*      */ 
/*      */   public PageFormat pageDialog(PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */     throws HeadlessException
/*      */   {
/*  658 */     if (GraphicsEnvironment.isHeadless()) {
/*  659 */       throw new HeadlessException();
/*      */     }
/*      */ 
/*  662 */     final GraphicsConfiguration localGraphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*      */ 
/*  665 */     Rectangle localRectangle = localGraphicsConfiguration.getBounds();
/*  666 */     int i = localRectangle.x + localRectangle.width / 3;
/*  667 */     int j = localRectangle.y + localRectangle.height / 3;
/*      */ 
/*  669 */     PrintService localPrintService = (PrintService)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run()
/*      */       {
/*  673 */         PrintService localPrintService = RasterPrinterJob.this.getPrintService();
/*  674 */         if (localPrintService == null) {
/*  675 */           ServiceDialog.showNoPrintService(localGraphicsConfiguration);
/*  676 */           return null;
/*      */         }
/*  678 */         return localPrintService;
/*      */       }
/*      */     });
/*  682 */     if (localPrintService == null) {
/*  683 */       return null;
/*      */     }
/*      */ 
/*  686 */     ServiceDialog localServiceDialog = new ServiceDialog(localGraphicsConfiguration, i, j, localPrintService, DocFlavor.SERVICE_FORMATTED.PAGEABLE, paramPrintRequestAttributeSet, (Frame)null);
/*      */ 
/*  689 */     localServiceDialog.show();
/*      */ 
/*  691 */     if (localServiceDialog.getStatus() == 1) {
/*  692 */       PrintRequestAttributeSet localPrintRequestAttributeSet = localServiceDialog.getAttributes();
/*      */ 
/*  694 */       SunAlternateMedia localSunAlternateMedia = SunAlternateMedia.class;
/*      */ 
/*  696 */       if ((paramPrintRequestAttributeSet.containsKey(localSunAlternateMedia)) && (!localPrintRequestAttributeSet.containsKey(localSunAlternateMedia)))
/*      */       {
/*  698 */         paramPrintRequestAttributeSet.remove(localSunAlternateMedia);
/*      */       }
/*  700 */       paramPrintRequestAttributeSet.addAll(localPrintRequestAttributeSet);
/*      */ 
/*  702 */       PageFormat localPageFormat = defaultPage();
/*      */ 
/*  704 */       OrientationRequested localOrientationRequested = (OrientationRequested)paramPrintRequestAttributeSet.get(OrientationRequested.class);
/*      */ 
/*  707 */       int k = 1;
/*  708 */       if (localOrientationRequested != null) {
/*  709 */         if (localOrientationRequested == OrientationRequested.REVERSE_LANDSCAPE)
/*  710 */           k = 2;
/*  711 */         else if (localOrientationRequested == OrientationRequested.LANDSCAPE) {
/*  712 */           k = 0;
/*      */         }
/*      */       }
/*  715 */       localPageFormat.setOrientation(k);
/*      */ 
/*  717 */       Object localObject = (Media)paramPrintRequestAttributeSet.get(Media.class);
/*  718 */       if (localObject == null) {
/*  719 */         localObject = (Media)localPrintService.getDefaultAttributeValue(Media.class);
/*      */       }
/*      */ 
/*  722 */       if (!(localObject instanceof MediaSizeName)) {
/*  723 */         localObject = MediaSizeName.NA_LETTER;
/*      */       }
/*  725 */       MediaSize localMediaSize = MediaSize.getMediaSizeForName((MediaSizeName)localObject);
/*      */ 
/*  727 */       if (localMediaSize == null) {
/*  728 */         localMediaSize = MediaSize.NA.LETTER;
/*      */       }
/*  730 */       Paper localPaper = new Paper();
/*  731 */       float[] arrayOfFloat = localMediaSize.getSize(1);
/*  732 */       double d1 = Math.rint(arrayOfFloat[0] * 72.0D / 25400.0D);
/*  733 */       double d2 = Math.rint(arrayOfFloat[1] * 72.0D / 25400.0D);
/*  734 */       localPaper.setSize(d1, d2);
/*  735 */       MediaPrintableArea localMediaPrintableArea = (MediaPrintableArea)paramPrintRequestAttributeSet.get(MediaPrintableArea.class);
/*      */       double d3;
/*      */       double d5;
/*      */       double d4;
/*      */       double d6;
/*  740 */       if (localMediaPrintableArea != null)
/*      */       {
/*  743 */         d3 = Math.rint(localMediaPrintableArea.getX(25400) * 72.0F);
/*      */ 
/*  745 */         d5 = Math.rint(localMediaPrintableArea.getY(25400) * 72.0F);
/*      */ 
/*  747 */         d4 = Math.rint(localMediaPrintableArea.getWidth(25400) * 72.0F);
/*      */ 
/*  749 */         d6 = Math.rint(localMediaPrintableArea.getHeight(25400) * 72.0F);
/*      */       }
/*      */       else
/*      */       {
/*  753 */         if (d1 >= 432.0D) {
/*  754 */           d3 = 72.0D;
/*  755 */           d4 = d1 - 144.0D;
/*      */         } else {
/*  757 */           d3 = d1 / 6.0D;
/*  758 */           d4 = d1 * 0.75D;
/*      */         }
/*  760 */         if (d2 >= 432.0D) {
/*  761 */           d5 = 72.0D;
/*  762 */           d6 = d2 - 144.0D;
/*      */         } else {
/*  764 */           d5 = d2 / 6.0D;
/*  765 */           d6 = d2 * 0.75D;
/*      */         }
/*      */       }
/*  768 */       localPaper.setImageableArea(d3, d5, d4, d6);
/*  769 */       localPageFormat.setPaper(localPaper);
/*      */ 
/*  771 */       return localPageFormat;
/*      */     }
/*  773 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean printDialog(PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */     throws HeadlessException
/*      */   {
/*  794 */     if (GraphicsEnvironment.isHeadless()) {
/*  795 */       throw new HeadlessException();
/*      */     }
/*      */ 
/*  799 */     DialogTypeSelection localDialogTypeSelection = (DialogTypeSelection)paramPrintRequestAttributeSet.get(DialogTypeSelection.class);
/*      */ 
/*  803 */     if (localDialogTypeSelection == DialogTypeSelection.NATIVE) {
/*  804 */       this.attributes = paramPrintRequestAttributeSet;
/*      */       try {
/*  806 */         debug_println("calling setAttributes in printDialog");
/*  807 */         setAttributes(paramPrintRequestAttributeSet);
/*      */       }
/*      */       catch (PrinterException localPrinterException1)
/*      */       {
/*      */       }
/*      */ 
/*  813 */       boolean bool = printDialog();
/*  814 */       this.attributes = paramPrintRequestAttributeSet;
/*  815 */       return bool;
/*      */     }
/*      */ 
/*  829 */     final GraphicsConfiguration localGraphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*      */ 
/*  833 */     PrintService localPrintService1 = (PrintService)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run()
/*      */       {
/*  837 */         PrintService localPrintService = RasterPrinterJob.this.getPrintService();
/*  838 */         if (localPrintService == null) {
/*  839 */           ServiceDialog.showNoPrintService(localGraphicsConfiguration);
/*  840 */           return null;
/*      */         }
/*  842 */         return localPrintService;
/*      */       }
/*      */     });
/*  846 */     if (localPrintService1 == null) {
/*  847 */       return false;
/*      */     }
/*      */ 
/*  851 */     StreamPrintServiceFactory[] arrayOfStreamPrintServiceFactory = null;
/*      */     Object localObject;
/*  852 */     if ((localPrintService1 instanceof StreamPrintService)) {
/*  853 */       arrayOfStreamPrintServiceFactory = lookupStreamPrintServices(null);
/*  854 */       localObject = new StreamPrintService[arrayOfStreamPrintServiceFactory.length];
/*  855 */       for (int i = 0; i < arrayOfStreamPrintServiceFactory.length; i++)
/*  856 */         localObject[i] = arrayOfStreamPrintServiceFactory[i].getPrintService(null);
/*      */     }
/*      */     else {
/*  859 */       localObject = (PrintService[])AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run()
/*      */         {
/*  863 */           PrintService[] arrayOfPrintService = PrinterJob.lookupPrintServices();
/*  864 */           return arrayOfPrintService;
/*      */         }
/*      */       });
/*  868 */       if ((localObject == null) || (localObject.length == 0))
/*      */       {
/*  873 */         localObject = new PrintService[1];
/*  874 */         localObject[0] = localPrintService1; }  } 
/*      */ Rectangle localRectangle = localGraphicsConfiguration.getBounds();
/*  879 */     int j = localRectangle.x + localRectangle.width / 3;
/*  880 */     int k = localRectangle.y + localRectangle.height / 3;
/*      */ 
/*  883 */     PrinterJobWrapper localPrinterJobWrapper = new PrinterJobWrapper(this);
/*  884 */     paramPrintRequestAttributeSet.add(localPrinterJobWrapper);
/*      */     PrintService localPrintService2;
/*      */     try { localPrintService2 = ServiceUI.printDialog(localGraphicsConfiguration, j, k, (PrintService[])localObject, localPrintService1, DocFlavor.SERVICE_FORMATTED.PAGEABLE, paramPrintRequestAttributeSet); }
/*      */     catch (IllegalArgumentException localIllegalArgumentException)
/*      */     {
/*  892 */       localPrintService2 = ServiceUI.printDialog(localGraphicsConfiguration, j, k, (PrintService[])localObject, localObject[0], DocFlavor.SERVICE_FORMATTED.PAGEABLE, paramPrintRequestAttributeSet);
/*      */     }
/*      */ 
/*  897 */     paramPrintRequestAttributeSet.remove(PrinterJobWrapper.class);
/*      */ 
/*  899 */     if (localPrintService2 == null) {
/*  900 */       return false;
/*      */     }
/*      */ 
/*  903 */     if (!localPrintService1.equals(localPrintService2)) {
/*      */       try {
/*  905 */         setPrintService(localPrintService2);
/*      */       }
/*      */       catch (PrinterException localPrinterException2)
/*      */       {
/*  912 */         this.myService = localPrintService2;
/*      */       }
/*      */     }
/*  915 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean printDialog()
/*      */     throws HeadlessException
/*      */   {
/*  929 */     if (GraphicsEnvironment.isHeadless()) {
/*  930 */       throw new HeadlessException();
/*      */     }
/*      */ 
/*  933 */     HashPrintRequestAttributeSet localHashPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
/*      */ 
/*  935 */     localHashPrintRequestAttributeSet.add(new Copies(getCopies()));
/*  936 */     localHashPrintRequestAttributeSet.add(new JobName(getJobName(), null));
/*  937 */     boolean bool = printDialog(localHashPrintRequestAttributeSet);
/*  938 */     if (bool) {
/*  939 */       JobName localJobName = (JobName)localHashPrintRequestAttributeSet.get(JobName.class);
/*  940 */       if (localJobName != null) {
/*  941 */         setJobName(localJobName.getValue());
/*      */       }
/*  943 */       Copies localCopies = (Copies)localHashPrintRequestAttributeSet.get(Copies.class);
/*  944 */       if (localCopies != null) {
/*  945 */         setCopies(localCopies.getValue());
/*      */       }
/*      */ 
/*  948 */       Destination localDestination1 = (Destination)localHashPrintRequestAttributeSet.get(Destination.class);
/*      */ 
/*  950 */       if (localDestination1 != null) {
/*      */         try {
/*  952 */           this.mDestType = 1;
/*  953 */           this.mDestination = new File(localDestination1.getURI()).getPath();
/*      */         } catch (Exception localException) {
/*  955 */           this.mDestination = "out.prn";
/*  956 */           PrintService localPrintService2 = getPrintService();
/*  957 */           if (localPrintService2 != null) {
/*  958 */             Destination localDestination2 = (Destination)localPrintService2.getDefaultAttributeValue(Destination.class);
/*      */ 
/*  960 */             if (localDestination2 != null)
/*  961 */               this.mDestination = new File(localDestination2.getURI()).getPath();
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/*  966 */         this.mDestType = 0;
/*  967 */         PrintService localPrintService1 = getPrintService();
/*  968 */         if (localPrintService1 != null) {
/*  969 */           this.mDestination = localPrintService1.getName();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  974 */     return bool;
/*      */   }
/*      */ 
/*      */   public void setPrintable(Printable paramPrintable)
/*      */   {
/*  984 */     setPageable(new OpenBook(defaultPage(new PageFormat()), paramPrintable));
/*      */   }
/*      */ 
/*      */   public void setPrintable(Printable paramPrintable, PageFormat paramPageFormat)
/*      */   {
/*  996 */     setPageable(new OpenBook(paramPageFormat, paramPrintable));
/*  997 */     updatePageAttributes(getPrintService(), paramPageFormat);
/*      */   }
/*      */ 
/*      */   public void setPageable(Pageable paramPageable)
/*      */     throws NullPointerException
/*      */   {
/* 1011 */     if (paramPageable != null) {
/* 1012 */       this.mDocument = paramPageable;
/*      */     }
/*      */     else
/* 1015 */       throw new NullPointerException();
/*      */   }
/*      */ 
/*      */   protected void initPrinter()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected boolean isSupportedValue(Attribute paramAttribute, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */   {
/* 1025 */     PrintService localPrintService = getPrintService();
/* 1026 */     return (paramAttribute != null) && (localPrintService != null) && (localPrintService.isAttributeValueSupported(paramAttribute, DocFlavor.SERVICE_FORMATTED.PAGEABLE, paramPrintRequestAttributeSet));
/*      */   }
/*      */ 
/*      */   protected void setAttributes(PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */     throws PrinterException
/*      */   {
/* 1039 */     setCollated(false);
/* 1040 */     this.sidesAttr = null;
/* 1041 */     this.pageRangesAttr = null;
/* 1042 */     this.copiesAttr = 0;
/* 1043 */     this.jobNameAttr = null;
/* 1044 */     this.userNameAttr = null;
/* 1045 */     this.destinationAttr = null;
/* 1046 */     this.collateAttReq = false;
/*      */ 
/* 1048 */     PrintService localPrintService = getPrintService();
/* 1049 */     if ((paramPrintRequestAttributeSet == null) || (localPrintService == null)) {
/* 1050 */       return;
/*      */     }
/*      */ 
/* 1053 */     int i = 0;
/* 1054 */     Fidelity localFidelity = (Fidelity)paramPrintRequestAttributeSet.get(Fidelity.class);
/* 1055 */     if ((localFidelity != null) && (localFidelity == Fidelity.FIDELITY_TRUE)) {
/* 1056 */       i = 1;
/*      */     }
/*      */ 
/* 1059 */     if (i == 1) {
/* 1060 */       localObject1 = localPrintService.getUnsupportedAttributes(DocFlavor.SERVICE_FORMATTED.PAGEABLE, paramPrintRequestAttributeSet);
/*      */ 
/* 1064 */       if (localObject1 != null) {
/* 1065 */         throw new PrinterException("Fidelity cannot be satisfied");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1075 */     Object localObject1 = (SheetCollate)paramPrintRequestAttributeSet.get(SheetCollate.class);
/*      */ 
/* 1077 */     if (isSupportedValue((Attribute)localObject1, paramPrintRequestAttributeSet)) {
/* 1078 */       setCollated(localObject1 == SheetCollate.COLLATED);
/*      */     }
/*      */ 
/* 1081 */     this.sidesAttr = ((Sides)paramPrintRequestAttributeSet.get(Sides.class));
/* 1082 */     if (!isSupportedValue(this.sidesAttr, paramPrintRequestAttributeSet)) {
/* 1083 */       this.sidesAttr = Sides.ONE_SIDED;
/*      */     }
/*      */ 
/* 1086 */     this.pageRangesAttr = ((PageRanges)paramPrintRequestAttributeSet.get(PageRanges.class));
/* 1087 */     if (!isSupportedValue(this.pageRangesAttr, paramPrintRequestAttributeSet)) {
/* 1088 */       this.pageRangesAttr = null;
/*      */     }
/* 1090 */     else if ((SunPageSelection)paramPrintRequestAttributeSet.get(SunPageSelection.class) == SunPageSelection.RANGE)
/*      */     {
/* 1093 */       localObject2 = this.pageRangesAttr.getMembers();
/*      */ 
/* 1095 */       setPageRange(localObject2[0][0] - 1, localObject2[0][1] - 1);
/*      */     } else {
/* 1097 */       setPageRange(-1, -1);
/*      */     }
/*      */ 
/* 1101 */     Object localObject2 = (Copies)paramPrintRequestAttributeSet.get(Copies.class);
/* 1102 */     if ((isSupportedValue((Attribute)localObject2, paramPrintRequestAttributeSet)) || ((i == 0) && (localObject2 != null)))
/*      */     {
/* 1104 */       this.copiesAttr = ((Copies)localObject2).getValue();
/* 1105 */       setCopies(this.copiesAttr);
/*      */     } else {
/* 1107 */       this.copiesAttr = getCopies();
/*      */     }
/*      */ 
/* 1110 */     Destination localDestination = (Destination)paramPrintRequestAttributeSet.get(Destination.class);
/*      */ 
/* 1113 */     if (isSupportedValue(localDestination, paramPrintRequestAttributeSet))
/*      */     {
/*      */       try
/*      */       {
/* 1118 */         this.destinationAttr = ("" + new File(localDestination.getURI().getSchemeSpecificPart()));
/*      */       }
/*      */       catch (Exception localException) {
/* 1121 */         localObject3 = (Destination)localPrintService.getDefaultAttributeValue(Destination.class);
/*      */ 
/* 1123 */         if (localObject3 != null) {
/* 1124 */           this.destinationAttr = ("" + new File(((Destination)localObject3).getURI().getSchemeSpecificPart()));
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1130 */     JobSheets localJobSheets = (JobSheets)paramPrintRequestAttributeSet.get(JobSheets.class);
/* 1131 */     if (localJobSheets != null) {
/* 1132 */       this.noJobSheet = (localJobSheets == JobSheets.NONE);
/*      */     }
/*      */ 
/* 1135 */     Object localObject3 = (JobName)paramPrintRequestAttributeSet.get(JobName.class);
/* 1136 */     if ((isSupportedValue((Attribute)localObject3, paramPrintRequestAttributeSet)) || ((i == 0) && (localObject3 != null)))
/*      */     {
/* 1138 */       this.jobNameAttr = ((JobName)localObject3).getValue();
/* 1139 */       setJobName(this.jobNameAttr);
/*      */     } else {
/* 1141 */       this.jobNameAttr = getJobName();
/*      */     }
/*      */ 
/* 1144 */     RequestingUserName localRequestingUserName = (RequestingUserName)paramPrintRequestAttributeSet.get(RequestingUserName.class);
/*      */ 
/* 1146 */     if ((isSupportedValue(localRequestingUserName, paramPrintRequestAttributeSet)) || ((i == 0) && (localRequestingUserName != null)))
/*      */     {
/* 1148 */       this.userNameAttr = localRequestingUserName.getValue();
/*      */     }
/*      */     else try {
/* 1151 */         this.userNameAttr = getUserName();
/*      */       } catch (SecurityException localSecurityException) {
/* 1153 */         this.userNameAttr = "";
/*      */       }
/*      */ 
/*      */ 
/* 1160 */     Media localMedia = (Media)paramPrintRequestAttributeSet.get(Media.class);
/* 1161 */     OrientationRequested localOrientationRequested = (OrientationRequested)paramPrintRequestAttributeSet.get(OrientationRequested.class);
/*      */ 
/* 1163 */     MediaPrintableArea localMediaPrintableArea = (MediaPrintableArea)paramPrintRequestAttributeSet.get(MediaPrintableArea.class);
/*      */ 
/* 1166 */     if (((localOrientationRequested != null) || (localMedia != null) || (localMediaPrintableArea != null)) && ((getPageable() instanceof OpenBook)))
/*      */     {
/* 1172 */       Pageable localPageable = getPageable();
/* 1173 */       Printable localPrintable = localPageable.getPrintable(0);
/* 1174 */       PageFormat localPageFormat = (PageFormat)localPageable.getPageFormat(0).clone();
/* 1175 */       Paper localPaper = localPageFormat.getPaper();
/*      */ 
/* 1180 */       if ((localMediaPrintableArea == null) && (localMedia != null) && (localPrintService.isAttributeCategorySupported(MediaPrintableArea.class)))
/*      */       {
/* 1183 */         Object localObject4 = localPrintService.getSupportedAttributeValues(MediaPrintableArea.class, null, paramPrintRequestAttributeSet);
/*      */ 
/* 1186 */         if (((localObject4 instanceof MediaPrintableArea[])) && (((MediaPrintableArea[])localObject4).length > 0))
/*      */         {
/* 1188 */           localMediaPrintableArea = ((MediaPrintableArea[])(MediaPrintableArea[])localObject4)[0];
/*      */         }
/*      */       }
/*      */ 
/* 1192 */       if ((isSupportedValue(localOrientationRequested, paramPrintRequestAttributeSet)) || ((i == 0) && (localOrientationRequested != null)))
/*      */       {
/*      */         int j;
/* 1195 */         if (localOrientationRequested.equals(OrientationRequested.REVERSE_LANDSCAPE))
/* 1196 */           j = 2;
/* 1197 */         else if (localOrientationRequested.equals(OrientationRequested.LANDSCAPE))
/* 1198 */           j = 0;
/*      */         else {
/* 1200 */           j = 1;
/*      */         }
/* 1202 */         localPageFormat.setOrientation(j);
/*      */       }
/*      */       Object localObject5;
/* 1205 */       if ((isSupportedValue(localMedia, paramPrintRequestAttributeSet)) || ((i == 0) && (localMedia != null)))
/*      */       {
/* 1207 */         if ((localMedia instanceof MediaSizeName)) {
/* 1208 */           localObject5 = (MediaSizeName)localMedia;
/* 1209 */           MediaSize localMediaSize = MediaSize.getMediaSizeForName((MediaSizeName)localObject5);
/* 1210 */           if (localMediaSize != null) {
/* 1211 */             float f1 = localMediaSize.getX(25400) * 72.0F;
/* 1212 */             float f2 = localMediaSize.getY(25400) * 72.0F;
/* 1213 */             localPaper.setSize(f1, f2);
/* 1214 */             if (localMediaPrintableArea == null) {
/* 1215 */               localPaper.setImageableArea(72.0D, 72.0D, f1 - 144.0D, f2 - 144.0D);
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1223 */       if ((isSupportedValue(localMediaPrintableArea, paramPrintRequestAttributeSet)) || ((i == 0) && (localMediaPrintableArea != null)))
/*      */       {
/* 1225 */         localObject5 = localMediaPrintableArea.getPrintableArea(25400);
/*      */ 
/* 1227 */         for (int k = 0; k < localObject5.length; k++) {
/* 1228 */           localObject5[k] *= 72.0F;
/*      */         }
/* 1230 */         localPaper.setImageableArea(localObject5[0], localObject5[1], localObject5[2], localObject5[3]);
/*      */       }
/*      */ 
/* 1234 */       localPageFormat.setPaper(localPaper);
/* 1235 */       localPageFormat = validatePage(localPageFormat);
/* 1236 */       setPrintable(localPrintable, localPageFormat);
/*      */     }
/*      */     else
/*      */     {
/* 1240 */       this.attributes = paramPrintRequestAttributeSet;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void spoolToService(PrintService paramPrintService, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */     throws PrinterException
/*      */   {
/* 1256 */     if (paramPrintService == null) {
/* 1257 */       throw new PrinterException("No print service found.");
/*      */     }
/*      */ 
/* 1260 */     DocPrintJob localDocPrintJob = paramPrintService.createPrintJob();
/* 1261 */     PageableDoc localPageableDoc = new PageableDoc(getPageable());
/* 1262 */     if (paramPrintRequestAttributeSet == null)
/* 1263 */       paramPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
/*      */     try
/*      */     {
/* 1266 */       localDocPrintJob.print(localPageableDoc, paramPrintRequestAttributeSet);
/*      */     } catch (PrintException localPrintException) {
/* 1268 */       throw new PrinterException(localPrintException.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void print()
/*      */     throws PrinterException
/*      */   {
/* 1281 */     print(this.attributes);
/*      */   }
/*      */ 
/*      */   protected void debug_println(String paramString)
/*      */   {
/* 1286 */     if (debugPrint)
/* 1287 */       System.out.println("RasterPrinterJob " + paramString + " " + this);
/*      */   }
/*      */ 
/*      */   public void print(PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */     throws PrinterException
/*      */   {
/* 1306 */     PrintService localPrintService = getPrintService();
/* 1307 */     debug_println("psvc = " + localPrintService);
/* 1308 */     if (localPrintService == null) {
/* 1309 */       throw new PrinterException("No print service found.");
/*      */     }
/*      */ 
/* 1314 */     PrinterState localPrinterState = (PrinterState)localPrintService.getAttribute(PrinterState.class);
/*      */     Object localObject1;
/* 1316 */     if (localPrinterState == PrinterState.STOPPED) {
/* 1317 */       localObject1 = (PrinterStateReasons)localPrintService.getAttribute(PrinterStateReasons.class);
/*      */ 
/* 1320 */       if ((localObject1 != null) && (((PrinterStateReasons)localObject1).containsKey(PrinterStateReason.SHUTDOWN)))
/*      */       {
/* 1323 */         throw new PrinterException("PrintService is no longer available.");
/*      */       }
/*      */     }
/*      */ 
/* 1327 */     if ((PrinterIsAcceptingJobs)localPrintService.getAttribute(PrinterIsAcceptingJobs.class) == PrinterIsAcceptingJobs.NOT_ACCEPTING_JOBS)
/*      */     {
/* 1330 */       throw new PrinterException("Printer is not accepting job.");
/*      */     }
/*      */ 
/* 1333 */     if (((localPrintService instanceof SunPrinterJobService)) && (((SunPrinterJobService)localPrintService).usesClass(getClass())))
/*      */     {
/* 1335 */       setAttributes(paramPrintRequestAttributeSet);
/*      */ 
/* 1337 */       if (this.destinationAttr != null)
/*      */       {
/* 1342 */         localObject1 = new File(this.destinationAttr);
/*      */         try
/*      */         {
/* 1345 */           if (((File)localObject1).createNewFile())
/* 1346 */             ((File)localObject1).delete();
/*      */         }
/*      */         catch (IOException localIOException) {
/* 1349 */           throw new PrinterException("Cannot write to file:" + this.destinationAttr);
/*      */         }
/*      */         catch (SecurityException localSecurityException)
/*      */         {
/*      */         }
/*      */ 
/* 1358 */         File localFile = ((File)localObject1).getParentFile();
/* 1359 */         if (((((File)localObject1).exists()) && ((!((File)localObject1).isFile()) || (!((File)localObject1).canWrite()))) || ((localFile != null) && ((!localFile.exists()) || ((localFile.exists()) && (!localFile.canWrite())))))
/*      */         {
/* 1363 */           throw new PrinterException("Cannot write to file:" + this.destinationAttr);
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/* 1368 */       spoolToService(localPrintService, paramPrintRequestAttributeSet);
/* 1369 */       return;
/*      */     }
/*      */ 
/* 1373 */     initPrinter();
/*      */ 
/* 1375 */     int i = getCollatedCopies();
/* 1376 */     int j = getNoncollatedCopies();
/* 1377 */     debug_println("getCollatedCopies()  " + i + " getNoncollatedCopies() " + j);
/*      */ 
/* 1385 */     int k = this.mDocument.getNumberOfPages();
/* 1386 */     if (k == 0) {
/* 1387 */       return;
/*      */     }
/*      */ 
/* 1390 */     int m = getFirstPage();
/* 1391 */     int n = getLastPage();
/* 1392 */     if (n == -1) {
/* 1393 */       int i1 = this.mDocument.getNumberOfPages();
/* 1394 */       if (i1 != -1) {
/* 1395 */         n = this.mDocument.getNumberOfPages() - 1;
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/* 1400 */       synchronized (this) {
/* 1401 */         this.performingPrinting = true;
/* 1402 */         this.userCancelled = false;
/*      */       }
/*      */ 
/* 1405 */       startDoc();
/* 1406 */       if (isCancelled()) {
/* 1407 */         cancelDoc();
/*      */       }
/*      */ 
/* 1412 */       boolean bool = true;
/* 1413 */       if (paramPrintRequestAttributeSet != null) {
/* 1414 */         SunPageSelection localSunPageSelection = (SunPageSelection)paramPrintRequestAttributeSet.get(SunPageSelection.class);
/*      */ 
/* 1416 */         if ((localSunPageSelection != null) && (localSunPageSelection != SunPageSelection.RANGE)) {
/* 1417 */           bool = false;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1422 */       debug_println("after startDoc rangeSelected? " + bool + " numNonCollatedCopies " + j);
/*      */ 
/* 1439 */       for (int i2 = 0; i2 < i; i2++) {
/* 1440 */         int i3 = m; int i4 = 0;
/*      */ 
/* 1444 */         for (; ((i3 <= n) || (n == -1)) && (i4 == 0); 
/* 1444 */           i3++)
/*      */         {
/*      */           int i5;
/* 1447 */           if ((this.pageRangesAttr != null) && (bool)) {
/* 1448 */             i5 = this.pageRangesAttr.next(i3);
/* 1449 */             if (i5 == -1) {
/*      */               break;
/*      */             }
/* 1451 */             if (i5 != i3 + 1);
/*      */           }
/*      */           else
/*      */           {
/* 1456 */             for (i5 = 0; 
/* 1458 */               (i5 < j) && (i4 == 0); 
/* 1459 */               i5++)
/*      */             {
/* 1461 */               if (isCancelled()) {
/* 1462 */                 cancelDoc();
/*      */               }
/* 1464 */               debug_println("printPage " + i3);
/* 1465 */               i4 = printPage(this.mDocument, i3);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1471 */       if (isCancelled()) {
/* 1472 */         cancelDoc();
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/* 1477 */       this.previousPaper = null;
/* 1478 */       synchronized (this) {
/* 1479 */         if (this.performingPrinting) {
/* 1480 */           endDoc();
/*      */         }
/* 1482 */         this.performingPrinting = false;
/* 1483 */         notify();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void validatePaper(Paper paramPaper1, Paper paramPaper2)
/*      */   {
/* 1498 */     if ((paramPaper1 == null) || (paramPaper2 == null)) {
/* 1499 */       return;
/*      */     }
/* 1501 */     double d1 = paramPaper1.getWidth();
/* 1502 */     double d2 = paramPaper1.getHeight();
/* 1503 */     double d3 = paramPaper1.getImageableX();
/* 1504 */     double d4 = paramPaper1.getImageableY();
/* 1505 */     double d5 = paramPaper1.getImageableWidth();
/* 1506 */     double d6 = paramPaper1.getImageableHeight();
/*      */ 
/* 1511 */     Paper localPaper = new Paper();
/* 1512 */     d1 = d1 > 0.0D ? d1 : localPaper.getWidth();
/* 1513 */     d2 = d2 > 0.0D ? d2 : localPaper.getHeight();
/* 1514 */     d3 = d3 > 0.0D ? d3 : localPaper.getImageableX();
/* 1515 */     d4 = d4 > 0.0D ? d4 : localPaper.getImageableY();
/* 1516 */     d5 = d5 > 0.0D ? d5 : localPaper.getImageableWidth();
/* 1517 */     d6 = d6 > 0.0D ? d6 : localPaper.getImageableHeight();
/*      */ 
/* 1521 */     if (d5 > d1) {
/* 1522 */       d5 = d1;
/*      */     }
/* 1524 */     if (d6 > d2) {
/* 1525 */       d6 = d2;
/*      */     }
/* 1527 */     if (d3 + d5 > d1) {
/* 1528 */       d3 = d1 - d5;
/*      */     }
/* 1530 */     if (d4 + d6 > d2) {
/* 1531 */       d4 = d2 - d6;
/*      */     }
/* 1533 */     paramPaper2.setSize(d1, d2);
/* 1534 */     paramPaper2.setImageableArea(d3, d4, d5, d6);
/*      */   }
/*      */ 
/*      */   public PageFormat defaultPage(PageFormat paramPageFormat)
/*      */   {
/* 1546 */     PageFormat localPageFormat = (PageFormat)paramPageFormat.clone();
/* 1547 */     localPageFormat.setOrientation(1);
/* 1548 */     Paper localPaper = new Paper();
/* 1549 */     double d1 = 72.0D;
/*      */ 
/* 1551 */     Media localMedia = null;
/*      */ 
/* 1553 */     PrintService localPrintService = getPrintService();
/*      */     double d2;
/*      */     double d3;
/* 1554 */     if (localPrintService != null)
/*      */     {
/* 1556 */       localMedia = (Media)localPrintService.getDefaultAttributeValue(Media.class);
/*      */ 
/* 1559 */       if (((localMedia instanceof MediaSizeName)) && ((localObject = MediaSize.getMediaSizeForName((MediaSizeName)localMedia)) != null))
/*      */       {
/* 1562 */         d2 = ((MediaSize)localObject).getX(25400) * d1;
/* 1563 */         d3 = ((MediaSize)localObject).getY(25400) * d1;
/* 1564 */         localPaper.setSize(d2, d3);
/* 1565 */         localPaper.setImageableArea(d1, d1, d2 - 2.0D * d1, d3 - 2.0D * d1);
/*      */ 
/* 1568 */         localPageFormat.setPaper(localPaper);
/* 1569 */         return localPageFormat;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1576 */     Object localObject = Locale.getDefault().getCountry();
/* 1577 */     if ((!Locale.getDefault().equals(Locale.ENGLISH)) && (localObject != null) && (!((String)localObject).equals(Locale.US.getCountry())) && (!((String)localObject).equals(Locale.CANADA.getCountry())))
/*      */     {
/* 1582 */       double d4 = 25.399999999999999D;
/* 1583 */       d2 = Math.rint(210.0D * d1 / d4);
/* 1584 */       d3 = Math.rint(297.0D * d1 / d4);
/* 1585 */       localPaper.setSize(d2, d3);
/* 1586 */       localPaper.setImageableArea(d1, d1, d2 - 2.0D * d1, d3 - 2.0D * d1);
/*      */     }
/*      */ 
/* 1591 */     localPageFormat.setPaper(localPaper);
/*      */ 
/* 1593 */     return localPageFormat;
/*      */   }
/*      */ 
/*      */   public PageFormat validatePage(PageFormat paramPageFormat)
/*      */   {
/* 1601 */     PageFormat localPageFormat = (PageFormat)paramPageFormat.clone();
/* 1602 */     Paper localPaper = new Paper();
/* 1603 */     validatePaper(localPageFormat.getPaper(), localPaper);
/* 1604 */     localPageFormat.setPaper(localPaper);
/*      */ 
/* 1606 */     return localPageFormat;
/*      */   }
/*      */ 
/*      */   public void setCopies(int paramInt)
/*      */   {
/* 1613 */     this.mNumCopies = paramInt;
/*      */   }
/*      */ 
/*      */   public int getCopies()
/*      */   {
/* 1620 */     return this.mNumCopies;
/*      */   }
/*      */ 
/*      */   protected int getCopiesInt()
/*      */   {
/* 1627 */     return this.copiesAttr > 0 ? this.copiesAttr : getCopies();
/*      */   }
/*      */ 
/*      */   public String getUserName()
/*      */   {
/* 1635 */     return System.getProperty("user.name");
/*      */   }
/*      */ 
/*      */   protected String getUserNameInt()
/*      */   {
/* 1642 */     if (this.userNameAttr != null)
/* 1643 */       return this.userNameAttr;
/*      */     try
/*      */     {
/* 1646 */       return getUserName(); } catch (SecurityException localSecurityException) {
/*      */     }
/* 1648 */     return "";
/*      */   }
/*      */ 
/*      */   public void setJobName(String paramString)
/*      */   {
/* 1658 */     if (paramString != null)
/* 1659 */       this.mDocName = paramString;
/*      */     else
/* 1661 */       throw new NullPointerException();
/*      */   }
/*      */ 
/*      */   public String getJobName()
/*      */   {
/* 1669 */     return this.mDocName;
/*      */   }
/*      */ 
/*      */   protected String getJobNameInt()
/*      */   {
/* 1676 */     return this.jobNameAttr != null ? this.jobNameAttr : getJobName();
/*      */   }
/*      */ 
/*      */   protected void setPageRange(int paramInt1, int paramInt2)
/*      */   {
/* 1687 */     if ((paramInt1 >= 0) && (paramInt2 >= 0)) {
/* 1688 */       this.mFirstPage = paramInt1;
/* 1689 */       this.mLastPage = paramInt2;
/* 1690 */       if (this.mLastPage < this.mFirstPage) this.mLastPage = this.mFirstPage; 
/*      */     }
/* 1692 */     else { this.mFirstPage = -1;
/* 1693 */       this.mLastPage = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int getFirstPage()
/*      */   {
/* 1702 */     return this.mFirstPage == -1 ? 0 : this.mFirstPage;
/*      */   }
/*      */ 
/*      */   protected int getLastPage()
/*      */   {
/* 1710 */     return this.mLastPage;
/*      */   }
/*      */ 
/*      */   protected void setCollated(boolean paramBoolean)
/*      */   {
/* 1722 */     this.mCollate = paramBoolean;
/* 1723 */     this.collateAttReq = true;
/*      */   }
/*      */ 
/*      */   protected boolean isCollated()
/*      */   {
/* 1731 */     return this.mCollate;
/*      */   }
/*      */ 
/*      */   protected abstract void startDoc()
/*      */     throws PrinterException;
/*      */ 
/*      */   protected abstract void endDoc()
/*      */     throws PrinterException;
/*      */ 
/*      */   protected abstract void abortDoc();
/*      */ 
/*      */   protected void cancelDoc()
/*      */     throws PrinterAbortException
/*      */   {
/* 1751 */     abortDoc();
/* 1752 */     synchronized (this) {
/* 1753 */       this.userCancelled = false;
/* 1754 */       this.performingPrinting = false;
/* 1755 */       notify();
/*      */     }
/* 1757 */     throw new PrinterAbortException();
/*      */   }
/*      */ 
/*      */   protected int getCollatedCopies()
/*      */   {
/* 1769 */     return isCollated() ? getCopiesInt() : 1;
/*      */   }
/*      */ 
/*      */   protected int getNoncollatedCopies()
/*      */   {
/* 1779 */     return isCollated() ? 1 : getCopiesInt();
/*      */   }
/*      */ 
/*      */   synchronized void setGraphicsConfigInfo(AffineTransform paramAffineTransform, double paramDouble1, double paramDouble2)
/*      */   {
/* 1794 */     Point2D.Double localDouble = new Point2D.Double(paramDouble1, paramDouble2);
/* 1795 */     paramAffineTransform.transform(localDouble, localDouble);
/*      */ 
/* 1797 */     if ((this.pgConfig == null) || (this.defaultDeviceTransform == null) || (!paramAffineTransform.equals(this.defaultDeviceTransform)) || (this.deviceWidth != (int)localDouble.getX()) || (this.deviceHeight != (int)localDouble.getY()))
/*      */     {
/* 1803 */       this.deviceWidth = ((int)localDouble.getX());
/* 1804 */       this.deviceHeight = ((int)localDouble.getY());
/* 1805 */       this.defaultDeviceTransform = paramAffineTransform;
/* 1806 */       this.pgConfig = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized PrinterGraphicsConfig getPrinterGraphicsConfig() {
/* 1811 */     if (this.pgConfig != null) {
/* 1812 */       return this.pgConfig;
/*      */     }
/* 1814 */     String str = "Printer Device";
/* 1815 */     PrintService localPrintService = getPrintService();
/* 1816 */     if (localPrintService != null) {
/* 1817 */       str = localPrintService.toString();
/*      */     }
/* 1819 */     this.pgConfig = new PrinterGraphicsConfig(str, this.defaultDeviceTransform, this.deviceWidth, this.deviceHeight);
/*      */ 
/* 1822 */     return this.pgConfig;
/*      */   }
/*      */ 
/*      */   protected int printPage(Pageable paramPageable, int paramInt)
/*      */     throws PrinterException
/*      */   {
/*      */     PageFormat localPageFormat2;
/*      */     PageFormat localPageFormat1;
/*      */     Printable localPrintable;
/*      */     try
/*      */     {
/* 1838 */       localPageFormat2 = paramPageable.getPageFormat(paramInt);
/* 1839 */       localPageFormat1 = (PageFormat)localPageFormat2.clone();
/* 1840 */       localPrintable = paramPageable.getPrintable(paramInt);
/*      */     } catch (Exception localException) {
/* 1842 */       PrinterException localPrinterException = new PrinterException("Error getting page or printable.[ " + localException + " ]");
/*      */ 
/* 1845 */       localPrinterException.initCause(localException);
/* 1846 */       throw localPrinterException;
/*      */     }
/*      */ 
/* 1852 */     Paper localPaper1 = localPageFormat1.getPaper();
/*      */ 
/* 1854 */     if ((localPageFormat1.getOrientation() != 1) && (this.landscapeRotates270))
/*      */     {
/* 1857 */       d1 = localPaper1.getImageableX();
/* 1858 */       d2 = localPaper1.getImageableY();
/* 1859 */       double d3 = localPaper1.getImageableWidth();
/* 1860 */       double d4 = localPaper1.getImageableHeight();
/* 1861 */       localPaper1.setImageableArea(localPaper1.getWidth() - d1 - d3, localPaper1.getHeight() - d2 - d4, d3, d4);
/*      */ 
/* 1864 */       localPageFormat1.setPaper(localPaper1);
/* 1865 */       if (localPageFormat1.getOrientation() == 0)
/* 1866 */         localPageFormat1.setOrientation(2);
/*      */       else {
/* 1868 */         localPageFormat1.setOrientation(0);
/*      */       }
/*      */     }
/*      */ 
/* 1872 */     double d1 = getXRes() / 72.0D;
/* 1873 */     double d2 = getYRes() / 72.0D;
/*      */ 
/* 1878 */     Rectangle2D.Double localDouble1 = new Rectangle2D.Double(localPaper1.getImageableX() * d1, localPaper1.getImageableY() * d2, localPaper1.getImageableWidth() * d1, localPaper1.getImageableHeight() * d2);
/*      */ 
/* 1888 */     AffineTransform localAffineTransform1 = new AffineTransform();
/*      */ 
/* 1893 */     AffineTransform localAffineTransform2 = new AffineTransform();
/* 1894 */     localAffineTransform2.scale(d1, d2);
/*      */ 
/* 1899 */     int i = (int)localDouble1.getWidth();
/* 1900 */     if (i % 4 != 0) {
/* 1901 */       i += 4 - i % 4;
/*      */     }
/* 1903 */     if (i <= 0) {
/* 1904 */       throw new PrinterException("Paper's imageable width is too small.");
/*      */     }
/*      */ 
/* 1907 */     int j = (int)localDouble1.getHeight();
/* 1908 */     if (j <= 0) {
/* 1909 */       throw new PrinterException("Paper's imageable height is too small.");
/*      */     }
/*      */ 
/* 1917 */     int k = 4194304 / i / 3;
/*      */ 
/* 1919 */     int m = (int)Math.rint(localPaper1.getImageableX() * d1);
/* 1920 */     int n = (int)Math.rint(localPaper1.getImageableY() * d2);
/*      */ 
/* 1931 */     AffineTransform localAffineTransform3 = new AffineTransform();
/* 1932 */     localAffineTransform3.translate(-m, n);
/* 1933 */     localAffineTransform3.translate(0.0D, k);
/* 1934 */     localAffineTransform3.scale(1.0D, -1.0D);
/*      */ 
/* 1944 */     BufferedImage localBufferedImage = new BufferedImage(1, 1, 5);
/*      */ 
/* 1951 */     PeekGraphics localPeekGraphics = createPeekGraphics(localBufferedImage.createGraphics(), this);
/*      */ 
/* 1954 */     Rectangle2D.Double localDouble2 = new Rectangle2D.Double(localPageFormat1.getImageableX(), localPageFormat1.getImageableY(), localPageFormat1.getImageableWidth(), localPageFormat1.getImageableHeight());
/*      */ 
/* 1959 */     localPeekGraphics.transform(localAffineTransform2);
/* 1960 */     localPeekGraphics.translate(-getPhysicalPrintableX(localPaper1) / d1, -getPhysicalPrintableY(localPaper1) / d2);
/*      */ 
/* 1962 */     localPeekGraphics.transform(new AffineTransform(localPageFormat1.getMatrix()));
/* 1963 */     initPrinterGraphics(localPeekGraphics, localDouble2);
/* 1964 */     AffineTransform localAffineTransform4 = localPeekGraphics.getTransform();
/*      */ 
/* 1975 */     setGraphicsConfigInfo(localAffineTransform2, localPaper1.getWidth(), localPaper1.getHeight());
/*      */ 
/* 1977 */     int i1 = localPrintable.print(localPeekGraphics, localPageFormat2, paramInt);
/* 1978 */     debug_println("pageResult " + i1);
/* 1979 */     if (i1 == 0) {
/* 1980 */       debug_println("startPage " + paramInt);
/*      */ 
/* 1987 */       Paper localPaper2 = localPageFormat1.getPaper();
/* 1988 */       boolean bool = (this.previousPaper == null) || (localPaper2.getWidth() != this.previousPaper.getWidth()) || (localPaper2.getHeight() != this.previousPaper.getHeight());
/*      */ 
/* 1992 */       this.previousPaper = localPaper2;
/*      */ 
/* 1994 */       startPage(localPageFormat1, localPrintable, paramInt, bool);
/* 1995 */       Graphics2D localGraphics2D1 = createPathGraphics(localPeekGraphics, this, localPrintable, localPageFormat1, paramInt);
/*      */       Object localObject1;
/*      */       Object localObject2;
/* 2005 */       if (localGraphics2D1 != null) {
/* 2006 */         localGraphics2D1.transform(localAffineTransform2);
/*      */ 
/* 2008 */         localGraphics2D1.translate(-getPhysicalPrintableX(localPaper1) / d1, -getPhysicalPrintableY(localPaper1) / d2);
/*      */ 
/* 2010 */         localGraphics2D1.transform(new AffineTransform(localPageFormat1.getMatrix()));
/* 2011 */         initPrinterGraphics(localGraphics2D1, localDouble2);
/*      */ 
/* 2013 */         this.redrawList.clear();
/*      */ 
/* 2015 */         localObject1 = localGraphics2D1.getTransform();
/*      */ 
/* 2017 */         localPrintable.print(localGraphics2D1, localPageFormat2, paramInt);
/*      */ 
/* 2019 */         for (int i2 = 0; i2 < this.redrawList.size(); i2++) {
/* 2020 */           localObject2 = (GraphicsState)this.redrawList.get(i2);
/* 2021 */           localGraphics2D1.setTransform((AffineTransform)localObject1);
/* 2022 */           ((PathGraphics)localGraphics2D1).redrawRegion(((GraphicsState)localObject2).region, ((GraphicsState)localObject2).sx, ((GraphicsState)localObject2).sy, ((GraphicsState)localObject2).theClip, ((GraphicsState)localObject2).theTransform);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2034 */         localObject1 = this.cachedBand;
/* 2035 */         if ((this.cachedBand == null) || (i != this.cachedBandWidth) || (k != this.cachedBandHeight))
/*      */         {
/* 2038 */           localObject1 = new BufferedImage(i, k, 5);
/*      */ 
/* 2040 */           this.cachedBand = ((BufferedImage)localObject1);
/* 2041 */           this.cachedBandWidth = i;
/* 2042 */           this.cachedBandHeight = k;
/*      */         }
/* 2044 */         Graphics2D localGraphics2D2 = ((BufferedImage)localObject1).createGraphics();
/*      */ 
/* 2046 */         localObject2 = new Rectangle2D.Double(0.0D, 0.0D, i, k);
/*      */ 
/* 2049 */         initPrinterGraphics(localGraphics2D2, (Rectangle2D)localObject2);
/*      */ 
/* 2051 */         ProxyGraphics2D localProxyGraphics2D = new ProxyGraphics2D(localGraphics2D2, this);
/*      */ 
/* 2054 */         Graphics2D localGraphics2D3 = ((BufferedImage)localObject1).createGraphics();
/* 2055 */         localGraphics2D3.setColor(Color.white);
/*      */ 
/* 2064 */         ByteInterleavedRaster localByteInterleavedRaster = (ByteInterleavedRaster)((BufferedImage)localObject1).getRaster();
/* 2065 */         byte[] arrayOfByte = localByteInterleavedRaster.getDataStorage();
/*      */ 
/* 2071 */         int i3 = n + j;
/*      */ 
/* 2078 */         int i4 = (int)getPhysicalPrintableX(localPaper1);
/* 2079 */         int i5 = (int)getPhysicalPrintableY(localPaper1);
/*      */ 
/* 2081 */         for (int i6 = 0; i6 <= j; 
/* 2082 */           i6 += k)
/*      */         {
/* 2088 */           localGraphics2D3.fillRect(0, 0, i, k);
/*      */ 
/* 2095 */           localGraphics2D2.setTransform(localAffineTransform1);
/* 2096 */           localGraphics2D2.transform(localAffineTransform3);
/* 2097 */           localAffineTransform3.translate(0.0D, -k);
/*      */ 
/* 2102 */           localGraphics2D2.transform(localAffineTransform2);
/* 2103 */           localGraphics2D2.transform(new AffineTransform(localPageFormat1.getMatrix()));
/*      */ 
/* 2105 */           Rectangle localRectangle = localGraphics2D2.getClipBounds();
/* 2106 */           localRectangle = localAffineTransform4.createTransformedShape(localRectangle).getBounds();
/*      */ 
/* 2108 */           if ((localRectangle == null) || ((localPeekGraphics.hitsDrawingArea(localRectangle)) && (i > 0) && (k > 0)))
/*      */           {
/* 2118 */             int i7 = m - i4;
/* 2119 */             if (i7 < 0) {
/* 2120 */               localGraphics2D2.translate(i7 / d1, 0.0D);
/* 2121 */               i7 = 0;
/*      */             }
/* 2123 */             int i8 = n + i6 - i5;
/* 2124 */             if (i8 < 0) {
/* 2125 */               localGraphics2D2.translate(0.0D, i8 / d2);
/* 2126 */               i8 = 0;
/*      */             }
/*      */ 
/* 2131 */             localProxyGraphics2D.setDelegate((Graphics2D)localGraphics2D2.create());
/* 2132 */             localPrintable.print(localProxyGraphics2D, localPageFormat2, paramInt);
/* 2133 */             localProxyGraphics2D.dispose();
/* 2134 */             printBand(arrayOfByte, i7, i8, i, k);
/*      */           }
/*      */         }
/*      */ 
/* 2138 */         localGraphics2D3.dispose();
/* 2139 */         localGraphics2D2.dispose();
/*      */       }
/*      */ 
/* 2142 */       debug_println("calling endPage " + paramInt);
/* 2143 */       endPage(localPageFormat1, localPrintable, paramInt);
/*      */     }
/*      */ 
/* 2146 */     return i1;
/*      */   }
/*      */ 
/*      */   public void cancel()
/*      */   {
/* 2157 */     synchronized (this) {
/* 2158 */       if (this.performingPrinting) {
/* 2159 */         this.userCancelled = true;
/*      */       }
/* 2161 */       notify();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isCancelled()
/*      */   {
/* 2172 */     boolean bool = false;
/*      */ 
/* 2174 */     synchronized (this) {
/* 2175 */       bool = (this.performingPrinting) && (this.userCancelled);
/* 2176 */       notify();
/*      */     }
/*      */ 
/* 2179 */     return bool;
/*      */   }
/*      */ 
/*      */   protected Pageable getPageable()
/*      */   {
/* 2186 */     return this.mDocument;
/*      */   }
/*      */ 
/*      */   protected Graphics2D createPathGraphics(PeekGraphics paramPeekGraphics, PrinterJob paramPrinterJob, Printable paramPrintable, PageFormat paramPageFormat, int paramInt)
/*      */   {
/* 2207 */     return null;
/*      */   }
/*      */ 
/*      */   protected PeekGraphics createPeekGraphics(Graphics2D paramGraphics2D, PrinterJob paramPrinterJob)
/*      */   {
/* 2222 */     return new PeekGraphics(paramGraphics2D, paramPrinterJob);
/*      */   }
/*      */ 
/*      */   protected void initPrinterGraphics(Graphics2D paramGraphics2D, Rectangle2D paramRectangle2D)
/*      */   {
/* 2235 */     paramGraphics2D.setClip(paramRectangle2D);
/* 2236 */     paramGraphics2D.setPaint(Color.black);
/*      */   }
/*      */ 
/*      */   public boolean checkAllowedToPrintToFile()
/*      */   {
/*      */     try
/*      */     {
/* 2246 */       throwPrintToFile();
/* 2247 */       return true; } catch (SecurityException localSecurityException) {
/*      */     }
/* 2249 */     return false;
/*      */   }
/*      */ 
/*      */   private void throwPrintToFile()
/*      */   {
/* 2259 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 2260 */     if (localSecurityManager != null) {
/* 2261 */       if (this.printToFilePermission == null) {
/* 2262 */         this.printToFilePermission = new FilePermission("<<ALL FILES>>", "read,write");
/*      */       }
/*      */ 
/* 2265 */       localSecurityManager.checkPermission(this.printToFilePermission);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String removeControlChars(String paramString)
/*      */   {
/* 2276 */     char[] arrayOfChar1 = paramString.toCharArray();
/* 2277 */     int i = arrayOfChar1.length;
/* 2278 */     char[] arrayOfChar2 = new char[i];
/* 2279 */     int j = 0;
/*      */ 
/* 2281 */     for (int k = 0; k < i; k++) {
/* 2282 */       int m = arrayOfChar1[k];
/* 2283 */       if ((m > 13) || (m < 9) || (m == 11) || (m == 12)) {
/* 2284 */         arrayOfChar2[(j++)] = m;
/*      */       }
/*      */     }
/* 2287 */     if (j == i) {
/* 2288 */       return paramString;
/*      */     }
/* 2290 */     return new String(arrayOfChar2, 0, j);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  174 */     String str1 = (String)AccessController.doPrivileged(new GetPropertyAction("sun.java2d.print.pipeline"));
/*      */ 
/*  178 */     if (str1 != null) {
/*  179 */       if (str1.equalsIgnoreCase("pdl"))
/*  180 */         forcePDL = true;
/*  181 */       else if (str1.equalsIgnoreCase("raster")) {
/*  182 */         forceRaster = true;
/*      */       }
/*      */     }
/*      */ 
/*  186 */     String str2 = (String)AccessController.doPrivileged(new GetPropertyAction("sun.java2d.print.shapetext"));
/*      */ 
/*  190 */     if (str2 != null)
/*  191 */       shapeTextProp = true;
/*      */   }
/*      */ 
/*      */   private class GraphicsState
/*      */   {
/*      */     Rectangle2D region;
/*      */     Shape theClip;
/*      */     AffineTransform theTransform;
/*      */     double sx;
/*      */     double sy;
/*      */ 
/*      */     private GraphicsState()
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.RasterPrinterJob
 * JD-Core Version:    0.6.2
 */