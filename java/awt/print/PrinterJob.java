/*     */ package java.awt.print;
/*     */ 
/*     */ import java.awt.AWTError;
/*     */ import java.awt.HeadlessException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.print.DocFlavor.SERVICE_FORMATTED;
/*     */ import javax.print.PrintService;
/*     */ import javax.print.PrintServiceLookup;
/*     */ import javax.print.StreamPrintServiceFactory;
/*     */ import javax.print.attribute.PrintRequestAttributeSet;
/*     */ import javax.print.attribute.standard.Media;
/*     */ import javax.print.attribute.standard.MediaPrintableArea;
/*     */ import javax.print.attribute.standard.MediaSize;
/*     */ import javax.print.attribute.standard.MediaSizeName;
/*     */ import javax.print.attribute.standard.OrientationRequested;
/*     */ 
/*     */ public abstract class PrinterJob
/*     */ {
/*     */   public static PrinterJob getPrinterJob()
/*     */   {
/*  73 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  74 */     if (localSecurityManager != null) {
/*  75 */       localSecurityManager.checkPrintJobAccess();
/*     */     }
/*  77 */     return (PrinterJob)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/*  80 */         String str = System.getProperty("java.awt.printerjob", null);
/*     */         try {
/*  82 */           return (PrinterJob)Class.forName(str).newInstance();
/*     */         } catch (ClassNotFoundException localClassNotFoundException) {
/*  84 */           throw new AWTError("PrinterJob not found: " + str);
/*     */         } catch (InstantiationException localInstantiationException) {
/*  86 */           throw new AWTError("Could not instantiate PrinterJob: " + str); } catch (IllegalAccessException localIllegalAccessException) {
/*     */         }
/*  88 */         throw new AWTError("Could not access PrinterJob: " + str);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static PrintService[] lookupPrintServices()
/*     */   {
/* 107 */     return PrintServiceLookup.lookupPrintServices(DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
/*     */   }
/*     */ 
/*     */   public static StreamPrintServiceFactory[] lookupStreamPrintServices(String paramString)
/*     */   {
/* 148 */     return StreamPrintServiceFactory.lookupStreamPrintServiceFactories(DocFlavor.SERVICE_FORMATTED.PAGEABLE, paramString);
/*     */   }
/*     */ 
/*     */   public PrintService getPrintService()
/*     */   {
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   public void setPrintService(PrintService paramPrintService)
/*     */     throws PrinterException
/*     */   {
/* 195 */     throw new PrinterException("Setting a service is not supported on this class");
/*     */   }
/*     */ 
/*     */   public abstract void setPrintable(Printable paramPrintable);
/*     */ 
/*     */   public abstract void setPrintable(Printable paramPrintable, PageFormat paramPageFormat);
/*     */ 
/*     */   public abstract void setPageable(Pageable paramPageable)
/*     */     throws NullPointerException;
/*     */ 
/*     */   public abstract boolean printDialog()
/*     */     throws HeadlessException;
/*     */ 
/*     */   public boolean printDialog(PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */     throws HeadlessException
/*     */   {
/* 308 */     if (paramPrintRequestAttributeSet == null) {
/* 309 */       throw new NullPointerException("attributes");
/*     */     }
/* 311 */     return printDialog();
/*     */   }
/*     */ 
/*     */   public abstract PageFormat pageDialog(PageFormat paramPageFormat)
/*     */     throws HeadlessException;
/*     */ 
/*     */   public PageFormat pageDialog(PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */     throws HeadlessException
/*     */   {
/* 371 */     if (paramPrintRequestAttributeSet == null) {
/* 372 */       throw new NullPointerException("attributes");
/*     */     }
/* 374 */     return pageDialog(defaultPage());
/*     */   }
/*     */ 
/*     */   public abstract PageFormat defaultPage(PageFormat paramPageFormat);
/*     */ 
/*     */   public PageFormat defaultPage()
/*     */   {
/* 393 */     return defaultPage(new PageFormat());
/*     */   }
/*     */ 
/*     */   public PageFormat getPageFormat(PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */   {
/* 415 */     PrintService localPrintService = getPrintService();
/* 416 */     PageFormat localPageFormat = defaultPage();
/*     */ 
/* 418 */     if ((localPrintService == null) || (paramPrintRequestAttributeSet == null)) {
/* 419 */       return localPageFormat;
/*     */     }
/*     */ 
/* 422 */     Media localMedia = (Media)paramPrintRequestAttributeSet.get(Media.class);
/* 423 */     MediaPrintableArea localMediaPrintableArea = (MediaPrintableArea)paramPrintRequestAttributeSet.get(MediaPrintableArea.class);
/*     */ 
/* 425 */     OrientationRequested localOrientationRequested = (OrientationRequested)paramPrintRequestAttributeSet.get(OrientationRequested.class);
/*     */ 
/* 428 */     if ((localMedia == null) && (localMediaPrintableArea == null) && (localOrientationRequested == null)) {
/* 429 */       return localPageFormat;
/*     */     }
/* 431 */     Paper localPaper = localPageFormat.getPaper();
/*     */     Object localObject;
/* 436 */     if ((localMediaPrintableArea == null) && (localMedia != null) && (localPrintService.isAttributeCategorySupported(MediaPrintableArea.class)))
/*     */     {
/* 438 */       localObject = localPrintService.getSupportedAttributeValues(MediaPrintableArea.class, null, paramPrintRequestAttributeSet);
/*     */ 
/* 441 */       if (((localObject instanceof MediaPrintableArea[])) && (((MediaPrintableArea[])localObject).length > 0))
/*     */       {
/* 443 */         localMediaPrintableArea = ((MediaPrintableArea[])(MediaPrintableArea[])localObject)[0];
/*     */       }
/*     */     }
/*     */ 
/* 447 */     if ((localMedia != null) && (localPrintService.isAttributeValueSupported(localMedia, null, paramPrintRequestAttributeSet)))
/*     */     {
/* 449 */       if ((localMedia instanceof MediaSizeName)) {
/* 450 */         localObject = (MediaSizeName)localMedia;
/* 451 */         MediaSize localMediaSize = MediaSize.getMediaSizeForName((MediaSizeName)localObject);
/* 452 */         if (localMediaSize != null) {
/* 453 */           double d1 = 72.0D;
/* 454 */           double d2 = localMediaSize.getX(25400) * d1;
/* 455 */           double d3 = localMediaSize.getY(25400) * d1;
/* 456 */           localPaper.setSize(d2, d3);
/* 457 */           if (localMediaPrintableArea == null) {
/* 458 */             localPaper.setImageableArea(d1, d1, d2 - 2.0D * d1, d3 - 2.0D * d1);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 466 */     if ((localMediaPrintableArea != null) && (localPrintService.isAttributeValueSupported(localMediaPrintableArea, null, paramPrintRequestAttributeSet)))
/*     */     {
/* 468 */       localObject = localMediaPrintableArea.getPrintableArea(25400);
/*     */ 
/* 470 */       for (int j = 0; j < localObject.length; j++) {
/* 471 */         localObject[j] *= 72.0F;
/*     */       }
/* 473 */       localPaper.setImageableArea(localObject[0], localObject[1], localObject[2], localObject[3]);
/*     */     }
/*     */ 
/* 477 */     if ((localOrientationRequested != null) && (localPrintService.isAttributeValueSupported(localOrientationRequested, null, paramPrintRequestAttributeSet)))
/*     */     {
/*     */       int i;
/* 480 */       if (localOrientationRequested.equals(OrientationRequested.REVERSE_LANDSCAPE))
/* 481 */         i = 2;
/* 482 */       else if (localOrientationRequested.equals(OrientationRequested.LANDSCAPE))
/* 483 */         i = 0;
/*     */       else {
/* 485 */         i = 1;
/*     */       }
/* 487 */       localPageFormat.setOrientation(i);
/*     */     }
/*     */ 
/* 490 */     localPageFormat.setPaper(localPaper);
/* 491 */     localPageFormat = validatePage(localPageFormat);
/* 492 */     return localPageFormat;
/*     */   }
/*     */ 
/*     */   public abstract PageFormat validatePage(PageFormat paramPageFormat);
/*     */ 
/*     */   public abstract void print()
/*     */     throws PrinterException;
/*     */ 
/*     */   public void print(PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */     throws PrinterException
/*     */   {
/* 561 */     print();
/*     */   }
/*     */ 
/*     */   public abstract void setCopies(int paramInt);
/*     */ 
/*     */   public abstract int getCopies();
/*     */ 
/*     */   public abstract String getUserName();
/*     */ 
/*     */   public abstract void setJobName(String paramString);
/*     */ 
/*     */   public abstract String getJobName();
/*     */ 
/*     */   public abstract void cancel();
/*     */ 
/*     */   public abstract boolean isCancelled();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.print.PrinterJob
 * JD-Core Version:    0.6.2
 */