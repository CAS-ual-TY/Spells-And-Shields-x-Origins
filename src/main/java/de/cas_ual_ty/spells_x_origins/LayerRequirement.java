package de.cas_ual_ty.spells_x_origins;

import com.google.gson.JsonObject;
import de.cas_ual_ty.spells.capability.SpellProgressionHolder;
import de.cas_ual_ty.spells.requirement.IRequirementType;
import de.cas_ual_ty.spells.requirement.Requirement;
import de.cas_ual_ty.spells.util.SpellsFileUtil;
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

import java.util.concurrent.atomic.AtomicBoolean;

public class LayerRequirement extends Requirement
{
    protected ResourceLocation layer;
    protected ResourceLocation origin;
    
    public LayerRequirement(IRequirementType<?> type)
    {
        super(type);
    }
    
    public LayerRequirement(IRequirementType<?> type, ResourceLocation layer, ResourceLocation origin)
    {
        super(type);
        this.layer = layer;
        this.origin = origin;
    }
    
    @Override
    public boolean passes(SpellProgressionHolder spellProgressionHolder, ContainerLevelAccess containerLevelAccess)
    {
        AtomicBoolean ret = new AtomicBoolean(false);
        
        IOriginContainer.get(spellProgressionHolder.getPlayer()).ifPresent((container) -> {
            ResourceKey<OriginLayer> layerKey = ResourceKey.create(OriginsDynamicRegistries.LAYERS_REGISTRY, layer);
            if(container.hasOrigin(layerKey))
            {
                ResourceKey<Origin> originKey = container.getOrigin(layerKey);
                
                if(originKey != null && originKey.location().equals(origin))
                {
                    ret.set(true);
                }
            }
        });
        
        return ret.get();
    }
    
    @Override
    public MutableComponent makeDescription(SpellProgressionHolder spellProgressionHolder, ContainerLevelAccess containerLevelAccess)
    {
        return Component.translatable(getDescriptionId(), Component.translatable("layer." + origin.getNamespace() + "." + origin.getPath() + ".name"), Component.translatable("origin." + origin.getNamespace() + "." + origin.getPath() + ".name"));
    }
    
    @Override
    public void writeToJson(JsonObject jsonObject)
    {
        jsonObject.addProperty("origin", origin.toString());
        jsonObject.addProperty("layer", layer.toString());
    }
    
    @Override
    public void readFromJson(JsonObject jsonObject)
    {
        origin = new ResourceLocation(SpellsFileUtil.jsonString(jsonObject, "origin"));
        layer = new ResourceLocation(SpellsFileUtil.jsonString(jsonObject, "layer"));
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
