package com.grim3212.assorted.decor;

import com.grim3212.assorted.decor.services.IToolsHelper;
import com.grim3212.assorted.lib.platform.Services;

public class ToolsServices {
    public static final IToolsHelper TOOLS = Services.load(IToolsHelper.class);
}
