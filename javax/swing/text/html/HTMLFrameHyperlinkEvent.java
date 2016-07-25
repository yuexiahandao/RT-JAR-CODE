/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.event.InputEvent;
/*     */ import java.net.URL;
/*     */ import javax.swing.event.HyperlinkEvent;
/*     */ import javax.swing.event.HyperlinkEvent.EventType;
/*     */ import javax.swing.text.Element;
/*     */ 
/*     */ public class HTMLFrameHyperlinkEvent extends HyperlinkEvent
/*     */ {
/*     */   private String targetFrame;
/*     */ 
/*     */   public HTMLFrameHyperlinkEvent(Object paramObject, HyperlinkEvent.EventType paramEventType, URL paramURL, String paramString)
/*     */   {
/*  52 */     super(paramObject, paramEventType, paramURL);
/*  53 */     this.targetFrame = paramString;
/*     */   }
/*     */ 
/*     */   public HTMLFrameHyperlinkEvent(Object paramObject, HyperlinkEvent.EventType paramEventType, URL paramURL, String paramString1, String paramString2)
/*     */   {
/*  68 */     super(paramObject, paramEventType, paramURL, paramString1);
/*  69 */     this.targetFrame = paramString2;
/*     */   }
/*     */ 
/*     */   public HTMLFrameHyperlinkEvent(Object paramObject, HyperlinkEvent.EventType paramEventType, URL paramURL, Element paramElement, String paramString)
/*     */   {
/*  84 */     super(paramObject, paramEventType, paramURL, null, paramElement);
/*  85 */     this.targetFrame = paramString;
/*     */   }
/*     */ 
/*     */   public HTMLFrameHyperlinkEvent(Object paramObject, HyperlinkEvent.EventType paramEventType, URL paramURL, String paramString1, Element paramElement, String paramString2)
/*     */   {
/* 102 */     super(paramObject, paramEventType, paramURL, paramString1, paramElement);
/* 103 */     this.targetFrame = paramString2;
/*     */   }
/*     */ 
/*     */   public HTMLFrameHyperlinkEvent(Object paramObject, HyperlinkEvent.EventType paramEventType, URL paramURL, String paramString1, Element paramElement, InputEvent paramInputEvent, String paramString2)
/*     */   {
/* 122 */     super(paramObject, paramEventType, paramURL, paramString1, paramElement, paramInputEvent);
/* 123 */     this.targetFrame = paramString2;
/*     */   }
/*     */ 
/*     */   public String getTarget()
/*     */   {
/* 130 */     return this.targetFrame;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.HTMLFrameHyperlinkEvent
 * JD-Core Version:    0.6.2
 */