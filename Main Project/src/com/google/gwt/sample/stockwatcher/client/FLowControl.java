package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

public class FLowControl {
	private static FLowControl instance;

	private FLowControl() {
	}

	public static void go(Composite c) {
		if (instance == null)
			instance = new FLowControl(); // not sure why we need this yet since
											// everything is static.
		RootPanel.get("application").clear();
		RootPanel.get("application").getElement().getStyle()
				.setPosition(Position.RELATIVE); // not sure why, but GWT throws
													// an exception without
													// this. Adding to CSS
													// doesn't work.
		// add, determine height/width, center, then move. height/width are
		// unknown until added to document. Catch-22!
		RootPanel.get("application").add(c);
		int left = Window.getClientWidth() / 2 - c.getOffsetWidth() / 2; // find
																			// center
		int top = Window.getClientHeight() / 2 - c.getOffsetHeight() / 2;
		RootPanel.get("application").setWidgetPosition(c, left, top);
		History.newItem(c.getTitle()); // TODO: need to change and implement (or
										// override) this method on each screen
	}

}