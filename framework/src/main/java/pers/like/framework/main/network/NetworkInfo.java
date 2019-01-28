package pers.like.framework.main.network;

import android.net.Network;

/**
 * @author Like
 */
public class NetworkInfo {

    private NetworkStatus status;
    private Network network;

    public NetworkInfo() {
    }

    public NetworkInfo(NetworkStatus status, Network network) {
        this.status = status;
        this.network = network;
    }

    public NetworkStatus getStatus() {
        return status;
    }

    public void setStatus(NetworkStatus status) {
        this.status = status;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

}
