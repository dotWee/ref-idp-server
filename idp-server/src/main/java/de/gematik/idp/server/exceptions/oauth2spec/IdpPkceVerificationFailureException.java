/*
 *  Copyright 2023 gematik GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.gematik.idp.server.exceptions.oauth2spec;

import de.gematik.idp.error.IdpErrorType;
import de.gematik.idp.server.exceptions.IdpServerException;
import org.springframework.http.HttpStatus;

public class IdpPkceVerificationFailureException extends IdpServerException {

  private static final long serialVersionUID = -8208966997249616972L;

  public IdpPkceVerificationFailureException(final String message) {
    super(message, IdpErrorType.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
  }
}
