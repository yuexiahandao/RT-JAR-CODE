/*     */ package sun.print;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import javax.print.PrintService;
/*     */ import javax.print.attribute.HashPrintServiceAttributeSet;
/*     */ import javax.print.attribute.PrintServiceAttributeSet;
/*     */ import javax.print.event.PrintServiceAttributeEvent;
/*     */ import javax.print.event.PrintServiceAttributeListener;
/*     */ 
/*     */ class ServiceNotifier extends Thread
/*     */ {
/*     */   private PrintService service;
/*     */   private Vector listeners;
/*  47 */   private boolean stop = false;
/*     */   private PrintServiceAttributeSet lastSet;
/*     */ 
/*     */   ServiceNotifier(PrintService paramPrintService)
/*     */   {
/*  51 */     super(paramPrintService.getName() + " notifier");
/*  52 */     this.service = paramPrintService;
/*  53 */     this.listeners = new Vector();
/*     */     try {
/*  55 */       setPriority(4);
/*  56 */       setDaemon(true);
/*  57 */       start();
/*     */     } catch (SecurityException localSecurityException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   void addListener(PrintServiceAttributeListener paramPrintServiceAttributeListener) {
/*  63 */     synchronized (this) {
/*  64 */       if ((paramPrintServiceAttributeListener == null) || (this.listeners == null)) {
/*  65 */         return;
/*     */       }
/*  67 */       this.listeners.add(paramPrintServiceAttributeListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   void removeListener(PrintServiceAttributeListener paramPrintServiceAttributeListener) {
/*  72 */     synchronized (this) {
/*  73 */       if ((paramPrintServiceAttributeListener == null) || (this.listeners == null)) {
/*  74 */         return;
/*     */       }
/*  76 */       this.listeners.remove(paramPrintServiceAttributeListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean isEmpty() {
/*  81 */     return (this.listeners == null) || (this.listeners.isEmpty());
/*     */   }
/*     */ 
/*     */   void stopNotifier() {
/*  85 */     this.stop = true;
/*     */   }
/*     */ 
/*     */   void wake()
/*     */   {
/*     */     try
/*     */     {
/*  93 */       interrupt();
/*     */     }
/*     */     catch (SecurityException localSecurityException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 105 */     long l1 = 15000L;
/* 106 */     long l2 = 2000L;
/*     */ 
/* 112 */     while (!this.stop) {
/*     */       try {
/* 114 */         Thread.sleep(l2);
/*     */       } catch (InterruptedException localInterruptedException) {
/*     */       }
/* 117 */       synchronized (this) {
/* 118 */         if (this.listeners != null)
/*     */         {
/* 121 */           long l3 = System.currentTimeMillis();
/* 122 */           if (this.listeners != null)
/*     */           {
/*     */             PrintServiceAttributeSet localPrintServiceAttributeSet;
/* 123 */             if ((this.service instanceof AttributeUpdater)) {
/* 124 */               localPrintServiceAttributeSet = ((AttributeUpdater)this.service).getUpdatedAttributes();
/*     */             }
/*     */             else {
/* 127 */               localPrintServiceAttributeSet = this.service.getAttributes();
/*     */             }
/* 129 */             if ((localPrintServiceAttributeSet != null) && (!localPrintServiceAttributeSet.isEmpty())) {
/* 130 */               for (int i = 0; i < this.listeners.size(); i++) {
/* 131 */                 PrintServiceAttributeListener localPrintServiceAttributeListener = (PrintServiceAttributeListener)this.listeners.elementAt(i);
/*     */ 
/* 133 */                 HashPrintServiceAttributeSet localHashPrintServiceAttributeSet = new HashPrintServiceAttributeSet(localPrintServiceAttributeSet);
/*     */ 
/* 135 */                 PrintServiceAttributeEvent localPrintServiceAttributeEvent = new PrintServiceAttributeEvent(this.service, localHashPrintServiceAttributeSet);
/*     */ 
/* 137 */                 localPrintServiceAttributeListener.attributeUpdate(localPrintServiceAttributeEvent);
/*     */               }
/*     */             }
/*     */           }
/* 141 */           l2 = (System.currentTimeMillis() - l3) * 10L;
/* 142 */           if (l2 < l1)
/* 143 */             l2 = l1;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.ServiceNotifier
 * JD-Core Version:    0.6.2
 */