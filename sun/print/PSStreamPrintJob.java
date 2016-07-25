/*     */ package sun.print;
/*     */ 
/*     */ import java.awt.print.PageFormat;
/*     */ import java.awt.print.Pageable;
/*     */ import java.awt.print.Paper;
/*     */ import java.awt.print.Printable;
/*     */ import java.awt.print.PrinterException;
/*     */ import java.awt.print.PrinterJob;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
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
/*     */ import javax.print.attribute.standard.DocumentName;
/*     */ import javax.print.attribute.standard.Fidelity;
/*     */ import javax.print.attribute.standard.JobName;
/*     */ import javax.print.attribute.standard.JobOriginatingUserName;
/*     */ import javax.print.attribute.standard.Media;
/*     */ import javax.print.attribute.standard.MediaSize;
/*     */ import javax.print.attribute.standard.MediaSize.NA;
/*     */ import javax.print.attribute.standard.MediaSizeName;
/*     */ import javax.print.attribute.standard.OrientationRequested;
/*     */ import javax.print.attribute.standard.RequestingUserName;
/*     */ import javax.print.event.PrintJobAttributeListener;
/*     */ import javax.print.event.PrintJobEvent;
/*     */ import javax.print.event.PrintJobListener;
/*     */ 
/*     */ public class PSStreamPrintJob
/*     */   implements CancelablePrintJob
/*     */ {
/*     */   private transient Vector jobListeners;
/*     */   private transient Vector attrListeners;
/*     */   private transient Vector listenedAttributeSets;
/*     */   private PSStreamPrintService service;
/*     */   private boolean fidelity;
/*  75 */   private boolean printing = false;
/*  76 */   private boolean printReturned = false;
/*  77 */   private PrintRequestAttributeSet reqAttrSet = null;
/*  78 */   private PrintJobAttributeSet jobAttrSet = null;
/*     */   private PrinterJob job;
/*     */   private Doc doc;
/*  85 */   private InputStream instream = null;
/*  86 */   private Reader reader = null;
/*     */ 
/*  89 */   private String jobName = "Java Printing";
/*  90 */   private int copies = 1;
/*  91 */   private MediaSize mediaSize = MediaSize.NA.LETTER;
/*  92 */   private OrientationRequested orient = OrientationRequested.PORTRAIT;
/*     */ 
/*     */   PSStreamPrintJob(PSStreamPrintService paramPSStreamPrintService) {
/*  95 */     this.service = paramPSStreamPrintService;
/*     */   }
/*     */ 
/*     */   public PrintService getPrintService() {
/*  99 */     return this.service;
/*     */   }
/*     */ 
/*     */   public PrintJobAttributeSet getAttributes() {
/* 103 */     synchronized (this) {
/* 104 */       if (this.jobAttrSet == null)
/*     */       {
/* 106 */         HashPrintJobAttributeSet localHashPrintJobAttributeSet = new HashPrintJobAttributeSet();
/* 107 */         return AttributeSetUtilities.unmodifiableView(localHashPrintJobAttributeSet);
/*     */       }
/* 109 */       return this.jobAttrSet;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addPrintJobListener(PrintJobListener paramPrintJobListener)
/*     */   {
/* 115 */     synchronized (this) {
/* 116 */       if (paramPrintJobListener == null) {
/* 117 */         return;
/*     */       }
/* 119 */       if (this.jobListeners == null) {
/* 120 */         this.jobListeners = new Vector();
/*     */       }
/* 122 */       this.jobListeners.add(paramPrintJobListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removePrintJobListener(PrintJobListener paramPrintJobListener) {
/* 127 */     synchronized (this) {
/* 128 */       if ((paramPrintJobListener == null) || (this.jobListeners == null)) {
/* 129 */         return;
/*     */       }
/* 131 */       this.jobListeners.remove(paramPrintJobListener);
/* 132 */       if (this.jobListeners.isEmpty())
/* 133 */         this.jobListeners = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void closeDataStreams()
/*     */   {
/* 146 */     if (this.doc == null) {
/* 147 */       return;
/*     */     }
/*     */ 
/* 150 */     Object localObject1 = null;
/*     */     try
/*     */     {
/* 153 */       localObject1 = this.doc.getPrintData();
/*     */     } catch (IOException localIOException1) {
/* 155 */       return;
/*     */     }
/*     */ 
/* 158 */     if (this.instream != null) {
/*     */       try {
/* 160 */         this.instream.close();
/*     */       } catch (IOException localIOException2) {
/*     */       } finally {
/* 163 */         this.instream = null;
/*     */       }
/*     */     }
/* 166 */     else if (this.reader != null) {
/*     */       try {
/* 168 */         this.reader.close();
/*     */       } catch (IOException localIOException3) {
/*     */       } finally {
/* 171 */         this.reader = null;
/*     */       }
/*     */     }
/* 174 */     else if ((localObject1 instanceof InputStream))
/*     */       try {
/* 176 */         ((InputStream)localObject1).close();
/*     */       }
/*     */       catch (IOException localIOException4) {
/*     */       }
/* 180 */     else if ((localObject1 instanceof Reader))
/*     */       try {
/* 182 */         ((Reader)localObject1).close();
/*     */       }
/*     */       catch (IOException localIOException5) {
/*     */       }
/*     */   }
/*     */ 
/*     */   private void notifyEvent(int paramInt) {
/* 189 */     synchronized (this) {
/* 190 */       if (this.jobListeners != null)
/*     */       {
/* 192 */         PrintJobEvent localPrintJobEvent = new PrintJobEvent(this, paramInt);
/* 193 */         for (int i = 0; i < this.jobListeners.size(); i++) {
/* 194 */           PrintJobListener localPrintJobListener = (PrintJobListener)this.jobListeners.elementAt(i);
/* 195 */           switch (paramInt)
/*     */           {
/*     */           case 101:
/* 198 */             localPrintJobListener.printJobCanceled(localPrintJobEvent);
/* 199 */             break;
/*     */           case 103:
/* 202 */             localPrintJobListener.printJobFailed(localPrintJobEvent);
/* 203 */             break;
/*     */           case 106:
/* 206 */             localPrintJobListener.printDataTransferCompleted(localPrintJobEvent);
/* 207 */             break;
/*     */           case 105:
/* 210 */             localPrintJobListener.printJobNoMoreEvents(localPrintJobEvent);
/* 211 */             break;
/*     */           case 102:
/* 214 */             localPrintJobListener.printJobCompleted(localPrintJobEvent);
/*     */           case 104:
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addPrintJobAttributeListener(PrintJobAttributeListener paramPrintJobAttributeListener, PrintJobAttributeSet paramPrintJobAttributeSet)
/*     */   {
/* 228 */     synchronized (this) {
/* 229 */       if (paramPrintJobAttributeListener == null) {
/* 230 */         return;
/*     */       }
/* 232 */       if (this.attrListeners == null) {
/* 233 */         this.attrListeners = new Vector();
/* 234 */         this.listenedAttributeSets = new Vector();
/*     */       }
/* 236 */       this.attrListeners.add(paramPrintJobAttributeListener);
/* 237 */       if (paramPrintJobAttributeSet == null) {
/* 238 */         paramPrintJobAttributeSet = new HashPrintJobAttributeSet();
/*     */       }
/* 240 */       this.listenedAttributeSets.add(paramPrintJobAttributeSet);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removePrintJobAttributeListener(PrintJobAttributeListener paramPrintJobAttributeListener)
/*     */   {
/* 246 */     synchronized (this) {
/* 247 */       if ((paramPrintJobAttributeListener == null) || (this.attrListeners == null)) {
/* 248 */         return;
/*     */       }
/* 250 */       int i = this.attrListeners.indexOf(paramPrintJobAttributeListener);
/* 251 */       if (i == -1) {
/* 252 */         return;
/*     */       }
/* 254 */       this.attrListeners.remove(i);
/* 255 */       this.listenedAttributeSets.remove(i);
/* 256 */       if (this.attrListeners.isEmpty()) {
/* 257 */         this.attrListeners = null;
/* 258 */         this.listenedAttributeSets = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void print(Doc paramDoc, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */     throws PrintException
/*     */   {
/* 267 */     synchronized (this) {
/* 268 */       if (this.printing) {
/* 269 */         throw new PrintException("already printing");
/*     */       }
/* 271 */       this.printing = true;
/*     */     }
/*     */ 
/* 275 */     this.doc = paramDoc;
/*     */ 
/* 277 */     ??? = paramDoc.getDocFlavor();
/*     */     Object localObject2;
/*     */     try {
/* 281 */       localObject2 = paramDoc.getPrintData();
/*     */     } catch (IOException localIOException1) {
/* 283 */       notifyEvent(103);
/* 284 */       throw new PrintException("can't get print data: " + localIOException1.toString());
/*     */     }
/*     */ 
/* 287 */     if ((??? == null) || (!this.service.isDocFlavorSupported((DocFlavor)???))) {
/* 288 */       notifyEvent(103);
/* 289 */       throw new PrintJobFlavorException("invalid flavor", (DocFlavor)???);
/*     */     }
/*     */ 
/* 292 */     initializeAttributeSets(paramDoc, paramPrintRequestAttributeSet);
/*     */ 
/* 294 */     getAttributeValues((DocFlavor)???);
/*     */ 
/* 296 */     String str = ((DocFlavor)???).getRepresentationClassName();
/* 297 */     if ((((DocFlavor)???).equals(DocFlavor.INPUT_STREAM.GIF)) || (((DocFlavor)???).equals(DocFlavor.INPUT_STREAM.JPEG)) || (((DocFlavor)???).equals(DocFlavor.INPUT_STREAM.PNG)) || (((DocFlavor)???).equals(DocFlavor.BYTE_ARRAY.GIF)) || (((DocFlavor)???).equals(DocFlavor.BYTE_ARRAY.JPEG)) || (((DocFlavor)???).equals(DocFlavor.BYTE_ARRAY.PNG)))
/*     */     {
/*     */       try
/*     */       {
/* 304 */         this.instream = paramDoc.getStreamForBytes();
/* 305 */         printableJob(new ImagePrinter(this.instream), this.reqAttrSet);
/* 306 */         return;
/*     */       } catch (ClassCastException localClassCastException1) {
/* 308 */         notifyEvent(103);
/* 309 */         throw new PrintException(localClassCastException1);
/*     */       } catch (IOException localIOException2) {
/* 311 */         notifyEvent(103);
/* 312 */         throw new PrintException(localIOException2);
/*     */       }
/*     */     }
/* 314 */     if ((((DocFlavor)???).equals(DocFlavor.URL.GIF)) || (((DocFlavor)???).equals(DocFlavor.URL.JPEG)) || (((DocFlavor)???).equals(DocFlavor.URL.PNG)))
/*     */     {
/*     */       try
/*     */       {
/* 318 */         printableJob(new ImagePrinter((URL)localObject2), this.reqAttrSet);
/* 319 */         return;
/*     */       } catch (ClassCastException localClassCastException2) {
/* 321 */         notifyEvent(103);
/* 322 */         throw new PrintException(localClassCastException2);
/*     */       }
/*     */     }
/* 324 */     if (str.equals("java.awt.print.Pageable"))
/*     */       try {
/* 326 */         pageableJob((Pageable)paramDoc.getPrintData(), this.reqAttrSet);
/* 327 */         return;
/*     */       } catch (ClassCastException localClassCastException3) {
/* 329 */         notifyEvent(103);
/* 330 */         throw new PrintException(localClassCastException3);
/*     */       } catch (IOException localIOException3) {
/* 332 */         notifyEvent(103);
/* 333 */         throw new PrintException(localIOException3);
/*     */       }
/* 335 */     if (str.equals("java.awt.print.Printable")) {
/*     */       try {
/* 337 */         printableJob((Printable)paramDoc.getPrintData(), this.reqAttrSet);
/* 338 */         return;
/*     */       } catch (ClassCastException localClassCastException4) {
/* 340 */         notifyEvent(103);
/* 341 */         throw new PrintException(localClassCastException4);
/*     */       } catch (IOException localIOException4) {
/* 343 */         notifyEvent(103);
/* 344 */         throw new PrintException(localIOException4);
/*     */       }
/*     */     }
/* 347 */     notifyEvent(103);
/* 348 */     throw new PrintException("unrecognized class: " + str);
/*     */   }
/*     */ 
/*     */   public void printableJob(Printable paramPrintable, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */     throws PrintException
/*     */   {
/*     */     try
/*     */     {
/* 356 */       synchronized (this) {
/* 357 */         if (this.job != null) {
/* 358 */           throw new PrintException("already printing");
/*     */         }
/* 360 */         this.job = new PSPrinterJob();
/*     */       }
/*     */ 
/* 363 */       this.job.setPrintService(getPrintService());
/* 364 */       ??? = new PageFormat();
/* 365 */       if (this.mediaSize != null) {
/* 366 */         Paper localPaper = new Paper();
/* 367 */         localPaper.setSize(this.mediaSize.getX(25400) * 72.0D, this.mediaSize.getY(25400) * 72.0D);
/*     */ 
/* 369 */         localPaper.setImageableArea(72.0D, 72.0D, localPaper.getWidth() - 144.0D, localPaper.getHeight() - 144.0D);
/*     */ 
/* 371 */         ((PageFormat)???).setPaper(localPaper);
/*     */       }
/* 373 */       if (this.orient == OrientationRequested.REVERSE_LANDSCAPE)
/* 374 */         ((PageFormat)???).setOrientation(2);
/* 375 */       else if (this.orient == OrientationRequested.LANDSCAPE) {
/* 376 */         ((PageFormat)???).setOrientation(0);
/*     */       }
/* 378 */       this.job.setPrintable(paramPrintable, (PageFormat)???);
/* 379 */       this.job.print(paramPrintRequestAttributeSet);
/* 380 */       notifyEvent(102);
/*     */     }
/*     */     catch (PrinterException localPrinterException) {
/* 383 */       notifyEvent(103);
/* 384 */       throw new PrintException(localPrinterException);
/*     */     } finally {
/* 386 */       this.printReturned = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void pageableJob(Pageable paramPageable, PrintRequestAttributeSet paramPrintRequestAttributeSet) throws PrintException
/*     */   {
/*     */     try
/*     */     {
/* 394 */       synchronized (this) {
/* 395 */         if (this.job != null) {
/* 396 */           throw new PrintException("already printing");
/*     */         }
/* 398 */         this.job = new PSPrinterJob();
/*     */       }
/*     */ 
/* 401 */       this.job.setPrintService(getPrintService());
/* 402 */       this.job.setPageable(paramPageable);
/* 403 */       this.job.print(paramPrintRequestAttributeSet);
/* 404 */       notifyEvent(102);
/*     */     }
/*     */     catch (PrinterException localPrinterException) {
/* 407 */       notifyEvent(103);
/* 408 */       throw new PrintException(localPrinterException);
/*     */     } finally {
/* 410 */       this.printReturned = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void initializeAttributeSets(Doc paramDoc, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */   {
/* 420 */     this.reqAttrSet = new HashPrintRequestAttributeSet();
/* 421 */     this.jobAttrSet = new HashPrintJobAttributeSet();
/*     */     Attribute[] arrayOfAttribute;
/* 424 */     if (paramPrintRequestAttributeSet != null) {
/* 425 */       this.reqAttrSet.addAll(paramPrintRequestAttributeSet);
/* 426 */       arrayOfAttribute = paramPrintRequestAttributeSet.toArray();
/* 427 */       for (int i = 0; i < arrayOfAttribute.length; i++) {
/* 428 */         if ((arrayOfAttribute[i] instanceof PrintJobAttribute)) {
/* 429 */           this.jobAttrSet.add(arrayOfAttribute[i]);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 434 */     DocAttributeSet localDocAttributeSet = paramDoc.getAttributes();
/* 435 */     if (localDocAttributeSet != null) {
/* 436 */       arrayOfAttribute = localDocAttributeSet.toArray();
/* 437 */       for (int j = 0; j < arrayOfAttribute.length; j++) {
/* 438 */         if ((arrayOfAttribute[j] instanceof PrintRequestAttribute)) {
/* 439 */           this.reqAttrSet.add(arrayOfAttribute[j]);
/*     */         }
/* 441 */         if ((arrayOfAttribute[j] instanceof PrintJobAttribute)) {
/* 442 */           this.jobAttrSet.add(arrayOfAttribute[j]);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 448 */     String str = "";
/*     */     try {
/* 450 */       str = System.getProperty("user.name");
/*     */     }
/*     */     catch (SecurityException localSecurityException)
/*     */     {
/*     */     }
/*     */     Object localObject1;
/* 454 */     if ((str == null) || (str.equals(""))) {
/* 455 */       localObject1 = (RequestingUserName)paramPrintRequestAttributeSet.get(RequestingUserName.class);
/*     */ 
/* 457 */       if (localObject1 != null) {
/* 458 */         this.jobAttrSet.add(new JobOriginatingUserName(((RequestingUserName)localObject1).getValue(), ((RequestingUserName)localObject1).getLocale()));
/*     */       }
/*     */       else
/*     */       {
/* 462 */         this.jobAttrSet.add(new JobOriginatingUserName("", null));
/*     */       }
/*     */     } else {
/* 465 */       this.jobAttrSet.add(new JobOriginatingUserName(str, null));
/*     */     }
/*     */ 
/* 470 */     if (this.jobAttrSet.get(JobName.class) == null)
/*     */     {
/*     */       Object localObject2;
/* 472 */       if ((localDocAttributeSet != null) && (localDocAttributeSet.get(DocumentName.class) != null)) {
/* 473 */         localObject2 = (DocumentName)localDocAttributeSet.get(DocumentName.class);
/*     */ 
/* 475 */         localObject1 = new JobName(((DocumentName)localObject2).getValue(), ((DocumentName)localObject2).getLocale());
/* 476 */         this.jobAttrSet.add((Attribute)localObject1);
/*     */       } else {
/* 478 */         localObject2 = "JPS Job:" + paramDoc;
/*     */         try {
/* 480 */           Object localObject3 = paramDoc.getPrintData();
/* 481 */           if ((localObject3 instanceof URL))
/* 482 */             localObject2 = ((URL)paramDoc.getPrintData()).toString();
/*     */         }
/*     */         catch (IOException localIOException) {
/*     */         }
/* 486 */         localObject1 = new JobName((String)localObject2, null);
/* 487 */         this.jobAttrSet.add((Attribute)localObject1);
/*     */       }
/*     */     }
/*     */ 
/* 491 */     this.jobAttrSet = AttributeSetUtilities.unmodifiableView(this.jobAttrSet);
/*     */   }
/*     */ 
/*     */   private void getAttributeValues(DocFlavor paramDocFlavor)
/*     */     throws PrintException
/*     */   {
/* 499 */     if (this.reqAttrSet.get(Fidelity.class) == Fidelity.FIDELITY_TRUE)
/* 500 */       this.fidelity = true;
/*     */     else {
/* 502 */       this.fidelity = false;
/*     */     }
/*     */ 
/* 505 */     Attribute[] arrayOfAttribute = this.reqAttrSet.toArray();
/* 506 */     for (int i = 0; i < arrayOfAttribute.length; i++) {
/* 507 */       Attribute localAttribute = arrayOfAttribute[i];
/* 508 */       Class localClass = localAttribute.getCategory();
/* 509 */       if (this.fidelity == true) {
/* 510 */         if (!this.service.isAttributeCategorySupported(localClass)) {
/* 511 */           notifyEvent(103);
/* 512 */           throw new PrintJobAttributeException("unsupported category: " + localClass, localClass, null);
/*     */         }
/* 514 */         if (!this.service.isAttributeValueSupported(localAttribute, paramDocFlavor, null))
/*     */         {
/* 516 */           notifyEvent(103);
/* 517 */           throw new PrintJobAttributeException("unsupported attribute: " + localAttribute, null, localAttribute);
/*     */         }
/*     */       }
/*     */ 
/* 521 */       if (localClass == JobName.class)
/* 522 */         this.jobName = ((JobName)localAttribute).getValue();
/* 523 */       else if (localClass == Copies.class)
/* 524 */         this.copies = ((Copies)localAttribute).getValue();
/* 525 */       else if (localClass == Media.class) {
/* 526 */         if (((localAttribute instanceof MediaSizeName)) && (this.service.isAttributeValueSupported(localAttribute, null, null)))
/*     */         {
/* 528 */           this.mediaSize = MediaSize.getMediaSizeForName((MediaSizeName)localAttribute);
/*     */         }
/*     */       }
/* 531 */       else if (localClass == OrientationRequested.class)
/* 532 */         this.orient = ((OrientationRequested)localAttribute);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void cancel()
/*     */     throws PrintException
/*     */   {
/* 539 */     synchronized (this) {
/* 540 */       if (!this.printing)
/* 541 */         throw new PrintException("Job is not yet submitted.");
/* 542 */       if ((this.job != null) && (!this.printReturned)) {
/* 543 */         this.job.cancel();
/* 544 */         notifyEvent(101);
/* 545 */         return;
/*     */       }
/* 547 */       throw new PrintException("Job could not be cancelled.");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.PSStreamPrintJob
 * JD-Core Version:    0.6.2
 */