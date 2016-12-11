
#ifndef AFCRYPTOR_AES_H
#define AFCRYPTOR_AES_H

#if __cplusplus
extern "C" {
#endif

void AES128_ECB_encrypt(uint8_t *input, const uint8_t *key, uint8_t *output);
void AES128_ECB_decrypt(uint8_t *input, const uint8_t *key, uint8_t *output);

#if __cplusplus
}
#endif

#endif //AFCRYPTOR_AES_H
