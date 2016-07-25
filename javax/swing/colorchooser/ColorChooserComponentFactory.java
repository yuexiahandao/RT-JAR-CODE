/*    */ package javax.swing.colorchooser;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ 
/*    */ public class ColorChooserComponentFactory
/*    */ {
/*    */   public static AbstractColorChooserPanel[] getDefaultChooserPanels()
/*    */   {
/* 51 */     return new AbstractColorChooserPanel[] { new DefaultSwatchChooserPanel(), new ColorChooserPanel(new ColorModelHSV()), new ColorChooserPanel(new ColorModelHSL()), new ColorChooserPanel(new ColorModel()), new ColorChooserPanel(new ColorModelCMYK()) };
/*    */   }
/*    */ 
/*    */   public static JComponent getPreviewPanel()
/*    */   {
/* 61 */     return new DefaultPreviewPanel();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.ColorChooserComponentFactory
 * JD-Core Version:    0.6.2
 */