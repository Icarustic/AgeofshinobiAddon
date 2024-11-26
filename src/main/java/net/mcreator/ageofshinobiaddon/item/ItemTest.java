package net.mcreator.ageofshinobiaddon.item;

import com.google.common.collect.Lists;
import net.mcreator.ageofshinobiaddon.ElementsAgeofshinobiaddonMod;
import net.mcreator.ageofshinobiaddon.entity.EntitySenkoSpirit;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.narutomod.creativetab.TabModTab;
import net.narutomod.item.ItemJutsu;
import net.narutomod.procedure.ProcedureUtils;

import java.util.List;

@ElementsAgeofshinobiaddonMod.ModElement.Tag
public class ItemTest extends ElementsAgeofshinobiaddonMod.ModElement {
    @ObjectHolder("ageofshinobiaddon:test")
    public static final Item block = null;
    public static final int ENTITYID = 366; // Example entity ID
    public static final ItemJutsu.JutsuEnum SENKO = new ItemJutsu.JutsuEnum(0, "senko", 'B', 50d, new EntitySenkoSpirit.EC.Jutsu());

    public ItemTest(ElementsAgeofshinobiaddonMod instance) {
        super(instance, 366);
    }

    public void initElements() {
        this.elements.items.add(() -> new RangedItem(SENKO));
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("ageofshinobiaddon:test", "inventory"));
    }



    public static class RangedItem extends ItemJutsu.Base {
        public RangedItem(ItemJutsu.JutsuEnum... list) {
            super(ItemJutsu.JutsuEnum.Type.OTHER, list);
            this.setUnlocalizedName("test");
            this.setRegistryName("test");
            this.setCreativeTab(TabModTab.tab);
            this.defaultCooldownMap[SENKO.index] = 0L; // Set cooldown for the jutsu
        }

        protected float getPower(ItemStack stack, EntityLivingBase entity, int timeLeft) {
            return 1.0F; // Return the power value
        }


        public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
            super.addInformation(stack, world, tooltip, flag);
            tooltip.add(TextFormatting.GREEN + I18n.translateToLocal("tooltip.test.description") + TextFormatting.RESET);
        }
    }

}