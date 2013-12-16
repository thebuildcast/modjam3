package assets.modjam3.common;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.crash.CallableMinecraftVersion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import assets.modjam3.common.*;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels={"stocktrader","stockviewer","balanceupdate","stockclient","traderbalance"}, packetHandler = PacketHandler.class)
public class FinancialExpansion {
	@Instance(Reference.MOD_ID)
	public static FinancialExpansion instance;
	 private GuiHandler guiHandler = new GuiHandler();
	int nickelOreID;
	
	int coinPressID;
	int stockViewerID;
	int nickelBlockID;
	int nickelIngotID;
	int stockTraderID;
	int nickelCoinID;
	int bankCardID;
	int bankAtmID;
	int chipPartID;
	public MarketManager market;
	
	
	public static Block blocknickelOre;
	public static Block blockcoinPress;
	public static Block blockstockViewer;
	public static Block blockbankATM;
	public static Block blocknickelBlock;

	public static Block blockstockTrader;

	public static Item itemnickelIngot;
	public static Item itemnickelCoin;
	public static Item itembankCard;
	public static Item itemchipPart;
	
	OreGeneration oregeneration = new OreGeneration();
	
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
		
		//Block
		bankAtmID = config.get("Machine IDs", "Bank ATM ID", 703).getInt();
		coinPressID = config.get("Machine IDs", "Coin Press ID", 700).getInt();
		stockViewerID =config.get("Machine IDs", "StockViewer  ID", 701).getInt();
		nickelOreID = config.get("Ore IDs", "Nickel Ore ID", 800).getInt();

		nickelBlockID=config.get("Other IDs", "Nickel Block ID", 801).getInt();
		

		stockTraderID = config.get("Machine IDs", "StockTrader  ID", 702).getInt();

		//Ingot
		nickelIngotID = config.get("Item IDs", "Nickel Ingot ID", 1000).getInt();
		//item
	
