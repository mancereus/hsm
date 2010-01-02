package de.dkfz.hsmgui.server.handler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import org.apache.commons.logging.Log;
import org.mortbay.log.Slf4jLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.dkfz.hsmgui.shared.rpc.SendGreeting;
import de.dkfz.hsmgui.shared.rpc.SendGreetingResult;

public class SendGreetingHandler implements ActionHandler<SendGreeting, SendGreetingResult> {
//	private final Logger logger = LoggerFactory.getLogger(SendGreetingHandler.class);
	private final Provider<ServletContext> servletContext;
	private final Provider<HttpServletRequest> servletRequest;

	@Inject
	public SendGreetingHandler(
				   final Provider<ServletContext> servletContext,				   
				   final Provider<HttpServletRequest> servletRequest) {
		this.servletContext = servletContext;
		this.servletRequest = servletRequest;
	}

	@Override
	public SendGreetingResult execute(final SendGreeting action,
					  final ExecutionContext context) throws ActionException {
		final String name = action.getName();
		 
		try {
			String serverInfo = servletContext.get().getServerInfo();
			
			String userAgent = servletRequest.get().getHeader("User-Agent");
			
			final String message = "Hello, " + name + "!<br><br>I am running " + serverInfo
					+ ".<br><br>It looks like you are using:<br>" + userAgent;

			
			//final String message = "Hello " + action.getName(); 
			
			return new SendGreetingResult(name, message);
		}
		catch (Exception cause) {
//			logger.info("Unable to send message");
			
			throw new ActionException(cause);
		}
	}

	@Override
	public void rollback(final SendGreeting action,
			     final SendGreetingResult result,
			     final ExecutionContext context) throws ActionException {
		// Nothing to do here
	}
	
	@Override
	public Class<SendGreeting> getActionType() {
		return SendGreeting.class;
	}
}