/*     */ package sun.print;
/*     */ 
/*     */ import java.awt.print.PageFormat;
/*     */ import java.awt.print.Pageable;
/*     */ import java.awt.print.Paper;
/*     */ import java.awt.print.Printable;
/*     */ import java.awt.print.PrinterException;
/*     */ import java.awt.print.PrinterJob;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.Vector;
/*     */ import javax.print.CancelablePrintJob;
/*     */ import javax.print.Doc;
/*     */ import javax.print.DocFlavor;
/*     */ import javax.print.DocFlavor.BYTE_ARRAY;
/*     */ import javax.print.DocFlavor.INPUT_STREAM;
/*     */ import javax.print.DocFlavor.URL;
/*     */ import javax.print.PrintException;
/*     */ import javax.print.PrintService;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.AttributeSetUtilities;
/*     */ import javax.print.attribute.DocAttributeSet;
/*     */ import javax.print.attribute.HashPrintJobAttributeSet;
/*     */ import javax.print.attribute.HashPrintRequestAttributeSet;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintJobAttributeSet;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ import javax.print.attribute.PrintRequestAttributeSet;
/*     */ import javax.print.attribute.standard.Copies;
/*     */ import javax.print.attribute.standard.Destination;
/*     */ import javax.print.attribute.standard.DocumentName;
/*     */ import javax.print.attribute.standard.Fidelity;
/*     */ import javax.print.attribute.standard.JobName;
/*     */ import javax.print.attribute.standard.JobOriginatingUserName;
/*     */ import javax.print.attribute.standard.Media;
/*     */ import javax.print.attribute.standard.MediaSize;
/*     */ import javax.print.attribute.standard.MediaSizeName;
/*     */ import javax.print.attribute.standard.OrientationRequested;
/*     */ import javax.print.attribute.standard.PrinterIsAcceptingJobs;
/*     */ import javax.print.attribute.standard.PrinterState;
/*     */ import javax.print.attribute.standard.PrinterStateReason;
/*     */ import javax.print.attribute.standard.PrinterStateReasons;
/*     */ import javax.print.attribute.standard.RequestingUserName;
/*     */ import javax.print.event.PrintJobAttributeListener;
/*     */ import javax.print.event.PrintJobEvent;
/*     */ import javax.print.event.PrintJobListener;
/*     */ import sun.awt.windows.WPrinterJob;
/*     */ 
/*     */ public class Win32PrintJob
/*     */   implements CancelablePrintJob
/*     */ {
/*     */   private transient Vector jobListeners;
/*     */   private transient Vector attrListeners;
/*     */   private transient Vector listenedAttributeSets;
/*     */   private Win32PrintService service;
/*     */   private boolean fidelity;
/*  88 */   private boolean printing = false;
/*  89 */   private boolean printReturned = false;
/*  90 */   private PrintRequestAttributeSet reqAttrSet = null;
/*  91 */   private PrintJobAttributeSet jobAttrSet = null;
/*     */   private PrinterJob job;
/*     */   private Doc doc;
/*  94 */   private String mDestination = null;
/*     */ 
/* 100 */   private InputStream instream = null;
/* 101 */   private Reader reader = null;
/*     */ 
/* 104 */   private String jobName = "Java Printing";
/* 105 */   private int copies = 0;
/* 106 */   private MediaSizeName mediaName = null;
/* 107 */   private MediaSize mediaSize = null;
/* 108 */   private OrientationRequested orient = null;
/*     */   private long hPrintJob;
/*     */   private static final int PRINTBUFFERLEN = 8192;
/*     */ 
/*     */   Win32PrintJob(Win32PrintService paramWin32PrintService)
/*     */   {
/* 117 */     this.service = paramWin32PrintService;
/*     */   }
/*     */ 
/*     */   public PrintService getPrintService() {
/* 121 */     return this.service;
/*     */   }
/*     */ 
/*     */   public PrintJobAttributeSet getAttributes() {
/* 125 */     synchronized (this) {
/* 126 */       if (this.jobAttrSet == null)
/*     */       {
/* 128 */         HashPrintJobAttributeSet localHashPrintJobAttributeSet = new HashPrintJobAttributeSet();
/* 129 */         return AttributeSetUtilities.unmodifiableView(localHashPrintJobAttributeSet);
/*     */       }
/* 131 */       return this.jobAttrSet;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addPrintJobListener(PrintJobListener paramPrintJobListener)
/*     */   {
/* 137 */     synchronized (this) {
/* 138 */       if (paramPrintJobListener == null) {
/* 139 */         return;
/*     */       }
/* 141 */       if (this.jobListeners == null) {
/* 142 */         this.jobListeners = new Vector();
/*     */       }
/* 144 */       this.jobListeners.add(paramPrintJobListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removePrintJobListener(PrintJobListener paramPrintJobListener) {
/* 149 */     synchronized (this) {
/* 150 */       if ((paramPrintJobListener == null) || (this.jobListeners == null)) {
/* 151 */         return;
/*     */       }
/* 153 */       this.jobListeners.remove(paramPrintJobListener);
/* 154 */       if (this.jobListeners.isEmpty())
/* 155 */         this.jobListeners = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void closeDataStreams()
/*     */   {
/* 169 */     if (this.doc == null) {
/* 170 */       return;
/*     */     }
/*     */ 
/* 173 */     Object localObject1 = null;
/*     */     try
/*     */     {
/* 176 */       localObject1 = this.doc.getPrintData();
/*     */     } catch (IOException localIOException1) {
/* 178 */       return;
/*     */     }
/*     */ 
/* 181 */     if (this.instream != null) {
/*     */       try {
/* 183 */         this.instream.close();
/*     */       } catch (IOException localIOException2) {
/*     */       } finally {
/* 186 */         this.instream = null;
/*     */       }
/*     */     }
/* 189 */     else if (this.reader != null) {
/*     */       try {
/* 191 */         this.reader.close();
/*     */       } catch (IOException localIOException3) {
/*     */       } finally {
/* 194 */         this.reader = null;
/*     */       }
/*     */     }
/* 197 */     else if ((localObject1 instanceof InputStream))
/*     */       try {
/* 199 */         ((InputStream)localObject1).close();
/*     */       }
/*     */       catch (IOException localIOException4) {
/*     */       }
/* 203 */     else if ((localObject1 instanceof Reader))
/*     */       try {
/* 205 */         ((Reader)localObject1).close();
/*     */       }
/*     */       catch (IOException localIOException5)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   private void notifyEvent(int paramInt)
/*     */   {
/* 216 */     switch (paramInt) {
/*     */     case 101:
/*     */     case 102:
/*     */     case 103:
/*     */     case 105:
/*     */     case 106:
/* 222 */       closeDataStreams();
/*     */     case 104:
/*     */     }
/* 225 */     synchronized (this) {
/* 226 */       if (this.jobListeners != null)
/*     */       {
/* 228 */         PrintJobEvent localPrintJobEvent = new PrintJobEvent(this, paramInt);
/* 229 */         for (int i = 0; i < this.jobListeners.size(); i++) {
/* 230 */           PrintJobListener localPrintJobListener = (PrintJobListener)this.jobListeners.elementAt(i);
/* 231 */           switch (paramInt)
/*     */           {
/*     */           case 102:
/* 234 */             localPrintJobListener.printJobCompleted(localPrintJobEvent);
/* 235 */             break;
/*     */           case 101:
/* 238 */             localPrintJobListener.printJobCanceled(localPrintJobEvent);
/* 239 */             break;
/*     */           case 103:
/* 242 */             localPrintJobListener.printJobFailed(localPrintJobEvent);
/* 243 */             break;
/*     */           case 106:
/* 246 */             localPrintJobListener.printDataTransferCompleted(localPrintJobEvent);
/* 247 */             break;
/*     */           case 105:
/* 250 */             localPrintJobListener.printJobNoMoreEvents(localPrintJobEvent);
/*     */           case 104:
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addPrintJobAttributeListener(PrintJobAttributeListener paramPrintJobAttributeListener, PrintJobAttributeSet paramPrintJobAttributeSet)
/*     */   {
/* 264 */     synchronized (this) {
/* 265 */       if (paramPrintJobAttributeListener == null) {
/* 266 */         return;
/*     */       }
/* 268 */       if (this.attrListeners == null) {
/* 269 */         this.attrListeners = new Vector();
/* 270 */         this.listenedAttributeSets = new Vector();
/*     */       }
/* 272 */       this.attrListeners.add(paramPrintJobAttributeListener);
/* 273 */       if (paramPrintJobAttributeSet == null) {
/* 274 */         paramPrintJobAttributeSet = new HashPrintJobAttributeSet();
/*     */       }
/* 276 */       this.listenedAttributeSets.add(paramPrintJobAttributeSet);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removePrintJobAttributeListener(PrintJobAttributeListener paramPrintJobAttributeListener)
/*     */   {
/* 282 */     synchronized (this) {
/* 283 */       if ((paramPrintJobAttributeListener == null) || (this.attrListeners == null)) {
/* 284 */         return;
/*     */       }
/* 286 */       int i = this.attrListeners.indexOf(paramPrintJobAttributeListener);
/* 287 */       if (i == -1) {
/* 288 */         return;
/*     */       }
/* 290 */       this.attrListeners.remove(i);
/* 291 */       this.listenedAttributeSets.remove(i);
/* 292 */       if (this.attrListeners.isEmpty()) {
/* 293 */         this.attrListeners = null;
/* 294 */         this.listenedAttributeSets = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void print(Doc paramDoc, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */     throws PrintException
/*     */   {
/* 303 */     synchronized (this) {
/* 304 */       if (this.printing) {
/* 305 */         throw new PrintException("already printing");
/*     */       }
/* 307 */       this.printing = true;
/*     */     }
/*     */ 
/* 311 */     ??? = (PrinterState)this.service.getAttribute(PrinterState.class);
/*     */ 
/* 313 */     if (??? == PrinterState.STOPPED) {
/* 314 */       localObject2 = (PrinterStateReasons)this.service.getAttribute(PrinterStateReasons.class);
/*     */ 
/* 317 */       if ((localObject2 != null) && (((PrinterStateReasons)localObject2).containsKey(PrinterStateReason.SHUTDOWN)))
/*     */       {
/* 320 */         throw new PrintException("PrintService is no longer available.");
/*     */       }
/*     */     }
/*     */ 
/* 324 */     if ((PrinterIsAcceptingJobs)this.service.getAttribute(PrinterIsAcceptingJobs.class) == PrinterIsAcceptingJobs.NOT_ACCEPTING_JOBS)
/*     */     {
/* 327 */       throw new PrintException("Printer is not accepting job.");
/*     */     }
/*     */ 
/* 331 */     this.doc = paramDoc;
/*     */ 
/* 333 */     Object localObject2 = paramDoc.getDocFlavor();
/*     */     Object localObject3;
/*     */     try {
/* 337 */       localObject3 = paramDoc.getPrintData();
/*     */     } catch (IOException localIOException1) {
/* 339 */       notifyEvent(103);
/* 340 */       throw new PrintException("can't get print data: " + localIOException1.toString());
/*     */     }
/*     */ 
/* 343 */     if ((localObject2 == null) || (!this.service.isDocFlavorSupported((DocFlavor)localObject2))) {
/* 344 */       notifyEvent(103);
/* 345 */       throw new PrintJobFlavorException("invalid flavor", (DocFlavor)localObject2);
/*     */     }
/*     */ 
/* 348 */     initializeAttributeSets(paramDoc, paramPrintRequestAttributeSet);
/*     */ 
/* 350 */     getAttributeValues((DocFlavor)localObject2);
/*     */ 
/* 352 */     String str = ((DocFlavor)localObject2).getRepresentationClassName();
/*     */ 
/* 354 */     if ((((DocFlavor)localObject2).equals(DocFlavor.INPUT_STREAM.GIF)) || (((DocFlavor)localObject2).equals(DocFlavor.INPUT_STREAM.JPEG)) || (((DocFlavor)localObject2).equals(DocFlavor.INPUT_STREAM.PNG)) || (((DocFlavor)localObject2).equals(DocFlavor.BYTE_ARRAY.GIF)) || (((DocFlavor)localObject2).equals(DocFlavor.BYTE_ARRAY.JPEG)) || (((DocFlavor)localObject2).equals(DocFlavor.BYTE_ARRAY.PNG)))
/*     */     {
/*     */       try
/*     */       {
/* 361 */         this.instream = paramDoc.getStreamForBytes();
/* 362 */         if (this.instream == null) {
/* 363 */           notifyEvent(103);
/* 364 */           throw new PrintException("No stream for data");
/*     */         }
/* 366 */         printableJob(new ImagePrinter(this.instream));
/* 367 */         this.service.wakeNotifier();
/* 368 */         return;
/*     */       } catch (ClassCastException localClassCastException1) {
/* 370 */         notifyEvent(103);
/* 371 */         throw new PrintException(localClassCastException1);
/*     */       } catch (IOException localIOException2) {
/* 373 */         notifyEvent(103);
/* 374 */         throw new PrintException(localIOException2);
/*     */       }
/*     */     }
/* 376 */     if ((((DocFlavor)localObject2).equals(DocFlavor.URL.GIF)) || (((DocFlavor)localObject2).equals(DocFlavor.URL.JPEG)) || (((DocFlavor)localObject2).equals(DocFlavor.URL.PNG)))
/*     */     {
/*     */       try
/*     */       {
/* 380 */         printableJob(new ImagePrinter((URL)localObject3));
/* 381 */         this.service.wakeNotifier();
/* 382 */         return;
/*     */       } catch (ClassCastException localClassCastException2) {
/* 384 */         notifyEvent(103);
/* 385 */         throw new PrintException(localClassCastException2);
/*     */       }
/*     */     }
/* 387 */     if (str.equals("java.awt.print.Pageable"))
/*     */       try {
/* 389 */         pageableJob((Pageable)paramDoc.getPrintData());
/* 390 */         this.service.wakeNotifier();
/* 391 */         return;
/*     */       } catch (ClassCastException localClassCastException3) {
/* 393 */         notifyEvent(103);
/* 394 */         throw new PrintException(localClassCastException3);
/*     */       } catch (IOException localIOException3) {
/* 396 */         notifyEvent(103);
/* 397 */         throw new PrintException(localIOException3);
/*     */       }
/* 399 */     if (str.equals("java.awt.print.Printable"))
/*     */       try {
/* 401 */         printableJob((Printable)paramDoc.getPrintData());
/* 402 */         this.service.wakeNotifier();
/* 403 */         return;
/*     */       } catch (ClassCastException localClassCastException4) {
/* 405 */         notifyEvent(103);
/* 406 */         throw new PrintException(localClassCastException4);
/*     */       } catch (IOException localIOException4) {
/* 408 */         notifyEvent(103);
/* 409 */         throw new PrintException(localIOException4);
/*     */       }
/* 411 */     if ((str.equals("[B")) || (str.equals("java.io.InputStream")) || (str.equals("java.net.URL")))
/*     */     {
/* 415 */       if (str.equals("java.net.URL")) {
/* 416 */         URL localURL = (URL)localObject3;
/*     */         try {
/* 418 */           this.instream = localURL.openStream();
/*     */         } catch (IOException localIOException7) {
/* 420 */           notifyEvent(103);
/* 421 */           throw new PrintException(localIOException7.toString());
/*     */         }
/*     */       } else {
/*     */         try {
/* 425 */           this.instream = paramDoc.getStreamForBytes();
/*     */         } catch (IOException localIOException5) {
/* 427 */           notifyEvent(103);
/* 428 */           throw new PrintException(localIOException5.toString());
/*     */         }
/*     */       }
/*     */ 
/* 432 */       if (this.instream == null) {
/* 433 */         notifyEvent(103);
/* 434 */         throw new PrintException("No stream for data");
/*     */       }
/*     */ 
/* 437 */       if (this.mDestination != null) {
/*     */         try {
/* 439 */           FileOutputStream localFileOutputStream = new FileOutputStream(this.mDestination);
/* 440 */           byte[] arrayOfByte1 = new byte[1024];
/*     */           int j;
/* 443 */           while ((j = this.instream.read(arrayOfByte1, 0, arrayOfByte1.length)) >= 0) {
/* 444 */             localFileOutputStream.write(arrayOfByte1, 0, j);
/*     */           }
/* 446 */           localFileOutputStream.flush();
/* 447 */           localFileOutputStream.close();
/*     */         } catch (FileNotFoundException localFileNotFoundException) {
/* 449 */           notifyEvent(103);
/* 450 */           throw new PrintException(localFileNotFoundException.toString());
/*     */         } catch (IOException localIOException6) {
/* 452 */           notifyEvent(103);
/* 453 */           throw new PrintException(localIOException6.toString());
/*     */         }
/* 455 */         notifyEvent(106);
/* 456 */         notifyEvent(102);
/* 457 */         this.service.wakeNotifier();
/* 458 */         return;
/*     */       }
/*     */ 
/* 461 */       if (!startPrintRawData(this.service.getName(), this.jobName)) {
/* 462 */         notifyEvent(103);
/* 463 */         throw new PrintException("Print job failed to start.");
/*     */       }
/* 465 */       BufferedInputStream localBufferedInputStream = new BufferedInputStream(this.instream);
/* 466 */       int i = 0;
/*     */       try {
/* 468 */         byte[] arrayOfByte2 = new byte[8192];
/*     */ 
/* 470 */         while ((i = localBufferedInputStream.read(arrayOfByte2, 0, 8192)) >= 0) {
/* 471 */           if (!printRawData(arrayOfByte2, i)) {
/* 472 */             localBufferedInputStream.close();
/* 473 */             notifyEvent(103);
/* 474 */             throw new PrintException("Problem while spooling data");
/*     */           }
/*     */         }
/* 477 */         localBufferedInputStream.close();
/* 478 */         if (!endPrintRawData()) {
/* 479 */           notifyEvent(103);
/* 480 */           throw new PrintException("Print job failed to close properly.");
/*     */         }
/* 482 */         notifyEvent(106);
/*     */       } catch (IOException localIOException8) {
/* 484 */         notifyEvent(103);
/* 485 */         throw new PrintException(localIOException8.toString());
/*     */       } finally {
/* 487 */         notifyEvent(105);
/*     */       }
/*     */     } else {
/* 490 */       notifyEvent(103);
/* 491 */       throw new PrintException("unrecognized class: " + str);
/*     */     }
/* 493 */     this.service.wakeNotifier();
/*     */   }
/*     */ 
/*     */   public void printableJob(Printable paramPrintable) throws PrintException {
/*     */     try {
/* 498 */       synchronized (this) {
/* 499 */         if (this.job != null) {
/* 500 */           throw new PrintException("already printing");
/*     */         }
/* 502 */         this.job = new WPrinterJob();
/*     */       }
/*     */ 
/* 505 */       ??? = getPrintService();
/* 506 */       this.job.setPrintService((PrintService)???);
/* 507 */       if (this.copies == 0) {
/* 508 */         localObject2 = (Copies)((PrintService)???).getDefaultAttributeValue(Copies.class);
/* 509 */         this.copies = ((Copies)localObject2).getValue();
/*     */       }
/*     */ 
/* 512 */       if (this.mediaName == null) {
/* 513 */         localObject2 = ((PrintService)???).getDefaultAttributeValue(Media.class);
/* 514 */         if ((localObject2 instanceof MediaSizeName)) {
/* 515 */           this.mediaName = ((MediaSizeName)localObject2);
/* 516 */           this.mediaSize = MediaSize.getMediaSizeForName(this.mediaName);
/*     */         }
/*     */       }
/*     */ 
/* 520 */       if (this.orient == null) {
/* 521 */         this.orient = ((OrientationRequested)((PrintService)???).getDefaultAttributeValue(OrientationRequested.class));
/*     */       }
/*     */ 
/* 525 */       this.job.setCopies(this.copies);
/* 526 */       this.job.setJobName(this.jobName);
/* 527 */       Object localObject2 = new PageFormat();
/* 528 */       if (this.mediaSize != null) {
/* 529 */         Paper localPaper = new Paper();
/* 530 */         localPaper.setSize(this.mediaSize.getX(25400) * 72.0D, this.mediaSize.getY(25400) * 72.0D);
/*     */ 
/* 532 */         localPaper.setImageableArea(72.0D, 72.0D, localPaper.getWidth() - 144.0D, localPaper.getHeight() - 144.0D);
/*     */ 
/* 534 */         ((PageFormat)localObject2).setPaper(localPaper);
/*     */       }
/* 536 */       if (this.orient == OrientationRequested.REVERSE_LANDSCAPE)
/* 537 */         ((PageFormat)localObject2).setOrientation(2);
/* 538 */       else if (this.orient == OrientationRequested.LANDSCAPE) {
/* 539 */         ((PageFormat)localObject2).setOrientation(0);
/*     */       }
/* 541 */       this.job.setPrintable(paramPrintable, (PageFormat)localObject2);
/* 542 */       this.job.print(this.reqAttrSet);
/* 543 */       notifyEvent(106);
/*     */     }
/*     */     catch (PrinterException localPrinterException) {
/* 546 */       notifyEvent(103);
/* 547 */       throw new PrintException(localPrinterException);
/*     */     } finally {
/* 549 */       this.printReturned = true;
/* 550 */       notifyEvent(105);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void pageableJob(Pageable paramPageable) throws PrintException {
/*     */     try {
/* 556 */       synchronized (this) {
/* 557 */         if (this.job != null) {
/* 558 */           throw new PrintException("already printing");
/*     */         }
/* 560 */         this.job = new WPrinterJob();
/*     */       }
/*     */ 
/* 563 */       ??? = getPrintService();
/* 564 */       this.job.setPrintService((PrintService)???);
/* 565 */       if (this.copies == 0) {
/* 566 */         Copies localCopies = (Copies)((PrintService)???).getDefaultAttributeValue(Copies.class);
/* 567 */         this.copies = localCopies.getValue();
/*     */       }
/* 569 */       this.job.setCopies(this.copies);
/* 570 */       this.job.setJobName(this.jobName);
/* 571 */       this.job.setPageable(paramPageable);
/* 572 */       this.job.print(this.reqAttrSet);
/* 573 */       notifyEvent(106);
/*     */     }
/*     */     catch (PrinterException localPrinterException) {
/* 576 */       notifyEvent(103);
/* 577 */       throw new PrintException(localPrinterException);
/*     */     } finally {
/* 579 */       this.printReturned = true;
/* 580 */       notifyEvent(105);
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void initializeAttributeSets(Doc paramDoc, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */   {
/* 590 */     this.reqAttrSet = new HashPrintRequestAttributeSet();
/* 591 */     this.jobAttrSet = new HashPrintJobAttributeSet();
/*     */     Attribute[] arrayOfAttribute;
/* 594 */     if (paramPrintRequestAttributeSet != null) {
/* 595 */       this.reqAttrSet.addAll(paramPrintRequestAttributeSet);
/* 596 */       arrayOfAttribute = paramPrintRequestAttributeSet.toArray();
/* 597 */       for (int i = 0; i < arrayOfAttribute.length; i++) {
/* 598 */         if ((arrayOfAttribute[i] instanceof PrintJobAttribute)) {
/* 599 */           this.jobAttrSet.add(arrayOfAttribute[i]);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 604 */     DocAttributeSet localDocAttributeSet = paramDoc.getAttributes();
/* 605 */     if (localDocAttributeSet != null) {
/* 606 */       arrayOfAttribute = localDocAttributeSet.toArray();
/* 607 */       for (int j = 0; j < arrayOfAttribute.length; j++) {
/* 608 */         if ((arrayOfAttribute[j] instanceof PrintRequestAttribute)) {
/* 609 */           this.reqAttrSet.add(arrayOfAttribute[j]);
/*     */         }
/* 611 */         if ((arrayOfAttribute[j] instanceof PrintJobAttribute)) {
/* 612 */           this.jobAttrSet.add(arrayOfAttribute[j]);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 618 */     String str = "";
/*     */     try {
/* 620 */       str = System.getProperty("user.name");
/*     */     }
/*     */     catch (SecurityException localSecurityException)
/*     */     {
/*     */     }
/*     */     Object localObject1;
/* 624 */     if ((str == null) || (str.equals(""))) {
/* 625 */       localObject1 = (RequestingUserName)paramPrintRequestAttributeSet.get(RequestingUserName.class);
/*     */ 
/* 627 */       if (localObject1 != null) {
/* 628 */         this.jobAttrSet.add(new JobOriginatingUserName(((RequestingUserName)localObject1).getValue(), ((RequestingUserName)localObject1).getLocale()));
/*     */       }
/*     */       else
/*     */       {
/* 632 */         this.jobAttrSet.add(new JobOriginatingUserName("", null));
/*     */       }
/*     */     } else {
/* 635 */       this.jobAttrSet.add(new JobOriginatingUserName(str, null));
/*     */     }
/*     */ 
/* 640 */     if (this.jobAttrSet.get(JobName.class) == null)
/*     */     {
/*     */       Object localObject2;
/* 642 */       if ((localDocAttributeSet != null) && (localDocAttributeSet.get(DocumentName.class) != null)) {
/* 643 */         localObject2 = (DocumentName)localDocAttributeSet.get(DocumentName.class);
/*     */ 
/* 645 */         localObject1 = new JobName(((DocumentName)localObject2).getValue(), ((DocumentName)localObject2).getLocale());
/* 646 */         this.jobAttrSet.add((Attribute)localObject1);
/*     */       } else {
/* 648 */         localObject2 = "JPS Job:" + paramDoc;
/*     */         try {
/* 650 */           Object localObject3 = paramDoc.getPrintData();
/* 651 */           if ((localObject3 instanceof URL))
/* 652 */             localObject2 = ((URL)paramDoc.getPrintData()).toString();
/*     */         }
/*     */         catch (IOException localIOException) {
/*     */         }
/* 656 */         localObject1 = new JobName((String)localObject2, null);
/* 657 */         this.jobAttrSet.add((Attribute)localObject1);
/*     */       }
/*     */     }
/*     */ 
/* 661 */     this.jobAttrSet = AttributeSetUtilities.unmodifiableView(this.jobAttrSet);
/*     */   }
/*     */ 
/*     */   private void getAttributeValues(DocFlavor paramDocFlavor) throws PrintException
/*     */   {
/* 666 */     if (this.reqAttrSet.get(Fidelity.class) == Fidelity.FIDELITY_TRUE)
/* 667 */       this.fidelity = true;
/*     */     else {
/* 669 */       this.fidelity = false;
/*     */     }
/*     */ 
/* 673 */     Attribute[] arrayOfAttribute = this.reqAttrSet.toArray();
/* 674 */     for (int i = 0; i < arrayOfAttribute.length; i++) {
/* 675 */       Attribute localAttribute = arrayOfAttribute[i];
/* 676 */       Class localClass = localAttribute.getCategory();
/* 677 */       if (this.fidelity == true) {
/* 678 */         if (!this.service.isAttributeCategorySupported(localClass)) {
/* 679 */           notifyEvent(103);
/* 680 */           throw new PrintJobAttributeException("unsupported category: " + localClass, localClass, null);
/*     */         }
/* 682 */         if (!this.service.isAttributeValueSupported(localAttribute, paramDocFlavor, null))
/*     */         {
/* 684 */           notifyEvent(103);
/* 685 */           throw new PrintJobAttributeException("unsupported attribute: " + localAttribute, null, localAttribute);
/*     */         }
/*     */       }
/*     */ 
/* 689 */       if (localClass == Destination.class) {
/* 690 */         URI localURI = ((Destination)localAttribute).getURI();
/* 691 */         if (!"file".equals(localURI.getScheme())) {
/* 692 */           notifyEvent(103);
/* 693 */           throw new PrintException("Not a file: URI");
/*     */         }
/*     */         try {
/* 696 */           this.mDestination = new File(localURI).getPath();
/*     */         } catch (Exception localException) {
/* 698 */           throw new PrintException(localException);
/*     */         }
/*     */ 
/* 701 */         SecurityManager localSecurityManager = System.getSecurityManager();
/* 702 */         if (localSecurityManager != null) {
/*     */           try {
/* 704 */             localSecurityManager.checkWrite(this.mDestination);
/*     */           } catch (SecurityException localSecurityException) {
/* 706 */             notifyEvent(103);
/* 707 */             throw new PrintException(localSecurityException);
/*     */           }
/*     */         }
/*     */       }
/* 711 */       else if (localClass == JobName.class) {
/* 712 */         this.jobName = ((JobName)localAttribute).getValue();
/* 713 */       } else if (localClass == Copies.class) {
/* 714 */         this.copies = ((Copies)localAttribute).getValue();
/* 715 */       } else if (localClass == Media.class) {
/* 716 */         if ((localAttribute instanceof MediaSizeName)) {
/* 717 */           this.mediaName = ((MediaSizeName)localAttribute);
/*     */ 
/* 721 */           if (!this.service.isAttributeValueSupported(localAttribute, null, null))
/* 722 */             this.mediaSize = MediaSize.getMediaSizeForName(this.mediaName);
/*     */         }
/*     */       }
/* 725 */       else if (localClass == OrientationRequested.class) {
/* 726 */         this.orient = ((OrientationRequested)localAttribute);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private native boolean startPrintRawData(String paramString1, String paramString2);
/*     */ 
/*     */   private native boolean printRawData(byte[] paramArrayOfByte, int paramInt);
/*     */ 
/*     */   private native boolean endPrintRawData();
/*     */ 
/*     */   public void cancel() throws PrintException {
/* 738 */     synchronized (this) {
/* 739 */       if (!this.printing)
/* 740 */         throw new PrintException("Job is not yet submitted.");
/* 741 */       if ((this.job != null) && (!this.printReturned)) {
/* 742 */         this.job.cancel();
/* 743 */         notifyEvent(101);
/* 744 */         return;
/*     */       }
/* 746 */       throw new PrintException("Job could not be cancelled.");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.Win32PrintJob
 * JD-Core Version:    0.6.2
 */