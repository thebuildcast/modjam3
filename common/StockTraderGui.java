package assets.modjam3.common;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.storage.ISaveFormat;

public class StockTraderGui extends GuiContainer{
	GuiTextField textReason;
	public StockTraderGui(InventoryPlayer inventory,StockTraderTile gold) {
		super(new StockTraderContainer(gold,inventory));
		
	}
public void initGui(){
	textReason= new GuiTextField(fontRenderer, width / 2 -30, height / 2 -70, 98, 20);
    textReason.setFocused(true);
    textReason.setEnabled(true);
    textReason.setMaxStringLength(20);
}
	  protected void drawGuiContainerForegroundLayer(int par1, int par2)
      {
               fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, (ySize - 96) + 2, 0xffffff);
               this.fontRenderer.drawString("Stock Trader", 66, 6, 4210752);
              
               buttonList.add(new GuiButton(1, width / 2 + 2, height / 2 -40, 98, 20, "Submit"));
             
               textReason.drawTextBox();
 }
	  public void keyTyped(char c, int i){
		  super.keyTyped(c, i);
		 
		  textReason.textboxKeyTyped(c, i);
		  
	       

	        if (i == 28 || i == 156)
	        {
	            this.actionPerformed((GuiButton)this.buttonList.get(0));
	        }
		  }
	  
	  protected void actionPerformed(GuiButton par1GuiButton)
	    {
	        if (par1GuiButton.enabled)
	        {
	            if (par1GuiButton.id == 1)
	            {
	                System.out.println("test");
	            }
	            else if (par1GuiButton.id == 0)
	            {
	               
	            }
	        }
	    }
	  
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		// TODO Auto-generated method stub
		
	}
/*public void drawScreen(int i,int j,float f){
	textReason.drawTextBox();
	super.drawScreen(i, j, f);
}*/

}