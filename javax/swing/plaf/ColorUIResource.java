/*    */ package javax.swing.plaf;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.beans.ConstructorProperties;
/*    */ 
/*    */ public class ColorUIResource extends Color
/*    */   implements UIResource
/*    */ {
/*    */   @ConstructorProperties({"red", "green", "blue"})
/*    */   public ColorUIResource(int paramInt1, int paramInt2, int paramInt3)
/*    */   {
/* 52 */     super(paramInt1, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   public ColorUIResource(int paramInt) {
/* 56 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   public ColorUIResource(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 60 */     super(paramFloat1, paramFloat2, paramFloat3);
/*    */   }
/*    */ 
/*    */   public ColorUIResource(Color paramColor) {
/* 64 */     super(paramColor.getRGB(), (paramColor.getRGB() & 0xFF000000) != -16777216);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.ColorUIResource
 * JD-Core Version:    0.6.2
 */