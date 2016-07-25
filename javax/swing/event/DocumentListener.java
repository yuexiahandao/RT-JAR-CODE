package javax.swing.event;

import java.util.EventListener;

public abstract interface DocumentListener extends EventListener
{
  public abstract void insertUpdate(DocumentEvent paramDocumentEvent);

  public abstract void removeUpdate(DocumentEvent paramDocumentEvent);

  public abstract void changedUpdate(DocumentEvent paramDocumentEvent);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.DocumentListener
 * JD-Core Version:    0.6.2
 */