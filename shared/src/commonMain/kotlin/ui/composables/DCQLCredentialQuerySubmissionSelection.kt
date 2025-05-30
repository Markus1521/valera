package ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import at.asitplus.openid.dcql.DCQLCredentialSubmissionOption
import at.asitplus.wallet.lib.agent.SubjectCredentialStore
import at.asitplus.wallet.lib.data.rfc.tokenStatusList.primitives.TokenStatus


@Composable
fun DCQLCredentialQuerySubmissionSelection(
    selectionOptions: List<DCQLCredentialSubmissionOption<SubjectCredentialStore.StoreEntry>>,
    checkRevocationStatus: suspend (SubjectCredentialStore.StoreEntry) -> TokenStatus?,
    currentlySelectedOptionIndex: Int?,
    onChangeSelection: (Int?) -> Unit,
    decodeToBitmap: (ByteArray) -> ImageBitmap?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        selectionOptions.forEachIndexed { index, option ->
            val isSelected = index == currentlySelectedOptionIndex
            DCQLCredentialQuerySubmissionSelectionOption(
                checkRevocationStatus = checkRevocationStatus,
                isSelected = isSelected,
                onToggleSelection = { onChangeSelection(if (isSelected) null else index) },
                decodeToBitmap = decodeToBitmap,
                option = option,
            )
        }
    }
}
