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

package de.gematik.idp.server.controllers;

import de.gematik.idp.crypto.model.PkiIdentity;
import de.gematik.idp.data.IdpKeyDescriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class IdpKey {

    private final PkiIdentity identity;

    public IdpKeyDescriptor buildJwk(final boolean addX5C) {
        return IdpKeyDescriptor.constructFromX509Certificate(identity.getCertificate(), identity.getKeyId(), addX5C);
    }
}
