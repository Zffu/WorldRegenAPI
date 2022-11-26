package net.zffu.worldregenapi;

import org.bukkit.World;

public interface ResetingWorld {

    boolean load();
    void unload();
    boolean restoreFromSource();

    boolean isLoaded();
    World getWorld();

}
