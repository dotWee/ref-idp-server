/*
 * Copyright (c) 2021 gematik GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.gematik.idp.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class IdpDiscoveryDocument {

    private String authorizationEndpoint;
    private String alternativeAuthorizationEndpoint;
    private String ssoEndpoint;
    private String pairingEndpoint;
    private String tokenEndpoint;
    private String uriDisc;
    private String issuer;
    private String jwksUri;
    private long exp;
    private long nbf;
    private long iat;
    private String pukUriAuth;
    private String pukUriToken;
    private String[] subjectTypesSupported;
    private String[] idTokenSigningAlgValuesSupported;
    private String[] responseTypesSupported;
    private String[] scopesSupported;
    private String[] responseModesSupported;
    private String[] grantTypesSupported;
    private String[] acrValuesSupported;
    private String[] tokenEndpointAuthMethodsSupported;
}
