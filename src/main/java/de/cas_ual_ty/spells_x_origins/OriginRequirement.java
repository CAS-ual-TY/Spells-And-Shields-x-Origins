package de.cas_ual_ty.spells_x_origins;

import com.google.gson.JsonObject;
import de.cas_ual_ty.spells.capability.SpellProgressionHolder;
import de.cas_ual_ty.spells.requirement.IRequirementType;
import de.cas_ual_ty.spells.requirement.Requirement;
import de.cas_ual_ty.spells.util.SpellsFileUtil;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ContainerLevelAccess;

import java.util.concurrent.atomic.AtomicBoolean;

public class OriginRequirement extends Requirement
{
    protected ResourceLocation origin;
    
    public OriginRequirement(IRequirementType<?> type)
    {
        super(type);
    }
    
    public OriginRequirement(IRequirementType<?> type, ResourceLocation origin)
    {
        super(type);
        this.origin = origin;
    }
    
    @Override
    public boolean passes(SpellProgressionHolder spellProgressionHolder, ContainerLevelAccess containerLevelAccess)
    {
        AtomicBoolean ret = new AtomicBoolean(false);
        
        IOriginContainer.get(spellProgressionHolder.getPlayer()).ifPresent((container) -> {
            container.getOrigins().forEach((layerKey, originKey) ->
            {
                if(originKey != null && originKey.location().equals(origin))
                {
                    ret.set(true);
                }
            });
        });
        
        return ret.get();
    }
    
    @Override
    public MutableComponent makeDescription(SpellProgressionHolder spellProgressionHolder, ContainerLevelAccess containerLevelAccess)
    {
        return Component.translatable(getDescriptionId(), Component.translatable("origin." + origin.getNamespace() + "." + origin.getPath() + ".name"));
    }
    
    @Override
    public void writeToJson(JsonObject jsonObject)
    {
        jsonObject.addProperty("origin", origin.toString());
    }
    
    @Override
    public void readFromJson(JsonObject jsonObject)
    {
        origin = new ResourceLocation(SpellsFileUtil.jsonString(jsonObject, "origin"));
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
