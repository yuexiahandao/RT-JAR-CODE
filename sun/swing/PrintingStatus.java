/*     */ package sun.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.print.PageFormat;
/*     */ import java.awt.print.Printable;
/*     */ import java.awt.print.PrinterException;
/*     */ import java.awt.print.PrinterJob;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ public class PrintingStatus
/*     */ {
/*     */   private final PrinterJob job;
/*     */   private final Component parent;
/*     */   private JDialog abortDialog;
/*     */   private JButton abortButton;
/*     */   private JLabel statusLabel;
/*     */   private MessageFormat statusFormat;
/*  62 */   private final AtomicBoolean isAborted = new AtomicBoolean(false);
/*     */ 
/*  65 */   private final Action abortAction = new AbstractAction() {
/*     */     public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/*  67 */       if (!PrintingStatus.this.isAborted.get()) {
/*  68 */         PrintingStatus.this.isAborted.set(true);
/*     */ 
/*  71 */         PrintingStatus.this.abortButton.setEnabled(false);
/*  72 */         PrintingStatus.this.abortDialog.setTitle(UIManager.getString("PrintingDialog.titleAbortingText"));
/*     */ 
/*  74 */         PrintingStatus.this.statusLabel.setText(UIManager.getString("PrintingDialog.contentAbortingText"));
/*     */ 
/*  78 */         PrintingStatus.this.job.cancel();
/*     */       }
/*     */     }
/*  65 */   };
/*     */ 
/*  83 */   private final WindowAdapter closeListener = new WindowAdapter() {
/*     */     public void windowClosing(WindowEvent paramAnonymousWindowEvent) {
/*  85 */       PrintingStatus.this.abortAction.actionPerformed(null);
/*     */     }
/*  83 */   };
/*     */ 
/*     */   public static PrintingStatus createPrintingStatus(Component paramComponent, PrinterJob paramPrinterJob)
/*     */   {
/* 100 */     return new PrintingStatus(paramComponent, paramPrinterJob);
/*     */   }
/*     */ 
/*     */   protected PrintingStatus(Component paramComponent, PrinterJob paramPrinterJob) {
/* 104 */     this.job = paramPrinterJob;
/* 105 */     this.parent = paramComponent;
/*     */   }
/*     */ 
/*     */   private void init()
/*     */   {
/* 110 */     String str1 = UIManager.getString("PrintingDialog.titleProgressText");
/*     */ 
/* 113 */     String str2 = UIManager.getString("PrintingDialog.contentInitialText");
/*     */ 
/* 118 */     this.statusFormat = new MessageFormat(UIManager.getString("PrintingDialog.contentProgressText"));
/*     */ 
/* 121 */     String str3 = UIManager.getString("PrintingDialog.abortButtonText");
/*     */ 
/* 123 */     String str4 = UIManager.getString("PrintingDialog.abortButtonToolTipText");
/*     */ 
/* 125 */     int i = getInt("PrintingDialog.abortButtonMnemonic", -1);
/*     */ 
/* 127 */     int j = getInt("PrintingDialog.abortButtonDisplayedMnemonicIndex", -1);
/*     */ 
/* 130 */     this.abortButton = new JButton(str3);
/* 131 */     this.abortButton.addActionListener(this.abortAction);
/*     */ 
/* 133 */     this.abortButton.setToolTipText(str4);
/* 134 */     if (i != -1) {
/* 135 */       this.abortButton.setMnemonic(i);
/*     */     }
/* 137 */     if (j != -1) {
/* 138 */       this.abortButton.setDisplayedMnemonicIndex(j);
/*     */     }
/* 140 */     this.statusLabel = new JLabel(str2);
/* 141 */     JOptionPane localJOptionPane = new JOptionPane(this.statusLabel, 1, -1, null, new Object[] { this.abortButton }, this.abortButton);
/*     */ 
/* 146 */     localJOptionPane.getActionMap().put("close", this.abortAction);
/*     */ 
/* 149 */     if ((this.parent != null) && ((this.parent.getParent() instanceof JViewport))) {
/* 150 */       this.abortDialog = localJOptionPane.createDialog(this.parent.getParent(), str1);
/*     */     }
/*     */     else {
/* 153 */       this.abortDialog = localJOptionPane.createDialog(this.parent, str1);
/*     */     }
/*     */ 
/* 156 */     this.abortDialog.setDefaultCloseOperation(0);
/* 157 */     this.abortDialog.addWindowListener(this.closeListener);
/*     */   }
/*     */ 
/*     */   public void showModal(final boolean paramBoolean)
/*     */   {
/* 170 */     if (SwingUtilities.isEventDispatchThread())
/* 171 */       showModalOnEDT(paramBoolean);
/*     */     else
/*     */       try {
/* 174 */         SwingUtilities.invokeAndWait(new Runnable() {
/*     */           public void run() {
/* 176 */             PrintingStatus.this.showModalOnEDT(paramBoolean);
/*     */           } } );
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/* 180 */         throw new RuntimeException(localInterruptedException);
/*     */       } catch (InvocationTargetException localInvocationTargetException) {
/* 182 */         Throwable localThrowable = localInvocationTargetException.getCause();
/* 183 */         if ((localThrowable instanceof RuntimeException))
/* 184 */           throw ((RuntimeException)localThrowable);
/* 185 */         if ((localThrowable instanceof Error)) {
/* 186 */           throw ((Error)localThrowable);
/*     */         }
/* 188 */         throw new RuntimeException(localThrowable);
/*     */       }
/*     */   }
/*     */ 
/*     */   private void showModalOnEDT(boolean paramBoolean)
/*     */   {
/* 200 */     assert (SwingUtilities.isEventDispatchThread());
/* 201 */     init();
/* 202 */     this.abortDialog.setModal(paramBoolean);
/* 203 */     this.abortDialog.setVisible(true);
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 212 */     if (SwingUtilities.isEventDispatchThread())
/* 213 */       disposeOnEDT();
/*     */     else
/* 215 */       SwingUtilities.invokeLater(new Runnable() {
/*     */         public void run() {
/* 217 */           PrintingStatus.this.disposeOnEDT();
/*     */         }
/*     */       });
/*     */   }
/*     */ 
/*     */   private void disposeOnEDT()
/*     */   {
/* 229 */     assert (SwingUtilities.isEventDispatchThread());
/* 230 */     if (this.abortDialog != null) {
/* 231 */       this.abortDialog.removeWindowListener(this.closeListener);
/* 232 */       this.abortDialog.dispose();
/* 233 */       this.abortDialog = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isAborted()
/*     */   {
/* 243 */     return this.isAborted.get();
/*     */   }
/*     */ 
/*     */   public Printable createNotificationPrintable(Printable paramPrintable)
/*     */   {
/* 256 */     return new NotificationPrintable(paramPrintable);
/*     */   }
/*     */ 
/*     */   static int getInt(Object paramObject, int paramInt)
/*     */   {
/* 306 */     Object localObject = UIManager.get(paramObject);
/* 307 */     if ((localObject instanceof Integer)) {
/* 308 */       return ((Integer)localObject).intValue();
/*     */     }
/* 310 */     if ((localObject instanceof String))
/*     */       try {
/* 312 */         return Integer.parseInt((String)localObject);
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException) {
/*     */       }
/* 316 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private class NotificationPrintable
/*     */     implements Printable
/*     */   {
/*     */     private final Printable printDelegatee;
/*     */ 
/*     */     public NotificationPrintable(Printable arg2)
/*     */     {
/*     */       Object localObject;
/* 263 */       if (localObject == null) {
/* 264 */         throw new NullPointerException("Printable is null");
/*     */       }
/* 266 */       this.printDelegatee = localObject;
/*     */     }
/*     */ 
/*     */     public int print(Graphics paramGraphics, PageFormat paramPageFormat, final int paramInt)
/*     */       throws PrinterException
/*     */     {
/* 273 */       int i = this.printDelegatee.print(paramGraphics, paramPageFormat, paramInt);
/*     */ 
/* 275 */       if ((i != 1) && (!PrintingStatus.this.isAborted())) {
/* 276 */         if (SwingUtilities.isEventDispatchThread())
/* 277 */           updateStatusOnEDT(paramInt);
/*     */         else {
/* 279 */           SwingUtilities.invokeLater(new Runnable() {
/*     */             public void run() {
/* 281 */               PrintingStatus.NotificationPrintable.this.updateStatusOnEDT(paramInt);
/*     */             }
/*     */           });
/*     */         }
/*     */       }
/* 286 */       return i;
/*     */     }
/*     */ 
/*     */     private void updateStatusOnEDT(int paramInt)
/*     */     {
/* 295 */       assert (SwingUtilities.isEventDispatchThread());
/* 296 */       Object[] arrayOfObject = { new Integer(paramInt + 1) };
/*     */ 
/* 298 */       PrintingStatus.this.statusLabel.setText(PrintingStatus.this.statusFormat.format(arrayOfObject));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.PrintingStatus
 * JD-Core Version:    0.6.2
 */