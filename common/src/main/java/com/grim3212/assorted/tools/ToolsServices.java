package com.grim3212.assorted.tools;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.tools.services.IToolsHelper;

public class ToolsServices {
    public static final IToolsHelper TOOLS = Services.load(IToolsHelper.class);
}
