package com.grim3212.assorted.tools.common.handler;

import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TagLoadListener {

	@SubscribeEvent
	public void tagsUpdatedEvent(TagsUpdatedEvent event) {
		ToolsConfig.COMMON.init();
	}

}