		nickelCoinID = config.get("Item IDs", "Nickel Coin ID", 1001).getInt();
		bankCardID =config.get("Item IDs", "Bank Card ID", 1002).getInt();
		chipPartID = config.get("Item IDs", "Chip Part ID", 1003).getInt();
		
	}
	
	@Init
	public void load(FMLInitializationEvent event){
		
		
		VillagerRegistry.instance().registerVillagerId(100);
		if (event.getSide().isClient()){
		VillagerRegistry.instance().registerVillagerSkin(100, DefaultProps.BANKER_SKIN);
		}
		BankerTradeHandler bankerTradeHandler = new BankerTradeHandler();
		VillagerRegistry.instance().registerVillageTradeHandler(100, bankerTradeHandler);
		VillagerRegistry.instance().registerVillageCreationHandler(new VillageHandlerBank());
		//Block
		blockcoinPress = new BlockCoinPress(coinPressID);
		registerBlock(blockcoinPress,"Coin Press", blockcoinPress.getUnlocalizedName());
		blockstockViewer = new BlockStockViewer(stockViewerID);
		registerBlock(blockstockViewer,"Stock Viewer", blockstockViewer.getUnlocalizedName());
		
		blockbankATM = new BlockBankATM(bankAtmID);
		registerBlock(blockbankATM,"Bank ATM", blockbankATM.getUnlocalizedName());
		
		blocknickelBlock = new BlockNickelBlock(nickelBlockID);
		registerBlock(blocknickelBlock, "Nickel Block", blocknickelBlock.getUnlocalizedName());
		//Tiles
		blockstockTrader = new BlockStockTrader(stockTraderID);
		registerBlock(blockstockTrader,"Stock Trader", blockstockTrader.getUnlocalizedName());
	//Ore
		blocknickelOre = new BlockNickelOre(nickelOreID);
		registerBlock(blocknickelOre, "Nickel Ore", blocknickelOre.getUnlocalizedName());
		
		
	//Ingot
		itemnickelIngot = new ItemNickelIngot(nickelIngotID);
		registerItem(itemnickelIngot, "Nickel Ingot", itemnickelIngot.getUnlocalizedName());
		
	//Other
		itemnickelCoin = new ItemNickelCoin(nickelCoinID);
		registerItem(itemnickelCoin, "Nickel Coin", itemnickelCoin.getUnlocalizedName());
		itembankCard = new ItemBankCard(bankCardID);
		registerItem(itembankCard, "Bank Card", itembankCard.getUnlocalizedName());
		itemchipPart = new ItemChipPart(chipPartID);
		registerItem(itemchipPart, "Chip Part", itemchipPart.getUnlocalizedName());
		
		GameRegistry.addSmelting(nickelOreID, new ItemStack(itemnickelIngot, 1), 1F);
		GameRegistry.registerWorldGenerator(oregeneration);
		 GameRegistry.registerTileEntity(CoinPressTile.class, "CoinPressTile");
		 GameRegistry.registerTileEntity(StockViewerTile.class, "StockViewerTile");
		 GameRegistry.registerTileEntity(StockTraderTile.class, "StockTraderTile");
		networkRegisters();
		
		GameRegistry.addRecipe(new ItemStack(FinancialExpansion.blockbankATM, 1),
				new Object[]{
			"cbc",
			"cdc",
			"cbc",
			'c', FinancialExpansion.itemnickelCoin,
			'd', FinancialExpansion.itembankCard,
			'b', FinancialExpansion.blocknickelBlock,
		});
		GameRegistry.addRecipe(new ItemStack(FinancialExpansion.blocknickelBlock, 1),
				new Object[]{
			"nnn",
			"nnn",
			"nnn",
			'n', FinancialExpansion.itemnickelIngot
		});
		GameRegistry.addRecipe(new ItemStack(FinancialExpansion.blockcoinPress, 1),
				new Object[]{
			"ibi",
			"idi",
			"ibi",
			'i', Item.ingotIron,
			'b', FinancialExpansion.blocknickelBlock, 
			'd', Item.diamond,
		});
		GameRegistry.addRecipe(new ItemStack(FinancialExpansion.itemchipPart, 2),
				new Object[]{
			"nrn",
			"rdr",
			"nrn",
			'n', FinancialExpansion.itemnickelCoin,
			'r', Item.redstone, 
			'd', Item.diamond,
		});
		GameRegistry.addRecipe(new ItemStack(FinancialExpansion.itembankCard, 1),
				new Object[]{
			"ncn",
			"dcd",
			"ncn",
			'n', FinancialExpansion.itemnickelCoin,
			'c', FinancialExpansion.itemchipPart, 
			'd', Item.diamond,
		});
		GameRegistry.addRecipe(new ItemStack(FinancialExpansion.blockstockTrader, 1),
				new Object[]{
			"ccc",
			"bab",
			"ccc",
			'a', FinancialExpansion.blockstockViewer,
			'b', FinancialExpansion.blocknickelBlock,
			'c', FinancialExpansion.itemnickelCoin,
		});
		GameRegistry.addRecipe(new ItemStack(FinancialExpansion.blockstockViewer, 1),
				new Object[]{
			"ccc",
			"bab",
			"ccc",
			'a', FinancialExpansion.blockbankATM,
			'b', FinancialExpansion.blocknickelBlock,
			'c', FinancialExpansion.itemnickelCoin,
		});
	}
	
	public static void registerBlock(Block block, String name, String unlocalizedName){
		GameRegistry.registerBlock(block, Reference.MOD_ID + unlocalizedName);
		LanguageRegistry.addName(block, name);
	}
	  @EventHandler
	  public void serverLoad(FMLServerStartingEvent event)
	  {
	    event.registerServerCommand(new CardCommand());
	    //market2 = new MarketManager();
	  }
	  @EventHandler
	  public void clientLoad(FMLServerStartingEvent event)
	  {
	   market = new MarketManager();
	    
	  }
	public static void registerItem(Item item, String name, String unlocalizedName){
		GameRegistry.registerItem(item, Reference.MOD_ID + unlocalizedName);
		LanguageRegistry.addName(item, name);
		
	}
	 public void networkRegisters(){
         NetworkRegistry.instance().registerGuiHandler(instance, guiHandler);
 }
}

