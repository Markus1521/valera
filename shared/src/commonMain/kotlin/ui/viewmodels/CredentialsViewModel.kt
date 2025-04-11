package ui.viewmodels

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.asitplus.wallet.app.common.WalletMain
import data.storage.StoreEntryId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CredentialsViewModel(
    val walletMain: WalletMain,
    val navigateToAddCredentialsPage: () -> Unit,
    val navigateToQrAddCredentialsPage: () -> Unit,
    val navigateToCredentialDetailsPage: (Long) -> Unit,
    val imageDecoder: (ByteArray) -> ImageBitmap?,
    val onClickLogo: () -> Unit
) {
    val storeContainer = walletMain.subjectCredentialStore.observeStoreContainer()

    fun removeStoreEntryById(storeEntryId: StoreEntryId) {
        walletMain.scope.launch(Dispatchers.IO) {
            walletMain.subjectCredentialStore.removeStoreEntryById(storeEntryId)
        }
    }
}
