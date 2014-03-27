package TFC.WorldGen.Generators.Trees;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import TFC.TFCBlocks;
import TFC.TileEntities.TileEntityFruitTreeWood;

public class WorldGenCustomFruitTree2 extends WorldGenerator
{
    private Block leavesBlock;
    private int metaId;

    public WorldGenCustomFruitTree2(boolean flag, Block block, int meta)
    {
        super(flag);
        leavesBlock = block;
        metaId = meta;

    }
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        if(world.isAirBlock(i, j, k) && j < 250)
        {
            world.setBlock(i, j, k, TFCBlocks.fruitTreeWood, metaId+8, 0x2);
            ((TileEntityFruitTreeWood)world.getTileEntity(i, j, k)).setTrunk(true);
            ((TileEntityFruitTreeWood)world.getTileEntity(i, j, k)).setHeight(0);
            ((TileEntityFruitTreeWood)world.getTileEntity(i, j, k)).setBirth();
        }
        if(world.isAirBlock(i, j+1, k))
        {
            world.setBlock(i, j+1, k, TFCBlocks.fruitTreeWood, metaId+8, 0x2);
            ((TileEntityFruitTreeWood)world.getTileEntity(i, j+1, k)).setTrunk(true);
            ((TileEntityFruitTreeWood)world.getTileEntity(i, j+1, k)).setHeight(1);
            ((TileEntityFruitTreeWood)world.getTileEntity(i, j+1, k)).setBirth();
        }
        if(world.isAirBlock(i, j+2, k))
        {
            world.setBlock(i, j+2, k, TFCBlocks.fruitTreeWood, metaId+8, 0x2);
            ((TileEntityFruitTreeWood)world.getTileEntity(i, j+2, k)).setTrunk(true);
            ((TileEntityFruitTreeWood)world.getTileEntity(i, j+2, k)).setHeight(2);
            ((TileEntityFruitTreeWood)world.getTileEntity(i, j+2, k)).setBirth();

            if(world.isAirBlock(i+1, j+2, k))
            {
                world.setBlock(i+1, j+2, k, TFCBlocks.fruitTreeWood, metaId+8, 0x2);
                SurroundWithLeaves(world,i+1,j+2,k);
            }
            if(world.isAirBlock(i-1, j+2, k))
            {
                world.setBlock(i-1, j+2, k, TFCBlocks.fruitTreeWood, metaId+8, 0x2);
                SurroundWithLeaves(world,i-1,j+2,k);
            }
            if(world.isAirBlock(i, j+2, k+1))
            {
                world.setBlock(i, j+2, k+1, TFCBlocks.fruitTreeWood, metaId+8, 0x2);
                SurroundWithLeaves(world,i,j+2,k+1);
            }
            if(world.isAirBlock(i, j+2, k-1))
            {
                world.setBlock(i, j+2, k-1, TFCBlocks.fruitTreeWood, metaId+8, 0x2);
                SurroundWithLeaves(world,i,j+2,k-1);
            }

        }
        if(world.isAirBlock(i, j+3, k) || world.getBlock(i, j+3, k) == leavesBlock)
        {
            world.setBlock(i, j+3, k, TFCBlocks.fruitTreeWood, metaId+8, 0x2);
            ((TileEntityFruitTreeWood)world.getTileEntity(i, j+3, k)).setTrunk(true);
            ((TileEntityFruitTreeWood)world.getTileEntity(i, j+3, k)).setHeight(3);
            ((TileEntityFruitTreeWood)world.getTileEntity(i, j+3, k)).setBirth();
            SurroundWithLeaves(world,i,j+3,k);
        }


        return true;
    }
    
    public void SurroundWithLeaves(World world, int i, int j, int k)
    {
        for (int y = 1; y >= 0; y--)
        {
            for (int x = 1; x >= -1; x--)
            {
                for (int z = 1; z >= -1; z--)
                {
                    if(world.isAirBlock(i+x, j+y, k+z)) {
                        world.setBlock(i+x, j+y, k+z, leavesBlock, metaId, 0x2);
                    }
                }
            }
        }
    }
}
