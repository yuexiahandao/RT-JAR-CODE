package com.sun.org.apache.xalan.internal.xsltc;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract interface DOM
{
  public static final int FIRST_TYPE = 0;
  public static final int NO_TYPE = -1;
  public static final int NULL = 0;
  public static final int RETURN_CURRENT = 0;
  public static final int RETURN_PARENT = 1;
  public static final int SIMPLE_RTF = 0;
  public static final int ADAPTIVE_RTF = 1;
  public static final int TREE_RTF = 2;

  public abstract DTMAxisIterator getIterator();

  public abstract String getStringValue();

  public abstract DTMAxisIterator getChildren(int paramInt);

  public abstract DTMAxisIterator getTypedChildren(int paramInt);

  public abstract DTMAxisIterator getAxisIterator(int paramInt);

  public abstract DTMAxisIterator getTypedAxisIterator(int paramInt1, int paramInt2);

  public abstract DTMAxisIterator getNthDescendant(int paramInt1, int paramInt2, boolean paramBoolean);

  public abstract DTMAxisIterator getNamespaceAxisIterator(int paramInt1, int paramInt2);

  public abstract DTMAxisIterator getNodeValueIterator(DTMAxisIterator paramDTMAxisIterator, int paramInt, String paramString, boolean paramBoolean);

  public abstract DTMAxisIterator orderNodes(DTMAxisIterator paramDTMAxisIterator, int paramInt);

  public abstract String getNodeName(int paramInt);

  public abstract String getNodeNameX(int paramInt);

  public abstract String getNamespaceName(int paramInt);

  public abstract int getExpandedTypeID(int paramInt);

  public abstract int getNamespaceType(int paramInt);

  public abstract int getParent(int paramInt);

  public abstract int getAttributeNode(int paramInt1, int paramInt2);

  public abstract String getStringValueX(int paramInt);

  public abstract void copy(int paramInt, SerializationHandler paramSerializationHandler)
    throws TransletException;

  public abstract void copy(DTMAxisIterator paramDTMAxisIterator, SerializationHandler paramSerializationHandler)
    throws TransletException;

  public abstract String shallowCopy(int paramInt, SerializationHandler paramSerializationHandler)
    throws TransletException;

  public abstract boolean lessThan(int paramInt1, int paramInt2);

  public abstract void characters(int paramInt, SerializationHandler paramSerializationHandler)
    throws TransletException;

  public abstract Node makeNode(int paramInt);

  public abstract Node makeNode(DTMAxisIterator paramDTMAxisIterator);

  public abstract NodeList makeNodeList(int paramInt);

  public abstract NodeList makeNodeList(DTMAxisIterator paramDTMAxisIterator);

  public abstract String getLanguage(int paramInt);

  public abstract int getSize();

  public abstract String getDocumentURI(int paramInt);

  public abstract void setFilter(StripFilter paramStripFilter);

  public abstract void setupMapping(String[] paramArrayOfString1, String[] paramArrayOfString2, int[] paramArrayOfInt, String[] paramArrayOfString3);

  public abstract boolean isElement(int paramInt);

  public abstract boolean isAttribute(int paramInt);

  public abstract String lookupNamespace(int paramInt, String paramString)
    throws TransletException;

  public abstract int getNodeIdent(int paramInt);

  public abstract int getNodeHandle(int paramInt);

  public abstract DOM getResultTreeFrag(int paramInt1, int paramInt2);

  public abstract DOM getResultTreeFrag(int paramInt1, int paramInt2, boolean paramBoolean);

  public abstract SerializationHandler getOutputDomBuilder();

  public abstract int getNSType(int paramInt);

  public abstract int getDocument();

  public abstract String getUnparsedEntityURI(String paramString);

  public abstract Hashtable getElementsWithIDs();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.DOM
 * JD-Core Version:    0.6.2
 */