package de.dkfz.hsmgui.client.gin;

import net.customware.gwt.presenter.client.DefaultEventBus;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.gin.AbstractPresenterModule;
import net.customware.gwt.presenter.client.place.PlaceManager;

import com.google.inject.Singleton;

import de.dkfz.hsmgui.client.mvp.AppPresenter;
import de.dkfz.hsmgui.client.mvp.GreetingPresenter;
import de.dkfz.hsmgui.client.mvp.GreetingResponsePresenter;
import de.dkfz.hsmgui.client.mvp.GreetingResponseView;
import de.dkfz.hsmgui.client.mvp.GreetingView;

public class GreetingClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {		
		bind(EventBus.class).to(DefaultEventBus.class).in(Singleton.class);
		bind(PlaceManager.class).in(Singleton.class);
		
		bindPresenter(GreetingPresenter.class, GreetingPresenter.Display.class, GreetingView.class);
		bindPresenter(GreetingResponsePresenter.class, GreetingResponsePresenter.Display.class, GreetingResponseView.class);
		
		bind(AppPresenter.class).in(Singleton.class);
//		bind(CachingDispatchAsync.class);
	}
}