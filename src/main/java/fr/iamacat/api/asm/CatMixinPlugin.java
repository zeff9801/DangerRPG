package fr.iamacat.api.asm;

import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CatMixinPlugin implements IMixinConfigPlugin {
    protected static List<String> commonMixins = new ArrayList<>();
    protected static List<String> clientMixins = new ArrayList<>();
    protected static List<String> serverMixins = new ArrayList<>();

    protected static final String CLIENT = "client";
    protected static final String SERVER = "server";

    protected static void addMixin(String mixinClass) {
        commonMixins.add(mixinClass);
    }
    protected static void addMixin(String mixinClass, String side) {
        switch (side.toLowerCase()) {
            case CLIENT:
                clientMixins.add(mixinClass);
                break;
            case SERVER:
                serverMixins.add(mixinClass);
                break;
            default:
                commonMixins.add(mixinClass);
                break;
        }
    }
    private boolean someCondition() {
        // Your logic to determine if the other mixin should be applied
        return false; // Replace with your actual condition
    }

    private boolean anotherCondition() {
        // Your logic to determine if the other mixin should be applied
        return false; // Replace with your actual condition
    }

    @Override
    public void onLoad(String mixinPackage) {
        //Mixins.addConfiguration("hypothetical_mixin_json_file");
        if (someCondition()) {
            addMixin("hypothetical_mixin_1");
        }
        if (anotherCondition()) {
            addMixin("hypothetical_mixin_2");
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        MixinEnvironment.Side side = MixinEnvironment.getCurrentEnvironment().getSide();
        if (side == MixinEnvironment.Side.CLIENT) {
            return clientMixins.contains(mixinClassName) || commonMixins.contains(mixinClassName);
        } else if (side == MixinEnvironment.Side.SERVER) {
            return serverMixins.contains(mixinClassName) || commonMixins.contains(mixinClassName);
        }
        return false;  // Par défaut, on ne charge pas si ce n'est ni client ni serveur
    }


    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        List<String> allMixins = new ArrayList<>();
        allMixins.addAll(commonMixins);
        allMixins.addAll(clientMixins);
        allMixins.addAll(serverMixins);
        return allMixins;
    }
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
