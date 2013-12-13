package assets.modjam3.common;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
public class FinancialExpansion {
	@Instance(Reference.MOD_ID)
	public static FinancialExpansion instance;
	int nickelOreID;
	
	int nickelIngotID;
	
	public static Block blocknickelOre;
	public static Item itemnickelIngot;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event){
		
		ModMetadata data = event.getModMetadata();
		data.name = Reference.MOD_NAME;
		data.version = Reference.MOD_VERSION;
		data.authorList = Arrays.asList(new String[] {"Scorp"});
		data.description = Reference.MOD_DESCRIPTION;
		data.autogenerated = false;
		
		//Config
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		//Ore
		nickelOreID = config.get("Ore IDs", "Nickel Ore ID", 800).getInt();
		
		//Ingot
		nickelIngotID = config.get("Ingot IDs", "Nickel Ingot ID", 1000).getInt();
		
		
	}
	
	@Init
	public void load(FMLInitializationEvent event){
		
	//Ore
		blocknickelOre = new BlockNickelOre(nickelOreID);
		registerBlock(blocknickelOre, "Nickel Ore", blocknickelOre.getUnlocalizedName());
	}
	
	public static void registerBlock(Block block, String name, String unlocalizedName){
		GameRegistry.registerBlock(block, Reference.MOD_ID + unlocalizedName);
		LanguageRegistry.addName(block, name);
	}
	
	public static void registerItem(Item item, String name, String unlocalizedName){
		GameRegistry.registerItem(item, Reference.MOD_ID + unlocalizedName);
		LanguageRegistry.addName(item, name);
	}
}

