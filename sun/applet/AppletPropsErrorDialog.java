/*     */ package sun.applet;
/*     */ 
/*     */ import java.awt.Button;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Event;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Label;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Rectangle;
/*     */ 
/*     */ class AppletPropsErrorDialog extends Dialog
/*     */ {
/*     */   public AppletPropsErrorDialog(Frame paramFrame, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 201 */     super(paramFrame, paramString1, true);
/* 202 */     Panel localPanel = new Panel();
/* 203 */     add("Center", new Label(paramString2));
/* 204 */     localPanel.add(new Button(paramString3));
/* 205 */     add("South", localPanel);
/* 206 */     pack();
/*     */ 
/* 208 */     Dimension localDimension = size();
/* 209 */     Rectangle localRectangle = paramFrame.bounds();
/* 210 */     move(localRectangle.x + (localRectangle.width - localDimension.width) / 2, localRectangle.y + (localRectangle.height - localDimension.height) / 2);
/*     */   }
/*     */ 
/*     */   public boolean action(Event paramEvent, Object paramObject)
/*     */   {
/* 215 */     hide();
/* 216 */     dispose();
/* 217 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletPropsErrorDialog
 * JD-Core Version:    0.6.2
 */