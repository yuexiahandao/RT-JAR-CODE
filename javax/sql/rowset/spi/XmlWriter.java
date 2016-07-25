package javax.sql.rowset.spi;

import java.io.Writer;
import java.sql.SQLException;
import javax.sql.RowSetWriter;
import javax.sql.rowset.WebRowSet;

public abstract interface XmlWriter extends RowSetWriter
{
  public abstract void writeXML(WebRowSet paramWebRowSet, Writer paramWriter)
    throws SQLException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.spi.XmlWriter
 * JD-Core Version:    0.6.2
 */