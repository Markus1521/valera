package ui.views.iso.verifier

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import at.asitplus.valera.resources.Res
import at.asitplus.valera.resources.info_text_waiting_for_response
import at.asitplus.wallet.app.common.iso.transfer.BluetoothInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.multipaz.compose.permissions.rememberBluetoothPermissionState
import ui.viewmodels.iso.VerifierState
import ui.viewmodels.iso.VerifierViewModel
import org.jetbrains.compose.resources.stringResource
import ui.views.LoadingView

@Composable
fun VerifierView(
    vm: VerifierViewModel,
    onError: (Throwable) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val verifierState by vm.verifierState.collectAsState()

    val isBluetoothEnabled = BluetoothInfo().isBluetoothEnabled()
    if (!isBluetoothEnabled) {
        // TODO if bluetooth is not available, NFC data transfer should still work
        //vm.handleError(stringResource(Res.string.error_bluetooth_unavailable))
    }

    val blePermissionState = rememberBluetoothPermissionState()
    if (!blePermissionState.isGranted) {
        CoroutineScope(Dispatchers.Main).launch {
            blePermissionState.launchPermissionRequest()
        }
    }

    when (verifierState) {
        VerifierState.INIT -> VerifierDocumentSelectionView(vm, bottomBar)
        VerifierState.SELECT_CUSTOM_REQUEST -> VerifierCustomSelectionView(vm)
        VerifierState.QR_ENGAGEMENT -> VerifierQrEngagementView(vm)
        VerifierState.WAITING_FOR_RESPONSE ->
            LoadingView(stringResource(Res.string.info_text_waiting_for_response), vm.navigateUp)

        VerifierState.PRESENTATION -> VerifierPresentationView(vm)
        VerifierState.ERROR -> onError(Throwable(vm.errorMessage.value))
    }
}
