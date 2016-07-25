/*     */ package javax.xml.bind.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ 
/*     */ public class ValidationEventCollector
/*     */   implements ValidationEventHandler
/*     */ {
/*  52 */   private final List<ValidationEvent> events = new ArrayList();
/*     */ 
/*     */   public ValidationEvent[] getEvents()
/*     */   {
/*  63 */     return (ValidationEvent[])this.events.toArray(new ValidationEvent[this.events.size()]);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*  70 */     this.events.clear();
/*     */   }
/*     */ 
/*     */   public boolean hasEvents()
/*     */   {
/*  81 */     return !this.events.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean handleEvent(ValidationEvent event) {
/*  85 */     this.events.add(event);
/*     */ 
/*  87 */     boolean retVal = true;
/*  88 */     switch (event.getSeverity()) {
/*     */     case 0:
/*  90 */       retVal = true;
/*  91 */       break;
/*     */     case 1:
/*  93 */       retVal = true;
/*  94 */       break;
/*     */     case 2:
/*  96 */       retVal = false;
/*  97 */       break;
/*     */     default:
/*  99 */       _assert(false, Messages.format("ValidationEventCollector.UnrecognizedSeverity", Integer.valueOf(event.getSeverity())));
/*     */     }
/*     */ 
/* 105 */     return retVal;
/*     */   }
/*     */ 
/*     */   private static void _assert(boolean b, String msg) {
/* 109 */     if (!b)
/* 110 */       throw new InternalError(msg);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.util.ValidationEventCollector
 * JD-Core Version:    0.6.2
 */