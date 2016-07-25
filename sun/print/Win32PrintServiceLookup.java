/*     */ package sun.print;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import javax.print.DocFlavor;
/*     */ import javax.print.MultiDocPrintService;
/*     */ import javax.print.PrintService;
/*     */ import javax.print.PrintServiceLookup;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.AttributeSet;
/*     */ import javax.print.attribute.HashPrintRequestAttributeSet;
/*     */ import javax.print.attribute.HashPrintServiceAttributeSet;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ import javax.print.attribute.PrintRequestAttributeSet;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ import javax.print.attribute.PrintServiceAttributeSet;
/*     */ import javax.print.attribute.standard.PrinterName;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ public class Win32PrintServiceLookup extends PrintServiceLookup
/*     */ {
/*     */   private String defaultPrinter;
/*     */   private PrintService defaultPrintService;
/*     */   private String[] printers;
/*     */   private PrintService[] printServices;
/*     */   private static Win32PrintServiceLookup win32PrintLUS;
/*     */ 
/*     */   public static Win32PrintServiceLookup getWin32PrintLUS()
/*     */   {
/*  72 */     if (win32PrintLUS == null)
/*     */     {
/*  77 */       PrintServiceLookup.lookupDefaultPrintService();
/*     */     }
/*  79 */     return win32PrintLUS;
/*     */   }
/*     */ 
/*     */   public Win32PrintServiceLookup()
/*     */   {
/*  84 */     if (win32PrintLUS == null) {
/*  85 */       win32PrintLUS = this;
/*     */ 
/*  87 */       String str = (String)AccessController.doPrivileged(new GetPropertyAction("os.name"));
/*     */ 
/*  91 */       if ((str != null) && (str.startsWith("Windows 98"))) {
/*  92 */         return;
/*     */       }
/*     */ 
/*  95 */       PrinterChangeListener localPrinterChangeListener = new PrinterChangeListener();
/*  96 */       localPrinterChangeListener.setDaemon(true);
/*  97 */       localPrinterChangeListener.start();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized PrintService[] getPrintServices()
/*     */   {
/* 107 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 108 */     if (localSecurityManager != null) {
/* 109 */       localSecurityManager.checkPrintJobAccess();
/*     */     }
/* 111 */     if (this.printServices == null) {
/* 112 */       refreshServices();
/*     */     }
/* 114 */     return this.printServices;
/*     */   }
/*     */ 
/*     */   private synchronized void refreshServices() {
/* 118 */     this.printers = getAllPrinterNames();
/* 119 */     if (this.printers == null)
/*     */     {
/* 122 */       this.printServices = new PrintService[0];
/* 123 */       return;
/*     */     }
/*     */ 
/* 126 */     PrintService[] arrayOfPrintService = new PrintService[this.printers.length];
/* 127 */     PrintService localPrintService = getDefaultPrintService();
/* 128 */     for (int i = 0; i < this.printers.length; i++) {
/* 129 */       if ((localPrintService != null) && (this.printers[i].equals(localPrintService.getName())))
/*     */       {
/* 131 */         arrayOfPrintService[i] = localPrintService;
/*     */       }
/* 133 */       else if (this.printServices == null) {
/* 134 */         arrayOfPrintService[i] = new Win32PrintService(this.printers[i]);
/*     */       }
/*     */       else {
/* 137 */         for (int j = 0; j < this.printServices.length; j++) {
/* 138 */           if ((this.printServices[j] != null) && (this.printers[i].equals(this.printServices[j].getName())))
/*     */           {
/* 140 */             arrayOfPrintService[i] = this.printServices[j];
/* 141 */             this.printServices[j] = null;
/* 142 */             break;
/*     */           }
/*     */         }
/* 145 */         if (j == this.printServices.length) {
/* 146 */           arrayOfPrintService[i] = new Win32PrintService(this.printers[i]);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 153 */     if (this.printServices != null) {
/* 154 */       for (i = 0; i < this.printServices.length; i++) {
/* 155 */         if (((this.printServices[i] instanceof Win32PrintService)) && (!this.printServices[i].equals(this.defaultPrintService)))
/*     */         {
/* 157 */           ((Win32PrintService)this.printServices[i]).invalidateService();
/*     */         }
/*     */       }
/*     */     }
/* 161 */     this.printServices = arrayOfPrintService;
/*     */   }
/*     */ 
/*     */   public synchronized PrintService getPrintServiceByName(String paramString)
/*     */   {
/* 167 */     if ((paramString == null) || (paramString.equals(""))) {
/* 168 */       return null;
/*     */     }
/*     */ 
/* 171 */     PrintService[] arrayOfPrintService = getPrintServices();
/* 172 */     for (int i = 0; i < arrayOfPrintService.length; i++) {
/* 173 */       if (arrayOfPrintService[i].getName().equals(paramString)) {
/* 174 */         return arrayOfPrintService[i];
/*     */       }
/*     */     }
/* 177 */     return null;
/*     */   }
/*     */ 
/*     */   boolean matchingService(PrintService paramPrintService, PrintServiceAttributeSet paramPrintServiceAttributeSet)
/*     */   {
/* 183 */     if (paramPrintServiceAttributeSet != null) {
/* 184 */       Attribute[] arrayOfAttribute = paramPrintServiceAttributeSet.toArray();
/*     */ 
/* 186 */       for (int i = 0; i < arrayOfAttribute.length; i++) {
/* 187 */         PrintServiceAttribute localPrintServiceAttribute = paramPrintService.getAttribute(arrayOfAttribute[i].getCategory());
/*     */ 
/* 189 */         if ((localPrintServiceAttribute == null) || (!localPrintServiceAttribute.equals(arrayOfAttribute[i]))) {
/* 190 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 194 */     return true;
/*     */   }
/*     */ 
/*     */   public PrintService[] getPrintServices(DocFlavor paramDocFlavor, AttributeSet paramAttributeSet)
/*     */   {
/* 200 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 201 */     if (localSecurityManager != null) {
/* 202 */       localSecurityManager.checkPrintJobAccess();
/*     */     }
/* 204 */     HashPrintRequestAttributeSet localHashPrintRequestAttributeSet = null;
/* 205 */     HashPrintServiceAttributeSet localHashPrintServiceAttributeSet = null;
/*     */ 
/* 207 */     if ((paramAttributeSet != null) && (!paramAttributeSet.isEmpty()))
/*     */     {
/* 209 */       localHashPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
/* 210 */       localHashPrintServiceAttributeSet = new HashPrintServiceAttributeSet();
/*     */ 
/* 212 */       localObject1 = paramAttributeSet.toArray();
/* 213 */       for (int i = 0; i < localObject1.length; i++) {
/* 214 */         if ((localObject1[i] instanceof PrintRequestAttribute))
/* 215 */           localHashPrintRequestAttributeSet.add(localObject1[i]);
/* 216 */         else if ((localObject1[i] instanceof PrintServiceAttribute)) {
/* 217 */           localHashPrintServiceAttributeSet.add(localObject1[i]);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 227 */     Object localObject1 = null;
/* 228 */     if ((localHashPrintServiceAttributeSet != null) && (localHashPrintServiceAttributeSet.get(PrinterName.class) != null)) {
/* 229 */       localObject2 = (PrinterName)localHashPrintServiceAttributeSet.get(PrinterName.class);
/* 230 */       PrintService localPrintService = getPrintServiceByName(((PrinterName)localObject2).getValue());
/* 231 */       if ((localPrintService == null) || (!matchingService(localPrintService, localHashPrintServiceAttributeSet))) {
/* 232 */         localObject1 = new PrintService[0];
/*     */       } else {
/* 234 */         localObject1 = new PrintService[1];
/* 235 */         localObject1[0] = localPrintService;
/*     */       }
/*     */     } else {
/* 238 */       localObject1 = getPrintServices();
/*     */     }
/*     */ 
/* 241 */     if (localObject1.length == 0) {
/* 242 */       return localObject1;
/*     */     }
/* 244 */     Object localObject2 = new ArrayList();
/* 245 */     for (int j = 0; j < localObject1.length; j++)
/*     */       try {
/* 247 */         if (localObject1[j].getUnsupportedAttributes(paramDocFlavor, localHashPrintRequestAttributeSet) == null)
/*     */         {
/* 249 */           ((ArrayList)localObject2).add(localObject1[j]);
/*     */         }
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException) {
/*     */       }
/* 254 */     localObject1 = new PrintService[((ArrayList)localObject2).size()];
/* 255 */     return (PrintService[])((ArrayList)localObject2).toArray((Object[])localObject1);
/*     */   }
/*     */ 
/*     */   public MultiDocPrintService[] getMultiDocPrintServices(DocFlavor[] paramArrayOfDocFlavor, AttributeSet paramAttributeSet)
/*     */   {
/* 265 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 266 */     if (localSecurityManager != null) {
/* 267 */       localSecurityManager.checkPrintJobAccess();
/*     */     }
/* 269 */     return new MultiDocPrintService[0];
/*     */   }
/*     */ 
/*     */   public synchronized PrintService getDefaultPrintService()
/*     */   {
/* 274 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 275 */     if (localSecurityManager != null) {
/* 276 */       localSecurityManager.checkPrintJobAccess();
/*     */     }
/*     */ 
/* 282 */     this.defaultPrinter = getDefaultPrinterName();
/* 283 */     if (this.defaultPrinter == null) {
/* 284 */       return null;
/*     */     }
/*     */ 
/* 287 */     if ((this.defaultPrintService != null) && (this.defaultPrintService.getName().equals(this.defaultPrinter)))
/*     */     {
/* 290 */       return this.defaultPrintService;
/*     */     }
/*     */ 
/* 296 */     this.defaultPrintService = null;
/*     */ 
/* 298 */     if (this.printServices != null) {
/* 299 */       for (int i = 0; i < this.printServices.length; i++) {
/* 300 */         if (this.defaultPrinter.equals(this.printServices[i].getName())) {
/* 301 */           this.defaultPrintService = this.printServices[i];
/* 302 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 307 */     if (this.defaultPrintService == null) {
/* 308 */       this.defaultPrintService = new Win32PrintService(this.defaultPrinter);
/*     */     }
/* 310 */     return this.defaultPrintService;
/*     */   }
/*     */ 
/*     */   private native String getDefaultPrinterName();
/*     */ 
/*     */   private native String[] getAllPrinterNames();
/*     */ 
/*     */   private native long notifyFirstPrinterChange(String paramString);
/*     */ 
/*     */   private native void notifyClosePrinterChange(long paramLong);
/*     */ 
/*     */   private native int notifyPrinterChange(long paramLong);
/*     */ 
/*     */   static
/*     */   {
/*  58 */     AccessController.doPrivileged(new LoadLibraryAction("awt"));
/*     */   }
/*     */ 
/*     */   class PrinterChangeListener extends Thread
/*     */   {
/*     */     long chgObj;
/*     */ 
/*     */     PrinterChangeListener()
/*     */     {
/* 316 */       this.chgObj = Win32PrintServiceLookup.this.notifyFirstPrinterChange(null);
/*     */     }
/*     */ 
/*     */     public void run() {
/* 320 */       if (this.chgObj != -1L)
/*     */       {
/*     */         while (true) {
/* 323 */           if (Win32PrintServiceLookup.this.notifyPrinterChange(this.chgObj) != 0)
/*     */             try {
/* 325 */               Win32PrintServiceLookup.this.refreshServices();
/*     */             } catch (SecurityException localSecurityException) {
/* 327 */               return;
/*     */             }
/*     */         }
/* 330 */         Win32PrintServiceLookup.this.notifyClosePrinterChange(this.chgObj);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.Win32PrintServiceLookup
 * JD-Core Version:    0.6.2
 */