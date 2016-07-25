package com.sun.org.apache.xml.internal.dtm;

import com.sun.org.apache.xml.internal.utils.XMLString;
import javax.xml.transform.SourceLocator;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public abstract interface DTM
{
  public static final int NULL = -1;
  public static final short ROOT_NODE = 0;
  public static final short ELEMENT_NODE = 1;
  public static final short ATTRIBUTE_NODE = 2;
  public static final short TEXT_NODE = 3;
  public static final short CDATA_SECTION_NODE = 4;
  public static final short ENTITY_REFERENCE_NODE = 5;
  public static final short ENTITY_NODE = 6;
  public static final short PROCESSING_INSTRUCTION_NODE = 7;
  public static final short COMMENT_NODE = 8;
  public static final short DOCUMENT_NODE = 9;
  public static final short DOCUMENT_TYPE_NODE = 10;
  public static final short DOCUMENT_FRAGMENT_NODE = 11;
  public static final short NOTATION_NODE = 12;
  public static final short NAMESPACE_NODE = 13;
  public static final short NTYPES = 14;

  public abstract void setFeature(String paramString, boolean paramBoolean);

  public abstract void setProperty(String paramString, Object paramObject);

  public abstract DTMAxisTraverser getAxisTraverser(int paramInt);

  public abstract DTMAxisIterator getAxisIterator(int paramInt);

  public abstract DTMAxisIterator getTypedAxisIterator(int paramInt1, int paramInt2);

  public abstract boolean hasChildNodes(int paramInt);

  public abstract int getFirstChild(int paramInt);

  public abstract int getLastChild(int paramInt);

  public abstract int getAttributeNode(int paramInt, String paramString1, String paramString2);

  public abstract int getFirstAttribute(int paramInt);

  public abstract int getFirstNamespaceNode(int paramInt, boolean paramBoolean);

  public abstract int getNextSibling(int paramInt);

  public abstract int getPreviousSibling(int paramInt);

  public abstract int getNextAttribute(int paramInt);

  public abstract int getNextNamespaceNode(int paramInt1, int paramInt2, boolean paramBoolean);

  public abstract int getParent(int paramInt);

  public abstract int getDocument();

  public abstract int getOwnerDocument(int paramInt);

  public abstract int getDocumentRoot(int paramInt);

  public abstract XMLString getStringValue(int paramInt);

  public abstract int getStringValueChunkCount(int paramInt);

  public abstract char[] getStringValueChunk(int paramInt1, int paramInt2, int[] paramArrayOfInt);

  public abstract int getExpandedTypeID(int paramInt);

  public abstract int getExpandedTypeID(String paramString1, String paramString2, int paramInt);

  public abstract String getLocalNameFromExpandedNameID(int paramInt);

  public abstract String getNamespaceFromExpandedNameID(int paramInt);

  public abstract String getNodeName(int paramInt);

  public abstract String getNodeNameX(int paramInt);

  public abstract String getLocalName(int paramInt);

  public abstract String getPrefix(int paramInt);

  public abstract String getNamespaceURI(int paramInt);

  public abstract String getNodeValue(int paramInt);

  public abstract short getNodeType(int paramInt);

  public abstract short getLevel(int paramInt);

  public abstract boolean isSupported(String paramString1, String paramString2);

  public abstract String getDocumentBaseURI();

  public abstract void setDocumentBaseURI(String paramString);

  public abstract String getDocumentSystemIdentifier(int paramInt);

  public abstract String getDocumentEncoding(int paramInt);

  public abstract String getDocumentStandalone(int paramInt);

  public abstract String getDocumentVersion(int paramInt);

  public abstract boolean getDocumentAllDeclarationsProcessed();

  public abstract String getDocumentTypeDeclarationSystemIdentifier();

  public abstract String getDocumentTypeDeclarationPublicIdentifier();

  public abstract int getElementById(String paramString);

  public abstract String getUnparsedEntityURI(String paramString);

  public abstract boolean supportsPreStripping();

  public abstract boolean isNodeAfter(int paramInt1, int paramInt2);

  public abstract boolean isCharacterElementContentWhitespace(int paramInt);

  public abstract boolean isDocumentAllDeclarationsProcessed(int paramInt);

  public abstract boolean isAttributeSpecified(int paramInt);

  public abstract void dispatchCharactersEvents(int paramInt, ContentHandler paramContentHandler, boolean paramBoolean)
    throws SAXException;

  public abstract void dispatchToEvents(int paramInt, ContentHandler paramContentHandler)
    throws SAXException;

  public abstract Node getNode(int paramInt);

  public abstract boolean needsTwoThreads();

  public abstract ContentHandler getContentHandler();

  public abstract LexicalHandler getLexicalHandler();

  public abstract EntityResolver getEntityResolver();

  public abstract DTDHandler getDTDHandler();

  public abstract ErrorHandler getErrorHandler();

  public abstract DeclHandler getDeclHandler();

  public abstract void appendChild(int paramInt, boolean paramBoolean1, boolean paramBoolean2);

  public abstract void appendTextChild(String paramString);

  public abstract SourceLocator getSourceLocatorFor(int paramInt);

  public abstract void documentRegistration();

  public abstract void documentRelease();

  public abstract void migrateTo(DTMManager paramDTMManager);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.DTM
 * JD-Core Version:    0.6.2
 */