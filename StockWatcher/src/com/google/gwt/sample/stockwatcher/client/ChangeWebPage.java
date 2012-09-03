package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

public class ChangeWebPage extends Frame implements EventListener {
	public ChangeWebPage() {
		super();
		sinkEvents(Event.ONLOAD);

		addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				System.out.println(event.getSource());
				// <iframe style="visibility: visible;" id="ext-gen17"
				// src="http://..." class="gwt-Frame"></iframe>
				// however, the src attribute never changes
			}
		});
	}
	
	
	public ChangeWebPage(Event event) {
		super();
		sinkEvents(Event.ONLOAD);

		addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				System.out.println(event.getSource());
				// <iframe style="visibility: visible;" id="ext-gen17"
				// src="http://..." class="gwt-Frame"></iframe>
				// however, the src attribute never changes
			}
		});
	}
	public static native String getIframeUri(IFrameElement iframe) /*-{
    if (iframe.contentDocument !== undefined) {
        if (iframe.contentDocument.defaultView !== undefined
                && iframe.contentDocument.defaultView.location !== undefined) {
            return iframe.contentDocument.defaultView.location.href;
        } else {
            return iframe.contentDocument.URL;
        }
    } else if (iframe.contentWindow !== undefined
            && iframe.contentWindow.document !== undefined) {
        return iframe.contentWindow.document;
    } else {
        return null;
    }
}-*/;
}
