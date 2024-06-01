package thedrake;
import java.util.List;

import java.io.PrintWriter;
import thedrake.JSONSerializable;

public class Troop implements JSONSerializable {
    private final String name;
    private final Offset2D aversPivot;
    private final Offset2D reversPivot;
    private final List<TroopAction>aversActions;
    private final List<TroopAction>reversActions;

    public Troop(String name, Offset2D aversPivot, Offset2D reversPivot, List<TroopAction>aversActions,List<TroopAction>reversActions)
    {
        this.name = name;
        this.aversPivot = aversPivot;
        this.reversPivot = reversPivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    public Troop(String name,List<TroopAction>aversActions,List<TroopAction>reversActions) {
        this(name,
                new Offset2D(1, 1),
                new Offset2D(1, 1),
                aversActions,
                reversActions);
    }

    public Troop(String name, Offset2D pivot,List<TroopAction>aversActions,List<TroopAction>reversActions) {
        this(name, pivot, pivot,aversActions,reversActions);
    }


    public String name()
    {
        return name;
    }

    public Offset2D pivot(TroopFace face)
    {
        return (face == TroopFace.AVERS)? aversPivot: reversPivot;
    }

    public List<TroopAction> actions(TroopFace face)
    {
        if(face==TroopFace.AVERS)
            return aversActions;
        else
            return reversActions;
    }

        @Override
    public void toJSON(PrintWriter writer) {
        writer.print('"'+this.name+'"');
        }
}
