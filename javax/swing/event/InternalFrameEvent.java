/*     */ package javax.swing.event;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import javax.swing.JInternalFrame;
/*     */ 
/*     */ public class InternalFrameEvent extends AWTEvent
/*     */ {
/*     */   public static final int INTERNAL_FRAME_FIRST = 25549;
/*     */   public static final int INTERNAL_FRAME_LAST = 25555;
/*     */   public static final int INTERNAL_FRAME_OPENED = 25549;
/*     */   public static final int INTERNAL_FRAME_CLOSING = 25550;
/*     */   public static final int INTERNAL_FRAME_CLOSED = 25551;
/*     */   public static final int INTERNAL_FRAME_ICONIFIED = 25552;
/*     */   public static final int INTERNAL_FRAME_DEICONIFIED = 25553;
/*     */   public static final int INTERNAL_FRAME_ACTIVATED = 25554;
/*     */   public static final int INTERNAL_FRAME_DEACTIVATED = 25555;
/*     */ 
/*     */   public InternalFrameEvent(JInternalFrame paramJInternalFrame, int paramInt)
/*     */   {
/* 140 */     super(paramJInternalFrame, paramInt);
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/*     */     String str;
/* 151 */     switch (this.id) {
/*     */     case 25549:
/* 153 */       str = "INTERNAL_FRAME_OPENED";
/* 154 */       break;
/*     */     case 25550:
/* 156 */       str = "INTERNAL_FRAME_CLOSING";
/* 157 */       break;
/*     */     case 25551:
/* 159 */       str = "INTERNAL_FRAME_CLOSED";
/* 160 */       break;
/*     */     case 25552:
/* 162 */       str = "INTERNAL_FRAME_ICONIFIED";
/* 163 */       break;
/*     */     case 25553:
/* 165 */       str = "INTERNAL_FRAME_DEICONIFIED";
/* 166 */       break;
/*     */     case 25554:
/* 168 */       str = "INTERNAL_FRAME_ACTIVATED";
/* 169 */       break;
/*     */     case 25555:
/* 171 */       str = "INTERNAL_FRAME_DEACTIVATED";
/* 172 */       break;
/*     */     default:
/* 174 */       str = "unknown type";
/*     */     }
/* 176 */     return str;
/*     */   }
/*     */ 
/*     */   public JInternalFrame getInternalFrame()
/*     */   {
/* 188 */     return (this.source instanceof JInternalFrame) ? (JInternalFrame)this.source : null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.InternalFrameEvent
 * JD-Core Version:    0.6.2
 */