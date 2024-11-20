//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.mcreator.ahnzbrusticaddon;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import java.util.Collection;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.narutomod.ModConfig;
import net.narutomod.PlayerTracker;
import net.narutomod.entity.EntityNinjaMob;
import net.narutomod.gui.overlay.OverlayChakraDisplay;
import net.narutomod.procedure.ProcedureUtils;

@ElementsAhznbrusticaddonMod.ModElement.Tag
public class Chakra extends ElementsAhznbrusticaddonMod.ModElement {
    private static final Map<EntityPlayer, PathwayPlayer> playerMap = Maps.newHashMap();
    private static final Map<EntityLivingBase, Pathway> livingMap = Maps.newHashMap();
    private static final String DATAKEY = "ChakraPathwaySystem";
    private static PathwayPlayer clientPlayerPathway = null;

    public Chakra(ElementsAhznbrusticaddonMod instance) {
        super(instance, 395);
    }

    @SideOnly(Side.CLIENT)
    public static boolean isInitialized(EntityPlayer player) {
        return clientPlayerPathway != null && clientPlayerPathway.user == player;
    }

    public static Collection<PathwayPlayer> getPlayerMap() {
        return playerMap.values();
    }

    public static double getLevel(EntityLivingBase entity) {
        Pathway cp = (Pathway)pathway(entity);
        return (double)MathHelper.sqrt(Math.max(cp.getAmount(), cp.getMax()));
    }


    public static double getChakraModifier(EntityLivingBase entity) {
        return ProcedureUtils.getCDModifier(getLevel(entity));
    }

    public static Object pathway(EntityLivingBase user) {
        if (user instanceof EntityPlayer) {
            return pathway((EntityPlayer)user);
        } else if (user instanceof EntityNinjaMob.Base) {
            return ((EntityNinjaMob.Base)user).getChakraPathway();
        } else {
            Pathway p = (Pathway)livingMap.get(user);
            if (p == null) {
                p = new Pathway(user);
                livingMap.put(user, p);
            }

            return p;
        }
    }

    public static PathwayPlayer pathway(EntityPlayer player) {
        if (!player.world.isRemote) {
            PathwayPlayer p = (PathwayPlayer)playerMap.get(player);
            return p != null ? p : new PathwayPlayer(player);
        } else {
            if (clientPlayerPathway == null || clientPlayerPathway.user != player) {
                clientPlayerPathway = new PathwayPlayer(player);
            }

            return clientPlayerPathway;
        }
    }

