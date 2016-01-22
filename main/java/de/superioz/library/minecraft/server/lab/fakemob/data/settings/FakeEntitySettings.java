package de.superioz.library.minecraft.server.lab.fakemob.data.settings;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class FakeEntitySettings {

    protected boolean showName = false;
    protected boolean isOnGround = false;
    protected boolean exists = false;

    public FakeEntitySettings(boolean exists, boolean isOnGround, boolean showName){
        this.exists = exists;
        this.isOnGround = isOnGround;
        this.showName = showName;
    }

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
}
