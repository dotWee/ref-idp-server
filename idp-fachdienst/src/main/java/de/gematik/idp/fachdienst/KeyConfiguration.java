/*
 * Copyright (c) 2022 gematik GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.gematik.idp.fachdienst;

import de.gematik.idp.authentication.IdpJwtProcessor;
import de.gematik.idp.crypto.CryptoLoader;
import de.gematik.idp.crypto.model.PkiIdentity;
import de.gematik.idp.data.FederationPrivKey;
import de.gematik.idp.fachdienst.configuration.FachdienstConfiguration;
import de.gematik.idp.fachdienst.configuration.KeyConfig;
import de.gematik.idp.fachdienst.exceptions.FachdienstException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

@Configuration
@RequiredArgsConstructor
public class KeyConfiguration {

    private final ResourceLoader resourceLoader;
    private final FachdienstConfiguration fachdienstConfiguration;

    @Bean
    public FederationPrivKey sigKey() {
        return getFederationPrivKey(fachdienstConfiguration.getSigKeyConfig());
    }

    @Bean
    public IdpJwtProcessor jwtProcessor() {
        return new IdpJwtProcessor(sigKey().getIdentity());
    }

    private FederationPrivKey getFederationPrivKey(final KeyConfig keyConfiguration) {
        final Resource resource = resourceLoader.getResource(keyConfiguration.getFileName());
        try (final InputStream inputStream = resource.getInputStream()) {
            final PkiIdentity pkiIdentity = CryptoLoader.getIdentityFromP12(
                StreamUtils.copyToByteArray(inputStream), "00");

            pkiIdentity.setKeyId(Optional.ofNullable(keyConfiguration.getKeyId()));
            pkiIdentity.setUse(Optional.ofNullable(keyConfiguration.getUse()));
            return new FederationPrivKey(pkiIdentity);
        } catch (final IOException e) {
            throw new FachdienstException(
                "Error while loading Key from resource '" + keyConfiguration.getFileName() + "'", e);
        }
    }
}