    public void preInit(FMLPreInitializationEvent event) {
        this.elements.addNetworkMessage(PathwayPlayer.ServerMessage.Handler.class, PathwayPlayer.ServerMessage.class, new Side[]{Side.CLIENT});
        this.elements.addNetworkMessage(PathwayPlayer.ConsumeMessage.Handler.class, PathwayPlayer.ConsumeMessage.class, new Side[]{Side.SERVER});
    }

    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PathwayPlayer.PlayerHook());
    }

    public static class PathwayPlayer extends Pathway<EntityPlayer> {
        private boolean forceSync;
        private int motionlessTime;
        private double prevX;
        private double prevZ;

        protected PathwayPlayer(EntityPlayer playerIn) {
            super(playerIn);
            this.setMax(PlayerTracker.getBattleXp(playerIn) * 0.5);
            this.set(playerIn.getEntityData().getDouble("ChakraPathwaySystem"));
            if (this.getAmount() < 0.0) {
                this.set(this.getMax());
            }
//double check these variables
            this.prevX = playerIn.posX;
            this.prevZ = playerIn.posZ;
            if (!playerIn.world.isRemote) {
                Chakra.playerMap.put(playerIn, this);
                this.sendToClient();
            }

        }

        public void warningDisplay() {
            OverlayChakraDisplay.notEnoughChakraWarning((EntityPlayer)this.user);
        }

        public void sendToClient() {
            if (this.user instanceof EntityPlayerMP) {
                Chakra.PathwayPlayer.ServerMessage.sendToSelf((EntityPlayerMP)this.user, this.getAmount(), this.getMax());
            }

        }

        protected void set(double amountIn) {
            if (amountIn != this.getAmount()) {
                super.set(amountIn);
                ((EntityPlayer)this.user).getEntityData().setDouble("ChakraPathwaySystem", amountIn);
                this.sendToClient();
                this.motionlessTime = 0;
            }

        }

        public boolean consume(double amountIn, boolean ignoreMax) {
            boolean flag = super.consume(amountIn, ignoreMax);
            if (amountIn > 0.0 && !flag) {
                this.warningDisplay();
            }

            return flag;
        }

        protected void onUpdate() {
            super.onUpdate();
            if (((EntityPlayer)this.user).world instanceof WorldServer && ((WorldServer)((EntityPlayer)this.user).world).areAllPlayersAsleep()) {
                this.consume(-0.6F);
            }

            ++this.motionlessTime;
            if (((EntityPlayer)this.user).posX != this.prevX || ((EntityPlayer)this.user).posZ != this.prevZ || !((EntityPlayer)this.user).onGround || ((EntityPlayer)this.user).isSwingInProgress) {
                this.motionlessTime = 0;
            }
//check variables (Unsure about this)
            if (this.motionlessTime > 80) {
                this.consume(-ModConfig.CHAKRA_REGEN_RATE - 0.001F * ((EntityPlayer)this.user).experienceLevel * ((EntityPlayer)this.user).experienceTotal);
            }

            double d = PlayerTracker.getBattleXp((EntityPlayer)this.user) * 0.5;
            if (d != this.getMax() || this.forceSync) {
                this.forceSync = false;
                this.setMax(d);
                this.sendToClient();
            }

            this.prevX = ((EntityPlayer)this.user).posX;
            this.prevZ = ((EntityPlayer)this.user).posZ;
        }

        public static class ConsumeMessage implements IMessage {
            public double amount;

            public ConsumeMessage() {
            }

            public ConsumeMessage(double amountIn) {
                this.amount = amountIn;
            }

            public static void sendToServer(double amountIn) {
                AhnzbrusticaddonMod.PACKET_HANDLER.sendToServer(new ConsumeMessage(amountIn));
            }

            public void toBytes(ByteBuf buf) {
                buf.writeDouble(this.amount);
            }

            public void fromBytes(ByteBuf buf) {
                this.amount = buf.readDouble();
            }

            public static class Handler implements IMessageHandler<ConsumeMessage, IMessage> {
                public Handler() {
                }
//Unsure about these func and variables Ill have to se what works
                public IMessage onMessage(ConsumeMessage message, MessageContext context) {
                    EntityPlayerMP entity = context.getServerHandler().player;
                    entity.getServer().addScheduledTask(() -> {
                        Chakra.pathway((EntityPlayer)entity).consume(message.amount);
                    });
                    return null;
                }
            }
        }

        public static class ServerMessage implements IMessage {
            double amount;
            double max;

            public ServerMessage() {
            }

            public ServerMessage(double amountIn, double maxIn) {
                this.amount = amountIn;
                this.max = maxIn;
            }

            public static void sendToSelf(EntityPlayerMP player, double d1, double d2) {
                AhnzbrusticaddonMod.PACKET_HANDLER.sendTo(new ServerMessage(d1, d2), player);
            }

            public void toBytes(ByteBuf buf) {
                buf.writeDouble(this.amount);
                buf.writeDouble(this.max);
            }

            public void fromBytes(ByteBuf buf) {
                this.amount = buf.readDouble();
                this.max = buf.readDouble();
            }

            public static class Handler implements IMessageHandler<ServerMessage, IMessage> {
                public Handler() {
                }

                @SideOnly(Side.CLIENT)
                public IMessage onMessage(ServerMessage message, MessageContext context) {
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        EntityPlayer player = Minecraft.getMinecraft().player;
                        if (player != null) {
                            Chakra.pathway((EntityPlayer)player).setMax(message.max).set(message.amount);
                        }

                    });
                    return null;
                }
            }
        }

        public static class PlayerHook {
            public PlayerHook() {
            }

            @SubscribeEvent
            public void onDeath(LivingDeathEvent event) {
                Entity entity = event.getEntityLiving();
                if (entity instanceof EntityPlayer) {
                    if (entity.world.isRemote) {
                        Chakra.clientPlayerPathway = null;
                    } else {
                        Pathway p = (Pathway)Chakra.playerMap.get((EntityPlayer)entity);
                        if (p != null) {
                            p.set(Math.min(PlayerTracker.keepNinjaXp(entity.world) && PlayerTracker.getBattleXp((EntityPlayer)entity) > 10.0 ? 10.0 : 0.0, p.getMax()));
                            Chakra.playerMap.remove((EntityPlayer)entity);
                        }
                    }
                }

            }

            @SubscribeEvent
            public void onTick(TickEvent.PlayerTickEvent event) {
                if (PlayerTracker.isNinja(event.player) && event.phase == Phase.END && !event.player.world.isRemote) {
                    Pathway p = (Pathway)Chakra.playerMap.get(event.player);
                    if (p != null) {
                        p.onUpdate();
                    } else if (event.player.experienceLevel >= 10) {
                        Chakra.pathway(event.player);
                    }
                }

            }

            @SubscribeEvent
            public void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
                if (event.player instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)event.player).connection.sendPacket(new SPacketSetExperience(event.player.experience, event.player.experienceTotal, event.player.experienceTotal));
                    PathwayPlayer p = (PathwayPlayer)Chakra.playerMap.get(event.player);
                    if (p != null) {
                        p.forceSync = true;
                    }
                }

            }

            @SubscribeEvent
            public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
                if (!event.player.world.isRemote && PlayerTracker.isNinja(event.player) && event.player.experienceLevel >= 10) {
                    Chakra.pathway(event.player);
                }

            }

            @SubscribeEvent(
                    priority = EventPriority.LOWEST
            )
            public void onRespawn(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
                EntityPlayer oldPlayer = event.getOriginal();
                EntityPlayer newPlayer = event.getEntityPlayer();
                PathwayPlayer p = (PathwayPlayer)Chakra.playerMap.get(oldPlayer);
                if (p != null) {
                    if (oldPlayer == newPlayer) {
                        p.forceSync = true;
                    } else {
                        PathwayPlayer pnew = Chakra.pathway(newPlayer);
                        pnew.set(p.getAmount());
                        pnew.forceSync = true;
                        Chakra.playerMap.remove(oldPlayer);
                    }
                }

            }
        }
    }

    public static class Pathway<T extends EntityLivingBase> {
        protected final T user;
        private double amount;
        private double max;

        public Pathway(T userIn) {
            this.user = userIn;
        }

        public double getMax() {
            return this.max;
        }

        public Pathway<T> setMax(double maxIn) {
            this.max = maxIn;
            return this;
        }

        protected void set(double amountIn) {
            this.amount = amountIn;
        }

        public boolean consume(double amountIn, boolean ignoreMax) {
            double d = this.getAmount();
            double max = this.getMax();
            double d1 = d - amountIn;
            d1 = d1 > max ? (ignoreMax ? d1 : (amountIn > 0.0 ? d1 : (d > max ? d : max))) : (d1 > 0.0 ? d1 : d);
            this.set(d1);
            return d != d1;
        }

        public boolean consume(double amountIn) {
            return this.consume(amountIn, false);
        }

        public void consume(float percent) {
            this.consume(percent, false);
        }

        public void consume(float percent, boolean ignoreMax) {
            this.consume(this.getMax() * (double)percent, ignoreMax);
        }

        public void clear() {
            this.set(0.0);
        }

        public boolean isFull() {
            return this.getAmount() >= this.getMax();
        }

        public double getAmount() {
            return this.amount;
        }

        protected void onUpdate() {
            double d = this.getAmount();
            double d1 = this.getMax();
            if (d > d1 * 4.0 && this.user.isEntityAlive()) {
                this.user.attackEntityFrom(DamageSource.GENERIC, Float.MAX_VALUE);
            } else {
                if (d > d1 && this.user.ticksExisted % 20 == 0) {
                    this.consume(10.0);
                }

                if (d < 10.0 && d1 > 150.0 && (!(this.user instanceof EntityPlayer) || !((EntityPlayer)this.user).isCreative())) {
                    this.user.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100, 3));
                    this.user.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 3));
                    this.user.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 3));
                }

            }
        }

        public void warningDisplay() {
        }

        public String toString() {
            return "Chakra:{amount:" + this.getAmount() + ",max:" + this.getMax() + "," + this.user.getName() + "}";
        }
    }
}
