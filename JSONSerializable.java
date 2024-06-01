package thedrake;

import java.io.PrintWriter;
import java.io.StringWriter;

public interface JSONSerializable {
    public void toJSON(PrintWriter writer);
}
