package de.dkfz.hsmgui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

import de.dkfz.hsmgui.client.gin.GreetingGinjector;
import de.dkfz.hsmgui.client.mvp.AppPresenter;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HsmGui implements EntryPoint {
	private final GreetingGinjector injector = GWT.create(GreetingGinjector.class);

	public void onModuleLoad() {
		final AppPresenter appPresenter = injector.getAppPresenter();
		appPresenter.go(RootPanel.get());

		injector.getPlaceManager().fireCurrentPlace();
	}
}
