/*     */ package com.sun.imageio.plugins.wbmp;
/*     */ 
/*     */ import com.sun.imageio.plugins.common.I18N;
/*     */ import com.sun.imageio.plugins.common.ImageUtil;
/*     */ import javax.imageio.metadata.IIOMetadata;
/*     */ import javax.imageio.metadata.IIOMetadataNode;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class WBMPMetadata extends IIOMetadata
/*     */ {
/*     */   static final String nativeMetadataFormatName = "javax_imageio_wbmp_1.0";
/*     */   public int wbmpType;
/*     */   public int width;
/*     */   public int height;
/*     */ 
/*     */   public WBMPMetadata()
/*     */   {
/*  53 */     super(true, "javax_imageio_wbmp_1.0", "com.sun.imageio.plugins.wbmp.WBMPMetadataFormat", null, null);
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/*  60 */     return true;
/*     */   }
/*     */ 
/*     */   public Node getAsTree(String paramString) {
/*  64 */     if (paramString.equals("javax_imageio_wbmp_1.0"))
/*  65 */       return getNativeTree();
/*  66 */     if (paramString.equals("javax_imageio_1.0"))
/*     */     {
/*  68 */       return getStandardTree();
/*     */     }
/*  70 */     throw new IllegalArgumentException(I18N.getString("WBMPMetadata0"));
/*     */   }
/*     */ 
/*     */   private Node getNativeTree()
/*     */   {
/*  75 */     IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("javax_imageio_wbmp_1.0");
/*     */ 
/*  78 */     addChildNode(localIIOMetadataNode, "WBMPType", new Integer(this.wbmpType));
/*  79 */     addChildNode(localIIOMetadataNode, "Width", new Integer(this.width));
/*  80 */     addChildNode(localIIOMetadataNode, "Height", new Integer(this.height));
/*     */ 
/*  82 */     return localIIOMetadataNode;
/*     */   }
/*     */ 
/*     */   public void setFromTree(String paramString, Node paramNode) {
/*  86 */     throw new IllegalStateException(I18N.getString("WBMPMetadata1"));
/*     */   }
/*     */ 
/*     */   public void mergeTree(String paramString, Node paramNode) {
/*  90 */     throw new IllegalStateException(I18N.getString("WBMPMetadata1"));
/*     */   }
/*     */ 
/*     */   public void reset() {
/*  94 */     throw new IllegalStateException(I18N.getString("WBMPMetadata1"));
/*     */   }
/*     */ 
/*     */   private IIOMetadataNode addChildNode(IIOMetadataNode paramIIOMetadataNode, String paramString, Object paramObject)
/*     */   {
/* 100 */     IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode(paramString);
/* 101 */     if (paramObject != null) {
/* 102 */       localIIOMetadataNode.setUserObject(paramObject);
/* 103 */       localIIOMetadataNode.setNodeValue(ImageUtil.convertObjectToString(paramObject));
/*     */     }
/* 105 */     paramIIOMetadataNode.appendChild(localIIOMetadataNode);
/* 106 */     return localIIOMetadataNode;
/*     */   }
/*     */ 
/*     */   protected IIOMetadataNode getStandardChromaNode()
/*     */   {
/* 112 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Chroma");
/* 113 */     IIOMetadataNode localIIOMetadataNode2 = new IIOMetadataNode("BlackIsZero");
/* 114 */     localIIOMetadataNode2.setAttribute("value", "TRUE");
/*     */ 
/* 116 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/* 117 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   protected IIOMetadataNode getStandardDimensionNode()
/*     */   {
/* 122 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Dimension");
/* 123 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*     */ 
/* 127 */     localIIOMetadataNode2 = new IIOMetadataNode("ImageOrientation");
/* 128 */     localIIOMetadataNode2.setAttribute("value", "Normal");
/* 129 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 131 */     return localIIOMetadataNode1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.wbmp.WBMPMetadata
 * JD-Core Version:    0.6.2
 */