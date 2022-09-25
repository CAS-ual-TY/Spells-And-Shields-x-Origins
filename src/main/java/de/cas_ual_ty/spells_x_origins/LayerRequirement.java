package de.cas_ual_ty.spells_x_origins;

import com.google.gson.JsonObject;
import de.cas_ual_ty.spells.capability.SpellProgressionHolder;
import de.cas_ual_ty.spells.requirement.IRequirementType;
import de.cas_ual_ty.spells.requirement.Requirement;
import de.cas_ual_ty.spells.util.SpellsFileUtil;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ContainerLevelAccess;

import java.util.concurrent.atomic.AtomicBoolean;

public class LayerRequirement extends Requirement
{
    protected ResourceLocation layerRL;
    protected ResourceLocation originRL;
    
    public LayerRequirement(IRequirementType.RequirementType type)
    {
        super(type);
    }
    
    public LayerRequirement(IRequirementType.RequirementType type, ResourceLocation layerRL, ResourceLocation originRL)
    {
        super(type);
        this.layerRL = layerRL;
        this.originRL = originRL;
    }
    
    @Override
    public boolean passes(SpellProgressionHolder spellProgressionHolder, ContainerLevelAccess containerLevelAccess)
    {
        AtomicBoolean ret = new AtomicBoolean(false);
        
        IOriginContainer.get(spellProgressionHolder.getPlayer()).ifPresent((container) -> {
            OriginLayer layer = OriginsAPI.getLayersRegistry().get(layerRL);
            if(container.hasOrigin(layer))
            {
                Origin origin = container.getOrigin(layer);
                
                if(origin != null && origin.getRegistryName().equals(originRL))
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
        return new TranslatableComponent(getDescriptionId(), new TranslatableComponent("layer." + originRL.getNamespace() + "." + originRL.getPath() + ".name"), new TranslatableComponent("origin." + originRL.getNamespace() + "." + originRL.getPath() + ".name"));
    }
    
    @Override
    public void writeToJson(JsonObject jsonObject)
    {
        jsonObject.addProperty("origin", originRL.toString());
        jsonObject.addProperty("layer", layerRL.toString());
    }
    
    @Override
    public void readFromJson(JsonObject jsonObject)
    {
        originRL = new ResourceLocation(SpellsFileUtil.jsonString(jsonObject, "origin"));
        layerRL = new ResourceLocation(SpellsFileUtil.jsonString(jsonObject, "layer"));
    }
    
    @Override
    public void writeToBuf(FriendlyByteBuf buf)
    {
        buf.writeResourceLocation(originRL);
        buf.writeResourceLocation(layerRL);
    }
    
    @Override
    public void readFromBuf(FriendlyByteBuf buf)
    {
        originRL = buf.readResourceLocation();
        layerRL = buf.readResourceLocation();
    }
}
