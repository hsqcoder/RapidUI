package pers.like.framework.main.network;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import pers.like.framework.main.util.Logger;

/**
 * @author Like
 */
public class NetworkManager {


    public final MutableLiveData<NetworkInfo> NETWORK_INFO = new MutableLiveData<>();

    public NetworkManager(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        manager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {

            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                NETWORK_INFO.postValue(new NetworkInfo(NetworkStatus.AVAILABLE, network));
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                NETWORK_INFO.postValue(new NetworkInfo(NetworkStatus.UNAVAILABLE, network));
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
            }

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
            }

            @Override
            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties);
                Logger.e(linkProperties.toString());
            }

        });
    }

}
