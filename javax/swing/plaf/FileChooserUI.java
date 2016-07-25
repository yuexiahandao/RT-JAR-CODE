/*    */ package javax.swing.plaf;
/*    */ 
/*    */ import java.io.File;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JFileChooser;
/*    */ import javax.swing.filechooser.FileFilter;
/*    */ import javax.swing.filechooser.FileView;
/*    */ 
/*    */ public abstract class FileChooserUI extends ComponentUI
/*    */ {
/*    */   public abstract FileFilter getAcceptAllFileFilter(JFileChooser paramJFileChooser);
/*    */ 
/*    */   public abstract FileView getFileView(JFileChooser paramJFileChooser);
/*    */ 
/*    */   public abstract String getApproveButtonText(JFileChooser paramJFileChooser);
/*    */ 
/*    */   public abstract String getDialogTitle(JFileChooser paramJFileChooser);
/*    */ 
/*    */   public abstract void rescanCurrentDirectory(JFileChooser paramJFileChooser);
/*    */ 
/*    */   public abstract void ensureFileIsVisible(JFileChooser paramJFileChooser, File paramFile);
/*    */ 
/*    */   public JButton getDefaultButton(JFileChooser paramJFileChooser)
/*    */   {
/* 58 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.FileChooserUI
 * JD-Core Version:    0.6.2
 */