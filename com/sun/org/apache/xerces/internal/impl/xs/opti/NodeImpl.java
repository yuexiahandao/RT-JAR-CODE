/*    */ package com.sun.org.apache.xerces.internal.impl.xs.opti;
/*    */ 
/*    */ public class NodeImpl extends DefaultNode
/*    */ {
/*    */   String prefix;
/*    */   String localpart;
/*    */   String rawname;
/*    */   String uri;
/*    */   short nodeType;
/*    */   boolean hidden;
/*    */ 
/*    */   public NodeImpl()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NodeImpl(String prefix, String localpart, String rawname, String uri, short nodeType)
/*    */   {
/* 44 */     this.prefix = prefix;
/* 45 */     this.localpart = localpart;
/* 46 */     this.rawname = rawname;
/* 47 */     this.uri = uri;
/* 48 */     this.nodeType = nodeType;
/*    */   }
/*    */ 
/*    */   public String getNodeName()
/*    */   {
/* 53 */     return this.rawname;
/*    */   }
/*    */ 
/*    */   public String getNamespaceURI()
/*    */   {
/* 58 */     return this.uri;
/*    */   }
/*    */ 
/*    */   public String getPrefix()
/*    */   {
/* 63 */     return this.prefix;
/*    */   }
/*    */ 
/*    */   public String getLocalName()
/*    */   {
/* 68 */     return this.localpart;
/*    */   }
/*    */ 
/*    */   public short getNodeType()
/*    */   {
/* 73 */     return this.nodeType;
/*    */   }
/*    */ 
/*    */   public void setReadOnly(boolean hide, boolean deep)
/*    */   {
/* 80 */     this.hidden = hide;
/*    */   }
/*    */ 
/*    */   public boolean getReadOnly()
/*    */   {
/* 85 */     return this.hidden;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl
 * JD-Core Version:    0.6.2
 */