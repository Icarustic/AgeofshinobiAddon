//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.mcreator.ageofshinobiaddon;

import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(
        modid = "ageofshinobiaddon",
        version = "5.0"
)
public class AgeofshinobiaddonMod {
    public static final String MODID = "ageofshinobiaddon";
    public static final String VERSION = "5.0";
    public static final SimpleNetworkWrapper PACKET_HANDLER;
    @SidedProxy(
            clientSide = "net.mcreator.ageofshinobiaddon.ClientProxyAgeofshinobiaddonMod",
            serverSide = "net.mcreator.ageofshinobiaddon.ServerProxyAgeofshinobiaddonMod"
    )
    public static IProxyAgeofshinobiaddonMod proxy;
    @Instance("ageofshinobiaddon")
    public static AgeofshinobiaddonMod instance;
    public ElementsAgeofshinobiaddonMod elements = new ElementsAgeofshinobiaddonMod();

    public AgeofshinobiaddonMod() {
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        GameRegistry.registerWorldGenerator(this.elements, 5);
        GameRegistry.registerFuelHandler(this.elements);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ElementsAgeofshinobiaddonMod.GuiHandler());
        this.elements.preInit(event);
        MinecraftForge.EVENT_BUS.register(this.elements);
        this.elements.getElements().forEach((element) -> {
            element.preInit(event);
        });
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        this.elements.getElements().forEach((element) -> {
            element.init(event);
        });
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        this.elements.getElements().forEach((element) -> {
            element.serverLoad(event);
        });
        proxy.serverLoad(event);
    }
//UNSURE ABOUT THE CHANGES HERE BC THE THING SHOWED ERRORS IDK IF IT'S OBSTRUCTED.
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(this.elements.getBlocks().stream().map(Supplier::get).toArray(Block[]::new));
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(this.elements.getItems().stream().map(Supplier::get).toArray(Item[]::new));
    }

    @SubscribeEvent
    public void registerBiomes(RegistryEvent.Register<Biome> event) {
        event.getRegistry().registerAll(this.elements.getBiomes().stream().map(Supplier::get).toArray(Biome[]::new));
    }

    @SubscribeEvent
    public void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(this.elements.getEntities().stream().map(Supplier::get).toArray(EntityEntry[]::new));
    }

    @SubscribeEvent
    public void registerPotions(RegistryEvent.Register<Potion> event) {
        event.getRegistry().registerAll(this.elements.getPotions().stream().map(Supplier::get).toArray(Potion[]::new));
    }

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        this.elements.registerSounds(event);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        this.elements.getElements().forEach((element) -> {
            element.registerModels(event);
        });
    }

    static {
        PACKET_HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel("ageofshinobiaddo:a");
        FluidRegistry.enableUniversalBucket();
    }
}
