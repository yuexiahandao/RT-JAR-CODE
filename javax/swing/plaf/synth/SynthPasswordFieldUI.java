/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.PasswordView;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ public class SynthPasswordFieldUI extends SynthTextFieldUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  49 */     return new SynthPasswordFieldUI();
/*     */   }
/*     */ 
/*     */   protected String getPropertyPrefix()
/*     */   {
/*  61 */     return "PasswordField";
/*     */   }
/*     */ 
/*     */   public View create(Element paramElement)
/*     */   {
/*  72 */     return new PasswordView(paramElement);
/*     */   }
/*     */ 
/*     */   void paintBackground(SynthContext paramSynthContext, Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/*  80 */     paramSynthContext.getPainter().paintPasswordFieldBackground(paramSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  90 */     paramSynthContext.getPainter().paintPasswordFieldBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   protected void installKeyboardActions()
/*     */   {
/*  98 */     super.installKeyboardActions();
/*  99 */     ActionMap localActionMap = SwingUtilities.getUIActionMap(getComponent());
/* 100 */     if ((localActionMap != null) && (localActionMap.get("select-word") != null)) {
/* 101 */       Action localAction = localActionMap.get("select-line");
/* 102 */       if (localAction != null)
/* 103 */         localActionMap.put("select-word", localAction);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthPasswordFieldUI
 * JD-Core Version:    0.6.2
 */