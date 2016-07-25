/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.InputEvent;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.io.Serializable;
/*     */ import java.util.EventListener;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.event.EventListenerList;
/*     */ 
/*     */ public class DefaultButtonModel
/*     */   implements ButtonModel, Serializable
/*     */ {
/*  51 */   protected int stateMask = 0;
/*     */ 
/*  54 */   protected String actionCommand = null;
/*     */ 
/*  57 */   protected ButtonGroup group = null;
/*     */ 
/*  60 */   protected int mnemonic = 0;
/*     */ 
/*  67 */   protected transient ChangeEvent changeEvent = null;
/*     */ 
/*  70 */   protected EventListenerList listenerList = new EventListenerList();
/*     */ 
/*  74 */   private boolean menuItem = false;
/*     */   public static final int ARMED = 1;
/*     */   public static final int SELECTED = 2;
/*     */   public static final int PRESSED = 4;
/*     */   public static final int ENABLED = 8;
/*     */   public static final int ROLLOVER = 16;
/*     */ 
/*     */   public DefaultButtonModel()
/*     */   {
/*  81 */     this.stateMask = 0;
/*  82 */     setEnabled(true);
/*     */   }
/*     */ 
/*     */   public void setActionCommand(String paramString)
/*     */   {
/* 122 */     this.actionCommand = paramString;
/*     */   }
/*     */ 
/*     */   public String getActionCommand()
/*     */   {
/* 129 */     return this.actionCommand;
/*     */   }
/*     */ 
/*     */   public boolean isArmed()
/*     */   {
/* 136 */     return (this.stateMask & 0x1) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isSelected()
/*     */   {
/* 143 */     return (this.stateMask & 0x2) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isEnabled()
/*     */   {
/* 150 */     return (this.stateMask & 0x8) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isPressed()
/*     */   {
/* 157 */     return (this.stateMask & 0x4) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isRollover()
/*     */   {
/* 164 */     return (this.stateMask & 0x10) != 0;
/*     */   }
/*     */ 
/*     */   public void setArmed(boolean paramBoolean)
/*     */   {
/* 171 */     if ((isMenuItem()) && (UIManager.getBoolean("MenuItem.disabledAreNavigable")))
/*     */     {
/* 173 */       if (isArmed() != paramBoolean);
/*     */     }
/* 177 */     else if ((isArmed() == paramBoolean) || (!isEnabled())) {
/* 178 */       return;
/*     */     }
/*     */ 
/* 182 */     if (paramBoolean)
/* 183 */       this.stateMask |= 1;
/*     */     else {
/* 185 */       this.stateMask &= -2;
/*     */     }
/*     */ 
/* 188 */     fireStateChanged();
/*     */   }
/*     */ 
/*     */   public void setEnabled(boolean paramBoolean)
/*     */   {
/* 195 */     if (isEnabled() == paramBoolean) {
/* 196 */       return;
/*     */     }
/*     */ 
/* 199 */     if (paramBoolean) {
/* 200 */       this.stateMask |= 8;
/*     */     } else {
/* 202 */       this.stateMask &= -9;
/*     */ 
/* 204 */       this.stateMask &= -2;
/* 205 */       this.stateMask &= -5;
/*     */     }
/*     */ 
/* 209 */     fireStateChanged();
/*     */   }
/*     */ 
/*     */   public void setSelected(boolean paramBoolean)
/*     */   {
/* 216 */     if (isSelected() == paramBoolean) {
/* 217 */       return;
/*     */     }
/*     */ 
/* 220 */     if (paramBoolean)
/* 221 */       this.stateMask |= 2;
/*     */     else {
/* 223 */       this.stateMask &= -3;
/*     */     }
/*     */ 
/* 226 */     fireItemStateChanged(new ItemEvent(this, 701, this, paramBoolean ? 1 : 2));
/*     */ 
/* 232 */     fireStateChanged();
/*     */   }
/*     */ 
/*     */   public void setPressed(boolean paramBoolean)
/*     */   {
/* 241 */     if ((isPressed() == paramBoolean) || (!isEnabled())) {
/* 242 */       return;
/*     */     }
/*     */ 
/* 245 */     if (paramBoolean)
/* 246 */       this.stateMask |= 4;
/*     */     else {
/* 248 */       this.stateMask &= -5;
/*     */     }
/*     */ 
/* 251 */     if ((!isPressed()) && (isArmed())) {
/* 252 */       int i = 0;
/* 253 */       AWTEvent localAWTEvent = EventQueue.getCurrentEvent();
/* 254 */       if ((localAWTEvent instanceof InputEvent))
/* 255 */         i = ((InputEvent)localAWTEvent).getModifiers();
/* 256 */       else if ((localAWTEvent instanceof ActionEvent)) {
/* 257 */         i = ((ActionEvent)localAWTEvent).getModifiers();
/*     */       }
/* 259 */       fireActionPerformed(new ActionEvent(this, 1001, getActionCommand(), EventQueue.getMostRecentEventTime(), i));
/*     */     }
/*     */ 
/* 266 */     fireStateChanged();
/*     */   }
/*     */ 
/*     */   public void setRollover(boolean paramBoolean)
/*     */   {
/* 273 */     if ((isRollover() == paramBoolean) || (!isEnabled())) {
/* 274 */       return;
/*     */     }
/*     */ 
/* 277 */     if (paramBoolean)
/* 278 */       this.stateMask |= 16;
/*     */     else {
/* 280 */       this.stateMask &= -17;
/*     */     }
/*     */ 
/* 283 */     fireStateChanged();
/*     */   }
/*     */ 
/*     */   public void setMnemonic(int paramInt)
/*     */   {
/* 290 */     this.mnemonic = paramInt;
/* 291 */     fireStateChanged();
/*     */   }
/*     */ 
/*     */   public int getMnemonic()
/*     */   {
/* 298 */     return this.mnemonic;
/*     */   }
/*     */ 
/*     */   public void addChangeListener(ChangeListener paramChangeListener)
/*     */   {
/* 305 */     this.listenerList.add(ChangeListener.class, paramChangeListener);
/*     */   }
/*     */ 
/*     */   public void removeChangeListener(ChangeListener paramChangeListener)
/*     */   {
/* 312 */     this.listenerList.remove(ChangeListener.class, paramChangeListener);
/*     */   }
/*     */ 
/*     */   public ChangeListener[] getChangeListeners()
/*     */   {
/* 329 */     return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireStateChanged()
/*     */   {
/* 341 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 344 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 345 */       if (arrayOfObject[i] == ChangeListener.class)
/*     */       {
/* 347 */         if (this.changeEvent == null)
/* 348 */           this.changeEvent = new ChangeEvent(this);
/* 349 */         ((ChangeListener)arrayOfObject[(i + 1)]).stateChanged(this.changeEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void addActionListener(ActionListener paramActionListener)
/*     */   {
/* 358 */     this.listenerList.add(ActionListener.class, paramActionListener);
/*     */   }
/*     */ 
/*     */   public void removeActionListener(ActionListener paramActionListener)
/*     */   {
/* 365 */     this.listenerList.remove(ActionListener.class, paramActionListener);
/*     */   }
/*     */ 
/*     */   public ActionListener[] getActionListeners()
/*     */   {
/* 382 */     return (ActionListener[])this.listenerList.getListeners(ActionListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireActionPerformed(ActionEvent paramActionEvent)
/*     */   {
/* 394 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 397 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 398 */       if (arrayOfObject[i] == ActionListener.class)
/*     */       {
/* 402 */         ((ActionListener)arrayOfObject[(i + 1)]).actionPerformed(paramActionEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void addItemListener(ItemListener paramItemListener)
/*     */   {
/* 411 */     this.listenerList.add(ItemListener.class, paramItemListener);
/*     */   }
/*     */ 
/*     */   public void removeItemListener(ItemListener paramItemListener)
/*     */   {
/* 418 */     this.listenerList.remove(ItemListener.class, paramItemListener);
/*     */   }
/*     */ 
/*     */   public ItemListener[] getItemListeners()
/*     */   {
/* 435 */     return (ItemListener[])this.listenerList.getListeners(ItemListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireItemStateChanged(ItemEvent paramItemEvent)
/*     */   {
/* 447 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 450 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 451 */       if (arrayOfObject[i] == ItemListener.class)
/*     */       {
/* 455 */         ((ItemListener)arrayOfObject[(i + 1)]).itemStateChanged(paramItemEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 498 */     return this.listenerList.getListeners(paramClass);
/*     */   }
/*     */ 
/*     */   public Object[] getSelectedObjects()
/*     */   {
/* 503 */     return null;
/*     */   }
/*     */ 
/*     */   public void setGroup(ButtonGroup paramButtonGroup)
/*     */   {
/* 510 */     this.group = paramButtonGroup;
/*     */   }
/*     */ 
/*     */   public ButtonGroup getGroup()
/*     */   {
/* 523 */     return this.group;
/*     */   }
/*     */ 
/*     */   boolean isMenuItem() {
/* 527 */     return this.menuItem;
/*     */   }
/*     */ 
/*     */   void setMenuItem(boolean paramBoolean) {
/* 531 */     this.menuItem = paramBoolean;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DefaultButtonModel
 * JD-Core Version:    0.6.2
 */