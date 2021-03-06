package com.blayzer.atomcore.tileentities.modules;

import cofh.redstoneflux.api.IEnergyReceiver;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.blayzer.atomcore.tileentities.Property;

public class EnergyDispenserModule extends Module {

    //TODO add advanced dispersal, atm to one machine

    private final TileEntity te;
    private final int maxOutput;
    private final ArrayList<TileEntity> neighbours;
    private final Map<TileEntity, EnumFacing> neighbourFaces;

    public EnergyDispenserModule(TileEntity te, int maxOutput) {
        this.te = te;
        this.maxOutput = maxOutput;
        neighbours = new ArrayList<>();
        neighbourFaces = new HashMap<>();
        refreshNeighbours();
    }


    @Override
    public boolean tick() {
        super.tick();
        if (atTick(8)) {
            refreshNeighbours();
        }
        return disperseEnergy();
    }

    private boolean disperseEnergy() {
        boolean sync = false;
        IEnergyStorage storage = (IEnergyStorage) this.te;
        if (storage.getEnergyStored() > 0)
            if (!neighbours.isEmpty())
                for (TileEntity te : neighbours)
                    if (sendEnergyTo(te, neighbourFaces.get(te)))
                        sync = true;
        return sync;
//            ((TileEntityBase) te).markDirty(true);
    }

    /**
     * @return energy sent
     */
    private boolean sendEnergyTo(TileEntity neighbour, EnumFacing facing) {
        IEnergyStorage localStorage = (IEnergyStorage) this.te;

        if (neighbour.hasCapability(CapabilityEnergy.ENERGY, facing) && neighbour instanceof IEnergyStorage) {
            IEnergyStorage storage = (IEnergyStorage) neighbour;
            if (storage.canReceive()) {
                int receive = storage.receiveEnergy(Math.min(localStorage.getEnergyStored(), maxOutput), false);
                if (receive > 0) {
                    Property.Energy syncableEnergy = (Property.Energy) this.te;
                    return syncableEnergy.changeEnergy(-receive, false);
                }
            }else {
                return false;
            }
        } else if (neighbour instanceof IEnergyReceiver) {
            IEnergyReceiver storage = (IEnergyReceiver) neighbour;
            if (storage.canConnectEnergy(facing)) {
                int receive = storage.receiveEnergy(facing, Math.min(localStorage.getEnergyStored(), maxOutput), false);
                if (receive > 0) {
                    Property.Energy syncableEnergy = (Property.Energy) this.te;
                    return syncableEnergy.changeEnergy(-receive, false);
                }
            }else {
                return false;
            }
        } else if (neighbour.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, facing)) {
            ITeslaConsumer storage = (ITeslaConsumer) neighbour;
            long receive = storage.givePower(Math.min(localStorage.getEnergyStored(), maxOutput), false);
            if (receive > 0) {
                Property.Energy syncableEnergy = (Property.Energy) this.te;
                return syncableEnergy.changeEnergy(-(int) receive, false);
            }else {
                return false;
            }
        }
        return false;
    }

    private void refreshNeighbours() {
        neighbours.clear();
        neighbourFaces.clear();
        BlockPos pos = te.getPos();
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos neighbourPos = pos.offset(facing);
            TileEntity tileEntity;
            try {
                tileEntity = te.getWorld().getTileEntity(neighbourPos);
                if (tileEntity instanceof IEnergyStorage || tileEntity instanceof IEnergyReceiver || tileEntity instanceof ITeslaConsumer) {
                    neighbours.add(tileEntity);
                    neighbourFaces.put(tileEntity, facing.getOpposite());
                }
            } catch (NullPointerException e) {
            }
        }
    }


}
