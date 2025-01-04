package mixac1.dangerrpg.asm;

import java.util.function.Predicate;

import cpw.mods.fml.common.Loader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TargetedMod {
    OPTIFINE("optifine", true, Loader::isModLoaded);

    @Getter
    private final String modName;
    @Getter
    private final boolean loadInDevelopment;
    @Getter
    private final Predicate<String> condition;

    // Méthode pour vérifier si le mod est chargé
    public boolean isModLoaded() {
        try {
            return this.condition.test(this.modName);
        } catch (NullPointerException e) {
            System.err.println("The mod " + this.modName + " is not loaded or the check failed.");
            return false;
        }
    }
}
