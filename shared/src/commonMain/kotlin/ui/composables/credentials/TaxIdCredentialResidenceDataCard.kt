package ui.composables.credentials

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.PersonalDataCategory
import data.credentials.TaxIdCredentialAdapter
import ui.composables.AttributeRepresentation

@Composable
fun TaxIdCredentialResidenceDataCard(
    credentialAdapter: TaxIdCredentialAdapter,
    modifier: Modifier = Modifier,
) {
    CredentialDetailCard(
        credentialScheme = credentialAdapter.scheme,
        personalDataCategory = PersonalDataCategory.ResidenceData,
        credentialAdapter = credentialAdapter,
        modifier = modifier,
    ) {
        TaxIdCredentialResidenceDataCardContent(
            credentialAdapter = credentialAdapter,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
        )
    }
}

@Composable
fun TaxIdCredentialResidenceDataCardContent(
    credentialAdapter: TaxIdCredentialAdapter, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier,
    ) {
        val spacingModifier = Modifier.padding(bottom = 4.dp)
        credentialAdapter.residentAddress?.let {
            AttributeRepresentation(it, modifier = spacingModifier)
        }
    }
}