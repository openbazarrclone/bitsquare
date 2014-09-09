/*
 * This file is part of Bitsquare.
 *
 * Bitsquare is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bitsquare is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bitsquare. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bitsquare.gui.pm.account.content;

import io.bitsquare.btc.WalletFacade;
import io.bitsquare.gui.PresentationModel;
import io.bitsquare.gui.model.account.content.RegistrationModel;
import io.bitsquare.locale.BSResources;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.Coin;

import com.google.inject.Inject;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.bitsquare.gui.util.BSFormatter.formatCoinWithCode;

public class RegistrationPM extends PresentationModel<RegistrationModel> {
    private static final Logger log = LoggerFactory.getLogger(RegistrationPM.class);

    // Those are needed for the addressTextField
    public final ObjectProperty<Address> address = new SimpleObjectProperty<>();
    public final BooleanProperty isPayButtonDisabled = new SimpleBooleanProperty(true);
    public final StringProperty requestPlaceOfferErrorMessage = new SimpleStringProperty();
    public final BooleanProperty showTransactionPublishedScreen = new SimpleBooleanProperty();


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Inject
    private RegistrationPM(RegistrationModel model) {
        super(model);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void initialized() {
        super.initialized();

        if (model.addressEntry != null) {
            address.set(model.addressEntry.getAddress());
        }

        model.isWalletFunded.addListener((ov, oldValue, newValue) -> {
            if (newValue)
                validateInput();
        });
        validateInput();

        model.payFeeSuccess.addListener((ov, oldValue, newValue) -> isPayButtonDisabled.set(newValue));

        requestPlaceOfferErrorMessage.bind(model.payFeeErrorMessage);
        showTransactionPublishedScreen.bind(model.payFeeSuccess);
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void activate() {
        super.activate();
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void deactivate() {
        super.deactivate();
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void terminate() {
        super.terminate();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // UI actions (called by CB)
    ///////////////////////////////////////////////////////////////////////////////////////////

    public void payFee() {
        model.payFeeErrorMessage.set(null);
        model.payFeeSuccess.set(false);

        isPayButtonDisabled.set(true);

        model.payFee();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Getters (called by CB)
    ///////////////////////////////////////////////////////////////////////////////////////////

    public WalletFacade getWalletFacade() {
        return model.getWalletFacade();
    }

    public Coin getFeeAsCoin() {
        return model.getFeeAsCoin();
    }

    public String getAddressAsString() {
        return model.addressEntry != null ? model.addressEntry.getAddress().toString() : "";
    }

    public String getPaymentLabel() {
        return BSResources.get("Bitsquare account registration fee");
    }

    public String getFeeAsString() {
        return formatCoinWithCode(model.getFeeAsCoin());
    }

    public String getTransactionId() {
        return model.getTransactionId();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    // Private
    ///////////////////////////////////////////////////////////////////////////////////////////

    private void validateInput() {
        isPayButtonDisabled.set(!(model.isWalletFunded.get()));
    }

}