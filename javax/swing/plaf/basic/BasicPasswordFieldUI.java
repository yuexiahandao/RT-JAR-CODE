/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.PasswordView;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ public class BasicPasswordFieldUI extends BasicTextFieldUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  52 */     return new BasicPasswordFieldUI();
/*     */   }
/*     */ 
/*     */   protected String getPropertyPrefix()
/*     */   {
/*  63 */     return "PasswordField";
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  72 */     super.installDefaults();
/*  73 */     String str = getPropertyPrefix();
/*  74 */     Character localCharacter = (Character)UIManager.getDefaults().get(str + ".echoChar");
/*  75 */     if (localCharacter != null)
/*  76 */       LookAndFeel.installProperty(getComponent(), "echoChar", localCharacter);
/*     */   }
/*     */ 
/*     */   public View create(Element paramElement)
/*     */   {
/*  87 */     return new PasswordView(paramElement);
/*     */   }
/*     */ 
/*     */   ActionMap createActionMap()
/*     */   {
/*  97 */     ActionMap localActionMap = super.createActionMap();
/*  98 */     if (localActionMap.get("select-word") != null) {
/*  99 */       Action localAction = localActionMap.get("select-line");
/* 100 */       if (localAction != null) {
/* 101 */         localActionMap.remove("select-word");
/* 102 */         localActionMap.put("select-word", localAction);
/*     */       }
/*     */     }
/* 105 */     return localActionMap;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicPasswordFieldUI
 * JD-Core Version:    0.6.2
 */