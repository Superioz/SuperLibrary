package de.superioz.library.minecraft.server.common.lab.fakemob.data;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class FakeHumanProfile {

    protected String skinName;
    protected String capeUrl;

    public FakeHumanProfile(String capeUrl, String skinName){
        this.capeUrl = capeUrl;
        this.skinName = skinName;
    }

    public String getCapeUrl(){
        return capeUrl;
    }

    public void setCapeUrl(String capeUrl){
        this.capeUrl = capeUrl;
    }

    public String getSkinName(){
        return skinName;
    }

    public void setSkinName(String skinName){
        this.skinName = skinName;
    }

}
