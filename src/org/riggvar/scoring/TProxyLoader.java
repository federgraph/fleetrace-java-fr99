package org.riggvar.scoring;

/**
 * ProxyLoader - Singleton use getInstance().setProxyLoader(realProxyLoader) to
 * inject an implementation
 */
public class TProxyLoader implements IProxyLoader {
    private static TProxyLoader instance;
    private IProxyLoader delegate;

    private TProxyLoader() {
    }

    public void Calc(TFRProxy proxy) {
        if (delegate == null)
            initDefault();
        delegate.Calc(proxy);
    }

    public void setProxyLoader(IProxyLoader value) {
        delegate = value;
    }

    public void initDefault() {
        delegate = new TProxyLoaderJS08();
    }

    public static TProxyLoader getInstance() {
        if (instance == null)
            instance = new TProxyLoader();
        return instance;
    }
}
