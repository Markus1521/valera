package ui.viewmodels.intents

import at.asitplus.wallet.app.common.WalletMain

class SigningPreloadIntentViewModel(
    val walletMain: WalletMain,
    val uri: String,
    val onSuccess: () -> Unit,
    val onFailure: (Throwable) -> Unit
) {
    suspend fun process() {
        at.asitplus.catchingUnwrapped {
            walletMain.signingService.resumePreloadCertificate(url = uri)
        }.onSuccess {
            onSuccess()
        }.onFailure {
            onFailure(it)
        }
    }
}