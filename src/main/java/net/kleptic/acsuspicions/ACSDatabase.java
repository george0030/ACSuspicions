package net.kleptic.acsuspicions;

import java.util.UUID;

public abstract class ACSDatabase {

    protected ACSuspicions plugin;
    public ACSDatabase(ACSuspicions plugin){
        this.plugin = plugin;
    }
    
    public abstract int countSuspicions(UUID player);
    
    public abstract boolean addSuspicion(Suspicion suspicion);

}
