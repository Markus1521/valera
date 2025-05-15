package ui.composables.credentials

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.asitplus.wallet.lib.agent.SubjectCredentialStore
import data.credentials.HealthIdCredentialAdapter

@Composable
fun HealthIdView(
    credential: SubjectCredentialStore.StoreEntry? = null,
    credentialAdapter: HealthIdCredentialAdapter? = null,
    modifier: Modifier = Modifier,
) {
    val credentialAdapter = remember(credential) {
        credentialAdapter ?: credential?.let {
            HealthIdCredentialAdapter.createFromStoreEntry(credential)
        } ?: throw IllegalArgumentException("Either credential or credentialAdapter must be provided.")
    }

    Column(modifier = modifier) {
        val spacingModifier = Modifier.padding(bottom = 16.dp)
        HealthIdRepresentationDataCard(
            credentialAdapter = credentialAdapter,
            modifier = spacingModifier,
        )
    }
}