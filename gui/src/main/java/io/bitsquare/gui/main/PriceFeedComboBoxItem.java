package io.bitsquare.gui.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PriceFeedComboBoxItem {
    private static final Logger log = LoggerFactory.getLogger(PriceFeedComboBoxItem.class);

    public final String currencyCode;
    public final StringProperty displayStringProperty = new SimpleStringProperty();

    public PriceFeedComboBoxItem(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setDisplayString(String displayString) {
        this.displayStringProperty.set(displayString);
    }
}
