package de.cas_ual_ty.spells_x_origins;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.cas_ual_ty.spells.capability.SpellProgressionHolder;
import de.cas_ual_ty.spells.requirement.Requirement;
import de.cas_ual_ty.spells.requirement.RequirementType;
import de.cas_ual_ty.spells.util.SpellsDowngrade;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ContainerLevelAccess;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class OriginRequirement extends Requirement
{
    public static Codec<OriginRequirement> makeCodec(RequirementType<OriginRequirement> type)
    {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.xmap(ResourceLocation::new, ResourceLocation::toString).fieldOf("origin").forGetter(OriginRequirement::getOrigin)
        ).apply(instance, (origin) -> new OriginRequirement(type, origin)));
    }
    
    protected ResourceLocation origin;
    
    public OriginRequirement(RequirementType<?> type)
    {
        super(type);
    }
    
    public OriginRequirement(RequirementType<?> type, ResourceLocation origin)
    {
        super(type);
        this.origin = origin;
    }
    
    public ResourceLocation getOrigin()
    {
        return origin;
    }
    
    @Override
    protected boolean doesPlayerPass(SpellProgressionHolder spellProgressionHolder, ContainerLevelAccess containerLevelAccess)
    {
        AtomicBoolean ret = new AtomicBoolean(false);
        
        IOriginContainer.get(spellProgressionHolder.getPlayer()).ifPresent((container) -> {
            container.getOrigins().forEach((layerKey, originKey) ->
            {
                if(originKey != null && originKey.getRegistryName().equals(origin))
                {
                    ret.set(true);
                }
            });
        });
        
        return ret.get();
    }
    
    @Override
    public void makeDescription(List<Component> tooltip, SpellProgressionHolder spellProgressionHolder, ContainerLevelAccess containerLevelAccess)
    {
        tooltip.add(SpellsDowngrade.translatable(getDescriptionId(), SpellsDowngrade.translatable("origin." + origin.getNamespace() + "." + origin.getPath() + ".name")));
    }
    
    @Override
    public void writeToBuf(FriendlyByteBuf buf)
    {
        buf.writeResourceLocation(origin);
    }
    
    @Override
    public void readFromBuf(FriendlyByteBuf buf)
    {
        origin = buf.readResourceLocation();
    }
}
