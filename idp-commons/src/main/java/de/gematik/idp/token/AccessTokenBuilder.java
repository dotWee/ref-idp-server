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

package de.gematik.idp.token;

import static de.gematik.idp.field.ClaimName.*;
import static de.gematik.idp.token.TokenBuilderUtil.buildSubjectClaim;

import de.gematik.idp.IdpConstants;
import de.gematik.idp.authentication.IdpJwtProcessor;
import de.gematik.idp.authentication.JwtBuilder;
import de.gematik.idp.crypto.Nonce;
import de.gematik.idp.exceptions.IdpRuntimeException;
import de.gematik.idp.exceptions.RequiredClaimException;
import de.gematik.idp.field.ClaimName;
import de.gematik.idp.field.IdpScope;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.jose4j.jwt.NumericDate;

@Data
public class AccessTokenBuilder {

    private static final List<ClaimName> CLAIMS_TO_TAKE_FROM_AUTHENTICATION_TOKEN = List
        .of(PROFESSION_OID, GIVEN_NAME, FAMILY_NAME,
            ORGANIZATION_NAME, ID_NUMBER, CLIENT_ID, SCOPE, AUTH_TIME);
    private final IdpJwtProcessor jwtProcessor;
    private final String issuerUrl;
    private final String serverSubjectSalt;

    public JsonWebToken buildAccessToken(final JsonWebToken authenticationToken) {
        final ZonedDateTime now = ZonedDateTime.now();
        final Map<String, Object> claimsMap = new HashMap<>();
        final String clientId = authenticationToken.getBodyClaim(CLIENT_ID)
            .orElseThrow(() -> new RequiredClaimException("Unable to obtain " + CLIENT_ID.getJoseName() + "!"))
            .toString();

        CLAIMS_TO_TAKE_FROM_AUTHENTICATION_TOKEN.stream()
            .map(claimName -> Pair.of(claimName, authenticationToken.getBodyClaim(claimName)))
            .filter(pair -> pair.getValue().isPresent())
            .forEach(pair -> claimsMap.put(pair.getKey().getJoseName(), pair.getValue().get()));
        claimsMap.put(ISSUED_AT.getJoseName(), now.toEpochSecond());
        claimsMap.put(ISSUER.getJoseName(), issuerUrl);
        claimsMap.put(AUTHENTICATION_CLASS_REFERENCE.getJoseName(), IdpConstants.EIDAS_LOA_HIGH);
        claimsMap.put(AUDIENCE.getJoseName(), determineAudienceBasedOnScope(authenticationToken.getScopesBodyClaim()));
        claimsMap.put(SUBJECT.getJoseName(),
            buildSubjectClaim(
                clientId,
                authenticationToken.getStringBodyClaim(ID_NUMBER)
                    .orElseThrow(() -> new RequiredClaimException("Missing '" + ID_NUMBER.getJoseName() + "' claim!")),
                serverSubjectSalt));
        claimsMap.put(AUTHORIZED_PARTY.getJoseName(), clientId);
        claimsMap.put(JWT_ID.getJoseName(), new Nonce().getNonceAsHex(IdpConstants.JTI_LENGTH));
        claimsMap.put(AUTHENTICATION_METHODS_REFERENCE.getJoseName(),
            authenticationToken.getBodyClaim(AUTHENTICATION_METHODS_REFERENCE)
                .orElse(getAmrString()));
        claimsMap.put(EXPIRES_AT.getJoseName(), NumericDate.fromSeconds(now.plusMinutes(5).toEpochSecond()).getValue());

        final Map<String, Object> headerClaimsMap = new HashMap<>();
        headerClaimsMap.put(TYPE.getJoseName(), "at+JWT");

        return jwtProcessor.buildJwt(new JwtBuilder()
            .replaceAllBodyClaims(claimsMap)
            .replaceAllHeaderClaims(headerClaimsMap));
    }

    private static String determineAudienceBasedOnScope(final Set<IdpScope> scopesBodyClaim) {
        if (scopesBodyClaim.contains(IdpScope.PAIRING)) {
            return IdpConstants.AUDIENCE_PAIRING;
        } else if (scopesBodyClaim.contains(IdpScope.EREZEPT)) {
            return IdpConstants.AUDIENCE_EREZEPT;
        } else {
            throw new IdpRuntimeException("Could not determine Audience for scopes '" + scopesBodyClaim + "'");
        }
    }

    @SneakyThrows
    private String[] getAmrString() {
        return new String[]{"mfa", "sc", "pin"};
    }
}
