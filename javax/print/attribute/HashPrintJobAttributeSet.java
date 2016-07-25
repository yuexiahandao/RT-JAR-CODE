/*    */ package javax.print.attribute;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class HashPrintJobAttributeSet extends HashAttributeSet
/*    */   implements PrintJobAttributeSet, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -4204473656070350348L;
/*    */ 
/*    */   public HashPrintJobAttributeSet()
/*    */   {
/* 49 */     super(PrintJobAttribute.class);
/*    */   }
/*    */ 
/*    */   public HashPrintJobAttributeSet(PrintJobAttribute paramPrintJobAttribute)
/*    */   {
/* 62 */     super(paramPrintJobAttribute, PrintJobAttribute.class);
/*    */   }
/*    */ 
/*    */   public HashPrintJobAttributeSet(PrintJobAttribute[] paramArrayOfPrintJobAttribute)
/*    */   {
/* 81 */     super(paramArrayOfPrintJobAttribute, PrintJobAttribute.class);
/*    */   }
/*    */ 
/*    */   public HashPrintJobAttributeSet(PrintJobAttributeSet paramPrintJobAttributeSet)
/*    */   {
/* 98 */     super(paramPrintJobAttributeSet, PrintJobAttribute.class);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.HashPrintJobAttributeSet
 * JD-Core Version:    0.6.2
 */