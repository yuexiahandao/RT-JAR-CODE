/*     */ package com.sun.security.auth.callback;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.ConfirmationCallback;
/*     */ import javax.security.auth.callback.NameCallback;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.callback.TextOutputCallback;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPasswordField;
/*     */ import javax.swing.JTextField;
/*     */ 
/*     */ public class DialogCallbackHandler
/*     */   implements CallbackHandler
/*     */ {
/*     */   private Component parentComponent;
/*     */   private static final int JPasswordFieldLen = 8;
/*     */   private static final int JTextFieldLen = 8;
/*     */ 
/*     */   public DialogCallbackHandler()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DialogCallbackHandler(Component paramComponent)
/*     */   {
/*  79 */     this.parentComponent = paramComponent;
/*     */   }
/*     */ 
/*     */   public void handle(Callback[] paramArrayOfCallback)
/*     */     throws UnsupportedCallbackException
/*     */   {
/* 102 */     ArrayList localArrayList1 = new ArrayList(3);
/*     */ 
/* 105 */     ArrayList localArrayList2 = new ArrayList(2);
/*     */ 
/* 107 */     ConfirmationInfo localConfirmationInfo = new ConfirmationInfo(null);
/*     */     Object localObject1;
/* 109 */     for (int i = 0; i < paramArrayOfCallback.length; i++) {
/* 110 */       if ((paramArrayOfCallback[i] instanceof TextOutputCallback)) {
/* 111 */         localObject1 = (TextOutputCallback)paramArrayOfCallback[i];
/*     */ 
/* 113 */         switch (((TextOutputCallback)localObject1).getMessageType()) {
/*     */         case 0:
/* 115 */           localConfirmationInfo.messageType = 1;
/* 116 */           break;
/*     */         case 1:
/* 118 */           localConfirmationInfo.messageType = 2;
/* 119 */           break;
/*     */         case 2:
/* 121 */           localConfirmationInfo.messageType = 0;
/* 122 */           break;
/*     */         default:
/* 124 */           throw new UnsupportedCallbackException(paramArrayOfCallback[i], "Unrecognized message type");
/*     */         }
/*     */ 
/* 128 */         localArrayList1.add(((TextOutputCallback)localObject1).getMessage());
/*     */       }
/*     */       else
/*     */       {
/*     */         JLabel localJLabel;
/*     */         Object localObject2;
/*     */         Object localObject3;
/* 130 */         if ((paramArrayOfCallback[i] instanceof NameCallback)) {
/* 131 */           localObject1 = (NameCallback)paramArrayOfCallback[i];
/*     */ 
/* 133 */           localJLabel = new JLabel(((NameCallback)localObject1).getPrompt());
/*     */ 
/* 135 */           localObject2 = new JTextField(8);
/* 136 */           localObject3 = ((NameCallback)localObject1).getDefaultName();
/* 137 */           if (localObject3 != null) {
/* 138 */             ((JTextField)localObject2).setText((String)localObject3);
/*     */           }
/*     */ 
/* 145 */           Box localBox = Box.createHorizontalBox();
/* 146 */           localBox.add(localJLabel);
/* 147 */           localBox.add((Component)localObject2);
/* 148 */           localArrayList1.add(localBox);
/*     */ 
/* 151 */           localArrayList2.add(new Action() {
/*     */             public void perform() {
/* 153 */               this.val$nc.setName(this.val$name.getText());
/*     */             }
/*     */           });
/*     */         }
/* 157 */         else if ((paramArrayOfCallback[i] instanceof PasswordCallback)) {
/* 158 */           localObject1 = (PasswordCallback)paramArrayOfCallback[i];
/*     */ 
/* 160 */           localJLabel = new JLabel(((PasswordCallback)localObject1).getPrompt());
/*     */ 
/* 162 */           localObject2 = new JPasswordField(8);
/*     */ 
/* 164 */           if (!((PasswordCallback)localObject1).isEchoOn()) {
/* 165 */             ((JPasswordField)localObject2).setEchoChar('*');
/*     */           }
/*     */ 
/* 168 */           localObject3 = Box.createHorizontalBox();
/* 169 */           ((Box)localObject3).add(localJLabel);
/* 170 */           ((Box)localObject3).add((Component)localObject2);
/* 171 */           localArrayList1.add(localObject3);
/*     */ 
/* 173 */           localArrayList2.add(new Action() {
/*     */             public void perform() {
/* 175 */               this.val$pc.setPassword(this.val$password.getPassword());
/*     */             }
/*     */           });
/*     */         }
/* 179 */         else if ((paramArrayOfCallback[i] instanceof ConfirmationCallback)) {
/* 180 */           localObject1 = (ConfirmationCallback)paramArrayOfCallback[i];
/*     */ 
/* 182 */           localConfirmationInfo.setCallback((ConfirmationCallback)localObject1);
/* 183 */           if (((ConfirmationCallback)localObject1).getPrompt() != null)
/* 184 */             localArrayList1.add(((ConfirmationCallback)localObject1).getPrompt());
/*     */         }
/*     */         else
/*     */         {
/* 188 */           throw new UnsupportedCallbackException(paramArrayOfCallback[i], "Unrecognized Callback");
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 194 */     i = JOptionPane.showOptionDialog(this.parentComponent, localArrayList1.toArray(), "Confirmation", localConfirmationInfo.optionType, localConfirmationInfo.messageType, null, localConfirmationInfo.options, localConfirmationInfo.initialValue);
/*     */ 
/* 205 */     if ((i == 0) || (i == 0))
/*     */     {
/* 208 */       localObject1 = localArrayList2.iterator();
/* 209 */       while (((Iterator)localObject1).hasNext()) {
/* 210 */         ((Action)((Iterator)localObject1).next()).perform();
/*     */       }
/*     */     }
/* 213 */     localConfirmationInfo.handleResult(i);
/*     */   }
/*     */ 
/*     */   private static abstract interface Action
/*     */   {
/*     */     public abstract void perform();
/*     */   }
/*     */ 
/*     */   private static class ConfirmationInfo {
/*     */     private int[] translations;
/* 224 */     int optionType = 2;
/* 225 */     Object[] options = null;
/* 226 */     Object initialValue = null;
/*     */ 
/* 228 */     int messageType = 3;
/*     */     private ConfirmationCallback callback;
/*     */ 
/*     */     void setCallback(ConfirmationCallback paramConfirmationCallback) throws UnsupportedCallbackException {
/* 236 */       this.callback = paramConfirmationCallback;
/*     */ 
/* 238 */       int i = paramConfirmationCallback.getOptionType();
/* 239 */       switch (i) {
/*     */       case 0:
/* 241 */         this.optionType = 0;
/* 242 */         this.translations = new int[] { 0, 0, 1, 1, -1, 1 };
/*     */ 
/* 247 */         break;
/*     */       case 1:
/* 249 */         this.optionType = 1;
/* 250 */         this.translations = new int[] { 0, 0, 1, 1, 2, 2, -1, 2 };
/*     */ 
/* 256 */         break;
/*     */       case 2:
/* 258 */         this.optionType = 2;
/* 259 */         this.translations = new int[] { 0, 3, 2, 2, -1, 2 };
/*     */ 
/* 264 */         break;
/*     */       case -1:
/* 266 */         this.options = paramConfirmationCallback.getOptions();
/*     */ 
/* 272 */         this.translations = new int[] { -1, paramConfirmationCallback.getDefaultOption() };
/*     */ 
/* 275 */         break;
/*     */       default:
/* 277 */         throw new UnsupportedCallbackException(paramConfirmationCallback, "Unrecognized option type: " + i);
/*     */       }
/*     */ 
/* 282 */       int j = paramConfirmationCallback.getMessageType();
/* 283 */       switch (j) {
/*     */       case 1:
/* 285 */         this.messageType = 2;
/* 286 */         break;
/*     */       case 2:
/* 288 */         this.messageType = 0;
/* 289 */         break;
/*     */       case 0:
/* 291 */         this.messageType = 1;
/* 292 */         break;
/*     */       default:
/* 294 */         throw new UnsupportedCallbackException(paramConfirmationCallback, "Unrecognized message type: " + j);
/*     */       }
/*     */     }
/*     */ 
/*     */     void handleResult(int paramInt)
/*     */     {
/* 303 */       if (this.callback == null) {
/* 304 */         return;
/*     */       }
/*     */ 
/* 307 */       for (int i = 0; i < this.translations.length; i += 2) {
/* 308 */         if (this.translations[i] == paramInt) {
/* 309 */           paramInt = this.translations[(i + 1)];
/* 310 */           break;
/*     */         }
/*     */       }
/* 313 */       this.callback.setSelectedIndex(paramInt);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.callback.DialogCallbackHandler
 * JD-Core Version:    0.6.2
 */