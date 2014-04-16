package TFC.Items.Tools;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import TFC.TFCBlocks;
import TFC.TFCItems;
import TFC.API.Enums.EnumItemReach;
import TFC.API.Enums.EnumSize;
import TFC.API.Util.Helper;
import TFC.Items.ItemTerra;
import cpw.mods.fml.common.eventhandler.Event;

public class ItemCustomRedSteelBucket extends ItemTerra
{
	/** field for checking if the bucket has been filled. */
	private Block isFull;

	public ItemCustomRedSteelBucket(Block par2)
	{
		super();
		this.isFull = par2;
		this.setFolder("tools/");
	}

	@Override
	public EnumSize getSize(ItemStack is) {
		// TODO Auto-generated method stub
		return EnumSize.LARGE;
	}

	@Override
	public boolean canStack() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer par3EntityPlayer)
	{
		float var4 = 1.0F;
		double var5 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * var4;
		double var7 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * var4 + 1.62D - par3EntityPlayer.yOffset;
		double var9 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * var4;
		boolean var11 = this.isFull != Blocks.flowing_water;
		MovingObjectPosition var12 = Helper.getMovingObjectPositionFromPlayer(world, par3EntityPlayer, var11);

		if (var12 == null)
		{
			return par1ItemStack;
		}
		else
		{
			if (var12.typeOfHit == MovingObjectType.BLOCK)
			{
				int i = var12.blockX;
				int j = var12.blockY;
				int k = var12.blockZ;

				if (!world.canMineBlock(par3EntityPlayer, i, j, k))
				{
					return par1ItemStack;
				}

				if (this.isFull != Blocks.flowing_water)
				{
					if (!par3EntityPlayer.canPlayerEdit(i, j, k, var12.sideHit, par1ItemStack))
					{
						return par1ItemStack;
					}

					FillBucketEvent event = new FillBucketEvent(par3EntityPlayer, par1ItemStack, world, var12);
					if (event.isCanceled())
					{
						return par1ItemStack;
					}

					if (event.getResult() == Event.Result.ALLOW)
					{
						return event.result;
					}

					if ((world.getBlock(i, j, k) == Blocks.water) && world.getBlockMetadata(i, j, k) == 0)
					{
						world.setBlockToAir(i, j, k);

						if (par3EntityPlayer.capabilities.isCreativeMode)
						{
							return par1ItemStack;
						}

						if (--par1ItemStack.stackSize <= 0)
						{
							return new ItemStack(TFCItems.RedSteelBucketSaltWater);
						}

						if (!par3EntityPlayer.inventory.addItemStackToInventory(new ItemStack(TFCItems.RedSteelBucketSaltWater)))
						{
							par3EntityPlayer.dropItem(TFCItems.RedSteelBucketSaltWater, 1);
						}

						return par1ItemStack;
					}
					else if ((world.getBlock(i, j, k) == TFCBlocks.FreshWaterStill || world.getBlock(i, j, k) == TFCBlocks.HotWaterStill ) && world.getBlockMetadata(i, j, k) == 0)
					{
						world.setBlockToAir(i, j, k);

						if (par3EntityPlayer.capabilities.isCreativeMode)
							return par1ItemStack;

						if (--par1ItemStack.stackSize <= 0)
							return new ItemStack(TFCItems.RedSteelBucketWater);

						if (!par3EntityPlayer.inventory.addItemStackToInventory(new ItemStack(TFCItems.RedSteelBucketWater)))
							par3EntityPlayer.entityDropItem(new ItemStack(TFCItems.RedSteelBucketWater, 1, 0), 0);

						return par1ItemStack;
					}
				}
				else
				{
					if (this.isFull != Blocks.flowing_water)
					{
						return new ItemStack(TFCItems.RedSteelBucketEmpty);
					}

					if (var12.sideHit == 0)
					{
						--j;
					}

					if (var12.sideHit == 1)
					{
						++j;
					}

					if (var12.sideHit == 2)
					{
						--k;
					}

					if (var12.sideHit == 3)
					{
						++k;
					}

					if (var12.sideHit == 4)
					{
						--i;
					}

					if (var12.sideHit == 5)
					{
						++i;
					}

					if (!par3EntityPlayer.canPlayerEdit(i, j, k, var12.sideHit, par1ItemStack))
					{
						return par1ItemStack;
					}

					if (this.handleWaterInHell(world, var5, var7, var9, i, j, k) && !par3EntityPlayer.capabilities.isCreativeMode)
					{
						return new ItemStack(TFCItems.RedSteelBucketEmpty);
					}
				}
			}
		}
		return par1ItemStack;
	}

	public boolean handleWaterInHell(World par1World, double par2, double par4, double par6, int par8, int par9, int par10)
	{
		if (this.isFull != Blocks.flowing_water)
		{
			return false;
		}
		else if (!par1World.isAirBlock(par8, par9, par10) && par1World.getBlock(par8, par9, par10).getMaterial().isSolid())
		{
			return false;
		}
		else
		{
			if (par1World.provider.isHellWorld && this.isFull == Blocks.flowing_water)
			{
				par1World.playSoundEffect(par2 + 0.5D, par4 + 0.5D, par6 + 0.5D, "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);

				for (int var11 = 0; var11 < 8; ++var11)
				{
					par1World.spawnParticle("largesmoke", par8 + Math.random(), par9 + Math.random(), par10 + Math.random(), 0.0D, 0.0D, 0.0D);
				}
			}
			else
			{
				par1World.setBlock(par8, par9, par10, this.isFull);
			}

			return true;
		}
	}

	@Override
	public EnumItemReach getReach(ItemStack is)
	{
		return EnumItemReach.SHORT;
	}
}
