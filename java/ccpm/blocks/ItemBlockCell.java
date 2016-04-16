package ccpm.blocks;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockCell extends ItemBlock {

	public ItemBlockCell(Block b) {
		super(b);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("ccpm.energycell");
	}

	@Override
	public int getMetadata (int meta) {
		return meta;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		if(stack.getItemDamage() == 0)
		{
			return "ccpm.energycell.rf";
		}
		if(stack.getItemDamage() == 1)
		{
			return "ccpm.energycell.th";
		}
		if(stack.getItemDamage() == 2)
		{
			return "ccpm.energycell.ma";
		}
		return "ccpm.energycell";
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		int meta = stack.getItemDamage();
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
		list.add("This is an energy cell from ecology mod."); 
		list.add("Place it under the machine from ecology mod");
		list.add("to use as an energy source.");
		
		if(meta == 0)
			list.add("This cell uses rf to work");
		
		if(meta == 1)
			list.add("This cell uses aer essentia from aura to work, but pollutes them");
		
		if(meta == 2)
			list.add("This cell uses mana to work");
		
		if(Loader.isModLoaded("OpenComputers"))
		{
			list.add("This can be attached to your computer from OpenConputers");
		}
		}
		else
		{
			list.add("");
			list.add("<Press LShift to show more info>");
		}
	}
}
