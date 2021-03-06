package com.blayzer.atomcore.tileentities;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraftforge.energy.IEnergyStorage;

interface Energy extends IEnergyStorage, ITeslaHolder {

    interface Consumer extends Energy, ITeslaConsumer, IEnergyReceiver {
    }

    interface Producer extends Energy, ITeslaProducer, IEnergyProvider {
        int getGeneration();
    }

    interface Battery extends Energy, Consumer, Producer {
    }
}
