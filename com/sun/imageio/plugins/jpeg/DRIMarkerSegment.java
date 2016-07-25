/*    */ package com.sun.imageio.plugins.jpeg;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import javax.imageio.metadata.IIOInvalidTreeException;
/*    */ import javax.imageio.metadata.IIOMetadataNode;
/*    */ import javax.imageio.stream.ImageOutputStream;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ class DRIMarkerSegment extends MarkerSegment
/*    */ {
/* 43 */   int restartInterval = 0;
/*    */ 
/*    */   DRIMarkerSegment(JPEGBuffer paramJPEGBuffer) throws IOException
/*    */   {
/* 47 */     super(paramJPEGBuffer);
/* 48 */     this.restartInterval = ((paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF) << 8);
/* 49 */     this.restartInterval |= paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF;
/* 50 */     paramJPEGBuffer.bufAvail -= this.length;
/*    */   }
/*    */ 
/*    */   DRIMarkerSegment(Node paramNode) throws IIOInvalidTreeException {
/* 54 */     super(221);
/* 55 */     updateFromNativeNode(paramNode, true);
/*    */   }
/*    */ 
/*    */   IIOMetadataNode getNativeNode() {
/* 59 */     IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("dri");
/* 60 */     localIIOMetadataNode.setAttribute("interval", Integer.toString(this.restartInterval));
/* 61 */     return localIIOMetadataNode;
/*    */   }
/*    */ 
/*    */   void updateFromNativeNode(Node paramNode, boolean paramBoolean) throws IIOInvalidTreeException
/*    */   {
/* 66 */     this.restartInterval = getAttributeValue(paramNode, null, "interval", 0, 65535, true);
/*    */   }
/*    */ 
/*    */   void write(ImageOutputStream paramImageOutputStream)
/*    */     throws IOException
/*    */   {
/*    */   }
/*    */ 
/*    */   void print()
/*    */   {
/* 79 */     printTag("DRI");
/* 80 */     System.out.println("Interval: " + Integer.toString(this.restartInterval));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.DRIMarkerSegment
 * JD-Core Version:    0.6.2
 */