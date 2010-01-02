package de.dkfz.hsmgui.shared.event;

import com.google.gwt.event.shared.EventHandler;

public interface GreetingSentEventHandler extends EventHandler {

	void onGreetingSent(GreetingSentEvent event);

}