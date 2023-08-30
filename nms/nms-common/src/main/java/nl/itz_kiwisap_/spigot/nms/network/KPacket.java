package nl.itz_kiwisap_.spigot.nms.network;

public interface KPacket {

    Object getNMSInstance();

    default Object getBundleNMSInstance() {
        return null;
    }
}