/*    */ package sun.font;
/*    */ 
/*    */ import java.awt.font.TextAttribute;
/*    */ import java.util.AbstractMap;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ 
/*    */ public final class AttributeMap extends AbstractMap<TextAttribute, Object>
/*    */ {
/*    */   private AttributeValues values;
/*    */   private Map<TextAttribute, Object> delegateMap;
/* 78 */   private static boolean first = false;
/*    */ 
/*    */   public AttributeMap(AttributeValues paramAttributeValues)
/*    */   {
/* 62 */     this.values = paramAttributeValues;
/*    */   }
/*    */ 
/*    */   public Set<Map.Entry<TextAttribute, Object>> entrySet() {
/* 66 */     return delegate().entrySet();
/*    */   }
/*    */ 
/*    */   public Object put(TextAttribute paramTextAttribute, Object paramObject) {
/* 70 */     return delegate().put(paramTextAttribute, paramObject);
/*    */   }
/*    */ 
/*    */   public AttributeValues getValues()
/*    */   {
/* 75 */     return this.values;
/*    */   }
/*    */ 
/*    */   private Map<TextAttribute, Object> delegate()
/*    */   {
/* 80 */     if (this.delegateMap == null) {
/* 81 */       if (first) {
/* 82 */         first = false;
/* 83 */         Thread.dumpStack();
/*    */       }
/* 85 */       this.delegateMap = this.values.toMap(new HashMap(27));
/*    */ 
/* 89 */       this.values = null;
/*    */     }
/*    */ 
/* 92 */     return this.delegateMap;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 96 */     if (this.values != null) {
/* 97 */       return "map of " + this.values.toString();
/*    */     }
/* 99 */     return super.toString();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.AttributeMap
 * JD-Core Version:    0.6.2
 */