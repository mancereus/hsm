package de.dkfz.hsmgui.client.gin;

import net.customware.gwt.dispatch.client.gin.ClientDispatchModule;
import net.customware.gwt.presenter.client.place.PlaceManager;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import de.dkfz.hsmgui.client.mvp.AppPresenter;

@GinModules({ ClientDispatchModule.class, GreetingClientModule.class })
public interface GreetingGinjector extends Ginjector {

	AppPresenter getAppPresenter();

	PlaceManager getPlaceManager();

}