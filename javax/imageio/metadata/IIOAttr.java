/*     */ package javax.imageio.metadata;
/*     */ 
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ class IIOAttr extends IIOMetadataNode
/*     */   implements Attr
/*     */ {
/*     */   Element owner;
/*     */   String name;
/*     */   String value;
/*     */ 
/*     */   public IIOAttr(Element paramElement, String paramString1, String paramString2)
/*     */   {
/* 138 */     this.owner = paramElement;
/* 139 */     this.name = paramString1;
/* 140 */     this.value = paramString2;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 144 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getNodeName() {
/* 148 */     return this.name;
/*     */   }
/*     */ 
/*     */   public short getNodeType() {
/* 152 */     return 2;
/*     */   }
/*     */ 
/*     */   public boolean getSpecified() {
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */   public String getValue() {
/* 160 */     return this.value;
/*     */   }
/*     */ 
/*     */   public String getNodeValue() {
/* 164 */     return this.value;
/*     */   }
/*     */ 
/*     */   public void setValue(String paramString) {
/* 168 */     this.value = paramString;
/*     */   }
/*     */ 
/*     */   public void setNodeValue(String paramString) {
/* 172 */     this.value = paramString;
/*     */   }
/*     */ 
/*     */   public Element getOwnerElement() {
/* 176 */     return this.owner;
/*     */   }
/*     */ 
/*     */   public void setOwnerElement(Element paramElement) {
/* 180 */     this.owner = paramElement;
/*     */   }
/*     */ 
/*     */   public boolean isId()
/*     */   {
/* 192 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.metadata.IIOAttr
 * JD-Core Version:    0.6.2
 */