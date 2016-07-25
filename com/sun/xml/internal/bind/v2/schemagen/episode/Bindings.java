package com.sun.xml.internal.bind.v2.schemagen.episode;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("bindings")
public abstract interface Bindings extends TypedXmlWriter
{
  @XmlElement
  public abstract Bindings bindings();

  @XmlElement("class")
  public abstract Klass klass();

  public abstract Klass typesafeEnumClass();

  @XmlElement
  public abstract SchemaBindings schemaBindings();

  @XmlAttribute
  public abstract void scd(String paramString);

  @XmlAttribute
  public abstract void version(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.episode.Bindings
 * JD-Core Version:    0.6.2
 */