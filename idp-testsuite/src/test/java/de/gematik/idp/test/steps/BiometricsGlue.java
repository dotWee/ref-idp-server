package de.gematik.idp.test.steps;

import de.gematik.idp.test.steps.model.Context;
import de.gematik.idp.test.steps.model.ContextKey;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import net.thucydides.core.annotations.Steps;

@Slf4j
public class BiometricsGlue {

    @Steps
    IdpBiometricsSteps biosteps;

    @When("I create a device information token with")
    public void iCreateADeviceInformationTokenWith(final DataTable data) {
        Context.getThreadContext().put(ContextKey.DEVICE_INFO, biosteps.getMapFromDatatable(data));
    }

    @And("I create pairing data with")
    public void iCreatePairingDataWith(final DataTable data) {
        Context.getThreadContext().put(ContextKey.PAIRING_DATA, biosteps.getMapFromDatatable(data));
    }

    @And("I create authentication data with")
    public void iCreateAuthenticationDataWith(final DataTable data) {
        Context.getThreadContext().put(ContextKey.AUTHENTICATION_DATA, biosteps.getMapFromDatatable(data));
    }

    @And("I sign pairing data with {string}")
    public void iSignPairingDataWithCert(final String certFile) {
        biosteps.signPairingData(certFile);
    }

    @And("I sign authentication data with {string}")
    public void iSignAuthenticationDataWithKey(final String keyFile) {
        biosteps.signAuthenticationData(keyFile);
    }

    @When("I register the device with {string}")
    public void iRegisterTheDeviceWithCert(final String certFile) {
        biosteps.registerDeviceWithCert(certFile);
    }

    @When("I deregister the device with {string}")
    public void iDeregisterTheDeviceWithThisismykey(final String keyVerifier) {
        biosteps.deregisterDeviceWithKey(keyVerifier);
    }

    @And("I request all pairings")
    public void iRequestAllPairings() {
        biosteps.requestAllPairings();
    }
}
