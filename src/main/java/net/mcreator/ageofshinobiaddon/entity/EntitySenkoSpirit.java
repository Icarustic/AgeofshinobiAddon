package net.mcreator.ageofshinobiaddon.entity;

import net.mcreator.ageofshinobiaddon.ElementsAgeofshinobiaddonMod;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.narutomod.item.ItemJutsu;
import net.narutomod.ElementsNarutomodMod;

import java.util.List;

@ElementsAgeofshinobiaddonMod.ModElement.Tag
public class EntitySenkoSpirit extends ElementsAgeofshinobiaddonMod.ModElement {
    public static final int ENTITYID = 401;
    public static final int SECOND_ENTITYID = 402; // ID for the second entity

    public EntitySenkoSpirit(ElementsAgeofshinobiaddonMod instance) {
        super(instance, 999);
    }

    @Override
    public void initElements() {
        elements.entities.add(() -> EntityEntryBuilder.create().entity(EC.class)
                .id(new ResourceLocation("narutomod", "teleporting_projectile"), ENTITYID)
                .name("teleporting_projectile").tracker(64, 3, true).build());
        elements.entities.add(() -> EntityEntryBuilder.create().entity(EntitySpiritStrike.class)
                .id(new ResourceLocation("narutomod", "spirit_strike"), SECOND_ENTITYID)
                .name("spirit_strike").tracker(64, 3, true).build());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EC.class, renderManager -> new RenderSenkoSpirit2(renderManager));
        RenderingRegistry.registerEntityRenderingHandler(EntitySpiritStrike.class, renderManager -> new RenderSpiritStrike(renderManager));
    }

    public static class EC extends EntityScalableProjectile.Base {
        private final float damage = 10.0f; // Damage dealt to the target
        private int ticksAlive = 0; // Ticks the projectile has been alive
        private static final int MAX_LIFETIME = 200; // Maximum lifetime before despawning


        @Override
        public void onUpdate() {
            super.onUpdate();
            ticksAlive++;

            // Check if the projectile has landed on the ground
            if (this.onGround) {
                if (ticksAlive > MAX_LIFETIME) {
                    this.setDead(); // Despawn the projectile after MAX_LIFETIME
                }
            }

            // Despawn the projectile if it has been alive for too long
            if (ticksAlive > MAX_LIFETIME) {
                this.setDead();
            }
        }

        public EC(World worldIn) {
            super(worldIn);
            this.setSize(0.2f, 0.2f);
        }

        public EC(EntityLivingBase shooterIn) {
            super(shooterIn);
            this.setSize(0.2f, 0.2f);
        }

        @Override
        protected void onImpact(RayTraceResult result) {
            if (!this.world.isRemote) {
                final EntityLivingBase target;
                if (result.entityHit instanceof EntityLivingBase) {
                    target = (EntityLivingBase) result.entityHit;
                } else if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                    target = findNearbyEntity(result.getBlockPos());
                } else {
                    target = null;
                }

                // Teleport the shooter to the target
                if (target != null && this.shootingEntity instanceof EntityLivingBase) {
                    EntityLivingBase shooter = (EntityLivingBase) this.shootingEntity;
                    shooter.setPositionAndUpdate(target.posX, target.posY, target.posZ);

                    // Spawn the second entity above the target
                    EntitySpiritStrike spiritStrike = new EntitySpiritStrike(this.world, target, shooter);
                    spiritStrike.setPosition(target.posX, target.posY + 5.0D, target.posZ);
                    this.world.spawnEntity(spiritStrike); // Spawn the spirit strike entity

                    // Apply slowness to the target
                    target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, 1));

                    // Schedule the explosion effect after a delay
                    this.world.getMinecraftServer().addScheduledTask(() -> {
                        target.attackEntityFrom(DamageSource.causeThrownDamage(this, shooter), this.damage);
                        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                    });

                    this.setDead();
                }
            }
        }

        private EntityLivingBase findNearbyEntity(BlockPos pos) {
            double radius = 3.0D; // 3 blocks radius
            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                    new AxisAlignedBB(pos).grow(radius));

            for (EntityLivingBase entity : nearbyEntities) {
                if (entity != this.shootingEntity) {
                    return entity;
                }
            }
            return null; // No entity found
        }

        @Override
        protected void readEntityFromNBT(NBTTagCompound compound) {
        }

        @Override
        protected void writeEntityToNBT(NBTTagCompound compound) {
        }

        public static class Jutsu implements ItemJutsu.IJutsuCallback {
            @Override
            public boolean createJutsu(ItemStack stack, EntityLivingBase entity, float power) {
                Vec3d vec = entity.getLookVec(); // Get the player's look vector
                EC projectile = new EC(entity);
                projectile.setPosition(entity.posX, entity.posY + 1.0D, entity.posZ); // Set the position slightly above the player
                projectile.shoot(vec.x, vec.y, vec.z, 1.0F, 0.0F); // Set the projectile's direction and speed
                projectile.rotationYaw = entity.rotationYaw; // Set the projectile's yaw to match the player's
                projectile.rotationPitch = entity.rotationPitch; // Set the projectile's pitch to match the player's
                entity.world.spawnEntity(projectile); // Spawn the projectile in the world
                return true;
            }
        }
    }

    public static class EntitySpiritStrike extends Entity {
        private EntityLivingBase shooter; // Reference to the shooter
        private EntityLivingBase target; // Reference to the target
        private float speed = 0.1f; // Speed at which the entity moves towards the target
        private int ticksAlive = 0; // Ticks the entity has been alive
        private static final int DAMAGE_TICKS = 100; // Number of ticks to deal damage
        private static final float TICK_DAMAGE = 1.0F; // Damage dealt per tick
        private static final int DAMAGE_INTERVAL = 5; // Number of ticks between damage applications
        private int explosionTickCounter = 0; // Counter for explosion ticks

        public EntitySpiritStrike(World worldIn) {
            super(worldIn);
            this.setSize(0.5f, 0.5f); // Size of the spirit strike entity
        }

        public EntitySpiritStrike(World worldIn, EntityLivingBase target, EntityLivingBase shooter) {
            this(worldIn);
            this.target = target; // Set the target reference
            this.shooter = shooter; // Set the shooter reference
            this.setPosition(target.posX, target.posY + 5.0D, target.posZ); // Spawn above the target
        }

        @Override
        protected void entityInit() {
            // Initialize entity data here
        }

        @Override
        public void onUpdate() {
            super.onUpdate();
            if (!this.world.isRemote && this.target != null) {
                // Make the spirit strike hover above the target
                this.setPosition(this.target.posX, this.target.posY + 5.0D, this.target.posZ);

                // Increment the ticks alive
                ticksAlive++;

                // Deal damage periodically
                if (ticksAlive % DAMAGE_INTERVAL == 0) { // Every 5 ticks (0.25 seconds)
                    target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shooter), TICK_DAMAGE);
                    explosionTickCounter++; // Increment the explosion tick counter

                    // Check if it's time to create an explosion
                    if (explosionTickCounter >= 3) { // Every 3 ticks of damage
                        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                        this.world.newExplosion(this, this.posX, this.posY, this.posZ, 2.0F, false, true); // Explosion effect
                        explosionTickCounter = 0; // Reset the explosion counter
                    }
                }

                // Check if it has lived long enough to die
                if (ticksAlive >= DAMAGE_TICKS) {
                    this.setDead(); // Remove the entity after the duration
                }
            }
        }

        @Override
        protected void readEntityFromNBT(NBTTagCompound compound) {
        }

        @Override
        protected void writeEntityToNBT(NBTTagCompound compound) {
        }
    }
    @SideOnly(Side.CLIENT)
    public static class RenderSenkoSpirit2 extends Render<EC> {
        private final ModelSenkoSpirit2 model;

        public RenderSenkoSpirit2(RenderManager renderManager) {
            super(renderManager);
            this.model = new ModelSenkoSpirit2(); // Initialize the model
        }

        @Override
        public void doRender(EC entity, double x, double y, double z, float entityYaw, float partialTicks) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            this.bindEntityTexture(entity);

            // Set the rotation based on the entity's yaw and pitch
            GlStateManager.rotate(-entity.rotationYaw, 0.0F, 1.0F, 0.0F); // Yaw rotation
            GlStateManager.rotate(entity.rotationPitch, 1.0F, 0.0F, 0.0F); // Pitch rotation

            this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F); // Render the model
            GlStateManager.popMatrix();
        }

        @Override
        protected ResourceLocation getEntityTexture(EC entity) {
            return new ResourceLocation("ageofshinobiaddon:textures/shark.png");
        }
    }

    @SideOnly(Side.CLIENT)
    public static class RenderSpiritStrike extends Render<EntitySpiritStrike> {
        private final ModelSenkoSpirit model; // Reusing the same model for simplicity


        public RenderSpiritStrike(RenderManager renderManager) {
            super(renderManager);
            this.model = new ModelSenkoSpirit(); // Initialize the model
        }

        @Override
        public void doRender(EntitySpiritStrike entity, double x, double y, double z, float entityYaw, float partialTicks) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            this.bindEntityTexture(entity);
            this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F); // Render the model
            GlStateManager.popMatrix();
        }

        @Override
        protected ResourceLocation getEntityTexture(EntitySpiritStrike entity) {
            return new ResourceLocation("ageofshinobiaddon:textures/shark.png");
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ModelSenkoSpirit extends ModelBase {
        private final ModelRenderer bone2;
        private final ModelRenderer bone;

        public ModelSenkoSpirit() {
            textureWidth = 32;
            textureHeight = 32;
            bone2 = new ModelRenderer(this);
            bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
            bone2.cubeList.add(new ModelBox(bone2, 12, 0, -1.5F, -1.0F, -1.5F, 3, 2, 3, 0.0F, false));
            bone2.cubeList.add(new ModelBox(bone2, 9, 5, -1.5F, -1.5F, -1.5F, 3, 3, 3, -0.1F, false));
            bone2.cubeList.add(new ModelBox(bone2, 0, 8, -1.5F, -2.0F, -1.5F, 3, 4, 3, -0.3F, false));
            bone2.cubeList.add(new ModelBox(bone2, 0, 0, -1.5F, -2.5F, -1.5F, 3, 5, 3, -0.5F, false));
            bone = new ModelRenderer(this);
            bone.setRotationPoint(0.0F, 3.5F, 0.0F);
            setRotationAngle(bone, 0.0F, 0.5236F, 0.0F);
            bone.cubeList.add(new ModelBox(bone, 8, 17, -1.0F, -1.0F, -1.0F, 2, 2, 2, 0.1F, false));
            bone.cubeList.add(new ModelBox(bone, 0, 15, -1.0F, -1.5F, -1.0F, 2, 3, 2, -0.1F, false));
            bone.cubeList.add(new ModelBox(bone, 12, 11, -1.0F, -2.0F, -1.0F, 2, 4, 2, -0.3F, false));
        }

        @Override
        public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
            bone2.render(f5);
            bone.render(f5);
        }

        public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ModelSenkoSpirit2 extends ModelBase {
        private final ModelRenderer bone2;
        private final ModelRenderer bone;

        public ModelSenkoSpirit2() {
            textureWidth = 32;
            textureHeight = 32;
            bone2 = new ModelRenderer(this);
            bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
            bone2.cubeList.add(new ModelBox(bone2, 12, 0, -1.5F, -1.0F, -1.5F, 3, 2, 3, 0.0F, false));
            bone2.cubeList.add(new ModelBox(bone2, 9, 5, -1.5F, -1.5F, -1.5F, 3, 3, 3, -0.1F, false));
            bone2.cubeList.add(new ModelBox(bone2, 0, 8, -1.5F, -2.0F, -1.5F, 3, 4, 3, -0.3F, false));
            bone2.cubeList.add(new ModelBox(bone2, 0, 0, -1.5F, -2.5F, -1.5F, 3, 5, 3, -0.5F, false));
            bone = new ModelRenderer(this);
            bone.setRotationPoint(0.0F, 3.5F, 0.0F);
            setRotationAngle(bone, 0.0F, 0.5236F, 0.0F);
            bone.cubeList.add(new ModelBox(bone, 8, 17, -1.0F, -1.0F, -1.0F, 2, 2, 2, 0.1F, false));
            bone.cubeList.add(new ModelBox(bone, 0, 15, -1.0F, -1.5F, -1.0F, 2, 3, 2, -0.1F, false));
            bone.cubeList.add(new ModelBox(bone, 12, 11, -1.0F, -2.0F, -1.0F, 2, 4, 2, -0.3F, false));
        }

        @Override
        public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
            bone2.render(f5);
            bone.render(f5);
        }

        public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }
}