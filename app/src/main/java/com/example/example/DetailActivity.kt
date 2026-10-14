package com.example.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.example.ui.theme.ExampleTheme

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengambil data dari Intent
        val passedName =
            intent.getStringExtra("EXTRA_NAME") ?: "Guest"

        val passedSemester =
            intent.getIntExtra("EXTRA_SEMESTER", 0)

        val isActive =
            intent.getBooleanExtra("EXTRA_IS_ACTIVE", false)

        setContent {
            ExampleTheme()  {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    DetailScreen(
                        name = passedName,
                        semester = passedSemester,
                        isActive = isActive,
                        onBackClicked = {
                            finish()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun DetailScreen(
    name: String,
    semester: Int,
    isActive: Boolean,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Halaman Detail Mahasiswa",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Nama: $name",
                    fontSize = 16.sp
                )

                Text(
                    text = "Semester: $semester",
                    fontSize = 16.sp
                )

                Text(
                    text = "Status Aktif: ${
                        if (isActive) "Ya" else "Tidak"
                    }",
                    fontSize = 16.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = onBackClicked,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Kembali & Hancurkan Activity (finish)")
        }
    }
}

@Preview(
    showBackground = true,
    name = "Detail Screen Preview"
)
@Composable
fun DetailScreenPreview() {

    ExampleTheme() { }
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {

            DetailScreen(
                name = "Rahmat Ababil",
                semester = 6,
                isActive = true,
                onBackClicked = {}
            )
        }
    }

