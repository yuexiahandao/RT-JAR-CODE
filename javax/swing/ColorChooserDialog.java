/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Frame;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.Serializable;
/*     */ import java.util.Locale;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ class ColorChooserDialog extends JDialog
/*     */ {
/*     */   private Color initialColor;
/*     */   private JColorChooser chooserPane;
/*     */   private JButton cancelButton;
/*     */ 
/*     */   public ColorChooserDialog(Dialog paramDialog, String paramString, boolean paramBoolean, Component paramComponent, JColorChooser paramJColorChooser, ActionListener paramActionListener1, ActionListener paramActionListener2)
/*     */     throws HeadlessException
/*     */   {
/* 617 */     super(paramDialog, paramString, paramBoolean);
/* 618 */     initColorChooserDialog(paramComponent, paramJColorChooser, paramActionListener1, paramActionListener2);
/*     */   }
/*     */ 
/*     */   public ColorChooserDialog(Frame paramFrame, String paramString, boolean paramBoolean, Component paramComponent, JColorChooser paramJColorChooser, ActionListener paramActionListener1, ActionListener paramActionListener2)
/*     */     throws HeadlessException
/*     */   {
/* 625 */     super(paramFrame, paramString, paramBoolean);
/* 626 */     initColorChooserDialog(paramComponent, paramJColorChooser, paramActionListener1, paramActionListener2);
/*     */   }
/*     */ 
/*     */   protected void initColorChooserDialog(Component paramComponent, JColorChooser paramJColorChooser, ActionListener paramActionListener1, ActionListener paramActionListener2)
/*     */   {
/* 633 */     this.chooserPane = paramJColorChooser;
/*     */ 
/* 635 */     Locale localLocale = getLocale();
/* 636 */     String str1 = UIManager.getString("ColorChooser.okText", localLocale);
/* 637 */     String str2 = UIManager.getString("ColorChooser.cancelText", localLocale);
/* 638 */     String str3 = UIManager.getString("ColorChooser.resetText", localLocale);
/*     */ 
/* 640 */     Container localContainer = getContentPane();
/* 641 */     localContainer.setLayout(new BorderLayout());
/* 642 */     localContainer.add(paramJColorChooser, "Center");
/*     */ 
/* 647 */     JPanel localJPanel = new JPanel();
/* 648 */     localJPanel.setLayout(new FlowLayout(1));
/* 649 */     JButton localJButton1 = new JButton(str1);
/* 650 */     getRootPane().setDefaultButton(localJButton1);
/* 651 */     localJButton1.getAccessibleContext().setAccessibleDescription(str1);
/* 652 */     localJButton1.setActionCommand("OK");
/* 653 */     localJButton1.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/* 655 */         ColorChooserDialog.this.hide();
/*     */       }
/*     */     });
/* 658 */     if (paramActionListener1 != null) {
/* 659 */       localJButton1.addActionListener(paramActionListener1);
/*     */     }
/* 661 */     localJPanel.add(localJButton1);
/*     */ 
/* 663 */     this.cancelButton = new JButton(str2);
/* 664 */     this.cancelButton.getAccessibleContext().setAccessibleDescription(str2);
/*     */ 
/* 667 */     AbstractAction local2 = new AbstractAction() {
/*     */       public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/* 669 */         ((AbstractButton)paramAnonymousActionEvent.getSource()).fireActionPerformed(paramAnonymousActionEvent);
/*     */       }
/*     */     };
/* 672 */     KeyStroke localKeyStroke = KeyStroke.getKeyStroke(27, 0);
/* 673 */     InputMap localInputMap = this.cancelButton.getInputMap(2);
/*     */ 
/* 675 */     ActionMap localActionMap = this.cancelButton.getActionMap();
/* 676 */     if ((localInputMap != null) && (localActionMap != null)) {
/* 677 */       localInputMap.put(localKeyStroke, "cancel");
/* 678 */       localActionMap.put("cancel", local2);
/*     */     }
/*     */ 
/* 682 */     this.cancelButton.setActionCommand("cancel");
/* 683 */     this.cancelButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/* 685 */         ColorChooserDialog.this.hide();
/*     */       }
/*     */     });
/* 688 */     if (paramActionListener2 != null) {
/* 689 */       this.cancelButton.addActionListener(paramActionListener2);
/*     */     }
/* 691 */     localJPanel.add(this.cancelButton);
/*     */ 
/* 693 */     JButton localJButton2 = new JButton(str3);
/* 694 */     localJButton2.getAccessibleContext().setAccessibleDescription(str3);
/* 695 */     localJButton2.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/* 697 */         ColorChooserDialog.this.reset();
/*     */       }
/*     */     });
/* 700 */     int i = SwingUtilities2.getUIDefaultsInt("ColorChooser.resetMnemonic", localLocale, -1);
/* 701 */     if (i != -1) {
/* 702 */       localJButton2.setMnemonic(i);
/*     */     }
/* 704 */     localJPanel.add(localJButton2);
/* 705 */     localContainer.add(localJPanel, "South");
/*     */ 
/* 707 */     if (JDialog.isDefaultLookAndFeelDecorated()) {
/* 708 */       boolean bool = UIManager.getLookAndFeel().getSupportsWindowDecorations();
/*     */ 
/* 710 */       if (bool) {
/* 711 */         getRootPane().setWindowDecorationStyle(5);
/*     */       }
/*     */     }
/* 714 */     applyComponentOrientation((paramComponent == null ? getRootPane() : paramComponent).getComponentOrientation());
/*     */ 
/* 716 */     pack();
/* 717 */     setLocationRelativeTo(paramComponent);
/*     */ 
/* 719 */     addWindowListener(new Closer());
/*     */   }
/*     */ 
/*     */   public void show() {
/* 723 */     this.initialColor = this.chooserPane.getColor();
/* 724 */     super.show();
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 728 */     this.chooserPane.setColor(this.initialColor);
/*     */   }
/*     */   class Closer extends WindowAdapter implements Serializable {
/*     */     Closer() {
/*     */     }
/* 733 */     public void windowClosing(WindowEvent paramWindowEvent) { ColorChooserDialog.this.cancelButton.doClick(0);
/* 734 */       Window localWindow = paramWindowEvent.getWindow();
/* 735 */       localWindow.hide(); }
/*     */   }
/*     */ 
/*     */   static class DisposeOnClose extends ComponentAdapter implements Serializable
/*     */   {
/*     */     public void componentHidden(ComponentEvent paramComponentEvent) {
/* 741 */       Window localWindow = (Window)paramComponentEvent.getComponent();
/* 742 */       localWindow.dispose();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ColorChooserDialog
 * JD-Core Version:    0.6.2
 */