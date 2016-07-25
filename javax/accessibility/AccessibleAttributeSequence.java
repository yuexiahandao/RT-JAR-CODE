/*    */ package javax.accessibility;
/*    */ 
/*    */ import javax.swing.text.AttributeSet;
/*    */ 
/*    */ public class AccessibleAttributeSequence
/*    */ {
/*    */   public int startIndex;
/*    */   public int endIndex;
/*    */   public AttributeSet attributes;
/*    */ 
/*    */   public AccessibleAttributeSequence(int paramInt1, int paramInt2, AttributeSet paramAttributeSet)
/*    */   {
/* 73 */     this.startIndex = paramInt1;
/* 74 */     this.endIndex = paramInt2;
/* 75 */     this.attributes = paramAttributeSet;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleAttributeSequence
 * JD-Core Version:    0.6.2
 */