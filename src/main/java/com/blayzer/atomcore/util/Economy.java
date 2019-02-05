package com.blayzer.atomcore.util;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import com.blayzer.atomcore.AtomCore;

public class Economy {
	
	public static EconomyService economyService;
	
	private static void economyInit(){
        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
        economyService = serviceOpt.get();
	}
	
	public static boolean isEnabled() {
		if(economyService != null)
			return true;
		else
			return false;
	}
	
	public static BigDecimal getBalance(UUID playerUUID) {
		if(!isEnabled())
			economyInit();
        Optional<UniqueAccount> uOpt = economyService.getOrCreateAccount(playerUUID);
        if (uOpt.isPresent()) {
            UniqueAccount acc = uOpt.get();
            BigDecimal balance = acc.getBalance(economyService.getDefaultCurrency());
            return balance;
        }
		return null;
	}
	
	public static boolean giveMoney(UUID playerUUID, BigDecimal amount) {
		if(!isEnabled())
			economyInit();
        Optional<UniqueAccount> uOpt = economyService.getOrCreateAccount(playerUUID);
        if (uOpt.isPresent()) {
            UniqueAccount acc = uOpt.get();
            acc.deposit(economyService.getDefaultCurrency(), amount, Cause.of(EventContext.builder().build(), AtomCore.instance));
            return true;
        }
        return false;
	}
	
	public static boolean takeMoney(UUID playerUUID, BigDecimal amount) {
		if(!isEnabled())
			economyInit();
        Optional<UniqueAccount> uOpt = economyService.getOrCreateAccount(playerUUID);
        if (uOpt.isPresent()) {
            UniqueAccount acc = uOpt.get();
            acc.withdraw(economyService.getDefaultCurrency(), amount, null);
            return true;
        }
        return false;
	}

}
