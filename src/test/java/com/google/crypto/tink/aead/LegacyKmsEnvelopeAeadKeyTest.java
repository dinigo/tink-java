// Copyright 2023 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////

package com.google.crypto.tink.aead;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.crypto.tink.util.SecretBytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class LegacyKmsEnvelopeAeadKeyTest {
  private static final AeadParameters CHACHA20POLY1305_PARAMETERS =
      ChaCha20Poly1305Parameters.create(ChaCha20Poly1305Parameters.Variant.NO_PREFIX);

  @Test
  public void createKeyAndGetProperties() throws Exception {
    LegacyKmsEnvelopeAeadParameters parameters =
        LegacyKmsEnvelopeAeadParameters.builder()
            .setKekUri("SomeKekUri")
            .setDekParsingStrategy(
                LegacyKmsEnvelopeAeadParameters.DekParsingStrategy.ASSUME_CHACHA20POLY1305)
            .setDekParametersForNewKeys(CHACHA20POLY1305_PARAMETERS)
            .build();

    LegacyKmsEnvelopeAeadKey key = LegacyKmsEnvelopeAeadKey.create(parameters);

    assertThat(key.getOutputPrefix().size()).isEqualTo(0);
    assertThat(key.getParameters()).isEqualTo(parameters);
    assertThat(key.getIdRequirementOrNull()).isNull();
  }

  @Test
  public void testEqualKey() throws Exception {
    LegacyKmsEnvelopeAeadParameters parameters1 =
        LegacyKmsEnvelopeAeadParameters.builder()
            .setKekUri("SomeKekUri")
            .setDekParsingStrategy(
                LegacyKmsEnvelopeAeadParameters.DekParsingStrategy.ASSUME_CHACHA20POLY1305)
            .setDekParametersForNewKeys(CHACHA20POLY1305_PARAMETERS)
            .build();
    LegacyKmsEnvelopeAeadParameters parameters1Copy =
        LegacyKmsEnvelopeAeadParameters.builder()
            .setKekUri("SomeKekUri")
            .setDekParsingStrategy(
                LegacyKmsEnvelopeAeadParameters.DekParsingStrategy.ASSUME_CHACHA20POLY1305)
            .setDekParametersForNewKeys(CHACHA20POLY1305_PARAMETERS)
            .build();
    LegacyKmsEnvelopeAeadParameters parameters2 =
        LegacyKmsEnvelopeAeadParameters.builder()
            .setKekUri("someOtherKekUri")
            .setDekParsingStrategy(
                LegacyKmsEnvelopeAeadParameters.DekParsingStrategy.ASSUME_CHACHA20POLY1305)
            .setDekParametersForNewKeys(CHACHA20POLY1305_PARAMETERS)
            .build();

    LegacyKmsEnvelopeAeadKey key1 = LegacyKmsEnvelopeAeadKey.create(parameters1);
    LegacyKmsEnvelopeAeadKey key1Copy = LegacyKmsEnvelopeAeadKey.create(parameters1Copy);
    LegacyKmsEnvelopeAeadKey key2 = LegacyKmsEnvelopeAeadKey.create(parameters2);

    assertTrue(key1.equalsKey(key1Copy));
    assertFalse(key1.equalsKey(key2));
  }

  @Test
  public void testDifferentKeyTypesEquality_fails() throws Exception {
    LegacyKmsEnvelopeAeadParameters parameters =
        LegacyKmsEnvelopeAeadParameters.builder()
            .setKekUri("SomeKekUri")
            .setDekParsingStrategy(
                LegacyKmsEnvelopeAeadParameters.DekParsingStrategy.ASSUME_CHACHA20POLY1305)
            .setDekParametersForNewKeys(CHACHA20POLY1305_PARAMETERS)
            .build();
    LegacyKmsEnvelopeAeadKey key = LegacyKmsEnvelopeAeadKey.create(parameters);

    XChaCha20Poly1305Key xChaCha20Poly1305Key =
        XChaCha20Poly1305Key.create(SecretBytes.randomBytes(32));

    assertThat(key.equalsKey(xChaCha20Poly1305Key)).isFalse();
  }
}
