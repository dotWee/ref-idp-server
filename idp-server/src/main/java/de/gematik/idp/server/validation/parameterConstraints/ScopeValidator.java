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

package de.gematik.idp.server.validation.parameterConstraints;

import de.gematik.idp.error.IdpErrorType;
import de.gematik.idp.field.IdpScope;
import de.gematik.idp.server.exceptions.IdpServerException;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ScopeValidator implements ConstraintValidator<CheckScope, String> {

    @Override
    public boolean isValid(final String rawScopes, final ConstraintValidatorContext context) {
        final String[] scopes = rawScopes.split(" ");
        final long numberOfValidScopes = Stream.of(scopes)
            .map(IdpScope::fromJwtValue)
            .filter(Optional::isPresent)
            .count();

        if (numberOfValidScopes != scopes.length) {
            throw new IdpServerException(1030, IdpErrorType.INVALID_SCOPE, "Fachdienst ist unbekannt");
        }

        if ((numberOfValidScopes < 2) || !rawScopes.contains(IdpScope.OPENID.getJwtValue())) {
            throw new IdpServerException(1022, IdpErrorType.INVALID_SCOPE, "scope ist ungültig");
        }

        return true;
    }
}

