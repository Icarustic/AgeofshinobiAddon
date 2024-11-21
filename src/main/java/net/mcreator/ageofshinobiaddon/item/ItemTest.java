package net.mcreator.ageofshinobiaddon.item;

import net.mcreator.ageofshinobiaddon.ElementsAhznbrusticaddonMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.narutomod.creativetab.TabModTab;
import net.narutomod.entity.EntityHidingInAsh;
import net.narutomod.item.ItemJutsu;

@ElementsAhznbrusticaddonMod.ModElement.Tag
public class ItemTest extends ElementsAhznbrusticaddonMod.ModElement {
    @GameRegistry.ObjectHolder("ahnzbrusticaddon:test")
    public static final Item block = null;
    public static final int ENTITYID = 123;
    public static final int ENTITY2ID = 10123;

    public static final ItemJutsu.JutsuEnum HIDINGINASH = new ItemJutsu.JutsuEnum(2, "hiding_in_ash", 'B', 50d, new EntityHidingInAsh.EC.Jutsu());

    public ItemTest(ElementsAhznbrusticaddonMod instance) {
        super(instance, 366);
    }

    public ItemTest(ElementsAhznbrusticaddonMod elements, int sortid) {
        super(elements, sortid);
    }

    @Override
    public void initElements() {
        elements.items.add(() -> new RangedItem( HIDINGINASH));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("ahnzbrusticaddon:test", "inventory"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        //RenderingRegistry.registerEntityRenderingHandler(EntityBigFireball.class, renderManager -> {
          //  return new RenderBigFireball(renderManager);
       // });
    }

    public static class RangedItem extends ItemJutsu.Base {
        public RangedItem(ItemJutsu.JutsuEnum... list) {
            super(ItemJutsu.JutsuEnum.Type.OTHER, list);
            this.setRegistryName("test");
            this.setUnlocalizedName("test");
            this.setCreativeTab(TabModTab.tab);
            //this.defaultCooldownMap[GREATFIREBALL.index] = 0;
            //this.defaultCooldownMap[1] = 0;
        }

        @Override
        protected float getPower(ItemStack stack, EntityLivingBase entity, int timeLeft) {
            ItemJutsu.JutsuEnum je = this.getCurrentJutsu(stack);
            if (je == HIDINGINASH) {
                return this.getPower(stack, entity, timeLeft, 1.0f, 15f);
            } //else if (je == GREATFIREBALL) {
                //return this.getPower(stack, entity, timeLeft, 0.1f, 30f);
            //}
            else {
                return this.getPower(stack, entity, timeLeft, 1.0f, 30f);
            }
            //float power = 1f + (float)(this.getMaxUseDuration() - timeLeft) / (this.getCurrentJutsu(stack) == HIDINGINASH ? 10 : 20);
            //return Math.min(power, this.getMaxPower(stack, entity));
        }

        @Override
        protected float getMaxPower(ItemStack stack, EntityLivingBase entity) {
            ItemJutsu.JutsuEnum jutsu = this.getCurrentJutsu(stack);
            float f = super.getMaxPower(stack, entity);
            //if (jutsu == GFANNIHILATION || jutsu == GREATFLAME) {
                //return Math.min(f, 30.0f);
            //}
            //else if (jutsu == GREATFIREBALL) {
                //return Math.min(f, 10.0f);
            //}
            return f;
        }
    }
}