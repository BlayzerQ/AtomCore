package com.blayzer.atomcore.util;

import java.util.logging.Logger;

public final class ModUtil {
	
	public static final String MOD_ID = "atomcore";
    public static final String VERSION = "1.3";
    public static final String NAME = "AtomCore";
    public static final Logger LOGGER = Logger.getLogger(NAME);
    private static final String PROXY_BASE = "com.blayzer.atomcore.proxy.";
    public static final String ClIENT_PROXY = PROXY_BASE + "ClientProxy";
    public static final String COMMON_PROXY = PROXY_BASE + "CommonProxy";
    public static final String SERVER_PROXY = PROXY_BASE + "ServerProxy";
    
    public static final String RESOURCE_PREFIX = ModUtil.MOD_ID.toLowerCase() + ":";

    public static final String BLOCK_CONTROLLER = "blockminer";
    public static final String BLOCK_GENERATOR = "generator";

}
