package mods.audino.proxy;

public interface Proxy {

    void preInit();

    void init();

    void postInit();

    void registerRenderInformation();
}