package net.kleptic.acsuspicions;

import java.sql.Timestamp;
import java.util.UUID;

public class Suspicion {
    public final UUID player;
    public final Timestamp time;
    public final String hacktype;
    public final String server;

    public Suspicion(UUID player, Timestamp time, String hacktype, String server){
        this.player = player;
        this.time = time;
        this.hacktype = hacktype;
        this.server = server;
    }
    
}
