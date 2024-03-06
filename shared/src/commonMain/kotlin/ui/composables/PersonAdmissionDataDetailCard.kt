package ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class AdmissionData(
    val carModel: String?,
    val licensePlateNumber: String?,
)

@Composable
fun PersonAdmissionDataDetailCard(
    admissionData: AdmissionData,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            PersonAttributeDetailCardHeading(
                iconText = PersonalDataCategory.AdmissionData.iconText,
                title = PersonalDataCategory.AdmissionData.categoryTitle,
            )

            val textGap = 4.dp
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                if (admissionData.carModel != null) {
                    Text(
                        admissionData.carModel,
                        modifier = Modifier.padding(bottom = textGap),
                    )
                }
                if (admissionData.licensePlateNumber != null) {
                    Text(
                        admissionData.licensePlateNumber,
                        modifier = Modifier.padding(bottom = textGap),
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp - textGap))
        }
    }
}