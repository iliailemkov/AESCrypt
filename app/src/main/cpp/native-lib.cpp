#include <jni.h>
#include <string>
#include "aes.h"

uint8_t AES_KEY[16] = { (uint8_t) 0x00, (uint8_t) 0x01, (uint8_t) 0x02, (uint8_t) 0x03,
                        (uint8_t) 0x04, (uint8_t) 0x05, (uint8_t) 0x06, (uint8_t) 0x07,
                        (uint8_t) 0x08, (uint8_t) 0x09, (uint8_t) 0x0a, (uint8_t) 0x0b,
                        (uint8_t) 0x0c, (uint8_t) 0x0d, (uint8_t) 0x0e, (uint8_t) 0x0f };

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_beardie_aescrypt_MyFile_decryptData(JNIEnv *env, jobject instance,
                                                    jbyteArray encr_, jint length) {
    jbyte *encr = env->GetByteArrayElements(encr_, NULL);

    int op = length/16;

    uint8_t *buffer = new uint8_t[length];

    for (int i = 0; i < op; i++){
        uint8_t *d = ((uint8_t*) encr) + 16 * i;
        uint8_t *b = ((uint8_t*) buffer) + 16 * i;
        AES128_ECB_decrypt(d, AES_KEY, b);
    }

    env->ReleaseByteArrayElements(encr_, encr, 0);
    jbyteArray ret = env->NewByteArray(length);
    env->SetByteArrayRegion(ret, 0, length, (jbyte *) buffer);

    delete[] buffer;
    return ret;
}

extern "C"

JNIEXPORT jbyteArray JNICALL
Java_com_example_beardie_aescrypt_MyFile_encryptData(JNIEnv *env, jobject instance,
                                                    jbyteArray decr_, jint length) {
    jbyte *decr = env->GetByteArrayElements(decr_, NULL);

    uint8_t *buffer = new uint8_t[length];

    int op = length/16;


    for (int i = 0; i < op; i++){
        uint8_t *d = ((uint8_t*) decr) + 16*i;
        uint8_t *b = ((uint8_t*) buffer) + 16*i;
        AES128_ECB_encrypt(d, AES_KEY, b);
    }

    env->ReleaseByteArrayElements(decr_, decr, 0);
    jbyteArray ret = env->NewByteArray(length);
    env->SetByteArrayRegion(ret, 0, length, (jbyte *) buffer);

    delete[] buffer;
    return ret;
}