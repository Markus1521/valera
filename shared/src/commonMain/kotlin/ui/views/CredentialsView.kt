package ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.asitplus.valera.resources.Res
import at.asitplus.valera.resources.heading_label_my_data_screen
import at.asitplus.wallet.lib.agent.SubjectCredentialStore
import at.asitplus.wallet.lib.data.rfc.tokenStatusList.primitives.TokenStatus
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.resources.stringResource
import ui.composables.CustomFloatingActionMenu
import ui.composables.FloatingActionButtonHeightSpacer
import ui.composables.Logo
import ui.composables.credentials.CredentialCard
import ui.viewmodels.CredentialsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CredentialsView(
    vm: CredentialsViewModel,
    bottomBar: @Composable () -> Unit
) {
    val credentialsStatus by vm.storeContainer.map {
        CredentialState.Success(it.credentials)
    }.collectAsState(
        CredentialState.Loading
    )
    val credentialStatusesState by produceState(
        CredentialStatusesState.Loading() as CredentialStatusesState,
        credentialsStatus
    ) {
        when (val delegate = credentialsStatus) {
            is CredentialState.Loading -> value = CredentialStatusesState.Loading()
            is CredentialState.Success -> {
                val credentialsWithStatus = mutableMapOf<Long, TokenStatus?>()
                delegate.credentials.forEach { (id, credential) ->
                    credentialsWithStatus[id] = vm.walletMain.checkRevocationStatus(credential)
                    value = CredentialStatusesState.Loading(credentialsWithStatus)
                }
                value = CredentialStatusesState.Success(credentialsWithStatus)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            stringResource(Res.string.heading_label_my_data_screen),
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.headlineLarge,
                        )
                    }
                },
                actions = {
                    Logo(onClick = vm.onClickLogo)
                    Column(modifier = Modifier.clickable(onClick = vm.onClickSettings)) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = null,
                        )
                    }
                    Spacer(Modifier.width(15.dp))
                }
            )
        },
        floatingActionButton = {
            when (val it = credentialsStatus) {
                is CredentialState.Success -> {
                    if (it.credentials.isNotEmpty()) {
                        CustomFloatingActionMenu(
                            addCredential = vm.navigateToAddCredentialsPage,
                            addCredentialQr = vm.navigateToQrAddCredentialsPage
                        )
                    }
                }

                else -> {}
            }
        },
        bottomBar = { bottomBar() }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier.padding(scaffoldPadding).fillMaxSize(),
        ) {
            when (val credentialsStatusDelegate = credentialsStatus) {
                CredentialState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is CredentialState.Success -> {
                    val credentials = credentialsStatusDelegate.credentials.sortedBy { (id, credential) ->
                        credentialStatusesState.credentialStatuses[id]?.value ?: 256.toUByte()
                    }
                    if (credentials.isEmpty()) {
                        NoDataLoadedView(vm.navigateToAddCredentialsPage, vm.navigateToQrAddCredentialsPage)
                    } else {
                        LazyColumn {
                            items(
                                credentials.size,
                                key = {
                                    credentials[it].first
                                }
                            ) { index ->
                                val storeEntry = credentials[index]
                                val storeEntryIdentifier = storeEntry.first
                                val credential = storeEntry.second

                                val isTokenStatusEvaluated =
                                    storeEntryIdentifier in credentialStatusesState.credentialStatuses
                                val tokenStatus = credentialStatusesState.credentialStatuses[storeEntryIdentifier]

                                Column {
                                    CredentialCard(
                                        credential,
                                        isTokenStatusEvaluated = isTokenStatusEvaluated,
                                        tokenStatus = tokenStatus,
                                        onDelete = {
                                            vm.removeStoreEntryById(storeEntryIdentifier)
                                        },
                                        onOpenDetails = {
                                            vm.navigateToCredentialDetailsPage(storeEntryIdentifier)
                                        },
                                        imageDecoder = vm.imageDecoder,
                                        modifier = Modifier.padding(
                                            start = 16.dp,
                                            end = 16.dp,
                                            bottom = 16.dp
                                        ),
                                    )
                                }
                            }
                            item {
                                FloatingActionButtonHeightSpacer(
                                    externalPadding = 16.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private sealed interface CredentialState {
    data object Loading : CredentialState
    data class Success(
        val credentials: List<Pair<Long, SubjectCredentialStore.StoreEntry>>,
    ) : CredentialState
}

private sealed interface CredentialStatusesState {
    val credentialStatuses: Map<Long, TokenStatus?>

    data class Loading(
        override val credentialStatuses: Map<Long, TokenStatus?> = mapOf(),
    ) : CredentialStatusesState

    data class Success(
        override val credentialStatuses: Map<Long, TokenStatus?> = mapOf(),
    ) : CredentialStatusesState
}