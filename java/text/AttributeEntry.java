/*      */ package java.text;
/*      */ 
/*      */ import java.util.Map.Entry;
/*      */ 
/*      */ class AttributeEntry
/*      */   implements Map.Entry
/*      */ {
/*      */   private AttributedCharacterIterator.Attribute key;
/*      */   private Object value;
/*      */ 
/*      */   AttributeEntry(AttributedCharacterIterator.Attribute paramAttribute, Object paramObject)
/*      */   {
/* 1088 */     this.key = paramAttribute;
/* 1089 */     this.value = paramObject;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject) {
/* 1093 */     if (!(paramObject instanceof AttributeEntry)) {
/* 1094 */       return false;
/*      */     }
/* 1096 */     AttributeEntry localAttributeEntry = (AttributeEntry)paramObject;
/* 1097 */     return (localAttributeEntry.key.equals(this.key)) && (this.value == null ? localAttributeEntry.value == null : localAttributeEntry.value.equals(this.value));
/*      */   }
/*      */ 
/*      */   public Object getKey()
/*      */   {
/* 1102 */     return this.key;
/*      */   }
/*      */ 
/*      */   public Object getValue() {
/* 1106 */     return this.value;
/*      */   }
/*      */ 
/*      */   public Object setValue(Object paramObject) {
/* 1110 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public int hashCode() {
/* 1114 */     return this.key.hashCode() ^ (this.value == null ? 0 : this.value.hashCode());
/*      */   }
/*      */ 
/*      */   public String toString() {
/* 1118 */     return this.key.toString() + "=" + this.value.toString();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.AttributeEntry
 * JD-Core Version:    0.6.2
 */