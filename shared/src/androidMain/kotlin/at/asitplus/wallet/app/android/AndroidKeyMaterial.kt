package at.asitplus.wallet.app.android

import at.asitplus.signum.supreme.os.AndroidKeystoreSigner
import at.asitplus.wallet.app.common.WalletKeyMaterial
import at.asitplus.wallet.lib.agent.KeyMaterial

/**
 * Fork of the vclib `DefaultCryptoService`, but with biometric callbacks to authenticate the user
 *
 */
class AndroidKeyMaterial(keyMaterial: KeyMaterial) : WalletKeyMaterial(keyMaterial) {

    override suspend fun sign(data: ByteArray) =
        if (keyMaterial.getUnderLyingSigner() is AndroidKeystoreSigner) {
            //This is just here to pass the prompt message
            (keyMaterial.getUnderLyingSigner() as AndroidKeystoreSigner).sign(data) {
                unlockPrompt {
                    promptText?.let { message = it }
                    promptSubtitle?.let { subtitle = it }
                    promptCancelText?.let { cancelText = it }
                }
            }
        } else super.sign(data)
}

