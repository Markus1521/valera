package ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import at.asitplus.wallet.app.common.WalletMain

class SigningViewModel(
    val navigateUp: () -> Unit,
    val onQrScanned: (String) -> Unit,
    val walletMain: WalletMain,
    val onClickLogo: () -> Unit,
    val onClickSettings: () -> Unit
) {
    var isLoading by mutableStateOf(false)
}