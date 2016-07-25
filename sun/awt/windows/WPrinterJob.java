/*      */ package sun.awt.windows;
/*      */ 
/*      */ import java.awt.Button;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FileDialog;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Label;
/*      */ import java.awt.Panel;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.peer.ComponentPeer;
/*      */ import java.awt.print.PageFormat;
/*      */ import java.awt.print.Pageable;
/*      */ import java.awt.print.Paper;
/*      */ import java.awt.print.Printable;
/*      */ import java.awt.print.PrinterException;
/*      */ import java.awt.print.PrinterJob;
/*      */ import java.io.File;
/*      */ import java.io.FilePermission;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import javax.print.PrintService;
/*      */ import javax.print.PrintServiceLookup;
/*      */ import javax.print.StreamPrintService;
/*      */ import javax.print.attribute.Attribute;
/*      */ import javax.print.attribute.HashPrintRequestAttributeSet;
/*      */ import javax.print.attribute.PrintRequestAttributeSet;
/*      */ import javax.print.attribute.standard.Chromaticity;
/*      */ import javax.print.attribute.standard.Copies;
/*      */ import javax.print.attribute.standard.Destination;
/*      */ import javax.print.attribute.standard.Media;
/*      */ import javax.print.attribute.standard.MediaSize;
/*      */ import javax.print.attribute.standard.MediaSizeName;
/*      */ import javax.print.attribute.standard.MediaTray;
/*      */ import javax.print.attribute.standard.OrientationRequested;
/*      */ import javax.print.attribute.standard.PageRanges;
/*      */ import javax.print.attribute.standard.PrintQuality;
/*      */ import javax.print.attribute.standard.PrinterResolution;
/*      */ import javax.print.attribute.standard.SheetCollate;
/*      */ import javax.print.attribute.standard.Sides;
/*      */ import sun.awt.Win32FontManager;
/*      */ import sun.java2d.Disposer;
/*      */ import sun.java2d.DisposerRecord;
/*      */ import sun.java2d.DisposerTarget;
/*      */ import sun.print.DialogOwner;
/*      */ import sun.print.PeekGraphics;
/*      */ import sun.print.PeekMetrics;
/*      */ import sun.print.RasterPrinterJob;
/*      */ import sun.print.ServiceDialog;
/*      */ import sun.print.SunAlternateMedia;
/*      */ import sun.print.SunMinMaxPage;
/*      */ import sun.print.SunPageSelection;
/*      */ import sun.print.Win32MediaTray;
/*      */ import sun.print.Win32PrintService;
/*      */ import sun.print.Win32PrintServiceLookup;
/*      */ 
/*      */ public class WPrinterJob extends RasterPrinterJob
/*      */   implements DisposerTarget
/*      */ {
/*      */   protected static final long PS_ENDCAP_ROUND = 0L;
/*      */   protected static final long PS_ENDCAP_SQUARE = 256L;
/*      */   protected static final long PS_ENDCAP_FLAT = 512L;
/*      */   protected static final long PS_JOIN_ROUND = 0L;
/*      */   protected static final long PS_JOIN_BEVEL = 4096L;
/*      */   protected static final long PS_JOIN_MITER = 8192L;
/*      */   protected static final int POLYFILL_ALTERNATE = 1;
/*      */   protected static final int POLYFILL_WINDING = 2;
/*      */   private static final int MAX_WCOLOR = 255;
/*      */   private static final int SET_DUP_VERTICAL = 16;
/*      */   private static final int SET_DUP_HORIZONTAL = 32;
/*      */   private static final int SET_RES_HIGH = 64;
/*      */   private static final int SET_RES_LOW = 128;
/*      */   private static final int SET_COLOR = 512;
/*      */   private static final int SET_ORIENTATION = 16384;
/*      */   private static final int SET_COLLATED = 32768;
/*      */   private static final int PD_ALLPAGES = 0;
/*      */   private static final int PD_SELECTION = 1;
/*      */   private static final int PD_PAGENUMS = 2;
/*      */   private static final int PD_NOSELECTION = 4;
/*      */   private static final int PD_COLLATE = 16;
/*      */   private static final int PD_PRINTTOFILE = 32;
/*      */   private static final int DM_ORIENTATION = 1;
/*      */   private static final int DM_PAPERSIZE = 2;
/*      */   private static final int DM_COPIES = 256;
/*      */   private static final int DM_DEFAULTSOURCE = 512;
/*      */   private static final int DM_PRINTQUALITY = 1024;
/*      */   private static final int DM_COLOR = 2048;
/*      */   private static final int DM_DUPLEX = 4096;
/*      */   private static final int DM_YRESOLUTION = 8192;
/*      */   private static final int DM_COLLATE = 32768;
/*      */   private static final short DMCOLLATE_FALSE = 0;
/*      */   private static final short DMCOLLATE_TRUE = 1;
/*      */   private static final short DMORIENT_PORTRAIT = 1;
/*      */   private static final short DMORIENT_LANDSCAPE = 2;
/*      */   private static final short DMCOLOR_MONOCHROME = 1;
/*      */   private static final short DMCOLOR_COLOR = 2;
/*      */   private static final short DMRES_DRAFT = -1;
/*      */   private static final short DMRES_LOW = -2;
/*      */   private static final short DMRES_MEDIUM = -3;
/*      */   private static final short DMRES_HIGH = -4;
/*      */   private static final short DMDUP_SIMPLEX = 1;
/*      */   private static final short DMDUP_VERTICAL = 2;
/*      */   private static final short DMDUP_HORIZONTAL = 3;
/*      */   private static final int MAX_UNKNOWN_PAGES = 9999;
/*  276 */   private boolean driverDoesMultipleCopies = false;
/*  277 */   private boolean driverDoesCollation = false;
/*  278 */   private boolean userRequestedCollation = false;
/*  279 */   private boolean noDefaultPrinter = false;
/*      */ 
/*  303 */   private HandleRecord handleRecord = new HandleRecord();
/*      */   private int mPrintPaperSize;
/*      */   private int mPrintXRes;
/*      */   private int mPrintYRes;
/*      */   private int mPrintPhysX;
/*      */   private int mPrintPhysY;
/*      */   private int mPrintWidth;
/*      */   private int mPrintHeight;
/*      */   private int mPageWidth;
/*      */   private int mPageHeight;
/*      */   private int mAttSides;
/*      */   private int mAttChromaticity;
/*      */   private int mAttXRes;
/*      */   private int mAttYRes;
/*      */   private int mAttQuality;
/*      */   private int mAttCollate;
/*      */   private int mAttCopies;
/*      */   private int mAttMediaSizeName;
/*      */   private int mAttMediaTray;
/*  350 */   private String mDestination = null;
/*      */   private Color mLastColor;
/*      */   private Color mLastTextColor;
/*      */   private String mLastFontFamily;
/*      */   private float mLastFontSize;
/*      */   private int mLastFontStyle;
/*      */   private int mLastRotation;
/*      */   private float mLastAwScale;
/*      */   private PrinterJob pjob;
/*  378 */   private ComponentPeer dialogOwnerPeer = null;
/*      */ 
/*  408 */   private Object disposerReferent = new Object();
/*      */ 
/*  626 */   private String lastNativeService = null;
/*      */ 
/*      */   public WPrinterJob()
/*      */   {
/*  395 */     Disposer.addRecord(this.disposerReferent, this.handleRecord = new HandleRecord());
/*      */ 
/*  397 */     initAttributeMembers();
/*      */   }
/*      */ 
/*      */   public Object getDisposerReferent()
/*      */   {
/*  411 */     return this.disposerReferent;
/*      */   }
/*      */ 
/*      */   public PageFormat pageDialog(PageFormat paramPageFormat)
/*      */     throws HeadlessException
/*      */   {
/*  439 */     if (GraphicsEnvironment.isHeadless()) {
/*  440 */       throw new HeadlessException();
/*      */     }
/*      */ 
/*  443 */     if ((getPrintService() instanceof StreamPrintService)) {
/*  444 */       return super.pageDialog(paramPageFormat);
/*      */     }
/*      */ 
/*  447 */     PageFormat localPageFormat = (PageFormat)paramPageFormat.clone();
/*  448 */     boolean bool = false;
/*      */ 
/*  454 */     WPageDialog localWPageDialog = new WPageDialog((Frame)null, this, localPageFormat, null);
/*      */ 
/*  456 */     localWPageDialog.setRetVal(false);
/*  457 */     localWPageDialog.setVisible(true);
/*  458 */     bool = localWPageDialog.getRetVal();
/*  459 */     localWPageDialog.dispose();
/*      */ 
/*  462 */     if ((bool) && (this.myService != null))
/*      */     {
/*  465 */       String str = getNativePrintService();
/*  466 */       if (!this.myService.getName().equals(str))
/*      */       {
/*      */         try
/*      */         {
/*  470 */           setPrintService(Win32PrintServiceLookup.getWin32PrintLUS().getPrintServiceByName(str));
/*      */         }
/*      */         catch (PrinterException localPrinterException)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  478 */       updatePageAttributes(this.myService, localPageFormat);
/*      */ 
/*  480 */       return localPageFormat;
/*      */     }
/*  482 */     return paramPageFormat;
/*      */   }
/*      */ 
/*      */   private boolean displayNativeDialog()
/*      */   {
/*  489 */     if (this.attributes == null) {
/*  490 */       return false;
/*      */     }
/*      */ 
/*  493 */     DialogOwner localDialogOwner = (DialogOwner)this.attributes.get(DialogOwner.class);
/*  494 */     Frame localFrame = localDialogOwner != null ? localDialogOwner.getOwner() : null;
/*      */ 
/*  496 */     WPrintDialog localWPrintDialog = new WPrintDialog(localFrame, this);
/*  497 */     localWPrintDialog.setRetVal(false);
/*  498 */     localWPrintDialog.setVisible(true);
/*  499 */     boolean bool = localWPrintDialog.getRetVal();
/*  500 */     localWPrintDialog.dispose();
/*      */ 
/*  502 */     Destination localDestination = (Destination)this.attributes.get(Destination.class);
/*      */ 
/*  504 */     if ((localDestination == null) || (!bool)) {
/*  505 */       return bool;
/*      */     }
/*  507 */     String str1 = null;
/*  508 */     String str2 = "sun.print.resources.serviceui";
/*  509 */     ResourceBundle localResourceBundle = ResourceBundle.getBundle(str2);
/*      */     try {
/*  511 */       str1 = localResourceBundle.getString("dialog.printtofile");
/*      */     } catch (MissingResourceException localMissingResourceException) {
/*      */     }
/*  514 */     FileDialog localFileDialog = new FileDialog(localFrame, str1, 1);
/*      */ 
/*  517 */     URI localURI = localDestination.getURI();
/*      */ 
/*  520 */     String str3 = localURI != null ? localURI.getSchemeSpecificPart() : null;
/*      */ 
/*  522 */     if (str3 != null) {
/*  523 */       localObject1 = new File(str3);
/*  524 */       localFileDialog.setFile(((File)localObject1).getName());
/*  525 */       localObject2 = ((File)localObject1).getParentFile();
/*  526 */       if (localObject2 != null)
/*  527 */         localFileDialog.setDirectory(((File)localObject2).getPath());
/*      */     }
/*      */     else {
/*  530 */       localFileDialog.setFile("out.prn");
/*      */     }
/*      */ 
/*  533 */     localFileDialog.setVisible(true);
/*  534 */     Object localObject1 = localFileDialog.getFile();
/*  535 */     if (localObject1 == null) {
/*  536 */       localFileDialog.dispose();
/*  537 */       return false;
/*      */     }
/*  539 */     Object localObject2 = localFileDialog.getDirectory() + (String)localObject1;
/*  540 */     File localFile1 = new File((String)localObject2);
/*  541 */     File localFile2 = localFile1.getParentFile();
/*      */ 
/*  543 */     while (((localFile1.exists()) && ((!localFile1.isFile()) || (!localFile1.canWrite()))) || ((localFile2 != null) && ((!localFile2.exists()) || ((localFile2.exists()) && (!localFile2.canWrite())))))
/*      */     {
/*  547 */       new PrintToFileErrorDialog(localFrame, ServiceDialog.getMsg("dialog.owtitle"), ServiceDialog.getMsg("dialog.writeerror") + " " + (String)localObject2, ServiceDialog.getMsg("button.ok")).setVisible(true);
/*      */ 
/*  552 */       localFileDialog.setVisible(true);
/*  553 */       localObject1 = localFileDialog.getFile();
/*  554 */       if (localObject1 == null) {
/*  555 */         localFileDialog.dispose();
/*  556 */         return false;
/*      */       }
/*  558 */       localObject2 = localFileDialog.getDirectory() + (String)localObject1;
/*  559 */       localFile1 = new File((String)localObject2);
/*  560 */       localFile2 = localFile1.getParentFile();
/*      */     }
/*  562 */     localFileDialog.dispose();
/*  563 */     this.attributes.add(new Destination(localFile1.toURI()));
/*  564 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean printDialog()
/*      */     throws HeadlessException
/*      */   {
/*  580 */     if (GraphicsEnvironment.isHeadless()) {
/*  581 */       throw new HeadlessException();
/*      */     }
/*      */ 
/*  585 */     if (this.attributes == null) {
/*  586 */       this.attributes = new HashPrintRequestAttributeSet();
/*      */     }
/*      */ 
/*  589 */     if ((getPrintService() instanceof StreamPrintService)) {
/*  590 */       return super.printDialog(this.attributes);
/*      */     }
/*      */ 
/*  593 */     if (this.noDefaultPrinter == true) {
/*  594 */       return false;
/*      */     }
/*  596 */     return displayNativeDialog();
/*      */   }
/*      */ 
/*      */   public void setPrintService(PrintService paramPrintService)
/*      */     throws PrinterException
/*      */   {
/*  613 */     super.setPrintService(paramPrintService);
/*  614 */     if ((paramPrintService instanceof StreamPrintService)) {
/*  615 */       return;
/*      */     }
/*  617 */     this.driverDoesMultipleCopies = false;
/*  618 */     this.driverDoesCollation = false;
/*  619 */     setNativePrintServiceIfNeeded(paramPrintService.getName());
/*      */   }
/*      */ 
/*      */   private native void setNativePrintService(String paramString)
/*      */     throws PrinterException;
/*      */ 
/*      */   private void setNativePrintServiceIfNeeded(String paramString)
/*      */     throws PrinterException
/*      */   {
/*  630 */     if ((paramString != null) && (!paramString.equals(this.lastNativeService))) {
/*  631 */       setNativePrintService(paramString);
/*  632 */       this.lastNativeService = paramString;
/*      */     }
/*      */   }
/*      */ 
/*      */   public PrintService getPrintService() {
/*  637 */     if (this.myService == null) {
/*  638 */       String str = getNativePrintService();
/*      */ 
/*  640 */       if (str != null) {
/*  641 */         this.myService = Win32PrintServiceLookup.getWin32PrintLUS().getPrintServiceByName(str);
/*      */ 
/*  645 */         if (this.myService != null) {
/*  646 */           return this.myService;
/*      */         }
/*      */       }
/*      */ 
/*  650 */       this.myService = PrintServiceLookup.lookupDefaultPrintService();
/*  651 */       if (this.myService != null) {
/*      */         try {
/*  653 */           setNativePrintServiceIfNeeded(this.myService.getName());
/*      */         } catch (Exception localException) {
/*  655 */           this.myService = null;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  660 */     return this.myService;
/*      */   }
/*      */ 
/*      */   private native String getNativePrintService();
/*      */ 
/*      */   private void initAttributeMembers() {
/*  666 */     this.mAttSides = 0;
/*  667 */     this.mAttChromaticity = 0;
/*  668 */     this.mAttXRes = 0;
/*  669 */     this.mAttYRes = 0;
/*  670 */     this.mAttQuality = 0;
/*  671 */     this.mAttCollate = -1;
/*  672 */     this.mAttCopies = 0;
/*  673 */     this.mAttMediaTray = 0;
/*  674 */     this.mAttMediaSizeName = 0;
/*  675 */     this.mDestination = null;
/*      */   }
/*      */ 
/*      */   protected void setAttributes(PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */     throws PrinterException
/*      */   {
/*  693 */     initAttributeMembers();
/*  694 */     super.setAttributes(paramPrintRequestAttributeSet);
/*      */ 
/*  696 */     this.mAttCopies = getCopiesInt();
/*  697 */     this.mDestination = this.destinationAttr;
/*      */ 
/*  699 */     if (paramPrintRequestAttributeSet == null) {
/*  700 */       return;
/*      */     }
/*  702 */     Attribute[] arrayOfAttribute = paramPrintRequestAttributeSet.toArray();
/*  703 */     for (int i = 0; i < arrayOfAttribute.length; i++) {
/*  704 */       Object localObject = arrayOfAttribute[i];
/*      */       try {
/*  706 */         if (((Attribute)localObject).getCategory() == Sides.class) {
/*  707 */           setSidesAttrib((Attribute)localObject);
/*      */         }
/*  709 */         else if (((Attribute)localObject).getCategory() == Chromaticity.class) {
/*  710 */           setColorAttrib((Attribute)localObject);
/*      */         }
/*  712 */         else if (((Attribute)localObject).getCategory() == PrinterResolution.class) {
/*  713 */           setResolutionAttrib((Attribute)localObject);
/*      */         }
/*  715 */         else if (((Attribute)localObject).getCategory() == PrintQuality.class) {
/*  716 */           setQualityAttrib((Attribute)localObject);
/*      */         }
/*  718 */         else if (((Attribute)localObject).getCategory() == SheetCollate.class) {
/*  719 */           setCollateAttrib((Attribute)localObject);
/*  720 */         } else if ((((Attribute)localObject).getCategory() == Media.class) || (((Attribute)localObject).getCategory() == SunAlternateMedia.class))
/*      */         {
/*  725 */           if (((Attribute)localObject).getCategory() == SunAlternateMedia.class) {
/*  726 */             Media localMedia = (Media)paramPrintRequestAttributeSet.get(Media.class);
/*  727 */             if ((localMedia == null) || (!(localMedia instanceof MediaTray)))
/*      */             {
/*  729 */               localObject = ((SunAlternateMedia)localObject).getMedia();
/*      */             }
/*      */           }
/*  732 */           if ((localObject instanceof MediaSizeName)) {
/*  733 */             setWin32MediaAttrib((Attribute)localObject);
/*      */           }
/*  735 */           if ((localObject instanceof MediaTray))
/*  736 */             setMediaTrayAttrib((Attribute)localObject);
/*      */         }
/*      */       }
/*      */       catch (ClassCastException localClassCastException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private native void getDefaultPage(PageFormat paramPageFormat);
/*      */ 
/*      */   public PageFormat defaultPage(PageFormat paramPageFormat)
/*      */   {
/*  759 */     PageFormat localPageFormat = (PageFormat)paramPageFormat.clone();
/*  760 */     getDefaultPage(localPageFormat);
/*  761 */     return localPageFormat;
/*      */   }
/*      */ 
/*      */   protected native void validatePaper(Paper paramPaper1, Paper paramPaper2);
/*      */ 
/*      */   protected Graphics2D createPathGraphics(PeekGraphics paramPeekGraphics, PrinterJob paramPrinterJob, Printable paramPrintable, PageFormat paramPageFormat, int paramInt)
/*      */   {
/*  789 */     PeekMetrics localPeekMetrics = paramPeekGraphics.getMetrics();
/*      */     WPathGraphics localWPathGraphics;
/*  798 */     if ((!forcePDL) && ((forceRaster == true) || (localPeekMetrics.hasNonSolidColors()) || (localPeekMetrics.hasCompositing())))
/*      */     {
/*  802 */       localWPathGraphics = null;
/*      */     } else {
/*  804 */       BufferedImage localBufferedImage = new BufferedImage(8, 8, 1);
/*      */ 
/*  806 */       Graphics2D localGraphics2D = localBufferedImage.createGraphics();
/*      */ 
/*  808 */       boolean bool = !paramPeekGraphics.getAWTDrawingOnly();
/*  809 */       localWPathGraphics = new WPathGraphics(localGraphics2D, paramPrinterJob, paramPrintable, paramPageFormat, paramInt, bool);
/*      */     }
/*      */ 
/*  814 */     return localWPathGraphics;
/*      */   }
/*      */ 
/*      */   protected double getXRes()
/*      */   {
/*  819 */     if (this.mAttXRes != 0) {
/*  820 */       return this.mAttXRes;
/*      */     }
/*  822 */     return this.mPrintXRes;
/*      */   }
/*      */ 
/*      */   protected double getYRes()
/*      */   {
/*  827 */     if (this.mAttYRes != 0) {
/*  828 */       return this.mAttYRes;
/*      */     }
/*  830 */     return this.mPrintYRes;
/*      */   }
/*      */ 
/*      */   protected double getPhysicalPrintableX(Paper paramPaper)
/*      */   {
/*  835 */     return this.mPrintPhysX;
/*      */   }
/*      */ 
/*      */   protected double getPhysicalPrintableY(Paper paramPaper) {
/*  839 */     return this.mPrintPhysY;
/*      */   }
/*      */ 
/*      */   protected double getPhysicalPrintableWidth(Paper paramPaper) {
/*  843 */     return this.mPrintWidth;
/*      */   }
/*      */ 
/*      */   protected double getPhysicalPrintableHeight(Paper paramPaper) {
/*  847 */     return this.mPrintHeight;
/*      */   }
/*      */ 
/*      */   protected double getPhysicalPageWidth(Paper paramPaper) {
/*  851 */     return this.mPageWidth;
/*      */   }
/*      */ 
/*      */   protected double getPhysicalPageHeight(Paper paramPaper) {
/*  855 */     return this.mPageHeight;
/*      */   }
/*      */ 
/*      */   protected boolean isCollated()
/*      */   {
/*  866 */     return this.userRequestedCollation;
/*      */   }
/*      */ 
/*      */   protected int getCollatedCopies()
/*      */   {
/*  878 */     debug_println("driverDoesMultipleCopies=" + this.driverDoesMultipleCopies + " driverDoesCollation=" + this.driverDoesCollation);
/*      */ 
/*  880 */     if ((super.isCollated()) && (!this.driverDoesCollation))
/*      */     {
/*  883 */       this.mAttCollate = 0;
/*  884 */       this.mAttCopies = 1;
/*  885 */       return getCopies();
/*      */     }
/*      */ 
/*  888 */     return 1;
/*      */   }
/*      */ 
/*      */   protected int getNoncollatedCopies()
/*      */   {
/*  900 */     if ((this.driverDoesMultipleCopies) || (super.isCollated())) {
/*  901 */       return 1;
/*      */     }
/*  903 */     return getCopies();
/*      */   }
/*      */ 
/*      */   private long getPrintDC()
/*      */   {
/*  914 */     return this.handleRecord.mPrintDC;
/*      */   }
/*      */ 
/*      */   private void setPrintDC(long paramLong) {
/*  918 */     this.handleRecord.mPrintDC = paramLong;
/*      */   }
/*      */ 
/*      */   private long getDevMode() {
/*  922 */     return this.handleRecord.mPrintHDevMode;
/*      */   }
/*      */ 
/*      */   private void setDevMode(long paramLong) {
/*  926 */     this.handleRecord.mPrintHDevMode = paramLong;
/*      */   }
/*      */ 
/*      */   private long getDevNames() {
/*  930 */     return this.handleRecord.mPrintHDevNames;
/*      */   }
/*      */ 
/*      */   private void setDevNames(long paramLong) {
/*  934 */     this.handleRecord.mPrintHDevNames = paramLong;
/*      */   }
/*      */ 
/*      */   protected void beginPath() {
/*  938 */     beginPath(getPrintDC());
/*      */   }
/*      */ 
/*      */   protected void endPath() {
/*  942 */     endPath(getPrintDC());
/*      */   }
/*      */ 
/*      */   protected void closeFigure() {
/*  946 */     closeFigure(getPrintDC());
/*      */   }
/*      */ 
/*      */   protected void fillPath() {
/*  950 */     fillPath(getPrintDC());
/*      */   }
/*      */ 
/*      */   protected void moveTo(float paramFloat1, float paramFloat2) {
/*  954 */     moveTo(getPrintDC(), paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   protected void lineTo(float paramFloat1, float paramFloat2) {
/*  958 */     lineTo(getPrintDC(), paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   protected void polyBezierTo(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
/*      */   {
/*  965 */     polyBezierTo(getPrintDC(), paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
/*      */   }
/*      */ 
/*      */   protected void setPolyFillMode(int paramInt)
/*      */   {
/*  977 */     setPolyFillMode(getPrintDC(), paramInt);
/*      */   }
/*      */ 
/*      */   protected void selectSolidBrush(Color paramColor)
/*      */   {
/*  990 */     if (!paramColor.equals(this.mLastColor)) {
/*  991 */       this.mLastColor = paramColor;
/*  992 */       float[] arrayOfFloat = paramColor.getRGBColorComponents(null);
/*      */ 
/*  994 */       selectSolidBrush(getPrintDC(), (int)(arrayOfFloat[0] * 255.0F), (int)(arrayOfFloat[1] * 255.0F), (int)(arrayOfFloat[2] * 255.0F));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int getPenX()
/*      */   {
/* 1007 */     return getPenX(getPrintDC());
/*      */   }
/*      */ 
/*      */   protected int getPenY()
/*      */   {
/* 1017 */     return getPenY(getPrintDC());
/*      */   }
/*      */ 
/*      */   protected void selectClipPath()
/*      */   {
/* 1025 */     selectClipPath(getPrintDC());
/*      */   }
/*      */ 
/*      */   protected void frameRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
/*      */   {
/* 1030 */     frameRect(getPrintDC(), paramFloat1, paramFloat2, paramFloat3, paramFloat4);
/*      */   }
/*      */ 
/*      */   protected void fillRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, Color paramColor)
/*      */   {
/* 1035 */     float[] arrayOfFloat = paramColor.getRGBColorComponents(null);
/*      */ 
/* 1037 */     fillRect(getPrintDC(), paramFloat1, paramFloat2, paramFloat3, paramFloat4, (int)(arrayOfFloat[0] * 255.0F), (int)(arrayOfFloat[1] * 255.0F), (int)(arrayOfFloat[2] * 255.0F));
/*      */   }
/*      */ 
/*      */   protected void selectPen(float paramFloat, Color paramColor)
/*      */   {
/* 1046 */     float[] arrayOfFloat = paramColor.getRGBColorComponents(null);
/*      */ 
/* 1048 */     selectPen(getPrintDC(), paramFloat, (int)(arrayOfFloat[0] * 255.0F), (int)(arrayOfFloat[1] * 255.0F), (int)(arrayOfFloat[2] * 255.0F));
/*      */   }
/*      */ 
/*      */   protected boolean selectStylePen(int paramInt1, int paramInt2, float paramFloat, Color paramColor)
/*      */   {
/* 1061 */     float[] arrayOfFloat = paramColor.getRGBColorComponents(null);
/*      */     long l1;
/* 1063 */     switch (paramInt1) { case 0:
/* 1064 */       l1 = 512L; break;
/*      */     case 1:
/* 1065 */       l1 = 0L; break;
/*      */     case 2:
/*      */     default:
/* 1067 */       l1 = 256L;
/*      */     }
/*      */     long l2;
/* 1070 */     switch (paramInt2) { case 2:
/* 1071 */       l2 = 4096L; break;
/*      */     case 0:
/*      */     default:
/* 1073 */       l2 = 8192L; break;
/*      */     case 1:
/* 1074 */       l2 = 0L;
/*      */     }
/*      */ 
/* 1077 */     return selectStylePen(getPrintDC(), l1, l2, paramFloat, (int)(arrayOfFloat[0] * 255.0F), (int)(arrayOfFloat[1] * 255.0F), (int)(arrayOfFloat[2] * 255.0F));
/*      */   }
/*      */ 
/*      */   protected boolean setFont(String paramString, float paramFloat1, int paramInt1, int paramInt2, float paramFloat2)
/*      */   {
/* 1090 */     boolean bool = true;
/*      */ 
/* 1092 */     if ((!paramString.equals(this.mLastFontFamily)) || (paramFloat1 != this.mLastFontSize) || (paramInt1 != this.mLastFontStyle) || (paramInt2 != this.mLastRotation) || (paramFloat2 != this.mLastAwScale))
/*      */     {
/* 1098 */       bool = setFont(getPrintDC(), paramString, paramFloat1, (paramInt1 & 0x1) != 0, (paramInt1 & 0x2) != 0, paramInt2, paramFloat2);
/*      */ 
/* 1104 */       if (bool) {
/* 1105 */         this.mLastFontFamily = paramString;
/* 1106 */         this.mLastFontSize = paramFloat1;
/* 1107 */         this.mLastFontStyle = paramInt1;
/* 1108 */         this.mLastRotation = paramInt2;
/* 1109 */         this.mLastAwScale = paramFloat2;
/*      */       }
/*      */     }
/* 1112 */     return bool;
/*      */   }
/*      */ 
/*      */   protected void setTextColor(Color paramColor)
/*      */   {
/* 1122 */     if (!paramColor.equals(this.mLastTextColor)) {
/* 1123 */       this.mLastTextColor = paramColor;
/* 1124 */       float[] arrayOfFloat = paramColor.getRGBColorComponents(null);
/*      */ 
/* 1126 */       setTextColor(getPrintDC(), (int)(arrayOfFloat[0] * 255.0F), (int)(arrayOfFloat[1] * 255.0F), (int)(arrayOfFloat[2] * 255.0F));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String removeControlChars(String paramString)
/*      */   {
/* 1137 */     return super.removeControlChars(paramString);
/*      */   }
/*      */ 
/*      */   protected void textOut(String paramString, float paramFloat1, float paramFloat2, float[] paramArrayOfFloat)
/*      */   {
/* 1152 */     String str = removeControlChars(paramString);
/* 1153 */     assert ((paramArrayOfFloat == null) || (str.length() == paramString.length()));
/* 1154 */     if (str.length() == 0) {
/* 1155 */       return;
/*      */     }
/* 1157 */     textOut(getPrintDC(), str, str.length(), false, paramFloat1, paramFloat2, paramArrayOfFloat);
/*      */   }
/*      */ 
/*      */   protected void glyphsOut(int[] paramArrayOfInt, float paramFloat1, float paramFloat2, float[] paramArrayOfFloat)
/*      */   {
/* 1175 */     char[] arrayOfChar = new char[paramArrayOfInt.length];
/* 1176 */     for (int i = 0; i < paramArrayOfInt.length; i++) {
/* 1177 */       arrayOfChar[i] = ((char)(paramArrayOfInt[i] & 0xFFFF));
/*      */     }
/* 1179 */     String str = new String(arrayOfChar);
/* 1180 */     textOut(getPrintDC(), str, paramArrayOfInt.length, true, paramFloat1, paramFloat2, paramArrayOfFloat);
/*      */   }
/*      */ 
/*      */   protected int getGDIAdvance(String paramString)
/*      */   {
/* 1194 */     paramString = removeControlChars(paramString);
/* 1195 */     if (paramString.length() == 0) {
/* 1196 */       return 0;
/*      */     }
/* 1198 */     return getGDIAdvance(getPrintDC(), paramString);
/*      */   }
/*      */ 
/*      */   protected void drawImage3ByteBGR(byte[] paramArrayOfByte, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8)
/*      */   {
/* 1220 */     drawDIBImage(getPrintDC(), paramArrayOfByte, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, 24, null);
/*      */   }
/*      */ 
/*      */   protected void drawDIBImage(byte[] paramArrayOfByte, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, int paramInt, IndexColorModel paramIndexColorModel)
/*      */   {
/* 1251 */     int i = 24;
/* 1252 */     byte[] arrayOfByte = null;
/*      */ 
/* 1254 */     if (paramIndexColorModel != null) {
/* 1255 */       i = paramInt;
/* 1256 */       arrayOfByte = new byte[(1 << paramIndexColorModel.getPixelSize()) * 4];
/* 1257 */       for (int j = 0; j < paramIndexColorModel.getMapSize(); j++) {
/* 1258 */         arrayOfByte[(j * 4 + 0)] = ((byte)(paramIndexColorModel.getBlue(j) & 0xFF));
/* 1259 */         arrayOfByte[(j * 4 + 1)] = ((byte)(paramIndexColorModel.getGreen(j) & 0xFF));
/* 1260 */         arrayOfByte[(j * 4 + 2)] = ((byte)(paramIndexColorModel.getRed(j) & 0xFF));
/*      */       }
/*      */     }
/*      */ 
/* 1264 */     drawDIBImage(getPrintDC(), paramArrayOfByte, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, i, arrayOfByte);
/*      */   }
/*      */ 
/*      */   protected void startPage(PageFormat paramPageFormat, Printable paramPrintable, int paramInt, boolean paramBoolean)
/*      */   {
/* 1283 */     invalidateCachedState();
/*      */ 
/* 1285 */     deviceStartPage(paramPageFormat, paramPrintable, paramInt, paramBoolean);
/*      */   }
/*      */ 
/*      */   protected void endPage(PageFormat paramPageFormat, Printable paramPrintable, int paramInt)
/*      */   {
/* 1294 */     deviceEndPage(paramPageFormat, paramPrintable, paramInt);
/*      */   }
/*      */ 
/*      */   private void invalidateCachedState()
/*      */   {
/* 1301 */     this.mLastColor = null;
/* 1302 */     this.mLastTextColor = null;
/* 1303 */     this.mLastFontFamily = null;
/*      */   }
/*      */ 
/*      */   public void setCopies(int paramInt)
/*      */   {
/* 1310 */     super.setCopies(paramInt);
/* 1311 */     this.mAttCopies = paramInt;
/* 1312 */     setNativeCopies(paramInt);
/*      */   }
/*      */ 
/*      */   public native void setNativeCopies(int paramInt);
/*      */ 
/*      */   private native boolean jobSetup(Pageable paramPageable, boolean paramBoolean);
/*      */ 
/*      */   protected native void initPrinter();
/*      */ 
/*      */   private native boolean _startDoc(String paramString1, String paramString2)
/*      */     throws PrinterException;
/*      */ 
/*      */   protected void startDoc()
/*      */     throws PrinterException
/*      */   {
/* 1350 */     if (!_startDoc(this.mDestination, getJobName()))
/* 1351 */       cancel();
/*      */   }
/*      */ 
/*      */   protected native void endDoc();
/*      */ 
/*      */   protected native void abortDoc();
/*      */ 
/*      */   private static native void deleteDC(long paramLong1, long paramLong2, long paramLong3);
/*      */ 
/*      */   protected native void deviceStartPage(PageFormat paramPageFormat, Printable paramPrintable, int paramInt, boolean paramBoolean);
/*      */ 
/*      */   protected native void deviceEndPage(PageFormat paramPageFormat, Printable paramPrintable, int paramInt);
/*      */ 
/*      */   protected native void printBand(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*      */ 
/*      */   protected native void beginPath(long paramLong);
/*      */ 
/*      */   protected native void endPath(long paramLong);
/*      */ 
/*      */   protected native void closeFigure(long paramLong);
/*      */ 
/*      */   protected native void fillPath(long paramLong);
/*      */ 
/*      */   protected native void moveTo(long paramLong, float paramFloat1, float paramFloat2);
/*      */ 
/*      */   protected native void lineTo(long paramLong, float paramFloat1, float paramFloat2);
/*      */ 
/*      */   protected native void polyBezierTo(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);
/*      */ 
/*      */   protected native void setPolyFillMode(long paramLong, int paramInt);
/*      */ 
/*      */   protected native void selectSolidBrush(long paramLong, int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */   protected native int getPenX(long paramLong);
/*      */ 
/*      */   protected native int getPenY(long paramLong);
/*      */ 
/*      */   protected native void selectClipPath(long paramLong);
/*      */ 
/*      */   protected native void frameRect(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
/*      */ 
/*      */   protected native void fillRect(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */   protected native void selectPen(long paramLong, float paramFloat, int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */   protected native boolean selectStylePen(long paramLong1, long paramLong2, long paramLong3, float paramFloat, int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */   protected native boolean setFont(long paramLong, String paramString, float paramFloat1, boolean paramBoolean1, boolean paramBoolean2, int paramInt, float paramFloat2);
/*      */ 
/*      */   protected native void setTextColor(long paramLong, int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */   protected native void textOut(long paramLong, String paramString, int paramInt, boolean paramBoolean, float paramFloat1, float paramFloat2, float[] paramArrayOfFloat);
/*      */ 
/*      */   private native int getGDIAdvance(long paramLong, String paramString);
/*      */ 
/*      */   private native void drawDIBImage(long paramLong, byte[] paramArrayOfByte1, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, int paramInt, byte[] paramArrayOfByte2);
/*      */ 
/*      */   private final String getPrinterAttrib()
/*      */   {
/* 1560 */     PrintService localPrintService = getPrintService();
/* 1561 */     String str = localPrintService != null ? localPrintService.getName() : null;
/* 1562 */     return str;
/*      */   }
/*      */ 
/*      */   private final boolean getCollateAttrib()
/*      */   {
/* 1567 */     return this.mAttCollate == 1;
/*      */   }
/*      */ 
/*      */   private void setCollateAttrib(Attribute paramAttribute) {
/* 1571 */     if (paramAttribute == SheetCollate.COLLATED)
/* 1572 */       this.mAttCollate = 1;
/*      */     else
/* 1574 */       this.mAttCollate = 0;
/*      */   }
/*      */ 
/*      */   private void setCollateAttrib(Attribute paramAttribute, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */   {
/* 1580 */     setCollateAttrib(paramAttribute);
/* 1581 */     paramPrintRequestAttributeSet.add(paramAttribute);
/*      */   }
/*      */ 
/*      */   private final int getOrientAttrib()
/*      */   {
/* 1587 */     int i = 1;
/* 1588 */     OrientationRequested localOrientationRequested = this.attributes == null ? null : (OrientationRequested)this.attributes.get(OrientationRequested.class);
/*      */ 
/* 1590 */     if (localOrientationRequested != null) {
/* 1591 */       if (localOrientationRequested == OrientationRequested.REVERSE_LANDSCAPE)
/* 1592 */         i = 2;
/* 1593 */       else if (localOrientationRequested == OrientationRequested.LANDSCAPE) {
/* 1594 */         i = 0;
/*      */       }
/*      */     }
/*      */ 
/* 1598 */     return i;
/*      */   }
/*      */ 
/*      */   private void setOrientAttrib(Attribute paramAttribute, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */   {
/* 1603 */     if (paramPrintRequestAttributeSet != null)
/* 1604 */       paramPrintRequestAttributeSet.add(paramAttribute);
/*      */   }
/*      */ 
/*      */   private final int getCopiesAttrib()
/*      */   {
/* 1610 */     return getCopiesInt();
/*      */   }
/*      */ 
/*      */   private final void setRangeCopiesAttribute(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
/*      */   {
/* 1616 */     if (this.attributes != null) {
/* 1617 */       if (paramBoolean) {
/* 1618 */         this.attributes.add(new PageRanges(paramInt1, paramInt2));
/* 1619 */         setPageRange(paramInt1, paramInt2);
/*      */       }
/* 1621 */       this.attributes.add(new Copies(paramInt3));
/*      */ 
/* 1626 */       super.setCopies(paramInt3);
/* 1627 */       this.mAttCopies = paramInt3;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final int getFromPageAttrib()
/*      */   {
/* 1633 */     if (this.attributes != null) {
/* 1634 */       PageRanges localPageRanges = (PageRanges)this.attributes.get(PageRanges.class);
/*      */ 
/* 1636 */       if (localPageRanges != null) {
/* 1637 */         int[][] arrayOfInt = localPageRanges.getMembers();
/* 1638 */         return arrayOfInt[0][0];
/*      */       }
/*      */     }
/* 1641 */     return getMinPageAttrib();
/*      */   }
/*      */ 
/*      */   private final int getToPageAttrib()
/*      */   {
/* 1646 */     if (this.attributes != null) {
/* 1647 */       PageRanges localPageRanges = (PageRanges)this.attributes.get(PageRanges.class);
/*      */ 
/* 1649 */       if (localPageRanges != null) {
/* 1650 */         int[][] arrayOfInt = localPageRanges.getMembers();
/* 1651 */         return arrayOfInt[(arrayOfInt.length - 1)][1];
/*      */       }
/*      */     }
/* 1654 */     return getMaxPageAttrib();
/*      */   }
/*      */ 
/*      */   private final int getMinPageAttrib() {
/* 1658 */     if (this.attributes != null) {
/* 1659 */       SunMinMaxPage localSunMinMaxPage = (SunMinMaxPage)this.attributes.get(SunMinMaxPage.class);
/*      */ 
/* 1661 */       if (localSunMinMaxPage != null) {
/* 1662 */         return localSunMinMaxPage.getMin();
/*      */       }
/*      */     }
/* 1665 */     return 1;
/*      */   }
/*      */ 
/*      */   private final int getMaxPageAttrib() {
/* 1669 */     if (this.attributes != null) {
/* 1670 */       localObject = (SunMinMaxPage)this.attributes.get(SunMinMaxPage.class);
/*      */ 
/* 1672 */       if (localObject != null) {
/* 1673 */         return ((SunMinMaxPage)localObject).getMax();
/*      */       }
/*      */     }
/*      */ 
/* 1677 */     Object localObject = getPageable();
/* 1678 */     if (localObject != null) {
/* 1679 */       int i = ((Pageable)localObject).getNumberOfPages();
/* 1680 */       if (i <= -1) {
/* 1681 */         i = 9999;
/*      */       }
/* 1683 */       return i == 0 ? 1 : i;
/*      */     }
/*      */ 
/* 1686 */     return 2147483647;
/*      */   }
/*      */ 
/*      */   private final boolean getDestAttrib() {
/* 1690 */     return this.mDestination != null;
/*      */   }
/*      */ 
/*      */   private final int getQualityAttrib()
/*      */   {
/* 1695 */     return this.mAttQuality;
/*      */   }
/*      */ 
/*      */   private void setQualityAttrib(Attribute paramAttribute) {
/* 1699 */     if (paramAttribute == PrintQuality.HIGH)
/* 1700 */       this.mAttQuality = -4;
/* 1701 */     else if (paramAttribute == PrintQuality.NORMAL)
/* 1702 */       this.mAttQuality = -3;
/*      */     else
/* 1704 */       this.mAttQuality = -2;
/*      */   }
/*      */ 
/*      */   private void setQualityAttrib(Attribute paramAttribute, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */   {
/* 1710 */     setQualityAttrib(paramAttribute);
/* 1711 */     paramPrintRequestAttributeSet.add(paramAttribute);
/*      */   }
/*      */ 
/*      */   private final int getColorAttrib()
/*      */   {
/* 1716 */     return this.mAttChromaticity;
/*      */   }
/*      */ 
/*      */   private void setColorAttrib(Attribute paramAttribute) {
/* 1720 */     if (paramAttribute == Chromaticity.COLOR)
/* 1721 */       this.mAttChromaticity = 2;
/*      */     else
/* 1723 */       this.mAttChromaticity = 1;
/*      */   }
/*      */ 
/*      */   private void setColorAttrib(Attribute paramAttribute, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */   {
/* 1729 */     setColorAttrib(paramAttribute);
/* 1730 */     paramPrintRequestAttributeSet.add(paramAttribute);
/*      */   }
/*      */ 
/*      */   private final int getSidesAttrib()
/*      */   {
/* 1735 */     return this.mAttSides;
/*      */   }
/*      */ 
/*      */   private void setSidesAttrib(Attribute paramAttribute) {
/* 1739 */     if (paramAttribute == Sides.TWO_SIDED_LONG_EDGE)
/* 1740 */       this.mAttSides = 2;
/* 1741 */     else if (paramAttribute == Sides.TWO_SIDED_SHORT_EDGE)
/* 1742 */       this.mAttSides = 3;
/*      */     else
/* 1744 */       this.mAttSides = 1;
/*      */   }
/*      */ 
/*      */   private void setSidesAttrib(Attribute paramAttribute, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */   {
/* 1750 */     setSidesAttrib(paramAttribute);
/* 1751 */     paramPrintRequestAttributeSet.add(paramAttribute);
/*      */   }
/*      */ 
/*      */   private final int[] getWin32MediaAttrib()
/*      */   {
/* 1756 */     int[] arrayOfInt = { 0, 0 };
/* 1757 */     if (this.attributes != null) {
/* 1758 */       Media localMedia = (Media)this.attributes.get(Media.class);
/* 1759 */       if ((localMedia instanceof MediaSizeName)) {
/* 1760 */         MediaSizeName localMediaSizeName = (MediaSizeName)localMedia;
/* 1761 */         MediaSize localMediaSize = MediaSize.getMediaSizeForName(localMediaSizeName);
/* 1762 */         if (localMediaSize != null) {
/* 1763 */           arrayOfInt[0] = ((int)(localMediaSize.getX(25400) * 72.0D));
/* 1764 */           arrayOfInt[1] = ((int)(localMediaSize.getY(25400) * 72.0D));
/*      */         }
/*      */       }
/*      */     }
/* 1768 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   private void setWin32MediaAttrib(Attribute paramAttribute) {
/* 1772 */     if (!(paramAttribute instanceof MediaSizeName)) {
/* 1773 */       return;
/*      */     }
/* 1775 */     MediaSizeName localMediaSizeName = (MediaSizeName)paramAttribute;
/* 1776 */     this.mAttMediaSizeName = ((Win32PrintService)this.myService).findPaperID(localMediaSizeName);
/*      */   }
/*      */ 
/*      */   private void addPaperSize(PrintRequestAttributeSet paramPrintRequestAttributeSet, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1782 */     if (paramPrintRequestAttributeSet == null) {
/* 1783 */       return;
/*      */     }
/* 1785 */     MediaSizeName localMediaSizeName = ((Win32PrintService)this.myService).findWin32Media(paramInt1);
/*      */ 
/* 1787 */     if (localMediaSizeName == null) {
/* 1788 */       localMediaSizeName = ((Win32PrintService)this.myService).findMatchingMediaSizeNameMM(paramInt2, paramInt3);
/*      */     }
/*      */ 
/* 1792 */     if (localMediaSizeName != null)
/* 1793 */       paramPrintRequestAttributeSet.add(localMediaSizeName);
/*      */   }
/*      */ 
/*      */   private void setWin32MediaAttrib(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1798 */     addPaperSize(this.attributes, paramInt1, paramInt2, paramInt3);
/* 1799 */     this.mAttMediaSizeName = paramInt1;
/*      */   }
/*      */ 
/*      */   private void setMediaTrayAttrib(Attribute paramAttribute)
/*      */   {
/* 1804 */     if (paramAttribute == MediaTray.BOTTOM)
/* 1805 */       this.mAttMediaTray = 2;
/* 1806 */     else if (paramAttribute == MediaTray.ENVELOPE)
/* 1807 */       this.mAttMediaTray = 5;
/* 1808 */     else if (paramAttribute == MediaTray.LARGE_CAPACITY)
/* 1809 */       this.mAttMediaTray = 11;
/* 1810 */     else if (paramAttribute == MediaTray.MAIN)
/* 1811 */       this.mAttMediaTray = 1;
/* 1812 */     else if (paramAttribute == MediaTray.MANUAL)
/* 1813 */       this.mAttMediaTray = 4;
/* 1814 */     else if (paramAttribute == MediaTray.MIDDLE)
/* 1815 */       this.mAttMediaTray = 3;
/* 1816 */     else if (paramAttribute == MediaTray.SIDE)
/*      */     {
/* 1818 */       this.mAttMediaTray = 7;
/* 1819 */     } else if (paramAttribute == MediaTray.TOP) {
/* 1820 */       this.mAttMediaTray = 1;
/*      */     }
/* 1822 */     else if ((paramAttribute instanceof Win32MediaTray))
/* 1823 */       this.mAttMediaTray = ((Win32MediaTray)paramAttribute).winID;
/*      */     else
/* 1825 */       this.mAttMediaTray = 1;
/*      */   }
/*      */ 
/*      */   private void setMediaTrayAttrib(int paramInt)
/*      */   {
/* 1831 */     this.mAttMediaTray = paramInt;
/* 1832 */     MediaTray localMediaTray = ((Win32PrintService)this.myService).findMediaTray(paramInt);
/*      */   }
/*      */ 
/*      */   private int getMediaTrayAttrib() {
/* 1836 */     return this.mAttMediaTray;
/*      */   }
/*      */ 
/*      */   private final int getSelectAttrib() {
/* 1840 */     if (this.attributes != null) {
/* 1841 */       SunPageSelection localSunPageSelection = (SunPageSelection)this.attributes.get(SunPageSelection.class);
/*      */ 
/* 1843 */       if (localSunPageSelection == SunPageSelection.RANGE)
/* 1844 */         return 2;
/* 1845 */       if (localSunPageSelection == SunPageSelection.SELECTION)
/* 1846 */         return 1;
/* 1847 */       if (localSunPageSelection == SunPageSelection.ALL) {
/* 1848 */         return 0;
/*      */       }
/*      */     }
/* 1851 */     return 4;
/*      */   }
/*      */ 
/*      */   private final boolean getPrintToFileEnabled() {
/* 1855 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1856 */     if (localSecurityManager != null) {
/* 1857 */       FilePermission localFilePermission = new FilePermission("<<ALL FILES>>", "read,write");
/*      */       try
/*      */       {
/* 1860 */         localSecurityManager.checkPermission(localFilePermission);
/*      */       } catch (SecurityException localSecurityException) {
/* 1862 */         return false;
/*      */       }
/*      */     }
/* 1865 */     return true;
/*      */   }
/*      */ 
/*      */   private final void setNativeAttributes(int paramInt1, int paramInt2, int paramInt3) {
/* 1869 */     if (this.attributes == null)
/*      */       return;
/*      */     Object localObject;
/* 1872 */     if ((paramInt1 & 0x20) != 0) {
/* 1873 */       localObject = (Destination)this.attributes.get(Destination.class);
/*      */ 
/* 1875 */       if (localObject == null)
/*      */         try {
/* 1877 */           this.attributes.add(new Destination(new File("./out.prn").toURI()));
/*      */         }
/*      */         catch (SecurityException localSecurityException) {
/*      */           try {
/* 1881 */             this.attributes.add(new Destination(new URI("file:out.prn")));
/*      */           }
/*      */           catch (URISyntaxException localURISyntaxException) {
/*      */           }
/*      */         }
/*      */     }
/*      */     else {
/* 1888 */       this.attributes.remove(Destination.class);
/*      */     }
/*      */ 
/* 1891 */     if ((paramInt1 & 0x10) != 0)
/* 1892 */       setCollateAttrib(SheetCollate.COLLATED, this.attributes);
/*      */     else {
/* 1894 */       setCollateAttrib(SheetCollate.UNCOLLATED, this.attributes);
/*      */     }
/*      */ 
/* 1897 */     if ((paramInt1 & 0x2) != 0)
/* 1898 */       this.attributes.add(SunPageSelection.RANGE);
/* 1899 */     else if ((paramInt1 & 0x1) != 0)
/* 1900 */       this.attributes.add(SunPageSelection.SELECTION);
/*      */     else {
/* 1902 */       this.attributes.add(SunPageSelection.ALL);
/*      */     }
/*      */ 
/* 1905 */     if ((paramInt2 & 0x1) != 0) {
/* 1906 */       if ((paramInt3 & 0x4000) != 0)
/* 1907 */         setOrientAttrib(OrientationRequested.LANDSCAPE, this.attributes);
/*      */       else {
/* 1909 */         setOrientAttrib(OrientationRequested.PORTRAIT, this.attributes);
/*      */       }
/*      */     }
/*      */ 
/* 1913 */     if ((paramInt2 & 0x800) != 0) {
/* 1914 */       if ((paramInt3 & 0x200) != 0)
/* 1915 */         setColorAttrib(Chromaticity.COLOR, this.attributes);
/*      */       else {
/* 1917 */         setColorAttrib(Chromaticity.MONOCHROME, this.attributes);
/*      */       }
/*      */     }
/*      */ 
/* 1921 */     if ((paramInt2 & 0x400) != 0)
/*      */     {
/* 1923 */       if ((paramInt3 & 0x80) != 0)
/* 1924 */         localObject = PrintQuality.DRAFT;
/* 1925 */       else if ((paramInt2 & 0x40) != 0)
/* 1926 */         localObject = PrintQuality.HIGH;
/*      */       else {
/* 1928 */         localObject = PrintQuality.NORMAL;
/*      */       }
/* 1930 */       setQualityAttrib((Attribute)localObject, this.attributes);
/*      */     }
/*      */ 
/* 1933 */     if ((paramInt2 & 0x1000) != 0)
/*      */     {
/* 1935 */       if ((paramInt3 & 0x10) != 0)
/* 1936 */         localObject = Sides.TWO_SIDED_LONG_EDGE;
/* 1937 */       else if ((paramInt3 & 0x20) != 0)
/* 1938 */         localObject = Sides.TWO_SIDED_SHORT_EDGE;
/*      */       else {
/* 1940 */         localObject = Sides.ONE_SIDED;
/*      */       }
/* 1942 */       setSidesAttrib((Attribute)localObject, this.attributes);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void getDevModeValues(PrintRequestAttributeSet paramPrintRequestAttributeSet, DevModeValues paramDevModeValues)
/*      */   {
/* 1962 */     Copies localCopies = (Copies)paramPrintRequestAttributeSet.get(Copies.class);
/* 1963 */     if (localCopies != null) {
/* 1964 */       paramDevModeValues.dmFields |= 256;
/* 1965 */       paramDevModeValues.copies = ((short)localCopies.getValue());
/*      */     }
/*      */ 
/* 1968 */     SheetCollate localSheetCollate = (SheetCollate)paramPrintRequestAttributeSet.get(SheetCollate.class);
/* 1969 */     if (localSheetCollate != null) {
/* 1970 */       paramDevModeValues.dmFields |= 32768;
/* 1971 */       paramDevModeValues.collate = (localSheetCollate == SheetCollate.COLLATED ? 1 : 0);
/*      */     }
/*      */ 
/* 1975 */     Chromaticity localChromaticity = (Chromaticity)paramPrintRequestAttributeSet.get(Chromaticity.class);
/* 1976 */     if (localChromaticity != null) {
/* 1977 */       paramDevModeValues.dmFields |= 2048;
/* 1978 */       if (localChromaticity == Chromaticity.COLOR)
/* 1979 */         paramDevModeValues.color = 2;
/*      */       else {
/* 1981 */         paramDevModeValues.color = 1;
/*      */       }
/*      */     }
/*      */ 
/* 1985 */     Sides localSides = (Sides)paramPrintRequestAttributeSet.get(Sides.class);
/* 1986 */     if (localSides != null) {
/* 1987 */       paramDevModeValues.dmFields |= 4096;
/* 1988 */       if (localSides == Sides.TWO_SIDED_LONG_EDGE)
/* 1989 */         paramDevModeValues.duplex = 2;
/* 1990 */       else if (localSides == Sides.TWO_SIDED_SHORT_EDGE)
/* 1991 */         paramDevModeValues.duplex = 3;
/*      */       else {
/* 1993 */         paramDevModeValues.duplex = 1;
/*      */       }
/*      */     }
/*      */ 
/* 1997 */     OrientationRequested localOrientationRequested = (OrientationRequested)paramPrintRequestAttributeSet.get(OrientationRequested.class);
/*      */ 
/* 1999 */     if (localOrientationRequested != null) {
/* 2000 */       paramDevModeValues.dmFields |= 1;
/* 2001 */       paramDevModeValues.orient = (localOrientationRequested == OrientationRequested.LANDSCAPE ? 2 : 1);
/*      */     }
/*      */ 
/* 2005 */     Media localMedia = (Media)paramPrintRequestAttributeSet.get(Media.class);
/* 2006 */     if ((localMedia instanceof MediaSizeName)) {
/* 2007 */       paramDevModeValues.dmFields |= 2;
/* 2008 */       localObject1 = (MediaSizeName)localMedia;
/* 2009 */       paramDevModeValues.paper = ((short)((Win32PrintService)this.myService).findPaperID((MediaSizeName)localObject1));
/*      */     }
/*      */ 
/* 2013 */     Object localObject1 = null;
/* 2014 */     if ((localMedia instanceof MediaTray)) {
/* 2015 */       localObject1 = (MediaTray)localMedia;
/*      */     }
/* 2017 */     if (localObject1 == null) {
/* 2018 */       localObject2 = (SunAlternateMedia)paramPrintRequestAttributeSet.get(SunAlternateMedia.class);
/*      */ 
/* 2020 */       if ((localObject2 != null) && ((((SunAlternateMedia)localObject2).getMedia() instanceof MediaTray))) {
/* 2021 */         localObject1 = (MediaTray)((SunAlternateMedia)localObject2).getMedia();
/*      */       }
/*      */     }
/*      */ 
/* 2025 */     if (localObject1 != null) {
/* 2026 */       paramDevModeValues.dmFields |= 512;
/* 2027 */       paramDevModeValues.bin = ((short)((Win32PrintService)this.myService).findTrayID((MediaTray)localObject1));
/*      */     }
/*      */ 
/* 2030 */     Object localObject2 = (PrintQuality)paramPrintRequestAttributeSet.get(PrintQuality.class);
/* 2031 */     if (localObject2 != null) {
/* 2032 */       paramDevModeValues.dmFields |= 1024;
/* 2033 */       if (localObject2 == PrintQuality.DRAFT)
/* 2034 */         paramDevModeValues.xres_quality = -1;
/* 2035 */       else if (localObject2 == PrintQuality.HIGH)
/* 2036 */         paramDevModeValues.xres_quality = -4;
/*      */       else {
/* 2038 */         paramDevModeValues.xres_quality = -3;
/*      */       }
/*      */     }
/*      */ 
/* 2042 */     PrinterResolution localPrinterResolution = (PrinterResolution)paramPrintRequestAttributeSet.get(PrinterResolution.class);
/*      */ 
/* 2044 */     if (localPrinterResolution != null) {
/* 2045 */       paramDevModeValues.dmFields |= 9216;
/* 2046 */       paramDevModeValues.xres_quality = ((short)localPrinterResolution.getCrossFeedResolution(100));
/*      */ 
/* 2048 */       paramDevModeValues.yres = ((short)localPrinterResolution.getFeedResolution(100));
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void setJobAttributes(PrintRequestAttributeSet paramPrintRequestAttributeSet, int paramInt1, int paramInt2, short paramShort1, short paramShort2, short paramShort3, short paramShort4, short paramShort5, short paramShort6, short paramShort7)
/*      */   {
/* 2070 */     if (paramPrintRequestAttributeSet == null) {
/* 2071 */       return;
/*      */     }
/*      */ 
/* 2074 */     if ((paramInt1 & 0x100) != 0) {
/* 2075 */       paramPrintRequestAttributeSet.add(new Copies(paramShort1));
/*      */     }
/*      */ 
/* 2078 */     if ((paramInt1 & 0x8000) != 0) {
/* 2079 */       if ((paramInt2 & 0x8000) != 0)
/* 2080 */         paramPrintRequestAttributeSet.add(SheetCollate.COLLATED);
/*      */       else {
/* 2082 */         paramPrintRequestAttributeSet.add(SheetCollate.UNCOLLATED);
/*      */       }
/*      */     }
/*      */ 
/* 2086 */     if ((paramInt1 & 0x1) != 0) {
/* 2087 */       if ((paramInt2 & 0x4000) != 0)
/* 2088 */         paramPrintRequestAttributeSet.add(OrientationRequested.LANDSCAPE);
/*      */       else {
/* 2090 */         paramPrintRequestAttributeSet.add(OrientationRequested.PORTRAIT);
/*      */       }
/*      */     }
/*      */ 
/* 2094 */     if ((paramInt1 & 0x800) != 0)
/* 2095 */       if ((paramInt2 & 0x200) != 0)
/* 2096 */         paramPrintRequestAttributeSet.add(Chromaticity.COLOR);
/*      */       else
/* 2098 */         paramPrintRequestAttributeSet.add(Chromaticity.MONOCHROME);
/*      */     Object localObject;
/* 2102 */     if ((paramInt1 & 0x400) != 0)
/*      */     {
/* 2110 */       if (paramShort6 < 0)
/*      */       {
/* 2112 */         if ((paramInt2 & 0x80) != 0)
/* 2113 */           localObject = PrintQuality.DRAFT;
/* 2114 */         else if ((paramInt1 & 0x40) != 0)
/* 2115 */           localObject = PrintQuality.HIGH;
/*      */         else {
/* 2117 */           localObject = PrintQuality.NORMAL;
/*      */         }
/* 2119 */         paramPrintRequestAttributeSet.add((Attribute)localObject);
/* 2120 */       } else if ((paramShort6 > 0) && (paramShort7 > 0)) {
/* 2121 */         paramPrintRequestAttributeSet.add(new PrinterResolution(paramShort6, paramShort7, 100));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2126 */     if ((paramInt1 & 0x1000) != 0)
/*      */     {
/* 2128 */       if ((paramInt2 & 0x10) != 0)
/* 2129 */         localObject = Sides.TWO_SIDED_LONG_EDGE;
/* 2130 */       else if ((paramInt2 & 0x20) != 0)
/* 2131 */         localObject = Sides.TWO_SIDED_SHORT_EDGE;
/*      */       else {
/* 2133 */         localObject = Sides.ONE_SIDED;
/*      */       }
/* 2135 */       paramPrintRequestAttributeSet.add((Attribute)localObject);
/*      */     }
/*      */ 
/* 2138 */     if ((paramInt1 & 0x2) != 0) {
/* 2139 */       addPaperSize(paramPrintRequestAttributeSet, paramShort2, paramShort3, paramShort4);
/*      */     }
/*      */ 
/* 2142 */     if ((paramInt1 & 0x200) != 0) {
/* 2143 */       localObject = ((Win32PrintService)this.myService).findMediaTray(paramShort5);
/*      */ 
/* 2145 */       paramPrintRequestAttributeSet.add(new SunAlternateMedia((Media)localObject));
/*      */     }
/*      */   }
/*      */ 
/*      */   private native boolean showDocProperties(long paramLong, PrintRequestAttributeSet paramPrintRequestAttributeSet, int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4, short paramShort5, short paramShort6, short paramShort7, short paramShort8, short paramShort9);
/*      */ 
/*      */   public PrintRequestAttributeSet showDocumentProperties(Window paramWindow, PrintService paramPrintService, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*      */   {
/*      */     try
/*      */     {
/* 2169 */       setNativePrintServiceIfNeeded(paramPrintService.getName());
/*      */     } catch (PrinterException localPrinterException) {
/*      */     }
/* 2172 */     long l = ((WWindowPeer)paramWindow.getPeer()).getHWnd();
/* 2173 */     DevModeValues localDevModeValues = new DevModeValues(null);
/* 2174 */     getDevModeValues(paramPrintRequestAttributeSet, localDevModeValues);
/* 2175 */     boolean bool = showDocProperties(l, paramPrintRequestAttributeSet, localDevModeValues.dmFields, localDevModeValues.copies, localDevModeValues.collate, localDevModeValues.color, localDevModeValues.duplex, localDevModeValues.orient, localDevModeValues.paper, localDevModeValues.bin, localDevModeValues.xres_quality, localDevModeValues.yres);
/*      */ 
/* 2188 */     if (bool) {
/* 2189 */       return paramPrintRequestAttributeSet;
/*      */     }
/* 2191 */     return null;
/*      */   }
/*      */ 
/*      */   private final void setResolutionDPI(int paramInt1, int paramInt2)
/*      */   {
/* 2197 */     if (this.attributes != null) {
/* 2198 */       PrinterResolution localPrinterResolution = new PrinterResolution(paramInt1, paramInt2, 100);
/*      */ 
/* 2200 */       this.attributes.add(localPrinterResolution);
/*      */     }
/* 2202 */     this.mAttXRes = paramInt1;
/* 2203 */     this.mAttYRes = paramInt2;
/*      */   }
/*      */ 
/*      */   private void setResolutionAttrib(Attribute paramAttribute) {
/* 2207 */     PrinterResolution localPrinterResolution = (PrinterResolution)paramAttribute;
/* 2208 */     this.mAttXRes = localPrinterResolution.getCrossFeedResolution(100);
/* 2209 */     this.mAttYRes = localPrinterResolution.getFeedResolution(100);
/*      */   }
/*      */ 
/*      */   private void setPrinterNameAttrib(String paramString) {
/* 2213 */     PrintService localPrintService = getPrintService();
/*      */ 
/* 2215 */     if (paramString == null) {
/* 2216 */       return;
/*      */     }
/*      */ 
/* 2219 */     if ((localPrintService != null) && (paramString.equals(localPrintService.getName()))) {
/* 2220 */       return;
/*      */     }
/* 2222 */     PrintService[] arrayOfPrintService = PrinterJob.lookupPrintServices();
/* 2223 */     for (int i = 0; i < arrayOfPrintService.length; i++)
/* 2224 */       if (paramString.equals(arrayOfPrintService[i].getName()))
/*      */       {
/*      */         try {
/* 2227 */           setPrintService(arrayOfPrintService[i]);
/*      */         } catch (PrinterException localPrinterException) {
/*      */         }
/* 2230 */         return;
/*      */       }
/*      */   }
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   static
/*      */   {
/*  384 */     Toolkit.getDefaultToolkit();
/*      */ 
/*  386 */     initIDs();
/*      */ 
/*  388 */     Win32FontManager.registerJREFontsForPrinting();
/*      */   }
/*      */ 
/*      */   private static final class DevModeValues
/*      */   {
/*      */     int dmFields;
/*      */     short copies;
/*      */     short collate;
/*      */     short color;
/*      */     short duplex;
/*      */     short orient;
/*      */     short paper;
/*      */     short bin;
/*      */     short xres_quality;
/*      */     short yres;
/*      */   }
/*      */ 
/*      */   static class HandleRecord
/*      */     implements DisposerRecord
/*      */   {
/*      */     private long mPrintDC;
/*      */     private long mPrintHDevMode;
/*      */     private long mPrintHDevNames;
/*      */ 
/*      */     public void dispose()
/*      */     {
/*  299 */       WPrinterJob.deleteDC(this.mPrintDC, this.mPrintHDevMode, this.mPrintHDevNames);
/*      */     }
/*      */   }
/*      */ 
/*      */   class PrintToFileErrorDialog extends Dialog
/*      */     implements ActionListener
/*      */   {
/*      */     public PrintToFileErrorDialog(Frame paramString1, String paramString2, String paramString3, String arg5)
/*      */     {
/* 2241 */       super(paramString2, true);
/*      */       String str;
/* 2242 */       init(paramString1, paramString2, paramString3, str);
/*      */     }
/*      */ 
/*      */     public PrintToFileErrorDialog(Dialog paramString1, String paramString2, String paramString3, String arg5)
/*      */     {
/* 2247 */       super(paramString2, true);
/*      */       String str;
/* 2248 */       init(paramString1, paramString2, paramString3, str);
/*      */     }
/*      */ 
/*      */     private void init(Component paramComponent, String paramString1, String paramString2, String paramString3)
/*      */     {
/* 2253 */       Panel localPanel = new Panel();
/* 2254 */       add("Center", new Label(paramString2));
/* 2255 */       Button localButton = new Button(paramString3);
/* 2256 */       localButton.addActionListener(this);
/* 2257 */       localPanel.add(localButton);
/* 2258 */       add("South", localPanel);
/* 2259 */       pack();
/*      */ 
/* 2261 */       Dimension localDimension = getSize();
/* 2262 */       if (paramComponent != null) {
/* 2263 */         Rectangle localRectangle = paramComponent.getBounds();
/* 2264 */         setLocation(localRectangle.x + (localRectangle.width - localDimension.width) / 2, localRectangle.y + (localRectangle.height - localDimension.height) / 2);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2270 */       setVisible(false);
/* 2271 */       dispose();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WPrinterJob
 * JD-Core Version:    0.6.2
 */