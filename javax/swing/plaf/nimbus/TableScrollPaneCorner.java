/*    */ package javax.swing.plaf.nimbus;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.image.BufferedImage;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.Painter;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.plaf.UIResource;
/*    */ 
/*    */ class TableScrollPaneCorner extends JComponent
/*    */   implements UIResource
/*    */ {
/*    */   protected void paintComponent(Graphics paramGraphics)
/*    */   {
/* 50 */     Painter localPainter = (Painter)UIManager.get("TableHeader:\"TableHeader.renderer\"[Enabled].backgroundPainter");
/*    */ 
/* 52 */     if (localPainter != null)
/* 53 */       if ((paramGraphics instanceof Graphics2D)) {
/* 54 */         localPainter.paint((Graphics2D)paramGraphics, this, getWidth() + 1, getHeight());
/*    */       }
/*    */       else
/*    */       {
/* 58 */         BufferedImage localBufferedImage = new BufferedImage(getWidth(), getHeight(), 2);
/*    */ 
/* 60 */         Graphics2D localGraphics2D = (Graphics2D)localBufferedImage.getGraphics();
/* 61 */         localPainter.paint(localGraphics2D, this, getWidth() + 1, getHeight());
/* 62 */         localGraphics2D.dispose();
/* 63 */         paramGraphics.drawImage(localBufferedImage, 0, 0, null);
/* 64 */         localBufferedImage = null;
/*    */       }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.TableScrollPaneCorner
 * JD-Core Version:    0.6.2
 */