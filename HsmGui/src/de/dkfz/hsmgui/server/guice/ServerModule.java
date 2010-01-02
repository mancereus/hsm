package de.dkfz.hsmgui.server.guice;

import net.customware.gwt.dispatch.server.guice.ActionHandlerModule;
import org.apache.commons.logging.Log;
import org.mortbay.log.Slf4jLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import de.dkfz.hsmgui.server.handler.SendGreetingHandler;

/**
 * Module which binds the handlers and configurations
 *
 */
public class ServerModule extends ActionHandlerModule {

	@Override
	protected void configureHandlers() {
		bindHandler(SendGreetingHandler.class);
		
//		bind(Logger.class).toProvider(LogProvider.class).in(Singleton.class);
	}
}