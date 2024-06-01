package thedrake;

import java.io.PrintWriter;
import thedrake.JSONSerializable;

public enum PlayingSide implements JSONSerializable {
    ORANGE, BLUE;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("\"%s\"", this.toString());
        //writer.printf('"'+this.toString()+'"');
    }
}
