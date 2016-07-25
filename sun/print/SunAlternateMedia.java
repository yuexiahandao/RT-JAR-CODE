/*    */ package sun.print;
/*    */ 
/*    */ import javax.print.attribute.PrintRequestAttribute;
/*    */ import javax.print.attribute.standard.Media;
/*    */ 
/*    */ public class SunAlternateMedia
/*    */   implements PrintRequestAttribute
/*    */ {
/*    */   private static final long serialVersionUID = -8878868345472850201L;
/*    */   private Media media;
/*    */ 
/*    */   public SunAlternateMedia(Media paramMedia)
/*    */   {
/* 43 */     this.media = paramMedia;
/*    */   }
/*    */ 
/*    */   public Media getMedia() {
/* 47 */     return this.media;
/*    */   }
/*    */ 
/*    */   public final Class getCategory() {
/* 51 */     return SunAlternateMedia.class;
/*    */   }
/*    */ 
/*    */   public final String getName() {
/* 55 */     return "sun-alternate-media";
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 59 */     return "alternate-media: " + this.media.toString();
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 67 */     return this.media.hashCode();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.SunAlternateMedia
 * JD-Core Version:    0.6.2
 */