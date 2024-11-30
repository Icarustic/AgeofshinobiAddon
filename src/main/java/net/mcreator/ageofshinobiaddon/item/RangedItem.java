//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.mcreator.ageofshinobiaddon.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface RangedItem {
    void onUpdate(Entity var1, ItemStack var2);

    void onUsingTick(ItemStack var1, EntityLivingBase var2, int var3, int var4);

    void OnUpdate(Entity var1, ItemStack var2);
}
