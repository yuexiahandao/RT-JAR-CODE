package com.sun.xml.internal.org.jvnet.fastinfoset.sax.helpers;

import com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmContentHandler;
import com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

public class FastInfosetDefaultHandler extends DefaultHandler
  implements LexicalHandler, EncodingAlgorithmContentHandler, PrimitiveTypeContentHandler
{
  public void comment(char[] ch, int start, int length)
    throws SAXException
  {
  }

  public void startCDATA()
    throws SAXException
  {
  }

  public void endCDATA()
    throws SAXException
  {
  }

  public void startDTD(String name, String publicId, String systemId)
    throws SAXException
  {
  }

  public void endDTD()
    throws SAXException
  {
  }

  public void startEntity(String name)
    throws SAXException
  {
  }

  public void endEntity(String name)
    throws SAXException
  {
  }

  public void octets(String URI, int algorithm, byte[] b, int start, int length)
    throws SAXException
  {
  }

  public void object(String URI, int algorithm, Object o)
    throws SAXException
  {
  }

  public void booleans(boolean[] b, int start, int length)
    throws SAXException
  {
  }

  public void bytes(byte[] b, int start, int length)
    throws SAXException
  {
  }

  public void shorts(short[] s, int start, int length)
    throws SAXException
  {
  }

  public void ints(int[] i, int start, int length)
    throws SAXException
  {
  }

  public void longs(long[] l, int start, int length)
    throws SAXException
  {
  }

  public void floats(float[] f, int start, int length)
    throws SAXException
  {
  }

  public void doubles(double[] d, int start, int length)
    throws SAXException
  {
  }

  public void uuids(long[] msblsb, int start, int length)
    throws SAXException
  {
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.fastinfoset.sax.helpers.FastInfosetDefaultHandler
 * JD-Core Version:    0.6.2
 */