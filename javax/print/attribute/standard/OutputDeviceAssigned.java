/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.TextSyntax;
/*     */ 
/*     */ public final class OutputDeviceAssigned extends TextSyntax
/*     */   implements PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 5486733778854271081L;
/*     */ 
/*     */   public OutputDeviceAssigned(String paramString, Locale paramLocale)
/*     */   {
/*  69 */     super(paramString, paramLocale);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  97 */     return (super.equals(paramObject)) && ((paramObject instanceof OutputDeviceAssigned));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 112 */     return OutputDeviceAssigned.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 125 */     return "output-device-assigned";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.OutputDeviceAssigned
 * JD-Core Version:    0.6.2
 */