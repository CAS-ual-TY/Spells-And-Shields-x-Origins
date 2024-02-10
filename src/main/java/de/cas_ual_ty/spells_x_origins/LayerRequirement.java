package de.cas_ual_ty.spells_x_origins;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.cas_ual_ty.spells.capability.SpellProgressionHolder;
import de.cas_ual_ty.spells.requirement.Requirement;
import de.cas_ual_ty.spells.requirement.RequirementType;
import de.cas_ual_ty.spells.util.SpellsDowngrade;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.api.registry.OriginsDynamicRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ContainerLevelAccess;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class LayerRequirement extends Requirement
{
    public static Codec<LayerRequirement> makeCodec(RequirementType<LayerRequirement> type)
    {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.xmap(ResourceLocation::new, ResourceLocation::toString).fieldOf("layer").forGetter(LayerRequirement::getLayer),
                Codec.STRING.xmap(ResourceLocation::new, ResourceLocation::toString).fieldOf("origin").forGetter(LayerRequirement::getOrigin)
        ).apply(instance, (layer, origin) -> new LayerRequirement(type, layer, origin)));
    }
    
    protected ResourceLocation layer;
    protected ResourceLocation origin;
    
    public LayerRequirement(RequirementType<?> type)
    {
        super(type);
    }
    
    public LayerRequirement(RequirementType<?> type, ResourceLocation layer, ResourceLocation origin)
    {
        super(type);
        this.layer = layer;
        this.origin = origin;
    }
    
    public ResourceLocation getLayer()
    {
        return layer;
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
            OriginLayer layer = OriginsAPI.getLayersRegistry().get(this.layer);
            if(container.hasOrigin(layer))
            {
                Origin origin = container.getOrigin(layer);
            
                if(origin != null && origin.getRegistryName().equals(this.origin))
                {
                    ret.set(true);
                }
            }
        });
    
        return ret.get();
    }
    
    @Override
    public void makeDescription(List<Component> tooltip, SpellProgressionHolder spellProgressionHolder, ContainerLevelAccess containerLevelAccess)
    {
        tooltip.add(SpellsDowngrade.translatable(getDescriptionId(), SpellsDowngrade.translatable("layer." + origin.getNamespace() + "." + origin.getPath() + ".name"), SpellsDowngrade.translatable("origin." + origin.getNamespace() + "." + origin.getPath() + ".name")));
    }
    
    @Override
    public void writeToBuf(FriendlyByteBuf buf)
    {
        buf.writeResourceLocation(origin);
        buf.writeResourceLocation(layer);
    }
    
    @Override
    public void readFromBuf(FriendlyByteBuf buf)
    {
        origin = buf.readResourceLocation();
        layer = buf.readResourceLocation();
    }
}
