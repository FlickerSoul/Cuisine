package snownee.cuisine.tiles;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.cuisine.api.process.BasinInteracting;
import snownee.cuisine.api.process.BasinInteracting.Output;
import snownee.cuisine.api.process.CuisineProcessingRecipeManager;
import snownee.cuisine.api.process.Processing;
import snownee.kiwi.tile.TileInventoryBase;
import snownee.kiwi.util.NBTHelper.Tag;

public class TileBasin extends TileInventoryBase
{
    public FluidTank tank = new FluidTank(8000)
    {
        @Override
        protected void onContentsChanged()
        {
            TileBasin.this.onContentsChanged(0);
        }

        @Override
        public boolean canFillFluidType(FluidStack fluid)
        {
            if (fluid == null || !canFill() || fluid.getFluid().isGaseous(fluid) || fluid.getFluid().isLighterThanAir())
            {
                return false;
            }
            return TileBasin.this.getClass() != TileBasin.class || fluid.getFluid().getTemperature(fluid) < 500;
        }
    };
    public int tickCheckThrowing = 0;
    private float renderingAmount = 0;
    boolean squeezingFailed = false;

    public TileBasin()
    {
        super(1);
        tank.setTileEntity(this);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        }
        return super.getCapability(capability, facing);
    }

    public void spillFluids()
    {
        FluidEvent.fireEvent(new FluidEvent.FluidSpilledEvent(tank.getFluid(), world, pos));
    }

    @SideOnly(Side.CLIENT)
    public FluidStack getFluidForRendering(float partialTicks)
    {
        final FluidStack actual = tank.getFluid();
        if (actual == null)
        {
            renderingAmount = 0;
            return null;
        }
        float delta = actual.amount - renderingAmount;
        if (Math.abs(delta) < 40)
        {
            renderingAmount = actual.amount;
        }
        else
        {
            renderingAmount += delta * partialTicks * 0.1;
        }
        return new FluidStack(actual, (int) renderingAmount);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        tank.readFromNBT(compound.getCompoundTag("tank"));
        if (tank.getFluid() != null)
        {
            renderingAmount = tank.getFluid().amount;
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
        compound.setBoolean("squeezingFailed", squeezingFailed);
        return compound;
    }

    @Override
    protected void readPacketData(NBTTagCompound data)
    {
        super.readPacketData(data);
        tank.readFromNBT(data.getCompoundTag("tank"));
        if (data.hasKey("squeezingFailed", Tag.BYTE))
        {
            squeezingFailed = data.getBoolean("squeezingFailed");
        }
    }

    @Override
    protected NBTTagCompound writePacketData(NBTTagCompound data)
    {
        super.writePacketData(data);
        data.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
        return writeToNBT(data);
    }

    public void process(CuisineProcessingRecipeManager<BasinInteracting> recipeManager, ItemStack input, boolean simulated)
    {
        if (squeezingFailed && recipeManager == Processing.SQUEEZING)
        {
            return;
        }
        if (input.isEmpty())
        {
            squeezingFailed = true;
            return;
        }
        FluidStack fluid = tank.getFluid();
        BasinInteracting recipe = recipeManager.findRecipe(input, fluid);
        if (recipe != null)
        {
            Output output = recipe.getOutput(input, fluid, world.rand);
            if (output.fluid != null)
            {
                if (output.fluid.amount > tank.getCapacity())
                {
                    squeezingFailed = true;
                    return;
                }
                if (output.fluid.amount <= 0)
                {
                    output.fluid = null;
                }
                if (!simulated)
                {
                    tank.setFluid(output.fluid);
                }
            }
            if (!simulated)
            {
                if (!output.item.isEmpty())
                {
                    InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), output.item);
                }
                recipe.consumeInput(input, fluid, world.rand);
            }
            refresh();
        }
        else
        {
            squeezingFailed = true;
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return BasinInteracting.isKnownInput(Processing.SQUEEZING, stack);
    }

    @Override
    public void onContentsChanged(int slot)
    {
        squeezingFailed = false;
        refresh();
    }

    public FluidStack getCurrentFluidContent()
    {
        FluidStack content = this.tank.getFluid();
        if (content == null)
        {
            return null;
        }
        else
        {
            // Never assume people won't do bad things such as manipulating the return value
            return content.copy();
        }
    }

}
