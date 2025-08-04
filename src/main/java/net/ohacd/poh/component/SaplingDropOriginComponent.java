package net.ohacd.poh.component;

import org.ladysnake.cca.api.v3.component.ComponentV3;

public interface SaplingDropOriginComponent extends ComponentV3 {
    public enum origin {
        NATURAL,
        PLAYER
    }
    origin getOrigin();
    void setOrigin(origin origin);
}
