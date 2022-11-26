package net.zffu.worldregenapi;

import net.zffu.worldregenapi.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;


import java.io.File;
import java.io.IOException;

public class ResetingLocalWorld implements ResetingWorld {

    private final File sourceWorldfolder;
    private File activeWorldFolder;

    private World bukkitWorld;

    private String activeWorldName;

    public ResetingLocalWorld(File worldfolder, String worldname, boolean loadOnInit) {

        this.sourceWorldfolder = new File(
                worldfolder,
                worldname
        );

        this.activeWorldName = sourceWorldfolder.getName() + "_active_" + System.currentTimeMillis();

        if(loadOnInit) load();
    }

    public ResetingLocalWorld(File worldfolder, String worldname, boolean loadOnInit, String destinationWorldName) {
        this.sourceWorldfolder = new File(
                worldfolder,
                worldname
        );

        this.activeWorldName = destinationWorldName;

        if(loadOnInit) load();
    }


    public boolean load() {
        if (isLoaded()) return true;

        this.activeWorldFolder = new File(
                Bukkit.getWorldContainer().getParentFile(),
                this.activeWorldName
        );

        try {
            FileUtil.copy(sourceWorldfolder, activeWorldFolder);
        } catch (IOException e) {
            Bukkit.getLogger().severe("[WORLDREGEN-API] Could not Load the world " + this.sourceWorldfolder.getName());
            e.printStackTrace();
            return false;
        }

        this.bukkitWorld = Bukkit.createWorld(
                new WorldCreator(activeWorldFolder.getName())
        );

        if (bukkitWorld != null) this.bukkitWorld.setAutoSave(false);
        return isLoaded();

    }

    public void unload() {
        if(bukkitWorld != null) Bukkit.unloadWorld(bukkitWorld, false);
        if(activeWorldFolder != null) FileUtil.delete(activeWorldFolder);

        bukkitWorld = null;
        activeWorldFolder = null;

    }

    public boolean restoreFromSource() {
        unload();
        return load();
    }

    public World getWorld() {return this.bukkitWorld;}
    public boolean isLoaded() {return getWorld() != null;}
}
