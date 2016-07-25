/*     */ package com.sun.imageio.plugins.jpeg;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.imageio.metadata.IIOInvalidTreeException;
/*     */ import javax.imageio.metadata.IIOMetadataNode;
/*     */ import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ class DHTMarkerSegment extends MarkerSegment
/*     */ {
/*  46 */   List tables = new ArrayList();
/*     */ 
/*     */   DHTMarkerSegment(boolean paramBoolean) {
/*  49 */     super(196);
/*  50 */     this.tables.add(new Htable(JPEGHuffmanTable.StdDCLuminance, true, 0));
/*  51 */     if (paramBoolean) {
/*  52 */       this.tables.add(new Htable(JPEGHuffmanTable.StdDCChrominance, true, 1));
/*     */     }
/*  54 */     this.tables.add(new Htable(JPEGHuffmanTable.StdACLuminance, false, 0));
/*  55 */     if (paramBoolean)
/*  56 */       this.tables.add(new Htable(JPEGHuffmanTable.StdACChrominance, false, 1));
/*     */   }
/*     */ 
/*     */   DHTMarkerSegment(JPEGBuffer paramJPEGBuffer) throws IOException
/*     */   {
/*  61 */     super(paramJPEGBuffer);
/*  62 */     int i = this.length;
/*  63 */     while (i > 0) {
/*  64 */       Htable localHtable = new Htable(paramJPEGBuffer);
/*  65 */       this.tables.add(localHtable);
/*  66 */       i -= 17 + localHtable.values.length;
/*     */     }
/*  68 */     paramJPEGBuffer.bufAvail -= this.length;
/*     */   }
/*     */ 
/*     */   DHTMarkerSegment(JPEGHuffmanTable[] paramArrayOfJPEGHuffmanTable1, JPEGHuffmanTable[] paramArrayOfJPEGHuffmanTable2)
/*     */   {
/*  73 */     super(196);
/*  74 */     for (int i = 0; i < paramArrayOfJPEGHuffmanTable1.length; i++) {
/*  75 */       this.tables.add(new Htable(paramArrayOfJPEGHuffmanTable1[i], true, i));
/*     */     }
/*  77 */     for (i = 0; i < paramArrayOfJPEGHuffmanTable2.length; i++)
/*  78 */       this.tables.add(new Htable(paramArrayOfJPEGHuffmanTable2[i], false, i));
/*     */   }
/*     */ 
/*     */   DHTMarkerSegment(Node paramNode) throws IIOInvalidTreeException
/*     */   {
/*  83 */     super(196);
/*  84 */     NodeList localNodeList = paramNode.getChildNodes();
/*  85 */     int i = localNodeList.getLength();
/*  86 */     if ((i < 1) || (i > 4)) {
/*  87 */       throw new IIOInvalidTreeException("Invalid DHT node", paramNode);
/*     */     }
/*  89 */     for (int j = 0; j < i; j++)
/*  90 */       this.tables.add(new Htable(localNodeList.item(j)));
/*     */   }
/*     */ 
/*     */   protected Object clone()
/*     */   {
/*  95 */     DHTMarkerSegment localDHTMarkerSegment = (DHTMarkerSegment)super.clone();
/*  96 */     localDHTMarkerSegment.tables = new ArrayList(this.tables.size());
/*  97 */     Iterator localIterator = this.tables.iterator();
/*  98 */     while (localIterator.hasNext()) {
/*  99 */       Htable localHtable = (Htable)localIterator.next();
/* 100 */       localDHTMarkerSegment.tables.add(localHtable.clone());
/*     */     }
/* 102 */     return localDHTMarkerSegment;
/*     */   }
/*     */ 
/*     */   IIOMetadataNode getNativeNode() {
/* 106 */     IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("dht");
/* 107 */     for (int i = 0; i < this.tables.size(); i++) {
/* 108 */       Htable localHtable = (Htable)this.tables.get(i);
/* 109 */       localIIOMetadataNode.appendChild(localHtable.getNativeNode());
/*     */     }
/* 111 */     return localIIOMetadataNode;
/*     */   }
/*     */ 
/*     */   void write(ImageOutputStream paramImageOutputStream)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   void print()
/*     */   {
/* 123 */     printTag("DHT");
/* 124 */     System.out.println("Num tables: " + Integer.toString(this.tables.size()));
/*     */ 
/* 126 */     for (int i = 0; i < this.tables.size(); i++) {
/* 127 */       Htable localHtable = (Htable)this.tables.get(i);
/* 128 */       localHtable.print();
/*     */     }
/* 130 */     System.out.println();
/*     */   }
/*     */ 
/*     */   Htable getHtableFromNode(Node paramNode) throws IIOInvalidTreeException
/*     */   {
/* 135 */     return new Htable(paramNode);
/*     */   }
/*     */ 
/*     */   void addHtable(JPEGHuffmanTable paramJPEGHuffmanTable, boolean paramBoolean, int paramInt) {
/* 139 */     this.tables.add(new Htable(paramJPEGHuffmanTable, paramBoolean, paramInt));
/*     */   }
/*     */ 
/*     */   class Htable
/*     */     implements Cloneable
/*     */   {
/*     */     int tableClass;
/*     */     int tableID;
/*     */     private static final int NUM_LENGTHS = 16;
/* 150 */     short[] numCodes = new short[16];
/*     */     short[] values;
/*     */ 
/*     */     Htable(JPEGBuffer arg2)
/*     */     {
/*     */       Object localObject;
/* 154 */       this.tableClass = (localObject.buf[localObject.bufPtr] >>> 4);
/* 155 */       this.tableID = (localObject.buf[(localObject.bufPtr++)] & 0xF);
/* 156 */       for (int i = 0; i < 16; i++) {
/* 157 */         this.numCodes[i] = ((short)(localObject.buf[(localObject.bufPtr++)] & 0xFF));
/*     */       }
/*     */ 
/* 160 */       i = 0;
/* 161 */       for (int j = 0; j < 16; j++) {
/* 162 */         i += this.numCodes[j];
/*     */       }
/* 164 */       this.values = new short[i];
/* 165 */       for (j = 0; j < i; j++)
/* 166 */         this.values[j] = ((short)(localObject.buf[(localObject.bufPtr++)] & 0xFF));
/*     */     }
/*     */ 
/*     */     Htable(JPEGHuffmanTable paramBoolean, boolean paramInt, int arg4)
/*     */     {
/* 171 */       this.tableClass = (paramInt != 0 ? 0 : 1);
/*     */       int i;
/* 172 */       this.tableID = i;
/* 173 */       this.numCodes = paramBoolean.getLengths();
/* 174 */       this.values = paramBoolean.getValues();
/*     */     }
/*     */ 
/*     */     Htable(Node arg2)
/*     */       throws IIOInvalidTreeException
/*     */     {
/*     */       Node localNode;
/* 178 */       if (localNode.getNodeName().equals("dhtable")) {
/* 179 */         NamedNodeMap localNamedNodeMap = localNode.getAttributes();
/* 180 */         int i = localNamedNodeMap.getLength();
/* 181 */         if (i != 2) {
/* 182 */           throw new IIOInvalidTreeException("dhtable node must have 2 attributes", localNode);
/*     */         }
/*     */ 
/* 185 */         this.tableClass = MarkerSegment.getAttributeValue(localNode, localNamedNodeMap, "class", 0, 1, true);
/* 186 */         this.tableID = MarkerSegment.getAttributeValue(localNode, localNamedNodeMap, "htableId", 0, 3, true);
/* 187 */         if ((localNode instanceof IIOMetadataNode)) {
/* 188 */           IIOMetadataNode localIIOMetadataNode = (IIOMetadataNode)localNode;
/* 189 */           JPEGHuffmanTable localJPEGHuffmanTable = (JPEGHuffmanTable)localIIOMetadataNode.getUserObject();
/*     */ 
/* 191 */           if (localJPEGHuffmanTable == null) {
/* 192 */             throw new IIOInvalidTreeException("dhtable node must have user object", localNode);
/*     */           }
/*     */ 
/* 195 */           this.numCodes = localJPEGHuffmanTable.getLengths();
/* 196 */           this.values = localJPEGHuffmanTable.getValues();
/*     */         } else {
/* 198 */           throw new IIOInvalidTreeException("dhtable node must have user object", localNode);
/*     */         }
/*     */       }
/*     */       else {
/* 202 */         throw new IIOInvalidTreeException("Invalid node, expected dqtable", localNode);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected Object clone()
/*     */     {
/* 209 */       Htable localHtable = null;
/*     */       try {
/* 211 */         localHtable = (Htable)super.clone(); } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */       }
/* 213 */       if (this.numCodes != null) {
/* 214 */         localHtable.numCodes = ((short[])this.numCodes.clone());
/*     */       }
/* 216 */       if (this.values != null) {
/* 217 */         localHtable.values = ((short[])this.values.clone());
/*     */       }
/* 219 */       return localHtable;
/*     */     }
/*     */ 
/*     */     IIOMetadataNode getNativeNode() {
/* 223 */       IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("dhtable");
/* 224 */       localIIOMetadataNode.setAttribute("class", Integer.toString(this.tableClass));
/* 225 */       localIIOMetadataNode.setAttribute("htableId", Integer.toString(this.tableID));
/*     */ 
/* 227 */       localIIOMetadataNode.setUserObject(new JPEGHuffmanTable(this.numCodes, this.values));
/*     */ 
/* 229 */       return localIIOMetadataNode;
/*     */     }
/*     */ 
/*     */     void print()
/*     */     {
/* 234 */       System.out.println("Huffman Table");
/* 235 */       System.out.println("table class: " + (this.tableClass == 0 ? "DC" : "AC"));
/*     */ 
/* 237 */       System.out.println("table id: " + Integer.toString(this.tableID));
/*     */ 
/* 239 */       new JPEGHuffmanTable(this.numCodes, this.values).toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.DHTMarkerSegment
 * JD-Core Version:    0.6.2
 */