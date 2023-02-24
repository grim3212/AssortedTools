package com.grim3212.assorted.decor;

import com.grim3212.assorted.decor.services.IConfigHelper;
import com.grim3212.assorted.lib.platform.Services;

public class ToolsServices {
    public static final IConfigHelper CONFIG = Services.load(IConfigHelper.class);
}
