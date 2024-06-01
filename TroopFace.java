package thedrake;

import java.io.PrintWriter;
import thedrake.JSONSerializable;

public enum TroopFace implements JSONSerializable {
    AVERS, REVERS;

    @Override
    public void toJSON(PrintWriter writer) {
        //writer.printf('"'+this.toString()+'"');
        writer.printf("\"%s\"", this.toString());
    }
}
