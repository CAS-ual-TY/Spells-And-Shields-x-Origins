package de.cas_ual_ty.spells_x_origins;

import de.cas_ual_ty.spells.SpellsAndShields;
import de.cas_ual_ty.spells.requirement.IRequirementType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod(SpellsXOrigins.MOD_ID)
public class SpellsXOrigins
{
    public static final String MOD_ID = "spells_and_shields_x_origins";
    
    public static final DeferredRegister<IRequirementType<?>> REQUIREMENTS = DeferredRegister.create(new ResourceLocation(SpellsAndShields.MOD_ID, "requirements"), MOD_ID);
    
    public static final RegistryObject<IRequirementType<?>> ORIGIN_REQUIREMENT = REQUIREMENTS.register("origin", () -> OriginRequirement::new);
    public static final RegistryObject<IRequirementType<?>> LAYER_REQUIREMENT = REQUIREMENTS.register("origin_layer", () -> LayerRequirement::new);
    
    public SpellsXOrigins()
    {
        REQUIREMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
