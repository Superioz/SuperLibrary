package de.superioz.library.minecraft.server.common.npc.meta.settings;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class EntitySettings {

    protected boolean showName = false;
    protected boolean isOnGround = false;
    protected boolean exists = false;
    protected boolean fixedName = false;
    protected boolean fixedAim = true;

    public EntitySettings(boolean showName, boolean fixedName, boolean fixedAim){
        this.exists = false;
        this.isOnGround = false;
        this.showName = showName;
        this.fixedAim = fixedAim;
        this.fixedName = fixedName;
    }

    // -- Intern methods

    public boolean isExists(){
        return exists;
    }

    public void setExists(boolean exists){
        this.exists = exists;
    }

    public boolean isOnGround(){
        return isOnGround;
    }

    public void setIsOnGround(boolean isOnGround){
        this.isOnGround = isOnGround;
    }

    public boolean isShowName(){
        return showName;
    }

    public void setShowName(boolean showName){
        this.showName = showName;
    }

    public boolean isFixedName(){
        return !showName && fixedName;
    }

    public boolean isFixedAim(){
        return fixedAim;
    }
}
