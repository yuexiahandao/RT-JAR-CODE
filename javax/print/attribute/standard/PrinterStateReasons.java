/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.AbstractSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ 
/*     */ public final class PrinterStateReasons extends HashMap<PrinterStateReason, Severity>
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -3731791085163619457L;
/*     */ 
/*     */   public PrinterStateReasons()
/*     */   {
/*     */   }
/*     */ 
/*     */   public PrinterStateReasons(int paramInt)
/*     */   {
/* 109 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public PrinterStateReasons(int paramInt, float paramFloat)
/*     */   {
/* 123 */     super(paramInt, paramFloat);
/*     */   }
/*     */ 
/*     */   public PrinterStateReasons(Map<PrinterStateReason, Severity> paramMap)
/*     */   {
/* 146 */     this();
/* 147 */     for (Map.Entry localEntry : paramMap.entrySet())
/* 148 */       put((PrinterStateReason)localEntry.getKey(), (Severity)localEntry.getValue());
/*     */   }
/*     */ 
/*     */   public Severity put(PrinterStateReason paramPrinterStateReason, Severity paramSeverity)
/*     */   {
/* 177 */     if (paramPrinterStateReason == null) {
/* 178 */       throw new NullPointerException("reason is null");
/*     */     }
/* 180 */     if (paramSeverity == null) {
/* 181 */       throw new NullPointerException("severity is null");
/*     */     }
/* 183 */     return (Severity)super.put(paramPrinterStateReason, paramSeverity);
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 198 */     return PrinterStateReasons.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 211 */     return "printer-state-reasons";
/*     */   }
/*     */ 
/*     */   public Set<PrinterStateReason> printerStateReasonSet(Severity paramSeverity)
/*     */   {
/* 236 */     if (paramSeverity == null) {
/* 237 */       throw new NullPointerException("severity is null");
/*     */     }
/* 239 */     return new PrinterStateReasonSet(paramSeverity, entrySet());
/*     */   }
/*     */ 
/*     */   private class PrinterStateReasonSet extends AbstractSet<PrinterStateReason>
/*     */   {
/*     */     private Severity mySeverity;
/*     */     private Set myEntrySet;
/*     */ 
/*     */     public PrinterStateReasonSet(Severity paramSet, Set arg3) {
/* 249 */       this.mySeverity = paramSet;
/*     */       Object localObject;
/* 250 */       this.myEntrySet = localObject;
/*     */     }
/*     */ 
/*     */     public int size() {
/* 254 */       int i = 0;
/* 255 */       Iterator localIterator = iterator();
/* 256 */       while (localIterator.hasNext()) {
/* 257 */         localIterator.next();
/* 258 */         i++;
/*     */       }
/* 260 */       return i;
/*     */     }
/*     */ 
/*     */     public Iterator iterator() {
/* 264 */       return new PrinterStateReasons.PrinterStateReasonSetIterator(PrinterStateReasons.this, this.mySeverity, this.myEntrySet.iterator());
/*     */     }
/*     */   }
/*     */ 
/*     */   private class PrinterStateReasonSetIterator implements Iterator {
/*     */     private Severity mySeverity;
/*     */     private Iterator myIterator;
/*     */     private Map.Entry myEntry;
/*     */ 
/*     */     public PrinterStateReasonSetIterator(Severity paramIterator, Iterator arg3) {
/* 276 */       this.mySeverity = paramIterator;
/*     */       Object localObject;
/* 277 */       this.myIterator = localObject;
/* 278 */       goToNext();
/*     */     }
/*     */ 
/*     */     private void goToNext() {
/* 282 */       this.myEntry = null;
/* 283 */       while ((this.myEntry == null) && (this.myIterator.hasNext())) {
/* 284 */         this.myEntry = ((Map.Entry)this.myIterator.next());
/* 285 */         if ((Severity)this.myEntry.getValue() != this.mySeverity)
/* 286 */           this.myEntry = null;
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 292 */       return this.myEntry != null;
/*     */     }
/*     */ 
/*     */     public Object next() {
/* 296 */       if (this.myEntry == null) {
/* 297 */         throw new NoSuchElementException();
/*     */       }
/* 299 */       Object localObject = this.myEntry.getKey();
/* 300 */       goToNext();
/* 301 */       return localObject;
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 305 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PrinterStateReasons
 * JD-Core Version:    0.6.2
 */