package assets.modjam3.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Random;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

public class StockViewerContainer extends Container{
protected StockViewerTile tile_entity;
	
	public StockViewerContainer(StockViewerTile tile_entity, InventoryPlayer player_inventory){
		this.tile_entity = tile_entity;
		int o = 0;
		addSlotToContainer(new Slot(tile_entity, o, 152+1*18, 18));
		o++;
		addSlotToContainer(new Slot(tile_entity, o, 152+1*18, 2*18));
		o++;
         for(int q = 0; q <3; q++){
         for(int p = 0; p <9; p++){
        
        
         addSlotToContainer(new Slot(tile_entity, o, 8+p*18, 18+q*18));
        
             o++;
            
             }}
		
		 bindPlayerInventory(player_inventory);
	}
	private void bindPlayerInventory(InventoryPlayer player_inventory) {
		// TODO Auto-generated method stub
		 for(int i = 0; i < 3; i++){
             for(int j = 0; j < 9; j++){
                     addSlotToContainer(new Slot(player_inventory, j+i * 9 + 9, 8 + j * 18, 86 + i * 18));
                    
             }
     }

     for(int i = 0; i < 9; i++){
             addSlotToContainer(new Slot(player_inventory, i, 8 + i * 18, 144));
             
     }
	}
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		 return tile_entity.isUseableByPlayer(entityplayer);
		
	}
	  
	  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
	       
          ItemStack stack = null;
      Slot slot_object = (Slot) inventorySlots.get(slot);
      
      if(slot_object != null && slot_object.getHasStack()){
              ItemStack stack_in_slot = slot_object.getStack();
              stack = stack_in_slot.copy();
             
              if(slot == 0){
                      if(!mergeItemStack(stack_in_slot, 1, inventorySlots.size(), true)){
                              return null;
                      }
              }
              else if(!mergeItemStack(stack_in_slot, 0, 1, false)){
                      return null;
              }
     
              if(stack_in_slot.stackSize == 0){
                      slot_object.putStack(null);
              }
              else{
                      slot_object.onSlotChanged();
              }
      }
      stack.stackTagCompound =null;
      stack.setItemDamage(0);
      return stack;
	        
	      //  return null;
}
	 @Override
	  public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
	    {
		 
		  if (par1!=-999){
			  if (par3==4){
				  par3=0;
			  }
		  if ((par1 >27||par1==0||par1==1)||tile_entity.getStackInSlot(par1)==null){
			 super.slotClick(par1, par2, par3, par4EntityPlayer);
		  }else{
			  if(tile_entity.getStackInSlot(0)==null)
					  return null;
			  if(tile_entity.getStackInSlot(0).getItem()==FinancialExpansion.itembankCard){
				if   (tile_entity.getStackInSlot(0).stackTagCompound.getInteger("balance")>=tile_entity.getStackInSlot(par1).getItemDamage()){
		 int bal=tile_entity.getStackInSlot(0).stackTagCompound.getInteger("balance")-tile_entity.getStackInSlot(par1).getItemDamage();
		 
		 //PacketDispatcher.sendPacketToServer(packet(bal));
		  
		 tile_entity.getStackInSlot(0).stackTagCompound.setInteger("balance",bal );
					ItemStack stack =tile_entity.getStackInSlot(par1);
		  stack.setItemDamage(0);
		  stack.stackTagCompound=null;
		  tile_entity.bcd=true;
		  tile_entity.setInventorySlotContents(1, stack);
		  FinancialExpansion.instance.market.completeTrade(par1-2,bal);
		  tile_entity.bcd=true;
		  tile_entity.setInventorySlotContents(par1,null);
		  par4EntityPlayer.worldObj.markBlockForUpdate(tile_entity.xCoord, tile_entity.yCoord, tile_entity.zCoord);
		
		
		  }}}}
		  return null;
	    }
	 public Packet packet(int balance){
	
			

			ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
			DataOutputStream outputStream = new DataOutputStream(bos);
			try {
			   
			       outputStream.writeInt(tile_entity.xCoord);
			       outputStream.writeInt(tile_entity.yCoord);
			       outputStream.writeInt(tile_entity.zCoord);
			       outputStream.writeInt(balance);
			} catch (Exception ex) {
			        ex.printStackTrace();
			}

			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "balanceupdate";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			return packet;
		}
}