package nl.itz_kiwisap_.spigot.nms;

final class KiwiNMSInstance {

    private KiwiNMSInstance() {
    }

    static KiwiNMS INSTANCE;

    static void init(KiwiNMS instance) {
        if (INSTANCE != null) {
            throw new IllegalStateException("KiwiNMS has already been initialized!");
        }

        INSTANCE = instance;
    }
}