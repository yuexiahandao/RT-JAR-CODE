/*      */ package sun.print;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Shape;
/*      */ import java.awt.font.FontRenderContext;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.geom.PathIterator;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.print.PageFormat;
/*      */ import java.awt.print.Pageable;
/*      */ import java.awt.print.Paper;
/*      */ import java.awt.print.Printable;
/*      */ import java.awt.print.PrinterException;
/*      */ import java.awt.print.PrinterIOException;
/*      */ import java.awt.print.PrinterJob;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Method;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.nio.charset.CoderMalfunctionError;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.Locale;
/*      */ import java.util.Properties;
/*      */ import javax.print.PrintService;
/*      */ import javax.print.StreamPrintService;
/*      */ import javax.print.attribute.HashPrintRequestAttributeSet;
/*      */ import javax.print.attribute.PrintRequestAttributeSet;
/*      */ import javax.print.attribute.standard.Copies;
/*      */ import javax.print.attribute.standard.Destination;
/*      */ import javax.print.attribute.standard.DialogTypeSelection;
/*      */ import javax.print.attribute.standard.JobName;
/*      */ import javax.print.attribute.standard.Sides;
/*      */ import sun.awt.CharsetString;
/*      */ import sun.awt.FontConfiguration;
/*      */ import sun.awt.FontDescriptor;
/*      */ import sun.awt.PlatformFont;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.font.Font2D;
/*      */ import sun.font.FontUtilities;
/*      */ 
/*      */ public class PSPrinterJob extends RasterPrinterJob
/*      */ {
/*      */   protected static final int FILL_EVEN_ODD = 1;
/*      */   protected static final int FILL_WINDING = 2;
/*      */   private static final int MAX_PSSTR = 65535;
/*      */   private static final int RED_MASK = 16711680;
/*      */   private static final int GREEN_MASK = 65280;
/*      */   private static final int BLUE_MASK = 255;
/*      */   private static final int RED_SHIFT = 16;
/*      */   private static final int GREEN_SHIFT = 8;
/*      */   private static final int BLUE_SHIFT = 0;
/*      */   private static final int LOWNIBBLE_MASK = 15;
/*      */   private static final int HINIBBLE_MASK = 240;
/*      */   private static final int HINIBBLE_SHIFT = 4;
/*  143 */   private static final byte[] hexDigits = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
/*      */   private static final int PS_XRES = 300;
/*      */   private static final int PS_YRES = 300;
/*      */   private static final String ADOBE_PS_STR = "%!PS-Adobe-3.0";
/*      */   private static final String EOF_COMMENT = "%%EOF";
/*      */   private static final String PAGE_COMMENT = "%%Page: ";
/*      */   private static final String READIMAGEPROC = "/imStr 0 def /imageSrc {currentfile /ASCII85Decode filter /RunLengthDecode filter  imStr readstring pop } def";
/*      */   private static final String COPIES = "/#copies exch def";
/*      */   private static final String PAGE_SAVE = "/pgSave save def";
/*      */   private static final String PAGE_RESTORE = "pgSave restore";
/*      */   private static final String SHOWPAGE = "showpage";
/*      */   private static final String IMAGE_SAVE = "/imSave save def";
/*      */   private static final String IMAGE_STR = " string /imStr exch def";
/*      */   private static final String IMAGE_RESTORE = "imSave restore";
/*      */   private static final String COORD_PREP = " 0 exch translate 1 -1 scale[72 300 div 0 0 72 300 div 0 0]concat";
/*      */   private static final String SetFontName = "F";
/*      */   private static final String DrawStringName = "S";
/*      */   private static final String EVEN_ODD_FILL_STR = "EF";
/*      */   private static final String WINDING_FILL_STR = "WF";
/*      */   private static final String EVEN_ODD_CLIP_STR = "EC";
/*      */   private static final String WINDING_CLIP_STR = "WC";
/*      */   private static final String MOVETO_STR = " M";
/*      */   private static final String LINETO_STR = " L";
/*      */   private static final String CURVETO_STR = " C";
/*      */   private static final String GRESTORE_STR = "R";
/*      */   private static final String GSAVE_STR = "G";
/*      */   private static final String NEWPATH_STR = "N";
/*      */   private static final String CLOSEPATH_STR = "P";
/*      */   private static final String SETRGBCOLOR_STR = " SC";
/*      */   private static final String SETGRAY_STR = " SG";
/*      */   private int mDestType;
/*  262 */   private String mDestination = "lp";
/*      */ 
/*  264 */   private boolean mNoJobSheet = false;
/*      */   private String mOptions;
/*      */   private Font mLastFont;
/*      */   private Color mLastColor;
/*      */   private Shape mLastClip;
/*      */   private AffineTransform mLastTransform;
/*  277 */   private EPSPrinter epsPrinter = null;
/*      */   FontMetrics mCurMetrics;
/*      */   PrintStream mPSStream;
/*      */   File spoolFile;
/*  299 */   private String mFillOpStr = "WF";
/*      */ 
/*  306 */   private String mClipOpStr = "WC";
/*      */ 
/*  311 */   ArrayList mGStateStack = new ArrayList();
/*      */   private float mPenX;
/*      */   private float mPenY;
/*      */   private float mStartPathX;
/*      */   private float mStartPathY;
/*  338 */   private static Properties mFontProps = null;
/*      */ 
/*      */   private static Properties initProps()
/*      */   {
/*  361 */     String str1 = System.getProperty("java.home");
/*      */ 
/*  363 */     if (str1 != null) {
/*  364 */       String str2 = SunToolkit.getStartupLocale().getLanguage();
/*      */       try
/*      */       {
/*  367 */         File localFile = new File(str1 + File.separator + "lib" + File.separator + "psfontj2d.properties." + str2);
/*      */ 
/*  371 */         if (!localFile.canRead())
/*      */         {
/*  373 */           localFile = new File(str1 + File.separator + "lib" + File.separator + "psfont.properties." + str2);
/*      */ 
/*  376 */           if (!localFile.canRead())
/*      */           {
/*  378 */             localFile = new File(str1 + File.separator + "lib" + File.separator + "psfontj2d.properties");
/*      */ 
/*  381 */             if (!localFile.canRead())
/*      */             {
/*  383 */               localFile = new File(str1 + File.separator + "lib" + File.separator + "psfont.properties");
/*      */ 
/*  386 */               if (!localFile.canRead()) {
/*  387 */                 return (Properties)null;
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  394 */         BufferedInputStream localBufferedInputStream = new BufferedInputStream(new FileInputStream(localFile.getPath()));
/*      */ 
/*  396 */         Properties localProperties = new Properties();
/*  397 */         localProperties.load(localBufferedInputStream);
/*  398 */         localBufferedInputStream.close();
/*  399 */         return localProperties;
/*      */       } catch (Exception localException) {
/*  401 */         return (Properties)null;
/*      */       }
/*      */     }
/*  404 */     return (Properties)null;
/*      */   }
/*      */ 
/*      */   public boolean printDialog()
/*      */     throws HeadlessException
/*      */   {
/*  426 */     if (GraphicsEnvironment.isHeadless()) {
/*  427 */       throw new HeadlessException();
/*      */     }
/*      */ 
/*  430 */     if (this.attributes == null) {
/*  431 */       this.attributes = new HashPrintRequestAttributeSet();
/*      */     }
/*  433 */     this.attributes.add(new Copies(getCopies()));
/*  434 */     this.attributes.add(new JobName(getJobName(), null));
/*      */ 
/*  436 */     boolean bool = false;
/*  437 */     DialogTypeSelection localDialogTypeSelection = (DialogTypeSelection)this.attributes.get(DialogTypeSelection.class);
/*      */ 
/*  439 */     if (localDialogTypeSelection == DialogTypeSelection.NATIVE)
/*      */     {
/*  442 */       this.attributes.remove(DialogTypeSelection.class);
/*  443 */       bool = printDialog(this.attributes);
/*      */ 
/*  445 */       this.attributes.add(DialogTypeSelection.NATIVE);
/*      */     } else {
/*  447 */       bool = printDialog(this.attributes);
/*      */     }
/*      */ 
/*  450 */     if (bool) {
/*  451 */       JobName localJobName = (JobName)this.attributes.get(JobName.class);
/*  452 */       if (localJobName != null) {
/*  453 */         setJobName(localJobName.getValue());
/*      */       }
/*  455 */       Copies localCopies = (Copies)this.attributes.get(Copies.class);
/*  456 */       if (localCopies != null) {
/*  457 */         setCopies(localCopies.getValue());
/*      */       }
/*      */ 
/*  460 */       Destination localDestination = (Destination)this.attributes.get(Destination.class);
/*      */ 
/*  462 */       if (localDestination != null) {
/*      */         try {
/*  464 */           this.mDestType = 1;
/*  465 */           this.mDestination = new File(localDestination.getURI()).getPath();
/*      */         } catch (Exception localException) {
/*  467 */           this.mDestination = "out.ps";
/*      */         }
/*      */       } else {
/*  470 */         this.mDestType = 0;
/*  471 */         PrintService localPrintService = getPrintService();
/*  472 */         if (localPrintService != null) {
/*  473 */           this.mDestination = localPrintService.getName();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  478 */     return bool;
/*      */   }
/*      */ 
/*      */   protected void startDoc()
/*      */     throws PrinterException
/*      */   {
/*  496 */     if (this.epsPrinter == null)
/*      */     {
/*      */       Object localObject;
/*  497 */       if ((getPrintService() instanceof PSStreamPrintService)) {
/*  498 */         StreamPrintService localStreamPrintService = (StreamPrintService)getPrintService();
/*  499 */         this.mDestType = 2;
/*  500 */         if (localStreamPrintService.isDisposed()) {
/*  501 */           throw new PrinterException("service is disposed");
/*      */         }
/*  503 */         localObject = localStreamPrintService.getOutputStream();
/*  504 */         if (localObject == null)
/*  505 */           throw new PrinterException("Null output stream");
/*      */       }
/*      */       else
/*      */       {
/*  509 */         this.mNoJobSheet = this.noJobSheet;
/*  510 */         if (this.destinationAttr != null) {
/*  511 */           this.mDestType = 1;
/*  512 */           this.mDestination = this.destinationAttr;
/*      */         }
/*  514 */         if (this.mDestType == 1) {
/*      */           try {
/*  516 */             this.spoolFile = new File(this.mDestination);
/*  517 */             localObject = new FileOutputStream(this.spoolFile);
/*      */           } catch (IOException localIOException) {
/*  519 */             throw new PrinterIOException(localIOException);
/*      */           }
/*      */         } else {
/*  522 */           PrinterOpener localPrinterOpener = new PrinterOpener(null);
/*  523 */           AccessController.doPrivileged(localPrinterOpener);
/*  524 */           if (localPrinterOpener.pex != null) {
/*  525 */             throw localPrinterOpener.pex;
/*      */           }
/*  527 */           localObject = localPrinterOpener.result;
/*      */         }
/*      */       }
/*      */ 
/*  531 */       this.mPSStream = new PrintStream(new BufferedOutputStream((OutputStream)localObject));
/*  532 */       this.mPSStream.println("%!PS-Adobe-3.0");
/*      */     }
/*      */ 
/*  535 */     this.mPSStream.println("%%BeginProlog");
/*  536 */     this.mPSStream.println("/imStr 0 def /imageSrc {currentfile /ASCII85Decode filter /RunLengthDecode filter  imStr readstring pop } def");
/*  537 */     this.mPSStream.println("/BD {bind def} bind def");
/*  538 */     this.mPSStream.println("/D {def} BD");
/*  539 */     this.mPSStream.println("/C {curveto} BD");
/*  540 */     this.mPSStream.println("/L {lineto} BD");
/*  541 */     this.mPSStream.println("/M {moveto} BD");
/*  542 */     this.mPSStream.println("/R {grestore} BD");
/*  543 */     this.mPSStream.println("/G {gsave} BD");
/*  544 */     this.mPSStream.println("/N {newpath} BD");
/*  545 */     this.mPSStream.println("/P {closepath} BD");
/*  546 */     this.mPSStream.println("/EC {eoclip} BD");
/*  547 */     this.mPSStream.println("/WC {clip} BD");
/*  548 */     this.mPSStream.println("/EF {eofill} BD");
/*  549 */     this.mPSStream.println("/WF {fill} BD");
/*  550 */     this.mPSStream.println("/SG {setgray} BD");
/*  551 */     this.mPSStream.println("/SC {setrgbcolor} BD");
/*  552 */     this.mPSStream.println("/ISOF {");
/*  553 */     this.mPSStream.println("     dup findfont dup length 1 add dict begin {");
/*  554 */     this.mPSStream.println("             1 index /FID eq {pop pop} {D} ifelse");
/*  555 */     this.mPSStream.println("     } forall /Encoding ISOLatin1Encoding D");
/*  556 */     this.mPSStream.println("     currentdict end definefont");
/*  557 */     this.mPSStream.println("} BD");
/*  558 */     this.mPSStream.println("/NZ {dup 1 lt {pop 1} if} BD");
/*      */ 
/*  567 */     this.mPSStream.println("/S {");
/*  568 */     this.mPSStream.println("     moveto 1 index stringwidth pop NZ sub");
/*  569 */     this.mPSStream.println("     1 index length 1 sub NZ div 0");
/*  570 */     this.mPSStream.println("     3 2 roll ashow newpath} BD");
/*  571 */     this.mPSStream.println("/FL [");
/*  572 */     if (mFontProps == null) {
/*  573 */       this.mPSStream.println(" /Helvetica ISOF");
/*  574 */       this.mPSStream.println(" /Helvetica-Bold ISOF");
/*  575 */       this.mPSStream.println(" /Helvetica-Oblique ISOF");
/*  576 */       this.mPSStream.println(" /Helvetica-BoldOblique ISOF");
/*  577 */       this.mPSStream.println(" /Times-Roman ISOF");
/*  578 */       this.mPSStream.println(" /Times-Bold ISOF");
/*  579 */       this.mPSStream.println(" /Times-Italic ISOF");
/*  580 */       this.mPSStream.println(" /Times-BoldItalic ISOF");
/*  581 */       this.mPSStream.println(" /Courier ISOF");
/*  582 */       this.mPSStream.println(" /Courier-Bold ISOF");
/*  583 */       this.mPSStream.println(" /Courier-Oblique ISOF");
/*  584 */       this.mPSStream.println(" /Courier-BoldOblique ISOF");
/*      */     } else {
/*  586 */       int i = Integer.parseInt(mFontProps.getProperty("font.num", "9"));
/*  587 */       for (int j = 0; j < i; j++) {
/*  588 */         this.mPSStream.println("    /" + mFontProps.getProperty(new StringBuilder().append("font.").append(String.valueOf(j)).toString(), "Courier ISOF"));
/*      */       }
/*      */     }
/*      */ 
/*  592 */     this.mPSStream.println("] D");
/*      */ 
/*  594 */     this.mPSStream.println("/F {");
/*  595 */     this.mPSStream.println("     FL exch get exch scalefont");
/*  596 */     this.mPSStream.println("     [1 0 0 -1 0 0] makefont setfont} BD");
/*      */ 
/*  598 */     this.mPSStream.println("%%EndProlog");
/*      */ 
/*  600 */     this.mPSStream.println("%%BeginSetup");
/*  601 */     if (this.epsPrinter == null)
/*      */     {
/*  603 */       PageFormat localPageFormat = getPageable().getPageFormat(0);
/*  604 */       double d1 = localPageFormat.getPaper().getHeight();
/*  605 */       double d2 = localPageFormat.getPaper().getWidth();
/*      */ 
/*  609 */       this.mPSStream.print("<< /PageSize [" + d2 + " " + d1 + "]");
/*      */ 
/*  612 */       final PrintService localPrintService = getPrintService();
/*  613 */       Boolean localBoolean = (Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run() {
/*      */           try {
/*  617 */             Class localClass = Class.forName("sun.print.IPPPrintService");
/*  618 */             if (localClass.isInstance(localPrintService)) {
/*  619 */               Method localMethod = localClass.getMethod("isPostscript", (Class[])null);
/*      */ 
/*  621 */               return (Boolean)localMethod.invoke(localPrintService, (Object[])null);
/*      */             }
/*      */           } catch (Throwable localThrowable) {
/*      */           }
/*  625 */           return Boolean.TRUE;
/*      */         }
/*      */       });
/*  629 */       if (localBoolean.booleanValue()) {
/*  630 */         this.mPSStream.print(" /DeferredMediaSelection true");
/*      */       }
/*      */ 
/*  633 */       this.mPSStream.print(" /ImagingBBox null /ManualFeed false");
/*  634 */       this.mPSStream.print(isCollated() ? " /Collate true" : "");
/*  635 */       this.mPSStream.print(" /NumCopies " + getCopiesInt());
/*      */ 
/*  637 */       if (this.sidesAttr != Sides.ONE_SIDED) {
/*  638 */         if (this.sidesAttr == Sides.TWO_SIDED_LONG_EDGE)
/*  639 */           this.mPSStream.print(" /Duplex true ");
/*  640 */         else if (this.sidesAttr == Sides.TWO_SIDED_SHORT_EDGE) {
/*  641 */           this.mPSStream.print(" /Duplex true /Tumble true ");
/*      */         }
/*      */       }
/*  644 */       this.mPSStream.println(" >> setpagedevice ");
/*      */     }
/*  646 */     this.mPSStream.println("%%EndSetup");
/*      */   }
/*      */ 
/*      */   protected void abortDoc()
/*      */   {
/*  740 */     if ((this.mPSStream != null) && (this.mDestType != 2)) {
/*  741 */       this.mPSStream.close();
/*      */     }
/*  743 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run()
/*      */       {
/*  747 */         if ((PSPrinterJob.this.spoolFile != null) && (PSPrinterJob.this.spoolFile.exists())) {
/*  748 */           PSPrinterJob.this.spoolFile.delete();
/*      */         }
/*  750 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   protected void endDoc()
/*      */     throws PrinterException
/*      */   {
/*  761 */     if (this.mPSStream != null) {
/*  762 */       this.mPSStream.println("%%EOF");
/*  763 */       this.mPSStream.flush();
/*  764 */       if (this.mDestType != 2) {
/*  765 */         this.mPSStream.close();
/*      */       }
/*      */     }
/*  768 */     if (this.mDestType == 0) {
/*  769 */       if (getPrintService() != null) {
/*  770 */         this.mDestination = getPrintService().getName();
/*      */       }
/*  772 */       PrinterSpooler localPrinterSpooler = new PrinterSpooler(null);
/*  773 */       AccessController.doPrivileged(localPrinterSpooler);
/*  774 */       if (localPrinterSpooler.pex != null)
/*  775 */         throw localPrinterSpooler.pex;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void startPage(PageFormat paramPageFormat, Printable paramPrintable, int paramInt, boolean paramBoolean)
/*      */     throws PrinterException
/*      */   {
/*  788 */     double d1 = paramPageFormat.getPaper().getHeight();
/*  789 */     double d2 = paramPageFormat.getPaper().getWidth();
/*  790 */     int i = paramInt + 1;
/*      */ 
/*  796 */     this.mGStateStack = new ArrayList();
/*  797 */     this.mGStateStack.add(new GState());
/*      */ 
/*  799 */     this.mPSStream.println("%%Page: " + i + " " + i);
/*      */ 
/*  803 */     if ((paramInt > 0) && (paramBoolean))
/*      */     {
/*  805 */       this.mPSStream.print("<< /PageSize [" + d2 + " " + d1 + "]");
/*      */ 
/*  808 */       final PrintService localPrintService = getPrintService();
/*  809 */       Boolean localBoolean = (Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run()
/*      */         {
/*      */           try
/*      */           {
/*  815 */             Class localClass = Class.forName("sun.print.IPPPrintService");
/*      */ 
/*  817 */             if (localClass.isInstance(localPrintService)) {
/*  818 */               Method localMethod = localClass.getMethod("isPostscript", (Class[])null);
/*      */ 
/*  821 */               return (Boolean)localMethod.invoke(localPrintService, (Object[])null);
/*      */             }
/*      */           }
/*      */           catch (Throwable localThrowable)
/*      */           {
/*      */           }
/*  827 */           return Boolean.TRUE;
/*      */         }
/*      */       });
/*  832 */       if (localBoolean.booleanValue()) {
/*  833 */         this.mPSStream.print(" /DeferredMediaSelection true");
/*      */       }
/*  835 */       this.mPSStream.println(" >> setpagedevice");
/*      */     }
/*  837 */     this.mPSStream.println("/pgSave save def");
/*  838 */     this.mPSStream.println(d1 + " 0 exch translate 1 -1 scale[72 300 div 0 0 72 300 div 0 0]concat");
/*      */   }
/*      */ 
/*      */   protected void endPage(PageFormat paramPageFormat, Printable paramPrintable, int paramInt)
/*      */     throws PrinterException
/*      */   {
/*  849 */     this.mPSStream.println("pgSave restore");
/*  850 */     this.mPSStream.println("showpage");
/*      */   }
/*      */ 
/*      */   protected void drawImageBGR(byte[] paramArrayOfByte, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, int paramInt1, int paramInt2)
/*      */   {
/*  874 */     setTransform(new AffineTransform());
/*  875 */     prepDrawing();
/*      */ 
/*  877 */     int i = (int)paramFloat7;
/*  878 */     int j = (int)paramFloat8;
/*      */ 
/*  880 */     this.mPSStream.println("/imSave save def");
/*      */ 
/*  884 */     int k = 3 * i;
/*  885 */     while (k > 65535) {
/*  886 */       k /= 2;
/*      */     }
/*      */ 
/*  889 */     this.mPSStream.println(k + " string /imStr exch def");
/*      */ 
/*  893 */     this.mPSStream.println("[" + paramFloat3 + " 0 " + "0 " + paramFloat4 + " " + paramFloat1 + " " + paramFloat2 + "]concat");
/*      */ 
/*  900 */     this.mPSStream.println(i + " " + j + " " + 8 + "[" + i + " 0 " + "0 " + j + " 0 " + 0 + "]" + "/imageSrc load false 3 colorimage");
/*      */ 
/*  908 */     int m = 0;
/*  909 */     byte[] arrayOfByte1 = new byte[i * 3];
/*      */     try
/*      */     {
/*  915 */       m = (int)paramFloat6 * paramInt1;
/*      */ 
/*  917 */       for (int n = 0; n < j; n++)
/*      */       {
/*  922 */         m += (int)paramFloat5;
/*      */ 
/*  924 */         m = swapBGRtoRGB(paramArrayOfByte, m, arrayOfByte1);
/*  925 */         byte[] arrayOfByte2 = rlEncode(arrayOfByte1);
/*  926 */         byte[] arrayOfByte3 = ascii85Encode(arrayOfByte2);
/*  927 */         this.mPSStream.write(arrayOfByte3);
/*  928 */         this.mPSStream.println("");
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*      */ 
/*  940 */     this.mPSStream.println("imSave restore");
/*      */   }
/*      */ 
/*      */   protected void printBand(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     throws PrinterException
/*      */   {
/*  956 */     this.mPSStream.println("/imSave save def");
/*      */ 
/*  960 */     int i = 3 * paramInt3;
/*  961 */     while (i > 65535) {
/*  962 */       i /= 2;
/*      */     }
/*      */ 
/*  965 */     this.mPSStream.println(i + " string /imStr exch def");
/*      */ 
/*  969 */     this.mPSStream.println("[" + paramInt3 + " 0 " + "0 " + paramInt4 + " " + paramInt1 + " " + paramInt2 + "]concat");
/*      */ 
/*  976 */     this.mPSStream.println(paramInt3 + " " + paramInt4 + " " + 8 + "[" + paramInt3 + " 0 " + "0 " + -paramInt4 + " 0 " + paramInt4 + "]" + "/imageSrc load false 3 colorimage");
/*      */ 
/*  984 */     int j = 0;
/*  985 */     byte[] arrayOfByte1 = new byte[paramInt3 * 3];
/*      */     try
/*      */     {
/*  988 */       for (int k = 0; k < paramInt4; k++) {
/*  989 */         j = swapBGRtoRGB(paramArrayOfByte, j, arrayOfByte1);
/*  990 */         byte[] arrayOfByte2 = rlEncode(arrayOfByte1);
/*  991 */         byte[] arrayOfByte3 = ascii85Encode(arrayOfByte2);
/*  992 */         this.mPSStream.write(arrayOfByte3);
/*  993 */         this.mPSStream.println("");
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException) {
/*  997 */       throw new PrinterIOException(localIOException);
/*      */     }
/*      */ 
/* 1000 */     this.mPSStream.println("imSave restore");
/*      */   }
/*      */ 
/*      */   protected Graphics2D createPathGraphics(PeekGraphics paramPeekGraphics, PrinterJob paramPrinterJob, Printable paramPrintable, PageFormat paramPageFormat, int paramInt)
/*      */   {
/* 1023 */     PeekMetrics localPeekMetrics = paramPeekGraphics.getMetrics();
/*      */     PSPathGraphics localPSPathGraphics;
/* 1029 */     if ((!forcePDL) && ((forceRaster == true) || (localPeekMetrics.hasNonSolidColors()) || (localPeekMetrics.hasCompositing())))
/*      */     {
/* 1033 */       localPSPathGraphics = null;
/*      */     }
/*      */     else {
/* 1036 */       BufferedImage localBufferedImage = new BufferedImage(8, 8, 1);
/*      */ 
/* 1038 */       Graphics2D localGraphics2D = localBufferedImage.createGraphics();
/* 1039 */       boolean bool = !paramPeekGraphics.getAWTDrawingOnly();
/*      */ 
/* 1041 */       localPSPathGraphics = new PSPathGraphics(localGraphics2D, paramPrinterJob, paramPrintable, paramPageFormat, paramInt, bool);
/*      */     }
/*      */ 
/* 1046 */     return localPSPathGraphics;
/*      */   }
/*      */ 
/*      */   protected void selectClipPath()
/*      */   {
/* 1055 */     this.mPSStream.println(this.mClipOpStr);
/*      */   }
/*      */ 
/*      */   protected void setClip(Shape paramShape)
/*      */   {
/* 1060 */     this.mLastClip = paramShape;
/*      */   }
/*      */ 
/*      */   protected void setTransform(AffineTransform paramAffineTransform) {
/* 1064 */     this.mLastTransform = paramAffineTransform;
/*      */   }
/*      */ 
/*      */   protected boolean setFont(Font paramFont)
/*      */   {
/* 1072 */     this.mLastFont = paramFont;
/* 1073 */     return true;
/*      */   }
/*      */ 
/*      */   private int[] getPSFontIndexArray(Font paramFont, CharsetString[] paramArrayOfCharsetString)
/*      */   {
/* 1084 */     int[] arrayOfInt = null;
/*      */ 
/* 1086 */     if (mFontProps != null) {
/* 1087 */       arrayOfInt = new int[paramArrayOfCharsetString.length];
/*      */     }
/*      */ 
/* 1090 */     for (int i = 0; (i < paramArrayOfCharsetString.length) && (arrayOfInt != null); i++)
/*      */     {
/* 1094 */       CharsetString localCharsetString = paramArrayOfCharsetString[i];
/*      */ 
/* 1096 */       CharsetEncoder localCharsetEncoder = localCharsetString.fontDescriptor.encoder;
/* 1097 */       String str1 = localCharsetString.fontDescriptor.getFontCharsetName();
/*      */ 
/* 1104 */       if ("Symbol".equals(str1))
/* 1105 */         str1 = "symbol";
/* 1106 */       else if (("WingDings".equals(str1)) || ("X11Dingbats".equals(str1)))
/*      */       {
/* 1108 */         str1 = "dingbats";
/*      */       }
/* 1110 */       else str1 = makeCharsetName(str1, localCharsetString.charsetChars);
/*      */ 
/* 1113 */       int j = paramFont.getStyle() | FontUtilities.getFont2D(paramFont).getStyle();
/*      */ 
/* 1116 */       String str2 = FontConfiguration.getStyleString(j);
/*      */ 
/* 1122 */       String str3 = paramFont.getFamily().toLowerCase(Locale.ENGLISH);
/* 1123 */       str3 = str3.replace(' ', '_');
/* 1124 */       String str4 = mFontProps.getProperty(str3, "");
/*      */ 
/* 1129 */       String str5 = mFontProps.getProperty(str4 + "." + str1 + "." + str2, null);
/*      */ 
/* 1133 */       if (str5 != null)
/*      */       {
/*      */         try
/*      */         {
/* 1138 */           arrayOfInt[i] = Integer.parseInt(mFontProps.getProperty(str5));
/*      */         }
/*      */         catch (NumberFormatException localNumberFormatException)
/*      */         {
/* 1147 */           arrayOfInt = null;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1154 */         arrayOfInt = null;
/*      */       }
/*      */     }
/*      */ 
/* 1158 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   private static String escapeParens(String paramString)
/*      */   {
/* 1163 */     if ((paramString.indexOf('(') == -1) && (paramString.indexOf(')') == -1)) {
/* 1164 */       return paramString;
/*      */     }
/* 1166 */     int i = 0;
/* 1167 */     int j = 0;
/* 1168 */     while ((j = paramString.indexOf('(', j)) != -1) {
/* 1169 */       i++;
/* 1170 */       j++;
/*      */     }
/* 1172 */     j = 0;
/* 1173 */     while ((j = paramString.indexOf(')', j)) != -1) {
/* 1174 */       i++;
/* 1175 */       j++;
/*      */     }
/* 1177 */     char[] arrayOfChar1 = paramString.toCharArray();
/* 1178 */     char[] arrayOfChar2 = new char[arrayOfChar1.length + i];
/* 1179 */     j = 0;
/* 1180 */     for (int k = 0; k < arrayOfChar1.length; k++) {
/* 1181 */       if ((arrayOfChar1[k] == '(') || (arrayOfChar1[k] == ')')) {
/* 1182 */         arrayOfChar2[(j++)] = '\\';
/*      */       }
/* 1184 */       arrayOfChar2[(j++)] = arrayOfChar1[k];
/*      */     }
/* 1186 */     return new String(arrayOfChar2);
/*      */   }
/*      */ 
/*      */   protected int platformFontCount(Font paramFont, String paramString)
/*      */   {
/* 1196 */     if (mFontProps == null) {
/* 1197 */       return 0;
/*      */     }
/* 1199 */     CharsetString[] arrayOfCharsetString = ((PlatformFont)paramFont.getPeer()).makeMultiCharsetString(paramString, false);
/*      */ 
/* 1201 */     if (arrayOfCharsetString == null)
/*      */     {
/* 1203 */       return 0;
/*      */     }
/* 1205 */     int[] arrayOfInt = getPSFontIndexArray(paramFont, arrayOfCharsetString);
/* 1206 */     return arrayOfInt == null ? 0 : arrayOfInt.length;
/*      */   }
/*      */ 
/*      */   protected boolean textOut(Graphics paramGraphics, String paramString, float paramFloat1, float paramFloat2, Font paramFont, FontRenderContext paramFontRenderContext, float paramFloat3)
/*      */   {
/* 1212 */     boolean bool = true;
/*      */ 
/* 1214 */     if (mFontProps == null) {
/* 1215 */       return false;
/*      */     }
/* 1217 */     prepDrawing();
/*      */ 
/* 1229 */     paramString = removeControlChars(paramString);
/* 1230 */     if (paramString.length() == 0) {
/* 1231 */       return true;
/*      */     }
/* 1233 */     CharsetString[] arrayOfCharsetString = ((PlatformFont)paramFont.getPeer()).makeMultiCharsetString(paramString, false);
/*      */ 
/* 1236 */     if (arrayOfCharsetString == null)
/*      */     {
/* 1238 */       return false;
/*      */     }
/*      */ 
/* 1246 */     int[] arrayOfInt = getPSFontIndexArray(paramFont, arrayOfCharsetString);
/* 1247 */     if (arrayOfInt != null)
/*      */     {
/* 1249 */       for (int i = 0; i < arrayOfCharsetString.length; i++) {
/* 1250 */         CharsetString localCharsetString = arrayOfCharsetString[i];
/* 1251 */         CharsetEncoder localCharsetEncoder = localCharsetString.fontDescriptor.encoder;
/*      */ 
/* 1253 */         StringBuffer localStringBuffer = new StringBuffer();
/* 1254 */         byte[] arrayOfByte = new byte[localCharsetString.length * 2];
/* 1255 */         int j = 0;
/*      */         try {
/* 1257 */           ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte);
/* 1258 */           localCharsetEncoder.encode(CharBuffer.wrap(localCharsetString.charsetChars, localCharsetString.offset, localCharsetString.length), localByteBuffer, true);
/*      */ 
/* 1262 */           localByteBuffer.flip();
/* 1263 */           j = localByteBuffer.limit();
/*      */         } catch (IllegalStateException localIllegalStateException) {
/* 1265 */           continue;
/*      */         } catch (CoderMalfunctionError localCoderMalfunctionError) {
/* 1267 */           continue;
/*      */         }
/*      */         float f;
/* 1275 */         if ((arrayOfCharsetString.length == 1) && (paramFloat3 != 0.0F)) {
/* 1276 */           f = paramFloat3;
/*      */         } else {
/* 1278 */           Rectangle2D localRectangle2D = paramFont.getStringBounds(localCharsetString.charsetChars, localCharsetString.offset, localCharsetString.offset + localCharsetString.length, paramFontRenderContext);
/*      */ 
/* 1283 */           f = (float)localRectangle2D.getWidth();
/*      */         }
/*      */ 
/* 1287 */         if (f == 0.0F) {
/* 1288 */           return bool;
/*      */         }
/* 1290 */         localStringBuffer.append('<');
/* 1291 */         for (int k = 0; k < j; k++) {
/* 1292 */           int m = arrayOfByte[k];
/*      */ 
/* 1294 */           String str = Integer.toHexString(m);
/* 1295 */           int n = str.length();
/* 1296 */           if (n > 2)
/* 1297 */             str = str.substring(n - 2, n);
/* 1298 */           else if (n == 1)
/* 1299 */             str = "0" + str;
/* 1300 */           else if (n == 0) {
/* 1301 */             str = "00";
/*      */           }
/* 1303 */           localStringBuffer.append(str);
/*      */         }
/* 1305 */         localStringBuffer.append('>');
/*      */ 
/* 1310 */         getGState().emitPSFont(arrayOfInt[i], paramFont.getSize2D());
/*      */ 
/* 1313 */         this.mPSStream.println(localStringBuffer.toString() + " " + f + " " + paramFloat1 + " " + paramFloat2 + " " + "S");
/*      */ 
/* 1316 */         paramFloat1 += f;
/*      */       }
/*      */     }
/* 1319 */     else bool = false;
/*      */ 
/* 1323 */     return bool;
/*      */   }
/*      */ 
/*      */   protected void setFillMode(int paramInt)
/*      */   {
/* 1333 */     switch (paramInt)
/*      */     {
/*      */     case 1:
/* 1336 */       this.mFillOpStr = "EF";
/* 1337 */       this.mClipOpStr = "EC";
/* 1338 */       break;
/*      */     case 2:
/* 1341 */       this.mFillOpStr = "WF";
/* 1342 */       this.mClipOpStr = "WC";
/* 1343 */       break;
/*      */     default:
/* 1346 */       throw new IllegalArgumentException();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void setColor(Color paramColor)
/*      */   {
/* 1356 */     this.mLastColor = paramColor;
/*      */   }
/*      */ 
/*      */   protected void fillPath()
/*      */   {
/* 1365 */     this.mPSStream.println(this.mFillOpStr);
/*      */   }
/*      */ 
/*      */   protected void beginPath()
/*      */   {
/* 1373 */     prepDrawing();
/* 1374 */     this.mPSStream.println("N");
/*      */ 
/* 1376 */     this.mPenX = 0.0F;
/* 1377 */     this.mPenY = 0.0F;
/*      */   }
/*      */ 
/*      */   protected void closeSubpath()
/*      */   {
/* 1387 */     this.mPSStream.println("P");
/*      */ 
/* 1389 */     this.mPenX = this.mStartPathX;
/* 1390 */     this.mPenY = this.mStartPathY;
/*      */   }
/*      */ 
/*      */   protected void moveTo(float paramFloat1, float paramFloat2)
/*      */   {
/* 1400 */     this.mPSStream.println(trunc(paramFloat1) + " " + trunc(paramFloat2) + " M");
/*      */ 
/* 1407 */     this.mStartPathX = paramFloat1;
/* 1408 */     this.mStartPathY = paramFloat2;
/*      */ 
/* 1410 */     this.mPenX = paramFloat1;
/* 1411 */     this.mPenY = paramFloat2;
/*      */   }
/*      */ 
/*      */   protected void lineTo(float paramFloat1, float paramFloat2)
/*      */   {
/* 1419 */     this.mPSStream.println(trunc(paramFloat1) + " " + trunc(paramFloat2) + " L");
/*      */ 
/* 1421 */     this.mPenX = paramFloat1;
/* 1422 */     this.mPenY = paramFloat2;
/*      */   }
/*      */ 
/*      */   protected void bezierTo(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
/*      */   {
/* 1439 */     this.mPSStream.println(trunc(paramFloat1) + " " + trunc(paramFloat2) + " " + trunc(paramFloat3) + " " + trunc(paramFloat4) + " " + trunc(paramFloat5) + " " + trunc(paramFloat6) + " C");
/*      */ 
/* 1445 */     this.mPenX = paramFloat5;
/* 1446 */     this.mPenY = paramFloat6;
/*      */   }
/*      */ 
/*      */   String trunc(float paramFloat) {
/* 1450 */     float f = Math.abs(paramFloat);
/* 1451 */     if ((f >= 1.0F) && (f <= 1000.0F)) {
/* 1452 */       paramFloat = Math.round(paramFloat * 1000.0F) / 1000.0F;
/*      */     }
/* 1454 */     return Float.toString(paramFloat);
/*      */   }
/*      */ 
/*      */   protected float getPenX()
/*      */   {
/* 1463 */     return this.mPenX;
/*      */   }
/*      */ 
/*      */   protected float getPenY()
/*      */   {
/* 1471 */     return this.mPenY;
/*      */   }
/*      */ 
/*      */   protected double getXRes()
/*      */   {
/* 1479 */     return 300.0D;
/*      */   }
/*      */ 
/*      */   protected double getYRes()
/*      */   {
/* 1486 */     return 300.0D;
/*      */   }
/*      */ 
/*      */   protected double getPhysicalPrintableX(Paper paramPaper)
/*      */   {
/* 1494 */     return 0.0D;
/*      */   }
/*      */ 
/*      */   protected double getPhysicalPrintableY(Paper paramPaper)
/*      */   {
/* 1503 */     return 0.0D;
/*      */   }
/*      */ 
/*      */   protected double getPhysicalPrintableWidth(Paper paramPaper) {
/* 1507 */     return paramPaper.getImageableWidth();
/*      */   }
/*      */ 
/*      */   protected double getPhysicalPrintableHeight(Paper paramPaper) {
/* 1511 */     return paramPaper.getImageableHeight();
/*      */   }
/*      */ 
/*      */   protected double getPhysicalPageWidth(Paper paramPaper) {
/* 1515 */     return paramPaper.getWidth();
/*      */   }
/*      */ 
/*      */   protected double getPhysicalPageHeight(Paper paramPaper) {
/* 1519 */     return paramPaper.getHeight();
/*      */   }
/*      */ 
/*      */   protected int getNoncollatedCopies()
/*      */   {
/* 1529 */     return 1;
/*      */   }
/*      */ 
/*      */   protected int getCollatedCopies() {
/* 1533 */     return 1;
/*      */   }
/*      */ 
/*      */   private String[] printExecCmd(String paramString1, String paramString2, boolean paramBoolean, String paramString3, int paramInt, String paramString4)
/*      */   {
/* 1539 */     int i = 1;
/* 1540 */     int j = 2;
/* 1541 */     int k = 4;
/* 1542 */     int m = 8;
/* 1543 */     int n = 16;
/* 1544 */     int i1 = 0;
/*      */ 
/* 1546 */     int i2 = 2;
/* 1547 */     int i3 = 0;
/*      */ 
/* 1549 */     if ((paramString1 != null) && (!paramString1.equals("")) && (!paramString1.equals("lp"))) {
/* 1550 */       i1 |= i;
/* 1551 */       i2++;
/*      */     }
/* 1553 */     if ((paramString2 != null) && (!paramString2.equals(""))) {
/* 1554 */       i1 |= j;
/* 1555 */       i2++;
/*      */     }
/* 1557 */     if ((paramString3 != null) && (!paramString3.equals(""))) {
/* 1558 */       i1 |= k;
/* 1559 */       i2++;
/*      */     }
/* 1561 */     if (paramInt > 1) {
/* 1562 */       i1 |= m;
/* 1563 */       i2++;
/*      */     }
/* 1565 */     if (paramBoolean) {
/* 1566 */       i1 |= n;
/* 1567 */       i2++;
/*      */     }
/*      */ 
/* 1570 */     String str = System.getProperty("os.name");
/*      */     String[] arrayOfString;
/* 1571 */     if ((str.equals("Linux")) || (str.contains("OS X"))) {
/* 1572 */       arrayOfString = new String[i2];
/* 1573 */       arrayOfString[(i3++)] = "/usr/bin/lpr";
/* 1574 */       if ((i1 & i) != 0) {
/* 1575 */         arrayOfString[(i3++)] = ("-P" + paramString1);
/*      */       }
/* 1577 */       if ((i1 & k) != 0) {
/* 1578 */         arrayOfString[(i3++)] = ("-J" + paramString3);
/*      */       }
/* 1580 */       if ((i1 & m) != 0) {
/* 1581 */         arrayOfString[(i3++)] = ("-#" + paramInt);
/*      */       }
/* 1583 */       if ((i1 & n) != 0) {
/* 1584 */         arrayOfString[(i3++)] = "-h";
/*      */       }
/* 1586 */       if ((i1 & j) != 0)
/* 1587 */         arrayOfString[(i3++)] = new String(paramString2);
/*      */     }
/*      */     else {
/* 1590 */       i2++;
/* 1591 */       arrayOfString = new String[i2];
/* 1592 */       arrayOfString[(i3++)] = "/usr/bin/lp";
/* 1593 */       arrayOfString[(i3++)] = "-c";
/* 1594 */       if ((i1 & i) != 0) {
/* 1595 */         arrayOfString[(i3++)] = ("-d" + paramString1);
/*      */       }
/* 1597 */       if ((i1 & k) != 0) {
/* 1598 */         arrayOfString[(i3++)] = ("-t" + paramString3);
/*      */       }
/* 1600 */       if ((i1 & m) != 0) {
/* 1601 */         arrayOfString[(i3++)] = ("-n" + paramInt);
/*      */       }
/* 1603 */       if ((i1 & n) != 0) {
/* 1604 */         arrayOfString[(i3++)] = "-o nobanner";
/*      */       }
/* 1606 */       if ((i1 & j) != 0) {
/* 1607 */         arrayOfString[(i3++)] = ("-o" + paramString2);
/*      */       }
/*      */     }
/* 1610 */     arrayOfString[(i3++)] = paramString4;
/* 1611 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   private static int swapBGRtoRGB(byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2) {
/* 1615 */     int i = 0;
/* 1616 */     while ((paramInt < paramArrayOfByte1.length - 2) && (i < paramArrayOfByte2.length - 2)) {
/* 1617 */       paramArrayOfByte2[(i++)] = paramArrayOfByte1[(paramInt + 2)];
/* 1618 */       paramArrayOfByte2[(i++)] = paramArrayOfByte1[(paramInt + 1)];
/* 1619 */       paramArrayOfByte2[(i++)] = paramArrayOfByte1[(paramInt + 0)];
/* 1620 */       paramInt += 3;
/*      */     }
/* 1622 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private String makeCharsetName(String paramString, char[] paramArrayOfChar)
/*      */   {
/* 1632 */     if ((paramString.equals("Cp1252")) || (paramString.equals("ISO8859_1")))
/* 1633 */       return "latin1";
/*      */     int i;
/* 1634 */     if (paramString.equals("UTF8"))
/*      */     {
/* 1636 */       for (i = 0; i < paramArrayOfChar.length; i++) {
/* 1637 */         if (paramArrayOfChar[i] > 'ÿ') {
/* 1638 */           return paramString.toLowerCase();
/*      */         }
/*      */       }
/* 1641 */       return "latin1";
/* 1642 */     }if (paramString.startsWith("ISO8859"))
/*      */     {
/* 1644 */       for (i = 0; i < paramArrayOfChar.length; i++) {
/* 1645 */         if (paramArrayOfChar[i] > '') {
/* 1646 */           return paramString.toLowerCase();
/*      */         }
/*      */       }
/* 1649 */       return "latin1";
/*      */     }
/* 1651 */     return paramString.toLowerCase();
/*      */   }
/*      */ 
/*      */   private void prepDrawing()
/*      */   {
/* 1662 */     while ((!isOuterGState()) && ((!getGState().canSetClip(this.mLastClip)) || (!getGState().mTransform.equals(this.mLastTransform))))
/*      */     {
/* 1666 */       grestore();
/*      */     }
/*      */ 
/* 1672 */     getGState().emitPSColor(this.mLastColor);
/*      */ 
/* 1678 */     if (isOuterGState()) {
/* 1679 */       gsave();
/* 1680 */       getGState().emitTransform(this.mLastTransform);
/* 1681 */       getGState().emitPSClip(this.mLastClip);
/*      */     }
/*      */   }
/*      */ 
/*      */   private GState getGState()
/*      */   {
/* 1703 */     int i = this.mGStateStack.size();
/* 1704 */     return (GState)this.mGStateStack.get(i - 1);
/*      */   }
/*      */ 
/*      */   private void gsave()
/*      */   {
/* 1713 */     GState localGState = getGState();
/* 1714 */     this.mGStateStack.add(new GState(localGState));
/* 1715 */     this.mPSStream.println("G");
/*      */   }
/*      */ 
/*      */   private void grestore()
/*      */   {
/* 1724 */     int i = this.mGStateStack.size();
/* 1725 */     this.mGStateStack.remove(i - 1);
/* 1726 */     this.mPSStream.println("R");
/*      */   }
/*      */ 
/*      */   private boolean isOuterGState()
/*      */   {
/* 1735 */     return this.mGStateStack.size() == 1;
/*      */   }
/*      */ 
/*      */   void convertToPSPath(PathIterator paramPathIterator)
/*      */   {
/* 1836 */     float[] arrayOfFloat = new float[6];
/*      */     int j;
/* 1843 */     if (paramPathIterator.getWindingRule() == 0)
/* 1844 */       j = 1;
/*      */     else {
/* 1846 */       j = 2;
/*      */     }
/*      */ 
/* 1849 */     beginPath();
/*      */ 
/* 1851 */     setFillMode(j);
/*      */ 
/* 1853 */     while (!paramPathIterator.isDone()) {
/* 1854 */       int i = paramPathIterator.currentSegment(arrayOfFloat);
/*      */ 
/* 1856 */       switch (i) {
/*      */       case 0:
/* 1858 */         moveTo(arrayOfFloat[0], arrayOfFloat[1]);
/* 1859 */         break;
/*      */       case 1:
/* 1862 */         lineTo(arrayOfFloat[0], arrayOfFloat[1]);
/* 1863 */         break;
/*      */       case 2:
/* 1868 */         float f1 = getPenX();
/* 1869 */         float f2 = getPenY();
/* 1870 */         float f3 = f1 + (arrayOfFloat[0] - f1) * 2.0F / 3.0F;
/* 1871 */         float f4 = f2 + (arrayOfFloat[1] - f2) * 2.0F / 3.0F;
/* 1872 */         float f5 = arrayOfFloat[2] - (arrayOfFloat[2] - arrayOfFloat[0]) * 2.0F / 3.0F;
/* 1873 */         float f6 = arrayOfFloat[3] - (arrayOfFloat[3] - arrayOfFloat[1]) * 2.0F / 3.0F;
/* 1874 */         bezierTo(f3, f4, f5, f6, arrayOfFloat[2], arrayOfFloat[3]);
/*      */ 
/* 1877 */         break;
/*      */       case 3:
/* 1880 */         bezierTo(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3], arrayOfFloat[4], arrayOfFloat[5]);
/*      */ 
/* 1883 */         break;
/*      */       case 4:
/* 1886 */         closeSubpath();
/*      */       }
/*      */ 
/* 1891 */       paramPathIterator.next();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void deviceFill(PathIterator paramPathIterator, Color paramColor, AffineTransform paramAffineTransform, Shape paramShape)
/*      */   {
/* 1903 */     setTransform(paramAffineTransform);
/* 1904 */     setClip(paramShape);
/* 1905 */     setColor(paramColor);
/* 1906 */     convertToPSPath(paramPathIterator);
/*      */ 
/* 1911 */     this.mPSStream.println("G");
/* 1912 */     selectClipPath();
/* 1913 */     fillPath();
/* 1914 */     this.mPSStream.println("R N");
/*      */   }
/*      */ 
/*      */   private byte[] rlEncode(byte[] paramArrayOfByte)
/*      */   {
/* 1936 */     int i = 0;
/* 1937 */     int j = 0;
/* 1938 */     int k = 0;
/* 1939 */     int m = 0;
/* 1940 */     byte[] arrayOfByte1 = new byte[paramArrayOfByte.length * 2 + 2];
/* 1941 */     while (i < paramArrayOfByte.length) {
/* 1942 */       if (m == 0) {
/* 1943 */         k = i++;
/* 1944 */         m = 1;
/*      */       }
/*      */ 
/* 1947 */       while ((m < 128) && (i < paramArrayOfByte.length) && (paramArrayOfByte[i] == paramArrayOfByte[k]))
/*      */       {
/* 1949 */         m++;
/* 1950 */         i++;
/*      */       }
/*      */ 
/* 1953 */       if (m > 1) {
/* 1954 */         arrayOfByte1[(j++)] = ((byte)(257 - m));
/* 1955 */         arrayOfByte1[(j++)] = paramArrayOfByte[k];
/* 1956 */         m = 0;
/*      */       }
/*      */       else
/*      */       {
/* 1961 */         while ((m < 128) && (i < paramArrayOfByte.length) && (paramArrayOfByte[i] != paramArrayOfByte[(i - 1)]))
/*      */         {
/* 1963 */           m++;
/* 1964 */           i++;
/*      */         }
/* 1966 */         arrayOfByte1[(j++)] = ((byte)(m - 1));
/* 1967 */         for (int n = k; n < k + m; n++) {
/* 1968 */           arrayOfByte1[(j++)] = paramArrayOfByte[n];
/*      */         }
/* 1970 */         m = 0;
/*      */       }
/*      */     }
/* 1972 */     arrayOfByte1[(j++)] = -128;
/* 1973 */     byte[] arrayOfByte2 = new byte[j];
/* 1974 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, j);
/*      */ 
/* 1976 */     return arrayOfByte2;
/*      */   }
/*      */ 
/*      */   private byte[] ascii85Encode(byte[] paramArrayOfByte)
/*      */   {
/* 1983 */     byte[] arrayOfByte1 = new byte[(paramArrayOfByte.length + 4) * 5 / 4 + 2];
/* 1984 */     long l1 = 85L;
/* 1985 */     long l2 = l1 * l1;
/* 1986 */     long l3 = l1 * l2;
/* 1987 */     long l4 = l1 * l3;
/* 1988 */     int i = 33;
/*      */ 
/* 1990 */     int j = 0;
/* 1991 */     int k = 0;
/*      */     long l5;
/*      */     long l6;
/* 1994 */     while (j + 3 < paramArrayOfByte.length) {
/* 1995 */       l5 = ((paramArrayOfByte[(j++)] & 0xFF) << 24) + ((paramArrayOfByte[(j++)] & 0xFF) << 16) + ((paramArrayOfByte[(j++)] & 0xFF) << 8) + (paramArrayOfByte[(j++)] & 0xFF);
/*      */ 
/* 1999 */       if (l5 == 0L) {
/* 2000 */         arrayOfByte1[(k++)] = 122;
/*      */       } else {
/* 2002 */         l6 = l5;
/* 2003 */         arrayOfByte1[(k++)] = ((byte)(int)(l6 / l4 + i)); l6 %= l4;
/* 2004 */         arrayOfByte1[(k++)] = ((byte)(int)(l6 / l3 + i)); l6 %= l3;
/* 2005 */         arrayOfByte1[(k++)] = ((byte)(int)(l6 / l2 + i)); l6 %= l2;
/* 2006 */         arrayOfByte1[(k++)] = ((byte)(int)(l6 / l1 + i)); l6 %= l1;
/* 2007 */         arrayOfByte1[(k++)] = ((byte)(int)(l6 + i));
/*      */       }
/*      */     }
/*      */ 
/* 2011 */     if (j < paramArrayOfByte.length) {
/* 2012 */       int m = paramArrayOfByte.length - j;
/*      */ 
/* 2014 */       l5 = 0L;
/* 2015 */       while (j < paramArrayOfByte.length) {
/* 2016 */         l5 = (l5 << 8) + (paramArrayOfByte[(j++)] & 0xFF);
/*      */       }
/*      */ 
/* 2019 */       int n = 4 - m;
/* 2020 */       while (n-- > 0) {
/* 2021 */         l5 <<= 8;
/*      */       }
/* 2023 */       byte[] arrayOfByte3 = new byte[5];
/* 2024 */       l6 = l5;
/* 2025 */       arrayOfByte3[0] = ((byte)(int)(l6 / l4 + i)); l6 %= l4;
/* 2026 */       arrayOfByte3[1] = ((byte)(int)(l6 / l3 + i)); l6 %= l3;
/* 2027 */       arrayOfByte3[2] = ((byte)(int)(l6 / l2 + i)); l6 %= l2;
/* 2028 */       arrayOfByte3[3] = ((byte)(int)(l6 / l1 + i)); l6 %= l1;
/* 2029 */       arrayOfByte3[4] = ((byte)(int)(l6 + i));
/*      */ 
/* 2031 */       for (int i1 = 0; i1 < m + 1; i1++) {
/* 2032 */         arrayOfByte1[(k++)] = arrayOfByte3[i1];
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2037 */     arrayOfByte1[(k++)] = 126; arrayOfByte1[(k++)] = 62;
/*      */ 
/* 2049 */     byte[] arrayOfByte2 = new byte[k];
/* 2050 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, k);
/* 2051 */     return arrayOfByte2;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  344 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/*  347 */         PSPrinterJob.access$002(PSPrinterJob.access$100());
/*  348 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public static class EPSPrinter
/*      */     implements Pageable
/*      */   {
/*      */     private PageFormat pf;
/*      */     private PSPrinterJob job;
/*      */     private int llx;
/*      */     private int lly;
/*      */     private int urx;
/*      */     private int ury;
/*      */     private Printable printable;
/*      */     private PrintStream stream;
/*      */     private String epsTitle;
/*      */ 
/*      */     public EPSPrinter(Printable paramPrintable, String paramString, PrintStream paramPrintStream, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2176 */       this.printable = paramPrintable;
/* 2177 */       this.epsTitle = paramString;
/* 2178 */       this.stream = paramPrintStream;
/* 2179 */       this.llx = paramInt1;
/* 2180 */       this.lly = paramInt2;
/* 2181 */       this.urx = (this.llx + paramInt3);
/* 2182 */       this.ury = (this.lly + paramInt4);
/*      */ 
/* 2186 */       Paper localPaper = new Paper();
/* 2187 */       localPaper.setSize(paramInt3, paramInt4);
/* 2188 */       localPaper.setImageableArea(0.0D, 0.0D, paramInt3, paramInt4);
/* 2189 */       this.pf = new PageFormat();
/* 2190 */       this.pf.setPaper(localPaper);
/*      */     }
/*      */ 
/*      */     public void print() throws PrinterException {
/* 2194 */       this.stream.println("%!PS-Adobe-3.0 EPSF-3.0");
/* 2195 */       this.stream.println("%%BoundingBox: " + this.llx + " " + this.lly + " " + this.urx + " " + this.ury);
/*      */ 
/* 2197 */       this.stream.println("%%Title: " + this.epsTitle);
/* 2198 */       this.stream.println("%%Creator: Java Printing");
/* 2199 */       this.stream.println("%%CreationDate: " + new Date());
/* 2200 */       this.stream.println("%%EndComments");
/* 2201 */       this.stream.println("/pluginSave save def");
/* 2202 */       this.stream.println("mark");
/*      */ 
/* 2204 */       this.job = new PSPrinterJob();
/* 2205 */       this.job.epsPrinter = this;
/* 2206 */       this.job.mPSStream = this.stream;
/* 2207 */       this.job.mDestType = 2;
/*      */ 
/* 2209 */       this.job.startDoc();
/*      */       try {
/* 2211 */         this.job.printPage(this, 0);
/*      */       } catch (Throwable localThrowable) {
/* 2213 */         if ((localThrowable instanceof PrinterException)) {
/* 2214 */           throw ((PrinterException)localThrowable);
/*      */         }
/* 2216 */         throw new PrinterException(localThrowable.toString());
/*      */       }
/*      */       finally {
/* 2219 */         this.stream.println("cleartomark");
/* 2220 */         this.stream.println("pluginSave restore");
/* 2221 */         this.job.endDoc();
/*      */       }
/* 2223 */       this.stream.flush();
/*      */     }
/*      */ 
/*      */     public int getNumberOfPages() {
/* 2227 */       return 1;
/*      */     }
/*      */ 
/*      */     public PageFormat getPageFormat(int paramInt) {
/* 2231 */       if (paramInt > 0) {
/* 2232 */         throw new IndexOutOfBoundsException("pgIndex");
/*      */       }
/* 2234 */       return this.pf;
/*      */     }
/*      */ 
/*      */     public Printable getPrintable(int paramInt)
/*      */     {
/* 2239 */       if (paramInt > 0) {
/* 2240 */         throw new IndexOutOfBoundsException("pgIndex");
/*      */       }
/* 2242 */       return this.printable;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class GState
/*      */   {
/*      */     Color mColor;
/*      */     Shape mClip;
/*      */     Font mFont;
/*      */     AffineTransform mTransform;
/*      */ 
/*      */     GState()
/*      */     {
/* 1750 */       this.mColor = Color.black;
/* 1751 */       this.mClip = null;
/* 1752 */       this.mFont = null;
/* 1753 */       this.mTransform = new AffineTransform();
/*      */     }
/*      */ 
/*      */     GState(GState arg2)
/*      */     {
/*      */       Object localObject;
/* 1757 */       this.mColor = localObject.mColor;
/* 1758 */       this.mClip = localObject.mClip;
/* 1759 */       this.mFont = localObject.mFont;
/* 1760 */       this.mTransform = localObject.mTransform;
/*      */     }
/*      */ 
/*      */     boolean canSetClip(Shape paramShape)
/*      */     {
/* 1765 */       return (this.mClip == null) || (this.mClip.equals(paramShape));
/*      */     }
/*      */ 
/*      */     void emitPSClip(Shape paramShape)
/*      */     {
/* 1770 */       if ((paramShape != null) && ((this.mClip == null) || (!this.mClip.equals(paramShape))))
/*      */       {
/* 1772 */         String str1 = PSPrinterJob.this.mFillOpStr;
/* 1773 */         String str2 = PSPrinterJob.this.mClipOpStr;
/* 1774 */         PSPrinterJob.this.convertToPSPath(paramShape.getPathIterator(new AffineTransform()));
/* 1775 */         PSPrinterJob.this.selectClipPath();
/* 1776 */         this.mClip = paramShape;
/*      */ 
/* 1778 */         PSPrinterJob.this.mClipOpStr = str1;
/* 1779 */         PSPrinterJob.this.mFillOpStr = str1;
/*      */       }
/*      */     }
/*      */ 
/*      */     void emitTransform(AffineTransform paramAffineTransform)
/*      */     {
/* 1785 */       if ((paramAffineTransform != null) && (!paramAffineTransform.equals(this.mTransform))) {
/* 1786 */         double[] arrayOfDouble = new double[6];
/* 1787 */         paramAffineTransform.getMatrix(arrayOfDouble);
/* 1788 */         PSPrinterJob.this.mPSStream.println("[" + (float)arrayOfDouble[0] + " " + (float)arrayOfDouble[1] + " " + (float)arrayOfDouble[2] + " " + (float)arrayOfDouble[3] + " " + (float)arrayOfDouble[4] + " " + (float)arrayOfDouble[5] + "] concat");
/*      */ 
/* 1796 */         this.mTransform = paramAffineTransform;
/*      */       }
/*      */     }
/*      */ 
/*      */     void emitPSColor(Color paramColor) {
/* 1801 */       if ((paramColor != null) && (!paramColor.equals(this.mColor))) {
/* 1802 */         float[] arrayOfFloat = paramColor.getRGBColorComponents(null);
/*      */ 
/* 1807 */         if ((arrayOfFloat[0] == arrayOfFloat[1]) && (arrayOfFloat[1] == arrayOfFloat[2])) {
/* 1808 */           PSPrinterJob.this.mPSStream.println(arrayOfFloat[0] + " SG");
/*      */         }
/*      */         else
/*      */         {
/* 1813 */           PSPrinterJob.this.mPSStream.println(arrayOfFloat[0] + " " + arrayOfFloat[1] + " " + arrayOfFloat[2] + " " + " SC");
/*      */         }
/*      */ 
/* 1819 */         this.mColor = paramColor;
/*      */       }
/*      */     }
/*      */ 
/*      */     void emitPSFont(int paramInt, float paramFloat)
/*      */     {
/* 1825 */       PSPrinterJob.this.mPSStream.println(paramFloat + " " + paramInt + " " + "F");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class PluginPrinter
/*      */     implements Printable
/*      */   {
/*      */     private PSPrinterJob.EPSPrinter epsPrinter;
/*      */     private Component applet;
/*      */     private PrintStream stream;
/*      */     private String epsTitle;
/*      */     private int bx;
/*      */     private int by;
/*      */     private int bw;
/*      */     private int bh;
/*      */     private int width;
/*      */     private int height;
/*      */ 
/*      */     public PluginPrinter(Component paramComponent, PrintStream paramPrintStream, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2100 */       this.applet = paramComponent;
/* 2101 */       this.epsTitle = "Java Plugin Applet";
/* 2102 */       this.stream = paramPrintStream;
/* 2103 */       this.bx = paramInt1;
/* 2104 */       this.by = paramInt2;
/* 2105 */       this.bw = paramInt3;
/* 2106 */       this.bh = paramInt4;
/* 2107 */       this.width = paramComponent.size().width;
/* 2108 */       this.height = paramComponent.size().height;
/* 2109 */       this.epsPrinter = new PSPrinterJob.EPSPrinter(this, this.epsTitle, paramPrintStream, 0, 0, this.width, this.height);
/*      */     }
/*      */ 
/*      */     public void printPluginPSHeader()
/*      */     {
/* 2114 */       this.stream.println("%%BeginDocument: JavaPluginApplet");
/*      */     }
/*      */ 
/*      */     public void printPluginApplet() {
/*      */       try {
/* 2119 */         this.epsPrinter.print();
/*      */       } catch (PrinterException localPrinterException) {
/*      */       }
/*      */     }
/*      */ 
/*      */     public void printPluginPSTrailer() {
/* 2125 */       this.stream.println("%%EndDocument: JavaPluginApplet");
/* 2126 */       this.stream.flush();
/*      */     }
/*      */ 
/*      */     public void printAll() {
/* 2130 */       printPluginPSHeader();
/* 2131 */       printPluginApplet();
/* 2132 */       printPluginPSTrailer();
/*      */     }
/*      */ 
/*      */     public int print(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt) {
/* 2136 */       if (paramInt > 0) {
/* 2137 */         return 1;
/*      */       }
/*      */ 
/* 2143 */       this.applet.printAll(paramGraphics);
/* 2144 */       return 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class PrinterOpener
/*      */     implements PrivilegedAction
/*      */   {
/*      */     PrinterException pex;
/*      */     OutputStream result;
/*      */ 
/*      */     private PrinterOpener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Object run()
/*      */     {
/*      */       try
/*      */       {
/*  663 */         PSPrinterJob.this.spoolFile = Files.createTempFile("javaprint", ".ps", new FileAttribute[0]).toFile();
/*  664 */         PSPrinterJob.this.spoolFile.deleteOnExit();
/*      */ 
/*  666 */         this.result = new FileOutputStream(PSPrinterJob.this.spoolFile);
/*  667 */         return this.result;
/*      */       }
/*      */       catch (IOException localIOException) {
/*  670 */         this.pex = new PrinterIOException(localIOException);
/*      */       }
/*  672 */       return null; }  } 
/*      */   private class PrinterSpooler implements PrivilegedAction { PrinterException pex;
/*      */ 
/*      */     private PrinterSpooler() {  } 
/*      */     // ERROR //
/*      */     private void handleProcessFailure(Process paramProcess, String[] paramArrayOfString, int paramInt) throws IOException { // Byte code:
/*      */       //   0: new 83	java/io/StringWriter
/*      */       //   3: dup
/*      */       //   4: invokespecial 165	java/io/StringWriter:<init>	()V
/*      */       //   7: astore 4
/*      */       //   9: aconst_null
/*      */       //   10: astore 5
/*      */       //   12: new 82	java/io/PrintWriter
/*      */       //   15: dup
/*      */       //   16: aload 4
/*      */       //   18: invokespecial 163	java/io/PrintWriter:<init>	(Ljava/io/Writer;)V
/*      */       //   21: astore 6
/*      */       //   23: aconst_null
/*      */       //   24: astore 7
/*      */       //   26: aload 6
/*      */       //   28: ldc 6
/*      */       //   30: invokevirtual 164	java/io/PrintWriter:append	(Ljava/lang/CharSequence;)Ljava/io/PrintWriter;
/*      */       //   33: iload_3
/*      */       //   34: invokestatic 168	java/lang/Integer:toString	(I)Ljava/lang/String;
/*      */       //   37: invokevirtual 164	java/io/PrintWriter:append	(Ljava/lang/CharSequence;)Ljava/io/PrintWriter;
/*      */       //   40: pop
/*      */       //   41: aload 6
/*      */       //   43: ldc 3
/*      */       //   45: invokevirtual 164	java/io/PrintWriter:append	(Ljava/lang/CharSequence;)Ljava/io/PrintWriter;
/*      */       //   48: pop
/*      */       //   49: aload_2
/*      */       //   50: astore 8
/*      */       //   52: aload 8
/*      */       //   54: arraylength
/*      */       //   55: istore 9
/*      */       //   57: iconst_0
/*      */       //   58: istore 10
/*      */       //   60: iload 10
/*      */       //   62: iload 9
/*      */       //   64: if_icmpge +34 -> 98
/*      */       //   67: aload 8
/*      */       //   69: iload 10
/*      */       //   71: aaload
/*      */       //   72: astore 11
/*      */       //   74: aload 6
/*      */       //   76: ldc 2
/*      */       //   78: invokevirtual 164	java/io/PrintWriter:append	(Ljava/lang/CharSequence;)Ljava/io/PrintWriter;
/*      */       //   81: aload 11
/*      */       //   83: invokevirtual 164	java/io/PrintWriter:append	(Ljava/lang/CharSequence;)Ljava/io/PrintWriter;
/*      */       //   86: ldc 4
/*      */       //   88: invokevirtual 164	java/io/PrintWriter:append	(Ljava/lang/CharSequence;)Ljava/io/PrintWriter;
/*      */       //   91: pop
/*      */       //   92: iinc 10 1
/*      */       //   95: goto -35 -> 60
/*      */       //   98: aload_1
/*      */       //   99: invokevirtual 173	java/lang/Process:getErrorStream	()Ljava/io/InputStream;
/*      */       //   102: astore 8
/*      */       //   104: aconst_null
/*      */       //   105: astore 9
/*      */       //   107: new 81	java/io/InputStreamReader
/*      */       //   110: dup
/*      */       //   111: aload 8
/*      */       //   113: invokespecial 159	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
/*      */       //   116: astore 10
/*      */       //   118: aconst_null
/*      */       //   119: astore 11
/*      */       //   121: new 77	java/io/BufferedReader
/*      */       //   124: dup
/*      */       //   125: aload 10
/*      */       //   127: invokespecial 151	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
/*      */       //   130: astore 12
/*      */       //   132: aconst_null
/*      */       //   133: astore 13
/*      */       //   135: aload 12
/*      */       //   137: invokevirtual 150	java/io/BufferedReader:ready	()Z
/*      */       //   140: ifeq +27 -> 167
/*      */       //   143: aload 6
/*      */       //   145: invokevirtual 162	java/io/PrintWriter:println	()V
/*      */       //   148: aload 6
/*      */       //   150: ldc 1
/*      */       //   152: invokevirtual 164	java/io/PrintWriter:append	(Ljava/lang/CharSequence;)Ljava/io/PrintWriter;
/*      */       //   155: aload 12
/*      */       //   157: invokevirtual 152	java/io/BufferedReader:readLine	()Ljava/lang/String;
/*      */       //   160: invokevirtual 164	java/io/PrintWriter:append	(Ljava/lang/CharSequence;)Ljava/io/PrintWriter;
/*      */       //   163: pop
/*      */       //   164: goto -29 -> 135
/*      */       //   167: aload 12
/*      */       //   169: ifnull +85 -> 254
/*      */       //   172: aload 13
/*      */       //   174: ifnull +23 -> 197
/*      */       //   177: aload 12
/*      */       //   179: invokevirtual 149	java/io/BufferedReader:close	()V
/*      */       //   182: goto +72 -> 254
/*      */       //   185: astore 14
/*      */       //   187: aload 13
/*      */       //   189: aload 14
/*      */       //   191: invokevirtual 176	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*      */       //   194: goto +60 -> 254
/*      */       //   197: aload 12
/*      */       //   199: invokevirtual 149	java/io/BufferedReader:close	()V
/*      */       //   202: goto +52 -> 254
/*      */       //   205: astore 14
/*      */       //   207: aload 14
/*      */       //   209: astore 13
/*      */       //   211: aload 14
/*      */       //   213: athrow
/*      */       //   214: astore 15
/*      */       //   216: aload 12
/*      */       //   218: ifnull +33 -> 251
/*      */       //   221: aload 13
/*      */       //   223: ifnull +23 -> 246
/*      */       //   226: aload 12
/*      */       //   228: invokevirtual 149	java/io/BufferedReader:close	()V
/*      */       //   231: goto +20 -> 251
/*      */       //   234: astore 16
/*      */       //   236: aload 13
/*      */       //   238: aload 16
/*      */       //   240: invokevirtual 176	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*      */       //   243: goto +8 -> 251
/*      */       //   246: aload 12
/*      */       //   248: invokevirtual 149	java/io/BufferedReader:close	()V
/*      */       //   251: aload 15
/*      */       //   253: athrow
/*      */       //   254: aload 10
/*      */       //   256: ifnull +85 -> 341
/*      */       //   259: aload 11
/*      */       //   261: ifnull +23 -> 284
/*      */       //   264: aload 10
/*      */       //   266: invokevirtual 158	java/io/InputStreamReader:close	()V
/*      */       //   269: goto +72 -> 341
/*      */       //   272: astore 12
/*      */       //   274: aload 11
/*      */       //   276: aload 12
/*      */       //   278: invokevirtual 176	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*      */       //   281: goto +60 -> 341
/*      */       //   284: aload 10
/*      */       //   286: invokevirtual 158	java/io/InputStreamReader:close	()V
/*      */       //   289: goto +52 -> 341
/*      */       //   292: astore 12
/*      */       //   294: aload 12
/*      */       //   296: astore 11
/*      */       //   298: aload 12
/*      */       //   300: athrow
/*      */       //   301: astore 17
/*      */       //   303: aload 10
/*      */       //   305: ifnull +33 -> 338
/*      */       //   308: aload 11
/*      */       //   310: ifnull +23 -> 333
/*      */       //   313: aload 10
/*      */       //   315: invokevirtual 158	java/io/InputStreamReader:close	()V
/*      */       //   318: goto +20 -> 338
/*      */       //   321: astore 18
/*      */       //   323: aload 11
/*      */       //   325: aload 18
/*      */       //   327: invokevirtual 176	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*      */       //   330: goto +8 -> 338
/*      */       //   333: aload 10
/*      */       //   335: invokevirtual 158	java/io/InputStreamReader:close	()V
/*      */       //   338: aload 17
/*      */       //   340: athrow
/*      */       //   341: aload 8
/*      */       //   343: ifnull +85 -> 428
/*      */       //   346: aload 9
/*      */       //   348: ifnull +23 -> 371
/*      */       //   351: aload 8
/*      */       //   353: invokevirtual 157	java/io/InputStream:close	()V
/*      */       //   356: goto +72 -> 428
/*      */       //   359: astore 10
/*      */       //   361: aload 9
/*      */       //   363: aload 10
/*      */       //   365: invokevirtual 176	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*      */       //   368: goto +60 -> 428
/*      */       //   371: aload 8
/*      */       //   373: invokevirtual 157	java/io/InputStream:close	()V
/*      */       //   376: goto +52 -> 428
/*      */       //   379: astore 10
/*      */       //   381: aload 10
/*      */       //   383: astore 9
/*      */       //   385: aload 10
/*      */       //   387: athrow
/*      */       //   388: astore 19
/*      */       //   390: aload 8
/*      */       //   392: ifnull +33 -> 425
/*      */       //   395: aload 9
/*      */       //   397: ifnull +23 -> 420
/*      */       //   400: aload 8
/*      */       //   402: invokevirtual 157	java/io/InputStream:close	()V
/*      */       //   405: goto +20 -> 425
/*      */       //   408: astore 20
/*      */       //   410: aload 9
/*      */       //   412: aload 20
/*      */       //   414: invokevirtual 176	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*      */       //   417: goto +8 -> 425
/*      */       //   420: aload 8
/*      */       //   422: invokevirtual 157	java/io/InputStream:close	()V
/*      */       //   425: aload 19
/*      */       //   427: athrow
/*      */       //   428: aload 6
/*      */       //   430: invokevirtual 161	java/io/PrintWriter:flush	()V
/*      */       //   433: new 79	java/io/IOException
/*      */       //   436: dup
/*      */       //   437: aload 4
/*      */       //   439: invokevirtual 167	java/io/StringWriter:toString	()Ljava/lang/String;
/*      */       //   442: invokespecial 156	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */       //   445: athrow
/*      */       //   446: astore 21
/*      */       //   448: aload 6
/*      */       //   450: invokevirtual 161	java/io/PrintWriter:flush	()V
/*      */       //   453: new 79	java/io/IOException
/*      */       //   456: dup
/*      */       //   457: aload 4
/*      */       //   459: invokevirtual 167	java/io/StringWriter:toString	()Ljava/lang/String;
/*      */       //   462: invokespecial 156	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */       //   465: athrow
/*      */       //   466: astore 8
/*      */       //   468: aload 8
/*      */       //   470: astore 7
/*      */       //   472: aload 8
/*      */       //   474: athrow
/*      */       //   475: astore 22
/*      */       //   477: aload 6
/*      */       //   479: ifnull +33 -> 512
/*      */       //   482: aload 7
/*      */       //   484: ifnull +23 -> 507
/*      */       //   487: aload 6
/*      */       //   489: invokevirtual 160	java/io/PrintWriter:close	()V
/*      */       //   492: goto +20 -> 512
/*      */       //   495: astore 23
/*      */       //   497: aload 7
/*      */       //   499: aload 23
/*      */       //   501: invokevirtual 176	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*      */       //   504: goto +8 -> 512
/*      */       //   507: aload 6
/*      */       //   509: invokevirtual 160	java/io/PrintWriter:close	()V
/*      */       //   512: aload 22
/*      */       //   514: athrow
/*      */       //   515: astore 6
/*      */       //   517: aload 6
/*      */       //   519: astore 5
/*      */       //   521: aload 6
/*      */       //   523: athrow
/*      */       //   524: astore 24
/*      */       //   526: aload 4
/*      */       //   528: ifnull +33 -> 561
/*      */       //   531: aload 5
/*      */       //   533: ifnull +23 -> 556
/*      */       //   536: aload 4
/*      */       //   538: invokevirtual 166	java/io/StringWriter:close	()V
/*      */       //   541: goto +20 -> 561
/*      */       //   544: astore 25
/*      */       //   546: aload 5
/*      */       //   548: aload 25
/*      */       //   550: invokevirtual 176	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*      */       //   553: goto +8 -> 561
/*      */       //   556: aload 4
/*      */       //   558: invokevirtual 166	java/io/StringWriter:close	()V
/*      */       //   561: aload 24
/*      */       //   563: athrow
/*      */       //
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   177	182	185	java/lang/Throwable
/*      */       //   135	167	205	java/lang/Throwable
/*      */       //   135	167	214	finally
/*      */       //   205	216	214	finally
/*      */       //   226	231	234	java/lang/Throwable
/*      */       //   264	269	272	java/lang/Throwable
/*      */       //   121	254	292	java/lang/Throwable
/*      */       //   121	254	301	finally
/*      */       //   292	303	301	finally
/*      */       //   313	318	321	java/lang/Throwable
/*      */       //   351	356	359	java/lang/Throwable
/*      */       //   107	341	379	java/lang/Throwable
/*      */       //   107	341	388	finally
/*      */       //   379	390	388	finally
/*      */       //   400	405	408	java/lang/Throwable
/*      */       //   98	428	446	finally
/*      */       //   446	448	446	finally
/*      */       //   26	466	466	java/lang/Throwable
/*      */       //   26	477	475	finally
/*      */       //   487	492	495	java/lang/Throwable
/*      */       //   12	515	515	java/lang/Throwable
/*      */       //   12	526	524	finally
/*      */       //   536	541	544	java/lang/Throwable } 
/*  705 */     public Object run() { if ((PSPrinterJob.this.spoolFile == null) || (!PSPrinterJob.this.spoolFile.exists())) {
/*  706 */         this.pex = new PrinterException("No spool file");
/*  707 */         return null;
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  713 */         String str = PSPrinterJob.this.spoolFile.getAbsolutePath();
/*  714 */         String[] arrayOfString = PSPrinterJob.this.printExecCmd(PSPrinterJob.this.mDestination, PSPrinterJob.this.mOptions, PSPrinterJob.this.mNoJobSheet, PSPrinterJob.this.getJobNameInt(), 1, str);
/*      */ 
/*  718 */         Process localProcess = Runtime.getRuntime().exec(arrayOfString);
/*  719 */         localProcess.waitFor();
/*  720 */         int i = localProcess.exitValue();
/*  721 */         if (0 != i)
/*  722 */           handleProcessFailure(localProcess, arrayOfString, i);
/*      */       }
/*      */       catch (IOException localIOException) {
/*  725 */         this.pex = new PrinterIOException(localIOException);
/*      */       } catch (InterruptedException localInterruptedException) {
/*  727 */         this.pex = new PrinterException(localInterruptedException.toString());
/*      */       } finally {
/*  729 */         PSPrinterJob.this.spoolFile.delete();
/*      */       }
/*  731 */       return null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.PSPrinterJob
 * JD-Core Version:    0.6.2
 */