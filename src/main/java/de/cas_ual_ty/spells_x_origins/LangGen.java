package de.cas_ual_ty.spells_x_origins;

import de.cas_ual_ty.spells.requirement.IRequirementType;
import de.cas_ual_ty.spells.requirement.Requirement;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LangGen extends LanguageProvider
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        event.getGenerator().addProvider(true, new LangGen(event.getGenerator(), "en_us"));
    }
    
    public LangGen(DataGenerator gen, String locale)
    {
        super(gen, SpellsXOrigins.MOD_ID, locale);
    }
    
    @Override
    protected void addTranslations()
    {
        addRequirement(SpellsXOrigins.ORIGIN_REQUIREMENT, "Origin: %s");
        addRequirement(SpellsXOrigins.LAYER_REQUIREMENT, "%s: %s");
    }
    
    public void addRequirement(Supplier<? extends IRequirementType<?>> requirement, String desc)
    {
        addRequirement(requirement, "", desc);
    }
    
    public void addRequirement(Supplier<? extends IRequirementType<?>> requirement, String suffix, String desc)
    {
        Requirement inst = requirement.get().makeInstance();
        String descriptionId = inst.getDescriptionId();
        add(descriptionId + suffix, desc);
    }
}