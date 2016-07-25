/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.peer.LabelPeer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ 
/*     */ public class Label extends Component
/*     */   implements Accessible
/*     */ {
/*     */   public static final int LEFT = 0;
/*     */   public static final int CENTER = 1;
/*     */   public static final int RIGHT = 2;
/*     */   String text;
/*  99 */   int alignment = 0;
/*     */   private static final String base = "label";
/* 102 */   private static int nameCounter = 0;
/*     */   private static final long serialVersionUID = 3094126758329070636L;
/*     */ 
/*     */   public Label()
/*     */     throws HeadlessException
/*     */   {
/* 117 */     this("", 0);
/*     */   }
/*     */ 
/*     */   public Label(String paramString)
/*     */     throws HeadlessException
/*     */   {
/* 132 */     this(paramString, 0);
/*     */   }
/*     */ 
/*     */   public Label(String paramString, int paramInt)
/*     */     throws HeadlessException
/*     */   {
/* 150 */     GraphicsEnvironment.checkHeadless();
/* 151 */     this.text = paramString;
/* 152 */     setAlignment(paramInt);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException, HeadlessException
/*     */   {
/* 166 */     GraphicsEnvironment.checkHeadless();
/* 167 */     paramObjectInputStream.defaultReadObject();
/*     */   }
/*     */ 
/*     */   String constructComponentName()
/*     */   {
/* 175 */     synchronized (Label.class) {
/* 176 */       return "label" + nameCounter++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 186 */     synchronized (getTreeLock()) {
/* 187 */       if (this.peer == null)
/* 188 */         this.peer = getToolkit().createLabel(this);
/* 189 */       super.addNotify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getAlignment()
/*     */   {
/* 200 */     return this.alignment;
/*     */   }
/*     */ 
/*     */   public synchronized void setAlignment(int paramInt)
/*     */   {
/* 213 */     switch (paramInt) {
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/* 217 */       this.alignment = paramInt;
/* 218 */       LabelPeer localLabelPeer = (LabelPeer)this.peer;
/* 219 */       if (localLabelPeer != null) {
/* 220 */         localLabelPeer.setAlignment(paramInt);
/*     */       }
/* 222 */       return;
/*     */     }
/* 224 */     throw new IllegalArgumentException("improper alignment: " + paramInt);
/*     */   }
/*     */ 
/*     */   public String getText()
/*     */   {
/* 234 */     return this.text;
/*     */   }
/*     */ 
/*     */   public void setText(String paramString)
/*     */   {
/* 246 */     int i = 0;
/* 247 */     synchronized (this) {
/* 248 */       if ((paramString != this.text) && ((this.text == null) || (!this.text.equals(paramString))))
/*     */       {
/* 250 */         this.text = paramString;
/* 251 */         LabelPeer localLabelPeer = (LabelPeer)this.peer;
/* 252 */         if (localLabelPeer != null) {
/* 253 */           localLabelPeer.setText(paramString);
/*     */         }
/* 255 */         i = 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 260 */     if (i != 0)
/* 261 */       invalidateIfValid();
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 275 */     String str = ",align=";
/* 276 */     switch (this.alignment) { case 0:
/* 277 */       str = str + "left"; break;
/*     */     case 1:
/* 278 */       str = str + "center"; break;
/*     */     case 2:
/* 279 */       str = str + "right";
/*     */     }
/* 281 */     return super.paramString() + str + ",text=" + this.text;
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 306 */     if (this.accessibleContext == null) {
/* 307 */       this.accessibleContext = new AccessibleAWTLabel();
/*     */     }
/* 309 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  58 */     Toolkit.loadLibraries();
/*  59 */     if (!GraphicsEnvironment.isHeadless())
/*  60 */       initIDs();
/*     */   }
/*     */ 
/*     */   protected class AccessibleAWTLabel extends Component.AccessibleAWTComponent
/*     */   {
/*     */     private static final long serialVersionUID = -3568967560160480438L;
/*     */ 
/*     */     public AccessibleAWTLabel()
/*     */     {
/* 326 */       super();
/*     */     }
/*     */ 
/*     */     public String getAccessibleName()
/*     */     {
/* 337 */       if (this.accessibleName != null) {
/* 338 */         return this.accessibleName;
/*     */       }
/* 340 */       if (Label.this.getText() == null) {
/* 341 */         return super.getAccessibleName();
/*     */       }
/* 343 */       return Label.this.getText();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 355 */       return AccessibleRole.LABEL;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Label
 * JD-Core Version:    0.6.2
 */