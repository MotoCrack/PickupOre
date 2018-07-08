package me.devnatan.pickupore.nbt;

public class MethodNames {

    protected static String getTypeMethodName(){
        Version v = Version.getVersion();
        if(v == Version.MC1_8_R3){
            return "b";
        }
        
        return "d";
    }

}
