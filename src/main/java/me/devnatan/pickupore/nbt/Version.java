package me.devnatan.pickupore.nbt;

import org.bukkit.Bukkit;

/**
 *
 * Copyright (c) 2018 FakeNetwork. Todos os direitos reservados.
 * @version 1.0
 * @author DevNatan
 *
 */
public enum Version {

    UNKNOWN(0),
    MC1_8_R1(181),
    MC1_8_R3(183);

    private static Version version;
    private static String versionPlain;
    private static String pkg;
    private final int id;

    Version(int id) {
        this.id = id;
    }

    public static Version getVersion() {
        if (version != null) {
            return version;
        }

        final String ver = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        versionPlain = ver;
        pkg = "net.minecraft.server." + versionPlain + ".";

        try {
            version = Version.valueOf(ver.replace("v", "MC"));
        } catch (IllegalArgumentException ex) {
            version = UNKNOWN;
        }

        return version;
    }

    public static String getVersionPlain() {
        if(versionPlain == null) {
            getVersion();
        }

        return versionPlain;
    }

    public static String getPackage() {
        if(pkg == null){
            getVersion();
        }

        return pkg;
    }

}
