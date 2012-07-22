package com.navigen;

import com.hotels.aat.concordion.extension.navigen.NaviGenListener;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.listener.SpecificationProcessingListener;

public class NaviGen implements ConcordionExtension {

    public void addTo(ConcordionExtender extender) {
        SpecificationProcessingListener naviGenListener = new NaviGenListener();
        extender.withSpecificationProcessingListener(naviGenListener);
    }

}
