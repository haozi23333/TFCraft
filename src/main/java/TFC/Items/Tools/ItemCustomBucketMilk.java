package TFC.Items.Tools;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import TFC.TFCItems;
import TFC.API.IFood;
import TFC.API.TFCOptions;
import TFC.API.Enums.EnumFoodGroup;
import TFC.API.Enums.EnumItemReach;
import TFC.Core.TFCTabs;
import TFC.Core.TFC_Core;
import TFC.Core.TFC_Time;
import TFC.Core.Player.FoodStatsTFC;
import TFC.Items.ItemTerra;
import TFC.TileEntities.TileEntityBarrel;

public class ItemCustomBucketMilk extends ItemTerra implements IFood
{
	public ItemCustomBucketMilk()
	{
		super();
		this.setMaxStackSize(1);
		setCreativeTab(TFCTabs.TFCTools);
		this.setFolder("tools/");
	}
	
	@Override
	public boolean canStack()
	{
		return false;
	}

	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) 
	{
		if(is.hasTagCompound() && is.getTagCompound().hasKey("foodWeight"))
		{
			float ounces = is.getTagCompound().getFloat("foodWeight");
			if(ounces > 0)
				arraylist.add(ounces+" oz");
			float decay = is.getTagCompound().getFloat("foodDecay");
			if(decay > 0)
			{
				float perc = decay/ounces;
				String s = EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("gui.milk.fresh");
				if(perc > 50)
					s = EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("gui.milk.old");
				if(perc > 70)
					s = EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("gui.milk.sour");

				arraylist.add(s);
			}
			if(TFCOptions.enableDebugMode)
				arraylist.add(EnumChatFormatting.DARK_GRAY + "Decay: " + decay);
		}
	}

	public static ItemStack createTag(ItemStack is, float weight)
	{
		NBTTagCompound nbt = is.getTagCompound();
		if(nbt == null)
			nbt = new NBTTagCompound();
		nbt.setFloat("foodWeight", weight);
		nbt.setFloat("foodDecay", 0);
		nbt.setInteger("decayTimer", (int)TFC_Time.getTotalHours()+1);

		is.setTagCompound(nbt);
		return is;
	}

	public ItemStack onEaten(ItemStack is, World world, EntityPlayer player)
	{
		FoodStatsTFC foodstats = TFC_Core.getPlayerFoodStats(player);
		if(!world.isRemote && foodstats.needFood())
		{
			world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

			foodstats.eatFood(is);
			foodstats.restoreWater(player, 8000);

			TFC_Core.setPlayerFoodStats(player, foodstats);

			is = new ItemStack(TFCItems.WoodenBucketEmpty);
			is.stackTagCompound = null;
		}
		return is;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 32;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		return EnumAction.drink;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer entity)
	{
		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, entity, true);
		FoodStatsTFC fs = TFC_Core.getPlayerFoodStats(entity);
		Boolean canDrink = fs.getMaxWater(entity) - 500 > fs.waterLevel;

		if(mop == null)
		{
			if(is.getItemDamage() > 1 && canDrink)
				entity.setItemInUse(is, this.getMaxItemUseDuration(is));
		}
		else
		{
			if (mop.typeOfHit == MovingObjectType.BLOCK)
			{
				int i = mop.blockX;
				int j = mop.blockY;
				int k = mop.blockZ;
				if(world.getTileEntity(i,j,k) != null && world.getTileEntity(i,j,k) instanceof TileEntityBarrel)
				{
					TileEntityBarrel TE = (TileEntityBarrel) world.getTileEntity(i,j,k);
					if(TE.checkValidAddition(13))
						return new ItemStack(TFCItems.WoodenBucketEmpty);
				}
			}
		}
		entity.setItemInUse(is, this.getMaxItemUseDuration(is));
		return is;
	}

	@Override
	public EnumFoodGroup getFoodGroup()
	{
		return EnumFoodGroup.Dairy;
	}

	@Override
	public int getFoodID()
	{
		return -1;
	}

	@Override
	public ItemStack onDecayed(ItemStack is, World world, int i, int j, int k) 
	{
		return new ItemStack(TFCItems.WoodenBucketEmpty);
	}

	@Override
	public float getDecayRate()
	{
		return 6f;
	}
	
	@Override
	public EnumItemReach getReach(ItemStack is){
		return EnumItemReach.SHORT;
	}
}
