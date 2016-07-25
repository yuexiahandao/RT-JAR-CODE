package javax.xml.stream.events;

import java.util.List;

public abstract interface DTD extends XMLEvent
{
  public abstract String getDocumentTypeDeclaration();

  public abstract Object getProcessedDTD();

  public abstract List getNotations();

  public abstract List getEntities();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.events.DTD
 * JD-Core Version:    0.6.2
 */