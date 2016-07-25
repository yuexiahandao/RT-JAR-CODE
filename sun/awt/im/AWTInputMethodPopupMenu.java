/*     */ package sun.awt.im;
/*     */ 
/*     */ import java.awt.CheckboxMenuItem;
/*     */ import java.awt.Component;
/*     */ import java.awt.Menu;
/*     */ import java.awt.MenuItem;
/*     */ import java.awt.PopupMenu;
/*     */ 
/*     */ class AWTInputMethodPopupMenu extends InputMethodPopupMenu
/*     */ {
/* 225 */   static PopupMenu delegate = null;
/*     */ 
/*     */   AWTInputMethodPopupMenu(String paramString) {
/* 228 */     synchronized (this) {
/* 229 */       if (delegate == null)
/* 230 */         delegate = new PopupMenu(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   void show(Component paramComponent, int paramInt1, int paramInt2)
/*     */   {
/* 236 */     delegate.show(paramComponent, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   void removeAll() {
/* 240 */     delegate.removeAll();
/*     */   }
/*     */ 
/*     */   void addSeparator() {
/* 244 */     delegate.addSeparator();
/*     */   }
/*     */ 
/*     */   void addToComponent(Component paramComponent) {
/* 248 */     paramComponent.add(delegate);
/*     */   }
/*     */ 
/*     */   Object createSubmenu(String paramString) {
/* 252 */     return new Menu(paramString);
/*     */   }
/*     */ 
/*     */   void add(Object paramObject) {
/* 256 */     delegate.add((MenuItem)paramObject);
/*     */   }
/*     */ 
/*     */   void addMenuItem(String paramString1, String paramString2, String paramString3) {
/* 260 */     addMenuItem(delegate, paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   void addMenuItem(Object paramObject, String paramString1, String paramString2, String paramString3)
/*     */   {
/*     */     Object localObject;
/* 265 */     if (isSelected(paramString2, paramString3))
/* 266 */       localObject = new CheckboxMenuItem(paramString1, true);
/*     */     else {
/* 268 */       localObject = new MenuItem(paramString1);
/*     */     }
/* 270 */     ((MenuItem)localObject).setActionCommand(paramString2);
/* 271 */     ((MenuItem)localObject).addActionListener(this);
/* 272 */     ((MenuItem)localObject).setEnabled(paramString2 != null);
/* 273 */     ((Menu)paramObject).add((MenuItem)localObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.im.AWTInputMethodPopupMenu
 * JD-Core Version:    0.6.2
 */