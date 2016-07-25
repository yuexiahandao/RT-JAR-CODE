/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.net.URI;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ import javax.print.attribute.URISyntax;
/*     */ 
/*     */ public final class Destination extends URISyntax
/*     */   implements PrintJobAttribute, PrintRequestAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 6776739171700415321L;
/*     */ 
/*     */   public Destination(URI paramURI)
/*     */   {
/*  71 */     super(paramURI);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  94 */     return (super.equals(paramObject)) && ((paramObject instanceof Destination));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 108 */     return Destination.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 120 */     return "spool-data-destination";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.Destination
 * JD-Core Version:    0.6.2
 */