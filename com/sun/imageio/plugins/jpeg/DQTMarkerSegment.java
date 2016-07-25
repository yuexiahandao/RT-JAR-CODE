/*     */ package com.sun.imageio.plugins.jpeg;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.imageio.IIOException;
/*     */ import javax.imageio.metadata.IIOInvalidTreeException;
/*     */ import javax.imageio.metadata.IIOMetadataNode;
/*     */ import javax.imageio.plugins.jpeg.JPEGQTable;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ class DQTMarkerSegment extends MarkerSegment
/*     */ {
/*  47 */   List tables = new ArrayList();
/*     */ 
/*     */   DQTMarkerSegment(float paramFloat, boolean paramBoolean) {
/*  50 */     super(219);
/*  51 */     this.tables.add(new Qtable(true, paramFloat));
/*  52 */     if (paramBoolean)
/*  53 */       this.tables.add(new Qtable(false, paramFloat));
/*     */   }
/*     */ 
/*     */   DQTMarkerSegment(JPEGBuffer paramJPEGBuffer) throws IOException
/*     */   {
/*  58 */     super(paramJPEGBuffer);
/*  59 */     int i = this.length;
/*  60 */     while (i > 0) {
/*  61 */       Qtable localQtable = new Qtable(paramJPEGBuffer);
/*  62 */       this.tables.add(localQtable);
/*  63 */       i -= localQtable.data.length + 1;
/*     */     }
/*  65 */     paramJPEGBuffer.bufAvail -= this.length;
/*     */   }
/*     */ 
/*     */   DQTMarkerSegment(JPEGQTable[] paramArrayOfJPEGQTable) {
/*  69 */     super(219);
/*  70 */     for (int i = 0; i < paramArrayOfJPEGQTable.length; i++)
/*  71 */       this.tables.add(new Qtable(paramArrayOfJPEGQTable[i], i));
/*     */   }
/*     */ 
/*     */   DQTMarkerSegment(Node paramNode) throws IIOInvalidTreeException
/*     */   {
/*  76 */     super(219);
/*  77 */     NodeList localNodeList = paramNode.getChildNodes();
/*  78 */     int i = localNodeList.getLength();
/*  79 */     if ((i < 1) || (i > 4)) {
/*  80 */       throw new IIOInvalidTreeException("Invalid DQT node", paramNode);
/*     */     }
/*  82 */     for (int j = 0; j < i; j++)
/*  83 */       this.tables.add(new Qtable(localNodeList.item(j)));
/*     */   }
/*     */ 
/*     */   protected Object clone()
/*     */   {
/*  88 */     DQTMarkerSegment localDQTMarkerSegment = (DQTMarkerSegment)super.clone();
/*  89 */     localDQTMarkerSegment.tables = new ArrayList(this.tables.size());
/*  90 */     Iterator localIterator = this.tables.iterator();
/*  91 */     while (localIterator.hasNext()) {
/*  92 */       Qtable localQtable = (Qtable)localIterator.next();
/*  93 */       localDQTMarkerSegment.tables.add(localQtable.clone());
/*     */     }
/*  95 */     return localDQTMarkerSegment;
/*     */   }
/*     */ 
/*     */   IIOMetadataNode getNativeNode() {
/*  99 */     IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("dqt");
/* 100 */     for (int i = 0; i < this.tables.size(); i++) {
/* 101 */       Qtable localQtable = (Qtable)this.tables.get(i);
/* 102 */       localIIOMetadataNode.appendChild(localQtable.getNativeNode());
/*     */     }
/* 104 */     return localIIOMetadataNode;
/*     */   }
/*     */ 
/*     */   void write(ImageOutputStream paramImageOutputStream)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   void print()
/*     */   {
/* 116 */     printTag("DQT");
/* 117 */     System.out.println("Num tables: " + Integer.toString(this.tables.size()));
/*     */ 
/* 119 */     for (int i = 0; i < this.tables.size(); i++) {
/* 120 */       Qtable localQtable = (Qtable)this.tables.get(i);
/* 121 */       localQtable.print();
/*     */     }
/* 123 */     System.out.println();
/*     */   }
/*     */ 
/*     */   Qtable getChromaForLuma(Qtable paramQtable)
/*     */   {
/* 132 */     Qtable localQtable = null;
/*     */ 
/* 135 */     int i = 1;
/* 136 */     for (int j = 1; ; j++) { paramQtable.getClass(); if (j >= 64) break;
/* 137 */       if (paramQtable.data[j] != paramQtable.data[(j - 1)]) {
/* 138 */         i = 0;
/* 139 */         break;
/*     */       }
/*     */     }
/* 142 */     if (i != 0) {
/* 143 */       localQtable = (Qtable)paramQtable.clone();
/* 144 */       localQtable.tableID = 1;
/*     */     }
/*     */     else
/*     */     {
/* 148 */       j = 0;
/* 149 */       for (int k = 1; ; k++) { paramQtable.getClass(); if (k >= 64) break;
/* 150 */         if (paramQtable.data[k] > paramQtable.data[j]) {
/* 151 */           j = k;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 160 */       float f = paramQtable.data[j] / JPEGQTable.K1Div2Luminance.getTable()[j];
/*     */ 
/* 163 */       JPEGQTable localJPEGQTable = JPEGQTable.K2Div2Chrominance.getScaledInstance(f, true);
/*     */ 
/* 166 */       localQtable = new Qtable(localJPEGQTable, 1);
/*     */     }
/* 168 */     return localQtable;
/*     */   }
/*     */ 
/*     */   Qtable getQtableFromNode(Node paramNode) throws IIOInvalidTreeException {
/* 172 */     return new Qtable(paramNode);
/*     */   }
/*     */ 
/*     */   class Qtable
/*     */     implements Cloneable
/*     */   {
/*     */     int elementPrecision;
/*     */     int tableID;
/* 181 */     final int QTABLE_SIZE = 64;
/*     */     int[] data;
/* 188 */     private final int[] zigzag = { 0, 1, 5, 6, 14, 15, 27, 28, 2, 4, 7, 13, 16, 26, 29, 42, 3, 8, 12, 17, 25, 30, 41, 43, 9, 11, 18, 24, 31, 40, 44, 53, 10, 19, 23, 32, 39, 45, 52, 54, 20, 22, 33, 38, 46, 51, 55, 60, 21, 34, 37, 47, 50, 56, 59, 61, 35, 36, 48, 49, 57, 58, 62, 63 };
/*     */ 
/*     */     Qtable(boolean paramFloat, float arg3)
/*     */     {
/* 200 */       this.elementPrecision = 0;
/* 201 */       JPEGQTable localJPEGQTable = null;
/* 202 */       if (paramFloat != 0) {
/* 203 */         this.tableID = 0;
/* 204 */         localJPEGQTable = JPEGQTable.K1Div2Luminance;
/*     */       } else {
/* 206 */         this.tableID = 1;
/* 207 */         localJPEGQTable = JPEGQTable.K2Div2Chrominance;
/*     */       }
/*     */       Object localObject;
/* 209 */       if (localObject != 0.75F) {
/* 210 */         float f = JPEG.convertToLinearQuality(localObject);
/* 211 */         if (paramFloat != 0) {
/* 212 */           localJPEGQTable = JPEGQTable.K1Luminance.getScaledInstance(f, true);
/*     */         }
/*     */         else {
/* 215 */           localJPEGQTable = JPEGQTable.K2Div2Chrominance.getScaledInstance(f, true);
/*     */         }
/*     */       }
/*     */ 
/* 219 */       this.data = localJPEGQTable.getTable();
/*     */     }
/*     */ 
/*     */     Qtable(JPEGBuffer arg2)
/*     */       throws IIOException
/*     */     {
/*     */       Object localObject;
/* 223 */       this.elementPrecision = (localObject.buf[localObject.bufPtr] >>> 4);
/* 224 */       this.tableID = (localObject.buf[(localObject.bufPtr++)] & 0xF);
/* 225 */       if (this.elementPrecision != 0)
/*     */       {
/* 227 */         throw new IIOException("Unsupported element precision");
/*     */       }
/* 229 */       this.data = new int[64];
/*     */ 
/* 231 */       for (int i = 0; i < 64; i++) {
/* 232 */         this.data[i] = (localObject.buf[(localObject.bufPtr + this.zigzag[i])] & 0xFF);
/*     */       }
/* 234 */       localObject.bufPtr += 64;
/*     */     }
/*     */ 
/*     */     Qtable(JPEGQTable paramInt, int arg3) {
/* 238 */       this.elementPrecision = 0;
/*     */       int i;
/* 239 */       this.tableID = i;
/* 240 */       this.data = paramInt.getTable();
/*     */     }
/*     */ 
/*     */     Qtable(Node arg2)
/*     */       throws IIOInvalidTreeException
/*     */     {
/*     */       Node localNode;
/* 244 */       if (localNode.getNodeName().equals("dqtable")) {
/* 245 */         NamedNodeMap localNamedNodeMap = localNode.getAttributes();
/* 246 */         int i = localNamedNodeMap.getLength();
/* 247 */         if ((i < 1) || (i > 2)) {
/* 248 */           throw new IIOInvalidTreeException("dqtable node must have 1 or 2 attributes", localNode);
/*     */         }
/*     */ 
/* 251 */         this.elementPrecision = 0;
/* 252 */         this.tableID = MarkerSegment.getAttributeValue(localNode, localNamedNodeMap, "qtableId", 0, 3, true);
/* 253 */         if ((localNode instanceof IIOMetadataNode)) {
/* 254 */           IIOMetadataNode localIIOMetadataNode = (IIOMetadataNode)localNode;
/* 255 */           JPEGQTable localJPEGQTable = (JPEGQTable)localIIOMetadataNode.getUserObject();
/* 256 */           if (localJPEGQTable == null) {
/* 257 */             throw new IIOInvalidTreeException("dqtable node must have user object", localNode);
/*     */           }
/*     */ 
/* 260 */           this.data = localJPEGQTable.getTable();
/*     */         } else {
/* 262 */           throw new IIOInvalidTreeException("dqtable node must have user object", localNode);
/*     */         }
/*     */       }
/*     */       else {
/* 266 */         throw new IIOInvalidTreeException("Invalid node, expected dqtable", localNode);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected Object clone()
/*     */     {
/* 272 */       Qtable localQtable = null;
/*     */       try {
/* 274 */         localQtable = (Qtable)super.clone(); } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */       }
/* 276 */       if (this.data != null) {
/* 277 */         localQtable.data = ((int[])this.data.clone());
/*     */       }
/* 279 */       return localQtable;
/*     */     }
/*     */ 
/*     */     IIOMetadataNode getNativeNode() {
/* 283 */       IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("dqtable");
/* 284 */       localIIOMetadataNode.setAttribute("elementPrecision", Integer.toString(this.elementPrecision));
/*     */ 
/* 286 */       localIIOMetadataNode.setAttribute("qtableId", Integer.toString(this.tableID));
/*     */ 
/* 288 */       localIIOMetadataNode.setUserObject(new JPEGQTable(this.data));
/* 289 */       return localIIOMetadataNode;
/*     */     }
/*     */ 
/*     */     void print() {
/* 293 */       System.out.println("Table id: " + Integer.toString(this.tableID));
/* 294 */       System.out.println("Element precision: " + Integer.toString(this.elementPrecision));
/*     */ 
/* 297 */       new JPEGQTable(this.data).toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.DQTMarkerSegment
 * JD-Core Version:    0.6.2
 */