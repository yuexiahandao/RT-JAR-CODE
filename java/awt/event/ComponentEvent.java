/*     */ package java.awt.event;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Component;
/*     */ import java.awt.Rectangle;
/*     */ 
/*     */ public class ComponentEvent extends AWTEvent
/*     */ {
/*     */   public static final int COMPONENT_FIRST = 100;
/*     */   public static final int COMPONENT_LAST = 103;
/*     */   public static final int COMPONENT_MOVED = 100;
/*     */   public static final int COMPONENT_RESIZED = 101;
/*     */   public static final int COMPONENT_SHOWN = 102;
/*     */   public static final int COMPONENT_HIDDEN = 103;
/*     */   private static final long serialVersionUID = 8101406823902992965L;
/*     */ 
/*     */   public ComponentEvent(Component paramComponent, int paramInt)
/*     */   {
/* 119 */     super(paramComponent, paramInt);
/*     */   }
/*     */ 
/*     */   public Component getComponent()
/*     */   {
/* 130 */     return (this.source instanceof Component) ? (Component)this.source : null;
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/* 141 */     Object localObject = this.source != null ? ((Component)this.source).getBounds() : null;
/*     */     String str;
/* 145 */     switch (this.id) {
/*     */     case 102:
/* 147 */       str = "COMPONENT_SHOWN";
/* 148 */       break;
/*     */     case 103:
/* 150 */       str = "COMPONENT_HIDDEN";
/* 151 */       break;
/*     */     case 100:
/* 153 */       str = "COMPONENT_MOVED (" + localObject.x + "," + localObject.y + " " + localObject.width + "x" + localObject.height + ")";
/*     */ 
/* 155 */       break;
/*     */     case 101:
/* 157 */       str = "COMPONENT_RESIZED (" + localObject.x + "," + localObject.y + " " + localObject.width + "x" + localObject.height + ")";
/*     */ 
/* 159 */       break;
/*     */     default:
/* 161 */       str = "unknown type";
/*     */     }
/* 163 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.ComponentEvent
 * JD-Core Version:    0.6.2
 */