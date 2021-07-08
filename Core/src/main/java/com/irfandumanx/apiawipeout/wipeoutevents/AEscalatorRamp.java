package main.java.com.irfandumanx.apiawipeout.wipeoutevents;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.region.Region;

public abstract class AEscalatorRamp extends Region {
    public AEscalatorRamp(ApiaWipeout instance) {
        super(instance);
    }
    public enum RampType {
        NORTH,
        SOUTH,
        WEST,
        EAST
    }

    public abstract RampType getRampType();
    public abstract void setRampType(RampType rampType);
    public abstract int getNumberOfBlock();
    public abstract void setNumberOfBlock(int numberOfBlock);

}
