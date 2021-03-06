package ecomod.core.stuff;

import ecomod.api.EcomodItems;
import ecomod.api.EcomodStuff;
import ecomod.api.capabilities.IPollution;
import ecomod.api.capabilities.PollutionProvider;
import ecomod.client.advancements.triggers.EMTriggers;
import ecomod.client.gui.EMGuiHandler;
import ecomod.common.utils.EMUtils;
import ecomod.common.world.FluidPollution;
import ecomod.common.world.gen.BiomeWasteland;
import ecomod.core.EMConsts;
import ecomod.core.EcologyMod;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class MainRegistry
{
	public static BiomeWasteland biome_wasteland = new BiomeWasteland();
	
	public static void doPreInit()
	{
		MinecraftForge.EVENT_BUS.register(MainRegistry.class);
		
		EcomodStuff.concentrated_pollution = new FluidPollution();
		
		FluidRegistry.registerFluid(EcomodStuff.concentrated_pollution);
		
		FluidRegistry.addBucketForFluid(EcomodStuff.concentrated_pollution);
		
		EcomodStuff.ecomod_creative_tabs = new CreativeTabs(EMConsts.modid){
			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(EcomodItems.RESPIRATOR);
			}
		};
		
		EMBlocks.doPreInit();
		EMItems.doPreInit();
		EMTiles.doPreInit();
		
		if(Loader.isModLoaded("opencomputers|core"))
			EMIntermod.OCpreInit();
		
		EMRecipes.doPreInit();
		
		EcomodStuff.custom_te_pollution_determinants = NonNullList.create();
	}
	
	public static void doInit()
	{
		if(!ModAPIManager.INSTANCE.hasAPI("ecomodapi"))
		{
			EcologyMod.log.error("EcomodAPI has not been loaded!!!");
			throw new NullPointerException("EcomodAPI has not been loaded!!!");
		}
		
		IGuiHandler igh = new EMGuiHandler();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(EcologyMod.instance, igh);
		
		if(EMConfig.wasteland_spawns_naturally)
			BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(biome_wasteland, 10));
		BiomeDictionary.addTypes(biome_wasteland, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.RARE, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.WET, BiomeDictionary.Type.COLD);
		BiomeManager.addStrongholdBiome(biome_wasteland);
		
		BiomeProvider.allowedBiomes.add(biome_wasteland);
		
		EcologyMod.log.info("Wasteland ID : "+Biome.getIdForBiome(biome_wasteland));
		
		EMBlocks.doInit();
		EMItems.doInit();
		
		if(Loader.isModLoaded("ic2"))
			EMIntermod.init_ic2_support();
		
		EMRecipes.doInit();
		
		EMTriggers.setup();
		
		FMLInterModComms.sendMessage("waila", "register", "ecomod.core.stuff.compat.EMWailaHandler.callbackRegister");
		
		//FMLInterModComms.sendFunctionMessage(EMConsts.modid, EMIntermod.key_add_te_pollution_determinant, ecomod.test.TEST_TEPollutionDeterminant.class.getName());
	}
	
	public static void doPostInit()
	{
		EMIntermod.registerBCFuels();
		
		EMIntermod.thermal_expansion_imc();
		
		if(Loader.isModLoaded("ic2"))
			EMIntermod.setup_ic2_support();
		
		EMRecipes.doPostInit();
	}
	
	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
	{
		EcologyMod.log.info("Registering Sounds");
		event.getRegistry().register(EcomodStuff.advanced_filter_working = new SoundEvent(EMUtils.resloc("advanced_filter_working")).setRegistryName(EMUtils.resloc("advanced_filter_working")));
		event.getRegistry().register(EcomodStuff.analyzer = new SoundEvent(EMUtils.resloc("analyzer")).setRegistryName(EMUtils.resloc("analyzer")));
	}
	
	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event)
	{
		EcologyMod.log.info("Registering Biomes");
		event.getRegistry().register(biome_wasteland.setRegistryName(EMUtils.resloc("wasteland")));
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		EMBlocks.register(event);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		EMItems.register(event);
	}
	
	public static final ResourceLocation POLLUTION_CAPABILITY_RESLOC = EMUtils.resloc("pollution");
	
	@SubscribeEvent
	public static void onCapabilityAttachment(AttachCapabilitiesEvent event)
	{
		if(event != null)
		if(event.getObject() != null)
		if(EcomodStuff.CAPABILITY_POLLUTION != null)
		if((event.getObject() instanceof ItemStack && ((ItemStack)event.getObject()).getItem() instanceof ItemFood) || event.getObject() instanceof ItemFood || event.getObject().getClass() == ItemFood.class)
		{
			event.addCapability(POLLUTION_CAPABILITY_RESLOC, new PollutionProvider());
		}
	}
	
	@net.minecraftforge.common.capabilities.CapabilityInject(IPollution.class)
	private static void onPollutionCapability(net.minecraftforge.common.capabilities.Capability cap)
	{
		EcologyMod.log.info("Pollution capability had been initialized!");
	}
}
