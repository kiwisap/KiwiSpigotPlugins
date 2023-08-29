package nl.itz_kiwisap_.spigot;

import nl.itz_kiwisap_.spigot.utils.JavaReflections;
import nl.itz_kiwisap_.spigot.utils.ProtocolVersion;
import nl.itz_kiwisap_.spigot.nms.*;
import nl.itz_kiwisap_.spigot.v1_16_R3.KiwiNMS_v1_16_R3;
import nl.itz_kiwisap_.spigot.v1_17_R1.KiwiNMS_v1_17_R1;
import nl.itz_kiwisap_.spigot.v1_18_R2.KiwiNMS_v1_18_R2;
import nl.itz_kiwisap_.spigot.v1_19_R3.KiwiNMS_v1_19_R3;
import nl.itz_kiwisap_.spigot.v1_20_R1.KiwiNMS_v1_20_R1;

final class KiwiNMSInitializer {

    private KiwiNMSInitializer() {
    }

    static void initialize() throws Exception {
        Class<?> nmsInstance = JavaReflections.getClass("nl.itz_kiwisap_.spigotlibraries.nms.KiwiNMSInstance");

        KiwiNMS instance = switch (ProtocolVersion.getServerVersion()) {
            case MINECRAFT_1_16_5 -> new KiwiNMS_v1_16_R3();
            case MINECRAFT_1_17_1 -> new KiwiNMS_v1_17_R1();
            case MINECRAFT_1_18_2 -> new KiwiNMS_v1_18_R2();
            case MINECRAFT_1_19_4 -> new KiwiNMS_v1_19_R3();
            case MINECRAFT_1_20_1 -> new KiwiNMS_v1_20_R1();
            default ->
                    throw new IllegalStateException("Unsupported server version: " + ProtocolVersion.getServerVersion());
        };

        JavaReflections.getMethod(nmsInstance, "init", KiwiNMS.class).invoke(null, instance);
    }
}