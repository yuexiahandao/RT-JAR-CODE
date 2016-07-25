/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ 
/*     */ public abstract class TextAction extends AbstractAction
/*     */ {
/*     */   public TextAction(String paramString)
/*     */   {
/*  69 */     super(paramString);
/*     */   }
/*     */ 
/*     */   protected final JTextComponent getTextComponent(ActionEvent paramActionEvent)
/*     */   {
/*  82 */     if (paramActionEvent != null) {
/*  83 */       Object localObject = paramActionEvent.getSource();
/*  84 */       if ((localObject instanceof JTextComponent)) {
/*  85 */         return (JTextComponent)localObject;
/*     */       }
/*     */     }
/*  88 */     return getFocusedComponent();
/*     */   }
/*     */ 
/*     */   public static final Action[] augmentList(Action[] paramArrayOfAction1, Action[] paramArrayOfAction2)
/*     */   {
/* 106 */     Hashtable localHashtable = new Hashtable();
/*     */     Action localAction;
/*     */     String str;
/* 107 */     for (localAction : paramArrayOfAction1) {
/* 108 */       str = (String)localAction.getValue("Name");
/* 109 */       localHashtable.put(str != null ? str : "", localAction);
/*     */     }
/* 111 */     for (localAction : paramArrayOfAction2) {
/* 112 */       str = (String)localAction.getValue("Name");
/* 113 */       localHashtable.put(str != null ? str : "", localAction);
/*     */     }
/* 115 */     ??? = new Action[localHashtable.size()];
/* 116 */     ??? = 0;
/* 117 */     for (Enumeration localEnumeration = localHashtable.elements(); localEnumeration.hasMoreElements(); ) {
/* 118 */       ???[(???++)] = ((Action)localEnumeration.nextElement());
/*     */     }
/* 120 */     return ???;
/*     */   }
/*     */ 
/*     */   protected final JTextComponent getFocusedComponent()
/*     */   {
/* 133 */     return JTextComponent.getFocusedComponent();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.TextAction
 * JD-Core Version:    0.6.2
 */